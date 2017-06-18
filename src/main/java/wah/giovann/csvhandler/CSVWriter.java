package wah.giovann.csvhandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by giovadmin on 6/16/17.
 */
public class CSVWriter {
    private CSVFileFormat format;

    public CSVWriter(CSVFileFormat f) {
        this.format = f;
    }

    public void write(CSVArray array, String filePath, boolean append) {
        try (FileWriter fw = new FileWriter(new File(filePath), append)) {
            fw.write(array.csvString(format));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CSVFileFormat getFormat() {
        return this.format;
    }

    public void setFormat(CSVFileFormat f) {
        this.format = f;
    }
}
