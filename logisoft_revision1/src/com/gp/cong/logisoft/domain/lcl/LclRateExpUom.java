/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "lcl_rate_exp_uom")
public class LclRateExpUom extends Domain {

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
    @Column(name = "charge_code_id")
    private int chargeCodeId;
    @Basic(optional = false)
    @Column(name = "start_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Basic(optional = false)
    @Column(name = "end_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetime;
    @Basic(optional = false)
    @Column(name = "per_cbft_amount")
    private BigDecimal perCbftAmount;
    @Basic(optional = false)
    @Column(name = "per_100lbs_amount")
    private BigDecimal per100lbsAmount;
    @Basic(optional = false)
    @Column(name = "per_iton_amount")
    private BigDecimal perItonAmount;
    @Basic(optional = false)
    @Column(name = "per_cbm_amount")
    private BigDecimal perCbmAmount;
    @Basic(optional = false)
    @Column(name = "per_100kg_amount")
    private BigDecimal per100kgAmount;
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
    @JoinColumn(name = "dest_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation destination;
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation origin;

    public LclRateExpUom() {
    }

    public LclRateExpUom(Long id) {
        this.id = id;
    }

    public LclRateExpUom(Long id, String transMode, int chargeCodeId, Date startDatetime, Date endDatetime, BigDecimal perCbftAmount, BigDecimal per100lbsAmount, BigDecimal perItonAmount, BigDecimal perCbmAmount, BigDecimal per100kgAmount, Date enteredDatetime, int enteredByUserId, Date modifiedDatetime, int modifiedByUserId) {
        this.id = id;
        this.transMode = transMode;
        this.chargeCodeId = chargeCodeId;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.perCbftAmount = perCbftAmount;
        this.per100lbsAmount = per100lbsAmount;
        this.perItonAmount = perItonAmount;
        this.perCbmAmount = perCbmAmount;
        this.per100kgAmount = per100kgAmount;
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

    public int getChargeCodeId() {
        return chargeCodeId;
    }

    public void setChargeCodeId(int chargeCodeId) {
        this.chargeCodeId = chargeCodeId;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public BigDecimal getPerCbftAmount() {
        return perCbftAmount;
    }

    public void setPerCbftAmount(BigDecimal perCbftAmount) {
        this.perCbftAmount = perCbftAmount;
    }

    public BigDecimal getPer100lbsAmount() {
        return per100lbsAmount;
    }

    public void setPer100lbsAmount(BigDecimal per100lbsAmount) {
        this.per100lbsAmount = per100lbsAmount;
    }

    public BigDecimal getPerItonAmount() {
        return perItonAmount;
    }

    public void setPerItonAmount(BigDecimal perItonAmount) {
        this.perItonAmount = perItonAmount;
    }

    public BigDecimal getPerCbmAmount() {
        return perCbmAmount;
    }

    public void setPerCbmAmount(BigDecimal perCbmAmount) {
        this.perCbmAmount = perCbmAmount;
    }

    public BigDecimal getPer100kgAmount() {
        return per100kgAmount;
    }

    public void setPer100kgAmount(BigDecimal per100kgAmount) {
        this.per100kgAmount = per100kgAmount;
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
        if (!(object instanceof LclRateExpUom)) {
            return false;
        }
        LclRateExpUom other = (LclRateExpUom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclRateExpUom[id=" + id + "]";
    }
}
