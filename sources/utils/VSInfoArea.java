/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VSInfoArea.
 */
public class VSInfoArea extends JTextPane {
	private static final long serialVersionUID = 1L;
	
    /**
     * Instantiates a new vS info area.
     */
    public VSInfoArea() {
        init();
    }

    /**
     * Instantiates a new vS info area.
     * 
     * @param text the text
     */
    public VSInfoArea(String text) {
        setText(text);
        init();
    }

    /**
     * Inits the.
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
