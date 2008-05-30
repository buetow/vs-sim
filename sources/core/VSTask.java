/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core;

import events.*;
import events.implementations.*;
import events.internal.*;
import prefs.VSPrefs;
import protocols.VSAbstractProtocol;

/**
 * The Class VSTask. An object of this class represents a task to do or done.
 * All tasks are managed by the task manager. There are local and global timed
 * tasks. Local timed tasks are being fullfilled if the process' local time is
 * reached. Global timed tasks are being fullfilled if the simulation's time is
 * reached.
 *
 * @author Paul C. Buetow
 */
public class VSTask implements Comparable {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The Constant LOCAL. Used for the constructor if it's a local timed
     * task.
     */
    public final static boolean LOCAL = true;

    /** The Constant GLOBAL. Used for the constructor if it's a global timed
     * task.
     */
    public final static boolean GLOBAL = false;

    /** The task time. */
    private long taskTime;

    /** The event which takes plase if the task time matches. */
    private VSAbstractEvent event;

    /** The process to run the task at. */
    private VSProcess process;

    /** The simulation's default prefs. */
    private VSPrefs prefs;

    /** The message line, which gets drawn by the simulation canvas. this
     * reference is needed here in order to remove the message line from
     * the painting area if the message is not relevant
     */
    private simulator.VSSimulatorCanvas.VSMessageLine messageLine;

    /** The task is programmed. The task will be still in the task manager
     * after reset.
     */
    private boolean isProgrammed;

    /** The task is global timed. If set to true, its local timed. */
    private boolean isGlobalTimed;

    /** The task counter. Needed for the unique task numbers. */
    private static int taskCounter;

    /** The task number. */
    private int taskNum;

    /**
     * Instantiates a new task.
     *
     * @param taskTime the task time
     * @param process the process
     * @param event the event
     * @param isLocal the taks is local timed
     */
    public VSTask(long taskTime, VSProcess process, VSAbstractEvent event,
                  boolean isLocal) {
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
     * Sets if the task is programmed.
     *
     * @param isProgrammed true, if the task is programmed
     */
    public void isProgrammed(boolean isProgrammed) {
        this.isProgrammed = isProgrammed;
    }

    /**
     * Checks if the task is programmed.
     *
     * @return true, if the task is programmed
     */
    public boolean isProgrammed() {
        return isProgrammed;
    }

    /**
     * Checks if the task is a message receive event.
     *
     * @return true, if it is a message receive event
     */
    public boolean isVSMessageReceiveEvent() {
        return event instanceof VSMessageReceiveEvent;
    }

    /**
     * Checks if the task is a process recover event.
     *
     * @return true, if it is a process recover event
     */
    public boolean isVSProcessRecoverEvent() {
        return event instanceof VSProcessRecoverEvent;
    }

    /**
     * Checks if the task belongs to the specified protocol object.
     *
     * @param protocol the protocol object to check against.
     *
     * @return true, if it's a task using the protocol object.
     */
    public boolean isProtocol(VSAbstractProtocol protocol) {
        if (event instanceof VSAbstractProtocol)
            return ((VSAbstractProtocol) event).equals(protocol);

        return false;
    }

    /**
     * Checks if the task's time is over.
     *
     * @return true, if it's over
     */
    public boolean timeOver() {
        if (isGlobalTimed)
            return taskTime < process.getGlobalTime();

        return taskTime < process.getTime();
    }

    /**
     * Checks if the task equals to another task.
     *
     * @param task the task to compare to
     *
     * @return true, if equal (the task nums equal)
     */
    public boolean equals(VSTask task) {
        return taskNum == task.getTaskNum();
    }

    /**
     * Checks if the task belongs to the specified process.
     *
     * @param process the process to check against
     *
     * @return true, if the task is using the process
     */
    public boolean isProcess(VSProcess process) {
        return this.process.equals(process);
    }

    /**
     * Checks if the task is global timed.
     *
     * @return true, if the taks is global timed
     */
    public boolean isGlobalTimed() {
        return isGlobalTimed;
    }

    /**
     * Gets the process.
     *
     * @return the process of the event
     */
    public VSProcess getProcess() {
        return process;
    }

    /**
     * Sets the message line.
     *
     * @param messageLine the message line
     */
    public void setMessageLine(simulator.VSSimulatorCanvas.VSMessageLine
                               messageLine) {
        this.messageLine = messageLine;
    }

    /**
     * Runs the task.
     */
    public void run() {
        if (event.getProcess() == null)
            event.init(process);

        if (messageLine != null) {
            if (!event.onStart())
                process.getSimulatorCanvas().removeMessageLine(messageLine);
        } else {
            event.onStart();
        }
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
    public VSAbstractEvent getEvent() {
        return event;
    }

    /**
     * Logg a message.
     *
     * @param message the message to logg
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
            boolean a = event instanceof VSProcessRecoverEvent;
            boolean b = task.getEvent() instanceof VSProcessRecoverEvent;

            if (a && b)
                return 0;

            if (a)
                return -1;

            if (b)
                return 1;

            /* If it's a VSProtocolEvent, it should get handled first */
            a = event instanceof VSProtocolEvent;
            b = task.getEvent() instanceof VSProtocolEvent;

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

