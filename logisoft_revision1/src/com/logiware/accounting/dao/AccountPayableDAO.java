package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_PAYABLE;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.form.AccountPayableForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.accounting.model.VendorModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.utils.AuditNotesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccountPayableDAO extends BaseHibernateDAO implements ConstantsInterface {

    public String buildApQuery(AccountPayableForm form, Integer userId) throws Exception {
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "In Progress")) {
            return null;
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from transaction ap");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ap.cust_no = tp.acct_no)");
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "My AP Accounts")) {
            queryBuilder.append(" join vendor_info ve");
            queryBuilder.append("  on (tp.acct_no = ve.cust_accno");
            queryBuilder.append("   and ve.ap_specialist = '").append(userId).append("')");
        }
        queryBuilder.append(" where ap.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and ap.balance <> 0.00");
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            queryBuilder.append(" and (tp.acct_no = '").append(form.getVendorNumber()).append("'");
            queryBuilder.append(" or  tp.masteracct_no = '").append(form.getVendorNumber()).append("')");
        }
        if (CommonUtils.isNotEmpty(form.getInvoiceNumber())) {
            queryBuilder.append(" and ap.invoice_number = '").append(form.getInvoiceNumber()).append("'");
        }
        if (CommonUtils.isEqualIgnoreCase(form.getHold(), ONLY)) {
            queryBuilder.append(" and ap.status = '").append(STATUS_HOLD).append("'");
        } else if (CommonUtils.isEqualIgnoreCase(form.getHold(), YES)) {
            queryBuilder.append(" and (ap.status = '").append(STATUS_HOLD).append("'");
            queryBuilder.append(" or ap.status = '").append(STATUS_OPEN).append("')");
        } else {
            queryBuilder.append(" and ap.status = '").append(STATUS_OPEN).append("'");
        }
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "My AP Entries")) {
            queryBuilder.append("   and ap.created_by = '").append(userId).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and ap.transaction_date >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and ap.transaction_date <= '").append(toDate).append("'");
        }
        return queryBuilder.toString();
    }

    public String buildArQuery(AccountPayableForm form, Integer userId) throws Exception {
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "In Progress")
                || CommonUtils.isEqualIgnoreCase(form.getHold(), ONLY)
                || CommonUtils.isEqualIgnoreCase(form.getAr(), NO)) {
            return null;
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from transaction ar");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ar.cust_no = tp.acct_no)");
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "My AP Accounts")) {
            queryBuilder.append(" join vendor_info ve");
            queryBuilder.append("  on (tp.acct_no = ve.cust_accno");
            queryBuilder.append("   and ve.ap_specialist = '").append(userId).append("')");
        }
        queryBuilder.append(" where ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            queryBuilder.append(" and (tp.acct_no = '").append(form.getVendorNumber()).append("'");
            queryBuilder.append(" or  tp.masteracct_no = '").append(form.getVendorNumber()).append("')");
        }
        if (CommonUtils.isNotEmpty(form.getInvoiceNumber())) {
            queryBuilder.append(" and (ar.invoice_number = '").append(form.getInvoiceNumber()).append("'");
            queryBuilder.append(" or ar.bill_ladding_no = '").append(form.getInvoiceNumber()).append("')");
        }
        queryBuilder.append(" and ar.balance <> 0.00");
        queryBuilder.append(" and ar.balance_in_process <> 0.00");
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "My AP Entries")) {
            queryBuilder.append("   and ar.created_by = '").append(userId).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and ar.transaction_date >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and ar.transaction_date <= '").append(toDate).append("'");
        }
        return queryBuilder.toString();
    }

    public String buildInvQuery(AccountPayableForm form, Integer userId) throws Exception {
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "In Progress")
                && CommonUtils.isNotEqualIgnoreCase(form.getHold(), ONLY)) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" from ap_invoice inv");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (inv.account_number = tp.acct_no)");
            queryBuilder.append(" where (inv.status = '").append(STATUS_OPEN).append("'");
            queryBuilder.append(" or inv.status = '").append(STATUS_IN_PROGRESS).append("')");
            if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(form.getVendorNumber()).append("'");
                queryBuilder.append(" or  tp.masteracct_no = '").append(form.getVendorNumber()).append("')");
            }
            if (CommonUtils.isNotEmpty(form.getInvoiceNumber())) {
                queryBuilder.append(" and inv.invoice_number like '%").append(form.getInvoiceNumber()).append("%'");
            }
            if (CommonUtils.isNotEmpty(form.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                queryBuilder.append(" and inv.date >= '").append(fromDate).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                queryBuilder.append(" and inv.date <= '").append(toDate).append("'");
            }
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    public String buildEdiQuery(AccountPayableForm form, Integer userId) throws Exception {
        if (CommonUtils.isEqualIgnoreCase(form.getOnly(), "In Progress")
                && CommonUtils.isNotEqualIgnoreCase(form.getHold(), ONLY)) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" from edi_invoice edi");
            queryBuilder.append(" join trading_partner tp");
            queryBuilder.append(" on (edi.vendor_number = tp.acct_no)");
            queryBuilder.append(" where edi.status = '").append(STATUS_EDI_IN_PROGRESS).append("'");
            if (CommonUtils.isNotEmpty(form.getInvoiceNumber())) {
                queryBuilder.append(" and edi.invoice_number like '%").append(form.getInvoiceNumber()).append("%'");
            }
            if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(form.getVendorNumber()).append("'");
                queryBuilder.append(" or  tp.masteracct_no = '").append(form.getVendorNumber()).append("')");
            }
            if (CommonUtils.isNotEmpty(form.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                queryBuilder.append(" and edi.invoice_date >= '").append(fromDate).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                queryBuilder.append(" and edi.invoice_date <= '").append(toDate).append("'");
            }
            return queryBuilder.toString();
        } else {
            return null;
        }
    }
    
    public VendorModel getVendor(String vendorNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(vendor_number) as vendorNumber,");
        queryBuilder.append("upper(vendor_name) as vendorName,");
        queryBuilder.append("upper(sub_type) as subType,");
        queryBuilder.append("upper(address) as address,");
        queryBuilder.append("upper(zip) as zip,");
        queryBuilder.append("upper(contact) as contact,");
        queryBuilder.append("upper(phone) as phone,");
        queryBuilder.append("upper(fax) as fax,");
        queryBuilder.append("upper(email) as email,");
        queryBuilder.append("credit_limit as creditLimit,");
        queryBuilder.append("credit_term as creditTerm");
        queryBuilder.append(" from vendor_details_view");
        queryBuilder.append(" where vendor_number = '").append(vendorNumber).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("subType", StringType.INSTANCE);
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("zip", StringType.INSTANCE);
        query.addScalar("contact", StringType.INSTANCE);
        query.addScalar("phone", StringType.INSTANCE);
        query.addScalar("fax", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("creditLimit", StringType.INSTANCE);
        query.addScalar("creditTerm", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(VendorModel.class));
        return (VendorModel) query.uniqueResult();
    }

    private Integer getApRows(String apQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(ap.transaction_id)").append(apQuery);
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getArRows(String arQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(ar.rowCount)");
        queryBuilder.append(" from (");
        queryBuilder.append("select count(ar.transaction_id) as rowCount").append(arQuery);
        queryBuilder.append(") as ar");
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getInvRows(String invQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(inv.id)").append(invQuery);
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getEdiRows(String ediQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(edi.id)").append(ediQuery);
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    public Object getAgingBuckets(Map<String, String> queries) {
        String apQuery = queries.get("apQuery");
        String arQuery = queries.get("arQuery");
        String invQuery = queries.get("invQuery");
        String ediQuery = queries.get("ediQuery");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(sum(age_30_amount),2) as age30Amount,");
        queryBuilder.append("format(sum(age_60_amount),2) as age60Amount,");
        queryBuilder.append("format(sum(age_90_amount),2) as age90Amount,");
        queryBuilder.append("format(sum(age_91_amount),2) as age91Amount,");
        queryBuilder.append("format(sum(ap_amount),2) as apAmount,");
        queryBuilder.append("format(sum(ar_amount),2) as arAmount,");
        queryBuilder.append("format(sum(ap_amount+ar_amount),2) as netAmount");
        queryBuilder.append(" from (");
        boolean isunion = false;
        if (CommonUtils.isNotEmpty(apQuery)) {
            queryBuilder.append("(select sum(if(datediff(current_date(),ap.transaction_date) between 0 and 30,ap.balance,0)) as age_30_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),ap.transaction_date) between 31 and 60,ap.balance,0)) as age_60_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),ap.transaction_date) between 61 and 90,ap.balance,0)) as age_90_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),ap.transaction_date) >= 91,ap.balance,0)) as age_91_amount,");
            queryBuilder.append("sum(ap.balance) as ap_amount,");
            queryBuilder.append("0 as ar_amount");
            queryBuilder.append(apQuery).append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(arQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(select 0 as age_30_amount,");
            queryBuilder.append("0 as age_60_amount,");
            queryBuilder.append("0 as age_90_amount,");
            queryBuilder.append("0 as age_91_amount,");
            queryBuilder.append("0 as ap_amount,");
            queryBuilder.append("sum(ar.balance) as ar_amount");
            queryBuilder.append(arQuery).append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(invQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(select sum(if(datediff(current_date(),inv.date) between 0 and 30,inv.invoice_amount,0)) as age_30_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),inv.date) between 31 and 60,inv.invoice_amount,0)) as age_60_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),inv.date) between 61 and 90,inv.invoice_amount,0)) as age_90_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),inv.date) >= 91,inv.invoice_amount,0)) as age_91_amount,");
            queryBuilder.append("sum(inv.invoice_amount) as ap_amount,");
            queryBuilder.append("0 as ar_amount");
            queryBuilder.append(invQuery).append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(ediQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(select sum(if(datediff(current_date(),edi.invoice_date) between 0 and 30,edi.invoice_amount,0)) as age_30_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),edi.invoice_date) between 31 and 60,edi.invoice_amount,0)) as age_60_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),edi.invoice_date) between 61 and 90,edi.invoice_amount,0)) as age_90_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),edi.invoice_date) >= 91,edi.invoice_amount,0)) as age_91_amount,");
            queryBuilder.append("sum(edi.invoice_amount) as ap_amount,");
            queryBuilder.append("0 as ar_amount");
            queryBuilder.append(ediQuery).append(")");
        }
        queryBuilder.append(") as ap");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.uniqueResult();
    }

    public List<ResultModel> getResults(Map<String, String> queries, String sortBy, String orderBy, int start, int limit) {
        String apQuery = queries.get("apQuery");
        String arQuery = queries.get("arQuery");
        String invQuery = queries.get("invQuery");
        String ediQuery = queries.get("ediQuery");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(ap.vendor_number) as vendorNumber,");
        queryBuilder.append("upper(ap.vendor_name) as vendorName,");
        queryBuilder.append("upper(ap.invoice_or_bl) as invoiceOrBl,");
        queryBuilder.append("upper(ap.invoice_number) as invoiceNumber,");
        queryBuilder.append("upper(ap.bl_number) as blNumber,");
        queryBuilder.append("date_format(ap.invoice_date,'%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("upper(if(ge.codedesc <> '', ge.codedesc, 'DUE UPON RECEIPT')) as creditTerms,");
        queryBuilder.append("date_format(date_add(ap.invoice_date, interval coalesce(ge.code, 0) day),'%m/%d/%Y') as dueDate,");
        queryBuilder.append("ap.age as age,");
        queryBuilder.append("ap.credit_hold as creditHold,");
        queryBuilder.append("format(ap.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("upper(ap.check_number) as checkNumber,");
        queryBuilder.append("ap.transaction_type as transactionType,");
        queryBuilder.append("ap.status as status,");
        queryBuilder.append("ap.id as id,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','true','false') as overhead,");
        queryBuilder.append("if(count(nt.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as uploaded,");
        queryBuilder.append("ap.note_module_id as noteModuleId,");
        queryBuilder.append("ap.note_ref_id as noteRefId,");
        queryBuilder.append("ap.document_id as documentId,");
        queryBuilder.append("ap.deletable as deletable,");
        queryBuilder.append("coalesce(ge.code, 0) as creditDays ");
        queryBuilder.append(" from (");
        queryBuilder.append("select ap.vendor_number as vendor_number,");
        queryBuilder.append("ap.vendor_name as vendor_name,");
        queryBuilder.append("ap.bl_number as bl_number,");
        queryBuilder.append("ap.invoice_number as invoice_number,");
        queryBuilder.append("ap.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ap.invoice_date as invoice_date,");
        queryBuilder.append("datediff(sysdate(),ap.invoice_date) as age,");
        queryBuilder.append("if(ap.credit_hold <> '',ap.credit_hold,'N') as credit_hold,");
        queryBuilder.append("ap.invoice_amount as invoice_amount,");
        queryBuilder.append("ap.check_number as check_number,");
        queryBuilder.append("ap.transaction_type as transaction_type,");
        queryBuilder.append("ap.status as status,");
        queryBuilder.append("ap.id as id,");
        queryBuilder.append("ap.note_module_id as note_module_id,");
        queryBuilder.append("ap.note_ref_id as note_ref_id,");
        queryBuilder.append("ap.document_id as document_id,");
        queryBuilder.append("ap.deletable as deletable ");
        queryBuilder.append(" from (");
        boolean isunion = false;
        if (CommonUtils.isNotEmpty(apQuery)) {
            String checkNumber = "if((ap.ach_batch_sequence <> ''),convert(concat('ACH - ',ap.ach_batch_sequence) using latin1),ap.cheque_number)";
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("null as bl_number,");
            queryBuilder.append("ap.invoice_number as invoice_number,");
            queryBuilder.append("ap.invoice_number as invoice_or_bl,");
            queryBuilder.append("ap.transaction_date as invoice_date,");
            queryBuilder.append("ap.credit_hold as credit_hold,");
            queryBuilder.append("ap.transaction_amt as invoice_amount,");
            queryBuilder.append("cast(").append(checkNumber).append(" as char charset latin1) as check_number,");
            queryBuilder.append("ap.transaction_type as transaction_type,");
            queryBuilder.append("ap.status as status,");
            queryBuilder.append("cast(concat(ap.transaction_id,'AP') as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AP_INVOICE).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(concat(ap.cust_no,'-',ap.invoice_number) as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(concat(ap.cust_no,'-',ap.invoice_number) as char character set latin1) as document_id,");
            queryBuilder.append("true as deletable");
            queryBuilder.append(apQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(arQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            StringBuilder blNumber = new StringBuilder();
            blNumber.append("if(ar.bill_ladding_no like '%04-%',");
            blNumber.append("replace(replace(ar.bill_ladding_no,substring_index(ar.bill_ladding_no,'04-',1),''),'-',''),ar.bill_ladding_no)");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append(blNumber).append(" as bl_number,");
            queryBuilder.append("ar.invoice_number as invoice_number,");
            queryBuilder.append("if(ar.bill_ladding_no <> '',").append(blNumber).append(",ar.invoice_number) as invoice_or_bl,");
            queryBuilder.append("ar.transaction_date as invoice_date,");
            queryBuilder.append("ar.credit_hold as credit_hold,");
            queryBuilder.append("-ar.balance as invoice_amount,");
            queryBuilder.append("ar.cheque_number as check_number,");
            queryBuilder.append("ar.transaction_type as transaction_type,");
            queryBuilder.append("'").append(STATUS_OPEN).append("' as status,");
            queryBuilder.append("cast(concat(ar.transaction_id,'AR') as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AR_INVOICE).append("' as char character set latin1) as note_module_id,");
            String referenceId = "concat(ar.cust_no,'-',if(ar.bill_ladding_no <> '',ar.bill_ladding_no,ar.invoice_number))";
            queryBuilder.append("cast(").append(referenceId).append(" as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(").append(referenceId).append(" as char character set latin1) as document_id,");
            queryBuilder.append("if(ar.invoice_number <> '', (select if(count(*) > 0, true, (select if(count(*) > 0, true, false) from `edi_invoice` nap where nap.`vendor_number` = ar.`cust_no` and nap.`invoice_number` = ar.`invoice_number` and nap.`status` = 'EDI Posted To AP')) from `ap_invoice` nap where nap.`account_number` = ar.`cust_no` and nap.`invoice_number` = ar.`invoice_number` and nap.`status` = 'AP'), false) as deletable");
            queryBuilder.append(arQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(invQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("null as bl_number,");
            queryBuilder.append("inv.invoice_number as invoice_number,");
            queryBuilder.append("inv.invoice_number as invoice_or_bl,");
            queryBuilder.append("inv.date as invoice_date,");
            queryBuilder.append("'N' as credit_hold,");
            queryBuilder.append("inv.invoice_amount as invoice_amount,");
            queryBuilder.append("null as check_number,");
            queryBuilder.append("'").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("' as transaction_type,");
            queryBuilder.append("'In Progress' as status,");
            queryBuilder.append("cast(concat(inv.id,'INV') as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AP_INVOICE).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(concat(inv.account_number,'-',inv.invoice_number) as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(concat(inv.account_number,'-',inv.invoice_number) as char character set latin1) as document_id,");
            queryBuilder.append("true as deletable");
            queryBuilder.append(invQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(ediQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("null as bl_number,");
            queryBuilder.append("edi.invoice_number as invoice_number,");
            queryBuilder.append("edi.invoice_number as invoice_or_bl,");
            queryBuilder.append("edi.invoice_date as invoice_date,");
            queryBuilder.append("'N' as credit_hold,");
            queryBuilder.append("edi.invoice_amount as invoice_amount,");
            queryBuilder.append("null as check_number,");
            queryBuilder.append("'").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("' as transaction_type,");
            queryBuilder.append("'EDI In Progress' as status,");
            queryBuilder.append("cast(concat(edi.id,'EDI') as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AP_INVOICE).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(concat(edi.vendor_number,'-',edi.invoice_number) as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(concat(edi.vendor_number,'-',edi.invoice_number) as char character set latin1) as document_id,");
            queryBuilder.append("true as deletable");
            queryBuilder.append(ediQuery);
            queryBuilder.append(")");
        }
        queryBuilder.append(") as ap");
        queryBuilder.append(" group by ap.vendor_number,ap.invoice_or_bl,ap.transaction_type,ap.id");
        queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        if (limit != 0) {
            queryBuilder.append(" limit ").append(start).append(",").append(limit);
        }
        queryBuilder.append(") as ap");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ap.vendor_number = tp.acct_no)");
        queryBuilder.append(" left join vendor_info ve");
        queryBuilder.append("  on (ap.vendor_number = ve.cust_accno)");
        queryBuilder.append(" left join genericcode_dup ge");
        queryBuilder.append("  on (ve.credit_terms = ge.id)");
        queryBuilder.append(" left join notes nt");
        queryBuilder.append(" on (ap.note_module_id = nt.module_id");
        queryBuilder.append(" and ap.note_ref_id = nt.module_ref_id");
        queryBuilder.append(" and nt.note_type = 'Manual')");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name = 'INVOICE'");
        queryBuilder.append(" and doc.document_name = 'INVOICE'");
        queryBuilder.append(" and ap.document_id = doc.document_id)");
        queryBuilder.append(" where ap.vendor_number <> ''");
        queryBuilder.append(" group by ap.vendor_number,ap.invoice_or_bl,ap.transaction_type,ap.id");
        queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("invoiceOrBl", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("invoiceDate", StringType.INSTANCE);
        query.addScalar("creditTerms", StringType.INSTANCE);
        query.addScalar("dueDate", StringType.INSTANCE);
        query.addScalar("age", IntegerType.INSTANCE);
        query.addScalar("creditHold", StringType.INSTANCE);
        query.addScalar("invoiceAmount", StringType.INSTANCE);
        query.addScalar("checkNumber", StringType.INSTANCE);
        query.addScalar("transactionType", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("id", StringType.INSTANCE);
        query.addScalar("overhead", StringType.INSTANCE);
        query.addScalar("manualNotes", StringType.INSTANCE);
        query.addScalar("uploaded", StringType.INSTANCE);
        query.addScalar("noteModuleId", StringType.INSTANCE);
        query.addScalar("noteRefId", StringType.INSTANCE);
        query.addScalar("documentId", StringType.INSTANCE);
        query.addScalar("deletable", BooleanType.INSTANCE);
        query.addScalar("creditDays", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public void search(AccountPayableForm form, Integer userId) throws Exception {
        getCurrentSession().flush();
        VendorModel vendor = null;
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            vendor = getVendor(form.getVendorNumber());
        }
        Map<String, String> queries = new HashMap<String, String>();
        String apQuery = buildApQuery(form, userId);
        String arQuery = buildArQuery(form, userId);
        String invQuery = buildInvQuery(form, userId);
        String ediQuery = buildEdiQuery(form, userId);
        if (CommonUtils.isAtLeastOneNotEmpty(apQuery, arQuery, invQuery, ediQuery)) {
            int apRows = CommonUtils.isNotEmpty(apQuery) ? getApRows(apQuery) : 0;
            int arRows = CommonUtils.isNotEmpty(arQuery) ? getArRows(arQuery) : 0;
            int invRows = CommonUtils.isNotEmpty(invQuery) ? getInvRows(invQuery) : 0;
            int ediRows = CommonUtils.isNotEmpty(ediQuery) ? getEdiRows(ediQuery) : 0;
            queries.put("apQuery", apRows > 0 ? apQuery : null);
            queries.put("arQuery", arRows > 0 ? arQuery : null);
            queries.put("invQuery", invRows > 0 ? invQuery : null);
            queries.put("ediQuery", ediRows > 0 ? ediQuery : null);
            int totalRows = apRows + arRows + invRows + ediRows;
            if (totalRows > 0) {
                if (CommonUtils.in(form.getAction(), "createPdf", "createExcel")) {
                    List<ResultModel> results = getResults(queries, form.getSortBy(), form.getOrderBy(), 0, 0);
                    form.setResults(results);
                } else {
                    int limit = form.getLimit();
                    int start = limit * (form.getSelectedPage() - 1);
                    List<ResultModel> results = getResults(queries, form.getSortBy(), form.getOrderBy(), start, limit);
                    form.setResults(results);
                    form.setSelectedRows(results.size());
                    int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
                    form.setTotalPages(totalPages);
                    form.setTotalRows(totalRows);
                }
                if (null != vendor) {
                    Object result = getAgingBuckets(queries);
                    if (null != result) {
                        Object[] col = (Object[]) result;
                        vendor.setAge30Amount((String) col[0]);
                        vendor.setAge60Amount((String) col[1]);
                        vendor.setAge90Amount((String) col[2]);
                        vendor.setAge91Amount((String) col[3]);
                        vendor.setApAmount((String) col[4]);
                        vendor.setArAmount((String) col[5]);
                        vendor.setNetAmount((String) col[6]);
                    }
                }
            }
        }
        if (null != vendor && CommonUtils.isEmpty(vendor.getAge30Amount())) {
            vendor.setAge30Amount("0.00");
            vendor.setAge60Amount("0.00");
            vendor.setAge90Amount("0.00");
            vendor.setAge91Amount("0.00");
            vendor.setApAmount("0.00");
            vendor.setArAmount("0.00");
            vendor.setNetAmount("0.00");
        }
        form.setVendor(vendor);
    }

    private void setReadyToPayInvoice(String ids, User user) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        for (String id : StringUtils.split(ids.replace("AP", "").replace("AR", ""), ",")) {
            Transaction invoice = transactionDAO.findById(id);
            if (null != invoice) {
                invoice.setStatus(STATUS_READY_TO_PAY);
                invoice.setUpdatedOn(new Date());
                invoice.setUpdatedBy(user.getUserId());
                invoice.setOwner(user.getUserId());
                String moduleId;
                Double amount;
                String invoiceOrBl;
                StringBuilder desc = new StringBuilder();
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    invoice.setGlAccountNumber(LoadLogisoftProperties.getProperty(AP_CONTROL_ACCOUNT));
                    moduleId = NotesConstants.AP_INVOICE;
                    amount = invoice.getTransactionAmt();
                    invoiceOrBl = invoice.getInvoiceNumber();
                    desc.append("Invoice '");
                } else {
                    invoice.setGlAccountNumber(LoadLogisoftProperties.getProperty(AR_CONTROL_ACCOUNT));
                    moduleId = NotesConstants.AR_INVOICE;
                    invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
                    amount = -invoice.getBalance();
                    desc.append("Invoice/BL '");
                }
                String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
                desc.append(invoiceOrBl).append("'");
                desc.append(" of '").append(invoice.getCustName()).append("'");
                desc.append(" for amount '").append(NumberUtils.formatNumber(amount)).append("'");
                desc.append(" is set ready to pay by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
            }
        }
    }

    private void holdInvoice(String ids, User user) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        String moduleId = NotesConstants.AP_INVOICE;
        for (String id : StringUtils.split(ids.replace("AP", ""), ",")) {
            Transaction invoice = transactionDAO.findById(id);
            if (null != invoice && CommonUtils.isNotEqualIgnoreCase(invoice.getStatus(), STATUS_HOLD)) {
                String moduleRefId = invoice.getCustNo() + "-" + invoice.getInvoiceNumber();
                invoice.setStatus(STATUS_HOLD);
                invoice.setUpdatedOn(new Date());
                invoice.setUpdatedBy(user.getUserId());
                StringBuilder desc = new StringBuilder();
                desc.append("Invoice '").append(invoice.getInvoiceNumber()).append("'");
                desc.append(" of '").append(invoice.getCustName()).append("'");
                desc.append(" for amount '").append(NumberUtils.formatNumber(invoice.getTransactionAmt())).append("'");
                desc.append(" is holded by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
            }
        }
    }

    private void deleteApInvoice(String id, User user) throws Exception {
        String accrualsModule = NotesConstants.ACCRUALS;
        String arModule = NotesConstants.AR_INVOICE;
        String apModule = NotesConstants.AP_INVOICE;
        ApTransactionHistoryDAO historyDAO = new ApTransactionHistoryDAO();
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        Transaction oldInvoice = transactionDAO.findById(id);
        String vendorNumber = oldInvoice.getCustNo();
        String invoiceNumber = oldInvoice.getInvoiceNumber();
        Date postedDate = accrualsDAO.getPostedDate(oldInvoice.getPostedDate());
        oldInvoice.setStatus(STATUS_REJECT);
        oldInvoice.setUpdatedOn(new Date());
        oldInvoice.setUpdatedBy(user.getUserId());
        List<TransactionLedger> accruals = accrualsDAO.getAssignedAccruals(vendorNumber, invoiceNumber);
        for (TransactionLedger accrual : accruals) {
            // Revert back Assigned Accruals into Open Accruals when released
            if (accountDetailsDAO.validateAccount(accrual.getGlAccountNumber())) {
                if (!accrual.isDirectGlAccount()) {
                    accrual.setStatus(STATUS_OPEN);
                    accrual.setUpdatedOn(new Date());
                    accrual.setUpdatedBy(user.getUserId());
                    accrual.setPostedDate(null);
                    accrual.setBalance(accrual.getTransactionAmt());
                    accrual.setBalanceInProcess(accrual.getTransactionAmt());
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                        desc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        if (addAnd) {
                            desc.append(" and ");
                        }
                        desc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                        if (addAnd) {
                            desc.append(" and ");
                        }
                        desc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                    }
                    desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                    desc.append(" is unassigned from Invoice '").append(accrual.getInvoiceNumber()).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), accrualsModule, accrual.getTransactionId().toString(), accrualsModule, user);
                    accrual.setInvoiceNumber(STATUS_EDI_ASSIGNED.equals(accrual.getStatus()) ? accrual.getInvoiceNumber() : null);
                }
                //Create negative PJ Subledger
                TransactionLedger pjSubledger = new TransactionLedger();
                PropertyUtils.copyProperties(pjSubledger, accrual);
                pjSubledger.setInvoiceNumber(invoiceNumber);
                pjSubledger.setTransactionAmt(-pjSubledger.getTransactionAmt());
                pjSubledger.setBalance(-pjSubledger.getBalance());
                pjSubledger.setBalanceInProcess(-pjSubledger.getBalanceInProcess());
                pjSubledger.setTransactionDate(new Date());
                pjSubledger.setPostedDate(postedDate);
                pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                pjSubledger.setStatus(STATUS_OPEN);
                pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                pjSubledger.setUpdatedOn(null);
                pjSubledger.setUpdatedBy(null);
                pjSubledger.setCreatedOn(new Date());
                pjSubledger.setCreatedBy(user.getUserId());
                accrualsDAO.save(pjSubledger);
                if (!accrual.isDirectGlAccount() && CommonUtils.isNotEmpty(accrual.getCostId())) {
                    if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                        Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            columns.put("b.invoice_number", accrual.getInvoiceNumber());
                            columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                            accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                        } else {
                            columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                            columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                            accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                        }
                    } else {
                        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                        if (null != fclBlCostCodes) {
                            fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                            fclBlCostCodes.setInvoiceNumber(null);
                            fclBlCostCodes.setDatePaid(null);

                            if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCostCodes.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCostCodes.getCostCode())) {
                                List<FclBlCostCodes> consolidatorList = new ArrayList();
                                String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                                List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                                FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                                String importflagFor = fclBl.getImportFlag();
                                if (importflagFor.equals("I")) {
                                    consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                                } else {
                                    consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                                }
                                List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();
                                for (FclBlCostCodes fclBlCost : costList) {
                                    if (!consolidatorList.contains(fclBlCost)) {
                                        newList.add(fclBlCost);
                                    }
                                }
                                for (FclBlCostCodes fclBlCostCode : newList) {
                                    if (CommonUtils.notIn(fclBlCostCode.getCostCode(), "OCNFRT", "OFIMP")) {
                                        fclBlCostCode.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                                        fclBlCostCodes.setInvoiceNumber(null);
                                        fclBlCostCodes.setDatePaid(null);
                                    }
                                }
                            }
                        }
                    }
                } else if (accrual.isDirectGlAccount()) {
                    // For Direct AP Invoice, delete the accrual
                    accrualsDAO.delete(accrual);
                }
            } else {
                throw new AccountingException("No GL account found for original accrual");
            }
        }

        List<ArTransactionHistory> arPayments = arTransactionHistoryDAO.getArPaidInApInvoice(vendorNumber + "-" + invoiceNumber);
        if (CommonUtils.isNotEmpty(arPayments)) {
            for (ArTransactionHistory arPayment : arPayments) {
                String customerNumber = arPayment.getCustomerNumber();
                Transaction arInvoice = transactionDAO.getArTransaction(customerNumber, arPayment.getBlNumber(), arPayment.getInvoiceNumber());
                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                arTransactionHistory.setCustomerNumber(customerNumber);
                arTransactionHistory.setBlNumber(arInvoice.getBillLaddingNo());
                arTransactionHistory.setInvoiceNumber(arInvoice.getInvoiceNumber());
                arTransactionHistory.setInvoiceDate(arInvoice.getTransactionDate());
                arTransactionHistory.setTransactionDate(new Date());
                arTransactionHistory.setPostedDate(postedDate);
                arTransactionHistory.setTransactionAmount(-arPayment.getTransactionAmount());
                arTransactionHistory.setAdjustmentAmount(0d);
                arTransactionHistory.setCustomerReferenceNumber(arInvoice.getCustomerReferenceNo());
                arTransactionHistory.setVoyageNumber(arInvoice.getVoyageNo());
                arTransactionHistory.setCheckNumber(vendorNumber + "-" + invoiceNumber);
                arTransactionHistory.setArBatchId(null);
                arTransactionHistory.setApBatchId(null);
                arTransactionHistory.setTransactionType("AP INV");
                arTransactionHistory.setCreatedBy(user.getLoginName());
                arTransactionHistory.setCreatedDate(new Date());
                arTransactionHistoryDAO.save(arTransactionHistory);

                arInvoice.setBalance(arInvoice.getBalance() - arPayment.getTransactionAmount());
                arInvoice.setBalanceInProcess(arInvoice.getBalanceInProcess() - arPayment.getTransactionAmount());

                String apInvoice = vendorNumber + "-" + invoiceNumber;
                String invoiceOrBl = (CommonUtils.isNotEmpty(arInvoice.getBillLaddingNo()) ? arInvoice.getBillLaddingNo() : arInvoice.getInvoiceNumber());
                String moduleRefId = arInvoice.getCustNo() + "-" + invoiceOrBl;
                StringBuilder desc = new StringBuilder();
                desc.append("For Amount : ").append(NumberUtils.formatNumber(-arPayment.getTransactionAmount())).append(",");
                desc.append(" removed from ");
                desc.append(CommonUtils.isEqualIgnoreCase(arInvoice.getApInvoiceStatus(), STATUS_EDI_ASSIGNED) ? "EDI" : "AP");
                desc.append(" Invoice - '").append(apInvoice).append("'");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), arModule, moduleRefId, NotesConstants.AR_INVOICE, user);
            }
        }

        Transaction newInvoice = new Transaction();
        PropertyUtils.copyProperties(newInvoice, oldInvoice);
        newInvoice.setTransactionDate(postedDate);
        newInvoice.setPostedDate(postedDate);
        newInvoice.setTransactionAmt(-newInvoice.getTransactionAmt());
        newInvoice.setBalance(-newInvoice.getBalance());
        newInvoice.setBalanceInProcess(-newInvoice.getTransactionAmt());
        newInvoice.setBillTo(YES);
        newInvoice.setStatus(STATUS_REJECT);
        newInvoice.setCreatedOn(new Date());
        newInvoice.setCreatedBy(user.getUserId());
        newInvoice.setUpdatedOn(null);
        newInvoice.setUpdatedBy(null);
        newInvoice.setApBatchId(null);
        newInvoice.setArBatchId(null);
        newInvoice.setAchBatchSequence(null);
        transactionDAO.save(newInvoice);
        ApTransactionHistory apTransactionHistory = new ApTransactionHistory(newInvoice);
        apTransactionHistory.setInvoiceDate(oldInvoice.getTransactionDate());
        apTransactionHistory.setCreatedBy(user.getLoginName());
        historyDAO.save(apTransactionHistory);

        String glPeriod = DateUtils.formatDate(postedDate, "yyyyMM");
        StringBuilder desc = new StringBuilder();
        desc.append("Invoice '").append(invoiceNumber).append("'");
        desc.append(" of '").append(oldInvoice.getCustName()).append("'");
        desc.append(" deleted on GL Period '").append(glPeriod).append("'");
        desc.append(" for amount '").append(NumberUtils.formatNumber(oldInvoice.getTransactionAmt())).append("'");
        desc.append(" by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), apModule, vendorNumber + "-" + invoiceNumber, apModule, user);
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(invoiceNumber, vendorNumber, TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        if (null != apInvoice) {
            // delete invoice records from ap_invoice when released
            apInvoiceDAO.delete(apInvoice);
        } else {
            // archive invoice records from edi_invoice when released
            EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(vendorNumber, invoiceNumber, STATUS_EDI_POSTED_TO_AP);
            if (null != ediInvoice) {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_ARCHIVE);
                ediInvoiceDAO.update(ediInvoice);
            }
        }
    }

    private void deleteArInvoice(String id, User user) throws Exception {
        String accrualsModule = NotesConstants.ACCRUALS;
        String arModule = NotesConstants.AR_INVOICE;
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        Transaction oldInvoice = transactionDAO.findById(id);
        String customerNumber = oldInvoice.getCustNo();
        String invoiceNumber = oldInvoice.getInvoiceNumber();
        Date postedDate = accrualsDAO.getPostedDate(oldInvoice.getPostedDate());

        double invoiceAmount = 0d;
        List<TransactionLedger> accruals = accrualsDAO.getAssignedAccruals(customerNumber, invoiceNumber);
        for (TransactionLedger accrual : accruals) {
            // Revert back Assigned Accruals into Open Accruals when released
            if (accountDetailsDAO.validateAccount(accrual.getGlAccountNumber())) {
                invoiceAmount += accrual.getTransactionAmt();
                accrual.setStatus(STATUS_OPEN);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(user.getUserId());
                accrual.setPostedDate(null);
                accrual.setBalance(accrual.getTransactionAmt());
                accrual.setBalanceInProcess(accrual.getTransactionAmt());
                StringBuilder desc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    desc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    if (addAnd) {
                        desc.append(" and ");
                    }
                    desc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    if (addAnd) {
                        desc.append(" and ");
                    }
                    desc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                }
                desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                desc.append(" is unassigned from Invoice '").append(accrual.getInvoiceNumber()).append("'");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), accrualsModule, accrual.getTransactionId().toString(), accrualsModule, user);
                accrual.setInvoiceNumber(STATUS_EDI_ASSIGNED.equals(accrual.getStatus()) ? accrual.getInvoiceNumber() : null);
                //Create negative PJ Subledger
                TransactionLedger pjSubledger = new TransactionLedger();
                PropertyUtils.copyProperties(pjSubledger, accrual);
                pjSubledger.setInvoiceNumber(invoiceNumber);
                pjSubledger.setTransactionAmt(-pjSubledger.getTransactionAmt());
                pjSubledger.setBalance(-pjSubledger.getBalance());
                pjSubledger.setBalanceInProcess(-pjSubledger.getBalanceInProcess());
                pjSubledger.setTransactionDate(new Date());
                pjSubledger.setPostedDate(postedDate);
                pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                pjSubledger.setStatus(STATUS_OPEN);
                pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                pjSubledger.setUpdatedOn(null);
                pjSubledger.setUpdatedBy(null);
                pjSubledger.setCreatedOn(new Date());
                pjSubledger.setCreatedBy(user.getUserId());
                accrualsDAO.save(pjSubledger);
                if (!accrual.isDirectGlAccount() && CommonUtils.isNotEmpty(accrual.getCostId())) {
                    if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                        Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            columns.put("b.invoice_number", accrual.getInvoiceNumber());
                            columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                            accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                        } else {
                            columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                            columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                            accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                        }
                    } else {
                        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                        if (null != fclBlCostCodes) {
                            fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                            fclBlCostCodes.setInvoiceNumber(null);
                            fclBlCostCodes.setDatePaid(null);
                        }
                    }
                }
            } else {
                throw new AccountingException("No GL account found for original accrual");
            }
        }

        List<ArTransactionHistory> arPayments = arTransactionHistoryDAO.getArPaidInApInvoice(customerNumber + "-" + invoiceNumber);
        if (CommonUtils.isNotEmpty(arPayments)) {
            for (ArTransactionHistory arPayment : arPayments) {
                invoiceAmount += arPayment.getTransactionAmount();
                customerNumber = arPayment.getCustomerNumber();
                Transaction arInvoice = transactionDAO.getArTransaction(customerNumber, arPayment.getBlNumber(), arPayment.getInvoiceNumber());
                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                arTransactionHistory.setCustomerNumber(customerNumber);
                arTransactionHistory.setBlNumber(arInvoice.getBillLaddingNo());
                arTransactionHistory.setInvoiceNumber(arInvoice.getInvoiceNumber());
                arTransactionHistory.setInvoiceDate(arInvoice.getTransactionDate());
                arTransactionHistory.setTransactionDate(new Date());
                arTransactionHistory.setPostedDate(postedDate);
                arTransactionHistory.setTransactionAmount(-arPayment.getTransactionAmount());
                arTransactionHistory.setAdjustmentAmount(0d);
                arTransactionHistory.setCustomerReferenceNumber(arInvoice.getCustomerReferenceNo());
                arTransactionHistory.setVoyageNumber(arInvoice.getVoyageNo());
                arTransactionHistory.setCheckNumber(customerNumber + "-" + invoiceNumber);
                arTransactionHistory.setArBatchId(null);
                arTransactionHistory.setApBatchId(null);
                arTransactionHistory.setTransactionType("AP INV");
                arTransactionHistory.setCreatedBy(user.getLoginName());
                arTransactionHistory.setCreatedDate(new Date());
                arTransactionHistoryDAO.save(arTransactionHistory);

                arInvoice.setBalance(arInvoice.getBalance() - arPayment.getTransactionAmount());
                arInvoice.setBalanceInProcess(arInvoice.getBalanceInProcess() - arPayment.getTransactionAmount());

                String apInvoice = customerNumber + "-" + invoiceNumber;
                String invoiceOrBl = (CommonUtils.isNotEmpty(arInvoice.getBillLaddingNo()) ? arInvoice.getBillLaddingNo() : arInvoice.getInvoiceNumber());
                String moduleRefId = arInvoice.getCustNo() + "-" + invoiceOrBl;
                StringBuilder desc = new StringBuilder();
                desc.append("For Amount : ").append(NumberUtils.formatNumber(-arPayment.getTransactionAmount())).append(",");
                desc.append(" removed from ");
                desc.append(CommonUtils.isEqualIgnoreCase(arInvoice.getApInvoiceStatus(), STATUS_EDI_ASSIGNED) ? "EDI" : "AP");
                desc.append(" Invoice - '").append(apInvoice).append("'");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), arModule, moduleRefId, NotesConstants.AR_INVOICE, user);
            }
        }
        oldInvoice.setTransactionAmt(oldInvoice.getTransactionAmt() + invoiceAmount);
        oldInvoice.setBalance(oldInvoice.getBalance() + invoiceAmount);
        oldInvoice.setBalanceInProcess(oldInvoice.getBalanceInProcess() + invoiceAmount);
        oldInvoice.setUpdatedOn(new Date());
        oldInvoice.setUpdatedBy(user.getUserId());
        transactionDAO.update(oldInvoice);
        //Ar Transaction History
        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
        arTransactionHistory.setCustomerNumber(customerNumber);
        arTransactionHistory.setBlNumber(null);
        arTransactionHistory.setInvoiceNumber(invoiceNumber);
        arTransactionHistory.setInvoiceDate(oldInvoice.getTransactionDate());
        arTransactionHistory.setTransactionDate(oldInvoice.getTransactionDate());
        arTransactionHistory.setPostedDate(postedDate);
        arTransactionHistory.setTransactionAmount(invoiceAmount);
        arTransactionHistory.setArBatchId(null);
        arTransactionHistory.setApBatchId(null);
        arTransactionHistory.setTransactionType("AP CN");
        arTransactionHistory.setCreatedBy(user.getLoginName());
        arTransactionHistory.setCreatedDate(new Date());
        new ArTransactionHistoryDAO().save(arTransactionHistory);
        //PJ Subledger
        TransactionLedger pjSubledger = new TransactionLedger();
        pjSubledger.setCustNo(customerNumber);
        pjSubledger.setCustName(oldInvoice.getCustName());
        pjSubledger.setInvoiceNumber(invoiceNumber);
        pjSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("arControlAccount"));
        pjSubledger.setTransactionAmt(invoiceAmount);
        pjSubledger.setBalance(invoiceAmount);
        pjSubledger.setBalanceInProcess(invoiceAmount);
        pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        pjSubledger.setStatus(STATUS_OPEN);
        pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
        pjSubledger.setTransactionDate(oldInvoice.getTransactionDate());
        pjSubledger.setPostedDate(postedDate);
        pjSubledger.setCreatedOn(new Date());
        pjSubledger.setCreatedBy(user.getUserId());
        pjSubledger.setUpdatedOn(null);
        pjSubledger.setUpdatedBy(null);
        accrualsDAO.save(pjSubledger);

        String glPeriod = DateUtils.formatDate(postedDate, "yyyyMM");
        StringBuilder desc = new StringBuilder();
        desc.append("Invoice '").append(invoiceNumber).append("'");
        desc.append(" of '").append(oldInvoice.getCustName()).append("'");
        desc.append(" deleted on GL Period '").append(glPeriod).append("'");
        desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount)).append("'");
        desc.append(" by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), arModule, customerNumber + "-" + invoiceNumber, arModule, user);
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(invoiceNumber, customerNumber, TRANSACTION_TYPE_ACCOUNT_PAYABLE);

        if (null != apInvoice) {
            // delete invoice records from ap_invoice when released
            apInvoiceDAO.delete(apInvoice);
        } else {
            // archive invoice records from edi_invoice when released
            EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(customerNumber, invoiceNumber, STATUS_EDI_POSTED_TO_AP);
            if (null != ediInvoice) {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_ARCHIVE);
                ediInvoiceDAO.update(ediInvoice);
            }
        }
    }

    private void unInprogressAccruals(String vendorNumber, String invoiceNumber, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        String moduleId = NotesConstants.ACCRUALS;
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        Disjunction status = Restrictions.disjunction();
        status.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
        status.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
        criteria.add(status);
        List<TransactionLedger> accruals = criteria.list();
        if (CommonUtils.isNotEmpty(accruals)) {
            for (TransactionLedger accrual : accruals) {
                String previousStatus = accrual.getStatus();
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(STATUS_OPEN);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(user.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                        Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            columns.put("b.invoice_number", accrual.getInvoiceNumber());
                            columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                            accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                        } else {
                            columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                            columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                            accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                        }
                    } else {
                        FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                        if (null != fclBlCost) {
                            fclBlCost.setInvoiceNumber(invoiceNumber);
                            fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        }
                    }
                }
                StringBuilder desc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                    desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
                }
                desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                desc.append(" is unmarked as In Progress for");
                desc.append(CommonUtils.isEqualIgnoreCase(previousStatus, STATUS_EDI_IN_PROGRESS) ? "EDI" : "");
                desc.append(" Invoice - '").append(invoiceNumber).append("'");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getTransactionId().toString(), moduleId, user);
            }
        }
    }

    public void unInprogressArs(String vendorNumber, String invoiceNumber, Integer invoiceId, User user) throws Exception {
        String moduleId = NotesConstants.AR_INVOICE;
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("apInvoiceId", invoiceId));
        Disjunction status = Restrictions.disjunction();
        status.add(Restrictions.eq("apInvoiceStatus", STATUS_IN_PROGRESS));
        status.add(Restrictions.eq("apInvoiceStatus", STATUS_EDI_IN_PROGRESS));
        criteria.add(status);
        List<Transaction> ars = criteria.list();
        for (Transaction ar : ars) {
            String previousStatus = ar.getApInvoiceStatus();
            String moduleRefId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
            ar.setApInvoiceAmount(0d);
            ar.setApInvoiceId(null);
            ar.setApInvoiceStatus(null);
            ar.setBalanceInProcess(ar.getBalance());
            ar.setUpdatedOn(new Date());
            ar.setUpdatedBy(user.getUserId());
            String apInvoice = vendorNumber + "-" + invoiceNumber;
            StringBuilder desc = new StringBuilder();
            desc.append("Unmarked as In Progress for ");
            desc.append(CommonUtils.isEqualIgnoreCase(previousStatus, STATUS_EDI_IN_PROGRESS) ? "EDI" : "AP");
            desc.append(" Invoice - '").append(apInvoice).append("'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
    }

    private void deleteInprogressInvoice(String id, User user) throws Exception {
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.findById(id);
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = apInvoice.getAccountNumber() + "-" + apInvoice.getInvoiceNumber();
        StringBuilder desc = new StringBuilder();
        desc.append("In Progress Invoice - ").append(apInvoice.getInvoiceNumber());
        desc.append(" of ").append(apInvoice.getCustomerName());
        desc.append(" (").append(apInvoice.getAccountNumber()).append(")");
        desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
        desc.append(" deleted by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        unInprogressAccruals(apInvoice.getAccountNumber(), apInvoice.getInvoiceNumber(), user);
        unInprogressArs(apInvoice.getAccountNumber(), apInvoice.getInvoiceNumber(), apInvoice.getId(), user);
        if (null != apInvoice) {
            apInvoiceDAO.delete(apInvoice);
        }
    }

    private void deleteEdiInprogressInvoice(String id, User user) throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(id);
        ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_ARCHIVE);
        StringBuilder desc = new StringBuilder();
        desc.append("EDI Invoice - ").append(ediInvoice.getInvoiceNumber());
        desc.append(" archived by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        unInprogressAccruals(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), user);
        unInprogressArs(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), ediInvoice.getId(), user);
    }

    private void deleteInvoice(String ids, User user) throws Exception {
        for (String id : StringUtils.split(ids, ",")) {
            if (id.endsWith("AP")) {
                deleteApInvoice(id.replace("AP", ""), user);
            } else if (id.endsWith("AR")) {
                deleteArInvoice(id.replace("AR", ""), user);
            } else if (id.endsWith("INV")) {
                deleteInprogressInvoice(id.replace("INV", ""), user);
            } else {
                deleteEdiInprogressInvoice(id.replace("EDI", ""), user);
            }
        }
    }

    public void save(String payIds, String holdIds, String deleteIds, User user) throws Exception {
        if (CommonUtils.isNotEmpty(payIds)) {
            setReadyToPayInvoice(payIds, user);
        }
        if (CommonUtils.isNotEmpty(holdIds)) {
            holdInvoice(holdIds, user);
        }
        if (CommonUtils.isNotEmpty(deleteIds)) {
            deleteInvoice(deleteIds, user);
        }
    }    private FclBl getFclBl(String blNumber) {
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.eq("bolId", blNumber));
        criteria.setMaxResults(1);
        return (FclBl) criteria.uniqueResult();
    }
}
