package com.gp.cong.logisoft.jobscheduler;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class MailPropertyReader {

    private static final Logger log = Logger.getLogger(MailPropertyReader.class);
    private static Properties properties;

    public MailPropertyReader() {
	if (properties == null) {
	    setProperties();
	}
    }

    public Properties getProperties() {
	return properties;
    }

    public void setProperties() {
	try {
	    properties = new Properties();
	    ClassLoader cl = ResourceLoader.class.getClassLoader();

	    properties.load(cl.getResourceAsStream("com/gp/cong/struts/mail.properties"));
	} catch (Exception e) {
	    log.info("setProperties() in MailPropertyReader failed on " + new Date(),e);
	}

    }
}
