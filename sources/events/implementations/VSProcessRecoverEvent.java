/*
 * VS-Simulator (http://vs-sim.buetow.org)
 * Copyright (c) 2008 - 2009 by Dipl.-Inform. (FH) Paul C. Buetow
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

package events.implementations;

import events.*;
import simulator.*;

/**
 * The class VSProcessRecoverEvent. This event makes a process to recover if
 * it is crashed.
 *
 * @author Paul C. Buetow
 */
public class VSProcessRecoverEvent extends VSAbstractEvent
            implements VSCopyableEvent {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see events.VSCopyableEvent#initCopy(events.VSAbstractEvent)
     */
    public void initCopy(VSAbstractEvent copy) {
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    public void onInit() {
        setClassname(getClass().toString());
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#createShortname()()
     */
    protected String createShortname(String savedShortname) {
	return VSMain.prefs.getString("lang.en.process.recover");
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onStart()
     */
    public void onStart() {
        if (process.isCrashed()) {
            process.isCrashed(false);
            log(prefs.getString("lang.en.recovered"));
        }
    }
}
