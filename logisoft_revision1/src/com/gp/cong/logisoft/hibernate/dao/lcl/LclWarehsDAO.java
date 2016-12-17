/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.Warehs;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;

/**
 *
 * @author Administrator
 */
public class LclWarehsDAO extends BaseHibernateDAO<Warehs> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public LclWarehsDAO() {
        super(Warehs.class);
    }

    public LclWarehsDAO(String databaseSchema) {
    }

    public Object[] getROZipCode(String unlocCode, String warehouse) throws Exception {
        Object warehs[] = null;
        String queryString = "SELECT w.warehsno,w.warehsname, w.address,w.city,w.state,w.zipcode,w.phone,w.fax FROM  terminal t,warehouse w WHERE t.unLocationCode1 = '"
                + unlocCode + "' AND CONCAT('W',t.trmnum) = w.warehsno  AND t.actyon = 'Y'";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        List<Warehs[]> list = queryObject.list();
        if (list != null && !list.isEmpty()) {
            warehs = (Object[]) list.get(0);
        }
        return warehs;
    }

    public Object[] LclDeliverCargo(String unlocCode, String warehouse) throws Exception {
        Object warehs[] = null;
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT w.warehsno,w.warehsname, w.address,w.city, ");
        queryStr.append(" w.state,w.zipcode,w.phone,w.fax  ");
        queryStr.append(" FROM  warehouse w WHERE ");
        queryStr.append(" w.warehsno= CONCAT(:warehouse,(SELECT trmnum FROM terminal WHERE unLocationCode1=:unlocCode LIMIT 1)) ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setString("warehouse", warehouse);
        queryObject.setString("unlocCode", unlocCode);
        List<Warehs[]> list = queryObject.list();
        if (list != null && !list.isEmpty()) {
            warehs = (Object[]) list.get(0);
        }
        return warehs;
    }

    public String getWarehouseNo(String headerId) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT wh.warehsno FROM lcl_unit_whse luw");
        stringBuilder.append(" JOIN warehouse wh ON wh.id=luw.warehouse_id ").append("where luw.ss_header_id=").append(headerId);
        return (String) getSession().createSQLQuery(stringBuilder.toString()).uniqueResult();

    }
}
