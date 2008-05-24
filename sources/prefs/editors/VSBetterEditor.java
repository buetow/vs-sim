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
    private VSInfoArea infoArea;
    private String title;

    public VSBetterEditor(VSPrefs prefs, VSPrefs prefsToEdit, String title) {
        super(prefs, prefsToEdit);
        this.title = title;
        this.contentPane = createContentPane();
    }

    public String getTitle() {
        return title;
    }

    public Container getContentPane() {
        contentPane.setBackground(Color.WHITE);
        return contentPane;
    }

    private JPanel createContentPane() {
        JPanel panel  = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        infoArea = new VSInfoArea();
        JPanel editPanel = getEditPanel();
        JPanel buttonPanel = getButtonPanel();

        JScrollPane scrollPane = new JScrollPane(editPanel);
        panel.add(editPanel);
        //panel.add(infoArea);
        panel.add(buttonPanel);

        return panel;
    }

    protected void addToButtonPanelFront(JPanel buttonPanel) { }
    protected void addToButtonPanelLast(JPanel buttonPanel) { }
    protected void addToEditTableLast() { }

    public void actionPerformed(ActionEvent e) {
        //String actionCommand = e.getActionCommand();

        /* More action in the super class!!! */
        super.actionPerformed(e);
    }

    protected VSInfoArea getInfoArea() {
        return infoArea;
    }
}
