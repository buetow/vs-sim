package simulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
import prefs.editors.*;
import protocols.*;

public class VSMain {
    public VSMain(VSPrefs prefs) {
        init(prefs, null);
    }

    public VSMain(VSPrefs prefs, Component relativeTo) {
        init(prefs, relativeTo);
    }

    private void init(VSPrefs prefs, Component relativeTo) {
        new VSSimulationEditor(prefs, relativeTo);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        VSPrefs prefs = VSDefaultPrefs.init();
        VSRegisteredProtocols.init(prefs);
        new VSMain(prefs);
    }
}
