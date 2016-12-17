/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl.bl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLCorrectionForm;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

/**
 *
 * @author saravanan
 */
public class LCLCorrectionChargeDAO extends BaseHibernateDAO<LclCorrectionCharge> {

    public LCLCorrectionChargeDAO() {
        super(LclCorrectionCharge.class);
    }

    public void insertCorrectionCharge(Long correctionId, String oldAmount, String newAmount, Integer chargeId, String billToParty, Integer userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO lcl_correction_charge (correction_id,old_amount,new_amount,gl_mapping_id,bill_to_party,entered_datetime,");
        sb.append("entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (").append(correctionId).append(",");
        sb.append(oldAmount).append(",");
        newAmount = CommonUtils.isNotEmpty(newAmount) ? newAmount : "0.00";
        sb.append(newAmount).append(",").append(chargeId).append(",'").append(billToParty).append("',");
        sb.append("SYSDATE(),").append(userId).append(",SYSDATE(),").append(userId).append(")");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
    }

    public void insertCorrectionChargeWithBookingAC(LCLCorrectionForm lclCorrectionForm, Integer userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO lcl_correction_charge (correction_id,old_amount,new_amount,gl_mapping_id,bill_to_party,entered_datetime,");
        sb.append("entered_by_user_id,modified_datetime,modified_by_user_id, ");
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            sb.append("lcl_booking_ac_id");
        } else {
            sb.append("lcl_bl_ac_id");
        }
        sb.append(") VALUES (");
        sb.append(lclCorrectionForm.getCorrectionId());
        sb.append(",");
        sb.append(lclCorrectionForm.getOldAmount()).append(",");
        String newAmount = CommonUtils.isNotEmpty(lclCorrectionForm.getNewAmount()) ? lclCorrectionForm.getNewAmount() : "0.00";
        sb.append(newAmount).append(",").append(lclCorrectionForm.getChargeId()).append(",'").append(lclCorrectionForm.getBillToParty()).append("',");
        sb.append("SYSDATE(),").append(userId).append(",SYSDATE(),").append(userId);
        sb.append(",").append(lclCorrectionForm.getLclBookingAcId() == 0.0 ? null : lclCorrectionForm.getLclBookingAcId()).append(")");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
    }

    public void updateCorrectionCharge(Long correctionChargeId, String newAmount, String billToParty, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_correction_charge set new_amount= ").append(newAmount).append(",bill_to_party = '").append(billToParty);
        queryBuilder.append("',modified_datetime=SYSDATE(), modified_by_user_id = ").append(userId).append(" where id = ").append(correctionChargeId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public Object[] getCurrentCorrection(Long correctionId, Integer chargeId, String amount, String newAmount, String viewMode,
            Long lclBookingAcId, String moduleName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select old_amount,new_amount,bill_to_party,id from lcl_correction_charge where  gl_mapping_id = ");
        queryBuilder.append(chargeId);
        queryBuilder.append(" AND correction_id = ");
        queryBuilder.append(correctionId);
        if (moduleName.equalsIgnoreCase("Imports")) {
            queryBuilder.append(" AND lcl_booking_ac_id = ");
        } else {
            queryBuilder.append(" AND lcl_bl_ac_id = ");
        }
        queryBuilder.append(lclBookingAcId);
        if (CommonUtils.isEmpty(viewMode)) {
            queryBuilder.append(" AND old_amount = ").append(amount);
        }
        queryBuilder.append(" LIMIT 1");
        return (Object[]) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BigInteger getCorrectionChargeCount(Long correctionId, Integer chargeId, String oldAmount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from lcl_correction_charge where  gl_mapping_id = ");
        queryBuilder.append(chargeId);
        queryBuilder.append(" AND correction_id = ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" AND old_amount = ");
        queryBuilder.append(oldAmount);
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BigInteger getNewChargeCount(Long correctionId, Integer chargeId, String oldAmount, String newAmount, Long fileId, Integer voidValue) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from lcl_correction_charge lcc JOIN lcl_correction lc ON lc.id = lcc.correction_id ");
        queryBuilder.append("WHERE lcc.gl_mapping_id = ");
        queryBuilder.append(chargeId);
        queryBuilder.append(" AND lcc.correction_id > ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" AND lcc.old_amount = ");
        queryBuilder.append(oldAmount);
        queryBuilder.append(" AND new_amount = ").append(newAmount);
        queryBuilder.append(" AND lc.file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND lc.void = ");
        queryBuilder.append(voidValue);
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object getLatestCorrection(Long fileId, Integer chargeId, String amount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lcc.bill_to_party FROM lcl_correction_charge lcc JOIN lcl_correction lc ON lc.id = lcc.correction_id ");
        queryBuilder.append("WHERE lcc.gl_mapping_id = ");
        queryBuilder.append(chargeId);
        queryBuilder.append(" AND lcc.new_amount = ");
        queryBuilder.append(amount);
        queryBuilder.append(" AND lc.file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" and void = 0 ORDER BY lc.id DESC LIMIT 1");
        return (Object) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object getCurrentCorrectionBilltoParty(Long correctionId, Integer chargeId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bill_to_party FROM lcl_correction_charge WHERE gl_mapping_id = ");
        queryBuilder.append(chargeId);
        queryBuilder.append(" AND correction_id = ");
        queryBuilder.append(correctionId);
        return (Object) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object[] getLatestCorrectionOldAmount(Long fileId, Integer chargeId, String billToParty, Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lc.id as correctionId,IF(lcc.correction_id<");
        queryBuilder.append(correctionId);
        queryBuilder.append(",lcc.new_amount,lcc.old_amount) AS amount FROM lcl_correction_charge lcc JOIN lcl_correction lc ON lc.id = ");
        queryBuilder.append("lcc.correction_id  WHERE lcc.gl_mapping_id = ");
        queryBuilder.append(chargeId);
        queryBuilder.append(" AND lcc.bill_to_party = '");
        queryBuilder.append(billToParty);
        queryBuilder.append("' AND lc.file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND lcc.correction_id != ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" and void = 0 ORDER BY lc.id");
        List<Object[]> latestCorrectionObj = (List<Object[]>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        Object[] returnObject = null;
        if (latestCorrectionObj != null && latestCorrectionObj.size() > 0) {
            returnObject = latestCorrectionObj.get(0);
            if (latestCorrectionObj.size() > 1) {
                int latestCorrectionId = Integer.parseInt(returnObject[0].toString());
                if (latestCorrectionId < correctionId) {
                    returnObject = latestCorrectionObj.get(latestCorrectionObj.size() - 1);
                }
            }
        }
        return returnObject;
    }

    public List<Object[]> getAllNewCorrectionCharges(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT gl.id,gl.Charge_code,gl.charge_description,lcc.new_amount,lcc.bill_to_party,lcc.id AS ");
        queryBuilder.append("correction_charge_id ,lcc.lcl_booking_ac_id FROM ");
        queryBuilder.append("lcl_correction_charge lcc JOIN gl_mapping gl ON lcc.gl_mapping_id = gl.id JOIN lcl_correction lc ON ");
        queryBuilder.append("lcc.correction_id = lc.id WHERE lcc.old_amount = 0.00 AND lc.void = 0 and lc.status='O' AND lc.file_number_id = ");
        queryBuilder.append(fileId);
        return (List<Object[]>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<Object[]> getAllCorrectionChargesForApprove(Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT gl_mapping_id,new_amount - old_amount,bill_to_party FROM lcl_correction_charge where correction_id = ");
        queryBuilder.append(correctionId);
        return (List<Object[]>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<Object[]> getAllCorrectionChargesForApproveByFileId(Long fileId, String correctionCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lcc.gl_mapping_id,");
        sb.append("lcc.new_amount - lcc.old_amount").append(",lcc.bill_to_party,old_amount FROM lcl_correction_charge ");
        sb.append("lcc JOIN lcl_correction lc ON lcc.correction_id = lc.id WHERE lc.status != 'A' AND void = 0 AND  lc.file_number_id = ");
        sb.append(fileId);
        return (List<Object[]>) getCurrentSession().createSQLQuery(sb.toString()).list();
    }

    public List<Object[]> getAllCorrectionChargesForReverse(Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT gl_mapping_id,old_amount,new_amount,bill_to_party FROM lcl_correction_charge where correction_id = ");
        queryBuilder.append(correctionId);
        return (List<Object[]>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<Object> getCorrectionChargesSumForCreditDebit(String correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT new_amount-old_amount FROM lcl_correction_charge WHERE correction_id = ").append(correctionId);
        return (List<Object>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public BigDecimal getCorrectionChargesDifferenceForCreditDebit(Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT SUM(new_amount)-SUM(old_amount) FROM lcl_correction_charge WHERE correction_id = ");
        queryBuilder.append(correctionId);
        return (BigDecimal) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List<Object[]> getAllCorrectionChargesByCorrectionId(Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT gl.Charge_code,lcc.old_amount,lcc.new_amount FROM lcl_correction_charge lcc ");
        queryBuilder.append("JOIN gl_mapping gl ON lcc.gl_mapping_id = gl.id WHERE lcc.correction_id = ");
        queryBuilder.append(correctionId);
        return (List<Object[]>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public void deleteCorrectionCharge(Long correctionChargeId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_correction_charge where id = ");
        queryBuilder.append(correctionChargeId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getBlBilltoParty(Long fileId, Integer arGlMappingId, String amount, Long blAcId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append("  ar_bill_to_party as billToParty ");
        query.append("from");
        query.append("  lcl_bl_ac ");
        query.append("where");
        query.append("  file_number_id =:fileId ");
        query.append("  and ar_gl_mapping_id =:arGlMappingId ");
        query.append("  and ar_amount =:amount ");
        if (blAcId != null && blAcId != 0) {
            query.append("  and id = ").append(blAcId).append(" ");
        }
        query.append("limit 1");
        SQLQuery queryObj = getCurrentSession().createSQLQuery(query.toString());
        queryObj.addScalar("billToParty", StringType.INSTANCE);
        queryObj.setParameter("fileId", fileId);
        queryObj.setParameter("arGlMappingId", arGlMappingId);
        queryObj.setParameter("amount", amount);
        return (String) queryObj.uniqueResult();
    }

    public String getArBillToParty(Long fileId, Integer arGlMappingId, String amount, Long lclBookingAcId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append("  ar_bill_to_party as billToParty ");
        query.append("from");
        query.append("  lcl_booking_ac ");
        query.append("where");
        query.append("  file_number_id = ").append(fileId);
        query.append("  and ar_gl_mapping_id = ").append(arGlMappingId);
        query.append("  and ar_amount = ").append(amount);
        query.append("  and ar_bill_to_party <> 'A' ");
        if (lclBookingAcId != null && lclBookingAcId != 0) {
            query.append("  and id = ").append(lclBookingAcId).append(" ");
        }
        query.append("limit 1");
        return (String) getCurrentSession().createSQLQuery(query.toString()).addScalar("billToParty", StringType.INSTANCE).uniqueResult();
    }

    public List getCorrectionId(Long fileId) throws Exception {
        String query = "SELECT id FROM `lcl_correction` lc WHERE lc.`file_number_id`=" + fileId + " AND lc.`void` IS NOT TRUE";
        return getCurrentSession().createSQLQuery(query).list();
    }

    public void updateCorrectionBillToCode(String billToCode, Integer userId, Long correctionId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append("  `lcl_correction_charge` lc ");
        sb.append(" SET");
        sb.append("  lc.`bill_to_party` =:billToCode,");
        sb.append("  lc.`modified_by_user_id` =:userId,");
        sb.append("  lc.`modified_datetime` = SYSDATE() ");
        sb.append(" WHERE lc.`correction_id` =:correctionId");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("billToCode", billToCode);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("correctionId", correctionId);
        queryObject.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateCorrectionListBillToCode(String billToCode, Integer userId, List correctionId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append("  `lcl_correction_charge` lc ");
        sb.append(" SET");
        sb.append("  lc.`bill_to_party` =:billToCode,");
        sb.append("  lc.`modified_by_user_id` =:userId,");
        sb.append("  lc.`modified_datetime` = SYSDATE() ");
        sb.append(" WHERE lc.`correction_id` IN (:correctionidList)");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("billToCode", billToCode);
        queryObject.setParameter("userId", userId);
        queryObject.setParameterList("correctionidList", correctionId);
        queryObject.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String getNewBillToCode(Long correctionId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lcc.`bill_to_party` as billToCode");
        sb.append(" FROM");
        sb.append("  `lcl_correction` lc ");
        sb.append("  JOIN `lcl_correction_charge` lcc ");
        sb.append("    ON (lc.`id` = lcc.`correction_id`) ");
        sb.append("WHERE lc.`id` =:correctionId ");
        sb.append("  AND (lcc.`old_amount` <> '' ");
        sb.append("  AND lcc.`old_amount` <> 0.00 ");
        sb.append("  AND lcc.`lcl_booking_ac_id` IS NULL) ");
        sb.append("  ORDER BY lcc.`id` DESC LIMIT 1");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.addScalar("billToCode", StringType.INSTANCE);
        queryObject.setParameter("correctionId", correctionId);
        return queryObject.uniqueResult() != null ? queryObject.uniqueResult().toString() : "";
    }

    public List getCorrectionChargeId(Long correctionId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lcc.`id` ");
        sb.append("FROM");
        sb.append("  `lcl_correction` lc ");
        sb.append("  JOIN `lcl_correction_charge` lcc ");
        sb.append("    ON (lc.`id` = lcc.`correction_id`) ");
        sb.append("WHERE lc.`id` =:correctionId ");
        sb.append("  AND (lcc.`old_amount` <> '' ");
        sb.append("  AND lcc.`old_amount` <> 0.00 ");
        sb.append("  AND lcc.`lcl_booking_ac_id` IS NULL)");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("correctionId", correctionId);
        return queryObject.list();
    }

    public void updateBillToCode(List id, String billToCode) throws Exception {
        String query = "UPDATE `lcl_correction_charge` lc SET lc.`bill_to_party`=:billToCode WHERE lc.`id` IN (:id);";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("billToCode", billToCode);
        queryObject.setParameterList("id", id);
        queryObject.executeUpdate();
    }

    public void updateBlAcIdByLclE(Long corrChargeId, Long blAcId) throws Exception {
        String query = "UPDATE lcl_correction_charge lc SET lc.lcl_bl_ac_id=:blAcId WHERE lc.id=:corrChargeId";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("corrChargeId", corrChargeId);
        queryObject.setParameter("blAcId", blAcId);
        queryObject.executeUpdate();
    }

    public LclCorrectionCharge getChargeByChargeCodeAndCorrectionID(Long correctionId, String chargeCode) throws Exception {
        Query queryObject = getCurrentSession().createQuery("from LclCorrectionCharge where lclCorrection.id=:correctionId and glMapping.chargeCode=:chargeCode");
        queryObject.setParameter("correctionId", correctionId);
        queryObject.setParameter("chargeCode", chargeCode);
        return (LclCorrectionCharge) queryObject.uniqueResult();
    }
    
    
     public List<LclCorrectionCharge> getLclCorrectionChargesList(Long correctionId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(correctionId, false);
        List manualChargesList = getLclChargesByFileNumberME(correctionId, true);
        autoChargesList.addAll(manualChargesList);
        List CAFList = getListByFileIdBCCME(correctionId, "0005", false);
        autoChargesList.addAll(CAFList);
        List CAFList95 = getListByFileIdBCCME(correctionId, "0095", false);
        autoChargesList.addAll(CAFList95);
        return autoChargesList;
    }
     
      public List<LclCorrectionCharge> getLclCostByFileNumber(Long correctionId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclCorrectionCharge.class, "lclCorrectionCharge");
        criteria.createAlias("lclCorrectionCharge.glMapping", "glMapping");
        if (!CommonUtils.isEmpty(correctionId)) {
            criteria.add(Restrictions.eq("lclCorrection.id", correctionId));
        }
        criteria.add(Restrictions.eq("manualCharge", manualEntry));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0012"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0005"));
        criteria.add(Restrictions.ne("glMapping.blueScreenChargeCode", "0095"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
      
    public List<LclCorrectionCharge> getLclChargesByFileNumberME(Long correctionId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclCorrectionCharge.class, "lclCorrectionCharge");
        if (!CommonUtils.isEmpty(correctionId)) {
            criteria.add(Restrictions.eq("lclCorrection.id", correctionId));
        }
        criteria.add(Restrictions.eq("manualCharge", manualEntry));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
   
     public List<LclBlAc> getListByFileIdBCCME(Long correctionId, String blueScreenChargeCode, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclCorrectionCharge.class, "lclCorrectionCharge");
       criteria.createAlias("lclCorrectionCharge.glMapping", "glMapping");
        if (!CommonUtils.isEmpty(correctionId)) {
            criteria.add(Restrictions.eq("lclCorrection.id", correctionId));
        }
        criteria.add(Restrictions.eq("manualCharge", manualEntry));
        criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", blueScreenChargeCode));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }


    public List<LclBlAc> getAllCorrectedBlCharges(Long fileId, Long correctionId) throws Exception {
        Query queryObject = getCurrentSession().createQuery(" from LclBlAc bl join fetch bl.lclCorrection lc where bl.lclFileNumber.id=:fileId and lc.id=:correctionId ");
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("correctionId", correctionId);
        return (List<LclBlAc>) queryObject.list();
    }

    public List<LclCorrectionCharge> getAllApprovedCorrectionCharges(Long fileId, Long correctionId) throws Exception {
        Query queryObject = getCurrentSession().createQuery("from LclCorrectionCharge lch where lclCorrection.id=:correctionId and lclCorrection.lclFileNumber.id=:fileId ");
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("correctionId", correctionId);
        return (List<LclCorrectionCharge>) queryObject.list();
    }

    public Long getPreviousCorrectionId(Long fileId) throws Exception {
        String query = "SELECT id FROM `lcl_correction` lc WHERE lc.`file_number_id`=" + fileId + " AND lc.`void` IS NOT TRUE "
                + " and status ='A' ORDER BY id DESC LIMIT 0,1";
        BigInteger value = (BigInteger) getCurrentSession().createSQLQuery(query).uniqueResult();
        if (value == null) {
            query = "SELECT id FROM `lcl_correction` lc WHERE lc.`file_number_id`=" + fileId + " AND lc.`void` IS NOT TRUE "
                    + "ORDER BY id DESC LIMIT 1";
            value = (BigInteger) getCurrentSession().createSQLQuery(query).uniqueResult();
        }
        return value != null ? value.longValue() : 0;
    }

    public void deleteLCLBlAcWithCorrection(Long fileId, Long correctionId, String chargeCode) throws Exception {
        String sb = "delete from lcl_bl_ac ac  JOIN gl_mapping gl ON gl.ar_gl_mapping_id = gl.id where "
                + " ac.file_number_id =:fileId and accorrection_id=:correctionId and gl.charge_code=:chargeCode";
        SQLQuery query = getCurrentSession().createSQLQuery(sb);
        query.setParameter("correctionId", correctionId);
        query.setParameter("fileId", fileId);
        query.setParameter("chargeCode", chargeCode);
        query.executeUpdate();
    }

    public void deleteAllCorrectionCharge(Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_correction_charge where correction_id = ");
        queryBuilder.append(correctionId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    
    
    public void insertExportCorrectionCharge(LclCorrection correction, LclBlAc lclBlAc, Integer chargeId,
            BigDecimal oldAmount, BigDecimal newAmount, String billToParty,
            String oldBillToparty, User user, boolean manualCharge, String engMet, String ratePerUnitUom,
            BigDecimal perUnitVolume, BigDecimal perUnitWeight, BigDecimal minimum) throws Exception {
        Date date = new Date();
        LclCorrectionCharge charge = new LclCorrectionCharge();
        charge.setLclCorrection(correction);
        charge.setGlMapping(new GlMappingDAO().findById(chargeId));
        charge.setLclBlAc(lclBlAc);
        charge.setOldAmount(oldAmount);
        charge.setNewAmount(newAmount);
        charge.setBillToParty(billToParty);
        charge.setOldBillToParty(oldBillToparty);
        charge.setManualCharge(manualCharge);
        charge.setEnteredBy(user);
        charge.setModifiedBy(user);
        charge.setEnteredDatetime(date);
        charge.setModifiedDatetime(date);

        charge.setRatePerUnitUom(ratePerUnitUom);
        charge.setRatePerVolumeUnit(perUnitVolume);
        charge.setRatePerWeightUnit(perUnitWeight);
        charge.setMinimumRate(minimum);
        if (!ratePerUnitUom.equalsIgnoreCase("FL") && engMet.equalsIgnoreCase("M")) {
            charge.setRatePerWeightUnitDiv(new BigDecimal(1000));
        } else if (!ratePerUnitUom.equalsIgnoreCase("FL") && engMet.equalsIgnoreCase("E")) {
            charge.setRatePerWeightUnitDiv(new BigDecimal(100));
        } else {
            charge.setRatePerWeightUnitDiv(null);
        }
        charge.setRatePerVolumeUnitDiv(!ratePerUnitUom.equalsIgnoreCase("FL") ? new BigDecimal(1000) : null);
        charge.setPrintOnBl(null != lclBlAc ? lclBlAc.getPrintOnBl() : true);

        new LCLCorrectionChargeDAO().saveOrUpdate(charge);
    }

    public void updateLclBookingAcId(Integer lclBookingAcId, Integer userId,
            Long correctionId, Integer glMappingId, String billToParty) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append("  `lcl_correction_charge` lc ");
        sb.append(" SET");
        sb.append("  lc.`lcl_booking_ac_id` =:lclBookingAcId,");
        sb.append("  lc.`modified_by_user_id` =:userId,");
        sb.append("  lc.`modified_datetime` = SYSDATE() ");
        sb.append(" WHERE lc.`correction_id` =:correctionId and lc.`lcl_booking_ac_id` is NULL");
        sb.append("  and lc.`gl_mapping_id` =:glMappingId and lc.bill_to_party =:billToParty");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("lclBookingAcId", lclBookingAcId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("correctionId", correctionId);
        queryObject.setParameter("glMappingId", glMappingId);
        queryObject.setParameter("billToParty", billToParty);
        queryObject.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void saveCorrectionCharge(LclCorrection correction, GlMapping glMapping,
            BigDecimal oldAmount, BigDecimal newAmount, String billToParty,
            String oldBillToparty, User user, String engMet, String ratePerUnitUom,
            BigDecimal perUnitVolume, BigDecimal perUnitWeight, BigDecimal minimum) throws Exception {
        Date date = new Date();
        LclCorrectionCharge charge = new LclCorrectionCharge();
        charge.setLclCorrection(correction);
        charge.setGlMapping(glMapping);
        charge.setLclBlAc(null);
        charge.setOldAmount(oldAmount);
        charge.setNewAmount(newAmount);
        charge.setBillToParty(billToParty);
        charge.setOldBillToParty(oldBillToparty);
        charge.setManualCharge(false);
        charge.setEnteredBy(user);
        charge.setModifiedBy(user);
        charge.setEnteredDatetime(date);
        charge.setModifiedDatetime(date);

        charge.setRatePerUnitUom(ratePerUnitUom);
        charge.setRatePerVolumeUnit(perUnitVolume);
        charge.setRatePerWeightUnit(perUnitWeight);
        charge.setMinimumRate(minimum);
        if (!ratePerUnitUom.equalsIgnoreCase("FL") && engMet.equalsIgnoreCase("M")) {
            charge.setRatePerWeightUnitDiv(new BigDecimal(1000));
        } else if (!ratePerUnitUom.equalsIgnoreCase("FL") && engMet.equalsIgnoreCase("E")) {
            charge.setRatePerWeightUnitDiv(new BigDecimal(100));
        } else {
            charge.setRatePerWeightUnitDiv(null);
        }
        charge.setRatePerVolumeUnitDiv(!ratePerUnitUom.equalsIgnoreCase("FL") ? new BigDecimal(1000) : null);
        charge.setPrintOnBl(true);

        new LCLCorrectionChargeDAO().saveOrUpdate(charge);
    }
}
