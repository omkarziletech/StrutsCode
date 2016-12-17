package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.Printer;

public class PrinterDAO extends BaseHibernateDAO {

    public Iterator getAllPrintersForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select printerId,printerType from Printer").list().iterator();
        return results;
    }

    public Iterator getAllPrintersName(String printType, StringBuffer printerIds) throws Exception {
        String queryString = "select printerId,printerName from Printer where printerType=?0 and printerId not in";
        queryString = queryString + "(" + printerIds.toString() + ")";

        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", printType);
        return queryObject.list().iterator();
    }

    public Printer findById(Integer printerType) throws Exception {
        String queryString = " from Printer where printerId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", printerType);
        return (Printer) queryObject.setMaxResults(1).uniqueResult();
    }

    public List findAllPrinters() throws Exception {
        String queryString = "from Printer";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }
}
