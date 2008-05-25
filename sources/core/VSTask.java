/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core;

import events.*;
import events.implementations.*;
import events.internal.*;
import prefs.VSPrefs;
import protocols.VSProtocol;

// TODO: Auto-generated Javadoc
/**
 * The Class VSTask.
 */
public class VSTask implements Comparable {
    
    /** The Constant LOCAL. */
    public final static boolean LOCAL = true;
    
    /** The Constant GLOBAL. */
    public final static boolean GLOBAL = false;
    
    /** The task time. */
    private long taskTime;
    
    /** The event. */
    private VSEvent event;
    
    /** The process. */
    private VSProcess process;
    
    /** The prefs. */
    private VSPrefs prefs;
    
    /** The is programmed. */
    private boolean isProgrammed;
    
    /** The is global timed. */
    private boolean isGlobalTimed;
    
    /** The task counter. */
    private static int taskCounter;
    
    /** The task num. */
    private int taskNum;

    /**
     * Instantiates a new vS task.
     * 
     * @param taskTime the task time
     * @param process the process
     * @param event the event
     * @param isLocal the is local
     */
    public VSTask(long taskTime, VSProcess process, VSEvent event, boolean isLocal) {
        this.process = process;
        this.taskTime = taskTime > 0 ? taskTime : 0;
        this.event = event;
        this.prefs = process.getPrefs();
        this.isGlobalTimed = !isLocal;
        this.taskNum = ++taskCounter;
    }

    /**
     * Gets the task num.
     * 
     * @return the task num
     */
    public int getTaskNum() {
        return taskNum;
    }

    /**
     * Checks if is programmed.
     * 
     * @param isProgrammed the is programmed
     */
    public void isProgrammed(boolean isProgrammed) {
        this.isProgrammed = isProgrammed;
    }

    /**
     * Checks if is programmed.
     * 
     * @return true, if is programmed
     */
    public boolean isProgrammed() {
        return isProgrammed;
    }

    /**
     * Checks if is message receive event.
     * 
     * @return true, if is message receive event
     */
    public boolean isMessageReceiveEvent() {
        return event instanceof MessageReceiveEvent;
    }

    /**
     * Checks if is process recover event.
     * 
     * @return true, if is process recover event
     */
    public boolean isProcessRecoverEvent() {
        return event instanceof ProcessRecoverEvent;
    }

    /**
     * Checks if is protocol.
     * 
     * @param protocol the protocol
     * 
     * @return true, if is protocol
     */
    public boolean isProtocol(VSProtocol protocol) {
        if (event instanceof VSProtocol)
            return ((VSProtocol) event).equals(protocol);

        return false;
    }

    /**
     * Time over.
     * 
     * @return true, if successful
     */
    public boolean timeOver() {
        if (isGlobalTimed)
            return taskTime < process.getGlobalTime();

        return taskTime < process.getTime();
    }

    /**
     * Equals.
     * 
     * @param task the task
     * 
     * @return true, if successful
     */
    public boolean equals(VSTask task) {
        return taskNum == task.getTaskNum();
        /*
        return event.equals(task.getEvent())
               && taskTime == task.getTaskTime()
               && isGlobalTimed == task.isGlobalTimed()
               && isProgrammed == task.isProgrammed;
        	   */
    }

    /**
     * Checks if is process.
     * 
     * @param process the process
     * 
     * @return true, if is process
     */
    public boolean isProcess(VSProcess process) {
        return this.process.equals(process);
    }

    /**
     * Checks if is global timed.
     * 
     * @return true, if is global timed
     */
    public boolean isGlobalTimed() {
        return isGlobalTimed;
    }

    /**
     * Gets the process.
     * 
     * @return the process
     */
    public VSProcess getProcess() {
        return process;
    }

    /**
     * Run.
     */
    public void run() {
        if (event.getProcess() == null)
            event.init(process);
        event.onStart();
    }

    /**
     * Gets the task time.
     * 
     * @return the task time
     */
    public long getTaskTime() {
        return taskTime;
    }

    /**
     * Gets the event.
     * 
     * @return the event
     */
    public VSEvent getEvent() {
        return event;
    }

    /**
     * Logg.
     * 
     * @param message the message
     */
    private void logg(String message) {
        process.logg(message);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.task"));
        buffer.append(" ");
        buffer.append(getTaskTime());
        buffer.append(event.toString());
        buffer.append("; PID: ");
        buffer.append(process.getProcessID());
        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object object) {
        if (object instanceof VSTask) {
            final VSTask task = (VSTask) object;

            if (taskTime < task.getTaskTime())
                return -1;

            else if (taskTime > task.getTaskTime())
                return 1;

            /* If it's a ProtocolRecover, it should get handled very first */
            boolean a = event instanceof ProcessRecoverEvent;
            boolean b = task.getEvent() instanceof ProcessRecoverEvent;

            if (a && b)
                return 0;

            if (a)
                return -1;

            if (b)
                return 1;


            /* If it's a ProtocolEvent, it should get handled first */
            a = event instanceof ProtocolEvent;
            b = task.getEvent() instanceof ProtocolEvent;

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

