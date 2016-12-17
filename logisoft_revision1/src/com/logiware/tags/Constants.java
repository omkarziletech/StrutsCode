package com.logiware.tags;

/**
 *
 * @author Lakshmi Narayanan
 */
public class Constants {

    public static java.util.List getAll(java.lang.String className) throws ClassNotFoundException {
	Class clazz = Class.forName(className);
	java.util.List<java.lang.String> list = new java.util.ArrayList<java.lang.String>();
	for (Object object : clazz.getEnumConstants()) {
	    list.add(object.toString());
	}
	return list;
    }
}
