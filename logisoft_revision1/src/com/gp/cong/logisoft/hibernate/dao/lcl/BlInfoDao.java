package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.BlInfo;
import org.hibernate.Query;

/**
 *
 * @author Rajesh
 */
public class BlInfoDao extends BaseHibernateDAO<BlInfo> {

    public BlInfoDao() {
        super(BlInfo.class);
    }

    public BlInfo findById(Long id) throws Exception {
        Query query = getCurrentSession().getNamedQuery("BlInfo.findById").setLong("id", id);
        return (BlInfo) query.uniqueResult();
    }
}
