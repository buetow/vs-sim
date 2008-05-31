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

import core.*;
import prefs.*;
import prefs.editors.*;
import utils.*;

/**
 * The class VSSimulatorFrame, an object of this class represents a window
 * of the simulator. The window can have several tabs. Each tab contains
 * an independent simulation.
 *
 * @author Paul C. Buetow
 */
public class VSSimulatorFrame extends VSFrame {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The pause item. */
    private JMenuItem pauseItem;

    /** The replay item. */
    private JMenuItem replayItem;

    /** The reset item. */
    private JMenuItem resetItem;

    /** The start item. */
    private JMenuItem startItem;

    /** The pause button. */
    private JButton pauseButton;

    /** The replay button. */
    private JButton replayButton;

    /** The reset button. */
    private JButton resetButton;

    /** The start button. */
    private JButton startButton;

    /** The menu edit. */
    private JMenu menuEdit;

    /** The menu file. */
    private JMenu menuFile;

    /** The menu simulation. */
    private JMenu menuSimulation;

    /** The tool bar. */
    private JToolBar toolBar;

    /** The prefs. */
    private VSPrefs prefs;

    /** The simulations. */
    private Vector<VSSimulator> simulations;

    /** The current simulation. */
    private VSSimulator currentSimulation;

    /** The tabbed pane. */
    private JTabbedPane tabbedPane;

    /** The action listener */
    private ActionListener actionListener;

    /**
     * Instantiates a new VSSimulatorFrame object.
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the window relative to
     */
    public VSSimulatorFrame(VSPrefs prefs, Component relativeTo) {
        super(prefs.getString("lang.name"), relativeTo);
        this.prefs = prefs;
        this.simulations = new Vector<VSSimulator>();

        final VSPrefs finalPrefs = this.prefs;
        actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                String sourceText = null;

                if (source instanceof JMenuItem)
                    sourceText = ((JMenuItem) source).getText();
                else
                    sourceText = ((ImageIcon) ((JButton) source).getIcon()).
                                 getDescription();

                if (sourceText.equals(
                finalPrefs.getString("lang.simulation.close"))) {
                    removeCurrentSimulation();

                } else if (sourceText.equals(
                finalPrefs.getString("lang.simulation.new"))) {
                    VSPrefs newPrefs = VSDefaultPrefs.init();
                    VSSimulatorEditor simulatorEditor =
                        new VSSimulatorEditor(newPrefs, VSSimulatorFrame.this,
                                              VSSimulatorEditor.OPENED_NEW_TAB);
                    new VSEditorFrame(newPrefs, VSSimulatorFrame.this,
                                      simulatorEditor);

                } else if (sourceText.equals(
                finalPrefs.getString("lang.window.new"))) {
                    new VSMain(VSDefaultPrefs.init(),
                               VSSimulatorFrame.this);

                } else if (sourceText.equals(
                finalPrefs.getString("lang.window.close"))) {
                    dispose();

                } else if (sourceText.equals(
                finalPrefs.getString("lang.about"))) {
                    new VSAbout(finalPrefs, VSSimulatorFrame.this);

                } else if (sourceText.equals(
                finalPrefs.getString("lang.quit"))) {
                    System.exit(0);

                } else if (sourceText.equals(
                finalPrefs.getString("lang.start"))) {
                    VSMenuItemStates menuItemState =
                        currentSimulation.getMenuItemStates();
                    menuItemState.setStart(false);
                    menuItemState.setPause(true);
                    menuItemState.setReset(false);
                    menuItemState.setReplay(true);
                    currentSimulation.getSimulatorCanvas().play();
                    updateSimulationMenu();

                } else if (sourceText.equals(
                finalPrefs.getString("lang.pause"))) {
                    VSMenuItemStates menuItemState =
                        currentSimulation.getMenuItemStates();
                    menuItemState.setStart(true);
                    menuItemState.setPause(false);
                    menuItemState.setReset(true);
                    menuItemState.setReplay(true);
                    currentSimulation.getSimulatorCanvas().pause();
                    updateSimulationMenu();

                } else if (sourceText.equals(
                finalPrefs.getString("lang.reset"))) {
                    VSMenuItemStates menuItemState =
                        currentSimulation.getMenuItemStates();
                    menuItemState.setStart(true);
                    menuItemState.setPause(false);
                    menuItemState.setReset(false);
                    menuItemState.setReplay(false);
                    currentSimulation.getSimulatorCanvas().reset();
                    updateSimulationMenu();

                } else if (sourceText.equals(
                finalPrefs.getString("lang.replay"))) {
                    VSMenuItemStates menuItemState =
                        currentSimulation.getMenuItemStates();
                    menuItemState.setStart(false);
                    menuItemState.setPause(true);
                    menuItemState.setReset(false);
                    menuItemState.setReplay(true);
                    currentSimulation.getSimulatorCanvas().reset();
                    currentSimulation.getSimulatorCanvas().play();
                    updateSimulationMenu();
                }
            }
        };

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

    /**
     * Creates the menu bar.
     *
     * @return the j menu bar
     */
    private JMenuBar createMenuBar() {
        /* File menu */
        menuFile = new JMenu(prefs.getString("lang.file"));
        menuFile.setMnemonic(prefs.getInteger("keyevent.file"));
        JMenuItem menuItem;

        menuItem = new JMenuItem(prefs.getString("lang.simulation.new"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.new"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(actionListener);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(
            prefs.getString("lang.simulation.close"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.close"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(actionListener);
        menuFile.add(menuItem);

        menuFile.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.window.new"));
        menuItem.addActionListener(actionListener);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.window.close"));
        menuItem.addActionListener(actionListener);
        menuFile.add(menuItem);


        menuFile.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.about"));
        menuItem.addActionListener(actionListener);
        menuFile.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.quit"));
        menuItem.addActionListener(actionListener);
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
        resetItem.addActionListener(actionListener);
        resetItem.setEnabled(false);
        menuSimulation.add(resetItem);
        resetButton = new JButton(getImageIcon("reset.png",
                                               prefs.getString("lang.reset")));
        resetButton.addActionListener(actionListener);
        toolBar.add(resetButton);

        replayItem = new JMenuItem(
            prefs.getString("lang.replay"));
        replayItem.setAccelerator(KeyStroke.getKeyStroke(
                                      prefs.getInteger("keyevent.replay"),
                                      ActionEvent.ALT_MASK));
        replayItem.addActionListener(actionListener);
        replayItem.setEnabled(false);
        menuSimulation.add(replayItem);
        replayButton = new JButton(
            getImageIcon("replay.png", prefs.getString("lang.replay")));
        replayButton.addActionListener(actionListener);
        toolBar.add(replayButton);

        pauseItem = new JMenuItem(prefs.getString("lang.pause"));
        pauseItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.pause"),
                                     ActionEvent.ALT_MASK));
        pauseItem.addActionListener(actionListener);
        menuSimulation.add(pauseItem);
        pauseItem.setEnabled(false);
        pauseButton = new JButton(getImageIcon("pause.png",
                                               prefs.getString("lang.pause")));
        pauseButton.addActionListener(actionListener);
        toolBar.add(pauseButton);

        startItem = new JMenuItem(prefs.getString("lang.start"));
        startItem.setAccelerator(KeyStroke.getKeyStroke(
                                     prefs.getInteger("keyevent.start"),
                                     ActionEvent.ALT_MASK));
        startItem.addActionListener(actionListener);
        menuSimulation.add(startItem);
        startButton = new JButton(getImageIcon("start.png",
                                               prefs.getString("lang.start")));
        startButton.addActionListener(actionListener);
        toolBar.add(startButton);


        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(menuFile);
        mainMenuBar.add(menuEdit);
        mainMenuBar.add(menuSimulation);

        return mainMenuBar;
    }

    /**
     * Creates the content pane.
     *
     * @return the container
     */
    private Container createContentPane() {
        Container pane = getContentPane();
        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM,
                                     JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                JTabbedPane pane = (JTabbedPane) ce.getSource();
                currentSimulation = (VSSimulator) pane.getSelectedComponent();
                currentSimulation.getSimulatorCanvas().paint();
                updateEditMenu();
                updateSimulationMenu();
            }
        });

        pane.add(toolBar, BorderLayout.PAGE_START);
        pane.add(tabbedPane, BorderLayout.CENTER);

        return pane;
    }

    /**
     * Updates the edit menu. Called if another simulator tab has been selected
     * or if processes have been added or removed.
     */
    public void updateEditMenu() {
        menuEdit.removeAll();

        JMenuItem globalPrefsItem = new JMenuItem(
            prefs.getString("lang.prefs"));
        globalPrefsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                VSPrefs simulationPrefs = currentSimulation.getPrefs();
                VSSimulatorEditor.TAKEOVER_BUTTON = true;
                VSSimulatorEditor simulationEditor = new VSSimulatorEditor(
                    simulationPrefs, VSSimulatorFrame.this, currentSimulation);
                new VSEditorFrame(prefs, VSSimulatorFrame.this,
                                  simulationEditor);
            }
        });
        menuEdit.add(globalPrefsItem);
        menuEdit.addSeparator();

        if (currentSimulation == null)
            return;

        final String processString = prefs.getString("lang.process");
        final ArrayList<VSProcess> arr =
            currentSimulation.getSimulatorCanvas().getProcessesArray();
        final int numProcesses = arr.size();

        int processNum = 0;
        for (VSProcess process : arr) {
            int processID = process.getProcessID();
            JMenuItem processItem = new JMenuItem(processString + " " +
                                                  processID);
            if (processNum < 10)
                processItem.setAccelerator(
                    KeyStroke.getKeyStroke(0x31+processNum,
                                           ActionEvent.ALT_MASK));
            final int finalProcessNum = processNum++;
            processItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    currentSimulation.getSimulatorCanvas().editProcess(
                        finalProcessNum);
                }
            });
            menuEdit.add(processItem);
        }
    }

    /**
     * Updates the simulation menu. Called if the simulator state has changed
     * (e.g. start/play/stop/replay etc)
     */
    public synchronized void updateSimulationMenu() {
        VSMenuItemStates menuItemState = currentSimulation.getMenuItemStates();

        pauseItem.setEnabled(menuItemState.getPause());
        replayItem.setEnabled(menuItemState.getReplay());
        resetItem.setEnabled(menuItemState.getReset());
        startItem.setEnabled(menuItemState.getStart());

        pauseButton.setEnabled(menuItemState.getPause());
        replayButton.setEnabled(menuItemState.getReplay());
        resetButton.setEnabled(menuItemState.getReset());
        startButton.setEnabled(menuItemState.getStart());
    }

    /* (non-Javadoc)
     * @see java.awt.Window#dispose()
     */
    public void dispose() {
        synchronized (simulations) {
            for (VSSimulator simulation : simulations)
                simulation.getSimulatorCanvas().stopThread();
        }
        super.dispose();
    }

    /**
     * Adds the simulation.
     *
     * @param simulation the simulation
     */
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

    /**
     * Removes the simulation.
     *
     * @param simulationToRemove the simulation to remove
     */
    public void removeSimulation(VSSimulator simulationToRemove) {
        if (simulations.size() == 1) {
            dispose();

        } else {
            simulations.remove(simulationToRemove);
            tabbedPane.remove(simulationToRemove);
            simulationToRemove.getSimulatorCanvas().stopThread();
        }
    }

    /**
     * Removes the current simulation.
     */
    private void removeCurrentSimulation() {
        removeSimulation(currentSimulation);
    }

    /**
     * Gets the current simulation.
     *
     * @return the current simulation
     */
    public VSSimulator getCurrentSimulation() {
        return currentSimulation;
    }

    /**
     * Gets the image icon.
     *
     * @param name the name
     * @param descr the descr
     *
     * @return the image icon
     */
    private ImageIcon getImageIcon(String name, String descr) {
        java.net.URL imageURL = getClass().getResource("/icons/"+name);

        if (imageURL == null)
            return new ImageIcon("icons/"+name, descr);


        return new ImageIcon(imageURL, descr);
    }
}
