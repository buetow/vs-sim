/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import protocols.VSProtocol;
import core.VSMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class DummyProtocol.
 */
public class DummyProtocol extends VSProtocol {
    
    /**
     * Instantiates a new dummy protocol.
     */
    public DummyProtocol() {
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
        logg("onClientReset()");
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientStart()
     */
    protected void onClientStart() {
        logg("onClientStart()");

        VSMessage message = new VSMessage(getClassname());
        message.setString("Greeting", "Hello World!");
        message.setInteger("A number", 1);
        message.setBoolean("A boolean", true);
        message.setFloat("A float", 1.2f);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        logg("onClientRecv("+recvMessage+")");

        String s = recvMessage.getString("Greeting");
        int n = recvMessage.getInteger("A number");
        boolean b = recvMessage.getBoolean("A boolean");
        float f = recvMessage.getFloat("A float");
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerReset()
     */
    protected void onServerReset() {
        logg("onClientReset()");
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        logg("onServerRecv("+recvMessage+")");
    }

    /* (non-Javadoc)
     * @see protocols.VSProtocol#toString()
     */
    public String toString() {
        return super.toString() + "; Dummy Test";
    }
}
