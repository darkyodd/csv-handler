package wah.giovann.csvhandler.format;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVFormat {
    public static final CSVFormat DEFAULT_FORMAT = new CSVFormat(
            new CSVFormat.Builder()
    );
    private char delimiter;
    private char[] line_end;
    private Charset character_encoding;
    private boolean hasHeader;
    private boolean embedded_line_breaks;
    private ArrayList<String> header;

    public boolean hasHeader(){
        return this.hasHeader;
    }

    public char getDelimiter() {
        return this.delimiter;
    }

    public Charset getCharacterEncoding() {
        return this.character_encoding;
    }

    public char[] getLineEnd() {
        return this.line_end;
    }

    public boolean getEmbeddedLineBreaks() {
        return this.embedded_line_breaks;
    }

    public ArrayList<String> getHeader() {
        return this.header;
    }

    private CSVFormat(Builder b){
        this.delimiter = b.delimiter;
        this.line_end = b.line_end;
        this.character_encoding = b.character_encoding;
        this.hasHeader = b.hasHeader;
        this.embedded_line_breaks = b.embedded_line_breaks;
        this.header = b.header;
    }

    public static class Builder {
        private char delimiter;
        private char[] line_end;
        private Charset character_encoding;
        private boolean hasHeader;
        private boolean embedded_line_breaks;
        private ArrayList<String> header;

        public Builder(){
            this.delimiter = ',';
            this.line_end = new char[]{'\n'};
            this.character_encoding = Charset.defaultCharset();
            this.hasHeader = false;
            this.header = null;
            this.embedded_line_breaks = false;
        }
        public Builder delimiter(char c){
            this.delimiter = c;
            return this;
        }
        public Builder lineEnd(char[] c) {
            this.line_end = c;
            return this;
        }
        public Builder characterEncoding(Charset c){
            this.character_encoding = c;
            return this;
        }
        public Builder csvHasHeader(boolean h){
            this.hasHeader = h;
            return this;
        }
        public Builder header(ArrayList b){
            this.header = b;
            if (b == null || b.size() == 0) this.hasHeader = false;
            else this.hasHeader = true;
            return this;
        }
        public Builder embeddedLineBreaks(boolean b){
            this.embedded_line_breaks = b;
            return this;
        }
        public CSVFormat build() {
            return new CSVFormat(this);
        }
    }
}
