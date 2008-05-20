package prefs;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.*;

public class VSDefaultPrefs extends VSPrefs {
    public static VSPrefs init() {
        return init(VSPrefs.PREFERENCES_FILENAME);
    }

    public static VSPrefs init(String fileName) {
        File file = new File(fileName);
        VSPrefs prefs = null;

        if (file.exists()) {
            prefs = openFile(fileName);

        } else {
            prefs = new VSDefaultPrefs();
            prefs.fillWithDefaults();
        }

        return prefs;
    }

    public void saveFile() {
        super.saveFile(VSPrefs.PREFERENCES_FILENAME);
    }

    public void fillWithDefaults() {
        super.clear();
        fillDefaultBooleans();
        fillDefaultColors();
        fillDefaultFloats();
        fillDefaultIntegers();
        fillDefaultLongs();
        fillDefaultStrings();
    }

    public void fillDefaultStrings() {
        initString("lang.clientrequest.start", "Clientanfrage starten");
        initString("lang.about", "About");
        initString("lang.about.info!", "Dieses Programm wurde von Paul Bütow im Rahmen der Diplomarbeit bei Prof. Dr.-Ing. Oßmann erstellt. Dieses Programm stellt noch keinesfalls eine fertige Version dar, da es sich noch in Entwicklung befindet und die Diplomarbeit erst mitte August abgeschlossen sein wird! Bei Fehlern bitte eine kurze Mail mitsamt Fehlerbeschreibung an paul@buetow.org schicken! Dieser Simulator wird später außerdem unter einer open source Linzenz (wahrscheinlich der GNU General Public License) freigegeben!");
        initString("lang.activate", "aktivieren");
        initString("lang.activated", "aktiviert");
        initString("lang.deactivated", "deaktiviert");
        initString("lang.actualize", "Aktualisieren");
        initString("lang.cancel", "Abbrechen");
        initString("lang.client", "Client");
        initString("lang.close", "Schliessen");
        initString("lang.colorchooser", "Farbauswahl");
        initString("lang.colorchooser2", "Bitte Farbe auswählen");
        initString("lang.crash", "Abstürzen");
        initString("lang.antialiasing", "Anti-Aliasing");
        initString("lang.all", "Alle");
        initString("lang.crashed", "Abgestürzt");
        initString("lang.deactivate", "deaktivieren");
        initString("lang.default", "Defaults");
        initString("lang.edit", "Editieren");
        initString("lang.editor", "Editor");
        initString("lang.event", "Ereignis");
        initString("lang.events.process", "Prozessereignisse");
        initString("lang.events.protocol", "Protokollereignisse");
        initString("lang.file", "Datei");
        initString("lang.filter", "Filter");
        initString("lang.global", "Global");
        initString("lang.local", "Lokal");
        initString("lang.logging.active", "Logging");
        initString("lang.logging.clear", "Loggs löschen");
        initString("lang.message", "Nachricht");
        initString("lang.message.recv", "Nachricht erhalten");
        initString("lang.message.sent", "Nachricht versendet");
        initString("lang.new", "Neu");
        initString("lang.ok", "OK");
        initString("lang.open", "Öffnen");
        initString("lang.pause", "Pausieren");
        initString("lang.prefs", "Einstellungen");
        initString("lang.prefs.ext", "Erweiterte Einstellungen");
        initString("lang.prefs.info!", "Prozesseinstellungen können später für jeden Prozess einzelnd eingestellt werden.Die folgenden Werte sind lediglich die globalen Defaultwerte, die für neue Prozesse verwendet werden!");
        initString("lang.prefs.more", "Mehr Einstellungen");
        initString("lang.prefs.process", "Prozesseinstellungen");
        initString("lang.prefs.process.ext", "Erweiterte Prozesseinstellungen");
        initString("lang.prefs.process.info!", "Änderungen werden erst nach Betätigen des \"Übernehmen\" oder \"OK\" Knopfes übernommen!");
        initString("lang.prefs.protocol", "Protokolleinstellungen");
        initString("lang.prefs.protocol.info!", "Änderungen werden erst nach Betätigen des \"Übernehmen\" oder \"OK\" Knopfes übernommen!");
        initString("lang.process", "Prozess");
        initString("lang.processes.all", "Alle Prozesse");
        initString("lang.process.id", "PID");
        initString("lang.process.new", "Neuer Prozess");
        initString("lang.process.time.local", "Lokale Zeit");
        initString("lang.protocol", "Protokoll");
        initString("lang.protocol.client", "Clientseite");
        initString("lang.protocol.editor", "Protokolleditor");
        initString("lang.protocol.server", "Serverseite");
        initString("lang.protocol.tasks.activation", "Client-/Serverprotokoll Aktivierung");
        initString("lang.protocol.tasks.client", "Client Task-Manager (Clientanfragen)");
        initString("lang.quit", "Beenden");
        initString("lang.recover", "Wiederbeleben");
        initString("lang.recovered", "Wiederbelebt");
        initString("lang.remove", "Entfernen");
        initString("lang.replay", "Wiederholen");
        initString("lang.requests", "Anfragen");
        initString("lang.reset", "Reset");
        initString("lang.save", "Speichern");
        initString("lang.saveas", "Speichern unter");
        initString("lang.server", "Server");
        initString("lang.simulation", "Simulation");
        initString("lang.simulation.finished", "Simulation beendet");
        initString("lang.simulation.new", "Neue Simulation");
        initString("lang.simulation.paused", "Simulation pausiert");
        initString("lang.simulation.resetted", "Simulation zurückgesetzt");
        initString("lang.simulation.started", "Simulation gestartet");
        initString("lang.start", "Starten");
        initString("lang.stop", "Stoppen");
        initString("lang.takeover", "Übernehmen");
        initString("lang.task", "Aufgabe");
        initString("lang.task.manager", "Aufgabenmanager");
        initString("lang.tasks.fullfilled", "Abgelaufene Aufgaben");
        initString("lang.tasks.global", "Globale Aufgaben");
        initString("lang.tasks.local", "Lokale Aufgaben");
        initString("lang.time", "Zeit");
        initString("lang.time.lamport", "Lamportzeit");
        initString("lang.time.vector", "Vektorzeit");
        initString("lang.type", "Typ");
        initString("name", "Verteilte Systeme v0.2-devel");
    }

    public void fillDefaultIntegers() {
        /* Simulation prefs */
        initInteger("sim.process.num", 3, "Anzahl der Prozesse", 1, 6);
        initIntegerUnit("sim.message.prob.outage", 0, "W'keit, dass eine Nachricht verloren geht", 0, 100, "%");
        initIntegerUnit("sim.process.prob.crash", 0, "W'keit, dass der Prozess ausfällt", 0, 100, "%");
        initIntegerUnit("sim.seconds", 30, "Simulationsdauer", 5, 120, "s");

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
        initInteger("keyevent.simulation", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.start", KeyEvent.VK_S, null, 0, 100);
        initInteger("keyevent.stop", KeyEvent.VK_P, null, 0, 100);

        initIntegerUnit("window.prefs.xsize", 350, "X-Grösse des Einstellungsfensters", 550, 3200, "px");
        initIntegerUnit("window.prefs.ysize", 600, "Y-Grösse des Einstellungsfensters", 640, 2400, "px");
        initIntegerUnit("window.loggsize", 300, "Y-Grösse des Loggingfensters", 100, 1000, "px");
        initIntegerUnit("window.splitsize", 300, null, 100, 1000, "px");
        initIntegerUnit("window.xsize", 1024, "X-Grösse des Hauptfensters", 800, 3200, "px");
        initIntegerUnit("window.ysize", 768, "Y-Grösse des Hauptfensters", 600, 2400, "px");
    }

    public void fillDefaultFloats() {
        /* Simulation prefs */
        initFloat("sim.process.clock.variance", 0, "Uhrabweichung");
    }

    public void fillDefaultLongs() {
        /* Simulation prefs */
        initLongUnit("sim.message.sendingtime.min", 1000, "Minimale Nachrichtenübertragungszeit", "ms");
        initLongUnit("sim.message.sendingtime.max", 5000, "Maximale Nachrichtenübertragungszeit", "ms");
    }

    public void fillDefaultColors() {
        /* Internal prefs */
        initColor("paintarea.background", new Color(0xFF, 0xFF, 0xFF));
        initColor("process.default", new Color(0x00, 0x00, 0x00));
        initColor("process.running", new Color(0x0D, 0xD8, 0x09));
        initColor("process.crashed", new Color(0xff, 0x00, 0x00));
        initColor("process.highlight", new Color(0xff, 0xA5, 0x00));
        initColor("process.line", new Color(0x00, 0x00, 0x00));
        initColor("process.secondline", new Color(0xAA, 0xAA, 0xAA));
        initColor("process.sepline", new Color(0xff, 0x00, 0x00));
        initColor("process.stopped", new Color(0x00, 0x00, 0x00));
        initColor("message.arrived", new Color(0x00, 0x85, 0xD2));
        initColor("message.sending", new Color(0x0D, 0xD8, 0x09));
        initColor("message.lost", new Color(0xFF, 0x00, 0x00));
    }

    public void fillDefaultBooleans() {
        initBoolean("sim.message.own.recv", false, "Prozesse empfangen ihre eigenen Nachrichten");
    }
}
