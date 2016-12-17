/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public class UnitTypeDAO extends BaseHibernateDAO<UnitType> {

    public UnitTypeDAO() {
        super(UnitType.class);
    }
    //this method are using in importAddUnits.jsp,importAddDetailsPopup.jsp and lclAddUnits.jsp

    public List getAllUnittypesForDisplay(String impFlag, String expFlag) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select id,description from UnitType ");
        if ("1".equalsIgnoreCase(impFlag)) {
            sb.append("where enabledLclImports=").append(impFlag);
        } else {
            sb.append("where enabledLclExports=").append(expFlag);
        }
        sb.append(" order by description");
        Query queryObject = getSession().createQuery(sb.toString());
        List codeList = new ArrayList();
        for (Object codes : queryObject.list()) {
            Object[] code = (Object[]) codes;
            codeList.add(new LabelValueBean(code[1].toString(), code[0].toString()));
        }
        return codeList;
    }

     public List<LabelValueBean> getUnassignedUnit(String id) throws Exception {
        List<LabelValueBean> unassignedUnits = new ArrayList<LabelValueBean>();
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT  UCASE(lu.unit_no) AS unitno, CONCAT(UCASE(lu.unit_no),',',IF(ut.short_desc<>'',ut.short_desc,SUBSTRING(ut.description,1,4)),',',IF(lu.`comments`<>'',lu.comments,'') )AS units FROM lcl_unit lu JOIN lcl_unit_whse lw ON lu.`id` = lw.`unit_id`  ");
        queryStr.append(" JOIN unit_type ut ON ut.`id`=lu.`unit_type_id` WHERE lw.`ss_header_id` IS NULL AND lw.id= (SELECT id FROM lcl_unit_whse   ");
        queryStr.append("  lw WHERE lu.`id` = lw.`unit_id` ORDER BY lw.id DESC LIMIT 1) AND lw.`warehouse_id` =:warehsId ");
        queryStr.append("  AND lw.`ss_header_id` IS  NULL  AND lu.unit_no <> ''  ");
        Query query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("warehsId", id);
        List list = query.list();
        unassignedUnits.add(new LabelValueBean("Select", ""));
        if (!list.isEmpty() && null != list) {
            for (Object row : list) {
                Object[] obj = (Object[]) row;
                unassignedUnits.add(new LabelValueBean((String) obj[1], (String) obj[0]));
            }
        }
        return unassignedUnits;
    }

    public List<UnitType> getAllUnitTypeList(String unitId) throws Exception {
        Criteria criteria = getSession().createCriteria(UnitType.class);
        criteria.addOrder(Order.desc("id"));
        return (List<UnitType>) criteria.list();
    }

    public UnitType getUnitTypeByDesc(String desc) throws Exception {
        return (UnitType) getSession().getNamedQuery("UnitType.findByDescription").setString("description", desc).uniqueResult();
    }
}
