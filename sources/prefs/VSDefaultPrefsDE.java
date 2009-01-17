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

package prefs;

import java.awt.Color;
import java.awt.event.KeyEvent;

/**
 * The class VSDefaultPrefsDE, makes sure that the simulator has its default
 * configuration values.
 *
 * @author Paul C. Buetow
 */
public class VSDefaultPrefsDE extends VSSerializablePrefs {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Inits a prefs object with default values.
     *
     * @return the lang.process.removeprefs
     */
    public static VSPrefs init() {
        VSDefaultPrefsDE prefs = new VSDefaultPrefsDE();
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
        initString("lang.de.about", "About");
        initString("lang.de.about.info", "Dieses Programm wurde von Paul Bütow im Rahmen der Diplomarbeit \"Objektorientierte Entwicklung eines GUI-basierten Tools für die ereignisbasierte Simulator verteilter Systeme\" bei Prof. Dr.-Ing. Oßmann als 1. Prüfer sowie Betreuer und Prof. Dr. rer. nat. Fassbender als 2. Prüfer erstellt. Bei Fehlern bitte eine kurze Mail mit Fehlerbeschreibung an vs@paul.buetow.org schicken!");
        initString("lang.de.activate", "aktivieren");
        initString("lang.de.activated", "aktiviert");
        initString("lang.de.actualize", "Aktualisieren");
        initString("lang.de.all", "Alle");
        initString("lang.de.antialiasing", "Anti-Aliasing");
        initString("lang.de.cancel", "Abbrechen");
        initString("lang.de.client", "Client");
        initString("lang.de.clientrequest.start", "Clientanfrage starten");
        initString("lang.de.close", "Schliessen");
        initString("lang.de.colorchooser", "Farbauswahl");
        initString("lang.de.colorchooser2", "Bitte Farbe auswählen");
        initString("lang.de.copy", "Kopieren");
        initString("lang.de.crashed", "Abgestürzt");
        initString("lang.de.dat", "Simulation (.dat)");
        initString("lang.de.deactivate", "deaktivieren");
        initString("lang.de.deactivated", "deaktiviert");
        initString("lang.de.default", "Defaults");
        initString("lang.de.edit", "Editieren");
        initString("lang.de.editor", "Editor");
        initString("lang.de.event", "Ereignis");
        initString("lang.de.event.add.global", "Globales Ereignis einfügen");
        initString("lang.de.event.add.local", "Lokales Ereignis einfügen");
        initString("lang.de.event.add.time", "bei");
        initString("lang.de.events", "Ereignisse");
        initString("lang.de.events.process", "Prozessereignisse");
        initString("lang.de.file", "Datei");
        initString("lang.de.filter", "Filter");
        initString("lang.de.loging.active", "Logging");
        initString("lang.de.loging.clear", "Loggs löschen");
        initString("lang.de.message", "Nachricht");
        initString("lang.de.message.recv", "Nachricht erhalten");
        initString("lang.de.message.sent", "Nachricht versendet");
        initString("lang.de.mode.expert", "Expertenmodus");
        initString("lang.de.name", "VS-Simulator 1.0");
        initString("lang.de.ok", "OK");
        initString("lang.de.open", "Öffnen");
        initString("lang.de.pause", "Pausieren");
        initString("lang.de.prefs", "Einstellungen");
        initString("lang.de.prefs.color", "Farbeinstellungen");
        initString("lang.de.prefs.diverse", "Diverse Einstellungen");
        initString("lang.de.prefs.ext", "Erweiterte Einstellungen");
        initString("lang.de.prefs.message", "Nachrichteneinstellungen");
        initString("lang.de.prefs.message.defaults", "Nachrichteneinstellungen für neue Prozesse");
        initString("lang.de.prefs.more", "Mehr Einstellungen");
        initString("lang.de.prefs.process", "Prozesseinstellungen");
        initString("lang.de.prefs.process", "Prozessstandardeinstellungen");
        initString("lang.de.prefs.process.defaults", "Einstellungen für neue Prozesse");
        initString("lang.de.prefs.process.ext", "Erweiterte Prozesseinstellungen");
        initString("lang.de.prefs.protocols", "Protokolleinstellungen");
        initString("lang.de.prefs.simulator", "Simulationseinstellungen");
        initString("lang.de.process", "Prozess");
        initString("lang.de.process.add.new", "Neuen Prozess hinzufügen");
        initString("lang.de.process.crash", "Prozess abstürzen");
        initString("lang.de.process.edit", "Prozess editieren");
        initString("lang.de.process.id", "PID");
        initString("lang.de.process.new", "Neuer Prozess");
        initString("lang.de.process.not.selected", "Kein Prozess ausgewählt");
        initString("lang.de.process.recover", "Prozess wiederbeleben");
        initString("lang.de.process.remove", "Prozess entfernen");
        initString("lang.de.process.selected", "Aktuell ausgewählter Prozess");
        initString("lang.de.process.time.local", "Lokale Zeit");
        initString("lang.de.processes.all", "Alle Prozesse");
        initString("lang.de.protocol", "Protokoll");
        initString("lang.de.protocol.client", "Clientseite");
        initString("lang.de.protocol.editor", "Protokolleditor");
        initString("lang.de.protocol.server", "Serverseite");
        initString("lang.de.protocol.tasks.activation", "Client-/Serverprotokoll Aktivierung");
        initString("lang.de.protocol.tasks.client", "Client Task-Manager (Clientanfragen)");
        initString("lang.de.protocols", "Protokolle");
        initString("lang.de.quit", "Beenden");
        initString("lang.de.recovered", "Wiederbelebt");
        initString("lang.de.remove", "Entfernen");
        initString("lang.de.replay", "Wiederholen");
        initString("lang.de.reset", "Reset");
        initString("lang.de.save", "Speichern");
        initString("lang.de.saveas", "Speichern unter");
        initString("lang.de.server", "Server");
        initString("lang.de.serverrequest.start", "Serveranfrage starten");
        initString("lang.de.simulator", "Simulator");
        initString("lang.de.simulator.close", "Simulation schliessen");
        initString("lang.de.simulator.finished", "Simulation beendet");
        initString("lang.de.simulator.new", "Neue Simulation");
        initString("lang.de.simulator.paused", "Simulation pausiert");
        initString("lang.de.simulator.resetted", "Simulation zurückgesetzt");
        initString("lang.de.simulator.started", "Simulation gestartet");
        initString("lang.de.start", "Starten");
        initString("lang.de.stop", "Stoppen");
        initString("lang.de.takeover", "Übernehmen");
        initString("lang.de.task", "Aufgabe");
        initString("lang.de.task.manager", "Ereigniseditor");
        initString("lang.de.tasks.fullfilled", "Abgelaufene Aufgaben");
        initString("lang.de.tasks.global", "Globale Aufgaben");
        initString("lang.de.tasks.local", "Lokale Aufgaben");
        initString("lang.de.time", "Zeit");
        initString("lang.de.time.lamport", "Lamportzeit");
        initString("lang.de.time.vector", "Vektorzeit");
        initString("lang.de.timed.global", "Globale Ereignisse");
        initString("lang.de.timed.local", "Lokale Ereignisse");
        initString("lang.de.type", "Typ");
        initString("lang.de.value", "Wert");
        initString("lang.de.variable", "Variable");
        initString("lang.de.variables", "Variablen");
        initString("lang.de.variables.global", "Globale Variablen");
        initString("lang.de.window.close", "Fenster schliessen");
        initString("lang.de.window.new", "Neues Fenster");
    }

    /**
     * Fill with default integers.
     */
    public void fillDefaultIntegers() {
        /* Simulator prefs */
        initInteger("sim.process.num", 3, "Anzahl der Prozesse", 1, 6);
        initInteger("message.prob.outage", 0, "Nachrichtenverlustw'keit", 0, 100, "%");
        initInteger("process.prob.crash", 0, "Prozessausfallw'keit", 0, 100, "%");
        initInteger("sim.seconds", 15, "Dauer der Simulation", 5, 120, "s");

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

        initInteger("div.window.prefs.xsize", 400, "Einstellungsfenster X-Achse", 550, 3200, "px");
        initInteger("div.window.prefs.ysize", 400, "Einstellungsfenster Y-Achse", 640, 2400, "px");
        initInteger("div.window.logsize", 300, "Loggfenster Y-Achse", 100, 1000, "px");
        initInteger("div.window.splitsize", 320, "Toolbar X-Achse", 100, 1000, "px");
        initInteger("div.window.xsize", 1024, "Hauptfenster X-Achse", 750, 3200, "px");
        initInteger("div.window.ysize", 768, "Hauptfenster Y-Achse", 600, 2400, "px");
    }

    /**
     * Fill with default floats.
     */
    public void fillDefaultFloats() {
        /* Simulator prefs */
        initFloat("process.clock.variance", 0, "Uhrabweichung");
        initFloat("sim.clock.speed", 0.5f, "Abspielgeschwindigkeit der Simulation");
    }

    /**
     * Fill default longs.
     */
    public void fillDefaultLongs() {
        /* Simulator prefs */
        initLong("message.sendingtime.min", 500, "Minimale Übertragungszeit", "ms");
        initLong("message.sendingtime.max", 2000, "Maximale Übertragungszeit", "ms");
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
        initBoolean("sim.mode.expert", false, "Expertenmodus aktivieren");
        initBoolean("sim.message.own.recv", false, "Prozesse empfangen eigene Nachrichten");
        initBoolean("sim.message.prob.mean", true, "Mittelwerte der Nachrichtverlustw'k. bilden");
        initBoolean("sim.message.sendingtime.mean", true, "Mittelwerte der Übertragungszeiten bilden");
        initBoolean("sim.messages.relevant", true, "Nur relevante Nachrichten anzeigen");
        initBoolean("sim.periodic", false, "Simulation periodisch wiederholen");
        initBoolean("sim.update.lamporttime.all", false, "Lamportzeiten betreffen alle Ereignisse");
        initBoolean("sim.update.vectortime.all", false, "Vektorzeiten betreffen alle Ereignisse");
    }
}
