/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;


// TODO: Auto-generated Javadoc
/**
 * The Class VSTools.
 */
public final class VSTools {

    /**
     * Gets the time string.
     *
     * @param time the time
     *
     * @return the time string
     */
    public static String getTimeString(long time) {
        String ret = ""+time;

        while (ret.length() < 6)
            ret = "0" + ret;

        return ret + "ms";
    }

    /**
     * Gets the string time.
     *
     * @param string the string
     *
     * @return the string time
     */
    public static long getStringTime(String string) {
        try {
            /* Ignore the "ms" postfix */
            Long longValue = Long.valueOf(string.substring(0, string.length()-2));
            return longValue.longValue();
        } catch (NumberFormatException e) {
        }

        return 0;
    }
}
