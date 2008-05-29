/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.internal;

import events.*;
import protocols.VSAbstractProtocol;

/**
 * The Class VSProtocolScheduleEvent. This event is used if a protocol (which
 * is a subclass of VSAbstractProtocol) reschedules itself to run again on a
 * specific time.
 *
 * @author Paul C. Buetow
 */
public class VSProtocolScheduleEvent extends VSAbstractEvent {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The event is a server protocol schedule. */
    private boolean isServerSchedule; /* true = server, false = client */

    /** The reference to the protocol object to schedule. */
    private VSAbstractProtocol protocol;

    /**
     * Create a VSProtocolScheduleEvent object
     *
     * @param protocol the protocol
     * @param isServerSchedule the event is a client protocol schedule if
     *	false, else server schedule
     */
    public VSProtocolScheduleEvent(VSAbstractProtocol protocol,
                                   boolean isServerSchedule) {
        this.protocol = protocol;
        this.isServerSchedule = isServerSchedule;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /**
     * Sets if it is client protocol schedule.
     *
     * @param isServerSchedule false, if the event is a client protocol
     * schedule. true, if server.
     */
    public void isServerSchedule(boolean isServerSchedule) {
        this.isServerSchedule = isServerSchedule;
    }

    /**
     * Sets if it is client protocol schedule.
     *
     * @return false, if the event is a client protocol schedule. true, if
     *	server.
     */
    public boolean isServerSchedule() {
        return isServerSchedule;
    }

    /**
     * Sets the protocol.
     *
     * @param protocol the protocol
     */
    public void setProtocol(VSAbstractProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public VSAbstractProtocol getProtocol() {
        return protocol;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public boolean onStart() {
        if (isServerSchedule)
            protocol.onServerScheduleStart();
        else
            protocol.onClientScheduleStart();

        return true;
    }
}
