/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core;

import core.time.*;
import events.*;
import prefs.VSPrefs;

/**
 * This class represents a message which is send from one process to another
 * process in the simulation.
 *
 * @author Paul C. Buetow
 */
public class VSMessage extends VSPrefs {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** Each message belongs to a specific protocol. This variable defined the
     * class name of the protocol being used.
     */
    private String protocolClassname;

    /** The default application preferences. */
    private VSPrefs prefs;

    /** A reference to the process who sent this message. */
    private VSProcess sendingProcess;

    /** The vector time of the sending process after sending. The receiver
     * process will use this vector time in order to update the local vector
     * time.
     */
    private VSVectorTime vectorTime;

    /** The lamport time of the sending process after sending. The receiver
     * process will use this lamport time in order to update the local vector
     * time.
     */
    private long lamportTime;

    /** Each message has its own unique ID. The ID will show up in the logging
     * window of the simulator
     */
    private long messageID;

    /** This counter is used in order to generate unique message ID's. */
    private static long messageCounter;

    /**
     * The constructor of the message. Creates a new message object.
     */
    public VSMessage() {
        this.messageID = ++messageCounter;
    }

    /**
     * Initializes the message.
     *
     * @param process The sending process of this message.
     * @param protocolClassname The classname of the protocol this message.
     */
    public void init(VSProcess process, String protocolClassname) {
        this.sendingProcess = process;
        this.protocolClassname = protocolClassname;
        this.prefs = process.getPrefs();

        lamportTime = sendingProcess.getLamportTime();
        vectorTime = sendingProcess.getVectorTime().getCopy();
    }

    /**
     * Gets the protocol name of the message.
     *
     * @return The protocol name of the message.
     */
    public String getName() {
        return VSRegisteredEvents.getNameByClassname(getProtocolClassname());
    }

    /**
     * Gets the protocol classname.
     *
     * @return The protocol classname of the message.
     */
    public String getProtocolClassname() {
        return protocolClassname;
    }

    /**
     * Gets the message id.
     *
     * @return The id of the message.
     */
    public long getMessageID() {
        return messageID;
    }

    /**
     * Gets a reference of the sending process.
     *
     * @return The process which sent this message.
     */
    public VSProcess getSendingProcess() {
        return sendingProcess;
    }

    /**
     * Gets the lamport time.
     *
     * @return The lamport time of the sending process.
     */
    public long getLamportTime() {
        return lamportTime;
    }

    /**
     * Gets the vector time.
     *
     * @return The vector time of the sending process.
     */
    public VSVectorTime getVectorTime() {
        return vectorTime;
    }

    /* (non-Javadoc)
     * @see prefs.VSPrefs#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ID: ");
        buffer.append(messageID);
        buffer.append("; ");
        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": ");
        buffer.append(VSRegisteredEvents.getShortnameByClassname(
                          getProtocolClassname()));

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
     * @return true, if the messages have the same id. Otherwise false.
     */
    public boolean equals(VSMessage message) {
        return messageID == message.getMessageID();
    }
}

