/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import java.util.ArrayList;
import java.util.Vector;

import protocols.VSAbstractProtocol;
import core.VSMessage;

/**
 * The Class OnePhaseCommitProtocol.
 */
public class OnePhaseCommitProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /* Server variables, coordinator */
    private ArrayList<Integer> pids;

    /* Client variables */
    private boolean ackSent;

    /**
     * Instantiates a one phase commit protocol.
     */
    public OnePhaseCommitProtocol() {
        setClassname(getClass().toString());

        /* Can be changed via GUI variables editor of each process */
        Vector<Integer> vec = new Vector<Integer>();
        vec.add(2);
        vec.add(3);

        initVector("pids", vec, "PIDs beteilitger Prozesse");
        initLong("timeout", 5000, "Zeit bis erneuerter Anfrage", "ms");
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    protected void onInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientReset()
     */
    protected void onClientReset() {
        pids.clear();
        pids.addAll(getVector("pids"));
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    protected void onClientStart() {
        if (pids == null) {
            pids = new ArrayList<Integer>();
            pids.addAll(getVector("pids"));

        }

        if (pids.size() != 0) {
            long timeout = getLong("timeout") + process.getTime();
            scheduleAt(timeout); /* Will run onClientSchedule() at the specified local time */

            VSMessage message = new VSMessage();
            message.setBoolean("wantAck", true);
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        if (pids.size() == 0)
            return;

        if (recvMessage.getBoolean("isAck")) {
            Integer pid = recvMessage.getIntegerObj("pid");
            if (pids.contains(pid))
                pids.remove(pid);

            logg("ACK von Prozess " + pid + " erhalten!");

            if (pids.size() == 0)
                logg("ACKs von allen beteiligten Prozessen erhalten!");
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientSchedule()
     */
    protected void onClientSchedule() {
        onClientStart();
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    protected void onServerReset() {
        ackSent = false;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        if (ackSent)
            return;

        VSMessage message = new VSMessage();
        message.setBoolean("isAck", true);
        message.setInteger("pid", process.getProcessID());
        sendMessage(message);
        ackSent = true;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerSchedule()
     */
    protected void onServerSchedule() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
