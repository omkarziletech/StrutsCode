package com.logiware.common.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.common.domain.OnlineUser;
import com.logiware.common.form.OnlineUserForm;
import com.logiware.common.model.ResultModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class OnlineUserDAO extends BaseHibernateDAO {
    private static final Logger log = Logger.getLogger(OnlineUserDAO.class);

    public void save(OnlineUser transientInstance) {
        getCurrentSession().save(transientInstance);
    }

    public void update(OnlineUser persistentInstance) {
        getCurrentSession().update(persistentInstance);
    }

    public void delete(OnlineUser persistentInstance) {
        getCurrentSession().delete(persistentInstance);
    }

    public void delete(Integer userId) {
        String query = "delete from online_user where user_id = " + userId;
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
    }

    public OnlineUser findById(Serializable id) {
        return (OnlineUser) getCurrentSession().get(OnlineUser.class, id);
    }

    public OnlineUser findByUserId(Integer userId) {
        Criteria criteria = getCurrentSession().createCriteria(OnlineUser.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.setMaxResults(1);
        return (OnlineUser) criteria.uniqueResult();
    }

    public void killAllUsers() {
        Transaction tx = null;
        try {
            tx = getCurrentSession().getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            String queryString = "delete from online_user";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            log.info("Killing all users failed on " + new Date(), e);
            if (null != tx && !tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public boolean isUserAlreadyLoggedOn(Integer userId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  online_user ");
        queryBuilder.append("where user_id = ").append(userId);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean isUserAlreadyLoggedOn(String sessionId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  online_user ");
        queryBuilder.append("where session_id = '").append(sessionId).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean isUserAlreadyLoggedOn(Integer userId, String sessionId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  online_user ");
        queryBuilder.append("where");
        if (CommonUtils.isNotEmpty(userId)) {
            queryBuilder.append("  user_id = ").append(userId);
        }
        if (CommonUtils.isNotEmpty(sessionId)) {
            if (CommonUtils.isNotEmpty(userId)) {
                queryBuilder.append(" or ");
            }
            queryBuilder.append("  session_id = '").append(sessionId).append("'");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    private Integer getTotalRows() {
        SQLQuery query = getCurrentSession().createSQLQuery("select count(*) as result from online_user");
        query.addScalar("result", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }

    private String buildQuery(String sortBy, String orderBy) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  upper(u.login_name) as loginName,");
        queryBuilder.append("  upper(u.first_name) as firstName,");
        queryBuilder.append("  upper(u.last_name) as lastName,");
        queryBuilder.append("  concat(t.trmnum, ' - ', t.terminal_location) as terminal,");
        queryBuilder.append("  u.telephone as phone,");
        queryBuilder.append("  u.email as email,");
        queryBuilder.append("  u.address1 as address,");
        queryBuilder.append("  upper(c.un_loc_name) as city,");
        queryBuilder.append("  u.state as state,");
        queryBuilder.append("  g.codedesc as country,");
        queryBuilder.append("  o.ip_address as ipAddress,");
        queryBuilder.append("  date_format(o.logged_on, '%b %d, %Y %r') as loggedOn,");
        queryBuilder.append("  o.id as id,");
        queryBuilder.append("  u.user_id as userId ");
        queryBuilder.append("from");
        queryBuilder.append("  online_user o ");
        queryBuilder.append("  join user_details u ");
        queryBuilder.append("    on (o.user_id = u.user_id) ");
        queryBuilder.append("  left join terminal t ");
        queryBuilder.append("    on (u.terminal_id = t.trmnum) ");
        queryBuilder.append("  left join un_location c ");
        queryBuilder.append("    on (u.city = c.id) ");
        queryBuilder.append("  left join genericcode_dup g ");
        queryBuilder.append("    on (u.country = g.id) ");
        queryBuilder.append("order by ").append(sortBy).append(" ").append(orderBy);
        return queryBuilder.toString();
    }

    public List<ResultModel> getOnlineUsers(String sortBy, String orderBy, Integer start, Integer limit) {
        String queryString = buildQuery(sortBy, orderBy);
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        if (null != start) {
            query.setFirstResult(start);
        }
        if (null != limit) {
            query.setMaxResults(limit);
        }
        query.addScalar("loginName", StringType.INSTANCE);
        query.addScalar("firstName", StringType.INSTANCE);
        query.addScalar("lastName", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.addScalar("phone", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("city", StringType.INSTANCE);
        query.addScalar("state", StringType.INSTANCE);
        query.addScalar("country", StringType.INSTANCE);
        query.addScalar("ipAddress", StringType.INSTANCE);
        query.addScalar("loggedOn", StringType.INSTANCE);
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("userId", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public void search(OnlineUserForm onlineUserForm) {
        int totalRows = getTotalRows();
        if (totalRows > 0) {
            int limit = onlineUserForm.getLimit();
            int start = limit * (onlineUserForm.getSelectedPage() - 1);
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            List<ResultModel> onlineUsers = getOnlineUsers(onlineUserForm.getSortBy(), onlineUserForm.getOrderBy(), start, limit);
            onlineUserForm.setTotalPages(totalPages);
            onlineUserForm.setTotalRows(totalRows);
            onlineUserForm.setSelectedRows(onlineUsers.size());
            onlineUserForm.setOnlineUsers(onlineUsers);
        }
    }
}
