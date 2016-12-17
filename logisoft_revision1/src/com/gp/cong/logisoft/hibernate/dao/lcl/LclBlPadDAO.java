/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPad;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Owner
 */
public class LclBlPadDAO extends BaseHibernateDAO<LclBlPad> {

    public LclBlPadDAO() {
        super(LclBlPad.class);
    }

    public LclBlPad getLclBookingPadByFileNumber(Long fileId) throws Exception {
            Criteria criteria = getSession().createCriteria(LclBlPad.class, "lclBlPad");
            criteria.createAlias("lclBookingPad.lclFileNumber", "lclFileNumber");
            if (!CommonUtils.isEmpty(fileId)) {
                criteria.add(Restrictions.eq("lclFileNumber.id", new Long(fileId)));
            }
            return (LclBlPad) criteria.uniqueResult();
    }

    public void deleteBlPadByFileNumber(Long fileNumberId) throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBlPad where lclFileNumber.id = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }
}
