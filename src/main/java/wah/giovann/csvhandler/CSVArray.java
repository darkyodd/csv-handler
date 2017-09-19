package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * The <code>CSVArray</code> class is a collection <code>CSVRecord</code> objects that represents the contents
 * of at .csv file.
 * <br><br>
 * A <code>CSVArray</code> can be manipulated in a number of ways, such as modifying the header, adding or removing
 * data items or columns, or to sorting based on column names. The <code>CSVArray</code> can then written to file using an instance of the
 * <code>CSVWriter</code> class.
 * <br><br>
 * If a <code>CSVArray</code> instance does not have a header, the use of whats called a dummy header is put into place. With
 * a dummy header, CSV file columns can be referred to by their indices. To check whether a <code>CSVArray</code> has a header,
 * see the {@link #hasHeader()} method.
 * <br><br>
 * In the normal case, a <code>CSVArray</code> instance is created by a <code>CSVReader</code> using the <code>readCSV</code> method.
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVArray extends ArrayList<CSVRecord> {
    private CSVHeader header;

    /**
     * Constructs a new <code>CSVArray</code> instance.
     */
    public CSVArray() {
        super();
        this.header = null;
    }

    /**
     * Constructs a new <code>CSVArray</code> instance based on another <code>CSVArray</code> instance.
     * @param other A <code>CSVArray</code> instance.
     */
    public CSVArray(CSVArray other) {
        this(other, other.header);
    }

    /**
     * Constructs a new <code>CSVArray</code> instance with the specified <code>CSVHeader</code> instance.
     * @param h The <code>CSVHeader</code> instance.
     */
    protected CSVArray(CSVHeader h) {
        super();
        this.header = h;
    }

    /**
     * Constructs a new <code>CSVArray</code> instance with the specified <code>CSVHeader</code> instance and <code>Collection</code>
     * instance.
     * @param c The <code>Collection</code> instance.
     * @param h The <code>CSVHeader</code> instance.
     */
    protected CSVArray(Collection c, CSVHeader h) {
        super(c);
        this.header = h;
    }

    /**
     * Sorts the <code>CSVArray</code> records based on their values in the column at index <code>columnIndex</code>. Whether or not
     * the records should be sorted by treating values as numeric or alphanumeric is determined by the <code>boolean</code> <code>numeric</code>. Whether
     * or not the records should be sorted in ascending or descending order is determined by the <code>boolean</code> <code>ascendingOrder</code>.
     * @param columnIndex Index of the header column with which records will be sorted.
     * @param numeric If true, treat the values found at the index <code>columnIndex</code> as numeric. Otherwise, treat them as alphanumeric.
     * @param ascendingOrder If true, sort the records in ascending order. Otherwise, sort the records in descending order.
     */
    public void sortBy(int columnIndex, boolean numeric, boolean ascendingOrder) {
        if (ascendingOrder) {
            this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                    new Double(r1.getDouble(columnIndex)).compareTo(new Double(r2.getDouble(columnIndex))) :
                    r1.get(columnIndex).compareTo(r2.get(columnIndex))));
        }
        else {
            this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                    new Double(r2.getDouble(columnIndex)).compareTo(new Double(r1.getDouble(columnIndex))) :
                    r2.get(columnIndex).compareTo(r1.get(columnIndex))));
        }
    }

    /**
     * Sorts the <code>CSVArray</code> records based on the field values at <code>column</code>. Whether or not
     * the records should be sorted by treating values as numeric or alphanumeric is determined by the <code>boolean</code> <code>numeric</code>. Whether
     * or not the records should be sorted in ascending or descending order is determined by the <code>boolean</code> <code>ascendingOrder</code>.
     * @param column Header column with which records will be sorted.
     * @param numeric If true, treat the values found at the index <code>columnIndex</code> as numeric. Otherwise, treat them as alphanumeric.
     * @param ascendingOrder If true, sort the records in ascending order. Otherwise, sort the records in descending order.
     */
    public void sortBy(String column, boolean numeric, boolean ascendingOrder) {
        int index = this.header.indexOfColumn(column);
        this.sortBy(index, numeric, ascendingOrder);
    }

    /**
     * Returns a HashMap containing a grouping of CSVRecords in this CSVArray based on
     * the values passed through the column variable.
     * @param columns The columns to group the CSVArray objects by.
     * @return A HashMap object
     */
    public HashMap<String, CSVArray> groupBy(String... columns) {
        if (columns.length == 0) return null;
        HashMap<String, CSVArray> map = new HashMap<>();
        String key;
        for (CSVRecord r : this) {
            if (columns.length == 1) {
                key = r.get(columns[0]);
            } else {
                key = new String();
                for (int i = 0; i < columns.length; i++) {
                    key += r.get(columns[i]);
                    if (i != columns.length - 1) key += "|";
                }
            }
            CSVArray arr = map.get(key);
            if (arr == null) {
                arr = new CSVArray();
                arr.putHeader(this.getHeaderList());
                arr.insertData((ArrayList<String>)r.getValues());
                map.put(key, arr);
            }
            else {
                arr.insertData((ArrayList<String>)r.getValues());
            }
        }
        return map;
    }

    /**
     *Returns an <code>ArrayList</code> of all strings in the header of the file, in the order in which they appear/will appear in the
     * file.
     * @return An <code>ArrayList</code> of all the header values.
     */
    public ArrayList<String> getHeaderList() {
        return this.header.getColumnsList();
    }

    /**
     * Returns the name of the header column at the specified <code>index</code>. If the array does not have
     * a header, the returned <code>String</code> will have the prefix "<code>DUMMY HEADER:</code>", followed simply
     * by the index argument itself.
     * @param index Index of the header to check.
     * @return The name of the column at that index.
     */
    public String getColumnName(int index) {
        return this.header.getColumnName(index);
    }

    /**
     * Returns a <code>String</code> representation of this <code>CSVArray</code>'s header. If this
     * <code>CSVArray</code> does not have a header, this <code>String</code> will indicate that the
     * header is a "<code>DUMMY HEADER</code>", i.e., it does not logically represent any part of the
     * input/output file, and is for internal use only.
     * @return <code>String</code> representation of the <code>CSVArray</code> header.
     */
    public String getHeaderString() {
        return this.header.toString();
    }

    /**
     * Turns all header values into the empty string, i.e., "".
     */
    public void clearHeader() {
        this.header.clearHeader();
    }

    /**
     * Returns an <code>ArrayList</code> containing the value at the specified column index for every <code>CSVRecord</code>
     * in this <code>CSVArray</code>, in the order in which that record appears in the array.
     * @param index The index to build the <code>ArrayList</code> of values from.
     * @return <code>ArrayList</code> of <code>CSVRecord</code> values at the specified index.
     */
    public ArrayList<String> getColumnList(int index) {
        ArrayList<String> ret = new ArrayList<>();
        this.forEach(item -> {
            ret.add(item.get(index));
        });
        return ret;
    }

    /**
     * Returns an <code>ArrayList</code> containing the value at the specified header column for every <code>CSVRecord</code>
     * in this <code>CSVArray</code>, in the order in which that record appears in the array.
     * @param column The header column to build the <code>ArrayList</code> of values from.
     * @return <code>ArrayList</code> of <code>CSVRecord</code> values at the specified header column.
     */
    public ArrayList<String> getColumnList(String column) {
        int index = this.header.indexOfColumn(column);
        return this.getColumnList(index);
    }

    /**
     * Removes the header column at the specified index and all <code>CSVRecord</code> values at that column index.
     * @param columnIndex The index of the column to remove.
     */
    public void removeColumn(int columnIndex) {
        this.forEach(item -> {
            String s = item.remove(columnIndex);
            if (s == null) {
                ArrayList<Object> err = new ArrayList<>();
                err.add(columnIndex);
                err.add(item);
                throw new CSVIntegrityException(CSVIntegrityException.COLUMN_REMOVAL_FAILED, err);
            }
        });
        if (this.header.removeColumn(columnIndex) == null) {
            ArrayList<Object> err = new ArrayList<>();
            err.add(columnIndex);
            err.add(this.header);
            throw new CSVIntegrityException(CSVIntegrityException.COLUMN_REMOVAL_FAILED, err);
        }
    }

    /**
     * Removes the specified header column and all <code>CSVRecord</code> values at that column.
     * @param column The name of the column to remove.
     */
    public void removeColumn(String column) {
        int index = this.header.indexOfColumn(column);
        removeColumn(index);
    }

    /**
     * Returns a <code>boolean</code> value indicating whether or not this <code>CSVArray</code> has a header. If there
     * @return <code>true</code> if this <code>CSVArray</code> has a header, <code>false</code> otherwise.
     */
    public boolean hasHeader() {
        return this.header.getIsDummyHeader();
    }

    /**
     *If this <code>CSVArray</code> has no header, this method will add a new column at the specified index. All
     * <code>CSVRecord</code> values at that index are initiated to the empty string. If the <code>CSVArray</code>
     * has a header, this method does nothing. The <code>{@link #hasHeader()}</code> method can be used to check if a <code>CSVArray</code>
     * has a header or not.
     * @param columnIndex The index at which to add the new column.
     */
    public void addDummyColumn(int columnIndex) {
        if (this.header.getIsDummyHeader()) {
            this.header.addDummyColumn(columnIndex);
            this.forEach(item -> {
                item.insert(columnIndex, "");
            });
        }
    }

    /**
     *If this <code>CSVArray</code> has a header, this method will add a new column at the specified index, with the specified name. All
     * <code>CSVRecord</code> values at that index are initiated to the empty string. If the <code>CSVArray</code>
     * has no header, this method does nothing. The <code>{@link #hasHeader()}</code> method can be used to check if a <code>CSVArray</code>
     * has a header or not.
     * @param columnIndex The index at which to add the new column.
     * @param columnName The name of the new column.
     */
    public void addColumn(int columnIndex, String columnName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.addColumn(columnIndex, columnName);
            this.forEach(item -> {
               item.insert(columnIndex, "");
            });
        }
    }

    /**
     * Removes the header from this <code>CSVArray</code> instance.
     */
    public void removeHeader() {
        this.header.setIsDummyHeader(true);
    }

    /**
     * Adds a header to this <code>CSVArray</code> instance if it has none. Otherwise, the existing header is replaced with
     * the new one.
     * @param newHeader An <code>ArrayList</code> of <code>String</code>s representing the new header information.
     */
    public void putHeader(ArrayList<String> newHeader) {
        if (this.header == null) {
            this.header = new CSVHeader(newHeader);
        }
        else {
            this.header.setIsDummyHeader(false);
            this.header.setColumnNames(newHeader);
        }
    }

    /**
     * Switches the positions of the two columns specified at indices <code>index1</code> and <code>index2</code> in the <code>CSVArray</code> instance.
     * @param index1 The index of the first column to swap.
     * @param index2 The index of the second column to swap.
     */
    public void swapColumns(int index1, int index2) {
        this.header.swapColumns(index1, index2);
        this.forEach(item -> {
           item.swapValues(index1, index2);
        });
    }

    /**
     * Switches the positions of the two columns specified by <code>columnName1</code> and <code>columnName2</code> in the <code>CSVArray</code> instance.
     * @param columnName1 The name of the first column to swap.
     * @param columnName2 The name of the second column to swap.
     */
    public void swapColumns(String columnName1, String columnName2) {
        int index1 = this.header.indexOfColumn(columnName1);
        int index2 = this.header.indexOfColumn(columnName2);
        this.swapColumns(index1, index2);
    }

    /**
     * Creates and inserts a new <code>CSVRecord</code> data item into the array, at the specified index.
     * @param data New data item, represented by an <code>ArrayList</code> of <code>String</code> values.
     * @param index The index at which to insert the new data item.
     * @return The <code>CSVRecord</code> instance created by this operation.
     */
    public CSVRecord insertData(ArrayList<String> data, int index) {
        CSVRecord r = new CSVRecord(this.header, data);
        this.add(index, r);
        return r;
    }

    /**
     * Creates and inserts a new <code>CSVRecord</code> data item into the array.
     * @param data New data item, represented by an <code>ArrayList</code> of <code>String</code> values.
     * @return The <code>CSVRecord</code> instance created by this operation.
     */
    public CSVRecord insertData(ArrayList<String> data) {
        CSVRecord r = new CSVRecord(this.header, data);
        this.add(r);
        return r;
    }

    /**
     * If this <code>CSVArray</code> instance has a header, renames the specified header column. Otherwise,
     * does nothing.
     * @param oldName The name of the header column to be renamed.
     * @param newName The new name of the header column.
     */
    public void renameColumn(String oldName, String newName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.renameColumn(this.header.indexOfColumn(oldName), newName);
        }
    }

    /**
     * If this <code>CSVArray</code> instance has a header, renames the header column at the specified index. Otherwise,
     * does nothing.
     * @param columnIndex The index of the header column to be renamed.
     * @param newName The new name of the header column.
     */
    public void renameColumn(int columnIndex, String newName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.renameColumn(columnIndex, newName);
        }
    }

    /**
     * Merges all the CSVArrays in this map into one CSVArray, assuming they all have the same header.
     * @param map The HashMap to collapse.
     * @return a new CSVArray made of the contents of the map
     */
    public static CSVArray collapse(HashMap<String,CSVArray> map) {
        Set<String> keys = map.keySet();
        CSVArray ret = new CSVArray();
        for (String key : keys) {
            CSVArray add = map.get(key);
            if (!ret.hasHeader() && add.hasHeader() && ret.header.equals(add.header)) ret.putHeader(add.getHeaderList());
            ret.addAll(add);
        }
        return ret;
    }

    /**
     * Replaces this CSVArray's header and CSVRecords with shallow copies of those in the other CSVArray.
     * @param other The CSVArray to copy.
     */
    public void copyState(CSVArray other) {
        this.clear();
        this.header = new CSVHeader(other.header);
        for (CSVRecord r : other) {
            this.insertData((ArrayList<String>)r.getValues());
        }
    }

    /**
     * Returns a <code>String</code> representation of this <code>CSVArray</code> instance, using the specification
     * given in the <code>format</code> argument.
     * @param format The <code>CSVFileFormat</code>
     * @return A <code>String</code> representation of this instance.
     */
    public String csvString(CSVFileFormat format) {
        StringBuilder sb = new StringBuilder();
        if (!this.header.getIsDummyHeader()){
            for (int i = 0; i < header.totalColumns(); i++){
                sb.append(header.getColumnName(i));
                if (i < header.totalColumns()-1) sb.append(format.getDelimiter());
                else {
                    for (char c : format.getOutputFileLineEnd()){
                        sb.append(c);
                    }
                }
            }
        }
        this.forEach(item ->{
            sb.append(((CSVRecord)item).getRecordString(format.getDelimiter()));
            for (char c : format.getOutputFileLineEnd()){
                sb.append(c);
            }
        });
        return sb.toString();
    }


    /**
     * Returns a <code>String</code> representation fo this <code>CSVArray</code> instance.
     * @return A <code>String</code> representation of this instance.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.header.toString());
        this.forEach(item -> {
            sb.append('\n');
            sb.append(item.toString());
        });
        return sb.toString();
    }
}
