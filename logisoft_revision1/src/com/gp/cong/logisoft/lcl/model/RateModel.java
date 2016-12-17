package com.gp.cong.logisoft.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.ChargesInfoBean;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RateModel implements java.io.Serializable {

    // For only blue screen fetch beans
    private String chargeCode;
    private String chargeType;
    private BigDecimal barrelOFRate;
    private BigDecimal barrelTTRate;
    private BigDecimal OfRateMeasure;
    private BigDecimal OfRateWeight;
    private BigDecimal OfRateMin;
    private BigDecimal flatRate;
    private BigDecimal totalPct;
    private BigDecimal blueCft;
    private BigDecimal blueWgt;
    private BigDecimal blueMin;
    private BigDecimal blueInsurt;
    private BigDecimal blueInsamt;
    private BigDecimal blueCbm;
    private BigDecimal blueKgs;

    // For commodity Details In Correction
    private CommodityType commodityType;
    private BigDecimal cft = BigDecimal.ZERO;
    private BigDecimal cbm = BigDecimal.ZERO;
    private BigDecimal lbs = BigDecimal.ZERO;
    private BigDecimal kgs = BigDecimal.ZERO;
    private boolean hazmat;
    private boolean barrel;
    private int pieceCount;
    private BigDecimal totalChargeAmount = BigDecimal.ZERO;
    private String engMet = "";
    private RatesCalculationModel ratesModel;

    public LinkedHashMap<String, RateModel> rollUpList(Map<String, List<RateModel>> blueChargeList) {
        LinkedHashMap<String, RateModel> blueChargeMap = new LinkedHashMap<String, RateModel>();
        for (Map.Entry key : blueChargeList.entrySet()) {
            List<RateModel> modelList = (List<RateModel>) key.getValue();
            for (RateModel model : modelList) {
                if (!blueChargeMap.containsKey(model.getChargeCode())) {
                    blueChargeMap.put(model.getChargeCode(), model);
                }
            }
        }
        return blueChargeMap;
    }

    public List<ChargesInfoBean> getLclExportsChargeList(RatesCalculationModel ratesModel,
            Map<String, List<RateModel>> blueChargeList, List<RateModel> commodityList) throws Exception {
        this.engMet = ratesModel.getEngMet();
        this.ratesModel = ratesModel;
        List<ChargesInfoBean> chargeList = new ArrayList<ChargesInfoBean>();
        if (!blueChargeList.isEmpty()) {
            List<RateModel> autoBlueChargeList = new ArrayList<RateModel>(this.rollUpList(blueChargeList).values());
            for (RateModel commodity : commodityList) {
                cft = cft.add(commodity.getCft());
                cbm = cbm.add(commodity.getCbm());
                lbs = lbs.add(commodity.getLbs());
                kgs = kgs.add(commodity.getKgs());
            }
            for (RateModel commodity : commodityList) {
                chargeList.addAll(this.differentiateWithChargeType(engMet, autoBlueChargeList, commodity));
            }
        }
        return chargeList;
    }

    public List<ChargesInfoBean> differentiateWithChargeType(String engMet,
            List<RateModel> autoBlueChargeList, RateModel commodity) throws Exception {
        List<ChargesInfoBean> chargeList = new ArrayList();
        ChargesInfoBean cinfobean = new ChargesInfoBean();
        GlMapping glmapping = new GlMapping();
        String blueChargeCode = "";
        int blueChargeType = 0;
        for (RateModel blueCharge : autoBlueChargeList) {

            if (!CommonUtils.isEmpty(blueCharge.getChargeCode())
                    && (blueCharge.getChargeCode().equalsIgnoreCase("0001")
                    || CommonUtils.in(blueCharge.getChargeCode(), "OFBARR/TTBARR", "OFBARR"))) {
                chargeList.addAll(calculateOceanFreight(engMet, blueCharge, commodity));
            } else if (!chargeList.isEmpty()) {
                if (CommonUtils.isNotEmpty(blueCharge.getChargeCode())) {
                    glmapping = new GlMappingDAO().findByBlueScreenChargeCode(blueCharge.getChargeCode(), "LCLE", "AR");
                    blueChargeCode = glmapping.getChargeCode();
                    blueChargeType = Integer.parseInt(blueCharge.getChargeType());
                }

                if (CommonUtils.isNotEmpty(blueCharge.getChargeType())
                        && !blueCharge.getChargeType().equalsIgnoreCase("0")) {

                    if (blueCharge.getChargeType().equalsIgnoreCase("1")) {
                        cinfobean = new ChargesInfoBean();
                        cinfobean.setChargeType(blueChargeType);
                        cinfobean.setChargesDesc(glmapping.getChargeDescriptions());
                        cinfobean.setCommodityCode(commodity.getCommodityType().getCode());
                        cinfobean.setChargeCode(blueChargeCode);
                        cinfobean.setGlMapping(glmapping);
                        cinfobean.setRate(blueCharge.getFlatRate());
                        cinfobean.setPcb("");
                        cinfobean.setRatePerUnit(blueCharge.getFlatRate());
                        cinfobean.setRatePerUnitUom("FL");
                        cinfobean.setRateUom(engMet);
                        chargeList.add(cinfobean);
                        totalChargeAmount = totalChargeAmount.add(cinfobean.getRate());
                    } else if (blueCharge.getChargeType().equalsIgnoreCase("2")) {
                        // For Total Percentage Calculation. EX : CAF;
                    } else if (blueCharge.getChargeType().equalsIgnoreCase("3")) {
                        cinfobean = new ChargesInfoBean();
                        cinfobean.setChargeType(blueChargeType);
                        cinfobean.setChargesDesc(glmapping.getChargeDescriptions());
                        cinfobean.setCommodityCode(commodity.getCommodityType().getCode());
                        cinfobean.setChargeCode(blueChargeCode);
                        cinfobean.setGlMapping(glmapping);
                        BigDecimal defalutMeasureValue = new BigDecimal(1000);
                        BigDecimal defalutWeightValue = null;
                        Double calculatedWeight = 0.00;
                        Double calculatedMeasure = 0.00;
                        if (CommonUtils.isNotEmpty(engMet)) {
                            if (engMet.equalsIgnoreCase("E")) {
                                defalutWeightValue = new BigDecimal(100);
                                calculatedWeight = ((lbs.divide(defalutWeightValue))
                                        .multiply(blueCharge.getBlueWgt())).doubleValue();
                                calculatedMeasure = (cft.multiply(blueCharge.getBlueCft())).doubleValue();

                                cinfobean.setRatePerWeightUnit(blueCharge.getBlueWgt());
                                cinfobean.setRatePerWeightUnitDiv(defalutWeightValue);
                                cinfobean.setRatePerVolumeUnit(blueCharge.getBlueCft());
                                cinfobean.setRatePerVolumeUnitDiv(defalutMeasureValue);
                                cinfobean.setWeightRate(blueCharge.getBlueWgt().doubleValue());
                                cinfobean.setMeasureRate(blueCharge.getBlueCft().doubleValue());
                            } else if (engMet.equalsIgnoreCase("M")) {
                                defalutWeightValue = new BigDecimal(1000);
                                calculatedWeight = ((kgs.divide(defalutWeightValue))
                                        .multiply(blueCharge.getBlueKgs())).doubleValue();
                                calculatedMeasure = (cbm.multiply(blueCharge.getBlueCbm())).doubleValue();

                                cinfobean.setRatePerWeightUnit(blueCharge.getBlueKgs());
                                cinfobean.setRatePerWeightUnitDiv(defalutWeightValue);
                                cinfobean.setRatePerVolumeUnit(blueCharge.getBlueCbm());
                                cinfobean.setRatePerVolumeUnitDiv(defalutMeasureValue);
                                cinfobean.setWeightRate(blueCharge.getBlueKgs().doubleValue());
                                cinfobean.setMeasureRate(blueCharge.getBlueCbm().doubleValue());
                            }
                        }
                        cinfobean.setMinCharge(blueCharge.getBlueMin());
                        if (calculatedWeight >= calculatedMeasure) {
                            cinfobean.setRatePerUnit(new BigDecimal(calculatedWeight));
                            cinfobean.setRatePerUnitUom("W");
                            cinfobean.setRate(new BigDecimal(calculatedWeight));
                        } else {
                            cinfobean.setRatePerUnit(new BigDecimal(calculatedMeasure));
                            cinfobean.setRatePerUnitUom("V");
                            cinfobean.setRate(new BigDecimal(calculatedMeasure));
                        }
                        if (cinfobean.getRatePerUnit().doubleValue() <= blueCharge.getBlueMin().doubleValue()) {
                            cinfobean.setRatePerUnit(blueCharge.getBlueMin());
                            cinfobean.setRatePerUnitUom("M");
                            cinfobean.setRate(blueCharge.getBlueMin());
                        }
                        cinfobean.setRateUom(engMet);
                        chargeList.add(cinfobean);
                        totalChargeAmount = totalChargeAmount.add(cinfobean.getRate());
                    } else if (blueCharge.getChargeType().equalsIgnoreCase("4")) {
                        cinfobean = getInsuranceCharge(engMet, blueCharge, commodity);
                        cinfobean.setGlMapping(glmapping);
                        cinfobean.setRateUom(engMet);
                        chargeList.add(cinfobean);
                    }
                }
            }
        }
        return chargeList;
    }

    public List<ChargesInfoBean> calculateOceanFreight(String engMet,
            RateModel blueCharge, RateModel commodity) throws Exception {
        List<ChargesInfoBean> chargeList = new ArrayList();
        ChargesInfoBean cinfobean = null;

        if (!CommonUtils.isEmpty(blueCharge.getOfRateMeasure())
                || !CommonUtils.isEmpty(blueCharge.getOfRateWeight())
                || !CommonUtils.isEmpty(blueCharge.getOfRateMin())) {
            cinfobean = new ChargesInfoBean();
            GlMapping glMapping = new GlMappingDAO().findByChargeCode("OCNFRT", "LCLE", "AR");
            BigDecimal defalutMeasureValue = new BigDecimal(1000);
            BigDecimal defalutWeightValue = null;
            Double calculatedWeight = 0.00;
            Double calculatedMeasure = 0.00;
            cinfobean.setChargeCode("0001");
            cinfobean.setChargesDesc("OCEAN FREIGHT");
            cinfobean.setGlMapping(glMapping);
            cinfobean.setCommodityCode(commodity.getCommodityType().getCode());
            cinfobean.setChargeType(3);
            if (CommonUtils.isNotEmpty(engMet)) {
                if (engMet.equalsIgnoreCase("E")) {
                    defalutWeightValue = new BigDecimal(100);
                    calculatedWeight = ((lbs.divide(defalutWeightValue))
                            .multiply(blueCharge.getOfRateWeight())).doubleValue();
                    calculatedMeasure = (cft.multiply(blueCharge.getOfRateMeasure())).doubleValue();

                    cinfobean.setRatePerWeightUnit(blueCharge.getOfRateWeight());
                    cinfobean.setRatePerWeightUnitDiv(defalutWeightValue);
                    cinfobean.setRatePerVolumeUnit(blueCharge.getOfRateMeasure());
                    cinfobean.setRatePerVolumeUnitDiv(defalutMeasureValue);
                    cinfobean.setWeightRate(blueCharge.getOfRateWeight().doubleValue());
                    cinfobean.setMeasureRate(blueCharge.getOfRateMeasure().doubleValue());
                } else if (engMet.equalsIgnoreCase("M")) {
                    defalutWeightValue = new BigDecimal(1000);
                    calculatedWeight = ((kgs.divide(defalutWeightValue))
                            .multiply(blueCharge.getOfRateWeight())).doubleValue();
                    calculatedMeasure = (cbm.multiply(blueCharge.getOfRateMeasure())).doubleValue();

                    cinfobean.setRatePerWeightUnit(blueCharge.getOfRateWeight());
                    cinfobean.setRatePerWeightUnitDiv(defalutWeightValue);
                    cinfobean.setRatePerVolumeUnit(blueCharge.getOfRateMeasure());
                    cinfobean.setRatePerVolumeUnitDiv(defalutMeasureValue);
                    cinfobean.setWeightRate(blueCharge.getOfRateWeight().doubleValue());
                    cinfobean.setMeasureRate(blueCharge.getOfRateMeasure().doubleValue());
                }
            }
            cinfobean.setMinCharge(blueCharge.getOfRateMin());
            if (calculatedWeight >= calculatedMeasure) {
                cinfobean.setRatePerUnit(new BigDecimal(calculatedWeight));
                cinfobean.setRatePerUnitUom("FRW");
                cinfobean.setRate(new BigDecimal(calculatedWeight));
            } else {
                cinfobean.setRatePerUnit(new BigDecimal(calculatedMeasure));
                cinfobean.setRatePerUnitUom("FRV");
                cinfobean.setRate(new BigDecimal(calculatedMeasure));
            }
            if (cinfobean.getRatePerUnit().doubleValue() <= blueCharge.getOfRateMin().doubleValue()) {
                cinfobean.setRatePerUnit(blueCharge.getOfRateMin());
                cinfobean.setRatePerUnitUom("FRM");
                cinfobean.setRate(blueCharge.getOfRateMin());
            }
            cinfobean.setRateUom(engMet);
            chargeList.add(cinfobean);
            totalChargeAmount = totalChargeAmount.add(cinfobean.getRate());
        }
        if (!CommonUtils.isEmpty(blueCharge.getBarrelOFRate())) {
            cinfobean = new ChargesInfoBean();
            GlMapping OFBARR = new GlMappingDAO().findByChargeCode("OFBARR", "LCLE", "AR");
            cinfobean = new ChargesInfoBean();
            cinfobean.setChargeCode(OFBARR.getChargeCode().equalsIgnoreCase("") ? "" : "OFBARR");
            cinfobean.setChargesDesc("BARELL OFRATE");
            cinfobean.setGlMapping(OFBARR);
            cinfobean.setCommodityCode(commodity.getCommodityType().getCode());
            cinfobean.setRate(blueCharge.getBarrelOFRate().multiply(new BigDecimal(commodity.getPieceCount())));
            cinfobean.setPcb("");
            cinfobean.setRatePerUnit(blueCharge.getBarrelOFRate());
            cinfobean.setRatePerWeightUnit(blueCharge.getBarrelOFRate());
            cinfobean.setRatePerUnitUom("FL");
            cinfobean.setRateUom(engMet);
            chargeList.add(cinfobean);
            totalChargeAmount = totalChargeAmount.add(cinfobean.getRate());
        }
        if (!CommonUtils.isEmpty(blueCharge.getBarrelTTRate())) {
            cinfobean = new ChargesInfoBean();
            GlMapping TTBARR = new GlMappingDAO().findByChargeCode("TTBARR", "LCLE", "AR");
            cinfobean = new ChargesInfoBean();
            cinfobean.setChargeCode(TTBARR.getChargeCode().equalsIgnoreCase("") ? "" : "TTBARR");
            cinfobean.setChargesDesc("BARELL TTA");
            cinfobean.setGlMapping(TTBARR);
            cinfobean.setCommodityCode(commodity.getCommodityType().getCode());
            cinfobean.setRate(blueCharge.getBarrelTTRate().multiply(new BigDecimal(commodity.getPieceCount())));
            cinfobean.setPcb("");
            cinfobean.setRatePerUnit(blueCharge.getBarrelTTRate());
            cinfobean.setRatePerWeightUnit(blueCharge.getBarrelTTRate());
            cinfobean.setRatePerUnitUom("FL");
            cinfobean.setRateUom(engMet);
            chargeList.add(cinfobean);
            totalChargeAmount = totalChargeAmount.add(cinfobean.getRate());
        }
        return chargeList;
    }

    public ChargesInfoBean getInsuranceCharge(String engMet,
            RateModel blueCharge, RateModel commodity) throws Exception {
        ChargesInfoBean cinfobean = null;
        BigDecimal insuranceValue = this.ratesModel.getValueOfGoods();
        BigDecimal insurancePercentage = blueCharge.getBlueInsurt();
        BigDecimal insuranceBlueAmt = blueCharge.getBlueInsamt();
        BigDecimal valueA = BigDecimal.ZERO;
        BigDecimal valueB = BigDecimal.ZERO;
        BigDecimal CIFValue = BigDecimal.ZERO;
        BigDecimal calculatedInsuranceAmt = BigDecimal.ZERO;
        if (!CommonUtils.isEmpty(insuranceBlueAmt) && !CommonUtils.isEmpty(insurancePercentage)) {
            valueA = ((insuranceValue.add(this.totalChargeAmount)).divide(insuranceBlueAmt)).multiply(insurancePercentage);
        }
        valueB = new BigDecimal(0.1).multiply(insuranceValue.add(this.totalChargeAmount).add(valueA));
        CIFValue = insuranceValue.add(this.totalChargeAmount).add(valueA).add(valueB);
        if (!CommonUtils.isEmpty(insuranceBlueAmt)) {
            calculatedInsuranceAmt = (CIFValue.divide(new BigDecimal(100))).multiply(insurancePercentage);
        }
        cinfobean = new ChargesInfoBean();
        cinfobean.setChargesDesc("INSURANCE");
        cinfobean.setRate(calculatedInsuranceAmt);
        cinfobean.setChargeCode("0006");
        cinfobean.setChargeType(4);
        cinfobean.setRatePerUnitUom("FL");
        cinfobean.setRatePerWeightUnit(insurancePercentage);
        cinfobean.setRatePerWeightUnitDiv(insuranceBlueAmt);
        totalChargeAmount = BigDecimal.ZERO;
        return cinfobean;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getBarrelOFRate() {
        return barrelOFRate;
    }

    public void setBarrelOFRate(BigDecimal barrelOFRate) {
        this.barrelOFRate = barrelOFRate;
    }

    public BigDecimal getBarrelTTRate() {
        return barrelTTRate;
    }

    public void setBarrelTTRate(BigDecimal barrelTTRate) {
        this.barrelTTRate = barrelTTRate;
    }

    public BigDecimal getOfRateMeasure() {
        return OfRateMeasure;
    }

    public void setOfRateMeasure(BigDecimal OfRateMeasure) {
        this.OfRateMeasure = OfRateMeasure;
    }

    public BigDecimal getOfRateWeight() {
        return OfRateWeight;
    }

    public void setOfRateWeight(BigDecimal OfRateWeight) {
        this.OfRateWeight = OfRateWeight;
    }

    public BigDecimal getOfRateMin() {
        return OfRateMin;
    }

    public void setOfRateMin(BigDecimal OfRateMin) {
        this.OfRateMin = OfRateMin;
    }

    public BigDecimal getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(BigDecimal flatRate) {
        this.flatRate = flatRate;
    }

    public BigDecimal getTotalPct() {
        return totalPct;
    }

    public void setTotalPct(BigDecimal totalPct) {
        this.totalPct = totalPct;
    }

    public BigDecimal getBlueCft() {
        return blueCft;
    }

    public void setBlueCft(BigDecimal blueCft) {
        this.blueCft = blueCft;
    }

    public BigDecimal getBlueWgt() {
        return blueWgt;
    }

    public void setBlueWgt(BigDecimal blueWgt) {
        this.blueWgt = blueWgt;
    }

    public BigDecimal getBlueMin() {
        return blueMin;
    }

    public void setBlueMin(BigDecimal blueMin) {
        this.blueMin = blueMin;
    }

    public BigDecimal getBlueInsurt() {
        return blueInsurt;
    }

    public void setBlueInsurt(BigDecimal blueInsurt) {
        this.blueInsurt = blueInsurt;
    }

    public BigDecimal getBlueInsamt() {
        return blueInsamt;
    }

    public void setBlueInsamt(BigDecimal blueInsamt) {
        this.blueInsamt = blueInsamt;
    }

    public BigDecimal getBlueCbm() {
        return blueCbm;
    }

    public void setBlueCbm(BigDecimal blueCbm) {
        this.blueCbm = blueCbm;
    }

    public BigDecimal getBlueKgs() {
        return blueKgs;
    }

    public void setBlueKgs(BigDecimal blueKgs) {
        this.blueKgs = blueKgs;
    }

    public CommodityType getCommodityType() {
        if (commodityType == null) {
            commodityType = new CommodityType();
        }
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public BigDecimal getCft() {
        return cft;
    }

    public void setCft(BigDecimal cft) {
        this.cft = cft;
    }

    public BigDecimal getCbm() {
        return cbm;
    }

    public void setCbm(BigDecimal cbm) {
        this.cbm = cbm;
    }

    public BigDecimal getLbs() {
        return lbs;
    }

    public void setLbs(BigDecimal lbs) {
        this.lbs = lbs;
    }

    public BigDecimal getKgs() {
        return kgs;
    }

    public void setKgs(BigDecimal kgs) {
        this.kgs = kgs;
    }

    public boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public boolean isBarrel() {
        return barrel;
    }

    public void setBarrel(boolean barrel) {
        this.barrel = barrel;
    }

    public int getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

}
