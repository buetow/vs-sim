package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.File;

import simulator.*;
import utils.*;
import core.*;
import protocols.*;
import events.*;
import prefs.VSPrefs;

public class VSProcessEditor extends VSBetterEditor {
    private VSProcess process;
    public static boolean TAKEOVER_BUTTON;
    public VSProcessEditor(VSPrefs prefs, VSProcess process) {
        super(prefs, process, prefs.getString("lang.name") + " - " + prefs.getString("lang.prefs.process"));;
        this.process = process;
        disposeFrameWithParentIfExists();
        getInfoArea().setText(prefs.getString("lang.prefs.process.info!"));
    }

    protected void addToButtonPanelFront(JPanel buttonPanel) {
        JButton takeoverButton = new JButton(
            prefs.getString("lang.takeover"));
        takeoverButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
        takeoverButton.addActionListener(this);
        buttonPanel.add(takeoverButton);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            process.updateFromVSPrefs();

        } else if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();
            process.updateFromVSPrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
