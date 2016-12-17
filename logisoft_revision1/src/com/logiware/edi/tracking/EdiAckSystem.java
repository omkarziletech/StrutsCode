package com.logiware.edi.tracking;

public class EdiAckSystem implements java.io.Serializable {

    private Integer id;
    private String quoteTerm;
    private String quoteDr;
    private String fileName;
    private String controlSequenceNumber;
    private String ediCompanyIOrG;
    private String ackReceivedDate;
    private String ackCreatedTimeStamp;
    private String shipmentId;
    private String shippingInstructionAuditNumber;
    private String severity;
    private String detailCommentsInAckMessage;
    private String bookingNumber;
    private String billOfLadingNumber;
    private String carrierScac;
    private String divisionCode;
    private String forwarderReferenceNumber;
    private String consigneeOrderNumber;
    private String purchaseOrderNumber;
    private String contractNumber;
    private String exportReferenceNumber;
    private String brokerReferenceNumber;
    private String customerOrderNumber;
    private String federalMaritimeComNumber;
    private String invoiceNumber;
    private String transactionReferenceNumber;
    private String detailAuditId;
    private Integer siId;
    private byte[] xml;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuoteTerm() {
        return quoteTerm;
    }

    public void setQuoteTerm(String quoteTerm) {
        this.quoteTerm = quoteTerm;
    }

    public String getQuoteDr() {
        return quoteDr;
    }

    public void setQuoteDr(String quoteDr) {
        this.quoteDr = quoteDr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getControlSequenceNumber() {
        return controlSequenceNumber;
    }

    public void setControlSequenceNumber(String controlSequenceNumber) {
        this.controlSequenceNumber = controlSequenceNumber;
    }

    public String getEdiCompanyIOrG() {
        return ediCompanyIOrG;
    }

    public void setEdiCompanyIOrG(String ediCompanyIOrG) {
        this.ediCompanyIOrG = ediCompanyIOrG;
    }

    public String getAckReceivedDate() {
        return ackReceivedDate;
    }

    public void setAckReceivedDate(String ackReceivedDate) {
        this.ackReceivedDate = ackReceivedDate;
    }

    public String getAckCreatedTimeStamp() {
        return ackCreatedTimeStamp;
    }

    public void setAckCreatedTimeStamp(String ackCreatedTimeStamp) {
        this.ackCreatedTimeStamp = ackCreatedTimeStamp;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShippingInstructionAuditNumber() {
        return shippingInstructionAuditNumber;
    }

    public void setShippingInstructionAuditNumber(
            String shippingInstructionAuditNumber) {
        this.shippingInstructionAuditNumber = shippingInstructionAuditNumber;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDetailCommentsInAckMessage() {
        return detailCommentsInAckMessage;
    }

    public void setDetailCommentsInAckMessage(String detailCommentsInAckMessage) {
        this.detailCommentsInAckMessage = detailCommentsInAckMessage;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBillOfLadingNumber() {
        return billOfLadingNumber;
    }

    public void setBillOfLadingNumber(String billOfLadingNumber) {
        this.billOfLadingNumber = billOfLadingNumber;
    }

    public String getCarrierScac() {
        return carrierScac;
    }

    public void setCarrierScac(String carrierScac) {
        this.carrierScac = carrierScac;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getForwarderReferenceNumber() {
        return forwarderReferenceNumber;
    }

    public void setForwarderReferenceNumber(String forwarderReferenceNumber) {
        this.forwarderReferenceNumber = forwarderReferenceNumber;
    }

    public String getConsigneeOrderNumber() {
        return consigneeOrderNumber;
    }

    public void setConsigneeOrderNumber(String consigneeOrderNumber) {
        this.consigneeOrderNumber = consigneeOrderNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getExportReferenceNumber() {
        return exportReferenceNumber;
    }

    public void setExportReferenceNumber(String exportReferenceNumber) {
        this.exportReferenceNumber = exportReferenceNumber;
    }

    public String getBrokerReferenceNumber() {
        return brokerReferenceNumber;
    }

    public void setBrokerReferenceNumber(String brokerReferenceNumber) {
        this.brokerReferenceNumber = brokerReferenceNumber;
    }

    public String getCustomerOrderNumber() {
        return customerOrderNumber;
    }

    public void setCustomerOrderNumber(String customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
    }

    public String getFederalMaritimeComNumber() {
        return federalMaritimeComNumber;
    }

    public void setFederalMaritimeComNumber(String federalMaritimeComNumber) {
        this.federalMaritimeComNumber = federalMaritimeComNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getDetailAuditId() {
        return detailAuditId;
    }

    public void setDetailAuditId(String detailAuditId) {
        this.detailAuditId = detailAuditId;
    }

    public Integer getSiId() {
        return siId;
    }

    public void setSiId(Integer siId) {
        this.siId = siId;
    }

    public byte[] getXml() {
        return xml;
    }

    public void setXml(byte[] xml) {
        this.xml = xml;
    }
}
