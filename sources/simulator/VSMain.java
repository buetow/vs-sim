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

package simulator;

import java.awt.*;
import java.util.Locale;
import javax.swing.*;

import events.*;
import prefs.*;
import prefs.editors.*;

/**
 * The class VSMain. This class contains the static main method. The simulator
 * starts here!
 *
 * @author Paul C. Buetow
 */
public class VSMain {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VSMain object.
     *
     * @param prefs the prefs
     */
    public VSMain(VSPrefs prefs) {
        init(prefs, null);
    }

    /**
     * Instantiates a new VSMain object
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the window relative to
     */
    public VSMain(VSPrefs prefs, Component relativeTo) {
        init(prefs, relativeTo);
    }

    /**
     * Inits the VSMain object.
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the window relative to
     */
    private void init(VSPrefs prefs, Component relativeTo) {
        VSSimulatorFrame simulatorFrame =
            new VSSimulatorFrame(prefs, relativeTo);
        /*
                VSSimulatorEditor editor =
                    new VSSimulatorEditor(prefs, simulatorFrame,
                                          VSSimulatorEditor.OPENED_NEW_WINDOW);
                new VSEditorFrame(prefs, relativeTo, editor);
        		*/
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        Locale.setDefault(Locale.GERMAN);
        javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        VSPrefs prefs = VSDefaultPrefs.init();
        VSRegisteredEvents.init(prefs);
        new VSMain(prefs);
    }
}
