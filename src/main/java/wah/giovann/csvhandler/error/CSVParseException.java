package wah.giovann.csvhandler.error;

/**
 * Created by giovadmin on 5/11/17.
 */
public class CSVParseException extends Exception {

    int errorType;
    Object relatedObject;

    public CSVParseException(int e, Object o) {
        this.errorType = e;
        this.relatedObject = o;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }
}
