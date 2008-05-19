package utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class VSFrame extends JFrame {
    private final static int X_LOCATION_OFFSET = 40;
    private final static int Y_LOCATION_OFFSET = 80;
    private Component parent;
    private boolean dispose;

    public VSFrame(String title, Component parent) {
        super(title);
        init(parent);
    }

    public VSFrame(String title) {
        super(title);
        init(null);
    }

    private void init(Component parent) {
        this.parent = parent;
        this.dispose = false;
    }

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

    public void setSize(int width, int height) {
        super.setSize(width, height);
        setCorrectLocation();
    }
}
