package wah.giovann.csvhandler;

import wah.giovann.csvhandler.exception.ValueConversionException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVRecord extends HashMap implements Map {

    private CSVRecord () {
        super();
    }

    private CSVRecord (Map other) {
        super(other);
    }

    public double getDouble(String key){
        String obj = (String) this.get(key);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.DOUBLE, key, this);
        }
    }

    public float getFloat(String key) {
        String obj = (String) this.get(key);
        if (obj != null) {
            return new Float(obj).floatValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.FLOAT, key, this);
        }
    }

    public long getLong(String key) {
        String obj = (String) this.get(key);
        if (obj != null) {
            return new Long(obj).longValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.LONG, key, this);
        }
    }

    public int getInt(String key) {
        String obj = (String) this.get(key);
        if (obj != null) {
            return new Integer(obj).intValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.INT, key, this);
        }
    }

    public char getChar(String key) {
        String obj = (String) this.get(key);
        if (obj != null && obj.length() == 1) {
            return obj.charAt(0);
        }
        else {
            throw new ValueConversionException(ValueConversionException.CHAR, key, this);
        }
    }

    public short getShort(String key) {
        String obj = (String) this.get(key);
        if (obj != null && obj.length() == 1) {
            return new Short(obj).shortValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.SHORT, key, this);
        }
    }

    public byte getByte(String key) {
        String obj = (String) this.get(key);
        if (obj != null && obj.length() == 1) {
            return new Byte(obj).byteValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BYTE, key, this);
        }
    }

    public boolean getBoolean(String key) {
        String obj = (String) this.get(key);
        if (obj != null && obj.length() == 1) {
            return new Boolean(obj).booleanValue();
        }
        else {
            throw new ValueConversionException(ValueConversionException.BOOLEAN, key, this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = this.values().iterator();
        while (it.hasNext()){
            sb.append ( it.next().toString() );
            if (it.hasNext()) sb.append(", ");a
        }
        return sb.toString();
    }
}
