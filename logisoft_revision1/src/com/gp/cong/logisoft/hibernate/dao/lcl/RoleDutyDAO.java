/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.RoleDuty;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Owner
 */
public class RoleDutyDAO extends BaseHibernateDAO<RoleDuty> {

    public RoleDutyDAO() {
        super(RoleDuty.class);
    }

    public List<RoleDuty> getAllRoleName(String roleName) throws Exception {
        Criteria criteria = getSession().createCriteria(RoleDuty.class, "roleDuty");
        criteria.add(Restrictions.eq("roleName", roleName));
        return criteria.list();
    }

    public RoleDuty getRoleDetails(Integer roleId) throws Exception {
        Criteria criteria = getSession().createCriteria(RoleDuty.class, "roleDuty");
        criteria.add(Restrictions.eq("roleId", roleId));
        return (RoleDuty) criteria.uniqueResult();
    }

    public Boolean getRoleDetails(String columnName, Integer roleId) throws Exception {
        String queryString = "SELECT " + columnName + " FROM role_duties where role_id=" + roleId;
        return (Boolean) getSession().createSQLQuery(queryString).uniqueResult();
    }

    public Boolean getRoleDetails(String columnName, String roleId) throws Exception {
        String queryString = "SELECT " + columnName + " FROM role_duties where role_id=" + roleId;
        return (Boolean) getSession().createSQLQuery(queryString).uniqueResult();
    }
}
