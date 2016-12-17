package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentChecksDAO;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.ApInquiryDAO;
import com.logiware.accounting.dao.ArInquiryDAO;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.ArBatchBean;
import com.logiware.bean.PaymentBean;
import com.logiware.bean.PaymentCheckBean;
import com.logiware.form.ArBatchForm;
import com.logiware.utils.AuditNotesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;

/**
 *
 * @author lakshh
 */
public class ArBatchDAO extends BaseHibernateDAO implements ConstantsInterface {

    public void save(ArBatch transientInstance) {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public Integer saveAndReturnId(ArBatch transientInstance) throws Exception {
        getSession().save(transientInstance);
        return transientInstance.getBatchId();
    }

    public void update(ArBatch persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
    }

    public void delete(ArBatch persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
    }

    public ArBatch findById(Integer id) throws Exception {
        getSession().flush();
        ArBatch instance = (ArBatch) getSession().get("com.gp.cvst.logisoft.domain.ArBatch", id);
        return instance;
    }

    public List<String> getUsers() throws Exception {
        String query = "SELECT login_name FROM user_details WHERE STATUS='ACTIVE'";
        return getCurrentSession().createSQLQuery(query).list();
    }

    public ArBatchBean getArBatch(Integer batchId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select cast(Batch_id as char character set latin1),date_format(Deposit_date,'%m/%d/%Y'),");
        queryBuilder.append("date_format(date,'%m/%d/%Y'),batch_type,format(Total_Amount,2),format(Applied_Amount,2),");
        queryBuilder.append("if(batch_type='D',format(Total_Amount-Applied_Amount,2),''),User_id,Bank_Account,Gl_Account,Status,notes");
        queryBuilder.append(" from ar_batch where batch_id=").append(batchId);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        Object[] col = (Object[]) result;
        ArBatchBean arBatch = new ArBatchBean();
        arBatch.setBatchId((String) col[0]);
        arBatch.setDepositDate((String) col[1]);
        arBatch.setDate((String) col[2]);
        arBatch.setBatchType((String) col[3]);
        arBatch.setTotalAmount((String) col[4]);
        arBatch.setAppliedAmount((String) col[5]);
        arBatch.setBalanceAmount((String) col[6]);
        arBatch.setUser((String) col[7]);
        arBatch.setBankAccount((String) col[8]);
        arBatch.setGlAccount((String) col[9]);
        arBatch.setStatus((String) col[10]);
        arBatch.setNotes((String) col[11]);
        return arBatch;
    }

    public String buildArBatchSearchQuery(ArBatchForm arBatchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ar_batch b");
        if (CommonUtils.isNotEmpty(arBatchForm.getUser())) {
            queryBuilder.append(" where b.user_id = '").append(arBatchForm.getUser()).append("'");
        } else {
            queryBuilder.append(" where b.user_id is not null");
        }
        if (CommonUtils.isNotEmpty(arBatchForm.getBatchId())) {
            queryBuilder.append(" and b.batch_id = ").append(arBatchForm.getBatchId());
        } else {
            if (CommonUtils.isEqualIgnoreCase(arBatchForm.getStatus(), STATUS_OPEN)) {
                queryBuilder.append(" and b.status = '").append(STATUS_OPEN).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(arBatchForm.getStatus(), STATUS_CLOSED)) {
                queryBuilder.append(" and (b.status = '").append(STATUS_CLOSED).append("'");
                queryBuilder.append(" or b.status = '").append(STATUS_RECONCILE_IN_PROGRESS).append("'");
                queryBuilder.append(" or b.status = '").append(CLEAR).append("')");
            }
            if (CommonUtils.isNotEqual(arBatchForm.getBatchType(), AccountingConstants.AR_BATCH_BOTH)) {
                queryBuilder.append(" and b.batch_type = '").append(arBatchForm.getBatchType()).append("'");
            }
            if (CommonUtils.isNotEmpty(arBatchForm.getFromDate()) || CommonUtils.isNotEmpty(arBatchForm.getToDate())) {
                if (CommonUtils.isNotEmpty(arBatchForm.getFromDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(arBatchForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    queryBuilder.append(" and b.deposit_date >= '").append(fromDate).append(" 00:00:00'");
                }
                if (CommonUtils.isNotEmpty(arBatchForm.getToDate())) {
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(arBatchForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    queryBuilder.append(" and b.deposit_date <= '").append(toDate).append(" 23:59:59'");
                }
            }
            if (CommonUtils.isNotEmpty(arBatchForm.getBatchAmount()) && CommonUtils.isNotEqual(arBatchForm.getBatchAmount(), "0.00")) {
                queryBuilder.append(" and b.total_amount = ").append(arBatchForm.getBatchAmount().replaceAll(",", ""));
            }
        }
        return queryBuilder.toString();
    }

    public Integer getTotalArBatches(String condition) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(b.batch_id)").append(condition);
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public List<ArBatchBean> getArBatches(String sortBy, String orderBy, String condition, int start, int limit) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(b.batch_id as char character set latin1) as batchId,");
        queryBuilder.append("date_format(b.deposit_date,'%m/%d/%Y') as depositDate,");
        queryBuilder.append("b.batch_type as batchType,");
        queryBuilder.append("format(b.total_amount,2) as totalAmount,");
        queryBuilder.append("format(b.applied_amount,2) as appliedAmount,");
        queryBuilder.append("if(b.batch_type = 'D',format(b.balance_amount,2),'') as balanceAmount,");
        queryBuilder.append("b.user as user,");
        queryBuilder.append("b.bank_account as bankAccount,");
        queryBuilder.append("b.gl_account as glAccount,");
        queryBuilder.append("b.status as status,");
        queryBuilder.append("if(count(doc.document_id) > 0,'true','false') as uploaded,");
        queryBuilder.append("date_format(min(tl.cleared_date),'%m/%d/%Y') as clearedDate,");
        queryBuilder.append("upper(b.notes) as notes");
        queryBuilder.append(" from (");
        queryBuilder.append("select b.batch_id as batch_id,");
        queryBuilder.append("b.deposit_date as deposit_date,");
        queryBuilder.append("b.batch_type as batch_type,");
        queryBuilder.append("b.total_amount as total_amount,");
        queryBuilder.append("b.applied_amount as applied_amount,");
        queryBuilder.append("if(b.batch_type = 'D',b.total_amount - b.applied_amount,0) as balance_amount,");
        queryBuilder.append("b.user_id as user,");
        queryBuilder.append("b.bank_account as bank_account,");
        queryBuilder.append("b.gl_account as gl_account,");
        queryBuilder.append("b.status as status,");
        queryBuilder.append("cast(b.batch_id as char character set latin1) as document_id,");
        queryBuilder.append("b.notes as notes");
        queryBuilder.append(condition);
        if (CommonUtils.isNotEmpty(sortBy) && CommonUtils.isNotEmpty(orderBy)) {
            queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        } else {
            queryBuilder.append(" order by b.deposit_date desc");
        }
        queryBuilder.append(" limit ").append(start).append(",").append(limit);
        queryBuilder.append(") as b");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name = 'AR BATCH'");
        queryBuilder.append(" and doc.document_name = 'AR BATCH'");
        queryBuilder.append(" and b.document_id = doc.document_id)");
        queryBuilder.append(" left join transaction_ledger tl");
        queryBuilder.append(" on (b.batch_id = tl.ar_batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and tl.reconciled = '").append(YES).append("'");
        queryBuilder.append(" and tl.cleared_date is not null)");
        queryBuilder.append(" group by b.batch_id");
        if (CommonUtils.isNotEmpty(sortBy) && CommonUtils.isNotEmpty(orderBy)) {
            queryBuilder.append(" order by b.").append(sortBy).append(" ").append(orderBy);
        } else {
            queryBuilder.append(" order by b.deposit_date desc");
        }
        getCurrentSession().flush();
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ArBatchBean.class));
        return query.list();
    }

    public void voidArBatch(String batchId, Integer userId) throws Exception {
        List<String> paymentChecksIds = new PaymentChecksDAO().getPaymentChecksIds(batchId);
        for (String paymentCheckId : paymentChecksIds) {
            voidCheck(batchId, paymentCheckId, userId);
        }
        StringBuilder queryBuilder = new StringBuilder("update ar_batch set Total_Amount=0,Applied_Amount=0,Adjust_Amount=0,");
        queryBuilder.append(" Balance_Amount=0,Prepay_Amount=0,On_Acct_Amount=0,status='").append(STATUS_VOID).append("',Using_By=null");
        queryBuilder.append(" where batch_id=").append(batchId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void voidCheck(String batchId, String paymentCheckId, Integer userId) throws Exception {
        List<String> savedArItems = getArItemsSavedInCheck(batchId, paymentCheckId);
        List<String> savedApItems = getApItemsSavedInCheck(batchId, paymentCheckId);
        List<String> savedAcItems = getAcItemsSavedInCheck(batchId, paymentCheckId);
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        User loginUser = new UserDAO().findById(userId);
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        if (CommonUtils.isNotEmpty(savedArItems)) {
            for (String id : savedArItems) {
                Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(id));
                String invoiceOrBl = CommonUtils.isNotEmpty(transaction.getBillLaddingNo()) ? transaction.getBillLaddingNo() : transaction.getInvoiceNumber();
                String key = transaction.getCustNo() + "-" + invoiceOrBl;
                StringBuilder desc = new StringBuilder("Invoice/BL '").append(invoiceOrBl).append("'");
                desc.append(" of '").append(transaction.getCustNo()).append("'");
                desc.append(" removed from this batch - '").append(batchId).append("'");
                desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, key, NotesConstants.AR_INVOICE, loginUser);
            }
        }
        if (CommonUtils.isNotEmpty(savedApItems)) {
            for (String id : savedApItems) {
                Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(id));
                String key = transaction.getCustNo() + "-" + transaction.getInvoiceNumber();
                StringBuilder desc = new StringBuilder("Invoice '").append(transaction.getInvoiceNumber()).append("'");
                desc.append(" of '").append(transaction.getCustNo()).append("'");
                desc.append(" removed this batch - '").append(batchId).append("'");
                desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, loginUser);
            }
        }
        if (CommonUtils.isNotEmpty(savedAcItems)) {
            FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
            for (String id : savedAcItems) {
                TransactionLedger accrual = accountingLedgerDAO.findById(Integer.parseInt(id));
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
                        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                        if (null != fclBlCostCodes) {
                            fclBlCostCodes.setInvoiceNumber(accrual.getInvoiceNumber());
                            fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        }
                    }
                }
                String key = id;
                StringBuilder desc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    desc.append("B/L - '").append(accrual.getBillLaddingNo().trim()).append("'");
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
                desc.append(" of '").append(accrual.getCustNo()).append("'");
                desc.append(" removed from this batch - '").append(batchId).append("'");
                desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, loginUser);
            }
        }
        removePayments(batchId, paymentCheckId, userId);
        StringBuilder queryBuilder = new StringBuilder("delete from payment_checks where id=").append(paymentCheckId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        queryBuilder = new StringBuilder("update ar_batch");
        queryBuilder.append(" set Applied_Amount=(select if(sum(payment_amt) is null,0,sum(payment_amt)) from payments ");
        queryBuilder.append(" where batch_id=").append(batchId).append("),Balance_Amount=if(batch_type='D',Total_Amount-Applied_Amount,0)");
        queryBuilder.append(" where batch_id=").append(batchId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public String getBatchType(String batchId) {
        StringBuilder queryBuilder = new StringBuilder("select if(batch_type='N','NET SETT','DEPOSIT') as batchType from ar_batch");
        queryBuilder.append(" where batch_id=").append(batchId);
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Integer getTotalApplyPayments(String arQuery, String apQuery, String acQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(totalRows) from (");
        queryBuilder.append("(select count(tr.Transaction_ID) as totalRows").append(arQuery).append(")");
        if (null != apQuery) {
            queryBuilder.append(" union ");
            queryBuilder.append("(select count(tr.Transaction_ID) as totalRows").append(apQuery).append(")");
        }
        if (null != acQuery) {
            queryBuilder.append(" union ");
            queryBuilder.append("(select count(tr.Transaction_ID) as totalRows").append(acQuery).append(")");
        }
        queryBuilder.append(") as tbl");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public List<AccountingBean> searchApplyPayments(ArBatchForm arBatchForm, String arQuery, String apQuery, String acQuery) {
        List<AccountingBean> applyPayments = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tbl.acct_name,tbl.acct_no,tbl.ecu_designation,trim(tbl.Customer_Reference_No),tbl.Charge_Code,");
        queryBuilder.append("tbl.invoiceOrBl,tbl.Voyage_No,tbl.dockReceipt,format(tbl.transactionAmount,2),");
        queryBuilder.append("format(tbl.balanceInProcess,2),tbl.balanceInProcess,date_format(tbl.Transaction_date,'%m/%d/%Y'),");
        queryBuilder.append("tbl.Transaction_type,tbl.transactionId,");
        queryBuilder.append("tbl.Status,tbl.correction_notice,tbl.manifest_flag,tbl.subType,");
        queryBuilder.append("tbl.note_module_id,tbl.note_ref_id,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments,");
        queryBuilder.append("cast(if(red.id <> 0, red.id, '') as char character set latin1) as arInvoiceId");
        queryBuilder.append(" from (");
        queryBuilder.append("select tbl.acct_name,tbl.acct_no,tbl.ecu_designation,tbl.Customer_Reference_No,tbl.Charge_Code,");
        queryBuilder.append("tbl.invoiceOrBl,tbl.Voyage_No,tbl.dockReceipt,tbl.transactionAmount,");
        queryBuilder.append("tbl.balanceInProcess,tbl.Transaction_date,");
        queryBuilder.append("tbl.Transaction_type,tbl.transactionId,");
        queryBuilder.append("tbl.Status,tbl.correction_notice,tbl.manifest_flag,tbl.subType,");
        queryBuilder.append("tbl.note_module_id,tbl.note_ref_id,tbl.document_id from (");
        //getting ar
        queryBuilder.append("(select tp.acct_name,tp.acct_no,tp.ecu_designation,tr.Customer_Reference_No,tr.Charge_Code,");
        queryBuilder.append("if(tr.Bill_Ladding_No!='',tr.Bill_Ladding_No,tr.Invoice_number) as invoiceOrBl,tr.Bill_Ladding_No,");
        queryBuilder.append("tr.Invoice_number,tr.Voyage_No,tr.drcpt as dockReceipt,tr.Transaction_amt as transactionAmount,");
        queryBuilder.append("tr.Balance_In_Process as balanceInProcess,tr.Transaction_date,tr.Transaction_type,");
        queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,");
        queryBuilder.append("tr.Status,tr.correction_notice,tr.manifest_flag,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('AR_INVOICE' as char character set latin1) as note_module_id,");
        queryBuilder.append("concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number)) as note_ref_id,");
        queryBuilder.append("concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number)) as document_id");
        queryBuilder.append(arQuery).append(")");
        if (null != apQuery) {
            //getting ap
            queryBuilder.append(" union ");
            queryBuilder.append("(select tp.acct_name,tp.acct_no,tp.ecu_designation,tr.Customer_Reference_No,tr.Charge_Code,");
            queryBuilder.append("tr.Invoice_number as invoiceOrBl,tr.Bill_Ladding_No,");
            queryBuilder.append("tr.Invoice_number,tr.Voyage_No,tr.drcpt as dockReceipt,-tr.Transaction_amt as transactionAmount,");
            queryBuilder.append("-tr.Balance_In_Process as balanceInProcess,tr.Transaction_date,tr.Transaction_type,");
            queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,");
            queryBuilder.append("tr.Status,tr.correction_notice,tr.manifest_flag,");
            queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
            queryBuilder.append("cast('AP_INVOICE' as char character set latin1) as note_module_id,");
            queryBuilder.append("concat(tp.acct_no,'-',tr.invoice_number) as note_ref_id,");
            queryBuilder.append("concat(tp.acct_no,'-',tr.Invoice_number) as document_id");
            queryBuilder.append(apQuery).append(")");
        }
        if (null != acQuery) {
            //getting ac
            queryBuilder.append(" union ");
            queryBuilder.append("(select tp.acct_name,tp.acct_no,tp.ecu_designation,tr.Customer_Reference_No,tr.Charge_Code,");
            queryBuilder.append("trim(tr.Invoice_number) as invoiceOrBl,tr.Bill_Ladding_No,");
            queryBuilder.append("trim(tr.Invoice_number),tr.Voyage_No,tr.drcpt as dockReceipt,-tr.Transaction_amt as transactionAmount,");
            queryBuilder.append("-tr.Balance_In_Process as balanceInProcess,tr.Transaction_date,tr.Transaction_type,");
            queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,");
            queryBuilder.append("tr.Status,tr.correction_notice,tr.manifest_flag,");
            queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
            queryBuilder.append("cast('ACCRUALS' as char character set latin1) as note_module_id,");
            queryBuilder.append("cast(tr.transaction_id as char character set latin1) as note_ref_id,");
            queryBuilder.append("concat(tp.acct_no,'-',trim(tr.Invoice_number)) as document_id");
            queryBuilder.append(acQuery).append(")");
        }
        queryBuilder.append(") as tbl");
        if (CommonUtils.isNotEmpty(arBatchForm.getSortBy()) && CommonUtils.isNotEmpty(arBatchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append(arBatchForm.getSortBy()).append(" ").append(arBatchForm.getOrderBy());
        } else {
            queryBuilder.append(" order By tbl.Transaction_date,tbl.invoiceOrBl");
        }
        int start = (arBatchForm.getCurrentPageSize() * (arBatchForm.getPageNo() - 1));
        int end = arBatchForm.getCurrentPageSize();
        queryBuilder.append(" limit ").append(start).append(",").append(end);
        queryBuilder.append(") as tbl");
        queryBuilder.append("  left join ar_red_invoice red");
        queryBuilder.append("    on (");
        queryBuilder.append("      tbl.Transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and tbl.invoiceOrBl = red.invoice_number");
        queryBuilder.append("      and tbl.acct_no = red.customer_number");
        queryBuilder.append("    )");
        queryBuilder.append(" left join notes note");
        queryBuilder.append(" on (tbl.note_module_id=note.module_id and tbl.note_ref_id=note.module_ref_id and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name='INVOICE' and doc.document_name='INVOICE' and tbl.document_id=doc.document_id)");
        queryBuilder.append(" group by tbl.transactionId");
        if (CommonUtils.isNotEmpty(arBatchForm.getSortBy()) && CommonUtils.isNotEmpty(arBatchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append(arBatchForm.getSortBy()).append(" ").append(arBatchForm.getOrderBy());
        } else {
            queryBuilder.append(" order By tbl.Transaction_date,tbl.invoiceOrBl");
        }
        Query query = getSession().createSQLQuery(queryBuilder.toString());
        List<Object> result = query.list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean accountingBean = new AccountingBean();
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setEcuDesignation((String) col[2]);
            accountingBean.setCustomerReference((String) col[3]);
            accountingBean.setChargeCode((String) col[4]);
            accountingBean.setInvoiceOrBl((String) col[5]);
            accountingBean.setVoyage((String) col[6]);
            accountingBean.setDockReceipt((String) col[7]);
            accountingBean.setFormattedAmount((String) col[8]);
            accountingBean.setFormattedBalanceInProcess((String) col[9]);
            accountingBean.setBalanceInProcess(Double.parseDouble(col[10].toString()));
            accountingBean.setFormattedDate((String) col[11]);
            accountingBean.setTransactionType((String) col[12]);
            accountingBean.setTransactionId(col[13].toString());
            accountingBean.setStatus((String) col[14]);
            accountingBean.setCorrectionNotice((String) col[15]);
            accountingBean.setManifestFlag((String) col[16]);
            accountingBean.setSubType((String) col[17]);
            accountingBean.setNoteModuleId((String) col[18]);
            accountingBean.setNoteRefId((String) col[19]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[20]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[21]));
            accountingBean.setArInvoiceId((String) col[22]);
            applyPayments.add(accountingBean);
        }
        return applyPayments;
    }

    public AccountingBean getAppliedArTransaction(PaymentBean paymentBean) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name,tp.acct_no,trim(tr.Customer_Reference_No),tr.Charge_Code,");
        queryBuilder.append("if(tr.Bill_Ladding_No!='',tr.Bill_Ladding_No,tr.Invoice_number) as invoiceOrBl,");
        queryBuilder.append("tr.Voyage_No,tr.quotation_no,format(tr.Transaction_amt,2),");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y'),tr.Transaction_type,");
        queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('AR_INVOICE' as char character set latin1) as note_module_id,");
        queryBuilder.append("concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number)) as note_ref_id,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments,");
        queryBuilder.append("cast(if(red.id <> 0, red.id, '') as char character set latin1) as arInvoiceId,");
        queryBuilder.append("tr.Balance_In_Process");
        queryBuilder.append(" from transaction tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" left join notes note on (note.module_id='AR_INVOICE'");
        queryBuilder.append(" and note.module_ref_id=concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number))");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc on (doc.screen_name='INVOICE' and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number)))");
        queryBuilder.append("  left join ar_red_invoice red");
        queryBuilder.append("    on (");
        queryBuilder.append("      tr.invoice_number = red.invoice_number");
        queryBuilder.append("      and tr.cust_no = red.customer_number");
        queryBuilder.append("    )");
        queryBuilder.append(" where tr.transaction_id = ").append(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, ""));
        queryBuilder.append(" group by tr.transaction_id");
        Object result = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            AccountingBean accountingBean = new AccountingBean();
            Object[] col = (Object[]) result;
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setQuoteNumber((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedDate((String) col[8]);
            accountingBean.setTransactionType((String) col[9]);
            accountingBean.setTransactionId((String) col[10]);
            accountingBean.setSubType((String) col[11]);
            accountingBean.setNoteModuleId((String) col[12]);
            accountingBean.setNoteRefId((String) col[13]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[14]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[15]));
            accountingBean.setArInvoiceId((String) col[16]);
            double balanceInProcess = Double.parseDouble(col[17].toString());
            double paidAmount = Double.parseDouble(paymentBean.getPaidAmount());
            double adjustAmount = CommonUtils.isEmpty(paymentBean.getAdjustAmount()) ? 0d : Double.parseDouble(paymentBean.getAdjustAmount());
            accountingBean.setBalanceInProcess(balanceInProcess);
            accountingBean.setFormattedBalanceInProcess(NumberUtils.formatNumber(balanceInProcess, "###,###,##0.00"));
            if (CommonUtils.isEmpty(balanceInProcess) && CommonUtils.isNotEmpty(paidAmount) && CommonUtils.isEmpty(adjustAmount)) {
                accountingBean.setSelected(true);
            }
            accountingBean.setPaidAmount(paymentBean.getPaidAmount());
            accountingBean.setAdjustAmount(paymentBean.getAdjustAmount());
            accountingBean.setGlAccount(paymentBean.getGlAccount());
            return accountingBean;
        }
        return null;
    }

    public AccountingBean getAppliedApTransaction(PaymentBean paymentBean) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name,tp.acct_no,tr.Customer_Reference_No,tr.Charge_Code,");
        queryBuilder.append("tr.Invoice_number as invoiceOrBl,tr.Voyage_No,");
        queryBuilder.append("tr.Bill_Ladding_No,format(-tr.Transaction_amt,2),date_format(tr.Transaction_date,'%m/%d/%Y'),tr.Transaction_type,");
        queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('AP_INVOICE' as char character set latin1) as note_module_id,");
        queryBuilder.append("concat(tp.acct_no,'-',tr.invoice_number) as note_ref_id,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments");
        queryBuilder.append(" from transaction tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" left join notes note on (note.module_id='AP_INVOICE'");
        queryBuilder.append(" and note.module_ref_id=concat(tp.acct_no,'-',tr.invoice_number)");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc on (doc.screen_name='INVOICE' and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=concat(tp.acct_no,'-',tr.invoice_number))");
        queryBuilder.append(" where tr.transaction_id = ").append(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_PAYABLE, ""));
        Object result = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            AccountingBean accountingBean = new AccountingBean();
            Object[] col = (Object[]) result;
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setQuoteNumber((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedDate((String) col[8]);
            accountingBean.setTransactionType((String) col[9]);
            accountingBean.setTransactionId((String) col[10]);
            accountingBean.setSubType((String) col[11]);
            accountingBean.setNoteModuleId((String) col[12]);
            accountingBean.setNoteRefId((String) col[13]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[14]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[15]));
            accountingBean.setBalanceInProcess(0d);
            accountingBean.setFormattedBalanceInProcess("0.00");
            accountingBean.setSelected(true);
            accountingBean.setPaidAmount(paymentBean.getPaidAmount());
            return accountingBean;
        }
        return null;
    }

    public AccountingBean getAppliedAcTransaction(PaymentBean paymentBean) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name,tp.acct_no,trim(tr.Customer_Reference_No),tr.Charge_Code,");
        queryBuilder.append("trim(tr.Invoice_number) as invoiceOrBl,tr.Voyage_No,tr.Bill_Ladding_No,");
        queryBuilder.append("format(-tr.Transaction_amt,2),date_format(tr.Transaction_date,'%m/%d/%Y'),tr.Transaction_type,");
        queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,tr.Status,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('ACCRUALS' as char character set latin1) as note_module_id,");
        queryBuilder.append("cast(tr.transaction_id as char character set latin1) as note_ref_id,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments");
        queryBuilder.append(" from transaction_ledger tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" left join notes note on (note.module_id='ACCRUALS'");
        queryBuilder.append(" and note.module_ref_id=cast(tr.transaction_id as char character set latin1)");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc on (doc.screen_name='INVOICE' and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=concat(tp.acct_no,'-',trim(tr.Invoice_number)))");
        queryBuilder.append(" where tr.transaction_id = ").append(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCRUALS, ""));
        queryBuilder.append(" group by tr.transaction_id");
        Object result = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            AccountingBean accountingBean = new AccountingBean();
            Object[] col = (Object[]) result;
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setQuoteNumber((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedDate((String) col[8]);
            accountingBean.setTransactionType((String) col[9]);
            accountingBean.setTransactionId((String) col[10]);
            accountingBean.setStatus((String) col[11]);
            accountingBean.setSubType((String) col[12]);
            accountingBean.setNoteModuleId((String) col[13]);
            accountingBean.setNoteRefId((String) col[14]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[15]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[16]));
            accountingBean.setBalanceInProcess(0d);
            accountingBean.setFormattedBalanceInProcess("0.00");
            accountingBean.setSelected(true);
            accountingBean.setPaidAmount(paymentBean.getPaidAmount());
            return accountingBean;
        }
        return null;
    }

    public String buildArQuery(ArBatchForm arBatchForm) {
        StringBuilder queryBuilder = new StringBuilder(" from transaction tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" where tr.Transaction_type ='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        if (CommonUtils.isNotEmpty(arBatchForm.getArTransactionIds())) {
            queryBuilder.append(" and tr.transaction_id not in (").append(arBatchForm.getArTransactionIds()).append(")");
        }
        if ((CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isNotEmpty(arBatchForm.getSearchValue()))) {
            if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_DOCK_RECEIPT)) {
                if (arBatchForm.getSearchValue().trim().length() == 6 || arBatchForm.getSearchValue().trim().length() < 3) {
                    queryBuilder.append(" and tr.drcpt = '").append(arBatchForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" and tr.drcpt like '").append(arBatchForm.getSearchValue()).append("%'");
                } 
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_INVOICE_BL)) {
                queryBuilder.append(" and (tr.Invoice_number like '%").append(arBatchForm.getSearchValue()).append("%'");
                queryBuilder.append(" or tr.Bill_Ladding_No like '%").append(arBatchForm.getSearchValue()).append("%')");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_VOYAGE)) {
                queryBuilder.append(" and (tr.Voyage_No like '%").append(arBatchForm.getSearchValue()).append("%'");
                String drcpts = new ArInquiryDAO().getDocReceiptsByVoyage(arBatchForm.getSearchValue().trim(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                if (CommonUtils.isNotEmpty(drcpts)) {
                    queryBuilder.append(" or tr.drcpt in ").append(drcpts);
                }
                queryBuilder.append(")");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CONTAINER)) {
                queryBuilder.append(" and tr.Container_No like '%").append(arBatchForm.getSearchValue()).append("%'");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_COST_CODE)) {
                queryBuilder.append(" and tr.Charge_Code like '%").append(arBatchForm.getSearchValue()).append("%'");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)) {
                String ids = getArInPayments(arBatchForm);
                if (CommonUtils.isNotEmpty(ids)) {
                    queryBuilder.append(" and tr.transaction_id in ").append(ids);
                } else {
                    queryBuilder.append(" and tr.Transaction_type ='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
                }
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_AMOUNT)) {
                String ids = getArInPayments(arBatchForm);
                if (CommonUtils.isNotEmpty(ids)) {
                    queryBuilder.append(" and tr.transaction_id in ").append(ids);
                } else {
                    queryBuilder.append(" and tr.Transaction_type ='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
                }
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_INVOICE_BL_DR_AMOUNT)) {
                queryBuilder.append(" and tr.Transaction_amt = '").append(arBatchForm.getSearchValue().replace(",", "")).append("'");
            }
        } else {
            String customerNumber = arBatchForm.getCustomerNumber();
            if (arBatchForm.isOtherCustomer()) {
                customerNumber = arBatchForm.getOtherCustomerNumber();
            }
            if (arBatchForm.isShowAssociatedCompanies()) {
                queryBuilder.append(" and (tp.acct_no='").append(customerNumber).append("'");
                queryBuilder.append(" or tp.masteracct_no='").append(customerNumber).append("')");
            } else {
                queryBuilder.append(" and tp.acct_no='").append(customerNumber).append("'");
            }
        }
        if (arBatchForm.isShowZeroBalanceReceivables()) {
            queryBuilder.append(" and tr.Balance=0 and tr.Balance_In_Process=0");
        } else {
            queryBuilder.append(" and tr.Balance<>0 and tr.Balance_In_Process<>0");
        }
        return queryBuilder.toString();
    }

    private String getArInPayments(ArBatchForm arBatchForm) {
        StringBuilder queryBuilder = new StringBuilder("select distinct tr.Transaction_ID from transaction tr");
        queryBuilder.append(" join trading_partner tp on tp.acct_no=tr.cust_no");
        if (CommonUtils.isNotEmpty(arBatchForm.getSearchBy())
                && CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)) {
            queryBuilder.append(" join payments py on (py.transaction_id=tr.Transaction_ID");
            queryBuilder.append(" and py.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("')");
            queryBuilder.append(" join ar_batch ab on ab.batch_id=py.batch_id");
            queryBuilder.append(" join payment_checks pc on (pc.Id=py.Payment_Check_Id");
            queryBuilder.append(" and pc.Check_no='").append(arBatchForm.getSearchValue()).append("')");
        } else {
            queryBuilder.append(" join payments py on (py.transaction_id=tr.Transaction_ID");
            queryBuilder.append(" and py.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("')");
            queryBuilder.append(" join ar_batch ab on ab.batch_id=py.batch_id");
            queryBuilder.append(" join payment_checks pc on (pc.Id=py.Payment_Check_Id");
            queryBuilder.append(" and pc.Received_Amt=").append(arBatchForm.getSearchValue().replace(",", "")).append(")");
        }
        queryBuilder.append(" where tr.Transaction_type ='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (null != result && !result.isEmpty()) {
            return result.toString().replace("[", "(").replace("]", ")");
        }
        return null;
    }

    public String buildApQuery(ArBatchForm arBatchForm) {
        StringBuilder queryBuilder = new StringBuilder(" from transaction tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" where tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        if (CommonUtils.isNotEmpty(arBatchForm.getApTransactionIds())) {
            queryBuilder.append(" and tr.transaction_id not in (").append(arBatchForm.getApTransactionIds()).append(")");
        }
        if ((CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isNotEmpty(arBatchForm.getSearchValue()))) {
            if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_DOCK_RECEIPT)) {
                String invoiceNumbers = new ApInquiryDAO().getAcInvoiceNumbers("drcpt", arBatchForm.getSearchValue(), false);
                if (CommonUtils.isNotEmpty(invoiceNumbers)) {
                    queryBuilder.append(" and tr.invoice_number in (").append(invoiceNumbers).append(")");
                } else {
                    queryBuilder.append(" and tr.drcpt = '").append(arBatchForm.getSearchValue()).append("'");
                }
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_INVOICE_BL)) {
                ApInquiryDAO apInquiryDAO = new ApInquiryDAO();
                String invoiceNumbers = "";
                String invoiceNumbers1 = apInquiryDAO.getAcInvoiceNumbers("invoice_number", arBatchForm.getSearchValue(), false);
                String invoiceNumbers2 = apInquiryDAO.getAcInvoiceNumbers("bill_ladding_no", arBatchForm.getSearchValue(), false);
                if (CommonUtils.isNotEmpty(invoiceNumbers1)) {
                    invoiceNumbers += invoiceNumbers1;
                }
                if (CommonUtils.isNotEmpty(invoiceNumbers2)) {
                    invoiceNumbers += (CommonUtils.isNotEmpty(invoiceNumbers) ? "," : "") + invoiceNumbers2;
                }
                if (CommonUtils.isNotEmpty(invoiceNumbers)) {
                    queryBuilder.append(" and (tr.invoice_number in (").append(invoiceNumbers).append(")");
                    queryBuilder.append(" or tr.invoice_number like '%").append(arBatchForm.getSearchValue()).append("%')");
                } else {
                    queryBuilder.append(" and tr.invoice_number like '%").append(arBatchForm.getSearchValue()).append("%'");
                }
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_VOYAGE)) {
                String invoiceNumbers = new ApInquiryDAO().getAcInvoiceNumbers("voyage_no", arBatchForm.getSearchValue(), false);
                if (CommonUtils.isNotEmpty(invoiceNumbers)) {
                    queryBuilder.append(" and tr.invoice_number in (").append(invoiceNumbers).append(")");
                } else {
                    return null;
                }
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CONTAINER)) {
                queryBuilder.append(" and tr.Container_No like '%").append(arBatchForm.getSearchValue()).append("%'");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_COST_CODE)) {
                queryBuilder.append(" and tr.Charge_Code like '%").append(arBatchForm.getSearchValue()).append("%'");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_INVOICE_BL_DR_AMOUNT)) {
                queryBuilder.append(" and tr.Transaction_amt = '").append(arBatchForm.getSearchValue().replace(",", "")).append("'");
            }
        } else {
            String customerNumber = arBatchForm.getCustomerNumber();
            if (arBatchForm.isOtherCustomer()) {
                customerNumber = arBatchForm.getOtherCustomerNumber();
            }
            if (arBatchForm.isShowAssociatedCompanies()) {
                queryBuilder.append(" and (tp.acct_no='").append(customerNumber).append("'");
                queryBuilder.append(" or tp.masteracct_no='").append(customerNumber).append("')");
            } else {
                queryBuilder.append(" and tp.acct_no='").append(customerNumber).append("'");
            }
        }
        queryBuilder.append(" and (tr.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tr.Status='").append(STATUS_READY_TO_PAY).append("')");
        queryBuilder.append(" and tr.Balance<>0 and tr.Balance_In_Process<>0");
        return queryBuilder.toString();
    }

    public String buildAcQuery(ArBatchForm arBatchForm) {
        StringBuilder queryBuilder = new StringBuilder(" from transaction_ledger tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" where tr.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        if (CommonUtils.isNotEmpty(arBatchForm.getAcTransactionIds())) {
            queryBuilder.append(" and tr.transaction_id not in (").append(arBatchForm.getAcTransactionIds()).append(")");
        }
        if ((CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isNotEmpty(arBatchForm.getSearchValue()))) {
            if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_DOCK_RECEIPT)) {
                if (arBatchForm.getSearchValue().trim().length() == 6 || arBatchForm.getSearchValue().trim().length() < 3) {
                    queryBuilder.append(" and tr.drcpt = '").append(arBatchForm.getSearchValue()).append("'");
                } else {
                    queryBuilder.append(" and tr.drcpt like '").append(arBatchForm.getSearchValue()).append("%'");
                } 
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_INVOICE_BL)) {
                queryBuilder.append(" and (tr.Invoice_number like '%").append(arBatchForm.getSearchValue()).append("%'");
                queryBuilder.append(" or tr.Bill_Ladding_No like '%").append(arBatchForm.getSearchValue()).append("%')");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_VOYAGE)) {
                queryBuilder.append(" and (tr.Voyage_No like '%").append(arBatchForm.getSearchValue()).append("%'");
                String drcpts = new AccountingLedgerDAO().getDocReceiptsForVoyage(arBatchForm.getSearchValue().trim());
                if (CommonUtils.isNotEmpty(drcpts)) {
                    queryBuilder.append(" or tr.drcpt in ").append(drcpts);
                }
                queryBuilder.append(")");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CONTAINER)) {
                queryBuilder.append(" and tr.Container_No like '%").append(arBatchForm.getSearchValue()).append("%'");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_COST_CODE)) {
                queryBuilder.append(" and tr.Charge_Code like '%").append(arBatchForm.getSearchValue()).append("%'");
            } else if (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_INVOICE_BL_DR_AMOUNT)) {
                queryBuilder.append(" and tr.Transaction_amt = '").append(arBatchForm.getSearchValue().replace(",", "")).append("'");
            }
        } else {
            String customerNumber = arBatchForm.getCustomerNumber();
            if (arBatchForm.isOtherCustomer()) {
                customerNumber = arBatchForm.getOtherCustomerNumber();
            }
            if (arBatchForm.isShowAssociatedCompanies()) {
                queryBuilder.append(" and (tp.acct_no='").append(customerNumber).append("'");
                queryBuilder.append(" or tp.masteracct_no='").append(customerNumber).append("')");
            } else {
                queryBuilder.append(" and tp.acct_no='").append(customerNumber).append("'");
            }
        }
        if (arBatchForm.isShowAccruals() || arBatchForm.isShowAssignedAccruals() || arBatchForm.isShowInactiveAccruals()) {
            queryBuilder.append(" and (");
            String addOr = "";
            if (arBatchForm.isShowAccruals()) {
                queryBuilder.append(" ((tr.Status='").append(STATUS_OPEN).append("' or tr.Status='").append(STATUS_DISPUTE).append("')");
                queryBuilder.append(" and tr.Balance<>0 and tr.Balance_In_Process<>0)");
                addOr = " or ";
            }
            if (arBatchForm.isShowAssignedAccruals()) {
                queryBuilder.append(addOr).append(" ((tr.Status='").append(STATUS_ASSIGN).append("'");
                queryBuilder.append(" or tr.Status='").append(STATUS_EDI_ASSIGNED).append("')");
                queryBuilder.append(" and tr.Balance=0 and tr.Balance_In_Process=0)");
                addOr = " or ";
            }
            if (arBatchForm.isShowInactiveAccruals()) {
                queryBuilder.append(addOr).append(" (tr.Status='").append(STATUS_INACTIVE).append("' and tr.Balance<>0 and tr.Balance_In_Process<>0)");
            }
            queryBuilder.append(")");
        } else {
            queryBuilder.append(" and (tr.Status='").append(STATUS_OPEN).append("' or tr.Status='").append(STATUS_DISPUTE).append("')");
            queryBuilder.append(" and tr.Balance<>0 and tr.Balance_In_Process<>0");
        }
        return queryBuilder.toString();
    }

    public Double getPaymentAmountFromPayments(Integer batchId) {
        String query = "select sum(payment_amt) from payments where batch_id=" + batchId;
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public Double getCheckAmountFromChecks(Integer batchId) {
        String query = "select sum(Received_Amt) from payment_checks where batch_id=" + batchId;
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public String getUnBalancedChecks(Integer batchId) {
        String query = "select distinct(check_no) from payment_checks where Received_Amt-Applied_Amount!=0 and batch_id=" + batchId;
        getCurrentSession().flush();
        List result = getCurrentSession().createSQLQuery(query).list();
        if (null != result && !result.isEmpty()) {
            return result.toString().replace("[", "").replace("]", "");
        }
        return null;
    }

    public Integer getNoOfPayments(Integer batchId) {
        String query = "select count(id) from payments where batch_id=" + batchId;
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null == result ? 0 : Integer.parseInt(result.toString());
    }

    public List<PaymentCheckBean> getPaymentChecks(Integer batchId) {
        List<PaymentCheckBean> paymentChecks = new ArrayList<PaymentCheckBean>();
        StringBuilder queryBuilder = new StringBuilder("select pc.id,pc.check_no,tp.acct_no,tp.acct_name,");
        queryBuilder.append("format(pc.received_amt,2) as check_amount,format(pc.applied_amount,2) as applied_amount,");
        queryBuilder.append("if(pc.received_amt-pc.applied_amount<>0,'true','false') as out_of_balance,");
        queryBuilder.append("count(p.id) as no_of_invoices,if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType");
        queryBuilder.append(" from payment_checks pc join trading_partner tp on tp.acct_no=pc.cust_id");
        queryBuilder.append(" left join payments p on pc.id = p.payment_check_id");
        queryBuilder.append(" where pc.batch_id=").append(batchId);
        queryBuilder.append(" group by pc.id");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            PaymentCheckBean paymentCheck = new PaymentCheckBean();
            paymentCheck.setCheckId(col[0].toString());
            paymentCheck.setCheckNumber((String) col[1]);
            paymentCheck.setCustomerNumber((String) col[2]);
            paymentCheck.setCustomerName((String) col[3]);
            paymentCheck.setCheckAmount((String) col[4]);
            paymentCheck.setAppliedAmount((String) col[5]);
            paymentCheck.setOutOfBalance(Boolean.valueOf((String) col[6]));
            paymentCheck.setNoOfInvoices(Integer.parseInt(col[7].toString()));
            paymentCheck.setSubType((String) col[8]);
            paymentChecks.add(paymentCheck);
        }
        return paymentChecks;
    }

    public PaymentCheckBean getPaymentCheck(Integer id) {
        StringBuilder queryBuilder = new StringBuilder("select pc.Id,pc.check_no,tp.acct_no,tp.acct_name,");
        queryBuilder.append("format(pc.Received_Amt,2),format(pc.applied_amount,2),format(pc.Received_Amt-pc.applied_amount,2),");
        queryBuilder.append("count(p.id) as no_of_invoices from payment_checks pc");
        queryBuilder.append(" join trading_partner tp on tp.acct_no=pc.Cust_id left join payments p on pc.id = p.payment_check_id");
        queryBuilder.append(" where pc.Id=").append(id);
        queryBuilder.append(" group by pc.id");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        Object[] col = (Object[]) result;
        PaymentCheckBean paymentCheck = new PaymentCheckBean();
        paymentCheck.setCheckId(col[0].toString());
        paymentCheck.setCheckNumber((String) col[1]);
        paymentCheck.setCustomerNumber((String) col[2]);
        paymentCheck.setCustomerName((String) col[3]);
        paymentCheck.setCheckAmount((String) col[4]);
        paymentCheck.setAppliedAmount((String) col[5]);
        paymentCheck.setCheckBalance((String) col[6]);
        paymentCheck.setNoOfInvoices(Integer.parseInt(col[7].toString()));
        return paymentCheck;
    }

    public List<AccountingBean> getAppliedArTransactions(String paymentCheckId, String batchId) {
        List<AccountingBean> appliedArTransactions = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tr.customerName as customerName,");
        queryBuilder.append("tr.customerNumber as customerNumber,");
        queryBuilder.append("tr.customerReference as customerReference,");
        queryBuilder.append("tr.chargeCode as chargeCode,");
        queryBuilder.append("tr.invoiceOrBl as invoiceOrBl,");
        queryBuilder.append("tr.voyage as voyage,");
        queryBuilder.append("tr.quoteNumber as quoteNumber,");
        queryBuilder.append("tr.formattedAmount as formattedAmount,");
        queryBuilder.append("tr.formattedBalanceInProcess as formattedBalanceInProcess,");
        queryBuilder.append("tr.balanceInProcess as balanceInProcess,");
        queryBuilder.append("tr.formattedDate as formattedDate,");
        queryBuilder.append("tr.transactionType as transactionType,");
        queryBuilder.append("tr.transactionId as transactionId,");
        queryBuilder.append("tr.paymentAmount as paymentAmount,");
        queryBuilder.append("tr.adjustmentAmount as adjustmentAmount,");
        queryBuilder.append("tr.glAccount as glAccount,");
        queryBuilder.append("tr.subType as subType,");
        queryBuilder.append("tr.note_module_id as noteModuleId,");
        queryBuilder.append("tr.note_ref_id as noteRefId,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments,");
        queryBuilder.append("cast(if(red.id <> 0, red.id, '') as char character set latin1) as arInvoiceId");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("trim(tr.customer_reference_no) as customerReference,");
        queryBuilder.append("tr.charge_code as chargeCode,");
        queryBuilder.append("if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number) as invoiceOrBl,");
        queryBuilder.append("tr.voyage_no as voyage,");
        queryBuilder.append("tr.quotation_no as quoteNumber,");
        queryBuilder.append("format(tr.transaction_amt,2) as formattedAmount,");
        queryBuilder.append("format(tr.balance_in_process,2) as formattedBalanceInProcess,");
        queryBuilder.append("tr.balance_in_process as balanceInProcess,");
        queryBuilder.append("date_format(tr.transaction_date,'%m/%d/%Y') as formattedDate,");
        queryBuilder.append("tr.transaction_type as transactionType,");
        queryBuilder.append("concat(tr.transaction_id,tr.transaction_type) as transactionId,");
        queryBuilder.append("format(p.payment_amt,2) as paymentAmount,");
        queryBuilder.append("format(p.adjustment_amt,2) as adjustmentAmount,");
        queryBuilder.append("p.charge_code as glAccount,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('AR_INVOICE' as char character set latin1) as note_module_id,");
        queryBuilder.append("concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number)) as note_ref_id,");
        queryBuilder.append("concat(tp.acct_no,'-',if(tr.bill_ladding_no!='',tr.bill_ladding_no,tr.invoice_number)) as document_id,");
        queryBuilder.append("tr.invoice_number");
        queryBuilder.append(" from payments p");
        queryBuilder.append(" join transaction tr");
        queryBuilder.append(" on (p.transaction_id=tr.transaction_id");
        queryBuilder.append(" and tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("')");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no=tr.cust_no)");
        queryBuilder.append(" where p.payment_check_id=").append(paymentCheckId);
        queryBuilder.append(" and p.batch_id=").append(batchId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(") as tr");
        queryBuilder.append("  left join ar_red_invoice red");
        queryBuilder.append("    on (");
        queryBuilder.append("      tr.invoice_number = red.invoice_number");
        queryBuilder.append("      and tr.customerNumber = red.customer_number");
        queryBuilder.append("    )");
        queryBuilder.append(" left join notes note");
        queryBuilder.append(" on (note.module_id=tr.note_module_id");
        queryBuilder.append(" and note.module_ref_id=tr.note_ref_id");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name='INVOICE'");
        queryBuilder.append(" and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=tr.document_id)");
        queryBuilder.append(" group by tr.transactionId");
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean accountingBean = new AccountingBean();
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setQuoteNumber((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedBalanceInProcess((String) col[8]);
            double balanceInProcess = Double.parseDouble(col[9].toString());
            accountingBean.setBalanceInProcess(balanceInProcess);
            accountingBean.setFormattedDate((String) col[10]);
            accountingBean.setTransactionType((String) col[11]);
            accountingBean.setTransactionId((String) col[12]);
            accountingBean.setPaidAmount(col[13].toString().replace(",", ""));
            accountingBean.setAdjustAmount(col[14].toString().replace(",", ""));
            accountingBean.setGlAccount((String) col[15]);
            accountingBean.setSubType((String) col[16]);
            accountingBean.setNoteModuleId((String) col[17]);
            accountingBean.setNoteRefId((String) col[18]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[19]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[20]));
            accountingBean.setArInvoiceId((String) col[21]);
            double paidAmount = Double.parseDouble(accountingBean.getPaidAmount());
            double adjustAmount = CommonUtils.isEmpty(accountingBean.getAdjustAmount()) ? 0d : Double.parseDouble(accountingBean.getAdjustAmount());
            if (CommonUtils.isEmpty(balanceInProcess) && CommonUtils.isNotEmpty(paidAmount) && CommonUtils.isEmpty(adjustAmount)) {
                accountingBean.setSelected(true);
            }
            appliedArTransactions.add(accountingBean);
        }
        return appliedArTransactions;
    }

    public List<AccountingBean> getAppliedApTransactions(String paymentCheckId, String batchId) {
        List<AccountingBean> appliedApTransactions = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tr.customerName as customerName,");
        queryBuilder.append("tr.customerNumber as customerNumber,");
        queryBuilder.append("tr.customerReference as customerReference,");
        queryBuilder.append("tr.chargeCode as chargeCode,");
        queryBuilder.append("tr.invoiceOrBl as invoiceOrBl,");
        queryBuilder.append("tr.voyage as voyage,");
        queryBuilder.append("tr.quoteNumber as quoteNumber,");
        queryBuilder.append("tr.formattedAmount as formattedAmount,");
        queryBuilder.append("tr.formattedDate as formattedDate,");
        queryBuilder.append("tr.transactionType as transactionType,");
        queryBuilder.append("tr.transactionId as transactionId,");
        queryBuilder.append("tr.paymentAmount as paymentAmount,");
        queryBuilder.append("tr.subType as subType,");
        queryBuilder.append("tr.note_module_id as noteModuleId,");
        queryBuilder.append("tr.note_ref_id as noteRefId,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("trim(tr.customer_reference_no) as customerReference,");
        queryBuilder.append("tr.charge_code as chargeCode,");
        queryBuilder.append("tr.invoice_number as invoiceOrBl,");
        queryBuilder.append("tr.voyage_no as voyage,");
        queryBuilder.append("tr.bill_ladding_no as quoteNumber,");
        queryBuilder.append("format(-tr.transaction_amt,2) as formattedAmount,");
        queryBuilder.append("date_format(tr.transaction_date,'%m/%d/%Y') as formattedDate,");
        queryBuilder.append("tr.transaction_type as transactionType,");
        queryBuilder.append("concat(tr.transaction_id,tr.transaction_type) as transactionId,");
        queryBuilder.append("format(p.payment_amt,2) as paymentAmount,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('AP_INVOICE' as char character set latin1) as note_module_id,");
        queryBuilder.append("concat(tp.acct_no,'-',tr.invoice_number) as note_ref_id,");
        queryBuilder.append("concat(tp.acct_no,'-',tr.invoice_number) as document_id");
        queryBuilder.append(" from payments p");
        queryBuilder.append(" join transaction tr");
        queryBuilder.append(" on (p.transaction_id=tr.transaction_id");
        queryBuilder.append(" and tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("')");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no=tr.cust_no)");
        queryBuilder.append(" where p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.batch_id=").append(batchId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(") as tr");
        queryBuilder.append(" left join notes note");
        queryBuilder.append(" on (note.module_id=tr.note_module_id");
        queryBuilder.append(" and note.module_ref_id=tr.note_ref_id");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name='INVOICE'");
        queryBuilder.append(" and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=tr.document_id)");
        queryBuilder.append(" group by tr.transactionId");
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean accountingBean = new AccountingBean();
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setQuoteNumber((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedBalanceInProcess("0.00");
            accountingBean.setBalanceInProcess(0d);
            accountingBean.setFormattedDate((String) col[8]);
            accountingBean.setTransactionType((String) col[9]);
            accountingBean.setTransactionId((String) col[10]);
            accountingBean.setPaidAmount(col[11].toString().replace(",", ""));
            accountingBean.setSubType((String) col[12]);
            accountingBean.setNoteModuleId((String) col[13]);
            accountingBean.setNoteRefId((String) col[14]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[15]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[16]));
            accountingBean.setSelected(true);
            appliedApTransactions.add(accountingBean);
        }
        return appliedApTransactions;
    }

    public List<AccountingBean> getAppliedAcTransactions(String paymentCheckId, String batchId) {
        List<AccountingBean> appliedArTransactions = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tr.customerName as customerName,");
        queryBuilder.append("tr.customerNumber as customerNumber,");
        queryBuilder.append("tr.customerReference as customerReference,");
        queryBuilder.append("tr.chargeCode as chargeCode,");
        queryBuilder.append("tr.invoiceOrBl as invoiceOrBl,");
        queryBuilder.append("tr.voyage as voyage,");
        queryBuilder.append("tr.quoteNumber as quoteNumber,");
        queryBuilder.append("tr.formattedAmount as formattedAmount,");
        queryBuilder.append("tr.status as status,");
        queryBuilder.append("tr.formattedDate as formattedDate,");
        queryBuilder.append("tr.transactionType as transactionType,");
        queryBuilder.append("tr.transactionId as transactionId,");
        queryBuilder.append("tr.paymentAmount as paymentAmount,");
        queryBuilder.append("tr.subType as subType,");
        queryBuilder.append("tr.note_module_id as noteModuleId,");
        queryBuilder.append("tr.note_ref_id as noteRefId,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("trim(tr.customer_reference_no) customerReference,");
        queryBuilder.append("tr.charge_code as chargeCode,");
        queryBuilder.append("trim(tr.Invoice_number) as invoiceOrBl,");
        queryBuilder.append("tr.voyage_no as voyage,");
        queryBuilder.append("tr.bill_ladding_no as quoteNumber,");
        queryBuilder.append("format(-tr.transaction_amt,2) as formattedAmount,");
        queryBuilder.append("tr.status as status,");
        queryBuilder.append("date_format(tr.transaction_date,'%m/%d/%Y') as formattedDate,");
        queryBuilder.append("tr.transaction_type as transactionType,");
        queryBuilder.append("concat(tr.transaction_id,tr.transaction_type) as transactionId,");
        queryBuilder.append("format(p.payment_amt,2) as paymentAmount,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
        queryBuilder.append("cast('ACCRUALS' as char character set latin1) as note_module_id,");
        queryBuilder.append("cast(tr.transaction_id as char character set latin1) as note_ref_id,");
        queryBuilder.append("concat(tp.acct_no,'-',trim(tr.Invoice_number)) as document_id");
        queryBuilder.append(" from payments p");
        queryBuilder.append(" join transaction_ledger tr");
        queryBuilder.append(" on p.transaction_id=tr.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (tp.acct_no=tr.cust_no)");
        queryBuilder.append(" where p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.batch_id=").append(batchId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(") as tr");
        queryBuilder.append(" left join notes note");
        queryBuilder.append(" on (note.module_id=tr.note_module_id");
        queryBuilder.append(" and note.module_ref_id=tr.note_ref_id");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name='INVOICE'");
        queryBuilder.append(" and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=tr.document_id)");
        queryBuilder.append(" group by tr.transactionId");
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean accountingBean = new AccountingBean();
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setQuoteNumber((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedBalanceInProcess("0.00");
            accountingBean.setBalanceInProcess(0d);
            accountingBean.setStatus((String) col[8]);
            accountingBean.setFormattedDate((String) col[9]);
            accountingBean.setTransactionType((String) col[10]);
            accountingBean.setTransactionId((String) col[11]);
            accountingBean.setPaidAmount(col[12].toString().replace(",", ""));
            accountingBean.setSubType((String) col[13]);
            accountingBean.setNoteModuleId((String) col[14]);
            accountingBean.setNoteRefId((String) col[15]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[16]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[17]));
            accountingBean.setSelected(true);
            appliedArTransactions.add(accountingBean);
        }
        return appliedArTransactions;
    }

    public PaymentBean getAppliedOnAccount(String paymentCheckId, String batchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(payment_amt,2) from payments");
        queryBuilder.append(" where invoice_no='").append(AccountingConstants.ON_ACCOUNT).append("'");
        queryBuilder.append(" and payment_type='Check' and Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and batch_id=").append(batchId);
        Object result = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            PaymentBean paymentBean = new PaymentBean();
            paymentBean.setPaidAmount(result.toString().replace(",", ""));
            return paymentBean;
        }
        return null;
    }

    public List<PaymentBean> getAppliedPrepayments(String paymentCheckId, String batchId) {
        List<PaymentBean> appliedPrepayments = new ArrayList<PaymentBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select Bill_Ladding_No,payment_amt,notes from payments");
        queryBuilder.append(" where invoice_no='").append(AccountingConstants.PRE_PAYMENT).append("'");
        queryBuilder.append(" and payment_type='Check' and Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and batch_id=").append(batchId);
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            PaymentBean paymentBean = new PaymentBean();
            paymentBean.setDocReceipt((String) col[0]);
            paymentBean.setPaidAmount(col[1].toString().replace(",", ""));
            paymentBean.setNotes((String) col[2]);
            appliedPrepayments.add(paymentBean);
        }
        return appliedPrepayments;
    }

    public List<PaymentBean> getAppliedGlAccounts(String paymentCheckId, String batchId) {
        List<PaymentBean> appliedGLAccounts = new ArrayList<PaymentBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select charge_code,payment_amt,notes from payments");
        queryBuilder.append(" where invoice_no='").append(AccountingConstants.CHARGE_CODE).append("'");
        queryBuilder.append(" and payment_type='Check' and Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and batch_id=").append(batchId);
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            PaymentBean paymentBean = new PaymentBean();
            paymentBean.setGlAccount((String) col[0]);
            paymentBean.setPaidAmount(col[1].toString().replace(",", ""));
            paymentBean.setNotes((String) col[2]);
            appliedGLAccounts.add(paymentBean);
        }
        return appliedGLAccounts;
    }

    public void removePayments(String batchId, String paymentCheckId, Integer userId) {
        getCurrentSession().flush();
        //Updating the status of selected accruals in OPS
        StringBuilder queryBuilder = new StringBuilder("update transaction_ledger tl,fcl_bl_costcodes cost,payments p");
        queryBuilder.append(" set cost.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tl.Status='").append(STATUS_PENDING).append("'");
        queryBuilder.append(" and cost.code_id=tl.cost_id");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        //Updating the balance of selected accruals
        queryBuilder = new StringBuilder("update transaction_ledger tl,payments p");
        queryBuilder.append(" set tl.Status='").append(STATUS_OPEN).append("',tl.Balance_In_Process=tl.Balance,");
        queryBuilder.append(" tl.Updated_By=").append(userId).append(",tl.Updated_On=sysdate()");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tl.Status='").append(STATUS_PENDING).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        //Updating the balance of selected payables
        queryBuilder = new StringBuilder("update transaction tr,payments p");
        queryBuilder.append(" set tr.Status='").append(STATUS_OPEN).append("',tr.Balance_In_Process=tr.Balance,");
        queryBuilder.append(" tr.Updated_By=").append(userId).append(",tr.Updated_On=sysdate()");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        //Updating the balance of selected receivables
        queryBuilder = new StringBuilder("update transaction tr,payments p");
        queryBuilder.append(" set tr.Status='").append(STATUS_OPEN).append("',tr.Balance_In_Process=tr.Balance_In_Process+p.Payment_Amt+p.Adjustment_Amt,");
        queryBuilder.append(" tr.Updated_By=").append(userId).append(",tr.Updated_On=sysdate()");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        //Removing all the payments from the check
        queryBuilder = new StringBuilder("delete from payments where batch_id=").append(batchId);
        queryBuilder.append(" and Payment_Check_Id=").append(paymentCheckId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public List<String> getArItemsSavedInCheck(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(tr.transaction_id as char character set latin1)");
        queryBuilder.append(" from transaction tr,payments p");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<String> getApItemsSavedInCheck(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(tr.transaction_id as char character set latin1)");
        queryBuilder.append(" from transaction tr,payments p");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<String> getAcItemsSavedInCheck(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(tl.Transaction_Id as char character set latin1)");
        queryBuilder.append(" from transaction_ledger tl,payments p");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Transaction_Id=p.transaction_id");
        queryBuilder.append(" and tl.Status='").append(STATUS_PENDING).append("'");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public AccountingBean getNewlyAddedAcTransaction(String tranasctionId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name,tp.acct_no,trim(tr.Customer_Reference_No),tr.Charge_Code,");
        queryBuilder.append("trim(tr.Invoice_number) as invoiceOrBl,");
        queryBuilder.append("tr.Voyage_No,tr.drcpt,format(-tr.Transaction_amt,2),format(-tr.Balance_In_Process,2),-tr.Balance_In_Process,");
        queryBuilder.append("date_format(tr.Transaction_date,'%m/%d/%Y'),tr.Transaction_type,");
        queryBuilder.append("concat(tr.transaction_id,tr.Transaction_type) as transactionId,tr.Status,");
        queryBuilder.append("cast('ACCRUALS' as char character set latin1) as note_module_id,");
        queryBuilder.append("cast(tr.transaction_id as char character set latin1) as note_ref_id,");
        queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes,");
        queryBuilder.append("if(count(doc.document_id)>0,'true','false') as hasDocuments");
        queryBuilder.append(" from transaction_ledger tr join trading_partner tp on tp.acct_no=tr.cust_no");
        queryBuilder.append(" left join notes note on (note.module_id='ACCRUALS'");
        queryBuilder.append(" and note.module_ref_id=tr.transaction_id");
        queryBuilder.append(" and note.note_type='Manual')");
        queryBuilder.append(" left join document_store_log doc on (doc.screen_name='INVOICE' and doc.document_name='INVOICE'");
        queryBuilder.append(" and doc.document_id=concat(tp.acct_no,'-',trim(tr.Invoice_number)))");
        queryBuilder.append(" where tr.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tr.transaction_id=").append(tranasctionId);
        queryBuilder.append(" group by tr.transaction_id");
        Object result = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            Object[] col = (Object[]) result;
            AccountingBean accountingBean = new AccountingBean();
            accountingBean.setCustomerName((String) col[0]);
            accountingBean.setCustomerNumber((String) col[1]);
            accountingBean.setCustomerReference((String) col[2]);
            accountingBean.setChargeCode((String) col[3]);
            accountingBean.setInvoiceOrBl((String) col[4]);
            accountingBean.setVoyage((String) col[5]);
            accountingBean.setDockReceipt((String) col[6]);
            accountingBean.setFormattedAmount((String) col[7]);
            accountingBean.setFormattedBalanceInProcess((String) col[8]);
            accountingBean.setBalanceInProcess(Double.parseDouble(col[9].toString()));
            accountingBean.setFormattedDate((String) col[10]);
            accountingBean.setTransactionType((String) col[11]);
            accountingBean.setTransactionId(col[12].toString());
            accountingBean.setStatus((String) col[13]);
            accountingBean.setNoteModuleId((String) col[14]);
            accountingBean.setNoteRefId((String) col[15]);
            accountingBean.setManualNotes(Boolean.valueOf((String) col[16]));
            accountingBean.setHasDocuments(Boolean.valueOf((String) col[17]));
            return accountingBean;
        }
        return null;
    }

    public String getDocReceiptsForApVoyage(String voyageNo) {
        StringBuilder queryBuilder = new StringBuilder("select distinct(concat(\"'\",drcpt,\"'\")) from transaction");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and drcpt is not null");
        queryBuilder.append(" and voyage_no like '%").append(voyageNo).append("'");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "(").replace("]", ")");
        }
        return null;
    }

    public List<PaymentBean> getPaymentsForCheck(String batchId, String paymentCheckId) {
        List<PaymentBean> payments = new ArrayList<PaymentBean>();
        StringBuilder queryBuilder = new StringBuilder("");
        queryBuilder.append("select t.customerNumber,t.customerName,t.transactionType,t.invoiceOrBl,t.paidAmount,t.adjustAmount,t.glAccount from");
        queryBuilder.append(" ((").append(buildOnAccountPaymentsQuery(batchId, paymentCheckId)).append(")");
        queryBuilder.append(" union (").append(buildPrePaymentsQuery(batchId, paymentCheckId)).append(")");
        queryBuilder.append(" union (").append(buildChargeCodePaymentsQuery(batchId, paymentCheckId)).append(")");
        queryBuilder.append(" union (").append(buildArPaymentsQuery(batchId, paymentCheckId)).append(")");
        queryBuilder.append(" union (").append(buildApPaymentsQuery(batchId, paymentCheckId)).append(")");
        queryBuilder.append(" union (").append(buildAcPaymentsQuery(batchId, paymentCheckId)).append(")) as t");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            PaymentBean payment = new PaymentBean();
            payment.setCustomerNumber((String) col[0]);
            payment.setCustomerName((String) col[1]);
            payment.setTransactionType((String) col[2]);
            payment.setInvoiceOrBl((String) col[3]);
            payment.setPaidAmount((String) col[4]);
            payment.setAdjustAmount((String) col[5]);
            payment.setGlAccount((String) col[6]);
            payments.add(payment);
        }
        return payments;
    }

    public String buildOnAccountPaymentsQuery(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as customerNumber,tp.acct_name as customerName,'OA' as transactionType,");
        queryBuilder.append("p.invoice_no as invoiceOrBl,format(p.Payment_Amt,2) as paidAmount,'' as adjustAmount,");
        queryBuilder.append("p.Charge_Code as glAccount,p.id");
        queryBuilder.append(" from payments p join payment_checks pc on pc.Id=p.Payment_Check_Id");
        queryBuilder.append(" join trading_partner tp on pc.Cust_id=tp.acct_no");
        queryBuilder.append(" where p.Payment_Type='Check' and p.invoice_no='").append(AccountingConstants.ON_ACCOUNT).append("'");
        queryBuilder.append(" and p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        return queryBuilder.toString();
    }

    public String buildPrePaymentsQuery(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as customerNumber,tp.acct_name as customerName,'PP' as transactionType,");
        queryBuilder.append("p.Bill_Ladding_No as invoiceOrBl,format(p.Payment_Amt,2) as paidAmount,'' as adjustAmount,");
        queryBuilder.append("p.Charge_Code as glAccount,p.id");
        queryBuilder.append(" from payments p join payment_checks pc on pc.Id=p.Payment_Check_Id");
        queryBuilder.append(" join trading_partner tp on pc.Cust_id=tp.acct_no");
        queryBuilder.append(" where p.Payment_Type='Check' and p.invoice_no='").append(AccountingConstants.PRE_PAYMENT).append("'");
        queryBuilder.append(" and p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        return queryBuilder.toString();
    }

    public String buildChargeCodePaymentsQuery(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as customerNumber,tp.acct_name as customerName,'CC' as transactionType,");
        queryBuilder.append("p.invoice_no as invoiceOrBl,format(p.Payment_Amt,2) as paidAmount,'' as adjustAmount,");
        queryBuilder.append("p.Charge_Code as glAccount,p.id");
        queryBuilder.append(" from payments p join payment_checks pc on pc.Id=p.Payment_Check_Id");
        queryBuilder.append(" join trading_partner tp on pc.Cust_id=tp.acct_no");
        queryBuilder.append(" where p.Payment_Type='Check' and p.invoice_no='").append(AccountingConstants.CHARGE_CODE).append("'");
        queryBuilder.append(" and p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        return queryBuilder.toString();
    }

    public String buildArPaymentsQuery(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as customerNumber,tp.acct_name as customerName,tr.Transaction_type as transactionType,");
        queryBuilder.append("if(tr.Bill_Ladding_No!='',tr.Bill_Ladding_No,tr.Invoice_number) as invoiceOrBl,");
        queryBuilder.append("format(p.Payment_Amt,2) as paidAmount,format(p.Adjustment_Amt,2) as adjustAmount,");
        queryBuilder.append("p.Charge_Code as glAccount,p.id");
        queryBuilder.append(" from payments p join payment_checks pc on pc.Id=p.Payment_Check_Id");
        queryBuilder.append(" join transaction tr on tr.Transaction_ID=p.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" join trading_partner tp on tr.cust_no=tp.acct_no");
        queryBuilder.append(" where p.Payment_Type='P' and p.transaction_type= '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and p.batch_id=").append(batchId);
        queryBuilder.append(" and p.Payment_Check_Id=").append(paymentCheckId);
        return queryBuilder.toString();
    }

    public String buildApPaymentsQuery(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as customerNumber,tp.acct_name as customerName,tr.Transaction_type as transactionType,");
        queryBuilder.append("tr.Invoice_number as invoiceOrBl,");
        queryBuilder.append("format(p.Payment_Amt,2) as paidAmount,'' as adjustAmount,'' as glAccount,p.id");
        queryBuilder.append(" from payments p join payment_checks pc on pc.Id=p.Payment_Check_Id");
        queryBuilder.append(" join transaction tr on tr.Transaction_ID=p.transaction_id");
        queryBuilder.append(" and tr.Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" join trading_partner tp on tr.cust_no=tp.acct_no");
        queryBuilder.append(" where p.Payment_Type='P' and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and p.batch_id=").append(batchId);
        queryBuilder.append(" and p.Payment_Check_Id=").append(paymentCheckId);
        return queryBuilder.toString();
    }

    public String buildAcPaymentsQuery(String batchId, String paymentCheckId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as customerNumber,tp.acct_name as customerName,tl.Transaction_type as transactionType,");
        queryBuilder.append("if(tl.Invoice_number!='',tl.Invoice_number,tl.Bill_Ladding_No) as invoiceOrBl,");
        queryBuilder.append("format(p.Payment_Amt,2) as paidAmount,'' as adjustAmount,'' as glAccount,p.id");
        queryBuilder.append(" from payments p join payment_checks pc on pc.Id=p.Payment_Check_Id");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on tl.Transaction_ID=p.transaction_id");
        queryBuilder.append(" and tl.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" join trading_partner tp on tl.cust_no=tp.acct_no");
        queryBuilder.append(" where p.Payment_Type='P' and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and p.batch_id=").append(batchId);
        queryBuilder.append(" and p.Payment_Check_Id=").append(paymentCheckId);
        return queryBuilder.toString();
    }

    public String getBatchWhereInvoiceLocked(Integer batchId, String transactionId, String transactionType) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select distinct(ab.batch_id) from payments p,ar_batch ab where ab.Batch_id=p.Batch_Id");
        queryBuilder.append(" and ab.batch_id != ").append(batchId).append(" and ab.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and p.Payment_Type='P' and p.transaction_id='").append(transactionId).append("'");
        queryBuilder.append(" and p.transaction_type='").append(transactionType).append("'");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return null != result ? result.toString().replace("[", "").replace("]", "") : null;
    }

    public Object getCustomerRef(String arBatchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT tp.acct_name,tp.acct_no,");
        queryBuilder.append("CONCAT(IF(ca.address1!='',CONCAT('\n',ca.address1,'\n'),''),");
        queryBuilder.append("IF(ca.city1!='' && ca.state!='' && gen_country.codedesc!='',CONCAT(ca.city1,', ',ca.state,', ',gen_country.codedesc),");
        queryBuilder.append("IF(ca.city1!='' && ca.state!='',CONCAT(ca.city1,', ',ca.state),");
        queryBuilder.append("IF(ca.city1!='' && gen_country.codedesc!='',CONCAT(ca.city1,', ',gen_country.codedesc),");
        queryBuilder.append("IF(ca.city1!='',ca.city1,IF(ca.state!='',ca.state,IF(gen_country.codedesc!='',gen_country.codedesc,'')))))),");
        queryBuilder.append("IF(ca.ar_phone!='',CONCAT('\nPhone : ',ca.ar_phone),''),IF(ca.ar_fax!='',CONCAT('\nFax : ',ca.ar_fax),''),");
        queryBuilder.append("IF(ca.acct_rec_email!='',CONCAT('\nEmail : ',ca.acct_rec_email),'')) AS address,");
        queryBuilder.append("tr.Invoice_number,ab.Deposit_date,ab.Notes FROM ar_batch ab");
        queryBuilder.append(" JOIN TRANSACTION tr ON tr.invoice_number = CONCAT('NET SETT',ab.`Batch_id`) AND tr.`Transaction_type`='AR'");
        queryBuilder.append(" JOIN trading_partner tp ON tp.acct_no=tr.cust_no");
        queryBuilder.append(" LEFT JOIN cust_accounting ca ON ca.acct_no=tp.acct_no");
        queryBuilder.append(" LEFT JOIN genericcode_dup gen_country ON gen_country.id=ca.country");
        queryBuilder.append(" WHERE ab.Batch_id='").append(arBatchId).append("' limit 1");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List<Object[]> getReceivables(String arBatchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT invoice_or_bl,amount FROM ((SELECT p.Invoice_no AS invoice_or_bl,FORMAT(p.Payment_Amt,2) AS amount,p.Id FROM");
        queryBuilder.append(" payments p WHERE p.payment_type='Check' AND p.Invoice_no='ON ACCOUNT' AND p.Batch_Id='").append(arBatchId).append("')");
        queryBuilder.append(" UNION (SELECT TRIM(IF(p.Bill_Ladding_No!='',p.Bill_Ladding_No,p.Invoice_no)) AS invoice_or_bl,FORMAT(p.Payment_Amt,2)");
        queryBuilder.append(" AS amount,p.Id FROM payments p WHERE p.payment_type='Check' AND p.Invoice_no='PRE PAYMENT' AND");
        queryBuilder.append(" p.Batch_Id='").append(arBatchId).append("')");
        queryBuilder.append(" UNION (SELECT p.Invoice_no AS invoice_or_bl,FORMAT(p.Payment_Amt,2) AS amount,p.Id");
        queryBuilder.append(" FROM payments p WHERE p.payment_type='Check' AND p.Invoice_no='CHARGE CODE' AND");
        queryBuilder.append(" p.Batch_Id='").append(arBatchId).append("')");
        queryBuilder.append(" UNION (SELECT TRIM(IF(tr.Bill_Ladding_No!='',tr.Bill_Ladding_No,tr.Invoice_number)) AS invoice_or_bl,");
        queryBuilder.append(" FORMAT(p.Payment_Amt,2) AS amount,p.Id FROM payments p JOIN TRANSACTION tr ON tr.Transaction_type='AR' AND");
        queryBuilder.append(" tr.Transaction_ID=p.transaction_id AND p.transaction_type = 'AR' AND");
        queryBuilder.append(" p.payment_type='P' WHERE p.Batch_Id='").append(arBatchId).append("'").append(" )) AS t");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<Object[]> getPayables(String arBatchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT TRIM(t.invoice_or_bl),t.amount FROM ((SELECT tr.Invoice_number");
        queryBuilder.append(" AS invoice_or_bl,FORMAT(p.Payment_Amt,2) AS amount,p.id FROM payments p JOIN TRANSACTION tr ON");
        queryBuilder.append(" tr.Transaction_type='AP' AND tr.Transaction_ID=p.transaction_id AND p.transaction_type = 'AP'");
        queryBuilder.append(" WHERE p.Batch_Id='").append(arBatchId).append("')");
        queryBuilder.append(" UNION (SELECT tr.Invoice_number AS invoice_or_bl,");
        queryBuilder.append(" FORMAT(p.Payment_Amt,2) AS amount,p.id FROM payments p JOIN transaction_ledger tr ON tr.Transaction_type='AC' AND");
        queryBuilder.append(" tr.Transaction_ID=p.transaction_id AND p.transaction_type = 'AC'");
        queryBuilder.append(" WHERE p.Batch_Id='").append(arBatchId).append("'").append(" )) AS t");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public String validateGlAccountForAccruals(String arBatchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.transaction_id from transaction_ledger tl,payments p,payment_checks pc,ar_batch b");
        queryBuilder.append(" where tl.transaction_type='AC' and tl.transaction_id=p.transaction_id");
        queryBuilder.append(" and p.payment_type='P' and p.transaction_type = 'AC' and p.payment_check_id=pc.id and p.batch_id=b.batch_id");
        queryBuilder.append(" and pc.batch_id=b.batch_id and b.batch_id=").append(arBatchId);
        queryBuilder.append(" and tl.gl_account_number not in (select account from account_details)");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return (null != result && !result.isEmpty()) ? result.toString().replace("[", "").replace("]", "") : "valid";
    }

    public PaymentBean getInvoice(String customerNumber, String transactionType, String invoiceOrBl, Double paidAmount) {
        PaymentBean invoice = new PaymentBean();
        invoice.setTransactionType(transactionType);
        invoice.setInvoiceOrBl(invoiceOrBl);
        if (CommonUtils.isEqual(transactionType, TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            paidAmount = -paidAmount;
        }
        invoice.setPaidAmount(NumberUtils.formatNumber(paidAmount));
        StringBuilder queryBuilder = new StringBuilder("select concat(transaction_id,transaction_type) as transaction_id,");
        queryBuilder.append("format(if(transaction_type='AP',-balance_in_process,balance_in_process),2) as balance_in_process");
        queryBuilder.append(" from transaction");
        queryBuilder.append(" where cust_no='").append(customerNumber).append("'");
        queryBuilder.append(" and (bill_ladding_no='").append(invoiceOrBl).append("'");
        if (CommonUtils.isEqual(transactionType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
            queryBuilder.append(" or drcpt='").append(invoiceOrBl).append("'");
        }
        queryBuilder.append(" or invoice_number='").append(invoiceOrBl).append("')");
        queryBuilder.append(" and transaction_type='").append(transactionType).append("'");
        queryBuilder.append(" order by transaction_date limit 1");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            Object[] col = (Object[]) result;
            invoice.setTransactionId((String) col[0]);
            invoice.setBalanceInProcess((String) col[1]);
            Double balanceInProcess = Double.parseDouble(((String) col[1]).replace(",", ""));
            invoice.setMatches(CommonUtils.isEqual(paidAmount, balanceInProcess));
            if (CommonUtils.isEqual(paidAmount, balanceInProcess)) {
                invoice.setMatches(true);
            } else {
                invoice.setMatches(false);
                invoice.setComments("Payment amount " + invoice.getPaidAmount() + " is not equal to balance " + invoice.getBalanceInProcess());
            }
        } else {
            invoice.setMatches(false);
            invoice.setComments("Invoice is not matched");
        }
        return invoice;
    }

    public List<Transaction> getApPayments(String batchId) {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_PAYAMENT));
        criteria.add(Restrictions.eq("arBatchId", Integer.parseInt(batchId)));
        return criteria.list();
    }

    public List<TransactionLedger> getSubledgers(String batchId) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_PURCHASE_JOURNAL));
        disjunction.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_RCT));
        disjunction.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_NET_SETT));
        criteria.add(disjunction);
        criteria.add(Restrictions.eq("arBatchId", Integer.parseInt(batchId)));
        return criteria.list();
    }

    public List<Object> getArInvoices(String batchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from Transaction tr, Payments p");
        queryBuilder.append(" where tr.transactionType = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and tr.transactionId = p.transactionId");
        queryBuilder.append(" and p.transactionType = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and p.batchId = ").append(batchId);
        return getSession().createQuery(queryBuilder.toString()).list();
    }

    public List<Object> getApInvoices(String batchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from Transaction tr, Payments p");
        queryBuilder.append(" where tr.transactionType = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and tr.transactionId = p.transactionId");
        queryBuilder.append(" and p.transactionType = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and p.batchId = ").append(batchId);
        return getSession().createQuery(queryBuilder.toString()).list();
    }

    public List<Object> getAccruals(String batchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from TransactionLedger tl, Payments p");
        queryBuilder.append(" where tl.transactionType = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.transactionId = p.transactionId");
        queryBuilder.append(" and p.transactionType = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and p.batchId = ").append(batchId);
        return getSession().createQuery(queryBuilder.toString()).list();
    }

    public void removeApInvoices(String batchId, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" delete from transaction");
        queryBuilder.append(" where transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and status = '").append(STATUS_PAID).append("'");
        queryBuilder.append(" and ar_batch_id = ").append(batchId);
        if (CommonUtils.isNotEmpty(ids)) {
            queryBuilder.append(" and transaction_id not in (").append(ids).append(")");
        }
        getSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public boolean checkDisputeStatus(String transactionId) throws Exception {
        String query = "select status from transaction where transaction_id='" + transactionId + "'";
        String result = (String) getCurrentSession().createSQLQuery(query).uniqueResult();
        return CommonUtils.isEqualIgnoreCase(result, "Dispute");
    }

    public String[] billToPartyNameAndNo(String fileNo, String shipmentType) throws Exception {
        String acctNameAndNo[] = new String[2];
        String billTo = "";
        StringBuilder queryBuilder = new StringBuilder();
        if (shipmentType.equalsIgnoreCase("LCLI") || shipmentType.equalsIgnoreCase("LCLE")) {
            queryBuilder.append("select ");
            queryBuilder.append(" bill_to_party,");
            queryBuilder.append("   fwd_acct_no,(select acct_name from trading_partner where acct_no=fwd_acct_no),");
            queryBuilder.append("   ship_acct_no,(select acct_name from trading_partner where acct_no=ship_acct_no),");
            queryBuilder.append("   cons_acct_no,(select acct_name from trading_partner where acct_no=cons_acct_no),");
            queryBuilder.append("   third_party_acct_no,(select acct_name from trading_partner where acct_no=third_party_acct_no),");
            queryBuilder.append("   sup_acct_no,(select acct_name from trading_partner where acct_no=sup_acct_no),");
            queryBuilder.append("   noty_acct_no,(select acct_name from trading_partner where acct_no=noty_acct_no)");
            queryBuilder.append(" from ");
            queryBuilder.append("   lcl_booking ");
            queryBuilder.append(" where file_number_id =(select id from lcl_file_number where file_number='").append(fileNo).append("')");
        } else {
            fileNo = fileNo.length() > 8 ? fileNo.substring(2, 10) : fileNo.substring(2, 8);
            queryBuilder.append("select bill_to_code, ");
            queryBuilder.append("  forward_agent_no,forwarding_agent_name, ");
            queryBuilder.append("  house_shipper_no,house_shipper_name, ");
            queryBuilder.append("  houseconsignee,house_consignee_name, ");
            queryBuilder.append("  billtrdprty,third_party_name, ");
            queryBuilder.append("  agent_no,agent, ");
            queryBuilder.append("  house_notify_party_no,house_notify_party_name ");
            queryBuilder.append(" from ");
            queryBuilder.append(" fcl_bl  ");
            queryBuilder.append(" where file_no ='").append(fileNo).append("'");
            queryBuilder.append(" order by bol desc");
        }
        Object queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        if (queryObject != null) {
            Object[] acctNameAndNoObj = (Object[]) queryObject;
            if (acctNameAndNoObj[0] != null && !acctNameAndNoObj[0].toString().trim().equals("")) {
                billTo = acctNameAndNoObj[0].toString();
            }
            if (billTo.equalsIgnoreCase("F")) {
                if (acctNameAndNoObj[1] != null && !acctNameAndNoObj[1].toString().trim().equals("")) {
                    acctNameAndNo[0] = acctNameAndNoObj[1].toString();
                }
                if (acctNameAndNoObj[2] != null && !acctNameAndNoObj[2].toString().trim().equals("")) {
                    acctNameAndNo[1] = acctNameAndNoObj[2].toString();
                }
            } else if (billTo.equalsIgnoreCase("S")) {
                if (acctNameAndNoObj[3] != null && !acctNameAndNoObj[3].toString().trim().equals("")) {
                    acctNameAndNo[0] = acctNameAndNoObj[3].toString();
                }
                if (acctNameAndNoObj[4] != null && !acctNameAndNoObj[4].toString().trim().equals("")) {
                    acctNameAndNo[1] = acctNameAndNoObj[4].toString();
                }
            } else if (billTo.equalsIgnoreCase("C")) {
                if (acctNameAndNoObj[5] != null && !acctNameAndNoObj[5].toString().trim().equals("")) {
                    acctNameAndNo[0] = acctNameAndNoObj[5].toString();
                }
                if (acctNameAndNoObj[6] != null && !acctNameAndNoObj[6].toString().trim().equals("")) {
                    acctNameAndNo[1] = acctNameAndNoObj[6].toString();
                }
            } else if (billTo.equalsIgnoreCase("T")) {
                if (acctNameAndNoObj[7] != null && !acctNameAndNoObj[7].toString().trim().equals("")) {
                    acctNameAndNo[0] = acctNameAndNoObj[7].toString();
                }
                if (acctNameAndNoObj[8] != null && !acctNameAndNoObj[8].toString().trim().equals("")) {
                    acctNameAndNo[1] = acctNameAndNoObj[8].toString();
                }
            } else if (billTo.equalsIgnoreCase("A")) {
                if (acctNameAndNoObj[9] != null && !acctNameAndNoObj[9].toString().trim().equals("")) {
                    acctNameAndNo[0] = acctNameAndNoObj[9].toString();
                }
                if (acctNameAndNoObj[10] != null && !acctNameAndNoObj[10].toString().trim().equals("")) {
                    acctNameAndNo[1] = acctNameAndNoObj[10].toString();
                }
            } else if (billTo.equalsIgnoreCase("N")) {
                if (acctNameAndNoObj[11] != null && !acctNameAndNoObj[11].toString().trim().equals("")) {
                    acctNameAndNo[0] = acctNameAndNoObj[11].toString();
                }
                if (acctNameAndNoObj[12] != null && !acctNameAndNoObj[12].toString().trim().equals("")) {
                    acctNameAndNo[1] = acctNameAndNoObj[12].toString();
                }
            }
        }
        return acctNameAndNo;
    }

    public Boolean getApInvoiceAmt(String custNo, String invoiceNumber, Double invoiceAmt) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT  (DATEDIFF(SYSDATE(), t.transaction_date) > ge.code) as result ");
        queryBuilder.append("FROM TRANSACTION t LEFT JOIN vendor_info ve  ON (t.cust_no = ve.cust_accno)  ");
        queryBuilder.append("LEFT JOIN genericcode_dup ge  ON (ve.credit_terms = ge.id) where ");
        queryBuilder.append("t.cust_no =:custNo and  t.transaction_amt =:invoiceAmt  and  t.transaction_type='AP' and ");
        queryBuilder.append("(t.bill_ladding_no =:invoiceNumber  OR t.invoice_number =:invoiceNumber)");
        SQLQuery queryObj = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObj.setString("custNo", custNo);
        queryObj.setString("invoiceNumber", invoiceNumber);
        queryObj.setDouble("invoiceAmt", invoiceAmt);
        queryObj.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObj.uniqueResult();
    }
}
