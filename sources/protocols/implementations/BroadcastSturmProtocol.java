/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import java.util.ArrayList;

import core.VSMessage;
import protocols.VSProtocol;

// TODO: Auto-generated Javadoc
/**
 * The Class BroadcastSturmProtocol.
 */
public class BroadcastSturmProtocol extends VSProtocol {

    /** The sent messages. */
    private ArrayList<VSMessage> sentMessages;

    /** The broadcast count. */
    private static int broadcastCount;

    /**
     * Instantiates a new broadcast sturm protocol.
     */
    public BroadcastSturmProtocol() {
        setClassname(getClass().toString());
        sentMessages = new ArrayList<VSMessage>();
    }

    /* (non-Javadoc)
     * @see events.VSEvent#onInit()
     */
    protected void onInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientReset()
     */
    protected void onClientReset() {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientStart()
     */
    protected void onClientStart() {
        VSMessage message = new VSMessage(getClassname());
        message.setInteger("Broadcast", broadcastCount++);
        sentMessages.add(message);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerReset()
     */
    protected void onServerReset() {
        sentMessages.clear();
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        if (!sentMessages.contains(recvMessage)) {
            VSMessage message = new VSMessage(getClassname());
            message.setInteger("Broadcast", recvMessage.getInteger("Broadcast"));

            sentMessages.add(message);
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
