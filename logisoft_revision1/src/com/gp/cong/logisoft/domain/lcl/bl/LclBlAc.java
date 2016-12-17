/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cvst.logisoft.domain.GlMapping;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_ac")
public class LclBlAc extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "trans_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDatetime;
    @Column(name = "sp_reference_no")
    private String spReferenceNo;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Basic(optional = false)
    @Column(name = "manual_entry")
    private boolean manualEntry;
    @Column(name = "ap_amount")
    private BigDecimal apAmount;
    @Column(name = "ap_bill_to_party")
    private String apBillToParty;
    @Basic(optional = false)
    @Column(name = "print_on_bl")
    private boolean printOnBl;
    @Basic(optional = false)
    @Column(name = "bundle_into_of")
    private boolean bundleIntoOf;
    @Column(name = "rate_uom")
    private String rateUom;
    @Column(name = "rate_per_weight_unit")
    private BigDecimal ratePerWeightUnit;
    @Column(name = "rate_per_weight_unit_div")
    private BigDecimal ratePerWeightUnitDiv;
    @Column(name = "rate_per_volume_unit")
    private BigDecimal ratePerVolumeUnit;
    @Column(name = "rate_per_volume_unit_div")
    private BigDecimal ratePerVolumeUnitDiv;
    @Column(name = "rate_flat_minimum")
    private BigDecimal rateFlatMinimum;
    @Column(name = "rate_per_unit")
    private BigDecimal ratePerUnit;
    @Column(name = "rate_per_unit_div")
    private BigDecimal ratePerUnitDiv;
    @Basic(optional = false)
    @Column(name = "rate_per_unit_uom")
    private String ratePerUnitUom;
    @Basic(optional = false)
    @Column(name = "adjustment_amount")
    private BigDecimal adjustmentAmount;
    @Basic(optional = false)
    @Column(name = "ar_amount")
    private BigDecimal arAmount;
    @Column(name = "ar_bill_to_party")
    private String arBillToParty;
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "adjustment_comments")
    private String adjustmentComment;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclBlAc")
    private List<LclBlAcTrans> lclBlAcTransList;
    @JoinColumn(name = "sp_contact_id", referencedColumnName = "id")
    @ManyToOne
    private LclContact spContact;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "sp_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner supAcct;
    @JoinColumn(name = "ar_gl_mapping_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GlMapping arglMapping;
    @JoinColumn(name = "ap_gl_mapping_id", referencedColumnName = "id")
    @ManyToOne
    private GlMapping apglMapping;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "booking_piece_id", referencedColumnName = "id")
    @ManyToOne
    private LclBlPiece lclBlPiece;
    @Transient
    private String label1;
    @Transient
    private String label2;
    @Transient
    private BigDecimal rolledupCharges;
    @Transient
    private BigDecimal totalWeight;
    @Transient
    private BigDecimal totalMeasure;
    @Transient
    private String consolidateCharges;

    public LclBlAc() {
    }

    public LclBlAc(Long id) {
        this.id = id;
    }

    public LclBlAc(Long id, Date transDatetime, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.transDatetime = transDatetime;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(Date transDatetime) {
        this.transDatetime = transDatetime;
    }

    public String getSpReferenceNo() {
        return spReferenceNo;
    }

    public void setSpReferenceNo(String spReferenceNo) {
        this.spReferenceNo = spReferenceNo;
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

    public List<LclBlAcTrans> getLclBlAcTransList() {
        return lclBlAcTransList;
    }

    public void setLclBlAcTransList(List<LclBlAcTrans> lclBlAcTransList) {
        this.lclBlAcTransList = lclBlAcTransList;
    }

    public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public TradingPartner getSupAcct() {
        return supAcct;
    }

    public void setSupAcct(TradingPartner supAcct) {
        this.supAcct = supAcct;
    }

    public LclContact getSpContact() {
        return spContact;
    }

    public void setSpContact(LclContact spContact) {
        this.spContact = spContact;
    }

    public GlMapping getApglMapping() {
        return apglMapping;
    }

    public void setApglMapping(GlMapping apglMapping) {
        this.apglMapping = apglMapping;
    }

    public GlMapping getArglMapping() {
        return arglMapping;
    }

    public void setArglMapping(GlMapping arglMapping) {
        this.arglMapping = arglMapping;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public boolean isManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public BigDecimal getApAmount() {
        return apAmount;
    }

    public void setApAmount(BigDecimal apAmount) {
        this.apAmount = apAmount;
    }

    public String getApBillToParty() {
        return apBillToParty;
    }

    public void setApBillToParty(String apBillToParty) {
        this.apBillToParty = apBillToParty;
    }

    public String getRateUom() {
        return rateUom;
    }

    public void setRateUom(String rateUom) {
        this.rateUom = rateUom;
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

    public BigDecimal getRateFlatMinimum() {
        return rateFlatMinimum;
    }

    public void setRateFlatMinimum(BigDecimal rateFlatMinimum) {
        this.rateFlatMinimum = rateFlatMinimum;
    }

    public BigDecimal getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(BigDecimal ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public BigDecimal getRatePerUnitDiv() {
        return ratePerUnitDiv;
    }

    public void setRatePerUnitDiv(BigDecimal ratePerUnitDiv) {
        this.ratePerUnitDiv = ratePerUnitDiv;
    }

    public String getRatePerUnitUom() {
        return ratePerUnitUom;
    }

    public void setRatePerUnitUom(String ratePerUnitUom) {
        this.ratePerUnitUom = ratePerUnitUom;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public String getArBillToParty() {
        return arBillToParty;
    }

    public void setArBillToParty(String arBillToParty) {
        this.arBillToParty = arBillToParty;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public LclBlPiece getLclBlPiece() {
        return lclBlPiece;
    }

    public void setLclBlPiece(LclBlPiece lclBlPiece) {
        this.lclBlPiece = lclBlPiece;
    }

    public BigDecimal getRolledupCharges() {
        return rolledupCharges;
    }

    public void setRolledupCharges(BigDecimal rolledupCharges) {
        this.rolledupCharges = rolledupCharges;
    }

    public BigDecimal getTotalMeasure() {
        return totalMeasure;
    }

    public void setTotalMeasure(BigDecimal totalMeasure) {
        this.totalMeasure = totalMeasure;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public boolean getBundleIntoOf() {
        return bundleIntoOf;
    }

    public void setBundleIntoOf(boolean bundleIntoOf) {
        this.bundleIntoOf = bundleIntoOf;
    }

    public boolean getPrintOnBl() {
        return printOnBl;
    }

    public void setPrintOnBl(boolean printOnBl) {
        this.printOnBl = printOnBl;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getConsolidateCharges() {
        return consolidateCharges;
    }

    public void setConsolidateCharges(String consolidateCharges) {
        this.consolidateCharges = consolidateCharges;
    }

    public String getAdjustmentComment() {
        return adjustmentComment;
    }

    public void setAdjustmentComment(String adjustmentComment) {
        this.adjustmentComment = adjustmentComment;
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
        if (!(object instanceof LclBlAc)) {
            return false;
        }
        LclBlAc other = (LclBlAc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlAc[id=" + id + "]";
    }
}
