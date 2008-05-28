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

    /** The event classnames by eventnames. */
    private static HashMap<String,String> eventClassnamesByNames =
        new HashMap<String,String>();

    /** The event shortnames by classnames. */
    private static HashMap<String,String> eventShortnamesByClassnames =
        new HashMap<String,String>();

    /** The event names by classnames. */
    private static HashMap<String,String> eventNamesByClassnames =
        new HashMap<String,String>();

    /** The event classnames by shortnames. */
    private static HashMap<String,String> eventClassnamesByShortnames =
        new HashMap<String,String>();

    /** The editable protocols classnames. */
    private static ArrayList<String> editableProtocolsClassnames =
        new ArrayList<String>();

    private static HashMap<String,ArrayList<String>> clientVariables =
        new HashMap<String,ArrayList<String>>();

    private static HashMap<String,ArrayList<String>> serverVariables =
        new HashMap<String,ArrayList<String>>();

    private static HashMap<String,Boolean> isOnServerStartProtocol =
        new HashMap<String,Boolean>();

    /** The prefs. */
    private static VSPrefs prefs;

    /**
     * Inits the.
     *
     * @param prefs_ the prefs_
     */
    public static void init(VSPrefs prefs_) {
        prefs = prefs_;

        registerEvent("events.implementations.VSProcessCrashEvent", "Prozessabsturz", null);
        registerEvent("events.implementations.VSProcessRecoverEvent", "Prozesswiederbelebung", null);
        registerEvent("protocols.implementations.VSBerkelyTimeProtocol", "Berkeley Algorithmus zur internen Sync.", "Berkeley");
        registerEvent("protocols.implementations.VSBroadcastSturmProtocol", "Broadcaststurm", null);
        registerEvent("protocols.implementations.VSDummyProtocol", "Beispiel/Dummy", null);
        registerEvent("protocols.implementations.VSExternalTimeSyncProtocol", "Christians Methode zur externen Sync.", "Christians");
        registerEvent("protocols.implementations.VSInternalTimeSyncProtocol", "Interne Synchronisation", "Interne Sync.");
        registerEvent("protocols.implementations.VSPingPongProtocol", "Ping Pong", null);
        registerEvent("protocols.implementations.VSOnePhaseCommitProtocol", "Ein-Phasen Commit", "1-Phasen Commit");
        registerEvent("protocols.implementations.VSTwoPhaseCommitProtocol", "Zwei-Phasen Commit", "2-Phasen Commit");

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

                if (serverProtocol.hasOnServerStart())
                    isOnServerStartProtocol.put(protocolClassname, new Boolean(true));
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
        Set<String> set =  eventClassnamesByNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventName : set)
            if (getClassnameByEventname(eventName).startsWith("protocols.implementations"))
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
        ArrayList<String> shortnames = new ArrayList<String>();
        shortnames.addAll(eventClassnamesByShortnames.keySet());
        Collections.sort(shortnames);
        Vector<String> vector = new Vector<String>();

        for (String eventShortname : shortnames) {
            String eventClassname = getClassnameByShortname(eventShortname);
            if (eventClassname.startsWith("protocols.implementations"))
                vector.add(eventClassname);
        }

        return vector;
    }

    /**
     * Gets the non protocol names.
     *
     * @return the non protocol names
     */
    public static Vector<String> getNonProtocolNames() {
        Set<String> set =  eventClassnamesByNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventName : set)
            if (getClassnameByEventname(eventName).startsWith("events.implementations"))
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
        Set<String> set =  eventNamesByClassnames.keySet();
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
    public static String getClassnameByEventname(String eventName) {
        return eventClassnamesByNames.get(eventName);
    }

    /**
     * Gets the name.
     *
     * @param eventClassname the event classname
     *
     * @return the name
     */
    public static String getNameByClassname(String eventClassname) {
        return eventNamesByClassnames.get(eventClassname);
    }

    /**
     * Gets the shortname.
     *
     * @param eventClassname the event classname
     *
     * @return the shortname
     */
    public static String getShortnameByClassname(String eventClassname) {
        return eventShortnamesByClassnames.get(eventClassname);
    }

    /**
     * Gets the classname.
     *
     * @param eventShortname the event shortname
     *
     * @return the shortname
     */
    public static String getClassnameByShortname(String eventShortname) {
        return eventClassnamesByShortnames.get(eventShortname);
    }

    /**
     * Checks if the protocol uses onServerStart or onClientStart
     *
     * @param protocolClassname the protocol's classname
     *
     * @return true if onServerStart, false if onClientStart
     */
    public static boolean isOnServerStartProtocol(String protocolClassname) {
        if (isOnServerStartProtocol.containsKey(protocolClassname))
            return isOnServerStartProtocol.get(protocolClassname).booleanValue();

        return false;
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
        return createEventInstanceByClassname(eventClassnamesByNames.get(eventName), process);
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

        eventNamesByClassnames.put(eventClassname, eventName);
        eventShortnamesByClassnames.put(eventClassname, eventShortname);
        eventClassnamesByNames.put(eventName, eventClassname);
        eventClassnamesByShortnames.put(eventShortname, eventClassname);
    }
}
