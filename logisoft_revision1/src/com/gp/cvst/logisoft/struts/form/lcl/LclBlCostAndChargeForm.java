/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclBlCostAndChargeForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclBlCostAndChargeForm.class);
    private String id;
    private String chargesCode;
    private String flatRateAmount;
    private String billCharge;
    private String fileNumber;
    private Long fileNumberId;
    private Integer chargesCodeId;
    private LclBlAc lclBlAc;
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
    private String billToParty;
    private String manualEntry;
    private String adjustmentAmount;
    private String adjustmentComment;
    // Spot Rate
    private String checkWM;
    private String rate;
    private String rateN;
    private String comment;
    private Boolean spotCheckBottom;
    private Boolean spotCheckOF;
    private String blBillingType;
    private String existBillToParty;
    private String fwdAcctNo;
     private BigDecimal docChargeAmount;

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBillCharge() {
        return billCharge;
    }

    public void setBillCharge(String billCharge) {
        this.billCharge = billCharge;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public LclBlCostAndChargeForm() {
        if (lclBlAc == null) {
            lclBlAc = new LclBlAc();
        }
        if (lclBlAc.getLclFileNumber() == null) {
            lclBlAc.setLclFileNumber(new LclFileNumber());
        }
    }

    public Integer getChargesCodeId() {
        return chargesCodeId;
    }

    public void setChargesCodeId(Integer chargesCodeId) {
        this.chargesCodeId = chargesCodeId;
    }

    public Long getFileNumberId() {
        if (lclBlAc.getLclFileNumber() != null) {
            return lclBlAc.getLclFileNumber().getId();
        }
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        if (lclBlAc.getLclFileNumber() != null) {
            lclBlAc.getLclFileNumber().setId(fileNumberId);
        }
        this.fileNumberId = fileNumberId;
    }

    public String getFlatRateAmount() {
        if (lclBlAc.getRatePerUnit() != null) {
            return NumberUtils.convertToTwoDecimal(lclBlAc.getRatePerUnit().doubleValue());
        }
        return flatRateAmount;
    }

    public String getCostAmount() {
        if (lclBlAc.getApAmount() != null) {
            return NumberUtils.convertToTwoDecimal(lclBlAc.getApAmount().doubleValue());
        }
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        if (costAmount != null && !costAmount.trim().equals("")) {
            lclBlAc.setApAmount(new BigDecimal(costAmount));
        } else {
            lclBlAc.setApAmount(new BigDecimal(0.00));
        }
    }

    public String getInvoiceNumber() {
        if (lclBlAc.getInvoiceNumber() != null) {
            return "" + lclBlAc.getInvoiceNumber();
        }
        return "";
    }

    public void setInvoiceNumber(String invoiceNumber) {
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            lclBlAc.setInvoiceNumber(invoiceNumber.toUpperCase());
        } else {
            lclBlAc.setInvoiceNumber(null);
        }
    }

    public String getUom() {
        if (lclBlAc != null && lclBlAc.getRateUom() != null && !lclBlAc.getRateUom().trim().equals("")) {
            return lclBlAc.getRateUom();
        }
        return "M";
    }

    public void setUom(String uom) {
        lclBlAc.setRateUom(uom);
    }

    public void setFlatRateAmount(String flatRateAmount) {
        if (flatRateAmount != null && !flatRateAmount.trim().equals("")) {
            lclBlAc.setRatePerUnit(new BigDecimal(flatRateAmount));
            lclBlAc.setArAmount(lclBlAc.getRatePerUnit());
        } else {
            lclBlAc.setRatePerUnit(new BigDecimal(0.00));
        }
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

    public LclBlAc getLclBlAc() {
        return lclBlAc;
    }

    public void setLclBlAc(LclBlAc lclBlAc) {
        this.lclBlAc = lclBlAc;
    }

    public String getMeasure() {
        if (lclBlAc.getRatePerVolumeUnit() != null) {
            return NumberUtils.convertToTwoDecimal(lclBlAc.getRatePerVolumeUnit().doubleValue());
        }
        return measure;
    }

    public void setMeasure(String measure) {
        if (measure != null && !measure.trim().equals("")) {
            lclBlAc.setRatePerVolumeUnit(new BigDecimal(measure));
        } else {
            lclBlAc.setRatePerVolumeUnit(new BigDecimal(0.00));
        }
    }

    public String getWeight() {
        if (lclBlAc.getRatePerWeightUnit() != null) {
            return NumberUtils.convertToTwoDecimal(lclBlAc.getRatePerWeightUnit().doubleValue());
        }
        return weight;
    }

    public void setWeight(String weight) {
        if (weight != null && !weight.trim().equals("")) {
            lclBlAc.setRatePerWeightUnit(new BigDecimal(weight));
        } else {
            lclBlAc.setRatePerWeightUnit(new BigDecimal(0.00));
        }
    }

    public String getMinimum() {
        if (lclBlAc.getRateFlatMinimum() != null) {
            return NumberUtils.convertToTwoDecimal(lclBlAc.getRateFlatMinimum().doubleValue());
        }
        return minimum;
    }

    public void setMinimum(String minimum) {
        if (minimum != null && !minimum.trim().equals("")) {
            lclBlAc.setRateFlatMinimum(new BigDecimal(minimum));
        } else {
            lclBlAc.setRateFlatMinimum(new BigDecimal(0.00));
        }
    }

    public String getThirdPartyname() {
        if (lclBlAc != null && lclBlAc.getSupAcct() != null && lclBlAc.getSupAcct().getAccountName() != null) {
            return lclBlAc.getSupAcct().getAccountName();
        }
        return "";
    }

    public void setThirdPartyname(String thirdPartyname) {
        this.thirdPartyname = thirdPartyname;
    }

    public String getThirdpartyaccountNo() {
        if (lclBlAc != null && lclBlAc.getSupAcct() != null && lclBlAc.getSupAcct().getAccountno() != null) {
            return lclBlAc.getSupAcct().getAccountno();
        }
        return "";
    }

    public void setThirdpartyaccountNo(String thirdpartyaccountNo) throws Exception {
        if (CommonUtils.isNotEmpty(thirdpartyaccountNo)) {
            lclBlAc.setSupAcct(new TradingPartnerDAO().findById(thirdpartyaccountNo));
        } else {
            lclBlAc.setSupAcct(null);
        }
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

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String id = request.getParameter("id");
        if (CommonUtils.isNotEmpty(id)) {
            try {
                lclBlAc = new LclBlAcDAO().findById(Long.parseLong(id));
            } catch (Exception ex) {
                log.info("reset()in LclBlCostAndChargeForm failed on " + new Date(), ex);
            }
        }
        if (lclBlAc == null) {
            lclBlAc = new LclBlAc();
        }
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(String manualEntry) {
        this.manualEntry = manualEntry;
    }

    public String getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(String adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getAdjustmentComment() {
        return adjustmentComment;
    }

    public void setAdjustmentComment(String adjustmentComment) {
        this.adjustmentComment = adjustmentComment;
    }

    public String getCheckWM() {
        return checkWM;
    }

    public void setCheckWM(String checkWM) {
        this.checkWM = checkWM;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getBlBillingType() {
        return blBillingType;
    }

    public void setBlBillingType(String blBillingType) {
        this.blBillingType = blBillingType;
    }

    public String getExistBillToParty() {
        return existBillToParty;
    }

    public void setExistBillToParty(String existBillToParty) {
        this.existBillToParty = existBillToParty;
    }

    public String getFwdAcctNo() {
        return fwdAcctNo;
    }

    public void setFwdAcctNo(String fwdAcctNo) {
        this.fwdAcctNo = fwdAcctNo;
    }

    public BigDecimal getDocChargeAmount() {
        return docChargeAmount;
    }

    public void setDocChargeAmount(BigDecimal docChargeAmount) {
        this.docChargeAmount = docChargeAmount;
    }
    
}
