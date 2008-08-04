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
 * The class VSOnePhaseCommitProtocol, an implementation of the one phase
 * commit protocol.
 *
 * @author Paul C. Buetow
 */
public class VSOnePhaseCommitProtocol extends VSAbstractProtocol {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /* Server variables */
    private boolean ackSent;

    /**
     * Instantiates a one phase commit protocol.
     */
    public VSOnePhaseCommitProtocol() {
        super(VSAbstractProtocol.HAS_ON_SERVER_START);
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractProtocol#onServerInit()
     */
    public void onServerInit() {
        /* Can be changed via GUI variables editor of each process */
        Vector<Integer> vec = new Vector<Integer>();
        vec.add(1);
        vec.add(3);

        initVector("pids", vec, "PIDs beteilitger Prozesse");
        initLong("timeout", 2500, "Zeit bis erneute Anfrage", "ms");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    public void onServerReset() {
        if (pids != null) {
            pids.clear();
            pids.addAll(getVector("pids"));
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerStart()
     */
    public void onServerStart() {
        if (pids == null) {
            pids = new ArrayList<Integer>();
            pids.addAll(getVector("pids"));
        }

        if (pids.size() != 0) {
            long timeout = getLong("timeout") + process.getTime();
            /* Will run onServerSchedule() at the specified local time */
            scheduleAt(timeout);

            VSMessage message = new VSMessage();
            message.setBoolean("wantAck", true);
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    public void onServerRecv(VSMessage recvMessage) {
        if (pids.size() == 0)
            return;

        if (recvMessage.getBoolean("isAck")) {
            Integer pid = recvMessage.getIntegerObj("pid");
            if (pids.contains(pid))
                pids.remove(pid);
            else
                return;

            logg("ACK von Prozess " + pid + " erhalten!");

            if (pids.size() == 0) {
                logg("ACKs von allen beteiligten Prozessen erhalten! " +
                     "Festgeschrieben!");

                /* Remove the active schedule which has been created in the
                   onServerStart method */
                removeSchedules();
            }
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerSchedule()
     */
    public void onServerSchedule() {
        onServerStart();
    }


    /* Client variables, coordinator */
    private ArrayList<Integer> pids;

    /* (non-Javadoc)
     * @see events.VSAbstractProtocol#onClientInit()
     */
    public void onClientInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientReset()
     */
    public void onClientReset() {
        ackSent = false;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    public void onClientRecv(VSMessage recvMessage) {
        if (ackSent)
            return;

        VSMessage message = new VSMessage();
        message.setBoolean("isAck", true);
        message.setInteger("pid", process.getProcessID());
        sendMessage(message);
        ackSent = true;
        logg("Festgeschrieben");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientSchedule()
     */
    public void onClientSchedule() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
