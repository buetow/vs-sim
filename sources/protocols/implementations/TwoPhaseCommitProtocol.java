/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import protocols.VSProtocol;
import core.VSMessage;

/**
 * The Class TwoPhaseCommitProtocol
 */
public class TwoPhaseCommitProtocol extends VSProtocol {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new two phase commit protocol.
     */
    public TwoPhaseCommitProtocol() {
        setClassname(getClass().toString());
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
