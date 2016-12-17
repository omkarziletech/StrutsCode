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
 * @author lakshh
 */
@Entity
@Table(name = "lcl_booking_import")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclBookingImport extends Domain {

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
    @Column(name = "door_delivery_eta")
     @Temporal(TemporalType.TIMESTAMP)
    private Date doorDeliveryEta;
    @Column(name = "pod_signed_by")
    private String podSignedBy;
    @Column(name = "pod_signed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date podSignedDatetime;
    @Column(name = "last_free_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFreeDateTime;
    @Column(name = "customs_entry_class")
    private String entryClass;
    @Column(name = "customs_release_no")
    private String customsReleaseNo;
    @Column(name = "customs_release_value")
    private BigDecimal customsReleaseValue;
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
    @Column(name = "door_delivery_comment")
    private Boolean doorDeliveryComment;
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
    @Column(name = "cargo_on_hold")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cargoOnHold;
    @Column(name = "cargo_general_order")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cargoGeneralOrder;
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
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Column(name = "elite_ipi_loaded")
    private boolean ipiLoadedContainer;
    @Column(name = "ipi_atd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ipiATDDate;
    @Column(name="ipi_load_no")
    private String ipiLoadNo;
    @Column(name="picked_up_by")
    private String pickedUpBy;
    @Column(name = "customs_entry_no_prtloc")
    private String printEntry7512;
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
    @JoinColumn(name = "export_agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner exportAgentAcctNo;
    @JoinColumn(name = "usa_port_of_exit_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation usaPortOfExit;
    @Column(name = "fd_eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdEta;
    @JoinColumn(name = "foreign_port_of_discharge_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation foreignPortOfDischarge;
    @JoinColumn(name = "customs_clearance_received_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User customsClearanceUserId;
    @JoinColumn(name = "delivery_order_received_user_id", referencedColumnName = "user_id")
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
    @JoinColumn(name = "cargo_hold_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User cargoHoldUserId;
    @JoinColumn(name = "cargo_order_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User cargoOrderUserId;

    public LclBookingImport() {
    }

    public LclBookingImport(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclBookingImport(Long fileNumberId, Date enteredDatetime, Date modifiedDatetime) {
        this.fileNumberId = fileNumberId;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public boolean getTransShipment() {
        return transShipment;
    }

    public void setTransShipment(boolean transShipment) {
        this.transShipment = transShipment;
    }

    public String getDoorDeliveryStatus() {
        return doorDeliveryStatus;
    }

    public void setDoorDeliveryStatus(String doorDeliveryStatus) {
        this.doorDeliveryStatus = doorDeliveryStatus;
    }

    public Date getDoorDeliveryEta() {
        return doorDeliveryEta;
    }

    public void setDoorDeliveryEta(Date doorDeliveryEta) {
        this.doorDeliveryEta = doorDeliveryEta;
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

    public String getItClass() {
        return itClass;
    }

    public void setItClass(String itClass) {
        this.itClass = itClass;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public Date getItDatetime() {
        return itDatetime;
    }

    public void setItDatetime(Date itDatetime) {
        this.itDatetime = itDatetime;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
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

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
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

    public Boolean getDeclaredWeightEstimated() {
        return declaredWeightEstimated;
    }

    public void setDeclaredWeightEstimated(Boolean declaredWeightEstimated) {
        this.declaredWeightEstimated = declaredWeightEstimated;
    }

    public Boolean getExpressReleaseClause() {
        return expressReleaseClause;
    }

    public void setExpressReleaseClause(Boolean expressReleaseClause) {
        this.expressReleaseClause = expressReleaseClause;
    }

    public Boolean getDoorDeliveryComment() {
        return doorDeliveryComment;
    }

    public void setDoorDeliveryComment(Boolean doorDeliveryComment) {
        this.doorDeliveryComment = doorDeliveryComment;
    }

    public Date getGoDatetime() {
        return goDatetime;
    }

    public void setGoDatetime(Date goDatetime) {
        this.goDatetime = goDatetime;
    }

    @Override
    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    @Override
    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    @Override
    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    @Override
    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    @Override
    public User getEnteredBy() {
        return enteredBy;
    }

    @Override
    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    @Override
    public User getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Warehouse getDestWhse() {
        return destWhse;
    }

    public void setDestWhse(Warehouse destWhse) {
        this.destWhse = destWhse;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public TradingPartner getIpiCfsAcctNo() {
        return ipiCfsAcctNo;
    }

    public void setIpiCfsAcctNo(TradingPartner ipiCfsAcctNo) {
        this.ipiCfsAcctNo = ipiCfsAcctNo;
    }

    public String getInbondVia() {
        return inbondVia;
    }

    public void setInbondVia(String inbondVia) {
        this.inbondVia = inbondVia;
    }

    public Date getFdEta() {
        return fdEta;
    }

    public void setFdEta(Date fdEta) {
        this.fdEta = fdEta;
    }

    public UnLocation getForeignPortOfDischarge() {
        return foreignPortOfDischarge;
    }

    public void setForeignPortOfDischarge(UnLocation foreignPortOfDischarge) {
        this.foreignPortOfDischarge = foreignPortOfDischarge;
    }

    public UnLocation getUsaPortOfExit() {
        return usaPortOfExit;
    }

    public void setUsaPortOfExit(UnLocation usaPortOfExit) {
        this.usaPortOfExit = usaPortOfExit;
    }

    public Date getCustomsClearanceReceived() {
        return customsClearanceReceived;
    }

    public void setCustomsClearanceReceived(Date customsClearanceReceived) {
        this.customsClearanceReceived = customsClearanceReceived;
    }

    public BigDecimal getDeclaredWeight() {
        return declaredWeight;
    }

    public void setDeclaredWeight(BigDecimal declaredWeight) {
        this.declaredWeight = declaredWeight;
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

    public Date getOriginalBlReceived() {
        return originalBlReceived;
    }

    public void setOriginalBlReceived(Date originalBlReceived) {
        this.originalBlReceived = originalBlReceived;
    }

    public Date getReleaseOrderReceived() {
        return releaseOrderReceived;
    }

    public void setReleaseOrderReceived(Date releaseOrderReceived) {
        this.releaseOrderReceived = releaseOrderReceived;
    }

    public Date getPaymentReleaseReceived() {
        return paymentReleaseReceived;
    }

    public void setPaymentReleaseReceived(Date paymentReleaseReceived) {
        this.paymentReleaseReceived = paymentReleaseReceived;
    }

    public User getReleaseOrderUserId() {
        return releaseOrderUserId;
    }

    public void setReleaseOrderUserId(User releaseOrderUserId) {
        this.releaseOrderUserId = releaseOrderUserId;
    }

    public User getOriginalBlUserId() {
        return originalBlUserId;
    }

    public void setOriginalBlUserId(User originalBlUserId) {
        this.originalBlUserId = originalBlUserId;
    }

    public User getCustomsClearanceUserId() {
        return customsClearanceUserId;
    }

    public void setCustomsClearanceUserId(User customsClearanceUserId) {
        this.customsClearanceUserId = customsClearanceUserId;
    }

    public User getPaymentReleaseUserId() {
        return paymentReleaseUserId;
    }

    public void setPaymentReleaseUserId(User paymentReleaseUserId) {
        this.paymentReleaseUserId = paymentReleaseUserId;
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

    public Date getCargoGeneralOrder() {
        return cargoGeneralOrder;
    }

    public void setCargoGeneralOrder(Date cargoGeneralOrder) {
        this.cargoGeneralOrder = cargoGeneralOrder;
    }

    public User getCargoHoldUserId() {
        return cargoHoldUserId;
    }

    public void setCargoHoldUserId(User cargoHoldUserId) {
        this.cargoHoldUserId = cargoHoldUserId;
    }

    public Date getCargoOnHold() {
        return cargoOnHold;
    }

    public void setCargoOnHold(Date cargoOnHold) {
        this.cargoOnHold = cargoOnHold;
    }

    public User getCargoOrderUserId() {
        return cargoOrderUserId;
    }

    public void setCargoOrderUserId(User cargoOrderUserId) {
        this.cargoOrderUserId = cargoOrderUserId;
    }

    public boolean isIpiLoadedContainer() {
        return ipiLoadedContainer;
    }

    public void setIpiLoadedContainer(boolean ipiLoadedContainer) {
        this.ipiLoadedContainer = ipiLoadedContainer;
    }

    public Date getIpiATDDate() {
        return ipiATDDate;
    }

    public void setIpiATDDate(Date ipiATDDate) {
        this.ipiATDDate = ipiATDDate;
    }

    public String getIpiLoadNo() {
        return ipiLoadNo;
    }

    public void setIpiLoadNo(String ipiLoadNo) {
        this.ipiLoadNo = ipiLoadNo;
    }

    public String getPickedUpBy() {
        return pickedUpBy;
    }

    public void setPickedUpBy(String pickedUpBy) {
        this.pickedUpBy = pickedUpBy;
    }

    public String getPrintEntry7512() {
        return printEntry7512;
    }

    public void setPrintEntry7512(String printEntry7512) {
        this.printEntry7512 = printEntry7512;
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
        if (!(object instanceof LclBookingImport)) {
            return false;
        }
        LclBookingImport other = (LclBookingImport) object;
        if ((this.fileNumberId == null && other.fileNumberId != null) || (this.fileNumberId != null && !this.fileNumberId.equals(other.fileNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingImport[fileNumberId=" + fileNumberId + "]";
    }
}
