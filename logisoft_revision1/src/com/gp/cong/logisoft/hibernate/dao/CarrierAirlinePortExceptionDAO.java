package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;

public class CarrierAirlinePortExceptionDAO extends BaseHibernateDAO {

    public Iterator getAllPortCodesForDisplay()throws Exception {
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select id,portcode from CarrierAirlinePortExeption order by id").list().iterator();
        return results;
    }
}
