package protocols;

import prefs.VSPrefs;
import events.*;
import core.*;

abstract public class VSProtocol extends VSEvent {
    private boolean isServer;
    private boolean isClient;
    private boolean currentContextIsServer;

    public void init(VSProcess process) {
        super.init(process);
        onInit();
    }

    protected void sendMessage(VSMessage message) {
        process.increaseLamportTime();
        process.increaseVectorTime();
        message.init(process);
        process.sendMessage(message);
    }

    private final boolean isIncorrectProtocol(VSMessage message) {
        return !message.getClassname().equals(getClassname());
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

    protected int getNumProcesses() {
        return process.getSimulationPanel().getNumProcesses();
    }

    public String toString() {
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
