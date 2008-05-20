package simulator;

import java.util.*;
import java.util.regex.*;
import javax.swing.*;

import utils.*;

public class VSLogging {
    private JTextArea loggingArea;
    private String filterText;
    private ArrayList<StringBuffer> pauseLines;
    private ArrayList<StringBuffer> loggingLines;
    private VSSimulationCanvas simulationCanvas;
    private boolean isFiltered;
    private boolean isPaused;
    private Pattern filterPattern;

    public VSLogging() {
        loggingArea = new JTextArea(0, 0);
        loggingArea.setEditable(false);
		loggingArea.setLineWrap(true);
		loggingArea.setWrapStyleWord(true);
        loggingLines = new ArrayList<StringBuffer>();
        pauseLines = new ArrayList<StringBuffer>();
        filterText = "";
    }

    public void setSimulationCanvas(VSSimulationCanvas simulationCanvas) {
        this.simulationCanvas = simulationCanvas;
    }

    public JTextArea getLoggingArea() {
        return loggingArea;
    }

    public void logg(String message) {
        if (simulationCanvas == null)
            logg(message, 0);
        else
            logg(message, simulationCanvas.getTime());
    }

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

    public synchronized void isPaused(boolean isPaused) {
        this.isPaused = isPaused;

        if (!isPaused) {
            for (StringBuffer buffer : pauseLines)
                loggFiltered(buffer);

            pauseLines.clear();
        }
    }

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

    public synchronized void isFiltered(boolean isFiltered) {
        this.isFiltered = isFiltered;

        if (!isFiltered)
            setFilterText("");
        else
            filter();
    }

    public synchronized void setFilterText(String filterText) {
        this.filterText = filterText;
        filter();
    }

    public synchronized void clear() {
        loggingLines.clear();
        pauseLines.clear();
        loggingArea.setText("");
    }

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
