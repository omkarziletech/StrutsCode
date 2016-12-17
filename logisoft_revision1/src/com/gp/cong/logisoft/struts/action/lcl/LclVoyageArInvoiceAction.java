/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.lcl.report.LclArInvoicePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclVoyageArInvoiceForm;
import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;
import com.logiware.hibernate.dao.ArRedInvoiceChargesDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import com.logiware.thread.ArInvoiceNumberThread;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclVoyageArInvoiceAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static final String AR_RED_INVOICE_CHARGE = "invoiceCharge";
    private static final String POST_INVOICE = "postInvoice";
    private static final String AGENT_INVOICE_POPUP = "agentInvoicePopup";
    private ArRedInvoiceChargesDAO arRedInvoiceChargeDAO = new ArRedInvoiceChargesDAO();
    private ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
    ArRedInvoiceBC arRedInvoiceBC = new ArRedInvoiceBC();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        String SCREEN_NAME = SCREENNAME_AR_INVOICE;
        if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            SCREEN_NAME = SCREENNAME_EXP_AR_INVOICE;
        }
        List<ArRedInvoice> invoiceList = arRedInvoiceDAO.getArInvoiceDetails(SCREEN_NAME, fileNumberId);
        request.setAttribute("invoiceList", invoiceList);
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getVoyageId())) {
            LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclVoyageArInvoiceForm.getVoyageId()));
            if (lclssheader != null && lclssheader.getOrigin() != null) {
                lclVoyageArInvoiceForm.setVoyageTerminal(new TerminalDAO().getTerminal(lclssheader.getOrigin().getUnLocationCode()));
            }
            request.setAttribute("lclssheader", lclssheader);
        }
        if (CommonUtils.isEmpty(lclVoyageArInvoiceForm.getDate())) {
            lclVoyageArInvoiceForm.setDate(DateUtils.formatStringDateToAppFormatMMM(new Date()));
        }
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward searchAR(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        String SCREEN_NAME = SCREENNAME_AR_INVOICE;
        if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            SCREEN_NAME = SCREENNAME_EXP_AR_INVOICE;
        }
        List<ArRedInvoice> invoiceList = arRedInvoiceDAO.getArInvoiceDetails(SCREEN_NAME, lclVoyageArInvoiceForm.getFileNumberId());
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward unPost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        String invoiceId = request.getParameter("invoiceId");
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(invoiceId));
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
            Object voyageValues[] = null;
            if ("EXP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                voyageValues = lclUnitSSDAO.exportVoyageUnitValues(lclVoyageArInvoiceForm.getFileNumberId());
            } else {
                voyageValues = lclUnitSSDAO.getVoyageUnitValues(lclVoyageArInvoiceForm.getFileNumberId());
            }
            arRedInvoiceDAO.lclUnManifestAccruals(arRedInvoice, invoiceChargeList, loginUser.getFirstName(), voyageValues);
            arRedInvoice.setStatus(STATUS_I);
            arRedInvoice.setReadyToPost(null);
            arRedInvoice.setPostedDate(null);
            arRedInvoiceDAO.saveOrUpdate(arRedInvoice);
        }
        List<ArRedInvoice> invoiceList = arRedInvoiceDAO.findByProperty("fileNo", fileNumberId);
        request.setAttribute("voyageId", request.getParameter("fileNumber"));
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        ArRedInvoiceChargesDAO arRedInvoiceChargesDAO = new ArRedInvoiceChargesDAO();
        String invoiceId = request.getParameter("invoiceId");
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(invoiceId));
            request.setAttribute("arRedInvoice", arRedInvoice);
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargesDAO.findByProperty("arRedInvoiceId", Integer.parseInt(invoiceId));
            request.setAttribute("invoiceChargeList", invoiceChargeList);
        }
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getVoyageId())) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(lclVoyageArInvoiceForm.getVoyageId()));
            lclVoyageArInvoiceForm.setVoyageTerminal(s[2]);
        }
        request.setAttribute("newItemFlag", request.getParameter("newItemFlag"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.setAttribute("fileNumber", request.getParameter("fileNumberId"));
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        ArRedInvoice arRedInvoice = lclVoyageArInvoiceForm.getArRedInvoice();
        if (lclVoyageArInvoiceForm.getArRedInvoice().getId() == null && "Yes".equalsIgnoreCase(lclVoyageArInvoiceForm.getPrintOnDrFlag())) {
            arRedInvoice.setPrintOnDr(Boolean.TRUE);
        } else {
            arRedInvoice.setPrintOnDr(Boolean.FALSE);
        }
        arRedInvoice.setFileNo(lclVoyageArInvoiceForm.getFileNumberId());
        if (LCL_EXPORT.equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            arRedInvoice.setFileType(LCL_SHIPMENT_TYPE_EXPORT);
            arRedInvoice.setScreenName(SCREENNAME_EXP_AR_INVOICE);
        } else {
            arRedInvoice.setFileType(LCL_SHIPMENT_TYPE_IMPORT);
            arRedInvoice.setScreenName(SCREENNAME_AR_INVOICE);
        }
        arRedInvoice.setStatus("Open");
        arRedInvoice.setDate(DateUtils.parseDate(lclVoyageArInvoiceForm.getDate(), "dd-MMM-yyyy"));
        arRedInvoice.setDueDate(DateUtils.parseDate(lclVoyageArInvoiceForm.getDueDate(), "dd-MMM-yyyy"));
        User user = getCurrentUser(request);
        if (CommonUtils.isEmpty(arRedInvoice.getInvoiceNumber())) {
            ArInvoiceNumberThread thread = new ArInvoiceNumberThread();
            arRedInvoice.setInvoiceNumber(thread.getInvoiceNumber());
            arRedInvoice.setInvoiceBy(user.getFirstName() + " " + user.getLastName());
        } else {
            arRedInvoice.setInvoiceNumber(lclVoyageArInvoiceForm.getArRedInvoice().getInvoiceNumber());
        }
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getUnitNo())) {
            arRedInvoice.setBlNumber(lclVoyageArInvoiceForm.getUnitNo());
        }
        new ArRedInvoiceDAO().saveOrUpdate(arRedInvoice);
        ArRedInvoice arRedInvoiceV = arRedInvoiceDAO.findById(arRedInvoice.getId());
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        lclVoyageArInvoiceForm.getArRedInvoice().setInvoiceAmount(amount);
        arRedInvoiceDAO.update(lclVoyageArInvoiceForm.getArRedInvoice());
        request.setAttribute("arRedInvoice", arRedInvoiceV);
        request.setAttribute("newItemFlag", true);
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getVoyageId())) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(lclVoyageArInvoiceForm.getVoyageId()));
            lclVoyageArInvoiceForm.setVoyageTerminal(s[2]);
        }
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("terminate", lclVoyageArInvoiceForm.getPageName());
        request.setAttribute("fileNumber", request.getParameter("fileNumberId"));
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        request.setAttribute("voyageId", lclVoyageArInvoiceForm.getVoyageId());
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward post(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        String invoiceId = request.getParameter("invoiceId");
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.setAttribute("fileNumber", request.getParameter("fileNumberId"));
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(invoiceId));
            request.setAttribute("arRedInvoice", arRedInvoice);
            List<CustomerContact> checkCodeFByAcctNo = null;
            if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
                checkCodeFByAcctNo = new CustomerContactDAO().checkCodeKForInvoiceNotification(arRedInvoice.getCustomerNumber());
            } else {
                checkCodeFByAcctNo = new CustomerContactDAO().checkCodeK(arRedInvoice.getCustomerNumber());
            }
            request.setAttribute("contactList", checkCodeFByAcctNo);
        }
        request.setAttribute("voyageId", request.getParameter("fileNumber"));
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(POST_INVOICE);
    }

    public ActionForward saveCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        String shipmentType = null;
        if (LCL_EXPORT.equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
        } else {
            shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        }
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getTerminal())) {
            if (lclVoyageArInvoiceForm.getTerminal().contains("-")) {
                String[] terminalNo = lclVoyageArInvoiceForm.getTerminal().split("-");
                lclVoyageArInvoiceForm.setTerminal(terminalNo[0]);
                lclVoyageArInvoiceForm.getArRedInvoiceCharges().setTerminal(terminalNo[0]);
            } else {
                lclVoyageArInvoiceForm.getArRedInvoiceCharges().setTerminal(lclVoyageArInvoiceForm.getTerminal());
            }
        }
        lclVoyageArInvoiceForm.getArRedInvoiceCharges().setChargeCode(lclVoyageArInvoiceForm.getChargeCode());
        String revExp = "R";
        if (shipmentType.equalsIgnoreCase(LCL_SHIPMENT_TYPE_EXPORT)) {
            String originId = new LclSsHeaderDAO()
                    .getExportVoyageColumnValue("origin_id", lclVoyageArInvoiceForm.getVoyageId());
            lclVoyageArInvoiceForm.getArRedInvoiceCharges().setGlAccount(glMappingDAO
                    .getLclExportDerivedGlAccount(lclVoyageArInvoiceForm.getChargeId().toString(), "", "0", originId));
        } else {
            lclVoyageArInvoiceForm.getArRedInvoiceCharges()
                    .setGlAccount(glMappingDAO.dervieGlAccount(lclVoyageArInvoiceForm.getChargeCode(), shipmentType, lclVoyageArInvoiceForm.getVoyageTerminal(), revExp));
        }
        request.setAttribute("fileNumber", request.getParameter("fileNumberId"));
        request.setAttribute("newItemFlag", true);
        lclVoyageArInvoiceForm.getArRedInvoiceCharges().setInvoiceNumber(lclVoyageArInvoiceForm.getArRedInvoice().getInvoiceNumber());
        lclVoyageArInvoiceForm.getArRedInvoiceCharges().setArRedInvoiceId(lclVoyageArInvoiceForm.getArRedInvoiceId());
        lclVoyageArInvoiceForm.getArRedInvoiceCharges().setBlDrNumber(lclVoyageArInvoiceForm.getUnitNo());
        arRedInvoiceChargeDAO.save(lclVoyageArInvoiceForm.getArRedInvoiceCharges());
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", lclVoyageArInvoiceForm.getArRedInvoiceId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        lclVoyageArInvoiceForm.getArRedInvoice().setInvoiceAmount(amount);
        arRedInvoiceDAO.update(lclVoyageArInvoiceForm.getArRedInvoice());
        request.setAttribute("arRedInvoice", lclVoyageArInvoiceForm.getArRedInvoice());
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("terminate", lclVoyageArInvoiceForm.getPageName());
        request.setAttribute("voyageId", lclVoyageArInvoiceForm.getVoyageId());
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        String invoiceId = request.getParameter("invoiceId");
        String fileNumberId = request.getParameter("fileNumberId");
        request.setAttribute("voyageId", request.getParameter("fileNumber"));
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(invoiceId));
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            lclCostChargeDAO.updateSpRefNo(arRedInvoice.getInvoiceNumber());
            List<ArRedInvoiceCharges> invoiceChargeList = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", Integer.parseInt(invoiceId));
            for (ArRedInvoiceCharges arc : invoiceChargeList) {
                if (arc != null) {
                    arRedInvoiceChargeDAO.delete(arc);
                }
            }
            arRedInvoiceDAO.delete(arRedInvoice);
            arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(invoiceId));
            String SCREEN_NAME = SCREENNAME_AR_INVOICE;
            if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
                SCREEN_NAME = SCREENNAME_EXP_AR_INVOICE;
            }
            List<ArRedInvoice> invoiceList = arRedInvoiceDAO.getArInvoiceDetails(SCREEN_NAME, lclVoyageArInvoiceForm.getFileNumberId());
            if (invoiceList.isEmpty()) {
                request.setAttribute("invoiceList", "false");
            }
            request.setAttribute("newItemFlag", true);
            request.setAttribute("arRedInvoice", arRedInvoice);
            request.setAttribute("voyageId", request.getParameter("fileNumber"));
        }
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteInvoiceCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        request.setAttribute("fileNumber", lclVoyageArInvoiceForm.getFileNumber());
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("newItemFlag", true);
        String invoiceChargeId = request.getParameter("invoiceChargeId");
        if (CommonUtils.isNotEmpty(invoiceChargeId)) {
            ArRedInvoiceCharges arRedInvoiceCharges = arRedInvoiceChargeDAO.findById(Integer.parseInt(invoiceChargeId));
            if (arRedInvoiceCharges != null) {
                new ArRedInvoiceChargesDAO().delete(arRedInvoiceCharges);
            }
        }
        request.setAttribute("voyageId", request.getParameter("fileNumber"));
        List<ArRedInvoiceCharges> invoiceChargeList = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", lclVoyageArInvoiceForm.getArRedInvoiceId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        lclVoyageArInvoiceForm.getArRedInvoice().setInvoiceAmount(amount);
        arRedInvoiceDAO.update(lclVoyageArInvoiceForm.getArRedInvoice());
        request.setAttribute("arRedInvoice", lclVoyageArInvoiceForm.getArRedInvoice());
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(AR_RED_INVOICE_CHARGE);
    }

    public ActionForward postArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        Date now = new Date();
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(lclVoyageArInvoiceForm.getArRedInvoiceId());
        if (null != arRedInvoice) {
            User loginUser = getCurrentUser(request);
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
            lclVoyageArInvoiceForm.setCustomerName(arRedInvoice.getCustomerName());
            lclVoyageArInvoiceForm.setCustomerNumber(arRedInvoice.getCustomerNumber());
            Object voyageValues[] = null;
            if ("EXP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                voyageValues = lclUnitSSDAO.exportVoyageUnitValues(lclVoyageArInvoiceForm.getFileNumberId());
            } else {
                voyageValues = lclUnitSSDAO.getVoyageUnitValues(lclVoyageArInvoiceForm.getFileNumberId());
            }
            arRedInvoiceDAO.lclManifestAccruals(arRedInvoice, invoiceChargeList, loginUser.getFirstName(), voyageValues, null);
            arRedInvoice.setReadyToPost(STATUS_M);
            arRedInvoice.setStatus(STATUS_AR);
            arRedInvoice.setPostedDate(now);
            if ("postArRedInvoice".equalsIgnoreCase(lclVoyageArInvoiceForm.getPageName())) {
                String textMessage = "The following AR Invoice have been Posted:";
                textMessage += "<br> Customer : " + arRedInvoice.getCustomerName();
                textMessage += "<br> Invoice# : " + arRedInvoice.getInvoiceNumber();
                textMessage += "<br> Invoice Date : " + arRedInvoice.getDate();
                textMessage += "<br> Invoice Amount : " + arRedInvoice.getInvoiceAmount();
                String emailName = "VOYAGE INVOICE";
                String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/" + LclReportConstants.FOLDER_NAME + "/" + LclReportConstants.MODULENAME
                        + "/" + LclReportConstants.FOLDERNAME_ARINVOICE + "/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/";
                File dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String filelocation = fileLocation + "Invoice_" + arRedInvoice.getInvoiceNumber() + "_" + loginUser.getLoginName()
                        + "_" + DateUtils.formatDate(now, "_yyyyMMdd_HHmmss") + ".pdf";
                String realPath = this.getServlet().getServletContext().getRealPath("/");
                lclArInvoicePdfCreator.createReport(arRedInvoice, filelocation, realPath, loginUser, null, null, false);
                List<CustomerContact> customerContactList = new ArrayList();
                if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
                    customerContactList = new CustomerContactDAO().checkCodeKForInvoiceNotification(arRedInvoice.getCustomerNumber());
                    for (Object object : customerContactList) {
                        CustomerContact customerContact = (CustomerContact) object;
                        if (null != customerContact.getCodek()) {
                            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                            String subject = LoadLogisoftProperties.getProperty("application.email.companyName") + " INVOICE # " + arRedInvoice.getInvoiceNumber() + "-" + arRedInvoice.getCustomerName();
                            if (CommonUtils.isNotEmpty(customerContact.getEmail())) {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getEmail(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getEmail(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo(emailName, null, CommonConstants.CONTACT_MODE_EMAIL, 0, now, "LCLARINVOICE", "", loginUser.getLoginName());
                            } else {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getFax(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getFax(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo(emailName, null, CommonConstants.CONTACT_MODE_FAX, 0, now, "LCLARINVOICE", "", loginUser.getLoginName());
                            }
                            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                            emailSchedulerVO.setTextMessage(textMessage);
                            emailSchedulerVO.setHtmlMessage(textMessage);
                            emailSchedulerVO.setFileLocation(filelocation);
                            emailSchedulerVO.setModuleId(arRedInvoice.getInvoiceNumber());
                            emailschedulerDAO.save(emailSchedulerVO);
                        }
                    }
                } else {
                    String property = "";
                    String brandValue = "";
                    customerContactList = new CustomerContactDAO().checkCodeK(arRedInvoice.getCustomerNumber());
                    if ("IMP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                        brandValue = new TradingPartnerDAO().getBusinessUnit(new LclUnitSsDAO().getUnitOriginAcct(arRedInvoice.getFileNo()));
                    }
                    property = brandValue.equalsIgnoreCase("ECI") ? "application.Econo.companyname"
                            : brandValue.equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.ECU.companyname";
                    for (Object object : customerContactList) {
                        CustomerContact customerContact = (CustomerContact) object;
                        if (null != customerContact.getCodek() && CommonUtils.in(customerContact.getCodek().getCode(), "E", "F")) {
                            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                            String subject = LoadLogisoftProperties.getProperty(property) + " INVOICE # " + arRedInvoice.getInvoiceNumber() + "-" + arRedInvoice.getCustomerName();
                            if (CommonUtils.isNotEmpty(customerContact.getEmail()) && CommonUtils.isEqual(customerContact.getCodek().getCode(), "E")) {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getEmail(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getEmail(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo(emailName, null, CommonConstants.CONTACT_MODE_EMAIL, 0, now, "LCLARINVOICE", "", loginUser.getLoginName());
                            } else {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getFax(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getFax(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo(emailName, null, CommonConstants.CONTACT_MODE_FAX, 0, now, "LCLARINVOICE", "", loginUser.getLoginName());
                            }
                            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                            emailSchedulerVO.setTextMessage(textMessage);
                            emailSchedulerVO.setHtmlMessage(textMessage);
                            emailSchedulerVO.setFileLocation(filelocation);
                            emailSchedulerVO.setModuleId(arRedInvoice.getInvoiceNumber());
                            emailschedulerDAO.save(emailSchedulerVO);
                        }
                    }
                }
            }
            request.setAttribute("displayMessage", "This invoice " + arRedInvoice.getInvoiceNumber() + " is Posted to AR Succesfully");
        }
        arRedInvoiceDAO.getCurrentSession().clear();
        arRedInvoiceDAO.update(arRedInvoice);
        String SCREEN_NAME = "Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())
                ? SCREENNAME_EXP_AR_INVOICE : SCREENNAME_AR_INVOICE;
        List<ArRedInvoice> invoiceList = arRedInvoiceDAO.getArInvoiceDetails(SCREEN_NAME, lclVoyageArInvoiceForm.getFileNumberId());
        request.setAttribute("arRedInvoiceform", lclVoyageArInvoiceForm);
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("arInvoice", arRedInvoice);
        request.setAttribute("voyageId", lclVoyageArInvoiceForm.getVoyageId());
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward postInvoiceWithoutEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        Date now = new Date();
        User loginUser = getCurrentUser(request);
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(lclVoyageArInvoiceForm.getArRedInvoiceId());
        lclVoyageArInvoiceForm.setNotification("Send");
        if (null != arRedInvoice) {
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
            lclVoyageArInvoiceForm.setCustomerName(arRedInvoice.getCustomerName());
            lclVoyageArInvoiceForm.setCustomerNumber(arRedInvoice.getCustomerNumber());
            Object voyageValues[] = null;
            if ("EXP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                voyageValues = lclUnitSSDAO.exportVoyageUnitValues(lclVoyageArInvoiceForm.getFileNumberId());
            } else {
                voyageValues = lclUnitSSDAO.getVoyageUnitValues(lclVoyageArInvoiceForm.getFileNumberId());
            }
            arRedInvoiceDAO.lclManifestAccruals(arRedInvoice, invoiceChargeList, loginUser.getFirstName(), voyageValues, null);
            arRedInvoice.setReadyToPost(STATUS_M);
            arRedInvoice.setStatus(STATUS_AR);
            arRedInvoice.setPostedDate(now);
            request.setAttribute("displayMessage", "This invoice " + arRedInvoice.getInvoiceNumber() + " is Posted to AR Succesfully");
        }
        arRedInvoice = arRedInvoiceDAO.saveAndReturn(arRedInvoice);
        String SCREEN_NAME = SCREENNAME_AR_INVOICE;
        if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            SCREEN_NAME = SCREENNAME_EXP_AR_INVOICE;
        }
        List<ArRedInvoice> invoiceList = arRedInvoiceDAO.getArInvoiceDetails(SCREEN_NAME, lclVoyageArInvoiceForm.getFileNumberId());
        request.setAttribute("arRedInvoiceform", lclVoyageArInvoiceForm);
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("arInvoice", arRedInvoice);
        request.setAttribute("voyageId", lclVoyageArInvoiceForm.getVoyageId());
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward openAgentInvoicePopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        if ("Yes".equalsIgnoreCase(lclVoyageArInvoiceForm.getAgentFlag())) {
            List<ImportsManifestBean> autoAgentList = new LclUnitSsDAO().getAgentInvoiceCharges(lclVoyageArInvoiceForm.getFileNumberId());
            request.setAttribute("autoAgentChargeList", autoAgentList);
        }
        setAgentList(request, lclVoyageArInvoiceForm);
        request.setAttribute("arRedInvoiceform", lclVoyageArInvoiceForm);
        return mapping.findForward(AGENT_INVOICE_POPUP);
    }

    private void setAgentList(HttpServletRequest request, LclVoyageArInvoiceForm lclVoyageArInvoiceForm) throws Exception {
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        ImportsManifestBean drAgentCharges = lclUnitSSDAO.getAgentDetailsByRelsToInv(Long.parseLong(lclVoyageArInvoiceForm.getFileNumberId()), request.getParameter("agentAcctNo"));
        request.setAttribute("drAgentCharge", drAgentCharges);
        //request.setAttribute("agentBeanNotRelsToInv", agentBeanNotRelsToInv);
    }

    public ActionForward saveAgentInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        ArRedInvoiceChargesDAO arRedInvoiceChargesDAO = new ArRedInvoiceChargesDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        List<ImportsManifestBean> autoAgentList = new ArrayList<ImportsManifestBean>();
        if ("Yes".equalsIgnoreCase(lclVoyageArInvoiceForm.getAgentFlag())) {
            autoAgentList = lclUnitSSDAO.getAgentInvoiceCharges(lclVoyageArInvoiceForm.getFileNumberId());
        }
        List<ImportsManifestBean> agentList = lclUnitSSDAO.getDRSForAgentInvoice(Long.parseLong(lclVoyageArInvoiceForm.getFileNumberId()), "");
        autoAgentList.addAll(agentList);
        ArRedInvoice arRedInvoice = null;
        if (CommonUtils.isEmpty(lclVoyageArInvoiceForm.getArRedInvoiceId())) {
            arRedInvoice = arRedInvoiceBC.saveAgentInvoice(autoAgentList, getCurrentUser(request), lclUnitSSDAO.getTotalAmount(),
                    lclVoyageArInvoiceForm.getFileNumberId(), true, lclVoyageArInvoiceForm);
        } else {
            arRedInvoice = arRedInvoiceDAO.findById(lclVoyageArInvoiceForm.getArRedInvoiceId());
        }
        for (ImportsManifestBean agent : autoAgentList) {
            if (CommonUtils.isNotEmpty(agent.getConcatenatedFileNos()) && agent.getConcatenatedFileNos().endsWith(",")) {
                agent.setConcatenatedFileNos(agent.getConcatenatedFileNos().substring(0, agent.getConcatenatedFileNos().length() - 1));
            }
            arRedInvoiceChargesDAO.insertArRedInvoiceCharge(arRedInvoice.getId().toString(), agent.getChargeCode(), agent.getTotalCharges().toString(), "", "", agent.getGlAccount(),
                    agent.getShipmentType(), agent.getBillingTerminal(), arRedInvoice.getInvoiceNumber(), agent.getUnitNumber(), agent.getConcatenatedFileNos());
        }
        if (lclUnitSSDAO.getBookingAcIds() != null && !lclUnitSSDAO.getBookingAcIds().equals("")) {
            lclCostChargeDAO.updateInvoiceNumberForCharges(arRedInvoice.getInvoiceNumber(), lclUnitSSDAO.getBookingAcIds(), getCurrentUser(request).getUserId());
        }
        lclVoyageArInvoiceForm.setArRedInvoiceId(arRedInvoice.getId());
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getVoyageId())) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(lclVoyageArInvoiceForm.getVoyageId()));
            lclVoyageArInvoiceForm.setVoyageTerminal(s[2]);
        }
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargesDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        arRedInvoice.setInvoiceAmount(amount);
        arRedInvoiceDAO.update(arRedInvoice);
        lclVoyageArInvoiceForm.setArRedInvoice(arRedInvoice);
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("newItemFlag", request.getParameter("newItemFlag"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.setAttribute("fileNumber", request.getParameter("fileNumberId"));
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteEntireInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        ArRedInvoiceChargesDAO arRedInvoiceChargesDAO = new ArRedInvoiceChargesDAO();
        if (CommonUtils.isNotEmpty(lclVoyageArInvoiceForm.getInvoiceId())) {
            ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(lclVoyageArInvoiceForm.getInvoiceId()));
            lclCostChargeDAO.updateSpRefNo(arRedInvoice.getInvoiceNumber());
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargesDAO.findByProperty("arRedInvoiceId", Integer.parseInt(lclVoyageArInvoiceForm.getInvoiceId()));
            for (ArRedInvoiceCharges arc : invoiceChargeList) {
                if (arc != null) {
                    arRedInvoiceChargeDAO.delete(arc);
                }
            }
            arRedInvoiceDAO.delete(arRedInvoice);
        }
        String SCREEN_NAME = SCREENNAME_AR_INVOICE;
        if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            SCREEN_NAME = SCREENNAME_EXP_AR_INVOICE;
        }
        List<ArRedInvoice> invoiceList = arRedInvoiceDAO.getArInvoiceDetails(SCREEN_NAME, lclVoyageArInvoiceForm.getFileNumberId());
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("lclVoyageArInvoiceForm", lclVoyageArInvoiceForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showDrLevelAgentInvoce(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogiwareActionForm logForm = (LogiwareActionForm) form;
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        String message = null;
        List<ImportsManifestBean> arDrLevetChargeList = lclUnitSSDAO.showDrLevelAgentInvoce(logForm.getModuleId());
        if (CommonUtils.isNotEmpty(arDrLevetChargeList)) {
            request.setAttribute("arDrLevetChargeList", arDrLevetChargeList);
            message = "showDrLevelAgentInvoce";
        }
        return mapping.findForward(message);
    }

    public ActionForward drLevelAgentCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogiwareActionForm logForm = (LogiwareActionForm) form;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        List<ImportsManifestBean> drLevelAgentCharges = lclUnitSSDAO.drAgentInvoiceCharges(logForm.getModuleId());
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        ArRedInvoice arRedInvoice = null;
        String invoiceId = request.getParameter("invoiceId");
        if (!CommonUtils.isEmpty(invoiceId)) {
            arRedInvoice = arRedInvoiceDAO.findById(Integer.parseInt(invoiceId));
        }
        for (ImportsManifestBean charge : drLevelAgentCharges) {
            arRedInvoiceChargeDAO.insertArRedInvoiceCharge(arRedInvoice.getId().toString(), charge.getChargeCode(), charge.getTotalCharges().toString(), "", "", charge.getGlAccount(),
                    "LCLI", charge.getBillingTerminal(), arRedInvoice.getInvoiceNumber(), arRedInvoice.getBlNumber(), charge.getFileNo());
            lclCostChargeDAO.updateInvoiceNumberForCharges(arRedInvoice.getInvoiceNumber(), charge.getChargeId().toString(), getCurrentUser(request).getUserId());
        }
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        arRedInvoice.setInvoiceAmount(amount);
        arRedInvoiceDAO.update(arRedInvoice);
        arRedInvoiceDAO.saveOrUpdate(arRedInvoice);
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("drLevelAgentCharges", drLevelAgentCharges);
        return mapping.findForward("drLevelAgentCharges");
    }

    public ActionForward checkCodeFCountByAcctNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclVoyageArInvoiceForm lclVoyageArInvoiceForm = (LclVoyageArInvoiceForm) form;
        CustomerContactDAO customerContacDao = new CustomerContactDAO();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String acctNo = request.getParameter("acctNo");
        List<ImportsManifestBean> checkCodeFByAcctNo = null;
        String codeKContact = "";
        if ("Exports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
            checkCodeFByAcctNo = customerContacDao.getAlLEmailIdsOfExportCodeK(acctNo);
        } else {
            codeKContact = customerContacDao.getEmailAndFaxOfCodeK(acctNo);
        }
        if (!CommonUtils.isEmpty(checkCodeFByAcctNo) && checkCodeFByAcctNo != null || CommonUtils.isNotEmpty(codeKContact)) {
            out.print("true");
        } else {
            out.print("-->AR Invoice Auto Notification Contact does NOT exist," + " For the Party "
                    + request.getParameter("acctName") + "(" + acctNo
                    + ")<br><br> Please Enter Contacts in TP ");
        }
        return mapping.findForward("checkCodeFCountByAcctNo");
    }
}
