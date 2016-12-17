/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cvst.logisoft.struts.form.lcl.ExportVoyageHblBatchForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserPrinterAssociationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.lcl.report.ExportUnitsLargeManifestPdf;
import com.gp.cong.logisoft.lcl.report.ExportVoyageHblBatchPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclConsolidationMiniManifestPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclOceanManifestPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ExportVoyageHblBatchAction extends LogiwareDispatchAction implements LclReportConstants {

    private static final Logger log = Logger.getLogger(ExportVoyageHblBatchAction.class);

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportVoyageHblBatchForm batchForm = (ExportVoyageHblBatchForm) form;
        User user = getCurrentUser(request);
        Map<String, String> printerList = new UserPrinterAssociationDAO().getPrinterListHbl(user.getUserId());
        request.setAttribute("printerList", CommonUtils.isNotEmpty(printerList) ? printerList : "");
        request.setAttribute("unitList", new ExportUnitQueryUtils().getUnitList(Long.parseLong(batchForm.getHeaderId())));
        request.setAttribute("isEmailorFax", new EmailschedulerDAO().isEmailedOrFaxed(batchForm.getVoyageNumber(), batchForm.getVoyageNumber()));
        request.setAttribute("exportVoyageHblBatchForm", batchForm);
        return mapping.findForward("success");
    }

    public ActionForward setPrint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportVoyageHblBatchForm batchForm = (ExportVoyageHblBatchForm) form;
        User user = getCurrentUser(request);
        ExportVoyageHblBatchPdfCreator pdfCreator = new ExportVoyageHblBatchPdfCreator();
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "LCLExportHouseBL"
                + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String realpath = request.getSession().getServletContext().getRealPath("/");
        request.setAttribute("unitList", new ExportUnitQueryUtils().getUnitList(Long.parseLong(batchForm.getHeaderId())));
        request.setAttribute("isEmailorFax", new EmailschedulerDAO().isEmailedOrFaxed(batchForm.getVoyageNumber(), batchForm.getVoyageNumber()));
        String toAddress = CommonUtils.isNotEmpty(batchForm.getToEmailAddress()) ? batchForm.getToEmailAddress() : "";
        String ccAddress = CommonUtils.isNotEmpty(batchForm.getCcEmailAddress()) ? batchForm.getCcEmailAddress() : "";
        String subject = CommonUtils.isNotEmpty(batchForm.getEmailSubject()) ? batchForm.getEmailSubject().toUpperCase() : "";
        String tempFileName = "";
        List<Long> actualDrList = new ArrayList<Long>();
        List<Long> unratedDrList = new ArrayList<Long>();
        if (CommonUtils.isNotEmpty(batchForm.getFileNumberId())) {
            actualDrList.add(Long.parseLong(batchForm.getFileNumberId()));
        } else {
            actualDrList = new ExportUnitQueryUtils().getAllPickedCargoBkg(Long.parseLong(batchForm.getHeaderId()), Long.parseLong(batchForm.getUnitSSId()));
        }

        if (CommonUtils.isNotEmpty(actualDrList)) {
            for (Long fileId : actualDrList) {
                boolean printFlag = true;
                boolean isConsolidate = false;
                for (String type : batchForm.getDocumentTypes().split(",")) {
                    if (type.equalsIgnoreCase("FreightInvoiceCollect")) {
                        printFlag = new ExportUnitQueryUtils().isDrContainCollectCharge(fileId);
                    }
                    if (printFlag) {
                        tempFileName = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                                + "LCLExportHouseBL" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/"
                                + "BatchHouseBL_" + fileId + "_" + type + "_" + new Date().getTime() + ".pdf";
                        if (type.equalsIgnoreCase("MiniConsolidationManifest")) {
                            isConsolidate = new LclConsolidateDAO().isConsoildateFile(String.valueOf(fileId));
                            if (isConsolidate) {
                                LclConsolidationMiniManifestPdfCreator lclminimanifest = new LclConsolidationMiniManifestPdfCreator();
                                lclminimanifest.createReport(realpath, tempFileName, type, String.valueOf(fileId));
                            }
                        } else if (type.equalsIgnoreCase("UnratedDockReceipt")) {
                        } else if ((CommonUtils.isNotEqual(type, "LargePrintManifest")) && (CommonUtils.isNotEqual(type, "Manifest"))
                                && (CommonUtils.isNotEqual(type, "MiniConsolidationManifest")) && (CommonUtils.isNotEqual(type, "unitUnratedDockReceipt"))) {
                            pdfCreator.creareReport(tempFileName, batchForm, fileId, type, user, realpath);
                        }
                        int noOfCopies = type.equalsIgnoreCase("Original") ? batchForm.getOriginal()
                                : type.equalsIgnoreCase("NonNegotiable") ? batchForm.getNonNegotiable()
                                        : type.equalsIgnoreCase("SignedNonNegotiable") ? batchForm.getSignedNonNegotiable()
                                                : type.equalsIgnoreCase("UnsignedOriginal") ? batchForm.getUnsignedOriginal()
                                                        : type.equalsIgnoreCase("FreightInvoice") ? batchForm.getFrieghtInvoice()
                                                                : type.equalsIgnoreCase("FreightInvoiceCollect") ? batchForm.getFrieghtInvoiceCollect()
                                                                           : (type.equalsIgnoreCase("MiniConsolidationManifest") && isConsolidate) ? batchForm.getUnitMiniConsolidationManifest() : 0;
                        Map<String, String> printerList = new UserPrinterAssociationDAO().getPrinterListHbl(user.getUserId());
                        String printer = "QuicK Print";
                        if (type.equalsIgnoreCase("UnsignedOriginal")) {
                            printer = "Bill of Lading (Original)";
                        } else if (type.equalsIgnoreCase("Original")) {
                            printer = "Bill of Lading (Original SIGNED)";
                        }
                        String printerName = printerList.get(printer);
                        if (CommonUtils.isEmpty(printerName)) {
                            printerName = batchForm.getPrinterName();
                        }
                        if (noOfCopies != 0) {
                            saveMail("LCL Export House BL", tempFileName, "Print", "Pending",
                                    new Date(), toAddress, user.getEmail(), ccAddress, "", subject, "", "", "Export Voyage", batchForm.getVoyageNumber(),
                                    user.getFirstName(), "", printerName, noOfCopies, "");
                        }
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(batchForm.getFileNumberId()) && batchForm.getDocumentTypes().contains("UnratedDockReceipt")) {
            unratedDrList.add(Long.parseLong(batchForm.getFileNumberId()));
        } else {
            unratedDrList = new ExportUnitQueryUtils().getAllPickedCargoBkgUnrated(Long.parseLong(batchForm.getHeaderId()), Long.parseLong(batchForm.getUnitSSId()));
        }
        if (CommonUtils.isNotEmpty(unratedDrList) && batchForm.getDocumentTypes().contains("UnratedDockReceipt")) {
            for (Long fileId1 : unratedDrList) {
                for (String type : batchForm.getDocumentTypes().split(",")) {
                    if (type.equalsIgnoreCase("UnratedDockReceipt")) {
                          tempFileName = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                                + "LCLExportHouseBL" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/"
                                + "BatchHouseBL_" + fileId1 + "_" + type + "_" + new Date().getTime() + ".pdf";
                        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId1);
                        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
                        LclPdfCreator lclPdfCreator = new LclPdfCreator(lclBooking);
                        lclPdfCreator.createPdf(realpath, tempFileName, "Booking Confirmation Without Rate", null);
                    }
                    int noOfCopies = type.equalsIgnoreCase("UnratedDockReceipt") ? batchForm.getUnitUnratedDockReceipt() : 0;

                    Map<String, String> printerList = new UserPrinterAssociationDAO().getPrinterListHbl(user.getUserId());
                    String printer = "QuicK Print";
                    if (type.equalsIgnoreCase("UnsignedOriginal")) {
                        printer = "Bill of Lading (Original)";
                    } else if (type.equalsIgnoreCase("Original")) {
                        printer = "Bill of Lading (Original SIGNED)";
                    }
                    String printerName = printerList.get(printer);
                    if (CommonUtils.isEmpty(printerName)) {
                        printerName = batchForm.getPrinterName();
                    }
                    if (noOfCopies != 0) {
                        saveMail("LCL Export House BL", tempFileName, "Print", "Pending",
                                new Date(), toAddress, user.getEmail(), ccAddress, "", subject, "", "", "Export Voyage", batchForm.getVoyageNumber(),
                                user.getFirstName(), "", printerName, noOfCopies, "");
                    }
                }
            }
        }

        List<Long> unitlist = null;
        unitlist = new ExportUnitQueryUtils().getUnitSSIdList(Long.parseLong(batchForm.getHeaderId()), Long.parseLong(batchForm.getUnitSSId()));
        if (CommonUtils.isNotEmpty(unitlist)) {
            String toAddress1 = CommonUtils.isNotEmpty(batchForm.getToEmailAddress()) ? batchForm.getToEmailAddress() : "";
            String ccAddress1 = CommonUtils.isNotEmpty(batchForm.getCcEmailAddress()) ? batchForm.getCcEmailAddress() : "";
            String subject1 = CommonUtils.isNotEmpty(batchForm.getEmailSubject()) ? batchForm.getEmailSubject().toUpperCase() : "";
            String tempFileName1 = "";
            for (Long value : unitlist) {
                for (String type : batchForm.getDocumentTypes().split(",")) {
                    LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(value);
                    if (type.equalsIgnoreCase("LargePrintManifest")) {
                        tempFileName1 = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                                + "LCLExportHouseBL" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/"
                                + "BatchHouseBL_" + lclUnitSs.getLclUnit().getUnitNo() + "_" + type + "_" + new Date().getTime() + ".pdf";
                        ExportUnitsLargeManifestPdf exportUnitsLargeManifestPdf = new ExportUnitsLargeManifestPdf(lclUnitSs);
                        exportUnitsLargeManifestPdf.createPdf(realpath, tempFileName1);
                    } else if (type.equalsIgnoreCase("Manifest")) {
                        tempFileName1 = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                                + "LCLExportHouseBL" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/"
                                + "BatchHouseBL_" + lclUnitSs.getLclUnit().getUnitNo() + "_" + type + "_" + new Date().getTime() + ".pdf";
                        LclOceanManifestPdfCreator lclOceanManifestPdfCreator = new LclOceanManifestPdfCreator(lclUnitSs);
                        lclOceanManifestPdfCreator.CreateOceanManifestPdf(realpath, tempFileName1);
                    }
                    int noOfCopies = type.equalsIgnoreCase("LargePrintManifest") ? batchForm.getUnitLargePrintManifest()
                            : type.equalsIgnoreCase("Manifest") ? batchForm.getUnitManifest() : 0;

                    Map<String, String> printerList = new UserPrinterAssociationDAO().getPrinterListHbl(user.getUserId());
                    String printer = "QuicK Print";
                    String printerName = printerList.get(printer);
                    if (CommonUtils.isEmpty(printerName)) {
                        printerName = batchForm.getPrinterName();
                    }
                    if (noOfCopies != 0) {
                        saveMail("LCL Export House BL", tempFileName1, "Print", "Pending",
                                new Date(), toAddress1, user.getEmail(), ccAddress1, "", subject1, "", "", "Export Voyage", batchForm.getVoyageNumber(),
                                user.getFirstName(), "", printerName, noOfCopies, "");
                    }
                }
            }
        }

        return mapping.findForward("success");
    }

    public ActionForward setEmail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportVoyageHblBatchForm batchForm = (ExportVoyageHblBatchForm) form;
        User user = getCurrentUser(request);
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
        ExportVoyageHblBatchPdfCreator pdfCreator = new ExportVoyageHblBatchPdfCreator();
        File file = new File(outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "LCLExportHouseBL"
                + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String realpath = request.getSession().getServletContext().getRealPath("/");
        request.setAttribute("unitList", new ExportUnitQueryUtils().getUnitList(Long.parseLong(batchForm.getHeaderId())));
        request.setAttribute("isEmailorFax", new EmailschedulerDAO().isEmailedOrFaxed(batchForm.getVoyageNumber(), batchForm.getVoyageNumber()));

        List<String> emailList = new ArrayList();
        if (batchForm.isNegotiableEmail()) {
            emailList.add("nonNegotiable");
        }
        if (batchForm.isFrieghtEmail()) {
            emailList.add("FreightInvoice");
        }
        if (batchForm.isFrieghtEmailCollect()) {
            emailList.add("FreightInvoiceCollect");
        }
        if (batchForm.isUnitLargePrintManifestEmail()) {
            emailList.add("LargePrintManifest");
        }
        if (batchForm.isUnitManifestEmail()) {
            emailList.add("Manifest");
        }
        if (batchForm.isUnitMiniConsolidationManifestEmail()) {
            emailList.add("MiniConsolidationManifest");
        }
        if (batchForm.isUnitUnratedDockReceiptEmail()) {
            emailList.add("UnratedDockReceipt");
        }

        StringBuilder fileNameBuilder = new StringBuilder();
        for (String email : emailList) {
            String fileName = "";
            fileName = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                    + "LCLExportHouseBL" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd")
                    + "/" + "BatchHouseBL_" + batchForm.getVoyageNumber() + "_" + email + "_" + new Date().getTime() + ".pdf";
            if (email.equalsIgnoreCase("Manifest")) {
                LclOceanManifestPdfCreator lclOceanManifestPdfCreator = new LclOceanManifestPdfCreator(null);
                fileName = lclOceanManifestPdfCreator.createManifestEmailReport(realpath, fileName, batchForm);
            } else if (email.equalsIgnoreCase("LargePrintManifest")) {
                ExportUnitsLargeManifestPdf exportUnitsLargeManifestPdf = new ExportUnitsLargeManifestPdf(null);
                fileName = exportUnitsLargeManifestPdf.createManifestEmailReport(realpath, fileName, batchForm);
            } else if (email.equalsIgnoreCase("MiniConsolidationManifest")) {
                LclConsolidationMiniManifestPdfCreator lclminimanifest = new LclConsolidationMiniManifestPdfCreator();
                fileName = lclminimanifest.createEmailReport(realpath, fileName, batchForm);
            } else if (email.equalsIgnoreCase("UnratedDockReceipt")) {
                LclPdfCreator lclPdfCreator = new LclPdfCreator(null);
                lclPdfCreator.createEmailReport(realpath, fileName, batchForm, request);

            } else {
                fileName = pdfCreator.createEmailReport(fileName, batchForm, user, realpath, email);
            }
            if (fileName != null) {
                fileNameBuilder.append(fileName).append(";");
            }
        }

        String toAddress = CommonUtils.isNotEmpty(batchForm.getToEmailAddress()) ? batchForm.getToEmailAddress() : "";
        toAddress = batchForm.isEmailMe() && toAddress.equalsIgnoreCase("") ? user.getEmail() : toAddress;
        String ccAddress = CommonUtils.isNotEmpty(batchForm.getCcEmailAddress()) ? batchForm.getCcEmailAddress() : "";
        String bccAddress = CommonUtils.isNotEmpty(batchForm.getBccEmailAddress()) ? batchForm.getBccEmailAddress() : "";
        String companyName = LoadLogisoftProperties.getProperty("application.email.companyName");
        String subject = CommonUtils.isNotEmpty(batchForm.getEmailSubject()) ? batchForm.getEmailSubject().toUpperCase() : "";
        String emailMessage = CommonUtils.isNotEmpty(batchForm.getEmailMessage()) ? batchForm.getEmailMessage().toUpperCase() : "";
        String message = batchForm.getEmailBody(user, companyName, emailMessage, request);
        if (CommonUtils.isNotEmpty(fileNameBuilder)) {
            saveMail("LCL Export House BL", fileNameBuilder.toString().substring(0, fileNameBuilder.length() - 1), "Email", "Pending",
                    new Date(), toAddress, user.getEmail(), ccAddress, bccAddress,
                    subject, message, message, "Export Voyage", batchForm.getVoyageNumber(),
                    user.getFirstName(), "", "", 0, "");
        }
        return mapping.findForward("success");
    }

    public void saveMail(final String name, final String fileLocation, final String type,
            final String status, Date emailDate, final String to, final String from, final String cc,
            final String bcc, final String subject, final String htmlMsg, final String textMsg,
            final String moduleName, final String moduleId, final String userName, final String coverLetter,
            final String printerName, final Integer printCopy, final String responseCode) throws Exception {
        EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
        mailTransaction.setName(name);
        mailTransaction.setFileLocation(fileLocation);
        mailTransaction.setType(type);
        mailTransaction.setStatus(status);
        mailTransaction.setNoOfTries(0);
        mailTransaction.setEmailDate(emailDate);
        mailTransaction.setToAddress(to);
        mailTransaction.setFromAddress(from);
        mailTransaction.setCcAddress(cc);
        mailTransaction.setBccAddress(bcc);
        mailTransaction.setSubject(subject);
        mailTransaction.setHtmlMessage(htmlMsg);
        mailTransaction.setTextMessage(textMsg);
        mailTransaction.setModuleName(moduleName);
        mailTransaction.setModuleId(moduleId);
        mailTransaction.setUserName(userName);
        mailTransaction.setCoverLetter(coverLetter);
        mailTransaction.setPrinterName(printerName);
        mailTransaction.setPrintCopy(printCopy);
        mailTransaction.setResponseCode(responseCode);
        new EmailschedulerDAO().save(mailTransaction);
    }
}
