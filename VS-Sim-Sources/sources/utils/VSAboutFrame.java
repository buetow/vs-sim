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

package utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
//import utils.*;

/**
 * The class VSAboutFrame. This class is only for the about window which
 * shows up if selected in the GUI.
 *
 * @author Paul C. Buetow
 */
public class VSAboutFrame extends VSFrame {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The prefs. */
    private VSPrefs prefs;

    /**
     * Instantiates a new VSAboutFrame object.
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the about window relative to
     */
    public VSAboutFrame(VSPrefs prefs, Component relativeTo) {
        super(prefs.getString("lang.name") + " - "
              + prefs.getString("lang.about"), relativeTo);
        this.prefs = prefs;

        disposeWithParent();
        setContentPane(createContentPane());
        setSize(350, 250);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Creates the content pane.
     *
     * @return the container
     */
    public Container createContentPane() {
        Container contentPane = getContentPane();

        VSInfoArea infoArea = new VSInfoArea(
            prefs.getString("lang.about.info"));
        JPanel buttonPane = createButtonPanel();
        JScrollPane scrollPane = new JScrollPane(infoArea);

        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        return contentPane;
    }

    /**
     * Creates the button panel.
     *
     * @return the panel
     */
    public JPanel createButtonPanel() {
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(Color.WHITE);

        JButton closeButton = new JButton(
            prefs.getString("lang.close"));
        closeButton.setMnemonic(prefs.getInteger("keyevent.close"));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if (actionCommand.equals(prefs.getString("lang.close")))
                    dispose();
            }
        });
        buttonPane.add(closeButton);

        return buttonPane;
    }
}
