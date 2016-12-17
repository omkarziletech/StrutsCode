package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.AesHistory;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AesHistoryDAO extends BaseHibernateDAO {

    public void save(AesHistory transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public void update(AesHistory persistanceInstance) {
        getSession().update(persistanceInstance);
        getSession().flush();
    }

    public void delete(AesHistory persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public AesHistory findById(Integer id) throws Exception {
        AesHistory instance = (AesHistory) getSession().get("com.gp.cong.logisoft.domain.AesHistory", id);
        return instance;
    }

    public List findByItnNumber(String itn) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AesHistory.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (itn != null && !itn.equals("")) {
            criteria.add(Restrictions.like("fileNumber", "%" + itn + "%"));
            criteria.addOrder(Order.desc("id"));
        }
        return criteria.list();
    }

    public List searchItnList(String itn, String fileNumber, String status) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AesHistory.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (itn != null && !itn.equals("")) {
            criteria.add(Restrictions.like("itn", itn + "%"));
        }
        if (fileNumber != null && !fileNumber.equals("")) {
            criteria.add(Restrictions.like("fileNumber", "%" + fileNumber + "%"));
        }
        if (status != null && !status.equals("")) {
            criteria.add(Restrictions.eq("status", status));
        }
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }
}
