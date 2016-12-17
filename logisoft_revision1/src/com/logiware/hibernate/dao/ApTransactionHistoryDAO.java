package com.logiware.hibernate.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApTransactionHistoryDAO extends BaseHibernateDAO {

    public void save(ApTransactionHistory transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public ApTransactionHistory findById(java.lang.Integer id) throws Exception {
        return (ApTransactionHistory) getCurrentSession().get(ApTransactionHistory.class, id);
    }

    public List<ApTransactionHistory> getApTransactionHistories(String arBatchId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ApTransactionHistory.class);
        criteria.add(Restrictions.eq("arBatchId", Integer.parseInt(arBatchId)));
        return criteria.list();
    }
}
