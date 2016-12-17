package com.logiware.properties;

import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * Query properties to pass the property and value for query.
 * @author sunil
 */
public class QueryProperties {
private static final Logger log = Logger.getLogger(QueryProperties.class);
    static Properties prop = null;

    private static void init() throws Exception {
        prop = new Properties();
            prop.load(QueryProperties.class.getResourceAsStream("/com/logiware/properties/Query.properties"));
    }

    /**
     * @param property
     * @return
     * geeting property from logisoft properties File...
     */
    public static String getProperty(String property)throws Exception {
        if (prop == null) {
            init();
        }
        return prop.getProperty(property);
    }

    /**
     * @param property
     * @return
     * geeting property from logisoft properties File...
     */
    public static String getQuery(String property)throws Exception {
        if (prop == null) {
            init();
        }
        return prop.getProperty(property);
    }

    /**
     * Get the Query by property.
     * @param property - property name
     * @param params - runtime values for properties file. ex:  ${0}, ${1} etc
     * @return
     */
    public static String getQuery(String property, String... params)throws Exception {
        if (prop == null) {
            init();
        }
        String query = prop.getProperty(property);
        if (query!=null && query.contains("other_db")) {
            String dbSchema = LoadLogisoftProperties.getProperty("elite.database.name");
            query = query.replaceAll("other_db", dbSchema);
        }
        for (int i = 0; i < params.length; i++) {
            String key = "\\{" + i + "\\}";
            String value = params[i];
            query = query.replaceAll(key, value);
        }
        return query;
    }

    public static void main(String[] args)throws Exception {
        log.info(getQuery("FCL_FILE_NUMBER", new String[]{"123"}));
    }
}
