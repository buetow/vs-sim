package simulator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import prefs.*;
import core.*;
import utils.*;

public class VSSimulation extends VSFrame implements ActionListener {
    private JTextField filterTextField;
    private JCheckBox filterActiveCheckBox;
    private JCheckBox lamportActiveCheckBox;
    private JCheckBox vectorTimeActiveCheckBox;
    private JMenuItem pauseItem;
    private JMenuItem replayItem;
    private JMenuItem resetItem;
    private JMenuItem startItem;
    private JPanel processEditPanel;
    private JSplitPane splitPaneH;
    private JSplitPane splitPaneV;
    private Thread thread;
    private VSLogging logging;
    private VSPrefs prefs;
    private VSSimulationPanel simulationPanel;
    private boolean hasStarted = false;

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

        /*
        splitPaneH = new JSplitPane();
        splitPaneH.setDividerLocation(
            prefs.getInteger("window.splitsize"));

        	*/
        splitPaneV = new JSplitPane();
        splitPaneV.setDividerLocation(
            prefs.getInteger("window.ysize")
            - prefs.getInteger("window.loggsize"));

        simulationPanel = new VSSimulationPanel(prefs, this, logging);
        logging.setSimulationPanel(simulationPanel);
        simulationPanel.setBackground(prefs.getColor("paintarea.background"));//new Color(0xFD, 0xFC, 0xF7));
        //processEditPanel = createProcessEditPanel();

        JScrollPane paintScrollPane = new JScrollPane(simulationPanel);
        JScrollPane textScrollPane = new JScrollPane(loggingArea);
        JPanel toolsPanel = createToolsPanel();

        JPanel loggingPane = new JPanel(new BorderLayout());
        loggingPane.add(textScrollPane, BorderLayout.CENTER);
        loggingPane.add(toolsPanel, BorderLayout.SOUTH);
        loggingPane.setPreferredSize(new Dimension(200, 1));

        //JScrollPane processEditScrollPane = new JScrollPane(processEditPanel);

        /*
        splitPaneH.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneH.setLeftComponent(processEditScrollPane);
        splitPaneH.setRightComponent(paintScrollPane);
        splitPaneH.setContinuousLayout(true);
        splitPaneH.setOneTouchExpandable(true);
        */

        splitPaneV.setOrientation(JSplitPane.VERTICAL_SPLIT);
        //splitPaneV.setTopComponent(splitPaneH);
        splitPaneV.setTopComponent(paintScrollPane);
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

    /*
    private JPanel createProcessEditPanel() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Insets insets = new Insets(5, 5, 5, 5);
        constraints.insets = insets;
        constraints.ipady = 5;
        constraints.ipadx = 5;
        int row = 0;

        Vector<VSProcess> processes = simulationPanel.getProcesses();

        for (VSProcess p : processes) {
            constraints.gridy = row++;
            //editPanel.add(new VSProcessEditor(prefs, p), constraints);
        }

        return editPanel;
    }
    */

    public int getSplitSize() {
        return 0;//splitPaneH.getDividerLocation();
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
            runThread();

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
            runThread();
        }
    }

    private void runThread() {
        if (hasStarted) {
            simulationPanel.play();

        } else {
            hasStarted = true;
            thread = new Thread(simulationPanel);
            thread.start();
        }
    }

    public void finish() {
        startItem.setEnabled(false);
        pauseItem.setEnabled(false);
        resetItem.setEnabled(true);
        replayItem.setEnabled(true);
    }
}
