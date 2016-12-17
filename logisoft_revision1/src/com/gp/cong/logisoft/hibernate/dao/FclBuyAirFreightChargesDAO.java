package com.gp.cong.logisoft.hibernate.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.gp.cong.logisoft.domain.FclBuyAirFreightCharges;

public class FclBuyAirFreightChargesDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(FclBuyAirFreightChargesDAO.class);

    public void save(FclBuyAirFreightCharges transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }
}
