/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteDestinationServices;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.type.BooleanType;

/**
 *
 * @author lakshh
 */
public class LclQuoteAcDAO extends BaseHibernateDAO<LclQuoteAc> {

    private Double totalWeight = 0.0;

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public LclQuoteAcDAO() {
        super(LclQuoteAc.class);
    }

    public LclQuoteAc getLclCostByChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        return (LclQuoteAc) criteria.uniqueResult();
    }

    public List<LclQuoteAc> getLclCostByFileNumberAsc(Long fileId, String moduleName) throws Exception {
        String chargeCode = "";
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            chargeCode = LclCommonConstant.CHARGE_CODE_DOOR;
        } else {
            chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        }
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List denseList = getCFAList(fileId, "0251", false);
        autoChargesList.addAll(denseList);
        List pickupList = getChargeCodeList(fileId, chargeCode, false);
        autoChargesList.addAll(pickupList);
        List manualChargesList = getLclCostByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        List CAFList = getCFAList(fileId, "0005", false);
        autoChargesList.addAll(CAFList);
        List CAFList95 = getCFAList(fileId, "0095", false);
        autoChargesList.addAll(CAFList95);
        return autoChargesList;
    }

    public List<LclQuoteAc> getChargeCodeList(Long fileId, String chargeCode, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclQuoteAc> getLclCostByFileNumberME(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclQuoteAc> getLclCostByFileNumber(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0012"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "1612"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0005"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0095"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0251"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "INLAND"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "DOORDEL"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclQuoteAc> getCFAList(Long fileId, String blueScreenChargeCode, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", blueScreenChargeCode));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclQuoteAc> findByFileAndCommodityList(Long fileNumberId, Long bookingPieceId) throws Exception {
        LclQuoteAc lclQuoteAc = null;
        String queryString = "from LclQuoteAc where lclFileNumber='" + fileNumberId + "' and lclQuotePiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<LclQuoteAc> getWithoutOcnFrtList(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "OCNFRT"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public Integer getLclChargeCountbyFileNumber(Long fileId, int manualEntry) throws Exception {
        BigInteger count = new BigInteger("0");
        String queryString = "select count(*) from lcl_quote_ac where file_number_id = ?0 and manual_entry = ?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", manualEntry);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();

    }

    public void deleteLclCostByFileNumber(Long fileId, String moduleName) throws Exception {
        GlMappingDAO glmappingdao = new GlMappingDAO();
        //Manual Charge Pickup
        String shipmentType = "", blueScreenCode = "";
        if ("I".equalsIgnoreCase(moduleName)) {
            shipmentType = LclCommonConstant.LCL_SHIPMENT_TYPE_IMPORT;
            blueScreenCode = LclCommonConstant.CHARGE_CODE_DOOR;
        } else {
            shipmentType = LclCommonConstant.LCL_SHIPMENT_TYPE_EXPORT;
            blueScreenCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        }
        GlMapping glmapping = glmappingdao.findByChargeCode(blueScreenCode, shipmentType, "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_quote_ac where file_number_id=?0 and manual_entry=0 and ar_gl_mapping_id!=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setLong("0", fileId);
            queryObject.setInteger("1", glmapping.getId());
            queryObject.executeUpdate();
        }
        GlMapping glmappingDestFee1 = glmappingdao.findByBlueScreenChargeCode(LclCommonConstant.LCL_DESTFEE1_BLUESCREEN_CHARGE_CODE, shipmentType, "AR");
        GlMapping glmappingDestFee2 = glmappingdao.findByBlueScreenChargeCode(LclCommonConstant.LCL_DESTFEE3_BLUESCREEN_CHARGE_CODE, shipmentType, "AR");
        if (glmappingDestFee1 != null && glmappingDestFee1.getId() != null &&  glmappingDestFee2 != null && glmappingDestFee2.getId() != null) {
            String queryString = "DELETE FROM lcl_quote_ac WHERE file_number_id=?0 AND (ar_gl_mapping_id=?1  OR ar_gl_mapping_id=?2)";
            Query queryObjectDest = getSession().createSQLQuery(queryString);
            queryObjectDest.setParameter("0", fileId);
            queryObjectDest.setParameter("1", glmappingDestFee1.getId());
            queryObjectDest.setParameter("2", glmappingDestFee2.getId());
            queryObjectDest.executeUpdate();
        }
    }

    public String getTotalLclCostByFileNumber(Long fileId) throws Exception {
        String queryString = "SELECT FORMAT(SUM(ar_amount + adjustment_amount),2) FROM lcl_quote_ac WHERE file_number_id =" + fileId;
        return (String) getSession().createSQLQuery(queryString).uniqueResult();
    }

    public void deleteManualAutoRates(Long fileId) throws Exception {
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0012", "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_quote_ac where file_number_id=?0 and ar_gl_mapping_id!=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.executeUpdate();
        }
    }

    public void deletePickUpRatesOnly(Long fileId) throws Exception {
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0012", "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_quote_ac where file_number_id=?0 and ar_gl_mapping_id=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.executeUpdate();
        }
    }

    public LclQuoteAc manaualChargeValidate(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclQuoteAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (LclQuoteAc) criteria.uniqueResult();
    }
    public List<LclQuoteAc> HazadrousChargeValidate(Long fileId, String[] chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclQuoteAc.manualEntry", manualEntry));
        if (null != chargeCode && chargeCode.length > 0) {
            criteria.add(Restrictions.in("glMapping.chargeCode", chargeCode));
        }
        return (List<LclQuoteAc>) criteria.list();
    }

    public int deleteRatesByChargeCodeAndManualEntry(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        int deletedRows = 0;
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(chargeCode, "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_quote_ac where file_number_id=?0 and ar_gl_mapping_id=?1 and manual_entry=?2";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.setParameter("2", manualEntry);
            deletedRows = queryObject.executeUpdate();
        }
        return deletedRows;
    }

    public Double getTotalLclCostWithoutParticularCharge(Long fileId, Integer arGlMappingId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        queryString = "SELECT SUM(ar_amount) FROM lcl_quote_ac WHERE file_number_id =" + fileId + " AND ar_gl_mapping_id!=" + arGlMappingId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }

        return total.doubleValue();
    }

    public LclQuoteAc getLclCostByChargeCodeQuotePiece(Long fileId, String bluescreenChargeCode, boolean manualEntry, Long quotePieceId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        criteria.createAlias("lclQuoteAc.lclQuotePiece", "lclQuotePiece");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclQuoteAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(bluescreenChargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", bluescreenChargeCode));
        }
        if (!CommonUtils.isEmpty(quotePieceId)) {
            criteria.add(Restrictions.eq("lclQuotePiece.id", quotePieceId));
        }
        return (LclQuoteAc) criteria.uniqueResult();
    }

    public List<Object[]> getSpotRateCharge(Long fileId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT SUM(IF(gl.`Charge_code` <> 'OCNFRT',ac.rate_per_volume_unit,0))  AS volume, ");
        queryString.append(" SUM(IF(gl.`Charge_code` <> 'OCNFRT',ac.rate_per_weight_unit,0)) AS weight, ");
        queryString.append(" SUM(IF(gl.`Charge_code` = 'OCNFRT',ac.rate_per_volume_unit,0))  AS ocenfrt_volume, ");
        queryString.append(" SUM(IF(gl.`Charge_code` = 'OCNFRT',ac.rate_per_weight_unit,0)) AS ocenfrt_weight ");
        queryString.append(" FROM lcl_quote_ac ac JOIN gl_mapping gl ON gl.`id` = ac.`ar_gl_mapping_id` ");
        queryString.append(" WHERE manual_entry = 0 AND file_number_id =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("fileId", fileId);
        List<Object[]> result_list = query.list();
        return result_list;
    }

    public List<LclQuoteAc> getAllLclCostByBluescreenChargeCodeME(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return criteria.list();
    }

    public int deleteRatesByChargeCode(Long fileId, String chargeCode, String shipmentType) throws Exception {
        int deletedRows = 0;
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(chargeCode, shipmentType, "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_quote_ac where file_number_id=?0 and ar_gl_mapping_id=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            deletedRows = queryObject.executeUpdate();
        }
        return deletedRows;
    }

    public int deleteRatesByGlmappingChargeCodeAndManualEntry(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        int deletedRows = 0;
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByChargeCode(chargeCode, "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_quote_ac where file_number_id=?0 and ar_gl_mapping_id=?1 and manual_entry=?2";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.setParameter("2", manualEntry);
            deletedRows = queryObject.executeUpdate();
        }
        return deletedRows;
    }

    public List<LclQuoteAc> getAllLclCostByBluescreenChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        return criteria.list();
    }

    public LclQuoteAc getTTCharges(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.like("glMapping.chargeCode", "TT%"));
        return (LclQuoteAc) criteria.uniqueResult();
    }

    public List<LclQuoteAc> getTTChargesList(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.like("glMapping.chargeCode", "TT%"));
        return criteria.list();
    }

    public void updateCharges(String id, String flag, String index) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(id)) {
            queryBuilder.append("update lcl_quote_ac set ");
            if (index.equals("B")) {
                queryBuilder.append("bundle_into_of  = '");
            } else {
                queryBuilder.append("print_on_bl  = '");
            }
            queryBuilder.append(flag).append("' where id=").append(id);
            Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
            queryObject.executeUpdate();
        }
    }

    public LclQuoteAc findByBlueScreenChargeCode(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return (LclQuoteAc) criteria.uniqueResult();
    }

    public List<LclQuoteAc> getLclCostWithoutCAF(Long fileId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List pickupList = getCFAList(fileId, "0012", false);
        autoChargesList.addAll(pickupList);
        List manualChargesList = getLclCostByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        return autoChargesList;
    }

    public Double getTotalWithoutParticularChargeCode(Long fileId, String chargeCode) throws Exception {
        StringBuilder queryString = new StringBuilder();
        BigDecimal total = new BigDecimal(0.00);
        queryString.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_quote_ac lqac JOIN gl_mapping gp ON gp.id = lqac.ar_gl_mapping_id AND gp.Charge_code !='");
        queryString.append(chargeCode);
        queryString.append("' AND file_number_id =  ?0");
        Query queryObject = getSession().createSQLQuery(queryString.toString());
        queryObject.setParameter("0", fileId);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total.doubleValue();
    }
    //Import

    public BigDecimal getTotalAmountByCollect(Long fileId, String billToCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_quote_ac WHERE file_number_id = ").append(fileId);
        sb.append(" and ar_bill_to_party!= '").append(billToCode).append("'");
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public BigDecimal getTotalAmountByPrepaid(Long fileId, String billToCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_quote_ac WHERE file_number_id = ").append(fileId);
        sb.append(" and ar_bill_to_party= '").append(billToCode).append("'");
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public void updateChargesByBillToParty(Long fileId, String billToParty, Integer userId, String manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
//        String glMappingId = new GlMappingDAO().getglMappingIdUsingBlueScreenCode(ConstantsInterface.LCL_IMP_BLUESCREEN_CHARGE_CODE, "LCLI",
//                ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        sb.append("update lcl_quote_ac set ar_bill_to_party = '").append(billToParty).append("',modified_by_user_id = ");
        sb.append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId).append(" and ar_amount > 0.00");
//        sb.append(" and ar_gl_mapping_id NOT IN (").append(glMappingId).append(")");
        if (!manualEntry.equals("")) {
            sb.append(" and manual_entry= ").append(manualEntry);
        }
//        if (manualEntry.equals("")) {
//            sb.append(" AND rels_to_inv=").append(false);
//        }
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String hasAutoRates(Long fileId, String moduleName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) >= 1, 'true', 'false') FROM lcl_quote_ac");
        queryBuilder.append(" WHERE file_number_id = ").append(fileId);
        if (moduleName != null && !"Imports".equalsIgnoreCase(moduleName)) {
            queryBuilder.append("  AND manual_entry = 0 AND ar_gl_mapping_id NOT IN");
            queryBuilder.append("(SELECT id FROM gl_mapping WHERE charge_code = 'OCNFRT')");
        }
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public LclQuoteAc getLclQuoteAcByChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        criteria.setMaxResults(1);
        return (LclQuoteAc) criteria.uniqueResult();
    }

    public void deleteChargesByArGlMappingId(Long fileId, String arGlMappingId, String manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_quote_ac where file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" and ar_gl_mapping_id IN( ");
        queryBuilder.append(arGlMappingId);
        queryBuilder.append(") and manual_entry =  ");
        queryBuilder.append(manualEntry);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public BigDecimal getTotalApAmt(Long fileId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        queryString = "SELECT SUM(ap_amount) FROM lcl_quote_ac WHERE file_number_id =" + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total;
    }

    public String isChargeCodeValidate(String fileId, String chargeCode, String manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM lcl_quote_ac lqa ");
        if (null != chargeCode && !"".equalsIgnoreCase(chargeCode)) {
            sb.append("JOIN gl_mapping gm ON gm.id=lqa.ar_gl_mapping_id ");
        }
        sb.append("WHERE lqa.file_number_id = ").append(fileId);
        if (null != chargeCode && !"".equalsIgnoreCase(chargeCode)) {
            sb.append(" AND gm.charge_code='").append(chargeCode).append("'");
        }
        if (!"".equalsIgnoreCase(manualEntry)) {
            sb.append(" AND manual_entry=").append(manualEntry);
        }
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public int getTransitTime(String polUnCode, String podUnCode, String commodityNumber) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        String blueScreenDb = LoadLogisoftProperties.getProperty("elite.database.name");
        queryStr.append("SELECT r.trans FROM ");
        queryStr.append(blueScreenDb).append(".rates r");
        queryStr.append(" JOIN ports pol ON r.orgnum=pol.govschnum  ");
        queryStr.append(" JOIN ports pod ON r.dstnum=pod.govschnum  ");
        queryStr.append(" WHERE r.chgcod=:chargeCode ");
        queryStr.append(" AND pol.unlocationcode=:polUnCode ");
        queryStr.append(" AND pod.unlocationcode =:podUnCode ");
        queryStr.append("AND r.comnum=:commoNo Limit 1");
        Query query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("chargeCode", "0080");
        query.setParameter("polUnCode", polUnCode);
        query.setParameter("podUnCode", podUnCode);
        query.setParameter("commoNo", commodityNumber);
        Object count = query.uniqueResult();
        if (count != null) {
            return (Integer) count;
        }
        return 0;
    }

    public List<LclQuoteAc> getOcnftOfimpList(Long fileId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("FROM LclQuoteAc lqa LEFT JOIN fetch lqa.arglMapping gm ");
        query.append("WHERE lqa.lclFileNumber.id=?0 AND (gm.chargeCode='OCNFRT' OR gm.chargeCode='OFIMP') ORDER BY lqa.id ASC");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("0", fileId);
        List list = hqlQuery.list();
        return list;
    }

    public List<LclQuoteAc> getLclCostByFileNumberAscPdf(Long fileId, String moduleName) throws Exception {
        String blueScreenChargeCode = "";
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            blueScreenChargeCode = LclCommonConstant.CHARGE_CODE_DOOR;
        } else {
            blueScreenChargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        }
        List autoChargesList = getLclCostByFileNumberPdf(fileId, false);
        List pickupList = getChargeCodeList(fileId, blueScreenChargeCode, false);
        autoChargesList.addAll(pickupList);
        List manualChargesList = getLclCostByFileNumberMEPdf(fileId, true);
        autoChargesList.addAll(manualChargesList);
        List CAFList = getCFAList(fileId, "0005", false);
        autoChargesList.addAll(CAFList);
        List CAFList95 = getCFAList(fileId, "0095", false);
        autoChargesList.addAll(CAFList95);
        return autoChargesList;
    }

    public List<LclQuoteAc> getLclCostByFileNumberMEPdf(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "OFIMP"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "OCNFRT"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclQuoteAc> getLclCostByFileNumberPdf(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuoteAc.class, "lclQuoteAc");
        criteria.createAlias("lclQuoteAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0012"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "1612"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0005"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0095"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "OFIMP"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "OCNFRT"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "INLAND"));
        criteria.add(Restrictions.ne("glMapping.chargeCode", "DOORDEL"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public void deleteByQtAcId(Long fileId) throws Exception {
        String query = "delete from lcl_Quote_ac where id=" + fileId;
        SQLQuery deleteQuery = getSession().createSQLQuery(query);
        deleteQuery.executeUpdate();
    }

    public List<LclQuoteAc> getQuoteDestinationChargeList(Long fileId, List blueScreenChargeCode, Boolean manualEntry, Boolean destinationServices) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("FROM LclQuoteAc lba LEFT JOIN fetch lba.arglMapping gm ");
        query.append("WHERE lba.lclFileNumber.id=:file_Id AND lba.manualEntry =:manua AND gm.destinationServices =:dest AND gm.chargeCode IN(:blue)  AND (lba.arAmount != 0.00 OR lba.apAmount !=0.00) ORDER BY lba.id ASC");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("file_Id", fileId);
        hqlQuery.setParameter("manua", manualEntry);
        hqlQuery.setParameter("dest", destinationServices);
        hqlQuery.setParameterList("blue", blueScreenChargeCode);
        List list = hqlQuery.list();
        return list;
    }

    public Boolean isValidateRates(String fileId, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*) > 0, TRUE, FALSE) AS result FROM lcl_quote_ac lqc  ");
        queryBuilder.append(" JOIN gl_mapping gm ON (lqc.ar_gl_mapping_id=gm.id AND ");
        queryBuilder.append(" gm.Charge_code=:chargeCode AND shipment_type=:shipType) ");
        queryBuilder.append(" WHERE lqc.file_number_id=:fileId ");
        queryBuilder.append(" AND lqc.ap_amount <> 0.00 AND lqc.ar_amount <> 0.00  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("chargeCode", chargeCode);
        query.setString("shipType", "LCLE");
        query.setString("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public List<LclQuoteAc> getRatesByBlueScreenCode(Long fileId, Boolean manualEntry, List chargeCodeList) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("FROM LclQuoteAc lba LEFT JOIN FETCH lba.arglMapping gm ");
        queryBuilder.append("  WHERE lba.lclFileNumber.id=:fileId AND lba.manualEntry =:manual AND ");
        queryBuilder.append("  gm.blueScreenChargeCode IN(:blueScreenList) ");
        Query query = getSession().createQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("manual", manualEntry);
        query.setParameterList("blueScreenList", chargeCodeList);
        List chargeList = query.list();
        return chargeList;
    }

    public LclQuoteDestinationServices getDestinationCharge(Long quoteAcId, Long fileId) throws Exception {
        Query query = getSession().createQuery("from LclQuoteDestinationServices "
                + "ld join fetch ld.lclquoteAc ac where ac.id=:quoteAcId and ld.lclFileNumber.id=:fileId");
        query.setParameter("quoteAcId", quoteAcId);
        query.setParameter("fileId", fileId);
        return (LclQuoteDestinationServices) query.uniqueResult();
    }

    public boolean isChargeContainDestinationCharge(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*) > 0, TRUE, FALSE) AS result FROM lcl_quote_ac lqc  ");
        queryBuilder.append(" JOIN gl_mapping gm ON (lqc.ar_gl_mapping_id=gm.id AND gm.destination_services = 1)");
        queryBuilder.append(" WHERE lqc.file_number_id=:fileId and lqc.ar_amount <> 0.00 ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public LclQuoteDestinationServices getDestinationChargeWithChargeCode(Long fileId, String chargeCode) throws Exception {
        Query query = getSession().createQuery("from LclQuoteDestinationServices "
                + " ld join fetch ld.lclquoteAc ac where ac.arglMapping.chargeCode=:chargeCode "
                + " and ac.arglMapping.destinationServices = 1 and ld.lclFileNumber.id=:fileId");
        query.setParameter("fileId", fileId);
        query.setParameter("chargeCode", chargeCode);
        return (LclQuoteDestinationServices) query.uniqueResult();
    }
    
    public List<LclQuoteAc> getQTAllDestinationChargeList(Long fileId) throws Exception {
        Query query = getSession().createQuery(" FROM LclQuoteAc ac join fetch ac.arglMapping ar  where "
                + "  ar.destinationServices = 1 and ac.arAmount <> 0.00 and ac.manualEntry = 1 and ac.lclFileNumber.id =:fileId ");
        query.setParameter("fileId", fileId);
        return query.list();
    }

    public String getAgentAccountNo(String fileId)throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT agent_acct_no FROM lcl_quote WHERE  file_number_id='").append(fileId).append("'");
    Query queryObject = getSession().createSQLQuery(sb.toString());
    String keyValue = (String)queryObject.uniqueResult();
    return CommonUtils.isNotEmpty(keyValue) ? keyValue : "";    
    }
}
