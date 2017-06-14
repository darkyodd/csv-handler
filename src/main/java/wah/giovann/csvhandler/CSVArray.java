package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVArray extends ArrayList<CSVRecord> {
    private CSVFileFormat format;
    private CSVHeader header;

    public CSVArray(CSVFileFormat f, CSVHeader h) {
        super();
        this.format = f;
        this.header = h;
    }

    public CSVArray(Collection c, CSVFileFormat f, CSVHeader h) {
        super(c);
        this.format = f;
        this.header = h;
    }

    public void sortBy(String column, boolean numeric, boolean ascendingOrder) {
        if (ascendingOrder) {
            this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                    new Double(r1.getDouble(column)).compareTo(new Double(r2.getDouble(column))) :
                    r1.get(column).compareTo(r2.get(column))));
        }
        else {
            this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                    new Double(r2.getDouble(column)).compareTo(new Double(r1.getDouble(column))) :
                    r2.get(column).compareTo(r1.get(column))));
        }
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

    public List getHeaderList() {
        return this.header.getColumnsList();
    }

    public String getHeaderColumnName(int index) {
        return this.header.getColumnName(index);
    }

    public String getHeaderString() {
        return this.header.toString();
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
        this.forEach(item -> {
            String s = item.remove(column);
            if (s == null) {
                ArrayList<Object> err = new ArrayList<>();
                err.add(column);
                err.add(item);
                throw new CSVIntegrityException(CSVIntegrityException.COLUMN_REMOVAL_FAILED, err);
            }
        });
        if (!this.header.removeColumn(column)) {
            ArrayList<Object> err = new ArrayList<>();
            err.add(column);
            err.add(this.header);
            throw new CSVIntegrityException(CSVIntegrityException.COLUMN_REMOVAL_FAILED, err);
        }
    }

    public void addDummyColumn(int columnIndex) {

    }

    public void addColumn(String columnName, int columnIndex) {
        if (columnIndex < this.header.totalColumns()) {
            this.header.addColumn(columnName);
        }
        else {
            throw new IndexOutOfBoundsException("Column index too large, cannot add column.");
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

    public void switchColumns(int index1, int index2) {

    }

    public void insertData(ArrayList<String> data, int index) {

    }

    public void renameColumn(String oldName, String newName) {
        if (!this.header.getIsDummyHeader()) {
            this.header.renameColumn(this.header.indexOfColumn(oldName), newName);
        }
    }

    public String csvString() {
        StringBuilder sb = new StringBuilder();
        if (format.getHasHeader()){
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
            sb.append(((CSVRecord)item).getRecordString(this.format.getDelimiter()));
            for (char c : format.getOutputFileLineEnd()){
                sb.append(c);
            }
        });
        return sb.toString();
    }

    public String toString() {
        return csvString();
    }
}
