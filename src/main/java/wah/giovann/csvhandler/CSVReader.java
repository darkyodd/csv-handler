package wah.giovann.csvhandler;

import wah.giovann.csvhandler.specification.CSVSpecification;

/**
 * Class responsible for the reading information from .csv files. Instances
 * are created using a custom CSVSpecification object or the default CSVSpecification
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
