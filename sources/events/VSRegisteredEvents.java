/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package events;

import java.util.*;

import prefs.*;
import core.*;
import utils.*;

/**
 * The Class VSRegisteredEvents.
 */
public final class VSRegisteredEvents {
    private static final long serialVersionUID = 1L;

    /** The event classnames. */
    private static HashMap<String,String> eventClassnames =
        new HashMap<String,String>();

    /** The event shortnames. */
    private static HashMap<String,String> eventShortnames =
        new HashMap<String,String>();

    /** The event names. */
    private static HashMap<String,String> eventNames =
        new HashMap<String,String>();

    /** The editable protocols classnames. */
    private static ArrayList<String> editableProtocolsClassnames =
        new ArrayList<String>();

    private static HashMap<String,ArrayList<String>> clientVariables =
        new HashMap<String,ArrayList<String>>();

    private static HashMap<String,ArrayList<String>> serverVariables =
        new HashMap<String,ArrayList<String>>();

    /** The prefs. */
    private static VSPrefs prefs;

    /**
     * Inits the.
     *
     * @param prefs_ the prefs_
     */
    public static void init(VSPrefs prefs_) {
        prefs = prefs_;

        registerEvent("events.implementations.ProcessCrashEvent", "Prozessabsturz", null);
        registerEvent("events.implementations.ProcessRecoverEvent", "Prozesswiederbelebung", null);
        registerEvent("protocols.implementations.BerkelyTimeProtocol", "Berkeley Algorithmus zur internen Sync.", "Berkeley");
        registerEvent("protocols.implementations.BroadcastSturmProtocol", "Broadcaststurm", null);
        registerEvent("protocols.implementations.DummyProtocol", "Beispiel/Dummy", null);
        registerEvent("protocols.implementations.ExternalTimeSyncProtocol", "Christians Methode zur externen Sync.", "Christians");
        registerEvent("protocols.implementations.InternalTimeSyncProtocol", "Interne Synchronisation", "Interne Sync.");
        registerEvent("protocols.implementations.PingPongProtocol", "Ping Pong", null);
        registerEvent("protocols.implementations.OnePhaseCommitProtocol", "Ein-Phasen Commit", "1-Phasen Commit");
        registerEvent("protocols.implementations.TwoPhaseCommitProtocol", "Zwei-Phasen Commit", "2-Phasen Commit");

        /* Make dummy objects of each protocol, to see if they contain VSPrefs values to edit */
        Vector<String> protocolClassnames = getProtocolClassnames();
        VSClassLoader classLoader = new VSClassLoader();

        for (String protocolClassname : protocolClassnames) {
            Object serverObject = classLoader.newInstance(protocolClassname);
            Object clientObject = classLoader.newInstance(protocolClassname);

            if (clientObject instanceof protocols.VSAbstractProtocol &&
                    serverObject instanceof protocols.VSAbstractProtocol) {

                protocols.VSAbstractProtocol serverProtocol = (protocols.VSAbstractProtocol) serverObject;
                protocols.VSAbstractProtocol clientProtocol = (protocols.VSAbstractProtocol) clientObject;

                serverProtocol.onServerInit();
                clientProtocol.onClientInit();

                if (!serverProtocol.isEmpty() || !clientProtocol.isEmpty())
                    editableProtocolsClassnames.add(protocolClassname);

                if (!serverProtocol.isEmpty()) {
                    ArrayList<String> variables = new ArrayList<String>();
                    variables.addAll(serverProtocol.getAllFullKeys());
                    serverVariables.put(protocolClassname, variables);
                }

                if (!clientProtocol.isEmpty()) {
                    ArrayList<String> variables = new ArrayList<String>();
                    variables.addAll(clientProtocol.getAllFullKeys());
                    clientVariables.put(protocolClassname, variables);
                }
            }
        }
    }

    /**
     * Gets the editable protocols classnames.
     *
     * @return the editable protocols classnames
     */
    public static ArrayList<String> getEditableProtocolsClassnames() {
        return editableProtocolsClassnames;
    }

    /**
     * Gets the protocols server variable names
     *
     * @return The variable names
     */
    public static ArrayList<String> getProtocolServerVariables(String protocolClassname) {
        return serverVariables.get(protocolClassname);
    }

    /**
     * Gets the protocols server variable names
     *
     * @return The variable names
     */
    public static ArrayList<String> getProtocolClientVariables(String protocolClassname) {
        return clientVariables.get(protocolClassname);
    }

    /**
     * Gets the protocol names.
     *
     * @return the protocol names
     */
    public static Vector<String> getProtocolNames() {
        Set<String> set =  eventClassnames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventName : set)
            if (getClassname(eventName).startsWith("protocols.implementations"))
                vector.add(eventName);

        Collections.sort(vector);

        return vector;
    }

    /**
     * Gets the protocol classnames.
     *
     * @return the protocol classnames
     */
    public static Vector<String> getProtocolClassnames() {
        Set<String> set =  eventNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventClassname : set)
            if (eventClassname.startsWith("protocols.implementations"))
                vector.add(eventClassname);

        Collections.sort(vector);

        return vector;
    }

    /**
     * Gets the non protocol names.
     *
     * @return the non protocol names
     */
    public static Vector<String> getNonProtocolNames() {
        Set<String> set =  eventClassnames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventName : set)
            if (getClassname(eventName).startsWith("events.implementations"))
                vector.add(eventName);

        Collections.sort(vector);

        return vector;
    }

    /**
     * Gets the non protocol classnames.
     *
     * @return the non protocol classnames
     */
    public static Vector<String> getNonProtocolClassnames() {
        Set<String> set =  eventNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventClassname : set)
            if (eventClassname.startsWith("events.implementations"))
                vector.add(eventClassname);

        Collections.sort(vector);

        return vector;
    }

    /**
     * Gets the classname.
     *
     * @param eventName the event name
     *
     * @return the classname
     */
    public static String getClassname(String eventName) {
        return eventClassnames.get(eventName);
    }

    /**
     * Gets the name.
     *
     * @param eventClassname the event classname
     *
     * @return the name
     */
    public static String getName(String eventClassname) {
        return eventNames.get(eventClassname);
    }

    /**
     * Gets the shortname.
     *
     * @param eventClassname the event classname
     *
     * @return the shortname
     */
    public static String getShortname(String eventClassname) {
        return eventShortnames.get(eventClassname);
    }

    /**
     * Creates the event instance by classname.
     *
     * @param eventClassname the event classname
     * @param process the process
     *
     * @return the lang.process.removeevent
     */
    public static VSAbstractEvent createEventInstanceByClassname(String eventClassname, VSProcess process) {
        final Object protocolObj = new VSClassLoader().newInstance(eventClassname);

        if (protocolObj instanceof VSAbstractEvent) {
            VSAbstractEvent event = (VSAbstractEvent) protocolObj;
            event.init(process);
            return event;
        }

        return null;
    }

    /**
     * Creates the event instance by name.
     *
     * @param eventName the event name
     * @param process the process
     *
     * @return the lang.process.removeevent
     */
    public static VSAbstractEvent createEventInstanceByName(String eventName, VSProcess process) {
        return createEventInstanceByClassname(eventClassnames.get(eventName), process);
    }

    /**
     * Register event.
     *
     * @param eventClassname the event classname
     * @param eventName the event name
     * @param eventShortname the event shortname
     */
    private static void registerEvent(String eventClassname, String eventName, String eventShortname) {
        if (eventShortname == null)
            eventShortname = eventName;

        eventNames.put(eventClassname, eventName);
        eventShortnames.put(eventClassname, eventShortname);
        eventClassnames.put(eventName, eventClassname);
    }
}
