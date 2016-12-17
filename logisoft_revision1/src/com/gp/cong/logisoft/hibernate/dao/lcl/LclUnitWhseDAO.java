/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;

public class LclUnitWhseDAO extends BaseHibernateDAO<LclUnitWhse> {

    private static final Log log = LogFactory.getLog(LclUnitWhse.class);

    public LclUnitWhseDAO() {
        super(LclUnitWhse.class);
    }

    public void insert(Long ssHeaderId, Long unitId, Integer wareHouseId, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_unit_whse(ss_header_id,unit_id,warehouse_id,");
        queryBuilder.append("entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (");
        queryBuilder.append(":ssHeaderId,:unitId,:wareHouseId,:sysDate,:userId,:sysDate,:userId)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("ssHeaderId", ssHeaderId);
        query.setParameter("unitId", unitId);
        query.setParameter("wareHouseId", wareHouseId);
        query.setParameter("sysDate", new Date());
        query.setParameter("userId", userId);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public void insert(String unitId, String wareHouseId, String userId, String sealNo, String chassisNo, String loadBy) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_unit_whse(unit_id,warehouse_id,");
        queryBuilder.append("entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id,seal_no_out,chassis_no_in,stuffed_user_id) VALUES (");
        queryBuilder.append(":unitId,:wareHouseId,:sysDate,:userId,:sysDate,:userId,:sealNo,:chassisNo,:loadBy)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("unitId", unitId);
        query.setParameter("wareHouseId", wareHouseId);
        query.setParameter("sysDate", new Date());
        query.setParameter("userId", userId);
        query.setParameter("sealNo", sealNo);
        query.setParameter("chassisNo", chassisNo);
        query.setParameter("loadBy", loadBy.equalsIgnoreCase("") ? null : loadBy);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public Boolean isWarehouse(Long ssHeaderId, Long unitId, Integer warehouseId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select  if(count(*)>0, true,false) as result from lcl_unit_whse ");
        queryBuilder.append(" where ss_header_id=:ssHeaderId and unit_id=:unitId ");
        queryBuilder.append(" and warehouse_id=:warehouseId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("ssHeaderId", ssHeaderId);
        query.setParameter("unitId", unitId);
        query.setParameter("warehouseId", warehouseId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public LclUnitWhse createInstance(LclUnit lclunit, LclSsHeader lclSsHeader, User user, Date now) throws Exception {
        LclUnitWhse lclUnitWhse = this.getLclUnitWhseFirstRecord(lclunit.getId(), lclSsHeader.getId());
        if (lclUnitWhse == null) {
            lclUnitWhse = new LclUnitWhse();
            RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(lclSsHeader.getOrigin().getUnLocationCode(), "Y");
            if (terminal != null) {
                Warehouse wareHouse = new WarehouseDAO().getWareHouseBywarehsNo("W" + terminal.getTrmnum());
                lclUnitWhse.setWarehouse(wareHouse);
            }
            lclUnitWhse.setLclSsHeader(lclSsHeader);
            lclUnitWhse.setDepartedDatetime(now);
            lclUnitWhse.setLclUnit(lclunit);
            lclUnitWhse.setDepartedDatetime(now);
            lclUnitWhse.setEnteredBy(user);
            lclUnitWhse.setEnteredDatetime(now);
        }
        lclUnitWhse.setModifiedBy(user);
        lclUnitWhse.setModifiedDatetime(now);
        return lclUnitWhse;
    }

    public LclUnitWhse getLclUnitWhseDetails(Long unitId, Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitWhse.class, "lclUnitWhse");
        criteria.createAlias("lclUnitWhse.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitWhse.lclSsHeader", "lclSsHeader");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        return (LclUnitWhse) criteria.uniqueResult();
    }

    public String getLocationValues(Long unitId, Long headerId) throws Exception {
        String queryStr = "select location from lcl_unit_whse uw where uw.unit_id =:unitId and uw.ss_header_id =:headerId order by uw.id desc limit 1";
        SQLQuery queryObject = getSession().createSQLQuery(queryStr);
        queryObject.setParameter("unitId", unitId);
        queryObject.setParameter("headerId", headerId);
        String location = (String) queryObject.uniqueResult();
        return null != location ? location : "";
    }

    public String getLCLUnitWhseSeal(Long unitId, Long headerId) throws Exception {//seal no coming from suHeadingNote lclunitss.this method may be wrong
        StringBuilder sb = new StringBuilder();
        sb.append("select seal_no_in from lcl_unit_whse uw where uw.unit_id = ").append(unitId).append(" and uw.ss_header_id = ").append(headerId).append(" order by uw.id desc limit 1");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        return (String) queryObject.uniqueResult();
    }

    public String sealNoOut(Long unitId, Long headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select seal_no_out from lcl_unit_whse uw where uw.unit_id = ").append(unitId).append(" and uw.ss_header_id = ").append(headerId).append(" order by uw.id desc limit 1");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        return (String) queryObject.uniqueResult();
    }

    public List<LclUnitWhse> getwhseListByUnitHeaderId(Long unitId, Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitWhse.class, "lclUnitWhse");
        criteria.createAlias("lclUnitWhse.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitWhse.lclSsHeader", "lclSsHeader");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    public LclUnitWhse getLclUnitWhseFirstRecord(Long unitId, Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitWhse.class, "lclUnitWhse");
        criteria.createAlias("lclUnitWhse.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitWhse.lclSsHeader", "lclSsHeader");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (LclUnitWhse) criteria.uniqueResult();
    }

    public void updateSsHeaderId(Long oldSsHeaderId, Long newSsHeaderId, Long unitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_whse set ss_header_id =:newSsHeaderId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_header_id =:oldSsHeaderId and unit_id=:unitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("newSsHeaderId", newSsHeaderId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.setParameter("oldSsHeaderId", oldSsHeaderId);
        queryObject.setParameter("unitId", unitId);
        queryObject.executeUpdate();
    }

    public void updateUnitId(Long ssHeaderId, Long oldUnitId, Long newUnitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE lcl_unit_whse set unit_id =:newUnitId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_header_id =:ssHeaderId  and unit_id=:oldUnitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.setParameter("oldUnitId", oldUnitId);
        queryObject.setParameter("newUnitId", newUnitId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }

    public void deleteWarehouseId(Long newUnitId) throws Exception {
        String query = "delete from lcl_unit_whse where ss_header_id is null and unit_id=:newUnitId ";
        SQLQuery queryObject = getSession().createSQLQuery(query);
        queryObject.setParameter("newUnitId", newUnitId);
        queryObject.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }

    public Boolean isStrippedDateExist(Long ssHeaderId, Long unitId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(DATE_ADD(destuffed_datetime,INTERVAL 5 DAY) >= CURDATE(),TRUE,FALSE) as result FROM ");
        queryStr.append(" lcl_unit_whse where ");
        queryStr.append(" ss_header_id=:ssHeaderId AND unit_id=:unitId ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.setParameter("unitId", unitId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }

    public Object getLoadbyName(Long unitId, String scheduleNO) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT ud.user_id as userId FROM lcl_unit_ss luss ");
        queryStr.append(" JOIN lcl_ss_header lsh ON luss.`ss_header_id` = lsh.`id` ");
        queryStr.append(" JOIN  lcl_unit lu ON lu.id = luss.unit_id ");
        queryStr.append(" JOIN lcl_unit_whse luw ON (luw.`ss_header_id`=lsh.id AND luw.unit_id=lu.id) ");
        queryStr.append(" JOIN user_details ud ON luw.`stuffed_user_id`=ud.`user_id` ");
        queryStr.append(" WHERE lu.id =:unitId AND luss.`ss_header_id` = (SELECT id FROM lcl_ss_header WHERE schedule_no =:scheduleNO) ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setLong("unitId", unitId);
        queryObject.setParameter("scheduleNO", scheduleNO);
        return queryObject.uniqueResult();
    }

    public LclUnitWhse getLclUnitWhse(Integer wareHouseId, Long unitId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitWhse.class, "lclUnitWhse");
        criteria.createAlias("lclUnitWhse.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitWhse.warehouse", "warehouse");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(wareHouseId)) {
            criteria.add(Restrictions.eq("warehouse.id", wareHouseId));
        }
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        return (LclUnitWhse) criteria.uniqueResult();      
    }
}
