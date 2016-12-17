package com.gp.cvst.logisoft.domain;

import java.util.Date;

/**
 * PaymentOnAccount generated by MyEclipse - Hibernate Tools
 */
public class PaymentOnAccount implements java.io.Serializable {

    // Fields    
    private Integer id;
    private Integer batchId;
    private Date batchDate;
    private String custId;
    private String checkNo;
    private Date checkDate;
    private Double onAccountAmt;

    // Constructors
    /**
     * default constructor
     */
    public PaymentOnAccount() {
    }

    /**
     * full constructor
     */
    public PaymentOnAccount(Integer batchId, Date batchDate, String custId, String checkNo, Date checkDate, Double onAccountAmt) {
        this.batchId = batchId;
        this.batchDate = batchDate;
        this.custId = custId;
        this.checkNo = checkNo;
        this.checkDate = checkDate;
        this.onAccountAmt = onAccountAmt;
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

    public Double getOnAccountAmt() {
        return this.onAccountAmt;
    }

    public void setOnAccountAmt(Double onAccountAmt) {
        this.onAccountAmt = onAccountAmt;
    }
}