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

package events.internal;

import java.io.*;

import events.VSAbstractEvent;
import serialize.VSSerialize;

/**
 * The class VSAbstractInternalEvent, this class if for destinguishing between
 * internal and non-internal events. Internal usage only.
 *
 * @author Paul C. Buetow
 */
abstract public class VSAbstractInternalEvent extends VSAbstractEvent {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see serialize.VSSerializable#serialize(serialize.VSSerialize,
     *	java.io.ObjectOutputStream)
     */
    public synchronized void serialize(VSSerialize serialize,
                                       ObjectOutputStream objectOutputStream)
    throws IOException {
        super.serialize(serialize, objectOutputStream);

        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));

        /** For later backwards compatibility, to add more stuff */
        objectOutputStream.writeObject(new Boolean(false));
    }

    /* (non-Javadoc)
     * @see serialize.VSSerializable#deserialize(serialize.VSSerialize,
     *	java.io.ObjectInputStream)
     */
    @SuppressWarnings("unchecked")
    public synchronized void deserialize(VSSerialize serialize,
                                         ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException {
        super.deserialize(serialize, objectInputStream);

        if (VSSerialize.DEBUG)
            System.out.println("Deserializing: VSAbstractInternalEvent");

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();

        /** For later backwards compatibility, to add more stuff */
        objectInputStream.readObject();
    }
}
