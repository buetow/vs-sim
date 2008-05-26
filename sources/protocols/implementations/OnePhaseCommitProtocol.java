/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import java.util.ArrayList;

import protocols.VSProtocol;
import core.VSMessage;

/**
 * The Class OnePhaseCommitProtocol.
 */
public class OnePhaseCommitProtocol extends VSProtocol {
    private static final long serialVersionUID = 1L;

    /* Client variables, coordinator */
    private ArrayList<Integer> peerPids;

    /* Server variables, peers */
    private boolean ackSent;

    /**
     * Instantiates a one phase commit protocol.
     */
    public OnePhaseCommitProtocol() {
        setClassname(getClass().toString());
        initInteger("numProcesses", 0, "Anzahl beteilitger Prozesse");
    }

    /* (non-Javadoc)
     * @see events.VSEvent#onInit()
     */
    protected void onInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientReset()
     */
    protected void onClientReset() {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientStart()
     */
    protected void onClientStart() {
        int numProcesses = getInteger("numProcesses");
        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("wantAck", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {

    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerReset()
     */
    protected void onServerReset() {
        ackSent = false;
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
