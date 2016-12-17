/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_booking_pad")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclBookingPad extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "commodity_desc")
    private String commodityDesc;
    @Column(name = "pickup_hours")
    private String pickupHours;
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
    @Column(name = "pickup_to")
    private String pickUpTo;
    @Column(name = "pickup_city")
    private String pickUpCity;
    @Lob
    @Column(name = "delivery_instructions")
    private String deliveryInstructions;
    @Column(name = "delivery_hours")
    private String deliveryHours;
    @Column(name = "delivery_ready_date")
    @Temporal(TemporalType.DATE)
    private Date deliveryReadyDate;
    @Column(name = "delivery_reference_no")
    private String deliveryReferenceNo;
    @Column(name = "delivered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredDatetime;
    @Lob
    @Column(name = "terms_of_service")
    private String termsOfService;
    @Column(name = "scac")
    private String scac;
    @Column(name = "last_free_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFreeDate;
    @Column(name = "delivery_commercial")
    private Boolean deliveryCommercial;
    @Column(name = "lift_gate")
    private Boolean liftGate;
    @Column(name = "delivery_notes")
    private String deliveryNotes;
    @Column(name = "delivery_need_proof")
    private Boolean needPOD;
    @Column(name = "pickup_est_date")
    @Temporal(TemporalType.DATE)
    private Date estPickupDate;
    @Column(name = "delivery_est_date")
    @Temporal(TemporalType.DATE)
    private Date estimatedDeliveryDate;
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
    @JoinColumn(name = "issuing_terminal", referencedColumnName = "trmnum")
    @ManyToOne
    private RefTerminal issuingTerminal;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "booking_ac_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LclBookingAc lclBookingAc;

    public LclBookingPad() {
    }

    public LclBookingPad(Long id) {
        this.id = id;
    }

    public LclBookingPad(Long id, Date enteredDatetime, Date modifiedDatetime) {
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
        return deliveryContact;
    }

    public void setDeliveryContact(LclContact deliveryContact) {
        this.deliveryContact = deliveryContact;
    }

    public LclContact getPickupContact() {
        return pickupContact;
    }

    public void setPickupContact(LclContact pickupContact) {
        this.pickupContact = pickupContact;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public String getDeliveryHours() {
        return deliveryHours;
    }

    public void setDeliveryHours(String deliveryHours) {
        this.deliveryHours = deliveryHours;
    }

    public Date getDeliveryReadyDate() {
        return deliveryReadyDate;
    }

    public void setDeliveryReadyDate(Date deliveryReadyDate) {
        this.deliveryReadyDate = deliveryReadyDate;
    }

    public String getDeliveryReferenceNo() {
        return deliveryReferenceNo;
    }

    public void setDeliveryReferenceNo(String deliveryReferenceNo) {
        this.deliveryReferenceNo = deliveryReferenceNo;
    }

    public LclBookingAc getLclBookingAc() {
        if (lclBookingAc == null) {
            lclBookingAc = new LclBookingAc();
        }
        return lclBookingAc;
    }

    public void setLclBookingAc(LclBookingAc lclBookingAc) {
        this.lclBookingAc = lclBookingAc;
    }

    public String getPickUpTo() {
        return pickUpTo;
    }

    public void setPickUpTo(String pickUpTo) {
        this.pickUpTo = pickUpTo;
    }

    public Date getLastFreeDate() {
        return lastFreeDate;
    }

    public void setLastFreeDate(Date lastFreeDate) {
        this.lastFreeDate = lastFreeDate;
    }

    public String getPickUpCity() {
        return pickUpCity;
    }

    public void setPickUpCity(String pickUpCity) {
        this.pickUpCity = pickUpCity;
    }

    public Boolean getDeliveryCommercial() {
        return deliveryCommercial;
    }

    public void setDeliveryCommercial(Boolean deliveryCommercial) {
        this.deliveryCommercial = deliveryCommercial;
    }

    public Boolean getLiftGate() {
        return liftGate;
    }

    public void setLiftGate(Boolean liftGate) {
        this.liftGate = liftGate;
    }

    public Boolean getNeedPOD() {
        return needPOD;
    }

    public void setNeedPOD(Boolean needPOD) {
        this.needPOD = needPOD;
    }

    public Date getEstPickupDate() {
        return estPickupDate;
    }

    public void setEstPickupDate(Date estPickupDate) {
        this.estPickupDate = estPickupDate;
    }

    public Date getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(String deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
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
        if (!(object instanceof LclBookingPad)) {
            return false;
        }
        LclBookingPad other = (LclBookingPad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingPad[id=" + id + "]";
    }
}
