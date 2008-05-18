package protocols;

import java.util.*;

import prefs.*;
import core.*;
import utils.*;

public final class VSRegisteredProtocols {
    private static HashMap<String,String> protocolClassnames;
    private static HashMap<String,String> protocolShortnames;
    private static HashMap<String,String> protocolNames;
    private static VSPrefs prefs;

    public static void init(VSPrefs prefs_) {
        prefs = prefs_;
        protocolNames = new HashMap<String, String>();
        protocolShortnames = new HashMap<String, String>();
        protocolClassnames = new HashMap<String, String>();

        registerProtocol("protocols.implementations.BerkelyTimeProtocol", "Berkeley Algorithmus zur internen Sync.", "Berkeley");
        registerProtocol("protocols.implementations.BroadcastSturmProtocol", "Broadcaststurm", null);
        registerProtocol("protocols.implementations.DummyProtocol", "Beispiel/Dummy", null);
        registerProtocol("protocols.implementations.ExternalTimeSyncProtocol", "Christians Methode zur externen Sync.", "Christians");
        registerProtocol("protocols.implementations.InternalTimeSyncProtocol", "Interne Synchronisation", "Interne Sync.");
        registerProtocol("protocols.implementations.PingPongProtocol", "Ping Pong", null);
    }

    public static Vector<String> getProtocolNames() {
        Set<String> set =  protocolClassnames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String protocolName : set)
            vector.add(protocolName);

        Collections.sort(vector);

        return vector;
    }

    public static Vector<String> getProtocolClassnames() {
        Set<String> set =  protocolNames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String protocolClassname : set)
            vector.add(protocolClassname);

        Collections.sort(vector);

        return vector;
    }

    public static String getProtocolClassname(String protocolName) {
        return protocolClassnames.get(protocolName);
    }

    public static String getProtocolName(String protocolClassname) {
        return protocolNames.get(protocolClassname);
    }

    public static String getProtocolShortname(String protocolClassname) {
        return protocolShortnames.get(protocolClassname);
    }

    public static VSProtocol getProtocolInstanceByName(String protocolName, VSProcess process) {
        final String protocolClassname = protocolClassnames.get(protocolName);
        final Object protocolObj = new VSClassLoader().newInstance(protocolClassname);

        if (protocolObj instanceof VSProtocol) {
            VSProtocol protocol = (VSProtocol) protocolObj;
            protocol.init(process);
            return protocol;
        }

        return null;
    }

    private static void registerProtocol(String protocolClassname, String protocolName, String protocolShortname) {
        if (protocolShortname == null)
            protocolShortname = protocolName;

        protocolNames.put(protocolClassname, protocolName);
        protocolShortnames.put(protocolClassname, protocolShortname);
        protocolClassnames.put(protocolName, protocolClassname);
    }
}
