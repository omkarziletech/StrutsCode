package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.GenericCode;

public class FCLPortConfigurationDAO extends BaseHibernateDAO {

    public GenericCode getReleaseClause(String destination) throws Exception {
        String queryString = " select f.blClauseId from FCLPortConfiguration f,Ports p where f.shedulenumber=p.id and p.unLocationCode='" + destination + "'";
        Object obj = (GenericCode)getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return (GenericCode)obj;
    }

    public List<FCLPortConfiguration> getPortRules(Integer portId) throws Exception {
        String queryString = "from FCLPortConfiguration f where f.shedulenumber=" + portId + ""
                + "and ((f.nadmRule is not null and  f.nadmRule!=0) or (f.ncomRule is not null and "
                + " f.ncomRule!=0) or (f.radmRule is not null and  f.radmRule!=0) or "
                + "(f.rcomRule is not null and  f.rcomRule!=0))";
        return getCurrentSession().createQuery(queryString).list();
    }

    public FCLPortConfiguration findById(java.lang.Integer id) throws Exception {
        FCLPortConfiguration instance = (FCLPortConfiguration) getSession().get(
                "com.gp.cong.logisoft.domain.FCLPortConfiguration", id);
        return instance;
    }

    public void update(FCLPortConfiguration persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }
}
