package protocols;

import java.util.*;

import prefs.*;
import core.*;
import utils.*;

public final class RegisteredProtocols {
    private static HashMap<String,String> protocolClassnames;
    private static HashMap<String,String> protocolNames;
    private static VSPrefs prefs;

    public static void init(VSPrefs prefs_) {
        prefs = prefs_;
        protocolNames = new HashMap<String, String>();
        protocolClassnames = new HashMap<String, String>();

        registerProtocol("protocols.DummyProtocol");
        registerProtocol("protocols.PingPongProtocol");
        registerProtocol("protocols.ExternalTimeSyncProtocol");
        registerProtocol("protocols.InternalTimeSyncProtocol");
        registerProtocol("protocols.BroadcastSturmProtocol");
        registerProtocol("protocols.BerkelyTimeProtocol");
    }

    public static Vector<String> getProtocolNames() {
        Set<String> set =  protocolClassnames.keySet();
        Vector<String> vector = new Vector<String>();

        for (String protocolName : set)
            vector.add(protocolName);

        Collections.sort(vector);

        return vector;
    }

    public static String getProtocolClassname(String protocolName) {
        return protocolClassnames.get(protocolName);
    }

    public static String getProtocolName(String protocolClassname) {
        return protocolNames.get(protocolClassname);
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

    public static void registerProtocol(String protocolClassname) {
        int index = protocolClassname.lastIndexOf('.');

        if (index < 0) {
            protocolNames.put(protocolClassname, protocolClassname);
            protocolClassnames.put(protocolClassname, protocolClassname);
            return;
        }

        String protocolName = protocolClassname.substring(index + 1);
        index = protocolName.lastIndexOf("Protocol");

        if (index < 0 || index != protocolName.length() - 8) {
            protocolNames.put(protocolClassname, protocolName);
            protocolClassnames.put(protocolName, protocolClassname);
            return;
        }

        protocolName = protocolName.substring(0, index);
        protocolNames.put(protocolClassname, protocolName);
        protocolClassnames.put(protocolName, protocolClassname);
    }
}
