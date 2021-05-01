/*
 * VS-Simulator (http://buetow.org)
 * Copyright (c) 2008 by Dipl.-Inform. (FH) Paul C. Buetow
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

package simulator;

import core.*;
import events.*;
import events.internal.*;

/**
 * The class VSCreateTask, an object of this class represents how new
 * VSTask objects are to be created using JComboBox selections of the
 * GUI editor..
 *
 * @author Paul C. Buetow
 */
public class VSCreateTask {
    /** the serial version uid */
    private static final long serialVersionUID = 1L;

    /** The event classname. */
    private String eventClassname;

    /** The create task menu string. */
    private String menuText;

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
     * @param menuText the menu text
     * @param eventClassname the event classname
     */
    public VSCreateTask(String menuText, String eventClassname) {
        this.menuText = menuText;
        this.eventClassname = eventClassname;
    }

    /**
     * Instantiates a new VSCreateTask dummy object.
     *
     * @param menuText the menu text
     */
    public VSCreateTask(String menuText) {
        this.menuText = menuText;
        this.eventClassname = null;
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
     * Sets if it is a client protocol.
     *
     * @param isClientProtocol the is client protocol
     */
    public void isClientProtocol(boolean isClientProtocol) {
        this.isClientProtocol = isClientProtocol;
    }

    /**
     * Sets if it is a client request.
     *
     * @param isRequest the is client request
     */
    public void isRequest(boolean isRequest) {
        this.isRequest = isRequest;
    }

    /**
     * Checks if it is a dummy object..
     *
     * @return true, if dummy
     */
    public boolean isDummy() {
        return eventClassname == null;
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
     * Gets the create tasks menu text.
     *
     * @return The text
     */
    public String getMenuText() {
        return menuText;
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
    public VSTask createTask(VSInternalProcess process, long time,
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

