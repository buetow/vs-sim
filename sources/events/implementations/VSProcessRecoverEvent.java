/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.implementations;

import events.VSAbstractEvent;

/**
 * The Class VSProcessRecoverEvent.
 */
public class VSProcessRecoverEvent extends VSAbstractEvent {
    private static final long serialVersionUID = 1L;

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
        if (process.isCrashed()) {
            process.isCrashed(false);
            logg(prefs.getString("lang.recovered"));
        }
    }
}