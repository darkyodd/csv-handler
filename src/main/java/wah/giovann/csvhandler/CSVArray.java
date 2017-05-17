package wah.giovann.csvhandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVArray<T extends CSVRecord> extends ArrayList {
    /**
     *  State pattern (to let CSVRecord know when certain operations can take place)
     */
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

    public String csvString() {
        StringBuilder sb = new StringBuilder();
        if (format.getHasHeader()){
            for (int i = 0; i < header.totalColumns(); i++){
                sb.append(header.getColumnName(i));
                if (i < header.totalColumns()-1) sb.append(format.getDelimiter());
                else {
                    for (char c : format.getDestinationLineEnd()){
                        sb.append(c);
                    }
                }
            }
        }
        this.forEach(item ->{
            sb.append(((CSVRecord)item).getRecordString(this.format.getDelimiter()));
            for (char c : format.getDestinationLineEnd()){
                sb.append(c);
            }
        });
        return sb.toString();
    }
    public String toString() {
        return csvString();
    }
}
