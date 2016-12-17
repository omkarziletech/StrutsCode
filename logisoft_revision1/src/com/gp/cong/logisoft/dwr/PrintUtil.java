package com.gp.cong.logisoft.dwr;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.BlCorrectionUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclPrintUtil;
import com.gp.cong.logisoft.bc.accounting.ARInvoiceBC;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.BookingConstants;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC;
import com.gp.cong.logisoft.bc.fcl.ManifestBC;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationReportBC;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.hibernate.dao.CreditDebitNoteDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.PrintConfigDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.lcl.report.LclArInvoicePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclCorrectionDebitCreditPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclImportBkgPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.lcl.report.LclVoyageNotificationPdfCreator;
import com.gp.cong.logisoft.reports.ArRedInvoicePdfCreator;
import com.gp.cong.logisoft.reports.CorrectionDebitORCreditNoteReportPdfCreator;
import com.gp.cong.logisoft.reports.FclBlCorrectionsReportPdfCreator;
import com.gp.cong.logisoft.reports.MultiQuotePdfCreator;
import com.gp.cong.logisoft.struts.form.PrintConfigForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import com.logiware.hibernate.domain.ArRedInvoice;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class PrintUtil implements LclReportConstants, PrintReportsConstants {

    private static final Logger log = Logger.getLogger(PrintUtil.class);
    PrintConfigBC printConfigBC = new PrintConfigBC();
    final String REQUEST_MAP = "requestMap";
    private static final String nl = System.getProperty("line.separator");

    public String printReport(String screenName, String documentName, String previewFlag,
            String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
        String reportLocation = LoadLogisoftProperties.getProperty("true".equalsIgnoreCase(previewFlag) ? "report.preview.location" : "reportLocation");
        String reportName = generatePdfReport(reportLocation, screenName, documentName, fromEmailAddress, fromName, request);
        String destination = requestMap.get("destination");
        if (CommonUtils.isNotEmpty(reportName) && !CommonConstants.CREDITDEBITNOTE.equals(screenName) && !CommonConstants.SCREEN_NAME_CORRECTIONS.equals(screenName)
                && !CommonConstants.SCREEN_NAME_BL.equalsIgnoreCase(screenName)) {
            String userName = "";
            if (session.getAttribute("loginuser") != null) {
                User user = (User) session.getAttribute("loginuser");
                userName = user.getLoginName();
            }
            if (reportName.contains(".pdf") || reportName.contains(".xlsx")) {
                File file = new File(reportName);
                reportName = reportName.replace(".pdf", "");
                if (CommonUtils.isNotEmpty(destination) && destination.indexOf("(") != -1 && destination.indexOf(")") != -1) {
                    destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
                    reportName += "_" + destination;
                }
                if (reportName.contains(".xlsx")) {
                    reportName = reportName.replace(".xlsx", "");
                    reportName += CommonUtils.isNotEmpty(userName) ? "_" + userName : "";
                    reportName += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".xlsx";
                } else {
                    reportName += CommonUtils.isNotEmpty(userName) ? "_" + userName : "";
                    reportName += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                }
                file.renameTo(new File(reportName));
            }
        }
        return reportName;
    }

    public void printReportClose(String value, String phone, String fax, String namefromTerminal, HttpServletRequest request) {
        HttpSession session = request.getSession();//remove
        String value1 = (String) ((null != session.getAttribute("valueFromTerminal") && !session.getAttribute("valueFromTerminal").equals("")) ? session.getAttribute("valueFromTerminal") : "");
        if (!value1.equalsIgnoreCase(value)) {
            session.removeAttribute("valueFromTerminal");
            session.setAttribute("valueFromTerminal", value);
        } else {
            session.setAttribute("valueFromTerminal", value1);
        }
        session.setAttribute("phoneFromTerminal", phone);
        session.setAttribute("faxFromTerminal", fax);
        session.setAttribute("nameTerminal", namefromTerminal);
    }

    public String generatePdfReport(String reportLocation, String screenName, String documentName,
            String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
        LclPrintUtil lclPrintUtil = new LclPrintUtil();
        if (CommonConstants.CREDITDEBITNOTE.equals(screenName)) {
            return debitCreditNotes(reportLocation, requestMap.get(CREDITDEBITNOTEPRINT), requestMap.get(NOTICE_NO),
                    requestMap.get(FILENO), documentName, request);
        }
        if (CommonConstants.SCREEN_NAME_QUOTATION.equals(screenName) && CommonConstants.DOCUMENT_NAME_QUOTATION.endsWith(documentName)) {
            return printQuotationReport(reportLocation, requestMap.get(QUOTATION_NO),
                    requestMap.get(QUOTATION_REMARKS), requestMap.get(FILENO),
                    requestMap.get(DESTINATION), fromEmailAddress, fromName, request);
        }
        if (CommonConstants.SCREEN_NAME_BOOKINGRATES.equals(screenName) && CommonConstants.DOCUMENT_NAME_BOOKINGRATES.endsWith(documentName)) {
            return printBookingReport(reportLocation, requestMap.get(BOOKING_ID),
                    requestMap.get("filesToPrint"), "", requestMap.get(FILENO),
                    requestMap.get(DESTINATION), documentName, fromEmailAddress, fromName, request);
        }
        if (CommonConstants.SCREEN_NAME_MultiQuote.equals(screenName) && CommonConstants.DOCUMENT_NAME_MultiQuote.endsWith(documentName)) {
            return printMultiQuoteReport(requestMap.get(QUOTATION_NO), requestMap.get(FILENO), request);
        }
        if (CommonConstants.SCREEN_NAME_BOOKINGRATES.equals(screenName) && CommonConstants.DOCUMENT_NAME_BOOKINGNONRATES.endsWith(documentName)) {
            return printBookingReport(reportLocation, requestMap.get(BOOKING_ID),
                    requestMap.get("filesToPrint"), REQUEST_PARAM, requestMap.get(FILENO),
                    requestMap.get(DESTINATION), documentName, fromEmailAddress, fromName, request);
        }
        if (CommonConstants.SCREEN_NAME_BOOKINGRATES.equals(screenName) && CommonConstants.DOCUMENT_NAME_BOOKINGCOVERSHEET.endsWith(documentName)) {
            return printBookingReport(reportLocation, requestMap.get(BOOKING_ID),
                    requestMap.get("filesToPrint"), REQUEST_PARAM, requestMap.get(FILENO),
                    requestMap.get(DESTINATION), documentName, fromEmailAddress, fromName, request);
        }
        if (CommonConstants.SCREEN_NAME_BOOKINGRATES.equals(screenName) && CommonConstants.DOCUMENT_NAME_PICKUPORDER.endsWith(documentName)) {
            return printBookingReport(reportLocation, requestMap.get(BOOKING_ID),
                    requestMap.get("filesToPrint"), REQUEST_PARAM, requestMap.get(FILENO),
                    requestMap.get(DESTINATION), documentName, fromEmailAddress, fromName, request);
        }
        // BL reports....................
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_UNFREIGHTED_ORIGINALBL.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    UNFREIGHTED_ORIGINAL_BL, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_UNFREIGHTED_NONNEGOTIABLE.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    UNFREIGHTED_NON_NEGOTIABLE, request);
        }
        /*if(CommonConstants.SCREEN_NAME_BL.equals(screenName)&& CommonConstants.DOCUMENT_NAME_UNFREIGHTED_MASTERBL.equals(documentName)){
         return printBLReports(requestMap.get(BL_ID),requestMap.get(FILENO),
         UNFREIGHTED_MASTER_BL);
         }*/
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHTED_ORIGINALBL.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHTED_ORIGINAL_BL, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHTED_NONNEGOTIABLE.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHTED_NON_NEGOTIABLE, request);
        }
        /*if(CommonConstants.SCREEN_NAME_BL.equals(screenName)&& CommonConstants.DOCUMENT_NAME_FREIGHTED_MASTERBL.equals(documentName)){
         return printBLReports(requestMap.get(BL_ID),requestMap.get(FILENO),
         FREIGHTED_MASTER_BL);
         }*/
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHT_INVOICE_AGENT, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHT_INVOICE_SHIPPER, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHT_INVOICE_FORWARDER, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHT_INVOICE_THIRDPARTY, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHT_INVOICE_CONSIGNEE, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FREIGHT_INVOICE_NOTIFYPARTY, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FCL_ARRIVAL_NOTICE, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE_NONRATED.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FCL_ARRIVALNOTICE_NON_RATED, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_CONFIRM_ONBOARD_NOTICE.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    FCL_CONFIRM_ONBOARD_NOTICE, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_STREAMSHIP_BL.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    STEAMSHIPBL, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_BL_MANIFESTED.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    MANIFESTBL, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.UNMARKED_HOUSE_BILLOFLADDING.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    UNMARKED_HOUSE_BILLOFLADDING, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_VGM_DECLARATION.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    VGM_DECLARATION, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.CONTAINER_RESPONSIBILITY_WAIVER.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    CONTAINER_RESPONSIBILITY_WAIVER, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.AUTHORITY_TO_MAKE_ENTRY.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    AUTHORITY_TO_MAKE_ENTRY, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DELIVERY_ORDER.equals(documentName)) {
            return printBLReports(reportLocation, requestMap.get(BL_ID), requestMap.get(FILENO),
                    DELIVERY_ORDER, request);
        }
        if (CommonConstants.SCREEN_NAME_BL.equals(screenName) && CommonConstants.DOCUMENT_NAME_AR_INVOICE.equals(documentName)) {
            return previewArInvoice(reportLocation, requestMap.get(AR_INVOICE), requestMap.get(AR_INVOICE_ID), request);
        }
        //correction reports................................
        if (CommonConstants.SCREEN_NAME_CORRECTIONS.equals(screenName) && CommonConstants.DOCUMENT_NAME_CORRECTIONS.equals(documentName)) {
            return printBlCorrectionReport(reportLocation, requestMap.get(BOL_NO), requestMap.get(NOTICE_NO),
                    requestMap.get(FILENO), request);
        }
        if (CommonConstants.LCL_CREDITDEBITNOTE.equals(screenName)) {
            return lclDebitCreditNotes(reportLocation, requestMap, documentName, request, lclPrintUtil);
        }
        if (CommonUtils.isEqual(screenName, SCREENNAME_QUOTEREPORT)) {
            return lclPrintUtil.printQuoteReport(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"), documentName, request);
        }
        if (CommonUtils.isEqual(screenName, SCREENNAME_BOOKINGREPORT)) {
            return lclPrintUtil.printBookingReport(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"), documentName, request);
        }
        if (CommonUtils.isEqual(screenName, SCREENNAME_BLREPORT) || CommonUtils.isEqual(screenName, "EditLCLBLPool")) {
            return lclPrintUtil.printReportBL(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"),
                    documentName, request);
        }
        if (CommonUtils.isEqual(screenName, SCREENNAME_LCLIMPUNITS) && CommonUtils.isEqual(documentName.trim(), DOCUMENTLCLCFSINSTRUCTIONS.trim())) {
            return lclPrintUtil.printBkgCFSReport(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"), documentName);
        }
        if (CommonUtils.isEqual(screenName, SCREENNAME_LCLIMPUNITS) && CommonUtils.isEqual(documentName, LCL_INSPECTION_CONTROL_FORM)) {
            return lclPrintUtil.printBkgCFSReport(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"), documentName);
        }
        if (CommonUtils.isEqual(screenName, "LclImpUnitReports") && CommonUtils.isEqual(documentName.trim(), PANDL.trim())) {
            return lclPrintUtil.printProfitAndLossReport(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"));
        }
        if (CommonUtils.isEqual(screenName, "LclImpUnitReports") && CommonUtils.isEqual(documentName, DOCUMENT_IMPORTEXCEPTIONALARMS)) {
            return lclPrintUtil.printImportsExceptionsAndAlarms(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"));
        }
        if (CommonUtils.isEqual(screenName, "LclImpUnitReports") && CommonUtils.isEqual(documentName, DOCUMENT_IMPORT_WAREHOUSE)) {
            return lclPrintUtil.printCfsWareHouseSheet(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"));
        }
        if (CommonUtils.isEqual(screenName, SCREENNAME_LCLEXPORTUNITREPORT) || CommonUtils.isEqual(screenName, SCREENNAME_LCLIMPUNITS)) {
            return lclPrintUtil.printExportUnitsReport(reportLocation, requestMap.get("fileId"), requestMap.get("fileNo"), documentName, request);
        }
        if (CommonUtils.isEqual(screenName, "LCLSSMaster")) {
            return lclPrintUtil.printExportSSReport(reportLocation, requestMap.get("fileId"), "", documentName, request);
        }
        if (CommonUtils.isEqual(screenName, SCREENNAMELCLIMPORTBOOKINGREPORT)) {
            return lclPrintUtil.printImportBkgReport(reportLocation, requestMap.get("fileId"),
                    requestMap.get("fileNo"), documentName, request);
        }
        return null;
    }

    private String printQuotationReport(String reportLocation, String quotationNo, String printRemarks,
            String fileNo, String destination, String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String filePath = reportLocation + "/Documents/" + ReportConstants.FCL_QUOTATION + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        MessageResources messageResources = CommonConstants.loadMessageResources();
        User user = (User) session.getAttribute("loginuser");
        String regionRemarks = new QuotationBC().fetchRegionRemarks(destination, null);
        String realPath = session.getServletContext().getRealPath("/");
        filePath += fileNo + ".pdf";
        new QuotationReportBC().createQuotationPDF(quotationNo, filePath, realPath, messageResources,
                user, printRemarks, regionRemarks, fromEmailAddress, fromName, request);
        return filePath;
    }

    private String printBookingReport(String reportLocation, String bookingId, String fileToPrint,
            String simpleRequest, String fileNo, String destination,
            String documentName, String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        BookingFclBC bookingFclBC = new BookingFclBC();
        BookingfclUnitsDAO bookingFclUnitsDAO = new BookingfclUnitsDAO();
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        HttpSession session = request.getSession();
        MessageResources messageResources = CommonConstants.loadMessageResources();
        if (CommonUtils.isEmpty(bookingId) && CommonUtils.isNotEmpty(fileNo)) {
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-") > -1 ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo : "0");
            if (null != bookingFcl) {
                bookingId = "" + bookingFcl.getBookingId();
                destination = bookingFcl.getPortofDischarge();
            }
        }
        if (bookingId != null && !bookingId.equals("")) {
            BookingFcl bookingFcl = bookingFclDAO.findById(Integer.parseInt(bookingId));
            FclBl fclBl = bookingFclBC.getBlByFileNo(bookingFcl.getFileNo());
            if (fclBl != null) {
                bookingFcl.setBlBy(fclBl.getBlBy());
                bookingFcl.setPortofOrgin(fclBl.getPortOfLoading());
                bookingFcl.setDestination(fclBl.getPortofDischarge());
                bookingFcl.setBlDate(fclBl.getBolDate());
                bookingFcl.setBlFlag("on");
                bookingFcl.setSSBookingNo(fclBl.getBookingNo());
            }
            request.setAttribute(BookingConstants.BOOKINGVALUES, bookingFcl);
            List fclRates = bookingFclUnitsDAO.getbookingfcl(bookingFcl.getBookingNumber());
            List otherChargesList = new ArrayList();
            List otherList = bookingFclUnitsDAO.getbookingfcl1(bookingFcl.getBookingNumber());

            List bookingfclUnitsList1 = new ArrayList();
            List perkglbsList = new ArrayList();
            boolean flag1 = false;
            boolean importFlag = "I".equalsIgnoreCase(bookingFcl.getImportFlag());
            for (int i = 0; i < otherList.size(); i++) {
                BookingfclUnits c1 = (BookingfclUnits) otherList.get(i);

                if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per1000kg")) || c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                    perkglbsList.add(c1);
                } else if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("perbl"))) {
                    otherChargesList.add(c1);
                }
            }
            List consolidaorList = bookingFclBC.consolidateRates(fclRates, messageResources, importFlag);
            request.setAttribute("consolidaorList", consolidaorList);
            request.setAttribute(BookingConstants.FCLRATES, fclRates);
            request.setAttribute(BookingConstants.OTHERCHARGESLIST, otherChargesList);
            request.setAttribute(BookingConstants.PERKGLBSLIST, perkglbsList);
        }

        //-----TO GET THE REGION REMARKS FROM GENERICCODE_DUP TABLE BASED ON DESTINATION -----
        String regionRemarks = new QuotationBC().fetchRegionRemarks(destination, "booking");
        //--ends--.
        String outputFileName = reportLocation + "/Documents/" + ReportConstants.BOOKING_CONFIRMATION + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String realPath = session.getServletContext().getRealPath("/");
        //--'R'is appended for the filename having Rates--------
        if (simpleRequest.equals("")) {
            outputFileName = outputFileName + fileNo + "R" + ".pdf";
        } else if ("Pickup Order".equals(documentName)) {
            outputFileName = outputFileName + "Pickup Order" + fileNo + ".pdf";
        } else if ("Booking Cover Sheet".equalsIgnoreCase(documentName)) {
            outputFileName = outputFileName + "CoverSheet" + fileNo + ".pdf";
        } else {
            outputFileName = outputFileName + fileNo + ".pdf";
        }
        bookingFclBC.createBookingConfirmationReport(bookingId, outputFileName, realPath, messageResources, simpleRequest,
                regionRemarks, fileToPrint, documentName, fromEmailAddress, fromName, request);
        return outputFileName;
    }

    private String printMultiQuoteReport(String quotationNo, String fileNo, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String filePath = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/"
                + ReportConstants.FCL_MultiQuote + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        MessageResources messageResources = CommonConstants.loadMessageResources();
        String realPath = session.getServletContext().getRealPath("/");
        filePath += fileNo + ".pdf";
        Quotation quotation = new QuotationDAO().getFileNoObject(fileNo);
        MultiQuotePdfCreator multiQuotePdfCreator = new MultiQuotePdfCreator(request, quotation);
        multiQuotePdfCreator.createReport(quotation, quotationNo, filePath, fileNo, realPath, messageResources, request);
        return filePath;
    }

    public String previewArInvoice(String reportLocation, String invoiceNumber, String invoiceId, HttpServletRequest request) throws Exception {
        String fileName = "";
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        String userName = user.getLoginName();
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            File dir = null;
            Date now = new Date();
            ArRedInvoice arRedInvoice = new ArRedInvoiceBC().getAPInvoice(invoiceId);
            if (arRedInvoice.getFileType() != null && arRedInvoice.getFileType().equalsIgnoreCase("LCLE")) {
                LclPrintUtil lclPrintUtil = new LclPrintUtil();
                return lclPrintUtil.lclArInvoiceReport(reportLocation, "", String.valueOf(arRedInvoice.getId()), "", "No", request);
            }
            String fileLocation = reportLocation + "/Documents";
            dir = new File(fileLocation);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String realPath = session.getServletContext().getRealPath("/");
            if (CommonUtils.isNotEmpty(arRedInvoice.getFileType()) && "LCLI".equalsIgnoreCase(arRedInvoice.getFileType())) {
                fileLocation += "/" + LclReportConstants.MODULENAME + "/Ar Invoice/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/";
                dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileName = fileLocation + "/Invoice_" + arRedInvoice.getInvoiceNumber();
                fileName += CommonUtils.isNotEmpty(userName) ? "_" + userName : "";
                fileName += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
                lclArInvoicePdfCreator.createReport(arRedInvoice, fileName, realPath, user, null, null, false);
            } else {
                MessageResources messageResources = CommonConstants.loadMessageResources();
                fileLocation += "/RedInvoice/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/";
                dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileName = fileLocation + "/Invoice_" + arRedInvoice.getInvoiceNumber();
                fileName += CommonUtils.isNotEmpty(userName) ? "_" + userName : "";
                fileName += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                ArRedInvoicePdfCreator arRedInvoicePdfCreator = new ArRedInvoicePdfCreator();
                arRedInvoicePdfCreator.createReport(arRedInvoice, fileName, realPath, messageResources, user);
            }
        }
        return fileName;
    }

    public String printBLReports(String reportLocation, String blId, String fileNo,
            String documentName, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        MessageResources messageResources = CommonConstants.loadMessageResources();
        Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
        String destination = requestMap.get("destination");
        FclBlBC fclBlBC = new FclBlBC();
        User user = new User();
        String userName = "";
        if (session.getAttribute("loginuser") != null) {
            user = (com.gp.cong.logisoft.domain.User) session.getAttribute("loginuser");
            if (null != user) {
                user = new UserDAO().findById(user.getUserId());
                userName = user.getLoginName();
            }
        }
        File file = new File(reportLocation + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        reportLocation = reportLocation + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/" + documentName + "_" + fileNo;
        if (CommonUtils.isNotEmpty(destination) && destination.indexOf("(") != -1 && destination.indexOf(")") != -1) {
            destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            reportLocation += "_" + destination;
        }
        reportLocation += CommonUtils.isNotEmpty(userName) ? "_" + userName : "";
        reportLocation += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
        String realPath = session.getServletContext().getRealPath("/");
        fclBlBC.createFclBillLadingReport(blId, reportLocation, realPath, messageResources, user, documentName);
        return reportLocation;
    }

    public String printBlCorrectionReport(String reportLocation, String blNo, String noticeNo, String fileNo,
            HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String realPath = session.getServletContext().getRealPath("/");
        MessageResources messageResources = CommonConstants.loadMessageResources();
        FclBlCorrectionsForm fclBlCorrectionsForm = new FclBlCorrectionsForm();
        fclBlCorrectionsForm.setBlNumber(blNo);
        fclBlCorrectionsForm.setNoticeNo(noticeNo);
        fclBlCorrectionsForm.setViewMode("view");
        FclBlCorrectionsBC fclBlCorrectionsBC = new FclBlCorrectionsBC();
        String outputFileName = reportLocation + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        outputFileName = outputFileName + "FCL-CN-" + blNo + "-" + noticeNo + ".pdf";
        FclBlCorrections fclBlCorrections = fclBlCorrectionsBC.editCorrectionRecord(fclBlCorrectionsForm, request, messageResources);
        FclBlCorrectionsReportPdfCreator fclBlCorrectionsReportPdfCreator = new FclBlCorrectionsReportPdfCreator();
        fclBlCorrectionsReportPdfCreator.createFclBlCorrectionsReport(fclBlCorrections, outputFileName, realPath, messageResources, request);
        return outputFileName;
    }

    public String debitCreditNotes(String reportLocation, String blNo, String noticeNo,
            String fileNo, String document, HttpServletRequest request) throws Exception {
        String customerNumber = null;
        String customerName = null;
        String debitOrCreditNote = null;
        if (document != null) {
            String stringArray[] = new CommonFunctions().splitString(document, DELIMETER);
            if (stringArray != null && stringArray.length > 2) {
                customerNumber = (null != stringArray[1]) ? stringArray[1].replace(")", "").toUpperCase() : stringArray[0];
                customerName = (null != stringArray[0]) ? stringArray[0].replace(")", "") : stringArray[0];
                debitOrCreditNote = (null != stringArray[2]) ? stringArray[2].replace(")", "").toUpperCase() : stringArray[0];

            }
        }
        MessageResources messageResources = CommonConstants.loadMessageResources();
        FclBlCorrectionsForm fclBlCorrectionsForm = new FclBlCorrectionsForm();
        fclBlCorrectionsForm.setBlNumber(blNo);
        fclBlCorrectionsForm.setNoticeNo(noticeNo);
        fclBlCorrectionsForm.setViewMode(null);
        FclBlCorrectionsBC fclBlCorrectionsBC = new FclBlCorrectionsBC();

        String outputFileName = reportLocation + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String realPath = request.getSession().getServletContext().getRealPath("/");
        outputFileName = outputFileName + "FCL-CN-" + blNo + "-" + noticeNo + "-" + debitOrCreditNote + ".pdf";//"("+customerNumber+")-"
        FclBlCorrections fclBlCorrections = fclBlCorrectionsBC.editCorrectionRecord(fclBlCorrectionsForm, request, messageResources);
        fclBlCorrections.setDebitOrCreditNote(debitOrCreditNote);
        fclBlCorrections.setToParty(customerName);
        fclBlCorrections.setToPartyNo(customerNumber);
        String noticeNumber = fclBlCorrections.getNoticeNo();
        ManifestBC manifestBC = new ManifestBC();
        request.setAttribute("transactionList", manifestBC.getListToDisplayOnCreditDebitReport(customerNumber, fclBlCorrections, noticeNumber, messageResources, debitOrCreditNote));
        CorrectionDebitORCreditNoteReportPdfCreator fCorrectionDebitORCreditNoteReportPdfCreator = new CorrectionDebitORCreditNoteReportPdfCreator();
        fCorrectionDebitORCreditNoteReportPdfCreator.createFclBlCorrectionsReport(fclBlCorrections, outputFileName, realPath, messageResources, request);
        return outputFileName;
    }

    public String lclDebitCreditNotes(String reportLocation, Map<String, String> requestMap, String document,
            HttpServletRequest request, LclPrintUtil lclPrintUtil) throws Exception {
        LclContact lclContact = null;
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
        LclUtils lclUtils = new LclUtils();
        String selectedMenu = requestMap.get(SELECTEDMENU);
        String fileId = requestMap.get(FILEID);
        String fileNo = requestMap.get(FILENO);
        Long longFileId = Long.parseLong(fileId);
        Long correctionId = Long.parseLong(requestMap.get(CORRECTIONID));
        String noticeNo = requestMap.get(NOTICE_NO);
        String customerNumber = StringUtils.substringBefore(StringUtils.substringAfter(document, "("), ")");
        String debitOrCreditNote = StringUtils.substringBeforeLast(StringUtils.substringAfterLast(document, "("), ")");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        File file = new File(reportLocation + "/" + LCL_IMPORTS_BKG);
        if (!file.exists()) {
            file.mkdir();
        }
        String realPath = session.getServletContext().getRealPath("/");
        LCLCorrectionNoticeBean lclCorrectionNoticeBean = lclCorrectionDAO.getAllCorrectionByFileIdReports(fileId, correctionId, selectedMenu);
        lclCorrectionNoticeBean.setDebitOrCreditNote(debitOrCreditNote);
        lclCorrectionNoticeBean.setCorrectionNo(noticeNo);
        String folderName = "";
        CustomerAddress customerAddress = null;
        StringBuilder addressDetails = new StringBuilder();
        String[] documentData = null;
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getLclChargeByFileNumberAsc(longFileId);
            LclBookingAc lclBookingAc = lclBookingAcList.get(0);
            if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("C")) {
                customerAddress = lclBookingAc.getLclFileNumber().getLclBooking().getConsAcct().getCustAddr();
            } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("A")) {
                customerAddress = lclBookingAc.getLclFileNumber().getLclBooking().getAgentAcct().getCustAddr();
            } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("N")) {
                customerAddress = lclBookingAc.getLclFileNumber().getLclBooking().getNotyAcct().getCustAddr();
            } else {
                customerAddress = lclBookingAc.getLclFileNumber().getLclBooking().getThirdPartyAcct().getCustAddr();
            }
            lclCorrectionNoticeBean.setBillToPartyAddress(lclUtils.getConcatenatedLclImportsContactAddress(customerAddress));
            request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionChargesPdf(correctionId));
            folderName = LCL_IMPORTS_BKG;
        } else {
            documentData = document.split("#");
            debitOrCreditNote = StringUtils.substringBeforeLast(StringUtils.substringAfterLast(documentData[0], "("), ")");
            customerNumber = StringUtils.substringBefore(StringUtils.substringAfter(documentData[0], "("), ")");
            lclCorrectionNoticeBean = lclCorrectionDAO.getExportCorrectionFileds(correctionId, Long.parseLong(fileId));
            lclCorrectionNoticeBean = lclCorrectionNoticeBean == null ? new LCLCorrectionNoticeBean() : lclCorrectionNoticeBean;
            lclCorrectionNoticeBean.setDebitOrCreditNote(debitOrCreditNote);
            lclCorrectionNoticeBean.setCorrectionNo(noticeNo);

            CustAddress cust = new CustAddressDAO().findByAccountNo(customerNumber);
            if (cust != null) {
                String con_address = CommonUtils.isNotEmpty(cust.getAddress1()) ? cust.getAddress1() + "\n" : "";
                con_address += CommonUtils.isNotEmpty(cust.getCity()) ? cust.getCity() : "";
                con_address += CommonUtils.isNotEmpty(cust.getState()) && CommonUtils.isNotEmpty(cust.getCity()) ? ","
                        + cust.getState() : CommonUtils.isNotEmpty(cust.getState()) ? "" + cust.getState() : "";
                con_address += CommonUtils.isNotEmpty(cust.getCountry()) ? "," + cust.getCountry() + "\n" : "\n";
                con_address += CommonUtils.isNotEmpty(cust.getZip()) ? cust.getZip() : "";
                con_address += CommonUtils.isNotEmpty(cust.getPhone()) ? " " + "PHONE" + cust.getPhone() : "";
                lclCorrectionNoticeBean.setBillToPartyAddress(con_address);
                lclCorrectionNoticeBean.setCustomerAcctNo(customerNumber);
                lclCorrectionNoticeBean.setCustomer(customerNumber);
            }
            request.setAttribute("lclCorrectionChargesList", new BlCorrectionUtils()
                    .getFormattedCorrectionExportcharges(new Integer(documentData[1])));
            folderName = LCL_BOOKING;
        }
        if (customerAccountingDAO.isCreditDebitNote(customerNumber)) {
            reportLocation = reportLocation + "/" + folderName + "/";
            File F = new File(reportLocation);
            if (!F.exists()) {
                F.mkdirs();
            }
            reportLocation += "LCL-CN-" + requestMap.get(CREDITDEBITNOTEPRINT) + "-" + noticeNo + "-"
                    + debitOrCreditNote + ".pdf";//"("+customerNumber+")-"
            LclCorrectionDebitCreditPdfCreator lclCorrectionDebitORCreditNoteReportPdfCreator = new LclCorrectionDebitCreditPdfCreator();
            lclCorrectionDebitORCreditNoteReportPdfCreator.createReport(lclCorrectionNoticeBean, reportLocation, realPath, request, longFileId, selectedMenu);
        } else {
            if (selectedMenu.equalsIgnoreCase("Imports")) {
                reportLocation = reportLocation + "/" + LCL_IMPORTS_BKG + "/" + fileNo + DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                LclImportBkgPdfCreator lclImportBkgPdfCreator = new LclImportBkgPdfCreator();
                lclImportBkgPdfCreator.createPdf(realPath, null, fileId, fileNo, reportLocation, DOCUMENTLCLIMPORTSARRIVALNOTICE, null, user);
            } else {
                LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
                file = new File(reportLocation + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "Bill Of Lading" + "/"
                        + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                CreditDebitNote creditDebitNote = new CreditDebitNoteDAO().findById(new Integer(documentData[1]));
                lclBLPdfCreator.setPrintdocumentName("Freight Invoice");
                lclBLPdfCreator.setExportBilltoParty(creditDebitNote.getBillToParty());
                reportLocation = reportLocation + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                        + "Bill Of Lading" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/" + "BillofLading_" + fileNo + ".pdf";;
                lclBLPdfCreator.createReport(realPath, reportLocation, "Freight Invoice", fileId, "", creditDebitNote.getCorrectionNumber(), true);
            }
        }
        return reportLocation;
    }

    public boolean savePrintConfiguration(PrintConfigForm printConfigForm, HttpServletRequest request) throws Exception {
        if (null != printConfigForm.getScreenName() && printConfigForm.getScreenName().startsWith("LCL")) {
            printConfigForm.setEmailMessage(this.appendLclEmailBody(printConfigForm.getToName(),
                    printConfigForm.getNameFromTerminal(), printConfigForm.getTerminalFax(),                     
                    printConfigForm.getTerminalPhone(), printConfigForm.getComment(), printConfigForm.getBrand(), request));
        }
        HttpSession session = request.getSession();
        ARInvoiceBC aRInvoiceBC = new ARInvoiceBC();
        String realPath = session.getServletContext().getRealPath("/");
        User user = (User) session.getAttribute("loginuser");
        String filesToPrint = (null != request.getParameter("filesToPrint") && !request.getParameter("filesToPrint").equals("")) ? request.getParameter("filesToPrint") : "";
        String changeInTerminalEmail = "";
        String fromAddress = "";
        String fileNo = "";
        fileNo = printConfigForm.getFileNumber();
        fileNo = null != fileNo ? fileNo.substring(-1 != fileNo.indexOf("-") ? fileNo.indexOf("-") + 1 : 0) : "";
        if (session.getAttribute("changeInTerminalEmail") != null) {
            changeInTerminalEmail = (String) session.getAttribute("changeInTerminalEmail");
        }
        if (filesToPrint.equalsIgnoreCase("confirmOnBoard")) {
            fromAddress = new RefTerminalDAO().getFclDocumentDeptEmail(fileNo);
        } else if (CommonUtils.isNotEmpty(changeInTerminalEmail)) {
            fromAddress = changeInTerminalEmail;
        } //        else if (CommonUtils.isNotEmpty(printConfigForm.getFromEmailAddress())) {
        //            fromAddress = printConfigForm.getFromEmailAddress();
        //        } 
        else {
            fromAddress = user.getEmail();
        }
        fromAddress = CommonUtils.isNotEmpty(fromAddress) ? fromAddress : user.getEmail();
        String configId = null != printConfigForm.getId() ? printConfigForm.getId().toString() : "";
        String fromName = "";
        if (filesToPrint.equalsIgnoreCase("confirmOnBoard")) {
            fromName = fromAddress;
        } //        else if (CommonUtils.isNotEmpty(printConfigForm.getNameFromTerminal())) {
        //            fromName = printConfigForm.getNameFromTerminal();
        //        } 
        else {
            fromName = user.getFirstName();
        }
        String fromBkgEmailAddress = printConfigForm.getFromEmailAddress();
        String fromBkgEmailName = printConfigForm.getNameFromTerminal();

        boolean emailAndEmailSp = false;
        String importflagForExport = null != printConfigForm.getImportflagForExport() ? printConfigForm.getImportflagForExport() : "";
        String moduleForExport = null != printConfigForm.getModuleForExport() ? printConfigForm.getModuleForExport() : "";
        String screenNameForExport = null != printConfigForm.getScreenNameForExport() ? printConfigForm.getScreenNameForExport() : "";

        if (importflagForExport.equals("false") && moduleForExport.equals("FCL") && (screenNameForExport.equals("Quotation") || screenNameForExport.equals("BL") || screenNameForExport.equals("Booking"))) {
            printConfigForm.setFileLocation("");
            if (CommonUtils.isEmpty(printConfigForm.getFileLocation())) {
                String fileLocation = printReport(printConfigForm.getScreenName(), printConfigForm.getDocumentName(),
                        "false", fromBkgEmailAddress, fromBkgEmailName, request);
                printConfigForm.setFileLocation(fileLocation);
            }
        } else {
            if (CommonUtils.isEmpty(printConfigForm.getFileLocation())) {
                String fileLocation = printReport(printConfigForm.getScreenName(), printConfigForm.getDocumentName(), "false",
                        fromBkgEmailAddress, fromBkgEmailName, request);
                printConfigForm.setFileLocation(fileLocation);
            } else if (CommonUtils.isNotEmpty(printConfigForm.getFileLocation())) {
                File file = new File(printConfigForm.getFileLocation());
                if (!file.exists()) {
                    String fileLocation = printReport(printConfigForm.getScreenName(), printConfigForm.getDocumentName(),
                            "false", fromBkgEmailAddress, fromBkgEmailName, request);
                    printConfigForm.setFileLocation(fileLocation);
                }
            }
        }
        if (printConfigForm.isEmail() && printConfigForm.isEmailSp()) {
            emailAndEmailSp = true;
            String ccEmailId = "";
            String fromAddressForcover = "";
            String fromNameForcover = "";
            if (printConfigForm.isEmailCoverPageOk()) {
                ccEmailId = printConfigForm.getCcAddress();
            } else {
                if ("LCLQuotation".equalsIgnoreCase(printConfigForm.getScreenName())) {
                    Long fileId = new LclFileNumberDAO().getFileIdByFileNumber(fileNo);
                    GenericCode salesPerson = new EmailschedulerDAO().getSalesPersonDetails(fileId);
                    if (null != salesPerson) {
                        ccEmailId = salesPerson.getField3();
                    }
                } else {
                    ccEmailId = new EmailschedulerDAO().getSalesPersonMailId(fileNo, importflagForExport);
                }
            }
            fromName = filesToPrint.equalsIgnoreCase("confirmOnBoard") ? fromAddress : user.getFirstName();
            if (CommonUtils.isNotEmpty(printConfigForm.getFromEmailAddress())) {
                fromAddressForcover = printConfigForm.getFromEmailAddress();
            } else {
                fromAddressForcover = fromAddress;
            }
            if (CommonUtils.isNotEmpty(printConfigForm.getNameFromTerminal())) {
                fromNameForcover = printConfigForm.getNameFromTerminal();
            } else {
                fromNameForcover = fromName;
            }
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(user.getLoginName(), printConfigForm.getToAddress(), fromNameForcover,
                    fromAddressForcover, printConfigForm.getCcAddress(), printConfigForm.getBccAddress(), printConfigForm.getEmailSubject(), printConfigForm.getEmailMessage());
            emailSchedulerVO.setTextMessage(printConfigForm.getEmailMessage());
            emailSchedulerVO.setHtmlMessage(printConfigForm.getEmailMessage());
            String fileLocation = null != printConfigForm.getFileLocation() ? printConfigForm.getFileLocation() : "";
            emailSchedulerVO.setEmailInfo(printConfigForm.getScreenName(), fileLocation, CommonConstants.CONTACT_MODE_EMAIL,
                    0, new Date(), printConfigForm.getScreenName(), configId, user.getLoginName());
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            emailSchedulerVO.setSubject(printConfigForm.getEmailSubject());
            emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");
            if (null != printConfigForm.getFromEmailAddress() && !printConfigForm.getFromEmailAddress().equals("")) {
                emailSchedulerVO.setFromAddress(printConfigForm.getFromEmailAddress());
            }
            if (null != printConfigForm.getNameFromTerminal() && !printConfigForm.getNameFromTerminal().equals("")) {
                emailSchedulerVO.setFromName(printConfigForm.getNameFromTerminal());
            }
            aRInvoiceBC.save(emailSchedulerVO);
            log.info("---------------------------Email Delivery Report----------------------------");
            log.info("Email Sent Successfully !!!!!");
            log.info("Email Sent By User : " + emailSchedulerVO.getUserName());
            log.info("FromAddress : " + emailSchedulerVO.getFromAddress());
            log.info("ToAddress : " + emailSchedulerVO.getToAddress());
            log.info("CcAddress : " + emailSchedulerVO.getCcAddress());
            log.info("BccAddress : " + emailSchedulerVO.getBccAddress());
            log.info("Email Subject : " + emailSchedulerVO.getSubject());
            log.info("Email Sent Date : " + emailSchedulerVO.getEmailDate());
            log.info("ModuleId : " + emailSchedulerVO.getModuleId());
            log.info("Current Email Status : " + emailSchedulerVO.getStatus());
        }
        if (printConfigForm.isEmail() && !emailAndEmailSp) {
            fromName = filesToPrint.equalsIgnoreCase("confirmOnBoard") ? fromAddress : user.getFirstName();
            String fromAddressForcover = "";
            String fromNameForcover = "";
            if (CommonUtils.isNotEmpty(printConfigForm.getFromEmailAddress())) {
                fromAddressForcover = printConfigForm.getFromEmailAddress();
            } else {
                fromAddressForcover = fromAddress;
            }
            if (CommonUtils.isNotEmpty(printConfigForm.getNameFromTerminal())) {
                fromNameForcover = printConfigForm.getNameFromTerminal();
            } else {
                fromNameForcover = fromName;
            }
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(user.getLoginName(), printConfigForm.getToAddress(), fromNameForcover,
                    fromAddressForcover, printConfigForm.getCcAddress(), printConfigForm.getBccAddress(), printConfigForm.getEmailSubject(), printConfigForm.getEmailMessage());
            emailSchedulerVO.setTextMessage(printConfigForm.getEmailMessage());
            emailSchedulerVO.setHtmlMessage(printConfigForm.getEmailMessage());

            String fileLocation = null != printConfigForm.getFileLocation() ? printConfigForm.getFileLocation() : "";
            emailSchedulerVO.setEmailInfo(printConfigForm.getScreenName(), fileLocation, CommonConstants.CONTACT_MODE_EMAIL,
                    0, new Date(), printConfigForm.getScreenName(), configId, user.getLoginName());
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            emailSchedulerVO.setSubject(printConfigForm.getEmailSubject());
            emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");
            if (!printConfigForm.getContactId().equals("")) {
                emailSchedulerVO.setContactId(printConfigForm.getContactId());
            }
            if (!printConfigForm.getAccountNo().equals("")) {
                emailSchedulerVO.setAccountNo(printConfigForm.getAccountNo());
            }
            if (null != printConfigForm.getFromEmailAddress() && !printConfigForm.getFromEmailAddress().equals("")) {
                emailSchedulerVO.setFromAddress(printConfigForm.getFromEmailAddress());
            }
            if (null != printConfigForm.getNameFromTerminal() && !printConfigForm.getNameFromTerminal().equals("")) {
                emailSchedulerVO.setFromName(printConfigForm.getNameFromTerminal());
            }
            aRInvoiceBC.save(emailSchedulerVO);
            log.info("---------------------------Email Delivery Report----------------------------");
            log.info("Email Sent Successfully !!!!!");
            log.info("Email Sent By User : " + emailSchedulerVO.getUserName());
            log.info("FromAddress : " + emailSchedulerVO.getFromAddress());
            log.info("ToAddress : " + emailSchedulerVO.getToAddress());
            log.info("CcAddress : " + emailSchedulerVO.getCcAddress());
            log.info("BccAddress : " + emailSchedulerVO.getBccAddress());
            log.info("Email Subject : " + emailSchedulerVO.getSubject());
            log.info("Email Sent Date : " + emailSchedulerVO.getEmailDate());
            log.info("ModuleId : " + emailSchedulerVO.getModuleId());
            log.info("Current Email Status : " + emailSchedulerVO.getStatus());
        }
        if (printConfigForm.isEmailSp() && !emailAndEmailSp) {
            String toAddress = "";
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setToName(user.getFirstName());
            if ("LCLQuotation".equalsIgnoreCase(printConfigForm.getScreenName())) {
                Long fileId = new LclFileNumberDAO().getFileIdByFileNumber(fileNo);
                GenericCode salesPerson = new EmailschedulerDAO().getSalesPersonDetails(fileId);
                if (null != salesPerson) {
                    toAddress = salesPerson.getField3();
                }
            } else {
                toAddress = new EmailschedulerDAO().getSalesPersonMailId(fileNo, importflagForExport);
            }
            emailSchedulerVO.setToAddress(toAddress);
            emailSchedulerVO.setFromName(user.getLoginName());
            emailSchedulerVO.setFromAddress(fromAddress);
            emailSchedulerVO.setModuleName(printConfigForm.getScreenName());
            emailSchedulerVO.setName(printConfigForm.getScreenName());
            emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_EMAIL);
            emailSchedulerVO.setFileLocation(printConfigForm.getFileLocation());
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            emailSchedulerVO.setModuleId(null != configId ? configId.substring(-1 != configId.indexOf("-") ? configId.indexOf("-") + 1 : 0) : "");
            emailSchedulerVO.setSubject(printConfigForm.getEmailSubject());
            emailSchedulerVO.setTextMessage(printConfigForm.getEmailMessage());

            emailSchedulerVO.setHtmlMessage("Please Find the attachment");
            emailSchedulerVO.setUserName(user.getLoginName());
            emailSchedulerVO.setEmailDate(new Date());
            emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");

            aRInvoiceBC.save(emailSchedulerVO);
            log.info("---------------------------Email Sending Status----------------------------");
            log.info("Email Sent Successfully !!!!!");
            log.info("Email Sent By User : " + emailSchedulerVO.getUserName());
            log.info("FromAddress : " + emailSchedulerVO.getFromAddress());
            log.info("ToAddress : " + toAddress);
            log.info("CcAddress : " + emailSchedulerVO.getCcAddress());
            log.info("BccAddress : " + emailSchedulerVO.getBccAddress());
            log.info("Email Subject : " + emailSchedulerVO.getSubject());
            log.info("Email Sent Date : " + emailSchedulerVO.getEmailDate());
            log.info("ModuleId : " + emailSchedulerVO.getModuleId());
            log.info("Current Email Status : " + emailSchedulerVO.getStatus());
        }

        if (printConfigForm.isPrint()) {
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            if ("LCLBooking".equalsIgnoreCase(printConfigForm.getScreenName()) && "Barrel D/R".equalsIgnoreCase(printConfigForm.getDocumentName())) {
                emailSchedulerVO.setName(printConfigForm.getDocumentName());
            } else {
                emailSchedulerVO.setName(printConfigForm.getScreenName());
            }
            emailSchedulerVO.setModuleName(printConfigForm.getScreenName());
            if (CommonConstants.CONTACT_MODE_LABEL_PRINT.equalsIgnoreCase(printConfigForm.getDocumentName())) {
                emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_LABEL_PRINT);
            } else {
                emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_PRINT);
            }
            emailSchedulerVO.setFileLocation(printConfigForm.getFileLocation());
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            emailSchedulerVO.setModuleId(null != configId ? configId.substring(-1 != configId.indexOf("-") ? configId.indexOf("-") + 1 : 0) : "");
            emailSchedulerVO.setTextMessage(printConfigForm.getEmailMessage());
            emailSchedulerVO.setHtmlMessage(printConfigForm.getEmailMessage());
            emailSchedulerVO.setSubject(printConfigForm.getEmailSubject());
            emailSchedulerVO.setUserName(user.getLoginName());
            emailSchedulerVO.setEmailDate(new Date());
            emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");
            emailSchedulerVO.setPrinterName(printConfigForm.getPrinterName() != null ? printConfigForm.getPrinterName() : "");
            if ("LCLBL".equalsIgnoreCase(printConfigForm.getScreenName()) && "Bill of Lading (Original)".equalsIgnoreCase(printConfigForm.getDocumentName())) {
                emailSchedulerVO.setPrinterTray(printConfigForm.getPrinterTray() != null ? printConfigForm.getPrinterTray() : "");
            }
            emailSchedulerVO.setPrintCopy(null != printConfigForm.getPrintCopy() ? printConfigForm.getPrintCopy() : 1);
            if(CommonUtils.isNotEmpty(printConfigForm.getId())){
                emailSchedulerVO.setModuleReferenceId(printConfigForm.getId());
            }
            aRInvoiceBC.save(emailSchedulerVO);

        }

        if (printConfigForm.isFax()) {
            String[] toFaxNumbers = StringUtils.split(printConfigForm.getToFaxNumber(), ",");
            for (String toFaxNumber : toFaxNumbers) {
                EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                String coverLetter = printConfigBC.createFaxCoverLetter(toFaxNumber, printConfigForm, realPath, user.getLoginName());
                emailSchedulerVO.setName(printConfigForm.getScreenName());
                emailSchedulerVO.setModuleName(printConfigForm.getScreenName());
                emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_FAX);
                emailSchedulerVO.setCoverLetter(coverLetter);
                emailSchedulerVO.setToAddress(toFaxNumber);
                emailSchedulerVO.setToName(printConfigForm.getToName());
                emailSchedulerVO.setFromAddress(printConfigForm.getFromFaxNumber());
                emailSchedulerVO.setFromName(printConfigForm.getFromName());
                emailSchedulerVO.setFileLocation(printConfigForm.getFileLocation());
                emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                emailSchedulerVO.setModuleId(null != configId ? configId.substring(-1 != configId.indexOf("-") ? configId.indexOf("-") + 1 : 0) : "");
                emailSchedulerVO.setHtmlMessage("");
                emailSchedulerVO.setSubject(printConfigForm.getSubject());
                if (null != printConfigForm.getScreenName() && printConfigForm.getScreenName().length() > 2
                        && printConfigForm.getScreenName().substring(0, 3).equalsIgnoreCase("LCL")) {
                    PrintConfigDAO printConfigDAO = new PrintConfigDAO();
                    StringBuilder sb = new StringBuilder();
                    String faxNo = printConfigForm.getToFaxNumber();
                    faxNo = printConfigDAO.getCompanyName(faxNo);
                    emailSchedulerVO.setCompanyName(faxNo);
                    sb.append(printConfigForm.getDocumentName()).append(" ").append("DR#").append(printConfigForm.getFileNumber()).append(nl).append(printConfigForm.getMessage());
                    printConfigForm.setMessage(sb.toString());
                }
                emailSchedulerVO.setTextMessage(printConfigForm.getMessage());
                emailSchedulerVO.setUserName(user.getLoginName());
                emailSchedulerVO.setEmailDate(new Date());
                emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");
                if (!printConfigForm.getContactId().equals("")) {
                    emailSchedulerVO.setContactId(printConfigForm.getContactId());
                }
                if (!printConfigForm.getAccountNo().equals("")) {
                    emailSchedulerVO.setAccountNo(printConfigForm.getAccountNo());
                }

                aRInvoiceBC.save(emailSchedulerVO);
                log.info("---------------------------Fax Delivery Report----------------------------");
                log.info("Fax Sent Successfully !!!!!");
                log.info("Fax Sent By User : " + emailSchedulerVO.getUserName());
                log.info("FromName : " + emailSchedulerVO.getFromName());
                log.info("FromAddress : " + emailSchedulerVO.getFromAddress());
                log.info("ToName : " + emailSchedulerVO.getToName());
                log.info("ToAddress : " + emailSchedulerVO.getToAddress());
                log.info("Fax Subject : " + emailSchedulerVO.getSubject());
                log.info("Fax Sent Date : " + emailSchedulerVO.getEmailDate());
                log.info("ModuleId : " + emailSchedulerVO.getModuleId());
                log.info("Current Email Status : " + emailSchedulerVO.getStatus());
            }
        }

        if (printConfigForm.isEmailMe()) {
            fromName = filesToPrint.equalsIgnoreCase("confirmOnBoard") ? fromAddress : user.getFirstName();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setToName(user.getFirstName());
            emailSchedulerVO.setToAddress(user.getEmail());
            emailSchedulerVO.setFromName(fromName);
            emailSchedulerVO.setFromAddress(fromAddress);
            emailSchedulerVO.setModuleName(printConfigForm.getScreenName());
            emailSchedulerVO.setName(printConfigForm.getScreenName());
            emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_EMAIL);
            emailSchedulerVO.setFileLocation(printConfigForm.getFileLocation());
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            emailSchedulerVO.setModuleId(null != configId ? configId.substring(-1 != configId.indexOf("-") ? configId.indexOf("-") + 1 : 0) : "");
            emailSchedulerVO.setSubject(printConfigForm.getEmailSubject());
            printConfigForm.setEmailMessage(this.appendLclEmailBody(printConfigForm.getToName(),
                    printConfigForm.getFromName(), printConfigForm.getUserTerminalFaxNo(),                     
                    printConfigForm.getUserTerminalPhoneNo(), printConfigForm.getComment(), printConfigForm.getBrand(), request));
            emailSchedulerVO.setTextMessage(printConfigForm.getEmailMessage());
            emailSchedulerVO.setHtmlMessage(printConfigForm.getEmailMessage());

            emailSchedulerVO.setUserName(user.getLoginName());
            emailSchedulerVO.setEmailDate(new Date());
            emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");

            aRInvoiceBC.save(emailSchedulerVO);
            log.info("---------------------------Email Sending Status----------------------------");
            log.info("Email Sent Successfully !!!!!");
            log.info("Email Sent By User : " + emailSchedulerVO.getUserName());
            log.info("FromAddress : " + emailSchedulerVO.getFromAddress());
            log.info("ToAddress : " + emailSchedulerVO.getToAddress());
            if (CommonUtils.isNotEmpty(emailSchedulerVO.getCcAddress())) {
                log.info("CcAddress : " + emailSchedulerVO.getCcAddress());
            }
            if (CommonUtils.isNotEmpty(emailSchedulerVO.getBccAddress())) {
                log.info("BccAddress : " + emailSchedulerVO.getBccAddress());
            }
            log.info("Email Subject : " + emailSchedulerVO.getSubject());
            log.info("Email Sent Date : " + emailSchedulerVO.getEmailDate());
            if (CommonUtils.isNotEmpty(emailSchedulerVO.getModuleId())) {
                log.info("ModuleId : " + emailSchedulerVO.getModuleId());
            }
            log.info("Current Email Status : " + emailSchedulerVO.getStatus());
        }
        if (printConfigForm.isEmailAll()) {
            List<CustomerContact> customerContactList = new LCLBookingDAO().getCodeFEmailContactList(printConfigForm.getId().toString());
            if (customerContactList != null && !customerContactList.isEmpty()) {
                for (CustomerContact customerContact : customerContactList) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    emailSchedulerVO.setToName(user.getFirstName());
                    emailSchedulerVO.setToAddress(customerContact.getEmail());
                    emailSchedulerVO.setFromName(user.getLoginName());
                    emailSchedulerVO.setFromAddress(fromAddress);
                    emailSchedulerVO.setModuleName(printConfigForm.getScreenName());
                    emailSchedulerVO.setName(printConfigForm.getScreenName());
                    emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_EMAIL);
                    emailSchedulerVO.setFileLocation(printConfigForm.getFileLocation());
                    emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                    emailSchedulerVO.setModuleId(null != configId ? configId.substring(-1 != configId.indexOf("-") ? configId.indexOf("-") + 1 : 0) : "");
                    emailSchedulerVO.setSubject(printConfigForm.getEmailSubject());
                    emailSchedulerVO.setTextMessage(printConfigForm.getEmailMessage());
                    emailSchedulerVO.setHtmlMessage(printConfigForm.getEmailMessage());
                    emailSchedulerVO.setUserName(user.getLoginName());
                    emailSchedulerVO.setEmailDate(new Date());
                    emailSchedulerVO.setModuleId(printConfigForm.getFileNumber() != null ? printConfigForm.getFileNumber() : "");

                    aRInvoiceBC.save(emailSchedulerVO);
                    log.info("---------------------------Email Sending Status----------------------------");
                    log.info("Email Sent Successfully !!!!!");
                    log.info("Email Sent By User : " + emailSchedulerVO.getUserName());
                    log.info("FromAddress : " + emailSchedulerVO.getFromAddress());
                    log.info("ToAddress : " + emailSchedulerVO.getToAddress());
                    if (CommonUtils.isNotEmpty(emailSchedulerVO.getCcAddress())) {
                        log.info("CcAddress : " + emailSchedulerVO.getCcAddress());
                    }
                    if (CommonUtils.isNotEmpty(emailSchedulerVO.getBccAddress())) {
                        log.info("BccAddress : " + emailSchedulerVO.getBccAddress());
                    }
                    log.info("Email Subject : " + emailSchedulerVO.getSubject());
                    log.info("Email Sent Date : " + emailSchedulerVO.getEmailDate());
                    if (CommonUtils.isNotEmpty(emailSchedulerVO.getModuleId())) {
                        log.info("ModuleId : " + emailSchedulerVO.getModuleId());
                    }
                    log.info("Current Email Status : " + emailSchedulerVO.getStatus());
                }
            }
        }
        session.removeAttribute("valueFromTerminal");
        session.removeAttribute("phoneFromTerminal");
        session.removeAttribute("faxFromTerminal");
        session.removeAttribute("nameTerminal");
        return true;
    }

    public String changeFileNo(String bolId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Map requestMap = (Map) session.getAttribute("requestMap");
        Set<Map.Entry> set = (Set<Map.Entry>) requestMap.entrySet();
        Map requestMapDup = new HashMap();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Entry entry = (Entry) iterator.next();
            if (entry.getKey().toString().equalsIgnoreCase("blId")) {
                requestMapDup.put(entry.getKey().toString().toString(), bolId);
            } else {
                requestMapDup.put(entry.getKey().toString().toString(), entry.getValue().toString());
            }
        }
        session.setAttribute("TempBolId", bolId);
        session.setAttribute(REQUEST_MAP, requestMapDup);
        return "yes";
    }

    public boolean getEmailStatus(String screenName, String fileNo) throws Exception {
        String moduleId = fileNo;
        if (CommonUtils.in(screenName, "Quotation", "Booking", "BL")) {
            fileNo = "04-" + fileNo;
        }
        return new EmailschedulerDAO().isEmailedOrFaxed(fileNo, moduleId);
    }

    public String pullClientEmail(String fileNo) throws Exception {
        if (null != fileNo && !fileNo.equals("")) {
            if (fileNo.indexOf("-") != 0 && fileNo.indexOf("-") != -1) {
                fileNo = fileNo.substring(fileNo.indexOf("-") + 1);
            }
            FclBl fclBl = new FclBlDAO().getFileNoObject(fileNo);
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(fileNo);
            if (fclBl == null && bookingFcl != null) {
                if (bookingFcl.getBookingContact() != null) {
                    return bookingFcl.getBookingContact();
                }
            } else if (bookingFcl == null && fclBl != null) {
                if (fclBl.getBookingContact() != null) {
                    return fclBl.getBookingContact();
                }
            } else {
                if (fclBl.getBookingContact() != null) {
                    return fclBl.getBookingContact();
                }
            }
        }
        return "";
    }

    public String getPropertyPdf(String propertyName) throws Exception {
        String outputFileName = LoadLogisoftProperties.getProperty(propertyName);
        return outputFileName;
    }

    public String generateVoyNotiReport(String unitSsId, String documentName, String email, String voyContent, String emailSubject, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(outputFileName + "/" + LclReportConstants.IMPORT_UNITS);
        if (!file.exists()) {
            file.mkdir();
        }
        String realPath = session.getServletContext().getRealPath("/");
        LclVoyageNotificationPdfCreator voyageNotification = new LclVoyageNotificationPdfCreator();
        outputFileName = outputFileName + "/" + LclReportConstants.IMPORT_UNITS + "/" + LclReportConstants.VOYAGE_NOTIFICATION + ".pdf";
        voyageNotification.createPdf(realPath, outputFileName, unitSsId, voyContent);
        this.doMailTransaction(outputFileName, user, email, emailSubject);
        return outputFileName;
    }

    private void doMailTransaction(String fileName, User thisUser, String email, String emailSubject) throws Exception {
        String cc = null;
        String htmlMsg = "";
        String textMsg = null;
        String coverLetter = null;
        String printerName = null;
        String responseCode = null;
        EmailschedulerDAO emailDao = new EmailschedulerDAO();
        Date today = new Date();
        String[] splitEmails = email.split(",");
        for (int i = 0; i < splitEmails.length; i++) {
            String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Boolean emailFlag = splitEmails[i].toString().matches(emailreg);
            String emailType = "";
            if (emailFlag) {
                emailType = "Email";
            } else {
                emailType = "Fax";
            }

            emailDao.saveMailTransactions(VOYAGE_NOTIFICATION, fileName,
                    emailType, EMAIL_STATUS_PENDING, today, splitEmails[i].toString(),
                    thisUser.getEmail(), cc, emailSubject.toUpperCase(), htmlMsg,
                    textMsg, SCREENNAME_LCLIMPUNITS, SCREENNAME_LCLIMPUNITS,
                    thisUser.getOutsourceEmail(), coverLetter, printerName, 1, responseCode);
        }
    }

    private String appendLclEmailBody(String toName, String fromName,
            String fromFaxNumber, String terminalPhone, String comment, String brand, HttpServletRequest req) throws Exception {
        String companyURL = "", companyLogo = "";
        String imagePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        if (CommonUtils.isNotEmpty(brand)) {
            companyURL = LoadLogisoftProperties.getProperty(brand.equalsIgnoreCase("ECI") ? "application.Econo.website"
                    : brand.equalsIgnoreCase("ECU") ? "application.ECU.website" : "application.OTI.website");
            companyLogo = imagePath + LoadLogisoftProperties.getProperty(brand.equalsIgnoreCase("ECU")
                    ? "application.image.logo" : "application.image.econo.logo");
        } else {
            String code = new SystemRulesDAO().getSystemRules("CompanyCode");
            companyURL = LoadLogisoftProperties.getProperty(code.equalsIgnoreCase("02") ? "application.OTI.website" : "application.Econo.website");
            companyLogo = imagePath + LoadLogisoftProperties.getProperty("application.image.econo.logo");
        }
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<HTML><BODY>");
        emailBody.append("<div style='font-family: sans-serif;'>");
        emailBody.append("<b>Please DO NOT reply to this message, see note 3 below.<b><br>");
        emailBody.append("<a href='http://").append(companyURL).append("' target='_blank'><img src='").append(companyLogo).append("'></a>");
        emailBody.append("<br>");
        emailBody.append("<p></p>");
        emailBody.append("<b>To Name:</b>").append(null != toName ? toName : "").append("<br>");
        emailBody.append("<b>To Company:</b>").append("").append("<p></p>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>From Name:</b>").append(null != fromName ? fromName : "").append("<br>");
        emailBody.append("<b>From Fax #:</b>").append(null != fromFaxNumber ? fromFaxNumber : "").append("<br>");
        emailBody.append("<b>From Phone #:</b>").append(null != terminalPhone ? terminalPhone : "").append("<br>");
        emailBody.append("<font color='red'><b>Important Comments: </b>").append(null != comment ? comment.toUpperCase() : "").append("</font>");
        emailBody.append("<pre>");
        emailBody.append("</pre><p></p>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>Did you know?</b><br>");
        emailBody.append("NEED LCL TRANS-ATLANTIC/PACIFIC SERVICES?  WE CAN ASSIST WITH YOUR IMPORT AND<br>");
        emailBody.append("EXPORT NEEDS TO AND FROM ASIA, EUROPE, THE MED, MIDDLE EAST AND AFRICA.<br>");
        emailBody.append("<br>");
        emailBody.append("CALL 1-866-326-6648  OR BOOK ON LINE AT <a href='http://'").append(companyURL).append("' target='_blank'>").append(companyURL).append("</a><br>");
        emailBody.append("<p></p>");
        emailBody.append("<a href='http://www.inttra.com/econocaribe/shipping-instructions?msc=ecbkem' target='_blank'><img src'http://www.econocaribe.com/media/mail/inttra_ad.jpg'></a><p></p>");
        emailBody.append("<b>Helpful Information:</b><br>");
        emailBody.append("1. Open the attached PDF image with Adobe Acrobat Reader. This software can<br>");
        emailBody.append("be downloaded for free, just visit <a href='http://'").append(companyURL).append("' target='_blank'>").append(companyURL).append("</a>.<br>");
        emailBody.append("2. The attached image may contain multiple pages.<br>");
        emailBody.append("3. Please do not reply to this email, it is sent from an automated<br>");
        emailBody.append("system, there will be no response from this address. For assistance contact<br>");
        emailBody.append("your sales representative or your local office at (866) 326-6648.<br>");
        emailBody.append("</b></b>");
        emailBody.append("</div>");
        emailBody.append("</BODY>");
        emailBody.append("</HTML>");
        return emailBody.toString();
    }
}
