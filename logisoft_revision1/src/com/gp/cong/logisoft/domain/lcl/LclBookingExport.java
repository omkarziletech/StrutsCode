/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author aravindhan.v
 */
@Entity
@Table(name = "lcl_booking_export")
@NamedQueries({
    @NamedQuery(name = "LclBookingExport.findAll", query = "SELECT l FROM LclBookingExport l"),
    @NamedQuery(name = "LclBookingExport.findByFileNumberId", query = "SELECT l FROM LclBookingExport l WHERE l.fileNumberId = :fileNumberId"),
    @NamedQuery(name = "LclBookingExport.findByDeliverPickup", query = "SELECT l FROM LclBookingExport l WHERE l.deliverPickup = :deliverPickup"),
    @NamedQuery(name = "LclBookingExport.findByDeliverPickupDatetime", query = "SELECT l FROM LclBookingExport l WHERE l.deliverPickupDatetime = :deliverPickupDatetime"),
    @NamedQuery(name = "LclBookingExport.findByAes", query = "SELECT l FROM LclBookingExport l WHERE l.aes = :aes"),
    @NamedQuery(name = "LclBookingExport.findByUps", query = "SELECT l FROM LclBookingExport l WHERE l.ups = :ups"),
    @NamedQuery(name = "LclBookingExport.findByRoutedTransaction", query = "SELECT l FROM LclBookingExport l WHERE l.routedTransaction = :routedTransaction"),
    @NamedQuery(name = "LclBookingExport.findByPrereleaseDatetime", query = "SELECT l FROM LclBookingExport l WHERE l.prereleaseDatetime = :prereleaseDatetime"),
    @NamedQuery(name = "LclBookingExport.findByReleasedDatetime", query = "SELECT l FROM LclBookingExport l WHERE l.releasedDatetime = :releasedDatetime"),
    @NamedQuery(name = "LclBookingExport.findByEnteredDatetime", query = "SELECT l FROM LclBookingExport l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclBookingExport.findByModifiedDatetime", query = "SELECT l FROM LclBookingExport l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclBookingExport extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_number_id")
    private Long fileNumberId;
    @Basic(optional = false)
    @Column(name = "deliver_pickup")
    private String deliverPickup;
    @Column(name = "deliver_pickup_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliverPickupDatetime;
    @Basic(optional = false)
    @Column(name = "aes")
    private boolean aes;
    @Basic(optional = false)
    @Column(name = "no_bl_required")
    private boolean noBlRequired;
    @Basic(optional = false)
    @Column(name = "ups")
    private boolean ups;
    @Basic(optional = false)
    @Column(name = "calc_heavy")
    private boolean calcHeavy = true;
    @Basic(optional = false)
    @Column(name = "cfcl")
    private boolean cfcl;
    @Basic(optional = false)
    @Column(name = "routed_transaction")
    private boolean routedTransaction;
    @Column(name = "prerelease_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date prereleaseDatetime;
    @Column(name = "released_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedDatetime;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Column(name = "storage_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date storageDatetime;
    @Basic(optional = false)
    @Column(name = "include_dest_fees")
    private boolean includeDestfees;
    @JoinColumn(name = "released_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User releaseUser;
    @JoinColumn(name = "prerelease_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User prereleaseUser;
    @JoinColumn(name = "rt_agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner rtAgentAcct;
    @JoinColumn(name = "origin_whse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse orginWarehouse;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "cfcl_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner cfclAcctNo;

    public LclBookingExport() {
    }

    public LclBookingExport(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclBookingExport(Long fileNumberId, String deliverPickup, Date deliverPickupDatetime, boolean aes, boolean ups, boolean routedTransaction, Date enteredDatetime, Date modifiedDatetime) {
        this.fileNumberId = fileNumberId;
        this.deliverPickup = deliverPickup;
        this.deliverPickupDatetime = deliverPickupDatetime;
        this.aes = aes;        
        this.ups = ups;
        this.routedTransaction = routedTransaction;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getDeliverPickup() {
        return deliverPickup;
    }

    public void setDeliverPickup(String deliverPickup) {
        this.deliverPickup = deliverPickup;
    }

    public Date getDeliverPickupDatetime() {
        return deliverPickupDatetime;
    }

    public void setDeliverPickupDatetime(Date deliverPickupDatetime) {
        this.deliverPickupDatetime = deliverPickupDatetime;
    }

    public boolean getAes() {
        return aes;
    }

    public void setAes(boolean aes) {
        this.aes = aes;
    }

    public boolean isNoBlRequired() {
        return noBlRequired;
    }

    public void setNoBlRequired(boolean noBlRequired) {
        this.noBlRequired = noBlRequired;
    }    

    public boolean getUps() {
        return ups;
    }

    public void setUps(boolean ups) {
        this.ups = ups;
    }

    public boolean getRoutedTransaction() {
        return routedTransaction;
    }

    public void setRoutedTransaction(boolean routedTransaction) {
        this.routedTransaction = routedTransaction;
    }

    public Date getPrereleaseDatetime() {
        return prereleaseDatetime;
    }

    public void setPrereleaseDatetime(Date prereleaseDatetime) {
        this.prereleaseDatetime = prereleaseDatetime;
    }

    public Date getReleasedDatetime() {
        return releasedDatetime;
    }

    public void setReleasedDatetime(Date releasedDatetime) {
        this.releasedDatetime = releasedDatetime;
    }

    @Override
    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    @Override
    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    @Override
    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    @Override
    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    @Override
    public User getEnteredBy() {
        return enteredBy;
    }

    @Override
    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    @Override
    public User getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Warehouse getOrginWarehouse() {
        return orginWarehouse;
    }

    public void setOrginWarehouse(Warehouse orginWarehouse) {
        this.orginWarehouse = orginWarehouse;
    }

    public User getPrereleaseUser() {
        return prereleaseUser;
    }

    public void setPrereleaseUser(User prereleaseUser) {
        this.prereleaseUser = prereleaseUser;
    }

    public User getReleaseUser() {
        return releaseUser;
    }

    public void setReleaseUser(User releaseUser) {
        this.releaseUser = releaseUser;
    }

    public TradingPartner getRtAgentAcct() {
        return rtAgentAcct;
    }

    public void setRtAgentAcct(TradingPartner rtAgentAcct) {
        this.rtAgentAcct = rtAgentAcct;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public TradingPartner getCfclAcctNo() {
        return cfclAcctNo;
    }

    public void setCfclAcctNo(TradingPartner cfclAcctNo) {
        this.cfclAcctNo = cfclAcctNo;
    }

    public boolean isCalcHeavy() {
        return calcHeavy;
    }

    public void setCalcHeavy(boolean calcHeavy) {
        this.calcHeavy = calcHeavy;
    }

    public boolean isCfcl() {
        return cfcl;
    }

    public void setCfcl(boolean cfcl) {
        this.cfcl = cfcl;
    }

    public Date getStorageDatetime() {
        return storageDatetime;
    }

    public void setStorageDatetime(Date storageDatetime) {
        this.storageDatetime = storageDatetime;
    }

    public boolean isIncludeDestfees() {
        return includeDestfees;
    }

    public void setIncludeDestfees(boolean includeDestfees) {
        this.includeDestfees = includeDestfees;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileNumberId != null ? fileNumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclBookingExport)) {
            return false;
        }
        LclBookingExport other = (LclBookingExport) object;
        if ((this.fileNumberId == null && other.fileNumberId != null) || (this.fileNumberId != null && !this.fileNumberId.equals(other.fileNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingExport[fileNumberId=" + fileNumberId + "]";
    }
}
