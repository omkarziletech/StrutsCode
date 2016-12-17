/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_booking")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclBooking extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_number_id")
    private Long fileNumberId;
    @Basic(optional = false)
    @Column(name = "booking_type")
    private String bookingType;
    @Basic(optional = false)
    @Column(name = "trans_mode")
    private String transMode;
    @Basic(optional = false)
    @Column(name = "client_pwk_recvd")
    private boolean clientPwkRecvd;
    @Column(name = "rtd_transaction")
    private Boolean rtdTransaction;
    @Column(name = "value_of_goods")
    private BigDecimal valueOfGoods;
    @Column(name = "pod_fd_tt")
    private Integer podfdtt;
    @Column(name = "non_rated")
    private boolean nonRated;
    @Column(name = "poo_pickup")
    private Boolean pooPickup;
    @Column(name = "fd_deliver")
    private Boolean fdDeliver;
    @Column(name = "fd_Eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdEta;
    @Column(name = "poo_whse_lrdt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pooWhseLrdt;
    @Basic(optional = false)
    @Column(name = "spot_rate")
    private boolean spotRate;
    @Column(name = "rate_type")
    private String rateType;
    @Column(name = "delivery_metro")
    private String deliveryMetro;
    @Basic(optional = false)
    @Column(name = "relay_override")
    private boolean relayOverride;
    @Basic(optional = false)
    @Column(name = "billing_type")
    private String billingType;
    @Basic(optional = true)
    @Column(name = "default_agent")
    private boolean defaultAgent = true;
    @Basic(optional = false)
    @Column(name = "over_short_damaged")
    private boolean overShortdamaged;
    @Column(name = "insurance")
    private boolean insurance;
    @Column(name = "documentation")
    private boolean documentation;
    @Column(name = "hold")
    private String hold;
    @Column(name = "declared_cargo_value")
    private BigDecimal declaredCargoValue;
    @Column(name = "sup_reference")
    private String supReference;
    @Column(name = "ship_reference")
    private String shipReference;
    @Column(name = "fwd_fmc_no")
    private String fwdFmcNo;
    @Column(name = "fwd_reference")
    private String fwdReference;
    @Column(name = "cons_reference")
    private String consReference;
    @Column(name = "noty_reference")
    private String notyReference;
    @Column(name = "override_dim_uom")
    private String overrideDimUom;
    @Column(name = "override_dim_volume")
    private BigDecimal overrideDimVolume;
    @Column(name = "override_dim_weight")
    private BigDecimal overrideDimWeight;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
     @Column(name = "terminate_desc")
    private String terminateDesc;
     
    @JoinColumn(name = "pod_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation portOfDestination;
    
    @JoinColumn(name = "pol_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation portOfLoading;
    @JoinColumn(name = "client_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner clientAcct;
    @JoinColumn(name = "booked_ss_header_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsHeader bookedSsHeaderId;
    @JoinColumn(name = "fd_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation finalDestination;
    @JoinColumn(name = "poo_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation portOfOrigin;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "billing_terminal", referencedColumnName = "trmnum")
    @ManyToOne
    private RefTerminal terminal;
    @JoinColumn(name = "rtd_agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner rtdAgentAcct;
    @JoinColumn(name = "noty_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner notyAcct;
    @JoinColumn(name = "cons_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner consAcct;
    @JoinColumn(name = "fwd_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner fwdAcct;
  
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private LclFileNumber lclFileNumber;
    
    @JoinColumn(name = "client_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact clientContact;
    
    @JoinColumn(name = "sup_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact supContact;
    @JoinColumn(name = "ship_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact shipContact;
    @JoinColumn(name = "fwd_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact fwdContact;
    @JoinColumn(name = "cons_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact consContact;
    @JoinColumn(name = "noty_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact notyContact;
    @JoinColumn(name = "poo_whse_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Warehouse pooWhse;
    @JoinColumn(name = "poo_whse_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact pooWhseContact;
    @JoinColumn(name = "rtd_agent_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact rtAgentContact;
    @JoinColumn(name = "agent_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact agentContact;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner agentAcct;
    @JoinColumn(name = "sup_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner supAcct;
    @JoinColumn(name = "ship_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner shipAcct;
    @JoinColumn(name = "third_party_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner thirdPartyAcct;
    @JoinColumn(name = "third_party_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact thirdPartyContact;
    @Column(name = "bill_to_party")
    private String billToParty;
    @Column(name = "spot_wm_rate")
    private BigDecimal spotWmRate;
    @Column(name = "spot_measure_rate")
    private BigDecimal spotRateMeasure;
    @Column(name = "spot_rate_uom")
    private String spotRateUom;
    @Column(name = "spot_comment")
    private String spotComment;
    @Column(name = "spot_rate_bottom")
    private boolean spotRateBottom;
    @Column(name = "spot_rate_ofrate")
    private boolean spotOfRate;
    @JoinColumn(name = "noty2_contact_id", referencedColumnName = "id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private LclContact notify2Contact;
    @Column(name = "noty2_reference")
    private String noty2Reference;
    @Column(name = "insurance_cif")
    private BigDecimal cifValue;

    public LclBooking() {
    }

    public LclBooking(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclBooking(Long fileNumberId, String bookingType, String transMode, boolean clientPwkRecvd, boolean spotRate, String rateType, boolean relayOverride, String billingType, boolean defaultAgent, Date enteredDatetime, Date modifiedDatetime, boolean overShortdamaged) {
        this.fileNumberId = fileNumberId;
        this.bookingType = bookingType;
        this.transMode = transMode;
        this.clientPwkRecvd = clientPwkRecvd;
        this.spotRate = spotRate;
        this.rateType = rateType;
        this.relayOverride = relayOverride;
        this.billingType = billingType;
        this.defaultAgent = defaultAgent;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.overShortdamaged = overShortdamaged;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public boolean getClientPwkRecvd() {
        return clientPwkRecvd;
    }

    public void setClientPwkRecvd(boolean clientPwkRecvd) {
        this.clientPwkRecvd = clientPwkRecvd;
    }

    public boolean getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(boolean spotRate) {
        this.spotRate = spotRate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getDeliveryMetro() {
        return deliveryMetro;
    }

    public void setDeliveryMetro(String deliveryMetro) {
        this.deliveryMetro = deliveryMetro;
    }

    public boolean getRelayOverride() {
        return relayOverride;
    }

    public void setRelayOverride(boolean relayOverride) {
        this.relayOverride = relayOverride;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean getNonRated() {
        return nonRated;
    }

    public void setNonRated(boolean nonRated) {
        this.nonRated = nonRated;
    }

    public BigDecimal getValueOfGoods() {
        return valueOfGoods;
    }

    public void setValueOfGoods(BigDecimal valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
    }

    public boolean getDefaultAgent() {
        return defaultAgent;
    }

    public void setDefaultAgent(boolean defaultAgent) {
        this.defaultAgent = defaultAgent;
    }

    public boolean getOverShortdamaged() {
        return overShortdamaged;
    }

    public void setOverShortdamaged(boolean overShortdamaged) {
        this.overShortdamaged = overShortdamaged;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public Boolean getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Boolean documentation) {
        this.documentation = documentation;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }
    
    public BigDecimal getDeclaredCargoValue() {
        return declaredCargoValue;
    }

    public void setDeclaredCargoValue(BigDecimal declaredCargoValue) {
        this.declaredCargoValue = declaredCargoValue;
    }

    public String getSupReference() {
        return supReference;
    }

    public void setSupReference(String supReference) {
        this.supReference = supReference;
    }

    public String getShipReference() {
        return shipReference;
    }

    public void setShipReference(String shipReference) {
        this.shipReference = shipReference;
    }

    public String getFwdFmcNo() {
        return fwdFmcNo;
    }

    public void setFwdFmcNo(String fwdFmcNo) {
        this.fwdFmcNo = fwdFmcNo;
    }

    public String getFwdReference() {
        return fwdReference;
    }

    public void setFwdReference(String fwdReference) {
        this.fwdReference = fwdReference;
    }

    public String getConsReference() {
        return consReference;
    }

    public void setConsReference(String consReference) {
        this.consReference = consReference;
    }

    public String getNotyReference() {
        return notyReference;
    }

    public void setNotyReference(String notyReference) {
        this.notyReference = notyReference;
    }

    public String getOverrideDimUom() {
        return overrideDimUom;
    }

    public void setOverrideDimUom(String overrideDimUom) {
        this.overrideDimUom = overrideDimUom;
    }

    public BigDecimal getOverrideDimVolume() {
        return overrideDimVolume;
    }

    public void setOverrideDimVolume(BigDecimal overrideDimVolume) {
        this.overrideDimVolume = overrideDimVolume;
    }

    public BigDecimal getOverrideDimWeight() {
        return overrideDimWeight;
    }

    public void setOverrideDimWeight(BigDecimal overrideDimWeight) {
        this.overrideDimWeight = overrideDimWeight;
    }

    public Integer getPodfdtt() {
        return podfdtt;
    }

    public void setPodfdtt(Integer podfdtt) {
        this.podfdtt = podfdtt;
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

    public LclSsHeader getBookedSsHeaderId() {
        return bookedSsHeaderId;
    }

    public void setBookedSsHeaderId(LclSsHeader bookedSsHeaderId) {
        this.bookedSsHeaderId = bookedSsHeaderId;
    }

    public UnLocation getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(UnLocation finalDestination) {
        this.finalDestination = finalDestination;
    }

    public UnLocation getPortOfDestination() {
        return portOfDestination;
    }

    public void setPortOfDestination(UnLocation portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public UnLocation getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(UnLocation portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public UnLocation getPortOfOrigin() {
        return portOfOrigin;
    }

    public void setPortOfOrigin(UnLocation portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
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

    public RefTerminal getTerminal() {
        return terminal;
    }

    public void setTerminal(RefTerminal terminal) {
        this.terminal = terminal;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public TradingPartner getAgentAcct() {
        return agentAcct;
    }

    public void setAgentAcct(TradingPartner agentAcct) {
        this.agentAcct = agentAcct;
    }

    public TradingPartner getClientAcct() {
        return clientAcct;
    }

    public void setClientAcct(TradingPartner clientAcct) {
        this.clientAcct = clientAcct;
    }

    public LclContact getClientContact() {
        if (clientContact == null) {
            clientContact = new LclContact();
        }
        return clientContact;
    }

    public void setClientContact(LclContact clientContact) {
        this.clientContact = clientContact;
    }

    public TradingPartner getConsAcct() {
        return consAcct;
    }

    public void setConsAcct(TradingPartner consAcct) {
        this.consAcct = consAcct;
    }

    public LclContact getConsContact() {
        if (consContact == null) {
            consContact = new LclContact();
        }
        return consContact;
    }

    public void setConsContact(LclContact consContact) {
        this.consContact = consContact;
    }

    public TradingPartner getFwdAcct() {
        return fwdAcct;
    }

    public void setFwdAcct(TradingPartner fwdAcct) {
        this.fwdAcct = fwdAcct;
    }

    public LclContact getFwdContact() {
        if (fwdContact == null) {
            fwdContact = new LclContact();
        }
        return fwdContact;
    }

    public void setFwdContact(LclContact fwdContact) {
        this.fwdContact = fwdContact;
    }

    public TradingPartner getNotyAcct() {
        return notyAcct;
    }

    public void setNotyAcct(TradingPartner notyAcct) {
        this.notyAcct = notyAcct;
    }

    public LclContact getNotyContact() {
        if (notyContact == null) {
            notyContact = new LclContact();
        }
        return notyContact;
    }

    public void setNotyContact(LclContact notyContact) {
        this.notyContact = notyContact;
    }

    public TradingPartner getShipAcct() {
        return shipAcct;
    }

    public void setShipAcct(TradingPartner shipAcct) {
        this.shipAcct = shipAcct;
    }

    public LclContact getShipContact() {
        if (shipContact == null) {
            shipContact = new LclContact();
        }
        return shipContact;
    }

    public void setShipContact(LclContact shipContact) {
        this.shipContact = shipContact;
    }

    public TradingPartner getSupAcct() {
        return supAcct;
    }

    public void setSupAcct(TradingPartner supAcct) {
        this.supAcct = supAcct;
    }

    public LclContact getSupContact() {
        if (supContact == null) {
            supContact = new LclContact();
        }
        return supContact;
    }

    public void setSupContact(LclContact supContact) {
        this.supContact = supContact;
    }

    public Date getFdEta() {
        return fdEta;
    }

    public void setFdEta(Date fdEta) {
        this.fdEta = fdEta;
    }

    public Warehouse getPooWhse() {
        return pooWhse;
    }

    public void setPooWhse(Warehouse pooWhse) {
        this.pooWhse = pooWhse;
    }

    public LclContact getPooWhseContact() {
        if (pooWhseContact == null) {
            pooWhseContact = new LclContact();
        }
        return pooWhseContact;
    }

    public void setPooWhseContact(LclContact pooWhseContact) {
        this.pooWhseContact = pooWhseContact;
    }

    public Date getPooWhseLrdt() {
        return pooWhseLrdt;
    }

    public void setPooWhseLrdt(Date pooWhseLrdt) {
        this.pooWhseLrdt = pooWhseLrdt;
    }

    public LclContact getRtAgentContact() {
        return rtAgentContact;
    }

    public void setRtAgentContact(LclContact rtAgentContact) {
        this.rtAgentContact = rtAgentContact;
    }

    public TradingPartner getRtdAgentAcct() {
        return rtdAgentAcct;
    }

    public void setRtdAgentAcct(TradingPartner rtdAgentAcct) {
        this.rtdAgentAcct = rtdAgentAcct;
    }

    public Boolean getRtdTransaction() {
        return rtdTransaction;
    }

    public void setRtdTransaction(Boolean rtdTransaction) {
        this.rtdTransaction = rtdTransaction;
    }

    public Boolean getFdDeliver() {
        return fdDeliver;
    }

    public void setFdDeliver(Boolean fdDeliver) {
        this.fdDeliver = fdDeliver;
    }

    public Boolean getPooPickup() {
        return pooPickup;
    }

    public void setPooPickup(Boolean pooPickup) {
        this.pooPickup = pooPickup;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public TradingPartner getThirdPartyAcct() {
        return thirdPartyAcct;
    }

    public void setThirdPartyAcct(TradingPartner thirdPartyAcct) {
        this.thirdPartyAcct = thirdPartyAcct;
    }

    public LclContact getThirdPartyContact() {
        return thirdPartyContact;
    }

    public void setThirdPartyContact(LclContact thirdPartyContact) {
        this.thirdPartyContact = thirdPartyContact;
    }

    public LclContact getAgentContact() {
        return agentContact;
    }

    public void setAgentContact(LclContact agentContact) {
        this.agentContact = agentContact;
    }

    public String getSpotRateUom() {
        return spotRateUom;
    }

    public void setSpotRateUom(String spotRateUom) {
        this.spotRateUom = spotRateUom;
    }

    public BigDecimal getSpotWmRate() {
        return spotWmRate;
    }

    public void setSpotWmRate(BigDecimal spotWmRate) {
        this.spotWmRate = spotWmRate;
    }

    public String getSpotComment() {
        return spotComment;
    }

    public void setSpotComment(String spotComment) {
        this.spotComment = spotComment;
    }

    public BigDecimal getSpotRateMeasure() {
        return spotRateMeasure;
    }

    public void setSpotRateMeasure(BigDecimal spotRateMeasure) {
        this.spotRateMeasure = spotRateMeasure;
    }

    public boolean getSpotOfRate() {
        return spotOfRate;
    }

    public void setSpotOfRate(boolean spotOfRate) {
        this.spotOfRate = spotOfRate;
    }

    public boolean getSpotRateBottom() {
        return spotRateBottom;
    }

    public void setSpotRateBottom(boolean spotRateBottom) {
        this.spotRateBottom = spotRateBottom;
    }

    public LclContact getNotify2Contact() {
        if (notify2Contact == null) {
            notify2Contact = new LclContact();
        }
        return notify2Contact;
    }

    public void setNotify2Contact(LclContact notify2Contact) {
        this.notify2Contact = notify2Contact;
    }

    public String getNoty2Reference() {
        return noty2Reference;
    }

    public void setNoty2Reference(String noty2Reference) {
        this.noty2Reference = noty2Reference;
    }

    public String getTerminateDesc() {
        return terminateDesc;
    }

    public void setTerminateDesc(String terminateDesc) {
        this.terminateDesc = terminateDesc;
    }

    public BigDecimal getCifValue() {
        return cifValue;
    }

    public void setCifValue(BigDecimal cifValue) {
        this.cifValue = cifValue;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileNumberId != null ? fileNumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclBooking)) {
            return false;
        }
        LclBooking other = (LclBooking) object;
        if ((this.fileNumberId == null && other.fileNumberId != null) || (this.fileNumberId != null && !this.fileNumberId.equals(other.fileNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBooking[fileNumberId=" + fileNumberId + "]";
    }
}
