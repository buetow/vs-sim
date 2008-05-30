/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package simulator;

import core.*;
import events.*;
import events.internal.*;

/**
 * The Class VSCreateTask. An object of this class represents how new
 * VSTask objects are to be created using JComboBox selections of the
 * GUI editor..
 *
 * @author Paul C. Buetow
 */
public class VSCreateTask {
    /** the serial version uid */
    private static final long serialversionuid = 1l;

    /** The event classname. */
    private String eventClassname;

    /** The protocol classname. */
    private String protocolClassname;

    /** The shortname. */
    private String shortname;

    /** The task is a protocol activation. */
    private boolean isProtocolActivation;

    /** The task is a protocol deactivation. */
    private boolean isProtocolDeactivation;

    /** The task is a client protocol. */
    private boolean isClientProtocol;

    /** True, if the task is a client request. false, if the task is a
     * server request
     */
    private boolean isRequest;

    /**
     * Instantiates a new VSCreateTask object.
     *
     * @param eventClassname the event classname
     */
    public VSCreateTask(String eventClassname) {
        this.eventClassname = eventClassname;
    }

    /**
     * Sets if it is  a protocol activation task.
     *
     * @param isProtocolActivation true, if it is a protocol activation
     *	task.
     */
    public void isProtocolActivation(boolean isProtocolActivation) {
        this.isProtocolActivation = isProtocolActivation;

        if (isProtocolActivation)
            isProtocolDeactivation(false);
    }

    /**
     * Sets if it is  a protocol deactivation task.
     *
     * @param isProtocolDeactivation true, if it is a protocol deactivation
     *	task.
     */
    public void isProtocolDeactivation(boolean isProtocolDeactivation) {
        this.isProtocolDeactivation = isProtocolDeactivation;

        if (isProtocolDeactivation)
            isProtocolActivation(false);
    }

    /**
     * Checks if is client protocol.
     *
     * @param isClientProtocol the is client protocol
     */
    public void isClientProtocol(boolean isClientProtocol) {
        this.isClientProtocol = isClientProtocol;
    }

    /**
     * Checks if is client request.
     *
     * @param isRequest the is client request
     */
    public void isRequest(boolean isRequest) {
        this.isRequest = isRequest;
    }

    /**
     * Sets the protocol classname.
     *
     * @param protocolClassname the protocol classname
     */
    public void setProtocolClassname(String protocolClassname) {
        this.protocolClassname = protocolClassname;
    }

    /**
     * Sets the shortname.
     *
     * @param shortname the shortname
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * Creates the task.
     *
     * @param process the process
     * @param time the time
     * @param localTimedTask the local timed task
     *
     * @return the new task
     */
    public VSTask createTask(VSProcess process, long time,
                             boolean localTimedTask) {
        VSAbstractEvent event = null;

        if (isRequest) {
            event = process.getProtocolObject(eventClassname);

        } else {
            event = VSRegisteredEvents.createEventInstanceByClassname(
                        eventClassname, process);
        }

        event.init(process);
        if (shortname != null)
            event.setShortname(shortname);

        if (isProtocolActivation || isProtocolDeactivation) {
            VSProtocolEvent protocolEvent = (VSProtocolEvent) event;
            protocolEvent.setProtocolClassname(protocolClassname);
            protocolEvent.isProtocolActivation(isProtocolActivation);
            protocolEvent.isClientProtocol(isClientProtocol);
        }

        return new VSTask(time, process, event, localTimedTask);
    }
}

