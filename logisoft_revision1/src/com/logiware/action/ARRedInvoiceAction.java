package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.reports.ArRedInvoicePdfCreator;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.form.ARRedInvoiceForm;
import com.logiware.hibernate.dao.ArRedInvoiceChargesDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ARRedInvoiceAction extends DispatchAction {

    private GlMappingDAO glMappingDAO = new GlMappingDAO();
    ArRedInvoiceBC arRedInvoiceBC = new ArRedInvoiceBC();
    ArRedInvoiceChargesDAO arRedInvoiceChargesDAO = new ArRedInvoiceChargesDAO();

    public ActionForward showHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        ArRedInvoice arRedInvoice = new ArRedInvoice();
        arRedInvoice.setDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +30);
        arRedInvoice.setDueDate(cal.getTime());
        boolean importFlag = false;
        if (CommonUtils.isNotEmpty(arRedInvoice.getBol())) {
            FclBl fclBl = new FclBlDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            importFlag = null != fclBl && "I".equalsIgnoreCase(fclBl.getImportFlag());
        } else if (CommonUtils.isNotEmpty(arRedInvoice.getBookingNo())) {
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            importFlag = null != bookingFcl && "I".equalsIgnoreCase(bookingFcl.getImportFlag());
        } else if (CommonUtils.isNotEmpty(arRedInvoice.getQuuoteNo())) {
            Quotation quotation = new QuotationDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            importFlag = null != quotation && "I".equalsIgnoreCase(quotation.getFileType());
        }
        request.setAttribute("importFlag", importFlag);
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        return mapping.findForward("success");
    }

    public ActionForward addFromBl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        ArRedInvoice arRedInvoice = new ArRedInvoice();
        arRedInvoice.setDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +30);
        arRedInvoice.setDueDate(cal.getTime());
        boolean importFlag = false;
        if ("BL".equalsIgnoreCase(arRedInvoiceForm.getScreenName())) {
            FclBl fclBl = new FclBlDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            request.setAttribute("fclBl", fclBl);
            if (null != fclBl && "I".equalsIgnoreCase(fclBl.getImportFlag())) {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLI", "AC"));
            } else {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLE", "AR"));
            }
            if (null != fclBl && CommonUtils.isNotEmpty(fclBl.getBillingTerminal())) {
                String isstrm = fclBl.getBillingTerminal().contains("-") ? fclBl.getBillingTerminal().substring(fclBl.getBillingTerminal().indexOf("-") + 1) : "";
                request.setAttribute("issuingTerminal", isstrm);
            }
            importFlag = null != fclBl && "I".equalsIgnoreCase(fclBl.getImportFlag());
        } else if ("BOOKING".equalsIgnoreCase(arRedInvoiceForm.getScreenName())) {
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            request.setAttribute("fclBl", bookingFcl);
            if (null != bookingFcl && "I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLI", "AC"));
            } else {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLE", "AR"));
            }
            if (null != bookingFcl && CommonUtils.isNotEmpty(bookingFcl.getIssuingTerminal())) {
                String isstrm = bookingFcl.getIssuingTerminal().contains("-") ? bookingFcl.getIssuingTerminal().substring(bookingFcl.getIssuingTerminal().indexOf("-") + 1) : "";
                request.setAttribute("issuingTerminal", isstrm);
            }
            importFlag = null != bookingFcl && "I".equalsIgnoreCase(bookingFcl.getImportFlag());
        } else if ("QUOTE".equalsIgnoreCase(arRedInvoiceForm.getScreenName())) {
            Quotation quotation = new QuotationDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            request.setAttribute("fclBl", quotation);
            if (null != quotation && "I".equalsIgnoreCase(quotation.getFileType())) {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLI", "AC"));
            } else {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLE", "AR"));
            }
            if (null != quotation && CommonUtils.isNotEmpty(quotation.getIssuingTerminal())) {
                String isstrm = quotation.getIssuingTerminal().contains("-") ? quotation.getIssuingTerminal().substring(quotation.getIssuingTerminal().indexOf("-") + 1) : "";
                request.setAttribute("issuingTerminal", isstrm);
            }
            importFlag = null != quotation && "I".equalsIgnoreCase(quotation.getFileType());
        }
        request.setAttribute("importFlag", importFlag);
        request.setAttribute("arRedInvoice", arRedInvoice);
        arRedInvoiceForm.setCusName("");
        arRedInvoiceForm.setCusNumber("");
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        return mapping.findForward("success");
    }

    public ActionForward listArInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        List<Integer> arInvoiceIdList = new ArrayList<Integer>();
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        String groupFileNo = "";
        List cfclArinvoiceList = new ArrayList();
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getVoyInternal())) {
            groupFileNo = this.getCfclLinkedDr(arRedInvoiceForm.getVoyInternal());
            arInvoiceIdList = arRedInvoiceDAO.getArInvoiceId(groupFileNo);
            if (!arInvoiceIdList.isEmpty()) {
                cfclArinvoiceList = arRedInvoiceDAO.getArInvoiceDetails(arInvoiceIdList);
            }
        }
        List redInvoiceList = arRedInvoiceDAO.findByProperty("fileNo", arRedInvoiceForm.getFileNo());
        redInvoiceList.addAll(cfclArinvoiceList);
        boolean importFlag = (null != arRedInvoiceForm.getFileType() && arRedInvoiceForm.getFileType().equalsIgnoreCase("I"));
        request.setAttribute("arRedInvoiceList", redInvoiceList);
        request.setAttribute("importFlag", importFlag);
        request.setAttribute("fileNo", arRedInvoiceForm.getFileNo());
        return mapping.findForward("searchArRedInvoice");
    }

    public ActionForward saveOrupdateArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        String apInvoiceId = arRedInvoiceForm.getArRedInvoiceId();
        ArRedInvoice arRedInvoice = new ArRedInvoice();
        if (CommonUtils.isNotEmpty(apInvoiceId)) {
            arRedInvoice = arRedInvoiceBC.updateArRedInvoice(arRedInvoiceForm, loginUser);
        } else {
            arRedInvoice = arRedInvoiceBC.saveArRedInvoiceDetails(arRedInvoiceForm, loginUser);
        }
        request.setAttribute("userName", loginUser.getFirstName() + " " + loginUser.getLastName());
        String fileNo = CommonUtils.isNotEmpty(arRedInvoice.getScreenName()) ? arRedInvoice.getFileNo() : "";
        request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        request.setAttribute("importFlag", (null != arRedInvoice.getFileType() && arRedInvoice.getFileType().equalsIgnoreCase("I")));
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            getRedInvoiceData(arRedInvoiceForm, request);
        }
        request.setAttribute("arRedInvoice", arRedInvoiceBC.getArRedInvoice(arRedInvoice.getId()));
        return mapping.findForward("success");
    }

    public ActionForward searchArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        request.setAttribute("arRedInvoiceList", arRedInvoiceBC.getInvoices(arRedInvoiceForm));
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("fileNo", arRedInvoiceForm.getFileNo());
        return mapping.findForward("searchArRedInvoice");
    }

    public ActionForward addLineItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ArRedInvoice arRedInvoice = arRedInvoiceBC.saveArRedInvoice(arRedInvoiceForm, loginUser);
        if (null != arRedInvoice) {
            ArRedInvoiceCharges arRedInvoiceCharges = new ArRedInvoiceCharges(arRedInvoiceForm);
            arRedInvoiceCharges.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
            arRedInvoiceCharges.setArRedInvoiceId(arRedInvoice.getId());
            arRedInvoiceCharges.setGlAccount(new GlMappingDAO().dervieGlAccount(arRedInvoiceForm.getChargeCode(), arRedInvoiceForm.getShipmentType(), arRedInvoiceForm.getTerminalCode(), "R"));
            StringBuilder desc = new StringBuilder("Added Charge For Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
            desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
            desc.append(" ,ChargeCode '").append(arRedInvoiceCharges.getChargeCode()).append("'");
            desc.append(" ,Amount '").append(arRedInvoiceCharges.getAmount()).append("'");
            desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String moduleRefId = "";
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            new NotesBC().saveNotes(moduleRefId, loginUser.getLoginName(), desc.toString());
            arRedInvoiceChargesDAO.save(arRedInvoiceCharges);
            request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));

        }
        request.setAttribute("userName", loginUser.getFirstName() + " " + loginUser.getLastName());
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            getRedInvoiceData(arRedInvoiceForm, request);
        }
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("importFlag", (null != arRedInvoice.getFileType() && arRedInvoice.getFileType().equalsIgnoreCase("I")));
        return mapping.findForward("success");
    }

    public ActionForward deleteArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getArRedInvoiceId())) {
            ArRedInvoice arRedInvoice = new ArRedInvoiceDAO().findById(Integer.parseInt(arRedInvoiceForm.getArRedInvoiceId()));
            StringBuilder desc = new StringBuilder("Deleted Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
            desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
            desc.append("  by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String moduleRefId = "";
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            new NotesBC().saveNotes(moduleRefId, loginUser.getLoginName(), desc.toString());
            List chargesList = arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId());
            for (Object object : chargesList) {
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                desc = new StringBuilder("Deleted Charge For Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
                desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
                desc.append(" ,ChargeCode '").append(arRedInvoiceCharges.getChargeCode()).append("'");
                desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                new NotesBC().saveNotes(moduleRefId, loginUser.getLoginName(), desc.toString());
                arRedInvoiceChargesDAO.delete(arRedInvoiceCharges);
            }
            request.setAttribute("importFlag", (null != arRedInvoice.getFileType() && arRedInvoice.getFileType().equalsIgnoreCase("I")));
            new ArRedInvoiceDAO().delete(arRedInvoice);
        }
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        arRedInvoiceForm.setCusName("");
        arRedInvoiceForm.setCusNumber("");
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            getRedInvoiceData(arRedInvoiceForm, request);
        }
        return mapping.findForward("success");
    }

    public ActionForward deleteAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ArRedInvoice arRedInvoice = arRedInvoiceBC.getAPInvoice(arRedInvoiceForm.getArRedInvoiceId());
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getAccrualsId())) {
            ArRedInvoiceCharges arRedInvoiceCharges = arRedInvoiceChargesDAO.findById(Integer.parseInt(arRedInvoiceForm.getAccrualsId()));
            if (CommonUtils.isNotEmpty(arRedInvoice.getInvoiceAmount()) && CommonUtils.isNotEmpty(arRedInvoiceCharges.getAmount())) {
                arRedInvoice.setInvoiceAmount(arRedInvoice.getInvoiceAmount() - arRedInvoiceCharges.getAmount());
            }
            arRedInvoiceChargesDAO.delete(arRedInvoiceCharges);
        }
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        request.setAttribute("userName", loginUser.getFirstName() + " " + loginUser.getLastName());
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            getRedInvoiceData(arRedInvoiceForm, request);
        }
        request.setAttribute("importFlag", (null != arRedInvoice.getFileType() && arRedInvoice.getFileType().equalsIgnoreCase("I")));
        request.setAttribute("arRedInvoice", arRedInvoice);
        return mapping.findForward("success");
    }

    public ActionForward editArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ArRedInvoice arRedInvoice = arRedInvoiceBC.getAPInvoice(arRedInvoiceForm.getArRedInvoiceId());
        if (null != arRedInvoice) {
            arRedInvoiceForm.setCusName(arRedInvoice.getCustomerName());
            arRedInvoiceForm.setAccountNumber(arRedInvoice.getCustomerNumber());
        }
        if (null != arRedInvoice && null != arRedInvoice.getTerm() && !arRedInvoice.getTerm().trim().equals("")) {
            arRedInvoiceForm.setTermDesc(arRedInvoiceBC.getCreditTermDesc(arRedInvoice.getTerm()));
            arRedInvoiceForm.setTerm(arRedInvoice.getTerm());
        } else {
            arRedInvoiceForm.setTermDesc("Due Upon Receipt");
            arRedInvoiceForm.setTerm("11344");
        }
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));
        request.setAttribute("userName", loginUser.getFirstName() + " " + loginUser.getLastName());
        request.setAttribute("editArRedInvoice", "editArRedInvoice");
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            getRedInvoiceData(arRedInvoiceForm, request);
        }
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("importFlag", (null != arRedInvoice.getFileType() && arRedInvoice.getFileType().equalsIgnoreCase("I")));
        return mapping.findForward("success");
    }

    public ActionForward reversePostArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        String moduleRefId = "";
        ArRedInvoice arRedInvoice = arRedInvoiceBC.getAPInvoice(arRedInvoiceForm.getArRedInvoiceId());
        if (null != arRedInvoice) {
            String fileNo = CommonUtils.isNotEmpty(arRedInvoice.getScreenName()) ? arRedInvoice.getFileNo() : "";
            List lineItemList = arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId());
            double invoiceAmount = 0d;
            for (Object object : lineItemList) {
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                invoiceAmount += arRedInvoiceCharges.getAmount();
            }

            FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
            List<FclBlCorrections> correctionList = fclBlCorrectionsDAO.findByFileNo(fileNo);
            for (FclBlCorrections fclBlCorrections : correctionList) {
                if (fclBlCorrections.getPostedDate() == null) {
                    fclBlCorrections.setProfitAfterCn(fclBlCorrections.getProfitAfterCn() - invoiceAmount);
                    fclBlCorrections.setCurrentProfit(fclBlCorrections.getCurrentProfit() - invoiceAmount);
                    fclBlCorrectionsDAO.update(fclBlCorrections);
                }
            }

            arRedInvoice.setInvoiceAmount(invoiceAmount);
            arRedInvoiceForm.setCusName(arRedInvoice.getCustomerName());
            arRedInvoiceForm.setAccountNumber(arRedInvoice.getCustomerNumber());
            arRedInvoiceBC.unManifestAccruals(arRedInvoice, lineItemList, loginUser.getFirstName());
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            new NotesBC().saveNotes(moduleRefId, loginUser.getLoginName(), "Invoice " + arRedInvoice.getInvoiceNumber() + " Reversed");
            arRedInvoice.setReadyToPost(null);
            arRedInvoice.setStatus("I");
            arRedInvoice.setPostedDate(null);
            new ArRedInvoiceDAO().update(arRedInvoice);
        }
        List redInvoiceList = new ArRedInvoiceDAO().findByProperty("fileNo", arRedInvoiceForm.getFileNo());
        request.setAttribute("arRedInvoiceList", redInvoiceList);
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("fileNo", arRedInvoiceForm.getFileNo());
        return mapping.findForward("searchArRedInvoice");
    }

    public ActionForward postArRedInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        MessageResources messageResources = CommonConstants.loadMessageResources();
        User loginUser = (User) session.getAttribute("loginuser");
        String moduleRefId = "";
        BookingFcl bookingFcl = null;
        Quotation quotation = null;
        FclBl fclBl = null;
        fclBl = new FclBlDAO().getOriginalBl(arRedInvoiceForm.getFileNo());
        quotation = new QuotationDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
        bookingFcl = new BookingFclDAO().findbyFileNo(arRedInvoiceForm.getFileNo());
        String brand = "";
        if (null != fclBl && null != fclBl.getBrand()) {
            brand = fclBl.getBrand();
        } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
            brand = bookingFcl.getBrand();
        } else if (null != quotation && null != quotation.getBrand()) {
            brand = quotation.getBrand();
        }
        String companyName = "";
        if (brand.equals("Econo")) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if (brand.equals("OTI")) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }
        ArRedInvoice arRedInvoice = arRedInvoiceBC.getAPInvoice(arRedInvoiceForm.getArRedInvoiceId());
        if (null != arRedInvoice) {
            List lineItemList = arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId());
            double invoiceAmount = 0d;
            for (Object object : lineItemList) {
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                invoiceAmount += arRedInvoiceCharges.getAmount();
            }
            arRedInvoice.setInvoiceAmount(invoiceAmount);
            arRedInvoiceForm.setCusName(arRedInvoice.getCustomerName());
            arRedInvoiceForm.setAccountNumber(arRedInvoice.getCustomerNumber());
            arRedInvoiceBC.manifestAccruals(arRedInvoice, lineItemList, loginUser.getFirstName());
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));

            FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
            List<FclBlCorrections> correctionList = fclBlCorrectionsDAO.findByFileNo(moduleRefId);
            for (FclBlCorrections fclBlCorrections : correctionList) {
                if (fclBlCorrections.getPostedDate() == null) {
                    fclBlCorrections.setProfitAfterCn(fclBlCorrections.getProfitAfterCn() + invoiceAmount);
                    fclBlCorrections.setCurrentProfit(fclBlCorrections.getCurrentProfit() + invoiceAmount);
                    fclBlCorrectionsDAO.update(fclBlCorrections);
                }
            }

            new NotesBC().saveNotes(moduleRefId, loginUser.getLoginName(), "Invoice " + arRedInvoice.getInvoiceNumber() + " Posted");
            arRedInvoice.setReadyToPost("M");
            arRedInvoice.setStatus("AR");
            arRedInvoice.setPostedDate(new Date());
            new ArRedInvoiceDAO().update(arRedInvoice);
            if ("Send".equalsIgnoreCase(arRedInvoiceForm.getNotification())) {
                String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//RedInvoice//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
                File dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = fileLocation + "//" + arRedInvoice.getInvoiceNumber() + ".pdf";
                String realPath = this.getServlet().getServletContext().getRealPath("/");
                ArRedInvoicePdfCreator arRedInvoicePdfCreator = new ArRedInvoicePdfCreator();
                arRedInvoicePdfCreator.createReport(arRedInvoice, fileName, realPath, messageResources, loginUser);
                List customerContactList = new FclBlBC().checkForE1andE2OfCodek(arRedInvoice.getCustomerNumber(),arRedInvoice.getFileType());
                String textMessage = "The following AR Invoice have been Posted:";
                textMessage += "<br> Customer : " + arRedInvoice.getCustomerName();
                textMessage += "<br> Invoice# : " + arRedInvoice.getInvoiceNumber();
                textMessage += "<br> Invoice Date : " + arRedInvoice.getDate();
                textMessage += "<br> Invoice Amount : " + arRedInvoice.getInvoiceAmount();
                for (Object object : customerContactList) {
                    CustomerContact customerContact = (CustomerContact) object;
                    if (CommonUtils.isNotEmpty(customerContact.getEmail()) && null != customerContact.getCodek()
                            && CommonUtils.in(customerContact.getCodek().getCode(), "E","F")) {
                        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                        String subject = companyName + " INVOICE # " + arRedInvoice.getInvoiceNumber() + "-" + arRedInvoice.getCustomerName();
                        if ("I".equals(arRedInvoice.getFileType())) {
                            if (CommonUtils.isNotEmpty(customerContact.getEmail()) && CommonUtils.isEqual(customerContact.getCodek().getCode(), "E")) {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getEmail(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getEmail(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BL", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "BL", "", loginUser.getLoginName());
                            } else {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getFax(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getFax(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BL", null, CommonConstants.CONTACT_MODE_FAX, 0, new Date(), "BL", "", loginUser.getLoginName());
                            }
                        } else {
                            if (CommonUtils.isNotEmpty(customerContact.getEmail()) && CommonUtils.isEqual(customerContact.getCodek().getCode(), "E")) {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getEmail(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getEmail(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BL", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "BL", "", loginUser.getLoginName());
                            } else {
                                emailSchedulerVO.setEmailData(customerContact.getFirstName() + " " + customerContact.getLastName(), customerContact.getFax(), loginUser.getFirstName() + " " + loginUser.getLastName(), loginUser.getFax(), "", "", subject, "");
                                emailSchedulerVO.setEmailInfo("BL", null, CommonConstants.CONTACT_MODE_FAX, 0, new Date(), "BL", "", loginUser.getLoginName());
                            }
                        }
                        emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                        emailSchedulerVO.setTextMessage(textMessage);
                        emailSchedulerVO.setHtmlMessage(textMessage);
                        emailSchedulerVO.setFileLocation(fileName);
                        emailSchedulerVO.setModuleId(arRedInvoice.getInvoiceNumber());
                        emailschedulerDAO.save(emailSchedulerVO);
                    }
                }
            }
            request.setAttribute("displayMessage", "This invoice " + arRedInvoice.getInvoiceNumber() + " is Posted to AR Succesfully");
        }
        request.setAttribute("arRedInvoiceList", arRedInvoiceBC.getInvoices(arRedInvoiceForm));
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("fileNo", arRedInvoiceForm.getFileNo());
        return mapping.findForward("searchArRedInvoice");
    }

    public ActionForward contactNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        String apInvoiceId = arRedInvoiceForm.getArRedInvoiceId();
        ArRedInvoice arRedInvoice = new ArRedInvoice();
        if (CommonUtils.isNotEmpty(apInvoiceId)) {
            arRedInvoice = arRedInvoiceBC.updateArRedInvoice(arRedInvoiceForm, loginUser);
        } else {
            arRedInvoice = arRedInvoiceBC.saveArRedInvoiceDetails(arRedInvoiceForm, loginUser);
        }
        request.setAttribute("userName", loginUser.getFirstName() + " " + loginUser.getLastName());
        String fileNo = CommonUtils.isNotEmpty(arRedInvoice.getScreenName()) ? arRedInvoice.getFileNo() : "";
        request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("chargeCodeList", new ArrayList().add(new LabelValueBean("Select", "")));
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            getRedInvoiceData(arRedInvoiceForm, request);
        }
        request.setAttribute("arRedInvoice", arRedInvoiceBC.getArRedInvoice(arRedInvoice.getId()));
        request.setAttribute("ContactConfigE1andE2", new FclBlBC().checkForE1andE2OfCodek(arRedInvoice.getCustomerNumber(),arRedInvoice.getFileType()));
        request.setAttribute("importFlag", "I".equalsIgnoreCase(arRedInvoice.getFileType()));
        return mapping.findForward("success");
    }

    public void getRedInvoiceData(ARRedInvoiceForm arRedInvoiceForm, HttpServletRequest request) throws Exception {
        request.setAttribute("terminalCodeList", new DBUtil().getTerminalCodeList());
        ArRedInvoice arRedInvoice = new ArRedInvoice();
        arRedInvoice.setDate(new Date());
        request.setAttribute("arRedInvoice", arRedInvoice);
        if ("BL".equalsIgnoreCase(arRedInvoiceForm.getScreenName())) {
            FclBl fclBl = new FclBlDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            request.setAttribute("fclBl", fclBl);
            if (null != fclBl && "I".equalsIgnoreCase(fclBl.getImportFlag())) {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLI", "AC"));
            } else {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLE", "AR"));
            }
            if (null != fclBl && CommonUtils.isNotEmpty(fclBl.getBillingTerminal())) {
                String isstrm = fclBl.getBillingTerminal().contains("-") ? fclBl.getBillingTerminal().substring(fclBl.getBillingTerminal().indexOf("-") + 1) : "";
                request.setAttribute("issuingTerminal", isstrm);
            }
        } else if ("BOOKING".equalsIgnoreCase(arRedInvoiceForm.getScreenName())) {
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            request.setAttribute("fclBl", bookingFcl);
            if (null != bookingFcl && "I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLI", "AC"));
            } else {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLE", "AR"));
            }
            if (null != bookingFcl && CommonUtils.isNotEmpty(bookingFcl.getIssuingTerminal())) {
                String isstrm = bookingFcl.getIssuingTerminal().contains("-") ? bookingFcl.getIssuingTerminal().substring(bookingFcl.getIssuingTerminal().indexOf("-") + 1) : "";
                request.setAttribute("issuingTerminal", isstrm);
            }
        } else if ("QUOTE".equalsIgnoreCase(arRedInvoiceForm.getScreenName())) {
            Quotation quotation = new QuotationDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
            quotation.setImportFlag(quotation.getFileType());
            request.setAttribute("fclBl", quotation);
            if (null != quotation && "I".equalsIgnoreCase(quotation.getFileType())) {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLI", "AC"));
            } else {
                request.setAttribute("chargeCodeList", glMappingDAO.getChargeCodeArInvoice("FCLE", "AR"));
            }
            if (null != quotation && CommonUtils.isNotEmpty(quotation.getIssuingTerminal())) {
                String isstrm = quotation.getIssuingTerminal().contains("-") ? quotation.getIssuingTerminal().substring(quotation.getIssuingTerminal().indexOf("-") + 1) : "";
                request.setAttribute("issuingTerminal", isstrm);
            }
        }
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
    }

    public ActionForward searchInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        User user = (User) session.getAttribute("loginuser");
        request.setAttribute("userId", user.getUserId());
        request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        List l = new ArRedInvoiceDAO().searchInvoice(arRedInvoiceForm);
        List<ArRedInvoice> resultList = new ArrayList<ArRedInvoice>();
        for (Object object : l) {
            ArRedInvoice arRedInvoice = (ArRedInvoice) object;
            if (CommonUtils.isNotEmpty(arRedInvoice.getFileNo())) {
                arRedInvoice.setQuuoteNo(new QuotationDAO().findQuoteId(arRedInvoice.getFileNo()));
                arRedInvoice.setBookingNo(new BookingFclDAO().getBookingId(arRedInvoice.getFileNo()));
                arRedInvoice.setBol(new ArRedInvoiceDAO().findingBolByFileNo(arRedInvoice.getFileNo()));
                if ("I".equalsIgnoreCase(arRedInvoice.getFileType())) {
                    arRedInvoice.setSelectedMenu("Imports");
                    arRedInvoice.setItemId(new ItemDAO().getItemId("Quotes, Bookings, and BLs", "IMP"));
                } else {
                    arRedInvoice.setSelectedMenu("Exports");
                    arRedInvoice.setItemId(new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
                }
                if (CommonUtils.isNotEmpty(arRedInvoice.getBol())) {
                    arRedInvoice.setModuleId("FCLBL");
                } else if (CommonUtils.isNotEmpty(arRedInvoice.getBookingNo())) {
                    arRedInvoice.setModuleId("BOOKING");
                }
                if (CommonUtils.isNotEmpty(arRedInvoice.getQuuoteNo())) {
                    arRedInvoice.setModuleId("QUOTE");
                }

                resultList.add(arRedInvoice);
            }
        }
        request.setAttribute("invoicePoolList", resultList);
        return mapping.findForward("searchInvoice");
    }
    public ActionForward postArRedInvoiceWithoutMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ARRedInvoiceForm arRedInvoiceForm = (ARRedInvoiceForm) form;
        HttpSession session = request.getSession();
        MessageResources messageResources = CommonConstants.loadMessageResources();
        User loginUser = (User) session.getAttribute("loginuser");
        String moduleRefId = "";
        BookingFcl bookingFcl = null;
        Quotation quotation = null;
        FclBl fclBl = null;
        fclBl = new FclBlDAO().getOriginalBl(arRedInvoiceForm.getFileNo());
        quotation = new QuotationDAO().getFileNoObject(arRedInvoiceForm.getFileNo());
        bookingFcl = new BookingFclDAO().findbyFileNo(arRedInvoiceForm.getFileNo());
        String brand = "";
        if (null != fclBl && null != fclBl.getBrand()) {
            brand = fclBl.getBrand();
        } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
            brand = bookingFcl.getBrand();
        } else if (null != quotation && null != quotation.getBrand()) {
            brand = quotation.getBrand();
        }
        String companyName = "";
        if (brand.equals("Econo")) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if (brand.equals("OTI")) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }
        ArRedInvoice arRedInvoice = arRedInvoiceBC.getAPInvoice(arRedInvoiceForm.getArRedInvoiceId());
        if (null != arRedInvoice) {
            List lineItemList = arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId());
            double invoiceAmount = 0d;
            for (Object object : lineItemList) {
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                invoiceAmount += arRedInvoiceCharges.getAmount();
            }
            arRedInvoice.setInvoiceAmount(invoiceAmount);
            arRedInvoiceForm.setCusName(arRedInvoice.getCustomerName());
            arRedInvoiceForm.setAccountNumber(arRedInvoice.getCustomerNumber());
            arRedInvoiceBC.manifestAccruals(arRedInvoice, lineItemList, loginUser.getFirstName());
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            request.setAttribute("lineItems", arRedInvoiceChargesDAO.getCharges(arRedInvoice.getId()));

            FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
            List<FclBlCorrections> correctionList = fclBlCorrectionsDAO.findByFileNo(moduleRefId);
            for (FclBlCorrections fclBlCorrections : correctionList) {
                if (fclBlCorrections.getPostedDate() == null) {
                    fclBlCorrections.setProfitAfterCn(fclBlCorrections.getProfitAfterCn() + invoiceAmount);
                    fclBlCorrections.setCurrentProfit(fclBlCorrections.getCurrentProfit() + invoiceAmount);
                    fclBlCorrectionsDAO.update(fclBlCorrections);
                }
            }

            new NotesBC().saveNotes(moduleRefId, loginUser.getLoginName(), "Invoice " + arRedInvoice.getInvoiceNumber() + " Posted");
            arRedInvoice.setReadyToPost("M");
            arRedInvoice.setStatus("AR");
            arRedInvoice.setPostedDate(new Date());
            new ArRedInvoiceDAO().update(arRedInvoice);
         request.setAttribute("displayMessage", "This invoice " + arRedInvoice.getInvoiceNumber() + " is Posted to AR Succesfully");
        }
        request.setAttribute("arRedInvoiceList", arRedInvoiceBC.getInvoices(arRedInvoiceForm));
        request.setAttribute("arRedInvoice", arRedInvoice);
        request.setAttribute("arRedInvoiceform", arRedInvoiceForm);
        request.setAttribute("fileNo", arRedInvoiceForm.getFileNo());
        return mapping.findForward("searchArRedInvoice");
    }
    
    public String getCfclLinkedDr(String voyageNo) {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        StringBuilder sb = new StringBuilder();
        BigInteger ssHeaderId = null;
        List lclFileNumberList = null;
        String fileNumber[] = null;
        String groupFileNo = "";
        if (CommonUtils.isNotEmpty(voyageNo)) {
            ssHeaderId = bookingFclDAO.getSsHeaderId(voyageNo);
            lclFileNumberList = new BookingFclDAO().getFileNumber(ssHeaderId);
        }
        if (lclFileNumberList != null) {
            Object[] file = (Object[]) lclFileNumberList.get(0);
            if (file[1] != null && !file[1].equals("")) {
                fileNumber = file[1].toString().split(",");
                for (String drNo : fileNumber) {
                    sb.append("'").append(drNo).append("',");
                }
            }
            groupFileNo = StringUtils.removeEnd(sb.toString(), ",");
        }
        return groupFileNo;
    }
}
