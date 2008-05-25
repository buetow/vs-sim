/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package simulator;

/**
 * The Class VSMenuItemStates. Used by the VSSimulation to update the
 * "simulation" bar of the VSSimulationFrame.
 */
public class VSMenuItemStates {
    /** The pause. */
    private volatile boolean pause;

    /** The replay. */
    private volatile boolean replay;

    /** The reset. */
    private volatile boolean reset;

    /** The start. */
    private volatile boolean start;

    /**
     * Instantiates a new lang.process.removemenu item states.
     *
     * @param pause the pause
     * @param replay the replay
     * @param reset the reset
     * @param start the start
     */
    public VSMenuItemStates(boolean pause, boolean replay, boolean reset, boolean start) {
        this.pause = pause;
        this.replay = replay;
        this.reset = reset;
        this.start = start;
    }

    /**
     * Sets the pause.
     *
     * @param pause the new pause
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * Sets the replay.
     *
     * @param replay the new replay
     */
    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    /**
     * Sets the reset.
     *
     * @param reset the new reset
     */
    public void setReset(boolean reset) {
        this.reset = reset;
    }

    /**
     * Sets the start.
     *
     * @param start the new start
     */
    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * Gets the pause.
     *
     * @return the pause
     */
    public boolean getPause() {
        return pause;
    }

    /**
     * Gets the replay.
     *
     * @return the replay
     */
    public boolean getReplay() {
        return replay;
    }

    /**
     * Gets the reset.
     *
     * @return the reset
     */
    public boolean getReset() {
        return reset;
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public boolean getStart() {
        return start;
    }
}
