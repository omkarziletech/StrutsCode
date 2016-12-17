package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.CarrierAirline;

public class CarrierAirlineDAO extends BaseHibernateDAO {

    public void save(CarrierAirline transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }
}
