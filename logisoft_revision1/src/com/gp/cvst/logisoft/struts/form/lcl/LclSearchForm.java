/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.lcl.model.FileSearchBean;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.domain.lcl.LclUserDefaults;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUserDefaultsDAO;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Owner
 */
public class LclSearchForm extends LogiwareActionForm {

    private String fileNumber;
    private String commodity;
    private String copyQuote;
    private String copyBooking;
    private String clientNo;
    private String foreignAgent;
    private String foreignAgentAccount;
    private String origin;
    private String originSelect;
    private String portName;
    private String polName;
    private String podName;
    private String countryCode;
    private String destinationName;
    private String startDate;
    private String endDate;
    private String sailDate;
    private String client;
    private String pol;
    private String issuingTerminal;
    private String containerNo;
    private String shipperName;
    private String pod;
    private String originRegion;
    private String ssl;
    private String sslineNo;
    private String forwarder;
    private String destination;
    private String destinationRegion;
    private String createdBy;
    private String consignee;
    private String filterBy;
    private String filterByInventory;
    private String sslBookingNo;
    private String bookedBy;
    private String destCountryCode;
    private String podCountryCode;
    private String polCountryCode;
    private String limit;
    private String masterBl;
    private String inbondNo;
    private Integer sortBy;
    private String consigneeNo;
    private String shipperNo;
    private String forwarderNo;
    private String filenumber;
    private String highlightFileNo;
    private String blbookedBy;
    private String blcreatedBy;
    private List<LclSearchTemplate> templateList;
    private String cfcl;
    private String methodName;
    private String moduleName;
    private List<FileSearchBean> files;
    private String customerPo;
    private String trackingNo;
    private String warehouseDocNo;
    private String cfclAccount;
    private String cfclAcct;
    private String searchFileNo;
    private String bookedForVoyage;
    private String currentLocation;
    private String includeIntr;
    private String voyageUnit;
    private String concatenatedOrigin;
    private String concatenatedPol;
    private String concatenatedPod;
    private String concatenatedDest;
    private String indexValue;
    private String originValue;
    private String polValue;
    private String podValue;
    private String destValue;
    private String blPoolOwner;
    private String blOwner;
    private String amsHBL;
    private String subHouse;
    private Integer userId;
    private String currentLocName;
    private String currentLocCode;
    private String searchType;
    private String sortByValue;
    private String salesCode;
    private String datasource;
    private String ipiLoadNo;
    private String includeBkg;
    private String curUncode;
    // for  store snd get Conoslidated files  in Export search Query
    private String conoslidateFiles;
    private String orderBy;
    private String terminalNo;
    private String destinationAllcities;
    private String lclDefaultName;
    private Integer lclDefaultId;
    private boolean recordCount;
    private boolean searchAndApply;
    private String ignoreSearchStatus;
    //for export
    private String buttonValue;
    private String multieOrigin;
    private String multieOriginCode;
    
    // For Load Button in search screen
    private String voyageorigin;
    private String voyagedestination;
    private String headerId;
    private String unitSsId;
    private String unitId;
    private String voyageFilter;
    private String associatedTerminal;
   
    public LclSearchForm() {
    }

    public List<LclSearchTemplate> getTemplateList() throws Exception {
        if (templateList == null) {
            templateList = new LclSearchTemplateDAO().getAllTemplate();
        }
        return templateList;
    }

    public String getBlOwner() {
        return blOwner;
    }

    public void setBlOwner(String blOwner) {
        this.blOwner = blOwner;
    }

    public String getBlPoolOwner() {
        return blPoolOwner;
    }

    public void setBlPoolOwner(String blPoolOwner) {
        this.blPoolOwner = blPoolOwner;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getIncludeIntr() {
        return includeIntr;
    }

    public void setIncludeIntr(String includeIntr) {
        this.includeIntr = includeIntr;
    }

    public String getBookedForVoyage() {
        return bookedForVoyage;
    }

    public void setBookedForVoyage(String bookedForVoyage) {
        this.bookedForVoyage = bookedForVoyage;
    }

    public String getFilterByInventory() {
        return filterByInventory;
    }

    public void setFilterByInventory(String filterByInventory) {
        this.filterByInventory = filterByInventory;
    }

    public String getSearchFileNo() {
        return searchFileNo;
    }

    public void setSearchFileNo(String searchFileNo) {
        this.searchFileNo = searchFileNo;
    }

    public String getCustomerPo() {
        return customerPo;
    }

    public void setCustomerPo(String customerPo) {
        this.customerPo = customerPo;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getBlbookedBy() {
        return blbookedBy;
    }

    public void setBlbookedBy(String blbookedBy) {
        this.blbookedBy = blbookedBy;
    }

    public String getBlcreatedBy() {
        return blcreatedBy;
    }

    public void setBlcreatedBy(String blcreatedBy) {
        this.blcreatedBy = blcreatedBy;
    }

    public String getForeignAgentAccount() {
        return foreignAgentAccount;
    }

    public void setForeignAgentAccount(String foreignAgentAccount) {
        this.foreignAgentAccount = foreignAgentAccount;
    }

    public String getForeignAgent() {
        return foreignAgent;
    }

    public void setForeignAgent(String foreignAgent) {
        this.foreignAgent = foreignAgent;
    }

    public String getCfclAccount() {
        return cfclAccount;
    }

    public void setCfclAccount(String cfclAccount) {
        this.cfclAccount = cfclAccount;
    }

    public String getCfclAcct() {
        return cfclAcct;
    }

    public void setCfclAcct(String cfclAcct) {
        this.cfclAcct = cfclAcct;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCopyBooking() {
        return copyBooking;
    }

    public void setCopyBooking(String copyBooking) {
        this.copyBooking = copyBooking;
    }

    public String getCopyQuote() {
        return copyQuote;
    }

    public void setCopyQuote(String copyQuote) {
        this.copyQuote = copyQuote;
    }

    public String getDestCountryCode() {
        return destCountryCode;
    }

    public void setDestCountryCode(String destCountryCode) {
        this.destCountryCode = destCountryCode;
    }

    public String getPodCountryCode() {
        return podCountryCode;
    }

    public void setPodCountryCode(String podCountryCode) {
        this.podCountryCode = podCountryCode;
    }

    public String getPolCountryCode() {
        return polCountryCode;
    }

    public void setPolCountryCode(String polCountryCode) {
        this.polCountryCode = polCountryCode;
    }

    public String getWarehouseDocNo() {
        return warehouseDocNo;
    }

    public void setWarehouseDocNo(String warehouseDocNo) {
        this.warehouseDocNo = warehouseDocNo;
    }

    public String getCfcl() {
        return cfcl;
    }

    public void setCfcl(String cfcl) {
        this.cfcl = cfcl;
    }

    public void setTemplateList(List<LclSearchTemplate> templateList) {
        this.templateList = templateList;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationRegion() {
        return destinationRegion;
    }

    public void setDestinationRegion(String destinationRegion) {
        this.destinationRegion = destinationRegion;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder;
    }

    public String getInbondNo() {
        return inbondNo;
    }

    public void setInbondNo(String inbondNo) {
        this.inbondNo = inbondNo;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginRegion() {
        return originRegion;
    }

    public void setOriginRegion(String originRegion) {
        this.originRegion = originRegion;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public Integer getSortBy() {
        return sortBy;
    }

    public void setSortBy(Integer sortBy) {
        this.sortBy = sortBy;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(String sslineNo) {
        this.sslineNo = sslineNo;
    }

    public String getSslBookingNo() {
        return sslBookingNo;
    }

    public void setSslBookingNo(String sslBookingNo) {
        this.sslBookingNo = sslBookingNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<FileSearchBean> getFiles() {
        return files;
    }

    public void setFiles(List<FileSearchBean> files) {
        this.files = files;
    }

    public String getConsigneeNo() {
        return consigneeNo;
    }

    public void setConsigneeNo(String consigneeNo) {
        this.consigneeNo = consigneeNo;
    }

    public String getForwarderNo() {
        return forwarderNo;
    }

    public void setForwarderNo(String forwarderNo) {
        this.forwarderNo = forwarderNo;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public String getHighlightFileNo() {
        return highlightFileNo;
    }

    public void setHighlightFileNo(String highlightFileNo) {
        this.highlightFileNo = highlightFileNo;
    }

//    public void reset(ActionMapping mapping, HttpServletRequest request) {
//        super.reset(mapping, request);
//        String methodName = request.getParameter("methodName");
//        if (methodName.equals("backToSearch")) {
//            highlightFileNo = "highlight";
//            request.setAttribute("highlightFileNo", highlightFileNo);
//        }
//    }
    public String getFilenumber() {
        return filenumber;
    }

    public void setFilenumber(String filenumber) {
        this.filenumber = filenumber;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getVoyageUnit() {
        return voyageUnit;
    }

    public void setVoyageUnit(String voyageUnit) {
        this.voyageUnit = voyageUnit;
    }

    public String getOriginSelect() {
        return originSelect;
    }

    public void setOriginSelect(String originSelect) {
        this.originSelect = originSelect;
    }

    public String getConcatenatedOrigin() {
        return concatenatedOrigin;
    }

    public void setConcatenatedOrigin(String concatenatedOrigin) {
        this.concatenatedOrigin = concatenatedOrigin;
    }

    public String getConcatenatedDest() {
        return concatenatedDest;
    }

    public void setConcatenatedDest(String concatenatedDest) {
        this.concatenatedDest = concatenatedDest;
    }

    public String getConcatenatedPod() {
        return concatenatedPod;
    }

    public void setConcatenatedPod(String concatenatedPod) {
        this.concatenatedPod = concatenatedPod;
    }

    public String getConcatenatedPol() {
        return concatenatedPol;
    }

    public void setConcatenatedPol(String concatenatedPol) {
        this.concatenatedPol = concatenatedPol;
    }

    public String getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(String indexValue) {
        this.indexValue = indexValue;
    }

    public String getDestValue() {
        return destValue;
    }

    public void setDestValue(String destValue) {
        this.destValue = destValue;
    }

    public String getOriginValue() {
        return originValue;
    }

    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    public String getPodValue() {
        return podValue;
    }

    public void setPodValue(String podValue) {
        this.podValue = podValue;
    }

    public String getPolValue() {
        return polValue;
    }

    public void setPolValue(String polValue) {
        this.polValue = polValue;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAmsHBL() {
        return amsHBL;
    }

    public void setAmsHBL(String amsHBL) {
        this.amsHBL = amsHBL;
    }

    public String getSubHouse() {
        return subHouse;
    }

    public void setSubHouse(String subHouse) {
        this.subHouse = subHouse;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSortByValue() {
        return sortByValue;
    }

    public void setSortByValue(String sortByValue) {
        this.sortByValue = sortByValue;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public List<LabelValueBean> getRegions() throws Exception {
        return new GenericCodeDAO().getRegions();
    }

    public String getConoslidateFiles() {
        return conoslidateFiles;
    }

    public void setConoslidateFiles(String conoslidateFiles) {
        this.conoslidateFiles = conoslidateFiles;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getIpiLoadNo() {
        return ipiLoadNo;
    }

    public void setIpiLoadNo(String ipiLoadNo) {
        this.ipiLoadNo = ipiLoadNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getDestinationAllcities() {
        return destinationAllcities;
    }

    public void setDestinationAllcities(String destinationAllcities) {
        this.destinationAllcities = destinationAllcities;
    }

    public String getLclDefaultName() {
        return lclDefaultName;
    }

    public void setLclDefaultName(String lclDefaultName) {
        this.lclDefaultName = lclDefaultName;
    }

    public Integer getLclDefaultId() {
        return lclDefaultId;
    }

    public void setLclDefaultId(Integer lclDefaultId) {
        this.lclDefaultId = lclDefaultId;
    }

    public boolean isSearchAndApply() {
        return searchAndApply;
    }

    public void setSearchAndApply(boolean searchAndApply) {
        this.searchAndApply = searchAndApply;
    }

    public boolean isRecordCount() {
        return recordCount;
    }

    public void setRecordCount(boolean recordCount) {
        this.recordCount = recordCount;
    }

    public String getIgnoreSearchStatus() {
        return ignoreSearchStatus;
    }

    public void setIgnoreSearchStatus(String ignoreSearchStatus) {
        this.ignoreSearchStatus = ignoreSearchStatus;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getMultieOrigin() {
        return multieOrigin;
    }

    public void setMultieOrigin(String multieOrigin) {
        this.multieOrigin = multieOrigin;
    }

    public String getMultieOriginCode() {
        return multieOriginCode;
    }

    public void setMultieOriginCode(String multieOriginCode) {
        this.multieOriginCode = multieOriginCode;
    }

    public String getVoyageorigin() {
        return voyageorigin;
    }

    public void setVoyageorigin(String voyageorigin) {
        this.voyageorigin = voyageorigin;
    }

    public String getVoyagedestination() {
        return voyagedestination;
    }

    public void setVoyagedestination(String voyagedestination) {
        this.voyagedestination = voyagedestination;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getVoyageFilter() {
        return voyageFilter;
    }

    public void setVoyageFilter(String voyageFilter) {
        this.voyageFilter = voyageFilter;
    }

    public String getCurUncode() {
        return curUncode;
    }

    public void setCurUncode(String curUncode) {
        this.curUncode = curUncode;
    }

    public String getIncludeBkg() {
        return includeBkg;
    }

    public void setIncludeBkg(String includeBkg) {
        this.includeBkg = includeBkg;
    }

    public String getAssociatedTerminal() {
        return associatedTerminal;
    }

    public void setAssociatedTerminal(String associatedTerminal) {
        this.associatedTerminal = associatedTerminal;
    }
    
   }
