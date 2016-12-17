/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.lcl.model.UnitSsAutoCostingModel;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsAutoCosting;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.referencedata.form.LclUnitSsAutoCostingForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Mei
 */
public class UnitSsAutoCostingDAO extends BaseHibernateDAO<LclUnitSsAutoCosting> {

    private Map<Integer, String> vendorMap = new HashMap<Integer, String>();

    public UnitSsAutoCostingDAO() {
        super(LclUnitSsAutoCosting.class);
    }

    public List<UnitSsAutoCostingModel> search(LclUnitSsAutoCostingForm autoCostingForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  ");
        queryBuilder.append(" SELECT  ");
        queryBuilder.append(" ua.id AS autoCostId,  ");
        queryBuilder.append(" UnLocationGetNameStateCntryByID(ua.origin_id) AS originName, ");
        queryBuilder.append(" UnLocationGetNameStateCntryByID(ua.destination_id) AS fdName, ");
        queryBuilder.append(" ua.type AS costType, ");
        queryBuilder.append(" ua.sp_acct_no AS vendorNo,tp.acct_name as vendorName, ");
        queryBuilder.append(" gm.charge_code AS costCode,ua.rate_uom as rateUom, ");
        queryBuilder.append(" ua.rate_per_uom as ratePerUom,ua.rate_action as rateAction,lu.description as unitTypeDesc,  ");
        queryBuilder.append(" ua.rate_condition as rateCondition,ua.rate_condition_qty as rateConQty,gm.charge_code AS costCode, ");
        queryBuilder.append(" lu.id as unitTypeId ");
        queryBuilder.append(" FROM ");
        queryBuilder.append(" lcl_ss_unit_auto_costing ua ");
        queryBuilder.append(" JOIN trading_partner tp ON tp.acct_no=ua.sp_acct_no ");
        queryBuilder.append(" JOIN gl_mapping gm ON gm.id=ua.gl_mapping_id ");
        queryBuilder.append(" JOIN unit_type lu ON lu.id=ua.unit_type_id ");
        queryBuilder.append(" where ua.rate_per_uom <> 0.00 ");
        if (CommonUtils.isNotEmpty(autoCostingForm.getOriginId())) {
            queryBuilder.append(" AND ua.origin_id=").append(autoCostingForm.getOriginId());
        }
        if (CommonUtils.isNotEmpty(autoCostingForm.getFdId())) {
            queryBuilder.append(" AND  ua.destination_id=").append(autoCostingForm.getFdId());
        }
        if (CommonUtils.isNotEmpty(autoCostingForm.getUnitTypeId())) {
            queryBuilder.append("  AND ua.unit_type_id=").append(autoCostingForm.getUnitTypeId());
        }
        if (CommonUtils.isNotEmpty(autoCostingForm.getCostCodeId())) {
            queryBuilder.append("  AND ua.gl_mapping_id=").append(autoCostingForm.getCostCodeId());
        }
        queryBuilder.append(" GROUP BY ua.gl_mapping_id,ua.unit_type_id,ua.type,ua.rate_uom ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(UnitSsAutoCostingModel.class));
        query.addScalar("autoCostId", LongType.INSTANCE);
        query.addScalar("originName", StringType.INSTANCE);
        query.addScalar("fdName", StringType.INSTANCE);
        query.addScalar("costType", StringType.INSTANCE);
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("vendorNo", StringType.INSTANCE);
        query.addScalar("unitTypeDesc", StringType.INSTANCE);
        query.addScalar("rateUom", StringType.INSTANCE);
        query.addScalar("ratePerUom", BigDecimalType.INSTANCE);
        query.addScalar("rateAction", StringType.INSTANCE);
        query.addScalar("rateCondition", StringType.INSTANCE);
        query.addScalar("rateConQty", BigDecimalType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("unitTypeId", IntegerType.INSTANCE);
        return query.list();
    }

    public List<UnitSsAutoCostingModel> getAutoCost(Integer originId,
            Integer fdId, Long unitTypeId, String type) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  ");
        queryBuilder.append(" SELECT  ");
        queryBuilder.append(" ua.type AS costType,ua.rate_uom AS rateUom,ua.gl_mapping_id as apCostId, ");
        queryBuilder.append(" ua.rate_per_uom AS ratePerUom,ua.rate_action AS rateAction, ");
        queryBuilder.append(" ua.rate_condition AS rateCondition,ua.rate_Condition_qty AS rateConQty,ua.sp_acct_no as vendorNo ");
        queryBuilder.append(" FROM lcl_ss_unit_auto_costing ua ");
        queryBuilder.append(" WHERE  ");
        queryBuilder.append(" ua.origin_id=:originId ");
        queryBuilder.append(" AND ua.destination_id=:fdId ");
        queryBuilder.append(" AND ua.unit_type_id=:unitTypeId ");
        queryBuilder.append(" AND TYPE=:type  GROUP BY ua.gl_mapping_id ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("originId", originId);
        query.setParameter("fdId", fdId);
        query.setParameter("unitTypeId", unitTypeId);
        query.setParameter("type", type);
        query.setResultTransformer(Transformers.aliasToBean(UnitSsAutoCostingModel.class));
        query.addScalar("costType", StringType.INSTANCE);
        query.addScalar("rateUom", StringType.INSTANCE);
        query.addScalar("ratePerUom", BigDecimalType.INSTANCE);
        query.addScalar("apCostId", IntegerType.INSTANCE);
        query.addScalar("rateAction", StringType.INSTANCE);
        query.addScalar("rateCondition", StringType.INSTANCE);
        query.addScalar("rateConQty", BigDecimalType.INSTANCE);
        query.addScalar("vendorNo", StringType.INSTANCE);
        return query.list();
    }

    public void calculatedByUnitAutoCost(LclUnitSs unitSs, Integer originId, Integer fdId,
            Long unitTypeId, User loginUser) throws Exception {
        List<UnitSsAutoCostingModel> baseAutoCostList = getAutoCost(originId, fdId, unitTypeId, "BASE");
        if (baseAutoCostList != null && !baseAutoCostList.isEmpty()) {
            List<ExportVoyageSearchModel> commodityList = new LclUnitSsDAO().getBlSumOfCommodityVal(unitSs.getId());
            ExportVoyageSearchModel commModel = commodityList.get(0);
            Double volumeMetric = commModel.getTotalVolumeMetric() != null ? commModel.getTotalVolumeMetric().doubleValue() : 0.00;
            Double weightMetric = commModel.getTotalWeightMetric() != null ? commModel.getTotalWeightMetric().doubleValue() : 0.00;
            Map<Integer, Double> baseAutoCostAmt = this.calculateBaseAmt(baseAutoCostList, volumeMetric, weightMetric);
            if (!baseAutoCostAmt.isEmpty()) {
                List<UnitSsAutoCostingModel> addAutoCostList = getAutoCost(originId, fdId, unitTypeId, "ADD");
                baseAutoCostAmt = this.calculateAddAmt(addAutoCostList, baseAutoCostAmt, volumeMetric, weightMetric);
                saveAutoCost(unitSs, baseAutoCostAmt, loginUser, new Date());
            }
        }

    }

    public void saveAutoCost(LclUnitSs unitSs, Map<Integer, Double> apAmtMap, User loginUser, Date now) throws Exception {
        for (Map.Entry<Integer, Double> cost : apAmtMap.entrySet()) {
            Integer apCostId = cost.getKey();
            Double apCostAmt = cost.getValue();
            String vendorNo = vendorMap.get(apCostId);
            if (0.0 != apCostAmt) {
                List<LclSsAc> lclSsAcList = new ArrayList<LclSsAc>();
                LclSsAc lclSsAc = new LclSsAcDAO().createInstance(unitSs, apCostAmt, apCostId,
                        "LCLE", vendorNo != null ? vendorNo : "ECOCON0001", loginUser, now,
                        unitSs.getLclSsHeader().getScheduleNo());
                lclSsAcList.add(lclSsAc);
                if (null != lclSsAcList && !lclSsAcList.isEmpty()) {
                    new LclManifestDAO().createLclAccrualsforAutoCosting(lclSsAcList);
                }
            }
        }
    }

    public Map calculateBaseAmt(List<UnitSsAutoCostingModel> autoCostList,
            Double volumeMetric, Double weightMetric) throws Exception {
        Map<Integer, Double> baseAmtMap = new HashMap<Integer, Double>();
        if (null != autoCostList && !autoCostList.isEmpty()) {
            for (UnitSsAutoCostingModel baseCost : autoCostList) {
                Double baseAutoCostAmt = 0.00;
                if ("FLAT".equalsIgnoreCase(baseCost.getRateUom())
                        && baseCost.getRatePerUom().doubleValue() > baseAutoCostAmt) {
                    baseAutoCostAmt = baseCost.getRatePerUom().doubleValue();
                } else if ("MEASURE".equalsIgnoreCase(baseCost.getRateUom())) {
                    Double mesureAmt = volumeMetric * baseCost.getRatePerUom().doubleValue();
                    if (mesureAmt > baseAutoCostAmt) {
                        baseAutoCostAmt = mesureAmt;
                    }
                } else if ("WEIGHT".equalsIgnoreCase(baseCost.getRateUom())) {
                    Double weightAmt = weightMetric * baseCost.getRatePerUom().doubleValue();
                    if (weightAmt > baseAutoCostAmt) {
                        baseAutoCostAmt = weightAmt;
                    }
                }
                vendorMap.put(baseCost.getApCostId(), baseCost.getVendorNo());
                baseAmtMap.put(baseCost.getApCostId(), baseAutoCostAmt);
            }
        }
        return baseAmtMap;
    }

    public Map calculateAddAmt(List<UnitSsAutoCostingModel> autoCostList, Map<Integer, Double> baseCostAmtMap,
            Double volumeMetric, Double weightMetric) throws Exception {
        if (null != autoCostList && !autoCostList.isEmpty()) {
            for (UnitSsAutoCostingModel addCost : autoCostList) {
                Double baseAutoCostAmt = baseCostAmtMap.get(addCost.getApCostId());
                if (baseAutoCostAmt != null) {
                    if ("FLAT".equalsIgnoreCase(addCost.getRateUom())) {
                        baseAutoCostAmt += addCost.getRatePerUom().doubleValue();
                    } else if ("MEASURE".equalsIgnoreCase(addCost.getRateUom())) {
                        baseAutoCostAmt = getAmt(volumeMetric, baseAutoCostAmt, addCost.getRatePerUom().doubleValue(),
                                addCost.getRateAction(), addCost.getRateCondition(), addCost.getRateConQty().doubleValue());
                    } else if ("WEIGHT".equalsIgnoreCase(addCost.getRateUom())) {
                        baseAutoCostAmt = getAmt(weightMetric, baseAutoCostAmt, addCost.getRatePerUom().doubleValue(),
                                addCost.getRateAction(), addCost.getRateCondition(), addCost.getRateConQty().doubleValue());
                    }
                    baseCostAmtMap.put(addCost.getApCostId(), baseAutoCostAmt);
                }
            }
        }
        return baseCostAmtMap;
    }

    public Double getAmt(Double volumeMetric, Double baseAutoCostAmt, Double rateAmt, String rateAction,
            String rateCondtion, Double rateCondAmt) {
        Double amt = baseAutoCostAmt;
        if ("EQ".equalsIgnoreCase(rateCondtion) && volumeMetric.equals(rateCondAmt)) {
            amt = getRateAction(baseAutoCostAmt, rateAmt, rateAction);
        } else if ("EQGT".equalsIgnoreCase(rateCondtion) && volumeMetric >= rateCondAmt) {
            amt = getRateAction(baseAutoCostAmt, rateAmt, rateAction);
        } else if ("EQLT".equalsIgnoreCase(rateCondtion) && volumeMetric <= rateCondAmt) {
            amt = getRateAction(baseAutoCostAmt, rateAmt, rateAction);
        } else if ("GT".equalsIgnoreCase(rateCondtion) && volumeMetric > rateCondAmt) {
            amt = getRateAction(baseAutoCostAmt, rateAmt, rateAction);
        } else if ("LT".equalsIgnoreCase(rateCondtion) && volumeMetric < rateCondAmt) {
            amt = getRateAction(baseAutoCostAmt, rateAmt, rateAction);
        }
        return amt;
    }

    public Double getRateAction(Double baseAutoCostAmt, Double costAmt, String rateAction) {
        if ("+".equalsIgnoreCase(rateAction)) {
            baseAutoCostAmt += costAmt;
        } else if ("-".equalsIgnoreCase(rateAction)) {
            baseAutoCostAmt -= costAmt;
        } else if ("*".equalsIgnoreCase(rateAction)) {
            baseAutoCostAmt = baseAutoCostAmt * costAmt;
        } else if ("/".equalsIgnoreCase(rateAction)) {
            baseAutoCostAmt = baseAutoCostAmt / costAmt;
        }
        return baseAutoCostAmt;
    }

    public String validateExistCost(String origin, String destination, String chargeCode) throws Exception {
        List<String> costCodeList = Arrays.asList(chargeCode.split(","));
        LclUnitSsAutoCostingForm autoCostingForm = new LclUnitSsAutoCostingForm();
        autoCostingForm.setOriginId(Integer.parseInt(origin));
        autoCostingForm.setFdId(Integer.parseInt(destination));
        Set<String> costSet = new HashSet<String>();
        List<UnitSsAutoCostingModel> unitCostList = this.search(autoCostingForm);
        String concatValue = "";
        for (UnitSsAutoCostingModel autoCost : unitCostList) {
            concatValue = autoCost.getCostCode()+"#"
                    +autoCost.getUnitTypeId().toString()+"#"+autoCost.getRateUom();
            if (costCodeList.contains(concatValue)) {
                costSet.add(autoCost.getCostCode());
            }
        }
        String result = costSet.size() > 0 ? costSet.toString().substring(1, costSet.toString().length() - 1) : "";
        return result;
    }

    public void deleteUnitSsCost(String costId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("delete from lcl_ss_unit_auto_costing where id =" + costId);
        query.executeUpdate();
    }

    public void deleteUnitSsCostByOrgDest(int org, int dest, String costCode, Long unitType,String rateUom) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" delete luc from lcl_ss_unit_auto_costing luc join gl_mapping ");
        sb.append(" gl on gl.id = luc.gl_mapping_id where luc.origin_id =:origin ");
        sb.append(" and luc.destination_id=:destination and gl.charge_code=:code and luc.unit_type_id=:unitType ");
        sb.append(" and luc.rate_uom=:rateUom ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", org);
        query.setParameter("destination", dest);
        query.setParameter("code", costCode);
        query.setParameter("unitType", unitType);
        query.setParameter("rateUom", rateUom);
        query.executeUpdate();
    }

}
