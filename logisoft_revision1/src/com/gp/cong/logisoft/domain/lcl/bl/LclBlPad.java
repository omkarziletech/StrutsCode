/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_pad")
public class LclBlPad extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "commodity_desc")
    private String commodityDesc;
    @Column(name = "sp_reference_no")
    private String spReferenceNo;
    @Column(name = "sp_accrual_amount")
    private BigDecimal spAccrualAmount;
    @Column(name = "charge_amount")
    private BigDecimal chargeAmount;
    @Column(name = "pickup_hours")
    private String pickupHours;
    @Column(name = "scac")
    private String scac;
    @Column(name = "pickup_ready_date")
    @Temporal(TemporalType.DATE)
    private Date pickupReadyDate;
    @Column(name = "pickup_cutoff_date")
    @Temporal(TemporalType.DATE)
    private Date pickupCutoffDate;
    @Column(name = "pickup_reference_no")
    private String pickupReferenceNo;
    @Lob
    @Column(name = "pickup_instructions")
    private String pickupInstructions;
    @Lob
    @Column(name = "pickup_ready_note")
    private String pickupReadyNote;
    @Column(name = "pickedup_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickedupDatetime;
    @Lob
    @Column(name = "delivery_instructions")
    private String deliveryInstructions;
    @Column(name = "delivered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredDatetime;
    @Lob
    @Column(name = "terms_of_service")
    private String termsOfService;
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
    @JoinColumn(name = "delivery_contact_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private LclContact deliveryContact;
    @JoinColumn(name = "pickup_contact_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private LclContact pickupContact;
    @JoinColumn(name = "sp_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner spAcct;
    @JoinColumn(name = "issuing_terminal", referencedColumnName = "trmnum")
    @ManyToOne
    private RefTerminal issuingTerminal;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "pickup_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner pickupAcct;

    public LclBlPad() {
    }

    public LclBlPad(Long id) {
        this.id = id;
    }

    public LclBlPad(Long id, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getSpReferenceNo() {
        return spReferenceNo;
    }

    public void setSpReferenceNo(String spReferenceNo) {
        this.spReferenceNo = spReferenceNo;
    }

    public BigDecimal getSpAccrualAmount() {
        return spAccrualAmount;
    }

    public void setSpAccrualAmount(BigDecimal spAccrualAmount) {
        this.spAccrualAmount = spAccrualAmount;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public RefTerminal getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(RefTerminal issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getPickupHours() {
        return pickupHours;
    }

    public void setPickupHours(String pickupHours) {
        this.pickupHours = pickupHours;
    }

    public Date getPickupReadyDate() {
        return pickupReadyDate;
    }

    public void setPickupReadyDate(Date pickupReadyDate) {
        this.pickupReadyDate = pickupReadyDate;
    }

    public Date getPickupCutoffDate() {
        return pickupCutoffDate;
    }

    public void setPickupCutoffDate(Date pickupCutoffDate) {
        this.pickupCutoffDate = pickupCutoffDate;
    }

    public String getPickupReferenceNo() {
        return pickupReferenceNo;
    }

    public void setPickupReferenceNo(String pickupReferenceNo) {
        this.pickupReferenceNo = pickupReferenceNo;
    }

    public String getPickupInstructions() {
        return pickupInstructions;
    }

    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }

    public String getPickupReadyNote() {
        return pickupReadyNote;
    }

    public void setPickupReadyNote(String pickupReadyNote) {
        this.pickupReadyNote = pickupReadyNote;
    }

    public Date getPickedupDatetime() {
        return pickedupDatetime;
    }

    public void setPickedupDatetime(Date pickedupDatetime) {
        this.pickedupDatetime = pickedupDatetime;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public Date getDeliveredDatetime() {
        return deliveredDatetime;
    }

    public void setDeliveredDatetime(Date deliveredDatetime) {
        this.deliveredDatetime = deliveredDatetime;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
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

    public LclContact getDeliveryContact() {
        if (deliveryContact == null) {
            deliveryContact = new LclContact();
        }
        return deliveryContact;
    }

    public void setDeliveryContact(LclContact deliveryContact) {
        this.deliveryContact = deliveryContact;
    }

    public LclContact getPickupContact() {
        if (pickupContact == null) {
            pickupContact = new LclContact();
        }
        return pickupContact;
    }

    public void setPickupContact(LclContact pickupContact) {
        this.pickupContact = pickupContact;
    }

    public TradingPartner getSpAcct() {
        return spAcct;
    }

    public void setSpAcct(TradingPartner spAcct) {
        this.spAcct = spAcct;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public TradingPartner getPickupAcct() {
        return pickupAcct;
    }

    public void setPickupAcct(TradingPartner pickupAcct) {
        this.pickupAcct = pickupAcct;
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
        if (!(object instanceof LclBlPad)) {
            return false;
        }
        LclBlPad other = (LclBlPad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlPad[id=" + id + "]";
    }
}
