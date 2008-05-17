package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.File;

import prefs.*;
import simulator.*;
import utils.*;

public abstract class VSEditorFrame extends VSEditor {
    protected VSInfoArea infoArea;
    protected VSFrame frame;

    public VSEditorFrame(VSPrefs prefs, Component relativeTo, VSPrefs prefsToEdit, String title) {
        super(prefs, prefsToEdit);
        frame = new VSFrame(title, relativeTo);
        init();
    }

    public VSEditorFrame(VSPrefs prefs, Component relativeTo, VSPrefs prefsToEdit, String title, int prefsCategory) {
        super(prefs, prefsToEdit, prefsCategory);
        frame = new VSFrame(title, relativeTo);
        init();
    }

    private void init() {
        frame.setJMenuBar(createJMenuBar());
        frame.setContentPane(createContentPane());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(prefs.getInteger("window.prefs.xsize"),
                      prefs.getInteger("window.prefs.ysize"));
        frame.setResizable(false);
        frame.setVisible(true);

        /*
        frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
         	Window window = we.getWindow();
         }
        });
        */
    }

    private Container createContentPane() {
        Container contentPane  = frame.getContentPane();

        infoArea = new VSInfoArea();
        JPanel editPanel = super.editPanel;
        JPanel buttonPanel = createButtonPanel();

        JScrollPane scrollPane = new JScrollPane(editPanel);
        contentPane.add(infoArea, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        return contentPane;
    }

    protected void addToEditPanelFront(JPanel editPanel) { }

    protected void addToEditPanelLast(JPanel editPanel) { }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = super.buttonPanel;

        JButton cancelButton = new JButton(
            prefs.getString("lang.cancel"));
        cancelButton.setMnemonic(prefs.getInteger("keyevent.cancel"));
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    private JMenuBar createJMenuBar() {
        /* File menu */
        JMenu menuFile = new JMenu(
            prefs.getString("lang.file"));
        menuFile.setMnemonic(prefs.getInteger("keyevent.file"));
        JMenuItem menuItem;

        menuItem = new JMenuItem(
            prefs.getString("lang.save"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.save"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        if (!(this instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuItem = new JMenuItem(
            prefs.getString("lang.saveas"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.saveas"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        if (!(this instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuItem = new JMenuItem(
            prefs.getString("lang.open"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.open"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        if (!(this instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuFile.addSeparator();

        menuItem = new JMenuItem(
            prefs.getString("lang.close"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.close"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        /* Edit menu */
        JMenu menuEdit = new JMenu(
            prefs.getString("lang.edit"));
        menuEdit.setMnemonic(prefs.getInteger("keyevent.edit"));

        menuItem = new JMenuItem(
            prefs.getString("lang.default"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.default"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuEdit.add(menuItem);

        if (!(this instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuEdit.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.prefs"));
        /*
        if (super.prefsCategory == ALL_PREFERENCES) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                        prefs.getInteger("keyevent.prefs"),
                                        ActionEvent.ALT_MASK));
            menuItem.addActionListener(this);
        }
        */
        menuItem.setEnabled(false);
        menuEdit.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.prefs.ext"));
        if (super.prefsCategory == SIMULATION_PREFERENCES) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                        prefs.getInteger("keyevent.prefs.ext"),
                                        ActionEvent.ALT_MASK));
            menuItem.addActionListener(this);
        } else {
            menuItem.setEnabled(false);
        }
        menuEdit.add(menuItem);

        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(menuFile);
        mainMenuBar.add(menuEdit);

        return mainMenuBar;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.close"))) {
            frame.dispose();

        } else if (actionCommand.equals(prefs.getString("lang.saveas"))) {
            JFileChooser fileChooser = new JFileChooser();
            int ret = fileChooser.showSaveDialog(frame);

            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                savePrefs();
                prefs.saveFile(file.getName());
            }

        } else if (actionCommand.equals(prefs.getString("lang.open"))) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int ret = fileChooser.showOpenDialog(frame);

            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                prefsToEdit = prefs = VSDefaultPrefs.init(file.getName());
                resetEditPanel();
            }

        } else if (actionCommand.equals(prefs.getString("lang.cancel"))) {
            frame.dispose();

        } else if (actionCommand.equals(prefs.getString("lang.prefs"))) {
            newVSEditorInstance(prefs, prefs, VSEditor.SIMULATION_PREFERENCES);

        } else if (actionCommand.equals(prefs.getString("lang.prefs.ext"))) {
            newVSEditorInstance(prefs, prefs, VSEditor.ALL_PREFERENCES);

        } else {
            /* More action in the super class!!! */
            super.actionPerformed(e);
        }
    }

    public void newVSEditorInstance(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) { };

    protected VSFrame getFrame() {
        return frame;
    }
}
