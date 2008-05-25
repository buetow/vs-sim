/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
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

// TODO: Auto-generated Javadoc
/**
 * The Class VSSimulator.
 */
public class VSSimulator extends JPanel {

    /** The global text fields. */
    private ArrayList<String> globalTextFields;

    /** The local text fields. */
    private ArrayList<String> localTextFields;

    /** The create tasks. */
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

    /** The has started. */
    private boolean hasStarted = false;

    /** The last selected process num. */
    private int lastSelectedProcessNum;

    /** The simulation counter. */
    private static int simulationCounter;

    /** The simulation num. */
    private static int simulationNum;

    /**
     * The Class VSMenuItemStates.
     */
    public class VSMenuItemStates {

        /** The pause. */
        private volatile boolean pause;

        /** The replay. */
        private volatile boolean replay;

        /** The reset. */
        private volatile boolean reset;

        /** The start. */
        private volatile boolean start;

        /**
         * Instantiates a new vS menu item states.
         *
         * @param pause the pause
         * @param replay the replay
         * @param reset the reset
         * @param start the start
         */
        public VSMenuItemStates(boolean pause, boolean replay, boolean reset, boolean start) {
            this.pause = pause;
            this.replay = replay;
            this.reset = reset;
            this.start = start;
        }

        /**
         * Sets the pause.
         *
         * @param pause the new pause
         */
        public void setPause(boolean pause) {
            this.pause = pause;
        }

        /**
         * Sets the replay.
         *
         * @param replay the new replay
         */
        public void setReplay(boolean replay) {
            this.replay = replay;
        }

        /**
         * Sets the reset.
         *
         * @param reset the new reset
         */
        public void setReset(boolean reset) {
            this.reset = reset;
        }

        /**
         * Sets the start.
         *
         * @param start the new start
         */
        public void setStart(boolean start) {
            this.start = start;
        }

        /**
         * Gets the pause.
         *
         * @return the pause
         */
        public boolean getPause() {
            return pause;
        }

        /**
         * Gets the replay.
         *
         * @return the replay
         */
        public boolean getReplay() {
            return replay;
        }

        /**
         * Gets the reset.
         *
         * @return the reset
         */
        public boolean getReset() {
            return reset;
        }

        /**
         * Gets the start.
         *
         * @return the start
         */
        public boolean getStart() {
            return start;
        }
    }

    /**
     * Instantiates a new vS simulator.
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
     * Fill content pane.
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
        splitPaneH.setLeftComponent(createProcessPane());
        splitPaneH.setRightComponent(canvasPanel);
        splitPaneH.setContinuousLayout(true);
        splitPaneH.setOneTouchExpandable(true);

        splitPaneV.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneV.setTopComponent(splitPaneH);
        splitPaneV.setBottomComponent(loggingPanel);
        splitPaneV.setContinuousLayout(true);

        this.add(splitPaneV);
    }

    /** The last expert state. */
    private boolean lastExpertState;

    /**
     * Creates the tools panel.
     *
     * @return the j panel
     */
    private JPanel createToolsPanel() {
        JPanel toolsPanel = new JPanel();
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));

        JCheckBox expertActiveCheckBox = new JCheckBox(prefs.getString("lang.mode.expert"));
        expertActiveCheckBox.setSelected(expertMode);
        expertActiveCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
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
            lamportActiveCheckBox = new JCheckBox(prefs.getString("lang.time.lamport"));
            lamportActiveCheckBox.setSelected(false);
            lamportActiveCheckBox.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton = (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    simulationCanvas.showLamport(buttonModel.isSelected());
                    if (buttonModel.isSelected())
                        vectorTimeActiveCheckBox.setSelected(false);
                }
            });
            toolsPanel.add(lamportActiveCheckBox);

            vectorTimeActiveCheckBox = new JCheckBox(prefs.getString("lang.time.vector"));
            vectorTimeActiveCheckBox.setSelected(false);
            vectorTimeActiveCheckBox.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton = (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    simulationCanvas.showVectorTime(buttonModel.isSelected());
                    if (buttonModel.isSelected())
                        lamportActiveCheckBox.setSelected(false);
                }
            });
            toolsPanel.add(vectorTimeActiveCheckBox);

            JCheckBox antiAliasing = new JCheckBox(prefs.getString("lang.antialiasing"));
            antiAliasing.setSelected(false);
            antiAliasing.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton = (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    simulationCanvas.isAntiAliased(buttonModel.isSelected());
                }
            });
            toolsPanel.add(antiAliasing);
        }

        JCheckBox loggingActiveCheckBox = new JCheckBox(prefs.getString("lang.logging.active"));
        loggingActiveCheckBox.setSelected(true);
        loggingActiveCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                AbstractButton abstractButton = (AbstractButton) ce.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                logging.isPaused(!buttonModel.isSelected());
            }
        });
        toolsPanel.add(loggingActiveCheckBox);

        if (expertMode) {
            filterActiveCheckBox = new JCheckBox(prefs.getString("lang.filter"));
            filterActiveCheckBox.setSelected(false);
            filterActiveCheckBox.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    AbstractButton abstractButton = (AbstractButton) ce.getSource();
                    ButtonModel buttonModel = abstractButton.getModel();
                    logging.isFiltered(buttonModel.isSelected());
                    if (buttonModel.isSelected())
                        logging.setFilterText(filterTextField.getText());
                }
            });
            toolsPanel.add(filterActiveCheckBox);

            filterTextField = new JTextField();
            filterTextField.getDocument().addDocumentListener(new DocumentListener() {
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

            JButton clearButton = new JButton(prefs.getString("lang.logging.clear"));
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String actionCommand = ae.getActionCommand();
                    if (actionCommand.equals(prefs.getString("lang.logging.clear"))) {
                        logging.clear();
                    }
                }
            });
            toolsPanel.add(clearButton);
        }

        return toolsPanel;
    }

    /**
     * Creates the process pane.
     *
     * @return the j panel
     */
    private JPanel createProcessPane() {
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

        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
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
                localTextFields.set(lastSelectedProcessNum, localTextField.getText());
                globalTextFields.set(lastSelectedProcessNum, globalTextField.getText());
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
                    VSProcessEditor processEditor = new VSProcessEditor(prefs, process);
                    tabbedPane.setComponentAt(1, processEditor.getContentPane());
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
     * @return the j panel
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
     * @param localTasks the local tasks
     *
     * @return the j panel
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
     * The Class VSCreateTask.
     */
    private class VSCreateTask {

        /** The event classname. */
        private String eventClassname;

        /** The protocol classname. */
        private String protocolClassname;

        /** The shortname. */
        private String shortname;

        /* Those 3 values are for ProtocolEvent events */
        /** The is protocol activation. */
        private boolean isProtocolActivation;

        /** The is protocol deactivation. */
        private boolean isProtocolDeactivation;

        /** The is client protocol. */
        private boolean isClientProtocol;

        /* Those values are for ProtocolClient onStart events */
        /** The is client request. */
        private boolean isClientRequest;

        /**
         * Instantiates a new vS create task.
         *
         * @param eventClassname the event classname
         */
        public VSCreateTask(String eventClassname) {
            this.eventClassname = eventClassname;
        }

        /**
         * Checks if is protocol activation.
         *
         * @param isProtocolActivation the is protocol activation
         */
        public void isProtocolActivation(boolean isProtocolActivation) {
            this.isProtocolActivation = isProtocolActivation;

            if (isProtocolActivation)
                isProtocolDeactivation(false);
        }

        /**
         * Checks if is protocol deactivation.
         *
         * @param isProtocolDeactivation the is protocol deactivation
         */
        public void isProtocolDeactivation(boolean isProtocolDeactivation) {
            this.isProtocolDeactivation = isProtocolDeactivation;

            if (isProtocolDeactivation)
                isProtocolActivation(false);
        }

        /**
         * Checks if is client protocol.
         *
         * @param isClientProtocol the is client protocol
         */
        public void isClientProtocol(boolean isClientProtocol) {
            this.isClientProtocol = isClientProtocol;
        }

        /**
         * Checks if is client request.
         *
         * @param isClientRequest the is client request
         */
        public void isClientRequest(boolean isClientRequest) {
            this.isClientRequest = isClientRequest;
        }

        /**
         * Sets the protocol classname.
         *
         * @param protocolClassname the new protocol classname
         */
        public void setProtocolClassname(String protocolClassname) {
            this.protocolClassname = protocolClassname;
        }

        /**
         * Sets the shortname.
         *
         * @param shortname the new shortname
         */
        public void setShortname(String shortname) {
            this.shortname = shortname;
        }

        /**
         * Creates the task.
         *
         * @param process the process
         * @param time the time
         * @param localTimedTask the local timed task
         *
         * @return the vS task
         */
        public VSTask createTask(VSProcess process, long time, boolean localTimedTask) {
            VSEvent event = null;

            if (isClientRequest) {
                event = process.getProtocolObject(eventClassname);

            } else {
                event = VSRegisteredEvents.createEventInstanceByClassname(eventClassname, process);
            }

            event.init(process);
            if (shortname != null)
                event.setShortname(shortname);

            if (isProtocolActivation || isProtocolDeactivation) {
                ProtocolEvent protocolEvent = (ProtocolEvent) event;
                protocolEvent.setProtocolClassname(protocolClassname);
                protocolEvent.isProtocolActivation(isProtocolActivation);
                protocolEvent.isClientProtocol(isClientProtocol);
            }

            return new VSTask(time, process, event, localTimedTask);
        }
    }

    /**
     * The Class VSTaskManagerTableModel.
     */
    private class VSTaskManagerTableModel extends AbstractTableModel implements MouseListener {

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
         * Instantiates a new vS task manager table model.
         *
         * @param process the process
         * @param localTask the local task
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
         * @param table the new table
         */
        public void setTable(JTable table) {
            this.table = table;
        }

        /**
         * Sets the.
         *
         * @param process the process
         * @param localTasks the local tasks
         * @param allProcesses the all processes
         */
        public void set(VSProcess process, boolean localTasks, boolean allProcesses) {
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
         * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
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
         * Removes the task at row.
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
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent me) {
            JTable source = (JTable) me.getSource();
            final int row = source.rowAtPoint(me.getPoint());
            final int col = source.columnAtPoint(me.getPoint());

            if (SwingUtilities.isRightMouseButton(me)) {
                ActionListener actionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String actionCommand = ae.getActionCommand();
                        if (actionCommand.equals(prefs.getString("lang.remove"))) {
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
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent me) { }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent me) { }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent me) { }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent me) { }
    }

    /**
     * Creates the task table.
     *
     * @param localTasks the local tasks
     *
     * @return the j table
     */
    private JTable createTaskTable(boolean localTasks) {
        VSProcess process = getSelectedProcess();
        VSTaskManagerTableModel model = new VSTaskManagerTableModel(process, localTasks);

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
     * @param localTasks the local tasks
     *
     * @return the j panel
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

                ArrayList<VSProcess> processes = getConcernedProcesses(localTasks);

                for (VSProcess process : processes) {
                    VSTask task = createTask.createTask(process, time, localTasks);
                    taskManager.addTask(task, VSTaskManager.PROGRAMMED);

                    if (selectedProcess == null || process.equals(selectedProcess)) {
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

        boolean flag = createTasks == null;
        if (flag) createTasks = new ArrayList<VSCreateTask>();

        Vector<String> eventClassnames = VSRegisteredEvents.getNonProtocolClassnames();

        comboBox.setMaximumRowCount(15);
        comboBox.addItem("-- " + prefs.getString("lang.events.process") + " --");
        if (flag)
            createTasks.add(null);

        for (String eventClassname : eventClassnames) {
            String eventShortname = VSRegisteredEvents.getShortname(eventClassname);
            comboBox.addItem(eventShortname);
            if (flag)
                createTasks.add(new VSCreateTask(eventClassname));
        }

        comboBox.addItem("-- " + prefs.getString("lang.requests") + " --");
        if (flag)
            createTasks.add(null);
        String clientrequest = prefs.getString("lang.clientrequest.start");

        eventClassnames = VSRegisteredEvents.getProtocolClassnames();
        for (String eventClassname : eventClassnames) {
            String eventShortname = VSRegisteredEvents.getShortname(eventClassname)
                                    + " " + clientrequest;

            comboBox.addItem(eventShortname);

            if (flag) {
                VSCreateTask createTask = new VSCreateTask(eventClassname);
                createTask.setShortname(eventShortname);
                createTask.isClientRequest(true);
                createTasks.add(createTask);
            }
        }

        comboBox.addItem("-- " + prefs.getString("lang.events.protocol") + " --");
        if (flag)
            createTasks.add(null);

        eventClassnames = VSRegisteredEvents.getProtocolClassnames();
        String activate = prefs.getString("lang.activate");
        String deactivate = prefs.getString("lang.deactivate");
        String client = prefs.getString("lang.client");
        String server = prefs.getString("lang.server");
        String protocolEventClassname = "events.internal.ProtocolEvent";

        for (String eventClassname : eventClassnames) {
            String eventShortname_ = VSRegisteredEvents.getShortname(eventClassname);

            String eventShortname = eventShortname_ + " " + client + " " + activate;
            comboBox.addItem(eventShortname);
            if (flag) {
                VSCreateTask createTask = new VSCreateTask(protocolEventClassname);
                createTask.isProtocolActivation(true);
                createTask.isClientProtocol(true);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + client + " " + deactivate;
            comboBox.addItem(eventShortname);
            if (flag) {
                VSCreateTask createTask = new VSCreateTask(protocolEventClassname);
                createTask.isProtocolDeactivation(true);
                createTask.isClientProtocol(true);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + server + " " + activate;
            comboBox.addItem(eventShortname);
            if (flag) {
                VSCreateTask createTask = new VSCreateTask(protocolEventClassname);
                createTask.isProtocolActivation(true);
                createTask.isClientProtocol(false);
                createTask.setProtocolClassname(eventClassname);
                createTask.setShortname(eventShortname);
                createTasks.add(createTask);
            }

            eventShortname = eventShortname_ + " " + server + " " + deactivate;
            comboBox.addItem(eventShortname);
            if (flag) {
                VSCreateTask createTask = new VSCreateTask(protocolEventClassname);
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
     * @param localTasks the local tasks
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
        taskManagerLocalModel.set(process, VSTaskManagerTableModel.LOCAL, allProcesses);
        taskManagerGlobalModel.set(process, VSTaskManagerTableModel.GLOBAL, allProcesses);
    }

    /**
     * Finish.
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
    public VSSimulator.VSMenuItemStates getMenuItemStates() {
        return menuItemStates;
    }

    /**
     * Gets the simulation canvas.
     *
     * @return the simulation canvas
     */
    public VSSimulatorCanvas getSimulationCanvas() {
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
     * Removes the process at index.
     *
     * @param index the index
     */
    public void removeProcessAtIndex(int index) {
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
     * Adds the process at index.
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
     * Fire expert mode changed.
     */
    public void fireExpertModeChanged() {
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        /* Update the Task Manager GUI */
        int selectedIndex = tabbedPane.getSelectedIndex();

        if (expertMode) {
            tabbedPane.remove(localPanel);
            tabbedPane.insertTab(prefs.getString("lang.events"), null, splitPane1, null, 0);
            splitPane1.setTopComponent(localPanel);
            //splitPane1.setDividerLocation((int) (getPaintSize()/2) - 20);

            /* addPanel */
            localAddPanel.add(localPIDComboBox, 2);

        } else {
            tabbedPane.remove(splitPane1);
            tabbedPane.insertTab(prefs.getString("lang.events"), null, localPanel, null, 0);

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
