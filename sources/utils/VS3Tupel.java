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

/**
 * The class VS3Tupel, an object of this class represents a 3-Tupel of objects.
 * Each object can have its own type.
 *
 * @author Paul C. Buetow
 */
public final class VS3Tupel<A,B,C> {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The a. */
    private A a;

    /** The b. */
    private B b;

    /** The c. */
    private C c;

    /**
     * Instantiates a new tupel.
     *
     * @param a the a
     * @param b the b
     * @param c the c
     */
    public VS3Tupel(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Gets the a.
     *
     * @return the a
     */
    public A getA() {
        return a;
    }

    /**
     * Gets the b.
     *
     * @return the b
     */
    public B getB() {
        return b;
    }

    /**
     * Gets the c.
     *
     * @return the c
     */
    public C getC() {
        return c;
    }
}
