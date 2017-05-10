package wah.giovann.csvhandler;

import wah.giovann.csvhandler.format.CSVFormat;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVArray<T extends CSVRecord> extends ArrayList {
    /**
     *  State pattern (to let CSVRecord know when certain operations can take place)
     */
    private CSVFormat format;
    public CSVArray() {
        super();
    }

    public CSVArray(Collection c) {
        super(c);
    }

    public String csvString() {

        return null;
    }

    public String toString() {
        return csvString();
    }
}
