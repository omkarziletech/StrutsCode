package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.accounting.form.ApInquiryForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.accounting.model.VendorModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApInquiryDAO extends BaseHibernateDAO implements ConstantsInterface {

    public String getAcInvoiceNumbers(String searchBy, String searchValue, boolean isFullSearch) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select distinct(concat(\"'\",ac.invoice_number,\"'\")) as invoiceNumber");
        queryBuilder.append(" from transaction_ledger ac");
        queryBuilder.append(" where ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and ac.invoice_number != ''");
        queryBuilder.append(" and ac.").append(searchBy);
        if (isFullSearch) {
            queryBuilder.append(" = '").append(searchValue).append("'");
        } else if (searchBy.equalsIgnoreCase("drcpt")) {
            if (searchValue.trim().length() == 6 || searchValue.trim().length() < 3) {
                queryBuilder.append(" = '").append(searchValue).append("'");
            } else{
                queryBuilder.append(" like '").append(searchValue).append("%'");
            }
        } else {
            queryBuilder.append(" like '%").append(searchValue).append("%'");
        }
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    private String getCheckNumbers(String fromAmount, String toAmount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select distinct(concat(\"'\",check_number,\"'\")) as check_number");
        queryBuilder.append(" from ap_payment_check");
        queryBuilder.append(" where check_number != ''");
        if (CommonUtils.isNotEmpty(fromAmount)) {
            queryBuilder.append(" and check_amount >= ").append(fromAmount);
        }
        if (CommonUtils.isNotEmpty(toAmount)) {
            queryBuilder.append(" and check_amount <= ").append(toAmount);
        }
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    public String buildApQuery(ApInquiryForm apInquiryForm, String invoiceNumbers)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from transaction ap");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ap.cust_no = tp.acct_no)");
        queryBuilder.append(" where ap.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        if (CommonUtils.isNotEmpty(invoiceNumbers)) {
            queryBuilder.append(" and ap.invoice_number in (").append(invoiceNumbers).append(")");
        } else if (CommonUtils.isNotEmpty(apInquiryForm.getSearchBy())) {
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_number")) {
                if (apInquiryForm.isFullSearch()) {
                    queryBuilder.append(" and ap.invoice_number = '").append(apInquiryForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" and ap.invoice_number like '%").append(apInquiryForm.getSearchValue()).append("%'");
                }
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_amount")) {
                if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
                    queryBuilder.append(" and ap.transaction_amt >= ").append(apInquiryForm.getFromAmount());
                }
                if (CommonUtils.isNotEmpty(apInquiryForm.getToAmount())) {
                    queryBuilder.append(" and ap.transaction_amt <= ").append(apInquiryForm.getToAmount());
                }
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "check_number")) {
                if (apInquiryForm.isFullSearch()) {
                    queryBuilder.append(" and (ap.cheque_number = '").append(apInquiryForm.getSearchValue()).append("'");
                    queryBuilder.append(" or ap.ach_batch_sequence = '").append(apInquiryForm.getSearchValue().trim()).append("'");
                    queryBuilder.append(" or ap.ap_batch_id = '").append(apInquiryForm.getSearchValue().trim()).append("'");
                    queryBuilder.append(" or ap.ar_batch_id = '").append(apInquiryForm.getSearchValue().trim()).append("')");
                } else {
                    queryBuilder.append(" and (ap.cheque_number like '%").append(apInquiryForm.getSearchValue()).append("%'");
                    queryBuilder.append(" or ap.ach_batch_sequence like '%").append(apInquiryForm.getSearchValue()).append("%')");
                }
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "check_amount")) {
                String checkNumbers = getCheckNumbers(apInquiryForm.getFromAmount(), apInquiryForm.getToAmount());
                queryBuilder.append(" and (ap.cheque_number in (").append(checkNumbers).append(")");
                queryBuilder.append(" or ap.ach_batch_sequence in (").append(checkNumbers).append("))");
            }
        } else {
            if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowSubsidiairy(), YES)){
                    queryBuilder.append(" or tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                }
                queryBuilder.append(")");
            }
            StringBuilder statuses = new StringBuilder();
            statuses.append("'").append(STATUS_OPEN).append("',");
            statuses.append("'").append(STATUS_READY_TO_PAY).append("',");
            statuses.append("'").append(STATUS_PENDING).append("',");
            statuses.append("'").append(STATUS_HOLD).append("'");
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Open")) {
                queryBuilder.append(" and ap.status in (").append(statuses.toString()).append(")");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Posted")) {
                statuses.append(",'").append(STATUS_PAID).append("'");
                queryBuilder.append(" and ap.status in (").append(statuses.toString()).append(")");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Paid")) {
                queryBuilder.append(" and ap.status = '").append(STATUS_PAID).append("'");
            }
        }
        String searchDateBy = CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), "invoice_date") ? "transaction_date" : "check_date";
        if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and ap.").append(searchDateBy).append(" >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and ap.").append(searchDateBy).append(" <= '").append(toDate).append("'");
        }
        return queryBuilder.toString();
    }

    public String buildAcQuery(ApInquiryForm apInquiryForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from transaction_ledger ac");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ac.cust_no = tp.acct_no)");
        queryBuilder.append(" where ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        if (CommonUtils.isNotEmpty(apInquiryForm.getSearchBy())) {
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_number")) {
                if (apInquiryForm.isFullSearch()) {
                    queryBuilder.append(" and ac.invoice_number = '").append(apInquiryForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" and ac.invoice_number like '%").append(apInquiryForm.getSearchValue()).append("%'");
                }
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "drcpt")) {
                 queryBuilder.append(" and ac.").append(apInquiryForm.getSearchBy());
                if (apInquiryForm.getSearchValue().trim().length() == 6 || apInquiryForm.getSearchValue().trim().length() < 3) {
                    queryBuilder.append(" = '").append(apInquiryForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" like '").append(apInquiryForm.getSearchValue()).append("%'");
                }
            }
            else{
                queryBuilder.append(" and ac.").append(apInquiryForm.getSearchBy());
                if (apInquiryForm.isFullSearch()) {
                    queryBuilder.append(" = '").append(apInquiryForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" like '%").append(apInquiryForm.getSearchValue()).append("%'");
                }
            }
        } else {
            if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowSubsidiairy(), YES)){
                    queryBuilder.append(" or  tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                }
                queryBuilder.append(")");
            }
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "All")
                    && CommonUtils.isNotEqualIgnoreCase(apInquiryForm.getShowAccruals(), "All")) {
                queryBuilder.append(" and ac.status in ('").append(STATUS_DISPUTE).append("','").append(STATUS_EDI_DISPUTE).append("',");
                queryBuilder.append("'").append(STATUS_IN_PROGRESS).append("','").append(STATUS_EDI_IN_PROGRESS).append("')");
            }
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Disputed")) {
                queryBuilder.append(" and ac.status in ('").append(STATUS_DISPUTE).append("','").append(STATUS_EDI_DISPUTE).append("')");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "In Progress")) {
                queryBuilder.append(" and ac.status in ('").append(STATUS_IN_PROGRESS).append("','").append(STATUS_EDI_IN_PROGRESS).append("')");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), "Open")) {
                queryBuilder.append(" and ac.status = '").append(STATUS_OPEN).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), "Deleted")) {
                queryBuilder.append(" and ac.status = '").append(STATUS_DELETED).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), "Inactive")) {
                queryBuilder.append(" and ac.status = '").append(STATUS_INACTIVE).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), "Assigned")) {
                queryBuilder.append(" and ac.status in ('").append(STATUS_ASSIGN).append("','").append(STATUS_EDI_ASSIGNED).append("')");
            }
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and ac.transaction_date >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and ac.transaction_date <= '").append(toDate).append("'");
        }
        return queryBuilder.toString();
    }

    public String buildArQuery(ApInquiryForm apInquiryForm)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from transaction ar");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ar.cust_no = tp.acct_no)");
        queryBuilder.append(" where ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (CommonUtils.isNotEmpty(apInquiryForm.getSearchBy())) {
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_number")) {
                if (apInquiryForm.isFullSearch()) {
                    queryBuilder.append(" and ar.invoice_number = '").append(apInquiryForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" and ar.invoice_number like '%").append(apInquiryForm.getSearchValue()).append("%'");
                }
            } else {
                queryBuilder.append(" and ar.").append(apInquiryForm.getSearchBy());
                if (apInquiryForm.isFullSearch()) {
                    queryBuilder.append(" = '").append(apInquiryForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" like '%").append(apInquiryForm.getSearchValue()).append("%'");
                }
            }
        } else {
            if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowSubsidiairy(), YES)){
                    queryBuilder.append(" or  tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                }
                queryBuilder.append(")");
            }
            StringBuilder statuses = new StringBuilder();
            statuses.append("'").append(STATUS_OPEN).append("',");
            statuses.append("'").append(STATUS_READY_TO_PAY).append("'");
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Open")) {
                queryBuilder.append(" and (ar.status in (").append(statuses.toString()).append(")");
                queryBuilder.append(" or ar.balance <> 0.00)");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Posted")) {
                statuses.append("'").append(STATUS_PAID).append("'");
                queryBuilder.append(" and (ar.status in (").append(statuses.toString()).append(")");
                queryBuilder.append(" or ar.balance <> 0.00)");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Paid")) {
                queryBuilder.append(" and (ar.status = '").append(STATUS_PAID).append("'");
                queryBuilder.append(" or ar.balance = 0.00)");
            }
        }
        String searchDateBy = CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), "invoice_date") ? "transaction_date" : "check_date";
        if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and ar.").append(searchDateBy).append(" >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and ar.").append(searchDateBy).append(" <= '").append(toDate).append("'");
        }
        queryBuilder.append(" group by ar.cust_no,if(ar.bill_ladding_no != '', ar.bill_ladding_no,ar.invoice_number)");
        return queryBuilder.toString();
    }

    public String buildInvQuery(ApInquiryForm apInquiryForm, String invoiceNumbers)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ap_invoice inv");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (inv.account_number = tp.acct_no)");
        queryBuilder.append(" where inv.status != '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        if (CommonUtils.isNotEmpty(invoiceNumbers)) {
            queryBuilder.append(" and inv.invoice_number in (").append(invoiceNumbers).append(")");
        } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_number")) {
            if (apInquiryForm.isFullSearch()) {
                queryBuilder.append(" and inv.invoice_number = '").append(apInquiryForm.getSearchValue()).append("'");
            } else {
                queryBuilder.append(" and inv.invoice_number like '%").append(apInquiryForm.getSearchValue()).append("%'");
            }
        } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_amount")) {
            if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
                queryBuilder.append(" and inv.invoice_amount >= ").append(apInquiryForm.getFromAmount());
            }
            if (CommonUtils.isNotEmpty(apInquiryForm.getToAmount())) {
                queryBuilder.append(" and inv.invoice_amount <= ").append(apInquiryForm.getToAmount());
            }
        } else {
            if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowSubsidiairy(), YES)){
                    queryBuilder.append(" or  tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                }
                queryBuilder.append(")");
            }
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Disputed")) {
                queryBuilder.append(" and inv.status = '").append(STATUS_DISPUTE).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "In Progress")) {
                queryBuilder.append(" and inv.status = '").append(STATUS_IN_PROGRESS).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Rejected")) {
                queryBuilder.append(" and inv.status = '").append(STATUS_REJECT).append("'");
            }
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and inv.date >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and inv.date <= '").append(toDate).append("'");
        }
        return queryBuilder.toString();
    }

    public String buildEdiQuery(ApInquiryForm apInquiryForm, String invoiceNumbers)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from edi_invoice edi");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (edi.vendor_number = tp.acct_no)");
        StringBuilder statuses = new StringBuilder();
        statuses.append("'").append(STATUS_EDI_DUPLICATE).append("',");
        statuses.append("'").append(STATUS_EDI_ARCHIVE).append("',");
        statuses.append("'").append(STATUS_EDI_POSTED_TO_AP).append("'");
        queryBuilder.append(" where edi.status not in(").append(statuses.toString()).append(")");
        if (CommonUtils.isNotEmpty(invoiceNumbers)) {
            queryBuilder.append(" and edi.invoice_number in (").append(invoiceNumbers).append(")");
        } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_number")) {
            if (apInquiryForm.isFullSearch()) {
                queryBuilder.append(" and edi.invoice_number = '").append(apInquiryForm.getSearchValue()).append("'");
            } else {
                queryBuilder.append(" and edi.invoice_number like '%").append(apInquiryForm.getSearchValue()).append("%'");
            }
        } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_amount")) {
            if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
                queryBuilder.append(" and edi.invoice_amount >= ").append(apInquiryForm.getFromAmount());
            }
            if (CommonUtils.isNotEmpty(apInquiryForm.getToAmount())) {
                queryBuilder.append(" and edi.invoice_amount <= ").append(apInquiryForm.getToAmount());
            }
        } else {
            if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
                queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowSubsidiairy(), YES)){
                    queryBuilder.append(" or  tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
                }
                queryBuilder.append(")");
            }
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Disputed")) {
                queryBuilder.append(" and edi.status = '").append(STATUS_EDI_DISPUTE).append("'");
            } else {
                queryBuilder.append(" and edi.status = '").append(STATUS_EDI_IN_PROGRESS).append("'");
            }
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            queryBuilder.append(" and edi.invoice_date >= '").append(fromDate).append("'");
        }
        if (CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
            String toDate = DateUtils.formatDate(DateUtils.parseDate(apInquiryForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and edi.invoice_date <= '").append(toDate).append("'");
        }
        return queryBuilder.toString();
    }

    public VendorModel getVendor(String vendorNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(vendor_number) as vendorNumber,");
        queryBuilder.append("upper(vendor_name) as vendorName,");
        queryBuilder.append("upper(sub_type) as subType,");
        queryBuilder.append("ecu_designation as ecuDesignation,");
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
        query.setResultTransformer(Transformers.aliasToBean(VendorModel.class));
        return (VendorModel) query.uniqueResult();
    }

    private Integer getApRows(String apQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(ap.transaction_id)").append(apQuery);
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getAcRows(String acQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(ac.transaction_id)").append(acQuery);
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
        String acQuery = queries.get("acQuery");
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
        if (CommonUtils.isNotEmpty(acQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(select sum(if(datediff(current_date(),ac.transaction_date) between 0 and 30,ac.balance,0)) as age_30_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),ac.transaction_date) between 31 and 60,ac.balance,0)) as age_60_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),ac.transaction_date) between 61 and 90,ac.balance,0)) as age_90_amount,");
            queryBuilder.append("sum(if(datediff(current_date(),ac.transaction_date) >= 91,ac.balance,0)) as age_91_amount,");
            queryBuilder.append("sum(ac.balance) as ap_amount,");
            queryBuilder.append("0 as ar_amount");
            queryBuilder.append(acQuery).append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(arQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(select 0 as age_30_amount,");
            queryBuilder.append("0 as age_60_amount,");
            queryBuilder.append("0 as age_90_amount,");
            queryBuilder.append("0 as age_91_amount,");
            queryBuilder.append("0 as ap_amount,");
            queryBuilder.append("-sum(ar.balance) as ar_amount");
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
        String acQuery = queries.get("acQuery");
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
        queryBuilder.append("date_format(date_add(ap.invoice_date, interval coalesce((select ge.code from vendor_info ve join genericcode_dup ge on (ge.id = ve.credit_terms) where ve.cust_accno = ap.vendor_number limit 1), 0) day),'%m/%d/%Y') as dueDate,");
        queryBuilder.append("format(ap.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("format(ap.invoice_balance,2) as invoiceBalance,");
        queryBuilder.append("upper(ap.check_number) as checkNumber,");
        queryBuilder.append("date_format(ap.cleared_date,'%m/%d/%Y') as clearedDate,");
        queryBuilder.append("date_format(ap.payment_date,'%m/%d/%Y') as paymentDate,");
        queryBuilder.append("upper(ap.dock_receipt) as dockReceipt,");
        queryBuilder.append("upper(ap.voyage) as voyage,");
        queryBuilder.append("upper(ap.cost_code) as costCode,");
        queryBuilder.append("ap.transaction_type as transactionType,");
        queryBuilder.append("ap.status as status,");
        queryBuilder.append("ap.cost_id as costId,");
        queryBuilder.append("ap.id as id,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','true','false') as overhead,");
        queryBuilder.append("if(count(nt.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as uploaded,");
        queryBuilder.append("ap.note_module_id as noteModuleId,");
        queryBuilder.append("ap.note_ref_id as noteRefId,");
        queryBuilder.append("ap.document_id as documentId");
        queryBuilder.append(" from (");
        queryBuilder.append("select ap.vendor_number as vendor_number,");
        queryBuilder.append("ap.vendor_name as vendor_name,");
        queryBuilder.append("ap.bl_number as bl_number,");
        queryBuilder.append("ap.invoice_number as invoice_number,");
        queryBuilder.append("ap.invoice_or_bl as invoice_or_bl,");
        queryBuilder.append("ap.invoice_date as invoice_date,");
        queryBuilder.append("ap.invoice_amount as invoice_amount,");
        queryBuilder.append("ap.invoice_balance as invoice_balance,");
        queryBuilder.append("ap.check_number as check_number,");
        queryBuilder.append("ap.cleared_date as cleared_date,");
        queryBuilder.append("ap.payment_date as payment_date,");
        queryBuilder.append("ap.dock_receipt as dock_receipt,");
        queryBuilder.append("ap.voyage as voyage,");
        queryBuilder.append("ap.cost_code as cost_code,");
        queryBuilder.append("ap.transaction_type as transaction_type,");
        queryBuilder.append("ap.status as status,");
        queryBuilder.append("ap.cost_id as cost_id,");
        queryBuilder.append("ap.id as id,");
        queryBuilder.append("ap.note_module_id as note_module_id,");
        queryBuilder.append("ap.note_ref_id as note_ref_id,");
        queryBuilder.append("ap.document_id as document_id");
        queryBuilder.append(" from (");
        boolean isunion = false;
        if (CommonUtils.isNotEmpty(apQuery)) {
            StringBuilder statuses = new StringBuilder();
            statuses.append("if(ap.status = '").append(STATUS_HOLD).append("','Hold',");
            statuses.append("if(ap.status = '").append(STATUS_REJECT).append("','Deleted',");
            statuses.append("if(ap.status = '").append(STATUS_PENDING).append("','Pending',");
            statuses.append("if(ap.status = '").append(STATUS_READY_TO_PAY).append("','Ready To Pay',");
            statuses.append("if(ap.status = '").append(STATUS_PAID).append("','Paid',");
            statuses.append("if(ap.status = '").append(STATUS_WAITING_FOR_APPROVAL).append("','Waiting For Approval','Open'))))))");
            String checkNumber = "if((ap.ach_batch_sequence <> ''),convert(concat('ACH - ',ap.ach_batch_sequence) using latin1),ap.cheque_number)";
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("null as bl_number,");
            queryBuilder.append("ap.invoice_number as invoice_number,");
            queryBuilder.append("ap.invoice_number as invoice_or_bl,");
            queryBuilder.append("ap.transaction_date as invoice_date,");
            queryBuilder.append("ap.transaction_amt as invoice_amount,");
            queryBuilder.append("ap.balance as invoice_balance,");
            queryBuilder.append("cast(").append(checkNumber).append(" as char charset latin1) as check_number,");
            queryBuilder.append("ap.cleared_date as cleared_date,");
            queryBuilder.append("ap.check_date as payment_date,");
            queryBuilder.append("null as dock_receipt,");
            queryBuilder.append("null as voyage,");
            queryBuilder.append("null as cost_code,");
            queryBuilder.append("ap.transaction_type as transaction_type,");
            queryBuilder.append(statuses.toString()).append(" as status,");
            queryBuilder.append("null as cost_id,");
            queryBuilder.append("cast(ap.transaction_id as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AP_INVOICE).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(concat(ap.cust_no,'-',ap.invoice_number) as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(concat(ap.cust_no,'-',ap.invoice_number) as char character set latin1) as document_id");
            queryBuilder.append(apQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(acQuery)) {
            StringBuilder statuses = new StringBuilder();
            statuses.append("if(ac.status = '").append(STATUS_INACTIVE).append("','Inactive',");
            statuses.append("if(ac.status = '").append(STATUS_DELETED).append("','Deleted',");
            statuses.append("if(ac.status = '").append(STATUS_IN_PROGRESS).append("','In Progress',");
            statuses.append("if(ac.status = '").append(STATUS_PENDING).append("','Pending',");
            statuses.append("if(ac.status = '").append(STATUS_DISPUTE).append("','Disputed',");
            statuses.append("if(ac.status = '").append(STATUS_EDI_DISPUTE).append("','EDI Disputed',");
            statuses.append("if(ac.status = '").append(STATUS_ASSIGN).append("','Assigned',ac.status)))))))");
            queryBuilder.append(isunion ? " union " : "");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("ac.bill_ladding_no as bl_number,");
            queryBuilder.append("ac.invoice_number as invoice_number,");
            queryBuilder.append("if(ac.invoice_number != '',ac.invoice_number,ac.bill_ladding_no) as invoice_or_bl,");
            queryBuilder.append("ac.transaction_date as invoice_date,");
            queryBuilder.append("ac.transaction_amt as invoice_amount,");
            queryBuilder.append("ac.balance as invoice_balance,");
            queryBuilder.append("null as check_number,");
            queryBuilder.append("null as cleared_date,");
            queryBuilder.append("null as payment_date,");
            queryBuilder.append("ac.drcpt as dock_receipt,");
            queryBuilder.append("ac.voyage_no as voyage,");
            queryBuilder.append("ac.charge_code as cost_code,");
            queryBuilder.append("ac.transaction_type as transaction_type,");
            queryBuilder.append(statuses.toString()).append(" as status,");
            queryBuilder.append("cast(ac.cost_id as char character set latin1) as cost_id,");
            queryBuilder.append("cast(ac.transaction_id as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.ACCRUALS).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(ac.transaction_id as char character set latin1) as note_ref_id,");
            String documentId = "if(ac.invoice_number != '',concat(ac.cust_no,'-',ac.invoice_number),null)";
            queryBuilder.append("cast(").append(documentId).append(" as char character set latin1) as document_id");
            queryBuilder.append(acQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(arQuery)) {
            StringBuilder statuses = new StringBuilder();
            statuses.append("if(ar.status = '").append(STATUS_PAID).append("' or ar.balance = 0.00,'Paid',");
            statuses.append("if(ar.status = '").append(STATUS_READY_TO_PAY).append("','Ready To Pay',");
            statuses.append("if(ar.status = '").append(STATUS_WAITING_FOR_APPROVAL).append("','Waiting For Approval','Open')))");
            queryBuilder.append(isunion ? " union " : "");
            StringBuilder blNumber = new StringBuilder();
            blNumber.append("if(ar.bill_ladding_no like '%04-%',");
            blNumber.append("replace(replace(ar.bill_ladding_no,substring_index(ar.bill_ladding_no,'04-',1),''),'-',''),ar.bill_ladding_no)");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append(blNumber).append(" as bl_number,");
            queryBuilder.append("ar.invoice_number as invoice_number,");
            queryBuilder.append("if(ar.bill_ladding_no != '',").append(blNumber).append(",ar.invoice_number) as invoice_or_bl,");
            queryBuilder.append("min(ar.transaction_date) as invoice_date,");
            queryBuilder.append("-sum(ar.transaction_amt) as invoice_amount,");
            queryBuilder.append("-sum(ar.balance) as invoice_balance,");
            queryBuilder.append("ar.cheque_number as check_number,");
            queryBuilder.append("null as cleared_date,");
            queryBuilder.append("max(ar.check_date) as payment_date,");
            queryBuilder.append("ar.drcpt as dock_receipt,");
            queryBuilder.append("ar.voyage_no as voyage,");
            queryBuilder.append("ar.charge_code as cost_code,");
            queryBuilder.append("ar.transaction_type as transaction_type,");
            queryBuilder.append(statuses.toString()).append(" as status,");
            queryBuilder.append("null as cost_id,");
            queryBuilder.append("cast(group_concat(ar.transaction_id) as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AR_INVOICE).append("' as char character set latin1) as note_module_id,");
            String referenceId = "concat(ar.cust_no,'-',if(ar.bill_ladding_no != '',ar.bill_ladding_no,ar.invoice_number))";
            queryBuilder.append("cast(").append(referenceId).append(" as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(").append(referenceId).append(" as char character set latin1) as document_id");
            queryBuilder.append(arQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(invQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            StringBuilder statuses = new StringBuilder();
            statuses.append("if(inv.status = '").append(STATUS_DISPUTE).append("','Disputed',");
            statuses.append("if(inv.status = '").append(STATUS_REJECT).append("','Rejected','In Progress'))");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("null as bl_number,");
            queryBuilder.append("inv.invoice_number as invoice_number,");
            queryBuilder.append("inv.invoice_number as invoice_or_bl,");
            queryBuilder.append("inv.date as invoice_date,");
            queryBuilder.append("inv.invoice_amount as invoice_amount,");
            queryBuilder.append("inv.invoice_amount as invoice_balance,");
            queryBuilder.append("null as check_number,");
            queryBuilder.append("null as cleared_date,");
            queryBuilder.append("null as payment_date,");
            queryBuilder.append("null as dock_receipt,");
            queryBuilder.append("null as voyage,");
            queryBuilder.append("null as cost_code,");
            queryBuilder.append("'").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("' as transaction_type,");
            queryBuilder.append(statuses.toString()).append(" as status,");
            queryBuilder.append("null as cost_id,");
            queryBuilder.append("cast(inv.id as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AP_INVOICE).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(concat(inv.account_number,'-',inv.invoice_number) as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(concat(inv.account_number,'-',inv.invoice_number) as char character set latin1) as document_id");
            queryBuilder.append(invQuery);
            queryBuilder.append(")");
            isunion = true;
        }
        if (CommonUtils.isNotEmpty(ediQuery)) {
            queryBuilder.append(isunion ? " union " : "");
            StringBuilder statuses = new StringBuilder();
            statuses.append("if(edi.status = '").append(STATUS_EDI_DISPUTE).append("','EDI Disputed',");
            statuses.append("if(edi.status = '").append(STATUS_EDI_OPEN).append("','EDI In Progress',edi.status))");
            queryBuilder.append("(");
            queryBuilder.append("select tp.acct_no as vendor_number,");
            queryBuilder.append("tp.acct_name as vendor_name,");
            queryBuilder.append("null as bl_number,");
            queryBuilder.append("edi.invoice_number as invoice_number,");
            queryBuilder.append("edi.invoice_number as invoice_or_bl,");
            queryBuilder.append("edi.invoice_date as invoice_date,");
            queryBuilder.append("edi.invoice_amount as invoice_amount,");
            queryBuilder.append("edi.invoice_amount as invoice_balance,");
            queryBuilder.append("null as check_number,");
            queryBuilder.append("null as cleared_date,");
            queryBuilder.append("null as payment_date,");
            queryBuilder.append("null as dock_receipt,");
            queryBuilder.append("null as voyage,");
            queryBuilder.append("null as cost_code,");
            queryBuilder.append("'").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("' as transaction_type,");
            queryBuilder.append(statuses.toString()).append(" as status,");
            queryBuilder.append("null as cost_id,");
            queryBuilder.append("cast(edi.id as char character set latin1) as id,");
            queryBuilder.append("cast('").append(NotesConstants.AP_INVOICE).append("' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(concat(edi.vendor_number,'-',edi.invoice_number) as char character set latin1) as note_ref_id,");
            queryBuilder.append("cast(concat(edi.vendor_number,'-',edi.invoice_number) as char character set latin1) as document_id");
            queryBuilder.append(ediQuery);
            queryBuilder.append(")");
        }
        queryBuilder.append(") as ap");
        queryBuilder.append(" group by ap.vendor_number,ap.invoice_or_bl,ap.transaction_type,ap.id");
        queryBuilder.append(" order by ap.").append(sortBy).append(" ").append(orderBy);
        if (limit != 0) {
            queryBuilder.append(" limit ").append(start).append(",").append(limit);
        }
        queryBuilder.append(") as ap");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ap.vendor_number = tp.acct_no)");
        queryBuilder.append(" left join notes nt");
        queryBuilder.append(" on (ap.note_module_id = nt.module_id");
        queryBuilder.append(" and ap.note_ref_id = nt.module_ref_id");
        queryBuilder.append(" and nt.note_type = 'Manual')");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name = 'INVOICE'");
        queryBuilder.append(" and doc.document_name = 'INVOICE'");
        queryBuilder.append(" and ap.document_id = doc.document_id)");
        queryBuilder.append(" where ap.vendor_number != ''");
        queryBuilder.append(" group by ap.vendor_number,ap.invoice_or_bl,ap.transaction_type,ap.id");
        queryBuilder.append(" order by ap.").append(sortBy).append(" ").append(orderBy);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public void search(ApInquiryForm apInquiryForm) throws Exception {
        VendorModel vendor = null;
        if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
            vendor = getVendor(apInquiryForm.getVendorNumber());
        }
        Map<String, String> queries = new HashMap<String, String>();
        String apQuery = null;
        String acQuery = null;
        String arQuery = null;
        String invQuery = null;
        String ediQuery = null;
        if (CommonUtils.isNotEmpty(apInquiryForm.getSearchBy())) {
            String[] filters = new String[]{"drcpt", "voyage_no", "container_no", "booking_no", "bill_ladding_no", "sub_house_bl", "master_bl"};
            if (CommonUtils.in(apInquiryForm.getSearchBy(), filters)) {
                acQuery = buildAcQuery(apInquiryForm);
                String invoiceNumbers = getAcInvoiceNumbers(apInquiryForm.getSearchBy(), apInquiryForm.getSearchValue(), apInquiryForm.isFullSearch());
                if (CommonUtils.isNotEmpty(invoiceNumbers)) {
                    apQuery = buildApQuery(apInquiryForm, invoiceNumbers);
                    invQuery = buildInvQuery(apInquiryForm, invoiceNumbers);
                    ediQuery = buildEdiQuery(apInquiryForm, invoiceNumbers);
                }
            } else if (CommonUtils.in(apInquiryForm.getSearchBy(), "invoice_amount", "check_number", "check_amount")) {
                apQuery = buildApQuery(apInquiryForm, null);
            } else {
                apQuery = buildApQuery(apInquiryForm, null);
                acQuery = buildAcQuery(apInquiryForm);
            }
        } else {
            if (CommonUtils.in(apInquiryForm.getShowInvoices(), "All", "Open", "Paid", "Posted")) {
                apQuery = buildApQuery(apInquiryForm, null);
            }
            if (CommonUtils.isNotEmpty(apInquiryForm.getShowAccruals())
                    || CommonUtils.in(apInquiryForm.getShowInvoices(), "All", "Disputed", "In Progress")) {
                acQuery = buildAcQuery(apInquiryForm);
            }
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getAr(), YES)
                && (CommonUtils.in(apInquiryForm.getShowInvoices(), "All", "Open", "Paid", "Posted")
                || CommonUtils.notIn(apInquiryForm.getSearchBy(), "invoice_amount", "check_number", "check_amount"))) {
            arQuery = buildArQuery(apInquiryForm);
        }
        if ((CommonUtils.isEmpty(apInquiryForm.getSearchBy())
                && CommonUtils.in(apInquiryForm.getShowInvoices(), "All", "Disputed", "In Progress"))
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_number")
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), "invoice_amount")) {
            invQuery = buildInvQuery(apInquiryForm, null);
            ediQuery = buildEdiQuery(apInquiryForm, null);
        } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), "Rejected")) {
            invQuery = buildInvQuery(apInquiryForm, null);
        }
        if (CommonUtils.isAtLeastOneNotEmpty(apQuery, acQuery, arQuery, invQuery, ediQuery)) {
            int apRows = CommonUtils.isNotEmpty(apQuery) ? getApRows(apQuery) : 0;
            int acRows = CommonUtils.isNotEmpty(acQuery) ? getAcRows(acQuery) : 0;
            int arRows = CommonUtils.isNotEmpty(arQuery) ? getArRows(arQuery) : 0;
            int invRows = CommonUtils.isNotEmpty(invQuery) ? getInvRows(invQuery) : 0;
            int ediRows = CommonUtils.isNotEmpty(ediQuery) ? getEdiRows(ediQuery) : 0;
            queries.put("apQuery", apRows > 0 ? apQuery : null);
            queries.put("acQuery", acRows > 0 ? acQuery : null);
            queries.put("arQuery", arRows > 0 ? arQuery : null);
            queries.put("invQuery", invRows > 0 ? invQuery : null);
            queries.put("ediQuery", ediRows > 0 ? ediQuery : null);
            int totalRows = apRows + acRows + arRows + invRows + ediRows;
            if (totalRows > 0) {
                if (CommonUtils.in(apInquiryForm.getAction(), "createPdf", "createExcel")) {
                    List<ResultModel> results = getResults(queries, apInquiryForm.getSortBy(), apInquiryForm.getOrderBy(), 0, 0);
                    apInquiryForm.setResults(results);
                } else {
                    int limit = apInquiryForm.getLimit();
                    int start = limit * (apInquiryForm.getSelectedPage() - 1);
                    List<ResultModel> results = getResults(queries, apInquiryForm.getSortBy(), apInquiryForm.getOrderBy(), start, limit);
                    apInquiryForm.setResults(results);
                    apInquiryForm.setSelectedRows(results.size());
                    int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
                    apInquiryForm.setTotalPages(totalPages);
                    apInquiryForm.setTotalRows(totalRows);
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
        apInquiryForm.setVendor(vendor);
    }
}
