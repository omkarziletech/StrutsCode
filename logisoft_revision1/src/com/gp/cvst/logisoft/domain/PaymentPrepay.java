package com.gp.cvst.logisoft.domain;

import java.util.Date;


/**
 * PaymentPrepay generated by MyEclipse - Hibernate Tools
 */
public class PaymentPrepay implements java.io.Serializable {

    // Fields    
    private Integer id;
    private Integer batchId;
    private Date batchDate;
    private String custId;
    private String checkNo;
    private Date checkDate;
    private String dockReceiptNo;
    private Double prepayAmt;
    private String notes;
    private Integer paymentCheckId;

    // Constructors
    /**
     * default constructor
     */
    public PaymentPrepay() {
    }

    /**
     * full constructor
     */
    public PaymentPrepay(Integer batchId, Date batchDate, String custId, String checkNo, Date checkDate, String dockReceiptNo, Double prepayAmt) {
        this.batchId = batchId;
        this.batchDate = batchDate;
        this.custId = custId;
        this.checkNo = checkNo;
        this.checkDate = checkDate;
        this.dockReceiptNo = dockReceiptNo;
        this.prepayAmt = prepayAmt;
    }
    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Date getBatchDate() {
        return this.batchDate;
    }

    public void setBatchDate(Date batchDate) {
        this.batchDate = batchDate;
    }

    public String getCustId() {
        return this.custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCheckNo() {
        return this.checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public Date getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getDockReceiptNo() {
        return this.dockReceiptNo;
    }

    public void setDockReceiptNo(String dockReceiptNo) {
        this.dockReceiptNo = dockReceiptNo;
    }

    public Double getPrepayAmt() {
        return this.prepayAmt;
    }

    public void setPrepayAmt(Double prepayAmt) {
        this.prepayAmt = prepayAmt;
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