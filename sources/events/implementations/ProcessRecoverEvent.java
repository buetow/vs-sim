/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.implementations;

import events.VSEvent;

/**
 * The Class ProcessRecoverEvent.
 */
public class ProcessRecoverEvent extends VSEvent {
    private static final long serialVersionUID = 1L;

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
