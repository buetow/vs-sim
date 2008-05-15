package editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;

import prefs.VSPrefs;

public class VSColorChooser extends JPanel implements ChangeListener {
    protected JColorChooser colorChooser;
    private Color color;
    private JTextField valField;
    private VSPrefs prefs;

    public VSColorChooser(VSPrefs prefs, JTextField valField) {
        super(new BorderLayout());
        this.prefs = prefs;
        this.color = valField.getBackground();
        this.valField = valField;

        colorChooser = new JColorChooser(Color.yellow);
        colorChooser.setColor(color);
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder(
                                   prefs.getString("lang.colorchooser2")));
        add(colorChooser, BorderLayout.CENTER);
    }

    public void stateChanged(ChangeEvent e) {
        Color newColor = colorChooser.getColor();
        valField.setBackground(newColor);
        valField.repaint();
    }
}
