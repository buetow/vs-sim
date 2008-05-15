package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class VSInfoArea extends JTextPane {
    public VSInfoArea() {
        initialize();
    }

    public VSInfoArea(String text) {
        setText(text);
        initialize();
    }

    private void initialize() {
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        setBorder(new CompoundBorder(
                      new LineBorder(Color.BLACK),
                      new EmptyBorder(15, 15, 15, 15)));
        setBackground(Color.WHITE);
    }
}
