package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.Payments;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.PaymentsDAO;
import com.logiware.accounting.form.ArInquiryForm;
import com.logiware.accounting.model.CustomerModel;
import com.logiware.accounting.model.ResultModel;
import com.logiware.accounting.model.VendorModel;
import com.logiware.common.model.Model;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.utils.AuditNotesUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArInquiryDAO extends BaseHibernateDAO implements ConstantsInterface {

    public String getDocReceiptsByVoyage(String voyageNumber, String type) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  concat(\"'\",drcpt,\"'\") ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ");
        queryBuilder.append("where");
        queryBuilder.append("  transaction_type = '").append(type).append("'");
        queryBuilder.append("  and voyage_no like '%").append(voyageNumber).append("%'");
        queryBuilder.append("  and drcpt <> '' ");
        queryBuilder.append("group by drcpt");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "(").replace("]", ")");
        } else {
            return null;
        }
    }

    private String getPaidInvoiceIds(ArInquiryForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  ar.transaction_id as id ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ar");
        queryBuilder.append("  join trading_partner tp");
        queryBuilder.append("    on (");
        queryBuilder.append("      ar.cust_no = tp.acct_no");
        if (CommonUtils.isEmpty(form.getSearchFilter()) && CommonUtils.isNotEmpty(form.getCustomerNumber())) {
            queryBuilder.append("      and (");
            queryBuilder.append("        tp.acct_no = '").append(form.getCustomerNumber()).append("'");
            if (CommonUtils.in("Show Subsidiairy", form.getShowFilters())) {
                queryBuilder.append("        or tp.masteracct_no = '").append(form.getCustomerNumber()).append("'");
            }
            queryBuilder.append("      )");
        }
        queryBuilder.append("    )");
        queryBuilder.append("  join payments py");
        queryBuilder.append("    on (");
        queryBuilder.append("      ar.transaction_id = py.transaction_id");
        queryBuilder.append("      and py.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("    )");
        queryBuilder.append("  join ar_batch ab");
        queryBuilder.append("    on (");
        queryBuilder.append("      py.batch_id = ab.batch_id");
        if (CommonUtils.isEqualIgnoreCase(form.getSearchDate(), "Payment/Adjustment Date")
                && (CommonUtils.isNotEmpty(form.getFromDate()) || CommonUtils.isNotEmpty(form.getToDate()))) {
            if (CommonUtils.isNotEmpty(form.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                queryBuilder.append("      and ab.deposit_date >= '").append(fromDate).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                queryBuilder.append("      and ab.deposit_date <= '").append(toDate).append("'");
            }
        }
        queryBuilder.append("    ) ");
        if (CommonUtils.in(form.getSearchFilter(), SEARCH_BY_CHECK_NUMBER, SEARCH_BY_CHECK_AMOUNT)) {
            queryBuilder.append("  join payment_checks pc");
            queryBuilder.append("    on (");
            queryBuilder.append("      py.payment_check_id = pc.id");
            if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), SEARCH_BY_CHECK_NUMBER)) {
                queryBuilder.append("      and pc.check_no = '").append(form.getSearchValue5().trim()).append("'");
            } else {
                queryBuilder.append("      and pc.received_amt between ").append(form.getFromAmount6()).append(" and ").append(form.getToAmount6());
            }
            queryBuilder.append("    ) ");
        }
        queryBuilder.append("where");
        queryBuilder.append("  ar.transaction_type ='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "").replace("]", "");
        } else {
            return null;
        }
    }

    private String buildArWhere(ArInquiryForm form) throws Exception {
        StringBuilder whereBuilder = new StringBuilder();
        whereBuilder.append("        from");
        whereBuilder.append("          transaction ar ");
        whereBuilder.append("          join trading_partner tp");
        whereBuilder.append("            on (");
        whereBuilder.append("              ar.cust_no = tp.acct_no");
        if (CommonUtils.isEmpty(form.getSearchFilter()) && CommonUtils.isNotEmpty(form.getCustomerNumber())) {
            whereBuilder.append("              and (");
            whereBuilder.append("                tp.acct_no = '").append(form.getCustomerNumber()).append("'");
            if (CommonUtils.in("Show Subsidiairy", form.getShowFilters())) {
                whereBuilder.append("                or tp.masteracct_no = '").append(form.getCustomerNumber()).append("'");
            }
            whereBuilder.append("              )");
        }
        whereBuilder.append("            )");
        whereBuilder.append("        where");
        whereBuilder.append("          ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice/BL")) {
            whereBuilder.append("          and (");
            whereBuilder.append("            ar.invoice_number like '%").append(form.getSearchValue1()).append("%'");
            whereBuilder.append("            or ar.bill_ladding_no like '%").append(form.getSearchValue1()).append("%'");
            whereBuilder.append("          )");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Dock Receipt")) {
            if(form.getSearchValue2().trim().length() == 6 || form.getSearchValue2().trim().length() < 3){
             whereBuilder.append("        and ar.drcpt = '").append(form.getSearchValue2()).append("'");
            }else {
            whereBuilder.append("        and ar.drcpt like '").append(form.getSearchValue2()).append("%'");// blueScreen Search
            }
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Container Number")) {
            whereBuilder.append("        and ar.container_no like '%").append(form.getSearchValue3()).append("%'");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Voyage Number")) {
            String drcpts = getDocReceiptsByVoyage(form.getSearchValue4(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            if (CommonUtils.isNotEmpty(drcpts)) {
                whereBuilder.append("        and (");
                whereBuilder.append("          ar.voyage_no like '%").append(form.getSearchValue4()).append("%'");
                whereBuilder.append("          or ar.drcpt in ").append(drcpts);
                whereBuilder.append("        )");
            } else {
                whereBuilder.append("   and ar.voyage_no like '%").append(form.getSearchValue4()).append("%'");
            }
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Check Number")) {
            String ids = getPaidInvoiceIds(form);
            if (CommonUtils.isNotEmpty(ids)) {
                whereBuilder.append("        and ar.transaction_id in (").append(ids).append(")");
            } else {
                whereBuilder.append("        and ar.transaction_id = -1");
            }
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Check Amount")) {
            String ids = getPaidInvoiceIds(form);
            if (CommonUtils.isNotEmpty(ids)) {
                whereBuilder.append("        and ar.transaction_id in (").append(ids).append(")");
            } else {
                whereBuilder.append("        and ar.transaction_id = -1");
            }
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice Amount")) {
            whereBuilder.append("        and ar.transaction_amt between ").append(form.getFromAmount7()).append(" and ").append(form.getToAmount7());
        } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice Balance")) {
            whereBuilder.append("        and ar.balance between ").append(form.getFromAmount8()).append(" and ").append(form.getToAmount8());
        } else {
            if ((CommonUtils.isNotEmpty(form.getFromDate()) || CommonUtils.isNotEmpty(form.getToDate()))) {
                if (CommonUtils.isEqual(form.getSearchDate(), "Invoice Date")) {
                    if (CommonUtils.isNotEmpty(form.getFromDate())) {
                        String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                        whereBuilder.append("        and ar.transaction_date >= '").append(fromDate).append("'");
                    }
                    if (CommonUtils.isNotEmpty(form.getToDate())) {
                        String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                        whereBuilder.append("        and ar.transaction_date <= '").append(toDate).append("'");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getSearchDate(), "Payment/Adjustment Date")) {
                    String ids = getPaidInvoiceIds(form);
                    if (CommonUtils.isNotEmpty(ids)) {
                        whereBuilder.append("        and ar.transaction_id in (").append(ids).append(")");
                    } else {
                        whereBuilder.append("        and ar.transaction_id = -1");
                    }
                }
            }
            if (CommonUtils.in("Open Invoices", form.getShowFilters()) && CommonUtils.in("Paid Invoices", form.getShowFilters())) {
                //do nothing
            } else if (CommonUtils.in("Open Invoices", form.getShowFilters())) {
                whereBuilder.append("        and ar.balance <> 0.00");
            } else if (CommonUtils.in("Paid Invoices", form.getShowFilters())) {
                whereBuilder.append("        and ar.balance = 0.00");
            }
            if (CommonUtils.in("NS Invoices", form.getShowFilters())) {
                if (form.isNsInvoiceOnly()) {
                    whereBuilder.append("        and ar.invoice_number like '").append(SUBLEDGER_CODE_NETSETT).append("%'");
                } else {
                    // do nothing
                }
            } else {
                whereBuilder.append("        and (");
                whereBuilder.append("          ar.invoice_number not like '").append(SUBLEDGER_CODE_NETSETT).append("%'");
                whereBuilder.append("          or ar.invoice_number is null");
                whereBuilder.append("        )");
            }
            if (CommonUtils.in("Credit Hold - Yes", form.getShowFilters()) && CommonUtils.in("Credit Hold - No", form.getShowFilters())) {
                //do nothing
            } else if (CommonUtils.in("Credit Hold - Yes", form.getShowFilters())) {
                whereBuilder.append("        and ar.credit_hold = '").append(YES).append("'");
            } else if (CommonUtils.in("Credit Hold - No", form.getShowFilters())) {
                whereBuilder.append("        and (");
                whereBuilder.append("          ar.credit_hold <> '").append(YES).append("'");
                whereBuilder.append("          or ar.credit_hold is null");
                whereBuilder.append("        )");
            }
        }
        return whereBuilder.toString();
    }

    private String buildCcWhere(ArInquiryForm form) throws Exception {
        if (CommonUtils.in(form.getSearchFilter(), SEARCH_BY_CHECK_NUMBER, SEARCH_BY_CHECK_AMOUNT)) {
            StringBuilder whereBuilder = new StringBuilder();
            whereBuilder.append("        from");
            whereBuilder.append("          payments cc ");
            whereBuilder.append("          join trading_partner tp");
            whereBuilder.append("            on (cc.cust_no = tp.acct_no)");
            whereBuilder.append("          join ar_batch ab");
            whereBuilder.append("            on (");
            whereBuilder.append("              cc.batch_id = ab.batch_id");
            if (CommonUtils.isEqualIgnoreCase(form.getSearchDate(), "Payment/Adjustment Date")
                    && (CommonUtils.isNotEmpty(form.getFromDate()) || CommonUtils.isNotEmpty(form.getToDate()))) {
                if (CommonUtils.isNotEmpty(form.getFromDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                    whereBuilder.append("              and ab.deposit_date >= '").append(fromDate).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getToDate())) {
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                    whereBuilder.append("              and ab.deposit_date <= '").append(toDate).append("'");
                }
            }
            whereBuilder.append("            )");
            whereBuilder.append("          join payment_checks pc");
            whereBuilder.append("            on (");
            whereBuilder.append("              cc.payment_check_id = pc.id");
            if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), SEARCH_BY_CHECK_NUMBER)) {
                whereBuilder.append("              and pc.check_no = '").append(form.getSearchValue5().trim()).append("'");
            } else {
                whereBuilder.append("              and pc.received_amt between ").append(form.getFromAmount6()).append(" and ").append(form.getToAmount6());
            }
            whereBuilder.append("            )");
            whereBuilder.append("        where");
            whereBuilder.append("          cc.payment_type = 'Check'");
            whereBuilder.append("          and cc.invoice_no = 'CHARGE CODE'");
            return whereBuilder.toString();
        } else {
            return null;
        }
    }

    private String buildApWhere(ArInquiryForm form) throws Exception {
        if (CommonUtils.in("Payables", form.getShowFilters())) {
            StringBuilder whereBuilder = new StringBuilder();
            whereBuilder.append("        from");
            whereBuilder.append("          transaction ap ");
            whereBuilder.append("          join trading_partner tp");
            whereBuilder.append("            on (");
            whereBuilder.append("              ap.cust_no = tp.acct_no");
            if (CommonUtils.isEmpty(form.getSearchFilter()) && CommonUtils.isNotEmpty(form.getCustomerNumber())) {
                whereBuilder.append("              and (");
                whereBuilder.append("                tp.acct_no = '").append(form.getCustomerNumber()).append("'");
                if (CommonUtils.in("Show Subsidiairy", form.getShowFilters())) {
                    whereBuilder.append("                or tp.masteracct_no = '").append(form.getCustomerNumber()).append("'");
                }
                whereBuilder.append("              )");
            }
            whereBuilder.append("            )");
            whereBuilder.append("        where");
            whereBuilder.append("          ap.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
            if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice/BL")) {
                whereBuilder.append("          and ap.invoice_number like '%").append(form.getSearchValue1()).append("%'");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Dock Receipt")) {
                if (form.getSearchValue2().trim().length() == 6 || form.getSearchValue2().trim().length() < 3) {
                     whereBuilder.append("          and ap.drcpt = '").append(form.getSearchValue2()).append("'");
                } else {
                     whereBuilder.append("          and ap.drcpt like '").append(form.getSearchValue2()).append("%'");
                }
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Container Number")) {
                whereBuilder.append("        and ap.container_no like '%").append(form.getSearchValue3()).append("%'");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Voyage Number")) {
                whereBuilder.append("          and (");
                whereBuilder.append("            ap.voyage_no like '%").append(form.getSearchValue4()).append("%'");
                String drcpts = getDocReceiptsByVoyage(form.getSearchValue4(), TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                if (CommonUtils.isNotEmpty(drcpts)) {
                    whereBuilder.append("            or ap.drcpt in ").append(drcpts);
                }
                whereBuilder.append("          )");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice Amount")) {
                whereBuilder.append("          and ap.transaction_amt between ").append(form.getFromAmount7()).append(" and ").append(form.getToAmount7());
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice Balance")) {
                whereBuilder.append("          and ap.balance between ").append(form.getFromAmount8()).append(" and ").append(form.getToAmount8());
            } else if (CommonUtils.isNotEmpty(form.getFromDate()) || CommonUtils.isNotEmpty(form.getToDate())) {
                if (CommonUtils.isNotEmpty(form.getFromDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                    whereBuilder.append("          and ap.transaction_date >= '").append(fromDate).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getToDate())) {
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                    whereBuilder.append("          and ap.transaction_date <= '").append(toDate).append("'");
                }
            }
            whereBuilder.append("          and ap.status in ('").append(STATUS_OPEN).append("', '").append(STATUS_PENDING).append("')");
            whereBuilder.append("          and ap.balance <> 0.00");
            return whereBuilder.toString();
        } else {
            return null;
        }
    }

    private String buildAcWhere(ArInquiryForm form) throws Exception {
        if (CommonUtils.in("Accruals", form.getShowFilters())) {
            StringBuilder whereBuilder = new StringBuilder();
            whereBuilder.append("        from");
            whereBuilder.append("          transaction_ledger ac ");
            whereBuilder.append("          join trading_partner tp");
            whereBuilder.append("            on (");
            whereBuilder.append("              ac.cust_no = tp.acct_no");
            if (CommonUtils.isEmpty(form.getSearchFilter()) && CommonUtils.isNotEmpty(form.getCustomerNumber())) {
                whereBuilder.append("            and (");
                whereBuilder.append("              tp.acct_no = '").append(form.getCustomerNumber()).append("'");
                if (CommonUtils.in("Show Subsidiairy", form.getShowFilters())) {
                    whereBuilder.append("              or tp.masteracct_no = '").append(form.getCustomerNumber()).append("'");
                }
                whereBuilder.append("            )");
            }
            whereBuilder.append("            )");
            whereBuilder.append("        where");
            whereBuilder.append("          ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
            if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice/BL")) {
                whereBuilder.append("          and ac.invoice_number like '%").append(form.getSearchValue1()).append("%'");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Dock Receipt")) {
                if (form.getSearchValue2().trim().length() == 6 || form.getSearchValue2().trim().length() < 3) {
                    whereBuilder.append("          and ac.drcpt = '").append(form.getSearchValue2()).append("'");
                } else {
                    whereBuilder.append("          and ac.drcpt like '").append(form.getSearchValue2()).append("%'");
                }
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Container Number")) {
                whereBuilder.append("        and ac.container_no like '%").append(form.getSearchValue3()).append("%'");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Voyage Number")) {
                whereBuilder.append("          and (");
                whereBuilder.append("            ac.voyage_no like '%").append(form.getSearchValue4()).append("%'");
                String drcpts = getDocReceiptsByVoyage(form.getSearchValue4(), TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                if (CommonUtils.isNotEmpty(drcpts)) {
                    whereBuilder.append("            or ac.drcpt in ").append(drcpts);
                }
                whereBuilder.append("          )");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice Amount")) {
                whereBuilder.append("          and ac.transaction_amt between ").append(form.getFromAmount7()).append(" and ").append(form.getToAmount7());
            } else if (CommonUtils.isEqualIgnoreCase(form.getSearchFilter(), "Invoice Balance")) {
                whereBuilder.append("          and ac.balance between ").append(form.getFromAmount8()).append(" and ").append(form.getToAmount8());
            } else if (CommonUtils.isNotEmpty(form.getFromDate()) || CommonUtils.isNotEmpty(form.getToDate())) {
                if (CommonUtils.isNotEmpty(form.getFromDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                    whereBuilder.append("          and ac.transaction_date >= '").append(fromDate).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getToDate())) {
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                    whereBuilder.append("          and ac.transaction_date <= '").append(toDate).append("'");
                }
            }
            whereBuilder.append("          and ac.status <> '").append(STATUS_ASSIGN).append("'");
            whereBuilder.append("          and ac.status <> '").append(STATUS_EDI_ASSIGNED).append("'");
            whereBuilder.append("          and ac.status <> '").append(STATUS_INACTIVE).append("'");
            whereBuilder.append("          and ac.transaction_amt <> 0.00");
            return whereBuilder.toString();
        } else {
            return null;
        }
    }

    private Integer getArRows(String arQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  sum(ar.row_count) as rowCount ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    select");
        queryBuilder.append("      count(ar.transaction_id) as row_count");
        queryBuilder.append("      ").append(arQuery);
        queryBuilder.append("  ) as ar");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("rowCount", IntegerType.INSTANCE);
        Integer count = (Integer) query.uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getCcRows(String ccQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  sum(cc.row_count) as rowCount ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    select");
        queryBuilder.append("      count(cc.id) as row_count");
        queryBuilder.append("      ").append(ccQuery);
        queryBuilder.append("  ) as cc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("rowCount", IntegerType.INSTANCE);
        Integer count = (Integer) query.uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getApRows(String apQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  sum(ap.row_count) as rowCount ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    select");
        queryBuilder.append("      count(ap.transaction_id) as row_count");
        queryBuilder.append("      ").append(apQuery);
        queryBuilder.append("  ) as ap");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("rowCount", IntegerType.INSTANCE);
        Integer count = (Integer) query.uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private Integer getAcRows(String acQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  sum(ac.row_count) as rowCount ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    select");
        queryBuilder.append("      count(ac.transaction_id) as row_count");
        queryBuilder.append("      ").append(acQuery);
        queryBuilder.append("  ) as ac");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("rowCount", IntegerType.INSTANCE);
        Integer count = (Integer) query.uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private String buildArSelect(ArInquiryForm form) throws Exception {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("        select");
        selectBuilder.append("          ar.correction_notice as correction_notice,");
        selectBuilder.append("          tp.acct_no as customer_number,");
        selectBuilder.append("          tp.acct_name as customer_name,");
        selectBuilder.append("          ar.bill_to as bill_to,");
        if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "shipper")) {
            selectBuilder.append("          ar.shipper_no as tp_number,");
            selectBuilder.append("          ar.shipper_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "consignee")) {
            selectBuilder.append("          ar.cons_no as tp_number,");
            selectBuilder.append("          ar.cons_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "forwarder")) {
            selectBuilder.append("          ar.fwd_no as tp_number,");
            selectBuilder.append("          ar.fwd_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "agent")) {
            selectBuilder.append("          ar.agent_no as tp_number,");
            selectBuilder.append("          ar.agent_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "third party")) {
            selectBuilder.append("          ar.third_pty_no as tp_number,");
            selectBuilder.append("          ar.third_pty_name as tp_name,");
        }
        selectBuilder.append("          ar.transaction_type as type,");
        selectBuilder.append("          ar.status as status,");
        selectBuilder.append("          ar.transaction_date as invoice_date,");
        selectBuilder.append("          ar.drcpt as dock_receipt,");
        selectBuilder.append("          ar.bill_ladding_no as bl_number,");
        selectBuilder.append("          if(ar.invoice_number = 'pre payment',ar.bill_ladding_no,ar.invoice_number) as invoice_number,");
        selectBuilder.append("          ar.voyage_no as voyage,");
        selectBuilder.append("          ar.customer_reference_no as reference,");
        selectBuilder.append("          ar.transaction_amt as invoice_amount,");
        selectBuilder.append("          ar.credit_hold as credit_hold,");
        selectBuilder.append("          ar.balance as invoice_balance,");
        selectBuilder.append("          ar.balance_in_process as balance_in_process,");
        selectBuilder.append("          ar.transaction_id as id,");
        selectBuilder.append("          'false' as excluded,");
        selectBuilder.append("          'false' as emailed,");
        selectBuilder.append("          tp.acct_type as acct_type,");
        selectBuilder.append("          tp.sub_type as sub_type,");
        selectBuilder.append("          cast('AR_INVOICE' as char character set latin1) as note_module_id,");
        selectBuilder.append("          cast(concat(tp.acct_no, '-', if(ar.bill_ladding_no <> '', ar.bill_ladding_no, ar.invoice_number)) as char character set latin1) as note_ref_id,");
        selectBuilder.append("          cast(concat(tp.acct_no, '-', if(ar.bill_ladding_no <> '', ar.bill_ladding_no, ar.invoice_number)) as char character set latin1) as document_id ");
        return selectBuilder.toString();
    }

    private String buildCcSelect(ArInquiryForm form) throws Exception {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("        select");
        selectBuilder.append("          null as correction_notice,");
        selectBuilder.append("          tp.acct_no as customer_number,");
        selectBuilder.append("          tp.acct_name as customer_name,");
        selectBuilder.append("          null as bill_to,");
        if (CommonUtils.isNotEmpty(form.getAccountType())) {
            selectBuilder.append("          null as tp_number,");
            selectBuilder.append("          null as tp_name,");
        }
        selectBuilder.append("          'GL' as type,");
        selectBuilder.append("          '' as status,");
        selectBuilder.append("          ab.deposit_date as invoice_date,");
        selectBuilder.append("          null as dock_receipt,");
        selectBuilder.append("          null as bl_number,");
        selectBuilder.append("          cc.charge_code as invoice_number,");
        selectBuilder.append("          null as voyage,");
        selectBuilder.append("          cast(concat(ab.batch_id, '-', pc.check_no) as char character set latin1) as reference,");
        selectBuilder.append("          cc.payment_amt as invoice_amount,");
        selectBuilder.append("          null as credit_hold,");
        selectBuilder.append("          0.00 as invoice_balance,");
        selectBuilder.append("          0.00 as balance_in_process,");
        selectBuilder.append("          cc.id as id,");
        selectBuilder.append("          'false' as excluded,");
        selectBuilder.append("          'false' as emailed,");
        selectBuilder.append("          tp.acct_type as acct_type,");
        selectBuilder.append("          tp.sub_type as sub_type,");
        selectBuilder.append("          cast('CHARGE CODE' as char character set latin1) as note_module_id,");
        selectBuilder.append("          cast(cc.id as char character set latin1) as note_ref_id,");
        selectBuilder.append("          cast(cc.id as char character set latin1) as document_id ");
        return selectBuilder.toString();
    }

    private String buildApSelect(ArInquiryForm form) throws Exception {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("        select");
        selectBuilder.append("          ap.correction_notice as correction_notice,");
        selectBuilder.append("          tp.acct_no as customer_number,");
        selectBuilder.append("          tp.acct_name as customer_name,");
        selectBuilder.append("          null as bill_to,");
        if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "shipper")) {
            selectBuilder.append("          ap.shipper_no as tp_number,");
            selectBuilder.append("          ap.shipper_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "consignee")) {
            selectBuilder.append("          ap.cons_no as tp_number,");
            selectBuilder.append("          ap.cons_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "forwarder")) {
            selectBuilder.append("          ap.fwd_no as tp_number,");
            selectBuilder.append("          ap.fwd_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "agent")) {
            selectBuilder.append("          ap.agent_no as tp_number,");
            selectBuilder.append("          ap.agent_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "third party")) {
            selectBuilder.append("          ap.third_pty_no as tp_number,");
            selectBuilder.append("          ap.third_pty_name as tp_name,");
        }
        selectBuilder.append("          ap.transaction_type as type,");
        selectBuilder.append("          ap.status as status,");
        selectBuilder.append("          ap.transaction_date as invoice_date,");
        selectBuilder.append("          null as dock_receipt,");
        selectBuilder.append("          null as bl_number,");
        selectBuilder.append("          ap.invoice_number as invoice_number,");
        selectBuilder.append("          null as voyage,");
        selectBuilder.append("          ap.customer_reference_no as reference,");
        selectBuilder.append("          -ap.transaction_amt as invoice_amount,");
        selectBuilder.append("          null as credit_hold,");
        selectBuilder.append("          -ap.balance as invoice_balance,");
        selectBuilder.append("          -ap.balance_in_process as balance_in_process,");
        selectBuilder.append("          ap.transaction_id as id,");
        selectBuilder.append("          'false' as excluded,");
        selectBuilder.append("          'false' as emailed,");
        selectBuilder.append("          tp.acct_type as acct_type,");
        selectBuilder.append("          tp.sub_type as sub_type,");
        selectBuilder.append("          cast('AP_INVOICE' as char character set latin1) as note_module_id,");
        selectBuilder.append("          cast(concat(tp.acct_no, '-', ap.invoice_number) as char character set latin1) as note_ref_id,");
        selectBuilder.append("          cast(concat(tp.acct_no, '-', ap.invoice_number) as char character set latin1) as document_id ");
        return selectBuilder.toString();
    }

    private String buildAcSelect(ArInquiryForm form) throws Exception {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("          select");
        selectBuilder.append("          ac.correction_notice as correction_notice,");
        selectBuilder.append("          tp.acct_no as customer_number,");
        selectBuilder.append("          tp.acct_name as customer_name,");
        selectBuilder.append("          null as bill_to,");
        if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "shipper")) {
            selectBuilder.append("          ac.shipper_no as tp_number,");
            selectBuilder.append("          ac.shipper_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "consignee")) {
            selectBuilder.append("          ac.cons_no as tp_number,");
            selectBuilder.append("          ac.cons_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "forwarder")) {
            selectBuilder.append("          ac.fwd_no as tp_number,");
            selectBuilder.append("          ac.fwd_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "agent")) {
            selectBuilder.append("          ac.agent_no as tp_number,");
            selectBuilder.append("          ac.agent_name as tp_name,");
        } else if (CommonUtils.isEqualIgnoreCase(form.getAccountType(), "third party")) {
            selectBuilder.append("          ac.third_pty_no as tp_number,");
            selectBuilder.append("          ac.third_pty_name as tp_name,");
        }
        selectBuilder.append("          ac.transaction_type as type,");
        selectBuilder.append("          ac.status as status,");
        selectBuilder.append("          ac.transaction_date as invoice_date,");
        selectBuilder.append("          null as dock_receipt,");
        selectBuilder.append("          if(ac.bill_ladding_no <> '', ac.bill_ladding_no, ac.drcpt) as bl_number,");
        selectBuilder.append("          ac.invoice_number as invoice_number,");
        selectBuilder.append("          ac.voyage_no as voyage,");
        selectBuilder.append("          ac.customer_reference_no as reference,");
        selectBuilder.append("          -ac.transaction_amt as invoice_amount,");
        selectBuilder.append("          null as credit_hold,");
        selectBuilder.append("          -ac.balance as invoice_balance,");
        selectBuilder.append("          -ac.balance_in_process as balance_in_process,");
        selectBuilder.append("          ac.transaction_id as id,");
        selectBuilder.append("          'false' as excluded,");
        selectBuilder.append("          'false' as emailed,");
        selectBuilder.append("          tp.acct_type as acct_type,");
        selectBuilder.append("          tp.sub_type as sub_type,");
        selectBuilder.append("          cast('ACCRUALS' as char character set latin1) as note_module_id,");
        selectBuilder.append("          cast(ac.transaction_id as char character set latin1) as note_ref_id,");
        selectBuilder.append("          cast(if(ac.invoice_number <> '', concat(tp.acct_no, '-', ac.invoice_number), null) as char character set latin1) as document_id ");
        return selectBuilder.toString();
    }

    private List<ResultModel> getResults(ArInquiryForm form, Map<String, String> where, String sortBy, String orderBy, int start, int limit) throws Exception {
        String arWhere = where.get("arWhere");
        String ccWhere = where.get("ccWhere");
        String apWhere = where.get("apWhere");
        String acWhere = where.get("acWhere");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(t.correction_notice <> '0-CNA', t.correction_notice, '') as correctionNotice,");
        queryBuilder.append("  upper(t.customer_number) as customerNumber,");
        queryBuilder.append("  upper(t.customer_name) as customerName,");
        queryBuilder.append("  upper(t.bill_to) as billTo,");
        if (CommonUtils.isNotEmpty(form.getAccountType())) {
            queryBuilder.append("  upper(t.tp_number) as tpNumber,");
            queryBuilder.append("  upper(t.tp_name) as tpName,");
        }
        queryBuilder.append("  upper(t.type) as type,");
        queryBuilder.append("  upper(t.status) as status,");
        queryBuilder.append("  date_format(t.invoice_date, '%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("  if(fcl.bolid <> '', if(red.invoice_number <> '', 'FCL, INV', 'FCL'), if(lcl.file_number <> '', if(red.invoice_number <> '', 'LCL, INV', 'LCL'), if(red.invoice_number <> '', 'INV', ''))) as source,");
        queryBuilder.append("  if(fcl.bol <> 0, fcl.bol, if(lcl.id <> 0, lcl.id, if(red.id <> 0, red.id, ''))) as sourceId,");
        queryBuilder.append("  if(fcl.bolid <> '', if(fcl.importflag = 'I', 'I', 'E'), IF(lcl.id <> 0,lclb.booking_type,'')) as sourceType,");
        queryBuilder.append("  if(red.id <> 0, red.id, '') as arInvoiceId,");
        queryBuilder.append("  upper(t.dock_receipt) as dockReceipt,");
        queryBuilder.append("  upper(if(fcl.bolid <> '' and red.invoice_number <> '',CONCAT(SUBSTRING_INDEX(fcl.bolid,'-04-',1),'-04-',red.file_no), t.bl_number)) as blNumber,");
        queryBuilder.append("  upper(t.invoice_number) as invoiceNumber,");
        queryBuilder.append("  upper(t.voyage) as voyage,");
        queryBuilder.append("  upper(t.reference) as reference,");
        queryBuilder.append("  format(t.invoice_amount, 2) as invoiceAmount,");
        queryBuilder.append("  datediff(now(), t.invoice_date) as age,");
        queryBuilder.append("  if(t.credit_hold = 'Y', 'Y', 'N') as creditHold,");
        queryBuilder.append("  format(t.invoice_balance, 2) as invoiceBalance,");
        queryBuilder.append("  if(t.invoice_balance <> t.balance_in_process, 'true', 'false') as includedInBatch,");
        queryBuilder.append("  t.id as id,");
        queryBuilder.append("  if(count(n.id) > 0, 'true', 'false') as manualNotes,");
        queryBuilder.append("  t.note_module_id as noteModuleId,");
        queryBuilder.append("  t.note_ref_id as noteRefId,");
        queryBuilder.append("  if(t.acct_type like '%V%' and t.sub_type = 'Overhead', 'true', 'false') as overhead,");
        queryBuilder.append("  if(count(d.document_id) > 0,'true', 'false') as uploaded,");
        queryBuilder.append("  t.document_id as documentId,");
        queryBuilder.append("  if(lclb.`booking_type` <> 'E' and red.`id` is null, if(`CustomerGetIsNoCredit`(t.`customer_number`), `LclImpIsDrArBalanceEqual`(lcl.`id`, t.`customer_number`, t.`invoice_balance`), true), null) as balanceMatches");
        queryBuilder.append(" from");
        queryBuilder.append("  (");
        queryBuilder.append("    select");
        queryBuilder.append("      t.correction_notice as correction_notice,");
        queryBuilder.append("      t.customer_number as customer_number,");
        queryBuilder.append("      t.customer_name as customer_name,");
        queryBuilder.append("      t.bill_to as bill_to,");
        if (CommonUtils.isNotEmpty(form.getAccountType())) {
            queryBuilder.append("      t.tp_number as tp_number,");
            queryBuilder.append("      t.tp_name as tp_name,");
        }
        queryBuilder.append("      t.type as type,");
        queryBuilder.append("      t.status as status,");
        queryBuilder.append("      t.invoice_date as invoice_date,");
        queryBuilder.append("      t.dock_receipt as dock_receipt,");
        queryBuilder.append("      t.dock_receipt as file_no,");
        queryBuilder.append("      t.bl_number as bl_number,");
        queryBuilder.append("      t.invoice_number as invoice_number,");
        queryBuilder.append("      t.voyage as voyage,");
        queryBuilder.append("      t.reference as reference,");
        queryBuilder.append("      t.invoice_amount as invoice_amount,");
        queryBuilder.append("      t.credit_hold as credit_hold,");
        queryBuilder.append("      t.invoice_balance as invoice_balance,");
        queryBuilder.append("      t.balance_in_process as balance_in_process,");
        queryBuilder.append("      t.id as id,");
        queryBuilder.append("      t.note_module_id as note_module_id,");
        queryBuilder.append("      t.note_ref_id as note_ref_id,");
        queryBuilder.append("      t.acct_type as acct_type,");
        queryBuilder.append("      t.sub_type as sub_type,");
        queryBuilder.append("      t.document_id as document_id ");
        queryBuilder.append("    from");
        queryBuilder.append("      (");
        if (CommonUtils.isNotEmpty(arWhere)) {
            String arSelect = buildArSelect(form);
            queryBuilder.append("        (");
            queryBuilder.append(arSelect);
            queryBuilder.append(arWhere);
            queryBuilder.append("        )");
        }
        if (CommonUtils.isNotEmpty(ccWhere)) {
            String ccSelect = buildCcSelect(form);
            if (CommonUtils.isNotEmpty(arWhere)) {
                queryBuilder.append("        union");
            }
            queryBuilder.append("        (");
            queryBuilder.append(ccSelect);
            queryBuilder.append(ccWhere);
            queryBuilder.append("        )");
        }
        if (CommonUtils.isNotEmpty(apWhere)) {
            String apSelect = buildApSelect(form);
            if (CommonUtils.isNotEmpty(arWhere)) {
                queryBuilder.append("        union");
            }
            queryBuilder.append("        (");
            queryBuilder.append(apSelect);
            queryBuilder.append(apWhere);
            queryBuilder.append("        )");
        }
        if (CommonUtils.isNotEmpty(acWhere)) {
            String acSelect = buildAcSelect(form);
            if (CommonUtils.isAtLeastOneNotEmpty(arWhere, apWhere)) {
                queryBuilder.append("        union");
            }
            queryBuilder.append("        (");
            queryBuilder.append(acSelect);
            queryBuilder.append(acWhere);
            queryBuilder.append("        )");
        }
        queryBuilder.append("      ) as t ");
        queryBuilder.append("group by t.type, t.id ");
        queryBuilder.append("order by t.").append(sortBy).append(" ").append(orderBy).append(" ");
        if (CommonUtils.isEqualIgnoreCase(sortBy, "invoice_date")) {
            queryBuilder.append("  , t.id asc, t.bl_number asc ");
        } else if (CommonUtils.isEqualIgnoreCase(sortBy, "invoice_number")) {
            queryBuilder.append("  , t.bl_number ").append(orderBy).append(" ");
        }
        queryBuilder.append("limit ").append(start).append(",").append(limit);
        queryBuilder.append("  ) as t");
        queryBuilder.append("  left join fcl_bl fcl");
        queryBuilder.append("    on (");
        queryBuilder.append("      t.type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and t.file_no = fcl.file_no");
        queryBuilder.append("    )");
        queryBuilder.append("  left join lcl_file_number lcl");
        queryBuilder.append("    on (");
        queryBuilder.append("      t.type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and t.dock_receipt = lcl.file_number");
        queryBuilder.append("    )");
        queryBuilder.append("  left join lcl_booking lclb");
        queryBuilder.append("    on (");
        queryBuilder.append("      t.type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and lclb.file_number_id = lcl.id");
        queryBuilder.append("    )");
        queryBuilder.append("  left join ar_red_invoice red");
        queryBuilder.append("    on (");
        queryBuilder.append("      t.type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and t.invoice_number = red.invoice_number");
        queryBuilder.append("      and t.customer_number = red.customer_number");
        queryBuilder.append("    )");
        queryBuilder.append("  left join notes n");
        queryBuilder.append("    on (");
        queryBuilder.append("      t.note_module_id = n.module_id");
        queryBuilder.append("      and t.note_ref_id = n.module_ref_id");
        queryBuilder.append("      and n.note_type = 'Manual'");
        queryBuilder.append("    )");
        queryBuilder.append("  left join document_store_log d");
        queryBuilder.append("    on (");
        queryBuilder.append("      t.document_id = d.document_id");
        queryBuilder.append("      and d.screen_name = 'INVOICE'");
        queryBuilder.append("      and d.document_name = 'INVOICE'");
        queryBuilder.append("    ) ");
        queryBuilder.append("group by t.type, t.id ");
        queryBuilder.append("order by t.").append(sortBy).append(" ").append(orderBy);
        if (CommonUtils.isEqualIgnoreCase(sortBy, "invoice_date")) {
            queryBuilder.append("  , t.id asc, t.bl_number asc");
        } else if (CommonUtils.isEqualIgnoreCase(sortBy, "invoice_number")) {
            queryBuilder.append("  , t.bl_number ").append(orderBy);
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("correctionNotice", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("billTo", StringType.INSTANCE);
        if (CommonUtils.isNotEmpty(form.getAccountType())) {
            query.addScalar("tpName", StringType.INSTANCE);
            query.addScalar("tpNumber", StringType.INSTANCE);
        }
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("invoiceDate", StringType.INSTANCE);
        query.addScalar("source", StringType.INSTANCE);
        query.addScalar("sourceId", StringType.INSTANCE);
        query.addScalar("sourceType", StringType.INSTANCE);
        query.addScalar("arInvoiceId", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("voyage", StringType.INSTANCE);
        query.addScalar("reference", StringType.INSTANCE);
        query.addScalar("invoiceAmount", StringType.INSTANCE);
        query.addScalar("age", IntegerType.INSTANCE);
        query.addScalar("creditHold", StringType.INSTANCE);
        query.addScalar("invoiceBalance", StringType.INSTANCE);
        query.addScalar("includedInBatch", StringType.INSTANCE);
        query.addScalar("id", StringType.INSTANCE);
        query.addScalar("manualNotes", StringType.INSTANCE);
        query.addScalar("noteModuleId", StringType.INSTANCE);
        query.addScalar("noteRefId", StringType.INSTANCE);
        query.addScalar("overhead", StringType.INSTANCE);
        query.addScalar("uploaded", StringType.INSTANCE);
        query.addScalar("documentId", StringType.INSTANCE);
        query.addScalar("balanceMatches", BooleanType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public CustomerModel getCustomer(String customerNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("   c.`customer_number` as customerNumber,");
        queryBuilder.append("   c.`customer_name` as customerName,");
        queryBuilder.append("   replace(c.`address`, '\\n', '<br/>') as address,");
        queryBuilder.append("   c.`contact` as contact,");
        queryBuilder.append("   c.`phone` as phone,");
        queryBuilder.append("   c.`fax` as fax,");
        queryBuilder.append("   c.`email` as email,");
        queryBuilder.append("   c.`type` as type,");
        queryBuilder.append("   c.`ecu_designation` as ecuDesignation,");
        queryBuilder.append("   c.`shipper` as shipper,");
        queryBuilder.append("   c.`vendor` as vendor,");
        queryBuilder.append("   c.`consignee` as consignee,");
        queryBuilder.append("   c.`master` as master,");
        queryBuilder.append("   c.`credit_limit` as creditLimit,");
        queryBuilder.append("   c.`credit_status` as creditStatus,");
        queryBuilder.append("   c.`credit_term` as creditTerm,");
        queryBuilder.append("   c.`collector` as collector,");
        queryBuilder.append("   c.`importCredit` as importCredit,");
        queryBuilder.append("   c.`exemptCreditProcess` as exemptCreditProcess,");
        queryBuilder.append("   c.`hhgPeAutosCredit` as hhgPeAutosCredit,");
        queryBuilder.append("   c.`salesperson` as salesperson,");
        queryBuilder.append("   ucase((select concat(m.`type`, ' - ', if(m.`status` = 'Completed', 'Success', if(m.`status` = 'Failed', 'Fail', m.`status`))) from `mail_transactions` m where m.`module_id` = c.`customer_number` and m.`module_name` = 'AR Statement' order by id desc limit 1)) as lastStatement ");
        queryBuilder.append("from");
        queryBuilder.append("  `customer_details_view` c ");
        queryBuilder.append("where");
        queryBuilder.append("  c.`customer_number` = '").append(customerNumber).append("' ");
        queryBuilder.append("limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("contact", StringType.INSTANCE);
        query.addScalar("phone", StringType.INSTANCE);
        query.addScalar("fax", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("ecuDesignation", StringType.INSTANCE);
        query.addScalar("shipper", StringType.INSTANCE);
        query.addScalar("vendor", StringType.INSTANCE);
        query.addScalar("consignee", StringType.INSTANCE);
        query.addScalar("master", StringType.INSTANCE);
        query.addScalar("creditLimit", StringType.INSTANCE);
        query.addScalar("creditStatus", StringType.INSTANCE);
        query.addScalar("creditTerm", StringType.INSTANCE);
        query.addScalar("collector", StringType.INSTANCE);
        query.addScalar("importCredit", StringType.INSTANCE);
        query.addScalar("exemptCreditProcess", StringType.INSTANCE);
        query.addScalar("hhgPeAutosCredit", StringType.INSTANCE);
        query.addScalar("salesPerson", StringType.INSTANCE);
        query.addScalar("lastStatement", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CustomerModel.class));
        CustomerModel customer = (CustomerModel) query.uniqueResult();

        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("select");
        queryBuilder.append("  date_format(ab.deposit_date, '%m/%d/%Y') as paymentDate,");
        queryBuilder.append("  format(pc.received_amt, 2) as paymentAmount,");
        queryBuilder.append("  pc.check_no as paymentReference ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ar");
        queryBuilder.append("  join payments py");
        queryBuilder.append("    on (");
        queryBuilder.append("      ar.transaction_id = py.transaction_id");
        queryBuilder.append("      and py.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("    )");
        queryBuilder.append("  join ar_batch ab");
        queryBuilder.append("    on (py.batch_id = ab.batch_id)");
        queryBuilder.append("  join payment_checks pc");
        queryBuilder.append("    on (py.payment_check_id = pc.id) ");
        queryBuilder.append("where");
        queryBuilder.append("  ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("  and ar.cust_no = '").append(customerNumber).append("' ");
        queryBuilder.append("order by py.id desc limit 1");
        query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("paymentDate", StringType.INSTANCE);
        query.addScalar("paymentAmount", StringType.INSTANCE);
        query.addScalar("paymentReference", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CustomerModel.class));
        CustomerModel payment = (CustomerModel) query.uniqueResult();
        if (null != payment) {
            customer.setPaymentDate(payment.getPaymentDate());
            customer.setPaymentAmount(payment.getPaymentAmount());
            customer.setPaymentReference(payment.getPaymentReference());
        }
        return customer;
    }

    public CustomerModel getArAgingBuckets(String arWhere) {
        CustomerModel aging = null;
        if (CommonUtils.isNotEmpty(arWhere)) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select");
            queryBuilder.append("  format(sum(if(datediff(now(), ar.transaction_date) between 0 and 30, ar.balance, 0.00)), 2) as age30Amount,");
            queryBuilder.append("  format(sum(if(datediff(now(), ar.transaction_date) between 31 and 60, ar.balance, 0.00)), 2) as age60Amount,");
            queryBuilder.append("  format(sum(if(datediff(now(), ar.transaction_date) between 61 and 90, ar.balance, 0.00)), 2) as age90Amount,");
            queryBuilder.append("  format(sum(if(datediff(now(), ar.transaction_date) >= 91, ar.balance, 0.00)), 2) as age91Amount,");
            queryBuilder.append("  format(sum(ar.balance), 2) as total ");
            queryBuilder.append(arWhere);
            SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            query.addScalar("age30Amount", StringType.INSTANCE);
            query.addScalar("age60Amount", StringType.INSTANCE);
            query.addScalar("age90Amount", StringType.INSTANCE);
            query.addScalar("age91Amount", StringType.INSTANCE);
            query.addScalar("total", StringType.INSTANCE);
            query.setResultTransformer(Transformers.aliasToBean(CustomerModel.class));
            aging = (CustomerModel) query.uniqueResult();
        }
        if (null == aging) {
            aging = new CustomerModel();
            aging.setAge30Amount("0.00");
            aging.setAge60Amount("0.00");
            aging.setAge90Amount("0.00");
            aging.setAge91Amount("0.00");
            aging.setTotal("0.00");
        }
        return aging;
    }

    public VendorModel getApAgingBuckets(String apWhere, String acWhere) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  format(sum(t.age_30_amount), 2) as age30Amount,");
        queryBuilder.append("  format(sum(t.age_60_amount), 2) as age60Amount,");
        queryBuilder.append("  format(sum(t.age_90_amount), 2) as age90Amount,");
        queryBuilder.append("  format(sum(t.age_91_amount), 2) as age91Amount,");
        queryBuilder.append("  format(sum(t.total), 2) as total,");
        queryBuilder.append("  format(sum(t.ac_amount), 2) as acAmount ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        if (CommonUtils.isNotEmpty(apWhere)) {
            queryBuilder.append("    (");
            queryBuilder.append("      select");
            queryBuilder.append("        -sum(if(datediff(now(), ap.transaction_date) between 0 and 30, ap.balance, 0.00)) as age_30_amount,");
            queryBuilder.append("        -sum(if(datediff(now(), ap.transaction_date) between 31 and 60, ap.balance, 0.00)) as age_60_amount,");
            queryBuilder.append("        -sum(if(datediff(now(), ap.transaction_date) between 61 and 90, ap.balance, 0.00)) as age_90_amount,");
            queryBuilder.append("        -sum(if(datediff(now(), ap.transaction_date) >= 91, ap.balance, 0.00)) as age_91_amount,");
            queryBuilder.append("        -sum(ap.balance) as total,");
            queryBuilder.append("        0.00 as ac_amount ");
            queryBuilder.append(apWhere);
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(acWhere)) {
            if (CommonUtils.isNotEmpty(apWhere)) {
                queryBuilder.append("    union");
            }
            queryBuilder.append("    (");
            queryBuilder.append("      select");
            queryBuilder.append("        0.00 as age_30_amount,");
            queryBuilder.append("        0.00 as age_60_amount,");
            queryBuilder.append("        0.00 as age_90_amount,");
            queryBuilder.append("        0.00 as age_91_amount,");
            queryBuilder.append("        0.00 as total,");
            queryBuilder.append("        -sum(ac.balance) as ac_amount ");
            queryBuilder.append(acWhere);
            queryBuilder.append("    )");
        }
        queryBuilder.append("  ) as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("age30Amount", StringType.INSTANCE);
        query.addScalar("age60Amount", StringType.INSTANCE);
        query.addScalar("age90Amount", StringType.INSTANCE);
        query.addScalar("age91Amount", StringType.INSTANCE);
        query.addScalar("total", StringType.INSTANCE);
        query.addScalar("acAmount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(VendorModel.class));
        return (VendorModel) query.uniqueResult();
    }

    public VendorModel getAcAgingBuckets(String acWhere) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  format(sum(t.age_30_amount), 2) as age30Amount,");
        queryBuilder.append("  format(sum(t.age_60_amount), 2) as age60Amount,");
        queryBuilder.append("  format(sum(t.age_90_amount), 2) as age90Amount,");
        queryBuilder.append("  format(sum(t.age_91_amount), 2) as age91Amount,");
        queryBuilder.append("  format(sum(t.total), 2) as total,");
        queryBuilder.append("  format(sum(t.ac_amount), 2) as acAmount ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        if (CommonUtils.isNotEmpty(acWhere)) {
            queryBuilder.append("    (");
            queryBuilder.append("      select");
            queryBuilder.append("        -sum(if(datediff(now(), ac.transaction_date) between 0 and 30, ac.balance, 0.00)) as age_30_amount,");
            queryBuilder.append("        -sum(if(datediff(now(), ac.transaction_date) between 31 and 60, ac.balance, 0.00)) as age_60_amount,");
            queryBuilder.append("        -sum(if(datediff(now(), ac.transaction_date) between 61 and 90, ac.balance, 0.00)) as age_90_amount,");
            queryBuilder.append("        -sum(if(datediff(now(), ac.transaction_date) >= 91, ac.balance, 0.00)) as age_91_amount,");
            queryBuilder.append("        -sum(ac.balance) as total,");
            queryBuilder.append("        0.00 as ac_amount ");
            queryBuilder.append(acWhere);
            queryBuilder.append("    )");
        }
        queryBuilder.append("  ) as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("age30Amount", StringType.INSTANCE);
        query.addScalar("age60Amount", StringType.INSTANCE);
        query.addScalar("age90Amount", StringType.INSTANCE);
        query.addScalar("age91Amount", StringType.INSTANCE);
        query.addScalar("total", StringType.INSTANCE);
        query.addScalar("acAmount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(VendorModel.class));
        return (VendorModel) query.uniqueResult();
    }

    public void search(ArInquiryForm form) throws Exception {
        Map<String, String> where = new HashMap<String, String>();
        String arWhere = buildArWhere(form);
        String ccWhere = buildCcWhere(form);
        String apWhere = buildApWhere(form);
        String acWhere = buildAcWhere(form);
        if (CommonUtils.isAtLeastOneNotEmpty(arWhere, ccWhere, apWhere, acWhere)) {
            int arRows = CommonUtils.isNotEmpty(arWhere) ? getArRows(arWhere) : 0;
            int ccRows = CommonUtils.isNotEmpty(ccWhere) ? getCcRows(ccWhere) : 0;
            int apRows = CommonUtils.isNotEmpty(apWhere) ? getApRows(apWhere) : 0;
            int acRows = CommonUtils.isNotEmpty(acWhere) ? getAcRows(acWhere) : 0;
            where.put("arWhere", arRows > 0 ? arWhere : null);
            where.put("ccWhere", ccRows > 0 ? ccWhere : null);
            where.put("apWhere", apRows > 0 ? apWhere : null);
            where.put("acWhere", acRows > 0 ? acWhere : null);
            int totalRows = apRows + ccRows + arRows + acRows;
            if (totalRows > 0) {
                int limit = form.getLimit();
                int start = limit * (form.getSelectedPage() - 1);
                int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
                List<ResultModel> results = getResults(form, where, form.getSortBy(), form.getOrderBy(), start, limit);
                form.setTotalPages(totalPages);
                form.setTotalRows(totalRows);
                form.setSelectedRows(results.size());
                form.setResults(results);
            }
            CustomerModel customer = null;
            if (CommonUtils.isAllNotEmpty(form.getCustomerNumber())) {
                customer = getCustomer(form.getCustomerNumber());
            }
            CustomerModel arAging = getArAgingBuckets(arRows > 0 ? arWhere : null);
            if (null != customer) {
                customer.setAge30Amount(arAging.getAge30Amount());
                customer.setAge60Amount(arAging.getAge60Amount());
                customer.setAge90Amount(arAging.getAge90Amount());
                customer.setAge91Amount(arAging.getAge91Amount());
                customer.setTotal(arAging.getTotal());
            } else {
                customer = arAging;
            }
            form.setArSummary(customer);
            if (apRows > 0) {
                VendorModel vendor = getApAgingBuckets(apRows > 0 ? apWhere : null, acRows > 0 ? acWhere : null);
                if (null != vendor) {
                    double netAmount1 = NumberUtils.parseNumber(customer.getTotal()) + NumberUtils.parseNumber(vendor.getTotal());
                    vendor.setNetAmount1(NumberUtils.formatNumber(netAmount1));
                    double netAmount2 = netAmount1 + NumberUtils.parseNumber(vendor.getAcAmount());
                    vendor.setNetAmount2(NumberUtils.formatNumber(netAmount2));
                    form.setApSummary(vendor);
                }
            }
            if (acRows > 0) {
                VendorModel vendor = getAcAgingBuckets(acRows > 0 ? acWhere : null);
                if (null != vendor) {
                    double netAmount1 = NumberUtils.parseNumber(customer.getTotal()) + NumberUtils.parseNumber(vendor.getTotal());
                    vendor.setNetAmount1(NumberUtils.formatNumber(netAmount1));
                    double netAmount2 = netAmount1 + NumberUtils.parseNumber(vendor.getAcAmount());
                    vendor.setNetAmount2(NumberUtils.formatNumber(netAmount2));
                    form.setAcSummary(vendor);
                }
            }
        }
    }

    public List<ResultModel> getCharges(String source, String customerNumber, String blNumber, String invoiceNumber, String correctionNotice) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  *");
        queryBuilder.append("from");
        queryBuilder.append("(");
        queryBuilder.append("  (select");
        queryBuilder.append("    tp.acct_no as customerNumber,");
        queryBuilder.append("    tp.acct_name as customerName,");
        queryBuilder.append("    chg.bill_ladding_no AS blNumber,");
        queryBuilder.append("    chg.invoice_number as invoiceNumber,");
        queryBuilder.append("    date_format(chg.transaction_date, '%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("    date_format(chg.posted_date, '%m/%d/%Y') as postedDate,");
        queryBuilder.append("    chg.charge_code as chargeCode,");
        queryBuilder.append("    chg.gl_account_number as glAccount,");
        queryBuilder.append("    chg.subledger_source_code as subledger,");
        queryBuilder.append("    format(chg.transaction_amt, 2) as amount ");
        queryBuilder.append("  from");
        queryBuilder.append("    transaction_ledger chg ");
        queryBuilder.append("    join trading_partner tp");
        queryBuilder.append("      on (");
        queryBuilder.append("        chg.cust_no = tp.acct_no");
        queryBuilder.append("        and tp.acct_no = '").append(customerNumber).append("'");
        queryBuilder.append("      ) ");
        queryBuilder.append("  where");
        queryBuilder.append("    (");
        if (CommonUtils.isEqualIgnoreCase(source, "FCL")) {
            queryBuilder.append("      chg.bill_ladding_no = '").append(blNumber).append("'");
            queryBuilder.append("      or chg.invoice_number = '").append(blNumber).append("'");
        } else if (CommonUtils.isEqualIgnoreCase(source, "LCL")) {
            queryBuilder.append("      chg.bill_ladding_no = '").append(blNumber).append("'");
            queryBuilder.append("      and chg.invoice_number = '").append(invoiceNumber).append("'");
        } else if (CommonUtils.in(source, "INV", "FCL, INV", "LCL, INV")) {
            queryBuilder.append("      chg.bill_ladding_no = '").append(invoiceNumber).append("'");
            queryBuilder.append("      or chg.invoice_number = '").append(invoiceNumber).append("'");
        } else if (CommonUtils.isNotEmpty(blNumber)) {
            queryBuilder.append("      chg.bill_ladding_no = '").append(blNumber).append("'");
            queryBuilder.append("      or chg.invoice_number = '").append(blNumber).append("'");
        } else {
            queryBuilder.append("      chg.bill_ladding_no = '").append(invoiceNumber).append("'");
            queryBuilder.append("      or chg.invoice_number = '").append(invoiceNumber).append("'");
        }
        queryBuilder.append("    )");
        if (CommonUtils.isNotEmpty(correctionNotice)) {
            queryBuilder.append("    and chg.correction_notice = '").append(correctionNotice).append("'");
        }
        queryBuilder.append("    and chg.subledger_source_code like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%' ");
        queryBuilder.append("  order by chg.transaction_date, chg.transaction_id");
        queryBuilder.append("  )");
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            queryBuilder.append("  union all");
            queryBuilder.append("  (select");
            queryBuilder.append("    tp.acct_no as customerNumber,");
            queryBuilder.append("    tp.acct_name as customerName,");
            queryBuilder.append("    cost.bill_ladding_no as blNumber,");
            queryBuilder.append("    cost.invoice_number as invoiceNumber,");
            queryBuilder.append("    date_format(cost.transaction_date, '%m/%d/%Y') as invoiceDate,");
            queryBuilder.append("    date_format(cost.posted_date, '%m/%d/%Y') as postedDate,");
            queryBuilder.append("    cost.charge_code as chargeCode,");
            queryBuilder.append("    cost.gl_account_number as glAccount,");
            queryBuilder.append("    cost.transaction_type as subledger,");
            queryBuilder.append("    format(cost.transaction_amt, 2) as amount ");
            queryBuilder.append("  from");
            queryBuilder.append("    transaction_ledger cost ");
            queryBuilder.append("    join trading_partner tp ");
            queryBuilder.append("      on (");
            queryBuilder.append("        cost.cust_no = tp.acct_no ");
            queryBuilder.append("        and tp.acct_no = '").append(customerNumber).append("'");
            queryBuilder.append("      ) ");
            queryBuilder.append("  where cost.invoice_number = '").append(invoiceNumber).append("'");
            queryBuilder.append("    and cost.transaction_type = 'AC'");
            queryBuilder.append("  order by cost.transaction_date, cost.transaction_id");
            queryBuilder.append("  )");
            queryBuilder.append("  union all");
            queryBuilder.append("  (select ");
            queryBuilder.append("    tp.acct_no as customerNumber,");
            queryBuilder.append("    tp.acct_name as customerName,");
            queryBuilder.append("    ar.bl_number as blNumber,");
            queryBuilder.append("    ar.invoice_number as invoiceNumber,");
            queryBuilder.append("    date_format(ar.invoice_date, '%m/%d/%Y') as invoiceDate,");
            queryBuilder.append("    date_format(ar.posted_date, '%m/%d/%Y') as postedDate,");
            queryBuilder.append("    null as chargeCode,");
            queryBuilder.append("    null as glAccount,");
            queryBuilder.append("    'AR' as subledger,");
            queryBuilder.append("    format(ar.transaction_amount, 2) as amount ");
            queryBuilder.append("  from");
            queryBuilder.append("    ar_transaction_history ar ");
            queryBuilder.append("    join trading_partner tp ");
            queryBuilder.append("      on (");
            queryBuilder.append("        ar.customer_number = tp.acct_no ");
            queryBuilder.append("      ) ");
            queryBuilder.append("  where ar.check_number = '").append(customerNumber).append("-").append(invoiceNumber).append("'");
            queryBuilder.append("    and ar.transaction_type = 'AP INV'");
            queryBuilder.append("  order by ar.transaction_date, ar.id");
            queryBuilder.append("  )");
        }
        queryBuilder.append(") as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("invoiceDate", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("subledger", StringType.INSTANCE);
        query.addScalar("amount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public List<ResultModel> getPostedTransactions(String source, String customerNumber, String blNumber, String invoiceNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        String paymentTypes = "'AP PY','AR PY','PP INV','AP INV','ADJ'";
        queryBuilder.append("  date_format(if(th.transaction_type in (").append(paymentTypes).append("), th.posted_date, th.transaction_date), '%m/%d/%Y') as transactionDate,");
        queryBuilder.append("  date_format(th.posted_date, '%m/%d/%Y') as postedDate,");
        queryBuilder.append("  th.transaction_type as type,");
        queryBuilder.append("  format(th.transaction_amount, 2) as transactionAmount,");
        queryBuilder.append("  if(");
        queryBuilder.append("    th.ar_batch_id <> 0,");
        queryBuilder.append("    th.ar_batch_id,");
        queryBuilder.append("    if(");
        queryBuilder.append("      th.ap_batch_id <> 0,");
        queryBuilder.append("      th.ap_batch_id,");
        queryBuilder.append("      null");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as batchNumber,");
        queryBuilder.append("  upper(th.check_number) as checkNumber,");
        queryBuilder.append("  th.gl_account_number as glAccount,");
        queryBuilder.append("  if(");
        queryBuilder.append("    th.adjustment_amount <> 0,");
        queryBuilder.append("    date_format(th.transaction_date, '%m/%d/%Y'),");
        queryBuilder.append("    null");
        queryBuilder.append("  ) as adjustmentDate,");
        queryBuilder.append("  format(th.adjustment_amount, 2) as adjustmentAmount,");
        queryBuilder.append("  upper(");
        queryBuilder.append("    if(");
        queryBuilder.append("      th.created_by <> '',");
        queryBuilder.append("      th.created_by,");
        queryBuilder.append("      'System'");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as user ");
        queryBuilder.append("from");
        queryBuilder.append("  ar_transaction_history th ");
        queryBuilder.append("where");
        queryBuilder.append("  th.customer_number = '").append(customerNumber).append("'");
        if (CommonUtils.isEqualIgnoreCase(source, "FCL")) {
            queryBuilder.append("  and th.invoice_or_bl = '").append(blNumber).append("'");
        } else if (CommonUtils.isEqualIgnoreCase(source, "LCL")) {
            queryBuilder.append("  and (");
            queryBuilder.append("    th.invoice_or_bl = '").append(blNumber).append("'");
            queryBuilder.append("    or th.invoice_or_bl = '").append(invoiceNumber).append("'");
            queryBuilder.append("  )");
        } else if (CommonUtils.in(source, "INV", "FCL, INV", "LCL, INV")) {
            queryBuilder.append("  and th.invoice_or_bl = '").append(invoiceNumber).append("'");
        } else if (CommonUtils.isNotEmpty(blNumber)) {
            queryBuilder.append("  and th.invoice_or_bl = '").append(blNumber).append("'");
        } else {
            queryBuilder.append("  and th.invoice_or_bl = '").append(invoiceNumber).append("'");
        }
        queryBuilder.append(" order by th.transaction_date, th.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("transactionDate", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("transactionAmount", StringType.INSTANCE);
        query.addScalar("batchNumber", StringType.INSTANCE);
        query.addScalar("checkNumber", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("adjustmentDate", StringType.INSTANCE);
        query.addScalar("adjustmentAmount", StringType.INSTANCE);
        query.addScalar("user", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public List<ResultModel> getUnpostedTransactions(String source, Integer id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  date_format(t.transaction_date, '%m/%d/%Y') as transactionDate,");
        queryBuilder.append("  date_format(t.posted_date, '%m/%d/%Y') as postedDate,");
        queryBuilder.append("  t.type as type,");
        queryBuilder.append("  format(t.transaction_amount, 2) as transactionAmount,");
        queryBuilder.append("  t.batch_number as batchNumber,");
        queryBuilder.append("  upper(t.check_number) as checkNumber,");
        queryBuilder.append("  t.gl_account as glAccount,");
        queryBuilder.append("  date_format(t.adjustment_date, '%m/%d/%Y') as adjustmentDate,");
        queryBuilder.append("  format(t.adjustment_amount, 2) as adjustmentAmount,");
        queryBuilder.append("  upper(t.user) as user ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    (select");
        queryBuilder.append("      ab.deposit_date as transaction_date,");
        queryBuilder.append("      null as posted_date,");
        queryBuilder.append("      'AR PY' as type,");
        queryBuilder.append("      -py.payment_amt as transaction_amount,");
        queryBuilder.append("      ab.batch_id as batch_number,");
        queryBuilder.append("      pc.check_no as check_number,");
        queryBuilder.append("      py.charge_code as gl_account,");
        queryBuilder.append("      if(");
        queryBuilder.append("        py.adjustment_amt <> 0,");
        queryBuilder.append("        ab.deposit_date,");
        queryBuilder.append("        null");
        queryBuilder.append("      ) as adjustment_date,");
        queryBuilder.append("      -py.adjustment_amt as adjustment_amount,");
        queryBuilder.append("      py.username as user,");
        queryBuilder.append("      false as posted,");
        queryBuilder.append("      py.id as id");
        queryBuilder.append("    from");
        queryBuilder.append("      payments py");
        queryBuilder.append("      join ar_batch ab");
        queryBuilder.append("        on (py.batch_id = ab.batch_id and ab.status = 'Open')");
        queryBuilder.append("      join payment_checks pc");
        queryBuilder.append("        on (py.payment_check_id = pc.id)");
        queryBuilder.append("    where py.transaction_id = ").append(id);
        queryBuilder.append("      and py.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("')");
        queryBuilder.append("    union");
        queryBuilder.append("    (select ");
        queryBuilder.append("      ap.date as transaction_date,");
        queryBuilder.append("      null as posted_date,");
        queryBuilder.append("      'AP INV' as type,");
        queryBuilder.append("      -ar.ap_invoice_amount as transaction_amount,");
        queryBuilder.append("      null as batch_number,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        ap.account_number,");
        queryBuilder.append("        '-',");
        queryBuilder.append("        ap.invoice_number");
        queryBuilder.append("      ) as check_number,");
        queryBuilder.append("      null as gl_account,");
        queryBuilder.append("      null as adjustment_date,");
        queryBuilder.append("      null as adjustment_amount,");
        queryBuilder.append("      ud.login_name as user,");
        queryBuilder.append("      false as posted,");
        queryBuilder.append("      ap.id as id");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction ar");
        queryBuilder.append("      join ap_invoice ap");
        queryBuilder.append("        on (ar.ap_invoice_id = ap.id)");
        queryBuilder.append("      left join user_details ud");
        queryBuilder.append("        on (ar.updated_by = ud.user_id)");
        queryBuilder.append("    where ar.transaction_id = '").append(id).append("')");
        queryBuilder.append("  ) as t ");
        queryBuilder.append("order by t.posted, t.transaction_date, t.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("transactionDate", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("transactionAmount", StringType.INSTANCE);
        query.addScalar("batchNumber", StringType.INSTANCE);
        query.addScalar("checkNumber", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("adjustmentDate", StringType.INSTANCE);
        query.addScalar("adjustmentAmount", StringType.INSTANCE);
        query.addScalar("user", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public ResultModel getMoreInfo(String source, Integer id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  trim(upper(ar.bill_ladding_no)) AS blNumber,");
        queryBuilder.append("  trim(upper(ar.invoice_number)) as invoiceNumber,");
        queryBuilder.append("  trim(upper(ar.vessel_no)) as vesselNumber,");
        queryBuilder.append("  trim(upper(ar.voyage_no)) as voyageNumber,");
        queryBuilder.append("  trim(upper(ar.master_bl)) as masterBl,");
        queryBuilder.append("  trim(if(ar.bill_ladding_no LIKE 'IMP-%', (select upper(b.sub_house_bl) from lcl_file_number f join lcl_booking_import b ON (b.file_number_id = f.id) where f.file_number = ar.drcpt limit 1), '')) AS subHouseBl,");
        queryBuilder.append("  trim(if(ar.bill_ladding_no like 'IMP-%', (select upper(a.ams_no) from lcl_file_number f join lcl_booking_import_ams a on (a.file_number_id = f.id) where f.file_number = ar.drcpt limit 1), '')) as amsHouseBl,");
        queryBuilder.append("  trim(upper(ar.bl_terms)) as blTerms,");
        queryBuilder.append("  trim(upper(ar.container_no)) as containerNumber,");
        queryBuilder.append("  trim(upper(ar.customer_reference_no)) as reference,");
        queryBuilder.append("  ar.transaction_id as id ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ar ");
        queryBuilder.append("  left join lcl_file_number lcl");
        queryBuilder.append("    on (");
        queryBuilder.append("      lcl.file_number = ar.drcpt");
        queryBuilder.append("    )");
        queryBuilder.append("where");
        queryBuilder.append("  ar.transaction_id = ").append(id);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("vesselNumber", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("masterBl", StringType.INSTANCE);
        query.addScalar("subHouseBl", StringType.INSTANCE);
        query.addScalar("amsHouseBl", StringType.INSTANCE);
        query.addScalar("blTerms", StringType.INSTANCE);
        query.addScalar("containerNumber", StringType.INSTANCE);
        query.addScalar("reference", StringType.INSTANCE);
        query.addScalar("id", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return (ResultModel) query.uniqueResult();
    }

    public List<ResultModel> getInvoices(String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(fcl.bolid <> '', 'FCL', if(lcl.file_number <> '', 'LCL', if(red.invoice_number <> '', 'INV', ''))) as source,");
        queryBuilder.append("  IF(fcl.bolid <> '',UPPER(SUBSTRING_INDEX(ar.bill_ladding_no,'-04-',-1)),UPPER(ar.drcpt)) AS dockReceipt,");
        queryBuilder.append("  upper(ar.bill_ladding_no) as blNumber,");
        queryBuilder.append("  upper(ar.invoice_number) as invoiceNumber,");
        queryBuilder.append("  ar.cust_no as customerNumber,");
        queryBuilder.append("  ar.`correction_flag` AS correctionFlag ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ar");
        queryBuilder.append("  left join fcl_bl fcl");
        queryBuilder.append("    on (");
        queryBuilder.append("      ar.drcpt = fcl.file_no");
        queryBuilder.append("    )");
        queryBuilder.append("  left join lcl_file_number lcl");
        queryBuilder.append("    on (");
        queryBuilder.append("      ar.drcpt = lcl.file_number");
        queryBuilder.append("    )");
        queryBuilder.append("  left join ar_red_invoice red");
        queryBuilder.append("    on (");
        queryBuilder.append("      ar.invoice_number = red.invoice_number");
        queryBuilder.append("      and ar.cust_no = red.customer_number");
        queryBuilder.append("    )");
        queryBuilder.append("where");
        queryBuilder.append("  ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (ids.contains(",")) {
            queryBuilder.append("  and ar.transaction_id in (").append(ids).append(") ");
        } else {
            queryBuilder.append("  and ar.transaction_id = ").append(ids).append(" ");
        }
        queryBuilder.append("group by ar.cust_no, ar.bill_ladding_no, ar.invoice_number");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("source", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("correctionFlag", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public List<Transaction> getMassAdjustmentInvoices() throws Exception {
        Double thresholdAmount = Double.parseDouble(LoadLogisoftProperties.getProperty("arInquiryAdjustAmountThresHold"));
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.between("balance", -thresholdAmount, thresholdAmount));
        criteria.add(Restrictions.neProperty("balance", "transactionAmt"));
        criteria.add(Restrictions.ne("balance", 0d));
        criteria.add(Restrictions.ne("balanceInProcess", 0d));
        return criteria.list();
    }

    public void saveAdjustment(Transaction ar, String invoiceOrBl, String glAccount, User user) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        double amount = -ar.getBalance();
        ar.setBalance(ar.getBalance() + amount);
        ar.setBalanceInProcess(ar.getBalanceInProcess() + amount);
        //Payment for Adjustments
        Payments payment = new Payments();
        payment.setCheck_no(AccountingConstants.ADJ_CHECK_NO);
        payment.setChargeCode(glAccount);
        payment.setAdjustmentAmt(amount);
        payment.setTransactionId(ar.getTransactionId());
        payment.setTransactionType(ar.getTransactionType());
        payment.setCustNo(ar.getCustNo());
        payment.setCheckDate(new Date());
        payment.setBatchDate(new Date());
        payment.setInvoiceNo(ar.getInvoiceNumber());
        payment.setBillLaddingNo(ar.getBillLaddingNo());
        payment.setAdjustmentDate(new Date());
        payment.setUserName(user.getLoginName());
        new PaymentsDAO().save(payment);
        //Saving into history
        ArTransactionHistory history = new ArTransactionHistory();
        history.setCustomerNumber(ar.getCustNo());
        history.setBlNumber(ar.getBillLaddingNo());
        history.setInvoiceNumber(ar.getInvoiceNumber());
        history.setInvoiceDate(new Date());
        history.setTransactionDate(new Date());
        history.setPostedDate(new Date());
        history.setAdjustmentAmount(amount);
        history.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
        history.setVoyageNumber(ar.getVoyageNo());
        history.setArBatchId(null);
        history.setApBatchId(null);
        history.setTransactionType("ADJ");
        history.setCreatedBy(user.getLoginName());
        history.setCreatedDate(new Date());
        new ArTransactionHistoryDAO().save(history);

        //Subledger for Adjustments
        TransactionLedger subledger = new TransactionLedger();
        subledger.setCustName(ar.getCustName());
        subledger.setCustNo(ar.getCustNo());
        subledger.setTransactionAmt(amount);
        subledger.setTransactionDate(new Date());
        subledger.setPostedDate(new Date());
        subledger.setBalance(0d);
        subledger.setBalanceInProcess(0d);
        subledger.setStatus(AccountingConstants.STATUS_OPEN);
        subledger.setCustomerReferenceNo("ADJ Done from AR Inquiry");
        subledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
        subledger.setChargeCode(glAccount);
        subledger.setGlAccountNumber(glAccount);
        subledger.setSubledgerSourceCode(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE);
        subledger.setArBatchId(null);
        subledger.setApBatchId(null);
        subledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
        subledger.setInvoiceNumber(ar.getInvoiceNumber());
        subledger.setBillLaddingNo(ar.getBillLaddingNo());
        subledger.setChequeNumber(AccountingConstants.ADJ_CHECK_NO);
        subledger.setCreatedBy(user.getUserId());
        subledger.setCreatedOn(new Date());
        new AccountingLedgerDAO().save(subledger);

        StringBuilder desc = new StringBuilder();
        desc.append("Adjustment of '").append(NumberUtils.formatNumber(amount)).append("'");
        desc.append(" for the Invoice/BL '").append(invoiceOrBl).append("'");
        desc.append(" by ").append(user.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        String moduleRefId = ar.getCustNo() + "-" + invoiceOrBl;
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, moduleRefId, NotesConstants.AR_INVOICE, user);

        transactionDAO.update(ar);
    }

    public void updateInvoice(Integer id, String newCustName, String newCustNo, String customerReference, User user) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        Transaction ar = transactionDAO.findById(id);
        String queryString = "";
        if (CommonUtils.isNotEmpty(customerReference)) {
            String custNo = ar.getCustNo();
            String invoiceOrBl = CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber();

            //update in history records
            queryString = "update ar_transaction_history set customer_reference_number = ?0 where customer_number = ?1 and (bl_number = ?2 or invoice_number = ?3)";
            Query queryObject = transactionDAO.getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter("0", customerReference);
            queryObject.setParameter("1", custNo);
            queryObject.setParameter("2", invoiceOrBl);
            queryObject.setParameter("3", invoiceOrBl);
            queryObject.executeUpdate();

            //update in transaction
            queryString = "update transaction set customer_reference_no = ?0 where cust_no = ?1 and (bill_ladding_no = ?2 or invoice_number = ?3)and transaction_type = ?4";
            Query querytransaction = transactionDAO.getCurrentSession().createSQLQuery(queryString);
            querytransaction.setParameter("0", customerReference);
            querytransaction.setParameter("1", custNo);
            querytransaction.setParameter("2", invoiceOrBl);
            querytransaction.setParameter("3", invoiceOrBl);
            querytransaction.setParameter("4", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            querytransaction.executeUpdate();

            StringBuilder desc = new StringBuilder();
            desc.append("Customer Reference updated to --> ").append(customerReference);
            desc.append(" by ").append(user.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String moduleRefId = custNo + "-" + invoiceOrBl;
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, moduleRefId, NotesConstants.AR_INVOICE, user);
        } else {
            String oldCustName = ar.getCustName();
            String oldCustNo = ar.getCustNo();
            String oldInvoiceOrBl = CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber();
            String newInvoiceOrBl = oldInvoiceOrBl;
            Transaction mergedAr = transactionDAO.getTransaction(newCustNo, oldInvoiceOrBl, id);
            if (null != mergedAr) {
                //Merging invoices of same customer
                mergedAr.setTransactionAmt(mergedAr.getTransactionAmt() + ar.getTransactionAmt());
                mergedAr.setBalance(mergedAr.getBalance() + ar.getBalance());
                mergedAr.setBalanceInProcess(mergedAr.getBalanceInProcess() + ar.getBalanceInProcess());
                if ("PRE PAYMENT".equals(ar.getInvoiceNumber()) && "PRE PAYMENT".equals(mergedAr.getInvoiceNumber())) {
                    if (DateUtils.compareTo(ar.getTransactionDate(), mergedAr.getTransactionDate()) < 0) {
                        mergedAr.setTransactionDate(ar.getTransactionDate());
                    }
                    if (DateUtils.compareTo(ar.getPostedDate(), mergedAr.getPostedDate()) < 0) {
                        mergedAr.setPostedDate(ar.getPostedDate());
                    }
                } else if (!"PRE PAYMENT".equals(ar.getInvoiceNumber()) && "PRE PAYMENT".equals(mergedAr.getInvoiceNumber())) {
                    mergedAr.setTransactionDate(ar.getTransactionDate());
                    mergedAr.setPostedDate(ar.getPostedDate());
                } else if ("PRE PAYMENT".equals(ar.getInvoiceNumber()) && !"PRE PAYMENT".equals(mergedAr.getInvoiceNumber())) {
                    //Nothing
                } else {
                    if (DateUtils.compareTo(ar.getTransactionDate(), mergedAr.getTransactionDate()) < 0) {
                        mergedAr.setTransactionDate(ar.getTransactionDate());
                    }
                    if (DateUtils.compareTo(ar.getPostedDate(), mergedAr.getPostedDate()) < 0) {
                        mergedAr.setPostedDate(ar.getPostedDate());
                    }
                }
                mergedAr.setCreditHold("Y".equals(ar.getCreditHold()) ? "Y" : mergedAr.getCreditHold());
                mergedAr.setEmailed(ar.isEmailed() ? true : mergedAr.isEmailed());
                mergedAr.setRemovedFromHold(ar.isRemovedFromHold() ? true : mergedAr.isRemovedFromHold());
                transactionDAO.delete(ar);
                transactionDAO.update(mergedAr);

                //update in payments
                queryString = "update payments set cust_no = ?0, transaction_id = ?1 where transaction_id = ?2 and transaction_type = ?3";
                Query queryObject = transactionDAO.getCurrentSession().createSQLQuery(queryString);
                queryObject.setParameter("0", newCustNo);
                queryObject.setParameter("1", mergedAr.getTransactionId());
                queryObject.setParameter("2", id);
                queryObject.setParameter("3", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                queryObject.executeUpdate();

                newInvoiceOrBl = CommonUtils.isNotEmpty(mergedAr.getBillLaddingNo()) ? mergedAr.getBillLaddingNo() : mergedAr.getInvoiceNumber();
            } else {
                //Moving invoice from one customer to another
                ar.setCustName(newCustName);
                ar.setCustNo(newCustNo);
                transactionDAO.update(ar);

                //update in payments
                queryString = "update payments set cust_no = ?0 where transaction_id = ?1 and transaction_type = ?2";
                Query queryObject = transactionDAO.getCurrentSession().createSQLQuery(queryString);
                queryObject.setParameter("0", newCustNo);
                queryObject.setParameter("1", id);
                queryObject.setParameter("2", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                queryObject.executeUpdate();
            }
            //update in history records
            queryString = "update ar_transaction_history set customer_number = ?0, bl_number = ?1, invoice_number = ?2 where customer_number = ?3 and (bl_number = ?4 or invoice_number = ?5)";
            Query queryObject = transactionDAO.getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter("0", newCustNo);
            queryObject.setParameter("1", newInvoiceOrBl);
            queryObject.setParameter("2", newInvoiceOrBl.startsWith("IMP-") ? newInvoiceOrBl.replace("IMP-", "") : newInvoiceOrBl);
            queryObject.setParameter("3", oldCustNo);
            queryObject.setParameter("4", oldInvoiceOrBl);
            queryObject.setParameter("5", oldInvoiceOrBl);
            queryObject.executeUpdate();

            //update in subledgers
            queryString = "update transaction_ledger set cust_name = ?0, cust_no = ?1, bill_ladding_no = ?2,invoice_number = ?3 where cust_no = ?4 and (bill_ladding_no = ?5 or invoice_number = ?6) and subledger_source_code like ?7";
            Query querytransaction = transactionDAO.getCurrentSession().createSQLQuery(queryString);
            querytransaction.setParameter("0", newCustName);
            querytransaction.setParameter("1", newCustNo);
            querytransaction.setParameter("2", newInvoiceOrBl);
            querytransaction.setParameter("3", newInvoiceOrBl.startsWith("IMP-") ? newInvoiceOrBl.replace("IMP-", "") : newInvoiceOrBl);
            querytransaction.setParameter("4", oldCustNo);
            querytransaction.setParameter("5", oldInvoiceOrBl);
            querytransaction.setParameter("6", oldInvoiceOrBl);
            querytransaction.setParameter("7", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "%");
            querytransaction.executeUpdate();

            String oldKey = oldCustNo + "-" + oldInvoiceOrBl;
            String newKey = newCustNo + "-" + newInvoiceOrBl;

            //update in documents
            queryString = "update document_store_log set document_id = ?0 where document_id = ?1 and screen_name = ?2";
            Query queryObjectPayment = transactionDAO.getCurrentSession().createSQLQuery(queryString);
            queryObjectPayment.setParameter("0", newKey);
            queryObjectPayment.setParameter("1", oldKey);
            queryObjectPayment.setParameter("2", "INVOICE");
            queryObjectPayment.executeUpdate();

            //update in notes
            queryString = "update notes set module_ref_id = ?0 where module_ref_id = ?1 and module_id = ?2";
            Query queryObjectnotes = transactionDAO.getCurrentSession().createSQLQuery(queryString);
            queryObjectnotes.setParameter("0", newKey);
            queryObjectnotes.setParameter("1", oldKey);
            queryObjectnotes.setParameter("2", NotesConstants.AR_INVOICE);
            queryObjectnotes.executeUpdate();

            StringBuilder desc = new StringBuilder();
            desc.append("Updated Customer name");
            desc.append(" from ").append(oldCustName).append("(").append(oldCustNo).append(")");
            desc.append(" to ").append(newCustName).append("(").append(newCustNo).append(")");
            desc.append(" by ").append(user.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, newKey, NotesConstants.AR_INVOICE, user);
        }
    }

    public String getFclBillingTerminal(String fileNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("   substring_index(billing_terminal,'-', -1) as billing_terminal ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl ");
        queryBuilder.append("where");
        queryBuilder.append("  file_no = '").append(fileNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("billing_terminal", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public String getLclBillingTerminal(String fileNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("   b.billing_terminal as billing_terminal ");
        queryBuilder.append("from");
        queryBuilder.append("  lcl_file_number f");
        queryBuilder.append("  join lcl_booking b ");
        queryBuilder.append("    on (f.id = b.file_number_id)");
        queryBuilder.append("where");
        queryBuilder.append("  f.file_number = '").append(fileNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("billing_terminal", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public List<Model> getLclImpChecks(Long fileNoId, String custNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  a.`reference_no` as checkNumber,");
        queryBuilder.append("  concat_ws(");
        queryBuilder.append("    ' / ',");
        queryBuilder.append("    if(a.`reference_name` <> '', ucase(a.`reference_name`), null),");
        queryBuilder.append("    date_format(a.`trans_datetime`, '%d-%M-%Y')");
        queryBuilder.append("  ) as paidBy,");
        queryBuilder.append("  format(a.`amount`, 2) as amount,");
        queryBuilder.append("  concat_ws(");
        queryBuilder.append("    ' / ',");
        queryBuilder.append("    ucase(`UserDetailsGetLoginNameByID` (a.`entered_by_user_id`)),");
        queryBuilder.append("    date_format(a.`entered_datetime`, '%d-%M-%Y')");
        queryBuilder.append("  ) as enteredBy,");
        queryBuilder.append("  a.`payment_type` as paymentType ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    if(");
        queryBuilder.append("      a.`ar_bill_to_party` = 'C',");
        queryBuilder.append("      b.`cons_acct_no`,");
        queryBuilder.append("      if(");
        queryBuilder.append("        a.`ar_bill_to_party` = 'N',");
        queryBuilder.append("        b.`noty_acct_no`,");
        queryBuilder.append("        if(");
        queryBuilder.append("          a.`ar_bill_to_party` = 'T',");
        queryBuilder.append("          b.`third_party_acct_no`,");
        queryBuilder.append("          null");
        queryBuilder.append("        )");
        queryBuilder.append("      )");
        queryBuilder.append("    ) as cust_no,");
        queryBuilder.append("    tx.`reference_no`,");
        queryBuilder.append("    tx.`reference_name`,");
        queryBuilder.append("    tx.`trans_datetime`,");
        queryBuilder.append("    tx.`amount`,");
        queryBuilder.append("    tx.`entered_by_user_id`,");
        queryBuilder.append("    tx.`entered_datetime`,");
        queryBuilder.append("    tx.`payment_type` ");
        queryBuilder.append("  from");
        queryBuilder.append("    `lcl_booking` b ");
        queryBuilder.append("    join `lcl_booking_ac` a ");
        queryBuilder.append("      on (");
        queryBuilder.append("        b.`file_number_id` = a.`file_number_id` ");
        queryBuilder.append("        and a.`ar_bill_to_party` not in ('A', 'W') ");
        queryBuilder.append("        and a.`ar_amount` <> 0.00 ");
        queryBuilder.append("        and a.`ar_gl_mapping_id` is not null");
        queryBuilder.append("      ) ");
        queryBuilder.append("    join `lcl_booking_ac_ta` t ");
        queryBuilder.append("      on (a.`id` = t.`lcl_booking_ac_id`) ");
        queryBuilder.append("    join `lcl_booking_ac_trans` tx ");
        queryBuilder.append("      on (");
        queryBuilder.append("        t.`lcl_booking_ac_trans_id` = tx.`id` ");
        queryBuilder.append("        and tx.`reference_no` <> ''");
        queryBuilder.append("      ) ");
        queryBuilder.append("  where b.`file_number_id` = :fileNoId");
        queryBuilder.append("  group by tx.`id`) as a ");
        queryBuilder.append("where a.`cust_no` = :custNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNoId", fileNoId);
        query.setString("custNo", custNo);
        query.addScalar("checkNumber", StringType.INSTANCE);
        query.addScalar("paidBy", StringType.INSTANCE);
        query.addScalar("amount", StringType.INSTANCE);
        query.addScalar("enteredBy", StringType.INSTANCE);
        query.addScalar("paymentType", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(Model.class));
        return query.list();
    }

    public List<Model> getLclImpPayments(Long fileNoId, String custNo) {
        getCurrentSession().createSQLQuery("set @total_amount \\:= 0.00").executeUpdate();
        getCurrentSession().createSQLQuery("set @total_payment \\:= 0.00").executeUpdate();
        getCurrentSession().createSQLQuery("set @total_balance \\:= 0.00").executeUpdate();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  a.`charge_desc` as chargeDesc,");
        queryBuilder.append("  format(a.`amount`, 2) as amount,");
        queryBuilder.append("  format(a.`payment`, 2) as payment,");
        queryBuilder.append("  format(a.`balance`, 2) as balance ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    `GlMappingGetDescById`(a.`ar_gl_mapping_id`) as charge_desc,");
        queryBuilder.append("    a.`amount`,");
        queryBuilder.append("    a.`payment`,");
        queryBuilder.append("    a.`amount` - a.`payment` as balance,");
        queryBuilder.append("    @total_amount \\:= @total_amount + a.`amount`,");
        queryBuilder.append("    @total_payment \\:= @total_payment + a.`payment`,");
        queryBuilder.append("    @total_balance \\:= @total_balance + (a.`amount` - a.`payment`) ");
        queryBuilder.append("  from");
        queryBuilder.append("    (select ");
        queryBuilder.append("      a.`ar_gl_mapping_id`,");
        queryBuilder.append("      if(");
        queryBuilder.append("        a.`ar_bill_to_party` = 'C',");
        queryBuilder.append("        b.`cons_acct_no`,");
        queryBuilder.append("        if(");
        queryBuilder.append("          a.`ar_bill_to_party` = 'N',");
        queryBuilder.append("          b.`noty_acct_no`,");
        queryBuilder.append("          if(");
        queryBuilder.append("            a.`ar_bill_to_party` = 'T',");
        queryBuilder.append("            b.`third_party_acct_no`,");
        queryBuilder.append("            null");
        queryBuilder.append("          )");
        queryBuilder.append("        )");
        queryBuilder.append("      ) as cust_no,");
        queryBuilder.append("      a.`ar_amount` as amount,");
        queryBuilder.append("      sum(coalesce(t.`amount`, 0.00)) as payment	");
        queryBuilder.append("    from");
        queryBuilder.append("      `lcl_booking` b ");
        queryBuilder.append("      join `lcl_booking_ac` a ");
        queryBuilder.append("        on (");
        queryBuilder.append("          b.`file_number_id` = a.`file_number_id` ");
        queryBuilder.append("          and a.`ar_bill_to_party` not in ('A', 'W')");
        queryBuilder.append("          and a.`ar_amount` <> 0.00 ");
        queryBuilder.append("          and a.`ar_gl_mapping_id` is not null");
        queryBuilder.append("        ) ");
        queryBuilder.append("      left join `lcl_booking_ac_ta` t ");
        queryBuilder.append("        on (a.`id` = t.`lcl_booking_ac_id`) ");
        queryBuilder.append("    where b.`file_number_id` = :fileNoId");
        queryBuilder.append("    group by a.`id`) as a ");
        queryBuilder.append("  where a.`cust_no` = :custNo ");
        queryBuilder.append("  order by field(charge_desc, 'OCEAN FREIGHT IMPORT') desc, charge_desc asc");
        queryBuilder.append("  ) as a ");
        queryBuilder.append("union all ");
        queryBuilder.append("select");
        queryBuilder.append("  'TOTAL ($-USD)' as chargeDesc,");
        queryBuilder.append("  format(@total_amount, 2) as amount,");
        queryBuilder.append("  format(@total_payment, 2) as payment,");
        queryBuilder.append("  format(@total_balance, 2) as balance");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNoId", fileNoId);
        query.setString("custNo", custNo);
        query.addScalar("chargeDesc", StringType.INSTANCE);
        query.addScalar("amount", StringType.INSTANCE);
        query.addScalar("payment", StringType.INSTANCE);
        query.addScalar("balance", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(Model.class));
        return query.list();
    }

    public void updateCustomerReference(String custNo, String referenceType, String userName) throws Exception {
        StringBuilder table = new StringBuilder();
        table.append("  `transaction` ar");
        table.append("  join `lcl_file_number` fn");
        table.append("    on (fn.`file_number` = ar.`drcpt`)");

        StringBuilder where = new StringBuilder();
        where.append("where ar.`cust_no` = :custNo");
        where.append("  and ar.`bill_ladding_no` like 'IMP-%' ");
        where.append("  and ar.`transaction_type` = 'AR' ");
        where.append("  and ar.`balance` <> 0.00");

        StringBuilder customerReference = new StringBuilder();
        if ("Master BL".equalsIgnoreCase(referenceType)) {
            customerReference.append("coalesce((select ucase(um.`masterbl`) from `lcl_booking_piece` bp join `lcl_booking_piece_unit` pu on (pu.`booking_piece_id` = bp.`id`) join `lcl_unit_ss` us on (us.`id` = pu.`lcl_unit_ss_id`) join `lcl_unit_ss_manifest` um on (um.`ss_header_id` = us.`ss_header_id` and um.`unit_id` = us.`unit_id`) where bp.`file_number_id` = fn.`id` limit 1), '')");
        } else if ("Sub House BL".equalsIgnoreCase(referenceType)) {
            customerReference.append("coalesce((select ucase(i.`sub_house_bl`) from `lcl_booking_import` i where i.`file_number_id` = fn.`id` limit 1), '')");
        } else if ("AMS House BL".equalsIgnoreCase(referenceType)) {
            customerReference.append("coalesce((select ucase(a.`ams_no`) from `lcl_booking_import_ams` a where a.`file_number_id` = fn.`id` limit 1), '')");
        } else if ("Container Number".equalsIgnoreCase(referenceType)) {
            customerReference.append("coalesce((select ucase(ut.`unit_no`) from `lcl_booking_piece` bp join `lcl_booking_piece_unit` pu on (pu.`booking_piece_id` = bp.`id`) join `lcl_unit_ss` us on (us.`id` = pu.`lcl_unit_ss_id`) join `lcl_unit` ut on (ut.`id` = us.`unit_id`) where bp.`file_number_id` = fn.`id` limit 1), '')");
        }

        StringBuilder insert = new StringBuilder();
        insert.append("insert into `notes` (");
        insert.append("  `module_id`,");
        insert.append("  `module_ref_id`,");
        insert.append("  `item_name`,");
        insert.append("  `note_desc`,");
        insert.append("  `note_type`,");
        insert.append("  `status`,");
        insert.append("  `void`,");
        insert.append("  `updatedate`,");
        insert.append("  `updated_by`");
        insert.append(") ");
        insert.append("select ");
        insert.append("  '").append(NotesConstants.AR_INVOICE).append("' as module_id,");
        insert.append("  concat(ar.`cust_no`, '-', ar.`bill_ladding_no`) as module_ref_id,");
        insert.append("  '").append(NotesConstants.AR_INVOICE).append("' as item_name,");
        insert.append("  concat('Customer Reference: ', ifnull(ar.`customer_reference_no`, ''), ' --> ', ").append(customerReference).append(") as notes_desc,");
        insert.append("  'auto' as note_type,");
        insert.append("  'Pending' as status,");
        insert.append("  'N' as void,");
        insert.append("  now() as updatedate,");
        insert.append("  '").append(userName).append("' as updated_by ");
        insert.append("from");
        insert.append(table);
        insert.append(where);
        SQLQuery query = getCurrentSession().createSQLQuery(insert.toString());
        query.setString("custNo", custNo);
        query.executeUpdate();
        
        StringBuilder update = new StringBuilder();
        update.append("update");
        update.append(table);
        update.append("set");
        update.append("  ar.`customer_reference_no` = ").append(customerReference);
        update.append(where);
        query = getCurrentSession().createSQLQuery(update.toString());
        query.setString("custNo", custNo);
        query.executeUpdate();
    }
}
