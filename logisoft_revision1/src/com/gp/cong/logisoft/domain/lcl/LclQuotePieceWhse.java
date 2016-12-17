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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "lcl_quote_piece_whse")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclQuotePieceWhse extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "arrived_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivedDatetime;
    @Column(name = "stowed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stowedDatetime;
    @Column(name = "location")
    private String location;
    @Column(name = "removed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date removedDatetime;
    @Column(name = "departed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departedDatetime;
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
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Warehouse warehouse;
    @JoinColumn(name = "quote_piece_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclQuotePiece lclQuotePiece;

    public LclQuotePieceWhse() {
    }

    public LclQuotePieceWhse(Long id) {
        this.id = id;
    }

    public LclQuotePieceWhse(Long id, Date enteredDatetime, Date modifiedDatetime) {
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

    public Date getArrivedDatetime() {
        return arrivedDatetime;
    }

    public void setArrivedDatetime(Date arrivedDatetime) {
        this.arrivedDatetime = arrivedDatetime;
    }

    public Date getStowedDatetime() {
        return stowedDatetime;
    }

    public void setStowedDatetime(Date stowedDatetime) {
        this.stowedDatetime = stowedDatetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRemovedDatetime() {
        return removedDatetime;
    }

    public void setRemovedDatetime(Date removedDatetime) {
        this.removedDatetime = removedDatetime;
    }

    public Date getDepartedDatetime() {
        return departedDatetime;
    }

    public void setDepartedDatetime(Date departedDatetime) {
        this.departedDatetime = departedDatetime;
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

    public LclQuotePiece getLclQuotePiece() {
        return lclQuotePiece;
    }

    public void setLclQuotePiece(LclQuotePiece lclQuotePiece) {
        this.lclQuotePiece = lclQuotePiece;
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
        if (!(object instanceof LclQuotePieceWhse)) {
            return false;
        }
        LclQuotePieceWhse other = (LclQuotePieceWhse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclQuotePieceWhse[id=" + id + "]";
    }
}
