package wah.giovann.csvhandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *<code>CSVWriter</code> is a very simple class used to quickly write a <code>CSVArray</code> instance to file.
 * <br><br>
 * It takes a <code>CSVFileFormat</code> instance as an argument to the constructor, which in term determines the
 * format that the .csv file will be written in. Here, only format's <code>outputFileLineEnd</code> and <code>delimiter</code>
 * fields will be used to write the <code>CSVArray</code> to file. The data written to the file is
 * compliant with RFC 4180 (see <a href="https://en.wikipedia.org/wiki/Comma-separated_values" target="_blank">the CSV wikipedia page</a>).
 *
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVWriter {
    private CSVFileFormat format;

    /**
     *Creates a new <code>CSVWriter</code> instance, based on the output <code>CSVFileFormat</code> instance argument.
     * @param outputFormat the output file format
     */
    public CSVWriter(CSVFileFormat outputFormat) {
        this.format = outputFormat;
    }

    /**
     *Writes the CSV data contained within the <code>CSVArray</code> instance to a file. The file path is specified by the
     * <code>filePath</code> string parameter. Whether or not the data is appended to the file is determined by the <code>append</code>
     * parameter.
     * @param array the <code>CSVArray</code> instance to write to file.
     * @param filePath the path where the file will be saved.
     * @param append if true, append the data to the file in the filePath. Otherwise, overwrite.
     */
    public void write(CSVArray array, String filePath, boolean append) {
        int index = filePath.lastIndexOf('/');
        String dir = filePath.substring(0,index);
        File f = null;
        FileWriter fw = null;
        try {
            f = new File(dir);
            if (f.isDirectory() && !f.exists()) f.mkdir();
            fw = new FileWriter(filePath,append);
            fw.write(array.csvString(format));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fw != null) fw.close();
                if (f != null) f = null;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *Returns the <code>CSVFileFormat</code> associated with this <code>CSVReader</code>.
     * @return the input file format .
     */
    public CSVFileFormat getFormat() {
        return this.format;
    }

    /**
     *Sets the <code>CSVFileFormat</code> associated with this <code>CSVReader</code>.
     * @param f the new format
     */
    public void setFormat(CSVFileFormat f) {
        this.format = f;
    }
}
