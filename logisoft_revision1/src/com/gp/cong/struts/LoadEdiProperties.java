package com.gp.cong.struts;

import java.util.Properties;

public class LoadEdiProperties {

    static Properties prop = null;

    public static Properties getProp() {
        return prop;
    }

    public static void setProp(Properties prop) {
        LoadEdiProperties.prop = prop;
    }

    private static void init()throws Exception {
        prop = new Properties();
            prop.load(LoadEdiProperties.class.getResourceAsStream("/com/gp/cong/struts/edi.properties"));
    }

    public static String getProperty(String property)throws Exception {
        if (prop == null) {
            init();
        }
        return null != prop.getProperty(property)?prop.getProperty(property).trim():"";
    }

    public String getTreeMenuLable(String label) throws Exception {
        if (prop == null) {
            init();
        }
        return prop.getProperty(label);
    }
}
