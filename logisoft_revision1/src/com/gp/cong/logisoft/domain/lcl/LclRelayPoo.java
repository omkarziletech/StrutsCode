/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
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
@Table(name = "lcl_relay_poo")
@DynamicUpdate(true)
@DynamicInsert(true)
public class LclRelayPoo extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @Column(name = "co_dow")
    private Integer coDow;
    @Column(name = "co_tod")
    @Temporal(TemporalType.TIME)
    private Date coTod;
    @Basic(optional = false)
    @Column(name = "transit_time")
    private int transitTime;
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
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "poo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation poo;
    @JoinColumn(name = "relay_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclRelay lclRelay;

    public LclRelayPoo() {
    }

    public LclRelayPoo(Long id) {
        this.id = id;
    }

    public LclRelayPoo(Long id, boolean active, int transitTime, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.active = active;
        this.transitTime = transitTime;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getCoDow() {
        return coDow;
    }

    public void setCoDow(Integer coDow) {
        this.coDow = coDow;
    }

    public Date getCoTod() {
        return coTod;
    }

    public void setCoTod(Date coTod) {
        this.coTod = coTod;
    }

    public int getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(int transitTime) {
        this.transitTime = transitTime;
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

    public UnLocation getPoo() {
        return poo;
    }

    public void setPoo(UnLocation poo) {
        this.poo = poo;
    }

    public LclRelay getLclRelay() {
        return lclRelay;
    }

    public void setLclRelay(LclRelay lclRelay) {
        this.lclRelay = lclRelay;
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
        if (!(object instanceof LclRelayPoo)) {
            return false;
        }
        LclRelayPoo other = (LclRelayPoo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclRelayPoo[id=" + id + "]";
    }
}
