package simulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import editors.*;
import prefs.*;
import protocols.*;

public class VSMain {
    public VSMain(VSPrefs prefs) {
        initialize(prefs, null);
    }

    public VSMain(VSPrefs prefs, Component relativeTo) {
        initialize(prefs, relativeTo);
    }

    private void initialize(VSPrefs prefs, Component relativeTo) {
        new VSSimulationEditor(prefs, relativeTo);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        VSPrefs prefs = VSDefaultPrefs.initialize();
        RegisteredProtocols.initialize(prefs);
        new VSMain(prefs);
    }
}
