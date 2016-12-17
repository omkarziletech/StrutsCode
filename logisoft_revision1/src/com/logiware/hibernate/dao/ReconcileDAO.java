package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.bean.ReconcileModel;
import com.logiware.excel.ReconcileExcelCreator;
import com.logiware.form.ReconcileForm;
import com.logiware.hibernate.domain.ReconcileLog;
import com.logiware.utils.AuditNotesUtils;
import com.logiware.utils.ReconcileCsvReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReconcileDAO extends BaseHibernateDAO implements ConstantsInterface {

    private String buildCostsAgainstDirectGLQuery(String glAccount, String reconcileDate) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select 'AP INVOICE' as transaction_type,");
        queryBuilder.append("tl.invoice_number as reference_number,");
        queryBuilder.append("'' as batch_id,");
        queryBuilder.append("tl.transaction_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("if(tl.reconciled='").append(STATUS_IN_PROGRESS).append("',");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("','') as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id)as char) as id");
        queryBuilder.append(" from transaction_ledger tl");
        queryBuilder.append(" where tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.status='").append(STATUS_ASSIGN).append("' and tl.direct_gl_account=1");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.cleared is null or tl.cleared!='").append(YES).append("')");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tl.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" group by tl.cust_no,tl.invoice_number");
        queryBuilder.append(" order by tl.cust_no,tl.invoice_number");
        return queryBuilder.toString();
    }

    private String buildOpenChecksQuery(String glAccount, String reconcileDate) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tr.pay_method as transaction_type,");
        queryBuilder.append("if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("concat('").append(PAYMENT_METHOD_ACH).append(" - ',tr.ach_batch_sequence),tr.cheque_number) as reference_number,");
        queryBuilder.append("cast(tr.ap_batch_id as char) as batch_id,");
        queryBuilder.append("tr.transaction_date as transaction_date,");
        queryBuilder.append("if(sum(tr.transaction_amt)<0,-sum(tr.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tr.transaction_amt)>0,sum(tr.transaction_amt),0) as credit,");
        queryBuilder.append("if(tr.reconciled='").append(STATUS_IN_PROGRESS).append("',");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("','') as status,");
        queryBuilder.append("cast(group_concat(tr.transaction_id) as char) as id");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and (((tr.pay_method!='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and tr.status='").append(STATUS_PAID).append("'");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date>'").append(reconcileDate).append("'))))");
        queryBuilder.append(" or ((tr.pay_method='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.status='").append(STATUS_SENT).append("' or tr.status='").append(STATUS_READY_TO_SEND).append("')");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date<='").append(reconcileDate).append("')))))");
        queryBuilder.append(" and (tr.cleared is null or tr.cleared!='").append(YES).append("')");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" group by concat(tr.pay_method,if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("tr.ach_batch_sequence,concat(tr.ap_batch_id,tr.cheque_number,tr.cust_no)),tr.void)");
        queryBuilder.append(" order by concat(tr.pay_method,if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("tr.ach_batch_sequence,concat(tr.ap_batch_id,tr.cheque_number,tr.cust_no)),tr.void)");
        return queryBuilder.toString();
    }

    private String buildOpenDepositsQuery(String glAccount, String reconcileDate) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select 'DEPOSIT' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(tl.transaction_amt>0,tl.transaction_amt,0) as debit,");
        queryBuilder.append("if(tl.transaction_amt<0,-tl.transaction_amt,0) as credit,");
        queryBuilder.append("if(tl.reconciled='").append(STATUS_IN_PROGRESS).append("',");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("','') as status,");
        queryBuilder.append("cast(tl.transaction_id as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" order by ab.Batch_id");
        return queryBuilder.toString();
    }

    private String buildJeQuery(String glAccount, String reconcilePeriod) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select 'JE' as transaction_type,");
        queryBuilder.append("je.journal_entry_id as reference_number,");
        queryBuilder.append("cast(b.batch_id as char) as batch_id,");
        queryBuilder.append("je.period as transaction_date,");
        queryBuilder.append("li.debit as debit,");
        queryBuilder.append("li.credit as credit,");
        queryBuilder.append("if(li.status='").append(STATUS_RECONCILE_IN_PROGRESS).append("',");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("','') as status,");
        queryBuilder.append("li.line_item_id as id");
        queryBuilder.append(" from line_item li");
        queryBuilder.append(" join journal_entry je");
        queryBuilder.append(" on (li.journal_entry_id=je.journal_entry_id");
        queryBuilder.append(" and je.period<='").append(reconcilePeriod).append("'");
        queryBuilder.append(" and (je.subledger_close is null or je.subledger_close!='").append(YES).append("'))");
        queryBuilder.append(" join batch b");
        queryBuilder.append(" on (b.batch_id=je.batch_id and b.status!='").append(STATUS_DELETED).append("')");
        queryBuilder.append(" where (li.status is null or li.status!='").append(CLEAR).append("')");
        queryBuilder.append(" and li.account='").append(glAccount).append("'");
        return queryBuilder.toString();
    }

    private Map<String, String> buildQueries(ReconcileForm reconcileForm) throws Exception {
        Map<String, String> queries = new HashMap<String, String>();
        String glAccount = reconcileForm.getGlAccount();
        String reconcileDate = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String reconcilePeriod = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyyMM");
        queries.put("costsAgainstDirectGLQuery", buildCostsAgainstDirectGLQuery(glAccount, reconcileDate));
        queries.put("openChecksQuery", buildOpenChecksQuery(glAccount, reconcileDate));
        queries.put("openDepositsQuery", buildOpenDepositsQuery(glAccount, reconcileDate));
        queries.put("jeQuery", buildJeQuery(glAccount, reconcilePeriod));
        return queries;
    }

    private Integer getTotalRows(Map<String, String> queries, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(t.id) as totalRows");
        queryBuilder.append(" from (");
        queryBuilder.append(" (").append(queries.get("costsAgainstDirectGLQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openChecksQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openDepositsQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("jeQuery")).append(")");
        queryBuilder.append(" ) as t");
        if (CommonUtils.isNotEmpty(ids)) {
            queryBuilder.append(" where t.id not in (").append(ids).append(")");
        }
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private List<ReconcileModel> getTransactions(Map<String, String> queries, String sortBy, String orderBy, int startRow, int limit, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("if(t.transaction_type='JE',t.transaction_date,date_format(t.transaction_date,'%m/%d/%Y')) as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append(" (").append(queries.get("costsAgainstDirectGLQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openChecksQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openDepositsQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("jeQuery")).append(")");
        queryBuilder.append(" ) as t");
        if (CommonUtils.isNotEmpty(ids)) {
            queryBuilder.append(" where t.id not in (").append(ids).append(")");
        }
        queryBuilder.append(" order by t.").append(sortBy).append(" ").append(orderBy);
//	queryBuilder.append(" limit ").append(startRow).append(",").append(limit);
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class)).list();
    }

    private ReconcileModel getOpenTransactions(Map<String, String> queries, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select format(sum(t.debit),2) as debit,");
        queryBuilder.append("format(sum(t.credit),2) as credit");
        queryBuilder.append(" from (");
        queryBuilder.append(" (").append(queries.get("costsAgainstDirectGLQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openChecksQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openDepositsQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("jeQuery")).append(")");
        queryBuilder.append(" ) as t");
        queryBuilder.append(" where t.status!='").append(STATUS_RECONCILE_IN_PROGRESS).append("'");
        if (CommonUtils.isNotEmpty(ids)) {
            queryBuilder.append(" and t.id not in (").append(ids).append(")");
        }
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();

    }
    
    public Double getGlBalanceInJE(String account, String startPeriod, String endPeriod) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(li.debit)-sum(li.credit)");
        queryBuilder.append(" from line_item li");
        queryBuilder.append(" join journal_entry je");
        queryBuilder.append(" on (li.journal_entry_id=je.journal_entry_id");
        queryBuilder.append(" and (je.subledger_close is null or je.subledger_close!='").append(YES).append("')");
        queryBuilder.append(" and je.period between '").append(startPeriod).append("' and '").append(endPeriod).append("')");
        queryBuilder.append(" join batch b");
        queryBuilder.append(" on (je.batch_id=b.batch_id");
        queryBuilder.append(" and b.status='").append(STATUS_POSTED).append("')");
        queryBuilder.append(" where li.account='").append(account).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Double.parseDouble(result.toString()) : 0d;
    }

    public Double getGlBalanceInSubledger(String account, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(tl.transaction_amt)");
        queryBuilder.append(" from transaction_ledger tl");
        queryBuilder.append(" where (tl.subledger_source_code!='' or tl.subledger_source_code is not null)");
        queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_PAID).append("')");
        queryBuilder.append(" and tl.GL_account_number='").append(account).append("'");
        queryBuilder.append(" and tl.posted_date between '").append(startDate).append("' and '").append(endDate).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Double.parseDouble(result.toString()) : 0d;
    }

    public Double getGlBalance(ReconcileForm reconcileForm) throws Exception {
        Double glBalance = 0d;
        Calendar reconcileDate = Calendar.getInstance();
        reconcileDate.setTime(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"));
        Integer reconciledPeriod = reconcileDate.get(Calendar.MONTH) + 1;
        String period = DateUtils.formatDate(reconcileDate.getTime(), "yyyyMM");
        FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getLastClosedPeriod(period);
        Integer lastClosedPeriod = Integer.parseInt(fiscalPeriod.getPeriod());
        Integer lastClosedYear = fiscalPeriod.getYear();
        glBalance += new AccountBalanceDAO().getGLBalance(reconcileForm.getGlAccount(), fiscalPeriod.getPeriodDis());
        if (CommonUtils.isNotEqual(reconciledPeriod, lastClosedPeriod)) {
            Calendar lastClosedDate = Calendar.getInstance();
            lastClosedDate.set(lastClosedYear, lastClosedPeriod, 1);
            String startPeriod = DateUtils.formatDate(lastClosedDate.getTime(), "yyyyMM");
            String endPeriod = DateUtils.formatDate(reconcileDate.getTime(), "yyyyMM");
            String startDate = DateUtils.formatDate(lastClosedDate.getTime(), "yyyy-MM-dd 00:00:00");
            String endDate = DateUtils.formatDate(reconcileDate.getTime(), "yyyy-MM-dd 23:59:59");
            glBalance += getGlBalanceInJE(reconcileForm.getGlAccount(), startPeriod, endPeriod);
            glBalance += getGlBalanceInSubledger(reconcileForm.getGlAccount(), startDate, endDate);
        }
        return glBalance;
    }

    public void search(ReconcileForm reconcileForm) throws Exception {
        setGroupConcatMaxLength();
        List<String> selectedIds = new ArrayList<String>();
        if (CommonUtils.isNotEmpty(reconcileForm.getTransactions())) {
            for (ReconcileModel transaction : reconcileForm.getTransactions()) {
                selectedIds.add("'" + transaction.getId() + "'");
            }
        }
        String ids = selectedIds.toString().replace("[", "").replace("]", "");
        Integer selectedSize = reconcileForm.getTransactions().size();
        Map<String, String> queries = buildQueries(reconcileForm);
        reconcileForm.setQueries(queries);
        Integer totalRows = getTotalRows(queries, ids) + selectedSize;
        reconcileForm.setTotalRows(totalRows);
        Integer limit = reconcileForm.getLimit();
        Integer totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
        reconcileForm.setTotalPages(totalPages);
        Integer startRow = limit * (reconcileForm.getSelectedPage() - 1);
        reconcileForm.setTransactions(getTransactions(queries, reconcileForm.getSortBy(), reconcileForm.getOrderBy(), startRow, limit, ids));
        reconcileForm.setSelectedRows(null != reconcileForm.getTransactions() ? reconcileForm.getTransactions().size() : 0);
        reconcileForm.setGlBalance(NumberUtils.formatNumber(getGlBalance(reconcileForm)));
        Double difference = 0d;
        ReconcileModel transaction = getOpenTransactions(queries, ids);
        reconcileForm.setDepositsOpen(transaction.getDebit());
        reconcileForm.setChecksOpen(transaction.getCredit());
        difference += Double.parseDouble(reconcileForm.getBankBalance().replaceAll(",", ""));
        difference -= Double.parseDouble(reconcileForm.getGlBalance().replaceAll(",", ""));
        difference += Double.parseDouble(reconcileForm.getDepositsOpen().replaceAll(",", ""));
        difference -= Double.parseDouble(reconcileForm.getChecksOpen().replaceAll(",", ""));
        reconcileForm.setDifference(NumberUtils.formatNumber(difference));
    }

    public ReconcileModel getZbaTransaction(String glAccount, String reconcileDate, Double zbaAmount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'ZBA' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id) as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.applied_amount=").append(zbaAmount);
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" group by ab.Batch_id");
        queryBuilder.append(" order by ab.Batch_id");
        queryBuilder.append(") as t limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getCheckDepositPackage(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Check Deposit Package' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id) as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.applied_amount=").append(amount);
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" group by ab.Batch_id");
        queryBuilder.append(") as t");
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getCheckPaid(String glAccount, String reconcileDate, String checkNumber, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Check Paid' as transaction_type,");
        queryBuilder.append("tr.cheque_number as reference_number,");
        queryBuilder.append("cast(tr.ap_batch_id as char) as batch_id,");
        queryBuilder.append("tr.transaction_date as transaction_date,");
        queryBuilder.append("if(sum(tr.transaction_amt)<0,-sum(tr.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tr.transaction_amt)>0,sum(tr.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tr.transaction_id) as char) as id,");
        queryBuilder.append("sum(tr.transaction_amt) as amount");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and tr.pay_method='").append(PAYMENT_METHOD_CHECK).append("'");
        queryBuilder.append(" and tr.status='").append(STATUS_PAID).append("'");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date>'").append(reconcileDate).append("'))");
        queryBuilder.append(" and tr.cleared='").append(NO).append("'");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and tr.cheque_number='").append(checkNumber).append("'");
        queryBuilder.append(" group by tr.ap_batch_id,tr.cheque_number,tr.cust_no,tr.void");
        queryBuilder.append(" order by tr.ap_batch_id,tr.cheque_number,tr.cust_no,tr.void");
        queryBuilder.append(") as t");
        queryBuilder.append(" where t.amount=").append(-amount);
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getIncomingWireTransfer(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Incoming Wire Transfer' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id) as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.applied_amount=").append(amount);
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" group by ab.Batch_id");
        queryBuilder.append(") as t");
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getPreauthAchCredit(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Preauth ACH Credit' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id) as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.applied_amount=").append(amount);
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" group by ab.Batch_id");
        queryBuilder.append(") as t");
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getPreauthorizedAchDebit(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Preauthorized ACH Debit' as transaction_type,");
        queryBuilder.append("if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("concat('").append(PAYMENT_METHOD_ACH).append(" - ',tr.ach_batch_sequence),tr.cheque_number) as reference_number,");
        queryBuilder.append("cast(tr.ap_batch_id as char) as batch_id,");
        queryBuilder.append("tr.transaction_date as transaction_date,");
        queryBuilder.append("if(sum(tr.transaction_amt)<0,-sum(tr.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tr.transaction_amt)>0,sum(tr.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tr.transaction_id) as char) as id,");
        queryBuilder.append("sum(tr.transaction_amt) as amount");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and (((tr.pay_method='").append(PAYMENT_METHOD_ACH_DEBIT).append("'");
        queryBuilder.append(" and tr.status='").append(STATUS_PAID).append("'");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date>'").append(reconcileDate).append("'))))");
        queryBuilder.append(" or ((tr.pay_method='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.status='").append(STATUS_SENT).append("' or tr.status='").append(STATUS_READY_TO_SEND).append("')");
        queryBuilder.append(" and tr.ach_batch_sequence is not null");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date<='").append(reconcileDate).append("')))))");
        queryBuilder.append(" and tr.cleared='").append(NO).append("'");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" group by concat(tr.pay_method,if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("tr.ach_batch_sequence,tr.ap_batch_id),tr.void)");
        queryBuilder.append(" order by concat(tr.pay_method,if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("tr.ach_batch_sequence,tr.ap_batch_id),tr.void)");
        queryBuilder.append(") as t");
        queryBuilder.append(" where t.amount=").append(-amount);
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getOutgoingWireTransfer(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Outgoing Wire Transfer' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(tr.ap_batch_id as char) as batch_id,");
        queryBuilder.append("tr.transaction_date as transaction_date,");
        queryBuilder.append("if(sum(tr.transaction_amt)<0,-sum(tr.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tr.transaction_amt)>0,sum(tr.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tr.transaction_id) as char) as id,");
        queryBuilder.append("sum(tr.transaction_amt) as amount");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and tr.pay_method='").append(PAYMENT_METHOD_WIRE).append("'");
        queryBuilder.append(" and tr.status='").append(STATUS_PAID).append("'");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date>'").append(reconcileDate).append("'))");
        queryBuilder.append(" and tr.cleared='").append(NO).append("'");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" group by tr.ap_batch_id,tr.cust_no,tr.void");
        queryBuilder.append(" order by tr.ap_batch_id,tr.cust_no,tr.void");
        queryBuilder.append(") as t");
        queryBuilder.append(" where t.amount=").append(-amount);
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getDepositedItemReturned(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Deposited Item Returned' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id) as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.applied_amount=").append(amount);
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" group by ab.Batch_id");
        queryBuilder.append(") as t");
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getIndividualLoanPayment(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Individual Loan Payment' as transaction_type,");
        queryBuilder.append("if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("concat('").append(PAYMENT_METHOD_ACH).append(" - ',tr.ach_batch_sequence),tr.cheque_number) as reference_number,");
        queryBuilder.append("cast(tr.ap_batch_id as char) as batch_id,");
        queryBuilder.append("tr.transaction_date as transaction_date,");
        queryBuilder.append("if(sum(tr.transaction_amt)<0,-sum(tr.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tr.transaction_amt)>0,sum(tr.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tr.transaction_id) as char) as id,");
        queryBuilder.append("sum(tr.transaction_amt) as amount");
        queryBuilder.append(" from transaction tr");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and (((tr.pay_method='").append(PAYMENT_METHOD_ACH_DEBIT).append("'");
        queryBuilder.append(" and tr.status='").append(STATUS_PAID).append("'");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date>'").append(reconcileDate).append("'))))");
        queryBuilder.append(" or ((tr.pay_method='").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append(" and (tr.status='").append(STATUS_SENT).append("' or tr.status='").append(STATUS_READY_TO_SEND).append("')");
        queryBuilder.append(" and tr.ach_batch_sequence is not null");
        queryBuilder.append(" and (tr.void='").append(NO).append("'");
        queryBuilder.append(" or (tr.void='").append(YES).append("' and tr.void_date<='").append(reconcileDate).append("')))))");
        queryBuilder.append(" and tr.cleared='").append(NO).append("'");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" group by concat(tr.pay_method,if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("tr.ach_batch_sequence,tr.ap_batch_id),tr.void)");
        queryBuilder.append(" order by concat(tr.pay_method,if(tr.pay_method='").append(PAYMENT_METHOD_ACH).append("',");
        queryBuilder.append("tr.ach_batch_sequence,tr.ap_batch_id),tr.void)");
        queryBuilder.append(") as t");
        queryBuilder.append(" where t.amount=").append(-amount);
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public ReconcileModel getIndLoanDeposit(String glAccount, String reconcileDate, double amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("date_format(t.transaction_date,'%m/%d/%Y') as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append("select 'Deposited Item Returned' as transaction_type,");
        queryBuilder.append("null as reference_number,");
        queryBuilder.append("cast(ab.batch_id as char) as batch_id,");
        queryBuilder.append("ab.deposit_date as transaction_date,");
        queryBuilder.append("if(sum(tl.transaction_amt)>0,sum(tl.transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.transaction_amt)<0,-sum(tl.transaction_amt),0) as credit,");
        queryBuilder.append("'").append(STATUS_RECONCILE_IN_PROGRESS).append("' as status,");
        queryBuilder.append("cast(group_concat(tl.transaction_id) as char) as id");
        queryBuilder.append(" from ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.applied_amount=").append(amount);
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        queryBuilder.append(" group by ab.Batch_id");
        queryBuilder.append(") as t");
        queryBuilder.append(" limit 1");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return (ReconcileModel) query.uniqueResult();
    }

    public void importTemplate(ReconcileForm reconcileForm) throws Exception {
        setGroupConcatMaxLength();
        new ReconcileCsvReader().readCsv(reconcileForm);
        search(reconcileForm);
    }

    public List<ReconcileModel> getJeTransactions(String batchId, String glAccount, String reconcilePeriod) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select 'JE' as transactionType,");
        queryBuilder.append("je.journal_entry_id as referenceNumber,");
        queryBuilder.append("cast(b.batch_id as char) as batchId,");
        queryBuilder.append("je.period as transactionDate,");
        queryBuilder.append("format(li.debit,2) as debit,");
        queryBuilder.append("format(li.credit,2) as credit,");
        queryBuilder.append("'' as status,");
        queryBuilder.append("li.line_item_id as id");
        queryBuilder.append(" from line_item li");
        queryBuilder.append(" join journal_entry je");
        queryBuilder.append(" on (li.journal_entry_id=je.journal_entry_id");
        queryBuilder.append(" and je.period<='").append(reconcilePeriod).append("')");
        queryBuilder.append(" join batch b");
        queryBuilder.append(" on (b.batch_id='").append(batchId).append("'");
        queryBuilder.append(" and b.batch_id=je.batch_id)");
        queryBuilder.append(" where li.account='").append(glAccount).append("'");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class)).list();
    }

    private void saveCostAgainstDirectGL(String glAccount, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger tl");
        queryBuilder.append(" set tl.reconciled='").append(NO).append("'");
        queryBuilder.append(" where tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.status='").append(STATUS_ASSIGN).append("' and tl.direct_gl_account=1");
        queryBuilder.append(" and (tl.cleared is null or tl.cleared!='").append(YES).append("')");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tl.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tl.transaction_id not in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger tl");
        queryBuilder.append(" set tl.reconciled='").append(STATUS_IN_PROGRESS).append("'");
        queryBuilder.append(" where tl.transaction_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void saveOpenChecks(String glAccount, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction tr");
        queryBuilder.append(" set tr.reconciled='").append(NO).append("'");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and (tr.cleared is null or tr.cleared!='").append(YES).append("')");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and tr.transaction_id not in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction tr");
        queryBuilder.append(" set tr.reconciled='").append(STATUS_IN_PROGRESS).append("'");
        queryBuilder.append(" where tr.transaction_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void saveOpenDeposits(String glAccount, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tl.transaction_id in (").append(ids).append(")");
        queryBuilder.append(" set tl.reconciled='").append(NO).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger tl");
        queryBuilder.append(" set tl.reconciled='").append(STATUS_IN_PROGRESS).append("'");
        queryBuilder.append(" where tl.transaction_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void saveJe(String glAccount, String reconcilePeriod, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update line_item li");
        queryBuilder.append(" join journal_entry je");
        queryBuilder.append(" on (li.journal_entry_id=je.journal_entry_id");
        queryBuilder.append(" and je.period<='").append(reconcilePeriod).append("'");
        queryBuilder.append(" and (je.subledger_close is null or je.subledger_close!='").append(YES).append("'))");
        queryBuilder.append(" join batch b");
        queryBuilder.append(" on (b.batch_id=je.batch_id and b.status!='").append(STATUS_DELETED).append("')");
        queryBuilder.append(" set li.status='").append(STATUS_RECONCILE_IN_PROGRESS).append("'");
        queryBuilder.append(" where (li.status is null or li.status!='").append(CLEAR).append("')");
        queryBuilder.append(" and li.account='").append(glAccount).append("'");
        queryBuilder.append(" and li.line_item_id not in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update line_item li");
        queryBuilder.append(" set li.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" where li.line_item_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void save(ReconcileForm reconcileForm) throws Exception {
        List<String> costAgainstDirectGLIds = new ArrayList<String>();
        List<String> openChecksIds = new ArrayList<String>();
        List<String> openDepositsIds = new ArrayList<String>();
        List<String> openJeIds = new ArrayList<String>();
        if (CommonUtils.isNotEmpty(reconcileForm.getTransactions())) {
            for (ReconcileModel transaction : reconcileForm.getTransactions()) {
                if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "ZBA")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Check Deposit Package")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Check Paid")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Incoming Wire Transfer")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Preauth ACH Credit")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Preauthorized ACH Debit")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Outgoing Wire Transfer")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Deposited Item Returned")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Individual Loan Payment")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "AP INVOICE")) {
                    costAgainstDirectGLIds.add(transaction.getId());
                } else if (CommonUtils.in(transaction.getTransactionType(),
                        PAYMENT_METHOD_ACH, PAYMENT_METHOD_CHECK, PAYMENT_METHOD_WIRE, PAYMENT_METHOD_CREDIT_CARD, PAYMENT_METHOD_ACH_DEBIT)) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "DEPOSIT")) {
                    openDepositsIds.add(transaction.getId());
                } else {
                    String[] ids = transaction.getId().split(",");
                    for (String id : ids) {
                        openJeIds.add("'" + id + "'");
                    }
                }
            }
        }
        String glAccount = reconcileForm.getGlAccount();
        String reconcileDate = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String reconcilePeriod = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyyMM");
        if (CommonUtils.isNotEmpty(costAgainstDirectGLIds)) {
            saveCostAgainstDirectGL(glAccount, reconcileDate, costAgainstDirectGLIds.toString().replace("[", "").replace("]", ""));
        }
        if (CommonUtils.isNotEmpty(openChecksIds)) {
            saveOpenChecks(glAccount, reconcileDate, openChecksIds.toString().replace("[", "").replace("]", ""));
        }
        if (CommonUtils.isNotEmpty(openDepositsIds)) {
            saveOpenDeposits(glAccount, reconcileDate, openDepositsIds.toString().replace("[", "").replace("]", ""));
        }
        if (CommonUtils.isNotEmpty(openJeIds)) {
            saveJe(glAccount, reconcilePeriod, openJeIds.toString().replace("[", "").replace("]", ""));
        }
        getCurrentSession().flush();
    }

    private void reconcileCostAgainstDirectGL(String glAccount, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger tl");
        queryBuilder.append(" set tl.reconciled='").append(NO).append("'");
        queryBuilder.append(" where tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.status='").append(STATUS_ASSIGN).append("' and tl.direct_gl_account=1");
        queryBuilder.append(" and (tl.cleared is null or tl.cleared!='").append(YES).append("')");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tl.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tl.transaction_id not in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger tl");
        queryBuilder.append(" set tl.reconciled='").append(YES).append("',");
        queryBuilder.append("tl.reconciled_date='").append(reconcileDate).append("',");
        queryBuilder.append("tl.cleared='").append(YES).append("',");
        queryBuilder.append("tl.cleared_date='").append(reconcileDate).append("'");
        queryBuilder.append(" where tl.transaction_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void reconcileOpenChecks(String glAccount, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction tr");
        queryBuilder.append(" set tr.reconciled='").append(NO).append("'");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append(" and tr.ap_batch_id is not null");
        queryBuilder.append(" and (tr.cleared is null or tr.cleared!='").append(YES).append("')");
        queryBuilder.append(" and (tr.reconciled is null or tr.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and tr.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tr.transaction_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and tr.transaction_id not in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction tr");
        queryBuilder.append(" set tr.reconciled='").append(YES).append("',");
        queryBuilder.append("tr.reconciled_date='").append(reconcileDate).append("',");
        queryBuilder.append("tr.cleared='").append(YES).append("',");
        queryBuilder.append("tr.cleared_date='").append(reconcileDate).append("',");
        queryBuilder.append("tr.status='").append(CLEAR).append("'");
        queryBuilder.append(" where tr.transaction_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction tr");
        queryBuilder.append(" join transaction py");
        queryBuilder.append(" on (tr.cust_no=py.cust_no");
        queryBuilder.append(" and tr.invoice_number=py.invoice_number");
        queryBuilder.append(" and tr.ap_batch_id=py.ap_batch_id");
        queryBuilder.append(" and py.transaction_id in (").append(ids).append("))");
        queryBuilder.append(" set tr.reconciled='").append(YES).append("',");
        queryBuilder.append("tr.reconciled_date='").append(reconcileDate).append("',");
        queryBuilder.append("tr.cleared='").append(YES).append("',");
        queryBuilder.append("tr.cleared_date='").append(reconcileDate).append("'");
        queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and tr.status='").append(STATUS_PAID).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void reconcileOpenDeposits(String glAccount, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ar_batch ab");
        queryBuilder.append(" join transaction_ledger tl");
        queryBuilder.append(" on (tl.ar_batch_id=ab.batch_id");
        queryBuilder.append(" and tl.subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(YES).append("')");
        queryBuilder.append(" and (tl.invoice_number is null or tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("')");
        queryBuilder.append(" and tl.transaction_amt<>0)");
        queryBuilder.append(" and tl.gl_account_number='").append(glAccount).append("'");
        queryBuilder.append(" and tl.transaction_id in (").append(ids).append(")");
        queryBuilder.append(" set tl.reconciled='").append(NO).append("'");
        queryBuilder.append(" where ab.deposit_date<='").append(reconcileDate).append("'");
        queryBuilder.append(" and ab.status='").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" and ab.batch_type='").append(AccountingConstants.AR_CASH_BATCH).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger tl");
        queryBuilder.append(" set tl.reconciled='").append(YES).append("',");
        queryBuilder.append("tl.reconciled_date='").append(reconcileDate).append("',");
        queryBuilder.append("tl.cleared='").append(YES).append("',");
        queryBuilder.append("tl.cleared_date='").append(reconcileDate).append("'");
        queryBuilder.append(" where tl.transaction_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void reconcileJe(String glAccount, String reconcilePeriod, String reconcileDate, String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update line_item li");
        queryBuilder.append(" join journal_entry je");
        queryBuilder.append(" on (li.journal_entry_id=je.journal_entry_id");
        queryBuilder.append(" and je.period<='").append(reconcilePeriod).append("'");
        queryBuilder.append(" and (je.subledger_close is null or je.subledger_close!='").append(YES).append("'))");
        queryBuilder.append(" join batch b");
        queryBuilder.append(" on (b.batch_id=je.batch_id and b.status!='").append(STATUS_DELETED).append("')");
        queryBuilder.append(" set li.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" where (li.status is null or li.status!='").append(CLEAR).append("')");
        queryBuilder.append(" and li.account='").append(glAccount).append("'");
        queryBuilder.append(" and li.line_item_id not in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("update line_item li");
        queryBuilder.append(" set li.status='").append(CLEAR).append("',");
        queryBuilder.append("li.date='").append(reconcileDate).append("'");
        queryBuilder.append(" where li.line_item_id in (").append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private List<ReconcileModel> getOpenTransactions(Map<String, String> queries) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.transaction_type as transactionType,");
        queryBuilder.append("t.reference_number as referenceNumber,");
        queryBuilder.append("t.batch_id as batchId,");
        queryBuilder.append("if(t.transaction_type='JE',t.transaction_date,date_format(t.transaction_date,'%m/%d/%Y')) as transactionDate,");
        queryBuilder.append("format(t.debit,2) as debit,");
        queryBuilder.append("format(t.credit,2) as credit,");
        queryBuilder.append("t.status as status,");
        queryBuilder.append("t.id as id");
        queryBuilder.append(" from (");
        queryBuilder.append(" (").append(queries.get("costsAgainstDirectGLQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openChecksQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("openDepositsQuery")).append(")");
        queryBuilder.append(" union");
        queryBuilder.append(" (").append(queries.get("jeQuery")).append(")");
        queryBuilder.append(" ) as t");
        queryBuilder.append(" where t.status!='").append(STATUS_RECONCILE_IN_PROGRESS).append("'");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReconcileModel.class));
        return query.list();

    }

    public void updateBankDetails(String glAccount, String bankName, String bankAccount, String reconcileDate, String bankBalance) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update bank_details bank");
        queryBuilder.append(" set bank.last_reconciled_date='").append(reconcileDate).append("',");
        queryBuilder.append("bank.bank_balance=").append(bankBalance.replace(",", ""));
        queryBuilder.append(" where bank.gl_account_no='").append(glAccount).append("'");
        queryBuilder.append(" and bank.Bank_Name='").append(bankName).append("'");
        queryBuilder.append(" and bank.Bank_Acct_No='").append(bankAccount).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

        Integer id = new BankDetailsDAO().getId(bankAccount, bankName, glAccount);
        ReconcileLog reconcileLog = new ReconcileLog();
        reconcileLog.setGlAccount(glAccount);
        reconcileLog.setBankId(id);
        reconcileLog.setReconcileDate(DateUtils.parseDate(reconcileDate, "yyyy-MM-dd hh:mm:ss"));
        reconcileLog.setBalance(Double.parseDouble(bankBalance));
        getSession().save(reconcileLog);
    }

    public void reconcile(ReconcileForm reconcileForm, User loginUser) throws Exception {
        setGroupConcatMaxLength();
        List<String> costAgainstDirectGLIds = new ArrayList<String>();
        List<String> openChecksIds = new ArrayList<String>();
        List<String> openDepositsIds = new ArrayList<String>();
        List<String> openJeIds = new ArrayList<String>();
        if (CommonUtils.isNotEmpty(reconcileForm.getTransactions())) {
            for (ReconcileModel transaction : reconcileForm.getTransactions()) {
                if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "ZBA")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Check Deposit Package")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Check Paid")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Incoming Wire Transfer")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Preauth ACH Credit")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Preauthorized ACH Debit")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Outgoing Wire Transfer")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Deposited Item Returned")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Individual Loan Payment")) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "Ind Loan Deposit")) {
                    openDepositsIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "AP INVOICE")) {
                    costAgainstDirectGLIds.add(transaction.getId());
                } else if (CommonUtils.in(transaction.getTransactionType(),
                        PAYMENT_METHOD_ACH, PAYMENT_METHOD_CHECK, PAYMENT_METHOD_WIRE, PAYMENT_METHOD_CREDIT_CARD, PAYMENT_METHOD_ACH_DEBIT)) {
                    openChecksIds.add(transaction.getId());
                } else if (CommonUtils.isEqualIgnoreCase(transaction.getTransactionType(), "DEPOSIT")) {
                    openDepositsIds.add(transaction.getId());
                } else {
                    String[] ids = transaction.getId().split(",");
                    for (String id : ids) {
                        openJeIds.add("'" + id + "'");
                    }
                }
            }
        }
        String glAccount = reconcileForm.getGlAccount();
        String reconcileDate = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String reconcilePeriod = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyyMM");
        if (CommonUtils.isNotEmpty(costAgainstDirectGLIds)) {
            reconcileCostAgainstDirectGL(glAccount, reconcileDate, costAgainstDirectGLIds.toString().replace("[", "").replace("]", ""));
        }
        if (CommonUtils.isNotEmpty(openChecksIds)) {
            reconcileOpenChecks(glAccount, reconcileDate, openChecksIds.toString().replace("[", "").replace("]", ""));
        }
        if (CommonUtils.isNotEmpty(openDepositsIds)) {
            reconcileOpenDeposits(glAccount, reconcileDate, openDepositsIds.toString().replace("[", "").replace("]", ""));
        }
        if (CommonUtils.isNotEmpty(openJeIds)) {
            reconcileJe(glAccount, reconcilePeriod, reconcileDate, openJeIds.toString().replace("[", "").replace("]", ""));
        }
        updateBankDetails(glAccount, reconcileForm.getBankName(), reconcileForm.getBankAccount(), reconcileDate, reconcileForm.getBankBalance());
        getCurrentSession().flush();
        reconcileForm.setTransactions(null);
        StringBuilder desc = new StringBuilder("GL Account '").append(glAccount).append("'");
        desc.append(" reconciled by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.RECONCILATION, glAccount, NotesConstants.RECONCILATION, loginUser);
        reconcileForm.setGlBalance(NumberUtils.formatNumber(getGlBalance(reconcileForm)));
        Map<String, String> queries = buildQueries(reconcileForm);
        List<ReconcileModel> openChecks = new ArrayList<ReconcileModel>();
        List<ReconcileModel> openDeposits = new ArrayList<ReconcileModel>();
        List<ReconcileModel> openChecksAndDeposits = getOpenTransactions(queries);
        double totalDebit = 0d;
        double totalCredit = 0d;
        for (ReconcileModel transaction : openChecksAndDeposits) {
            totalDebit += Double.parseDouble(transaction.getDebit().replace(",", ""));
            totalCredit += Double.parseDouble(transaction.getCredit().replace(",", ""));
            if (CommonUtils.isNotEmpty(Double.parseDouble(transaction.getCredit().replace(",", "")))) {
                openChecks.add(transaction);
            }
            if (CommonUtils.isNotEmpty(Double.parseDouble(transaction.getDebit().replace(",", "")))) {
                openDeposits.add(transaction);
            }
        }
        reconcileForm.setDepositsOpen(NumberUtils.formatNumber(totalDebit));
        reconcileForm.setChecksOpen(NumberUtils.formatNumber(totalCredit));
        Double difference = 0d;
        difference += Double.parseDouble(reconcileForm.getBankBalance().replaceAll(",", ""));
        difference -= Double.parseDouble(reconcileForm.getGlBalance().replaceAll(",", ""));
        difference += Double.parseDouble(reconcileForm.getDepositsOpen().replaceAll(",", ""));
        difference -= Double.parseDouble(reconcileForm.getChecksOpen().replaceAll(",", ""));
        reconcileForm.setDifference(NumberUtils.formatNumber(difference));
        reconcileForm.setFileName(new ReconcileExcelCreator(reconcileForm).create(openChecks, openDeposits));
    }

    public boolean isArBatchReconciled(String batchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') as reconciled");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where subledger_source_code='").append(SUB_LEDGER_CODE_RCT).append("'");
        queryBuilder.append(" and reconciled='").append(YES).append("'");
        queryBuilder.append(" and ar_batch_id=").append(batchId);
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }
}
