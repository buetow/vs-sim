/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events;

import core.VSProcess;
import prefs.VSPrefs;

/**
 * The Class VSAbstractEvent.
 */
abstract public class VSAbstractEvent extends VSPrefs {
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
     * Inits the event.
     *
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
     * Logg.
     *
     * @param message the message
     */
    public void logg(String message) {
        process.logg(/*toString() + "; " + */message);
    }

    /**
     * Equals.
     *
     * @param event the event
     *
     * @return true, if successful
     */
    public boolean equals(VSAbstractEvent event) {
        return super.getID() == event.getID();
    }

    /**
     * On init.
     */
    abstract public void onInit();

    /**
     * On start.
     */
    abstract public void onStart();
}
