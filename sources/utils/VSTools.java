/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.util.Vector;

/**
 * The Class VSTools.
 */
public final class VSTools {
    private static final long serialVersionUID = 1L;

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

    /**
     * Gets the integer vector represented by a string comma separated.
     *
     * @param string the string
     *
     * @return the parsed vector
     */
    public static Vector<Integer> parseIntegerVector(String string)
    throws exceptions.VSParseIntegerVectorException {
        Vector<Integer> vec = new Vector<Integer>();

        int index = string.indexOf('[');
        if (index == -1)
            throw new exceptions.VSParseIntegerVectorException();

        string = string.substring(index+1);

        index = string.indexOf(']');
        if (index == -1)
            throw new exceptions.VSParseIntegerVectorException();

        string = string.substring(0, index);

        try {
            while ( (index = string.indexOf(',')) != -1 ) {
                String substring = string.substring(0, index);

                /* Remove leading whitespaces */
                while (substring.charAt(0) == ' ')
                    substring = substring.substring(1);

                vec.add(Integer.parseInt(substring));
                string = string.substring(index+1);
            }
            /* Remove leading whitespaces */
            while (string.charAt(0) == ' ')
                string = string.substring(1);
            vec.add(Integer.parseInt(string));

        } catch (StringIndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }

        return vec;
    }
}
