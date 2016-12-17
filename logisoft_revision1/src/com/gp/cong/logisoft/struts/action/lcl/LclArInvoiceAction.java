/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.lcl.report.LclArInvoicePdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclArInvoiceForm;
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
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclArInvoiceAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static final String AR_RED_INVOICE_CHARGE = "invoiceCharge";
    private static final String POST_INVOICE = "postInvoice";
    private ArRedInvoiceChargesDAO arRedInvoiceChargeDAO = new ArRedInvoiceChargesDAO();
    private ArRedInvoiceDAO arInvoiceDAO = new ArRedInvoiceDAO();
    ArRedInvoiceBC arRedInvoiceBC = new ArRedInvoiceBC();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;//diplay Invoice popup
        String screenName = "Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) ? SCREENNAME_DR_AR_INVOICE : "LCLE DR";
        List<ArRedInvoice> invoiceList = arInvoiceDAO.getArInvoiceDetails(screenName, lclArInvoiceForm.getFileNumberId());
        if (CommonUtils.isNotEmpty(request.getParameter("headerId"))) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(lclArInvoiceForm.getHeaderId()));
            request.setAttribute("closedBy", s[0]);
            request.setAttribute("auditedBy", s[1]);
            lclArInvoiceForm.setVoyTerminalNo(s[2]);
        }
        request.setAttribute("listFlag", request.getParameter("listFlag"));
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("lclArInvoiceForm", lclArInvoiceForm);
        request.setAttribute("todayDate", new Date());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward searchAR(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;//search button action
        String screenName = "Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) ? SCREENNAME_DR_AR_INVOICE : "LCLE DR";
        List<ArRedInvoice> invoiceList = arInvoiceDAO.getArInvoiceDetails(screenName, lclArInvoiceForm.getFileNumberId());
        if (CommonUtils.isNotEmpty(request.getParameter("headerId"))) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(lclArInvoiceForm.getHeaderId()));
            request.setAttribute("closedBy", s[0]);
            request.setAttribute("auditedBy", s[1]);
            lclArInvoiceForm.setVoyTerminalNo(s[2]);
        }
        request.setAttribute("listFlag", true);
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("todayDate", new Date());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward displayManualRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        String fileNumber = request.getParameter("fileNumber");
        String terminate = request.getParameter("terminate");
        String consoTerminate = request.getParameter("consoTerminate");
        lclArInvoiceForm.setComments(request.getParameter("comment"));
        lclArInvoiceForm.setConsoTerminate(consoTerminate);
        //LclDwr lclDwr = new LclDwr();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        //lclDwr.lclDomTermination(fileNumberId, "X", "Cancelled by Customer", terminateComment, String.valueOf(user.getUserId()));
        List<LclBookingAc> lclBookingAcList = new LclCostChargeDAO().executeQuery("from LclBookingAc where lclFileNumber.id=" + Long.parseLong(fileNumberId) + " AND manualEntry=1");
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        ArRedInvoice arRedInvoice = null;
        ArRedInvoice cuArRedInvoice = new ArRedInvoice();
        if (lclBooking.getClientAcct() != null) {
            cuArRedInvoice.setCustomerName(lclBooking.getClientContact().getCompanyName());
            cuArRedInvoice.setContactName(lclBooking.getClientContact().getContactName());
            cuArRedInvoice.setAddress(lclBooking.getClientContact().getAddress());
            cuArRedInvoice.setCustomerNumber(lclBooking.getClientAcct().getAccountno());
            cuArRedInvoice.setPhoneNumber(lclBooking.getClientContact().getPhone1());
            cuArRedInvoice.setCustomerType(lclBooking.getClientAcct().getAcctType());
        }
        double amount = 0d;
        for (LclBookingAc lclBookingAc : lclBookingAcList) {
            amount += lclBookingAc.getArAmount().doubleValue();
        }
        cuArRedInvoice.setInvoiceAmount(amount);
        arRedInvoice = arInvoiceDAO.saveAndReturn(cuArRedInvoice);
        PropertyUtils.copyProperties(lclArInvoiceForm, arRedInvoice);
        for (LclBookingAc lclBookingAc : lclBookingAcList) {
            ArRedInvoiceCharges invoiceCharges = new ArRedInvoiceCharges();
            invoiceCharges.setArRedInvoiceId(arRedInvoice.getId());
            invoiceCharges.setChargeCode(lclBookingAc.getArglMapping().getChargeCode());
            invoiceCharges.setDescription(lclBookingAc.getArglMapping().getChargeDescriptions());
            invoiceCharges.setGlAccount(glMappingDAO.dervieGlAccount(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAc.getArglMapping().getShipmentType(),
                    lclBookingAc.getLclFileNumber().getLclBooking().getTerminal().getTrmnum(), "R"));
            invoiceCharges.setShipmentType(lclBookingAc.getArglMapping().getShipmentType());
            invoiceCharges.setBlDrNumber(fileNumber);
            invoiceCharges.setAmount(lclBookingAc.getArAmount().doubleValue());
            arRedInvoiceChargeDAO.save(invoiceCharges);
        }
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
        request.setAttribute("arRedInvoice", lclArInvoiceForm);
        request.setAttribute("newItemFlag", true);
        request.setAttribute("fileNumber", fileNumber);
        request.setAttribute("terminate", terminate);
        request.setAttribute("consoTerminate", consoTerminate);
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("todayDate", new Date());
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward unPost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        String invoiceId = request.getParameter("invoiceId");
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arInvoiceDAO.findById(Integer.parseInt(invoiceId));
            if (arRedInvoice != null) {
                List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
                arInvoiceDAO.lclUnManifestAccruals(arRedInvoice, invoiceChargeList, loginUser.getFirstName(), null);
            }
            String message = "Invoice " + arRedInvoice.getInvoiceNumber() + " Reversed";
            this.addPostDetails(lclArInvoiceForm, arRedInvoice, message, request);
            arRedInvoice.setStatus("I");
            arRedInvoice.setPostedDate(null);
            new ArRedInvoiceDAO().saveOrUpdate(arRedInvoice);
        }
        String screenName = "Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) ? SCREENNAME_DR_AR_INVOICE : "LCLE DR";
        List<ArRedInvoice> invoiceList = arInvoiceDAO.getArInvoiceDetails(screenName, fileNumberId);
        request.setAttribute("listFlag", request.getParameter("listFlag"));
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("todayDate", new Date());
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        String invoiceId = request.getParameter("invoiceId");
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arInvoiceDAO.findById(Integer.parseInt(invoiceId));
            request.setAttribute("arRedInvoice", arRedInvoice);
            List<ArRedInvoiceCharges> invoiceChargeList = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", Integer.parseInt(invoiceId));
            request.setAttribute("invoiceChargeList", invoiceChargeList);
        }
        request.setAttribute("listFlag", request.getParameter("listFlag"));
        request.setAttribute("newItemFlag", request.getParameter("newItemFlag"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        if (CommonUtils.isNotEmpty(request.getParameter("headerId"))) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(request.getParameter("headerId")));
            request.setAttribute("closedBy", s[0]);
            request.setAttribute("auditedBy", s[1]);
            lclArInvoiceForm.setVoyTerminalNo(s[2]);
        }
        request.setAttribute("todayDate", new Date());
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        ArRedInvoice cuArRedInvoice = lclArInvoiceForm.getArRedInvoice();
        cuArRedInvoice.setFileNo(lclArInvoiceForm.getFileNumberId());
        cuArRedInvoice.setBlNumber(lclArInvoiceForm.getFileNumber());
        //   String oldInvoiceNo = arInvoiceDAO.findMaxInvoiceNumber();
        cuArRedInvoice.setDate(DateUtils.parseDate(lclArInvoiceForm.getDate(), "dd-MMM-yyyy"));
        cuArRedInvoice.setDueDate(DateUtils.parseDate(lclArInvoiceForm.getDueDate(), "dd-MMM-yyyy"));
        User user = getCurrentUser(request);

        ArInvoiceNumberThread thread = new ArInvoiceNumberThread();
        cuArRedInvoice.setInvoiceNumber(thread.getInvoiceNumber());
        cuArRedInvoice.setInvoiceBy(user.getFirstName() + " " + user.getLastName());

        if ("Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName())) {
            cuArRedInvoice.setFileType("LCLI");
            cuArRedInvoice.setScreenName("LCLI DR");
        } else {
            cuArRedInvoice.setFileType("LCLE");
            cuArRedInvoice.setScreenName("LCLE DR");
        }
        new ArRedInvoiceDAO().saveOrUpdate(cuArRedInvoice);
        ArRedInvoice arRedInvoice = arInvoiceDAO.findById(cuArRedInvoice.getId());
        lclArInvoiceForm.setArRedInvoice(arRedInvoice);
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        lclArInvoiceForm.getArRedInvoice().setInvoiceAmount(amount);
        arInvoiceDAO.update(lclArInvoiceForm.getArRedInvoice());
        request.setAttribute("arRedInvoice", lclArInvoiceForm.getArRedInvoice());
        request.setAttribute("newItemFlag", true);
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("terminate", lclArInvoiceForm.getPageName());
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        if (CommonUtils.isNotEmpty(lclArInvoiceForm.getHeaderId())) {
            String[] s = new LclSsHeaderDAO().getLclSsHeaderValues(Long.parseLong(lclArInvoiceForm.getHeaderId()));
            request.setAttribute("closedBy", s[0]);
            request.setAttribute("auditedBy", s[1]);
            lclArInvoiceForm.setVoyTerminalNo(s[2]);
        }
        request.setAttribute("lclArInvoiceForm", lclArInvoiceForm);
        request.setAttribute("todayDate", new Date());
        if (!"Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) && CommonUtils.isNotEmpty(lclArInvoiceForm.getPageName())
                && "Terminate".equalsIgnoreCase(lclArInvoiceForm.getPageName())) {
            LclDwr lclDwr = new LclDwr();
            String terminateComment = lclArInvoiceForm.getComments();
            request.setAttribute("closePopup", "close");
            lclDwr.lclDomTermination(lclArInvoiceForm.getFileNumberId(), "X", "Cancelled by Customer", terminateComment, String.valueOf(user.getUserId()), lclArInvoiceForm.getConsoTerminate());
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward post(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        String forward_name = "";
        String invoiceId = request.getParameter("invoiceId");
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arInvoiceDAO.findById(Integer.parseInt(invoiceId));
            arRedInvoice.setFileNo(lclArInvoiceForm.getFileNumberId());
            arRedInvoice.setStatus("I");
            arRedInvoice.setPostedDate(new Date());
            request.setAttribute("arRedInvoice", arRedInvoice);
            List<CustomerContact> contactList = null;
            if ("Exports".equalsIgnoreCase(lclArInvoiceForm.getModuleName())) {
                contactList = new CustomerContactDAO().checkCodeKForInvoiceNotification(arRedInvoice.getCustomerNumber());
                forward_name = "postInvoice";
            } else {
                contactList = new CustomerContactDAO().checkCodeK(arRedInvoice.getCustomerNumber());
                forward_name = "postImportInvoice";
            }
            request.setAttribute("contactList", contactList);
        }
        request.setAttribute("todayDate", new Date());
        request.setAttribute("closePopup", "");
        return mapping.findForward(forward_name);
    }

    public ActionForward saveCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;//save invoice charge
        String shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        if (LCL_EXPORT.equalsIgnoreCase(lclArInvoiceForm.getModuleName())) {
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
        }
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        if (CommonUtils.isNotEmpty(lclArInvoiceForm.getTerminal())) {
            if (lclArInvoiceForm.getTerminal().contains("-")) {
                String[] terminalNo = lclArInvoiceForm.getTerminal().split("-");
                lclArInvoiceForm.setTerminal(terminalNo[0]);
                lclArInvoiceForm.getArRedInvoiceCharges().setTerminal(terminalNo[0]);
            } else {
                lclArInvoiceForm.getArRedInvoiceCharges().setTerminal(lclArInvoiceForm.getTerminal());
            }
        }
        lclArInvoiceForm.getArRedInvoiceCharges().setChargeCode(lclArInvoiceForm.getChargeCode());
        String revExp = "R";
        if (shipmentType.equalsIgnoreCase(LCL_SHIPMENT_TYPE_EXPORT)) {
            String bookingType = new LCLBookingDAO().getExportBookingColumnValue("booking_type", lclArInvoiceForm.getFileNumberId());
            String polId = new LCLBookingDAO().getExportBookingColumnValue("pol_id", lclArInvoiceForm.getFileNumberId());
            String column1 = "poo_id";
            if (bookingType.equalsIgnoreCase("T")) {
                LclBookingImport bookingImport = new LclBookingImportDAO()
                        .getByProperty("lclFileNumber.id", Long.parseLong(lclArInvoiceForm.getFileNumberId()));
                column1 = "pod_id";
                polId = bookingImport.getUsaPortOfExit().getId().toString();
            }
            String originId = new LCLBookingDAO().getExportBookingColumnValue(column1, lclArInvoiceForm.getFileNumberId());
            String voyOriginID = new LclSsHeaderDAO().getvoyageOriginIdWithFileId(lclArInvoiceForm.getFileNumberId());
            if (voyOriginID.equalsIgnoreCase("0")) {
                voyOriginID = polId;
            }
            lclArInvoiceForm.getArRedInvoiceCharges().setGlAccount(glMappingDAO
                    .getLclExportDerivedGlAccount(lclArInvoiceForm.getChargeId(),
                            lclArInvoiceForm.getTerminal(), originId, voyOriginID));
        } else {
            lclArInvoiceForm.getArRedInvoiceCharges().setGlAccount(glMappingDAO
                    .dervieGlAccount(lclArInvoiceForm.getChargeCode(), shipmentType, lclArInvoiceForm.getTerminal(), revExp));
        }
        request.setAttribute("arRedInvoice", lclArInvoiceForm.getArRedInvoice());
        request.setAttribute("newItemFlag", true);
        lclArInvoiceForm.getArRedInvoiceCharges().setInvoiceNumber(lclArInvoiceForm.getArRedInvoice().getInvoiceNumber());
        lclArInvoiceForm.getArRedInvoiceCharges().setArRedInvoiceId(lclArInvoiceForm.getArRedInvoiceId());
        lclArInvoiceForm.getArRedInvoiceCharges().setBlDrNumber(lclArInvoiceForm.getFileNumber());
        lclArInvoiceForm.getArRedInvoiceCharges().setShipmentType(shipmentType);
        arRedInvoiceChargeDAO.save(lclArInvoiceForm.getArRedInvoiceCharges());
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", lclArInvoiceForm.getArRedInvoiceId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        lclArInvoiceForm.getArRedInvoice().setInvoiceAmount(amount);
        arInvoiceDAO.update(lclArInvoiceForm.getArRedInvoice());
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("terminate", lclArInvoiceForm.getPageName());
        request.setAttribute("todayDate", new Date());
        request.setAttribute("lclArInvoiceForm", lclArInvoiceForm);
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        String invoiceId = request.getParameter("invoiceId");
        if (CommonUtils.isNotEmpty(invoiceId)) {
            ArRedInvoice arRedInvoice = arInvoiceDAO.findById(Integer.parseInt(invoiceId));
            List<ArRedInvoiceCharges> invoiceChargeList = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", Integer.parseInt(invoiceId));
            for (ArRedInvoiceCharges arc : invoiceChargeList) {
                if (arc != null) {
                    arRedInvoiceChargeDAO.delete(arc);
                }
            }
            if (arRedInvoice != null) {
                arInvoiceDAO.delete(arRedInvoice);
            }
            //request.setAttribute("listFlag", false);
            request.setAttribute("newItemFlag", false);
            request.setAttribute("todayDate", new Date());
        }
        String screenName = "Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) ? SCREENNAME_DR_AR_INVOICE : "LCLE DR";
        List<ArRedInvoice> invoiceList = arInvoiceDAO.getArInvoiceDetails(screenName, lclArInvoiceForm.getFileNumberId());
        request.setAttribute("invoiceList", invoiceList);
        //request.setAttribute("listFlag", request.getParameter("listFlag"));
        request.setAttribute("lclArInvoiceForm", lclArInvoiceForm);
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteInvoiceCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        request.setAttribute("fileNumber", lclArInvoiceForm.getFileNumber());
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("newItemFlag", true);
        String invoiceChargeId = request.getParameter("invoiceChargeId");
        if (CommonUtils.isNotEmpty(invoiceChargeId)) {
            ArRedInvoiceCharges arRedInvoiceCharges = arRedInvoiceChargeDAO.findById(Integer.parseInt(invoiceChargeId));
            if (arRedInvoiceCharges != null) {
                new ArRedInvoiceChargesDAO().delete(arRedInvoiceCharges);
            }
        }
        request.setAttribute("todayDate", new Date());
        List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", lclArInvoiceForm.getArRedInvoiceId());
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceChargeList) {
            amount += arc.getAmount();
        }
        lclArInvoiceForm.getArRedInvoice().setInvoiceAmount(amount);
        arInvoiceDAO.update(lclArInvoiceForm.getArRedInvoice());
        request.setAttribute("arRedInvoice", lclArInvoiceForm.getArRedInvoice());
        request.setAttribute("invoiceChargeList", invoiceChargeList);
        request.setAttribute("closePopup", "");
        return mapping.findForward(AR_RED_INVOICE_CHARGE);
    }

    public ActionForward postArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        Date now = new Date();
        User loginUser = getCurrentUser(request);
        ArRedInvoice arRedInvoice = arInvoiceDAO.findById(lclArInvoiceForm.getArRedInvoiceId());
        lclArInvoiceForm.setNotification("Send");
        if (null != arRedInvoice) {
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
            lclArInvoiceForm.setCustomerName(arRedInvoice.getCustomerName());
            lclArInvoiceForm.setCustomerNumber(arRedInvoice.getCustomerNumber());
            arInvoiceDAO.lclManifestAccruals(arRedInvoice, invoiceChargeList, loginUser.getFirstName(), null, arRedInvoice.getBlNumber());
            String message = "Invoice " + arRedInvoice.getInvoiceNumber() + " Posted";
            this.addPostDetails(lclArInvoiceForm, arRedInvoice, message, request);
            arRedInvoice.setReadyToPost("M");
            arRedInvoice.setStatus("AR");
            arRedInvoice.setPostedDate(now);
            if ("Send".equalsIgnoreCase(lclArInvoiceForm.getNotification())) {
                String textMessage = "The following AR Invoice have been Posted:";
                textMessage += "<br> Customer : " + arRedInvoice.getCustomerName();
                textMessage += "<br> Invoice# : " + arRedInvoice.getInvoiceNumber();
                textMessage += "<br> Invoice Date : " + arRedInvoice.getDate();
                textMessage += "<br> Invoice Amount : " + arRedInvoice.getInvoiceAmount();
                String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/" + LclReportConstants.FOLDER_NAME + "/" + LclReportConstants.MODULENAME
                        + "/" + LclReportConstants.FOLDERNAME_ARINVOICE + "/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/";
                File dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String filelocation = fileLocation + "Invoice_" + arRedInvoice.getInvoiceNumber() + "_" + loginUser.getLoginName()
                        + "_" + DateUtils.formatDate(now, "_yyyyMMdd_HHmmss") + ".pdf";
                String realPath = this.getServlet().getServletContext().getRealPath("/");
                LclArInvoicePdfCreator lclArInvoicePdfCreator = new LclArInvoicePdfCreator();
                lclArInvoicePdfCreator.createReport(arRedInvoice, filelocation, realPath, loginUser, null, null, false);
                List<CustomerContact> customerContactList = new ArrayList();
                if ("Exports".equalsIgnoreCase(lclArInvoiceForm.getModuleName())) {
                    customerContactList = new CustomerContactDAO().checkCodeKForInvoiceNotification(arRedInvoice.getCustomerNumber());
                    for (Object object : customerContactList) {
                        CustomerContact customerContact = (CustomerContact) object;
                        if (null != customerContact.getCodek()) {
                            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                            String subject = LoadLogisoftProperties.getProperty("application.email.companyName") + " INVOICE # " + arRedInvoice.getInvoiceNumber() + "-" + arRedInvoice.getCustomerName();
                            if (CommonUtils.isNotEmpty(customerContact.getEmail())) {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getEmail(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getEmail(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BOOKING", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "LCLARINVOICE", "", loginUser.getLoginName());
                            } else {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getFax(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getFax(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BOOKING", null, CommonConstants.CONTACT_MODE_FAX, 0, new Date(), "LCLARINVOICE", "", loginUser.getLoginName());
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
                    customerContactList = new CustomerContactDAO().checkCodeK(arRedInvoice.getCustomerNumber());
                    String brandValue = new LclFileNumberDAO().getBusinessUnit(arRedInvoice.getFileNo());
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
                                emailSchedulerVO.setEmailInfo("BOOKING", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "LCLARINVOICE", "", loginUser.getLoginName());
                            } else {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getFax(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getFax(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BOOKING", null, CommonConstants.CONTACT_MODE_FAX, 0, new Date(), "LCLARINVOICE", "", loginUser.getLoginName());
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
        arRedInvoice = arInvoiceDAO.saveAndReturn(arRedInvoice);
        String screenName = "Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) ? SCREENNAME_DR_AR_INVOICE : "LCLE DR";
        List<ArRedInvoice> invoiceList = arInvoiceDAO.getArInvoiceDetails(screenName, lclArInvoiceForm.getFileNumberId());
        request.setAttribute("arRedInvoiceform", lclArInvoiceForm);
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("arInvoice", arRedInvoice);
        request.setAttribute("listFlag", true);
        request.setAttribute("todayDate", now);
        request.setAttribute("fileNumber", lclArInvoiceForm.getFileNumber());
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward postInvoiceWithoutEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        User loginUser = getCurrentUser(request);
        ArRedInvoice arRedInvoice = arInvoiceDAO.findById(lclArInvoiceForm.getArRedInvoiceId());
        lclArInvoiceForm.setNotification("Send");
        if (null != arRedInvoice) {
            List<ArRedInvoiceCharges> invoiceChargeList = arRedInvoiceChargeDAO.findByProperty("arRedInvoiceId", arRedInvoice.getId());
            lclArInvoiceForm.setCustomerName(arRedInvoice.getCustomerName());
            lclArInvoiceForm.setCustomerNumber(arRedInvoice.getCustomerNumber());
            arInvoiceDAO.lclManifestAccruals(arRedInvoice, invoiceChargeList, loginUser.getFirstName(), null, arRedInvoice.getBlNumber());
            String message = "Invoice " + arRedInvoice.getInvoiceNumber() + " Posted";
            this.addPostDetails(lclArInvoiceForm, arRedInvoice, message, request);
            arRedInvoice.setReadyToPost("M");
            arRedInvoice.setStatus("AR");
            arRedInvoice.setPostedDate(new Date());
            request.setAttribute("displayMessage", "This invoice " + arRedInvoice.getInvoiceNumber() + " is Posted to AR Succesfully");
        }
        arRedInvoice = arInvoiceDAO.saveAndReturn(arRedInvoice);
        String screenName = "Imports".equalsIgnoreCase(lclArInvoiceForm.getModuleName()) ? SCREENNAME_DR_AR_INVOICE : "LCLE DR";
        List<ArRedInvoice> invoiceList = arInvoiceDAO.getArInvoiceDetails(screenName, lclArInvoiceForm.getFileNumberId());
        request.setAttribute("arRedInvoiceform", lclArInvoiceForm);
        request.setAttribute("invoiceList", invoiceList);
        request.setAttribute("arInvoice", arRedInvoice);
        request.setAttribute("listFlag", true);
        request.setAttribute("todayDate", new Date());
        request.setAttribute("fileNumber", lclArInvoiceForm.getFileNumber());
        request.setAttribute("closePopup", "");
        return mapping.findForward(SUCCESS);
    }

    public void addPostDetails(LclArInvoiceForm lclArInvoiceForm, ArRedInvoice arRedInvoice, String message, HttpServletRequest request) throws Exception {
        LclRemarks lclRemarks = null == new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + new Long(lclArInvoiceForm.getFileNumberId()) + " AND type='AR'") ? new LclRemarks() : new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + new Long(lclArInvoiceForm.getFileNumberId()) + " AND type='AR'");
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(new Long(lclArInvoiceForm.getFileNumberId()));
        lclRemarks.setRemarks(message);
        lclRemarks.setType("AR");
        lclRemarks.setLclFileNumber(lclFileNumber);
        lclRemarks.setEnteredBy(getCurrentUser(request));
        lclRemarks.setModifiedBy(getCurrentUser(request));
        lclRemarks.setModifiedDatetime(new Date());
        lclRemarks.setEnteredDatetime(new Date());
        request.setAttribute("closePopup", "");
        new LclRemarksDAO().saveOrUpdate(lclRemarks);
    }

    public ActionForward checkCodeFCountByAcctNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclArInvoiceForm lclArInvoiceForm = (LclArInvoiceForm) form;
        CustomerContactDAO customerContacDao = new CustomerContactDAO();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String acctNo = request.getParameter("acctNo");
        List<ImportsManifestBean> checkCodeFByAcctNo = null;
        String codeKContact = "";
        if ("Exports".equalsIgnoreCase(lclArInvoiceForm.getModuleName())) {
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
