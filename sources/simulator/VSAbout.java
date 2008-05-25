package simulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
import utils.*;

public class VSAbout extends VSFrame implements ActionListener {
    private VSPrefs prefs;

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


    public Container createContentPane() {
        Container contentPane = getContentPane();

        VSInfoArea infoArea = new VSInfoArea(prefs.getString("lang.about.info!"));
        JPanel buttonPane = createButtonPane();
        JScrollPane scrollPane = new JScrollPane(infoArea);

        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        return contentPane;
    }

    public JPanel createButtonPane() {
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(Color.WHITE);

        JButton closeButton = new JButton(
            prefs.getString("lang.close"));
        closeButton.setMnemonic(prefs.getInteger("keyevent.close"));
        closeButton.addActionListener(this);
        buttonPane.add(closeButton);

        return buttonPane;
    }


    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.close")))
            dispose();
    }
}
