package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.FiscalYear;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class FiscalYear.
 * @see com.gp.cvst.logisoft.hibernate.dao.FiscalYear
 * @author MyEclipse - Hibernate Tools
 */
public class FiscalYearDAO extends BaseHibernateDAO {

    public void save(FiscalYear transientInstance) {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(FiscalYear transientInstance)throws Exception{
	    getSession().update(transientInstance);
	    getSession().flush();
    }

    public void delete(FiscalYear persistentInstance)throws Exception{
	    getSession().delete(persistentInstance);
	    getSession().flush();
    }

    public FiscalYear findById(java.lang.Integer id)throws Exception{
	    FiscalYear instance = (FiscalYear) getSession().get("com.gp.cvst.logisoft.domain.FiscalYear", id);
	    return instance;
    }

    public void closeFiscalYearAndItsPeriods(Integer year) throws Exception{
	 String query = "update fiscal_year set active='Close',cls_period='Close',adj_period='Close' where year="+year;
	 getCurrentSession().createSQLQuery(query).executeUpdate();
	 query = "update fiscal_period set status='Close' where year="+year;
	 getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public List findByExample(FiscalYear instance) throws Exception{
	    List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.FiscalYear").add(Example.create(instance)).list();
	    return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception{
	    String queryString = "from FiscalYear as model where model." + propertyName + "= ?0";
	    Query queryObject = getSession().createQuery(queryString);
	    queryObject.setParameter("0", value);
	    return queryObject.list();
    }

    public FiscalYear merge(FiscalYear detachedInstance) throws Exception{
	    FiscalYear result = (FiscalYear) getSession().merge(detachedInstance);
	    return result;
    }

    public void attachDirty(FiscalYear instance)throws Exception{
	    getSession().saveOrUpdate(instance);
	    getSession().flush();
    }

    public void attachClean(FiscalYear instance) throws Exception{
	    getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public int findfisyearid(int year) throws Exception{
	int result = 0;
	String qeury = "select  fy.id from FiscalYear fy where fy.year='" + year + "'";
	    Iterator it = getCurrentSession().createQuery(qeury).list().iterator();
	    result = (Integer) it.next();
	    if (result == 0) {
		result = 0;
	    }
	return result;
    }

    public String getcount(String active)throws Exception{
	String result = null;
	String queryString = "SELECT count( * ) as total_record  FROM FiscalYear fis where fis.active='" + active + "'";
	String queryObject = getCurrentSession().createQuery(queryString).toString();
	return result;
    }

    public List activevalue(String active)throws Exception{
	List list = new ArrayList();
	    String queryString = "SELECT fis.year from FiscalYear fis where fis.active='" + active + "'";
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    list = queryObject.list();
	return list;
    }

    public Iterator yearlist() throws Exception{
	Iterator list = null;
	    String queryString = "SELECT distinct fis.year from FiscalYear fis where active='Open'";
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    list = queryObject.list().iterator();
	return list;
    }

    public Iterator getFiscalYearForStatus(String status)throws Exception{
	Iterator list = null;
	    String queryString = null;
	    if (status == null || status.equals("") || status.equals("ALL")) {
		queryString = "SELECT distinct fis.year from FiscalYear fis order by fis.year desc ";
	    } else {
		queryString = "SELECT distinct fis.year from FiscalYear fis where active='" + status + "'";
	    }

	    Query queryObject = getCurrentSession().createQuery(queryString);
	    list = queryObject.list().iterator();
	return list;
    }

    public List distinctYear()throws Exception{
	List list = null;
	    String queryString = "SELECT distinct fis.year from FiscalYear fis ";
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    list = queryObject.list();
	return list;

    }

    public String getFiscalYearStatus(String year)throws Exception{
	String status = "";
	    String queryString = "SELECT distinct fis.active from FiscalYear fis where fis.year =" + year;
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    status = (String) queryObject.uniqueResult();
	return status;
    }
}
