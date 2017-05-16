package wah.giovann.csvhandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    public CSVHeader(ArrayList<String> h) throws Exception{
        if (h != null){
            this.columnNames = h;
            this.dummyHeader = false;
            if (this.hasDuplicates()) {
                throw new Exception("The ArrayList argument cannot contain duplicate values.");
            }
        }
        else {
            throw new Exception("'NULL' argument cannot be passed to CSVHeader constructor.");
        }
    }

    public CSVHeader(int columns) throws Exception {
        if (columns > 0) {
            this.columnNames = new ArrayList<>();
            this.dummyHeader = true;
            for (int col = 0; col < columns; col++) {
                this.columnNames.add(new String(col + ""));
            }
        }
        else throw new Exception("The integer argument to CSVHeader must be greater than 0");
    }

    public void clearColumns() {
        this.columnNames.clear();
    }

    public void setIsDummyHeader(boolean b) {
        this.dummyHeader = b;
        setDummyColumns();
    }

    private void setDummyColumns() {
        if (this.dummyHeader) {
            for (int i = 0; i < this.totalColumns(); i++){
                this.renameColumn(i, i+"");
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

    public String getColumn(int index){
        return this.columnNames.get(index);
    }

    public int indexOf(String obj) {
        return this.columnNames.indexOf(obj);
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

    public void addColumn() {
        if (this.dummyHeader) {
            int name = this.totalColumns();
            this.columnNames.add(name+"");
        }
    }

    public void addColumn(String name) {
        if (!this.dummyHeader && !this.columnNames.contains(name)) this.columnNames.add(name);
    }

    public void addColumn(int index, String name) {
        if (!this.dummyHeader && !this.columnNames.contains(name)) this.columnNames.add(index, name);
    }

    public void renameColumn(int index, String name) {
        if (!this.dummyHeader && !this.columnNames.contains(name)) this.columnNames.set(index, name);
    }

    public boolean hasDuplicates() {
        Set<String> temp = new HashSet<String>(this.columnNames);
        if (temp.size() < this.totalColumns()) return true;
        else return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dummy Header: "+this.dummyHeader+"\n");
        for (String s : this.columnNames) {
            sb.append('[');
            sb.append(s);
            sb.append(']');
            sb.append('\n');
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        CSVHeader h = (CSVHeader) o;
        if (this.dummyHeader != h.dummyHeader) return false;
        else if (this.totalColumns() != h.totalColumns()) return false;
        else {
            for (int i = 0; i < this.totalColumns(); i++){
                if (!this.getColumn(i).equals(h.getColumn(i))) return false;
            }
        }
        return true;
    }
}
