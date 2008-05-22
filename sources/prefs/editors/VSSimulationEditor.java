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
    private VSSimulatorFrame simulatorFrame;
    private VSSimulation simulation;
    public static boolean TAKEOVER_BUTTON;
    private boolean dontStartNewSimulation;

    public VSSimulationEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame, VSSimulation simulation) {
        super(prefs, prefs, prefs.getString("lang.name")
              + " - " + prefs.getString("lang.prefs"));
        this.dontStartNewSimulation = true;//simulation != null;
        this.simulatorFrame = simulatorFrame;
        this.simulation = simulation;
        getInfoArea().setText(prefs.getString("lang.prefs.info!"));
    }

    public VSSimulationEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame) {
        super(prefs, prefs, prefs.getString("lang.name")
              + " - " + prefs.getString("lang.prefs"));
        this.simulatorFrame = simulatorFrame;
        getInfoArea().setText(prefs.getString("lang.prefs.info!"));
    }

    protected void addToButtonPanelFront(JPanel buttonPanel) {
        if (TAKEOVER_BUTTON) {
            TAKEOVER_BUTTON = false;
            JButton takeoverButton = new JButton(
                prefs.getString("lang.takeover"));
            takeoverButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
            takeoverButton.addActionListener(this);
            buttonPanel.add(takeoverButton);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();
            if (expertModeChanged()) {
                if (simulation != null)
                    simulation.fireExpertModeChanged();
            }
            simulation.updateFromPrefs();

        } else if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            if (expertModeChanged()) {
                if (simulation != null)
                    simulation.fireExpertModeChanged();
            }
            if (!dontStartNewSimulation)
                simulatorFrame.addSimulation(new VSSimulation(prefsToEdit, simulatorFrame));

        } else {
            super.actionPerformed(e);
        }
    }
}
