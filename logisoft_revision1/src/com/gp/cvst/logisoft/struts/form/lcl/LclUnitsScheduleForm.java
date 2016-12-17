package com.gp.cvst.logisoft.struts.form.lcl;

public class LclUnitsScheduleForm extends LogiwareActionForm {

    private String methodName;
    private Integer portOfOriginId;
    private String origin;
    private Integer finalDestinationId;
    private String destination;
    private Integer polId;
    private String polName;
    private String polCode;
    private Integer podId;
    private String podName;
    private String podCode;
    private String filterByChanges;
    private String voyageId;
    private String serviceType;
    private String limit = "50";
    private String unitNo;
    private String voyageNo;
    private String dispositionId;
    private String dispositionCode;
    //Import 
    private String billsTerminal;
    private String billsTerminalNo;
    private String loginName;
    private String loginId;
    private String loginUserSearchFlag;
    private String columnName;
    private String masterBl;
    private String unitNoCheck;
    private String agentNo;
    //Export
    private String unitVoyageSearch;
    private String warehouseName;
    private String warehouseNo;
    private String warehouseId;
    private String cfclAcctName;
    private String cfclAcctNo;
    private boolean showUnCompleteUnits;
    private boolean unitMultiFlag;
    private String sortBy;
    private String unitId;
    private boolean isLclContainerSize;

    private String bookingNo;
    private boolean bookingMultiFlag;
    private boolean emailMe;
    private String toEmailAddress;
    private String ccEmailAddress;
    private String bccEmailAddress;
    private String emailSubject;
    private String emailMessage;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getFinalDestinationId() {
        return finalDestinationId;
    }

    public void setFinalDestinationId(Integer finalDestinationId) {
        this.finalDestinationId = finalDestinationId;
    }

    public Integer getPortOfOriginId() {
        return portOfOriginId;
    }

    public void setPortOfOriginId(Integer portOfOriginId) {
        this.portOfOriginId = portOfOriginId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFilterByChanges() {
        return filterByChanges;
    }

    public void setFilterByChanges(String filterByChanges) {
        this.filterByChanges = filterByChanges;
    }

    public String getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(String voyageId) {
        this.voyageId = voyageId;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPolCode() {
        return polCode;
    }

    public void setPolCode(String polCode) {
        this.polCode = polCode;
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

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public String getDispositionId() {
        return dispositionId;
    }

    public void setDispositionId(String dispositionId) {
        this.dispositionId = dispositionId;
    }

    public String getDispositionCode() {
        return dispositionCode;
    }

    public void setDispositionCode(String dispositionCode) {
        this.dispositionCode = dispositionCode;
    }

    public String getLoginUserSearchFlag() {
        return loginUserSearchFlag;
    }

    public void setLoginUserSearchFlag(String loginUserSearchFlag) {
        this.loginUserSearchFlag = loginUserSearchFlag;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getUnitNoCheck() {
        return unitNoCheck;
    }

    public void setUnitNoCheck(String unitNoCheck) {
        this.unitNoCheck = unitNoCheck;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
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

    public boolean isShowUnCompleteUnits() {
        return showUnCompleteUnits;
    }

    public void setShowUnCompleteUnits(boolean showUnCompleteUnits) {
        this.showUnCompleteUnits = showUnCompleteUnits;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public boolean isUnitMultiFlag() {
        return unitMultiFlag;
    }

    public void setUnitMultiFlag(boolean unitMultiFlag) {
        this.unitMultiFlag = unitMultiFlag;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isIsLclContainerSize() {
        return isLclContainerSize;
    }

    public void setIsLclContainerSize(boolean isLclContainerSize) {
        this.isLclContainerSize = isLclContainerSize;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public boolean isBookingMultiFlag() {
        return bookingMultiFlag;
    }

    public void setBookingMultiFlag(boolean bookingMultiFlag) {
        this.bookingMultiFlag = bookingMultiFlag;
    }

    public boolean isEmailMe() {
        return emailMe;
    }

    public void setEmailMe(boolean emailMe) {
        this.emailMe = emailMe;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String getCcEmailAddress() {
        return ccEmailAddress;
    }

    public void setCcEmailAddress(String ccEmailAddress) {
        this.ccEmailAddress = ccEmailAddress;
    }

    public String getBccEmailAddress() {
        return bccEmailAddress;
    }

    public void setBccEmailAddress(String bccEmailAddress) {
        this.bccEmailAddress = bccEmailAddress;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

}
