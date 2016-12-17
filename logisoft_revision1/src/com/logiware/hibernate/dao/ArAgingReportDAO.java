package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cvst.logisoft.struts.form.AgingReportForm;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArAgingReportDAO extends BaseHibernateDAO implements ConstantsInterface {

    private String getArCustomerForCollector(String collectorId)throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select concat(\"'\",acct_no,\"'\") from cust_accounting");
        if (null != collectorId) {
            queryBuilder.append(" where ar_contact_code=").append(collectorId);
        } else {
            queryBuilder.append(" where ar_contact_code is not null");
        }
        queryBuilder.append(" order by acct_no");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "").replace("]", "");
        }
        return null;
    }

    private String buildSummaryQuery(AgingReportForm agingReportForm)throws Exception {
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(agingReportForm.getDateRangeTo(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String paymentDate = cutOffDate;
        String customersForCollector = null;
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getNoPaymentDate(), ON)) {
            paymentDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59");
        }
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAllCollectors(), ON)) {
            customersForCollector = getArCustomerForCollector(null);
        } else if (CommonUtils.isNotEmpty(agingReportForm.getCollector())) {
            customersForCollector = getArCustomerForCollector(agingReportForm.getCollector());
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ar.customer_number as customerNumber,");
        queryBuilder.append("ar.customer_name as customerName,");
        queryBuilder.append("ar.bluescreen_account as blueScreenAccount,");
        queryBuilder.append("user.login_name as collector,");
        queryBuilder.append("if(ca.credit_limit!=0 and gen.codedesc!='No Credit',gen.codedesc,'No Credit') as creditStatus,");
        queryBuilder.append("format(if(ca.credit_limit!=0 and gen.codedesc!='No Credit',ca.credit_limit,0),2) as creditLimit,");
        queryBuilder.append("format(ar.age30Days,2) as age30Days,");
        queryBuilder.append("format(ar.age60Days,2) as age60Days,");
        queryBuilder.append("format(ar.age90Days,2) as age90Days,");
        queryBuilder.append("format(ar.age91Days,2) as age91Days,");
        queryBuilder.append("format(ar.ageTotal,2) as ageTotal");
        queryBuilder.append(" from (");
        queryBuilder.append("select ar.customer_number as customer_number,");
        queryBuilder.append("ar.customer_name as customer_name,");
        queryBuilder.append("ar.bluescreen_account as bluescreen_account,");
        queryBuilder.append("ar.invoice_date as invoice_date,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ar.invoice_date)<=30,ar.balance,0)) as age30Days,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ar.invoice_date)>=31");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',ar.invoice_date)<=60,ar.balance,0)) as age60Days,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ar.invoice_date)>=61");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',ar.invoice_date)<=90,ar.balance,0)) as age90Days,");
        queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ar.invoice_date)>=91,ar.balance,0)) as age91Days,");
        queryBuilder.append("sum(ar.balance) as ageTotal");
        queryBuilder.append(" from (");
        queryBuilder.append("select ar.customer_number as customer_number,");
        queryBuilder.append("ar.customer_name as customer_name,");
        queryBuilder.append("ar.bluescreen_account as bluescreen_account,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ar.invoice_date as invoice_date,");
        queryBuilder.append("sum(th.transaction_amount+th.adjustment_amount) as balance");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_no as customer_number,");
        queryBuilder.append("tp.acct_name as customer_name,");
        queryBuilder.append("tp.eci_acct_no as bluescreen_account,");
        queryBuilder.append("upper(trim(if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number))) as invoice_or_bl,");
        queryBuilder.append("tr.Transaction_date as invoice_date");
        queryBuilder.append(" from transaction  tr");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no=tr.cust_no");
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
        queryBuilder.append(")");
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAllCustomersCheck(), ON)
                || CommonUtils.isNotEmpty(customersForCollector)) {
            if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAgents(), "onlyAgents")) {
                queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
            } else if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAgents(), "agentsNotIncluded")) {
                queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
            }
        }
        if ((CommonUtils.isNotEqual(agingReportForm.getCollector(), "0"))
                || (CommonUtils.isEqual(agingReportForm.getAllCollectors(), ON))) {
            queryBuilder.append(" join cust_accounting ca");
            queryBuilder.append(" on (ca.acct_no=tp.acct_no");
            if (CommonUtils.isNotEqual(agingReportForm.getCollector(), "0")) {
                queryBuilder.append(" and ca.ar_contact_code = ").append(agingReportForm.getCollector());
            } else {
                queryBuilder.append(" and ca.ar_contact_code is not null");
            }
            queryBuilder.append(")");
        }
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" group by customer_number,invoice_or_bl");
        queryBuilder.append(" order by customer_name,customer_number,invoice_date asc,invoice_or_bl");
        queryBuilder.append(") as ar");
        queryBuilder.append(" join ar_transaction_history th");
        queryBuilder.append(" on (ar.customer_number=th.customer_number");
        queryBuilder.append(" and ar.invoice_or_bl=th.invoice_or_bl");
        queryBuilder.append(" and ((th.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and th.posted_date<='").append(cutOffDate).append("')");
        queryBuilder.append(" or (th.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and th.posted_date<='").append(paymentDate).append("')))");
        queryBuilder.append(" group by ar.customer_number,ar.invoice_or_bl");
        queryBuilder.append(" order by ar.customer_name,ar.customer_number");
        queryBuilder.append(") as ar");
        queryBuilder.append(" where ar.balance!=0.00");
        queryBuilder.append(" group by ar.customer_number");
        queryBuilder.append(" order by ar.customer_name,ar.customer_number");
        queryBuilder.append(") as ar");
        queryBuilder.append(" left join cust_accounting ca");
        queryBuilder.append(" on (ar.customer_number=ca.acct_no)");
        queryBuilder.append(" left join genericcode_dup gen");
        queryBuilder.append(" on (ca.credit_status=gen.id)");
        queryBuilder.append(" left join user_details user");
        queryBuilder.append(" on (ca.ar_contact_code=user.user_id)");
        queryBuilder.append(" where ar.ageTotal!=0.00");
        queryBuilder.append(" group by ar.customer_number");
        queryBuilder.append(" order by ar.customer_name,ar.customer_number");
        return queryBuilder.toString();
    }

    public List<ReportBean> getSummaryTransactions(AgingReportForm agingReportForm)throws Exception {
        String query = buildSummaryQuery(agingReportForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    private String buildDetailsQuery(AgingReportForm agingReportForm)throws Exception {
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(agingReportForm.getDateRangeTo(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String paymentDate = cutOffDate;
        String customersForCollector = null;
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getNoPaymentDate(), ON)) {
            paymentDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59");
        }
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAllCollectors(), ON)) {
            customersForCollector = getArCustomerForCollector(null);
        } else if (CommonUtils.isNotEmpty(agingReportForm.getCollector())) {
            customersForCollector = getArCustomerForCollector(agingReportForm.getCollector());
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ar.customer_number as customerNumber,");
        queryBuilder.append("ar.customer_name as customerName,");
        queryBuilder.append("ar.bluescreen_account as blueScreenAccount,");
        queryBuilder.append("if(ar.bl_number like '%04-%',");
        queryBuilder.append("replace(replace(ar.bl_number,substring_index(ar.bl_number,'04-',1),''),'-',''),ar.bl_number) as blNumber,");
        queryBuilder.append("ar.invoice_number as invoiceNumber,");
        queryBuilder.append("date_format(ar.invoice_date,'%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("ar.customer_reference as customerReference,");
        queryBuilder.append("ar.voyage_number as voyageNumber,");
        queryBuilder.append("format(if(datediff('").append(cutOffDate).append("',ar.invoice_date)<=30,ar.balance,0),2) as age30Days,");
        queryBuilder.append("format(if(datediff('").append(cutOffDate).append("',ar.invoice_date)>=31");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',ar.invoice_date)<=60,ar.balance,0),2) as age60Days,");
        queryBuilder.append("format(if(datediff('").append(cutOffDate).append("',ar.invoice_date)>=61");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',ar.invoice_date)<=90,ar.balance,0),2) as age90Days,");
        queryBuilder.append("format(if(datediff('").append(cutOffDate).append("',ar.invoice_date)>=91,ar.balance,0),2) as age91Days,");
        queryBuilder.append("format(ar.balance,2) as ageTotal");
        queryBuilder.append(" from (");
        queryBuilder.append("select ar.customer_number as customer_number,");
        queryBuilder.append("ar.customer_name as customer_name,");
        queryBuilder.append("ar.bluescreen_account as bluescreen_account,");
        queryBuilder.append("ar.bl_number as bl_number,");
        queryBuilder.append("ar.invoice_number as invoice_number,");
        queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ar.invoice_date as invoice_date,");
        queryBuilder.append("ar.customer_reference as customer_reference,");
        queryBuilder.append("ar.voyage_number as voyage_number,");
        queryBuilder.append("sum(th.transaction_amount+th.adjustment_amount) as balance");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_no as customer_number,");
        queryBuilder.append("tp.acct_name as customer_name,");
        queryBuilder.append("tp.eci_acct_no as bluescreen_account,");
        queryBuilder.append("upper(tr.bill_ladding_no) as bl_number,");
        queryBuilder.append("upper(tr.invoice_number) as invoice_number,");
        queryBuilder.append("upper(trim(if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number))) as invoice_or_bl,");
        queryBuilder.append("tr.Transaction_date as invoice_date,");
        queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
        queryBuilder.append("trim(tr.voyage_no) as voyage_number");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no=tr.cust_no");
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
        queryBuilder.append(")");
        if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAllCustomersCheck(), ON)
                || CommonUtils.isNotEmpty(customersForCollector)) {
            if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAgents(), "onlyAgents")) {
                queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
            } else if (CommonUtils.isEqualIgnoreCase(agingReportForm.getAgents(), "agentsNotIncluded")) {
                queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
            }
        }
        if ((CommonUtils.isNotEqual(agingReportForm.getCollector(), "0"))
                || (CommonUtils.isEqual(agingReportForm.getAllCollectors(), ON))) {
            queryBuilder.append(" join cust_accounting ca");
            queryBuilder.append(" on (ca.acct_no=tp.acct_no");
            if (CommonUtils.isNotEqual(agingReportForm.getCollector(), "0")) {
                queryBuilder.append(" and ca.ar_contact_code = ").append(agingReportForm.getCollector());
            } else {
                queryBuilder.append(" and ca.ar_contact_code is not null");
            }
            queryBuilder.append(")");
        }
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" group by customer_number,invoice_or_bl");
        queryBuilder.append(" order by customer_name,customer_number,invoice_date asc,invoice_or_bl");
        queryBuilder.append(") as ar");
        queryBuilder.append(" join ar_transaction_history th");
        queryBuilder.append(" on (ar.customer_number=th.customer_number");
        queryBuilder.append(" and ar.invoice_or_bl=th.invoice_or_bl");
        queryBuilder.append(" and ((th.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and th.posted_date<='").append(cutOffDate).append("')");
        queryBuilder.append(" or (th.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and th.posted_date<='").append(paymentDate).append("')))");
        queryBuilder.append(" group by ar.customer_number,ar.invoice_or_bl");
        queryBuilder.append(" order by ar.customer_name,ar.customer_number,ar.invoice_date asc,ar.invoice_or_bl");
        queryBuilder.append(") as ar");
        queryBuilder.append(" where ar.balance!=0.00");
        queryBuilder.append(" order by ar.customer_name,ar.customer_number,ar.invoice_date asc,ar.invoice_or_bl");
        return queryBuilder.toString();
    }

    public List<ReportBean> getDetailTransactions(AgingReportForm agingReportForm)throws Exception {
        String query = buildDetailsQuery(agingReportForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public CustomerBean getCustomerDetails(String customerNumber)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("tp.acct_type as accountType,");
        queryBuilder.append("tp.eci_acct_no as blueScreenAccount,");
        queryBuilder.append("concat(if(cust_addr.address1!='',concat(cust_addr.address1,'\n'),''),");
        queryBuilder.append("if(cust_addr.city1!='' && cust_addr.state!='',concat(cust_addr.city1,', ',cust_addr.state),");
        queryBuilder.append("if(cust_addr.city1!='',cust_addr.city1,if(cust_addr.state!='',cust_addr.state,''))),");
        queryBuilder.append("if(cust_addr.zip!='',concat(' ',cust_addr.zip),'')) as address,");
        queryBuilder.append("ca.ar_phone as phone,");
        queryBuilder.append("ca.acct_rec_email as email,");
        queryBuilder.append("ca.ar_fax as fax");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" left join cust_accounting ca on ca.acct_no=tp.acct_no");
        queryBuilder.append(" left join cust_address cust_addr on ca.cust_address_id = cust_addr.id");
        queryBuilder.append(" where tp.acct_no = '").append(customerNumber).append("' limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(CustomerBean.class));
        return (CustomerBean) query.uniqueResult();
    }
}
