package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.ApInvoiceDAO;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.dao.FclManifestDAO;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.accounting.excel.AccrualsExcelCreator;
import com.logiware.accounting.form.AccrualsForm;
import com.logiware.accounting.reports.AccrualsReportCreator;
import com.logiware.accounting.reports.EdiInvoiceCreator;
import com.logiware.hibernate.dao.SSMastersApprovedDAO;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Naryanan
 */
public class AccrualsAction extends BaseAction {

    private static final String RESULTS = "results";

    public ActionForward searchByVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        new AccrualsDAO().search(accrualsForm);
        request.setAttribute("accrualsForm", accrualsForm);
        return mapping.findForward(RESULTS);
    }

    public ActionForward searchByInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        new AccrualsDAO().search(accrualsForm);
        request.setAttribute("accrualsForm", accrualsForm);
        return mapping.findForward(RESULTS);
    }

    public ActionForward searchByFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        new AccrualsDAO().search(accrualsForm);
        request.setAttribute("accrualsForm", accrualsForm);
        return mapping.findForward(RESULTS);
    }

    public ActionForward inactivateAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            int count = new AccrualsDAO().inactivateAccruals(accrualsForm, user);
            PrintWriter out = response.getWriter();
            out.print((count > 0 ? ((count == 1 ? ("One Accrual is") : (count + " Accruals are")) + " inactivated successfully") : ("No Accrual is inactivated")));
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward activateAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            int count = new AccrualsDAO().activateAccruals(accrualsForm, user);
            PrintWriter out = response.getWriter();
            out.print((count > 0 ? ((count == 1 ? ("One Accrual is") : (count + " Accruals are")) + " activated successfully") : ("No Accrual is activated")));
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward deleteOrInactivateAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            new AccrualsDAO().deleteOrInactivateAccruals(accrualsForm, user);
            PrintWriter out = response.getWriter();
            int inactivecount = 0;
            int deletecount = 0;
            int activecount = 0;
            int undeletecount = 0;
            StringBuilder message = new StringBuilder();
            if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
                inactivecount = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ",")).size();
            }
            if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
                deletecount = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ",")).size();
            }
            if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
                activecount = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ",")).size();
            }
            if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
                undeletecount = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ",")).size();
            }
            if (inactivecount > 0) {
                message.append(deletecount > 0 || activecount > 0 || undeletecount > 0 ? "1)" : "");
                message.append(inactivecount).append(inactivecount > 1 ? " accruals are" : " accrual is").append(" inactivated");
            }
            if (deletecount > 0) {
                message.append(inactivecount > 0 ? "<br/>2)" : activecount > 0 || undeletecount > 0 ? "<br/>1)" : "");
                message.append(deletecount).append(deletecount > 1 ? " accruals are" : " accrual is").append(" deleted");
            }
            if (activecount > 0) {
                if (inactivecount > 0 && deletecount > 0) {
                    message.append("<br/>3)");
                } else if (inactivecount > 0 || deletecount > 0) {
                    message.append("<br/>2)");
                } else if (undeletecount > 0) {
                    message.append("<br/>1)");
                }
                message.append(activecount).append(activecount > 1 ? " accruals are" : " accrual is").append(" activated");
            }
            if (undeletecount > 0) {
                if (inactivecount > 0 && deletecount > 0 && activecount > 0) {
                    message.append("<br/>4)");
                } else if ((inactivecount > 0 && deletecount > 0) || (inactivecount > 0 && activecount > 0) || (deletecount > 0 && activecount > 0)) {
                    message.append("<br/>3)");
                } else if (inactivecount > 0 || deletecount > 0 || activecount > 0) {
                    message.append("<br/>2)");
                }
                message.append(undeletecount).append(undeletecount > 1 ? " accruals are" : " accrual is").append(" undeleted");
            }
            out.print(message.toString());
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward deriveGlAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        String suffix = request.getParameter("suffix");
        String shipmentType = request.getParameter("shipmentType");
        String terminal = request.getParameter("terminal");
        String glAccount = new AccrualsDAO().deriveGlAccount(account, suffix, shipmentType, terminal);
        PrintWriter out = response.getWriter();
        out.print(glAccount);
        out.flush();
        out.close();
        return null;
    }

    public ActionForward addAccrual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            new AccrualsDAO().addAccrual(accrualsForm, user, request);
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "arBatch")) {
                return mapping.findForward("addForArBatch");
            } else {
                request.setAttribute("accrualsForm", accrualsForm);
                return mapping.findForward(RESULTS);
            }
        }
    }

    public ActionForward showUpdateAccrual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        TransactionLedger accrual = new AccrualsDAO().findById(accrualsForm.getAccrualId());
        accrualsForm.setUpdateVendorName(accrual.getCustName());
        accrualsForm.setUpdateVendorNumber(accrual.getCustNo());
        accrualsForm.setUpdateInvoiceNumber(accrual.getInvoiceNumber());
        accrualsForm.setUpdateCostCode(accrual.getChargeCode());
        accrualsForm.setUpdateGlAccount(accrual.getGlAccountNumber());
        accrualsForm.setUpdateShipmentType(accrual.getShipmentType());
        accrualsForm.setUpdateBluescreenCostCode(accrual.getBlueScreenChargeCode());
        accrualsForm.setUpdateTerminal(accrual.getTerminal());
        accrualsForm.setUpdateAmount(NumberUtils.formatNumber(accrual.getTransactionAmt(), "0.00"));
        request.setAttribute("accrualsForm", accrualsForm);
        return mapping.findForward("updateAccruals");
    }

    public ActionForward updateAccrual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form; 
            User user = (User) request.getSession().getAttribute("loginuser");
            new AccrualsDAO().updateAccrual(accrualsForm, user);
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "arBatch")) {
                PrintWriter out = response.getWriter();
                out.print(CommonUtils.isNotEmpty(accrualsForm.getNewAccrualId()) ? accrualsForm.getNewAccrualId() : "");
                out.flush();
                out.close();
                return null;
            } else {
                if (CommonUtils.isNotEmpty(accrualsForm.getResults())) {
                    request.setAttribute("accrualsForm", accrualsForm);
                    return mapping.findForward(RESULTS);
                } else {
                    PrintWriter out = response.getWriter();
                    out.print("success");
                    out.flush();
                    out.close();
                    return null;
                }
            }
        }
    }

    public ActionForward isInvoiceUploaded(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        PrintWriter out = response.getWriter();
        String documentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
        String screenName = "INVOICE";
        String documentName = "INVOICE";
        out.print(new DocumentStoreLogDAO().isUploaded(documentId, screenName, documentName));
        out.flush();
        out.close();
        return null;
    }

    public ActionForward disputeInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            accrualsForm.setStatus(STATUS_DISPUTE);
            User user = (User) request.getSession().getAttribute("loginuser");
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            String documentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            String screenName = "INVOICE";
            String documentName = "INVOICE";
            String fileLocation = new DocumentStoreLogDAO().getFiles(documentId, screenName, documentName);
            emailSchedulerVO.setFileLocation(fileLocation);
            emailSchedulerVO.setType(CONTACT_MODE_EMAIL);
            emailSchedulerVO.setStatus(EMAIL_STATUS_PENDING);
            emailSchedulerVO.setNoOfTries(0);
            emailSchedulerVO.setEmailDate(new Date());
            emailSchedulerVO.setModuleName("ACCRUALS");
            emailSchedulerVO.setModuleId(accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber());
            emailSchedulerVO.setUserName(user.getLoginName());
            emailSchedulerVO.setFromName(user.getFirstName());
            emailSchedulerVO.setFromAddress(user.getEmail());
            emailSchedulerVO.setToAddress(accrualsForm.getToAddress());
            String ccAddress = accrualsForm.getCcAddress().replace(";", ",");
            if (ccAddress.contains(user.getEmail())) {
                emailSchedulerVO.setCcAddress(ccAddress);
            } else {
                if (ccAddress.isEmpty()) {
                    emailSchedulerVO.setCcAddress(user.getEmail());
                } else {
                    emailSchedulerVO.setCcAddress(ccAddress.concat(",").concat(user.getEmail()));
                }
            }
            emailSchedulerVO.setBccAddress(accrualsForm.getBccAddress());
            emailSchedulerVO.setSubject(accrualsForm.getSubject());
            emailSchedulerVO.setTextMessage(accrualsForm.getBody());
            emailSchedulerVO.setHtmlMessage(accrualsForm.getBody());
            new EmailschedulerDAO().save(emailSchedulerVO);
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                new AccrualsDAO().disputeEdiInvoice(accrualsForm, user);
            } else {
                new AccrualsDAO().disputeInvoice(accrualsForm, user);
            }
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward unDisputeInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                new AccrualsDAO().unDisputeEdiInvoice(accrualsForm, user);
            } else {
                new AccrualsDAO().unDisputeInvoice(accrualsForm, user);
            }
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward rejectInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            accrualsForm.setStatus(STATUS_REJECT);
            User user = (User) request.getSession().getAttribute("loginuser");
            new AccrualsDAO().rejectInvoice(accrualsForm, user);
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward unRejectInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            new AccrualsDAO().unRejectInvoice(accrualsForm, user);
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward inprogressInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            accrualsForm.setStatus(STATUS_IN_PROGRESS);
            User user = (User) request.getSession().getAttribute("loginuser");
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                new AccrualsDAO().inprogressEdiInvoice(accrualsForm, user);
            } else {
                new AccrualsDAO().inprogressInvoice(accrualsForm, user);
            }
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward postInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            AccrualsForm accrualsForm = (AccrualsForm) form;
            User user = (User) request.getSession().getAttribute("loginuser");
            if (new ApInvoiceDAO().isOpenInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber())) {
                if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                    new AccrualsDAO().postEdiInvoice(accrualsForm, user, request);
                } else {
                    new AccrualsDAO().postInvoice(accrualsForm, user, request);
                }
                PrintWriter out = response.getWriter();
                out.print("success");
                out.flush();
                out.close();
            }
            return null;
        }
    }

    public ActionForward isAccrualsFullyMapped(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        PrintWriter out = response.getWriter();
        out.print(new AccrualsDAO().isAccrualsFullyMapped(accrualsForm.getAssignedAccrualIds()));
        out.flush();
        out.close();
        return null;
    }

    public ActionForward fromEdiInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.findById(accrualsForm.getEdiInvoiceId());
        if (null != ediInvoice) {
            Vendor vendor = new TradingPartnerDAO().getVendor(ediInvoice.getVendorNumber());
            GenericCode genericCode;
            if (null != vendor && null != vendor.getCterms()) {
                genericCode = vendor.getCterms();
            } else {
                Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
                genericCode = new GenericCodeDAO().getGenericCode("Due Upon Receipt", codeTypeId);
            }
            accrualsForm.setVendorName(ediInvoice.getVendorName());
            accrualsForm.setVendorNumber(ediInvoice.getVendorNumber());
            accrualsForm.setSearchVendorName(ediInvoice.getVendorName());
            accrualsForm.setSearchVendorNumber(ediInvoice.getVendorNumber());
            accrualsForm.setInvoiceNumber(ediInvoice.getInvoiceNumber());
            accrualsForm.setEdiInvoiceNumber(ediInvoice.getInvoiceNumber());
            accrualsForm.setInvoiceAmount(NumberUtils.formatNumber(ediInvoice.getInvoiceAmount(), "0.00"));
            accrualsForm.setRemainingAmount(NumberUtils.formatNumber(ediInvoice.getInvoiceAmount(), "0.00"));
            accrualsForm.setInvoiceDate(DateUtils.formatDate(ediInvoice.getInvoiceDate(), "MM/dd/yyyy"));
            accrualsForm.setCreditId(genericCode.getId());
            accrualsForm.setCreditTerm(Integer.parseInt(genericCode.getCode()));
            accrualsForm.setCreditDesc(genericCode.getCodedesc());
            accrualsForm.setDueDate(new ApInvoiceDAO().calculateDueDate(accrualsForm.getInvoiceDate(), accrualsForm.getCreditTerm()));
            if (CommonUtils.isEqualIgnoreCase(ediInvoice.getStatus(), STATUS_EDI_DISPUTE)) {
                accrualsForm.setDispute(true);
            }
            accrualsForm.setAction("searchByInvoice");
            accrualsForm.setFrom("EDI");
            new AccrualsDAO().search(accrualsForm);
            String documentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            String screenName = "INVOICE";
            String documentName = "INVOICE";
            if (!new DocumentStoreLogDAO().isUploaded(documentId, screenName, documentName)) {
                String contextPath = this.servlet.getServletContext().getRealPath("/");
                String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
                ediInvoiceDAO.copyDocuments(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), fileName);
            }
        }
        request.setAttribute("fromParams", request.getParameter("fromParams").replace("%26", "&").replace("%26", "="));
        request.setAttribute("accrualsForm", accrualsForm);
        return mapping.findForward(SUCCESS);
    }
    public ActionForward fromSsMasters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        FclBlDAO fclBlDAO = new FclBlDAO();
        String fileNo = accrualsForm.getFileNo();
        FclBl bl = fclBlDAO.getFileNoObject(fileNo);
        User user = (User) request.getSession().getAttribute("loginuser");
        new FclManifestDAO(bl, user).postAccrualsBeforeManifest();
        String vendorName = "C-Collect".equals(bl.getStreamShipBl()) ? bl.getAgent() : bl.getSslineName();
        String vendorNumber = "C-Collect".equals(bl.getStreamShipBl()) ? bl.getAgentNo() : bl.getSslineNo();
        String blNumber = CommonUtils.isNotEmpty(bl.getNewMasterBL()) ? bl.getNewMasterBL() : bl.getBolId();
        new SSMastersApprovedDAO().copySSMasterApprovedDocumentsToAp(vendorNumber, fileNo, blNumber);
        accrualsForm.setVendorName(vendorName);
        accrualsForm.setVendorNumber(vendorNumber);
        accrualsForm.setSearchVendorName(vendorName);
        accrualsForm.setSearchVendorNumber(vendorNumber);
        accrualsForm.setInvoiceNumber(blNumber);
        String invoiceDate = DateUtils.formatDate(bl.getSailDate(), "MM/dd/yyyy");
        accrualsForm.setInvoiceDate(invoiceDate);
        Vendor vendor = new TradingPartnerDAO().getVendor(vendorNumber);
        GenericCode genericCode;
        if (null != vendor && null != vendor.getCterms()) {
            genericCode = vendor.getCterms();
        } else {
            Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
            genericCode = new GenericCodeDAO().getGenericCode("Due Upon Receipt", codeTypeId);
        }
        accrualsForm.setCreditId(genericCode.getId());
        accrualsForm.setCreditTerm(Integer.parseInt(genericCode.getCode()));
        accrualsForm.setCreditDesc(genericCode.getCodedesc());
        accrualsForm.setDueDate(new ApInvoiceDAO().calculateDueDate(accrualsForm.getInvoiceDate(), accrualsForm.getCreditTerm()));
        accrualsForm.setSearchBy("drcpt");
        accrualsForm.setSearchValue(bl.getFileNo());
        accrualsForm.setAction("searchBySSMasters");
        new AccrualsDAO().search(accrualsForm);
        accrualsForm.setAction("searchByFilter");
        request.setAttribute("accrualsForm", accrualsForm);
        request.setAttribute("fromParams", request.getParameter("fromParams").replace("%26", "&").replace("%26", "="));
        return mapping.findForward(SUCCESS);
    }
    public ActionForward fromLclSsMasters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        String document_id = accrualsForm.getNewDockReceipt() +"-"+accrualsForm.getSsMasterId();
        new SSMastersApprovedDAO().copyLclSSMasterApprovedDocumentsToAp(accrualsForm.getNewVendorNumber(), document_id, accrualsForm.getFileNo());
        Vendor vendor = new TradingPartnerDAO().getVendor(accrualsForm.getNewVendorNumber());
        TradingPartner tp = new TradingPartnerDAO().findById(accrualsForm.getNewVendorNumber());
        accrualsForm.setVendorNumber(accrualsForm.getNewVendorNumber());
        if(null != tp){
            accrualsForm.setVendorName(tp.getAccountName());
            accrualsForm.setSearchVendorName(tp.getAccountName());
        }
        accrualsForm.setSearchVendorNumber(accrualsForm.getNewVendorNumber());
        accrualsForm.setInvoiceNumber(accrualsForm.getSsMasterBl());
        accrualsForm.setInvoiceDate(accrualsForm.getNewInvoiceDate());
        GenericCode genericCode;
        if (null != vendor && null != vendor.getCterms()) {
            genericCode = vendor.getCterms();
        } else {
            Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
            genericCode = new GenericCodeDAO().getGenericCode("Due Upon Receipt", codeTypeId);
        }
        accrualsForm.setCreditId(genericCode.getId());
        accrualsForm.setCreditTerm(Integer.parseInt(genericCode.getCode()));
        accrualsForm.setCreditDesc(genericCode.getCodedesc());
        if(CommonUtils.isNotEmpty(accrualsForm.getInvoiceDate()) && CommonUtils.isNotEmpty(accrualsForm.getCreditTerm())){
            accrualsForm.setDueDate(new ApInvoiceDAO().calculateDueDate(accrualsForm.getInvoiceDate(), accrualsForm.getCreditTerm()));
        }else{
            accrualsForm.setDueDate(new ApInvoiceDAO().calculateDueDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"), 0));
        }
        accrualsForm.setSearchBy("invoice_number");
        accrualsForm.setSearchValue(accrualsForm.getSsMasterBl());
        new AccrualsDAO().searchByLclSSMasters(accrualsForm);
        accrualsForm.setAction("searchByFilter");
        request.setAttribute("accrualsForm", accrualsForm);
        if(null != request.getParameter("fromParams")){
            request.setAttribute("fromParams", request.getParameter("fromParams").replace("%26", "&").replace("%26", "="));
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward overwriteDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            String oldDocumentId = request.getParameter("oldDocumentId");
            String newDocumentId = request.getParameter("newDocumentId");
            new DocumentStoreLogDAO().overwriteDocumentId("INVOICE", "INVOICE", oldDocumentId, newDocumentId);
            return null;
        }
    }

    public ActionForward createPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        PrintWriter out = response.getWriter();
        out.print(new AccrualsReportCreator(accrualsForm, this.getServlet().getServletContext().getRealPath("/")).create());
        out.flush();
        out.close();
        return null;
    }

    public ActionForward createExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        PrintWriter out = response.getWriter();
        out.print(new AccrualsExcelCreator(accrualsForm).create());
        out.flush();
        out.close();
        return null;
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        AccrualsForm newAccrualsForm = new AccrualsForm();
        if (accrualsForm.isFirstClear()) {
            newAccrualsForm.setVendorName(accrualsForm.getVendorName());
            newAccrualsForm.setVendorNumber(accrualsForm.getVendorNumber());
            newAccrualsForm.setSearchVendorName(accrualsForm.getVendorName());
            newAccrualsForm.setSearchVendorNumber(accrualsForm.getVendorNumber());
            newAccrualsForm.setFirstClear(false);
        } else {
            newAccrualsForm.setFirstClear(true);
        }
        newAccrualsForm.setFrom(accrualsForm.getFrom());
        request.setAttribute("accrualsForm", newAccrualsForm);
        return mapping.findForward(SUCCESS);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualsForm accrualsForm = new AccrualsForm();
        AccrualsDAO accrualsDao = new AccrualsDAO();
        accrualsForm.setFirstClear(true);
        request.setAttribute("shipmentTypes", accrualsDao.getShipmentTypes());
        request.setAttribute("accrualsForm", accrualsForm);
        return mapping.findForward(SUCCESS);
    }
}
