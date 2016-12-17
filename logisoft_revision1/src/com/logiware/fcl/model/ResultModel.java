package com.logiware.fcl.model;

import java.io.Serializable;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ResultModel implements Serializable {

    private String fileType;
    private String quoteStatus;
    private String multiStatus;
    private String bookingStatus;
    private String blStatus;
    private boolean hazmat;
    private String ediStatus;
    private String bookingEdiStatus;
    private String fileNumber;
    private String blId;
    private String bookingId;
    private String quoteId;
    private String fileStatus;
    private boolean documentReceived;
    private String documentStatus;
    private String sslBookingNumber;
    private String startDate;
    private String origin;
    private String doorOrigin;
    private String pol;
    private String pod;
    private String destination;
    private String doorDestination;
    private String clientName;
    private String sslineName;
    private String issuingTerminal;
    private String trackingStatus;
    private String aesStatus;
    private int aesCount;
    private String createdBy;
    private String bookedBy;
    private String releaseType;
    private String containerNumber;
    private String eta;
    private String etd;
    private String invoiceNumber;
    private String createdDate;
    private String postedDate;
    private String invoiceAmount;
    private String billToParty;
    private String user;
    private String description;
    private String status;
    private Long id;
    private String sslineBl;
    private String comments;
    private String ackComments;
    private String sailDate;
    private String containers;
    private String dateOprDone;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(String quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBlStatus() {
        return blStatus;
    }

    public void setBlStatus(String blStatus) {
        this.blStatus = blStatus;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getEdiStatus() {
        return ediStatus;
    }

    public void setEdiStatus(String ediStatus) {
        this.ediStatus = ediStatus;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getBlId() {
        return blId;
    }

    public void setBlId(String blId) {
        this.blId = blId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public boolean isDocumentReceived() {
        return documentReceived;
    }

    public void setDocumentReceived(boolean documentReceived) {
        this.documentReceived = documentReceived;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getSslBookingNumber() {
        return sslBookingNumber;
    }

    public void setSslBookingNumber(String sslBookingNumber) {
        this.sslBookingNumber = sslBookingNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDoorOrigin() {
        return doorOrigin;
    }

    public void setDoorOrigin(String doorOrigin) {
        this.doorOrigin = doorOrigin;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDoorDestination() {
        return doorDestination;
    }

    public void setDoorDestination(String doorDestination) {
        this.doorDestination = doorDestination;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSslineName() {
        return sslineName;
    }

    public void setSslineName(String sslineName) {
        this.sslineName = sslineName;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public String getAesStatus() {
        return aesStatus;
    }

    public void setAesStatus(String aesStatus) {
        this.aesStatus = aesStatus;
    }

    public int getAesCount() {
        return aesCount;
    }

    public void setAesCount(int aesCount) {
        this.aesCount = aesCount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSslineBl() {
        return sslineBl;
    }

    public void setSslineBl(String sslineBl) {
        this.sslineBl = sslineBl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAckComments() {
        return ackComments;
    }

    public void setAckComments(String ackComments) {
        this.ackComments = ackComments;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getContainers() {
        return containers;
    }

    public void setContainers(String containers) {
        this.containers = containers;
    }

    public String getBookingEdiStatus() {
        return bookingEdiStatus;
    }

    public void setBookingEdiStatus(String bookingEdiStatus) {
        this.bookingEdiStatus = bookingEdiStatus;
    }

    public String getDateOprDone() {
        return dateOprDone;
    }

    public void setDateOprDone(String dateOprDone) {
        this.dateOprDone = dateOprDone;
    }

    public String getMultiStatus() {
        return multiStatus;
    }

    public void setMultiStatus(String multiStatus) {
        this.multiStatus = multiStatus;
    }
    
}
