/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 *
 * @author Admin
 */
public class PrintProperties{
private static final Logger log = Logger.getLogger(PrintProperties.class);
    /**
     * Get the separtor to make the space between field name and field value while printing
     * to console.
     * @param str - field value length
     * @return space
     */
    private String getSeparators(String str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < (50 - str.length()); i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    /**
     * Get the value for the field.
     * @param object - current class object
     * @param field - field
     * @return - field value
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.Exception
     */
    public Object getValue(Object object, Field field) throws IllegalArgumentException, IllegalAccessException, Exception {
        return getGetter(object,field).invoke(object);
    }

    private Method getGetter(Object object, Field field) throws NoSuchMethodException{
        return object.getClass().getMethod(getMethodName(field, "get"));
    }

    /**
     * Get the mthod name for instance variable which could be setter and getter.
     * @param f - Field of class
     * @param s - Get or Set
     * @return - method name of field.
     */
    private String getMethodName(Field f, String s) {
        String fName = f.getName();
        StringBuffer MName = new StringBuffer(s);
        MName.append(fName.substring(0, 1).toUpperCase());
        MName.append(fName.substring(1));
        return MName.toString();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("========================== " + this.getClass().getSimpleName() + " ================================\n");
        try {
            Field [] fields = this.getClass().getDeclaredFields();
            for (Field f : fields) {
                buffer.append(f.getName());
                buffer.append(getSeparators(f.getName()));
                buffer.append(getValue(this,f));
                buffer.append("\n");
            }
            buffer.append("=====================================================================");
        } catch (Exception e) {
        log.info("toString() failed on " + new Date(),e);
        }
        return buffer.toString();
    }
}
