package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.Terminal;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

public class TerminalDAO extends BaseHibernateDAO {


    public Iterator getAllTerminalsForDisplay()throws Exception {
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select terminal.ecitrm,terminal.trmloc from Terminal terminal where terminal.ecitrm !='' order by locnam").list().iterator();
        return results;
    }

    public Terminal findById(String ecitrm) throws Exception {
            String queryString = " from Terminal where ecitrm=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", ecitrm);
            return (Terminal) queryObject.setMaxResults(1).uniqueResult();
    }

    public List<RefTerminalTemp> getTerminals(String terminal) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.add(Restrictions.or(Restrictions.like("trmnum", terminal + "%"), Restrictions.like("terminalLocation", terminal + "%")));
        criteria.addOrder(Order.asc("trmnum"));
        return criteria.list();
    }

    public String defaultTerminalEmail(String terminalId) throws Exception {
        String queryString = "select importsContactEmail from RefTerminal where trmnum =" + terminalId+"";
        Query queryObject = getCurrentSession().createQuery(queryString);
        Object terminalEmail = queryObject.setMaxResults(1).uniqueResult();
        return null!=terminalEmail?terminalEmail.toString():"";
    }
    public RefTerminal findByTerminalNo(String terminalNo) throws Exception {
        Query query = getCurrentSession().createQuery("FROM RefTerminal WHERE trmnum = :terminalNumber").setString("terminalNumber", terminalNo);
        return (RefTerminal) query.uniqueResult();
    }
    
    public String getTerminal(String unlocationCode) {
        String queryString = "select trmnum from terminal where unlocationcode1 = :unlocationcode1 order by field(actyon, 'Y') desc";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("unlocationcode1", unlocationCode);
        query.addScalar("trmnum", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String)query.uniqueResult();
    }
     
     public String getIntraBookerId(String terminalNo) throws Exception {
        String queryString = "select intra_booker_id from terminal where trmnum = :terminalNo";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("terminalNo", terminalNo);
        Object intraBookerId = query.setMaxResults(1).uniqueResult();
        return null!=intraBookerId ? intraBookerId.toString() : "";
    }
     public String getImportsDoorDeliveryEmail(String terminalNo) throws Exception {
        String queryString = "select imports_door_delivery_email from terminal where trmnum = :terminalNo";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("terminalNo", terminalNo);
        Object intraBookerId = query.setMaxResults(1).uniqueResult();
        return null!=intraBookerId ? intraBookerId.toString() : "";
    }
}
