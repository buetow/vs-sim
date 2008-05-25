/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core.time;

// TODO: Auto-generated Javadoc
/**
 * This class defined how the lamport timestamps are represented.
 */
public class VSLamportTime implements VSTime {
    private static final long serialVersionUID = 1L;

    /** Specified the global time of the lamport timestamp. It's used for correct painting in the simulator paint area. */
    private long globalTime;

    /** Specified the process' local lamport time. */
    private long lamportTime;

    /**
     * A simple constructor.
     *
     * @param globalTime The global time.
     * @param lamportTime The local lamport time.
     */
    public VSLamportTime(long globalTime, long lamportTime) {
        this.globalTime = globalTime;
        this.lamportTime = lamportTime;
    }

    /**
     * Getter method.
     *
     * @return The global time.
     */
    public long getGlobalTime() {
        return globalTime;
    }

    /**
     * Getter method.
     *
     * @return The process' local lamport time.
     */
    public long getLamportTime() {
        return lamportTime;
    }

    /**
     * String representation.
     *
     * @return The string representation of the lamport time.
     */
    public String toString() {
        return "(" + lamportTime + ")";
    }
}
