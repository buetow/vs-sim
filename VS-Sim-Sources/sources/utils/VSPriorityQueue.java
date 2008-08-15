/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 *
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package utils;

import java.util.PriorityQueue;

/**
 * The class VSPriorityQueue. This class is the same like the standard
 * VSPriorityQueue of the Java API. It only overrides the get(int) method.
 *
 * @author Paul C. Buetow
 */
public final class VSPriorityQueue<T> extends PriorityQueue<T> {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Gets the specific element. If the index is out of bounds, it will return
     * null.
     *
     * @param index the index
     *
     * @return the element, or null, if out of bounds
     */
    public T get(int index) {
        int i = 0;

        for (T t : this)
            if (i++ == index)
                return t;

        return null;
    }
}
