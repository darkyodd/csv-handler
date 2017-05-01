package wah.giovann.csvhandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVObject<CSVRecord> extends ArrayList<CSVRecord> {

    public CSVObject() {
        super();
    }

    public CSVObject(Collection c) {
        super(c);
    }

    public String csvString() {

        return null;
    }

    public String toString() {
        return csvString();
    }
}
