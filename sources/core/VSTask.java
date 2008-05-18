package core;

import events.*;
import events.implementations.*;
import prefs.VSPrefs;
import protocols.VSProtocol;
import simulator.*;

public class VSTask implements Comparable {
    public final static boolean LOCAL = true;
    public final static boolean GLOBAL = false;
    private long taskTime;
    private VSEvent event;
    private VSProcess process;
    private VSPrefs prefs;
    private boolean isProgrammed;
    private boolean isGlobalTimed;

    public VSTask(long taskTime, VSProcess process, VSEvent event, boolean isLocal) {
        this.process = process;
        this.taskTime = taskTime > 0 ? taskTime : 0;
        this.event = event;
        this.prefs = process.getPrefs();
        this.isGlobalTimed = !isLocal;
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

    public boolean isProcess(VSProcess process) {
        return this.process.equals(process);
    }

    public boolean isGlobalTimed() {
        return isGlobalTimed;
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

        } else if (event instanceof VSEvent) {
            onEventStart();

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
        final String eventName = message.getName();
        final String eventClassname = message.getProtocolClassname();

        process.updateLamportTime(message.getLamportTime()+1);
        process.updateVectorTime(message.getVectorTime());

        Object protocolObj;

        if (process.objectExists(eventClassname))
            protocolObj = process.getObject(eventClassname);
        else
            protocolObj = null;

        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.message.recv"));
        buffer.append("; ");
        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": " );
        buffer.append(eventName);
        buffer.append("; ");
        buffer.append(prefs.getString("lang.message"));
        buffer.append(" ");
        buffer.append(message);;

        if (protocolObj == null) {
            logg(buffer.toString());

        } else {
            final VSProtocol protocol = (VSProtocol) protocolObj;
            logg(buffer.toString());
            protocol.onMessageRecv(message);
        }
    }

    private void onProtocolStart() {
        ((VSProtocol) event).onStart();
    }

    private void onEventStart() {
        event.onStart();
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
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.task"));
        buffer.append(" ");
        buffer.append(getTaskTime());

        if (event instanceof VSMessage) {
            buffer.append("; ");
            buffer.append(((VSMessage)event).toString());

        } else if (event instanceof VSProtocol) {
            buffer.append("; ");
            buffer.append(((VSProtocol)event).toString());
        }

        return buffer.toString();
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

