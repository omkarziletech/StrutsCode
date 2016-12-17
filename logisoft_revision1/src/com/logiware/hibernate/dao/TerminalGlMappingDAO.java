package com.logiware.hibernate.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.form.TerminalGlMappingForm;
import com.logiware.hibernate.domain.TerminalGlMapping;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

/**
 *
 * @author Lakshminarayanan
 */
public class TerminalGlMappingDAO extends BaseHibernateDAO implements java.io.Serializable {
    //private static final long serialVersionUID = 923291650376998703L;

    public void save(TerminalGlMapping transientInstance) throws Exception {
	getCurrentSession().saveOrUpdate(transientInstance);
	getCurrentSession().flush();
    }

    public void update(TerminalGlMapping persistentInstance) throws Exception {
	getCurrentSession().update(persistentInstance);
	getCurrentSession().flush();
    }

    public void delete(TerminalGlMapping persistentInstance) throws Exception {
	getCurrentSession().delete(persistentInstance);
	getCurrentSession().flush();
    }

    public TerminalGlMapping findById(Integer id) throws Exception {
	TerminalGlMapping instance = (TerminalGlMapping) getCurrentSession().get("com.logiware.hibernate.domain.TerminalGlMapping", id);
	return instance;
    }

    public List<TerminalGlMapping> findByProperty(String propertyName, Object value) throws Exception {
	Query query = getCurrentSession().createQuery("from TerminalGlMapping as model where model." + propertyName + " = ?0");
	query.setParameter("0", value);
	return query.list();
    }

    public List<TerminalGlMapping> getTerminalGlMappings(TerminalGlMappingForm terminalGlMappingForm) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(TerminalGlMapping.class);
	criteria.addOrder(Order.asc("terminal"));
	return criteria.list();
    }
}
