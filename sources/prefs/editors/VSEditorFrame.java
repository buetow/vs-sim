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

package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
import utils.*;

// TODO: Auto-generated Javadoc
/**
 * The class VSEditorFrame.
 */
public class VSEditorFrame extends VSFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** The editor. */
    private VSAbstractBetterEditor editor;

    /** The prefs. */
    private VSPrefs prefs;

    /**
     * Instantiates a new lang.process.removeeditor frame.
     *
     * @param prefs the prefs
     * @param relativeTo the relative to
     * @param editor the editor
     */
    public VSEditorFrame(VSPrefs prefs, Component relativeTo, VSAbstractBetterEditor editor) {
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
