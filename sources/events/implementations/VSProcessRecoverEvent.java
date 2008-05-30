/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.implementations;

import events.VSAbstractEvent;

/**
 * The class VSProcessRecoverEvent. This event makes a process to recover if
 * it is crashed.
 *
 * @author Paul C. Buetow
 */
public class VSProcessRecoverEvent extends VSAbstractEvent {
    /** The serial version uid */
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
    public boolean onStart() {
        if (process.isCrashed()) {
            process.isCrashed(false);
            logg(prefs.getString("lang.recovered"));
        }

        return true;
    }
}
