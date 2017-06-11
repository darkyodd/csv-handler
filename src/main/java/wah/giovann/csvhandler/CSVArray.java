package wah.giovann.csvhandler;

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

    public void sortBy(String column, boolean numeric) {
    //    this.sort((CSVRecord r1, CSVRecord r2)->(numeric?r1.getDouble(column)));
    }

    public void sortBy(int columnIndex, boolean numeric) {

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
