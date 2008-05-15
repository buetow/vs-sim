package core;

import prefs.VSPrefs;
import protocols.*;
import events.VSEvent;

public class VSMessage extends VSPrefs implements VSEvent {
    private String protocolClassname;
    private VSProcess sendingProcess;
    private long messageID;
    private static long messageCounter;
    private long lamportTime;

    public VSMessage(String protocolClassname) {
        this.protocolClassname = protocolClassname;
        this.messageID = ++messageCounter;
    }

    public String getProtocolName() {
        return RegisteredProtocols.getProtocolName(getProtocolClassname());
    }

    public String getProtocolClassname() {
        return protocolClassname;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setSendingProcess(VSProcess sendingProcess) {
        this.sendingProcess = sendingProcess;
        lamportTime = sendingProcess.getLamportTime();
    }

    public VSProcess getSendingProcess() {
        return sendingProcess;
    }

    public long getLamportTime() {
        return lamportTime;
    }

    public String toString() {
        return "ID: " + messageID;
    }

    public String toStringFull() {
        return toString() + "; " + super.toString();
    }

    public boolean equals(VSMessage message) {
        return messageID == message.getMessageID();
    }
}

