/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.internal;

import core.VSMessage;
import events.VSEvent;
import protocols.VSProtocol;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReceiveEvent.
 */
public class MessageReceiveEvent extends VSEvent {

    /** The message. */
    private VSMessage message;

    /**
     * Instantiates a new message receive event.
     *
     * @param message the message
     */
    public MessageReceiveEvent(VSMessage message) {
        this.message = message;
    }

    /* (non-Javadoc)
     * @see events.VSEvent#onInit()
     */
    protected void onInit() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSEvent#onStart()
     */
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
