/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.Factory;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ListUtils extends org.apache.commons.collections.ListUtils {

    private static final Logger log = Logger.getLogger(ListUtils.class);

    public static List lazyList(final Class c) throws Exception {
	Factory factory = new Factory() {
	    public Object create() {
		try {
		    return c.newInstance();
		} catch (InstantiationException ex) {
		    log.info("lazyList on ListUtils failed on " + new Date(),ex);
		} catch (IllegalAccessException ex) {
		    log.info("lazyList on ListUtils failed on " + new Date(),ex);
		}
		return null;
	    }
	};
	return ListUtils.lazyList(new ArrayList(), factory);
    }

    public static <T> List<List<T>> split(List<T> list, int limit, int min)
	    throws NullPointerException, IllegalArgumentException {
	if (list == null) {
	    throw new NullPointerException("The list parameter is null.");
	}
	if (limit <= 0) {
	    throw new IllegalArgumentException(
		    "The limit parameter must be more than 0.");
	}
	if (min <= 0) {
	    throw new IllegalArgumentException(
		    "The min parameter must be more than 0.");
	}
	List<List<T>> ret = new ArrayList<List<T>>();
	int listSize = list.size();
	if (min >= listSize) {
	    ret.add(list);
	} else {
	    int count = (listSize / limit) + (listSize % limit > 0 ? 1 : 0);
	    count = count > min ? count : min;
	    List<T> subList = new ArrayList<T>();
	    for (int i = 0; i < listSize; i++) {
		subList.add(list.get(i));
		if (subList.size() >= count || i + 1 == listSize) {
		    ret.add(subList);
		    subList = new ArrayList<T>();
		}
	    }
	}
	return ret;
    }
}
