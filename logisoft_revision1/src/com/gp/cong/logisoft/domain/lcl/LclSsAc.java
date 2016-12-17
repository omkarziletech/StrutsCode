/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.GlMapping;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author i3
 */
@Entity
@Table(name = "lcl_ss_ac")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclSsAc extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "manual_entry")
    private boolean manualEntry;
    @Basic(optional = false)
    @Column(name = "trans_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDatetime;
    @Column(name = "ap_reference_no")
    private String apReferenceNo;
    @Basic(optional = false)
    @Column(name = "ap_amount")
    private BigDecimal apAmount;
    @Column(name = "ap_trans_type")
    private String apTransType;
    @Column(name = "ar_reference_no")
    private String arReferenceNo;
    @Basic(optional = false)
    @Column(name = "ar_amount")
    private BigDecimal arAmount;
    @Column(name = "ar_trans_type")
    private String arTransType;
    @Column(name = "posted_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedDatetime;
    @Basic(optional = false)
    @Column(name = "deleted")
    private boolean deleted;
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
    private User modifiedByUserId;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredByUserId;
    @JoinColumn(name = "ar_gl_mapping_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GlMapping arGlMappingId;
    @JoinColumn(name = "ar_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner arAcctNo;
    @JoinColumn(name = "ap_gl_mapping_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GlMapping apGlMappingId;
    @JoinColumn(name = "ap_acct_no", referencedColumnName = "acct_no")
    @ManyToOne(optional = false)
    private TradingPartner apAcctNo;
    @JoinColumn(name = "unit_ss_id", referencedColumnName = "id")
    @ManyToOne
    private LclUnitSs lclUnitSs;
    @JoinColumn(name = "ss_detail_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsDetail lclSsDetail;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @Transient
    private String totalCostByInvoiceNo;
    @Column(name = "distributed")
    private boolean distributed;

    public LclSsAc() {
    }

    public LclSsAc(Long id) {
        this.id = id;
    }

    public LclSsAc(Long id, boolean manualEntry, Date transDatetime,
            BigDecimal apAmount, BigDecimal arAmount, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.manualEntry = manualEntry;
        this.transDatetime = transDatetime;
        this.apAmount = apAmount;
        this.arAmount = arAmount;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public Date getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(Date transDatetime) {
        this.transDatetime = transDatetime;
    }

    public String getApReferenceNo() {
        return apReferenceNo;
    }

    public void setApReferenceNo(String apReferenceNo) {
        this.apReferenceNo = apReferenceNo;
    }

    public BigDecimal getApAmount() {
        return apAmount;
    }

    public void setApAmount(BigDecimal apAmount) {
        this.apAmount = apAmount;
    }

    public String getArReferenceNo() {
        return arReferenceNo;
    }

    public void setArReferenceNo(String arReferenceNo) {
        this.arReferenceNo = arReferenceNo;
    }

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public Date getPostedDatetime() {
        return postedDatetime;
    }

    public void setPostedDatetime(Date postedDatetime) {
        this.postedDatetime = postedDatetime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public TradingPartner getApAcctNo() {
        return apAcctNo;
    }

    public void setApAcctNo(TradingPartner apAcctNo) {
        this.apAcctNo = apAcctNo;
    }

    public GlMapping getApGlMappingId() {
        return apGlMappingId;
    }

    public void setApGlMappingId(GlMapping apGlMappingId) {
        this.apGlMappingId = apGlMappingId;
    }

    public TradingPartner getArAcctNo() {
        return arAcctNo;
    }

    public void setArAcctNo(TradingPartner arAcctNo) {
        this.arAcctNo = arAcctNo;
    }

    public GlMapping getArGlMappingId() {
        return arGlMappingId;
    }

    public void setArGlMappingId(GlMapping arGlMappingId) {
        this.arGlMappingId = arGlMappingId;
    }

    public User getEnteredByUserId() {
        return enteredByUserId;
    }

    public void setEnteredByUserId(User enteredByUserId) {
        this.enteredByUserId = enteredByUserId;
    }

    public User getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(User modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public LclUnitSs getLclUnitSs() {
        return lclUnitSs;
    }

    public void setLclUnitSs(LclUnitSs lclUnitSs) {
        this.lclUnitSs = lclUnitSs;
    }

    public LclSsDetail getLclSsDetail() {
        return lclSsDetail;
    }

    public void setLclSsDetail(LclSsDetail lclSsDetail) {
        this.lclSsDetail = lclSsDetail;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public String getApTransType() {
        return apTransType;
    }

    public void setApTransType(String apTransType) {
        this.apTransType = apTransType;
    }

    public String getArTransType() {
        return arTransType;
    }

    public void setArTransType(String arTransType) {
        this.arTransType = arTransType;
    }

    public boolean isDistributed() {
        return distributed;
    }

    public void setDistributed(boolean distributed) {
        this.distributed = distributed;
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
        if (!(object instanceof LclSsAc)) {
            return false;
        }
        LclSsAc other = (LclSsAc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cvst.logisoft.struts.form.lcl.LclSsAc[id=" + id + "]";
    }

    public String getTotalCostByInvoiceNo() {
        return totalCostByInvoiceNo;
    }

    public void setTotalCostByInvoiceNo(String totalCostByInvoiceNo) {
        this.totalCostByInvoiceNo = totalCostByInvoiceNo;
    }

    public int compareTo(LclSsAc anotherInstance) {
        if (null == anotherInstance || null == this || null == anotherInstance.getApReferenceNo() || null == this.getApReferenceNo()) {
            return 0;
        } else {
            return this.getApReferenceNo().compareTo(anotherInstance.getApReferenceNo());
        }
    }
}
