/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class VSFrame.
 */
public class VSFrame extends JFrame {
    
    /** The Constant X_LOCATION_OFFSET. */
    private final static int X_LOCATION_OFFSET = 40;
    
    /** The Constant Y_LOCATION_OFFSET. */
    private final static int Y_LOCATION_OFFSET = 80;
    
    /** The parent. */
    private Component parent;
    
    /** The dispose. */
    private boolean dispose;

    /**
     * Instantiates a new vS frame.
     * 
     * @param title the title
     * @param parent the parent
     */
    public VSFrame(String title, Component parent) {
        super(title);
        init(parent);
    }

    /**
     * Instantiates a new vS frame.
     * 
     * @param title the title
     */
    public VSFrame(String title) {
        super(title);
        init(null);
    }

    /**
     * Inits the.
     * 
     * @param parent the parent
     */
    private void init(Component parent) {
        this.parent = parent;
        this.dispose = false;
    }

    /**
     * Dispose with parent.
     */
    public void disposeWithParent() {
        if (!dispose && parent != null && parent instanceof Window) {
            Window window = (Window) parent;
            window.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent we) {
                    VSFrame.this.dispose();
                }
            });
        }
        dispose = true;
    }

    /**
     * Sets the correct location.
     */
    private void setCorrectLocation() {
        int x = 0, y = 0;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (parent == null) {
            x = (int) (screenSize.width - getWidth()) / 2;
            y = 50;//(int) (screenSize.height - getHeight()) / 2;

        } else {
            final Point location = parent.getLocation();
            x = (int) location.getX() + X_LOCATION_OFFSET;
            y = (int) location.getY() + Y_LOCATION_OFFSET;
        }

        if (x + super.getWidth() >= screenSize.width)
            x = screenSize.width - super.getWidth();
        else if (x < 0)
            x = 0;

        if (y + super.getHeight() >= screenSize.height)
            y = screenSize.height - super.getHeight();

        super.setLocation(x, y);
    }

    /* (non-Javadoc)
     * @see java.awt.Window#setSize(int, int)
     */
    public void setSize(int width, int height) {
        super.setSize(width, height);
        setCorrectLocation();
    }
}
