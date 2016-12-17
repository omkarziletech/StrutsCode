package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.accounting.model.SubledgerModel;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.GlBatchBean;
import com.logiware.form.GlBatchForm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class GlBatchDAO extends BaseHibernateDAO implements Serializable, ConstantsInterface {

    public void save(Batch batch) throws Exception {
        getCurrentSession().save(batch);
        getCurrentSession().flush();
    }

    public Batch saveAndReturn(Batch batch) throws Exception {
        getCurrentSession().save(batch);
        getCurrentSession().flush();
        return batch;
    }

    public void update(Batch batch) throws Exception {
            getCurrentSession().update(batch);
            getCurrentSession().flush();
    }

    public void delete(Batch batch) throws Exception {
            getCurrentSession().delete(batch);
            getCurrentSession().flush();
    }

    public Batch getBatchByIndex(Integer index) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Batch.class);
            criteria.addOrder(Order.asc("batchId"));
            criteria.setFirstResult(index.intValue()).setMaxResults(1);
            Batch batch = (Batch) criteria.uniqueResult();
            getCurrentSession().flush();
            return batch;
    }

    public Integer getBatchIndex(Integer batchId) throws Exception {
        String query = "select count(*) from batch where batch_id<=" + batchId;
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) - 1 : 0);
    }

    public Batch findById(Integer id) throws Exception {
            Batch batch = (Batch) getCurrentSession().get(Batch.class, id);
            getCurrentSession().flush();
            return batch;
    }

    public List<Integer> getOpenBatches(Integer batchId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select batch_id from batch where status='").append("Open").append("' and batch_id!=").append(batchId);
        List<Integer> openBatches = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        getCurrentSession().flush();
        return openBatches;
    }

    public Integer getTotal(String condition) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(b.Batch_Id)");
        queryBuilder.append(" from batch b");
        queryBuilder.append(condition);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        getCurrentSession().flush();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }

    public List<GlBatchBean> search(String condition, String sortBy, String orderBy, int start, int end) {
        List glBatches = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select b.batch_id as batchId,");
        queryBuilder.append("b.batch_desc as description,");
        queryBuilder.append("ge.code as subledgerType,");
        queryBuilder.append("if(b.type = 'manual','manual','auto') as batchType,");
        queryBuilder.append("format(b.total_debit,2) as debit,");
        queryBuilder.append("format(b.total_credit,2) as credit,");
        queryBuilder.append("if(b.ready_to_post = 'yes' && b.status != 'deleted','true','false') as readyToPost,");
        queryBuilder.append("b.status as status,");
        queryBuilder.append("if(b.status = 'posted',min(je.period),'') as period,");
        queryBuilder.append("if(count(doc.document_id) >0,'true','false') as uploaded");
        queryBuilder.append(" from batch b");
        queryBuilder.append(" left join genericcode_dup ge");
        queryBuilder.append(" on (ge.id = b.source_ledger)");
        queryBuilder.append(" left join journal_entry je");
        queryBuilder.append(" on (je.batch_id=b.batch_id)");
        queryBuilder.append(" left join document_store_log doc");
        queryBuilder.append(" on (doc.screen_name = 'JOURNAL ENTRY'");
        queryBuilder.append(" and doc.document_name = 'JOURNAL ENTRY'");
        queryBuilder.append(" and cast(doc.document_id as char character set latin1) = cast(b.batch_id as char character set latin1))");
        queryBuilder.append(condition);
        queryBuilder.append(" group by b.batch_id");
        queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setFirstResult(start).setMaxResults(end);
        List result = query.list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            GlBatchBean glBatch = new GlBatchBean();
            glBatch.setId(col[0].toString());
            glBatch.setDescription((String) col[1]);
            glBatch.setSubledgerType((String) col[2]);
            glBatch.setType((String) col[3]);
            glBatch.setDebit((String) col[4]);
            glBatch.setCredit((String) col[5]);
            glBatch.setReadyToPost(Boolean.valueOf((String) col[6]).booleanValue());
            glBatch.setStatus((String) col[7]);
            glBatch.setPeriod((String) col[8]);
            glBatch.setUploaded(Boolean.valueOf((String) col[9]));
            glBatches.add(glBatch);
        }
        return glBatches;
    }

    public String buildQuery(GlBatchForm glBatchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(glBatchForm.getBatchId())) {
            queryBuilder.append(" where b.Batch_Id = '").append(glBatchForm.getBatchId()).append("'");
        } else {
            if (CommonUtils.isEqual(glBatchForm.getStatus(), "Open")) {
                queryBuilder.append(" where (b.Status='Open' or b.Status='ready to post')");
            } else {
                queryBuilder.append(" where (b.Status='Open' or b.Status='ready to post'");
                queryBuilder.append(" or b.Status='posted' or b.Status='deleted')");
            }
            if (CommonUtils.isNotEmpty(glBatchForm.getSubledgerType())) {
                queryBuilder.append(" and b.Source_Ledger = '").append(glBatchForm.getSubledgerType()).append("'");
            }
        }
        return queryBuilder.toString();
    }

    public List<LabelValueBean> getSubledgerTypes() {
        List<LabelValueBean> subledgerTypes = new ArrayList<LabelValueBean>();
        StringBuilder queryBuilder = new StringBuilder("select g.code,g.id from genericcode_dup g,codetype c");
        queryBuilder.append(" where g.codetypeid=c.codetypeid and c.description='Source Codes'");
        queryBuilder.append(" order by g.code='GL-JE' desc,g.code");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            subledgerTypes.add(new LabelValueBean((String) col[0], col[1].toString()));
        }
        return subledgerTypes;
    }

    public List<LabelValueBean> getPeriods() throws Exception {
        List<LabelValueBean> periods = new ArrayList<LabelValueBean>();
        FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
        StringBuilder queryBuilder = new StringBuilder("select period_dis from fiscal_period");
        queryBuilder.append(" where status='").append("Open").append("'");
        queryBuilder.append(" or period_dis='").append(fiscalPeriod.getPeriodDis()).append("'");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            String col = (String) row;
            periods.add(new LabelValueBean(col, col));
        }
        return periods;
    }

    public Integer getNoOfBatches() {
        String query = "select count(*) from batch";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }

    public Integer getNoOfJournalEntries(Integer batchId) {
        String query = "select count(*) from journal_entry where batch_id='" + batchId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }

    public Double getTotalDebitFromJEs(Integer batchId) {
        String query = "select sum(debit) from journal_entry where batch_id='" + batchId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Double.valueOf(null != result ? Double.parseDouble(result.toString()) : 0d);
    }

    public Double getTotalCreditFromJEs(Integer batchId) {
        String query = "select sum(credit) from journal_entry where batch_id='" + batchId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Double.valueOf(null != result ? Double.parseDouble(result.toString()) : 0d);
    }

    public Double getTotalDebitFromJEs(Integer batchId, String journalEntryId) {
        String query = "select sum(debit) from journal_entry where batch_id='" + batchId + "' and journal_entry_id!='" + journalEntryId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Double.valueOf(null != result ? Double.parseDouble(result.toString()) : 0d);
    }

    public Double getTotalCreditFromJEs(Integer batchId, String journalEntryId) {
        String query = "select sum(credit) from journal_entry where batch_id='" + batchId + "' and journal_entry_id!='" + journalEntryId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Double.valueOf(null != result ? Double.parseDouble(result.toString()) : 0d);
    }

    public JournalEntry getJournalEntry(Integer batchId, Integer index) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(JournalEntry.class);
            criteria.add(Restrictions.eq("batchId", batchId));
            criteria.addOrder(Order.asc("journalEntryId"));
            criteria.setFirstResult(index.intValue()).setMaxResults(1);
            JournalEntry journalEntry = (JournalEntry) criteria.uniqueResult();
            getCurrentSession().flush();
            return journalEntry;
    }

    public JournalEntry getJournalEntry(String id)  throws Exception {
            JournalEntry journalEntry = (JournalEntry) getCurrentSession().get(JournalEntry.class, id);
            getCurrentSession().flush();
            return journalEntry;
    }

    public void save(JournalEntry transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public Integer getJournalEntryIndex(Integer batchId, String journalEntryId) throws Exception {
        String query = "select count(*) from journal_entry where batch_id=" + batchId + " and journal_entry_id<='" + journalEntryId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) - 1 : 0);
    }

    public Integer getLastJournalEntrySuffix(Integer batchId)  throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select replace(replace(journal_entry_id,concat(batch_id,'-'),''),'R','') as suffix");
        queryBuilder.append(" from journal_entry where batch_id=").append(batchId).append("  order by journal_entry_id desc limit 1");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        getCurrentSession().flush();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }

    public void deleteJournalEntry(String journalEntryId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from journal_entry where journal_entry_id='").append(journalEntryId).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public Double getTotalDebitFromLineItems(String journalEntryId) throws Exception {
        String query = "select sum(debit) from line_item where journal_entry_id='" + journalEntryId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Double.valueOf(null != result ? Double.parseDouble(result.toString()) : 0d);
    }

    public Double getTotalCreditFromLineItems(String journalEntryId) throws Exception {
        String query = "select sum(credit) from line_item where journal_entry_id='" + journalEntryId + "'";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        getCurrentSession().flush();
        return Double.valueOf(null != result ? Double.parseDouble(result.toString()) : 0d);
    }

    public void deleteLineItems(String journalEntryId, List<String> lineItemIds) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from line_item where journal_entry_id='").append(journalEntryId).append("'");
        if (CommonUtils.isNotEmpty(lineItemIds)) {
            queryBuilder.append(" and line_item_id not in ").append(lineItemIds.toString().replace("[", "(").replace("]", ")"));
        }
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public List<LineItem> getLineItems(String journalEntryId) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(LineItem.class);
            criteria.add(Restrictions.eq("journalEntryId", journalEntryId));
            criteria.addOrder(Order.asc("lineItemId"));
            List<LineItem> lineItems = criteria.list();
            getCurrentSession().flush();
            return lineItems;
    }

    public LineItem getLineItem(String id) throws Exception {
            LineItem lineItem = (LineItem) getCurrentSession().get(LineItem.class, id);
            getCurrentSession().flush();
            return lineItem;
    }

    public void save(LineItem transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public String validateGlBatchForPosting(Integer batchId)  throws Exception {
        String jeQuery = "select count(*) from journal_entry je where je.Batch_Id=" + batchId;
        Object jeCount = getCurrentSession().createSQLQuery(jeQuery).uniqueResult();
        getCurrentSession().flush();
        if (Integer.parseInt(jeCount.toString()) > 0) {
            StringBuilder liQuery = new StringBuilder("select count(t.Journal_Entry_Id) from (");
            liQuery.append(" select li.Line_Item_Id,je.Journal_Entry_Id from journal_entry je");
            liQuery.append(" left join line_item li on li.Journal_Entry_Id=je.Journal_Entry_Id");
            liQuery.append(" where je.Batch_Id=").append(batchId);
            liQuery.append(") as t where t.line_item_id is null");
            Object noLiCount = getCurrentSession().createSQLQuery(liQuery.toString()).uniqueResult();
            getCurrentSession().flush();
            return Integer.parseInt(noLiCount.toString()) > 0 ? "No Line Items" : null;
        }
        return "No Journal Entries";
    }

    public void updateAccountBalance(Integer batchId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  update_account_balance_from_je (");
        queryBuilder.append("    ").append(batchId);
        queryBuilder.append("  )");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void voidBatch(Integer batchId) {
        String query = "delete li from journal_entry je,line_item li where li.journal_entry_id=je.journal_entry_id and je.Batch_Id=" + batchId;
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
        query = "delete from journal_entry where Batch_Id=" + batchId;
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
        query = "update batch set status='deleted' where Batch_Id=" + batchId;
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
    }

    public List<SubledgerModel> getDrillDownDetailsForLineItem(String lineItemId) {
        List drillDownDetials = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder("select charge_code,format(amount,2) from transaction_ledger_history");
        queryBuilder.append(" where line_item_id='").append(lineItemId).append("'");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            drillDownDetials.add(new SubledgerModel((String) col[0], (String) col[1]));
        }
        return drillDownDetials;
    }

    public double getGlAccountBalance(String account, String period) {
        StringBuilder queryBuilder = new StringBuilder("select sum(li.debit)-sum(li.credit)");
        queryBuilder.append(" from line_item li,journal_entry je,batch b");
        queryBuilder.append(" where b.Batch_Id=je.Batch_Id and b.status='").append("posted").append("'");
        queryBuilder.append(" and je.journal_entry_id=li.journal_entry_id and je.period='").append(period).append("'");
        queryBuilder.append(" and li.account='").append(account).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        getCurrentSession().flush();
        return null != result ? Double.parseDouble(result.toString()) : 0d;
    }

    public List<AccountingBean> getACCSubledgersHistory(String journalEntryId) {
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder query = new StringBuilder();
        query.append("select tlh.cust_no,tlh.cust_name,tlh.gl_account,tlh.bill_of_ladding,tlh.invoice,tlh.voyage,tlh.charge_code,");
        query.append("date_format(tlh.transaction_date,'%m/%d/%Y'),date_format(tlh.reporting_date,'%m/%d/%Y'),date_format(tlh.posted_date,'%m/%d/%Y'),");
        query.append("if(tlh.amount>0,tlh.amount,0) as debit,if(tlh.amount<0,abs(tlh.amount),0) as credit,li.Line_Item_Id");
        query.append(" from line_item li,transaction_ledger_history tlh");
        query.append(" where li.Line_Item_Id=tlh.line_item_id");
        query.append(" and li.Journal_Entry_Id='").append(journalEntryId).append("'");
        query.append(" order by tlh.gl_account");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setVendorNumber((String) col[0]);
            transaction.setVendorName((String) col[1]);
            transaction.setGlAccount((String) col[2]);
            transaction.setBillOfLadding((String) col[3]);
            transaction.setInvoiceNumber((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setChargeCode((String) col[6]);
            transaction.setFormattedDate((String) col[7]);
            transaction.setFormattedReportingDate((String) col[8]);
            transaction.setFormattedPostedDate((String) col[9]);
            transaction.setDebitAmount(Double.valueOf(Double.parseDouble(col[10].toString())));
            transaction.setCreditAmount(Double.valueOf(Double.parseDouble(col[11].toString())));
            transaction.setLineItemId((String) col[12]);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<AccountingBean> getPJSubledgersHistory(String journalEntryId) {
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder query = new StringBuilder();
        query.append("select tlh.cust_no,tlh.cust_name,tlh.gl_account,tlh.bill_of_ladding,tlh.invoice,tlh.voyage,tlh.charge_code,");
        query.append("date_format(tlh.transaction_date,'%m/%d/%Y'),date_format(tlh.reporting_date,'%m/%d/%Y'),date_format(tlh.posted_date,'%m/%d/%Y'),");
        query.append("if(tlh.amount>0,tlh.amount,0) as debit,if(tlh.amount<0,abs(tlh.amount),0) as credit,li.Line_Item_Id");
        query.append(" from line_item li,transaction_ledger_history tlh");
        query.append(" where li.Line_Item_Id=tlh.line_item_id");
        query.append(" and li.Journal_Entry_Id='").append(journalEntryId).append("'");
        query.append(" order by tlh.gl_account");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setVendorNumber((String) col[0]);
            transaction.setVendorName((String) col[1]);
            transaction.setGlAccount((String) col[2]);
            transaction.setBillOfLadding((String) col[3]);
            transaction.setInvoiceNumber((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setChargeCode((String) col[6]);
            transaction.setFormattedDate((String) col[7]);
            transaction.setFormattedReportingDate((String) col[8]);
            transaction.setFormattedPostedDate((String) col[9]);
            transaction.setDebitAmount(Double.valueOf(Double.parseDouble(col[10].toString())));
            transaction.setCreditAmount(Double.valueOf(Double.parseDouble(col[11].toString())));
            transaction.setLineItemId((String) col[12]);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<AccountingBean> getCDSubledgersHistory(String journalEntryId) {
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder query = new StringBuilder();
        query.append("select tlh.cust_no,tlh.cust_name,tlh.gl_account,tlh.bill_of_ladding,tlh.invoice,tlh.voyage,tlh.charge_code,");
        query.append("date_format(tlh.transaction_date,'%m/%d/%Y'),date_format(tlh.posted_date,'%m/%d/%Y'),");
        query.append("if((tlh.amount>0 and ad.Normal_Balance='Debit') or (tlh.amount<0 and ad.Normal_Balance='Credit'),abs(tlh.amount),0) as debit,");
        query.append("if((tlh.amount<0 and ad.Normal_Balance='Debit') or (tlh.amount>0 and ad.Normal_Balance='Credit'),abs(tlh.amount),0) as credit,");
        query.append("tlh.Ap_Batch_Id,tlh.transaction_type,li.Line_Item_Id");
        query.append(" from line_item li,transaction_ledger_history tlh,account_details ad");
        query.append(" where li.Line_Item_Id=tlh.line_item_id");
        query.append(" and tlh.gl_account=ad.Account");
        query.append(" and li.Journal_Entry_Id='").append(journalEntryId).append("'");
        query.append(" order by tlh.gl_account");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setVendorNumber((String) col[0]);
            transaction.setVendorName((String) col[1]);
            transaction.setGlAccount((String) col[2]);
            transaction.setBillOfLadding((String) col[3]);
            transaction.setInvoiceNumber((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setChargeCode((String) col[6]);
            transaction.setFormattedDate((String) col[7]);
            transaction.setFormattedPostedDate((String) col[8]);
            transaction.setDebitAmount(Double.valueOf(Double.parseDouble(col[9].toString())));
            transaction.setCreditAmount(Double.valueOf(Double.parseDouble(col[10].toString())));
            transaction.setApBatchId((Integer) col[11]);
            transaction.setLineItemId((String) col[12]);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<AccountingBean> getARSubledgersHistory(String journalEntryId) {
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder query = new StringBuilder();
        query.append("select tlh.cust_no,tlh.cust_name,tlh.gl_account,tlh.bill_of_ladding,tlh.invoice,tlh.voyage,tlh.charge_code,");
        query.append("date_format(tlh.transaction_date,'%m/%d/%Y'),date_format(tlh.posted_date,'%m/%d/%Y'),");
        query.append("if(tlh.amount<0,abs(tlh.amount),0) as debit,if(tlh.amount>0,tlh.amount,0) as credit,li.Line_Item_Id");
        query.append(" from line_item li,transaction_ledger_history tlh");
        query.append(" where li.Line_Item_Id=tlh.line_item_id");
        query.append(" and li.Journal_Entry_Id='").append(journalEntryId).append("'");
        query.append(" order by tlh.gl_account");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setVendorNumber((String) col[0]);
            transaction.setVendorName((String) col[1]);
            transaction.setGlAccount((String) col[2]);
            transaction.setBillOfLadding((String) col[3]);
            transaction.setInvoiceNumber((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setChargeCode((String) col[6]);
            transaction.setFormattedDate((String) col[7]);
            transaction.setFormattedPostedDate((String) col[8]);
            transaction.setDebitAmount(Double.valueOf(Double.parseDouble(col[9].toString())));
            transaction.setCreditAmount(Double.valueOf(Double.parseDouble(col[10].toString())));
            transaction.setLineItemId((String) col[11]);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<AccountingBean> getNSSubledgersHistory(String journalEntryId) {
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder query = new StringBuilder();
        query.append("select tlh.cust_no,tlh.cust_name,tlh.gl_account,tlh.bill_of_ladding,tlh.invoice,tlh.voyage,tlh.charge_code,");
        query.append("date_format(tlh.transaction_date,'%m/%d/%Y'),date_format(tlh.posted_date,'%m/%d/%Y'),");
        query.append("if(tlh.amount>0,tlh.amount,0) as debit,if(tlh.amount<0,abs(tlh.amount),0) as credit,");
        query.append("tlh.ar_batch_id,tlh.transaction_type,li.Line_Item_Id");
        query.append(" from line_item li,transaction_ledger_history tlh");
        query.append(" where li.Line_Item_Id=tlh.line_item_id");
        query.append(" and li.Journal_Entry_Id='").append(journalEntryId).append("'");
        query.append(" order by tlh.gl_account");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setVendorNumber((String) col[0]);
            transaction.setVendorName((String) col[1]);
            transaction.setGlAccount((String) col[2]);
            transaction.setBillOfLadding((String) col[3]);
            transaction.setInvoiceNumber((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setChargeCode((String) col[6]);
            transaction.setFormattedDate((String) col[7]);
            transaction.setFormattedPostedDate((String) col[8]);
            transaction.setDebitAmount(Double.valueOf(Double.parseDouble(col[9].toString())));
            transaction.setCreditAmount(Double.valueOf(Double.parseDouble(col[10].toString())));
            transaction.setArBatchId((Integer) col[11]);
            transaction.setTransactionType((String) col[12]);
            transaction.setLineItemId((String) col[13]);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<AccountingBean> getRCTSubledgersHistory(String journalEntryId) {
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder query = new StringBuilder();
        query.append("select tlh.cust_no,tlh.cust_name,tlh.gl_account,tlh.bill_of_ladding,tlh.invoice,tlh.voyage,tlh.charge_code,");
        query.append("date_format(tlh.transaction_date,'%m/%d/%Y'),date_format(tlh.posted_date,'%m/%d/%Y'),");
        query.append("if(tlh.amount>0,tlh.amount,0) as debit,if(tlh.amount<0,abs(tlh.amount),0) as credit,");
        query.append("tlh.ar_batch_id,tlh.transaction_type,li.Line_Item_Id");
        query.append(" from line_item li,transaction_ledger_history tlh");
        query.append(" where li.Line_Item_Id=tlh.line_item_id");
        query.append(" and li.Journal_Entry_Id='").append(journalEntryId).append("'");
        query.append(" order by tlh.gl_account");
        List result = getCurrentSession().createSQLQuery(query.toString()).list();
        getCurrentSession().flush();
        for (Object row : result) {
            Object[] col = (Object[]) (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setVendorNumber((String) col[0]);
            transaction.setVendorName((String) col[1]);
            transaction.setGlAccount((String) col[2]);
            transaction.setBillOfLadding((String) col[3]);
            transaction.setInvoiceNumber((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setChargeCode((String) col[6]);
            transaction.setFormattedDate((String) col[7]);
            transaction.setFormattedPostedDate((String) col[8]);
            transaction.setDebitAmount(Double.valueOf(Double.parseDouble(col[9].toString())));
            transaction.setCreditAmount(Double.valueOf(Double.parseDouble(col[10].toString())));
            transaction.setArBatchId((Integer) col[11]);
            transaction.setTransactionType((String) col[12]);
            transaction.setLineItemId((String) col[13]);
            transactions.add(transaction);
        }
        return transactions;
    }
}