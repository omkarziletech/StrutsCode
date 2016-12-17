package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.UserPrinterAssociation;
import java.util.HashMap;
import java.util.Map;

public class UserPrinterAssociationDAO extends BaseHibernateDAO {

    public void save(UserPrinterAssociation transientInstance) {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void delete(UserPrinterAssociation persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public UserPrinterAssociation findById(Integer printerId) throws Exception {
        String queryString = " from UserPrinterAssociation where printerId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", printerId);
        return (UserPrinterAssociation) queryObject.setMaxResults(1).uniqueResult();
    }

    public List findByUserId(Integer userId) throws Exception {
        String queryString = " from UserPrinterAssociation where userId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", userId);
        return queryObject.list();
    }

    public List getAllPrinterId() throws Exception {
        String queryString = "select printerId from UserPrinterAssociation";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public UserPrinterAssociation findBy(Long documentId, Integer userId) {
        String queryString = " from UserPrinterAssociation where userId=:userId and documentId.id=:documentId";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("documentId", documentId);
        queryObject.setParameter("userId", userId);
        return (UserPrinterAssociation) queryObject.setMaxResults(1).uniqueResult();
    }

    public Map<String, String> getPrinterListHbl(Integer userId) throws Exception {
        Map<String, String> printerMap = new HashMap<String, String>();
        String queryString = "SELECT pg.`document_name` AS documentName,ups.`printer_name` AS printer FROM user_printer_association  ups ";
        queryString += " LEFT JOIN print_config  pg ON ups.`document_id`= pg.`id` WHERE ups.user_id=:userId";
        queryString += " AND pg.screen_name='LCLBL'";
        Query query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        List<Object[]> result = query.list();
        if (!result.isEmpty()) {
            for (Object[] value : result) {
                printerMap.put(value[0].toString(), value[1].toString());
            }
        }
        return printerMap;
    }
}
