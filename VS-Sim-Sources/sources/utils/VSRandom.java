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

import java.util.Random;

/**
 * The class VSRandom. Some customization of the standard Random class of Java.
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
