/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_booking_dispo")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclBookingDispo.findAll", query = "SELECT l FROM LclBookingDispo l"),
    @NamedQuery(name = "LclBookingDispo.findById", query = "SELECT l FROM LclBookingDispo l WHERE l.id = :id"),
    @NamedQuery(name = "LclBookingDispo.findByDispositionDatetime", query = "SELECT l FROM LclBookingDispo l WHERE l.dispositionDatetime = :dispositionDatetime"),
    @NamedQuery(name = "LclBookingDispo.findByEnteredDatetime", query = "SELECT l FROM LclBookingDispo l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclBookingDispo.findByModifiedDatetime", query = "SELECT l FROM LclBookingDispo l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclBookingDispo extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "disposition_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dispositionDatetime;
    @Lob
    @Column(name = "remarks")
    private String remarks;
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
    private User enteredBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse warehouse;
    @JoinColumn(name = "un_location_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation unLocation;
    @JoinColumn(name = "ss_detail_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsDetail lclSsDetail;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private LclUnit lclUnit;
    @JoinColumn(name = "disposition_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Disposition disposition;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;

    public LclBookingDispo() {
    }

    public LclBookingDispo(Long id) {
        this.id = id;
    }

    public LclBookingDispo(Long id, Date dispositionDatetime, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.dispositionDatetime = dispositionDatetime;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDispositionDatetime() {
        return dispositionDatetime;
    }

    public void setDispositionDatetime(Date dispositionDatetime) {
        this.dispositionDatetime = dispositionDatetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LclSsDetail getLclSsDetail() {
        return lclSsDetail;
    }

    public void setLclSsDetail(LclSsDetail lclSsDetail) {
        this.lclSsDetail = lclSsDetail;
    }

    public LclUnit getLclUnit() {
        return lclUnit;
    }

    public void setLclUnit(LclUnit lclUnit) {
        this.lclUnit = lclUnit;
    }

    public Disposition getDisposition() {
        return disposition;
    }

    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public UnLocation getUnLocation() {
        return unLocation;
    }

    public void setUnLocation(UnLocation unLocation) {
        this.unLocation = unLocation;
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
        if (!(object instanceof LclBookingDispo)) {
            return false;
        }
        LclBookingDispo other = (LclBookingDispo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingDispo[id=" + id + "]";
    }
}
