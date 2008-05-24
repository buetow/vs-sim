package protocols;

import prefs.VSPrefs;
import events.*;
import core.*;

abstract public class VSProtocol extends VSEvent {
    private boolean isServer;
    private boolean isClient;
    private boolean currentContextIsServer;

    protected void sendMessage(VSMessage message) {
        if (process == null)
            return;

        process.increaseLamportTime();
        process.increaseVectorTime();
        message.init(process);
        process.sendMessage(message);
    }

    private final boolean isIncorrectProtocol(VSMessage message) {
        return !message.getProtocolClassname().equals(getClassname());
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
            isServer = false;
            onServerReset();
        }

        if (isClient) {
            currentContextIsServer = false;
            isClient = false;
            onClientReset();
        }
    }

    abstract protected void onInit();
    abstract protected void onClientStart();
    abstract protected void onClientReset();
    abstract protected void onClientRecv(VSMessage message);
    abstract protected void onServerReset();
    abstract protected void onServerRecv(VSMessage message);

    protected int getNumProcesses() {
        if (process == null)
            return 0;

        return process.getSimulationCanvas().getNumProcesses();
    }

    public String toString() {
        if (process == null)
            return "";

        StringBuffer buffer = new StringBuffer();

        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": ");
        buffer.append(getShortname());
        buffer.append(" ");

        if (currentContextIsServer)
            buffer.append(prefs.getString("lang.server"));
        else
            buffer.append(prefs.getString("lang.client"));

        return buffer.toString();
    }
}
