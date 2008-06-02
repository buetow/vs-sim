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

import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import core.*;
import protocols.*;
import events.*;
import prefs.VSPrefs;

/**
 * The class VSProcessEditor.
 */
public class VSProcessEditor extends VSAbstractBetterEditor {
    private static final long serialVersionUID = 1L;

    /** The process. */
    private VSProcess process;

    /** The TAKEOVE r_ button. */
    public static boolean TAKEOVER_BUTTON;

    /**
     * Instantiates a new lang.process.removeprocess editor.
     *
     * @param prefs the prefs
     * @param process the process
     */
    public VSProcessEditor(VSPrefs prefs, VSProcess process) {
        super(prefs, process, prefs.getString("lang.name") + " - " + prefs.getString("lang.prefs.process"));;
        this.process = process;
        disposeFrameWithParentIfExists();
        makeProtocolVariablesEditable();
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractBetterEditor#addToButtonPanelFront(javax.swing.JPanel)
     */
    protected void addToButtonPanelFront(JPanel buttonPanel) {
        JButton takeoverButton = new JButton(
            prefs.getString("lang.takeover"));
        takeoverButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
        takeoverButton.addActionListener(this);
        buttonPanel.add(takeoverButton);
    }

    /**
     * Make protocol variables editable.
     */
    protected void makeProtocolVariablesEditable() {
        ArrayList<String> editableProtocolsClassnames =
            VSRegisteredEvents.getEditableProtocolsClassnames();

        //String protocolString = " " + prefs.getString("lang.protocol");
        String clientString = " " + prefs.getString("lang.client");
        String serverString = " " + prefs.getString("lang.server");

        for (String protocolClassname : editableProtocolsClassnames) {
            String protocolShortname = VSRegisteredEvents.getShortnameByClassname(protocolClassname);
            VSAbstractProtocol protocol = process.getProtocolObject(protocolClassname);
            protocol.onClientInit();
            protocol.onServerInit();

            ArrayList<String> clientVariables = VSRegisteredEvents.getProtocolClientVariables(protocolClassname);
            if (clientVariables != null)
                addToEditor(protocolShortname + clientString, protocolShortname, protocol, clientVariables);

            ArrayList<String> serverVariables = VSRegisteredEvents.getProtocolServerVariables(protocolClassname);
            if (serverVariables != null)
                addToEditor(protocolShortname + serverString, protocolShortname, protocol, serverVariables);
        }
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractBetterEditor#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            process.updateFromPrefs();

        } else if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();
            process.updateFromPrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
