package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.UserBankAccountPermission;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class UserBankAccountPermission.
 * @see com.gp.cvst.logisoft.hibernate.dao.UserBankAccountPermission
 * @author MyEclipse - Hibernate Tools
 */
public class UserBankAccountPermissionDAO extends BaseHibernateDAO {

    //property constants
    public static final String BANK_ID = "bankId";
    public static final String USER_ID = "userId";

    public void save(UserBankAccountPermission transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(UserBankAccountPermission persistentInstance) throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public UserBankAccountPermission findById(java.lang.Integer id)throws Exception {
            UserBankAccountPermission instance = (UserBankAccountPermission) getSession().get("com.gp.cvst.logisoft.hibernate.dao.UserBankAccountPermission", id);
            return instance;
    }

    public List findByExample(UserBankAccountPermission instance) throws Exception {
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.UserBankAccountPermission").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
            String queryString = "from UserBankAccountPermission as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByBankId(Object bankId) throws Exception {
        return findByProperty(BANK_ID, bankId);
    }

    public List findByUserId(Object userId) throws Exception {
        return findByProperty(USER_ID, userId);
    }

    public UserBankAccountPermission merge(UserBankAccountPermission detachedInstance)throws Exception {
            UserBankAccountPermission result = (UserBankAccountPermission) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(UserBankAccountPermission instance) throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(UserBankAccountPermission instance) throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Iterator getBankAccount(Integer user) throws Exception {
        Iterator results = null;
            List<Integer> bankIdList = new ArrayList<Integer>();
			getCurrentSession().flush();
            bankIdList = getCurrentSession().createSQLQuery(
                    "select bank_id from bank_user where user_id='" + user + "'").list();
            String bankIdString = "";
            for (Integer bid : bankIdList) {
                if (null != bid) {
                    if (!bankIdString.trim().equals("")) {
                        bankIdString = bankIdString + ',';
                    }
                    bankIdString = bankIdString + bid.toString();
                }
            }
            BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
            results = bankDetailsDAO.getbankAccountDetails(bankIdString);
        return results;
    }
}
