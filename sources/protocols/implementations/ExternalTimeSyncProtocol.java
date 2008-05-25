/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import core.VSMessage;
import protocols.VSProtocol;

// TODO: Auto-generated Javadoc
/**
 * The Class ExternalTimeSyncProtocol.
 */
public class ExternalTimeSyncProtocol extends VSProtocol {
    private static final long serialVersionUID = 1L;

    /** The request time. */
    private long requestTime;

    /** The waiting for response. */
    private boolean waitingForResponse;

    /**
     * Instantiates a new external time sync protocol.
     */
    public ExternalTimeSyncProtocol() {
        setClassname(getClass().toString());
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
        requestTime = process.getTime();
        waitingForResponse = true;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("isClientRequest", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("isServerResponse"))
            return;

        if (waitingForResponse)
            waitingForResponse = false;
        else
            return;

        long recvTime = process.getTime();
        long roundTripTime = recvTime - requestTime;
        long serverTime = recvMessage.getLong("time");
        long newTime = serverTime + (long) (roundTripTime / 2);

        logg("Server Zeit: " + serverTime + "; RTT: " + roundTripTime + "; Alte Zeit: " + recvTime + "; Neue Zeit: " + newTime + "; Offset: " + (newTime - recvTime));
        process.setTime(newTime);
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerReset()
     */
    protected void onServerReset() {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("isClientRequest"))
            return;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage(getClassname());
        message.setLong("time", process.getTime());
        message.setBoolean("isServerResponse", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#toString()
     */
    public String toString() {
        return super.toString(); //+ "; " + prefs.getString("lang.requesttime") + ": " + requestTime;
    }
}
