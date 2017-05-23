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
            String str = r.readLine();
            char[] line1 = str.toCharArray();
            char delim = this.format.getDelimiter();
            int line = 1;
            //get header
            ArrayList<String> h = new ArrayList<>();
            for (int i = 0; i < line1.length; i++) {
                char c = line1[i];
                if (i == line1.length-1) {
                    buffer.append(c);
                    h.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (c==delim) {
                    h.add(buffer.toString());
                    buffer.setLength(0);
                }
                /*
                else if (c=='"'){
                    //its either the beginning of a field, the end of a field, the beginning
                    //of an embedded quote, the end of an embedded quote, or an embedded quote.
                    if (quoteStack.isEmpty()) { //beginning of field
                        if (i != line1.length-1) {
                            quoteStack.push(c);
                        }
                        else throw new CSVParseException(0,null); //and invalid quote located at the end of the line.
                    }
                    else if (quoteStack.size() == 1) {
                        if ((i == line1.length-1) ||
                                (i < line1.length-1 && (line1[i + 1] == delim || line1[i + 1] == '\r' || line1[i + 1] == '\n'))) {
                            //this is the end of a field
                            quoteStack.pop();
                        } else if (i < line1.length-1 &&
                                line1[i + 1] == '"') {
                            //this signifies the beginning of an embedded quote
                            quoteStack.push(c);
                        }
                        else throw new CSVParseException(0,null); //some invalid character is after the quote.
                    }
                    else if (quoteStack.size() == 2) {
                        if (line1[i-1] == '"') { //this is an embedded quote
                            buffer.append(c);

                        }
                    }
                    else {
                        //error because a "rogue" quote was encountered at the end of the line
                        throw new CSVParseException(0,null);
                    }
                }
                */
                else {
                    buffer.append(c);
                }
            }
            CSVHeader header = new CSVHeader(h);
            CSVArray<CSVRecord> ret = new CSVArray<>(this.format, header);
            char lc;
            ArrayList<String> d = new ArrayList<>();
            buffer.setLength(0);
            while((nread = r.read(arr)) != -1) {
                for (int i = 0; i < nread; i++){
                    char c = arr[i];
                    if (i == nread-1) {
                        lc = c;
                    }
                    if (c==delim) {
                        d.add(buffer.toString());
                        buffer.setLength(0);
                    }
                    else if (c=='\r') {
                        if (i < nread-1) {
                            if (arr[i+1] == '\n') {
                                i++;
                            }
                            d.add(buffer.toString());
                            buffer.setLength(0);
                            CSVRecord rec = new CSVRecord(header, d);
                            d.clear();
                            ret.add(rec);
                        }
                    }
                    else if (c=='\n') {
                        d.add(buffer.toString());
                        buffer.setLength(0);
                        CSVRecord rec = new CSVRecord(header, d);
                        d.clear();
                        ret.add(rec);
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
        File file = new File(ClassLoader.getSystemClassLoader().getResource("facebook2Train.csv").getFile());
        CSVFileFormat format = CSVFileFormat.DEFAULT_FORMAT;
        CSVReader reader = new CSVReader(format);
        try {
            long start = System.currentTimeMillis();
            CSVArray<CSVRecord> arr = reader.getCSVArray(file);
            long end = System.currentTimeMillis();
            double time = (new Double(end) - new Double(start))/1000;
            System.out.println(time);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
