package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.accthistorybean;
import com.gp.cvst.logisoft.beans.comparisonbean;
import com.gp.cvst.logisoft.domain.AccountBalance;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.bean.AccountsBean;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class AccountBalance.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.AccountBalance
 * @author MyEclipse - Hibernate Tools
 */
/**
 * @author user
 *
 */
public class AccountBalanceDAO extends BaseHibernateDAO {

    private Logger log = Logger.getLogger(AccountBalanceDAO.class);
    NumberFormat number = new DecimalFormat("###,###,##0.00");

    public void save(AccountBalance transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(AccountBalance persistentInstance) throws Exception {
        getSession().update(persistentInstance);
        getSession().flush();
    }

    public void delete(AccountBalance persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public AccountBalance findById(java.lang.Integer id) throws Exception {
        AccountBalance instance = (AccountBalance) getSession().get("com.gp.cvst.logisoft.hibernate.dao.AccountBalance", id);
        return instance;
    }

    public AccountBalance getAccountBalance(String account, String year, String period) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AccountBalance.class);
        criteria.add(Restrictions.eq("account", account));
        criteria.add(Restrictions.eq("year", year));
        criteria.add(Restrictions.eq("period", period));
        criteria.setMaxResults(1);
        return (AccountBalance) criteria.uniqueResult();
    }

    public List findByExample(AccountBalance instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.AccountBalance").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from AccountBalance as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public AccountBalance merge(AccountBalance detachedInstance) throws Exception {
        AccountBalance result = (AccountBalance) getSession().merge(detachedInstance);
        log.debug("merge successful");
        return result;
    }

    public void attachDirty(AccountBalance instance) throws Exception {
        getSession().saveOrUpdate(instance);
    }

    public void attachClean(AccountBalance instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findforSearch(String year, String account) throws Exception {
        accthistorybean objChartCodeBean = null;
        List<accthistorybean> acclist = new ArrayList<accthistorybean>();
        String queryString = "";
        int y = Integer.parseInt(year);
        y = y - 1;
        String queryString1 = "select ayeb.closingBalance from AccountYearEndBalance ayeb where ayeb.year='" + y + "' and ayeb.account='" + account + "'";
        List qo = getCurrentSession().createQuery(queryString1).list();
        String re = null;
        double p = 0;
        try {
            p = (Double) qo.get(0);
            re = Double.toString(p);

        } catch (IndexOutOfBoundsException e) {
            p = 0.0;
            re = "0.00";
            log.info("Exception in findforSearch: ", e);
        }
        if (re != null) {
            objChartCodeBean = new accthistorybean();
            String periodbalance = re;
            p = Double.parseDouble(periodbalance);

            String period = "Beg";
            String enddate = "01/01/" + year;
            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setEnddate(enddate);
            objChartCodeBean.setPeriodbalance(number.format(p));
            acclist.add(objChartCodeBean);
        }
        queryString = "select acbal.period,fiscal.endDate,acbal.periodBalance from AccountBalance acbal,FiscalPeriod fiscal where acbal.year='" + year + "'and acbal.year=fiscal.year and acbal.period=fiscal.period and acbal.account='" + account + "'";
        List queryObject;
        Iterator iter = null;
        queryObject = getCurrentSession().createQuery(queryString).list();
        iter = queryObject.iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        while (iter.hasNext()) {
            objChartCodeBean = new accthistorybean();
            Object[] row = (Object[]) iter.next();
            String period = (String) row[0];

            Date edate = (Date) row[1];
            String enddate = sdf.format(edate);

            Double periodbalance = (Double) row[2];
            if (periodbalance == null) {
                periodbalance = new Double(0);
            }

            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setEnddate(enddate);
            objChartCodeBean.setPeriodbalance(number.format(periodbalance.doubleValue()));

            acclist.add(objChartCodeBean);

        }

        return acclist;
    }

    public List findforSearch1(String account, String year1, String year2) throws Exception {

        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        String queryString = "";
        String queryString1 = "";
        queryString = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year1 + "'";
        queryString1 = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        NumberFormat number = new DecimalFormat("00");
        NumberFormat number1 = new DecimalFormat("###,###,##0.00");
        double i = 1;
        while (iter.hasNext() && iter1.hasNext()) {
            if (i > 12) {
                break;
            }
            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            double b = (Double) row1;
            objChartCodeBean.setPeriod(number.format(i));
            objChartCodeBean.setActuals(number1.format(a));
            objChartCodeBean.setBudget(number1.format(b));
            acclist.add(objChartCodeBean);
            i++;
            closeSession();

        }
        return acclist;
    }

    public List findforSearch11(String account, String year1, String year2) throws Exception {
        NumberFormat number1 = new DecimalFormat("###,###,##0.00");
        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        int y1 = Integer.parseInt(year1);
        y1 = y1 - 1;
        String queryString11 = "select ayeb.closingBalance from AccountYearEndBalance ayeb where ayeb.year='" + y1 + "' and ayeb.account='" + account + "'";
        List qo1 = getCurrentSession().createQuery(queryString11).list();
        double re1 = 0.0;
        try {
            double p = (Double) qo1.get(0);
            re1 = p;
        } catch (IndexOutOfBoundsException e) {
            re1 = 0.00;
            log.info("Exception in findforSearch11: ", e);
        }
        int y2 = Integer.parseInt(year2);
        y2 = y2 - 1;
        String queryString12 = "select ayeb.closingBalance from AccountYearEndBalance ayeb where ayeb.year='" + y2 + "' and ayeb.account='" + account + "'";
        List qo2 = getCurrentSession().createQuery(queryString12).list();
        double re2 = 0.0;
        try {
            double p = (Double) qo2.get(0);
            re2 = p;
        } catch (IndexOutOfBoundsException e) {
            re2 = 0.00;
            log.info("Exception in findforSearch11: ", e);
        }
        String queryString = "";
        String queryString1 = "";
        queryString = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year1 + "'";
        queryString1 = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        int i = 1;
        double r1 = re1;
        double r2 = re2;
        while (iter.hasNext() && iter1.hasNext()) {
            if (i > 12) {
                break;
            }
            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            r1 = r1 + a;
            double b = (Double) row1;
            r2 = r2 + b;
            objChartCodeBean.setPeriod(Integer.toString(i));
            objChartCodeBean.setActuals(number1.format(r1));
            objChartCodeBean.setBudget(number1.format(r2));
            acclist.add(objChartCodeBean);
            i++;
        }
        return acclist;
    }

    public List getAccountNumber(String account, String period, String year) throws Exception {
        String queryString = "from AccountBalance where account=?0 and period=?1 and year=?2";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", account);
        queryObject.setParameter("1", period);
        queryObject.setParameter("2", year);
        return queryObject.list();
    }

    public List getAccountNumber1(String account) throws Exception {
        String queryString = "from AccountBalance where account=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", account);
        return queryObject.list();
    }

    public List getYearClosingAmountAndAccount(int year) throws Exception {
        String queryString = "select sum(period_balance)+closing_balance as year_closing_amount,account.account from account_details  account left join account_year_end_balance yearend on(account.account=yearend.account), account_balance balance where yearend.year='" + year + "' "
                + " and account.acct_type='Income Statement' and balance.year='" + (++year) + "' and balance.account=account.Account and balance.period!='CL' group by account ";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("year_closing_amount", DoubleType.INSTANCE).addScalar("account", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public void updateAcountBalanceForPeriodAndYear(AccountBalance accountBalance) throws Exception {
        String query = "update AccountBalance accBal set accBal.totalDebit=" + accountBalance.getTotalDebit() + "+accBal.totalDebit, accBal.toatlCredit=" + accountBalance.getToatlCredit() + "+accBal.toatlCredit, "
                + " periodBalance = accBal.totalDebit-accBal.toatlCredit where "
                + " accBal.account='" + accountBalance.getAccount() + "' and accBal.period='" + accountBalance.getPeriod() + "' and accBal.year='" + accountBalance.getYear() + "'";
        Query queryObject = getCurrentSession().createQuery(query);
        queryObject.executeUpdate();
    }

    public List getAccountAndBalanceForYearPeriodAndGroup(Integer year, Integer fromPeriod, Integer toPeriod, String group) throws Exception {
        String fromPeriodAsString = "" + fromPeriod;
        String toPeriodAsString = "" + toPeriod;
        if (null != fromPeriodAsString && fromPeriodAsString.length() == 1) {
            fromPeriodAsString = "0" + fromPeriod;
        }
        if (null != toPeriodAsString && toPeriodAsString.length() == 1) {
            toPeriodAsString = "0" + toPeriod;
        }
        String queryString = "select ad.acct_desc, sum(total_debit)-sum(toatl_credit) as balance,ad.normal_balance  from  account_balance ab, account_details ad where "
                + " ab.account = ad.account and ad.acct_group='" + group + "' and ab.Period between '" + fromPeriodAsString + "' and '" + toPeriodAsString + "' "
                + " and ab.year='" + year + "' group by ad.acct_desc";

        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("acct_desc", StringType.INSTANCE).addScalar("balance", DoubleType.INSTANCE).addScalar("normal_balance", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    /**
     * @param year
     * @param fromPeriod
     * @param toPeriod
     * @param group
     * @return For Column current period of IncomeStatement Report.
     */
    public List getAccountAndBalanceForYearEndPeriodAndGroup(Integer year, Integer fromPeriod, Integer toPeriod, String group, String constant) throws Exception {
        String fromPeriodAsString = "" + fromPeriod;
        String toPeriodAsString = "" + toPeriod;
        if (null != fromPeriodAsString && fromPeriodAsString.length() == 1) {
            fromPeriodAsString = "0" + fromPeriod;
        }

        if (null != toPeriodAsString && toPeriodAsString.length() == 1) {
            toPeriodAsString = "0" + toPeriod;
        }
        String queryString = "";
        if (null != constant && ReportConstants.INCOME_STATEMENT_CURRENT_PERIOD.equals(constant)) {
            queryString = "select ad.acct_desc, sum(total_debit)-sum(toatl_credit) as balance,ad.normal_balance  from  account_balance ab, account_details ad where "
                    + " ab.account = ad.account and ad.acct_group='" + group + "' and ab.Period ='" + toPeriodAsString + "' "
                    + " and ab.year='" + year + "' group by ad.acct_desc";
        } else {
            if (null != constant && ReportConstants.INCOME_STATEMENT_PREVIOUS_YEAR.equals(constant)) {
                queryString = "select ad.acct_desc, sum(total_debit)-sum(toatl_credit) as balance,ad.normal_balance  from  account_balance ab, account_details ad where "
                        + " ab.account = ad.account and ad.acct_group='" + group + "' "
                        + " and ab.year='" + year + "' group by ad.acct_desc";
            }
        }
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("acct_desc", StringType.INSTANCE).addScalar("balance", DoubleType.INSTANCE).addScalar("normal_balance", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public List getAccountAndBalanceFromBudget(Integer year, Integer fromPeriod, Integer toPeriod, String group, String constant, String budgetSet) throws Exception {
        String fromPeriodAsString = "" + fromPeriod;
        String toPeriodAsString = "" + toPeriod;
        if (null != fromPeriodAsString && fromPeriodAsString.length() == 1) {
            fromPeriodAsString = "0" + fromPeriod;
        }
        if (null != toPeriodAsString && toPeriodAsString.length() == 1) {
            toPeriodAsString = "0" + toPeriod;
        }
        String queryString = "";
        if (null != constant && ReportConstants.INCOME_STATEMENT_BUDGET_YTD.equals(constant)) {
            queryString = "select ad.acct_desc, sum(budgetamount) as balance,ad.normal_balance  from  budget ab, account_details ad where "
                    + " ab.account = ad.account and ab.budgetset='" + budgetSet + "' and ad.acct_group='" + group + "' and ab.Period between '" + fromPeriodAsString + "' and '" + toPeriodAsString + "'"
                    + " and ab.year='" + year + "' group by ad.acct_desc";
        } else {
            if (null != constant && ReportConstants.INCOME_STATEMENT_BUDGET_ANNUAL.equals(constant)) {
                queryString = "select ad.acct_desc, sum(budgetamount) as balance,ad.normal_balance  from  budget ab, account_details ad where "
                        + " ab.account = ad.account and ad.acct_group='" + group + "' "
                        + " and ab.year='" + year + "' group by ad.acct_desc";
            }
        }

        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("acct_desc", StringType.INSTANCE).addScalar("balance", DoubleType.INSTANCE).addScalar("normal_balance", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public Double getBalanceForYearPeriodAndGroup(Integer year, Integer fromPeriod, Integer toPeriod, String group) throws Exception {
        String fromPeriodAsString = "" + fromPeriod;
        String toPeriodAsString = "" + toPeriod;
        if (null != fromPeriodAsString && fromPeriodAsString.length() == 1) {
            fromPeriodAsString = "0" + fromPeriod;
        }
        if (null != toPeriodAsString && toPeriodAsString.length() == 1) {
            toPeriodAsString = "0" + toPeriod;
        }
        String queryString = "select sum(total_debit)-sum(toatl_credit) as balance from  account_balance ab, account_details ad where "
                + " ab.account = ad.account and ad.acct_group='" + group + "' and ab.Period between '" + fromPeriodAsString + "' and '" + toPeriodAsString + "' "
                + " and ab.year='" + year + "'";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("balance", DoubleType.INSTANCE);
        Object value = queryObject.uniqueResult();
        return null != value ? (Double) value : 0d;
    }

    public List getBalanceForYearPeriodAndGroupAccount(Integer year, Integer fromPeriod, Integer toPeriod, String group) throws Exception {
        String fromPeriodAsString = "" + fromPeriod;
        String toPeriodAsString = "" + toPeriod;
        if (null != fromPeriodAsString && fromPeriodAsString.length() == 1) {
            fromPeriodAsString = "0" + fromPeriod;
        }
        if (null != toPeriodAsString && toPeriodAsString.length() == 1) {
            toPeriodAsString = "0" + toPeriod;
        }
        String queryString = "select sum(total_debit)-sum(toatl_credit) as balance,ad.Acct_Desc from  account_balance ab, account_details ad where "
                + " ab.account = ad.account and ad.acct_group='" + group + "' and ab.Period between '" + fromPeriodAsString + "' and '" + toPeriodAsString + "' "
                + " and ab.year='" + year + "' group by ab.account";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("balance", DoubleType.INSTANCE).addScalar("Acct_Desc", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public Double getOtherOperatingExpenses(Integer year, Integer fromPeriod, Integer toPeriod, String group) throws Exception {
        String fromPeriodAsString = "" + fromPeriod;
        String toPeriodAsString = "" + toPeriod;
        if (null != fromPeriodAsString && fromPeriodAsString.length() == 1) {
            fromPeriodAsString = "0" + fromPeriod;
        }
        if (null != toPeriodAsString && toPeriodAsString.length() == 1) {
            toPeriodAsString = "0" + toPeriod;
        }
        String queryString = "select sum(total_debit)-sum(toatl_credit) as balance from  account_balance ab, account_details ad where "
                + " ab.account = ad.account and ad.acct_group='" + group + "' and ab.Period between '" + fromPeriodAsString + "' and '" + toPeriodAsString + "' "
                + " and ab.year='" + year + "' limit 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("balance", DoubleType.INSTANCE);
        Object value = queryObject.uniqueResult();
        return null != value ? (Double) value : 0d;
    }
    //Nagendra for Previoues Year Balance

    public AccountBalance findAccountPreviouesPeriodBalanceForAccountAndYear(Integer year, String account, String period) throws Exception {
        Object accountPeriodEndBalanceObj = getSession().createQuery("from AccountBalance ab where ab.year='" + year.toString() + "' and ab.account='" + account + "' and ab.period='" + period + "'").setMaxResults(1).uniqueResult();
        return (AccountBalance) accountPeriodEndBalanceObj;
    }
    //

    public String getCompanyName() throws Exception {
        String queryString = "select Rule_Name from system_rules WHERE rule_code = 'CompanyName' limit 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("Rule_Name", StringType.INSTANCE);
        Object ruleName = queryObject.uniqueResult();
        return null != ruleName ? ruleName.toString() : "";
    }

    public Double getAccountBalanceForPreviousPeriods(String account, Integer period, Integer year) throws Exception {
        Double glBalance = 0d;
        glBalance += new AccountYearEndBalanceDAO().getAccountYearEndBalance(account, year - 1);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(period_balance) from account_balance");
        queryBuilder.append(" where account='").append(account).append("'");
        Integer lastYear = year - 1;
        String status = new FiscalYearDAO().getFiscalYearStatus(lastYear.toString());
        if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_OPEN)) {
            queryBuilder.append(" and ((period<='12' and year='").append(lastYear).append("')");
            queryBuilder.append(" or (period<='").append(NumberUtils.formatNumber(period, "00")).append("' and year='").append(year).append("'))");
            lastYear--;
        } else {
            queryBuilder.append(" and period<='").append(NumberUtils.formatNumber(period, "00")).append("' and year='").append(year).append("'");
        }
        Object periodBalance = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != periodBalance) {
            glBalance += Double.parseDouble(periodBalance.toString());
        }
        return glBalance;
    }

    public List<AccountsBean> getAccountsForIncomeStatement(String accountGroup, String fromPeriod, String toPeriod, Integer year, String budgetSet) throws Exception {
        List<AccountsBean> accountsForIncomeStatement = new ArrayList<AccountsBean>();
        StringBuilder queryBuilder = new StringBuilder("select cast(group_concat(ad.Account) as char), ad.Acct_Desc");
        queryBuilder.append(" ,ad.Normal_Balance from account_details ad");
        queryBuilder.append(" where ad.Acct_Group='").append(accountGroup).append("'and ad.Account is not null and ad.Acct_Desc is not nulL");
        queryBuilder.append(" group BY ad.Acct_Desc");
        queryBuilder.append(" order BY ad.Acct_Desc");
        List accounts = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : accounts) {
            Object[] col = (Object[]) row;
            String account = col[0].toString();
            StringTokenizer tokenizer = new StringTokenizer(account, ",");
            account = "";
            while (tokenizer.hasMoreTokens()) {
                account += "'" + tokenizer.nextToken() + "',";
            }
            account = StringUtils.removeEnd(account, ",");
            String accountDesc = col[1].toString();
            String normalBalance = null != col[2] ? col[2].toString() : "";
            Double currentYearBalance = this.getAccountBalance(account, fromPeriod, toPeriod, year, "currentYear");
            Double currentMonthBalance = this.getAccountBalance(account, fromPeriod, toPeriod, year, "currentMonth");
            Double previousYearBalance = this.getAccountBalance(account, fromPeriod, "AD", year - 1, "previousYear");
            Double priorYearBalance = this.getAccountBalance(account, fromPeriod, toPeriod, year - 1, "previousYear");
            Double budgetYtdBalance = new BudgetDAO().getBudgetAmount(account, toPeriod, year, budgetSet);
            Double annualBudgetBalance = new BudgetDAO().getBudgetAmount(account, null, year, budgetSet);
            AccountsBean accountsBean = new AccountsBean();
            accountsBean.setAccount(account);
            accountsBean.setAccountDescription(accountDesc);
            if (CommonUtils.isEqual(normalBalance, AccountingConstants.NORMAL_BALANCE_CREDIT)) {
                currentYearBalance = (-1) * currentYearBalance;
                previousYearBalance = (-1) * previousYearBalance;
                currentMonthBalance = (-1) * currentMonthBalance;
                priorYearBalance = (-1) * priorYearBalance;
                budgetYtdBalance = (-1) * budgetYtdBalance;
                annualBudgetBalance = (-1) * annualBudgetBalance;
            }
            accountsBean.setCurrentYearBalance(currentYearBalance);
            accountsBean.setPreviousYearBalance(previousYearBalance);
            accountsBean.setCurrentMonthBalance(currentMonthBalance);
            accountsBean.setPriorYearBalance(priorYearBalance);
            accountsBean.setBudgetYtdBalance(budgetYtdBalance);
            accountsBean.setAnnualBudgetBalance(annualBudgetBalance);
            accountsForIncomeStatement.add(accountsBean);
        }
        return accountsForIncomeStatement;
    }

    public Double getAccountBalance(String account, String fromPeriod, String toPeriod, Integer year, String type) throws Exception {
        Double accountBalance = 0d;
        if (CommonUtils.isEqual(type, "currentYear") || CommonUtils.isEqual(type, "previousYear")) {
            accountBalance += new AccountYearEndBalanceDAO().getAccountYearEndBalance(account, year - 1);
        }
        StringBuilder queryBuilder = new StringBuilder("select sum(ab.totalDebit)-sum(ab.toatlCredit) as totalBalance");
        queryBuilder.append(" from AccountBalance ab ");
        queryBuilder.append(" where ab.account in (").append(account).append(")");
        if (CommonUtils.isEqual(type, "currentYear") || CommonUtils.isEqual(type, "previousYear")) {
            queryBuilder.append(" and ab.period between '").append(fromPeriod).append("' and '").append(toPeriod).append("' and ab.year='").append(year).append("'");
        } else {
            queryBuilder.append(" and ab.period ='").append(toPeriod).append("' and ab.year='").append(year).append("'");
        }
        Double balance = (Double) getSession().createQuery(queryBuilder.toString()).uniqueResult();
        if (null != balance) {
            accountBalance += balance;
        }
        return accountBalance;
    }

    public Double getGLBalance(String account, String endPeriod) throws Exception {
        FiscalYear year = new FiscalPeriodDAO().getLastClosedYear(endPeriod);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  sum(g.balance) as balance ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    (select ");
        queryBuilder.append("      coalesce(sum(b.total_debit - b.toatl_credit), 0.00) as balance ");
        queryBuilder.append("    from");
        queryBuilder.append("      account_details a ");
        queryBuilder.append("      left join account_balance b ");
        queryBuilder.append("        on (");
        queryBuilder.append("          a.account = b.account ");
        queryBuilder.append("          and concat(b.year, b.period) between '").append(year.getNextYearPeriod()).append("' and '").append(endPeriod).append("'");
        queryBuilder.append("          and b.period not in ('AD', 'CL')");
        queryBuilder.append("        ) ");
        queryBuilder.append("    where a.account = '").append(account).append("') ");
        queryBuilder.append("    union all");
        queryBuilder.append("    (select ");
        queryBuilder.append("      coalesce(sum(b.closing_balance), 0.00) as balance ");
        queryBuilder.append("    from");
        queryBuilder.append("      account_details a ");
        queryBuilder.append("      join account_year_end_balance b ");
        queryBuilder.append("        on (");
        queryBuilder.append("          a.account = b.account and b.year = ").append(year.getYear());
        queryBuilder.append("        ) ");
        queryBuilder.append("    where a.account = '").append(account).append("')");
        queryBuilder.append("  ) as g ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("balance", DoubleType.INSTANCE);
        return (Double) query.uniqueResult();
    }

    public Map<String, Double> getClosingBalances(String startAccount, String endAccount, String endPeriod) {
        FiscalYear year = new FiscalPeriodDAO().getLastClosedYear(endPeriod);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  g.account as account,");
        queryBuilder.append("  sum(g.balance) as balance ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    (select ");
        queryBuilder.append("      a.account as account,");
        queryBuilder.append("      coalesce(sum(b.total_debit - b.toatl_credit), 0.00) as balance ");
        queryBuilder.append("    from");
        queryBuilder.append("      account_details a ");
        queryBuilder.append("      left join account_balance b ");
        queryBuilder.append("        on (");
        queryBuilder.append("          a.account = b.account ");
        queryBuilder.append("          and concat(b.year, b.period) between '").append(year.getNextYearPeriod()).append("' and '").append(endPeriod).append("'");
        queryBuilder.append("          and b.period not in ('AD', 'CL')");
        queryBuilder.append("        ) ");
        queryBuilder.append("    where");
        if (CommonUtils.isNotEmpty(endAccount)) {
            queryBuilder.append("      a.account between '").append(startAccount).append("' and '").append(endAccount).append("'");
        } else {
            queryBuilder.append("      a.account = '").append(startAccount).append("'");
        }
        queryBuilder.append("    group by a.account)");
        queryBuilder.append("    union all");
        queryBuilder.append("    (select ");
        queryBuilder.append("      a.account as account,");
        queryBuilder.append("      coalesce(sum(b.closing_balance), 0.00) as balance ");
        queryBuilder.append("    from");
        queryBuilder.append("      account_details a ");
        queryBuilder.append("      join account_year_end_balance b ");
        queryBuilder.append("        on (");
        queryBuilder.append("          a.account = b.account and b.year = ").append(year.getYear());
        queryBuilder.append("        ) ");
        queryBuilder.append("    where");
        if (CommonUtils.isNotEmpty(endAccount)) {
            queryBuilder.append("      a.account between '").append(startAccount).append("' and '").append(endAccount).append("'");
        } else {
            queryBuilder.append("      a.account = '").append(startAccount).append("'");
        }
        queryBuilder.append("    group by a.account)");
        queryBuilder.append("  ) as g ");
        queryBuilder.append("group by g.account");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("account", StringType.INSTANCE);
        query.addScalar("balance", DoubleType.INSTANCE);
        List<Object> result = query.list();
        Map<String, Double> balances = new HashMap<String, Double>();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            balances.put((String) col[0], (Double) col[1]);
        }
        return balances;
    }
}
