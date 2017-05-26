package wah.giovann.csvhandler.error;

/**
 * Created by giovadmin on 5/11/17.
 */
public class CSVParseException extends Exception {

    int errorType;
    Object relatedObject;
    int line;

    public CSVParseException(int e, Object o, int l) {
        this.errorType = e;
        this.relatedObject = o;
        this.line = l;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }
}
