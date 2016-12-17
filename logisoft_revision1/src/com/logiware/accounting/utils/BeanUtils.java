package com.logiware.accounting.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class BeanUtils {

    public static void copyProperties(Object fromObj, Object toObj, String... excludes) throws Exception {
	Class<? extends Object> fromClass = fromObj.getClass();
	Class<? extends Object> toClass = toObj.getClass();
	BeanInfo fromBean = Introspector.getBeanInfo(fromClass);
	BeanInfo toBean = Introspector.getBeanInfo(toClass);
	PropertyDescriptor[] toDescriptors = toBean.getPropertyDescriptors();
	List<PropertyDescriptor> fromDescriptors = Arrays.asList(fromBean.getPropertyDescriptors());
	List<String> excludeProperties = null != excludes ? Arrays.asList(excludes) : new ArrayList<String>();
	for (PropertyDescriptor toDescriptor : toDescriptors) {
	    if (!excludeProperties.contains(toDescriptor.getDisplayName())
		    && !toDescriptor.getDisplayName().equals("class") && toDescriptor.getWriteMethod() != null) {
		PropertyDescriptor fromDescriptor = fromDescriptors.get(fromDescriptors.indexOf(toDescriptor));
		if (fromDescriptor.getDisplayName().equals(toDescriptor.getDisplayName())) {
		    toDescriptor.getWriteMethod().invoke(toObj, fromDescriptor.getReadMethod().invoke(fromObj, (Object[]) null));
		}
	    }
	}
    }
    
    public static void copyProperties(Object fromObj, Object toObj, List<String> include) throws Exception {
	Class<? extends Object> fromClass = fromObj.getClass();
	Class<? extends Object> toClass = toObj.getClass();
	BeanInfo fromBean = Introspector.getBeanInfo(fromClass);
	BeanInfo toBean = Introspector.getBeanInfo(toClass);
	PropertyDescriptor[] toDescriptors = toBean.getPropertyDescriptors();
	List<PropertyDescriptor> fromDescriptors = Arrays.asList(fromBean.getPropertyDescriptors());
	for (PropertyDescriptor toDescriptor : toDescriptors) {
	    if (include.contains(toDescriptor.getDisplayName())
		    && !toDescriptor.getDisplayName().equals("class") && toDescriptor.getWriteMethod() != null) {
		PropertyDescriptor fromDescriptor = fromDescriptors.get(fromDescriptors.indexOf(toDescriptor));
		if (fromDescriptor.getDisplayName().equals(toDescriptor.getDisplayName())) {
		    toDescriptor.getWriteMethod().invoke(toObj, fromDescriptor.getReadMethod().invoke(fromObj, (Object[]) null));
		}
	    }
	}
    }
}
