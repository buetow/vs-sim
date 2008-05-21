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
    private String title;

    public VSProcessEditor(VSPrefs prefs, VSProcess process) {
        super(prefs, process, prefs.getString("name") + " - " + prefs.getString("lang.prefs.process"));;
        this.process = process;
        init();
    }

    public VSProcessEditor(VSPrefs prefs, VSProcess process, int prefsCategory) {
        super(prefs, process, prefs.getString("name") + " - " + prefs.getString("lang.prefs.process"
                + (prefsCategory == ALL_PREFERENCES ? ".ext" : "")), prefsCategory);

        this.process = process;
        init();
    }

    private void init() {
        infoArea.setText(prefs.getString("lang.prefs.process.info!"));
        VSFrame frame = getFrame();
        if (frame != null)
            frame.disposeWithParent();
        createButtonPanel();
    }

    public String getTitle() {
        return title;
    }

    protected void addToEditPanelFront(JPanel editPanel) {
        super.addToEditPanelFront(editPanel);

        if (prefsCategory != SIMULATION_PREFERENCES)
            return;

    }

    protected void resetEditPanel() {
        super.resetEditPanel();
    }

    protected void savePrefs() {
        super.savePrefs();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = super.buttonPanel;

        JButton cancelButton = new JButton(
            prefs.getString("lang.takeover"));
        cancelButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            process.updateFromVSPrefs();
            disposeFrameIfExists();

        } else if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();
            process.updateFromVSPrefs();

        } else {
            super.actionPerformed(e);
        }
    }

    public void newVSEditorInstance(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) {
        if (prefsToEdit instanceof VSProcess) {
            VSProcess process = (VSProcess) prefsToEdit;
            new VSEditorFrame(prefs, getFrame(), new VSProcessEditor(prefs, process, prefsCategory));
        }
    }
}
