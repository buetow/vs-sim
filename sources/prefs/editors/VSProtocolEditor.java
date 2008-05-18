package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import protocols.*;
import utils.*;
import core.*;
import prefs.VSPrefs;

public class VSProtocolEditor extends VSEditorFrame {
    private VSProtocol protocol;
    private JTextField textField;

    public VSProtocolEditor(VSPrefs prefs, Component relativeTo, VSProtocol protocol) {
        super(prefs, relativeTo, protocol, prefs.getString("name") + " - "
              + protocol.getName() + " " + prefs.getString("lang.editor"), ALL_PREFERENCES);

        this.protocol = protocol;
        init();
    }

    private void init() {
        super.getFrame().disposeWithParent();
        super.infoArea.setText(prefs.getString("lang.prefs.protocol.info!"));
        createButtonPanel();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = super.buttonPanel;

        JButton cancelButton = new JButton(
            prefs.getString("lang.takeover"));
        cancelButton.setMnemonic(prefs.getInteger("keyevent.takeover"));
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();
            frame.dispose();

        } else if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();

        } else {
            super.actionPerformed(e);
        }
    }
}
