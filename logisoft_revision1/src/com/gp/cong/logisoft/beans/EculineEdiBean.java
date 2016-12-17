package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Rajesh
 */
public class EculineEdiBean implements Serializable {

    private BigInteger id;
    private String containerNo;
    private Date sailDate;
    private Date arvlDate;
    private String voyNo;
    private String polUncode;
    private String podUncode;
    private String origin;
    private String destination;
    private BigInteger originId;
    private BigInteger destinationId;
    private String vesselName;
    private String vesselCode;
    private String contSize;
    private String shortContSize;
    private String pieces;
    private String cube;
    private String weight;
    private String refNo;
    private String noOfBl;
    private String sslineNo;
    private String scacCode;
    private String sslineName;
    private String lloydsNo;
    private String sealNo;
    private String masterBl;
    private String sender;
    private String senderEmail;
    private String receiver;
    private String receiverEmail;
    private String billingTerminal;
    private String terminalNo;
    private String containerRemarks;
    private String agentNo;
    private String warehouseNo;
    private String warehouseName;
    private String warehouseAddress;
    private String eciVoy;
    private String unitId;
    private String scheduleNo;
    private boolean approved;
    private String unitTypeId;
    private boolean adjudicated;
    private boolean vesselErrorCheck;
    private String ssLineBluescreenNo;
    private Integer invCount;
    private Integer invProcessed;
    private String unPolCount;
    private String unPodCount;
    private String docReceived;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getArvlDate() {
        return arvlDate;
    }

    public void setArvlDate(Date arvlDate) {
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

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
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

    public String getSslineNo() throws Exception {
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

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

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

    public String getVesselCode() {
        return vesselCode;
    }

    public void setVesselCode(String vesselCode) {
        this.vesselCode = vesselCode;
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

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public BigInteger getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(BigInteger destinationId) {
        this.destinationId = destinationId;
    }

    public BigInteger getOriginId() {
        return originId;
    }

    public void setOriginId(BigInteger originId) {
        this.originId = originId;
    }

    public String getSslineName() {
        return sslineName;
    }

    public void setSslineName(String sslineName) {
        this.sslineName = sslineName;
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(String unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public boolean isAdjudicated() {
        return adjudicated;
    }

    public void setAdjudicated(boolean adjudicated) {
        this.adjudicated = adjudicated;
    }

    public boolean isVesselErrorCheck() {
        return vesselErrorCheck;
    }

    public void setVesselErrorCheck(boolean vesselErrorCheck) {
        this.vesselErrorCheck = vesselErrorCheck;
    }

    public String getScacCode() {
        return scacCode;
    }

    public void setScacCode(String scacCode) {
        this.scacCode = scacCode;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getShortContSize() {
        return shortContSize;
    }

    public void setShortContSize(String shortContSize) {
        this.shortContSize = shortContSize;
    }

    public String getSsLineBluescreenNo() {
        return ssLineBluescreenNo;
    }

    public void setSsLineBluescreenNo(String ssLineBluescreenNo) {
        this.ssLineBluescreenNo = ssLineBluescreenNo;
    }

    public Integer getInvCount() {
        return invCount;
    }

    public void setInvCount(Integer invCount) {
        this.invCount = invCount;
    }

    public Integer getInvProcessed() {
        return invProcessed;
    }

    public void setInvProcessed(Integer invProcessed) {
        this.invProcessed = invProcessed;
    }

    public String getUnPolCount() {
        return unPolCount;
    }

    public void setUnPolCount(String unPolCount) {
        this.unPolCount = unPolCount;
    }

    public String getUnPodCount() {
        return unPodCount;
    }

    public void setUnPodCount(String unPodCount) {
        this.unPodCount = unPodCount;
    }

    public String getDocReceived() {
        return docReceived;
    }

    public void setDocReceived(String docReceived) {
        this.docReceived = docReceived;
    }

}
