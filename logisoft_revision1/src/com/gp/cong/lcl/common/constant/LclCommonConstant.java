/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

/**
 *
 * @author Meiyazhakan
 */
public interface LclCommonConstant {

    //Constant for BlueScreenCode using Pickup Details in Export
    String CHARGE_CODE_INLAND = "INLAND";
    String CHARGE_CODE_IPI = "IPI";
    String CHARGE_CODE_IPI_HAZ = "IPIHAZ";
    String CHARGE_CODE_CAF = "CAF";
    String BLUESCREEN_CHARGE_CODE_INLAND = "0012";
    //Constant for BlueScreenCode using Door Delivery Details in Import
    String CHARGE_CODE_DOOR = "DOORDEL";
    String BLUESCREEN_CODE_DOOR = "1612";
    // Lcl ShipmentType for Import
    String LCL_SHIPMENT_TYPE_IMPORT = "LCLI";
    // Lcl ShipmentType for Import
    String LCL_SHIPMENT_TYPE_EXPORT = "LCLE";
    //Constant for Transcation Type
    String LCL_TRANSACTION_TYPE_AR = "AR";
    //Constant for BlueScreenCode using Documentation Details both Export & Import
    String BLUESCREEN_CHARGECODE_DOCUM = "DOCUM";
    //Eculine Edi
    String TRANS_MODE_VESSEL = "V";
    String SERVICE_MODE_IMPORT = "I";
    String STATUS_PENDING = "P";
    String DATA_SOURCE_LOGIWARE = "L";
    String STATUS_EMPTY = "E";
    String DISPOSITION_DATA = "DATA";
    String RATETYPE_RETAIL = "R";
    String RATETYPE_FTF = "F";
    String BILLTYPE_COLLECT = "C";
    String PERSONAL_EFFECTS_NO = "N";
    //account type remarks
    String CLIENT = "manual";
    String SUPPLIER = "supplier";
    String SHIPPER = "shipper";
    String CONSIGNEE = "consignee";
    String FORWARDER = "forwarder";
    String NOTIFY = "notify";
    String CONTACT_TYPE_CLIENT = "client";
    String CONTACT_TYPE_NOTIFY2 = "notify2";
    //Container types
    String _20DV = "20ft dry freight container";
    String _40DV = "40ft dry freight container";
    String _40HC = "40ft high cube dry freight container";
    //LCL TYPES
    String LCL_EXPORT_TYPE = "E";
    String LCL_IMPORT_TYPE = "I";
    String LCL_TRANSHIPMENT_TYPE = "T";
    String LCL_TRANSMODE_R = "R";
    String LCL_OVERRIDEDIM_TYPE_I = "I";
    // Disposition Elite Code
    String LCL_DISPOSITION_DATA = "DATA";
    String LCL_DISPOSITION_WATR = "WATR";
    //LCL
    String LCL_IMPORT = "Imports";
    String LCL_EXPORT = "Exports";
    //POD and FD Different BlueCharge Code
    String LCL_IMP_BLUESCREEN_CHARGE_CODE = "1607,1617";
    String LCL_DESTFEE1_BLUESCREEN_CHARGE_CODE = "0350";
    String LCL_DESTFEE3_BLUESCREEN_CHARGE_CODE = "0351";
    //scan/Attach Screen Name
    String LCL_IMPORTS_SCREENNAME = "LCL IMPORTS DR";
    String LCL_EXPORTS_SCREENNAME = "LCL EXPORTS DR";
    String LCL_UNITSIMP_SCREENNAME = "LCL IMPORTS UNIT";
    String LCL_UNITSEXP_SCREENNAME = "LCL EXPORTS UNIT";
    String LCL_SCAN_OPERATION = "Scan or Attach";
    String REMARKS_TYPE_MANUAL = "manual";
    String REMARK_TYPE_MANUAL = "Manual Note";
    String REMARKS_TYPE_AUTO = "auto";
    String REMARKS_TYPE_T = "T";
    String REMARKS_TYPE_GRI = "G";
    String GRI_REMARKS_FD = "Gri Remarks Pod";
    String REMARKS_TYPE_SPECIAL_REMARKS = "Special Remarks";
    String REMARKS_TYPE_INTERNAL_REMARKS = "Internal Remarks";
    String SPECIAL_REMARKS_FD = "Special Remarks Pod";
    String INTERNAL_REMARKS_FD = "Internal Remarks Pod";
    String REMARKS_TYPE_OSD = "OSD";
    String REMARKS_TYPE_EXTERNAL_COMMENT = "E";
    String REMARKS_TYPE_LOADING_REMARKS = "Loading Remarks";
    String REMARKS_LABEL_LOADING_REMARKS = "RMKS:";
    String REMARKS_TYPE_PRIORITY_VIEW = "Priority View";
    String REMARKS_TYPE_AUTO_RATES = "AutoRates";
    String LCL_3PREF_TYPE_OTI = "OTI";
    String REMARKS_QUOTE_CREATED = "Quote Created";
    String REMARKS_TYPE_LCL_CORRECTIONS = "LclCorrections";
    String REMARKS_TYPE_CTS_EDI = "CTS_EDI";
    String REMARKS_TYPE_EXPORT_REF = "Export Reference";
    String REMARKS_TYPE_ROUTING_INSTRU = "Routing Instruction";
    String LCL_FILE_TYPE_QUOTE = "LCL_IMP_QUOTE";
    // Email Types
    String EMAIL_TYPES = "E1,E2,E3";
    String FAX_TYPES = "F1,F2,F3";
    String EMAIL_TYPE_E1 = "E1";
    String EMAIL_TYPE_E2 = "E2";
    String EMAIL_TYPE_E3 = "E3";
    String FAX_TYPE_F1 = "F1";
    String FAX_TYPE_F2 = "F2";
    String FAX_TYPE_F3 = "F3";
    String EDI_CTS = "cts";
    String _3PARTY_TYPE_HTC = "HTC";//3Party Hot Code Type
    String _3PARTY_TYPE_CP = "CP";//3Party Customer Code Type
    String _3PARTY_TYPE_NCM = "NCM";//3Party NCM Type
    String _3PARTY_TYPE_WH = "WH";//3Party Ware house Doc Type
    String _3PARTY_TYPE_HSC = "HSC";//3Party HS Code Type
    String _3PARTY_TYPE_TR = "TR";//3Party Tracking Type
    String _3PARTY_TYPE_OVR = "OVR";//3Party Hot Code Reference
    String _3PARTY_AES_ITNNUMBER = "AES_ITNNUMBER";
    String _3PARTY_AES_EXCEPTION = "AES_EXCEPTION";
    String CHARGE_CODE_EXWORK = "EXWORK";
    String NOTIFICATION_FROM_NAME = "ECI Automated Notification";
    String NOTIFICATION_TEXT_MESSAGE = "Automated Notification of Imports Container disposition change";
    String EXP_NOTIFICATION_TEXT_MESSAGE = "Automated Notification of Exports disposition change";
    String EXP_NOTIFICATION_CODJ_MESSAGE = "Automated Notification of Exports";
    String DISP_NOTIFICATION_STATUS_MINUTE = "MINUTE";
    String DISP_NOTIFICATION_STATUS_DAILY = "DAILY";
    String STATUS_I = "I";
    String STATUS_M = "M";
    String STATUS_AR = "AR";
    String SCREENNAME_AR_INVOICE = "IMP VOYAGE";
    String SCREENNAME_EXP_AR_INVOICE = "EXP VOYAGE";
    String SCREENNAME_DR_AR_INVOICE = "LCLI DR";
    String REMARKS_STATUS_OUTSOURCE = "Outsource";
    String RATE_UNIT_PER_UOM_WEIGHT = "W";
    String RATE_UNIT_PER_UOM_VOLUME = "V";
    String RATE_UNIT_PER_UOM_MIN = "M";
    String RATE_UNIT_PER_UOM_MAX = "MAX";
    String RATE_UNIT_PER_UOM_FRW = "FRW";
    String RATE_UNIT_PER_UOM_FRV = "FRV";
    String RATE_UNIT_PER_UOM_FRM = "FRM";
    String RATE_UNIT_PER_UOM_PCT = "PCT";
    String RATE_UNIT_PER_UOM_FL = "FL";
    String RATE_UOM_I = "I";
    String RATE_UOM_M = "M";
    String FILE_STATE_QUOTE = "Q";
    String FILE_STATE_BOOKING = "B";
    String FILE_STATE_BL = "BL";
    String AR_RED_INVOICE_SCREENNAME_VOYAGE = "IMP VOYAGE";
    String AR_RED_INVOICE_SCREENNAME_DR = "LCLI DR";
    //Ports Table Column Name
    String PORTS_HSCODE = "hscode";
    String PORTS_ECIPORTCODE = "eciportcode";
    String PORTS_ENGMET = "engmet";
    //RoleDuty Table ColumnName
    String BKG_IMP_TERMINATE = "lcl_import_terminate";
    String BL_UN_POST = "unPost";
    String EDIT_LCL_BL_OWNER = "edit_lcl_bl_owner";
    //ChargeCode
    String CAF_CHARGE_CODE = "CAF";
    String HAZMET_CHARGE_CODE = "HAZFEE";
    String BL_PRINT_OPTION_FPFEILD = "FPFEILD";
    String BL_PRINT_OPTION_PORTFIELD = "PORTFIELD";
    String DESTINATION_SERVICES = "DTHC PREPAID SERVICE";
    String REMARKS_QT_AUTO_NOTES = "QT-AutoNotes";
    String REMARKS_QT_MANUAL_NOTES = "QT-ManualNotes";
    String REMARKS_DR_AUTO_NOTES = "DR-AutoNotes";
    String REMARKS_DR_MANUAL_NOTES = "DR-ManualNotes";
    String REMARKS_BL_AUTO_NOTES = "BL-AutoNotes";
    String REMARKS_BL_MANUAL_NOTES = "BL-ManualNotes";
    String REMARKS_ON_HOLD_NOTES = "OnHoldNotes";
    String DOORDELIVERYSEARCHSCREEN ="doorDeliverySearchTab";
    
    String BOOKED_OVERRIDE_EXISTING_REMARKS="Booked Override Existing Values-Yes->";
    String ACTUAL_OVERRIDE_EXISTING_REMARKS="Actual Override Existing Values-Yes->";
}
