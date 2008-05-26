/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols;

import java.util.ArrayList;

import events.internal.*;
import events.*;
import core.*;

/**
 * The Class VSAbstractProtocol.
 */
abstract public class VSAbstractProtocol extends VSAbstractEvent {
    private static final long serialVersionUID = 1L;

    /** The protocol object is a server. */
    private boolean isServer;

    /** The protocol object is a client. */
    private boolean isClient;

    /** The current protocol object's context is a server. */
    private boolean currentContextIsServer;

    /** The protocol's server schedules */
    private ArrayList<VSTask> serverSchedules = new ArrayList<VSTask>();

    /** The protocol's client schedules */
    private ArrayList<VSTask> clientSchedules = new ArrayList<VSTask>();

    /**
     * Send a message.
     *
     * @param message the message to send
     */
    protected void sendMessage(VSMessage message) {
        if (process == null)
            return;

        process.increaseLamportTime();
        process.increaseVectorTime();
        message.init(process, getClassname());
        process.sendMessage(message);
    }

    /**
     * Checks if it's the incorrect protocol
     *
     * @param message the message to check against
     *
     * @return true, if is incorrect protocol
     */
    private final boolean isIncorrectProtocol(VSMessage message) {
        return !message.getProtocolClassname().equals(getClassname());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public final void onStart() {
        if (isClient) {
            currentContextIsServer = false;
            onClientStart();
        }
    }

    /**
     * Runs a client schedule
     */
    public final void onClientScheduleStart() {
        if (isClient) {
            currentContextIsServer = false;
            onClientSchedule();
        }
    }

    /**
     * Runs a server schedule
     */
    public final void onServerScheduleStart() {
        if (isServer) {
            currentContextIsServer = true;
            onServerSchedule();
        }
    }

    /**
     * On message recv.
     *
     * @param message the message
     */
    public final void onMessageRecvStart(VSMessage message) {
        if (isIncorrectProtocol(message))
            return;

        if (isServer) {
            currentContextIsServer = true;
            onServerRecv(message);
        }

        if (isClient) {
            currentContextIsServer = false;
            onClientRecv(message);
        }
    }

    /**
     * Sets if is server.
     *
     * @param isServer the is server
     */
    public final void isServer(boolean isServer) {
        this.isServer = isServer;
    }

    /**
     * Checks if is server.
     *
     * @param isServer the is server
     */
    public final boolean isServer() {
        return isServer;
    }

    /**
     * Sets if is client.
     *
     * @param isClient the is client
     */
    public final void isClient(boolean isClient) {
        this.isClient = isClient;
    }

    /**
     * Checks if is client.
     *
     * @param isClient the is client
     */
    public final boolean isClient() {
        return isClient;
    }

    /**
     * Reset.
     */
    public void reset() {
        //if (isServer) {
        currentContextIsServer = true;
        isServer = false;
        onServerReset();
        serverSchedules.clear();
        //}

        //if (isClient) {
        currentContextIsServer = false;
        isClient = false;
        onClientReset();
        clientSchedules.clear();
        //}
    }

    /**
     * Reschedules the protocol for a new time and runs onClientSchedule or onServerSchedule
     *
     * @param time The process' local time to run the schedule at.
     */
    protected final void scheduleAt(long time) {
        VSAbstractEvent scheduleEvent = new ProtocolScheduleEvent(this, currentContextIsServer);
        VSTask scheduleTask = new VSTask(time, process, scheduleEvent, VSTask.LOCAL);
        if (currentContextIsServer)
            serverSchedules.add(scheduleTask);
        else
            clientSchedules.add(scheduleTask);
        process.getSimulationCanvas().getTaskManager().addTask(scheduleTask);
    }

    /**
     * Removes all schedules of the protocol (server or client)
     */
    protected final void removeSchedules() {
        if (currentContextIsServer) {
            process.getSimulationCanvas().getTaskManager().removeAllTasks(serverSchedules);
            serverSchedules.clear();
        } else {
            process.getSimulationCanvas().getTaskManager().removeAllTasks(clientSchedules);
            clientSchedules.clear();
        }
    }

    /**
     * On client start.
     */
    abstract protected void onClientStart();

    /**
     * On client reset.
     */
    abstract protected void onClientReset();

    /**
     * On client schedule.
     */
    abstract protected void onClientSchedule();

    /**
     * On client recv.
     *
     * @param message the message
     */
    abstract protected void onClientRecv(VSMessage message);

    /**
     * On server reset.
     */
    abstract protected void onServerReset();

    /**
     * On server recv.
     *
     * @param message the message
     */
    abstract protected void onServerRecv(VSMessage message);

    /**
     * On server schedule.
     */
    abstract protected void onServerSchedule();

    /**
     * Gets the num processes.
     *
     * @return the num processes
     */
    protected int getNumProcesses() {
        if (process == null)
            return 0;

        return process.getSimulationCanvas().getNumProcesses();
    }

    /* (non-Javadoc)
     * @see prefs.VSPrefs#toString()
     */
    public String toString() {
        if (process == null)
            return "";

        StringBuffer buffer = new StringBuffer();

        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": ");
        buffer.append(getShortname());
        buffer.append(" ");

        if (currentContextIsServer)
            buffer.append(prefs.getString("lang.server"));
        else
            buffer.append(prefs.getString("lang.client"));

        return buffer.toString();
    }
}
