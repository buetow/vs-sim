/*
 * Copyright (c) 2008 Paul C. Buetow, vs-sim@dev.buetow.org
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

package core.time;

import java.util.ArrayList;

/**
 * The class VSVectorTime, defined how the vector timestamps are represented.
 *
 * @author Paul C. Buetow
 */
public class VSVectorTime extends ArrayList<Long> implements VSTime {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The global time. */
    private long globalTime;

    /**
     * Instantiates a new vector time.
     *
     * @param globalTime the global time
     */
    public VSVectorTime(long globalTime) {
        this.globalTime = globalTime;
    }

    /**
     * To long array.
     *
     * @return the long[]
     */
    public long[] toLongArray() {
        final int size = super.size();
        final long[] arr = new long[size];

        for (int i = 0; i < size; ++i)
            arr[i] = super.get(i).longValue();

        return arr;
    }

    /**
     * Sets the global time.
     *
     * @param globalTime the new global time
     */
    public void setGlobalTime(long globalTime) {
        this.globalTime = globalTime;
    }

    /* (non-Javadoc)
     * @see core.time.VSTime#getGlobalTime()
     */
    public long getGlobalTime() {
        return globalTime;
    }

    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public VSVectorTime getCopy() {
        final VSVectorTime vectorTime = new VSVectorTime(globalTime);
        final int size = super.size();

        for (int i = 0; i < size; ++i)
            vectorTime.add(super.get(i));

        return vectorTime;
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#toString()
     */
    public String toString() {
        final int size = super.size();
        final StringBuffer buffer = new StringBuffer();
        buffer.append("(");

        for (int i = 0; i < size-1; ++i)
            buffer.append(super.get(i)+",");
        buffer.append(super.get(size-1)+")");

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.util.ArrayList#get(int)
     */
    public Long get(int index) {
        if (index >= super.size())
            return new Long(0);

        return super.get(index);
    }
}
