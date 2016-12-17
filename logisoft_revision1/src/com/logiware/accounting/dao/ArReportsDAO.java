package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.accounting.model.ReportModel;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArReportsDAO extends BaseHibernateDAO implements ConstantsInterface {

    /**
     * Return a CustomerBean object which contain customer details
     *
     * @param customerNumber
     * @return customer details
     * @see CustomerBean
     */
    public CustomerBean getCustomerDetails(String customerNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("tp.acct_type as accountType,");
        queryBuilder.append("tp.type as type,");
        queryBuilder.append("tp.sub_type as subType,");
        queryBuilder.append("if(tp.eci_acct_no != '',tp.eci_acct_no,if(tp.ecifwno != '',tp.ecifwno,tp.ecivendno)) as blueScreenAccount,");
        queryBuilder.append("tp.ecu_designation as ecuDesignation,");
        queryBuilder.append("if(tp.type = 'master',tp.acct_no,tp.masteracct_no) as master,");
        queryBuilder.append("concat(if(cust_addr.address1!='',concat(cust_addr.address1,'\n'),''),");
        queryBuilder.append("if(cust_addr.city1!='' && cust_addr.state!='',concat(cust_addr.city1,', ',cust_addr.state),");
        queryBuilder.append("if(cust_addr.city1!='',cust_addr.city1,if(cust_addr.state!='',cust_addr.state,''))),");
        queryBuilder.append("if(cust_addr.zip!='',concat(' ',cust_addr.zip),'')) as address,");
        queryBuilder.append("ca.ar_phone as phone,");
        queryBuilder.append("ca.acct_rec_email as email,");
        queryBuilder.append("ca.ar_fax as fax,");
        queryBuilder.append("if(ca.credit_balance='Y','Y','N') as creditBalance,");
        queryBuilder.append("if(ca.credit_invoice='Y','Y','N') as creditInvoice,");
        queryBuilder.append("concat(ud.first_name,' ',ud.last_name) as collector,");
        queryBuilder.append("ud.email as collectorEmail");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" left join cust_accounting ca");
        queryBuilder.append(" on ca.acct_no=tp.acct_no");
        queryBuilder.append(" left join user_details ud");
        queryBuilder.append(" on ud.user_id=ca.ar_contact_code");
        queryBuilder.append(" left join cust_address cust_addr");
        queryBuilder.append(" on ca.cust_address_id = cust_addr.id");
        queryBuilder.append(" where tp.acct_no = '").append(customerNumber).append("'");
        queryBuilder.append(" limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(CustomerBean.class));
        return (CustomerBean) query.uniqueResult();
    }

    /**
     * Return all the customers with credit if the statement is being created
     * for all customers or all or individual collectors
     *
     * @param arReportsForm
     * @return customers with credit
     */
    private String getCustomersWithCredit(ArReportsForm arReportsForm) {
        if (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select concat(\"'\",ar.customer_number,\"'\")");
            queryBuilder.append(" from (");
            queryBuilder.append("select cust_no as customer_number,");
            queryBuilder.append("sum(balance) as balance");
            queryBuilder.append(" from transaction");
            queryBuilder.append(" where transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
            queryBuilder.append(" group by cust_no");
            queryBuilder.append(") as ar");
            queryBuilder.append(" where ar.balance < 0");
            List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
            return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
        } else {
            return null;    
        }
    }

    /**
     * Return all the customers if the statement is being created for all or
     * individual collectors
     *
     * @param arReportsForm
     * @return customers for the given collector
     */
    private String getCustomersForCollector(ArReportsForm arReportsForm) {
        if (CommonUtils.isEmpty(arReportsForm.getCollector())) {
            return null;
        } else {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select concat(\"'\",acct_no,\"'\")");
            queryBuilder.append(" from cust_accounting");
            if (CommonUtils.isNotEqual(arReportsForm.getCollector(), ALL)) {
                queryBuilder.append(" where ar_contact_code = ").append(arReportsForm.getCollector());
            } else {
                queryBuilder.append(" where ar_contact_code is not null");
            }
            queryBuilder.append(" order by acct_no");
            List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
            return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
        }
    }

    /**
     * Build and return the query to include AR invoices into the statement
     *
     * @param arReportsForm
     * @param customersForCollector
     * @param customersWithCredit
     * @param isMaster
     * @return AR query for statement
     */
    private String buildArStatementQuery(ArReportsForm arReportsForm, String customersForCollector, String customersWithCredit, boolean isMaster) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("(select ar.invoice_date as invoice_date,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ar.booking_number as booking_number,");
        queryBuilder.append("ar.cust_name as cust_name,");
        queryBuilder.append("ar.cust_no as cust_no,");
        queryBuilder.append("ar.customer_reference as customer_reference,");
        queryBuilder.append("ar.consignee as consignee,");
        queryBuilder.append("ar.voyage as voyage,");
        queryBuilder.append("sum(if(th.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','AP INV','ADJ'),");
        queryBuilder.append("th.transaction_amount,0)) as invoice_amount,");
        queryBuilder.append("sum(if(th.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','AP INV','ADJ'),");
        queryBuilder.append("th.transaction_amount+th.adjustment_amount,0)) as payment,");
        queryBuilder.append("sum(th.transaction_amount+th.adjustment_amount) as balance,");
        queryBuilder.append("'AR' as transaction_type,");
        queryBuilder.append("cast('AR_INVOICE' as char) as note_module_id,");
        queryBuilder.append("concat(ar.customer_number,'-',ar.invoice_or_bl) as note_ref_id,");
        queryBuilder.append("ar.customer_number as customer_number,");
        queryBuilder.append("th.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_no as customer_number,");
        queryBuilder.append("min(tr.transaction_date) as invoice_date,");
        queryBuilder.append("trim(if(tr.bill_ladding_no != '',upper(tr.bill_ladding_no),upper(tr.invoice_number))) as invoice_or_bl,");
        queryBuilder.append("trim(tr.booking_no) as booking_number,");
        queryBuilder.append("trim(tr.cust_name) as cust_name,");
        queryBuilder.append("trim(tr.cust_no) as cust_no,");
        queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
        queryBuilder.append("trim(tr.cons_name) as consignee,");
        queryBuilder.append("trim(tr.voyage_no) as voyage");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no=tr.cust_no");
        if (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(customersForCollector)) {
            if (arReportsForm.isCreditStatement()) {
                queryBuilder.append(" and tp.acct_no in (").append(customersWithCredit).append(")");
            } else {
                queryBuilder.append(" and tp.acct_no not in (").append(customersWithCredit).append(")");
            }
        }       
        if (arReportsForm.isAllCustomers()) {
            queryBuilder.append(" and tp.acct_no is not null");
        } else if (CommonUtils.isNotEmpty(customersForCollector)) {
            queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
            if (isMaster) {
                queryBuilder.append(" and (tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                queryBuilder.append(" or tp.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("')");
            } else {
                queryBuilder.append(" and tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
            }
        }
        if (arReportsForm.isAllCustomers()
                || CommonUtils.isNotEmpty(customersForCollector)
                || CommonUtils.isEmpty(arReportsForm.getCustomerNumber())) {
            if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), ONLY)) {
                queryBuilder.append(" and (tp.sub_type = 'Export Agent'");
                queryBuilder.append(" or tp.sub_type = 'Import Agent')");
            } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), NO)) {
                queryBuilder.append(" and (tp.sub_type is null");
                queryBuilder.append(" or (tp.sub_type != 'Export Agent'");
                queryBuilder.append(" and tp.sub_type != 'Import Agent'))");
            }
        }
        queryBuilder.append(")");
        queryBuilder.append(" where tr.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (arReportsForm.isCreditInvoice()) {
            queryBuilder.append(" and tr.balance != 0");
        } else {
            queryBuilder.append(" and tr.balance > 0");
        }
        if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), ONLY)) {
            queryBuilder.append(" and tr.invoice_number like '").append(SUBLEDGER_CODE_NETSETT).append("%'");
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getPrepayments(), ONLY)) {
            queryBuilder.append(" and tr.invoice_number = '").append(PRE_PAYMENT).append("'");
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), YES)
                && CommonUtils.isEqualIgnoreCase(arReportsForm.getPrepayments(), YES)) {
            //Nothing required
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), YES)) {
            queryBuilder.append(" and (tr.invoice_number is null");
            queryBuilder.append(" or tr.invoice_number != '").append(PRE_PAYMENT).append("'");
            queryBuilder.append(" or tr.invoice_number like '").append(SUBLEDGER_CODE_NETSETT).append("%')");
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getPrepayments(), YES)) {
            queryBuilder.append(" and (tr.invoice_number is null");
            queryBuilder.append(" or tr.invoice_number = '").append(PRE_PAYMENT).append("'");
            queryBuilder.append(" or tr.invoice_number not like '").append(SUBLEDGER_CODE_NETSETT).append("%')");
        } else {
            queryBuilder.append(" and (tr.invoice_number is null");
            queryBuilder.append(" or (tr.invoice_number not like '").append(SUBLEDGER_CODE_NETSETT).append("%'");
            queryBuilder.append(" and tr.invoice_number != '").append(PRE_PAYMENT).append("'))");
        }
        if (CommonUtils.isNotEmpty(arReportsForm.getExcludeIds())) {
            queryBuilder.append(" and tr.transaction_id not in (").append(arReportsForm.getExcludeIds()).append(")");
        }
        queryBuilder.append(" group by tr.cust_no,invoice_or_bl");
        queryBuilder.append(" order by tr.transaction_date");
        queryBuilder.append(") as ar");
        queryBuilder.append(" join ar_transaction_history th");
        queryBuilder.append(" on (ar.customer_number = th.customer_number");
        queryBuilder.append(" and ar.invoice_or_bl = th.invoice_or_bl)");
        queryBuilder.append(" group by ar.customer_number,ar.invoice_or_bl)");
        return queryBuilder.toString();
    }

    /**
     * Build and return the query to include AP invoices into the statement
     *
     * @param arReportsForm
     * @param customersForCollector
     * @param customersWithCredit
     * @param isMaster
     * @return AP query for statement
     */
    private String buildApStatementQuery(ArReportsForm arReportsForm, String customersForCollector, String customersWithCredit, boolean isMaster) {
        if (arReportsForm.isAp()) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("(select tr.transaction_date as invoice_date,");
            queryBuilder.append("trim(upper(tr.invoice_number)) as invoice_or_bl,");
            queryBuilder.append("NULL AS cust_name,");
            queryBuilder.append("NULL AS cust_no,");
            queryBuilder.append("trim(tr.booking_no) as booking_number,");
            queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
            queryBuilder.append("trim(tr.cons_name) as consignee,");
            queryBuilder.append("trim(tr.voyage_no) as voyage,");
            queryBuilder.append("-tr.transaction_amt as invoice_amount,");
            queryBuilder.append("0 as payment,");
            queryBuilder.append("-tr.transaction_amt as balance,");
            queryBuilder.append("'AP' as transaction_type,");
            queryBuilder.append("tr.transaction_id as id,");
            queryBuilder.append("null as notes");
            queryBuilder.append(" from transaction tr");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (tp.acct_no=tr.cust_no");
            if (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(customersForCollector)) {
                if (arReportsForm.isCreditStatement()) {
                    queryBuilder.append(" and tp.acct_no in (").append(customersWithCredit).append(")");
                } else {
                    queryBuilder.append(" and tp.acct_no not in (").append(customersWithCredit).append(")");
                }
            }
            if (arReportsForm.isAllCustomers()) {
                queryBuilder.append(" and tp.acct_no is not null");
            } else if (CommonUtils.isNotEmpty(customersForCollector)) {
                queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
            } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                if (isMaster) {
                    queryBuilder.append(" and (tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                    queryBuilder.append(" or tp.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("')");
                } else {
                    queryBuilder.append(" and tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                }
            }
            if (arReportsForm.isAllCustomers()
                    || CommonUtils.isNotEmpty(customersForCollector)
                    || CommonUtils.isEmpty(arReportsForm.getCustomerNumber())) {
                if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), ONLY)) {
                    queryBuilder.append(" and (tp.sub_type = 'Export Agent'");
                    queryBuilder.append(" or tp.sub_type = 'Import Agent')");
                } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), NO)) {
                    queryBuilder.append(" and (tp.sub_type is null");
                    queryBuilder.append(" or (tp.sub_type != 'Export Agent'");
                    queryBuilder.append(" and tp.sub_type != 'Import Agent'))");
                }
            }
            queryBuilder.append(")");
            queryBuilder.append(" where tr.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
            queryBuilder.append(" and tr.status = '").append(STATUS_OPEN).append("'");
            queryBuilder.append(" and tr.transaction_amt != 0)");
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    /**
     * Build and return the query to include accruals into the statement
     *
     * @param arReportsForm
     * @param customersForCollector
     * @param customersWithCredit
     * @param isMaster
     * @return accruals query for statement
     */
    private String buildAcStatementQuery(ArReportsForm arReportsForm, String customersForCollector, String customersWithCredit, boolean isMaster) {
        if (arReportsForm.isAc()) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("(select tl.transaction_date as invoice_date,");
            queryBuilder.append("trim(if(tl.invoice_number != '',upper(tl.invoice_number),upper(tl.bill_ladding_no))) as invoice_or_bl,");
            queryBuilder.append("NULL AS cust_name,");
            queryBuilder.append("NULL AS cust_no,");
            queryBuilder.append("trim(tl.booking_no) as booking_number,");
            queryBuilder.append("trim(tl.customer_reference_no) as customer_reference,");
            queryBuilder.append("trim(tl.cons_name) as consignee,");
            queryBuilder.append("trim(tl.voyage_no) as voyage,");
            queryBuilder.append("-tl.transaction_amt as invoice_amount,");
            queryBuilder.append("0 as payment,");
            queryBuilder.append("-tl.transaction_amt as balance,");
            queryBuilder.append("'AC' as transaction_type,");
            queryBuilder.append("null as notes,");
            queryBuilder.append("tl.transaction_id as id");
            queryBuilder.append(" from transaction_ledger tl");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (tp.acct_no=tl.cust_no");
           if (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(customersForCollector)) {
                if (arReportsForm.isCreditStatement()) {
                    queryBuilder.append(" and tp.acct_no in (").append(customersWithCredit).append(")");
                } else {
                    queryBuilder.append(" and tp.acct_no not in (").append(customersWithCredit).append(")");
                }
            }
            if (arReportsForm.isAllCustomers()) {
                queryBuilder.append(" and tp.acct_no is not null");
            } else if (CommonUtils.isNotEmpty(customersForCollector)) {
                queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
            } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                if (isMaster) {
                    queryBuilder.append(" and (tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                    queryBuilder.append(" or tp.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("')");
                } else {
                    queryBuilder.append(" and tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                }
            }
            if (arReportsForm.isAllCustomers()
                    || CommonUtils.isNotEmpty(customersForCollector)
                    || CommonUtils.isEmpty(arReportsForm.getCustomerNumber())) {
                if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), ONLY)) {
                    queryBuilder.append(" and (tp.sub_type = 'Export Agent'");
                    queryBuilder.append(" or tp.sub_type = 'Import Agent')");
                } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), NO)) {
                    queryBuilder.append(" and (tp.sub_type is null");
                    queryBuilder.append(" or (tp.sub_type != 'Export Agent'");
                    queryBuilder.append(" and tp.sub_type != 'Import Agent'))");
                }
            }
            queryBuilder.append(")");
            queryBuilder.append(" where tl.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
            queryBuilder.append(" and tl.status != '").append(STATUS_ASSIGN).append("'");
            queryBuilder.append(" and tl.status != '").append(STATUS_EDI_ASSIGNED).append("'");
            queryBuilder.append(" and tl.status != '").append(STATUS_INACTIVE).append("'");
            queryBuilder.append(" and tl.transaction_amt != 0)");
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    /**
     * Build and return queries in a map to include AR invoices, AP invoices and
     * Accruals
     *
     * @param arReportsForm
     * @return map of AR, AP and accruals queries
     */
    private Map<String, String> buildStatementQueries(ArReportsForm arReportsForm) {
        String customersWithCredit = getCustomersWithCredit(arReportsForm);
        String customersForCollector = getCustomersForCollector(arReportsForm);
        boolean isMaster = false;
        if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
            isMaster = new TradingPartnerDAO().isMaster(arReportsForm.getCustomerNumber());
        }
        String arQuery = buildArStatementQuery(arReportsForm, customersForCollector, customersWithCredit, isMaster);
        String apQuery = buildApStatementQuery(arReportsForm, customersForCollector, customersWithCredit, isMaster);
        String acQuery = buildAcStatementQuery(arReportsForm, customersForCollector, customersWithCredit, isMaster);
        Map<String, String> queries = new HashMap<String, String>();
        queries.put("arQuery", arQuery);
        queries.put("apQuery", apQuery);
        queries.put("acQuery", acQuery);
        return queries;
    }

    /**
     * Return ReportBean object of AR buckets for the statement
     *
     * @param arQuery
     * @return AR buckets
     * @throws Exception
     */
    private ReportBean getArStatementBuckets(String arQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("concat('$',if(ar.30days <0,concat('(',format(-ar.30days,2),')'),format(if(ar.30days !=0,ar.30days,0),2))) as age30Days,");
        queryBuilder.append("concat('$',if(ar.60days <0,concat('(',format(-ar.60days,2),')'),format(if(ar.60days !=0,ar.60days,0),2))) as age60Days,");
        queryBuilder.append("concat('$',if(ar.90days <0,concat('(',format(-ar.90days,2),')'),format(if(ar.90days !=0,ar.90days,0),2))) as age90Days,");
        queryBuilder.append("concat('$',if(ar.91days <0,concat('(',format(-ar.91days,2),')'),format(if(ar.91days !=0,ar.91days,0),2))) as age91Days,");
        queryBuilder.append("concat('$',if(ar.total <0,concat('(',format(-ar.total,2),')'),format(if(ar.total !=0,ar.total,0),2))) as ageTotal");
        queryBuilder.append(" from (");
        queryBuilder.append("select sum(if(datediff(sysdate(),ar.invoice_date) <=30,ar.balance,0)) as 30days,");
        queryBuilder.append("sum(if(datediff(sysdate(),ar.invoice_date) >=31 and datediff(sysdate(),ar.invoice_date) <=60,ar.balance,0)) as 60days,");
        queryBuilder.append("sum(if(datediff(sysdate(),ar.invoice_date) >=61 and datediff(sysdate(),ar.invoice_date) <=90,ar.balance,0)) as 90days,");
        queryBuilder.append("sum(if(datediff(sysdate(),ar.invoice_date) >=91,ar.balance,0)) as 91days,");
        queryBuilder.append("sum(ar.balance) as total");
        queryBuilder.append(" from (");
        queryBuilder.append(arQuery);
        queryBuilder.append(") as ar");
        queryBuilder.append(") as ar");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return (ReportBean) query.uniqueResult();
    }

    /**
     * Return ReportBean object of AP buckets for the statement
     *
     * @param apQuery
     * @param acQuery
     * @return AP buckets
     * @throws Exception
     */
    private ReportBean getApStatementBuckets(String apQuery, String acQuery) {
        if (null == apQuery && null == acQuery) {
            return null;
        } else {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select ");
            queryBuilder.append("concat('$',if(ap.30days <0,concat('(',format(-ap.30days,2),')'),format(if(ap.30days !=0,ap.30days,0),2))) as age30Days,");
            queryBuilder.append("concat('$',if(ap.60days <0,concat('(',format(-ap.60days,2),')'),format(if(ap.60days !=0,ap.60days,0),2))) as age60Days,");
            queryBuilder.append("concat('$',if(ap.90days <0,concat('(',format(-ap.90days,2),')'),format(if(ap.90days !=0,ap.90days,0),2))) as age90Days,");
            queryBuilder.append("concat('$',if(ap.91days <0,concat('(',format(-ap.91days,2),')'),format(if(ap.91days !=0,ap.91days,0),2))) as age91Days,");
            queryBuilder.append("concat('$',if(ap.total <0,concat('(',format(-ap.total,2),')'),format(if(ap.total !=0,ap.total,0),2))) as ageTotal,");
            queryBuilder.append("format(if(ap.ap !=0,ap.ap,0),2) as apBalance,");
            queryBuilder.append("format(if(ap.ac !=0,ap.ac,0),2) as acBalance");
            queryBuilder.append(" from (");
            queryBuilder.append("select sum(if(datediff(sysdate(),ap.invoice_date) <=30,ap.balance,0)) as 30Days,");
            queryBuilder.append("sum(if(datediff(sysdate(),ap.invoice_date) >=31 and datediff(sysdate(),ap.invoice_date)<=60,ap.balance,0)) as 60Days,");
            queryBuilder.append("sum(if(datediff(sysdate(),ap.invoice_date) >=61 and datediff(sysdate(),ap.invoice_date)<=90,ap.balance,0)) as 90Days,");
            queryBuilder.append("sum(if(datediff(sysdate(),ap.invoice_date) >=91,ap.balance,0)) as 91Days,");
            queryBuilder.append("sum(ap.balance) as total,");
            queryBuilder.append("sum(if(ap.transaction_type ='AP',ap.balance,0)) as ap,");
            queryBuilder.append("sum(if(ap.transaction_type ='AC',ap.balance,0)) as ac");
            queryBuilder.append(" from (");
            if (null != apQuery) {
                queryBuilder.append(apQuery);
            }
            if (null != acQuery) {
                queryBuilder.append(null != apQuery ? " union " : "").append(acQuery);
            }
            queryBuilder.append(") as ap");
            queryBuilder.append(") as ap");
            SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
            return (ReportBean) query.uniqueResult();
        }
    }

    /**
     * Return List of Report Bean objects of transactions to include into the
     * statement
     *
     * @param isExcelReport
     * @param queries
     * @return transactions for the statement
     * @throws Exception
     */
    private List<ReportBean> getStatementTransactions(boolean isExcelReport, Map<String, String> queries) {
        String arQuery = queries.get("arQuery");
        String apQuery = queries.get("apQuery");
        String acQuery = queries.get("acQuery");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select date_format(t.invoice_date,'%m/%d/%Y') as invoiceDate,");
        if (isExcelReport) {
            queryBuilder.append("upper(if(t.invoice_or_bl like '%04-%',");
            queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,");
            queryBuilder.append("'04-',1),''),'-',''),t.invoice_or_bl)) as invoiceOrBl,");
            queryBuilder.append("upper(t.cust_name) as customerName,");
            queryBuilder.append("upper(t.cust_no) as customerNumber,");
            queryBuilder.append("upper(t.booking_number) as bookingNumber,");
            queryBuilder.append("upper(t.customer_reference) as customerReference,");
            queryBuilder.append("upper(t.consignee) as consignee,");
            queryBuilder.append("upper(t.voyage) as voyage,");
        } else {
            queryBuilder.append("upper(left(if(t.invoice_or_bl like '%04-%',");
            queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,");
            queryBuilder.append("'04-',1),''),'-',''),t.invoice_or_bl),20)) as invoiceOrBl,");
            queryBuilder.append("upper(left(t.booking_number,12)) as bookingNumber,");
            queryBuilder.append("upper(left(t.customer_reference,25)) as customerReference,");
            queryBuilder.append("upper(left(t.consignee,10)) as consignee,");
            queryBuilder.append("upper(left(t.voyage,10)) as voyage,");
        }
        queryBuilder.append("format(t.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("format(t.payment,2) as paymentAmount,");
        queryBuilder.append("format(t.balance,2) as balance,");
        queryBuilder.append("cast(t.notes as char character set latin1) as notes");
        queryBuilder.append(" from ( ");
        queryBuilder.append("(select ar.invoice_date as invoice_date,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ar.booking_number as booking_number,");
        queryBuilder.append("ar.cust_name as cust_name,");
        queryBuilder.append("ar.cust_no as cust_no,");
        queryBuilder.append("ar.customer_reference as customer_reference,");
        queryBuilder.append("ar.consignee as consignee,");
        queryBuilder.append("ar.voyage as voyage,");
        queryBuilder.append("ar.invoice_amount as invoice_amount,");
        queryBuilder.append("ar.payment as payment,");
        queryBuilder.append("ar.balance as balance,");
        queryBuilder.append("ar.transaction_type as transaction_type,");
        queryBuilder.append("ar.id as id,");
        queryBuilder.append("upper(if(ar.prepayment_notes!='' && group_concat(n.note_desc separator '<--->')!='',");
        queryBuilder.append("concat(ar.prepayment_notes,'<--->',group_concat(n.note_desc separator '<--->')!=''),");
        queryBuilder.append("if(ar.prepayment_notes!='',ar.prepayment_notes,");
        queryBuilder.append("if(group_concat(n.note_desc separator '<--->')!='',group_concat(n.note_desc separator '<--->'),'')))) as notes");
        queryBuilder.append(" from ( ");
        queryBuilder.append("(select ar.invoice_date as invoice_date,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ar.booking_number as booking_number,");
        queryBuilder.append("ar.cust_name as cust_name,");
        queryBuilder.append("ar.cust_no as cust_no,");
        queryBuilder.append("ar.customer_reference as customer_reference,");
        queryBuilder.append("ar.consignee as consignee,");
        queryBuilder.append("ar.voyage as voyage,");
        queryBuilder.append("ar.invoice_amount as invoice_amount,");
        queryBuilder.append("ar.payment as payment,");
        queryBuilder.append("ar.balance as balance,");
        queryBuilder.append("ar.transaction_type as transaction_type,");
        queryBuilder.append("ar.note_module_id as note_module_id,");
        queryBuilder.append("ar.note_ref_id as note_ref_id,");
        queryBuilder.append("group_concat(p.notes separator '<--->') as prepayment_notes,");
        queryBuilder.append("ar.id as id");
        queryBuilder.append(" from ").append(arQuery).append(" as ar");
        queryBuilder.append(" left join payments p");
        queryBuilder.append(" on (p.payment_type='Check' and p.invoice_no='PRE PAYMENT'");
        queryBuilder.append(" and p.Bill_Ladding_No=ar.invoice_or_bl and p.Cust_No=ar.customer_number)");
        queryBuilder.append(" group by ar.invoice_or_bl)");
        queryBuilder.append(") as ar");
        queryBuilder.append(" left join notes n");
        queryBuilder.append(" on (ar.note_module_id=n.module_id");
        queryBuilder.append(" and ar.note_ref_id=n.module_ref_id");
        queryBuilder.append(" and n.print_on_report = 1)");
        queryBuilder.append(" where ar.balance!=0");
        queryBuilder.append(" group by ar.invoice_or_bl,ar.transaction_type");
        queryBuilder.append(" order by ar.invoice_date)");
        if (null != apQuery) {
            queryBuilder.append(" union ").append(apQuery);
        }
        if (null != acQuery) {
            queryBuilder.append(" union ").append(acQuery);
        }
        queryBuilder.append(" ) as t");
        queryBuilder.append(" order by t.invoice_date");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("invoiceDate", StringType.INSTANCE);
        query.addScalar("invoiceOrBl", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        if (isExcelReport) {
            query.addScalar("customerName", StringType.INSTANCE);
            query.addScalar("customerNumber", StringType.INSTANCE);
        }
        query.addScalar("customerReference", StringType.INSTANCE);
        query.addScalar("consignee", StringType.INSTANCE);
        query.addScalar("voyage", StringType.INSTANCE);
        query.addScalar("invoiceAmount", StringType.INSTANCE);
        query.addScalar("paymentAmount", StringType.INSTANCE);
        query.addScalar("balance", StringType.INSTANCE);
        query.addScalar("notes", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return query.list();
    }

    /**
     * Return ReportModel object contains arBuckets, apBuckets and transactions
     *
     * @param isExcelReport
     * @param arReportsForm
     * @return statement model
     */
    public ReportModel getStatementModel(boolean isExcelReport, ArReportsForm arReportsForm) {
        Map<String, String> queries = buildStatementQueries(arReportsForm);
        String arQuery = queries.get("arQuery");
        String apQuery = queries.get("apQuery");
        String acQuery = queries.get("acQuery");
        ReportBean arBuckets = getArStatementBuckets(arQuery);
        ReportBean apBuckets = getApStatementBuckets(apQuery, acQuery);
        List<ReportBean> transactions = getStatementTransactions(isExcelReport, queries);
        return new ReportModel(arBuckets, apBuckets, transactions);
    }

    /**
     * Return List of Report Bean objects of accounts configured for statements
     *
     * @param isCustomerStatement
     * @return accounts
     * @throws Exception
     */
    public List<ReportBean> getAccountsConfiguredForStatements(boolean isCustomerStatement) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(date_format(max(mail.email_date),'%Y-%m-%d') as char character set latin1) as lastStatementDate");
        queryBuilder.append(" from mail_transactions mail");
        queryBuilder.append(" where mail.module_name='AR Statement'");
        String lastStatementDate = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
//	EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
//	List<EmailSchedulerVO> faxes = emailschedulerDAO.getFaxes("AR Statement", "Completed", lastStatementDate);
//	for (EmailSchedulerVO fax : faxes) {
//	    fax.setStatus(Fax.checkStatus(fax.getResponseCode()));
//	    emailschedulerDAO.update(fax);
//	}
        getCurrentSession().flush();
        queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as accountName,");
        queryBuilder.append("tp.acct_no as accountNumber,");
        queryBuilder.append("tp.acct_type as accountType,");
        queryBuilder.append("if(addr.address1 != '',addr.address1,'') as address,");
        queryBuilder.append("if(addr.city1 != '',addr.city1,'') as city,");
        queryBuilder.append("if(addr.state != '',addr.state,'') as state,");
        queryBuilder.append("if(addr.zip != '',addr.zip,'') as zip,");
        queryBuilder.append("if(user.login_name != '',user.login_name,'') as collector,");
        queryBuilder.append("if(ge.codedesc is null,'No',ge.codedesc) as statementType,");
        queryBuilder.append("if(ca.schedule_stmt !='',ca.schedule_stmt,'') as statementFrequency,");
        queryBuilder.append("if(ca.exempt_credit_process = 'Y','Yes','No') as exemptCreditProcess,");
        queryBuilder.append("if(ca.hhg_pe_autoscredit = 'Y','Yes','No') as hhgPeAutosCredit,");
        queryBuilder.append("if(ca.credit_balance = 'Y','Yes','No') as creditBalance,");
        queryBuilder.append("if(ca.credit_invoice = 'Y','Yes','No') as creditInvoice,");
        queryBuilder.append("if(ca.acct_rec_email != '',ca.acct_rec_email,'') as email,");
        queryBuilder.append("if(ca.ar_fax != '',ca.ar_fax,'') as fax,");
        queryBuilder.append("if(ca.ar_phone !='',ca.ar_phone,'') as phone,");
        queryBuilder.append("if(mail.status = 'Completed','Success',if(mail.status = 'Failed','Fail','')) as attempt,");
        queryBuilder.append("format(tr.30days,2) as age30Days,");
        queryBuilder.append("format(tr.60days,2) as age60Days,");
        queryBuilder.append("format(tr.90days,2) as age90Days,");
        queryBuilder.append("format(tr.91days,2) as age91Days,");
        queryBuilder.append("format(tr.total,2) as ageTotal");
        queryBuilder.append(" from (");
        queryBuilder.append("select tr.cust_no as customer_number,");
        queryBuilder.append("sum(if(datediff(sysdate(),tr.transaction_date) <= 30,tr.balance,0)) as 30days,");
        queryBuilder.append("sum(if(datediff(sysdate(),tr.transaction_date) >= 31 and datediff(sysdate(),tr.transaction_date) <= 60,tr.balance,0)) as 60days,");
        queryBuilder.append("sum(if(datediff(sysdate(),tr.transaction_date) >= 61 and datediff(sysdate(),tr.transaction_date) <= 90,tr.balance,0)) as 90days,");
        queryBuilder.append("sum(if(datediff(sysdate(),tr.transaction_date) >= 91,tr.balance,0)) as 91days,");
        queryBuilder.append("sum(tr.balance) as total");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" where tr.cust_no != ''");
        queryBuilder.append(" and tr.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("group by tr.cust_no");
        queryBuilder.append(") as tr");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tr.customer_number = tp.acct_no)");
        queryBuilder.append(" join cust_accounting ca");
        queryBuilder.append(" on (tp.acct_no = ca.acct_no");
        if (!isCustomerStatement) {
            queryBuilder.append(" and ca.exempt_credit_process='Y'");
        }
        queryBuilder.append(")");
        queryBuilder.append(" left join cust_address addr");
        queryBuilder.append(" on ca.cust_address_id = addr.id");
        queryBuilder.append(" left join user_details user");
        queryBuilder.append(" on (ca.ar_contact_code = user.user_id)");
        queryBuilder.append(" left join genericcode_dup ge");
        queryBuilder.append(" on (ge.id = ca.statements)");
        queryBuilder.append(" left join mail_transactions mail");
        queryBuilder.append(" on (mail.module_name = 'AR Statement'");
        queryBuilder.append(" and mail.module_id = tp.acct_no");
        queryBuilder.append(" and mail.email_date >= '").append(lastStatementDate).append(" 23:59:59')");
        queryBuilder.append(" where tr.total != 0");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return query.list();
    }

    /**
     * Build and return summary aging query
     *
     * @param arReportsForm
     * @return summary aging query
     */
    private String buildAgingSummaryQuery(ArReportsForm arReportsForm) throws Exception {
        String paymentTypes = "'AP PY', 'AR PY', 'AP INV', 'ADJ'";
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getCutOffDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String paymentDate = arReportsForm.isAllPayments() ? DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59") : cutOffDate;
        StringBuilder query = new StringBuilder();
        query.append("select");
        query.append("  upper(if(t.type = 'master', t.acct_no, t.masteracct_no)) as masterAccountNumber,");
        query.append("  upper(coalesce(t.eci_acct_no, t.ecifwno, t.ecivendno)) as blueScreenAccount,");
        query.append("  upper(t.ecu_designation) as ecuDesignation,");
        query.append("  upper(t.acct_no) as customerNumber,");
        query.append("  upper(t.acct_name) as customerName,");
        query.append("  upper(u.login_name) as collector,");
        query.append("  upper(concat(s.code,'-',s.codedesc)) as salesCode,");
        query.append("  upper(if(c.codedesc <> '', c.codedesc, 'No Credit')) as creditStatus,");
        query.append("  if(a.credit_limit <> 0, format(a.credit_limit, 2), '0.00') as creditLimit,");
        query.append("  format(h.age30days, 2) as age30Days,");
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            query.append("  format(h.age45days, 2) as age45Days,");
        }
        query.append("  format(h.age60days, 2) as age60Days,");
        query.append("  format(h.age90days, 2) as age90Days,");
        query.append("  format(h.age120days, 2) as age120Days,");
        query.append("  format(h.age180days, 2) as age180Days,");
        query.append("  format(h.age181days, 2) as age181Days,");
        query.append("  format(h.total, 2) as ageTotal ");
        query.append("from");
        query.append("  (select");
        query.append("      h.customer_number as customer_number,");
        query.append("      h.age30days as age30days,");
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            query.append("  h.age45days as age45Days,");
        }
        query.append("      h.age60days as age60days,");
        query.append("      h.age90days as age90days,");
        query.append("      h.age120days as age120days,");
        query.append("      h.age180days as age180days,");
        query.append("      h.age181days as age181days,");
        query.append("      h.total as total");
        query.append("    from");
        query.append("      (select");
        if (arReportsForm.isMaster()) {
            query.append("      if(t.type <> 'master' and t.masteracct_no <> '', t.masteracct_no, t.acct_no) as master_customer,");
            query.append("      if(t.type <> 'master' and t.masteracct_no <> '', t.masteracct_no, t.acct_no) as customer_number,");
        } else {
            query.append("      h.customer_number as customer_number,");
        }
        query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) <= 30, h.invoice_balance, 0.00)) as age30days,");
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) between 31 and 45, h.invoice_balance, 0.00)) as age45days,");
            query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) between 46 and 60, h.invoice_balance, 0.00)) as age60days,");
        } else {
            query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) between 31 and 60, h.invoice_balance, 0.00)) as age60days,");
        }
        query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) between 61 and 90, h.invoice_balance, 0.00)) as age90days,");
        query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) between 91 and 120, h.invoice_balance, 0.00)) as age120days,");
        query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) between 121 and 180, h.invoice_balance, 0.00)) as age180days,");
        query.append("      sum(if(datediff('").append(cutOffDate).append("', h.invoice_date) >= 181, h.invoice_balance, 0.00)) as age181days,");
        query.append("      sum(h.invoice_balance) as total");
        query.append("    from");
        query.append("      (select");
        query.append("        h.customer_number as customer_number,");
        query.append("        h.invoice_date as invoice_date,");
        query.append("        h.invoice_balance as invoice_balance");
        query.append("      from");
        query.append("        (select");
        query.append("          h.customer_number as customer_number,");
        query.append("          h.invoice_or_bl as invoice_or_bl,");
        query.append("          h.invoice_date as invoice_date,");
        query.append("          h.invoice_balance as invoice_balance");
        query.append("        from");
        query.append("          (select");
        query.append("            h.customer_number as customer_number,");
        query.append("            h.invoice_or_bl as invoice_or_bl,");
        query.append("            coalesce(h.invoice_date, h.alt_invoice_date) as invoice_date,");
        query.append("            h.invoice_amount + coalesce(sum(p.transaction_amount + p.adjustment_amount), 0.00) as invoice_balance");
        query.append("          from");
        query.append("            (select");
        query.append("              h.customer_number as customer_number,");
        query.append("              h.invoice_or_bl as invoice_or_bl,");
        query.append("              min(if(h.transaction_type <> 'PP INV', h.invoice_date, null)) as invoice_date,");
        query.append("              min(h.invoice_date) as alt_invoice_date,");
        query.append("              sum(h.transaction_amount + h.adjustment_amount) as invoice_amount");
        query.append("            from");
        query.append("              ar_transaction_history h");
        query.append("              join trading_partner t");
        query.append("                on (");
        query.append("                  h.customer_number = t.acct_no");
        if (arReportsForm.isAllCustomers()) {
            if (CommonUtils.isNotEmpty(arReportsForm.getCustomerFromRange())) {
                if (CommonUtils.isNotEmpty(arReportsForm.getCustomerToRange())) {
                    String range = arReportsForm.getCustomerFromRange() + "-" + arReportsForm.getCustomerToRange();
                    query.append("                  and t.acct_name rlike '^[").append(range).append("]'");
                } else {
                    query.append("                  and t.acct_name like '").append(arReportsForm.getCustomerFromRange()).append("%'");
                }
            }
            if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), ONLY)) {
                query.append("                  and t.sub_type in ('Export Agent', 'Import Agent')");
            } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), NO)) {
                query.append("                  and (t.sub_type is null or t.sub_type not in ('Export Agent', 'Import Agent'))");
            }
        } else if (CommonUtils.isEmpty(arReportsForm.getSalesManager().getManagerName())) {
            if (CommonUtils.isEmpty(arReportsForm.getCollector())) {
                if (arReportsForm.isMaster()) {
                    query.append("                  and (");
                    query.append("                    t.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                    query.append("                    or t.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                    query.append("                  )");
                } else {
                    query.append("                  and t.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                }
            }
        }
        query.append("                )");
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            query.append("              join cust_general_info g");
            query.append("                on (");
            if (arReportsForm.getSalesManager().getSalesId().contains(",")) {
                query.append("                  g.sales_code in (").append(arReportsForm.getSalesManager().getSalesId()).append(")");
            } else {
                query.append("                  g.sales_code = ").append(arReportsForm.getSalesManager().getSalesId());
            }
            query.append("                  and t.acct_no = g.acct_no");
            query.append("                )");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            query.append("              join cust_accounting a");
            query.append("                on (");
            query.append("                  t.acct_no = a.acct_no");
            if (CommonUtils.isEqualIgnoreCase(arReportsForm.getCollector(), ALL)) {
                query.append("                  and a.ar_contact_code is not null");
            } else {
                query.append("                  and a.ar_contact_code = ").append(arReportsForm.getCollector());
            }
            query.append("                )");
        }
        query.append("            where");
        if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), ONLY)) {
            query.append("              h.transaction_type = 'NS INV'");
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), NO)) {
            query.append("              h.transaction_type not in (").append(paymentTypes).append(", 'NS INV')");
        } else {
            query.append("              h.transaction_type not in (").append(paymentTypes).append(")");
        }
        query.append("              and h.posted_date <= '").append(cutOffDate).append("'");
        query.append("            group by h.customer_number, h.invoice_or_bl");
        query.append("            ) as h");
        query.append("            left join ar_transaction_history p");
        query.append("              on (");
        query.append("                h.customer_number = p.customer_number");
        query.append("                and h.invoice_or_bl = p.invoice_or_bl");
        query.append("                and p.transaction_type in (").append(paymentTypes).append(")");
        query.append("                and p.posted_date <= '").append(paymentDate).append("'");
        query.append("              )");
        query.append("            group by h.customer_number, h.invoice_or_bl");
        query.append("            ) as h");
        query.append("          where h.invoice_balance <> 0.00");
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            query.append("            and datediff('").append(cutOffDate).append("', h.invoice_date) > 45 ");
        }
        query.append("        ) as h");
        query.append("        join transaction i");
        query.append("          on (");
        query.append("            h.customer_number = i.cust_no");
        query.append("            and if(i.bill_ladding_no <> '', i.bill_ladding_no, i.invoice_number) =  h.invoice_or_bl");
        query.append("            and i.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        query.append("          )");
        query.append("        group by h.customer_number, h.invoice_or_bl");
        query.append("      ) as h");
        if (arReportsForm.isMaster()) {
            query.append("      join trading_partner t");
            query.append("        on (h.customer_number = t.acct_no)");
            query.append("      group by master_customer");
        } else {
            query.append("      group by h.customer_number");
        }
        query.append("    ) as h");
        query.append("  where h.total <> 0 ");
        query.append("  ) as h");
        query.append("  join trading_partner t");
        query.append("    on (h.customer_number = t.acct_no)");
        query.append("  left join cust_accounting a");
        query.append("    on (t.acct_no = a.acct_no)");
        query.append("  left join genericcode_dup c");
        query.append("    on (a.credit_status = c.id)");
        query.append("  left join cust_general_info g");
        query.append("    on (t.acct_no = g.acct_no)");
        query.append("  left join genericcode_dup s");
        query.append("    on (g.sales_code = s.id)");
        query.append("  left join user_details u");
        query.append("    on (a.ar_contact_code = u.user_id) ");
        if (arReportsForm.isTop10Customers() && (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(arReportsForm.getCollector()))) {
            query.append("order by h.total desc limit 10");
        } else {
            query.append("order by t.acct_name, t.acct_no");
        }
        return query.toString();
    }

    /**
     * Build and return detail aging query
     *
     * @param arReportsForm
     * @return detail aging query
     */
    private String buildAgingDetailQuery(ArReportsForm arReportsForm) throws Exception {
        String paymentTypes = "'AP PY', 'AR PY', 'AP INV', 'ADJ'";
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getCutOffDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String paymentDate = arReportsForm.isAllPayments() ? DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59") : cutOffDate;
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append("  upper(if(h.type = 'master', h.customer_number, h.masteracct_no)) as masterAccountNumber,");
        if (arReportsForm.isMaster()) {
            query.append("  upper(if(h.type <> 'master' and m.acct_no <> '', m.ecu_designation, h.ecu_designation)) as ecuDesignation,");
            query.append("  upper(if(h.type <> 'master' and m.acct_no <> '', coalesce(m.eci_acct_no, m.ecifwno, m.ecivendno), coalesce(h.eci_acct_no, h.ecifwno, h.ecivendno))) as blueScreenAccount,");
            query.append("  upper(if(h.type <> 'master' and m.acct_no <> '', m.acct_no, h.customer_number)) as customerNumber,");
            query.append("  upper(if(h.type <> 'master' and m.acct_no <> '', m.acct_name, h.customer_name)) as customerName,");
        } else {
            query.append("  upper(coalesce(h.eci_acct_no, h.ecifwno, h.ecivendno)) as blueScreenAccount,");
            query.append("  upper(h.ecu_designation) as ecuDesignation,");
            query.append("  upper(h.customer_number) as customerNumber,");
            query.append("  upper(h.customer_name) as customerName,");
        }
        query.append("  upper(u.login_name) as collector,");
        query.append("  upper(concat(s.code,'-',s.codedesc)) as salesCode,");
        query.append("  upper(c.codedesc) as creditStatus,");
        query.append("  h.billing_terminal as billingTerminal,");
        query.append("  upper(if(h.invoice_or_bl like '%-04-%', concat('04', substring_index(h.invoice_or_bl, '-04-', - 1)), h.invoice_or_bl)) as invoiceOrBl,");
        query.append("  upper(h.customer_reference) as customerReference,");
        query.append("  upper(h.voyage) as voyage,");
        query.append("  date_format(h.invoice_date, '%m/%d/%Y') as invoiceDate,");
        query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) <= 30, format(h.invoice_balance, 2), '0.00') as age30Days,");
        if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) between 31 and 45, format(h.invoice_balance, 2), '0.00') as age45Days,");
            query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) between 46 and 60, format(h.invoice_balance, 2), '0.00') as age60Days,");
        } else {
            query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) between 31 and 60, format(h.invoice_balance, 2), '0.00') as age60Days,");
        }
        query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) between 61 and 90, format(h.invoice_balance, 2), '0.00') as age90Days,");
        query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) between 91 and 120, format(h.invoice_balance, 2), '0.00') as age120Days,");
        query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) between 121 and 180, format(h.invoice_balance, 2), '0.00') as age180Days,");
        query.append("  if(datediff('").append(cutOffDate).append("', h.invoice_date) >= 181, format(h.invoice_balance, 2), '0.00') as age181Days,");
        query.append("  format(h.invoice_balance, 2) as ageTotal ");
        query.append("from");
        query.append("  (select ");
        query.append("    h.type,");
        query.append("    h.masteracct_no,");
        query.append("    h.eci_acct_no,");
        query.append("    h.ecifwno,");
        query.append("    h.ecivendno,");
        query.append("    h.ecu_designation,");
        query.append("    h.customer_number,");
        query.append("    h.customer_name,");
        query.append("    h.billing_terminal,");
        query.append("    h.invoice_or_bl,");
        query.append("    h.customer_reference,");
        query.append("    h.voyage,");
        query.append("    h.invoice_date,");
        query.append("    h.invoice_balance ");
        query.append("  from");
        query.append("    (select ");
        query.append("      h.type,");
        query.append("      h.masteracct_no,");
        query.append("      h.eci_acct_no,");
        query.append("      h.ecifwno,");
        query.append("      h.ecivendno,");
        query.append("      h.ecu_designation,");
        query.append("      h.customer_number,");
        query.append("      h.customer_name,");
        query.append("      h.billing_terminal,");
        query.append("      h.invoice_or_bl,");
        query.append("      h.customer_reference,");
        query.append("      h.voyage,");
        query.append("      coalesce(h.invoice_date, h.alt_invoice_date) as invoice_date,");
        query.append("      h.invoice_amount + coalesce(sum(p.transaction_amount + p.adjustment_amount), 0.00) as invoice_balance ");
        query.append("    from");
        query.append("      (select ");
        query.append("        i.type,");
        query.append("        i.masteracct_no,");
        query.append("        i.eci_acct_no,");
        query.append("        i.ecifwno,");
        query.append("        i.ecivendno,");
        query.append("        i.ecu_designation,");
        query.append("        i.customer_number,");
        query.append("        i.customer_name,");
        query.append("        i.billing_terminal,");
        query.append("        h.invoice_or_bl,");
        query.append("        i.customer_reference,");
        query.append("        i.voyage,");
        query.append("        min(if(h.transaction_type <> 'PP INV', h.invoice_date, null)) as invoice_date,");
        query.append("        min(h.invoice_date) as alt_invoice_date,");
        query.append("        sum(h.transaction_amount + h.adjustment_amount) as invoice_amount ");
        query.append("      from");
        query.append("        (select ");
        query.append("          t.type,");
        query.append("          t.masteracct_no,");
        query.append("          t.eci_acct_no,");
        query.append("          t.ecifwno,");
        query.append("          t.ecivendno,");
        query.append("          t.ecu_designation,");
        query.append("          t.acct_no as customer_number,");
        query.append("          t.acct_name as customer_name,");
        if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            query.append("          IF(o.billing_terminal='01','04',o.billing_terminal) AS billing_terminal,");
        }else{
          query.append("          o.billing_terminal,");  
        }
        query.append("          o.invoice_or_bl,");
        query.append("          i.customer_reference_no as customer_reference,");
        query.append("          i.voyage_no as voyage ");
        query.append("        from");
        query.append("          transaction i ");
        query.append("          join transaction_other_info o ");
        query.append("            on (o.transaction_id = i.transaction_id) ");
        query.append("          join trading_partner t ");
        query.append("            on (t.acct_no = i.cust_no) ");
        query.append("          join cust_accounting a ");
        query.append("            on (");
        query.append("              a.acct_no = t.acct_no ");
        if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            query.append("              and a.credit_status = (select c.id from genericcode_dup c where c.codedesc = 'No Credit' and c.codetypeid = (select codetypeid from codetype where description = 'Credit Status'))");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            if (CommonUtils.isEqualIgnoreCase(arReportsForm.getCollector(), ALL)) {
                query.append("              and a.ar_contact_code is not null");
            } else {
                query.append("              and a.ar_contact_code = ").append(arReportsForm.getCollector());
            }
        }
        query.append("            ) ");
        query.append("        where");
        query.append("          i.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            if (arReportsForm.getTerminalManager().getTerminalNumber().contains(",")) {
                query.append("          and o.billing_terminal in ('").append(arReportsForm.getTerminalManager().getTerminalNumber().replace(",", "','")).append("')");
            } else {
                query.append("          and o.billing_terminal = '").append(arReportsForm.getTerminalManager().getTerminalNumber()).append("'");
            }
            query.append("          and i.balance <> 0.00");
            query.append("          AND t.ecu_designation ='LO'");
        } else {
            if (arReportsForm.isTop10Customers() && (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(arReportsForm.getCollector()))) {
                SQLQuery sqlQuery = getCurrentSession().createSQLQuery(buildAgingSummaryQuery(arReportsForm));
                sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
                List<ReportBean> list = sqlQuery.list();
                if (CommonUtils.isNotEmpty(list)) {
                    query.append("          and t.acct_no in (");
                    boolean isNotFirst = false;
                    for (ReportBean bean : list) {
                        if (isNotFirst) {
                            query.append(",");
                            isNotFirst = true;
                        }
                        query.append("'").append(bean.getCustomerNumber()).append("'");
                    }
                    query.append("          )");
                } else {
                    return null;
                }
            } else if (arReportsForm.isAllCustomers()) {
                if (CommonUtils.isNotEmpty(arReportsForm.getCustomerFromRange())) {
                    if (CommonUtils.isNotEmpty(arReportsForm.getCustomerToRange())) {
                        String range = arReportsForm.getCustomerFromRange() + "-" + arReportsForm.getCustomerToRange();
                        query.append("          and t.acct_name rlike '^[").append(range).append("]'");
                    } else {
                        query.append("          and t.acct_name like '").append(arReportsForm.getCustomerFromRange()).append("%'");
                    }
                }
                if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), ONLY)) {
                    query.append("          and t.sub_type in ('Export Agent', 'Import Agent')");
                } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getAgents(), NO)) {
                    query.append("          and (t.sub_type is null or t.sub_type not in ('Export Agent', 'Import Agent'))");
                }
            } else {
                if (CommonUtils.isEmpty(arReportsForm.getCollector())) {
                    if (arReportsForm.isMaster()) {
                        query.append("          and (");
                        query.append("            t.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                        query.append("            or t.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                        query.append("          )");
                    } else {
                        query.append("          and t.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
                    }
                }
            }
        }
        query.append("        ) as i ");
        query.append("        join ar_transaction_history h ");
        query.append("          on (");
        query.append("            i.customer_number = h.customer_number ");
        query.append("            and i.invoice_or_bl = h.invoice_or_bl");
        query.append("          ) ");
        query.append("      where");
        if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            query.append("        h.transaction_type not in (").append(paymentTypes).append(")");
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), ONLY)) {
            query.append("        h.transaction_type = 'NS INV'");
        } else if (CommonUtils.isEqualIgnoreCase(arReportsForm.getNetsett(), NO)) {
            query.append("        h.transaction_type not in (").append(paymentTypes).append(", 'NS INV')");
        } else {
            query.append("        h.transaction_type not in (").append(paymentTypes).append(")");
        }
        query.append("        and h.posted_date <= '").append(cutOffDate).append("' ");
        query.append("      group by h.customer_number,");
        query.append("        h.invoice_or_bl) as h ");
        query.append("      left join ar_transaction_history p ");
        query.append("        on (");
        query.append("          h.customer_number = p.customer_number ");
        query.append("          and h.invoice_or_bl = p.invoice_or_bl ");
        query.append("          and p.transaction_type in (").append(paymentTypes).append(") ");
        query.append("          and p.posted_date <= '").append(paymentDate).append("'");
        query.append("        ) ");
        query.append("    group by h.customer_number,");
        query.append("      h.invoice_or_bl) as h ");
        query.append("  where");
        if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            query.append("    datediff('").append(cutOffDate).append("', h.invoice_date) > 21 ");
            query.append("    and h.invoice_balance > 0.00) as h ");
        } else {
            query.append("    h.invoice_balance <> 0.00) as h ");
        }
        query.append("  left join trading_partner m ");
        query.append("    on (m.acct_no = h.masteracct_no) ");
        query.append("  left join cust_accounting a ");
        query.append("    on (h.customer_number = a.acct_no) ");
        query.append("  left join cust_general_info g ");
        query.append("    on (h.customer_number = g.acct_no) ");
        query.append("  left join genericcode_dup c ");
        query.append("    on (a.credit_status = c.id) ");
        query.append("  left join genericcode_dup s ");
        query.append("    on (g.sales_code = s.id) ");
        query.append("  left join user_details u ");
        query.append("    on (a.ar_contact_code = u.user_id) ");
        query.append("group by h.customer_number,");
        query.append("  h.invoice_or_bl ");
        query.append("order by h.customer_name,");
        query.append("  h.customer_number,");
        query.append("  h.invoice_date ");

        return query.toString();
    }

    /**
     * Return List of Report Bean objects of accounts configured for statements
     *
     * @param arReportsForm
     * @return aging transactions
     * @throws Exception
     */
    public List<ReportBean> getAgingTransactions(ArReportsForm arReportsForm) throws Exception {
        String queryString;
        if (CommonUtils.isEqual(arReportsForm.getReportType(), SUMMARY)) {
            queryString = buildAgingSummaryQuery(arReportsForm);
        } else {
            queryString = buildAgingDetailQuery(arReportsForm);
        }
        if (CommonUtils.isNotEmpty(queryString)) {
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
            return query.list();
        } else {
            return new ArrayList<ReportBean>();
        }
    }

    /**
     * Build and return AR notes query
     *
     * @param arReportsForm
     * @return AR notes query
     * @throws java.lang.Exception
     */
    public String buildNotesQuery(ArReportsForm arReportsForm) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("upper(note.note_desc) as notesDescription,");
        queryBuilder.append("date_format(note.followup_date,'%m/%d/%Y') as followupDate,");
        queryBuilder.append("upper(note.updated_by) as createdBy,");
        queryBuilder.append("date_format(note.updatedate,'%m/%d/%Y') as createdDate");
        queryBuilder.append(" from notes note");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no = note.module_ref_id");
        if (CommonUtils.isNotEmpty(arReportsForm.getAccountAssignedTo())) {
            queryBuilder.append(")");
            queryBuilder.append(" join cust_accounting ca");
            queryBuilder.append(" on (ca.acct_no = tp.acct_no)");
            queryBuilder.append(" join user_details user");
            queryBuilder.append(" on (user.user_id = ca.ar_contact_code");
            queryBuilder.append(" and user.login_name = '").append(arReportsForm.getAccountAssignedTo()).append("')");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getNotesEnteredBy())) {
            queryBuilder.append(")");
            queryBuilder.append(" join user_details user");
            queryBuilder.append(" on (user.login_name = note.updated_by");
            queryBuilder.append(" and user.user_id = '").append(arReportsForm.getNotesEnteredBy()).append("')");
        } else {
            queryBuilder.append(" and tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("')");
        }
        queryBuilder.append(" where note.module_id = 'AR_CONFIGURATION'");
        queryBuilder.append(" and note.note_type = 'manual'");
        queryBuilder.append(" and note.updated_by != 'System'");
        queryBuilder.append(" and note.updateDate between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" order by note.updateDate");
        return queryBuilder.toString();
    }

    /**
     * Return List of Report Bean objects of AR notes
     *
     * @param arReportsForm
     * @return AR notes
     * @throws Exception
     */
    public List<ReportBean> getArNotes(ArReportsForm arReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(buildNotesQuery(arReportsForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return query.list();
    }

    /**
     * Build and return DSO query
     *
     * @param arReportsForm
     * @return DSO query
     * @throws java.lang.Exception
     */
    public String buildDsoQuery(ArReportsForm arReportsForm) throws Exception {
        String paymentTypes = "'AP PY','AR PY','PP INV','OA INV','NS INV','AP INV','ADJ'";
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        Calendar currentYear = Calendar.getInstance();
        String numberOfDays = String.valueOf((currentYear.getTimeInMillis() - lastYear.getTimeInMillis()) / (24 * 60 * 60 * 1000));
        String fromDate = DateUtils.formatDate(lastYear.getTime(), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(currentYear.getTime(), "yyyy-MM-dd 23:59:59");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(ar.open_receivables,2) as openReceivables,");
        queryBuilder.append("format(ar.sales,2) as sales,");
        queryBuilder.append("format(((ar.open_receivables / ar.sales) * ").append(numberOfDays).append("),2) as dsoRatio");
        queryBuilder.append(" from (");
        queryBuilder.append("select if(sum(ar.open_receivables) != 0,sum(ar.open_receivables),0) as open_receivables,");
        queryBuilder.append("if(sum(ar.sales) != 0,-sum(ar.sales),0) as sales");
        queryBuilder.append(" from (");
        queryBuilder.append("select ar.customer_number as customer_number,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ar.open_receivables as open_receivables,");
        queryBuilder.append("if(sum(th.transaction_amount + th.adjustment_amount) !=0 ,sum(th.transaction_amount + th.adjustment_amount),0) as sales");
        queryBuilder.append(" from (");
        queryBuilder.append("select ar.customer_number as customer_number,");
        queryBuilder.append("ar.invoice_date as invoice_date,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("sum(th.transaction_amount + th.adjustment_amount) as open_receivables");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_no as customer_number,");
        queryBuilder.append("min(ar.transaction_date) as invoice_date,");
        queryBuilder.append("trim(if(ar.bill_ladding_no != '',upper(ar.bill_ladding_no),upper(ar.invoice_number))) as invoice_or_bl");
        queryBuilder.append(" from transaction ar");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ar.cust_no = tp.acct_no");
        if (CommonUtils.isEqual(arReportsForm.getDsoFilter(), "All Accounts Receivable")) {
            queryBuilder.append(" and tp.acct_no!=''");
        } else if (CommonUtils.isEqual(arReportsForm.getDsoFilter(), "By Collector")) {
            String customersForCollector = getCustomersForCollector(arReportsForm);
            if (CommonUtils.isNotEmpty(customersForCollector)) {
                queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
            } else {
                queryBuilder.append(" and tp.acct_no is null");
            }
        } else {
            queryBuilder.append(" and tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
        }
        queryBuilder.append(")");
        queryBuilder.append(" where ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" group by customer_number,invoice_or_bl");
        queryBuilder.append(") as ar");
        queryBuilder.append(" join ar_transaction_history th");
        queryBuilder.append(" on (ar.customer_number = th.customer_number");
        queryBuilder.append(" and ar.invoice_or_bl = th.invoice_or_bl)");
        queryBuilder.append(" where ar.invoice_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" group by customer_number,invoice_or_bl");
        queryBuilder.append(") as ar");
        queryBuilder.append(" left join ar_transaction_history th");
        queryBuilder.append(" on (ar.customer_number = th.customer_number");
        queryBuilder.append(" and ar.invoice_or_bl = th.invoice_or_bl");
        queryBuilder.append(" and th.transaction_type in (").append(paymentTypes).append("))");
        queryBuilder.append(" group by customer_number,invoice_or_bl");
        queryBuilder.append(") as ar");
        queryBuilder.append(") as ar");
        return queryBuilder.toString();
    }

    /**
     * Return ReportBean object of DSO calculation
     *
     * @param arReportsForm
     * @return DSO
     * @throws Exception
     */
    public ReportBean getDsoModel(ArReportsForm arReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(buildDsoQuery(arReportsForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return (ReportBean) query.uniqueResult();
    }

    /**
     * Build and return No Credit query
     *
     * @param arReportsForm
     * @return No Credit query
     * @throws java.lang.Exception
     */
    public String buildNoCreditQuery(ArReportsForm arReportsForm) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append("  upper(t.acct_no) as customerNumber,");
        query.append("  upper(t.acct_name) as customerName,");
        query.append("  upper(u.login_name) as collector,");
        query.append("  if(f.bolid <> '', substring_index(f.billing_terminal, '-', -1), if(l.file_number <> '', substring_index(lb.billing_terminal, '-', -1), if(i.invoice_number like '03%', substring(i.invoice_number, 3, 2), if(i.bill_ladding_no regexp '^[0-9][0-9]', substring(i.bill_ladding_no, 1, 2), '')))) as billingTerminal,");
        query.append("  upper(if(i.invoice_or_bl like '%04-%', concat('04', substring_index(i.invoice_or_bl, '-04-', -1)), i.invoice_or_bl)) as invoiceOrBl,");
        query.append("  date_format(i.invoice_date, '%m/%d/%Y') as invoiceDate,");
        query.append("  format(i.amount, 2) as invoiceAmount,");
        query.append("  format(i.balance, 2) as balance ");
        query.append("from");
        query.append("  (select ");
        query.append("    i.cust_no as customer_number,");
        query.append("    i.bill_ladding_no as bill_ladding_no,");
        query.append("    i.invoice_number as invoice_number,");
        query.append("    if(i.bill_ladding_no <> '', i.bill_ladding_no, i.invoice_number) as invoice_or_bl,");
        query.append("    i.drcpt as drcpt,");
//        query.append("    cast(replace(substring(i.drcpt, locate('04', i.drcpt) + 2), '-R', '') as char character set latin1) as file_no,");
        query.append("    i.drcpt as file_no,");
        query.append("    i.transaction_date as invoice_date,");
        query.append("    i.transaction_amt as amount,");
        query.append("    i.balance as balance,");
        query.append("    a.ar_contact_code as ar_contact_code ");
        query.append("  from");
        query.append("    transaction i ");
        query.append("    join cust_accounting a ");
        query.append("      on (");
        query.append("        i.cust_no = a.acct_no ");
        query.append("        and a.credit_status = (select id from genericcode_dup where codedesc = 'No Credit' and codetypeid = (select codetypeid from codetype where description = 'Credit Status' limit 1) limit 1)");
        query.append("      ) ");
        query.append("  where i.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        query.append("    and i.transaction_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        query.append("    and i.balance <> 0.00) as i");
        query.append("  join trading_partner t");
        query.append("    on (i.customer_number = t.acct_no)");
        query.append("  left join user_details u");
        query.append("    on (i.ar_contact_code = u.user_id)");
        query.append("  left join fcl_bl f");
        query.append("    on (i.file_no = f.file_no)");
        query.append("  left join lcl_file_number l");
        query.append("    on (i.drcpt = l.file_number)");
        query.append("  left join lcl_booking lb");
        query.append("    on (l.id = lb.file_number_id) ");
        query.append("order by i.customer_number, i.invoice_date, i.invoice_or_bl");
        return query.toString();
    }

    /**
     * Return List of Report Bean objects of No Credit Invoices
     *
     * @param arReportsForm
     * @return No Credit Invoices
     * @throws Exception
     */
    public List<ReportBean> getNoCreditInvoices(ArReportsForm arReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(buildNoCreditQuery(arReportsForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return query.list();
    }

    private String buildActivityQuery(ArReportsForm arReportsForm) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String paymentTypes = "'AP PY', 'AR PY', 'AP INV', 'ADJ'";
        String customersForCollector = getCustomersForCollector(arReportsForm);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  ar.customer_number as customerNumber,");
        queryBuilder.append("  ar.customer_name as customerName,");
        queryBuilder.append("  ar.invoice_or_bl as invoiceOrBl,");
        queryBuilder.append("  date_format(ar.invoice_date, '%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("  format(ar.invoice_amount, 2) as invoiceAmount,");
        queryBuilder.append("  format(ar.invoice_balance, 2) as balance,");
        queryBuilder.append("  date_format(ar.date_paid, '%m/%d/%Y') as paymentDate,");
        queryBuilder.append("  coalesce((select tl.subledger_source_code from transaction_ledger tl where ar.customer_number = tl.cust_no and ar.invoice_or_bl = tl.bill_ladding_no and tl.subledger_source_code like 'AR-%' limit 1), (select tl.subledger_source_code from transaction_ledger tl where ar.customer_number = tl.cust_no and ar.invoice_or_bl = tl.invoice_number and tl.subledger_source_code like 'AR-%' limit 1), (select tl.subledger_source_code from transaction_ledger tl where ar.customer_number = tl.cust_no and ar.invoice_or_bl = tl.invoice_number and tl.subledger_source_code = 'NET SETT' limit 1), (select tl.subledger_source_code from transaction_ledger tl where ar.customer_number = tl.cust_no and ar.invoice_or_bl = tl.invoice_number and tl.subledger_source_code = 'PJ' and tl.transaction_type = 'AR' limit 1)) as type,");
        queryBuilder.append("  upper(ud.login_name) as collector ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    ar.customer_number as customer_number,");
        queryBuilder.append("    ar.customer_name as customer_name,");
        queryBuilder.append("    ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("    ar.invoice_date as invoice_date,");
        queryBuilder.append("    sum(");
        queryBuilder.append("      if(");
        queryBuilder.append("        th.transaction_type not in (").append(paymentTypes).append("),");
        queryBuilder.append("        th.transaction_amount + adjustment_amount,");
        queryBuilder.append("        0");
        queryBuilder.append("      )");
        queryBuilder.append("    ) as invoice_amount,");
        queryBuilder.append("    sum(");
        queryBuilder.append("      th.transaction_amount + adjustment_amount");
        queryBuilder.append("    ) as invoice_balance,");
        queryBuilder.append("    max(");
        queryBuilder.append("      if(");
        queryBuilder.append("        th.transaction_type in (").append(paymentTypes).append("),");
        queryBuilder.append("        th.transaction_date,");
        queryBuilder.append("        null");
        queryBuilder.append("      )");
        queryBuilder.append("    ) as date_paid");
        queryBuilder.append("  from");
        queryBuilder.append("    (select ");
        queryBuilder.append("      tp.acct_no as customer_number,");
        queryBuilder.append("      tp.acct_name as customer_name,");
        queryBuilder.append("      min(ar.transaction_date) as invoice_date,");
        queryBuilder.append("      trim(");
        queryBuilder.append("        if(");
        queryBuilder.append("          ar.bill_ladding_no != '',");
        queryBuilder.append("          upper(ar.bill_ladding_no),");
        queryBuilder.append("          upper(ar.invoice_number)");
        queryBuilder.append("        )");
        queryBuilder.append("      ) as invoice_or_bl");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction ar");
        queryBuilder.append("      join trading_partner tp");
        queryBuilder.append("        on (");
        queryBuilder.append("          ar.cust_no = tp.acct_no");
        if (arReportsForm.isAllCustomers()) {
            //Nothing needed to add
        } else if (CommonUtils.isNotEmpty(customersForCollector)) {
            queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
            queryBuilder.append(" and (tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
            queryBuilder.append(" or tp.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("')");
        }
        queryBuilder.append("        )");
        queryBuilder.append("    where ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and ar.posted_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append("    group by customer_number, invoice_or_bl");
        queryBuilder.append("    ) as ar");
        queryBuilder.append("    join ar_transaction_history th");
        queryBuilder.append("      on (");
        queryBuilder.append("        ar.customer_number = th.customer_number");
        queryBuilder.append("        and ar.invoice_or_bl = th.invoice_or_bl");
        queryBuilder.append("        and th.posted_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append("      )");
        queryBuilder.append("  group by ar.customer_number, ar.invoice_or_bl");
        queryBuilder.append("  ) as ar ");
        queryBuilder.append("  left join cust_accounting ca ");
        queryBuilder.append("    on (ar.customer_number = ca.acct_no)");
        queryBuilder.append("  left join user_details ud ");
        queryBuilder.append("    on (ca.ar_contact_code = ud.user_id)");
        queryBuilder.append("where (");
        queryBuilder.append("    ar.invoice_amount != 0 ");
        queryBuilder.append("    or ar.invoice_balance != 0");
        queryBuilder.append("  ) ");
        queryBuilder.append("group by ar.customer_number, ar.invoice_or_bl");
        return queryBuilder.toString();
    }

    public List<ReportBean> getActivityList(ArReportsForm arReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(buildActivityQuery(arReportsForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return query.list();
    }

    public String buildDisputeQuery(ArReportsForm arReportsForm) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String customersForCollector = getCustomersForCollector(arReportsForm);
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append(" ar.customer_number as customerNumber,");
        query.append(" ar.customer_name as customerName,");
        query.append(" ar.invoice_or_bl as invoiceOrBl,");
        query.append(" date_format(ar.invoice_date, '%m/%d/%Y') AS invoiceDate,");
        query.append(" format(ar.invoice_amount, 2) as invoiceAmount,");
        query.append(" format(ar.invoice_balance, 2) as balance,");
        query.append(" date_format(ar.dispute_date, '%m/%d/%Y') as disputedDate,");
        query.append(" ar.disputed_user as disputedUser,");
        query.append(" ar.dispute_sent_user as disputeSentUser");
        query.append(" from ");
        query.append("(select ");
        query.append(" ar.customer_number as customer_number,");
        query.append(" ar.customer_name as customer_name,");
        query.append(" ar.invoice_or_bl as invoice_or_bl,");
        query.append(" ar.invoice_date as invoice_date,");
        query.append(" ar.transaction_amt as invoice_amount,");
        query.append(" ar.Balance_In_Process as invoice_balance,");
        query.append(" ti.dispute_date as dispute_date,");
        query.append(" ti.disputed_user as disputed_user,");
        query.append(" ti.dispute_sent_user as dispute_sent_user");
        query.append(" from ");
        query.append(" (select ");
        query.append("   tp.acct_no as customer_number,");
        query.append("  tp.acct_name as customer_name,");
        query.append("  min(ar.transaction_date) as invoice_date,");
        query.append("  trim(");
        query.append("   if(");
        query.append("    ar.bill_ladding_no != '',");
        query.append("    upper(ar.bill_ladding_no),");
        query.append("    upper(ar.invoice_number)");
        query.append(" )");
        query.append(" ) as invoice_or_bl,");
        query.append(" ar.transaction_amt as transaction_amt,");
        query.append(" ar.Balance_In_Process as Balance_In_Process ");
        query.append(" from ");
        query.append(" transaction ar ");
        query.append(" join trading_partner tp ");
        query.append("  on (");
        query.append(" ar.cust_no = tp.acct_no ");
        if (arReportsForm.isAllCustomers()) {
            //Nothing needed to add
        } else if (CommonUtils.isNotEmpty(customersForCollector)) {
            query.append(" and tp.acct_no in (").append(customersForCollector).append(")");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
            query.append(" and (tp.acct_no = '").append(arReportsForm.getCustomerNumber()).append("'");
            query.append(" or tp.masteracct_no = '").append(arReportsForm.getCustomerNumber()).append("')");
        }
        query.append(" ) ");
        query.append(" where ar.Transaction_type = 'AR' ");
        query.append(" and ar.status = 'Dispute' ");
        query.append("  and ar.ap_invoice_id is null ");
        query.append("   and ar.transaction_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        query.append(" group by customer_number,");
        query.append(" invoice_or_bl) as ar ");
        query.append(" join transaction_other_info ti on (ar.invoice_or_bl=ti.invoice_or_bl)");
        query.append(" group by ar.customer_number,");
        query.append(" ar.invoice_or_bl) as ar ");
        query.append(" left join cust_accounting ca ");
        query.append("   on (ar.customer_number = ca.acct_no) ");
        query.append(" group by ar.customer_number,");
        query.append(" ar.invoice_or_bl ");
        return query.toString();
    }

    public List<ReportBean> getDisputeList(ArReportsForm arReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(buildDisputeQuery(arReportsForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReportBean.class));
        return query.list();
    }
}
