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
        fillButtonPanel(editor.getButtonPanel());
        setContentPane(editor.getContentPane());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(prefs.getInteger("div.window.prefs.xsize"),
                prefs.getInteger("div.window.prefs.ysize"));
        setResizable(false);
        setVisible(true);
    }

    private void fillButtonPanel(JPanel buttonPanel) {
        JButton okButton = new JButton(
            prefs.getString("lang.ok"));
        okButton.setMnemonic(prefs.getInteger("keyevent.ok"));
        okButton.addActionListener(this);
        buttonPanel.add(okButton, 0);

        JButton cancelButton = new JButton(
            prefs.getString("lang.cancel"));
        cancelButton.setMnemonic(prefs.getInteger("keyevent.cancel"));
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton, 1);
        buttonPanel.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            editor.actionPerformed(e);
            dispose();

        } else if (actionCommand.equals(prefs.getString("lang.cancel"))) {
            editor.actionPerformed(e);
            dispose();

        } else {
            editor.actionPerformed(e);
        }
    }
}
