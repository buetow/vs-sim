/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core.time;

/**
 * This interface is a guidline for general time format classes.
 *
 * @author Paul C. Buetow
 */
public interface VSTime {
    /**
     * Gets the global time.
     *
     * @return The global time
     */
    public long getGlobalTime();

    /**
     * Returns a string representation.
     *
     * @return The representation of the implementing object as a string
     */
    public String toString();
}
