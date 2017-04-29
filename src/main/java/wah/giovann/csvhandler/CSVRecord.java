package wah.giovann.csvhandler;

import wah.giovann.csvhandler.exception.ValueConversionException;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
/**
 * Created by giovadmin on 4/27/17.
 */
public class CSVRecord<K,V> implements Map<K,V> {

    private CSVObject csvObject;

    private CSVRecord () {
        super();
        this.csvObject = null;
    }

    private CSVRecord (CSVRecord other) {

        this.csvObject = null;
    }

    private CSVRecord (CSVObject obj, List<Entry<K,V>> entries) {

    }

    public V remove(Object key){

        return null;
    }

    public boolean isEmpty() {
        return false;
    }

    public Collection<V> values() {

        return null;
    }

    public Set<Entry<K,V>> entrySet() {

        return null;
    }
    public void clear(){

    }

    public void putAll (Map m) {

    }

    public Set keySet() {

        return null;
    }

    public boolean containsKey(Object key){

        return false;
    }

    public boolean containsValue(Object value) {

        return false;
    }

    public V put(K key, V value) {

        return null;
    }

    public V get(Object o){

        return null;
    }

    public int size() {

        return 0;
    }

    public double getDouble(String key){
        String obj = (String) this.get(key);
        if (obj != null){
            return new Double(obj).doubleValue();
        }
        else {
            throw new ValueConversionException("The data at field '" + key + "' could not be converted to a double.");
        }
    }

        public float getFloat(String key) {
            String obj = (String) this.get(key);
            if (obj != null) {
                return new Float(obj).floatValue();
            }
            else {
                throw new ValueConversionException("The data at field '" + key + "' could not be converted to a float.");
            }
        }

        public long getLong(String key) {
            String obj = (String) this.get(key);
            if (obj != null) {
                return new Long(obj).longValue();
            }
            else {
                throw new ValueConversionException("The data at field '"+key+"' could not be converted to a long.");
            }
        }

        public int getInt(String key) {
            String obj = (String) this.get(key);
            if (obj != null) {
                return new Integer(obj).intValue();
            }
            else {
                throw new ValueConversionException("The data at field '"+key+"' could not be converted to an int.");
            }
        }

        public char getChar(String key) {
            String obj = (String) this.get(key);
            if (obj != null && obj.length() == 1) {
                return obj.charAt(0);
            }
            else {
                throw new ValueConversionException("The data at field '" + key + "' could not be converted to a char.");
            }
    }

}
