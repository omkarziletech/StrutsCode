package com.gp.cvst.logisoft.struts.form.lcl;

public class LclAddUnitsForm extends LogiwareActionForm {

    private Long unitId;
    private Long unitssId;
    private String unitNo;
    private String oldUnitNo;
    private Long unitType;
    private String hazmatPermitted;
    private String drayageProvided;
    private String receivedMaster;
    private String intermodalProvided;
    private String stopoff;
    private String chasisNo;
    private String filterByChanges;
    private String refrigerationPermitted;
    private String remarks;
    private String grossWeightMetric;
    private String grossWeightImperial;
    private String volumeMetric;
    private String volumeImperial;
    private String warehouseName;
    private String disposition;
    private String location;
    private String arrivedDateTime;
    private String departedDateTime;
    private String dispositionDateTime;
    private Integer warehouseId;
    private Integer dispositionId;
    private Long unitWarehouseId;
    private Long unitDispoId;
    private Long headerId;
    private String unitsReopened;
    private String bookingNumber;
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
    private String etaPodDate;
    private String sealNoIn;
    private String sealNoOut;
    private String SUHeadingNote;
    private String strippedDate;
    private String approxDueDate;
    private String masterBL;
    private String buttonValue;
    private String message;
    private String unmanifestLCLUnit;
    private String unitStatus;
    private Long bookingAcId;
    private Integer dupCfsId;
    private Long dupUnitTypeId;
    private String oldUnitId;
    private String cob;
    //Imports
    private String loadedByUser;
    private String loadedByUserId;
    private String strippedByUser;
    private String strippedByUserId;
    private String postFlag;
    private String doorNumber;
    private String coloaderDevngAcctNo;
    private String coloaderDevngAcctName;
    private Long unLocationId;
    private String prepaidCollect;
    private String wareHouseNo;
    private String wareHouseId;
    private boolean hazStatus;
    private String sealNo;
    private String loadedBy;
    private String loaddeByUserId;

    private double dunnageWeightLbs;
    private double dunnageWeightKgs;
    private double tareWeightLbs;
    private double tareWeightKgs;
    private double cargoWeightLbs;
    private double cargoWeightKgs;
    private String verificationSignature;
    private String verificationDate;
    private String comments;

    public Long getBookingAcId() {
        return bookingAcId;
    }

    public void setBookingAcId(Long bookingAcId) {
        this.bookingAcId = bookingAcId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public Long getUnitType() {
        return unitType;
    }

    public void setUnitType(Long unitType) {
        this.unitType = unitType;
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

    public String getGrossWeightImperial() {
        return grossWeightImperial;
    }

    public void setGrossWeightImperial(String grossWeightImperial) {
        this.grossWeightImperial = grossWeightImperial;
    }

    public String getGrossWeightMetric() {
        return grossWeightMetric;
    }

    public void setGrossWeightMetric(String grossWeightMetric) {
        this.grossWeightMetric = grossWeightMetric;
    }

    public String getVolumeImperial() {
        return volumeImperial;
    }

    public void setVolumeImperial(String volumeImperial) {
        this.volumeImperial = volumeImperial;
    }

    public String getVolumeMetric() {
        return volumeMetric;
    }

    public void setVolumeMetric(String volumeMetric) {
        this.volumeMetric = volumeMetric;
    }

    public String getFilterByChanges() {
        return filterByChanges;
    }

    public void setFilterByChanges(String filterByChanges) {
        this.filterByChanges = filterByChanges;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArrivedDateTime() {
        return arrivedDateTime;
    }

    public void setArrivedDateTime(String arrivedDateTime) {
        this.arrivedDateTime = arrivedDateTime;
    }

    public String getDepartedDateTime() {
        return departedDateTime;
    }

    public void setDepartedDateTime(String departedDateTime) {
        this.departedDateTime = departedDateTime;
    }

    public String getDispositionDateTime() {
        return dispositionDateTime;
    }

    public void setDispositionDateTime(String dispositionDateTime) {
        this.dispositionDateTime = dispositionDateTime;
    }

    public Long getUnitDispoId() {
        return unitDispoId;
    }

    public void setUnitDispoId(Long unitDispoId) {
        this.unitDispoId = unitDispoId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getUnitWarehouseId() {
        return unitWarehouseId;
    }

    public void setUnitWarehouseId(Long unitWarehouseId) {
        this.unitWarehouseId = unitWarehouseId;
    }

    public String getOldUnitNo() {
        return oldUnitNo;
    }

    public void setOldUnitNo(String oldUnitNo) {
        this.oldUnitNo = oldUnitNo;
    }

    public String getUnitsReopened() {
        return unitsReopened;
    }

    public void setUnitsReopened(String unitsReopened) {
        this.unitsReopened = unitsReopened;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Long getUnitssId() {
        return unitssId;
    }

    public void setUnitssId(Long unitssId) {
        this.unitssId = unitssId;
    }

    public Integer getDispositionId() {
        return dispositionId;
    }

    public void setDispositionId(Integer dispositionId) {
        this.dispositionId = dispositionId;
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

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUnmanifestLCLUnit() {
        return unmanifestLCLUnit;
    }

    public void setUnmanifestLCLUnit(String unmanifestLCLUnit) {
        this.unmanifestLCLUnit = unmanifestLCLUnit;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
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

    public Integer getDupCfsId() {
        return dupCfsId;
    }

    public void setDupCfsId(Integer dupCfsId) {
        this.dupCfsId = dupCfsId;
    }

    public Long getDupUnitTypeId() {
        return dupUnitTypeId;
    }

    public void setDupUnitTypeId(Long dupUnitTypeId) {
        this.dupUnitTypeId = dupUnitTypeId;
    }

    public String getOldUnitId() {
        return oldUnitId;
    }

    public void setOldUnitId(String oldUnitId) {
        this.oldUnitId = oldUnitId;
    }

    public String getSUHeadingNote() {
        return SUHeadingNote;
    }

    public void setSUHeadingNote(String SUHeadingNote) {
        this.SUHeadingNote = SUHeadingNote;
    }

    public String getLoadedByUser() {
        return loadedByUser;
    }

    public void setLoadedByUser(String loadedByUser) {
        this.loadedByUser = loadedByUser;
    }

    public String getStrippedByUser() {
        return strippedByUser;
    }

    public void setStrippedByUser(String strippedByUser) {
        this.strippedByUser = strippedByUser;
    }

    public String getLoadedByUserId() {
        return loadedByUserId;
    }

    public void setLoadedByUserId(String loadedByUserId) {
        this.loadedByUserId = loadedByUserId;
    }

    public String getStrippedByUserId() {
        return strippedByUserId;
    }

    public void setStrippedByUserId(String strippedByUserId) {
        this.strippedByUserId = strippedByUserId;
    }

    public String getEtaPodDate() {
        return etaPodDate;
    }

    public void setEtaPodDate(String etaPodDate) {
        this.etaPodDate = etaPodDate;
    }

    public String getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
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

    public String getColoaderDevngAcctName() {
        return coloaderDevngAcctName;
    }

    public void setColoaderDevngAcctName(String coloaderDevngAcctName) {
        this.coloaderDevngAcctName = coloaderDevngAcctName;
    }

    public Long getUnLocationId() {
        return unLocationId;
    }

    public void setUnLocationId(Long unLocationId) {
        this.unLocationId = unLocationId;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getWareHouseNo() {
        return wareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        this.wareHouseNo = wareHouseNo;
    }

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public boolean isHazStatus() {
        return hazStatus;
    }

    public void setHazStatus(boolean hazStatus) {
        this.hazStatus = hazStatus;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getReceivedMaster() {
        return receivedMaster;
    }

    public void setReceivedMaster(String receivedMaster) {
        this.receivedMaster = receivedMaster;
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

    public String getCob() {
        return cob;
    }

    public void setCob(String cob) {
        this.cob = cob;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

}
