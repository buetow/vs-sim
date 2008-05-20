package events.implementations;

import core.VSMessage;
import core.VSProcess;
import events.VSEvent;
import protocols.VSProtocol;

public class MessageReceiveEvent extends VSEvent {
    private VSMessage message;

    public MessageReceiveEvent(VSMessage message) {
        this.message = message;
    }

    protected void onInit() {
        setClassname(getClass().toString());
    }

    public void onStart() {
        String eventName = message.getName();
        String protocolClassname = message.getProtocolClassname();

        process.updateLamportTime(message.getLamportTime()+1);
        process.updateVectorTime(message.getVectorTime());

        Object protocolObj = null;

        if (process.objectExists(protocolClassname))
            protocolObj = process.getObject(protocolClassname);

        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.message.recv"));
        buffer.append("; ");
        buffer.append(prefs.getString("lang.protocol"));
        buffer.append(": " );
        buffer.append(eventName);
        buffer.append("; ");
        buffer.append(prefs.getString("lang.message"));
        buffer.append(" ");
        buffer.append(message);;

        if (protocolObj == null) {
            logg(buffer.toString());

        } else {
            final VSProtocol protocol = (VSProtocol) protocolObj;
            logg(buffer.toString());
            protocol.onMessageRecv(message);
        }

    }
}
