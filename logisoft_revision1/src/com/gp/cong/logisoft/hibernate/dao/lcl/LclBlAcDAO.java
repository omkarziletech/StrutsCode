/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;

/**
 *
 * @author lakshh
 */
public class LclBlAcDAO extends BaseHibernateDAO<LclBlAc> {

    public LclBlAcDAO() {
        super(LclBlAc.class);
    }

    public LclBlAc getLclCostByChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        return (LclBlAc) criteria.uniqueResult();
    }

    public LclBlAc getBlChageWithBlueScreenCharge(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBlAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        return (LclBlAc) criteria.setMaxResults(1).uniqueResult();
    }

    public List<LclBlAc> getLclCostByFileNumberAsc(Long fileId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List pickupList = getListByFileIdBCCME(fileId, "0012", false);
        autoChargesList.addAll(pickupList);
        List manualChargesList = getLclCostByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        List CAFList = getListByFileIdBCCME(fileId, "0005", false);
        autoChargesList.addAll(CAFList);
        List CAFList95 = getListByFileIdBCCME(fileId, "0095", false);
        autoChargesList.addAll(CAFList95);
        return autoChargesList;
    }

    public List<LclBlAc> getLclChargesByFileNo(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclBlAc> getLclChargesByFileNumberAsc(Long fileId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List manualChargesList = getLclChargesByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        List CAFList = getListByFileIdBCCME(fileId, "0005", false);
        autoChargesList.addAll(CAFList);
        List CAFList95 = getListByFileIdBCCME(fileId, "0095", false);
        autoChargesList.addAll(CAFList95);
        return autoChargesList;
    }

    public List<LclBlAc> getLclCostByFileNumberME(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclBlAc> getLclChargesByFileNumberME(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.gt("arAmount", new BigDecimal("0.00")));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclBlAc> getLclCostByFileNumber(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0012"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0005"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0095"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclBlAc> getListByFileIdBCCME(Long fileId, String blueScreenChargeCode, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", blueScreenChargeCode));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public Integer getLclChargeCountbyFileNumber(Long fileId, int manualEntry) throws Exception {
        BigInteger count = new BigInteger("0");
        String queryString = "select count(*) from lcl_bl_ac where file_number_id = ?0 and manual_entry = ?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", manualEntry);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();

    }

    public Integer getCountbyFileNumberAndChargeId(Long fileId, Integer chargeId) throws Exception {
        BigInteger count = new BigInteger("0");
        String queryString = "select count(*) from lcl_bl_ac where file_number_id = ?0 and ar_gl_mapping_id = ?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", chargeId);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();

    }

    public void updateChargesForApproval(Long fileId, Integer userId, Object[] lclCorrectionCharge) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_bl_ac set adjustment_amount = adjustment_amount + ");
        queryBuilder.append(lclCorrectionCharge[1].toString());
        queryBuilder.append(",modified_datetime=SYSDATE(), modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(",ar_bill_to_party = '");
        queryBuilder.append(lclCorrectionCharge[2].toString());
        queryBuilder.append("' where file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" and ar_gl_mapping_id = ");
        queryBuilder.append(lclCorrectionCharge[0].toString());
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateChargesForReversal(Long fileId, String arGlMappingId, Integer userId, String oldAmount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_bl_ac set adjustment_amount = ").append(oldAmount).append(" - ar_amount ,modified_datetime = SYSDATE(), modified_by_user_id = ").append(userId).append(" where file_number_id = ").append(fileId).append(" and ar_gl_mapping_id = ").append(arGlMappingId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void insertChargesForApproval(Long fileId, String arGlMappingId, Integer userId, String arAmount, String arBillToParty) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_bl_ac (file_number_id,manual_entry,trans_datetime,adjustment_amount,ar_amount,ar_gl_mapping_id,");
        queryBuilder.append("ar_bill_to_party,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (");
        queryBuilder.append(fileId).append(",1,SYSDATE(),0.00,");
        queryBuilder.append(arAmount).append(",");
        queryBuilder.append(arGlMappingId);
        queryBuilder.append(",'");
        queryBuilder.append(arBillToParty);
        queryBuilder.append("',");
        queryBuilder.append("SYSDATE(),");
        queryBuilder.append(userId);
        queryBuilder.append(",SYSDATE(),");
        queryBuilder.append(userId);
        queryBuilder.append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void insertChargesForTransipment(String fileId, Integer arGlMappingId, String manualEntry, String ratePerUnitUom, String PrintOnBL, String userId, Double arAmount, String arBillToParty) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_bl_ac (file_number_id,manual_entry,trans_datetime,adjustment_amount,ar_amount,rate_per_unit_uom,print_on_bl,ar_gl_mapping_id,");
        queryBuilder.append("ar_bill_to_party,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (");
        queryBuilder.append(fileId).append(",1,SYSDATE(),0.00,");
        queryBuilder.append(arAmount);
        queryBuilder.append(",'");
        queryBuilder.append(ratePerUnitUom);
        queryBuilder.append("',");
        queryBuilder.append(PrintOnBL).append(",");
        queryBuilder.append(arGlMappingId);
        queryBuilder.append(",'");
        queryBuilder.append(arBillToParty);
        queryBuilder.append("',");
        queryBuilder.append("SYSDATE(),");
        queryBuilder.append(userId);
        queryBuilder.append(",SYSDATE(),");
        queryBuilder.append(userId);
        queryBuilder.append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteChargesByFileIdAndArGlMappingId(Long fileId, String arGlMappingId, String manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_bl_ac where file_number_id = ").append(fileId).append(" and ar_gl_mapping_id = ").append(arGlMappingId).append(" and manual_entry =  ").append(manualEntry);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteLclCostByFileNumber(Long fileId) throws Exception {
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0012", "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_bl_ac where file_number_id=?0 and manual_entry=0 and ar_gl_mapping_id!=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.executeUpdate();
        }
        GlMapping glmappingDestFee1 = glmappingdao.findByBlueScreenChargeCode(LclCommonConstant.LCL_DESTFEE1_BLUESCREEN_CHARGE_CODE, "LCLE", "AR");
        GlMapping glmappingDestFee2 = glmappingdao.findByBlueScreenChargeCode(LclCommonConstant.LCL_DESTFEE3_BLUESCREEN_CHARGE_CODE, "LCLE", "AR");
        if (glmappingDestFee1 != null && glmappingDestFee1.getId() != null && glmappingDestFee2 != null && glmappingDestFee2.getId() != null) {
            String queryString = "DELETE FROM lcl_bl_ac WHERE file_number_id=?0 AND (ar_gl_mapping_id=?1  OR ar_gl_mapping_id=?2)";
            Query queryObjectDest = getSession().createSQLQuery(queryString);
            queryObjectDest.setParameter("0", fileId);
            queryObjectDest.setParameter("1", glmappingDestFee1.getId());
            queryObjectDest.setParameter("2", glmappingDestFee2.getId());
            queryObjectDest.executeUpdate();
        }
    }

    public BigDecimal getTotalChargesAmtByBl(Long fileId) throws Exception {
        String queryString;
        queryString = "SELECT SUM(ar_amount+adjustment_amount) FROM lcl_bl_ac WHERE file_number_id =?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        return (BigDecimal) queryObject.uniqueResult();
    }

    public void deleteManualAutoRates(Long fileId) throws Exception {
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0012", "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_bl_ac where file_number_id=?0 and ar_gl_mapping_id!=?1";
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
            String queryString = "delete from lcl_bl_ac where file_number_id=?0 and ar_gl_mapping_id=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.executeUpdate();
        }
    }

    public LclBlAc manualChargeValidate(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBlAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (LclBlAc) criteria.setMaxResults(1).uniqueResult();
    }

    public List<LclBlAc> findByFileAndCommodityList(Long fileNumberId, Long blPieceId) throws Exception {
        String queryString = "from LclBlAc where lclFileNumber='" + fileNumberId + "' and lclBlPiece='" + blPieceId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public LclBlAc getLclCostByChargeCodeBlPiece(Long fileId, String bluescreenChargeCode, boolean manualEntry, Long blPieceId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        criteria.createAlias("lclBlAc.lclBlPiece", "lclBlPiece");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBlAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(bluescreenChargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", bluescreenChargeCode));
        }
        if (!CommonUtils.isEmpty(blPieceId)) {
            criteria.add(Restrictions.eq("lclBlPiece.id", blPieceId));
        }
        return (LclBlAc) criteria.uniqueResult();
    }

    public Double getAllOfrAndTTCharges(Long fileId, Integer manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        Double total = 0.00;
        queryBuilder.append("SELECT SUM(lbac.ar_amount) FROM lcl_bl_ac lbac JOIN gl_mapping gm ON lbac.ar_gl_mapping_id = gm.id WHERE ").append("lbac.manual_entry = ").append(manualEntry).append(" AND lbac.file_number_id = ").append(fileId).append(" AND (gm.Charge_code = '").append(CommonConstants.OFR_CHARGECODE).append("' OR gm.Charge_code like 'TT%')");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = Double.parseDouble(o.toString());
        }
        return total;
    }

    public Double getChargeForPBA(Long fileId, Integer manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        Double total = 0.00;
        queryBuilder.append("SELECT SUM(lbac.ar_amount) FROM lcl_bl_ac lbac JOIN gl_mapping gm ON lbac.ar_gl_mapping_id = gm.id WHERE ").append("lbac.manual_entry = ").append(manualEntry).append(" AND lbac.file_number_id = ").append(fileId).append(" AND (gm.Charge_code = '").append(CommonConstants.ADVSHP_CHARGECODE).append("' OR gm.Charge_code = '").append(CommonConstants.ADVFF_CHARGECODE).append("')");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = Double.parseDouble(o.toString());
        }
        return total;
    }

    public void updateBillToPartyForCharges(Long fileId, String billToParty, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_bl_ac set ar_bill_to_party = '").append(billToParty).append("',modified_by_user_id = ").append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId).append(" and ar_amount > 0.00");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateBillToPartyChargesME(Long fileId, String billToParty, Integer userId, String manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
        String glMappingId = new GlMappingDAO().getglMappingIdUsingBlueScreenCode(ConstantsInterface.LCL_IMP_BLUESCREEN_CHARGE_CODE, "LCLI",
                "AR");
        sb.append("update lcl_bl_ac set ar_bill_to_party = '").append(billToParty).append("',modified_by_user_id = ");
        sb.append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId).append(" and ar_amount > 0.00");
        sb.append(" and ar_gl_mapping_id NOT IN (").append(glMappingId).append(")");
        if (!"".equalsIgnoreCase(manualEntry)) {
            sb.append(" and manual_entry= ").append(manualEntry);
        }
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateCharges(String chargeId, String value, String flag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(chargeId)) {
            if (!"C".equalsIgnoreCase(flag)) {
                queryBuilder.append("update lcl_bl_ac set ");
                queryBuilder.append("B".equalsIgnoreCase(flag) ? " bundle_into_of" : " print_on_bl");
                queryBuilder.append("=:checkBoxUpdate where id=:chargeId ");
            } else {
                queryBuilder.append("update lcl_correction_charge set print_on_bl=:checkBoxUpdate where id=:chargeId");
            }
            Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
            queryObject.setBoolean("checkBoxUpdate", Boolean.parseBoolean(value));
            queryObject.setLong("chargeId", Long.parseLong(chargeId));
            queryObject.executeUpdate();
        }
    }

    public void deleteBlAcByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBlAc where lclFileNumber.id = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public Boolean isChargeCodeValidate(String fileId, String chargeCode, String manualEntry) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) FROM lcl_bl_ac lbc ");
        query.append("JOIN gl_mapping gm ON gm.id=lbc.ar_gl_mapping_id ");
        query.append("WHERE lbc.file_number_id = ").append(fileId);
        if (!"".equalsIgnoreCase(chargeCode)) {
            query.append(" AND gm.charge_code='").append(chargeCode).append("'");
        }
        if (!"".equalsIgnoreCase(manualEntry)) {
            query.append(" AND manual_entry=").append(manualEntry);
        }
        Object result = getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
        return null != result && Integer.parseInt(result.toString()) > 0 ? true : false;
    }

    public String getConsolidateChargesWithDrs(List fileNumberId, String chargeCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(CONCAT('DR ',lc.`file_number`,' = $', b.`ar_amount`)) AS charges FROM lcl_booking_ac b ");
        sb.append(" JOIN lcl_file_number lc ON lc.id=b.`file_number_id` JOIN  gl_mapping g ON g.id = b.`ar_gl_mapping_id`  ");
        sb.append(" WHERE b.`manual_entry` = 1 and b.file_number_id IN(:fileNumberId) AND g.`Charge_code` =:chargeCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameterList("fileNumberId", fileNumberId);
        query.setParameter("chargeCode", chargeCode);
        return query.setMaxResults(1).uniqueResult() != null ? query.setMaxResults(1).uniqueResult().toString() : "";
    }

    public LclBlAc getTTCharges(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBookingAc");
        criteria.createAlias(
                "lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }

        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.like("glMapping.chargeCode", "TT%"));
        return (LclBlAc) criteria.uniqueResult();
    }

    public List<Object[]> getBLSpotRateCharge(Long fileId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT SUM(IF(gl.`Charge_code` <> 'OCNFRT',ac.rate_per_volume_unit,0))  AS volume, ");
        queryString.append(" SUM(IF(gl.`Charge_code` <> 'OCNFRT',ac.rate_per_weight_unit,0)) AS weight, ");
        queryString.append(" SUM(IF(gl.`Charge_code` = 'OCNFRT',ac.rate_per_volume_unit,0))  AS ocenfrt_volume, ");
        queryString.append(" SUM(IF(gl.`Charge_code` = 'OCNFRT',ac.rate_per_weight_unit,0)) AS ocenfrt_weight ");
        queryString.append(" FROM lcl_bl_ac ac JOIN gl_mapping gl ON gl.`id` = ac.`ar_gl_mapping_id` ");
        queryString.append(" WHERE manual_entry = 0 AND file_number_id =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("fileId", fileId);
        List<Object[]> result_list = query.list();
        return result_list;
    }

    public void updateBundleOFR(String chargeId, String value) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(chargeId)) {
            List chargeIdList = Arrays.asList(chargeId.split(","));
            queryBuilder.append("update lcl_bl_ac set bundle_into_of  =:checkBoxUpdate where id IN(:chargeId)");
            Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
            queryObject.setBoolean("checkBoxUpdate", Boolean.parseBoolean(value));
            queryObject.setParameterList("chargeId", chargeIdList);
            queryObject.executeUpdate();
        }
    }

    public List getBLChargeTotalbyBillToAndFileId(Long fileId, String billToParty, Long correctionNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT SUM(ar_amount+adjustment_amount) AS total FROM lcl_bl_ac WHERE file_number_id= ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND ar_bill_to_party IN ('");
        queryBuilder.append(billToParty);
        queryBuilder.append("')");
        queryBuilder.append(" AND print_on_bl=1 ");
        queryBuilder.append("  UNION ALL SELECT SUM(lc.new_amount) AS total FROM lcl_correction_charge lc JOIN lcl_bl_ac ac ON lc.lcl_bl_ac_id=ac.id WHERE lc.correction_id=(SELECT id FROM lcl_correction WHERE file_number_id= ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND correction_no= ");
        queryBuilder.append(correctionNo);
        queryBuilder.append(" ORDER BY id DESC LIMIT 1) AND bill_to_party IN ('");
        queryBuilder.append(billToParty);
        queryBuilder.append("')");
        queryBuilder.append(" AND ac.print_on_bl=1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("total", DoubleType.INSTANCE);
        return query.list();
    }

    public Double getTotalWithoutParticularChargeCode(Long fileId, String chargeCode) throws Exception {
        StringBuilder queryString = new StringBuilder();
        BigDecimal total = new BigDecimal(0.00);
        queryString.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_bl_ac lbac JOIN gl_mapping gp ON gp.id = lbac.ar_gl_mapping_id AND gp.Charge_code !='");
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

    public List<LclBlAc> getLclCostWithoutCAF(Long fileId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List manualChargesList = getLclCostByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        return autoChargesList;
    }

    public LclBlAc findByChargeCode(Long fileId, Boolean manualEntry,
            String shipmentType, String chargeCode) throws Exception {
        LclBlAc lclBlAc = null;
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" FROM LclBlAc lba LEFT JOIN fetch lba.arglMapping gm ");
        queryStr.append("  WHERE lba.lclFileNumber.id=:fileId and lba.arAmount <> 0.00 AND lba.manualEntry =:manualEntry ");
        queryStr.append("  AND gm.shipmentType=:shipmentType and gm.chargeCode=:chargeCode  ");
        Query query = getSession().createQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("manualEntry", manualEntry);
        query.setParameter("shipmentType", shipmentType);
        query.setParameter("chargeCode", chargeCode);
        List<LclBlAc> lclBookingAcList = query.list();
        if (CommonUtils.isNotEmpty(lclBookingAcList)) {
            lclBlAc = lclBookingAcList.get(0);
        }
        return lclBlAc;
    }

    public double getBLTotalCollectChages(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(amount) AS amount FROM ");
        sb.append(" (SELECT SUM(ar_amount+adjustment_amount) AS amount FROM lcl_bl_ac ac  ");
        sb.append(" JOIN gl_mapping gl ON gl.id = ac.`ar_gl_mapping_id` ");
        sb.append(" WHERE ac.`file_number_id`=:fileId ");
        sb.append(" AND gl.`Charge_code` <> 'CAF' AND ac.`ar_bill_to_party` = 'A' ");
        sb.append(" GROUP BY gl.`Charge_code`) AS fl ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        Object obj = query.uniqueResult();
        return obj != null ? Double.parseDouble(obj.toString()) : 0l;
    }

    public void updateBLAdjustmentWithBlueScreenCode(String blueCode, String chargeCode, Long fileId, BigDecimal amount) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" update lcl_bl_ac ac join gl_mapping gl on gl.id = ac.ar_gl_mapping_id set ac.adjustment_amount =:amount ");
        sb.append(" where gl.bluescreen_chargecode =:blueCode and gl.charge_code=:chargeCode and ac.file_number_id=:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("blueCode", blueCode);
        query.setParameter("chargeCode", chargeCode);
        query.setParameter("fileId", fileId);
        query.setParameter("amount", amount);
        query.executeUpdate();
    }

    public String hasAutoRates(String fileId, String moduleName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) >= 1, 'true', 'false') FROM lcl_bl_ac");
        queryBuilder.append(" WHERE file_number_id = ").append(fileId);
        if (moduleName != null && !"Imports".equalsIgnoreCase(moduleName)) {
            queryBuilder.append("  AND manual_entry = 0 AND ar_gl_mapping_id  IN");
            queryBuilder.append("(SELECT id FROM gl_mapping WHERE charge_code = 'OCNFRT' OR charge_code='OFIMP')");
        }
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Boolean checkBlInsurance(Long fileId, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) > 0, TRUE, FALSE) AS result ");
        queryBuilder.append(" FROM lcl_bl_ac lba JOIN gl_mapping gm  ");
        queryBuilder.append(" ON (gm.id = lba.ar_gl_mapping_id) ");
        queryBuilder.append(" WHERE gm.`Charge_code` =:chargeCode ");
        queryBuilder.append(" AND lba.`file_number_id` =:fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("chargeCode", chargeCode);
        query.setParameter("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public void updateAdjustmentCharge(BigInteger id, BigDecimal adjustAmount, String comments, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_bl_ac set adjustment_amount =:adjustAmount,");
        queryBuilder.append("adjustment_comments =:comments, `modified_datetime` = SYSDATE(),");
        queryBuilder.append("`modified_by_user_id` =:userId WHERE id=:id");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setBigDecimal("adjustAmount", adjustAmount);
        queryObject.setInteger("userId", userId);
        queryObject.setString("comments", comments);
        queryObject.setBigInteger("id", id);
        queryObject.executeUpdate();
    }
}
