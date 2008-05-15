package simulator;

import javax.swing.*;
import java.util.*;

import utils.*;

public class VSLogging {
    private JTextArea loggingArea;
    private VSSimulationPanel simulationPanel;
    private volatile boolean isPaused;
    private StringBuffer pauseBuffer;

    public VSLogging() {
        loggingArea = new JTextArea(0, 0);
        loggingArea.setEditable(false);
        pauseBuffer = new StringBuffer();
    }

    public void setSimulationPanel(VSSimulationPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
    }

    public JTextArea getLoggingArea() {
        return loggingArea;
    }

    public void logg(String message) {
        if (simulationPanel == null)
            logg(message, 0);
        else
            logg(message, simulationPanel.getTime());
    }

    public void logg(String message, long time) {
        if (isPaused) {
            pauseBuffer.append(VSTools.getTimeString(time));
            pauseBuffer.append(": ");
            pauseBuffer.append(message);
            pauseBuffer.append("\n");

        } else {
            loggingArea.append(VSTools.getTimeString(time) + ": " + message + "\n");
            loggingArea.setCaretPosition(loggingArea.getDocument().getLength());
        }
    }

    public void isPaused(boolean isPaused) {
        this.isPaused = isPaused;

        if (!isPaused) {
            loggingArea.append(pauseBuffer.toString());
            loggingArea.setCaretPosition(loggingArea.getDocument().getLength());
            pauseBuffer.delete(0, pauseBuffer.length());
        }
    }
}
