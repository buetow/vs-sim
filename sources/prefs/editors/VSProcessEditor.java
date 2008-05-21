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

        editPanelConstraints.gridy = editPanelRow++;
        editPanel.add(new JLabel(prefs.getString("lang.protocol.editor")), editPanelConstraints);

        editPanelConstraints.insets = insets;
        editPanelConstraints.gridy = editPanelRow++;
        JPanel protocolSelectorPanel = createProtocolSelector();
        editPanel.add(protocolSelectorPanel, editPanelConstraints);

        editPanelConstraints.insets = insetsTopSpaceing;
        editPanelConstraints.gridy = editPanelRow++;
    }

    private JPanel createProtocolSelector() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        Vector<String> registeredProtocolsNames = VSRegisteredEvents.getProtocolNames();
        Vector<String> registeredProtocolsShortnames = new Vector<String>();
        final HashMap<String,String> eventNames = new HashMap<String,String>();

        for (String protocolName : registeredProtocolsNames) {
            String protocolClassname = VSRegisteredEvents.getClassname(protocolName);
            String protocolShortname = VSRegisteredEvents.getShortname(protocolClassname);
            registeredProtocolsShortnames.add(protocolShortname);
            eventNames.put(protocolShortname, protocolName);
        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.ipadx = 10;
        constraints.ipady = 10;

        final JComboBox comboBox = new JComboBox(registeredProtocolsShortnames);
        comboBox.setBackground(Color.WHITE);
        panel.add(comboBox, constraints);
        constraints.gridy = 1;
        JButton button = new JButton(prefs.getString("lang.edit"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (ae.getActionCommand().equals(prefs.getString("lang.edit"))) {
                    String eventShortname = (String) comboBox.getSelectedItem();
                    String eventName = eventNames.get(eventShortname);
                    String eventClassname = VSRegisteredEvents.getClassname(eventName);
                    VSProtocol protocol = null;
                    if (process.objectExists(eventClassname)) {
                        Object object = process.getObject(eventClassname);
                        if (object instanceof VSProtocol)
                            protocol = (VSProtocol) process.getObject(eventClassname);
                        else
                            return;
                    } else {
                        protocol = (VSProtocol) VSRegisteredEvents.createEventInstanceByName(eventName, process);
                        process.setObject(eventClassname, protocol);
                    }

                    //new VSProtocolEditor(prefs, frame, protocol);
                }
            }
        });

        panel.add(button, constraints);

        return panel;
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
