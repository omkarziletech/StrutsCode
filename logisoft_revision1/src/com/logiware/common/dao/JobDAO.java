package com.logiware.common.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.common.domain.Job;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Lakshmi Narayanan
 */
public class JobDAO extends BaseHibernateDAO {

    public Job findById(Serializable id) {
	return (Job) getCurrentSession().get(Job.class, id);
    }

    public Job findByClassName(String className) {
	Criteria criteria = getCurrentSession().createCriteria(Job.class);
	criteria.add(Restrictions.eq("className", className));
	criteria.setMaxResults(1);
	return (Job) criteria.uniqueResult();
    }

    public List<Job> getAllJobs() {
	getCurrentSession().flush();
	Criteria criteria = getCurrentSession().createCriteria(Job.class);
	return criteria.list();
    }

    public List<Job> getEnabledJobs() {
	Criteria criteria = getCurrentSession().createCriteria(Job.class);
	criteria.add(Restrictions.eq("enabled", true));
	return criteria.list();
    }
    public String getUserMailId(String userName){
        String query="select email from user_details where login_name='"+userName+"' limit 1";
        Object email=getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
        return null!=email?email.toString():"";
    }
    public boolean getJobStatus(String jobName)throws Exception{
        String q="select enabled from job where name='"+jobName+"'";
        String status=getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return status.equals("true");
    }
}
