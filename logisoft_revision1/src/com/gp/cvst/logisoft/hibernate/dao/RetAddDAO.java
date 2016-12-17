/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.RetAdd;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.struts.form.RetAddSearchForm;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Vinay
 */
public class RetAddDAO extends BaseHibernateDAO {

    public RetAddDAO() {
    }

    public List search(RetAddSearchForm retAddSearchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT trm.terminal_location,pr.portname,");
        queryBuilder.append(" rd.usrnam,rd.code,rd.email,rd.dprtmn,rd.lognid, ");
        queryBuilder.append(" rd.blterm,rd.destin,rd.id  ");
        queryBuilder.append(" FROM retadd rd LEFT JOIN ports pr ON rd.destin = pr.govschnum ");
        queryBuilder.append(" JOIN terminal trm ON trm.trmnum = rd.blterm  ");
        queryBuilder.append(" WHERE rd.blterm <> '' ");
        if (CommonUtils.isNotEmpty(retAddSearchForm.getOrigTerm())) {
            queryBuilder.append(" AND rd.blterm='").append(retAddSearchForm.getOrigTerm()).append("'");
        }
        if (CommonUtils.isNotEmpty(retAddSearchForm.getDestination())) {
            queryBuilder.append(" AND rd.destin='").append(retAddSearchForm.getDestination()).append("'");
        }
        if (CommonUtils.isNotEmpty(retAddSearchForm.getUserName())) {
            queryBuilder.append(" AND rd.usrnam='").append(retAddSearchForm.getUserName()).append("'");
        }
        ArrayList<RetAdd> results = new ArrayList<RetAdd>();
        List pseudoRes = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : pseudoRes) {
            RetAdd r = setRetAdd((Object[]) row);
            results.add(r);
        }
        return results;
    }

    public RetAdd getRetAdd(Integer retAddId) {
        String query = "from RetAdd where id=:retAddId";
        Query queryObj = getCurrentSession().createQuery(query.toString());
        queryObj.setParameter("retAddId", retAddId);
        return (RetAdd) queryObj.setMaxResults(1).uniqueResult();
    }

    public RetAdd setRetAdd(Object[] col) {
        RetAdd r = new RetAdd();
        r.setOriginTerminal((String) col[0]);
        r.setDestinationPort(col[8].toString().equals("00000") ? "All Ports" : (String) col[1]);
        r.setUserName((String) col[2]);
        r.setCode((String) col[3]);
        r.setEmail((String) col[4]);
        r.setDept((String) col[5]);
        r.setLoginID((String) col[6]);
        r.setOriginId((String) col[7]);
        r.setRetAddId((Integer) col[9]);
        return r;
    }

    public void updateRetAdd(RetAddSearchForm retAddSearchForm) {
        RetAdd rad = this.getRetAdd(retAddSearchForm.getRetAddId());
        rad.setEmail(retAddSearchForm.getEmail().toLowerCase());
        getCurrentSession().saveOrUpdate(rad);
        getCurrentSession().flush();
    }

    public String getRetAddEmail(String trmnum, String schnum) {
        String loctnQuery = "SELECT email FROM retadd WHERE blterm = '" + trmnum + "'  AND CODE = 'F' LIMIT 1";
        Object result = getCurrentSession().createSQLQuery(loctnQuery).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public String getLCLRetAddEmail(String trmnum, String schnum) {
        String loctnQuery = "SELECT email FROM retadd WHERE blterm = '" + trmnum + "'  AND CODE = 'L' LIMIT 1";
        Object result = getCurrentSession().createSQLQuery(loctnQuery).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public String getLCLDefaultRetAddEmail() {
        String loctnQuery = "SELECT email FROM retadd WHERE blterm = '" + "01" + "'  AND CODE = 'L' LIMIT 1";
        Object result = getCurrentSession().createSQLQuery(loctnQuery).uniqueResult();
        return null != result ? result.toString() : "";
    }
//    private final String cols = "SELECT trm.terminal_location, pr.countryname, rd.usrnam, "
//            + "rd.code, rd.email, rd.dprtmn, rd.lognid, rd.blterm, rd.destin";
//    private final String tables = " FROM retadd rd left join ports pr on rd.destin=pr.govschnum, terminal trm";
//    private final String defaultReqs = " WHERE trm.trmnum=rd.blterm";
//    private final String organize = " GROUP BY rd.blterm,rd.destin, rd.code";
//    private RetAddSearchForm rasf;
}
