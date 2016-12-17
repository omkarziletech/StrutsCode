package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.CONTACT_MODE_EMAIL;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.scheduler.EmailSchedulerBC;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.struts.form.EmailSchedulerForm;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

/**
 * @author Administrator
 *
 */
public class EmailschedulerDAO extends BaseHibernateDAO implements ConstantsInterface {

    public List<EmailSchedulerVO> findForEmails(Date sortByFromDate, Date sortByToDate, Date startDate, Date endDate, String status) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EmailSchedulerVO.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sortFromDate = null;
        String sortToDate = null;
        sortFromDate = dateFormat.format(sortByFromDate);
        sortToDate = dateFormat.format(sortByToDate);
        sortByFromDate = dateFormat.parse(sortFromDate);
        sortByToDate = dateFormat.parse(sortToDate);
        criteria.add(Restrictions.between("emailDate", sortByFromDate, sortByToDate));
        criteria.addOrder(Order.asc("emailDate"));
        //for status
        if (status != null && !status.equals("")) {
            criteria.add(Restrictions.eq("status", status));
            criteria.addOrder(Order.asc("status"));
        }
        //for start and end date
        if (startDate != null && !startDate.equals("")) {
            criteria.add(Restrictions.ge("emailDate", startDate));
            criteria.addOrder(Order.asc("emailDate"));
        }
        if (endDate != null && !endDate.equals("")) {
            criteria.add(Restrictions.le("emailDate", endDate));
            criteria.addOrder(Order.asc("emailDate"));
        }
        return criteria.list();
    }

    public List<EmailSchedulerVO> findForEmailSchedular(EmailSchedulerForm emailSchedulerForm) throws Exception {
        String fromDate = null;
        String toDate = null;
        String[] dates = new EmailSchedulerBC().getDatesForCriteria(emailSchedulerForm.getSortBy());
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getStartDate())) {
            fromDate = DateUtils.formatDate(DateUtils.parseDate(emailSchedulerForm.getStartDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getEndDate())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtils.parseDate(emailSchedulerForm.getEndDate(), "MM/dd/yyyy"));
            cal.add(cal.DATE, 1);
            toDate = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd");
        }
        StringBuffer queryString = null;
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getStatus())) {
            queryString = new StringBuffer("from EmailSchedulerVO where status='" + emailSchedulerForm.getStatus() + "'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getModuleName())) {
            queryString.append(" and moduleName='" + emailSchedulerForm.getModuleName() + "'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getUserName()) && !"All".equalsIgnoreCase(emailSchedulerForm.getUserName())) {
            queryString.append(" and userName='" + emailSchedulerForm.getUserName() + "'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getFileName())) {
            queryString.append(" and moduleId like '%" + emailSchedulerForm.getFileName() + "%'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getToEmailOrFax())) {
            queryString.append(" and toAddress like '%" + emailSchedulerForm.getToEmailOrFax() + "%'");
        } else if (CommonUtils.isNotEmpty(fromDate) && CommonUtils.isNotEmpty(toDate)) {
            queryString.append("and emailDate between '" + fromDate + "' and '" + toDate + "'");
        } else if (CommonUtils.isNotEmpty(dates[0]) && CommonUtils.isNotEmpty(dates[1])) {
            queryString.append("and emailDate between '" + dates[0] + "' and '" + dates[1] + "'");
        }
        queryString.append(" order by emailDate desc");
        List<EmailSchedulerVO> list = getCurrentSession().createQuery(queryString.toString()).list();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            EmailSchedulerVO emailSchedulerVO = (EmailSchedulerVO) iterator.next();
            if (null != emailSchedulerVO.getEmailDate()) {
                emailSchedulerVO.setFormatedEmailDate(null != emailSchedulerVO.getEmailDate() ? format.format(emailSchedulerVO.getEmailDate()) : "");
            }
            if (emailSchedulerVO.getFileLocation() != null && !emailSchedulerVO.getFileLocation().equals("")) {
                if (emailSchedulerVO.getFileLocation().indexOf('/') > -1) {
                    emailSchedulerVO.setTempFileLocation(emailSchedulerVO.getFileLocation().substring(emailSchedulerVO.getFileLocation().indexOf('/'),
                            emailSchedulerVO.getFileLocation().length()));
                } else {
                    emailSchedulerVO.setTempFileLocation(emailSchedulerVO.getFileLocation());
                }
            }

        }
        return list;
    }

    public List<EmailSchedulerVO> findForEmails1(String sortByFromDate, String sortByToDate, String startDate,
            String endDate, String status, String userName, String fileName, String toEmailOrFax) throws Exception {
        String fromDate = null;
        String toDate = null;
        if (CommonUtils.isNotEmpty(startDate)) {
            fromDate = DateUtils.formatDate(DateUtils.parseDate(startDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        }
        if (CommonUtils.isNotEmpty(endDate)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtils.parseDate(endDate, "MM/dd/yyyy"));
            cal.add(cal.DATE, 1);
            toDate = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd");
        }
        StringBuffer queryString = new StringBuffer("from EmailSchedulerVO where status='" + status + "'");
        if (fileName != null && !fileName.equals("")) {
            queryString.append(" and moduleId like '%" + fileName + "%'");
        } else {
            if (userName != null && !userName.equals("") && !userName.equals("All")) {
                queryString.append(" and userName='" + userName + "'");
            }

            if (CommonUtils.isNotEmpty(toEmailOrFax)) {
                queryString.append(" and toAddress like '%" + toEmailOrFax + "%'");
            }
            if (CommonUtils.isNotEmpty(fromDate) && CommonUtils.isNotEmpty(toDate)) {
                queryString.append(" and emailDate between '" + fromDate + "' and '" + toDate + "'");
            } else if (CommonUtils.isNotEmpty(sortByFromDate) && CommonUtils.isNotEmpty(sortByToDate)) {
                queryString.append(" and emailDate between '" + sortByFromDate + "' and '" + sortByToDate + "'");
            }
        }
        queryString.append(" order by emailDate desc");
        List<EmailSchedulerVO> list = getCurrentSession().createQuery(queryString.toString()).list();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            EmailSchedulerVO emailSchedulerVO = (EmailSchedulerVO) iterator.next();
            if (null != emailSchedulerVO.getEmailDate()) {
                emailSchedulerVO.setFormatedEmailDate(null != emailSchedulerVO.getEmailDate() ? format.format(emailSchedulerVO.getEmailDate()) : "");
            }
            if (emailSchedulerVO.getFileLocation() != null && !emailSchedulerVO.getFileLocation().equals("")) {
                if (emailSchedulerVO.getFileLocation().indexOf('/') > -1) {
                    emailSchedulerVO.setTempFileLocation(emailSchedulerVO.getFileLocation().substring(emailSchedulerVO.getFileLocation().indexOf('/'),
                            emailSchedulerVO.getFileLocation().length()));
                } else {
                    emailSchedulerVO.setTempFileLocation(emailSchedulerVO.getFileLocation());
                }
            }

        }
        return list;
    }

    public List<EmailSchedulerVO> findForEmailsUsingModuleId(String moduleId, String moduleName) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer queryString = null;
        if (moduleId != null && !moduleId.equals("")) {
            queryString = new StringBuffer("from EmailSchedulerVO where moduleId='" + moduleId + "' and status='pending'");
        }
        if (moduleName != null && moduleName.equalsIgnoreCase("quotation")) {
            queryString.append(" and moduleName='Quotation'");
        } else if (moduleName != null && moduleName.equalsIgnoreCase("Booking")) {
            queryString.append(" and moduleName in ('Quotation','Booking')");
        } else if (moduleName != null && moduleName.equalsIgnoreCase("BL")) {
            queryString.append(" and moduleName in ('Quotation','Booking','BL')");
        }
        queryString.append(" order by id desc");
        List<EmailSchedulerVO> list = getCurrentSession().createQuery(queryString.toString()).list();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            EmailSchedulerVO emailSchedulerVO = (EmailSchedulerVO) iterator.next();
            emailSchedulerVO.setFormatedEmailDate(format.format(emailSchedulerVO.getEmailDate()));
            if (emailSchedulerVO.getFileLocation() != null && !emailSchedulerVO.getFileLocation().equals("")) {
                emailSchedulerVO.setTempFileLocation(emailSchedulerVO.getFileLocation().substring(emailSchedulerVO.getFileLocation().indexOf('/'), emailSchedulerVO.getFileLocation().length()));
            }

        }
        return list;
    }

    public List<EmailSchedulerVO> findForEmailsStatusModuleId(String moduleId, String moduleName) throws Exception {
        StringBuffer queryString = null;
        if (moduleId != null && !moduleId.equals("")) {
            queryString = new StringBuffer("from EmailSchedulerVO where moduleId like '%" + moduleId + "%'");
        }
        if (moduleName != null && moduleName.equalsIgnoreCase("quotation")) {
            queryString.append(" and moduleName='Quotation'");
        } else if (moduleName != null && moduleName.equalsIgnoreCase("Booking")) {
            queryString.append(" and moduleName ='Booking'");
        } else if (moduleName != null && moduleName.equalsIgnoreCase("BL")) {
            queryString.append(" and moduleName ='BL'");
        }
        queryString.append(" order by id desc");
        return getCurrentSession().createQuery(queryString.toString()).list();
    }

    public List<EmailSchedulerVO> getPendingEmails() throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(EmailSchedulerVO.class);
        criteria.add(Restrictions.eq("status", "Pending"));
        criteria.add(Restrictions.eq("type", CONTACT_MODE_EMAIL));
        criteria.setMaxResults(200);
        return criteria.list();
    }

    public List<EmailSchedulerVO> getPendingPrints() throws Exception {
        getCurrentSession().flush();
        String[] types = new String[]{CONTACT_MODE_PRINT, CONTACT_MODE_LABEL_PRINT};
        Criteria criteria = getCurrentSession().createCriteria(EmailSchedulerVO.class);
        criteria.add(Restrictions.eq("status", "Pending"));
        criteria.add(Restrictions.in("type", types));
        criteria.setMaxResults(30);
        return criteria.list();
    }

    public List<EmailSchedulerVO> getPendingFaxes() throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(EmailSchedulerVO.class);
        criteria.add(Restrictions.eq("status", "Pending"));
        criteria.add(Restrictions.eq("type", CONTACT_MODE_FAX));
        criteria.setMaxResults(30);
        return criteria.list();
    }

    public List<EmailSchedulerVO> getPendingFaxAndPrint() throws Exception {
        if (null != getSession()) {
            getCurrentSession().flush();
            String queryString = "from EmailSchedulerVO where status='Pending' and (type='" + CONTACT_MODE_PRINT + "' or type='" + CONTACT_MODE_LABEL_PRINT + "' or type='" + CONTACT_MODE_FAX + "')";
            Query query = getCurrentSession().createQuery(queryString);
            query.setMaxResults(15);
            return query.list();
        }
        return null;
    }

    public void save(EmailSchedulerVO transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void saveEmail(EmailSchedulerVO transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void saveOrUpdate(EmailSchedulerVO transientInstance) throws Exception {
        getCurrentSession().saveOrUpdate(transientInstance);
        getCurrentSession().flush();
    }

    public void update(EmailSchedulerVO persistanceInstance) throws Exception {
        getCurrentSession().update(persistanceInstance);
        getCurrentSession().flush();
    }

    public void updateStatus(EmailSchedulerVO persistanceInstance, String status) throws Exception {
        int noofTries = "Completed".equalsIgnoreCase(status) ? 1 : 0;
        String query = "update mail_transactions set status='" + status + "',no_of_tries='" + noofTries + "' where id=" + persistanceInstance.getId();
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public void updateStatus(Integer id, String status, String responseCode) throws Exception {
        StringBuilder query = new StringBuilder("update mail_transactions");
        query.append(" set status='").append(status).append("'");
        if (null != responseCode && !responseCode.isEmpty()) {
            query.append(",response_code='").append(responseCode).append("'");
        }
        if (null != responseCode && !responseCode.equals("error")) {
            query.append(",jobnum='").append(responseCode).append("'");
        }
        query.append(" where id=").append(id);
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public EmailSchedulerVO findById(java.lang.Integer id) throws Exception {
        EmailSchedulerVO instance = (EmailSchedulerVO) getSession().get("com.gp.cong.logisoft.domain.EmailSchedulerVO", id);
        return instance;
    }
 
    public String getSalesPersonMailId(String moduleId, String importflag) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT g.field3  FROM quotation q JOIN cust_general_info c ON");
        if (importflag.equalsIgnoreCase("true")) {
            sb.append(" c.acct_no=q.clientnumber JOIN genericcode_dup g ON g.id=c.cons_sales_code WHERE q.file_no =:moduleId");
        } else {
            sb.append(" c.acct_no=q.clientnumber JOIN genericcode_dup g ON g.id=c.sales_code WHERE q.file_no =:moduleId");
        }
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("moduleId", moduleId);
        return (String) queryObject.uniqueResult();
    }

    public GenericCode getSalesPersonDetails(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  g.`code` as code,");
        queryBuilder.append("  g.`codedesc` as codedesc,");
        queryBuilder.append("  g.`field3` as field3 ");
        queryBuilder.append("from");
        queryBuilder.append("  genericcode_dup g,");
        queryBuilder.append("  cust_general_info c,");
        queryBuilder.append("  lcl_quote l ");
        queryBuilder.append("where l.`file_number_id` = :fileNumberId ");
        queryBuilder.append("  and l.`client_acct_no` = c.`acct_no`");
        queryBuilder.append("  and c.`cons_sales_code` = g.`id`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNumberId", fileNumberId);
        query.addScalar("code", StringType.INSTANCE);
        query.addScalar("codedesc", StringType.INSTANCE);
        query.addScalar("field3", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(GenericCode.class));
        return (GenericCode)query.uniqueResult();
    }
    public GenericCode getSalesPersonDetailsForFcl(String fileNumber, String importflag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  g.`code` as code,");
        queryBuilder.append("  g.`codedesc` as codedesc,");
        queryBuilder.append("  g.`field3` as field3 ");
        queryBuilder.append("from");
        queryBuilder.append("  genericcode_dup g,");
        queryBuilder.append("  cust_general_info c,");
        queryBuilder.append("  quotation q ");
        queryBuilder.append("where q.`file_no` = :fileNumber ");
        queryBuilder.append("  and q.`clientnumber` = c.`acct_no`");
        if (importflag.equalsIgnoreCase("true")) {
            queryBuilder.append("  and c.`cons_sales_code` = g.`id`");
        } else {
            queryBuilder.append("  and c.`sales_code` = g.`id`");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileNumber", fileNumber);
        query.addScalar("code", StringType.INSTANCE);
        query.addScalar("codedesc", StringType.INSTANCE);
        query.addScalar("field3", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(GenericCode.class));
        return (GenericCode) query.uniqueResult();
    }

    public String getDisputeEmailStatus(String moduleId) throws Exception {
        StringBuilder query = new StringBuilder("select count(*) from mail_transactions");
        query.append(" where module_name='ACCRUALS' and status='").append(STATUS_DISPUTE).append("'");
        query.append(" and to_address!='' and file_location!='' ");
        query.append(" and module_id='").append(moduleId).append("'");
        Object count = getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
        return null != count && Integer.parseInt(count.toString()) > 0 ? STATUS_DISPUTE : "";
    }

    public List<EmailSchedulerVO> getDisputeEmail(String moduleId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EmailSchedulerVO.class);
        criteria.add(Restrictions.eq("moduleName", "ACCRUALS"));
        criteria.add(Restrictions.eq("status", STATUS_DISPUTE));
        criteria.add(Restrictions.eq("moduleId", moduleId));
        criteria.add(Restrictions.ne("fileLocation", ""));
        criteria.add(Restrictions.ne("toAddress", ""));
        return criteria.list();
    }

    public List<String> getByEmail(String email) throws Exception {
        String query = "select distinct(to_address) from mail_transactions where to_address like '%" + email + "%'";
        return getSession().createSQLQuery(query).list();
    }

    public List<EmailSchedulerVO> getFaxes(String moduleName, String status, String lastEmailDate) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EmailSchedulerVO.class);
        criteria.add(Restrictions.eq("moduleName", moduleName));
        criteria.add(Restrictions.eq("type", "Fax"));
        criteria.add(Restrictions.eq("status", status));
        criteria.add(Restrictions.ge("emailDate", DateUtils.parseDate(lastEmailDate, "yyyy-MM-dd")));
        return criteria.list();
    }

    public boolean isEmailedOrFaxed(String fileNo, String moduleId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,1,0) as result");
        queryBuilder.append(" from mail_transactions");
        queryBuilder.append(" where module_id = '").append(moduleId).append("'");
        queryBuilder.append(" or module_id = '").append(fileNo).append("'");
        queryBuilder.append(" limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void saveMailTransactions(final String name, final String fileLocation,
            final String type, final String status, Date emailDate, final String to,
            final String from, final String cc, final String subject, final String htmlMsg,
            final String textMsg, final String moduleName, final String moduleId,
            final String userName, final String coverLetter, final String printerName,
            final Integer printCopy, final String responseCode) throws Exception {
        EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
        mailTransaction.setName(name);
        mailTransaction.setFileLocation(fileLocation);
        mailTransaction.setType(type);
        mailTransaction.setStatus(status);
        mailTransaction.setNoOfTries(0);
        mailTransaction.setEmailDate(emailDate);
        mailTransaction.setToAddress(to);
        mailTransaction.setFromAddress(from);
        mailTransaction.setCcAddress(cc);
        mailTransaction.setSubject(subject);
        mailTransaction.setHtmlMessage(htmlMsg);
        mailTransaction.setTextMessage(textMsg);
        mailTransaction.setModuleName(moduleName);
        mailTransaction.setModuleId(moduleId);
        mailTransaction.setUserName(userName);
        mailTransaction.setCoverLetter(coverLetter);
        mailTransaction.setPrinterName(printerName);
        mailTransaction.setPrintCopy(printCopy);
        mailTransaction.setResponseCode(responseCode);
        save(mailTransaction);
    }

    public List<EmailSchedulerVO> searchEmailSchedular(EmailSchedulerForm emailSchedulerForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id AS id,NAME AS name,file_location AS fileLocation,TYPE AS type,STATUS AS status,");
        sb.append("no_of_tries AS noOfTries,email_date AS emailDate,to_address AS toAddress,cc_address As ccAddress,bcc_address As bccAddress,user_name AS userName,module_id as moduleId,from_address as FromAddress FROM mail_transactions");
        sb.append(" WHERE status='").append(emailSchedulerForm.getStatus()).append("'");
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getStartDate()) && CommonUtils.isNotEmpty(emailSchedulerForm.getEndDate())) {
            String startDate = DateUtils.formateDateToDDMMMYYYY(emailSchedulerForm.getStartDate());
            String endDate = DateUtils.formateDateToDDMMMYYYY(emailSchedulerForm.getEndDate());
            sb.append(" AND email_date BETWEEN '").append(startDate).append(" 00:00:00'").append(" AND '").append(endDate).append(" 23:59:59'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getToEmailOrFax())) {
            sb.append(" AND to_address LIKE '%").append(emailSchedulerForm.getToEmailOrFax()).append("%'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getCcAddress())) {
            sb.append(" AND cc_address LIKE '%").append(emailSchedulerForm.getCcAddress()).append("%'");
        }
         if (CommonUtils.isNotEmpty(emailSchedulerForm.getBccAddress())) {
            sb.append(" AND bcc_address LIKE '%").append(emailSchedulerForm.getBccAddress()).append("%'");
        }
        
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getFileName())) {
            sb.append(" AND module_id='").append(emailSchedulerForm.getFileName()).append("'");
        }
        if (CommonUtils.isNotEmpty(emailSchedulerForm.getUserName())) {
            sb.append(" AND user_name='").append(emailSchedulerForm.getUserName()).append("'");
        }
        if(CommonUtils.isNotEmpty(emailSchedulerForm.getFilterByName())){
            sb.append(" AND  name='").append(emailSchedulerForm.getFilterByName()).append("'");
        }
        sb.append(" ORDER BY id desc LIMIT ").append(emailSchedulerForm.getLimit());
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(EmailSchedulerVO.class));
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("name", StringType.INSTANCE);
        query.addScalar("fileLocation", StringType.INSTANCE);
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("noOfTries", IntegerType.INSTANCE);
        query.addScalar("emailDate", TimestampType.INSTANCE);
        query.addScalar("toAddress", StringType.INSTANCE);
        query.addScalar("ccAddress", StringType.INSTANCE);
        query.addScalar("bccAddress", StringType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("moduleId", StringType.INSTANCE);
        query.addScalar("moduleId", StringType.INSTANCE);
        query.addScalar("fromAddress", StringType.INSTANCE);
        return query.list();
    }
}
