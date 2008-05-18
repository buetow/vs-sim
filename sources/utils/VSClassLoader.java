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

        } catch (Exception e) {
            System.out.println(e + "; Classname " + classname);
        }

        return object;
    }
}
