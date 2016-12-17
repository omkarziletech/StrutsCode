package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.TerminalBean;
import com.logiware.accounting.model.TerminalModel;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author NambuRajasekar
 */
public class TerminalManagerDao extends BaseHibernateDAO<TerminalManager> {

    public TerminalManagerDao() {
        super(TerminalManager.class);
    }

    public List<TerminalBean> getTerminalManagers(String trmNo) throws Exception {
        String queryString = "SELECT t.id as terminalManagerId, t.user_id AS userId ,ud.login_name AS userName,CONCAT(ud.first_name,' ',ud.last_name) AS managerName, "
                + " ud.email AS managerEmail FROM terminal_manager t  JOIN user_details ud ON ud.user_id = t.user_id WHERE t.trmnum=:trmNo ";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setResultTransformer(Transformers.aliasToBean(TerminalBean.class));
        query.addScalar("terminalManagerId", IntegerType.INSTANCE);
        query.addScalar("userId", IntegerType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("managerName", StringType.INSTANCE);
        query.addScalar("managerEmail", StringType.INSTANCE);
        query.setString("trmNo", trmNo);
        return query.list();
    }

    public List<TerminalModel> getTerminalManagers() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat(u.first_name,' ',u.last_name) AS managerName, u.email AS managerEmail, ");
        queryBuilder.append("GROUP_CONCAT(t.trmnum) AS terminalNumber FROM  user_details u JOIN terminal_manager t ");
        queryBuilder.append("ON t.user_id = u.user_id  GROUP BY u.user_id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("managerName", StringType.INSTANCE);
        query.addScalar("managerEmail", StringType.INSTANCE);
        query.addScalar("terminalNumber", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(TerminalModel.class));
        return query.list();
    }

    public List<TerminalModel> getTrmManagersForDisputeJob() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select (select concat(ud.first_name,' ',ud.last_name) from user_details ud where ud.login_name='KLENGELER') AS managerName, ");
        queryBuilder.append("(select ud.email from user_details ud where ud.login_name='KLENGELER') AS managerEmail, ");
        queryBuilder.append(" '' AS terminalNumber union ");
        queryBuilder.append("select concat(u.first_name,' ',u.last_name) as managerName, ");
        queryBuilder.append("u.email AS managerEmail,");
        queryBuilder.append("  group_concat(`trmnum`) as terminalNumber ");
        queryBuilder.append("from user_details u ");
        queryBuilder.append("JOIN terminal_manager t ON t.user_id = u.user_id  GROUP BY u.user_id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("managerName", StringType.INSTANCE);
        query.addScalar("managerEmail", StringType.INSTANCE);
        query.addScalar("terminalNumber", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(TerminalModel.class));
        return query.list();
    }

}
