/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Mei
 */
public class CorporateAccountDAO extends BaseHibernateDAO {

    public CorporateAccount findById(Long id) throws Exception {
        getCurrentSession().flush();
        return (CorporateAccount) getCurrentSession().get(CorporateAccount.class, id);
    }

    public void saveOrUpdate(CorporateAccount transientInstance) throws Exception {
        getCurrentSession().saveOrUpdate(transientInstance);
    }

    public void update(CorporateAccount transientInstance) throws Exception {
        getCurrentSession().update(transientInstance);
    }

    public void delete(CorporateAccount transientInstance) throws Exception {
        getSession().delete(transientInstance);
        getSession().flush();
    }

    public List<CorporateAccount> getCorporateAcctType(String corporateName, String commodityCode) throws Exception {
        String queryStr = " from CorporateAccount ";
        if (CommonUtils.isNotEmpty(corporateName) || CommonUtils.isNotEmpty(commodityCode)) {
            queryStr += " where ";
        }
        if (CommonUtils.isNotEmpty(corporateName)) {
            queryStr += " corporateName=:name";
        }
        if (CommonUtils.isNotEmpty(corporateName) && CommonUtils.isNotEmpty(commodityCode)) {
            queryStr += " and ";
        }
        if (CommonUtils.isNotEmpty(commodityCode)) {
            queryStr += " eliteCommodityCode=:commodityCode";
        }
        Query query = getCurrentSession().createQuery(queryStr);
        if (CommonUtils.isNotEmpty(corporateName)) {
            query.setString("name", corporateName.toUpperCase());
        }
        if (CommonUtils.isNotEmpty(commodityCode)) {
            query.setString("commodityCode", commodityCode);
        }
        return query.list();
    }

    public boolean isValidateAcctName(String acctName) throws Exception {
        String queryStr = "select if(count(*)>0, true,false) as result from corporate_account where name=:acctName";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setString("acctName", acctName);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }
}
