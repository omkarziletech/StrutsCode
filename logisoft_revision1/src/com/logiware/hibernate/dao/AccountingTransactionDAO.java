package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.struts.form.*;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.ArTransactionBean;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.*;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class Transaction(for Accounting
 * Module only).
 *
 * @see com.gp.cvst.logisoft.domain.Transaction
 * @author Lakshminarayanan
 */
public class AccountingTransactionDAO extends BaseHibernateDAO implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = -8604051996915806913L;

    public void save(Transaction transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void update(Transaction persistentInstance) throws Exception {
        getCurrentSession().update(persistentInstance);
        getCurrentSession().flush();
    }

    public void saveOrUpdate(Transaction instance) throws Exception {
        getCurrentSession().saveOrUpdate(instance);
        getCurrentSession().flush();
    }

    public void delete(Transaction persistentInstance) throws Exception {
        getCurrentSession().delete(persistentInstance);
        getCurrentSession().flush();
    }

    public Transaction findById(Integer id) throws Exception {
        Transaction instance = (Transaction) getCurrentSession().get("com.gp.cvst.logisoft.domain.Transaction", id);
        return instance;
    }

    public Transaction findById(String id) throws Exception {
        return findById(Integer.parseInt(id));
    }

    public List<Transaction> findByExample(Transaction instance) throws Exception {
        List results = getCurrentSession().createCriteria("com.gp.cvst.logisoft.domain.Transaction").add(Example.create(instance)).list();
        return results;
    }

    public List<Transaction> findByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return criteria.list();
    }

    public List<Transaction> findByPropertyWithMultipleValues(String propertyName, Collection value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.in(propertyName, value));
        return criteria.list();
    }

    public Integer getCreditTermDays(String creditTerms) throws Exception {
        Integer creditTermDays = 0;
        if (CommonUtils.isEqualIgnoreCase(creditTerms, AccountingConstants.DUE_UPON_RECEIPT)) {
            creditTermDays = 0;
        } else if (CommonUtils.isEqualIgnoreCase(creditTerms, AccountingConstants.NET_7_DAYS)) {
            creditTermDays = 7;
        } else if (CommonUtils.isEqualIgnoreCase(creditTerms, AccountingConstants.NET_15_DAYS)) {
            creditTermDays = 15;
        } else if (CommonUtils.isEqualIgnoreCase(creditTerms, AccountingConstants.NET_30_DAYS)) {
            creditTermDays = 30;
        } else if (CommonUtils.isEqualIgnoreCase(creditTerms, AccountingConstants.NET_45_DAYS)) {
            creditTermDays = 45;
        } else if (CommonUtils.isEqualIgnoreCase(creditTerms, AccountingConstants.NET_60_DAYS)) {
            creditTermDays = 60;
        }
        return creditTermDays;
    }

    public String calculateDueDate(String accountNumber, String invoiceDate) throws Exception {
        Vendor vendor = new TradingPartnerDAO().getVendorByCustomerNumber(accountNumber);
        String dueDate = "";
        if (null != vendor && CommonUtils.isNotEmpty(invoiceDate)) {
            Date convertedDate = DateUtils.parseDate(invoiceDate, "MM/dd/yyyy");
            int daysToAdd = 0;
            if (null != vendor.getCterms()) {
                if (CommonUtils.isEqualIgnoreCase(vendor.getCterms().getCodedesc(), AccountingConstants.DUE_UPON_RECEIPT)) {
                    daysToAdd = 0;
                } else if (CommonUtils.isEqualIgnoreCase(vendor.getCterms().getCodedesc(), AccountingConstants.NET_7_DAYS)) {
                    daysToAdd = 7;
                } else if (CommonUtils.isEqualIgnoreCase(vendor.getCterms().getCodedesc(), AccountingConstants.NET_15_DAYS)) {
                    daysToAdd = 15;
                } else if (CommonUtils.isEqualIgnoreCase(vendor.getCterms().getCodedesc(), AccountingConstants.NET_30_DAYS)) {
                    daysToAdd = 30;
                } else if (CommonUtils.isEqualIgnoreCase(vendor.getCterms().getCodedesc(), AccountingConstants.NET_45_DAYS)) {
                    daysToAdd = 45;
                } else if (CommonUtils.isEqualIgnoreCase(vendor.getCterms().getCodedesc(), AccountingConstants.NET_60_DAYS)) {
                    daysToAdd = 60;
                }
            }
            if (daysToAdd >= 0) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(convertedDate);
                calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
                dueDate = DateUtils.formatDate(calendar.getTime(), "MM/dd/yyyy");
            }
        }
        return dueDate;
    }

    /**
     * Getting Aging Transactions for AR
     *
     * @param agingReportForm
     * @return List of AR Aging Transactions
     */
    public List<AccountingBean> getArAgingTransactions(AgingReportForm agingReportForm) throws Exception {
        List<AccountingBean> arAgingTransactions = new ArrayList<AccountingBean>();
        if (CommonUtils.isEqualIgnoreCase(ReportConstants.REPORT_TYPE_SUMMARY, agingReportForm.getReport())) {
            arAgingTransactions = getArAgingTransactionsInSummary(agingReportForm);
        } else {
            arAgingTransactions = getArAgingTransactionsInDetail(agingReportForm);
        }
        return arAgingTransactions;
    }

    private List<AccountingBean> getArAgingTransactionsInSummary(AgingReportForm agingReportForm) throws Exception {
        List<AccountingBean> arAgingTransactions = new ArrayList<AccountingBean>();
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(agingReportForm.getDateRangeTo(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String paymentDate = cutOffDate;
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getNoPaymentDate(), ON)) {
            paymentDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        }
        StringBuilder query = new StringBuilder();
        query.append("select out3.acct_no,out3.acct_name,user.login_name,gen.codedesc,ca.credit_limit,");
        query.append("out3.0_30days,out3.31_60days,out3.61_90days,out3.91days,out3.total,out3.eci_acct_no");
        query.append(" from (");
        query.append("select out2.acct_no,out2.acct_name,sum(out2.0_30days) as 0_30days,sum(out2.31_60days) as 31_60days,");
        query.append("sum(out2.61_90days) 61_90days,sum(out2.91days) 91days,sum(out2.total) as total,out2.eci_acct_no");
        query.append(" from (");
        query.append("select out1.acct_no,out1.acct_name,");
        query.append("sum(if(datediff('").append(cutOffDate).append("',out1.invoice_date)<=30,out1.amount,0)) as 0_30days,");
        query.append("sum(if(datediff('").append(cutOffDate).append("',out1.invoice_date)>=31");
        query.append(" and datediff('").append(cutOffDate).append("',out1.invoice_date)<=60,out1.amount,0)) as 31_60days,");
        query.append("sum(if(datediff('").append(cutOffDate).append("',out1.invoice_date)>=61");
        query.append(" and datediff('").append(cutOffDate).append("',out1.invoice_date)<=90,out1.amount,0)) as 61_90days,");
        query.append("sum(if(datediff('").append(cutOffDate).append("',out1.invoice_date)>=91,out1.amount,0)) as 91days,out1.amount as total,");
        query.append(" out1.eci_acct_no from (");
        query.append("select tp.acct_no,tp.acct_name,if(ath.bl_number!='',ath.bl_number,ath.invoice_number) as invoice_or_bl,");
        query.append("sum(ath.transaction_amount+ath.adjustment_amount) as amount,");
        query.append("if(ath.transaction_type like '%BL',ath.invoice_date,min(ath.invoice_date)) as invoice_date,tp.eci_acct_no");
        query.append(" from ar_transaction_history ath join trading_partner tp on tp.acct_no=ath.customer_number");
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAllCustomersCheck(), ON)) {
            if (CommonUtils.isNotEmpty(agingReportForm.getCustomerRangeFrom())) {
                if (CommonUtils.isNotEmpty(agingReportForm.getCustomerRangeTo())) {
                    query.append(" and (tp.acct_name rlike '^[").append(agingReportForm.getCustomerRangeFrom());
                    query.append("-").append(agingReportForm.getCustomerRangeTo()).append("]')");
                } else {
                    query.append(" and tp.acct_name like '").append(agingReportForm.getCustomerRangeFrom()).append("%'");
                }
            }
        } else if (CommonUtils.isNotEmpty(agingReportForm.getCustomerNumber())) {
            query.append(" and tp.acct_no='").append(agingReportForm.getCustomerNumber().trim()).append("'");
        }
        query.append(" where ((ath.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        query.append(" and date_format(ath.posted_date,'%Y-%m-%d')<='").append(cutOffDate).append("')");
        query.append(" or (ath.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        query.append(" and date_format(ath.posted_date,'%Y-%m-%d')<='").append(paymentDate).append("'))");
        query.append(" group by tp.acct_name,tp.acct_no,invoice_or_bl");
        query.append(" order by tp.acct_name,tp.acct_no,ath.invoice_date asc,invoice_or_bl");
        query.append(" ) as out1 where out1.amount!=0");
        query.append(" group by out1.acct_name,out1.acct_no,out1.invoice_or_bl");
        query.append(" order by out1.acct_name,out1.acct_no,out1.invoice_date asc,out1.invoice_or_bl");
        query.append(" )  as out2");
        query.append(" where out2.total!=0");
        query.append(" group by out2.acct_name,out2.acct_no");
        query.append(" order by out2.acct_name,out2.acct_no");
        query.append(" ) as out3");
        if ((CommonUtils.isNotEqual(agingReportForm.getCollector(), "0"))
                || (CommonUtils.isEqual(agingReportForm.getAllCollectors(), ON))) {
            query.append(" join cust_accounting ca on ca.acct_no=out3.acct_no");
            if (CommonUtils.isNotEqual(agingReportForm.getCollector(), "0")) {
                query.append(" and ca.ar_contact_code = ").append(agingReportForm.getCollector());
            } else {
                query.append(" and ca.ar_contact_code is not null");
            }
        } else {
            query.append(" left join cust_accounting ca on out3.acct_no=ca.acct_no");
        }
        query.append(" left join genericcode_dup gen on gen.id=ca.credit_status");
        query.append(" left join user_details user on user.user_id=ca.ar_contact_code");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean arTransaction = new AccountingBean();
            arTransaction.setCustomerNumber((String) col[0]);
            arTransaction.setCustomerName((String) col[1]);
            arTransaction.setCollector((String) col[2]);
            arTransaction.setCreditStatus((String) col[3]);
            arTransaction.setCreditStatus((String) col[3]);
            arTransaction.setCreditLimit(null != col[4] ? Double.parseDouble(col[4].toString()) : 0d);
            arTransaction.setAge0_30Balance(null != col[5] ? Double.parseDouble(col[5].toString()) : 0d);
            arTransaction.setAge31_60Balance(null != col[6] ? Double.parseDouble(col[6].toString()) : 0d);
            arTransaction.setAge61_90Balance(null != col[7] ? Double.parseDouble(col[7].toString()) : 0d);
            arTransaction.setAge91Balance(null != col[8] ? Double.parseDouble(col[8].toString()) : 0d);
            arTransaction.setTotal(null != col[9] ? Double.parseDouble(col[9].toString()) : 0d);
            arTransaction.setBlueScreenAccount((String) col[10]);
            arAgingTransactions.add(arTransaction);
        }
        return arAgingTransactions;
    }

    private List<AccountingBean> getArAgingTransactionsInDetail(AgingReportForm agingReportForm) throws Exception {
        List<AccountingBean> arAgingTransactions = new ArrayList<AccountingBean>();
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(agingReportForm.getDateRangeTo(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String paymentDate = cutOffDate;
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getNoPaymentDate(), ON)) {
            paymentDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        }
        StringBuilder queryBuilder = new StringBuilder("select t.acct_no,t.acct_name,t.bl_number,t.invoice_number,");
        queryBuilder.append("t.invoice_date,t.customer_reference_number,t.voyage_number,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)<=30,t.amount,0)) as 0_30days,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=31");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',t.invoice_date)<=60,t.amount,0)) as 31_60days,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=61");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',t.invoice_date)<=90,t.amount,0)) as 61_90days,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=91,t.amount,0)) as 91days,t.amount,");
        queryBuilder.append(" t.eci_acct_no from (select tp.acct_no,tp.acct_name,tp.eci_acct_no,ath.bl_number,ath.invoice_number,");
        queryBuilder.append("ath.customer_reference_number,ath.voyage_number,sum(ath.transaction_amount+ath.adjustment_amount) as amount,");
        queryBuilder.append("if(ath.transaction_type like '%BL' or ath.transaction_type='',ath.invoice_date,min(ath.invoice_date)) as invoice_date");
        queryBuilder.append(" from ar_transaction_history ath join trading_partner tp on tp.acct_no=ath.customer_number");
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAllCustomersCheck(), ON)) {
            if (CommonUtils.isNotEmpty(agingReportForm.getCustomerRangeFrom())) {
                if (CommonUtils.isNotEmpty(agingReportForm.getCustomerRangeTo())) {
                    queryBuilder.append(" and (tp.acct_name rlike '^[").append(agingReportForm.getCustomerRangeFrom());
                    queryBuilder.append("-").append(agingReportForm.getCustomerRangeTo()).append("]')");
                } else {
                    queryBuilder.append(" and tp.acct_name like '").append(agingReportForm.getCustomerRangeFrom()).append("%'");
                }
            }
        } else if (CommonUtils.isNotEmpty(agingReportForm.getCustomerNumber())) {
            queryBuilder.append(" and tp.acct_no='").append(agingReportForm.getCustomerNumber().trim()).append("'");
        }
        if ((CommonUtils.isNotEqual(agingReportForm.getCollector(), "0"))
                || (CommonUtils.isEqual(agingReportForm.getAllCollectors(), ON))) {
            queryBuilder.append(" join cust_accounting ca on ca.acct_no=tp.acct_no");
            if (CommonUtils.isNotEqual(agingReportForm.getCollector(), "0")) {
                queryBuilder.append(" and ca.ar_contact_code = ").append(agingReportForm.getCollector());
            } else {
                queryBuilder.append(" and ca.ar_contact_code is not null");
            }
        }
        queryBuilder.append(" where ((ath.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and date_format(ath.posted_date,'%Y-%m-%d')<='").append(cutOffDate).append("')");
        queryBuilder.append(" or (ath.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and date_format(ath.posted_date,'%Y-%m-%d')<='").append(paymentDate).append("'))");
        queryBuilder.append(" group by tp.acct_name,tp.acct_no,if(ath.bl_number!='',ath.bl_number,ath.invoice_number)");
        queryBuilder.append(" order by tp.acct_name,tp.acct_no,ath.invoice_date asc,if(ath.bl_number!='',ath.bl_number,ath.invoice_number))");
        queryBuilder.append(" as t where t.amount!=0");
        queryBuilder.append(" group by t.acct_name,t.acct_no,if(t.bl_number!='',t.bl_number,t.invoice_number)");
        queryBuilder.append(" order by t.acct_name,t.acct_no,t.invoice_date asc,if(t.bl_number!='',t.bl_number,t.invoice_number)");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean arTransaction = new AccountingBean();
            arTransaction.setCustomerNumber((String) col[0]);
            arTransaction.setCustomerName((String) col[1]);
            arTransaction.setBillOfLadding((String) col[2]);
            arTransaction.setInvoiceNumber((String) col[3]);
            arTransaction.setTransactionDate(null != col[4] ? DateUtils.parseDate(col[4].toString(), "yyyy-MM-dd") : null);
            arTransaction.setCustomerReference((String) col[5]);
            arTransaction.setVoyage((String) col[6]);
            arTransaction.setAge0_30Balance(null != col[7] ? Double.parseDouble(col[7].toString()) : 0d);
            arTransaction.setAge31_60Balance(null != col[8] ? Double.parseDouble(col[8].toString()) : 0d);
            arTransaction.setAge61_90Balance(null != col[9] ? Double.parseDouble(col[9].toString()) : 0d);
            arTransaction.setAge91Balance(null != col[10] ? Double.parseDouble(col[10].toString()) : 0d);
            arTransaction.setTotal(null != col[11] ? Double.parseDouble(col[11].toString()) : 0d);
            arTransaction.setBlueScreenAccount((String) col[12]);
            arAgingTransactions.add(arTransaction);
        }
        return arAgingTransactions;
    }

    public List<ReportBean> getArDsoList(DsoReportForm dsoReportForm) throws Exception {
        FiscalPeriod startPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(dsoReportForm.getFromPeriodId()));
        FiscalPeriod endPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(dsoReportForm.getToPeriodId()));
        String fromDate = "";
        String toDate = "";
        if (null != startPeriod && null != endPeriod) {
            Date startDate = startPeriod.getStartDate();
            Date endDate = endPeriod.getEndDate();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            fromDate = dateFormat.format(cal.getTime());
            cal.setTime(endDate);
            cal.add(Calendar.HOUR, 23);
            cal.add(Calendar.MINUTE, 59);
            cal.add(Calendar.SECOND, 59);
            toDate = dateFormat.format(cal.getTime());
        }
        List<String> vendorsForCollector = new ArrayList<String>();
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(BY_COLLECTOR)) {
            StringBuilder queryStr = new StringBuilder();
            queryStr.append("select concat('\"',acct_no,'\"') from cust_accounting where ar_contact_code=").append(dsoReportForm.getUserId()).append("");
            Query query = getCurrentSession().createSQLQuery(queryStr.toString());
            vendorsForCollector = query.list();
        }
        List<ReportBean> arDsoList = new ArrayList<ReportBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(open_receivables,2) as open_receivables,format(total_billed_amount,2) as total_billed_amount,");
        queryBuilder.append(" no_of_days,round((open_receivables/total_billed_amount)*no_of_days,2) as dso_ratio from");
        queryBuilder.append(" (select sum(t.open_receivables) as open_receivables,sum(t.total_billed_amount) as total_billed_amount,");
        queryBuilder.append("datediff('").append(toDate).append("','").append(fromDate).append("')+1 as no_of_days from");
        queryBuilder.append(" ((select sum(tr.balance) as open_receivables,0 as total_billed_amount from transaction tr");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("' and");
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(ALL_ACCOUNTS_RECEIVABLE)) {
            queryBuilder.append(" tr.cust_no!=''");
        }
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(BY_COLLECTOR)) {
            if (CommonUtils.isNotEmpty(vendorsForCollector)) {
                queryBuilder.append(" tr.cust_no in ").append(vendorsForCollector.toString().replace("[", "(").replace("]", ")"));
            } else {
                queryBuilder.append(" tr.transaction_type='AP'");
            }
        }
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(BY_CUSTOMER)) {
            queryBuilder.append(" tr.cust_no='").append(dsoReportForm.getVendorNumber()).append("'");
        }
        queryBuilder.append(" and tr.transaction_date");
        queryBuilder.append(" between '").append(fromDate).append("' and '").append(toDate).append("') union");
        queryBuilder.append(" (select 0 as open_receivables, sum(tr.balance+p.payment_amt+p.adjustment_amt) as total_billed_amount from ");
        queryBuilder.append(" transaction tr join payments p on p.transaction_id = tr.transaction_id");
        queryBuilder.append(" and p.transaction_type =  'AR' and p.payment_type='P' join ar_batch b on b.batch_id=p.batch_id");
        queryBuilder.append(" and b.status='").append(STATUS_CLOSED).append("' where ");
        queryBuilder.append(" tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("' and");
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(ALL_ACCOUNTS_RECEIVABLE)) {
            queryBuilder.append(" tr.cust_no!=''");
        }
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(BY_COLLECTOR)) {
            if (CommonUtils.isNotEmpty(vendorsForCollector)) {
                queryBuilder.append(" tr.cust_no in ").append(vendorsForCollector.toString().replace("[", "(").replace("]", ")"));
            } else {
                queryBuilder.append(" tr.transaction_type='AP'");
            }
        }
        if (dsoReportForm.getSearchDsoBy().equalsIgnoreCase(BY_CUSTOMER)) {
            queryBuilder.append(" tr.cust_no='").append(dsoReportForm.getVendorNumber()).append("'");
        }
        queryBuilder.append(" and tr.transaction_date");
        queryBuilder.append(" between '").append(fromDate).append("' and '").append(toDate).append("')) as t) as t ");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        List<Object> result = query.list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ReportBean reportBean = new ReportBean();
            reportBean.setOpenReceivables((String) col[0]);
            reportBean.setTotalAmount((String) col[1]);
            reportBean.setNumberOfDays(null != col[2] ? col[2].toString() : "");
            reportBean.setDsoRatio(null != col[3] ? col[3].toString() : "");
            arDsoList.add(reportBean);
        }
        return arDsoList;
    }

    public String buildArCreditHoldQuery(ArCreditHoldForm arCreditHoldForm, boolean isOpsLevelUser) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from CustomerOnHoldNoCreditOverLimit_View c");
        queryBuilder.append(" where (c.no_credit='Y' or c.past_due='on' or c.over_limit='on')");
        if (CommonUtils.isNotEmpty(arCreditHoldForm.getCustomerNumber())) {
            queryBuilder.append(" and c.acct_no='").append(arCreditHoldForm.getCustomerNumber().trim()).append("'");
        }
        if (CommonUtils.isNotEmpty(arCreditHoldForm.getBillOfLadding())) {
            queryBuilder.append(" and c.bill_of_ladding like '%").append(arCreditHoldForm.getBillOfLadding()).append("%'");
        }
        if (CommonUtils.isNotEmpty(arCreditHoldForm.getSalesPerson())) {
            queryBuilder.append(" and c.sales_person='").append(arCreditHoldForm.getSalesPerson().trim()).append("'");
        }
        if (CommonUtils.isNotEmpty(arCreditHoldForm.getBillingTerminal())) {
            queryBuilder.append(" and (c.billing_terminal='").append(arCreditHoldForm.getBillingTerminal().trim()).append("'");
            queryBuilder.append(" or substr(c.bill_of_ladding,1,2) = '").append(arCreditHoldForm.getBillingTerminal().trim()).append("')");
        }
        if (CommonUtils.isNotEmpty(arCreditHoldForm.getDestination())) {
            queryBuilder.append(" and (c.destination='").append(arCreditHoldForm.getDestination().trim()).append("'");
            queryBuilder.append(" or substr(c.bill_of_ladding,3,3)='").append(arCreditHoldForm.getDestination().trim()).append("')");
        }
        if (CommonUtils.isNotEmpty(arCreditHoldForm.getCollectorName())) {
            queryBuilder.append(" and c.collector='").append(arCreditHoldForm.getCollectorName()).append("'");
        }
        if (isOpsLevelUser) {
            queryBuilder.append(" and c.credit_hold_by_accounting=1");
        }
        return queryBuilder.toString();
    }

    public Integer getNoOfArCreditHoldTransactions(String conditions) throws Exception {
        getCurrentSession().flush();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(c.transaction_id)");
        queryBuilder.append(conditions);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Integer.parseInt(result.toString()) : 0;
    }

    public List<AccountingBean> getArCreditHoldTransactions(String conditions, int currentPageSize, int pageNo, String sortBy, String orderBy) throws Exception {
        getCurrentSession().flush();
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        int start = (currentPageSize * (pageNo - 1));
        int end = currentPageSize;
        StringBuilder query = new StringBuilder();
        query.append("select c.acct_type,c.acct_name,c.acct_no,c.collector,format(c.credit_limit,2) as credit_limit,");
        query.append("c.credit_terms_desc,c.bl_terms,c.bill_to,c.bill_of_ladding,date_format(c.invoice_date,'%m/%d/%Y') as invoice_date,");
        query.append("format(c.invoice_amount,2) as invoice_amount,c.age,format(c.invoice_balance,2) as invoice_balance,");
        query.append("date_format(c.arrival_date,'%m/%d/%Y') as arrival_date,c.credit_hold,c.credit_status,");
        query.append("c.no_credit,c.past_due,c.over_limit,c.transaction_id,c.saved_in_batch,c.overhead,c.correction_notice,c.manifest_flag,");
        query.append("bl_created_by,if(count(doc.document_id)>0,'true','false') as has_documents from (");
        query.append(" select c.acct_type,c.acct_name,c.acct_no,c.collector,c.credit_limit,c.credit_terms_value,");
        query.append("c.credit_terms_desc,c.bl_terms,c.bill_to,c.bill_of_ladding,c.invoice_date,");
        query.append("c.invoice_amount,c.age,c.invoice_balance,c.arrival_date,c.credit_hold,c.credit_status,");
        query.append("c.no_credit,c.past_due,c.over_limit,c.transaction_id,c.saved_in_batch,c.overhead,c.correction_notice,c.manifest_flag,");
        query.append("c.bl_created_by");
        query.append(conditions);
        query.append(" order by ").append(sortBy).append(" ").append(orderBy);
        query.append(" limit ").append(start).append(",").append(end);
        query.append(" ) as c left join document_store_log doc on (doc.screen_name='INVOICE' and doc.document_name='INVOICE'");
        query.append(" and doc.document_id=concat(c.acct_no,'-',c.bill_of_ladding))");
        query.append(" group by c.acct_no,c.bill_of_ladding,c.transaction_id");
        query.append(" order by ").append(sortBy).append(" ").append(orderBy);
        List result = getSession().createSQLQuery(query.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setCustomerType((String) col[0]);
            transaction.setCustomerName((String) col[1]);
            transaction.setCustomerNumber((String) col[2]);
            transaction.setCollector((String) col[3]);
            transaction.setFormattedCredit((String) col[4]);
            transaction.setCreditTerms((String) col[5]);
            transaction.setBlTerms((String) col[6]);
            transaction.setBillTo((String) col[7]);
            transaction.setBillOfLadding((String) col[8]);
            transaction.setFormattedDate((String) col[9]);
            transaction.setFormattedAmount((String) col[10]);
            transaction.setAge(Integer.parseInt(col[11].toString()));
            transaction.setFormattedBalance((String) col[12]);
            transaction.setFormattedArrivalDate((String) col[13]);
            transaction.setCreditHold((String) col[14]);
            transaction.setCreditStatus((String) col[15]);
            transaction.setNoCredit((String) col[16]);
            transaction.setPastDue((String) col[17]);
            transaction.setOverLimit((String) col[18]);
            transaction.setTransactionId(col[19].toString());
            transaction.setSavedInBatches((String) col[20]);
            transaction.setSubType((String) col[21]);
            transaction.setCorrectionNotice((String) col[22]);
            transaction.setManifestFlag((String) col[23]);
            transaction.setBlCreatedBy((String) col[24]);
            transaction.setHasDocuments(Boolean.valueOf((String) col[25]));
            transactions.add(transaction);
        }
        return transactions;
    }

    public String buildCheckRegisterQuery(CheckRegisterForm checkRegisterForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("from transaction where Transaction_type = '").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        if (CommonUtils.isNotEmpty(checkRegisterForm.getBankAccountNo())) {
            queryBuilder.append(" and bank_account_no like '%").append(checkRegisterForm.getBankAccountNo()).append("%'");
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getGlAccountNo())) {
            queryBuilder.append(" and GL_account_number like '%").append(checkRegisterForm.getGlAccountNo().trim()).append("%'");
            if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getShowStatus(), YES)) {
                queryBuilder.append(" and (cleared = '").append(YES).append("' and Status='").append(CLEAR).append("')");
            } else if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getShowStatus(), NO)) {
                queryBuilder.append(" and (cleared = '").append(NO).append("' and Status='").append(STATUS_RECONCILE_IN_PROGRESS).append("')");
            } else if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getShowStatus(), STATUS_VOID)) {
                queryBuilder.append(" and void = '").append(YES).append("' and (Status='").append(STATUS_PAID).append("' or Status='").append(STATUS_SENT).append("' or Status='").append(STATUS_READY_TO_SEND).append("')");
            } else {
                queryBuilder.append(" and ((cleared = '").append(YES).append("' and Status='").append(CLEAR).append("')");
                queryBuilder.append(" or (cleared = '").append(NO).append("' and Status='").append(STATUS_RECONCILE_IN_PROGRESS).append("')");
                queryBuilder.append(" or (void = '").append(YES).append("' and (Status='").append(STATUS_PAID).append("' or Status='").append(STATUS_SENT).append("' or Status='").append(STATUS_READY_TO_SEND).append("'))");
                queryBuilder.append(" or (Status='").append(STATUS_PAID).append("' or Status='").append(STATUS_SENT).append("' or Status='").append(STATUS_READY_TO_SEND).append("'))");
            }
            if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getPayMethod(), PAYMENT_METHOD_CHECK)) {
                queryBuilder.append(" and pay_method = '").append(PAYMENT_METHOD_CHECK).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getPayMethod(), PAYMENT_METHOD_ACH)) {
                queryBuilder.append(" and pay_method = '").append(PAYMENT_METHOD_ACH).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getPayMethod(), PAYMENT_METHOD_WIRE)) {
                queryBuilder.append(" and pay_method = '").append(PAYMENT_METHOD_WIRE).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getPayMethod(), PAYMENT_METHOD_ACH_DEBIT)) {
                queryBuilder.append(" and pay_method = '").append(PAYMENT_METHOD_ACH_DEBIT).append("'");
            } else {
                queryBuilder.append(" and (pay_method = '").append(PAYMENT_METHOD_CHECK).append("'");
                queryBuilder.append(" or pay_method = '").append(PAYMENT_METHOD_ACH).append("'");
                queryBuilder.append(" or pay_method = '").append(PAYMENT_METHOD_WIRE).append("'");
                queryBuilder.append(" or pay_method = '").append(PAYMENT_METHOD_ACH_DEBIT).append("'");
                queryBuilder.append(" or pay_method = '").append(PAYMENT_METHOD_CREDIT_CARD).append("')");
            }
        } else {
            queryBuilder.append(" and GL_account_number is not null ");
            queryBuilder.append(" and ((cleared = '").append(YES).append("' and Status='").append(CLEAR).append("')");
            queryBuilder.append(" or (cleared = '").append(NO).append("' and Status='").append(STATUS_RECONCILE_IN_PROGRESS).append("')");
            queryBuilder.append(" or (void = '").append(YES).append("' and Status='").append(STATUS_PAID).append("')");
            queryBuilder.append(" or (Status='").append(STATUS_PAID).append("' or Status='").append(STATUS_SENT).append("' or Status='").append(STATUS_READY_TO_SEND).append("'))");
        }
        boolean addOr = false;
        boolean hasStartCheck = false;
        if (CommonUtils.isNotEmpty(checkRegisterForm.getStartCheckNumber()) || CommonUtils.isNotEmpty(checkRegisterForm.getEndCheckNumber())) {
            queryBuilder.append(" and ((");
            addOr = true;
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getStartCheckNumber())) {
            queryBuilder.append(" cast(Cheque_number as signed integer) >= '").append(checkRegisterForm.getStartCheckNumber()).append("'");
            hasStartCheck = true;
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getEndCheckNumber())) {
            if (hasStartCheck) {
                queryBuilder.append(" and");
            }
            queryBuilder.append(" cast(Cheque_number as signed integer) <= '").append(checkRegisterForm.getEndCheckNumber()).append("'");
        }
        if (addOr) {
            queryBuilder.append(") or (");
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getStartCheckNumber())) {
            queryBuilder.append(" ach_batch_sequence >= '").append(checkRegisterForm.getStartCheckNumber()).append("'");
        }

        if (CommonUtils.isNotEmpty(checkRegisterForm.getEndCheckNumber())) {
            if (hasStartCheck) {
                queryBuilder.append(" and");
            }
            queryBuilder.append(" ach_batch_sequence <= '").append(checkRegisterForm.getEndCheckNumber()).append("'");
        }

        if (addOr) {
            queryBuilder.append("))");
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getBatchNumber())) {
            queryBuilder.append(" and Ap_Batch_Id like '%").append(checkRegisterForm.getBatchNumber()).append("%'");
        } else {
            queryBuilder.append(" and Ap_Batch_Id is not null");
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getVendorNumber())) {
            queryBuilder.append(" and Cust_no = '").append(checkRegisterForm.getVendorNumber()).append("'");
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getInvoiceAmount())) {
            queryBuilder.append(" and Transaction_amt ").append(checkRegisterForm.getInvoiceOperator()).append(" ").append(checkRegisterForm.getInvoiceAmount()).append("");
        }
        if (CommonUtils.isNotEmpty(checkRegisterForm.getBankReconcileDate())) {
            queryBuilder.append(" and reconciled_date = '").append(checkRegisterForm.getBankReconcileDate()).append("'");
        }
        return queryBuilder.toString();
    }

    public Integer getTotalCheckRegisters(String queryConditions) throws Exception {
        Integer totalSize = 0;
        StringBuilder queryBuilder = new StringBuilder(" select count(Transaction_Id) ");
        queryBuilder.append(queryConditions);
        queryBuilder.append(" and Balance=0 and Balance_In_Process=0");
        queryBuilder.append(" group by cast(Cheque_number as signed integer),Ap_Batch_Id,Cust_no");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (null != result) {
            totalSize = result.size();
        }
        return totalSize;
    }

    public List<TransactionBean> getCheckRegisters(CheckRegisterForm checkRegisterForm, String queryConditions) throws Exception {
        List<TransactionBean> checkRegistgers = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select cast(group_concat(Transaction_Id)as char) as transactionIds, Cust_no, Cust_name,");
        queryBuilder.append("Cheque_number,check_date,sum(Transaction_amt),Transaction_date,bank_account_no,cleared,cleared_date,confirmation_number,");
        queryBuilder.append("void,reprint,Ap_Batch_Id,pay_method,GL_account_number,ach_batch_sequence,Invoice_number,Status ");
        queryBuilder.append(queryConditions);
        if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "gotoPage")
                || CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "doSort")) {
            String transactionIds = "";
            if (CommonUtils.isNotEmpty(checkRegisterForm.getVoidIds())) {
                transactionIds = transactionIds + "," + StringUtils.removeStart(StringUtils.removeEnd(checkRegisterForm.getVoidIds(), ":-"), ":-");
            }
            if (CommonUtils.isNotEmpty(checkRegisterForm.getReprintIds())) {
                transactionIds = transactionIds + "," + StringUtils.removeStart(StringUtils.removeEnd(checkRegisterForm.getReprintIds(), ":-"), ":-");
            }
            if (CommonUtils.isNotEmpty(transactionIds)) {
                queryBuilder.append(" and transaction_id not in (").append(StringUtils.removeStart(StringUtils.removeEnd(StringUtils.replace(transactionIds, ":-", ","), ","), ",")).append(")");
            }
        }
        queryBuilder.append(" and Balance=0 and Balance_In_Process=0");
        if (CommonUtils.isNotEqual(checkRegisterForm.getButtonValue(), "print")
                || CommonUtils.isNotEqual(checkRegisterForm.getButtonValue(), "exportToExcel")) {
            queryBuilder.append(" group by cast(Cheque_number as signed integer),Ap_Batch_Id,Cust_no");
        }
        if ((CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "gotoPage")
                || CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "doSort"))
                && CommonUtils.isNotEmpty(checkRegisterForm.getSortBy()) && CommonUtils.isNotEmpty(checkRegisterForm.getSortOrder())) {
            queryBuilder.append(" order by ").append(checkRegisterForm.getSortBy()).append(" ").append(checkRegisterForm.getSortOrder());
        } else {
            queryBuilder.append(" order by Transaction_date desc,cast(Cheque_number as signed integer) desc");
        }
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "doSearch")
                || CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "gotoPage")
                || CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "doSort")) {
            int start = (checkRegisterForm.getCurrentPageSize() * (checkRegisterForm.getPageNo() - 1));
            int end = checkRegisterForm.getCurrentPageSize();
            query.setFirstResult(start).setMaxResults(end);
        }
        List<Object> queryResult = query.list();
        for (Object row : queryResult) {
            Object[] col = (Object[]) row;
            TransactionBean checkRegister = new TransactionBean();
            checkRegister.setTransactionId((String) col[0]);
            checkRegister.setCustomerNo((String) col[1]);
            checkRegister.setCustomer((String) col[2]);
            checkRegister.setChequenumber(null != col[3] ? col[3].toString() : "");
            checkRegister.setCheckDate(null != col[4] ? col[4].toString() : "");
            checkRegister.setAmount(null != col[5] ? NumberUtils.formatNumber(Double.parseDouble(col[5].toString()), "###,###,##0.00") : "");
            checkRegister.setTransDate(null != col[6] ? DateUtils.formatDate(DateUtils.parseDate(col[6].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            checkRegister.setBankAccountNumber(null != col[7] ? col[7].toString() : "");
            checkRegister.setCleared(null != col[8] ? col[8].toString() : "");
            checkRegister.setClearedDate(null != col[9] ? DateUtils.formatDate(DateUtils.parseDate(col[9].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            checkRegister.setConfirmationNumber(null != col[10] ? col[10].toString() : "");
            checkRegister.setVoidTransaction(null != col[11] ? col[11].toString() : "");
            checkRegister.setReprint(null != col[12] ? col[12].toString() : "");
            checkRegister.setApBatchId(null != col[13] ? Integer.parseInt(col[13].toString()) : null);
            checkRegister.setPaymentMethod(null != col[14] ? col[14].toString() : "");
            checkRegister.setGlAcctNo(null != col[15] ? col[15].toString() : "");
            if (CommonUtils.isEqualIgnoreCase(checkRegister.getPaymentMethod(), PAYMENT_METHOD_ACH)) {
                String achBatchSequence = null != col[16] && !col[16].toString().trim().equals("") ? col[16].toString() : "0";
                if (CommonUtils.isNotEqual(achBatchSequence, "0")) {
                    checkRegister.setChequenumber(StringUtils.leftPad(achBatchSequence, 7, "0"));
                }
            }
            checkRegister.setInvoiceOrBl(null != col[17] ? col[17].toString() : "");
            checkRegister.setStatus((String) col[18]);
            checkRegistgers.add(checkRegister);
        }
        return checkRegistgers;
    }

    public List<TransactionBean> getOpenChecksForReconcile(ReconcileForm reconcileForm) throws Exception {
        String date = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getBankReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        List<TransactionBean> reconcileList = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select tbl.transactionIds,tbl.pay_method,");
        queryBuilder.append("tbl.reference,tbl.batchId,tbl.credit,tbl.debit,tbl.date,status from (");
        queryBuilder.append("(select cast(group_concat(tr.Transaction_Id)as char) as transactionIds,");
        queryBuilder.append("tr.pay_method,tr.Cheque_number AS reference,tr.Ap_Batch_Id as batchId,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)>0,sum(tr.Transaction_amt),0),2) as credit,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)<0,-sum(tr.Transaction_amt),0),2) as debit,");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y') as date,");
        queryBuilder.append("if(tr.reconciled='").append(STATUS_IN_PROGRESS).append("','");
        queryBuilder.append(STATUS_RECONCILE_IN_PROGRESS).append("','").append(STATUS_PAID).append("') as status,'voidednotAch' as paymentType");
        queryBuilder.append(" from transaction tr where tr.GL_account_number='").append(reconcileForm.getGlAccountNumber()).append("'");
        queryBuilder.append(" and tr.bank_account_no='").append(reconcileForm.getBankAccount()).append("'");
        queryBuilder.append(" and date_format(tr.Transaction_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.pay_method!='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.Status='").append(STATUS_PAID).append("' or tr.reconciled='").append(STATUS_IN_PROGRESS).append("')");
        queryBuilder.append(" and tr.cleared='").append(NO).append("' and tr.void='").append(YES).append("'");
        queryBuilder.append(" and date_format(tr.void_date,'%Y-%m-%d')>'").append(date).append("'");
        queryBuilder.append(" group by cast(tr.Cheque_number as signed integer),tr.Ap_Batch_Id,tr.cust_no");
        queryBuilder.append(" order by tr.Ap_Batch_Id,cast(tr.Cheque_number as signed integer),tr.cust_no)");
        queryBuilder.append(" union ");
        queryBuilder.append("(select cast(group_concat(tr.Transaction_Id)as char) as transactionIds,");
        queryBuilder.append("tr.pay_method,tr.Cheque_number AS reference,tr.Ap_Batch_Id as batchId,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)>0,sum(tr.Transaction_amt),0),2) as credit,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)<0,-sum(tr.Transaction_amt),0),2) as debit,");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y') as date,");
        queryBuilder.append("if(tr.reconciled='").append(STATUS_IN_PROGRESS).append("','");
        queryBuilder.append(STATUS_RECONCILE_IN_PROGRESS).append("','").append(STATUS_PAID).append("') as status,'notAch' as paymentType");
        queryBuilder.append(" from transaction tr where tr.GL_account_number='").append(reconcileForm.getGlAccountNumber()).append("'");
        queryBuilder.append(" and tr.bank_account_no='").append(reconcileForm.getBankAccount()).append("'");
        queryBuilder.append(" and date_format(tr.Transaction_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.pay_method!='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.Status='").append(STATUS_PAID).append("' or tr.reconciled='").append(STATUS_IN_PROGRESS).append("')");
        queryBuilder.append(" and tr.cleared='").append(NO).append("' and tr.void='").append(NO).append("'");
        queryBuilder.append(" group by cast(tr.Cheque_number as signed integer),tr.Ap_Batch_Id,tr.cust_no");
        queryBuilder.append(" order by tr.Ap_Batch_Id,cast(tr.Cheque_number as signed integer),tr.cust_no)");
        queryBuilder.append(" union ");
        queryBuilder.append(" (select cast(group_concat(tr.Transaction_Id)as char) as transactionIds,");
        queryBuilder.append("tr.pay_method,'' AS reference,CONCAT('ACH - ',tr.ach_batch_sequence) AS batchId,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)>0,sum(tr.Transaction_amt),0),2) as credit,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)<0,-sum(tr.Transaction_amt),0),2) as debit,");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y') as date,");
        queryBuilder.append("if(tr.reconciled='").append(STATUS_IN_PROGRESS).append("','");
        queryBuilder.append(STATUS_RECONCILE_IN_PROGRESS).append("','").append(STATUS_PAID).append("') as status,'Ach' as paymentType");
        queryBuilder.append(" from transaction tr where tr.GL_account_number='").append(reconcileForm.getGlAccountNumber()).append("'");
        queryBuilder.append(" and tr.bank_account_no='").append(reconcileForm.getBankAccount()).append("'");
        queryBuilder.append(" and date_format(tr.Transaction_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.pay_method='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.Status='").append(STATUS_SENT).append("' or tr.Status='").append(STATUS_READY_TO_SEND).append("' or tr.reconciled='").append(STATUS_IN_PROGRESS).append("')");
        queryBuilder.append(" and tr.cleared='").append(NO).append("'");
        queryBuilder.append(" group by tr.ach_batch_sequence");
        queryBuilder.append(" order by tr.Ap_Batch_Id,cast(tr.Cheque_number as signed integer),tr.cust_no)");
        queryBuilder.append(" union ");
        queryBuilder.append(" (select cast(group_concat(tr.Transaction_Id)as char) as transactionIds,");
        queryBuilder.append("tr.pay_method,'' AS reference,CONCAT('ACH - ',tr.ach_batch_sequence) AS batchId,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)<0,-sum(tr.Transaction_amt),0),2) as credit,");
        queryBuilder.append("format(if(sum(tr.Transaction_amt)>0,sum(tr.Transaction_amt),0),2) as debit,");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y') as date,");
        queryBuilder.append("if(tr.reconciled='").append(STATUS_IN_PROGRESS).append("','");
        queryBuilder.append(STATUS_RECONCILE_IN_PROGRESS).append("','").append(STATUS_PAID).append("') as status,'voidedAch' as paymentType");
        queryBuilder.append(" from transaction tr where tr.GL_account_number='").append(reconcileForm.getGlAccountNumber()).append("'");
        queryBuilder.append(" and tr.bank_account_no='").append(reconcileForm.getBankAccount()).append("'");
        queryBuilder.append(" and date_format(tr.Transaction_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.pay_method='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.Status='").append(STATUS_SENT).append("' or tr.Status='").append(STATUS_READY_TO_SEND).append("' or tr.reconciled='").append(STATUS_IN_PROGRESS).append("')");
        queryBuilder.append(" and tr.cleared='").append(NO).append("' and tr.void='").append(YES).append("'");
        queryBuilder.append(" and date_format(tr.void_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" group by tr.ach_batch_sequence");
        queryBuilder.append(" order by tr.Ap_Batch_Id,cast(tr.Cheque_number as signed integer),tr.cust_no)");
        queryBuilder.append(" ) as tbl order by tbl.batchId");
        getCurrentSession().flush();
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).addScalar("tbl.transactionIds", StringType.INSTANCE).addScalar("tbl.pay_method", StringType.INSTANCE).addScalar("tbl.reference", StringType.INSTANCE).addScalar("tbl.batchId", StringType.INSTANCE).addScalar("tbl.credit", StringType.INSTANCE).addScalar("tbl.debit", StringType.INSTANCE).addScalar("tbl.date", StringType.INSTANCE).addScalar("tbl.status", StringType.INSTANCE);
        List<Object> resultList = query.list();
        for (Object row : resultList) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            String transactionId = col[0].toString();
            transactionId = StringUtils.replace(transactionId, ",", "@" + MODULE_TRANSACTION + ",");
            transactionId += "@" + MODULE_TRANSACTION;
            transactionBean.setTransactionId(transactionId);
            transactionBean.setPaymentMethod(null != col[1] ? col[1].toString().toUpperCase() : "");
            transactionBean.setCustomerReference(null != col[2] ? col[2].toString() : "");
            transactionBean.setBatchId(null != col[3] ? col[3].toString() : "");
            transactionBean.setCredit(null != col[4] ? col[4].toString() : "0.00");
            transactionBean.setDebit(null != col[5] ? col[5].toString() : "0.00");
            transactionBean.setTransDate(null != col[6] ? col[6].toString() : "");
            transactionBean.setStatus(null != col[7] ? col[7].toString() : "");
            reconcileList.add(transactionBean);
        }
        return reconcileList;
    }

    public List<Transaction> getArTransactionsWithCreditHold(String custNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.ne("balance", 0d));
        criteria.add(Restrictions.eq("custNo", custNo));
        criteria.add(Restrictions.eq("creditHold", YES));
        return criteria.list();
    }

    public List<ArTransactionBean> getArPaymentsAgainstCredit(String custNumber, String billOfLadding, String invoiceNumber, Integer apBatchId) {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("custNo", custNumber));
        criteria.add(Restrictions.eq("status", STATUS_PAID));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_PAYAMENT));
        criteria.add(Restrictions.eq("voidTransaction", NO));
        criteria.add(Restrictions.eq("apBatchId", apBatchId));
        criteria.add(Restrictions.eq("billLaddingNo", billOfLadding));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.addOrder(Order.asc("transactionDate"));
        List<Transaction> result = criteria.list();
        List<ArTransactionBean> arTransactionHistoryList = new ArrayList<ArTransactionBean>();
        for (Transaction arPayment : result) {
            ArTransactionBean arTransactionBean = new ArTransactionBean();
            arTransactionBean.setTransactionAmount((-1) * arPayment.getTransactionAmt());
            arTransactionBean.setTransactionDate(arPayment.getTransactionDate());
            arTransactionBean.setTransactionType(TRANSACTION_TYPE_PAYAMENT);
            arTransactionBean.setBatchId(arPayment.getApBatchId().toString());
            arTransactionBean.setCheckNo(arPayment.getChequeNumber());
            arTransactionHistoryList.add(arTransactionBean);
        }
        return arTransactionHistoryList;
    }

    public List<AccountingBean> getAccountPayables(String conditions) throws Exception {
        getCurrentSession().flush();
        List<AccountingBean> accountPayables = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder("select tp.acct_name,tp.acct_no,tr.Invoice_number,tr.Bill_Ladding_No,");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y'),gen.codedesc as term,format(tr.Balance,2),datediff(sysdate(),tr.Transaction_date) as age,");
        queryBuilder.append("tr.Cheque_number,date_format(tr.Due_Date,'%m/%d/%Y'),tr.Transaction_type,tr.Status,tr.Transaction_ID,if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType");
        queryBuilder.append(conditions);
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean accountPayable = new AccountingBean();
            accountPayable.setVendorName((String) col[0]);
            accountPayable.setVendorNumber((String) col[1]);
            accountPayable.setInvoiceNumber((null != (String) col[2] ? ((String) col[2]).toUpperCase() : (String) col[2]));
            accountPayable.setBillOfLadding((String) col[3]);
            accountPayable.setFormattedDate((String) col[4]);
            accountPayable.setCreditTerms((String) col[5]);
            accountPayable.setFormattedBalance((String) col[6]);
            accountPayable.setAge(null != col[7] ? Integer.parseInt(col[7].toString()) : 0);
            accountPayable.setCheckNumber((String) col[8]);
            accountPayable.setFormattedDueDate((String) col[9]);
            accountPayable.setTransactionType((String) col[10]);
            accountPayable.setStatus((String) col[11]);
            accountPayable.setCreditHold(CommonUtils.isEqualIgnoreCase(accountPayable.getStatus(), STATUS_HOLD) ? "Yes" : "No");
            accountPayable.setTransactionId(col[12].toString());
            accountPayable.setSubType((String) col[13]);
            accountPayables.add(accountPayable);
        }
        return accountPayables;
    }

    public CustomerBean getCustomerDetailsForAccountPayables(boolean checkVendor, String conditions) throws Exception {
        CustomerBean customerBean = new CustomerBean();
        StringBuilder queryBuilder = new StringBuilder("select max(tp.acct_name),max(tp.acct_no),max(tp.sub_type),max(gen.codedesc) as term,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and datediff(sysdate(),tr.Transaction_date)<=30,tr.Balance,0)),2) as 30days,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and datediff(sysdate(),tr.Transaction_date)>=31 and datediff(sysdate(),tr.Transaction_date)<=60,tr.Balance,0)),2) as 60days,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and datediff(sysdate(),tr.Transaction_date)>=61 and datediff(sysdate(),tr.Transaction_date)<=90,tr.Balance,0)),2) as 90days,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and datediff(sysdate(),tr.Transaction_date)>=91,tr.Balance,0)),2) as 91days,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("',tr.Balance,0)),2) as apamount,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("',-tr.Balance,0)),2) as aramount,");
        queryBuilder.append("format(sum(if(tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("',tr.Balance,-tr.Balance)),2) as netamount");
        queryBuilder.append(conditions);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            Object[] col = (Object[]) result;
            customerBean.setCustomerName(checkVendor ? (String) col[0] : "");
            customerBean.setCustomerNumber(checkVendor ? (String) col[1] : "");
            customerBean.setSubType(checkVendor ? (String) col[2] : "");
            customerBean.setCreditTerm(checkVendor ? (String) col[3] : "");
            customerBean.setCurrent((String) col[4]);
            customerBean.setThirtyOneToSixtyDays((String) col[5]);
            customerBean.setSixtyOneToNintyDays((String) col[6]);
            customerBean.setGreaterThanNintyDays((String) col[7]);
            customerBean.setOutstandingPayables((String) col[8]);
            customerBean.setOutstandingRecievables((String) col[9]);
            customerBean.setTotal((String) col[10]);
        }
        return customerBean;
    }

    public String buildAccountPayablesQuery(AccountPayableForm accountPayableForm, User loginuser) throws Exception {
        StringBuilder queryBuilder = new StringBuilder(" from transaction tr join trading_partner tp on tp.acct_no=tr.cust_no");
        boolean onlyMyAp = false;
        boolean bothTransaction = false;
        if (CommonUtils.isEqualIgnoreCase(accountPayableForm.getOnlyAP(), AccountingConstants.YES)) {
            queryBuilder.append(" join vendor_info ve on (ve.cust_accno=tr.cust_no and ve.ap_specialist=").append(loginuser.getUserId()).append(")");
            onlyMyAp = true;
        } else {
            queryBuilder.append(" left join vendor_info ve on ve.cust_accno=tr.cust_no");
        }
        queryBuilder.append(" left join genericcode_dup gen on gen.id=ve.credit_terms");
        queryBuilder.append(" where tr.Balance<>0");
        if (!onlyMyAp && CommonUtils.isEqualIgnoreCase(accountPayableForm.getShowOnlyMyAPEntries(), AccountingConstants.YES)) {
            queryBuilder.append(" and tr.created_by=").append(loginuser.getUserId());
        } else if (!onlyMyAp && CommonUtils.isEqualIgnoreCase(accountPayableForm.getOnlyAR(), AccountingConstants.YES)) {
            bothTransaction = true;
            queryBuilder.append(" and ((tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
            queryBuilder.append(" and tr.balance_in_process <>0)");
        }
        if (bothTransaction) {
            queryBuilder.append(" or ");
        } else {
            queryBuilder.append(" and ");
        }
        queryBuilder.append(" (tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        if (CommonUtils.isEqualIgnoreCase(accountPayableForm.getShowHold(), ONLY)) {
            queryBuilder.append(" and (tr.Status='").append(STATUS_HOLD).append("'");
        } else {
            queryBuilder.append(" and (tr.Status='").append(STATUS_OPEN).append("'");
            if (CommonUtils.isEqualIgnoreCase(accountPayableForm.getShowHold(), AccountingConstants.YES)) {
                queryBuilder.append(" or tr.Status='").append(STATUS_HOLD).append("'");
            }
        }
        if (bothTransaction) {
            queryBuilder.append(")");
        }
        queryBuilder.append("))");
        if (CommonUtils.isNotEmpty(accountPayableForm.getVendornumber())) {
            StringBuilder vendors = new StringBuilder();
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(accountPayableForm.getVendornumber());
            if (CommonUtils.isEqualIgnoreCase(accountPayableForm.getChparent(), AccountingConstants.YES)) {
                if (CommonUtils.isNotEmpty(tradingPartner.getMaster())) {
                    vendors.append("'").append(tradingPartner.getMaster()).append("'");
                    String childsOfMaster = new TradingPartnerDAO().getChilds(tradingPartner.getMaster());
                    if (CommonUtils.isNotEmpty(childsOfMaster)) {
                        vendors.append(",").append(childsOfMaster);
                    }
                } else {
                    vendors.append("'").append(tradingPartner.getAccountno()).append("'");
                }
                String childOfCustomer = new TradingPartnerDAO().getChilds(tradingPartner.getAccountno());
                if (CommonUtils.isNotEmpty(childOfCustomer)) {
                    vendors.append(",").append(childOfCustomer);
                }
            } else {
                vendors.append("'").append(tradingPartner.getAccountno()).append("'");
            }
            queryBuilder.append(" and tr.cust_no in (").append(vendors).append(")");
        }
        if (CommonUtils.isNotEmpty(accountPayableForm.getInvoicenumber())) {
            queryBuilder.append(" and tr.Invoice_number='").append(accountPayableForm.getInvoicenumber()).append("'");
        }
        if (CommonUtils.isNotEmpty(accountPayableForm.getDatefrom()) && CommonUtils.isNotEmpty(accountPayableForm.getDateto())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(accountPayableForm.getDatefrom(), "MM/dd/yyyy"), "yyyy-MM-dd");
            String toDate = DateUtils.formatDate(DateUtils.parseDate(accountPayableForm.getDateto(), "MM/dd/yyyy"), "yyyy-MM-dd");
            queryBuilder.append(" and date_format(tr.Transaction_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("'");
        }
        queryBuilder.append(" order by tr.cust_no,tr.Transaction_date");
        return queryBuilder.toString();
    }

    public Transaction getOnAccountTransaction(String customerNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        criteria.add(Restrictions.eq("invoiceNumber", AccountingConstants.ON_ACCOUNT));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public Transaction getPrepaymentTransaction(String customerNumber, String billOfLading) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        Disjunction disjunction = Restrictions.disjunction();
        if (billOfLading.startsWith("04")) {
            if (billOfLading.startsWith("04-")) {
                String drcpt = billOfLading.substring(3);
                criteria.add(Restrictions.like("billLaddingNo", drcpt, MatchMode.END));
                if (drcpt.contains("-")) {
                    disjunction.add(Restrictions.eq("docReceipt", StringUtils.substringBefore(drcpt, "-")));
                } else {
                    disjunction.add(Restrictions.eq("docReceipt", drcpt));
                }
            } else {
                String drcptA = billOfLading.substring(2);
                criteria.add(Restrictions.like("billLaddingNo", drcptA, MatchMode.END));
                if (drcptA.contains("-")) {
                    disjunction.add(Restrictions.eq("docReceipt", StringUtils.substringBefore(drcptA, "-")));
                } else {
                    disjunction.add(Restrictions.eq("docReceipt", drcptA));
                }
            }
        } else {
            disjunction.add(Restrictions.eq("billLaddingNo", billOfLading));
            disjunction.add(Restrictions.eq("docReceipt", billOfLading));
        }
        disjunction.add(Restrictions.eq("invoiceNumber", billOfLading));
        criteria.add(disjunction);
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public Transaction getNetSettTransaction(String batchId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("invoiceNumber", "NET SETT" + batchId));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public Transaction getTransaction(String customerNumber, String invoiceOrBl, Integer transactionId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        Disjunction disjunction = Restrictions.disjunction();
        if (invoiceOrBl.startsWith("04")) {
            String fclBillLaddingNo = new FclBlDAO().getBillLaddingNo(invoiceOrBl.substring(2));
            if (CommonUtils.isNotEmpty(fclBillLaddingNo)) {
                disjunction.add(Restrictions.like("billLaddingNo", fclBillLaddingNo, MatchMode.END));
            } else {
                disjunction.add(Restrictions.eq("billLaddingNo", invoiceOrBl));
            }
        } else {
            disjunction.add(Restrictions.eq("billLaddingNo", invoiceOrBl));
        }
        disjunction.add(Restrictions.eq("invoiceNumber", invoiceOrBl));
        disjunction.add(Restrictions.eq("docReceipt", invoiceOrBl));
        criteria.add(disjunction);
        criteria.add(Restrictions.ne("transactionId", transactionId));
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public void reconcileApTransaction(String vendorNumber, String invoiceNumber, Date clearedDate, Integer apBatchId) throws Exception {
        String reconciledDate = DateUtils.formatDate(clearedDate, "yyyy-MM-dd");
        StringBuilder query = new StringBuilder("update transaction");
        query.append(" set cleared='").append(YES).append("',cleared_date='").append(reconciledDate).append("',");
        query.append("reconciled='").append(YES).append("',reconciled_date='").append(reconciledDate).append("'");
        query.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        query.append(" and status='").append(STATUS_PAID).append("'");
        query.append(" and cust_no='").append(vendorNumber).append("'");
        query.append(" and invoice_number='").append(invoiceNumber).append("'");
        query.append(" and ap_batch_id=").append(apBatchId);
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public List<String> getDocReceiptsForVoyage(String voyageNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select distinct(drcpt) from transaction");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and drcpt!=''");
        queryBuilder.append(" and voyage_no like '%").append(voyageNo).append("%'");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public boolean isApInvoiceAvailable(String invoiceNumber, String vendorNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from transaction");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("'");
        queryBuilder.append(" and cust_no='").append(vendorNumber).append("'");
        queryBuilder.append(" and status !='").append(STATUS_REJECT).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null == result || Integer.parseInt(result.toString()) <= 0;
    }

    public Transaction getArInvoice(String customerNumber, String blNumber, String dockReceipt) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        if (CommonUtils.isNotEmpty(dockReceipt)) {
            Conjunction conjunction = Restrictions.conjunction();
            conjunction.add(Restrictions.eq("invoiceNumber", "PRE PAYMENT"));
            conjunction.add(Restrictions.eq("docReceipt", dockReceipt));
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.eq("billLaddingNo", blNumber));
            disjunction.add(conjunction);
            criteria.add(disjunction);
        } else {
            criteria.add(Restrictions.eq("billLaddingNo", blNumber));
        }
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public Transaction getArInvoiceOnlyFcl(String customerNumber, String blNumber, String dockReceipt) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        if (CommonUtils.isNotEmpty(dockReceipt)) {
            criteria.add(Restrictions.like("billLaddingNo", blNumber, MatchMode.END));
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.eq("docReceipt", dockReceipt));
            disjunction.add(Restrictions.eq("invoiceNumber", "PRE PAYMENT"));
            criteria.add(disjunction);
        } else {
            criteria.add(Restrictions.like("billLaddingNo", blNumber, MatchMode.END));
        }
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public Transaction getArTransaction(String customerNumber, String blNumber, String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        Disjunction disjunction = Restrictions.disjunction();
        if (CommonUtils.isNotEmpty(blNumber)) {
            disjunction.add(Restrictions.eq("billLaddingNo", blNumber));
            disjunction.add(Restrictions.eq("invoiceNumber", blNumber));
        } else {
            disjunction.add(Restrictions.eq("billLaddingNo", invoiceNumber));
            disjunction.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        }
        criteria.add(disjunction);
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public void updateTransactionOtherInfo(String disputeUser, String disputeSentUser, Integer transactionId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update transaction_other_info set dispute_date='").append(DateUtils.formatDate(new Date(), "yyyy-MM-dd")).append("',");
        query.append("disputed_user='").append(disputeUser).append("',");
        query.append("dispute_sent_user='").append(disputeSentUser).append("'");
        query.append(" where transaction_id='").append(transactionId).append("'");
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public void updateTransactionOtherInfo(Integer transactionId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update transaction_other_info set dispute_date=NULL,disputed_user='',dispute_sent_user='' ");
        query.append(" where transaction_id='").append(transactionId).append("'");
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public Transaction getArLclInvoice(String customerNumber, String dockReceipt) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        criteria.add(Restrictions.eq("invoiceNumber", dockReceipt));
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public String[] getOpsUserNameEmail(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  concat_ws(' ', u.`first_name`, u.`last_name`) as toName,");
        queryBuilder.append("  u.`email` as toAddress ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    b.`manifested_by` as user_name");
        queryBuilder.append("  from");
        queryBuilder.append("    `fcl_bl` b");
        queryBuilder.append("  where b.`file_no` = :fileNo");
        queryBuilder.append("  order by b.`bol` desc limit 1) as f ");
        queryBuilder.append("  join `user_details` u ");
        queryBuilder.append("    on (u.`login_name` = f.`user_name`) ");
        queryBuilder.append("order by field(u.`status`, 'ACTIVE') desc limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileNo", StringUtils.substringAfter(fileNo, "-04-"));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("toAddress", StringType.INSTANCE);
        Object[] result = (Object[]) query.uniqueResult();
        if (null != result) {
            return Arrays.copyOf(result, result.length, String[].class);
        }
        return null;
    }
}
