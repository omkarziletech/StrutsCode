package com.gp.cong.logisoft.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;

public class RatesCalculationModel extends BaseHibernateDAO {

    // Common Need For Rates Calculation 
    private String origin;
    private String pol;
    private String pod;
    private String destination;
    private String engMet;
    private String rateType;

    // For Future Usage
    private String buttonValue;
    private String moduleName;

    // For Extra Charges Params
    private boolean hazmat;
    private boolean calcHeavyOrExtraLength = false;
    private boolean pickUp;
    private String insurance = "";
    private boolean caf;
    private String deliveryMetro = "";
    private BigDecimal valueOfGoods = BigDecimal.ZERO;
    private String dataBaseName = "";
    // For Only Calculating process.
    private String extraCharges = "0000,";
    Map<String, List<RateModel>> autoChargeList = new HashMap<String, List<RateModel>>();
    List<RateModel> commodityList = new ArrayList<>();

    public Map<String, List<RateModel>> calculateExportBlRates(List<RateModel> commodityList) throws Exception {
        this.commodityList = commodityList;
        this.engMet = this.getEngMet();
        this.dataBaseName = LoadLogisoftProperties.getProperty("elite.database.name");
        for (RateModel commodity : this.commodityList) {
            this.getExtraCharges(commodity);
            List<RateModel> calculatedBlueRateList = new ArrayList<RateModel>();
            calculatedBlueRateList.addAll(this
                    .calculateOceanFreightCharge(commodity.getCommodityType().getCode(), commodity.isBarrel()));
            if (!calculatedBlueRateList.isEmpty()) {
                String blueChargeCodes = this
                        .calculateStandardCharges(commodity.getCommodityType().getCode(), extraCharges);
                blueChargeCodes = blueChargeCodes + "," + extraCharges;
                if (!blueChargeCodes.isEmpty() && !commodity.isBarrel()) {
                    calculatedBlueRateList.addAll(this
                            .getBlueRatesFromBlueChargeCode(blueChargeCodes, commodity.getCommodityType().getCode()));
                }
                blueChargeCodes = "";
                this.extraCharges = "";
            }
            autoChargeList.put(commodity.getCommodityType().getCode(), calculatedBlueRateList);
        }
        return autoChargeList;
    }

    public void getExtraCharges(RateModel commodity) throws Exception {
        this.extraCharges += commodity.getHazmat() ? "0119," : "";
        if (!CommonUtils.isEmpty(this.valueOfGoods)
                && !CommonUtils.isEmpty(this.insurance)
                && this.insurance.equalsIgnoreCase("Y")) {
            this.extraCharges += "0006,";
        }
        if (!CommonUtils.isEmpty(deliveryMetro)) {
            if (this.deliveryMetro.equalsIgnoreCase("O")) {
                this.extraCharges += "0060,";
            } else if (this.deliveryMetro.equalsIgnoreCase("I")) {
                this.extraCharges += "0015,";
            }
        }
        if (this.calcHeavyOrExtraLength) {
            this.extraCharges += getHeavyLiftCharge(commodity);
            //this.extraCharges += getExtraLengthCharge(commodity.getLclBlPieceDetailList());
            this.extraCharges += getDenseCargoCharge();
        }
    }

    public String getHeavyLiftCharge(RateModel commodity) {
        int piece = 1;
        Double totalHeavyLift = 0.0;
        piece = !CommonUtils.isEmpty(commodity.getPieceCount()) ? commodity.getPieceCount() : 0;
        totalHeavyLift = (!CommonUtils.isEmpty(commodity.getCbm())
                ? commodity.getCbm() : commodity.getCbm()).doubleValue();
        totalHeavyLift = totalHeavyLift / piece;
        if (totalHeavyLift >= 4000 && totalHeavyLift < 6000) {
            return "0031,";
        } else if (totalHeavyLift >= 6000 && totalHeavyLift < 8000) {
            return "0240,";
        } else if (totalHeavyLift >= 8000) {
            return "0241,";
        }
        return "";
    }

    public String getExtraLengthCharge(List<LclBlPieceDetail> pieceDetailList) {
        boolean firstCndnSatisfied = false, secondCndnSatisfied = false;
        for (LclBlPieceDetail pieceDetail : pieceDetailList) {
            if (pieceDetail.getActualLength() != null) {
                if (pieceDetail.getActualLength().doubleValue() >= 180 && pieceDetail.getActualLength().doubleValue() < 360) {
                    firstCndnSatisfied = true;
                }
                if (pieceDetail.getActualLength().doubleValue() >= 360) {
                    secondCndnSatisfied = true;
                }
            }
            if (pieceDetail.getActualHeight() != null) {
                if (pieceDetail.getActualHeight().doubleValue() >= 180 && pieceDetail.getActualHeight().doubleValue() < 360) {
                    firstCndnSatisfied = true;
                }
                if (pieceDetail.getActualHeight().doubleValue() >= 360) {
                    secondCndnSatisfied = true;
                }
            }
            if (pieceDetail.getActualWidth() != null) {
                if (pieceDetail.getActualWidth().doubleValue() >= 180 && pieceDetail.getActualWidth().doubleValue() < 360) {
                    firstCndnSatisfied = true;
                }
                if (pieceDetail.getActualWidth().doubleValue() >= 360) {
                    secondCndnSatisfied = true;
                }
            }
        }
        if (secondCndnSatisfied) {
            return "0242,";
        } else if (firstCndnSatisfied) {
            return "0032,";
        }
        return "";
    }

    public String getDenseCargoCharge() throws Exception {
        if (this.engMet.equalsIgnoreCase("M") && CommonUtils.isEmpty(this.commodityList)) {
            Double totalkgs = 0.0;
            Double totalcbm = 0.0;
            for (RateModel commodity : this.commodityList) {
                totalkgs += (CommonUtils.isEmpty(commodity.getKgs())
                        ? commodity.getKgs() : 0.00).doubleValue();
                totalcbm += (CommonUtils.isEmpty(commodity.getCbm())
                        ? commodity.getLbs() : 0.00).doubleValue();
            }
            String denseCargo = LoadLogisoftProperties.getProperty("DenseCargoFee");
            Double denseCargoKgs = null != denseCargo ? Double.parseDouble(denseCargo) : 1000;
            if (totalkgs >= denseCargoKgs && totalcbm != 0) {
                Double kgscbmratio = totalkgs / totalcbm;
                if (kgscbmratio >= 1000) {
                    return "0251,";
                }
            }
        }
        return "";
    }

    public List<RateModel> calculateOceanFreightCharge(String commNo, boolean barrel) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("call GetExportOceanFreightRate(:poo,:pol,:pod,:fd,:rate,:commNo,:barrel)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("poo", this.origin);
        query.setParameter("pol", this.pol);
        query.setParameter("pod", this.pod);
        query.setParameter("fd", this.destination);
        query.setParameter("rate", this.rateType);
        query.setParameter("commNo", commNo);
        query.setParameter("barrel", barrel);
        query.setResultTransformer(Transformers.aliasToBean(RateModel.class));
        if (barrel) {
            query.addScalar("chargeCode", StringType.INSTANCE);
            query.addScalar("barrelOFRate", BigDecimalType.INSTANCE);
            query.addScalar("barrelTTRate", BigDecimalType.INSTANCE);
        } else {
            query.addScalar("chargeCode", StringType.INSTANCE);
            query.addScalar("OfRateMeasure", BigDecimalType.INSTANCE);
            query.addScalar("OfRateWeight", BigDecimalType.INSTANCE);
            query.addScalar("OfRateMin", BigDecimalType.INSTANCE);
        }
        return query.list();
    }

    public String calculateStandardCharges(String commNo, String extraCharges) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("call GetLCLExportRates(:poo,:pol,:pod,:fd,:rate,:commNo,:extraCharges)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("poo", this.origin);
        query.setParameter("pol", this.pol);
        query.setParameter("pod", this.pod);
        query.setParameter("fd", this.destination);
        query.setParameter("rate", this.rateType);
        query.setParameter("commNo", commNo);
        query.setParameter("extraCharges", extraCharges);
        Object result = query.uniqueResult();
        getCurrentSession().flush();
        getCurrentSession().clear();
        return result != null ? result.toString() : "";
    }

    public List<RateModel> getBlueRatesFromBlueChargeCode(String chargeCode, String commNo) throws Exception {
        List<RateModel> rateModelList = null;
        List<RateModel> chargeModelList = new ArrayList<RateModel>();
        for (String blueCode : chargeCode.split(",")) {
            if (!CommonUtils.in(blueCode, "0001", "0060", "0015","OFBARR/TTBARR","OFBARR")) {
                rateModelList = new ArrayList<RateModel>();
                rateModelList.addAll(this.getBlueChargeList(this.origin, this.destination, commNo, blueCode));
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.origin, this.destination, "000000", blueCode));
                }
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.origin, this.pod, commNo, blueCode));
                }
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.origin, this.pod, "000000", blueCode));
                }
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.pol, this.destination, commNo, blueCode));
                }
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.pol, this.destination, "000000", blueCode));
                }
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.pol, this.pod, commNo, blueCode));
                }
                if (rateModelList.isEmpty()) {
                    rateModelList.addAll(this.getBlueChargeList(this.pol, this.pod, "000000", blueCode));
                }
                chargeModelList.addAll(rateModelList);
            }
        }
        return chargeModelList;
    }

    public List<RateModel> getBlueChargeList(String origin, String destination, String commNo, String blueChargeCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT chdcod as chargeCode,chgtyp as chargeType,flatrt as flatRate, ");
        sb.append(" totpct as totalPct,cuftrt as blueCft,wghtrt as blueWgt,minchg as blueMin, ");
        sb.append(" insurt as blueInsurt,insamt as blueInsamt,cbmrt as blueCbm,kgsrt as blueKgs ");
        sb.append(" FROM ").append(this.dataBaseName).append(".prtchg WHERE trmnum=(select trmnum from terminal ");
        sb.append(" where unlocationCode1=:origin and actyon =:rateType limit 1) ");
        sb.append(" and prtnum=(select eciportcode from ports where unlocationcode=:destination limit 1)  ");
        sb.append(" and chdcod IN(:chdcod) AND commod=:commNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        query.setParameter("commNo", commNo);
        query.setParameter("rateType", this.rateType);
        query.setParameter("chdcod", blueChargeCode);
        query.setResultTransformer(Transformers.aliasToBean(RateModel.class));
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("chargeType", StringType.INSTANCE);
        query.addScalar("flatRate", BigDecimalType.INSTANCE);
        query.addScalar("totalPct", BigDecimalType.INSTANCE);
        query.addScalar("blueCft", BigDecimalType.INSTANCE);
        query.addScalar("blueWgt", BigDecimalType.INSTANCE);
        query.addScalar("blueMin", BigDecimalType.INSTANCE);
        query.addScalar("blueInsurt", BigDecimalType.INSTANCE);
        query.addScalar("blueInsamt", BigDecimalType.INSTANCE);
        query.addScalar("blueCbm", BigDecimalType.INSTANCE);
        query.addScalar("blueKgs", BigDecimalType.INSTANCE);
        return query.list();
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getEngMet() throws Exception {
        if (CommonUtils.isEmpty(engMet)) {
            Ports port = new PortsDAO().getPorts(this.destination);
            engMet = port != null ? port.getEngmet() : "E";
        }
        return engMet;
    }

    public void setEngMet(String engMet) {
        this.engMet = engMet;
    }

    public boolean isCalcHeavyOrExtraLength() {
        return calcHeavyOrExtraLength;
    }

    public void setCalcHeavyOrExtraLength(boolean calcHeavyOrExtraLength) {
        this.calcHeavyOrExtraLength = calcHeavyOrExtraLength;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getDeliveryMetro() {
        return deliveryMetro;
    }

    public void setDeliveryMetro(String deliveryMetro) {
        this.deliveryMetro = deliveryMetro;
    }

    public BigDecimal getValueOfGoods() {
        return valueOfGoods;
    }

    public void setValueOfGoods(BigDecimal valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
    }
}
