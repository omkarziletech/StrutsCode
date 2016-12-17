package com.gp.cvst.logisoft.struts.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ARInvoiceBC;
import com.gp.cong.logisoft.bc.accounting.CustomerStatementBC;
import com.gp.cong.logisoft.bc.accounting.FiscalPeriodBC;
import com.gp.cong.logisoft.bc.accounting.JournalEntryBC;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.NewQuotationReportBC;
import com.gp.cong.logisoft.bc.fcl.QuotationReportBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.PrintConfig;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.struts.form.PrintConfigForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.ArInvoice;
import com.gp.cvst.logisoft.domain.CustomerStatementPromtMessage;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.EmailForm;
import org.apache.commons.lang3.StringUtils;

public class EmailAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        EmailForm emailForm = (EmailForm) form;
        String buttonAction = request.getParameter("buttonValue");
        String id = emailForm.getId();
        String fromPeriod = emailForm.getFromPeriod();
        String toPeriod = emailForm.getToPeriod();
        String emailOption = emailForm.getEmailOption();
        String customerName = emailForm.getCustomerName();
        String reportTitle = emailForm.getReportTitle();
        String transactionId = emailForm.getTransactionId();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String path = LoadLogisoftProperties.getProperty("reportLocation");
        String realPath = this.getServlet().getServletContext().getRealPath("/");
        ARInvoiceBC aRInvoiceBC = new ARInvoiceBC();
        String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        //Get all form parameters
        User user = (User) session.getAttribute("loginuser");
        String fromName = user.getFirstName();
        String fromAddress = user.getEmail();
        String toAddress = emailForm.getToAddress();
        String ccAddress = emailForm.getCcAddress();
        String bccAddress = emailForm.getBccAddress();
        String subject = emailForm.getSubject();
        String mailMessage = emailForm.getBody();
        String findForward = "emailPage";
        //for testing


        if ("AccountReceivable".equals(buttonAction)) {
            String outputFileName = null;
            String fileName = null;

            if (null != id && !id.trim().equals("") && ("invoice".equals(emailOption) || "".equals(emailOption))) {
                String[] Ids = id.split(",");
                String[] transId = transactionId.split(",");
                String inVoiceId = null;
                String transactionNo = null;
                for (int i = 0; i < Ids.length; i++) {
                    inVoiceId = Ids[i];
                    transactionNo = transId[i];
                    ArInvoice arInvoice = aRInvoiceBC.getInvoiceForEdit(
                            inVoiceId, null);
                    if (null == arInvoice) {
                        try {
                            arInvoice = aRInvoiceBC.getInvoice(new Integer(
                                    inVoiceId));
                        } catch (NumberFormatException nfe) {
                            arInvoice = null;
                        }
                    }
                    if (null != arInvoice) {
                        inVoiceId = null != arInvoice ? arInvoice.getId().toString() : "";
                        File file = new File(path + "/Documents/ArInvoice/" + dateFolder);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        if (null == outputFileName) {
                            outputFileName = path;
                            outputFileName = outputFileName + "/Documents/ArInvoice/" + dateFolder + inVoiceId + ".pdf";
                            fileName = outputFileName;
                        } else {
                            outputFileName = outputFileName + ";" + path + "/Documents/ArInvoice/" + dateFolder + inVoiceId + ".pdf";
                            fileName = path + "/Documents/ArInvoice/" + dateFolder + inVoiceId + ".pdf";
                        }
                        aRInvoiceBC.createArInvoiceReport(inVoiceId, fileName, realPath);
                    }
                }
                // inserting in mail_transactions table
                Date today = new Date();
                EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                emailSchedulerVO.setEmailData(null, toAddress, fromName,
                        fromAddress, ccAddress, bccAddress, subject,
                        mailMessage);
                emailSchedulerVO.setEmailInfo("AR Invoice", outputFileName,
                        "Email", 0, today, "ARINQUIRY", customerName, user.getLoginName());
                new EmailschedulerDAO().save(emailSchedulerVO);
                String notesDesc = "Invoice Emailed to " + toAddress;
                if (null != emailForm.getCustomerId() && emailForm.getCustomerId() != 0) {
                    Notes notes = new Notes(NotesConstants.ARCONFIGURATION, null,
                            today, NotesConstants.NOTES_TYPE_AUTO, notesDesc,
                            fromName);
                    NotesDAO notesDAO = new NotesDAO();
                    notes.setModuleRefId(null != emailForm.getCustomerId() ? emailForm.getCustomerId().toString() : "0");
                    notesDAO.save(notes);
                }
                request.setAttribute("emailForm", emailForm);
            } else if ("estatement".equals(emailOption)) {
                File file = new File(path + "/Documents/ArCustomerStatement/" + dateFolder);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Date today = new Date();
                if (null == outputFileName) {
                    outputFileName = path;
                    fileName = outputFileName;
                    CustomerStatementBC statementBC = new CustomerStatementBC();
                    CustomerStatementPromtMessage promtMessage = new CustomerStatementPromtMessage();
//					customerStatementVO statementVO = statementBC.getCustomerStatmentObject(customerName,
//						promtMessage);
                    if (customerName != null && !customerName.equals("")) {
                        outputFileName = outputFileName + "/Documents/" + ReportConstants.ARCUSTSTATEMENT + "/" + dateFolder + customerName + ".pdf";
//						aRInvoiceBC.createEstatementReport(customerName,
//							statementVO, outputFileName, realPath,
//							fromName, exclude);
                    } else {
                        outputFileName = outputFileName + "/Documents/" + ReportConstants.ARCUSTSTATEMENT + "/" + dateFolder + ReportConstants.ALLCUSTOMERS + ".pdf";
//						aRInvoiceBC.createEstatementReport(customerName,
//							statementVO, outputFileName, realPath,
//							fromName, exclude);
                    }
                }
                EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                emailSchedulerVO.setEmailData(null, toAddress, fromName,
                        fromAddress, ccAddress, bccAddress, subject,
                        mailMessage);
                emailSchedulerVO.setEmailInfo("AR Invoice", outputFileName,
                        "Email", 0, today, "ARINQUIRY", customerName, user.getLoginName());
                new EmailschedulerDAO().save(emailSchedulerVO);
                String notesDesc = "Statement Emailed to " + toAddress;
                if (null != emailForm.getCustomerId() && emailForm.getCustomerId() != 0) {
                    Notes notes = new Notes(NotesConstants.ARCONFIGURATION, null,
                            today, NotesConstants.NOTES_TYPE_AUTO, notesDesc,
                            fromName);
                    NotesDAO notesDAO = new NotesDAO();
                    notes.setModuleRefId(null != emailForm.getCustomerId() ? emailForm.getCustomerId().toString() : "0");
                    notesDAO.save(notes);
                }
                request.setAttribute("emailForm", emailForm);

            } else {

                String[] Ids = id.split(",");
                String[] transId = transactionId.split(",");
                String inVoiceId = null;
                String transactionNo = null;
                for (int i = 0; i < Ids.length; i++) {
                    inVoiceId = Ids[i];
                    transactionNo = transId[i];
                    ArInvoice arInvoice = aRInvoiceBC.getInvoiceForEdit(
                            inVoiceId, null);
                    if (null == arInvoice) {
                        try {
                            arInvoice = aRInvoiceBC.getInvoice(new Integer(
                                    inVoiceId));
                        } catch (NumberFormatException nfe) {
                            arInvoice = null;
                        }

                    }
                    if (null != arInvoice) {
                        inVoiceId = null != arInvoice ? arInvoice.getId().toString() : "";
                        File file = new File(path + "/Documents/ArInvoice/" + dateFolder);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        if (null == outputFileName) {
                            outputFileName = path;
                            outputFileName = outputFileName + "/Documents/ArInvoice/" + dateFolder + inVoiceId + ".pdf";
                            fileName = outputFileName;
                        } else {
                            outputFileName = outputFileName + ";" + path + "/Documents/ArInvoice/" + dateFolder + inVoiceId + ".pdf";
                            fileName = path + "/Documents/ArInvoice/" + dateFolder + inVoiceId + ".pdf";
                        }
                        aRInvoiceBC.createArInvoiceReport(inVoiceId,
                                fileName, realPath);
                    }
                }
                File file = new File(path + "/Documents/ArCustomerStatement/" + dateFolder);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Date today = new Date();
                fileName = outputFileName;
                CustomerStatementBC statementBC = new CustomerStatementBC();
                CustomerStatementPromtMessage promtMessage = new CustomerStatementPromtMessage();
//				customerStatementVO statementVO = statementBC.getCustomerStatmentObject(customerName,
//					promtMessage);
                if (customerName != null && !customerName.equals("")) {
                    if (null == outputFileName) {
                        fileName = path + "/Documents/" + ReportConstants.ARCUSTSTATEMENT + "/" + dateFolder + customerName + ".pdf";
                        outputFileName = fileName;
                    } else {
                        fileName = path + "/Documents/" + ReportConstants.ARCUSTSTATEMENT + "/" + dateFolder + customerName + ".pdf";
                        outputFileName = outputFileName + ";" + fileName;
                    }

//					aRInvoiceBC.createEstatementReport(customerName, statementVO, fileName, realPath, fromName, exclude);
                } else {
                    if (null == outputFileName) {
                        fileName = path + "/Documents/" + ReportConstants.ARCUSTSTATEMENT + "/" + dateFolder + ReportConstants.ALLCUSTOMERS + ".pdf";
                        outputFileName = fileName;
                    } else {
                        fileName = path + "/Documents/" + ReportConstants.ARCUSTSTATEMENT + "/" + dateFolder + ReportConstants.ALLCUSTOMERS + ".pdf";
                        outputFileName = outputFileName + ";" + fileName;
                    }
//					aRInvoiceBC.createEstatementReport(customerName, statementVO, fileName, realPath, fromName, exclude);
                }
                EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                emailSchedulerVO.setEmailData(null, toAddress, fromName,
                        fromAddress, ccAddress, bccAddress, subject,
                        mailMessage);
                emailSchedulerVO.setEmailInfo("AR Invoice", outputFileName,
                        "Email", 0, today, "ARINQUIRY", customerName, user.getLoginName());
                new EmailschedulerDAO().save(emailSchedulerVO);
                String notesDesc = "Invoice and Statement Emailed to " + toAddress;
                if (null != emailForm.getCustomerId() && emailForm.getCustomerId() != 0) {
                    Notes notes = new Notes(NotesConstants.ARCONFIGURATION, null,
                            today, NotesConstants.NOTES_TYPE_AUTO, notesDesc,
                            fromName);
                    NotesDAO notesDAO = new NotesDAO();
                    notes.setModuleRefId(null != emailForm.getCustomerId() ? emailForm.getCustomerId().toString() : "0");
                    notesDAO.save(notes);
                }
                request.setAttribute("emailForm", emailForm);
            }
        } else if ("JournalEntry".equals(buttonAction)) {
            String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
            String folderName = null;
            String fileId = null;
            File file = new File(outputFileName + "/Documents/JournalEntry/" + dateFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
            folderName = outputFileName + "/Documents/JournalEntry/" + dateFolder;
            fileId = getFileName(folderName, id);
            outputFileName = outputFileName + "/Documents/JournalEntry/" + dateFolder + fileId + ".pdf";
            session = ((HttpServletRequest) request).getSession();
            // inserting in mail_transactions table
            Date today = new Date();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName,
                    fromAddress, ccAddress, bccAddress, subject, mailMessage);
            emailSchedulerVO.setEmailInfo(id, outputFileName, "Email",
                    0, today, "Journal Entry", id, user.getLoginName());
            JournalEntryBC journalEntryBC = new JournalEntryBC();
            journalEntryBC.save(emailSchedulerVO);
            String notesDesc = "Statement Emailed to " + toAddress;
            Notes notes = new Notes(NotesConstants.JOURNALENTRY, today,
                    NotesConstants.NOTES_TYPE_AUTO, notesDesc, fromName);
            NotesDAO notesDAO = new NotesDAO();
            notesDAO.save(notes);
            realPath = this.getServlet().getServletContext().getRealPath(
                    "/");
            journalEntryBC.createJournalEntryReport(id, outputFileName,
                    realPath);
        } else if ("Quotes".equals(buttonAction)) {
            MessageResources messageResources = getResources(request);
            String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
            String folderName = null;
            String fileId = null;
            File file = new File(outputFileName + "/Documents/Quotes/" + dateFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
            folderName = outputFileName + "/Documents/Quotes/" + dateFolder;
            fileId = getFileName(folderName, id);

            outputFileName = outputFileName + "/Documents/Quotes/" + dateFolder + fileId + ".pdf";
            session = ((HttpServletRequest) request).getSession();
            // inserting in mail_transactions table
            Date today = new Date();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName,
                    fromAddress, ccAddress, bccAddress, subject, mailMessage);
            emailSchedulerVO.setEmailInfo(id, outputFileName, "Email",
                    0, today, "Quotes", id, user.getLoginName());
            QuotationReportBC quotationReportBC = new QuotationReportBC();
            quotationReportBC.save(emailSchedulerVO);
            String notesDesc = "Statement Emailed to " + toAddress;
            Notes notes = new Notes(NotesConstants.JOURNALENTRY, today,
                    NotesConstants.NOTES_TYPE_AUTO, notesDesc, fromName);
            NotesDAO notesDAO = new NotesDAO();
            notesDAO.save(notes);
            NewQuotationReportBC newQuotationReportBC = new NewQuotationReportBC();
            if (session.getAttribute("loginuser") != null) {
                user = (com.gp.cong.logisoft.domain.User) session.getAttribute("loginuser");
            }
            realPath = this.getServlet().getServletContext().getRealPath("/");
            quotationReportBC.createQuotationPDF(id, outputFileName, realPath, messageResources, user, "", "", "", "",
                    request);
        } else if ("Booking".equals(buttonAction)) {
            MessageResources messageResources = getResources(request);
            String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
            String folderName = null;
            String fileId = null;
            File file = new File(outputFileName + "/Documents/Fcl_Booking_Confirmation/" + dateFolder);
            if (!file.exists()) {
                file.mkdirs();
            }

            folderName = outputFileName + "/Documents/Fcl_Booking_Confirmation/" + dateFolder;
            fileId = getFileName(folderName, emailForm.getSsBookingNo());
            outputFileName = outputFileName + "/Documents/Fcl_Booking_Confirmation/" + dateFolder + emailForm.getSsBookingNo() + ".pdf";
            session = ((HttpServletRequest) request).getSession();
            // inserting in mail_transactions table
            Date today = new Date();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName,
                    fromAddress, ccAddress, bccAddress, subject, mailMessage);
            emailSchedulerVO.setEmailInfo(emailForm.getSsBookingNo(), outputFileName, "Email",
                    0, today, "Booking", emailForm.getSsBookingNo(), user.getLoginName());
            BookingFclBC bookingFclBC = new BookingFclBC();
            bookingFclBC.save(emailSchedulerVO);
            String notesDesc = "Statement Emailed to " + toAddress;
            Notes notes = new Notes(NotesConstants.JOURNALENTRY, today,
                    NotesConstants.NOTES_TYPE_AUTO, notesDesc, fromName);
            NotesDAO notesDAO = new NotesDAO();
            notesDAO.save(notes);
            realPath = this.getServlet().getServletContext().getRealPath("/");
            bookingFclBC.createBookingConfirmationReport(id, outputFileName, realPath, messageResources, "", "", "", "", "", "", request);
        } else if ("Batch".equals(buttonAction)) {
            String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
            String folderName = null;
            String fileId = null;
            File file = new File(outputFileName + "/Documents/Batches/" + dateFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
            folderName = outputFileName + "/Documents/Batches/" + dateFolder;
            fileId = getFileName(folderName, id);
            outputFileName = outputFileName + "/Documents/Batches/" + dateFolder + fileId + ".pdf";
            JournalEntryBC journalEntryBC = new JournalEntryBC();
            session = ((HttpServletRequest) request).getSession();
            Date today = new Date();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName,
                    fromAddress, ccAddress, bccAddress, subject, mailMessage);
            emailSchedulerVO.setEmailInfo(id, outputFileName, "Email",
                    0, today, "Batches", id, user.getLoginName());
            journalEntryBC.save(emailSchedulerVO);
            String notesDesc = "Statement Emailed to " + toAddress;
            Notes notes = new Notes(NotesConstants.BATCH, today,
                    NotesConstants.NOTES_TYPE_AUTO, notesDesc, fromName);
            NotesDAO notesDAO = new NotesDAO();
            notesDAO.save(notes);
            realPath = this.getServlet().getServletContext().getRealPath(
                    "/");
            journalEntryBC.createJournalEntryReport(id, outputFileName,
                    realPath);
            request.setAttribute("fileName", outputFileName);

        } else if ("newMail".equals(buttonAction)) {
            // inserting in mail_transactions table
            FormFile uploadedFile = emailForm.getFile();
            String fileName = "";
            String fileLocation = "";
            if (CommonUtils.isNotEmpty(uploadedFile.getFileName())) {
                File dir = new File(path + "/Documents/admin_mail/" + dateFolder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileLocation = path + "/Documents/admin_mail/" + dateFolder + uploadedFile.getFileName();
                fileName = uploadedFile.getFileName().substring(0, uploadedFile.getFileName().indexOf('.'));
                File file = new File(fileLocation);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(uploadedFile.getFileData());
                fileOutputStream.close();
            }
            Date today = new Date();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName,
                    fromAddress, ccAddress, bccAddress, subject, mailMessage);
            emailSchedulerVO.setEmailInfo(fileName, fileLocation,
                    "Email", 0, today, "JournalEntry", "10", user.getLoginName());
            new EmailschedulerDAO().save(emailSchedulerVO);
            String notesDesc = "Statement Emailed to " + toAddress;
            Notes notes = new Notes(NotesConstants.NEW_EMAIL, today,
                    NotesConstants.NOTES_TYPE_AUTO, notesDesc, fromName);
            NotesDAO notesDAO = new NotesDAO();
            notesDAO.save(notes);
            return mapping.findForward("mailPage");
        } else if ("FiscalPeriod".equals(buttonAction)) {
            String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
            File file = new File(outputFileName + "/Documents/" + ReportConstants.INCOMESTATEMENT + "/" + dateFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
            outputFileName = outputFileName + "/Documents/" + ReportConstants.INCOMESTATEMENT + "/" + dateFolder + id + "_" + fromPeriod + "_" + toPeriod + ".pdf";
            session = ((HttpServletRequest) request).getSession();
            // inserting in mail_transactions table
            Date today = new Date();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName,
                    fromAddress, ccAddress, bccAddress, subject, mailMessage);
            emailSchedulerVO.setEmailInfo(id, outputFileName, "Email",
                    0, today, "Fiscal Period", id, user.getLoginName());
            FiscalPeriodBC fiscalPeriodBC = new FiscalPeriodBC();
            fiscalPeriodBC.save(emailSchedulerVO);
            realPath = this.getServlet().getServletContext().getRealPath(
                    "/");
            fiscalPeriodBC.createIncomeStatement(new Integer(id),
                    new Integer(fromPeriod), new Integer(toPeriod),
                    outputFileName, realPath, reportTitle, null);
        } else if ("ACCRUALS".equals(buttonAction)) {
            String message = emailForm.getBody();
            Date today = new Date();
            String moduleId = emailForm.getId();
            String moduleName = "INVOICE";
            DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
            List<DocumentStoreLog> documentList = documentStoreLogDAO.getDocumentStoreLog(moduleId, moduleName, moduleName);
            StringBuilder fileLocation = new StringBuilder();
            if (null != documentList && documentList.size() > 0) {
                for (DocumentStoreLog documentStoreLog : documentList) {
                    fileLocation.append(documentStoreLog.getFileLocation()).append(documentStoreLog.getFileName()).append(";");
                }
                EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                emailSchedulerVO.setEmailData(null, toAddress, fromName,
                        fromAddress, ccAddress, bccAddress, subject, message);
                emailSchedulerVO.setEmailInfo("ACCRUALS_INVOICE", StringUtils.removeEnd(fileLocation.toString(), ";"), CommonConstants.CONTACT_MODE_EMAIL,
                        0, today, "ACCRUALS", moduleId, user.getLoginName());
                emailSchedulerVO.setModuleId(moduleId);
                emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_DISPUTE);
                new EmailschedulerDAO().save(emailSchedulerVO);
            }
        } else if ("CreateMailForArStatement".equals(buttonAction)) {
            emailForm.setSubject("AR Statement for " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            request.setAttribute("emailForm", emailForm);
        } else if ("ArStatement".equals(buttonAction)) {
            String message = emailForm.getBody();
            String fileLocation = emailForm.getFileLocation();
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(null, toAddress, fromName, fromAddress, ccAddress, bccAddress, subject, message);
            emailSchedulerVO.setEmailInfo("ArStatement", fileLocation, CommonConstants.CONTACT_MODE_EMAIL,
                    0, new Date(), "ArStatement", "ArStatement", user.getLoginName());
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            new EmailschedulerDAO().save(emailSchedulerVO);
        } else if ("PRINTCONFIG".equals(buttonAction)) {
            session = ((HttpServletRequest) request).getSession();
            // inserting in mail_transactions table

            String message = emailForm.getBody();
            Date today = new Date();
            String moduleId = emailForm.getId();
            String screenName = emailForm.getScreenName();
            PrintConfigBC printConfigBC = new PrintConfigBC();
            List<PrintConfig> printConfigList = printConfigBC.findPrintConfigByScreenName(screenName, null, user.getUserId());
            if (null != printConfigList && !printConfigList.isEmpty()) {
                for (PrintConfig printConfig : printConfigList) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    emailSchedulerVO.setEmailData(user.getLoginName(), toAddress, fromName,
                            fromAddress, ccAddress, bccAddress, subject, message);
                    String fileLocation = null != printConfig.getFileLocation() ? printConfig.getFileLocation() : "";
                    emailSchedulerVO.setEmailInfo(screenName, fileLocation, CommonConstants.CONTACT_MODE_EMAIL,
                            0, today, screenName, moduleId, null);
                    emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                    new EmailschedulerDAO().save(emailSchedulerVO);
                    String displayMessage = "Email Sent Successfully to ";
                    if (null != toAddress && !toAddress.trim().equals("")) {
                        displayMessage = displayMessage + " " + toAddress;
                    }
                    if (null != ccAddress && !ccAddress.trim().equals("")) {
                        displayMessage = displayMessage + " ," + ccAddress;
                    }
                    if (null != bccAddress && !bccAddress.trim().equals("")) {
                        displayMessage = displayMessage + " ," + bccAddress;
                    }
                    PrintConfigForm printConfigForm = new PrintConfigForm();
                    printConfigForm.setScreenName(screenName);
                    printConfigForm.setFileLocation(fileLocation);
                    request.setAttribute("displayMessage", displayMessage);
                    request.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.findPrintConfigByScreenName(printConfigForm.getScreenName(), null, user.getUserId()));
                    request.setAttribute("printForm", printConfigForm);
                    findForward = "printListPage";
                    //return mapping.findForward(findForward);
                }
            }
        } else {
            emailForm.setCcAddress(user.getEmail());
            request.setAttribute("emailForm", emailForm);
        }

        return mapping.findForward(findForward);
    }

    public String getFileName(String folderName, String id) {
        String fileName = null;
        String fileId = null;
        fileName = folderName + id + ".pdf";
        File file = new File(fileName);
        int i = 1;
        if (!file.exists()) {
            fileId = id;
        }
        while (file.exists()) {
            fileId = id + "(" + i + ")";
            i++;
            fileName = folderName + fileId + ".pdf";
            file = new File(fileName);
        }
        return fileId;
    }
}
