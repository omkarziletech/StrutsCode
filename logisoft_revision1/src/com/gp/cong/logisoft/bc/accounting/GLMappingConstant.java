package com.gp.cong.logisoft.bc.accounting;

public interface GLMappingConstant {

    String GLMAPPINGFORM = "glmappingForm";
    String GLACCOUNTLIST = "glAccountList";
    String GLACCOUNT = "glAccount";
    String GLMAPPING_EXCEL_SHEET_NAME = "glMappingExcelSheetName";
    String GLMAPPING_BLUE_SCREEN_CHARGE_CODE = "Blue Screen Charge Code";
    String GLMAPPING_CHARGE_CODE = "Charge Code";
    String GLMAPPING_CHARGE_DESCRIPTION = "Charge Description";
    String GLMAPPING_GL_ACCOUNT = "GL Account";
    String GLMAPPING_SHIPMENT_TYPE = "Shipment Type";
    String GLMAPPING_TRANSACTION_TYPE = "Transaction Type";
    String GLMAPPING_SUFFIX_VALUE = "Suffix Value";
    String GLMAPPING_SUFFIX_ALTERNATE = "Suffix Alternate";
    String GLMAPPING_HEADER = "List of GL Accounts";
    String GL_MAPPING_CHARGE_CODE = "chargeCode";
    String GL_MAPPING_GL_EXP = "GL-EXP";
    String GL_MAPPING_DERIVE_YN = "Derive Y/N";
    String GL_MAPPING_SUBLEDGER_CODE = "Subledger Code";
    String GL_MAPPING_REV_EXP = "Rev/Exp";
    String REVENUE = "R";
    String EXPENSE = "E";
    String BOTH = "Both";
    //Terminal to Gl Mappings
    String LCL_EXPORT_BILLING = "lclExportBilling";
    String LCL_EXPORT_LOADING = "lclExportLoading";
    String LCL_EXPORT_DOCKRECEIPT = "lclExportDockreceipt";
    String FCL_EXPORT_BILLING = "fclExportBilling";
    String FCL_EXPORT_LOADING = "fclExportLoading";
    String FCL_EXPORT_DOCKRECEIPT = "fclExportDockreceipt";
    String AIR_EXPORT_BILLING = "airExportBilling";
    String AIR_EXPORT_LOADING = "airExportLoading";
    String AIR_EXPORT_DOCKRECEIPT = "airExportDockreceipt";
    String LCL_IMPORT_BILLING = "lclImportBilling";
    String FCL_IMPORT_BILLING = "fclImportBilling";
    String FCL_IMPORT_LOADING  = "fclImportLoading";
    String AIR_IMPORT_BILLING = "airImportBilling";
    String AIR_IMPORT_LOADING  = "airImportLoading";
    String INLAND_EXPORT_LOADING = "inlandExportLoading";
    //Shipment Types
    String LCLE="LCLE";
    String FCLE="FCLE";
    String AIRE="AIRE";
    String LCLI="LCLI";
    String FCLI="FCLI";
    String AIRI="AIRI";
    String INLE="INLE";
    //LCLE-B,L,D	    FCLE-B,L,D	      AIRE-B,L,D	   LCLI-B,L	   FCLI-B,L     	AIRI-B,L	    INLE-L
    //Derive YNs
    String FIXED = "F";
    String BILLING = "B";
    String LOADING = "L";
    String DOCK_RECEIPT = "D";
}
