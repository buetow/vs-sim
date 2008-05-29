/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The Class VSInfoArea. An object of this class is used for some information
 *areas. E.g. in the VSAbout class.
 *
 * @author Paul C. Buetow
 */
public class VSInfoArea extends JTextPane {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VSInfoArea.
     */
    public VSInfoArea() {
        init();
    }

    /**
     * Instantiates a new VSInfoArea.
     *
     * @param text the text to display
     */
    public VSInfoArea(String text) {
        setText(text);
        init();
    }

    /**
     * Inits the info area.
     */
    private void init() {
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        setBorder(new CompoundBorder(
                      new LineBorder(Color.BLACK),
                      new EmptyBorder(15, 15, 15, 15)));
        setBackground(Color.WHITE);
    }
}
