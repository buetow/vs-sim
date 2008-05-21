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
import prefs.*;

public class VSSimulationEditor extends VSBetterEditor {
    private boolean startNewSimulation;
    private VSSimulatorFrame simulatorFrame;

    public VSSimulationEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame) {
        super(prefs, prefs, prefs.getString("name")
              + " - " + prefs.getString("lang.prefs"));
        this.simulatorFrame = simulatorFrame;

        startNewSimulation = true;
        init();
    }

    public VSSimulationEditor(VSPrefs prefs, int prefsCategory) {
        super(prefs, prefs, prefs.getString("name")
              + " - " + prefs.getString("lang.prefs"
                                        + (prefsCategory == ALL_PREFERENCES ? ".ext" : "")),
              prefsCategory);

        startNewSimulation = false;
        init();
    }

    private void init() {
        infoArea.setText(prefs.getString("lang.prefs.info!"));
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            super.actionPerformed(e);
            prefsToEdit.saveFile();

            VSFrame frame = getFrame();
            if (frame != null)
                frame.dispose();

            if (startNewSimulation)
                simulatorFrame.addSimulation(
                    new VSSimulation(prefs, simulatorFrame));

        } else {
            super.actionPerformed(e);
        }
    }

    public void newVSEditorInstance(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) {
        VSPrefs newPrefs = VSDefaultPrefs.init();
        new VSEditorFrame(newPrefs, getFrame(), new VSSimulationEditor(prefs, prefsCategory));
    }
}
