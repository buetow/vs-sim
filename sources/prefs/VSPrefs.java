package prefs;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

public abstract class VSPrefs implements Serializable {
    public static final String BOOLEAN_PREFIX = "Boolean: ";
    public static final String COLOR_PREFIX = "Color: ";
    public static final String FLOAT_PREFIX = "Float: ";
    public static final String INTEGER_PREFIX = "Integer: ";
    public static final String LONG_PREFIX = "Long: ";
    public static final String STRING_PREFIX = "String: ";
    private HashMap<String,Color> colorVSPrefs;
    private HashMap<String,Float> floatVSPrefs;
    private HashMap<String,Integer> integerVSPrefs;
    private HashMap<String,Long> longVSPrefs;
    private HashMap<String,SettingRestriction> settingRestrictionPrefs;
    private HashMap<String,String> descriptionPrefs;
    private HashMap<String,String> stringVSPrefs;
    private HashMap<String,Boolean> booleanVSPrefs;
    private HashMap<String,Object> objectVSPrefs;
    private HashMap<String,String> units;
    private static final long serialVersionUID = 4L;
    protected final static String PREFERENCES_FILENAME = "vs.dat";
    private static long idCounter;
    protected long id;

    public class SettingRestriction implements Serializable {
    }

    public class IntegerSettingRestriction extends SettingRestriction {
        private int minValue;
        private int maxValue;

        public IntegerSettingRestriction(int minValue, int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public int getMinValue() {
            return minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }
    }

    public class StringSettingRestriction extends SettingRestriction {
        Vector<String> possibleSelections;

        public StringSettingRestriction(String [] possibleSelections) {
            this.possibleSelections = new Vector<String>();

            for (String elem : possibleSelections)
                this.possibleSelections.add(elem);
        }

        public Vector<String> getPossibleSelections() {
            return possibleSelections;
        }
    }

    public VSPrefs() {
        colorVSPrefs = new HashMap<String,Color>();
        descriptionPrefs = new HashMap<String,String>();
        floatVSPrefs = new HashMap<String,Float>();
        integerVSPrefs = new HashMap<String,Integer>();
        longVSPrefs = new HashMap<String,Long>();
        settingRestrictionPrefs = new HashMap<String,SettingRestriction>();
        stringVSPrefs = new HashMap<String,String>();
        booleanVSPrefs = new HashMap<String,Boolean>();
        objectVSPrefs = new HashMap<String,Object>();
        units = new HashMap<String,String>();
        id = ++idCounter;
    }

    protected synchronized void clear() {
        colorVSPrefs.clear();
        floatVSPrefs.clear();
        integerVSPrefs.clear();
        longVSPrefs.clear();
        stringVSPrefs.clear();
        booleanVSPrefs.clear();
        objectVSPrefs.clear();
        descriptionPrefs.clear();
        settingRestrictionPrefs.clear();
    }

    public synchronized Object getObject(String key) {
        Object val = objectVSPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such object config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    public synchronized boolean objectExists(String key) {
        return null != objectVSPrefs.get(key);
    }

    public synchronized String getString(String key) {
        String val = stringVSPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such string config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    public synchronized Integer getIntegerObj(String key) {
        Integer val = integerVSPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such integer config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    public int getInteger(String key) {
        return getIntegerObj(key).intValue();
    }

    public synchronized Float getFloatObj(String key) {
        Float val = floatVSPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such float config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    public float getFloat(String key) {
        return getFloatObj(key).floatValue();
    }

    public synchronized Long getLongObj(String key) {
        Long val = longVSPrefs.get(key);

        if (val == null) {
            System.err.println("Fatal: No such long config value \""
                               + key + "\"");
            System.exit(1);
        }

        return val;
    }

    public long getLong(String key) {
        return getLongObj(key).longValue();
    }

    public synchronized Boolean getBooleanObj(String key) {
        Boolean val = booleanVSPrefs.get(key);

        if (val == null)
            return new Boolean(false);

        return val;
    }

    public boolean getBoolean(String key) {
        return getBooleanObj(key).booleanValue();
    }

    public synchronized Color getColor(String key) {
        Color color = colorVSPrefs.get(key);

        if (color == null) {
            System.err.println("Fatal: No such color config value \""
                               + key + "\"");
            System.exit(1);
        }

        return color;
    }

    public synchronized String getDescription(String key) {
        return descriptionPrefs.get(key);
    }

    public synchronized String getUnit(String key) {
        return units.get(key);
    }

    public synchronized SettingRestriction getRestriction(String key) {
        return settingRestrictionPrefs.get(key);
    }

    public synchronized Set<String> getStringKeySet() {
        return stringVSPrefs.keySet();
    }

    public synchronized Set<String> getIntegerKeySet() {
        return integerVSPrefs.keySet();
    }

    public synchronized Set<String> getFloatKeySet() {
        return floatVSPrefs.keySet();
    }

    public synchronized Set<String> getLongKeySet() {
        return longVSPrefs.keySet();
    }

    public synchronized Set<String> getBooleanKeySet() {
        return booleanVSPrefs.keySet();
    }

    public synchronized Set<String> getColorKeySet() {
        return colorVSPrefs.keySet();
    }

    public synchronized void setObject(String key, Object val) {
        objectVSPrefs.put(key, val);
    }

    public synchronized void setString(String key, String val) {
        stringVSPrefs.put(key, val);
    }

    public synchronized void setInteger(String key, Integer val) {
        integerVSPrefs.put(key, val);
    }

    public synchronized void setColor(String key, Color color) {
        colorVSPrefs.put(key, color);
    }

    public synchronized void setInt(String key, int val) {
        integerVSPrefs.put(key, new Integer(val));
    }

    public synchronized void setFloat(String key, float val) {
        floatVSPrefs.put(key, new Float(val));
    }

    public synchronized void setFloat(String key, Float val) {
        floatVSPrefs.put(key, val);
    }

    public synchronized void setLong(String key, long val) {
        longVSPrefs.put(key, new Long(val));
    }

    public synchronized void setLong(String key, Long val) {
        longVSPrefs.put(key, val);
    }

    public synchronized void setBoolean(String key, boolean val) {
        booleanVSPrefs.put(key, new Boolean(val));
    }

    public synchronized void setBoolean(String key, Boolean val) {
        booleanVSPrefs.put(key, val);
    }

    public synchronized void initString(String key, String val) {
        if (!stringVSPrefs.containsKey(key))
            stringVSPrefs.put(key, val);
    }

    public synchronized void initInteger(String key, Integer val) {
        if (!integerVSPrefs.containsKey(key))
            integerVSPrefs.put(key, val);
    }

    public void initInteger(String key, int val) {
        initInteger(key, new Integer(val));
    }

    public synchronized void initLong(String key, Long val) {
        if (!longVSPrefs.containsKey(key))
            longVSPrefs.put(key, val);
    }

    public void initLong(String key, int val) {
        initLong(key, new Long(val));
    }

    public synchronized void initFloat(String key, Float val) {
        if (!floatVSPrefs.containsKey(key))
            floatVSPrefs.put(key, val);
    }

    public void initFloat(String key, float val) {
        initFloat(key, new Float(val));
    }

    public synchronized void setLongIfUnset(String key, Long val) {
        if (!longVSPrefs.containsKey(key))
            longVSPrefs.put(key, val);
    }

    public void setLongIfUnset(String key, long val) {
        setLongIfUnset(key, new Long(val));
    }

    public synchronized void initBoolean(String key, Boolean val) {
        if (!booleanVSPrefs.containsKey(key))
            booleanVSPrefs.put(key, val);
    }

    public void initBoolean(String key, boolean val) {
        initBoolean(key, new Boolean(val));
    }

    public synchronized void initColor(String key, Color color) {
        if (!colorVSPrefs.containsKey(key))
            colorVSPrefs.put(key, color);
    }

    public synchronized void setDescriptionIfUnset(String key, String descr) {
        if (descr == null || descriptionPrefs.containsKey(key))
            return;
        descriptionPrefs.put(key, descr);
    }

    public synchronized void setRestriction(String key, SettingRestriction settingRestriction) {
        settingRestrictionPrefs.put(key, settingRestriction);
    }

    public synchronized void setUnit(String key, String unit) {
        units.put(key, unit);
    }

    public void initString(String key, String val, String descr) {
        initString(key, val);
        setDescriptionIfUnset(STRING_PREFIX + key, descr);
    }

    public void setLongIfUnset(String key, long val, String descr) {
        setLongIfUnset(key, val);
        setDescriptionIfUnset(LONG_PREFIX + key, descr);
    }

    public void initBoolean(String key, boolean val, String descr) {
        initBoolean(key, val);
        setDescriptionIfUnset(BOOLEAN_PREFIX + key, descr);
    }

    public void initBooleanUnit(String key, boolean val, String descr, String unit) {
        initBoolean(key, val, descr);
        setUnit(BOOLEAN_PREFIX + key, unit);
    }

    public void initInteger(String key, Integer val, String descr, IntegerSettingRestriction r) {
        initInteger(key, val);
        setDescriptionIfUnset(INTEGER_PREFIX + key, descr);
        setRestriction(INTEGER_PREFIX + key, r);
    }

    public void initInteger(String key, Integer val, String descr, int minValue, int maxValue) {
        initInteger(key, val, descr, new IntegerSettingRestriction(minValue, maxValue));
    }

    public void initInteger(String key, int val, String descr, IntegerSettingRestriction r) {
        initInteger(key, new Integer(val), descr, r);
    }

    public void initInteger(String key, int val, String descr, int minValue, int maxValue) {
        initInteger(key, new Integer(val), descr, minValue, maxValue);
    }

    public void initIntegerUnit(String key, int val, String descr, int minValue, int maxValue, String unit) {
        initInteger(key, new Integer(val), descr, minValue, maxValue);
        setUnit(INTEGER_PREFIX + key, unit);
    }

    public void initLong(String key, Long val, String descr) {
        initLong(key, val);
        setDescriptionIfUnset(LONG_PREFIX + key, descr);
    }

    public void initLong(String key, int val, String descr) {
        initLong(key, new Long(val), descr);
    }

    public void initLongUnit(String key, int val, String descr, String unit) {
        initLong(key, new Long(val), descr);
        setUnit(LONG_PREFIX + key, unit);
    }

    public void initFloat(String key, Float val, String descr) {
        initFloat(key, val);
        setDescriptionIfUnset(FLOAT_PREFIX + key, descr);
    }

    public void initFloat(String key, float val, String descr) {
        initFloat(key, new Float(val), descr);
    }

    public void initFloatUnit(String key, float val, String descr, String unit) {
        initFloat(key, new Float(val), descr);
        setUnit(FLOAT_PREFIX + key, unit);
    }


    public void initColor(String key, Color val, String descr) {
        initColor(key, val);
        setDescriptionIfUnset(COLOR_PREFIX + key, descr);
    }

    public void fillDefaultStrings() {}
    public void fillDefaultIntegers() {}
    public void fillDefaultFloats() {}
    public void fillDefaultColors() {}
    public void fillDefaultBooleans() {}
    public void fillWithDefaults() {}

    public synchronized void writeObject(ObjectOutputStream objectOutputStream)
    throws IOException {
        objectOutputStream.writeObject(booleanVSPrefs);
        objectOutputStream.writeObject(colorVSPrefs);
        objectOutputStream.writeObject(floatVSPrefs);
        objectOutputStream.writeObject(integerVSPrefs);
        objectOutputStream.writeObject(longVSPrefs);
        objectOutputStream.writeObject(stringVSPrefs);
        objectOutputStream.writeObject(units);
    }

    public synchronized void readObject(ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException {
        booleanVSPrefs = (HashMap<String,Boolean>) objectInputStream.readObject();
        colorVSPrefs = (HashMap<String,Color>) objectInputStream.readObject();
        descriptionPrefs = new HashMap<String,String>();
        floatVSPrefs = (HashMap<String,Float>) objectInputStream.readObject();
        integerVSPrefs = (HashMap<String,Integer>) objectInputStream.readObject();
        longVSPrefs = (HashMap<String,Long>) objectInputStream.readObject();
        settingRestrictionPrefs = new HashMap<String,SettingRestriction>();
        stringVSPrefs = (HashMap<String,String>) objectInputStream.readObject();
        units = (HashMap<String,String>) objectInputStream.readObject();
    }

    public void saveFile(String filename) {
        //System.out.println("SAVE FILE " + filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() { };

    public static VSPrefs openFile(String filename) {
        VSPrefs prefs = null;
        //System.out.println("OPEN FILE " + filename);

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

    public void copyIntegers(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initInteger(key, getInteger(key), getDescription(INTEGER_PREFIX + key),
                                 (IntegerSettingRestriction) getRestriction(INTEGER_PREFIX + key));
    }

    public void copyLongs(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initLong(key, getLong(key), getDescription(LONG_PREFIX + key));
    }

    public void copyFloats(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initFloat(key, getFloat(key), getDescription(FLOAT_PREFIX + key));
    }

    public void copyStrings(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initString(key, getString(key), getDescription(STRING_PREFIX + key));
    }

    public void copyColors(VSPrefs copyInto, String[] keys) {
        for (String key : keys)
            copyInto.initColor(key, getColor(key), getDescription(COLOR_PREFIX + key));
    }

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

    public long getID() {
        return id;
    }
}
