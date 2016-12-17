package com.gp.cong.logisoft.domain;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.tradingpartner.GeneralInformationBC;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import java.io.Serializable;
import java.util.Date;

public class GeneralInformation implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String accountNo;
    private String idType;
    private String insuranceComment;
    private String insure;
    private String commodityNo;
    private String commodityDesc;
    private String impCommNo;
    private String impCommDesc;
    private String salesCode;
    private String salesCodeName;
    private String ad1Phones;
    private String ad1Faxs;
    private String goalAcct;
    private String inventoryCode;
    private String userName;
    private String password;
    private String poa;
    private Integer maxDaysBetVisits;
    private String fwFmcNo;
    private String fwChbNo;
    private String idText;
    private Date ldPwdActivated;
    private String specialRemarks;
    private String routingInstruction;
    private String importTrackingScreen;
    private String activatePwdQuotes;
    private String allowLclQuotes;
    private String allowFclQuotes;
    private String reservedForFuture;
    private String faxSailingSchedule;
    private String fclMailingList;
    private String christmasCard;
    private String importsCfs;
    private String pbaSurchrge;
    private GenericCode genericCode;
    private GenericCode fclCommodity;
    private GenericCode impCommodity;
    private GenericCode retailCommodity;
    private String einMaster;
    private String commodityMaster;
    private String importMaster;
    private GenericCode salescode;
    private String accounttype;
    private Customer customer;
    private String dunsNo;
    private String active;
    private String subType;
    //new fields
    private String shippingCode;
    private String exportAgent;
    private String importAgent;
    private String nvoOtiLicenseNo;
    private String allwaysBillCoload;
    private String cfclPortCode;
    private String mergeNoteInfo;
    private String ertRefNo;
    private String fclWebquoteUseCommodity;
    private String lclRateSheet;
    private String hotCodes;
    private String hotCodes1;
    private String knownShipIdAir;
    private String updateBy;
    private String CFCL;
    private String importWebDapDdp;
    private String importQuoteColoadRetail;
    private String consUserName;
    private String consPassword;
    private boolean consAllowLclWebQuotes;
    private boolean consAllowFclWebQuotes;
    private boolean consImportTrackingScreen;
    private boolean consActivatePwdQuotes;
    private Date consLastPwdActivatedDate;
    private String consFclWebQuoteUseCommodity;
    private String consLclRateSheet;
    private String consImportWebDapDdp;
    private String consLclImportQuoting;
    private GenericCode consColoadCommodity;
    private GenericCode consRetailCommodity;
    private GenericCode consFclCommodity;
    private String shipffCustControlLogin;
    private String consCustControlLogin;
    private String consImportFreightRelease;
    private String shipffImportFreightRelease;
    private GenericCode consSalesCode;
    private String shipffSalesAgencyBrokerageAgreement;
    private String shipffReceiveLclExports315Status;
    private String shipffInttraAccountNumber;
    private String shipffSendLclDocsToWebsite;
    private String shipffAllowCFCLWebBooking;
    
    private String consSalesAgencyBrokerageAgreement;
    private String consReceiveLclExports315Status;
    private String consInttraAccountNumber;
    private String consSendLclDocsToWebsite;
    private String consAllowCFCLWebBooking;
    private String applyCustomerCommodityRates;

    public GenericCode getRetailCommodity() {
        return retailCommodity;
    }

    public void setRetailCommodity(GenericCode retailCommodity) {
        this.retailCommodity = retailCommodity;
    }

    public String getKnownShipIdAir() {
        return knownShipIdAir;
    }

    public void setKnownShipIdAir(String knownShipIdAir) {
        this.knownShipIdAir = knownShipIdAir;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public String getLclRateSheet() {
        return lclRateSheet;
    }

    public void setLclRateSheet(String lclRateSheet) {
        this.lclRateSheet = lclRateSheet;
    }

    public String getFclWebquoteUseCommodity() {
        return fclWebquoteUseCommodity;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setFclWebquoteUseCommodity(String fclWebquoteUseCommodity) {
        this.fclWebquoteUseCommodity = fclWebquoteUseCommodity;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    // Default Constructor
    public GeneralInformation() {
    }

    // By Pradeep.
    public GeneralInformation(TradingPartnerForm tradingPartnerForm) throws Exception {
        knownShipIdAir = tradingPartnerForm.getKnownShipIdAir();
        accountNo = tradingPartnerForm.getAccountNo();
        idType = tradingPartnerForm.getIdType();
        insuranceComment = tradingPartnerForm.getInsuranceComment();
        commodityNo = tradingPartnerForm.getCommodity();
        commodityDesc = tradingPartnerForm.getCommDesc();
        impCommNo = tradingPartnerForm.getImpCommodity();
        impCommDesc = tradingPartnerForm.getImpCommodityDesc();
        salesCode = tradingPartnerForm.getSalesCode();
        salesCodeName = tradingPartnerForm.getSalesCodeName();
        ad1Phones = tradingPartnerForm.getPhone1();
        ad1Faxs = tradingPartnerForm.getFax1();
        goalAcct = tradingPartnerForm.getGoalAcct();
        fclWebquoteUseCommodity = tradingPartnerForm.getFclWebquoteUseCommodity();
        inventoryCode = tradingPartnerForm.getInventoryCode();
        userName = tradingPartnerForm.getUserName();
        password = tradingPartnerForm.getPassword();
        poa = tradingPartnerForm.getPoa();
        if (tradingPartnerForm.getMaxDay() != null && !tradingPartnerForm.getMaxDay().trim().equals("")) {
            maxDaysBetVisits = Integer.parseInt(tradingPartnerForm.getMaxDay());
        }
        fwFmcNo = tradingPartnerForm.getFrieghtFmc();
        fwChbNo = tradingPartnerForm.getFrieghtChb();
        idText = tradingPartnerForm.getIdtext();
        if (tradingPartnerForm.getLastDate() != null && !tradingPartnerForm.getLastDate().trim().equals("")) {
            ldPwdActivated = DateUtils.parseToDate(tradingPartnerForm.getLastDate());
        }
        specialRemarks = tradingPartnerForm.getSpclRemark();
        routingInstruction = tradingPartnerForm.getDefaultRoute();
        importTrackingScreen = (null != tradingPartnerForm.getImportTrackingScreen()
                ? (tradingPartnerForm.getImportTrackingScreen().equalsIgnoreCase("on") ? "Y" : "N") : null);
        activatePwdQuotes = tradingPartnerForm.getActivatePwdQuotes();
        allowLclQuotes = tradingPartnerForm.getAllowLclQuotes();
        allowFclQuotes = tradingPartnerForm.getAllowFclQuotes();
        reservedForFuture = tradingPartnerForm.getReservedForFuture();
        faxSailingSchedule = tradingPartnerForm.getFaxSailingSchedule();
        fclMailingList = tradingPartnerForm.getFclMailingList();
        christmasCard = tradingPartnerForm.getChristmasCard();
        importsCfs = tradingPartnerForm.getImportsCfs();
        pbaSurchrge = tradingPartnerForm.getPbaSurchrge();
        subType = tradingPartnerForm.getSubType();
        exportAgent = tradingPartnerForm.getExportAgent();
        importAgent = tradingPartnerForm.getImportAgent();
        nvoOtiLicenseNo = tradingPartnerForm.getNvoOtiLicenseNo();
        allwaysBillCoload = tradingPartnerForm.getAllwaysBillCoload();
        cfclPortCode = tradingPartnerForm.getCfclPortCode();
        mergeNoteInfo = tradingPartnerForm.getMergeNoteInfo();
        ertRefNo = tradingPartnerForm.getErtRefNo();
        //setting for those new fields added
        shippingCode = tradingPartnerForm.getShippingCode();
        hotCodes = tradingPartnerForm.getHotCodes();
        hotCodes1 = tradingPartnerForm.getHotCodes1();
        insuranceComment = tradingPartnerForm.getInsuranceComment();
        lclRateSheet = tradingPartnerForm.getLclRateSheet();
        activatePwdQuotes = (null != tradingPartnerForm.getActivatePwdQuotes()
                ? (tradingPartnerForm.getActivatePwdQuotes().equalsIgnoreCase("on") ? "Y" : "N") : null);

        String accType = "";
        // setting account type
        if (tradingPartnerForm.getAccountType1() != null && tradingPartnerForm.getAccountType1().equals("on")) {
            accType = "S,";
        }
        if (tradingPartnerForm.getAccountType2() != null && tradingPartnerForm.getAccountType2().equals("on")) {
            accType = accType + "F,";
        }
        if (tradingPartnerForm.getAccountType3() != null && tradingPartnerForm.getAccountType3().equals("on")) {
            accType = accType + "N,";
        }
        if (tradingPartnerForm.getAccountType4() != null && tradingPartnerForm.getAccountType4().equals("on")) {
            accType = accType + "C,";
        }
        if (tradingPartnerForm.getAccountType5() != null && tradingPartnerForm.getAccountType5().equals("on")) {
            accType = accType + "SS,";
        }
        if (tradingPartnerForm.getAccountType6() != null && tradingPartnerForm.getAccountType6().equals("on")) {
            accType = accType + "T,";
        }
        if (tradingPartnerForm.getAccountType7() != null && tradingPartnerForm.getAccountType7().equals("on")) {
            accType = accType + "A,";
        }
        if (tradingPartnerForm.getAccountType8() != null && tradingPartnerForm.getAccountType8().equals("on")) {
            accType = accType + "I,";
        }
        if (tradingPartnerForm.getAccountType9() != null && tradingPartnerForm.getAccountType9().equals("on")) {
            accType = accType + "E,";
        }
        if (tradingPartnerForm.getAccountType10() != null && tradingPartnerForm.getAccountType10().equals("on")) {
            accType = accType + "V,";
        } else {
            subType = null;
        }
        if (tradingPartnerForm.getAccountType11() != null && tradingPartnerForm.getAccountType11().equals("on")) {
            accType = accType + "O,";
        }
        if (tradingPartnerForm.getAccountType12() != null && tradingPartnerForm.getAccountType12().equals("on")) {
            accType = accType + "L,";
        }
        if (tradingPartnerForm.getAccountType13() != null && tradingPartnerForm.getAccountType13().equals("on")) {
            accType = accType + "Z,";
        }
        if (accType.length() > 0) {
            accounttype = accType.substring(0, accType.length() - 1);
        }

        if (tradingPartnerForm.getInsure() != null && tradingPartnerForm.getInsure().equals("on")) {
            insure = "Y";
        } else {
            insure = "N";
        }
        if (tradingPartnerForm.getCFCL() != null && tradingPartnerForm.getCFCL().equals("on")) {
            CFCL = "Y";
        } else {
            CFCL = "N";
        }
        if (tradingPartnerForm.getGoalAcct() != null && tradingPartnerForm.getGoalAcct().equals("on")) {
            goalAcct = "Y";
        } else {
            goalAcct = "N";
        }
        if (tradingPartnerForm.getFclWebquoteUseCommodity() != null && tradingPartnerForm.getFclWebquoteUseCommodity().equals("on")) {
            fclWebquoteUseCommodity = "Y";
        } else {
            fclWebquoteUseCommodity = "N";
        }
        if (tradingPartnerForm.getPoa() != null && tradingPartnerForm.getPoa().equals("on")) {
            poa = "Y";
        } else {
            poa = "N";
        }
        if (tradingPartnerForm.getEinmaster() != null && tradingPartnerForm.getEinmaster().equals("on")) {
            einMaster = "Y";
        } else {
            einMaster = "N";
        }
        if (tradingPartnerForm.getCommoditymaster() != null && tradingPartnerForm.getCommoditymaster().equals("on")) {
            commodityMaster = "Y";
        } else {
            commodityMaster = "N";
        }
        if (tradingPartnerForm.getImportmaster() != null && tradingPartnerForm.getImportmaster().equals("on")) {
            importMaster = "Y";
        } else {
            importMaster = "N";
        }
        if (tradingPartnerForm.getAllowLclQuotes() != null && tradingPartnerForm.getAllowLclQuotes().equalsIgnoreCase("on")) {
            allowLclQuotes = "Y";
        } else {
            allowLclQuotes = "N";
        }
        if (tradingPartnerForm.getAllowFclQuotes() != null && tradingPartnerForm.getAllowFclQuotes().equalsIgnoreCase("on")) {
            allowFclQuotes = "Y";
        } else {
            allowFclQuotes = "N";
        }
        if (tradingPartnerForm.getFaxSailingSchedule() != null && tradingPartnerForm.getFaxSailingSchedule().equalsIgnoreCase(
                "on")) {
            faxSailingSchedule = "Y";
        } else {
            faxSailingSchedule = "N";
        }
        if (tradingPartnerForm.getFclMailingList() != null && tradingPartnerForm.getFclMailingList().equalsIgnoreCase("on")) {
            fclMailingList = "Y";
        } else {
            fclMailingList = "N";
        }
        if (tradingPartnerForm.getChristmasCard() != null && tradingPartnerForm.getChristmasCard().equalsIgnoreCase("on")) {
            christmasCard = "Y";
        } else {
            christmasCard = "N";
        }
        if (tradingPartnerForm.getPbaSurchrge() != null && tradingPartnerForm.getPbaSurchrge().equalsIgnoreCase("on")) {
            pbaSurchrge = "Y";
        } else {
            pbaSurchrge = "N";
        }
        if (tradingPartnerForm.getImportTrackingScreen() != null && tradingPartnerForm.getImportTrackingScreen().equalsIgnoreCase("on")) {
            importTrackingScreen = "Y";
        } else {
            importTrackingScreen = "N";
        }
        if (tradingPartnerForm.getActivatePwdQuotes() != null && tradingPartnerForm.getActivatePwdQuotes().equalsIgnoreCase(
                "on")) {
            activatePwdQuotes = "Y";
        } else {
            activatePwdQuotes = "N";
        }

        if (tradingPartnerForm.getActive() != null && tradingPartnerForm.getActive().equalsIgnoreCase("on")) {
            active = "Y";
        } else {
            active = "N";
        }
        // private GenericCode impCommodity;
        einMaster = tradingPartnerForm.getEinmaster();
        commodityMaster = tradingPartnerForm.getCommoditymaster();
        importMaster = tradingPartnerForm.getImportmaster();

        // private Customer customer;
        dunsNo = tradingPartnerForm.getDunsNo();
        ad1Faxs = tradingPartnerForm.getFax1();
        idText = tradingPartnerForm.getIdtext();
        routingInstruction = tradingPartnerForm.getDefaultRoute();
        insuranceComment = tradingPartnerForm.getInsuranceComment();
        fwFmcNo = tradingPartnerForm.getFrieghtFmc();
        fwChbNo = tradingPartnerForm.getFrieghtChb();
        inventoryCode = tradingPartnerForm.getInventoryCode();
        importWebDapDdp = tradingPartnerForm.getImportWebDapDdp();
        importQuoteColoadRetail = tradingPartnerForm.getImportQuoteColoadRetail();
    }

    public GeneralInformation SetGeneralInformation(GeneralInformation generalInfo, TradingPartnerForm tradingPartnerForm) throws Exception {
        if (null == generalInfo) {
            generalInfo = new GeneralInformation();
            generalInfo.accountNo = tradingPartnerForm.getAccountNo();
        }
        generalInfo.knownShipIdAir = tradingPartnerForm.getKnownShipIdAir();
        generalInfo.idType = tradingPartnerForm.getIdType();
        generalInfo.insuranceComment = tradingPartnerForm.getInsuranceComment();
        generalInfo.commodityNo = tradingPartnerForm.getCommodity();
        generalInfo.commodityDesc = tradingPartnerForm.getCommDesc();
        generalInfo.impCommNo = tradingPartnerForm.getImpCommodity();
        generalInfo.impCommDesc = tradingPartnerForm.getImpCommodityDesc();
        generalInfo.salesCode = tradingPartnerForm.getSalesCode();
        generalInfo.salesCodeName = tradingPartnerForm.getSalesCodeName();
        generalInfo.ad1Phones = tradingPartnerForm.getPhone1();
        generalInfo.ad1Faxs = tradingPartnerForm.getFax1();
        generalInfo.goalAcct = tradingPartnerForm.getGoalAcct();
        generalInfo.fclWebquoteUseCommodity = tradingPartnerForm.getFclWebquoteUseCommodity();
        generalInfo.inventoryCode = tradingPartnerForm.getInventoryCode();
        generalInfo.userName = tradingPartnerForm.getUserName();
        generalInfo.password = tradingPartnerForm.getPassword();
        generalInfo.poa = tradingPartnerForm.getPoa();
        if (tradingPartnerForm.getMaxDay() != null && !tradingPartnerForm.getMaxDay().trim().equals("")) {
            generalInfo.maxDaysBetVisits = Integer.parseInt(tradingPartnerForm.getMaxDay());
        }
        generalInfo.fwFmcNo = tradingPartnerForm.getFrieghtFmc();
        generalInfo.fwChbNo = tradingPartnerForm.getFrieghtChb();
        generalInfo.idText = tradingPartnerForm.getIdtext();
        if (tradingPartnerForm.getLastDate() != null && !tradingPartnerForm.getLastDate().trim().equals("")) {
            generalInfo.ldPwdActivated = DateUtils.parseDate(tradingPartnerForm.getLastDate(), "MM/dd/yyyy");
        }
        generalInfo.specialRemarks = tradingPartnerForm.getSpclRemark();
        generalInfo.routingInstruction = tradingPartnerForm.getDefaultRoute();
        generalInfo.importTrackingScreen = (null != tradingPartnerForm.getImportTrackingScreen()
                ? (tradingPartnerForm.getImportTrackingScreen().equalsIgnoreCase("on") ? "Y" : "N") : null);
        generalInfo.activatePwdQuotes = tradingPartnerForm.getActivatePwdQuotes();
        generalInfo.allowLclQuotes = tradingPartnerForm.getAllowLclQuotes();
        generalInfo.allowFclQuotes = tradingPartnerForm.getAllowFclQuotes();
        generalInfo.reservedForFuture = tradingPartnerForm.getReservedForFuture();
        generalInfo.faxSailingSchedule = tradingPartnerForm.getFaxSailingSchedule();
        generalInfo.fclMailingList = tradingPartnerForm.getFclMailingList();
        generalInfo.christmasCard = tradingPartnerForm.getChristmasCard();
        generalInfo.importsCfs = tradingPartnerForm.getImportsCfs();
        generalInfo.pbaSurchrge = tradingPartnerForm.getPbaSurchrge();
        generalInfo.subType = tradingPartnerForm.getSubType();
        generalInfo.exportAgent = tradingPartnerForm.getExportAgent();
        generalInfo.importAgent = tradingPartnerForm.getImportAgent();
        generalInfo.nvoOtiLicenseNo = tradingPartnerForm.getNvoOtiLicenseNo();
        generalInfo.allwaysBillCoload = tradingPartnerForm.getAllwaysBillCoload();
        generalInfo.cfclPortCode = tradingPartnerForm.getCfclPortCode();
        generalInfo.mergeNoteInfo = tradingPartnerForm.getMergeNoteInfo();
        generalInfo.ertRefNo = tradingPartnerForm.getErtRefNo();
        //setting for those new fields added
        generalInfo.shippingCode = tradingPartnerForm.getShippingCode();
        generalInfo.hotCodes = tradingPartnerForm.getHotCodes();
        generalInfo.hotCodes1 = tradingPartnerForm.getHotCodes1();
        generalInfo.insuranceComment = tradingPartnerForm.getInsuranceComment();
        generalInfo.lclRateSheet = tradingPartnerForm.getLclRateSheet();
        generalInfo.activatePwdQuotes = (null != tradingPartnerForm.getActivatePwdQuotes()
                ? (tradingPartnerForm.getActivatePwdQuotes().equalsIgnoreCase("on") ? "Y" : "N") : null);

        GeneralInformationBC generalInformationBC = new GeneralInformationBC();
        String accType = "";
        // setting account type
        if (tradingPartnerForm.getAccountType1() != null && tradingPartnerForm.getAccountType1().equals("on")) {
            accType = "S,";
        }
        if (tradingPartnerForm.getAccountType2() != null && tradingPartnerForm.getAccountType2().equals("on")) {
            accType = accType + "F,";
        }
        if (tradingPartnerForm.getAccountType3() != null && tradingPartnerForm.getAccountType3().equals("on")) {
            accType = accType + "N,";
        }
        if (tradingPartnerForm.getAccountType4() != null && tradingPartnerForm.getAccountType4().equals("on")) {
            accType = accType + "C,";
        }
        if (tradingPartnerForm.getAccountType5() != null && tradingPartnerForm.getAccountType5().equals("on")) {
            accType = accType + "SS,";
        }
        if (tradingPartnerForm.getAccountType6() != null && tradingPartnerForm.getAccountType6().equals("on")) {
            accType = accType + "T,";
        }
        if (tradingPartnerForm.getAccountType7() != null && tradingPartnerForm.getAccountType7().equals("on")) {
            accType = accType + "A,";
        }
        if (tradingPartnerForm.getAccountType8() != null && tradingPartnerForm.getAccountType8().equals("on")) {
            accType = accType + "I,";
        }
        if (tradingPartnerForm.getAccountType9() != null && tradingPartnerForm.getAccountType9().equals("on")) {
            accType = accType + "E,";
        }
        if (tradingPartnerForm.getAccountType10() != null && tradingPartnerForm.getAccountType10().equals("on")) {
            accType = accType + "V,";
        } else {
            generalInfo.subType = null;
        }
        if (tradingPartnerForm.getAccountType11() != null && tradingPartnerForm.getAccountType11().equals("on")) {
            accType = accType + "O,";
        }
        if (tradingPartnerForm.getAccountType12() != null && tradingPartnerForm.getAccountType12().equals("on")) {
            accType = accType + "L,";
        }
        if (tradingPartnerForm.getAccountType13() != null && tradingPartnerForm.getAccountType13().equals("on")) {
            accType = accType + "Z,";
        }
        if (accType.length() > 0) {
            generalInfo.accounttype = accType.substring(0, accType.length() - 1);
        }

        if (tradingPartnerForm.getInsure() != null && tradingPartnerForm.getInsure().equals("on")) {
            generalInfo.insure = "Y";
        } else {
            generalInfo.insure = "N";
        }
        if (tradingPartnerForm.getCFCL() != null && tradingPartnerForm.getCFCL().equals("on")) {
            CFCL = "Y";
        } else {
            CFCL = "N";
        }
        if (tradingPartnerForm.getGoalAcct() != null && tradingPartnerForm.getGoalAcct().equals("on")) {
            generalInfo.goalAcct = "Y";
        } else {
            generalInfo.goalAcct = "N";
        }
        generalInfo.fclWebquoteUseCommodity = tradingPartnerForm.getFclWebquoteUseCommodity();
        if (tradingPartnerForm.getPoa() != null && tradingPartnerForm.getPoa().equals("on")) {
            generalInfo.poa = "Y";
        } else {
            generalInfo.poa = "N";
        }
        if (tradingPartnerForm.getEinmaster() != null && tradingPartnerForm.getEinmaster().equals("on")) {
            generalInfo.einMaster = "Y";
        } else {
            generalInfo.einMaster = "N";
        }
        if (tradingPartnerForm.getCommoditymaster() != null && tradingPartnerForm.getCommoditymaster().equals("on")) {
            generalInfo.commodityMaster = "Y";
        } else {
            generalInfo.commodityMaster = "N";
        }
        if (tradingPartnerForm.getImportmaster() != null && tradingPartnerForm.getImportmaster().equals("on")) {
            generalInfo.importMaster = "Y";
        } else {
            generalInfo.importMaster = "N";
        }
        if (tradingPartnerForm.getAllowLclQuotes() != null && tradingPartnerForm.getAllowLclQuotes().equalsIgnoreCase("on")) {
            generalInfo.allowLclQuotes = "Y";
        } else {
            generalInfo.allowLclQuotes = "N";
        }
        if (tradingPartnerForm.getAllowFclQuotes() != null && tradingPartnerForm.getAllowFclQuotes().equalsIgnoreCase("on")) {
            generalInfo.allowFclQuotes = "Y";
        } else {
            generalInfo.allowFclQuotes = "N";
        }
        if (tradingPartnerForm.getFaxSailingSchedule() != null && tradingPartnerForm.getFaxSailingSchedule().equalsIgnoreCase(
                "on")) {
            generalInfo.faxSailingSchedule = "Y";
        } else {
            generalInfo.faxSailingSchedule = "N";
        }
        if (tradingPartnerForm.getFclMailingList() != null && tradingPartnerForm.getFclMailingList().equalsIgnoreCase("on")) {
            generalInfo.fclMailingList = "Y";
        } else {
            generalInfo.fclMailingList = "N";
        }
        if (tradingPartnerForm.getChristmasCard() != null && tradingPartnerForm.getChristmasCard().equalsIgnoreCase("on")) {
            generalInfo.christmasCard = "Y";
        } else {
            generalInfo.christmasCard = "N";
        }
        if (tradingPartnerForm.getPbaSurchrge() != null && tradingPartnerForm.getPbaSurchrge().equalsIgnoreCase("on")) {
            generalInfo.pbaSurchrge = "Y";
        } else {
            generalInfo.pbaSurchrge = "N";
        }
        if (tradingPartnerForm.getImportTrackingScreen() != null && tradingPartnerForm.getImportTrackingScreen().equalsIgnoreCase("on")) {
            generalInfo.importTrackingScreen = "Y";
        } else {
            generalInfo.importTrackingScreen = "N";
        }
        if (tradingPartnerForm.getActivatePwdQuotes() != null && tradingPartnerForm.getActivatePwdQuotes().equalsIgnoreCase(
                "on")) {
            generalInfo.activatePwdQuotes = "Y";
        } else {
            generalInfo.activatePwdQuotes = "N";
        }

        if (tradingPartnerForm.getActive() != null && tradingPartnerForm.getActive().equalsIgnoreCase("on")) {
            generalInfo.active = "Y";
        } else {
            generalInfo.active = "N";
        }
        // private GenericCode impCommodity;
        generalInfo.einMaster = tradingPartnerForm.getEinmaster();
        generalInfo.commodityMaster = tradingPartnerForm.getCommoditymaster();
        generalInfo.importMaster = tradingPartnerForm.getImportmaster();

        // private Customer customer;
        generalInfo.dunsNo = tradingPartnerForm.getDunsNo();
        generalInfo.ad1Faxs = tradingPartnerForm.getFax1();
        generalInfo.idText = tradingPartnerForm.getIdtext();
        generalInfo.routingInstruction = tradingPartnerForm.getDefaultRoute();
        generalInfo.insuranceComment = tradingPartnerForm.getInsuranceComment();
        generalInfo.fwFmcNo = tradingPartnerForm.getFrieghtFmc();
        generalInfo.fwChbNo = tradingPartnerForm.getFrieghtChb();
        generalInfo.inventoryCode = tradingPartnerForm.getInventoryCode();
        generalInfo.importWebDapDdp = tradingPartnerForm.getImportWebDapDdp();
        generalInfo.importQuoteColoadRetail = tradingPartnerForm.getImportQuoteColoadRetail();
        return generalInfo;
    }

    public TradingPartnerForm loadGeneralInformation(
            GeneralInformation generalInformation, TradingPartnerForm tradingPartnerForm) throws Exception {
        tradingPartnerForm.setIdType(generalInformation.getIdType());
        if (generalInformation.getGenericCode() != null) {
            tradingPartnerForm.setCommodity(generalInformation.getGenericCode().getCode());
            tradingPartnerForm.setCommDesc(generalInformation.getGenericCode().getCodedesc());
        }
        if (null != generalInformation.getImpCommodity()) {
            tradingPartnerForm.setImpCommodity(generalInformation.getImpCommodity().getCode());
            tradingPartnerForm.setImpCommodityDesc(generalInformation.getImpCommodity().getCodedesc());
        }
        if (null != generalInformation.getRetailCommodity()) {
            tradingPartnerForm.setRetailCommodity(generalInformation.getRetailCommodity().getCode());
            tradingPartnerForm.setRetailCommodityDesc(generalInformation.getRetailCommodity().getCodedesc());
        }
        if (null != generalInformation.getFclCommodity()) {
            tradingPartnerForm.setFclCommodity(generalInformation.getFclCommodity().getCode());
            tradingPartnerForm.setFclCommodityDesc(generalInformation.getFclCommodity().getCodedesc());
        }
        if (null != generalInformation.getConsColoadCommodity()) {
            tradingPartnerForm.setConsColoadCommodity(generalInformation.getConsColoadCommodity().getCode());
            tradingPartnerForm.setConsColoadCommodityDesc(generalInformation.getConsColoadCommodity().getCodedesc());
        }
        if (null != generalInformation.getConsRetailCommodity()) {
            tradingPartnerForm.setConsRetailCommodity(generalInformation.getConsRetailCommodity().getCode());
            tradingPartnerForm.setConsRetailCommodityDesc(generalInformation.getConsRetailCommodity().getCodedesc());
        }
        if (null != generalInformation.getConsFclCommodity()) {
            tradingPartnerForm.setConsFclCommodity(generalInformation.getConsFclCommodity().getCode());
            tradingPartnerForm.setConsFclCommodityDesc(generalInformation.getConsFclCommodity().getCodedesc());
        }
        if (null != generalInformation.getConsSalesCode()) {
            tradingPartnerForm.setConsSalesCode(generalInformation.getConsSalesCode().getCode());
            tradingPartnerForm.setConsSalesCodeDesc(generalInformation.getConsSalesCode().getCodedesc());
        }
        tradingPartnerForm.setIndex(generalInformation.getId().toString());
        if (generalInformation.getSalescode() != null) {
            tradingPartnerForm.setSalesCode(generalInformation.getSalescode().getCode());
            tradingPartnerForm.setSalesCodeName(generalInformation.getSalescode().getCodedesc());
        }
        tradingPartnerForm.setHotCodes(generalInformation.getHotCodes());
        tradingPartnerForm.setHotCodes1(generalInformation.getHotCodes1());
        tradingPartnerForm.setLclRateSheet(generalInformation.getLclRateSheet());
        tradingPartnerForm.setInventoryCode(generalInformation.getInventoryCode());
        tradingPartnerForm.setUserName(generalInformation.getUserName());
        tradingPartnerForm.setPassword(generalInformation.getPassword());
        tradingPartnerForm.setPoa(generalInformation.getPoa());
        generalInformation.setIdText(generalInformation.getIdText());
        tradingPartnerForm.setDunsNo(generalInformation.getDunsNo());
        tradingPartnerForm.setShippingCode(generalInformation.getShippingCode());
        tradingPartnerForm.setPhone1(generalInformation.getAd1Phones());
        tradingPartnerForm.setFax1(generalInformation.getAd1Faxs());
        tradingPartnerForm.setSpclRemark(generalInformation.getSpecialRemarks());
        tradingPartnerForm.setIdtext(generalInformation.getIdText());
        tradingPartnerForm.setDefaultRoute(generalInformation.getRoutingInstruction());
        tradingPartnerForm.setInsuranceComment(generalInformation.getInsuranceComment());
        tradingPartnerForm.setFrieghtChb(generalInformation.getFwChbNo());
        tradingPartnerForm.setFrieghtFmc(generalInformation.getFwFmcNo());
        tradingPartnerForm.setInventoryCode(generalInformation.getInventoryCode());
        if (generalInformation.getImportsCfs() != null) {
            tradingPartnerForm.setImportsCfs(generalInformation.getImportsCfs());
        } else {
            tradingPartnerForm.setImportsCfs("U");
        }
        tradingPartnerForm.setSubType(generalInformation.getSubType());
        tradingPartnerForm.setExportAgent(generalInformation.getExportAgent());
        tradingPartnerForm.setImportAgent(generalInformation.getImportAgent());
        tradingPartnerForm.setNvoOtiLicenseNo(generalInformation.getNvoOtiLicenseNo());
        if (generalInformation.getAllwaysBillCoload() != null) {
            tradingPartnerForm.setAllwaysBillCoload(generalInformation.getAllwaysBillCoload());
        } else {
            tradingPartnerForm.setAllwaysBillCoload("N");
        }
        tradingPartnerForm.setCfclPortCode(generalInformation.getCfclPortCode());
        tradingPartnerForm.setMergeNoteInfo(generalInformation.getMergeNoteInfo());
        tradingPartnerForm.setErtRefNo(generalInformation.getErtRefNo());
        tradingPartnerForm.setKnownShipIdAir(generalInformation.getKnownShipIdAir());

        //--------------------------------------------------
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("S") > -1) {
            tradingPartnerForm.setAccountType1("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("F") > -1) {
            tradingPartnerForm.setAccountType2("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("N") > -1) {
            tradingPartnerForm.setAccountType3("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("C") > -1) {
            tradingPartnerForm.setAccountType4("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("SS") > -1) {
            tradingPartnerForm.setAccountType5("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("T") > -1) {
            tradingPartnerForm.setAccountType6("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("A") > -1) {
            tradingPartnerForm.setAccountType7("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("I") > -1) {
            tradingPartnerForm.setAccountType8("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("E") > -1) {
            tradingPartnerForm.setAccountType9("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("V") > -1) {
            tradingPartnerForm.setAccountType10("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("O") > -1) {
            tradingPartnerForm.setAccountType11("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("L") > -1) {
            tradingPartnerForm.setAccountType12("on");
        }
        if (generalInformation.getAccounttype() != null && generalInformation.getAccounttype().indexOf("Z") > -1) {
            tradingPartnerForm.setAccountType13("on");
        }
        if (generalInformation.getInsure() != null && generalInformation.getInsure().equals("Y")) {
            tradingPartnerForm.setInsure("on");
        } else {
            tradingPartnerForm.setInsure("off");
        }
        if (generalInformation.getCFCL() != null && generalInformation.getCFCL().equals("Y")) {
            tradingPartnerForm.setCFCL("on");
        } else {
            tradingPartnerForm.setCFCL("off");
        }
        if (generalInformation.getGoalAcct() != null && generalInformation.getGoalAcct().equals("Y")) {
            tradingPartnerForm.setGoalAcct("on");
        } else {
            tradingPartnerForm.setGoalAcct("off");
        }
        tradingPartnerForm.setFclWebquoteUseCommodity(generalInformation.getFclWebquoteUseCommodity());
        if (generalInformation.getPoa() != null && generalInformation.getPoa().equals("Y")) {
            tradingPartnerForm.setPoa("on");
        } else {
            tradingPartnerForm.setPoa("off");
        }
        if (generalInformation.getEinMaster() != null && generalInformation.getEinMaster().equals("Y")) {
            tradingPartnerForm.setEinmaster("on");
        } else {
            tradingPartnerForm.setEinmaster("off");
        }
        if (generalInformation.getCommodityMaster() != null && generalInformation.getCommodityMaster().equals("Y")) {
            generalInformation.setCommodityMaster("on");
        } else {
            generalInformation.setCommodityMaster("off");
        }
        if (generalInformation.getImportMaster() != null && generalInformation.getImportMaster().equals("Y")) {
            tradingPartnerForm.setImportmaster("on");
        } else {
            tradingPartnerForm.setImportmaster("off");
        }
        if (generalInformation.getAllowLclQuotes() != null && generalInformation.getAllowLclQuotes().equalsIgnoreCase("Y")) {
            tradingPartnerForm.setAllowLclQuotes("on");
        } else {
            tradingPartnerForm.setAllowLclQuotes("off");
        }
        if (generalInformation.getAllowFclQuotes() != null && generalInformation.getAllowFclQuotes().equalsIgnoreCase("Y")) {
            tradingPartnerForm.setAllowFclQuotes("on");
        } else {
            tradingPartnerForm.setAllowFclQuotes("off");
        }
        if (generalInformation.getFaxSailingSchedule() != null && generalInformation.getFaxSailingSchedule().equalsIgnoreCase(
                "Y")) {
            tradingPartnerForm.setFaxSailingSchedule("on");
        } else {
            tradingPartnerForm.setFaxSailingSchedule("off");
        }
        if (generalInformation.getFclMailingList() != null && generalInformation.getFclMailingList().equalsIgnoreCase("Y")) {
            tradingPartnerForm.setFclMailingList("on");
        } else {
            tradingPartnerForm.setFclMailingList("off");
        }
        if (generalInformation.getChristmasCard() != null && generalInformation.getChristmasCard().equalsIgnoreCase("Y")) {
            tradingPartnerForm.setChristmasCard("on");
        } else {
            tradingPartnerForm.setChristmasCard("off");
        }
        if (generalInformation.getPbaSurchrge() != null && generalInformation.getPbaSurchrge().equalsIgnoreCase("Y")) {
            tradingPartnerForm.setPbaSurchrge("on");
        } else {
            tradingPartnerForm.setPbaSurchrge("off");
        }
        if (generalInformation.getImportTrackingScreen() != null && generalInformation.getImportTrackingScreen().equalsIgnoreCase("Y")) {
            tradingPartnerForm.setImportTrackingScreen("on");
        } else {
            tradingPartnerForm.setImportTrackingScreen("off");
        }
        if (generalInformation.getActivatePwdQuotes() != null && generalInformation.getActivatePwdQuotes().equalsIgnoreCase(
                "Y")) {
            tradingPartnerForm.setActivatePwdQuotes("on");
        } else {
            tradingPartnerForm.setActivatePwdQuotes("off");
        }

        if (generalInformation.getActive() != null && generalInformation.getActive().equalsIgnoreCase("on")) {
            tradingPartnerForm.setActive("on");
        } else {
            tradingPartnerForm.setActive("off");
        }
        //--------------------------------------------------
        if (null != generalInformation.getMaxDaysBetVisits()) {
            tradingPartnerForm.setMaxDay(generalInformation.getMaxDaysBetVisits().toString());
        }
        if (null != generalInformation.getLdPwdActivated()) {
            tradingPartnerForm.setLastDate(DateUtils.formatDate(generalInformation.getLdPwdActivated(), "MM/dd/yyyy"));
        }
        if (generalInformation.getImportWebDapDdp() != null) {
            tradingPartnerForm.setImportWebDapDdp(generalInformation.getImportWebDapDdp());
        } else {
            tradingPartnerForm.setImportWebDapDdp("N");
        }
        tradingPartnerForm.setImportQuoteColoadRetail(generalInformation.getImportQuoteColoadRetail());
        tradingPartnerForm.setConsUserName(generalInformation.getConsUserName());
        tradingPartnerForm.setConsPassword(generalInformation.getConsPassword());
        tradingPartnerForm.setConsAllowLclWebQuotes(generalInformation.isConsAllowLclWebQuotes());
        tradingPartnerForm.setConsAllowFclWebQuotes(generalInformation.isConsAllowFclWebQuotes());
        tradingPartnerForm.setConsImportTrackingScreen(generalInformation.isConsImportTrackingScreen());
        tradingPartnerForm.setConsActivatePwdQuotes(generalInformation.isConsActivatePwdQuotes());
        if (null != generalInformation.getConsLastPwdActivatedDate()) {
            tradingPartnerForm.setConsLastPwdActivatedDate(DateUtils.formatDate(generalInformation.getConsLastPwdActivatedDate(), "MM/dd/yyyy"));
        }
        tradingPartnerForm.setConsFclWebQuoteUseCommodity(generalInformation.getConsFclWebQuoteUseCommodity());
        tradingPartnerForm.setConsLclRateSheet(generalInformation.getConsLclRateSheet());
        tradingPartnerForm.setConsImportWebDapDdp(generalInformation.getConsImportWebDapDdp());
        tradingPartnerForm.setConsLclImportQuoting(generalInformation.getConsLclImportQuoting());
        tradingPartnerForm.setShipffCustControlLogin(generalInformation.getShipffCustControlLogin());
        tradingPartnerForm.setConsCustControlLogin(generalInformation.getConsCustControlLogin());
        tradingPartnerForm.setConsImportFreightRelease(generalInformation.getConsImportFreightRelease());
        tradingPartnerForm.setShipffImportFreightRelease(generalInformation.getShipffImportFreightRelease());
        tradingPartnerForm.setShipffSalesAgencyBrokerageAgreement(generalInformation.getShipffSalesAgencyBrokerageAgreement());
        tradingPartnerForm.setShipffReceiveLclExports315Status(generalInformation.getShipffReceiveLclExports315Status());
        tradingPartnerForm.setShipffInttraAccountNumber(generalInformation.getShipffInttraAccountNumber());
        tradingPartnerForm.setShipffSendLclDocsToWebsite(generalInformation.getShipffSendLclDocsToWebsite());
        tradingPartnerForm.setShipffAllowCFCLWebBooking(generalInformation.getShipffAllowCFCLWebBooking());
        tradingPartnerForm.setConsSalesAgencyBrokerageAgreement(generalInformation.getConsSalesAgencyBrokerageAgreement());
        tradingPartnerForm.setConsReceiveLclExports315Status(generalInformation.getConsReceiveLclExports315Status());
        tradingPartnerForm.setConsInttraAccountNumber(generalInformation.getConsInttraAccountNumber());
        tradingPartnerForm.setConsSendLclDocsToWebsite(generalInformation.getConsSendLclDocsToWebsite());
        tradingPartnerForm.setConsAllowCFCLWebBooking(generalInformation.getConsAllowCFCLWebBooking());
        tradingPartnerForm.setApplyCustomerCommodityRates(generalInformation.getApplyCustomerCommodityRates());
        return tradingPartnerForm;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDunsNo() {
        return dunsNo;
    }

    public void setDunsNo(String dunsNo) {
        this.dunsNo = dunsNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public GenericCode getGenericCode() {
        return genericCode;
    }

    public void setGenericCode(GenericCode genericCode) {
        this.genericCode = genericCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getActivatePwdQuotes() {
        return activatePwdQuotes;
    }

    public void setActivatePwdQuotes(String activatePwdQuotes) {
        this.activatePwdQuotes = activatePwdQuotes;
    }

    public String getAd1Faxs() {
        return ad1Faxs;
    }

    public void setAd1Faxs(String ad1Faxs) {
        this.ad1Faxs = ad1Faxs;
    }

    public String getAd1Phones() {
        return ad1Phones;
    }

    public void setAd1Phones(String ad1Phones) {
        this.ad1Phones = ad1Phones;
    }

    public String getAllowFclQuotes() {
        return allowFclQuotes;
    }

    public void setAllowFclQuotes(String allowFclQuotes) {
        this.allowFclQuotes = allowFclQuotes;
    }

    public String getAllowLclQuotes() {
        return allowLclQuotes;
    }

    public void setAllowLclQuotes(String allowLclQuotes) {
        this.allowLclQuotes = allowLclQuotes;
    }

    public String getChristmasCard() {
        return christmasCard;
    }

    public void setChristmasCard(String christmasCard) {
        this.christmasCard = christmasCard;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getFaxSailingSchedule() {
        return faxSailingSchedule;
    }

    public void setFaxSailingSchedule(String faxSailingSchedule) {
        this.faxSailingSchedule = faxSailingSchedule;
    }

    public String getFclMailingList() {
        return fclMailingList;
    }

    public void setFclMailingList(String fclMailingList) {
        this.fclMailingList = fclMailingList;
    }

    public String getFwChbNo() {
        return fwChbNo;
    }

    public void setFwChbNo(String fwChbNo) {
        this.fwChbNo = fwChbNo;
    }

    public String getFwFmcNo() {
        return fwFmcNo;
    }

    public void setFwFmcNo(String fwFmcNo) {
        this.fwFmcNo = fwFmcNo;
    }

    public String getGoalAcct() {
        return goalAcct;
    }

    public void setGoalAcct(String goalAcct) {
        this.goalAcct = goalAcct;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getImpCommDesc() {
        return impCommDesc;
    }

    public void setImpCommDesc(String impCommDesc) {
        this.impCommDesc = impCommDesc;
    }

    public String getImpCommNo() {
        return impCommNo;
    }

    public void setImpCommNo(String impCommNo) {
        this.impCommNo = impCommNo;
    }

    public String getImportsCfs() {
        return importsCfs;
    }

    public void setImportsCfs(String importsCfs) {
        this.importsCfs = importsCfs;
    }

    public String getImportTrackingScreen() {
        return importTrackingScreen;
    }

    public void setImportTrackingScreen(String importTrackingScreen) {
        this.importTrackingScreen = importTrackingScreen;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public Integer getMaxDaysBetVisits() {
        return maxDaysBetVisits;
    }

    public void setMaxDaysBetVisits(Integer maxDaysBetVisits) {
        this.maxDaysBetVisits = maxDaysBetVisits;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPbaSurchrge() {
        return pbaSurchrge;
    }

    public void setPbaSurchrge(String pbaSurchrge) {
        this.pbaSurchrge = pbaSurchrge;
    }

    public String getPoa() {
        return poa;
    }

    public void setPoa(String poa) {
        this.poa = poa;
    }

    public String getReservedForFuture() {
        return reservedForFuture;
    }

    public void setReservedForFuture(String reservedForFuture) {
        this.reservedForFuture = reservedForFuture;
    }

    public String getRoutingInstruction() {
        return routingInstruction;
    }

    public void setRoutingInstruction(String routingInstruction) {
        this.routingInstruction = routingInstruction;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getSalesCodeName() {
        return salesCodeName;
    }

    public void setSalesCodeName(String salesCodeName) {
        this.salesCodeName = salesCodeName;
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GenericCode getImpCommodity() {
        return impCommodity;
    }

    public void setImpCommodity(GenericCode impCommodity) {
        this.impCommodity = impCommodity;
    }

    public GenericCode getSalescode() {
        return salescode;
    }

    public void setSalescode(GenericCode salescode) {
        this.salescode = salescode;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId1() {
        // TODO Auto-generated method stub
        return this.getId();
    }

    public String getIdText() {
        return idText;
    }

    public void setIdText(String idText) {
        this.idText = idText;
    }

    public String getInsuranceComment() {
        return insuranceComment;
    }

    public void setInsuranceComment(String insuranceComment) {
        this.insuranceComment = insuranceComment;
    }

    public String getInsure() {
        return insure;
    }

    public void setInsure(String insure) {
        this.insure = insure;
    }

    public Date getLdPwdActivated() {
        return ldPwdActivated;
    }

    public void setLdPwdActivated(Date ldPwdActivated) {
        this.ldPwdActivated = ldPwdActivated;
    }

    public String getEinMaster() {
        return einMaster;
    }

    public void setEinMaster(String einMaster) {
        this.einMaster = einMaster;
    }

    public String getCommodityMaster() {
        return commodityMaster;
    }

    public void setCommodityMaster(String commodityMaster) {
        this.commodityMaster = commodityMaster;
    }

    public String getImportMaster() {
        return importMaster;
    }

    public void setImportMaster(String importMaster) {
        this.importMaster = importMaster;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getExportAgent() {
        return exportAgent;
    }

    public void setExportAgent(String exportAgent) {
        this.exportAgent = exportAgent;
    }

    public String getImportAgent() {
        return importAgent;
    }

    public void setImportAgent(String importAgent) {
        this.importAgent = importAgent;
    }

    public String getNvoOtiLicenseNo() {
        return nvoOtiLicenseNo;
    }

    public void setNvoOtiLicenseNo(String nvoOtiLicenseNo) {
        this.nvoOtiLicenseNo = nvoOtiLicenseNo;
    }

    public String getAllwaysBillCoload() {
        return allwaysBillCoload;
    }

    public void setAllwaysBillCoload(String allwaysBillCoload) {
        this.allwaysBillCoload = allwaysBillCoload;
    }

    public String getCfclPortCode() {
        return cfclPortCode;
    }

    public void setCfclPortCode(String cfclPortCode) {
        this.cfclPortCode = cfclPortCode;
    }

    public String getMergeNoteInfo() {
        return mergeNoteInfo;
    }

    public void setMergeNoteInfo(String mergeNoteInfo) {
        this.mergeNoteInfo = mergeNoteInfo;
    }

    public String getErtRefNo() {
        return ertRefNo;
    }

    public String getCFCL() {
        return CFCL;
    }

    public void setCFCL(String CFCL) {
        this.CFCL = CFCL;
    }

    public void setErtRefNo(String ertRefNo) {
        this.ertRefNo = ertRefNo;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public String getHotCodes1() {
        return hotCodes1;
    }

    public void setHotCodes1(String hotCodes1) {
        this.hotCodes1 = hotCodes1;
    }

    public GenericCode getFclCommodity() {
        return fclCommodity;
    }

    public void setFclCommodity(GenericCode fclCommodity) {
        this.fclCommodity = fclCommodity;
    }

    public String getImportWebDapDdp() {
        return importWebDapDdp;
    }

    public void setImportWebDapDdp(String importWebDapDdp) {
        this.importWebDapDdp = importWebDapDdp;
    }

    public String getImportQuoteColoadRetail() {
        return importQuoteColoadRetail;
    }

    public void setImportQuoteColoadRetail(String importQuoteColoadRetail) {
        this.importQuoteColoadRetail = importQuoteColoadRetail;
    }

    public String getConsUserName() {
        return consUserName;
    }

    public void setConsUserName(String consUserName) {
        this.consUserName = consUserName;
    }

    public String getConsPassword() {
        return consPassword;
    }

    public void setConsPassword(String consPassword) {
        this.consPassword = consPassword;
    }

    public boolean isConsAllowLclWebQuotes() {
        return consAllowLclWebQuotes;
    }

    public void setConsAllowLclWebQuotes(boolean consAllowLclWebQuotes) {
        this.consAllowLclWebQuotes = consAllowLclWebQuotes;
    }

    public boolean isConsAllowFclWebQuotes() {
        return consAllowFclWebQuotes;
    }

    public void setConsAllowFclWebQuotes(boolean consAllowFclWebQuotes) {
        this.consAllowFclWebQuotes = consAllowFclWebQuotes;
    }

    public boolean isConsImportTrackingScreen() {
        return consImportTrackingScreen;
    }

    public void setConsImportTrackingScreen(boolean consImportTrackingScreen) {
        this.consImportTrackingScreen = consImportTrackingScreen;
    }

    public boolean isConsActivatePwdQuotes() {
        return consActivatePwdQuotes;
    }

    public void setConsActivatePwdQuotes(boolean consActivatePwdQuotes) {
        this.consActivatePwdQuotes = consActivatePwdQuotes;
    }

    public Date getConsLastPwdActivatedDate() {
        return consLastPwdActivatedDate;
    }

    public void setConsLastPwdActivatedDate(Date consLastPwdActivatedDate) {
        this.consLastPwdActivatedDate = consLastPwdActivatedDate;
    }

    public String getConsFclWebQuoteUseCommodity() {
        return consFclWebQuoteUseCommodity;
    }

    public void setConsFclWebQuoteUseCommodity(String consFclWebQuoteUseCommodity) {
        this.consFclWebQuoteUseCommodity = consFclWebQuoteUseCommodity;
    }

    public String getConsLclRateSheet() {
        return consLclRateSheet;
    }

    public void setConsLclRateSheet(String consLclRateSheet) {
        this.consLclRateSheet = consLclRateSheet;
    }

    public String getConsImportWebDapDdp() {
        return consImportWebDapDdp;
    }

    public void setConsImportWebDapDdp(String consImportWebDapDdp) {
        this.consImportWebDapDdp = consImportWebDapDdp;
    }

    public String getConsLclImportQuoting() {
        return consLclImportQuoting;
    }

    public void setConsLclImportQuoting(String consLclImportQuoting) {
        this.consLclImportQuoting = consLclImportQuoting;
    }

    public GenericCode getConsColoadCommodity() {
        return consColoadCommodity;
    }

    public void setConsColoadCommodity(GenericCode consColoadCommodity) {
        this.consColoadCommodity = consColoadCommodity;
    }

    public GenericCode getConsRetailCommodity() {
        return consRetailCommodity;
    }

    public void setConsRetailCommodity(GenericCode consRetailCommodity) {
        this.consRetailCommodity = consRetailCommodity;
    }

    public GenericCode getConsFclCommodity() {
        return consFclCommodity;
    }

    public void setConsFclCommodity(GenericCode consFclCommodity) {
        this.consFclCommodity = consFclCommodity;
    }

    public String getShipffCustControlLogin() {
        return shipffCustControlLogin;
    }

    public void setShipffCustControlLogin(String shipffCustControlLogin) {
        this.shipffCustControlLogin = shipffCustControlLogin;
    }

    public String getConsCustControlLogin() {
        return consCustControlLogin;
    }

    public void setConsCustControlLogin(String consCustControlLogin) {
        this.consCustControlLogin = consCustControlLogin;
    }

    public GenericCode getConsSalesCode() {
        return consSalesCode;
    }

    public void setConsSalesCode(GenericCode consSalesCode) {
        this.consSalesCode = consSalesCode;
    }

    public String getConsImportFreightRelease() {
        return consImportFreightRelease;
    }

    public void setConsImportFreightRelease(String consImportFreightRelease) {
        this.consImportFreightRelease = consImportFreightRelease;
    }

    public String getShipffImportFreightRelease() {
        return shipffImportFreightRelease;
    }

    public void setShipffImportFreightRelease(String shipffImportFreightRelease) {
        this.shipffImportFreightRelease = shipffImportFreightRelease;
    }

    public String getShipffSalesAgencyBrokerageAgreement() {
        return shipffSalesAgencyBrokerageAgreement;
    }

    public void setShipffSalesAgencyBrokerageAgreement(String shipffSalesAgencyBrokerageAgreement) {
        this.shipffSalesAgencyBrokerageAgreement = shipffSalesAgencyBrokerageAgreement;
    }

    public String getShipffReceiveLclExports315Status() {
        return shipffReceiveLclExports315Status;
    }

    public void setShipffReceiveLclExports315Status(String shipffReceiveLclExports315Status) {
        this.shipffReceiveLclExports315Status = shipffReceiveLclExports315Status;
    }

    public String getShipffInttraAccountNumber() {
        return shipffInttraAccountNumber;
    }

    public void setShipffInttraAccountNumber(String shipffInttraAccountNumber) {
        this.shipffInttraAccountNumber = shipffInttraAccountNumber;
    }

    public String getShipffSendLclDocsToWebsite() {
        return shipffSendLclDocsToWebsite;
    }

    public void setShipffSendLclDocsToWebsite(String shipffSendLclDocsToWebsite) {
        this.shipffSendLclDocsToWebsite = shipffSendLclDocsToWebsite;
    }

    public String getShipffAllowCFCLWebBooking() {
        return shipffAllowCFCLWebBooking;
    }

    public void setShipffAllowCFCLWebBooking(String shipffAllowCFCLWebBooking) {
        this.shipffAllowCFCLWebBooking = shipffAllowCFCLWebBooking;
    }

    public String getConsSalesAgencyBrokerageAgreement() {
        return consSalesAgencyBrokerageAgreement;
    }

    public void setConsSalesAgencyBrokerageAgreement(String consSalesAgencyBrokerageAgreement) {
        this.consSalesAgencyBrokerageAgreement = consSalesAgencyBrokerageAgreement;
    }

    public String getConsReceiveLclExports315Status() {
        return consReceiveLclExports315Status;
    }

    public void setConsReceiveLclExports315Status(String consReceiveLclExports315Status) {
        this.consReceiveLclExports315Status = consReceiveLclExports315Status;
    }

    public String getConsInttraAccountNumber() {
        return consInttraAccountNumber;
    }

    public void setConsInttraAccountNumber(String consInttraAccountNumber) {
        this.consInttraAccountNumber = consInttraAccountNumber;
    }

    public String getConsSendLclDocsToWebsite() {
        return consSendLclDocsToWebsite;
    }

    public void setConsSendLclDocsToWebsite(String consSendLclDocsToWebsite) {
        this.consSendLclDocsToWebsite = consSendLclDocsToWebsite;
    }

    public String getConsAllowCFCLWebBooking() {
        return consAllowCFCLWebBooking;
    }

    public void setConsAllowCFCLWebBooking(String consAllowCFCLWebBooking) {
        this.consAllowCFCLWebBooking = consAllowCFCLWebBooking;
    }

    public String getApplyCustomerCommodityRates() {
        return applyCustomerCommodityRates;
    }

    public void setApplyCustomerCommodityRates(String applyCustomerCommodityRates) {
        this.applyCustomerCommodityRates = applyCustomerCommodityRates;
    }

   
}
