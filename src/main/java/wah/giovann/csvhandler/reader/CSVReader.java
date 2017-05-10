package wah.giovann.csvhandler.reader;

import wah.giovann.csvhandler.format.CSVFormat;

/**
 * Class responsible for the reading information from .csv files. Instances
 * are created using a custom wah.giovann.csvhandler.format.CSVFormat object or the default wah.giovann.csvhandler.format.CSVFormat
 * object.
 *
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVReader {
    private CSVFormat format;

    public CSVReader(CSVFormat ft){
        this.format = ft;
    }
}
