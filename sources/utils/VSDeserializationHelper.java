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

import java.util.HashMap;

/**
 * The class VSDeserializationHelper, this static class helps do deserialize
 * a saved simulator!
 *
 * @author Paul C. Buetow
 */
public final class VSDeserializationHelper {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** True if debugg mode of deserialization */
    public static final boolean DEBUG = true;

    /** For temp object storage */
    private static HashMap<String,Object> objects;

    /**
     * Initializes the helper.
     */
    public static void init() {
        objects = new HashMap<String,Object>();
    }

    /**
     * Destroys the helper.
     */
    public static void destroy() {
        objects = null;
    }

    /**
     * Sets an object.
     *
     * @param key The object key
     * @param object The object itself
     */
    public static void setObject(String key, Object object) {
        objects.put(key, object);
    }

    /**
     * Sets an object.
     *
     * @param num The object number
     * @param key The object key
     * @param object The object itself
     */
    public static void setObject(int num, String key, Object object) {
        objects.put(key + ":" + num, object);
    }

    /**
     * Gets an object.
     *
     * @param num The object number
     * @param key The object key
     *
     * @return The object itself
     */
    public static Object getObject(int num, String key) {
        Object object = objects.get(key + ":" + num);

        if (object == null) {
            System.err.println("No such deserialization helper key "
                               + key + ":" + num);
            System.exit(1);
        }

        return object;
    }

    /**
     * Gets an object.
     *
     * @param key The object key
     *
     * @return The object itself
     */
    public static Object getObject(String key) {
        Object object = objects.get(key);

        if (object == null) {
            System.err.println("No such deserialization helper key " + key);
            System.exit(1);
        }

        return object;
    }
}
