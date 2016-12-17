package com.gp.cong.common;

/**
 *
 * @author Lakshmi Narayanan
 */
public interface ConstantsInterface {

    String NEWLINE = System.lineSeparator();
    //button actions
    String SAVE_ACTION = "save";
    String UPDATE_ACTION = "update";
    String EDIT_ACTION = "edit";
    String DELETE_ACTION = "delete";
    String ADD_ACTION = "add";
    String YES = "Y";
    String NO = "N";
    String BOTH = "BOTH";
    String ON = "on";
    String OFF = "off";
    String TRUE = "true";
    String FALSE = "false";
    String ALL = "ALL";
    String NONE = "NONE";
    String ONLY = "Only";
    //constants for currency code
    String CURRENCY_CODE = "USD";
    //constants for invoice number description
    String CODE_TYPE_DESCRIPTION_INVOICENO = "LCL Invoice Number";
    String GENERIC_CODE_INVOICENO = "LCL_INV";
    //constants for Bl Correction Type
    String CODE_TYPE_DESCRIPTION_BL_CORRECTION = "Bl Correction Type";
    String CODE_TYPE_DESCRIPTION_BL_CORRECTION_CODE = "Bl Correction Code";
    String GENERIC_CODE_A_BL_CORRECTION = "A";
    String GENERIC_CODE_Y_BL_CORRECTION = "Y";
    String GENERIC_CODE_S_BL_CORRECTION = "S";
    String GENERIC_CODE_A_CORRECTION_IMPORTS = "003";
    String GENERIC_EVENT_CODE_CORRECTION_NOTES = "100016";
    //constants for AP transaction type & status
    String TRANSACTION_TYPE_ACCRUALS = "AC";
    String TRANSACTION_TYPE_DISPUTE = "DS";
    String TRANSACTION_TYPE_IN_PROGRESS = "IP";
    String TRANSACTION_TYPE_PENDING = "PN";
    String TRANSACTION_TYPE_INACTIVE_ACCRUALS = "Inactive AC";
    String TRANSACTION_TYPE_ACCOUNT_PAYABLE = "AP";
    String TRANSACTION_TYPE_ACCOUNT_RECEIVABLE = "AR";
    String TRANSACTION_TYPE_RECEIPTS = "RC";
    String TRANSACTION_TYPE_READY_TO_PAY = "RP";
    String TRANSACTION_TYPE_PAYAMENT = "PY";
    String TRANSACTION_TYPE_CASH_DEPOSIT = "CD";
    String TRANSACTION_TYPE_PAYMENT_WITH_ACCOUNT_PAYABLE = "PY+AP";
    String STATUS_OPEN = "Open";
    String STATUS_ASSIGN = "AS";
    String STATUS_PAYABLE = "PA";
    String STATUS_HOLD = "H";
    String STATUS_READY_TO_PAY = "RP";
    String STATUS_PAID = "PY";
    String STATUS_REJECT = "R";
    String STATUS_VOID = "Void";
    String STATUS_INACTIVE = "inactive";
    String STATUS_PAY = "P";
    String STATUS_WAITING_FOR_APPROVAL = "WFA";
    String STATUS_WAITING_TO_SEND = "WTS";
    String STATUS_READY_TO_SEND = "Ready To Send";
    String STATUS_SENT = "Sent";
    String STATUS_APPROVED = "Approved";
    String STATUS_UNASSIGN = "Unassigned";
    String STATUS_DISPUTE = "Dispute";
    String STATUS_CLOSED = "Closed";
    String STATUS_CLEARED = "Cleared";
    String STATUS_IN_PROGRESS = "I";
    String STATUS_RECONCILE_IN_PROGRESS = "RIP";
    String STATUS_POSTED_TO_GL = "PostedtoGL";
    String STATUS_CHARGE_CODE = "ChargeCode";
    String STATUS_CHARGE_CODE_POSTED = "ChargeCodePosted";
    String STATUS_RESOLVED = "resolved";
    String STATUS_PENDING = "pending";
    String STATUS_POSTED = "posted";
    String STATUS_DELETED = "deleted";
    String STATUS_READY_TO_POST = "ready to post";
    Integer CHARGE_CODE_TYPE = 2;
    Integer COST_CODE_TYPE = 36;
    //constants for account payable
    String PAY_ALL_VENDOR = "payAllVendor";
    String RELEASE_TRANSACTION = "releaseTransaction";
    String SHOW_TRANSACTION_DETAILS = "showTransactionDetails";
    String TRANSACTION_DETAILS_PAGE = "transactionDetails";
    String SHOW_IN_PROGRESS = "showInProgress";
    String SHOW_ACCRUALS = "showAccruals";
    String AP_INQUIRY_PAGE = "AP Inquiry";
    //constants for ARInquiry
    String SHOW_MORE_TRANSACTION_INFORMATION = "showMoreTransactionInfo";
    String TRANSACTION_DETAILS_MAP = "transactionDetails";
    String TRANSACTION_INFORMATION = "transactionInfo";
    String TRAILER_NUMBERS = "trailerNumbers";
    String ESTATEMENT_TO_EXCEL = "eStatementToExcel";
    String CLEAR = "clear";
    //common constants for AccrualsPage
    String REJECT_INVOICE = "rejectInvoice";
    String DISPUTE_INVOICE = "disputeInvoice";
    String AMOUNT_NOT_EQUAL_TO_ASSIGNED_ACCRUALS_AMOUNT = "amountNotEqualToAssignedAccrualsAmount";
    String AMOUNT_EQUAL_TO_ASSIGNED_ACCRUALS_AMOUNT = "amountEqualToAssignedAccrualsAmount";
    // for Item structure
    String UNIQUE_ITEM = "uniqueItem";
    String QUOTE_CODE = "QOT";
    String BOOKING_CODE = "BOK";
    String FCL_CODE = "FCL";
    String SEARCH_FILE_NUMBER = "SFN";//
    String QUOTE_CODE_IMP = "IMPQOT";
    String BOOKING_CODE_IMP = "IMPBOK";
    String FCL_CODE_IMP = "IMPBL";
    String SEARCH_FILE_NUMBER_IMP = "SFI";
    //property value
    String SEARCH_LIMIT = "search_limit";
    int DEFAULT_SEARCH_LIMIT = 100;
    //scan page
    String SCAN_LIST = "scanList";
    String SCAN_SUB_LIST1 = "scanSubList1";
    String SCAN_SUB_LIST2 = "scanSubList2";
    String SCAN_SCREEN_NAME_LIST = "scanScreenNameList";
    String FILTER_SCREEN_NAME_LIST = "filterScreenNameList";
    String SCAN_SCREEN_DOCUMENT_TYPE_LIST = "scanScreenDocumentTypeList";
    String SYSTEM_RULE = "systemRule";
    String FILE_LOCATION = "FileLocation";
    String FILE_NAME = "FileName";
    String PAGE_ACTION_ATTACH = "Attach";
    String PAGE_ACTION_SCAN = "Scan";
    String DOCUMENT_STORE_LOG = "documentStoreLog";
    String SS_MASTER_BL = "SS LINE MASTER BL";
    String AGENT_HOUSE_BL = "AGENT HOUSE BL";
    String ORGIN_INVOICE = "ORIGIN INVOICE";
    String FCLFILE = "FCLFILE";
    //Print page
    String PRINT_LIST = "printList";
    String FAX_COVER_LETTER = "FaxCoverLetter";
    String LCL_PRINT_LIST = "lclPrintList";
    //customer contact mode
    String CONTACT_MODE_EMAIL = "Email";
    String CONTACT_MODE_FAX = "Fax";
    String CONTACT_MODE_PRINT = "Print";
    String CONTACT_MODE_LABEL_PRINT = "Label Print";
    String CONTACT_MODE_NO = "No";
    String EMAIL_STATUS_PENDING = "Pending";
    String EMAIL_STATUS_COMPLETED = "Completed";
    String EMAIL_STATUS_DISPUTE = "Dispute";
    //common constants for role
    String ROLE_NAME_SUPERVISOR = "Supervisor";
    String ROLE_NAME_ADMIN = "Admin";
    String ROLE_NAME_APSPECIALIST = "APSpecialist";
    String ROLE_NAME_COLLECTOR = "Collector";
    String ROLE_NAME_SALES = "SALES";
    String ROLE_NAME_CUSTOMER = "CUSTOMER";
    String ROLE_NAME_INTERNATIONAL_COLLECTIONS = "InternationalCollections";
    String ROLE_NAME_ARCLERK = "ARClerk";
    String ROLE_NAME_PETTY_CASH = "PettyCash";
    String ROLE_NAME_ACCOUNTING_SUPERVISOR = "AccountingSupervisor";
    String ROLE_NAME_AP_MANAGER = "APManager";
    //common constants for payment type
    String PAYMENT_METHOD_CHECK = "CHECK";
    String PAYMENT_METHOD_ACH = "ACH";
    String PAYMENT_METHOD_WIRE = "WIRE";
    String PAYMENT_METHOD_ACH_DEBIT = "ACH DEBIT";
    String PAYMENT_METHOD_CREDIT_CARD = "CREDIT CARD";
    //Common constants for SystemRuleCode
    String SYSTEM_RULE_CODE_FEDERAL_ID = "FederalId";
    String SYSTEM_RULE_CODE_COMPANY_CODE = "CompanyCode";
    String SYSTEM_RULE_CODE_COMPANY_NAME = "CompanyName";
    String SYSTEM_RULE_CODE_EMAIL = "Email";
    //common constants for SubLedgerCode
    String SUB_LEDGER_CODE_PURCHASE_JOURNAL = "PJ";
    String SUB_LEDGER_CODE_CASH_DEPOSIT = "CD";
    String SUB_LEDGER_CODE_ACCRUALS = "ACC";
    String SUB_LEDGER_CODE_RCT = "RCT";
    String SUB_LEDGER_CODE_NET_SETT = "NET SETT";
    //Common constants for Reports
    String BATCH_REPORT = "BatchReport";
    String SUB_LEDGER_REPORT = "SubLegerReport";
    String PURCHASE_JOURNAL_REPORT = "PurchaseJournalReport";
    String CASH_DISBURSEMENT_REPORT = "CashDisbursementReport";
    //Common constants for GL Account Number
    String AP_CONTROL_ACCOUNT = "apControlAccount";
    String AR_CONTROL_ACCOUNT = "arControlAccount";
    //Common constants for Date Type
    String INVOICE_DATE = "invoiceDate";
    String PAYMENT_DATE = "paymentDate";
    //Common constants for Sort By
    String SORT_BY_GL_ACCOUNT = "glAccount";
    String SORT_BY_VENDOR = "vendor";
    String SORT_BY_CHARGECODE = "chargeCode";
    String SORT_BY_SUB_LEDGER = "subLedger";
    String SORT_BY_AR_BATCH_ID = "arBatchId";
    String SORT_BY_TRANSACTION_DATE = "transactionDate";
    String SORT_BY_USER = "user";
    String SORT_BY_BILL_OF_LADDING = "billOfLadding";
    String SORT_FOR_ALL_SUBLEDGERS = "ForAllSubledgers";
    String SORT_BY_CHECK_NUMBER = "checkNumber";
    String SORT_FOR_PJ_SUBLEDGER = "ForPJSubledger";
    //CommonConstants for AP Reports
    String AP_AGING_REPORT = "Aging";
    String AP_ADJUSTED_ACCRUALS_REPORT = "AdjustedAccruals";
    String AP_VENDOR_REPORT = "Vendor";
    String AP_ACTIVITY_REPORT = "Activity";
    String AP_TIME_LAPSE_REPORT = "TimeLapse";
    String AP_PAYMENT_REPORT = "Payment";
    String AP_VOIDED_CHECK_REPORT = "VoidedCheck";
    String AP_CHECK_REGISTER_REPORT = "CheckRegister";
    String AP_ACCOUNT_REPORT = "Account";
    String AP_VOLUME_REPORT = "Volume";
    String AP_DPO_REPORT = "DPO";
    String AP_DISPUTED_ITEMS_REPORT = "DisputedItems";
    String AP_DISPUTED_EMAIL_LOG_REPORT = "DisputedEmailLog";
    String AP_REJECTED_ITEMS_REPORT = "RejectedItems";
    String AP_SUMMARY_REPORT = "Summary";
    String AP_DETAIL_REPORT = "Detail";
    String ALL_ACCOUNTS_PAYABLE = "AllAccountsPayable";
    String BY_USER = "ByUser";
    String BY_VENDOR = "ByVendor";
    String All_BANK_ACCOUNT = "AllBankAccount";
    // //CommonConstants for AR Reports
    String AR_DSO_REPORT = "DSO";
    String ALL_ACCOUNTS_RECEIVABLE = "AllAccountsReceivable";
    String BY_COLLECTOR = "ByCollector";
    String BY_CUSTOMER = "ByCustomer";
    String AR_ACCOUNT_NOTES_REPORT = "ArAccountNotesReport";
    // print name for creditDebitnote
    String CREDITDEBITNOTE = "CreditDebitNote";
    String LCL_CREDITDEBITNOTE = "LclCreditDebitNote";
    //Constants for Print
    String SCREEN_NAME_QUOTATION = "Quotation";
    String DOCUMENT_NAME_QUOTATION = "Quotation";
    String SCREEN_NAME_MultiQuote = "MultiQuote";
    String DOCUMENT_NAME_MultiQuote = "MultiQuote";
    String DOCUMENT_NAME_CORRECTIONS = "Corrections";
    String DOCUMENT_NAME_LCL_CORRECTIONS = "LCLCorrections";
    String SCREEN_NAME_BOOKINGRATES = "Booking";
    String DOCUMENT_NAME_BOOKINGRATES = "Booking Confirmation With Rate";
    String DOCUMENT_NAME_BOOKINGNONRATES = "Booking Confirmation Without Rate";
    String DOCUMENT_NAME_BOOKINGCOVERSHEET = "Booking Cover Sheet";
    String DOCUMENT_NAME_PICKUPORDER = "Pickup Order";
    String SCREEN_NAME_BL = "BL";
    String SCREEN_NAME_CORRECTIONS = "Corrections";
    String SCREEN_NAME_LCL_CORRECTIONS = "LCLCorrections";
    String DOCUMENT_NAME_UNFREIGHTED_ORIGINALBL = "UnFreighted Original House BL";
    String DOCUMENT_NAME_UNFREIGHTED_NONNEGOTIABLE = "UnFreighted Non-neg House BL";
    //String DOCUMENT_NAME_UNFREIGHTED_MASTERBL="UnFreightedMasterBL";
    //String DOCUMENT_NAME_STREAMSHIPBL_WITHBKG ="StreamShipBlwithBkg#";
    String DOCUMENT_NAME_STREAMSHIP_BL = "Steam Ship Master BL";
    String DOCUMENT_NAME_BL_MANIFESTED = "Manifest";
    String UNMARKED_HOUSE_BILLOFLADDING = "Unmarked House Bill of Lading";
    String DOCUMENT_NAME_VGM_DECLARATION = "VGM Declaration";
    String DOCUMENT_NAME_FREIGHTED_ORIGINALBL = "Freighted Original House BL";
    String DOCUMENT_NAME_FREIGHTED_NONNEGOTIABLE = "Freighted Non-neg House BL";
    //String DOCUMENT_NAME_FREIGHTED_MASTERBL ="FreightedMasterBL";
    String DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER = "FreightInvoice(Shipper)";
    String DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER = "FreightInvoice(Forwarder)";
    String DOCUMENT_NAME_FREIGHT_INVOICE_AGENT = "FreightInvoice(Agent)";
    String DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY = "FreightInvoice(ThirdParty)";
    String DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE = "FreightInvoice(Consignee)";
    String DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY = "FreightInvoice(NotifyParty)";
    String DOCUMENT_NAME_FCL_ARRIVALNOTICE = "FCLArrivalNotice";
    String DOCUMENT_NAME_FCL_ARRIVALNOTICE_NONRATED = "FCLArrivalNotice(Non-Rated)";
    String DOCUMENT_NAME_CONFIRM_ONBOARD_NOTICE = "ConfirmOnBoardNotice";
    String FREIGHTED_ORIGINAL_HOUSE_BL = "Freighted Original House BL";
    String UNFREIGHTED_ORIGINAL_HOUSE_BL = "UnFreighted Original House BL";
    String DOCUMENT_NAME_AR_INVOICE = "Invoice";
    String CONTAINER_RESPONSIBILITY_WAIVER = "Container Responsibility Waiver";
    String AUTHORITY_TO_MAKE_ENTRY = "Authority To Make Entry";
    String DELIVERY_ORDER = "Delivery Order";

    //Constants for SearchByType
    String SEARCH_BY_BILL_LADDING_NUMBER = "BillLaddingNumber";
    String SEARCH_BY_DOCK_RECEIPT = "Dock Receipt";
    String SEARCH_BY_CUSTOMER_REFERENCE_NUMBER = "Customer Reference Number";
    String SEARCH_BY_CHECK_NUMBER = "Check Number";
    String SEARCH_BY_INVOICE_BL_DR = "Invoice/BL/DR";
    String SEARCH_BY_INVOICE_BL_DR_AMOUNT = "Invoice/BL/DR Amount";
    String SEARCH_BY_INVOICE_NUMBER = "Invoice Number";
    String SEARCH_BY_INVOICE_BL = "Invoice/BL";
    String SEARCH_BY_INVOICE_AMOUNT = "Invoice Amount";
    String SEARCH_BY_INVOICE_BALANCE = "Invoice Balance";
    String SEARCH_BY_CHECK_AMOUNT = "Check Amount";
    String SEARCH_BY_VOYAGE = "Voyage";
    String SEARCH_BY_COST_CODE = "Cost Code";
    String SEARCH_BY_CONTAINER = "Container Number";
    String SEARCH_BY_HOUSE_BILL = "House Bill";
    String SEARCH_BY_SUB_HOUSE_BILL = "Sub-House Bill";
    String SEARCH_BY_MASTER_BILL = "Master Bill";
    String SEARCH_BY_BOOKING_NUMBER = "Booking Number";
    String SEARCH_BY_IT = "IT";
    String SEARCH_BY_DISPUTED = "Disputed";
    //Constants for page Type
    String PAGE_AR_APPLYPAYMENT = "arApplypayment";
    String PAGE_AR_INQUIRY = "arInquiry";
    String PAGE_ACCRUALS = "Accruals";
    String PAGE_ADD_ACCRUALS = "AddAccruals";
    String PAGE_AP_INQUIRY = "apInquiry";
    String PAGE_GL_MAPPING = "glMapping";
    String PAGE_RECONCILE = "reconcile";
    String PAGE_SUB_LEDGER = "subLedger";
    //Constants for AutoCompleter
    String AUTOCOMPLETER_CHARGE_CODE = "ChargeCode";
    String AUTOCOMPLETER_GL_ACCOUNT = "GlAccount";
    String AUTOCOMPLETER_VENDOR_NAME = "Vendor";
    String AUTOCOMPLETER_CUSTOMER_NAME = "Customer";
    String AUTOCOMPLETER_USER_NAME = "User";
    String AUTOCOMPLETER_COLLECTOR = "Collector";
    String AUTOCOMPLETER_EMAIL = "Email";
    String AUTOCOMPLETER_BANK = "Bank";
    String AUTOCOMPLETER_MODULE = "Module";
    String AUTOCOMPLETER_SUBLEDGER = "SubLedger";
    String AUTOCOMPLETER_FISCAL_PERIOD = "FiscalPeriod";
    String AUTOCOMPLETER_ORIG_TERMINAL = "OriginalTerminal";
    String AUTOCOMPLETER_DEST_PORT = "DestinationPort";
    String AUTOCOMPLETER_SSL_NAME = "SSLineName";
    String AUTOCOMPLETER_DESTIN = "Destin";
    String AUTOCOMPLETER_USRNAME = "RetAddUser";
    String AUTOCOMPLETER_TERMIN = "OTerm";
    String AUTOCOMPLETER_PORTNAME = "portName";
    String AUTOCOMPLETER_CITY = "City";
    String AUTOCOMPLETER_GENERICCODE = "GenericCode";
    String AUTOCOMPLETER_BILLIING_TERMINAL = "BillingTerminal";
    String AUTOCOMPLETER_DESTINATION = "Destination";
    String AUTOCOMPLETER_STATE = "State";
    String AUTOCOMPLETER_BL = "BL";
    String AUTOCOMPLETER_MASTER_NAME = "Master";
    //Constants for table
    String MODULE_TRANSACTION = "transaction";
    String MODULE_TRANSACTION_LEDGER = "transactionLedger";
    String MODULE_AR_BATCH = "arBatch";
    String MODULE_LINE_ITEM = "lineItem";
    //Constants for Errors and Exceptions
    String ERROR = "Error";
    String ERROR_LOGIN = "Login Error";
    String ERROR_MESSAGE = "errorMessage";
    //Constants for Message display
    String MESSAGE_LOGIN_ERROR = "Your Session got Expired. Please login again...";
    String MESSAGE_SUCCESS = "Success";
    String MESSAGE_AVAILABLE = "Available";
    String MESSAGE_LOCKED = "Locked";
    String MESSAGE_UNLOCKED = "Un Locked";
    //Constants for EDI
    String EDIPROPERTIES = "/com/gp/cong/struts/edi.properties";
    String EDITRACKINGPROPERTIES = "/com/gp/cong/struts/ediTracking.properties";
    String SUCCESS = "success";
    String FAILURE = "failure";
    String NOERROR = "No Error";
    String LOGIN_USER = "loginuser";
    String CODE_TYPE_LIST = "codeTypeList";
    //Bill_to party constants of BL
    String FORWARDER = "Forwarder";
    String SHIPPER = "Shipper";
    String THIRDPARTY = "ThirdParty";
    String AGENT = "Agent";
    //FclBlForm 
    String FCLBL_FORM = "fclBlForm";
    //DB column names of FclBl
    String FILENO = "file_No";
    String ORIGIN = "terminal";
    String DESTINATION = "Port";
    String POL = "Port_of_Loading";
    String POD = "PortofDischarge";
    String ETA = "eta";
    String ETD = "sail_date";
    String SSL = "ssline_name";
    String DISPUTED_LIST = "disputedList";
    String ACKNOWLEDGE = "acknowledge";
    String EQUAL = "=";
    String NOT_EQUAL = "<>";
    String GREATER_THAN = ">";
    String LESS_THAN = "<";
    String GREATER_THAN_EQUAL = ">=";
    String LESS_THAN_EQUAL = "<=";
    String DIVISION = "division";
    String TERMINAL = "terminal";
    String BL_TERM_BOTH = "B-Both";
    String BL_TERM_PREPAID = "P-Prepaid";
    String BL_TERM_COLLECT = "C-Collect";
    String BL_TERM_THIRD_PARTY = "T-3rd Party";
    String STATUS_FAILED = "Failed";
    String NS_INVOICE_HEADER = "NS Invoice Report";
    String ECONOCARIBE_RECEIVABLES_INVOICE = "Our invoice";
    String RECEIVABLES_AMOUNT = "AMOUNT";
    String ECONOCARIBE_PAYABLES_INVOICE = "Your invoice";
    String PAYABLES_AMOUNT = "AMOUNT";
    String RECEIVABLES_TOTAL = "Total";
    String PAYABLES_TOTAL = "Total";
    String ECONOCARIBE_RECEIVABLES = "Econocaribe's receivables:";
    String ECONOCARIBE_PAYABLES = "Econocaribe's payables:";
    String RECEIVABLES_DATE = "DATE";
    String RECEIVABLES_REF = "YOUR REF NO.";
    String NET_AMOUNT_OUR_FAVOUR = "NET AMOUNT IN OUR FAVOUR";
    String NET_AMOUNT_YOUR_FAVOUR = "NET AMOUNT IN YOUR FAVOUR";
    String SUBLEDGER_CODE_NETSETT = "NET SETT";
    String SCREEN_NAME_LCLBOOKINGRATES = "LCL Booking";
    //EDI Invoice status
    String STATUS_EDI_OPEN = "EDI Open";
    String STATUS_EDI_IN_PROGRESS = "EDI In Progress";
    String STATUS_EDI_ASSIGNED = "EDI Assigned";
    String STATUS_EDI_DISPUTE = "EDI Dispute";
    String STATUS_EDI_DUPLICATE = "EDI Duplicate";
    String STATUS_EDI_ARCHIVE = "EDI Archive";
    String STATUS_EDI_READY_TO_POST = "EDI Ready To Post";
    String STATUS_EDI_READY_TO_POST_FULLY_MAPPED = "EDI Ready To Post / Fully Mapped";
    String STATUS_EDI_POSTED_TO_AP = "EDI Posted To AP";
    //AR Batch status
    String STATUS_REVERSED = "Reversed";
    String PRE_PAYMENT = "PRE PAYMENT";
    String SUMMARY = "summary";
    String DETAIL = "detail";
    //JSP Modes
    String VIEW_MODE = "view";
    String PARTIAL_MODE = "partial";
    String FULL_MODE = "full";
    //FCL Constants
    String OFR_BLUESCREEN_CHARGECODE = "0001";
    String OFR = "OFR";
    //--These are used for glmapping
    String OFR_BLUESCREEN_CHARGEDESC = "OCEAN FREIGHT";
    String OFR_CHARGECODE = "OCNFRT";
    String TT_CHARGECODE = "TTREV";
    String FFCOMM_CHARGECODE = "FFCOMM";
    String PBA_CHARGECODE = "ADVSUR";
    String ADVSHP_CHARGECODE = "ADVSHP";
    String ADVFF_CHARGECODE = "ADVFF";
    //--These are used for cost calculation
    String UNITTYPE_CODES = "A,B,C,D,E,F";
    String ISSUE_TERMINAL_CODES = "08,09,17,15,19,38,59,61,63,73,18";
    String CHARGE_CODES = "139,149,159,250";
    String COST_CODES = "001,FFCOMM,009,012,022";
    //--These are used for coload rates
    String USER_SYSTEM = "system";
    String CTS_USER_SYSTEM = "CTSEDI";
    String DEFAULT_COLOAD_COMMODITY = "032500";
    String DEFAULT_PACKAGE_TYPE = "Package";
    //-- Message Constants
    String HAZARDOUS_MESSAGE = "HAZARDOUS CARGO";
    String DRAYAGE_MESSAGE = "(Local Drayage Included)";
    String RATE_CHANGE_MESSAGE = "Rate Change is applied";
    String NO_RATE_CHANGE_MESSAGE = "Rate change is not applied";
    //-- Charge Code
    String DRAYAGE_CHARGE_CODE = "DRAY";
    String INTERMODAL_CHARGE_CODE = "INTMDL";
    String INSURANCE_CHARGE_CODE = "INSURE";
    String INLAND_CHARGE_CODE = "INLAND";
    String DOCUMENT_CHARGE_CODE = "DOCUM";
    String BLUESCREEN_CHARGE_CODE = "005";
    String LABEL_PRINT = "Label Print";
    String UNKNOWN_DEST = "007UN";
    // -- Lcl predefined remarks
    Integer LCL_PREDEFINED_REMARKS_CODE = 80;
    //-- LCL Bluscreen ChargeCode Imports
    String LCL_IMP_BLUESCREEN_CHARGE_CODE = "1607,1617";
    //-- Company Code
    String COMPANY_CODE = "CompanyCode";
    // --LCLI predefined remarks
    String LCL_PREDEFINED_REMARKS_LCLI_CODE = "Pre-defined Remarks LCLI";
    
    //Brand for fcl
    String ECU_Worldwide = "Ecu Worldwide";
    //BillToCode for lcl
}
