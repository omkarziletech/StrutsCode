package com.gp.cong.logisoft.bc.fcl;

public interface FclBlConstants {

    String FCLBL = "fclBl";
    String COUNTRYNAME = "UNITED STATES";
    String TRANSACTIONBEAN = "transactionBean";
    String BLCORRECTIONLIST = "FclBlCorrectionList";
    String BLVOIDEDCORRECTIONLIST = "voidedCorrectionList";
    String BLCORRECTIONFORM = "FclBlCorrectionForm";
    String BLCORRECTIONFORMFORSESSION = "FclBlCorrectionFormforSession";
    String FCL_BL_CORRECTION = "FclBlCorrection";
    String FCL_BL_CHARGESLIST = "FclBlChargesList";
    String FCL_BL_CONTAINER = "FclBlContainer";
    String FCL_BL_NEWCORRECTION = "FclBlCorrection";
    String DELIMITER = "==";
    String NOTICE_NO_LIST = "noticeNumberList";
    String CUSTOMER_LIST = "customerContactList";
    String CODEC_CUSTOMER_LIST = "codeCContactList";
    String CODEJ_CUSTOMER_LIST = "codeJContactList";
    String CODEF_CUSTOMER_LIST = "codeFContactList";
    String CODEF_NAME_EMAIL_LIST = "codeFContactNameAndEmail";
    String COLOADER_EMAIL1 = "coloaderEmail1";
    String COLOADER_FAX = "coloaderFax";
    String HOUSE_CONSIGNEE_EMAIL1 = "houseConsigneeEmail1";
    String HOUSE_CONSIGNEE_FAX = "houseConsigneeFax";
    String NOTIFY_PARTY_EMAIL1 = "notifyPartyEmail1";
    String NOTIFY_PARTY_FAX = "notifyPartyFax";
    String NOTIFY2_PARTY_EMAIL1 = "notify2PartyEmail1";
    String NOTIFY2_PARTY_FAX = "notify2PartyFax";
    String CFS_DEV_WARHSE_MANAGER_EMAIL = "cFSDevWarhseManagerEmail";
    String CFS_DEV_WARHSE_MANAGER_FAX = "cFSDevWarhseManagerFax";
    String CONTACTBUTTON = "contactButton";
    int CODETYPEID = 51;
    String HAZMATQUOTE = "Booking";
    String HAZMATQUOTEFORBL = "FclBl";
    String CLOSED = "blcolsed";
    String AUDITED = "blaudited";
    String OPNED = "blOpen";
    String CANCELAUDITED = "blAuditedCancel";
    String EQUALDELIMITER = "-";
    String CHARACTERDELIMITER = "A";
    String FCLBL_COSTS_LIST = "FclBlCostsList";
    String FCLBL_FORM = "fclBillLaddingform";
    String BLVOIDLIST = "blVoidList";
    String INTRA = "INTTRA";
    String GTNEXUS = "GT Nexus";
    String DISABLE = "Disable";
    String SENDCREDIT_LIST = "sendCreditMemoList";
    // charge code name
    String OCEANCODE = "OCNFRT";
    String FFCODE = "FFCOMM";
    String FFCODETWO = "105";
    String FFCODETHREE = "005";
    String FFCODEDESC = "FF COMMISSION";
    String FAECODE = "FAECOMM";
    String FAECODEDESC = "FAE COMMISSION";
    String ADMINFEEWITHNOCOMMISION = "INCENT";
    String FAECSC = "FAECSC";
    String FAECSCDESC = "FAE CSC/THC";
    String PBASURCHARGECODE = "PBASUR";
    String PBASURCHARGEDESC = "PBA SURCHARGE";
    String ADVANCEFFCODE = "ADVFF";
    String ADVANCEFFDESC = "ADVANCE CHGS - FF";//
    String ADVANCESHIPPERCODE = "ADVSHP";// please change in checkConfirmBoard() in fclbillLading.js
    String ADVANCESHIPPERDESC = "ADVANCE CHGS - SHR";
    String ADVANCESURCHARGECODE = "ADVSUR";
    String ADVANCESURCHARGEDESC = "ADVANCE SURCHARGE";
    String PBACODE = "PBA";// please change in checkConfirmBoard() in fclbillLading.js
    String PBADESC = "PBA";
    String ADMIN = "ADMIN";
    String INTRAMP = "INTRAMP";
    String INTMDL = "INTMDL";
    String CONSIGNEE = "Consignee";
    String INTFS = "INTFS";
    Integer FIRSTUNITTYPEID = 11307;
    String ROUTEDFORADMIN = "routedForAdmin";
    String ROUTEDFORCOMM = "routedForComm";
    String AGENTFORADMIN = "agentForAdmin";
    String AGENTFORCOMM = "agentForComm";
    String FORWARDERNO = "NO FF ASSIGNED";
    String FORWARDERNO2 = "NO FF ASSIGNED / B/L PROVIDED";
    String FORWARDERNO3 = "NO FRT. FORWARDER ASSIGNED";
    String CONTAINERRUEL = "$ PER CONTAINER";
    String PERBLRUEL = "$ PER BL CHARGES";
    String MULTICONTAINERRUEL = "$ MULTI-CONTAINER TIER";
    String PERCENTAGERUEL = "PERCENTAGE OF PROFIT";
    String CNA0 = "0-CNA";//CORRECTIONNOTICEFORTL
    String CNS = "-CNS";//CORRECTIONNOTICEFORTL
    String CNA = "-CNA";//CORRECTIONNOTICEFORTL
    String CNA00 = "0-CNA-";//CORRECTIONNOTICEFORTL
    String UPDATEFUNTION = "UpdateAmount";
    String SUBLADGER = "AR-CN";
    String UPDATEFUNTION2 = "updateCustomer";
    String VOIDED = "VOIDED";
    String DEBITNOTE = "DEBIT NOTE";
    String CREDTINOTE = "CREDIT NOTE";
    String PERDMCODEDESC = "PER DIEM";
    String PERDMCODE = "PERDIEM";
    /// ..............
    String REQUESTPAGE = "fclBillLaddingformForContainer";
    //Bill_to Code constants of BL
    String FORWARDER = "F";
    String SHIPPER = "S";
    String THIRDPARTY = "T";
    String AGENT = "A";
    String DEFAULT_FREIGHT_FORWARDER_NAME = "NO FF ASSIGNED";
    String DEFAULT_IMPORT_FLAG = "I";
    String DEFAULT_FREIGHT_FORWARDER_NO = "NOFFAA0001";
    String FREE_FORMAT_STRING = "REPORTABLE QUANTITY, UN, CLASS, PG, FLASH POINT( DEG ), "
            + "@ EACH,TOTAL GROSS WEIGHT, TOTAL VOLUME LITER, MARINE POLLUTANT, EXCEPTED QUANTITY, LIMITED "
            + "QUANTITY, INHALATION HAZARD, RESIDUE, EMS";
    
    public static final String VENDOR_FORWARDER = "Vendor(Forwarder),";
}
