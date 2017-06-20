package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The <code>CSVArray</code> class is a collection <code>CSVRecord</code> objects that represents the contents
 * of at .csv file.
 * <br><br>
 * A <code>CSVArray</code> can be manipulated in a number of ways, such as modifying the header, adding or removing
 * data items or columns, or to sorting based on column names. The <code>CSVArray</code> can then written to file using an instance of the
 * <code>CSVWriter</code> class.
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
     * @param columnIndex index of the header column with which records will be sorted.
     * @param numeric if true, treat the values found at the index <code>columnIndex</code> as numeric. Otherwise, treat them as alphanumeric.
     * @param ascendingOrder if true, sort the records in ascending order. Otherwise, sort the records in descending order.
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
     * @param column header column with which records will be sorted.
     * @param numeric if true, treat the values found at the index <code>columnIndex</code> as numeric. Otherwise, treat them as alphanumeric.
     * @param ascendingOrder if true, sort the records in ascending order. Otherwise, sort the records in descending order.
     */
    public void sortBy(String column, boolean numeric, boolean ascendingOrder) {
        int index = this.header.indexOfColumn(column);
        this.sortBy(index, numeric, ascendingOrder);
    }

    /**
     *Returns an <code>ArrayList</code> of all strings in the header of the file, in the order in which they appear/will appear in the
     * file.
     * @return An <code>ArrayList</code> of all the header values.
     */
    public ArrayList<String> getHeaderList() {
        return this.header.getColumnsList();
    }

    public String getColumnName(int index) {
        return this.header.getColumnName(index);
    }

    public String getHeaderString() {
        return this.header.toString();
    }

    public void clearHeader() {
        this.header.clearHeader();
    }

    public ArrayList<String> getColumnList(int index) {
        ArrayList<String> ret = new ArrayList<>();
        this.forEach(item -> {
            ret.add(item.get(index));
        });
        return ret;
    }

    public ArrayList<String> getColumnList(String column) {
        int index = this.header.indexOfColumn(column);
        return this.getColumnList(index);
    }

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

    public void removeColumn(String column) {
        int index = this.header.indexOfColumn(column);
        removeColumn(index);
    }

    public void addDummyColumn(int columnIndex) {
        if (this.header.getIsDummyHeader()) {
            this.header.addDummyColumn(columnIndex);
            this.forEach(item -> {
                item.insert(columnIndex, "");
            });
        }
    }

    public void addColumn(int columnIndex, String columnName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.addColumn(columnIndex, columnName);
            this.forEach(item -> {
               item.insert(columnIndex, "");
            });
        }
    }

    /**
     * Written while high-af
     */
    public void removeHeader() {
        this.header.setIsDummyHeader(true);
    }

    public void putHeader(ArrayList<String> newHeader) {
        this.header.setIsDummyHeader(false);
        this.header.setColumnNames(newHeader);
    }

    public void swapColumns(int index1, int index2) {
        this.header.swapColumns(index1, index2);
        this.forEach(item -> {
           item.swapValues(index1, index2);
        });
    }

    public void swapColumns(String columnName1, String columnName2) {
        int index1 = this.header.indexOfColumn(columnName1);
        int index2 = this.header.indexOfColumn(columnName2);
        this.swapColumns(index1, index2);
    }

    public CSVRecord insertData(ArrayList<String> data, int index) {
        CSVRecord r = new CSVRecord(this.header, data);
        this.add(index, r);
        return r;
    }

    public CSVRecord insertData(ArrayList<String> data) {
        CSVRecord r = new CSVRecord(this.header, data);
        this.add(r);
        return r;
    }

    public void renameColumn(String oldName, String newName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.renameColumn(this.header.indexOfColumn(oldName), newName);
        }
    }

    public void renameColumn(int columnIndex, String newName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.renameColumn(columnIndex, newName);
        }
    }

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
