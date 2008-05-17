package utils;

import java.util.*;
import java.lang.reflect.*;

import prefs.*;
import core.*;

public class VSClassLoader extends ClassLoader {
    public Object newInstance(String classname) {
        Object object = null;

        try {
            object = super.loadClass(classname, true).newInstance();
            //Constructor constructors[] = theClass.getConstructors();
            //object = constructors[0].newInstance(process);

        } catch (Exception e) {
            System.out.println(e);
        }

        return object;
    }
}
