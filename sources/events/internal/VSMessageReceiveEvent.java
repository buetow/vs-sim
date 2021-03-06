/*
 * Copyright (c) 2008 Paul C. Buetow, vs-sim@dev.buetow.org
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

//import java.io.*;

import core.VSMessage;
import protocols.VSAbstractProtocol;
import serialize.VSNotSerializable;

/**
 * The class VSMessageReceiveEvent, this event is used if a process receives
 * a message.
 *
 * @author Paul C. Buetow
 */
public class VSMessageReceiveEvent extends VSAbstractInternalEvent
            implements VSNotSerializable {
    /** The serioal version uid */
    private static final long serialVersionUID = 1L;

    /** The message. */
    private VSMessage message;

    /**
     * Instantiates a new message receive event.
     *
     * @param message the message
     */
    public VSMessageReceiveEvent(VSMessage message) {
        this.message = message;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public void onStart() {
        boolean onlyRelevantMessages = process.getPrefs().getBoolean(
                                           "sim.messages.relevant");

        //String eventName = message.getName();
        String protocolClassname = message.getProtocolClassname();

        if (onlyRelevantMessages && !isRelevantMessage())
            return;

        Object protocolObj = null;

        if (process.objectExists(protocolClassname))
            protocolObj = process.getObject(protocolClassname);

        process.updateLamportTime(message.getLamportTime()+1);
        process.updateVectorTime(message.getVectorTime());

        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.en.message.recv"));
        buffer.append("; ");
        buffer.append(message);;
        log(buffer.toString());

        if (protocolObj != null)
            ((VSAbstractProtocol) protocolObj).onMessageRecvStart(message);
    }

    /**
     * Checks if the message delivering is relevant.
     *
     * @return true, if relevant
     */
    public boolean isRelevantMessage() {
        String protocolClassname = message.getProtocolClassname();
        Object protocolObj = null;

        if (process.objectExists(protocolClassname))
            protocolObj = process.getObject(protocolClassname);
        else
            return false;

        if (!((VSAbstractProtocol) protocolObj).isRelevantMessage(message))
            return false;

        return true;
    }
}
