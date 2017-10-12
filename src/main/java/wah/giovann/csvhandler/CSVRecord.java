package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;
import wah.giovann.csvhandler.error.ValueConversionException;

import java.util.*;

/**
 * The <code>CSVRecord</code> class represents a row of data in a .csv file. <code>CSVRecord</code>s are not designed
 * to be instantiated manually. An instance of of this class
 * can only be created in the context of first creating a <code>CSVArray</code> object, and then accessing its <code>CSVRecord</code>s
 * as you would the members of any other <code>List</code> object. All data in a <code>CSVRecord</code> are stored as <code>String</code>s.
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
     * @param name The name of the header to check for.
     * @return True if the header contains this value
     */
    public boolean containsHeaderColumn(String name){
        return this.sharedHeader.containsColumn(name);
    }

    /**
     * Returns true if this <code>CSVRecord</code> contains this <code>String</code> value at any index.
     * @param value The <code>String</code> to search for.
     * @return True if this object contains the value.
     */
    public boolean containsValue(String value){
        return data.contains(value);
    }

    /**
     * Returns the <code>String</code> value at the specified column.
     * @param columnName The name of the column to get the value from.
     * @return The <code>String</code> value at the specified column.
     */
    public String get(String columnName) {
        if (!this.sharedHeader.containsColumn(columnName)) {
            throw new ArrayIndexOutOfBoundsException("No column named \""+columnName+"\" exists in this CSVRecord.");
        }
        return this.get(this.sharedHeader.indexOfColumn(columnName));
    }

    /**
     * Returns the <code>String</code> value at the specified column.
     * @param column The index of the column to get the value from.
     * @return The <code>String</code> value at the specified column.
     */
    public String get(int column) {
        return data.get(column);
    }

    /**
     * Returns the name of the column at the specified index.
     * @param index The column index
     * @return The name of the column at that index.
     */
    public String getColumnName(int index) {
        return this.sharedHeader.getColumnName(index);
    }

    /**
     * Returns the total number of values in this object.
     * @return The number of values in this <code>CSVRecord.</code>
     */
    public int size(){
        return this.data.size();
    }

    /**
     * Checks if this record is empty or not. A record is considered empty if its values are all null or the empty string.
     * @return <code>true</code> if this CSVRecord is empty.
     */
    public boolean isEmpty() {
        if (this.data.isEmpty()) return true;
        else {
            for (String s : this.data){
                if (s != null && !s.equals("")) return false;
            }
            return true;
        }
    }

    /**
     * Swaps the values at the specified indices.
     * @param index1 The first index.
     * @param index2 The second index.
     */
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

    /**
     * Sets the value at the specified column to the specified value.
     * @param column The column who's value will be set.
     * @param value The new value.
     */
    public void set (String column, Object value) {
        if (this.sharedHeader.containsColumn(column)) {
            this.data.set(this.sharedHeader.indexOfColumn(column), value.toString());
        }
        else {
            throw new CSVIntegrityException(CSVIntegrityException.INVALID_CSVRECORD_ADD, column);
        }
    }

    /**
     * Sets the value at the specified column to the specified value.
     * @param columnIndex The column index who's value will be set.
     * @param value The new value.
     */
    public void set (int columnIndex, Object value) {
        if (columnIndex < this.data.size()) this.data.set(columnIndex, value.toString());
        else throw new CSVIntegrityException(CSVIntegrityException.INVALID_CSVRECORD_ADD, columnIndex);
    }

    /**
     * Returns a list of all the values in this <code>CSVRecord</code>.
     * @return A <code>List</code> containing this record's values.
     */
    public List getValues() {
        return new ArrayList(this.data);
    }

    /**
     * Returns a list of all the column names in this record.
     * @return A <code>List</code> containing this record's header values.
     */
    public List getHeaderList() {
        return this.sharedHeader.getColumnsList();
    }

    /**
     * Returns a <code>String</code> representation of this record's header.
     * @return A <code>String</code> representing the header.
     */
    public String getHeaderString() {
        return this.sharedHeader.toString();
    }

    /**
     * Sets all values of this <code>CSVRecord</code> to the empty string.
     */
    public void clearAll() {
        for (int i = 0; i < this.data.size(); i++){
            clear(i);
        }
    }

    /**
     * Sets the value at the specified column to the empty string.
     * @param columnNum The index of the column to clear.
     */
    public void clear(int columnNum) {
        this.set(columnNum, "");
    }

    /**
     * Sets the value at the specified column to the empty string.
     * @param column The name of the column to clear.
     */
    public void clear(String column) {
        this.set(column, "");
    }

    /**
     * Returns the value at the specified column as a <code>double</code>.
     * @param column The column name who's value should be returned.
     * @return <code>double</code> value at the column.
     */
    public double getDouble(String column){
        String obj = this.get(column);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.DOUBLE, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>double</code>.
     * @param column The column index who's value should be returned.
     * @return <code>double</code> value at the column.
     */
    public double getDouble(int column){
        String obj = this.get(column);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.DOUBLE, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>float</code>.
     * @param column The column name who's value should be returned.
     * @return <code>float</code> value at the column.
     */
    public float getFloat(String column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Float(obj).floatValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.FLOAT, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>flaot</code>.
     * @param column The column index who's value should be returned.
     * @return <code>float</code> value at the column.
     */
    public float getFloat(int column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Float(obj).floatValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.FLOAT, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>long</code>.
     * @param column The column name who's value should be returned.
     * @return <code>long</code> value at the column.
     */
    public long getLong(String column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Long(obj).longValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.LONG, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>long</code>.
     * @param column The column index who's value should be returned.
     * @return <code>long</code> value at the column.
     */
    public long getLong(int column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Long(obj).longValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.LONG, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>int</code>.
     * @param column The column name who's value should be returned.
     * @return <code>int</code> value at the column.
     */
    public int getInt(String column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Integer(obj).intValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.INT, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>int</code>.
     * @param column The column index who's value should be returned.
     * @return <code>int</code> value at the column.
     */
    public int getInt(int column) {
        String obj = this.get(column);
        if (obj != null) {
            return new Integer(obj).intValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.INT, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>char</code>.
     * @param column The column name who's value should be returned.
     * @return <code>char</code> value at the column.
     */
    public char getChar(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return obj.charAt(0);
        }
        else {
            throw new ValueConversionException(ValueConversionException.CHAR, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>char</code>.
     * @param column The column index who's value should be returned.
     * @return <code>char</code> value at the column.
     */
    public char getChar(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return obj.charAt(0);
        }
        else {
            throw new ValueConversionException(ValueConversionException.CHAR, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>short</code>.
     * @param column The column name who's value should be returned.
     * @return <code>short</code> value at the column.
     */
    public short getShort(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Short(obj).shortValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.SHORT, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>short</code>.
     * @param column The column index who's value should be returned.
     * @return <code>short</code> value at the column.
     */
    public short getShort(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Short(obj).shortValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.SHORT, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>byte</code>.
     * @param column The column name who's value should be returned.
     * @return <code>byte</code> value at the column.
     */
    public byte getByte(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Byte(obj).byteValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BYTE, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>byte</code>.
     * @param column The column index who's value should be returned.
     * @return <code>byte</code> value at the column.
     */
    public byte getByte(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Byte(obj).byteValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BYTE, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>boolean</code>.
     * @param column The column name who's value should be returned.
     * @return <code>boolean</code> value at the column.
     */
    public boolean getBoolean(String column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Boolean(obj).booleanValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BOOLEAN, column, this);
        }
    }

    /**
     * Returns the value at the specified column as a <code>boolean</code>.
     * @param column The column index who's value should be returned.
     * @return <code>boolean</code> value at the column.
     */
    public boolean getBoolean(int column) {
        String obj = this.get(column);
        if (obj != null && obj.length() == 1) {
            return new Boolean(obj).booleanValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BOOLEAN, column, this);
        }
    }

    /**
     * Returns a <code>String</code> representation of this <code>CSVRecord</code>, delimited by the <code>char</code>.
     * value passed as argument.
     * @param delimiter The delimiter to use in the representation.
     * @return A <code>String</code> representation of this <code>CSVRecord</code>
     */
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
