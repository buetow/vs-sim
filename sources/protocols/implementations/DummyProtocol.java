package protocols.implementations;

import protocols.VSProtocol;
import core.VSMessage;

public class DummyProtocol extends VSProtocol {
    public DummyProtocol() {
        setClassname(getClass().toString());
    }

    protected void onInit() {
    }

    protected void onClientReset() {
        logg("onClientReset()");
    }

    protected void onClientStart() {
        logg("onClientStart()");

        VSMessage message = new VSMessage(getClassname());
        message.setString("Greeting", "Hello World!");
        message.setInteger("A number", 1);
        message.setBoolean("A boolean", true);
        message.setFloat("A float", 1.2f);
        sendMessage(message);
    }

    protected void onClientRecv(VSMessage recvMessage) {
        logg("onClientRecv("+recvMessage+")");

        String s = recvMessage.getString("Greeting");
        int n = recvMessage.getInteger("A number");
        boolean b = recvMessage.getBoolean("A boolean");
        float f = recvMessage.getFloat("A float");
    }

    protected void onServerReset() {
        logg("onClientReset()");
    }

    protected void onServerRecv(VSMessage recvMessage) {
        logg("onServerRecv("+recvMessage+")");
    }

    public String toString() {
        return super.toString() + "; Dummy Test";
    }
}
