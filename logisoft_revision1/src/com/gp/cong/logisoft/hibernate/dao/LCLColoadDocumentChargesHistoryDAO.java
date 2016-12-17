package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.LCLColoadDocumentChargesHistory;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;

public class LCLColoadDocumentChargesHistoryDAO extends BaseHibernateDAO {

    public void save(LCLColoadDocumentChargesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory(RefTerminalTemp org, PortsTemp des)throws Exception{
            String queryString = " from LCLColoadDocumentChargesHistory";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }
}
