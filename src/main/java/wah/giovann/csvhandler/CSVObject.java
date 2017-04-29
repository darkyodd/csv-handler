package wah.giovann.csvhandler;

import java.util.ArrayList;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVObject {
    private ArrayList<String> header;

    public CSVObject() {
        
    }
    public CSVObject(int f) {
        this.header = new ArrayList<String> (f);
    }

    public CSVObject(String csv) {

    }

    public CSVObject (ArrayList<String> h) {
        this.header = h;
    }
}
