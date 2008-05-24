package core;

import java.awt.*;
import java.util.*;

import core.time.*;
import events.*;
import events.implementations.*;
import prefs.*;
import protocols.*;
import simulator.*;
import utils.*;

public class VSProcess extends VSPrefs {
    private ArrayList<VSProtocol> protocolsToReset;
    private ArrayList<Long> crashHistory;
    private ArrayList<VSLamportTime> lamportTimeHistory;
    private ArrayList<VSVectorTime> vectorTimeHistory;
    private Color crashedColor;;
    private Color currentColor;
    private Color tmpColor;
    private VSLogging logging;
    private VSPrefs prefs;
    private VSRandom random;
    private VSSimulationCanvas simulationCanvas;
    private VSTask randomCrashTask;
    private VSVectorTime vectorTime;
    private boolean hasCrashed;
    private boolean hasStarted;
    private boolean isCrashed;
    private boolean isHighlighted;
    private boolean isPaused;
    private boolean timeModified;
    private double clockOffset;
    private float clockVariance;
    private int processID;
    private long globalTime;
    private long lamportTime;
    private long localTime;
    private static int processCounter;

    /* This array contains all Integer prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_INTEGER_VALUE_KEYS[] = {
        "process.prob.crash",
        "message.prob.outage",
    };

    /* This array contains all Long prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_LONG_VALUE_KEYS[] = {
        "message.sendingtime.min",
        "message.sendingtime.max",
    };

    /* This array contains all Float prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_FLOAT_VALUE_KEYS[] = {
        "process.clock.variance",
    };

    /* This array contains all Color prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_COLOR_VALUE_KEYS[] = {
        "col.process.default",
        "col.process.running",
        "col.process.stopped",
        "col.process.highlight",
        "col.process.crashed",
    };

    /* This array contains all String prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_STRING_VALUE_KEYS[] = {
    };

    public VSProcess(VSPrefs prefs, VSSimulationCanvas simulationCanvas, VSLogging logging) {
        this.protocolsToReset = new ArrayList<VSProtocol>();
        this.prefs = prefs;
        this.simulationCanvas = simulationCanvas;
        this.logging = logging;
        random = new VSRandom(processID+processCounter);

        initTimeFormats();

        isPaused = true;
        processID = ++processCounter;

        /* Create the super.VSPrefs with it's default prefs */
        fillWithDefaults();

        /* Make local copys in order to have more performance */
        clockVariance = getFloat("process.clock.variance");
        currentColor = getColor("col.process.default");

        /* Make additional process settings editable through GUI */
        initInteger("process.id", processID,
                    prefs.getString("lang.process.id"), 1, processID + 10);
        setLongIfUnset("process.localtime", localTime, prefs.getString("lang.process.time.local"));

        crashedColor = getColor("col.process.crashed");
        createRandomCrashTask();
    }

    private void initTimeFormats() {
        lamportTime = 0;
        lamportTimeHistory = new ArrayList<VSLamportTime>();

        vectorTime = new VSVectorTime(0);
        vectorTimeHistory = new ArrayList<VSVectorTime>();
        crashHistory = new ArrayList<Long>();

        final int numProcesses = simulationCanvas.getNumProcesses();
        for (int i = 0; i < numProcesses; ++i)
            vectorTime.add(new Long(0));
    }

    private void resetTimeFormats() {
        lamportTime = 0;
        lamportTimeHistory.clear();

        vectorTime = new VSVectorTime(0);
        vectorTimeHistory.clear();
        crashHistory.clear();

        final int numProcesses = simulationCanvas.getNumProcesses();
        for (int i = numProcesses; i > 0; --i)
            vectorTime.add(new Long(0));
    }


    /**
     * Called from the VSProcessEditor, after finishing editing!
     */
    public synchronized void updateFromVSPrefs() {
        setClockVariance(getFloat("process.clock.variance"));
        setProcessID(getInteger("process.id"));
        setLocalTime(getLong("process.localtime"));
        crashedColor = getColor("col.process.crashed");
        //simulationCanvas.repaint();
        createRandomCrashTask();
    }

    /**
     * Called from the VSProcessEditor, before starting editing!
     */
    public synchronized void updatePrefs() {
        setFloat("process.clock.variance", getClockVariance());
        setInteger("process.id", getProcessID());
        setLong("process.localtime", getTime());
    }

    public synchronized void syncTime(final long globalTime) {
        final long currentGlobalTimestep = globalTime - this.globalTime;
        this.globalTime = globalTime;

        localTime += currentGlobalTimestep;
        clockOffset += currentGlobalTimestep * (double) clockVariance;

        while (clockOffset >= 1) {
            clockOffset -= 1;
            ++localTime;
        }

        while (clockOffset <= -1) {
            clockOffset += 1;
            --localTime;
        }

        if (localTime < 0)
            localTime = 0;
    }

    private void setCurrentColor(Color newColor) {
        if (isHighlighted)
            tmpColor = newColor;
        else
            currentColor = newColor;
    }

    public synchronized void highlightOn() {
        tmpColor = currentColor;
        currentColor = getColor("col.process.highlight");
        isHighlighted = true;
    }

    public synchronized void highlightOff() {
        currentColor = tmpColor;
        isHighlighted = false;
    }

    public synchronized void reset() {
        isPaused = true;
        isCrashed = false;
        hasCrashed = false;
        localTime = 0;
        globalTime = 0;
        clockOffset = 0;

        for (VSProtocol protocol : protocolsToReset)
            protocol.reset();

        setCurrentColor(getColor("col.process.default"));
        resetTimeFormats();
    }

    public void createRandomCrashTask() {
        if (!isCrashed) {
            VSTaskManager taskManager = simulationCanvas.getTaskManager();
            long crashTime = getARandomCrashTime();

            if (crashTime < 0)
                return;

            if (randomCrashTask != null)
                taskManager.removeTask(randomCrashTask);


            if (crashTime >= getGlobalTime())  {
                VSEvent event = new ProcessCrashEvent();
                //event.init(this);
                randomCrashTask = new VSTask(crashTime, this, event, VSTask.GLOBAL);
                taskManager.addTask(randomCrashTask);

            } else {
                randomCrashTask = null;
            }
        }
    }

    public synchronized void addClockOffset(long add) {
        this.clockOffset += add;
    }

    public synchronized void play() {
        isPaused = false;
        setCurrentColor(getColor("col.process.running"));
    }

    public synchronized void pause() {
        isPaused = true;
        setCurrentColor(getColor("col.process.stopped"));
    }

    public synchronized void finish() {
        isPaused = true;
        setCurrentColor(getColor("col.process.default"));
    }

    public synchronized int getProcessID() {
        return processID;
    }

    public synchronized void setProcessID(int processID) {
        this.processID = processID;
    }

    public synchronized Color getColor() {
        return currentColor;
    }

    public synchronized void setLocalTime(final long localTime) {
        if (localTime >= 0)
            this.localTime = localTime;
        else
            this.localTime = 0;
    }

    public synchronized long getTime() {
        return localTime;
    }

    public synchronized void setTime(final long time) {
        if (time >= 0)
            this.localTime = time;
        else
            this.localTime = 0;

        this.timeModified = true;
    }

    public synchronized boolean isCrashed() {
        return isCrashed;
    }

    public synchronized void isCrashed(boolean isCrashed) {
        this.isCrashed = isCrashed;
        crashHistory.add(new Long(globalTime));
        if (!hasCrashed)
            hasCrashed = true;
    }

    public synchronized boolean hasCrashed() {
        return hasCrashed;
    }

    public synchronized Color getCrashedColor() {
        return crashedColor;
    }

    public synchronized boolean timeModified() {
        return timeModified;
    }

    public synchronized void timeModified(boolean timeModified) {
        this.timeModified = timeModified;
    }

    public synchronized long getGlobalTime() {
        return globalTime;
    }

    public synchronized void setGlobalTime(final long globalTime) {
        this.globalTime = globalTime >= 0 ? globalTime : 0;
    }

    public synchronized float getClockVariance() {
        return clockVariance;
    }

    public synchronized void setClockVariance(float clockVariance) {
        /* If negative, only allow < 1 prefs */
        if (clockVariance < 0) {
            int part = (int) -clockVariance;
            if (part > 0) {
                this.clockVariance = 0;
                return;
            }
        }

        this.clockVariance = clockVariance;
    }

    public synchronized long getDurationTime() {
        final long maxDurationTime = getLong("message.sendingtime.max");
        final long minDurationTime = getLong("message.sendingtime.min");

        if (maxDurationTime <= minDurationTime)
            return minDurationTime;

        final int diff = (int) (maxDurationTime - minDurationTime);

        /* Integer overflow */
        if (diff <= 0)
            return minDurationTime;

        return minDurationTime + random.nextInt(diff+1);
    }

    public synchronized long getARandomMessageOutageTime(final long durationTime) {
        /* Check if the message will have an outage or not */
        if (random.nextInt(100) < getInteger("message.prob.outage")) {
            /* Calculate the random outage time! */
            final long outageTime = globalTime + random.nextLong(durationTime+1) % simulationCanvas.getUntilTime();
            return outageTime;
        }

        /* No outage */
        return -1;
    }

    private long getARandomCrashTime() {
        /* Check if the process will crash or not */
        if (random.nextInt(100) <= getInteger("process.prob.crash")) {
            /* Calculate the random crash time! */
            final long crashTime =  random.nextLong(simulationCanvas.getUntilTime()+1) % simulationCanvas.getUntilTime();
            return crashTime;
        }

        /* No crash */
        return -1;
    }

    public synchronized VSTask getCrashTask() {
        return randomCrashTask;
    }

    public synchronized boolean isPaused() {
        return isPaused;
    }

    public void increaseLamportTime() {
        setLamportTime(getLamportTime()+1);
    }

    public void updateLamportTime(long time) {
        final long lamportTime = getLamportTime() + 1;

        if (time > lamportTime)
            setLamportTime(time);
        else
            setLamportTime(lamportTime);
    }

    public synchronized long getLamportTime() {
        return lamportTime;
    }

    public synchronized void setLamportTime(long lamportTime) {
        this.lamportTime = lamportTime;
        lamportTimeHistory.add(new VSLamportTime(globalTime, lamportTime));
    }

    public synchronized VSTime[] getLamportTimeArray() {
        final int size = lamportTimeHistory.size();
        final VSTime[] arr = new VSLamportTime[size];

        for (int i = 0; i < size; ++i)
            arr[i] = (VSTime) lamportTimeHistory.get(i);

        return arr;
    }

    public synchronized void increaseVectorTime() {
        vectorTime.set(processID-1, new Long(vectorTime.get(processID-1).longValue()+1));
        vectorTime.setGlobalTime(globalTime);
        vectorTimeHistory.add(vectorTime.getCopy());
    }

    public synchronized void updateVectorTime(VSVectorTime vectorTimeUpdate) {
        final int size = vectorTime.size();

        for (int i = 0; i < size; ++i) {
            if (i == processID-1)
                vectorTime.set(i, new Long(vectorTime.get(i).longValue()+1));
            else if (vectorTimeUpdate.get(i) > vectorTime.get(i))
                vectorTime.set(i, vectorTimeUpdate.get(i));
        }

        vectorTime.setGlobalTime(globalTime);
        vectorTimeHistory.add(vectorTime.getCopy());
    }

    public synchronized VSVectorTime getVectorTime() {
        return vectorTime;
    }

    public synchronized VSTime[] getVectorTimeArray() {
        final int size = vectorTimeHistory.size();
        final VSTime[] arr = new VSTime[size];

        for (int i = 0; i < size; ++i)
            arr[i] = (VSTime) vectorTimeHistory.get(i);

        return arr;
    }

    public synchronized Long[] getCrashHistoryArray() {
        final int size = crashHistory.size();
        final Long[] arr = new Long[size];

        for (int i = 0; i < size; ++i)
            arr[i] = crashHistory.get(i);

        return arr;
    }

    public void sendMessage(VSMessage message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.message.sent"));
        buffer.append("; ");
        buffer.append(message.toStringFull());
        logg(buffer.toString());
        simulationCanvas.sendMessage(message);
    }

    public void logg(String message) {
        logging.logg(toString() + "; " + message, globalTime);
    }

    public void fillWithDefaults() {
        prefs.copyIntegers(this, DEFAULT_INTEGER_VALUE_KEYS);
        prefs.copyLongs(this, DEFAULT_LONG_VALUE_KEYS);
        prefs.copyFloats(this, DEFAULT_FLOAT_VALUE_KEYS);
        prefs.copyColors(this, DEFAULT_COLOR_VALUE_KEYS);
        prefs.copyStrings(this, DEFAULT_STRING_VALUE_KEYS);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.process.id"));
        buffer.append(": ");
        buffer.append(getProcessID());
        buffer.append("; ");
        buffer.append(prefs.getString("lang.process.time.local"));
        buffer.append(": ");
        buffer.append(VSTools.getTimeString(getTime()));
        buffer.append("; ");
        buffer.append(prefs.getString("lang.time.lamport"));
        buffer.append(": ");
        buffer.append(lamportTime);
        buffer.append("; ");
        buffer.append(prefs.getString("lang.time.vector"));
        buffer.append(": ");
        buffer.append(vectorTime);
        return buffer.toString();
    }

    public String toStringFull() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(toString());
        buffer.append("; paused: ");
        buffer.append(isPaused);
        buffer.append("; crashed: ");
        buffer.append(isCrashed);
        buffer.append("; crashTask: ");
        buffer.append(randomCrashTask);
        return buffer.toString();
    }

    public boolean equals(VSProcess process) {
        return process.getProcessID() == processID;
    }

    public VSSimulationCanvas getSimulationCanvas() {
        return simulationCanvas;
    }

    public VSPrefs getPrefs() {
        return prefs;
    }

    public static void resetProcessCounter() {
        processCounter = 0;
    }

    public VSProtocol getProtocolObject(String protocolClassname) {
        VSProtocol protocol = null;

        if (!objectExists(protocolClassname)) {
            protocol = (VSProtocol) VSRegisteredEvents.createEventInstanceByClassname(protocolClassname, this);
            setObject(protocolClassname, protocol);
            protocolsToReset.add(protocol);

        } else {
            protocol = (VSProtocol) getObject(protocolClassname);
        }

        return protocol;
    }
}
