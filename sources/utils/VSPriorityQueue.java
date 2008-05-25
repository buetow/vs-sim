/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

import java.util.PriorityQueue;

/**
 * The Class VSPriorityQueue.
 */
public final class VSPriorityQueue<T> extends PriorityQueue<T> {
    private static final long serialVersionUID = 1L;

    /**
     * Gets the element.
     *
     * @param index the index
     *
     * @return the element.
     */
    public T get(int index) {
        int i = 0;

        for (T t : this)
            if (i++ == index)
                return t;

        return null;
    }
}
