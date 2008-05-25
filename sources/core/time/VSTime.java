/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package core.time;

// TODO: Auto-generated Javadoc
/**
 * This interface is a guidline for general time format classes.
 */
public interface VSTime {
    
    /**
     * Getter method.
     * 
     * @return The global time.
     */
    public long getGlobalTime();
    
    /**
     * String representation.
     * 
     * @return The representation of the implementing object as a string.
     */
    public String toString();
}
