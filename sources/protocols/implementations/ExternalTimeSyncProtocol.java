package protocols.implementations;

import core.VSMessage;
import prefs.VSPrefs;
import protocols.VSProtocol;

public class ExternalTimeSyncProtocol extends VSProtocol {
    private long requestTime;
    private boolean waitingForResponse;

    public ExternalTimeSyncProtocol() {
        setClassname(getClass().toString());
    }

    protected void onInit() {
    }

    protected void onClientReset() {
    }

    protected void onClientStart() {
        requestTime = process.getTime();
        waitingForResponse = true;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("isClientRequest", true);
        sendMessage(message);
    }

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

    protected void onServerReset() {
    }

    protected void onServerRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("isClientRequest"))
            return;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage(getClassname());
        message.setLong("time", process.getTime());
        message.setBoolean("isServerResponse", true);
        sendMessage(message);
    }

    public String toString() {
        return super.toString(); //+ "; " + prefs.getString("lang.requesttime") + ": " + requestTime;
    }
}
