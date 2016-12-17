package com.logiware.jobscheduler;

import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import java.io.Serializable;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import org.apache.log4j.Logger;
/**
 *
 * @author Lakshminarayanan
 */
public class OpenAllLocksJob implements Job, Serializable {
private static final Logger log = Logger.getLogger(OpenAllLocksJob.class);
    private static final long serialVersionUID = 5511962490380908088L;

    public void execute(JobExecutionContext jec) {
        try{
           log.info("Open All Locks started on "+new Date());
           new ProcessInfoDAO().truncate();
        }catch(Exception e){
            log.info("Open All Locks Job failed on "+new Date(),e);
    }
}
}
