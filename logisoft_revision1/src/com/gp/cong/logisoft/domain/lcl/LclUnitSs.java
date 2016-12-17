/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.logisoft.domain.TradingPartner;
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
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_unit_ss")
@DynamicInsert(true)
@DynamicUpdate(true)

public class LclUnitSs extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "cob")
    private boolean cob;
    @Column(name = "cob_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cobDatetime;
    @Column(name = "cob_remarks")
    private String cobRemarks;
    @JoinColumn(name = "cob_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = true)
    private User cobByUserId;
    @Basic(optional = false)
    @Column(name = "opt_docno_lg")
    private boolean optDocnoLg;
    @Column(name = "last_free_date")
    @Temporal(TemporalType.DATE)
    private Date lastFreeDate;
    @Basic(optional = false)
    @Column(name = "drayage_provided")
    private boolean drayageProvided;
    @Basic(optional = false)
    @Column(name = "intermodal_provided")
    private boolean intermodalProvided;
    @Basic(optional = false)
    @Column(name = "stop_off")
    private boolean stopOff;
    @Column(name = "hazmat_permitted")
    private Boolean hazmatPermitted;
    @Column(name = "refrigeration_permitted")
    private Boolean refrigerationPermitted;
    @Column(name = "edi")
    private Boolean edi;
    @Column(name = "chassis_no")
    private String chassisNo;
    @Column(name = "sp_reference_name")
    private String spReferenceName;
    @Column(name = "sp_booking_no")
    private String spBookingNo;
    @Basic(optional = true)
    @Column(name = "su_heading_note")
    private String SUHeadingNote;
    @Lob
    @Column(name = "trucking_remarks")
    private String truckingRemarks;
    @Column(name = "prepaid_collect")
    private String prepaidCollect = "P";
    @Column(name = "received_master")
    private String receivedMaster;
    @Lob
    @Column(name = "bl_body")
    private String blBody;
    @Column(name = "weight_imperial")
    private BigDecimal weightImperial;
    @Column(name = "weight_metric")
    private BigDecimal weightMetric;
    @Column(name = "volume_metric")
    private BigDecimal volumeMetric;
    @Column(name = "volume_imperial")
    private BigDecimal volumeImperial;
    @Column(name = "total_pieces")
    private Integer totalPieces;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Column(name = "completed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDatetime;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclUnit lclUnit;
    @JoinColumn(name = "sp_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner tradingPartner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnitSs")
    private List<LclBookingPieceUnit> lclBookingPieceUnitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnitSs")
    private List<LclSsAc> lclSsAcList;
    @Column(name = "solas_dunnage_weight_imperial")
    private BigDecimal solasDunnageWeightLBS;
    @Column(name = "solas_dunnage_weight_metric")
    private BigDecimal solasDunnageWeightKGS;
    @Column(name = "solas_tare_weight_imperial")
    private BigDecimal solasTareWeightLBS;
    @Column(name = "solas_tare_weight_metric")
    private BigDecimal solasTareWeightKGS;
    @Column(name = "solas_cargo_weight_imperial")
    private BigDecimal solasCargoWeightLBS;
    @Column(name = "solas_cargo_weight_metric")
    private BigDecimal solasCargoWeightKGS;
    @Column(name = "solas_verification_signature")
    private String solasVerificationSign;
    @Column(name = "solas_verification_date")
    private Date solasVerificationDate;
    @Column(name = "manifested_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date manifestedDateTime;

    @Transient
    private String displayStatus;
    @Transient
    private Integer scanAttachCount;
    @Transient
    private String dispoCode;
    @Transient
    private Boolean hazStatus;
    @Transient
    private Boolean inBondStatus;
    @Transient
    private boolean arInvoice;
    @Transient
    private String CFT;
    @Transient
    private String CBM;
    @Transient
    private String LBS;
    @Transient
    private String KGS;
    @Transient
    private BigInteger numDR;
    @Transient
    private String statusSendEdi;
    @Transient
    private String unitExceptionFlag;
    @Transient
    private Boolean scanAttachStatus;
    @Transient
    private Boolean isCheckedRates;
    @Transient
    private String dispoDesc;
    @Transient
    private String loadedByName;
    @Transient
    private User loadedByUserId;
    
    public LclUnitSs() {
    }

    public LclUnitSs(Long id) {
        this.id = id;
    }

    public LclUnitSs(Long id, String status, boolean cob, boolean optDocnoLg, boolean drayageProvided, boolean intermodalProvided, boolean stopOff, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.status = status;
        this.cob = cob;
        this.optDocnoLg = optDocnoLg;
        this.drayageProvided = drayageProvided;
        this.intermodalProvided = intermodalProvided;
        this.stopOff = stopOff;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getCob() {
        return cob;
    }

    public void setCob(boolean cob) {
        this.cob = cob;
    }

    public Date getCobDatetime() {
        return cobDatetime;
    }

    public void setCobDatetime(Date cobDatetime) {
        this.cobDatetime = cobDatetime;
    }

    public boolean getOptDocnoLg() {
        return optDocnoLg;
    }

    public void setOptDocnoLg(boolean optDocnoLg) {
        this.optDocnoLg = optDocnoLg;
    }

    public Date getLastFreeDate() {
        return lastFreeDate;
    }

    public void setLastFreeDate(Date lastFreeDate) {
        this.lastFreeDate = lastFreeDate;
    }

    public boolean getDrayageProvided() {
        return drayageProvided;
    }

    public void setDrayageProvided(boolean drayageProvided) {
        this.drayageProvided = drayageProvided;
    }

    public boolean getIntermodalProvided() {
        return intermodalProvided;
    }

    public void setIntermodalProvided(boolean intermodalProvided) {
        this.intermodalProvided = intermodalProvided;
    }

    public boolean getStopOff() {
        return stopOff;
    }

    public void setStopOff(boolean stopOff) {
        this.stopOff = stopOff;
    }

    public Boolean getHazmatPermitted() {
        return hazmatPermitted;
    }

    public void setHazmatPermitted(Boolean hazmatPermitted) {
        this.hazmatPermitted = hazmatPermitted;
    }

    public Boolean getRefrigerationPermitted() {
        return refrigerationPermitted;
    }

    public void setRefrigerationPermitted(Boolean refrigerationPermitted) {
        this.refrigerationPermitted = refrigerationPermitted;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
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

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public LclUnit getLclUnit() {
        return lclUnit;
    }

    public void setLclUnit(LclUnit lclUnit) {
        this.lclUnit = lclUnit;
    }

    public TradingPartner getTradingPartner() {
        return tradingPartner;
    }

    public void setTradingPartner(TradingPartner tradingPartner) {
        this.tradingPartner = tradingPartner;
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

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getScanAttachCount() {
        return scanAttachCount;
    }

    public void setScanAttachCount(Integer scanAttachCount) {
        this.scanAttachCount = scanAttachCount;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public Boolean getHazStatus() {
        return hazStatus;
    }

    public void setHazStatus(Boolean hazStatus) {
        this.hazStatus = hazStatus;
    }

    public Boolean getInBondStatus() {
        return inBondStatus;
    }

    public void setInBondStatus(Boolean inBondStatus) {
        this.inBondStatus = inBondStatus;
    }

    public String getCBM() {
        return CBM;
    }

    public void setCBM(String CBM) {
        this.CBM = CBM;
    }

    public String getCFT() {
        return CFT;
    }

    public void setCFT(String CFT) {
        this.CFT = CFT;
    }

    public String getKGS() {
        return KGS;
    }

    public void setKGS(String KGS) {
        this.KGS = KGS;
    }

    public String getLBS() {
        return LBS;
    }

    public void setLBS(String LBS) {
        this.LBS = LBS;
    }

    public BigInteger getNumDR() {
        return numDR;
    }

    public void setNumDR(BigInteger numDR) {
        this.numDR = numDR;
    }

    public List<LclBookingPieceUnit> getLclBookingPieceUnitList() {
        return lclBookingPieceUnitList;
    }

    public void setLclBookingPieceUnitList(List<LclBookingPieceUnit> lclBookingPieceUnitList) {
        this.lclBookingPieceUnitList = lclBookingPieceUnitList;
    }

    public List<LclSsAc> getLclSsAcList() {
        return lclSsAcList;
    }

    public void setLclSsAcList(List<LclSsAc> lclSsAcList) {
        this.lclSsAcList = lclSsAcList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getCobRemarks() {
        return cobRemarks;
    }

    public void setCobRemarks(String cobRemarks) {
        this.cobRemarks = cobRemarks;
    }

    public User getCobByUserId() {
        return cobByUserId;
    }

    public void setCobByUserId(User cobByUserId) {
        this.cobByUserId = cobByUserId;
    }

    public boolean isArInvoice() {
        return arInvoice;
    }

    public void setArInvoice(boolean arInvoice) {
        this.arInvoice = arInvoice;
    }

    public Boolean getEdi() {
        return edi;
    }

    public void setEdi(Boolean edi) {
        this.edi = edi;
    }

    public String getStatusSendEdi() {
        return statusSendEdi;
    }

    public void setStatusSendEdi(String statusSendEdi) {
        this.statusSendEdi = statusSendEdi;
    }

    public String getUnitExceptionFlag() {
        return unitExceptionFlag;
    }

    public void setUnitExceptionFlag(String unitExceptionFlag) {
        this.unitExceptionFlag = unitExceptionFlag;
    }

    public Boolean getScanAttachStatus() {
        return scanAttachStatus;
    }

    public void setScanAttachStatus(Boolean scanAttachStatus) {
        this.scanAttachStatus = scanAttachStatus;
    }

    public String getSUHeadingNote() {
        return SUHeadingNote;
    }

    public void setSUHeadingNote(String SUHeadingNote) {
        this.SUHeadingNote = SUHeadingNote;
    }

    public String getTruckingRemarks() {
        return truckingRemarks;
    }

    public void setTruckingRemarks(String truckingRemarks) {
        this.truckingRemarks = truckingRemarks;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public Boolean getIsCheckedRates() {
        return isCheckedRates;
    }

    public void setIsCheckedRates(Boolean isCheckedRates) {
        this.isCheckedRates = isCheckedRates;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getReceivedMaster() {
        return receivedMaster;
    }

    public void setReceivedMaster(String receivedMaster) {
        this.receivedMaster = receivedMaster;
    }

    public String getBlBody() {
        return blBody;
    }

    public void setBlBody(String blBody) {
        this.blBody = blBody;
    }

    public BigDecimal getWeightImperial() {
        return weightImperial;
    }

    public void setWeightImperial(BigDecimal weightImperial) {
        this.weightImperial = weightImperial;
    }

    public BigDecimal getWeightMetric() {
        return weightMetric;
    }

    public void setWeightMetric(BigDecimal weightMetric) {
        this.weightMetric = weightMetric;
    }

    public BigDecimal getVolumeMetric() {
        return volumeMetric;
    }

    public void setVolumeMetric(BigDecimal volumeMetric) {
        this.volumeMetric = volumeMetric;
    }

    public BigDecimal getVolumeImperial() {
        return volumeImperial;
    }

    public void setVolumeImperial(BigDecimal volumeImperial) {
        this.volumeImperial = volumeImperial;
    }

    public Integer getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Integer totalPieces) {
        this.totalPieces = totalPieces;
    }

    public BigDecimal getSolasDunnageWeightLBS() {
        return solasDunnageWeightLBS;
    }

    public void setSolasDunnageWeightLBS(BigDecimal solasDunnageWeightLBS) {
        this.solasDunnageWeightLBS = solasDunnageWeightLBS;
    }

    public BigDecimal getSolasDunnageWeightKGS() {
        return solasDunnageWeightKGS;
    }

    public void setSolasDunnageWeightKGS(BigDecimal solasDunnageWeightKGS) {
        this.solasDunnageWeightKGS = solasDunnageWeightKGS;
    }

    public BigDecimal getSolasTareWeightLBS() {
        return solasTareWeightLBS;
    }

    public void setSolasTareWeightLBS(BigDecimal solasTareWeightLBS) {
        this.solasTareWeightLBS = solasTareWeightLBS;
    }

    public BigDecimal getSolasTareWeightKGS() {
        return solasTareWeightKGS;
    }

    public void setSolasTareWeightKGS(BigDecimal solasTareWeightKGS) {
        this.solasTareWeightKGS = solasTareWeightKGS;
    }

    public BigDecimal getSolasCargoWeightLBS() {
        return solasCargoWeightLBS;
    }

    public void setSolasCargoWeightLBS(BigDecimal solasCargoWeightLBS) {
        this.solasCargoWeightLBS = solasCargoWeightLBS;
    }

    public BigDecimal getSolasCargoWeightKGS() {
        return solasCargoWeightKGS;
    }

    public void setSolasCargoWeightKGS(BigDecimal solasCargoWeightKGS) {
        this.solasCargoWeightKGS = solasCargoWeightKGS;
    }

    public String getSolasVerificationSign() {
        return solasVerificationSign;
    }

    public void setSolasVerificationSign(String solasVerificationSign) {
        this.solasVerificationSign = solasVerificationSign;
    }

    public Date getSolasVerificationDate() {
        return solasVerificationDate;
    }

    public void setSolasVerificationDate(Date solasVerificationDate) {
        this.solasVerificationDate = solasVerificationDate;
    }

    public String getLoadedByName() {
        return loadedByName;
    }

    public void setLoadedByName(String loadedByName) {
        this.loadedByName = loadedByName;
    }
    
    public Date getCompletedDatetime() {
        return completedDatetime;
    }

    public void setCompletedDatetime(Date completedDatetime) {
        this.completedDatetime = completedDatetime;
    }

    public User getLoadedByUserId() {
        return loadedByUserId;
    }

    public void setLoadedByUserId(User loadedByUserId) {
        this.loadedByUserId = loadedByUserId;
    }

    public Date getManifestedDateTime() {
        return manifestedDateTime;
    }

    public void setManifestedDateTime(Date manifestedDateTime) {
        this.manifestedDateTime = manifestedDateTime;
    }
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclUnitSs)) {
            return false;
        }
        LclUnitSs other = (LclUnitSs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitSs[id=" + id + "]";
    }
}
