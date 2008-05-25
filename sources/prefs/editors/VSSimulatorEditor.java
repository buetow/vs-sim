/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs.editors;

import java.awt.event.*;
import javax.swing.*;

import simulator.*;
import prefs.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VSSimulatorEditor.
 */
public class VSSimulatorEditor extends VSBetterEditor {

    /** The simulator frame. */
    private VSSimulatorFrame simulatorFrame;

    /** The simulation. */
    private VSSimulator simulation;

    /** The TAKEOVE r_ button. */
    public static boolean TAKEOVER_BUTTON;

    /** The dont start new simulation. */
    private boolean dontStartNewSimulation;

    /**
     * Instantiates a new lang.process.removesimulator editor.
     *
     * @param prefs the prefs
     * @param simulatorFrame the simulator frame
     * @param simulation the simulation
     */
    public VSSimulatorEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame, VSSimulator simulation) {
        super(prefs, prefs, prefs.getString("lang.name")
              + " - " + prefs.getString("lang.prefs"));
        this.dontStartNewSimulation = true;//simulation != null;
        this.simulatorFrame = simulatorFrame;
        this.simulation = simulation;
    }

    /**
     * Instantiates a new lang.process.removesimulator editor.
     *
     * @param prefs the prefs
     * @param simulatorFrame the simulator frame
     */
    public VSSimulatorEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame) {
        super(prefs, prefs, prefs.getString("lang.name")
              + " - " + prefs.getString("lang.prefs"));
        this.simulatorFrame = simulatorFrame;
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSBetterEditor#addToButtonPanelFront(javax.swing.JPanel)
     */
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

    /* (non-Javadoc)
     * @see prefs.editors.VSBetterEditor#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();

            if (simulation != null) {
                if (expertModeChanged())
                    simulation.fireExpertModeChanged();
                simulation.updateFromPrefs();
            }

        } else if (actionCommand.equals(prefs.getString("lang.cancel"))) {
            if (!dontStartNewSimulation)
                simulatorFrame.dispose();

        } else if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            if (expertModeChanged()) {
                if (simulation != null)
                    simulation.fireExpertModeChanged();
            }
            if (!dontStartNewSimulation)
                simulatorFrame.addSimulation(new VSSimulator(prefsToEdit, simulatorFrame));
            else if (simulation != null)
                simulation.updateFromPrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
