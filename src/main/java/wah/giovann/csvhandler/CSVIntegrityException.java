package wah.giovann.csvhandler;

import java.util.ArrayList;

/**
 * Created by giovadmin on 5/16/17.
 */
public class CSVIntegrityException extends RuntimeException {
    public static final int NULL_CSVHEADER_ARGUMENT = 0;
    public static final int DUPLICATE_CSVHEADER_COLUMNS = 1;
    public static final int INVALID_CSVRECORD_ADD = 2;
    public static final int INVALID_CSVHEADER_COLUMN_NUMBER = 3;

    private int errorType;
    private Object relatedObject;

    public CSVIntegrityException(int e, Object r){
        this.errorType = e;
        this.relatedObject = r;
    }

    public String getMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid Operation: ");
        switch (errorType) {
            case NULL_CSVHEADER_ARGUMENT:
                sb.append("The argument to the CSVHeader object constructor cannot be null.");
                break;
            case DUPLICATE_CSVHEADER_COLUMNS:
                sb.append("The List argument to the CSVHeader object constructor cannot contain duplicate values. ");
                ArrayList<String> dups = (ArrayList<String>) relatedObject;
                for (int i = 0; i < dups.size(); i++){
                    sb.append(dups.get(i));
                    if (i != dups.size()-1) sb.append(", ");
                }
                sb.append(".");
                break;
            case INVALID_CSVRECORD_ADD:
                sb.append("Could not add data item to CSVRecord object. The header does not contain a column '"+relatedObject.toString()+"'.");
                break;
            case INVALID_CSVHEADER_COLUMN_NUMBER:
                sb.append("The argument to the CSVHeader object constructor for a dummy header must be a positive integer greater than 0. ");
                sb.append("Value passed: "+relatedObject.toString()+".");
                break;
            default:
        }
        return sb.toString();
    }
}
