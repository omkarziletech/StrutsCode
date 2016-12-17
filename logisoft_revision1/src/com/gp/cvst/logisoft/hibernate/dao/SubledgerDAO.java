package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.Subledger;
import java.util.Date;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Subledger.
 * @see com.gp.cvst.logisoft.hibernate.dao.Subledger
 * @author MyEclipse - Hibernate Tools
 */
public class SubledgerDAO extends BaseHibernateDAO {

    public void save(Subledger transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Subledger persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public Subledger findById(java.lang.Integer id) throws Exception {
        Subledger instance = (Subledger) getSession().get("com.gp.cvst.logisoft.domain.Subledger", id);
        return instance;
    }

    public List findByExample(Subledger instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.Subledger").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Subledger as model where model." + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value + "%");
        return queryObject.list();
    }

    public Subledger merge(Subledger detachedInstance) throws Exception {
        Subledger result = (Subledger) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(Subledger instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Subledger instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
    //Getting Subledger List from Subledger table...............by  Pradeep

    public Iterator getSubledgerList() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery("select subledger.subLedgerId,subledger.subLedgerCode from Subledger subledger order by subledger.subLedgerCode").list().iterator();
        return results;
    }

    public List getUniqueSubLedgerList() throws Exception {
        List results = null;
        results = getCurrentSession().createQuery("select subledger.subLedgerId,subledger.subLedgerCode from Subledger subledger where subledger.subLedgerId not in (select distinct(subledgerAccts.subLedgerId) from SubledgerAccts subledgerAccts) order by subledger.subLedgerCode").list();
        return results;
    }

    public Iterator getUniqueSubLedgerList1() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery("select subledger.shipmentType,subledger.shipmentType from Subledger subledger").list().iterator();
        return results;
    }

    public String getid(String subledger) throws Exception {
        Iterator results = null;
        String id = "0";
        results = getCurrentSession().createQuery(
                "select subledger.subLedgerId from Subledger subledger where subledger.subLedgerCode='" + subledger + "'").list().iterator();
        if (results.hasNext()) {
            id = results.next().toString();
        }
        return id;
    }

    public Double getSubLedgerBalanceForGlAccount(String glAcct, Date startDate, Date endDate) throws Exception {
        Double subledgerBalance = 0d;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(tl.Transaction_amt)");
        queryBuilder.append("  from transaction_ledger tl where (tl.Subledger_Source_code!='' or tl.Subledger_Source_code is not null)");
        queryBuilder.append(" and tl.GL_account_number='").append(glAcct).append("'");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') between '").append(DateUtils.formatDate(startDate, "yyyy-MM-dd")).append("'");
        queryBuilder.append(" and '").append(DateUtils.formatDate(endDate, "yyyy-MM-dd")).append("'");
        queryBuilder.append(" and (tl.Status='").append(CommonConstants.STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(CommonConstants.STATUS_CHARGE_CODE).append("'");
        queryBuilder.append(" or tl.Status='").append(CommonConstants.STATUS_PAID).append("')");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            subledgerBalance = Double.parseDouble(result.toString());
        }
        return subledgerBalance;
    }
}
