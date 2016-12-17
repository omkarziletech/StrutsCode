package com.gp.cvst.logisoft;

public interface AccountingConstants {

    String YES = "yes";
    String NO = "no";
    String PeriodClosingBal = "Period Closing Bal=";
    String Reconciled = "Y";
    String Reconciled_N = "N";
    String CHARGE_CODE = "CHARGE CODE";
    String ON_ACCOUNT = "ON ACCOUNT";
    String TRANSACTION_TYPE_AR = "AR";
    String TRANSACTION_TYPE_AP = "AP";
    String TRANSACTION_TYPE_CR = "CR";
    String TRANSACTION_TYPE_AA = "AA";
    String CURRENCY_USD = "USD";
    String PRE_PAYMENT = "PRE PAYMENT";
    String INVOICE = "INVOICE";
    int SET_MAX_RECORDS_SIZE = 500;
    String PAYMENT_STATUS_CLOSED = "Closed";
    String AR_INQUIRY_PAGE = "AR Inquiry";
    String CHECK_PAYMENT = "Payment";
    String PAYMENT_TYPE_NETS = "NET SETT";
    String STATUS_PAID = "Paid";
    String STATUS_OPEN = "Open";
    String STATUS_ASSIGNED = "Assigned";
    String STATUS_CHARGECODE = "ChargeCode";
    String STATUS_CHARGECODEPOSTED = "ChargeCodePosted";
    String STATUS_POSTEDTOGL = "PostedtoGL";
    String TRANSACTION_TYPE_PY = "PY";
    String TRANSACTION_TYPE_AC = "AC";
    String SUBLEDGER_CODE_NETSETT = "NET SETT";
    String SUBLEDGER_CODE_AR_FCLE = "AR-FCLE";
    String SUBLEDGER_CODE_PJ = "PJ";
    String NET_SETTLEMENT_BATCH_N = "D";
    String NET_SETTLEMENT_BATCH_Y = "N";
    String AR_BATCH_TYPE_NETS = "N";//For NetSettlement Batch.
    String AR_BATCH_STATUS_OPEN = "Open";
    // Aging Report Screen
    String AGENTS = "agentsNotIncluded";
    String MIN_AMOUNT = "0.00";
    String DAYS_OVER_DUE = "0";
    /** Header Comments  **/
    String AR_APPLYPAYMENTS_CHECKSAVE = " Successfully Saved";
    String AR_APPLYPAYMENTS_CHECKUPDATE = " Successfully Updated";
    String AR_BATCH_SAVE = "Batch Successfully Saved with # ";
    /** Header Comments For AR Invoice */
    String SAVED_SUCESS = "Successfully Saved";
    String AR_BATCH_CLOSING_SUBLEDGER_CODE = "RCT";
    String AR_APPLYPAYMENTS_PAYMENT_TYPE_CHECK = "Check";
    String GL_FISCALPERIOD_STATUS_OPEN = "Open";
    String GL_FISCALPERIOD_STATUS_CLOSE = "Close";
    String DELIMITER = ":-";
    String DELIMITER_ROW = ":=";
    String DELIMITER_FOR_CUSTOMERNAME_NUMBER = "/#/";
    String INVOICE_STARTING_NUMBER = "1";
    String DEFAULT_GL_BATCHNO = "10000";
    String CODE_00 = "00";
    String CODE_01 = "01";
    /* code used to get GL ACCT Number to Insert a Record in TransLedger while Adj */
    String GL_MAPPING_REV = "R";
    String COMPANY_CODE = "CompanyCode";
    String ADJ = "Adj";
    String ADJ_CHECK_NO = "ADJ-SMALL";
    String ON = "on";
    String ALL_CUSTOMERS = "AllCustomers";
    /*AR Inquiry Form*/
    String SEARCH_DATE_BY = "invoiceDate";
    /*ArCredit Hold Select*/
    String AR_CREDIT_HOLD_SELECT = "00";
    /*	AP Reconcile */
    String UPLOAD_FILE_SUCCESS = "File Uploaded Successfully";
    int CSV_ROW_COUNT = 0;
    String SUBLEDGER_CODE_ACC = "ACC";
    String NORMAL_BALANCE_DEBIT = "Debit";
    String NORMAL_BALANCE_CREDIT = "Credit";
    String NEWARBATCH = "newArBatch";
    String COMPANY_NAME = "Econocaribe";
    String ALL = "All";
    String CREDIT_HOLD_CHECKED = "PlacedOnHold";
    String CREDIT_HOLD_UNCHECKED = "RemovedFromHold";
    String ADMIN = "Admin";
    String ACTION_INVOICE_CHARGE_LIST = "chargesList";
    String BATCH_POSTED_STATUS = "Closed";
    String ACTION_TRANSACTION = "Trans";
    String DEPOSIT = "DEPOSIT";
    String FISCAL_SET_BUDGET = "budget";
    String FISCAL_SET_ACTUAL = "actual";
    String FIXED_AMOUNT = "Fixed Amount";
    String SPREAD_AMOUNT = "Spread Amount";
    String BASE_AMOUNT_INCREASE = "Base,Amount Increase";
    String BASE_PERCENT_INCREASE = "Base,Percent Increase";
    //Credit Terms
    String DUE_UPON_RECEIPT = "DUE UPON RECEIPT";
    String NET_7_DAYS = "NET 7 DAYS";
    String NET_15_DAYS = "NET 15 DAYS";
    String NET_30_DAYS = "NET 30 DAYS";
    String NET_45_DAYS = "NET 45 DAYS";
    String NET_60_DAYS = "NET 60 DAYS";
    //Credit Status
    String NOCREDIT = "No Credit";
    String CREDITHOLD = "Credit Hold";
    String IN_GOOD_STANDING = "In Good Standing";
    String SUSPENDED_SEE_ACCOUNTING = "Suspended/See Accounting";
    String LEGAL_SEE_ACCOUNTING = "Legal/See Accounting";

    String AR_NET_SETT_BATCH="N";
    String AR_CASH_BATCH="D";
    String AR_BATCH_BOTH="Both";
}
