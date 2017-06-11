package wah.giovann.csvhandler.error;

import wah.giovann.csvhandler.CSVRecord;

/**
 * Created by giovadmin on 4/28/17.
 */
public class ValueConversionException extends RuntimeException {
    public static final int INT = 0;
    public static final int DOUBLE = 1;
    public static final int FLOAT = 2;
    public static final int LONG = 3;
    public static final int SHORT = 4;
    public static final int BYTE = 5;
    public static final int CHAR = 6;
    public static final int BOOLEAN = 7;

    private int errorCode;
    private String key;
    private int index;
    private CSVRecord record;

    public ValueConversionException (int err, String k, CSVRecord r) {
        this.errorCode = err;
        this.key = k;
        this.record = r;
        this.index = -1;
    }

    public ValueConversionException (int err, int i, CSVRecord r) {
        this.errorCode = err;
        this.index = i;
        this.record = r;
        this.key = null;
    }

    public String getMessage() {
        String err = "";
        switch ( this.errorCode ) {
            case INT:
                err = "int";
                break;
            case DOUBLE:
                err = "double";
                break;
            case FLOAT:
                err = "float";
                break;
            case LONG:
                err = "long";
                break;
            case SHORT:
                err = "short";
                break;
            case BYTE:
                err = "byte";
                break;
            case CHAR:
                err = "char";
                break;
            default:
                err = "boolean";
                break;
        }

        return "The data contained in field '"+((this.key!=null)?this.key:this.index)+"' could not be converted to data type "+err+".\nCSVRecord: " +record.toString();
    }
}
