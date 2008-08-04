/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 *
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package events;

import java.util.*;

import prefs.*;
import core.*;
import utils.*;

/**
 * The class VSRegisteredEvents. This class is responsible to manage all
 * events. It manages the event classnames, the event shortnames and the event
 * names. It also checks if a protocol (which is an event as well) has
 * variables which are editable through the GUI of the simulator.
 *
 * @author Paul C. Buetow
 */
public final class VSRegisteredEvents {
    /** The serial version uid */
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

        registerEvent("events.implementations.VSProcessCrashEvent",
                      "Prozessabsturz", null);
        registerEvent("events.implementations.VSProcessRecoverEvent",
                      "Prozesswiederbelebung", null);
        registerEvent("protocols.implementations.VSBasicMulticastProtocol",
                      "Basic Multicast", "Basic Multicast");
        registerEvent("protocols.implementations.VSBerkelyTimeProtocol",
                      "Berkeley Algorithmus zur internen Sync.", "Berkeley");
        registerEvent("protocols.implementations.VSBroadcastProtocol",
                      "Broadcast", null);
        registerEvent("protocols.implementations.VSDummyProtocol",
                      "Beispiel/Dummy", null);
        registerEvent("protocols.implementations.VSExternalTimeSyncProtocol",
                      "Christians Methode zur externen Sync.", "Christians");
        registerEvent("protocols.implementations.VSInternalTimeSyncProtocol",
                      "Interne Synchronisation", "Interne Sync.");
        registerEvent("protocols.implementations.VSOnePhaseCommitProtocol",
                      "Ein-Phasen Commit", "1-Phasen Commit");
        registerEvent("protocols.implementations.VSPingPongProtocol",
                      "Ping Pong", null);
        registerEvent("protocols.implementations.VSReliableMulticastProtocol",
                      "Reliable Multicast", "Reliable Multicast");
        registerEvent("protocols.implementations.VSTwoPhaseCommitProtocol",
                      "Zwei-Phasen Commit", "2-Phasen Commit");

        /* Make dummy objects of each protocol, to see if they contain VSPrefs
           values to edit */
        Vector<String> protocolClassnames = getProtocolClassnames();
        VSClassLoader classLoader = new VSClassLoader();

        for (String protocolClassname : protocolClassnames) {
            Object serverObject = classLoader.newInstance(protocolClassname);
            Object clientObject = classLoader.newInstance(protocolClassname);

            if (clientObject instanceof protocols.VSAbstractProtocol &&
                    serverObject instanceof protocols.VSAbstractProtocol) {

                protocols.VSAbstractProtocol serverProtocol =
                    (protocols.VSAbstractProtocol) serverObject;
                protocols.VSAbstractProtocol clientProtocol =
                    (protocols.VSAbstractProtocol) clientObject;

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
                    isOnServerStartProtocol.put(protocolClassname,
                                                new Boolean(true));
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
     * Gets the protocols server variable names.
     *
     * @return The variable names
     */
    public static ArrayList<String> getProtocolServerVariables(
        String protocolClassname) {
        return serverVariables.get(protocolClassname);
    }

    /**
     * Gets the protocols server variable names.
     *
     * @return The variable names
     */
    public static ArrayList<String> getProtocolClientVariables(
        String protocolClassname) {
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
            if (getClassnameByEventname(eventName).startsWith(
                        "protocols.implementations"))
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
            if (getClassnameByEventname(eventName).startsWith(
                        "events.implementations"))
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
        if (isOnServerStartProtocol.containsKey(protocolClassname)) {
            Boolean bool = isOnServerStartProtocol.get(protocolClassname);
            return bool.booleanValue();
        }

        return false;
    }

    /**
     * Creates the event instance by classname.
     *
     * @param eventClassname the event classname
     * @param process the process
     *
     * @return An instance of the event classname, if exists. Else null.
     */
    public static VSAbstractEvent createEventInstanceByClassname(
        String eventClassname, VSInternalProcess process) {
        Object protocolObj = new VSClassLoader().newInstance(eventClassname);

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
     * @return An instance of the event, if exists. Else null.
     */
    public static VSAbstractEvent createEventInstanceByName(String eventName,
            VSInternalProcess process) {
        return createEventInstanceByClassname(
                   eventClassnamesByNames.get(eventName), process);
    }

    /**
     * Registers an event.
     *
     * @param eventClassname the event classname
     * @param eventName the event name
     * @param eventShortname the event shortname
     */
    private static void registerEvent(String eventClassname, String eventName,
                                      String eventShortname) {
        if (eventShortname == null)
            eventShortname = eventName;

        eventNamesByClassnames.put(eventClassname, eventName);
        eventShortnamesByClassnames.put(eventClassname, eventShortname);
        eventClassnamesByNames.put(eventName, eventClassname);
        eventClassnamesByShortnames.put(eventShortname, eventClassname);
    }
}
