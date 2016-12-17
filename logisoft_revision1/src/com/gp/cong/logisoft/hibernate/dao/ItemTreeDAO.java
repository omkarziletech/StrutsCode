package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.Country;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ItemTree;

/**
 * @author Gho
 *
 */
public class ItemTreeDAO extends BaseHibernateDAO {

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
    public String getAccessModeByTabName(String tabName,int roleId,String uniqueCode) throws Exception{
            String queryString = "SELECT r.modify FROM item_master i, role_item_assoc r WHERE i.item_id = r.item_id AND i.item_desc = '"+tabName+"' AND r.role_id ="+roleId+" and i.unique_code = '"+uniqueCode+"'";
            return (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
    }
    public List<ItemTree> findByProperty(String propertyName, Object value) throws Exception{
            String queryString = "from ItemTree as model where model." + propertyName + " = ?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByProperty1(String propertyName, Object value)  throws Exception{
            String queryString = "from ItemTree as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByCustomerid(Object customerid) throws Exception{
        return findByProperty(CUSTOMER, customerid);
    }

    public List getChild(Item parentId) throws Exception{
            Session session = null;
            session = HibernateSessionFactory.getSession();
            String queryString = "from ItemTree where parentId=?0";
            Query queryObject = session.createQuery(queryString);
            queryObject.setParameter("0", parentId);
            return queryObject.list();
    }
}
