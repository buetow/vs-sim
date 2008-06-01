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

package serialize;

import java.io.*;
import java.util.HashMap;

import prefs.*;
import simulator.*;

/**
 * The class VSSerialize, this static class helps do deserialize
 * a saved simulator!
 *
 * @author Paul C. Buetow
 */
public final class VSSerialize {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** True if debugg mode of deserialization */
    public static final boolean DEBUG = true;

    /** The standard filename to save simulators to */
    public static final String STANDARD_FILENAME = "simulator.dat";

    /** For temp object storage */
    private static HashMap<String,Object> objects;

    /** Holds the current VSSerialize object */
    private static VSSerialize serialize;

    /**
     * Creates the VSSerialize object.
     */
    public VSSerialize() {
        init();
    }

    /**
     * Initializes the helper.
     */
    private void init() {
        this.serialize = this;
        objects = new HashMap<String,Object>();
    }

    /**
     * Gets the current VSSerialize object.
     *
     * @return The current serialize object
     */
    public static VSSerialize getSerialize() {
        return serialize;
    }

    /**
     * Sets an object.
     *
     * @param key The object key
     * @param object The object itself
     */
    public void setObject(String key, Object object) {
        objects.put(key, object);
    }

    /**
     * Sets an object.
     *
     * @param num The object number
     * @param key The object key
     * @param object The object itself
     */
    public void setObject(int num, String key, Object object) {
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
    public Object getObject(int num, String key) {
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
    public Object getObject(String key) {
        Object object = objects.get(key);

        if (object == null) {
            System.err.println("No such deserialization helper key " + key);
            System.exit(1);
        }

        return object;
    }

    /**
     * Saves the given simulator to the given filename.
     *
     * @param filename The filename
     * @param simulator The simulator
     */
    public void saveSimulator(String filename, VSSimulator simulator) {
        try {
            FileOutputStream fileOutputStream =
                new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(fileOutputStream);

            VSPrefs prefs = simulator.getPrefs();
            prefs.serialize(this, objectOutputStream);
            simulator.serialize(this, objectOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a simulator from the given filename.
     *
     * @param filename The filename.
     * @param simulatorFrame The simulator frame
     *
     * @return The simulator object, and null if no success
     */
    public VSSimulator openSimulator(String filename,
                                     VSSimulatorFrame simulatorFrame) {
        VSSimulator simulator = null;

        try {
            FileInputStream fileInputStream =
                new FileInputStream(filename);
            ObjectInputStream objectInputStream =
                new ObjectInputStream(fileInputStream);

            VSPrefs prefs = new VSPrefs();
            prefs.deserialize(this, objectInputStream);
            this.setObject("prefs", prefs);

            simulator = new VSSimulator(prefs, simulatorFrame);
            simulator.deserialize(this, objectInputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return simulator;
    }
}
