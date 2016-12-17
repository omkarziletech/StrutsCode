package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApReportsDAO extends BaseHibernateDAO implements ConstantsInterface {

    public CustomerBean getVendorDetails(String vendorNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select tp.acct_no as customerNumber,tp.acct_name as customerName,tp.sub_type as subType,");
        queryBuilder.append("tp.ecu_designation as ecuDesignation,");
        queryBuilder.append("concat(if(ca.address1!='',concat(ca.address1,'\n'),''),");
        queryBuilder.append("if(ca.city1!='' && ca.state!='' && country.codedesc!='',concat(ca.city1,', ',ca.state,', ',country.codedesc),");
        queryBuilder.append("if(ca.city1!='' && ca.state!='',concat(ca.city1,', ',ca.state),");
        queryBuilder.append("if(ca.city1!='' && country.codedesc!='',concat(ca.city1,', ',country.codedesc),");
        queryBuilder.append("if(ca.city1!='',ca.city1,if(ca.state!='',ca.state,if(country.codedesc!='',country.codedesc,''))))))) as address,");
        queryBuilder.append("ca.zip as zipCode,ve.company as contact,ve.phone as phone,ve.fax as fax,ve.email as email,");
        queryBuilder.append("format(if(ve.c_limit!='',ve.c_limit,0),2) as creditLimit,creditTerm.codedesc as creditTerm,");
        queryBuilder.append(" ud.login_name as apSpecialist");
        queryBuilder.append(" from trading_partner tp join vendor_info ve on ve.cust_accno=tp.acct_no");
        queryBuilder.append(" left join cust_address ca on ca.acct_no=tp.acct_no");
        queryBuilder.append(" left join genericcode_dup country on country.id=ca.country");
        queryBuilder.append(" left join genericcode_dup creditTerm on creditTerm.id=ve.credit_terms");
        queryBuilder.append(" left join user_details ud on ud.user_id=ve.ap_specialist");
        queryBuilder.append(" where tp.acct_no='").append(vendorNumber).append("'");
        queryBuilder.append(" order by field(ca.prime,'on') desc limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(CustomerBean.class));
        return (CustomerBean) query.uniqueResult();
    }

    private String buildApAgingQuery(ApReportsForm apReportsForm, String cutOffDate) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(t.vendor_name) as vendorName,upper(t.vendor_number) as vendorNumber,");
        queryBuilder.append("t.transaction_type as transactionType,t.ecu_designation as ecuDesignation,");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append("upper(t.invoice_number) as invoiceNumber,date_format(t.invoice_date,'%m/%d/%Y') as invoiceDate,");
            queryBuilder.append("t.vendor_reference as vendorReference,");
        }
        queryBuilder.append("format(sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)<=30,t.amount,0.00)),2) as age30Days,");
        queryBuilder.append("format(sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=31");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',t.invoice_date)<=60 ,t.amount,0.00)),2) as age60Days,");
        queryBuilder.append("format(sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=61");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',t.invoice_date)<=90 ,t.amount,0.00)),2) as age90Days,");
        queryBuilder.append("format(sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=91");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',t.invoice_date)<=120,t.amount,0.00)),2) as age120Days,");
        queryBuilder.append("format(sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=121");
        queryBuilder.append(" and datediff('").append(cutOffDate).append("',t.invoice_date)<=180,t.amount,0.00)),2) as age180Days,");
        queryBuilder.append("format(sum(if(datediff('").append(cutOffDate).append("',t.invoice_date)>=181,t.amount,0.00)),2) as age181Days,");
        queryBuilder.append("format(sum(t.amount),2) as ageTotal");
        queryBuilder.append(" from (select tp.acct_name as vendor_name,tp.acct_no as vendor_number,");
        queryBuilder.append("'").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("' as transaction_type,");
        queryBuilder.append("tp.ecu_designation as ecu_designation,");
        queryBuilder.append("ap.invoice_number as invoice_number,min(ap.invoice_date) as invoice_date,");
        queryBuilder.append("trim(ap.vendor_reference) as vendor_reference,sum(ap.amount) as amount");
        queryBuilder.append(" from ap_transaction_history ap");
        queryBuilder.append(" join trading_partner tp on ap.vendor_number=tp.acct_no");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES) || CommonUtils.isEmpty(apReportsForm.getVendorNumber())) {
            queryBuilder.append(" and tp.acct_no!=''");
        } else {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
        }
        queryBuilder.append(" where ap.posted_date<='").append(cutOffDate).append("' and ap.amount!=0.00");
        queryBuilder.append(" and (ap.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" or ap.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("')");
        if (CommonUtils.isNotEmpty(apReportsForm.getGlAccount())) {
            queryBuilder.append(" and ap.gl_account like '%").append(apReportsForm.getGlAccount().trim()).append("%'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getCostCode())) {
            queryBuilder.append(" and ap.charge_code like '%").append(apReportsForm.getCostCode().trim()).append("%'");
        }
        queryBuilder.append(" group by vendor_number,invoice_number");
        queryBuilder.append(" order by vendor_number,invoice_number) as t where t.amount!=0");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append(" group by t.vendor_number,t.invoice_number");
            queryBuilder.append(" order by t.vendor_number,t.invoice_number");
        } else {
            queryBuilder.append(" group by t.vendor_number");
            queryBuilder.append(" order by t.vendor_number");
        }
        return queryBuilder.toString();
    }

    private String buildAcAgingQuery(ApReportsForm apReportsForm, String cutOffDate) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.vendor_name as vendorName,t.vendor_number as vendorNumber,t.transaction_type as transactionType,");
        queryBuilder.append("t.ecu_designation as ecuDesignation,");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append("t.invoice_number as invoiceNumber,date_format(t.invoice_date,'%m/%d/%Y') as invoiceDate,");
            queryBuilder.append("t.vendor_reference as vendorReference,");
        }
        queryBuilder.append("format(t.30days,2) as age30Days,format(t.60days,2) as age60Days,format(t.90days,2) as age90Days,");
        queryBuilder.append("format(t.120days,2) as age120Days,format(t.180days,2) as age180Days,format(t.181days,2) as age181Days,");
        queryBuilder.append("format((t.30days+t.60days+t.90days+t.180days+t.181days),2) as ageTotal");
        queryBuilder.append(" from (select tp.acct_name as vendor_name,tp.acct_no as vendor_number,");
        queryBuilder.append("'").append(TRANSACTION_TYPE_ACCRUALS).append("' as transaction_type,");
        queryBuilder.append("tp.ecu_designation as ecu_designation,");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append("ac.invoice_number as invoice_number,ac.transaction_date as invoice_date,");
            queryBuilder.append("trim(if(ac.drcpt!=0.00,ac.drcpt,ac.bill_ladding_no)) as vendor_reference,");
            queryBuilder.append("if(datediff('").append(cutOffDate).append("',ac.transaction_date)<=30,ac.transaction_amt,0.00) as 30days,");
            queryBuilder.append("if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=31");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=60 ,ac.transaction_amt,0.00) as 60days,");
            queryBuilder.append("if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=61");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=90 ,ac.transaction_amt,0.00) as 90days,");
            queryBuilder.append("if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=91");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=120 ,ac.transaction_amt,0.00) as 120days,");
            queryBuilder.append("if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=121");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=180 ,ac.transaction_amt,0.00) as 180days,");
            queryBuilder.append("if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=181,ac.transaction_amt,0.00) as 181days");
        } else {
            queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ac.transaction_date)<=30,ac.transaction_amt,0.00)) as 30days,");
            queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=31");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=60 ,ac.transaction_amt,0.00)) as 60days,");
            queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=61");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=90 ,ac.transaction_amt,0.00)) as 90days,");
            queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=91");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=120 ,ac.transaction_amt,0.00)) as 120days,");
            queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=121");
            queryBuilder.append(" and datediff('").append(cutOffDate).append("',ac.transaction_date)<=180 ,ac.transaction_amt,0.00)) as 180days,");
            queryBuilder.append("sum(if(datediff('").append(cutOffDate).append("',ac.transaction_date)>=181,ac.transaction_amt,0.00)) as 181days");
        }
        queryBuilder.append(" from transaction_ledger ac");
        queryBuilder.append(" join trading_partner tp on ac.cust_no=tp.acct_no");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES) || CommonUtils.isEmpty(apReportsForm.getVendorNumber())) {
            queryBuilder.append(" and tp.acct_no!=''");
        } else {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
        }
        queryBuilder.append(" where ac.transaction_date<='").append(cutOffDate).append("'");
        queryBuilder.append(" and ac.transaction_amt!=0.00 and ac.balance!=0.00");
        queryBuilder.append(" and ac.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.subledger_source_code='").append(SUB_LEDGER_CODE_ACCRUALS).append("'");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getTransactionType(), BOTH)
                || CommonUtils.isEqualIgnoreCase(apReportsForm.getTransactionType(), TRANSACTION_TYPE_ACCRUALS)) {
            queryBuilder.append(" and (ac.status='").append(STATUS_OPEN).append("'  or ac.status='").append(STATUS_DISPUTE).append("'");
            queryBuilder.append(" or ac.status='").append(STATUS_IN_PROGRESS).append("' or ac.status='").append(STATUS_PENDING).append("')");
        } else {
            queryBuilder.append(" and ac.status='").append(STATUS_INACTIVE).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getGlAccount())) {
            queryBuilder.append(" and ac.gl_account_number like '%").append(apReportsForm.getGlAccount().trim()).append("%'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getCostCode())) {
            queryBuilder.append(" and ac.charge_code like '%").append(apReportsForm.getCostCode().trim()).append("%'");
        }
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append(" order by vendor_number,invoice_date");
        } else {
            queryBuilder.append(" group by vendor_number");
            queryBuilder.append(" order by vendor_number");
        }
        queryBuilder.append(") as t");
        return queryBuilder.toString();
    }

    public List<ReportBean> getAgingTransactions(ApReportsForm apReportsForm) throws Exception {
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getCutOffDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getTransactionType(), BOTH)) {
            String apQuery = buildApAgingQuery(apReportsForm, cutOffDate);
            String acQuery = buildAcAgingQuery(apReportsForm, cutOffDate);
            queryBuilder.append("(").append(apQuery).append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(").append(acQuery).append(")");
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            String apQuery = buildApAgingQuery(apReportsForm, cutOffDate);
            queryBuilder.append("(").append(apQuery).append(")");
        } else {
            String acQuery = buildAcAgingQuery(apReportsForm, cutOffDate);
            queryBuilder.append("(").append(acQuery).append(")");
        }
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public String buildAdjustedAccrualsQuery(ApReportsForm apReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(ac.bill_ladding_no != '',ac.bill_ladding_no,if(ac.voyage_no!='',ac.voyage_no,ac.drcpt)) as file,");
        queryBuilder.append("date_format(ac.sailing_date,'%m/%d/%Y') as reportingDate,date_format(ac.posted_date,'%m/%d/%Y') as postedDate,");
        queryBuilder.append("tp.acct_name as vendorName,tp.acct_no as vendorNumber,");
        queryBuilder.append("format(ac.original_amount,2) as originalAmount,format(ac.transaction_amt,2) as assignedAmount,");
        queryBuilder.append("format((ac.original_amount-ac.transaction_amt),2) as differenceAmount");
        queryBuilder.append(" from transaction_ledger ac");
        queryBuilder.append(" join trading_partner tp on tp.acct_no=ac.cust_no");
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorName())) {
            queryBuilder.append(" and tp.acct_name='").append(apReportsForm.getVendorName().trim()).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
        }
        queryBuilder.append(" where ac.status='").append(STATUS_ASSIGN).append("'");
        if (CommonUtils.isNotEmpty(apReportsForm.getFromDate()) || CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
            if (CommonUtils.isNotEmpty(apReportsForm.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                queryBuilder.append(" and ac.").append(apReportsForm.getSearchDateBy()).append(">='").append(fromDate).append("'");
            }
            if (CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                queryBuilder.append(" and ac.").append(apReportsForm.getSearchDateBy()).append("<='").append(toDate).append("'");
            }
        }
        queryBuilder.append(" order by ac.").append(apReportsForm.getSearchDateBy());
        return queryBuilder.toString();
    }

    public List<ReportBean> getAdjustedAccruals(ApReportsForm apReportsForm) throws Exception {
        String query = buildAdjustedAccrualsQuery(apReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public String buildVendorQuery() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(tp.acct_name) as vendorName,");
        queryBuilder.append("upper(tp.acct_no) as vendorNumber,");
        queryBuilder.append("upper(tp.sub_type) as subType,");
        queryBuilder.append("upper(tp.ecivendno) as eciVendorNumber,");
        queryBuilder.append("upper(ven.tin) as tin,");
        queryBuilder.append("upper(if(ven.w9='on','true','false')) as w9,");
        queryBuilder.append("upper(if(ven.credit_status='on','true','false')) as creditStatus,");
        queryBuilder.append("upper(gen.codedesc) as creditTerms,");
        queryBuilder.append("if(ven.c_limit is not null,format(ven.c_limit,2),'') as creditLimit,");
        queryBuilder.append("upper(ven.company) as apContact,");
        queryBuilder.append("upper(user.login_name) as apSpecialist,");
        queryBuilder.append("upper(pm.pay_method) as paymentMethod,");
        queryBuilder.append("upper(if(ap.paid_count<=0,'true','false')) as notPaid,");
        queryBuilder.append("upper(if(ac.active_count<=0,'true','false')) as inactiveAccount,");
        queryBuilder.append("upper(if(ven.exempt_inactivate=1,'true','false')) as exemptInactivate,");
        queryBuilder.append("date_format(tp.enter_date,'%m/%d/%Y') as accountAddedDate");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" left join vendor_info ven on ven.cust_accno=tp.acct_no");
        queryBuilder.append(" left join genericcode_dup gen on gen.id=ven.credit_terms");
        queryBuilder.append(" left join user_details user on ven.ap_specialist = user.user_id");
        queryBuilder.append(" left join payment_method pm on (pm.pay_accno=tp.acct_no and pm.default_pay_method='Y')");
        queryBuilder.append(" left join (select ap.cust_no as acct_no,count(ap.transaction_id) as paid_count from transaction ap");
        queryBuilder.append(" where ap.transaction_type='").append(STATUS_PAID).append("' and ap.void = '").append(NO).append("'");
        queryBuilder.append(" and ap.transaction_date between date_sub(sysdate(),interval 30 day) and sysdate()");
        queryBuilder.append(" group by ap.cust_no) as ap on ap.acct_no=tp.acct_no");
        queryBuilder.append(" left join (select ac.cust_no as acct_no,count(ac.transaction_id) as active_count");
        queryBuilder.append(" from transaction_ledger ac where ac.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.subledger_source_code = '").append(SUB_LEDGER_CODE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.status!='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and ac.transaction_date between date_sub(sysdate(),interval 365 day) and sysdate()");
        queryBuilder.append(" group by ac.cust_no) as ac on ac.acct_no=tp.acct_no");
        queryBuilder.append(" where tp.acct_type like '%V%'");
        queryBuilder.append(" and (tp.inactive='").append(NO).append("' or tp.inactive is null)");
        return queryBuilder.toString();
    }

    public List<ReportBean> getVendors() throws Exception {
        String query = buildVendorQuery();
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public String buildActivityQuery(ApReportsForm apReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(user.login_name) as userLoginName,tp.acct_name as vendorName,");
        queryBuilder.append("tp.acct_no as vendorNumber,upper(ap.invoice_number) as invoiceNumber,");
        queryBuilder.append("if(ac.bill_ladding_no != '',ac.bill_ladding_no,if(ac.voyage_no!='',ac.voyage_no,ac.drcpt)) as file,");
        queryBuilder.append("format(ac.transaction_amt,2) as accrualAmount, date_format(ap.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("date_format(ap.posted_date,'%m/%d/%Y') as postedDate,");
        queryBuilder.append("concat(datediff(ap.posted_date,ap.transaction_date),'') as postedTransactionDifference,");
        queryBuilder.append("date_format(ap.check_date,'%m/%d/%Y') as paidDate,");
        queryBuilder.append("concat(datediff(ap.check_date,ap.posted_date),'') as paidPostedDifference,upper(ap.pay_method) as paymentMethod");
        queryBuilder.append(" from transaction ap");
        queryBuilder.append(" join transaction_ledger ac on (ap.invoice_number=ac.invoice_number and ap.cust_no=ac.cust_no");
        queryBuilder.append(" and ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.status in ('").append(STATUS_ASSIGN).append("', '").append(STATUS_EDI_ASSIGNED).append("'))");
        queryBuilder.append(" join trading_partner tp on tp.acct_no = ap.cust_no");
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getEcuDesignation())) {
            queryBuilder.append(" and tp.ecu_designation = '").append(apReportsForm.getEcuDesignation()).append("'");
        }
        queryBuilder.append(" join user_details user on user.user_id = ap.created_by");
        if (CommonUtils.isNotEmpty(apReportsForm.getUserId())) {
            queryBuilder.append(" and user.user_id=").append(apReportsForm.getUserId());
        }
        queryBuilder.append(" where ap.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and ap.status in ('").append(STATUS_OPEN).append("', '").append(STATUS_READY_TO_PAY).append("', '").append(STATUS_PAID).append("')");
        if (CommonUtils.isNotEmpty(apReportsForm.getFromDate()) || CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
            if (CommonUtils.isNotEmpty(apReportsForm.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                queryBuilder.append(" and ap.posted_date>='").append(fromDate).append("'");
            }
            if (CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                queryBuilder.append(" and ap.posted_date<='").append(toDate).append("'");
            }
        }
        queryBuilder.append(" order by ap.posted_date");
        return queryBuilder.toString();
    }

    public List<ReportBean> getActivities(ApReportsForm apReportsForm) throws Exception {
        String query = buildActivityQuery(apReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();

    }

    public String getAllBankGlAccounts() throws Exception {
        String query = "select concat(\"'\",gl_account_no,\"'\") as gl_account_no from bank_details group by gl_account_no";
        List<String> result = getCurrentSession().createSQLQuery(query).list();
        return result.toString().replace("[", "(").replace("]", ")");
    }

    private String buildCheckRegisterQuery(ApReportsForm apReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(py.pay_method='ACH',right(concat('000000',py.ach_batch_sequence),7),py.cheque_number) as checkNumber,");
        queryBuilder.append("tp.acct_name as vendorName,py.bank_account_no as bankAccount,tp.acct_no as vendorNumber,");
        queryBuilder.append("date_format(py.transaction_date,'%m/%d/%Y') as paymentDate,upper(py.pay_method) as paymentMethod,");
        queryBuilder.append("if(py.void='").append(YES).append("','").append(STATUS_VOID).append("',");
        queryBuilder.append("if(py.cleared='").append(YES).append("',date_format(py.cleared_date,'%m/%d/%Y'),'')) as status,");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append("format(py.transaction_amt,2) as paymentAmount,py.invoice_number as invoiceNumber");
        } else {
            queryBuilder.append("format(sum(py.transaction_amt),2) as paymentAmount");
        }
        queryBuilder.append(" from transaction py join trading_partner tp on py.cust_no=tp.acct_no");
        queryBuilder.append(" join bank_details bd on bd.gl_account_no = py.gl_account_number");
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
        }
        queryBuilder.append(" where py.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and (py.status='").append(STATUS_PAID).append("'");
        queryBuilder.append(" or py.status='").append(STATUS_SENT).append("'");
        queryBuilder.append(" or py.status='").append(STATUS_READY_TO_SEND).append("'");
        queryBuilder.append(" or py.status='").append(CLEAR).append("')");
        if (CommonUtils.isNotEqual(apReportsForm.getPaymentMethod(), ALL)) {
            queryBuilder.append(" and py.pay_method='").append(apReportsForm.getPaymentMethod()).append("'");
        } else {
            queryBuilder.append(" and (py.pay_method='").append(PAYMENT_METHOD_CHECK).append("'");
            queryBuilder.append(" or py.pay_method='").append(PAYMENT_METHOD_ACH).append("'");
            queryBuilder.append(" or py.pay_method='").append(PAYMENT_METHOD_WIRE).append("'");
            queryBuilder.append(" or py.pay_method='").append(PAYMENT_METHOD_ACH_DEBIT).append("'");
            queryBuilder.append(" or py.pay_method='").append(PAYMENT_METHOD_CREDIT_CARD).append("')");
        }
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAllBankAccount(), All_BANK_ACCOUNT)) {
            String glAccounts = getAllBankGlAccounts();
            queryBuilder.append(" and py.gl_account_number in ").append(glAccounts);
        } else if (CommonUtils.isNotEmpty(apReportsForm.getGlAccount())) {
            queryBuilder.append(" and py.gl_account_number='").append(apReportsForm.getGlAccount().trim()).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getFromCheck()) || CommonUtils.isNotEmpty(apReportsForm.getToCheck())) {
            queryBuilder.append(" and ((");
            boolean isFromChecked = false;
            if (CommonUtils.isNotEmpty(apReportsForm.getFromCheck())) {
                isFromChecked = true;
                queryBuilder.append("cast(py.cheque_number as signed integer)>='").append(apReportsForm.getFromCheck()).append("'");
            }
            if (CommonUtils.isNotEmpty(apReportsForm.getToCheck())) {
                queryBuilder.append(isFromChecked ? " and " : "");
                queryBuilder.append("cast(py.cheque_number as signed integer)<='").append(apReportsForm.getToCheck()).append("'");
            }
            queryBuilder.append(") or (");
            if (CommonUtils.isNotEmpty(apReportsForm.getFromCheck())) {
                queryBuilder.append("py.ach_batch_sequence>='").append(apReportsForm.getFromCheck()).append("'");
            }
            if (CommonUtils.isNotEmpty(apReportsForm.getToCheck())) {
                queryBuilder.append(isFromChecked ? " and " : "");
                queryBuilder.append("py.ach_batch_sequence<='").append(apReportsForm.getToCheck()).append("'");
            }
            queryBuilder.append("))");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getFromDate()) || CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
            if (CommonUtils.isNotEmpty(apReportsForm.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                queryBuilder.append(" and py.transaction_date>='").append(fromDate).append("'");
            }
            if (CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                queryBuilder.append(" and py.transaction_date<='").append(toDate).append("'");
            }
        }
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), YES)) {
            queryBuilder.append(" and (py.cleared = '").append(YES).append("' and py.status='").append(CLEAR).append("')");
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), NO)) {
            queryBuilder.append(" and (py.cleared = '").append(NO).append("' and py.reconciled='").append(STATUS_IN_PROGRESS).append("')");
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), STATUS_VOID)) {
            queryBuilder.append(" and (py.void = '").append(YES).append("' and py.status!='").append(CLEAR).append("')");
        } else {
            queryBuilder.append(" and ((py.cleared = '").append(YES).append("' and py.status='").append(CLEAR).append("')");
            queryBuilder.append(" or (py.cleared = '").append(NO).append("' and py.reconciled='").append(STATUS_IN_PROGRESS).append("')");
            queryBuilder.append(" or (py.void = '").append(YES).append("' and py.status!='").append(CLEAR).append("')");
            queryBuilder.append(" or (py.status='").append(STATUS_PAID).append("'");
            queryBuilder.append(" or py.status='").append(STATUS_SENT).append("'");
            queryBuilder.append(" or py.status='").append(STATUS_READY_TO_SEND).append("'))");
        }
        queryBuilder.append(" and py.ap_batch_id is not null");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            queryBuilder.append(" order by checkNumber,py.transaction_date,vendorNumber,invoiceNumber");
        } else {
            queryBuilder.append(" group by checkNumber,vendorNumber");
            queryBuilder.append(" order by checkNumber,py.transaction_date,vendorNumber");
        }
        return queryBuilder.toString();
    }

    public List<ReportBean> getCheckRegisterTransactions(ApReportsForm apReportsForm) throws Exception {
        String query = buildCheckRegisterQuery(apReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public String getVendorsForApSpecialist(String userId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat('\"',cust_accno,'\"')");
        queryBuilder.append(" from vendor_info");
        queryBuilder.append(" where ap_specialist=").append(userId);
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "(").replace("]", ")") : null;
    }

    public String buildDpoQuery(ApReportsForm apReportsForm) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        FiscalPeriod fromPeriod = fiscalPeriodDAO.findById(Integer.parseInt(apReportsForm.getFromPeriodId()));
        FiscalPeriod toPeriod = fiscalPeriodDAO.findById(Integer.parseInt(apReportsForm.getToPeriodId()));
        String fromDate = DateUtils.formatDate(fromPeriod.getStartDate(), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(toPeriod.getEndDate(), "yyyy-MM-dd 23:59:59");
        StringBuilder queryBuilder = new StringBuilder();
        String vendors = null;
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            vendors = getVendorsForApSpecialist(apReportsForm.getUserId());
        }
        queryBuilder.append("select format(open_payables,2) as openPayables,format(total_costs,2) as totalCosts,");
        queryBuilder.append("concat(no_of_days,'') as numberOfDays,concat(round((open_payables/total_costs)*no_of_days,2),'') as dpoRatio");
        queryBuilder.append(" from (select sum(t.open_payables) as open_payables,sum(total_costs) as total_costs,");
        queryBuilder.append("(datediff('").append(toDate).append("','").append(fromDate).append("')+1) as no_of_days");
        queryBuilder.append(" from (");
        queryBuilder.append(" (select sum(ap.transaction_amt) as open_payables,0 as total_costs");
        queryBuilder.append(" from transaction ap");
        queryBuilder.append(" join trading_partner tp on tp.acct_no=ap.cust_no");
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(ALL_ACCOUNTS_PAYABLE)) {
            queryBuilder.append(" and tp.acct_no!='' ");
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            if (CommonUtils.isNotEmpty(vendors)) {
                queryBuilder.append(" and tp.acct_no in ").append(vendors);
            } else {
                queryBuilder.append(" and tp.acct_no=''");
            }
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_VENDOR)) {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
        }
        queryBuilder.append(" where ap.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and (ap.status='").append(STATUS_OPEN).append("' or ap.status='").append(STATUS_READY_TO_PAY).append("')");
        queryBuilder.append(" and ap.transaction_date between '").append(fromDate).append("' and '").append(toDate).append("')");
        queryBuilder.append("  union ");
        queryBuilder.append("(select 0 as open_payables,sum(ac.transaction_amt) as total_costs");
        queryBuilder.append(" from transaction_ledger ac");
        queryBuilder.append(" join trading_partner tp on tp.acct_no=ac.cust_no");
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(ALL_ACCOUNTS_PAYABLE)) {
            queryBuilder.append(" and tp.acct_no!='' ");
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            if (CommonUtils.isNotEmpty(vendors)) {
                queryBuilder.append(" and tp.acct_no in ").append(vendors);
            } else {
                queryBuilder.append(" and tp.acct_no=''");
            }
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_VENDOR)) {
            queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
        }
        queryBuilder.append(" where ac.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.status='").append(STATUS_ASSIGN).append("'");
        queryBuilder.append(" and ac.transaction_date between '").append(fromDate).append("' and '").append(toDate).append("')");
        queryBuilder.append(") as t");
        queryBuilder.append(") as t");
        return queryBuilder.toString();
    }

    public ReportBean getDpo(ApReportsForm apReportsForm) throws Exception {
        String query = buildDpoQuery(apReportsForm);
        return (ReportBean) getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).uniqueResult();
    }

    public String buildDisuputedItemsQuery(ApReportsForm apReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(tp.acct_name) as vendorName,upper(tp.acct_no) as vendorNumber,upper(ap.invoice_number) as invoiceNumber,");
        queryBuilder.append("date_format(ap.date,'%m/%d/%Y') as invoiceDate,format(ap.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("upper(ac.drcpt) as dockReceipt,ac.voyage_no as voyageNumber,format(ac.transaction_amt,2) as accrualAmount,");
        queryBuilder.append("(SELECT UPPER(ud.login_name) FROM vendor_info vf JOIN user_details ud ON ud.user_id= vf.ap_specialist");
        queryBuilder.append(" WHERE vf.cust_accno = tp.acct_no) as apSpecialist,");
        queryBuilder.append("(SELECT UPPER(login_name) FROM user_details WHERE user_id = ap.user_id) as userLoginName,");
        queryBuilder.append("(SELECT billing_terminal FROM user_details WHERE user_id = ap.user_id) as billingTerminal,");
        queryBuilder.append("date_format(ap.dispute_date,'%m/%d/%Y') as disputedDate,");
        queryBuilder.append("date_format(ap.resolved_date,'%m/%d/%Y') as resolvedDate,");
        queryBuilder.append("concat(datediff(ap.resolved_date,ap.dispute_date),'') as resolutionPeriod,");
        queryBuilder.append("group_concat(n.note_desc separator '<--->') as notesDescription");
        queryBuilder.append(" from ap_invoice ap join trading_partner tp on tp.acct_no=ap.account_number");
        queryBuilder.append(" left join transaction_ledger ac ");
        queryBuilder.append(" on (ac.invoice_number=ap.invoice_number and ac.cust_no=ap.account_number");
        queryBuilder.append(" and ac.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.status='").append(STATUS_DISPUTE).append("')");
        queryBuilder.append(" left join notes n on (((n.module_ref_id = concat(tp.acct_no,'-',ap.invoice_number) and n.module_id = 'AP_INVOICE')");
        queryBuilder.append(" or (n.module_ref_id = ac.Transaction_Id and n.module_id = 'ACCRUALS'))");
        queryBuilder.append(" and n.print_on_report = 1)");
        queryBuilder.append(" where ap.dispute_date is not null");
        if (apReportsForm.getResolveOptions().equalsIgnoreCase("Y")) {
            queryBuilder.append(" and ap.`resolved_date` is not null");
        } else if (apReportsForm.getResolveOptions().equalsIgnoreCase("N")) {
            queryBuilder.append(" and ap.`resolved_date` is null");
        }
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        queryBuilder.append(" and ap.date between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" group by concat(tp.acct_no,'-',ap.invoice_number),ac.Transaction_Id");
        queryBuilder.append(" order by ap.dispute_date desc,ap.invoice_number");
        return queryBuilder.toString();
    }

    public List<ReportBean> getDisputedItems(ApReportsForm apReportsForm) throws Exception {
        String query = buildDisuputedItemsQuery(apReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public List<ReportBean> getTrmManagerItems(ApReportsForm apReportsForm) throws Exception {
        String query = buildTerminalManagerQuery(apReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public String buildTerminalManagerQuery(ApReportsForm apReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(tp.acct_name) as vendorName,upper(tp.acct_no) as vendorNumber,upper(ap.invoice_number) as invoiceNumber,");
        queryBuilder.append("date_format(ap.date,'%m/%d/%Y') as invoiceDate,format(ap.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("upper(ac.drcpt) as dockReceipt,ac.voyage_no as voyageNumber,format(ac.transaction_amt,2) as accrualAmount,");
        queryBuilder.append("(SELECT UPPER(ud.login_name) FROM vendor_info vf JOIN user_details ud ON ud.user_id= vf.ap_specialist");
        queryBuilder.append(" WHERE vf.cust_accno = tp.acct_no) as apSpecialist,");
        queryBuilder.append("(SELECT UPPER(login_name) FROM user_details WHERE user_id = ap.user_id) as userLoginName,");
        queryBuilder.append("(SELECT billing_terminal FROM user_details WHERE user_id = ap.user_id) as billingTerminal,");
        queryBuilder.append("date_format(ap.dispute_date,'%m/%d/%Y') as disputedDate,");
        queryBuilder.append("date_format(ap.resolved_date,'%m/%d/%Y') as resolvedDate,");
        queryBuilder.append("concat(datediff(ap.resolved_date,ap.dispute_date),'') as resolutionPeriod,");
        queryBuilder.append("group_concat(n.note_desc separator '<--->') as notesDescription");
        queryBuilder.append(" from ap_invoice ap join trading_partner tp on tp.acct_no=ap.account_number");
        queryBuilder.append(" JOIN user_details up ON ap.`user_id` = up.`user_id`");
        queryBuilder.append(" left join transaction_ledger ac ");
        queryBuilder.append(" on (ac.invoice_number=ap.invoice_number and ac.cust_no=ap.account_number");
        queryBuilder.append(" and ac.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.status='").append(STATUS_DISPUTE).append("')");
        queryBuilder.append(" left join notes n on (((n.module_ref_id = concat(tp.acct_no,'-',ap.invoice_number) and n.module_id = 'AP_INVOICE')");
        queryBuilder.append(" or (n.module_ref_id = ac.Transaction_Id and n.module_id = 'ACCRUALS'))");
        queryBuilder.append(" and n.print_on_report = 1)");
        queryBuilder.append(" where ap.dispute_date is not null and ap.`resolved_date` is null ");
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        queryBuilder.append(" and ap.date between '").append(fromDate).append("' and '").append(toDate).append("'");
        if (!apReportsForm.getTerminalNo().isEmpty()) {// this is sent all Report To kris
            queryBuilder.append(" AND up.billing_terminal IN(").append(apReportsForm.getTerminalNo()).append(")");
        }
        queryBuilder.append(" group by concat(tp.acct_no,'-',ap.invoice_number),ac.Transaction_Id");
        queryBuilder.append(" order by ap.dispute_date desc,ap.invoice_number");
        return queryBuilder.toString();
    }
    
    public String buildDisputedEmailLogQuery(ApReportsForm apReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from mail_transactions mail");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on tp.acct_no=substring(mail.module_id,1,10)");
        queryBuilder.append(" where mail.module_name='ACCRUALS' and mail.type='Email'");
        if (CommonUtils.isNotEmpty(apReportsForm.getFromDate()) && CommonUtils.isNotEmpty(apReportsForm.getToDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            String toDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and mail.email_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getUserName())) {
            queryBuilder.append(" and mail.user_name='").append(apReportsForm.getUserName()).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getInvoiceNumber())) {
            queryBuilder.append(" and substring(mail.module_id,12)='").append(apReportsForm.getInvoiceNumber()).append("'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getSortBy()) && CommonUtils.isNotEmpty(apReportsForm.getOrderBy())) {
            queryBuilder.append(" order by ").append(apReportsForm.getSortBy()).append(" ").append(apReportsForm.getOrderBy());
        } else {
            queryBuilder.append(" order by id asc");
        }
        return queryBuilder.toString();
    }

    public Integer getTotalDisputedEmailLogs(String condition) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(id)").append(condition);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Integer.parseInt(result.toString()) : 0;
    }

    public List<ReportBean> getDisputedEmailLogs(String condition, int start, int end, boolean isPagination) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as vendorName,tp.acct_no as vendorNumber,");
        queryBuilder.append("substring(mail.module_id,12) as invoiceNumber,");
        queryBuilder.append("concat('[',mail.from_name,']',mail.from_address) as fromAddress,");
        queryBuilder.append("mail.to_address as toAddress,mail.cc_address as ccAddress,mail.bcc_address as bccAddress,");
        queryBuilder.append("mail.subject as subject,mail.html_message as htmlMessage,");
        queryBuilder.append("mail.status as status,mail.user_name as userName");
        queryBuilder.append(condition);
        Query query = getSession().createSQLQuery(queryBuilder.toString());
        if (isPagination) {
            query.setFirstResult(start).setMaxResults(end);
        }
        return query.setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    private String getVendorForApSpecialist(String apSpecialistId) {
        StringBuilder queryBuilder = new StringBuilder("select concat(\"'\",cust_accno,\"'\") from vendor_info");
        if (null != apSpecialistId) {
            queryBuilder.append(" where ap_specialist=").append(apSpecialistId);
        } else {
            queryBuilder.append(" where ap_specialist is not null");
        }
        queryBuilder.append(" order by cust_accno");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "").replace("]", "");
        }
        return null;
    }

    private String buildApQuery(ApReportsForm apReportsForm, String vendorForApSpecialist, boolean isMaster) {
        if (CommonUtils.in(apReportsForm.getInclude(), "Negative AP + AC only", "Negative AP only", "All AP + Open AC only", "All AP only")) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("(select tr.transaction_date as invoice_date,");
            queryBuilder.append("trim(tr.invoice_number) as invoice_or_bl,");
            queryBuilder.append("trim(tr.booking_no) as booking_number,");
            queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
            queryBuilder.append("trim(tr.cons_name) as consignee,");
            queryBuilder.append("trim(tr.voyage_no) as voyage,");
            queryBuilder.append("-tr.transaction_amt as invoice_amount,");
            queryBuilder.append("0 as payment,");
            queryBuilder.append("-tr.transaction_amt as balance,");
            queryBuilder.append("'AP' as transaction_type,");
            queryBuilder.append("cast('AP_INVOICE' as char character set latin1) as note_module_id,");
            queryBuilder.append("concat(tp.acct_no,'-',tr.invoice_number) as note_ref_id,");
            queryBuilder.append("tp.acct_no as customer_number,");
            queryBuilder.append("tp.acct_name as customer_name,");
            queryBuilder.append("null as prepayment_notes");
            queryBuilder.append(" from transaction tr");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (tp.acct_no=tr.cust_no");
            if (apReportsForm.isAllCustomers()) {
                queryBuilder.append(" and tp.acct_no is not null");
            } else if (CommonUtils.isNotEmpty(vendorForApSpecialist)) {
                queryBuilder.append(" and tp.acct_no in (").append(vendorForApSpecialist).append(")");
            } else if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
                if (isMaster) {
                    queryBuilder.append(" and (tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
                    queryBuilder.append(" or tp.masteracct_no='").append(apReportsForm.getVendorNumber()).append("')");
                } else {
                    queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
                }
            }
            if (apReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(vendorForApSpecialist)) {
                if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAgents(), "onlyAgents")) {
                    queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
                } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAgents(), "agentsNotIncluded")) {
                    queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
                }
            }
            queryBuilder.append(")");
            queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
            queryBuilder.append(" and tr.status='").append(STATUS_OPEN).append("'");
            if (CommonUtils.in(apReportsForm.getInclude(), "Negative AP + AC only", "Negative AP only")) {
                queryBuilder.append(" and tr.transaction_amt<0)");
            } else {
                queryBuilder.append(" and tr.transaction_amt<>0)");
            }
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    private String buildAcQuery(ApReportsForm apReportsForm, String vendorForApSpecialist, boolean isMaster) {
        if (CommonUtils.in(apReportsForm.getInclude(), "Negative AP + AC only", "Negative AC only", "All AP + Open AC only", "Open AC only")) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("(select tl.transaction_date as invoice_date,");
            queryBuilder.append("trim(if(tl.invoice_number!='',upper(tl.invoice_number),upper(tl.bill_ladding_no))) as invoice_or_bl,");
            queryBuilder.append("trim(tl.booking_no) as booking_number,");
            queryBuilder.append("trim(tl.customer_reference_no) as customer_reference,");
            queryBuilder.append("trim(tl.cons_name) as consignee,");
            queryBuilder.append("trim(tl.voyage_no) as voyage,");
            queryBuilder.append("-tl.transaction_amt as invoice_amount,");
            queryBuilder.append("0 as payment,");
            queryBuilder.append("-tl.transaction_amt as balance,");
            queryBuilder.append("'AC' as transaction_type,");
            queryBuilder.append("cast('ACCRUALS' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(tl.transaction_id as char character set latin1) as note_ref_id,");
            queryBuilder.append("tp.acct_no as customer_number,");
            queryBuilder.append("tp.acct_name as customer_name,");
            queryBuilder.append("null as prepayment_notes");
            queryBuilder.append(" from transaction_ledger tl");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (tp.acct_no=tl.cust_no");
            if (apReportsForm.isAllCustomers()) {
                queryBuilder.append(" and tp.acct_no is not null");
            } else if (CommonUtils.isNotEmpty(vendorForApSpecialist)) {
                queryBuilder.append(" and tp.acct_no in (").append(vendorForApSpecialist).append(")");
            } else if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
                if (isMaster) {
                    queryBuilder.append(" and (tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
                    queryBuilder.append(" or tp.masteracct_no='").append(apReportsForm.getVendorNumber()).append("')");
                } else {
                    queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
                }
            }
            if (apReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(vendorForApSpecialist)) {
                if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAgents(), "onlyAgents")) {
                    queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
                } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAgents(), "agentsNotIncluded")) {
                    queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
                }
            }
            queryBuilder.append(")");
            queryBuilder.append(" where tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
            queryBuilder.append(" and tl.status!='").append(STATUS_ASSIGN).append("'");
            queryBuilder.append(" and tl.status!='").append(STATUS_EDI_ASSIGNED).append("'");
            queryBuilder.append(" and tl.status!='").append(STATUS_INACTIVE).append("'");
            if (CommonUtils.in(apReportsForm.getInclude(), "Negative AP + AC only", "Negative AC only")) {
                queryBuilder.append(" and tl.transaction_amt<0)");
            } else {
                queryBuilder.append(" and tl.transaction_amt<>0)");
            }
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    private String buildArQuery(ApReportsForm apReportsForm, String vendorForApSpecialist, boolean isMaster) {
        if (apReportsForm.isIncludeAR()) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("(select ar.invoice_date as invoice_date,");
            queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
            queryBuilder.append("ar.booking_number as booking_number,");
            queryBuilder.append("ar.customer_reference as customer_reference,");
            queryBuilder.append("ar.consignee as consignee,");
            queryBuilder.append("ar.voyage as voyage,");
            queryBuilder.append("sum(if(th.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ'),");
            queryBuilder.append("th.transaction_amount,0)) as invoice_amount,");
            queryBuilder.append("sum(if(th.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ'),");
            queryBuilder.append("th.transaction_amount+th.adjustment_amount,0)) as payment,");
            queryBuilder.append("sum(th.transaction_amount+th.adjustment_amount) as balance,");
            queryBuilder.append("'AR' as transaction_type,");
            queryBuilder.append("cast('AR_INVOICE' as char character set latin1) as note_module_id,");
            queryBuilder.append("concat(ar.customer_number,'-',ar.invoice_or_bl) as note_ref_id,");
            queryBuilder.append("ar.customer_number as customer_number,");
            queryBuilder.append("ar.customer_name as customer_name");
            queryBuilder.append(" from (");
            queryBuilder.append("select tp.acct_no as customer_number,");
            queryBuilder.append("tp.acct_name as customer_name,");
            queryBuilder.append("min(tr.transaction_date) as invoice_date,");
            queryBuilder.append("trim(if(tr.bill_ladding_no!='',upper(tr.bill_ladding_no),upper(tr.invoice_number))) as invoice_or_bl,");
            queryBuilder.append("trim(tr.booking_no) as booking_number,");
            queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
            queryBuilder.append("trim(tr.cons_name) as consignee,");
            queryBuilder.append("trim(tr.voyage_no) as voyage");
            queryBuilder.append(" from transaction tr");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (tp.acct_no=tr.cust_no");
            if (apReportsForm.isAllCustomers()) {
                queryBuilder.append(" and tp.acct_no is not null");
            } else if (CommonUtils.isNotEmpty(vendorForApSpecialist)) {
                queryBuilder.append(" and tp.acct_no in (").append(vendorForApSpecialist).append(")");
            } else if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
                if (isMaster) {
                    queryBuilder.append(" and (tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
                    queryBuilder.append(" or tp.masteracct_no='").append(apReportsForm.getVendorNumber()).append("')");
                } else {
                    queryBuilder.append(" and tp.acct_no='").append(apReportsForm.getVendorNumber()).append("'");
                }
            }
            if (apReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(vendorForApSpecialist) || CommonUtils.isEmpty(apReportsForm.getVendorNumber())) {
                if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAgents(), "onlyAgents")) {
                    queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
                } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAgents(), "agentsNotIncluded")) {
                    queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
                }
            }
            queryBuilder.append(")");
            queryBuilder.append(" group by tr.cust_no,invoice_or_bl");
            queryBuilder.append(" order by tr.Transaction_date");
            queryBuilder.append(") as ar");
            queryBuilder.append(" join ar_transaction_history th");
            queryBuilder.append(" on (ar.customer_number=th.customer_number and ar.invoice_or_bl=th.invoice_or_bl)");
            queryBuilder.append(" group by ar.customer_number,ar.invoice_or_bl)");
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    public Map<String, String> buildQueries(ApReportsForm apReportsForm) {
        String vendorForApSpecialist = null;
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getApSpecialist(), ALL)) {
            vendorForApSpecialist = getVendorForApSpecialist(null);
        } else if (CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
            vendorForApSpecialist = getVendorForApSpecialist(apReportsForm.getApSpecialist());
        }
        boolean isMaster = false;
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
            isMaster = new TradingPartnerDAO().isMaster(apReportsForm.getVendorNumber());
        }
        String apQuery = buildApQuery(apReportsForm, vendorForApSpecialist, isMaster);
        String acQuery = buildAcQuery(apReportsForm, vendorForApSpecialist, isMaster);
        String arQuery = buildArQuery(apReportsForm, vendorForApSpecialist, isMaster);
        Map<String, String> queries = new HashMap<String, String>();
        queries.put("apQuery", apQuery);
        queries.put("acQuery", acQuery);
        queries.put("arQuery", arQuery);
        return queries;
    }

    public List<AccountingBean> getTransactions(ApReportsForm apReportsForm, Map<String, String> queries) {
        String arQuery = queries.get("arQuery");
        String apQuery = queries.get("apQuery");
        String acQuery = queries.get("acQuery");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select date_format(t.invoice_date,'%m/%d/%Y') as formattedDate,");
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getAction(), "exportStatementToExcel")) {
            queryBuilder.append("if(t.invoice_or_bl like '%04-%',");
            queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,'04-',1),''),'-',''),t.invoice_or_bl) as invoiceOrBl,");
            queryBuilder.append("t.booking_number as bookingNumber,");
            queryBuilder.append("t.customer_reference as customerReference,");
            queryBuilder.append("t.consignee as consignee,");
            queryBuilder.append("t.voyage as voyage,");
        } else {
            queryBuilder.append("left(if(t.invoice_or_bl like '%04-%',");
            queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,'04-',1),''),'-',''),t.invoice_or_bl),20) as invoiceOrBl,");
            queryBuilder.append("left(t.booking_number,12) as bookingNumber,");
            queryBuilder.append("left(t.customer_reference,25) as customerReference,");
            queryBuilder.append("left(t.consignee,10) as consignee,");
            queryBuilder.append("left(t.voyage,10) as voyage,");
        }
        queryBuilder.append("format(t.invoice_amount,2) as formattedAmount,");
        queryBuilder.append("format(t.payment,2) as formattedPayment,");
        queryBuilder.append("format(t.balance,2) as formattedBalance,");
        queryBuilder.append("t.customer_number as customerNumber,t.customer_name as customerName,");
        queryBuilder.append("if(t.prepayment_notes!='' && group_concat(n.note_desc separator '<--->')!='',");
        queryBuilder.append("concat(t.prepayment_notes,'<--->',group_concat(n.note_desc separator '<--->')!=''),");
        queryBuilder.append("if(t.prepayment_notes!='',t.prepayment_notes,");
        queryBuilder.append("if(group_concat(n.note_desc separator '<--->')!='',group_concat(n.note_desc separator '<--->'),''))) as invoiceNotes,");
        queryBuilder.append("t.transaction_type as transactionType,u.login_name as apSpecialist from ( ");
        if (null != apQuery) {
            queryBuilder.append(apQuery);
        }
        if (null != acQuery) {
            queryBuilder.append(null != apQuery ? " union " : "").append(acQuery);
        }
        if (null != arQuery) {
            queryBuilder.append((null != apQuery || null != acQuery) ? " union " : "");
            queryBuilder.append("(select ar.invoice_date as invoice_date,");
            queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
            queryBuilder.append("ar.booking_number as booking_number,");
            queryBuilder.append("ar.customer_reference as customer_reference,");
            queryBuilder.append("ar.consignee as consignee,");
            queryBuilder.append("ar.voyage as voyage,");
            queryBuilder.append("ar.invoice_amount as invoice_amount,");
            queryBuilder.append("ar.payment as payment,");
            queryBuilder.append("ar.balance as balance,");
            queryBuilder.append("ar.transaction_type as transaction_type,");
            queryBuilder.append("ar.note_module_id as note_module_id,");
            queryBuilder.append("ar.note_ref_id as note_ref_id,");
            queryBuilder.append("ar.customer_number as customer_number,");
            queryBuilder.append("ar.customer_name as customer_name,");
            queryBuilder.append("group_concat(p.notes separator '<--->') as prepayment_notes");
            queryBuilder.append(" from ").append(arQuery).append(" as ar");
            queryBuilder.append(" left join payments p");
            queryBuilder.append(" on (p.payment_type='Check' and p.invoice_no='PRE PAYMENT'");
            queryBuilder.append(" and p.bill_ladding_no=ar.invoice_or_bl and p.cust_no=ar.customer_number)");
            queryBuilder.append(" group by ar.invoice_or_bl)");
        }
        queryBuilder.append(" ) as t");
        queryBuilder.append(" left join notes n");
        queryBuilder.append(" on (t.note_module_id=n.module_id");
        queryBuilder.append(" and t.note_ref_id=n.module_ref_id");
        queryBuilder.append(" and n.print_on_report = 1) ");
        queryBuilder.append("left join vendor_info ve ");
        queryBuilder.append("on ve.cust_accno = t.customer_number ");
        queryBuilder.append("left join user_details u ");
        queryBuilder.append("on ve.ap_specialist = u.user_id");
        queryBuilder.append(" where t.balance!=0");
        queryBuilder.append(" group by t.invoice_or_bl,t.transaction_type");
        queryBuilder.append(" order by t.invoice_date");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(AccountingBean.class)).list();
    }

    public CustomerBean getPayableAgingBuckets(String apQuery, String acQuery) throws Exception {
        CustomerBean customerBean = new CustomerBean();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(sum(if(datediff(sysdate(),t.invoice_date)<=30,t.balance,0)),2) as 0_30days,");
        queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=31");
        queryBuilder.append("  and datediff(sysdate(),t.invoice_date)<=60,t.balance,0)),2) as 31_60days,");
        queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=61");
        queryBuilder.append(" and datediff(sysdate(),t.invoice_date)<=90,t.balance,0)),2) as 61_90days,");
        queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=91,t.balance,0)),2) as 91days,");
        queryBuilder.append("format(sum(t.balance),2) as total,");
        queryBuilder.append("format(sum(if(t.transaction_type='AP',t.balance,0)),2) as ap_balance,");
        queryBuilder.append("format(sum(if(t.transaction_type='AC',t.balance,0)),2) as ac_balance");
        queryBuilder.append(" from (");
        if (null != apQuery) {
            queryBuilder.append(apQuery);
        }
        if (null != acQuery) {
            queryBuilder.append(null != apQuery ? " union " : "").append(acQuery);
        }
        queryBuilder.append(" ) as t");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            Object[] col = (Object[]) result;
            customerBean.setCurrent(null != col[0] ? (String) col[0] : "0.00");
            customerBean.setThirtyOneToSixtyDays(null != col[1] ? (String) col[1] : "0.00");
            customerBean.setSixtyOneToNintyDays(null != col[2] ? (String) col[2] : "0.00");
            customerBean.setGreaterThanNintyDays(null != col[3] ? (String) col[3] : "0.00");
            customerBean.setTotal(null != col[4] ? (String) col[4] : "0.00");
            customerBean.setOutstandingPayables(null != col[5] ? (String) col[5] : "0.00");
            customerBean.setOutstandingAccruals(null != col[6] ? (String) col[6] : "0.00");
        } else {
            customerBean.setCurrent("0.00");
            customerBean.setThirtyOneToSixtyDays("0.00");
            customerBean.setSixtyOneToNintyDays("0.00");
            customerBean.setGreaterThanNintyDays("0.00");
            customerBean.setTotal("0.00");
        }
        return customerBean;
    }

    public CustomerBean getReceivableAgingBuckets(String arQuery) throws Exception {
        CustomerBean customerBean = new CustomerBean();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(sum(if(datediff(sysdate(),t.invoice_date)<=30,t.balance,0)),2) as 0_30days,");
        queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=31");
        queryBuilder.append("  and datediff(sysdate(),t.invoice_date)<=60,t.balance,0)),2) as 31_60days,");
        queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=61");
        queryBuilder.append(" and datediff(sysdate(),t.invoice_date)<=90,t.balance,0)),2) as 61_90days,");
        queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=91,t.balance,0)),2) as 91days,");
        queryBuilder.append("format(sum(t.balance),2) as total");
        queryBuilder.append(" from (");
        queryBuilder.append(arQuery);
        queryBuilder.append(" ) as t");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            Object[] col = (Object[]) result;
            customerBean.setCurrent(null != col[0] ? (String) col[0] : "0.00");
            customerBean.setThirtyOneToSixtyDays(null != col[1] ? (String) col[1] : "0.00");
            customerBean.setSixtyOneToNintyDays(null != col[2] ? (String) col[2] : "0.00");
            customerBean.setGreaterThanNintyDays(null != col[3] ? (String) col[3] : "0.00");
            customerBean.setTotal(null != col[4] ? (String) col[4] : "0.00");
        } else {
            customerBean.setCurrent("0.00");
            customerBean.setThirtyOneToSixtyDays("0.00");
            customerBean.setSixtyOneToNintyDays("0.00");
            customerBean.setGreaterThanNintyDays("0.00");
            customerBean.setTotal("0.00");
        }
        return customerBean;
    }

    public List<LabelValueBean> getApSpecialists() throws Exception {
        List<LabelValueBean> apSpecialist = new ArrayList<LabelValueBean>();
        apSpecialist.add(new LabelValueBean("Select User", ""));
        apSpecialist.add(new LabelValueBean("ALL", "ALL"));
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ud.user_id,ud.login_name from vendor_info vi");
        queryBuilder.append(" left join user_details ud on vi.ap_specialist = ud.user_id");
        queryBuilder.append(" where vi.ap_specialist is not null group by vi.ap_specialist");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            apSpecialist.add(new LabelValueBean((String) col[1], col[0].toString()));
        }
        return apSpecialist;
    }
}
