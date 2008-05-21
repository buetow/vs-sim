package simulator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import core.*;
import events.*;
import events.implementations.*;
import events.internal.*;
import prefs.*;
import prefs.editors.*;
import protocols.*;
import utils.*;

public class VSSimulation extends JPanel {
    private static int simulationCounter;
    private static int simulationNum;
    private VSSimulatorFrame simulatorFrame;
    private JTextField filterTextField;
    private JCheckBox filterActiveCheckBox;
    private JCheckBox lamportActiveCheckBox;
    private JCheckBox vectorTimeActiveCheckBox;
    private JComboBox processesComboBox;
    private JComboBox localPIDComboBox;
    private JComboBox globalPIDComboBox;
    private int lastSelectedProcessNum;
    private ArrayList<String> localTextFields;
    private ArrayList<String> globalTextFields;
    private JTextField localTextField;
    private JTextField globalTextField;
    private ArrayList<VSCreateTask> createTasks;
    private JSplitPane splitPaneH;
    private JSplitPane splitPaneV;
    private Thread thread;
    private VSLogging logging;
    private VSPrefs prefs;
    private VSSimulationCanvas simulationCanvas;
    private boolean hasStarted = false;
    private VSTaskManagerTableModel taskManagerLocalModel;
    private VSTaskManagerTableModel taskManagerGlobalModel;
    private VSTaskManager taskManager;
    private VSMenuItemStates menuItemStates;
    private JTabbedPane tabbedPane;
    private JPanel variablesPanel;
    private JPanel globalVariablesPanel;

    public class VSMenuItemStates {
        private volatile boolean pause;
        private volatile boolean replay;
        private volatile boolean reset;
        private volatile boolean start;

        public VSMenuItemStates(boolean pause, boolean replay, boolean reset, boolean start) {
            this.pause = pause;
            this.replay = replay;
            this.reset = reset;
            this.start = start;
        }

        public void setPause(boolean pause) {
            this.pause = pause;
        }

        public void setReplay(boolean replay) {
            this.replay = replay;
        }

        public void setReset(boolean pause) {
            this.reset = reset;
        }

        public void setStart(boolean start) {
            this.start = start;
        }

        public boolean getPause() {
            return pause;
        }

        public boolean getReplay() {
            return replay;
        }

        public boolean getReset() {
            return reset;
        }

        public boolean getStart() {
            return start;
        }
    }

    public VSSimulation(VSPrefs prefs, VSSimulatorFrame simulatorFrame) {
        this.prefs = prefs;
        this.simulatorFrame = simulatorFrame;
        this.logging = new VSLogging();
        this.simulationNum = ++simulationCounter;
        this.menuItemStates = new VSMenuItemStates(false, false, false, true);
        this.localTextFields = new ArrayList<String>();
        this.globalTextFields = new ArrayList<String>();

        logging.logg(prefs.getString("lang.simulation.new"));
        fillContentPane();

        int numProcesses = simulationCanvas.getNumProcesses();

        for (int i = 0; i <= numProcesses; ++i) {
            localTextFields.add("0000");
            globalTextFields.add("0000");
        }

        processesComboBox.setSelectedIndex(numProcesses);
        localPIDComboBox.setSelectedIndex(0);
        globalPIDComboBox.setSelectedIndex(0);

        thread = new Thread(simulationCanvas);
        thread.start();
    }

    private void fillContentPane() {
        JTextArea loggingArea = logging.getLoggingArea();

        splitPaneH = new JSplitPane();
        splitPaneH.setDividerLocation(
            prefs.getInteger("window.splitsize"));

        splitPaneV = new JSplitPane();
        splitPaneV.setDividerLocation(
            prefs.getInteger("window.ysize")
            - prefs.getInteger("window.loggsize"));

        simulationCanvas = new VSSimulationCanvas(prefs, this, logging);
        taskManager = simulationCanvas.getTaskManager();
        logging.setSimulationCanvas(simulationCanvas);
        simulationCanvas.setBackground(prefs.getColor("paintarea.background"));

        JPanel canvasPanel = new JPanel();
        canvasPanel.setLayout(new GridLayout(1, 1, 3, 3));
        canvasPanel.add(simulationCanvas);
        canvasPanel.setMinimumSize(new Dimension(0, 0));
        canvasPanel.setMaximumSize(new Dimension(0, 0));

        JScrollPane textScrollPane = new JScrollPane(loggingArea);
        JPanel toolsPanel = createToolsPanel();

        JPanel loggingPane = new JPanel(new BorderLayout());
        loggingPane.add(textScrollPane, BorderLayout.CENTER);
        loggingPane.add(toolsPanel, BorderLayout.SOUTH);
        loggingPane.setPreferredSize(new Dimension(200, 1));

        splitPaneH.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneH.setLeftComponent(createProcessPane());
        splitPaneH.setRightComponent(canvasPanel);
        splitPaneH.setContinuousLayout(true);
        //splitPaneH.setOneTouchExpandable(true);

        splitPaneV.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneV.setTopComponent(splitPaneH);
        splitPaneV.setBottomComponent(loggingPane);
        //splitPaneV.setOneTouchExpandable(true);
        splitPaneV.setContinuousLayout(true);

        this.add(splitPaneV, BorderLayout.CENTER);
    }

    private JPanel createToolsPanel() {
        JPanel toolsPanel = new JPanel();

        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));

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

        return toolsPanel;
    }

    private JPanel createProcessPane() {
        JPanel editPanel = new JPanel(new GridBagLayout());
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
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                JTabbedPane pane = (JTabbedPane) ce.getSource();
                //currentSimulation = (VSSimulation) pane.getSelectedComponent();
            }
        });
        //editPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel localPanel = createTaskLabel(VSTaskManagerTableModel.LOCAL);
        JPanel globalPanel = createTaskLabel(VSTaskManagerTableModel.GLOBAL);

        JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane1.setTopComponent(localPanel);
        splitPane1.setBottomComponent(globalPanel);
        splitPane1.setDividerLocation((int) (getPaintSize()/2) - 20);
        splitPane1.setOneTouchExpandable(true);
        tabbedPane.addTab(prefs.getString("lang.events"), splitPane1);

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
                    variablesPanel.removeAll();
                    VSProcess process = getSelectedProcess();
                    VSProcessEditor editor = new VSProcessEditor(prefs, process);
                    variablesPanel.add(new JScrollPane(editor.getContentPane()));
                }
            }
        });

        variablesPanel = new JPanel();
        globalVariablesPanel = new JPanel();

        variablesPanel.setLayout(new BoxLayout(variablesPanel, BoxLayout.Y_AXIS));
        globalVariablesPanel.setLayout(new BoxLayout(globalVariablesPanel, BoxLayout.Y_AXIS));

        tabbedPane.add(prefs.getString("lang.variables"), variablesPanel);
        tabbedPane.add(prefs.getString("lang.variables.global"), globalVariablesPanel);

        VSSimulationEditor editor = new VSSimulationEditor(prefs, simulatorFrame);
        globalVariablesPanel.add(new JScrollPane(editor.getContentPane()));

        editPanel.add(processesComboBox);
        editPanel.add(tabbedPane);

        return editPanel;
    }

    private JPanel createLabelPanel(String text) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);
        panel.add(label);

        return panel;
    }

    private JPanel createTaskLabel(boolean localTasks) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (localTasks)
            panel.add(createLabelPanel(prefs.getString("lang.local")));
        else
            panel.add(createLabelPanel(prefs.getString("lang.global")));

        JScrollPane scrollPane = new JScrollPane(createTaskTable(localTasks));
        panel.add(scrollPane);

        initAddPanel(panel, localTasks);

        return panel;
    }

    private class VSCreateTask {
        private String eventClassname;
        private String protocolClassname;
        private String shortname;

        /* Those 3 values are for ProtocolEvent events */
        private boolean isProtocolActivation;
        private boolean isProtocolDeactivation;
        private boolean isClientProtocol;

        /* Those values are for ProtocolClient onStart events */
        private boolean isClientRequest;

        public VSCreateTask(String eventClassname) {
            this.eventClassname = eventClassname;
        }

        public void isProtocolActivation(boolean isProtocolActivation) {
            this.isProtocolActivation = isProtocolActivation;

            if (isProtocolActivation)
                isProtocolDeactivation(false);
        }

        public void isProtocolDeactivation(boolean isProtocolDeactivation) {
            this.isProtocolDeactivation = isProtocolDeactivation;

            if (isProtocolDeactivation)
                isProtocolActivation(false);
        }

        public void isClientProtocol(boolean isClientProtocol) {
            this.isClientProtocol = isClientProtocol;
        }

        public void isClientRequest(boolean isClientRequest) {
            this.isClientRequest = isClientRequest;
        }

        public void setProtocolClassname(String protocolClassname) {
            this.protocolClassname = protocolClassname;
        }

        public void setShortname(String shortname) {
            this.shortname = shortname;
        }

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

    private class VSTaskManagerTableModel extends AbstractTableModel implements MouseListener {
        public static final boolean LOCAL = true;
        public static final boolean GLOBAL = false;
        public static final boolean ALL_PROCESSES = true;
        public static final boolean ONE_PROCESS = false;
        public boolean allProcesses;
        private VSPriorityQueue<VSTask> tasks;
        private String columnNames[];
        private int numColumns;
        private JTable table;

        public VSTaskManagerTableModel(VSProcess process, boolean localTask) {
            set(process, localTask, ONE_PROCESS);
            columnNames = new String[3];
            columnNames[0]= prefs.getString("lang.time") + " (ms)";
            columnNames[1] = prefs.getString("lang.process.id");
            columnNames[2] = prefs.getString("lang.event");
            numColumns = 3;
        }

        public void setTable(JTable table) {
            this.table = table;
        }

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

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public int getRowCount() {
            return tasks == null ? 0 : tasks.size();
        }

        public int getColumnCount() {
            return numColumns;
        }

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

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            fireTableDataChanged();
        }

        public void addTask(VSTask task) {
            tasks.add(task);
            fireTableDataChanged();
        }

        private void removeTaskAtRow(int row) {
            VSTask task = tasks.get(row);
            tasks.remove(task);
            taskManager.removeTask(task);
            fireTableDataChanged();
        }

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

        public void mouseEntered(MouseEvent me) { }
        public void mouseExited(MouseEvent me) { }
        public void mousePressed(MouseEvent me) { }
        public void mouseReleased(MouseEvent me) { }
    }

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

    private void initAddPanel(JPanel panel, final boolean localTasks) {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

        final JTextField textField = new JTextField();
        if (localTasks)
            localTextField = textField;
        else
            globalTextField = textField;

        textField.setText("0000");
        textField.setBackground(Color.WHITE);
        panel1.add(textField);

        panel1.add(new JLabel(" ms "));

        if (localTasks)
            panel1.add(localPIDComboBox);
        else
            panel1.add(globalPIDComboBox);

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

        panel1.add(takeoverButton);

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
        panel.add(panel1);
    }

    public int getSplitSize() {
        return splitPaneH.getDividerLocation();
    }

    public int getPaintSize() {
        return splitPaneV.getDividerLocation();
    }

    private int getSelectedProcessNum() {
        try {
            String string = (String) processesComboBox.getSelectedItem();
            int cutLen = prefs.getString("lang.process").length() + 1;
            string = string.substring(cutLen);
            return Integer.parseInt(string) - 1;
        } catch (NumberFormatException e) {
        }

        return simulationCanvas.getNumProcesses();
    }

    private VSProcess getSelectedProcess() {
        int processNum = getSelectedProcessNum();
        return simulationCanvas.getProcess(processNum);
    }

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

    public void updateTaskManagerTable() {
        VSProcess process = getSelectedProcess();
        boolean allProcesses = process == null;
        taskManagerLocalModel.set(process, VSTaskManagerTableModel.LOCAL, allProcesses);
        taskManagerGlobalModel.set(process, VSTaskManagerTableModel.GLOBAL, allProcesses);
    }

    public void finish() {
        menuItemStates.setStart(false);
        menuItemStates.setPause(false);
        menuItemStates.setReset(true);
        menuItemStates.setReplay(true);
        simulatorFrame.updateSimulationMenu();
    }

    public int getSimulationNum() {
        return simulationNum;
    }

    public VSSimulation.VSMenuItemStates getMenuItemStates() {
        return menuItemStates;
    }

    public VSSimulationCanvas getSimulationCanvas() {
        return simulationCanvas;
    }

    public VSFrame getSimulatorFrame() {
        return simulatorFrame;
    }
}
