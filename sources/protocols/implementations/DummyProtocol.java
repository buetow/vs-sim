/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import protocols.VSAbstractProtocol;
import core.VSMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class DummyProtocol.
 */
public class DummyProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new dummy protocol.
     */
    public DummyProtocol() {
        setClassname(getClass().toString());
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
        logg("onClientReset()");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    protected void onClientStart() {
        logg("onClientStart()");

        VSMessage message = new VSMessage();
        message.setString("Greeting", "Hello World!");
        message.setInteger("A number", 1);
        message.setBoolean("A boolean", true);
        message.setFloat("A float", 1.2f);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        logg("onClientRecv("+recvMessage+")");

        String s = recvMessage.getString("Greeting");
        int n = recvMessage.getInteger("A number");
        boolean b = recvMessage.getBoolean("A boolean");
        float f = recvMessage.getFloat("A float");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    protected void onServerReset() {
        logg("onClientReset()");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        logg("onServerRecv("+recvMessage+")");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#toString()
     */
    public String toString() {
        return super.toString() + "; Dummy Test";
    }
}
