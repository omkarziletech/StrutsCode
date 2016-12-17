package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.Country;
import com.gp.cong.logisoft.domain.State;

//import com.gerber.domain.Consignee;
/**
 * @author Gho
 *
 */
public class StateDAO extends BaseHibernateDAO {

    private static final String StateID = "id";
    private static final String State = "notify_party";
    private static final String CUSTOMER = "customer";

    /**
     * get all The State Instances
     * @return
     */
    public List getAllNotifyParties() throws Exception{
            String queryString = "from State";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public Iterator getAllStatesForDisplay()  throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select State.id,State.stateName from State State").list().iterator();
        return results;
    }

    public Iterator getStatesByCountry(Country country) throws Exception{
        if (country != null) {
        }
            String queryString = "select model.id, model.stateName from State as model where model.country=?0";
            Query query = getCurrentSession().createQuery(queryString);
            query.setParameter("0", country);
            return query.list().iterator();
    }

    public State findById(Integer id)  throws Exception{
            State instance = (State) getCurrentSession().get("com.gp.cong.logisoft.domain.State", id);
            return instance;
    }

    public List findByProperty(String propertyName, Object value)  throws Exception{
            String queryString = "select model.id,model.StateName " +
                    "from State as model where model." + propertyName + "= ?0";

            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByCustomerid(Object customerid)  throws Exception{
        return findByProperty(CUSTOMER, customerid);
    }

    public State findByCustomerId(Object customerid)  throws Exception{
        String queryString = "from State as model where model." + StateDAO.CUSTOMER + "= ?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", customerid);
        return (State)queryObject.setMaxResults(1).uniqueResult();
    }
}
