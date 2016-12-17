package com.gp.cong.logisoft.lcl.kn.bc;

import com.logiware.datamigration.LoadOpenArToLogiware;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Rajesh
 */
public class PropertyUtils {

    private static final Logger log = Logger.getLogger(PropertyUtils.class);
    private static final String propertyFile = "/com/logiware/datamigration/dbconnection.properties";
    private static Properties property;

    public static String getProperty(String key) {
        try {
            if (null == property) {
                property = new Properties();
                property.load(LoadOpenArToLogiware.class.getResourceAsStream(propertyFile));
            }
        } catch (IOException exception) {
            log.error(exception);
        }
        return property.getProperty(key);

    }
}
