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

/**
 * The class VSMain. This class contains the static main method. The simulator
 * starts here!
 *
 * @author Paul C. Buetow
 */
public class VSMain {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VSMain object.
     *
     * @param prefs the prefs
     */
    public VSMain(VSPrefs prefs) {
        init(prefs, null);
    }

    /**
     * Instantiates a new VSMain object
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the window relative to
     */
    public VSMain(VSPrefs prefs, Component relativeTo) {
        init(prefs, relativeTo);
    }

    /**
     * Inits the VSMain object.
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the window relative to
     */
    private void init(VSPrefs prefs, Component relativeTo) {
        VSSimulatorFrame simulatorFrame =
            new VSSimulatorFrame(prefs, relativeTo);

        new VSEditorFrame(prefs, relativeTo,
                          new VSSimulatorEditor(prefs, simulatorFrame));
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
