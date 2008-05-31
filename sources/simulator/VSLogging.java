/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 *
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
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

    /** The simulator canvas. */
    private VSSimulatorCanvas simulatorCanvas;

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
     * Sets the simulator canvas.
     *
     * @param simulatorCanvas the simulator canvas
     */
    public void setSimulatorCanvas(VSSimulatorCanvas simulatorCanvas) {
        this.simulatorCanvas = simulatorCanvas;
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
        if (simulatorCanvas == null)
            logg(message, 0);
        else
            logg(message, simulatorCanvas.getTime());
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
