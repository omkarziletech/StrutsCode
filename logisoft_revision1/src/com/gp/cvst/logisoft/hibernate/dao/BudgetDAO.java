package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.Budgetbean;
import com.gp.cvst.logisoft.beans.comparisonbean;
import com.gp.cvst.logisoft.domain.AccountBalance;
import com.gp.cvst.logisoft.domain.Budget;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object (DAO) for domain model class Budget.
 * @see com.gp.cvst.logisoft.hibernate.domain.DAO.Budget
 * @author MyEclipse - Hibernate Tools
 */
public class BudgetDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(BudgetDAO.class);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public void save(Budget transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(Budget persistInstance) throws Exception {
        getSession().update(persistInstance);
        getSession().flush();
    }

    public void delete(Budget persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public Budget findById(java.lang.Integer id) throws Exception {
        Budget instance = (Budget) getSession().get("com.gp.cvst.logisoft.domain.Budget", id);
        return instance;
    }

    public List findByExample(Budget instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.DAO.Budget").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Budget as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public Budget merge(Budget detachedInstance) throws Exception {
        Budget result = (Budget) getSession().merge(detachedInstance);
        log.debug("merge successful");
        return result;
    }

    public void attachDirty(Budget instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Budget instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findForSearch(String budgetacct, String budgetset, String year) throws Exception {
        Budgetbean objChartCodeBean = null;
        List<Budgetbean> lstGenericIfo = new ArrayList<Budgetbean>();
        String queryString = "";
        queryString = "select bg.period,bg.budgetAmount,fp.endDate from Budget bg,FiscalPeriod fp"
                + " where bg.account='" + budgetacct + "' and bg.budgetSet='" + budgetset + "'and bg.year='" + year + "' and fp.year=bg.year and fp.period=bg.period";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        while (iter.hasNext()) {
            objChartCodeBean = new Budgetbean();
            Object[] row = (Object[]) iter.next();
            String period = (String) row[0];
            double i = (Double) row[1];
            //String budgetamount=Double.toString(i);
            Date edate = (Date) row[2];
            String enddate = sdf.format(edate);
            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setBudgetamount(number.format(i));
            objChartCodeBean.setEnddate(enddate);
            lstGenericIfo.add(objChartCodeBean);
            objChartCodeBean = null;
        }

        return lstGenericIfo;
    }

    public List settingBudget(String budgetacct, String budgetset, String year, String value) throws Exception {
        Budgetbean objChartCodeBean = null;
        List<Budgetbean> lstGenericIfo = new ArrayList<Budgetbean>();
        String queryString = "";
        queryString = "select bg.period,fp.endDate from Budget bg,FiscalPeriod fp where bg.account='" + budgetacct + "' and bg.budgetSet='" + budgetset + "'and bg.year='" + year + "' and fp.year=bg.year and fp.period=bg.period";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Double i = Double.parseDouble(value);
        Iterator iter = queryObject.iterator();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        while (iter.hasNext()) {
            objChartCodeBean = new Budgetbean();
            Object[] row = (Object[]) iter.next();
            String period = (String) row[0];
            //double i=(Double)row[1];
            Date edate = (Date) row[1];
            String enddate = sdf.format(edate);
            //String budgetamount=Double.toString(i);
            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setBudgetamount(number.format(i));
            objChartCodeBean.setEnddate(enddate);
            lstGenericIfo.add(objChartCodeBean);
            objChartCodeBean = null;
        }

        return lstGenericIfo;
    }

    public List settingBudget(String budgetacct, String budgetset, String year, String value, String increaseby) throws Exception {
        Budgetbean objChartCodeBean = null;
        List<Budgetbean> lstGenericIfo = new ArrayList<Budgetbean>();
        String queryString = "";
        queryString = "select bg.period,fp.endDate from Budget bg,FiscalPeriod fp where bg.account='" + budgetacct + "' and bg.budgetSet='" + budgetset + "'and bg.year='" + year + "' and fp.year=bg.year and fp.period=bg.period";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        //double j=Double.parseDouble(value)+Double.parseDouble(increaseby);
        double v1 = Double.parseDouble(value);
        double v = v1;
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        while (iter.hasNext()) {
            objChartCodeBean = new Budgetbean();
            Object[] row = (Object[]) iter.next();
            String period = (String) row[0];
            //double i=(Double)row[1];
            Date edate = (Date) row[1];
            String enddate = sdf.format(edate);
            //String budgetamount=v;
            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setBudgetamount(number.format(v));
            objChartCodeBean.setEnddate(enddate);
            lstGenericIfo.add(objChartCodeBean);
            objChartCodeBean = null;
            double j = v + Double.parseDouble(increaseby);
            //value=Double.toString(j);
            v = j;
        }

        return lstGenericIfo;
    }

    public List settingBudgetforPercentIncrease(String budgetacct, String budgetset, String year, String value, String increaseby) throws Exception {
        Budgetbean objChartCodeBean = null;
        List<Budgetbean> lstGenericIfo = new ArrayList<Budgetbean>();
        String queryString = "";
        queryString = "select bg.period,fp.endDate from Budget bg,FiscalPeriod fp where"
                + " bg.account='" + budgetacct + "' and bg.budgetSet='" + budgetset + "'and bg.year='" + year + "' and fp.year=bg.year and fp.period=bg.period";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        double v = Double.parseDouble(value);
        double k = Double.parseDouble(increaseby);
        double l = Double.parseDouble(value);
        double m = l * (k / 100);
        while (iter.hasNext()) {
            objChartCodeBean = new Budgetbean();
            Object[] row = (Object[]) iter.next();
            String period = (String) row[0];
            Date edate = (Date) row[1];
            String enddate = sdf.format(edate);
            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setBudgetamount(number.format(v));
            objChartCodeBean.setEnddate(enddate);
            lstGenericIfo.add(objChartCodeBean);
            objChartCodeBean = null;
            double j = l + m;
            l = j;
            v = l;
        }

        return lstGenericIfo;
    }

    public List settingBudgetFromAcctBalance(String copyacct, String year) throws Exception {
        Budgetbean objChartCodeBean = null;
        List<Budgetbean> lstGenericIfo = new ArrayList<Budgetbean>();
        String queryString = "";
        queryString = "select ab.period,ab.periodBalance,fp.endDate from AccountBalance "
                + "ab,FiscalPeriod fp,Budget bg where ab.account='" + copyacct + "' and ab.year='" + year + "' and bg.account='" + copyacct + "' and bg.year='" + year + "'and bg.budgetSet='" + 1 + "'and fp.year=bg.year and fp.period=bg.period and ab.period=bg.period";
        List queryObject = getCurrentSession().createQuery(queryString).list();

        Iterator iter = queryObject.iterator();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        while (iter.hasNext()) {
            objChartCodeBean = new Budgetbean();
            Object[] row = (Object[]) iter.next();
            String period = (String) row[0];
            double i = (Double) row[1];
            //String budgetamount=Double.toString(i);
            Date edate = (Date) row[2];
            String enddate = sdf.format(edate);
            objChartCodeBean.setPeriod(period);
            objChartCodeBean.setBudgetamount(number.format(i));
            objChartCodeBean.setEnddate(enddate);
            lstGenericIfo.add(objChartCodeBean);
            objChartCodeBean = null;
        }

        return lstGenericIfo;
    }

    public Iterator getallbudgetset() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery("select distinct bg.budgetSet from Budget bg").list().iterator();
        return results;
    }

    public void savebudget(Double budgetamount, String budgetacct, String budgetset, String year, String period) throws Exception {
        String query = "UPDATE Budget bg set bg.budgetAmount='" + budgetamount + "'where bg.account='" + budgetacct + "'and bg.budgetSet='" + budgetset + "'and bg.year='" + year + "' and bg.period='" + period + "'";
        int updatedrecords = getCurrentSession().createQuery(query).executeUpdate();
    }

    public List findforBudget(String account, String year1, String year2, String bs1, String bs2) throws Exception {
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        NumberFormat num = new DecimalFormat("00");
        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        String queryString = "";
        String queryString1 = "";
        queryString = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year1 + "' and bg.budgetSet='" + bs1 + "'";
        queryString1 = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year2 + "' and bg.budgetSet='" + bs2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();


        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        double i = 1;
        while (iter.hasNext() && iter1.hasNext()) {

            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            double b = (Double) row1;


            objChartCodeBean.setPeriod(num.format(i));
            objChartCodeBean.setActuals(number.format(a));
            objChartCodeBean.setBudget(number.format(b));
            acclist.add(objChartCodeBean);
            i++;

        }
        return acclist;
    }

    public List findforBudgetforSet1(String account, String year1, String year2, String bs1) throws Exception {
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        NumberFormat num = new DecimalFormat("00");
        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        String queryString = "";
        String queryString1 = "";
        queryString = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year1 + "' and bg.budgetSet='" + bs1 + "'";
        queryString1 = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();


        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        double i = 1;
        while (iter.hasNext() && iter1.hasNext()) {

            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();

            double a = (Double) row;

            double b = (Double) row1;


            objChartCodeBean.setPeriod(num.format(i));
            objChartCodeBean.setActuals(number.format(a));
            objChartCodeBean.setBudget(number.format(b));
            acclist.add(objChartCodeBean);
            i++;

        }
        return acclist;
    }

    public List findforBudgetforSet2(String account, String year1, String year2, String bs2) throws Exception {
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        NumberFormat num = new DecimalFormat("00");
        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        String queryString = "";
        String queryString1 = "";
        queryString = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year1 + "'";
        queryString1 = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year2 + "' and bg.budgetSet='" + bs2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();


        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        double i = 1;
        while (iter.hasNext() && iter1.hasNext()) {

            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            double b = (Double) row1;

            objChartCodeBean.setPeriod(num.format(i));
            objChartCodeBean.setActuals(number.format(a));
            objChartCodeBean.setBudget(number.format(b));
            acclist.add(objChartCodeBean);
            i++;

        }
        return acclist;
    }

    public List findforBudget2(String account, String year1, String year2, String bs1, String bs2) throws Exception {

        NumberFormat number = new DecimalFormat("###,###,##0.00");
        NumberFormat num = new DecimalFormat("00");
        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        int y1 = Integer.parseInt(year1);
        y1 = y1 - 1;
        String queryString11 = "select ayeb.closingBalance from AccountYearEndBalance ayeb "
                + "where ayeb.year='" + y1 + "' and ayeb.account='" + account + "'";
        List qo1 = getCurrentSession().createQuery(queryString11).list();
        String re1 = null;
        try {
            double p1 = (Double) qo1.get(0);
            re1 = Double.toString(p1);
        } catch (IndexOutOfBoundsException e) {
            re1 = "0.00";
        }
        int y2 = Integer.parseInt(year2);
        y2 = y2 - 1;
        String queryString12 = "select ayeb.closingBalance from AccountYearEndBalance ayeb "
                + "where ayeb.year='" + y2 + "' and ayeb.account='" + account + "'";
        List qo2 = getCurrentSession().createQuery(queryString12).list();
        String re2 = null;
        try {
            double p2 = (Double) qo2.get(0);
            re2 = Double.toString(p2);
        } catch (IndexOutOfBoundsException e) {
            re2 = "0.00";
        }


        String queryString = "";
        String queryString1 = "";
        queryString = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year1 + "' and bg.budgetSet='" + bs1 + "'";
        queryString1 = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year2 + "' and bg.budgetSet='" + bs2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        double i = 1;
        double r1 = Double.parseDouble(re1);
        double r2 = Double.parseDouble(re2);
        while (iter.hasNext() && iter1.hasNext()) {
            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            r1 = r1 + a;
            double b = (Double) row1;
            r2 = r2 + b;

            objChartCodeBean.setPeriod(num.format(i));
            objChartCodeBean.setActuals(number.format(r1));
            objChartCodeBean.setBudget(number.format(r2));
            acclist.add(objChartCodeBean);
            i++;

        }
        return acclist;
    }

    public List findforBudgetforSet11(String account, String year1, String year2, String bs1) throws Exception {
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        NumberFormat num = new DecimalFormat("00");
        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        int y1 = Integer.parseInt(year1);
        y1 = y1 - 1;
        String queryString11 = "select ayeb.closingBalance from AccountYearEndBalance ayeb"
                + " where ayeb.year='" + y1 + "' and ayeb.account='" + account + "'";
        List qo1 = getCurrentSession().createQuery(queryString11).list();
        String re1 = null;
        try {
            double p1 = (Double) qo1.get(0);
            re1 = Double.toString(p1);
        } catch (IndexOutOfBoundsException e) {
            re1 = "0.00";
        }
        int y2 = Integer.parseInt(year2);
        y2 = y2 - 1;
        String queryString12 = "select ayeb.closingBalance from AccountYearEndBalance "
                + "ayeb where ayeb.year='" + y2 + "' and ayeb.account='" + account + "'";
        List qo2 = getCurrentSession().createQuery(queryString12).list();
        String re2 = null;
        try {
            double p2 = (Double) qo2.get(0);
            re2 = Double.toString(p2);
        } catch (IndexOutOfBoundsException e) {
            re2 = "0.00";
        }
        String queryString = "";
        String queryString1 = "";
        queryString = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year1 + "' and bg.budgetSet='" + bs1 + "'";
        queryString1 = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();
        double i = 1;
        double r1 = Double.parseDouble(re1);
        double r2 = Double.parseDouble(re2);
        while (iter.hasNext() && iter1.hasNext()) {
            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            r1 = r1 + a;
            double b = (Double) row1;
            r2 = r2 + b;
            objChartCodeBean.setPeriod(num.format(i));
            objChartCodeBean.setActuals(number.format(r1));
            objChartCodeBean.setBudget(number.format(r2));
            acclist.add(objChartCodeBean);
            i++;

        }
        return acclist;
    }

    public List findforBudgetforSet21(String account, String year1, String year2, String bs2) {
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        NumberFormat num = new DecimalFormat("00");

        comparisonbean objChartCodeBean = null;
        List<comparisonbean> acclist = new ArrayList<comparisonbean>();
        int y1 = Integer.parseInt(year1);
        y1 = y1 - 1;
        String queryString11 = "select ayeb.closingBalance from AccountYearEndBalance ayeb where"
                + " ayeb.year='" + y1 + "' and ayeb.account='" + account + "'";
        List qo1 = getCurrentSession().createQuery(queryString11).list();
        String re1 = null;
        try {
            double p1 = (Double) qo1.get(0);
            re1 = Double.toString(p1);
        } catch (IndexOutOfBoundsException e) {
            re1 = "0.00";
        }
        int y2 = Integer.parseInt(year2);
        y2 = y2 - 1;
        String queryString12 = "select ayeb.closingBalance from AccountYearEndBalance ayeb where ayeb.year='" + y2 + "' and ayeb.account='" + account + "'";
        List qo2 = getCurrentSession().createQuery(queryString12).list();
        String re2 = null;
        try {
            double p2 = (Double) qo2.get(0);
            re2 = Double.toString(p2);
        } catch (IndexOutOfBoundsException e) {
            re2 = "0.00";
        }

        String queryString = "";
        String queryString1 = "";
        queryString = "select acbal.periodBalance from AccountBalance acbal where acbal.account='" + account + "' and acbal.year='" + year1 + "'";
        queryString1 = "select bg.budgetAmount from Budget bg where bg.account='" + account + "' and bg.year='" + year2 + "' and bg.budgetSet='" + bs2 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = queryObject.iterator();
        Iterator iter1 = queryObject1.iterator();

        double i = 1;
        double r1 = Double.parseDouble(re1);
        double r2 = Double.parseDouble(re2);
        while (iter.hasNext() && iter1.hasNext()) {

            objChartCodeBean = new comparisonbean();
            Object row = (Object) iter.next();
            Object row1 = (Object) iter1.next();
            double a = (Double) row;
            r1 = r1 + a;
            double b = (Double) row1;
            r2 = r2 + b;
            objChartCodeBean.setPeriod(num.format(i));
            objChartCodeBean.setActuals(number.format(r1));
            objChartCodeBean.setBudget(number.format(r2));
            acclist.add(objChartCodeBean);
            i++;

        }
        return acclist;
    }

    public List getBatchDetailsForExcel(String budgetacct, String budgetset, String year1) {
        List<Budgetbean> batchListForExcel = new ArrayList<Budgetbean>();
        Budgetbean budgetbean = new Budgetbean();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        String queryString = "select b.period,b.budgetAmount from Budget b where b.account='" + budgetacct + "' and b.budgetSet='" + budgetset + "' and b.year='" + year1 + "' ";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            budgetbean = new Budgetbean();
            budgetbean.setPeriod(null != row[0] ? row[0].toString() : "");
            budgetbean.setBudgetamount(null != row[1] ? number.format(row[1]) : "");
            budgetbean.setBudgetaccount(budgetacct);
            budgetbean.setBudgetSet(budgetset);
            budgetbean.setYear(year1);
            batchListForExcel.add(budgetbean);
            budgetbean = null;
        }
        return batchListForExcel;
    }

    public List getBatchDetailsForExcelFiscal(String budgetacct, String budgetset, Integer year1) {
        List<Budgetbean> batchListForExcel = new ArrayList<Budgetbean>();
        Budgetbean budgetbean = new Budgetbean();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        String queryString = "select b.period,b.budgetAmount,b.account,b.budgetSet,b.year from Budget b where b.budgetSet='" + budgetset + "' and b.year='" + year1 + "'";
        // where b.account='"+budgetacct+"' and b.budgetSet='"+budgetset+"' and b.year='"+year1+"'
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            budgetbean = new Budgetbean();
            budgetbean.setPeriod(null != row[0] ? row[0].toString() : "");
            budgetbean.setBudgetamount(null != row[1] ? number.format(row[1]) : "");
            budgetbean.setBudgetaccount(null != row[2] ? row[2].toString() : "");
            budgetbean.setBudgetSet(null != row[3] ? row[3].toString() : "");
            budgetbean.setYear(null != row[4] ? row[4].toString() : "");
            batchListForExcel.add(budgetbean);

            budgetbean = null;
        }
        return batchListForExcel;
    }

    /**
     * @param year
     * @param budgetSet
     * @return true or false
     */
    public boolean checkBudgetSet(String year, String budgetSet) throws Exception {
        String queryString = "select count(*) from Budget where year=" + year + " and budgetSet='" + budgetSet + "'";
        Object result = getCurrentSession().createQuery(queryString).uniqueResult();
        if (null != result && Integer.parseInt(result.toString()) > 0) {
            return true;
        }
        return false;
    }

    public Double getBudgetAmount(String account, String period, Integer year, String budgetSet) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Budget.class);
        String[] accounts = StringUtils.split(account.replaceAll("'", ""), ",");
        criteria.add(Restrictions.in("account", accounts));
        criteria.add(Restrictions.eq("year", year));
        criteria.add(Restrictions.eq("budgetSet", budgetSet));
        if (CommonUtils.isNotEmpty(period)) {
            criteria.add(Restrictions.le("period", period));
        }
        criteria.setProjection(Projections.sum("budgetAmount"));
        Double budgetAmount = (Double) criteria.uniqueResult();
        if (budgetAmount != null) {
            return budgetAmount;
        }
        return 0d;
    }

    public List<Budget> getBudgetsByBudgetSet(String account, Integer year, String period, String budgetSet) throws Exception {
        List<Budget> budgets = new ArrayList<Budget>();
        StringBuilder queryBuilder = new StringBuilder("select budget,fiscalPeriod from Budget budget,FiscalPeriod fiscalPeriod where");
        queryBuilder.append(" budget.period=fiscalPeriod.period and budget.year=fiscalPeriod.year");
        queryBuilder.append(" and budget.account='").append(account).append("'");
        queryBuilder.append(" and budget.year=").append(year);
        if (CommonUtils.isNotEmpty(period)) {
            queryBuilder.append(" and budget.period='").append(period).append("'");
        }
        queryBuilder.append(" and budget.budgetSet='").append(budgetSet).append("'");
        List result = getCurrentSession().createQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            Budget budget = (Budget) col[0];
            FiscalPeriod fiscalPeriod = (FiscalPeriod) col[1];
            budget.setEndDate(DateUtils.formatDate(fiscalPeriod.getEndDate(), "MM/dd/yyyy"));
            budgets.add(budget);
        }
        return budgets;
    }

    public List<Budget> getBudgetsByActual(String account, Integer year, String period, String budgetSet) throws Exception {
        List<Budget> budgets = new ArrayList<Budget>();
        StringBuilder queryBuilder = new StringBuilder("select budget,fiscalPeriod,accountBalance from Budget budget,FiscalPeriod fiscalPeriod");
        queryBuilder.append(",AccountBalance accountBalance where accountBalance.period=fiscalPeriod.period");
        queryBuilder.append(" and accountBalance.year=fiscalPeriod.year and budget.account=accountBalance.account");
        queryBuilder.append(" and budget.period=fiscalPeriod.period and budget.year=fiscalPeriod.year");
        queryBuilder.append(" and budget.account='").append(account).append("'");
        queryBuilder.append(" and budget.year=").append(year);
        if (CommonUtils.isNotEmpty(period)) {
            queryBuilder.append(" and budget.period='").append(period).append("'");
        }
        queryBuilder.append(" and budget.budgetSet='").append(budgetSet).append("'");
        List result = getCurrentSession().createQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            Budget budget = (Budget) col[0];
            FiscalPeriod fiscalPeriod = (FiscalPeriod) col[1];
            AccountBalance accountBalance = (AccountBalance) col[2];
            Budget budgetToForm = new Budget(budget.getAccount(), fiscalPeriod.getPeriod(), accountBalance.getPeriodBalance(), budget.getBudgetSet(), fiscalPeriod.getYear());
            budgetToForm.setEndDate(DateUtils.formatDate(fiscalPeriod.getEndDate(), "MM/dd/yyyy"));
            budgetToForm.setId(budget.getId());
            budgets.add(budgetToForm);
        }
        return budgets;
    }
}
