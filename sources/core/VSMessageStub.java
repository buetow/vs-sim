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

package core;

import core.time.*;
import events.*;
import prefs.VSPrefs;

/**
 * An object of this class represents a message stub. A message stub allows
 * to run the init method on a VSMessage object. The init method should be
 * hidden by the protocol programming API.
 *
 * @author Paul C. Buetow
 */
public class VSMessageStub {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The message */
    private VSMessage message;

    /**
     * The constructor of the message stub. Creates a new message stub object.
	 *
	 * @message the message
     */
    public VSMessageStub(VSMessage message) {
        this.message = message;
    }

	
    public void init(VSInternalProcess process, String protocolClassname,
                     boolean isServerMessage) {
		message.init(process, protocolClassname, isServerMessage);
    }
}

