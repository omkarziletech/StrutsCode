package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.BookingInbondDetails;
import java.util.List;
import org.hibernate.Query;


public class BookingInbondDetailsDAO extends BaseHibernateDAO {

    public void save(BookingInbondDetails transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }
    public BookingInbondDetails findById(Integer id)throws Exception {
            BookingInbondDetails instance = (BookingInbondDetails) getSession().get(
                    "com.gp.cong.logisoft.domain.BookingInbondDetails", id);
            return instance;
    }
    public void delete(BookingInbondDetails persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }
    public List findAll()throws Exception {
            String queryString = "from BookingInbondDetails";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
    }
    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from BookingInbondDetails as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
        }
    
    public List<Long> getCFCLInbondDetails(String bookingId) {
        String queryString = "SELECT id FROM `booking_inbond_details` WHERE bol_id=:bookingId AND cfcl_flag IS TRUE";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("bookingId", bookingId);
        return queryObject.list();
    }

    public void deleteCFCLInbondDetails(List<Long> listId) {
        String queryString = "delete from booking_inbond_details where id IN (:listId)";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameterList("listId", listId);
        queryObject.executeUpdate();
    }
}
