package wah.giovann.csvhandler;

import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;
import wah.giovann.csvhandler.error.CSVParseException;

import java.io.*;
import java.util.ArrayList;

/**
 * The <code>CSVReader</code> class is responsible for reading of information from .csv files.
 * <br><br>
 *<code>CSVReader</code> parses .csv files and returns a usable <code>CSVArray</code> object for
 * data manipulation. The input file format is specified by a <code>CSVFileFormat</code> object passed to the
 * <code>CSVReader</code> constructor. Here, the format's <code>outputFileLineEnd</code> field is ignored. <code>CSVReader</code> can parse any file with specification
 * compliant with RFC 4180 (see <a href="https://en.wikipedia.org/wiki/Comma-separated_values" target="_blank">the CSV wikipedia page</a>).
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVReader {
    private final int bufSize = 256;
    private CSVFileFormat format;

    /**
     *Creates a new <code>CSVReader</code> instance that can read a .csv file based on the given input file format.
     * @param inputFormat - the input file format
     */
    public CSVReader(CSVFileFormat inputFormat) {
        this.format = inputFormat;
    }

    /**
     *Returns a <code>CSVArray</code> instance after parsing the contents of the <code>File</code> argument. The contents
     * the file must conform to specification RFC 4180 in order to be properly parsed.
     * @param file A reference to a file
     * @return A <code>CSVArray</code> instance.
     * @throws CSVParseException
     */
    public CSVArray readCSV(File file) throws CSVParseException {
        try(FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int nread;
            byte [] bytes = new byte[bufSize];
            while ((nread = fis.read(bytes)) != -1) {
                baos.write(bytes,0,nread);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            String charSet = getBestCharsetName(bais);
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, charSet));
            return this.readCSV(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *Returns a <code>CSVArray</code> instance after parsing the <code>String</code> representation of the CSV data. The contents
     * the data must conform to specification RFC 4180 in order to be properly parsed.
     * @param csvString a string representation of .csv file contents
     * @return A <code>CSVArray</code> instance.
     * @throws CSVParseException
     */
    public CSVArray readCSV(String csvString) throws CSVParseException {
        String charSet = getBestCharsetName(null);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(IOUtils.toInputStream(csvString, charSet), charSet))) {
            return this.readCSV(reader);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *Returns a <code>CSVArray</code> instance after parsing the characters of the <code>BufferedReader</code>. The contents
     * the data being read must conform to specification RFC 4180 in order to be properly parsed.
     * @param r the <code>BufferedReader</code>
     * @return
     * @throws CSVParseException
     */
    public CSVArray readCSV(BufferedReader r) throws CSVParseException {
        try {
            CSVArray ret = null;
            CSVHeader header = null;
            StringBuilder mainBuffer = new StringBuilder();
            StringBuilder spaceBuffer = new StringBuilder();
            char delim = this.format.getDelimiter();
            char lc = '\0'; //the last significant character encountered, excluding spaces and tabs
            int lineNum = 0; //file line number
            int columns = 1; //number of data columns detected
            int qCount = 0; //quote count
            boolean headerSet = false; //whether or not the header has been set
            boolean quotedFieldFinished = false; //whether a field that has an opening quote has been closed yet

            ArrayList<String> d = new ArrayList<>();
            String line;
            while((line = r.readLine()) != null) {
                lineNum++;
                char[] ca = line.toCharArray();
                for (int i = 0; i < ca.length; i++) {
                    char c = ca[i];
                    if (c == ' ' || c == '\t') {
                        spaceBuffer.append(c);
                    } else { //handle leading and trailing spaces that occur around quoted fields
                        if (spaceBuffer.length() != 0) {
                            if (!quotedFieldFinished && !(lc == delim && c == '"') && !(lc == '"' && c == delim)) {
                                mainBuffer.append(spaceBuffer);
                                spaceBuffer.setLength(0);
                            }
                            else if ((lc == delim && c == '"')){
                                spaceBuffer.setLength(0);
                            }
                        }
                    }

                    if (c == '"') {
                        if (qCount == 0 && !quotedFieldFinished && i != ca.length - 1 && (i == 0 || ca[i - 1] == delim || lc == delim)) { //this is definitely an opening field quote
                            qCount++;
                        } else if (qCount == 1 && !quotedFieldFinished && i != 0 && (i == ca.length - 1 || ca[i + 1] == delim || ca[i + 1] != '"')) { //this is definitely a closing field quote
                            qCount--;
                            quotedFieldFinished = true;
                        } else if (qCount == 1 && !quotedFieldFinished && i != 0 && i != ca.length - 1 && (ca[i + 1] == '"')) { //a double quote
                            i++;
                            mainBuffer.append(c);
                        } else {
                            //error
                            mainBuffer.append(c);
                            throw new CSVParseException(CSVParseException.UNEXPECTED_QUOTE, mainBuffer.toString(), lineNum);
                        }
                    } else if (c == delim) {
                        if (qCount == 0) {
                            d.add(this.format.getTrimSpace() ? mainBuffer.toString().trim() : mainBuffer.toString());
                            if (!this.format.getHasHeader() && !headerSet) {
                                columns++;
                            }
                            quotedFieldFinished = false;
                            spaceBuffer.setLength(0);
                            mainBuffer.setLength(0);
                        }
                        else if (!quotedFieldFinished){
                            mainBuffer.append(c);
                        }
                        else {
                            mainBuffer.append(c);
                            throw new CSVParseException(CSVParseException.UNEXPECTED_TOKEN, mainBuffer.toString(), lineNum);
                        }
                    } else {
                        if (!quotedFieldFinished && c != ' ' && c != '\t') {
                            mainBuffer.append(c);
                        }
                        else if (quotedFieldFinished && c != ' ' && c != '\t'){
                            mainBuffer.append(c);
                            throw new CSVParseException(CSVParseException.UNEXPECTED_TOKEN, mainBuffer.toString(), lineNum);
                        }
                    }
                    if (i == ca.length - 1) { //end of line
                        if (qCount == 0) {
                            d.add(this.format.getTrimSpace() ? mainBuffer.toString().trim() : mainBuffer.toString());
                            quotedFieldFinished = false;
                            spaceBuffer.setLength(0);
                            if (!headerSet) { //add header
                                if (this.format.getHasHeader()) header = new CSVHeader(d);
                                else {
                                    header = new CSVHeader(columns); //dummy header
                                }
                                ret = new CSVArray(header);
                                if (!this.format.getHasHeader()) {
                                    CSVRecord rec = new CSVRecord(header, d);
                                    ret.add(rec);
                                }
                                d.clear();
                                headerSet = true;
                            } else { //add new record
                                CSVRecord rec = new CSVRecord(header, d);
                                d.clear();
                                ret.add(rec);
                            }
                            mainBuffer.setLength(0);
                        }
                        else if (qCount == 1 && !quotedFieldFinished) { //multi-line field
                            mainBuffer.append('\n');
                        }
                        else {
                            throw new CSVParseException(CSVParseException.MISSING_CLOSING_QUOTE, mainBuffer.toString(), lineNum);
                        }
                    }
                    if (c != ' ' && c != '\t') {
                        lc = c;
                    }
                }
            }
            return ret;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the best <code>Charset</code> to use for the <code>InputStream</code>. If the <code>CSVFileFormat</code> object specifies a <code></code>Charset, that
     * is returned. Otherwise, the InputStream is partially read by UniversalDetector and the best Charset is then
     * detected. If this fails, the default system character encoding is returned.
     * @param is the InputStream
     * @return canonical name of the Charset, as a String
     */
    private String getBestCharsetName(InputStream is) {
        String ret = this.format.getCharacterSetName();
        if (ret != null) {
            return ret;
        }
        else {
            if (is != null) {
                UniversalDetector ud = new UniversalDetector(null);
                try {
                    byte[] buff = new byte[4096];
                    int nread;
                    while ((nread = is.read(buff)) > 0 && !ud.isDone()) {
                        ud.handleData(buff, 0, nread);
                    }
                    ud.dataEnd();
                    ret = ud.getDetectedCharset();
                } catch (IOException ie) {
                    ie.printStackTrace();
                } finally {
                    if (ret != null) return ret;
                    else return System.getProperty("file.encoding");
                }
            } else {
                return System.getProperty("file.encoding");
            }
        }
    }

    /**
     *Returns the <code>CSVFileFormat</code> associated with this <code>CSVReader</code>
     * @return the input file format .
     */
    public CSVFileFormat getFormat() {
        return this.format;
    }

    /**
     *Sets the <code>CSVFileFormat</code> associated with this <code>CSVReader</code>
     * @param f the new format
     */
    public void setFormat(CSVFileFormat f) {
        this.format = f;
    }
}
