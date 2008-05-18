package events.implementations;

import core.VSProcess;
import events.*;
import protocols.VSProtocol;

public class ProtocolEvent extends VSEvent {
    private String protocolClassname;
    private boolean isClientProtocol; /* true = client, false = server */
    private boolean isProtocolActivation; /* true = activate, false = deactivate */

    protected void onInit() {
        setClassname(getClass().toString());
    }

    public void isClientProtocol(boolean isClientProtocol) {
        this.isClientProtocol = isClientProtocol;
    }

    public boolean isClientProtocol() {
        return isClientProtocol;
    }

    public void isProtocolActivation(boolean isProtocolActivation) {
        this.isProtocolActivation = isProtocolActivation;
    }

    public boolean isProtocolActivation() {
        return isProtocolActivation;
    }

    public void setProtocolClassname(String protocolClassname) {
        this.protocolClassname = protocolClassname;
    }

    public void onStart() {
        //String type = isClientProtocol ? " Client!" : " Server!";
        //process.setBoolean(protocolClassname + type, isProtocolActivation);

        VSProtocol protocol;

        if (!process.objectExists(protocolClassname)) {
            protocol = (VSProtocol) VSRegisteredEvents.createEventInstanceByClassname(protocolClassname, process);
            process.setObject(protocolClassname, protocol);

        } else {
            protocol = (VSProtocol) process.getObject(protocolClassname);
        }

        if (isClientProtocol)
            protocol.isClient(isProtocolActivation);

        else
            protocol.isServer(isProtocolActivation);

        StringBuffer buffer = new StringBuffer();
        buffer.append(VSRegisteredEvents.getShortname(protocolClassname));
        buffer.append(" ");
        buffer.append(isClientProtocol
                      ? prefs.getString("lang.client") : prefs.getString("lang.server"));
        buffer.append(" ");
        buffer.append(isProtocolActivation
                      ? prefs.getString("lang.activated") : prefs.getString("lang.deactivated"));
        logg(buffer.toString());
    }
}
