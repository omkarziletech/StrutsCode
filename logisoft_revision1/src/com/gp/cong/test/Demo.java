package com.gp.cong.test;

import com.logiware.common.domain.Property;
import com.logiware.common.form.PropertyForm;
import org.apache.commons.beanutils.PropertyUtils;

public class Demo {

    public static void main(String[] args) throws Exception {
        String str = "lakshmi,narayanan";
        String[] arr = str.split(",");
        for(String s : arr){
            System.out.println(s);
        }
    }
    
    public Property showProperty(PropertyForm form) throws Exception{
        Property property = new Property();
        PropertyUtils.copyProperties(property, form);
        return property;
    }

}
