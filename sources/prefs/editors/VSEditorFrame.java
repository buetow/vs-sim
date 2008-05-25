/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
import utils.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VSEditorFrame.
 */
public class VSEditorFrame extends VSFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** The editor. */
    private VSBetterEditor editor;

    /** The prefs. */
    private VSPrefs prefs;

    /**
     * Instantiates a new lang.process.removeeditor frame.
     *
     * @param prefs the prefs
     * @param relativeTo the relative to
     * @param editor the editor
     */
    public VSEditorFrame(VSPrefs prefs, Component relativeTo, VSBetterEditor editor) {
        super(editor.getTitle(), relativeTo);
        this.prefs = prefs;
        this.editor = editor;
        init();
    }

    /**
     * Inits the.
     */
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

    /**
     * Fill button panel.
     *
     * @param buttonPanel the button panel
     */
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

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
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
