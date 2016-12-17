/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ClassUtil {

    public static Map<String, Object> getPropertyValueMap(List<String> properties, Object instance) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        Class instanceClass = instance.getClass();
        for(String property : properties){         
            String methodName = "get"+ property.substring(0,1).toUpperCase() + property.substring(1);
            Object value = instanceClass.getMethod(methodName).invoke(instance);
            propertyMap.put(property, value);
        }
      return propertyMap;
    }

}
