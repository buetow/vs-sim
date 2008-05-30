/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package simulator;

import java.util.*;
import java.util.regex.*;
import javax.swing.*;

import utils.*;

/**
 * The class VSLogging, an object of this class is responsible for the logging
 * of text messages into the simulator's logging window.
 *
 * @author Paul C. Buetow
 */
public class VSLogging {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The logging area. */
    private JTextArea loggingArea;

    /** The filter text. */
    private String filterText;

    /** The pause lines. Used for cacheing the logging if the logging is
     * deactivated for a while
     */
    private ArrayList<StringBuffer> pauseLines;

    /** The logging lines. */
    private ArrayList<StringBuffer> loggingLines;

    /** The simulation canvas. */
    private VSSimulatorCanvas simulationCanvas;

    /** The logging messages are filtered. */
    private boolean isFiltered;

    /** The logging is paused. */
    private boolean isPaused;

    /** The filter pattern. */
    private Pattern filterPattern;

    /**
     * Instantiates a new VSLogging object.
     */
    public VSLogging() {
        loggingArea = new JTextArea(0, 0);
        loggingArea.setEditable(false);
        loggingArea.setLineWrap(true);
        loggingArea.setWrapStyleWord(true);
        loggingLines = new ArrayList<StringBuffer>();
        pauseLines = new ArrayList<StringBuffer>();
        filterText = "";
    }

    /**
     * Sets the simulation canvas.
     *
     * @param simulationCanvas the simulation canvas
     */
    public void setSimulationCanvas(VSSimulatorCanvas simulationCanvas) {
        this.simulationCanvas = simulationCanvas;
    }

    /**
     * Gets the logging area.
     *
     * @return the logging area
     */
    public JTextArea getLoggingArea() {
        return loggingArea;
    }

    /**
     * Loggs a message using the global time.
     *
     * @param message the message
     */
    public void logg(String message) {
        if (simulationCanvas == null)
            logg(message, 0);
        else
            logg(message, simulationCanvas.getTime());
    }

    /**
     * Loggs a message using the specified time.
     *
     * @param message the message
     * @param time the time
     */
    public synchronized void logg(String message, long time) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(VSTools.getTimeString(time));
        buffer.append(": ");
        buffer.append(message);

        if (isPaused)
            pauseLines.add(buffer);
        else
            loggFiltered(buffer);
    }

    /**
     * Sets if the logging is paused.
     *
     * @param isPaused true, if the logging is paused
     */
    public synchronized void isPaused(boolean isPaused) {
        this.isPaused = isPaused;

        if (!isPaused) {
            for (StringBuffer buffer : pauseLines)
                loggFiltered(buffer);

            pauseLines.clear();
        }
    }

    /**
     * If the logging is filtered, it's using the pattern matching.
     *
     * @param buffer the logging buffer to filter
     */
    private void loggFiltered(StringBuffer buffer) {
        loggingLines.add(buffer);
        if (!isFiltered) {
            loggingArea.append(buffer.toString()+"\n");
            loggingArea.setCaretPosition(
                loggingArea.getDocument().getLength());

        } else if (filterPattern != null &&
                   filterPattern.matcher(buffer).find()) {
            loggingArea.append(buffer.toString()+"\n");
            loggingArea.setCaretPosition(
                loggingArea.getDocument().getLength());
        }
    }

    /**
     * Checks if the logging is filtered.
     *
     * @param isFiltered true, if the logging is filtered
     */
    public synchronized void isFiltered(boolean isFiltered) {
        this.isFiltered = isFiltered;

        if (!isFiltered)
            setFilterText("");
        else
            filter();
    }

    /**
     * Sets the filter text.
     *
     * @param filterText the new filter text
     */
    public synchronized void setFilterText(String filterText) {
        this.filterText = filterText;
        filter();
    }

    /**
     * Clears the logging.
     */
    public synchronized void clear() {
        loggingLines.clear();
        pauseLines.clear();
        loggingArea.setText("");
    }

    /**
     * Filters the logging.
     */
    private void filter() {
        try {
            filterPattern = Pattern.compile(filterText);
            StringBuffer buffer = new StringBuffer();

            for (StringBuffer line : loggingLines) {
                if (isFiltered) {
                    Matcher matcher = filterPattern.matcher(line);
                    if (matcher.find()) {
                        buffer.append(line);
                        buffer.append("\n");
                    }
                } else {
                    buffer.append(line);
                    buffer.append("\n");
                }
            }
            loggingArea.setText(buffer.toString());

        } catch (Exception e) {
            filterPattern = null;
            loggingArea.setText("");
        }
    }
}
