package wah.giovann.csvhandler;

import java.util.*;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVRecord {
    /**
     *  Maybe construct a Factory or Builder?
     *  Construct a proxy to prevent arbitray manipulation of data
     */
    private HashMap<String,String> data;
    private CSVFormat format;

    private CSVRecord() {
        this.format = CSVFormat.DEFAULT_FORMAT;
        this.data = new HashMap<>();
    }

    private CSVRecord(CSVFormat f) {
        this.format = f;
        this.data = new HashMap<>();
    }

    private CSVRecord(HashMap d, CSVFormat f) {
        this.format = f;
        this.data = d;
    }

    public boolean containsField(String field){
        if (!this.format.getHasHeader()) return false;
        return this.data.containsKey(field);
    }

    public boolean containsValue(Object value){
        return data.containsValue(value);
    }

    public String get(String field) {
        return data.get(field);
    }

    public String get(int column) {

        return null;
    }

    public int getColumns(){
        return this.data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public Set<String> getFieldSet() {
        return data.keySet();
    }

    public String put (String field, Object value) {
        return data.put(field, value.toString());
    }

    public void putAll(Map<String, String> m) {
        this.data.putAll(m);
    }

    public Collection<String> getValues() {
        return this.data.values();
    }

    public String remove(String field){
        return this.data.remove(field);
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

    @Override
    public String toString() {
        ArrayList<String> header = this.format.getDestinationHeader();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < header.size(); i++){
            sb.append(this.get(header.get(i)));
            if (i < header.size()-1) sb.append(this.format.getDelimiter());
        }
        return sb.toString();
    }
}
