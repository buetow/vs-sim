/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events;

import core.VSProcess;
import prefs.VSPrefs;

/**
 * The Class VSEvent.
 */
abstract public class VSEvent extends VSPrefs {
    private static final long serialVersionUID = 1L;

    /** The prefs. */
    protected VSPrefs prefs;

    /** The process. */
    protected VSProcess process;

    /** The event shortname. */
    private String eventShortname;

    /** The event classname. */
    private String eventClassname;

    /**
     * Inits the.
     *
     * @param process the process
     */
    public void init(VSProcess process) {
        this.process = process;
        this.prefs = process.getPrefs();

        onInit();
    }

    /**
     * Sets the classname.
     *
     * @param eventClassname the new classname
     */
    protected final void setClassname(String eventClassname) {
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
        return VSRegisteredEvents.getName(eventClassname);
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
            return VSRegisteredEvents.getShortname(eventClassname);

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
    public boolean equals(VSEvent event) {
        return super.getID() == event.getID();
    }

    /**
     * On init.
     */
    abstract protected void onInit();

    /**
     * On start.
     */
    abstract public void onStart();
}
