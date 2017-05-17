package wah.giovann.csvhandler;

import java.util.*;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVRecord {

    private ArrayList<String> data;
    private CSVHeader sharedHeader;

    private CSVRecord(CSVHeader h) {
        this.sharedHeader = h;
        this.data = new ArrayList<>();
    }

    private CSVRecord(CSVHeader h, ArrayList d) {
        this.sharedHeader = h;
        this.data = d;
    }

    public boolean containsHeaderColumn(String name){
        return this.sharedHeader.containsColumn(name);
    }

    public boolean containsValue(String value){
        return data.contains(value);
    }

    public String get(String columnName) {
        return this.get(this.sharedHeader.indexOfColumn(columnName));
    }

    public String get(int column) {
        return data.get(column);
    }

    public int getTotalColumns(){
        return this.sharedHeader.totalColumns();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public String add (String column, Object value) {
        if (this.sharedHeader.containsColumn(column)) {
            this.data.set(this.sharedHeader.indexOfColumn(column), value.toString());
        }
        else throw new CSVIntegrityException(CSVIntegrityException.INVALID_CSVRECORD_ADD, column);
        return null;
    }

    public Collection<String> getValues() {
        return this.data;
    }

    protected String remove(int columnNum) {
        return this.data.get(columnNum);
    }

    protected String remove(String column){
        if (this.sharedHeader.containsColumn(column))
            return this.data.remove(this.sharedHeader.indexOfColumn(column));
    }

    public double getDouble(String key){
        String obj = this.get(key);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.DOUBLE, key, this);
        }
    }

    public float getFloat(String key) {
        String obj = this.get(key);
        if (obj != null) {
            return new Float(obj).floatValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.FLOAT, key, this);
        }
    }

    public long getLong(String key) {
        String obj = this.get(key);
        if (obj != null) {
            return new Long(obj).longValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.LONG, key, this);
        }
    }

    public int getInt(String key) {
        String obj = this.get(key);
        if (obj != null) {
            return new Integer(obj).intValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.INT, key, this);
        }
    }

    public char getChar(String key) {
        String obj = this.get(key);
        if (obj != null && obj.length() == 1) {
            return obj.charAt(0);
        }
        else {
            throw new ValueConversionException(ValueConversionException.CHAR, key, this);
        }
    }

    public short getShort(String key) {
        String obj = this.get(key);
        if (obj != null && obj.length() == 1) {
            return new Short(obj).shortValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.SHORT, key, this);
        }
    }

    public byte getByte(String key) {
        String obj = this.get(key);
        if (obj != null && obj.length() == 1) {
            return new Byte(obj).byteValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BYTE, key, this);
        }
    }

    public boolean getBoolean(String key) {
        String obj = this.get(key);
        if (obj != null && obj.length() == 1) {
            return new Boolean(obj).booleanValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BOOLEAN, key, this);
        }
    }

    public String getRecordString(char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.data.size(); i++){
            sb.append(this.get(this.data.get(i)));
            if (i < this.data.size()-1) sb.append(delimiter);
        }
        return sb.toString();
    }
}
