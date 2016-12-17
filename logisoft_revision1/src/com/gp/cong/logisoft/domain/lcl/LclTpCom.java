/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
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
@Table(name = "lcl_tp_com")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclTpCom extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "start_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Basic(optional = false)
    @Column(name = "end_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetime;
    @Basic(optional = false)
    @Column(name = "trans_mode")
    private String transMode;
    @Basic(optional = false)
    @Column(name = "commission_type")
    private String commissionType;
    @Column(name = "flat_rate_amount")
    private BigDecimal flatRateAmount;
    @Column(name = "pct_bl_amount")
    private BigDecimal pctBlAmount;
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
    @JoinColumn(name = "agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne(optional = false)
    private TradingPartner agentAcct;
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation destination;
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation origin;

    public LclTpCom() {
    }

    public LclTpCom(Long id) {
        this.id = id;
    }

    public LclTpCom(Long id, Date startDatetime, Date endDatetime, String transMode, String commissionType, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.transMode = transMode;
        this.commissionType = commissionType;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public BigDecimal getFlatRateAmount() {
        return flatRateAmount;
    }

    public void setFlatRateAmount(BigDecimal flatRateAmount) {
        this.flatRateAmount = flatRateAmount;
    }

    public BigDecimal getPctBlAmount() {
        return pctBlAmount;
    }

    public void setPctBlAmount(BigDecimal pctBlAmount) {
        this.pctBlAmount = pctBlAmount;
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

    public TradingPartner getAgentAcct() {
        return agentAcct;
    }

    public void setAgentAcct(TradingPartner agentAcct) {
        this.agentAcct = agentAcct;
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
        if (!(object instanceof LclTpCom)) {
            return false;
        }
        LclTpCom other = (LclTpCom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclTpCom[id=" + id + "]";
    }
}
