package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.Scheduler;
import com.gp.cong.logisoft.jobscheduler.JobScheduler;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.utils.ArCreditHoldUtils;
import com.logiware.utils.AuditNotesUtils;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.type.IntegerType;

public class SchedulerDAO extends BaseHibernateDAO implements TradingPartnerConstants {

    public Scheduler findById(java.lang.Integer id) throws Exception {
        Scheduler instance = (Scheduler) getSession().get(
                "com.gp.cong.logisoft.domain.Scheduler", id);
        return instance;
    }

    public void saveScheduler(Scheduler scheduler) throws Exception {
        getCurrentSession().save(scheduler);
        getCurrentSession().flush();
    }

    public void updateScheduler(Scheduler scheduler) throws Exception {
        getCurrentSession().update(scheduler);
        getCurrentSession().flush();
    }

    public void deleteScheduler(Scheduler scheduler) throws Exception {
        getCurrentSession().delete(scheduler);
        getCurrentSession().flush();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Scheduler as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List<Scheduler> getSchedulers(String role) throws Exception {
        List<Scheduler> schedulerList = null;
        Transaction transaction = getCurrentSession().getTransaction();
        if (null != transaction && !transaction.isActive()) {
            transaction.begin();
        }
        StringBuilder queryBuilder = new StringBuilder("from Scheduler");
        queryBuilder.append(" order by id");
        schedulerList = getCurrentSession().createQuery(queryBuilder.toString()).list();
        return schedulerList;
    }

    /**
     * Place accounts on credit hold.
     *
     * @throws Exception
     * @ticket 3685
     */
    public void arCreditHold() throws Exception {
        /**
         * Initialize
         */
        Integer gracePeriod = 0;
        Integer graceLimit = 0;

        if (LoadLogisoftProperties.getProperty("tradingPartnerPastDueBuffer") != null) {
            /**
             * grace period in days
             */
            gracePeriod = Integer.parseInt(LoadLogisoftProperties.getProperty("tradingPartnerPastDueBuffer"));
        }

        if (LoadLogisoftProperties.getProperty("tradingPartnerOverLimitBuffer") != null) {
            /**
             * grace over limit in percentage
             */
            graceLimit = Integer.parseInt(LoadLogisoftProperties.getProperty("tradingPartnerOverLimitBuffer"));
        }

        /**
         * Bulk reset accounts.
         */
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE");
        queryBuilder.append(" cust_accounting ca ");
        queryBuilder.append("SET");
        queryBuilder.append("  ca.over_limit = '").append(CommonConstants.OFF).append("',");
        queryBuilder.append("  ca.past_due = '").append(CommonConstants.OFF).append("',");
        queryBuilder.append("  ca.credit_limit = 0,");
        queryBuilder.append("  ca.credit_status = ");
        queryBuilder.append("   (");
        queryBuilder.append("    SELECT");
        queryBuilder.append("      id");
        queryBuilder.append("    FROM");
        queryBuilder.append("      genericcode_dup");
        queryBuilder.append("    WHERE");
        queryBuilder.append("      codedesc = '").append(NOCREDIT).append("'");
        queryBuilder.append("   ) ");
        queryBuilder.append("WHERE");
        queryBuilder.append("  (");
        queryBuilder.append("   ca.exempt_credit_process <> '").append(CommonConstants.YES).append("'");
        queryBuilder.append("   OR ca.exempt_credit_process IS NULL");
        queryBuilder.append("  )");
        queryBuilder.append("  AND");
        queryBuilder.append("  (");
        queryBuilder.append("   ca.credit_status IS NULL");
        queryBuilder.append("   OR ca.credit_limit IS NULL");
        queryBuilder.append("  )");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();

        /**
         * Locates all accounts that are over their limit.
         */
        StringBuilder queryOverLimit = new StringBuilder();
        queryOverLimit.append("SELECT");
        queryOverLimit.append("  ca.acct_no ");
        queryOverLimit.append("FROM");
        queryOverLimit.append("  cust_accounting ca,");
        queryOverLimit.append("  genericcode_dup gen_status ");
        queryOverLimit.append("WHERE");
        queryOverLimit.append("  (");
        queryOverLimit.append("   ca.exempt_credit_process != '").append(CommonConstants.YES).append("'");
        queryOverLimit.append("   OR ca.exempt_credit_process IS NULL");
        queryOverLimit.append("  )");
        queryOverLimit.append("  AND ca.credit_limit <> 0");
        queryOverLimit.append("  AND ca.credit_status IS NOT NULL");
        queryOverLimit.append("  AND");
        queryOverLimit.append("  (");
        queryOverLimit.append("   gen_status.id = ca.credit_status");
        queryOverLimit.append("   AND gen_status.codedesc <> '").append(NOCREDIT).append("'");
        queryOverLimit.append("  )");
        queryOverLimit.append("  AND ( ca.credit_limit + ( ( ca.credit_limit / 100 ) * ").append(graceLimit).append(" ) ) <");
        queryOverLimit.append("    (");
        queryOverLimit.append("     SELECT");
        queryOverLimit.append("       SUM( tr.Balance ) AS net_amount");
        queryOverLimit.append("     FROM");
        queryOverLimit.append("       TRANSACTION tr");
        queryOverLimit.append("     WHERE");
        queryOverLimit.append("       tr.cust_no = ca.acct_no");
        queryOverLimit.append("       AND tr.Transaction_type = '").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryOverLimit.append("       AND tr.Balance <> 0");
        queryOverLimit.append("    )");

        /**
         * Locates all accounts that are past due.
         */
        StringBuilder queryPastDue = new StringBuilder();
        queryPastDue.append("SELECT");
        queryPastDue.append("  ca.acct_no ");
        queryPastDue.append("FROM");
        queryPastDue.append("  cust_accounting ca,");
        queryPastDue.append("  genericcode_dup gen_status,");
        queryPastDue.append("  genericcode_dup gen_terms ");
        queryPastDue.append("WHERE");
        queryPastDue.append("  (");
        queryPastDue.append("   ca.exempt_credit_process != 'Y'");
        queryPastDue.append("   OR ca.exempt_credit_process IS NULL");
        queryPastDue.append("  )");
        queryPastDue.append("  AND ca.credit_limit <> 0");
        queryPastDue.append("  AND ca.credit_status IS NOT NULL");
        queryPastDue.append("  AND");
        queryPastDue.append("  (");
        queryPastDue.append("   gen_status.id = ca.credit_status");
        queryPastDue.append("   AND gen_status.codedesc <> '").append(NOCREDIT).append("'");
        queryPastDue.append("  )");
        queryPastDue.append("  AND gen_terms.id = ca.credit_rate");
        queryPastDue.append("  AND");
        queryPastDue.append("  (");
        queryPastDue.append("   SELECT");
        queryPastDue.append("     SUM( tr.Balance ) AS past_due_amount");
        queryPastDue.append("   FROM");
        queryPastDue.append("     TRANSACTION tr");
        queryPastDue.append("   WHERE");
        queryPastDue.append("     tr.cust_no = ca.acct_no");
        queryPastDue.append("     AND tr.Transaction_type = '").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryPastDue.append("     AND DATEDIFF( SYSDATE(), tr.Transaction_date ) >");
        queryPastDue.append("       (");
        queryPastDue.append("        gen_terms.code ");
        queryPastDue.append("        + ( IF( ca.past_due_buffer <> ").append(gracePeriod).append(", ca.past_due_buffer, ").append(gracePeriod).append(" ) )");
        queryPastDue.append("       )");
        queryPastDue.append("  ) > 0");

        /**
         * Generate eMails.
         */
        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("SELECT");
        queryBuilder.append("  ud.login_name AS collector,");
        queryBuilder.append("  ud.email AS collector_email,");
        queryBuilder.append("  tp.acct_name AS customer_name,");
        queryBuilder.append("  tp.acct_no AS customer_number,");
        queryBuilder.append("  tr.Bill_Ladding_No AS bl_number,");
        queryBuilder.append("  tr.shipper_name AS shipper,");
        queryBuilder.append("  tr.Cons_name AS consignee,");
        queryBuilder.append("  tr.Container_No AS container,");
        queryBuilder.append("  tr.voyage_no AS voyage,");
        queryBuilder.append("  tr.seal_no AS seal,");
        queryBuilder.append("  DATE_FORMAT(tr.eta, '%m/%d/%Y') AS eta,");
        queryBuilder.append("  tr.steam_ship_line AS steam_ship_line,");
        queryBuilder.append("  tr.vessel_name AS vessel_name ");
        queryBuilder.append("FROM");
        queryBuilder.append("  transaction tr");
        queryBuilder.append("  JOIN trading_partner tp");
        queryBuilder.append("    ON (tp.acct_no = tr.cust_no)");
        queryBuilder.append("  JOIN cust_accounting ca");
        queryBuilder.append("    ON (");
        queryBuilder.append("      ca.acct_no = tp.acct_no");
        queryBuilder.append("      AND ca.credit_limit <> 0");
        queryBuilder.append("      AND ca.credit_status IS NOT NULL");
        queryBuilder.append("      AND (");
        queryBuilder.append("        ca.exempt_credit_process <> '").append(CommonConstants.YES).append("'");
        queryBuilder.append("        OR ca.exempt_credit_process IS NULL");
        queryBuilder.append("      )");
        queryBuilder.append("    )");
        queryBuilder.append("  JOIN genericcode_dup gen_status");
        queryBuilder.append("    ON (");
        queryBuilder.append("      gen_status.id=ca.credit_status");
        queryBuilder.append("      AND gen_status.codedesc = '").append(CREDITHOLD).append("'");
        queryBuilder.append("    )");
        queryBuilder.append("  JOIN user_details ud");
        queryBuilder.append("    ON (");
        queryBuilder.append("      ud.user_id = ca.ar_contact_code");
        queryBuilder.append("      AND ud.email <> ''");
        queryBuilder.append("    )");
        queryBuilder.append("WHERE");
        queryBuilder.append("  tr.Transaction_type = '").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("  AND tr.Balance <> 0");
        queryBuilder.append("  AND tr.Bill_Ladding_No <> ''");
        queryBuilder.append("  AND tr.Credit_Hold = '").append(CommonConstants.YES).append("'");
        queryBuilder.append("  AND tr.emailed = TRUE");
        queryBuilder.append("  AND tr.cust_no NOT IN ( ");
        queryBuilder.append(queryOverLimit.toString());
        queryBuilder.append(" UNION ");
        queryBuilder.append(queryPastDue.toString());
        queryBuilder.append(" )");

        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        ServletContext servletContext = JobScheduler.servletContext;
        String imagePath = "http://" + servletContext.getResource(LoadLogisoftProperties.getProperty("application.image.logo")).getPath();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            String collector = (String) col[0];
            String collectorEmail = (String) col[1];
            String customerName = (String) col[2];
            String customerNumber = (String) col[3];
            String blNumber = (String) col[4];
            String shipper = (String) col[5];
            String consignee = (String) col[6];
            String container = (String) col[7];
            String voyage = (String) col[8];
            String seal = (String) col[9];
            String eta = (String) col[10];
            String steamShipLine = (String) col[11];
            String vesselName = (String) col[12];
            ArCreditHoldUtils.sendEmail(false, imagePath, collector, collectorEmail, customerNumber, blNumber, shipper, consignee, container, seal, voyage, eta, steamShipLine, vesselName, null);
            StringBuilder desc = new StringBuilder("BL# - ").append(blNumber).append(" of ");
            desc.append(customerName).append("(").append(customerNumber).append(")");
            desc.append(" taken off credit hold by System on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, customerNumber + "-" + blNumber, "AR Credit Hold", null);
        }
        getCurrentSession().flush();


        /**
         * Bulk remove credit hold on AR transactions.
         */
        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("UPDATE");
        queryBuilder.append("  transaction tr ");
        queryBuilder.append("SET");
        queryBuilder.append("  tr.Credit_Hold = '").append(CommonConstants.NO).append("',");
        queryBuilder.append("  tr.emailed = FALSE,");
        queryBuilder.append("  tr.removed_from_hold = TRUE ");
        queryBuilder.append("WHERE");
        queryBuilder.append("  tr.Transaction_type = '").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("  AND tr.Balance <> 0");
        queryBuilder.append("  AND tr.Credit_Hold = '").append(CommonConstants.YES).append("'");
        queryBuilder.append("  AND tr.cust_no NOT IN (select t.acct_no from ( ");
        queryBuilder.append(queryOverLimit.toString());
        queryBuilder.append(" UNION ");
        queryBuilder.append(queryPastDue.toString());
        queryBuilder.append(" ) as t)");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();

        /**
         * Bulk remove credit hold from cust_accounting.
         */
        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("UPDATE");
        queryBuilder.append("  cust_accounting ca,");
        queryBuilder.append("  genericcode_dup gen_status ");
        queryBuilder.append("SET");
        queryBuilder.append("  ca.over_limit = '").append(CommonConstants.OFF).append("',");
        queryBuilder.append("  ca.past_due = '").append(CommonConstants.OFF).append("',");
        queryBuilder.append("  ca.credit_status = ");
        queryBuilder.append("   (");
        queryBuilder.append("    SELECT");
        queryBuilder.append("      id");
        queryBuilder.append("    FROM");
        queryBuilder.append("      genericcode_dup");
        queryBuilder.append("    WHERE");
        queryBuilder.append("      codedesc = '").append(IN_GOOD_STANDING).append("' limit 1");
        queryBuilder.append("   ),");
        queryBuilder.append("  ca.credit_status_update_by = '").append(System.class.getSimpleName()).append("' ");
        queryBuilder.append("WHERE");
        queryBuilder.append("  (");
        queryBuilder.append("   ca.exempt_credit_process <> '").append(CommonConstants.YES).append("'");
        queryBuilder.append("   OR ca.exempt_credit_process IS NULL");
        queryBuilder.append("  )");
        queryBuilder.append("  AND ca.credit_limit <> 0");
        queryBuilder.append("  AND ca.credit_status IS NOT NULL");
        queryBuilder.append("  AND gen_status.id = ca.credit_status");
        queryBuilder.append("  AND gen_status.codedesc = '").append(CREDITHOLD).append("'");
        queryBuilder.append("  AND ca.acct_no NOT IN (select t.acct_no from ( ");
        queryBuilder.append(queryOverLimit.toString());
        queryBuilder.append(" UNION ");
        queryBuilder.append(queryPastDue.toString());
        queryBuilder.append(" ) as t)");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();

        /**
         * Bulk apply credit hold to PAST DUE accounts in cust_accounting.
         */
        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("UPDATE");
        queryBuilder.append("  cust_accounting ca ");
        queryBuilder.append("SET");
        queryBuilder.append("  ca.past_due = '").append(CommonConstants.ON).append("',");
        queryBuilder.append("  ca.credit_status = ");
        queryBuilder.append("   (");
        queryBuilder.append("    SELECT");
        queryBuilder.append("      id");
        queryBuilder.append("    FROM");
        queryBuilder.append("      genericcode_dup");
        queryBuilder.append("    WHERE");
        queryBuilder.append("      codedesc = '").append(CREDITHOLD).append("' limit 1");
        queryBuilder.append("   ),");
        queryBuilder.append("  ca.credit_status_update_by = '").append(System.class.getSimpleName()).append("' ");
        queryBuilder.append("WHERE");
        queryBuilder.append("  ca.acct_no IN (select t.acct_no from ( ");
        queryBuilder.append(queryPastDue.toString());
        queryBuilder.append(" ) as t)");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();

        /**
         * Bulk apply credit hold to OVER LIMIT accounts in cust_accounting.
         */
        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("UPDATE");
        queryBuilder.append("  cust_accounting ca ");
        queryBuilder.append("SET");
        queryBuilder.append("  ca.over_limit = '").append(CommonConstants.ON).append("',");
        queryBuilder.append("  ca.credit_status = ");
        queryBuilder.append("   (");
        queryBuilder.append("    SELECT");
        queryBuilder.append("      id");
        queryBuilder.append("    FROM");
        queryBuilder.append("      genericcode_dup");
        queryBuilder.append("    WHERE");
        queryBuilder.append("       codedesc = '").append(CREDITHOLD).append("' limit 1");
        queryBuilder.append("  ), ");
        queryBuilder.append("  ca.credit_status_update_by = '").append(System.class.getSimpleName()).append("' ");
        queryBuilder.append("WHERE");
        queryBuilder.append("  ca.acct_no IN (select t.acct_no from ( ");
        queryBuilder.append(queryOverLimit.toString());
        queryBuilder.append(" ) as t)");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void updateNumberOfTryCount(Integer id) throws Exception {
        String queryString = "select no_of_tries from mail_transactions where id = " + id;
        Query queryObject = getSession().createSQLQuery(queryString).addScalar("no_of_tries", IntegerType.INSTANCE);
        Object noOftries = queryObject.uniqueResult();
        int tries = null != noOftries ? (Integer) noOftries : 0;
        if (tries > 5) {
            queryString = "update mail_transaction set status = 'Canceled' where id = " + id;
            queryObject = getSession().createSQLQuery(queryString).addScalar("no_of_tries", IntegerType.INSTANCE);
            queryObject.executeUpdate();
        }
    }
}
