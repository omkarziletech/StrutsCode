/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHotCode;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author administrator
 */
public class LclBookingHotCodeDAO extends BaseHibernateDAO<LclBookingHotCode> {

    private String dataBaseName;

    public LclBookingHotCodeDAO() throws Exception {
        super(LclBookingHotCode.class);
        dataBaseName = LoadLogisoftProperties.getProperty("elite.database.name");
    }

    public boolean isHotCodeNotExist(String Code, String fileId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select if(count(*)<1,true,false) as result from lcl_booking_hot_code "
                + " where code=:code and file_number_id=:fileId");
        query.setParameter("fileId", fileId);
        query.setParameter("code", Code);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void insertQuery(String fileId, String code, Integer user) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into lcl_booking_hot_code(file_number_id,code,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) ");
        sb.append(" values(:fileId,:code,:now,:user,:now,:user)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("code", code);
        query.setParameter("now", new Date());
        query.setParameter("user", user);
        query.executeUpdate();
    }

    public List<LclBookingHotCode> getHotCodeList(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery(" from LclBookingHotCode where lclFileNumber.id=" + fileId);
        return query.list();
    }

    public boolean isHotCodeExistForThreeDigit(String fileId, String type, String Code) throws Exception { // type is not but exists method will affected
        SQLQuery query = getCurrentSession().createSQLQuery("select if(count(*)<1,true,false) as result from lcl_booking_hot_code "
                + " where SUBSTRING_INDEX(code,'/',1)=:code and file_number_id=:fileId");
        query.setParameter("fileId", fileId);
        query.setParameter("code", Code);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean isHotCodeValidate(Long fileId, String code) throws Exception { // type is not but exists method will affected
        SQLQuery query = getCurrentSession().createSQLQuery("select if(count(*)>0,true,false) as result from lcl_booking_hot_code "
                + " where SUBSTRING_INDEX(code,'/',1)=:code and file_number_id=:fileId");
        query.setParameter("fileId", fileId);
        query.setParameter("code", code);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public LclBookingHotCode getHotCodeByFileIDCode(Long fileId, String code) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingHotCode.class, "lclHotCode");
        criteria.createAlias("lclHotCode.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(code)) {
            criteria.add(Restrictions.eq("code", code));
        }
        return (LclBookingHotCode) criteria.setMaxResults(1).uniqueResult();
    }

    public void saveHotCode(Long fileId, String code, Integer user) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into lcl_booking_hot_code(file_number_id,code,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) ");
        sb.append(" values(:fileId,:code,:now,:user,:now,:user)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("code", code);
        query.setParameter("now", new Date());
        query.setParameter("user", user);
        query.executeUpdate();

    }

    public List<Object[]> getHotCodeListToSendEDI(Long unitSsId) throws Exception {
        Query queryObject = getSession().createSQLQuery("SELECT 'HTC',bh.code from lcl_booking_hot_code bh JOIN lcl_booking_piece bp "
                + "ON bh.file_number_id=bp.file_number_id JOIN lcl_booking_piece_unit bpu ON bp.id=bpu.booking_piece_id "
                + "WHERE lcl_unit_ss_id=" + unitSsId + " GROUP BY bh.id");
        return (List<Object[]>) queryObject.list();
    }

    public Boolean isHazmatHotCodeExist(String fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT if((SELECT hazrds FROM ").append(dataBaseName).append(".ldgins WHERE loadcd = SUBSTRING_INDEX(lp.code,'/',1))='y',true,false)  as result  FROM lcl_booking_hot_code lp JOIN genericcode_dup gd ");
        queryStr.append("ON gd.code = SUBSTRING_INDEX(lp.code,'/',1) and ");
        queryStr.append("gd.Codetypeid= (SELECT codetypeid FROM codetype WHERE description='Hot Codes') ");
        queryStr.append("AND gd.Field1='Y' WHERE lp.file_number_id =:fileId limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) (query.uniqueResult() != null ? query.uniqueResult() : Boolean.FALSE);
    }

    public boolean qtHazmatCodeExit(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  IF(");
        sb.append("    (SELECT ");
        sb.append("      hazrds ");
        sb.append("    FROM ");
        sb.append(dataBaseName).append(".ldgins");
        sb.append("    WHERE loadcd = SUBSTRING_INDEX(lq.code, '/', 1)) = 'y',");
        sb.append("    TRUE,");
        sb.append("    FALSE");
        sb.append("  ) AS hazStatus ");
        sb.append(" FROM");
        sb.append("  lcl_quote_hot_code lq ");
        sb.append("  JOIN genericcode_dup gd ");
        sb.append("    ON gd.code = SUBSTRING_INDEX(lq.code, '/', 1) ");
        sb.append("    AND gd.Codetypeid = ");
        sb.append("    (SELECT ");
        sb.append("      codetypeid ");
        sb.append("    FROM");
        sb.append("      codetype ");
        sb.append("    WHERE description = 'Hot Codes') ");
        sb.append("    AND gd.Field1 = 'Y' ");
        sb.append(" WHERE lq.file_number_id = :fileId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("hazStatus", BooleanType.INSTANCE);
        return (Boolean) (queryObject.uniqueResult() != null ? queryObject.uniqueResult() : Boolean.FALSE);
    }

    public LclBookingHotCode getHotCode(Long fileId, String code) throws Exception {
        Query query = getCurrentSession().createQuery(" from LclBookingHotCode where lclFileNumber.id=:fileId and code=:code");
        query.setParameter("fileId", fileId);
        query.setParameter("code", code);
        return (LclBookingHotCode) query.uniqueResult();
    }

    public void deleteHotCodeByFileId(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery(" delete from LclBookingHotCode where lclFileNumber.id=:fileId");
        query.setParameter("fileId", fileId);
        query.executeUpdate();
    }
      public boolean isHotCodeExistsForConsolidateFile(String fileId, String hotCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*)>0,'true','false') as result FROM lcl_booking_hot_code hc  ");
        queryBuilder.append(" JOIN lcl_file_number fn ON fn.id=hc.file_number_id WHERE  ");
        queryBuilder.append(" fn.id IN (SELECT lcl_file_number_id_a FROM `lcl_consolidation` WHERE lcl_file_number_id_b = ");
        queryBuilder.append(" (SELECT lcl_file_number_id_b FROM `lcl_consolidation` WHERE lcl_file_number_id_a =:fileId)) ");
        queryBuilder.append(" AND hc.`code` =:hotCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("hotCode", hotCode);
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }
}
