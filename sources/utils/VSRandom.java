/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.util.Random;

/**
 * The Class VSRandom. Some customization of the standard Random class of Java.
 *
 * @author Paul C. Buetow
 */
public final class VSRandom extends Random {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VSrandom object.
     *
     * @param seedAdd the seed to add.
     */
    public VSRandom(long seedAdd) {
        super(seedAdd*System.currentTimeMillis()+seedAdd);
    }

    /* (non-Javadoc)
     * @see java.util.Random#nextInt()
     */
    public int nextInt() {
        return Math.abs(super.nextInt());
    }

    /**
     * Next long.
     *
     * @param mod the mod
     *
     * @return the random long
     */
    public long nextLong(long mod) {
        return Math.abs((super.nextLong() + System.currentTimeMillis()) % mod);
    }
}
