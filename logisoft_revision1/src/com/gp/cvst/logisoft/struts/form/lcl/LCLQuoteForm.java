/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author Owner
 */
public class LCLQuoteForm extends LogiwareActionForm implements LclCommonConstant {

    private static final Logger log = Logger.getLogger(LCLQuoteForm.class);
    private LclQuote lclQuote;
    private String fileNumberId;
    private LclContact lclContact;
    private Lcl3pRefNo lcl3pRefNo;
    private boolean showFullRelay;
    private boolean showFullRelayFd;
    private String defaultAgent;
    private String ssVoyage1;
    private String hazmatDr;
    private String originDr;
    private String destinationDr;
    private String commodityNo;
    private String retailCommodity;
    private String vesselName1;
    private String bookingContact;
    private String bookingContactEmail;
    private String copyFnVal;
    private String ssLine1;
    private String sailDate1;
    private String stGeorgeAccountNo;
    private String stGeorgeAddress;
    private String eciVoyage1;
    private String terminalLocation;
    private String clientWithConsignee;
    private String clientWithoutConsignee;
    private String originUnlocationCode;
    private String clientCompany;
    private String stdRateBasis;
    private String tempClientCompany;
    private Integer foreignDischargeId;
    private Integer portExitId;
    private String clientConsigneeCompany;
    private String osdRemarks;
    private String overShortdamaged;
    private String rtdTransaction;
    private String rtdAgentAcct;
    private String podUnlocationcode;
    private String duplicateDoorOrigin;
    private String nonRated;
    private String genCodefield1;
    private String importCommodity;
    // lclQuotePad Details
    private String issuingTerminal;
    private String ptrmnum;
    private String to;
    private String manualCompanyName;
    private String OriginCityZip;
    private String cityStateZip;
    private String pickupCutoffDate;
    private String pickupReadyDate;
    private String pickupReferenceNo;
    private String whsecompanyName;
    private String whseAddress;
    private String whseCity;
    private String whseState;
    private String whseZip;
    private String paddress;
    private String whsePhone;
    private String pickupInstructions;
    private String termsOfService;
    private String chargeAmount;
    private BigDecimal adjustmentAmount;
    private String pickupCost;
    private String spAcct;
    private String scacCode;
    private String pfax1;
    private String pemail1;
    private String pcontactName;
    private String pphone1;
    private String pickupHours;
    private String pickupReadyNote;
    private String pcommodityDesc;
    private String shipSupplier;
    private String whsCode;
    private String pickZip;
    
    // Client Section properties need to save in the database
    private Boolean cfcl;
    private String otiNumber;//clientOTINumber
    private String portExit;
    private String foreignDischarge;
    private String fmcNumber;
    private String commodityNumber;
    private Boolean aesBy;
    //  Trade Route Section properties need to save in DB
    private String relayOvr;
    private String doorOriginCityZip;
    private String cityName;
    private String eciVoyage;
    private String sailDate;
    private String ssLine;
    private String vesselName;
    private String ssVoyage;
    private String approximateDue;
    private String pooLrdt;
    private String termToDoBl;
    private String agentName;
    private String agentNumber;
    private String ERT;
    private String agentInfo;
    private String portGriRemarks;
    private String upcomingSailings;
    private String hotCodes;
    private String customerPo;
    private String wareHouseDoc;
    private String ncmNo;
    private String inbond;
    private String hsCode;
    private String tracking;
    private String externalComment;
    private String internalComment;
    private String pro;
    private String harmonizedCode;
    private String osd;
    private String valueOfGoods;
    private String remarksForLoading;
    private String storageDate;
    private String truckLine;
    private String weighedBy;
    private String stowedBy;
    private String dr;
    private String cargoRecd;
    private String piece;
    private String cube;
    private String weight;
    private String pwk;
    private boolean ups;
    private String pe;
    private String bookingHsCode;
    private String bookingHsCodeId;
    private String hsCodeWeightMetric;
    private String hsCodepiece;
    private String packageType;
    private String packageTypeId;
    private Boolean calcHeavy;
    private String pcBoth;
    private String aesItnNumber;
    private String aesException;
    private String pickupYesNo;
    private String doorMovePickupCts;
    private String fdEta1;
    private String fdEta;
    private String shipper_contactName;
    private String originCode;
    private String destinationCode;
    private String fileNumber;
    private String specialRemarks;
    private String internalRemarks;
    private String deliverCargoTo;
    private String polCode;
    private String podCode;
    private String origin;
    private String destination;
    private String screenType;
    private String polUnlocationcode;
    private String basedOnCity;
    private String index;
    private String pickupState;
    private String pickupZip;
    private String deliveryMetro = N;
    private String supContactName;
    private String newSupplier;
    private String shipContactName;
    private String newShipper;
    private String consContactName;
    private String newConsignee;
    private String notifyContactName;
    private String newNotify;
    private String itPort;
    private String rateTypes;
    private String deliveryMetroField;
      /**
     * Imports Properties
     *
     * @return
     */
    private String transShipMent;
    private String replicateFileNumber;
    private String bundleToOf;
    private String printOnBL;
    private String lastFd;//output
    private String pickupDate;//input
    private String goDate;//input
    private String cfsTerminal;
    private String scac;
    private String subHouseBl;
    private String itNo;
    private String entryNo;
    private String entryClass;
    private String declaredValue;
    private String stGeorgeAccount;
    private String FreightRelease;
    private String ExpressReleaseClasuse;
    private String relaySearch;
    private String quoteComplete;
    private String quoteCompleted;
    private LclQuoteImport lclQuoteImport;
    private String cfclAcctName;
    private String cfclAcctNo;
    private String newConsigneeContact;
    private String newClientContact;
    private String newShipperContact;
    private String newForwarderContact;
    private String consigneeManualContact;
    private String forwarederContactManual;
    private String clientContactManual;
    private String originUnlocationCodeForDr;
    private String unlocationCodeForDr;
    private String shipperManualContact;
    private String spotRateCommNo;
    private String lcl3pRefId;
    private String noteId;
    //quick quote
    private String commodityTypeForDr;
    private String packageIdDr;
    private String commodityNoForDr;
    private String packageDr;
    private String commodityTypeId;
    private String finalDestinationForDr;
    private String finalDestinationIdForDr;
    private String portOfOriginForDr;
    private String portOfOriginIdForDr;
    private String clientAcctForDr;
    private String rateTypeForDr;
    private String clientCompanyForDr;
    private String shipperAddress;
    private String forwarderAddress;
    private String consigneeAddress;
    private String notifyAddress;
    //Import
    private String pcBothImports;
    private String billtoCodeImports;
    private String amsHblNo;
    private String defaultAms;
    private String amsHblPiece;
    private String moduleName;
    private String notify2ContactName;//manual Notify2Name
    private String notify2AcctNo;
    private String showAllCities;
    //Customer Notes check by Icon
    private boolean clientIcon;
    private boolean shipperIcon;
    private boolean forwaderIcon;
    private boolean consigneeIcon;
    private boolean notifyIcon;
    private boolean notify2Icon;
    //Set ToolTip
    private String shipperToolTip;
    private String consigneeToolTip;
    private String notifyToolTip;
    private String notify2ToolTip;
    private String originalFileNo;
    //for destinationServices in Exports
    private boolean destinationServices;
    // separate the POD and FD Remarks in LCLExports
    private String specialRemarksPod;
    private String internalRemarksPod;
    private String portGriRemarksPod;
    //import External Gri Remarks
    private String externalGriRemarks;
    private String pooTrmNum;
    private String polTrmNum;
    private String podEciPortCode;
    private String fdEciPortCode;
    private String fdEngmet;
    private String reportSaveFlag;
    private Integer pooWhseId;
    private String originLrdDate;
    private String fdEtaDate;
    private Long bookedSsHeaderId;
    private String businessUnit;
    private String companyCode;
    private String eculineCommodity;
    private boolean homeScreenQtFileFlag;
    private String manualDoorOriginCityZip;
    private String previousSailing;
    private String transshipType;
    private String tPortOfLoadingId;
    private String tFinalDestinationId;
    private String agentBrand;
    private String cifValue;
    private String smallParcelRemarks;
    private String saveRemarks;

    public LCLQuoteForm() {
        if (lclQuote == null) {
            lclQuote = new LclQuote();
        }
        if (lclQuoteImport == null) {
            lclQuoteImport = new LclQuoteImport();
        }
    }

     public String getPreviousSailing() {
        return previousSailing;
    }

    public void setPreviousSailing(String previousSailing) {
        this.previousSailing = previousSailing;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getBundleToOf() {
        return bundleToOf;
    }

    public void setBundleToOf(String bundleToOf) {
        this.bundleToOf = bundleToOf;
    }

    public String getPrintOnBL() {
        return printOnBL;
    }

    public void setPrintOnBL(String printOnBL) {
        this.printOnBL = printOnBL;
    }

    public String getWhsCode() {
        return whsCode;
    }

    public void setWhsCode(String whsCode) {
        this.whsCode = whsCode;
    }

    public String getDestinationDr() {
        return destinationDr;
    }

    public void setDestinationDr(String destinationDr) {
        this.destinationDr = destinationDr;
    }

    public String getOriginDr() {
        return originDr;
    }

    public void setOriginDr(String originDr) {
        this.originDr = originDr;
    }

    public String getDeliveryMetroField() {
        return deliveryMetroField;
    }

    public void setDeliveryMetroField(String deliveryMetroField) {
        this.deliveryMetroField = deliveryMetroField;
    }

    public String getRateTypes() {
        return rateTypes;
    }

    public String getHazmatDr() {
        return hazmatDr;
    }

    public void setHazmatDr(String hazmatDr) {
        this.hazmatDr = hazmatDr;
    }

    public void setRateTypes(String rateTypes) {
        this.rateTypes = rateTypes;
    }

    public String getNonRated() {
        if (lclQuote.getNonRated() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setNonRated(String nonRated) {
        if (nonRated.equals("Y")) {
            lclQuote.setNonRated(TRUE);
        } else {
            lclQuote.setNonRated(FALSE);
        }
    }

    public String getConsContactName() {
        return consContactName;
    }

    public void setConsContactName(String consContactName) {
        this.consContactName = consContactName;
    }

    public String getQuoteComplete() {
        return quoteComplete;
    }

    public void setQuoteComplete(String quoteComplete) {
        this.quoteComplete = quoteComplete;
    }

    public String getQuoteCompleted() {
        return quoteCompleted;
    }

    public void setQuoteCompleted(String quoteCompleted) {
        this.quoteCompleted = quoteCompleted;
    }

    public Integer getForeignDischargeId() {
        return foreignDischargeId;
    }

    public void setForeignDischargeId(Integer foreignDischargeId) {
        this.foreignDischargeId = foreignDischargeId;
    }

    public Integer getPortExitId() {
        return portExitId;
    }

    public void setPortExitId(Integer portExitId) {
        this.portExitId = portExitId;
    }

    public String getNewConsignee() {
        return newConsignee;
    }

    public void setNewConsignee(String newConsignee) {
        this.newConsignee = newConsignee;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getClientConsigneeCompany() {
        return clientConsigneeCompany;
    }

    public void setClientConsigneeCompany(String clientConsigneeCompany) {
        this.clientConsigneeCompany = clientConsigneeCompany;
    }

    public String getClientContactManual() {
        return clientContactManual;
    }

    public void setClientContactManual(String clientContactManual) {
        this.clientContactManual = clientContactManual;
    }

    public String getConsigneeManualContact() {
        return consigneeManualContact;
    }

    public void setConsigneeManualContact(String consigneeManualContact) {
        this.consigneeManualContact = consigneeManualContact;
    }

    public String getForwarederContactManual() {
        return forwarederContactManual;
    }

    public void setForwarederContactManual(String forwarederContactManual) {
        this.forwarederContactManual = forwarederContactManual;
    }

    public String getReplicateFileNumber() {
        return replicateFileNumber;
    }

    public void setReplicateFileNumber(String replicateFileNumber) {
        this.replicateFileNumber = replicateFileNumber;
    }

    public String getShipperManualContact() {
        return shipperManualContact;
    }

    public void setShipperManualContact(String shipperManualContact) {
        this.shipperManualContact = shipperManualContact;
    }

    public String getNewClientContact() {
        return newClientContact;
    }

    public void setNewClientContact(String newClientContact) {
        this.newClientContact = newClientContact;
    }

    public String getNewConsigneeContact() {
        return newConsigneeContact;
    }

    public void setNewConsigneeContact(String newConsigneeContact) {
        this.newConsigneeContact = newConsigneeContact;
    }

    public String getNewForwarderContact() {
        return newForwarderContact;
    }

    public void setNewForwarderContact(String newForwarderContact) {
        this.newForwarderContact = newForwarderContact;
    }

    public String getNewShipperContact() {
        return newShipperContact;
    }

    public void setNewShipperContact(String newShipperContact) {
        this.newShipperContact = newShipperContact;
    }

    public String getStGeorgeAccountNo() {
        return stGeorgeAccountNo;
    }

    public void setStGeorgeAccountNo(String stGeorgeAccountNo) {
        this.stGeorgeAccountNo = stGeorgeAccountNo;
    }

    public String getStGeorgeAddress() {
        return stGeorgeAddress;
    }

    public void setStGeorgeAddress(String stGeorgeAddress) {
        this.stGeorgeAddress = stGeorgeAddress;
    }

    public String getNewNotify() {
        return newNotify;
    }

    public void setNewNotify(String newNotify) {
        this.newNotify = newNotify;
    }

    public String getForeignDischarge() {
        return foreignDischarge;
    }

    public void setForeignDischarge(String foreignDischarge) {
        this.foreignDischarge = foreignDischarge;
    }

    public String getPortExit() {
        return portExit;
    }

    public void setPortExit(String portExit) {
        this.portExit = portExit;
    }

    public String getNewShipper() {
        return newShipper;
    }

    public void setNewShipper(String newShipper) {
        this.newShipper = newShipper;
    }

    public String getNewSupplier() {
        return newSupplier;
    }

    public void setNewSupplier(String newSupplier) {
        this.newSupplier = newSupplier;
    }

    public String getNotifyContactName() {
        return notifyContactName;
    }

    public void setNotifyContactName(String notifyContactName) {
        this.notifyContactName = notifyContactName;
    }

    public String getShipContactName() {
        return shipContactName;
    }

    public void setShipContactName(String shipContactName) {
        this.shipContactName = shipContactName;
    }

    public String getOriginUnlocationCode() {
        if (lclQuote.getPortOfOrigin() != null) {
            return lclQuote.getPortOfOrigin().getUnLocationCode();
        } else {
            return "";
        }
    }

    public void setOriginUnlocationCode(String originUnlocationCode) {
        this.originUnlocationCode = originUnlocationCode;
    }

    public String getSupContactName() {
        return supContactName;
    }

    public void setSupContactName(String supContactName) {
        this.supContactName = supContactName;
    }

    public String getPickupState() {
        return pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    public String getPickupZip() {
        return pickupZip;
    }

    public void setPickupZip(String pickupZip) {
        this.pickupZip = pickupZip;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getBasedOnCity() {
        return basedOnCity;
    }

    public void setBasedOnCity(String basedOnCity) {
        this.basedOnCity = basedOnCity;
    }

    public String getDuplicateDoorOrigin() {
        return duplicateDoorOrigin;
    }

    public void setDuplicateDoorOrigin(String duplicateDoorOrigin) {
        this.duplicateDoorOrigin = duplicateDoorOrigin;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getDeliverCargoTo() {
        return lclQuote.getPooWhseContact().getAddress();
    }

    public String getPodUnlocationcode() {
        return podUnlocationcode;
    }

    public void setPodUnlocationcode(String podUnlocationcode) {
        this.podUnlocationcode = podUnlocationcode;
    }

    public void setDeliverCargoTo(String deliverCargoTo) {
        lclQuote.getPooWhseContact().setAddress(deliverCargoTo);
    }

    public String getInternalRemarks() {
        return internalRemarks;
    }

    public void setInternalRemarks(String internalRemarks) {
        this.internalRemarks = internalRemarks;
    }

    public String getDeliveryMetro() {
        return lclQuote.getDeliveryMetro();
    }

    public void setDeliveryMetro(String deliveryMetro) {
        if (CommonUtils.isNotEmpty(deliveryMetro)) {
            lclQuote.setDeliveryMetro(deliveryMetro);
        }
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
    }

    public String getCopyFnVal() {
        return copyFnVal;
    }

    public void setCopyFnVal(String copyFnVal) {
        this.copyFnVal = copyFnVal;
    }

    public String getRtdTransaction() {
        if (lclQuote.getRtdTransaction() != null && lclQuote.getRtdTransaction().equals(TRUE)) {
            return Y;
        } else {
            return N;
        }
    }

    public void setRtdTransaction(String rtdTransaction) {
        if (rtdTransaction.equals(Y)) {
            lclQuote.setRtdTransaction(TRUE);
        } else {
            lclQuote.setRtdTransaction(FALSE);
        }
    }

    public String getShipper_contactName() {
        return shipper_contactName;
    }

    public void setShipper_contactName(String shipper_contactName) {
        this.shipper_contactName = shipper_contactName;
    }

    public String getOverShortdamaged() {
        if (lclQuote.getOverShortDamaged() != null && lclQuote.getOverShortDamaged() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setOverShortdamaged(String overShortdamaged) {
        if (overShortdamaged.equals(Y)) {
            lclQuote.setOverShortDamaged(TRUE);
        } else {
            lclQuote.setOverShortDamaged(FALSE);
        }
    }

    public String getOsdRemarks() {
        return osdRemarks;
    }

    public void setOsdRemarks(String osdRemarks) {
        this.osdRemarks = osdRemarks;
    }

    public boolean isShowFullRelay() {
        return showFullRelay;
    }

    public void setShowFullRelay(boolean showFullRelay) {
        this.showFullRelay = showFullRelay;
    }

    public boolean isShowFullRelayFd() {
        return showFullRelayFd;
    }

    public void setShowFullRelayFd(boolean showFullRelayFd) {
        this.showFullRelayFd = showFullRelayFd;
    }

    public String getCity() {
        return lclQuote.getClientContact().getCity();
    }

    public void setCity(String city) {
        lclQuote.getClientContact().setCity(city);
    }

    public String getState() {
        return lclQuote.getClientContact().getState();
    }

    public void setState(String state) {
        lclQuote.getClientContact().setState(state);
    }

    public String getZip() {
        return lclQuote.getClientContact().getZip();
    }

    public void setZip(String zip) {
        lclQuote.getClientContact().setZip(zip);
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclQuote getLclQuote() {
        if (lclQuote == null) {
            lclQuote = new LclQuote();
        }
        return lclQuote;
    }

    public void setLclQuote(LclQuote lclQuote) {
        this.lclQuote = lclQuote;
    }

    public LclContact getLclContact() {
        return lclContact;
    }

    public void setLclContact(LclContact lclContact) {
        this.lclContact = lclContact;
    }

    public String getQuoteType() {
        return lclQuote.getQuoteType();
    }

    public void setQuoteType(String quoteType) {
        lclQuote.setQuoteType(quoteType);
    }

    public String getTransMode() {
        return lclQuote.getTransMode();
    }

    public void setTransMode(String transMode) {
        if (CommonUtils.isEmpty(transMode)) {
            transMode = T;
        }
        lclQuote.setTransMode(transMode);
    }

    public String getSpotRate() {
        if (lclQuote.getSpotRate() == TRUE) {
            return "Y";
        }
        return "N";
    }

    public void setSpotRate(String spotRate) {
        if (spotRate.equals("Y")) {
            lclQuote.setSpotRate(TRUE);
        } else {
            lclQuote.setSpotRate(FALSE);
        }
    }

    public String getRateType() {
        return lclQuote.getRateType();//in screen name is CTC/Retail/FTF
    }

    public void setRateType(String rateType) {
        lclQuote.setRateType(rateType);
    }

    public String getRelayOverride() {
        if (lclQuote.getRelayOverride() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setRelayOverride(String relayOverride) {
        if (relayOverride.equals(Y)) {
            lclQuote.setRelayOverride(TRUE);
        } else {
            lclQuote.setRelayOverride(FALSE);
        }
    }

    public String getBillingType() {
        return lclQuote.getBillingType();
    }

    public void setBillingType(String billingType) {
        if (CommonUtils.isEmpty(billingType)) {
            billingType = "B";
        }
        lclQuote.setBillingType(billingType);
    }

    public String getTerminalLocation() {
        if (lclQuote.getBillingTerminal() != null) {
            return lclQuote.getBillingTerminal().getTerminalLocation() + "/" + lclQuote.getBillingTerminal().getTrmnum();
        }
        return null;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public String getTrmnum() {
        if (lclQuote.getBillingTerminal() != null) {
            return lclQuote.getBillingTerminal().getTrmnum();
        }
        return null;
    }

    public void setTrmnum(String trmnum) throws Exception {
        if (CommonUtils.isNotEmpty(trmnum)) {
            RefTerminal billingTerminal = new RefTerminalDAO().findById(trmnum);
            lclQuote.setBillingTerminal(billingTerminal);
        }
    }

    public String getDefaultAgent() {
        if (lclQuote.getDefaultAgent() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setDefaultAgent(String defaultAgent) {
        if (defaultAgent.equals(Y)) {
            lclQuote.setDefaultAgent(TRUE);
        } else {
            lclQuote.setDefaultAgent(FALSE);
        }
    }

    public String getInsurance() {
        if (lclQuote.getInsurance() != null && lclQuote.getInsurance() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setInsurance(String insurance) {
        if (insurance.equals(Y)) {
            lclQuote.setInsurance(TRUE);
        } else {
            lclQuote.setInsurance(FALSE);
        }
    }

    public String getDocumentation() {
        if (lclQuote.getDocumentation() != null && lclQuote.getDocumentation() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setDocumentation(String documentation) {
        if (documentation.equals(Y)) {
            lclQuote.setDocumentation(TRUE);
        } else {
            lclQuote.setDocumentation(FALSE);
        }
    }

    public BigDecimal getDeclaredCargoValue() {
        return lclQuote.getDeclaredCargoValue();
    }

    public void setDeclaredCargoValue(BigDecimal declaredCargoValue) {
        lclQuote.setDeclaredCargoValue(declaredCargoValue);
    }

    public Boolean getSupIsTp() {
        return lclQuote.getSupIsTp();
    }

    public void setSupIsTp(Boolean supIsTp) {
        lclQuote.setSupIsTp(supIsTp);
    }

    public String getSupReference() {
        return lclQuote.getSupReference();
    }

    public void setSupReference(String supReference) {
        lclQuote.setSupReference(supReference);
    }

    public Boolean getShipIsTp() {
        return lclQuote.getShipIsTp();
    }

    public void setShipIsTp(Boolean shipIsTp) {
        lclQuote.setShipIsTp(shipIsTp);
    }

    public String getShipReference() {
        return lclQuote.getShipReference();
    }

    public void setShipReference(String shipReference) {
        lclQuote.setShipReference(shipReference);
    }

    public Boolean getFwdIsTp() {
        return lclQuote.getFwdIsTp();
    }

    public void setFwdIsTp(Boolean fwdIsTp) {
        lclQuote.setFwdIsTp(fwdIsTp);
    }

    public String getFwdFmcNo() {
        if (lclQuote.getFwdFmcNo() != null) {
            return "" + lclQuote.getFwdFmcNo();
        }
        return "";
    }

    public void setFwdFmcNo(String fwdFmcNo) {
        if (CommonUtils.isNotEmpty(fwdFmcNo)) {
            lclQuote.setFwdFmcNo(fwdFmcNo);
        } else {
            lclQuote.setFwdFmcNo(null);
        }
    }

    public String getFwdReference() {
        return lclQuote.getFwdReference();
    }

    public void setFwdReference(String fwdReference) {
        lclQuote.setFwdReference(fwdReference);
    }

    public Boolean getConsIsTp() {
        return lclQuote.getConsIsTp();
    }

    public void setConsIsTp(Boolean consIsTp) {
        lclQuote.setConsIsTp(consIsTp);
    }

    public String getConsReference() {
        return lclQuote.getConsReference();
    }

    public void setConsReference(String consReference) {
        lclQuote.setConsReference(consReference);
    }

    public Boolean getNotyIsTp() {
        return lclQuote.getNotyIsTp();
    }

    public void setNotyIsTp(Boolean notyIsTp) {
        lclQuote.setNotyIsTp(notyIsTp);
    }

    public String getNotyReference() {
        return lclQuote.getNotyReference();
    }

    public void setNotyReference(String notyReference) {
        lclQuote.setNotyReference(notyReference);
    }

    public String getOverrideDimType() {
        return lclQuote.getOverrideDimType();
    }

    public void setOverrideDimType(String overrideDimType) {
        lclQuote.setOverrideDimType(overrideDimType);
    }

    public BigDecimal getOverrideDimCubic() {
        return lclQuote.getOverrideDimCubic();
    }

    public void setOverrideDimCubic(BigDecimal overrideDimCubic) {
        lclQuote.setOverrideDimCubic(overrideDimCubic);
    }

    public BigDecimal getOverrideDimWeight() {
        return lclQuote.getOverrideDimWeight();
    }

    public void setOverrideDimWeight(BigDecimal overrideDimWeight) {
        lclQuote.setOverrideDimWeight(overrideDimWeight);
    }

    public String getAgentAcct() {
        return lclQuote.getAgentAcct().getAccountno();
    }

    public void setAgentAcct(String agentAcct) throws Exception {
        if (CommonUtils.isNotEmpty(agentAcct)) {
            lclQuote.setAgentAcct(new TradingPartnerDAO().findById(agentAcct));
        } else {
            lclQuote.setAgentAcct(null);
        }
    }

    public String getRtdAgentAcct() {
        return lclQuote.getRtdAgentAcct().getAccountno();
    }

    public void setRtdAgentAcct(String rtdAgentAcct) throws Exception {
        if (CommonUtils.isNotEmpty(rtdAgentAcct)) {
            lclQuote.setRtdAgentAcct(new TradingPartnerDAO().findById(rtdAgentAcct));
        } else {
            lclQuote.setRtdAgentAcct(null);
        }
    }

    public LclContact getClientContact() {
        return lclQuote.getClientContact();
    }

    public void setClientContact(LclContact clientContact) {
        lclQuote.setClientContact(clientContact);
    }

    public LclContact getPooWhseContact() {
        return lclQuote.getPooWhseContact();
    }

    public void setPooWhseContact(LclContact pooWhseContact) {
        lclQuote.setPooWhseContact(pooWhseContact);
    }

    public String getConsAcct() {
        return lclQuote.getConsAcct().getAccountno();
    }

    public void setConsAcct(String consAcct) {
        if (CommonUtils.isNotEmpty(consAcct)) {
            lclQuote.setConsAcct(new TradingPartner(consAcct));
        } else {
            lclQuote.setConsAcct(null);
        }
    }

    public LclContact getConsContact() {
        return lclQuote.getConsContact();
    }

    public void setConsContact(LclContact consContact) {
        lclQuote.setConsContact(consContact);
    }

    public String getFwdAcct() {
        return lclQuote.getFwdAcct().getAccountno();
    }

    public void setFwdAcct(String fwdAcct) {
        if (CommonUtils.isNotEmpty(fwdAcct)) {
            lclQuote.setFwdAcct(new TradingPartner(fwdAcct));
        } else {
            lclQuote.setFwdAcct(null);
        }
    }

    public LclContact getFwdContact() {
        return lclQuote.getFwdContact();
    }

    public void setFwdContact(LclContact fwdContact) {
        lclQuote.setFwdContact(fwdContact);
    }
    private LclFileNumber lclFileNumber;

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public String getNotyAcct() {
        return lclQuote.getNotyAcct().getAccountno();
    }

    public void setNotyAcct(String notyAcct) {
        if (CommonUtils.isNotEmpty(notyAcct)) {
            lclQuote.setNotyAcct(new TradingPartner(notyAcct));
        } else {
            lclQuote.setNotyAcct(null);
        }
    }

    public LclContact getNotyContact() {
        return lclQuote.getNotyContact();
    }

    public void setNotyContact(LclContact notyContact) {
        lclQuote.setNotyContact(notyContact);
    }

    public String getShipAcct() {
        return lclQuote.getShipAcct().getAccountno();
    }

    public void setShipAcct(String shipAcct) {
        if (CommonUtils.isNotEmpty(shipAcct)) {
            lclQuote.setShipAcct(new TradingPartner(shipAcct));
        } else {
            lclQuote.setShipAcct(null);
        }
    }

    public LclContact getShipContact() {
        return lclQuote.getShipContact();
    }

    public void setShipContact(LclContact shipContact) {
        lclQuote.setShipContact(shipContact);
    }

    public String getSupAcct() {
        return lclQuote.getSupAcct().getAccountno();
    }

    public void setSupAcct(String supAcct) {
        if (CommonUtils.isNotEmpty(supAcct)) {
            lclQuote.setSupAcct(new TradingPartner(supAcct));
        } else {
            lclQuote.setSupAcct(null);
        }
    }

    public LclContact getSupContact() {
        return lclQuote.getSupContact();
    }

    public String getStdRateBasis() {
        return stdRateBasis;
    }

    public void setStdRateBasis(String stdRateBasis) {
        this.stdRateBasis = stdRateBasis;
    }

    public void setSupContact(LclContact supContact) {
        lclQuote.setSupContact(supContact);
    }

    public RefTerminal getTerminal() {
        return lclQuote.getBillingTerminal();
    }

    public void setTerminal(RefTerminal billingTerminal) {
        lclQuote.setBillingTerminal(billingTerminal);
    }

    public String getClientAcct() {
        return lclQuote.getClientAcct().getAccountno();
    }

    public void setClientAcct(String clientAcct) {
        if (CommonUtils.isNotEmpty(clientAcct)) {
            lclQuote.setClientAcct(new TradingPartner(clientAcct));
        } else {
            lclQuote.setClientAcct(null);
        }
    }

    public String getEciVoyage1() {
        return eciVoyage1;
    }

    public void setEciVoyage1(String eciVoyage1) {
        this.eciVoyage1 = eciVoyage1;

    }

    public String getSailDate1() {
        return sailDate1;

    }

    public void setSailDate1(String sailDate1) {
        this.sailDate1 = sailDate1;

    }

    public String getSsLine1() {
        return ssLine1;

    }

    public void setSsLine1(String ssLine1) {
        this.ssLine1 = ssLine1;

    }

    public String getSsVoyage1() {
        return ssVoyage1;

    }

    public void setSsVoyage1(String ssVoyage1) {
        this.ssVoyage1 = ssVoyage1;

    }

    public String getVesselName1() {
        return vesselName1;

    }

    public void setVesselName1(String vesselName1) {
        this.vesselName1 = vesselName1;

    }

    public String getClientCompany() {
        return lclQuote.getClientContact().getCompanyName();
    }

    public void setClientCompany(String clientCompany) {
        lclQuote.getClientContact().setCompanyName(clientCompany);
    }

    public String getTempClientCompany() {
        return tempClientCompany;
    }

    public void setTempClientCompany(String tempClientCompany) {
        this.tempClientCompany = tempClientCompany;
    }

    public Boolean getCfcl() {
        return cfcl;
    }

    public void setCfcl(Boolean cfcl) {
        this.cfcl = cfcl;
    }

    public Boolean getAesBy() {
        return aesBy;
    }

    public void setAesBy(Boolean aesBy) {
        this.aesBy = aesBy;
    }

    public String getCommodityNumber() {
        return commodityNumber;
    }

    public void setCommodityNumber(String commodityNumber) {
        this.commodityNumber = commodityNumber;
    }

    public String getFmcNumber() {
        return fmcNumber;
    }

    public void setFmcNumber(String fmcNumber) {
        this.fmcNumber = fmcNumber;
    }

    public Lcl3pRefNo getLcl3pRefNo() {
        return lcl3pRefNo;
    }

    public void setLcl3pRefNo(Lcl3pRefNo lcl3pRefNo) {
        this.lcl3pRefNo = lcl3pRefNo;
    }

    public String getOtiNumber() {
        return this.otiNumber;
    }

    public void setOtiNumber(String otiNumber) {
        this.otiNumber = otiNumber;
    }

    public String getDoorMovePickupCts() {
        return doorMovePickupCts;
    }

    public void setDoorMovePickupCts(String doorMovePickupCts) {
        this.doorMovePickupCts = doorMovePickupCts;
    }

    public String getPickupYesNo() {
        if (CommonUtils.isEmpty(pickupYesNo)) {
            return N;
        }
        return pickupYesNo;
    }

    public void setPickupYesNo(String pickupYesNo) {
        this.pickupYesNo = pickupYesNo;
    }

    public String getAesException() {
        return aesException;
    }

    public void setAesException(String aesException) {
        this.aesException = aesException;
    }

    public String getAesItnNumber() {
        return aesItnNumber;
    }

    public void setAesItnNumber(String aesItnNumber) {
        this.aesItnNumber = aesItnNumber;
    }

    public Boolean getCalcHeavy() {
        return calcHeavy;
    }

    public void setCalcHeavy(Boolean calcHeavy) {
        this.calcHeavy = calcHeavy;
    }

    public String getPcBoth() {
        if (lclQuote.getBillingType() != null) {
            return lclQuote.getBillingType();
        }
        return "P";
    }

    public void setPcBoth(String pcBoth) {
        if (CommonUtils.isNotEmpty(pcBoth)) {
            lclQuote.setBillingType(pcBoth);
        }
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getPwk() {
        if (lclQuote.getClientPwkRecvd() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setPwk(String pwk) {
        if (pwk.equals(Y)) {
            lclQuote.setClientPwkRecvd(TRUE);
        } else {
            lclQuote.setClientPwkRecvd(FALSE);
        }
    }

    public String getERT() {
        if (ERT == null) {
            return "N";
        }
        return ERT;
    }

    public void setERT(String ERT) {
        this.ERT = ERT;
    }

    public String getCustomerPo() {
        return customerPo;
    }

    public void setCustomerPo(String customerPo) {
        this.customerPo = customerPo;
    }

    public String getExternalComment() {
        return externalComment;
    }

    public void setExternalComment(String externalComment) {
        this.externalComment = externalComment;
    }

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public String getPortGriRemarks() {
        return portGriRemarks;
    }

    public void setPortGriRemarks(String portGriRemarks) {
        this.portGriRemarks = portGriRemarks;
    }

    public String getUpcomingSailings() {
        return upcomingSailings;
    }

    public void setUpcomingSailings(String upcomingSailings) {
        this.upcomingSailings = upcomingSailings;
    }

    public String getWareHouseDoc() {
        return wareHouseDoc;
    }

    public void setWareHouseDoc(String wareHouseDoc) {
        this.wareHouseDoc = wareHouseDoc;
    }

    public String getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(String agentInfo) {
        this.agentInfo = agentInfo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getFdEta1() {
        return fdEta1;
    }

    public void setFdEta1(String fdEta1) {
        this.fdEta1 = fdEta1;
    }

    public String getApproximateDue() {
        return approximateDue;
    }

    public void setApproximateDue(String approximateDue) {
        this.approximateDue = approximateDue;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDoorOriginCityZip() {
        if (CommonUtils.isNotEmpty(this.manualDoorOriginCityZip)) {
            this.doorOriginCityZip = this.manualDoorOriginCityZip;
        }
        return doorOriginCityZip;
    }

    public void setDoorOriginCityZip(String doorOriginCityZip) {
        this.doorOriginCityZip = doorOriginCityZip;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getEciVoyage() {
        return eciVoyage;
    }

    public void setEciVoyage(String eciVoyage) {
        this.eciVoyage = eciVoyage;
    }

    public String getHarmonizedCode() {
        return harmonizedCode;
    }

    public void setHarmonizedCode(String harmonizedCode) {
        this.harmonizedCode = harmonizedCode;

    }

    public String getFdEta() throws Exception {
        if (lclQuote.getFdEta() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclQuote.getFdEta());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setFdEta(String fdEta) throws Exception {
        if (CommonUtils.isNotEmpty(fdEta)) {
            lclQuote.setFdEta(DateUtils.parseDate(fdEta, "dd-MMM-yyyy"));
        }
    }

    public String getPooLrdt() throws Exception {
        if (lclQuote.getPooWhseLrdt() != null) {
            String d = DateUtils.formatDate(lclQuote.getPooWhseLrdt(), "dd-MMM-yyyy hh:mm");
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPooLrdt(String pooLrdt) throws Exception {
        if (CommonUtils.isNotEmpty(pooLrdt)) {
            lclQuote.setPooWhseLrdt(DateUtils.parseDate(pooLrdt, "dd-MMM-yyyy"));
        }
    }

    public String getOsd() {
        return osd;
    }

    public void setOsd(String osd) {
        this.osd = osd;
    }

    public String getPiece() {
        return piece;

    }

    public void setPiece(String piece) {
        this.piece = piece;

    }

    public String getPro() {
        return pro;

    }

    public void setPro(String pro) {
        this.pro = pro;

    }

    public String getRelayOvr() {
        return relayOvr;

    }

    public void setRelayOvr(String relayOvr) {
        this.relayOvr = relayOvr;

    }

    public String getRemarksForLoading() {
        return remarksForLoading;

    }

    public void setRemarksForLoading(String remarksForLoading) {
        this.remarksForLoading = remarksForLoading;

    }

    public String getSailDate() {
        return sailDate;

    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;

    }

    public String getSsLine() {
        return ssLine;
    }

    public void setSsLine(String ssLine) {
        this.ssLine = ssLine;
    }

    public String getSsVoyage() {
        return ssVoyage;
    }

    public void setSsVoyage(String ssVoyage) {
        this.ssVoyage = ssVoyage;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public String getStowedBy() {
        return stowedBy;
    }

    public void setStowedBy(String stowedBy) {
        this.stowedBy = stowedBy;
    }

    public String getTermToDoBl() {
        return termToDoBl;
    }

    public void setTermToDoBl(String termToDoBl) {
        this.termToDoBl = termToDoBl;
    }

    public String getTruckLine() {
        return truckLine;
    }

    public void setTruckLine(String truckLine) {
        this.truckLine = truckLine;
    }

    public String getValueOfGoods() {
        if (lclQuote.getValueOfGoods() != null) {
            return "" + lclQuote.getValueOfGoods();
        }
        return "";
    }

    public void setValueOfGoods(String valueOfGoods) {
        if (CommonUtils.isNotEmpty(valueOfGoods)) {
            lclQuote.setValueOfGoods(new BigDecimal(valueOfGoods));
        } else {
            lclQuote.setValueOfGoods(null);
        }
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getWeighedBy() {
        return weighedBy;
    }

    public void setWeighedBy(String weighedBy) {
        this.weighedBy = weighedBy;
    }

    public String getCargoRecd() {
        return cargoRecd;
    }

    public void setCargoRecd(String cargoRecd) {
        this.cargoRecd = cargoRecd;
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getNcmNo() {
        return ncmNo;
    }

    public void setNcmNo(String ncmNo) {
        this.ncmNo = ncmNo;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getInbond() {
        return inbond;
    }

    public void setInbond(String inbond) {
        this.inbond = inbond;
    }
    private String portOfOrigin;
    private String portOfLoading;
    private String portOfDestination;
    private String finalDestination;
    private String billForm;
    private String thirdPartyName;
    private String thirdPartyAccount;
    private String trmnum;

    public String getFinalDestination() {
        if (lclQuote.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName()) && null != lclQuote.getFinalDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName() + "/" + lclQuote.getFinalDestination().getStateId().getCode() + '(' + lclQuote.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName()) && null != lclQuote.getFinalDestination().getCountryId()
                    && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName() + "/" + lclQuote.getFinalDestination().getCountryId().getCodedesc() + '(' + lclQuote.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName()).append("'('").append(lclQuote.getFinalDestination().getUnLocationCode()).append("')'");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public Integer getFinalDestinationId() {
        if (lclQuote.getFinalDestination() != null) {
            return lclQuote.getFinalDestination().getId();
        } else {
            return null;
        }
    }

    public void setFinalDestinationId(Integer finalDestinationId) throws Exception {
        if (CommonUtils.isNotEmpty(finalDestinationId)) {
            lclQuote.setFinalDestination(new UnLocationDAO().findById(finalDestinationId));
        }
    }

    public String getPortOfDestination() {
        if (lclQuote.getPortOfDestination() != null) {
            return lclQuote.getPortOfDestination().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setPortOfDestination(String portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public Integer getPortOfDestinationId() {
        if (lclQuote.getPortOfDestination() != null) {
            return lclQuote.getPortOfDestination().getId();
        } else {
            return null;
        }
    }

    public void setPortOfDestinationId(Integer portOfDestinationId) throws Exception {
        if (null != portOfDestinationId && CommonUtils.isNotEmpty(portOfDestinationId)) {
            lclQuote.setPortOfDestination(new UnLocationDAO().findById(portOfDestinationId));
        } else if ("Y".equalsIgnoreCase(transshipType) && CommonUtils.isNotEmpty(tFinalDestinationId)) {
                lclQuote.setPortOfDestination(new UnLocationDAO().findById(Integer.parseInt(tFinalDestinationId)));
        } else {
            lclQuote.setPortOfDestination(null);
        }
    }

    public String getPortOfLoading() {
        if (lclQuote.getPortOfLoading() != null) {
            return lclQuote.getPortOfLoading().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public Integer getPortOfLoadingId() {
        if (lclQuote.getPortOfLoading() != null) {
            return lclQuote.getPortOfLoading().getId();
        } else {
            return null;
        }
    }

    public void setPortOfLoadingId(Integer portOfLoadingId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfLoadingId)) {
            lclQuote.setPortOfLoading(new UnLocationDAO().findById(portOfLoadingId));
        }
    }

    public String getPortOfOrigin() {
        if (lclQuote.getPortOfOrigin() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName()) && null != lclQuote.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getStateId().getCode()) && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclQuote.getPortOfOrigin().getUnLocationName() + "/" + lclQuote.getPortOfOrigin().getStateId().getCode() + "(" + lclQuote.getPortOfOrigin().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())
                    && null != lclQuote.getPortOfOrigin().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getCountryId().getCodedesc())) {
                builder.append(lclQuote.getPortOfOrigin().getUnLocationName() + "/" + lclQuote.getPortOfOrigin().getCountryId().getCodedesc() + "(" + lclQuote.getPortOfOrigin().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclQuote.getPortOfOrigin().getUnLocationName() + "(" + lclQuote.getPortOfOrigin().getUnLocationCode() + ")");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public void setPortOfOrigin(String portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
    }

    public Integer getPortOfOriginId() {
        if (lclQuote.getPortOfOrigin() != null) {
            return lclQuote.getPortOfOrigin().getId();
        } else {
            return null;
        }
    }

    public void setPortOfOriginId(Integer portOfOriginId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfOriginId)) {
            lclQuote.setPortOfOrigin(new UnLocationDAO().findById(portOfOriginId));
        } else if ("Y".equalsIgnoreCase(transshipType) && CommonUtils.isNotEmpty(tPortOfLoadingId)) {
                lclQuote.setPortOfOrigin(new UnLocationDAO().findById(Integer.parseInt(tPortOfLoadingId)));
        } else {
            lclQuote.setPortOfOrigin(null);
        }
    }

    public String getBillForm() {
        if (!"B".equalsIgnoreCase(lclQuote.getBillingType()) && CommonUtils.isNotEmpty(lclQuote.getBillToParty())) {
            return lclQuote.getBillToParty();
        } else if ("B".equalsIgnoreCase(lclQuote.getBillingType())) {
            return null;
        }
        return "F";
    }

    public void setBillForm(String billForm) {
        lclQuote.setBillToParty(billForm);
    }

    public String getThirdPartyName() {
        if (lclQuote.getThirdPartyAcct() != null) {
            return lclQuote.getThirdPartyAcct().getAccountName();
        }
        return null;
    }

    public void setThirdPartyName(String thirdPartyName) {
        if (lclQuote.getThirdPartyAcct() != null && CommonUtils.isNotEmpty(thirdPartyName)) {
            lclQuote.getThirdPartyAcct().setAccountName(thirdPartyName);
        }
    }

    public String getThirdPartyAccount() {
        if (lclQuote.getThirdPartyAcct() != null) {
            return lclQuote.getThirdPartyAcct().getAccountno();
        }
        return null;
    }

    public void setThirdPartyAccount(String thirdPartyAccount) {
        if (CommonUtils.isNotEmpty(thirdPartyAccount)) {
            lclQuote.setThirdPartyAcct(new TradingPartner(thirdPartyAccount));
        } else {
            lclQuote.setThirdPartyAcct(null);
        }
    }
    private Long CustId;
    private String unlocationCode;

    public String getUnlocationCode() {
        if (lclQuote.getFinalDestination() != null) {
            return lclQuote.getFinalDestination().getUnLocationCode();
        } else {
            return "";
        }
    }

    public void setUnlocationCode(String unlocationCode) {
        this.unlocationCode = unlocationCode;
    }

    public Long getCustId() {
        return CustId;
    }

    public void setCustId(Long CustId) {
        this.CustId = CustId;
    }

    public String getSaveRemarks() {
        return saveRemarks;
    }

    public void setSaveRemarks(String saveRemarks) {
        this.saveRemarks = saveRemarks;
    }

    public String getClientWithConsignee() {
        return clientWithConsignee;
    }

    public void setClientWithConsignee(String clientWithConsignee) {
        this.clientWithConsignee = clientWithConsignee;
    }

    public String getClientWithoutConsignee() {
        return clientWithoutConsignee;
    }

    public void setClientWithoutConsignee(String clientWithoutConsignee) {
        this.clientWithoutConsignee = clientWithoutConsignee;
    }

    public String getPooDoor() {
        if (lclQuote.getPooDoor() != null && lclQuote.getPooDoor().equals(TRUE)) {
            return Y;
        }
        return N;
    }

    public void setPooDoor(String pooDoor) {
        if (pooDoor.equals(Y)) {
            lclQuote.setPooDoor(TRUE);
        } else {
            lclQuote.setPooDoor(FALSE);
        }
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        showFullRelay = false;
        showFullRelayFd = false;
        String parentId = request.getParameter("fileNumberId");
        transshipType = request.getParameter("transShipMent");
        tPortOfLoadingId = request.getParameter("portOfLoadingId");
        tFinalDestinationId = request.getParameter("finalDestinationId");
        if (CommonUtils.isNotEmpty(parentId)) {
            try {
                lclQuote = new LCLQuoteDAO().findById(Long.parseLong(parentId));
                new LCLQuoteDAO().getCurrentSession().evict(lclQuote);
                lclQuoteImport = new LclQuoteImportDAO().findById(Long.parseLong(parentId));
            } catch (Exception ex) {
                log.info("reset() failed on " + new Date(), ex);
            }
        }
        if (lclQuoteImport == null) {
            lclQuoteImport = new LclQuoteImport();
        }
    }

    public void setEnums(String transhipment) {
        if ("Y".equalsIgnoreCase(transhipment)) {
            lclQuote.setQuoteType(LCL_TRANSHIPMENT_TYPE);
        } else if (LCL_IMPORT.equalsIgnoreCase(this.moduleName)) {
            lclQuote.setQuoteType(LCL_IMPORT_TYPE);
        } else {
            lclQuote.setQuoteType(LCL_EXPORT_TYPE);
        }
        lclQuote.setTransMode(LCL_TRANSMODE_R);
        lclQuote.setOverrideDimType(LCL_OVERRIDEDIM_TYPE_I);
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getPolCode() {
        return polCode;
    }

    public void setPolCode(String polCode) {
        this.polCode = polCode;
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

    public String getScreenType() {
        return "Q";
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getPolUnlocationcode() {
        return polUnlocationcode;
    }

    public void setPolUnlocationcode(String polUnlocationcode) {
        this.polUnlocationcode = polUnlocationcode;
    }

    public String getExpressReleaseClasuse() {
        return ExpressReleaseClasuse;
    }

    public void setExpressReleaseClasuse(String ExpressReleaseClasuse) {
        this.ExpressReleaseClasuse = ExpressReleaseClasuse;
    }

    public String getFreightRelease() {
        return FreightRelease;
    }

    public void setFreightRelease(String FreightRelease) {
        this.FreightRelease = FreightRelease;
    }

    public String getCfsTerminal() {
        return cfsTerminal;
    }

    public void setCfsTerminal(String cfsTerminal) {
        this.cfsTerminal = cfsTerminal;
    }

    public String getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public LclQuoteImport getLclQuoteImport() {
        return lclQuoteImport;
    }

    public void setLclQuoteImport(LclQuoteImport lclQuoteImport) {
        this.lclQuoteImport = lclQuoteImport;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getStGeorgeAccount() {
        return stGeorgeAccount;
    }

    public void setStGeorgeAccount(String stGeorgeAccount) {
        this.stGeorgeAccount = stGeorgeAccount;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getLastFd() {
        return lastFd;
    }

    public void setLastFd(String lastFd) {
        this.lastFd = lastFd;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getTransShipMent() {
        if (lclQuoteImport.getTransShipment() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setTransShipMent(String transShipMent) {
        if (transShipMent.equals("Y")) {
            lclQuoteImport.setTransShipment(TRUE);
        } else {
            lclQuoteImport.setTransShipment(FALSE);
        }
    }

    public String getItPort() {
        return itPort;
    }

    public void setItPort(String itPort) {
        this.itPort = itPort;
    }

    public String getRelaySearch() {
        return relaySearch;
    }

    public void setRelaySearch(String relaySearch) {
        this.relaySearch = relaySearch;
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

    public boolean isUps() {
        return ups;
    }

    public void setUps(boolean ups) {
        this.ups = ups;
    }

    public String getSpotRateCommNo() {
        return spotRateCommNo;
    }

    public void setSpotRateCommNo(String spotRateCommNo) {
        this.spotRateCommNo = spotRateCommNo;
    }

    public String getBookingContact() {
        return bookingContact;
    }

    public void setBookingContact(String bookingContact) {
        this.bookingContact = bookingContact;
    }

    public String getBookingContactEmail() {
        return bookingContactEmail;
    }

    public void setBookingContactEmail(String bookingContactEmail) {
        this.bookingContactEmail = bookingContactEmail;
    }

    public String getBookingHsCode() {
        return bookingHsCode;
    }

    public void setBookingHsCode(String bookingHsCode) {
        this.bookingHsCode = bookingHsCode;
    }

    public String getBookingHsCodeId() {
        return bookingHsCodeId;
    }

    public void setBookingHsCodeId(String bookingHsCodeId) {
        this.bookingHsCodeId = bookingHsCodeId;
    }

    public String getHsCodeWeightMetric() {
        return hsCodeWeightMetric;
    }

    public void setHsCodeWeightMetric(String hsCodeWeightMetric) {
        this.hsCodeWeightMetric = hsCodeWeightMetric;
    }

    public String getHsCodepiece() {
        return hsCodepiece;
    }

    public void setHsCodepiece(String hsCodepiece) {
        this.hsCodepiece = hsCodepiece;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(String packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getManualCompanyName() {
        return manualCompanyName;
    }

    public void setManualCompanyName(String manualCompanyName) {
        this.manualCompanyName = manualCompanyName;
    }

    public String getPaddress() {
        return paddress;
    }

    public void setPaddress(String paddress) {
        this.paddress = paddress;
    }

    public String getPcommodityDesc() {
        return pcommodityDesc;
    }

    public void setPcommodityDesc(String pcommodityDesc) {
        this.pcommodityDesc = pcommodityDesc;
    }

    public String getPcontactName() {
        return pcontactName;
    }

    public void setPcontactName(String pcontactName) {
        this.pcontactName = pcontactName;
    }

    public String getPemail1() {
        return pemail1;
    }

    public void setPemail1(String pemail1) {
        this.pemail1 = pemail1;
    }

    public String getPfax1() {
        return pfax1;
    }

    public void setPfax1(String pfax1) {
        this.pfax1 = pfax1;
    }

    public String getPickupCost() {
        return pickupCost;
    }

    public void setPickupCost(String pickupCost) {
        this.pickupCost = pickupCost;
    }

    public String getPickupCutoffDate() {
        return pickupCutoffDate;
    }

    public void setPickupCutoffDate(String pickupCutoffDate) {
        this.pickupCutoffDate = pickupCutoffDate;
    }

    public String getPickZip() {
        return pickZip;
    }

    public void setPickZip(String pickZip) {
        this.pickZip = pickZip;
    }
    

    public String getPickupHours() {
        return pickupHours;
    }

    public void setPickupHours(String pickupHours) {
        this.pickupHours = pickupHours;
    }

    public String getPickupInstructions() {
        return pickupInstructions;
    }

    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }

    public String getPickupReadyDate() {
        return pickupReadyDate;
    }

    public void setPickupReadyDate(String pickupReadyDate) {
        this.pickupReadyDate = pickupReadyDate;
    }

    public String getPickupReadyNote() {
        return pickupReadyNote;
    }

    public void setPickupReadyNote(String pickupReadyNote) {
        this.pickupReadyNote = pickupReadyNote;
    }

    public String getPickupReferenceNo() {
        return pickupReferenceNo;
    }

    public void setPickupReferenceNo(String pickupReferenceNo) {
        this.pickupReferenceNo = pickupReferenceNo;
    }

    public String getPphone1() {
        return pphone1;
    }

    public void setPphone1(String pphone1) {
        this.pphone1 = pphone1;
    }

    public String getPtrmnum() {
        return ptrmnum;
    }

    public void setPtrmnum(String ptrmnum) {
        this.ptrmnum = ptrmnum;
    }

    public String getScacCode() {
        return scacCode;
    }

    public void setScacCode(String scacCode) {
        this.scacCode = scacCode;
    }

    public String getShipSupplier() {
        return shipSupplier;
    }

    public void setShipSupplier(String shipSupplier) {
        this.shipSupplier = shipSupplier;
    }

    public String getSpAcct() {
        return spAcct;
    }

    public void setSpAcct(String spAcct) {
        this.spAcct = spAcct;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getWhseAddress() {
        return whseAddress;
    }

    public void setWhseAddress(String whseAddress) {
        this.whseAddress = whseAddress;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getRetailCommodity() {
        return retailCommodity;
    }

    public void setRetailCommodity(String retailCommodity) {
        this.retailCommodity = retailCommodity;
    }

    public String getWhseCity() {
        return whseCity;
    }

    public void setWhseCity(String whseCity) {
        this.whseCity = whseCity;
    }

    public String getWhsePhone() {
        return whsePhone;
    }

    public void setWhsePhone(String whsePhone) {
        this.whsePhone = whsePhone;
    }

    public String getWhseState() {
        return whseState;
    }

    public void setWhseState(String whseState) {
        this.whseState = whseState;
    }

    public String getWhseZip() {
        return whseZip;
    }

    public void setWhseZip(String whseZip) {
        this.whseZip = whseZip;
    }

    public String getWhsecompanyName() {
        return whsecompanyName;
    }

    public void setWhsecompanyName(String whsecompanyName) {
        this.whsecompanyName = whsecompanyName;
    }

    public String getLcl3pRefId() {
        return lcl3pRefId;
    }

    public void setLcl3pRefId(String lcl3pRefId) {
        this.lcl3pRefId = lcl3pRefId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getClientAcctForDr() {
        return clientAcctForDr;
    }

    public String getRateTypeForDr() {
        return rateTypeForDr;
    }

    public void setRateTypeForDr(String rateTypeForDr) {
        this.rateTypeForDr = rateTypeForDr;
    }

    public void setClientAcctForDr(String clientAcctForDr) {
        this.clientAcctForDr = clientAcctForDr;
    }

    public String getClientCompanyForDr() {
        return clientCompanyForDr;
    }

    public void setClientCompanyForDr(String clientCompanyForDr) {
        this.clientCompanyForDr = clientCompanyForDr;
    }

    public String getFinalDestinationForDr() {
        return finalDestinationForDr;
    }

    public void setFinalDestinationForDr(String finalDestinationForDr) {
        this.finalDestinationForDr = finalDestinationForDr;
    }

    public String getFinalDestinationIdForDr() {
        return finalDestinationIdForDr;
    }

    public void setFinalDestinationIdForDr(String finalDestinationIdForDr) {
        this.finalDestinationIdForDr = finalDestinationIdForDr;
    }

    public String getPortOfOriginForDr() {
        return portOfOriginForDr;
    }

    public void setPortOfOriginForDr(String portOfOriginForDr) {
        this.portOfOriginForDr = portOfOriginForDr;
    }

    public String getPortOfOriginIdForDr() {
        return portOfOriginIdForDr;
    }

    public void setPortOfOriginIdForDr(String portOfOriginIdForDr) {
        this.portOfOriginIdForDr = portOfOriginIdForDr;
    }

    public String getCommodityNoForDr() {
        return commodityNoForDr;
    }

    public void setCommodityNoForDr(String commodityNoForDr) {
        this.commodityNoForDr = commodityNoForDr;
    }

    public String getCommodityTypeForDr() {
        return commodityTypeForDr;
    }

    public void setCommodityTypeForDr(String commodityTypeForDr) {
        this.commodityTypeForDr = commodityTypeForDr;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getPackageDr() {
        return packageDr;
    }

    public void setPackageDr(String packageDr) {
        this.packageDr = packageDr;
    }

    public String getPackageIdDr() {
        return packageIdDr;
    }

    public void setPackageIdDr(String packageIdDr) {
        this.packageIdDr = packageIdDr;
    }

    public String getOriginUnlocationCodeForDr() {
        return originUnlocationCodeForDr;
    }

    public void setOriginUnlocationCodeForDr(String originUnlocationCodeForDr) {
        this.originUnlocationCodeForDr = originUnlocationCodeForDr;
    }

    public String getUnlocationCodeForDr() {
        return unlocationCodeForDr;
    }

    public void setUnlocationCodeForDr(String unlocationCodeForDr) {
        this.unlocationCodeForDr = unlocationCodeForDr;
    }

    public String getGenCodefield1() {
        return genCodefield1;
    }

    public void setGenCodefield1(String genCodefield1) {
        this.genCodefield1 = genCodefield1;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getForwarderAddress() {
        return forwarderAddress;
    }

    public void setForwarderAddress(String forwarderAddress) {
        this.forwarderAddress = forwarderAddress;
    }

    public String getNotifyAddress() {
        return notifyAddress;
    }

    public void setNotifyAddress(String notifyAddress) {
        this.notifyAddress = notifyAddress;
    }

    public String getPcBothImports() {
        if (lclQuote.getBillingType() != null) {
            return lclQuote.getBillingType();
        }
        return "C";
    }

    public void setPcBothImports(String pcBothImports) {
        if (CommonUtils.isNotEmpty(pcBothImports)) {
            lclQuote.setBillingType(pcBothImports);
        }
    }

    public String getBilltoCodeImports() {
        if (CommonUtils.isNotEmpty(lclQuote.getBillToParty())) {
            return lclQuote.getBillToParty();
        }
        return "C";
    }

    public void setBilltoCodeImports(String billtoCodeImports) {
        lclQuote.setBillToParty(billtoCodeImports);
    }

    public String getAmsHblNo() {
        return amsHblNo;
    }

    public void setAmsHblNo(String amsHblNo) {
        this.amsHblNo = amsHblNo;
    }

    public String getAmsHblPiece() {
        return amsHblPiece;
    }

    public void setAmsHblPiece(String amsHblPiece) {
        this.amsHblPiece = amsHblPiece;
    }

    public String getDefaultAms() {
        return defaultAms;
    }

    public void setDefaultAms(String defaultAms) {
        this.defaultAms = defaultAms;
    }

    public String getNotify2ContactName() {
        return notify2ContactName;
    }

    public void setNotify2ContactName(String notify2ContactName) {
        this.notify2ContactName = notify2ContactName;
    }

    public String getNotify2AcctNo() {
        return notify2AcctNo;
    }

    public void setNotify2AcctNo(String notify2AcctNo) {
        this.notify2AcctNo = notify2AcctNo;
    }

    public LclContact getNotify2Contact() {
        return lclQuote.getNotify2Contact();
    }

    public void setNotify2Contact(LclContact notify2Contact) {
        lclQuote.setNotify2Contact(notify2Contact);
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public boolean isClientIcon() {
        return clientIcon;
    }

    public void setClientIcon(boolean clientIcon) {
        this.clientIcon = clientIcon;
    }

    public boolean isShipperIcon() {
        return shipperIcon;
    }

    public void setShipperIcon(boolean shipperIcon) {
        this.shipperIcon = shipperIcon;
    }

    public boolean isForwaderIcon() {
        return forwaderIcon;
    }

    public void setForwaderIcon(boolean forwaderIcon) {
        this.forwaderIcon = forwaderIcon;
    }

    public boolean isConsigneeIcon() {
        return consigneeIcon;
    }

    public void setConsigneeIcon(boolean consigneeIcon) {
        this.consigneeIcon = consigneeIcon;
    }

    public boolean isNotifyIcon() {
        return notifyIcon;
    }

    public void setNotifyIcon(boolean notifyIcon) {
        this.notifyIcon = notifyIcon;
    }

    public boolean isNotify2Icon() {
        return notify2Icon;
    }

    public void setNotify2Icon(boolean notify2Icon) {
        this.notify2Icon = notify2Icon;
    }

    public String getShipperToolTip() {
        return shipperToolTip;
    }

    public void setShipperToolTip(String shipperToolTip) {
        this.shipperToolTip = shipperToolTip;
    }

    public String getConsigneeToolTip() {
        return consigneeToolTip;
    }

    public void setConsigneeToolTip(String consigneeToolTip) {
        this.consigneeToolTip = consigneeToolTip;
    }

    public String getNotifyToolTip() {
        return notifyToolTip;
    }

    public void setNotifyToolTip(String notifyToolTip) {
        this.notifyToolTip = notifyToolTip;
    }

    public String getNotify2ToolTip() {
        return notify2ToolTip;
    }

    public void setNotify2ToolTip(String notify2ToolTip) {
        this.notify2ToolTip = notify2ToolTip;
    }

    public String getImportCommodity() {
        return importCommodity;
    }

    public void setImportCommodity(String importCommodity) {
        this.importCommodity = importCommodity;
    }

    public String getShowAllCities() {
        return showAllCities;
    }

    public void setShowAllCities(String showAllCities) {
        this.showAllCities = showAllCities;
    }

    public String getOriginCityZip() {
        return OriginCityZip;
    }

    public void setOriginCityZip(String OriginCityZip) {
        this.OriginCityZip = OriginCityZip;
    }

    public String getOriginalFileNo() {
        return originalFileNo;
    }

    public void setOriginalFileNo(String originalFileNo) {
        this.originalFileNo = originalFileNo;
    }

    public boolean isDestinationServices() throws Exception {
        boolean ischargeAvailable = false;
        if (CommonUtils.isNotEmpty(this.fileNumberId)) {
            ischargeAvailable = new LclQuoteAcDAO().isChargeContainDestinationCharge(Long.parseLong(this.fileNumberId));
        }
        return ischargeAvailable;
    }

    public void setDestinationServices(boolean destinationServices) {
        this.destinationServices = destinationServices;
    }

    public String getInternalRemarksPod() {
        return internalRemarksPod;
    }

    public void setInternalRemarksPod(String internalRemarksPod) {
        this.internalRemarksPod = internalRemarksPod;
    }

    public String getPortGriRemarksPod() {
        return portGriRemarksPod;
    }

    public void setPortGriRemarksPod(String portGriRemarksPod) {
        this.portGriRemarksPod = portGriRemarksPod;
    }

    public String getSpecialRemarksPod() {
        return specialRemarksPod;
    }

    public void setSpecialRemarksPod(String specialRemarksPod) {
        this.specialRemarksPod = specialRemarksPod;
    }

    public String getExternalGriRemarks() {
        return externalGriRemarks;
    }

    public void setExternalGriRemarks(String externalGriRemarks) {
        this.externalGriRemarks = externalGriRemarks;
    }

    public String getFdEciPortCode() {
        return fdEciPortCode;
    }

    public void setFdEciPortCode(String fdEciPortCode) {
        this.fdEciPortCode = fdEciPortCode;
    }

    public String getPodEciPortCode() {
        return podEciPortCode;
    }

    public void setPodEciPortCode(String podEciPortCode) {
        this.podEciPortCode = podEciPortCode;
    }

    public String getPolTrmNum() {
        return polTrmNum;
    }

    public void setPolTrmNum(String polTrmNum) {
        this.polTrmNum = polTrmNum;
    }

    public String getPooTrmNum() {
        return pooTrmNum;
    }

    public void setPooTrmNum(String pooTrmNum) {
        this.pooTrmNum = pooTrmNum;
    }

    public String getFdEngmet() {
        return fdEngmet;
    }

    public void setFdEngmet(String fdEngmet) {
        this.fdEngmet = fdEngmet;
    }

    public String getReportSaveFlag() {
        return reportSaveFlag;
    }

    public void setReportSaveFlag(String reportSaveFlag) {
        this.reportSaveFlag = reportSaveFlag;
    }

    public Integer getPooWhseId() {
        return pooWhseId;
    }

    public void setPooWhseId(Integer pooWhseId) {
        this.pooWhseId = pooWhseId;
    }

    public String getOriginLrdDate() {
        return originLrdDate;
    }

    public void setOriginLrdDate(String originLrdDate) {
        this.originLrdDate = originLrdDate;
    }

    public String getFdEtaDate() {
        return fdEtaDate;
    }

    public void setFdEtaDate(String fdEtaDate) {
        this.fdEtaDate = fdEtaDate;
    }

    public Long getBookedSsHeaderId() {
        return bookedSsHeaderId;
    }

    public void setBookedSsHeaderId(Long bookedSsHeaderId) {
        this.bookedSsHeaderId = bookedSsHeaderId;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getCompanyCode() throws Exception {
        companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (moduleName.equalsIgnoreCase("Imports")) {
            if (CommonUtils.isNotEmpty(fileNumberId)) {
                this.businessUnit = new LclFileNumberDAO().getBusinessUnitInImport(fileNumberId);
            } else {
                this.businessUnit = companyCode.equalsIgnoreCase("03") ? "ECU" : "OTI";
            }
        } else if (null != companyCode) {
            companyCode = companyCode.equalsIgnoreCase("03") ? "ECU" : "OTI";
            this.businessUnit = CommonUtils.isNotEmpty(this.businessUnit)
                    ? this.businessUnit : companyCode;
        }
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEculineCommodity() {
        return eculineCommodity;
    }

    public void setEculineCommodity(String eculineCommodity) {
        this.eculineCommodity = eculineCommodity;
    }

    public boolean isHomeScreenQtFileFlag() {
        return homeScreenQtFileFlag;
    }

    public void setHomeScreenQtFileFlag(boolean homeScreenQtFileFlag) {
        this.homeScreenQtFileFlag = homeScreenQtFileFlag;
    }

    public String getManualDoorOriginCityZip() {
        return manualDoorOriginCityZip;
    }

    public void setManualDoorOriginCityZip(String manualDoorOriginCityZip) {
        this.manualDoorOriginCityZip = manualDoorOriginCityZip;
    }

    public String getTransshipType() {
        return transshipType;
    }

    public void setTransshipType(String transshipType) {
        this.transshipType = transshipType;
    }

    public String gettPortOfLoadingId() {
        return tPortOfLoadingId;
    }

    public void settPortOfLoadingId(String tPortOfLoadingId) {
        this.tPortOfLoadingId = tPortOfLoadingId;
    }

    public String gettFinalDestinationId() {
        return tFinalDestinationId;
    }

    public void settFinalDestinationId(String tFinalDestinationId) {
        this.tFinalDestinationId = tFinalDestinationId;
    }

    public String getSmallParcelRemarks() {
        return smallParcelRemarks;
    }

    public void setSmallParcelRemarks(String smallParcelRemarks) {
        this.smallParcelRemarks = smallParcelRemarks;
    }

    public String getAgentBrand() {
        return agentBrand;
    }

    public void setAgentBrand(String agentBrand) {
        this.agentBrand = agentBrand;
    }

    public String getCifValue() {
          if (lclQuote.getCifValue() != null) {
            return "" + lclQuote.getCifValue();
        }
        return "";
    }

    public void setCifValue(String cifValue) {
        if (CommonUtils.isNotEmpty(cifValue)) {
            lclQuote.setCifValue(new BigDecimal(cifValue));
        } else {
            lclQuote.setCifValue(null);
        }
    }

}
