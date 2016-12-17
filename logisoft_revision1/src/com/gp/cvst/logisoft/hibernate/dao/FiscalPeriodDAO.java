package com.gp.cvst.logisoft.hibernate.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.FiscalPeriodBean;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.reports.dto.fiscalPeriodDTO;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Order;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class FiscalPeriod.
 * @see com.gp.cvst.logisoft.hibernate.dao.FiscalPeriod
 * @author MyEclipse - Hibernate Tools
 */
public class FiscalPeriodDAO extends BaseHibernateDAO implements ConstantsInterface {

    public static final String PERIOD = "period";
    public static final String YEAR = "year";
    public static final String STATUS = "status";
    public static final String PERIOD_DIS = "periodDis";

    public void save(FiscalPeriod transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(FiscalPeriod persistentInstance) throws Exception{
	    getSession().delete(persistentInstance);
	    getSession().flush();
    }

    public FiscalPeriod findById(java.lang.Integer id)throws Exception{
	    FiscalPeriod instance = (FiscalPeriod) getSession().get("com.gp.cvst.logisoft.domain.FiscalPeriod", id);
	    return instance;
    }

    public List findByExample(FiscalPeriod instance)throws Exception{
	    List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.FiscalPeriod").add(Example.create(instance)).list();
	    return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception{
	    String queryString = "from FiscalPeriod as model where model." + propertyName + " like ?0 and periodDis not like '%AD' and periodDis not like '%CL'";
	    Query queryObject = getSession().createQuery(queryString);
	    queryObject.setParameter("0", value);
	    return queryObject.list();
    }

    public List findByPeriod(Object period)throws Exception{
	return findByProperty(PERIOD, period);
    }

    public List findByYear(Object year)throws Exception{
	return findByProperty(YEAR, year);
    }

    public List findByStatus(Object status)throws Exception{
	return findByProperty(STATUS, status);
    }

    public List findByPeriodDis(Object periodDis)throws Exception{
	return findByProperty(PERIOD_DIS, periodDis);
    }

    public FiscalPeriod merge(FiscalPeriod detachedInstance)throws Exception{
	    FiscalPeriod result = (FiscalPeriod) getSession().merge(detachedInstance);
	    return result;
    }

    public void attachClean(FiscalPeriod instance)throws Exception{
	    getSession().buildLockRequest(LockOptions.NONE).lock(instance);
	    getSession().flush();
    }

    public Iterator getPeriodList()throws Exception{
	Iterator results = null;
	    results = getCurrentSession().createQuery(
		    "select fp.id,fp.periodDis from FiscalPeriod fp where fp.status='open' order by fp.periodDis desc").list().iterator();
	return results;
    }

    public Iterator getPeriodList2(String jeperiod)throws Exception{
	Iterator results = null;
	    results = getCurrentSession().createQuery(
		    "select fp.periodDis,fp.periodDis from FiscalPeriod fp where fp.status='open' or fp.periodDis='" + jeperiod + "' order by fp.periodDis desc").list().iterator();
	return results;
    }

    public List getPeriodList3(String period)throws Exception{
	List results = null;
	    String queryString =
		    "from FiscalPeriod fp where fp.periodDis=?0";
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    queryObject.setParameter("0", period);
	    results = queryObject.list();
	return results;
    }

    public Iterator getallyeardetails()throws Exception{
	Iterator results = null;
	    results = getCurrentSession().createQuery("select distinct fiscal.year from FiscalPeriod fiscal ORDER BY fiscal.year DESC").list().iterator();
	return results;
    }

    public String getStartDate(String year, String period)throws Exception{
	Date startDate = (Date) getCurrentSession().createQuery("select distinct fp.startDate from FiscalPeriod fp where fp.year='" + year + "' and fp.period='" + period + "'").uniqueResult();
	return null != startDate ? DateUtils.formatDate(startDate, "MM/dd/yyyy") : null;
    }

    public String getEndDate(String year, String period)throws Exception{
	Date endDate = (Date) getCurrentSession().createQuery("select distinct fp.endDate from FiscalPeriod fp where fp.year='" + year + "' and fp.period='" + period + "'").uniqueResult();
	return null != endDate ? DateUtils.formatDate(endDate, "MM/dd/yyyy") : null;
    }

    // To get Start & End Date
    public Iterator getStartEndDate(String year, String period)throws Exception{
	String queryString = "select fp.startDate,fp.endDate from FiscalPeriod fp where fp.year='" + year + "' and fp.period='" + period + "'";
	Iterator it = null;
	    it = getCurrentSession().createQuery(queryString).list().iterator();
	return it;
    }

    public List getdateList(String periodlist)throws Exception{
	List result = new ArrayList();
	String queryString = "select fp.startDate,fp.endDate from FiscalPeriod fp where fp.id='" + periodlist + "'";
	    List queryObject = getCurrentSession().createQuery(queryString).list();
	    Iterator itr = queryObject.iterator();
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    while (itr.hasNext()) {
		Object[] row = (Object[]) itr.next();
		Date startdate = (Date) row[0];
		Date enddate = (Date) row[1];

		String newdate = sdf.format(startdate);
		String endDate = sdf.format(enddate);

		result.add(newdate);
		result.add(endDate);

	    }
	return result;
    }

    public List findforsearch(int fisyear)throws Exception{
	    FiscalPeriodBean objFiscalPeriodBean = null;
	    List<FiscalPeriodBean> lstGenericIfo = new ArrayList<FiscalPeriodBean>();

	    String queryString = "select fis.startDate,fis.endDate,fis.status,fis.period from  FiscalPeriod fis where fis.year='" + fisyear + "'";
	    List queryObject = getCurrentSession().createQuery(queryString).list();
	    Iterator iter = queryObject.iterator();
	    int y = 0;
	    while (iter.hasNext()) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		objFiscalPeriodBean = new FiscalPeriodBean();
		Object[] row = (Object[]) iter.next();


		Date stardate = (Date) (row[0]);

		Date endate = (Date) (row[1]);
		String status = (String) row[2];
		String period = (String) row[3];
		String stardate1 = sdf.format(stardate);
		String endate1 = sdf.format(endate);
		//String status="close";
		objFiscalPeriodBean.setStaringdate(stardate1);
		objFiscalPeriodBean.setEndingdate(endate1);
		objFiscalPeriodBean.setStatus(status);
		objFiscalPeriodBean.setPeriod(period);
		lstGenericIfo.add(objFiscalPeriodBean);
		objFiscalPeriodBean = null;


	    }

	    return lstGenericIfo;
    }

    public String selctidonly(String id)throws Exception{
	int id1 = Integer.parseInt(id);
	String result = "";
	    String query = "SELECT fis.year from FiscalPeriod fis where fis.period='" + id1 + "'";
	    Iterator abc = getCurrentSession().createQuery(query).list().iterator();

	    int id2 = (Integer) abc.next();
	    result = String.valueOf(id2);
	return result;
    }

    public List comparevalue(int year) throws Exception{
	List list = new ArrayList();
	    String queryString = "SELECT fis.year from FiscalPeriod fis where fis.year='" + year + "'";
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    list = queryObject.list();
	return list;
    }

    public void yearupdate(String open, int newyear1) throws Exception{
	    String query = "UPDATE FiscalPeriod fis set fis.status='" + open + "' where fis.year='" + newyear1 + "'  ";
	    getCurrentSession().createQuery(query).executeUpdate();
    }

    public void update(FiscalPeriod persistanceInstance)throws Exception{
	    getSession().saveOrUpdate(persistanceInstance);
	    getSession().flush();
    }

    public List findYear(Integer year)throws Exception{
	    Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
	    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	    //if(year!=0)
	    if (year != null && !year.equals("")) {
		criteria.add(Restrictions.like("year", year));
	    }
	    return criteria.list();
    }

    public FiscalPeriod findYear(Integer year, String period) throws Exception{
	    Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
	    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	    if (year != null ) {
		criteria.add(Restrictions.like("year", year));
	    }
	    if (period != null && !period.equals("")) {
		criteria.add(Restrictions.like("period", period));
	    }
	    return (FiscalPeriod)criteria.setMaxResults(1).uniqueResult();
    }

    public int findid(String fisperiod1) throws Exception{
        String query = "SELECT fis.id from FiscalPeriod fis where fis.period='" + fisperiod1 + "'";
        Object id2 = getCurrentSession().createQuery(query).setMaxResults(1).uniqueResult();
	return null!=id2?(Integer)id2:0;
    }

    public List getPeriodList1(String year)throws Exception{
	fiscalPeriodDTO fisPerioddto = null;
	List<fiscalPeriodDTO> fiscalperiodList = new ArrayList<fiscalPeriodDTO>();
	String queryString = "select fis.startDate,fis.endDate,fis.status,fis.period,fis.year from "
		+ " FiscalPeriod fis where fis.year='" + year + "'  order by period";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	int y = 0;
	    while (iter.hasNext()) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		fisPerioddto = new fiscalPeriodDTO();
		Object[] row = (Object[]) iter.next();
		Date stardate = (Date) (row[0]);
		String stardate1 = sdf.format(stardate);
		Date endate = (Date) (row[1]);
		String endate1 = sdf.format(endate);
		String status = (String) row[2];
		String period = (String) row[3];
		Integer year1 = (Integer) row[4];
		String year2 = String.valueOf(year1);
		if (stardate1 == null) {
		    stardate1 = "";
		}
		if (endate1 == null) {
		    endate1 = "";
		}
		if (status == null) {
		    status = "";
		}
		if (period == null) {
		    period = "";
		}
		fisPerioddto.setStartingdate(stardate1);
		fisPerioddto.setEndingdate(endate1);
		fisPerioddto.setStatus(status);
		fisPerioddto.setPeriod(period);
		fisPerioddto.setYear(year2);
		fiscalperiodList.add(fisPerioddto);
		fisPerioddto = null;
	    }
	return fiscalperiodList;
    }

    public FiscalPeriod getPeriodForCurrentDate()throws Exception{
	return getFiscalPeriodForDate(new Date());
    }

    public FiscalPeriod getFiscalPeriodForDate(Date date)throws Exception{
	    if (date == null) {
		date = new Date();
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String queryString = " from FiscalPeriod fis where '" + sdf.format(date) + "' between fis.startDate and fis.endDate and status='Open'";
	    Query queryObject = getCurrentSession().createQuery(queryString);
            return (FiscalPeriod)queryObject.setMaxResults(1).uniqueResult();
    }

    public Integer getFiscalPeriodCountForStatus(String year, String status)throws Exception{
	    String queryString = "select count(*) from FiscalPeriod fis where fis.year='" + year + "' and fis.status='" + status + "' and fis.period!='CL' and fis.period!='AD'";
	    Query queryObject = getCurrentSession().createQuery(queryString);
            return (Integer)queryObject.uniqueResult();
    }

    public List getAllFiscalPeriods()throws Exception{
	return getCurrentSession().createQuery(" from FiscalPeriod fp where fp.status='Open' order by fp.periodDis desc ").list();
    }

    public List<FiscalPeriod> getFiscalPeriods()throws Exception{
	    return getCurrentSession().createQuery(" from FiscalPeriod fp order by fp.periodDis desc").list();
    }

    public List getAllFiscalPeriods1() throws Exception{
	List results = null;
	    results = getCurrentSession().createQuery(" from FiscalPeriod fp order by fp.periodDis desc ").list();
	return results;
    }

    public void unlockFiscalPeriod(String id) throws Exception{
	    String queryString = "update FiscalPeriod fis set fis.locked='N' where fis.id=" + id.trim();
	    getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public List getAllAccountAndBalancesByAccountTypeAndYear(Integer year, String accountType)throws Exception{
	    String queryString = "select d.account, sum(period_balance) as balance from account_Details d, account_balance b where d.account = b.account and d.acct_type = '" + accountType
		    + "' and b.year = " + year + " group by d.account having sum(period_balance) <> 0";
	    return getCurrentSession().createSQLQuery(queryString).addScalar("account", StringType.INSTANCE).addScalar("balance", DoubleType.INSTANCE).list();
    }

    public Double getAllAccountBalancesByAccountTypeAndYear(Integer year, String accountType)throws Exception{
	Double balance = 0d;
	    String queryString = "select sum(period_balance) as balance from account_Details d, account_balance b where d.account = b.account and d.acct_type = '" + accountType + "' and b.year = " + year;
	    balance = (Double) getCurrentSession().createSQLQuery(queryString).addScalar("balance", DoubleType.INSTANCE).uniqueResult();
	return balance;
    }

    public String getStatusByStartandEndDate(String startDate, String endDate) throws Exception{
	String queryString = "SELECT DISTINCT fp.status FROM fiscal_period fp WHERE DATE(fp.start_Date)<=DATE('" + startDate + "') AND DATE(fp.end_Date)>=DATE('" + endDate + "')";
	Query query = getCurrentSession().createSQLQuery(queryString);
	String status = "";
	List<String> resultList = query.list();
	if (null != resultList && !resultList.isEmpty()) {
	    if (resultList.size() > 1) {
		for (String result : resultList) {
		    if (result.trim().equals(STATUS_OPEN)) {
			status = result;
			break;
		    }
		}
	    } else {
		status = resultList.get(0);
	    }
	}
	return status;
    }

    public Integer getLastClosedPeriod(Integer period, Integer year)throws Exception{
	Integer lastClosedPeriod = null;
	    String queryString = "select period from fiscal_period "
		    + "where period<='" + NumberUtils.formatNumber(period, "00") + "' and year=" + year + " and status='Close' order by id desc limit 1";
	    Query query = getCurrentSession().createSQLQuery(queryString);
	    List list = query.list();
	    if (CommonUtils.isNotEmpty(list)) {
		for (Object object : list) {
		    if (CommonUtils.isEqual(object.toString(), "CL")
			    || CommonUtils.isEqual(object.toString(), "AD")) {
			lastClosedPeriod = 12;
		    } else {
			lastClosedPeriod = Integer.parseInt(object.toString());
		    }
		}
	    }
	return lastClosedPeriod;
    }

    public FiscalPeriod getLastClosedPeriod(String periodDis) throws Exception{
	Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
	criteria.add(Restrictions.le("periodDis", periodDis));
	criteria.add(Restrictions.eq("status", "close"));
	criteria.add(Restrictions.ne("period", "AD"));
	criteria.add(Restrictions.ne("period", "CL"));
	criteria.addOrder(Order.desc("periodDis"));
	criteria.setMaxResults(1);
	return (FiscalPeriod) criteria.uniqueResult();
    }

    public List<Integer> getAllOpenFiscalYears() throws Exception{
	    return getSession().createQuery(" select distinct year from FiscalPeriod where status='" + STATUS_OPEN + "'").list();
    }

    public List<Integer> getAllFiscalYears() {
	    return getSession().createQuery("select distinct year from FiscalPeriod order by year desc").list();
    }

    public FiscalPeriod getCurrentOpenPeriod()throws Exception{
	    Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
	    criteria.add(Restrictions.eq("status", STATUS_OPEN));
	    criteria.addOrder(Order.asc("year"));
	    criteria.addOrder(Order.asc("period"));
	    criteria.setMaxResults(1);
	    return (FiscalPeriod) criteria.uniqueResult();
    }

    public FiscalPeriod getNextOpenPeriod(String periodDis)throws Exception{
	    Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
	    criteria.add(Restrictions.gt("periodDis", periodDis));
	    criteria.add(Restrictions.eq("status", STATUS_OPEN));
	    criteria.addOrder(Order.asc("year"));
	    criteria.addOrder(Order.asc("period"));
	    criteria.setMaxResults(1);
	    return (FiscalPeriod) criteria.uniqueResult();
    }

    public FiscalPeriod getFiscalPeriod(String periodDis)throws Exception{
	    Criteria criteria = getCurrentSession().createCriteria(FiscalPeriod.class);
	    criteria.add(Restrictions.eq("periodDis", periodDis));
	    criteria.setMaxResults(1);
	    return (FiscalPeriod) criteria.uniqueResult();
    }

    public List<FiscalPeriod> getAllPeriodsForOpenYear(String periodDis)throws Exception{
	    StringBuilder queryBuilder = new StringBuilder("select fp from FiscalPeriod fp,FiscalYear fy");
	    queryBuilder.append(" where fy.active='Open'");
	    queryBuilder.append(" and fp.year=fy.year and fp.periodDis like ?0 order by fp.periodDis");
	    Query query = getSession().createQuery(queryBuilder.toString());
	    query.setParameter("0", periodDis);
	    return query.list();
    }

    public boolean checkSubledgersPendingForYearClosing(Integer year)throws Exception{
	    String startDate = year + "-01-01";
	    String endDate = year + "-12-31";
	    StringBuilder queryBuilder = new StringBuilder("select count(*) from transaction_ledger tl");
	    queryBuilder.append(" where ((tl.Subledger_Source_code='").append(SUB_LEDGER_CODE_PURCHASE_JOURNAL).append("'");
	    queryBuilder.append(" and tl.Status='").append(STATUS_OPEN).append("'");
	    queryBuilder.append(" and ((date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("')");
	    queryBuilder.append(" or (tl.posted_date is null and (date_format(tl.Transaction_date,'%Y-%m-%d') between '").append(startDate).append("'");
	    queryBuilder.append(" and '").append(endDate).append("')))");
	    queryBuilder.append(" or ((tl.Subledger_Source_code like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%'");
	    queryBuilder.append(" or tl.Subledger_Source_code='").append(SUB_LEDGER_CODE_CASH_DEPOSIT).append("'");
	    queryBuilder.append(" or tl.Subledger_Source_code='").append(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE).append("'");
	    queryBuilder.append(" or tl.Subledger_Source_code='").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("')");
	    queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
	    queryBuilder.append(" or tl.Status='").append(STATUS_PAID).append("'");
	    queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE).append("')");
	    queryBuilder.append(" and (date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'))))");
	    Object result = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
            return null != result && Integer.parseInt(result.toString()) > 0;
    }

    public boolean isPeriodOpenForThisDate(String date)throws Exception{
	StringBuilder queryBuilder = new StringBuilder("select status from fiscal_period");
	queryBuilder.append(" where start_date<='").append(date).append("'");
	queryBuilder.append(" and end_date>='").append(date).append("'");
	String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return null != result && result.equals(STATUS_OPEN);
    }
}
