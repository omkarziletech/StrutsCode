/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
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
@Table(name = "lcl_unit_whse")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclUnitWhse.findAll", query = "SELECT l FROM LclUnitWhse l"),
    @NamedQuery(name = "LclUnitWhse.findById", query = "SELECT l FROM LclUnitWhse l WHERE l.id = :id"),
    @NamedQuery(name = "LclUnitWhse.findByLocation", query = "SELECT l FROM LclUnitWhse l WHERE l.location = :location"),
    @NamedQuery(name = "LclUnitWhse.findByArrivedDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.arrivedDatetime = :arrivedDatetime"),
    @NamedQuery(name = "LclUnitWhse.findByDepartedDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.departedDatetime = :departedDatetime"),
    @NamedQuery(name = "LclUnitWhse.findByStuffedDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.stuffedDatetime = :stuffedDatetime"),
    @NamedQuery(name = "LclUnitWhse.findByDestuffedDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.destuffedDatetime = :destuffedDatetime"),
    @NamedQuery(name = "LclUnitWhse.findByOsdReceived", query = "SELECT l FROM LclUnitWhse l WHERE l.osdReceived = :osdReceived"),
    @NamedQuery(name = "LclUnitWhse.findByOsdDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.osdDatetime = :osdDatetime"),
    @NamedQuery(name = "LclUnitWhse.findBySealNoIn", query = "SELECT l FROM LclUnitWhse l WHERE l.sealNoIn = :sealNoIn"),
    @NamedQuery(name = "LclUnitWhse.findBySealNoOut", query = "SELECT l FROM LclUnitWhse l WHERE l.sealNoOut = :sealNoOut"),
    @NamedQuery(name = "LclUnitWhse.findByEnteredDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclUnitWhse.findByModifiedDatetime", query = "SELECT l FROM LclUnitWhse l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclUnitWhse extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "location")
    private String location;
    @Column(name = "arrived_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivedDatetime;
    @Column(name = "departed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departedDatetime;
    @Column(name = "stuffed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stuffedDatetime;
    @Column(name = "destuffed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date destuffedDatetime;
    @Column(name = "osd_received")
    private Boolean osdReceived;
    @Column(name = "osd_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date osdDatetime;
    @Column(name = "seal_no_in")
    private String sealNoIn;
    @Column(name = "seal_no_out")
    private String sealNoOut;
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
    @JoinColumn(name = "osd_by_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User osdUser;
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse warehouse;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclUnit lclUnit;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsHeader lclSsHeader;
    @JoinColumn(name = "stuffed_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User stuffedByUser;
    @JoinColumn(name = "destuffed_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User destuffedByUser;
    @Column(name = "chassis_no_in")
    private String chassisNoIn;
    
    public LclUnitWhse() {
    }

    public LclUnitWhse(Long id) {
        this.id = id;
    }

    public LclUnitWhse(Long id, Date enteredDatetime, Date modifiedDatetime) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getArrivedDatetime() {
        return arrivedDatetime;
    }

    public void setArrivedDatetime(Date arrivedDatetime) {
        this.arrivedDatetime = arrivedDatetime;
    }

    public Date getDepartedDatetime() {
        return departedDatetime;
    }

    public void setDepartedDatetime(Date departedDatetime) {
        this.departedDatetime = departedDatetime;
    }

    public Date getStuffedDatetime() {
        return stuffedDatetime;
    }

    public void setStuffedDatetime(Date stuffedDatetime) {
        this.stuffedDatetime = stuffedDatetime;
    }

    public Date getDestuffedDatetime() {
        return destuffedDatetime;
    }

    public void setDestuffedDatetime(Date destuffedDatetime) {
        this.destuffedDatetime = destuffedDatetime;
    }

    public Boolean getOsdReceived() {
        return osdReceived;
    }

    public void setOsdReceived(Boolean osdReceived) {
        this.osdReceived = osdReceived;
    }

    public Date getOsdDatetime() {
        return osdDatetime;
    }

    public void setOsdDatetime(Date osdDatetime) {
        this.osdDatetime = osdDatetime;
    }

    public String getSealNoIn() {
        return sealNoIn;
    }

    public void setSealNoIn(String sealNoIn) {
        this.sealNoIn = sealNoIn;
    }

    public String getSealNoOut() {
        return sealNoOut;
    }

    public void setSealNoOut(String sealNoOut) {
        this.sealNoOut = sealNoOut;
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

    public User getOsdUser() {
        return osdUser;
    }

    public void setOsdUser(User osdUser) {
        this.osdUser = osdUser;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LclUnit getLclUnit() {
        return lclUnit;
    }

    public void setLclUnit(LclUnit lclUnit) {
        this.lclUnit = lclUnit;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
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

    public User getStuffedByUser() {
        return stuffedByUser;
    }

    public void setStuffedByUser(User stuffedByUser) {
        this.stuffedByUser = stuffedByUser;
    }

    public User getDestuffedByUser() {
        return destuffedByUser;
    }

    public void setDestuffedByUser(User destuffedByUser) {
        this.destuffedByUser = destuffedByUser;
    }

    public String getChassisNoIn() {
        return chassisNoIn;
    }

    public void setChassisNoIn(String chassisNoIn) {
        this.chassisNoIn = chassisNoIn;
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
        if (!(object instanceof LclUnitWhse)) {
            return false;
        }
        LclUnitWhse other = (LclUnitWhse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitWhse[id=" + id + "]";
    }
}
