/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package simulator;

import java.util.*;
import java.util.regex.*;
import javax.swing.*;

import utils.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VSLogging.
 */
public class VSLogging {
    
    /** The logging area. */
    private JTextArea loggingArea;
    
    /** The filter text. */
    private String filterText;
    
    /** The pause lines. */
    private ArrayList<StringBuffer> pauseLines;
    
    /** The logging lines. */
    private ArrayList<StringBuffer> loggingLines;
    
    /** The simulation canvas. */
    private VSSimulatorCanvas simulationCanvas;
    
    /** The is filtered. */
    private boolean isFiltered;
    
    /** The is paused. */
    private boolean isPaused;
    
    /** The filter pattern. */
    private Pattern filterPattern;

    /**
     * Instantiates a new vS logging.
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
     * @param simulationCanvas the new simulation canvas
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
     * Logg.
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
     * Logg.
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
     * Checks if is paused.
     * 
     * @param isPaused the is paused
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
     * Logg filtered.
     * 
     * @param buffer the buffer
     */
    private void loggFiltered(StringBuffer buffer) {
        loggingLines.add(buffer);
        if (!isFiltered) {
            loggingArea.append(buffer.toString()+"\n");
            loggingArea.setCaretPosition(loggingArea.getDocument().getLength());

        } else if (filterPattern != null && filterPattern.matcher(buffer).find()) {
            loggingArea.append(buffer.toString()+"\n");
            loggingArea.setCaretPosition(loggingArea.getDocument().getLength());
        }
    }

    /**
     * Checks if is filtered.
     * 
     * @param isFiltered the is filtered
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
     * Clear.
     */
    public synchronized void clear() {
        loggingLines.clear();
        pauseLines.clear();
        loggingArea.setText("");
    }

    /**
     * Filter.
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
