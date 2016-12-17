package com.gp.cvst.logisoft.beans;

import java.math.BigDecimal;

/**
 *
 * @author NambuRajasekar.M
 */
public class MultiChargesOrderBean {

    private Long id;
    private Long order_id;
    private BigDecimal amount;
    private String unitNo;
    private String chargeCode;
    private String chargeCodeDesc;
    private String currency;
    private String acctNo;
    private String acctName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeCodeDesc() {
        return chargeCodeDesc;
    }

    public void setChargeCodeDesc(String chargeCodeDesc) {
        this.chargeCodeDesc = chargeCodeDesc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

}
