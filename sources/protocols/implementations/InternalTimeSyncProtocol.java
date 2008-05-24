package protocols.implementations;

import core.VSMessage;
import prefs.VSPrefs;
import protocols.VSProtocol;

public class InternalTimeSyncProtocol extends VSProtocol {
    private boolean waitingForResponse;

    public InternalTimeSyncProtocol() {
        setClassname(getClass().toString());

        /* Those prefs are editable through the VSProtocol VSEditor GUI. t_min and t_max in milliseconds  */
        setLong("t_min", 1000);
        setLong("t_max", 5000);
    }

    protected void onInit() {
    }

    protected void onClientReset() {
    }

    protected void onClientStart() {
        waitingForResponse = true;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("isClientRequest", true);
        sendMessage(message);
    }

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

    protected void onServerReset() {
    }

    protected void onServerRecv(VSMessage recvMessage) {
        /* Ignore all protocol messages which are not a request message, e.g. itself */
        if (!recvMessage.getBoolean("isClientRequest"))
            return;

        /* Multicast message to all processes */
        VSMessage message = new VSMessage(getClassname());
        message.setLong("time", process.getTime());
        message.setBoolean("isServerResponse", true);
        sendMessage(message);
    }

    public String toString() {
        return super.toString();
    }
}
