/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;

/**
 * The Class VSClassLoader. This class is used in order to create new objects
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
