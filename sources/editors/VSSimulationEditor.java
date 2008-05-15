package editors;

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

public class VSSimulationEditor extends VSEditorFrame {
    private boolean startNewVSSimulation;

    public VSSimulationEditor(VSPrefs prefs, Component relativeTo) {
        super(prefs, relativeTo, prefs, prefs.getString("name")
              + " - " + prefs.getString("lang.prefs"));

        startNewVSSimulation = true;
        initialize();
    }

    public VSSimulationEditor(VSPrefs prefs, Component relativeTo, int prefsCategory) {
        super(prefs, relativeTo, prefs, prefs.getString("name")
              + " - " + prefs.getString("lang.prefs"
                                        + (prefsCategory == ALL_PREFERENCES ? ".ext" : "")),
              prefsCategory);

        startNewVSSimulation = false;
        initialize();
    }

    private void initialize() {
        super.infoArea.setText(prefs.getString("lang.prefs.info!"));
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            super.actionPerformed(e);
            prefsToEdit.saveFile();

            frame.dispose();

            if (startNewVSSimulation)
                new VSSimulation(prefs, getFrame());

        } else {
            super.actionPerformed(e);
        }
    }

    public void newVSEditorInstance(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) {
        new VSSimulationEditor(prefs, getFrame(), prefsCategory);
    }
}
