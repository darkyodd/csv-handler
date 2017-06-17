package wah.giovann.csvhandler;

import wah.giovann.csvhandler.error.CSVIntegrityException;

import java.util.*;

/**
 * Created by giovadmin on 5/14/17.
 */
class CSVHeader {
    private ArrayList<String> columnNames;
    private boolean dummyHeader;

    public CSVHeader() {
        this.columnNames = new ArrayList<>();
        this.dummyHeader = false;
    }

    public CSVHeader(List<String> h) throws CSVIntegrityException {
        if (h != null){
            this.columnNames = new ArrayList<>(h);
            this.dummyHeader = false;
            ArrayList<String> dupColumns = (ArrayList<String>)this.getDuplicates();
            if (dupColumns != null) {
                throw new CSVIntegrityException(CSVIntegrityException.DUPLICATE_COLUMNS_IN_CONSTRUCTOR, dupColumns);
            }
        }
        else {
            throw new CSVIntegrityException(CSVIntegrityException.NULL_CSVHEADER_ARGUMENT, null);
        }
    }

    public CSVHeader(int columns) throws CSVIntegrityException {
        if (columns > 0) {
            this.columnNames = new ArrayList<>();
            this.dummyHeader = true;
            for (int col = 0; col < columns; col++) {
                this.columnNames.add(new String(col + ""));
            }
        }
        else throw new CSVIntegrityException(CSVIntegrityException.INVALID_CSVHEADER_COLUMN_NUMBER, columns);
    }

    public CSVHeader(CSVHeader other) {
        this.dummyHeader = other.dummyHeader;
        this.columnNames = new ArrayList<>(other.columnNames);
    }

    public void clearHeader() {
        for (int i = 0; i < this.columnNames.size(); i++) {
            this.columnNames.set(i, "");
        }
    }

    public void setIsDummyHeader(boolean b) {
        this.dummyHeader = b;
        setDummyColumns();
    }

    public void setDummyColumns() {
        if (this.dummyHeader) {
            for (int i = 0; i < this.totalColumns(); i++){
                this.columnNames.set(i, i+"");
            }
        }
    }

    public boolean getIsDummyHeader() {
        return this.dummyHeader;
    }

    public boolean containsColumn(String columnName) {
        return this.columnNames.contains(columnName);
    }

    public int totalColumns() {
        return this.columnNames.size();
    }

    public String getColumnName(int index){
        if (this.dummyHeader) return "DUMMY HEADER: "+this.columnNames.get(index);
        return this.columnNames.get(index);
    }

    public int indexOfColumn(String columnName) {
        return this.columnNames.indexOf(columnName);
    }

    public boolean removeColumn(String columnName) {
        boolean b = this.columnNames.remove(columnName);
        if (b && this.dummyHeader) this.setDummyColumns();
        return b;
    }

    public String removeColumn(int index) {
        String s = this.columnNames.remove(index);
        if (this.dummyHeader) this.setDummyColumns();
        return s;
    }
    public void addDummyColumn(int index) {
        if (this.dummyHeader) {
            this.columnNames.add(index, null);
            this.setDummyColumns();
        }
    }

    public void addColumn(int index, String name) {
        if (!this.dummyHeader) {
            if (!this.columnNames.contains(name)) this.columnNames.add(index, name);
            else throw new CSVIntegrityException(CSVIntegrityException.DUPLICATE_CSVHEADER_COLUMNS, name);
        }
        else {
            throw new CSVIntegrityException(CSVIntegrityException.ADDING_COLUMN_WITHOUT_HEADER, name);
        }
    }

    public void swapColumns(int index1, int index2){
        if (index1 >= 0 && index1 < this.columnNames.size() && index2 >= 0 && index1 < this.columnNames.size()) {
            String temp = this.columnNames.get(index1);
            this.columnNames.set(index1, this.columnNames.get(index2));
            this.columnNames.set(index2, temp);
        }
        else {
            ArrayList<Integer> indices = new ArrayList<>();
            indices.add(index1);
            indices.add(index2);
            throw new CSVIntegrityException(CSVIntegrityException.INVALID_VALUE_SWAP, indices);
        }
    }

    public void renameColumn(int index, String name) {
        if (!this.dummyHeader && !this.columnNames.contains(name)) this.columnNames.set(index, name);
    }

    public void setColumnNames(ArrayList<String> names) {
        if (names.size() == this.columnNames.size()) {
            this.columnNames = names;
        }
        else{
            ArrayList<Integer> lengths = new ArrayList<>();
            lengths.add(this.columnNames.size());
            lengths.add(names.size());
            throw new CSVIntegrityException(CSVIntegrityException.INVALID_HEADER_LENGTH, lengths);
        }
    }

    public ArrayList<String> getColumnsList() {
        return new ArrayList(this.columnNames);
    }

    public boolean hasDuplicates() {
        Set<String> temp = new HashSet<String>(this.columnNames);
        if (temp.size() < this.totalColumns()) return true;
        else return false;
    }

    public List<String> getDuplicates() {
        if (this.hasDuplicates()) {
            ArrayList<String> ret = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>(this.columnNames);
            Collections.sort(temp);
            for (int i = 0; i < temp.size(); i++) {
                if (i < temp.size()-1 && temp.get(i).equals(temp.get(i+1)) && !ret.contains(temp.get(i)) )
                    ret.add(temp.get(i));
            }
            return ret;
        }
        else return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DUMMY HEADER? "+this.dummyHeader+"\n");
        sb.append("COLUMNS: \n");
        for (String s : this.columnNames) {
            sb.append('[');
            sb.append(s);
            sb.append(']');
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        CSVHeader h = (CSVHeader) o;
        if (this.dummyHeader != h.dummyHeader) return false;
        else if (this.totalColumns() != h.totalColumns()) return false;
        else {
            for (int i = 0; i < this.totalColumns(); i++){
                if (!this.getColumnName(i).equals(h.getColumnName(i))) return false;
            }
        }
        return true;
    }
}

