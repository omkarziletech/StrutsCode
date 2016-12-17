package com.logiware.fcl.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.io.Serializable;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SessionForm implements Serializable {

    private String action;
    private Integer limit = 250;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String sortBy = "f.file_no";
    private String orderBy = "desc";
    private String fromDate;
    private String toDate;
    private String fileNumber;
    private boolean importFile;
    private boolean sailingDate;
    private String pol;
    private String pod;
    private String origin;
    private String destination;
    private String originRegion;
    private String originRegionDesc;
    private String destinationRegion;
    private String destinationRegionDesc;
    private String createdBy;
    private boolean createdByMe;
    private String filterBy;
    private String bookedBy;
    private boolean bookedByMe;
    private String issuingTerminal;
    private String containerNumber;
    private String sslBookingNumber;
    private boolean disableClient;
    private String clientName;
    private String clientNumber;
    private String manualClientName;
    private String shipperName;
    private String shipperNumber;
    private String sslName;
    private String sslNumber;
    private String forwarderName;
    private String forwarderNumber;
    private String consigneeName;
    private String consigneeNumber;
    private String masterBl;
    private String inboundNumber;
    private String aesItn;
    private String sortByDate;
    private String ams;
    private String subHouseBl;
    private String commodity;
    private String masterShipper;
    private String masterShipperNumber;
    private String approvedByCheck;
    private String loginCheck;
    private String testBoxBlNumber;
    private String testBoxNoticeNumber;
    private String billToParty;
    private String originalBillToPartyCorrectionTypeY;
    private String chargeDescription;
    private String differenAmount;
    private String customerContact;
    private String fileNo;
    private String includePosted;
    private String password;
    private String buttonValue;
    private String index;
    private String correctionCode;
    private String blNumber;
    private String shipper;
    private String forwarder;
    private String rampCity;
    private String date;
    private String sailDate;
    private String userName;
    private String noticeNo;
    private String streamShipBl;
    private String streamShipBlFromBlPage;
    private String houseBl;
    private String thirdParty;
    private String agent;
    private String voyages;
    //--properties for AddFclBlCorrections Page--
    private String correctionType;
    private String accountNumber;
    private String accountName;
    private String comments;
    private String oldComment;
    private String fax;
    private String email;
    private String chargeCode;
    private String amount;
    private String remarks;
    private String newAmount;
    private String isFax;
    private String isPost;
    private String index1;
    //--temporary property ----
    private String tempChargeCode;
    private String tempChargeCodeDesc;
    private String newTempCorrectionType;
    private String billTo;
    private String temp;
    private String approval;
    private String checkStatus;
    private String viewMode;
    private String id;
    private String tempCustomerName;
    private String tempCustomerNo;
    private String sendCopyTo;
    private String approvedBy;
    private String filerBy;
    private String fileType;
    private String chargeId;
    private boolean quickCn;
    //--Memo properties --
    private String creditMemoEmail;
    private String debitMemoEmail;
    private String currentProfit;
    private String profitAfterCn;

    private String moduleId;
    private String moduleRefId;
    private String eventCode;
    private String eventDate;
    private String eventDesc;
    private String eventBy;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(Integer selectedPage) {
        this.selectedPage = selectedPage;
    }

    public Integer getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(Integer selectedRows) {
        this.selectedRows = selectedRows;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public boolean isImportFile() {
        return importFile;
    }

    public void setImportFile(boolean importFile) {
        this.importFile = importFile;
    }

    public boolean isSailingDate() {
        return sailingDate;
    }

    public void setSailingDate(boolean sailingDate) {
        this.sailingDate = sailingDate;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOriginRegion() {
        return originRegion;
    }

    public void setOriginRegion(String originRegion) {
        this.originRegion = originRegion;
    }

    public String getOriginRegionDesc() throws Exception {
        return originRegionDesc;
    }

    public void setOriginRegionDesc(String originRegionDesc) {
        this.originRegionDesc = originRegionDesc;
    }

    public String getDestinationRegion() {
        return destinationRegion;
    }

    public void setDestinationRegion(String destinationRegion) {
        this.destinationRegion = destinationRegion;
    }

    public String getDestinationRegionDesc() throws Exception {
        if (CommonUtils.isNotEmpty(destinationRegion) && CommonUtils.isEmpty(destinationRegionDesc)) {
            destinationRegionDesc = new GenericCodeDAO().getCodeDescription(destinationRegion);
        }
        return destinationRegionDesc;
    }

    public void setDestinationRegionDesc(String destinationRegionDesc) {
        this.destinationRegionDesc = destinationRegionDesc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isCreatedByMe() {
        return createdByMe;
    }

    public void setCreatedByMe(boolean createdByMe) {
        this.createdByMe = createdByMe;
    }

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public boolean isBookedByMe() {
        return bookedByMe;
    }

    public void setBookedByMe(boolean bookedByMe) {
        this.bookedByMe = bookedByMe;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getSslBookingNumber() {
        return sslBookingNumber;
    }

    public void setSslBookingNumber(String sslBookingNumber) {
        this.sslBookingNumber = sslBookingNumber;
    }

    public boolean isDisableClient() {
        return disableClient;
    }

    public void setDisableClient(boolean disableClient) {
        this.disableClient = disableClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getManualClientName() {
        return manualClientName;
    }

    public void setManualClientName(String manualClientName) {
        this.manualClientName = manualClientName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperNumber() {
        return shipperNumber;
    }

    public void setShipperNumber(String shipperNumber) {
        this.shipperNumber = shipperNumber;
    }

    public String getSslName() {
        return sslName;
    }

    public void setSslName(String sslName) {
        this.sslName = sslName;
    }

    public String getSslNumber() {
        return sslNumber;
    }

    public void setSslNumber(String sslNumber) {
        this.sslNumber = sslNumber;
    }

    public String getForwarderName() {
        return forwarderName;
    }

    public void setForwarderName(String forwarderName) {
        this.forwarderName = forwarderName;
    }

    public String getForwarderNumber() {
        return forwarderNumber;
    }

    public void setForwarderNumber(String forwarderNumber) {
        this.forwarderNumber = forwarderNumber;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeNumber() {
        return consigneeNumber;
    }

    public void setConsigneeNumber(String consigneeNumber) {
        this.consigneeNumber = consigneeNumber;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getInboundNumber() {
        return inboundNumber;
    }

    public void setInboundNumber(String inboundNumber) {
        this.inboundNumber = inboundNumber;
    }

    public String getAesItn() {
        return aesItn;
    }

    public void setAesItn(String aesItn) {
        this.aesItn = aesItn;
    }

    public String getSortByDate() {
        return sortByDate;
    }

    public void setSortByDate(String sortByDate) {
        this.sortByDate = sortByDate;
    }

    public String getAms() {
        return ams;
    }

    public void setAms(String ams) {
        this.ams = ams;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getMasterShipper() {
        return masterShipper;
    }

    public void setMasterShipper(String masterShipper) {
        this.masterShipper = masterShipper;
    }

    public String getMasterShipperNumber() {
        return masterShipperNumber;
    }

    public void setMasterShipperNumber(String masterShipperNumber) {
        this.masterShipperNumber = masterShipperNumber;
    }

    public String getApprovedByCheck() {
        return approvedByCheck;
    }

    public void setApprovedByCheck(String approvedByCheck) {
        this.approvedByCheck = approvedByCheck;
    }

    public String getLoginCheck() {
        return loginCheck;
    }

    public void setLoginCheck(String loginCheck) {
        this.loginCheck = loginCheck;
    }

    public String getTestBoxBlNumber() {
        return testBoxBlNumber;
    }

    public void setTestBoxBlNumber(String testBoxBlNumber) {
        this.testBoxBlNumber = testBoxBlNumber;
    }

    public String getTestBoxNoticeNumber() {
        return testBoxNoticeNumber;
    }

    public void setTestBoxNoticeNumber(String testBoxNoticeNumber) {
        this.testBoxNoticeNumber = testBoxNoticeNumber;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getOriginalBillToPartyCorrectionTypeY() {
        return originalBillToPartyCorrectionTypeY;
    }

    public void setOriginalBillToPartyCorrectionTypeY(String originalBillToPartyCorrectionTypeY) {
        this.originalBillToPartyCorrectionTypeY = originalBillToPartyCorrectionTypeY;
    }

    public String getChargeDescription() {
        return chargeDescription;
    }

    public void setChargeDescription(String chargeDescription) {
        this.chargeDescription = chargeDescription;
    }

    public String getDifferenAmount() {
        return differenAmount;
    }

    public void setDifferenAmount(String differenAmount) {
        this.differenAmount = differenAmount;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getIncludePosted() {
        return includePosted;
    }

    public void setIncludePosted(String includePosted) {
        this.includePosted = includePosted;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCorrectionCode() {
        return correctionCode;
    }

    public void setCorrectionCode(String correctionCode) {
        this.correctionCode = correctionCode;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder;
    }

    public String getRampCity() {
        return rampCity;
    }

    public void setRampCity(String rampCity) {
        this.rampCity = rampCity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getStreamShipBl() {
        return streamShipBl;
    }

    public void setStreamShipBl(String streamShipBl) {
        this.streamShipBl = streamShipBl;
    }

    public String getStreamShipBlFromBlPage() {
        return streamShipBlFromBlPage;
    }

    public void setStreamShipBlFromBlPage(String streamShipBlFromBlPage) {
        this.streamShipBlFromBlPage = streamShipBlFromBlPage;
    }

    public String getHouseBl() {
        return houseBl;
    }

    public void setHouseBl(String houseBl) {
        this.houseBl = houseBl;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getVoyages() {
        return voyages;
    }

    public void setVoyages(String voyages) {
        this.voyages = voyages;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOldComment() {
        return oldComment;
    }

    public void setOldComment(String oldComment) {
        this.oldComment = oldComment;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(String newAmount) {
        this.newAmount = newAmount;
    }

    public String getIsFax() {
        return isFax;
    }

    public void setIsFax(String isFax) {
        this.isFax = isFax;
    }

    public String getIsPost() {
        return isPost;
    }

    public void setIsPost(String isPost) {
        this.isPost = isPost;
    }

    public String getIndex1() {
        return index1;
    }

    public void setIndex1(String index1) {
        this.index1 = index1;
    }

    public String getTempChargeCode() {
        return tempChargeCode;
    }

    public void setTempChargeCode(String tempChargeCode) {
        this.tempChargeCode = tempChargeCode;
    }

    public String getTempChargeCodeDesc() {
        return tempChargeCodeDesc;
    }

    public void setTempChargeCodeDesc(String tempChargeCodeDesc) {
        this.tempChargeCodeDesc = tempChargeCodeDesc;
    }

    public String getNewTempCorrectionType() {
        return newTempCorrectionType;
    }

    public void setNewTempCorrectionType(String newTempCorrectionType) {
        this.newTempCorrectionType = newTempCorrectionType;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempCustomerName() {
        return tempCustomerName;
    }

    public void setTempCustomerName(String tempCustomerName) {
        this.tempCustomerName = tempCustomerName;
    }

    public String getTempCustomerNo() {
        return tempCustomerNo;
    }

    public void setTempCustomerNo(String tempCustomerNo) {
        this.tempCustomerNo = tempCustomerNo;
    }

    public String getSendCopyTo() {
        return sendCopyTo;
    }

    public void setSendCopyTo(String sendCopyTo) {
        this.sendCopyTo = sendCopyTo;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getFilerBy() {
        return filerBy;
    }

    public void setFilerBy(String filerBy) {
        this.filerBy = filerBy;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public boolean isQuickCn() {
        return quickCn;
    }

    public void setQuickCn(boolean quickCn) {
        this.quickCn = quickCn;
    }

    public String getCreditMemoEmail() {
        return creditMemoEmail;
    }

    public void setCreditMemoEmail(String creditMemoEmail) {
        this.creditMemoEmail = creditMemoEmail;
    }

    public String getDebitMemoEmail() {
        return debitMemoEmail;
    }

    public void setDebitMemoEmail(String debitMemoEmail) {
        this.debitMemoEmail = debitMemoEmail;
    }

    public String getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(String currentProfit) {
        this.currentProfit = currentProfit;
    }

    public String getProfitAfterCn() {
        return profitAfterCn;
    }

    public void setProfitAfterCn(String profitAfterCn) {
        this.profitAfterCn = profitAfterCn;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleRefId() {
        return moduleRefId;
    }

    public void setModuleRefId(String moduleRefId) {
        this.moduleRefId = moduleRefId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

}
