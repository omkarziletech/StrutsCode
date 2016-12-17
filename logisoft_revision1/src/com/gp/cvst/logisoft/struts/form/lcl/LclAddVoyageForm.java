package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import java.util.List;

public class LclAddVoyageForm extends LogiwareActionForm {

    private String pooOrigin;
    private String pol;
    //  private String searchPolName;
    private String pod;
    //private String searchPodName;
    private String podDestination;
    private String scheduleNo;
    private String transMode;
    private String accountNumber;
    private String spReferenceNo;
    private String spReferenceName;
    private String loadLrdt;
    private String hazmatLoadingDeadline;
    private String generalLoadingDeadline;
    private String etaPod;
    private String sailDate;
    private Integer originId;
    private Integer polId;
    private Integer podId;
    private Integer finalDestinationId;
    private Integer originalOriginId;
    private Integer originalDestinationId;
    private String originalOriginName;
    private String originalDestinationName;
    private String methodName;
    private String accountName;
    private String polCode;
    private String podCode;
    private Integer arrivalId;
    private Integer departureId;
    private String arrivalPier;
    private String departurePier;
    private String hazmatPermitted;
    private String hazmatLrdt;
    private String refrigerationPermitted;
    private String hiddenRefrigerationPermitted;
    private String remarks;
    private String closedBy;
    private Integer closedUserId;
    private String closedDate;
    private String closedRemarks;
    private Integer reopenedUserId;
    private String reopenedDate;
    private String reopenedRemarks;
    private Integer auditedUserId;
    private String auditedBy;
    private String auditedDate;
    private String auditedRemarks;
    private Integer ttOverrideDays;
    private Integer lrdOverrideDays;
    private String agentBrandName;
    private String bookingNumber;
    private Long headerId;
    private Long detailId;
    private Long masterId;
    private Integer itemId;
    private String std;
    private String message;
    private String status;
    private LclSsHeader lclssheader;
    private String unitNo;
    private String SUHeadingNote;
    private Long unitId;
    private Long unitssId;
    private Integer index;
    private Long unitType;
    private String hazmatPermittedUnit;
    private String refrigerationPermittedUnit;
    private String openPopup;
    private String path;
    private String destuffedFileNumbers;
    private String stuffedFileNumbers;
    private String filterByChanges;
    private String co_tod;
    private String openLCLUnit;
    private Integer polPodTT;
    private Integer co_dbd;
    private String unitsReopened;
    private String contractNumber;
    private String prepaidCollect;
    private String destPrepaidCollect;
    private String shipperAccountNumber;
    private String shipperAccountNo;
    private String shipperEdi;
    private String consigneeAccountNumber;
    private String consigneeAccountNo;
    private String consigneeEdi;
    private String notifyAccountNumber;
    private String notifyAccountNo;
    private String manualNotyName;
    private String notifyEdi;
    private String exportReferenceEdi;
    private String blbody;
    private String newShipper;
    private String newConsignee;
    private String newNotify;
    private String displayLoadComplete;
    private String documentID;
    private String billTerminal;
    private String voyageTerminal;
    private String billTerminalNo;
    private String searchLoadDisplay;
    private String polEtd;
    private String moveType;
    private String unitVoyageSearch;
    private String docReceived;
    //Import Units
    private String itDatetime;
    private String itPort;
    private Integer itPortId;
    private String itNo;
    private Integer cfsWarehouseId;
    private String cfsWarehouse;
    private String unitWarehouse;
    private Integer unitsWarehouseId;
    private String originAcct;
    private String coloaderAcct;
    private String lastFreeDate;
    private String goDate;
    private String originAcctNo;
    private String coloaderAcctNo;
    private String polUnlocationCode;
    private String fdUnlocationCode;
    private String bookScheduleNo;
    private String approxDueDate;
    private String masterBL;
    private String unmanifestLCLUnit;
    private String fileNumber;
    private String statusMessage;
    private String className;
    private String billsTerminal;
    private String billsTerminalNo;
    private String strippedDate;
    private String sealNoIn;
    private String sealNoOut;
    private String schedule;
    private String voyageNo;
    private String loginName;
    private String loginId;
    private String unitException;
    private String exceptionFileNumbers;
    private String changeVoyageNo;
    private Long changeVoyHeaderId;
    private String cobRemarks;
    private String verifiedEta;
    private String ssDetailsId;
    private String eta;
    private String etd;
    private String vessel;
    private String ssVoyage;
    private String cob;
    private String drayageProvided;
    private String intermodalProvided;
    private String stopoff;
    private String chasisNo;
    private String receivedMaster;
    private String sealNo;
    private String releaseClause;
    private String clauseDescription;
    private String unitAutoCostFlag;
    private String voyenteredById;//voyage owner
    private String voyageOwnerFlag;//voyage owner flag
    private String voyageOwner;
    private Long oldUnitId;
    private String oldUnitNo;
    private String fileNumberId;
    private String postFlag;
    private String buttonValue;
    private String destuffedByUserId;
    private String stuffedByUserId;
    private String locked;
    private String doorNumber;
    private String coloaderDevngAcctNo;
    private String coloaderDevngAcct;
    private String dispositionCode;
    private String dispositionId;
    private String searchVoyageLimit;
    // ADD Details For LCL Exports
    private String landCarrier;
    private String landCarrierAcountNumber;
    private String landExitCity;
    private String landExitCityUnlocCode;
    private String landExitDate;
    private String exportAgentAcctName;
    private String exportAgentAcctNo;
    private String cfclAcctName;
    private String cfclAcctNo;
    private String ssMasterBl;
    private String sslDocsCutoffDate;
    // Adding for the send voyage Notification
    private boolean shipper;
    private boolean consignee;
    private boolean forwarder;
    private boolean notify;
    private boolean bookingContact;
    private boolean internalEmployees;
    private boolean portAgent;
    private String voyageChangeReason;
    private String changedFields;
    private String voyageReasonId;
    //Adding variable for StopOffs Option in Inland
    private String warehouseName;
    private Long warehouseId;
    private String warehouseNo;
    private boolean showAllDr;
    private String stopOffETD;
    private String stopOffETA;
    private String stopOffRemarks;
    private String goBackVoyage;
    private String goBackInland;
    private String goBackCfcl;
    private boolean intransitDr;
    // declare for selecting Arrival loacation when i complete the unit
    private String arrivallocation;
    private String unitTruckRemarks; //adding  for remarks added from unit in inland voayge
    private boolean showUnCompleteUnits;
    private String toScreenName;
    private boolean showBooking;
    private boolean showPreReleased;
    private String filterByNewValue;
    private String schServiceType;
    //Search
    private String searchOriginId;
    private String searchFdId;
    private String searchTerminalNo;
    private String searchLoginId;
    private String searchUnitNo;
    private String searchMasterBl;
    private String searchAgentNo;
    private String searchDispoId;
    private String searchVoyageNo;
    private String searchOrigin;
    private String searchFd;
    private String searchTerminal;
    private boolean releaseLock;
    private boolean searchLclContainerSize;
    private String loadedBy;
    private String loaddeByUserId;
    private String forceAgentAcctNo;//ReleasedDr's
    private String hazFlag;
    private String unitSsIds;
    private Integer totalPieces;
    private String volumeMetric;
    private String weightMetric;
    private String volumeImperial;
    private String weightImperial;
    private String pieceDRTotal;
    private String CBMDRTotal;
    private String KGSDRTotal;
    private String CFTDRTotal;
    private String LBSDRTotal;
    private String pieceBLTotal;
    private String CBMBLTotal;
    private String KGSBLTotal;
    private String CFTBLTotal;
    private String LBSBLTotal;
    private String isReleasedDr;
    private String changeVoyOpt;
    private boolean customerEmployee;
    private boolean billingTerminal;
    private boolean bookingPdf;
    private boolean nonRatedBl;
    private boolean homeScreenVoyageFileFlag;
    private boolean printSsDockReceipt;
    private String comments;

    // LCL Unit SOLAS/VGM Info
    private double dunnageWeightLbs;
    private double dunnageWeightKgs;
    private double tareWeightLbs;
    private double tareWeightKgs;
    private double cargoWeightLbs;
    private double cargoWeightKgs;
    private String verificationSignature;
    private String verificationDate;
    private String deleteMoveAction;
    private String wareHouseNo;

    private boolean autoConvert;
    private boolean printViaMasterBl;

    private String consolidateFiles;
    private boolean masterBlInvoiceValue;

    private boolean checkAllRealeaseWithCurrLoc;
    private boolean unPickDrOpt;

    public boolean isReleaseLock() {
        return releaseLock;
    }

    public void setReleaseLock(boolean releaseLock) {
        this.releaseLock = releaseLock;
    }

    public LclAddVoyageForm() {
        this.shipper = true;
        this.customerEmployee = true;
        this.bookingPdf = true;
        this.nonRatedBl = true;
        this.billingTerminal = true;
        this.consignee = true;
        this.forwarder = true;
        this.notify = true;
        this.bookingContact = true;
        this.internalEmployees = true;
        this.portAgent = true;
        this.showAllDr = false;
        this.showBooking = false;
        this.showPreReleased = false;
        this.autoConvert = true;
    }

    public String getGoBackCfcl() {
        return goBackCfcl;
    }

    public void setGoBackCfcl(String goBackCfcl) {
        this.goBackCfcl = goBackCfcl;
    }

    public String getGoBackInland() {
        return goBackInland;
    }

    public void setGoBackInland(String goBackInland) {
        this.goBackInland = goBackInland;
    }

    public String getDisplayLoadComplete() {
        return displayLoadComplete;
    }

    public void setDisplayLoadComplete(String displayLoadComplete) {
        this.displayLoadComplete = displayLoadComplete;
    }

    public LclSsHeader getLclssheader() {
        return lclssheader;
    }

    public void setLclssheader(LclSsHeader lclssheader) {
        this.lclssheader = lclssheader;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPodDestination() {
        return podDestination;
    }

    public void setPodDestination(String podDestination) {
        this.podDestination = podDestination;
    }

    public String getPol() {
        return pol;
    }

    public String getGoBackVoyage() {
        return goBackVoyage;
    }

    public void setGoBackVoyage(String goBackVoyage) {
        this.goBackVoyage = goBackVoyage;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPooOrigin() {
        return pooOrigin;
    }

    public void setPooOrigin(String pooOrigin) {
        this.pooOrigin = pooOrigin;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSpReferenceName() {
        return spReferenceName;
    }

    public void setSpReferenceName(String spReferenceName) {
        this.spReferenceName = spReferenceName;
    }

    public String getSpReferenceNo() {
        return spReferenceNo;
    }

    public void setSpReferenceNo(String spReferenceNo) {
        this.spReferenceNo = spReferenceNo;
    }

    public Integer getFinalDestinationId() {
        return finalDestinationId;
    }

    public void setFinalDestinationId(Integer finalDestinationId) {
        this.finalDestinationId = finalDestinationId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public Integer getPolId() {
        return polId;
    }

    public void setPolId(Integer polId) {
        this.polId = polId;
    }

    public Integer getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(Integer arrivalId) {
        this.arrivalId = arrivalId;
    }

    public String getLoadLrdt() {
        return loadLrdt;
    }

    public void setLoadLrdt(String loadLrdt) {
        this.loadLrdt = loadLrdt;
    }

    public String getEtaPod() {
        return etaPod;
    }

    public void setEtaPod(String etaPod) {
        this.etaPod = etaPod;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public Integer getDepartureId() {
        return departureId;
    }

    public void setDepartureId(Integer departureId) {
        this.departureId = departureId;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getArrivalPier() {
        return arrivalPier;
    }

    public void setArrivalPier(String arrivalPier) {
        this.arrivalPier = arrivalPier;
    }

    public String getDeparturePier() {
        return departurePier;
    }

    public void setDeparturePier(String departurePier) {
        this.departurePier = departurePier;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public Integer getClosedUserId() {
        return closedUserId;
    }

    public void setClosedUserId(Integer closedUserId) {
        this.closedUserId = closedUserId;
    }

    public Integer getAuditedUserId() {
        return auditedUserId;
    }

    public void setAuditedUserId(Integer auditedUserId) {
        this.auditedUserId = auditedUserId;
    }

    public String getHazmatLrdt() {
        return hazmatLrdt;
    }

    public void setHazmatLrdt(String hazmatLrdt) {
        this.hazmatLrdt = hazmatLrdt;
    }

    public String getHazmatPermitted() {
        return hazmatPermitted;
    }

    public void setHazmatPermitted(String hazmatPermitted) {
        this.hazmatPermitted = hazmatPermitted;
    }

    public String getRefrigerationPermitted() {
        return refrigerationPermitted;
    }

    public void setRefrigerationPermitted(String refrigerationPermitted) {
        this.refrigerationPermitted = refrigerationPermitted;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getPolCode() {
        return polCode;
    }

    public void setPolCode(String polCode) {
        this.polCode = polCode;
    }

    public String getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(String auditedBy) {
        this.auditedBy = auditedBy;
    }

    public String getAuditedDate() {
        return auditedDate;
    }

    public void setAuditedDate(String auditedDate) {
        this.auditedDate = auditedDate;
    }

    public String getAuditedRemarks() {
        return auditedRemarks;
    }

    public void setAuditedRemarks(String auditedRemarks) {
        this.auditedRemarks = auditedRemarks;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getClosedRemarks() {
        return closedRemarks;
    }

    public void setClosedRemarks(String closedRemarks) {
        this.closedRemarks = closedRemarks;
    }

    public Integer getLrdOverrideDays() {
        return lrdOverrideDays;
    }

    public void setLrdOverrideDays(Integer lrdOverrideDays) {
        this.lrdOverrideDays = lrdOverrideDays;
    }

    public String getAgentBrandName() {
        return agentBrandName;
    }

    public void setAgentBrandName(String agentBrandName) {
        this.agentBrandName = agentBrandName;
    }

    public Integer getTtOverrideDays() {
        return ttOverrideDays;
    }

    public void setTtOverrideDays(Integer ttOverrideDays) {
        this.ttOverrideDays = ttOverrideDays;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getUnitType() {
        return unitType;
    }

    public void setUnitType(Long unitType) {
        this.unitType = unitType;
    }

    public String getHazmatPermittedUnit() {
        return hazmatPermittedUnit;
    }

    public void setHazmatPermittedUnit(String hazmatPermittedUnit) {
        this.hazmatPermittedUnit = hazmatPermittedUnit;
    }

    public String getRefrigerationPermittedUnit() {
        return refrigerationPermittedUnit;
    }

    public void setRefrigerationPermittedUnit(String refrigerationPermittedUnit) {
        this.refrigerationPermittedUnit = refrigerationPermittedUnit;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getHiddenRefrigerationPermitted() {
        return hiddenRefrigerationPermitted;
    }

    public void setHiddenRefrigerationPermitted(String hiddenRefrigerationPermitted) {
        this.hiddenRefrigerationPermitted = hiddenRefrigerationPermitted;
    }

    public Long getUnitssId() {
        return unitssId;
    }

    public void setUnitssId(Long unitssId) {
        this.unitssId = unitssId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getOriginalDestinationId() {
        return originalDestinationId;
    }

    public void setOriginalDestinationId(Integer originalDestinationId) {
        this.originalDestinationId = originalDestinationId;
    }

    public String getOriginalDestinationName() {
        return originalDestinationName;
    }

    public void setOriginalDestinationName(String originalDestinationName) {
        this.originalDestinationName = originalDestinationName;
    }

    public Integer getOriginalOriginId() {
        return originalOriginId;
    }

    public void setOriginalOriginId(Integer originalOriginId) {
        this.originalOriginId = originalOriginId;
    }

    public String getOriginalOriginName() {
        return originalOriginName;
    }

    public void setOriginalOriginName(String originalOriginName) {
        this.originalOriginName = originalOriginName;
    }

    public String getOpenPopup() {
        return openPopup;
    }

    public void setOpenPopup(String openPopup) {
        this.openPopup = openPopup;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDestuffedFileNumbers() {
        return destuffedFileNumbers;
    }

    public void setDestuffedFileNumbers(String destuffedFileNumbers) {
        this.destuffedFileNumbers = destuffedFileNumbers;
    }

    public String getStuffedFileNumbers() {
        return stuffedFileNumbers;
    }

    public void setStuffedFileNumbers(String stuffedFileNumbers) {
        this.stuffedFileNumbers = stuffedFileNumbers;
    }

    public String getFilterByChanges() {
        return filterByChanges;
    }

    public void setFilterByChanges(String filterByChanges) {
        this.filterByChanges = filterByChanges;
    }

    public Integer getPolPodTT() {
        return polPodTT;
    }

    public void setPolPodTT(Integer polPodTT) {
        this.polPodTT = polPodTT;
    }

    public Integer getCo_dbd() {
        return co_dbd;
    }

    public void setCo_dbd(Integer co_dbd) {
        this.co_dbd = co_dbd;
    }

    public String getCo_tod() {
        return co_tod;
    }

    public void setCo_tod(String co_tod) {
        this.co_tod = co_tod;
    }

    public String getUnitsReopened() {
        return unitsReopened;
    }

    public void setUnitsReopened(String unitsReopened) {
        this.unitsReopened = unitsReopened;
    }

    public String getOpenLCLUnit() {
        return openLCLUnit;
    }

    public void setOpenLCLUnit(String openLCLUnit) {
        this.openLCLUnit = openLCLUnit;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getBlbody() {
        return blbody;
    }

    public void setBlbody(String blbody) {
        this.blbody = blbody;
    }

    public String getConsigneeAccountNumber() {
        return consigneeAccountNumber;
    }

    public void setConsigneeAccountNumber(String consigneeAccountNumber) {
        this.consigneeAccountNumber = consigneeAccountNumber;
    }

    public String getConsigneeEdi() {
        return consigneeEdi;
    }

    public void setConsigneeEdi(String consigneeEdi) {
        this.consigneeEdi = consigneeEdi;
    }

    public String getExportReferenceEdi() {
        return exportReferenceEdi;
    }

    public void setExportReferenceEdi(String exportReferenceEdi) {
        this.exportReferenceEdi = exportReferenceEdi;
    }

    public String getNotifyAccountNumber() {
        return notifyAccountNumber;
    }

    public void setNotifyAccountNumber(String notifyAccountNumber) {
        this.notifyAccountNumber = notifyAccountNumber;
    }

    public String getNotifyEdi() {
        return notifyEdi;
    }

    public void setNotifyEdi(String notifyEdi) {
        this.notifyEdi = notifyEdi;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getShipperAccountNumber() {
        return shipperAccountNumber;
    }

    public void setShipperAccountNumber(String shipperAccountNumber) {
        this.shipperAccountNumber = shipperAccountNumber;
    }

    public String getShipperEdi() {
        return shipperEdi;
    }

    public void setShipperEdi(String shipperEdi) {
        this.shipperEdi = shipperEdi;
    }

    public String getShipperAccountNo() {
        return shipperAccountNo;
    }

    public void setShipperAccountNo(String shipperAccountNo) {
        this.shipperAccountNo = shipperAccountNo;
    }

    public String getConsigneeAccountNo() {
        return consigneeAccountNo;
    }

    public void setConsigneeAccountNo(String consigneeAccountNo) {
        this.consigneeAccountNo = consigneeAccountNo;
    }

    public String getNotifyAccountNo() {
        return notifyAccountNo;
    }

    public void setNotifyAccountNo(String notifyAccountNo) {
        this.notifyAccountNo = notifyAccountNo;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public String getNewConsignee() {
        return newConsignee;
    }

    public void setNewConsignee(String newConsignee) {
        this.newConsignee = newConsignee;
    }

    public String getNewNotify() {
        return newNotify;
    }

    public void setNewNotify(String newNotify) {
        this.newNotify = newNotify;
    }

    public String getNewShipper() {
        return newShipper;
    }

    public void setNewShipper(String newShipper) {
        this.newShipper = newShipper;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getCfsWarehouse() {
        return cfsWarehouse;
    }

    public void setCfsWarehouse(String cfsWarehouse) {
        this.cfsWarehouse = cfsWarehouse;
    }

    public Integer getCfsWarehouseId() {
        return cfsWarehouseId;
    }

    public void setCfsWarehouseId(Integer cfsWarehouseId) {
        this.cfsWarehouseId = cfsWarehouseId;
    }

    public String getColoaderAcct() {
        return coloaderAcct;
    }

    public void setColoaderAcct(String coloaderAcct) {
        this.coloaderAcct = coloaderAcct;
    }

    public String getColoaderAcctNo() {
        return coloaderAcctNo;
    }

    public void setColoaderAcctNo(String coloaderAcctNo) {
        this.coloaderAcctNo = coloaderAcctNo;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getItDatetime() {
        return itDatetime;
    }

    public void setItDatetime(String itDatetime) {
        this.itDatetime = itDatetime;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public String getItPort() {
        return itPort;
    }

    public void setItPort(String itPort) {
        this.itPort = itPort;
    }

    public Integer getItPortId() {
        return itPortId;
    }

    public void setItPortId(Integer itPortId) {
        this.itPortId = itPortId;
    }

    public String getLastFreeDate() {
        return lastFreeDate;
    }

    public void setLastFreeDate(String lastFreeDate) {
        this.lastFreeDate = lastFreeDate;
    }

    public String getOriginAcct() {
        return originAcct;
    }

    public void setOriginAcct(String originAcct) {
        this.originAcct = originAcct;
    }

    public String getOriginAcctNo() {
        return originAcctNo;
    }

    public void setOriginAcctNo(String originAcctNo) {
        this.originAcctNo = originAcctNo;
    }

    public String getPolUnlocationCode() {
        return polUnlocationCode;
    }

    public void setPolUnlocationCode(String polUnlocationCode) {
        this.polUnlocationCode = polUnlocationCode;
    }

    public String getFdUnlocationCode() {
        return fdUnlocationCode;
    }

    public void setFdUnlocationCode(String fdUnlocationCode) {
        this.fdUnlocationCode = fdUnlocationCode;
    }

    public String getUnitWarehouse() {
        return unitWarehouse;
    }

    public void setUnitWarehouse(String unitWarehouse) {
        this.unitWarehouse = unitWarehouse;
    }

    public Integer getUnitsWarehouseId() {
        return unitsWarehouseId;
    }

    public void setUnitsWarehouseId(Integer unitsWarehouseId) {
        this.unitsWarehouseId = unitsWarehouseId;
    }

    public String getBillTerminal() {
        return billTerminal;
    }

    public void setBillTerminal(String billTerminal) {
        this.billTerminal = billTerminal;
    }

    public String getBillTerminalNo() {
        return billTerminalNo;
    }

    public void setBillTerminalNo(String billTerminalNo) {
        this.billTerminalNo = billTerminalNo;
    }

    public String getBookScheduleNo() {
        return bookScheduleNo;
    }

    public void setBookScheduleNo(String bookScheduleNo) {
        this.bookScheduleNo = bookScheduleNo;
    }

    public String getApproxDueDate() {
        return approxDueDate;
    }

    public void setApproxDueDate(String approxDueDate) {
        this.approxDueDate = approxDueDate;
    }

    public String getMasterBL() {
        return masterBL;
    }

    public void setMasterBL(String masterBL) {
        this.masterBL = masterBL;
    }

    public String getUnmanifestLCLUnit() {
        return unmanifestLCLUnit;
    }

    public void setUnmanifestLCLUnit(String unmanifestLCLUnit) {
        this.unmanifestLCLUnit = unmanifestLCLUnit;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSearchLoadDisplay() {
        return searchLoadDisplay;
    }

    public void setSearchLoadDisplay(String searchLoadDisplay) {
        this.searchLoadDisplay = searchLoadDisplay;
    }

    public String getPolEtd() {
        return polEtd;
    }

    public void setPolEtd(String polEtd) {
        this.polEtd = polEtd;
    }

    public String getManualNotyName() {
        return manualNotyName;
    }

    public void setManualNotyName(String manualNotyName) {
        this.manualNotyName = manualNotyName;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getBillsTerminal() {
        return billsTerminal;
    }

    public void setBillsTerminal(String billsTerminal) {
        this.billsTerminal = billsTerminal;
    }

    public String getBillsTerminalNo() {
        return billsTerminalNo;
    }

    public void setBillsTerminalNo(String billsTerminalNo) {
        this.billsTerminalNo = billsTerminalNo;
    }

    public String getUnitVoyageSearch() {
        return unitVoyageSearch;
    }

    public void setUnitVoyageSearch(String unitVoyageSearch) {
        this.unitVoyageSearch = unitVoyageSearch;
    }

    public String getDocReceived() {
        return docReceived;
    }

    public void setDocReceived(String docReceived) {
        this.docReceived = docReceived;
    }

    public String getSealNoIn() {
        return sealNoIn;
    }

    public void setSealNoIn(String sealNoIn) {
        this.sealNoIn = sealNoIn;
    }

    public String getSealNoOut() {
        return sealNoOut;
    }

    public void setSealNoOut(String sealNoOut) {
        this.sealNoOut = sealNoOut;
    }

    public String getStrippedDate() {
        return strippedDate;
    }

    public void setStrippedDate(String strippedDate) {
        this.strippedDate = strippedDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getExceptionFileNumbers() {
        return exceptionFileNumbers;
    }

    public void setExceptionFileNumbers(String exceptionFileNumbers) {
        this.exceptionFileNumbers = exceptionFileNumbers;
    }

    public String getUnitException() {
        return unitException;
    }

    public void setUnitException(String unitException) {
        this.unitException = unitException;
    }

    public String getCobRemarks() {
        return cobRemarks;
    }

    public void setCobRemarks(String cobRemarks) {
        this.cobRemarks = cobRemarks;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getSsDetailsId() {
        return ssDetailsId;
    }

    public void setSsDetailsId(String ssDetailsId) {
        this.ssDetailsId = ssDetailsId;
    }

    public String getSsVoyage() {
        return ssVoyage;
    }

    public void setSsVoyage(String ssVoyage) {
        this.ssVoyage = ssVoyage;
    }

    public String getVerifiedEta() {
        return verifiedEta;
    }

    public void setVerifiedEta(String verifiedEta) {
        this.verifiedEta = verifiedEta;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getCob() {
        return cob;
    }

    public void setCob(String cob) {
        this.cob = cob;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public String getReceivedMaster() {
        return receivedMaster;
    }

    public void setReceivedMaster(String receivedMaster) {
        this.receivedMaster = receivedMaster;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getDrayageProvided() {
        return drayageProvided;
    }

    public void setDrayageProvided(String drayageProvided) {
        this.drayageProvided = drayageProvided;
    }

    public String getIntermodalProvided() {
        return intermodalProvided;
    }

    public void setIntermodalProvided(String intermodalProvided) {
        this.intermodalProvided = intermodalProvided;
    }

    public String getStopoff() {
        return stopoff;
    }

    public void setStopoff(String stopoff) {
        this.stopoff = stopoff;
    }

    public String getClauseDescription() {
        return clauseDescription;
    }

    public void setClauseDescription(String clauseDescription) {
        this.clauseDescription = clauseDescription;
    }

    public String getReleaseClause() {
        return releaseClause;
    }

    public void setReleaseClause(String releaseClause) {
        this.releaseClause = releaseClause;
    }

    public String getUnitAutoCostFlag() {
        return unitAutoCostFlag;
    }

    public void setUnitAutoCostFlag(String unitAutoCostFlag) {
        this.unitAutoCostFlag = unitAutoCostFlag;
    }

    public String getVoyageTerminal() {
        return voyageTerminal;
    }

    public void setVoyageTerminal(String voyageTerminal) {
        this.voyageTerminal = voyageTerminal;
    }

    public String getVoyageOwnerFlag() {
        return voyageOwnerFlag;
    }

    public void setVoyageOwnerFlag(String voyageOwnerFlag) {
        this.voyageOwnerFlag = voyageOwnerFlag;
    }

    public String getVoyenteredById() {
        return voyenteredById;
    }

    public void setVoyenteredById(String voyenteredById) {
        this.voyenteredById = voyenteredById;
    }

    public String getVoyageOwner() {
        return voyageOwner;
    }

    public void setVoyageOwner(String voyageOwner) {
        this.voyageOwner = voyageOwner;
    }

    public Long getOldUnitId() {
        return oldUnitId;
    }

    public void setOldUnitId(Long oldUnitId) {
        this.oldUnitId = oldUnitId;
    }

    public String getSUHeadingNote() {
        return SUHeadingNote;
    }

    public void setSUHeadingNote(String SUHeadingNote) {
        this.SUHeadingNote = SUHeadingNote;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
    }

//    public String getSearchPodName() {
//        return searchPodName;
//    }
//
//    public void setSearchPodName(String searchPodName) {
//        this.searchPodName = searchPodName;
//    }
//
//    public String getSearchPolName() {
//        return searchPolName;
//    }
//
//    public void setSearchPolName(String searchPolName) {
//        this.searchPolName = searchPolName;
//    }
    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getDestuffedByUserId() {
        return destuffedByUserId;
    }

    public void setDestuffedByUserId(String destuffedByUserId) {
        this.destuffedByUserId = destuffedByUserId;
    }

    public String getStuffedByUserId() {
        return stuffedByUserId;
    }

    public void setStuffedByUserId(String stuffedByUserId) {
        this.stuffedByUserId = stuffedByUserId;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getColoaderDevngAcctNo() {
        return coloaderDevngAcctNo;
    }

    public void setColoaderDevngAcctNo(String coloaderDevngAcctNo) {
        this.coloaderDevngAcctNo = coloaderDevngAcctNo;
    }

    public String getColoaderDevngAcct() {
        return coloaderDevngAcct;
    }

    public void setColoaderDevngAcct(String coloaderDevngAcct) {
        this.coloaderDevngAcct = coloaderDevngAcct;
    }

    public String getDispositionCode() {
        return dispositionCode;
    }

    public void setDispositionCode(String dispositionCode) {
        this.dispositionCode = dispositionCode;
    }

    public String getDispositionId() {
        return dispositionId;
    }

    public void setDispositionId(String dispositionId) {
        this.dispositionId = dispositionId;
    }

    public String getSearchVoyageLimit() {
        return searchVoyageLimit;
    }

    public void setSearchVoyageLimit(String searchVoyageLimit) {
        this.searchVoyageLimit = searchVoyageLimit;
    }

    public String getLandCarrier() {
        return landCarrier;
    }

    public void setLandCarrier(String landCarrier) {
        this.landCarrier = landCarrier;
    }

    public String getLandCarrierAcountNumber() {
        return landCarrierAcountNumber;
    }

    public void setLandCarrierAcountNumber(String landCarrierAcountNumber) {
        this.landCarrierAcountNumber = landCarrierAcountNumber;
    }

    public String getLandExitCity() {
        return landExitCity;
    }

    public void setLandExitCity(String landExitCity) {
        this.landExitCity = landExitCity;
    }

    public String getLandExitCityUnlocCode() {
        return landExitCityUnlocCode;
    }

    public void setLandExitCityUnlocCode(String landExitCityUnlocCode) {
        this.landExitCityUnlocCode = landExitCityUnlocCode;
    }

    public String getLandExitDate() {
        return landExitDate;
    }

    public void setLandExitDate(String landExitDate) {
        this.landExitDate = landExitDate;
    }

    public String getExportAgentAcctName() {
        return exportAgentAcctName;
    }

    public void setExportAgentAcctName(String exportAgentAcctName) {
        this.exportAgentAcctName = exportAgentAcctName;
    }

    public String getExportAgentAcctNo() {
        return exportAgentAcctNo;
    }

    public void setExportAgentAcctNo(String exportAgentAcctNo) {
        this.exportAgentAcctNo = exportAgentAcctNo;
    }

    public boolean isConsignee() {
        return consignee;
    }

    public void setConsignee(boolean consignee) {
        this.consignee = consignee;
    }

    public boolean isForwarder() {
        return forwarder;
    }

    public void setForwarder(boolean forwarder) {
        this.forwarder = forwarder;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public boolean isShipper() {
        return shipper;
    }

    public void setShipper(boolean shipper) {
        this.shipper = shipper;
    }

    public boolean isBookingContact() {
        return bookingContact;
    }

    public void setBookingContact(boolean bookingContact) {
        this.bookingContact = bookingContact;
    }

    public boolean isInternalEmployees() {
        return internalEmployees;
    }

    public void setInternalEmployees(boolean internalEmployees) {
        this.internalEmployees = internalEmployees;
    }

    public boolean isPortAgent() {
        return portAgent;
    }

    public void setPortAgent(boolean portAgent) {
        this.portAgent = portAgent;
    }

    public String getVoyageChangeReason() {
        return voyageChangeReason;
    }

    public void setVoyageChangeReason(String voyageChangeReason) {
        this.voyageChangeReason = voyageChangeReason;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public boolean isShowAllDr() {
        return showAllDr;
    }

    public void setShowAllDr(boolean showAllDr) {
        this.showAllDr = showAllDr;
    }

    public Integer getReopenedUserId() {
        return reopenedUserId;
    }

    public void setReopenedUserId(Integer reopenedUserId) {
        this.reopenedUserId = reopenedUserId;
    }

    public String getReopenedDate() {
        return reopenedDate;
    }

    public void setReopenedDate(String reopenedDate) {
        this.reopenedDate = reopenedDate;
    }

    public String getReopenedRemarks() {
        return reopenedRemarks;
    }

    public void setReopenedRemarks(String reopenedRemarks) {
        this.reopenedRemarks = reopenedRemarks;
    }

    public String getStopOffETA() {
        return stopOffETA;
    }

    public void setStopOffETA(String stopOffETA) {
        this.stopOffETA = stopOffETA;
    }

    public String getStopOffETD() {
        return stopOffETD;
    }

    public void setStopOffETD(String stopOffETD) {
        this.stopOffETD = stopOffETD;
    }

    public String getStopOffRemarks() {
        return stopOffRemarks;
    }

    public void setStopOffRemarks(String stopOffRemarks) {
        this.stopOffRemarks = stopOffRemarks;
    }

    public boolean isIntransitDr() {
        return intransitDr;
    }

    public void setIntransitDr(boolean intransitDr) {
        this.intransitDr = intransitDr;
    }

    public String getArrivallocation() {
        return arrivallocation;
    }

    public void setArrivallocation(String arrivallocation) {
        this.arrivallocation = arrivallocation;
    }

    public String getCfclAcctName() {
        return cfclAcctName;
    }

    public void setCfclAcctName(String cfclAcctName) {
        this.cfclAcctName = cfclAcctName;
    }

    public String getCfclAcctNo() {
        return cfclAcctNo;
    }

    public void setCfclAcctNo(String cfclAcctNo) {
        this.cfclAcctNo = cfclAcctNo;
    }

    public String getUnitTruckRemarks() {
        return unitTruckRemarks;
    }

    public void setUnitTruckRemarks(String unitTruckRemarks) {
        this.unitTruckRemarks = unitTruckRemarks;
    }

    public boolean isShowUnCompleteUnits() {
        return showUnCompleteUnits;
    }

    public void setShowUnCompleteUnits(boolean showUnCompleteUnits) {
        this.showUnCompleteUnits = showUnCompleteUnits;
    }

    public String getToScreenName() {
        return toScreenName;
    }

    public void setToScreenName(String toScreenName) {
        this.toScreenName = toScreenName;
    }

    public boolean isShowBooking() {
        return showBooking;
    }

    public void setShowBooking(boolean showBooking) {
        this.showBooking = showBooking;
    }

    public String getSsMasterBl() {
        return ssMasterBl;
    }

    public void setSsMasterBl(String ssMasterBl) {
        this.ssMasterBl = ssMasterBl;
    }

    public String getSslDocsCutoffDate() {
        return sslDocsCutoffDate;
    }

    public void setSslDocsCutoffDate(String sslDocsCutoffDate) {
        this.sslDocsCutoffDate = sslDocsCutoffDate;
    }

    public String getFilterByNewValue() {
        return filterByNewValue;
    }

    public void setFilterByNewValue(String filterByNewValue) {
        this.filterByNewValue = filterByNewValue;
    }

    public String getSearchAgentNo() {
        return searchAgentNo;
    }

    public void setSearchAgentNo(String searchAgentNo) {
        this.searchAgentNo = searchAgentNo;
    }

    public String getSearchDispoId() {
        return searchDispoId;
    }

    public void setSearchDispoId(String searchDispoId) {
        this.searchDispoId = searchDispoId;
    }

    public String getSearchFdId() {
        return searchFdId;
    }

    public void setSearchFdId(String searchFdId) {
        this.searchFdId = searchFdId;
    }

    public String getSearchLoginId() {
        return searchLoginId;
    }

    public void setSearchLoginId(String searchLoginId) {
        this.searchLoginId = searchLoginId;
    }

    public String getSearchMasterBl() {
        return searchMasterBl;
    }

    public void setSearchMasterBl(String searchMasterBl) {
        this.searchMasterBl = searchMasterBl;
    }

    public String getSearchOriginId() {
        return searchOriginId;
    }

    public void setSearchOriginId(String searchOriginId) {
        this.searchOriginId = searchOriginId;
    }

    public String getSearchTerminalNo() {
        return searchTerminalNo;
    }

    public void setSearchTerminalNo(String searchTerminalNo) {
        this.searchTerminalNo = searchTerminalNo;
    }

    public String getSearchUnitNo() {
        return searchUnitNo;
    }

    public void setSearchUnitNo(String searchUnitNo) {
        this.searchUnitNo = searchUnitNo;
    }

    public String getSearchVoyageNo() {
        return searchVoyageNo;
    }

    public void setSearchVoyageNo(String searchVoyageNo) {
        this.searchVoyageNo = searchVoyageNo;
    }

    public String getSearchFd() {
        return searchFd;
    }

    public void setSearchFd(String searchFd) {
        this.searchFd = searchFd;
    }

    public String getSearchOrigin() {
        return searchOrigin;
    }

    public void setSearchOrigin(String searchOrigin) {
        this.searchOrigin = searchOrigin;
    }

    public String getSearchTerminal() {
        return searchTerminal;
    }

    public void setSearchTerminal(String searchTerminal) {
        this.searchTerminal = searchTerminal;
    }

    public String getVoyageReasonId() {
        return voyageReasonId;
    }

    public void setVoyageReasonId(String voyageReasonId) {
        this.voyageReasonId = voyageReasonId;
    }

    public String getLoadedBy() {
        return loadedBy;
    }

    public void setLoadedBy(String loadedBy) {
        this.loadedBy = loadedBy;
    }

    public String getLoaddeByUserId() {
        return loaddeByUserId;
    }

    public void setLoaddeByUserId(String loaddeByUserId) {
        this.loaddeByUserId = loaddeByUserId;
    }

    public Long getChangeVoyHeaderId() {
        return changeVoyHeaderId;
    }

    public void setChangeVoyHeaderId(Long changeVoyHeaderId) {
        this.changeVoyHeaderId = changeVoyHeaderId;
    }

    public String getChangeVoyageNo() {
        return changeVoyageNo;
    }

    public void setChangeVoyageNo(String changeVoyageNo) {
        this.changeVoyageNo = changeVoyageNo;
    }

    public String getDestPrepaidCollect() {
        return destPrepaidCollect;
    }

    public void setDestPrepaidCollect(String destPrepaidCollect) {
        this.destPrepaidCollect = destPrepaidCollect;
    }

    public String getSchServiceType() {
        return schServiceType;
    }

    public void setSchServiceType(String schServiceType) {
        this.schServiceType = schServiceType;
    }

    public String getForceAgentAcctNo() {
        return forceAgentAcctNo;
    }

    public void setForceAgentAcctNo(String forceAgentAcctNo) {
        this.forceAgentAcctNo = forceAgentAcctNo;
    }

    public String getHazFlag() {
        return hazFlag;
    }

    public void setHazFlag(String hazFlag) {
        this.hazFlag = hazFlag;
    }

    public String getUnitSsIds() {
        return unitSsIds;
    }

    public void setUnitSsIds(String unitSsIds) {
        this.unitSsIds = unitSsIds;
    }

    public String getOldUnitNo() {
        return oldUnitNo;
    }

    public void setOldUnitNo(String oldUnitNo) {
        this.oldUnitNo = oldUnitNo;
    }

    public Integer getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Integer totalPieces) {
        this.totalPieces = totalPieces;
    }

    public String getVolumeMetric() {
        return volumeMetric;
    }

    public void setVolumeMetric(String volumeMetric) {
        this.volumeMetric = volumeMetric;
    }

    public String getWeightMetric() {
        return weightMetric;
    }

    public void setWeightMetric(String weightMetric) {
        this.weightMetric = weightMetric;
    }

    public String getVolumeImperial() {
        return volumeImperial;
    }

    public void setVolumeImperial(String volumeImperial) {
        this.volumeImperial = volumeImperial;
    }

    public String getWeightImperial() {
        return weightImperial;
    }

    public void setWeightImperial(String weightImperial) {
        this.weightImperial = weightImperial;
    }

    public String getPieceDRTotal() {
        return pieceDRTotal;
    }

    public void setPieceDRTotal(String pieceDRTotal) {
        this.pieceDRTotal = pieceDRTotal;
    }

    public String getPieceBLTotal() {
        return pieceBLTotal;
    }

    public void setPieceBLTotal(String pieceBLTotal) {
        this.pieceBLTotal = pieceBLTotal;
    }

    public String getCBMDRTotal() {
        return CBMDRTotal;
    }

    public void setCBMDRTotal(String CBMDRTotal) {
        this.CBMDRTotal = CBMDRTotal;
    }

    public String getKGSDRTotal() {
        return KGSDRTotal;
    }

    public void setKGSDRTotal(String KGSDRTotal) {
        this.KGSDRTotal = KGSDRTotal;
    }

    public String getCFTDRTotal() {
        return CFTDRTotal;
    }

    public void setCFTDRTotal(String CFTDRTotal) {
        this.CFTDRTotal = CFTDRTotal;
    }

    public String getLBSDRTotal() {
        return LBSDRTotal;
    }

    public void setLBSDRTotal(String LBSDRTotal) {
        this.LBSDRTotal = LBSDRTotal;
    }

    public String getCBMBLTotal() {
        return CBMBLTotal;
    }

    public void setCBMBLTotal(String CBMBLTotal) {
        this.CBMBLTotal = CBMBLTotal;
    }

    public String getKGSBLTotal() {
        return KGSBLTotal;
    }

    public void setKGSBLTotal(String KGSBLTotal) {
        this.KGSBLTotal = KGSBLTotal;
    }

    public String getCFTBLTotal() {
        return CFTBLTotal;
    }

    public void setCFTBLTotal(String CFTBLTotal) {
        this.CFTBLTotal = CFTBLTotal;
    }

    public String getLBSBLTotal() {
        return LBSBLTotal;
    }

    public void setLBSBLTotal(String LBSBLTotal) {
        this.LBSBLTotal = LBSBLTotal;
    }

    public String getIsReleasedDr() {
        return isReleasedDr;
    }

    public void setIsReleasedDr(String isReleasedDr) {
        this.isReleasedDr = isReleasedDr;
    }

    public String getChangeVoyOpt() {
        return changeVoyOpt;
    }

    public void setChangeVoyOpt(String changeVoyOpt) {
        this.changeVoyOpt = changeVoyOpt;
    }

    public String getGeneralLoadingDeadline() {
        return generalLoadingDeadline;
    }

    public void setGeneralLoadingDeadline(String generalLoadingDeadline) {
        this.generalLoadingDeadline = generalLoadingDeadline;
    }

    public String getHazmatLoadingDeadline() {
        return hazmatLoadingDeadline;
    }

    public void setHazmatLoadingDeadline(String hazmatLoadingDeadline) {
        this.hazmatLoadingDeadline = hazmatLoadingDeadline;
    }

    public void getPickedDrMasterBL(String spBookingNo) throws Exception {
        List commodityList = new LclUnitDAO().getPickedDrForMasterBL(spBookingNo, false, this.headerId, this.unitssId);
        if (!commodityList.isEmpty()) {
            Object[] obj = (Object[]) commodityList.get(0);
            if (obj[0] != null && !obj[0].toString().trim().equals("")) {
                this.setCFTDRTotal(obj[0].toString());
            }
            if (obj[1] != null && !obj[1].toString().trim().equals("")) {
                this.setCBMDRTotal(obj[1].toString());
            }
            if (obj[2] != null && !obj[2].toString().trim().equals("")) {
                this.setLBSDRTotal(obj[2].toString());
            }
            if (obj[3] != null && !obj[3].toString().trim().equals("")) {
                this.setKGSDRTotal(obj[3].toString());
            }
            if (obj[4] != null && !obj[4].toString().trim().equals("")) {
                this.setPieceDRTotal(obj[4].toString());
            }
        }
        commodityList.clear();
        commodityList = new LclUnitDAO().getPickedDrForMasterBL(spBookingNo, true, this.headerId, this.unitssId);
        if (!commodityList.isEmpty()) {
            Object[] obj = (Object[]) commodityList.get(0);
            if (obj[0] != null && !obj[0].toString().trim().equals("")) {
                this.setCFTBLTotal(obj[0].toString());
                this.setVolumeImperial(obj[0].toString());
            }
            if (obj[1] != null && !obj[1].toString().trim().equals("")) {
                this.setCBMBLTotal(obj[1].toString());
                this.setVolumeMetric(obj[1].toString());
            }
            if (obj[2] != null && !obj[2].toString().trim().equals("")) {
                this.setLBSBLTotal(obj[2].toString());
                this.setWeightImperial(obj[2].toString());
            }
            if (obj[3] != null && !obj[3].toString().trim().equals("")) {
                this.setKGSBLTotal(obj[3].toString());
                this.setWeightMetric(obj[3].toString());
            }
            if (obj[4] != null && !obj[4].toString().trim().equals("")) {
                this.setPieceBLTotal(obj[4].toString());
                this.setTotalPieces(Integer.parseInt(obj[4].toString()));
            }
        }
    }

    public boolean isHomeScreenVoyageFileFlag() {
        return homeScreenVoyageFileFlag;
    }

    public void setHomeScreenVoyageFileFlag(boolean homeScreenVoyageFileFlag) {
        this.homeScreenVoyageFileFlag = homeScreenVoyageFileFlag;
    }

    public boolean isSearchLclContainerSize() {
        return searchLclContainerSize;
    }

    public void setSearchLclContainerSize(boolean searchLclContainerSize) {
        this.searchLclContainerSize = searchLclContainerSize;
    }

    public boolean isBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(boolean billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public boolean isBookingPdf() {
        return bookingPdf;
    }

    public void setBookingPdf(boolean bookingPdf) {
        this.bookingPdf = bookingPdf;
    }

    public boolean isCustomerEmployee() {
        return customerEmployee;
    }

    public void setCustomerEmployee(boolean customerEmployee) {
        this.customerEmployee = customerEmployee;
    }

    public boolean isNonRatedBl() {
        return nonRatedBl;
    }

    public void setNonRatedBl(boolean nonRatedBl) {
        this.nonRatedBl = nonRatedBl;
    }

    public boolean isPrintSsDockReceipt() {
        return printSsDockReceipt;
    }

    public void setPrintSsDockReceipt(boolean printSsDockReceipt) {
        this.printSsDockReceipt = printSsDockReceipt;
    }

    public double getDunnageWeightLbs() {
        return dunnageWeightLbs;
    }

    public void setDunnageWeightLbs(double dunnageWeightLbs) {
        this.dunnageWeightLbs = dunnageWeightLbs;
    }

    public double getDunnageWeightKgs() {
        return dunnageWeightKgs;
    }

    public void setDunnageWeightKgs(double dunnageWeightKgs) {
        this.dunnageWeightKgs = dunnageWeightKgs;
    }

    public double getTareWeightLbs() {
        return tareWeightLbs;
    }

    public void setTareWeightLbs(double tareWeightLbs) {
        this.tareWeightLbs = tareWeightLbs;
    }

    public double getTareWeightKgs() {
        return tareWeightKgs;
    }

    public void setTareWeightKgs(double tareWeightKgs) {
        this.tareWeightKgs = tareWeightKgs;
    }

    public double getCargoWeightLbs() {
        return cargoWeightLbs;
    }

    public void setCargoWeightLbs(double cargoWeightLbs) {
        this.cargoWeightLbs = cargoWeightLbs;
    }

    public double getCargoWeightKgs() {
        return cargoWeightKgs;
    }

    public void setCargoWeightKgs(double cargoWeightKgs) {
        this.cargoWeightKgs = cargoWeightKgs;
    }

    public String getVerificationSignature() {
        return verificationSignature;
    }

    public void setVerificationSignature(String verificationSignature) {
        this.verificationSignature = verificationSignature;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDeleteMoveAction() {
        return deleteMoveAction;
    }

    public void setDeleteMoveAction(String deleteMoveAction) {
        this.deleteMoveAction = deleteMoveAction;
    }

    public String getWareHouseNo() {
        return wareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        this.wareHouseNo = wareHouseNo;
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }

    public void setAutoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
    }

    public boolean isPrintViaMasterBl() {
        return printViaMasterBl;
    }

    public void setPrintViaMasterBl(boolean printViaMasterBl) {
        this.printViaMasterBl = printViaMasterBl;
    }

    public String getConsolidateFiles() {
        return consolidateFiles;
    }

    public void setConsolidateFiles(String consolidateFiles) {
        this.consolidateFiles = consolidateFiles;
    }

    public boolean isMasterBlInvoiceValue() {
        return masterBlInvoiceValue;
    }

    public void setMasterBlInvoiceValue(boolean masterBlInvoiceValue) {
        this.masterBlInvoiceValue = masterBlInvoiceValue;
    }

    public boolean isCheckAllRealeaseWithCurrLoc() {
        return checkAllRealeaseWithCurrLoc;
    }

    public void setCheckAllRealeaseWithCurrLoc(boolean checkAllRealeaseWithCurrLoc) {
        this.checkAllRealeaseWithCurrLoc = checkAllRealeaseWithCurrLoc;
    }

    public boolean isShowPreReleased() {
        return showPreReleased;
    }

    public void setShowPreReleased(boolean showPreReleased) {
        this.showPreReleased = showPreReleased;
    }

    public boolean isUnPickDrOpt() {
        return unPickDrOpt;
    }

    public void setUnPickDrOpt(boolean unPickDrOpt) {
        this.unPickDrOpt = unPickDrOpt;
    }
}
