/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author lakshh
 */
public class CommodityDetailsDAO extends BaseHibernateDAO<LclBookingPieceDetail> {

    public CommodityDetailsDAO() {
        super(LclBookingPieceDetail.class);
    }

    public List findDetailProperty(String propertyName, Object value)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(LclBookingPieceDetail.class);
            criteria.add(Restrictions.eq(propertyName, value));
            criteria.addOrder(Order.asc("id"));
            return criteria.list();
    }
}
