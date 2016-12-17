package com.gp.cvst.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.BlVoid;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class AccountBalance.
 * @see com.gp.cvst.logisoft.hibernate.dao.AccountBalance
 * @author MyEclipse - Hibernate Tools
 */
/**
 * @author user
 *
 */
public class BlVoidDAO extends BaseHibernateDAO {

	//property constants
	public static final String TRANSACTION_LEDGER_ID = "transactionLedgerId";
	public static final String TRANSACTION_TYPE = "transactionType";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String CUSTOMER_NUMBER = "customerNumber";
	public static final String CHARGE_CODE = "chargeCode";
	public static final String AMOUNT = "amount";

    
    public void save(BlVoid transientInstance)throws Exception {
            getSession().save(transientInstance);
            getSession().flush();
    }
    
	public void delete(BlVoid persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }
    
    public BlVoid findById( java.lang.Integer id) {
            BlVoid instance = (BlVoid) getSession()
                    .get("com.gp.cvst.logisoft.domain.BlVoid", id);
            return instance;
    }
    
    
    public List findByExample(BlVoid instance)throws Exception {
            List results = getSession()
                    .createCriteria("com.gp.cvst.logisoft.domain.BlVoid")
                    .add(Example.create(instance))
            .list();
            return results;
    }    
    
    public List findByProperty(String propertyName, Object value)throws Exception {
         String queryString = "from BlVoid as model where model." 
         						+ propertyName + "= ?0";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter("0", value);
		 return queryObject.list();
	}

	public List findByTransactionLedgerId(Object transactionLedgerId)throws Exception {
		return findByProperty(TRANSACTION_LEDGER_ID, transactionLedgerId);
	}
	
	public List findByTransactionType(Object transactionType)throws Exception {
		return findByProperty(TRANSACTION_TYPE, transactionType);
	}
	
	public List findByCustomerName(Object customerName)throws Exception {
		return findByProperty(CUSTOMER_NAME, customerName);
	}
	
	public List findByCustomerNumber(Object customerNumber)throws Exception {
		return findByProperty(CUSTOMER_NUMBER, customerNumber);
	}
	
	public List findByChargeCode(Object chargeCode)throws Exception {
		return findByProperty(CHARGE_CODE, chargeCode);
	}
	
	public List findByAmount(Object amount)throws Exception {
		return findByProperty(AMOUNT, amount);
	}
	
    public BlVoid merge(BlVoid detachedInstance)throws Exception {
            BlVoid result = (BlVoid) getSession()
                    .merge(detachedInstance);
            return result;
    }

    public void attachDirty(BlVoid instance)throws Exception {
            getSession().saveOrUpdate(instance);
    }
    
    public void attachClean(BlVoid instance)throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

	
}
