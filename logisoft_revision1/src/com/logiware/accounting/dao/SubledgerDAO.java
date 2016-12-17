package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.logiware.accounting.form.SubledgerForm;
import com.logiware.accounting.model.ResultModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SubledgerDAO extends BaseHibernateDAO implements ConstantsInterface {

    public TransactionLedger findById(Integer id) throws Exception {
	getCurrentSession().flush();
	return (TransactionLedger) getCurrentSession().get(TransactionLedger.class, id);
    }

    public TransactionLedger findById(String id) throws Exception {
	return findById(Integer.parseInt(id));
    }

    public void save(TransactionLedger transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public void update(TransactionLedger persistentInstance) throws Exception {
	getCurrentSession().update(persistentInstance);
    }

    public void delete(TransactionLedger persistentInstance) {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public List<TransactionLedger> getArSubledgers(String blNumber, String manifestFlag) {
	Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
	criteria.add(Restrictions.eq("billLaddingNo", blNumber));
	criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
	criteria.add(Restrictions.like("subledgerSourceCode", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-%"));
	if (CommonUtils.isNotEmpty(manifestFlag)) {
	    criteria.add(Restrictions.eq("manifestFlag", manifestFlag));
	}
	return criteria.list();
    }

    public List<LabelValueBean> getSubledgerTypes() {
	List<LabelValueBean> subledgerTypes = new ArrayList<LabelValueBean>();
	subledgerTypes.add(new LabelValueBean("ALL", ""));
	String query = "select subledger_code from subledger order by subledger_code";
	List<String> result = getCurrentSession().createSQLQuery(query).list();
	for (String subledgerType : result) {
	    subledgerTypes.add(new LabelValueBean(subledgerType, subledgerType));
	}
	return subledgerTypes;
    }

    private String buildSelectExpression(String subledger, String action) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	if (CommonUtils.isEqualIgnoreCase(action, "createExcel")) {
	    queryBuilder.append(" upper(vendor_name) as vendorName,");
	    queryBuilder.append(" upper(vendor_number) as vendorNumber,");
	}
	queryBuilder.append("  upper(subledger_source_code) as subledger,");
        queryBuilder.append("  cast(ar_batch_id as char character set latin1) as arBatchId,");
	queryBuilder.append("  cast(ap_batch_id as char character set latin1) as apBatchId,");
	queryBuilder.append("  upper(bill_ladding_no) as blNumber,");
	queryBuilder.append("  upper(invoice_number) as invoiceNumber,");
	queryBuilder.append("  gl_account_number as glAccount,");
	queryBuilder.append("  upper(charge_code) as chargeCode,");
	queryBuilder.append("  upper(voyage_no) as voyage,");
        queryBuilder.append("  date_format(transaction_date, '%m/%d/%Y') as transactionDate,");
	queryBuilder.append("  date_format(sailing_date, '%m/%d/%Y') as reportingDate,");
	queryBuilder.append("  date_format(posted_date, '%m/%d/%Y') as postedDate,");
	queryBuilder.append("  format(transaction_amt, 2) as amount,");
	queryBuilder.append("  format(debit, 2) as debit,");
	queryBuilder.append("  format(credit, 2) as credit,");
	queryBuilder.append("  upper(transaction_type) as transactionType,");
	queryBuilder.append("  journal_entry_number as journalEntryId,");
	queryBuilder.append("  line_item_number as lineItemId,");
	queryBuilder.append("  id as id");
	if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_PURCHASE_JOURNAL)
		&& CommonUtils.isEqualIgnoreCase(action, "createExcel")) {
	    queryBuilder.append("  ,notes as notes");
	}
	queryBuilder.append(" from (");
	queryBuilder.append(" select");
	if (CommonUtils.isEqualIgnoreCase(action, "createExcel")) {
	    queryBuilder.append(" tl.cust_name as vendor_name,");
	    queryBuilder.append(" tl.cust_no as vendor_number,");
	}
	queryBuilder.append("  tl.subledger_source_code as subledger_source_code,");
	queryBuilder.append("  tl.ar_batch_id as ar_batch_id,");
	queryBuilder.append("  tl.ap_batch_id as ap_batch_id,");
	queryBuilder.append("  tl.bill_ladding_no as bill_ladding_no,");
	queryBuilder.append("  tl.invoice_number as invoice_number,");
	queryBuilder.append("  tl.gl_account_number as gl_account_number,");
	queryBuilder.append("  tl.charge_code as charge_code,");
	queryBuilder.append("  tl.voyage_no as voyage_no,");
	queryBuilder.append("  tl.transaction_date as transaction_date,");
	queryBuilder.append("  tl.sailing_date as sailing_date,");
	queryBuilder.append("  tl.posted_date as posted_date,");
	queryBuilder.append("  tl.transaction_amt as transaction_amt,");
	if (CommonUtils.isNotEmpty(subledger)) {
	    if (CommonUtils.isStartsWith(subledger, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
		queryBuilder.append("  if(");
		queryBuilder.append("    tl.transaction_amt < 0,");
		queryBuilder.append("    - tl.transaction_amt,");
		queryBuilder.append("    0");
		queryBuilder.append("  ) as debit,");
		queryBuilder.append("  if(");
		queryBuilder.append("    tl.transaction_amt > 0,");
		queryBuilder.append("    tl.transaction_amt,");
		queryBuilder.append("    0");
		queryBuilder.append("  ) as credit,");
	    } else {
		queryBuilder.append("  if(");
		queryBuilder.append("    tl.transaction_amt > 0,");
		queryBuilder.append("    tl.transaction_amt,");
		queryBuilder.append("    0");
		queryBuilder.append("  ) as debit,");
		queryBuilder.append("  if(");
		queryBuilder.append("    tl.transaction_amt < 0,");
		queryBuilder.append("    - tl.transaction_amt,");
		queryBuilder.append("    0");
		queryBuilder.append("  ) as credit,");
	    }
	} else {
	    queryBuilder.append("  if(");
	    queryBuilder.append("    (");
	    queryBuilder.append("      tl.subledger_source_code like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%'");
	    queryBuilder.append("      and tl.transaction_amt < 0");
	    queryBuilder.append("    ),");
	    queryBuilder.append("    - tl.transaction_amt,");
	    queryBuilder.append("    if(");
	    queryBuilder.append("      tl.subledger_source_code not like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%'");
	    queryBuilder.append("      and tl.transaction_amt > 0,");
	    queryBuilder.append("      tl.transaction_amt,");
	    queryBuilder.append("      0");
	    queryBuilder.append("    )");
	    queryBuilder.append("  ) as debit,");
	    queryBuilder.append("  if(");
	    queryBuilder.append("    (");
	    queryBuilder.append("      tl.subledger_source_code like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%'");
	    queryBuilder.append("      and tl.transaction_amt > 0");
	    queryBuilder.append("    ),");
	    queryBuilder.append("    tl.transaction_amt,");
	    queryBuilder.append("    if(");
	    queryBuilder.append("      tl.subledger_source_code not like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%'");
	    queryBuilder.append("      and tl.transaction_amt < 0,");
	    queryBuilder.append("      - tl.transaction_amt,");
	    queryBuilder.append("      0");
	    queryBuilder.append("    )");
	    queryBuilder.append("  ) as credit,");
	}
	queryBuilder.append("  tl.transaction_type as transaction_type,");
	queryBuilder.append("  tl.journal_entry_number as journal_entry_number,");
	queryBuilder.append("  tl.line_item_number as line_item_number,");
	queryBuilder.append("  cast(tl.transaction_id as char character set latin1) as id");
	if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_PURCHASE_JOURNAL)
		&& CommonUtils.isEqualIgnoreCase(action, "createExcel")) {
	    queryBuilder.append(", ap.notes as notes");
	}
	return queryBuilder.toString();
    }

    private String buildAccWhereCondition(String fromDate, String toDate) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append(" tl.subledger_source_code = '").append(SUB_LEDGER_CODE_ACCRUALS).append("'");
	queryBuilder.append(" and tl.sailing_date between '").append(fromDate).append("'");
	queryBuilder.append(" and '").append(toDate).append("'");
	queryBuilder.append(" and (");
	queryBuilder.append("  tl.status in (");
	queryBuilder.append("    '").append(STATUS_OPEN).append("',");
	queryBuilder.append("    '").append(STATUS_PENDING).append("',");
	queryBuilder.append("    '").append(STATUS_IN_PROGRESS).append("',");
	queryBuilder.append("    '").append(STATUS_EDI_IN_PROGRESS).append("',");
	queryBuilder.append("    '").append(STATUS_DISPUTE).append("',");
	queryBuilder.append("    '").append(STATUS_EDI_DISPUTE).append("'");
	queryBuilder.append("  )");
	queryBuilder.append("  or (");
	queryBuilder.append("    tl.status in (");
	queryBuilder.append("      '").append(STATUS_ASSIGN).append("',");
	queryBuilder.append("      '").append(STATUS_EDI_ASSIGNED).append("'");
	queryBuilder.append("    )");
	queryBuilder.append("    and tl.posted_date > '").append(toDate).append("'");
	queryBuilder.append("  )");
	queryBuilder.append(")");
	return queryBuilder.toString();
    }

    private String buildWhereCondition(SubledgerForm subledgerForm) throws Exception {
	String fromDate = DateUtils.formatDate(DateUtils.parseDate(subledgerForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
	String toDate = DateUtils.formatDate(DateUtils.parseDate(subledgerForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append(" from transaction_ledger tl");
	if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), SUB_LEDGER_CODE_PURCHASE_JOURNAL)
		&& CommonUtils.isEqualIgnoreCase(subledgerForm.getAction(), "createExcel")) {
	    queryBuilder.append(" left join ap_invoice ap");
	    queryBuilder.append(" on (tl.cust_no = ap.account_number");
	    queryBuilder.append(" and tl.invoice_number = ap.invoice_number");
	    queryBuilder.append(" and ap.status = 'AP'");
	    queryBuilder.append(")");
	}
	queryBuilder.append(" where (");
	if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), SUB_LEDGER_CODE_ACCRUALS)) {
	    String accWhereCondition = buildAccWhereCondition(fromDate, toDate);
	    queryBuilder.append(accWhereCondition);
	} else if (CommonUtils.isNotEmpty(subledgerForm.getSubledger())) {
	    queryBuilder.append(" tl.subledger_source_code = '").append(subledgerForm.getSubledger()).append("'");
	    queryBuilder.append(" and tl.posted_date between '").append(fromDate).append("'");
	    queryBuilder.append(" and '").append(toDate).append("'");
	    queryBuilder.append(" and tl.status in (");
	    if (subledgerForm.isPosted()) {
		queryBuilder.append("  '").append(STATUS_POSTED_TO_GL).append("'");
		if (CommonUtils.in(subledgerForm.getSubledger(), SUB_LEDGER_CODE_RCT, SUB_LEDGER_CODE_NET_SETT)) {
		    queryBuilder.append("  ,'").append(STATUS_CHARGE_CODE_POSTED).append("'");
		}
	    } else if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), SUB_LEDGER_CODE_CASH_DEPOSIT)) {
		queryBuilder.append("  '").append(STATUS_PAID).append("'");
	    } else {
		queryBuilder.append("  '").append(STATUS_OPEN).append("'");
		if (CommonUtils.in(subledgerForm.getSubledger(), SUB_LEDGER_CODE_RCT, SUB_LEDGER_CODE_NET_SETT)) {
		    queryBuilder.append("  ,'").append(STATUS_CHARGE_CODE).append("'");
		}
	    }
	    queryBuilder.append(")");
	} else {
	    queryBuilder.append("(");
	    if (CommonUtils.isEqualIgnoreCase(subledgerForm.getAction(), "createExcel")) {
		if (subledgerForm.isAccruals()) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(DateUtils.parseDate(subledgerForm.getToDate(), "MM/dd/yyyy"));
		    calendar.set(Calendar.DAY_OF_MONTH, 1);
		    calendar.add(Calendar.MONTH, -11);
		    String accFromDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd 00:00:00");
		    String accWhereCondition = buildAccWhereCondition(accFromDate, toDate);
		    queryBuilder.append("  (");
		    queryBuilder.append(accWhereCondition);
		    queryBuilder.append("  )");
		    queryBuilder.append("  or");
		}
	    } else {
		String accWhereCondition = buildAccWhereCondition(fromDate, toDate);
		queryBuilder.append("  (");
		queryBuilder.append(accWhereCondition);
		queryBuilder.append("  )");
		queryBuilder.append("  or");
	    }
	    queryBuilder.append("  (");
	    queryBuilder.append("    tl.subledger_source_code != '").append(SUB_LEDGER_CODE_ACCRUALS).append("'");
	    queryBuilder.append("    and tl.posted_date between '").append(fromDate).append("'");
	    queryBuilder.append("    and '").append(toDate).append("'");
	    queryBuilder.append("    and tl.status in (");
	    if (subledgerForm.isPosted()) {
		queryBuilder.append("      '").append(STATUS_POSTED_TO_GL).append("',");
		queryBuilder.append("      '").append(STATUS_CHARGE_CODE_POSTED).append("'");
	    } else {
		queryBuilder.append("      '").append(STATUS_OPEN).append("',");
		queryBuilder.append("      '").append(STATUS_CHARGE_CODE).append("',");
		queryBuilder.append("      '").append(STATUS_PAID).append("'");
	    }
	    queryBuilder.append("    )");
	    queryBuilder.append("  )");
	    queryBuilder.append(")");
	}
	if (CommonUtils.isEqualIgnoreCase(subledgerForm.getAction(), "validateGlAccounts")) {
	    queryBuilder.append(" and (");
	    queryBuilder.append("   tl.gl_account_number is null");
	    queryBuilder.append("   or tl.gl_account_number = ''");
	    queryBuilder.append("   or tl.gl_account_number not in");
	    queryBuilder.append(" (select ");
	    queryBuilder.append("   account");
	    queryBuilder.append(" from");
	    queryBuilder.append("   account_details)");
	    queryBuilder.append(")");
	} else if (CommonUtils.isNotEmpty(subledgerForm.getSearchBy())) {
	    if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSearchBy(), "blank_gl")) {
		queryBuilder.append(" and (");
		queryBuilder.append("   tl.gl_account_number is null");
		queryBuilder.append("   or tl.gl_account_number = ''");
		queryBuilder.append(")");
	    } else if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSearchBy(), "gl_not_in_coa")) {
		queryBuilder.append(" and tl.gl_account_number != ''");
		queryBuilder.append(" and tl.gl_account_number not in");
		queryBuilder.append(" (select ");
		queryBuilder.append("   account");
		queryBuilder.append(" from");
		queryBuilder.append("   account_details)");
	    } else if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSearchBy(), "gl_account_number")) {
		queryBuilder.append(" and tl.gl_account_number = '").append(subledgerForm.getGlAccount()).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSearchBy(), "transaction_amt")) {
		if (CommonUtils.isNotEmpty(subledgerForm.getFromAmount())) {
		    queryBuilder.append(" and tl.transaction_amt >= ").append(subledgerForm.getFromAmount());
		}
		if (CommonUtils.isNotEmpty(subledgerForm.getToAmount())) {
		    queryBuilder.append(" and tl.transaction_amt <= ").append(subledgerForm.getToAmount());
		}
	    } else {
		queryBuilder.append(" and tl.").append(subledgerForm.getSearchBy());
		queryBuilder.append(" like '%").append(subledgerForm.getSearchValue()).append("%'");
	    }
	}
	queryBuilder.append(")");
	return queryBuilder.toString();
    }

    private Integer getTotalRows(String whereCondition) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select count(*)").append(whereCondition);
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return null != result ? Integer.parseInt(result.toString()) : 0;
    }

    private List<ResultModel> getResults(String subledger, String action, String whereCondition, String sortBy, String orderBy, int start, int limit) {
	StringBuilder queryBuilder = new StringBuilder();
	String selectExpression = buildSelectExpression(subledger, action);
	queryBuilder.append(selectExpression);
	queryBuilder.append(whereCondition);
	if (CommonUtils.isEqualIgnoreCase(action, "createExcel")) {
	    queryBuilder.append(" group by tl.transaction_id");
	}
	if (limit > 0) {
	    queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
	    queryBuilder.append(" limit ").append(start).append(", ").append(limit);
	}
	queryBuilder.append(") as t");
	queryBuilder.append(" group by id");
        if (limit > 0) {
            queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        }
	SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryBuilder.toString());
	sqlQuery.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
	return sqlQuery.list();
    }

    public void search(SubledgerForm subledgerForm) throws Exception {
	String whereCondition = buildWhereCondition(subledgerForm);
	if (CommonUtils.in(subledgerForm.getAction(), "createExcel", "validateGlAccounts")) {
	    List<ResultModel> results = getResults(subledgerForm.getSubledger(), subledgerForm.getAction(), whereCondition, null, null, 0, 0);
	    subledgerForm.setResults(results);
	} else {
	    int totalRows = getTotalRows(whereCondition);
	    if (totalRows > 0) {
		subledgerForm.setTotalRows(totalRows);
		int limit = subledgerForm.getLimit();
		int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
		subledgerForm.setTotalPages(totalPages);
		int start = limit * (subledgerForm.getSelectedPage() - 1);
		String subledger = subledgerForm.getSubledger();
		String sortBy = subledgerForm.getSortBy();
		String orderBy = subledgerForm.getOrderBy();
		List<ResultModel> results = getResults(subledger, subledgerForm.getAction(), whereCondition, sortBy, orderBy, start, limit);
		subledgerForm.setResults(results);
		subledgerForm.setSelectedRows(results.size());
	    }
	}
    }

    public boolean validateArBatches(SubledgerForm subledgerForm) throws Exception {
	String fromDate = DateUtils.formatDate(DateUtils.parseDate(subledgerForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	String toDate = DateUtils.formatDate(DateUtils.parseDate(subledgerForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  if(count(*) > 0, true, false) as result");
	queryBuilder.append(" from ar_batch");
	queryBuilder.append(" where status = '").append(STATUS_OPEN).append("'");
	queryBuilder.append(" and deposit_date between '").append(fromDate).append("'");
	queryBuilder.append(" and '").append(toDate).append("'");
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
	query.addScalar("result", BooleanType.INSTANCE);
	return (Boolean) query.uniqueResult();

    }

    public void updateGlAccount(String glAccount, String id) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("update");
	queryBuilder.append("  transaction_ledger");
	queryBuilder.append(" set gl_account_number = '").append(glAccount).append("'");
	queryBuilder.append(" where transaction_id = ").append(id);
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void post(SubledgerForm subledgerForm, User user) throws Exception {
	String fromDate = DateUtils.formatDate(DateUtils.parseDate(subledgerForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
	String toDate = DateUtils.formatDate(DateUtils.parseDate(subledgerForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
	String subledger = subledgerForm.getSubledger();

	getCurrentSession().createSQLQuery("set session group_concat_max_len=12*128*128*1024").executeUpdate();

	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("call ");
	if (CommonUtils.isStartsWith(subledger, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
	    queryBuilder.append("post_ar_subledger('").append(subledger).append("',");
	} else if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_ACCRUALS)) {
	    queryBuilder.append("post_acc_subledger(");
	} else if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_CASH_DEPOSIT)) {
	    queryBuilder.append("post_cd_subledger(");
	} else if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_NET_SETT)) {
	    queryBuilder.append("post_ns_subledger(");
	} else if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
	    queryBuilder.append("post_pj_subledger(");
	} else if (CommonUtils.isEqualIgnoreCase(subledger, SUB_LEDGER_CODE_RCT)) {
	    queryBuilder.append("post_rct_subledger(");
	}
	queryBuilder.append("'").append(fromDate).append("',");
	queryBuilder.append("'").append(toDate).append("',");
	queryBuilder.append("'").append(user.getLoginName()).append("'");
	queryBuilder.append(")");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }
}
