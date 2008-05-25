/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core;

import core.time.*;
import events.*;
import prefs.VSPrefs;

/**
 * This class represents a message which is send from one process to another process in the simulation.
 */
public class VSMessage extends VSPrefs {

    /** Each message belongs to a specific protocol. This variable defined the class name of the protocol being used. */
    private String protocolClassname;

    /** The default application preferences. */
    private VSPrefs prefs;

    /** A reference to the process who sent this message. */
    private VSProcess sendingProcess;

    /** The vector time of the sending process after sending. The receiver process will use this vector time in order to update the local vector time. */
    private VSVectorTime vectorTime;

    /** The lamport time of the sending process after sending. The receiver process will use this lamport time in order to update the local vector time. */
    private long lamportTime;

    /** Each message has its own unique ID. The ID will show up in the logging window of the simulator as well. */
    private long messageID;

    /** This counter is used in order to generate unique message ID's. */
    private static long messageCounter;

    /**
     * The constructor of the message.
     *
     * @param protocolClassname The classname of the protocol this message
     * belongs to.
     */
    public VSMessage(String protocolClassname) {
        this.protocolClassname = protocolClassname;
        this.messageID = ++messageCounter;
    }

    /**
     * Initialized the message.
     *
     * @param process The sending process of this message.
     */
    public void init(VSProcess process) {
        this.sendingProcess = process;
        this.prefs = process.getPrefs();
        lamportTime = sendingProcess.getLamportTime();
        vectorTime = sendingProcess.getVectorTime().getCopy();
    }

    /**
     * Getter method.
     *
     * @return The protocol name of the message.
     */
    public String getName() {
        return VSRegisteredEvents.getName(getProtocolClassname());
    }

    /**
     * Getter method.
     *
     * @return The protocol classname of the message.
     */
    public String getProtocolClassname() {
        return protocolClassname;
    }

    /**
     * Getter method.
     *
     * @return The ID of the message.
     */
    public long getMessageID() {
        return messageID;
    }

    /**
     * Getter method.
     *
     * @return The process which sent this message.
     */
    public VSProcess getSendingProcess() {
        return sendingProcess;
    }

    /**
     * Getter method.
     *
     * @return The lamport time of the sending process.
     */
    public long getLamportTime() {
        return lamportTime;
    }

    /**
     * Getter method.
     *
     * @return The vector time of the sending process.
     */
    public VSVectorTime getVectorTime() {
        return vectorTime;
    }

    /**
     * String representation of the message object.
     *
     * @return String representation of the message object.
     */
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

    /**
     * Extended string representation of the message object.
     *
     * @return Extended string representation of the message object.
     */
    public String toStringFull() {
        return toString() + "; " + super.toString();
    }

    /**
     * Compares two messages.
     *
     * @param message The message to compare with.
     *
     * @return true, if the messages have the same ID. Otherwise false.
     */
    public boolean equals(VSMessage message) {
        return messageID == message.getMessageID();
    }

    /**
     * For logging in the simulator's logging window!.
     *
     * @param message The message to logg.
     */
    public void logg(String message) { }
}

