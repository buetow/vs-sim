package core;

import core.time.*;
import events.*;
import prefs.VSPrefs;

public class VSMessage extends VSPrefs {
    private String protocolClassname;
    private VSPrefs prefs;
    private VSProcess sendingProcess;
    private VSVectorTime vectorTime;
    private long lamportTime;
    private long messageID;
    private static long messageCounter;

    public VSMessage(String protocolClassname) {
        this.protocolClassname = protocolClassname;
        this.messageID = ++messageCounter;
    }

    public void init(VSProcess process) {
        this.sendingProcess = process;
        this.prefs = process.getPrefs();
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
        StringBuffer buffer = new StringBuffer();

        buffer.append("ID: ");
        buffer.append(messageID);
        buffer.append("; ");
        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": ");
        buffer.append(VSRegisteredEvents.getShortname(getProtocolClassname()));

        return buffer.toString();
    }

    public String toStringFull() {
        return toString() + "; " + super.toString();
    }

    public boolean equals(VSMessage message) {
        return messageID == message.getMessageID();
    }

    public void logg(String message) { }
}

