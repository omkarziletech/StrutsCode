package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.Shploc;

public class ShplocDAO extends BaseHibernateDAO {
    // property constants

    public void save(Shploc shploc) throws Exception {
        getSession().save(shploc);
    }
}
