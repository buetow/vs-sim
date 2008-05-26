/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import core.VSMessage;
import protocols.VSAbstractProtocol;

// TODO: Auto-generated Javadoc
/**
 * The Class PingPongProtocol.
 */
public class PingPongProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /** The client counter. */
    private int clientCounter;

    /** The server counter. */
    private int serverCounter;

    /**
     * Instantiates a new ping pong protocol.
     */
    public PingPongProtocol() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    protected void onInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientReset()
     */
    protected void onClientReset() {
        clientCounter = 0;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    protected void onClientStart() {
        VSMessage message = new VSMessage();
        message.setBoolean("fromClient", true);
        message.setInteger("counter", ++clientCounter);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("fromServer"))
            return;

        logg("message: " + recvMessage.getInteger("counter"));

        VSMessage message = new VSMessage();
        message.setBoolean("fromClient", true);
        message.setInteger("counter", ++clientCounter);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    protected void onServerReset() {
        serverCounter = 0;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("fromClient"))
            return;

        logg("message: " + recvMessage.getInteger("counter"));

        VSMessage message = new VSMessage();
        message.setBoolean("fromServer", true);
        message.setInteger("counter", ++serverCounter);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
