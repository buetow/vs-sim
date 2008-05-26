/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import core.VSMessage;
import protocols.VSAbstractProtocol;

// TODO: Auto-generated Javadoc
/**
 * The Class InternalTimeSyncProtocol.
 */
public class InternalTimeSyncProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /** The waiting for response. */
    private boolean waitingForResponse;

    /**
     * Instantiates a new internal time sync protocol.
     */
    public InternalTimeSyncProtocol() {
        setClassname(getClass().toString());

        /* Those prefs are editable through the VSAbstractProtocol VSAbstractEditor GUI. t_min and t_max in milliseconds  */
        initLong("t_min", 500, "Max. Übetragungszeit", "ms");
        initLong("t_max", 2000, "Min. Übertragungszeit", "ms");
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
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    protected void onClientStart() {
        waitingForResponse = true;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage();
        message.setBoolean("isClientRequest", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        /* Ignore all protocol messages which are not a response message, e.g. itself */
        if (!recvMessage.getBoolean("isServerResponse"))
            return;

        if (waitingForResponse)
            waitingForResponse = false;
        else
            return;

        long tMax = getLong("t_max");
        long tMin = getLong("t_min");
        long serverTime = recvMessage.getLong("time");
        long newTime = serverTime + (long) ((tMax + tMin) / 2 );

        logg("Server Zeit: " + serverTime + "; (t_min,t_max): (" + tMin + "," + tMax
             + "); Alte Zeit: " + process.getTime() + "; Neue Zeit: " + newTime
             + "; Offset: " + (process.getTime() - newTime));

        process.setTime(newTime);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    protected void onServerReset() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        /* Ignore all protocol messages which are not a request message, e.g. itself */
        if (!recvMessage.getBoolean("isClientRequest"))
            return;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage();
        message.setLong("time", process.getTime());
        message.setBoolean("isServerResponse", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
