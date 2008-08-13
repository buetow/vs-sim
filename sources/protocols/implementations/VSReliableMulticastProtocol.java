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

package protocols.implementations;

import java.util.ArrayList;
import java.util.Vector;

import protocols.VSAbstractProtocol;
import core.VSMessage;

/**
 * The class VSReliableMulticastProtocol, an implementation of the reliable
 * multicast protocol.
 *
 * @author Paul C. Buetow
 */
public class VSReliableMulticastProtocol extends VSAbstractProtocol {
    /** The serial version uid */
    private static final long serialVersionUID = 2L;

    /**
     * Instantiates a two phase commit protocol object.
     */
    public VSReliableMulticastProtocol() {
        super(VSAbstractProtocol.HAS_ON_CLIENT_START);
        setClassname(getClass().toString());
    }

    /** PIDs of all processes which still have to send an ACK */
    private ArrayList<Integer> pids;

    /* (non-Javadoc)
     * @see events.VSAbstractProtocol#onClientInit()
     */
    public void onClientInit() {
        Vector<Integer> vec = new Vector<Integer>();
        vec.add(1);
        vec.add(3);

        initVector("pids", vec, "PIDs beteiligter Prozesse");
        initLong("timeout", 2500, "Zeit bis erneute Anfrage", "ms");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientReset()
     */
    public void onClientReset() {
        if (pids != null) {
            pids.clear();
            pids.addAll(getVector("pids"));
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    public void onClientStart() {
        if (pids == null) {
            pids = new ArrayList<Integer>();
            pids.addAll(getVector("pids"));
        }

        if (pids.size() != 0) {
            long timeout = getLong("timeout") + process.getTime();
            /* Will run onClientSchedule() at the specified local time */
            scheduleAt(timeout);

            VSMessage message = new VSMessage();
            message.setBoolean("isMulticast", true);
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    public void onClientRecv(VSMessage recvMessage) {
        if (pids.size() != 0 && recvMessage.getBoolean("isAck")) {
            Integer pid = recvMessage.getIntegerObj("pid");

            if (pids.contains(pid))
                pids.remove(pid);
            else
                return;

            log("ACK von Prozess " + pid + " erhalten!");

            if (pids.size() == 0) {
                log("ACKs von allen beteiligten Prozessen " +
                     "erhalten!");

                /* Remove the active schedule which has been created in the
                   onClientStart method */
                removeSchedules();
            }
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientSchedule()
     */
    public void onClientSchedule() {
        onClientStart();
    }

    /** True if ACK has been sent already */
    private boolean ackSent;

    /* (non-Javadoc)
     * @see events.VSAbstractProtocol#onServerInit()
     */
    public void onServerInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    public void onServerReset() {
        ackSent = false;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    public void onServerRecv(VSMessage recvMessage) {
        if (recvMessage.getBoolean("isMulticast")) {
            VSMessage message = new VSMessage();
            message.setBoolean("isAck", true);
            message.setInteger("pid", process.getProcessID());
            sendMessage(message);

            if (ackSent) {
                log("ACK erneut versendet");

            } else {
                log("ACK versendet");
                ackSent = true;
            }
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerSchedule()
     */
    public void onServerSchedule() {
    }
}
