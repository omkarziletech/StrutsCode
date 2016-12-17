package com.gp.cong.logisoft.hibernate.dao;

/**
 * @author Rohith
 */
import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.WarehouseTemp;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;

public class WarehouseDAO extends BaseHibernateDAO {

    /**
     * Method for Saving Warehouses
     *
     * @param transientInstance
     */
    public void save(Warehouse transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    /**
     *
     * @param id
     * @return id for the domain class Warehouse.java
     */
    public WarehouseTemp findById1(String id) throws Exception {
        WarehouseTemp instance = (WarehouseTemp) getCurrentSession().get("com.gp.cong.logisoft.domain.WarehouseTemp", id);
        return instance;
    }

    public Warehouse findById(Integer id) throws Exception {
        Warehouse instance = (Warehouse) getCurrentSession().get("com.gp.cong.logisoft.domain.Warehouse", id);
        return instance;
    }

    /**
     *
     * @param warehouseCode
     * @return getting all warehouse codes from the domain class Warehouse
     */
    public List findWarehouseCode(String warehouseCode) throws Exception {
        List list = new ArrayList();
        String queryString = "from Warehouse where id like'" + warehouseCode + "%'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public String[] getWarehouseAccountNo(String cfsWarehouseNo) throws Exception {
        String Values[] = new String[2];
        String queryString = "SELECT tp.acct_name,tp.acct_no FROM warehouse w JOIN trading_partner tp ON tp.acct_no=w.vendorno WHERE w.warehsno='" + cfsWarehouseNo + "'";
        Object queryObject = getCurrentSession().createSQLQuery(queryString.toString()).setMaxResults(1).uniqueResult();
        if (queryObject != null) {
            Object[] valueObj = (Object[]) queryObject;
            if (valueObj[0] != null && !valueObj[0].toString().trim().equals("")) {
                Values[0] = valueObj[0].toString();
            }
            if (valueObj[1] != null && !valueObj[1].toString().trim().equals("")) {
                Values[1] = valueObj[1].toString();
            }
        }
        return Values;
    }

    public List findWarehouseCode1(String warehouseCode) throws Exception {
        List list = new ArrayList();
        String queryString = "from WarehouseTemp where id like'" + warehouseCode + "%'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    /**
     *
     * @param warehouseCode
     * @param warehouseName
     * @param city
     * @param airCargo
     * @return This method is used for Searching the List of warehouses based on
     * the above parameters
     */
    public List findForSearchWarehouseAction(String warehouseCode, String warehouseName, String city, String airCargo, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(WarehouseTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (warehouseCode != null && !warehouseCode.equals("")) {
            criteria.add(Restrictions.ge("warehouseNo", warehouseCode));
            criteria.addOrder(Order.asc("warehouseNo"));
        }

        if (warehouseName != null && !warehouseName.equals("")) {
            criteria.add(Restrictions.ge("warehouseName", warehouseName));
            criteria.addOrder(Order.asc("warehouseName"));
        }
        if (city != null && !city.equals("")) {
            criteria.add(Restrictions.ge("city", city));
            criteria.addOrder(Order.asc("city"));
        }
        return criteria.list();
    }

    public List findForSearchWarehouse(String warehouseCode, String warehouseName, String city, String airCargo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(WarehouseTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (warehouseCode != null && !warehouseCode.equals("")) {
            criteria.add(Restrictions.like("warehouseNo", warehouseCode + "%"));
            criteria.addOrder(Order.asc("warehouseNo"));
        }
        if (warehouseName != null && !warehouseName.equals("")) {
            criteria.add(Restrictions.like("warehouseName", warehouseName + "%"));
            criteria.addOrder(Order.asc("warehouseName"));
        }
        if (city != null && !city.equals("")) {
            criteria.add(Restrictions.like("city", city + "%"));
            criteria.addOrder(Order.asc("city"));
        }
        if (airCargo != null && !airCargo.equals("")) {
            criteria.add(Restrictions.like("acWarehouseName", airCargo + "%"));
        }
        return criteria.list();
    }

    public List searchWarehouse(String warehouseCode, String warehouseName, String city, String type) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(WarehouseTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (warehouseCode != null && !warehouseCode.equals("")) {
            criteria.add(Restrictions.like("warehouseNo", warehouseCode + "%"));
            criteria.addOrder(Order.asc("warehouseNo"));
        }
        if (warehouseName != null && !warehouseName.equals("")) {
            criteria.add(Restrictions.like("warehouseName", warehouseName + "%"));
            criteria.addOrder(Order.asc("warehouseName"));
        }
        if (city != null && !city.equals("")) {
            criteria.add(Restrictions.like("city", city + "%"));
            criteria.addOrder(Order.asc("city"));
        }
        if (type != null && !type.equals("")) {
            criteria.add(Restrictions.eq("warehouseType", type));
        }

        return criteria.list();
    }

    public List findForWarehousenameAndAddress(String warehouseName, String warehouseAdress) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (warehouseName != null && !warehouseName.equals("")) {
            criteria.add(Restrictions.like("warehouseName", warehouseName + "%"));
            criteria.addOrder(Order.asc("warehouseName"));
        }
        if (warehouseAdress != null && !warehouseAdress.equals("")) {
            criteria.add(Restrictions.like("address", warehouseAdress + "%"));
            //criteria.addOrder(Order.asc("address"));
        }
//            criteria.add(Restrictions.eq("warehouseType",  "FCL"));
        return criteria.list();
    }

    public List findForWarehousenameByType(String warehouseName, String type) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.createAlias("cityCode", "city");
        if (warehouseName != null && !warehouseName.equals("")) {
            Criterion name = Restrictions.like("warehouseName", warehouseName + "%");
            Criterion code = Restrictions.like("warehouseNo", warehouseName + "%");
            Criterion city = Restrictions.like("city.unLocationName", warehouseName + "%");
            LogicalExpression orExp = Restrictions.or(name, code);
            criteria.add(Restrictions.or(orExp, city));
            criteria.addOrder(Order.asc("warehouseName"));
        }
        criteria.add(Restrictions.eq("warehouseType", type));
        return criteria.list();
    }

    public List findForWarehouseNo(String wareHouseCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (wareHouseCode != null && !wareHouseCode.equals("")) {
            criteria.add(Restrictions.like("warehouseNo", wareHouseCode + "%"));
            criteria.addOrder(Order.asc("warehouseNo"));
        }
        return criteria.list();
    }

    public boolean findForWarehouseNoAvailability(String wareHouseCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (wareHouseCode != null && !wareHouseCode.equals("")) {
            criteria.add(Restrictions.eq("warehouseNo", wareHouseCode));
            criteria.addOrder(Order.asc("warehouseNo"));
        }
        if (CommonUtils.isNotEmpty(criteria.list())) {
            return true;
        }
        return false;
    }

    public List findAllWarehouses() throws Exception {
        String queryString = "from WarehouseTemp order by id";
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

    public void delete(Warehouse persistanceInstance, String userName) throws Exception {
        getSession().delete(persistanceInstance);
    }

    public void update(Warehouse persistanceInstance, String userName) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
    }

    public Integer warehouseNo(String unloc) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id FROM warehouse w LEFT JOIN terminal t ON w.warehsno=CONCAT('T',t.trmnum) WHERE unlocationcode1='" + unloc + "' LIMIT 1");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        return (Integer) queryObject.uniqueResult();
    }

    public Warehouse getByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return (Warehouse) criteria.uniqueResult();
    }

    public Warehouse getWareHouseBywarehsNo(String warehouseNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("warehouseNo", warehouseNo));
        criteria.addOrder(Order.desc("id"));
        return (Warehouse) criteria.setMaxResults(1).uniqueResult();
    }

    public Integer getWarehouseId(String unloc) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id FROM warehouse w LEFT JOIN terminal t ON w.warehsno=CONCAT('W',t.trmnum) WHERE unlocationcode1=:unloc AND t.actyon = 'y' LIMIT 1");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.setParameter("unloc", unloc);
        return (Integer) queryObject.uniqueResult();
    }

    public Integer getWarehouseId(String unloc, String wareCode) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT id ");
        queryStr.append(" FROM  warehouse w WHERE ");
        queryStr.append(" w.warehsno= CONCAT(:wareCode,(SELECT terminal_trmnum FROM un_location WHERE un_loc_code=:unlocCode limit 1)) ");
        Query queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("wareCode", wareCode);
        queryObject.setParameter("unlocCode", unloc);
        return (Integer) queryObject.uniqueResult();
    }
    public Integer getLclWarehouseRouting(Integer originId, Integer destinationId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT w.id FROM warehouse w JOIN LCL_WHSE_ROUTING r ON r.warehouse_id = w.id WHERE r.origin_id=:originId AND r.destination_id=:destinationId AND r.active = 1 limit 1");
        Query queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("originId", originId);
        queryObject.setParameter("destinationId", destinationId);
        return (Integer) queryObject.uniqueResult();
    }
    public Integer getLclDefaultWarehouseRouting(Integer originId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT w.id FROM warehouse w JOIN LCL_WHSE_ROUTING r ON r.warehouse_id = w.id WHERE r.origin_id=:originId AND r.default = 1 AND r.active = 1 limit 1");
        Query queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("originId", originId);
        return (Integer) queryObject.uniqueResult();
    }
    public String[] getWhseDetails(Long ssHeaderId, Long lclUnitId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT w.vendorno,t.`acct_name` FROM warehouse w ");
        queryString.append(" JOIN  lcl_unit_ss_imports lusi ON lusi.cfs_warehouse_id = w.id ");
        queryString.append(" JOIN `trading_partner` t ON w.`vendorno` = t.`acct_no`");
        queryString.append(" WHERE lusi.ss_header_id =:ssHeaderId AND lusi.unit_id =:lclUnitId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString.toString());
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.setParameter("lclUnitId", lclUnitId);
        List values = queryObject.list();
        String[] data = new String[2];
        if (values != null && values.size() > 0) {
            Object[] value = (Object[]) values.get(0);
            if (value[0] != null && !value[0].toString().trim().equals("")) {
                data[0] = value[0].toString();
            }
            if (value[1] != null && !value[1].toString().trim().equals("")) {
                data[1] = value[1].toString();
            }
        }
        return data;
    }
}
