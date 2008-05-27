/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs;

import java.io.Serializable;
import java.util.Vector;

/**
 * The Class VSPrefsRestriction.
 */
public class VSPrefsRestriction implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The Class VSIntegerPrefRestriction.
     */
    public static class VSIntegerPrefRestriction extends VSPrefsRestriction {
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
        public VSIntegerPrefRestriction(int minValue, int maxValue) {
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
     * The Class StringVSPrefsRestriction.
     */
    public static class StringVSPrefsRestriction extends VSPrefsRestriction {
        private static final long serialVersionUID = 1L;

        /** The possible selections. */
        Vector<String> possibleSelections;

        /**
         * Instantiates a new string setting restriction.
         *
         * @param possibleSelections the possible selections
         */
        public StringVSPrefsRestriction(String [] possibleSelections) {
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
