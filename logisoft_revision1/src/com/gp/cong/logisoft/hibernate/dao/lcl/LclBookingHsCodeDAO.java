/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingHsCode;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Thamizh
 */
public class LclBookingHsCodeDAO extends BaseHibernateDAO<LclBookingHsCode> {

    public LclBookingHsCodeDAO() {
        super(LclBookingHsCode.class);
    }

    public List<LclBookingHsCode> getHsCodeList(List<Long> fileId) throws Exception {
        String queryString = "from LclBookingHsCode where lclFileNumber.id in(:fileId)";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileId", fileId);
        List list = query.list();
        return list;
    }

    public List<LclBookingHsCode> getHsCodeList(Long fileId) throws Exception {
        String queryString = "from LclBookingHsCode where lclFileNumber.id= " + fileId;
        return (List<LclBookingHsCode>) getSession().createQuery(queryString).list();
    }

    public void deleteBkgHsCodeByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingHsCode where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public String isCheckedBkgHsCode(Long fileId) throws Exception {
        String query = "SELECT IF(COUNT(*)>0,'true','false') FROM lcl_booking_hs_code WHERE file_number_id=" + fileId;
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }
    public String isHsCodeAvailable(Long fileId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("SELECT IF(COUNT(*)>0,'true','false') FROM lcl_booking_hs_code WHERE file_number_id in");
        query.append("(select lcl_file_number_id_a from lcl_consolidation where lcl_file_number_id_b =  ");
        query.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a =");
        query.append(fileId).append(") )");
        return (String) getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
    }

    public List<LclBookingHsCode> getHsCodeByList(Long fileId) throws Exception {
        String queryString = "from LclBookingHsCode where lclFileNumber.id= " + fileId + " and (noPieces is null or weightMetric is null)";
        return (List<LclBookingHsCode>) getSession().createQuery(queryString).list();
    }

    public String hsCodeAlreadyExist(String fileNumberId, String hsCode, String bookingHsCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') from lcl_booking_hs_code ");
        queryBuilder.append(" where file_number_id = ").append(Long.parseLong(fileNumberId)).append(" ");
        queryBuilder.append(" and (codes = '").append(hsCode).append("' or codes = '").append(bookingHsCode).append("')");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getHsCode(String consList) throws Exception {
        String query = "SELECT GROUP_CONCAT(hs.codes) AS codes FROM lcl_booking_hs_code hs  LEFT JOIN lcl_file_number fn ON fn.id = hs.file_number_id WHERE fn.id IN(" + consList + ")";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getHsCode(List fileId) throws Exception {
        String queryStr = "SELECT GROUP_CONCAT(DISTINCT codes) AS codes FROM lcl_booking_hs_code where file_number_id IN(:fileId)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameterList("fileId", fileId);
        String hsCode = (String) query.uniqueResult();
        return null != hsCode ? hsCode : "";
    }

    public LclBookingHsCode getConsolidateHsCodeByFileId(Long fileId, String codes) throws Exception {
        String queryString = "from LclBookingHsCode where lclFileNumber.id =" + fileId + " AND codes='" + codes + "' ";
        return (LclBookingHsCode) getSession().createQuery(queryString).uniqueResult();
    }

    public LclBookingHsCode getConsolidateHsCodeByFileNumberId(Long fileId) throws Exception {
        String queryString = "from LclBookingHsCode where lclFileNumber.id =" + fileId;
        return (LclBookingHsCode) getSession().createQuery(queryString).uniqueResult();
    }

    public LclFileNumber getLclFileNumber(Long fileNoId) throws Exception {
        String query = "from LclFileNumber where id = '" + fileNoId + "'";
        return (LclFileNumber) getSession().createQuery(query).uniqueResult();
    }

    public boolean isHsCodeExistsForConsolidateFile(String fileId, String hsCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*)>0,'true','false') as result FROM lcl_booking_hs_code hc  ");
        queryBuilder.append(" JOIN lcl_file_number fn ON fn.id=hc.file_number_id WHERE  ");
        queryBuilder.append(" fn.id IN (SELECT lcl_file_number_id_a FROM `lcl_consolidation` WHERE lcl_file_number_id_b = ");
        queryBuilder.append(" (SELECT lcl_file_number_id_b FROM `lcl_consolidation` WHERE lcl_file_number_id_a =:fileId)) ");
        queryBuilder.append(" AND hc.`codes` =:hsCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("hsCode", hsCode);
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }
}
