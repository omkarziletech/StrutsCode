package com.gp.cvst.logisoft.hibernate.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.AccountYearEndBalance;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Data access object (DAO) for domain model class AccountYearEndBalance.
 * @see com.gp.cvst.logisoft.hibernate.dao.AccountYearEndBalance
 * @author MyEclipse - Hibernate Tools
 */
public class AccountYearEndBalanceDAO extends BaseHibernateDAO {
    //property constants

    public static final String YEAR = "year";
    public static final String ACCOUNT = "account";
    public static final String CLOSING_BALANCE = "closingBalance";

    public void save(AccountYearEndBalance transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(AccountYearEndBalance persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public AccountYearEndBalance findById(java.lang.Integer id) throws Exception {
        AccountYearEndBalance instance = (AccountYearEndBalance) getSession().get("com.gp.cvst.logisoft.hibernate.dao.AccountYearEndBalance", id);
        return instance;
    }

    public List findByExample(AccountYearEndBalance instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.AccountYearEndBalance").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from AccountYearEndBalance as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByYear(Object year) throws Exception {
        return findByProperty(YEAR, year);
    }

    public List findByAccount(Object account) throws Exception {
        return findByProperty(ACCOUNT, account);
    }

    public List findByClosingBalance(Object closingBalance) throws Exception {
        return findByProperty(CLOSING_BALANCE, closingBalance);
    }

    public AccountYearEndBalance merge(AccountYearEndBalance detachedInstance) throws Exception {
        AccountYearEndBalance result = (AccountYearEndBalance) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(AccountYearEndBalance instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(AccountYearEndBalance instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public void deleteForAnYear(String year) throws Exception {
        getSession().createQuery("delete from AccountYearEndBalance ayeb where ayeb.year='" + year + "'").executeUpdate();
    }

    public void insertFromAccountBalance(String year) throws Exception {
        Connection conn = ((SessionImplementor)getSession()).connection();
        Statement st = conn.createStatement();
        int prevYear = Integer.parseInt(year) - 1;
        st.execute("delete from stage_account_year_end_balance_tmp");
        st.execute(" insert into stage_account_year_end_balance_tmp (account,year,balance) select account,year+1,closing_balance as balance from account_year_end_balance where year='" + prevYear + "'");
        st.execute(" insert into stage_account_year_end_balance_tmp (account,year,balance) select account,year,sum(period_balance) as balance from account_balance where year='" + year + "' group by account");
        st.execute("insert into account_year_end_balance (year, account,closing_balance) select year,account,sum(balance) from stage_account_year_end_balance_tmp where year='" + year + "' group by account ");
        if (st != null) {
            st.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    public AccountYearEndBalance findAccountYearEndBalanceForAccountAndYear(Integer year, String account) throws Exception {
        Object accountYearEndBalanceObj = getSession().createQuery("from AccountYearEndBalance ayeb where ayeb.year='" + year + "' and ayeb.account='" + account + "'").setMaxResults(1).uniqueResult();
        return (AccountYearEndBalance)accountYearEndBalanceObj;
    }

    public List<Object[]> getSumOfDebitAndCreditForAccountYearAndPeriod(String accountNo, Integer year, Integer period) throws Exception {
        List<Object[]> balanceList = null;
        NumberFormat formatter = new DecimalFormat("#00");
        String periodVal = formatter.format(new Integer(period).intValue());
        String queryString = "select sum(totalDebit),sum(toatlCredit) from AccountBalance where account='" + accountNo + "' and year='" + year + "' and period='" + periodVal + "'";
        balanceList = getCurrentSession().createQuery(queryString).list();
        return balanceList;
    }

    /**
     * period is passed as a , separated values
     * @param accountNo
     * @param year
     * @param period
     * @return
     */
    public List<Object[]> getSumOfDebitAndCreditForAccountYearAndPeriods(String accountNo, Integer year, String period) throws Exception {
        List<Object[]> balanceList = null;
        String queryString = "select sum(totalDebit),sum(toatlCredit) from AccountBalance where account='" + accountNo + "' and year='" + year + "' and period in (" + period + ")";
        balanceList = getCurrentSession().createQuery(queryString).list();
        return balanceList;
    }

    public Double getAccountYearEndBalance(String account, Integer year) throws Exception {
        String queryString = "select sum(closingBalance) from AccountYearEndBalance where account in (" + account + ") and year=" + year;
        Double yearEndBalance = (Double) getSession().createQuery(queryString).uniqueResult();
        if (null != yearEndBalance) {
            return yearEndBalance;
        }
        return 0d;
    }

    public AccountYearEndBalance getAccountYearEndBalance(Integer year, String account) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AccountYearEndBalance.class);
        criteria.add(Restrictions.eq("year", year));
        criteria.add(Restrictions.eq("account", account));
        criteria.setMaxResults(1);
        return (AccountYearEndBalance) criteria.uniqueResult();
    }

    public void setYearEndBalance(Integer year) throws Exception {
        getCurrentSession().getTransaction().commit();
        getCurrentSession().beginTransaction().begin();
        String query = "select account,sum(Total_Debit-Toatl_Credit) from account_balance where year='" + year + "' group by account";
        List<Object> result = getCurrentSession().createSQLQuery(query).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            String account = (String) col[0];
            Double closingBalance = Double.parseDouble(col[1].toString());
            AccountYearEndBalance previousYearEndBalance = getAccountYearEndBalance(year - 1, account);
            if (null != previousYearEndBalance) {
                closingBalance += previousYearEndBalance.getClosingBalance();
            }
            AccountYearEndBalance yearEndBalance = getAccountYearEndBalance(year, account);
            if (null == yearEndBalance) {
                yearEndBalance = new AccountYearEndBalance(year, account, closingBalance);
                save(yearEndBalance);
            } else {
                yearEndBalance.setClosingBalance(yearEndBalance.getClosingBalance() + closingBalance);
            }
        }
    }
}
