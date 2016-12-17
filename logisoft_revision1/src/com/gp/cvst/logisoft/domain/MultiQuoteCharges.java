/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.domain;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.GenericCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author 
 */
@Entity
@Table(name = "multi_quote_charges")
@DynamicInsert(true)
@DynamicUpdate(true)

public class MultiQuoteCharges extends Domain  implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "unitType")
    private String unitType;
    @Column(name = "unit_no")
    private String unitNo;
    @Column(name = "charge_code")
    private String chargeCode;
    @Column(name = "charge_code_desc")
    private String chargeCodeDesc;
   
    @Column(name = "amount")
    private Double amount;
    @Column(name = "number")
    private String number;
    @Column(name = "cost_type")
    private String costType;
    @Column(name = "markup")
    private Double markUp;
    @Column(name = "retail")
    private Double retail;
    @Column(name = "currency")
    private String currency;
    @Column(name = "effective_date")
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "charge_flag")
    private String chargeFlag;
    @Column(name = "new_flag")
    private String newFlag;
    @Column(name = "comment")
    private String comment;
    @Column(name = "adjustment")
    private Double adjustment;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateOn;
    @Column(name = "default_carrier")
    private String defaultCarrier;
    @Column(name = "out_of_gauge")
    private String outOfGauge;
    @Column(name = "out_of_gauge_comment")
    private String outOfGaugeComment;
    @Column(name = "adjustment_charge_comments")
    private String adjustmentChargeComments;
    
    @Column(name = "minimum")
    private Double minimum;    
    @Column(name = "include")
    private String include;    
    @Column(name = "print")
    private String print;
    @Column (name ="standard_charge")
    private String standardCharge;  
    
    @JoinColumn(name = "multi_quote_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MultiQuote multiQuote;
    // Transient object
    @Transient
    private GenericCode chg_Code;
    @Transient
    private GenericCode cost_type;
    @Transient
    private GenericCode currency_1;

    public MultiQuoteCharges() {
    }

    public MultiQuoteCharges(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public Double getMarkUp() {
        return markUp;
    }

    public void setMarkUp(Double markUp) {
        this.markUp = markUp;
    }

    public Double getRetail() {
        return retail;
    }

    public void setRetail(Double retail) {
        this.retail = retail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public String getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    public String getDefaultCarrier() {
        return defaultCarrier;
    }

    public void setDefaultCarrier(String defaultCarrier) {
        this.defaultCarrier = defaultCarrier;
    }

    public String getOutOfGauge() {
        return outOfGauge;
    }

    public void setOutOfGauge(String outOfGauge) {
        this.outOfGauge = outOfGauge;
    }

    public String getOutOfGaugeComment() {
        return outOfGaugeComment;
    }

    public void setOutOfGaugeComment(String outOfGaugeComment) {
        this.outOfGaugeComment = outOfGaugeComment;
    }

    public String getAdjustmentChargeComments() {
        return adjustmentChargeComments;
    }

    public void setAdjustmentChargeComments(String adjustmentChargeComments) {
        this.adjustmentChargeComments = adjustmentChargeComments;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getStandardCharge() {
        return standardCharge;
    }

    public void setStandardCharge(String standardCharge) {
        this.standardCharge = standardCharge;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }
    
    
    
    
    
    // Join
    public MultiQuote getMultiQuote() {
        return multiQuote;
    }

    public void setMultiQuote(MultiQuote multiQuote) {
        this.multiQuote = multiQuote;
    }

    public GenericCode getChg_Code() {
        return chg_Code;
    }

    public void setChg_Code(GenericCode chg_Code) {
        this.chg_Code = chg_Code;
    }

    public GenericCode getCost_type() {
        return cost_type;
    }

    public void setCost_type(GenericCode cost_type) {
        this.cost_type = cost_type;
    }

    public GenericCode getCurrency_1() {
        return currency_1;
    }

    public void setCurrency_1(GenericCode currency_1) {
        this.currency_1 = currency_1;
    }


    
    
    
 

    
    
    
    
    
    
    
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MultiQuoteCharges)) {
            return false;
        }
        MultiQuoteCharges other = (MultiQuoteCharges) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cvst.logisoft.domain.MultiQuoteCharges[ id=" + id + " ]";
    }
    
}
