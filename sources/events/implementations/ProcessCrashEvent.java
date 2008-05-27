/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.implementations;

import events.VSAbstractEvent;

/**
 * The Class ProcessCrashEvent.
 */
public class ProcessCrashEvent extends VSAbstractEvent {

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public void onStart() {
        if (!process.isCrashed()) {
            process.isCrashed(true);
            logg(prefs.getString("lang.crashed"));
        }
    }
}
