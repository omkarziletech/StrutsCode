package com.gp.cong.lcl.dwr;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.lcl.comparator.LclImportManifestBeanComparator;
import com.gp.cong.logisoft.lcl.report.CfsImpWareHousePdf;
import com.gp.cong.logisoft.lcl.report.CfsImpWarehouseWorksheet;
import com.gp.cong.logisoft.lcl.report.ExportDRCOBNotification;
import com.gp.cong.logisoft.lcl.report.ExportUnitsBlRiderPdfCreator;
import com.gp.cong.logisoft.lcl.report.ExportUnitsLargeManifestPdf;
import com.gp.cong.logisoft.lcl.report.ExportVGMDeclarationReport;
import com.gp.cong.logisoft.lcl.report.FreightInvoiceLclExcelCreator;
import com.gp.cong.logisoft.lcl.report.FreightInvoiceLclPdfCreator;
import com.gp.cong.logisoft.lcl.report.ImportRoutingPdfCreator;
import com.gp.cong.logisoft.lcl.report.LCLImpDR;
import com.gp.cong.logisoft.lcl.report.LclAllBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclArInvoicePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclBLPdfConfirmOnBoardNotice;
import com.gp.cong.logisoft.lcl.report.LclBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclBarrelDRPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclConsolidationMiniManifestPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclExportNewFormatFreightInvoice;
import com.gp.cong.logisoft.lcl.report.LclExportNewPdfFormat;
import com.gp.cong.logisoft.lcl.report.LclExportOldPdfFormat;
import com.gp.cong.logisoft.lcl.report.LclFilterPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclHazardousBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclImpForm;
import com.gp.cong.logisoft.lcl.report.LclImportBkgConciseStrippingPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclImportBkgDeliveryPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclImportBkgPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclImportBkgStrippingPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclImportCFSExcelCreator;
import com.gp.cong.logisoft.lcl.report.LclImportExceptionsAndAlarms;
import com.gp.cong.logisoft.lcl.report.LclImportInspectionExcelCreator;
import com.gp.cong.logisoft.lcl.report.LclImportProfitLossExcelCreator;
import com.gp.cong.logisoft.lcl.report.LclImportUnitDeliveryPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclLoadPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclMasterBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclOceanManifestPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclQuotePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.lcl.report.LclSSMasterFromHouseBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclThirdPartyInvoicePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclTruckOutboundPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclTruckPickupPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclUnitExcepionsPdfCreator;
import com.gp.cong.logisoft.lcl.report.MultimodalDangerousGoodsReport;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LclPrintUtil implements LclReportConstants, LclCommonConstant {

    public String lclPrintReport(String reportLocation, String fileId, String fileNumber, String screenName,
            String documentName, HttpServletRequest request) throws Exception {
        if (CommonUtils.isEqual(screenName, SCREENNAME_LCLEXPORTUNITREPORT)) {
            return printExportUnitsReport(reportLocation, fileId, fileNumber, documentName, request);//Exports and Imports
        }
        return null;
    }

    public String changeNoticeNo(String noticeNo, String index, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(noticeNo)) {
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setPrintFaxRadioLclBl(noticeNo);
            session.setAttribute("lclSession", lclSession);
            session.setAttribute("lclPrintFaxRadioIndex", index);
        }
        return "success";
    }

    public String isBlFound(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        int count = lclUnitSSDAO.getTotalCntBkgInUnitsExports(requestMap.get("fileId"));
        if (count == 0) {
            return "failure";
        }
        return "success";
    }

    public void createReportFolder(String reportLocation, String documentName) throws Exception {
        File file = new File(reportLocation + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + documentName + "/"
                + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String reportPath(String reportLocation, String documentName) throws Exception {
        reportLocation = reportLocation + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                + documentName + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        return reportLocation;
    }

    public String printBookingReport(String reportLocation, String fileId,
            String fileNumber, String documentName, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        if ("".equalsIgnoreCase(reportLocation)) {
            reportLocation = LoadLogisoftProperties.getProperty("report.preview.location");
        }
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclBooking booking = bookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        bookingDAO.getCurrentSession().evict(booking);
        if (CommonUtils.isEqual(documentName, COB_NOTIFICATION)) {
            createReportFolder(reportLocation, "BookingCobNotification");
            LclBl lclBl = booking.getLclFileNumber().getLclBl();
            ExportDRCOBNotification exportDRCOBNotification = new ExportDRCOBNotification(lclBl, booking);
            reportLocation = reportPath(reportLocation, "BookingCobNotification") + "COBNotification_" + fileNumber + ".pdf";
            exportDRCOBNotification.createReport(realPath, reportLocation, request);
        } else if (documentName.equalsIgnoreCase("TRUCK PICK-UP REQUEST")) {
            LclTruckPickupPdfCreator lclTruckPickupPdfCreator = new LclTruckPickupPdfCreator(booking);
            createReportFolder(reportLocation, "TruckPickUpRequest");
            reportLocation = reportPath(reportLocation, "TruckPickUpRequest") + "TRUCK_PICKUP_" + fileNumber + ".pdf";
            lclTruckPickupPdfCreator.lclTruckPickupreport(reportLocation);
        } else if (CommonUtils.isEqual(documentName, DOCUMENT_BARREL)) {
            createReportFolder(reportLocation, "BarrelDr");
            LclBarrelDRPdfCreator lclBarrelDRPdfCreator = new LclBarrelDRPdfCreator(booking, realPath);
            reportLocation = reportPath(reportLocation, "BarrelDr") + LCL_BARREL + "_" + fileNumber + ".pdf";
            lclBarrelDRPdfCreator.createReport(realPath, reportLocation, documentName, request);
        } else if (documentName.equalsIgnoreCase(CommonConstants.LABEL_PRINT)) {
            reportLocation = fileNumber;
        } else {
            String folderName = documentName.replaceAll(" ", "");
            createReportFolder(reportLocation, folderName);
            reportLocation = reportPath(reportLocation, folderName) + LCL_BOOKING + "_" + fileNumber + ".pdf";
            LclPdfCreator lclPdfCreator = new LclPdfCreator(booking);
            lclPdfCreator.createPdf(realPath, reportLocation, documentName, request);
        }
        return reportLocation;
    }

    public String printQuoteReport(String reportLocation, String fileId, String fileNumber,
            String documentName, HttpServletRequest request) throws Exception {
        if ("".equalsIgnoreCase(reportLocation)) {
            reportLocation = LoadLogisoftProperties.getProperty("report.preview.location");
        }
        LCLQuoteDAO quoteDAO = new LCLQuoteDAO();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        LclQuote quote = quoteDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        quoteDAO.getCurrentSession().evict(quote);
        String folderName = "E".equalsIgnoreCase(quote.getQuoteType()) ? LCL_QUOTE : FOLDERNAME_QUOTE_IMP;
        folderName = folderName.replaceAll(" ", "");
        createReportFolder(reportLocation, folderName);
        LclQuotePdfCreator lclQuotePdfCreator = new LclQuotePdfCreator(quote);
        reportLocation = reportPath(reportLocation, folderName) + "LclQuote_" + fileNumber + ".pdf";
        lclQuotePdfCreator.createQuotePdf(realPath, reportLocation);
        return reportLocation;
    }

    public String printReportBL(String reportLocation, String fileId, String fileNumber,
            String documentName, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        if (CommonUtils.isEqual(documentName, DOCUMENT_LCL_CONFIRMATION_BOARD)) {
            createReportFolder(reportLocation, "BlConfirmOnBoardNotice");
            reportLocation = reportPath(reportLocation, "BlConfirmOnBoardNotice") + "ConfirmOnBoardNotice_" + fileNumber + ".pdf";
            new LclBLPdfConfirmOnBoardNotice().createReport(realPath, fileId, reportLocation);
        } else if ("All Bill of Lading Edits".equalsIgnoreCase(documentName)) {
            System.out.println("Print Preview BL POOL PDF Start Time==" + new Date());
            createReportFolder(reportLocation, "BillofLadingEdits");
            reportLocation = reportPath(reportLocation, "BillofLadingEdits") + "BillofLadingEdits.pdf";
            LclAllBLPdfCreator pdfCreator = new LclAllBLPdfCreator();
            pdfCreator.createBlReport(realPath, reportLocation, documentName, fileId, "", lclSession.getPrintFaxRadioLclBl());
            System.out.println("Print Preview BL POOL PDF End Time==" + new Date());
        } else if ("Unrated Bill Of Lading(Non-Negotiable)".equalsIgnoreCase(documentName)) {
            createReportFolder(reportLocation, "UnratedBillOfLading");
            LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
            reportLocation = reportPath(reportLocation, "UnratedBillOfLading") + "UnratedBillOfLading(Non-Negotiable)_" + fileNumber + ".pdf";
            lclBLPdfCreator.createReport(realPath, reportLocation, documentName, fileId, "", lclSession.getPrintFaxRadioLclBl(), false);
        } else if ("Consolidation Mini Manifest".equalsIgnoreCase(documentName)) {
            createReportFolder(reportLocation, "ConsolidationMiniManifest");
            LclConsolidationMiniManifestPdfCreator lclminimanifest = new LclConsolidationMiniManifestPdfCreator();
            reportLocation = reportPath(reportLocation, "ConsolidationMiniManifest") + "ConsolidationMiniManifest_" + fileNumber + ".pdf";
            lclminimanifest.createReport(realPath, reportLocation, documentName, fileId);
        } else if ("SS Master From House BL".equalsIgnoreCase(documentName)) {
            LclSSMasterFromHouseBLPdfCreator lclSSMasterFromHouseBLPdfCreator = new LclSSMasterFromHouseBLPdfCreator();
            createReportFolder(reportLocation, "SSMasterFromHouseBL");
            reportLocation = reportPath(reportLocation, "SSMasterFromHouseBL") + "SSMasterFromHouseBL" + fileNumber + ".pdf";
            lclSSMasterFromHouseBLPdfCreator.createPdfReport(realPath, reportLocation, documentName, fileId, "", lclSession.getPrintFaxRadioLclBl(), Boolean.TRUE);
        } else if ("Unrated Bill Of Lading (Original)".equalsIgnoreCase(documentName)) {
            createReportFolder(reportLocation, "UnratedBillOfLadingOriginal");
            LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
            reportLocation = reportPath(reportLocation, "UnratedBillOfLadingOriginal") + "UnratedBillOfLading(Original)_" + fileNumber + ".pdf";
            lclBLPdfCreator.createReport(realPath, reportLocation, documentName, fileId, "", lclSession.getPrintFaxRadioLclBl(), false);
        } else if ("Unrated Bill of Lading (Original UNSIGNED)".equalsIgnoreCase(documentName)) {
            createReportFolder(reportLocation, "UnratedBillOfLadingOriginalUNSIGNED");
            LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
            reportLocation = reportPath(reportLocation, "UnratedBillOfLadingOriginalUNSIGNED") + "UnratedBillOfLading(Original UNSIGNED)_" + fileNumber + ".pdf";
            lclBLPdfCreator.createReport(realPath, reportLocation, documentName, fileId, "", lclSession.getPrintFaxRadioLclBl(), false);
        } else {
            LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
            String[] separateBillTo = documentName.split("#");
            String documentBillTo = "";
            if (separateBillTo.length == 2) {
                if (!separateBillTo[1].equalsIgnoreCase("")) {
                    documentBillTo = separateBillTo[1].substring(0, 1);
                }
                if (separateBillTo[0].equalsIgnoreCase("Freight Invoice")) {
                    LclExportNewFormatFreightInvoice newFormatFreightInvoice = new LclExportNewFormatFreightInvoice();
                    newFormatFreightInvoice.setDocumentBillTo(documentBillTo);
                    createReportFolder(reportLocation, "NewFormatFreightInvoice");
                    reportLocation = reportPath(reportLocation, "NewFormatFreightInvoice") + "NewFormatFreightInvoice" + fileNumber + ".pdf";
                    newFormatFreightInvoice.createReport(realPath, reportLocation, documentName, fileId);
                    return reportLocation;
                }
                if (separateBillTo[0].equalsIgnoreCase("LCL Freight Invoice")) {
                    lclBLPdfCreator.setExportBilltoParty(documentBillTo);
                    documentName = separateBillTo[0] + "" + separateBillTo[1];
                }
            }
            String folderName = documentName.replaceAll(" ", "");
            createReportFolder(reportLocation, folderName);
            lclBLPdfCreator.setPrintdocumentName(documentName);
            reportLocation = reportPath(reportLocation, folderName) + "BillofLading_" + fileNumber + ".pdf";
            lclBLPdfCreator.createReport(realPath, reportLocation, documentName, fileId, "", lclSession.getPrintFaxRadioLclBl(), true);
        }
        return reportLocation;
    }

    public String lclFilterOptionReport(HttpServletRequest request) throws Exception {

        String outputFileName = LoadLogisoftProperties.getProperty("report.preview.location");
        String realPath = request.getSession().getServletContext().getRealPath("/");
        File file = new File(outputFileName + "/" + "LclFilter");
        if (!file.exists()) {
            file.mkdirs();
        }
        LclFilterPdfCreator lclFilterPdfCreator = new LclFilterPdfCreator();
        outputFileName = outputFileName + "/" + "LclFilter" + "/" + "LclFilterDocument" + ".pdf";
        lclFilterPdfCreator.createPdf(realPath, outputFileName);
        return outputFileName;
    }
    //Print Total BKG is Related TO Unit

    public String printTotalBKGUnit(String unitSsId, String fileNo, String documentName, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String realPath = session.getServletContext().getRealPath("/");
        User user = (User) session.getAttribute("loginuser");
        String reportLocation = LoadLogisoftProperties.getProperty("report.preview.location");
        createReportFolder(reportLocation, "ImportUnit");
        reportLocation = reportPath(reportLocation, "ImportUnit") + fileNo + "_" + user.getLoginName()
                + DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
        LclImportBkgPdfCreator lclImportBkgPdfCreator = new LclImportBkgPdfCreator();
        lclImportBkgPdfCreator.setDocumentName(documentName);
        lclImportBkgPdfCreator.setUnitSsId(unitSsId);
        lclImportBkgPdfCreator.setFileNumberId("");
        lclImportBkgPdfCreator.setRealPath(realPath);
        lclImportBkgPdfCreator.createPdf(realPath, unitSsId, "", "", reportLocation, documentName, null, null);
//        lclImportBkgPdfCreator.createImportBLPdf(realPath, unitSsId, "", outputFileName, documentName, null, null);
        return reportLocation;
    }

    public String getPropertyPdf(String propertyName) throws Exception {
        String outputFileName = LoadLogisoftProperties.getProperty(propertyName);
        return outputFileName;
    }
//AR Invoice

    public String lclArInvoiceReport(String reportLocation, String buttonValue, String invoiceId, String unitSSId,
            String agentFlag, HttpServletRequest request) throws Exception {
        if ("".equalsIgnoreCase(reportLocation)) {
            reportLocation = LoadLogisoftProperties.getProperty("report.preview.location");
        }
        return printArInvoiceReport(reportLocation, buttonValue, invoiceId, unitSSId, agentFlag, request);
    }

    public String createLclELoadPdf(String headerId, String unitSsId, String unitNo,
            String fileIds, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String reportLocation = LoadLogisoftProperties.getProperty("report.preview.location");
        String realPath = session.getServletContext().getRealPath("/");
        LclUnitSs unitSs = new LclUnitSsDAO().findById(Long.parseLong(unitSsId));
        User user = (User) session.getAttribute("loginuser");
        createReportFolder(reportLocation, "LoadPrint");
        reportLocation = reportPath(reportLocation, "LoadPrint") + "LoadingGuideNewFormat_" + unitNo + "_" + user.getLoginName()
                + DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
        LclLoadPdfCreator loadPdfCreator = new LclLoadPdfCreator(unitSs, unitSs.getLclSsHeader());
        loadPdfCreator.createExportUnitReport(realPath, reportLocation, fileIds);
        return reportLocation;
    }

    private String printArInvoiceReport(String outputFileName, String buttonValue, String invoiceId, String unitSSId,
            String agentFlag, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        ArRedInvoice arRedInvoice = null;
        ArRedInvoiceBC arRedInvoiceBC = new ArRedInvoiceBC();
        List<ArRedInvoiceCharges> arRedInvoiceChargesList = null;
        Map<String, List<ImportsManifestBean>> fileMap = null, unitMap = null;
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        boolean sessionFlag = false;
        if (!buttonValue.equalsIgnoreCase("AgentInvoice")) {
            arRedInvoice = arRedInvoiceBC.getInvoiceForEdit("", invoiceId);
        } else {
            List<ImportsManifestBean> agentList = lclUnitSSDAO.getDRSForAgentInvoice(Long.parseLong(unitSSId), "CreateAgentInvoiceReport");
            if ("Yes".equalsIgnoreCase(agentFlag)) {
                List<ImportsManifestBean> autoAgentList = lclUnitSSDAO.getAgentInvoiceCharges(unitSSId);
                agentList.addAll(autoAgentList);
            }
            Collections.sort(agentList, new LclImportManifestBeanComparator());
            fileMap = lclUnitSSDAO.getFileMap();
            if (CommonUtils.isEmpty(lclUnitSSDAO.getUnitMap())) {
                unitMap = new LinkedHashMap();
            } else {
                unitMap = lclUnitSSDAO.getUnitMap();
            }
            unitMap.putAll(fileMap);
            arRedInvoice = arRedInvoiceBC.saveAgentInvoice(agentList, user, lclUnitSSDAO.getTotalAmount(), unitSSId, false, null);
            arRedInvoiceChargesList = formatArRedInvoiceCharges(agentList);
            sessionFlag = true;
        }
        Date now = new Date();
        File file = new File(outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "Ar Invoice"
                + "/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        outputFileName = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "Ar Invoice"
                + "/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + arRedInvoice.getInvoiceNumber() + "_" + user.getLoginName()
                + DateUtils.formatDate(now, "_yyyyMMdd_HHmmss") + ".pdf";
        String realPath = session.getServletContext().getRealPath("/");
        LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
        lclArInvoicePdfCreator.createReport(arRedInvoice, outputFileName, realPath, user, arRedInvoiceChargesList, unitMap, sessionFlag);
        return outputFileName;
    }

    public List<ArRedInvoiceCharges> formatArRedInvoiceCharges(List<ImportsManifestBean> agentList) {
        List<ArRedInvoiceCharges> arRedInvoiceChargesList = new ArrayList<ArRedInvoiceCharges>();
        for (ImportsManifestBean agent : agentList) {
            ArRedInvoiceCharges arRedInvoiceCharges = new ArRedInvoiceCharges();
            arRedInvoiceCharges.setChargeCode(agent.getChargeCode());
            arRedInvoiceCharges.setAmount(agent.getTotalCharges());
            arRedInvoiceCharges.setShipmentType(agent.getShipmentType());
            arRedInvoiceCharges.setTerminal(agent.getBillingTerminal());
            arRedInvoiceCharges.setInvoiceNumber(agent.getInvoiceNo());
            if (CommonUtils.isNotEmpty(agent.getConcatenatedFileNos())) {
                arRedInvoiceCharges.setLclDrNumber(agent.getConcatenatedFileNos().substring(0, agent.getConcatenatedFileNos().length() - 1));
            }
            arRedInvoiceChargesList.add(arRedInvoiceCharges);
        }
        return arRedInvoiceChargesList;
    }

    public String printExportUnitsReport(String reportLocation, String unitSsId, String fileNumber,
            String documentName, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        Long lclUnitssId = Long.parseLong(unitSsId);
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(lclUnitssId);
        if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, "Trucker OutBound Delivery")) {
            createReportFolder(reportLocation, "TruckerOutBondDelivery");
            reportLocation = reportPath(reportLocation, "TruckerOutBondDelivery") + "Trucker_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            LclTruckOutboundPdfCreator lclTruckOutboundPdfCreator = new LclTruckOutboundPdfCreator(lclUnitSs);
            lclTruckOutboundPdfCreator.createPdf(realPath, reportLocation, request);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, CFS_IMP_WAREHOUSE)) {
            createReportFolder(reportLocation, "CFSWarehouseProFormaInvoice");
            reportLocation = reportPath(reportLocation, "CFSWarehouseProFormaInvoice") + "CFS_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            CfsImpWareHousePdf cfsImpWareHousePdf = new CfsImpWareHousePdf(lclUnitSs);
            cfsImpWareHousePdf.setRealPath(realPath);
            cfsImpWareHousePdf.createPdf(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, EXPORTLOADINGGUIDEOLD)) {
            createReportFolder(reportLocation, "LoadingGuideOldFormat");
            reportLocation = reportPath(reportLocation, "LoadingGuideOldFormat") + "LoadingGuideOldFormat_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            LclExportOldPdfFormat lclExportOldPdfFormat = new LclExportOldPdfFormat(lclUnitSs);
            lclExportOldPdfFormat.createExportUnitReport(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, "Hazardous BL Form")) {
            createReportFolder(reportLocation, "HazardousBLForm");
            reportLocation = reportPath(reportLocation, "HazardousBLForm") + "Hazardous_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            LclHazardousBLPdfCreator lclHazardousBLPdfCreator = new LclHazardousBLPdfCreator(lclUnitSs);
            lclHazardousBLPdfCreator.createHazardousPdf(realPath, reportLocation, request);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, EXPORTLOADINGGUIDENEW)) {
            LclExportNewPdfFormat lclExportNewPdfFormat = new LclExportNewPdfFormat(lclUnitSs);
            createReportFolder(reportLocation, "LoadingGuideNewFormat");
            reportLocation = reportPath(reportLocation, "LoadingGuideNewFormat") + "LoadingGuideNewFormat_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            lclExportNewPdfFormat.createExportUnitReport(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, "Ocean Manifest")) {
            LclOceanManifestPdfCreator lclOceanManifestPdfCreator = new LclOceanManifestPdfCreator(lclUnitSs);
            createReportFolder(reportLocation, "OceanManifest");
            reportLocation = reportPath(reportLocation, "OceanManifest") + "OceanManifest_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            lclOceanManifestPdfCreator.CreateOceanManifestPdf(realPath, reportLocation);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, DOCUMENTLCLSTRIPPINGTALLY)) {//Imports
            LclImportBkgStrippingPdfCreator lclImportBkgStrippingPdfCreator = new LclImportBkgStrippingPdfCreator(lclUnitSs, request);
            createReportFolder(reportLocation, "StrippingTally");
            reportLocation = reportPath(reportLocation, "StrippingTally") + "StrippingTally_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            lclImportBkgStrippingPdfCreator.createPdf(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, DOCUMENTLCLCONCISESTRIPPINGTALLY)) {//Imports
            LclImportBkgConciseStrippingPdfCreator lclImportBkgConciseStrippingPdfCreator = new LclImportBkgConciseStrippingPdfCreator(lclUnitSs);
            createReportFolder(reportLocation, "ConciseStrippingTally");
            reportLocation = reportPath(reportLocation, "ConciseStrippingTally") + "ConciseStrippingTally_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            lclImportBkgConciseStrippingPdfCreator.createPdf(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, DOCUMENTLCLDELIVERYORDER)) {//Imports
            createReportFolder(reportLocation, "DeliveryOrder");
            reportLocation = reportPath(reportLocation, "DeliveryOrder") + "DeliveryOrder_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            LclImportUnitDeliveryPdfCreator importUnitDeliveryPdfCreator = new LclImportUnitDeliveryPdfCreator(lclUnitSs, request);
            importUnitDeliveryPdfCreator.createExportUnitReport(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, UNITEXCEPTIONSLIST)) {
            LclUnitExcepionsPdfCreator lclUnitExcepionsPdfCreator = new LclUnitExcepionsPdfCreator();
            createReportFolder(reportLocation, "UnitExceptionsList");
            reportLocation = reportPath(reportLocation, "UnitExceptionsList") + "UnitException_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            lclUnitExcepionsPdfCreator.createPdf(realPath, reportLocation, unitSsId);
        } else if (CommonFunctions.isNotNull(lclUnitSs) && CommonUtils.isEqual(documentName, MANIFEST_LARGE_PRINT)) {
            ExportUnitsLargeManifestPdf exportUnitsLargeManifestPdf = new ExportUnitsLargeManifestPdf(lclUnitSs);
            createReportFolder(reportLocation, "ManifestLargePrintFormat");
            reportLocation = reportPath(reportLocation, "ManifestLargePrintFormat") + "ManifestLarge_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            exportUnitsLargeManifestPdf.createPdf(realPath, reportLocation);
        } else if (CommonUtils.isEqual(documentName, BL_RIDER_FORM)) {
            createReportFolder(reportLocation, "BLRiderForm");
            reportLocation = reportPath(reportLocation, "BLRiderForm") + "BLRiderForm_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            ExportUnitsBlRiderPdfCreator exportUnitsBlRiderPdfCreator = new ExportUnitsBlRiderPdfCreator(lclUnitSs);
            exportUnitsBlRiderPdfCreator.createPdf(realPath, reportLocation);
        } else if (CommonUtils.isEqual(documentName, "VGM Declaration")) {
            createReportFolder(reportLocation, "LCL_Exports_VGM_Declaration");
            reportLocation = reportPath(reportLocation, "LCL_Exports_VGM_Declaration") + "VGM_Declaration_" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            ExportVGMDeclarationReport exportVGMDeclarationReport = new ExportVGMDeclarationReport(lclUnitSs);
            exportVGMDeclarationReport.createPdf(realPath, reportLocation);
        } else if (CommonUtils.isEqual(documentName, "Multimodal Dangerous Goods Form")) {
            createReportFolder(reportLocation, "MultimodalDangerousGoodsForm");
            reportLocation = reportPath(reportLocation, "MultimodalDangerousGoodsForm") + "MultimodalDangerousGoodsForm" + lclUnitSs.getLclUnit().getUnitNo() + ".pdf";
            new MultimodalDangerousGoodsReport(lclUnitSs, reportLocation, request).createPdf();
        }
        return reportLocation;
    }

    public String printBkgCFSReport(String reportLocation, String unitSsId, String fileNumber, String documentName) throws Exception {
        if (documentName.equalsIgnoreCase(DOCUMENTLCLCFSINSTRUCTIONS)) {
            createReportFolder(reportLocation, "CFSInstructionsForm");
            LclImportCFSExcelCreator lclImportCFSExcelCreator = new LclImportCFSExcelCreator();
            reportLocation = reportPath(reportLocation, "CFSInstructionsForm") + "CFSInstructionsForm_" + fileNumber + ".xlsx";
            lclImportCFSExcelCreator.create(reportLocation, unitSsId);
        }
        if (documentName.equalsIgnoreCase(LCL_INSPECTION_CONTROL_FORM)) {
            createReportFolder(reportLocation, "InspectionControlForm");
            LclImportInspectionExcelCreator lclImportInspectionExcelCreator = new LclImportInspectionExcelCreator();
            reportLocation = reportPath(reportLocation, "InspectionControlForm") + "InspectionControlForm_" + fileNumber + ".xlsx";
            lclImportInspectionExcelCreator.create(reportLocation, unitSsId);
        }
        return reportLocation;
    }

    public String printUnitImpBkg(String fileId, String fileNumber,
            String documentName, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String reportLocation = LoadLogisoftProperties.getProperty("report.preview.location");
        String ouputFile = createImportBkgReport(reportLocation, fileId, fileNumber, documentName, realPath, null, request);
        if (ouputFile.contains(".pdf")) {
            File files = new File(ouputFile);
            ouputFile = ouputFile.replace(".pdf", "");
            ouputFile += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
            files.renameTo(new File(ouputFile));
        }
        return ouputFile;
    }

    public String printImportBkgReport(String reportLocation, String fileId, String fileNumber,
            String documentName, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        if (CommonUtils.isEqual(documentName, FreightInvoiceWorksheet)) {
            return createFreightInvoiceXlsx(reportLocation, fileId, fileNumber, documentName, realPath, null, request);

        } else {
            return createImportBkgReport(reportLocation, fileId, fileNumber, documentName, realPath, null, request);
        }
    }

    public String createFreightInvoiceXlsx(String reportLocation, String fileId, String fileNumber,
            String documentName, String realPath, String emailId, HttpServletRequest request) throws Exception {
        createReportFolder(reportLocation, FREIGHT_INVOICE);
        reportLocation = reportPath(reportLocation, FREIGHT_INVOICE) + fileNumber + ".xlsx";
        new FreightInvoiceLclExcelCreator(reportLocation, fileId, realPath).createReport();
        return reportLocation;
    }

    public String createImportBkgReport(String reportLocation, String fileId, String fileNumber,
            String documentName, String realPath, String emailId, HttpServletRequest request) throws Exception {
        User user = null;
        if (request != null) {
            HttpSession session = request.getSession();
            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
            }
        }
        if (CommonUtils.isEqual(documentName, DOCUMENTLCLDELIVERYORDER) && null != fileNumber && !fileNumber.trim().equals("")) {
            createReportFolder(reportLocation, "DeliveryOrder");
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            bookingDAO.getCurrentSession().evict(lclBooking);
            LclImportBkgDeliveryPdfCreator lclImportDeliveryBkgPdfCreator = new LclImportBkgDeliveryPdfCreator(lclBooking);
            reportLocation = reportPath(reportLocation, "DeliveryOrder") + fileNumber + ".pdf";
            lclImportDeliveryBkgPdfCreator.createDeliveryOrderPdf(realPath, reportLocation, documentName, request);
        } else if (CommonUtils.isEqual(documentName, DOCUMENT_DR) && null != fileNumber && !fileNumber.trim().equals("")) {
            createReportFolder(reportLocation, documentName);
            reportLocation = reportPath(reportLocation, documentName) + fileNumber + ".pdf";
            new LCLImpDR().createImportBLPdf(realPath, fileId, fileNumber, reportLocation, documentName);
        } else if (CommonUtils.isEqual(documentName, DOCUMENT_FORM) && null != fileNumber && !fileNumber.trim().equals("")) {
            createReportFolder(reportLocation, "7512Form");
            reportLocation = reportPath(reportLocation, "7512Form") + fileNumber + ".pdf";
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            LclImpForm lclimpform = new LclImpForm(lclBooking, request);
            lclimpform.createImportBLPdf(realPath, fileId, fileNumber, reportLocation, documentName);
        } else if (documentName.equalsIgnoreCase(CommonConstants.LABEL_PRINT)) {
            reportLocation = fileNumber;
        } else if (documentName.equalsIgnoreCase(DOCUMENTNAME_ROUTINGINSTRUCTIONS)) {
            createReportFolder(reportLocation, FOLDERNAME_ROUTINGINSTRUCTIONS);
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            ImportRoutingPdfCreator importRoutingPdfCreator = new ImportRoutingPdfCreator(lclBooking);
            reportLocation = reportPath(reportLocation, FOLDERNAME_ROUTINGINSTRUCTIONS) + fileNumber + ".pdf";
            importRoutingPdfCreator.createReport(reportLocation);
        } else {
            String folderName = documentName;
            if ("Pre Advice/Arrival Notice/Status Update".equalsIgnoreCase(documentName)) {
                folderName = "PreAdvice_ArrivalNotice_StatusUpdate";
            }
            createReportFolder(reportLocation, folderName);
            LclImportBkgPdfCreator lclImportBkgPdfCreator = new LclImportBkgPdfCreator();
            reportLocation = reportPath(reportLocation, folderName) + fileNumber + ".pdf";
            lclImportBkgPdfCreator.setDocumentName(documentName);
            lclImportBkgPdfCreator.setFileNumberId(fileId);
            lclImportBkgPdfCreator.setRealPath(realPath);
            if (documentName.equalsIgnoreCase("Freight Invoice")) {
                FreightInvoiceLclPdfCreator freightInvoiceLclPdfCreator = new FreightInvoiceLclPdfCreator();
                freightInvoiceLclPdfCreator.setRealPath(realPath);
                freightInvoiceLclPdfCreator.createPdf(realPath, null, fileId, fileNumber, reportLocation, documentName, emailId, user);
            } else if (documentName.equalsIgnoreCase("Third Party Invoice")) {
                LclThirdPartyInvoicePdfCreator lclThirdPartyInvoicePdfCreator = new LclThirdPartyInvoicePdfCreator();
                lclThirdPartyInvoicePdfCreator.createReport(realPath, fileId, fileNumber, reportLocation, documentName, user);
            } else {
                lclImportBkgPdfCreator.createPdf(realPath, null, fileId, fileNumber, reportLocation, documentName, emailId, user);
            }
        }
        return reportLocation;
    }

    public String printExportSSReport(String reportLocation, String fileId, String fileNumber,
            String documentName, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        createReportFolder(reportLocation, "LclMasterBl");
        LclSSMasterBl lclSSMasterBl = new LclSSMasterBlDAO().findById(Long.parseLong(fileId));
        if (CommonFunctions.isNotNull(lclSSMasterBl) && CommonUtils.isEqual(documentName, "Master Bill of Lading")) {
            LclMasterBLPdfCreator lclMasterBLPdfCreator = new LclMasterBLPdfCreator(lclSSMasterBl);
            reportLocation = reportPath(reportLocation, "LclMasterBl") + "MasterBl_" + lclSSMasterBl.getLclSsHeader().getScheduleNo() + ".pdf";
            lclMasterBLPdfCreator.createMasterPdf(realPath, reportLocation);
        } else if (CommonFunctions.isNotNull(lclSSMasterBl) && CommonUtils.isEqual(documentName, "All House Bills of Lading")) {
            LclMasterBLPdfCreator lclMasterBLPdfCreator = new LclMasterBLPdfCreator(lclSSMasterBl);
            reportLocation = reportPath(reportLocation, "LclMasterBl") + "MasterBl_" + lclSSMasterBl.getLclSsHeader().getScheduleNo() + ".pdf";
            lclMasterBLPdfCreator.createMasterPdf(realPath, reportLocation);
        }
        return reportLocation;
    }

    public String printProfitAndLossReport(String reportLocation, String unitSsId, String unitNo) throws Exception {
        createReportFolder(reportLocation, "ProfitLoss");
        reportLocation = reportPath(reportLocation, "ProfitLoss") + unitNo + "_Profit_Loss.xlsx";
        new LclImportProfitLossExcelCreator(reportLocation, unitSsId, unitNo).createReport();
        return reportLocation;
    }

    public String printImportsExceptionsAndAlarms(String reportLocation, String unitSsId, String unitNo) throws Exception {
        createReportFolder(reportLocation, "ImportsExceptionsAlarms");
        reportLocation = reportPath(reportLocation, "ImportsExceptionsAlarms") + unitNo + "_ImportsExceptions_Alarms.xlsx";
        new LclImportExceptionsAndAlarms(reportLocation, unitSsId, unitNo).createReport();
        return reportLocation;
    }

    public String printCfsWareHouseSheet(String reportLocation, String unitSsId, String unitNo) throws Exception {
        createReportFolder(reportLocation, "CFSWareHouseSheet");
        reportLocation = reportPath(reportLocation, "CFSWareHouseSheet") + unitNo + "_CFS_WareHouse_Sheet.xlsx";
        new CfsImpWarehouseWorksheet(reportLocation, unitSsId, unitNo).createReport();
        return reportLocation;
    }

    public String isBkgNoFound(String id) throws Exception {
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        String BkgNo = lclunitssdao.getBkgNo(id);
        if (CommonFunctions.isNotNull(BkgNo)) {
            return "success";
        }
        return "failure";
    }
}
