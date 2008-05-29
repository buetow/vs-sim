/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events.internal;

import core.VSMessage;
import events.VSAbstractEvent;
import protocols.VSAbstractProtocol;

/**
 * The Class VSMessageReceiveEvent. This event is used if a process receives
 * a message.
 *
 * @author Paul C. Buetow
 */
public class VSMessageReceiveEvent extends VSAbstractEvent {
    /** The serioal version uid */
    private static final long serialVersionUID = 1L;

    /** The message. */
    private VSMessage message;

    /**
     * Instantiates a new message receive event.
     *
     * @param message the message
     */
    public VSMessageReceiveEvent(VSMessage message) {
        this.message = message;
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public boolean onStart() {
        boolean returnValue = true;
        boolean onlyRelevantMessages = process.getPrefs().getBoolean(
                                           "sim.messages.relevant");

        String eventName = message.getName();
        String protocolClassname = message.getProtocolClassname();

        Object protocolObj = null;

        if (process.objectExists(protocolClassname))
            protocolObj = process.getObject(protocolClassname);

        if (onlyRelevantMessages) {
            if (protocolObj == null)
                return false;

            if (!((VSAbstractProtocol) protocolObj).isRelevantMessage(message))
                return false;
        }

        process.updateLamportTime(message.getLamportTime()+1);
        process.updateVectorTime(message.getVectorTime());

        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.message.recv"));
        buffer.append("; ");
        buffer.append(message);;
        logg(buffer.toString());

        if (protocolObj != null)
            ((VSAbstractProtocol) protocolObj).onMessageRecvStart(message);

        return true;
    }
}
