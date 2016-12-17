package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AuditLogRecordDAO extends BaseHibernateDAO {
    //private Session session;

    public static final String STR_USER_NAME = "loginName";
    public static final String STR_PASSWORD = "password";

    public void save(AuditLogRecord transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public List findAllFclRecc(String id) throws Exception {
        String queryString = "from AuditLogRecordFcl where entityId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", id);
        return queryObject.list();
    }

    public void update(AuditLogRecord persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
        getSession().flush();
    }

    public void update(User persistanceInstance, String userName) throws Exception {
        getSession().update(persistanceInstance);
    }

    public void delete(User persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public AuditLogRecord findByIdAllMethods(Integer id, AuditLogRecord auditLogRecord) throws Exception {
        AuditLogRecord instance = (AuditLogRecord) getSession().get(auditLogRecord.getClass().getName(), id);
        return instance;
    }

    public AuditLogRecordUser findById(String userid) throws Exception {
        AuditLogRecordUser instance = (AuditLogRecordUser) getSession().get("com.gp.cong.logisoft.domain.AuditLogRecordUser", userid);
        return instance;
    }

    public AuditLogRelayOrigin findById1(Integer id) throws Exception {
        AuditLogRelayOrigin instance = (AuditLogRelayOrigin) getSession().get("com.gp.cong.logisoft.domain.AuditLogRelayOrigin", id);
        return instance;
    }

    public List findAllUsers(String queryString, String id) throws Exception {
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", id);
        return queryObject.list();
    }

    public List findAllRelayOrigin(String id) throws Exception {
        String queryString = "from AuditLogRelayOrigin where entityId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", id);
        return queryObject.list();
    }

    public List findAllVoids(AuditLogRecord auditLogRecord, String entityId) throws Exception {
        String queryString = "from " + auditLogRecord.getClass().getName() + " where entityId=?0 and (noteType='Auto' or voided = 'Yes') order by id desc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", entityId);
        return queryObject.list();
    }

    public List findForManagement(String userName, Date userCreatedDate, String firstName, String lastName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(User.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (userName != null && !userName.equals("")) {
            criteria.add(Restrictions.like("loginName", userName + "%"));
        }
        if (firstName != null && !firstName.equals("")) {
            criteria.add(Restrictions.like("firstName", firstName + "%"));
        }
        if (lastName != null && !lastName.equals("")) {
            criteria.add(Restrictions.like("lastName", lastName + "%"));
        }
        if (userCreatedDate != null && !userCreatedDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strSODate = dateFormat.format(userCreatedDate);
            Date soStartDate = (Date) dateFormat.parse(strSODate);
            Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
            criteria.add(Restrictions.between("userCreatedDate", soStartDate, soEndDate));
        }
        criteria.addOrder(Order.asc("loginName"));
        return criteria.list();

    }

    public List findLoginName(String loginName) throws Exception {
        List list = new ArrayList();
        String queryString = "from User where loginName=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", loginName);
        list = queryObject.list();
        return list;
    }

    public User findByUserName(String userName, String password) throws Exception {
        String queryString = "from User where upper(" + this.STR_USER_NAME + ")= ?0 and " + this.STR_PASSWORD + "= ?1 AND status='Active'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", userName);
        queryObject.setParameter("1", password);
        return (User) queryObject.setMaxResults(1).uniqueResult();
    }

    public List findSpecialNotes(String entityId) throws Exception {
        String queryString = "from AuditLogRecordCustomer where entityId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", entityId);
        return queryObject.list();
    }
}
