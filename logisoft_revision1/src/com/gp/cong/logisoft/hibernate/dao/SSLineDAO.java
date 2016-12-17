/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Vinay
 */
public class SSLineDAO extends BaseHibernateDAO {

    public List getSSLineNames(String sslineName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("SELECT fclorg.ssline_no, tp.acct_name");
        queryBuilder.append(" FROM trading_partner tp, fcl_org_dest_misc_data fclorg");
        queryBuilder.append(" WHERE fclorg.ssline_no=tp.acct_no AND tp.acct_name like ?0");
        queryBuilder.append(" GROUP BY fclorg.ssline_no ORDER BY tp.acct_name");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", sslineName + "%");
        return query.list();
    }
}
