/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import java.io.Serializable;
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

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_rate_exp_std")
public class LclRateExpStd extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "trans_mode")
    private String transMode;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "entered_by_user_id")
    private int enteredByUserId;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Basic(optional = false)
    @Column(name = "modified_by_user_id")
    private int modifiedByUserId;
    @JoinColumn(name = "commodity_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CommodityType commodityType;
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation destination;
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation origin;

    public LclRateExpStd() {
    }

    public LclRateExpStd(Long id) {
        this.id = id;
    }

    public LclRateExpStd(Long id, String transMode, Date enteredDatetime, int enteredByUserId, Date modifiedDatetime, int modifiedByUserId) {
        this.id = id;
        this.transMode = transMode;
        this.enteredDatetime = enteredDatetime;
        this.enteredByUserId = enteredByUserId;
        this.modifiedDatetime = modifiedDatetime;
        this.modifiedByUserId = modifiedByUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    public int getEnteredByUserId() {
        return enteredByUserId;
    }

    public void setEnteredByUserId(int enteredByUserId) {
        this.enteredByUserId = enteredByUserId;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public int getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(int modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public UnLocation getDestination() {
        return destination;
    }

    public void setDestination(UnLocation destination) {
        this.destination = destination;
    }

    public UnLocation getOrigin() {
        return origin;
    }

    public void setOrigin(UnLocation origin) {
        this.origin = origin;
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
        if (!(object instanceof LclRateExpStd)) {
            return false;
        }
        LclRateExpStd other = (LclRateExpStd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclRateExpStd[id=" + id + "]";
    }
}
