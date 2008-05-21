package simulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import events.*;
import prefs.*;
import prefs.editors.*;

public class VSMain {
    public VSMain(VSPrefs prefs) {
        init(prefs, null);
    }

    public VSMain(VSPrefs prefs, Component relativeTo) {
        init(prefs, relativeTo);
    }

    private void init(VSPrefs prefs, Component relativeTo) {
        VSSimulatorFrame simulatorFrame = new VSSimulatorFrame(prefs, relativeTo);
        new VSEditorFrame(prefs, relativeTo, new VSSimulationEditor(prefs, simulatorFrame));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        VSPrefs prefs = VSDefaultPrefs.init();
        VSRegisteredEvents.init(prefs);
        new VSMain(prefs);
    }
}
