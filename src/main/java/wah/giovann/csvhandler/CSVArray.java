package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVArray extends ArrayList<CSVRecord> {
    private CSVHeader header;

    public CSVArray() {
        super();
    }

    protected CSVArray(CSVHeader h) {
        super();
        this.header = h;
    }

    protected CSVArray(Collection c, CSVHeader h) {
        super(c);
        this.header = h;
    }

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

    public void sortBy(String column, boolean numeric, boolean ascendingOrder) {
        int index = this.header.indexOfColumn(column);
        this.sortBy(index, numeric, ascendingOrder);
    }

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
