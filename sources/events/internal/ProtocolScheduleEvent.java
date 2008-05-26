/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.internal;

import events.*;
import protocols.VSAbstractProtocol;

/**
 * The Class ProtocolScheduleEvent.
 */
public class ProtocolScheduleEvent extends VSAbstractEvent {
    private static final long serialVersionUID = 1L;

    /** The event is a client protocol schedule. */
    private boolean isClientSchedule; /* true = client, false = server */

    /** The reference to the protocol object to schedule. */
    private VSAbstractProtocol protocol;

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    protected void onInit() {
        setClassname(getClass().toString());
    }

    /**
     * Checks if is client protocol schedule.
     *
     * @param isClientSchedule the event is a client protocol schedule if true, else server schedule
     */
    public void isClientSchedule(boolean isClientSchedule) {
        this.isClientSchedule = isClientSchedule;
    }

    /**
     * Checks if is client protocol.
     *
     * @return true, if is client protocol schedule, else server protocol schedule
     */
    public boolean isClientSchedule() {
        return isClientSchedule;
    }

    /**
     * Sets the protocol
     *
     * @param protocol the protocol
     */
    public void setProtocol(VSAbstractProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the protocol
     *
     * @return the protocol
     */
    public VSAbstractProtocol getProtocol() {
        return protocol;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public void onStart() {
        if (isClientSchedule)
            protocol.onClientScheduleStart();
        else
            protocol.onServerScheduleStart();
    }
}
