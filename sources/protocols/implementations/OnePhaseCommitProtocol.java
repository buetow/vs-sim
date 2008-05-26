/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import java.util.Vector;

import protocols.VSAbstractProtocol;
import core.VSMessage;

/**
 * The Class OnePhaseCommitProtocol.
 */
public class OnePhaseCommitProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /* Client variables, coordinator */

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
        vec.add(4);

        initVector("pids", vec, "PIDs beteilitger Prozesse");
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
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    protected void onClientStart() {
        int numProcesses = getInteger("numProcesses");
        VSMessage message = new VSMessage();
        message.setBoolean("wantAck", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {

    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientSchedule()
     */
    protected void onClientSchedule() {
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
