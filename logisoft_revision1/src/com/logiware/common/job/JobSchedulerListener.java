package com.logiware.common.job;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import org.quartz.ee.servlet.QuartzInitializerListener;

/**
 *
 * @author Lakshmi Narayanan
 */
public class JobSchedulerListener extends QuartzInitializerListener {

    private static Properties prop = null;

    public void init() {
        prop = new Properties();
        try {
            prop.load(JobSchedulerListener.class.getResourceAsStream("/quartz.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JobScheduler.servletContext = sce.getServletContext();
        try {
            if (prop == null) {
                init();
            }
            String enabled = prop.getProperty("org.quartz.scheduler.enabled");
            if (null != enabled && "true".equalsIgnoreCase(enabled.trim())) {
                new JobScheduler().init();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (prop == null) {
                init();
            }
            String enabled = prop.getProperty("org.quartz.scheduler.enabled");
            if (null != enabled && "true".equalsIgnoreCase(enabled.trim())) {
                new JobScheduler().destroy();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Set<PooledDataSource> pooledDataSourceSet = (Set<PooledDataSource>) C3P0Registry.getPooledDataSources();
        for (PooledDataSource dataSource : pooledDataSourceSet) {
            try {
                dataSource.hardReset();
                dataSource.close();
            } catch (SQLException e) {
                // note - do not use log4j since it may have been unloaded by this point
                e.printStackTrace();
            }
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
