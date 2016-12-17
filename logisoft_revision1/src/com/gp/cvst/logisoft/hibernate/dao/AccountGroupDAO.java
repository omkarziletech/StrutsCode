package com.gp.cvst.logisoft.hibernate.dao;

import java.util.*;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.AccountGroup;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class AccountGroup.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.AccountGroup
 * @author MyEclipse - Hibernate Tools
 */
public class AccountGroupDAO extends BaseHibernateDAO {
    //property constants

    public static final String ACCT_GROUP = "acctGroup";
    public static final String ACCT_TYPE = "acctType";
    public static final String GROUP_DESC = "groupDesc";

    public void save(AccountGroup transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(AccountGroup persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public AccountGroup findById(java.lang.Integer id) throws Exception {
        AccountGroup instance = (AccountGroup) getSession().get("com.gp.cvst.logisoft.domain.AccountGroup", id);
        return instance;
    }

    public List findByExample(AccountGroup instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.AccountGroup").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from AccountGroup as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByAcctGroup(Object acctGroup) throws Exception {
        return findByProperty(ACCT_GROUP, acctGroup);
    }

    public List findByAcctType(Object acctType) throws Exception {
        return findByProperty(ACCT_TYPE, acctType);
    }

    public List findByGroupDesc(Object groupDesc) throws Exception {
        return findByProperty(GROUP_DESC, groupDesc);
    }

    public AccountGroup merge(AccountGroup detachedInstance) throws Exception {
        AccountGroup result = (AccountGroup) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(AccountGroup instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(AccountGroup instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Iterator getAcctGroups(String accttype) throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select accountgroup.acctGroup from AccountGroup accountgroup where accountgroup.acctType='" + accttype + "'").list().iterator();
        return results;
    }

    public String getAcctgroupDesc(String acctgroup) {
        String result = null;
        Iterator it = getCurrentSession().
                createQuery("select accountgroup.groupDesc from AccountGroup accountgroup where accountgroup.acctGroup='" + acctgroup + "'").list().iterator();
        result = (String) it.next();
        return result;
    }

    public List<String> getAccountTypes() {
        String query = "select ucase(acct_type) as acct_type from account_group group by acct_type";
        return getCurrentSession().createSQLQuery(query).list();
    }
}
