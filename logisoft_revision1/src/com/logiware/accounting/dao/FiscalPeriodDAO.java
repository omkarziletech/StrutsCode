package com.logiware.accounting.dao;

import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.logiware.accounting.model.AccountModel;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class FiscalPeriodDAO extends BaseHibernateDAO {

    public FiscalYear getFiscalYear(Integer year) {
        Criteria criteria = getCurrentSession().createCriteria(FiscalYear.class);
        criteria.add(Restrictions.eq("year", year));
        criteria.setMaxResults(1);
        return (FiscalYear) criteria.uniqueResult();
    }

    public FiscalYear getCurrentYear() {
        Calendar c = Calendar.getInstance();
        FiscalYear fiscalYear = getFiscalYear(c.get(Calendar.YEAR));
        if (null == fiscalYear) {
            Criteria criteria = getCurrentSession().createCriteria(FiscalYear.class);
            criteria.addOrder(Order.desc("year"));
            criteria.setMaxResults(1);
            fiscalYear = (FiscalYear) criteria.uniqueResult();
        }
        return fiscalYear;
    }

    public List<FiscalYear> getAllFiscalYears() {
        Criteria criteria = getCurrentSession().createCriteria(FiscalYear.class);
        criteria.addOrder(Order.desc("year"));
        return criteria.list();
    }

    public List<FiscalPeriod> getAllFiscalPeriods(Integer year) {
        Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
        criteria.add(Restrictions.eq("year", year));
        criteria.addOrder(Order.asc("startDate"));
        criteria.addOrder(Order.asc("period"));
        return criteria.list();
    }

    public Integer getBudgetSet(Integer year) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  max(budgetset) as budgetSet ");
        queryBuilder.append("from");
        queryBuilder.append("  budget ");
        queryBuilder.append("where");
        queryBuilder.append("  year = '").append(year).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("budgetSet", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }

    public void createYear(Integer year, String startPeriod, String endPeriod) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  create_new_year(");
        queryBuilder.append("    '").append(year).append("',");
        queryBuilder.append("    '").append(startPeriod).append("',");
        queryBuilder.append("    '").append(endPeriod).append("'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public void createBudget(Integer year, Integer budgetSet) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  create_budget(");
        queryBuilder.append("    '").append(year).append("',");
        queryBuilder.append("    '',");
        queryBuilder.append("    '").append(budgetSet).append("'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public String createJournalEntry(FiscalYear fiscalYear, String userName) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  create_year_closing_journal_entry(");
        queryBuilder.append("    '").append(fiscalYear.getYear()).append("',");
        queryBuilder.append("    '").append(fiscalYear.getStartPeriod()).append("',");
        queryBuilder.append("    '").append(fiscalYear.getEndPeriod()).append("',");
        queryBuilder.append("    '").append(userName).append("'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("new_batch_id", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public boolean hasOpenPeriods(Integer year) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  if(isnull(fp.year), 0, 1) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  fiscal_year fy");
        queryBuilder.append("  left join fiscal_period fp");
        queryBuilder.append("    on (");
        queryBuilder.append("      fp.period_dis between fy.start_period and fy.end_period ");
        queryBuilder.append("      and fp.period not in ('AD', 'CL')");
        queryBuilder.append("      and fp.status = 'Open'");
        queryBuilder.append("    ) ");
        queryBuilder.append("where fy.year = ").append(year).append(" ");
        queryBuilder.append("group by fy.year");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean hasOpenSubledgers(FiscalYear fiscalYear) throws Exception {
        FiscalPeriod startPeriod = getFiscalPeriod(fiscalYear.getStartPeriod());
        FiscalPeriod endPeriod = getFiscalPeriod(fiscalYear.getEndPeriod());
        String sDate = DateUtils.formatDate(startPeriod.getStartDate(), "yyyy-MM-dd kk:mm:ss");
        String eDate = DateUtils.formatDate(endPeriod.getEndDate(), "yyyy-MM-dd kk:mm:ss");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  if(sum(rowcount) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    (select ");
        queryBuilder.append("      count(*) as rowcount ");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction_ledger t ");
        queryBuilder.append("    where coalesce(t.posted_date, t.transaction_date) between '").append(sDate).append("' and '").append(eDate).append("'");
        queryBuilder.append("      and t.subledger_source_code = 'PJ' ");
        queryBuilder.append("      and t.status = 'Open') ");
        queryBuilder.append("    union");
        queryBuilder.append("    (select ");
        queryBuilder.append("      count(*) as rowcount ");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction_ledger t ");
        queryBuilder.append("    where t.posted_date between '").append(sDate).append("' and '").append(eDate).append("' ");
        queryBuilder.append("      and t.subledger_source_code like 'AR-%' ");
        queryBuilder.append("      and t.status = 'Open')");
        queryBuilder.append("    union");
        queryBuilder.append("    (select ");
        queryBuilder.append("      count(*) as rowcount ");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction_ledger t ");
        queryBuilder.append("    where t.posted_date between '").append(sDate).append("' and '").append(eDate).append("' ");
        queryBuilder.append("      and t.subledger_source_code in ('RCT', 'NET SETT')");
        queryBuilder.append("      and t.status in ('Open', 'Charge Code'))");
        queryBuilder.append("    union");
        queryBuilder.append("    (select ");
        queryBuilder.append("      count(*) as rowcount ");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction_ledger t ");
        queryBuilder.append("    where t.posted_date between '").append(sDate).append("' and '").append(eDate).append("' ");
        queryBuilder.append("      and t.subledger_source_code = 'CD' ");
        queryBuilder.append("      and t.status = 'PY')");
        queryBuilder.append("  ) as t ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void closeYear(FiscalYear fiscalYear) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  close_fiscal_year(");
        queryBuilder.append("    '").append(fiscalYear.getYear()).append("',");
        queryBuilder.append("    '").append(fiscalYear.getStartPeriod()).append("',");
        queryBuilder.append("    '").append(fiscalYear.getEndPeriod()).append("'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public FiscalPeriod getFiscalPeriod(String period) {
        Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
        criteria.add(Restrictions.eq("periodDis", period));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (FiscalPeriod) criteria.uniqueResult();
    }

    public void saveBudget(String account, Integer year, List<String> budgetSets, List<Double> amounts) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        SQLQuery query;
        for (String budgetSet : budgetSets) {
            queryBuilder.delete(0, queryBuilder.length());
            queryBuilder.append("call");
            queryBuilder.append("  insert_into_budget(");
            queryBuilder.append("    '").append(account).append("',");
            queryBuilder.append("    '").append(year).append("',");
            queryBuilder.append("    '").append(budgetSet).append("',");
            for (Double amount : amounts) {
                queryBuilder.append("    '").append(NumberUtils.formatNumber(amount, "0.00")).append("',");
            }
            queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
            queryBuilder.append("  )");
            query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            query.executeUpdate();
            getCurrentSession().flush();
        }
    }

    public List<AccountModel> getTrialBalances(String startPeriod, String endPeriod, boolean ecuFormat) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  create_trial_balance(");
        queryBuilder.append("    '").append(startPeriod).append("',");
        queryBuilder.append("    '").append(endPeriod).append("',");
        queryBuilder.append("    '").append(ecuFormat ? 1 : 0).append("'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("account", StringType.INSTANCE);
        query.addScalar("description", StringType.INSTANCE);
        query.addScalar("debit", StringType.INSTANCE);
        query.addScalar("credit", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(AccountModel.class));
        return query.list();
    }

    public List<AccountModel> getIncomeStatement(String accountGroup, String startPeriod, String endPeriod, Integer budgerSet) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call");
        queryBuilder.append("  generate_income_statement(");
        queryBuilder.append("    '").append(accountGroup).append("',");
        queryBuilder.append("    '").append(startPeriod).append("',");
        queryBuilder.append("    '").append(endPeriod).append("',");
        queryBuilder.append("    '").append(budgerSet).append("'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("account", StringType.INSTANCE);
        query.addScalar("currentPeriod", StringType.INSTANCE);
        query.addScalar("currentSelectedPeriod", StringType.INSTANCE);
        query.addScalar("currentYear", StringType.INSTANCE);
        query.addScalar("priorYearPeriod", StringType.INSTANCE);
        query.addScalar("priorYear", StringType.INSTANCE);
        query.addScalar("budgetPeriod", StringType.INSTANCE);
        query.addScalar("budgetYear", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(AccountModel.class));
        return query.list();
    }

    public FiscalPeriod getLastClosedPeriod(String periodDis) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
        criteria.add(Restrictions.le("periodDis", periodDis));
        criteria.add(Restrictions.eq("status", "close"));
        criteria.add(Restrictions.ne("period", "AD"));
        criteria.add(Restrictions.ne("period", "CL"));
        criteria.addOrder(Order.desc("periodDis"));
        criteria.setMaxResults(1);
        return (FiscalPeriod) criteria.uniqueResult();
    }

    public FiscalPeriod getPreviousPeriod(String periodDis) {
        Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
        criteria.add(Restrictions.lt("periodDis", periodDis));
        criteria.add(Restrictions.ne("period", "AD"));
        criteria.add(Restrictions.ne("period", "CL"));
        criteria.addOrder(Order.desc("periodDis"));
        criteria.setMaxResults(1);
        return (FiscalPeriod) criteria.uniqueResult();
    }

    public FiscalYear getLastClosedYear(String period) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  f.id as id,");
        queryBuilder.append("  f.year as year,");
        queryBuilder.append("  f.active as active,");
        queryBuilder.append("  f.cls_Period as clsPeriod,");
        queryBuilder.append("  f.adj_Period as adjPeriod,");
        queryBuilder.append("  f.start_period as startPeriod,");
        queryBuilder.append("  f.end_period as endPeriod,");
        queryBuilder.append("  p.period_dis as nextYearPeriod ");
        queryBuilder.append("from");
        queryBuilder.append("  fiscal_year f");
        queryBuilder.append("  join fiscal_period p");
        queryBuilder.append("    on (");
        queryBuilder.append("      p.year >= f.year");
        queryBuilder.append("      and p.period_dis > f.end_period");
        queryBuilder.append("      and p.period not in ('AD', 'CL')");
        queryBuilder.append("    ) ");
        queryBuilder.append("where ");
        queryBuilder.append("  f.end_period <= '").append(period).append("'");
        queryBuilder.append("  and f.active = 'Close' ");
        queryBuilder.append("order by f.year desc, p.period_dis asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setMaxResults(1);
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("year", IntegerType.INSTANCE);
        query.addScalar("active", StringType.INSTANCE);
        query.addScalar("clsPeriod", StringType.INSTANCE);
        query.addScalar("adjPeriod", StringType.INSTANCE);
        query.addScalar("startPeriod", StringType.INSTANCE);
        query.addScalar("endPeriod", StringType.INSTANCE);
        query.addScalar("nextYearPeriod", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(FiscalYear.class));
        return (FiscalYear) query.uniqueResult();
    }

    public List<String> getPeriods(String startPeriod, String endPeriod, boolean includeADCL) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  concat(\"'\", period_dis, \"'\") as period ");
        queryBuilder.append("from");
        queryBuilder.append("  fiscal_period ");
        queryBuilder.append("where");
        queryBuilder.append("  period_dis between '").append(startPeriod).append("' and '").append(endPeriod).append("' ");
        if (!includeADCL) {
            queryBuilder.append("  and period not in ('AD', 'CL') ");
        }
        queryBuilder.append("order by start_date, period");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    public String validateDate(String date, String bankName, String bankAccount) throws Exception {
        date = DateUtils.formatDate(DateUtils.parseDate(date, "MM/dd/yyyy"), "yyyy-MM-dd");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(");
        queryBuilder.append("    count(*) > 0,");
        queryBuilder.append("    if(");
        queryBuilder.append("      fp.`status` = 'Open',");
        queryBuilder.append("      (select ");
        queryBuilder.append("        if(");
        queryBuilder.append("          count(*) > 0,");
        queryBuilder.append("          concat(");
        queryBuilder.append("            'Bank account has been reconciled upto ',");
        queryBuilder.append("            date_format(bd.`last_reconciled_date`, '%m/%d/%Y'),");
        queryBuilder.append("            ', please select different date.'");
        queryBuilder.append("          ),");
        queryBuilder.append("          'Open'");
        queryBuilder.append("        ) ");
        queryBuilder.append("      from");
        queryBuilder.append("        `bank_details` bd ");
        queryBuilder.append("      where bd.`bank_name` = :bankName");
        queryBuilder.append("        and bd.`bank_acct_no` = :bankAccount");
        queryBuilder.append("        and bd.`last_reconciled_date` >= :reconcileDate),");
        queryBuilder.append("      'Please select another period, this period is closed.'");
        queryBuilder.append("    ),");
        queryBuilder.append("    'Please select another period, this period is not yet open.'");
        queryBuilder.append("  ) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  fiscal_period fp ");
        queryBuilder.append("where fp.`start_date` <= :startDate");
        queryBuilder.append("  and fp.`end_date` >= :endDate");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("bankName", bankName);
        query.setString("bankAccount", bankAccount);
        query.setString("reconcileDate", date);
        query.setString("startDate", date + " 00:00:00");
        query.setString("endDate", date + " 23:59:59");
        return (String) query.uniqueResult();
    }
}
