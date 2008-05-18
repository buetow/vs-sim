package events.implementations;

import core.VSProcess;
import events.*;

public class ProtocolEvent extends VSEvent {
    private String eventClassname;
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

    public void setEventClassname(String eventClassname) {
        this.eventClassname = eventClassname;
    }

    public void onStart() {
        String type = isClientProtocol ? "client" : "server";
        String name = VSRegisteredEvents.getName(eventClassname);
        process.setBoolean("sim."+name.toLowerCase()+"."+type+".enabled!", isProtocolActivation);

        StringBuffer buffer = new StringBuffer();
        buffer.append(VSRegisteredEvents.getShortname(eventClassname));
        buffer.append(" ");
        buffer.append(isClientProtocol
                      ? prefs.getString("lang.client") : prefs.getString("lang.server"));
        buffer.append(" ");
        buffer.append(isProtocolActivation
                      ? prefs.getString("lang.activated") : prefs.getString("lang.deactivated"));

        logg(buffer.toString());
    }
}