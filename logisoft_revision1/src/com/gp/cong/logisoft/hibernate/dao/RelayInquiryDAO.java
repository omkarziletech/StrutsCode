package com.gp.cong.logisoft.hibernate.dao;

/**
 * @author Rohith
 */
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.RelayInquiry;
import com.gp.cong.logisoft.domain.RelayInquiryTemp;
import com.gp.cong.logisoft.domain.Role;

public class RelayInquiryDAO extends BaseHibernateDAO {


    /**
     * Method for Saving Warehouses
     * @param transientInstance
     */
    public void save(RelayInquiry transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    /**
     *
     * @param id
     * @return id for the domain class Relay.java
     */
    public RelayInquiry findById(Integer relayId) throws Exception {
            RelayInquiry instance = (RelayInquiry) getCurrentSession().get("com.gp.cong.logisoft.domain.RelayInquiry", relayId);
            return instance;
    }

    public List findPortCode(String polcode, String podcode) throws Exception {
        List list = new ArrayList();
            String queryString = "from RelayInquiry where polcode=?0 and podcode=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", polcode);
            queryObject.setParameter("1", podcode);
            list = queryObject.list();
        return list;
    }

    /**
     *
     * @param warehouseCode
     * @return getting all warehouse codes from the domain class Relay
     */
    public List findWarehouseCode(String warehouseCode) throws Exception {
        List list = new ArrayList();
            String queryString = "from Warehouse where id=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", warehouseCode);
            list = queryObject.list();
        return list;
    }

    /**
     *
     * @param warehouseCode
     * @param warehouseName
     * @param city
     * @param airCargo
     * @return This method is used for Searching the List of warehouses based on the above parameters
     */
    public List findForSearchRelayAction(String polCode, String podCode)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(RelayInquiryTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            if (polCode != null && !polCode.equals("")) {
                criteria.add(Restrictions.like("polName", polCode + "%"));
                criteria.addOrder(Order.asc("polName"));
            }

            if (podCode != null && !podCode.equals("")) {
                criteria.add(Restrictions.like("podName", podCode + "%"));
                if (polCode == null || polCode.equals("")) {
                    criteria.addOrder(Order.asc("podName"));
                }
            }
            return criteria.list();
    }

    public List findForSearchRelayStartAction(String polCode, String podCode, String match) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(RelayInquiryTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (polCode != null && !polCode.equals("")) {


                criteria.add(Restrictions.ge("polName", polCode));
                criteria.addOrder(Order.asc("polName"));
            }
            if (podCode != null && !podCode.equals("")) {

                criteria.add(Restrictions.ge("podName", podCode));
                if (polCode == null || polCode.equals("")) {
                    criteria.addOrder(Order.asc("podName"));
                }
            }
            return criteria.list();
    }

    public List findAllRelays() throws Exception {
            String queryString = "from RelayInquiryTemp Order by polName";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List findRoleName(Role roleId) throws Exception {
        List list = new ArrayList();
            String queryString = "from User where role=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", roleId);
            list = queryObject.list();
        return list;
    }

    public void update(RelayInquiry persistanceInstance, String userName) throws Exception {
            getSession().saveOrUpdate(persistanceInstance);
            getSession().flush();
    }

    public void delete(RelayInquiry persistanceInstance, String userName1) throws Exception {
            getSession().delete(persistanceInstance);
    }
}
