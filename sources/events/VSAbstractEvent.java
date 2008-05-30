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

import core.VSProcess;
import prefs.VSPrefs;

/**
 * The class VSAbstractEvent. This abstract class defines the basic framework
 * of each event. an event is used to fullfill a specific task. An event object
 * will get stored in a VSTask object.
 *
 * @author Paul C. Buetow
 */
abstract public class VSAbstractEvent extends VSPrefs {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The prefs. */
    public VSPrefs prefs;

    /** The process. */
    public VSProcess process;

    /** The event shortname. */
    private String eventShortname;

    /** The event classname. */
    private String eventClassname;

    /**
     * Inits the event.
     *
     * @param process the process
     */
    public void init(VSProcess process) {
        this.process = process;
        this.prefs = process.getPrefs();

        init();
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
    public VSProcess getProcess() {
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
     *
     * @return false, if a message has been delivered but is not relevant and
     *	can get removed from the simulator canvas paint area. true otherwise.
     */
    abstract public boolean onStart();
}
