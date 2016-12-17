/**
 *
 */
package com.gp.cong.logisoft.bc.scheduler;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.struts.util.LabelValueBean;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.struts.form.EmailSchedulerForm;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 */
public class EmailSchedulerBC {

    /**
     *
     */
    private static final String TODAY = "TODAY";
    private static final String LAST_WEEK = "LAST_WEEK";
    private static final String PENDING = "PENDING";
    private static final String COMPLETED = "COMPLETED";
    static List<LabelValueBean> calenderList = new ArrayList<LabelValueBean>();
    static List<LabelValueBean> statusList = new ArrayList<LabelValueBean>();
    private static final Logger log = Logger.getLogger(EmailSchedulerBC.class);

//    public EmailSchedulerBC() {
//        // TODO Auto-generated constructor stub
//    }
//
    public List<EmailSchedulerVO> searchMail(String sortByFromDate, String sortByToDate, String startDate, String endDate, String status, String userName, String fileName, String toEmailOrFax) throws Exception {
	List<EmailSchedulerVO> mailList = null;
	EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
	mailList = emailschedulerDAO.findForEmails1(sortByFromDate, sortByToDate, startDate, endDate, status, userName, fileName, toEmailOrFax);
	return mailList;
    }

    public List<EmailSchedulerVO> searchMail(EmailSchedulerForm emailSchedulerForm) throws Exception {
	List<EmailSchedulerVO> mailList = null;
	EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
	mailList = emailschedulerDAO.findForEmailSchedular(emailSchedulerForm);
	return mailList;
    }

    public List<EmailSchedulerVO> searchMailUsingModuleId(String moduleId, String modulName) throws Exception {
	List<EmailSchedulerVO> mailList = null;
	EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
	mailList = emailschedulerDAO.findForEmailsUsingModuleId(moduleId, modulName);
	return mailList;
    }

    public String[] getDatesForCriteria(String sortBy) {
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String sortByFromDate = null;
	String sortByToDate = null;
	Calendar calendar = new GregorianCalendar();
	try {
	    if (sortBy.equals(TODAY)) {
		sortByToDate = dateFormat.format(calendar.getTime());
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		sortByFromDate = dateFormat.format(calendar.getTime());
	    } else if (sortBy.equals(LAST_WEEK)) {
		sortByToDate = dateFormat.format(calendar.getTime());
		calendar.add(Calendar.DAY_OF_WEEK, -7);
		sortByFromDate = dateFormat.format(calendar.getTime());
	    }
	} catch (Exception e) {
	    log.info("getDatesForCriteria failed on " + new Date(),e);
	}
	String[] dates = {sortByFromDate, sortByToDate};
	return dates;
    }

    public static List<LabelValueBean> getCalenderList() {
	if (calenderList.size() == 0) {
	    calenderList.add(new LabelValueBean("1 DAY", TODAY));
	    calenderList.add(new LabelValueBean("7 DAYS", LAST_WEEK));
	}
	return calenderList;
    }

    public static List<LabelValueBean> getStatusList() {
	if (statusList.size() == 0) {
	    statusList.add(new LabelValueBean("Pending", PENDING));
	    statusList.add(new LabelValueBean("Completed", COMPLETED));
	}
	return statusList;
    }
}
