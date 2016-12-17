/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_ss_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclSsDetail.findAll", query = "SELECT l FROM LclSsDetail l"),
    @NamedQuery(name = "LclSsDetail.findById", query = "SELECT l FROM LclSsDetail l WHERE l.id = :id"),
    @NamedQuery(name = "LclSsDetail.findByTransMode", query = "SELECT l FROM LclSsDetail l WHERE l.transMode = :transMode"),
    @NamedQuery(name = "LclSsDetail.findBySpReferenceNo", query = "SELECT l FROM LclSsDetail l WHERE l.spReferenceNo = :spReferenceNo"),
    @NamedQuery(name = "LclSsDetail.findBySpReferenceName", query = "SELECT l FROM LclSsDetail l WHERE l.spReferenceName = :spReferenceName"),
    @NamedQuery(name = "LclSsDetail.findBySpBookingNo", query = "SELECT l FROM LclSsDetail l WHERE l.spBookingNo = :spBookingNo"),
    @NamedQuery(name = "LclSsDetail.findByStd", query = "SELECT l FROM LclSsDetail l WHERE l.std = :std"),
    @NamedQuery(name = "LclSsDetail.findByAtd", query = "SELECT l FROM LclSsDetail l WHERE l.atd = :atd"),
    @NamedQuery(name = "LclSsDetail.findBySta", query = "SELECT l FROM LclSsDetail l WHERE l.sta = :sta"),
    @NamedQuery(name = "LclSsDetail.findByAta", query = "SELECT l FROM LclSsDetail l WHERE l.ata = :ata"),
    @NamedQuery(name = "LclSsDetail.findByDepartureLocation", query = "SELECT l FROM LclSsDetail l WHERE l.departureLocation = :departureLocation"),
    @NamedQuery(name = "LclSsDetail.findByArrivalLocation", query = "SELECT l FROM LclSsDetail l WHERE l.arrivalLocation = :arrivalLocation"),
    @NamedQuery(name = "LclSsDetail.findByGeneralLrdt", query = "SELECT l FROM LclSsDetail l WHERE l.generalLrdt = :generalLrdt"),
    @NamedQuery(name = "LclSsDetail.findByHazmatLrdt", query = "SELECT l FROM LclSsDetail l WHERE l.hazmatLrdt = :hazmatLrdt"),
    @NamedQuery(name = "LclSsDetail.findByRelayLrdOverride", query = "SELECT l FROM LclSsDetail l WHERE l.relayLrdOverride = :relayLrdOverride"),
    @NamedQuery(name = "LclSsDetail.findByRelayTtOverride", query = "SELECT l FROM LclSsDetail l WHERE l.relayTtOverride = :relayTtOverride"),
    @NamedQuery(name = "LclSsDetail.findByNocCustomer", query = "SELECT l FROM LclSsDetail l WHERE l.nocCustomer = :nocCustomer"),
    @NamedQuery(name = "LclSsDetail.findByNocRecvAgent", query = "SELECT l FROM LclSsDetail l WHERE l.nocRecvAgent = :nocRecvAgent"),
    @NamedQuery(name = "LclSsDetail.findByNocSales", query = "SELECT l FROM LclSsDetail l WHERE l.nocSales = :nocSales"),
    @NamedQuery(name = "LclSsDetail.findByMaxUnitWeightImperial", query = "SELECT l FROM LclSsDetail l WHERE l.maxUnitWeightImperial = :maxUnitWeightImperial"),
    @NamedQuery(name = "LclSsDetail.findByMaxUnitWeightMetric", query = "SELECT l FROM LclSsDetail l WHERE l.maxUnitWeightMetric = :maxUnitWeightMetric"),
    @NamedQuery(name = "LclSsDetail.findByEnteredDatetime", query = "SELECT l FROM LclSsDetail l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclSsDetail.findByModifiedDatetime", query = "SELECT l FROM LclSsDetail l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclSsDetail extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "trans_mode")
    private String transMode;
    @Column(name = "sp_reference_no")
    private String spReferenceNo;
    @Column(name = "sp_reference_name")
    private String spReferenceName;
    @Column(name = "sp_booking_no")
    private String spBookingNo;
    @Basic(optional = false)
    @Column(name = "std")
    @Temporal(TemporalType.TIMESTAMP)
    private Date std;
    @Column(name = "atd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date atd;
    @Column(name = "sta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sta;
    @Column(name = "ata")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ata;
    @Column(name = "confirmed_eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmedEta;
    @Column(name = "confirmed_eta_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmedEtaDateTime;
    @JoinColumn(name = "confirmed_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = true)
    private User confirmeByUserId;
    @Column(name = "departure_location")
    private String departureLocation;
    @Column(name = "arrival_location")
    private String arrivalLocation;
    @Column(name = "general_loading_deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalLoadingDeadline;
    @Column(name = "hazmat_loading_deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hazmatLoadingDeadline;
    @Column(name = "general_lrdt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalLrdt;
    @Column(name = "hazmat_lrdt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hazmatLrdt;
    @Column(name = "relay_lrd_override")
    private Integer relayLrdOverride;
    @Column(name = "relay_tt_override")
    private Integer relayTtOverride;
    @Column(name = "noc_customer")
    private Boolean nocCustomer;
    @Column(name = "noc_recv_agent")
    private Boolean nocRecvAgent;
    @Column(name = "noc_sales")
    private Boolean nocSales;
    @Column(name = "max_unit_weight_imperial")
    private BigDecimal maxUnitWeightImperial;
    @Column(name = "max_unit_weight_metric")
    private BigDecimal maxUnitWeightMetric;
    @Column(name = "doc_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date docReceived;
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
    @JoinColumn(name = "recv_agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner recvAgentAcctNo;
    @JoinColumn(name = "sp_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner spAcctNo;
    @JoinColumn(name = "arrival_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation arrival;
    @JoinColumn(name = "departure_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation departure;
    @JoinColumn(name = "stuffing_warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse stuffingWarehouse;
    @JoinColumn(name = "destuffing_warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse destuffingWarehouse;
    @Column(name = "detail_status")
    private String detailStatus;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsDetail")
    private List<LclUnitSsDispo> lclUnitSsDispoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsDetail")
    private List<LclBookingDispo> lclBookingDispoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsDetail")
    private List<LclSsAc> lclSsAcList;
    @Transient
    private Long totalTTDays;

    public LclSsDetail() {
    }

    public LclSsDetail(Long id) {
        this.id = id;
    }

    public LclSsDetail(Long id, String transMode, Date std, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.transMode = transMode;
        this.std = std;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
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

    public String getSpReferenceNo() {
        return spReferenceNo;
    }

    public void setSpReferenceNo(String spReferenceNo) {
        this.spReferenceNo = spReferenceNo;
    }

    public String getSpReferenceName() {
        return spReferenceName;
    }

    public void setSpReferenceName(String spReferenceName) {
        this.spReferenceName = spReferenceName;
    }

    public String getSpBookingNo() {
        return spBookingNo;
    }

    public void setSpBookingNo(String spBookingNo) {
        this.spBookingNo = spBookingNo;
    }

    public Date getStd() {
        return std;
    }

    public void setStd(Date std) {
        this.std = std;
    }

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public Date getSta() {
        return sta;
    }

    public void setSta(Date sta) {
        this.sta = sta;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public Date getGeneralLrdt() {
        return generalLrdt;
    }

    public void setGeneralLrdt(Date generalLrdt) {
        this.generalLrdt = generalLrdt;
    }

    public Date getHazmatLrdt() {
        return hazmatLrdt;
    }

    public void setHazmatLrdt(Date hazmatLrdt) {
        this.hazmatLrdt = hazmatLrdt;
    }

    public Integer getRelayLrdOverride() {
        return relayLrdOverride;
    }

    public void setRelayLrdOverride(Integer relayLrdOverride) {
        this.relayLrdOverride = relayLrdOverride;
    }

    public Integer getRelayTtOverride() {
        return relayTtOverride;
    }

    public void setRelayTtOverride(Integer relayTtOverride) {
        this.relayTtOverride = relayTtOverride;
    }

    public Boolean getNocCustomer() {
        return nocCustomer;
    }

    public void setNocCustomer(Boolean nocCustomer) {
        this.nocCustomer = nocCustomer;
    }

    public Boolean getNocRecvAgent() {
        return nocRecvAgent;
    }

    public void setNocRecvAgent(Boolean nocRecvAgent) {
        this.nocRecvAgent = nocRecvAgent;
    }

    public Boolean getNocSales() {
        return nocSales;
    }

    public void setNocSales(Boolean nocSales) {
        this.nocSales = nocSales;
    }

    public BigDecimal getMaxUnitWeightImperial() {
        return maxUnitWeightImperial;
    }

    public void setMaxUnitWeightImperial(BigDecimal maxUnitWeightImperial) {
        this.maxUnitWeightImperial = maxUnitWeightImperial;
    }

    public BigDecimal getMaxUnitWeightMetric() {
        return maxUnitWeightMetric;
    }

    public void setMaxUnitWeightMetric(BigDecimal maxUnitWeightMetric) {
        this.maxUnitWeightMetric = maxUnitWeightMetric;
    }

    public Date getDocReceived() {
        return docReceived;
    }

    public void setDocReceived(Date docReceived) {
        this.docReceived = docReceived;
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

    public UnLocation getArrival() {
        return arrival;
    }

    public void setArrival(UnLocation arrival) {
        this.arrival = arrival;
    }

    public UnLocation getDeparture() {
        return departure;
    }

    public void setDeparture(UnLocation departure) {
        this.departure = departure;
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

    public TradingPartner getRecvAgentAcctNo() {
        return recvAgentAcctNo;
    }

    public void setRecvAgentAcctNo(TradingPartner recvAgentAcctNo) {
        this.recvAgentAcctNo = recvAgentAcctNo;
    }

    public List<LclBookingDispo> getLclBookingDispoList() {
        return lclBookingDispoList;
    }

    public void setLclBookingDispoList(List<LclBookingDispo> lclBookingDispoList) {
        this.lclBookingDispoList = lclBookingDispoList;
    }

    public TradingPartner getSpAcctNo() {
        return spAcctNo;
    }

    public void setSpAcctNo(TradingPartner spAcctNo) {
        this.spAcctNo = spAcctNo;
    }

    public Warehouse getDestuffingWarehouse() {
        return destuffingWarehouse;
    }

    public void setDestuffingWarehouse(Warehouse destuffingWarehouse) {
        this.destuffingWarehouse = destuffingWarehouse;
    }

    public Warehouse getStuffingWarehouse() {
        return stuffingWarehouse;
    }

    public void setStuffingWarehouse(Warehouse stuffingWarehouse) {
        this.stuffingWarehouse = stuffingWarehouse;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public List<LclUnitSsDispo> getLclUnitSsDispoList() {
        return lclUnitSsDispoList;
    }

    public void setLclUnitSsDispoList(List<LclUnitSsDispo> lclUnitSsDispoList) {
        this.lclUnitSsDispoList = lclUnitSsDispoList;
    }

    public List<LclSsAc> getLclSsAcList() {
        return lclSsAcList;
    }

    public void setLclSsAcList(List<LclSsAc> lclSsAcList) {
        this.lclSsAcList = lclSsAcList;
    }

    public Date getConfirmedEta() {
        return confirmedEta;
    }

    public void setConfirmedEta(Date confirmedEta) {
        this.confirmedEta = confirmedEta;
    }

    public User getConfirmeByUserId() {
        return confirmeByUserId;
    }

    public void setConfirmeByUserId(User confirmeByUserId) {
        this.confirmeByUserId = confirmeByUserId;
    }

    public Date getConfirmedEtaDateTime() {
        return confirmedEtaDateTime;
    }

    public void setConfirmedEtaDateTime(Date confirmedEtaDateTime) {
        this.confirmedEtaDateTime = confirmedEtaDateTime;
    }

    public Long getTotalTTDays() {
        return totalTTDays;
    }

    public void setTotalTTDays(Long totalTTDays) {
        this.totalTTDays = totalTTDays;
    }

    public Date getGeneralLoadingDeadline() {
        return generalLoadingDeadline;
    }

    public void setGeneralLoadingDeadline(Date generalLoadingDeadline) {
        this.generalLoadingDeadline = generalLoadingDeadline;
    }

    public Date getHazmatLoadingDeadline() {
        return hazmatLoadingDeadline;
    }

    public void setHazmatLoadingDeadline(Date hazmatLoadingDeadline) {
        this.hazmatLoadingDeadline = hazmatLoadingDeadline;
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
        if (!(object instanceof LclSsDetail)) {
            return false;
        }
        LclSsDetail other = (LclSsDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclSsDetail[id=" + id + "]";
    }
}
