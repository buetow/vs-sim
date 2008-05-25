/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.implementations;

import events.VSEvent;

/**
 * The Class ProcessCrashEvent.
 */
public class ProcessCrashEvent extends VSEvent {

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
        if (!process.isCrashed()) {
            process.isCrashed(true);
            logg(prefs.getString("lang.crashed"));
        }
    }
}
