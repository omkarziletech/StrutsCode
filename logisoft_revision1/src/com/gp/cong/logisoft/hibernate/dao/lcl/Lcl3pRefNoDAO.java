/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.logiware.common.dao.PropertyDAO;
import java.math.BigInteger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 *
 * @author Administrator
 */
public class Lcl3pRefNoDAO extends BaseHibernateDAO<Lcl3pRefNo> {

    public Lcl3pRefNoDAO() {
        super(Lcl3pRefNo.class);
    }

    public Boolean isValidateAes(Long fileId) throws Exception {
        String queryString = "select if(count(*)>0,true,false) as result from lcl_3p_ref_no where file_number_id=:fileId AND (type='AES_ITNNUMBER' OR type='AES_EXCEPTION')";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.setMaxResults(1).uniqueResult();
    }

    public String isAesAvailable(Long fileId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("select if(count(*)>0,'true','false')  from lcl_3p_ref_no where (type='AES_ITNNUMBER' OR type='AES_EXCEPTION') AND file_number_id in");
        query.append("(select lcl_file_number_id_a from lcl_consolidation where lcl_file_number_id_b =  ");
        query.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a =");
        query.append(fileId).append(") )");
        return (String) getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
    }

    public Lcl3pRefNo getLclHscCodeByType(String fileId, String type) throws Exception {
        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "lcl3pRefNo");
        criteria.createAlias("lcl3pRefNo.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", Long.parseLong(fileId)));
        }
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lcl3pRefNo.type", type));
        }
        return (Lcl3pRefNo) criteria.uniqueResult();
    }

    public List<Lcl3pRefNo> getLclHscCodeListByType(String fileId, String type) throws Exception {
        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "lcl3pRefNo");
        criteria.createAlias("lcl3pRefNo.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", Long.parseLong(fileId)));
        }
        if (!CommonUtils.isEmpty(type)) {
            criteria.add(Restrictions.eq("lcl3pRefNo.type", type));
        }
        return criteria.list();
    }

    public List<Lcl3pRefNo> get3pRefAesList(Long fileId) throws Exception {
        String queryString = "from Lcl3pRefNo where lclFileNumber.id= " + fileId + " AND (type='AES_ITNNUMBER' OR type='AES_EXCEPTION')";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public String get3pWHList(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT GROUP_CONCAT(reference) from lcl_3p_ref_no where file_number_id='").append(fileId).append("'");
        queryBuilder.append("AND TYPE='WH'");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result && !result.isEmpty() ? result : "";
    }

    public String get3pWhseDoc(Long fileId) throws Exception {
        String query = "SELECT reference from lcl_3p_ref_no where file_number_id=" + fileId + " AND TYPE='WH' ORDER BY id DESC LIMIT 1";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public List<Lcl3pRefNo> getLclNcmNoListByType(String fileId, String type) throws Exception {
        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "lcl3pRefNo");
        criteria.createAlias("lcl3pRefNo.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", new Long(fileId)));
        }
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lcl3pRefNo.type", type));
        }
        return criteria.list();
    }

    public List<Object[]> get3pReferenceByUnitSsId(Long unitSsId) throws Exception {
        Query queryObject = getSession().createSQLQuery("SELECT 3p.type, 3p.reference FROM lcl_3p_ref_no 3p JOIN lcl_booking_piece bp ON 3p.file_number_id=bp.file_number_id JOIN lcl_booking_piece_unit bpu ON bp.id=bpu.booking_piece_id WHERE lcl_unit_ss_id=" + unitSsId + " GROUP BY 3p.id");
        return (List<Object[]>) queryObject.list();
    }

    public List<Object[]> getInbondByUnitSsId(Long unitSsId) throws Exception {
        Query queryObject = getSession().createSQLQuery("SELECT inbond.inbond_type,inbond.inbond_no FROM lcl_inbond inbond JOIN lcl_booking_piece bp ON inbond.file_number_id=bp.file_number_id JOIN lcl_booking_piece_unit bpu ON bp.id=bpu.booking_piece_id WHERE lcl_unit_ss_id=" + unitSsId + " GROUP BY inbond.inbond_id");
        return (List<Object[]>) queryObject.list();
    }

    public List<Object[]> getHsCodeByUnitSsId(Long unitSsId) throws Exception {
        Query queryObject = getSession().createSQLQuery("SELECT hs.codes, hs.no_pieces,hs.weight_metric,p.description FROM lcl_booking_hs_code hs LEFT JOIN package_type p ON hs.package_type_id=p.id JOIN lcl_booking_piece bp ON hs.file_number_id=bp.file_number_id JOIN lcl_booking_piece_unit bpu ON bp.id=bpu.booking_piece_id WHERE lcl_unit_ss_id=" + unitSsId + " GROUP BY hs.id");
        return (List<Object[]>) queryObject.list();
    }

    public Lcl3pRefNo getLcl3PRefDetails(Long fileId, String type, String refNo) throws Exception {
        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "lcl3pRefNo");
        criteria.createAlias("lcl3pRefNo.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(type)) {
            criteria.add(Restrictions.eq("type", type));
        }
        if (!CommonUtils.isEmpty(refNo)) {
            criteria.add(Restrictions.eq("reference", refNo));
        }
        return (Lcl3pRefNo) criteria.setMaxResults(1).uniqueResult();
    }

    public void deleteLcl3PRef(Long fileId, String type) throws Exception {
        String queryString = "delete from lcl_3p_ref_no where file_number_id=?0 and type=?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", type);
        queryObject.executeUpdate();
    }

    public List<Lcl3pRefNo> get3PRefList(Long fileId, String type) throws Exception {
        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "lcl3pRefNo");
        criteria.createAlias("lcl3pRefNo.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(type)) {
            criteria.add(Restrictions.eq("lcl3pRefNo.type", type));
        }
        return criteria.list();
    }

    public void delete3pRefNoByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete Lcl3pRefNo where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getReferenceSize(String id, String refno) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT l3r.reference FROM lcl_3p_ref_no l3r WHERE l3r.file_number_id= '").append(id).append("'");
        sb.append("AND l3r.reference='").append(refno).append("'");
        Query query = getSession().createSQLQuery(sb.toString());
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public BigInteger isCheckHazmatCode(Long fileNumberId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT  COUNT(*)  FROM lcl_3p_ref_no lp JOIN genericcode_dup gd ");
        queryStr.append("ON gd.code = SUBSTRING_INDEX(lp.reference,'/',1) AND lp.type='HTC' and ");
        queryStr.append("gd.Codetypeid= (SELECT codetypeid FROM codetype WHERE description='Hot Codes') ");
        queryStr.append("AND gd.Field1='Y' WHERE lp.file_number_id =:fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileNumberId);
        return (BigInteger) query.uniqueResult();
    }

    public String isChecked3PRefByType(String fileId, String type, String referenceValue) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT if(count(*)>0,'true','false') from lcl_3p_ref_no ");
        queryStr.append(" where file_number_id=:fileId ");
        queryStr.append(" and type=:refType ");
        if (CommonUtils.isNotEmpty(referenceValue)) {
            queryStr.append(" and SUBSTRING_INDEX(reference,'/',1)=:refValue ");
        }
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setString("fileId", fileId);
        query.setString("refType", type);
        if (CommonUtils.isNotEmpty(referenceValue)) {
            query.setString("refValue", referenceValue);
        }
        return (String) query.uniqueResult();
    }

    public void save3pRefNo(Long fileId, String type, String reference) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("INSERT INTO lcl_3p_ref_no(file_number_id,TYPE,reference)");
        queryStr.append(" VALUES(:fileId,:type,:reference) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setString("type", type);
        query.setString("reference", reference);
        query.executeUpdate();

    }

    public void delete3PByTypeAndReference(Long fileId, String type, String reference) throws Exception {
        String queryString = "delete from lcl_3p_ref_no where file_number_id=?0 and type=?1 and reference =?2";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", type);
        queryObject.setParameter("2", reference);
        queryObject.executeUpdate();
    }

    public List<Lcl3pRefNo> get3pRefAesList(List<Long> fileId) throws Exception {
        String queryString = "from Lcl3pRefNo where lclFileNumber.id in(:fileId) AND (type='AES_ITNNUMBER' OR type='AES_EXCEPTION')";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileId", fileId);
        List list = query.list();
        return list;
    }

    public List<Lcl3pRefNo> getAesTypeList(List<Long> fileId, String aesType) throws Exception {
        String queryString = "from Lcl3pRefNo where lclFileNumber.id in(:fileId) AND type=:aesType";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileId", fileId);
        query.setString("aesType", aesType);
        List list = query.list();
        return list;
    }

    public String getAesNo(List fileId, String refType) throws Exception {
        String queryStr = "SELECT GROUP_CONCAT(DISTINCT l3.`reference`) AS reference FROM lcl_3p_ref_no l3 where l3.file_number_id IN(:fileIds) AND type=:refType";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameterList("fileIds", fileId);
        query.setParameter("refType", refType);
        String ref = (String) query.uniqueResult();
        return null != ref ? ref : "";
    }

    public String getNcmNo(String consList) throws Exception {
        String query = "SELECT GROUP_CONCAT(l3.`reference`) AS reference FROM lcl_3p_ref_no l3  LEFT JOIN lcl_file_number fn ON fn.id = l3.file_number_id WHERE fn.id IN(" + consList + ") AND TYPE ='NCM'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getAesNo(String consList) throws Exception {
        String query = "SELECT GROUP_CONCAT(l3.`reference`) AS reference FROM lcl_3p_ref_no l3  LEFT JOIN lcl_file_number fn ON fn.id = l3.file_number_id WHERE fn.id IN(" + consList + ") AND (type='AES_ITNNUMBER' OR type='AES_EXCEPTION')";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getCustomerPo(String fileId) throws Exception {
        String query = "SELECT GROUP_CONCAT(reference) FROM  lcl_3p_ref_no WHERE  file_number_id=" + fileId + " AND TYPE='CP'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public List<Lcl3pRefNo> getAesTypeListByFileIdListAndType(List<Long> fileId, String aesType) throws Exception {
        String queryString = "from Lcl3pRefNo where lclFileNumber.id in(:fileId) AND type=:aesType GROUP BY reference";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("fileId", fileId);
        query.setString("aesType", aesType);
        List list = query.list();
        return list;
    }

    public String getCustomerPoForCommodityDesc(String fileId) throws Exception {
        String query = "SELECT GROUP_CONCAT(DISTINCT(reference)) FROM lcl_3p_ref_no WHERE (file_number_id IN(SELECT GROUP_CONCAT(lcl_file_number_id_b) AS fileId   FROM lcl_consolidation WHERE lcl_file_number_id_a =" + fileId + " UNION SELECT lcl_file_number_id_a AS fileId   FROM lcl_consolidation WHERE lcl_file_number_id_b =" + fileId + ") OR file_number_id = " + fileId + ") AND TYPE = 'CP'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public Integer insert3pRefNo(Long fileId, String type, String reference) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("INSERT INTO lcl_3p_ref_no(file_number_id,TYPE,reference)");
        queryStr.append(" VALUES(:fileId,:type,:reference) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setString("type", type);
        query.setString("reference", reference);
        int count = query.executeUpdate();
        return count;
    }

    public Boolean isValidateNOEEI(List fileId) throws Exception {
        String queryString = "select if(count(*)>0,true,false) as result from lcl_3p_ref_no where file_number_id IN (:fileId) AND type='AES_EXCEPTION' AND reference='NOEEI 30.37 (A)   LOW VALUE'";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameterList("fileId", fileId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.setMaxResults(1).uniqueResult();
    }

    public String getAesType(Long fileId) throws Exception {
        String queryString = "select type as result from lcl_3p_ref_no where file_number_id='" + fileId + "' AND (type='AES_ITNNUMBER' OR type='AES_EXCEPTION') order by id desc";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.addScalar("result", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public List<Lcl3pRefNo> getAesExcepList(List<Long> fileId,String aesType) throws Exception {
        String queryString = "from Lcl3pRefNo where lclFileNumber.id in(:fileId) AND type=:aesType";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameterList("fileId", fileId);
        query.setString("aesType", aesType);
        List list = query.list();
        if (CommonUtils.isEmpty(list)) {
            queryString = "from Lcl3pRefNo where lclFileNumber.id in(:fileId) AND type='AES_EXCEPTION'";
            query = getCurrentSession().createQuery(queryString).setMaxResults(1);
            query.setParameterList("fileId", fileId);
            list = query.list();
        }
        return list;
    }
}
