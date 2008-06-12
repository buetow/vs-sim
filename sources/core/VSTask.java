/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 *
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package core;

import java.io.*;

import events.*;
import events.implementations.*;
import events.internal.*;
import prefs.VSPrefs;
import protocols.VSAbstractProtocol;
import serialize.*;
import utils.*;

/**
 * The class VSTask, an object of this class represents a task to do or done.
 * All tasks are managed by the task manager. There are local and global timed
 * tasks. Local timed tasks are being fullfilled if the process' local time is
 * reached. Global timed tasks are being fullfilled if the simulator's time is
 * reached.
 *
 * @author Paul C. Buetow
 */
public class VSTask implements Comparable, VSSerializable {
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

    /** The simulator's default prefs. */
    private VSPrefs prefs;

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
        init(taskTime, process, event, isLocal);
    }

    /**
     * Instantiates a new task during a deserialization.
     *
     * @param serialize the serialize object
     * @param objectInputStream The input stream
     */
    public VSTask(VSSerialize serialize, ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException {
        deserialize(serialize, objectInputStream);
    }

    /**
     * Inits the task
     *
     * @param taskTime the task time
     * @param process the process
     * @param event the event
     * @param isLocal the taks is local timed
     */
    private void init(long taskTime, VSProcess process, VSAbstractEvent event,
                      boolean isLocal) {
        this.process = process;
        this.taskTime = taskTime > 0 ? taskTime : 0;
        /* May be not null if called from deserialization */
        if (this.event == null)
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
     * Checks if the task is using an "internal event".
     *
     * @return true, if the task is using an internal event
     */
    public boolean hasInternalEvent() {
        return event instanceof VSAbstractInternalEvent;
    }

    /**
     * Checks if the task should not get serialized.
     *
     * @return true, if the task should not get serialized
     */
    public boolean hasNotSerializableEvent() {
        return event instanceof VSNotSerializable;
    }

    /**
     * Checks if the task is a message receive event.
     *
     * @return true, if it is a message receive event
     */
    public boolean hasMessageReceiveEvent() {
        return event instanceof VSMessageReceiveEvent;
    }

    /**
     * Checks if the task is a process recover event.
     *
     * @return true, if it is a process recover event
     */
    public boolean hasProcessRecoverEvent() {
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
     * Runs the task.
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
        /*
        if (isProgrammed()) {
        	buffer.append("; Programmed");
        }
        */

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

            VSAbstractEvent event2 = task.getEvent();

            /* If it's a recovering, it should get handled very first */
            boolean a = event instanceof VSProcessRecoverEvent;
            boolean b = event2 instanceof VSProcessRecoverEvent;

            if (a && b)
                return 0;

            if (a)
                return -1;

            if (b)
                return 1;

            /* If it's a crash, it should get handled second first */
            a = event instanceof VSProcessCrashEvent;
            b = event2 instanceof VSProcessCrashEvent;

            if (a && b)
                return 0;

            if (a)
                return -1;

            if (b)
                return 1;

            /* If it's a VSProtocolEvent, it should get handled third  */
            a = event instanceof VSProtocolEvent;
            b = event2 instanceof VSProtocolEvent;

            if (a && b)
                return 0;

            if (a)
                return -1;

            if (b)
                return 1;

            String shortname = event.getShortname();
            String shortname2 = event2.getShortname();

			/* One of those may be null if an VSAbstractEvent object has not 
			   been initialized yet */
            if (shortname == null || shortname2 == null)
                return 0;

            return shortname.compareTo(shortname2);
        }

        return 0;
    }

    /* (non-Javadoc)
     * @see serialize.VSSerializable#serialize(serialize.VSSerialize,
     *	java.io.ObjectOutputStream)
     */
    public synchronized void serialize(VSSerialize serialize,
                                       ObjectOutputStream objectOutputStream)
    throws IOException {
        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));

        objectOutputStream.writeObject(new Integer(process.getProcessNum()));

        if (event.getClassname() == null)
            event.init(process);

        if (VSSerialize.DEBUG)
            System.out.println("Serializing: " + event.getClassname());

        objectOutputStream.writeObject(event.getClassname());
        objectOutputStream.writeObject(new Integer(event.getID()));
        event.serialize(serialize, objectOutputStream);
        objectOutputStream.writeObject(new Integer(taskNum));
        objectOutputStream.writeObject(new Long(taskTime));
        objectOutputStream.writeObject(new Boolean(isGlobalTimed));
        objectOutputStream.writeObject(new Boolean(isProgrammed));

        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));
    }

    /* (non-Javadoc)
     * @see serialize.VSSerializable#deserialize(serialize.VSSerialize,
     *	java.io.ObjectInputStream)
     */
    @SuppressWarnings("unchecked")
    public synchronized void deserialize(VSSerialize serialize,
                                         ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException {
        if (VSSerialize.DEBUG)
            System.out.println("Deserializing: VSTask");

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();

        int processNum = ((Integer) objectInputStream.readObject()).intValue();
        VSProcess process = (VSProcess)
                            serialize.getObject(processNum, "process");

        String eventClassname = (String) objectInputStream.readObject();
        int eventID = ((Integer) objectInputStream.readObject()).intValue();

        VSAbstractEvent event = null;

        if (serialize.objectExists(eventID, "event")) {
            event = (VSAbstractEvent) serialize.getObject(eventID, "event");

        } else {
            event = VSRegisteredEvents.
                    createEventInstanceByClassname(eventClassname, process);

            serialize.setObject(eventID, "event", event);
        }

        event.deserialize(serialize, objectInputStream);

        int taskNum = ((Integer) objectInputStream.readObject()).intValue();
        long taskTime = ((Long) objectInputStream.readObject()).longValue();
        Boolean isGlobalTimed = (Boolean) objectInputStream.readObject();
        Boolean isProgrammed = (Boolean) objectInputStream.readObject();

        serialize.setObject(taskNum, "task", this);
        init(taskTime, process, event, !isGlobalTimed.booleanValue());
        this.isProgrammed = isProgrammed.booleanValue();

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();
    }
}

