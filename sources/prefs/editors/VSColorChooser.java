/*
 * Copyright (c) 2008 Paul Buetow, vs@dev.buetow.org
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
import javax.swing.*;
import javax.swing.event.*;

import prefs.VSPrefs;

// TODO: Auto-generated Javadoc
/**
 * The class VSColorChooser.
 */
public class VSColorChooser extends JPanel implements ChangeListener {
    private static final long serialVersionUID = 1L;

    /** The color chooser. */
    protected JColorChooser colorChooser;

    /** The color. */
    private Color color;

    /** The val field. */
    private JTextField valField;

    /** The prefs. */
    //private VSPrefs prefs;

    /**
     * Instantiates a new lang.process.removecolor chooser.
     *
     * @param prefs the prefs
     * @param valField the val field
     */
    public VSColorChooser(VSPrefs prefs, JTextField valField) {
        super(new BorderLayout());
        //this.prefs = prefs;
        this.color = valField.getBackground();
        this.valField = valField;

        colorChooser = new JColorChooser(Color.yellow);
        colorChooser.setColor(color);
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder(
                                   prefs.getString("lang.colorchooser2")));
        add(colorChooser, BorderLayout.CENTER);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        Color newColor = colorChooser.getColor();
        valField.setBackground(newColor);
        valField.repaint();
    }
}
