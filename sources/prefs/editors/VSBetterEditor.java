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

public abstract class VSBetterEditor extends VSEditor {
    private Container contentPane;
    protected VSInfoArea infoArea;
    private String title;

    public VSBetterEditor(VSPrefs prefs, VSPrefs prefsToEdit, String title) {
        super(prefs, prefsToEdit);
        this.title = title;
        this.contentPane = createContentPane();
    }

    public VSBetterEditor(VSPrefs prefs, VSPrefs prefsToEdit, String title, int prefsCategory) {
        super(prefs, prefsToEdit, prefsCategory);
        this.title = title;
        this.contentPane = createContentPane();
    }

    public String getTitle() {
        return title;
    }

    public Container getContentPane() {
        return contentPane;
    }

    private JPanel createContentPane() {
        JPanel panel  = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        infoArea = new VSInfoArea();
        JPanel editPanel = super.editPanel;
        JPanel buttonPanel = createButtonPanel();

        JScrollPane scrollPane = new JScrollPane(editPanel);
        panel.add(infoArea, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
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

    public void actionPerformed(ActionEvent e) {
        //String actionCommand = e.getActionCommand();

        /* More action in the super class!!! */
        super.actionPerformed(e);
    }

    public void newVSEditorInstance(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) { };
}
