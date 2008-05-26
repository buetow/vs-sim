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

    /** The event is a server protocol schedule. */
    private boolean isServerSchedule; /* true = server, false = client */

    /** The reference to the protocol object to schedule. */
    private VSAbstractProtocol protocol;

    /**
     * Create a ProtocolScheduleEvent object
     *
     * @param protocol the protocol
     * @param isServerSchedule the event is a client protocol schedule if false, else server schedule
     */
    public ProtocolScheduleEvent(VSAbstractProtocol protocol, boolean isServerSchedule) {
        this.protocol = protocol;
        this.isServerSchedule = isServerSchedule;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    protected void onInit() {
        setClassname(getClass().toString());
    }

    /**
     * Checks if is client protocol schedule.
     *
     * @param isServerSchedule the event is a client protocol schedule if false, else server schedule
     */
    public void isServerSchedule(boolean isServerSchedule) {
        this.isServerSchedule = isServerSchedule;
    }

    /**
     * Checks if is client protocol.
     *
     * @return false, if is client protocol schedule, else server protocol schedule
     */
    public boolean isServerSchedule() {
        return isServerSchedule;
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
        if (isServerSchedule)
            protocol.onServerScheduleStart();
        else
            protocol.onClientScheduleStart();
    }
}
