package wah.giovann.csvhandler.reader;

import org.apache.commons.io.IOUtils;

import wah.giovann.csvhandler.CSVArray;
import wah.giovann.csvhandler.CSVFileFormat;

import java.io.*;

import org.mozilla.universalchardet.UniversalDetector;

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

    public CSVArray getCSVArray(File file) {
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

    public CSVArray getCSVArray(String csvString) {
        String charSet = getBestCharsetName(null);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(IOUtils.toInputStream(csvString, charSet), charSet))) {
            return this.getCSVArray(reader);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public CSVArray getCSVArray(BufferedReader r){
        try {
            StringBuilder buffer = new StringBuilder();
            char[] arr = new char[256];
            int nread;
            while((nread = r.read(arr)) != -1) {
                for (int i = 0; i < nread; i++){
                    out.println(arr[i]);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

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
