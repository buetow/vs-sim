/*
 * Copyright (c) 2008 Paul C. Buetow, vs-sim@dev.buetow.org
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

package prefs;

import java.awt.Color;
import java.awt.event.KeyEvent;

/**
 * The class VSDefaultPrefs, makes sure that the simulator has its default
 * configuration values.
 *
 * @author Paul C. Buetow
 */
public class VSDefaultPrefs extends VSSerializablePrefs {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Inits a prefs object with default values.
     *
     * @return the lang.process.removeprefs
     */
    public static VSPrefs init() {
        VSDefaultPrefs prefs = new VSDefaultPrefs();
        prefs.fillWithDefaults();
        return prefs;
    }

    /**
     * Fill everything with ts defaults.
     */
    public void fillWithDefaults() {
        super.clear();
        addWithDefaults();
    }

    /**
     * Adds default values if not existent.
     */
    public void addWithDefaults() {
        fillDefaultBooleans();
        fillDefaultColors();
        fillDefaultFloats();
        fillDefaultIntegers();
        fillDefaultLongs();
        fillDefaultStrings();
    }

    /**
     * Fill with default strings.
     */
    public void fillDefaultStrings() {
        initString("lang.en.about", "About");
        initString("lang.en.about.info", "This program used to be the diploma thesis of Paul C. Buetow. Please contact vs-sim@dev.buetow.org, if you find any errors!");
        initString("lang.en.activate", "activate");
        initString("lang.en.activated", "activated");
        initString("lang.en.actualize", "Activation");
        initString("lang.en.all", "All");
        initString("lang.en.antialiasing", "Anti-Aliasing");
        initString("lang.en.cancel", "Abort");
        initString("lang.en.client", "Client");
        initString("lang.en.clientrequest.start", "Start client request");
        initString("lang.en.close", "Close");
        initString("lang.en.colorchooser", "Color chooser");
        initString("lang.en.colorchooser2", "Please select color");
        initString("lang.en.copy", "Copy");
        initString("lang.en.crashed", "Crashed");
        initString("lang.en.dat", "Simulation (.dat)");
        initString("lang.en.deactivate", "deactivate");
        initString("lang.en.deactivated", "deactivated");
        initString("lang.en.default", "Defaults");
        initString("lang.en.edit", "Edit");
        initString("lang.en.editor", "Editor");
        initString("lang.en.event", "Event");
        initString("lang.en.event.add.global", "Insert global event");
        initString("lang.en.event.add.local", "Insert local event");
        initString("lang.en.event.add.time", "at");
        initString("lang.en.events", "Events");
        initString("lang.en.events.process", "Process events");
        initString("lang.en.file", "File");
        initString("lang.en.filter", "Filter");
        initString("lang.en.loging.active", "Logging");
        initString("lang.en.loging.clear", "Delete logs");
        initString("lang.en.message", "Message");
        initString("lang.en.message.recv", "Message received");
        initString("lang.en.message.sent", "Message sent");
        initString("lang.en.mode.expert", "Expert mode");
        initString("lang.en.name", "VS-Simulator 1.1");
        initString("lang.en.ok", "OK");
        initString("lang.en.open", "Open");
        initString("lang.en.pause", "Paus");
        initString("lang.en.prefs", "Preferences");
        initString("lang.en.prefs.color", "Color preferences");
        initString("lang.en.prefs.diverse", "Diverse preferences");
        initString("lang.en.prefs.ext", "Extended preferences");
        initString("lang.en.prefs.message", "Message preferences");
        initString("lang.en.prefs.message.defaults", "Message prefs. for new processes");
        initString("lang.en.prefs.more", "More preferences");
        initString("lang.en.prefs.process", "Process preferences");
        initString("lang.en.prefs.process", "Standard process preferences");
        initString("lang.en.prefs.process.defaults", "Preferences for new processes");
        initString("lang.en.prefs.process.ext", "Extended process preferences");
        initString("lang.en.prefs.protocols", "Protocol preferences");
        initString("lang.en.prefs.simulator", "Simulator preferences");
        initString("lang.en.process", "Process");
        initString("lang.en.process.add.new", "Insert new process");
        initString("lang.en.process.crash", "Crash process");
        initString("lang.en.process.edit", "Edit process");
        initString("lang.en.process.id", "PID");
        initString("lang.en.process.new", "New process");
        initString("lang.en.process.not.selected", "No process selected");
        initString("lang.en.process.recover", "Recover process");
        initString("lang.en.process.remove", "Remove process");
        initString("lang.en.process.selected", "Selected process");
        initString("lang.en.process.time.local", "Local time");
        initString("lang.en.processes.all", "All processes");
        initString("lang.en.protocol", "Protocol");
        initString("lang.en.protocol.client", "Client side");
        initString("lang.en.protocol.editor", "Protocol editor");
        initString("lang.en.protocol.server", "Server side");
        initString("lang.en.protocol.tasks.activation", "Client-/Server protocol activation");
        initString("lang.en.protocol.tasks.client", "Client Task-Manager (Client request)");
        initString("lang.en.protocols", "Protocols");
        initString("lang.en.quit", "Quit");
        initString("lang.en.recovered", "Recovered");
        initString("lang.en.remove", "Remove");
        initString("lang.en.replay", "Repeat");
        initString("lang.en.reset", "Reset");
        initString("lang.en.save", "Save");
        initString("lang.en.saveas", "Save as");
        initString("lang.en.server", "Server");
        initString("lang.en.serverrequest.start", "Start server request");
        initString("lang.en.simulator", "Simulator");
        initString("lang.en.simulator.close", "Close simulation");
        initString("lang.en.simulator.finished", "Simulation closed");
        initString("lang.en.simulator.new", "New simulation");
        initString("lang.en.simulator.paused", "Simulation paused");
        initString("lang.en.simulator.resetted", "Simulation resetted");
        initString("lang.en.simulator.started", "Simulation started");
        initString("lang.en.start", "Start");
        initString("lang.en.stop", "Stop");
        initString("lang.en.takeover", "Take over");
        initString("lang.en.task", "Task");
        initString("lang.en.task.manager", "Event editor");
        initString("lang.en.tasks.fullfilled", "Fullfilled tasks");
        initString("lang.en.tasks.global", "GLobal tasks");
        initString("lang.en.tasks.local", "Local tasks");
        initString("lang.en.time", "Time");
        initString("lang.en.time.lamport", "Lamport time");
        initString("lang.en.time.vector", "Vector time");
        initString("lang.en.timed.global", "Global events");
        initString("lang.en.timed.local", "Local events");
        initString("lang.en.type", "Type");
        initString("lang.en.value", "Value");
        initString("lang.en.variable", "Variable");
        initString("lang.en.variables", "Variables");
        initString("lang.en.variables.global", "Global variables");
        initString("lang.en.window.close", "Close window");
        initString("lang.en.window.new", "New window");

        /* Protocol names */
        initString("lang.en.events.implementations.VSProcessCrashEvent", "Process Crash Event");
        initString("lang.en.events.implementations.VSProcessCrashEvent.short", "Process Crash");
        initString("lang.en.events.implementations.VSProcessRecoverEvent", "Process Recover Event");
        initString("lang.en.events.implementations.VSProcessRecoverEvent.short", "Process Recover");
        initString("lang.en.protocols.implementations.VSBasicMulticastProtocol", "Basic Multicast Protocol");
        initString("lang.en.protocols.implementations.VSBasicMulticastProtocol.short", "Basic Multicast");
        initString("lang.en.protocols.implementations.VSBerkelyTimeProtocol", "Berkley algorithm for internal sync.");
        initString("lang.en.protocols.implementations.VSBerkelyTimeProtocol.short", "Berkley Protocol");
        initString("lang.en.protocols.implementations.VSBroadcastProtocol", "Broadcast Protocol");
        initString("lang.en.protocols.implementations.VSBroadcastProtocol.short", "Broadcast");
        initString("lang.en.protocols.implementations.VSDummyProtocol", "Example/Dummy Protocol");
        initString("lang.en.protocols.implementations.VSDummyProtocol.short", "Example/Dummy");
        initString("lang.en.protocols.implementations.VSExternalTimeSyncProtocol", "Christians method for external sync.");
        initString("lang.en.protocols.implementations.VSExternalTimeSyncProtocol.short", "Christians");
        initString("lang.en.protocols.implementations.VSInternalTimeSyncProtocol", "Internal Synchronization Protocol");
        initString("lang.en.protocols.implementations.VSInternalTimeSyncProtocol.short", "Internal sync.");
        initString("lang.en.protocols.implementations.VSOnePhaseCommitProtocol", "One-Phase Commit Protocol");
        initString("lang.en.protocols.implementations.VSOnePhaseCommitProtocol.short", "1-Phase Commit");
        initString("lang.en.protocols.implementations.VSPingPongProtocol", "Ping-Pong Protocol");
        initString("lang.en.protocols.implementations.VSPingPongProtocol.short", "Ping-Pong");
        initString("lang.en.protocols.implementations.VSReliableMulticastProtocol", "Reliable Multicast Protocol");
        initString("lang.en.protocols.implementations.VSReliableMulticastProtocol.short", "Reliable Multicast");
        initString("lang.en.protocols.implementations.VSTwoPhaseCommitProtocol", "Two-Phase Commit");
        initString("lang.en.protocols.implementations.VSTwoPhaseCommitProtocol.short", "2-Phase Commit");
    }

    /**
     * Fill with default integers.
     */
    public void fillDefaultIntegers() {
        /* Simulator prefs */
        initInteger("sim.process.num", 3, "Number of processes", 1, 6);
        initInteger("message.prob.outage", 0, "Message lost prob.", 0, 100, "%");
        initInteger("process.prob.crash", 0, "Process crash prob.", 0, 100, "%");
        initInteger("sim.seconds", 15, "Simulation duration", 5, 120, "s");

        /* Internal prefs */
        initInteger("keyevent.about", KeyEvent.VK_A, null, 0, 100);
        initInteger("keyevent.cancel", KeyEvent.VK_A, null, 0, 100);
        initInteger("keyevent.close", KeyEvent.VK_C, null, 0, 100);
        initInteger("keyevent.default", KeyEvent.VK_F, null, 0, 100);
        initInteger("keyevent.edit", KeyEvent.VK_E, null, 0, 100);
        initInteger("keyevent.file", KeyEvent.VK_D, null, 0, 100);
        initInteger("keyevent.new", KeyEvent.VK_N, null, 0, 100);
        initInteger("keyevent.actualize", KeyEvent.VK_A, null, 0, 100);
        initInteger("keyevent.takeover", KeyEvent.VK_B, null, 0, 100);
        initInteger("keyevent.ok", KeyEvent.VK_O, null, 0, 100);
        initInteger("keyevent.open", KeyEvent.VK_O, null, 0, 100);
        initInteger("keyevent.pause", KeyEvent.VK_P, null, 0, 100);
        initInteger("keyevent.prefs", KeyEvent.VK_P, null, 0, 100);
        initInteger("keyevent.prefs.ext", KeyEvent.VK_E, null, 0, 100);
        initInteger("keyevent.quit", KeyEvent.VK_B, null, 0, 100);
        initInteger("keyevent.replay", KeyEvent.VK_W, null, 0, 100);
        initInteger("keyevent.reset", KeyEvent.VK_R, null, 0, 100);
        initInteger("keyevent.save", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.saveas", KeyEvent.VK_V, null, 0, 100);
        initInteger("keyevent.simulator", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.start", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.stop", KeyEvent.VK_P, null, 0, 100);

        initInteger("div.window.prefs.xsize", 400, "Configuration window X-Axis", 550, 3200, "px");
        initInteger("div.window.prefs.ysize", 400, "Configuration window Y-Axis", 640, 2400, "px");
        initInteger("div.window.logsize", 300, "Log window Y-Axis", 100, 1000, "px");
        initInteger("div.window.splitsize", 320, "Toolbar X-Axis", 100, 1000, "px");
        initInteger("div.window.xsize", 1024, "Main window X-Axis", 750, 3200, "px");
        initInteger("div.window.ysize", 768, "Main window Y-Axis", 600, 2400, "px");
    }

    /**
     * Fill with default floats.
     */
    public void fillDefaultFloats() {
        /* Simulator prefs */
        initFloat("process.clock.variance", 0, "Clock variance");
        initFloat("sim.clock.speed", 0.5f, "Simulation play speed");
    }

    /**
     * Fill default longs.
     */
    public void fillDefaultLongs() {
        /* Simulator prefs */
        initLong("message.sendingtime.min", 500, "Max transmission time", "ms");
        initLong("message.sendingtime.max", 2000, "Min transmission time", "ms");
    }

    /**
     * Fill with default colors.
     */
    public void fillDefaultColors() {
        /* Internal prefs */
        initColor("col.background", new Color(0xFF, 0xFF, 0xFF));
        initColor("col.process.default", new Color(0x00, 0x00, 0x00));
        initColor("col.process.running", new Color(0x0D, 0xD8, 0x09));
        initColor("col.process.crashed", new Color(0xff, 0x00, 0x00));
        initColor("col.process.highlight", new Color(0xff, 0xA5, 0x00));
        initColor("col.process.line", new Color(0x00, 0x00, 0x00));
        initColor("col.process.secondline", new Color(0xAA, 0xAA, 0xAA));
        initColor("col.process.sepline", new Color(0xff, 0x00, 0x00));
        initColor("col.process.stopped", new Color(0x00, 0x00, 0x00));
        initColor("col.message.arrived", new Color(0x00, 0x85, 0xD2));
        initColor("col.message.sending", new Color(0x0D, 0xD8, 0x09));
        initColor("col.message.lost", new Color(0xFF, 0x00, 0x00));
    }

    /**
     * Fill with default booleans.
     */
    public void fillDefaultBooleans() {
        initBoolean("sim.mode.expert", false, "Expert mode");
        initBoolean("sim.message.own.recv", false, "Processes receive own messages");
        initBoolean("sim.message.prob.mean", true, "Use mean value of message lost prob.");
        initBoolean("sim.message.sendingtime.mean", true, "Use mean value of transmission times");
        initBoolean("sim.messages.relevant", true, "Only show relevant messages");
        initBoolean("sim.periodic", false, "Repeat simulation periodically");
        initBoolean("sim.update.lamporttime.all", false, "Lamport timestamps affect all events");
        initBoolean("sim.update.vectortime.all", false, "Vector timestamps affect all events");
    }
}
