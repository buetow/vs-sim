/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
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

/**
 * The Class VSProcess. A object of this class represents a process of a
 * simulation.
 *
 * @author Paul C. Buetow
 */
public class VSProcess extends VSPrefs {
    /** The data serialization id. */
    private static final long serialVersionUID = 1L;

    /** The protocols to reset if the simulation is over or the reset
     * button has been pressed.
     */
    private ArrayList<VSAbstractProtocol> protocolsToReset;

    /** The crash history. represents all crashes of the process using the
     * global simulation time.
     */
    private ArrayList<Long> crashHistory;

    /** The lamport time history. */
    private ArrayList<VSLamportTime> lamportTimeHistory;

    /** The vector time history. */
    private ArrayList<VSVectorTime> vectorTimeHistory;

    /** The color used if the process has crashed. */
    private Color crashedColor;;

    /** The process' current color. */
    private Color currentColor;

    /** A temp. color. For internal usage. */
    private Color tmpColor;

    /** The logging object. */
    private VSLogging logging;

    /** The simulation's default prefs. */
    private VSPrefs prefs;

    /** The random generator of the process. */
    private VSRandom random;

    /** The simulation canvas. */
    private VSSimulatorCanvas simulationCanvas;

    /** The random crash task. May be null if there is no such random task. */
    private VSTask randomCrashTask;

    /** The vector time. */
    private VSVectorTime vectorTime;

    /** The process has crashed. But may be working again. */
    private boolean hasCrashed;

    /** The process has started. But may be paused or crashed.. */
    private boolean hasStarted;

    /** The process is crashed. */
    private boolean isCrashed;

    /** The process is highlighted. */
    private boolean isHighlighted;

    /** The process is paused. */
    private boolean isPaused;

    /** The time has been modified in a task. Needed by the task manager to
     * calculate correct offsets.
     */
    private boolean timeModified;

    /** The clock offset. Used by the task manager and also by the process'
     * clock variance.
     */
    private double clockOffset;

    /** The clock variance. */
    private float clockVariance;

    /** The process id. */
    private int processID;

    /** The process num. It is different to the process id. It represents the
     * array index of there the process is stored at.
      */
    private int processNum;

    /** The global time. */
    private long globalTime;

    /** The lamport time. */
    private long lamportTime;

    /** The local time. */
    private long localTime;

    /** The process counter. Needed for the unique process id's. */
    private static int processCounter;

    /** The Constant DEFAULT_INTEGER_VALUE_KEYS.
     * This array contains all Integer prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_INTEGER_VALUE_KEYS[] = {
        "process.prob.crash",
        "message.prob.outage",
    };

    /** The Constant DEFAULT_LONG_VALUE_KEYS.
     * This array contains all Long prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_LONG_VALUE_KEYS[] = {
        "message.sendingtime.min",
        "message.sendingtime.max",
    };

    /** The Constant DEFAULT_FLOAT_VALUE_KEYS.
     * This array contains all Float prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_FLOAT_VALUE_KEYS[] = {
        "process.clock.variance",
    };

    /** The Constant DEFAULT_COLOR_VALUE_KEYS.
     * This array contains all Color prefs of the process which should show
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

    /** The Constant DEFAULT_STRING_VALUE_KEYS.
     * This array contains all String prefs of the process which should show
     * up in the prefs menu! All keys which dont start with "sim." only show
     * up in the extended prefs menu!
     */
    private static final String DEFAULT_STRING_VALUE_KEYS[] = {
    };

    /**
     * Instantiates a new process.
     *
     * @param prefs the simulation's default prefs
     * @param processNum the process num
     * @param simulationCanvas the simulation canvas
     * @param logging the logging object
     */
    public VSProcess(VSPrefs prefs, int processNum,
                     VSSimulatorCanvas simulationCanvas, VSLogging logging) {
        this.protocolsToReset = new ArrayList<VSAbstractProtocol>();
        this.processNum = processNum;
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
        crashedColor = getColor("col.process.crashed");

        /* Make additional process settings editable through GUI */
        initLong("process.localtime", localTime,
                 prefs.getString("lang.process.time.local"), "ms");

        createRandomCrashTask();
    }

    /**
     * Inits the time formats. E.g. lamport and vector time stamps.
     */
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

    /**
     * Reset time formats. E.g. lamport and vector time stamps.
     */
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
     * Called from the VSProcessEditor, after finishing editing! This makes
     * sure that the VSProcess object is using the up to date prefs!
     */
    public synchronized void updateFromVSPrefs() {
        setClockVariance(getFloat("process.clock.variance"));
        setLocalTime(getLong("process.localtime"));
        crashedColor = getColor("col.process.crashed");
        createRandomCrashTask();
    }

    /**
     * Called from the VSProcessEditor, before starting editing! This makes
     * sure that the editor edits the up to date prefs of the process!
     */
    public synchronized void updatePrefs() {
        setFloat("process.clock.variance", getClockVariance());
        setLong("process.localtime", getTime());
    }

    /**
     * Syncs the process' time. This method is using the clockOffset and
     * clockVariance variables. This method is called repeatedly from the
     * VSSimulationCanvas in order to update the process' local and global
     * time values.
     *
     * @param globalTime the global time.
     */
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

        /* We do not want a negative time */
        if (localTime < 0)
            localTime = 0;
    }

    /**
     * Sets the current color.
     *
     * @param newColor the new current color
     */
    private void setCurrentColor(Color newColor) {
        if (isHighlighted)
            tmpColor = newColor;
        else
            currentColor = newColor;
    }

    /**
     * Highlights the process.
     */
    public synchronized void highlightOn() {
        tmpColor = currentColor;
        currentColor = getColor("col.process.highlight");
        isHighlighted = true;
    }

    /**
     * Unhighlights the process.
     */
    public synchronized void highlightOff() {
        currentColor = tmpColor;
        isHighlighted = false;
    }

    /**
     * Resets the process.
     */
    public synchronized void reset() {
        isPaused = true;
        isCrashed = false;
        hasCrashed = false;
        localTime = 0;
        globalTime = 0;
        clockOffset = 0;

        for (VSAbstractProtocol protocol : protocolsToReset)
            protocol.reset();

        setCurrentColor(getColor("col.process.default"));
        resetTimeFormats();
    }

    /**
     * Creates the random crash task. The crash task will be created only if
     * the process is not crashed atm. and if VSProcess.getARandomCrashTime()
     * returns a non-negative value. The random crash task uses the simulaion's
     * global time for its scheduling.
     */
    public void createRandomCrashTask() {
        if (!isCrashed) {
            VSTaskManager taskManager = simulationCanvas.getTaskManager();
            long crashTime = getARandomCrashTime();

            if (crashTime < 0)
                return;

            if (randomCrashTask != null)
                taskManager.removeTask(randomCrashTask);

            if (crashTime >= getGlobalTime())  {
                VSAbstractEvent event = new VSProcessCrashEvent();
                randomCrashTask = new VSTask(crashTime, this, event,
                                             VSTask.GLOBAL);
                taskManager.addTask(randomCrashTask);

            } else {
                randomCrashTask = null;
            }
        }
    }

    /**
     * Creates a random percentage 0..100 using the process' own pseudo
     * random number generator object of the VSRandom class.
     *
     * @return A random percentage 0..100.
     */
    public synchronized int getRandomPercentage() {
        return random.nextInt() % 101;
    }

    /**
     * Adds the clock offset. This method is used by the task manager. The
     * clock offset identifies if the local time of the process has changed and
     * how much..
     *
     * @param add the clock offset to add.
     */
    public synchronized void addClockOffset(long add) {
        this.clockOffset += add;
    }

    /**
     * The process' state is 'play'. Called by the simulation canvas.
     */
    public synchronized void play() {
        isPaused = false;
        setCurrentColor(getColor("col.process.running"));
    }

    /**
     * The process' state is 'pause'. Called by the simulation canvas.
     */
    public synchronized void pause() {
        isPaused = true;
        setCurrentColor(getColor("col.process.stopped"));
    }

    /**
     * The process' state is 'Finish'. Called by the simulation canvas.
     */
    public synchronized void finish() {
        isPaused = true;
        setCurrentColor(getColor("col.process.default"));
    }

    /**
     * Gets the process id.
     *
     * @return the process id
     */
    public synchronized int getProcessID() {
        return processID;
    }

    /**
     * Gets the process num.  The num is different to the process id. It
     * represents the array index of there the process is stored at.
     *
     * @return the process num
     */
    public synchronized int getProcessNum() {
        return processNum;
    }

    /**
     * Sets the process id.
     *
     * @param processID the new process id
     */
    public synchronized void setProcessID(int processID) {
        this.processID = processID;
    }

    /**
     * Gets the current process' color.
     *
     * @return the current color of the process.
     */
    public synchronized Color getColor() {
        return currentColor;
    }

    /**
     * Sets the local time.
     *
     * @param localTime the new local time.
     */
    public synchronized void setLocalTime(final long localTime) {
        if (localTime >= 0)
            this.localTime = localTime;
        else
            this.localTime = 0;
    }

    /**
     * Gets the process' local time.
     *
     * @return the process' local time
     */
    public synchronized long getTime() {
        return localTime;
    }

    /**
     * Sets the process' local time.
     *
     * @param time the new local time of the process.
     */
    public synchronized void setTime(final long time) {
        if (time >= 0)
            this.localTime = time;
        else
            this.localTime = 0;

        this.timeModified = true;
    }

    /**
     * Checks if the process is crashed.
     *
     * @return true, if is crashed
     */
    public synchronized boolean isCrashed() {
        return isCrashed;
    }

    /**
     * Sets if the process is crashed.
     *
     * @param isCrashed true if the process is crashed.
     */
    public synchronized void isCrashed(boolean isCrashed) {
        this.isCrashed = isCrashed;
        crashHistory.add(new Long(globalTime));
        if (!hasCrashed)
            hasCrashed = true;
    }

    /**
     * Checks if the process has crashed. The difference to isCrashed is,
     * that the process may be fully functional again after crashing. This
     * method is needed by the simulation canvas in order to see if it should
     * paint 'crashed areas' using the crash history of this process.
     *
     * @return true, if yes
     */
    public synchronized boolean hasCrashed() {
        return hasCrashed;
    }

    /**
     * Gets the color of this process if it's crashed.
     *
     * @return the crashed color
     */
    public synchronized Color getCrashedColor() {
        return crashedColor;
    }

    /**
     * Checks if the time has been modified. by a task.
     * This mehod is needed by the task manager in order to add a clock offset
     * to the process object.
     *
     * @return true, if yes
     */
    public synchronized boolean timeModified() {
        return timeModified;
    }

    /**
     * Sets if the time has been modified by a task.
     *
     * @param timeModified true, if it has been modified.
     */
    public synchronized void timeModified(boolean timeModified) {
        this.timeModified = timeModified;
    }

    /**
     * Gets the global time.
     *
     * @return the global time
     */
    public synchronized long getGlobalTime() {
        return globalTime;
    }

    /**
     * Sets the global time.
     *
     * @param globalTime the new global time
     */
    public synchronized void setGlobalTime(final long globalTime) {
        this.globalTime = globalTime >= 0 ? globalTime : 0;
    }

    /**
     * Gets the clock variance.
     *
     * @return the clock variance
     */
    public synchronized float getClockVariance() {
        return clockVariance;
    }

    /**
     * Sets the clock variance.
     *
     * @param clockVariance the new clock variance
     */
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

    /**
     * Gets the duration time of a message to send.
     *
     * @return the duration time
     */
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

    /**
     * Gets the a random message outage time.
     *
     * @param durationTime the duration time
     *
     * @return the a random message outage time. It will be -1 if the message
     *	will not get lost at all.
     */
    public synchronized long getARandomMessageOutageTime(long durationTime,
            VSProcess receiverProcess) {
        int percentage = (int) ((getInteger("message.prob.outage") +
                                 receiverProcess.getInteger(
                                     "message.prob.outage")) / 2);

        /* Check if the message will have an outage or not */
        if (getRandomPercentage() < percentage) {

            /* Calculate the random outage time! */
            long outageTime = globalTime + random.nextLong(durationTime+1) %
                              simulationCanvas.getUntilTime();

            return outageTime;
        }

        /* No outage */
        return -1;
    }

    /**
     * Gets the a random crash time.
     *
     * @return the a random crash time. It will be -1 if the process will not
     *	crash at all randomly!
     */
    private long getARandomCrashTime() {
        /* Check if the process will crash or not */
        if (getRandomPercentage() < getInteger("process.prob.crash")) {
            /* Calculate the random crash time! */
            final long crashTime =  random.nextLong(
                                        simulationCanvas.getUntilTime()+1) %
                                    simulationCanvas.getUntilTime();
            return crashTime;
        }

        /* No crash */
        return -1;
    }

    /**
     * Gets the random crash task.
     *
     * @return the random crash task
     */
    public synchronized VSTask getCrashTask() {
        return randomCrashTask;
    }

    /**
     * Checks if the process is paused.
     *
     * @return true, if is paused
     */
    public synchronized boolean isPaused() {
        return isPaused;
    }

    /**
     * Increases the process' lamport time.
     */
    public void increaseLamportTime() {
        setLamportTime(getLamportTime()+1);
    }

    /**
     * Updates the process' lamport time.
     *
     * @param time the lamport time to use as its update reference.
     */
    public void updateLamportTime(long time) {
        final long lamportTime = getLamportTime() + 1;

        if (time > lamportTime)
            setLamportTime(time);
        else
            setLamportTime(lamportTime);
    }

    /**
     * Gets the lamport time.
     *
     * @return the lamport time.
     */
    public synchronized long getLamportTime() {
        return lamportTime;
    }

    /**
     * Sets the lamport time.
     *
     * @param lamportTime the new lamport time
     */
    public synchronized void setLamportTime(long lamportTime) {
        this.lamportTime = lamportTime;
        lamportTimeHistory.add(new VSLamportTime(globalTime, lamportTime));
    }

    /**
     * Gets the lamport time history as an array.
     *
     * @return the lamport time history array
     */
    public synchronized VSTime[] getLamportTimeArray() {
        final int size = lamportTimeHistory.size();
        final VSTime[] arr = new VSLamportTime[size];

        for (int i = 0; i < size; ++i)
            arr[i] = (VSTime) lamportTimeHistory.get(i);

        return arr;
    }

    /**
     * Increases the vector time.
     */
    public synchronized void increaseVectorTime() {
        vectorTime.set(processNum,
                       new Long(vectorTime.get(processNum).longValue()+1));
        vectorTime.setGlobalTime(globalTime);
        vectorTimeHistory.add(vectorTime.getCopy());
    }

    /**
     * Updates the vector time.
     *
     * @param vectorTimeUpdate the vector time of the other process to use for
     *	the update
     */
    public synchronized void updateVectorTime(VSVectorTime vectorTimeUpdate) {
        final int size = vectorTime.size();

        for (int i = 0; i < size; ++i) {
            if (i == processNum)
                vectorTime.set(i, new Long(vectorTime.get(i).longValue()+1));
            else if (vectorTimeUpdate.get(i) > vectorTime.get(i))
                vectorTime.set(i, vectorTimeUpdate.get(i));
        }

        vectorTime.setGlobalTime(globalTime);
        vectorTimeHistory.add(vectorTime.getCopy());
    }

    /**
     * Gets the vector time.
     *
     * @return the vector time
     */
    public synchronized VSVectorTime getVectorTime() {
        return vectorTime;
    }

    /**
     * Gets the vector time history as an array.
     *
     * @return the vector time history array
     */
    public synchronized VSTime[] getVectorTimeArray() {
        final int size = vectorTimeHistory.size();
        final VSTime[] arr = new VSTime[size];

        for (int i = 0; i < size; ++i)
            arr[i] = (VSTime) vectorTimeHistory.get(i);

        return arr;
    }

    /**
     * Gets the crash history array.
     *
     * @return the crash history array
     */
    public synchronized Long[] getCrashHistoryArray() {
        final int size = crashHistory.size();
        final Long[] arr = new Long[size];

        for (int i = 0; i < size; ++i)
            arr[i] = crashHistory.get(i);

        return arr;
    }

    /**
     * Called by a task if the process sends a message.
     *
     * @param message the message to send.
     */
    public void sendMessage(VSMessage message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefs.getString("lang.message.sent"));
        buffer.append("; ");
        buffer.append(message.toStringFull());
        logg(buffer.toString());
        simulationCanvas.sendMessage(message);
    }

    /**
     * Logg a message to the logging area.
     *
     * @param message the message to logg
     */
    public void logg(String message) {
        logging.logg(toString() + "; " + message, globalTime);
    }

    /* (non-Javadoc)
     * @see prefs.VSPrefs#fillWithDefaults()
     */
    public void fillWithDefaults() {
        prefs.copyIntegers(this, DEFAULT_INTEGER_VALUE_KEYS);
        prefs.copyLongs(this, DEFAULT_LONG_VALUE_KEYS);
        prefs.copyFloats(this, DEFAULT_FLOAT_VALUE_KEYS);
        prefs.copyColors(this, DEFAULT_COLOR_VALUE_KEYS);
        prefs.copyStrings(this, DEFAULT_STRING_VALUE_KEYS);
    }

    /* (non-Javadoc)
     * @see prefs.VSPrefs#toString()
     */
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

    /**
     * The extended string representation of the process object.
     *
     * @return the extended string representation
     */
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

    /**
     * Equals. Checks, if both processes have the same process num.
     *
     * @param process the process to compare to
     *
     * @return true, if both processes are the same (same processNum).
     */
    public boolean equals(VSProcess process) {
        return process.getProcessNum() == processNum;
    }

    /**
     * Gets the simulation canvas.
     *
     * @return the simulation canvas
     */
    public VSSimulatorCanvas getSimulationCanvas() {
        return simulationCanvas;
    }

    /**
     * Gets the simulation's default prefs.
     *
     * @return the default prefs
     */
    public VSPrefs getPrefs() {
        return prefs;
    }

    /**
     * Resets the process counter. The next newly created process will have
     * "0" as its process num. This static method is used by the simulator
     * canvas if it opens a new simulation.
     */
    public static void resetProcessCounter() {
        processCounter = 0;
    }

    /**
     * Removes the process at the specified index. Called by the simulation
     * canvas if a process has been removed from the simulation. Needed in
     * order to update the vector time and the local processNum.
     *
     * @param index the index the process has to get removed.
     */
    public void removeProcessAtIndex(int index) {
        if (index < processNum)
            --processNum;

        vectorTime.remove(index);
        for (VSVectorTime vectorTime : vectorTimeHistory)
            vectorTime.remove(index);
    }

    /**
     * Added a process. Needed in order to update the vector time's size.
     * Called by the simulation canvas if a process has been added to the
     * simulation.
     */
    public void addedAProcess() {
        vectorTime.add(new Long(0));
        for (VSVectorTime vectorTime : vectorTimeHistory)
            vectorTime.add(new Long(0));
    }

    /**
     * Gets the protocol object.
     *
     * @param protocolClassname the protocol classname
     *
     * @return the protocol object
     */
    public VSAbstractProtocol getProtocolObject(String protocolClassname) {
        VSAbstractProtocol protocol = null;

        if (!objectExists(protocolClassname)) {
            protocol = (VSAbstractProtocol)
                       VSRegisteredEvents.createEventInstanceByClassname(
                           protocolClassname, this);

            setObject(protocolClassname, protocol);
            protocolsToReset.add(protocol);

        } else {
            protocol = (VSAbstractProtocol) getObject(protocolClassname);
        }

        return protocol;
    }
}
