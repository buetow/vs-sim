package utils;

import java.util.*;

import prefs.*;

public class VSClassLoader extends ClassLoader {
    public Object newInstance(String classname) {
        Object object = null;

        try {
            object = loadClass(classname, true).newInstance();

        } catch (Exception e) {
            System.out.println(e);
        }

        return object;
    }
}
