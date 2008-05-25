/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.implementations;

import events.VSEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessRecoverEvent.
 */
public class ProcessRecoverEvent extends VSEvent {

    /* (non-Javadoc)
     * @see events.VSEvent#onInit()
     */
    protected void onInit() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSEvent#onStart()
     */
    public void onStart() {
        if (process.isCrashed()) {
            process.isCrashed(false);
            logg(prefs.getString("lang.recovered"));
        }
    }
}
