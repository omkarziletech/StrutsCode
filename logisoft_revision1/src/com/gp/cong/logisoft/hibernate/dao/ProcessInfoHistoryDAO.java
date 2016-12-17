package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.ProcessInfoHistory;

public class ProcessInfoHistoryDAO extends BaseHibernateDAO {

    public void save(ProcessInfoHistory transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }
}
