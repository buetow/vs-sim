/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
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

package simulator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import core.*;
import events.*;
import events.internal.*;
import prefs.*;
import prefs.editors.*;
import utils.*;

/**
 * The class VSSimulator, an object of this class represents a whole simulation.
 * It may be, that several parallel simulations exist. They are independent
 * fron each other.
 *
 * @author Paul C. Buetow
 */
public class VSSimulator extends JPanel {
    /** the serial version uid */
    private static final long serialversionuid = 1l;

    /** The global text fields. */
    private ArrayList<String> globalTextFields;

    /** The local text fields. */
    private ArrayList<String> localTextFields;

    /** The create tasks array list. */
    private ArrayList<VSCreateTask> createTasks;

    /** The filter active check box. */
    private JCheckBox filterActiveCheckBox;

    /** The lamport active check box. */
    private JCheckBox lamportActiveCheckBox;

    /** The vector time active check box. */
    private JCheckBox vectorTimeActiveCheckBox;

    /** The global pid combo box. */
    private JComboBox globalPIDComboBox;

    /** The local pid combo box. */
    private JComboBox localPIDComboBox;

    /** The processes combo box. */
    private JComboBox processesComboBox;

    /** The global add panel. */
    private JPanel globalAddPanel;

    /** The local add panel. */
    private JPanel localAddPanel;

    /** The local panel. */
    private JPanel localPanel;

    /** The logging panel. */
    private JPanel loggingPanel;

    /** The tools panel. */
    private JPanel toolsPanel;

    /** The split pane1. */
    private JSplitPane splitPane1;

    /** The split pane h. */
    private JSplitPane splitPaneH;

    /** The split pane v. */
    private JSplitPane splitPaneV;

    /** The tabbed pane. */
    private JTabbedPane tabbedPane;

    /** The logging area. */
    private JTextArea loggingArea;

    /** The filter text field. */
    private JTextField filterTextField;

    /** The global text field. */
    private JTextField globalTextField;

    /** The local text field. */
    private JTextField localTextField;

    /** The thread. */
    private Thread thread;

    /** The logging. */
    private VSLogging logging;

    /** The menu item states. */
    private VSMenuItemStates menuItemStates;

    /** The prefs. */
    private VSPrefs prefs;

    /** The simulation canvas. */
    private VSSimulatorCanvas simulationCanvas;

    /** The simulator frame. */
    private VSSimulatorFrame simulatorFrame;

    /** The task manager. */
    private VSTaskManager taskManager;

    /** The task manager global model. */
    private VSTaskManagerTableModel taskManagerGlobalModel;

    /** The task manager local model. */
    private VSTaskManagerTableModel taskManagerLocalModel;

    /** The simulation has started. */
    private boolean hasStarted = false;

    /** The last selected process num. */
    private int lastSelectedProcessNum;

    /** The last expert state. */
    private boolean lastExpertState;

    /** The simulation counter. */
    private static int simulationCounter;

    /** The simulation num. */
    private static int simulationNum;

    /**
     * The class VSTaskManagerTableModel, an object of this class handles
     * the task manager's JTable.
     */
    private class VSTaskManagerTableModel extends AbstractTableModel
                implements MouseListener {
        /** the serial version uid */
        private static final long serialversionuid = 1l;

        /** The Constant LOCAL. */
        public static final boolean LOCAL = true;

        /** The Constant GLOBAL. */
        public static final boolean GLOBAL = false;

        /** The Constant ALL_PROCESSES. */
        public static final boolean ALL_PROCESSES = true;

        /** The Constant ONE_PROCESS. */
        public static final boolean ONE_PROCESS = false;

        /** The all processes. */
        public boolean allProcesses;

        /** The tasks. */
        private VSPriorityQueue<VSTask> tasks;

        /** The column names. */
        private String columnNames[];

        /** The num columns. */
        private int numColumns;

        /** The table. */
        private JTable table;

        /**
         * Instantiates a new VSTaskManagerTableModel object
         *
         * @param process the process
         * @param localTask true, if this table manages the local task. false,
         *	if this table manages the global tasks.
         */
        public VSTaskManagerTableModel(VSProcess process, boolean localTask) {
            set(process, localTask, ONE_PROCESS);
            columnNames = new String[3];
            columnNames[0]= prefs.getString("lang.time") + " (ms)";
            columnNames[1] = prefs.getString("lang.process.id");
            columnNames[2] = prefs.getString("lang.event");
            numColumns = 3;
        }

        /**
         * Sets the table.
         *
         * @param table the table
         */
        public void setTable(JTable table) {
            this.table = table;
        }

        /**
         * Sets new values.
         *
         * @param process the process
         * @param localTasks true, if this table manages the local tasks. false
         *	if this table manages the global tasks.
         * @param allProcesses true, if this table shows tasks of all processes.
         *	false, if this table only shows tasks of the specified process.
         */
        public void set(VSProcess process, boolean localTasks,
                        boolean allProcesses) {
            this.allProcesses = allProcesses;

            if (allProcesses) {
                this.tasks = localTasks
                             ?  taskManager.getLocalTasks()
                             :  taskManager.getGlobalTasks();
            } else {
                this.tasks = localTasks
                             ?  taskManager.getProcessLocalTasks(process)
                             :  taskManager.getProcessGlobalTasks(process);
            }

            fireTableDataChanged();
        }

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int col) {
            return columnNames[col];
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return tasks == null ? 0 : tasks.size();
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return numColumns;
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            VSTask task = tasks.get(row);

            switch (col) {
            case 0:
                return task.getTaskTime();
            case 1:
                return task.getProcess().getProcessID();
            }

            return task.getEvent().getShortname();
        }

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#setValueAt(
         *	java.lang.Object, int, int)
         */
        public void setValueAt(Object value, int row, int col) {
            fireTableDataChanged();
        }

        /**
         * Adds the task.
         *
         * @param task the task
         */
        public void addTask(VSTask task) {
            tasks.add(task);
            fireTableDataChanged();
        }

        /**
         * Removes the task at a specified row.
         *
         * @param row the row
         */
        private void removeTaskAtRow(int row) {
            VSTask task = tasks.get(row);
            tasks.remove(task);
            taskManager.removeTask(task);
            fireTableDataChanged();
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseClicked(
         *	java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent me) {
            JTable source = (JTable) me.getSource();
            final int row = source.rowAtPoint(me.getPoint());
            final int col = source.columnAtPoint(me.getPoint());

            if (SwingUtilities.isRightMouseButton(me)) {
                ActionListener actionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String command = ae.getActionCommand();
                        if (command.equals(prefs.getString("lang.remove"))) {
                            removeTaskAtRow(row);
                        }
                    }
                };

                JPopupMenu popup = new JPopupMenu();
                JMenuItem item = new JMenuItem(prefs.getString("lang.remove"));
                item.addActionListener(actionListener);
                popup.add(item);

                popup.show(me.getComponent(), me.getX(), me.getY());
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseEntered(
         *	java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent me) { }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(
         *	java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent me) { }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mousePressed(
         *	java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent me) { }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseReleased(
         *	java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent me) { }
    }


    /**
     * Instantiates a new VSSimulator object.
     *
     * @param prefs the prefs
     * @param simulatorFrame the simulator frame
     */
    public VSSimulator(VSPrefs prefs, VSSimulatorFrame simulatorFrame) {
        this.prefs = prefs;
        this.simulatorFrame = simulatorFrame;
        this.logging = new VSLogging();
        this.simulationNum = ++simulationCounter;
        this.menuItemStates = new VSMenuItemStates(false, false, false, true);
        this.localTextFields = new ArrayList<String>();
        this.globalTextFields = new ArrayList<String>();

        logging.logg(prefs.getString("lang.simulation.new"));
        fillContentPane();
        updateFromPrefs();
        splitPaneH.setDividerLocation(
            prefs.getInteger("div.window.splitsize"));

        splitPaneV.setDividerLocation(
            prefs.getInteger("div.window.ysize")
            - prefs.getInteger("div.window.loggsize"));

        splitPane1.setDividerLocation((int) (getPaintSize()/2) - 20);

        int numProcesses = simulationCanvas.getNumProcesses();
        for (int i = 0; i <= numProcesses; ++i) {
            localTextFields.add("0000");
            globalTextFields.add("0000");
        }

        processesComboBox.setSelectedIndex(0);
        localPIDComboBox.setSelectedIndex(0);
        globalPIDComboBox.setSelectedIndex(0);

        thread = new Thread(simulationCanvas);
        thread.start();
    }

    /**
     * Fills the content pane.
     */
    private void fillContentPane() {
        loggingArea = logging.getLoggingArea();

        splitPaneH = new JSplitPane();
        splitPaneV = new JSplitPane();

        simulationCanvas = new VSSimulatorCanvas(prefs, this, logging);
        taskManager = simulationCanvas.getTaskManager();
        logging.setSimulationCanvas(simulationCanvas);

        JPanel canvasPanel = new JPanel();
        canvasPanel.setLayout(new GridLayout(1, 1, 3, 3));
        canvasPanel.add(simulationCanvas);
        canvasPanel.setMinimumSize(new Dimension(0, 0));
        canvasPanel.setMaximumSize(new Dimension(0, 0));

        loggingPanel = new JPanel(new BorderLayout());
        loggingPanel.add(new JScrollPane(loggingArea), BorderLayout.CENTER);
        loggingPanel.add(createToolsPanel(), BorderLayout.SOUTH);
        loggingPanel.setPreferredSize(new Dimension(200, 1));

        splitPaneH.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneH.setLeftComponent(createProcessPanel());
        splitPaneH.setRightComponent(canvasPanel);
        splitPaneH.setContinuousLayout(true);
        splitPaneH.setOneTouchExpandable(true);

        splitPaneV.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneV.setTopComponent(splitPaneH);
        splitPaneV.setBottomComponent(loggingPanel);
        splitPaneV.setContinuousLayout(true);

        this.add(splitPaneV);
    }

    /**
     * Creates the tools panel.
     *
     * @return the panel
     */
    private JPanel createToolsPanel() {
        JPanel toolsPanel = new JPanel();
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
        JCheckBox expertActiveCheckBox =
            new JCheckBox(prefs.getString("lang.mode.expert"));

        expertActiveCheckBox.setSelected(expertMode);
        expertActiveCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton =
                    (AbstractButton) ce.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                boolean newState = buttonModel.isSelected();
                if (lastExpertState != newState) {
                    lastExpertState = newState;
                    prefs.setBoolean("sim.mode.expert", newState);
                    fireExpertModeChanged();
                }
            }
        });
        toolsPanel.add(expertActiveCheckBox);

        if (expertMode) {
            lamportActiveCheckBox = new JCheckBox(
                prefs.getString("lang.time.lamport"));
            lamportActiveCheckBox.setSelected(false);
            lamportActiveCheckBox.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton =
                        (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    simulationCanvas.showLamport(buttonModel.isSelected());
                    if (buttonModel.isSelected())
                        vectorTimeActiveCheckBox.setSelected(false);
                }
            });
            toolsPanel.add(lamportActiveCheckBox);

            vectorTimeActiveCheckBox = new JCheckBox(
                prefs.getString("lang.time.vector"));
            vectorTimeActiveCheckBox.setSelected(false);
            vectorTimeActiveCheckBox.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton =
                        (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    simulationCanvas.showVectorTime(buttonModel.isSelected());
                    if (buttonModel.isSelected())
                        lamportActiveCheckBox.setSelected(false);
                }
            });
            toolsPanel.add(vectorTimeActiveCheckBox);

            JCheckBox antiAliasing = new JCheckBox(
                prefs.getString("lang.antialiasing"));
            antiAliasing.setSelected(false);
            antiAliasing.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton =
                        (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    simulationCanvas.isAntiAliased(buttonModel.isSelected());
                }
            });
            toolsPanel.add(antiAliasing);
        }

        JCheckBox loggingActiveCheckBox = new JCheckBox(
            prefs.getString("lang.logging.active"));
        loggingActiveCheckBox.setSelected(true);
        loggingActiveCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton =
                    (AbstractButton) ce.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                logging.isPaused(!buttonModel.isSelected());
            }
        });
        toolsPanel.add(loggingActiveCheckBox);

        if (expertMode) {
            filterActiveCheckBox = new JCheckBox(
                prefs.getString("lang.filter"));
            filterActiveCheckBox.setSelected(false);
            filterActiveCheckBox.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton =
                        (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    logging.isFiltered(buttonModel.isSelected());
                    if (buttonModel.isSelected())
                        logging.setFilterText(filterTextField.getText());
                }
            });
            toolsPanel.add(filterActiveCheckBox);

            filterTextField = new JTextField();
            filterTextField.getDocument().addDocumentListener(
            new DocumentListener() {
                public void insertUpdate(DocumentEvent de) {
                    logging.setFilterText(filterTextField.getText());
                }
                public void removeUpdate(DocumentEvent de) {
                    logging.setFilterText(filterTextField.getText());
                }
                public void changedUpdate(DocumentEvent de) {
                    logging.setFilterText(filterTextField.getText());
                }
            });
            toolsPanel.add(filterTextField);

            JButton clearButton = new JButton(
                prefs.getString("lang.logging.clear"));
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String command = ae.getActionCommand();
                    if (command.equals(
                    prefs.getString("lang.logging.clear"))) {
                        logging.clear();
                    }
                }
            });
            toolsPanel.add(clearButton);
        }

        return toolsPanel;
    }

    /**
     * Creates the process panel.
     *
     * @return the panel
     */
    private JPanel createProcessPanel() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        boolean expertMode = prefs.getBoolean("sim.mode.expert");
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

        processesComboBox = new JComboBox();
        localPIDComboBox = new JComboBox();
        globalPIDComboBox = new JComboBox();

        lastSelectedProcessNum = 0;
        int numProcesses = simulationCanvas.getNumProcesses();
        String processString = prefs.getString("lang.process");

        for (int i = 0; i < numProcesses; ++i) {
            int pid = simulationCanvas.getProcess(i).getProcessID();
            processesComboBox.addItem(processString + " " + pid);
            localPIDComboBox.addItem("PID: " + pid);
            globalPIDComboBox.addItem("PID: " + pid);
        }

        processesComboBox.addItem(prefs.getString("lang.processes.all"));
        localPIDComboBox.addItem(prefs.getString("lang.all"));
        globalPIDComboBox.addItem(prefs.getString("lang.all"));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP,
                                     JTabbedPane.WRAP_TAB_LAYOUT);
        localPanel = createTaskLabel(VSTaskManagerTableModel.LOCAL);
        JPanel globalPanel = createTaskLabel(VSTaskManagerTableModel.GLOBAL);

        splitPane1 = new JSplitPane();
        splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane1.setTopComponent(localPanel);
        splitPane1.setBottomComponent(globalPanel);
        splitPane1.setOneTouchExpandable(true);

        if (expertMode)
            tabbedPane.addTab(prefs.getString("lang.events"), splitPane1);

        else
            tabbedPane.addTab(prefs.getString("lang.events"), localPanel);

        processesComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                localTextFields.set(lastSelectedProcessNum,
                                    localTextField.getText());
                globalTextFields.set(lastSelectedProcessNum,
                                     globalTextField.getText());
                updateTaskManagerTable();

                int processNum = getSelectedProcessNum();
                localTextField.setText(localTextFields.get(processNum));
                globalTextField.setText(globalTextFields.get(processNum));
                localTextField.setBackground(Color.WHITE);
                globalTextField.setBackground(Color.WHITE);
                lastSelectedProcessNum = processNum;

                localPIDComboBox.setSelectedIndex(processNum);
                globalPIDComboBox.setSelectedIndex(processNum);

                if (processNum == simulationCanvas.getNumProcesses()) {
                    tabbedPane.setEnabledAt(1, false);
                    if (tabbedPane.getSelectedIndex() == 1)
                        tabbedPane.setSelectedIndex(0);

                } else if (!tabbedPane.isEnabledAt(1)) {
                    tabbedPane.setEnabledAt(1, true);
                }

                if (processNum != simulationCanvas.getNumProcesses()) {
                    VSProcess process = getSelectedProcess();
                    VSProcessEditor processEditor =
                        new VSProcessEditor(prefs, process);
                    tabbedPane.setComponentAt(1,
                                              processEditor.getContentPane());
                }
            }
        });

        tabbedPane.add(prefs.getString("lang.variables"), null);

        editPanel.add(processesComboBox);
        editPanel.add(tabbedPane);

        return editPanel;
    }

    /**
     * Creates the label panel.
     *
     * @param text the text
     *
     * @return the panel
     */
    private JPanel createLabelPanel(String text) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);
        panel.add(label);

        return panel;
    }

    /**
     * Creates the task label.
     *
     * @param localTasks true, if the local task label has to get created.
     *	false, if the global task label has to get created.
     *
     * @return the panel
     */
    private JPanel createTaskLabel(boolean localTasks) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (localTasks)
            panel.add(createLabelPanel(prefs.getString("lang.timed.local")));
        else
            panel.add(createLabelPanel(prefs.getString("lang.timed.global")));

        JScrollPane scrollPane = new JScrollPane(createTaskTable(localTasks));
        panel.add(scrollPane);

        if (localTasks)
            localAddPanel = initAddPanel(panel, localTasks);
        else
            globalAddPanel = initAddPanel(panel, localTasks);

        return panel;
    }

    /**
     * Creates the task table.
     *
     * @param localTasks true, if the local task label has to get created.
     *	false, if the global task label has to get created.
     *
     * @return the table
     */
    private JTable createTaskTable(boolean localTasks) {
        VSProcess process = getSelectedProcess();
        VSTaskManagerTableModel model =
            new VSTaskManagerTableModel(process, localTasks);

        if (localTasks)
            taskManagerLocalModel = model;
        else
            taskManagerGlobalModel = model;

        JTable table = new JTable(model);
        model.setTable(table);
        table.addMouseListener(model);

        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMaxWidth(62);
        col.setResizable(false);

        col = table.getColumnModel().getColumn(1);
        col.setMaxWidth(23);
        col.setResizable(false);

        col = table.getColumnModel().getColumn(2);
        col.sizeWidthToFit();
        table.setBackground(Color.WHITE);

        return table;
    }

    /**
     * Inits the add panel.
     *
     * @param panel the panel
     * @param localTasks true, if the local task label has to get created.
     *	false, if the global task label has to get created.
     *
     * @return the panel
     */
    private JPanel initAddPanel(JPanel panel, final boolean localTasks) {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        final JTextField textField = new JTextField();
        if (localTasks)
            localTextField = textField;
        else
            globalTextField = textField;

        textField.setText("0000");
        textField.setBackground(Color.WHITE);
        addPanel.add(textField);

        addPanel.add(new JLabel(" ms "));

        if (localTasks) {
            if (expertMode)
                addPanel.add(localPIDComboBox);
        } else {
            addPanel.add(globalPIDComboBox);
        }

        final JComboBox comboBox = new JComboBox();
        JButton takeoverButton = new JButton(prefs.getString("lang.takeover"));
        takeoverButton.addActionListener(new ActionListener() {
            private boolean isRed;
            public void actionPerformed(ActionEvent ae) {
                String textValue = textField.getText();
                Long longValue = null;

                try {
                    longValue = Long.valueOf(textValue);

                    if (longValue.longValue() < 0) {
                        makeRed();
                        return;
                    }

                    if (isRed) {
                        makeWhite();
                    }

                } catch (NumberFormatException e) {
                    makeRed();
                }

                if (longValue == null)
                    return;

                if (takeover(longValue.longValue())) {
                    if (isRed)
                        makeWhite();

                } else {
                    makeRed();
                }
            }

            private void makeWhite() {
                textField.setBackground(Color.WHITE);
                isRed = false;
            }

            private void makeRed() {
                textField.setBackground(Color.RED);
                isRed = true;
            }

            private boolean takeover(long time) {
                VSProcess selectedProcess = getSelectedProcess();
                int index = comboBox.getSelectedIndex();
                VSCreateTask createTask = createTasks.get(index);

                if (createTask == null)
                    return false;

                ArrayList<VSProcess> processes =
                    getConcernedProcesses(localTasks);

                for (VSProcess process : processes) {
                    VSTask task = createTask.createTask(process, time,
                                                        localTasks);
                    taskManager.addTask(task, VSTaskManager.PROGRAMMED);

                    if (selectedProcess == null ||
                    process.equals(selectedProcess)) {
                        if (localTasks)
                            taskManagerLocalModel.addTask(task);
                        else
                            taskManagerGlobalModel.addTask(task);
                    }
                }

                return true;
            }
        });

        addPanel.add(takeoverButton);

        boolean createTaskFlag = createTasks == null;
        if (createTaskFlag) createTasks = new ArrayList<VSCreateTask>();

        Vector<String> eventClassnames =
            VSRegisteredEvents.getNonProtocolClassnames();

        comboBox.setMaximumRowCount(20);
        comboBox.addItem("----- " + prefs.getString("lang.events.process") +
                         " -----");
        if (createTaskFlag)
            createTasks.add(null);

        for (String eventClassname : eventClassnames) {
            String eventShortname =
                VSRegisteredEvents.getShortnameByClassname(eventClassname);
            comboBox.addItem(eventShortname);
            if (createTaskFlag)
                createTasks.add(new VSCreateTask(eventClassname));
        }

        String activate = prefs.getString("lang.activate");
        String client = prefs.getString("lang.client");
        String clientRequest = prefs.getString("lang.clientrequest.start");
        String deactivate = prefs.getString("lang.deactivate");
        String server = prefs.getString("lang.server");
        String serverRequest = prefs.getString("lang.serverrequest.start");
        String protocol = prefs.getString("lang.protocol");

        String protocolEventClassname = "events.internal.VSProtocolEvent";
        eventClassnames = VSRegisteredEvents.getProtocolClassnames();

        for (String eventClassname : eventClassnames) {
            String eventShortname_ =
                VSRegisteredEvents.getShortnameByClassname(eventClassname);
            String eventShortname = null;

            comboBox.addItem("----- " + eventShortname_ + " " +
                             protocol + " -----");
            if (createTaskFlag)
                createTasks.add(null);

            if (VSRegisteredEvents.isOnServerStartProtocol(eventClassname))
                eventShortname = eventShortname_ + " " + serverRequest;
            else
                eventShortname = eventShortname_ + " " + clientRequest;

            comboBox.addItem(eventShortname);
            if (createTaskFlag) {
                VSCreateTask createTask = new VSCreateTask(eventClassname);
                createTask.setShortname(eventShortname);
                createTask.isRequest(true);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + client + " " + activate;
            comboBox.addItem(eventShortname);
            if (createTaskFlag) {
                VSCreateTask createTask =
                    new VSCreateTask(protocolEventClassname);
                createTask.isProtocolActivation(true);
                createTask.isClientProtocol(true);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + client + " " + deactivate;
            comboBox.addItem(eventShortname);
            if (createTaskFlag) {
                VSCreateTask createTask =
                    new VSCreateTask(protocolEventClassname);
                createTask.isProtocolDeactivation(true);
                createTask.isClientProtocol(true);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + server + " " + activate;
            comboBox.addItem(eventShortname);
            if (createTaskFlag) {
                VSCreateTask createTask =
                    new VSCreateTask(protocolEventClassname);
                createTask.isProtocolActivation(true);
                createTask.isClientProtocol(false);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + server + " " + deactivate;
            comboBox.addItem(eventShortname);
            if (createTaskFlag) {
                VSCreateTask createTask =
                    new VSCreateTask(protocolEventClassname);
                createTask.isProtocolDeactivation(true);
                createTask.isClientProtocol(false);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }
        }

        panel.add(comboBox);
        panel.add(addPanel);

        return addPanel;
    }

    /**
     * Gets the split size.
     *
     * @return the split size
     */
    public int getSplitSize() {
        return splitPaneH.getDividerLocation();
    }

    /**
     * Gets the paint size.
     *
     * @return the paint size
     */
    public int getPaintSize() {
        return splitPaneV.getDividerLocation();
    }

    /**
     * Gets the selected process num.
     *
     * @return the selected process num
     */
    private int getSelectedProcessNum() {
        return processesComboBox.getSelectedIndex();
    }

    /**
     * Gets the selected process.
     *
     * @return the selected process
     */
    private VSProcess getSelectedProcess() {
        int processNum = getSelectedProcessNum();
        return simulationCanvas.getProcess(processNum);
    }

    /**
     * Gets the concerned processes.
     *
     * @param localTasks true, if this table manages the local tasks. false
     *	if this table manages the global tasks.
     *
     * @return the concerned processes
     */
    private ArrayList<VSProcess> getConcernedProcesses(boolean localTasks) {
        int processNum = localTasks
                         ? localPIDComboBox.getSelectedIndex()
                         : globalPIDComboBox.getSelectedIndex();

        if (processNum == simulationCanvas.getNumProcesses())
            return simulationCanvas.getProcessesArray();

        ArrayList<VSProcess> arr = new ArrayList<VSProcess>();
        arr.add(simulationCanvas.getProcess(processNum));

        return arr;
    }

    /**
     * Update task manager table.
     */
    public void updateTaskManagerTable() {
        VSProcess process = getSelectedProcess();
        boolean allProcesses = process == null;
        taskManagerLocalModel.set(process,
                                  VSTaskManagerTableModel.LOCAL, allProcesses);
        taskManagerGlobalModel.set(process,
                                   VSTaskManagerTableModel.GLOBAL, allProcesses);
    }

    /**
     * The simulation has finished.
     */
    public void finish() {
        menuItemStates.setStart(false);
        menuItemStates.setPause(false);
        menuItemStates.setReset(true);
        menuItemStates.setReplay(true);
        simulatorFrame.updateSimulationMenu();
    }

    /**
     * Gets the simulation num.
     *
     * @return the simulation num
     */
    public int getSimulationNum() {
        return simulationNum;
    }

    /**
     * Gets the menu item states.
     *
     * @return the menu item states
     */
    public VSMenuItemStates getMenuItemStates() {
        return menuItemStates;
    }

    /**
     * Gets the simulation canvas.
     *
     * @return the simulation canvas
     */
    public VSSimulatorCanvas getSimulatorCanvas() {
        return simulationCanvas;
    }

    /**
     * Gets the simulator frame.
     *
     * @return the simulator frame
     */
    public VSSimulatorFrame getSimulatorFrame() {
        return simulatorFrame;
    }

    /**
     * Update from prefs.
     */
    public void updateFromPrefs() {
        simulationCanvas.setBackground(prefs.getColor("col.background"));
        simulationCanvas.updateFromPrefs();
    }

    /**
     * Removes the process at a specified index.
     *
     * @param index the index
     */
    public void removedAProcessAtIndex(int index) {
        if (lastSelectedProcessNum > index)
            --lastSelectedProcessNum;

        globalTextFields.remove(index);
        localTextFields.remove(index);

        globalPIDComboBox.removeItemAt(index);
        localPIDComboBox.removeItemAt(index);

        processesComboBox.removeItemAt(index);
        simulatorFrame.updateEditMenu();

        updateTaskManagerTable();
    }

    /**
     * Adds the process at a specified index.
     *
     * @param index the index
     */
    public void addProcessAtIndex(int index) {
        int processID = simulationCanvas.getProcess(index).getProcessID();
        String processString = prefs.getString("lang.process");

        localTextFields.add(index, "0000");
        globalTextFields.add(index, "0000");

        localPIDComboBox.insertItemAt("PID: " + processID, index);
        globalPIDComboBox.insertItemAt("PID: " + processID, index);

        processesComboBox.insertItemAt(processString + " " + processID, index);
        simulatorFrame.updateEditMenu();
    }

    /**
     * Fire expert mode changed. Tell, that the expert mode has changed.
     */
    public void fireExpertModeChanged() {
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        /* Update the Task Manager GUI */
        int selectedIndex = tabbedPane.getSelectedIndex();

        if (expertMode) {
            tabbedPane.remove(localPanel);
            tabbedPane.insertTab(prefs.getString("lang.events"), null,
                                 splitPane1, null, 0);
            splitPane1.setTopComponent(localPanel);
            //splitPane1.setDividerLocation((int) (getPaintSize()/2) - 20);

            /* addPanel */
            localAddPanel.add(localPIDComboBox, 2);

        } else {
            tabbedPane.remove(splitPane1);
            tabbedPane.insertTab(prefs.getString("lang.events"), null,
                                 localPanel, null, 0);

            /* addPanel */
            localAddPanel.remove(2);
        }

        tabbedPane.setSelectedIndex(selectedIndex);

        /* Update the 'Variables tab' */
        if (getSelectedProcessNum() != simulationCanvas.getNumProcesses()) {
            VSProcess process = getSelectedProcess();
            VSProcessEditor editor = new VSProcessEditor(prefs, process);
            tabbedPane.setComponentAt(1, editor.getContentPane());
        }

        /* Update the tools panel */
        loggingPanel.remove(1);
        loggingPanel.add(createToolsPanel(), BorderLayout.SOUTH);
        updateUI();
    }

    /**
     * Gets the prefs.
     *
     * @return the prefs
     */
    public VSPrefs getPrefs() {
        return prefs;
    }
}
