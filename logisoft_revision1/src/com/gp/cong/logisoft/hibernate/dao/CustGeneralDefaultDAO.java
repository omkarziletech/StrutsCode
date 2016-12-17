package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.AesHistory;
import com.gp.cong.logisoft.domain.CustGeneralDefault;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CustGeneralDefaultDAO extends BaseHibernateDAO {

    public void save(CustGeneralDefault transientInstance) throws Exception {
	getSession().saveOrUpdate(transientInstance);
	getSession().flush();
    }

    public void update(CustGeneralDefault persistanceInstance) throws Exception {
	getSession().update(persistanceInstance);
	getSession().flush();
    }

    public void delete(CustGeneralDefault persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public AesHistory findById(Integer id) throws Exception {
	AesHistory instance = (AesHistory) getSession().get("com.gp.cong.logisoft.domain.CustGeneralDefault", id);
	return instance;
    }

    public CustGeneralDefault getGeneralDefaultByAccountNumber(String accountNumber, String applyDefault) throws Exception {
	CustGeneralDefault custGeneralDefault = null;
	Criteria criteria = getCurrentSession().createCriteria(CustGeneralDefault.class);
	criteria.add(Restrictions.eq("acctNo", accountNumber));
	if ("Y".equalsIgnoreCase(applyDefault)) {
	    criteria.add(Restrictions.eq("applyDefaultValues", applyDefault));
	}
	criteria.setMaxResults(1);
	custGeneralDefault = (CustGeneralDefault) criteria.uniqueResult();
	return custGeneralDefault;
    }

    public String getDefaultDetailsAlert(String AccNo) throws Exception {
	String query = "SELECT important_notes FROM cust_general_default WHERE acct_no='" + AccNo + "' limit 1";
	return (String) getCurrentSession().createSQLQuery(query.toString()).uniqueResult();

    }
}
