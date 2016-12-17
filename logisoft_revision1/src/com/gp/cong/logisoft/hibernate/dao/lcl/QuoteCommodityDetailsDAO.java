/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author lakshh
 */
public class QuoteCommodityDetailsDAO extends BaseHibernateDAO<LclQuotePieceDetail> {

    public QuoteCommodityDetailsDAO() {
        super(LclQuotePieceDetail.class);
    }

    public List findDetailProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(LclQuotePieceDetail.class);
        criteria.add(Restrictions.eq(propertyName, value));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
