/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs.editors;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import prefs.VSPrefs;

// TODO: Auto-generated Javadoc
/**
 * The Class VSColorChooser.
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
