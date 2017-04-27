package wah.giovann.csvhandler.specification;

import java.nio.charset.Charset;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVSpecification {
    public static final CSVSpecification DEFAULT_SPECIFICATION = new CSVSpecification(
            new CSVSpecification.Builder()
    );
    private char delimiter;
    private char line_end;
    private Charset character_encoding;
    private boolean header;
    private boolean embedded_line_breaks;

    private CSVSpecification(Builder b){
        this.delimiter = b.delimiter;
        this.line_end = b.line_end;
        this.character_encoding = b.character_encoding;
        this.header = b.header;
        this.embedded_line_breaks = b.embedded_line_breaks;
    }

    public static class Builder {
        private char delimiter;
        private char line_end;
        private Charset character_encoding;
        private boolean header;
        private boolean embedded_line_breaks;

        public Builder(){
            this.delimiter = ',';
            this.line_end = '\n';
            this.character_encoding = Charset.defaultCharset();
            this.header = true;
            this.embedded_line_breaks = false;
        }
        public Builder delimiter(char c){
            this.delimiter = c;
            return this;
        }
        public Builder lineEnd(char c) {
            this.line_end = c;
            return this;
        }
        public Builder characterEncoding(Charset c){
            this.character_encoding = c;
            return this;
        }
        public Builder header(boolean b){
            this.header = b;
            return this;
        }
        public Builder embeddedLineBreaks(boolean b){
            this.embedded_line_breaks = b;
            return this;
        }
        public CSVSpecification build() {
            return new CSVSpecification(this);
        }
    }
}
