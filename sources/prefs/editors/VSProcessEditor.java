package prefs.editors;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import core.*;
import protocols.*;
import events.*;
import prefs.VSPrefs;

public class VSProcessEditor extends VSBetterEditor {
    private VSProcess process;
    public static boolean TAKEOVER_BUTTON;
    public VSProcessEditor(VSPrefs prefs, VSProcess process) {
        super(prefs, process, prefs.getString("lang.name") + " - " + prefs.getString("lang.prefs.process"));;
        this.process = process;
        disposeFrameWithParentIfExists();
        makeProtocolVariablesEditable();
    }

    protected void addToButtonPanelFront(JPanel buttonPanel) {
        JButton takeoverButton = new JButton(
            prefs.getString("lang.takeover"));
        takeoverButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
        takeoverButton.addActionListener(this);
        buttonPanel.add(takeoverButton);
    }

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
