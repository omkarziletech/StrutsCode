package com.logiware.common.servlet;

import com.logiware.common.job.JobScheduler;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.quartz.ee.servlet.QuartzInitializerServlet;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InitializeServlet extends QuartzInitializerServlet {

    private static final Logger log = Logger.getLogger(InitializeServlet.class);

    @Override
    public void init() throws ServletException {
	try {
	    JobScheduler.servletContext = this.getServletContext();
	    new JobScheduler().init();
//	    new TestScheduler().init();
	} catch (Exception e) {
	    log.info("Initilizing Report Jobs failed", e);
	}
    }
}
