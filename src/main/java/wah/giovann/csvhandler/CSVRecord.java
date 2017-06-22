package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;
import wah.giovann.csvhandler.error.ValueConversionException;

import java.util.*;

/**
 * The <code>CSVRecord</code> class represents a row of data in a .csv file. An instance of of this class
 * can only be created in the context of an existing <code>CSVArray</code> instance.
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVRecord {

    private ArrayList<String> data;
    private CSVHeader sharedHeader;

    protected CSVRecord(CSVHeader h) {
        this.sharedHeader = h;
        this.data = new ArrayList<>();
        for (int i = 0; i < h.totalColumns(); i++){
            this.data.add("");
        }
    }

    protected CSVRecord(CSVHeader h, ArrayList<String> d) {
        if (h.totalColumns() == d.size()) {
            this.sharedHeader = h;
            this.data = new ArrayList(d);
        }
        else {
            List<Object> headerAndData = new ArrayList<>();
            headerAndData.add(h.getColumnsList());
            headerAndData.add(d);
            throw new CSVIntegrityException(CSVIntegrityException.HEADER_AND_RECORD_DATA_INCOMPATABLE, headerAndData);
        }
    }

    protected CSVRecord(CSVRecord other) {
        this.sharedHeader = other.sharedHeader;
        this.data = new ArrayList<>(other.data);
    }

    /**
     * Returns true if one of the data items in this instance is mapped to the header column <code>name</code>.
     * @param name
     * @return
     */
    public boolean containsHeaderColumn(String name){
        return this.sharedHeader.containsColumn(name);
    }

    public boolean containsValue(String value){
        return data.contains(value);
    }

    public String get(String columnName) {
        if (!this.sharedHeader.containsColumn(columnName)) {
            throw new ArrayIndexOutOfBoundsException("No column named \""+columnName+"\" exists in this CSVRecord.");
        }
        return this.get(this.sharedHeader.indexOfColumn(columnName));
    }

    public String get(int column) {
        return data.get(column);
    }

    public String getColumnName(int index) {
        return this.sharedHeader.getColumnName(index);
    }

    public int size(){
        return this.data.size();
    }

    public boolean isEmpty() {
        if (this.data.isEmpty()) return true;
        else {
            for (String s : this.data){
                if (s != null && !s.equals("")) return false;
            }
            return true;
        }
    }

    public void swapValues(int index1, int index2){
        if (index1 >= 0 && index1 < this.data.size() && index2 >= 0 && index1 < this.data.size()) {
            String temp = this.data.get(index1);
            this.data.set(index1, this.data.get(index2));
            this.data.set(index2, temp);
        }
        else {
            ArrayList<Integer> indices = new ArrayList<>();
            indices.add(index1);
            indices.add(index2);
            throw new CSVIntegrityException(CSVIntegrityException.INVALID_VALUE_SWAP, indices);
        }
    }

    public void set (String column, Object value) {
        if (this.sharedHeader.containsColumn(column)) {
            this.data.set(this.sharedHeader.indexOfColumn(column), value.toString());
        }
        else {
            throw new CSVIntegrityException(CSVIntegrityException.INVALID_CSVRECORD_ADD, column);
        }
    }

    public void set (int columnIndex, Object value) {
        if (columnIndex < this.data.size()) this.data.set(columnIndex, value.toString());
        else throw new CSVIntegrityException(CSVIntegrityException.INVALID_CSVRECORD_ADD, columnIndex);
    }

    public List getValues() {
        return new ArrayList(this.data);
    }

    public List getHeaderList() {
        return this.sharedHeader.getColumnsList();
    }

    public String getHeaderString() {
        return this.sharedHeader.toString();
    }

    public void clearAll() {
        for (int i = 0; i < this.data.size(); i++){
            clear(i);
        }
    }

    public void clear(int columnNum) {
        this.set(columnNum, "");
    }

    public void clear(String column) {
        this.set(column, "");
    }

    public double getDouble(String column){
        String obj = this.get(column);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.DOUBLE, column, this);
        }
    }

    public double getDouble(int column){
        String obj = this.get(column);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.DOUBLE, column, this);
        }
    }

    public float getFloat(String column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Float(obj).floatValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.FLOAT, column, this);
        }
    }

    public float getFloat(int column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Float(obj).floatValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.FLOAT, column, this);
        }
    }

    public long getLong(String column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Long(obj).longValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.LONG, column, this);
        }
    }

    public long getLong(int column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Long(obj).longValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.LONG, column, this);
        }
    }

    public int getInt(String column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Integer(obj).intValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.INT, column, this);
        }
    }

    public int getInt(int column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Integer(obj).intValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.INT, column, this);
        }
    }

    public char getChar(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return obj.charAt(0);
        }
        else {
            throw new ValueConversionException(ValueConversionException.CHAR, column, this);
        }
    }

    public char getChar(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return obj.charAt(0);
        }
        else {
            throw new ValueConversionException(ValueConversionException.CHAR, column, this);
        }
    }


    public short getShort(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Short(obj).shortValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.SHORT, column, this);
        }
    }

    public short getShort(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Short(obj).shortValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.SHORT, column, this);
        }
    }

    public byte getByte(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Byte(obj).byteValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BYTE, column, this);
        }
    }

    public byte getByte(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Byte(obj).byteValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BYTE, column, this);
        }
    }

    public boolean getBoolean(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Boolean(obj).booleanValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BOOLEAN, column, this);
        }
    }

    public boolean getBoolean(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Boolean(obj).booleanValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BOOLEAN, column, this);
        }
    }

    public String getRecordString(char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.data.size(); i++){
            sb.append(this.data.get(i));
            if (i < this.data.size()-1) sb.append(delimiter);
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (String s : this.data) {
            sb.append('[');
            sb.append(s);
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        CSVRecord o = (CSVRecord) other;
        if (!o.sharedHeader.equals(this.sharedHeader)) return false;
        else {
            for (int i = 0; i < this.data.size(); i++) {
                if (!this.get(i).equals(o.get(i))) return false;
            }
            return true;
        }
    }

    protected String remove(int columnNum) {
        if (columnNum < this.sharedHeader.totalColumns()) return this.data.remove(columnNum);
        else return null;
    }

    protected String remove(String column){
        if (this.sharedHeader.containsColumn(column)) return this.data.remove(this.sharedHeader.indexOfColumn(column));
        else return null;
    }

    protected void setSharedHeader(CSVHeader header) {
        this.sharedHeader = header;
    }

    protected void insert(int index, String value) {
        this.data.add(index, value);
    }
}
