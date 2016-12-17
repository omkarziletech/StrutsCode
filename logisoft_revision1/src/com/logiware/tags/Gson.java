package com.logiware.tags;

/**
 *
 * @author Lakshmi Narayanan
 */
public class Gson {

    private final static com.google.gson.Gson gson = new com.google.gson.Gson();

    public static java.lang.Object fromJson(java.lang.String string, java.lang.String className) throws Exception {
        Class clazz = Class.forName(className);
        return gson.fromJson(string, clazz);
    }

    public static java.lang.String toJson(java.lang.Object object) throws Exception {
        return gson.toJson(object);
    }
}
