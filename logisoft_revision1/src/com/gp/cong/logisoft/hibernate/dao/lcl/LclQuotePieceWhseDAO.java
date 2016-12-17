/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceWhse;
import java.util.Collections;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Logiware
 */
public class LclQuotePieceWhseDAO extends BaseHibernateDAO<LclQuotePieceWhse> {

    public LclQuotePieceWhseDAO() {
        super(LclQuotePieceWhse.class);
    }

    public List<LclQuotePieceWhse> findByFileAndCommodityList(Long bookingPieceId) throws Exception {
        String queryString = "from LclQuotePieceWhse where lclQuotePiece='" + bookingPieceId + "' order by id asc";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }
    
    public List<LclQuotePieceWhse> findByCommodityList(Long bookingPieceId) throws Exception {
        String queryString = "from LclQuotePieceWhse where lclQuotePiece='" + bookingPieceId + "' order by id desc";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public LclQuotePieceWhse findByFileAndCommodity(Long bookingPieceId) throws Exception {
        String queryString = "from LclQuotePieceWhse where lclQuotePiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        return (LclQuotePieceWhse)query.setMaxResults(1).uniqueResult();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(LclQuotePieceWhse.class, "whse");
        criteria.add(Restrictions.eq(propertyName, value));
        criteria.addOrder(Order.asc("whse.enteredDatetime"));
        return criteria.list();
    }
}
