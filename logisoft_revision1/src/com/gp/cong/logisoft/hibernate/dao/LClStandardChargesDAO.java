package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.LCLColoadStandardCharges;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;

public class LClStandardChargesDAO extends BaseHibernateDAO {

    public void save(LCLColoadStandardCharges transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public Iterator getAllcoStandardChargesForDisplay(RefTerminalTemp org, PortsTemp des)throws Exception {
        String results = null;
        Iterator it = null;
            results = "select a from LCLColoadStandardCharges a,LCLColoadMaster b where a.standard='Y'and a.lclCoLoadId=b.id and b.originTerminal=?0 and b.destinationPort=?1";
            Query queryObject = getCurrentSession().createQuery(results);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            it = queryObject.list().iterator();
        return it;

    }
}
