package wah.giovann.csvhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVArray extends ArrayList<CSVRecord> {
    public static final int ASCENDING_ORDER = 0;
    public static final int DECENDING_ORDER = 1;

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

    public void sortBy(String column, boolean numeric, int order) {
        switch (order) {
            case ASCENDING_ORDER:
                this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                        new Double(r1.getDouble(column)).compareTo(new Double(r2.getDouble(column))) :
                        r1.get(column).compareTo(r2.get(column))));
                break;
            default:
                this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                        new Double(r2.getDouble(column)).compareTo(new Double(r1.getDouble(column))) :
                        r2.get(column).compareTo(r1.get(column))));

        }
    }

    public void sortBy(int columnIndex, boolean numeric, int order) {
        switch (order) {
            case ASCENDING_ORDER:
                this.sort((CSVRecord r1, CSVRecord r2) -> (numeric ?
                        new Double(r1.getDouble(columnIndex)).compareTo(new Double(r2.getDouble(columnIndex))) :
                        r1.get(columnIndex).compareTo(r2.get(columnIndex))));
                break;
            default:
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
