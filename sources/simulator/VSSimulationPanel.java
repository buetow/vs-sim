package simulator;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

import core.*;
import core.time.*;
import events.*;
import events.implementations.*;
import prefs.*;
import prefs.editors.*;
import utils.*;

public class VSSimulationPanel extends JPanel implements Runnable, MouseMotionListener, MouseListener, HierarchyBoundsListener  {
    private VSProcess highlightedProcess;
    private VSSimulation simulation;
    private VSPrefs prefs;
    private VSLogging logging;
    private int numProcesses;
    private int secondsSpaceing;
    private int threadSleep;
    private long untilTime;
    private volatile boolean isPaused = false;
    private volatile boolean isFinalized = false;
    private volatile boolean isFinished = false;
    private volatile boolean isResetted = false;
    private volatile boolean showLamport = false;
    private volatile boolean showVectorTime = false;
    private volatile long pauseTime;
    private volatile long startTime;
    private volatile long time;
    private volatile long lastTime;
    private VSTaskManager taskManager;
    private LinkedList<VSMessageLine> messageLines;
    private LinkedList<VSProcess> processes;

    /* Static constats */
    private static final int LINE_WIDTH = 5;
    private static final int SEPLINE_WIDTH = 2;
    private static final int XOFFSET = 50;
    private static final int YOFFSET = 30;
    private static final int YOUTER_SPACEING = 15;
    private static final int YSEPLINE_SPACEING = 20;
    private static final int TEXT_SPACEING = 10;
    private static final int ROW_HEIGHT = 14;

    /* Constats, which have to get calculated once after start */
    private Color processlineColor;
    private Color processSecondlineColor;
    private Color processSeplineColor;
    private Color messageArrivedColor;
    private Color messageSendingColor;
    private Color messageLostColor;

    private class VSMessageLine {
        private VSProcess receiverProcess;
        private Color color;
        private long sendTime;
        private long recvTime;
        private int senderNum;
        private int receiverNum;
        private int offset1;
        private int offset2;
        private boolean isArrived;
        private boolean isLost;
        private double x1;
        private double y1;
        private double x2;
        private double y2;
        private double x;
        private double y;
        private long outageTime;
        private long z;

        public VSMessageLine(VSProcess receiverProcess, long sendTime, long recvTime, long outageTime, int senderNum , int receiverNum) {
            this.receiverProcess = receiverProcess;
            this.sendTime = sendTime;
            this.recvTime = recvTime;
            this.outageTime = outageTime;
            this.senderNum = senderNum;
            this.receiverNum = receiverNum;
            this.isArrived = false;
            this.isLost = false;

            if (senderNum > receiverNum) {
                //offset1 = 1;
                offset2 = LINE_WIDTH;
            } else {
                offset1 = LINE_WIDTH - 1;
                //offset2 = 1;
            }

            /* Needed if the message gets lost after 0ms */
            this.x = getTimeXPosition(sendTime);
            this.y = getProcessYPosition(senderNum) + offset1;

            recalcOnWindowChanged();
        }

        public void recalcOnWindowChanged() {
            x1 = getTimeXPosition(sendTime);
            y1 = getProcessYPosition(senderNum) + offset1;
            x2 = getTimeXPosition(recvTime);
            y2 = getProcessYPosition(receiverNum) + offset2;

            if (isLost) {
                x = getTimeXPosition(z);
                y = y1 + ( ( (y2-y1) / (x2-x1)) * (x-x1));
            }
        }

        public void draw(final Graphics2D g, final long globalTime) {
            if (isArrived) {
                g.setColor(color);
                g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

            } else if (isLost) {
                g.setColor(messageLostColor);
                g.drawLine((int) x1, (int) y1, (int) x, (int) y);

            } else if (globalTime >= recvTime) {
                isArrived = true;
                if (receiverProcess.isCrashed())
                    color = messageLostColor;
                else
                    color = messageArrivedColor;
                draw(g, globalTime);

            } else if (outageTime >= 0 && outageTime <= globalTime){
                isLost = true;
                draw(g, globalTime);;

            } else {
                z = globalTime;
                x = globalTimeXPosition;
                y = y1 + ( ( (y2-y1) / (x2-x1)) * (x-x1));
                g.setColor(messageSendingColor);
                g.drawLine((int) x1, (int) y1, (int) x, (int) y);
            }
        }
    }

    public VSSimulationPanel(VSPrefs prefs, VSSimulation simulation, VSLogging logging) {
        this.prefs = prefs;
        this.simulation = simulation;
        this.logging = logging;
        this.taskManager = new VSTaskManager(prefs);
        this.messageLines = new LinkedList<VSMessageLine>();
        this.processes = new LinkedList<VSProcess>();

        numProcesses = prefs.getInteger("sim.process.num");
        untilTime = prefs.getInteger("sim.seconds") * 1000;
        recalcOnWindowChanged();

        secondsSpaceing = (int) untilTime / 15000;
        if (secondsSpaceing == 0)
            secondsSpaceing = 1;

        threadSleep = (int) untilTime / 7500;
        if (threadSleep == 0)
            threadSleep = 1;

        VSProcess.resetProcessCounter();
        for (int i = 0; i < numProcesses; ++i)
            createProcess(i);

        addMouseListener(this);
        addMouseMotionListener(this);
        addHierarchyBoundsListener(this);
    }

    double xPaintSize;
    double paintSize;
    double yDistance;
    double globalTimeXPosition;

    int xoffset_plus_xpaintsize;
    double xpaintsize_dividedby_untiltime;
    int paintProcessesOffset;

    int paintSecondlinesSeconds;
    int paintSecondlinesLine[] = new int[4];
    int paintSecondlinesYStringPos1;
    int paintSecondlinesYStringPos2;
    int paintGlobalTimeYPosition;

    /* This method contains very ugly code. But this has to be in order to gain performance! */
    private void recalcOnWindowChanged() {
        processlineColor = prefs.getColor("process.line");
        processSecondlineColor = prefs.getColor("process.secondline");
        processSeplineColor = prefs.getColor("process.sepline");
        messageArrivedColor = prefs.getColor("message.arrived");
        messageSendingColor = prefs.getColor("message.sending");
        messageLostColor = prefs.getColor("message.lost");

        paintSize = simulation.getPaintSize();
        xPaintSize = simulation.getWidth() - (3 * XOFFSET + simulation.getSplitSize());
        yDistance = (simulation.getPaintSize() - 2 * (YOFFSET + YOUTER_SPACEING))/ numProcesses;
        xpaintsize_dividedby_untiltime = xPaintSize / (double) untilTime;

        for (VSMessageLine messageLine : messageLines)
            messageLine.recalcOnWindowChanged();

        /* paintProcesses optimization, precalc things */
        {
            xoffset_plus_xpaintsize = XOFFSET + (int) xPaintSize;
            if (numProcesses > 1)
                paintProcessesOffset = (int) ((paintSize-2*(YOFFSET+YOUTER_SPACEING+YSEPLINE_SPACEING))/(numProcesses-1));
            else
                paintProcessesOffset = (int) ((paintSize-2*(YOFFSET+YOUTER_SPACEING+YSEPLINE_SPACEING)));
        }

        /* paintSecondlines optimization, precalc things */
        {
            int yMax = YOFFSET + YOUTER_SPACEING + (int) (numProcesses * yDistance);
            paintSecondlinesSeconds = (int) untilTime / 1000;
            paintSecondlinesLine[1] = YOFFSET;
            paintSecondlinesLine[3] = yMax;
            paintSecondlinesYStringPos1 = paintSecondlinesLine[1] - 5;
            paintSecondlinesYStringPos2 = paintSecondlinesLine[3] + 15;
        }

        /* paitnGlobalTime optimization, precalc things */
        {
            paintGlobalTimeYPosition = YOFFSET + YOUTER_SPACEING + (int) (numProcesses * yDistance);
        }
    }

    public VSProcess createProcess(int i) {
        VSProcess process = new VSProcess(prefs, this, logging);
        processes.add(process);
        logging.logg(prefs.getString("lang.process.new") + "; " + process);

        return process;
    }

    private void updateSimulation(final long globalTime, final long lastGlobalTime) {
        final long offset = globalTime - lastGlobalTime;
        for (long l = 0; l < offset; ++l)
            taskManager.runVSTasks(l, offset, lastGlobalTime);

        for (VSProcess process : processes)
            process.syncTime(globalTime);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final long globalTime = time;
        final Graphics2D g2d = (Graphics2D) g;

        globalTimeXPosition = getTimeXPosition(globalTime);

        paintSecondlines(g2d);
        paintProcesses(g2d, globalTime);
        paintGlobalTime(g2d, globalTime);

        synchronized (messageLines) {
            for (VSMessageLine line : messageLines)
                line.draw(g2d, globalTime);
        }

    }

    private void paintProcesses(Graphics2D g, long globalTime) {
        /* First paint the horizontal process timelines
         * Second paint the processes
         */
        final int yOffset = YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING;
        final int xPoints[] = { XOFFSET, xoffset_plus_xpaintsize, xoffset_plus_xpaintsize, XOFFSET, XOFFSET };
        final int yPoints[] = { yOffset, yOffset, yOffset + LINE_WIDTH, yOffset + LINE_WIDTH, yOffset };

        for (VSProcess process : processes) {
            final long localTime = process.getTime();

            g.setColor(process.getColor());
            g.fillPolygon(xPoints, yPoints, 5);

            if (process.hasCrashed()) {
                g.setColor(process.getCrashedColor());
                final Long crashHistory[] = process.getCrashHistoryArray();
                final int length = crashHistory.length;

                for (int i = 0; i < length; i += 2) {
                    final int crashStartPos = (int) getTimeXPosition(crashHistory[i].longValue());
                    int crashEndPos;

                    if (i == length - 1)
                        crashEndPos = xoffset_plus_xpaintsize;
                    else
                        crashEndPos = (int) getTimeXPosition(crashHistory[i+1].longValue());

                    final int xPointsCrashed[] = { crashStartPos, crashEndPos,
                                                   crashEndPos, crashStartPos, crashStartPos
                                                 };
                    g.fillPolygon(xPointsCrashed, yPoints, 5);
                }
            }

            g.setColor(process.getColor());
            g.drawString("P" + process.getProcessID() + ":", XOFFSET - 30, yPoints[0] + LINE_WIDTH);

            final long tmp = localTime > untilTime ? untilTime : localTime;
            final int xPos = 1 + (int) getTimeXPosition(tmp);
            final int yStart = yPoints[0] - 14;
            final int yEnd = yPoints[0];

            g.setColor(processlineColor);
            g.drawLine(xPos, yStart, xPos, yEnd);
            g.drawString(localTime+"ms", xPos + 2, yStart + TEXT_SPACEING);

            if (showLamport)
                paintTime(g, process.getLamportTimeArray(), process, yStart, 25);
            else if (showVectorTime)
                paintTime(g, process.getVectorTimeArray(), process, yStart, 20 * numProcesses);

            for (int i = 0; i < 5; ++i)
                yPoints[i] += paintProcessesOffset;
        }
    }

    private void paintTime(final Graphics2D g, final VSTime times[], final VSProcess process,
                           final int yStart, final int distance) {

        final int lastPos[] = { -1, -1, -1, -1 };

        for (VSTime time : times) {
            int xPos = (int) getTimeXPosition(time.getGlobalTime());
            int bestRow[] = { -1, -1 };

            for (int i = 0; i < 4; ++i) {
                if (lastPos[i] != -1) {
                    int diff = xPos - lastPos[i];
                    if (diff > distance) {
                        bestRow[0] = i;
                        bestRow[1] = -1;
                        break;
                    } else if (bestRow[0] == -1) {
                        bestRow[0] = i;
                        bestRow[1] = diff;
                    } else if (diff > bestRow[1]) {
                        bestRow[0] = i;
                        bestRow[1] = diff;
                    }
                } else {
                    bestRow[0] = i;
                    bestRow[1] = -1;
                    break;
                }
            }

            final int row = bestRow[0];
            if (bestRow[1] != -1)
                xPos += distance - bestRow[1];

            g.drawString(time.toString(), xPos + 2, yStart + 3 * TEXT_SPACEING + row * ROW_HEIGHT);
            lastPos[row] = xPos;
        }
    }

    private void paintSecondlines(Graphics2D g) {
        g.setColor(processSecondlineColor);

        int i;
        for (i = 0; i <= paintSecondlinesSeconds; i += secondsSpaceing) {
            paintSecondlinesLine[0] = paintSecondlinesLine[2] = (int) getTimeXPosition(i*1000);
            g.drawLine(paintSecondlinesLine[0], paintSecondlinesLine[1], paintSecondlinesLine[2], paintSecondlinesLine[3]);

            final int xStringPos = paintSecondlinesLine[0] - 5;
            g.drawString(i+"s", xStringPos, paintSecondlinesYStringPos1);
            if (!showVectorTime && !showLamport)
                g.drawString(i+"s", xStringPos, paintSecondlinesYStringPos2);
        }

        if (i > paintSecondlinesSeconds) {
            paintSecondlinesLine[0] = paintSecondlinesLine[2] = (int) getTimeXPosition(untilTime);
            g.drawLine(paintSecondlinesLine[0], paintSecondlinesLine[1], paintSecondlinesLine[2], paintSecondlinesLine[3]);
        }
    }

    private void paintGlobalTime(Graphics2D g, long globalTime) {
        g.setColor(processSeplineColor);
        final int xOffset = (int) globalTimeXPosition;

        final int xPoints[] = { xOffset, xOffset + SEPLINE_WIDTH, xOffset + SEPLINE_WIDTH, xOffset, xOffset };
        final int yOffset = YOFFSET - 8;
        final int yPoints[] = { yOffset, yOffset, paintGlobalTimeYPosition, paintGlobalTimeYPosition, yOffset };

        g.fillPolygon(xPoints, yPoints, 5);
        g.drawString(globalTime+"ms", xPoints[1]+1, yPoints[0]+TEXT_SPACEING);
    }

    private VSProcess getProcessAtYPos(int yPos) {
        final int reachDistance = (int) (yDistance/3);
        int y = YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING;

        int yOffset = numProcesses > 1
                      ?  (int) ((paintSize-2*(YOFFSET+YOUTER_SPACEING+YSEPLINE_SPACEING))/(numProcesses-1))
                      :  (int) ((paintSize-2*(YOFFSET+YOUTER_SPACEING+YSEPLINE_SPACEING)));

        for (int i = 0; i < numProcesses; ++i) {
            if (yPos < y + reachDistance && yPos > y - reachDistance - LINE_WIDTH)
                return processes.get(i);
            y += yOffset;
        }

        return null;
    }

    private double getTimeXPosition(long time) {
        return XOFFSET + xpaintsize_dividedby_untiltime * time;
    }

    private int getProcessYPosition(int i) {
        int y;

        if (numProcesses > 1)
            y = (int) ((paintSize -
                        2 * (YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING))/ (numProcesses-1));
        else
            y = (int) ((paintSize -
                        2 * (YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING)));

        return y * (i - 1) + YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING;
    }

    public long getTime() {
        return time;
    }

    public long getUntilTime() {
        return untilTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public VSTaskManager getTaskManager() {
        return taskManager;
    }

    public int getNumProcesses() {
        return numProcesses;
    }

    public void run() {
        play();

        while (true) {
            while (!isPaused && !isFinalized) {
                try {
                    Thread.sleep(threadSleep);
                } catch (Exception e) {
                    System.out.println(e);
                }

                updateSimulation(time, lastTime);
                repaint();

                lastTime = time;
                time = System.currentTimeMillis() - startTime;

                if (time > untilTime)
                    time = untilTime;

                if (time == untilTime) {
                    finish();
                    break;
                }
            }

            if (isPaused) {
                for (VSProcess p : processes)
                    p.pause();

                pauseTime = System.currentTimeMillis();

                logging.logg(prefs.getString("lang.simulation.paused"));
                repaint();
            }

            updateSimulation(time, lastTime);
            repaint();

            while (!isFinalized && (isPaused || isFinished || isResetted)) {
                try {
                    Thread.sleep(100);
                    repaint();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            if (isFinalized)
                break; /* Exit the thread */
        }
    }

    public void play() {
        logging.logg(prefs.getString("lang.simulation.started"));
        final long currentTime = System.currentTimeMillis();

        for (VSProcess p : processes)
            p.play();

        if (isResetted)
            isResetted = false;

        else if (isFinished)
            isFinished = false;

        if (isPaused) {
            isPaused = false;
            startTime += currentTime - pauseTime;
            time = currentTime - startTime;

        } else {
            startTime = currentTime;
            time = 0;
        }

        repaint();
    }

    public void finish() {
        for (VSProcess p : processes)
            p.finish();

        simulation.finish();
        isFinished = true;

        logging.logg(prefs.getString("lang.simulation.finished"));
        repaint();
    }

    public void pause() {
        isPaused = true;
    }

    public void reset() {
        if (!isResetted) {
            logging.logg(prefs.getString("lang.simulation.resetted"));

            isResetted = true;
            isPaused = false;
            isFinished = false;
            startTime = System.currentTimeMillis();
            time = 0;
            lastTime = 0;

            taskManager.reset();

            for (VSProcess p : processes)
                p.reset();

            synchronized (messageLines) {
                messageLines.clear();
            }
            repaint();
            logging.clear();
        }
    }

    public void finalize() {
        isFinalized = true;
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public void showLamport(boolean showLamport) {
        this.showLamport = showLamport;
        repaint();
    }

    public void showVectorTime(boolean showVectorTime) {
        this.showVectorTime = showVectorTime;
        repaint();
    }

    public void sendMessage(VSMessage message) {
        VSTask task = null;
        VSProcess sendingProcess = message.getSendingProcess();
        long deliverTime, outageTime, durationTime;
        boolean recvOwn = prefs.getBoolean("sim.message.own.recv");

        for (VSProcess receiverProcess : processes) {
            if (receiverProcess.equals(sendingProcess)) {
                if (recvOwn) {
                    deliverTime = sendingProcess.getGlobalTime();
                    task = new VSTask(deliverTime, receiverProcess, message);
                    taskManager.addTask(task);
                }

            } else {
                durationTime = sendingProcess.getDurationTime();
                deliverTime = sendingProcess.getGlobalTime() + durationTime;
                outageTime = sendingProcess.getARandomMessageOutageTime(durationTime);

                /* Only add a 'receiving message' task if the message will not get lost! */
                if (outageTime == -1) {
                    task = new VSTask(deliverTime, receiverProcess, message);
                    taskManager.addTask(task);
                }

                synchronized (messageLines) {
                    messageLines.add(
                        new VSMessageLine(receiverProcess, sendingProcess.getGlobalTime(),
                                          deliverTime, outageTime, sendingProcess.getProcessID(),
                                          receiverProcess.getProcessID()));
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        final VSProcess process = getProcessAtYPos(e.getY());

        if (process == null)
            return;

        if (SwingUtilities.isRightMouseButton(e)) {
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String actionCommand = ae.getActionCommand();
                    if (actionCommand.equals(prefs.getString("lang.edit"))) {
                        editProcess(process);

                    } else if (actionCommand.equals(prefs.getString("lang.crash"))) {
                        VSProcessEvent event = new ProcessCrashEvent();
                        event.init(process);
                        taskManager.addTask(new VSTask(process.getGlobalTime(), process, event));

                    } else if (actionCommand.equals(prefs.getString("lang.recover"))) {
                        VSProcessEvent event = new ProcessRecoverEvent();
                        event.init(process);
                        taskManager.addTask(new VSTask(process.getGlobalTime(), process, event));
                    }
                }
            };

            JPopupMenu popup = new JPopupMenu();
            JMenuItem item = new JMenuItem(prefs.getString("lang.edit"));
            item.addActionListener(actionListener);
            popup.add(item);

            item = new JMenuItem(prefs.getString("lang.crash"));
            if (process.isCrashed() || isPaused || time == 0 || isFinished)
                item.setEnabled(false);
            else
                item.addActionListener(actionListener);
            popup.add(item);

            item = new JMenuItem(prefs.getString("lang.recover"));
            if (!process.isCrashed() || isPaused || time == 0 || isFinished)
                item.setEnabled(false);
            else
                item.addActionListener(actionListener);
            popup.add(item);

            popup.show(e.getComponent(), e.getX(), e.getY());

        } else {
            editProcess(process);
        }
    }

    public void editProcess(int processNum) {
        VSProcess process = processes.get(processNum);
        editProcess(process);
    }

    public void editProcess(VSProcess process) {
        if (process != null) {
            process.updatePrefs();
            new VSProcessEditor(prefs, simulation, process);
        }
    }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) {
        if (highlightedProcess != null) {
            highlightedProcess.highlightOff();
            highlightedProcess = null;
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseDragged(MouseEvent e) { }

    public void mouseMoved(MouseEvent e) {
        VSProcess p = getProcessAtYPos(e.getY());

        if (p == null) {
            if (highlightedProcess != null) {
                highlightedProcess.highlightOff();
                highlightedProcess = null;
                repaint();
            }

            return;
        }

        if (highlightedProcess != null) {
            if (highlightedProcess.getProcessID() != p.getProcessID()) {
                highlightedProcess.highlightOff();
                highlightedProcess = p;
                p.highlightOn();
                repaint();
            }
        } else {
            highlightedProcess = p;
            p.highlightOn();
            repaint();
        }
    }

    public void ancestorMoved(HierarchyEvent e) { }

    public void ancestorResized(HierarchyEvent e) {
        recalcOnWindowChanged();
    }
}
