package com.logiware.tags;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.common.dao.PropertyDAO;
import com.logiware.common.model.OptionModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Balaji.E(Logiware)
 */
public class DAO {

    public static java.lang.String getUniqueResult(java.lang.String query) throws Exception {
        BaseHibernateDAO dao = new BaseHibernateDAO();
        dao.getCurrentSession().flush();
        SQLQuery q = dao.getCurrentSession().createSQLQuery(query);
        java.lang.String r = (java.lang.String)q.uniqueResult();
        return r;
    }

    public static java.lang.String getProperty(java.lang.String property) throws Exception {
        return (java.lang.String) new PropertyDAO().getProperty(property);
    }

    public static java.util.Map getMap(java.lang.String queryString) throws Exception {
        BaseHibernateDAO dao = new BaseHibernateDAO();
        dao.getCurrentSession().flush();
        SQLQuery query = dao.getCurrentSession().createSQLQuery(queryString);
        query.addScalar("keyName", StringType.INSTANCE);
        query.addScalar("valueName", StringType.INSTANCE);
        List<Object> result = query.list();
        Map<java.lang.String, java.lang.String> map = new HashMap<java.lang.String, java.lang.String>();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            map.put((java.lang.String) col[0], (java.lang.String) col[1]);
        }
        return map;
    }

    public static java.util.List getList(java.lang.String queryString) throws Exception {
        BaseHibernateDAO dao = new BaseHibernateDAO();
        dao.getCurrentSession().flush();
        SQLQuery query = dao.getCurrentSession().createSQLQuery(queryString);
        query.addScalar("label", StringType.INSTANCE);
        query.addScalar("value", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(OptionModel.class));
        return query.list();    
    }
}
