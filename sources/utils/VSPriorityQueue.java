package utils;

import java.util.PriorityQueue;

public final class VSPriorityQueue<T> extends PriorityQueue<T> {
    public T get(int index) {
        int i = 0;

        for (T t : this)
            if (i++ == index)
                return t;

        return null;
    }
}
