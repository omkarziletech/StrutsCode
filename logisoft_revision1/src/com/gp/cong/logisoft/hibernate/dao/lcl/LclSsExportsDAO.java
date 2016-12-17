/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclSsExports;
import java.util.List;
import org.hibernate.SQLQuery;

/**
 *
 * @author aravindhan.v
 */
public class LclSsExportsDAO extends BaseHibernateDAO<LclSsExports> {

    public LclSsExportsDAO() {
        super(LclSsExports.class);
    }

    public String[] getAgentAcctNo(Long ssHeaderId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        String[] agentValues = new String[2];
        queryStr.append("SELECT export_agent_acct_no AS agentNo ,");
        queryStr.append(" (select acct_name from trading_partner where acct_no=export_agent_acct_no) as agentName ");
        queryStr.append(" FROM lcl_ss_exports WHERE ss_header_id=:ssHeaderId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("ssHeaderId", ssHeaderId);
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            Object[] object = (Object[]) list.get(0);
            if (object[0] != null) {
                agentValues[0] = object[0].toString();
            } else {
                agentValues[0] = "";
            }
            if (object[1] != null) {
                agentValues[1] = object[1].toString();
            } else {
                agentValues[1] = "";
            }
        }
        return agentValues;
    }
}
