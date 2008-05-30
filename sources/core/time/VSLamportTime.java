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

package core.time;

/**
 * The class VSLamportTime. This class defined how the lamport
 * timestamps are represented.
 *
 * @author Paul C. Buetow
 */
public class VSLamportTime implements VSTime {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** Specified the global time of the lamport timestamp. It's used for
     * correct painting position in the simulator canvas paint area.
     */
    private long globalTime;

    /** Specified the process' local lamport time. */
    private long lamportTime;

    /**
     * A simple constructor.
     *
     * @param globalTime The global time.
     * @param lamportTime The local lamport time.
     */
    public VSLamportTime(long globalTime, long lamportTime) {
        this.globalTime = globalTime;
        this.lamportTime = lamportTime;
    }

    /* (non-Javadoc)
     * @see core.time.VSTime#getGlobalTime()
     */
    public long getGlobalTime() {
        return globalTime;
    }

    /**
     * Gets the lamport time.
     *
     * @return The process' local lamport time
     */
    public long getLamportTime() {
        return lamportTime;
    }

    /* (non-Javadoc)
     * @see core.time.VSTime#toString()
     */
    public String toString() {
        return "(" + lamportTime + ")";
    }
}
