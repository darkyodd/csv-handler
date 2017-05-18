package wah.giovann.csvhandler.reader;

import org.apache.commons.io.IOUtils;

import wah.giovann.csvhandler.CSVArray;
import wah.giovann.csvhandler.CSVFileFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

import org.mozilla.universalchardet.UniversalDetector;
import wah.giovann.csvhandler.CSVRecord;

import static java.lang.System.out;


/**
 * Class responsible for the reading information from .csv files. Instances
 * are created using a custom wah.giovann.csvhandler.CSVFileFormat object or the default wah.giovann.csvhandler.CSVFileFormat
 * object.
 *
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVReader {
    private CSVFileFormat format;

    public CSVReader(CSVFileFormat ft) {
        this.format = ft;
    }

    public CSVArray getCSVArray(File file) throws CSVParseException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)) ){
            String charSet = getBestCharsetName(bis);
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
            StringBuilder buffer = new StringBuilder();
            ArrayList<String> h = new ArrayList<>();
            ArrayList<CSVRecord> recs = new ArrayList<>();
            Stack<Character> quoteStack = new Stack<>();
            int nread;
            int total_columns = 0;
            int count = 0;
            boolean headerSet = false;
            char[] arr = new char[256];
            char[] line1 = r.readLine().toCharArray();
            char delim = this.format.getDelimiter();
            for (int i = 0; i < line1.length; i++) {
                char c = line1[i];
                if (c==delim) {

                }
                else if (c=='"'){
                    //its either the beginning of a field, the end of a field, the beginning
                    //of an embedded quote, the end of an embedded quote, or an embedded quote.
                    if (i < line1.length-1) {
                        if (quoteStack.isEmpty()) { //beginning of field
                            quoteStack.push(c);
                        }
                        else if (quoteStack.size() == 1) {
                            if ((line1[i + 1] == delim || line1[i + 1] == '\r' || line1[i + 1] == '\n')) {
                                //this is the end of a field
                                quoteStack.pop();
                            } else if (line1[i + 1] == '"') {
                                //this is the beginning of an embedded quote
                                quoteStack.push(c);
                            }
                        }
                        else if (quoteStack.size() == 2) {
                            if (line1) { //this is an embedded quote

                            }
                        }
                    }
                    else if (quoteStack.size() == 1) {

                    }
                    else {
                        //error because a "rogue" quote was encountered at the end of the line
                        throw new CSVParseException(0,null);
                    }
                }
                else if (c=='\n') {
                    if (quoteStack.size() > 0) buffer.append(c);
                    else throw new CSVParseException(0,null);
                }
                else {
                    buffer.append(c);
                }
            }
            while((nread = r.read(arr)) != -1) {
                for (int i = 0; i < nread; i++){
                    if (!headerSet) {

                    }
                    else {

                    }
                }
            }
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
                    if (is.markSupported()) is.mark(4097);
                    byte[] buff = new byte[4096];
                    int nread;
                    while ((nread = is.read(buff)) > 0 && !ud.isDone()) {
                        ud.handleData(buff, 0, nread);
                    }
                    ud.dataEnd();
                    ret = ud.getDetectedCharset();
                    if (is.markSupported()) is.reset();
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

    public static void main (String [] args) throws IOException {
        File file = new File(ClassLoader.getSystemClassLoader().getResource("facebook2Train.csv").getFile());
        CSVFileFormat format = CSVFileFormat.DEFAULT_FORMAT;
        CSVReader reader = new CSVReader(format);
        CSVArray arr = reader.getCSVArray(file);
    }

}
