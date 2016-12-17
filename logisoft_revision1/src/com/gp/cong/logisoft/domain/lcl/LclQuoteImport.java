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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "lcl_quote_import")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclQuoteImport extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_number_id")
    private Long fileNumberId;
    @Column(name = "customs_clearance_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date customsClearanceReceived;
    @Column(name = "transshipment")
    private boolean transShipment;
    @Column(name = "it_class")
    private String itClass;
    @Column(name = "inbond_via")
    private String inbondVia;
    @Column(name = "it_no")
    private String itNo;
    @Column(name = "it_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itDatetime;
    @Column(name = "picked_up_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickupDateTime;
    @Column(name = "door_delivery_status")
    private String doorDeliveryStatus;
    @Column(name = "pod_signed_by")
    private String podSignedBy;
    @Column(name = "pod_signed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date podSignedDatetime;
    @Column(name = "customs_entry_class")
    private String entryClass;
    @Column(name = "customs_entry_no")
    private String entryNo;
    @Column(name = "sub_house_bl")
    private String subHouseBl;
    @Column(name = "declared_value")
    private BigDecimal declaredValue;
    @Column(name = "declared_value_estimated")
    private Boolean declaredValueEstimated;
    @Column(name = "declared_weight")
    private BigDecimal declaredWeight;
    @Column(name = "declared_weight_uom")
    private String declaredWeightUom;
    @Column(name = "declared_weight_estimated")
    private Boolean declaredWeightEstimated;
    @Column(name = "delivery_order_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryOrderReceived;
    @Column(name = "express_release")
    private Boolean expressReleaseClause;
    @Lob
    @Column(name = "freight_release_bl_note")
    private String freightReleaseBlNote;
    @Column(name = "freight_released_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date freightReleaseDateTime;
    @Column(name = "original_bl_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date originalBlReceived;
    @Column(name = "payment_release_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentReleaseReceived;
    @Column(name = "release_order_received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseOrderReceived;
    @Column(name = "go_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date goDatetime;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Column(name = "door_delivery_eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doorDeliveryEta;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "dest_whse_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Warehouse destWhse;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "ipi_cfs_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner ipiCfsAcctNo;
    @JoinColumn(name = "usa_port_of_exit_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation usaPortOfExit;
    @JoinColumn(name = "foreign_port_of_discharge_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation foreignPortOfDischarge;
    @JoinColumn(name = "delivery_order_received_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User customsClearanceUserId;
    @JoinColumn(name = "customs_clearance_received_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User deliveryOrderUserId;
    @JoinColumn(name = "freight_released_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User freightReleaseUserId;
    @JoinColumn(name = "release_order_received_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User releaseOrderUserId;
    @JoinColumn(name = "original_bl_received_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User originalBlUserId;
    @JoinColumn(name = "payment_release_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User paymentReleaseUserId;
    @Column(name = "scac")
    private String scac;
    @Column(name = "customs_release_no")
    private String customsReleaseNo;
    @Column(name = "customs_release_value")
    private BigDecimal customsReleaseValue;
    @Column(name = "fd_eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdEta;
    @Column(name = "last_free_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFreeDateTime;
    @JoinColumn(name = "export_agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner exportAgentAcctNo;

    public LclQuoteImport() {
    }

    public LclQuoteImport(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclQuoteImport(Long fileNumberId, Date enteredDatetime, Date modifiedDatetime) {
        this.fileNumberId = fileNumberId;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Date getDoorDeliveryEta() {
        return doorDeliveryEta;
    }

    public void setDoorDeliveryEta(Date doorDeliveryEta) {
        this.doorDeliveryEta = doorDeliveryEta;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public Date getCustomsClearanceReceived() {
        return customsClearanceReceived;
    }

    public void setCustomsClearanceReceived(Date customsClearanceReceived) {
        this.customsClearanceReceived = customsClearanceReceived;
    }

    public User getCustomsClearanceUserId() {
        return customsClearanceUserId;
    }

    public void setCustomsClearanceUserId(User customsClearanceUserId) {
        this.customsClearanceUserId = customsClearanceUserId;
    }

    public BigDecimal getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(BigDecimal declaredValue) {
        this.declaredValue = declaredValue;
    }

    public Boolean getDeclaredValueEstimated() {
        return declaredValueEstimated;
    }

    public void setDeclaredValueEstimated(Boolean declaredValueEstimated) {
        this.declaredValueEstimated = declaredValueEstimated;
    }

    public BigDecimal getDeclaredWeight() {
        return declaredWeight;
    }

    public void setDeclaredWeight(BigDecimal declaredWeight) {
        this.declaredWeight = declaredWeight;
    }

    public Boolean getDeclaredWeightEstimated() {
        return declaredWeightEstimated;
    }

    public void setDeclaredWeightEstimated(Boolean declaredWeightEstimated) {
        this.declaredWeightEstimated = declaredWeightEstimated;
    }

    public String getDeclaredWeightUom() {
        return declaredWeightUom;
    }

    public void setDeclaredWeightUom(String declaredWeightUom) {
        this.declaredWeightUom = declaredWeightUom;
    }

    public Date getDeliveryOrderReceived() {
        return deliveryOrderReceived;
    }

    public void setDeliveryOrderReceived(Date deliveryOrderReceived) {
        this.deliveryOrderReceived = deliveryOrderReceived;
    }

    public User getDeliveryOrderUserId() {
        return deliveryOrderUserId;
    }

    public void setDeliveryOrderUserId(User deliveryOrderUserId) {
        this.deliveryOrderUserId = deliveryOrderUserId;
    }

    public Warehouse getDestWhse() {
        return destWhse;
    }

    public void setDestWhse(Warehouse destWhse) {
        this.destWhse = destWhse;
    }

    public String getDoorDeliveryStatus() {
        return doorDeliveryStatus;
    }

    public void setDoorDeliveryStatus(String doorDeliveryStatus) {
        this.doorDeliveryStatus = doorDeliveryStatus;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public Boolean getExpressReleaseClause() {
        return expressReleaseClause;
    }

    public void setExpressReleaseClause(Boolean expressReleaseClause) {
        this.expressReleaseClause = expressReleaseClause;
    }

    public UnLocation getForeignPortOfDischarge() {
        return foreignPortOfDischarge;
    }

    public void setForeignPortOfDischarge(UnLocation foreignPortOfDischarge) {
        this.foreignPortOfDischarge = foreignPortOfDischarge;
    }

    public String getFreightReleaseBlNote() {
        return freightReleaseBlNote;
    }

    public void setFreightReleaseBlNote(String freightReleaseBlNote) {
        this.freightReleaseBlNote = freightReleaseBlNote;
    }

    public Date getFreightReleaseDateTime() {
        return freightReleaseDateTime;
    }

    public void setFreightReleaseDateTime(Date freightReleaseDateTime) {
        this.freightReleaseDateTime = freightReleaseDateTime;
    }

    public User getFreightReleaseUserId() {
        return freightReleaseUserId;
    }

    public void setFreightReleaseUserId(User freightReleaseUserId) {
        this.freightReleaseUserId = freightReleaseUserId;
    }

    public Date getGoDatetime() {
        return goDatetime;
    }

    public void setGoDatetime(Date goDatetime) {
        this.goDatetime = goDatetime;
    }

    public String getInbondVia() {
        return inbondVia;
    }

    public void setInbondVia(String inbondVia) {
        this.inbondVia = inbondVia;
    }

    public TradingPartner getIpiCfsAcctNo() {
        return ipiCfsAcctNo;
    }

    public void setIpiCfsAcctNo(TradingPartner ipiCfsAcctNo) {
        this.ipiCfsAcctNo = ipiCfsAcctNo;
    }

    public String getItClass() {
        return itClass;
    }

    public void setItClass(String itClass) {
        this.itClass = itClass;
    }

    public Date getItDatetime() {
        return itDatetime;
    }

    public void setItDatetime(Date itDatetime) {
        this.itDatetime = itDatetime;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public Date getOriginalBlReceived() {
        return originalBlReceived;
    }

    public void setOriginalBlReceived(Date originalBlReceived) {
        this.originalBlReceived = originalBlReceived;
    }

    public User getOriginalBlUserId() {
        return originalBlUserId;
    }

    public void setOriginalBlUserId(User originalBlUserId) {
        this.originalBlUserId = originalBlUserId;
    }

    public Date getPaymentReleaseReceived() {
        return paymentReleaseReceived;
    }

    public void setPaymentReleaseReceived(Date paymentReleaseReceived) {
        this.paymentReleaseReceived = paymentReleaseReceived;
    }

    public User getPaymentReleaseUserId() {
        return paymentReleaseUserId;
    }

    public void setPaymentReleaseUserId(User paymentReleaseUserId) {
        this.paymentReleaseUserId = paymentReleaseUserId;
    }

    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getPodSignedBy() {
        return podSignedBy;
    }

    public void setPodSignedBy(String podSignedBy) {
        this.podSignedBy = podSignedBy;
    }

    public Date getPodSignedDatetime() {
        return podSignedDatetime;
    }

    public void setPodSignedDatetime(Date podSignedDatetime) {
        this.podSignedDatetime = podSignedDatetime;
    }

    public Date getReleaseOrderReceived() {
        return releaseOrderReceived;
    }

    public void setReleaseOrderReceived(Date releaseOrderReceived) {
        this.releaseOrderReceived = releaseOrderReceived;
    }

    public User getReleaseOrderUserId() {
        return releaseOrderUserId;
    }

    public void setReleaseOrderUserId(User releaseOrderUserId) {
        this.releaseOrderUserId = releaseOrderUserId;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public boolean getTransShipment() {
        return transShipment;
    }

    public void setTransShipment(boolean transShipment) {
        this.transShipment = transShipment;
    }

    public UnLocation getUsaPortOfExit() {
        return usaPortOfExit;
    }

    public void setUsaPortOfExit(UnLocation usaPortOfExit) {
        this.usaPortOfExit = usaPortOfExit;
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

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getCustomsReleaseNo() {
        return customsReleaseNo;
    }

    public void setCustomsReleaseNo(String customsReleaseNo) {
        this.customsReleaseNo = customsReleaseNo;
    }

    public BigDecimal getCustomsReleaseValue() {
        return customsReleaseValue;
    }

    public void setCustomsReleaseValue(BigDecimal customsReleaseValue) {
        this.customsReleaseValue = customsReleaseValue;
    }

    public Date getFdEta() {
        return fdEta;
    }

    public void setFdEta(Date fdEta) {
        this.fdEta = fdEta;
    }

    public Date getLastFreeDateTime() {
        return lastFreeDateTime;
    }

    public void setLastFreeDateTime(Date lastFreeDateTime) {
        this.lastFreeDateTime = lastFreeDateTime;
    }

    public TradingPartner getExportAgentAcctNo() {
        return exportAgentAcctNo;
    }

    public void setExportAgentAcctNo(TradingPartner exportAgentAcctNo) {
        this.exportAgentAcctNo = exportAgentAcctNo;
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
        if (!(object instanceof LclQuoteImport)) {
            return false;
        }
        LclQuoteImport other = (LclQuoteImport) object;
        if ((this.fileNumberId == null && other.fileNumberId != null) || (this.fileNumberId != null && !this.fileNumberId.equals(other.fileNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclQuoteImport[fileNumberId=" + fileNumberId + "]";
    }
}
