/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package simulator;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

import core.*;
import core.time.*;
import events.*;
import events.implementations.*;
import events.internal.*;
import prefs.*;
import prefs.editors.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VSSimulatorCanvas.
 */
public class VSSimulatorCanvas extends Canvas implements Runnable, MouseMotionListener, MouseListener, HierarchyBoundsListener  {
    private static final long serialVersionUID = 1L;

    /** The highlighted process. */
    private VSProcess highlightedProcess;

    /** The simulation. */
    private VSSimulator simulation;

    /** The prefs. */
    private VSPrefs prefs;

    /** The logging. */
    private VSLogging logging;

    /** The num processes. */
    private volatile int numProcesses;

    /** The seconds spaceing. */
    private int secondsSpaceing;

    /** The thread sleep. */
    private int threadSleep;

    /** The until time. */
    private long untilTime;

    /** The is paused. */
    private volatile boolean isPaused = true;

    /** The is thread stopped. */
    private volatile boolean isThreadStopped = false;

    /** The is finished. */
    private volatile boolean isFinished = false;

    /** The is resetted. */
    private volatile boolean isResetted = false;

    /** The is anti aliased. */
    private volatile boolean isAntiAliased = false;

    /** The is anti aliased changed. */
    private volatile boolean isAntiAliasedChanged = false;

    /** The show lamport. */
    private volatile boolean showLamport = false;

    /** The show vector time. */
    private volatile boolean showVectorTime = false;

    /** The pause time. */
    private volatile long pauseTime;

    /** The start time. */
    private volatile long startTime;

    /** The time. */
    private volatile long time;

    /** The last time. */
    private volatile long lastTime;

    /** The task manager. */
    private VSTaskManager taskManager;

    /** The message lines. */
    private LinkedList<VSMessageLine> messageLines;

    /** The processes. */
    private Vector<VSProcess> processes;

    /** The clock speed. */
    private double clockSpeed;

    /** The clock offset. */
    private double clockOffset;

    /** The simulation time. */
    private long simulationTime;

    /* GFX buffering */
    /** The strategy. */
    private BufferStrategy strategy;

    /** The g. */
    private Graphics2D g;

    /* Static constats */
    /** The Constant LINE_WIDTH. */
    private static final int LINE_WIDTH = 5;

    /** The Constant SEPLINE_WIDTH. */
    private static final int SEPLINE_WIDTH = 2;

    /** The Constant XOFFSET. */
    private static final int XOFFSET = 50;

    /** The Constant YOFFSET. */
    private static final int YOFFSET = 30;

    /** The Constant YOUTER_SPACEING. */
    private static final int YOUTER_SPACEING = 15;

    /** The Constant YSEPLINE_SPACEING. */
    private static final int YSEPLINE_SPACEING = 20;

    /** The Constant TEXT_SPACEING. */
    private static final int TEXT_SPACEING = 10;

    /** The Constant ROW_HEIGHT. */
    private static final int ROW_HEIGHT = 14;

    /* Constats, which have to get calculated once after start */
    /** The processline color. */
    private Color processlineColor;

    /** The process secondline color. */
    private Color processSecondlineColor;

    /** The process sepline color. */
    private Color processSeplineColor;

    /** The message arrived color. */
    private Color messageArrivedColor;

    /** The message sending color. */
    private Color messageSendingColor;

    /** The message lost color. */
    private Color messageLostColor;

    /** The background color. */
    private Color backgroundColor;

    /** The message line counter. */
    private long messageLineCounter;

    /**
     * The Class VSMessageLine.
     */
    private class VSMessageLine {

        /** The receiver process. */
        private VSProcess receiverProcess;

        /** The color. */
        private Color color;

        /** The send time. */
        private long sendTime;

        /** The recv time. */
        private long recvTime;

        /** The sender num. */
        private int senderNum;

        /** The receiver num. */
        private int receiverNum;

        /** The offset1. */
        private int offset1;

        /** The offset2. */
        private int offset2;

        /** The is arrived. */
        private boolean isArrived;

        /** The is lost. */
        private boolean isLost;

        /** The x1. */
        private double x1;

        /** The y1. */
        private double y1;

        /** The x2. */
        private double x2;

        /** The y2. */
        private double y2;

        /** The x. */
        private double x;

        /** The y. */
        private double y;

        /** The outage time. */
        private long outageTime;

        /** The z. */
        private long z;

        /** The message line num. */
        private long messageLineNum;

        /** The task. */
        private VSTask task;

        /**
         * Instantiates a new lang.process.removemessage line.
         *
         * @param receiverProcess the receiver process
         * @param sendTime the send time
         * @param recvTime the recv time
         * @param outageTime the outage time
         * @param senderNum the sender num
         * @param receiverNum the receiver num
         * @param task the task
         */
        public VSMessageLine(VSProcess receiverProcess, long sendTime, long recvTime, long outageTime, int senderNum , int receiverNum, VSTask task) {
            this.receiverProcess = receiverProcess;
            this.sendTime = sendTime;
            this.recvTime = recvTime;
            this.outageTime = outageTime;
            this.senderNum = senderNum;
            this.receiverNum = receiverNum;
            this.isArrived = false;
            this.isLost = false;
            this.messageLineNum = ++messageLineCounter;
            this.task = task;

            if (senderNum > receiverNum) {
                //offset1 = 1;
                offset2 = LINE_WIDTH;
            } else {
                offset1 = LINE_WIDTH - 1;
                //offset2 = 1;
            }

            /* Needed if the message gets lost after 0ms */
            this.x = getTimeXPosition(sendTime);
            this.y = getProcessYPosition(senderNum+1) + offset1;

            recalcOnChange();
            paint();
        }

        /**
         * Recalc on change.
         */
        public void recalcOnChange() {
            x1 = getTimeXPosition(sendTime);
            y1 = getProcessYPosition(senderNum+1) + offset1;
            x2 = getTimeXPosition(recvTime);
            y2 = getProcessYPosition(receiverNum+1) + offset2;

            if (isLost) {
                x = getTimeXPosition(z);
                y = y1 + ( ( (y2-y1) / (x2-x1)) * (x-x1));
            }

        }

        /**
         * Draw.
         *
         * @param g the g
         * @param globalTime the global time
         */
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

        /**
         * Removes the process at index.
         *
         * @param index the index
         *
         * @return true, if successful
         */
        public boolean removeProcessAtIndex(int index) {
            if (index == receiverNum || index == senderNum)
                return true;

            if (index < receiverNum)
                --receiverNum;

            if (index < senderNum)
                --senderNum;

            recalcOnChange();

            return false;
        }

        /**
         * Gets the message line num.
         *
         * @return the message line num
         */
        public long getMessageLineNum() {
            return messageLineNum;
        }

        /**
         * Equals.
         *
         * @param line the line
         *
         * @return true, if successful
         */
        public boolean equals(VSMessageLine line) {
            return messageLineNum == line.getMessageLineNum();
        }

        /**
         * Gets the task.
         *
         * @return the task
         */
        public VSTask getTask() {
            return task;
        }
    }

    /**
     * Instantiates a new lang.process.removesimulator canvas.
     *
     * @param prefs the prefs
     * @param simulation the simulation
     * @param logging the logging
     */
    public VSSimulatorCanvas(VSPrefs prefs, VSSimulator simulation, VSLogging logging) {
        this.prefs = prefs;
        this.simulation = simulation;
        this.logging = logging;
        this.taskManager = new VSTaskManager(prefs);
        this.messageLines = new LinkedList<VSMessageLine>();
        this.processes = new Vector<VSProcess>();

        numProcesses = prefs.getInteger("sim.process.num");
        updateFromPrefs();

        VSProcess.resetProcessCounter();
        for (int i = 0; i < numProcesses; ++i)
            processes.add(createProcess(i));

        addMouseListener(this);
        addMouseMotionListener(this);
        addHierarchyBoundsListener(this);
    }

    /** The x paint size. */
    double xPaintSize;

    /** The paint size. */
    double paintSize;

    /** The y distance. */
    double yDistance;

    /** The global time x position. */
    double globalTimeXPosition;

    /** The xoffset_plus_xpaintsize. */
    int xoffset_plus_xpaintsize;

    /** The xpaintsize_dividedby_untiltime. */
    double xpaintsize_dividedby_untiltime;

    /** The paint processes offset. */
    int paintProcessesOffset;

    /** The paint secondlines seconds. */
    int paintSecondlinesSeconds;

    /** The paint secondlines line. */
    int paintSecondlinesLine[] = new int[4];

    /** The paint secondlines y string pos1. */
    int paintSecondlinesYStringPos1;

    /** The paint secondlines y string pos2. */
    int paintSecondlinesYStringPos2;

    /** The paint global time y position. */
    int paintGlobalTimeYPosition;

    /* This method contains very ugly code. But this has to be in order to gain performance! */
    /**
     * Recalc on change.
     */
    private void recalcOnChange() {
        processlineColor = prefs.getColor("col.process.line");
        processSecondlineColor = prefs.getColor("col.process.secondline");
        processSeplineColor = prefs.getColor("col.process.sepline");
        messageArrivedColor = prefs.getColor("col.message.arrived");
        messageSendingColor = prefs.getColor("col.message.sending");
        messageLostColor = prefs.getColor("col.message.lost");
        backgroundColor = prefs.getColor("col.background");

        paintSize = simulation.getPaintSize();
        xPaintSize = simulation.getWidth() - (3 * XOFFSET + simulation.getSplitSize());
        yDistance = (simulation.getPaintSize() - 2 * (YOFFSET + YOUTER_SPACEING))/ numProcesses;
        xpaintsize_dividedby_untiltime = xPaintSize / (double) untilTime;

        for (VSMessageLine messageLine : messageLines)
            messageLine.recalcOnChange();

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

        if (strategy != null) {
            synchronized (strategy) {
                g = (Graphics2D) strategy.getDrawGraphics();
                g.setColor(backgroundColor);
                if (isAntiAliased)
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        }
    }

    /**
     * Update simulation.
     *
     * @param globalTime the global time
     * @param lastGlobalTime the last global time
     */
    private void updateSimulation(final long globalTime, final long lastGlobalTime) {
        if (isPaused || isFinished)
            return;

        final long lastSimulationTime = simulationTime;
        long offset = globalTime - lastGlobalTime;

        clockOffset += offset * clockSpeed;

        while (clockOffset >= 1) {
            --clockOffset;
            ++simulationTime;
        }

        if (simulationTime > untilTime)
            simulationTime = untilTime;

        offset = simulationTime - lastSimulationTime;

        for (long l = 0; l < offset; ++l)
            taskManager.runTasks(l, offset, lastSimulationTime);

        synchronized (processes) {
            for (VSProcess process : processes)
                process.syncTime(simulationTime);
        }
    }

    /**
     * Paint.
     */
    public void paint() {
        while (getBufferStrategy() == null) {
            createBufferStrategy(3);
            strategy = getBufferStrategy();

            if (strategy != null) {
                g = (Graphics2D) strategy.getDrawGraphics();
                g.setColor(backgroundColor);
            }
        }

        synchronized (strategy) {
            if (isAntiAliasedChanged) {
                if (isAntiAliased)
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                else
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                isAntiAliasedChanged = false;
            }

            g.fillRect(0, 0, getWidth(), getHeight());
            final long globalTime = simulationTime;

            globalTimeXPosition = getTimeXPosition(globalTime);
            paintSecondlines(g);
            paintProcesses(g, globalTime);
            paintGlobalTime(g, globalTime);

            synchronized (messageLines) {
                for (VSMessageLine line : messageLines)
                    line.draw(g, globalTime);
            }

            g.setColor(backgroundColor);

            strategy.show();
        }
    }

    /**
     * Paint processes.
     *
     * @param g the g
     * @param globalTime the global time
     */
    private void paintProcesses(Graphics2D g, long globalTime) {
        /* First paint the horizontal process timelines
         * Second paint the processes
         */
        final int yOffset = YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING;
        final int xPoints[] = { XOFFSET, xoffset_plus_xpaintsize, xoffset_plus_xpaintsize, XOFFSET, XOFFSET };
        final int yPoints[] = { yOffset, yOffset, yOffset + LINE_WIDTH, yOffset + LINE_WIDTH, yOffset };

        synchronized (processes) {
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
    }

    /**
     * Paint time.
     *
     * @param g the g
     * @param times the times
     * @param process the process
     * @param yStart the y start
     * @param distance the distance
     */
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

    /**
     * Paint secondlines.
     *
     * @param g the g
     */
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

    /**
     * Paint global time.
     *
     * @param g the g
     * @param globalTime the global time
     */
    private void paintGlobalTime(Graphics2D g, long globalTime) {
        g.setColor(processSeplineColor);
        final int xOffset = (int) globalTimeXPosition;

        final int xPoints[] = { xOffset, xOffset + SEPLINE_WIDTH, xOffset + SEPLINE_WIDTH, xOffset, xOffset };
        final int yOffset = YOFFSET - 8;
        final int yPoints[] = { yOffset, yOffset, paintGlobalTimeYPosition, paintGlobalTimeYPosition, yOffset };

        g.fillPolygon(xPoints, yPoints, 5);
        g.drawString(globalTime+"ms", xPoints[1]+1, yPoints[0]+TEXT_SPACEING);
    }

    /**
     * Gets the process at y pos.
     *
     * @param yPos the y pos
     *
     * @return the process at y pos
     */
    private VSProcess getProcessAtYPos(int yPos) {
        final int reachDistance = (int) (yDistance/3);
        int y = YOFFSET + YOUTER_SPACEING + YSEPLINE_SPACEING;

        int yOffset = numProcesses > 1
                      ?  (int) ((paintSize-2*(YOFFSET+YOUTER_SPACEING+YSEPLINE_SPACEING))/(numProcesses-1))
                      :  (int) ((paintSize-2*(YOFFSET+YOUTER_SPACEING+YSEPLINE_SPACEING)));

        //System.out.println("JO " + yPos + " " + yDistance + " " + yOffset);
        for (int i = 0; i < numProcesses; ++i) {
            if (yPos < y + reachDistance && yPos > y - reachDistance - LINE_WIDTH)
                return processes.get(i);
            y += yOffset;
        }

        return null;
    }

    /**
     * Gets the time x position.
     *
     * @param time the time
     *
     * @return the time x position
     */
    private double getTimeXPosition(long time) {
        return XOFFSET + xpaintsize_dividedby_untiltime * time;
    }

    /**
     * Gets the process y position.
     *
     * @param i the i
     *
     * @return the process y position
     */
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

    /**
     * Gets the time.
     *
     * @return the time
     */
    public long getTime() {
        return simulationTime;
    }

    /**
     * Gets the until time.
     *
     * @return the until time
     */
    public long getUntilTime() {
        return untilTime;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the task manager.
     *
     * @return the task manager
     */
    public VSTaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * Gets the num processes.
     *
     * @return the num processes
     */
    public int getNumProcesses() {
        return numProcesses;
    }

    /**
     * Gets the process.
     *
     * @param processNum the process num
     *
     * @return the process
     */
    public VSProcess getProcess(int processNum) {
        if (processNum >= processes.size())
            return null;

        return processes.get(processNum);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            while (!isThreadStopped && (isPaused || isFinished || isResetted)) {
                try {
                    Thread.sleep(100);
                    paint();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            if (isThreadStopped)
                break; /* Exit the thread */

            while (!isPaused && !isThreadStopped) {
                try {
                    Thread.sleep(threadSleep);
                } catch (Exception e) {
                    System.out.println(e);
                }

                updateSimulation(time, lastTime);

                if (simulationTime == untilTime) {
                    finish();
                    break;
                }

                paint();
                lastTime = time;
                time = System.currentTimeMillis() - startTime;

            }

            updateSimulation(time, lastTime);
            paint();
        }
    }

    /**
     * Play.
     */
    public void play() {
        logging.logg(prefs.getString("lang.simulation.started"));
        final long currentTime = System.currentTimeMillis();

        synchronized (processes) {
            for (VSProcess p : processes)
                p.play();
        }

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

        paint();
    }

    /**
     * Finish.
     */
    public void finish() {
        synchronized (processes) {
            for (VSProcess p : processes)
                p.finish();
        }

        simulation.finish();
        isFinished = true;

        logging.logg(prefs.getString("lang.simulation.finished"));
        paint();
    }

    /**
     * Pause.
     */
    public void pause() {
        isPaused = true;
        synchronized (processes) {
            for (VSProcess p : processes)
                p.pause();
        }

        pauseTime = System.currentTimeMillis();

        logging.logg(prefs.getString("lang.simulation.paused"));
        paint();
    }

    /**
     * Reset.
     */
    public void reset() {
        if (!isResetted) {
            logging.logg(prefs.getString("lang.simulation.resetted"));

            isResetted = true;
            isPaused = false;
            isFinished = false;
            startTime = System.currentTimeMillis();
            time = 0;
            lastTime = 0;
            clockOffset = 0;
            simulationTime = 0;

            synchronized (processes) {
                for (VSProcess process : processes)
                    process.reset();
            }

            /* Reset the task manager AFTER the processes, for the programmed tasks */
            taskManager.reset();

            synchronized (processes) {
                for (VSProcess process : processes)
                    process.createRandomCrashTask();
            }

            synchronized (messageLines) {
                messageLines.clear();
            }

            paint();
            logging.clear();
        }
    }

    /**
     * Stop thread.
     */
    public void stopThread() {
        isThreadStopped = true;
    }

    /**
     * Checks if is thread stopped.
     *
     * @return true, if is thread stopped
     */
    public boolean isThreadStopped() {
        return isThreadStopped;
    }

    /**
     * Show lamport.
     *
     * @param showLamport the show lamport
     */
    public void showLamport(boolean showLamport) {
        this.showLamport = showLamport;
        if (isPaused)
            paint();
    }

    /**
     * Show vector time.
     *
     * @param showVectorTime the show vector time
     */
    public void showVectorTime(boolean showVectorTime) {
        this.showVectorTime = showVectorTime;
        if (isPaused)
            paint();
    }

    /**
     * Checks if is anti aliased.
     *
     * @param isAntiAliased the is anti aliased
     */
    public void isAntiAliased(boolean isAntiAliased) {
        this.isAntiAliased = isAntiAliased;
        this.isAntiAliasedChanged = true;
        if (isPaused)
            paint();
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(VSMessage message) {
        VSTask task = null;
        VSAbstractEvent messageReceiveEvent = null;
        VSProcess sendingProcess = message.getSendingProcess();
        long deliverTime, outageTime, durationTime;
        boolean recvOwn = prefs.getBoolean("sim.message.own.recv");

        synchronized (processes) {
            for (VSProcess receiverProcess : processes) {
                if (receiverProcess.equals(sendingProcess)) {
                    if (recvOwn) {
                        deliverTime = sendingProcess.getGlobalTime();
                        messageReceiveEvent = new MessageReceiveEvent(message);
                        task = new VSTask(deliverTime, receiverProcess, messageReceiveEvent, VSTask.GLOBAL);
                        taskManager.addTask(task);
                    }

                } else {
                    durationTime = sendingProcess.getDurationTime();
                    deliverTime = sendingProcess.getGlobalTime() + durationTime;

                    if (prefs.getBoolean("sim.message.prob.mean"))
                        outageTime = sendingProcess.getARandomMessageOutageTime(
                                         durationTime, receiverProcess);
                    else
                        outageTime = sendingProcess.getARandomMessageOutageTime(
                                         durationTime, null);

                    /* Only add a 'receiving message' task if the message will not get lost! */
                    if (outageTime == -1) {
                        messageReceiveEvent = new MessageReceiveEvent(message);
                        task = new VSTask(deliverTime, receiverProcess, messageReceiveEvent, VSTask.GLOBAL);
                        taskManager.addTask(task);
                    }

                    synchronized (messageLines) {
                        messageLines.add(
                            new VSMessageLine(receiverProcess, sendingProcess.getGlobalTime(),
                                              deliverTime, outageTime, sendingProcess.getProcessNum(),
                                              receiverProcess.getProcessNum(), task));
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        final VSProcess process = getProcessAtYPos(me.getY());

        if (SwingUtilities.isRightMouseButton(me)) {
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String actionCommand = ae.getActionCommand();
                    if (actionCommand.equals(prefs.getString("lang.process.edit"))) {
                        editProcess(process);

                    } else if (actionCommand.equals(prefs.getString("lang.process.crash"))) {
                        VSAbstractEvent event = new ProcessCrashEvent();
                        taskManager.addTask(new VSTask(process.getGlobalTime(), process, event, VSTask.GLOBAL));

                    } else if (actionCommand.equals(prefs.getString("lang.process.recover"))) {
                        VSAbstractEvent event = new ProcessRecoverEvent();
                        taskManager.addTask(new VSTask(process.getGlobalTime(), process, event, VSTask.GLOBAL));

                    } else if (actionCommand.equals(prefs.getString("lang.process.remove"))) {
                        removeProcess(process);

                    } else if (actionCommand.equals(prefs.getString("lang.process.add.new"))) {
                        addProcess();
                    }
                }
            };


            JPopupMenu popup = new JPopupMenu();
            JMenuItem item = new JMenuItem(prefs.getString("lang.process.edit"));
            if (process == null)
                item.setEnabled(false);
            else
                item.addActionListener(actionListener);
            popup.add(item);

            item = new JMenuItem(prefs.getString("lang.process.crash"));
            if (process == null || process.isCrashed() || isPaused || time == 0 || isFinished)
                item.setEnabled(false);
            else
                item.addActionListener(actionListener);
            popup.add(item);

            item = new JMenuItem(prefs.getString("lang.process.recover"));
            if (process == null || !process.isCrashed() || isPaused || time == 0 || isFinished)
                item.setEnabled(false);
            else
                item.addActionListener(actionListener);
            popup.add(item);

            item = new JMenuItem(prefs.getString("lang.process.remove"));
            if (process == null)
                item.setEnabled(false);
            else
                item.addActionListener(actionListener);
            popup.add(item);

            popup.addSeparator();

            item = new JMenuItem(prefs.getString("lang.process.add.new"));
            item.addActionListener(actionListener);
            popup.add(item);


            popup.show(me.getComponent(), me.getX(), me.getY());

        } else {
            editProcess(process);
        }
    }

    /**
     * Edits the process.
     *
     * @param processNum the process num
     */
    public void editProcess(int processNum) {
        VSProcess process = processes.get(processNum);
        editProcess(process);
    }

    /**
     * Edits the process.
     *
     * @param process the process
     */
    public void editProcess(VSProcess process) {
        if (process != null) {
            process.updatePrefs();
            new VSEditorFrame(prefs, simulation.getSimulatorFrame(),
                              new VSProcessEditor(prefs, process));
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) { }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        if (highlightedProcess != null) {
            highlightedProcess.highlightOff();
            highlightedProcess = null;
            paint();
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
        VSProcess p = getProcessAtYPos(e.getY());

        if (p == null) {
            if (highlightedProcess != null) {
                highlightedProcess.highlightOff();
                highlightedProcess = null;
            }

            if (isPaused)
                paint();

            return;
        }

        if (highlightedProcess != null) {
            if (highlightedProcess.getProcessID() != p.getProcessID()) {
                highlightedProcess.highlightOff();
                highlightedProcess = p;
                p.highlightOn();
            }
        } else {
            highlightedProcess = p;
            p.highlightOn();
        }

        if (isPaused)
            paint();
    }

    /* (non-Javadoc)
     * @see java.awt.event.HierarchyBoundsListener#ancestorMoved(java.awt.event.HierarchyEvent)
     */
    public void ancestorMoved(HierarchyEvent e) { }

    /* (non-Javadoc)
     * @see java.awt.event.HierarchyBoundsListener#ancestorResized(java.awt.event.HierarchyEvent)
     */
    public void ancestorResized(HierarchyEvent e) {
        recalcOnChange();
    }

    /**
     * Gets the processes array.
     *
     * @return the processes array
     */
    public ArrayList<VSProcess> getProcessesArray() {
        ArrayList<VSProcess> arr = new ArrayList<VSProcess>();

        synchronized (processes) {
            for (VSProcess process : processes)
                arr.add(process);
        }

        return arr;
    }

    /**
     * Update from prefs.
     */
    public void updateFromPrefs() {
        untilTime = prefs.getInteger("sim.seconds") * 1000;
        clockSpeed = prefs.getFloat("sim.clock.speed");

        secondsSpaceing = (int) (untilTime / 15000);
        if (secondsSpaceing == 0)
            secondsSpaceing = 1;

        threadSleep = (int) (untilTime / 7500);
        if (threadSleep == 0)
            threadSleep = 1;

        recalcOnChange();
    }

    /**
     * Removes the process.
     *
     * @param process the process
     */
    private void removeProcess(VSProcess process) {
        if (numProcesses == 1) {
            simulation.getSimulatorFrame().removeSimulation(simulation);

        } else {
            int index = processes.indexOf(process);
            processes.remove(index);
            synchronized (processes) {
                for (VSProcess p : processes) {
                    p.removeProcessAtIndex(index);
                }
            }
            numProcesses = processes.size();
            taskManager.removeTasksOf(process);
            simulation.removeProcessAtIndex(index);
            recalcOnChange();

            ArrayList<VSMessageLine> removeThose = new ArrayList<VSMessageLine>();
            synchronized (messageLines) {
                for (VSMessageLine line : messageLines)
                    if (line.removeProcessAtIndex(index))
                        removeThose.add(line);
                for (VSMessageLine line : removeThose) {
                    VSTask deliverTask = line.getTask();
                    if (deliverTask != null)
                        taskManager.removeTask(deliverTask);
                    messageLines.remove(line);
                }
            }
        }
    }

    /**
     * Creates the process.
     *
     * @param processNum the process num
     *
     * @return the lang.process.removeprocess
     */
    private VSProcess createProcess(int processNum) {
        VSProcess process = new VSProcess(prefs, processNum, this, logging);
        logging.logg(prefs.getString("lang.process.new") + "; " + process);
        return process;
    }

    /**
     * Adds the process.
     */
    private void addProcess() {
        numProcesses = processes.size() + 1;
        VSProcess newProcess = createProcess(processes.size());
        processes.add(newProcess);
        synchronized (processes) {
            for (VSProcess process : processes)
                if (!process.equals(newProcess))
                    process.addedAProcess();
        }
        recalcOnChange();
        simulation.addProcessAtIndex(processes.size()-1);
    }
}
