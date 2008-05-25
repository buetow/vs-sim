/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs;

import java.awt.Color;
import java.io.*;
import java.util.*;

/**
 * The Class VSPrefs.
 */
public abstract class VSPrefs implements Serializable {

    /** The Constant BOOLEAN_PREFIX. */
    public static final String BOOLEAN_PREFIX = "Boolean: ";

    /** The Constant COLOR_PREFIX. */
    public static final String COLOR_PREFIX = "Color: ";

    /** The Constant FLOAT_PREFIX. */
    public static final String FLOAT_PREFIX = "Float: ";

    /** The Constant INTEGER_PREFIX. */
    public static final String INTEGER_PREFIX = "Integer: ";

    /** The Constant LONG_PREFIX. */
    public static final String LONG_PREFIX = "Long: ";

    /** The Constant STRING_PREFIX. */
    public static final String STRING_PREFIX = "String: ";

    /** The color prefs. */
    private HashMap<String,Color> colorPrefs;

    /** The float prefs. */
    private HashMap<String,Float> floatPrefs;

    /** The integer prefs. */
    private HashMap<String,Integer> integerPrefs;

    /** The long prefs. */
    private HashMap<String,Long> longPrefs;

    /** The setting restriction prefs. */
    private HashMap<String,SettingRestriction> settingRestrictionPrefs;

    /** The description prefs. */
    private HashMap<String,String> descriptionPrefs;

    /** The string prefs. */
    private HashMap<String,String> stringPrefs;

    /** The boolean prefs. */
    private HashMap<String,Boolean> booleanPrefs;

    /** The object prefs. */
    private HashMap<String,Object> objectPrefs;

    /** The units. */
    private HashMap<String,String> units;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4L;

    /** The Constant PREFERENCES_FILENAME. */
    protected final static String PREFERENCES_FILENAME = "vs.dat";

    /** The id counter. */
    private static long idCounter;

    /** The id. */
    protected long id;

    /**
     * The Class SettingRestriction.
     */
    public class SettingRestriction implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    /**
     * The Class IntegerSettingRestriction.
     */
    public class IntegerSettingRestriction extends SettingRestriction {
        private static final long serialVersionUID = 1L;
        /** The min value. */
        private int minValue;

        /** The max value. */
        private int maxValue;

        /**
         * Instantiates a new integer setting restriction.
         *
         * @param minValue the min value
         * @param maxValue the max value
         */
        public IntegerSettingRestriction(int minValue, int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        /**
         * Gets the min value.
         *
         * @return the min value
         */
        public int getMinValue() {
            return minValue;
        }

        /**
         * Gets the max value.
         *
         * @return the max value
         */
        public int getMaxValue() {
            return maxValue;
        }
    }

    /**
     * The Class StringSettingRestriction.
     */
    public class StringSettingRestriction extends SettingRestriction {
        private static final long serialVersionUID = 1L;

        /** The possible selections. */
        Vector<String> possibleSelections;

        /**
         * Instantiates a new string setting restriction.
         *
         * @param possibleSelections the possible selections
         */
        public StringSettingRestriction(String [] possibleSelections) {
            this.possibleSelections = new Vector<String>();

            for (String elem : possibleSelections)
                this.possibleSelections.add(elem);
        }

        /**
         * Gets the possible selections.
         *
         * @return the possible selections
         */
        public Vector<String> getPossibleSelections() {
            return possibleSelections;
        }
    }

    /**
     * Instantiates a new lang.process.removeprefs.
     */
    public VSPrefs() {
        colorPrefs = new HashMap<String,Color>();
        descriptionPrefs = new HashMap<String,String>();
        floatPrefs = new HashMap<String,Float>();
        integerPrefs = new HashMap<String,Integer>();
        longPrefs = new HashMap<String,Long>();
        settingRestrictionPrefs = new HashMap<String,SettingRestriction>();
        stringPrefs = new HashMap<String,String>();
        booleanPrefs = new HashMap<String,Boolean>();
        objectPrefs = new HashMap<String,Object>();
        units = new HashMap<String,String>();
        id = ++idCounter;
    }

    /**
     * Clear.
     */
    protected synchronized void clear() {
        colorPrefs.clear();
        floatPrefs.clear();
        integerPrefs.clear();
        longPrefs.clear();
        stringPrefs.clear();
        booleanPrefs.clear();
        objectPrefs.clear();
        descriptionPrefs.clear();
        settingRestrictionPrefs.clear();
    }

    /**
     * Gets the object.
     *
     * @param key the key
     *
     * @return the object
     */
    public synchronized Object getObject(String key) {
        Object val = objectPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such object config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    /**
     * Removes the object.
     *
     * @param key the key
     */
    public synchronized void removeObject(String key) {
        objectPrefs.remove(key);
    }

    /**
     * Object exists.
     *
     * @param key the key
     *
     * @return true, if successful
     */
    public synchronized boolean objectExists(String key) {
        return null != objectPrefs.get(key);
    }

    /**
     * Gets the string.
     *
     * @param key the key
     *
     * @return the string
     */
    public synchronized String getString(String key) {
        String val = stringPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such string config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    /**
     * Gets the integer obj.
     *
     * @param key the key
     *
     * @return the integer obj
     */
    public synchronized Integer getIntegerObj(String key) {
        Integer val = integerPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such integer config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    /**
     * Gets the integer.
     *
     * @param key the key
     *
     * @return the integer
     */
    public int getInteger(String key) {
        return getIntegerObj(key).intValue();
    }

    /**
     * Gets the float obj.
     *
     * @param key the key
     *
     * @return the float obj
     */
    public synchronized Float getFloatObj(String key) {
        Float val = floatPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such float config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    /**
     * Gets the float.
     *
     * @param key the key
     *
     * @return the float
     */
    public float getFloat(String key) {
        return getFloatObj(key).floatValue();
    }

    /**
     * Gets the long obj.
     *
     * @param key the key
     *
     * @return the long obj
     */
    public synchronized Long getLongObj(String key) {
        Long val = longPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such long config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    /**
     * Gets the long.
     *
     * @param key the key
     *
     * @return the long
     */
    public long getLong(String key) {
        return getLongObj(key).longValue();
    }

    /**
     * Gets the boolean obj.
     *
     * @param key the key
     *
     * @return the boolean obj
     */
    public synchronized Boolean getBooleanObj(String key) {
        Boolean val = booleanPrefs.get(key);

        if (val == null)
            return new Boolean(false);

        return val;
    }

    /**
     * Gets the boolean.
     *
     * @param key the key
     *
     * @return the boolean
     */
    public boolean getBoolean(String key) {
        return getBooleanObj(key).booleanValue();
    }

    /**
     * Gets the color.
     *
     * @param key the key
     *
     * @return the color
     */
    public synchronized Color getColor(String key) {
        Color color = colorPrefs.get(key);

        if (color == null) {
            System.err.println("Fatal: No such color config value \""
                               + key + "\"");
            System.exit(1);
        }

        return color;
    }

    /**
     * Gets the description.
     *
     * @param key the key
     *
     * @return the description
     */
    public synchronized String getDescription(String key) {
        return descriptionPrefs.get(key);
    }

    /**
     * Gets the unit.
     *
     * @param key the key
     *
     * @return the unit
     */
    public synchronized String getUnit(String key) {
        return units.get(key);
    }

    /**
     * Gets the restriction.
     *
     * @param key the key
     *
     * @return the restriction
     */
    public synchronized SettingRestriction getRestriction(String key) {
        return settingRestrictionPrefs.get(key);
    }

    /**
     * Gets the string key set.
     *
     * @return the string key set
     */
    public synchronized Set<String> getStringKeySet() {
        return stringPrefs.keySet();
    }

    /**
     * Gets the integer key set.
     *
     * @return the integer key set
     */
    public synchronized Set<String> getIntegerKeySet() {
        return integerPrefs.keySet();
    }

    /**
     * Gets the float key set.
     *
     * @return the float key set
     */
    public synchronized Set<String> getFloatKeySet() {
        return floatPrefs.keySet();
    }

    /**
     * Gets the long key set.
     *
     * @return the long key set
     */
    public synchronized Set<String> getLongKeySet() {
        return longPrefs.keySet();
    }

    /**
     * Gets the boolean key set.
     *
     * @return the boolean key set
     */
    public synchronized Set<String> getBooleanKeySet() {
        return booleanPrefs.keySet();
    }

    /**
     * Gets the color key set.
     *
     * @return the color key set
     */
    public synchronized Set<String> getColorKeySet() {
        return colorPrefs.keySet();
    }

    /**
     * Sets the object.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setObject(String key, Object val) {
        objectPrefs.put(key, val);
    }

    /**
     * Sets the string.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setString(String key, String val) {
        stringPrefs.put(key, val);
    }

    /**
     * Sets the integer.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setInteger(String key, Integer val) {
        integerPrefs.put(key, val);
    }

    /**
     * Sets the color.
     *
     * @param key the key
     * @param color the color
     */
    public synchronized void setColor(String key, Color color) {
        colorPrefs.put(key, color);
    }

    /**
     * Sets the int.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setInt(String key, int val) {
        integerPrefs.put(key, new Integer(val));
    }

    /**
     * Sets the float.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setFloat(String key, float val) {
        floatPrefs.put(key, new Float(val));
    }

    /**
     * Sets the float.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setFloat(String key, Float val) {
        floatPrefs.put(key, val);
    }

    /**
     * Sets the long.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setLong(String key, long val) {
        longPrefs.put(key, new Long(val));
    }

    /**
     * Sets the long.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setLong(String key, Long val) {
        longPrefs.put(key, val);
    }

    /**
     * Sets the boolean.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setBoolean(String key, boolean val) {
        booleanPrefs.put(key, new Boolean(val));
    }

    /**
     * Sets the boolean.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setBoolean(String key, Boolean val) {
        booleanPrefs.put(key, val);
    }

    /**
     * Inits the string.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void initString(String key, String val) {
        if (!stringPrefs.containsKey(key))
            stringPrefs.put(key, val);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void initInteger(String key, Integer val) {
        if (!integerPrefs.containsKey(key))
            integerPrefs.put(key, val);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     */
    public void initInteger(String key, int val) {
        initInteger(key, new Integer(val));
    }

    /**
     * Inits the long.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void initLong(String key, Long val) {
        if (!longPrefs.containsKey(key))
            longPrefs.put(key, val);
    }

    /**
     * Inits the long.
     *
     * @param key the key
     * @param val the val
     */
    public void initLong(String key, int val) {
        initLong(key, new Long(val));
    }

    /**
     * Inits the float.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void initFloat(String key, Float val) {
        if (!floatPrefs.containsKey(key))
            floatPrefs.put(key, val);
    }

    /**
     * Inits the float.
     *
     * @param key the key
     * @param val the val
     */
    public void initFloat(String key, float val) {
        initFloat(key, new Float(val));
    }

    /**
     * Sets the long if unset.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setLongIfUnset(String key, Long val) {
        if (!longPrefs.containsKey(key))
            longPrefs.put(key, val);
    }

    /**
     * Sets the long if unset.
     *
     * @param key the key
     * @param val the val
     */
    public void setLongIfUnset(String key, long val) {
        setLongIfUnset(key, new Long(val));
    }

    /**
     * Inits the boolean.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void initBoolean(String key, Boolean val) {
        if (!booleanPrefs.containsKey(key))
            booleanPrefs.put(key, val);
    }

    /**
     * Inits the boolean.
     *
     * @param key the key
     * @param val the val
     */
    public void initBoolean(String key, boolean val) {
        initBoolean(key, new Boolean(val));
    }

    /**
     * Inits the color.
     *
     * @param key the key
     * @param color the color
     */
    public synchronized void initColor(String key, Color color) {
        if (!colorPrefs.containsKey(key))
            colorPrefs.put(key, color);
    }

    /**
     * Sets the description if unset.
     *
     * @param key the key
     * @param descr the descr
     */
    public synchronized void setDescriptionIfUnset(String key, String descr) {
        if (descr == null || descriptionPrefs.containsKey(key))
            return;
        descriptionPrefs.put(key, descr);
    }

    /**
     * Sets the restriction.
     *
     * @param key the key
     * @param settingRestriction the setting restriction
     */
    public synchronized void setRestriction(String key, SettingRestriction settingRestriction) {
        settingRestrictionPrefs.put(key, settingRestriction);
    }

    /**
     * Sets the unit.
     *
     * @param key the key
     * @param unit the unit
     */
    public synchronized void setUnit(String key, String unit) {
        units.put(key, unit);
    }

    /**
     * Inits the string.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initString(String key, String val, String descr) {
        initString(key, val);
        setDescriptionIfUnset(STRING_PREFIX + key, descr);
    }

    /**
     * Sets the long if unset.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void setLongIfUnset(String key, long val, String descr) {
        setLongIfUnset(key, val);
        setDescriptionIfUnset(LONG_PREFIX + key, descr);
    }

    /**
     * Inits the boolean.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initBoolean(String key, boolean val, String descr) {
        initBoolean(key, val);
        setDescriptionIfUnset(BOOLEAN_PREFIX + key, descr);
    }

    /**
     * Inits the boolean unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param unit the unit
     */
    public void initBooleanUnit(String key, boolean val, String descr, String unit) {
        initBoolean(key, val, descr);
        setUnit(BOOLEAN_PREFIX + key, unit);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param r the r
     */
    public void initInteger(String key, Integer val, String descr, IntegerSettingRestriction r) {
        initInteger(key, val);
        setDescriptionIfUnset(INTEGER_PREFIX + key, descr);
        setRestriction(INTEGER_PREFIX + key, r);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param minValue the min value
     * @param maxValue the max value
     */
    public void initInteger(String key, Integer val, String descr, int minValue, int maxValue) {
        initInteger(key, val, descr, new IntegerSettingRestriction(minValue, maxValue));
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param r the r
     */
    public void initInteger(String key, int val, String descr, IntegerSettingRestriction r) {
        initInteger(key, new Integer(val), descr, r);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param minValue the min value
     * @param maxValue the max value
     */
    public void initInteger(String key, int val, String descr, int minValue, int maxValue) {
        initInteger(key, new Integer(val), descr, minValue, maxValue);
    }

    /**
     * Inits the integer unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param minValue the min value
     * @param maxValue the max value
     * @param unit the unit
     */
    public void initIntegerUnit(String key, int val, String descr, int minValue, int maxValue, String unit) {
        initInteger(key, new Integer(val), descr, minValue, maxValue);
        setUnit(INTEGER_PREFIX + key, unit);
    }

    /**
     * Inits the long.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initLong(String key, Long val, String descr) {
        initLong(key, val);
        setDescriptionIfUnset(LONG_PREFIX + key, descr);
    }

    /**
     * Inits the long.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initLong(String key, int val, String descr) {
        initLong(key, new Long(val), descr);
    }

    /**
     * Inits the long unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param unit the unit
     */
    public void initLongUnit(String key, int val, String descr, String unit) {
        initLong(key, new Long(val), descr);
        setUnit(LONG_PREFIX + key, unit);
    }

    /**
     * Inits the float.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initFloat(String key, Float val, String descr) {
        initFloat(key, val);
        setDescriptionIfUnset(FLOAT_PREFIX + key, descr);
    }

    /**
     * Inits the float.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initFloat(String key, float val, String descr) {
        initFloat(key, new Float(val), descr);
    }

    /**
     * Inits the float unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param unit the unit
     */
    public void initFloatUnit(String key, float val, String descr, String unit) {
        initFloat(key, new Float(val), descr);
        setUnit(FLOAT_PREFIX + key, unit);
    }


    /**
     * Inits the color.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initColor(String key, Color val, String descr) {
        initColor(key, val);
        setDescriptionIfUnset(COLOR_PREFIX + key, descr);
    }

    /**
     * Fill default strings.
     */
    public void fillDefaultStrings() {}

    /**
     * Fill default integers.
     */
    public void fillDefaultIntegers() {}

    /**
     * Fill default floats.
     */
    public void fillDefaultFloats() {}

    /**
     * Fill default colors.
     */
    public void fillDefaultColors() {}

    /**
     * Fill default booleans.
     */
    public void fillDefaultBooleans() {}

    /**
     * Fill with defaults.
     */
    public void fillWithDefaults() {}

    /**
     * Write object.
     *
     * @param objectOutputStream the object output stream
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public synchronized void writeObject(ObjectOutputStream objectOutputStream)
    throws IOException {
        objectOutputStream.writeObject(booleanPrefs);
        objectOutputStream.writeObject(colorPrefs);
        objectOutputStream.writeObject(floatPrefs);
        objectOutputStream.writeObject(integerPrefs);
        objectOutputStream.writeObject(longPrefs);
        objectOutputStream.writeObject(stringPrefs);
        objectOutputStream.writeObject(units);
    }

    /**
     * Read object.
     *
     * @param objectInputStream the object input stream
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    @SuppressWarnings("unchecked") public synchronized void readObject(ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException {
        booleanPrefs = (HashMap<String,Boolean>) objectInputStream.readObject();
        colorPrefs = (HashMap<String,Color>) objectInputStream.readObject();
        descriptionPrefs = new HashMap<String,String>();
        floatPrefs = (HashMap<String,Float>) objectInputStream.readObject();
        integerPrefs = (HashMap<String,Integer>) objectInputStream.readObject();
        longPrefs = (HashMap<String,Long>) objectInputStream.readObject();
        settingRestrictionPrefs = new HashMap<String,SettingRestriction>();
        stringPrefs = (HashMap<String,String>) objectInputStream.readObject();
        units = (HashMap<String,String>) objectInputStream.readObject();
    }

    /*
    public void saveFile(String filename) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    /*
    public static VSPrefs openFile(String filename) {
        VSPrefs prefs = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            prefs = (VSPrefs) objectInputStream.readObject();

            prefs.fillDefaultStrings();
            prefs.fillDefaultIntegers();
            prefs.fillDefaultFloats();
            prefs.fillDefaultColors();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prefs;
    }
    */

    /**
     * Copy integers.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyIntegers(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initInteger(key, getInteger(key), getDescription(INTEGER_PREFIX + key),
                                 (IntegerSettingRestriction) getRestriction(INTEGER_PREFIX + key));
    }

    /**
     * Copy longs.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyLongs(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initLong(key, getLong(key), getDescription(LONG_PREFIX + key));
    }

    /**
     * Copy floats.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyFloats(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initFloat(key, getFloat(key), getDescription(FLOAT_PREFIX + key));
    }

    /**
     * Copy strings.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyStrings(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initString(key, getString(key), getDescription(STRING_PREFIX + key));
    }

    /**
     * Copy colors.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyColors(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initColor(key, getColor(key), getDescription(COLOR_PREFIX + key));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String descr = "";

        Set<String> set = null;

        set = getIntegerKeySet();
        if (set.size() > 0) {
            descr += INTEGER_PREFIX;
            for (String key : set)
                descr += key + "=" + getInteger(key) + "; ";
        }

        set = getLongKeySet();
        if (set.size() > 0) {
            descr += LONG_PREFIX;
            for (String key : set)
                descr += key + "=" + getLong(key) + "; ";
        }

        set = getFloatKeySet();
        if (set.size() > 0) {
            descr += FLOAT_PREFIX;
            for (String key : set)
                descr += key + "=" + getFloat(key) + "; ";
        }

        set = getBooleanKeySet();
        if (set.size() > 0) {
            descr += BOOLEAN_PREFIX;
            for (String key : set)
                descr += key + "=" + getBoolean(key) + "; ";
        }

        set = getStringKeySet();
        if (set.size() > 0) {
            descr += STRING_PREFIX;
            for (String key : set)
                descr += key + "=" + getString(key) + "; ";
        }

        if (descr.endsWith("; "))
            return descr.substring(0, descr.length() - 2);

        return descr;
    }

    /**
     * Gets the iD.
     *
     * @return the iD
     */
    public long getID() {
        return id;
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        if (!colorPrefs.isEmpty())
            return false;

        if (!floatPrefs.isEmpty())
            return false;

        if (!integerPrefs.isEmpty())
            return false;

        if (!longPrefs.isEmpty())
            return false;

        if (!stringPrefs.isEmpty())
            return false;

        if (!booleanPrefs.isEmpty())
            return false;

        return true;
    }
}
