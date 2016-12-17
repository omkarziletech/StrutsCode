package com.gp.cong.logisoft.hibernate.dao;

/**
 * @author Rohith
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.PortException;
import com.gp.cong.logisoft.domain.RelayOrigin;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.Warehouse;

public class PortExceptionDAO extends BaseHibernateDAO {


    /**
     * Method for Saving Warehouses
     * @param transientInstance
     */
    public void save(PortException transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    /**
     *
     * @param id
     * @return id for the domain class Warehouse.java
     */
    public RelayOrigin findById(String id)throws Exception {
            RelayOrigin instance = (RelayOrigin) getCurrentSession().get("com.gp.cong.logisoft.domain.RelayOrigin", id);
            return instance;
    }

    /**
     *
     * @param warehouseCode
     * @return getting all warehouse codes from the domain class Warehouse
     */
    public List findWarehouseCode(String warehouseCode)throws Exception {
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
    public List findForSearchWarehouseAction(String warehouseCode, String warehouseName, String city, String airCargo)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            if (warehouseCode != null && !warehouseCode.equals("")) {
                criteria.add(Restrictions.like("id", warehouseCode + "%"));
            }

            if (warehouseName != null && !warehouseName.equals("")) {
                criteria.add(Restrictions.like("warehouseName", warehouseName + "%"));

            }

            if (city != null && !city.equals("0") && !city.equals("")) {

                UnLocationDAO unLocationDAO = new UnLocationDAO();
                UnLocation cityobj = unLocationDAO.findById(new Integer(city));
                criteria.add(Restrictions.like("cityCode", cityobj));

            }

            if (airCargo != null && !airCargo.equals("")) {
                criteria.add(Restrictions.like("acWarehouseName", airCargo + "%"));

            }
            criteria.addOrder(Order.asc("id"));
            return criteria.list();
    }

    public List findAllWarehouses()throws Exception {
            String queryString = "from Warehouse";
            Query queryObject = getCurrentSession().createQuery(queryString);

            return queryObject.list();
    }

    public List findRoleName(Role roleId)throws Exception {
        List list = new ArrayList();
            String queryString = "from User where role=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", roleId);
            list = queryObject.list();
        return list;
    }

    public void delete(Warehouse persistanceInstance) throws Exception {
            getSession().delete(persistanceInstance);
            getSession().flush();
    }

    public void update(Warehouse persistanceInstance, String userName)throws Exception {
            getSession().saveOrUpdate(persistanceInstance);
    }

    public Integer getNumberOFPortexceptions(Integer relayId) throws Exception {
        Iterator results = null;
            String queryString = "select count(*) from PortException where relayId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", relayId);
            results = queryObject.list().iterator();

            while (results.hasNext()) {
                int i = Integer.parseInt(results.next().toString());
                Integer noofbatches = new Integer(i);
                return noofbatches;
            }
        return null;
    }
}
