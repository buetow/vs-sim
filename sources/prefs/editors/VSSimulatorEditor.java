/*
 * Copyright (c) 2008 Paul Buetow, vs@dev.buetow.org
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * All icons of the icons/ folder are 	under a Creative Commons 
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa. 
 * 
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package prefs.editors;

import java.awt.event.*;
import javax.swing.*;

import simulator.*;
import prefs.*;

// TODO: Auto-generated Javadoc
/**
 * The class VSSimulatorEditor.
 */
public class VSSimulatorEditor extends VSAbstractBetterEditor {
    private static final long serialVersionUID = 1L;

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
     * @see prefs.editors.VSAbstractBetterEditor#addToButtonPanelFront(javax.swing.JPanel)
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
     * @see prefs.editors.VSAbstractBetterEditor#actionPerformed(java.awt.event.ActionEvent)
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
