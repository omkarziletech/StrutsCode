package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.Domain;
import java.util.List;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.hibernate.BaseHibernateDAO;
import java.util.Iterator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * A data access object (DAO) providing persistence and search support for
 * SedFilings entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 *
 * @see com.gp.cong.logisoft.domain.SedFilings
 * @author MyEclipse Persistence Tools
 */
public class SedFilingsDAO extends BaseHibernateDAO {


    public void save(SedFilings transientInstance)throws Exception{
	    getSession().saveOrUpdate(transientInstance);
	    getSession().flush();
    }

    public void update(SedFilings persistanceInstance) throws Exception{
	    getSession().update(persistanceInstance);
	    getSession().flush();
    }

    public void delete(SedFilings persistentInstance) throws Exception{
	    getSession().delete(persistentInstance);
	    getSession().flush();
    }

    public SedFilings findById(int id) throws Exception{
	    SedFilings instance = (SedFilings) getSession().get("com.gp.cong.logisoft.domain.SedFilings", id);
	    return instance;
    }

    public List findByDr(String fileNo) throws Exception{
	    Criteria criteria = getCurrentSession().createCriteria(SedFilings.class);
	    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	    if (fileNo != null && !fileNo.equals("")) {
		criteria.add(Restrictions.like("shpdr", fileNo + "%"));
		criteria.addOrder(Order.asc("shpdr"));
	    }
	    return criteria.list();
    }

    public SedFilings findByFileNumber(String fileNo) throws Exception{
	Iterator results = null;
	SedFilings sedFilings = null;
	    Criteria criteria = getCurrentSession().createCriteria(SedFilings.class);
	    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	    if (fileNo != null && !fileNo.equals("")) {
		criteria.add(Restrictions.like("shpdr", fileNo + "%"));
		criteria.addOrder(Order.asc("shpdr"));
	    }
	    results = criteria.list().iterator();
	    while (results.hasNext()) {
		sedFilings = (SedFilings) results.next();
	    }
	    return sedFilings;
    }

    public SedFilings findByTrnref(String trnref) throws Exception{
	Criteria criteria = getCurrentSession().createCriteria(SedFilings.class);
	criteria.add(Restrictions.like("trnref", trnref + "%"));
	criteria.addOrder(Order.desc("id"));
	criteria.setMaxResults(1);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	return(SedFilings)criteria.uniqueResult();
    }

    public int getSedCount(String fileName) throws Exception{
	int count = 0;
	    String queryString = "select COUNT(*) from sed_filings where SHPDR like ?0";
	    Query queryObject = getSession().createSQLQuery(queryString);
	    queryObject.setParameter("0", fileName);
	    count = Integer.parseInt(queryObject.uniqueResult().toString());
	    return count;
    }

    public List getSedFilingsList(String fileNo) throws Exception{
        String queryString = "from SedFilings where SHPDR='" + fileNo + "'";
        Query queryObject = getSession().createQuery(queryString);
	return queryObject.list();
    }

    public Domain saveAndReturn(Domain instance) throws Exception{
	    getCurrentSession().saveOrUpdate(instance);
	    getCurrentSession().flush();
	    getCurrentSession().clear();
	    instance = (Domain) new BaseHibernateDAO().findByInstance(instance).get(0);
	    return instance;
    }
    
    public void updateBookingNumber(String fileNumber,String bookingNumber)throws Exception{
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("update sed_filings");
	queryBuilder.append(" set bkgnum = '").append(bookingNumber).append("'");
	queryBuilder.append(" where shpdr = '").append(fileNumber).append("'");
	queryBuilder.append(" and status = 'N'");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	getCurrentSession().flush();
    }

    public String getCommentsOfAes(Integer id)throws Exception {
        String query="SELECT aes_comment FROM sed_filings WHERE id='"+id+"' limit 1";
        Object result = getSession().createSQLQuery(query).uniqueResult();
	return null!=result?result.toString():"";
    }
}