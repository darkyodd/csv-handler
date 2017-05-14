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

    public CSVArray(CSVFileFormat f) {
        super();
        this.format = f;

    }

    public CSVArray(Collection c, CSVFileFormat f) {
        super(c);
        this.format = f;
    }

    public String csvString() {
        StringBuilder sb = new StringBuilder();
        if (format.getHasHeader()){
            for (int i = 0; i < format.getDestinationHeader().size(); i++){
                sb.append(format.getDestinationHeader().get(i));
                if (i < format.getDestinationHeader().size()-1) sb.append(format.getDelimiter());
                else {
                    for (char c : format.getDestinationLineEnd()){
                        sb.append(c);
                    }
                }
            }
        }
        for (Object record : this) {
            sb.append(record.toString());
            for (char c : format.getDestinationLineEnd()){
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public String toString() {
        return csvString();
    }
}
