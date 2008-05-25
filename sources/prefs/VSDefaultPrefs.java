package prefs;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class VSDefaultPrefs extends VSPrefs {
    public static VSPrefs init() {
        return init(VSPrefs.PREFERENCES_FILENAME);
    }

    public static VSPrefs init(String fileName) {
        VSPrefs prefs = new VSDefaultPrefs();
        prefs.fillWithDefaults();
        return prefs;
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
        initString("lang.about", "About");
        initString("lang.about.info!", "Dieses Programm wurde von Paul Bütow im Rahmen der Diplomarbeit \"Objektorientierte Entwicklung eines GUI-basierten Tools für die ereignisbasierte Simulation verteilter Systeme\" bei Prof. Dr.-Ing. Oßmann erstellt. Dieses Programm stellt noch keinesfalls eine fertige Version dar, da es sich noch in Entwicklung befindet und die Diplomarbeit erst mitte August abgeschlossen sein wird! Bei Fehlern bitte eine kurze Mail mitsamt Fehlerbeschreibung an paul@buetow.org schicken! Dieser Simulator wird später außerdem unter einer open source Linzenz (wahrscheinlich der GNU General Public License) freigegeben!");
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
        initString("lang.crashed", "Abgestürzt");
        initString("lang.deactivate", "deaktivieren");
        initString("lang.deactivated", "deaktiviert");
        initString("lang.default", "Defaults");
        initString("lang.edit", "Editieren");
        initString("lang.editor", "Editor");
        initString("lang.event", "Ereignis");
        initString("lang.events", "Ereignisse");
        initString("lang.events.process", "Prozessereignisse");
        initString("lang.events.protocol", "Protokollereignisse");
        initString("lang.file", "Datei");
        initString("lang.filter", "Filter");
        initString("lang.logging.active", "Logging");
        initString("lang.logging.clear", "Loggs löschen");
        initString("lang.message", "Nachricht");
        initString("lang.message.recv", "Nachricht erhalten");
        initString("lang.message.sent", "Nachricht versendet");
        initString("lang.mode.expert", "Expertenmodus");
        initString("lang.name", "VS-Simulator v0.2-devel");
        initString("lang.ok", "OK");
        initString("lang.open", "Öffnen");
        initString("lang.pause", "Pausieren");
        initString("lang.prefs", "Einstellungen");
        initString("lang.prefs.color", "Farbeinstellungen");
        initString("lang.prefs.diverse", "Diverse Einstellungen");
        initString("lang.prefs.ext", "Erweiterte Einstellungen");
        initString("lang.prefs.info!", "Prozessvariablen können für jeden Prozess einzelnd eingestellt werden. Die hier gezeigen Prozessvariablen sind lediglich die globalen Defaultwerte, die für neue Prozesse verwendet werden!");
        initString("lang.prefs.message", "Nachrichteneinstellungen");
        initString("lang.prefs.more", "Mehr Einstellungen");
        initString("lang.prefs.process", "Prozesseinstellungen");
        initString("lang.prefs.process", "Prozessstandardeinstellungen");
        initString("lang.prefs.process.ext", "Erweiterte Prozesseinstellungen");
        initString("lang.prefs.process.info!", "Änderungen werden erst nach Betätigen des \"Übernehmen\" Knopfes übernommen!");
        initString("lang.prefs.protocols", "Protokolleinstellungen");
        initString("lang.prefs.simulation", "Simulationseinstellungen");
        initString("lang.process", "Prozess");
        initString("lang.process.add.new", "Neuen Prozess hinzufügen");
        initString("lang.process.crash", "Prozess abstürzen");
        initString("lang.process.remove", "Prozess entfernen");
        initString("lang.process.edit", "Prozess editieren");
        initString("lang.process.id", "PID");
        initString("lang.process.new", "Neuer Prozess");
        initString("lang.process.recover", "Prozess wiederbeleben");
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
        initString("lang.requests", "Anfragen");
        initString("lang.reset", "Reset");
        initString("lang.save", "Speichern");
        initString("lang.saveas", "Speichern unter");
        initString("lang.server", "Server");
        initString("lang.simulation", "Simulation");
        initString("lang.simulation.close", "Simulation schliessen");
        initString("lang.simulation.finished", "Simulation beendet");
        initString("lang.simulation.new", "Neue Simulation");
        initString("lang.simulation.new", "Neue Simulation");
        initString("lang.simulation.paused", "Simulation pausiert");
        initString("lang.simulation.resetted", "Simulation zurückgesetzt");
        initString("lang.simulation.started", "Simulation gestartet");
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

    public void fillDefaultIntegers() {
        /* Simulation prefs */
        initInteger("sim.process.num", 3, "Anzahl der Prozesse", 1, 6);
        initIntegerUnit("message.prob.outage", 0, "Nachrichtenverlustw'keit", 0, 100, "%");
        initIntegerUnit("process.prob.crash", 0, "Prozessausfallw'keit", 0, 100, "%");
        initIntegerUnit("sim.seconds", 15, "Simulationsdauer", 5, 120, "s");

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

        initIntegerUnit("div.window.prefs.xsize", 400, "Einstellungsfenster X-Achse", 550, 3200, "px");
        initIntegerUnit("div.window.prefs.ysize", 400, "Einstellungsfenster Y-Achse", 640, 2400, "px");
        initIntegerUnit("div.window.loggsize", 300, "Loggfenster Y-Achse", 100, 1000, "px");
        initIntegerUnit("div.window.splitsize", 320, "Toolbar X-Achse", 100, 1000, "px");
        initIntegerUnit("div.window.xsize", 1024, "Hauptfenster X-Achse", 800, 3200, "px");
        initIntegerUnit("div.window.ysize", 768, "Hauptfenster Y-Achse", 600, 2400, "px");
    }

    public void fillDefaultFloats() {
        /* Simulation prefs */
        initFloat("process.clock.variance", 0, "Uhrabweichung");
        initFloat("sim.clock.speed", 0.5f, "Simulationsgeschwindigkeit");
    }

    public void fillDefaultLongs() {
        /* Simulation prefs */
        initLongUnit("message.sendingtime.min", 500, "Minimale Übertragungszeit", "ms");
        initLongUnit("message.sendingtime.max", 2000, "Maximale Übertragungszeit", "ms");
    }

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

    public void fillDefaultBooleans() {
        //initBoolean("message.broadcast", false, "Nachrichten sind immer Broadcasts");
        initBoolean("sim.mode.expert", false, "Expertenmodus aktivieren");
        initBoolean("sim.message.own.recv", false, "Eigene Nachrichten empfangen");
    }
}
