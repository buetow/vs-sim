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

package serialize;

import java.io.*;

/**
 * The Interface VSSerializable, all classes which take part of the serialize/
 * deserialize proces are implementing this interface. It is preferred over the
 * standard Serializable interface of Java because we want not serialize the
 * whole class tree of each class but certain variables only!
 *
 * @author Paul C. Buetow
 */
public interface VSSerializable {
    /**
     * Serializes
     *
     * @param serialize The serialize object
     * @param objectOutputStream The object output stream
     */
    public void serialize(VSSerialize serialize,
                          ObjectOutputStream objectOutputStream)
    throws IOException;
    /**
     * Deserializes
     *
     * @param serialize The serialize object
     * @param objectInputStream The object input stream
     */
    public void deserialize(VSSerialize serialize,
                            ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException;
}
