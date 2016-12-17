package com.logiware.accounting.model;

import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Transaction;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ManifestModel {

    private FclBl fclBl;
    private BookingFcl bookingFcl;
    private FclBlCostCodes fclBlCostCodes;
    private Long fileId;
    private Long unitId;
    private Integer chargeId;
    private String blNumber;
    private String bookingNumber;
    private String voyageNumber;
    private Date sailDate;
    private Date eta;
    private String origin;
    private String destination;
    private String shipperNo;
    private String shipperName;
    private String consigneeNo;
    private String consigneeName;
    private String forwarderNo;
    private String forwarderName;
    private String thirdPartyNo;
    private String thirdPartyName;
    private String agentNo;
    private String agentName;
    private String notifyNo;
    private String notifyName;
    private String vesselNo;
    private String vesselName;
    private String dockReceipt;
    private String chargeCode;
    private String ratePerUnitUOM;
    private String transactionType;
    private String shipmentType;
    private String subledgerSourceCode;
    private String customerName;
    private String customerNumber;
    private String glAccount;
    private String bluescreenChargeCode;
    private String terminal;
    private String sealNumbers;
    private String containerNumbers;
    private Date postedDate;
    private Date reportingDate;
    private double amount;
    private double adjustmentAmount;
    private boolean importBl;
    private boolean manifest;
    private boolean correction;
    private boolean quickCn;
    private String correctionType;
    private String customerReferenceNo;
    private String streamShipLine;
    private String pooCode;
    private String podCode;
    private String fdCode;
    private String rateType;
    private User user;
    private String arrivalNoticeEmail;
    private String arrivalNoticeFax;
    private String fileLocation;
    private Transaction ar;
    private boolean ert;
    private List<FclBlCharges> fclCharges;
    private List<FclBlCostCodes> fclCosts;
    private List<FclBlCorrections> fclCorrections;
    private FclBlCorrections fclCorrection;
    private Map<String, Double> arCustomers;
    private Map<String, String> arCustomersBillToParty;
    private String masterBl;
    private String subhouseBl;
    private String billToParty;
    private Map<String, String> correctionBlNumber;

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotifyNo() {
        return notifyNo;
    }

    public void setNotifyNo(String notifyNo) {
        this.notifyNo = notifyNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeNo() {
        return consigneeNo;
    }

    public void setConsigneeNo(String consigneeNo) {
        this.consigneeNo = consigneeNo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getForwarderName() {
        return forwarderName;
    }

    public void setForwarderName(String forwarderName) {
        this.forwarderName = forwarderName;
    }

    public String getForwarderNo() {
        return forwarderNo;
    }

    public void setForwarderNo(String forwarderNo) {
        this.forwarderNo = forwarderNo;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyNo() {
        return thirdPartyNo;
    }

    public void setThirdPartyNo(String thirdPartyNo) {
        this.thirdPartyNo = thirdPartyNo;
    }

    public String getVesselNo() {
        return vesselNo;
    }

    public void setVesselNo(String vesselNo) {
        this.vesselNo = vesselNo;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getDockReceipt() {
        return dockReceipt;
    }

    public void setDockReceipt(String dockReceipt) {
        this.dockReceipt = dockReceipt;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getRatePerUnitUOM() {
        return ratePerUnitUOM;
    }

    public void setRatePerUnitUOM(String ratePerUnitUOM) {
        this.ratePerUnitUOM = ratePerUnitUOM;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getSubledgerSourceCode() {
        return subledgerSourceCode;
    }

    public void setSubledgerSourceCode(String subledgerSourceCode) {
        this.subledgerSourceCode = subledgerSourceCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public FclBl getFclBl() {
        return fclBl;
    }

    public void setFclBl(FclBl fclBl) {
        this.fclBl = fclBl;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getBluescreenChargeCode() {
        return bluescreenChargeCode;
    }

    public void setBluescreenChargeCode(String bluescreenChargeCode) {
        this.bluescreenChargeCode = bluescreenChargeCode;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getSealNumbers() {
        return sealNumbers;
    }

    public void setSealNumbers(String sealNumbers) {
        this.sealNumbers = sealNumbers;
    }

    public String getContainerNumbers() {
        return containerNumbers;
    }

    public void setContainerNumbers(String containers) {
        this.containerNumbers = containers;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isImportBl() {
        return importBl;
    }

    public void setImportBl(boolean importBl) {
        this.importBl = importBl;
    }

    public boolean isManifest() {
        return manifest;
    }

    public void setManifest(boolean manifest) {
        this.manifest = manifest;
    }

    public boolean isCorrection() {
        return correction;
    }

    public void setCorrection(boolean correction) {
        this.correction = correction;
    }

    public boolean isQuickCn() {
        return quickCn;
    }

    public void setQuickCn(boolean quickCn) {
        this.quickCn = quickCn;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getCustomerReferenceNo() {
        return customerReferenceNo;
    }

    public void setCustomerReferenceNo(String customerReferenceNo) {
        this.customerReferenceNo = customerReferenceNo;
    }

    public String getStreamShipLine() {
        return streamShipLine;
    }

    public void setStreamShipLine(String streamShipLine) {
        this.streamShipLine = streamShipLine;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction getAr() {
        return ar;
    }

    public void setAr(Transaction ar) {
        this.ar = ar;
    }

    public List<FclBlCharges> getFclCharges() {
        return fclCharges;
    }

    public void setFclCharges(List<FclBlCharges> fclCharges) {
        this.fclCharges = fclCharges;
    }

    public List<FclBlCostCodes> getFclCosts() {
        return fclCosts;
    }

    public void setFclCosts(List<FclBlCostCodes> fclCosts) {
        this.fclCosts = fclCosts;
    }

    public List<FclBlCorrections> getFclCorrections() {
        return fclCorrections;
    }

    public void setFclCorrections(List<FclBlCorrections> fclCorrections) {
        this.fclCorrections = fclCorrections;
    }

    public FclBlCorrections getFclCorrection() {
        return fclCorrection;
    }

    public void setFclCorrection(FclBlCorrections fclCorrection) {
        this.fclCorrection = fclCorrection;
    }

    public Map<String, Double> getArCustomers() {
        return arCustomers;
    }

    public void setArCustomers(Map<String, Double> arCustomers) {
        this.arCustomers = arCustomers;
    }

    public Map<String, String> getArCustomersBillToParty() {
        return arCustomersBillToParty;
    }

    public void setArCustomersBillToParty(Map<String, String> arCustomersBillToParty) {
        this.arCustomersBillToParty = arCustomersBillToParty;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public String getFdCode() {
        return fdCode;
    }

    public void setFdCode(String fdCode) {
        this.fdCode = fdCode;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getPooCode() {
        return pooCode;
    }

    public void setPooCode(String pooCode) {
        this.pooCode = pooCode;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public boolean isErt() {
        return ert;
    }

    public void setErt(boolean ert) {
        this.ert = ert;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public BookingFcl getBookingFcl() {
        return bookingFcl;
    }

    public void setBookingFcl(BookingFcl bookingFcl) {
        this.bookingFcl = bookingFcl;
    }

    public FclBlCostCodes getFclBlCostCodes() {
        return fclBlCostCodes;
    }

    public void setFclBlCostCodes(FclBlCostCodes fclBlCostCodes) {
        this.fclBlCostCodes = fclBlCostCodes;
    }

    public String getArrivalNoticeEmail() {
        return arrivalNoticeEmail;
    }

    public void setArrivalNoticeEmail(String arrivalNoticeEmail) {
        this.arrivalNoticeEmail = arrivalNoticeEmail;
    }

    public String getArrivalNoticeFax() {
        return arrivalNoticeFax;
    }

    public void setArrivalNoticeFax(String arrivalNoticeFax) {
        this.arrivalNoticeFax = arrivalNoticeFax;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getSubhouseBl() {
        return subhouseBl;
    }

    public void setSubhouseBl(String subhouseBl) {
        this.subhouseBl = subhouseBl;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public Map<String, String> getCorrectionBlNumber() {
        return correctionBlNumber;
    }

    public void setCorrectionBlNumber(Map<String, String> correctionBlNumber) {
        this.correctionBlNumber = correctionBlNumber;
    }

    public double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }
    
}
