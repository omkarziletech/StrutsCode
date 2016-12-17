/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author lakshh
 */
public class LclInbondsDAO extends BaseHibernateDAO<LclInbond> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public LclInbondsDAO() {
        super(LclInbond.class);
    }

    public List<LclInbond> getInbondList(List<Long> fileId) throws Exception {
        String queryString = "from LclInbond where lclFileNumber.id in(:fileId)";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileId", fileId);
        List list = query.list();
        return list;
    }

    public Boolean isInbondExists(String fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*)>0,TRUE,FALSE) AS result FROM lcl_inbond ");
        queryStr.append(" WHERE file_number_id=:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setString("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void inbondDetails(Long fileId, HttpServletRequest request) throws Exception {
        String inbondNumber = "";
        LclInbondsDAO lclInbondsDAO = new LclInbondsDAO();
        List<LclInbond> lclInbondList = lclInbondsDAO.findByProperty("lclFileNumber.id", fileId);
        if (!lclInbondList.isEmpty()) {
            for (LclInbond li : lclInbondList) {
                String inbondDatetime = "";
                String inbondPort = "";
                if (li.getInbondDatetime() != null) {
                    inbondDatetime = DateUtils.formatStringDateToAppFormatMMM(li.getInbondDatetime());
                }
                if (li.getInbondPort() != null && li.getInbondPort().getUnLocationName() != null) {
                    inbondPort = li.getInbondPort().getUnLocationName();
                }
                inbondNumber += li.getInbondType() + " " + li.getInbondNo() + " " + inbondPort + " " + inbondDatetime + "<br/>";
            }
            request.setAttribute("inbondNumber", inbondNumber.toUpperCase());
            request.setAttribute("inbondListSize", lclInbondList.size());
            request.setAttribute("inbondList", lclInbondList);
            LclInbond lclInbond = lclInbondList.get(0);
            String inbondDatetime = "";
            if (lclInbond != null && lclInbond.getInbondDatetime() != null) {
                inbondDatetime = DateUtils.formatStringDateToAppFormatMMM(lclInbond.getInbondDatetime());
            }
            request.setAttribute("lclInbond", lclInbond);
            request.setAttribute("inbondDatetime", inbondDatetime);
        }
    }

    public String getInbondType(String consList) throws Exception {
        String query = "SELECT GROUP_CONCAT(CONCAT(bn.inbond_type,bn.inbond_no))AS inbond FROM lcl_inbond bn JOIN lcl_file_number fn ON fn.id = bn.file_number_id WHERE fn.id IN(" + consList + ")";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getInbond(List fileId) throws Exception {
        String queryStr = "SELECT GROUP_CONCAT(DISTINCT inbond_no) AS inbondNo FROM lcl_inbond where file_number_id IN(:fileIds)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameterList("fileIds", fileId);
        String inbondNo = (String) query.uniqueResult();
        return null != inbondNo ? inbondNo : "";
    }
}
