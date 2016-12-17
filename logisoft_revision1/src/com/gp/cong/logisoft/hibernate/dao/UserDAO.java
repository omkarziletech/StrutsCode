package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.beans.SearchUserBean;
import com.gp.cong.logisoft.domain.*;
import com.gp.cong.logisoft.model.UserModel;
import com.logiware.accounting.model.TradingPartnerModel;
import com.logiware.common.dao.ChainedFieldsTransformer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

public class UserDAO extends BaseHibernateDAO {
    //private Session session;

    public static final String STR_USER_NAME = "loginName";
    public static final String STR_PASSWORD = "password";

    public void save(User transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public void saveAgentInformation(UserAgentInformation transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(User persistanceInstance, String userName) throws Exception {
        getSession().update(persistanceInstance);
        getSession().flush();
        getSession().clear();
    }

    public void delete(User persistanceInstance, String userName1) throws Exception {
    }

    public void delete(UserAgentInformation persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public User findById(Integer userid) throws Exception {
        User instance = (User) getSession().get("com.gp.cong.logisoft.domain.User", userid);
        return instance;
    }

    public UserAgentInformation findAgentUserId(Integer agentInformation) throws Exception {
        UserAgentInformation instance = (UserAgentInformation) getSession().get("com.gp.cong.logisoft.domain.UserAgentInformation", agentInformation);
        return instance;
    }

    public User findUserName(String loginName) throws Exception {
        return (User) getCurrentSession().createCriteria(User.class).add(Restrictions.eq("loginName", loginName)).setMaxResults(1).uniqueResult();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from User as model where model." + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByUserAgentProperty(String propertyName, Object value) throws Exception {
        String queryString = "from UserAgentInformation  where " + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByUserAgentProperties(String propertyName, Object value, String propertyName1, Object value1) throws Exception {
        String queryString = "from UserAgentInformation  where " + propertyName + " like ?0 and " + propertyName1 + " like ?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        queryObject.setParameter("1", value1);
        return queryObject.list();
    }

    public void updateProperty(String propertyName, Object propertyValue, Object updateValue) throws Exception {
        String queryString = "update User set " + propertyName + " = ?0 where " + propertyName + " = ?1";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("0", updateValue);
        query.setParameter("1", propertyValue);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public List findAllUsers() throws Exception {
        String queryString = " from User order by loginName";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public Iterator getUsersForLineManagers() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select userId,loginName from User").list().iterator();
        return results;
    }

    public Iterator getManagerDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select userId,firstName,lastName  from User where firstName!=null and lastName!=null order by firstName ").list().iterator();
        return results;
    }

    public List<UserModel> getUserList(SearchUserBean suBean) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT  ");
        queryStr.append(" ud.user_id AS userId,ud.login_name AS loginName,  ");
        queryStr.append(" ud.first_name AS firstName,ud.last_name AS lastName,  ");
        queryStr.append(" ud.telephone AS telephone,ud.email AS email,  ");
        queryStr.append("  (SELECT r.role_desc FROM role_master r WHERE ud.role=r.role_id) AS roleName, ");
        queryStr.append("  ud.email AS email,ud.status AS STATUS, ");
        queryStr.append(" DATE_FORMAT(ud.last_login_date,'%d-%b-%Y %T %p') AS lastLogin,  ");
        queryStr.append("  DATE_FORMAT(ud.usercreateddate,'%d-%b-%Y %T %p') AS createdBy ");
        queryStr.append(" FROM  ");
        queryStr.append("  user_details ud  ");
        queryStr.append(appendWhereCondition(suBean));
        if (CommonUtils.isNotEmpty(suBean.getSortBy())) {
            if ("up".equals(suBean.getSortBy())) {
                queryStr.append(" order by ud.").append(suBean.getColumnName()).append(" asc");
            } else {
                queryStr.append(" order by ud.").append(suBean.getColumnName()).append(" desc");
            }
        } else {
            queryStr.append("   order by ud.login_name asc");
        }

        queryStr.append(" LIMIT ").append(suBean.getLimit());
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setResultTransformer(Transformers.aliasToBean(UserModel.class));
        query.addScalar("userId", StringType.INSTANCE);
        query.addScalar("loginName", StringType.INSTANCE);
        query.addScalar("firstName", StringType.INSTANCE);
        query.addScalar("lastName", StringType.INSTANCE);
        query.addScalar("telephone", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("roleName", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("lastLogin", StringType.INSTANCE);
        query.addScalar("createdBy", StringType.INSTANCE);
        return query.list();
    }

    public String appendWhereCondition(SearchUserBean suBean) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" where ud.login_name is not null");
        if (CommonUtils.isNotEmpty(suBean.getLoginName())) {
            queryStr.append(" and ud.login_name like '").append(suBean.getLoginName()).append("%'");
        }
        if (CommonUtils.isNotEmpty(suBean.getFirstName())) {
            queryStr.append(" and ud.first_name like '").append(suBean.getFirstName()).append("%'");
        }
        if (CommonUtils.isNotEmpty(suBean.getLastName())) {
            queryStr.append(" and ud.last_name like '").append(suBean.getLastName()).append("%'");
        }
        if (CommonUtils.isNotEmpty(suBean.getUserStatus())) {
            queryStr.append(" and ud.status ='").append(suBean.getUserStatus()).append("'");
        }
        if (CommonUtils.isNotEmpty(suBean.getRoleName())) {
            queryStr.append(" and ud.role ='").append(suBean.getRoleName()).append("'");
        }
        if (CommonUtils.isNotEmpty(suBean.getTxtCal())) {

            queryStr.append(" and DATE_FORMAT(ud.usercreateddate,'%m/%d/%Y') ='").append(suBean.getTxtCal()).append("'");
        }
        return queryStr.toString();
    }

    public List findForManagement(String userName, Date userCreatedDate, String firstName, String lastName, String roleName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(User.class, "user");
        criteria.createAlias("user.role", "role");
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (userName != null && !userName.equals("")) {
            criteria.add(Restrictions.like("loginName", userName + "%"));
            criteria.addOrder(Order.asc("loginName"));
        }
        if (firstName != null && !firstName.equals("")) {
            criteria.add(Restrictions.like("firstName", firstName + "%"));
            criteria.addOrder(Order.asc("firstName"));
        }
        if (lastName != null && !lastName.equals("")) {
            criteria.add(Restrictions.like("lastName", lastName + "%"));
            criteria.addOrder(Order.asc("lastName"));
        }
        if (roleName != null && !roleName.equals("")) {
            criteria.add(Restrictions.like("role.roleDesc", roleName + "%"));
        }
        if (userCreatedDate != null && !userCreatedDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strSODate = dateFormat.format(userCreatedDate);
            //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
            Date soStartDate = (Date) dateFormat.parse(strSODate);
            Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
            criteria.add(Restrictions.between("userCreatedDate", soStartDate, soEndDate));
            criteria.addOrder(Order.asc("userCreatedDate"));
        }
        return criteria.list();
    }

    public List findLoginName1(String loginName, String lastName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(User.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (loginName != null && !loginName.equals("")) {
            criteria.add(Restrictions.like("firstName", loginName + "%"));
            criteria.addOrder(Order.asc("firstName"));
        }
        if (lastName != null && !lastName.equals("")) {
            criteria.add(Restrictions.like("lastName", lastName + "%"));
            criteria.addOrder(Order.asc("lastName"));
        }
        return criteria.list();
    }

    public List findLoginName2(String loginName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(User.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (loginName != null && !loginName.equals("")) {
            criteria.add(Restrictions.like("loginName", loginName + "%"));
            criteria.addOrder(Order.asc("loginName"));
        }
        return criteria.list();
    }

    public List findForManagementaction(String userName, Date userCreatedDate, String firstName, String lastName, String roleName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(User.class, "user");
        criteria.createAlias("user.role", "role");
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (userName != null && !userName.equals("")) {
            criteria.add(Restrictions.ge("loginName", userName));
            criteria.addOrder(Order.asc("loginName"));
        }
        if (firstName != null && !firstName.equals("")) {
            criteria.add(Restrictions.ge("firstName", firstName));
            criteria.addOrder(Order.asc("firstName"));
        }
        if (lastName != null && !lastName.equals("")) {
            criteria.add(Restrictions.ge("lastName", lastName));
            criteria.addOrder(Order.asc("lastName"));
        }
        if (roleName != null && !roleName.equals("")) {
            criteria.add(Restrictions.ge("role.roleDesc", roleName));
            criteria.addOrder(Order.asc("role.roleDesc"));
        }
        if (userCreatedDate != null && !userCreatedDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strSODate = dateFormat.format(userCreatedDate);
            //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
            Date soStartDate = (Date) dateFormat.parse(strSODate);
            criteria.add(Restrictions.ge("userCreatedDate", soStartDate));
            criteria.addOrder(Order.asc("userCreatedDate"));
        }
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

    public List findLoginNameForBatchProcess(String loginName) throws Exception {
        List list = new ArrayList();
        String queryString = "from User where loginName=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", loginName);
        list = queryObject.list();
        return list;
    }

    public List findTerminalName(RefTerminal terminalNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from User where terminal=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", terminalNo);
        list = queryObject.list();
        return list;
    }

    public User getUser(String loginName, String password) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("loginName", loginName));
        criteria.add(Restrictions.eq("password", password));
        criteria.add(Restrictions.eq("status", "Active"));
        return (User) criteria.uniqueResult();

    }

    public User findByUserName(String userName, String password) throws Exception {
        String queryString = "from User where upper(" + UserDAO.STR_USER_NAME + ")= ?0 and " + UserDAO.STR_PASSWORD + "= ?1 AND status='Active'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", userName);
        queryObject.setParameter("1", password);
        return (User) queryObject.setMaxResults(1).uniqueResult();

    }

    public List<User> findByUserName(String userName) throws Exception {
        List<User> userList = null;
        String queryString = "from User where upper(" + STR_USER_NAME + ") like ?0";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("0", userName + "%");
        userList = query.list();
        return userList;

    }

    public String findByPassword(String userName, String password) throws Exception {
        String queryString = "select count(userId) from User where upper(" + this.STR_USER_NAME + ")= ?0 and " + this.STR_PASSWORD + "= ?1 AND status='Active'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", userName);
        queryObject.setParameter("1", password);
        String result = (String) queryObject.uniqueResult().toString();
        return result.equals("0") ? "invalid" : "valid";
    }

    public Iterator getAllContactsForDisplay(StringBuffer userIds) throws Exception {
        String queryString = "select userId,firstName,lastName from User where userId not in  ";
        queryString = queryString + "(" + userIds.toString() + ") order by firstName";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list().iterator();
    }

    public List<User> findUserByNameAndRole(String loginName, String role) throws Exception {
        List<User> userList = null;
        userList = getCurrentSession().createCriteria(User.class, "user").createAlias("user.role", "role").add(Restrictions.eq("role.roleDesc", role)).add(Restrictions.like("user.loginName", loginName + "%")).list();
        return userList;

    }

    public List<User> getUsersForApConfig(String loginName) throws Exception {
        List<User> users = null;
        Criteria criteria = getCurrentSession().createCriteria(User.class, "user");
        criteria.createAlias("user.role", "role");
        criteria.add(Restrictions.or(Restrictions.eq("role.roleDesc", CommonConstants.ROLE_NAME_APSPECIALIST),
                Restrictions.eq("role.roleDesc", CommonConstants.ROLE_NAME_INTERNATIONAL_COLLECTIONS)));
        criteria.add(Restrictions.like("user.loginName", loginName + "%"));
        users = criteria.list();
        return users;
    }

    public List<User> findUserForApSpecialist(String loginName, String role) throws Exception {
        List<User> userList = null;
        userList = getCurrentSession().createCriteria(User.class, "user").createAlias("user.role", "role").add(Restrictions.eq("role.roleDesc", role)).add(Restrictions.like("user.loginName", loginName)).list();
        return userList;
    }

    public List<User> findUser(String firstName, String lastName, String email) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select user from User user where user.role is not null");
        if (CommonUtils.isNotEmpty(firstName)) {
            queryBuilder.append(" and user.firstName ='").append(firstName).append("'");
        }
        if (CommonUtils.isNotEmpty(lastName)) {
            queryBuilder.append(" and user.lastName ='").append(lastName).append("'");
        }
        if (CommonUtils.isNotEmpty(email)) {
            queryBuilder.append(" and user.email ='").append(email).append("'");
        }
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public Boolean checkRangeForCollector(Integer userId, String startRange, String endRange) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select count(*) as count from CollectorTradingPartner");
        queryBuilder.append(" where (startRange between '").append(startRange).append("' and '").append(endRange).append("'");
        queryBuilder.append(" or endRange between '").append(startRange).append("' and '").append(endRange).append("'");
        queryBuilder.append(" or '").append(startRange).append("' between startRange and endRange");
        queryBuilder.append(" or '").append(endRange).append("' between startRange and endRange)");
        queryBuilder.append(" and user.userId!=").append(userId);
        String count = (String) getCurrentSession().createQuery(queryBuilder.toString()).uniqueResult().toString();
        return count.equals("0");
    }

    public Boolean isCollectorAlreadyAppliedToConsigneeOnlyAccounts(Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select count(*) as count from collector_trading_partner_assn");
        queryBuilder.append(" where apply_to_consignee_only_accounts=1");
        queryBuilder.append(" and user_id!=").append(userId);
        String count = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult().toString();
        return !count.equals("0");
    }

    public Boolean checkSubTypesForApSpecialist(Integer userId, String[] subTypes) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(APSpecialistTradingPartner.class);
        criteria.add(Restrictions.in("subType", subTypes));
        criteria.add(Restrictions.ne("user.userId", userId));
        criteria.setProjection(Projections.rowCount());
        String count = (String) criteria.uniqueResult().toString();
        return count.equals("0");
    }

    public String getAllAssignedSubTypes(Integer userId) throws Exception {
        String queryString = "select cast(group_concat(sub_type) as char) from apspecialist_trading_partner_assn where sub_type is not null and user_id != " + userId;
        String assignedSubTypes = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != assignedSubTypes ? assignedSubTypes : "";
    }

    public void saveOrUpdateCollectorTradingPartner(CollectorTradingPartner collectorTradingPartner) throws Exception {
        getSession().saveOrUpdate(collectorTradingPartner);
        getSession().flush();
    }

    public void saveOrUpdateAPSpecialistTradingPartner(APSpecialistTradingPartner collectorTradingPartner) throws Exception {
        getSession().saveOrUpdate(collectorTradingPartner);
        getSession().flush();
    }

    public void unAssignAPSpecialistFromTradingPartner(Integer userId) throws Exception {
        User user = this.findById(userId);
        String subTypes = "";
        for (APSpecialistTradingPartner apSpecialistTradingPartner : user.getApSpecialistTradingPartners()) {
            subTypes += "'" + apSpecialistTradingPartner.getSubType() + "',";
        }
        if (CommonUtils.isNotEmpty(subTypes)) {
            StringBuilder queryBuilder = new StringBuilder("update trading_partner tp,vendor_info vendor set vendor.ap_specialist = null");
            queryBuilder.append(" ,vendor.ap_specialist_updated_by=null");
            queryBuilder.append(" where tp.acct_no = vendor.cust_accno and tp.sub_type in (");
            queryBuilder.append(StringUtils.removeEnd(subTypes, ",")).append(") and vendor.ap_specialist = ").append(userId);
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        }
        user.getApSpecialistTradingPartners().clear();
    }

    public void unAssignCollectorFromTradingPartner(Integer userId) throws Exception {
        User user = findById(userId);
        String range = "";
        boolean applyToConsigneeOnlyAccounts = false;
        for (CollectorTradingPartner collectorTradingPartner : user.getCollectorTradingPartners()) {
            if (collectorTradingPartner.isApplyToConsigneeOnlyAccounts()) {
                applyToConsigneeOnlyAccounts = true;
            } else {
                range += collectorTradingPartner.getStartRange() + "-" + collectorTradingPartner.getEndRange();
            }
        }
        if (applyToConsigneeOnlyAccounts) {
            StringBuilder queryBuilder = new StringBuilder("update trading_partner tp,cust_accounting ca");
            queryBuilder.append(" set ca.ar_contact_code = null,ca.collector_updated_by=null");
            queryBuilder.append(" where tp.acct_no = ca.acct_no and tp.acct_type='C'");
            queryBuilder.append(" and ca.ar_contact_code = ").append(userId);
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        }
        if (CommonUtils.isNotEmpty(range)) {
            StringBuilder queryBuilder = new StringBuilder("update trading_partner tp,cust_accounting ca");
            queryBuilder.append(" set ca.ar_contact_code = null,ca.collector_updated_by=null");
            queryBuilder.append(" where tp.acct_no = ca.acct_no and tp.acct_name rlike '^[");
            queryBuilder.append(range).append("]' and ca.ar_contact_code = ").append(userId);
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        }
        user.getCollectorTradingPartners().clear();
    }

    public List<TradingPartnerModel> getTpApSpecialists() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  u.user_id as userId,");
        queryBuilder.append("  group_concat(concat(\"'\", a.sub_type, \"'\")) as subType ");
        queryBuilder.append("from");
        queryBuilder.append("  user_details u");
        queryBuilder.append("  join apspecialist_trading_partner_assn a");
        queryBuilder.append("    on (u.user_id = a.user_id) ");
        queryBuilder.append("where");
        queryBuilder.append("  a.sub_type <> '' ");
        queryBuilder.append("group by u.user_id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("userId", IntegerType.INSTANCE);
        query.addScalar("subType", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(TradingPartnerModel.class));
        return query.list();
    }

    public void assignAPSpecialistToTradingPartner() throws Exception {
        List<TradingPartnerModel> apSpecialists = getTpApSpecialists();
        for (TradingPartnerModel apSpecialist : apSpecialists) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("update");
            queryBuilder.append("   vendor_info ap");
            queryBuilder.append("   join trading_partner tp");
            queryBuilder.append("     on (");
            queryBuilder.append("       ap.cust_accno = tp.acct_no");
            queryBuilder.append("       and tp.sub_type in (").append(apSpecialist.getSubType()).append(")");
            queryBuilder.append("     )");
            queryBuilder.append("set");
            queryBuilder.append("   ap.ap_specialist = ").append(apSpecialist.getUserId()).append(",");
            queryBuilder.append("   ap.ap_specialist_updated_by = 'System' ");
            queryBuilder.append("where");
            queryBuilder.append("  (");
            queryBuilder.append("    ap.ap_specialist_updated_by is null");
            queryBuilder.append("    or ap.ap_specialist_updated_by = ''");
            queryBuilder.append("    or ap.ap_specialist_updated_by = 'System'");
            queryBuilder.append("  or (ap.ap_specialist IS NULL AND ap.ap_specialist_updated_by <> 'System' AND ap.ap_specialist_updated_by <> '') )");
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        }
    }

    public List<TradingPartnerModel> getTpCollectors() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  u.user_id as userId,");
        queryBuilder.append("  c.apply_to_consignee_only_accounts as  consigneeOnly,");
        queryBuilder.append("  group_concat(concat(c.start_range, '-', c.end_range) separator '') as nameRange ");
        queryBuilder.append("from");
        queryBuilder.append("  user_details u");
        queryBuilder.append("  join collector_trading_partner_assn c");
        queryBuilder.append("    on (u.user_id = c.user_id) ");
        queryBuilder.append("where");
        queryBuilder.append("  c.apply_to_consignee_only_accounts <> 0");
        queryBuilder.append("  or (");
        queryBuilder.append("    c.start_range <> ''");
        queryBuilder.append("    and c.end_range <> ''");
        queryBuilder.append("  ) ");
        queryBuilder.append("group by u.user_id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("userId", IntegerType.INSTANCE);
        query.addScalar("consigneeOnly", BooleanType.INSTANCE);
        query.addScalar("nameRange", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(TradingPartnerModel.class));
        return query.list();
    }

    public void assignCollectorToTradingPartner() throws Exception {
        List<TradingPartnerModel> collectors = getTpCollectors();
        for (TradingPartnerModel collector : collectors) {
            if (collector.isConsigneeOnly()) {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("update");
                queryBuilder.append("   cust_accounting ar");
                queryBuilder.append("   join trading_partner tp");
                queryBuilder.append("     on (");
                queryBuilder.append("       ar.acct_no = tp.acct_no");
                queryBuilder.append("       and tp.acct_type='C'");
                queryBuilder.append("     )");
                queryBuilder.append("set");
                queryBuilder.append("   ar.ar_contact_code = ").append(collector.getUserId()).append(",");
                queryBuilder.append("   ar.collector_updated_by='System' ");
                queryBuilder.append("where");
                queryBuilder.append("  (");
                queryBuilder.append("    ar.collector_updated_by is null");
                queryBuilder.append("    or ar.collector_updated_by = ''");
                queryBuilder.append("    or ar.collector_updated_by = 'System'");
                queryBuilder.append(" or (ar.ar_contact_code IS NULL AND ar.collector_updated_by <> 'System' AND ar.collector_updated_by <> '') )");
                getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
                getCurrentSession().flush();
            }
            if (CommonUtils.isNotEmpty(collector.getNameRange())) {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("update");
                queryBuilder.append("   cust_accounting ar");
                queryBuilder.append("   join trading_partner tp");
                queryBuilder.append("     on (");
                queryBuilder.append("       ar.acct_no = tp.acct_no");
                queryBuilder.append("       and tp.acct_name rlike '^[").append(collector.getNameRange()).append("]'");
                queryBuilder.append("     )");
                queryBuilder.append("set");
                queryBuilder.append("   ar.ar_contact_code = ").append(collector.getUserId()).append(",");
                queryBuilder.append("   ar.collector_updated_by='System' ");
                queryBuilder.append("where");
                queryBuilder.append("  (");
                queryBuilder.append("    ar.collector_updated_by is null");
                queryBuilder.append("    or ar.collector_updated_by = ''");
                queryBuilder.append("    or ar.collector_updated_by = 'System'");
                queryBuilder.append(" or (ar.ar_contact_code IS NULL AND ar.collector_updated_by <> 'System' AND ar.collector_updated_by <> '') )");
                getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
            }
        }
    }

    public String getLoginName(Integer userId) {
        String query = "select login_name from user_details where user_id=" + userId;
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public List<String> getCollectors(String loginName) {
        StringBuilder queryBuilder = new StringBuilder("select ud.login_name from user_details ud");
        queryBuilder.append(" join cust_accounting ca on ca.ar_contact_code=ud.user_id");
        queryBuilder.append(" where ud.login_name like '").append(loginName).append("%'");
        queryBuilder.append(" group by ud.user_id");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<LabelValueBean> getCollectors() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select login_name as loginName,");
        queryBuilder.append("cast(user_id as char character set latin1) loginId");
        queryBuilder.append(" from user_details ud");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        List<LabelValueBean> collectors = new ArrayList<LabelValueBean>();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            collectors.add(new LabelValueBean((String) col[0], (String) col[1]));
        }
        return collectors;
    }

    public List<LabelValueBean> getDomesticUser() {
        List<User> users = null;
        Criteria criteria = getCurrentSession().createCriteria(User.class, "user");
        criteria.createAlias("user.role", "role");
        Criterion role1 = Restrictions.eq("role.roleDesc", CommonConstants.ROLE_NAME_CUSTOMER);
        Criterion role2 = Restrictions.eq("role.roleDesc", CommonConstants.ROLE_NAME_SALES);
        Criterion role3 = Restrictions.eq("role.roleDesc", CommonConstants.ROLE_NAME_ADMIN);
        LogicalExpression orExp1 = Restrictions.or(role1, role2);
        LogicalExpression orExp2 = Restrictions.or(role1, role3);
        criteria.add(Restrictions.or(orExp1, orExp2));
        users = criteria.list();
        List<LabelValueBean> domesticUser = new ArrayList<LabelValueBean>();
        domesticUser.add(new LabelValueBean("All", "All"));
        for (User row : users) {
            if (CommonUtils.isNotEmpty(row.getLastName())) {
                domesticUser.add(new LabelValueBean(row.getFirstName() + " " + row.getLastName(), "" + row.getUserId()));
            } else {
                domesticUser.add(new LabelValueBean(row.getFirstName(), "" + row.getUserId()));
            }
        }
        return domesticUser;
    }

    public User getUserInfo(Integer userId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select first_name as firstName,");
        queryBuilder.append("last_name as lastName,");
        queryBuilder.append("login_name as loginName,");
        queryBuilder.append("email as email,");
        queryBuilder.append("telephone as telephone, ");
        queryBuilder.append("fax as fax ");
        queryBuilder.append(" from user_details");
        queryBuilder.append(" where user_id = ").append(userId);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(User.class));
        return (User) query.uniqueResult();
    }

    public User getUserInfo(String loginName) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  u.user_id as userId,");
        queryBuilder.append("  u.first_name as firstName,");
        queryBuilder.append("  u.last_name as lastName,");
        queryBuilder.append("  u.login_name as loginName,");
        queryBuilder.append("  u.email as email,");
        queryBuilder.append("  t.trmnum as `terminal.trmnum` ");
        queryBuilder.append("from");
        queryBuilder.append("  user_details u");
        queryBuilder.append("  left join terminal t");
        queryBuilder.append("    on (u.terminal_id = t.trmnum) ");
        queryBuilder.append("where login_name = '").append(loginName).append("'");
        queryBuilder.append("  and status='ACTIVE' ");
        queryBuilder.append("limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("userId", IntegerType.INSTANCE);
        query.addScalar("firstName", StringType.INSTANCE);
        query.addScalar("lastName", StringType.INSTANCE);
        query.addScalar("loginName", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("terminal.trmnum", StringType.INSTANCE);
        query.setResultTransformer(new ChainedFieldsTransformer(User.class, null));
        return (User) query.uniqueResult();
    }

    public List<LabelValueBean> getPackageList() {
        List<LabelValueBean> packages = new ArrayList<LabelValueBean>();
        packages.add(new LabelValueBean("Select", ""));
        packages.add(new LabelValueBean("Bag", "BAG"));
        packages.add(new LabelValueBean("Bundle", "BDL"));
        packages.add(new LabelValueBean("Box", "BOX"));
        packages.add(new LabelValueBean("Barrel", "BRL"));
        packages.add(new LabelValueBean("Crate", "CRT"));
        packages.add(new LabelValueBean("Carton", "CTN"));
        packages.add(new LabelValueBean("Drum", "DRM"));
        packages.add(new LabelValueBean("Pail", "PAL"));
        packages.add(new LabelValueBean("Pieces", "PCS"));
        packages.add(new LabelValueBean("REEL", "REL"));
        packages.add(new LabelValueBean("Other", "OTH"));
        return packages;
    }

    public List<LabelValueBean> getPalletList() {
        List<LabelValueBean> pallets = new ArrayList<LabelValueBean>();
        pallets.add(new LabelValueBean("Select", ""));
        pallets.add(new LabelValueBean("Pallet", "PLT"));
        pallets.add(new LabelValueBean("Skid", "SKD"));
        pallets.add(new LabelValueBean("Loose", "LSE"));
        pallets.add(new LabelValueBean("Other", "OTH"));
        return pallets;
    }

    public String getOutSourceEmail(String userId) throws Exception {
        String queryString = "SELECT outsource_email FROM user_details WHERE user_id='" + userId + "'";
        Object query = (String) getSession().createSQLQuery(queryString).uniqueResult();
        return null != query ? query.toString() : "";
    }

    public String getAchApproverEmails() throws Exception {
        String queryString = "select group_concat(`email`) as email from `user_details` where ach_approver = true";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.addScalar("email", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public String getApSpecialistEmail(String vendorNo) throws Exception {
        String queryString = "select ud.`email` as email from `vendor_info` vi, `user_details` ud where vi.`cust_accno` = :vendorNo and vi.`ap_specialist` = ud.`user_id`";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("vendorNo", vendorNo);
        query.addScalar("email", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public String getEmail(Integer userId) {
        String queryString = "select ud.email as email from user_details ud where ud.user_id=:userId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setInteger("userId", userId);
        String email = (String) query.setMaxResults(1).uniqueResult();
        return null != email ? email : "";
    }

    public List<User> getInternalCollections() throws Exception {
        String queryString = "select user from User user where user.role=243";
        return getCurrentSession().createQuery(queryString).list();
    }

    public String getUserColumnValueWithUserId(String userId, String columnName) throws Exception {
        String queryString = "select "+columnName+" from user_details u where u.user_id=:userId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        Object result = query.uniqueResult();
        return null != result ? result.toString() : "";
    }

   public String getUserValueWithUserId(String userId, String columnName) throws Exception {
        String queryString = "select " + columnName + " FROM user_details JOIN terminal  WHERE user_id=:userId  AND trmnum=`terminal_id`";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        Object result = query.uniqueResult();
        return null != result ? result.toString() : "";
    }         
}
