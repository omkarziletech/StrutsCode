/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Rajesh
 */
@Entity
@Table(name = "eculine_edi")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "EculineEdi.findByVoyNo", query = "SELECT e FROM EculineEdi e WHERE e.voyNo = :voyNo"),
    @NamedQuery(name = "EculineEdi.findById", query = "SELECT e FROM EculineEdi e WHERE e.id = :id")
})
public class EculineEdi extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "header_ref")
    private String headerRef;
    @Column(name = "sender_code")
    private String senderCode;
    @Column(name = "sender_email")
    private String senderEmail;
    @Column(name = "receiver_code")
    private String receiverCode;
    @Column(name = "receiver_email")
    private String receiverEmail;
    @Column(name = "header_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date headerDate;
    @Column(name = "vessel_name")
    private String vesselName;
    @Column(name = "lloyds_No")
    private String lloydsNo;
    @Column(name = "voy_No")
    private String voyNo;
    @JoinColumn(name = "unitss_id", referencedColumnName = "id")
    @ManyToOne
    private LclUnitSs unitSs;
    @Basic(optional = false)
    @Column(name = "etd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date etd;
    @Basic(optional = false)
    @Column(name = "eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eta;
    @Column(name = "pol_uncode")
    private String polUncode;
    @Column(name = "pol_desc")
    private String polDesc;
    @Column(name = "pod_uncode")
    private String podUncode;
    @Column(name = "pod_desc")
    private String podDesc;
    @Column(name = "loading_agent")
    private String loadingAgent;
    @Column(name = "delivery_agent")
    private String deliveryAgent;
    @Column(name = "transport_means")
    private String transportMeans;
    @Column(name = "transport_name")
    private String transportName;
    @Column(name = "transport_terminal")
    private String transportTerminal;
    @Column(name = "shipping_name")
    private String shippingName;
    @Column(name = "scac_code")
    private String scacCode;
    @JoinColumn(name = "shipping_scac", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner ssline;
    @Column(name = "rate_currency")
    private String rateCurrency;
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eculineEdi", fetch = FetchType.EAGER)
    private List<ContainerInfo> containerInfoCollection;
    @JoinColumn(name = "terminal_id", referencedColumnName = "trmnum")
    @ManyToOne
    private RefTerminal terminal;
    @Column(name = "is_approved")
    private boolean approved;
    @Column(name = "file_name")
    private String fileName;
    @Lob
    @Column(name = "file")
    private byte[] file;
    @Column(name = "updated_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    @ManyToOne
    private User updatedByUser;
    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    @ManyToOne
    private User createdByUser;
    @Basic(optional = false)
    @Column(name = "vessel_error_check")
    private boolean vesselErrorCheck;
    @Basic(optional = false)
    @Column(name = "adjudicated")
    private boolean adjudicated = false;
    @Column(name = "doc_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date docReceived;
    public EculineEdi() {
    }

    public EculineEdi(Long id) {
        this.id = id;
    }

    public EculineEdi(Long id, Date etd, Date eta) {
        this.id = id;
        this.etd = etd;
        this.eta = eta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeaderRef() {
        return headerRef;
    }

    public void setHeaderRef(String headerRef) {
        this.headerRef = headerRef;
    }

    public String getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverCode() {
        return receiverCode;
    }

    public void setReceiverCode(String receiverCode) {
        this.receiverCode = receiverCode;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public Date getHeaderDate() {
        return headerDate;
    }

    public void setHeaderDate(Date headerDate) {
        this.headerDate = headerDate;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getLloydsNo() {
        return lloydsNo;
    }

    public void setLloydsNo(String lloydsNo) {
        this.lloydsNo = lloydsNo;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public String getPolUncode() {
        return polUncode;
    }

    public void setPolUncode(String polUncode) {
        this.polUncode = polUncode;
    }

    public String getPolDesc() {
        return polDesc;
    }

    public void setPolDesc(String polDesc) {
        this.polDesc = polDesc;
    }

    public String getPodUncode() {
        return podUncode;
    }

    public void setPodUncode(String podUncode) {
        this.podUncode = podUncode;
    }

    public String getPodDesc() {
        return podDesc;
    }

    public void setPodDesc(String podDesc) {
        this.podDesc = podDesc;
    }

    public String getLoadingAgent() {
        return loadingAgent;
    }

    public void setLoadingAgent(String loadingAgent) {
        this.loadingAgent = loadingAgent;
    }

    public String getDeliveryAgent() {
        return deliveryAgent;
    }

    public void setDeliveryAgent(String deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public String getTransportMeans() {
        return transportMeans;
    }

    public void setTransportMeans(String transportMeans) {
        this.transportMeans = transportMeans;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getTransportTerminal() {
        return transportTerminal;
    }

    public void setTransportTerminal(String transportTerminal) {
        this.transportTerminal = transportTerminal;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public TradingPartner getSsline() {
        return ssline;
    }

    public void setSsline(TradingPartner ssline) {
        this.ssline = ssline;
    }

    public String getRateCurrency() {
        return rateCurrency;
    }

    public void setRateCurrency(String rateCurrency) {
        this.rateCurrency = rateCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public List<ContainerInfo> getContainerInfoCollection() {
        return containerInfoCollection;
    }

    public void setContainerInfoCollection(List<ContainerInfo> containerInfoCollection) {
        this.containerInfoCollection = containerInfoCollection;
    }

    public RefTerminal getTerminal() {
        return terminal;
    }

    public void setTerminal(RefTerminal terminal) {
        this.terminal = terminal;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LclUnitSs getUnitSs() {
        return unitSs;
    }

    public void setUnitSs(LclUnitSs unitSs) {
        this.unitSs = unitSs;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getScacCode() {
        return scacCode;
    }

    public void setScacCode(String scacCode) {
        this.scacCode = scacCode;
    }

    public boolean isVesselErrorCheck() {
        return vesselErrorCheck;
    }

    public void setVesselErrorCheck(boolean vesselErrorCheck) {
        this.vesselErrorCheck = vesselErrorCheck;
    }

    public boolean isAdjudicated() {
        return adjudicated;
    }

    public void setAdjudicated(boolean adjudicated) {
        this.adjudicated = adjudicated;
    }

    public Date getDocReceived() {
        return docReceived;
    }

    public void setDocReceived(Date docReceived) {
        this.docReceived = docReceived;
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
        if (!(object instanceof EculineEdi)) {
            return false;
        }
        EculineEdi other = (EculineEdi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ecuedi.model.EculineEdi[id=" + id + "]";
    }
}
