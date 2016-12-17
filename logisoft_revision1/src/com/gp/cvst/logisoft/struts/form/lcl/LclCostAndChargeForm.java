/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclCostAndChargeForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclCostAndChargeForm.class);
    private String id;
    private String chargesCode;
    private String flatRateAmount;
    private BigDecimal dollarAmount;
    private BigDecimal adjustmentAmount;
    private String chargeAmount;
    private String billCharge;
    private String fileNumber;
    private Long fileNumberId;
    private Integer chargesCodeId;
    private LclBookingAc lclBookingAc;
    private String weight;
    private String measure;
    private String minimum;
    private String minimumForCost;
    private String weightForCost;
    private String measureForCost;
    private String thirdPartyname;
    private String costAmount;
    private String invoiceNumber;
    private String thirdpartyaccountNo;
    private String destination;
    private String commodityNumber;
    private String billingType;
    private String checkWM;
    private String rate;
    private String rateN;
    private String comment;
    private Boolean spotCheckBottom;
    private Boolean spotCheckOF;
    private String billToParty;
    //hiddenValues
    private String agentNo;
    private String fileNumberStatus;
    private String moduleName;
    private String manualEntry;
    private String arAmount;
    private String cityName;
    private String alternateAgent;
    private String alternateAgentAccntNo;
    private String destinatnSersRemark;
    private String lastInvoice;
    private String costStatus;
    private String chargeInvoiceNumber;
    private String billToPartyOldValue;
    private String hiddenChargeCode;
    private String hiddenBillToParty;
    private String bluescreenchargecode;
    private boolean manualCityChk;
    private String manualCityName;
    private Integer totaldestCarriageTT;
    private String bkgBillingType;
    private String bkgBillToParty;
    private String existBillToParty;
    private Integer totalDestTT;
    private Integer destFrequency;
    private Integer profitMax;
    private Integer profitMin;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateN() {
        return rateN;
    }

    public void setRateN(String rateN) {
        this.rateN = rateN;
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

    public BigDecimal getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(BigDecimal dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public LclCostAndChargeForm() {
        if (lclBookingAc == null) {
            lclBookingAc = new LclBookingAc();
        }
        if (lclBookingAc.getLclFileNumber() == null) {
            lclBookingAc.setLclFileNumber(new LclFileNumber());
        }
    }

    public Integer getChargesCodeId() {
        return chargesCodeId;
    }

    public void setChargesCodeId(Integer chargesCodeId) {
        this.chargesCodeId = chargesCodeId;
    }

    public String getBillCharge() {
        if (lclBookingAc != null && lclBookingAc.getBillCharge() != null && !lclBookingAc.getBillCharge().trim().equals("")) {
            return lclBookingAc.getBillCharge();
        }
        return "BL";
    }

    public void setBillCharge(String billCharge) {
        lclBookingAc.setBillCharge(billCharge);
    }

    public Long getFileNumberId() {
        if (lclBookingAc.getLclFileNumber() != null) {
            return lclBookingAc.getLclFileNumber().getId();
        }
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        if (lclBookingAc.getLclFileNumber() != null) {
            lclBookingAc.getLclFileNumber().setId(fileNumberId);
        }
        this.fileNumberId = fileNumberId;
    }

    public String getFlatRateAmount() {
        return flatRateAmount;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public String getInvoiceNumber() {
        if (lclBookingAc.getInvoiceNumber() != null) {
            return "" + lclBookingAc.getInvoiceNumber();
        }
        return "";
    }

    public void setInvoiceNumber(String invoiceNumber) {
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            lclBookingAc.setInvoiceNumber(invoiceNumber.toUpperCase());
        } else {
            lclBookingAc.setInvoiceNumber(null);
        }
    }

    public String getCommodityNumber() {
        return commodityNumber;
    }

    public void setCommodityNumber(String commodityNumber) {
        this.commodityNumber = commodityNumber;
    }

    public String getUom() {
        if (lclBookingAc != null && lclBookingAc.getRateUom() != null && !lclBookingAc.getRateUom().trim().equals("")) {
            return lclBookingAc.getRateUom();
        }
        return "M";
    }

    public void setUom(String uom) {
        lclBookingAc.setRateUom(uom);
    }

    public void setFlatRateAmount(String flatRateAmount) {
        this.flatRateAmount = flatRateAmount;
    }

    public String getChargesCode() {
        return chargesCode;
    }

    public void setChargesCode(String chargesCode) {
        this.chargesCode = chargesCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LclBookingAc getLclBookingAc() {
        return lclBookingAc;
    }

    public void setLclBookingAc(LclBookingAc lclBookingAc) {
        this.lclBookingAc = lclBookingAc;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasureForCost() {
        return measureForCost;
    }

    public void setMeasureForCost(String measureForCost) {
        this.measureForCost = measureForCost;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightForCost() {
        return weightForCost;
    }

    public void setWeightForCost(String weightForCost) {
        this.weightForCost = weightForCost;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMinimumForCost() {
        return minimumForCost;
    }

    public void setMinimumForCost(String minimumForCost) {
        this.minimumForCost = minimumForCost;
    }

    public String getThirdPartyname() {
        if (lclBookingAc != null && lclBookingAc.getSupAcct() != null && lclBookingAc.getSupAcct().getAccountName() != null) {
            return lclBookingAc.getSupAcct().getAccountName();
        }
        return "";
    }

    public void setThirdPartyname(String thirdPartyname) {
        this.thirdPartyname = thirdPartyname;
    }

    public String getThirdpartyaccountNo() {
        if (lclBookingAc != null && lclBookingAc.getSupAcct() != null && lclBookingAc.getSupAcct().getAccountno() != null) {
            return lclBookingAc.getSupAcct().getAccountno();
        }
        return "";
    }

    public void setThirdpartyaccountNo(String thirdpartyaccountNo) throws Exception {
        if (CommonUtils.isNotEmpty(thirdpartyaccountNo)) {
            lclBookingAc.setSupAcct(new TradingPartnerDAO().findById(thirdpartyaccountNo));
        } else {
            lclBookingAc.setSupAcct(null);
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
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

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getFileNumberStatus() {
        return fileNumberStatus;
    }

    public void setFileNumberStatus(String fileNumberStatus) {
        this.fileNumberStatus = fileNumberStatus;
    }

    public String getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(String manualEntry) {
        this.manualEntry = manualEntry;
    }

    public String getArAmount() {
        return arAmount;
    }

    public void setArAmount(String arAmount) {
        this.arAmount = arAmount;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String id = request.getParameter("id");
        if (CommonUtils.isNotEmpty(id)) {
            try {
                lclBookingAc = new LclCostChargeDAO().findById(Long.parseLong(id));
            } catch (Exception ex) {
                log.info("reset()in LclCostAndChargeForm failed on " + new Date(), ex);
            }
        }
        if (lclBookingAc == null) {
            lclBookingAc = new LclBookingAc();
        }
    }

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

    public String getLastInvoice() {
        return lastInvoice;
    }

    public void setLastInvoice(String lastInvoice) {
        this.lastInvoice = lastInvoice;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }

    public String getChargeInvoiceNumber() {
        return chargeInvoiceNumber;
    }

    public void setChargeInvoiceNumber(String chargeInvoiceNumber) {
        this.chargeInvoiceNumber = chargeInvoiceNumber;
    }

    public String getBillToPartyOldValue() {
        return billToPartyOldValue;
    }

    public void setBillToPartyOldValue(String billToPartyOldValue) {
        this.billToPartyOldValue = billToPartyOldValue;
    }

    public String getHiddenChargeCode() {
        return hiddenChargeCode;
    }

    public void setHiddenChargeCode(String hiddenChargeCode) {
        this.hiddenChargeCode = hiddenChargeCode;
    }

    public String getHiddenBillToParty() {
        return hiddenBillToParty;
    }

    public void setHiddenBillToParty(String hiddenBillToParty) {
        this.hiddenBillToParty = hiddenBillToParty;
    }

    public String getBluescreenchargecode() {
        return bluescreenchargecode;
    }

    public void setBluescreenchargecode(String bluescreenchargecode) {
        this.bluescreenchargecode = bluescreenchargecode;
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

    public String getBkgBillToParty() {
        return bkgBillToParty;
    }

    public void setBkgBillToParty(String bkgBillToParty) {
        this.bkgBillToParty = bkgBillToParty;
    }

    public String getBkgBillingType() {
        return bkgBillingType;
    }

    public void setBkgBillingType(String bkgBillingType) {
        this.bkgBillingType = bkgBillingType;
    }

    public String getExistBillToParty() {
        return existBillToParty;
    }

    public void setExistBillToParty(String existBillToParty) {
        this.existBillToParty = existBillToParty;
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

}
