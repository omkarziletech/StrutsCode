package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.FclContainertypeCharges;

public class FclContainertypeChargesDAO extends BaseHibernateDAO {

    public void save(FclContainertypeCharges transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }
}
