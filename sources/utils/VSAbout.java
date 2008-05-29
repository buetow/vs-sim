/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
import utils.*;

/**
 * The Class VSAbout. This class is only for the about window which shows up
 * if selected in the GUI.
 *
 * @author Paul C. Buetow
 */
public class VSAbout extends VSFrame {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The prefs. */
    private VSPrefs prefs;

    /**
     * Instantiates a new VSAbout object.
     *
     * @param prefs the prefs
     * @param relativeTo the component to open the about window relative to
     */
    public VSAbout(VSPrefs prefs, Component relativeTo) {
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
            prefs.getString("lang.about.info!"));
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
