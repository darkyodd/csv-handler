package wah.giovann.csvhandler;

import java.nio.charset.StandardCharsets;

/**
 *<code>CSVFileFormat</code> allows for the specification of input and/or output .csv file formats.
 * <br><br>
 *A <code>CSVFileFormat</code> instance can be created by either using the <code>CSVFileFormat.DEFAULT_FORMAT</code> member of the class, or by customizing a <code>CSVFileFormat.Builder</code> instance.
 * <br><br>
 *For example, to build an input <code>CSVFileFormat</code> instance that reads from a file with <code>UTF-16</code>
 * encoding, semicolon delimiters, and no header, the following code would work:
 * <br><br>
 *
 *     <code>&nbsp;&nbsp;CSVFileFormat format = new CSVFileFormat.Builder()</code><br>
 *     <code>&nbsp;&nbsp;&nbsp;.characterSetName(CSVFileFormat.UTF_16_CHARSET)</code><br>
 *     <code>&nbsp;&nbsp;&nbsp;.delimiter(CSVFileFormat.SEMICOLON_DELIMITER)</code><br>
 *     <code>&nbsp;&nbsp;&nbsp;.hasHeader(false)</code><br>
 *     <code>&nbsp;&nbsp;&nbsp;.build();</code><br>
 *
 * @author Giovann Wah
 * @version 1.0
 */
public class CSVFileFormat {
    /**
     * Constant for the line feed (\n) line end for an output file.
     */
    public static final char[] LINE_FEED_LINE_END = new char[]{'\n'};

    /**
     * Constant for the carriage return (\r) line end for an output file.
     */
    public static final char[] CARRIAGE_RETURN_LINE_END = new char[]{'\r'};

    /**
     * Constant for the carriage return + line feed (\r\n) line end for an output file.
     */
    public static final char[] CARRIAGE_RETURN_LINE_FEED_LINE_END = new char[]{'\r','\n'};

    /**
     * Constant for a comma (,) CSV delimiter.
     */
    public static final char COMMA_DELIMITER = ',';

    /**
     * Constant for a tab (\t) CSV delimiter.
     */
    public static final char TAB_DELIMITER = '\t';

    /**
     * Constant for a decimal (.) CSV delimiter.
     */
    public static final char DECIMAL_DELIMITER = '.';

    /**
     * Constant for a semicolon (;) CSV delimiter.
     */
    public static final char SEMICOLON_DELIMITER = ';';

    /**
     * Constant for a colon (:) CSV delimiter.
     */
    public static final char COLON_DELIMITER = ':';

    /**
     * Constant for a space ( ) CSV delimiter.
     */
    public static final char SPACE_DELIMITER = ' ';

    /**
     * Constant for a pipe (|) CSV delimiter.
     */
    public static final char PIPE_DELIMITER = '|';

    /**
     * Constant for the ISO-8859-1 character set.
     */
    public static final String ISO_8859_1_CHARSET = StandardCharsets.ISO_8859_1.name();

    /**
     * Constant for the US-ASCII character set.
     */
    public static final String US_ASCII_CHARSET = StandardCharsets.US_ASCII.name();

    /**
     * Constant for the UTF-16 character set.
     */
    public static final String UTF_16_CHARSET = StandardCharsets.UTF_16.name();

    /**
     * Constant for the UTF-16BE character set.
     */
    public static final String UTF_16BE_CHARSET = StandardCharsets.UTF_16BE.name();

    /**
     * Constant for the UTF-16LE character set.
     */
    public static final String UTF_16LE_CHARSET = StandardCharsets.UTF_16LE.name();

    /**
     * Constant for the UTF-8 character set.
     */
    public static final String UTF_8_CHARSET = StandardCharsets.UTF_8.name();

    /**
     * Constant for the default <code>CSVFileFormat</code> instance.<br><br>The default <code>CSVFileFormat</code>
     * instance uses the <code>COMMA_DELIMITER</code> delimiter, the <code>LINE_FEED_LINE_END</code> output file line end,
     * specifies that the input file has a header, specifies that fields with leading or trailing white space should be trimmed,
     * and automatically determines the character encoding of the input file.
     */
    public static final CSVFileFormat DEFAULT_FORMAT = new CSVFileFormat(
            new CSVFileFormat.Builder()
    );

    private char delimiter;
    private boolean trimSpace;
    private boolean hasHeader;
    private String characterSetName;
    private char[] outputFileLineEnd;

    /**
     *Returns a <code>boolean</code> value representing whether or not leading and trailing spaces should be
     * trimmed from fields in a record.
     * @return <code>true</code> of the input file should trim extra spaces, <code>false</code> otherwise.
     */
    public boolean getTrimSpace() { return this.trimSpace; }

    /**
     *Returns a <code>boolean</code> value representing whether or not the input file has a header.
     * @return <code>true</code> if the input file has a header, <code>false</code> otherwise.
     */
    public boolean getHasHeader(){
        return this.hasHeader;
    }

    /**
     *Returns the delimiter of the input/output file.
     * @return the <code>char</code> value chosen as the delimter for the input/output CSV file.
     */
    public char getDelimiter() {
        return this.delimiter;
    }

    /**
     *Returns the <code>String</code> representation of the canonical name of the selected character set.
     * @return <code>String</code> representation of the character set.
     */
    public String getCharacterSetName() {
        return this.characterSetName;
    }

    /**
     *Returns the <code>char</code> array representing the output file line end. This array can consist of up
     * to two characters, in one of three configurations: A line feed character, a carriage return character,
     * or a carriage return character followed by a line feed character.
     * @return <code>char</code> array representing the output file line end.
     */
    public char[] getOutputFileLineEnd() {
        return this.outputFileLineEnd;
    }

    private CSVFileFormat(Builder b){
        this.delimiter = b.delimiter;
        this.outputFileLineEnd = b.outputFileLineEnd;
        this.characterSetName = b.characterSetName;
        this.hasHeader = b.hasHeader;
        this.trimSpace = b.trimSpace;
    }

    /**
     *Returns a <code>String</code> representation of this object.
     * @return A <code>String</code> representing the state of the <code>CSVFileFormat</code> instance.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Delimiter: ");
        switch(this.delimiter) {
            case COMMA_DELIMITER:
                sb.append("COMMA DELIMITER");
                break;
            case TAB_DELIMITER:
                sb.append("TAB DELIMITER");
                break;
            case DECIMAL_DELIMITER:
                sb.append("DECIMAL DELIMITER");
                break;
            case SEMICOLON_DELIMITER:
                sb.append("SEMICOLON DELIMITER");
                break;
            case COLON_DELIMITER:
                sb.append("COLON DELIMITER");
                break;
            case SPACE_DELIMITER:
                sb.append("SPACE DELIMITER");
                break;
            case PIPE_DELIMITER:
                sb.append("PIPE DELIMITER");
                break;
            default:
                sb.append("'"+this.delimiter+"'");
                break;
        }
        sb.append("\n");
        sb.append("Destination Line End: ");
        if (this.outputFileLineEnd.length == 2) sb.append("CARRIAGE RETURN + LINE FEED");
        else if (this.outputFileLineEnd[0] == '\n') sb.append("LINE FEED");
        else sb.append("CARRIAGE RETURN");
        sb.append("\n");
        sb.append("Charset Name: "+this.characterSetName+"\n");
        sb.append("Has Header: "+this.hasHeader+"\n");
        sb.append("Trim Space: "+this.trimSpace);
        return sb.toString();
    }

    /**
     *A builder class used to construct a <code>CSVFileFormat</code> object.<br><br>
     *For example, to build an input <code>CSVFileFormat</code> instance that reads from a file with <code>UTF-16</code>
     * encoding, semicolon delimiters, and no header, the following code would work:
     * <br><br>
     *
     *     <code>&nbsp;&nbsp;CSVFileFormat format = new CSVFileFormat.Builder()</code><br>
     *     <code>&nbsp;&nbsp;&nbsp;.characterSetName(CSVFileFormat.UTF_16_CHARSET)</code><br>
     *     <code>&nbsp;&nbsp;&nbsp;.delimiter(CSVFileFormat.SEMICOLON_DELIMITER)</code><br>
     *     <code>&nbsp;&nbsp;&nbsp;.hasHeader(false)</code><br>
     *     <code>&nbsp;&nbsp;&nbsp;.build();</code><br><br>
     *Note that if the character set name for an input file is not specified, the <code>CSVReader</code> will
     * attempt to determine it automatically.
     * @author Giovann Wah
     * @version 1.0
     */
    public static class Builder {
        private char delimiter;
        private boolean trimSpace;
        private boolean hasHeader;
        private String characterSetName;
        private char[] outputFileLineEnd;

        /**
         * Constructs a default <code>CSVFileFormat.Builder</code> instance with the following default settings:<br>
         * <code>CSVFileFormat.COMMA_DELIMITER</code> file delimiter, <code>CSVFileFormat.LINE_FEED_LINE_END</code> file
         * line end, no character set specified, leading and trailing space should be trimmed, and the input file has a
         * header.
         */
        public Builder(){
            this.delimiter = CSVFileFormat.COMMA_DELIMITER;
            this.outputFileLineEnd = CSVFileFormat.LINE_FEED_LINE_END;
            this.characterSetName = null;
            this.hasHeader = true;
            this.trimSpace = true;
        }

        /**
         * Sets the file delimiter of the <code>CSVFileFormat</code> instance to be built.
         * @param c <code>char</code> to use as the delimiter.
         * @return <code>CSVFileFormat.Builder</code> instance.
         */
        public Builder delimiter(char c){
            this.delimiter = c;
            return this;
        }

        /**
         * Sets the output file line end <code>char</code> of the <code>CSVFileFormat</code> instance
         * to be built. This array can consist of up to two characters in length, in one of three configurations: A
         * line feed character, a carriage return character, or a carriage return character followed by a line feed character.
         * @param c <code>char</code> array to use as the line end.
         * @return <code>CSVFileFormat.Builder</code> instance.
         */
        public Builder outputFileLineEnd(char[] c) {
            this.outputFileLineEnd = c;
            return this;
        }

        /**
         * Sets the character set name of the <code>CSVFileFormat</code> instance to be built. Must be the
         * canonical name of the character set. By default, the character set is <code>null</code>; when null, the <code>CSVReader</code>
         * will attempt to determine the character set automatically.
         * @param s <code>String</code> representation the character set.
         * @return <code>CSVFileFormat.Builder</code> instance.
         */
        public Builder characterSetName(String s){
            this.characterSetName = s;
            return this;
        }

        /**
         * Sets a <code>boolean</code> value determining whether or not the input file has a header.
         * @param h If true, the input file has a header. Otherwise, the input file will be treated as though it doesn't.
         * @return <code>CSVFileFormat.Builder</code> instance.
         */
        public Builder hasHeader(boolean h){
            this.hasHeader = h;
            return this;
        }

        /**
         * Sets a <code>boolean</code> value determining whether or not the input fields should have leading
         * and trailing space trimmed.
         * @param b If true, leading and trailing space will be trimmed. Otherwise, no action will be performed.
         * @return <code>CSVFileFormat.Builder</code> instance.
         */
        public Builder trimSpace(boolean b){
            this.trimSpace = b;
            return this;
        }

        /**
         * Creates a new <code>CSVFileFormat</code> instance with all of the settings of this <code>CSVFileFormat.Builder</code>
         * instance.
         * @return A new <code>CSVFileFormat</code> instance.
         */
        public CSVFileFormat build() {
            return new CSVFileFormat(this);
        }
    }
}
