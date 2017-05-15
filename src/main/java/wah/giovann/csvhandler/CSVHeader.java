package wah.giovann.csvhandler;

import java.util.ArrayList;

/**
 * Created by giovadmin on 5/14/17.
 */
public class CSVHeader {
    private ArrayList<String> columnNames;

    public CSVHeader() {
        this.columnNames = new ArrayList<>();
    }

    public CSVHeader(ArrayList<String> h){
        this.columnNames = h;
    }

    public CSVHeader(int columns) {
        this.columnNames = new ArrayList<>();
        for (int col = 0; col < columns; col++){
            this.columnNames.add(new String(col+""));
        }
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
        return this.columnNames.remove(columnName);
    }

    public String remove(int index) {
        return this.columnNames.remove(index);
    }
}
