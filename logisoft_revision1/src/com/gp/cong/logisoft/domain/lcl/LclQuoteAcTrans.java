/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.logisoft.domain.User;
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
 * @author Administrator
 */
@Entity
@Table(name = "lcl_quote_ac_trans")
@NamedQueries({
    @NamedQuery(name = "LclQuoteAcTrans.findAll", query = "SELECT l FROM LclQuoteAcTrans l"),
    @NamedQuery(name = "LclQuoteAcTrans.findById", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.id = :id"),
    @NamedQuery(name = "LclQuoteAcTrans.findByTransType", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.transType = :transType"),
    @NamedQuery(name = "LclQuoteAcTrans.findByTransDatetime", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.transDatetime = :transDatetime"),
    @NamedQuery(name = "LclQuoteAcTrans.findByEntryType", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.entryType = :entryType"),
    @NamedQuery(name = "LclQuoteAcTrans.findByReferenceNo", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.referenceNo = :referenceNo"),
    @NamedQuery(name = "LclQuoteAcTrans.findByAmount", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.amount = :amount"),
    @NamedQuery(name = "LclQuoteAcTrans.findByEnteredDatetime", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclQuoteAcTrans.findByModifiedDatetime", query = "SELECT l FROM LclQuoteAcTrans l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclQuoteAcTrans implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "trans_type")
    private String transType;
    @Basic(optional = false)
    @Column(name = "trans_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDatetime;
    @Basic(optional = false)
    @Column(name = "entry_type")
    private String entryType;
    @Basic(optional = false)
    @Column(name = "reference_no")
    private String referenceNo;
    @Basic(optional = false)
    @Column(name = "amount")
    private BigDecimal amount;
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
    @JoinColumn(name = "quote_ac_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclQuoteAc lclQuoteAc;

    public LclQuoteAcTrans() {
    }

    public LclQuoteAcTrans(Long id) {
        this.id = id;
    }

    public LclQuoteAcTrans(Long id, String transType, Date transDatetime, String entryType, String referenceNo, BigDecimal amount, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.transType = transType;
        this.transDatetime = transDatetime;
        this.entryType = entryType;
        this.referenceNo = referenceNo;
        this.amount = amount;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Date getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(Date transDatetime) {
        this.transDatetime = transDatetime;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public LclQuoteAc getLclQuoteAc() {
        return lclQuoteAc;
    }

    public void setLclQuoteAc(LclQuoteAc lclQuoteAc) {
        this.lclQuoteAc = lclQuoteAc;
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
        if (!(object instanceof LclQuoteAcTrans)) {
            return false;
        }
        LclQuoteAcTrans other = (LclQuoteAcTrans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclQuoteAcTrans[id=" + id + "]";
    }

}
