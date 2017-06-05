package wah.giovann.csvhandler.error;

/**
 * Created by giovadmin on 5/11/17.
 */
public class CSVParseException extends Exception {
    public static final int UNEXPECTED_TOKEN = 0;
    public static final int UNEXPECTED_QUOTE = 1;
    public static final int MISSING_CLOSING_QUOTE = 2;
    int errorType;
    Object relatedObject;
    int lineNum;

    public CSVParseException(int e, Object o, int l) {
        this.errorType = e;
        this.relatedObject = o;
        this.lineNum = l;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        switch (this.errorType) {
            case UNEXPECTED_TOKEN:
                sb.append("Unexpected token on file line ");
                sb.append(lineNum);
                sb.append(": \n");
                sb.append(relatedObject.toString());
                sb.append("\n");
                for (int i = 0; i < relatedObject.toString().length()-1; i++){
                    sb.append(" ");
                }
                sb.append("^");
                break;
            case UNEXPECTED_QUOTE:
                sb.append("Unexpected quote on file line ");
                sb.append(lineNum);
                sb.append(": \n");
                sb.append(relatedObject.toString());
                sb.append("\n");
                for (int i = 0; i < relatedObject.toString().length()-1; i++){
                    sb.append(" ");
                }
                sb.append("^");
                break;
            case MISSING_CLOSING_QUOTE:
                sb.append("Expected closing quote on file line ");
                sb.append(lineNum);
                sb.append(": \n");
                sb.append(relatedObject.toString());
                sb.append("\n");
                for (int i = 0; i < relatedObject.toString().length()-1; i++){
                    sb.append(" ");
                }
                sb.append("^");
                break;
        }
        return sb.toString();
    }
}
