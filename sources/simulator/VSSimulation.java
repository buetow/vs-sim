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
import prefs.*;
import protocols.*;
import utils.*;

public class VSSimulation extends VSFrame implements ActionListener {
    private JTextField filterTextField;
    private JCheckBox filterActiveCheckBox;
    private JCheckBox lamportActiveCheckBox;
    private JCheckBox vectorTimeActiveCheckBox;
    private JComboBox processesComboBox;
    private ArrayList<VSCreateTask> createTasks;
    private JMenuItem pauseItem;
    private JMenuItem replayItem;
    private JMenuItem resetItem;
    private JMenuItem startItem;
    private JSplitPane splitPaneH;
    private JSplitPane splitPaneV;
    private Thread thread;
    private VSLogging logging;
    private VSPrefs prefs;
    private VSSimulationPanel simulationPanel;
    private boolean hasStarted = false;
    private VSTaskManagerTableModel taskManagerLocalModel;
    private VSTaskManagerTableModel taskManagerGlobalModel;
    private VSTaskManager taskManager;

    public VSSimulation (VSPrefs prefs, Component relativeTo) {
        super(prefs.getString("name"), relativeTo);
        this.prefs = prefs;
        this.logging = new VSLogging();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(prefs.getInteger("window.xsize")+100,
                prefs.getInteger("window.ysize"));
        setContentPane(createContentPane());
        setJMenuBar(createJMenuBar());
        setVisible(true);

        thread = new Thread(simulationPanel);
        //logging.start();
        logging.logg(prefs.getString("lang.simulation.new"));
    }

    private JMenuBar createJMenuBar() {
        /* File menu */
        JMenu menuFile = new JMenu(prefs.getString("lang.file"));
        menuFile.setMnemonic(prefs.getInteger("keyevent.file"));
        JMenuItem menuItem;

        menuItem = new JMenuItem(prefs.getString("lang.new"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.new"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(
            prefs.getString("lang.close"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.close"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuFile.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.about"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.about"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.quit"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.quit"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        /* Edit menu */
        JMenu menuEdit = new JMenu(
            prefs.getString("lang.edit"));
        menuEdit.setMnemonic(prefs.getInteger("keyevent.edit"));
        int numProcesses = simulationPanel.getNumProcesses();
        final String processString = prefs.getString("lang.process");
        for (int i = 0; i < numProcesses; ++i) {
            JMenuItem processItem = new JMenuItem(processString + " " + (i+1));
            processItem.setAccelerator(KeyStroke.getKeyStroke(0x31+i,
                                       ActionEvent.ALT_MASK));
            final int processNum = i;
            processItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    simulationPanel.editProcess(processNum);
                }
            });
            menuEdit.add(processItem);
        }

        /* Simulation menu */
        JMenu menuSimulation = new JMenu(
            prefs.getString("lang.simulation"));
        menuSimulation.setMnemonic(prefs.getInteger("keyevent.simulation"));

        startItem = new JMenuItem(
            prefs.getString("lang.start"));
        startItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.start"),
                                     ActionEvent.ALT_MASK));
        startItem.addActionListener(this);
        menuSimulation.add(startItem);

        pauseItem = new JMenuItem(
            prefs.getString("lang.pause"));
        pauseItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.pause"),
                                     ActionEvent.ALT_MASK));
        pauseItem.addActionListener(this);
        menuSimulation.add(pauseItem);
        pauseItem.setEnabled(false);

        resetItem = new JMenuItem(
            prefs.getString("lang.reset"));
        resetItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.reset"),
                                     ActionEvent.ALT_MASK));
        resetItem.addActionListener(this);
        resetItem.setEnabled(false);
        menuSimulation.add(resetItem);

        replayItem = new JMenuItem(
            prefs.getString("lang.replay"));
        replayItem.setAccelerator(KeyStroke.getKeyStroke(
                                      prefs.getInteger("keyevent.replay"),
                                      ActionEvent.ALT_MASK));
        replayItem.addActionListener(this);
        replayItem.setEnabled(false);
        menuSimulation.add(replayItem);

        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(menuFile);
        mainMenuBar.add(menuEdit);
        mainMenuBar.add(menuSimulation);

        return mainMenuBar;
    }

    private Container createContentPane() {
        JTextArea loggingArea = logging.getLoggingArea();

        splitPaneH = new JSplitPane();
        splitPaneH.setDividerLocation(
            prefs.getInteger("window.splitsize"));

        splitPaneV = new JSplitPane();
        splitPaneV.setDividerLocation(
            prefs.getInteger("window.ysize")
            - prefs.getInteger("window.loggsize"));

        simulationPanel = new VSSimulationPanel(prefs, this, logging);
        taskManager = simulationPanel.getTaskManager();
        logging.setSimulationPanel(simulationPanel);
        simulationPanel.setBackground(prefs.getColor("paintarea.background"));

        JScrollPane paintScrollPane = new JScrollPane(simulationPanel);
        JScrollPane textScrollPane = new JScrollPane(loggingArea);
        JPanel toolsPanel = createToolsPanel();

        JPanel loggingPane = new JPanel(new BorderLayout());
        loggingPane.add(textScrollPane, BorderLayout.CENTER);
        loggingPane.add(toolsPanel, BorderLayout.SOUTH);
        loggingPane.setPreferredSize(new Dimension(200, 1));

        splitPaneH.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneH.setLeftComponent(createProcessPane());
        splitPaneH.setRightComponent(paintScrollPane);
        splitPaneH.setContinuousLayout(true);
        splitPaneH.setOneTouchExpandable(true);

        splitPaneV.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneV.setTopComponent(splitPaneH);
        splitPaneV.setBottomComponent(loggingPane);
        splitPaneV.setOneTouchExpandable(true);
        splitPaneV.setContinuousLayout(true);

        Container contentPane = getContentPane();
        contentPane.add(splitPaneV, BorderLayout.CENTER);


        return contentPane;
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
                simulationPanel.showLamport(buttonModel.isSelected());
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
                simulationPanel.showVectorTime(buttonModel.isSelected());
                if (buttonModel.isSelected())
                    lamportActiveCheckBox.setSelected(false);
            }
        });
        toolsPanel.add(vectorTimeActiveCheckBox);

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

    private JSplitPane createProcessPane() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

        processesComboBox = new JComboBox();
        int numProcesses = simulationPanel.getNumProcesses();
        String processString = prefs.getString("lang.process");
        for (int i = 1; i <= numProcesses; ++i)
            processesComboBox.addItem(processString + " " + i);

        JPanel localPanel = createTaskLabel(VSTaskManagerTableModel.LOCAL);
        JPanel globalPanel = createTaskLabel(VSTaskManagerTableModel.GLOBAL);

        JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane1.setTopComponent(localPanel);
        splitPane1.setBottomComponent(globalPanel);
        splitPane1.setDividerLocation((int) (getPaintSize()/2) - 20);
        splitPane1.setOneTouchExpandable(true);

        JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane2.setTopComponent(processesComboBox);
        splitPane2.setBottomComponent(splitPane1);

        processesComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                updateTaskManagerTable();
            }
        });

        return splitPane2;
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

        /* Those 3 values are for ProtocolEvent events */
        private boolean isProtocolActivation;
        private boolean isProtocolDeactivation;
        private boolean isClientProtocol;

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

        public VSTask createTask(VSProcess process, long time, boolean localTimedTask) {
            VSEvent event = VSRegisteredEvents.createEventInstanceByClassname(eventClassname, process);

            event.init(process);

            if (isProtocolActivation || isProtocolDeactivation) {
                ProtocolEvent protocolEvent = (ProtocolEvent) event;
                protocolEvent.setEventClassname(eventClassname);
                protocolEvent.isProtocolActivation(isProtocolActivation);
                protocolEvent.isClientProtocol(isClientProtocol);
            }

            return new VSTask(time, process, event, localTimedTask);
        }
    }

    private class VSTaskManagerTableModel extends AbstractTableModel implements MouseListener {
        public static final boolean LOCAL = true;
        public static final boolean GLOBAL = false;
        private VSPriorityQueue<VSTask> tasks;
        private String columnNames[];
        private VSProcess process;

        public VSTaskManagerTableModel(VSProcess process, boolean localTask) {
            set(process, localTask);
            columnNames = new String[2];
            columnNames[0]= prefs.getString("lang.time") + " (ms)";
            columnNames[1] = prefs.getString("lang.event");
        }

        public void set(VSProcess process, boolean localTasks) {
            this.process = process;
            this.tasks = localTasks
                         ?  taskManager.getProcessLocalTasks(process)
                         :  taskManager.getProcessGlobalTasks(process);

            fireTableDataChanged();
        }

        public String getColumnName(int col) {
            if (col == 0)
                return columnNames[0];
            return columnNames[1];
        }

        public int getRowCount() {
            return tasks.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int col) {
            VSTask task = tasks.get(row);

            if (col == 0)
                return task.getTaskTime();

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

        private void removeRow(int row) {
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
                            removeRow(row);
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
        table.addMouseListener(model);

        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMaxWidth(75);
        col.setResizable(false);
        col = table.getColumnModel().getColumn(1);
        col.sizeWidthToFit();
        table.setBackground(Color.WHITE);

        return table;
    }

    private void initAddPanel(JPanel panel, final boolean localTasks) {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

        final JTextField textField = new JTextField();
        textField.setText("0000");
        textField.setBackground(Color.WHITE);
        panel1.add(textField);

        panel1.add(new JLabel(" ms "));
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
                VSProcess process = getSelectedProcess();
                int index = comboBox.getSelectedIndex();
                VSCreateTask createTask = createTasks.get(index);

                if (createTask == null)
                    return false;

                VSTask task = createTask.createTask(process, time, localTasks);
                taskManager.addTask(task, VSTaskManager.PROGRAMMED);

                if (localTasks)
                    taskManagerLocalModel.addTask(task);
                else
                    taskManagerGlobalModel.addTask(task);

                return true;
            }
        });

        panel1.add(takeoverButton);

        boolean flag = createTasks == null;
        if (flag) createTasks = new ArrayList<VSCreateTask>();

        Vector<String> eventClassnames = VSRegisteredEvents.getNonProtocolClassnames();

        comboBox.setMaximumRowCount(15);
        comboBox.addItem("-- " + prefs.getString("lang.events.process") + " --");
        if (flag) createTasks.add(null);

        for (String eventClassname : eventClassnames) {
            String eventShortname = VSRegisteredEvents.getShortname(eventClassname);
            comboBox.addItem(eventShortname);
            if (flag) createTasks.add(new VSCreateTask(eventClassname));
        }

        comboBox.addItem("-- " + prefs.getString("lang.events.protocol") + " --");
        if (flag) createTasks.add(null);

        eventClassnames = VSRegisteredEvents.getProtocolClassnames();
        String activate = prefs.getString("lang.activate");
        String deactivate = prefs.getString("lang.deactivate");
        String client = prefs.getString("lang.client");
        String server = prefs.getString("lang.server");

        for (String eventClassname : eventClassnames) {
            String eventShortname = VSRegisteredEvents.getShortname(eventClassname);
            comboBox.addItem(eventShortname + " " + client + " " + activate);
            comboBox.addItem(eventShortname + " " + client + " " + deactivate);
            comboBox.addItem(eventShortname + " " + server + " " + activate);
            comboBox.addItem(eventShortname + " " + server + " " + deactivate);

            if (flag) {
                VSCreateTask createTask = new VSCreateTask(eventClassname);
                createTask.isProtocolActivation(true);
                createTask.isClientProtocol(true);
                createTasks.add(createTask);

                createTask = new VSCreateTask(eventClassname);
                createTask.isProtocolDeactivation(true);
                createTask.isClientProtocol(true);
                createTasks.add(createTask);

                createTask = new VSCreateTask(eventClassname);
                createTask.isProtocolActivation(true);
                createTask.isClientProtocol(false);
                createTasks.add(createTask);

                createTask = new VSCreateTask(eventClassname);
                createTask.isProtocolDeactivation(true);
                createTask.isClientProtocol(false);
                createTasks.add(createTask);
            }
        }

        comboBox.addItem("-- " + prefs.getString("lang.requests") + " --");
        if (flag) createTasks.add(null);
        String clientrequest = prefs.getString("lang.clientrequest.start");

        for (String eventClassname : eventClassnames) {
            String eventShortname = VSRegisteredEvents.getShortname(eventClassname);
            comboBox.addItem(eventShortname + " " + clientrequest);
            if (flag) createTasks.add(new VSCreateTask(eventClassname));
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

    public void dispose() {
        simulationPanel.finalize();
        super.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();

        if (source.getText().equals(prefs.getString("lang.close"))) {
            dispose();

        } else if (source.getText().equals(prefs.getString("lang.new"))) {
            new VSMain(VSDefaultPrefs.init(), VSSimulation.this);

        } else if (source.getText().equals(prefs.getString("lang.about"))) {
            new VSAbout(prefs, VSSimulation.this);

        } else if (source.getText().equals(prefs.getString("lang.quit"))) {
            System.exit(0);

        } else if (source.getText().equals(prefs.getString("lang.start"))) {
            startItem.setEnabled(false);
            pauseItem.setEnabled(true);
            resetItem.setEnabled(false);
            replayItem.setEnabled(true);
            registeredProhread();

        } else if (source.getText().equals(prefs.getString("lang.pause"))) {
            startItem.setEnabled(true);
            pauseItem.setEnabled(false);
            resetItem.setEnabled(true);
            replayItem.setEnabled(true);
            simulationPanel.pause();

        } else if (source.getText().equals(prefs.getString("lang.reset"))) {
            startItem.setEnabled(true);
            pauseItem.setEnabled(false);
            resetItem.setEnabled(false);
            replayItem.setEnabled(false);
            simulationPanel.reset();

        } else if (source.getText().equals(prefs.getString("lang.replay"))) {
            startItem.setEnabled(false);
            pauseItem.setEnabled(true);
            resetItem.setEnabled(false);
            replayItem.setEnabled(true);
            simulationPanel.reset();
            registeredProhread();
        }
    }

    private void registeredProhread() {
        if (hasStarted) {
            simulationPanel.play();

        } else {
            hasStarted = true;
            thread = new Thread(simulationPanel);
            thread.start();
        }
    }

    private VSProcess getSelectedProcess() {
        String string = (String) processesComboBox.getSelectedItem();
        int cutLen = prefs.getString("lang.process").length() + 1;
        string = string.substring(cutLen);
        int processNum = Integer.parseInt(string) - 1;

        return simulationPanel.getProcess(processNum);
    }

    public void updateTaskManagerTable() {
        VSProcess process = getSelectedProcess();
        taskManagerLocalModel.set(process, VSTaskManagerTableModel.LOCAL);
        taskManagerGlobalModel.set(process, VSTaskManagerTableModel.GLOBAL);
    }

    public void finish() {
        startItem.setEnabled(false);
        pauseItem.setEnabled(false);
        resetItem.setEnabled(true);
        replayItem.setEnabled(true);
    }
}
