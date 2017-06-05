package wah.giovann.csvhandler;

import org.apache.commons.io.IOUtils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import wah.giovann.csvhandler.error.CSVParseException;


/**
 * Class responsible for the reading information from .csv files. Instances
 * are created using a custom wah.giovann.csvhandler.CSVFileFormat object or the default wah.giovann.csvhandler.CSVFileFormat
 * object.
 *
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVReader {
    private final int bufSize = 256;
    private CSVFileFormat format;

    public CSVReader(CSVFileFormat ft) {
        this.format = ft;
    }

    public CSVArray getCSVArray(File file) throws CSVParseException {
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
            return this.getCSVArray(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CSVArray getCSVArray(String csvString) throws CSVParseException {
        String charSet = getBestCharsetName(null);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(IOUtils.toInputStream(csvString, charSet), charSet))) {
            return this.getCSVArray(reader);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public CSVArray getCSVArray(BufferedReader r) throws CSVParseException {
        try {
            CSVArray<CSVRecord> ret = null;
            CSVHeader header = null;
            StringBuilder mainBuffer = new StringBuilder();
            StringBuilder spaceBuffer = new StringBuilder();
            char delim = this.format.getDelimiter();
            char lc = '\0';
            int lineNum = 0;
            int columns = 1;
            int qCount = 0; //quote count
            boolean headerSet = false;
            boolean quotedFieldFinished = false;

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
                        else {
                            mainBuffer.append(c);
                            throw new CSVParseException(CSVParseException.MISSING_CLOSING_QUOTE, mainBuffer.toString(), lineNum);
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
                                if (!this.format.getHasHeader()) {
                                    columns++;
                                }
                                if (this.format.getHasHeader()) header = new CSVHeader(d);
                                else {
                                    header = new CSVHeader(columns); //dummy header
                                }
                                ret = new CSVArray<>(this.format, header);
                                d.clear();
                                headerSet = true;
                            } else { //add new record
                                CSVRecord rec = new CSVRecord(header, d);
                                d.clear();
                                ret.add(rec);
                            }
                        }
                        else {
                            mainBuffer.append(c);
                            throw new CSVParseException(CSVParseException.MISSING_CLOSING_QUOTE, mainBuffer.toString(), lineNum);
                        }
                        mainBuffer.setLength(0);
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
     * Returns the best Charset to use for the InputStream. If the CSVFileFormat object specifies a Charset, that
     * is returned. Otherwise, the InputStream is partially read by UniversalDetector and the best Charset is then
     * detected. If this fails, the default system character encoding is returned.
     * @param is - the InputStream
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

    private void printList(List l){
        for (Object o : l){
            System.out.print(o.toString());
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void main (String [] args) throws IOException {
        File file = new File(ClassLoader.getSystemClassLoader().getResource("Test.csv").getFile());
        CSVFileFormat format = new CSVFileFormat.Builder()
                .delimiter(CSVFileFormat.SEMICOLON_DELIMITER)
                .hasHeader(true)
                .trimSpace(false)
                .build();
    //    CSVFileFormat format = CSVFileFormat.DEFAULT_FORMAT;
        CSVReader reader = new CSVReader(format);
        try {
            long start = System.currentTimeMillis();
            CSVArray<CSVRecord> arr = reader.getCSVArray(file);
            long end = System.currentTimeMillis();
            double time = (new Double(end) - new Double(start))/1000;
            System.out.println(arr);
            for (Object r : arr) {
                System.out.println("'" + ((CSVRecord)r).get("Letter") + "'");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
