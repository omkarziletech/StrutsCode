package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.util.CommonFunctions;
import org.hibernate.SQLQuery;

public class ProcessInfoDAO extends BaseHibernateDAO {

    public void save(ProcessInfo transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public ProcessInfo findById(Integer programid, String recordid, String deletestatus, String editstatus) throws Exception {
	String queryString = " from ProcessInfo where programid=?0 and recordid=?1 and (editstatus=?2 or editstatus=?3)";
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", programid);
	queryObject.setParameter("1", recordid);
	queryObject.setParameter("2", deletestatus);
	queryObject.setParameter("3", editstatus);
        return (ProcessInfo) queryObject.setMaxResults(1).uniqueResult();
    }

    public ProcessInfo findById(Integer id) throws Exception {
	ProcessInfo instance = (ProcessInfo) getSession().get("com.gp.cong.logisoft.domain.ProcessInfo", id);
	return instance;
    }

    public void update(ProcessInfo persistanceInstance) throws Exception {
	getSession().update(persistanceInstance);
	getSession().flush();
    }

    public void delete(ProcessInfo persistanceInstance) throws Exception {
	getSession().delete(persistanceInstance);
	getSession().flush();
    }

    public ProcessInfo getProcessInfo(int userid) throws Exception {
	String queryString = " from ProcessInfo where userid=?0";
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", userid);
	ProcessInfo processInfo = (ProcessInfo)queryObject.setMaxResults(1).uniqueResult();
	return processInfo;
    }

    public List getProcessInf(int userid) throws Exception {
	String queryString = " from ProcessInfo where userid=?0";
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", userid);
	List queryList = queryObject.list();
	return queryList;
    }

    public ProcessInfo getProcess(String editstatus) throws Exception {
	String queryString = " from ProcessInfo where editstatus=?0";
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", editstatus);
	ProcessInfo getProcess = (ProcessInfo)queryObject.setMaxResults(1).uniqueResult();
	return getProcess;
    }

    public List getLockedPages() throws Exception {
	String queryString = " from ProcessInfo";
	Query queryObject = getCurrentSession().createQuery(queryString);
	List queryList = queryObject.list();
	return queryList;
    }

    public List getLockedPages(Integer userid) throws Exception {
	String queryString = " from ProcessInfo where userid='" + userid + "' and ACTION='FILENO'";
	Query queryObject = getCurrentSession().createQuery(queryString);
	List queryList = queryObject.list();
	return queryList;
    }

    public ProcessInfo findWhoLockedRecords(String module, String fileNo, Integer userid) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(ProcessInfo.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (CommonFunctions.isNotNull(module)) {
	    criteria.add(Restrictions.eq("action", module));
	}
	if (CommonFunctions.isNotNull(fileNo)) {
	    criteria.add(Restrictions.like("recordid", fileNo + "%"));
	}
	if (CommonFunctions.isNotNull(userid)) {
	    criteria.add(Restrictions.eq("userid", userid));
	}
        criteria.setMaxResults(1);
	ProcessInfo processInfo = (ProcessInfo)criteria.uniqueResult();
	return processInfo;
    }

    public ProcessInfo findByFileNo(String fileNo, Integer userId) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(ProcessInfo.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

	if (CommonFunctions.isNotNull(fileNo)) {
	    criteria.add(Restrictions.like("recordid", fileNo));
	}
	if (CommonFunctions.isNotNull(userId)) {
	    criteria.add(Restrictions.ne("userid", userId));
	}
        criteria.setMaxResults(1);
	ProcessInfo processInfo = (ProcessInfo)criteria.uniqueResult();
	return processInfo;
    }

    public ProcessInfo findByFileNoAndUserId(String fileNo, Integer userId) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(ProcessInfo.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (CommonFunctions.isNotNull(fileNo)) {
	    criteria.add(Restrictions.like("recordid", fileNo));
	}
	if (CommonFunctions.isNotNull(userId)) {
	    criteria.add(Restrictions.eq("userid", userId));
	}
        criteria.setMaxResults(1);
	ProcessInfo processInfo = (ProcessInfo)criteria.uniqueResult();
        return processInfo;
    }

    public void truncate() throws Exception {
	getCurrentSession().createSQLQuery("delete from process_info").executeUpdate();
    }

    public void releaseLockByRecordIdAndModuleId(String recordId, String moduleId, Integer userId) throws Exception {
	String queryString = " delete from ProcessInfo where userid=?0 and recordid=?1 and moduleId=?2";
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", userId);
	queryObject.setParameter("1", recordId);
	queryObject.setParameter("2", moduleId);
	queryObject.executeUpdate();
    }

    public void releaseFclOpsFile(String recordId, Integer userId) throws Exception {
	String queryString = " delete from process_info where record_id=?0 and user_id=?1 and module_id in ('QUOTE','BOOKING','FCLBL')";
	SQLQuery query = getCurrentSession().createSQLQuery(queryString);
	query.setParameter("0", recordId);
	query.setParameter("1", userId);
	query.executeUpdate();
    }

    public ProcessInfo getProcessInfo(String recordId, String moduleId, String action) {
	Criteria criteria = getCurrentSession().createCriteria(ProcessInfo.class);
	criteria.add(Restrictions.eq("recordid", recordId));
	criteria.add(Restrictions.eq("moduleId", moduleId));
	criteria.add(Restrictions.eq("action", action));
	criteria.setMaxResults(1);
	return (ProcessInfo) criteria.uniqueResult();
    }

    public void deleteProcessInfo(Integer userId, String moduleId) {
	StringBuilder queryBuilder = new StringBuilder("delete from process_info");
	queryBuilder.append(" where user_id = '").append(userId).append("'");
	queryBuilder.append(" and module_id = '").append(moduleId).append("'");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteProcessInfo(Integer userId, String moduleId, String action) {
	StringBuilder queryBuilder = new StringBuilder("delete from process_info");
	queryBuilder.append(" where user_id = '").append(userId).append("'");
	queryBuilder.append(" and module_id = '").append(moduleId).append("'");
	queryBuilder.append(" and action = '").append(action).append("'");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }
}
