package wah.giovann.csvhandler.error;

import java.util.ArrayList;
import java.util.List;

/**
 *Exception thrown to prevent any action that may invalidate the integrity of a <code>CSVArray</code>
 * and/or the resulting output file.
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVIntegrityException extends RuntimeException {
    public static final int NULL_CSVHEADER_ARGUMENT = 0;
    public static final int DUPLICATE_COLUMNS_IN_CONSTRUCTOR = 1;
    public static final int INVALID_CSVRECORD_ADD = 2;
    public static final int INVALID_CSVHEADER_COLUMN_NUMBER = 3;
    public static final int HEADER_AND_RECORD_DATA_INCOMPATABLE = 4;
    public static final int COLUMN_REMOVAL_FAILED = 5;
    public static final int DUPLICATE_CSVHEADER_COLUMNS = 6;
    public static final int ADDING_COLUMN_WITHOUT_HEADER = 7;
    public static final int INVALID_HEADER_LENGTH = 8;
    public static final int INVALID_VALUE_SWAP = 9;

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
            case DUPLICATE_COLUMNS_IN_CONSTRUCTOR:
                sb.append("The List argument to the CSVHeader object constructor cannot contain duplicate values. '");
                ArrayList<String> dups = (ArrayList<String>) relatedObject;
                for (int i = 0; i < dups.size(); i++){
                    sb.append(dups.get(i));
                    if (i != dups.size()-1) sb.append(", ");
                }
                sb.append("'");
                break;
            case INVALID_CSVRECORD_ADD:
                sb.append("Could not add data item to CSVRecord object.");
                if (relatedObject.getClass().getName().equals("String")) {
                    sb.append(" The header does not contain the column '"+relatedObject.toString()+"'.");
                }
                else if (relatedObject.getClass().getName().equals("Integer")) {
                    sb.append(" Column index out of bounds: "+relatedObject.toString());
                }
                break;
            case INVALID_CSVHEADER_COLUMN_NUMBER:
                sb.append("The argument to the CSVHeader object constructor for a dummy header must be a positive integer greater than 0. ");
                sb.append("Value passed: "+relatedObject.toString()+".");
                break;
            case HEADER_AND_RECORD_DATA_INCOMPATABLE:
                List<Object> objs = (List<Object>) this.relatedObject;
                ArrayList<String> header = (ArrayList<String>) objs.get(0);
                ArrayList<String> data =  (ArrayList<String>) objs.get(1);

                sb.append("The header and data arguments to the CSVRecord are incompatible in size. ");
                sb.append("Header columns: "+ header.size());
                sb.append(", ");
                sb.append("Data size: "+data.size()+".\n");
                sb.append("Header: ");
                for (String s : header){
                    sb.append("["+s+"] ");
                }
                sb.append("\nData: ");
                for (String s : data) {
                    sb.append("["+s+"] ");
                }
                break;
            case COLUMN_REMOVAL_FAILED:
                ArrayList<Object> errObjs = (ArrayList<Object>) this.relatedObject;
                String column = errObjs.get(0).toString();
                String record = errObjs.get(1).toString();

                sb.append("Failed to remove column '");
                sb.append(column);
                sb.append("' from data item '");
                sb.append(record);
                sb.append("'.");
                break;
            case DUPLICATE_CSVHEADER_COLUMNS:
                sb.append("Cannot add duplicate column '");
                sb.append(this.relatedObject);
                sb.append("' to CSVHeader.");
                break;
            case ADDING_COLUMN_WITHOUT_HEADER:
                sb.append("Cannot add column '");
                sb.append(this.relatedObject);
                sb.append("'. This CSVArray has no header.");
                break;
            case INVALID_HEADER_LENGTH:
                ArrayList<Integer> lengths = (ArrayList<Integer>) this.relatedObject;
                sb.append("New header must be the same size as the old header. \nOld header length: ");
                sb.append(lengths.get(0));
                sb.append("\nNew header length: ");
                sb.append(lengths.get(1));
                break;
            default:

                break;
        }
        return sb.toString();
    }
}
