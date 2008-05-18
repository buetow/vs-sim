package events;

import java.util.*;

import prefs.*;
import core.*;
import utils.*;

public final class VSRegisteredEvents {
    private static HashMap<String,String> eventClassnames;
    private static HashMap<String,String> eventShortnames;
    private static HashMap<String,String> eventNames;
    private static VSPrefs prefs;

    public static void init(VSPrefs prefs_) {
        prefs = prefs_;
        eventNames = new HashMap<String, String>();
        eventShortnames = new HashMap<String, String>();
        eventClassnames = new HashMap<String, String>();

        registerEvent("events.implementations.ProcessCrashEvent", "Prozessabsturz", null);
        registerEvent("events.implementations.ProcessRecoverEvent", "Prozesswiederbelebung", null);
        registerEvent("protocols.implementations.BerkelyTimeProtocol", "Berkeley Algorithmus zur internen Sync.", "Berkeley");
        registerEvent("protocols.implementations.BroadcastSturmProtocol", "Broadcaststurm", null);
        registerEvent("protocols.implementations.DummyProtocol", "Beispiel/Dummy", null);
        registerEvent("protocols.implementations.ExternalTimeSyncProtocol", "Christians Methode zur externen Sync.", "Christians");
        registerEvent("protocols.implementations.InternalTimeSyncProtocol", "Interne Synchronisation", "Interne Sync.");
        registerEvent("protocols.implementations.PingPongProtocol", "Ping Pong", null);
    }

    public static Vector<String> getProtocolNames() {
        Set<String> set =  eventClassnames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventName : set)
            if (getClassname(eventName).startsWith("protocols"))
                vector.add(eventName);

        Collections.sort(vector);

        return vector;
    }

    public static Vector<String> getProtocolClassnames() {
        Set<String> set =  eventNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventClassname : set)
            if (eventClassname.startsWith("protocols"))
                vector.add(eventClassname);

        Collections.sort(vector);

        return vector;
    }

    public static Vector<String> getNonProtocolNames() {
        Set<String> set =  eventClassnames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventName : set)
            if (!getClassname(eventName).startsWith("protocols"))
                vector.add(eventName);

        Collections.sort(vector);

        return vector;
    }

    public static Vector<String> getNonProtocolClassnames() {
        Set<String> set =  eventNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String eventClassname : set)
            if (!eventClassname.startsWith("protocols"))
                vector.add(eventClassname);

        Collections.sort(vector);

        return vector;
    }

    public static String getClassname(String eventName) {
        return eventClassnames.get(eventName);
    }

    public static String getName(String eventClassname) {
        return eventNames.get(eventClassname);
    }

    public static String getShortname(String eventClassname) {
        return eventShortnames.get(eventClassname);
    }

    public static VSEvent createEventInstanceByClassname(String eventClassname, VSProcess process) {
        return createEventInstanceByName(getName(eventClassname), process);
    }

    public static VSEvent createEventInstanceByName(String eventName, VSProcess process) {
        final String eventClassname = eventClassnames.get(eventName);
        final Object protocolObj = new VSClassLoader().newInstance(eventClassname);

        if (protocolObj instanceof VSEvent) {
            VSEvent event = (VSEvent) protocolObj;
            event.init(process);
            return event;
        }

        return null;
    }

    private static void registerEvent(String eventClassname, String eventName, String eventShortname) {
        if (eventShortname == null)
            eventShortname = eventName;

        eventNames.put(eventClassname, eventName);
        eventShortnames.put(eventClassname, eventShortname);
        eventClassnames.put(eventName, eventClassname);
    }
}
