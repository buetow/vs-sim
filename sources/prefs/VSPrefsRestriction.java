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

package prefs;

import java.io.Serializable;
import java.util.Vector;

/**
 * The class VSPrefsRestriction.
 */
public class VSPrefsRestriction implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The class VSIntegerPrefsRestriction.
     */
    public static class VSIntegerPrefsRestriction extends VSPrefsRestriction {
        private static final long serialVersionUID = 1L;
        /** The min value. */
        private int minValue;

        /** The max value. */
        private int maxValue;

        /**
         * Instantiates a new integer setting restriction.
         *
         * @param minValue the min value
         * @param maxValue the max value
         */
        public VSIntegerPrefsRestriction(int minValue, int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        /**
         * Gets the min value.
         *
         * @return the min value
         */
        public int getMinValue() {
            return minValue;
        }

        /**
         * Gets the max value.
         *
         * @return the max value
         */
        public int getMaxValue() {
            return maxValue;
        }
    }

    /**
     * The class VSStringPrefsRestriction.
     */
    public static class VSStringPrefsRestriction extends VSPrefsRestriction {
        private static final long serialVersionUID = 1L;

        /** The possible selections. */
        Vector<String> possibleSelections;

        /**
         * Instantiates a new string setting restriction.
         *
         * @param possibleSelections the possible selections
         */
        public VSStringPrefsRestriction(String [] possibleSelections) {
            this.possibleSelections = new Vector<String>();

            for (String elem : possibleSelections)
                this.possibleSelections.add(elem);
        }

        /**
         * Gets the possible selections.
         *
         * @return the possible selections
         */
        public Vector<String> getPossibleSelections() {
            return possibleSelections;
        }
    }
}
