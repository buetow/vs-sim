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
public class VSPrefs implements Serializable {
    /** The Constant BOOLEAN_PREFIX. */
    public static final String BOOLEAN_PREFIX = "Boolean: ";

    /** The Constant COLOR_PREFIX. */
    public static final String COLOR_PREFIX = "Color: ";

    /** The Constant FLOAT_PREFIX. */
    public static final String FLOAT_PREFIX = "Float: ";

    /** The Constant INTEGER_PREFIX. */
    public static final String INTEGER_PREFIX = "Integer: ";

    /** The Constant VECTOR_PREFIX. */
    public static final String VECTOR_PREFIX = "Vector: ";

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

    /** The integer vector prefs. */
    private HashMap<String,Vector<Integer>> vectorPrefs;

    /** The long prefs. */
    private HashMap<String,Long> longPrefs;

    /** The setting restriction prefs. */
    private HashMap<String,VSPrefsRestriction> restrictions;

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
     * Instantiates a new lang.process.removeprefs.
     */
    public VSPrefs() {
        colorPrefs = new HashMap<String,Color>();
        descriptionPrefs = new HashMap<String,String>();
        floatPrefs = new HashMap<String,Float>();
        integerPrefs = new HashMap<String,Integer>();
        vectorPrefs = new HashMap<String,Vector<Integer>>();
        longPrefs = new HashMap<String,Long>();
        restrictions = new HashMap<String,VSPrefsRestriction>();
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
        vectorPrefs.clear();
        longPrefs.clear();
        stringPrefs.clear();
        booleanPrefs.clear();
        objectPrefs.clear();
        descriptionPrefs.clear();
        restrictions.clear();
    }

    /* Unit methods */

    /**
     * Gets the unit.
     *
     * @param key the key
     *
     * @return the unit
     */
    public synchronized String getUnit(String fullKey) {
        return units.get(fullKey);
    }

    /**
     * Sets the unit.
     *
     * @param key the key
     * @param unit the unit
     */
    public synchronized void initUnit(String key, String unit) {
        if (unit == null || units.containsKey(key))
            return;
        units.put(key, unit);
    }

    /* Description methods */
    /**
     * Sets the description if unset.
     *
     * @param key the key
     * @param descr the descr
     */
    public synchronized void initDescription(String key, String descr) {
        if (descr == null || descriptionPrefs.containsKey(key))
            return;
        descriptionPrefs.put(key, descr);
    }

    /**
     * Gets the description.
     *
     * @param key the key
     *
     * @return the description
     */
    public synchronized String getDescription(String fullKey) {
        return descriptionPrefs.get(fullKey);
    }

    /* Restriction methods */

    /**
     * Gets the restriction.
     *
     * @param key the key
     *
     * @return the restriction
     */
    public synchronized VSPrefsRestriction getRestriction(String fullKey) {
        return restrictions.get(fullKey);
    }

    /**
     * Sets the restriction.
     *
     * @param key the key
     * @param settingRestriction the setting restriction
     */
    public synchronized void initRestriction(String key, VSPrefsRestriction settingRestriction) {
        restrictions.put(key, settingRestriction);
    }

    /* Object methods */

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
     * Sets the object.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setObject(String key, Object val) {
        objectPrefs.put(key, val);
    }

    /* Boolean methods */

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
     * Gets the boolean key set.
     *
     * @return the boolean key set
     */
    public synchronized Set<String> getBooleanKeySet() {
        return booleanPrefs.keySet();
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
     * Inits the boolean.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initBoolean(String key, boolean val, String descr) {
        initBoolean(key, val);
        initDescription(BOOLEAN_PREFIX + key, descr);
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
     * Sets the boolean.
     *
     * @param key the key
     * @param val the val
     */
    public void setBoolean(String key, boolean val) {
        setBoolean(key, new Boolean(val));
    }

    /* Color methods */

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
     * Gets the color key set.
     *
     * @return the color key set
     */
    public synchronized Set<String> getColorKeySet() {
        return colorPrefs.keySet();
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
     * Inits the color.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initColor(String key, Color val, String descr) {
        initColor(key, val);
        initDescription(COLOR_PREFIX + key, descr);
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

    /* Float methods */

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
     * Gets the float key set.
     *
     * @return the float key set
     */
    public synchronized Set<String> getFloatKeySet() {
        return floatPrefs.keySet();
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
     * Inits the float.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param unit the unit
     */
    public void initFloat(String key, float val, String descr) {
        initFloat(key, val);
        initDescription(FLOAT_PREFIX + key, descr);
    }

    /**
     * Inits the float plus unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param unit the unit
     */
    public void initFloat(String key, float val, String descr, String unit) {
        initFloat(key, val, descr);
        initUnit(FLOAT_PREFIX + key, unit);
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
     * Sets the float.
     *
     * @param key the key
     * @param val the val
     */
    public void setFloat(String key, float val) {
        setFloat(key, new Float(val));
    }

    /* Integer methods */

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
     * Gets the integer key set.
     *
     * @return the integer key set
     */
    public synchronized Set<String> getIntegerKeySet() {
        return integerPrefs.keySet();
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
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     */
    public void initInteger(String key, int val) {
        if (!integerPrefs.containsKey(key))
            setInteger(key, new Integer(val));
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initInteger(String key, int val, String descr) {
        initInteger(key, val);
        initDescription(INTEGER_PREFIX + key, descr);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param r the restriction
     */
    public void initInteger(String key, int val, String descr, VSPrefsRestriction.VSIntegerPrefRestriction r) {
        initInteger(key, val, descr);
        initRestriction(INTEGER_PREFIX + key, r);
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param r the restriction
     */
    public void initInteger(String key, int val, String descr, VSPrefsRestriction.VSIntegerPrefRestriction r, String unit) {
        initInteger(key, val, descr, r);
        initUnit(INTEGER_PREFIX + key, unit);
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
        initInteger(key, val, descr,
                    new VSPrefsRestriction.VSIntegerPrefRestriction(minValue, maxValue));
    }

    /**
     * Inits the integer plus unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param minValue the min value
     * @param maxValue the max value
     * @param unit the unit
     */
    public void initInteger(String key, int val, String descr, int minValue, int maxValue, String unit) {
        initInteger(key, val, descr, minValue, maxValue);
        initUnit(INTEGER_PREFIX + key, unit);
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
     * Sets the integer.
     *
     * @param key the key
     * @param val the val
     */
    public void setInteger(String key, int val) {
        setInteger(key, new Integer(val));
    }

    /* Integer vector methods */

    /**
     * Gets the integer key set.
     *
     * @return the integer key set
     */
    public synchronized Set<String> getVectorKeySet() {
        return vectorPrefs.keySet();
    }

    /**
     * Gets the integer obj.
     *
     * @param key the key
     *
     * @return the integer obj
     */
    public synchronized Vector<Integer> getVector(String key) {
        Vector<Integer> val = vectorPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such integer config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    /**
     * Inits the integer.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void initVector(String key, Vector<Integer> val) {
        if (!vectorPrefs.containsKey(key))
            setVector(key, val);
    }

    /**
     * Inits the integer vector.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initVector(String key, Vector<Integer> val, String descr) {
        initVector(key, val);
        initDescription(VECTOR_PREFIX + key, descr);
    }

    /**
     * Inits the integer vector plus unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initVector(String key, Vector<Integer> val, String descr, String unit) {
        initVector(key, val, descr);
        initUnit(VECTOR_PREFIX + key, unit);
    }

    /**
     * Sets the integer vector.
     *
     * @param key the key
     * @param val the val
     */
    public synchronized void setVector(String key, Vector<Integer> val) {
        vectorPrefs.put(key, val);
    }

    /* Long methods */

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
     * Gets the long key set.
     *
     * @return the long key set
     */
    public synchronized Set<String> getLongKeySet() {
        return longPrefs.keySet();
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
     * Sets the long if unset.
     *
     * @param key the key
     * @param val the val
     */
    public void initLong(String key, long val) {
        initLong(key, new Long(val));
    }

    /**
     * Inits the long.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initLong(String key, long val, String descr) {
        initLong(key, val);
        initDescription(LONG_PREFIX + key, descr);
    }

    /**
     * Inits the long unit.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     * @param unit the unit
     */
    public void initLong(String key, long val, String descr, String unit) {
        initLong(key, val, descr);
        initUnit(LONG_PREFIX + key, unit);
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
     * Sets the long.
     *
     * @param key the key
     * @param val the val
     */
    public void setLong(String key, long val) {
        setLong(key, new Long(val));
    }

    /* String methods */

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
     * Gets the string key set.
     *
     * @return the string key set
     */
    public synchronized Set<String> getStringKeySet() {
        return stringPrefs.keySet();
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
     * Inits the string.
     *
     * @param key the key
     * @param val the val
     * @param descr the descr
     */
    public void initString(String key, String val, String descr) {
        initString(key, val);
        initDescription(STRING_PREFIX + key, descr);
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
        objectOutputStream.writeObject(vectorPrefs);
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
        vectorPrefs = (HashMap<String,Vector<Integer>>) objectInputStream.readObject();
        longPrefs = (HashMap<String,Long>) objectInputStream.readObject();
        restrictions = new HashMap<String,VSPrefsRestriction>();
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
            copyInto.initInteger(key,
                                 getInteger(key), getDescription(INTEGER_PREFIX + key),
                                 (VSPrefsRestriction.VSIntegerPrefRestriction) getRestriction(INTEGER_PREFIX + key),
                                 getUnit(INTEGER_PREFIX + key));
    }

    /**
     * Copy longs.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyLongs(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initLong(key, getLong(key),
                              getDescription(LONG_PREFIX + key),
                              getUnit(LONG_PREFIX + key));
    }

    /**
     * Copy floats.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyFloats(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initFloat(key, getFloat(key),
                               getDescription(FLOAT_PREFIX + key),
                               getUnit(FLOAT_PREFIX + key));
    }

    /**
     * Copy strings.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyStrings(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initString(key, getString(key),
                                getDescription(STRING_PREFIX + key));
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

    /**
     * Copy colors.
     *
     * @param copyInto the copy into
     * @param keys the keys
     */
    public void copyBooleans(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initBoolean(key, getBoolean(key), getDescription(BOOLEAN_PREFIX + key));
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

        set = getVectorKeySet();
        if (set.size() > 0) {
            descr += VECTOR_PREFIX;
            for (String key : set)
                descr += key + "=" + getVector(key) + "; ";
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

        if (!vectorPrefs.isEmpty())
            return false;

        if (!longPrefs.isEmpty())
            return false;

        if (!stringPrefs.isEmpty())
            return false;

        if (!booleanPrefs.isEmpty())
            return false;

        return true;
    }

    /**
     * Return all full keys
     *
     * @return Allf ull keys
     */
    public ArrayList<String> getAllFullKeys() {
        ArrayList<String> allKeys = new ArrayList<String>();

        Set<String> set = null;

        set = getIntegerKeySet();
        for (String key : set)
            allKeys.add(INTEGER_PREFIX + key);

        set = getVectorKeySet();
        for (String key : set)
            allKeys.add(VECTOR_PREFIX + key);

        set = getLongKeySet();
        for (String key : set)
            allKeys.add(LONG_PREFIX + key);

        set = getFloatKeySet();
        for (String key : set)
            allKeys.add(FLOAT_PREFIX + key);

        set = getBooleanKeySet();
        for (String key : set)
            allKeys.add(BOOLEAN_PREFIX + key);

        set = getStringKeySet();
        for (String key : set)
            allKeys.add(STRING_PREFIX + key);

        Collections.sort(allKeys);

        return allKeys;
    }
}
