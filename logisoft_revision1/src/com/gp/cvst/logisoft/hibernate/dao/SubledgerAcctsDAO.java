package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.AccountMaintenenceBean;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.SubledgerAccts;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class SubledgerAccts.
 * @see com.gp.cvst.logisoft.hibernate.dao.SubledgerAccts
 * @author MyEclipse - Hibernate Tools
 */
public class SubledgerAcctsDAO extends BaseHibernateDAO {

    public void save(SubledgerAccts transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(SubledgerAccts persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public SubledgerAccts findById(java.lang.Integer id)throws Exception {
            SubledgerAccts instance = (SubledgerAccts) getSession().get("com.gp.cvst.logisoft.hibernate.dao.SubledgerAccts", id);
            return instance;
    }

    public List findByExample(SubledgerAccts instance) throws Exception {
            List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.SubledgerAccts").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
            String queryString = "from SubledgerAccts as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public SubledgerAccts merge(SubledgerAccts detachedInstance) throws Exception {
            SubledgerAccts result = (SubledgerAccts) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(SubledgerAccts instance) throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(SubledgerAccts instance) throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List getSubledgeraddList(String account) throws Exception {
        Iterator results = null;
        AccountMaintenenceBean acbean = null;
        List<AccountMaintenenceBean> lstGenericIfo = new ArrayList<AccountMaintenenceBean>();
            results = getCurrentSession().createQuery(
                    "select a.subLedgerCode from Subledger a,SubledgerAccts b where a.subLedgerId=b.subLedgerId and b.controlAcct='" + account + "'").list().iterator();

            while (results.hasNext()) {
                acbean = new AccountMaintenenceBean();


                /**
                 *id1 is local variable
                 * id is id in GenericCode table
                 * strCode is code in GenericCode table
                 * strDesc is codedesc in GenericCode table
                 * strCodedesc is description in Codetype table
                 */
                String subledger = (String) results.next();
                acbean.setSubledger(subledger);

                lstGenericIfo.add(acbean);
//    acbean = null;

            }
            return lstGenericIfo;
    }

    public List getSLlist(String account) throws Exception {
        List list = new ArrayList();
            String queryString = "select b.subLedgerId from SubledgerAccts b where b.controlAcct='" + account + "'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            list = queryObject.list();
        return list;

    }

    public AccountDetails getControlAccount(String subledgerType)throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select ad.Account,ad.Acct_Desc from subledger_accts sa,subledger su,account_details ad");
	queryBuilder.append(" where ad.Account=sa.Control_Acct and sa.SubLedger_id=su.SubLedger_Id");
	queryBuilder.append(" and su.SubLedger_Code='").append(subledgerType).append("' limit 1");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	if(null!=result){
	    Object[] col = (Object[])result;
	    AccountDetails accountDetails = new AccountDetails();
	    accountDetails.setAccount((String)col[0]);
	    accountDetails.setAcctDesc((String)col[1]);
	    return accountDetails;
	}
	return null;
    }
}
