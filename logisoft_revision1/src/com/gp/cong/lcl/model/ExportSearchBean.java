/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import java.io.Serializable;

/**
 *
 * @author aravindhan.v
 */
public class ExportSearchBean implements LclCommonConstant, Serializable {

    private Integer fileNumberId;
    private String fileState;
    private String quoteComplete;
    private String fileNumber;
    private String fileStatus;
    private Integer clientPwk;
    private String bookingType;
    private String scheduleNo;
    private String sailDate;
    private String polLrd;
    private String pooLrd;
    private String transPolCode;
    private String transPolName;
    private String transPolState;
    private String transPodCode;
    private String transPodName;
    private String transPodCountry;
    private String originUncode;
    private String origin;
    private String polUncode;
    private String pol;
    private String podUncode;
    private String pod;
    private String destinationUncode;
    private String destination;
    private String relayOverride;
    private String shipName;
    private String shipNo;
    private String shipAddress;
    private String shipCity;
    private String shipState;
    private String shipZip;
    private String consName;
    private String consNo;
    private String consAddress;
    private String consCity;
    private String consState;
    private String consZip;
    private String fwdName;
    private String fwdNo;
    private String fwdAddress;
    private String fwdCity;
    private String fwdState;
    private String fwdZip;
    private String billingTerminal;
    private String pooPickup;
    private String pickupCity;
    private String bookedBy;
    private String quoteBy;
    private String creditS;
    private String creditF;
    private String creditT;
    private String billToParty;
    private String currentLocCode;
    private String currentLocName;
    private String createdDatetime;
    private String clientName;
    private String inbondNo;
    private Integer consolidatedFiles;
    private String relatedConoslidted;
    private String cfcl;
    private String hold;
    private String cfclAcct;
    private String sslName;
    private String agentAcct;
    private String totalPiece;
    private String totalWeightImperial;
    private String totalVolumeImperial;
    private String totalVolumeMetric;
    private String totalWeightMetric;
    private String bookedWeight;
    private String actualWeight;
    private String bookedVolume;
    private String actualVolume;
    private String bookedWeightMetric;
    private String actualWeightMetric;
    private String actualVolumeMetric;
    private String bookedVolumeMetric;
    private String totalBookedPiece;
    private String totalActualPiece;
    private Boolean hazmat;
    private String hazmatInfo;
    private String pickedUpDateTime;
    private String etaDate;
    private String dispoCode;
    private String dispoDesc;
    private String disp;
    private String postedUser;
    private Integer transshipment;
    private Integer shortShip;
    private String pieceUnit;
    private String dataSource;
    private String hotCodes;
    private String hotCodeKey;
    private String hotCodeCount;
    private String loadingRemarks;
    private String aesStatus;
    private Integer sedCount;
    private String voyageServiceType;
    private Integer shortShipSequence;
    private String currentFileStatus;
    private String correctionIds;
    private String lineLocation;
    private String pieceDetailLocation;
    private String lineLocationToolTip;
    private String originalFileStatus;
    private boolean drReleaseFlag;
    private boolean shipmentHoldFlag;
    private String cargoRecDate;
    private boolean priorityNotes;
    private String priorityNoteValues;
    private String ert;
    private String OSD;
    private String inlandETA;
    private String osdRemarks;

    public Integer getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Integer fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getQuoteComplete() {
        return quoteComplete;
    }

    public void setQuoteComplete(String quoteComplete) {
        this.quoteComplete = quoteComplete;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Integer getClientPwk() {
        return clientPwk;
    }

    public void setClientPwk(Integer clientPwk) {
        this.clientPwk = clientPwk;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getPolLrd() {
        return polLrd;
    }

    public void setPolLrd(String polLrd) {
        this.polLrd = polLrd;
    }

    public String getPooLrd() {
        return pooLrd;
    }

    public void setPooLrd(String pooLrd) {
        this.pooLrd = pooLrd;
    }

    public String getTransPolCode() {
        return transPolCode;
    }

    public void setTransPolCode(String transPolCode) {
        this.transPolCode = transPolCode;
    }

    public String getTransPolName() {
        return transPolName;
    }

    public void setTransPolName(String transPolName) {
        this.transPolName = transPolName;
    }

    public String getTransPolState() {
        return transPolState;
    }

    public void setTransPolState(String transPolState) {
        this.transPolState = transPolState;
    }

    public String getTransPodCode() {
        return transPodCode;
    }

    public void setTransPodCode(String transPodCode) {
        this.transPodCode = transPodCode;
    }

    public String getTransPodName() {
        return transPodName;
    }

    public void setTransPodName(String transPodName) {
        this.transPodName = transPodName;
    }

    public String getTransPodCountry() {
        return transPodCountry;
    }

    public void setTransPodCountry(String transPodCountry) {
        this.transPodCountry = transPodCountry;
    }

    public String getOriginUncode() {
        return originUncode;
    }

    public void setOriginUncode(String originUncode) {
        this.originUncode = originUncode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPolUncode() {
        return polUncode;
    }

    public void setPolUncode(String polUncode) {
        this.polUncode = polUncode;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPodUncode() {
        return podUncode;
    }

    public void setPodUncode(String podUncode) {
        this.podUncode = podUncode;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getDestinationUncode() {
        return destinationUncode;
    }

    public void setDestinationUncode(String destinationUncode) {
        this.destinationUncode = destinationUncode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRelayOverride() {
        return relayOverride;
    }

    public void setRelayOverride(String relayOverride) {
        this.relayOverride = relayOverride;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public String getShipZip() {
        return shipZip;
    }

    public void setShipZip(String shipZip) {
        this.shipZip = shipZip;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getConsNo() {
        return consNo;
    }

    public void setConsNo(String consNo) {
        this.consNo = consNo;
    }

    public String getConsAddress() {
        return consAddress;
    }

    public void setConsAddress(String consAddress) {
        this.consAddress = consAddress;
    }

    public String getConsCity() {
        return consCity;
    }

    public void setConsCity(String consCity) {
        this.consCity = consCity;
    }

    public String getConsState() {
        return consState;
    }

    public void setConsState(String consState) {
        this.consState = consState;
    }

    public String getConsZip() {
        return consZip;
    }

    public void setConsZip(String consZip) {
        this.consZip = consZip;
    }

    public String getFwdName() {
        return fwdName;
    }

    public void setFwdName(String fwdName) {
        this.fwdName = fwdName;
    }

    public String getFwdNo() {
        return fwdNo;
    }

    public void setFwdNo(String fwdNo) {
        this.fwdNo = fwdNo;
    }

    public String getFwdAddress() {
        return fwdAddress;
    }

    public void setFwdAddress(String fwdAddress) {
        this.fwdAddress = fwdAddress;
    }

    public String getFwdCity() {
        return fwdCity;
    }

    public void setFwdCity(String fwdCity) {
        this.fwdCity = fwdCity;
    }

    public String getFwdState() {
        return fwdState;
    }

    public void setFwdState(String fwdState) {
        this.fwdState = fwdState;
    }

    public String getFwdZip() {
        return fwdZip;
    }

    public void setFwdZip(String fwdZip) {
        this.fwdZip = fwdZip;
    }

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public String getPooPickup() {
        return pooPickup;
    }

    public void setPooPickup(String pooPickup) {
        this.pooPickup = pooPickup;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getQuoteBy() {
        return quoteBy;
    }

    public void setQuoteBy(String quoteBy) {
        this.quoteBy = quoteBy;
    }

    public String getCreditS() {
        return creditS;
    }

    public void setCreditS(String creditS) {
        this.creditS = creditS;
    }

    public String getCreditF() {
        return creditF;
    }

    public void setCreditF(String creditF) {
        this.creditF = creditF;
    }

    public String getCreditT() {
        return creditT;
    }

    public void setCreditT(String creditT) {
        this.creditT = creditT;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getCurrentLocCode() {
        return currentLocCode;
    }

    public void setCurrentLocCode(String currentLocCode) {
        this.currentLocCode = currentLocCode;
    }

    public String getCurrentLocName() {
        return currentLocName;
    }

    public void setCurrentLocName(String currentLocName) {
        this.currentLocName = currentLocName;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getInbondNo() {
        return inbondNo;
    }

    public void setInbondNo(String inbondNo) {
        this.inbondNo = inbondNo;
    }

    public Integer getConsolidatedFiles() {
        return consolidatedFiles;
    }

    public void setConsolidatedFiles(Integer consolidatedFiles) {
        this.consolidatedFiles = consolidatedFiles;
    }

    public String getRelatedConoslidted() {
        return relatedConoslidted;
    }

    public void setRelatedConoslidted(String relatedConoslidted) {
        this.relatedConoslidted = relatedConoslidted;
    }

    public String getCfcl() {
        return cfcl;
    }

    public void setCfcl(String cfcl) {
        this.cfcl = cfcl;
    }

    public String getCfclAcct() {
        return cfclAcct;
    }

    public void setCfclAcct(String cfclAcct) {
        this.cfclAcct = cfclAcct;
    }

    public String getSslName() {
        return sslName;
    }

    public void setSslName(String sslName) {
        this.sslName = sslName;
    }

    public String getAgentAcct() {
        return agentAcct;
    }

    public void setAgentAcct(String agentAcct) {
        this.agentAcct = agentAcct;
    }

    public String getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(String totalPiece) {
        this.totalPiece = totalPiece;
    }

    public String getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(String totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public String getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(String totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public String getTotalVolumeMetric() {
        return totalVolumeMetric;
    }

    public void setTotalVolumeMetric(String totalVolumeMetric) {
        this.totalVolumeMetric = totalVolumeMetric;
    }

    public String getBookedWeight() {
        return bookedWeight;
    }

    public void setBookedWeight(String bookedWeight) {
        this.bookedWeight = bookedWeight;
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getBookedVolume() {
        return bookedVolume;
    }

    public void setBookedVolume(String bookedVolume) {
        this.bookedVolume = bookedVolume;
    }

    public String getActualVolume() {
        return actualVolume;
    }

    public void setActualVolume(String actualVolume) {
        this.actualVolume = actualVolume;
    }

    public String getBookedWeightMetric() {
        return bookedWeightMetric;
    }

    public void setBookedWeightMetric(String bookedWeightMetric) {
        this.bookedWeightMetric = bookedWeightMetric;
    }

    public String getActualWeightMetric() {
        return actualWeightMetric;
    }

    public void setActualWeightMetric(String actualWeightMetric) {
        this.actualWeightMetric = actualWeightMetric;
    }

    public String getActualVolumeMetric() {
        return actualVolumeMetric;
    }

    public void setActualVolumeMetric(String actualVolumeMetric) {
        this.actualVolumeMetric = actualVolumeMetric;
    }

    public String getBookedVolumeMetric() {
        return bookedVolumeMetric;
    }

    public void setBookedVolumeMetric(String bookedVolumeMetric) {
        this.bookedVolumeMetric = bookedVolumeMetric;
    }

    public String getTotalBookedPiece() {
        return totalBookedPiece;
    }

    public void setTotalBookedPiece(String totalBookedPiece) {
        this.totalBookedPiece = totalBookedPiece;
    }

    public String getTotalActualPiece() {
        return totalActualPiece;
    }

    public void setTotalActualPiece(String totalActualPiece) {
        this.totalActualPiece = totalActualPiece;
    }

    public Boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(Boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getPickedUpDateTime() {
        return pickedUpDateTime;
    }

    public void setPickedUpDateTime(String pickedUpDateTime) {
        this.pickedUpDateTime = pickedUpDateTime;
    }

    public String getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(String etaDate) {
        this.etaDate = etaDate;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(String disp) {
        this.disp = disp;
    }

    public String getPostedUser() {
        return postedUser;
    }

    public void setPostedUser(String postedUser) {
        this.postedUser = postedUser;
    }

    public Integer getTransshipment() {
        return transshipment;
    }

    public void setTransshipment(Integer transshipment) {
        this.transshipment = transshipment;
    }

    public Integer getShortShip() {
        return shortShip;
    }

    public void setShortShip(Integer shortShip) {
        this.shortShip = shortShip;
    }

    public String getPieceUnit() {
        return pieceUnit;
    }

    public void setPieceUnit(String pieceUnit) {
        this.pieceUnit = pieceUnit;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public String getHotCodeCount() {
        return hotCodeCount;
    }

    public void setHotCodeCount(String hotCodeCount) {
        this.hotCodeCount = hotCodeCount;
    }

    public String getHotCodeKey() {
        return hotCodeKey;
    }

    public void setHotCodeKey(String hotCodeKey) {
        if (!"".equalsIgnoreCase(hotCodeKey)) {
            String[] value = hotCodeKey.split(",");
            if (value.length >= 3) {
                this.hotCodeKey = value[0] + "/" + value[1] + "/" + value[2];
            } else {
                this.hotCodeKey = hotCodeKey.replace(",", "/");
            }
        } else {
            this.hotCodeKey = hotCodeKey;
        }
    }

    public String getAesStatus() {
        return aesStatus;
    }

    public void setAesStatus(String aesStatus) {
        this.aesStatus = aesStatus;
    }

    public Integer getSedCount() {
        return sedCount;
    }

    public void setSedCount(Integer sedCount) {
        this.sedCount = sedCount;
    }

    public String getTotalWeightMetric() {
        return totalWeightMetric;
    }

    public void setTotalWeightMetric(String totalWeightMetric) {
        this.totalWeightMetric = totalWeightMetric;
    }

    public String getVoyageServiceType() {
        return voyageServiceType;
    }

    public void setVoyageServiceType(String voyageServiceType) {
        this.voyageServiceType = voyageServiceType;
    }

    public Integer getShortShipSequence() {
        return shortShipSequence;
    }

    public void setShortShipSequence(Integer shortShipSequence) {
        this.shortShipSequence = shortShipSequence;
    }

    public String getCurrentFileStatus() {
        return currentFileStatus;
    }

    public void setCurrentFileStatus(String currentFileStatus) {
        this.currentFileStatus = currentFileStatus;
    }

    public String getLoadingRemarks() {
        return loadingRemarks;
    }

    public void setLoadingRemarks(String loadingRemarks) {
        this.loadingRemarks = loadingRemarks;
    }

    public String getCorrectionIds() {
        return correctionIds;
    }

    public void setCorrectionIds(String correctionIds) {
        this.correctionIds = correctionIds;
    }

    public String getHazmatInfo() {
        return hazmatInfo;
    }

    public void setHazmatInfo(String hazmatInfo) {
        this.hazmatInfo = hazmatInfo;
    }

    public String getPieceDetailLocation() {
        return pieceDetailLocation;
    }

    public void setPieceDetailLocation(String pieceDetailLocation) {
        this.pieceDetailLocation = pieceDetailLocation;
    }

    public String getLineLocation() {
        return lineLocation;
    }

    public String getLineLocationToolTip() {
        return lineLocationToolTip;
    }

    public void setLineLocationToolTip(String lineLocationToolTip) {
        this.lineLocationToolTip = lineLocationToolTip;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public void setLineLocation(String lineLocation) {
        lineLocation = null != lineLocation ? lineLocation + "," : "";
        lineLocation += null != this.pieceDetailLocation ? this.pieceDetailLocation + "," : "";
        if (CommonUtils.isNotEmpty(lineLocation)) {
            String[] value = lineLocation.split(",");
            if (value.length >= 3) {
                this.lineLocation = value[0] + "," + value[1] + "," + value[2];
            } else {
                this.lineLocation = lineLocation.substring(0, lineLocation.length() - 1);
            }
            this.setLineLocationToolTip(lineLocation);
        } else {
            this.lineLocation = lineLocation;
        }
    }

    public String getOriginalFileStatus() {
        return originalFileStatus;
    }

    public void setOriginalFileStatus(String originalFileStatus) {
        this.originalFileStatus = originalFileStatus;
    }

    public boolean isDrReleaseFlag() {
        return drReleaseFlag;
    }

    public void setDrReleaseFlag(boolean drReleaseFlag) {
        this.drReleaseFlag = drReleaseFlag;
    }

    public boolean isShipmentHoldFlag() {
        return shipmentHoldFlag;
    }

    public void setShipmentHoldFlag(boolean shipmentHoldFlag) {
        this.shipmentHoldFlag = shipmentHoldFlag;
    }

    public String getCargoRecDate() {
        return cargoRecDate;
    }

    public void setCargoRecDate(String cargoRecDate) {
        this.cargoRecDate = cargoRecDate;
    }

    public boolean isPriorityNotes() {
        return priorityNotes;
    }

    public void setPriorityNotes(boolean priorityNotes) {
        this.priorityNotes = priorityNotes;
    }

    public String getPriorityNoteValues() {
        return priorityNoteValues;
    }

    public void setPriorityNoteValues(String priorityNoteValues) {
        this.priorityNoteValues = priorityNoteValues;
    }

    public String getErt() {
        return ert;
    }

    public void setErt(String ert) {
        this.ert = ert;
    }

    public String getOSD() {
        return OSD;
    }

    public void setOSD(String OSD) {
        this.OSD = OSD;
    }

    public String getInlandETA() {
        return inlandETA;
    }

    public void setInlandETA(String inlandETA) {
        this.inlandETA = inlandETA;
    }

    public String getOsdRemarks() {
        return osdRemarks;
    }

    public void setOsdRemarks(String osdRemarks) {
        this.osdRemarks = osdRemarks;
    }
    
}
