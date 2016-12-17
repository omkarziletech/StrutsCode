package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;

import com.gp.cong.logisoft.domain.Item;

public class RoleItemAssociationDAO extends BaseHibernateDAO {

    public List findById(Item itemId, Integer roleId)throws Exception{
            String queryString = "from RoleItemAssociation where itemId=?0 and roleId=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", itemId);
            queryObject.setParameter("1", roleId);
            //System.out.println("queryObject.list"+queryObject.list());
            return queryObject.list();
    }
    public boolean checkItemForRole(Integer roleId,Integer itemId) throws Exception{
            String queryString = "select count(*) from role_item_assoc where role_id="+roleId+" and item_id="+itemId;
            Object result = getCurrentSession().createSQLQuery(queryString).uniqueResult();
            if(null!=result && Integer.parseInt(result.toString())>0){
                return true;
            }
            return false;
    }
    public boolean modifyItemForRole(Integer roleId,Integer itemId) throws Exception{
            String queryString = "select modify from role_item_assoc where role_id="+roleId+" and item_id="+itemId+" limit 1";
            Object result = getCurrentSession().createSQLQuery(queryString).uniqueResult();
            if(null!=result && "modify".equalsIgnoreCase(result.toString())){
                return true;
            }
            return false;
    }
}
