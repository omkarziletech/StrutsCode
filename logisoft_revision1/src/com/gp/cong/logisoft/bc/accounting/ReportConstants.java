package com.gp.cong.logisoft.bc.accounting;

public interface ReportConstants {

    String REPORT_TYPE_SUMMARY = "Summary";
    String REPORT_TYPE_DETAIL = "Detail";
    String INCOMEGROUP = "Income Group 1";
    String INCOMEGROUP2 = "Income Group2";
    String REVENUEGROUP = "REVENUEGROUP";
    String EXPENSEGROUP1 = "Expense Group1";
    String EXPENSEGROUP2 = "Expense Group2";
    String EXPENSEGROUP3 = "Expense Group3";
    String EXPENSEGROUP4 = "Expense Group4";
    String EXPENSEGROUP5 = "Expense Group5";
    String INCOME_STATEMENT_CURRENT_PERIOD = "CurrentPeriod";
    String INCOME_STATEMENT_PREVIOUS_YEAR = "PreviousYear";
    String INCOME_STATEMENT_BUDGET_YTD = "BudgetYtd";
    String INCOME_STATEMENT_BUDGET_ANNUAL = "BudgetAnnual";
    String OTHEREXP = "Other Operating Expenses";
    String INTERESTPAID = "Interest Paid";
    String TAXES = "Taxes";
    String NONRECURRING = "Non-Recurring Events";
    String DIVIDEND = "Dividends to Stockholders";
    String TRIALBALANCE = "TrialBalance";
    String INCOMESTATEMENT = "IncomeStatement";
    String TRANSACTIONHISTORY = "TransactionHistory";
    //These For Fright Invoice Print
    String SHIPPER_OR_EXPORTER_FOR_FRIEGHT_INVOICE = "OTI CARGO INC AS AGENT FOR";
    String SHIPPER_OR_EXPORTER_ADDRESS_FOR_FRIEGHT_INVOICE = "2401 N.W.69 STREET" + "\n" + " MIAMI,FL 33147";
    String FRIEGHT_INVOICE_BILL_TO_NAME = "OTI CARGO INC";
    String FRIEGHT_INVOICE_BILL_TO_ADDRESS = "2401 N.W.69th Street Miami FL 33147";
    String FRIEGHT_INVOICE_BILL_TO_PHONE_NUMBER = "(866) 833-3755/(305)691 - 0022";
    String FRIEGHT_INVOICE_BILL_TO_FAX = "(305)694-3133(Accounts Receivable)";
    //THESE FOR AGING REPORTS
    String AGINGRPORT = "AgingReport";
    String CUSTOMERNO = "Customer No :";
    String ACOUNTNUMBER = "Account Number :";
    String CUSTOMERNAME = "Customer Name :";
    String CUSTOMERADDRESS = "Customer Address :";
    String SUBJET = "Subject :";
    String PHONE = "Phone No :";
    String FAX = "Fax :";
    String ADDRESS = "Address :";
    String TEXTAREA = "Message :";
    String INVOICENO = "Invoice/\nBL Number";
    String VOYAGENO = "Voyage No";
    String NEGATIVEBALANCE = "NEGATIVE BALANCE";
    String AGINGZEERO = "0";
    String AGINGTHIRTY = "30";
    String AGINGGREATERTHANTHIRTY = "31";
    String AGINGSIXTY = "60";
    String AGINGGREATERTHANSIXTY = "61";
    String AGINGNINTY = "90";
    String AGINGGREATERTHANNINTY = "90+";
    String AGINGGREATERTHANNINTYPLUS = "91+";
    String AGINGGMINIMUMAMOUNT = "0.00";
    String AGINGOVERDUE = "0";
    //Quotation Report
    String OTHER_CHARGES = "Per BL Charges";
    String INVOICEDATE = "Date";
    String AGING = "Age";
    String BALANCE = "Balance";
    String TOTALAMT = "Total Amount :";
    String ALLCUSTOMERS = "AllCustomer";
    String ALLCOLLECTOR = "AllCollector";
    String ONECOLLECTOR = "Collector";
    String SEARCHCUSTSAMPLE = "SearchCustomerSample";
    String BILLOFLADING = "BillOfLadingNo";
    String ARCUSTSTATEMENT = "ArCustomerStatement";
    String ARALLCOLLECTORSTATEMENT = "ArAllCollectorStatement";
    String TERMINAL = "Terminal :";
    String COMPANY = "Company :";
    String COLLECTOR = "Collector :";
    String AGERANGES = "Age Ranges";
    String CUSTOMERREFERENCE = "Customer\nReference";
    String INVOICEAMOUNT = "Invoice\nAmount";
    String PAYMENTORADDJUSTMENT = "Payment /\nAdjustment";
    String BOOKINGORQUOTENO = "Booking Number";
    String STATEMENTDATE = "Statement Date :";
    String TRANSACTIONTYPE = "AR";
    String TRANSACTIONTYPEAP = "AP";
    String TRANSACTIONTYPEAC = "AC";
    String PRIMARY = "on";
    String CUSTOMER = "CUSTOMER :";
    String TRANSACTIONSTATUS = "NET SETT";
    String ACCOUNTSUMMARY = "ACCOUNT SUMMARY";
    //THIS IS FOR JOURNAL ENTRY REPORT
    String BATCHDETAILS = "Batch Details";
    String BATCHNO = "Batch Number :";
    String DESCRIPTION = "Description  :";
    String DEBITTOTAL = "Debit Total  :";
    String CREDITTOTAL = "Credit Total :";
    String STATUS = "Status     :";
    String JOURNALENTRYID = "Journal Entry - ";
    String ENTRYNO = "Entry #:";
    String PERIOD = "Period :";
    String DATE = "Date:";
    String SOURCECODE = "Source Code :";
    String SOURCEDESCRIPTION = "Source Desc :";
    String DEBIT = "Debit :";
    String CREDIT = "Credit :";
    String MEMO = "Memo :";
    String LISTOFLINEITEMS = "List of Line Items";
    String ACCTNO = "Acct No :";
    String DEBITAMT = "Debit";
    String CREDITAMT = "Credit";
    String ACCTDESC = "Account Desc";
    String ITEMNO = "Item No";
    String REFERENCE = "Reference";
    String DESC = "Description";
    String CURRENCY = "Cur.";
    String COMMENT = "There are no line items for this Journal Entry";
    // This is for ARInvoice Report
    String ARINVOICEHEADING = "AR Invoice Report";
    String ARINVOICE = "AR Invoice";
    String ARINVOICECHARGES = "AR Invoice Charges";
    String CUSTOMERTYPE = "Customer Type :";
    String INVOICENUMBER = "Invoice No :";
    String BLDRNO = "BL/DR # :";
    String TERMS = "Terms :";
    String CONTACTNAME = "Contact Name :";
    String DUEDATE = "Due Date :";
    String CHARGECODE = "Charge Code";
    String GLACCOUNT = "GL Account";
    String QUANTITY = "Quantity";
    String AMOUNT = "Amount";
    String CONSIGNEE = "Consignee";
    // This is for Quotation Report
    String FCL_QUOTATION = "FCL_Quotation";
    String FCL_MultiQuote = "FCL_MultiQuote";
    //This is for AR Inquiry Report
    String AR_INQUIRY = "ARInquiry";
    String TYPE = "Type :";
    String EMAIL = "Email :";
    String MASTER_ACCOUNT = "Master Acct :";
    String CREDIT_LIMIT = "Credit Limit :";
    String OUTSTANDING_PAYABLES = "OutStanding Payables :";
    String OUTSTANDING_ACCRUALS = "OutStanding Accruals :";
    String NET_AMT_AR_AP = "Net Amt (AR-AP):";
    String NET_AMT_AR_AP_ACCR = "Net Amt (AR-AP-Accr):";
    String CURRENT = "Current :";
    String DAYS_3060 = "30-60 Days :";
    String DAYS_6090 = "60-90 Days :";
    String Gr90 = ">90 Days :";
    String TOTAL = "Total :";
    String INV_DATE_FROM = "Inv.Date From :";
    String INV_DATE_TO = "Inv.Date To:";
    String INVOICE_NUMBER = "Invoice Number:";
    String BILL_TO = "Bill To:";
    String SHOW_ALL_MEMBERS = "Show all Members :";
    String CHECK_NUMBER = "Check Number :";
    String CHECK_AMOUNT = "Check Amount :";
    String INVOICE_AMOUNT = "Invoice Amount :";
    String BL_TERMS = "BL Terms :";
    String DISPLAY_CREDIT_HOLD = "Display Credit Hold :";
    String SHOW_AR_ACCRUALS = "Show AR Accruals :";
    String SHOW_AP_ACCRUALS = "Show AP Accruals :";
    String SHOW_PAYABLES = "Show Payables :";
    String SHOW_PAID = "Show Paid :";
    String SHOW_MASTER_SUBSIDARY = "Show Master Subsidary :";
    String CHECK_DATE = "Check Date:";
    String CITY = "City:";
    String STATE = "State:";
    String COUNTRY = "Country:";
    String ZIP = "Zip:";
    String CREDITHOLD = "Credit Hold";
    String DEPOSITEDATE = "Deposite Date";
    String ADJ_AMOUNT = "Adj Amount";
    // TableHeading for AR Inquiry Transactions
    String PAYMENTS = " Payments";
    String PAYMENT_METHODS = " PAYMENT METHODS";
    //BOOKING CONSTANDS
    String WORKORDER = "WorkOrderReports";
    String REFERENCEREPORTS = "ReferenceReports";
    String COSTSHEET = "Cost Sheet";
    String ATTNNAME = "AttnName";
    String WORKORDERPHONE = "Phone";
    String WORKORDERFAX = "Fax";
    String SHIPPERNAME = "ShipperName";
    String SHIPPERADDRESS = "ShipperAddress";
    String SHIPPERPHONE = "Shipper Phone";
    String FORWARDERNAME = "ForwarderName";
    String FORWARDERADDRESS = "ForwarderAddress";
    String FORWARDERPHONE = "ForwarderPhone";
    String CONSIGNEENAME = "ConsigneeName";
    String CONSIGNEEADDRESS = "ConsigneeAddress";
    String CONSIGNEEPHONE = "ConsigneePhone";
    String TRUCKER = "Trucker";
    String BOOKINGNO = "Booking #";
    String LOADDATEORTIME = "Load Date/Time";
    String EQUIPMENT = "Equipment";
    String AMT = "Amount";
    String SSL = "SSL";
    String SSLNAME = "SS Line Name";
    String COMMODITY = "Commodity";
    String COMMODITYDESC = "Commodity Desc";
    String PUEQUIPMENT = "Pick-Up Equipment";
    String EARLIESTPU = "Earliest Pick-Up";
    String RETURN = "Return";
    String EARLIESTRETURN = "Earliest Return";
    String VESSEL = "Vessel";
    String DESTINATION = "Destination";
    String CUTOFF = "Cut Off";
    String RATEBREAKDOWN = "Rate BreakDown";
    String LOADINGADDRESS = "Loading Address";
    String RATE = "Rate";
    String CONTACT = "Contact";
    String INVOICE = "Invoice";
    // Booking Confirmation Report
    String BOOKING_CONFIRMATION = "Fcl_Booking_Confirmation";
    String BOOKING_COSTSHEET = "Fcl_Booking_Costsheet";
    // AR Invoice Report Constants
    Integer lINE_COUNT = 20;
    //FISCAL PERIOD REPORT CONSTANTS
    String FISCALREPORT = "Fiscal Period";
    //BUDGET REPORT CONSTANTS
    String BUDGETS = "Budgets";
    String BUDGETPERIOD = "Period";
    String BUDACCTNUM = "Account Number";
    String BUDYEAR = "Year";
    String BUDSET = "Set";
    String BUDCURRENCY = "Currency";
    //BL REPORT CONSTANTS
    String BILLOFLADINGFILENAME = "BillOfLading";
    String BILLOFLADINGHEADING = "BILL OF LADING";
    String SHIPPEROREXPORTER = "SHIPPER/EXPORTER (COMPLETE NAME AND ADDRESS)";
    String BOOKINGNUMBER = "BOOKING NO";
    String BILLOFLADINGNO = "BILL OF LADING NO";
    String EXPORTREFERENCE = "EXPORT REFERENCE";
    String BLCONSIGNEE = "CONSIGNEE (NOT NEGOTAIBLE UNLES CONSIGNED TO ORDER)";
    String BLFORWARDINGAGENTFMCNO = "FORWARDING AGENT FMC NO";
    String BLPOINTOFORIGIN = "POINT (STATE) OF ORIGIN";
    String BLNOTIFYPARTY = "NOTIFY PARTY (COMPLETE NAME AND ADDRESS)";
    String BLDELIVERY = "FOR DELIVERY PLEASE APPLY TO";
    String BLPRECARRIAGEBY = "PRE-CARRIAGE BY";
    String BLPORBYPREECARRIER = "PLACE OF RECEIPT BY PRE-CARRIER";
    String BLEXPORTCARRIAGE = "EXPORT CARRIAGE(VESSEL/VOYAGE/FLAG)";
    String BLPOL = "PORT OF LADING";
    String BLLOADINGPT = "LOADING PIER/TERMINAL";
    String BLPOD = "PORT OF DISCHARGE";
    String BLPODBYCARRIER = "PLACE OF DELIVERY BY CARRIER";
    String BLNUMBEROFORIGINALS = "NUMBER OF ORIGINALS";
    String BLPARTICULARSFURNISHEDBYSHIPPER = "PARTICULARS FURNISHED BY SHIPPER";
    String BLMARKSANDNUMBERS = "MARKS&NOS / CONTAINER NOS";
    String BLNOOFPACKAGES = "NO OF PKGS";
    String BLDESCRIPTIONOFPKG = "DESCRIPTION OF PACKAGES AND GOODS";
    String BLGORRWEIGHT = "GROSS WEIGHT";
    String BLMEASUREMENT = "MEASUREMENT";
    String ONWARD_INLAND_ROUTING = "SHIPPER LOAD,STOW,COUNT AND SEAL";
    String BLCOMMENT = "These commodities, technologies or software were exported from the US in accordance with the export administration regulations. Diversion contrary to US laws prohibited. Read Clause 6(4)b&c of Hereof concerning Extra Freight and Carriers Limitation of Liability.";
    String FREIGHTED_MASTERBL_REMARKS = "TRANSPORTATION PURSUANT TO THIS BILL OF LADING SUBJECT TO CONDITION SET FORTH IN ECONOCARIBE PUBLISHED TARIFF."
            + ".........................(THREE)BILL OF LADING,ALL OF THE SAME TENOR AND DATE,ONE OF WHICH BEING ACCOMPLISHED,THE OTHER TO STAND VOID.";
    String BLFREIGHTANDCHARGES = "FREIGHT & CHARGES";
    String BLBASIS = "BASIS";
    String BLRATE = "RATE";
    String BLPREPAID = "PREPAID";
    String BLCOLLECT = "COLLECT";
    String YES = "Yes";
    String NO = "No";
    String DOCKRECEIPTOPTION = "No";
    //Payment Report Constants
    String APPAYMENT = "ApPayment";
    String AGING_REPORT = "agingDetailReport";
    //For Aging Report
    String ACCT_NO = "Acct No";
    String ACCT_NAME = "Acct Name";
    String BL_DR_NO = "B/L-DR #";
    String INV_NO = "Inv #";
    String TRANSACTION_TYPE = "Type";
    String REF_NO = "Ref #";
    String CN_NO = "C/N #";
    String VOY_NO = "Voy #";
    String TOTAL_BALANCE = "TOTAL BALANCE";
    String EXCEL_DATE = "Date";
    String BLUESCREEN_ACCOUNT = "BlueScreen Account";
    //For FclBl report
    String OTICARGO = "O.T.I. CARGO";
    String NONNEGOTIABLE = "NON-NEGOTIABLE";
    String ORIGINAL = "ORIGINAL";
    String BILLOFLADINGFORREPORT = "BILL OF LADING";
    String FRIEGHTINVOICE = "FREIGHT INVOICE";
    String ARRIVALNOTICE = "ARRIVAL NOTICE";
    String UNMARKED_HOUSE_BILLOFLADDING = "Unmarked House Bill of Lading";
    String DOCK_RECEIPT = "DOCK RECEIPT";
}