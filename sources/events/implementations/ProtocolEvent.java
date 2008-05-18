package events.implementations;

import core.VSProcess;
import events.VSProcessEvent;
import protocols.VSRegisteredProtocols;

public class ProtocolEvent extends VSProcessEvent {
    private String protocolClassname;
    private boolean isClientProtocol; /* true = client, false = server */
    private boolean isProtocolActivation; /* true = activate, false = deactivate */

    public void setProtocolClassname(String protocolClassname) {
        this.protocolClassname = protocolClassname;
    }

    public String getProtocolClassname() {
        return protocolClassname;
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

    public void onStart() {
        String type = isClientProtocol ? "client" : "server";
        String name = VSRegisteredProtocols.getProtocolName(protocolClassname);
        process.setBoolean("sim."+name.toLowerCase()+"."+type+".enabled!", isProtocolActivation);

        StringBuffer buffer = new StringBuffer();
        buffer.append(VSRegisteredProtocols.getProtocolShortname(protocolClassname));
        buffer.append(" ");
        buffer.append(isClientProtocol
                      ? prefs.getString("lang.client") : prefs.getString("lang.server"));
        buffer.append(" ");
        buffer.append(isProtocolActivation
                      ? prefs.getString("lang.activated") : prefs.getString("lang.deactivated"));

        logg(buffer.toString());
    }
}
