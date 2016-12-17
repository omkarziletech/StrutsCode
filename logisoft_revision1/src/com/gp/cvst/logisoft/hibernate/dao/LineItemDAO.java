package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.AccountYearEndBalance;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.struts.form.ReconcileForm;
import org.hibernate.LockOptions;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Data access object (DAO) for domain model class LineItem.
 * @see com.gp.cvst.logisoft.domain.LineItem
 * @author MyEclipse - Hibernate Tools
 */
public class LineItemDAO extends BaseHibernateDAO {

    public void save(LineItem transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(LineItem persistentInstance)throws Exception {
	    getSession().delete(persistentInstance);
	    getSession().flush();
    }

    public LineItem findById(java.lang.String id)throws Exception {
	    LineItem instance = (LineItem) getSession().get("com.gp.cvst.logisoft.domain.LineItem", id);
	    return instance;
    }

    public List findByExample(LineItem instance)throws Exception {
	    List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.LineItem").add(Example.create(instance)).list();
	    return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
	    String queryString = "from LineItem as model where model." + propertyName + "= ?0";
	    Query queryObject = getSession().createQuery(queryString);
	    queryObject.setParameter("0", value);
	    return queryObject.list();
    }

    public LineItem merge(LineItem detachedInstance)throws Exception {
	    LineItem result = (LineItem) getSession().merge(detachedInstance);
	    return result;
    }

    public void attachDirty(LineItem instance)throws Exception {
	    getSession().saveOrUpdate(instance);
	    getSession().flush();
    }

    public void attachClean(LineItem instance)throws Exception {
	    getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List<ChartOfAccountBean> getTransactions(String account, String account1,
	    FiscalPeriod periodFrom, FiscalPeriod periodTo, String sortBy) throws Exception {
	Double periodBalace = new Double(0);
	NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
	List<ChartOfAccountBean> chartOfAccountBeanList = new ArrayList<ChartOfAccountBean>();
	String queryString1 = "select sum(ab.periodBalance) from AccountBalance ab where ab.account='" + account + "' and period < '" + periodFrom.getPeriod() + "' and year='" + periodFrom.getYear() + "'";
	AccountYearEndBalance ayeb = new AccountYearEndBalance();
	if (periodFrom != null) {
	    String period = "";
	    Integer year = periodFrom.getYear();
	    if (periodFrom.getPeriod().equals("AD") || periodFrom.getPeriod().equals("CL")) {
		period = "12";
	    } else {
		period = "" + (Integer.parseInt(periodFrom.getPeriod()) - 1);
	    }
	    if (Integer.parseInt(period) == 1) {
		period = "12";
		year = periodFrom.getYear() - 1;
	    }
	    queryString1 += " and ab.period<='" + period + "'and ab.year='" + year + "'";
	    AccountYearEndBalanceDAO ayebDAO = new AccountYearEndBalanceDAO();
	    //To get Previous Year balance of the Same Account
	    ayeb = ayebDAO.findAccountYearEndBalanceForAccountAndYear((periodFrom.getYear() - 1), account);
	    if (ayeb != null) {
		periodBalace = periodBalace + ayeb.getClosingBalance();
	    }
	}
	List periodBalanceList = getCurrentSession().createQuery(queryString1).list();

	if (periodBalanceList != null && !periodBalanceList.isEmpty()) {
	    if (periodBalanceList.get(0) != null) {
		periodBalace = periodBalace + (Double) periodBalanceList.get(0);
	    }
	}
	if (periodBalace == null) {
	    periodBalace = new Double(0);
	}
	ChartOfAccountBean objChartCodeBean = new ChartOfAccountBean("Beg", " ", " ", " ", " ", " ", " ", numberFormat.format(periodBalace), " ");
	chartOfAccountBeanList.add(objChartCodeBean);
	List<ChartOfAccountBean> list = getAllTransactions(account, account1, periodFrom, periodTo, periodBalace, sortBy);
	chartOfAccountBeanList.addAll(list);
	return chartOfAccountBeanList;
    }

    public Double getClosingBalance(String account, String Period, Integer year)throws Exception {
	Double periodBalace = new Double(0);
	String queryString1 = "select sum(ab.periodBalance) from AccountBalance ab where ab.account='" + account + "' ";
	AccountYearEndBalance ayeb = new AccountYearEndBalance();
	queryString1 += " and ab.period<'" + Period + "'and ab.year='" + year + "'";
	AccountYearEndBalanceDAO ayebDAO = new AccountYearEndBalanceDAO();
	    ayeb = ayebDAO.findAccountYearEndBalanceForAccountAndYear(year, account);
	    if (ayeb != null) {
		periodBalace = periodBalace + ayeb.getClosingBalance();

	    }
	    List periodBalanceList = getCurrentSession().createQuery(queryString1).list();

	    if (!periodBalanceList.isEmpty()) {
		if ((Double) periodBalanceList.get(0) != null) {
		    periodBalace = periodBalace + (Double) periodBalanceList.get(0);
		}

	    }

	return periodBalace;
    }

    public List<ChartOfAccountBean> getAllTransactions(String account, String account1, FiscalPeriod periodFrom, FiscalPeriod periodTo)throws Exception {
	NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	List<ChartOfAccountBean> chartOfAccountBeanList = new ArrayList<ChartOfAccountBean>();
	ChartOfAccountBean chartOfAccountBean = null;
	String query = null;

	if (account != null && !account.equals("")) {
	    if (account1 == null || account1.equals("")) {

		if (periodFrom != null && periodTo != null) {
		    query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%" + account + "%' and je.period Between '" + periodFrom.getPeriodDis() + "' and '" + periodTo.getPeriodDis() + "'";
		} else {
		    query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%" + account + "%'";
		}

	    } else {

		query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account between (select distinct account from LineItem where account like '%" + account + "%' ) and (select distinct account from LineItem where account like '%" + account1 + "%' )";
	    }
	} else {
	    if (account1 == null || account1.equals("") && !account.equals("")) {
		query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%" + account + "%'";
	    } else {

		query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%" + account1 + "%'";
	    }
	}


	List queryObject = getCurrentSession().createQuery(query).list();
	Iterator iter = queryObject.iterator();

	while (iter.hasNext()) {
	    chartOfAccountBean = new ChartOfAccountBean();
	    Object[] row = (Object[]) iter.next();
	    String debit = row[4].toString();
	    String credit = row[5].toString();
	    String acct = row[7].toString();
	    /*      Double periodBalace = new Double(0);
	    String queryString1 = "select sum(ab.periodBalance) from AccountBalance ab where ab.account='" + acct + "' ";
	    AccountYearEndBalance ayeb = new AccountYearEndBalance();

	    if (periodFrom != null) {
	    String p = "";

	    if (periodFrom.getPeriod().equals("AD") || periodFrom.getPeriod().equals("CL")) {
	    p = "=12";
	    } else {
	    p = periodFrom.getPeriod();
	    }
	    queryString1 += " and ab.period<'" + p + "'and ab.year='" + periodFrom.getYear() + "'";
	    AccountYearEndBalanceDAO ayebDAO = new AccountYearEndBalanceDAO();
	    //To get Previous Year balance of the Same Account
	    ayeb = ayebDAO.findAccountYearEndBalanceForAccountAndYear((periodFrom.getYear() - 1), acct);
	    if (ayeb != null) {
	    periodBalace = periodBalace + ayeb.getClosingBalance();
	    }
	    }
	    List periodBalanceList = getCurrentSession().createQuery(queryString1).list();

	    if (periodBalanceList != null && !periodBalanceList.isEmpty()) {
	    if (periodBalanceList.get(0) != null) {
	    periodBalace = periodBalace + (Double) periodBalanceList.get(0);
	    }
	    }
	    if (periodBalace == null) {
	    periodBalace = new Double(0);
	    }
	     */
	    chartOfAccountBean.setAcct(acct);
	    chartOfAccountBean.setPeriod(" ");
	    chartOfAccountBean.setDate((Date) row[0] == null ? "" : simpleDateFormat.format((Date) row[0]));
	    chartOfAccountBean.setSourcecode((String) row[1]);
	    chartOfAccountBean.setReference((String) row[2]);
	    chartOfAccountBean.setDescription((String) row[3]);
	    chartOfAccountBean.setDebit(numberFormat.format(Double.parseDouble(debit)));
	    chartOfAccountBean.setCredit(numberFormat.format(Double.parseDouble(credit)));
	    chartOfAccountBean.setCurrency((String) row[6]);
	    Double periodBalace = (Double.parseDouble(debit) - Double.parseDouble(credit));
	    chartOfAccountBean.setBalance(numberFormat.format(periodBalace));
	    chartOfAccountBean.setSourceamount(numberFormat.format(Double.parseDouble(credit) + Double.parseDouble(debit)));
	    chartOfAccountBeanList.add(chartOfAccountBean);
	}
	return chartOfAccountBeanList;

    }

    // With PeriodBalance Parameter By pradeep
    public List<ChartOfAccountBean> getAllTransactions(String account, String account1, FiscalPeriod periodFrom, FiscalPeriod periodTo, Double periodbalance, String sortBy) throws Exception {

	NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	List<ChartOfAccountBean> chartOfAccountBeanList = new ArrayList<ChartOfAccountBean>();
	ChartOfAccountBean chartOfAccountBean = null;
	String query = null;
	String sort = "line.account,je.period";
	if (sortBy != null && sortBy.equals("no")) {
	    sort = "je.period,line.account";
	}

	if (account != null && !account.equals("")) {
	    if (account1 == null || account1.equals("")) {

		if (periodFrom != null && periodTo != null) {
		    query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,"
			    + "line.debit,line.credit,line.currency,line.account,je.period from "
			    + "LineItem line,JournalEntry je,Batch b where je.journalEntryId="
			    + "line.journalEntryId and b.batchId=je.batchId and b.status='posted'"
			    + " and line.account like '%" + account + "%' and je.period Between '" + periodFrom.getPeriodDis() + "' and '" + periodTo.getPeriodDis() + "' ORDER BY je.period,line.date,je.journalEntryId";
		} else {

		    query = "select line.date, "
			    + "je.sourceCode.code,je.journalEntryId,je."
			    + "journalEntryDesc,line.debit,line.credit,"
			    + "line.currency,line.account,je.period from"
			    + " LineItem line,JournalEntry je,Batch b where je."
			    + "journalEntryId=line.journalEntryId and b.batchId=je.batchId and"
			    + " b.status='posted' and line.account like '%" + account + "%'";

		    //query="select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%"+account+"%'";

		}

	    } else {

		if (periodFrom != null && periodTo != null) {
		    query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account,je.period from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted'  and line.account between (select distinct account from LineItem where account like '%" + account + "%' ) and (select distinct account from LineItem where account like '%" + account1 + "%' ) and je.period Between '" + periodFrom.getPeriodDis() + "' and '" + periodTo.getPeriodDis() + "' Order BY " + sort + ",line.date,je.journalEntryId";

		} else {
		    query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account,je.period from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted'  and line.account between (select distinct account from LineItem where account like '%" + account + "%' ) and (select distinct account from LineItem where account like '%" + account1 + "%' ) Order BY line.account,je.period,line.date,je.journalEntryId";
		}
	    }
	} else {
	    if (account1 == null || account1.equals("") && !account.equals("")) {
		query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account,je.period from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%" + account + "%'";
	    } else {

		query = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency,line.account,je.period from LineItem line,JournalEntry je,Batch b where je.journalEntryId=line.journalEntryId and b.batchId=je.batchId and b.status='posted' and line.account like '%" + account1 + "%'";
	    }
	}

	List queryObject = getCurrentSession().createQuery(query).list();

	Iterator iter = queryObject.iterator();
	if (periodbalance == null) {
	    periodbalance = 0.0;
	}
	String lmonth = "";
	String ltempMonth = "";
	int tempv = 0;
	int lyear = 0;
	String prevAcct = "";
	while (iter.hasNext()) {
	    chartOfAccountBean = new ChartOfAccountBean();
	    Object[] row = (Object[]) iter.next();
	    Date liDate = (Date) row[0];
	    String debit = row[4].toString();
	    String credit = row[5].toString();
	    String acct = row[7].toString();
	    String period = row[8].toString();

	    if (period != null) {
		FiscalPeriodDAO fpDAO = new FiscalPeriodDAO();
		List fpObj = fpDAO.findByPeriodDis(period);
		if (!fpObj.isEmpty()) {
		    FiscalPeriod fp = (FiscalPeriod) fpObj.get(0);
		    lyear = fp.getYear();
		    lmonth = fp.getPeriod();
		}

	    }
	    if (tempv != 0 && !ltempMonth.equals(period)) {
		ChartOfAccountBean coaBean = new ChartOfAccountBean();
		coaBean.setBalance(AccountingConstants.PeriodClosingBal + numberFormat.format(periodbalance));
		coaBean.setReference("");
		//coaBean.setDescription("period Closing Balance.......")
		chartOfAccountBeanList.add(coaBean);
		coaBean = null;
	    }
	    ltempMonth = period;
	    /*	if(!chartOfAccountBeanList.isEmpty())
	    {
	    int listsize=chartOfAccountBeanList.size();
	    ChartOfAccountBean coaBean=new ChartOfAccountBean();
	    coaBean=(ChartOfAccountBean)chartOfAccountBeanList.get(listsize-1);


	    }*/

	    if (tempv != 0 && CommonUtils.isNotEqual(acct, prevAcct)) {
		periodbalance = getClosingBalance(acct, lmonth, lyear);
		ChartOfAccountBean objChartCodeBean = new ChartOfAccountBean("Beg", " ", " ", " ", " ", " ", " ", numberFormat.format(periodbalance), " ");
		chartOfAccountBeanList.add(objChartCodeBean);
		objChartCodeBean = null;

	    }
	    prevAcct = acct;
	    chartOfAccountBean.setAcct(acct);


	    chartOfAccountBean.setPeriod(period);

	    String linDate = "";
	    if (liDate != null) {
		linDate = simpleDateFormat.format(liDate);
	    }
	    chartOfAccountBean.setDate(linDate);
	    chartOfAccountBean.setSourcecode((String) row[1]);
	    chartOfAccountBean.setReference((String) row[2]);
	    chartOfAccountBean.setDescription((String) row[3]);
	    chartOfAccountBean.setDebit(numberFormat.format(Double.parseDouble(debit)));
	    chartOfAccountBean.setCredit(numberFormat.format(Double.parseDouble(credit)));
	    chartOfAccountBean.setNetchange(numberFormat.format(Double.parseDouble(debit) - Double.parseDouble(credit)));
	    chartOfAccountBean.setCurrency((String) row[6]);
	    periodbalance += Double.parseDouble(debit) - Double.parseDouble(credit);
	    chartOfAccountBean.setBalance(numberFormat.format(periodbalance));
	    chartOfAccountBean.setSourceamount(numberFormat.format(Double.parseDouble(credit) + Double.parseDouble(debit)));
	    chartOfAccountBeanList.add(chartOfAccountBean);
	    tempv++;
	}
	return chartOfAccountBeanList;

    }

    public List getTransactions(String account, String account1, FiscalPeriod periodFrom, FiscalPeriod periodTo)throws Exception {
	return getTransactions(account, account1, periodFrom, periodTo, null);

    }

    //// Bad coding for date. To be deleted....
    public List findforSearch1(String account, String datefrom, String dateto, String sourceCode) {
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	ChartOfAccountBean objChartCodeBean = null;
	List<ChartOfAccountBean> acclist = new ArrayList<ChartOfAccountBean>();
	StringTokenizer st = new StringTokenizer(datefrom, "/");
	StringTokenizer st1 = new StringTokenizer(dateto, "/");
	String mon = "";
	String d = "";
	String year = "";
	String tempyear = "";
	String tempmon = "";
	String emon = "";
	String eyear = "";
	String ed = "";
	if (st.hasMoreElements()) {
	    mon = st.nextToken();
	    d = st.nextToken();
	    year = st.nextToken();
	    tempyear = year;
	    tempmon = mon;
	}
	if (st1.hasMoreElements()) {
	    emon = st1.nextToken();
	    ed = st1.nextToken();
	    eyear = st1.nextToken();
	}

	int m = Integer.parseInt(mon);

	m = m - 1;
	if (m <= 0) {
	    m = 12;
	    int y = Integer.parseInt(year);
	    year = Integer.toString(y - 1);
	    mon = "12";
	}


	String queryString1 = "select ab.periodBalance from AccountBalance ab where ab.account='" + account + "' and ab.period='" + m + "'and ab.year='" + year + "'";
	List pbal = getCurrentSession().createQuery(queryString1).list();

	year = tempyear;
	mon = tempmon;
	double bal;
	if (!pbal.isEmpty()) {
	    bal = (Double) pbal.get(0);
	} else {
	    bal = 0.00;
	}
	String sdate = year + "-" + mon + "-" + 01;
	String edate = year + "-" + mon + "-" + d;
	String queryforcredit = "select sum(line.credit) from LineItem line where line.account='" + account + "' and line.date between '" + sdate + "'and '" + edate + "'";
	String queryfordebit = "select sum(line.debit) from LineItem line where line.account='" + account + "' and line.date between '" + sdate + "'and '" + edate + "'";
	List c = getCurrentSession().createQuery(queryforcredit).list();
	List de = getCurrentSession().createQuery(queryfordebit).list();
	double tdebit = 0.0;
	double tcredit = 0.0;

	if (c.get(0) != null && de.get(0) != null) {
		tdebit = (Double) de.get(0);
		tcredit = (Double) c.get(0);
	}
	double periodbalance = bal + tcredit - tdebit;
	if (periodbalance >= 0 || periodbalance <= 0) {
	    objChartCodeBean = new ChartOfAccountBean();
	    String p = "Beg";
	    objChartCodeBean.setPeriod(p);
	    objChartCodeBean.setSourcecode(" ");
	    objChartCodeBean.setReference(" ");
	    objChartCodeBean.setDescription(" ");
	    objChartCodeBean.setDebit(" ");
	    objChartCodeBean.setCredit(" ");
	    objChartCodeBean.setCurrency(" ");
	    objChartCodeBean.setBalance(number.format(periodbalance));
	    objChartCodeBean.setSourceamount(" ");
	    acclist.add(objChartCodeBean);

	}
	dateto = eyear + "-" + emon + "-" + ed;
	String queryString = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency from LineItem line,JournalEntry je where je.journalEntryId=line.journalEntryId and line.account='" + account + "'and line.date Between '" + edate + "'and '" + dateto + "'";

	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	while (iter.hasNext()) {
	    objChartCodeBean = new ChartOfAccountBean();
	    Object[] row = (Object[]) iter.next();
	    String p = "1";
	    Date dat = (Date) row[0];
	    String date = sdf.format(dat);
	    StringTokenizer period = new StringTokenizer(date, "/");
	    if (period.hasMoreElements()) {
		p = period.nextToken();
	    }
	    String sourcecode = (String) row[1];
	    String reference = (String) row[2];
	    String description = (String) row[3];
	    String debit = row[4].toString();
	    String credit = row[5].toString();
	    String currency = (String) row[6];
	    double fbal = periodbalance + (Double.parseDouble(credit) - Double.parseDouble(debit));
	    periodbalance = fbal;
	    double samount = Double.parseDouble(credit) + Double.parseDouble(debit);
	    String sourceamount = Double.toString(samount);
	    objChartCodeBean.setPeriod(p);
	    objChartCodeBean.setDate(date);
	    objChartCodeBean.setSourcecode(sourcecode);
	    objChartCodeBean.setReference(reference);
	    objChartCodeBean.setDescription(description);
	    objChartCodeBean.setDebit(debit);
	    objChartCodeBean.setCredit(credit);
	    objChartCodeBean.setCurrency(currency);
	    objChartCodeBean.setBalance(number.format(fbal));
	    objChartCodeBean.setSourceamount(sourceamount);
	    acclist.add(objChartCodeBean);
	    objChartCodeBean = null;
	}
	return acclist;
    }

    //Find for Search for three arguments.....
    //select * from line_item line, journal_entry journal where line.journal_entry_id=journal.journal_entry_id and journal.period in (select id from fiscal_period where sysdate() between  start_date and end_date)
    public List findforSearch1(String account, String datefrom, String dateto) throws Exception {
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	ChartOfAccountBean objChartCodeBean = null;
	List<ChartOfAccountBean> acclist = new ArrayList<ChartOfAccountBean>();
	StringTokenizer st = new StringTokenizer(datefrom, "/");
	StringTokenizer st1 = new StringTokenizer(dateto, "/");
	String mon = "";
	String d = "";
	String year = "";
	String tempyear = "";
	String tempmon = "";
	String emon = "";
	String eyear = "";
	String ed = "";

	if (st.hasMoreElements()) {
	    mon = st.nextToken();
	    d = st.nextToken();
	    year = st.nextToken();
	    tempyear = year;
	    tempmon = mon;
	}
	if (st1.hasMoreElements()) {
	    emon = st1.nextToken();
	    ed = st1.nextToken();
	    eyear = st1.nextToken();
	}

	int m = Integer.parseInt(mon);

	m = m - 1;
	if (m <= 0) {
	    m = 12;
	    int y = Integer.parseInt(year);
	    year = Integer.toString(y - 1);
	    mon = "12";
	}
	String queryString1 = "select ab.periodBalance from AccountBalance ab where ab.account='" + account + "' and ab.period='" + m + "'and ab.year='" + year + "'";
	List pbal = getCurrentSession().createQuery(queryString1).list();
	year = tempyear;
	mon = tempmon;
	double bal;
	if (!pbal.isEmpty()) {
	    bal = (Double) pbal.get(0);
	} else {
	    bal = 0.00;
	}
	String sdate = year + "-" + mon + "-" + 01;
	String edate = year + "-" + mon + "-" + d;
	String queryforcredit = "select sum(line.credit) from LineItem line where line.account='" + account + "' and line.date between '" + sdate + "'and '" + edate + "'";
	String queryfordebit = "select sum(line.debit) from LineItem line where line.account='" + account + "' and line.date between '" + sdate + "'and '" + edate + "'";
	List c = getCurrentSession().createQuery(queryforcredit).list();
	List de = getCurrentSession().createQuery(queryfordebit).list();
	double tdebit = 0.0;
	double tcredit = 0.0;
	if (c.get(0) != null && de.get(0) != null) {
		tdebit = (Double) de.get(0);

		tcredit = (Double) c.get(0);
	}
	double periodbalance = bal + tcredit - tdebit;
	if (periodbalance >= 0 || periodbalance <= 0) {

	    AccountYearEndBalanceDAO accountYearEndBalanceDAO = new AccountYearEndBalanceDAO();
	    Integer prevYear = Integer.parseInt(year) - 1;
	    AccountYearEndBalance accountYearEndBalance = accountYearEndBalanceDAO.findAccountYearEndBalanceForAccountAndYear(prevYear, account);
	    objChartCodeBean = new ChartOfAccountBean();
	    String p = "Beg";
	    objChartCodeBean.setPeriod(p);
	    String date = mon + "/" + "01/" + year;
	    objChartCodeBean.setDate(date);
	    String sourcecode = " ";
	    objChartCodeBean.setSourcecode(sourcecode);
	    String reference = " ";
	    objChartCodeBean.setReference(reference);
	    String description = " ";
	    objChartCodeBean.setDescription(description);
	    String debit = " ";
	    objChartCodeBean.setDebit(debit);
	    String credit = " ";
	    objChartCodeBean.setCredit(credit);
	    String currency = " ";
	    objChartCodeBean.setCurrency(currency);
	    String balance = Double.toString(periodbalance);

	    if (accountYearEndBalance != null && accountYearEndBalance.getClosingBalance() != null) {
		objChartCodeBean.setBalance(number.format(accountYearEndBalance.getClosingBalance()));
	    }
	    String sourceamount = " ";
	    objChartCodeBean.setSourceamount(sourceamount);
	    acclist.add(objChartCodeBean);
	}
	dateto = eyear + "-" + emon + "-" + ed;
	String queryString = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,"
		+ "line.debit,line.credit,line.currency from LineItem line,JournalEntry je where je.journalEntryId=line.journalEntryId "
		+ "and line.account='" + account + "'and line.date Between '" + edate + "'and '" + dateto + "'";

	List queryObject = getCurrentSession().createQuery(queryString).list();

	Iterator iter = queryObject.iterator();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	while (iter.hasNext()) {
	    objChartCodeBean = new ChartOfAccountBean();
	    Object[] row = (Object[]) iter.next();
	    String p = "1";
	    Date dat = (Date) row[0];
	    String date = sdf.format(dat);
	    StringTokenizer period = new StringTokenizer(date, "/");
	    if (period.hasMoreElements()) {
		p = period.nextToken();
	    }
	    String sourcecode = (String) row[1];
	    String reference = (String) row[2];
	    String description = (String) row[3];
	    //String debit=row[4].toString();
	    Double debit = (Double) row[4];
	    //String credit=row[5].toString();
	    Double credit = (Double) row[5];
	    String currency = (String) row[6];
	    if (debit == null) {
		debit = 0.0;
	    }
	    if (credit == null) {
		credit = 0.0;
	    }
	    double fbal = periodbalance + credit - debit;
	    periodbalance = fbal;
	    double samount = credit + debit;
	    //String balance=Double.toString(fbal);
	    //String sourceamount=Double.toString(samount);
	    objChartCodeBean.setPeriod(p);
	    objChartCodeBean.setDate(date);
	    objChartCodeBean.setSourcecode(sourcecode);
	    objChartCodeBean.setReference(reference);
	    objChartCodeBean.setDescription(description);
	    //if(debit==null)
	    objChartCodeBean.setDebit(number.format(debit));
	    objChartCodeBean.setCredit(number.format(credit));
	    objChartCodeBean.setCurrency(currency);
	    objChartCodeBean.setBalance(number.format(fbal));
	    objChartCodeBean.setSourceamount(number.format(samount));
	    acclist.add(objChartCodeBean);
	    objChartCodeBean = null;
	}
	return acclist;
    }

    public List findforShowAll2(String account) {
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	ChartOfAccountBean coab = null;
	List<ChartOfAccountBean> acclist = new ArrayList<ChartOfAccountBean>();
	String queryString = "select line.date, je.sourceCode.code,je.journalEntryId,je.journalEntryDesc,line.debit,line.credit,line.currency from LineItem line,JournalEntry je where je.journalEntryId=line.journalEntryId and line.account='" + account + "'";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	while (iter.hasNext()) {
	    coab = new ChartOfAccountBean();
	    Object[] row = (Object[]) iter.next();

	    String p = "1";
	    Date dat = (Date) row[0];
	    String date = sdf.format(dat);
	    StringTokenizer period = new StringTokenizer(date, "/");
	    if (period.hasMoreElements()) {
		p = period.nextToken();
	    }
	    String sourcecode = (String) row[1];
	    String reference = (String) row[2];
	    String description = (String) row[3];
	    Double debit = (Double) row[4];
	    //String credit=row[5].toString();
	    Double credit = (Double) row[5];
	    String currency = (String) row[6];
	    if (debit == null) {
		debit = 0.0;
	    }
	    if (credit == null) {
		credit = 0.0;
	    }

	    double fbal = credit - debit;
	    double samount = credit + debit;
	    String balance = Double.toString(fbal);
	    String sourceamount = Double.toString(samount);
	    coab.setPeriod(p);
	    coab.setDate(date);
	    coab.setSourcecode(sourcecode);
	    coab.setReference(reference);
	    coab.setDescription(description);
	    coab.setDebit(number.format(debit));
	    coab.setCredit(number.format(credit));
	    coab.setCurrency(currency);

	    coab.setBalance(number.format(fbal));
	    coab.setSourceamount(number.format(samount));
	    acclist.add(coab);
	    coab = null;

	}
	return acclist;
    }

    public List getNullItem() throws Exception {
	    String queryString = "from LineItem where journalEntryId is null and account is null ";
	    Query queryObject = getSession().createQuery(queryString);
	    return queryObject.list();
    }

    /**
     * deleting a JournalEntry
     * @param jeId
     * @return
     */
    public void deleteLineItemsForJE(String jeId, String batchId)throws Exception {
	Integer i = new Integer(0);
	    Connection conn = ((SessionImplementor)getSession()).connection();
	    Statement st = conn.createStatement();

	    st.execute("delete from line_item where Journal_Entry_id='" + jeId + "'");
	    st.execute("delete from journal_entry where Journal_Entry_id='" + jeId + "'");
	    st.execute("delete from batch where batch_Id='" + batchId + "'");
	    if (st != null) {
		st.close();
	    }
	    if (conn != null) {
		conn.close();
	    }
    }

    public List<TransactionBean> getJournalEntriesForReconcile(ReconcileForm reconcileForm)throws Exception {
	List<TransactionBean> reconcileList = new ArrayList<TransactionBean>();
	String period = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getBankReconcileDate(), "MM/dd/yyyy"), "yyyyMM");
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select je.journal_entry_id,b.batch_id,format(li.credit,2),format(li.debit,2),li.line_item_id,if(li.Status!='',li.Status,'')");
	queryBuilder.append(" from batch b,journal_entry je,line_item li");
	queryBuilder.append(" where b.batch_id=je.batch_id and b.status!='deleted' and je.journal_entry_id=li.journal_entry_id");
	queryBuilder.append(" and (je.subledger_close is null or je.subledger_close!='").append(CommonConstants.YES).append("')");
	queryBuilder.append(" and (li.status is null or li.status='").append(CommonConstants.STATUS_RECONCILE_IN_PROGRESS).append("'");
	queryBuilder.append(" or li.status!='").append(CommonConstants.CLEAR).append("')");
	queryBuilder.append(" and li.account='").append(reconcileForm.getGlAccountNumber()).append("'");
	queryBuilder.append(" and je.period<='").append(period).append("'");
	getCurrentSession().flush();
	List<Object> resultList = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	for (Object row : resultList) {
	    TransactionBean transactionBean = new TransactionBean();
	    Object[] col = (Object[]) row;
	    transactionBean.setPaymentMethod("JE");
	    transactionBean.setCustomerReference(col[0].toString());
	    transactionBean.setBatchId(col[1].toString());
	    transactionBean.setCredit(col[2].toString());
	    transactionBean.setDebit(col[3].toString());
	    transactionBean.setTransactionId(col[4].toString() + "@" + CommonConstants.MODULE_LINE_ITEM);
	    transactionBean.setStatus(col[5].toString());
	    reconcileList.add(transactionBean);
	}
	return reconcileList;
    }

public Double getJEBalanceForGlAccount(String glAcct, String startPeriod, String endPeriod)throws Exception {
	    StringBuilder queryBuilder = new StringBuilder("select sum(li.debit)-sum(li.credit)");
	    queryBuilder.append(" from Batch b,JournalEntry je,LineItem li");
	    queryBuilder.append(" where b.batchId=je.batchId  and b.status='posted' and je.journalEntryId=li.journalEntryId");
	    queryBuilder.append(" and (je.subledgerClose is null or je.subledgerClose!='").append(CommonConstants.YES).append("')");
	    queryBuilder.append(" and li.account='").append(glAcct).append("'");
	    queryBuilder.append(" and je.period between '").append(startPeriod).append("' and '").append(endPeriod).append("'");
	    Object jeBalance =  getCurrentSession().createQuery(queryBuilder.toString()).uniqueResult();
            return null!=jeBalance?(Double)jeBalance:0d;
    }

    public double getJeBalanceForGlAccount(String glAcct, String period)throws Exception {
	    StringBuilder queryBuilder = new StringBuilder("select sum(li.debit)-sum(li.credit)");
	    queryBuilder.append(" from Batch b,JournalEntry je,LineItem li");
	    queryBuilder.append(" where b.batchId=je.batchId  and b.status='posted' and je.journalEntryId=li.journalEntryId");
	    queryBuilder.append(" and li.account='").append(glAcct).append("'");
	    queryBuilder.append(" and je.period='").append(period).append("'");
	    Object jeBalance =  getCurrentSession().createQuery(queryBuilder.toString()).uniqueResult();
            return null!=jeBalance?(Double)jeBalance:0d;
    }
}
