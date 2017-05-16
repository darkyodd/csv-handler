package wah.giovann.csvhandler;

import java.util.ArrayList;

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

    public void clear() {
        this.columnNames.clear();
    }

    public void setIsDummyHeader(boolean b) {
        this.dummyHeader = b;
        setDummyColumns();
    }

    private void setDummyColumns() {
        if (this.dummyHeader) {
            for (int i = 0; i < this.columns(); i++){
                this.renameColumn(i, i+"");
            }
        }
    }

    public boolean getIsDummyHeader() {
        return this.dummyHeader;
    }

    public boolean contains(String columnName) {
        return this.columnNames.contains(columnName);
    }

    public int columns() {
        return this.columnNames.size();
    }

    public String get(int index){
        return this.columnNames.get(index);
    }
    
    public int indexOf(String obj) {
        return this.columnNames.indexOf(obj);
    }

    public boolean remove(String columnName) {
        boolean b = this.columnNames.remove(columnName);
        if (b && this.dummyHeader) this.setDummyColumns();
        return b;
    }

    public String remove(int index) {
        String s = this.columnNames.remove(index);
        if (this.dummyHeader) this.setDummyColumns();
        return s;
    }

    public void add() {
        if (this.dummyHeader) {
            int name = this.columns();
            this.columnNames.add(name+"");
        }
    }

    public void add(String name) {
        if (!this.dummyHeader) this.columnNames.add(name);
    }

    public void add(int index, String name) {
        if (!this.dummyHeader) this.columnNames.add(index, name);
    }

    public void renameColumn(int index, String name) {
        if (!this.dummyHeader) this.columnNames.set(index, name);
    }
}
