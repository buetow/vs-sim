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
 * The class VSClassLoader. This class is used in order to create new objects
 * by its classnames.
 *
 * @author Paul C. Buetow
 */
public class VSClassLoader extends ClassLoader {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of the given classname.
     *
     * @param classname the classname
     *
     * @return the object
     */
    public Object newInstance(String classname) {
        Object object = null;

        try {
            object = super.loadClass(classname, true).newInstance();

        } catch (Exception e) {
            System.out.println(e + "; Classname " + classname);
        }

        return object;
    }
}
