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
    private CSVFileFormat format;

    public CSVReader(CSVFileFormat ft) {
        this.format = ft;
    }

    public CSVArray getCSVArray(File file) throws CSVParseException {
        try(FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int nread;
            byte [] bytes = new byte[256];
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
            StringBuilder buffer = new StringBuilder();
            char[] line1 = r.readLine().toCharArray();
            char delim = this.format.getDelimiter();
            char c;
            char lc = '\0';
            int lineNum = 1;
            int columns = 1;
            int qCount = 0; //quote count
            int eqCount = 0; //embedded quote count
            ArrayList<String> d = new ArrayList<>();
            ArrayList<String> h = new ArrayList<>();
            for (int i = 0; i < line1.length; i++) {
                c = line1[i];
                if (i == line1.length-1) { //end of line
                    buffer.append(c);
                    if (this.format.getHasHeader()) h.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                    else d.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                    buffer.setLength(0);
                    lineNum++;
                }
                else if (c =='"') {
                    if (qCount == 0 && i != line1.length-1 && (i == 0 || line1[i-1] == delim)) { //this is definitely an opening field quote
                        qCount++;
                    }
                    else if (qCount == 1 && i != 0 && (i==line1.length-1 || line1[i+1]==delim || line1[i+1]=='\r' || line1[i+1]=='\n')) { //this is definitely a closing field quote
                        qCount--;
                    }
                    else if (qCount == 1 && i != 0 && i != line1.length-1 &&(line1[i+1]=='"')) { //a double quote
                        qCount++;
                    }

                    else {

                    }
                }
                else if (c==delim) {
                    if (this.format.getHasHeader()) h.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                    else {
                        d.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                        columns++;
                    }
                    buffer.setLength(0);
                }
                else {
                    buffer.append(c);
                }
            }
            CSVHeader header;
            if (this.format.getHasHeader()) header = new CSVHeader(h);
            else {
                header = new CSVHeader(columns); //dummy header
            }
            //csv record to return
            CSVArray<CSVRecord> ret = new CSVArray<>(this.format, header);
            if (!this.format.getHasHeader()) { //add first line to array
                CSVRecord rec = new CSVRecord(header, d);
                d.clear();
                ret.add(rec);
            }
            int nread;
            char[] arr = new char[256];
            buffer.setLength(0);
            while((nread = r.read(arr)) != -1) {
                for (int i = 0; i < nread; i++){
                    c = arr[i];
                    if (i == nread-1) {
                        lc = c;
                    }
                    if (c==delim) {
                        d.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                        buffer.setLength(0);
                    }
                    else if (c =='"') {
                        if (qCount == 0 && i != nread-1) {
                            qCount++;
                        }
                        else if (qCount == 1 && i != 0) {
                            qCount--;
                        }
                    }
                    else if (c=='\r') {
                        if (i < nread-1) { //if the carriage return isn't the last character in the char buffer...
                            if (arr[i+1] == '\n') {
                                i++;
                            }
                            d.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                            buffer.setLength(0);
                            CSVRecord rec = new CSVRecord(header, d);
                            d.clear();
                            ret.add(rec);
                            lineNum++;
                        }
                    }
                    else if (c=='\n') {
                        if (lc == '\r') { //new line

                        }
                        d.add(this.format.getTrimSpace()?buffer.toString().trim():buffer.toString());
                        buffer.setLength(0);
                        CSVRecord rec = new CSVRecord(header, d);
                        d.clear();
                        ret.add(rec);
                        lineNum++;
                    }
                    else {
                        buffer.append(c);
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
                .trimSpace(true)
                .build();
    //    CSVFileFormat format = CSVFileFormat.DEFAULT_FORMAT;
        CSVReader reader = new CSVReader(format);
        try {
            long start = System.currentTimeMillis();
            CSVArray<CSVRecord> arr = reader.getCSVArray(file);
            long end = System.currentTimeMillis();
            double time = (new Double(end) - new Double(start))/1000;
            System.out.print(arr);
            System.out.println();
            System.out.println(arr.getHeaderString());
            System.out.println(arr.getRecord(0).get(3));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
