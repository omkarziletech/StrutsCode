package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.FclInbondDetails;
import java.util.List;
import org.hibernate.Query;


public class FclInbondDetailsDAO extends BaseHibernateDAO {

    public void save(FclInbondDetails transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }
    public FclInbondDetails findById(Integer id)throws Exception {
            FclInbondDetails instance = (FclInbondDetails) getSession().get(
                    "com.gp.cong.logisoft.domain.FclInbondDetails", id);
            return instance;
    }
    public void delete(FclInbondDetails persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }
    public List findAll()throws Exception {
            String queryString = "from FclInbondDetails";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
    }
    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from FclInbondDetails as model where model." + propertyName + "= ?0 and model.bolId is not null";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }
    public List findAesdetails(String propertyName, Object value) throws Exception {
            String queryString = "from FclAESDetails as model where model." + propertyName + "= ?0 and model.trailerNoId is not null";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }
}
