/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.internal;

import events.*;
import protocols.VSAbstractProtocol;

/**
 * The Class VSProtocolEvent. This event is used if a protocol (server or
 * client part) of a process gets enabled or disabled. An object of this class
 * can be for 4 different purporses! Activation of the client protocol,
 * deactivation of the client protocol, activation of the server protocol,
 * deactivation of the server protocol.
 *
 * @author Paul C. Buetow
 */
public class VSProtocolEvent extends VSAbstractEvent {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The protocol classname. */
    private String protocolClassname;

    /** The event is a client protocol if true. Else it is a server protocol */
    private boolean isClientProtocol;

    /** The event is a protocol activation if true. Else it is a deactivation */
    private boolean isProtocolActivation;

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /**
     * Sets if it is a client protocol activation/deactivation.
     *
     * @param isClientProtocol the event is client protocol if true. the event
     * is a server protocol if false.
     */
    public void isClientProtocol(boolean isClientProtocol) {
        this.isClientProtocol = isClientProtocol;
    }

    /**
     * Checks if it is a client protocol activation/deactivation.
     *
     * @return the event is client protocol if true. the event
     * is a server protocol if false.
     */
    public boolean isClientProtocol() {
        return isClientProtocol;
    }

    /**
     * Sets if it is protocol activation.
     *
     * @param isProtocolActivation true, if it is a protocol activation. false,
     *	if it is a protocol deactivation.
     */
    public void isProtocolActivation(boolean isProtocolActivation) {
        this.isProtocolActivation = isProtocolActivation;
    }

    /**
     * Checks if it is protocol activation.
     *
     * @return true, if it is a protocol activation. false, if it is a protocol
     *	deactivation.
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
        VSAbstractProtocol protocol =
            process.getProtocolObject(protocolClassname);

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
