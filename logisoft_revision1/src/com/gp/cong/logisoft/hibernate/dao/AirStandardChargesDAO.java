package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.AirStandardCharges;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;

public class AirStandardChargesDAO extends BaseHibernateDAO {

    public void save(AirStandardCharges transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public Iterator getAllAirStandardChargesForDisplay(RefTerminalTemp org, PortsTemp des) throws Exception {
        String results = null;
        Iterator it = null;
        results = "select a from AirStandardCharges a,StandardCharges b where a.standard='Y'and a.standardId=b.id and b.orgTerminal=?0 and b.destPort=?1";
        Query queryObject = getCurrentSession().createQuery(results);
        queryObject.setParameter("0", org);
        queryObject.setParameter("1", des);
        it = queryObject.list().iterator();
        return it;

    }

    public Iterator getAllRetailStandardChargesForDisplay(RefTerminalTemp org, PortsTemp des) throws Exception {
        String results = null;
        Iterator it = null;
        results = "select a from RetailStandardCharges1 a,RetailStandardCharges b where a.standard='Y'and a.standardId=b.id and b.orgTerminal=?0 and b.destPort=?1";
        Query queryObject = getCurrentSession().createQuery(results);
        queryObject.setParameter("0", org);
        queryObject.setParameter("1", des);
        it = queryObject.list().iterator();
        return it;

    }

    public Iterator getAllRetailStandardCharges1ForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "from RetailStandardCharges1 where standard='Y'").list().iterator();
        return results;

    }
}
