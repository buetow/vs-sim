package editors;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import protocols.*;
import utils.*;
import core.*;
import prefs.VSPrefs;

public class VSProtocolEditor extends VSEditorFrame {
    private JCheckBox clientCheckBox;
    private JCheckBox serverCheckBox;
    private JComboBox clientComboBox;
    private VSProtocol protocol;
    private VSTaskManager taskManager;
    private JPanel clientTaskManagerEditorPanel;
    private JButton takeOverButton;
    private JButton deleteButton;
    private JTextField textField;

    public VSProtocolEditor(VSPrefs prefs, Component relativeTo, VSProtocol protocol) {
        super(prefs, relativeTo, protocol, prefs.getString("name") + " - "
              + protocol.getProtocolName() + " " + prefs.getString("lang.editor"), ALL_PREFERENCES);

        this.protocol = protocol;
        this.taskManager = protocol.getProcess().getSimulationPanel().getTaskManager();

        initialize();
    }

    private void initialize() {
        super.getFrame().disposeWithParent();
        super.infoArea.setText(prefs.getString("lang.prefs.protocol.info!"));
        initializeTaskManagerEditor(clientTaskManagerEditorPanel);
        initlializeClientServerCheckboxes();
        createButtonPanel();
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

    protected void addToEditPanelFront(JPanel editPanel) {
        super.addToEditPanelFront(editPanel);;

        editPanelConstraints.gridy = editPanelRow++;
        editPanel.add(new JLabel(prefs.getString("lang.protocol.tasks.activation")), editPanelConstraints);

        editPanelConstraints.insets = insets;
        editPanelConstraints.gridy = editPanelRow++;
        editPanel.add(createClientServerCheckboxes(), editPanelConstraints);

        editPanelConstraints.insets = insetsTopSpaceing;
        editPanelConstraints.gridy = editPanelRow++;
        editPanel.add(new JLabel(prefs.getString("lang.protocol.tasks.client")), editPanelConstraints);
        clientComboBox = new JComboBox();
        clientTaskManagerEditorPanel = new JPanel(new GridBagLayout());
        editPanelConstraints.insets = insets;
        editPanelConstraints.gridy = editPanelRow++;
        editPanel.add(clientTaskManagerEditorPanel, editPanelConstraints);

        editPanelConstraints.insets = insetsTopSpaceing;
        editPanelConstraints.gridy = editPanelRow++;
    }

    private JPanel createClientServerCheckboxes() {
        final String activated = prefs.getString("lang.activate");
        final String client = prefs.getString("lang.protocol.client") + " " + activated;
        final String server = prefs.getString("lang.protocol.server") + " " + activated;

        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        int row = 0;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 10;
        constraints.ipadx = 10;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridy = row++;
        constraints.gridx = 0;
        panel.add(new JLabel(client), constraints);

        constraints.gridx = 1;
        clientCheckBox = new JCheckBox();
        clientCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                if (takeOverButton != null && textField != null) {
                    AbstractButton abstractButton = (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    takeOverButton.setEnabled(buttonModel.isSelected());
                    textField.setEnabled(buttonModel.isSelected());
                    if (!buttonModel.isSelected()) {
                        clientComboBox.setEnabled(false);
                        deleteButton.setEnabled(false);
                    } else if (clientComboBox.getItemCount() > 0) {
                        clientComboBox.setEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                }
            }
        });

        panel.add(clientCheckBox, constraints);

        constraints.gridy = row++;
        constraints.gridx = 0;
        panel.add(new JLabel(server), constraints);

        constraints.gridx = 1;
        serverCheckBox = new JCheckBox();
        panel.add(serverCheckBox, constraints);

        return panel;
    }

    private void initlializeClientServerCheckboxes() {
        final String protocolName = protocol.getProtocolName();
        final VSProcess process = protocol.getProcess();

        String protocolKey = "sim."+protocolName.toLowerCase()+".client.enabled!";
        clientCheckBox.setSelected(process.getBoolean(protocolKey));
        protocolKey = "sim."+protocolName.toLowerCase()+".server.enabled!";
        serverCheckBox.setSelected(process.getBoolean(protocolKey));

    }

    private void initializeTaskManagerEditor(JPanel panel) {
        clientComboBox = new JComboBox();
        deleteButton = new JButton(prefs.getString("lang.remove"));
        takeOverButton = new JButton(prefs.getString("lang.takeover"));
        textField = new JTextField();

        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        int row = 0;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 10;
        constraints.ipadx = 10;
        constraints.insets = new Insets(5, 5, 5, 5);

        textField.setText("0000");
        textField.setColumns(10);
        constraints.gridy = row++;
        constraints.gridx = 0;
        panel.add(textField, constraints);

        Insets insetsBackup = constraints.insets;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 1;
        panel.add(new JLabel("ms"), constraints);

        constraints.insets = insetsBackup;
        constraints.gridx = 2;
        panel.add(takeOverButton, constraints);

        constraints.gridy = row++;
        constraints.gridx = 0;
        resetTaskManager();
        clientComboBox.setBackground(Color.WHITE);
        panel.add(clientComboBox, constraints);

        constraints.gridx = 2;
        panel.add(deleteButton, constraints);

        ActionListener actionListener = new ActionListener() {
            private boolean isRed;
            public void actionPerformed(ActionEvent ae) {
                if (ae.getActionCommand().equals(prefs.getString("lang.takeover"))) {
                    String textValue = textField.getText();
                    try {
                        Long longValue = Long.valueOf(textValue);

                        if (longValue.longValue() < 0) {
                            textField.setBackground(Color.RED);
                            isRed = true;
                            return;
                        }

                        clientComboBox.addItem(VSTools.getTimeString(longValue.longValue()));
                        clientComboBox.setSelectedIndex(clientComboBox.getItemCount()-1);
                        clientComboBox.setEnabled(true);
                        sortComboBox(clientComboBox);
                        deleteButton.setEnabled(true);

                        if (isRed) {
                            textField.setBackground(Color.WHITE);
                            isRed = false;
                        }
                    } catch (NumberFormatException e) {
                        textField.setBackground(Color.RED);
                        isRed = true;
                    }

                } else if (ae.getActionCommand().equals(prefs.getString("lang.remove"))) {
                    Object[] objects = clientComboBox.getSelectedObjects();
                    for (Object object : objects)
                        clientComboBox.removeItem(object);
                    if (clientComboBox.getItemCount() == 0) {
                        clientComboBox.setEnabled(false);
                        deleteButton.setEnabled(false);
                    }
                }
            }
        };

        takeOverButton.addActionListener(actionListener);
        deleteButton.addActionListener(actionListener);

        clientComboBox.setEnabled(false);
        deleteButton.setEnabled(false);
        takeOverButton.setEnabled(false);
        textField.setEnabled(false);
    }

    protected void resetEditPanel() {
        super.resetEditPanel();

        resetTaskManager();

        final VSProcess process = protocol.getProcess();
        final String protocolName = protocol.getProtocolName();
        String protocolKey = "sim."+protocolName.toLowerCase()+".client.enabled!";
        clientCheckBox.setSelected(process.getBoolean(protocolKey));
        protocolKey = "sim."+protocolName.toLowerCase()+".server.enabled!";
        serverCheckBox.setSelected(process.getBoolean(protocolKey));

        takeOverButton.setEnabled(clientCheckBox.isSelected());
        textField.setEnabled(clientCheckBox.isSelected());
        if (!clientCheckBox.isSelected()) {
            clientComboBox.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    protected void savePrefs() {
        super.savePrefs();
        saveTasks();
    }

    private void resetTaskManager() {
        clientComboBox.removeAllItems();
        LinkedList<VSTask> protocolVSTasks = taskManager.getProtocolTasks(protocol);

        for (VSTask task : protocolVSTasks)
            clientComboBox.addItem(VSTools.getTimeString(task.getTaskTime()));
    }

    private void saveTasks() {
        LinkedList<VSTask> tasks = new LinkedList<VSTask>();
        int numItems;

        numItems = clientComboBox.getItemCount();
        for (int i = 0; i < numItems; ++i) {
            long taskTime = VSTools.getStringTime((String) clientComboBox.getItemAt(i));
            VSTask task = new VSTask(taskTime, protocol.getProcess(), protocol);
            task.isProgrammed(true);
            tasks.addLast(task);
        }

        taskManager.modifyProtocolTasks(protocol, tasks);

        final VSProcess process = protocol.getProcess();
        final String protocolName = protocol.getProtocolName();
        String protocolKey = "sim."+protocolName.toLowerCase()+".client.enabled!";
        process.setBoolean(protocolKey, clientCheckBox.isSelected());
        protocol.isClient(clientCheckBox.isSelected());

        protocolKey = "sim."+protocolName.toLowerCase()+".server.enabled!";
        process.setBoolean(protocolKey, serverCheckBox.isSelected());
        protocol.isServer(serverCheckBox.isSelected());

        Object protocolsObj = null;
        if (process.objectExists("protocols.registered")) {
            protocolsObj = process.getObject("protocols.registered");
        } else {
            protocolsObj = new Vector<VSProtocol>();
            process.setObject("protocols.registered", protocolsObj);
        }

        if (protocolsObj instanceof Vector) {
            Vector<VSProtocol> protocols = (Vector<VSProtocol>) protocolsObj;
            if (!protocols.contains(protocol))
                protocols.add(protocol);
        }
    }

    private void sortComboBox(JComboBox comboBox) {
        Object selected = comboBox.getSelectedItem();
        int numItems = comboBox.getItemCount();
        Vector<String> vector = new Vector<String>();

        for (int i = 0; i < numItems; ++i) {
            String value = (String) comboBox.getItemAt(i);
            vector.add(value);
        }

        Collections.sort(vector);
        comboBox.removeAllItems();
        for (String value : vector)
            comboBox.addItem(value);
        comboBox.setSelectedItem(selected);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            frame.dispose();

        } else if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
