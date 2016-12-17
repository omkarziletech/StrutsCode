package com.logiware.common.query;

import java.util.Properties;

/**
 * Query properties to pass the property and value for query.
 *
 * @author Lakshmi Narayanan
 */
public class QueryProperties {

    private static Properties prop = null;

    private static void init() throws Exception {
        prop = new Properties();
        prop.load(QueryProperties.class.getResourceAsStream("/com/logiware/common/query/Query.properties"));
    }

    /**
     * @param property
     * @return getting property from properties file...
     * @throws java.lang.Exception
     */
    public static String getProperty(String property) throws Exception {
        if (prop == null) {
            init();
        }
        return prop.getProperty(property);
    }

    /**
     * @param property
     * @return getting property from properties file...
     * @throws java.lang.Exception
     */
    public static String getQuery(String property) throws Exception {
        if (prop == null) {
            init();
        }
        return prop.getProperty(property);
    }
}
