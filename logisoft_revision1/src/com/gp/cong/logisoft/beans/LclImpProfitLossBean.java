/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

/**
 *
 * @author VinothS
 */
public class LclImpProfitLossBean {

//    String unitSsId;
//    String chargeNo;
//    String chargeCode;
//    String chargeAmount;
//    String costNo;
//    String costCode;
//    String costAmount;
//    Double totalChargeAmt = 0.00;
//    Double totalCostAmt = 0.00;
//    Double netAmount = 0.00;
    private String blueChargeCode;
    private String chargeCode;
    private Double arAmount;
    private String blueCostCode;
    private String costCode;
    private Double apAmount;
    private String keyChargeCode;
    private String keyCostCode;
    private String chargeVendorNo;
    private String chargeVendorName;
    private String costVendorNo;
    private String costVendorName;
    private String fileStatus;
    private String bookingType;
    private boolean bluScreenFlag;
    private String agentBilledFlag;
    private String agentFlag;
    private String billtoParty;

    public String getBlueChargeCode() {
        return blueChargeCode;
    }

    public void setBlueChargeCode(String blueChargeCode) {
        this.blueChargeCode = blueChargeCode;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Double getArAmount() {
        return arAmount;
    }

    public void setArAmount(Double arAmount) {
        this.arAmount = arAmount;
    }

    public String getBlueCostCode() {
        return blueCostCode;
    }

    public void setBlueCostCode(String blueCostCode) {
        this.blueCostCode = blueCostCode;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public Double getApAmount() {
        return apAmount;
    }

    public void setApAmount(Double apAmount) {
        this.apAmount = apAmount;
    }

    public String getKeyChargeCode() {
        return keyChargeCode;
    }

    public void setKeyChargeCode(String keyChargeCode) {
        this.keyChargeCode = keyChargeCode;
    }

    public String getKeyCostCode() {
        return keyCostCode;
    }

    public void setKeyCostCode(String keyCostCode) {
        this.keyCostCode = keyCostCode;
    }

    public String getChargeVendorNo() {
        return chargeVendorNo;
    }

    public void setChargeVendorNo(String chargeVendorNo) {
        this.chargeVendorNo = chargeVendorNo;
    }

    public String getChargeVendorName() {
        return chargeVendorName;
    }

    public void setChargeVendorName(String chargeVendorName) {
        this.chargeVendorName = chargeVendorName;
    }

    public String getCostVendorNo() {
        return costVendorNo;
    }

    public void setCostVendorNo(String costVendorNo) {
        this.costVendorNo = costVendorNo;
    }

    public String getCostVendorName() {
        return costVendorName;
    }

    public void setCostVendorName(String costVendorName) {
        this.costVendorName = costVendorName;
    }

    public boolean isBluScreenFlag() {
        return bluScreenFlag;
    }

    public void setBluScreenFlag(boolean bluScreenFlag) {
        this.bluScreenFlag = bluScreenFlag;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getAgentBilledFlag() {
        return agentBilledFlag;
    }

    public void setAgentBilledFlag(String agentBilledFlag) {
        this.agentBilledFlag = agentBilledFlag;
    }
      
    public String getAgentFlag() {
        return agentFlag;
    }

    public void setAgentFlag(String agentFlag) {
        this.agentFlag = agentFlag;
    }
    
    public String getBilltoParty() {
        return billtoParty;
    }

    public void setBilltoParty(String billtoParty) {
        this.billtoParty = billtoParty;
    }
//    public String getChargeAmount() {
//        return chargeAmount;
//    }
//
//    public void setChargeAmount(String chargeAmount) {
//        if (CommonUtils.isNotEmpty(chargeAmount)) {
//            for (String charge : chargeAmount.split(",")) {
//                if (CommonUtils.isNotEmpty(charge)) {
//                    this.totalChargeAmt += Double.parseDouble(charge);
//                }
//            }
//        }
//        this.chargeAmount = chargeAmount;
//    }
//
//    public String getChargeCode() {
//        return chargeCode;
//    }
//
//    public void setChargeCode(String chargeCode) {
//        this.chargeCode = chargeCode;
//    }
//
//    public String getChargeNo() {
//        return chargeNo;
//    }
//
//    public void setChargeNo(String chargeNo) {
//        this.chargeNo = chargeNo;
//    }
//
//    public String getCostAmount() {
//        return costAmount;
//    }
//
//    public void setCostAmount(String costAmount) {
//        try {
//            if (CommonUtils.isNotEmpty(costAmount)) {
//                for (String cost : costAmount.split(",")) {
//                    if (CommonUtils.isNotEmpty(cost)) {
//                        this.totalCostAmt += Double.parseDouble(cost);
//                    }
//                }
//                if (CommonUtils.isNotEmpty(unitSsId)) {
//                    List<LclSsAc> lclSsAcList = new LclSsAcDAO().findByProperty("lclUnitSs.id", new Long(unitSsId));
//                    if (CommonUtils.isNotEmpty(lclSsAcList)) {
//                        for (LclSsAc lclSsAc : lclSsAcList) {
//                            if (lclSsAc.getApGlMappingId().getChargeCode().equalsIgnoreCase(costCode)) {
//                                this.totalCostAmt += lclSsAc.getApAmount().doubleValue();
//                            }
//                        }
//                    }
//                }
//                this.netAmount = totalChargeAmt - totalCostAmt;
//            } else {
//                this.netAmount = totalChargeAmt;
//            }
//            this.costAmount = costAmount;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public String getCostCode() {
//        return costCode;
//    }
//
//    public void setCostCode(String costCode) {
//        if (CommonUtils.isNotEmpty(costCode)) {
//            StringBuilder cost = new StringBuilder();
//            Set<String> codes = new HashSet<String>(Arrays.asList(costCode.split(",")));
//            int i = 1;
//            for (String code : codes) {
//                if (CommonUtils.isNotEmpty(code)) {
//                    cost.append(code);
//                    if (i < codes.size()) {
//                        cost.append(",");
//                    }
//                    i++;
//                }
//            }
//            this.costCode = cost.toString();
//        }
//    }
//
//    public String getCostNo() {
//        return costNo;
//    }
//
//    public void setCostNo(String costNo) {
//        if (CommonUtils.isNotEmpty(costNo)) {
//            StringBuilder cost = new StringBuilder();
//            Set<String> nos = new HashSet<String>(Arrays.asList(costNo.split(",")));
//            int i = 1;
//            for (String code : nos) {
//                if (CommonUtils.isNotEmpty(code)) {
//                    cost.append(code);
//                    if (i < nos.size()) {
//                        cost.append(",");
//                    }
//                    i++;
//                }
//            }
//            this.costNo = cost.toString();
//        }
//    }
//
//    public Double getTotalChargeAmt() {
//        return totalChargeAmt;
//    }
//
//    public void setTotalChargeAmt(Double totalChargeAmt) {
//        this.totalChargeAmt = totalChargeAmt;
//    }
//
//    public Double getTotalCostAmt() {
//        return totalCostAmt;
//    }
//
//    public void setTotalCostAmt(Double totalCostAmt) {
//        this.totalCostAmt = totalCostAmt;
//    }
//
//    public Double getNetAmount() {
//        return netAmount;
//    }
//
//    public void setNetAmount(Double netAmount) {
//        this.netAmount = netAmount;
//    }
//
//    public String getUnitSsId() {
//        return unitSsId;
//    }
//
//    public void setUnitSsId(String unitSsId) {
//        this.unitSsId = unitSsId;
//    }
}
