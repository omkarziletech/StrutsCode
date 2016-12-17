/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author saravanan
 */
@Entity
@Table(name = "lcl_correction")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
public class LclCorrection extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "correction_no")
    private long correctionNo;
    @Basic(optional = false)
    @Lob
    @Column(name = "comments")
    private String comments;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "debit_email")
    private String debitEmail;
    @Column(name = "credit_email")
    private String creditEmail;
    @Column(name = "approved_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedDate;
    @Column(name = "posted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedDate;
    @Column(name = "voided_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date voidedDate;
    @Column(name = "void")
    private Boolean void1;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Column(name = "current_profit")
    private BigDecimal currentProfit;
    @Column(name = "profit_aftercn")
    private BigDecimal profitAfterCN;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "posted_by", referencedColumnName = "user_id")
    @ManyToOne
    private User postedBy;
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    @ManyToOne
    private User approvedBy;
    @JoinColumn(name = "voided_by", referencedColumnName = "user_id")
    @ManyToOne
    private User voidedBy;
    @JoinColumn(name = "CODE", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GenericCode code;
    @JoinColumn(name = "TYPE", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GenericCode type;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "cons_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner consAcct;
    @JoinColumn(name = "noty_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner notyAcct;
    @JoinColumn(name = "third_party_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner thirdPartyAcct;
    @JoinColumn(name = "party_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner partyAcctNo;
    @JoinColumn(name = "cfs_dev_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner cfsDevAcctNo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclCorrection", fetch = FetchType.EAGER)
    private Collection<LclCorrectionCharge> lclCorrectionChargeCollection;
    @Column(name = "bill_to_party")
    private String billToParty;
    @Column(name = "billing_type")
    private String billingType;
    @JoinColumn(name = "old_shipper_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner oldShipper;
    @JoinColumn(name = "new_shipper_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner newShipper;
    @JoinColumn(name = "old_agent_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner oldAgent;
    @JoinColumn(name = "new_agent_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner newAgent;
    @JoinColumn(name = "old_forwarder_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner oldForwarder;
    @JoinColumn(name = "new_forwarder_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner newForwarder;
    @JoinColumn(name = "new_third_party_acct", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner newThirdParty;

    public LclCorrection() {
    }

    public LclCorrection(Long id) {
        this.id = id;
    }

    public LclCorrection(Long id, long correctionNo, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.correctionNo = correctionNo;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCorrectionNo() {
        return correctionNo;
    }

    public void setCorrectionNo(long correctionNo) {
        this.correctionNo = correctionNo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDebitEmail() {
        return debitEmail;
    }

    public void setDebitEmail(String debitEmail) {
        this.debitEmail = debitEmail;
    }

    public String getCreditEmail() {
        return creditEmail;
    }

    public void setCreditEmail(String creditEmail) {
        this.creditEmail = creditEmail;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Boolean getVoid1() {
        return void1;
    }

    public void setVoid1(Boolean void1) {
        this.void1 = void1;
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

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public Collection<LclCorrectionCharge> getLclCorrectionChargeCollection() {
        return lclCorrectionChargeCollection;
    }

    public void setLclCorrectionChargeCollection(Collection<LclCorrectionCharge> lclCorrectionChargeCollection) {
        this.lclCorrectionChargeCollection = lclCorrectionChargeCollection;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public GenericCode getCode() {
        return code;
    }

    public void setCode(GenericCode code) {
        this.code = code;
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

    public TradingPartner getConsAcct() {
        return consAcct;
    }

    public void setConsAcct(TradingPartner consAcct) {
        this.consAcct = consAcct;
    }

    public TradingPartner getNotyAcct() {
        return notyAcct;
    }

    public void setNotyAcct(TradingPartner notyAcct) {
        this.notyAcct = notyAcct;
    }

    public TradingPartner getThirdPartyAcct() {
        return thirdPartyAcct;
    }

    public void setThirdPartyAcct(TradingPartner thirdPartyAcct) {
        this.thirdPartyAcct = thirdPartyAcct;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public GenericCode getType() {
        return type;
    }

    public void setType(GenericCode type) {
        this.type = type;
    }

    public User getVoidedBy() {
        return voidedBy;
    }

    public void setVoidedBy(User voidedBy) {
        this.voidedBy = voidedBy;
    }

    public Date getVoidedDate() {
        return voidedDate;
    }

    public void setVoidedDate(Date voidedDate) {
        this.voidedDate = voidedDate;
    }

    public TradingPartner getPartyAcctNo() {
        return partyAcctNo;
    }

    public void setPartyAcctNo(TradingPartner partyAcctNo) {
        this.partyAcctNo = partyAcctNo;
    }

    public TradingPartner getCfsDevAcctNo() {
        return cfsDevAcctNo;
    }

    public void setCfsDevAcctNo(TradingPartner cfsDevAcctNo) {
        this.cfsDevAcctNo = cfsDevAcctNo;
    }

    public BigDecimal getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(BigDecimal currentProfit) {
        this.currentProfit = currentProfit;
    }

    public BigDecimal getProfitAfterCN() {
        return profitAfterCN;
    }

    public void setProfitAfterCN(BigDecimal profitAfterCN) {
        this.profitAfterCN = profitAfterCN;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public TradingPartner getOldShipper() {
        return oldShipper;
    }

    public void setOldShipper(TradingPartner oldShipper) {
        this.oldShipper = oldShipper;
    }

    public TradingPartner getNewShipper() {
        return newShipper;
    }

    public void setNewShipper(TradingPartner newShipper) {
        this.newShipper = newShipper;
    }

    public TradingPartner getOldAgent() {
        return oldAgent;
    }

    public void setOldAgent(TradingPartner oldAgent) {
        this.oldAgent = oldAgent;
    }

    public TradingPartner getNewAgent() {
        return newAgent;
    }

    public void setNewAgent(TradingPartner newAgent) {
        this.newAgent = newAgent;
    }

    public TradingPartner getOldForwarder() {
        return oldForwarder;
    }

    public void setOldForwarder(TradingPartner oldForwarder) {
        this.oldForwarder = oldForwarder;
    }

    public TradingPartner getNewForwarder() {
        return newForwarder;
    }

    public void setNewForwarder(TradingPartner newForwarder) {
        this.newForwarder = newForwarder;
    }

    public TradingPartner getNewThirdParty() {
        return newThirdParty;
    }

    public void setNewThirdParty(TradingPartner newThirdParty) {
        this.newThirdParty = newThirdParty;
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
        if (!(object instanceof LclCorrection)) {
            return false;
        }
        LclCorrection other = (LclCorrection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclCorrection[id=" + id + "]";
    }
}
