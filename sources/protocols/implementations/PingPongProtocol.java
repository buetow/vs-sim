package protocols.implementations;

import core.VSMessage;
import prefs.VSPrefs;
import protocols.VSProtocol;

public class PingPongProtocol extends VSProtocol {
    private int clientCounter;
    private int serverCounter;

    public PingPongProtocol() {
        setClassname(getClass().toString());
    }

    protected void onInit() {
    }

    protected void onClientReset() {
        clientCounter = 0;
    }

    protected void onClientStart() {
        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("fromClient", true);
        message.setInteger("counter", ++clientCounter);
        sendMessage(message);
    }

    protected void onClientRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("fromServer"))
            return;

        logg("message: " + recvMessage.getInteger("counter"));

        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("fromClient", true);
        message.setInteger("counter", ++clientCounter);
        sendMessage(message);
    }

    protected void onServerReset() {
        serverCounter = 0;
    }

    protected void onServerRecv(VSMessage recvMessage) {
        if (!recvMessage.getBoolean("fromClient"))
            return;

        logg("message: " + recvMessage.getInteger("counter"));

        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("fromServer", true);
        message.setInteger("counter", ++serverCounter);
        sendMessage(message);
    }

    public String toString() {
        return super.toString();
    }
}
