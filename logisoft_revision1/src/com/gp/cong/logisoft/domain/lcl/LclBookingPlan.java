/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "lcl_booking_plan")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclBookingPlan extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "seg_no")
    private Integer segNo;
    @Column(name = "from_etd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromEtd;
    @Column(name = "from_atd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromAtd;
    @Column(name = "to_eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toEta;
    @Column(name = "to_ata")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toAta;
    @Column(name = "trans_mode")
    private String transMode;
    @Basic(optional = false)
    @Column(name = "relay_override")
    private boolean relayOverride;
    @Column(name = "sp_reference")
    private String spReference;
    @Column(name = "sp_vessel")
    private String spVessel;
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
    @JoinColumn(name = "sp_contact_id", referencedColumnName = "id")
    @ManyToOne
    private LclContact spContact;
    @JoinColumn(name = "sp_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner spAcct;
    @JoinColumn(name = "to_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation toId;
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation fromId;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;

    public LclBookingPlan() {
    }

    public LclBookingPlan(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public LclBookingPlan(Long id) {
        this.id = id;
    }

    public LclBookingPlan(Long id, Integer segNo, boolean relayOverride, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.segNo = segNo;
        this.relayOverride = relayOverride;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSegNo() {
        return segNo;
    }

    public void setSegNo(Integer segNo) {
        this.segNo = segNo;
    }

    public Date getFromEtd() {
        return fromEtd;
    }

    public void setFromEtd(Date fromEtd) {
        this.fromEtd = fromEtd;
    }

    public Date getFromAtd() {
        return fromAtd;
    }

    public void setFromAtd(Date fromAtd) {
        this.fromAtd = fromAtd;
    }

    public Date getToEta() {
        return toEta;
    }

    public void setToEta(Date toEta) {
        this.toEta = toEta;
    }

    public Date getToAta() {
        return toAta;
    }

    public void setToAta(Date toAta) {
        this.toAta = toAta;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public boolean getRelayOverride() {
        return relayOverride;
    }

    public void setRelayOverride(boolean relayOverride) {
        this.relayOverride = relayOverride;
    }

    public String getSpReference() {
        return spReference;
    }

    public void setSpReference(String spReference) {
        this.spReference = spReference;
    }

    public String getSpVessel() {
        return spVessel;
    }

    public void setSpVessel(String spVessel) {
        this.spVessel = spVessel;
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

    public UnLocation getFromId() {
        return fromId;
    }

    public void setFromId(UnLocation fromId) {
        this.fromId = fromId;
    }

    public TradingPartner getSpAcct() {
        return spAcct;
    }

    public void setSpAcct(TradingPartner spAcct) {
        this.spAcct = spAcct;
    }

    public LclContact getSpContact() {
        if (spContact == null) {
            spContact = new LclContact();
        }
        return spContact;
    }

    public void setSpContact(LclContact spContact) {
        this.spContact = spContact;
    }

    public UnLocation getToId() {
        return toId;
    }

    public void setToId(UnLocation toId) {
        this.toId = toId;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
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
        if (!(object instanceof LclBookingPlan)) {
            return false;
        }
        LclBookingPlan other = (LclBookingPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingPlan[id=" + id + "]";
    }
}
