/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs.editors;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import core.*;
import protocols.*;
import events.*;
import prefs.VSPrefs;

// TODO: Auto-generated Javadoc
/**
 * The Class VSProcessEditor.
 */
public class VSProcessEditor extends VSBetterEditor {
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
     * @see prefs.editors.VSBetterEditor#addToButtonPanelFront(javax.swing.JPanel)
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

        String protocolString = " " + prefs.getString("lang.protocol");
        for (String protocolClassname : editableProtocolsClassnames) {
            String protocolShortname = VSRegisteredEvents.getShortname(protocolClassname);
            VSProtocol protocol = process.getProtocolObject(protocolClassname);
            addToEditor(protocolShortname + protocolString, protocolShortname, protocol);
        }
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSBetterEditor#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            process.updateFromVSPrefs();

        } else if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();
            process.updateFromVSPrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
