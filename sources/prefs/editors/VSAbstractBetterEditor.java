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
 * The class VSAbstractBetterEditor.
 */
public abstract class VSAbstractBetterEditor extends VSAbstractEditor {
    private static final long serialVersionUID = 1L;

    /** The content pane. */
    private Container contentPane;

    /** The info area. */
    private VSInfoArea infoArea;

    /** The title. */
    private String title;

    /**
     * Instantiates a new lang.process.removebetter editor.
     *
     * @param prefs the prefs
     * @param prefsToEdit the prefs to edit
     * @param title the title
     */
    public VSAbstractBetterEditor(VSPrefs prefs, VSPrefs prefsToEdit, String title) {
        super(prefs, prefsToEdit);
        this.title = title;
        this.contentPane = createContentPane();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the content pane.
     *
     * @return the content pane
     */
    public Container getContentPane() {
        contentPane.setBackground(Color.WHITE);
        return contentPane;
    }

    /**
     * Creates the content pane.
     *
     * @return the j panel
     */
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

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#addToButtonPanelFront(javax.swing.JPanel)
     */
    protected void addToButtonPanelFront(JPanel buttonPanel) { }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#addToButtonPanelLast(javax.swing.JPanel)
     */
    protected void addToButtonPanelLast(JPanel buttonPanel) { }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#addToEditTableLast()
     */
    protected void addToEditTableLast() { }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        //String actionCommand = e.getActionCommand();

        /* More action in the super class!!! */
        super.actionPerformed(e);
    }

    /**
     * Gets the info area.
     *
     * @return the info area
     */
    protected VSInfoArea getInfoArea() {
        return infoArea;
    }
}
