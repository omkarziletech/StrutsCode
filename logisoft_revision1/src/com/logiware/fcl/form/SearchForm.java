package com.logiware.fcl.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.fcl.model.ResultModel;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SearchForm extends BaseForm {

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
    private List<ResultModel> results;
    private String vessel;
    private String salesCode;
    private boolean olySpotRate;
    private String masterShipper;
    private String masterShipperNumber;

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
        if (CommonUtils.isNotEmpty(originRegion) && CommonUtils.isNotEmpty(originRegionDesc)) {
            originRegionDesc = new GenericCodeDAO().getCodeDescription(originRegion);
        }
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

    public List<ResultModel> getResults() {
        return results;
    }

    public void setResults(List<ResultModel> results) {
        this.results = results;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public boolean isOlySpotRate() {
        return olySpotRate;
    }

    public void setOlySpotRate(boolean olySpotRate) {
        this.olySpotRate = olySpotRate;
    }

    public List<LabelValueBean> getRegions() throws Exception {
        return new GenericCodeDAO().getRegions();
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

    public List<LabelValueBean> getFilterByOptions() throws Exception {
        List<LabelValueBean> options = new ArrayList<LabelValueBean>();
        options.add(new LabelValueBean("All", ""));
        options.add(new LabelValueBean("Quotation", "Quotation"));
        options.add(new LabelValueBean("Booking", "Booking"));
        options.add(new LabelValueBean("BL", "BL"));
        options.add(new LabelValueBean("Doc's Not Received", "Doc's Not Received"));
        options.add(new LabelValueBean("Un Manifested", "Un Manifested"));
        options.add(new LabelValueBean("Manifested", "Manifested"));
        options.add(new LabelValueBean("Manifested No Master", "Manifested No Master"));
        options.add(new LabelValueBean("Master Not Received", "Master Not Received"));
        options.add(new LabelValueBean("FAE Not Applied", "FAE Not Applied"));
        options.add(new LabelValueBean("Online Bookings", "Online Bookings"));
        return options;
    }

    public List<LabelValueBean> getSortByDateOptions() throws Exception {
        List<LabelValueBean> options = new ArrayList<LabelValueBean>();
        options.add(new LabelValueBean("Select", ""));
        options.add(new LabelValueBean("Container Cut off", "Container Cut off"));
        options.add(new LabelValueBean("Doc Cut Off", "Doc Cut Off"));
        options.add(new LabelValueBean("ETD", "ETD"));
        return options;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.disableClient = false;
        this.sailingDate = false;
        this.createdByMe = false;
        this.bookedByMe = false;
        this.olySpotRate = false;
    }
}
