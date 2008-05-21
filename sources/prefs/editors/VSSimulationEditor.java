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
    public static boolean TAKEOVER_BUTTON;

    public VSSimulationEditor(VSPrefs prefs, VSSimulatorFrame simulatorFrame) {
        super(prefs, prefs, prefs.getString("name")
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
            simulatorFrame.getCurrentSimulation().updateFromPrefs();

        } else if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            simulatorFrame.addSimulation(new VSSimulation(prefsToEdit, simulatorFrame));

        } else {
            super.actionPerformed(e);
        }
    }
}
