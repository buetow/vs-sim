/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package simulator;

import java.awt.*;
import javax.swing.*;

import events.*;
import prefs.*;
import prefs.editors.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VSMain.
 */
public class VSMain {

    /**
     * Instantiates a new vS main.
     *
     * @param prefs the prefs
     */
    public VSMain(VSPrefs prefs) {
        init(prefs, null);
    }

    /**
     * Instantiates a new vS main.
     *
     * @param prefs the prefs
     * @param relativeTo the relative to
     */
    public VSMain(VSPrefs prefs, Component relativeTo) {
        init(prefs, relativeTo);
    }

    /**
     * Inits the.
     *
     * @param prefs the prefs
     * @param relativeTo the relative to
     */
    private void init(VSPrefs prefs, Component relativeTo) {
        VSSimulatorFrame simulatorFrame = new VSSimulatorFrame(prefs, relativeTo);
        new VSEditorFrame(prefs, relativeTo, new VSSimulatorEditor(prefs, simulatorFrame));
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
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
