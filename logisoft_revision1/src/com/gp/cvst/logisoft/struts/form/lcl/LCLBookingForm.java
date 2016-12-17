/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import static com.gp.cong.lcl.common.constant.CommonConstant.FALSE;
import static com.gp.cong.lcl.common.constant.CommonConstant.N;
import static com.gp.cong.lcl.common.constant.CommonConstant.T;
import static com.gp.cong.lcl.common.constant.CommonConstant.TRUE;
import static com.gp.cong.lcl.common.constant.CommonConstant.Y;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
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
public class LCLBookingForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LCLBookingForm.class);
    private LclBooking lclBooking;
    private LclContact pickupContact;
    private LclBookingImport lclBookingImport;
    private String fileNumberId;
    private LclContact lclContact;
    private boolean showFullRelay;
    private boolean showFullRelayFd;
    private String defaultAgent;
    private Long commId;
    private String bookingContact;
    private String bookingContactEmail;
    private String terminal;
    private String copyFnVal;
    private String clientWithConsignee;
    private String clientWithoutConsignee;
    private String clientCompany;
    private String tempClientCompany;
    private String consigneeCredit;
    private String forwarderCredit;
    private String shipperCredit;
    private String clientCredit;
    private String clientPoaClient;
    private String forwarderPoaClient;
    private String clientName;
    private String clientConsigneeCompany;
    private String osdRemarks;
    private String overShortdamaged;
    private String rtdTransaction;
    private String rtdAgentAcct;
    private String origin;
    private String destination;
    private String polCode;
    private String podCode;
    private String pickupCity;
    private String pickupState;
    private String pickupZip;
    private String unlocationCode;
    private String thirdPartyname;
    private String thirdpartyaccountNo;
    private String originUnlocationCode;
    private String deliverCargoTo;
    private String whsCode;
    private String shipper_contactName;
    private String specialRemarks;
    private String originCode;
    private String destinationCode;
    private String internalRemarks;
    private String unlocationName;
    private String podUnlocationcode;
    private String trn;
    private String stGeorgeAddress;
    private String stGeorgeAccountNo;
    private String shpDr;
    private String screenType;
    private String pier;
    private String polUnlocationcode;
    private String basedOnCity;
    private String duplicateDoorOrigin;
    private String index;
    private String fileNumber;
    private String supContactName;
    private String newSupplier;
    private String shipContactName;
    private String newShipper;
    private String consContactName;
    private String newConsignee;
    private String notifyContactName;
    private String newNotify;
    private String PackageType;
    private String bookingHsCode;
    private String bookingHsCodeId;
    private String PackageTypeId;
    private String hsCodePiece;
    private String hsCodeWeightMetric;
    private String newSupplierForDr;
    private String newConsForDr;
    private Integer portExitId;
    private Integer foreignDischargeId;
    private BigDecimal dollarAmount;
    private BigDecimal adjustmentAmount;
    private String documentation;
    private String rateTypes;
    private String deliveryMetroField;
    private String genCodefield1;
    private String acctNo;
    private String checkDRChange;
    // lclBookingPad Details
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
    private String enteredBy;
    /**
     * LclBooking Import Fields
     *
     * @return
     */
    private String transShipMent;
    private String portOfOriginForDr;
    private String shipAcctForDr;
    private String finalDestinationForDr;
    private Integer finalDestinationIdForDr;
    private String shipContactForDr;
    private String consContactForDr;
    private String basedOnCountry;
    private String consAcctForDr;
    private String fwdContactForDr;
    private String fwdAcctForDr;
    private String commodityTypeForDr;
    private String commodityNoForDr;
    private String impVesselArrival;//output
    private String importApproxDue;//output
    private String impStripDate;//output
    private String lastFd;//output
    private String pickupDate;//input
    private String goDate;//input
    private String impEciVoyage;
    private String impSailDate;
    private String impSsLine;
    private String impVesselName;
    private String impPier;
    private String impSsVoyage;
    private String impUnitNo;
    private String impCFSWareName;
    private String impCFSWareaddress;
    private String impUnitWareNo;
    private String impUnitWareaddress;
    private String amsHblNo;
    private String amsHblPiece;
    private String amsHblScac;
    private String defaultAms;
    private Integer defaultPieces;
    private String copyRates;
    private String copyRatesDrNo;
    private String cfsWarehouseCoName;
    private String ipiLoadNo;
    private String pickedUpBy;
    private String importCreditShipper;
    private String importCreditConsignee;
    private String importCreditNotify;
    private String importCreditNotify2;
    /**
     * Misc Properties
     *
     * @return
     */
    private String cfsTerminal;
    private String scac;
    private String hbl;
    private String subHouseBl;
    private String itClass;
    private String itNo;
    private String entryNo;
    private String customsReleaseNo;
    private String stGeorgeAccount;
    private String FreightRelease;
    private String ExpressReleaseClasuse;
    private String module = "";
    private String relaySearch;
    private String difflclBookedDimsActual;
    private String cfclAcctName;
    private String cfclAcctNo;
    private String bookedPieceCount;
    private String bookedVolumeImperial;
    private String bookedWeightImperial;
    private Integer quickdrPortOfOriginId;
    private Long packageIdDr;
    private String packageDr;
    private String labelField;
    private String newConsigneeContact;
    private String newClientContact;
    private String newShipperContact;
    private String newForwarderContact;
    private String consigneeManualContact;
    private String forwarederContactManual;
    private String clientContactManual;
    private String shipperManualContact;
    private String newForwarderForDr;
    private String newConsigneeForDr;
    private Long commodityTypeId;
    private String rateAmount;
    private String measure;
    private String minimum;
    private String spotRateCommNo;
    private String inbondNo;
    private Long lcl3pRefId;
    private String thirdPName;
    private String shipperAddress;
    private String forwarderAddress;
    private String consigneeAddress;
    private String notifyAddress;
    private String supplierAddress;
    private String etaFDDate;
    private String ipiATDDate;
    private String doorDeliveryComment;
    //Import Voyages Id
    private String unitSsId;
    private String headerId;
    private String unitId;
    private String bundleToOf;
    private String relsToInv;
    private String printOnBL;
    private String impSearchFlag;
    private String masterBl;
    private String unitItNo;
    private String unitItDate;
    private String unitItPort;
    private String comno;
    private String flag;
    private String pickupFlag;
    private String currentLocationDojo;
    private String voyageOwner;
    private Integer unLocationId;
    //Export Voyage for goback to particular page option 
    private String pickVoyId;
    private String detailId;
    private String filterByChanges;
    private String backToVoyage;
    private String toScreenName;
    private Long consolidatedId;
    private String fromScreen;
    private String inTransitDr;
    //hidden fields
    private String selectedLrd;
    private String fileNumberStatus;
    private String spReferenceNo;
    private String transType;
    private String moduleName;
    private String thirdpRefNo;
    private String thirdpType;
    //Import Bkg hidden fields
    private String transUpcoming;
    private String transfileId;
    private String unitNo;
    private String unitSize;
    private String impCfsWarehsId;
    private String pdfDocumentName;
    //this hiddenfiels is used to Unit disposition
    private String disposition;
    private String description;
    private String shipUnloCode;
    //transhipment
    private String exportAgentAcctName;
    private String exportAgentAcctNo;
    private String unitStatus;
    //Customer Notes check by Icon
    private boolean shipperIcon;
    private boolean consigneeIcon;
    private boolean notifyIcon;
    private boolean forwaderIcon;
    private boolean clientIcon;
    private boolean notify2Icon;
    private boolean allowVoyageCopy;
    private String notify2ContactName;//manual Notify2Name
    private String notify2AcctNo;
    public String ediData;
    // CFS warehouse
    private String cfsWarehouseNo;
    private String cfsWarehouseAddress;
    private String cfsWarehouseCity;
    private String cfsWarehouseState;
    private String cfsWarehouseZip;
    private String cfsWarehousePhone;
    private String cfsWarehouseFax;
    // Unit warehouse
    private String unitWarehouseCoName;
    private String unitWarehouseAddress;
    private String unitWarehouseCity;
    private String unitWarehouseState;
    private String unitWarehouseZip;
    private String unitWarehousePhone;
    private String unitWarehouseFax;
    private String callBackFlag;//set transhipment flag in homeaction
    private String shipperToolTip;
    private String consigneeToolTip;
    private String notifyToolTip;
    private String notify2ToolTip;
    private String dispositionToolTip;
    private boolean ipiLoadedContainer;
    // for destination Services in Export
    private boolean destinationServices;
    // separate the POD and FD Remarks in LCLExports
    private String specialRemarksPod;
    private String internalRemarksPod;
    private String portGriRemarksPod;
    private String limit;
    private String isFormchangeFlag;
    //For exports Eculine logic
    private String businessUnit;
    private String eculineCommodity;
    private String companyCode;
    //Voyage Search
    private String searchOriginId;
    private String searchFdId;
    private String searchTerminalNo;
    private String searchLoginId;
    private String searchUnitNo;
    private String searchMasterBl;
    private String searchAgentNo;
    private String searchDispoId;
    private String searchVoyageNo;
    private String searchOrigin;
    private String searchFd;
    private String searchTerminal;
    // Transhipment file(copy from pol to origin and copy from fd to pod)
    private String transshipType;
    private String tPortOfLoadingId;
    private String tFinalDestinationId;
    private String pooTrmNum;
    private String polTrmNum;
    private String podEciPortCode;
    private String fdEciPortCode;
    private String fdEngmet;
    private Boolean cob = false;
    private String oldFdUnlocationCode;
    private String storageDatetime;
    private String batchHsCode;
    private String batchFileNo;
    private boolean formChangeFlag;// when close payment button pop-up
    private boolean homeScreenDrFileFlag;
    private String previousSailing;
    private String tabName;
    private String agentBrand;
    private String ediButton;
    private boolean osdValues;
    private String cifValue;
    private String smallParcelRemarks;
    private String saveRemarks;
    private String batchHotCode;
    private String dispoPopUpId;
    private String dispoCityCode;
    private String dispoWareHouse;
    private String dispoDateTime;
    private Boolean cfclForDR = false;
    private String cfclAcctNameForDr;
    private String cfclAcctNoForDr;
    private String unPickedFiles;
    private boolean changeBlCharge;

    public LCLBookingForm() {
        if (lclBooking == null) {
            lclBooking = new LclBooking();
        }
    }

    public String getPreviousSailing() {
        return previousSailing;
    }

    public void setPreviousSailing(String previousSailing) {
        this.previousSailing = previousSailing;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
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

    public void setRateTypes(String rateTypes) {
        this.rateTypes = rateTypes;
    }

    public BigDecimal getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(BigDecimal dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public String getRtdTransaction() {
        if (moduleName.equalsIgnoreCase("Imports") && lclBooking.getRtdTransaction() == null) {
            return N;
        } else if (lclBooking.getRtdTransaction() != null && lclBooking.getRtdTransaction()) {
            return Y;
        } else if (lclBooking.getRtdTransaction() != null && !lclBooking.getRtdTransaction()) {
            return N;
        }
        return "";
    }

    public void setRtdTransaction(String rtdTransaction) {
        if (rtdTransaction.equals("Y")) {
            lclBooking.setRtdTransaction(TRUE);
        } else if (rtdTransaction.equals("N")) {
            lclBooking.setRtdTransaction(FALSE);
        } else {
            lclBooking.setRtdTransaction(null);
        }
    }

    public Long getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(Long commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getNewConsForDr() {
        return newConsForDr;
    }

    public void setNewConsForDr(String newConsForDr) {
        this.newConsForDr = newConsForDr;
    }

    public String getNewSupplierForDr() {
        return newSupplierForDr;
    }

    public void setNewSupplierForDr(String newSupplierForDr) {
        this.newSupplierForDr = newSupplierForDr;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getShpDr() {
        return shpDr;
    }

    public String getPackageDr() {
        return packageDr;
    }

    public void setPackageDr(String packageDr) {
        this.packageDr = packageDr;
    }

    public Long getPackageIdDr() {
        return packageIdDr;
    }

    public void setPackageIdDr(Long packageIdDr) {
        this.packageIdDr = packageIdDr;
    }

    public Integer getQuickdrPortOfOriginId() {
        return quickdrPortOfOriginId;
    }

    public void setQuickdrPortOfOriginId(Integer quickdrPortOfOriginId) {
        this.quickdrPortOfOriginId = quickdrPortOfOriginId;
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

    public String getConsAcctForDr() {
        return consAcctForDr;
    }

    public void setConsAcctForDr(String consAcctForDr) {
        this.consAcctForDr = consAcctForDr;
    }

    public String getConsContactForDr() {
        return consContactForDr;
    }

    public void setConsContactForDr(String consContactForDr) {
        this.consContactForDr = consContactForDr;
    }

    public String getBasedOnCountry() {
        return basedOnCountry;
    }

    public void setBasedOnCountry(String basedOnCountry) {
        this.basedOnCountry = basedOnCountry;
    }

    public String getFinalDestinationForDr() {
        return finalDestinationForDr;
    }

    public void setFinalDestinationForDr(String finalDestinationForDr) {
        this.finalDestinationForDr = finalDestinationForDr;
    }

    public Integer getFinalDestinationIdForDr() {
        return finalDestinationIdForDr;
    }

    public void setFinalDestinationIdForDr(Integer finalDestinationIdForDr) {
        this.finalDestinationIdForDr = finalDestinationIdForDr;
    }

    public String getForwarderPoaClient() {
        return forwarderPoaClient;
    }

    public void setForwarderPoaClient(String forwarderPoaClient) {
        this.forwarderPoaClient = forwarderPoaClient;
    }

    public String getFwdAcctForDr() {
        return fwdAcctForDr;
    }

    public void setFwdAcctForDr(String fwdAcctForDr) {
        this.fwdAcctForDr = fwdAcctForDr;
    }

    public String getShipperManualContact() {
        return shipperManualContact;
    }

    public void setShipperManualContact(String shipperManualContact) {
        this.shipperManualContact = shipperManualContact;
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

    public String getCopyFnVal() {
        return copyFnVal;
    }

    public void setCopyFnVal(String copyFnVal) {
        this.copyFnVal = copyFnVal;
    }

    public String getFwdContactForDr() {
        return fwdContactForDr;
    }

    public void setFwdContactForDr(String fwdContactForDr) {
        this.fwdContactForDr = fwdContactForDr;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public LclContact getPickupContact() {
        return pickupContact;
    }

    public void setPickupContact(LclContact pickupContact) {
        this.pickupContact = pickupContact;
    }

    public String getPortOfOriginForDr() {
        return portOfOriginForDr;
    }

    public void setPortOfOriginForDr(String portOfOriginForDr) {
        this.portOfOriginForDr = portOfOriginForDr;
    }

    public String getShipAcctForDr() {
        return shipAcctForDr;
    }

    public void setShipAcctForDr(String shipAcctForDr) {
        this.shipAcctForDr = shipAcctForDr;
    }

    public String getShipContactForDr() {
        return shipContactForDr;
    }

    public void setShipContactForDr(String shipContactForDr) {
        this.shipContactForDr = shipContactForDr;
    }

    public void setShpDr(String shpDr) {
        this.shpDr = shpDr;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public String getClientPoaClient() {
        return clientPoaClient;
    }

    public void setClientPoaClient(String clientPoaClient) {
        this.clientPoaClient = clientPoaClient;
    }

    public String getClientCredit() {
        return clientCredit;
    }

    public void setClientCredit(String clientCredit) {
        this.clientCredit = clientCredit;
    }

    public String getConsigneeCredit() {
        return consigneeCredit;
    }

    public void setConsigneeCredit(String consigneeCredit) {
        this.consigneeCredit = consigneeCredit;
    }

    public String getForwarderCredit() {
        return forwarderCredit;
    }

    public void setForwarderCredit(String forwarderCredit) {
        this.forwarderCredit = forwarderCredit;
    }

    public String getShipperCredit() {
        return shipperCredit;
    }

    public void setShipperCredit(String shipperCredit) {
        this.shipperCredit = shipperCredit;
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

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getPickupState() {
        return pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    public String getSupContactName() {
        return supContactName;
    }

    public void setSupContactName(String supContactName) {
        this.supContactName = supContactName;
    }

    public String getNewSupplier() {
        return newSupplier;
    }

    public void setNewSupplier(String newSupplier) {
        this.newSupplier = newSupplier;
    }

    public String getNewShipper() {
        return newShipper;
    }

    public void setNewShipper(String newShipper) {
        this.newShipper = newShipper;
    }

    public String getShipContactName() {
        return shipContactName;
    }

    public void setShipContactName(String shipContactName) {
        this.shipContactName = shipContactName;
    }

    public String getConsContactName() {
        return consContactName;
    }

    public void setConsContactName(String consContactName) {
        this.consContactName = consContactName;
    }

    public String getNewConsignee() {
        return newConsignee;
    }

    public void setNewConsignee(String newConsignee) {
        this.newConsignee = newConsignee;
    }

    public String getNewNotify() {
        return newNotify;
    }

    public void setNewNotify(String newNotify) {
        this.newNotify = newNotify;
    }

    public String getNotifyContactName() {
        return notifyContactName;
    }

    public void setNotifyContactName(String notifyContactName) {
        this.notifyContactName = notifyContactName;
    }

    public String getPickupZip() {
        return pickupZip;
    }

    public void setPickupZip(String pickupZip) {
        this.pickupZip = pickupZip;
    }

    public String getDuplicateDoorOrigin() {
        return duplicateDoorOrigin;
    }

    public void setDuplicateDoorOrigin(String duplicateDoorOrigin) {
        this.duplicateDoorOrigin = duplicateDoorOrigin;
    }

    public String getShipper_contactName() {
        return shipper_contactName;
    }

    public void setShipper_contactName(String shipper_contactName) {
        this.shipper_contactName = shipper_contactName;
    }

    public String getOverShortdamaged() {
        if (lclBooking.getOverShortdamaged() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setOverShortdamaged(String overShortdamaged) {
        if (overShortdamaged.equals("Y")) {
            lclBooking.setOverShortdamaged(TRUE);
        } else {
            lclBooking.setOverShortdamaged(FALSE);
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
        return lclBooking.getClientContact().getCity();
    }

    public void setCity(String city) {
        lclBooking.getClientContact().setCity(city);
    }

    public String getState() {
        return lclBooking.getClientContact().getState();
    }

    public void setState(String state) {
        lclBooking.getClientContact().setState(state);
    }

    public String getZip() {
        return lclBooking.getClientContact().getZip();
    }

    public void setZip(String zip) {
        lclBooking.getClientContact().setZip(zip);
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public LclBooking getLclBooking() {
        if (lclBooking == null) {
            lclBooking = new LclBooking();
        }
        return lclBooking;
    }

    public void setLclBooking(LclBooking lclBooking) {
        this.lclBooking = lclBooking;
    }

    public LclContact getLclContact() {
        return lclContact;
    }

    public void setLclContact(LclContact lclContact) {
        this.lclContact = lclContact;
    }

    public String getBookingType() {
        return lclBooking.getBookingType();
    }

    public void setBookingType(String bookingType) {
        lclBooking.setBookingType(bookingType);
    }

    public String getTransMode() {
        return lclBooking.getTransMode();
    }

    public void setTransMode(String transMode) {
        if (CommonUtils.isEmpty(transMode)) {
            transMode = T;
        }
        lclBooking.setTransMode(transMode);
    }

    public String getSpotRate() {
        if (lclBooking.getSpotRate() == TRUE) {
            return "Y";
        }
        return "N";
    }

    public void setSpotRate(String spotRate) {
        if (spotRate.equals("Y")) {
            lclBooking.setSpotRate(TRUE);
        } else {
            lclBooking.setSpotRate(FALSE);
        }
    }

    public String getRateType() {
        return lclBooking.getRateType();//in screen name is CTC/Retail/FTF
    }

    public void setRateType(String rateType) {
        if (CommonUtils.isNotEmpty(rateType)) {
            lclBooking.setRateType(rateType);
        }
    }

    public String getRelayOverride() {
        if (lclBooking.getRelayOverride() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setRelayOverride(String relayOverride) {
        if (relayOverride.equals(Y)) {
            lclBooking.setRelayOverride(TRUE);
        } else {
            lclBooking.setRelayOverride(FALSE);
        }
    }

    public String getBillingType() {
        return lclBooking.getBillingType();
    }

    public void setBillingType(String billingType) {
        if (CommonUtils.isEmpty(billingType)) {
            billingType = "B";
        }
        lclBooking.setBillingType(billingType);
    }

    public String getTerminal() {
        if (lclBooking.getTerminal() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getTerminal().getTerminalLocation()) && CommonUtils.isNotEmpty(lclBooking.getTerminal().getTrmnum())) {
                builder.append(lclBooking.getTerminal().getTerminalLocation() + "/" + lclBooking.getTerminal().getTrmnum());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTrmnum() {
        if (lclBooking.getTerminal() != null) {
            return lclBooking.getTerminal().getTrmnum();
        }
        return null;
    }

    public void setTrmnum(String trmnum) throws Exception {
        if (CommonUtils.isNotEmpty(trmnum)) {
            RefTerminal terminal = new RefTerminalDAO().findById(trmnum);
            lclBooking.setTerminal(terminal);
        }
    }

    public String getDefaultAgent() {
        if (lclBooking.getDefaultAgent() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setDefaultAgent(String defaultAgent) {
        if (defaultAgent.equals(Y)) {
            lclBooking.setDefaultAgent(TRUE);
        } else {
            lclBooking.setDefaultAgent(FALSE);
        }
    }

    public String getInsurance() {
        if (lclBooking.getInsurance() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setInsurance(String insurance) {
        if (insurance.equals(Y)) {
            lclBooking.setInsurance(TRUE);
        } else {
            lclBooking.setInsurance(FALSE);
        }
    }

    public String getDocumentation() {
        if (lclBooking.getDocumentation() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setDocumentation(String documentation) {
        if (documentation.equals(Y)) {
            lclBooking.setDocumentation(TRUE);
        } else {
            lclBooking.setDocumentation(FALSE);
        }
    }

    public BigDecimal getDeclaredCargoValue() {
        return lclBooking.getDeclaredCargoValue();
    }

    public void setDeclaredCargoValue(BigDecimal declaredCargoValue) {
        lclBooking.setDeclaredCargoValue(declaredCargoValue);
    }

    public String getSupReference() {
        if (lclBooking.getSupReference() != null) {
            return "" + lclBooking.getSupReference();
        }
        return "";
    }

    public void setSupReference(String supReference) {
        if (CommonUtils.isNotEmpty(supReference)) {
            lclBooking.setSupReference(supReference);
        } else {
            lclBooking.setSupReference(null);
        }
    }

    public String getShipReference() {
        if (lclBooking.getShipReference() != null) {
            return "" + lclBooking.getShipReference();
        }
        return "";
    }

    public void setShipReference(String shipReference) {
        if (CommonUtils.isNotEmpty(shipReference)) {
            lclBooking.setShipReference(shipReference);
        } else {
            lclBooking.setShipReference(null);
        }
    }

    public String getFwdFmcNo() {
        if (lclBooking.getFwdFmcNo() != null) {
            return "" + lclBooking.getFwdFmcNo();
        }
        return "";
    }

    public void setFwdFmcNo(String fwdFmcNo) {
        if (CommonUtils.isNotEmpty(fwdFmcNo)) {
            lclBooking.setFwdFmcNo(fwdFmcNo);
        } else {
            lclBooking.setFwdFmcNo(null);
        }
    }

    public String getFwdReference() {
        if (lclBooking.getFwdReference() != null) {
            return "" + lclBooking.getFwdReference();
        }
        return "";
    }

    public void setFwdReference(String fwdReference) {
        if (CommonUtils.isNotEmpty(fwdReference)) {
            lclBooking.setFwdReference(fwdReference);
        } else {
            lclBooking.setFwdReference(null);
        }
    }

    public String getConsReference() {
        if (lclBooking.getConsReference() != null) {
            return "" + lclBooking.getConsReference();
        }
        return "";
    }

    public void setConsReference(String consReference) {
        if (CommonUtils.isNotEmpty(consReference)) {
            lclBooking.setConsReference(consReference);
        } else {
            lclBooking.setConsReference(null);
        }
    }

    public String getNotyReference() {
        if (lclBooking.getNotyReference() != null) {
            return "" + lclBooking.getNotyReference();
        }
        return "";
    }

    public void setNotyReference(String notyReference) {
        if (CommonUtils.isNotEmpty(notyReference)) {
            lclBooking.setNotyReference(notyReference);
        } else {
            lclBooking.setNotyReference(null);
        }
    }

    public String getOverrideDimType() {
        return lclBooking.getOverrideDimUom();
    }

    public void setOverrideDimType(String overrideDimType) {
        lclBooking.setOverrideDimUom(overrideDimType);
    }

    public BigDecimal getOverrideDimCubic() {
        return lclBooking.getOverrideDimVolume();
    }

    public void setOverrideDimCubic(BigDecimal overrideDimCubic) {
        lclBooking.setOverrideDimVolume(overrideDimCubic);
    }

    public BigDecimal getOverrideDimWeight() {
        return lclBooking.getOverrideDimWeight();
    }

    public void setOverrideDimWeight(BigDecimal overrideDimWeight) {
        lclBooking.setOverrideDimWeight(overrideDimWeight);
    }

    public String getAgentAcct() {
        return lclBooking.getAgentAcct().getAccountno();
    }

    public void setAgentAcct(String agentAcct) throws Exception {
        if (CommonUtils.isNotEmpty(agentAcct)) {
            lclBooking.setAgentAcct(new TradingPartnerDAO().findById(agentAcct));
        } else {
            lclBooking.setAgentAcct(null);
        }
    }

    public String getRtdAgentAcct() {
        return lclBooking.getRtdAgentAcct().getAccountno();
    }

    public void setRtdAgentAcct(String rtdAgentAcct) throws Exception {
        if (CommonUtils.isNotEmpty(rtdAgentAcct)) {
            lclBooking.setRtdAgentAcct(new TradingPartnerDAO().findById(rtdAgentAcct));
        } else {
            lclBooking.setRtdAgentAcct(null);
        }
    }

    public String getClientContactManual() {
        return clientContactManual;
    }

    public void setClientContactManual(String clientContactManual) {
        this.clientContactManual = clientContactManual;
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

    public LclContact getClientContact() {
        return lclBooking.getClientContact();
    }

    public void setClientContact(LclContact clientContact) {
        lclBooking.setClientContact(clientContact);
    }

    public String getBookingHsCodeId() {
        return bookingHsCodeId;
    }

    public void setBookingHsCodeId(String bookingHsCodeId) {
        this.bookingHsCodeId = bookingHsCodeId;
    }

    public String getPackageTypeId() {
        return PackageTypeId;
    }

    public void setPackageTypeId(String PackageTypeId) {
        this.PackageTypeId = PackageTypeId;
    }

    public LclContact getPooWhseContact() {
        return lclBooking.getPooWhseContact();
    }

    public void setPooWhseContact(LclContact pooWhseContact) {
        lclBooking.setPooWhseContact(pooWhseContact);
    }

    public String getConsAcct() {
        return lclBooking.getConsAcct().getAccountno();
    }

    public void setConsAcct(String consAcct) {
        if (CommonUtils.isNotEmpty(consAcct)) {
            lclBooking.setConsAcct(new TradingPartner(consAcct));
        } else {
            lclBooking.setConsAcct(null);
        }
    }

    public LclContact getConsContact() {
        return lclBooking.getConsContact();
    }

    public void setConsContact(LclContact consContact) {
        lclBooking.setConsContact(consContact);
    }

//    public LclContact getDoorDestinationContact() {
//        return lclBooking.getDoorDestinationContact();
//    }
//
//    public void setDoorDestinationContact(LclContact doorDestinationContact) {
//        lclBooking.setDoorDestinationContact(doorDestinationContact);
//    }
//    public LclContact getDoorOriginContact() {
//        return lclBooking.getDoorOriginContact();
//    }
//
//    public void setDoorOriginContact(LclContact doorOriginContact) {
//        lclBooking.setDoorOriginContact(doorOriginContact);
//    }
    public String getFwdAcct() {
        return lclBooking.getFwdAcct().getAccountno();
    }

    public void setFwdAcct(String fwdAcct) {
        if (CommonUtils.isNotEmpty(fwdAcct)) {
            lclBooking.setFwdAcct(new TradingPartner(fwdAcct));
        } else {
            lclBooking.setFwdAcct(null);
        }
    }

    public LclContact getFwdContact() {
        return lclBooking.getFwdContact();
    }

    public void setFwdContact(LclContact fwdContact) {
        lclBooking.setFwdContact(fwdContact);
    }
    private LclFileNumber lclFileNumber;

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public String getNotyAcct() {
        return lclBooking.getNotyAcct().getAccountno();
    }

    public void setNotyAcct(String notyAcct) {
        if (CommonUtils.isNotEmpty(notyAcct)) {
            lclBooking.setNotyAcct(new TradingPartner(notyAcct));
        } else {
            lclBooking.setNotyAcct(null);
        }
    }

    public LclContact getNotyContact() {
        return lclBooking.getNotyContact();
    }

    public void setNotyContact(LclContact notyContact) {
        lclBooking.setNotyContact(notyContact);
    }

    public String getShipAcct() {
        return lclBooking.getShipAcct().getAccountno();
    }

    public void setShipAcct(String shipAcct) {
        if (CommonUtils.isNotEmpty(shipAcct)) {
            lclBooking.setShipAcct(new TradingPartner(shipAcct));
        } else {
            lclBooking.setShipAcct(null);
        }
    }

    public LclContact getShipContact() {
        return lclBooking.getShipContact();
    }

    public void setShipContact(LclContact shipContact) {
        lclBooking.setShipContact(shipContact);
    }

    public String getSupAcct() {
        return lclBooking.getSupAcct().getAccountno();
    }

    public void setSupAcct(String supAcct) {
        if (CommonUtils.isNotEmpty(supAcct)) {
            lclBooking.setSupAcct(new TradingPartner(supAcct));
        } else {
            lclBooking.setSupAcct(null);
        }
    }

    public LclContact getSupContact() {
        return lclBooking.getSupContact();
    }

    public void setSupContact(LclContact supContact) {
        lclBooking.setSupContact(supContact);
    }

    public String getClientAcct() {
        return lclBooking.getClientAcct().getAccountno();
    }

    public void setClientAcct(String clientAcct) {
        if (CommonUtils.isNotEmpty(clientAcct)) {
            lclBooking.setClientAcct(new TradingPartner(clientAcct));
        } else {
            lclBooking.setClientAcct(null);
        }
    }

    public String getClientCompany() {
        return lclBooking.getClientContact().getCompanyName();
    }

    public void setClientCompany(String clientCompany) {
        lclBooking.getClientContact().setCompanyName(clientCompany);
    }

    public String getTempClientCompany() {
        return tempClientCompany;
    }

    public void setTempClientCompany(String tempClientCompany) {
        this.tempClientCompany = tempClientCompany;
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
    }

    public String getInternalRemarks() {
        return internalRemarks;
    }

    public void setInternalRemarks(String internalRemarks) {
        this.internalRemarks = internalRemarks;
    }

    public String getBookedPieceCount() {
        return bookedPieceCount;
    }

    public void setBookedPieceCount(String bookedPieceCount) {
        this.bookedPieceCount = bookedPieceCount;
    }

    public String getBookedVolumeImperial() {
        return bookedVolumeImperial;
    }

    public void setBookedVolumeImperial(String bookedVolumeImperial) {
        this.bookedVolumeImperial = bookedVolumeImperial;
    }

    public String getBookedWeightImperial() {
        return bookedWeightImperial;
    }

    public void setBookedWeightImperial(String bookedWeightImperial) {
        this.bookedWeightImperial = bookedWeightImperial;
    }
    // Client Section properties need to save in the database
    private Boolean cfcl;
    private String otiNumber;
    private String nonRated;
    private String replicateFileNumber;
    private String fmcNumber;
    private String commodityNumber;
    private Boolean aesBy;
    private String acctType;
    private String subType;

    public Boolean getCfcl() {
        return cfcl;
    }

    public void setCfcl(Boolean cfcl) {
        this.cfcl = cfcl;
    }

    public String getNonRated() {
        if (lclBooking.getNonRated() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setNonRated(String nonRated) {
        if (nonRated.equals("Y")) {
            lclBooking.setNonRated(TRUE);
        } else {
            lclBooking.setNonRated(FALSE);
        }
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

    public String getOtiNumber() {
        return this.otiNumber;
    }

    public void setOtiNumber(String otiNumber) {
        this.otiNumber = otiNumber;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;

    }

    public String getSubType() {
        return subType;

    }

    public void setSubType(String subType) {
        this.subType = subType;

    }

    public String getUnlocationName() {
        return unlocationName;

    }

    public void setUnlocationName(String unlocationName) {
        this.unlocationName = unlocationName;

    }

    public String getPodUnlocationcode() {
        return podUnlocationcode;

    }

    public void setPodUnlocationcode(String podUnlocationcode) {
        this.podUnlocationcode = podUnlocationcode;

    }

    public String getPolUnlocationcode() {
        return polUnlocationcode;

    }

    public void setPolUnlocationcode(String polUnlocationcode) {
        this.polUnlocationcode = polUnlocationcode;

    }
//  Trade Route Section properties need to save in DB
    private String relayOvr;
    private String podfdtt;
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
    private String suHeadingNote;
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
    // private String approxDue;
    private String piece;
    private String cube;
    private String weight;
    private String pwk;
    private Boolean ups;//Small Parcell Values
    private String pe;
    private Boolean calcHeavy;
    private String pcBoth;
    private String pcBothImports;
    private String aesItnNumber;
    private String aesException;
    private String pickupYesNo;
    private String doorMovePickupCts;
    private String supplierPoa;
    private String shipperPoa;
    private String forwarderPoa;
    private String consigneePoa;
    private String notifyPoa;
    private String fdEta1;
    private String fdEta;
    private String pol;
    private String pod;
    private String printDr;
    private String labelFieldName;
    private String deliveryMetro = N;
    private boolean hazmat;
    private String manualDoorOriginCityZip;
    private String hotCodeComments;
    private String holdComments;
    private Long hotCodeId;
    private Boolean noBLRequired;

    public String getReplicateFileNumber() {
        return replicateFileNumber;
    }

    public void setReplicateFileNumber(String replicateFileNumber) {
        this.replicateFileNumber = replicateFileNumber;
    }

    public String getDoorMovePickupCts() {
        return doorMovePickupCts;
    }

    public void setDoorMovePickupCts(String doorMovePickupCts) {
        this.doorMovePickupCts = doorMovePickupCts;
    }

    public String getPodfdtt() {
        if (lclBooking.getPodfdtt() != null) {
            return "" + lclBooking.getPodfdtt();
        }
        return "";
    }

    public void setPodfdtt(String podfdtt) {
        if (CommonUtils.isNotEmpty(podfdtt)) {
            lclBooking.setPodfdtt(new Integer(podfdtt));
        } else {
            lclBooking.setPodfdtt(null);
        }
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

    public String getLabelFieldName() {
        return labelFieldName;
    }

    public void setLabelFieldName(String labelFieldName) {
        this.labelFieldName = labelFieldName;
    }

    public String getPrintDr() {
        return printDr;
    }

    public void setPrintDr(String printDr) {
        this.printDr = printDr;
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

    public String getDeliveryMetro() {
        return lclBooking.getDeliveryMetro();
    }

    public void setDeliveryMetro(String deliveryMetro) {
        if (CommonUtils.isNotEmpty(deliveryMetro)) {
            lclBooking.setDeliveryMetro(deliveryMetro);
        }
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

    public String getPcBoth() {
        if (lclBooking.getBillingType() != null) {
            return lclBooking.getBillingType();
        }
        return "P";
    }

    public void setPcBoth(String pcBoth) {
        if (CommonUtils.isNotEmpty(pcBoth)) {
            lclBooking.setBillingType(pcBoth);
        }
    }

    public String getPcBothImports() {
        if (lclBooking.getBillingType() != null) {
            return lclBooking.getBillingType();
        }
        return "C";
    }

    public void setPcBothImports(String pcBothImports) {
        if (CommonUtils.isNotEmpty(pcBothImports)) {
            lclBooking.setBillingType(pcBothImports);
        }
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public Boolean getUps() {
        return ups;
    }

    public void setUps(Boolean ups) {
        this.ups = ups;
    }

    public Boolean getNoBLRequired() {
        return noBLRequired;
    }

    public void setNoBLRequired(Boolean noBLRequired) {
        this.noBLRequired = noBLRequired;
    }

    public String getPwk() {
        if (lclBooking.getClientPwkRecvd() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setPwk(String pwk) {
        if (pwk.equals(Y)) {
            lclBooking.setClientPwkRecvd(TRUE);
        } else {
            lclBooking.setClientPwkRecvd(FALSE);
        }
    }

    public String getERT() {
        if (CommonUtils.isEmpty(ERT)) {
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
        if (lclBooking.getFdEta() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBooking.getFdEta());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setFdEta(String fdEta) throws Exception {
        if (CommonUtils.isNotEmpty(fdEta)) {
            lclBooking.setFdEta(DateUtils.parseDate(fdEta, "dd-MMM-yyyy"));
        } else {
            lclBooking.setFdEta(null);
        }
    }

    public String getPooLrdt() throws Exception {
        if (lclBooking.getPooWhseLrdt() != null) {
            String d = DateUtils.formatDate(lclBooking.getPooWhseLrdt(), "dd-MMM-yyyy hh:mm");
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPooLrdt(String pooLrdt) throws Exception {
        if (CommonUtils.isNotEmpty(pooLrdt)) {
            lclBooking.setPooWhseLrdt(DateUtils.parseDate(pooLrdt, "dd-MMM-yyyy hh:mm"));
        } else {
            lclBooking.setPooWhseLrdt(null);
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
        if (lclBooking.getValueOfGoods() != null) {
            return "" + lclBooking.getValueOfGoods();
        }
        return "";
    }

    public void setValueOfGoods(String valueOfGoods) {
        if (CommonUtils.isNotEmpty(valueOfGoods)) {
            lclBooking.setValueOfGoods(new BigDecimal(valueOfGoods));
        } else {
            lclBooking.setValueOfGoods(null);
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
    private String billtoCodeImports;
    private String thirdPartyName;
    private String thirdPartyAccount;
    private String trmnum;
    private String foreignDischarge;
    private String portExit;

    public String getFinalDestination() {
        if (lclBooking.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName()) && null != lclBooking.getFinalDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBooking.getFinalDestination().getUnLocationName() + "/" + lclBooking.getFinalDestination().getStateId().getCode() + '(' + lclBooking.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName()) && lclBooking.getFinalDestination().getCountryId() != null
                    && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBooking.getFinalDestination().getUnLocationName() + "/" + lclBooking.getFinalDestination().getCountryId().getCodedesc() + '(' + lclBooking.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBooking.getFinalDestination().getUnLocationName() + '(' + lclBooking.getFinalDestination().getUnLocationCode() + ')');
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
        if (lclBooking.getFinalDestination() != null) {
            return lclBooking.getFinalDestination().getId();
        } else {
            return null;
        }
    }

    public void setFinalDestinationId(Integer finalDestinationId) throws Exception {
        if (CommonUtils.isNotEmpty(finalDestinationId)) {
            lclBooking.setFinalDestination(new UnLocationDAO().findById(finalDestinationId));
        }
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

    public String getThirdPartyname() {
        if (lclBooking.getThirdPartyAcct() != null) {
            return lclBooking.getThirdPartyAcct().getAccountName();
        }
        return null;
    }

    public void setThirdPartyname(String thirdPartyname) {
        if (CommonUtils.isNotEmpty(thirdPartyname)) {
            lclBooking.getThirdPartyAcct().setAccountName(thirdPartyname);
        }
    }

    public String getThirdpartyaccountNo() {
        if (lclBooking.getThirdPartyAcct() != null) {
            return lclBooking.getThirdPartyAcct().getAccountno();
        }
        return null;
    }

    public void setThirdpartyaccountNo(String thirdpartyaccountNo) {
        if (CommonUtils.isNotEmpty(thirdpartyaccountNo)) {
            lclBooking.setThirdPartyAcct(new TradingPartner(thirdpartyaccountNo));
        } else {
            lclBooking.setThirdPartyAcct(null);
        }
    }

//    public String getDoorOriginContact() {
//        StringBuilder builder = new StringBuilder();
//        if (null != lclBooking && lclBooking.getDoorOriginContact() != null && null != lclBooking.getDoorOriginContact().getId()) {
//              Zipcode contact = new ZipCodeDAO().findById(lclBooking.getDoorOriginContact().getId());
//            if(CommonUtils.isNotEmpty(contact.getCity())
//                    && CommonUtils.isNotEmpty(contact.getZip()) && CommonUtils.isNotEmpty(contact.getState())){
//                builder.append(contact.getZip()+"-"+contact.getCity()+"/"+contact.getState());
//            }else if(CommonUtils.isNotEmpty(contact.getCity())
//                    && CommonUtils.isNotEmpty(contact.getZip()) && CommonUtils.isEmpty(contact.getState())){
//                builder.append(contact.getZip()+"-"+contact.getCity());
//            }else if(CommonUtils.isEmpty(contact.getCity())
//                    && CommonUtils.isNotEmpty(contact.getZip()) && CommonUtils.isNotEmpty(contact.getState())){
//                builder.append(contact.getZip()+"/"+contact.getState());
//            }
//            return builder.toString();
//        }
//        return "";
//    }
//
//    public void setDoorOriginContact(String doorOriginContact) {
//        this.doorOriginContact = doorOriginContact;
//    }
//
//    public Integer getDoorOriginContactId() {
//        if (lclBooking.getDoorOriginContact() != null) {
//            return lclBooking.getDoorOriginContact().getId();
//        } else {
//            return null;
//        }
//    }
//
//    public void setDoorOriginContactId(Integer doorOriginContactId) {
//       if (CommonUtils.isNotEmpty(doorOriginContactId)) {
//            lclBooking.setDoorOriginContact(new ZipCodeDAO().findById(doorOriginContactId));
//        }
//    }
    public String getPortOfDestination() {
        if (lclBooking.getPortOfDestination() != null) {
            return lclBooking.getPortOfDestination().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setPortOfDestination(String portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public Integer getPortOfDestinationId() {
        if (lclBooking.getPortOfDestination() != null) {
            return lclBooking.getPortOfDestination().getId();
        } else {
            return null;
        }
    }

    public void setPortOfDestinationId(Integer portOfDestinationId) throws Exception {
        if (null != portOfDestinationId && CommonUtils.isNotEmpty(portOfDestinationId)) {
            lclBooking.setPortOfDestination(new UnLocationDAO().findById(portOfDestinationId));
        } else if ("Y".equalsIgnoreCase(transshipType)) {
            if (CommonUtils.isNotEmpty(tFinalDestinationId)) {
                lclBooking.setPortOfDestination(new UnLocationDAO().findById(Integer.parseInt(tFinalDestinationId)));
            } else {
                lclBooking.setPortOfDestination(null);
            }

        } else {
            lclBooking.setPortOfDestination(null);
        }
    }

    public String getPortOfLoading() {
        if (lclBooking.getPortOfLoading() != null) {
            return lclBooking.getPortOfLoading().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public Integer getPortOfLoadingId() {
        if (lclBooking.getPortOfLoading() != null) {
            return lclBooking.getPortOfLoading().getId();
        } else {
            return null;
        }
    }

    public void setPortOfLoadingId(Integer portOfLoadingId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfLoadingId)) {
            lclBooking.setPortOfLoading(new UnLocationDAO().findById(portOfLoadingId));
        } else {
            lclBooking.setPortOfLoading(null);
        }
    }

    public String getPortOfOrigin() {
        if (lclBooking.getPortOfOrigin() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationName()) && null != lclBooking.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfOrigin().getUnLocationName() + "/" + lclBooking.getPortOfOrigin().getStateId().getCode() + "(" + lclBooking.getPortOfOrigin().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationCode()) && null != lclBooking.getPortOfOrigin().getCountryId() && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getCountryId().getCodedesc())) {
                builder.append(lclBooking.getPortOfOrigin().getUnLocationName() + "/" + lclBooking.getPortOfOrigin().getCountryId().getCodedesc() + "(" + lclBooking.getPortOfOrigin().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfOrigin().getUnLocationName() + "(" + lclBooking.getPortOfOrigin().getUnLocationCode() + ")");
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
        if (lclBooking.getPortOfOrigin() != null) {
            return lclBooking.getPortOfOrigin().getId();
        } else {
            return null;
        }
    }

    public void setPortOfOriginId(Integer portOfOriginId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfOriginId)) {
            lclBooking.setPortOfOrigin(new UnLocationDAO().findById(portOfOriginId));
        } else if ("Y".equalsIgnoreCase(transshipType)) {
            if (CommonUtils.isNotEmpty(tPortOfLoadingId)) {
                lclBooking.setPortOfOrigin(new UnLocationDAO().findById(Integer.parseInt(tPortOfLoadingId)));
            } else {
                lclBooking.setPortOfOrigin(null);
            }
        } else {
            lclBooking.setPortOfOrigin(null);
        }
    }

    public Long getMasterScheduleNo() {
        if (lclBooking.getBookedSsHeaderId() != null) {
            return lclBooking.getBookedSsHeaderId().getId();
        }
        return null;
    }

    public void setMasterScheduleNo(Long masterScheduleNo) throws Exception {
        if (CommonUtils.isNotEmpty(masterScheduleNo)) {
            LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(masterScheduleNo);
            lclBooking.setBookedSsHeaderId(lclSsHeader);
        } else {
            lclBooking.setBookedSsHeaderId(null);
        }
    }

    public String getBillForm() {
        if (!"B".equalsIgnoreCase(lclBooking.getBillingType()) && CommonUtils.isNotEmpty(lclBooking.getBillToParty())) {
            return lclBooking.getBillToParty();
        } else if ("B".equalsIgnoreCase(lclBooking.getBillingType())) {
            return "";
        }
        return "F";
    }

    public void setBillForm(String billForm) {
        lclBooking.setBillToParty(billForm);
    }

    public String getBilltoCodeImports() {
        if (CommonUtils.isNotEmpty(lclBooking.getBillToParty())) {
            return lclBooking.getBillToParty();
        }
        return "C";
    }

    public void setBilltoCodeImports(String billtoCodeImports) {
        lclBooking.setBillToParty(billtoCodeImports);
    }

    public String getThirdPartyAccount() {
        return thirdPartyAccount;
    }

    public void setThirdPartyAccount(String thirdPartyAccount) {
        this.thirdPartyAccount = thirdPartyAccount;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getUnlocationCode() {
        if (lclBooking.getFinalDestination() != null) {
            return lclBooking.getFinalDestination().getUnLocationCode();
        } else {
            return "";
        }
    }

    public void setUnlocationCode(String unlocationCode) {
        this.unlocationCode = unlocationCode;
    }

    public String getOriginUnlocationCode() {
        if (lclBooking.getPortOfOrigin() != null) {
            return lclBooking.getPortOfOrigin().getUnLocationCode();
        } else {
            return "";
        }
    }

    public void setOriginUnlocationCode(String originUnlocationCode) {
        this.originUnlocationCode = originUnlocationCode;
    }

    public String getConsigneePoa() {
        return consigneePoa;
    }

    public void setConsigneePoa(String consigneePoa) {
        this.consigneePoa = consigneePoa;
    }

    public String getForwarderPoa() {
        return forwarderPoa;
    }

    public void setForwarderPoa(String forwarderPoa) {
        this.forwarderPoa = forwarderPoa;
    }

    public String getNotifyPoa() {
        return notifyPoa;
    }

    public void setNotifyPoa(String notifyPoa) {
        this.notifyPoa = notifyPoa;
    }

    public String getShipperPoa() {
        return shipperPoa;
    }

    public void setShipperPoa(String shipperPoa) {
        this.shipperPoa = shipperPoa;
    }

    public String getSupplierPoa() {
        return supplierPoa;
    }

    public void setSupplierPoa(String supplierPoa) {
        this.supplierPoa = supplierPoa;
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
        if (lclBooking.getPooPickup() != null && lclBooking.getPooPickup().equals(TRUE)) {
            return Y;
        }
        return N;
    }

    public void setPooDoor(String pooDoor) {
        if (pooDoor.equals(Y)) {
            lclBooking.setPooPickup(TRUE);
        } else {
            lclBooking.setPooPickup(FALSE);
        }
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

    public String getDeliverCargoTo() {
        return lclBooking.getPooWhseContact().getAddress();
    }

    public void setDeliverCargoTo(String deliverCargoTo) {
        lclBooking.getPooWhseContact().setAddress(deliverCargoTo);
    }

    public String getWhsCode() {
        return whsCode;
    }

    public void setWhsCode(String whsCode) {
        this.whsCode = whsCode;
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
                lclBooking = new LCLBookingDAO().findById(Long.parseLong(parentId));
                new LCLBookingDAO().getCurrentSession().evict(lclBooking);
//                String methodName = request.getParameter("methodName");
//                String isFormChangeFlag = request.getParameter("isFormchangeFlag");
//                if (null != lclBooking && (CommonUtils.in(methodName, "calculateImportCharges") || "true".equals(isFormChangeFlag))) {
//                    lclBookingImport = new LclBookingImportDAO().findById(Long.parseLong(parentId));
//                    User user = (User) request.getSession().getAttribute("loginuser");
//                    lclBooking.setModifiedBy(user);
//                    lclBooking.setModifiedDatetime(new Date());
//                    if (null != lclBookingImport) {
//                        lclBookingImport.setModifiedBy(user);
//                        lclBookingImport.setModifiedDatetime(new Date());
//                    }
//                }
            } catch (Exception ex) {
                log.info("reset() failed on " + new Date(), ex);
            }
        }
    }

    public void setEnums(HttpServletRequest request, String moduleName, String transhipment) {
        if ("Y".equalsIgnoreCase(transhipment)) {
            lclBooking.setBookingType(LclCommonConstant.LCL_TRANSHIPMENT_TYPE);
        } else if ("Imports".equalsIgnoreCase(moduleName)) {
            lclBooking.setBookingType(LclCommonConstant.LCL_IMPORT_TYPE);
        } else {
            lclBooking.setBookingType(LclCommonConstant.LCL_EXPORT_TYPE);
        }
        lclBooking.setTransMode("R");
        lclBooking.setOverrideDimUom("I");
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

    public String getScreenType() {
        return "B";
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getPier() {
        return pier;
    }

    public void setPier(String pier) {
        this.pier = pier;
    }

    public String getBasedOnCity() {
        return basedOnCity;
    }

    public void setBasedOnCity(String basedOnCity) {
        this.basedOnCity = basedOnCity;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getImportApproxDue() {
        return importApproxDue;
    }

    public void setImportApproxDue(String importApproxDue) {
        this.importApproxDue = importApproxDue;
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
        return transShipMent;
    }

    public void setTransShipMent(String transShipMent) {
        this.transShipMent = transShipMent;
    }

//    public String getTransShipMent() {
//        if (lclBookingImport.getTransShipment() == TRUE) {
//            return Y;
//        }
//        return N;
//    }
//
//    public void setTransShipMent(String transShipMent) {
//        if (transShipMent.equals("Y")) {
//            lclBookingImport.setTransShipment(TRUE);
//        } else {
//            lclBookingImport.setTransShipment(FALSE);
//        }
//    }
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

    public String getHbl() {
        return hbl;
    }

    public void setHbl(String hbl) {
        this.hbl = hbl;
    }

    public String getItClass() {
        return itClass;
    }

    public void setItClass(String itClass) {
        this.itClass = itClass;
    }

    //used for imports
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

    public LclBookingImport getLclBookingImport() {
        return lclBookingImport;
    }

    public void setLclBookingImport(LclBookingImport lclBookingImport) {
        this.lclBookingImport = lclBookingImport;
    }

    public String getCustomsReleaseNo() {
        return customsReleaseNo;
    }

    public void setCustomsReleaseNo(String customsReleaseNo) {
        this.customsReleaseNo = customsReleaseNo;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public String getRelaySearch() {
        return relaySearch;
    }

    public void setRelaySearch(String relaySearch) {
        this.relaySearch = relaySearch;
    }

    public String getDifflclBookedDimsActual() {
        return difflclBookedDimsActual;
    }

    public void setDifflclBookedDimsActual(String difflclBookedDimsActual) {
        this.difflclBookedDimsActual = difflclBookedDimsActual;
    }

    public String getCfclAcctName() {
        return cfclAcctName;
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

    public String getNewConsigneeForDr() {
        return newConsigneeForDr;
    }

    public void setNewConsigneeForDr(String newConsigneeForDr) {
        this.newConsigneeForDr = newConsigneeForDr;
    }

    public String getNewForwarderForDr() {
        return newForwarderForDr;
    }

    public void setNewForwarderForDr(String newForwarderForDr) {
        this.newForwarderForDr = newForwarderForDr;
    }

    public String getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(String rateAmount) {
        this.rateAmount = rateAmount;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getSpotRateCommNo() {
        return spotRateCommNo;
    }

    public void setSpotRateCommNo(String spotRateCommNo) {
        this.spotRateCommNo = spotRateCommNo;
    }

    public String getInbondNo() {
        return inbondNo;
    }

    public void setInbondNo(String inbondNo) {
        this.inbondNo = inbondNo;
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

    public String getPackageType() {
        return PackageType;
    }

    public void setPackageType(String PackageType) {
        this.PackageType = PackageType;
    }

    public String getHsCodePiece() {
        return hsCodePiece;
    }

    public void setHsCodePiece(String hsCodePiece) {
        this.hsCodePiece = hsCodePiece;
    }

    public String getHsCodeWeightMetric() {
        return hsCodeWeightMetric;
    }

    public void setHsCodeWeightMetric(String hsCodeWeightMetric) {
        this.hsCodeWeightMetric = hsCodeWeightMetric;
    }

    public String getPaddress() {
        return paddress;
    }

    public void setPaddress(String paddress) {
        this.paddress = paddress;
    }

    public String getOriginCityZip() {
        return OriginCityZip;
    }

    public void setOriginCityZip(String OriginCityZip) {
        this.OriginCityZip = OriginCityZip;
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

    public Long getLcl3pRefId() {
        return lcl3pRefId;
    }

    public void setLcl3pRefId(Long lcl3pRefId) {
        this.lcl3pRefId = lcl3pRefId;
    }

    public String getThirdPName() {
        return thirdPName;
    }

    public void setThirdPName(String thirdPName) {
        this.thirdPName = thirdPName;
    }

    public String getGenCodefield1() {
        return genCodefield1;
    }

    public void setGenCodefield1(String genCodefield1) {
        this.genCodefield1 = genCodefield1;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
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

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getImpEciVoyage() {
        return impEciVoyage;
    }

    public void setImpEciVoyage(String impEciVoyage) {
        this.impEciVoyage = impEciVoyage;
    }

    public String getImpSailDate() {
        return impSailDate;
    }

    public void setImpSailDate(String impSailDate) {
        this.impSailDate = impSailDate;
    }

    public String getImpSsLine() {
        return impSsLine;
    }

    public void setImpSsLine(String impSsLine) {
        this.impSsLine = impSsLine;
    }

    public String getImpStripDate() {
        return impStripDate;
    }

    public void setImpStripDate(String impStripDate) {
        this.impStripDate = impStripDate;
    }

    public String getImpVesselArrival() {
        return impVesselArrival;
    }

    public void setImpVesselArrival(String impVesselArrival) {
        this.impVesselArrival = impVesselArrival;
    }

    public String getImpVesselName() {
        return impVesselName;
    }

    public void setImpVesselName(String impVesselName) {
        this.impVesselName = impVesselName;
    }

    public String getImpPier() {
        return impPier;
    }

    public void setImpPier(String impPier) {
        this.impPier = impPier;
    }

    public String getImpSsVoyage() {
        return impSsVoyage;
    }

    public void setImpSsVoyage(String impSsVoyage) {
        this.impSsVoyage = impSsVoyage;
    }

    public String getImpCFSWareName() {
        return impCFSWareName;
    }

    public void setImpCFSWareName(String impCFSWareName) {
        this.impCFSWareName = impCFSWareName;
    }

    public String getImpCFSWareaddress() {
        return impCFSWareaddress;
    }

    public void setImpCFSWareaddress(String impCFSWareaddress) {
        this.impCFSWareaddress = impCFSWareaddress;
    }

    public String getImpUnitNo() {
        return impUnitNo;
    }

    public void setImpUnitNo(String impUnitNo) {
        this.impUnitNo = impUnitNo;
    }

    public String getImpUnitWareNo() {
        return impUnitWareNo;
    }

    public void setImpUnitWareNo(String impUnitWareNo) {
        this.impUnitWareNo = impUnitWareNo;
    }

    public String getImpUnitWareaddress() {
        return impUnitWareaddress;
    }

    public void setImpUnitWareaddress(String impUnitWareaddress) {
        this.impUnitWareaddress = impUnitWareaddress;
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

    public String getImpSearchFlag() {
        return impSearchFlag;
    }

    public void setImpSearchFlag(String impSearchFlag) {
        this.impSearchFlag = impSearchFlag;
    }

    public String getBundleToOf() {
        return bundleToOf;
    }

    public void setBundleToOf(String bundleToOf) {
        this.bundleToOf = bundleToOf;
    }

    public String getRelsToInv() {
        return relsToInv;
    }

    public void setRelsToInv(String relsToInv) {
        this.relsToInv = relsToInv;
    }

    public String getPrintOnBL() {
        return printOnBL;
    }

    public void setPrintOnBL(String printOnBL) {
        this.printOnBL = printOnBL;
    }

    public String getComno() {
        return comno;
    }

    public void setComno(String comno) {
        this.comno = comno;
    }

    public String getPickVoyId() {
        return pickVoyId;
    }

    public void setPickVoyId(String pickVoyId) {
        this.pickVoyId = pickVoyId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSelectedLrd() {
        return selectedLrd;
    }

    public void setSelectedLrd(String selectedLrd) {
        this.selectedLrd = selectedLrd;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCurrentLocationDojo() {
        return currentLocationDojo;
    }

    public void setCurrentLocationDojo(String currentLocationDojo) {
        this.currentLocationDojo = currentLocationDojo;
    }

    public Integer getUnLocationId() {
        return unLocationId;
    }

    public void setUnLocationId(Integer unLocationId) {
        this.unLocationId = unLocationId;
    }

    public String getFilterByChanges() {
        return filterByChanges;
    }

    public void setFilterByChanges(String filterByChanges) {
        this.filterByChanges = filterByChanges;
    }
    private String shortShip;
    private Long shortShipFileId;

    public String getShortShip() {
        return shortShip;
    }

    public void setShortShip(String shortShip) {
        this.shortShip = shortShip;
    }

    public Long getShortShipFileId() {
        return shortShipFileId;
    }

    public void setShortShipFileId(Long shortShipFileId) {
        this.shortShipFileId = shortShipFileId;
    }

    public String getFileNumberStatus() {
        return fileNumberStatus;
    }

    public void setFileNumberStatus(String fileNumberStatus) {
        this.fileNumberStatus = fileNumberStatus;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTransUpcoming() {
        return transUpcoming;
    }

    public void setTransUpcoming(String transUpcoming) {
        this.transUpcoming = transUpcoming;
    }

    public String getTransfileId() {
        return transfileId;
    }

    public void setTransfileId(String transfileId) {
        this.transfileId = transfileId;
    }

    public String getThirdpRefNo() {
        return thirdpRefNo;
    }

    public void setThirdpRefNo(String thirdpRefNo) {
        this.thirdpRefNo = thirdpRefNo;
    }

    public String getThirdpType() {
        return thirdpType;
    }

    public void setThirdpType(String thirdpType) {
        this.thirdpType = thirdpType;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getUnitItDate() {
        return unitItDate;
    }

    public void setUnitItDate(String unitItDate) {
        this.unitItDate = unitItDate;
    }

    public String getUnitItNo() {
        return unitItNo;
    }

    public void setUnitItNo(String unitItNo) {
        this.unitItNo = unitItNo;
    }

    public String getUnitItPort() {
        return unitItPort;
    }

    public void setUnitItPort(String unitItPort) {
        this.unitItPort = unitItPort;
    }

    public String getEtaFDDate() {
        return etaFDDate;
    }

    public void setEtaFDDate(String etaFDDate) {
        this.etaFDDate = etaFDDate;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShipUnloCode() {
        return shipUnloCode;
    }

    public void setShipUnloCode(String shipUnloCode) {
        this.shipUnloCode = shipUnloCode;
    }

    public String getExportAgentAcctName() {
        return exportAgentAcctName;
    }

    public void setExportAgentAcctName(String exportAgentAcctName) {
        this.exportAgentAcctName = exportAgentAcctName;
    }

    public String getExportAgentAcctNo() {
        return exportAgentAcctNo;
    }

    public void setExportAgentAcctNo(String exportAgentAcctNo) {
        this.exportAgentAcctNo = exportAgentAcctNo;
    }

    public String getImpCfsWarehsId() {
        return impCfsWarehsId;
    }

    public void setImpCfsWarehsId(String impCfsWarehsId) {
        this.impCfsWarehsId = impCfsWarehsId;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public String getDefaultAms() {
        return defaultAms;
    }

    public void setDefaultAms(String defaultAms) {
        this.defaultAms = defaultAms;
    }

    public boolean isClientIcon() {
        return clientIcon;
    }

    public void setClientIcon(boolean clientIcon) {
        this.clientIcon = clientIcon;
    }

    public boolean isConsigneeIcon() {
        return consigneeIcon;
    }

    public void setConsigneeIcon(boolean consigneeIcon) {
        this.consigneeIcon = consigneeIcon;
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

    public boolean isNotifyIcon() {
        return notifyIcon;
    }

    public void setNotifyIcon(boolean notifyIcon) {
        this.notifyIcon = notifyIcon;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public boolean isAllowVoyageCopy() {
        return allowVoyageCopy;
    }

    public void setAllowVoyageCopy(boolean allowVoyageCopy) {
        this.allowVoyageCopy = allowVoyageCopy;
    }

//    public LclContact getNotify2Contact() {
//        return lclBooking.getNotify2Contact();
//    }
//
//    public void setNotify2Contact(LclContact notify2Contact) {
//        lclBooking.setNotify2Contact(notify2Contact);
//    }
    public String getNotify2ContactName() {
        return notify2ContactName;
    }

    public void setNotify2ContactName(String notify2ContactName) {
        this.notify2ContactName = notify2ContactName;
    }

    public boolean isNotify2Icon() {
        return notify2Icon;
    }

    public void setNotify2Icon(boolean notify2Icon) {
        this.notify2Icon = notify2Icon;
    }

    public String getNotify2AcctNo() {
        return notify2AcctNo;
    }

    public void setNotify2AcctNo(String notify2AcctNo) {
        this.notify2AcctNo = notify2AcctNo;
    }

    public LclContact getNotify2Contact() {
        return lclBooking.getNotify2Contact();
    }

    public void setNotify2Contact(LclContact notify2Contact) {
        lclBooking.setNotify2Contact(notify2Contact);
    }

    public String getEdiData() {
        return ediData;
    }

    public void setEdiData(String ediData) {
        this.ediData = ediData;
    }

    public String getCfsWarehouseAddress() {
        return cfsWarehouseAddress;
    }

    public void setCfsWarehouseAddress(String cfsWarehouseAddress) {
        this.cfsWarehouseAddress = cfsWarehouseAddress;
    }

    public String getCfsWarehouseCity() {
        return cfsWarehouseCity;
    }

    public void setCfsWarehouseCity(String cfsWarehouseCity) {
        this.cfsWarehouseCity = cfsWarehouseCity;
    }

    public String getCfsWarehouseFax() {
        return cfsWarehouseFax;
    }

    public void setCfsWarehouseFax(String cfsWarehouseFax) {
        this.cfsWarehouseFax = cfsWarehouseFax;
    }

    public String getCfsWarehousePhone() {
        return cfsWarehousePhone;
    }

    public void setCfsWarehousePhone(String cfsWarehousePhone) {
        this.cfsWarehousePhone = cfsWarehousePhone;
    }

    public String getCfsWarehouseState() {
        return cfsWarehouseState;
    }

    public void setCfsWarehouseState(String cfsWarehouseState) {
        this.cfsWarehouseState = cfsWarehouseState;
    }

    public String getCfsWarehouseZip() {
        return cfsWarehouseZip;
    }

    public void setCfsWarehouseZip(String cfsWarehouseZip) {
        this.cfsWarehouseZip = cfsWarehouseZip;
    }

    public String getUnitWarehouseAddress() {
        return unitWarehouseAddress;
    }

    public void setUnitWarehouseAddress(String unitWarehouseAddress) {
        this.unitWarehouseAddress = unitWarehouseAddress;
    }

    public String getUnitWarehouseCity() {
        return unitWarehouseCity;
    }

    public void setUnitWarehouseCity(String unitWarehouseCity) {
        this.unitWarehouseCity = unitWarehouseCity;
    }

    public String getUnitWarehouseFax() {
        return unitWarehouseFax;
    }

    public void setUnitWarehouseFax(String unitWarehouseFax) {
        this.unitWarehouseFax = unitWarehouseFax;
    }

    public String getUnitWarehousePhone() {
        return unitWarehousePhone;
    }

    public void setUnitWarehousePhone(String unitWarehousePhone) {
        this.unitWarehousePhone = unitWarehousePhone;
    }

    public String getUnitWarehouseState() {
        return unitWarehouseState;
    }

    public void setUnitWarehouseState(String unitWarehouseState) {
        this.unitWarehouseState = unitWarehouseState;
    }

    public String getUnitWarehouseZip() {
        return unitWarehouseZip;
    }

    public void setUnitWarehouseZip(String unitWarehouseZip) {
        this.unitWarehouseZip = unitWarehouseZip;
    }

    public String getCheckDRChange() {
        return checkDRChange;
    }

    public void setCheckDRChange(String checkDRChange) {
        this.checkDRChange = checkDRChange;
    }

    public Integer getDefaultPieces() {
        return defaultPieces;
    }

    public void setDefaultPieces(Integer defaultPieces) {
        this.defaultPieces = defaultPieces;
    }

    public String getAmsHblScac() {
        return amsHblScac;
    }

    public void setAmsHblScac(String amsHblScac) {
        this.amsHblScac = amsHblScac;
    }

    public String getCallBackFlag() {
        return callBackFlag;
    }

    public void setCallBackFlag(String callBackFlag) {
        this.callBackFlag = callBackFlag;
    }

    public String getPdfDocumentName() {
        return pdfDocumentName;
    }

    public void setPdfDocumentName(String pdfDocumentName) {
        this.pdfDocumentName = pdfDocumentName;
    }

    public String getVoyageOwner() {
        return voyageOwner;
    }

    public void setVoyageOwner(String voyageOwner) {
        this.voyageOwner = voyageOwner;
    }

    public String getSuHeadingNote() {
        return suHeadingNote;
    }

    public void setSuHeadingNote(String suHeadingNote) {
        this.suHeadingNote = suHeadingNote;
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

    public String getNotify2ToolTip() {
        return notify2ToolTip;
    }

    public void setNotify2ToolTip(String notify2ToolTip) {
        this.notify2ToolTip = notify2ToolTip;
    }

    public String getNotifyToolTip() {
        return notifyToolTip;
    }

    public void setNotifyToolTip(String notifyToolTip) {
        this.notifyToolTip = notifyToolTip;
    }

    public String getDispositionToolTip() {
        return dispositionToolTip;
    }

    public void setDispositionToolTip(String dispositionToolTip) {
        this.dispositionToolTip = dispositionToolTip;
    }

    public String getCfsWarehouseNo() {
        return cfsWarehouseNo;
    }

    public void setCfsWarehouseNo(String cfsWarehouseNo) {
        this.cfsWarehouseNo = cfsWarehouseNo;
    }

    public String getSpReferenceNo() {
        return spReferenceNo;
    }

    public void setSpReferenceNo(String spReferenceNo) {
        this.spReferenceNo = spReferenceNo;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getPickupFlag() {
        return pickupFlag;
    }

    public void setPickupFlag(String pickupFlag) {
        this.pickupFlag = pickupFlag;
    }

    public String getSmallParcelRemarks() {
        return smallParcelRemarks;
    }

    public void setSmallParcelRemarks(String smallParcelRemarks) {
        this.smallParcelRemarks = smallParcelRemarks;
    }

//    public String getVoyagesBillinTerminalNoSearch() {
//        return voyagesBillinTerminalNoSearch;
//    }
//
//    public void setVoyagesBillinTerminalNoSearch(String voyagesBillinTerminalNoSearch) {
//        this.voyagesBillinTerminalNoSearch = voyagesBillinTerminalNoSearch;
//    }
//
//    public String getVoyagesBillinTerminalSearch() {
//        return voyagesBillinTerminalSearch;
//    }
//
//    public void setVoyagesBillinTerminalSearch(String voyagesBillinTerminalSearch) {
//        this.voyagesBillinTerminalSearch = voyagesBillinTerminalSearch;
//    }
//
//    public String getVoyagesDestinationIdSearch() {
//        return voyagesDestinationIdSearch;
//    }
//
//    public void setVoyagesDestinationIdSearch(String voyagesDestinationIdSearch) {
//        this.voyagesDestinationIdSearch = voyagesDestinationIdSearch;
//    }
//
//    public String getVoyagesDestinationNameSearch() {
//        return voyagesDestinationNameSearch;
//    }
//
//    public void setVoyagesDestinationNameSearch(String voyagesDestinationNameSearch) {
//        this.voyagesDestinationNameSearch = voyagesDestinationNameSearch;
//    }
//
//    public String getVoyagesDispositionIdSearch() {
//        return voyagesDispositionIdSearch;
//    }
//
//    public void setVoyagesDispositionIdSearch(String voyagesDispositionIdSearch) {
//        this.voyagesDispositionIdSearch = voyagesDispositionIdSearch;
//    }
//
//    public String getVoyagesDispositionSearch() {
//        return voyagesDispositionSearch;
//    }
//
//    public void setVoyagesDispositionSearch(String voyagesDispositionSearch) {
//        this.voyagesDispositionSearch = voyagesDispositionSearch;
//    }
//
//    public String getVoyagesOriginIdSearch() {
//        return voyagesOriginIdSearch;
//    }
//
//    public void setVoyagesOriginIdSearch(String voyagesOriginIdSearch) {
//        this.voyagesOriginIdSearch = voyagesOriginIdSearch;
//    }
//
//    public String getVoyagesOriginNameSearch() {
//        return voyagesOriginNameSearch;
//    }
//
//    public void setVoyagesOriginNameSearch(String voyagesOriginNameSearch) {
//        this.voyagesOriginNameSearch = voyagesOriginNameSearch;
//    }
//
//    public String getVoyagesVoyageNoSearch() {
//        return voyagesVoyageNoSearch;
//    }
//
//    public void setVoyagesVoyageNoSearch(String voyagesVoyageNoSearch) {
//        this.voyagesVoyageNoSearch = voyagesVoyageNoSearch;
//    }
//
//    public String getVoyagesUnitNoSearch() {
//        return voyagesUnitNoSearch;
//    }
//
//    public void setVoyagesUnitNoSearch(String voyagesUnitNoSearch) {
//        this.voyagesUnitNoSearch = voyagesUnitNoSearch;
//    }
    public boolean isIpiLoadedContainer() {
        return ipiLoadedContainer;
    }

    public void setIpiLoadedContainer(boolean ipiLoadedContainer) {
        this.ipiLoadedContainer = ipiLoadedContainer;
    }

    public String getIpiATDDate() {
        return ipiATDDate;
    }

    public void setIpiATDDate(String ipiATDDate) {
        this.ipiATDDate = ipiATDDate;
    }

    public String getDoorDeliveryComment() {
        return doorDeliveryComment;
    }

    public void setDoorDeliveryComment(String doorDeliveryComment) {
        this.doorDeliveryComment = doorDeliveryComment;
    }

//    public String getVoyagesMasterBlSearch() {
//        return voyagesMasterBlSearch;
//    }
//
//    public void setVoyagesMasterBlSearch(String voyagesMasterBlSearch) {
//        this.voyagesMasterBlSearch = voyagesMasterBlSearch;
//    }
//
//    public String getVoyagesAgentNoSearch() {
//        return voyagesAgentNoSearch;
//    }
//
//    public void setVoyagesAgentNoSearch(String voyagesAgentNoSearch) {
//        this.voyagesAgentNoSearch = voyagesAgentNoSearch;
//    }
    public String getCfsWarehouseCoName() {
        return cfsWarehouseCoName;
    }

    public void setCfsWarehouseCoName(String cfsWarehouseCoName) {
        this.cfsWarehouseCoName = cfsWarehouseCoName;
    }

    public String getIpiLoadNo() {
        return ipiLoadNo;
    }

    public void setIpiLoadNo(String ipiLoadNo) {
        this.ipiLoadNo = ipiLoadNo;
    }

    public String getPickedUpBy() {
        return pickedUpBy;
    }

    public void setPickedUpBy(String pickedUpBy) {
        this.pickedUpBy = pickedUpBy;
    }

    public String getUnitWarehouseCoName() {
        return unitWarehouseCoName;
    }

    public void setUnitWarehouseCoName(String unitWarehouseCoName) {
        this.unitWarehouseCoName = unitWarehouseCoName;
    }

    public String getImportCreditShipper() {
        return importCreditShipper;
    }

    public void setImportCreditShipper(String importCreditShipper) {
        this.importCreditShipper = importCreditShipper;
    }

    public String getImportCreditConsignee() {
        return importCreditConsignee;
    }

    public void setImportCreditConsignee(String importCreditConsignee) {
        this.importCreditConsignee = importCreditConsignee;
    }

    public String getImportCreditNotify() {
        return importCreditNotify;
    }

    public void setImportCreditNotify(String importCreditNotify) {
        this.importCreditNotify = importCreditNotify;
    }

    public String getImportCreditNotify2() {
        return importCreditNotify2;
    }

    public void setImportCreditNotify2(String importCreditNotify2) {
        this.importCreditNotify2 = importCreditNotify2;
    }

    public boolean isDestinationServices() throws Exception {
        boolean ischargeAvailable = false;
        if (CommonUtils.isNotEmpty(this.fileNumberId)) {
            ischargeAvailable = new LclCostChargeDAO().isDestinationChargeContain(Long.parseLong(this.fileNumberId));
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getIsFormchangeFlag() {
        return isFormchangeFlag;
    }

    public void setIsFormchangeFlag(String isFormchangeFlag) {
        this.isFormchangeFlag = isFormchangeFlag;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getEculineCommodity() {
        return eculineCommodity;
    }

    public void setEculineCommodity(String eculineCommodity) {
        this.eculineCommodity = eculineCommodity;
    }

    public String getBackToVoyage() {
        return backToVoyage;
    }

    public void setBackToVoyage(String backToVoyage) {
        this.backToVoyage = backToVoyage;
    }

    public String getToScreenName() {
        return toScreenName;
    }

    public void setToScreenName(String toScreenName) {
        this.toScreenName = toScreenName;
    }

    public Long getConsolidatedId() {
        return consolidatedId;
    }

    public void setConsolidatedId(Long consolidatedId) {
        this.consolidatedId = consolidatedId;
    }

    public String getFromScreen() {
        return fromScreen;
    }

    public void setFromScreen(String fromScreen) {
        this.fromScreen = fromScreen;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getInTransitDr() {
        return inTransitDr;
    }

    public void setInTransitDr(String inTransitDr) {
        this.inTransitDr = inTransitDr;
    }

    public String getSearchAgentNo() {
        return searchAgentNo;
    }

    public void setSearchAgentNo(String searchAgentNo) {
        this.searchAgentNo = searchAgentNo;
    }

    public String getSearchDispoId() {
        return searchDispoId;
    }

    public void setSearchDispoId(String searchDispoId) {
        this.searchDispoId = searchDispoId;
    }

    public String getSearchFdId() {
        return searchFdId;
    }

    public void setSearchFdId(String searchFdId) {
        this.searchFdId = searchFdId;
    }

    public String getSearchLoginId() {
        return searchLoginId;
    }

    public void setSearchLoginId(String searchLoginId) {
        this.searchLoginId = searchLoginId;
    }

    public String getSearchMasterBl() {
        return searchMasterBl;
    }

    public void setSearchMasterBl(String searchMasterBl) {
        this.searchMasterBl = searchMasterBl;
    }

    public String getSearchOriginId() {
        return searchOriginId;
    }

    public void setSearchOriginId(String searchOriginId) {
        this.searchOriginId = searchOriginId;
    }

    public String getSearchTerminalNo() {
        return searchTerminalNo;
    }

    public void setSearchTerminalNo(String searchTerminalNo) {
        this.searchTerminalNo = searchTerminalNo;
    }

    public String getSearchUnitNo() {
        return searchUnitNo;
    }

    public void setSearchUnitNo(String searchUnitNo) {
        this.searchUnitNo = searchUnitNo;
    }

    public String getSearchVoyageNo() {
        return searchVoyageNo;
    }

    public void setSearchVoyageNo(String searchVoyageNo) {
        this.searchVoyageNo = searchVoyageNo;
    }

    public String getSearchFd() {
        return searchFd;
    }

    public void setSearchFd(String searchFd) {
        this.searchFd = searchFd;
    }

    public String getSearchOrigin() {
        return searchOrigin;
    }

    public void setSearchOrigin(String searchOrigin) {
        this.searchOrigin = searchOrigin;
    }

    public String getSearchTerminal() {
        return searchTerminal;
    }

    public void setSearchTerminal(String searchTerminal) {
        this.searchTerminal = searchTerminal;
    }

    public String getFdEciPortCode() {
        return fdEciPortCode;
    }

    public void setFdEciPortCode(String fdEciPortCode) {
        this.fdEciPortCode = fdEciPortCode;
    }

    public String getFdEngmet() {
        return fdEngmet;
    }

    public void setFdEngmet(String fdEngmet) {
        this.fdEngmet = fdEngmet;
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

    public Boolean getCob() {
        return cob;
    }

    public void setCob(Boolean cob) {
        this.cob = cob;
    }

    public String getOldFdUnlocationCode() {
        return oldFdUnlocationCode;
    }

    public void setOldFdUnlocationCode(String oldFdUnlocationCode) {
        this.oldFdUnlocationCode = oldFdUnlocationCode;
    }

    public String getCopyRates() {
        return copyRates;
    }

    public void setCopyRates(String copyRates) {
        this.copyRates = copyRates;
    }

    public String getCopyRatesDrNo() {
        return copyRatesDrNo;
    }

    public void setCopyRatesDrNo(String copyRatesDrNo) {
        this.copyRatesDrNo = copyRatesDrNo;
    }

    public String getStorageDatetime() {
        return storageDatetime;
    }

    public void setStorageDatetime(String storageDatetime) {
        this.storageDatetime = storageDatetime;
    }

    public String getBatchHsCode() {
        return batchHsCode;
    }

    public void setBatchHsCode(String batchHsCode) {
        this.batchHsCode = batchHsCode;
    }

    public String getBatchFileNo() {
        return batchFileNo;
    }

    public void setBatchFileNo(String batchFileNo) {
        this.batchFileNo = batchFileNo;
    }

    public String getCompanyCode() throws Exception {
        companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (moduleName.equalsIgnoreCase("Imports")) {
            if (CommonUtils.isNotEmpty(fileNumberId)) {
                this.businessUnit = new LclFileNumberDAO().getBusinessUnit(fileNumberId);
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

    public boolean isFormChangeFlag() {
        return formChangeFlag;
    }

    public void setFormChangeFlag(boolean formChangeFlag) {
        this.formChangeFlag = formChangeFlag;
    }

    public boolean isHomeScreenDrFileFlag() {
        return homeScreenDrFileFlag;
    }

    public void setHomeScreenDrFileFlag(boolean homeScreenDrFileFlag) {
        this.homeScreenDrFileFlag = homeScreenDrFileFlag;
    }

    public String getManualDoorOriginCityZip() {
        return manualDoorOriginCityZip;
    }

    public void setManualDoorOriginCityZip(String manualDoorOriginCityZip) {
        this.manualDoorOriginCityZip = manualDoorOriginCityZip;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getAgentBrand() {
        return agentBrand;
    }

    public void setAgentBrand(String agentBrand) {
        this.agentBrand = agentBrand;
    }

    public String getEdiButton() {
        return ediButton;
    }

    public void setEdiButton(String ediButton) {
        this.ediButton = ediButton;
    }

    public String getHotCodeComments() {
        return hotCodeComments;
    }

    public void setHotCodeComments(String hotCodeComments) {
        this.hotCodeComments = hotCodeComments;
    }

    public String getHoldComments() {
        return holdComments;
    }

    public void setHoldComments(String holdComments) {
        this.holdComments = holdComments;
    }

    public boolean isOsdValues() {
        return osdValues;
    }

    public void setOsdValues(boolean osdValues) {
        this.osdValues = osdValues;
    }

    public Long getHotCodeId() {
        return hotCodeId;
    }

    public void setHotCodeId(Long hotCodeId) {
        this.hotCodeId = hotCodeId;
    }

    public String getSaveRemarks() {
        return saveRemarks;
    }

    public void setSaveRemarks(String saveRemarks) {
        this.saveRemarks = saveRemarks;
    }

    public String getCifValue() {
        if (lclBooking.getCifValue() != null) {
            return "" + lclBooking.getCifValue();
        }
        return "";
    }

    public void setCifValue(String cifValue) {
        if (CommonUtils.isNotEmpty(cifValue)) {
            lclBooking.setCifValue(new BigDecimal(cifValue));
        } else {
            lclBooking.setCifValue(null);
        }
    }

    public String getBatchHotCode() {
        return batchHotCode;
    }

    public void setBatchHotCode(String batchHotCode) {
        this.batchHotCode = batchHotCode;
    }

    public String getDispoPopUpId() {
        return dispoPopUpId;
    }

    public void setDispoPopUpId(String dispoPopUpId) {
        this.dispoPopUpId = dispoPopUpId;
    }

    public String getDispoDateTime() {
        return dispoDateTime;
    }

    public void setDispoDateTime(String dispoDateTime) {
        this.dispoDateTime = dispoDateTime;
    }

    public String getDispoWareHouse() {
        return dispoWareHouse;
    }

    public void setDispoWareHouse(String dispoWareHouse) {
        this.dispoWareHouse = dispoWareHouse;
    }

    public String getDispoCityCode() {
        return dispoCityCode;
    }

    public void setDispoCityCode(String dispoCityCode) {
        this.dispoCityCode = dispoCityCode;
    }

    public Boolean getCfclForDR() {
        return cfclForDR;
    }

    public void setCfclForDR(Boolean cfclForDR) {
        this.cfclForDR = cfclForDR;
    }

    public String getCfclAcctNameForDr() {
        return cfclAcctNameForDr;
    }

    public void setCfclAcctNameForDr(String cfclAcctNameForDr) {
        this.cfclAcctNameForDr = cfclAcctNameForDr;
    }

    public String getCfclAcctNoForDr() {
        return cfclAcctNoForDr;
    }

    public void setCfclAcctNoForDr(String cfclAcctNoForDr) {
        this.cfclAcctNoForDr = cfclAcctNoForDr;
    }

    public String getUnPickedFiles() {
        return unPickedFiles;
    }

    public void setUnPickedFiles(String unPickedFiles) {
        this.unPickedFiles = unPickedFiles;
    }

    public boolean isChangeBlCharge() {
        return changeBlCharge;
    }

    public void setChangeBlCharge(boolean changeBlCharge) {
        this.changeBlCharge = changeBlCharge;
    }

}
