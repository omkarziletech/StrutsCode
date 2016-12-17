package com.logiware.accounting.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.logiware.common.constants.Company;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.domain.EdiInvoiceLog;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.form.EdiInvoiceForm;
import com.logiware.accounting.model.AccrualModel;
import com.logiware.accounting.model.VendorModel;
import com.logiware.accounting.reports.EdiInvoiceCreator;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.accounting.domain.EdiInvoiceDetail;
import com.logiware.utils.AuditNotesUtils;
import com.logiware.utils.ExceptionUtils;
import com.oreilly.servlet.ServletUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceUtils {

    public static void createInvoiceFromXml(File file, Company company, String contextPath) throws Exception {
        InputStream inputStream = null;
        try {
            EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
            EdiInvoiceLog ediInvoiceLog = new EdiInvoiceLog();
            ediInvoiceLog.setCompany(company);
            ediInvoiceLog.setFileName(file.getName());
            inputStream = new FileInputStream(file);
            ediInvoiceLog.setFile(IOUtils.toByteArray(inputStream));
            try {
                EdiInvoice ediInvoice = new EdiInvoice(file, company);
                ediInvoice.setEdiInvoiceLog(ediInvoiceLog);
                ediInvoiceLog.setEdiInvoice(ediInvoice);
                ediInvoiceLog.setType("processed");
                String invoiceNo = ediInvoice.getInvoiceNumber();
                if (CommonUtils.isNotEmpty(ediInvoice.getVendorNumber())) {
                    List<String> bls = new ArrayList<String>();
                    for (EdiInvoiceDetail charges : ediInvoice.getEdiInvoiceDetails()) {
                        if (ediInvoiceDAO.isAtleastOneApproved(charges.getBlReference()) && !bls.contains(charges.getBlReference())) {
                            bls.add(charges.getBlReference());
                            doMailTransaction(invoiceNo, charges.getBlReference());
                        }
                    }
                    String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
                    ediInvoiceDAO.copyDocuments(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), fileName);
                    StringBuilder desc = new StringBuilder();
                    desc.append("EDI Invoice - ").append(ediInvoice.getInvoiceNumber());
                    desc.append(" initially created by system");
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    String key = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, null);
                }
            } catch (AccountingException e) {
                ediInvoiceLog.setType("error");
                ediInvoiceLog.setError(e.getMessage());
            } catch (Exception e) {
                ediInvoiceLog.setType("error");
                ediInvoiceLog.setError(ExceptionUtils.getStackTrace(e));
            }
            ediInvoiceDAO.save(ediInvoiceLog);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void search(EdiInvoiceForm ediInvoiceForm) throws Exception {
        new EdiInvoiceDAO().search(ediInvoiceForm);
    }

    public static void showAccruals(EdiInvoiceForm ediInvoiceForm, HttpServletRequest request) throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(ediInvoiceForm.getId());
        request.setAttribute("invoice", ediInvoice.getInvoiceNumber());
        request.setAttribute("accruals", ediInvoiceDAO.getAccruals(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber()));
    }

    public static void showLogs(HttpServletRequest request) throws Exception {
        request.setAttribute("logs", new EdiInvoiceDAO().getLogs("error"));
    }

    public static void updateVendor(EdiInvoiceForm ediInvoiceForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(ediInvoiceForm.getId());
        String oldVendorName = ediInvoice.getVendorName();
        String oldVendorNumber = ediInvoice.getVendorNumber();
        String newVendorNumber = ediInvoiceForm.getVendorNumber();
        if (CommonUtils.isNotEqual(oldVendorNumber, newVendorNumber)) {
            String newVendorName = ediInvoiceForm.getVendorName();
            StringBuilder desc = new StringBuilder("Vendor");
            if (CommonUtils.isNotEmpty(oldVendorNumber)) {
                desc.append(" changed from ").append(oldVendorName).append(" (").append(oldVendorNumber).append(")");
                desc.append(" to ").append(newVendorName).append(" (").append(newVendorNumber).append(")");
            } else {
                desc.append(" - ").append(newVendorName).append(" (").append(newVendorNumber).append(")");
                desc.append(" added");
            }
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            String key = newVendorNumber + "-" + ediInvoice.getInvoiceNumber();
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, user);
        }
        ediInvoice.setVendorName(ediInvoiceForm.getVendorName());
        ediInvoice.setVendorNumber(ediInvoiceForm.getVendorNumber());
        ediInvoiceDAO.updateVendor(ediInvoice, ediInvoiceForm.getVendorNumber(), ediInvoiceForm.getVendorName());
        PrintWriter out = response.getWriter();
        out.print("Invoice is updated successfully with vendor information");
        out.flush();
        out.close();
    }

    public static void printInvoice(EdiInvoiceForm ediInvoiceForm, String contextPath, HttpServletResponse response) throws Exception {
        EdiInvoice ediInvoice = new EdiInvoiceDAO().findById(ediInvoiceForm.getId());
        String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
        //Put the pdf file in response as inline document
        response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(fileName));
        response.setContentType("application/pdf;charset=utf-8");
        ServletUtils.returnFile(fileName, response.getOutputStream());
    }

    public static boolean postToAp(EdiInvoiceForm ediInvoiceForm, String contextPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(ediInvoiceForm.getId());
        List<AccrualModel> accruals = ediInvoiceDAO.getInvalidAccruals(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber());
        if (CommonUtils.isNotEmpty(accruals)) {
            request.setAttribute("invoice", ediInvoice.getInvoiceNumber());
            request.setAttribute("accruals", accruals);
            return false;
        } else {
            User user = (User) request.getSession().getAttribute("loginuser");
            double accrualsAmount = ediInvoiceDAO.getAccrualsAmount(ediInvoice);
            PrintWriter out = response.getWriter();
            if (CommonUtils.isEqual(ediInvoice.getInvoiceAmount(), accrualsAmount)) {
                new AccrualsDAO().postToAp(ediInvoice, user);
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_POSTED_TO_AP);
                String documentId = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
                String screenName = "INVOICE";
                String documentName = "INVOICE";
                if (!new DocumentStoreLogDAO().isUploaded(documentId, screenName, documentName)) {
                    String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
                    ediInvoiceDAO.copyDocuments(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), fileName);
                }
                out.print("success");
            } else {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_IN_PROGRESS);
                out.print(ConstantsInterface.STATUS_EDI_IN_PROGRESS);
            }
            ediInvoiceDAO.update(ediInvoice);
            out.flush();
            out.close();
            return true;
        }
    }

    public static void deriveGlAccount(String account, String suffix, String shipmentType, String terminal, HttpServletResponse response) throws Exception {
        String glAccount = new AccrualsDAO().deriveGlAccount(account, suffix, shipmentType, terminal);
        PrintWriter out = response.getWriter();
        out.print(glAccount);
        out.flush();
        out.close();
    }

    public static void updateAccrual(EdiInvoiceForm ediInvoiceForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        new AccrualsDAO().updateAccrual(ediInvoiceForm, user);
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        Integer invoiceId = Integer.parseInt(request.getParameter("invoiceId"));
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(invoiceId);
        double accrualsAmount = ediInvoiceDAO.getAccrualsAmount(ediInvoice);
        if (CommonUtils.isEqual(ediInvoice.getInvoiceAmount(), accrualsAmount)) {
            ediInvoiceDAO.setAccrualsInProgress(ediInvoice);
            if (ediInvoiceDAO.isAccrualsFullyMapped(ediInvoice)) {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_READY_TO_POST_FULLY_MAPPED);
            } else {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_READY_TO_POST);
            }
        } else {
            ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_IN_PROGRESS);
        }
        PrintWriter out = response.getWriter();
        out.print("Accrual is updated successfully");
        out.flush();
        out.close();

    }

    public static void removeAccrual(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer invoiceId = Integer.parseInt(request.getParameter("invoiceId"));
        User user = (User) request.getSession().getAttribute("loginuser");
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(invoiceId);
        TransactionLedger accrual = new AccrualsDAO().findById(id);
        accrual.setInvoiceNumber(null);
        accrual.setStatus(ConstantsInterface.STATUS_OPEN);
        accrual.setUpdatedOn(new Date());
        accrual.setUpdatedBy(user.getUserId());
        StringBuilder desc = new StringBuilder("Accrual of");
        boolean addAnd = false;
        if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
            desc.append(" B/L - ").append(accrual.getBillLaddingNo());
            addAnd = true;
        }
        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
            desc.append(addAnd ? " and" : "").append(" Doc Receipt - ").append(accrual.getDocReceipt());
            addAnd = true;
        }
        if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
            desc.append(addAnd ? " and" : "").append(" Voyage - ").append(accrual.getVoyageNo());
        }
        desc.append(" for amount ").append(NumberUtils.formatNumber(accrual.getTransactionAmt()));
        desc.append(" is ummarked as EDI In Progress");
        desc.append(" from Invoice - ").append(ediInvoice.getInvoiceNumber());
        desc.append(" by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        String key = accrual.getTransactionId().toString();
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, user);
        double accrualsAmount = ediInvoiceDAO.getAccrualsAmount(ediInvoice);
        if (CommonUtils.isEqual(ediInvoice.getInvoiceAmount(), accrualsAmount)) {
            ediInvoiceDAO.setAccrualsInProgress(ediInvoice);
            if (ediInvoiceDAO.isAccrualsFullyMapped(ediInvoice)) {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_READY_TO_POST_FULLY_MAPPED);
            } else {
                ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_READY_TO_POST);
            }
        } else {
            ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_IN_PROGRESS);
        }
        PrintWriter out = response.getWriter();
        out.print("Accrual is removed successfully");
        out.flush();
        out.close();

    }

    public static void archiveInvoice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(id);
        if (CommonUtils.isNotEqualIgnoreCase(ediInvoice.getStatus(), ConstantsInterface.STATUS_EDI_DUPLICATE)) {
            ediInvoiceDAO.setAccrualsOpen(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber());
        }
        ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_ARCHIVE);
        User user = (User) request.getSession().getAttribute("loginuser");
        StringBuilder desc = new StringBuilder();
        desc.append("EDI Invoice - ").append(ediInvoice.getInvoiceNumber());
        desc.append(" archived by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        String key = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, user);
        PrintWriter out = response.getWriter();
        out.print("Invoice is archived successfully");
        out.flush();
        out.close();
    }

    public static void removeLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        User user = (User) request.getSession().getAttribute("loginuser");
        EdiInvoiceLog ediInvoiceLog = new EdiInvoiceDAO().getEdiInvoiceLog(id);
        StringBuilder desc = new StringBuilder();
        desc.append("Log of file -").append(ediInvoiceLog.getFileName());
        desc.append(" removed by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        String key = ediInvoiceLog.getId().toString();
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, null);
        ediInvoiceLog.setType("removed");
        PrintWriter out = response.getWriter();
        out.print("Log is removed successfully");
        out.flush();
        out.close();

    }

    public static void viewEdiFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        EdiInvoiceLog ediInvoiceLog = new EdiInvoiceDAO().getEdiInvoiceLog(id);
        //Put the xml file in response as inline document
        response.addHeader("Content-Disposition", "inline; filename=" + ediInvoiceLog.getFileName());
        String contentType = CommonUtils.getMimeType(ediInvoiceLog.getFileName(), ediInvoiceLog.getFile());
        response.setContentType(contentType + ";charset=utf-8");
        IOUtils.write(ediInvoiceLog.getFile(), response.getOutputStream());
    }

    public static void updateEdiCode() throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        List<EdiInvoice> invoices = ediInvoiceDAO.getEmptyEdiCodeInvoices();
        for (EdiInvoice ediInvoice : invoices) {
            InputStream inputStream = null;
            boolean isHeader = false;
            String elementType = null;
            String characterType = null;
            XMLEventReader eventReader = null;
            try {
                EdiInvoiceLog log = ediInvoice.getEdiInvoiceLog();
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                inputStream = new ByteArrayInputStream(log.getFile());
                eventReader = inputFactory.createXMLEventReader(inputStream);
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        StartElement startElement = event.asStartElement();
                        if ("Header".equalsIgnoreCase(startElement.getName().toString())) {
                            isHeader = true;
                        } else if (null == elementType && isHeader && "Sender".equalsIgnoreCase(startElement.getName().toString())) {
                            elementType = "Sender";
                        } else if (null != elementType && null == characterType && isHeader
                                && "Sender".equals(elementType) && "Code".equalsIgnoreCase(startElement.getName().toString())) {
                            characterType = "Code";
                        }
                    } else if (event.isCharacters()) {
                        if (isHeader && "Sender".equals(elementType) && "Code".equals(characterType)) {
                            Characters text = event.asCharacters();
                            ediInvoice.setEdiCode(text.getData());
                            if (CommonUtils.isNotEmpty(ediInvoice.getEdiCode())) {
                                VendorModel vendor = ediInvoiceDAO.getVendor(ediInvoice.getEdiCode());
                                if (null != vendor && CommonUtils.isNotEmpty(vendor.getVendorNumber())) {
                                    ediInvoice.setVendorNumber(vendor.getVendorNumber());
                                    ediInvoice.setVendorName(vendor.getVendorName());
                                }
                                ediInvoiceDAO.update(ediInvoice);
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (null != eventReader) {
                    eventReader.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        }
    }

    public static void updateInvoiceAmount() throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        List<EdiInvoice> invoices = ediInvoiceDAO.getAllOpenInvoices();
        for (EdiInvoice ediInvoice : invoices) {
            InputStream inputStream = null;
            boolean isBody = false;
            boolean isInformation = false;
            boolean isDetails = false;
            boolean isSummary = false;
            String elementType = null;
            String characterType = null;
            XMLEventReader eventReader = null;
            try {
                EdiInvoiceLog log = ediInvoice.getEdiInvoiceLog();
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                inputStream = new ByteArrayInputStream(log.getFile());
                eventReader = inputFactory.createXMLEventReader(inputStream);
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        StartElement startElement = event.asStartElement();
                        if ("Body".equalsIgnoreCase(startElement.getName().toString())) {
                            isBody = true;
                        } else if (isBody && "Information".equalsIgnoreCase(startElement.getName().toString())) {
                            isInformation = true;
                        } else if (isBody && !isInformation && "Details".equalsIgnoreCase(startElement.getName().toString())) {
                            isDetails = true;
                        } else if (isBody && !isInformation && !isDetails && "Summary".equalsIgnoreCase(startElement.getName().toString())) {
                            isSummary = true;
                        }
                        if (null == elementType && isSummary && "TotalMonetaryAmount".equalsIgnoreCase(startElement.getName().toString())) {
                            elementType = "TotalMonetaryAmount";
                        } else if (null != elementType && null == characterType && isSummary
                                && "TotalMonetaryAmount".equals(elementType) && "TotalVATIncl".equalsIgnoreCase(startElement.getName().toString())) {
                            characterType = "TotalVATIncl";
                        }
                    } else if (event.isCharacters()) {
                        if (isBody && "TotalMonetaryAmount".equals(elementType) && "TotalVATIncl".equals(characterType)) {
                            Characters text = event.asCharacters();
                            ediInvoice.setInvoiceAmount(NumberUtils.parseNumber(text.getData()));
                            ediInvoiceDAO.update(ediInvoice);
                            break;
                        }
                    } else if (event.isEndElement()) {
                        EndElement endElement = event.asEndElement();
                        if (isDetails && "Details".equalsIgnoreCase(endElement.getName().toString())) {
                            isDetails = false;
                        } else if (isBody && "Information".equalsIgnoreCase(endElement.getName().toString())) {
                            isInformation = false;
                        }
                    }
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (null != eventReader) {
                    eventReader.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        }
    }

    public static void attachInvoices(EdiInvoiceForm ediInvoiceForm, String contextPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        List<EdiInvoice> ediInvoices = ediInvoiceDAO.getAllInvoices();
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        String screenName = "INVOICE";
        String documentName = "INVOICE";
        for (EdiInvoice ediInvoice : ediInvoices) {
            String documentId = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
            documentStoreLogDAO.deleteDocuments(screenName, documentName, documentId);
            String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
            ediInvoiceDAO.copyDocuments(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), fileName);
        }
    }

    private static void doMailTransaction(String invoiceId, String blNo) throws Exception {
        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
        String to = invoiceDao.getVoyageOwnerEmail(invoiceId, blNo);
        String cc = null;
        String htmlMsg = "";
        StringBuilder textMsg = new StringBuilder();
        String coverLetter = null;
        String printerName = null;
        String responseCode = null;
        EmailschedulerDAO emailDao = new EmailschedulerDAO();
        Date today = new Date();
        textMsg.append("Hi,").append("\n");
        textMsg.append("New Invoice in System for the HBL reference number : ").append(blNo);
        emailDao.saveMailTransactions("Invoice", "",
                "Email", "Pending", today, to,
                "", cc, "New Invoice in System ", htmlMsg,
                textMsg.toString(), "Invoice", "",
                "", coverLetter, printerName, 1, responseCode);
    }
}
