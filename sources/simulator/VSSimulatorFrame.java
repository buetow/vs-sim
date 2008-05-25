package simulator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import core.*;
import prefs.*;
import prefs.editors.*;
import utils.*;

public class VSSimulatorFrame extends VSFrame implements ActionListener {
    private JMenuItem pauseItem;
    private JMenuItem replayItem;
    private JMenuItem resetItem;
    private JMenuItem startItem;
    private JButton pauseButton;
    private JButton replayButton;
    private JButton resetButton;
    private JButton startButton;
    private JMenu menuEdit;
    private JMenu menuFile;
    private JMenu menuSimulation;
    private JToolBar toolBar;
    private VSPrefs prefs;
    private Vector<VSSimulator> simulations;
    private VSSimulator currentSimulation;
    private JTabbedPane tabbedPane;
    //private JSlider speedSlider;

    public VSSimulatorFrame(VSPrefs prefs, Component relativeTo) {
        super(prefs.getString("lang.name"), relativeTo);
        this.prefs = prefs;
        this.simulations = new Vector<VSSimulator>();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(prefs.getInteger("div.window.xsize"),
                prefs.getInteger("div.window.ysize"));

        setJMenuBar(createMenuBar());
        setLayout(new BorderLayout());
        setContentPane(createContentPane());
        setVisible(true);

        pauseButton.setEnabled(false);
        replayButton.setEnabled(false);
        resetButton.setEnabled(false);
        startButton.setEnabled(false);
        menuEdit.setEnabled(false);
        menuFile.setEnabled(false);
        menuSimulation.setEnabled(false);
    }

    private JMenuBar createMenuBar() {
        /* File menu */
        menuFile = new JMenu(prefs.getString("lang.file"));
        menuFile.setMnemonic(prefs.getInteger("keyevent.file"));
        JMenuItem menuItem;

        menuItem = new JMenuItem(prefs.getString("lang.simulation.new"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.new"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(
            prefs.getString("lang.simulation.close"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.close"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuFile.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.window.new"));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.window.close"));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);


        menuFile.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.about"));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.quit"));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        /* Edit menu */
        menuEdit = new JMenu(
            prefs.getString("lang.edit"));
        menuEdit.setMnemonic(prefs.getInteger("keyevent.edit"));
        updateEditMenu();

        /* Simulation menu */
        toolBar = new JToolBar();
        menuSimulation = new JMenu(
            prefs.getString("lang.simulation"));
        menuSimulation.setMnemonic(prefs.getInteger("keyevent.simulation"));

        resetItem = new JMenuItem(prefs.getString("lang.reset"));
        resetItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.reset"),
                                     ActionEvent.ALT_MASK));
        resetItem.addActionListener(this);
        resetItem.setEnabled(false);
        menuSimulation.add(resetItem);
        resetButton = new JButton(getImageIcon("reset.png", prefs.getString("lang.reset")));
        resetButton.addActionListener(this);
        toolBar.add(resetButton);

        replayItem = new JMenuItem(
            prefs.getString("lang.replay"));
        replayItem.setAccelerator(KeyStroke.getKeyStroke(
                                      prefs.getInteger("keyevent.replay"),
                                      ActionEvent.ALT_MASK));
        replayItem.addActionListener(this);
        replayItem.setEnabled(false);
        menuSimulation.add(replayItem);
        replayButton = new JButton(getImageIcon("replay.png", prefs.getString("lang.replay")));
        replayButton.addActionListener(this);
        toolBar.add(replayButton);

        pauseItem = new JMenuItem(prefs.getString("lang.pause"));
        pauseItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.pause"),
                                     ActionEvent.ALT_MASK));
        pauseItem.addActionListener(this);
        menuSimulation.add(pauseItem);
        pauseItem.setEnabled(false);
        pauseButton = new JButton(getImageIcon("pause.png", prefs.getString("lang.pause")));
        pauseButton.addActionListener(this);
        toolBar.add(pauseButton);

        startItem = new JMenuItem(prefs.getString("lang.start"));
        startItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.start"),
                                     ActionEvent.ALT_MASK));
        startItem.addActionListener(this);
        menuSimulation.add(startItem);
        startButton = new JButton(getImageIcon("start.png", prefs.getString("lang.start")));
        startButton.addActionListener(this);
        toolBar.add(startButton);


        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(menuFile);
        mainMenuBar.add(menuEdit);
        mainMenuBar.add(menuSimulation);

        return mainMenuBar;
    }

    private Container createContentPane() {
        Container pane = getContentPane();
        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                JTabbedPane pane = (JTabbedPane) ce.getSource();
                currentSimulation = (VSSimulator) pane.getSelectedComponent();
                currentSimulation.getSimulationCanvas().paint();
                updateEditMenu();
                updateSimulationMenu();
            }
        });

        pane.add(toolBar, BorderLayout.PAGE_START);
        pane.add(tabbedPane, BorderLayout.CENTER);

        return pane;
    }

    public void updateEditMenu() {
        menuEdit.removeAll();

        JMenuItem globalPrefsItem = new JMenuItem(prefs.getString("lang.prefs"));
        globalPrefsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                VSPrefs simulationPrefs = currentSimulation.getPrefs();
                VSSimulatorEditor.TAKEOVER_BUTTON = true;
                VSSimulatorEditor simulationEditor = new VSSimulatorEditor(
                    simulationPrefs, VSSimulatorFrame.this, currentSimulation);
                new VSEditorFrame(prefs, VSSimulatorFrame.this, simulationEditor);
            }
        });
        menuEdit.add(globalPrefsItem);
        menuEdit.addSeparator();

        if (currentSimulation == null)
            return;

        final String processString = prefs.getString("lang.process");
        final ArrayList<VSProcess> arr = currentSimulation.getSimulationCanvas().getProcessesArray();
        final int numProcesses = arr.size();

        int processNum = 0;
        for (VSProcess process : arr) {
            int processID = process.getProcessID();
            JMenuItem processItem = new JMenuItem(processString + " " + processID);
            if (processNum < 10)
                processItem.setAccelerator(KeyStroke.getKeyStroke(0x31+processNum,
                                           ActionEvent.ALT_MASK));
            final int finalProcessNum = processNum++;
            processItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    currentSimulation.getSimulationCanvas().editProcess(finalProcessNum);
                }
            });
            menuEdit.add(processItem);
        }
    }

    /* updateSimulationMenu can be called from concurrent threads */
    public synchronized void updateSimulationMenu() {
        VSSimulator.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();

        pauseItem.setEnabled(menuItemState.getPause());
        replayItem.setEnabled(menuItemState.getReplay());
        resetItem.setEnabled(menuItemState.getReset());
        startItem.setEnabled(menuItemState.getStart());

        pauseButton.setEnabled(menuItemState.getPause());
        replayButton.setEnabled(menuItemState.getReplay());
        resetButton.setEnabled(menuItemState.getReset());
        startButton.setEnabled(menuItemState.getStart());
    }

    public void dispose() {
        synchronized (simulations) {
            for (VSSimulator simulation : simulations)
                simulation.getSimulationCanvas().stopThread();
        }
        super.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String sourceText = null;

        if (source instanceof JMenuItem)
            sourceText = ((JMenuItem) source).getText();
        else
            sourceText = ((ImageIcon) ((JButton) source).getIcon()).getDescription();

        if (sourceText.equals(prefs.getString("lang.simulation.close"))) {
            removeCurrentSimulation();

        } else if (sourceText.equals(prefs.getString("lang.simulation.new"))) {
            VSPrefs newPrefs = VSDefaultPrefs.init();
            new VSEditorFrame(newPrefs, this, new VSSimulatorEditor(newPrefs, this));

        } else if (sourceText.equals(prefs.getString("lang.window.new"))) {
            new VSMain(VSDefaultPrefs.init(), this);

        } else if (sourceText.equals(prefs.getString("lang.window.close"))) {
            dispose();

        } else if (sourceText.equals(prefs.getString("lang.about"))) {
            new VSAbout(prefs, this);

        } else if (sourceText.equals(prefs.getString("lang.quit"))) {
            System.exit(0);

        } else if (sourceText.equals(prefs.getString("lang.start"))) {
            VSSimulator.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(false);
            menuItemState.setPause(true);
            menuItemState.setReset(false);
            menuItemState.setReplay(true);
            currentSimulation.getSimulationCanvas().play();
            updateSimulationMenu();

        } else if (sourceText.equals(prefs.getString("lang.pause"))) {
            VSSimulator.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(true);
            menuItemState.setPause(false);
            menuItemState.setReset(true);
            menuItemState.setReplay(true);
            currentSimulation.getSimulationCanvas().pause();
            updateSimulationMenu();

        } else if (sourceText.equals(prefs.getString("lang.reset"))) {
            VSSimulator.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(true);
            menuItemState.setPause(false);
            menuItemState.setReset(false);
            menuItemState.setReplay(false);
            currentSimulation.getSimulationCanvas().reset();
            updateSimulationMenu();

        } else if (sourceText.equals(prefs.getString("lang.replay"))) {
            VSSimulator.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(false);
            menuItemState.setPause(true);
            menuItemState.setReset(false);
            menuItemState.setReplay(true);
            currentSimulation.getSimulationCanvas().reset();
            currentSimulation.getSimulationCanvas().play();
            updateSimulationMenu();
        }
    }

    public void addSimulation(VSSimulator simulation) {
        simulation.setLayout(new GridLayout(1, 1, 3, 3));
        simulation.setMinimumSize(new Dimension(0, 0));
        simulation.setMaximumSize(new Dimension(0, 0));

        simulations.add(simulation);
        tabbedPane.addTab(prefs.getString("lang.simulation")
                          + " " + simulation.getSimulationNum(), simulation);
        tabbedPane.setSelectedComponent(simulation);

        if (simulations.size() == 1) {
            menuEdit.setEnabled(true);
            menuFile.setEnabled(true);
            menuSimulation.setEnabled(true);
        }
    }

    public void removeSimulation(VSSimulator simulationToRemove) {
        if (simulations.size() == 1) {
            dispose();

        } else {
            simulations.remove(simulationToRemove);
            tabbedPane.remove(simulationToRemove);
            simulationToRemove.getSimulationCanvas().stopThread();
        }
    }

    private void removeCurrentSimulation() {
        removeSimulation(currentSimulation);
    }

    public VSSimulator getCurrentSimulation() {
        return currentSimulation;
    }

    private ImageIcon getImageIcon(String name, String descr) {
        java.net.URL imageURL = getClass().getResource("/icons/"+name);

        if (imageURL == null)
            return new ImageIcon("icons/"+name, descr);


        return new ImageIcon(imageURL, descr);
    }

}
