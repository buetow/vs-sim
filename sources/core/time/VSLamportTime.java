/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core.time;

/**
 * The class VSLamportTime. This class defined how the lamport
 * timestamps are represented.
 *
 * @author Paul C. Buetow
 */
public class VSLamportTime implements VSTime {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** Specified the global time of the lamport timestamp. It's used for
     * correct painting position in the simulator canvas paint area.
     */
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

    /* (non-Javadoc)
     * @see core.time.VSTime#getGlobalTime()
     */
    public long getGlobalTime() {
        return globalTime;
    }

    /**
     * Gets the lamport time.
     *
     * @return The process' local lamport time
     */
    public long getLamportTime() {
        return lamportTime;
    }

    /* (non-Javadoc)
     * @see core.time.VSTime#toString()
     */
    public String toString() {
        return "(" + lamportTime + ")";
    }
}
