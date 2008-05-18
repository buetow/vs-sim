package protocols.implementations;

import java.util.ArrayList;

import core.VSMessage;
import prefs.VSPrefs;
import protocols.VSProtocol;

public class BroadcastSturmProtocol extends VSProtocol {
    private ArrayList<VSMessage> sentMessages;
    private static int broadcastCount;

    protected void onInit() {
        setClassname(getClass().toString());
        sentMessages = new ArrayList<VSMessage>();
    }

    protected void onClientReset() {
    }

    protected void onClientStart() {
        VSMessage message = new VSMessage(getClassname());
        message.setInteger("Broadcast", broadcastCount++);
        sentMessages.add(message);
        sendMessage(message);
    }

    protected void onClientRecv(VSMessage recvMessage) {
    }

    protected void onServerReset() {
        sentMessages.clear();
    }

    protected void onServerRecv(VSMessage recvMessage) {
        if (!sentMessages.contains(recvMessage)) {
            VSMessage message = new VSMessage(getClassname());
            message.setInteger("Broadcast", recvMessage.getInteger("Broadcast"));

            sentMessages.add(message);
            sendMessage(message);
        }
    }

    public String toString() {
        return super.toString();
    }
}
