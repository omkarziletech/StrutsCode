package com.logiware.jobscheduler;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import java.io.Serializable;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author Lakshminarayanan
 */
public class CollectorAssignmentJob extends BaseHibernateDAO implements Job, Serializable {

    private static final long serialVersionUID = 5511962490345678088L;
    private static final Logger log = Logger.getLogger(CollectorAssignmentJob.class);

    public void execute(JobExecutionContext jec) {
	try {
	    log.debug("Assign Collector to Trading Partner started on " + new Date());
	    Transaction hibernateTransaction = getCurrentSession().beginTransaction();
	    new UserDAO().assignCollectorToTradingPartner();
	    hibernateTransaction.commit();
	} catch (Exception e) {
	    log.info("Assign Collector to Trading Partner failed on " + new Date() + " :\n " , e);
	}
    }
}
