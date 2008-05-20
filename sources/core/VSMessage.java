package core;

import core.time.*;
import events.*;
import prefs.VSPrefs;
import protocols.*;

public class VSMessage extends VSPrefs {
    private String protocolClassname;
    private VSProcess sendingProcess;
    private long messageID;
    private static long messageCounter;
    private long lamportTime;
    private VSVectorTime vectorTime;

    public VSMessage(String protocolClassname) {
        this.protocolClassname = protocolClassname;
        this.messageID = ++messageCounter;
    }

    public void init(VSProcess process) {
        this.sendingProcess = process;
        lamportTime = sendingProcess.getLamportTime();
        vectorTime = sendingProcess.getVectorTime().getCopy();
    }

    public String getName() {
        return VSRegisteredEvents.getName(getProtocolClassname());
    }

    public String getProtocolClassname() {
        return protocolClassname;
    }

    public long getMessageID() {
        return messageID;
    }

    public VSProcess getSendingProcess() {
        return sendingProcess;
    }

    public long getLamportTime() {
        return lamportTime;
    }

    public VSVectorTime getVectorTime() {
        return vectorTime;
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

    public void logg(String message) { }
    public void onInit() { }
    public void onStart() { }
}

