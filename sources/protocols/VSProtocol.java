package protocols;

import prefs.VSPrefs;
import events.VSEvent;
import core.*;

abstract public class VSProtocol extends VSPrefs implements VSEvent {
    protected VSPrefs prefs;
    private String protocolClassname;
    private boolean isServer;
    private boolean isClient;
    protected VSProcess process;
    private boolean currentContextIsServer;

    public void init(VSProcess process) {
        this.process = process;
        this.prefs = process.getPrefs();

        onInit();
    }

    protected final void setProtocolClassname(String protocolClassname) {
        if (protocolClassname.startsWith("class "))
            protocolClassname = protocolClassname.substring(6);

        this.protocolClassname = protocolClassname;
    }

    public final String getProtocolClassname() {
        return protocolClassname;
    }

    public final String getProtocolName() {
        return VSRegisteredProtocols.getProtocolName(protocolClassname);
    }

    public final String getProtocolShortname() {
        return VSRegisteredProtocols.getProtocolShortname(protocolClassname);
    }

    public final VSProcess getProcess() {
        return process;
    }

    protected void sendMessage(VSMessage message) {
        process.increaseLamportTime();
        process.increaseVectorTime();
        message.init(process);
        process.sendMessage(message);
    }

    private final boolean isIncorrectProtocol(VSMessage message) {
        return !message.getProtocolClassname().equals(getProtocolClassname());
    }

    public final void onStart() {
        if (isClient) {
            onClientStart();
            currentContextIsServer = false;
        }
    }

    public final void onMessageRecv(VSMessage message) {
        if (isIncorrectProtocol(message))
            return;

        if (isServer) {
            currentContextIsServer = true;
            onServerRecv(message);
        }

        if (isClient) {
            currentContextIsServer = false;
            onClientRecv(message);
        }
    }

    public final void isServer(boolean isServer) {
        this.isServer = isServer;
    }

    public final void isClient(boolean isClient) {
        this.isClient = isClient;
    }

    public void reset() {
        if (isServer) {
            currentContextIsServer = true;
            onServerReset();
        }

        if (isClient) {
            currentContextIsServer = false;
            onClientReset();
        }
    }

    abstract protected void onInit();
    abstract protected void onClientStart();
    abstract protected void onClientReset();
    abstract protected void onClientRecv(VSMessage message);

    abstract protected void onServerReset();
    abstract protected void onServerRecv(VSMessage message);

    public void logg(String message) {
        process.logg(toString() + "; " + message);
    }

    public boolean equals(VSProtocol protocol) {
        return protocol.getID() == getID();
    }

    protected int getNumProcesses() {
        return process.getSimulationPanel().getNumProcesses();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": ");
        buffer.append(getProtocolShortname());
        buffer.append(" ");

        if (currentContextIsServer)
            buffer.append(prefs.getString("lang.server"));
        else
            buffer.append(prefs.getString("lang.client"));

        return buffer.toString();
    }
}
