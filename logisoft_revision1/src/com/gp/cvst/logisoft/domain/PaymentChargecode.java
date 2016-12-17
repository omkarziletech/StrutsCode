package com.gp.cvst.logisoft.domain;

public class PaymentChargecode implements java.io.Serializable {

    // Fields    
    private Integer id;
    private Integer batchId;
    private String custId;
    private String checkNo;
    private String chargeCode;
    private Double chargeCodeAmt;
    private String notes;
    private Integer paymentCheckId;

    // Constructors
    /**
     * default constructor
     */
    public PaymentChargecode() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Double getChargeCodeAmt() {
        return chargeCodeAmt;
    }

    public void setChargeCodeAmt(Double chargeCodeAmt) {
        this.chargeCodeAmt = chargeCodeAmt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPaymentCheckId() {
        return paymentCheckId;
    }

    public void setPaymentCheckId(Integer paymentCheckId) {
        this.paymentCheckId = paymentCheckId;
    }
}
