/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 *
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package utils;

import java.util.Vector;

/**
 * The class VSTools. This class contains only static methods. Those methods
 * are for general usage and don't fit into other classes.
 *
 * @author Paul C. Buetow
 */
public final class VSTools {
    /** The serial version uid */
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
            Long longValue = Long.valueOf(
                                 string.substring(0, string.length()-2));
            return longValue.longValue();
        } catch (NumberFormatException e) {
        }

        return 0;
    }

    /**
     * Gets the integer vector represented by a comma separated string.
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
