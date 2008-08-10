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
 * The class VSDefaultPrefs, makes sure that the simulator has its default
 * configuration values. (Btw: This is the only class which is allowed to have
 * code lines which are longer than 80 chars!)
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
        initString("lang.about", "About");
        initString("lang.about.info", "Dieses Programm wurde von Paul Bütow im Rahmen der Diplomarbeit \"Objektorientierte Entwicklung eines GUI-basierten Tools für die ereignisbasierte Simulator verteilter Systeme\" bei Prof. Dr.-Ing. Oßmann als 1. Prüfer sowie Betreuer und Prof. Dr. rer. nat. Fassbender als 2. Prüfer erstellt. Bei Fehlern bitte eine kurze Mail mit Fehlerbeschreibung an vs@paul.buetow.org schicken!");
        initString("lang.activate", "aktivieren");
        initString("lang.activated", "aktiviert");
        initString("lang.actualize", "Aktualisieren");
        initString("lang.all", "Alle");
        initString("lang.antialiasing", "Anti-Aliasing");
        initString("lang.cancel", "Abbrechen");
        initString("lang.client", "Client");
        initString("lang.clientrequest.start", "Clientanfrage starten");
        initString("lang.close", "Schliessen");
        initString("lang.colorchooser", "Farbauswahl");
        initString("lang.colorchooser2", "Bitte Farbe auswählen");
        initString("lang.copy", "Kopieren");
        initString("lang.crashed", "Abgestürzt");
        initString("lang.dat", "Simulation (.dat)");
        initString("lang.deactivate", "deaktivieren");
        initString("lang.deactivated", "deaktiviert");
        initString("lang.default", "Defaults");
        initString("lang.edit", "Editieren");
        initString("lang.editor", "Editor");
        initString("lang.event", "Ereignis");
        initString("lang.event.add.global", "Globales Ereignis einfügen");
        initString("lang.event.add.local", "Lokales Ereignis einfügen");
        initString("lang.event.add.time", "bei");
        initString("lang.events", "Ereignisse");
        initString("lang.events.process", "Prozessereignisse");
        initString("lang.file", "Datei");
        initString("lang.filter", "Filter");
        initString("lang.logging.active", "Logging");
        initString("lang.logging.clear", "Loggs löschen");
        initString("lang.message", "Nachricht");
        initString("lang.message.recv", "Nachricht erhalten");
        initString("lang.message.sent", "Nachricht versendet");
        initString("lang.mode.expert", "Expertenmodus");
        initString("lang.name", "VS-Simulator 1.0");
        initString("lang.ok", "OK");
        initString("lang.open", "Öffnen");
        initString("lang.pause", "Pausieren");
        initString("lang.prefs", "Einstellungen");
        initString("lang.prefs.color", "Farbeinstellungen");
        initString("lang.prefs.diverse", "Diverse Einstellungen");
        initString("lang.prefs.ext", "Erweiterte Einstellungen");
        initString("lang.prefs.message", "Nachrichteneinstellungen");
        initString("lang.prefs.message.defaults", "Nachrichteneinstellungen für neue Prozesse");
        initString("lang.prefs.more", "Mehr Einstellungen");
        initString("lang.prefs.process", "Prozesseinstellungen");
        initString("lang.prefs.process", "Prozessstandardeinstellungen");
        initString("lang.prefs.process.defaults", "Einstellungen für neue Prozesse");
        initString("lang.prefs.process.ext", "Erweiterte Prozesseinstellungen");
        initString("lang.prefs.protocols", "Protokolleinstellungen");
        initString("lang.prefs.simulator", "Simulationseinstellungen");
        initString("lang.process", "Prozess");
        initString("lang.process.add.new", "Neuen Prozess hinzufügen");
        initString("lang.process.crash", "Prozess abstürzen");
        initString("lang.process.edit", "Prozess editieren");
        initString("lang.process.id", "PID");
        initString("lang.process.new", "Neuer Prozess");
        initString("lang.process.not.selected", "Kein Prozess ausgewählt");
        initString("lang.process.recover", "Prozess wiederbeleben");
        initString("lang.process.remove", "Prozess entfernen");
        initString("lang.process.selected", "Aktuell ausgewählter Prozess");
        initString("lang.process.time.local", "Lokale Zeit");
        initString("lang.processes.all", "Alle Prozesse");
        initString("lang.protocol", "Protokoll");
        initString("lang.protocol.client", "Clientseite");
        initString("lang.protocol.editor", "Protokolleditor");
        initString("lang.protocol.server", "Serverseite");
        initString("lang.protocol.tasks.activation", "Client-/Serverprotokoll Aktivierung");
        initString("lang.protocol.tasks.client", "Client Task-Manager (Clientanfragen)");
        initString("lang.protocols", "Protokolle");
        initString("lang.quit", "Beenden");
        initString("lang.recovered", "Wiederbelebt");
        initString("lang.remove", "Entfernen");
        initString("lang.replay", "Wiederholen");
        initString("lang.reset", "Reset");
        initString("lang.save", "Speichern");
        initString("lang.saveas", "Speichern unter");
        initString("lang.server", "Server");
        initString("lang.serverrequest.start", "Serveranfrage starten");
        initString("lang.simulator", "Simulator");
        initString("lang.simulator.close", "Simulation schliessen");
        initString("lang.simulator.finished", "Simulation beendet");
        initString("lang.simulator.new", "Neue Simulation");
        initString("lang.simulator.paused", "Simulation pausiert");
        initString("lang.simulator.resetted", "Simulation zurückgesetzt");
        initString("lang.simulator.started", "Simulation gestartet");
        initString("lang.start", "Starten");
        initString("lang.stop", "Stoppen");
        initString("lang.takeover", "Übernehmen");
        initString("lang.task", "Aufgabe");
        initString("lang.task.manager", "Ereigniseditor");
        initString("lang.tasks.fullfilled", "Abgelaufene Aufgaben");
        initString("lang.tasks.global", "Globale Aufgaben");
        initString("lang.tasks.local", "Lokale Aufgaben");
        initString("lang.time", "Zeit");
        initString("lang.time.lamport", "Lamportzeit");
        initString("lang.time.vector", "Vektorzeit");
        initString("lang.timed.global", "Globale Ereignisse");
        initString("lang.timed.local", "Lokale Ereignisse");
        initString("lang.type", "Typ");
        initString("lang.value", "Wert");
        initString("lang.variable", "Variable");
        initString("lang.variables", "Variablen");
        initString("lang.variables.global", "Globale Variablen");
        initString("lang.window.close", "Fenster schliessen");
        initString("lang.window.new", "Neues Fenster");
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
        initInteger("keyevent.cancel", KeyEvent.VK_N, null, 0, 100);
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
        initInteger("keyevent.replay", KeyEvent.VK_R, null, 0, 100);
        initInteger("keyevent.reset", KeyEvent.VK_R, null, 0, 100);
        initInteger("keyevent.save", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.saveas", KeyEvent.VK_V, null, 0, 100);
        initInteger("keyevent.simulator", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.start", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.stop", KeyEvent.VK_P, null, 0, 100);

        initInteger("div.window.prefs.xsize", 400, "Einstellungsfenster X-Achse", 550, 3200, "px");
        initInteger("div.window.prefs.ysize", 400, "Einstellungsfenster Y-Achse", 640, 2400, "px");
        initInteger("div.window.loggsize", 300, "Loggfenster Y-Achse", 100, 1000, "px");
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
