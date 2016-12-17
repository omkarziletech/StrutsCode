/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cvst.logisoft.domain.GlMapping;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author saravanan
 */
@Entity
@Table(name = "lcl_correction_charge")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
public class LclCorrectionCharge extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "old_amount")
    private BigDecimal oldAmount;
    @Basic(optional = false)
    @Column(name = "new_amount")
    private BigDecimal newAmount;
    @Column(name = "bill_to_party")
    private String billToParty;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "correction_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclCorrection lclCorrection;
    @JoinColumn(name = "gl_mapping_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GlMapping glMapping;
    @JoinColumn(name = "lcl_booking_ac_id", referencedColumnName = "id", nullable = true)
    @OneToOne
    private LclBookingAc lclBookingAc;
    @JoinColumn(name = "lcl_bl_ac_id", referencedColumnName = "id", nullable = true)
    @OneToOne
    private LclBlAc lclBlAc;
    @Column(name = "old_bill_to_party")
    private String oldBillToParty;
    @Column(name = "manual_charge")
    private boolean manualCharge;
    @Column(name = "rate_per_weight_unit")
    private BigDecimal ratePerWeightUnit;
    @Column(name = "rate_per_weight_unit_div")
    private BigDecimal ratePerWeightUnitDiv;
    @Column(name = "rate_per_volume_unit")
    private BigDecimal ratePerVolumeUnit;
    @Column(name = "rate_per_volume_unit_div")
    private BigDecimal ratePerVolumeUnitDiv;
    @Column(name = "minimum_rate")
    private BigDecimal minimumRate;
    @Column(name = "rate_per_unit_uom")
    private String ratePerUnitUom;
    @Basic(optional = false)
    @Column(name = "print_on_bl")
    private boolean printOnBl;

    public LclCorrectionCharge() {
    }

    public LclCorrectionCharge(Long id) {
        this.id = id;
    }

    public LclCorrectionCharge(Long id, BigDecimal newAmount, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.newAmount = newAmount;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public LclCorrection getLclCorrection() {
        return lclCorrection;
    }

    public void setLclCorrection(LclCorrection lclCorrection) {
        this.lclCorrection = lclCorrection;
    }

    public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    public GlMapping getGlMapping() {
        return glMapping;
    }

    public void setGlMapping(GlMapping glMapping) {
        this.glMapping = glMapping;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LclBookingAc getLclBookingAc() {
        return lclBookingAc;
    }

    public void setLclBookingAc(LclBookingAc lclBookingAc) {
        this.lclBookingAc = lclBookingAc;
    }

    public LclBlAc getLclBlAc() {
        return lclBlAc;
    }

    public void setLclBlAc(LclBlAc lclBlAc) {
        this.lclBlAc = lclBlAc;
    }

    public String getOldBillToParty() {
        return oldBillToParty;
    }

    public void setOldBillToParty(String oldBillToParty) {
        this.oldBillToParty = oldBillToParty;
    }

    public boolean isManualCharge() {
        return manualCharge;
    }

    public void setManualCharge(boolean manualCharge) {
        this.manualCharge = manualCharge;
    }

    public BigDecimal getRatePerWeightUnit() {
        return ratePerWeightUnit;
    }

    public void setRatePerWeightUnit(BigDecimal ratePerWeightUnit) {
        this.ratePerWeightUnit = ratePerWeightUnit;
    }

    public BigDecimal getRatePerWeightUnitDiv() {
        return ratePerWeightUnitDiv;
    }

    public void setRatePerWeightUnitDiv(BigDecimal ratePerWeightUnitDiv) {
        this.ratePerWeightUnitDiv = ratePerWeightUnitDiv;
    }

    public BigDecimal getRatePerVolumeUnit() {
        return ratePerVolumeUnit;
    }

    public void setRatePerVolumeUnit(BigDecimal ratePerVolumeUnit) {
        this.ratePerVolumeUnit = ratePerVolumeUnit;
    }

    public BigDecimal getRatePerVolumeUnitDiv() {
        return ratePerVolumeUnitDiv;
    }

    public void setRatePerVolumeUnitDiv(BigDecimal ratePerVolumeUnitDiv) {
        this.ratePerVolumeUnitDiv = ratePerVolumeUnitDiv;
    }

    public BigDecimal getMinimumRate() {
        return minimumRate;
    }

    public void setMinimumRate(BigDecimal minimumRate) {
        this.minimumRate = minimumRate;
    }

    public String getRatePerUnitUom() {
        return ratePerUnitUom;
    }

    public void setRatePerUnitUom(String ratePerUnitUom) {
        this.ratePerUnitUom = ratePerUnitUom;
    }

    public boolean isPrintOnBl() {
        return printOnBl;
    }

    public void setPrintOnBl(boolean printOnBl) {
        this.printOnBl = printOnBl;
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
        if (!(object instanceof LclCorrectionCharge)) {
            return false;
        }
        LclCorrectionCharge other = (LclCorrectionCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge[id=" + id + "]";
    }

}
