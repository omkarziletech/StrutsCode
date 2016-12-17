/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclQuoteCostAndChargeForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclQuoteCostAndChargeForm.class);
    private Long id;
    private String chargesCode;
    private String flatRateAmount;
    private BigDecimal dollarAmount;
    private String billCharge;
    private String fileNumber;
    private Long fileNumberId;
    private Integer chargesCodeId;
    private LclQuoteAc lclQuoteAc;
    private String minimumForCost;
    private String weightForCost;
    private String measureForCost;
    private String weight;
    private String measure;
    private String minimum;
    private String thirdPartyname;
    private String costAmount;
    private String invoiceNumber;
    private String thirdpartyaccountNo;
    private String chargeCode;
    private String destination;
    private String billingType;
    private String checkWM;
    private String rate;
    private String rateN;
    private Boolean spotCheckBottom;
    private Boolean spotCheckOF;
    private String billToParty;
    private String moduleName;
    private String chargesCodeDesc;
    private Boolean manualEntry;
    private BigDecimal arAmount;
    private String apAmount;
    private String fileNumberStatus;
    private String destinatnSersRemark;
    private String cityName;
    private String alternateAgent;
    private String alternateAgentAccntNo;
    private boolean manualCityChk;
    private String manualCityName;
    private Integer totaldestCarriageTT;
    private Integer totalDestTT;
    private Integer destFrequency;
    private Integer profitMax;
    private Integer profitMin;

    public String getAlternateAgent() {
        return alternateAgent;
    }

    public void setAlternateAgent(String alternateAgent) {
        this.alternateAgent = alternateAgent;
    }

    public String getAlternateAgentAccntNo() {
        return alternateAgentAccntNo;
    }

    public void setAlternateAgentAccntNo(String alternateAgentAccntNo) {
        this.alternateAgentAccntNo = alternateAgentAccntNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDestinatnSersRemark() {
        return destinatnSersRemark;
    }

    public void setDestinatnSersRemark(String destinatnSersRemark) {
        this.destinatnSersRemark = destinatnSersRemark;
    }

    public String getFileNumberStatus() {
        return fileNumberStatus;
    }

    public void setFileNumberStatus(String fileNumberStatus) {
        this.fileNumberStatus = fileNumberStatus;
    }

    public String getCheckWM() {
        if (null == checkWM) {
            return "M";
        }
        return checkWM;
    }

    public void setCheckWM(String checkWM) {
        this.checkWM = checkWM;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    private String comment;

    public BigDecimal getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(BigDecimal dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public String getBillCharge() {
        if (lclQuoteAc != null && lclQuoteAc.getBillCharge() != null && !lclQuoteAc.getBillCharge().trim().equals("")) {
            return lclQuoteAc.getBillCharge();
        }
        return "BL";
    }

    public void setBillCharge(String billCharge) {
        lclQuoteAc.setBillCharge(billCharge);
    }

    public String getInvoiceNumber() {
        return (null != lclQuoteAc.getInvoiceNumber() ? lclQuoteAc.getInvoiceNumber() : "");
    }

    public void setInvoiceNumber(String invoiceNumber) {
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            lclQuoteAc.setInvoiceNumber(invoiceNumber);
        } else {
            lclQuoteAc.setInvoiceNumber(null);
        }
    }

    public String getMeasureForCost() {
//        if (lclQuoteAc.getCostMeasure() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMeasure().doubleValue());
//        }
        return measureForCost;
    }

    public void setMeasureForCost(String measureForCost) {
        this.measureForCost = measureForCost;
//        if (measureForCost != null && !measureForCost.trim().equals("")) {
//            lclQuoteAc.setCostMeasure(new BigDecimal(measureForCost));
//        } else {
//            lclQuoteAc.setCostMeasure(new BigDecimal(0.00));
//        }
    }

    public String getWeightForCost() {
//        if (lclQuoteAc.getCostWeight() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostWeight().doubleValue());
//        }
        return weightForCost;
    }

    public void setWeightForCost(String weightForCost) {
        this.weightForCost = weightForCost;
//        if (weightForCost != null && !weightForCost.trim().equals("")) {
//            lclQuoteAc.setCostWeight(new BigDecimal(weightForCost));
//        } else {
//            lclQuoteAc.setCostWeight(new BigDecimal(0.00));
//        }
    }

    public String getMinimumForCost() {
//        if (lclQuoteAc.getCostMinimum() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMinimum().doubleValue());
//        }
        return minimumForCost;
    }

    public void setMinimumForCost(String minimumForCost) {
        this.minimumForCost = minimumForCost;
//        if (minimumForCost != null && !minimumForCost.trim().equals("")) {
//            lclQuoteAc.setCostMinimum(new BigDecimal(minimumForCost));
//        } else {
//            lclQuoteAc.setCostMinimum(new BigDecimal(0.00));
//        }
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public LclQuoteCostAndChargeForm() {
        if (lclQuoteAc == null) {
            lclQuoteAc = new LclQuoteAc();
        }
        if (lclQuoteAc.getLclFileNumber() == null) {
            lclQuoteAc.setLclFileNumber(new LclFileNumber());
        }
    }

    public Integer getChargesCodeId() {
        return chargesCodeId;
    }

    public void setChargesCodeId(Integer chargesCodeId) {
        this.chargesCodeId = chargesCodeId;
    }

    public Long getFileNumberId() {
        if (lclQuoteAc.getLclFileNumber() != null) {
            return lclQuoteAc.getLclFileNumber().getId();
        }
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        if (lclQuoteAc.getLclFileNumber() != null) {
            lclQuoteAc.getLclFileNumber().setId(fileNumberId);
        }
        this.fileNumberId = fileNumberId;
    }

    public String getFlatRateAmount() {
//        if (lclQuoteAc.getRatePerUnit() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerUnit().doubleValue());
//        }
        return flatRateAmount;
    }

    public String getCostAmount() {
//        if (lclQuoteAc.getApAmount() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getApAmount().doubleValue());
//        }
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
//        if (costAmount != null && !costAmount.trim().equals("")) {
//            lclQuoteAc.setApAmount(new BigDecimal(costAmount));
//        } else {
//            lclQuoteAc.setApAmount(new BigDecimal(0.00));
//        }
    }

    public String getUom() {
        if (lclQuoteAc != null && lclQuoteAc.getRateUom() != null && !lclQuoteAc.getRateUom().trim().equals("")) {
            return lclQuoteAc.getRateUom();
        }
        return "M";
    }

    public void setUom(String uom) {
        lclQuoteAc.setRateUom(uom);
    }

    public void setFlatRateAmount(String flatRateAmount) {
        this.flatRateAmount = flatRateAmount;
//        if (flatRateAmount != null && !flatRateAmount.trim().equals("")) {
//            lclQuoteAc.setRatePerUnit(new BigDecimal(flatRateAmount));
//            lclQuoteAc.setArAmount(lclQuoteAc.getRatePerUnit());
//            lclQuoteAc.setRatePerUnitUom("FL");
//        } else {
//            lclQuoteAc.setRatePerUnit(new BigDecimal(0.00));
//        }
    }

    public String getChargesCode() {
        return chargesCode;
    }

    public void setChargesCode(String chargesCode) {
        this.chargesCode = chargesCode;
    }

    public Long getId() {
        return lclQuoteAc.getId();
    }

    public void setId(Long id) {
        if (id != 0) {
            lclQuoteAc.setId(id);
        }
    }

    public LclQuoteAc getLclQuoteAc() {
        return lclQuoteAc;
    }

    public void setLclQuoteAc(LclQuoteAc lclQuoteAc) {
        this.lclQuoteAc = lclQuoteAc;
    }

    public String getMeasure() {
//        if (lclQuoteAc.getRatePerVolumeUnit() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue());
//        }
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
//        if (measure != null && !measure.trim().equals("")) {
//            lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(measure));
//        } else {
//            lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(0.00));
//        }
    }

    public String getWeight() {
//        if (lclQuoteAc.getRatePerWeightUnit() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue());
//        }
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
//        if (weight != null && !weight.trim().equals("")) {
//            lclQuoteAc.setRatePerWeightUnit(new BigDecimal(weight));
//        } else {
//            lclQuoteAc.setRatePerWeightUnit(new BigDecimal(0.00));
//        }
    }

    public String getMinimum() {
//        if (lclQuoteAc.getRateFlatMinimum() != null) {
//            return NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue());
//        }
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
//        if (minimum != null && !minimum.trim().equals("")) {
//            lclQuoteAc.setRateFlatMinimum(new BigDecimal(minimum));
//        } else {
//            lclQuoteAc.setRateFlatMinimum(new BigDecimal(0.00));
//        }
    }

    public String getThirdPartyname() {
        if (lclQuoteAc != null && lclQuoteAc.getSupAcct() != null && lclQuoteAc.getSupAcct().getAccountName() != null) {
            return lclQuoteAc.getSupAcct().getAccountName();
        }
        return "";
    }

    public void setThirdPartyname(String thirdPartyname) {
        this.thirdPartyname = thirdPartyname;
    }

    public String getThirdpartyaccountNo() {
        if (lclQuoteAc != null && lclQuoteAc.getSupAcct() != null && lclQuoteAc.getSupAcct().getAccountno() != null) {
            return lclQuoteAc.getSupAcct().getAccountno();
        }
        return "";
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getRateN() {
        return rateN;
    }

    public void setRateN(String rateN) {
        this.rateN = rateN;
    }

    public Boolean getSpotCheckBottom() {
        return spotCheckBottom;
    }

    public void setSpotCheckBottom(Boolean spotCheckBottom) {
        this.spotCheckBottom = spotCheckBottom;
    }

    public Boolean getSpotCheckOF() {
        return spotCheckOF;
    }

    public void setSpotCheckOF(Boolean spotCheckOF) {
        this.spotCheckOF = spotCheckOF;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getChargesCodeDesc() {
        return chargesCodeDesc;
    }

    public void setChargesCodeDesc(String chargesCodeDesc) {
        this.chargesCodeDesc = chargesCodeDesc;
    }

    public Boolean getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public String getApAmount() {
        return apAmount;
    }

    public void setApAmount(String apAmount) {
        this.apAmount = apAmount;
    }

    public void setThirdpartyaccountNo(String thirdpartyaccountNo) throws Exception {
        if (CommonUtils.isNotEmpty(thirdpartyaccountNo)) {
            lclQuoteAc.setSupAcct(new TradingPartnerDAO().findById(thirdpartyaccountNo));
        } else {
            lclQuoteAc.setSupAcct(null);
        }
    }

    public boolean isManualCityChk() {
        return manualCityChk;
    }

    public void setManualCityChk(boolean manualCityChk) {
        this.manualCityChk = manualCityChk;
    }

    public String getManualCityName() {
        return manualCityName;
    }

    public void setManualCityName(String manualCityName) {
        this.manualCityName = manualCityName;
    }

    public Integer getTotaldestCarriageTT() {
        return totaldestCarriageTT;
    }

    public void setTotaldestCarriageTT(Integer totaldestCarriageTT) {
        this.totaldestCarriageTT = totaldestCarriageTT;
    }

    public Integer getTotalDestTT() {
        return totalDestTT;
    }

    public void setTotalDestTT(Integer totalDestTT) {
        this.totalDestTT = totalDestTT;
    }

    public Integer getDestFrequency() {
        return destFrequency;
    }

    public void setDestFrequency(Integer destFrequency) {
        this.destFrequency = destFrequency;
    }

    public Integer getProfitMax() {
        return profitMax;
    }

    public void setProfitMax(Integer profitMax) {
        this.profitMax = profitMax;
    }

    public Integer getProfitMin() {
        return profitMin;
    }

    public void setProfitMin(Integer profitMin) {
        this.profitMin = profitMin;
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String id = request.getParameter("id");
        if (CommonUtils.isNotEmpty(id)) {
            try {
                lclQuoteAc = new LclQuoteAcDAO().findById(Long.parseLong(id));
            } catch (Exception ex) {
                log.info("reset()in LclQuoteCostAndChargeForm failed on " + new Date(), ex);
            }
        }
        if (lclQuoteAc == null) {
            lclQuoteAc = new LclQuoteAc();
        }
    }
}
