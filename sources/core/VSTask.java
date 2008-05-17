package core;

import events.*;
import prefs.VSPrefs;
import protocols.VSProtocol;
import simulator.*;

public class VSTask implements Comparable {
    private long taskTime;
    private VSEvent event;
    private VSProcess process;
    private VSPrefs prefs;
    private boolean isProgrammed;

    public VSTask(long taskTime, VSProcess process, VSEvent event) {
        this.process = process;
        this.taskTime = taskTime > 0 ? taskTime : 0;
        this.event = event;
        this.prefs = process.getPrefs();
    }

    public void isProgrammed(boolean isProgrammed) {
        this.isProgrammed = isProgrammed;
    }

    public boolean isProgrammed() {
        return isProgrammed;
    }

    public boolean isMessage() {
        return event instanceof VSMessage;
    }

    public boolean isProcessRecoverEvent() {
        return event instanceof events.implementations.ProcessRecoverEvent;
    }

    public boolean isProtocol(VSProtocol protocol) {
        if (event instanceof VSProtocol)
            return ((VSProtocol) event).equals(protocol);

        return false;
    }

    public boolean isGlobalTimed() {
        if (event instanceof VSProtocol)
            return false;

        return true;
    }

    public VSProcess getProcess() {
        return process;
    }

    public void run() {
        if (event instanceof VSMessage) {
            onMessageRecv();

        } else if (event instanceof VSProtocol) {
            /* Lamport time will get incremented by the VSProtocol class */
            onProtocolStart();

        } else if (event instanceof VSProcessEvent) {
            onProcessEventStart();

        } else {
            onDummy();
        }
    }

    private void onDummy() {
        logg(prefs.getString("lang.process.task"));
    }

    /**
     * If the process recv a message, check if the message's protocol is activated
     * by the process. If yes, run the protocol on the message! If not, just logg
     * that the process does not support this protocol! The process will ignore the
     * message!
     */
    private void onMessageRecv() {
        final VSMessage message = (VSMessage) event;
        final String protocolName = message.getProtocolName();
        final String protocolClassname = message.getProtocolClassname();

        process.updateLamportTime(message.getLamportTime()+1);
        process.updateVectorTime(message.getVectorTime());

        Object protocolObj;

        if (process.objectExists(protocolClassname))
            protocolObj = process.getObject(protocolClassname);
        else
            protocolObj = null;

        String loggVSMessage = prefs.getString("lang.message.recv")
                               + "; " + prefs.getString("lang.protocol") + ": " + protocolName
                               + "; " + prefs.getString("lang.message") + " " + message;

        if (protocolObj == null) {
            logg(loggVSMessage);

        } else {
            final VSProtocol protocol = (VSProtocol) protocolObj;
            logg(loggVSMessage);
            protocol.onMessageRecv(message);
        }
    }

    private void onProtocolStart() {
        ((VSProtocol) event).onStart();
    }

    private void onProcessEventStart() {
        final VSProcessEvent processEvent = (VSProcessEvent) event;
        processEvent.onStart();
    }

    public long getTaskTime() {
        return taskTime;
    }

    public VSEvent getEvent() {
        return event;
    }

    private void logg(String message) {
        process.logg(message);
    }

    public String toString() {
        String descr = "VSTask: " + getTaskTime();

        if (event instanceof VSMessage)
            descr += (VSMessage) event;

        else if (event instanceof VSProtocol)
            descr += (VSProtocol) event;

        return descr;
    }

    public int compareTo(Object object) {
        if (object instanceof VSTask) {
            final VSTask task = (VSTask) object;

            if (taskTime < task.getTaskTime())
                return -1;

            else if (taskTime > task.getTaskTime())
                return 1;
        }

        return 0;
    }
}

