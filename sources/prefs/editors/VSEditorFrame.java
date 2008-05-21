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

public class VSEditorFrame extends VSFrame implements ActionListener {
    private VSBetterEditor editor;
    private VSPrefs prefs;

    public VSEditorFrame(VSPrefs prefs, Component relativeTo, VSBetterEditor editor) {
        super(editor.getTitle(), relativeTo);
        this.prefs = prefs;
        this.editor = editor;
        init();
    }

    private void init() {
        editor.setFrame(this);
        setJMenuBar(createJMenuBar());
        setContentPane(editor.getContentPane());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(prefs.getInteger("window.prefs.xsize"),
                prefs.getInteger("window.prefs.ysize"));
        setResizable(false);
        setVisible(true);
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

        if (!(editor instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuItem = new JMenuItem(
            prefs.getString("lang.saveas"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.saveas"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        if (!(editor instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuItem = new JMenuItem(
            prefs.getString("lang.open"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                    prefs.getInteger("keyevent.open"),
                                    ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menuFile.add(menuItem);

        if (!(editor instanceof VSSimulationEditor))
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

        if (!(editor instanceof VSSimulationEditor))
            menuItem.setEnabled(false);

        menuEdit.addSeparator();

        menuItem = new JMenuItem(prefs.getString("lang.prefs"));
        /*
        if (editor.getOrefsCategory() == ALL_PREFERENCES) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                        prefs.getInteger("keyevent.prefs"),
                                        ActionEvent.ALT_MASK));
            menuItem.addActionListener(this);
        }
        */
        menuItem.setEnabled(false);
        menuEdit.add(menuItem);

        menuItem = new JMenuItem(prefs.getString("lang.prefs.ext"));
        if (editor.getPrefsCategory() == VSEditor.SIMULATION_PREFERENCES) {
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
            dispose();

        } else if (actionCommand.equals(prefs.getString("lang.saveas"))) {
            JFileChooser fileChooser = new JFileChooser();
            int ret = fileChooser.showSaveDialog(this);

            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                editor.savePrefs();
                prefs.saveFile(file.getName());
            }

        } else if (actionCommand.equals(prefs.getString("lang.open"))) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int ret = fileChooser.showOpenDialog(this);

            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                VSPrefs newPrefs = VSDefaultPrefs.init(file.getName());
                editor.setPrefs(newPrefs);
                editor.setPrefsToEdit(newPrefs);
                editor.resetEditPanel();
            }

        } else if (actionCommand.equals(prefs.getString("lang.cancel"))) {
            dispose();

        } else if (actionCommand.equals(prefs.getString("lang.prefs"))) {
            editor.newVSEditorInstance(prefs, prefs, VSEditor.SIMULATION_PREFERENCES);

        } else if (actionCommand.equals(prefs.getString("lang.prefs.ext"))) {
            editor.newVSEditorInstance(prefs, prefs, VSEditor.ALL_PREFERENCES);

        } else {
            editor.actionPerformed(e);
        }
    }
}
