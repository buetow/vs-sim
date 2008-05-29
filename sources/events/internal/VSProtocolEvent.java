/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.internal;

import events.*;
import protocols.VSAbstractProtocol;

/**
 * The Class VSProtocolEvent.
 */
public class VSProtocolEvent extends VSAbstractEvent {
    private static final long serialVersionUID = 1L;

    /** The protocol classname. */
    private String protocolClassname;

    /** The event is a client protocol. */
    private boolean isClientProtocol; /* true = client, false = server */

    /** The event is a protocol activation. */
    private boolean isProtocolActivation; /* true = activate, false = deactivate */

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /**
     * Checks if is client protocol.
     *
     * @param isClientProtocol the is client protocol
     */
    public void isClientProtocol(boolean isClientProtocol) {
        this.isClientProtocol = isClientProtocol;
    }

    /**
     * Checks if is client protocol.
     *
     * @return true, if is client protocol
     */
    public boolean isClientProtocol() {
        return isClientProtocol;
    }

    /**
     * Checks if is protocol activation.
     *
     * @param isProtocolActivation the is protocol activation
     */
    public void isProtocolActivation(boolean isProtocolActivation) {
        this.isProtocolActivation = isProtocolActivation;
    }

    /**
     * Checks if is protocol activation.
     *
     * @return true, if is protocol activation
     */
    public boolean isProtocolActivation() {
        return isProtocolActivation;
    }

    /**
     * Sets the protocol classname.
     *
     * @param protocolClassname the new protocol classname
     */
    public void setProtocolClassname(String protocolClassname) {
        this.protocolClassname = protocolClassname;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public boolean onStart() {
        VSAbstractProtocol protocol = process.getProtocolObject(
                                          protocolClassname);

        if (isClientProtocol)
            protocol.isClient(isProtocolActivation);

        else
            protocol.isServer(isProtocolActivation);

        StringBuffer buffer = new StringBuffer();
        buffer.append(VSRegisteredEvents.getShortnameByClassname(
                          protocolClassname));

        buffer.append(" ");
        buffer.append(isClientProtocol
                      ? prefs.getString("lang.client")
                      : prefs.getString("lang.server"));

        buffer.append(" ");
        buffer.append(isProtocolActivation
                      ? prefs.getString("lang.activated")
                      : prefs.getString("lang.deactivated"));

        logg(buffer.toString());

        return true;
    }
}
