/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Wsware
 */
public class SsMasterBlForm extends LogiwareActionForm {

    private String methodName;
    private String voyageNumber;
    private String origin;
    private String destination;
    private String pol;
    private String pod;
    private String ssl;
    private String eta;
    private String etd;
    private String sslBl;
    private Integer originId;
    private Integer destinationId;
    private Integer polId;
    private Integer podId;
    private String sslAccountNo;
    private String sslBlPrepaid;
    
    
    // used for query transformers for search query
    
    private String headerId;
    private String serviceType;
    private String voyage;
    private String bookingNo;
    private String voyageOrigin;
    private String voyageDest;
    private String carrierName;
    private String disputedDate;
    private String std;
    private String sta;
    private String status;
    private String acknowledge;
    private String documentMasterId;
    private String documentId;
   
    
    @Override
    public String getMethodName() {
        return methodName;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getVoyageOrigin() {
        return voyageOrigin;
    }

    public void setVoyageOrigin(String voyageOrigin) {
        this.voyageOrigin = voyageOrigin;
    }

    public String getVoyageDest() {
        return voyageDest;
    }

    public void setVoyageDest(String voyageDest) {
        this.voyageDest = voyageDest;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDisputedDate() {
        return disputedDate;
    }

    public void setDisputedDate(String disputedDate) {
        this.disputedDate = disputedDate;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(String acknowledge) {
        this.acknowledge = acknowledge;
    }

    public String getDocumentMasterId() {
        return documentMasterId;
    }

    public void setDocumentMasterId(String documentMasterId) {
        this.documentMasterId = documentMasterId;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
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

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
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

    public String getSslBl() {
        return sslBl;
    }

    public void setSslBl(String sslBl) {
        this.sslBl = sslBl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public Integer getPolId() {
        return polId;
    }

    public void setPolId(Integer polId) {
        this.polId = polId;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public String getSslAccountNo() {
        return sslAccountNo;
    }

    public void setSslAccountNo(String sslAccountNo) {
        this.sslAccountNo = sslAccountNo;
    }
    
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getSslBlPrepaid() {
        return sslBlPrepaid;
    }

    public void setSslBlPrepaid(String sslBlPrepaid) {
        this.sslBlPrepaid = sslBlPrepaid;
    }
    
}
