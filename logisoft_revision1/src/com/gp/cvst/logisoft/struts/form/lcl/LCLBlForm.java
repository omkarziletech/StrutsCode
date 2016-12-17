package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import static com.gp.cong.lcl.common.constant.CommonConstant.FALSE;
import static com.gp.cong.lcl.common.constant.CommonConstant.N;
import static com.gp.cong.lcl.common.constant.CommonConstant.TRUE;
import static com.gp.cong.lcl.common.constant.CommonConstant.Y;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

public class LCLBlForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LCLBlForm.class);
    private LclBl lclBl;
    private LclContact pickupContact;
    private Long fileNumberId;
    private String blOwner;
    private String termsType1;
    private String termsType2;
    private String type2Date;
    private LclContact lclContact;
    private Lcl3pRefNo lcl3pRefNo;
    private boolean showFullRelay;
    private boolean showFullRelayFd;
    private String defaultAgent;
    private Long commId;
    private String ssVoyage1;
    private String vesselName1;
    private String ssLine1;
    private String sailDate1;
    private String eciVoyage1;
    private String terminal;
    private String clientWithConsignee;
    private String editShipAcctName;
    private String editConsAcctName;
    private String editNotyAcctName;
    private String editFwdAcctName;
    private String clientWithoutConsignee;
    private String clientCompany;
    private String tempClientCompany;
    private String clientConsigneeCompany;
    private String osdRemarks;
    private String bundleToOf;
    private String printOnBL;
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
    private Long CustId;
    private String unlocationCode;
    private String thirdPartyname;
    private String thirdpartyaccountNo;
    private String deliverCargoTo;
    private String shipper_contactName;
    private String originCode;
    private String destinationCode;
    private String unlocationName;
    private String podUnlocationcode;
    private String trn;
    private String shpDr;
    private String screenType;
    private String pier;
    private String polUnlocationcode;
    private String basedOnCity;
    private String duplicateDoorOrigin;
    private String index;
    private String pooCode;
    private String shipContactaddress;
    private String eReference;
    private String routingInstruction;
    private String fmc;
    private String enteredByName;
    private Integer enteredById;
    private String agentAddress;
    private String agentAddressConcat;
    private String agentPhone;
    private String agentFax;
    private String pointOfOrigin;
    private String newNotify;
    private String sameasConsignee;
    private String newConsignee;
    private String newShipper;
    private String notifyContactName;
    private String consContactName;
    private String shipContactName;
    private String ediShipperCheck;
    private String ediConsigneeCheck;
    private String ediNotifyCheck;
    private String ediForwarderCheck;
    private String consignee_contactName;
    private String voyageNumber;
    private String unitNumber;
    private String manifestedBy;
    private String fileNumber;
    private String lcl3pRefId;
    private String noteId;
    private String manifestedOn;
    private String aesCodes;
    private String hsCodes;
    private String ncmCodes;
    private String freightPickup;
    private String aesText;
    private String hsText;
    private String ncmText;
    private String freightPickupText;
    private String freightPickupAccountNo;
    private String sealNumber;
    private List termsTypeList;
    private String thirdPNameFlag;
    private String blOwnerName;
    private Integer blOwnerId;
    private String blNumber;
    private boolean blUnitCob;
    private String highVolumedis;
    private Long unitSsId;
    private Long bkgPieceUnitId;
    private String filterByChanges;
    private boolean docsBl;
    private boolean docsAes;
    private boolean docsCaricom;
    private String ratesFromTerminal;
    private String ratesFromTerminalNo;
    private Integer bookingFileNumberId;
    private String voyageClosedUser;
    private String hblPier;
    private String hblPierText;
    private String hblPol;
    private String hblPolText;
    private String deliveryOverride;
    private String deliveryText;
    private String ladenSailDate;
    private String consolidatedBlFileNo;
    private String printTermsType;
    private String correctedBl;
    private String editRateType;
    private String deliveryMetro = N;
    private String deliveryMetroField;
    private String invoiceValue;
    private String cifValue;   
    private boolean printFlag;
    private String printInsurance;

    public String getAesText() {
        return aesText;
    }

    public void setAesText(String aesText) {
        this.aesText = aesText;
    }

    public String getFreightPickupText() {
        return freightPickupText;
    }

    public void setFreightPickupText(String freightPickupText) {
        this.freightPickupText = freightPickupText;
    }

    public String getHsText() {
        return hsText;
    }

    public void setHsText(String hsText) {
        this.hsText = hsText;
    }

    public String getNcmText() {
        return ncmText;
    }

    public void setNcmText(String ncmText) {
        this.ncmText = ncmText;
    }

    public String getAesCodes() {
        return aesCodes;
    }

    public void setAesCodes(String aesCodes) {
        this.aesCodes = aesCodes;
    }

    public String getFreightPickup() {
        return freightPickup;
    }

    public void setFreightPickup(String freightPickup) {
        this.freightPickup = freightPickup;
    }

    public String getHsCodes() {
        return hsCodes;
    }

    public void setHsCodes(String hsCodes) {
        this.hsCodes = hsCodes;
    }

    public String getNcmCodes() {
        return ncmCodes;
    }

    public void setNcmCodes(String ncmCodes) {
        this.ncmCodes = ncmCodes;
    }

    public String getManifestedBy() {
        return manifestedBy;
    }

    public void setManifestedBy(String manifestedBy) {
        this.manifestedBy = manifestedBy;
    }

    public String getManifestedOn() {
        return manifestedOn;
    }

    public void setManifestedOn(String manifestedOn) {
        this.manifestedOn = manifestedOn;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public Long getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(Long unitSsId) {
        this.unitSsId = unitSsId;
    }

    public Long getBkgPieceUnitId() {
        return bkgPieceUnitId;
    }

    public void setBkgPieceUnitId(Long bkgPieceUnitId) {
        this.bkgPieceUnitId = bkgPieceUnitId;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAgentAddressConcat() {
        if (lclBl.getAgentContact() != null && lclBl.getAgentContact().getId() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getAgentContact().getCompanyName())) {
                builder.append(lclBl.getAgentContact().getCompanyName()).append(",").append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBl.getAgentContact().getAddress())) {
                builder.append(lclBl.getAgentContact().getAddress()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclBl.getAgentContact().getCity())) {
                builder.append(lclBl.getAgentContact().getCity()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclBl.getAgentContact().getState())) {
                builder.append(lclBl.getAgentContact().getState()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclBl.getAgentContact().getZip())) {
                builder.append(lclBl.getAgentContact().getZip());
            }
            return builder.toString();
        } else {
            return "";
        }

    }

    public void setAgentAddressConcat(String agentAddressConcat) {
        this.agentAddressConcat = agentAddressConcat;
    }

    public String getPointOfOrigin() {
        return pointOfOrigin;
    }

    public void setPointOfOrigin(String pointOfOrigin) {
        if (CommonUtils.isNotEmpty(pointOfOrigin)) {
            lclBl.setPointOfOrigin(pointOfOrigin);
        } else {
            lclBl.setPointOfOrigin("U.S.A");
        }
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public String getAgentFax() {
        return agentFax;
    }

    public void setAgentFax(String agentFax) {
        this.agentFax = agentFax;
    }

    public String getEditShipAcctName() {
        return editShipAcctName;
    }

    public void setEditShipAcctName(String editShipAcctName) {
        this.editShipAcctName = editShipAcctName;
    }

    public String getEditConsAcctName() {
        return editConsAcctName;
    }

    public void setEditConsAcctName(String editConsAcctName) {
        this.editConsAcctName = editConsAcctName;
    }

    public String getEditFwdAcctName() {
        return editFwdAcctName;
    }

    public void setEditFwdAcctName(String editFwdAcctName) {
        this.editFwdAcctName = editFwdAcctName;
    }

    public String getEditNotyAcctName() {
        return editNotyAcctName;
    }

    public void setEditNotyAcctName(String editNotyAcctName) {
        this.editNotyAcctName = editNotyAcctName;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }
    private String agentEmail;

    public LCLBlForm() {
        if (lclBl == null) {
            lclBl = new LclBl();
        }
    }

    public String getRtdTransaction() {
        if (lclBl.getRtdTransaction() != null && lclBl.getRtdTransaction()) {
            return Y;
        } else if (lclBl.getRtdTransaction() != null && !lclBl.getRtdTransaction()) {
            return N;
        }
        return "";
    }

    public void setRtdTransaction(String rtdTransaction) {
        if (rtdTransaction.equals("Y")) {
            lclBl.setRtdTransaction(TRUE);
        } else if (rtdTransaction.equals("N")) {
            lclBl.setRtdTransaction(FALSE);
        } else {
            lclBl.setRtdTransaction(null);
        }
    }

    public String getTermsType1() {
        return (lclBl.getTermsType1());
    }

    public void setTermsType1(String termsType1) {
        if (CommonUtils.isNotEmpty(termsType1)) {
            lclBl.setTermsType1(termsType1);
        } else {
            lclBl.setTermsType1("");
        }
    }

    public Integer getEnteredById() {
        return enteredById;
    }

    public void setEnteredById(Integer enteredById) {
        this.enteredById = enteredById;
    }

    public String getEnteredByName() {
        return enteredByName;
    }

    public void setEnteredByName(String enteredByName) {
        this.enteredByName = enteredByName;
    }

    public String getTermsType2() {
        return (lclBl.getTermsType2());
    }

    public void setTermsType2(String termsType2) {
        if (CommonUtils.isNotEmpty(termsType2)) {
            lclBl.setTermsType2(termsType2);
        } else {
            lclBl.setTermsType2("");
        }
    }

    public String getType2Date() throws Exception {
        return DateUtils.formatStringDateToAppFormatMMM(lclBl.getType2Date());
    }

    public void setType2Date(String type2Date) throws Exception {
        if (CommonUtils.isNotEmpty(type2Date)) {
            lclBl.setType2Date(DateUtils.parseDate(type2Date, "dd-MMM-yyyy"));
        } else {
            lclBl.setType2Date(null);
        }
    }

    public String getShpDr() {
        return shpDr;
    }

    public void setShpDr(String shpDr) {
        this.shpDr = shpDr;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
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
        if (lclBl.getOverShortdamaged() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setOverShortdamaged(String overShortdamaged) {
        if (overShortdamaged.equals("Y")) {
            lclBl.setOverShortdamaged(TRUE);
        } else {
            lclBl.setOverShortdamaged(FALSE);
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
        return lclBl.getClientContact().getCity();
    }

    public void setCity(String city) {
        lclBl.getClientContact().setCity(city);
    }

    public String getState() {
        return lclBl.getClientContact().getState();
    }

    public void setState(String state) {
        lclBl.getClientContact().setState(state);
    }

    public String getZip() {
        return lclBl.getClientContact().getZip();
    }

    public void setZip(String zip) {
        lclBl.getClientContact().setZip(zip);
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public LclBl getlclBl() {
        if (lclBl == null) {
            lclBl = new LclBl();
        }
        return lclBl;
    }

    public void setLclBl(LclBl lclBl) {
        this.lclBl = lclBl;
    }

    public LclContact getLclContact() {
        return lclContact;
    }

    public void setLclContact(LclContact lclContact) {
        this.lclContact = lclContact;
    }

    public String getBookingType() {
        return lclBl.getBookingType();
    }

    public void setBookingType(String bookingType) {
        lclBl.setBookingType(bookingType);
    }

    public String getTransMode() {
        return lclBl.getTransMode();
    }

    public void setTransMode(String transMode) {
        if (CommonUtils.isEmpty(transMode)) {
            transMode = T;
        }
        lclBl.setTransMode(transMode);
    }

    public String getClientPwkRecvd() {
        if (lclBl.getClientPwkRecvd() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setClientPwkRecvd(String clientPwkRecvd) {
        if (clientPwkRecvd.equals(Y)) {
            lclBl.setClientPwkRecvd(TRUE);
        } else {
            lclBl.setClientPwkRecvd(FALSE);
        }
    }

    public String getSpotRate() {
        if (lclBl.getSpotRate() == TRUE) {
            return "Y";
        }
        return "N";
    }

    public void setSpotRate(String spotRate) {
        if (spotRate.equals("Y")) {
            lclBl.setSpotRate(TRUE);
        } else {
            lclBl.setSpotRate(FALSE);
        }
    }

    public String getFreeBl() {
        if (lclBl.isFreeBL() == TRUE) {
            return "Y";
        }
        return "N";
    }

    public void setFreeBl(String freeBl) {
        if (freeBl.equals("Y")) {
            lclBl.setFreeBL(TRUE);
        } else {
            lclBl.setFreeBL(FALSE);
        }
    }

    public String getRateType() {
        return lclBl.getRateType();//in screen name is CTC/Retail/FTF
    }

    public void setRateType(String rateType) {
        if (CommonUtils.isNotEmpty(rateType)) {
            lclBl.setRateType(rateType);
        }
    }

    public String getRelayOverride() {
        if (lclBl.getRelayOverride() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setRelayOverride(String relayOverride) {
        if (relayOverride.equals("true")) {
            lclBl.setRelayOverride(TRUE);
        } else {
            lclBl.setRelayOverride(FALSE);
        }
    }

    public String getBillingType() {
        return lclBl.getBillingType();
    }

    public void setBillingType(String billingType) {
        if (CommonUtils.isEmpty(billingType)) {
            billingType = "B";
        }
        lclBl.setBillingType(billingType);
    }

    public String getTerminal() {
        if (lclBl.getTerminal() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getTerminal().getTerminalLocation()) && CommonUtils.isNotEmpty(lclBl.getTerminal().getTrmnum())) {
                builder.append(lclBl.getTerminal().getTerminalLocation() + "/" + lclBl.getTerminal().getTrmnum());
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
        if (lclBl.getTerminal() != null) {
            return lclBl.getTerminal().getTrmnum();
        }
        return null;
    }

    public void setTrmnum(String trmnum) throws Exception {
        if (CommonUtils.isNotEmpty(trmnum)) {
            RefTerminal terminal = new RefTerminalDAO().findById(trmnum);
            lclBl.setTerminal(terminal);
        }
    }

    public String getDefaultAgent() {
        if (lclBl.getDefaultAgent() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setDefaultAgent(String defaultAgent) {
        if (defaultAgent.equals(Y)) {
            lclBl.setDefaultAgent(TRUE);
        } else {
            lclBl.setDefaultAgent(FALSE);
        }
    }

    public String getInsurance() {
        if (lclBl.getInsurance() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setInsurance(String insurance) {
        if (insurance.equals(Y)) {
            lclBl.setInsurance(TRUE);
        }
    }

    public String getDocumentation() {
        if (lclBl.getDocumentation() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setDocumentation(String documentation) {
        if (Y.equals(documentation)) {
            lclBl.setDocumentation(TRUE);
        } else {
            lclBl.setDocumentation(FALSE);
        }
    }

    public String getValueOfGoods() {
        if (lclBl.getValueOfGoods() != null) {
            return "" + lclBl.getValueOfGoods();
        }
        return "";
    }

    public void setValueOfGoods(String valueOfGoods) {
        if (CommonUtils.isNotEmpty(valueOfGoods)) {
            lclBl.setValueOfGoods(new BigDecimal(valueOfGoods));
        } else {
            lclBl.setValueOfGoods(null);
        }
    }

    public BigDecimal getDeclaredCargoValue() {
        return lclBl.getDeclaredCargoValue();
    }

    public void setDeclaredCargoValue(BigDecimal declaredCargoValue) {
        lclBl.setDeclaredCargoValue(declaredCargoValue);
    }

    public String getSupReference() {
        return lclBl.getSupReference();
    }

    public void setSupReference(String supReference) {
        lclBl.setSupReference(supReference);
    }

    public String getShipReference() {
        return lclBl.getShipReference();
    }

    public void setShipReference(String shipReference) {
        lclBl.setShipReference(shipReference);
    }

    public String getFwdFmcNo() {
        return lclBl.getFwdFmcNo();
    }

    public void setFwdFmcNo(String fwdFmcNo) {
        lclBl.setFwdFmcNo(fwdFmcNo);
    }

    public String getFwdReference() {
        return lclBl.getFwdReference();
    }

    public void setFwdReference(String fwdReference) {
        lclBl.setFwdReference(fwdReference);
    }

    public String getConsReference() {
        return lclBl.getConsReference();
    }

    public void setConsReference(String consReference) {
        lclBl.setConsReference(consReference);
    }

    public String getNotyReference() {
        return lclBl.getNotyReference();
    }

    public void setNotyReference(String notyReference) {
        lclBl.setNotyReference(notyReference);
    }

    public String getOverrideDimType() {
        return lclBl.getOverrideDimUom();
    }

    public void setOverrideDimType(String overrideDimType) {
        lclBl.setOverrideDimUom(overrideDimType);
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

    public BigDecimal getOverrideDimCubic() {
        return lclBl.getOverrideDimVolume();
    }

    public void setOverrideDimCubic(BigDecimal overrideDimCubic) {
        lclBl.setOverrideDimVolume(overrideDimCubic);
    }

    public BigDecimal getOverrideDimWeight() {
        return lclBl.getOverrideDimWeight();
    }

    public void setOverrideDimWeight(BigDecimal overrideDimWeight) {
        lclBl.setOverrideDimWeight(overrideDimWeight);
    }

    public String getAgentAcct() {
        return lclBl.getAgentAcct().getAccountno();
    }

    public void setAgentAcct(String agentAcct) throws Exception {
        if (CommonUtils.isNotEmpty(agentAcct)) {
            lclBl.setAgentAcct(new TradingPartnerDAO().findById(agentAcct));
        } else {
            lclBl.setAgentAcct(null);
        }
    }

    public String getRtdAgentAcct() {
        return lclBl.getRtdAgentAcct().getAccountno();
    }

    public void setRtdAgentAcct(String rtdAgentAcct) throws Exception {
        if (CommonUtils.isNotEmpty(rtdAgentAcct)) {
            lclBl.setRtdAgentAcct(new TradingPartnerDAO().findById(rtdAgentAcct));
        } else {
            lclBl.setRtdAgentAcct(null);
        }
    }

    public LclContact getClientContact() {
        return lclBl.getClientContact();
    }

    public void setClientContact(LclContact clientContact) {
        lclBl.setClientContact(clientContact);
    }

    public String getConsAcct() {
        return lclBl.getConsAcct().getAccountno();
    }

    public void setConsAcct(String consAcct) {
        if (CommonUtils.isNotEmpty(consAcct)) {
            lclBl.setConsAcct(new TradingPartner(consAcct));
        } else {
            lclBl.setConsAcct(null);
        }
    }

    public LclContact getConsContact() {
        return lclBl.getConsContact();
    }

    public void setConsContact(LclContact consContact) {
        lclBl.setConsContact(consContact);
    }

//    public LclContact getDoorDestinationContact() {
//        return lclBl.getDoorDestinationContact();
//    }
//
//    public void setDoorDestinationContact(LclContact doorDestinationContact) {
//        lclBl.setDoorDestinationContact(doorDestinationContact);
//    }
//    public LclContact getDoorOriginContact() {
//        return lclBl.getDoorOriginContact();
//    }
//
//    public void setDoorOriginContact(LclContact doorOriginContact) {
//        lclBl.setDoorOriginContact(doorOriginContact);
//    }
    public String getFwdAcct() {
        return lclBl.getFwdAcct().getAccountno();
    }

    public void setFwdAcct(String fwdAcct) {
        if (CommonUtils.isNotEmpty(fwdAcct)) {
            lclBl.setFwdAcct(new TradingPartner(fwdAcct));
        } else {
            lclBl.setFwdAcct(null);
        }
    }

    public LclContact getFwdContact() {
        return lclBl.getFwdContact();
    }

    public void setFwdContact(LclContact fwdContact) {
        lclBl.setFwdContact(fwdContact);
    }
    private LclFileNumber lclFileNumber;

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public String getNotyAcct() {
        return lclBl.getNotyAcct().getAccountno();
    }

    public void setNotyAcct(String notyAcct) {
        if (CommonUtils.isNotEmpty(notyAcct)) {
            lclBl.setNotyAcct(new TradingPartner(notyAcct));
        } else {
            lclBl.setNotyAcct(null);
        }
    }

    public LclContact getNotyContact() {
        return lclBl.getNotyContact();
    }

    public void setNotyContact(LclContact notyContact) {
        lclBl.setNotyContact(notyContact);
    }

    public LclContact getPickupContact() {
        return pickupContact;
    }

    public void setPickupContact(LclContact pickupContact) {
        this.pickupContact = pickupContact;
    }

    public String getShipAcct() {
        return lclBl.getShipAcct().getAccountno();
    }

    public void setShipAcct(String shipAcct) {
        if (CommonUtils.isNotEmpty(shipAcct)) {
            lclBl.setShipAcct(new TradingPartner(shipAcct));
        } else {
            lclBl.setShipAcct(null);
        }
    }

    public LclContact getShipContact() {
        return lclBl.getShipContact();
    }

    public void setShipContact(LclContact shipContact) {
        lclBl.setShipContact(shipContact);
    }

    public String getSupAcct() {
        return lclBl.getSupAcct().getAccountno();
    }

    public void setSupAcct(String supAcct) {
        if (CommonUtils.isNotEmpty(supAcct)) {
            lclBl.setSupAcct(new TradingPartner(supAcct));
        } else {
            lclBl.setSupAcct(null);
        }
    }

    public LclContact getSupContact() {
        return lclBl.getSupContact();
    }

    public void setSupContact(LclContact supContact) {
        lclBl.setSupContact(supContact);
    }

    public LclContact getAgentContact() {
        return lclBl.getAgentContact();
    }

    public void setAgentContact(LclContact agentContact) {
        lclBl.setAgentContact(agentContact);
    }

    public String getClientAcct() {
        return lclBl.getClientAcct().getAccountno();
    }

    public void setClientAcct(String clientAcct) {
        if (CommonUtils.isNotEmpty(clientAcct)) {
            lclBl.setClientAcct(new TradingPartner(clientAcct));
        } else {
            lclBl.setClientAcct(null);
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
        return lclBl.getClientContact().getCompanyName();
    }

    public void setClientCompany(String clientCompany) {
        lclBl.getClientContact().setCompanyName(clientCompany);
    }

    public String getTempClientCompany() {
        return tempClientCompany;
    }

    public void setTempClientCompany(String tempClientCompany) {
        this.tempClientCompany = tempClientCompany;
    }
    // Client Section properties need to save in the database
    private Boolean cfcl;
    private String otiNumber;
    private String fmcNumber;
    private String commodityNumber;
    private String aesBy;
    private String acctType;
    private String subType;

    public Boolean getCfcl() {
        return cfcl;
    }

    public void setCfcl(Boolean cfcl) {
        this.cfcl = cfcl;
    }

    public String getAesBy() {
        if (CommonUtils.isEmpty(aesBy)) {
            return Y;
        }
        return aesBy;
    }

    public void setAesBy(String aesBy) {
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
    // private String approxDue;
    private String piece;
    private String cube;
    private String weight;
    private String pwk;
    private String ups;
    private String pe;
    private String calcHeavy = Y;
    private String pcBoth;
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
    private String billPPD;

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

    public String getCalcHeavy() {
        return calcHeavy;
    }

    public void setCalcHeavy(String calcHeavy) {
        this.calcHeavy = calcHeavy;
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
        if (lclBl.getBillingType() != null) {
            return lclBl.getBillingType();
        }
        return "P";
    }

    public void setPcBoth(String pcBoth) {
        if (CommonUtils.isNotEmpty(pcBoth)) {
            lclBl.setBillingType(pcBoth);
        }
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getPwk() {
        return pwk;
    }

    public void setPwk(String pwk) {
        this.pwk = pwk;
    }

    public String getERT() {
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
        return lclBl.getAgentAcct().getAccountName();
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

//    public String getApproxDue() {
//        return approxDue;
//    }
//
//    public void setApproxDue(String approxDue) {
//        this.approxDue = approxDue;
//    }
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
        if (lclBl.getFdEta() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBl.getFdEta());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setFdEta(String fdEta) throws Exception {
        if (CommonUtils.isNotEmpty(fdEta)) {
            lclBl.setFdEta(DateUtils.parseDate(fdEta, "dd-MMM-yyyy"));
        }
    }

    public String getPooLrdt() throws Exception {
        if (lclBl.getPooWhseLrdt() != null) {
            String d = DateUtils.formatDate(lclBl.getPooWhseLrdt(), "dd-MMM-yyyy hh:mm");
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPooLrdt(String pooLrdt) throws Exception {
        if (CommonUtils.isNotEmpty(pooLrdt)) {
            lclBl.setPooWhseLrdt(DateUtils.parseDate(pooLrdt, "dd-MMM-yyyy hh:mm"));
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
    private String thirdParty;
    private String agentAccount;
    private String expressBl;

    public String getFinalDestination() {
        if (lclBl.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationName()) && null != lclBl.getFinalDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName() + "/" + lclBl.getFinalDestination().getStateId().getCode() + '(' + lclBl.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationName()) && lclBl.getFinalDestination().getCountryId() != null
                    && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName() + "/" + lclBl.getFinalDestination().getCountryId().getCodedesc() + '(' + lclBl.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName() + '(' + lclBl.getFinalDestination().getUnLocationCode() + ')');
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getOriginCityName() {
        LclUtils lclUtils = new LclUtils();
        return lclUtils.getBlConcatenatedPortOfOrigin(lclBl);
    }

    public Integer getFinalDestinationId() {
        if (lclBl.getFinalDestination() != null) {
            return lclBl.getFinalDestination().getId();
        } else {
            return null;
        }
    }

    public void setFinalDestinationId(Integer finalDestinationId) throws Exception {
        if (CommonUtils.isNotEmpty(finalDestinationId)) {
            lclBl.setFinalDestination(new UnLocationDAO().findById(finalDestinationId));
        }
    }

    public String getRatesFromTerminal() {
        return ratesFromTerminal;
    }

    public void setRatesFromTerminal(String ratesFromTerminal) {
        this.ratesFromTerminal = ratesFromTerminal;
    }

    public String getRatesFromTerminalNo() {
        return ratesFromTerminalNo;
    }

    public void setRatesFromTerminalNo(String ratesFromTerminalNo) {
        this.ratesFromTerminalNo = ratesFromTerminalNo;
    }

    public String getExpressBl() {
        return expressBl;
    }

    public void setExpressBl(String expressBl) {
        this.expressBl = expressBl;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getAgentAccount() {
        return lclBl.getAgentAcct().getAccountno();
    }

    public void setAgentAccount(String agentAccount) {
        if (CommonUtils.isNotEmpty(agentAccount)) {
            lclBl.setAgentAcct(new TradingPartner(agentAccount));
        } else {
            lclBl.setAgentAcct(null);
        }
    }

    public String getThirdPartyname() {
        if (lclBl.getThirdPartyAcct() != null) {
            return lclBl.getThirdPartyAcct().getAccountName();
        }
        return null;
    }

    public void setThirdPartyname(String thirdPartyname) {
        if (CommonUtils.isNotEmpty(thirdPartyname) && lclBl.getThirdPartyAcct() != null) {
            lclBl.getThirdPartyAcct().setAccountName(thirdPartyname);
        }
    }

    public String getThirdPartyAccount() {
        if (lclBl.getThirdPartyAcct() != null) {
            return lclBl.getThirdPartyAcct().getAccountno();

        }
        return null;

    }

    public void setThirdPartyAccount(String thirdPartyAccount) {
        if (CommonUtils.isNotEmpty(thirdPartyAccount)) {
            lclBl.setThirdPartyAcct(new TradingPartner(thirdPartyAccount));

        } else {
            lclBl.setThirdPartyAcct(null);

        }
    }

//    public String getDoorOriginContact() {
//        StringBuilder builder = new StringBuilder();
//        if (null != lclBl && lclBl.getDoorOriginContact() != null && null != lclBl.getDoorOriginContact().getId()) {
//              Zipcode contact = new ZipCodeDAO().findById(lclBl.getDoorOriginContact().getId());
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
//        if (lclBl.getDoorOriginContact() != null) {
//            return lclBl.getDoorOriginContact().getId();
//        } else {
//            return null;
//        }
//    }
//
//    public void setDoorOriginContactId(Integer doorOriginContactId) {
//       if (CommonUtils.isNotEmpty(doorOriginContactId)) {
//            lclBl.setDoorOriginContact(new ZipCodeDAO().findById(doorOriginContactId));
//        }
//    }
    public String getPortOfDestination() {
        if (lclBl.getPortOfDestination() != null) {
            return lclBl.getPortOfDestination().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setPortOfDestination(String portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public Integer getPortOfDestinationId() {
        if (lclBl.getPortOfDestination() != null) {
            return lclBl.getPortOfDestination().getId();
        } else {
            return null;
        }
    }

    public void setPortOfDestinationId(Integer portOfDestinationId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfDestinationId)) {
            lclBl.setPortOfDestination(new UnLocationDAO().findById(portOfDestinationId));
        }
    }

    public String getPortOfLoading() {
        if (lclBl.getPortOfLoading() != null) {
            return lclBl.getPortOfLoading().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public Integer getPortOfLoadingId() {
        if (lclBl.getPortOfLoading() != null) {
            return lclBl.getPortOfLoading().getId();
        } else {
            return null;
        }
    }

    public void setPortOfLoadingId(Integer portOfLoadingId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfLoadingId)) {
            lclBl.setPortOfLoading(new UnLocationDAO().findById(portOfLoadingId));
        }
    }

    public String getPortOfOrigin() {
        if (lclBl.getPortOfOrigin() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationName()) && null != lclBl.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getStateId().getCode()) && CommonUtils.isEmpty(lclBl.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBl.getPortOfOrigin().getUnLocationName() + "/" + lclBl.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationCode()) && null != lclBl.getPortOfOrigin().getStateId() && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getStateId().getCode())) {
                builder.append(lclBl.getPortOfOrigin().getUnLocationName() + "/" + lclBl.getPortOfOrigin().getStateId().getCode());
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
        if (lclBl.getPortOfOrigin() != null) {
            return lclBl.getPortOfOrigin().getId();
        } else {
            return null;
        }
    }

    public void setPortOfOriginId(Integer portOfOriginId) throws Exception {
        if (CommonUtils.isNotEmpty(portOfOriginId)) {
            lclBl.setPortOfOrigin(new UnLocationDAO().findById(portOfOriginId));
        }
    }

    public Long getMasterScheduleNo() {
        if (lclBl.getBookedSsHeaderId() != null) {
            return lclBl.getBookedSsHeaderId().getId();
        }
        return null;
    }

    public void setMasterScheduleNo(Long masterScheduleNo) throws Exception {
        if (CommonUtils.isNotEmpty(masterScheduleNo)) {
            LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(masterScheduleNo);
            lclBl.setBookedSsHeaderId(lclSsHeader);
        } else {
            lclBl.setBookedSsHeaderId(null);
        }
    }

    public String getBillForm() {
        return lclBl.getBillToParty();
    }

    public void setBillForm(String billForm) {
        lclBl.setBillToParty(billForm);
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getUnlocationCode() {
        if (lclBl.getFinalDestination() != null) {
            return lclBl.getFinalDestination().getUnLocationCode();
        } else {
            return lclBl.getPortOfDestination().getUnLocationCode();
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
        if (lclBl.getPooPickup() != null && lclBl.getPooPickup().equals(TRUE)) {
            return Y;
        }
        return N;
    }

    public void setPooDoor(String pooDoor) {
        if (pooDoor.equals(Y)) {
            lclBl.setPooPickup(TRUE);
        } else {
            lclBl.setPooPickup(FALSE);
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
        return deliverCargoTo;
    }

    public void setDeliverCargoTo(String deliverCargoTo) {
        this.deliverCargoTo = deliverCargoTo;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        showFullRelay = false;
        showFullRelayFd = false;
        String fileId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileId)) {
            try {
                lclBl = new LCLBlDAO().findById(Long.parseLong(fileId));
                new LCLBlDAO().getCurrentSession().evict(lclBl);
            } catch (Exception ex) {
                log.info("onOpenDocument failed on " + new Date(), ex);
            }
        }
    }

    public void setEnums() {
        lclBl.setBookingType("E");
        lclBl.setTransMode("R");
        lclBl.setOverrideDimUom("I");

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

    public String getPooCode() {
        if (lclBl.getPortOfOrigin() != null) {
            return lclBl.getPortOfOrigin().getUnLocationCode();
        } else {
            return "";
        }
    }

    public void setPooCode(String pooCode) {
        this.pooCode = pooCode;
    }

    public String getShipContactaddress() {
        if (lclBl.getShipContact() != null && lclBl.getShipContact().getId() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getShipContact().getAddress()) && null != lclBl.getShipContact().getState()
                    && null != lclBl.getShipContact().getCity() && null != lclBl.getShipContact().getZip() && null != lclBl.getShipContact().getPhone1()) {
                builder.append(lclBl.getShipContact().getAddress());
            }

            return builder.toString();
        } else {
            return "";
        }
    }

    public void setShipContactaddress(String shipContactaddress) {
        this.shipContactaddress = shipContactaddress;
    }

    public String getBillPPD() {
        return billPPD;
    }

    public void setBillPPD(String billPPD) {
        this.billPPD = billPPD;
    }

    public String geteReference() {
        return eReference;
    }

    public void seteReference(String eReference) {
        this.eReference = eReference;
    }

    public String getRoutingInstruction() {
        return routingInstruction;
    }

    public void setRoutingInstruction(String routingInstruction) {
        this.routingInstruction = routingInstruction;
    }

    public String getFmc() {
        if (lclBl.getFwdFmcNo() != null) {
            return lclBl.getFwdFmcNo();
        }
        return null;

    }

    public void setFmc(String fmc) {
        if (CommonUtils.isNotEmpty(fmc)) {
            lclBl.setFwdFmcNo(fmc);
        }

    }

    public String getNewNotify() {
        return newNotify;
    }

    public void setNewNotify(String newNotify) {
        this.newNotify = newNotify;
    }

    public String getSameasConsignee() {
        return sameasConsignee;
    }

    public void setSameasConsignee(String sameasConsignee) {
        this.sameasConsignee = sameasConsignee;
    }

    public String getNewConsignee() {
        return newConsignee;
    }

    public void setNewConsignee(String newConsignee) {
        this.newConsignee = newConsignee;
    }

    public String getNotifyContactName() {
        return notifyContactName;
    }

    public void setNotifyContactName(String notifyContactName) {
        this.notifyContactName = notifyContactName;
    }

    public String getConsContactName() {
        return consContactName;
    }

    public void setConsContactName(String consContactName) {
        this.consContactName = consContactName;
    }

    public String getNewShipper() {
        return newShipper;
    }

    public void setNewShipper(String newShipper) {
        this.newShipper = newShipper;
    }

    public String getEdiShipperCheck() {
        return ediShipperCheck;
    }

    public void setEdiShipperCheck(String ediShipperCheck) {
        this.ediShipperCheck = ediShipperCheck;
    }

    public String getEdiConsigneeCheck() {
        return ediConsigneeCheck;
    }

    public void setEdiConsigneeCheck(String ediConsigneeCheck) {
        this.ediConsigneeCheck = ediConsigneeCheck;
    }

    public String getEdiNotifyCheck() {
        return ediNotifyCheck;
    }

    public void setEdiNotifyCheck(String ediNotifyCheck) {
        this.ediNotifyCheck = ediNotifyCheck;
    }

    public String getShipContactName() {
        return shipContactName;
    }

    public void setShipContactName(String shipContactName) {
        this.shipContactName = shipContactName;
    }

    public String getEdiForwarderCheck() {
        return ediForwarderCheck;
    }

    public void setEdiForwarderCheck(String ediForwarderCheck) {
        this.ediForwarderCheck = ediForwarderCheck;
    }

    public String getConsignee_contactName() {
        return consignee_contactName;
    }

    public void setConsignee_contactName(String consignee_contactName) {
        this.consignee_contactName = consignee_contactName;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getBlOwner() {
        return blOwner;
    }

    public void setBlOwner(String blOwner) {
        this.blOwner = blOwner;
    }

    public String getClientConsigneeCompany() {
        return clientConsigneeCompany;
    }

    public void setClientConsigneeCompany(String clientConsigneeCompany) {
        this.clientConsigneeCompany = clientConsigneeCompany;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
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

    public String getSealNumber() {
        return sealNumber;
    }

    public void setSealNumber(String sealNumber) {
        this.sealNumber = sealNumber;
    }

    public String getFreightPickupAccountNo() {
        return freightPickupAccountNo;
    }

    public void setFreightPickupAccountNo(String freightPickupAccountNo) {
        this.freightPickupAccountNo = freightPickupAccountNo;
    }

    public List getTermsTypeList() throws Exception {
        List termsList = new LCLPortConfigurationDAO().getTermsType(lclBl.getPortOfDestination().getId());
        return termsList;
    }

    public List getTermsType2List() throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Terms/Type 2");
        return new GenericCodeDAO().getCodebyCodeTypeId(codeTypeId, "Y");
    }

    public String getThirdPNameFlag() {
        return thirdPNameFlag;
    }

    public void setThirdPNameFlag(String thirdPNameFlag) {
        this.thirdPNameFlag = thirdPNameFlag;
    }

    public Integer getBlOwnerId() {
        return blOwnerId;
    }

    public void setBlOwnerId(Integer blOwnerId) {
        this.blOwnerId = blOwnerId;
    }

    public String getBlOwnerName() {
        return blOwnerName;
    }

    public void setBlOwnerName(String blOwnerName) {
        this.blOwnerName = blOwnerName;
    }

    public boolean isBlUnitCob() {
        return blUnitCob;
    }

    public void setBlUnitCob(boolean blUnitCob) {
        this.blUnitCob = blUnitCob;
    }

    public String getBlNumber() throws Exception {
        if (CommonUtils.isEmpty(this.blNumber)) {
            this.blNumber = new LCLBlDAO().getExportBlNumbering(this.fileNumberId.toString());
        }
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getHighVolumedis() {
        return highVolumedis;
    }

    public void setHighVolumedis(String highVolumedis) {
        this.highVolumedis = highVolumedis;
    }

    public String getFilterByChanges() {
        return filterByChanges;
    }

    public void setFilterByChanges(String filterByChanges) {
        this.filterByChanges = filterByChanges;
    }

    public boolean isDocsBl() {
        return docsBl;
    }

    public void setDocsBl(boolean docsBl) {
        this.docsBl = docsBl;
    }

    public boolean isDocsAes() {
        return docsAes;
    }

    public void setDocsAes(boolean docsAes) {
        this.docsAes = docsAes;
    }

    public boolean isDocsCaricom() {
        return docsCaricom;
    }

    public void setDocsCaricom(boolean docsCaricom) {
        this.docsCaricom = docsCaricom;
    }

    public Integer getBookingFileNumberId() {
        return bookingFileNumberId;
    }

    public void setBookingFileNumberId(Integer bookingFileNumberId) {
        this.bookingFileNumberId = bookingFileNumberId;
    }

    public String getVoyageClosedUser() {
        return voyageClosedUser;
    }

    public void setVoyageClosedUser(String voyageClosedUser) {
        this.voyageClosedUser = voyageClosedUser;
    }

    public String getHblPier() {
        return hblPier;
    }

    public void setHblPier(String hblPier) {
        this.hblPier = hblPier;
    }

    public String getHblPierText() {
        return hblPierText;
    }

    public void setHblPierText(String hblPierText) {
        this.hblPierText = hblPierText;
    }

    public String getHblPol() {
        return hblPol;
    }

    public void setHblPol(String hblPol) {
        this.hblPol = hblPol;
    }

    public String getHblPolText() {
        return hblPolText;
    }

    public void setHblPolText(String hblPolText) {
        this.hblPolText = hblPolText;
    }

    public boolean isPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(boolean printFlag) {
        this.printFlag = printFlag;
    }

    public String getDeliveryOverride() {
        return deliveryOverride;
    }

    public void setDeliveryOverride(String deliveryOverride) {
        this.deliveryOverride = deliveryOverride;
    }

    public String getDeliveryText() {
        return deliveryText;
    }

    public void setDeliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
    }

    public String getLadenSailDate() {
        return ladenSailDate;
    }

    public void setLadenSailDate(String ladenSailDate) {
        this.ladenSailDate = ladenSailDate;
    }

    public String getConsolidatedBlFileNo() throws Exception {
        StringBuilder consolidatedBlFileNo = new StringBuilder();
        if (null != this.bookingFileNumberId) {
            String filenumber = new LclFileNumberDAO().getFileNumberByFileId(this.bookingFileNumberId.toString());
            LclFileNumber file = new LclFileNumberDAO().getLclFileNumber(filenumber);
            consolidatedBlFileNo.append(file.getLclBooking().getPortOfOrigin().getUnLocationCode().toString().substring(2, 5)).append("-").append(filenumber);
            return consolidatedBlFileNo.toString();
        } else {
            return "";
        }
    }

    public void setConsolidatedBlFileNo(String consolidatedBlFileNo) {
        this.consolidatedBlFileNo = consolidatedBlFileNo;
    }

    public String getPrintTermsType() {
        return printTermsType;
    }

    public void setPrintTermsType(String printTermsType) {
        this.printTermsType = printTermsType;
    }

    public String getCorrectedBl() {
        return correctedBl;
    }

    public void setCorrectedBl(String correctedBl) {
        this.correctedBl = correctedBl;
    }

    public String getEditRateType() {
        return editRateType;
    }

    public void setEditRateType(String editRateType) {
        this.editRateType = editRateType;
    } 

    public String getDeliveryMetro() {
        return deliveryMetro;
    }

    public void setDeliveryMetro(String deliveryMetro) {
        this.deliveryMetro = deliveryMetro;
    }

    public String getDeliveryMetroField() {
        return deliveryMetroField;
    }

    public void setDeliveryMetroField(String deliveryMetroField) {
        this.deliveryMetroField = deliveryMetroField;
    }
    
    public String getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(String invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getCifValue() {
         if (lclBl.getCifValue() != null) {
            return "" + lclBl.getCifValue();
        }
        return "";
    }

    public void setCifValue(String cifValue) {
         if (CommonUtils.isNotEmpty(cifValue)) {
            lclBl.setCifValue(new BigDecimal(cifValue));
        } else {
            lclBl.setCifValue(null);
        }
    }

    public String getPrintInsurance() {
        return printInsurance;
    }

    public void setPrintInsurance(String printInsurance) {
        this.printInsurance = printInsurance;
    }
    
}
