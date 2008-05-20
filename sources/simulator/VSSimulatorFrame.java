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
import prefs.editors.*;
import protocols.*;
import utils.*;

public class VSSimulatorFrame extends VSFrame implements ActionListener {
    private JMenuItem pauseItem;
    private JMenuItem replayItem;
    private JMenuItem resetItem;
    private JMenuItem startItem;
    private JMenu menuEdit;
    private VSPrefs prefs;
    private ArrayList<VSSimulation> simulations;
    private VSSimulation currentSimulation;
    private JTabbedPane tabbedPane;

    public VSSimulatorFrame(VSPrefs prefs, Component relativeTo) {
        super(prefs.getString("name"), relativeTo);
        this.prefs = prefs;
        this.simulations = new ArrayList<VSSimulation>();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(prefs.getInteger("window.xsize")+100,
                prefs.getInteger("window.ysize"));
        setJMenuBar(createJMenuBar());
        setContentPane(createContentPane());
        setVisible(true);
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
        menuEdit = new JMenu(
            prefs.getString("lang.edit"));
        menuEdit.setMnemonic(prefs.getInteger("keyevent.edit"));
        updateEditMenu();

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
        updateSimulationMenu();

        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(menuFile);
        mainMenuBar.add(menuEdit);
        mainMenuBar.add(menuSimulation);

        return mainMenuBar;
    }

    private Container createContentPane() {
        Container pane = getContentPane();

        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                JTabbedPane pane = (JTabbedPane) ce.getSource();
                currentSimulation = (VSSimulation) pane.getSelectedComponent();
                currentSimulation.getSimulationPanel().paint();
                updateEditMenu();
                updateSimulationMenu();
            }
        });
        pane.add(tabbedPane, BorderLayout.CENTER);

        return pane;
    }

    private void updateEditMenu() {
        menuEdit.removeAll();

        if (currentSimulation == null)
            return;

        final String processString = prefs.getString("lang.process");
        final int numProcesses = currentSimulation.getSimulationPanel().getNumProcesses();

        for (int i = 0; i < numProcesses; ++i) {
            JMenuItem processItem = new JMenuItem(processString + " " + (i+1));
            processItem.setAccelerator(KeyStroke.getKeyStroke(0x31+i,
                                       ActionEvent.ALT_MASK));
            final int finalProcessNum = i;
            processItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    currentSimulation.getSimulationPanel().editProcess(finalProcessNum);
                }
            });
            menuEdit.add(processItem);
        }
    }

    public void updateSimulationMenu() {
        if (currentSimulation == null) {
            pauseItem.setEnabled(false);
            replayItem.setEnabled(false);
            resetItem.setEnabled(false);
            startItem.setEnabled(false);

        } else {
            VSSimulation.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            pauseItem.setEnabled(menuItemState.getPause());
            replayItem.setEnabled(menuItemState.getReplay());
            resetItem.setEnabled(menuItemState.getReset());
            startItem.setEnabled(menuItemState.getStart());
        }
    }

    public void dispose() {
        for (VSSimulation simulation : simulations)
            simulation.getSimulationPanel().stopThread();
        super.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();

        if (source.getText().equals(prefs.getString("lang.close"))) {
            dispose();

        } else if (source.getText().equals(prefs.getString("lang.new"))) {
            new VSSimulationEditor(VSDefaultPrefs.init(), this);

        } else if (source.getText().equals(prefs.getString("lang.about"))) {
            new VSAbout(prefs, this);

        } else if (source.getText().equals(prefs.getString("lang.quit"))) {
            System.exit(0);

        } else if (source.getText().equals(prefs.getString("lang.start"))) {
            VSSimulation.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(false);
            menuItemState.setPause(true);
            menuItemState.setReset(false);
            menuItemState.setReplay(true);
            currentSimulation.getSimulationPanel().play();
            updateSimulationMenu();

        } else if (source.getText().equals(prefs.getString("lang.pause"))) {
            VSSimulation.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(true);
            menuItemState.setPause(false);
            menuItemState.setReset(true);
            menuItemState.setReplay(true);
            currentSimulation.getSimulationPanel().pause();
            updateSimulationMenu();

        } else if (source.getText().equals(prefs.getString("lang.reset"))) {
            VSSimulation.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(true);
            menuItemState.setPause(false);
            menuItemState.setReset(false);
            menuItemState.setReplay(false);
            currentSimulation.getSimulationPanel().reset();
            updateSimulationMenu();

        } else if (source.getText().equals(prefs.getString("lang.replay"))) {
            VSSimulation.VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();
            menuItemState.setStart(false);
            menuItemState.setPause(true);
            menuItemState.setReset(false);
            menuItemState.setReplay(true);
            currentSimulation.getSimulationPanel().reset();
            currentSimulation.getSimulationPanel().play();
            updateSimulationMenu();
        }
    }

    public void addSimulation(VSSimulation simulation) {
        simulation.setLayout(new GridLayout(1, 1, 3, 3));
        simulation.setMinimumSize(new Dimension(0, 0));
        simulation.setMaximumSize(new Dimension(0, 0));

        simulations.add(simulation);

        tabbedPane.addTab(prefs.getString("lang.simulation")
                          + " " + simulation.getSimulationNum(), simulation);
    }
}
