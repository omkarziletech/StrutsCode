package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.logiware.accounting.form.ChartOfAccountsForm;
import com.logiware.accounting.model.AccountModel;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ChartOfAccountsDAO extends BaseHibernateDAO {

    public List<String> getAccountTypes() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  acct_type ");
        queryBuilder.append("from");
        queryBuilder.append("  account_group ");
        queryBuilder.append("group by acct_type");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    public List<String> getAccountGroups() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  acct_group ");
        queryBuilder.append("from");
        queryBuilder.append("  account_group ");
        queryBuilder.append("group by acct_group");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    private String buildSelectExpression() {
        StringBuilder whereBuilder = new StringBuilder();
        whereBuilder.append("select");
        whereBuilder.append("  account as account,");
        whereBuilder.append("  ucase(acct_desc) as description,");
        whereBuilder.append("  ucase(normal_balance) as normalBalance,");
        whereBuilder.append("  ucase(multi_currency) as multiCurrency,");
        whereBuilder.append("  ucase(acct_status) as status,");
        whereBuilder.append("  ucase(acct_type) as accountType,");
        whereBuilder.append("  ucase(acct_group) as accountGroup,");
        whereBuilder.append("  if((control_acct = '' or isnull(control_acct)), 'NO', 'YES') as controlAccount ");
        return whereBuilder.toString();
    }

    private String buildWhereClause(ChartOfAccountsForm form) {
        StringBuilder whereBuilder = new StringBuilder();
        whereBuilder.append("from");
        whereBuilder.append("  account_details ");
        whereBuilder.append("where");
        whereBuilder.append("  acct_status = '").append(form.getAccountStatus()).append("'");
        if (CommonUtils.isNotEmpty(form.getStartAccount()) && CommonUtils.isNotEmpty(form.getEndAccount())) {
            whereBuilder.append("  and account between '").append(form.getStartAccount()).append("' and '").append(form.getEndAccount()).append("'");
        } else if (CommonUtils.isNotEmpty(form.getStartAccount())) {
            whereBuilder.append("  and account = '").append(form.getStartAccount()).append("'");
        } else if (CommonUtils.isNotEmpty(form.getEndAccount())) {
            whereBuilder.append("  and account = '").append(form.getEndAccount()).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getAccountType())) {
            whereBuilder.append("  and acct_type = '").append(form.getAccountType()).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getAccountGroup())) {
            whereBuilder.append("  and acct_group = '").append(form.getAccountGroup()).append("'");
        }
        return whereBuilder.toString();
    }

    private Integer getTotalRows(String whereClause) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  count(*) as result ");
        queryBuilder.append(whereClause);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }

    public List<AccountModel> getAccounts(String whereClause, String sortBy, String orderBy, Integer start, Integer limit) {
        StringBuilder queryBuilder = new StringBuilder();
        String selectExpression = buildSelectExpression();
        queryBuilder.append(selectExpression);
        queryBuilder.append(whereClause);
        queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (null != start) {
            query.setFirstResult(start);
        }
        if (null != limit) {
            query.setMaxResults(limit);
        }
        query.addScalar("account", StringType.INSTANCE);
        query.addScalar("description", StringType.INSTANCE);
        query.addScalar("normalBalance", StringType.INSTANCE);
        query.addScalar("multiCurrency", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("accountType", StringType.INSTANCE);
        query.addScalar("accountGroup", StringType.INSTANCE);
        query.addScalar("controlAccount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(AccountModel.class));
        return query.list();
    }

    public void search(ChartOfAccountsForm form) {
        String whereClause = buildWhereClause(form);
        int totalRows = getTotalRows(whereClause);
        if (totalRows > 0) {
            int limit = form.getLimit();
            int start = limit * (form.getSelectedPage() - 1);
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            List<AccountModel> accounts = getAccounts(whereClause, form.getSortBy(), form.getOrderBy(), start, limit);
            form.setTotalPages(totalPages);
            form.setTotalRows(totalRows);
            form.setSelectedRows(accounts.size());
            form.setAccounts(accounts);
        }
    }

    public String buildTransactionsSelect() {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("select");
        selectBuilder.append("  l.account as account,");
        selectBuilder.append("  j.period as period,");
        selectBuilder.append("  date_format(l.date, '%m/%d/%Y') as date,");
        selectBuilder.append("  ucase(s.code) as sourceCode,");
        selectBuilder.append("  j.journal_entry_id as reference,");
        selectBuilder.append("  ucase(j.journal_entry_desc) as description,");
        selectBuilder.append("  format(l.debit, 2) as debit,");
        selectBuilder.append("  format(l.credit, 2) as credit,");
        selectBuilder.append("  format(l.debit - l.credit, 2) as netChange ");
        return selectBuilder.toString();
    }

    public String buildTransactionsWhere(String startAccount, String endAccount, List<String> periods) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from");
        queryBuilder.append("  line_item l ");
        queryBuilder.append("  join journal_entry j ");
        queryBuilder.append("    on (l.journal_entry_id = j.journal_entry_id) ");
        queryBuilder.append("  join batch b ");
        queryBuilder.append("    on (");
        queryBuilder.append("      j.batch_id = b.batch_id ");
        queryBuilder.append("      and b.status = 'Posted'");
        queryBuilder.append("    ) ");
        queryBuilder.append("  join fiscal_period p ");
        queryBuilder.append("    on (j.period = p.period_dis) ");
        queryBuilder.append("  join genericcode_dup s ");
        queryBuilder.append("    on (j.source_code = s.id) ");
        queryBuilder.append("where");
        if (CommonUtils.isNotEmpty(endAccount)) {
            queryBuilder.append("  l.account between '").append(startAccount).append("' and '").append(endAccount).append("'");
        } else {
            queryBuilder.append("  l.account = '").append(startAccount).append("'");
        }
        if (periods.size() > 1) {
            queryBuilder.append("  and j.period in (").append(periods.toString().replace("[", "").replace("]", "")).append(") ");
        } else {
            queryBuilder.append("  and j.period = ").append(periods.get(0)).append(" ");
        }
        return queryBuilder.toString();
    }

    public List<AccountModel> getTransactions(String whereClause) {
        StringBuilder queryBuilder = new StringBuilder();
        String selectExpression = buildTransactionsSelect();
        queryBuilder.append(selectExpression);
        queryBuilder.append(whereClause).append(" ");
        queryBuilder.append("order by l.account,");
        queryBuilder.append("  p.start_date,");
        queryBuilder.append("  l.date,");
        queryBuilder.append("  l.line_item_id ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("account", StringType.INSTANCE);
        query.addScalar("period", StringType.INSTANCE);
        query.addScalar("date", StringType.INSTANCE);
        query.addScalar("sourceCode", StringType.INSTANCE);
        query.addScalar("reference", StringType.INSTANCE);
        query.addScalar("description", StringType.INSTANCE);
        query.addScalar("debit", StringType.INSTANCE);
        query.addScalar("credit", StringType.INSTANCE);
        query.addScalar("netChange", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(AccountModel.class));
        return query.list();
    }

    public void searchTransactions(ChartOfAccountsForm form) {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
        FiscalYear fiscalYear = fiscalPeriodDAO.getLastClosedYear(form.getStartPeriod());
        Integer sPeriod = Integer.parseInt(form.getStartPeriod());
        Integer ePeriod = Integer.parseInt(fiscalYear.getEndPeriod());
        List<String> periods = fiscalPeriodDAO.getPeriods(form.getStartPeriod(), form.getEndPeriod(), ePeriod <= sPeriod);
        FiscalPeriod previousPeriod = fiscalPeriodDAO.getPreviousPeriod(form.getStartPeriod());
        Map<String, Double> closingBalances = accountBalanceDAO.getClosingBalances(form.getStartAccount(), form.getEndAccount(), previousPeriod.getPeriodDis());
        form.setClosingBalances(closingBalances);
        String whereClause = buildTransactionsWhere(form.getStartAccount(), form.getEndAccount(), periods);
        List<AccountModel> transactions = getTransactions(whereClause);
        form.setAccounts(transactions);
        form.setTotalRows(transactions.size());
    }
}
