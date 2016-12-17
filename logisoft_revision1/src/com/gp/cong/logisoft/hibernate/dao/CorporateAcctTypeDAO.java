/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Mei
 */
public class CorporateAcctTypeDAO extends BaseHibernateDAO {

    public CorporateAccountType findById(Long id) {
        getCurrentSession().flush();
        return (CorporateAccountType) getCurrentSession().get(CorporateAccountType.class, id);
    }

    public void save(CorporateAccountType transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public void update(CorporateAccountType transientInstance) throws Exception {
        getCurrentSession().update(transientInstance);
    }

    public void delete(CorporateAccount transientInstance) throws Exception {
        getSession().delete(transientInstance);
        getSession().flush();
    }

    public List<CorporateAccountType> getCorporateAcctType(String description) {
        String queryStr = " from CorporateAccountType ";
        if (CommonUtils.isNotEmpty(description)) {
            queryStr += " where acctTypeDescription like:description";
        }
        Query query = getCurrentSession().createQuery(queryStr);
        if (CommonUtils.isNotEmpty(description)) {
            query.setString("description", description + '%');
        }
        return query.list();
    }

    public List<LabelValueBean> getCorporateAcctType() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select description,");
        queryBuilder.append("id ");
        queryBuilder.append(" from corporate_account_type");
        queryBuilder.append(" where disabled =0");
        queryBuilder.append(" order by description asc ");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        List<LabelValueBean> description = new ArrayList<LabelValueBean>();
        description.add(new LabelValueBean("Select One", ""));
        for (Object row : result) {
            Object[] col = (Object[]) row;
            description.add(new LabelValueBean(col[0].toString(), col[1].toString()));
        }
        return description;
    }

    public boolean isValidateDescription(String description) throws Exception {
        String queryStr = "select if(count(*)>0, true,false) as result from corporate_account_type where description=:description";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setString("description", description);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }
}