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

package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The class VSInfoArea, an object of this class is used for some information
 *areas. E.g. in the VSAbout class.
 *
 * @author Paul C. Buetow
 */
public class VSInfoArea extends JTextPane {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VSInfoArea.
     */
    public VSInfoArea() {
        init();
    }

    /**
     * Instantiates a new VSInfoArea.
     *
     * @param text the text to display
     */
    public VSInfoArea(String text) {
        setText(text);
        init();
    }

    /**
     * Inits the info area.
     */
    private void init() {
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        setBorder(new CompoundBorder(
                      new LineBorder(Color.BLACK),
                      new EmptyBorder(15, 15, 15, 15)));
        setBackground(Color.WHITE);
    }
}
