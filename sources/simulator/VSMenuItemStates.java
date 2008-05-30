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

package simulator;

/**
 * The class VSMenuItemStates. Used by the VSSimulation to update the
 * "simulation" bar of the VSSimulationFrame.
 *
 * @author Paul C. Buetow
 */
public class VSMenuItemStates {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The pause state. */
    private volatile boolean pause;

    /** The replay state. */
    private volatile boolean replay;

    /** The reset state. */
    private volatile boolean reset;

    /** The start state. */
    private volatile boolean start;

    /**
     * Instantiates a new VSMenuItemStates object.
     *
     * @param pause the pause state
     * @param replay the replay state
     * @param reset the reset state
     * @param start the start state
     */
    public VSMenuItemStates(boolean pause, boolean replay, boolean reset,
                            boolean start) {
        this.pause = pause;
        this.replay = replay;
        this.reset = reset;
        this.start = start;
    }

    /**
     * Sets the pause state.
     *
     * @param pause the new pause state
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * Sets the replay state.
     *
     * @param replay the new replay state
     */
    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    /**
     * Sets the reset state.
     *
     * @param reset the new reset state
     */
    public void setReset(boolean reset) {
        this.reset = reset;
    }

    /**
     * Sets the start state.
     *
     * @param start the new start state
     */
    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * Gets the pause state.
     *
     * @return the pause state
     */
    public boolean getPause() {
        return pause;
    }

    /**
     * Gets the replay state.
     *
     * @return the replay state
     */
    public boolean getReplay() {
        return replay;
    }

    /**
     * Gets the reset state.
     *
     * @return the reset state
     */
    public boolean getReset() {
        return reset;
    }

    /**
     * Gets the start state.
     *
     * @return the start state
     */
    public boolean getStart() {
        return start;
    }
}
