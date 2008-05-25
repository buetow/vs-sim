package core.time;

/** This interface is a guidline for general time format classes.
 */
public interface VSTime {
    /** Getter method.
     * 
     * @return The global time.
     */
    public long getGlobalTime();
    /** String representation.
     * 
     * @return The representation of the implementing object as a string.
     */
    public String toString();
}
