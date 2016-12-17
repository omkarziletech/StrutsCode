package com.logiware.utils;

import java.lang.reflect.Method;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TagUtils {
    //Checks to see if Object 'o' is an instance of the class in the string "className"
    public static boolean instanceOf(Object o, String className)throws Exception {
        boolean returnValue;
        try {
            returnValue = Class.forName(className).isInstance(o);
        } catch (ClassNotFoundException e) {
            returnValue = false;
        }
        return returnValue;
    }

    //Checks to see if Object 'o' has a property specified in "propertyName"
    public static boolean hasProperty(Object o, String propertyName) {
        boolean methodFound = false;
        int i = 0;

        Class myClass = o.getClass();
        String methodName = "get" + propertyName.toUpperCase().charAt(0) + propertyName.substring(1);
        Method[] methods = myClass.getMethods();
        while (i < methods.length && !methodFound) {
            methodFound = methods[i].getName().compareTo(methodName) == 0;
            i++;
        }
        return methodFound;
    }
}
