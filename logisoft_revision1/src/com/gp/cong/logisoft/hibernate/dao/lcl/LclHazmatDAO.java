/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Administrator
 */
public class LclHazmatDAO extends BaseHibernateDAO<LclBookingHazmat> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);
    private String dataBaseName;

    public LclHazmatDAO() throws Exception {
        super(LclBookingHazmat.class);
        dataBaseName = LoadLogisoftProperties.getProperty("elite.database.name");
    }

    public void deleteHazmat(Long fileId, Long bkgPieceId) throws Exception {
        String queryStr = "delete from LclBookingHazmat where lclFileNumber.id=:fileId and lclBookingPiece.id=:bkgPieceId";
        Query query = getCurrentSession().createQuery(queryStr);
        query.setParameter("fileId", fileId);
        query.setParameter("bkgPieceId", bkgPieceId);
        query.executeUpdate();
    }

    public LclBookingHazmat findByFileAndCommodity(Long fileNumberId, Long bookingPieceId) throws Exception {
        String queryString = "from LclBookingHazmat where lclFileNumber='" + fileNumberId + "' and lclBookingPiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        return (LclBookingHazmat) query.setMaxResults(1).uniqueResult();
    }

    public List<LclBookingHazmat> findByFileAndCommodityList(Long fileNumberId, Long bookingPieceId) throws Exception {
        String queryString = "from LclBookingHazmat where lclFileNumber='" + fileNumberId + "' and lclBookingPiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<LclBookingHazmat> findByFileId(List fileNumberId) throws Exception {
        String queryString = "from LclBookingHazmat where lclFileNumber.id in(:fileNumberId)";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileNumberId", fileNumberId);
        List list = query.list();
        return list;
    }

    public int deleteBookingHazmatByFileId(Long id) throws Exception {
        int deletedRows = 0;
        String queryString = "delete from lcl_booking_hazmat where id=?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", id);
        deletedRows = queryObject.executeUpdate();
        return deletedRows;
    }

    public List<PackageType> getAllPackages() throws Exception {
        String queryString = "from PackageType";
        return getSession().createQuery(queryString).list();
    }

    public List getAllPkgComposition() throws Exception {
        List pkglist = new ArrayList();
        String queryString = "SELECT csname,short  FROM " + dataBaseName + ".compos";
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object pkg : queryObject.list()) {
            Object[] p = (Object[]) pkg;
            pkglist.add(new LabelValueBean(p[0].toString(), p[1].toString()));
        }
        return pkglist;
    }

    public List getOuterPkgType() throws Exception {
        List pkgType = new ArrayList();
        String queryString = "SELECT kupper,pkform  FROM " + dataBaseName + ".packtp tp where tp.usedsc = 'Y'";
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object pkg : queryObject.list()) {
            Object[] p = (Object[]) pkg;
            pkgType.add(new LabelValueBean(p[0].toString(), p[1].toString()));
        }
        return pkgType;
    }

    public List getInnerPkgType() throws Exception {
        List pkgType = new ArrayList();
        String queryString = "SELECT kupper,pkform  FROM " + dataBaseName + ".packtp tp";
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object pkg : queryObject.list()) {
            Object[] p = (Object[]) pkg;
            pkgType.add(new LabelValueBean(p[0].toString(), p[1].toString()));
        }
        return pkgType;
    }

    public List<GenericCode> getAllPackageComposition() throws Exception {
        String queryString = "from GenericCode where codetypeid=50 order by codedesc";
        return getSession().createQuery(queryString).list();
    }

    public void deleteBkgHazmetByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingHazmat where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getHotCodeByHazmatClassType(String classType) throws Exception {
        String queryString = "select group_concat(loadcd, '/', descrp) as hotCode from " + dataBaseName + ".ldgins where imoccd = :classType";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("classType", classType);
        query.addScalar("hotCode", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public boolean getHazStatus(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  ");
        sb.append("  IF(COUNT(hazmat) != COUNT(un_hazmat_no),true,false) AS STATUS ");
        sb.append(" FROM ");
        sb.append("  `lcl_booking_piece` lbp  ");
        sb.append("  LEFT JOIN `lcl_booking_hazmat` lbh  ");
        sb.append("    ON ( ");
        sb.append("      lbp.`id` = lbh.`booking_piece_id` ");
        sb.append("    )  ");
        sb.append(" WHERE lbp.`file_number_id` =:fileId AND hazmat >0");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setString("fileId", fileId);
        queryObject.addScalar("STATUS", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }

    public boolean getQtHotCodeStatus(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  IF(");
        sb.append("    COUNT(hazmat) = COUNT(un_hazmat_no),");
        sb.append("    TRUE,");
        sb.append("    FALSE");
        sb.append("  ) AS hazmatStatus ");
        sb.append(" FROM");
        sb.append("  `lcl_quote_piece` lqp ");
        sb.append("  LEFT JOIN `lcl_quote_hazmat` lh ");
        sb.append("    ON (lqp.`id` = lh.`quote_piece_id`) ");
        sb.append(" WHERE lqp.`file_number_id` =:fileId  AND hazmat >0");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("hazmatStatus", BooleanType.INSTANCE);
        return (Boolean) (queryObject.uniqueResult() != null ? queryObject.uniqueResult() : Boolean.FALSE);
    }

    public String isHazmat(String fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT IF(COUNT(*)>0 ,'true','false') FROM lcl_booking_hazmat ");
        queryStr.append(" WHERE booking_piece_id IN(SELECT id FROM lcl_booking_piece ");
        queryStr.append("WHERE file_number_id=:fileId AND hazmat=:hazmet)");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setBoolean("hazmet", true);
        return (String) query.uniqueResult();
    }

    public String getPluralByPkgType(String pkform) throws Exception {
        String queryString = "SELECT tp.plural FROM " + dataBaseName + ".packtp tp where tp.pkform=:pkform";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("pkform", pkform);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }
    
     public List<LclBookingHazmat> findById(List<Long> fileId) throws Exception {
        String queryString = "from LclBookingHazmat where lclFileNumber.id in(:fileNumberId)";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileNumberId", fileId);
        List list = query.list();
        return list;
    }
}
