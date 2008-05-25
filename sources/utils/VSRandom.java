/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class VSRandom.
 */
public final class VSRandom extends Random {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new lang.process.removerandom.
     *
     * @param seedAdd the seed add
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
     * @return the long
     */
    public long nextLong(long mod) {
        return Math.abs((super.nextLong() + System.currentTimeMillis()) % mod);
    }
}
