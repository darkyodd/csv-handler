package wah.giovann.csvhandler.reader;

import wah.giovann.csvhandler.spec.CSVSpecification;

/**
 * Class responsible for the reading information from .csv files. Instances
 * are created using a custom wah.giovann.csvhandler.spec.CSVSpecification object or the default wah.giovann.csvhandler.spec.CSVSpecification
 * object.
 *
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVReader {
    private CSVSpecification specification;

    public CSVReader(CSVSpecification sp){
        this.specification = sp;
    }
}
