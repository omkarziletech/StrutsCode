package com.logiware.accounting.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.hibernate.dao.CreditDebitNoteDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.lcl.report.FreightInvoiceLclPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclArInvoicePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclCorrectionDebitCreditPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.reports.ArRedInvoicePdfCreator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.logiware.accounting.dao.ArInquiryDAO;
import com.logiware.accounting.form.ArInquiryForm;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.accounting.form.SessionForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.excel.ExportNSInvoiceToExcel;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.dao.TerminalGlMappingDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.TerminalGlMapping;
import com.logiware.reports.NSInvoiceReportCreator;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArInquiryAction extends BaseAction {

    private static final String CHARGES = "charges";
    private static final String TRANSACTIONS = "transactions";
    private static final String MORE_INFO = "moreInfo";
    private static final String STATEMENT = "statement";
    private static final String PAYMENTS = "payments";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        SessionForm oldArInquiryForm = new SessionForm();
        PropertyUtils.copyProperties(oldArInquiryForm, arInquiryForm);
        request.getSession().setAttribute("oldArInquiryForm", oldArInquiryForm);
        new ArInquiryDAO().search(arInquiryForm);
        String adjustmentThreshold = LoadLogisoftProperties.getProperty("arInquiryAdjustAmountThresHold");
        request.setAttribute("adjustmentThreshold", Double.parseDouble(adjustmentThreshold));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward changeView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        SessionForm oldArInquiryForm = (SessionForm) session.getAttribute("oldArInquiryForm");
        PropertyUtils.copyProperties(arInquiryForm, oldArInquiryForm);
        arInquiryForm.setAccountType(request.getParameter("accountType"));
        oldArInquiryForm.setAccountType(request.getParameter("accountType"));
        request.getSession().setAttribute("oldArInquiryForm", oldArInquiryForm);
        new ArInquiryDAO().search(arInquiryForm);
        request.setAttribute("arInquiryForm", arInquiryForm);
        String adjustmentThreshold = LoadLogisoftProperties.getProperty("arInquiryAdjustAmountThresHold");
        request.setAttribute("adjustmentThreshold", Double.parseDouble(adjustmentThreshold));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward sortingAndPaging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        SessionForm oldArInquiryForm = (SessionForm) session.getAttribute("oldArInquiryForm");
        PropertyUtils.copyProperties(arInquiryForm, oldArInquiryForm);
        arInquiryForm.setSortBy(request.getParameter("sortBy"));
        arInquiryForm.setOrderBy(request.getParameter("orderBy"));
        arInquiryForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
        new ArInquiryDAO().search(arInquiryForm);
        request.setAttribute("arInquiryForm", arInquiryForm);
        String adjustmentThreshold = LoadLogisoftProperties.getProperty("arInquiryAdjustAmountThresHold");
        request.setAttribute("adjustmentThreshold", Double.parseDouble(adjustmentThreshold));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("arInquiryForm", new ArInquiryForm());
        request.getSession().removeAttribute("oldArInquiryForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        ArInquiryForm newArInquiryForm = new ArInquiryForm();
        newArInquiryForm.setCustomerName(arInquiryForm.getCustomerName());
        newArInquiryForm.setCustomerNumber(arInquiryForm.getCustomerNumber());
        newArInquiryForm.setDisabled(arInquiryForm.isDisabled());
        newArInquiryForm.setShowAllAccounts(arInquiryForm.isShowAllAccounts());
        newArInquiryForm.setToggled(arInquiryForm.isToggled());
        request.setAttribute("arInquiryForm", newArInquiryForm);
        request.getSession().removeAttribute("oldArInquiryForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        String source = arInquiryForm.getSource();
        String customerNumber = arInquiryForm.getCustomerNumber();
        String blNumber = arInquiryForm.getBlNumber();
        String invoiceNumber = arInquiryForm.getInvoiceNumber();
        String correctionNotice = arInquiryForm.getCorrectionNotice();
        List<ResultModel> charges = new ArInquiryDAO().getCharges(source, customerNumber, blNumber, invoiceNumber, correctionNotice);
        request.setAttribute("charges", charges);
        return mapping.findForward(CHARGES);
    }

    public ActionForward showTransactions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        String source = arInquiryForm.getSource();
        Integer id = arInquiryForm.getId();
        String customerNumber = arInquiryForm.getCustomerNumber();
        String blNumber = arInquiryForm.getBlNumber();
        String invoiceNumber = arInquiryForm.getInvoiceNumber();
        String correctionNotice = arInquiryForm.getCorrectionNotice();
        ArInquiryDAO arInquiryDAO = new ArInquiryDAO();
        List<ResultModel> postedTransactions = arInquiryDAO.getPostedTransactions(source, customerNumber, blNumber, invoiceNumber);
        List<ResultModel> unpostedTransactions = arInquiryDAO.getUnpostedTransactions(source, id);
        request.setAttribute("postedTransactions", postedTransactions);
        request.setAttribute("unpostedTransactions", unpostedTransactions);
        return mapping.findForward(TRANSACTIONS);
    }

    public ActionForward showMoreInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        String source = arInquiryForm.getSource();
        Integer id = arInquiryForm.getId();
        ResultModel ar = new ArInquiryDAO().getMoreInfo(source, id);
        request.setAttribute("ar", ar);
        return mapping.findForward(MORE_INFO);
    }

    public ActionForward sendStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        String customerNumber = arInquiryForm.getCustomerNumber();
        String customerName = arInquiryForm.getCustomerName();
        ArReportsForm arReportsForm = new ArReportsForm();
        arReportsForm.setCustomerNumber(customerNumber);
        arReportsForm.setCustomerName(customerName);
        arReportsForm.setExcludeIds(arInquiryForm.getExcludeIds());
        arReportsForm.setNetsett(YES);
        arReportsForm.setPrepayments(YES);
        arReportsForm.setCreditStatement(true);
        arReportsForm.setCreditInvoice(true);
        arReportsForm.setAction("FromArInquiry");
        request.setAttribute("arReportsForm", arReportsForm);
        return mapping.findForward(STATEMENT);
    }

    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        HttpSession session = request.getSession();
        FclBlBC fclBlBC = new FclBlBC();
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        FclBlDAO fclBlDAO = new FclBlDAO();
        ArRedInvoicePdfCreator arRedInvoicePdfCreator = new ArRedInvoicePdfCreator();
        LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
        String contextPath = this.getServlet().getServletContext().getRealPath("/");
        User user = (User) session.getAttribute("loginuser");
        MessageResources messageResources = CommonConstants.loadMessageResources();

        StringBuilder blFolderBuilder = new StringBuilder();
        blFolderBuilder.append(LoadLogisoftProperties.getProperty("reportLocation"));
        blFolderBuilder.append("/Documents/").append(ReportConstants.BILLOFLADINGFILENAME).append("/");
        blFolderBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
        File blFolder = new File(blFolderBuilder.toString());
        if (!blFolder.exists()) {
            blFolder.mkdirs();
        }
        StringBuilder lclFolderBuilder = new StringBuilder();
        lclFolderBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
        lclFolderBuilder.append(LclReportConstants.FOLDER_NAME).append("/");
        lclFolderBuilder.append(LclReportConstants.MODULENAME).append("/");
        lclFolderBuilder.append(ReportConstants.FRIEGHTINVOICE).append("/");
        lclFolderBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
        File lclFolder = new File(lclFolderBuilder.toString());
        if (!lclFolder.exists()) {
            lclFolder.mkdirs();
        }

        StringBuilder invoiceFolderBuilder = new StringBuilder();
        invoiceFolderBuilder.append(LoadLogisoftProperties.getProperty("reportLocation"));
        invoiceFolderBuilder.append("/Documents/RedInvoice/");
        invoiceFolderBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
        File invoiceFolder = new File(invoiceFolderBuilder.toString());
        if (!invoiceFolder.exists()) {
            invoiceFolder.mkdirs();
        }

        List<ResultModel> invoices = new ArInquiryDAO().getInvoices(arInquiryForm.getEmailIds());
        List<String> files = new ArrayList<String>();
        for (ResultModel invoice : invoices) {
            if (CommonUtils.isEqualIgnoreCase(invoice.getSource(), "FCL")) {
                String blNumber = "";
                if(CommonUtils.isEqualIgnoreCase(invoice.getCorrectionFlag(),"Y")){
                blNumber = new FclBlDAO().getCorrectedBolId(StringUtils.contains(invoice.getDockReceipt(), "CN")?
                        invoice.getDockReceipt().substring(0, 6):invoice.getDockReceipt());    
                }else{
                 blNumber = invoice.getBlNumber();
                }
                String drcpt = invoice.getDockReceipt();
                int fileIndex = blNumber.indexOf(drcpt) + drcpt.length();
                blNumber = blNumber.substring(0, fileIndex) + blNumber.substring(fileIndex).replace("CN", "==");
                FclBl bl = fclBlDAO.findById(blNumber);
                if (null != bl) {
                    String documentName;
                    if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getAgentNo())) {
                        documentName = PrintReportsConstants.FREIGHT_INVOICE_AGENT;
                    } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getBillTrdPrty())) {
                        documentName = PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY;
                    } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getForwardAgentNo())) {
                        documentName = PrintReportsConstants.FREIGHT_INVOICE_FORWARDER;
                    } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getShipperNo())) {
                        documentName = PrintReportsConstants.FREIGHT_INVOICE_SHIPPER;
                    } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getConsigneeNo())) {
                        documentName = PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE;
                    } else {
                        documentName = PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY;
                    }
                    String fileNo = StringUtils.contains(invoice.getDockReceipt(), "CN")?
                        invoice.getDockReceipt().substring(0, 6):invoice.getDockReceipt();
                    StringBuilder fileBuilder = new StringBuilder();
                    fileBuilder.append(blFolderBuilder.toString());
                    fileBuilder.append("/").append(documentName).append("_").append(fileNo).append(".pdf");
                    fclBlBC.createFclBillLadingReport(blNumber, fileBuilder.toString(), contextPath, messageResources, user, documentName);
                    files.add(fileBuilder.toString());
                    if (CommonUtils.isEqualIgnoreCase("I", bl.getImportFlag())) {
                        documentName = PrintReportsConstants.FCL_ARRIVAL_NOTICE;
                        fileBuilder = new StringBuilder();
                        fileBuilder.append(blFolderBuilder.toString());
                        fileBuilder.append("/").append(documentName).append("_").append(fileNo).append(".pdf");
                        fclBlBC.createFclBillLadingReport(blNumber, fileBuilder.toString(), contextPath, messageResources, user, documentName);
                        files.add(fileBuilder.toString());
                    }
                }
            } else if (CommonUtils.isEqualIgnoreCase(invoice.getSource(), "LCL")) {
                if (invoice.getBlNumber() == null) {// this is for Ar-invoice
                    ArRedInvoice arRedInvoice = arRedInvoiceDAO.getInvoice(invoice.getCustomerNumber(), invoice.getInvoiceNumber());
                    StringBuilder fileBuilder = new StringBuilder();
                    fileBuilder.append(invoiceFolderBuilder.toString());
                    fileBuilder.append("/Invoice_").append(arRedInvoice.getInvoiceNumber()).append(".pdf");
                    lclArInvoicePdfCreator.createReport(arRedInvoice, fileBuilder.toString(), contextPath, user, null, null, false);
                    files.add(fileBuilder.toString());
                } else if (invoice.getBlNumber() != null && invoice.getBlNumber().contains("CN")) {// this is for LclCorrection
                    LclContact lclContact = null;
                    LclUtils lclUtils = new LclUtils();
                    LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
                    String fileNo = invoice.getDockReceipt();
                    String correctionNo = invoice.getBlNumber().substring(invoice.getBlNumber().indexOf("CN") + 2, invoice.getBlNumber().length());
                    Long correctionId = new LCLCorrectionDAO().getLclCorrectionId(fileNo, correctionNo);
                    Long fileId = new LclFileNumberDAO().getFileIdByFileNumber(fileNo);
                    LCLCorrectionNoticeBean lclCorrectionNoticeBean = new LCLCorrectionDAO().getAllCorrectionByFileIdReports(String.valueOf(fileId), correctionId, "Imports");
                    String debitOrCreditNote = new CreditDebitNoteDAO().getDebitorCredit(fileNo, correctionNo);
                    lclCorrectionNoticeBean.setDebitOrCreditNote(debitOrCreditNote);
                    lclCorrectionNoticeBean.setCorrectionNo(correctionNo);
                    List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getLclChargeByFileNumberAsc(fileId);
                    LclBookingAc lclBookingAc = lclBookingAcList.get(0);
                    if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("C")) {
                        lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getConsContact();
                    } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("A")) {
                        lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getAgentContact();
                    } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("N")) {
                        lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getNotyContact();
                    } else {
                        lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getThirdPartyContact();
                    }
                    lclCorrectionNoticeBean.setBillToPartyAddress(lclUtils.getConcatenatedLclContactAddress(lclContact));
                    request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionChargesPdf(correctionId));
                    StringBuilder fileBuilder = new StringBuilder();
                    fileBuilder.append(lclFolderBuilder.toString());
                    fileBuilder.append("/").append("LCL-").append(invoice.getBlNumber()).append("-").append(debitOrCreditNote).append(".pdf");

                    LclCorrectionDebitCreditPdfCreator lclCorrectionDebitORCreditNoteReportPdfCreator = new LclCorrectionDebitCreditPdfCreator();
                    lclCorrectionDebitORCreditNoteReportPdfCreator.createReport(lclCorrectionNoticeBean, fileBuilder.toString(), contextPath, request, fileId, "Imports");
                    files.add(fileBuilder.toString());
                } else {// this is for Freight-invoice
                    FreightInvoiceLclPdfCreator freightInvoiceLclPdfCreator = new FreightInvoiceLclPdfCreator();
                    LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
                    String fileNo = invoice.getDockReceipt();
                    Long fileId = lclFileNumberDAO.getFileIdByFileNumber(fileNo);
                    StringBuilder fileBuilder = new StringBuilder();
                    fileBuilder.append(lclFolderBuilder.toString());
                    fileBuilder.append("/").append(fileNo).append("-").append("FreightInvoice").append(".pdf");
                    freightInvoiceLclPdfCreator.setRealPath(contextPath);
                    freightInvoiceLclPdfCreator.createPdf(contextPath, null, fileId.toString(), fileNo, fileBuilder.toString(), ReportConstants.FRIEGHTINVOICE, null, user);
                    files.add(fileBuilder.toString());
                }
            } else if (CommonUtils.isEqualIgnoreCase(invoice.getSource(), "INV")) {
                ArRedInvoice arRedInvoice = arRedInvoiceDAO.getInvoice(invoice.getCustomerNumber(), invoice.getInvoiceNumber());
                if (null != arRedInvoice) {
                    StringBuilder fileBuilder = new StringBuilder();
                    fileBuilder.append(invoiceFolderBuilder.toString());
                    fileBuilder.append("/Invoice_").append(arRedInvoice.getInvoiceNumber()).append(".pdf");
                    if ((arRedInvoice.getScreenName().equalsIgnoreCase("IMP VOYAGE")) || (arRedInvoice.getScreenName().equalsIgnoreCase("LCLI DR"))
                            || (arRedInvoice.getScreenName().equalsIgnoreCase("EXP VOYAGE")) || (arRedInvoice.getScreenName().equalsIgnoreCase("LCLE DR"))) {
                        lclArInvoicePdfCreator.createReport(arRedInvoice, fileBuilder.toString(), contextPath, user, null, null, false);
                    } else {
                        arRedInvoicePdfCreator.createReport(arRedInvoice, fileBuilder.toString(), contextPath, messageResources, user);
                    }
                    files.add(fileBuilder.toString());
                }
            }
        }
        if (CommonUtils.isNotEmpty(files)) {
            Long totalSize = 0L;
            for (String fileName : files) {
                File file = new File(fileName);
                totalSize += file.length();
            }
            double size = Math.round((double) totalSize / (1024 * 1024));
            if (size > 5D) {
                PrintWriter out = response.getWriter();
                out.print("Cannot send email. Attached files are too big (" + size + "). It should be < 5MB.");
                out.flush();
                out.close();
                return null;
            }
            String fileLocation = files.toString().replace("[", "").replace("]", "").replace(", ", ";");
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setType(CONTACT_MODE_EMAIL);
            emailSchedulerVO.setName("BL");
            emailSchedulerVO.setFileLocation(fileLocation);
            emailSchedulerVO.setStatus(EMAIL_STATUS_PENDING);
            emailSchedulerVO.setNoOfTries(0);
            emailSchedulerVO.setEmailDate(new Date());
            emailSchedulerVO.setModuleName("BL");
            emailSchedulerVO.setModuleId(user.getLoginName());
            emailSchedulerVO.setUserName(user.getLoginName());
            emailSchedulerVO.setToName(null);
            emailSchedulerVO.setToAddress(arInquiryForm.getToAddress());
            emailSchedulerVO.setFromName(user.getFirstName());
            emailSchedulerVO.setFromAddress(user.getEmail());
            emailSchedulerVO.setCcAddress(arInquiryForm.getCcAddress());
            emailSchedulerVO.setBccAddress(arInquiryForm.getBccAddress());
            emailSchedulerVO.setSubject(arInquiryForm.getSubject());
            emailSchedulerVO.setTextMessage(CommonUtils.isNotEmpty(arInquiryForm.getBody()) ? arInquiryForm.getBody() : "");
            emailSchedulerVO.setHtmlMessage(CommonUtils.isNotEmpty(arInquiryForm.getBody()) ? arInquiryForm.getBody() : "");
            new EmailschedulerDAO().save(emailSchedulerVO);
        } else {
            PrintWriter out = response.getWriter();
            out.print("Cannot send email. No files attached.");
            out.flush();
            out.close();
        }
        PrintWriter out = response.getWriter();
        out.print("Email is sent successfully.");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward disputeInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        FclBlBC fclBlBC = new FclBlBC();
        FclBlDAO fclBlDAO = new FclBlDAO();
        NotesDAO notesDAO = new NotesDAO();
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        ArRedInvoicePdfCreator arRedInvoicePdfCreator = new ArRedInvoicePdfCreator();
        String contextPath = this.getServlet().getServletContext().getRealPath("/");
        User user = (User) session.getAttribute("loginuser");
        MessageResources messageResources = CommonConstants.loadMessageResources();

        StringBuilder blFolderBuilder = new StringBuilder();
        blFolderBuilder.append(LoadLogisoftProperties.getProperty("reportLocation"));
        blFolderBuilder.append("/Documents/").append(ReportConstants.BILLOFLADINGFILENAME).append("/");
        blFolderBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
        File blFolder = new File(blFolderBuilder.toString());
        if (!blFolder.exists()) {
            blFolder.mkdirs();
        }
        StringBuilder lclFolderBuilder = new StringBuilder();
        lclFolderBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
        lclFolderBuilder.append(LclReportConstants.FOLDER_NAME).append("/");
        lclFolderBuilder.append(LclReportConstants.MODULENAME).append("/");
        lclFolderBuilder.append(ReportConstants.FRIEGHTINVOICE).append("/");
        lclFolderBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
        File lclFolder = new File(lclFolderBuilder.toString());
        if (!lclFolder.exists()) {
            lclFolder.mkdirs();
        }
        StringBuilder invoiceFolderBuilder = new StringBuilder();
        invoiceFolderBuilder.append(LoadLogisoftProperties.getProperty("reportLocation"));
        invoiceFolderBuilder.append("/Documents/RedInvoice/");
        invoiceFolderBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
        File invoiceFolder = new File(invoiceFolderBuilder.toString());
        if (!invoiceFolder.exists()) {
            invoiceFolder.mkdirs();
        }
        Transaction ar = transactionDAO.findById(id);
        String documentId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
        List<String> files = new ArrayList<String>();
        String screenName = "INVOICE";
        String documentName = "INVOICE";
        String fileLocation = new DocumentStoreLogDAO().getFiles(documentId, screenName, documentName);
        if (CommonUtils.isNotEmpty(fileLocation)) {
            files.addAll(Arrays.asList(fileLocation.split(";")));
        }
        List<ResultModel> invoices = new ArInquiryDAO().getInvoices(id);
        ResultModel invoice = invoices.get(0);
        if (CommonUtils.isEqualIgnoreCase(invoice.getSource(), "FCL")) {
            String blNumber = invoice.getBlNumber();
            String drcpt = invoice.getDockReceipt();
            int fileIndex = blNumber.indexOf(drcpt) + drcpt.length();
            blNumber = blNumber.substring(0, fileIndex) + blNumber.substring(fileIndex).replace("CN", "==");
            FclBl bl = fclBlDAO.findById(blNumber);
            if (null != bl) {
                String reportName;
                if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getAgentNo())) {
                    reportName = PrintReportsConstants.FREIGHT_INVOICE_AGENT;
                } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getBillTrdPrty())) {
                    reportName = PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY;
                } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getForwardAgentNo())) {
                    reportName = PrintReportsConstants.FREIGHT_INVOICE_FORWARDER;
                } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getShipperNo())) {
                    reportName = PrintReportsConstants.FREIGHT_INVOICE_SHIPPER;
                } else if (CommonUtils.isEqualIgnoreCase(invoice.getCustomerNumber(), bl.getConsigneeNo())) {
                    reportName = PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE;
                } else {
                    reportName = PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY;
                }
                String fileNo = invoice.getDockReceipt();
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(blFolderBuilder.toString());
                fileBuilder.append("/").append(reportName).append("_").append(fileNo).append(".pdf");
                fclBlBC.createFclBillLadingReport(blNumber, fileBuilder.toString(), contextPath, messageResources, user, reportName);
                files.add(fileBuilder.toString());
                if (CommonUtils.isEqualIgnoreCase("I", bl.getImportFlag())) {
                    reportName = PrintReportsConstants.FCL_ARRIVAL_NOTICE;
                    fileBuilder = new StringBuilder();
                    fileBuilder.append(blFolderBuilder.toString());
                    fileBuilder.append("/").append(reportName).append("_").append(fileNo).append(".pdf");
                    fclBlBC.createFclBillLadingReport(blNumber, fileBuilder.toString(), contextPath, messageResources, user, reportName);
                    files.add(fileBuilder.toString());
                }
            }
        } else if (CommonUtils.isEqualIgnoreCase(invoice.getSource(), "LCL")) {
            if (invoice.getBlNumber() == null) {// this is for Ar-invoice
                ArRedInvoice arRedInvoice = arRedInvoiceDAO.getInvoice(invoice.getCustomerNumber(), invoice.getInvoiceNumber());
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(invoiceFolderBuilder.toString());
                fileBuilder.append("/Invoice_").append(arRedInvoice.getInvoiceNumber()).append(".pdf");
                LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
                lclArInvoicePdfCreator.createReport(arRedInvoice, fileBuilder.toString(), contextPath, user, null, null, false);
                files.add(fileBuilder.toString());
            } else if (invoice.getBlNumber() != null && invoice.getBlNumber().contains("CN")) {// this is for LclCorrection
                LclContact lclContact = null;
                LclUtils lclUtils = new LclUtils();
                LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
                String fileNo = invoice.getDockReceipt();
                String correctionNo = invoice.getBlNumber().substring(invoice.getBlNumber().indexOf("CN") + 2, invoice.getBlNumber().length());
                Long correctionId = new LCLCorrectionDAO().getLclCorrectionId(fileNo, correctionNo);
                Long fileId = new LclFileNumberDAO().getFileIdByFileNumber(fileNo);
                LCLCorrectionNoticeBean lclCorrectionNoticeBean = new LCLCorrectionDAO().getAllCorrectionByFileIdReports(String.valueOf(fileId), correctionId, "Imports");
                String debitOrCreditNote = new CreditDebitNoteDAO().getDebitorCredit(fileNo, correctionNo);
                lclCorrectionNoticeBean.setDebitOrCreditNote(debitOrCreditNote);
                lclCorrectionNoticeBean.setCorrectionNo(correctionNo);
                List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getLclChargeByFileNumberAsc(fileId);
                LclBookingAc lclBookingAc = lclBookingAcList.get(0);
                if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("C")) {
                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getConsContact();
                } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("A")) {
                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getAgentContact();
                } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("N")) {
                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getNotyContact();
                } else {
                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getThirdPartyContact();
                }
                lclCorrectionNoticeBean.setBillToPartyAddress(lclUtils.getConcatenatedLclContactAddress(lclContact));
                request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionChargesPdf(correctionId));
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(lclFolderBuilder.toString());
                fileBuilder.append("/").append("LCL-").append(invoice.getBlNumber()).append("-").append(debitOrCreditNote).append(".pdf");

                LclCorrectionDebitCreditPdfCreator lclCorrectionDebitORCreditNoteReportPdfCreator = new LclCorrectionDebitCreditPdfCreator();
                lclCorrectionDebitORCreditNoteReportPdfCreator.createReport(lclCorrectionNoticeBean, fileBuilder.toString(), contextPath, request, fileId, "Imports");
                files.add(fileBuilder.toString());
            } else {// this is for Freight-invoice
                FreightInvoiceLclPdfCreator freightInvoiceLclPdfCreator = new FreightInvoiceLclPdfCreator();
                LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
                String fileNo = invoice.getDockReceipt();
                Long fileId = lclFileNumberDAO.getFileIdByFileNumber(fileNo);
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(lclFolderBuilder.toString());
                fileBuilder.append("/").append(fileNo).append("-").append("FreightInvoice").append(".pdf");
                freightInvoiceLclPdfCreator.setRealPath(contextPath);
                freightInvoiceLclPdfCreator.createPdf(contextPath, null, fileId.toString(), fileNo, fileBuilder.toString(), ReportConstants.FRIEGHTINVOICE, null, user);
                files.add(fileBuilder.toString());
            }
        } else if (CommonUtils.isEqualIgnoreCase(invoice.getSource(), "INV")) {
            ArRedInvoice arRedInvoice = arRedInvoiceDAO.getInvoice(invoice.getCustomerNumber(), invoice.getInvoiceNumber());
            if (null != arRedInvoice) {
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(invoiceFolderBuilder.toString());
                fileBuilder.append("/Invoice_").append(arRedInvoice.getInvoiceNumber()).append(".pdf");
                if ((arRedInvoice.getScreenName().equalsIgnoreCase("IMP VOYAGE")) || (arRedInvoice.getScreenName().equalsIgnoreCase("LCLI DR"))
                        || (arRedInvoice.getScreenName().equalsIgnoreCase("EXP VOYAGE")) || (arRedInvoice.getScreenName().equalsIgnoreCase("LCLE DR"))) {
                    LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
                    lclArInvoicePdfCreator.createReport(arRedInvoice, fileBuilder.toString(), contextPath, user, null, null, false);
                } else {
                    arRedInvoicePdfCreator.createReport(arRedInvoice, fileBuilder.toString(), contextPath, messageResources, user);
                }
                files.add(fileBuilder.toString());
            }
        }
        Long totalSize = 0L;
        for (String fileName : files) {
            File file = new File(fileName);
            totalSize += file.length();
        }
        double size = Math.round((double) totalSize / (1024 * 1024));
        if (size > 5D) {
            PrintWriter out = response.getWriter();
            out.print("Cannot send email. Attached files are too big (" + size + "). It should be < 5MB.");
            out.flush();
            out.close();
            return null;
        }
        fileLocation = CommonUtils.isNotEmpty(files) ? files.toString().replace("[", "").replace("]", "").replace(", ", ";") : "";
        EmailSchedulerVO email = new EmailSchedulerVO();
        email.setType(CONTACT_MODE_EMAIL);
        email.setName("AR_INVOICE");
        email.setFileLocation(fileLocation);
        email.setStatus(EMAIL_STATUS_PENDING);
        email.setNoOfTries(0);
        email.setEmailDate(new Date());
        email.setModuleName("AR_INVOICE");
        email.setModuleId(documentId);
        email.setUserName(user.getLoginName());
        email.setFromName(user.getFirstName());
        email.setFromAddress(user.getEmail());
        email.setToName(null);
        email.setToAddress(arInquiryForm.getToAddress());
        if (CommonUtils.isNotEmpty(arInquiryForm.getCcAddress())) {
            String ccAddress = arInquiryForm.getCcAddress().replace(";", ",");
            if (ccAddress.contains(user.getEmail())) {
                email.setCcAddress(ccAddress);
            } else {
                email.setCcAddress(ccAddress + "," + user.getEmail());
            }
        } else {
            email.setCcAddress(user.getEmail());
        }
        email.setBccAddress(arInquiryForm.getBccAddress());
        email.setSubject(arInquiryForm.getSubject());
        email.setTextMessage(CommonUtils.isNotEmpty(arInquiryForm.getBody()) ? arInquiryForm.getBody() : "");
        email.setHtmlMessage(CommonUtils.isNotEmpty(arInquiryForm.getBody()) ? arInquiryForm.getBody() : "");
        new EmailschedulerDAO().save(email);
        Notes notes = new Notes();
        notes.setModuleId(NotesConstants.AR_INVOICE);
        notes.setModuleRefId(documentId);
        notes.setNoteDesc("Marked as Dispute");
        notes.setNoteType(NotesConstants.AUTO);
        notes.setItemName(NotesConstants.AR_INVOICE);
        notes.setUpdateDate(new Date());
        notes.setUpdatedBy(user.getLoginName());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 15);
        notes.setFollowupDate(cal.getTime());
        notesDAO.save(notes);
        if (CommonUtils.isNotEmpty(arInquiryForm.getBody())) {
            notes = new Notes();
            notes.setModuleId(NotesConstants.AR_INVOICE);
            notes.setModuleRefId(documentId);
            notes.setNoteDesc(arInquiryForm.getBody());
            notes.setNoteType(NotesConstants.AUTO);
            notes.setItemName(NotesConstants.AR_INVOICE);
            notes.setUpdateDate(new Date());
            notes.setUpdatedBy(user.getLoginName());
            notesDAO.save(notes);
        }
        ar.setStatus(STATUS_DISPUTE);
        ar.setUpdatedOn(new Date());
        ar.setUpdatedBy(user.getUserId());
        transactionDAO.update(ar);
        transactionDAO.updateTransactionOtherInfo(user.getLoginName(), arInquiryForm.getToAddress(), ar.getTransactionId());
        PrintWriter out = response.getWriter();
        out.print("disputed");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward undisputeInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        NotesDAO notesDAO = new NotesDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        User user = (User) session.getAttribute("loginuser");
        Transaction ar = transactionDAO.findById(id);
        String moduleRefId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
        notesDAO.markNotesAsCompleted(NotesConstants.AR_INVOICE, moduleRefId, "Marked as Dispute");
        Notes notes = new Notes();
        notes.setModuleId(NotesConstants.AR_INVOICE);
        notes.setModuleRefId(moduleRefId);
        notes.setNoteDesc("Unmarked as Dispute");
        notes.setNoteType(NotesConstants.AUTO);
        notes.setItemName(NotesConstants.AR_INVOICE);
        notes.setUpdateDate(new Date());
        notes.setUpdatedBy(user.getLoginName());
        notesDAO.save(notes);
        ar.setStatus(null);
        ar.setUpdatedOn(new Date());
        ar.setUpdatedBy(user.getUserId());
        transactionDAO.update(ar);
        transactionDAO.updateTransactionOtherInfo(ar.getTransactionId());
        PrintWriter out = response.getWriter();
        out.print("undisputed");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward doMassAdjustment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            User user = (User) request.getSession().getAttribute("loginuser");
            List<Transaction> noSuffixInvoices = new ArrayList<Transaction>();
            int adjustmentCount = 0;
            ArInquiryDAO arInquiryDAO = new ArInquiryDAO();
            AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
            TerminalGlMappingDAO terminalGlMappingDAO = new TerminalGlMappingDAO();
            List<Transaction> invoices = arInquiryDAO.getMassAdjustmentInvoices();
            String glAccount = LoadLogisoftProperties.getProperty("arInquiryAdjustmentGLAccount");
            for (Transaction ar : invoices) {
                boolean noSuffix = true;
                String invoiceOrBl = CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber();
                if (invoiceOrBl.startsWith("03")) {
                    String billingTerminal = invoiceOrBl.substring(2, 4);
                    if (billingTerminal.matches("\\d+")) {
                        glAccount = glAccount.replace(glAccount.substring(glAccount.lastIndexOf("-") + 1), billingTerminal);
                        noSuffix = false;
                    }
                } else {
                    String billingTerminal = null;
                    if (null != ar.getDocReceipt()) {
                        if (null != ar.getDocReceipt() && ar.getDocReceipt().startsWith("04")) {
                            String fileNo = StringUtils.removeEnd(ar.getDocReceipt(), "-R");
                            billingTerminal = arInquiryDAO.getFclBillingTerminal(fileNo);
                        } else {
                            billingTerminal = arInquiryDAO.getLclBillingTerminal(ar.getDocReceipt());
                        }
                    }
                    if (CommonUtils.isEmpty(billingTerminal)) {
                        billingTerminal = invoiceOrBl.substring(0, 2);
                    }
                    if (null != billingTerminal && billingTerminal.matches("\\d+")) {
                        String shipmentType = accountingLedgerDAO.getShipmentTypeForAR(invoiceOrBl);
                        TerminalGlMapping terminalMapping = terminalGlMappingDAO.findById(Integer.parseInt(billingTerminal));
                        Integer suffixTerminal = null;
                        if (terminalMapping != null) {
                            if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.LCLE)) {
                                suffixTerminal = terminalMapping.getLclExportBilling();
                            } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.FCLE)) {
                                suffixTerminal = terminalMapping.getFclExportBilling();
                            } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.AIRE)) {
                                suffixTerminal = terminalMapping.getAirExportBilling();
                            } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.LCLI)) {
                                suffixTerminal = terminalMapping.getLclImportBilling();
                            } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.FCLI)) {
                                suffixTerminal = terminalMapping.getFclImportBilling();
                            } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.AIRI)) {
                                suffixTerminal = terminalMapping.getAirImportBilling();
                            }
                        }
                        String suffix = StringUtils.leftPad((null != suffixTerminal ? "" + suffixTerminal : billingTerminal), 2, "0");
                        if (CommonUtils.isNotEmpty(suffix)) {
                            glAccount = glAccount.replace(glAccount.substring(glAccount.lastIndexOf("-") + 1), suffix);
                            noSuffix = false;
                        }
                    }
                }
                if (noSuffix) {
                    noSuffixInvoices.add(ar);
                } else {
                    adjustmentCount++;
                    arInquiryDAO.saveAdjustment(ar, invoiceOrBl, glAccount, user);
                }
            }
            if (CommonUtils.isNotEmpty(noSuffixInvoices)) {
                request.setAttribute("noSuffixInvoices", noSuffixInvoices);
                return mapping.findForward("adjustment");
            } else {
                out = response.getWriter();
                if (adjustmentCount > 0) {
                    out.print(adjustmentCount + " invoices are adjusted successfully");
                } else {
                    out.print("No invoices are adjusted");
                }
                out.flush();
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward doAdjustment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            String id = request.getParameter("id");
            User user = (User) request.getSession().getAttribute("loginuser");
            List<Transaction> noSuffixInvoices = new ArrayList<Transaction>();
            ArInquiryDAO arInquiryDAO = new ArInquiryDAO();
            AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
            TerminalGlMappingDAO terminalGlMappingDAO = new TerminalGlMappingDAO();
            Transaction ar = new AccountingTransactionDAO().findById(id);
            String glAccount = LoadLogisoftProperties.getProperty("arInquiryAdjustmentGLAccount");
            boolean noSuffix = true;
            String invoiceOrBl = CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber();
            if (invoiceOrBl.startsWith("03")) {
                String billingTerminal = invoiceOrBl.substring(2, 4);
                if (billingTerminal.matches("\\d+")) {
                    glAccount = glAccount.replace(glAccount.substring(glAccount.lastIndexOf("-") + 1), billingTerminal);
                    noSuffix = false;
                }
            } else {
                String billingTerminal = null;
                if (null != ar.getDocReceipt()) {
                    if (null != ar.getDocReceipt() && ar.getDocReceipt().startsWith("04")) {
                        String fileNo = StringUtils.removeEnd(ar.getDocReceipt(), "-R");
                        billingTerminal = arInquiryDAO.getFclBillingTerminal(fileNo);
                    } else {
                        billingTerminal = arInquiryDAO.getLclBillingTerminal(ar.getDocReceipt());
                    }
                }
                if (CommonUtils.isEmpty(billingTerminal)) {
                    billingTerminal = invoiceOrBl.substring(0, 2);
                }
                if (null != billingTerminal && billingTerminal.matches("\\d+")) {
                    String shipmentType = accountingLedgerDAO.getShipmentTypeForAR(invoiceOrBl);
                    TerminalGlMapping terminalMapping = terminalGlMappingDAO.findById(Integer.parseInt(billingTerminal));
                    Integer suffixTerminal = null;
                    if (terminalMapping != null) {
                        if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.LCLE)) {
                            suffixTerminal = terminalMapping.getLclExportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.FCLE)) {
                            suffixTerminal = terminalMapping.getFclExportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.AIRE)) {
                            suffixTerminal = terminalMapping.getAirExportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.LCLI)) {
                            suffixTerminal = terminalMapping.getLclImportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.FCLI)) {
                            suffixTerminal = terminalMapping.getFclImportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.AIRI)) {
                            suffixTerminal = terminalMapping.getAirImportBilling();
                        }
                    }
                    String suffix = StringUtils.leftPad((null != suffixTerminal ? "" + suffixTerminal : billingTerminal), 2, "0");
                    if (CommonUtils.isNotEmpty(suffix)) {
                        glAccount = glAccount.replace(glAccount.substring(glAccount.lastIndexOf("-") + 1), suffix);
                        noSuffix = false;
                    }
                }
            }
            if (noSuffix) {
                noSuffixInvoices.add(ar);
            } else {
                arInquiryDAO.saveAdjustment(ar, invoiceOrBl, glAccount, user);
            }
            if (CommonUtils.isNotEmpty(noSuffixInvoices)) {
                request.setAttribute("noSuffixInvoices", noSuffixInvoices);
                return mapping.findForward("adjustment");
            } else {
                out = response.getWriter();
                out.print("Invoice - " + invoiceOrBl + " adjusted successfully");
                out.flush();
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward saveAdjustment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            User user = (User) request.getSession().getAttribute("loginuser");
            String id = request.getParameter("id");
            String suffix = request.getParameter("suffix");
            Transaction ar = new AccountingTransactionDAO().findById(id);
            String glAccount = LoadLogisoftProperties.getProperty("arInquiryAdjustmentGLAccount");
            String invoiceOrBl = CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber();
            out = response.getWriter();
            if (CommonUtils.isNotEmpty(suffix)) {
                suffix = StringUtils.leftPad(suffix, 2, "0");
                glAccount = glAccount.replace(glAccount.substring(glAccount.lastIndexOf("-") + 1), suffix);
                new ArInquiryDAO().saveAdjustment(ar, invoiceOrBl, glAccount, user);
                out.print("Invoice - " + invoiceOrBl + " adjusted successfully");
            } else {
                out.print("Please enter suffix value");
            }
            out.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward updateInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            User user = (User) request.getSession().getAttribute("loginuser");
            Integer id = Integer.parseInt(request.getParameter("id"));
            String customerName = request.getParameter("customerName");
            String customerNumber = request.getParameter("customerNumber");
            String customerReference = request.getParameter("customerReference");
            new ArInquiryDAO().updateInvoice(id, customerName, customerNumber, customerReference, user);
            out = response.getWriter();
            if (CommonUtils.isNotEmpty(customerReference)) {
                out.print("Invoice updated with customer reference successfully");
            } else {
                out.print("Invoice updated with new customer successfully");
            }
            out.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward createNsInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            String batchId = request.getParameter("batchId");
            String type = request.getParameter("type");
            out = response.getWriter();
            if (CommonUtils.isEqualIgnoreCase(type, "pdf")) {
                String contextPath = this.getServlet().getServletContext().getRealPath("/");
                out.print(new NSInvoiceReportCreator().createReport(contextPath, batchId));
            } else {
                out.print(new ExportNSInvoiceToExcel().exportToExcel(batchId));
            }
            out.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward showPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArInquiryForm arInquiryForm = (ArInquiryForm) form;
        Long sourceId = arInquiryForm.getSourceId();
        String custNo = arInquiryForm.getCustomerNumber();
        ArInquiryDAO arInquiryDAO = new ArInquiryDAO();
        request.setAttribute("checks", arInquiryDAO.getLclImpChecks(sourceId, custNo));
        request.setAttribute("payments", arInquiryDAO.getLclImpPayments(sourceId, custNo));
        return mapping.findForward(PAYMENTS);
    }

    public ActionForward updateCustomerReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String custNo = request.getParameter("customerNumber");
            String referenceType = request.getParameter("referenceType");
            User user = (User) request.getSession().getAttribute("loginuser");
            new ArInquiryDAO().updateCustomerReference(custNo, referenceType, user.getLoginName());
            out.println("Customer Reference is updated with " + referenceType + " successfully.");
            out.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
