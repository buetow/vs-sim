/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package utils;



// TODO: Auto-generated Javadoc
/**
 * The Class VSClassLoader.
 */
public class VSClassLoader extends ClassLoader {

    /**
     * New instance.
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
