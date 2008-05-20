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

    public boolean isMessageReceiveEvent() {
        return event instanceof events.implementations.MessageReceiveEvent;
    }

    public boolean isProcessRecoverEvent() {
        return event instanceof events.implementations.ProcessRecoverEvent;
    }

    public boolean isProtocol(VSProtocol protocol) {
        if (event instanceof VSProtocol)
            return ((VSProtocol) event).equals(protocol);

        return false;
    }

    public boolean timeOver() {
        if (isGlobalTimed)
            return taskTime < process.getGlobalTime();

        return taskTime < process.getTime();
    }

    public boolean equals(VSTask task) {
        return event.equals(task.getEvent())
               && taskTime == task.getTaskTime()
               && isGlobalTimed == task.isGlobalTimed()
               && isProgrammed == task.isProgrammed;
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
        if (event.getProcess() == null)
            event.init(process);
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
        buffer.append(event.toString());
        return buffer.toString();
    }

    public int compareTo(Object object) {
        if (object instanceof VSTask) {
            final VSTask task = (VSTask) object;

            if (taskTime < task.getTaskTime())
                return -1;

            else if (taskTime > task.getTaskTime())
                return 1;

            /* If it's a ProtocolEvent, it should get handled first */
            boolean a = event instanceof ProtocolEvent;
            boolean b = task.getEvent() instanceof ProtocolEvent;

            if (a && b)
                return 0;

            if (a)
                return -1;

            if (b)
                return 1;
        }

        return 0;
    }
}

