package core;

import java.util.ArrayList;

public class VSVectorTime extends ArrayList<Long> implements VSTime {
    /* Only needed for painting in the painting panel */
    private long globalTime;

    public VSVectorTime(long globalTime) {
        this.globalTime = globalTime;
    }

    public long[] toLongArray() {
        final int size = super.size();
        final long[] arr = new long[size];

        for (int i = 0; i < size; ++i)
            arr[i] = super.get(i).longValue();

        return arr;
    }

    public void setGlobalTime(long globalTime) {
        this.globalTime = globalTime;
    }

    public long getGlobalTime() {
        return globalTime;
    }

    public VSVectorTime getCopy() {
        final VSVectorTime vectorTime = new VSVectorTime(globalTime);
        final int size = super.size();

        for (int i = 0; i < size; ++i)
            vectorTime.add(super.get(i));

        return vectorTime;
    }

    public String toString() {
        final int size = super.size();
        final StringBuffer buffer = new StringBuffer();
        buffer.append("(");

        for (int i = 0; i < size-1; ++i)
            buffer.append(super.get(i)+",");
        buffer.append(super.get(size-1)+")");

        return buffer.toString();
    }
}
