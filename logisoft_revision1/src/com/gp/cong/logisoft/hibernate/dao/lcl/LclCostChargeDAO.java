/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.beans.LclImpAlarmRevenueCostBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingDestinationServices;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author lakshh
 */
public class LclCostChargeDAO extends BaseHibernateDAO<LclBookingAc> implements ConstantsInterface {

    private Double totalWeight = 0.0;

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public LclCostChargeDAO() {
        super(LclBookingAc.class);
    }

    public LclBookingAc getLclCostByChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        return (LclBookingAc) criteria.uniqueResult();
    }

    public List<LclBookingAc> getByCostAndChargeCode(Long fileId, Integer arGlId, Integer apGlId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.createAlias("lclBookingAc.arglMapping", "chargeGlMapping");
        criteria.createAlias("lclBookingAc.apglMapping", "costGlMapping");
        if (!CommonUtils.isEmpty(arGlId)) {
            criteria.add(Restrictions.eq("chargeGlMapping.id", arGlId));
        }
        if (!CommonUtils.isEmpty(apGlId)) {
            criteria.add(Restrictions.eq("costGlMapping.id", apGlId));
        }
        return (List<LclBookingAc>) criteria.list();
    }

    public LclBookingAc findByBlueScreenChargeCode(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        criteria.add(Restrictions.ne("apAmount", new BigDecimal(0.00)));
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return (LclBookingAc) criteria.setMaxResults(1).uniqueResult();
    }

    public LclBookingAc findByBlueScreenCode(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return (LclBookingAc) criteria.setMaxResults(1).uniqueResult();
    }

    public LclBlAc findBlAcByBlueScreenCode(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return (LclBlAc) criteria.setMaxResults(1).uniqueResult();
    }

    public LclBookingAc getFirstLclCostByBluescreenChargeCode(Long fileId, String chargeCode) throws Exception {
        LclBookingAc lclBookingAc = null;
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        List list = criteria.list();
        if (!list.isEmpty()) {
            lclBookingAc = (LclBookingAc) list.get(0);
        }
        return lclBookingAc;
    }

    public List<LclBookingAc> getAllLclCostByBluescreenChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        return criteria.list();
    }

    public List<LclBookingAc> getAllLclCostByBluescreenChargeCodeME(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", chargeCode));
        }
        criteria.add(Restrictions.ne("deleted", true));
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return criteria.list();
    }

    public List<LclBookingAc> getAllLclChargesByChargeCodeME(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        return criteria.list();
    }

    public List<LclBookingAc> getListWithoutParticularChargeCode(Long fileId, Boolean manualEntry, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.ne("glMapping.chargeCode", chargeCode));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclBookingAc> getWithoutOcnFrtManualRatesList(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.ne("glMapping.chargeCode", "OCNFRT"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<LclBookingAc> getLclCostByFileNumberAsc(Long fileId, String moduleName) throws Exception {
        String chargeCode = "";
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            chargeCode = LclCommonConstant.CHARGE_CODE_DOOR;
        } else {
            chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        }
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List denseCargoList = getCFAList(fileId, "0251", false);
        autoChargesList.addAll(denseCargoList);
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

    public List<LclBookingAc> getChargeCodeList(Long fileId, String chargeCode, Boolean manualEntry) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(" FROM LclBookingAc lba LEFT JOIN fetch lba.arglMapping gm ");
        query.append(" WHERE lba.lclFileNumber.id=?0 AND lba.manualEntry =?1 AND gm.chargeCode =?2 AND (lba.arAmount != 0.00 OR lba.apAmount !=0.00) ORDER BY lba.id ASC");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("0", fileId);
        hqlQuery.setParameter("1", manualEntry);
        hqlQuery.setParameter("2", chargeCode);
        List list = hqlQuery.list();
        getSession().flush();
        return list;
    }

    public List<LclBookingAc> getLclChargeByFileNumberAsc(Long fileId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List pickupList = getCFAList(fileId, "0012", false);
        autoChargesList.addAll(pickupList);
        List manualChargesList = getLclChargeByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        // List TTList = getCFAList(fileId, "0059", false);
        // autoChargesList.addAll(TTList);
        List CAFList = getCFAList(fileId, "0005", false);
        autoChargesList.addAll(CAFList);
        List CAFList95 = getCFAList(fileId, "0095", false);
        autoChargesList.addAll(CAFList95);
        return autoChargesList;
    }

    public List<LclBookingAc> getLclCostWithoutCAF(Long fileId) throws Exception {
        List autoChargesList = getLclCostByFileNumber(fileId, false);
        List pickupList = getCFAList(fileId, "0012", false);
        autoChargesList.addAll(pickupList);
        List manualChargesList = getLclCostByFileNumberME(fileId, true);
        autoChargesList.addAll(manualChargesList);
        return autoChargesList;
    }

    public List<LclBookingAc> getLclCostByFileNumberME(Long fileId, Boolean manualEntry) throws Exception {
        String queryString = "from LclBookingAc where lclFileNumber.id=:fileId and manualEntry =:manualEntry  and (arAmount != 0.00 OR apAmount !=0.00) order by id asc";
        Query query = getSession().createQuery(queryString);
        query.setParameter("fileId", fileId);
        query.setParameter("manualEntry", manualEntry);
        List list = query.list();
        return list;
    }

    public List<LclBookingAc> getArAmount(Long fileId, Boolean manualEntry) throws Exception {
        String queryString = "from LclBookingAc where lclFileNumber.id=:fileId and manualEntry =:manualEntry  and arAmount != 0.00 order by id asc";
        Query query = getSession().createQuery(queryString);
        query.setParameter("fileId", fileId);
        query.setParameter("manualEntry", manualEntry);
        List list = query.list();
        return list;
    }

    public List<LclBookingAc> getByFileNumberME(Long fileId, Boolean manualEntry) throws Exception {
        String queryString = "from LclBookingAc where lclFileNumber.id='" + fileId + "' and manualEntry =" + manualEntry + " and (arAmount != 0.00 OR apAmount !=0.00) order by id asc";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<LclBookingAc> getLclChargeByFileNumberME(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.gt("arAmount", BigDecimal.ZERO));
        criteria.addOrder(Order.asc("id"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<LclBookingAc> getRatesByBlueScreenCode(Long fileId, Boolean manualEntry, List chargeCodeList) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" FROM LclBookingAc lba LEFT JOIN fetch lba.arglMapping gm ");
        queryBuilder.append("  WHERE lba.lclFileNumber.id=:fileId AND lba.manualEntry =:manual AND ");
        queryBuilder.append("  gm.blueScreenChargeCode IN(:blueScreenList) ");
        queryBuilder.append("  order by lba.id asc ");
        Query query = getSession().createQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("manual", manualEntry);
        query.setParameterList("blueScreenList", chargeCodeList);
        List chargeList = query.list();
        return chargeList;
    }

    public List<LclBookingAc> getLclCostByFileNumber(Long fileId, Boolean manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" FROM LclBookingAc lba LEFT JOIN fetch lba.arglMapping gm WHERE lba.lclFileNumber.id=?0 ");
        queryBuilder.append(" AND lba.manualEntry =?1 AND (lba.arAmount != 0.00 OR lba.apAmount !=0.00) ");
        queryBuilder.append(" AND gm.blueScreenChargeCode NOT IN ('0005','0095','0251') and gm.chargeCode NOT IN('INLAND','DOORDEL')  order by lba.id asc");
        Query query = getSession().createQuery(queryBuilder.toString());
        query.setParameter("0", fileId);
        query.setParameter("1", manualEntry);
        List chargeList = query.list();
        return chargeList;
    }

    public List<LclBookingAc> getCFAList(Long fileId, String blueScreenChargeCode, Boolean manualEntry) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("FROM LclBookingAc lba LEFT JOIN fetch lba.arglMapping gm ");
        query.append("WHERE lba.lclFileNumber.id=?0 AND lba.manualEntry =?1 AND gm.blueScreenChargeCode =?2 AND (lba.arAmount != 0.00 OR lba.apAmount !=0.00) ORDER BY lba.id ASC");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("0", fileId);
        hqlQuery.setParameter("1", manualEntry);
        hqlQuery.setParameter("2", blueScreenChargeCode);
        List list = hqlQuery.list();
        getSession().flush();
        return list;
    }

    public void deleteLclCostByFileNumber(Long fileId, String moduleName) throws Exception {
        String shipmentType = "", blueScreenCode = "";
        if (moduleName.equalsIgnoreCase(LclCommonConstant.LCL_IMPORT)) {
            shipmentType = "LCLI";
            blueScreenCode = "1612";
        } else {
            shipmentType = "LCLE";
            blueScreenCode = "0012";
        }
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(blueScreenCode, shipmentType, "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_booking_ac where file_number_id=?0 and manual_entry=0 and ar_gl_mapping_id!=?1 and deleted=0";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.executeUpdate();
        }
        GlMapping glmappingDestFee1 = glmappingdao.findByBlueScreenChargeCode(LclCommonConstant.LCL_DESTFEE1_BLUESCREEN_CHARGE_CODE, shipmentType, "AR");
        GlMapping glmappingDestFee2 = glmappingdao.findByBlueScreenChargeCode(LclCommonConstant.LCL_DESTFEE3_BLUESCREEN_CHARGE_CODE, shipmentType, "AR");
         if (glmappingDestFee1 != null && glmappingDestFee1.getId() != null &&  glmappingDestFee2 != null && glmappingDestFee2.getId() != null) {
            String queryString = "DELETE FROM lcl_booking_ac WHERE file_number_id=?0 AND deleted=0 AND (ar_gl_mapping_id=?1  OR ar_gl_mapping_id=?2)";
            Query queryObjectDest = getSession().createSQLQuery(queryString);
            queryObjectDest.setParameter("0", fileId);
            queryObjectDest.setParameter("1", glmappingDestFee1.getId());
            queryObjectDest.setParameter("2", glmappingDestFee2.getId());
            queryObjectDest.executeUpdate();
        }
    }

    public BigDecimal getTotalLclCostByFileNumber(Long fileId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        queryString = "SELECT SUM(ar_amount + adjustment_amount) FROM lcl_booking_ac WHERE file_number_id =" + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total;
    }

    public BigDecimal getPaymentTotalByFileNumber(Long fileId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        queryString = "SELECT SUM(ar_amount + adjustment_amount) FROM lcl_booking_ac WHERE file_number_id =" + fileId + " and ar_bill_to_party IN ('C','N','T')";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total;
    }

    public String getTotalLclCostAmountByFileNumber(Long fileId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        try {
            queryString = "SELECT SUM(ap_amount) FROM lcl_booking_ac WHERE file_number_id =" + fileId;
            Query queryObject = getSession().createSQLQuery(queryString);
            Object o = queryObject.uniqueResult();
            if (o != null) {
                total = (BigDecimal) o;
            }

        } catch (Exception e) {
            throw e;
        }
        return total.toString();
    }

    public String getTotalLclCostAmountByFileList(List fileId) throws Exception {
        String total = "";
        String queryString;
        try {
            queryString = "SELECT SUM(ap_amount) FROM lcl_booking_ac WHERE file_number_id in (:fileId) ";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameterList("fileId", fileId);
            total = queryObject.uniqueResult() != null ? queryObject.uniqueResult().toString() : "";
        } catch (Exception e) {
            throw e;
        }
        return total;
    }

    public Double getTotalLclCostWithoutParticularCharge(Long fileId, Integer arGlMappingId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        queryString = "SELECT SUM(ar_amount) FROM lcl_booking_ac WHERE file_number_id =" + fileId + " AND ar_gl_mapping_id!=" + arGlMappingId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total.doubleValue();
    }

    public Double getTotalLclBlCostWithoutParticularCharge(Long fileId, Integer arGlMappingId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString;
        queryString = "SELECT SUM(ar_amount) FROM lcl_bl_ac WHERE file_number_id =" + fileId + " AND ar_gl_mapping_id!=" + arGlMappingId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total.doubleValue();
    }

    public void deleteManualAutoRates(Long fileId) throws Exception {
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0012", "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_booking_ac where file_number_id=?0 and ar_gl_mapping_id!=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.executeUpdate();
        }
    }

    public LclBookingAc manaualChargeValidate(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        LclBookingAc lclBookingAc = null;
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        criteria.add(Restrictions.ne("deleted", true));
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBookingAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        List<LclBookingAc> lclBookingAcList = criteria.list();
        if (CommonUtils.isNotEmpty(lclBookingAcList)) {
            lclBookingAc = lclBookingAcList.get(0);
        }
        return lclBookingAc;
    }

    public LclBlAc manaualBlChargeValidate(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        LclBlAc lclBlAc = null;
        Criteria criteria = getSession().createCriteria(LclBlAc.class, "lclBlAc");
        criteria.createAlias("lclBlAc.arglMapping", "glMapping");
       // criteria.add(Restrictions.ne("deleted", true));
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBlAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        List<LclBlAc> lclBlAcList = criteria.list();
        if (CommonUtils.isNotEmpty(lclBlAcList)) {
            lclBlAc = lclBlAcList.get(0);
        }
        return lclBlAc;
    }

    public LclBookingAc getCost(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.apglMapping", "glMapping");
        criteria.add(Restrictions.eq("deleted", false));
        criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        criteria.add(Restrictions.eq("lclBookingAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (LclBookingAc) criteria.setMaxResults(1).uniqueResult();
    }

    public LclBookingAc getLclBookingAcByChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
//        criteria.add(Restrictions.ne("deleted", true));
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (LclBookingAc) criteria.setMaxResults(1).uniqueResult();
    }

    public LclBookingAc getLclBookingAcByBillToParty(Long fileId, String chargeCode, String invoiceNumber) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        criteria.add(Restrictions.eq("spReferenceNo", invoiceNumber));
        criteria.add(Restrictions.eq("arBillToParty", "A"));
        criteria.add(Restrictions.gt("arAmount", BigDecimal.ZERO));
        return (LclBookingAc) criteria.setMaxResults(1).uniqueResult();
    }

//    public List<LclBookingAc> getAgentInvoiceCharges(Long fileId, String chargeCode, String invoiceNumber) throws Exception {
//        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
//        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
//        if (!CommonUtils.isEmpty(fileId)) {
//            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
//        }
//        if (CommonUtils.isNotEmpty(chargeCode)) {
//            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
//        }
//        criteria.add(Restrictions.eq("spReferenceNo", invoiceNumber));
//        criteria.add(Restrictions.eq("arBillToParty", "A"));
//        criteria.add(Restrictions.gt("arAmount", BigDecimal.ZERO));
//        return criteria.list();
//    }
    public List getAgentInvoiceChargesAmount(Long fileId, String chargeCode, String invoiceNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT l.ar_amount FROM lcl_booking_ac l JOIN gl_mapping g ON g.id=l.ar_gl_mapping_id WHERE ");
        queryBuilder.append(" l.file_number_id='").append(fileId);
        queryBuilder.append("' AND l.sp_reference_no='").append(invoiceNumber);
        queryBuilder.append("' AND g.charge_code='").append(chargeCode);
        queryBuilder.append("' AND l.ar_bill_to_party='A' AND l.ar_amount >0.0");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return queryObject.list();
    }

    public List<LclBookingAc> getLclBookingAcListByChargeCode(Long fileId, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.ne("deleted", true));
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (List<LclBookingAc>) criteria.list();
    }

    public String[] getoverWeightCharge(String podUnlocationCode) throws Exception {
        String weightValues[] = new String[2];
        String queryString = "SELECT ipc.over_weight_limit_20, ipc.over_weight_limit_40 FROM ports p LEFT JOIN import_port_configuration ipc "
                + "ON ipc.schnum=p.id WHERE p.unlocationcode='" + podUnlocationCode + "'";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                weightValues[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                weightValues[1] = agentObj[1].toString();
            }
        }
        return weightValues;
    }

    public LclImpAlarmRevenueCostBean getLclAlaramsRevanueAndCost(Long fileId, String chargeCode) throws Exception {
        LclImpAlarmRevenueCostBean revAlaramObj = getLclAlaramsRevanue(fileId, chargeCode);
        LclImpAlarmRevenueCostBean costAlaramObj = getLclAlaramsCost(fileId, chargeCode);
        LclImpAlarmRevenueCostBean alarmObject = new LclImpAlarmRevenueCostBean();
        if (revAlaramObj != null || costAlaramObj != null) {
            if (revAlaramObj != null) {
                alarmObject.setFileNumber(revAlaramObj.getFileNumber());
                alarmObject.setChargeCode(revAlaramObj.getChargeCode());
                alarmObject.setRevenueAmount(revAlaramObj.getRevenueAmount());
            }
            if (costAlaramObj != null) {
                alarmObject.setFileNumber(costAlaramObj.getFileNumber());
                alarmObject.setCostCode(costAlaramObj.getCostCode());
                alarmObject.setCostAmount(costAlaramObj.getCostAmount());
            }
        } else {
            alarmObject = null;
        }
        return alarmObject;
    }

    public LclImpAlarmRevenueCostBean getLclAlaramsRevanue(Long fileId, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT f.file_number AS fileNumber , SUM(IF(f.`status` != 'M' OR (ac.`sp_reference_no` IS NOT NULL AND ac.`ar_bill_to_party` = 'A' AND agent.ready_to_post != 'M') OR (ac.`sp_reference_no` IS NULL AND ac.`ar_bill_to_party` = 'A'),0.0,ac.ar_amount )) AS revenueAmount,ar.charge_code AS chargeCode");
        queryBuilder.append(" FROM lcl_booking_ac ac JOIN lcl_file_number f ON ac.file_number_id = f.id");
        queryBuilder.append(" LEFT JOIN gl_mapping ar ON ac.ar_gl_mapping_id = ar.id");
        queryBuilder.append(" LEFT JOIN");
        queryBuilder.append(" (SELECT ar.invoice_number,arc.lcl_dr_number,IF(ar.`ready_to_post` IS NULL,'',ar.`ready_to_post`) AS ready_to_post ");
        queryBuilder.append(" FROM ar_red_invoice ar LEFT JOIN ar_red_invoice_charges arc ON arc.ar_red_invoice_id = ar.id GROUP BY ar.invoice_number) AS agent ");
        queryBuilder.append(" ON agent.invoice_number = ac.sp_reference_no AND agent.lcl_dr_number LIKE CONCAT('%', f.file_number, '%') ");
        queryBuilder.append(" WHERE /*ac.deleted != 1 AND */ (ac.ap_amount <> 0 OR ac.ar_amount <> 0) ");
        queryBuilder.append(" AND (ar.charge_code IN ('");
        queryBuilder.append(chargeCode);
        queryBuilder.append("')) AND f.id =  ");
        queryBuilder.append(fileId);
        queryBuilder.append(" GROUP BY ar.id ; ");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setResultTransformer(Transformers.aliasToBean(LclImpAlarmRevenueCostBean.class));
        queryObject.addScalar("fileNumber", StringType.INSTANCE);
        queryObject.addScalar("revenueAmount", StringType.INSTANCE);
        queryObject.addScalar("chargeCode", StringType.INSTANCE);
        LclImpAlarmRevenueCostBean alarmObject;
        if (queryObject.list().size() > 0) {
            alarmObject = (LclImpAlarmRevenueCostBean) queryObject.list().get(0);
        } else {
            alarmObject = null;
        }
        return alarmObject;
    }

    public LclImpAlarmRevenueCostBean getLclAlaramsCost(Long fileId, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT f.file_number AS fileNumber ,IF(SUM(ac.ap_amount) IS NULL ,0.00,SUM(ac.ap_amount)) AS CostAmount,ap.charge_code AS costCode");
        queryBuilder.append(" FROM lcl_booking_ac ac JOIN lcl_file_number f ON ac.file_number_id = f.id");
        queryBuilder.append(" LEFT JOIN gl_mapping ap ON ac.ap_gl_mapping_id = ap.id");
        queryBuilder.append(" WHERE /*ac.deleted != 1 AND */ (ac.ap_amount <> 0 OR ac.ar_amount <> 0) ");
        queryBuilder.append(" AND (ap.charge_code IN ('");
        queryBuilder.append(chargeCode);
        queryBuilder.append("')) AND f.id =  ");
        queryBuilder.append(fileId);
        queryBuilder.append(" GROUP BY ap.id ; ");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setResultTransformer(Transformers.aliasToBean(LclImpAlarmRevenueCostBean.class));
        queryObject.addScalar("fileNumber", StringType.INSTANCE);
        queryObject.addScalar("CostAmount", StringType.INSTANCE);
        queryObject.addScalar("costCode", StringType.INSTANCE);
        LclImpAlarmRevenueCostBean alarmObject;
        if (queryObject.list().size() > 0) {
            alarmObject = (LclImpAlarmRevenueCostBean) queryObject.list().get(0);
        } else {
            alarmObject = null;
        }
        return alarmObject;
    }

    public int deleteRatesByChargeCode(Long fileId, String chargeCode) throws Exception {
        int deletedRows = 0;
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(chargeCode, "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_booking_ac where file_number_id=?0 and ar_gl_mapping_id=?1";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            deletedRows = queryObject.executeUpdate();
        }
        return deletedRows;
    }

    public int deleteRatesByChargeCodeAndManualEntry(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        int deletedRows = 0;
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(chargeCode, "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_booking_ac where file_number_id=?0 and ar_gl_mapping_id=?1 and manual_entry=?2";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.setParameter("2", manualEntry);
            deletedRows = queryObject.executeUpdate();
        }
        return deletedRows;
    }

    public int deleteRatesByGlmappingChargeCodeAndManualEntry(Long fileId, String chargeCode, boolean manualEntry) throws Exception {
        int deletedRows = 0;
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByChargeCode(chargeCode, "LCLE", "AR");
        if (glmapping != null && glmapping.getId() != null) {
            String queryString = "delete from lcl_booking_ac where file_number_id=?0 and ar_gl_mapping_id=?1 and manual_entry=?2";
            Query queryObject = getSession().createSQLQuery(queryString);
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", glmapping.getId());
            queryObject.setParameter("2", manualEntry);
            deletedRows = queryObject.executeUpdate();
        }
        return deletedRows;
    }

    public LclBookingAc getLclCostByChargeCodeBookingPiece(Long fileId, String bluescreenChargeCode, boolean manualEntry, Long bookingPieceId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        criteria.createAlias("lclBookingAc.lclBookingPiece", "lclBookingPiece");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBookingAc.manualEntry", manualEntry));
        if (!CommonUtils.isEmpty(bluescreenChargeCode)) {
            criteria.add(Restrictions.eq("glMapping.blueScreenChargeCode", bluescreenChargeCode));
        }
        if (!CommonUtils.isEmpty(bookingPieceId)) {
            criteria.add(Restrictions.eq("lclBookingPiece.id", bookingPieceId));
        }
        return (LclBookingAc) criteria.uniqueResult();
    }

    public List<LclBookingAc> findByFileAndCommodityList(Long fileNumberId, Long bookingPieceId) throws Exception {
        String queryString = "from LclBookingAc where lclFileNumber='" + fileNumberId + "' and lclBookingPiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<Object[]> getBookingSpotRateCharge(Long fileId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT SUM(IF(gl.`Charge_code` <> 'OCNFRT',ac.rate_per_volume_unit,0))  AS volume, ");
        queryString.append(" SUM(IF(gl.`Charge_code` <> 'OCNFRT',ac.rate_per_weight_unit,0)) AS weight, ");
        queryString.append(" SUM(IF(gl.`Charge_code` = 'OCNFRT',ac.rate_per_volume_unit,0))  AS ocenfrt_volume, ");
        queryString.append(" SUM(IF(gl.`Charge_code` = 'OCNFRT',ac.rate_per_weight_unit,0)) AS ocenfrt_weight ");
        queryString.append(" FROM lcl_booking_ac ac JOIN gl_mapping gl ON gl.`id` = ac.`ar_gl_mapping_id` ");
        queryString.append(" WHERE manual_entry = 0 AND file_number_id =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("fileId", fileId);
        List<Object[]> result_list = query.list();
        return result_list;
    }

    public BigDecimal getTotalIPICost(Long fileId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT SUM(ar_amount) FROM lcl_booking_ac ipchg JOIN gl_mapping gp ON gp.id = ipchg.ar_gl_mapping_id AND gp.Charge_code =");
        queryString.append("'IPI' AND gp.Transaction_Type = 'AR' and file_number_id =  ?0");
        Query queryObject = getSession().createSQLQuery(queryString.toString());
        queryObject.setParameter("0", fileId);
        return (BigDecimal) queryObject.uniqueResult();
    }

    public Double getTotalWithoutParticularChargeCode(Long fileId, String chargeCode) throws Exception {
        StringBuilder queryString = new StringBuilder();
        BigDecimal total = new BigDecimal(0.00);
        queryString.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_booking_ac lbac JOIN gl_mapping gp ON gp.id = lbac.ar_gl_mapping_id AND gp.Charge_code !='");
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

    public Double getBlTotalWithoutParticularChargeCode(Long fileId, String chargeCode) throws Exception {
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

    public LclBookingAc getTTCharges(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.add(Restrictions.like("glMapping.chargeCode", "TT%"));
        return (LclBookingAc) criteria.uniqueResult();
    }

    public void updateBillToPartyForCharges(Long fileId, String billToParty, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_ac set ar_bill_to_party = '");
        queryBuilder.append(billToParty);
        queryBuilder.append("',modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(",modified_datetime = SYSDATE() where file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" and ar_amount > 0.00");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateInvoiceNumberForCharges(String invoiceNumber, String ids, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_ac set sp_reference_no = '");
        queryBuilder.append(invoiceNumber);
        queryBuilder.append("',modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(",modified_datetime = SYSDATE() where id IN(");
        queryBuilder.append(ids).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateBillToPartyChargesME(Long fileId, String billToParty, Integer userId, String manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
//        String glMappingId = new GlMappingDAO().getglMappingIdUsingBlueScreenCode(ConstantsInterface.LCL_IMP_BLUESCREEN_CHARGE_CODE, "LCLI",
//                TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        sb.append("update lcl_booking_ac set ar_bill_to_party = '").append(billToParty).append("',modified_by_user_id = ");
        sb.append(userId).append(",modified_datetime = SYSDATE() ");
        if (billToParty.equalsIgnoreCase("A")) {
            sb.append(",rels_to_inv = true");
        } else {
            sb.append(",rels_to_inv = false");
        }
        if (billToParty.equalsIgnoreCase("W")) {
            sb.append(",post_ar = false ");
        }
        sb.append(" where file_number_id = ").append(fileId).append(" and ar_amount > 0.00");
        sb.append("and (sp_reference_no IS NULL OR sp_reference_no='') ");
//        sb.append(" and ar_gl_mapping_id NOT IN (").append(glMappingId).append(")");
//        if (!manualEntry.equals("")) {
//            sb.append(" and manual_entry= ").append(manualEntry);
//        }
//        if (manualEntry.equals("")) {
//            sb.append(" AND rels_to_inv=").append(false);
//        }
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List<BookingChargesBean> findBybookingAcId(String fileNumberId, List<String> billToPartyList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o.id AS id,o.chargeCode AS chargeCode,SUM(o.totalAmt) AS totalAmt, ");
        sb.append("SUM(o.paidAmt) AS paidAmt,SUM(o.balanceAmt) AS balanceAmt,o.arBillToParty AS arBillToParty FROM ");
        sb.append("(SELECT t.id AS id,remove_special_characters(t.chargeCode) AS chargeCode,t.totalAmt AS totalAmt, ");
        sb.append(" IF(lbcta.amount IS NULL,0.00,SUM(lbcta.amount)) AS paidAmt, ");
        sb.append(" IF(lbcta.amount IS NULL,SUM(t.totalAmt),(t.totalAmt) - SUM(lbcta.amount)) AS balanceAmt,t.arBillToParty AS arBillToParty FROM  ");
        sb.append(" (SELECT lbc.id AS id,IF(gm.charge_description = '' OR gm.charge_description IS NULL,gm.charge_code,gm.charge_description) AS chargeCode, ");
        sb.append(" SUM(lbc.ar_amount + lbc.adjustment_amount) AS totalAmt,lbc.`ar_bill_to_party` AS arBillToParty FROM lcl_booking_ac lbc ");
        sb.append(" JOIN gl_mapping gm ON gm.id=lbc.ar_gl_mapping_id ");
        sb.append(" WHERE lbc.file_number_id=:fileId ");
        sb.append(" AND lbc.ar_amount <> 0.00 AND lbc.ar_amount IS NOT NULL AND lbc.ar_bill_to_party IN (:billTo) GROUP BY id) t ");
        sb.append(" LEFT JOIN lcl_booking_ac_ta lbcta ON lbcta.lcl_booking_ac_id = t.id GROUP BY t.id) o GROUP BY chargeCode,arBillToParty");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileNumberId);
        query.setParameterList("billTo", billToPartyList);
        query.setResultTransformer(Transformers.aliasToBean(BookingChargesBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("arBillToParty", StringType.INSTANCE);
        query.addScalar("totalAmt", BigDecimalType.INSTANCE);
        query.addScalar("paidAmt", BigDecimalType.INSTANCE);
        query.addScalar("balanceAmt", BigDecimalType.INSTANCE);
        List<BookingChargesBean> chargeList = query.list();
        return chargeList;
    }

    public List<BookingChargesBean> findBybookingAcWarehouse(String fileNumberId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lbc.id as id,IF(gm.charge_description='' OR gm.charge_description IS NULL,gm.charge_code ,gm.charge_description) AS chargeCode,");
        sb.append("(SUM(lbc.ar_amount+lbc.adjustment_amount)) AS totalAmt,IF(lbcta.amount IS NULL ,0.00,SUM(lbcta.amount)) AS paidAmt, ");
        sb.append("IF(lbcta.amount IS NULL ,(SUM(lbc.ar_amount+lbc.adjustment_amount)),(SUM(lbc.ar_amount+lbc.adjustment_amount))- SUM(lbcta.amount)) as balanceAmt,lbc.`ar_bill_to_party` AS arBillToParty, ");
        sb.append(" gm.bluescreen_chargecode AS blueScreencode FROM lcl_booking_ac lbc ");
        sb.append("LEFT JOIN lcl_booking_ac_ta lbcta ON lbcta.lcl_booking_ac_id = lbc.id ");
        sb.append("JOIN gl_mapping gm ON gm.id=lbc.ar_gl_mapping_id ");
        sb.append("WHERE lbc.file_number_id IN(").append(fileNumberId).append(")");
        sb.append(" AND lbc.ar_amount <> 0.00 AND lbc.ar_amount IS NOT NULL AND lbc.ar_bill_to_party ='W'").append(" GROUP BY chargeCode");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(BookingChargesBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("arBillToParty", StringType.INSTANCE);
        query.addScalar("totalAmt", BigDecimalType.INSTANCE);
        query.addScalar("paidAmt", BigDecimalType.INSTANCE);
        query.addScalar("balanceAmt", BigDecimalType.INSTANCE);
        query.addScalar("blueScreencode", StringType.INSTANCE);
        List<BookingChargesBean> chargeList = query.list();
        return chargeList;
    }

    public void updateCharges(String id, String flag, String index) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(id)) {
            queryBuilder.append("update lcl_booking_ac set ");
            if (index.equals("B")) {
                queryBuilder.append("bundle_into_of  = '");
            } else if (index.equals("R")) {
                queryBuilder.append("rels_to_inv  = '");
            }
            queryBuilder.append(flag).append("' where id=").append(id);
            Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
            queryObject.executeUpdate();
        }
    }

    public List getAllBillToPartyImp() throws Exception {
        List billToPartyList = new ArrayList();
        billToPartyList.add(new LabelValueBean("Select", ""));
        billToPartyList.add(new LabelValueBean("Consignee", "C"));
        billToPartyList.add(new LabelValueBean("Agent", "A"));
        billToPartyList.add(new LabelValueBean("Third Party", "T"));
        billToPartyList.add(new LabelValueBean("Notify Party", "N"));
        billToPartyList.add(new LabelValueBean("Warehouse", "W"));
        return billToPartyList;
    }

    public List getBillToPartyForEculine() throws Exception {
        List billToPartyList = new ArrayList();
        billToPartyList.add(new LabelValueBean("Consignee", "C"));
        billToPartyList.add(new LabelValueBean("Third Party", "T"));
        billToPartyList.add(new LabelValueBean("Notify Party", "N"));
        return billToPartyList;
    }

    public List getAllBillToPartyExp() throws Exception {
        List billToPartyList = new ArrayList();
        billToPartyList.add(new LabelValueBean("Forwarder", "F"));
        billToPartyList.add(new LabelValueBean("Shipper", "S"));
        billToPartyList.add(new LabelValueBean("Third Party", "T"));
        billToPartyList.add(new LabelValueBean("Agent", "A"));
        return billToPartyList;
    }

    public List getAllBillToPartyBkg(String bType) throws Exception {
        List billToPartyList = new ArrayList();
        if (bType.equalsIgnoreCase("C")) {
            billToPartyList.add(new LabelValueBean("Agent", "A"));
        } else if (bType.equalsIgnoreCase("P")) {
            billToPartyList.add(new LabelValueBean("Forwarder", "F"));
            billToPartyList.add(new LabelValueBean("Third Party", "T"));
            billToPartyList.add(new LabelValueBean("Shipper", "S"));
        } else if (bType.equalsIgnoreCase("B")) {
            billToPartyList.add(new LabelValueBean("Forwarder", "F"));
            billToPartyList.add(new LabelValueBean("Third Party", "T"));
            billToPartyList.add(new LabelValueBean("Shipper", "S"));
            billToPartyList.add(new LabelValueBean("Agent", "A"));
        }
        return billToPartyList;
    }

    public Object getIdMebyFileNumberAndChargeId(Long fileId, Integer chargeId, String amount, String operation, String newAmount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id from lcl_booking_ac where file_number_id = ?0 and ar_gl_mapping_id = ?1 AND ar_amount = ");
        queryBuilder.append(amount);
        queryBuilder.append(" LIMIT 1");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", chargeId);
        Object instance = (Object) queryObject.uniqueResult();
        if (instance == null && operation.equalsIgnoreCase("R")) {
            queryBuilder = new StringBuilder();
            queryBuilder.append("select id from lcl_booking_ac where file_number_id = ?0 and ar_gl_mapping_id = ?1 AND ");
            queryBuilder.append("ar_amount  = ");
            queryBuilder.append(newAmount);
            queryBuilder.append(" LIMIT 1");
            queryObject = getSession().createSQLQuery(queryBuilder.toString());
            queryObject.setParameter("0", fileId);
            queryObject.setParameter("1", chargeId);
            instance = (Object) queryObject.uniqueResult();
        }
        return instance;
    }

    public void updateChargesForApproval(String bookingAcId, Integer userId, Object[] instance) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_ac set ar_amount = ar_amount + ");
        queryBuilder.append(instance[1].toString());
        queryBuilder.append(",ar_bill_to_party = '");
        queryBuilder.append(instance[2].toString());
        queryBuilder.append("',modified_datetime=SYSDATE(), modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(" where id = ");
        queryBuilder.append(bookingAcId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateChargesForReversal(String bookingAcId, Integer userId, Object[] instance) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_ac set ar_amount = ");
        queryBuilder.append(instance[1].toString());
        queryBuilder.append(",ar_bill_to_party = '");
        queryBuilder.append(instance[3].toString());
        queryBuilder.append("',modified_datetime = SYSDATE(), modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(" where id = ");
        queryBuilder.append(bookingAcId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void insertChargesForApproval(Long fileId, String arGlMappingId, Integer userId, String arAmount, String arBillToParty) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_booking_ac (file_number_id,manual_entry,trans_datetime,adjustment_amount,ar_amount,ar_gl_mapping_id,");
        queryBuilder.append("ar_bill_to_party,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (");
        queryBuilder.append(fileId).append(",1,SYSDATE(),0.00,").append(arAmount).append(",");
        queryBuilder.append(arGlMappingId).append(",'");
        queryBuilder.append(arBillToParty);
        queryBuilder.append("',");
        queryBuilder.append("SYSDATE(),");
        queryBuilder.append(userId);
        queryBuilder.append(",SYSDATE(),");
        queryBuilder.append(userId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteChargesById(String bookingAcId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_booking_ac where id IN ( ");
        queryBuilder.append(bookingAcId).append(")");
        queryBuilder.append(" and (sp_reference_no is null or sp_reference_no = '') ");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public BigDecimal getTotalAmountByCollect(Long fileId, String billToCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_booking_ac WHERE file_number_id = ").append(fileId);
        sb.append(" and ar_bill_to_party is not null");
        sb.append(" and ar_bill_to_party!= '").append(billToCode).append("'");
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public BigDecimal getTotalAmountByPrepaid(Long fileId, String billToCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ar_amount + adjustment_amount)  FROM lcl_booking_ac WHERE file_number_id = ").append(fileId);
        sb.append(" and ar_bill_to_party= '").append(billToCode).append("'");
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public BigDecimal getTotalAmtByARBillToParty(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_booking_ac WHERE file_number_id = ").append(fileId);
        sb.append(" AND ar_bill_to_party IN('N','C','T')");
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public Integer getIdByGlmapping(Long fileId, Integer manualEntry, String chargeCode,
            String shipmentType, String transationType) throws Exception {
        BigInteger bookingAcid = new BigInteger("0");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lbac.id FROM lcl_booking_ac lbac JOIN gl_mapping gm ON gm.id = lbac.ap_gl_mapping_id ");
        queryBuilder.append("WHERE file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND manual_entry = ");
        queryBuilder.append(manualEntry);
        queryBuilder.append(" AND gm.Charge_code = '");
        queryBuilder.append(chargeCode);
        queryBuilder.append("' AND gm.Transaction_Type = '");
        queryBuilder.append(transationType);
        queryBuilder.append("' AND gm.Shipment_Type = '");
        queryBuilder.append(shipmentType);
        queryBuilder.append("'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            bookingAcid = (BigInteger) o;
        }
        return bookingAcid.intValue();
    }

    public BigDecimal getTotalCostAmt(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ap_amount) FROM lcl_booking_ac WHERE file_number_id = ").append(fileId);
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String getConcatenetedBookingAcIds(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(id) USING utf8) AS bookingAcIds FROM lcl_booking_ac WHERE file_number_id = ");
        sb.append(fileId);
        sb.append(" AND ap_amount > 0.00 and manual_entry = 0");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String getBkgAcIdsWithoutApStatus(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT GROUP_CONCAT(ac.id) FROM lcl_booking_ac ac ");
        sb.append("LEFT JOIN lcl_booking_ac_ta ta ON ta.`lcl_booking_ac_id` = ac.`id` ");
        sb.append("LEFT JOIN lcl_booking_ac_trans t ON t.id = ta.lcl_booking_ac_trans_id ");
        sb.append("LEFT JOIN gl_mapping gm ON gm.id = ac.ar_gl_mapping_id ");
        sb.append("WHERE ac.file_number_id = :fileId  AND ac.manual_entry = 0 AND (");
        sb.append("(t.trans_type NOT IN ('AP', 'IP', 'DS') OR t.`trans_type` IS NULL) ");
        sb.append("AND (ac.`sp_reference_no` IS NULL OR ac.`sp_reference_no` = '')) AND gm.bluescreen_chargecode NOT IN ('0012','1612','0005','0095') ");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setLong("fileId", fileId);
        return (String) query.uniqueResult();
    }

    public LclBookingAc getBookingAc(Long fileId, Boolean manualEntry) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("manualEntry", manualEntry));
        criteria.createAlias("lclBookingAc.arglMapping", "arglMapping");
        criteria.add(Restrictions.eq("arglMapping.destinationServices", true));
        return (LclBookingAc) criteria.uniqueResult();
    }

    public void deleteChargesByFileIdAndArGlMappingId(Long fileId, String arGlMappingId, String manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_booking_ac where file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" and ar_gl_mapping_id IN( ");
        queryBuilder.append(arGlMappingId);
        queryBuilder.append(") and manual_entry =  ");
        queryBuilder.append(manualEntry);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateSpRefNo(String invoiceNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE lcl_booking_ac SET sp_reference_no=NULL ");
        queryBuilder.append(" WHERE sp_reference_no='").append(invoiceNo).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public double getArAmount(Integer id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ar_amount FROM lcl_booking_ac WHERE id = ").append(id);
        return ((BigDecimal) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult()).doubleValue();
    }

    public String isChargeCodeValidate(String fileId, String chargeCode, String manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM lcl_booking_ac lbc ");
        sb.append("JOIN gl_mapping gm ON gm.id=lbc.ar_gl_mapping_id ");
        sb.append("WHERE lbc.file_number_id = ").append(fileId);
        if (!"".equalsIgnoreCase(chargeCode)) {
            sb.append(" AND gm.charge_code='").append(chargeCode).append("'");
        }
        if (!"".equalsIgnoreCase(manualEntry)) {
            sb.append(" AND manual_entry=").append(manualEntry);
        }
        sb.append(" and ar_amount <> 0.00 ");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public void updateApAmount(Long fileId, BigDecimal apAmt, BigDecimal arAmt, Boolean manualEntry, Boolean deletedValue, Boolean deleted, String moduleName) throws Exception {
        String shipmentType = "", blueScreenCode = "";
        if (moduleName.equalsIgnoreCase(LclCommonConstant.LCL_IMPORT)) {
            shipmentType = "LCLI";
            blueScreenCode = "1612";
        } else {
            shipmentType = "LCLE";
            blueScreenCode = "0012";
        }
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(blueScreenCode, shipmentType, "AR");
        StringBuilder sb = new StringBuilder();
        sb.append("Update lcl_booking_ac set ").append("ar_amount=").append(arAmt).append(",rate_per_unit_uom='FL', ");
        sb.append(" ar_bill_to_party = null,rels_to_inv = 0 ");
        sb.append(" Where file_number_id=").append(fileId);
        sb.append(" AND ap_amount <> 0.00 AND manual_entry=").append(manualEntry);
        sb.append(" AND (sp_reference_no is null or sp_reference_no = '')");
        sb.append(" AND ").append("deleted=").append(deleted);
        if (glmapping != null && glmapping.getId() != null) {
            sb.append(" AND ar_gl_mapping_id!=").append(glmapping.getId());
        }
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void deletetransactionLedger(Long fileId, String fileNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM transaction_ledger WHERE cost_id IN(SELECT id FROM lcl_booking_ac WHERE file_number_id=").append(fileId);
        sb.append(" AND manual_entry=0 AND deleted=1) AND drcpt='").append(fileNumber).append("'");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List<LclBookingAc> getLclChargeCorrectionList(Long fileId, String moduleName, String viewMode) throws Exception {
        String blueScreenChargeCode;
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            blueScreenChargeCode = LclCommonConstant.CHARGE_CODE_DOOR;
        } else {
            blueScreenChargeCode = LclCommonConstant.BLUESCREEN_CHARGE_CODE_INLAND;
        }
        Criteria criteria = getCurrentSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.lclFileNumber", "lclFileNumber");
        criteria.createAlias("lclBookingAc.arglMapping", "arglMapping");
        criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        criteria.add(Restrictions.eq("manualEntry", false));
        criteria.add(Restrictions.eq("postAr", true));
        criteria.add(Restrictions.not(Restrictions.in("arglMapping.blueScreenChargeCode", "0012,1612,0005,0095".split(","))));
        criteria.add(Restrictions.ne("arBillToParty", "A"));
        if (CommonUtils.isEmpty(viewMode)) {
        criteria.add(Restrictions.ne("arAmount", BigDecimal.ZERO));
        }
        List chargeList = criteria.list();
        criteria = getCurrentSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.lclFileNumber", "lclFileNumber");
        criteria.createAlias("lclBookingAc.arglMapping", "arglMapping");
        criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        criteria.add(Restrictions.eq("arglMapping.chargeCode", blueScreenChargeCode));
        criteria.add(Restrictions.eq("manualEntry", false));
        criteria.add(Restrictions.eq("postAr", true));
        criteria.add(Restrictions.ne("arBillToParty", "A"));
        if (CommonUtils.isEmpty(viewMode)) {
        criteria.add(Restrictions.ne("arAmount", BigDecimal.ZERO));
        }
        chargeList.addAll(criteria.list());
        criteria = getCurrentSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.lclFileNumber", "lclFileNumber");
        criteria.createAlias("lclBookingAc.arglMapping", "arglMapping");
        criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        criteria.add(Restrictions.eq("manualEntry", true));
        criteria.add(Restrictions.eq("postAr", true));
        criteria.add(Restrictions.ne("arBillToParty", "A"));
        if (CommonUtils.isEmpty(viewMode)) {
        criteria.add(Restrictions.ne("arAmount", BigDecimal.ZERO));
        }
        chargeList.addAll(criteria.list());
        criteria = getCurrentSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.lclFileNumber", "lclFileNumber");
        criteria.createAlias("lclBookingAc.arglMapping", "arglMapping");
        criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        criteria.add(Restrictions.eq("arglMapping.blueScreenChargeCode", "0005"));
        criteria.add(Restrictions.eq("manualEntry", false));
        criteria.add(Restrictions.eq("postAr", true));
        criteria.add(Restrictions.ne("arBillToParty", "A"));
        if (CommonUtils.isEmpty(viewMode)) {
        criteria.add(Restrictions.ne("arAmount", BigDecimal.ZERO));
        }
        chargeList.addAll(criteria.list());
        criteria = getCurrentSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.lclFileNumber", "lclFileNumber");
        criteria.createAlias("lclBookingAc.arglMapping", "arglMapping");
        criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        criteria.add(Restrictions.eq("arglMapping.blueScreenChargeCode", "0095"));
        criteria.add(Restrictions.eq("manualEntry", false));
        criteria.add(Restrictions.eq("postAr", true));
        criteria.add(Restrictions.ne("arBillToParty", "A"));
        if (CommonUtils.isEmpty(viewMode)) {
        criteria.add(Restrictions.ne("arAmount", BigDecimal.ZERO));
        }
        chargeList.addAll(criteria.list());
        return chargeList;
    }

    public void deleteArAmount(Long fileId, String moduleName) throws Exception {
        String shipmentType = "", blueScreenCode = "";
        if (moduleName.equalsIgnoreCase(LclCommonConstant.LCL_IMPORT)) {
            shipmentType = "LCLI";
            blueScreenCode = "1612";
        } else {
            shipmentType = "LCLE";
            blueScreenCode = "0012";
        }
        GlMappingDAO glmappingdao = new GlMappingDAO();
        GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(blueScreenCode, shipmentType, "AR");
        if (glmapping != null && glmapping.getId() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("delete from lcl_booking_ac where file_number_id=");
            sb.append(fileId).append(" and manual_entry=0 and deleted=0 and (ap_amount is null or ap_amount=0.00) ");
            sb.append(" and (sp_reference_no is null or sp_reference_no = '') ");
            sb.append(" and ar_gl_mapping_id!=").append(glmapping.getId());
            Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
            queryObject.executeUpdate();
        }
    }

    public void deleteLclBookingAcTa(Integer bookingAcId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_booking_ac_ta");
        queryBuilder.append(" where lcl_booking_ac_id = '").append(bookingAcId).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void deleteBkgAcByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingAc where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getTransType(Long fileNumberId, Long id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select trans_type FROM lcl_booking_ac_trans where file_number_id=").append(fileNumberId);
        queryBuilder.append(" and id=").append(id);
        return (String) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getIpiBkgAcIds(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT GROUP_CONCAT(ac.id) FROM lcl_booking_ac ac ");
        queryBuilder.append("JOIN gl_mapping gm ON gm.id=ac.ar_gl_mapping_id ");
        queryBuilder.append("WHERE gm.bluescreen_chargecode IN (1607,1617) AND ac.file_number_id=").append(fileNumberId);
        return (String) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List<LclBookingAc> getDestinationChargeList(Long fileId, List blueScreenChargeCode, Boolean manualEntry, Boolean destinationServices) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("FROM LclBookingAc lba LEFT JOIN fetch lba.arglMapping gm ");
        query.append("WHERE lba.lclFileNumber.id=:file_Id AND lba.manualEntry =:manua AND gm.destinationServices =:dest AND gm.chargeCode IN(:blue)  AND (lba.arAmount != 0.00 OR lba.apAmount !=0.00) ORDER BY lba.id ASC");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("file_Id", fileId);
        hqlQuery.setParameter("manua", manualEntry);
        hqlQuery.setParameter("dest", destinationServices);
        hqlQuery.setParameterList("blue", blueScreenChargeCode);
        List list = hqlQuery.list();
        return list;
    }

    public void saveLclBookingAc(Long fileId, Integer arGlMappingId, String arBillToParty, Double arAmount, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("insert");
        queryBuilder.append("  into ");
        queryBuilder.append("lcl_booking_ac (");
        queryBuilder.append("  file_number_id,");
        queryBuilder.append("  manual_entry,");
        queryBuilder.append("  trans_datetime,");
        queryBuilder.append("  adjustment_amount,");
        queryBuilder.append("  ar_amount,");
        queryBuilder.append("  ar_gl_mapping_id,");
        queryBuilder.append("  ar_bill_to_party,");
        queryBuilder.append("  entered_datetime,");
        queryBuilder.append("  entered_by_user_id,");
        queryBuilder.append("  modified_datetime,");
        queryBuilder.append("  modified_by_user_id");
        queryBuilder.append(") ");
        queryBuilder.append("values (");
        queryBuilder.append("  ").append(fileId).append(",");
        queryBuilder.append("  1,");
        queryBuilder.append("  now(),");
        queryBuilder.append("  0.00,");
        queryBuilder.append("  ").append(arAmount).append(",");
        queryBuilder.append("  ").append(arGlMappingId).append(",");
        queryBuilder.append("  '").append(arBillToParty).append("',");
        queryBuilder.append("  now(),");
        queryBuilder.append("  ").append(userId).append(",");
        queryBuilder.append("  now(),");
        queryBuilder.append("  ").append(userId);
        queryBuilder.append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void updateLclBookingAc(Long fileId, Integer arGlMappingId, String oldBillToParty, Double oldAmount, String newBillToParty, Double newAmount, Long lclBookingAcId, User user) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ");
        queryBuilder.append("  lcl_booking_ac ");
        queryBuilder.append("set");
        queryBuilder.append("  ar_bill_to_party = '").append(newBillToParty).append("',");
        queryBuilder.append("  ar_amount = ").append(newAmount).append(", ");
        queryBuilder.append("  modified_by_user_id =").append(user.getUserId());
        queryBuilder.append(" where");
        queryBuilder.append("  file_number_id = ").append(fileId);
        queryBuilder.append("  and ar_gl_mapping_id = ").append(arGlMappingId);
        queryBuilder.append("  and ar_bill_to_party = '").append(oldBillToParty).append("'");
        queryBuilder.append("  and ar_amount = ").append(oldAmount).append(" ");
        if (lclBookingAcId != null && lclBookingAcId != 0) {
            queryBuilder.append(" and id = ").append(lclBookingAcId).append(" ");
        }
        queryBuilder.append("limit 1");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void updatePostArStatus(String chargeId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE lcl_booking_ac SET post_ar=:postArStatus ");
        queryBuilder.append(" WHERE id=:chargeId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setBoolean("postArStatus", true);
        query.setParameter("chargeId", chargeId);
        query.executeUpdate();
    }

    public List<LclBookingAc> getConsolidatedChargeME(List fileList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("from LclBookingAc where lclFileNumber.id IN(:fileList) and manualEntry=1 and arAmount <> 0.00");
        Query query = getCurrentSession().createQuery(sb.toString());
        query.setParameterList("fileList", fileList);
        return (List<LclBookingAc>) query.list();
    }

    public Boolean isValidateRates(String fileId, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*) > 0, TRUE, FALSE) AS result FROM lcl_booking_ac lbc  ");
        queryBuilder.append(" JOIN gl_mapping gm ON (lbc.ar_gl_mapping_id=gm.id AND ");
        queryBuilder.append(" gm.Charge_code=:chargeCode AND gm.shipment_type=:shipType) ");
        queryBuilder.append(" WHERE lbc.file_number_id=:fileId ");
        queryBuilder.append(" AND lbc.ap_amount <> 0.00 AND lbc.ar_amount <> 0.00  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("chargeCode", chargeCode);
        query.setString("shipType", "LCLE");
        query.setString("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public Boolean isValidateAgentCharges(String fileId, String shipmentType, String transcationType, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(lba.sp_reference_no IS NOT NULL,TRUE,FALSE) as result  ");
        queryBuilder.append(" FROM lcl_booking_ac lba WHERE lba.file_number_id = :fileId   ");
        queryBuilder.append(" and ar_bill_to_party='A'  AND lba.ar_gl_mapping_id =  ");
        queryBuilder.append(" (SELECT id FROM gl_mapping WHERE charge_code =:chargeCode    ");
        queryBuilder.append(" AND shipment_type =:shipType AND transaction_type = :transcationType LIMIT 1)  ");
        queryBuilder.append(" ORDER BY lba.id DESC LIMIT 1   ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileId", fileId);
        query.setString("shipType", shipmentType);
        query.setString("transcationType", transcationType);
        query.setString("chargeCode", chargeCode);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Boolean isValidateChargeCode(String fileId, String shipmentType, String billToParty, String transcationType, String chargeCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(count(*)>0,TRUE,FALSE) as result  ");
        queryBuilder.append(" FROM lcl_booking_ac lba WHERE lba.file_number_id = :fileId   ");
        queryBuilder.append(" and ar_bill_to_party=:billToParty  AND lba.ar_gl_mapping_id =  ");
        queryBuilder.append(" (SELECT id FROM gl_mapping WHERE charge_code =:chargeCode    ");
        queryBuilder.append(" AND shipment_type =:shipType AND transaction_type = :transcationType LIMIT 1)  ");
        queryBuilder.append(" ORDER BY lba.id DESC LIMIT 1   ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileId", fileId);
        query.setString("shipType", shipmentType);
        query.setString("billToParty", billToParty);
        query.setString("transcationType", transcationType);
        query.setString("chargeCode", chargeCode);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public BigDecimal getTotalAmountByCollectAll(Long fileId, String billToCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ar_amount + adjustment_amount) FROM lcl_booking_ac WHERE file_number_id = ").append(fileId);
        sb.append(" and ar_bill_to_party is not null");
        sb.append(" and ar_bill_to_party= '").append(billToCode).append("'");
        return (BigDecimal) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public Object getDoorDelStatus(String fileNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  IF(tl.`Charge_Code` != '', 'true', 'false') AS doorDellStatus ");
        sb.append("FROM");
        sb.append("  `transaction_ledger` tl ");
        sb.append("WHERE (tl.`Charge_Code` = 'DOORDEL' ");
        sb.append("AND tl.`Invoice_number` IS NOT NULL)");
        sb.append("  AND tl.`drcpt` = :fileNumber LIMIT 1 ");
        SQLQuery sqlQueryObject = getCurrentSession().createSQLQuery(sb.toString());
        sqlQueryObject.setParameter("fileNumber", fileNumber);
        return sqlQueryObject.uniqueResult() != null ? sqlQueryObject.uniqueResult() : "false";
    }

    public void deleteWarehouseCharges(Long fileId, String billTo, boolean postFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE FROM lcl_booking_ac WHERE ");
        queryBuilder.append(" file_number_id=:fileId AND ar_bill_to_party=:billTo   AND post_ar=:postDr");
        SQLQuery queryObj = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObj.setParameter("fileId", fileId);
        queryObj.setParameter("billTo", billTo);
        queryObj.setParameter("postDr", postFlag);
        queryObj.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public LclBookingAc findByChargeCode(Long fileId, Boolean manualEntry,
            String shipmentType, String chargeCode) throws Exception {
        LclBookingAc lclBookingAc = null;
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" FROM LclBookingAc lba LEFT JOIN fetch lba.arglMapping gm ");
        queryStr.append("  WHERE lba.lclFileNumber.id=:fileId and lba.arAmount <> 0.00 AND lba.manualEntry =:manualEntry ");
        queryStr.append("  AND gm.shipmentType=:shipmentType and gm.chargeCode=:chargeCode  ");
        Query query = getSession().createQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("manualEntry", manualEntry);
        query.setParameter("shipmentType", shipmentType);
        query.setParameter("chargeCode", chargeCode);
        List<LclBookingAc> lclBookingAcList = query.list();
        if (CommonUtils.isNotEmpty(lclBookingAcList)) {
            lclBookingAc = lclBookingAcList.get(0);
        }
        return lclBookingAc;
    }

    public LclBlAc findByBlChargeCode(Long fileId, Boolean manualEntry,
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
        List<LclBlAc> lclBlAcList = query.list();
        if (CommonUtils.isNotEmpty(lclBlAcList)) {
            lclBlAc = lclBlAcList.get(0);
        }
        return lclBlAc;
    }

    public void updateNewBillToParty(String arBillToParty, Integer uerId, Long fileId, Integer arGlMappingId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append("  lcl_booking_ac ");
        sb.append(" SET");
        sb.append("  ar_bill_to_party = :arBillToParty,");
        sb.append("  modified_by_user_id =:uerId,");
        sb.append("  modified_datetime = SYSDATE()");
        sb.append(" WHERE file_number_id =:fileId  ");
        sb.append("  AND ar_amount > 0.00 ");
        sb.append("  AND ar_gl_mapping_id =:arGlMappingId");
        sb.append("  AND ar_bill_to_party <> 'A'");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("arBillToParty", arBillToParty);
        queryObject.setParameter("uerId", uerId);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("arGlMappingId", arGlMappingId);
        queryObject.executeUpdate();
        getCurrentSession().flush();
    }

    public String BlueScreenTTRevChgCode(String pooorigin, String rateType) throws Exception {
        String blueScreen_db = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT l.ttcode FROM terminal t JOIN  ").append(blueScreen_db).append(".loctn l ON t.trmnum=l.trmnum ");
        queryBuilder.append("WHERE t.unlocationcode1 = '").append(pooorigin).append("' AND t.actyon= '").append(rateType).append("' LIMIT 1");
        return (String) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public LclBookingDestinationServices getDestinationCharge(Long bookingAcId, Long fileId) throws Exception {
        Query query = getSession().createQuery("from LclBookingDestinationServices "
                + "ld join fetch ld.lclbookingAc ac where ac.id=:bookingAcId and ld.lclFileNumber.id=:fileId");
        query.setParameter("bookingAcId", bookingAcId);
        query.setParameter("fileId", fileId);
        return (LclBookingDestinationServices) query.uniqueResult();
    }

    public boolean isDestinationChargeContain(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*) > 0, TRUE, FALSE) AS result FROM lcl_booking_ac lbc  ");
        queryBuilder.append(" JOIN gl_mapping gm ON (lbc.ar_gl_mapping_id=gm.id AND gm.destination_services = 1)");
        queryBuilder.append(" WHERE lbc.file_number_id=:fileId and lbc.ar_amount <> 0.00 ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public List<LclBookingAc> getAllDestinationChargeList(Long fileId) throws Exception {
        Query query = getSession().createQuery(" FROM LclBookingAc ac join fetch ac.arglMapping ar  where "
                + "  ar.destinationServices = 1 and ac.arAmount <> 0.00 and ac.manualEntry = 1 and ac.lclFileNumber.id =:fileId ");
        query.setParameter("fileId", fileId);
        return query.list();
    }

    public String getIpiAndInbBkgAcIds(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT GROUP_CONCAT(ac.id) FROM lcl_booking_ac ac ");
        queryBuilder.append(" JOIN gl_mapping gm ");
        queryBuilder.append("    ON gm.id = ac.ar_gl_mapping_id ");
        queryBuilder.append(" WHERE gm.bluescreen_chargecode IN (1607, 1617) ");
        queryBuilder.append("  AND ac.file_number_id = :fileNumberId ");
        queryBuilder.append("  AND ac.`sp_reference_no` IS NULL ");
        queryBuilder.append("  AND ac.`ar_bill_to_party` ='A'");
        queryBuilder.append("  AND ac.`manual_entry` IS FALSE");
        Query queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("fileNumberId", fileNumberId);
        return (String) queryObject.uniqueResult();
    }

    public LclBookingAc findByIpiAndInbChargeId(String acId) throws Exception {
        String queryString = "from LclBookingAc where id=" + acId;
        Query query = getSession().createQuery(queryString);
        return (LclBookingAc) query.uniqueResult();
    }

    public List<BookingChargesBean> findBybookingAcIdForPayment(String fileNumberId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lbc.id as id,IF(gm.charge_description='' OR gm.charge_description IS NULL,gm.charge_code ,gm.charge_description) AS chargeCode,");
        sb.append("(lbc.ar_amount+lbc.adjustment_amount) AS totalAmt,IF(lbcta.amount IS NULL ,0.00,SUM(lbcta.amount)) AS paidAmt, ");
        sb.append("IF(lbcta.amount IS NULL ,(lbc.ar_amount+lbc.adjustment_amount),(lbc.ar_amount+lbc.adjustment_amount)- SUM(lbcta.amount)) as balanceAmt,lbc.`ar_bill_to_party` AS arBillToParty FROM lcl_booking_ac lbc ");
        sb.append("LEFT JOIN lcl_booking_ac_ta lbcta ON lbcta.lcl_booking_ac_id = lbc.id ");
        sb.append("JOIN gl_mapping gm ON gm.id=lbc.ar_gl_mapping_id ");
        sb.append("WHERE lbc.file_number_id= ").append(fileNumberId);
        sb.append(" AND lbc.ar_amount <> 0.00 AND lbc.ar_amount IS NOT NULL AND lbc.ar_bill_to_party IN('C','N','T')").append(" GROUP BY lbc.id");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(BookingChargesBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("arBillToParty", StringType.INSTANCE);
        query.addScalar("totalAmt", BigDecimalType.INSTANCE);
        query.addScalar("paidAmt", BigDecimalType.INSTANCE);
        query.addScalar("balanceAmt", BigDecimalType.INSTANCE);
        List<BookingChargesBean> chargeList = query.list();
        return chargeList;
    }

    public Boolean isWareHouseChargeExist(String fileNumberId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*)>0,true,false) as result FROM ");
        queryStr.append(" lcl_booking_ac where file_number_id=:fileId and ar_bill_to_party='W' AND manual_entry=0 ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("fileId", fileNumberId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }

    public double getBookingTotalCollectChages(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(amount) AS amount FROM ");
        sb.append(" (SELECT SUM(ar_amount+adjustment_amount) AS amount FROM lcl_booking_ac ac  ");
        sb.append(" JOIN gl_mapping gl ON gl.id = ac.`ar_gl_mapping_id` ");
        sb.append(" WHERE ac.`file_number_id`=:fileId ");
        sb.append(" AND gl.`Charge_code` <> 'CAF' AND ac.`ar_bill_to_party` = 'A' ");
        sb.append(" GROUP BY gl.`Charge_code`) AS fl ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        Object obj = query.uniqueResult();
        return obj != null ? Double.parseDouble(obj.toString()) : 0l;
    }

    public List<BookingChargesBean> getConsolidatedAutoCharge(List fileIds) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ac.id as id,gl.Charge_code AS chargeCode, ");
        sb.append(" SUM(ac.adjustment_amount) AS adjustmentAmt, ");
        sb.append(" gl.bluescreen_chargecode as blueScreencode ");
        sb.append(" FROM lcl_booking_ac ac JOIN gl_mapping gl ON gl.id = ac.ar_gl_mapping_id ");
        sb.append(" WHERE  ac.file_number_id IN(:fileId) AND ac.`ar_amount` <> 0.00 ");
        sb.append(" GROUP BY gl.Charge_code ORDER BY ac.id ASC ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameterList("fileId", fileIds);
        query.setResultTransformer(Transformers.aliasToBean(BookingChargesBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("adjustmentAmt", BigDecimalType.INSTANCE);
        query.addScalar("blueScreencode", StringType.INSTANCE);
        List<BookingChargesBean> chargeList = query.list();
        return chargeList;
    }
     public Integer getLclBookingAcId(Long fileId, Integer arGlMappingId, String billToParty) throws Exception {
        BigInteger bookingAcid = new BigInteger("0");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lbac.id FROM lcl_booking_ac lbac ");
        queryBuilder.append("WHERE file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND ar_gl_mapping_id = ");
        queryBuilder.append(arGlMappingId);
        queryBuilder.append(" AND ar_bill_to_party = '");
        queryBuilder.append(billToParty);
        queryBuilder.append("'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            bookingAcid = (BigInteger) o;
        }
        return bookingAcid.intValue();
    }   
     
    public List<LclBookingAc> HazadrousChargeValidate(Long fileId,String[] chargeCode, boolean manualEntry) throws Exception {
        LclBookingAc lclBookingAc = null;
        Criteria criteria = getSession().createCriteria(LclBookingAc.class, "lclBookingAc");
        criteria.createAlias("lclBookingAc.arglMapping", "glMapping");
        criteria.add(Restrictions.ne("deleted", true));
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.eq("lclBookingAc.manualEntry", manualEntry));
        if (null != chargeCode && chargeCode.length > 0) {
            criteria.add(Restrictions.in("glMapping.chargeCode", chargeCode));
        }
        return (List<LclBookingAc>) criteria.list();
    }
}
