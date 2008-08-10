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

package events;

import java.io.*;

import core.VSAbstractProcess;
import core.VSInternalProcess;
import exceptions.*;
import prefs.*;
import serialize.*;
import utils.*;

/**
 * The class VSAbstractEvent. This abstract class defines the basic framework
 * of each event. an event is used to fullfill a specific task. An event object
 * will get stored in a VSTask object.
 *
 * @author Paul C. Buetow
 */
abstract public class VSAbstractEvent extends VSSerializablePrefs {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The prefs. */
    public VSPrefs prefs;

    /** The process. */
    public VSAbstractProcess process;

    /** The event shortname. */
    private String eventShortname;

    /** The event classname. */
    private String eventClassname;

    /**
     * Creates a copy of the event and using a new process.
     *
     * @param theProcess The new process
     * @return The copy
     */
    final public VSAbstractEvent getCopy(VSInternalProcess theProcess)
    throws VSEventNotCopyableException {

        if (theProcess == null)
            theProcess = (VSInternalProcess) process;

        if (!(this instanceof VSCopyableEvent))
            throw new VSEventNotCopyableException(
                eventShortname + " (" + eventClassname + ")");

        VSAbstractEvent copy =
            VSRegisteredEvents.createEventInstanceByClassname(
                eventClassname, theProcess);

        ((VSCopyableEvent) this).initCopy(copy);
        copy.setShortname(eventShortname);

        return copy;
    }

    /**
     * Creates a copy of the event.
     *
     * @return The copy
     */
    final public VSAbstractEvent getCopy() throws VSEventNotCopyableException {
        return getCopy(null);
    }

    /**
     * Inits the event.
     *
     * @param process the process
     */
    public void init(VSInternalProcess process) {
        if (this.process == null) {
            this.process = process;
            this.prefs = process.getPrefs();
            init();
        }
    }

    /**
     * Inits the event without setting the processes and prefs variables
     * of the object.
     */
    public void init() {
        onInit();
    }

    /**
     * Sets the classname.
     *
     * @param eventClassname the new classname
     */
    public final void setClassname(String eventClassname) {
        if (eventClassname.startsWith("class "))
            eventClassname = eventClassname.substring(6);

        this.eventClassname = eventClassname;
    }

    /**
     * Gets the classname.
     *
     * @return the classname
     */
    public String getClassname() {
        return eventClassname;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return VSRegisteredEvents.getNameByClassname(eventClassname);
    }

    /**
     * Sets the shortname.
     *
     * @param eventShortname the new shortname
     */
    public void setShortname(String eventShortname) {
        this.eventShortname = eventShortname;
    }

    /**
     * Gets the shortname.
     *
     * @return the shortname
     */
    public String getShortname() {
        if (eventShortname == null)
            return VSRegisteredEvents.getShortnameByClassname(eventClassname);

        return eventShortname;
    }

    /**
     * Gets the process.
     *
     * @return the process
     */
    public VSAbstractProcess getProcess() {
        return process;
    }

    /**
     * Logg a specific message.
     *
     * @param message the logging message
     */
    public void logg(String message) {
        process.logg(/*toString() + "; " + */message);
    }

    /**
     * Checks if the event equals to another event..
     *
     * @param event the event to compare against.
     *
     * @return true, if the events are the same (have the same event id)
     */
    public boolean equals(VSAbstractEvent event) {
        return super.getID() == event.getID();
    }

    /**
     * Every event has its own initialize method.
     */
    abstract public void onInit();

    /**
     * Every event can get started. This method get's executed if the event
     * takes place.
     */
    abstract public void onStart();

    /* (non-Javadoc)
     * @see serialize.VSSerializable#serialize(serialize.VSSerialize,
     *	java.io.ObjectOutputStream)
     */
    public synchronized void serialize(VSSerialize serialize,
                                       ObjectOutputStream objectOutputStream)
    throws IOException {
        super.serialize(serialize, objectOutputStream);

        if (VSSerialize.DEBUG)
            System.out.println("Serializing: VSAbstractEvent; id="+getID());

        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));

        objectOutputStream.writeObject(new Integer(super.getID()));
        objectOutputStream.writeObject(eventShortname);
        objectOutputStream.writeObject(eventClassname);

        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));
    }

    /* (non-Javadoc)
     * @see serialize.VSSerializable#deserialize(serialize.VSSerialize,
     *	java.io.ObjectInputStream)
     */
    @SuppressWarnings("unchecked")
    public synchronized void deserialize(VSSerialize serialize,
                                         ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException {
        super.deserialize(serialize, objectInputStream);

        if (VSSerialize.DEBUG)
            System.out.print("Deserializing: VSAbstractEvent ");

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();

        int id = ((Integer) objectInputStream.readObject()).intValue();
        this.eventShortname = (String) objectInputStream.readObject();
        this.eventClassname = (String) objectInputStream.readObject();

        if (VSSerialize.DEBUG)
            System.out.println(eventClassname);

        serialize.setObject(id, "event", this);

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();
    }
}
