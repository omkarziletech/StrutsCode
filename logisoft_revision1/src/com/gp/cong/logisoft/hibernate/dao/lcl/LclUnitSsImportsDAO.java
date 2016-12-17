/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

/**
 *
 * @author Logiware
 */
public class LclUnitSsImportsDAO extends BaseHibernateDAO<LclUnitSsImports> {

    public LclUnitSsImportsDAO() {
        super(LclUnitSsImports.class);
    }

    public LclUnitSsImports getLclUnitSSImportsByHeader(Long unitId, Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitSsImports.class, "lclUnitSs");
        criteria.createAlias("lclUnitSs.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitSs.lclSsHeader", "lclSsHeader");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        criteria.addOrder(Order.desc("id"));
        return (LclUnitSsImports) criteria.setMaxResults(1).uniqueResult();
    }

    public Long[] getWarehouseAndUnitTypeId(Long unitId, Long headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        Long values[] = new Long[2];
        sb.append("SELECT wh.id,lu.unit_type_id FROM lcl_unit_ss_imports lus JOIN lcl_unit lu ON lus.unit_id=lu.id LEFT JOIN warehouse wh ON wh.id=lus.cfs_warehouse_id WHERE lus.unit_id=");
        sb.append(unitId).append(" AND lus.ss_header_id=").append(headerId);
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                values[0] = Long.parseLong(agentObj[0].toString());
            } else {
                values[0] = 0l;
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                values[1] = Long.parseLong(agentObj[1].toString());
            } else {
                values[1] = 0l;
            }
        }
        return values;
    }

    public String[] getCfsWarehouse(String unitId, String headerId) throws Exception {
        getCurrentSession().clear();
        StringBuilder sb = new StringBuilder();
        String cfsValues[] = new String[9];
        sb.append("SELECT wh.warehsname,wh.warehsno,wh.address,wh.city,wh.state,wh.zipcode,wh.phone,wh.fax,wh.id FROM lcl_unit_ss_imports lus ");
        sb.append(" LEFT JOIN warehouse wh ON wh.id=lus.cfs_warehouse_id ").append("WHERE lus.unit_id=").append(unitId).append(" AND lus.ss_header_id=").append(headerId);
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                cfsValues[0] = agentObj[0].toString();
            } else {
                cfsValues[0] = "";
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                cfsValues[1] = agentObj[1].toString();
            } else {
                cfsValues[1] = "";
            }
            if (agentObj[2] != null && !agentObj[2].toString().trim().equals("")) {
                cfsValues[2] = agentObj[2].toString();
            }
            if (agentObj[3] != null && !agentObj[3].toString().trim().equals("")) {
                cfsValues[3] = agentObj[3].toString();
            }
            if (agentObj[4] != null && !agentObj[4].toString().trim().equals("")) {
                cfsValues[4] = agentObj[4].toString();
            }
            if (agentObj[5] != null && !agentObj[5].toString().trim().equals("")) {
                cfsValues[5] = agentObj[5].toString();
            }
            if (agentObj[6] != null && !agentObj[6].toString().trim().equals("")) {
                cfsValues[6] = agentObj[6].toString();
            }
            if (agentObj[7] != null && !agentObj[7].toString().trim().equals("")) {
                cfsValues[7] = agentObj[7].toString();
            }
            if (agentObj[8] != null && !agentObj[8].toString().trim().equals("")) {
                cfsValues[8] = agentObj[8].toString();
            }
        }
        return cfsValues;
    }

    public String[] getEmail1OfColoader(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] coloaderDetails = new String[2];
        sb.append("SELECT ca.email1,fax FROM lcl_unit_ss lus JOIN lcl_unit_ss_imports lusi ON lus.unit_id= lusi.unit_id ");
        sb.append("JOIN cust_address ca ON (ca.acct_no =  lusi.coloader_acct_no AND ca.prime = 'on') WHERE  lus.id= ").append(unitSsId);
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            Object[] obj = (Object[]) list.get(0);
            if (obj[0] != null && !obj[0].toString().trim().equals("")) {
                coloaderDetails[0] = obj[0].toString();
            }
            if (obj[1] != null && !obj[1].toString().trim().equals("")) {
                coloaderDetails[1] = obj[1].toString();
            }
        }
        return coloaderDetails;
    }

    public String[] getManagerOfCFSWarse(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] cfsDetails = new String[2];
//        sb.append("SELECT ud.email,ud.fax FROM lcl_unit_ss lus JOIN lcl_unit_ss_imports lusi ON lus.unit_id= lusi.unit_id ");
//        sb.append("JOIN warehouse war ON war.id =lusi.cfs_warehouse_id LEFT JOIN user_details ud ON war.manager= ud.user_id WHERE  lus.id=").append(unitSsId);
        sb.append("SELECT ");
        sb.append("  war.`cfs_devanning_email`,");
        sb.append("  war.`fax`");
        sb.append("FROM");
        sb.append("  lcl_unit_ss lus ");
        sb.append("  JOIN lcl_unit_ss_imports lusi ");
        sb.append("    ON lus.unit_id = lusi.unit_id ");
        sb.append("  JOIN warehouse war ");
        sb.append("    ON war.id = lusi.cfs_warehouse_id ");
        sb.append("WHERE lus.id =?");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setParameter(0, unitSsId);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            Object[] obj = (Object[]) list.get(0);
            if (obj[0] != null && !obj[0].toString().trim().equals("")) {
                cfsDetails[0] = obj[0].toString();
            }
            if (obj[1] != null && !obj[1].toString().trim().equals("")) {
                cfsDetails[1] = obj[1].toString();
            }
        }
        return cfsDetails;
    }

    public String[] getEmail1OfHouseConsignee(String fileId, String type) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] consDetails = new String[2];
        if (type.equals("cons") || type.equals("noty")) {
            sb.append("SELECT ca.email1,ca.fax FROM lcl_booking lb JOIN cust_address ca ON lb.").append(type).append("_acct_no=ca.acct_no WHERE lb.file_number_id= ");
        }
        sb.append(fileId).append(" LIMIT 1 ");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        List consList = query.list();
        if (consList != null && !consList.isEmpty()) {
            Object[] consObj = (Object[]) consList.get(0);
            if (consObj[0] != null && !consObj[0].toString().trim().equals("")) {
                consDetails[0] = consObj[0].toString();
            }
            if (consObj[1] != null && !consObj[1].toString().trim().equals("")) {
                consDetails[1] = consObj[1].toString();
            }
        }
        return consDetails;
    }

    public String[] getEmail1OfNotify2(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] notify2Details = new String[2];
        sb.append("SELECT ca.email1,ca.fax FROM lcl_booking lb JOIN lcl_contact lc ON lb.noty2_contact_id=lc.id ");
        sb.append("JOIN cust_address ca ON ca.acct_no=lc.tp_acct_no ");
        sb.append("WHERE lb.file_number_id= ");
        sb.append(fileId).append(" LIMIT 1 ");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            Object[] obj = (Object[]) list.get(0);
            if (obj[0] != null && !obj[0].toString().trim().equals("")) {
                notify2Details[0] = obj[0].toString();
            }
            if (obj[1] != null && !obj[1].toString().trim().equals("")) {
                notify2Details[1] = obj[1].toString();
            }
        }
        return notify2Details;
    }
    
    public String getimpCfsId(Long unitId, Long ssHeaderId) throws Exception {
        String query = "SELECT cfs_warehouse_id AS impCfsId FROM `lcl_unit_ss_imports` WHERE unit_id=:unitId AND ss_header_id=:ssHeaderId limit 1";
        SQLQuery queryObj = getCurrentSession().createSQLQuery(query);
        queryObj.setParameter("unitId", unitId);
        queryObj.setParameter("ssHeaderId", ssHeaderId);
        queryObj.addScalar("impCfsId", StringType.INSTANCE);
        return (String) queryObj.uniqueResult();
    }
}
