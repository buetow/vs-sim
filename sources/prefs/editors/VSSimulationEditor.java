package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.File;

import simulator.*;
import utils.*;
import prefs.VSPrefs;

public class VSSimulationEditor extends VSBetterEditor {
    private boolean startNewSimulation;
    private VSSimulatorFrame simulatorFrame;

    public VSSimulationEditor(VSPrefs prefs, Component relativeTo) {
        super(prefs, relativeTo, prefs, prefs.getString("name")
              + " - " + prefs.getString("lang.prefs"));
        this.simulatorFrame = (VSSimulatorFrame) relativeTo;

        startNewSimulation = true;
        init();
    }

    public VSSimulationEditor(VSPrefs prefs, Component relativeTo, int prefsCategory) {
        super(prefs, relativeTo, prefs, prefs.getString("name")
              + " - " + prefs.getString("lang.prefs"
                                        + (prefsCategory == ALL_PREFERENCES ? ".ext" : "")),
              prefsCategory);

        startNewSimulation = false;
        init();
    }

    private void init() {
        super.infoArea.setText(prefs.getString("lang.prefs.info!"));
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            super.actionPerformed(e);
            prefsToEdit.saveFile();

            frame.dispose();

            if (startNewSimulation)
                simulatorFrame.addSimulation(
                    new VSSimulation(prefs, simulatorFrame));

        } else {
            super.actionPerformed(e);
        }
    }

    public void newVSEditorInstance(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) {
        new VSSimulationEditor(prefs, getFrame(), prefsCategory);
    }
}
