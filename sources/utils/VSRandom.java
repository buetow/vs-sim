package utils;

import java.util.Random;

public final class VSRandom extends Random {
    public VSRandom(long seedAdd) {
        super(seedAdd*System.currentTimeMillis()+seedAdd);
    }

    public int nextInt() {
        return Math.abs(super.nextInt());
    }

    public long nextLong(long mod) {
        return Math.abs((super.nextLong() + System.currentTimeMillis()) % mod);
    }
}
