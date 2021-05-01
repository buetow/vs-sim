/*
 * Copyright (c) 2008 Paul C. Buetow, vs-sim@dev.buetow.org
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

/**
 * The class VSSimulatorEditor, is for editing a VSSimulator object.
 *
 * @author Paul C. Buetow
 */
public class VSSimulatorEditor extends VSAbstractBetterEditor {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The constant OPENED_NEW_WINDOW */
    public static final boolean OPENED_NEW_WINDOW = true;

    /** The constant OPENED_NEW_TAB */
    public static final boolean OPENED_NEW_TAB = false;

    /** The simulator frame. */
    private VSSimulatorFrame simulatorFrame;

    /** The simulator. */
    private VSSimulator simulator;

    /** The TAKEOVE r_ button. */
    public static boolean TAKEOVER_BUTTON;

    /** The dont start new simulator. */
    private boolean dontStartNewSimulator;

    /** Open a new simulator window. */
    private boolean openedNewWindow;

    /**
     * Instantiates a new VSSimulatorEditor object.
     *
     * @param prefs the prefs
     * @param simulatorFrame the simulator frame
     * @param simulator the simulator
     */
    public VSSimulatorEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame,
                             VSSimulator simulator) {
        super(prefs, prefs, prefs.getString("lang.en.name")
              + " - " + prefs.getString("lang.en.prefs"));
        this.dontStartNewSimulator = true;//simulator != null;
        this.simulatorFrame = simulatorFrame;
        this.simulator = simulator;
    }

    /**
     * Instantiates a new VSSimulatorEditor object.
     *
     * @param prefs the prefs
     * @param simulatorFrame the simulator frame
     */
    public VSSimulatorEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame,
                             boolean openedNewWindow) {
        super(prefs, prefs, prefs.getString("lang.en.name")
              + " - " + prefs.getString("lang.en.prefs"));
        this.simulatorFrame = simulatorFrame;
        this.openedNewWindow = openedNewWindow;
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractBetterEditor#addToButtonPanelFront(
     *	javax.swing.JPanel)
     */
    protected void addToButtonPanelFront(JPanel buttonPanel) {
        if (TAKEOVER_BUTTON) {
            TAKEOVER_BUTTON = false;
            JButton takeoverButton = new JButton(
                prefs.getString("lang.en.takeover"));
            takeoverButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
            takeoverButton.addActionListener(this);
            buttonPanel.add(takeoverButton);
        }
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractBetterEditor#actionPerformed(
     *	java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.en.takeover"))) {
            savePrefs();

            if (simulator != null) {
                if (expertModeChanged())
                    simulator.fireExpertModeChanged();
                simulator.updateFromPrefs();
            }

        } else if (actionCommand.equals(prefs.getString("lang.en.cancel"))) {
            if (!dontStartNewSimulator && openedNewWindow)
                simulatorFrame.dispose();

        } else if (actionCommand.equals(prefs.getString("lang.en.ok"))) {
            savePrefs();
            if (expertModeChanged()) {
                if (simulator != null)
                    simulator.fireExpertModeChanged();
            }
            if (!dontStartNewSimulator)
                simulatorFrame.addSimulator(new VSSimulator(prefsToEdit,
                                            simulatorFrame));
            else if (simulator != null)
                simulator.updateFromPrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
