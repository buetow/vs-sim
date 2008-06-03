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

package events.internal;

import java.io.*;

import events.*;
import protocols.VSAbstractProtocol;
import serialize.VSSerialize;

/**
 * The class VSProtocolScheduleEvent. This event is used if a protocol (which
 * is a subclass of VSAbstractProtocol) reschedules itself to run again on a
 * specific time.
 *
 * @author Paul C. Buetow
 */
public class VSProtocolScheduleEvent extends VSAbstractEvent {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The event is a server protocol schedule. */
    private boolean isServerSchedule; /* true = server, false = client */

    /** The reference to the protocol object to schedule. */
    private VSAbstractProtocol protocol;

    /**
     * Create a VSProtocolScheduleEvent object
     *
     * @param protocol the protocol
     * @param isServerSchedule the event is a client protocol schedule if
     *	false, else server schedule
     */
    public VSProtocolScheduleEvent(VSAbstractProtocol protocol,
                                   boolean isServerSchedule) {
        this.protocol = protocol;
        this.isServerSchedule = isServerSchedule;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /**
     * Sets if it is client protocol schedule.
     *
     * @param isServerSchedule false, if the event is a client protocol
     * schedule. true, if server.
     */
    public void isServerSchedule(boolean isServerSchedule) {
        this.isServerSchedule = isServerSchedule;
    }

    /**
     * Sets if it is client protocol schedule.
     *
     * @return false, if the event is a client protocol schedule. true, if
     *	server.
     */
    public boolean isServerSchedule() {
        return isServerSchedule;
    }

    /**
     * Sets the protocol.
     *
     * @param protocol the protocol
     */
    public void setProtocol(VSAbstractProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public VSAbstractProtocol getProtocol() {
        return protocol;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public void onStart() {
        if (isServerSchedule)
            protocol.onServerScheduleStart();
        else
            protocol.onClientScheduleStart();
    }

    /* (non-Javadoc)
     * @see serialize.VSSerializable#serialize(serialize.VSSerialize,
     *	java.io.ObjectOutputStream)
     */
    public synchronized void serialize(VSSerialize serialize,
                                       ObjectOutputStream objectOutputStream)
    throws IOException {
        super.serialize(serialize, objectOutputStream);

        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));

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
        super.deserialize(serialize, objectInputStream);

        if (VSSerialize.DEBUG)
            System.out.println("Deserializing: VSProtocolEvent");

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();

    }
}
