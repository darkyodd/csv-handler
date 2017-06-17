package wah.giovann.csvhandler;

/**
 * Created by giovadmin on 6/16/17.
 */
public class CSVWriter {
    private CSVFileFormat format;
    public CSVWriter(CSVFileFormat f) {

    }
    public CSVFileFormat getFormat() {
        return this.format;
    }
    public void setFormat(CSVFileFormat f) {
        this.format = f;
    }
}
