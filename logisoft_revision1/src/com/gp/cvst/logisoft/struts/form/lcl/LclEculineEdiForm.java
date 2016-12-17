package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Rajesh
 */
public class LclEculineEdiForm extends LogiwareActionForm {

    private String id;
    private String containerNo;
    private String sailDate;
    private String arvlDate;
    private String voyNo;
    private String polUncode;
    private String podUncode;
    private String vesselName;
    private String contSize;
    private String pieces;
    private String cube;
    private String weight;
    private String refNo;
    private String noOfBl;
    private String sslineNo;
    private String lloydsNo;
    private String sealNo;
    private String masterBl;
    private String sender;
    private String senderEmail;
    private String receiver;
    private String receiverEmail;
    private String containerRemarks;
    private String terminalNo;
    private String agentNo;
    private String warehouseNo;
    private String eciVoy;
    private String blToApprove;
    private String referenceNo;
    private String unitTypeId;
    private String ecuContSize;
    private boolean vesselErrorCheck;
    private String approved;
    private String unapproved;
    private String readyToApproved;
    private String setAgentValFlag;
    private String warehsAddress;
    private String invoiceNumber;
    private Integer limit = 25;
    private String autoCostFlag;
    private String docReceived;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArvlDate() {
        return arvlDate;
    }

    public void setArvlDate(String arvlDate) {
        this.arvlDate = arvlDate;
    }

    public String getContSize() {
        return contSize;
    }

    public void setContSize(String contSize) {
        this.contSize = contSize;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getContainerRemarks() {
        return containerRemarks;
    }

    public void setContainerRemarks(String containerRemarks) {
        this.containerRemarks = containerRemarks;
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    public String getLloydsNo() {
        return lloydsNo;
    }

    public void setLloydsNo(String lloydsNo) {
        this.lloydsNo = lloydsNo;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getNoOfBl() {
        return noOfBl;
    }

    public void setNoOfBl(String noOfBl) {
        this.noOfBl = noOfBl;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getPodUncode() {
        return podUncode;
    }

    public void setPodUncode(String podUncode) {
        this.podUncode = podUncode;
    }

    public String getPolUncode() {
        return polUncode;
    }

    public void setPolUncode(String polUncode) {
        this.polUncode = polUncode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(String sslineNo) {
        this.sslineNo = sslineNo;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getEciVoy() {
        return eciVoy;
    }

    public void setEciVoy(String eciVoy) {
        this.eciVoy = eciVoy;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getBlToApprove() {
        return blToApprove;
    }

    public void setBlToApprove(String blToApprove) {
        this.blToApprove = blToApprove;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public boolean isVesselErrorCheck() {
        return vesselErrorCheck;
    }

    public void setVesselErrorCheck(boolean vesselErrorCheck) {
        this.vesselErrorCheck = vesselErrorCheck;
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(String unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getEcuContSize() {
        return ecuContSize;
    }

    public void setEcuContSize(String ecuContSize) {
        this.ecuContSize = ecuContSize;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getUnapproved() {
        return unapproved;
    }

    public void setUnapproved(String unapproved) {
        this.unapproved = unapproved;
    }

    public String getReadyToApproved() {
        return readyToApproved;
    }

    public void setReadyToApproved(String readyToApproved) {
        this.readyToApproved = readyToApproved;
    }

    public String getSetAgentValFlag() {
        return setAgentValFlag;
    }

    public void setSetAgentValFlag(String setAgentValFlag) {
        this.setAgentValFlag = setAgentValFlag;
    }

    public String getWarehsAddress() {
        return warehsAddress;
    }

    public void setWarehsAddress(String warehsAddress) {
        this.warehsAddress = warehsAddress;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getAutoCostFlag() {
        return autoCostFlag;
    }

    public void setAutoCostFlag(String autoCostFlag) {
        this.autoCostFlag = autoCostFlag;
    }

    public String getDocReceived() {
        return docReceived;
    }

    public void setDocReceived(String docReceived) {
        this.docReceived = docReceived;
    }
    
}
