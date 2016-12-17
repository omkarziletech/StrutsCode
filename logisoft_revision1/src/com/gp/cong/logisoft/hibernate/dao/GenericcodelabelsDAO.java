package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.Genericcodelabels;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class Genericcodelabels.
 * @see com.gp.cong.logisoft.hibernate.dao.Genericcodelabels
 * @author MyEclipse - Hibernate Tools
 */
public class GenericcodelabelsDAO extends BaseHibernateDAO {

    //property constants
    public static final String CODETYPEID = "codetypeid";
    public static final String LABEL = "label";
    public static final String INPUTTYPE = "inputtype";

    public void save(Genericcodelabels transientInstance) throws Exception {
        getSession().save(transientInstance);
    }

    public void delete(Genericcodelabels persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public Genericcodelabels findById(java.lang.Integer id) throws Exception {
        Genericcodelabels instance = (Genericcodelabels) getSession().get("com.gp.cong.logisoft.domain.Genericcodelabels", id);
        return instance;
    }

    public List findByExample(Genericcodelabels instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cong.logisoft.domain.Genericcodelabels").add(Example.create(instance)).list();
        return results;
    }

    public List findByCodetypeId(Integer codeTypeId) throws Exception {
        String queryString = "select label from Genericcodelabels where codetypeid=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Genericcodelabels as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByCodetypeid(Object codetypeid) throws Exception {
        return findByProperty(CODETYPEID, codetypeid);
    }

    public List findByLabel(Object label) throws Exception {
        return findByProperty(LABEL, label);
    }

    public List findByInputtype(Object inputtype) throws Exception {
        return findByProperty(INPUTTYPE, inputtype);
    }

    public Genericcodelabels merge(Genericcodelabels detachedInstance) throws Exception {
        Genericcodelabels result = (Genericcodelabels) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(Genericcodelabels instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Genericcodelabels instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Iterator getAllCodesDetails(int i) throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select generic.label,generic.inputtype from Genericcodelabels generic where "
                + "generic.codetypeid=" + i).list().iterator();
        return results;
    }

    public List<Genericcodelabels> findForTableHeader(int codeTypeId) throws Exception {
        List<Genericcodelabels> genericHeader = null;
        genericHeader = getCurrentSession().createQuery("from Genericcodelabels code where code.codetypeid=" + codeTypeId).list();
        return genericHeader;
    }
    
    public List<String> getLabels(Integer codeTypeId){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  label ");
        queryBuilder.append("from");
        queryBuilder.append("  genericcodelabels ");
        queryBuilder.append("where");
        queryBuilder.append("  codetypeid = ?0");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", codeTypeId);
        query.addScalar("label", StringType.INSTANCE);
        return query.list();
    }
    
    public List<Genericcodelabels> getGenericCodeLabels(Integer codeTypeId){
        Criteria criteria = getCurrentSession().createCriteria(Genericcodelabels.class);
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        return criteria.list();
    }
}
