/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.google.gson.Gson;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.INSURANCE_CHARGE_CODE;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.REMARKS_DR_AUTO_NOTES;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclBlChargesCalculation;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlPieceDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclOptionsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.logisoft.lcl.report.LclBLPdfCreator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBlForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclBlCostAndChargeForm;
import com.logiware.accounting.dao.LclExportManifestDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.common.filter.JspWrapper;
import com.logiware.thread.LclFileNumberThread;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Owner
 */
public class LclBlAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static final String NEW_BOOKING = "newBooking";
    private static final String COPY_DR = "copyDr";
    private static String CHARGE_DESC = "chargeDesc";
    private static final String LCL_BL = "lclBl";
    private LclUtils lclUtils = new LclUtils();
    private BlUtils blUtils = new BlUtils();
    private LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();

    public ActionForward newBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setCommodityList(null);
        lclSession.setRoutingOptionsList(null);
        session.setAttribute("lclSession", lclSession);
        LCLBlForm lCLBlForm = (LCLBlForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        if (null != loginUser.getTerminal()) {
            lCLBlForm.setTerminal(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
            lCLBlForm.setTrmnum(loginUser.getTerminal().getTrmnum());
        }
        request.setAttribute("loginName", loginUser.getLoginName() + " " + loginUser.getLastName());
        request.setAttribute("createdTime", new Date());
        request.setAttribute("lCLBlForm", lCLBlForm);
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        return mapping.findForward("newBooking");
    }

    public ActionForward editBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm lclBlForm = (LCLBlForm) form;
        LCLBlDAO lclBlDAO = new LCLBlDAO();
        String fileNoId = request.getParameter("fileNumberId");
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        LclBl lclBl = null;
        User loginUser = getCurrentUser(request);
        Long fileId = Long.parseLong(fileNoId);
        if (CommonUtils.isNotEmpty(fileNoId)) {
            lclBl = lclBlDAO.findById(fileId);
            lclBlDAO.getCurrentSession().evict(lclBl);
            request.setAttribute("consolidatedDocuments", new LclConsolidateDAO().getConsolidatesFileNumbers(fileId.toString()));
            lclBl.setClientContact(lclBl.getClientContact());
            lclBl.setShipContact(lclBl.getShipContact());
            lclBl.setFwdContact(lclBl.getFwdContact());
            lclBl.setConsContact(lclBl.getConsContact());
            lclBl.setSupContact(lclBl.getSupContact());
            lclBl.setNotyContact(lclBl.getNotyContact());
            lclBl.setThirdPartyContact(lclBl.getThirdPartyContact());
            lclBl.setAgentContact(lclBl.getAgentContact());
        }
        String remarkTypes[] = {REMARKS_TYPE_ROUTING_INSTRU, REMARKS_TYPE_EXPORT_REF};
        setRemarks(lclBlForm, fileId, remarkTypes);
        lclBlForm.setBookingFileNumberId(CommonUtils.isNotEmpty(request.getParameter("bookingFileNumberId"))
                ? Integer.parseInt(request.getParameter("bookingFileNumberId")) : fileId.intValue());
        lclBlForm.setLclBl(lclBl);

        lclBlForm.setPortOfOriginId(lclBl.getPortOfOrigin().getId());
        if (lclBl.getPortOfLoading() != null) {
            lclBlForm.setPortOfLoadingId(lclBl.getPortOfLoading().getId());
        }
        if (lclBl.getPortOfDestination() != null) {
            lclBlForm.setPortOfDestinationId(lclBl.getPortOfDestination().getId());
        }
        if (lclBl.getFinalDestination() != null) {
            lclBlForm.setFinalDestinationId(lclBl.getFinalDestination().getId());
        }
        if (null != lclBl.getAgentAcct()) {
            lclBlForm.getlclBl().setAgentContact(lclBl.getAgentContact());
        }
        blUtils.setCreditStauAndLimit(lclBl, request, loginUser);
        if (lclBlForm.getlclBl().getFwdContact() != null) {
            if (lclBlForm.getlclBl().getFwdAcct() != null && null != lclBlForm.getlclBl().getFwdAcct().getGeneralInfo()) {
                // lclBlForm.getlclBl().setFwdFmcNo(lclBlForm.getlclBl().getFwdAcct().getGeneralInfo().getFwFmcNo());
                lclBlForm.getlclBl().getFwdContact().setAddress(lclBlForm.getFwdContact().getAddress());
            }
        }
        blUtils.setPrintOptionsList(lclBl.getLclFileNumber().getId(), request);
        List<LclBlPiece> lclCommodityList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBl.getFileNumberId());
        request.setAttribute("lclCommodityList", lclCommodityList);
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            request.setAttribute("highVolumeDis", lclCommodityList.get(0).getCommodityType().isHighVolumeDiscount());
        }
        request.setAttribute("checkBlInsurance", new LclBlAcDAO().checkBlInsurance(fileId, INSURANCE_CHARGE_CODE));
        request.setAttribute("aesDetailsList", new SedFilingsDAO().getSedFilingsList(lclBl.getLclFileNumber().getFileNumber()));
        blUtils.setPolAndPod(lclBl, request);
        if (!lclBl.getBillingType().equals("P")) {
            request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking());   // for Freight Agent Acct
        }
        request.setAttribute("lclBl", lclBl);
        request.setAttribute("bkgPooUnloc", lclBl.getLclFileNumber().getLclBooking().getPortOfOrigin().getUnLocationCode());
        if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBl.getLclFileNumber().getLclBooking().getBookingType())) {
            lclBlForm.getlclBl().setBookingType("T");
        }
        request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(lclBl.getFileNumberId()));
        blUtils.setWeightMeasureDetails(lclBl, lclBlForm, request);
        lclBlForm.setBillPPD(lclBl.getBillToParty());
        lclBlForm = blUtils.getPickedVoyageDetailsForBL(lclBlForm, fileNoId);
        lclUtils.setCorrection(request, lclBl.getFileNumberId());
        String destinationCode = null != lclBl.getFinalDestination() ? lclBl.getFinalDestination().getUnLocationCode()
                : null != lclBl.getPortOfDestination() ? lclBl.getPortOfDestination().getUnLocationCode() : "";
        String engmet = new PortsDAO().getPortValue(PORTS_ENGMET, destinationCode);
        blUtils.setSportRateValues(lclBl, engmet, request);
        String company = LoadLogisoftProperties.getProperty(lclBl.getLclFileNumber().getBusinessUnit().equalsIgnoreCase("ECU") ? "application.ECU.companyname"
                : lclBl.getLclFileNumber().getBusinessUnit().equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.Econo.companyname");
        request.setAttribute("brandName", company);
        request.setAttribute("ffcommchargecode", CommonConstants.FFCOMM_CHARGECODE);
        request.setAttribute("pbachargecode", CommonConstants.PBA_CHARGECODE);
        request.setAttribute("lCLBlForm", lclBlForm);
        return mapping.findForward("editBooking");
    }

    public ActionForward backToMainScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("mainScreen");
    }

    public ActionForward saveBl(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm lclBlForm = (LCLBlForm) form;
        lclBlForm.setBlNumber("");
        HttpSession session = request.getSession();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LCLBlDAO lclBlDAO = new LCLBlDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        lclBlForm.getlclBl().setModifiedBy(loginUser);
        lclBlForm.getlclBl().setModifiedDatetime(now);
        if (lclBlForm.getlclBl().getEnteredBy() == null) {
            lclBlForm.getlclBl().setEnteredBy(loginUser);
            lclBlForm.getlclBl().setEnteredDatetime(now);
        }
        LclFileNumber lclFileNumber = lclBlForm.getlclBl().getLclFileNumber();
        if (lclFileNumber == null) {
            LclFileNumberThread thread = new LclFileNumberThread();
            String fileNumber = thread.getFileNumber();
            Long newFileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
            if (newFileNumberId == 0) {
                lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "BL");
            } else {
                thread = new LclFileNumberThread();
                fileNumber = thread.getFileNumber();
                lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "BL");
            }
            if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                lclFileNumberDAO.getSession().getTransaction().begin();
            }
        }
        //hard code for enum
        lclBlForm.setEnums();
        lclBlForm.getlclBl().setLclFileNumber(lclFileNumber);
        lclBlForm.getlclBl().setFileNumberId(lclFileNumber.getId());

        request.setAttribute("contact", lclBlForm.getlclBl().getAgentContact());
        lclBlForm.getlclBl().setAgentContact(null != lclBlForm.getlclBl().getAgentAcct() ? lclBlForm.getlclBl().getAgentContact() : null);

        BlUtils util = new BlUtils();
        String fwd_contact_name = "on".equalsIgnoreCase(lclBlForm.getEdiForwarderCheck()) ? lclBlForm.getEditFwdAcctName() : "";
        lclBlForm.getlclBl().setFwdContact(util.getBLContact(lclFileNumber, lclBlForm.getlclBl().getFwdContact(),
                loginUser, fwd_contact_name, "blForwarder"));

        String ship_contact_name = "on".equalsIgnoreCase(lclBlForm.getNewShipper()) ? lclBlForm.getShipContactName()
                : "on".equalsIgnoreCase(lclBlForm.getEdiShipperCheck()) ? lclBlForm.getEditShipAcctName() : "";
        lclBlForm.getlclBl().setShipContact(util.getBLContact(lclFileNumber, lclBlForm.getlclBl().getShipContact(),
                loginUser, ship_contact_name, "blShipper"));

        String cons_contact_name = "on".equalsIgnoreCase(lclBlForm.getNewConsignee()) ? lclBlForm.getConsContactName()
                : "on".equalsIgnoreCase(lclBlForm.getEdiConsigneeCheck()) ? lclBlForm.getEditConsAcctName() : "";
        lclBlForm.getlclBl().setConsContact(util.getBLContact(lclFileNumber, lclBlForm.getlclBl().getConsContact(),
                loginUser, cons_contact_name, "blConsignee"));

        String noty_contact_name = "on".equalsIgnoreCase(lclBlForm.getNewNotify()) ? lclBlForm.getNotifyContactName()
                : "on".equalsIgnoreCase(lclBlForm.getEdiNotifyCheck()) ? lclBlForm.getEditNotyAcctName() : "";
        lclBlForm.getlclBl().setNotyContact(util.getBLContact(lclFileNumber, lclBlForm.getlclBl().getNotyContact(),
                loginUser, noty_contact_name, "blNotify"));

        lclBlForm.getlclBl().setThirdPartyContact(util.getBLContact(lclFileNumber, lclBlForm.getlclBl().getThirdPartyContact(),
                loginUser, "", "blThirdParty"));

        lclBlForm.getlclBl().setSupContact(util.getBLContact(lclFileNumber, lclBlForm.getlclBl().getSupContact(),
                loginUser, "", "blSupplier"));
        lclBlForm.getlclBl().setDocsBl(lclBlForm.isDocsBl());
        lclBlForm.getlclBl().setDocsCaricom(lclBlForm.isDocsCaricom());
        lclBlForm.getlclBl().setDocsAes(lclBlForm.isDocsAes());
        lclBlForm.getlclBl().setRatesFromTerminalNo(CommonUtils.isNotEmpty(lclBlForm.getRatesFromTerminalNo())
                ? lclBlForm.getRatesFromTerminalNo() : null);
        if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclFileNumber.getLclBooking().getBookingType())) {
            lclBlForm.getlclBl().setBookingType("T");
        }
        lclRemarksDAO.updateRemarksByField(lclFileNumber, REMARKS_TYPE_EXPORT_REF, REMARKS_TYPE_EXPORT_REF,
                lclBlForm.geteReference(), loginUser, REMARKS_BL_AUTO_NOTES);

        lclRemarksDAO.updateRemarksByField(lclFileNumber, REMARKS_TYPE_ROUTING_INSTRU, REMARKS_TYPE_ROUTING_INSTRU,
                lclBlForm.getRoutingInstruction(), loginUser, REMARKS_BL_AUTO_NOTES);

        if (lclBlForm.getlclBl().getBillingType().equalsIgnoreCase("C")) {
            lclBlForm.setBillPPD("A");
        }
        //adding and updating the bl_Owner in lcl_bl
        if (!"".equalsIgnoreCase(lclBlForm.getBlOwnerName())) {
            User blOwner = new UserDAO().findById(null != lclBlForm.getBlOwnerId() ? lclBlForm.getBlOwnerId() : 0);
            lclBlForm.getlclBl().setBlOwner(blOwner);
        }
        lclBlForm.getlclBl().setInvoiceValue(CommonUtils.isNotEmpty(lclBlForm.getInvoiceValue())
                ? new BigDecimal(lclBlForm.getInvoiceValue()) : BigDecimal.ZERO);
        lclBlDAO.getCurrentSession().clear();
        lclBlForm.getlclBl().setRateType(lclBlForm.getEditRateType());
        lclBlForm.getlclBl().setDeliveryMetro(lclBlForm.getDeliveryMetro());
        lclBlDAO.saveOrUpdate(lclBlForm.getlclBl());
        lclSession.setCommodityList(null);
        session.setAttribute("lclSession", lclSession);
        List<LclBlPiece> lclCommodityList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId());
        if (lclCommodityList != null && !lclCommodityList.isEmpty()
                && lclCommodityList.get(0).getCommodityType() != null) {
            request.setAttribute("lclCommodityList", lclCommodityList);
            request.setAttribute("highVolumeDis", lclCommodityList.get(0).getCommodityType().isHighVolumeDiscount());
        }
        LclBl lclBl = new LCLBlDAO().getByProperty("lclFileNumber.id", lclFileNumber.getId());
        new LCLBlDAO().getCurrentSession().evict(lclBl);
        blUtils.setPolAndPod(lclBl, request);
        blUtils.setCreditStauAndLimit(lclBl, request, loginUser);
        blUtils.setPrintOptionsList(lclBl.getFileNumberId(), request);
        request.setAttribute("checkBlInsurance", new LclBlAcDAO().checkBlInsurance(lclBl.getFileNumberId(), INSURANCE_CHARGE_CODE));
        request.setAttribute("bkgPooUnloc", lclBl.getLclFileNumber().getLclBooking().getPortOfOrigin().getUnLocationCode());
        request.setAttribute("consolidatedDocuments", new LclConsolidateDAO().getConsolidatesFileNumbers(lclBl.getFileNumberId().toString()));
        if (!lclBl.getBillingType().equals("P")) {
            request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking()); //for Freight Agent Acct
        }
        request.setAttribute("lclBl", lclBl);
        request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(lclBl.getFileNumberId()));
        lclBlForm = blUtils.getPickedVoyageDetailsForBL(lclBlForm, fileNumberId);
        blUtils.setWeightMeasureDetails(lclBl, lclBlForm, request);
        lclUtils.setCorrection(request, lclFileNumber.getId());
//        LclRemarks manifestRemarks = lclRemarksDAO.executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclFileNumber.getId() + " AND type='auto' AND (remarks=' BL Manifested')");
//        if (manifestRemarks == null && lclBl.getLclFileNumber().getStatus().equalsIgnoreCase("M")) {
//            lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), "auto", " BL Manifested", loginUser.getUserId());
//        }
        String destinationCode = null != lclBl.getFinalDestination() ? lclBl.getFinalDestination().getUnLocationCode()
                : null != lclBl.getPortOfDestination() ? lclBl.getPortOfDestination().getUnLocationCode() : "";
        String engmet = new PortsDAO().getPortValue(PORTS_ENGMET, destinationCode);
        util.setSportRateValues(lclBl, engmet, request);
        String company = LoadLogisoftProperties.getProperty(lclBl.getLclFileNumber().getBusinessUnit().equalsIgnoreCase("ECU") ? "application.ECU.companyname"
                : lclBl.getLclFileNumber().getBusinessUnit().equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.Econo.companyname");
        request.setAttribute("brandName", company);
        request.setAttribute("ffcommchargecode", CommonConstants.FFCOMM_CHARGECODE);
        request.setAttribute("pbachargecode", CommonConstants.PBA_CHARGECODE);
        return mapping.findForward(NEW_BOOKING);
    }

    public void setUserAndDateTime(LclContact lclContact, HttpServletRequest request) {
        if (lclContact.getEnteredBy() == null || lclContact.getEnteredDatetime() == null) {
            lclContact.setEnteredBy(getCurrentUser(request));
            lclContact.setEnteredDatetime(new Date());
        }
        lclContact.setModifiedBy(getCurrentUser(request));
        lclContact.setModifiedDatetime(new Date());
    }

    public List<Lcl3pRefNo> addOtiNumber(LCLBlForm lclBlForm) {
        List<Lcl3pRefNo> list = new ArrayList<Lcl3pRefNo>();
        if (CommonUtils.isNotEmpty(lclBlForm.getOtiNumber())) {
            Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNo();
            lcl3pRefNo.setReference(lclBlForm.getOtiNumber());
            lcl3pRefNo.setType("OTI");
            list.add(lcl3pRefNo);
        }
        return list;
    }

    public LCLBlForm setOtiNumber(List<Lcl3pRefNo> list, LCLBlForm lclBlForm) {
        for (Lcl3pRefNo lcl3pRefNo : list) {
            if (lcl3pRefNo.getType().equals("OTI")) {
                lclBlForm.setOtiNumber(lcl3pRefNo.getReference());
            }
        }
        return lclBlForm;

    }

    public ActionForward calculateCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm lclBlForm = (LCLBlForm) form;
        List lclBlPiecesList = null;
        LclBl lclBl = null;
        if (CommonUtils.isNotEmpty(lclBlForm.getFileNumberId())) {
            lclBl = new LCLBlDAO().getByProperty("lclFileNumber.id", lclBlForm.getFileNumberId());
            lclBl.setDeliveryMetro(lclBlForm.getDeliveryMetro());
            lclBl.setFinalDestination(new UnLocationDAO().findById(lclBlForm.getFinalDestinationId()));
            boolean rateTypeFlag = !lclBlForm.getRateType().equalsIgnoreCase(lclBl.getRateType()) ? Boolean.TRUE : Boolean.FALSE;
            //lclBl.setFinalDestination();
            LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            User loginUser = getCurrentUser(request);
            lclRemarksDAO.insertLclRemarks(lclBlForm.getFileNumberId(), REMARKS_BL_AUTO_NOTES, "User Chooses to Calculate Charges", loginUser.getUserId());
            lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBlForm.getFileNumberId());
            LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
            if (CommonUtils.isNotEmpty(lclBlForm.getOrigin()) && CommonUtils.isNotEmpty(lclBlForm.getDestination())
                    && lclBlPiecesList != null && !lclBlPiecesList.isEmpty() && lclBlForm.getRateType() != null && !lclBlForm.getRateType().trim().equals("")) {
                String rateType = "R".equalsIgnoreCase(lclBlForm.getRateType()) ? "Y" : lclBlForm.getRateType();
                // Calculating imports rates.
                List<LclBlAc> blAcList = blUtils.getTransshipmentRaterForBl(lclBl, loginUser, request);
                if (CommonUtils.isEmpty(blAcList)) {
                    lclBlChargesCalculation.calculateRates(lclBlForm.getOrigin(), lclBlForm.getDestination(), lclBlForm.getPol(),
                            lclBlForm.getPod(), lclBlForm.getFileNumberId(), lclBlPiecesList, loginUser, "", lclBlForm.getInsurance(),
                            lclBl.getValueOfGoods(), rateType, "C", null, null, null, null, null, lclBlForm.getDeliveryMetro(), lclBlForm.getPcBoth(), null,
                            lclBl.getBillToParty(), request, false, rateTypeFlag);
                }
                request.setAttribute("ofratebasis", lclBlChargesCalculation.getOfratebasis());
                request.setAttribute("stdchgratebasis", lclBlChargesCalculation.getStdchgratebasis());
            }
            List<LclBlAc> chargeList = lclBlAcDAO.getLclCostByFileNumberAsc(lclBlForm.getFileNumberId());
            String engmet = new PortsDAO().getPortValue(PORTS_ENGMET, lclBlForm.getDestination());
            blUtils.setWeighMeasureForBl(request, lclBlPiecesList, engmet);
            blUtils.setRolledUpChargesForBl(chargeList, request, lclBlForm.getFileNumberId(), lclBlAcDAO, lclBlPiecesList,
                    lclBlForm.getlclBl().getBillingType(),
                    engmet, lclBl);
        }
        request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(lclBl.getFileNumberId()));
        if (!lclBl.getBillingType().equals("P")) {
            request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking());
        }
        request.setAttribute("lclBl", lclBl);
        request.setAttribute("ffcommchargecode", CommonConstants.FFCOMM_CHARGECODE);
        request.setAttribute("pbachargecode", CommonConstants.PBA_CHARGECODE);
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward displayAES(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm lclBlForm = (LCLBlForm) form;
        String fileId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileId)) {
            List<Long> conoslidatelist = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(fileId));
            conoslidatelist.add(Long.parseLong(fileId));
            request.setAttribute("lcl3PList", new Lcl3pRefNoDAO().get3pRefAesList(conoslidatelist));
        }
        request.setAttribute("lclBlForm", lclBlForm);
        return mapping.findForward("aes");
    }

    public ActionForward updateBillToCode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm lclBlForm = (LCLBlForm) form;
        LCLBlDAO lCLBlDAO = new LCLBlDAO();
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        User loginUser = getCurrentUser(request);
        String billToParty = request.getParameter("billPPD");
        String exitbillToParty = request.getParameter("whsePhone");
        String billingType = request.getParameter("pcBoth");
        lCLBlDAO.updateBillToParty(lclBlForm.getFileNumberId(), billingType, billToParty, "", "", loginUser.getUserId());
        LclBl lclBl = lCLBlDAO.getByProperty("lclFileNumber.id", lclBlForm.getFileNumberId());
        String notes = "Bill to Code -> " + exitbillToParty + " to " + billToParty;//notes
        lclRemarksDAO.insertLclRemarks(lclBlForm.getFileNumberId(), REMARKS_BL_AUTO_NOTES, notes, loginUser.getUserId());
        lclBlAcDAO.updateBillToPartyChargesME(lclBlForm.getFileNumberId(), billToParty, loginUser.getUserId(), "");
        List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBlForm.getFileNumberId());
        String[] cafData = new LclBlChargesCalculation().getCAFCalculationContent(lclBl);
        Ports ports = new PortsDAO().getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
        if (lclBlPiecesList != null && lclBlPiecesList.size() > 0) {
            if (billingType.equals("C")) {
                boolean isCAFChargeContain = new LclDwr().checkLclCollectCharges(lclBl.getPortOfDestination().getUnLocationCode());
                if (isCAFChargeContain) {
                    new LclBlChargesCalculation().calculateCAFCharge(cafData[0], cafData[1], cafData[2],
                            cafData[3], lclBlPiecesList, billingType, loginUser, lclBlForm.getFileNumberId(), request, ports, billToParty);
                }
            } else if (billingType.equals("P") || billingType.equals("B")) {
                LclRemarksDAO remarksDAO = new LclRemarksDAO();
                LclBlAc lclBlAc = lclBlAcDAO.findByChargeCode(lclBlForm.getFileNumberId(), false, "LCLE", "CAF");
                if (lclBlAc != null) {
                    String desc = "DELETED -> Charge Code -> " + lclBlAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBlAc.getArAmount();
                    lclBlAcDAO.delete(lclBlAc);
                    remarksDAO.insertLclRemarks(lclBlForm.getFileNumberId(), REMARKS_BL_AUTO_NOTES, desc, loginUser.getUserId());
                }
            }
        }
        blUtils.setWeightMeasureDetails(lclBl, lclBlForm, request);
        List<LclBlAc> chargeList = lclBlAcDAO.getLclCostByFileNumberAsc(lclBlForm.getFileNumberId());
        blUtils.setRolledUpChargesForBl(chargeList, request, lclBlForm.getFileNumberId(), lclBlAcDAO, lclBlPiecesList,
                lclBlForm.getlclBl().getBillingType(), ports.getEngmet(), lclBl);
        if (!lclBl.getBillingType().equals("P")) {
            request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
        }
        request.setAttribute("lclBl", lclBl);
        return mapping.findForward("chargeDesc");
    }

    public ActionForward revertToBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm blForm = (LCLBlForm) form;
        ActionForward redirectAction = null;
        LCLBlDAO blDao = new LCLBlDAO();
        LclBLPieceDAO pieceDAO = new LclBLPieceDAO();
        LclOptionsDAO optionsDao = new LclOptionsDAO();
        LclBlAcDAO acDao = new LclBlAcDAO();
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        if (CommonUtils.isNotEmpty(blForm.getFileNumberId())) {
            optionsDao.deleteOption(blForm.getFileNumberId());
            List<LclBlPiece> blPieceList = pieceDAO.findByProperty("lclFileNumber.id", blForm.getFileNumberId());
            if (CommonUtils.isNotEmpty(blPieceList)) {
                for (LclBlPiece blPiece : blPieceList) {
                    new LclBlPieceDetailDAO().deleteByPiece(blPiece.getId());
                    pieceDAO.delete(blPiece);
                }
            }
            fileDao.updateFileNumberState(String.valueOf(blForm.getFileNumberId()), "B");
            acDao.deleteBlAcByFileNumber(blForm.getFileNumberId());
            fileDao.updateConsolidateBl(blForm.getFileNumberId(), "B");
            LclBl lclBl = blDao.findById(blForm.getFileNumberId());
            if (null != lclBl) {
                blDao.delete(lclBl);
                List<String> contactTypes = Arrays.asList("blForwarder", "blShipper", "blConsignee", "blNotify", "blThirdParty", "blSupplier", "blAgent", "blClient");
                new LCLContactDAO().deleteByFileNumberIdAndContactType(blForm.getFileNumberId(), contactTypes);
            }
            if (CommonUtils.isNotEmpty(blForm.geteReference())) {
                lclRemarksDAO.deleteRemarks(blForm.getFileNumberId(), REMARKS_TYPE_EXPORT_REF);
            }
            if (CommonUtils.isNotEmpty(blForm.getRoutingInstruction())) {
                lclRemarksDAO.deleteRemarks(blForm.getFileNumberId(), REMARKS_TYPE_ROUTING_INSTRU);
            }
            lclRemarksDAO.insertLclRemarks(blForm.getFileNumberId(), REMARKS_TYPE_AUTO, "B/L reversed back to Booking", getCurrentUser(request).getUserId());
        }
        return redirectAction;
    }

    public ActionForward upcomingSailings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // LCLBlForm blForm = (LCLBlForm) form;
        // LclBl lclBl = new LCLBlDAO().findById(blForm.getFileNumberId());
        String poo = request.getParameter("poo");
        String pol = request.getParameter("pol");
        String pod = request.getParameter("pod");
        String fd = request.getParameter("fd");
        String sailingsId = request.getParameter("sailings");
        String relayOverride = request.getParameter("relayOverride");
        String cfcl = request.getParameter("cfcl").equals("true") ? "C" : "E";
        if (fd == null || "".equalsIgnoreCase(fd)) {
            fd = pod;
        }
        List<LclBookingVoyageBean> upcomingSailings = null;
        LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
        if ("true".equalsIgnoreCase(relayOverride)) {
            LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelayOverride(Integer.parseInt(poo),
                    Integer.parseInt(pol), Integer.parseInt(pod), Integer.parseInt(fd), 0);
            if (bookingPlanBean != null) {
                upcomingSailings = bookingPlanDAO.getUpComingSailingsScheduleByBl(Integer.parseInt(pol),
                        Integer.parseInt(pol), Integer.parseInt(pod), Integer.parseInt(pod), "V", bookingPlanBean, cfcl);
            }
        } else {
            LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(Integer.parseInt(poo),
                    Integer.parseInt(fd), "N");
            if (bookingPlanBean != null) {
                upcomingSailings = bookingPlanDAO.getUpComingSailingsScheduleByBl(Integer.parseInt(poo),
                        bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(), Integer.parseInt(fd), "V", bookingPlanBean, cfcl);
            }
        }
        request.setAttribute("voyageList", upcomingSailings);
        request.setAttribute("sailingsId", sailingsId);
        return mapping.findForward("upcomingSailings");
    }

    public ActionForward updateSailings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileId");
        String masterSchdId = request.getParameter("sailingsId");
        new LCLBlDAO().updateEciSailings(fileId, masterSchdId);
        PrintWriter out = response.getWriter();
        out.print(true);
        out.flush();
        out.close();
        return null;
    }

    public ActionForward clientSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchByValue", request.getParameter("searchByValue"));
        return mapping.findForward("clientSearch");
    }

    public void setRemarks(LCLBlForm lclBlForm, Long fileId, String[] remarkTypes) throws Exception {
        List remarks = lclRemarksDAO.getRemarksByTypes(fileId, remarkTypes);
        for (Object row : remarks) {
            Object[] col = (Object[]) row;
            if (col[1].toString().equalsIgnoreCase("Export Reference")) {
                lclBlForm.seteReference(col[0].toString());
            }
            if (col[1].toString().equalsIgnoreCase("Routing Instruction")) {
                lclBlForm.setRoutingInstruction(col[0].toString());
            }
        }
    }

    public ActionForward updateVoidInBl(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward redirectAction = null;
        User user = getCurrentUser(request);
        LCLBlForm blForm = (LCLBlForm) form;
        if (CommonUtils.isNotEmpty(blForm.getFileNumberId())) {
            Date today = new Date();
            LCLBlDAO blDao = new LCLBlDAO();
            LclBLPieceDAO pieceDAO = new LclBLPieceDAO();
            LclBlAcDAO acDao = new LclBlAcDAO();
            LclFileNumberDAO fileDao = new LclFileNumberDAO();
            LclBl bl = blDao.findById(blForm.getFileNumberId());
            blDao.getCurrentSession().evict(bl);
            String realPath = request.getSession().getServletContext().getRealPath("/");
            String voidComments = request.getParameter("voidComments");
            if (CommonUtils.isNotEmpty(voidComments)) {
                lclRemarksDAO.insertLclRemarks(blForm.getFileNumberId(), REMARKS_DR_AUTO_NOTES, voidComments.toUpperCase(), user.getUserId());
            }

            if (blForm.isBlUnitCob()) {
                LclCostChargeDAO costChargeDAO = new LclCostChargeDAO();
                LclManifestDAO lclManifestDAO = new LclManifestDAO();
                LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
                LclBookingAc lclBookingAc = costChargeDAO.getCost(blForm.getFileNumberId(), "FFCOMM", true);
                if (lclBookingAc != null && NumberUtils.isNotZero(lclBookingAc.getApAmount())) {
                    String costStatus = lclBookingAcTransDAO.getTransType(lclBookingAc.getId());
                    if ("AC".equalsIgnoreCase(costStatus)) {
                        String notes1 = "DELETED -> Code -> " + lclBookingAc.getApglMapping().getChargeCode() + " Cost Amount -> " + lclBookingAc.getApAmount();
                        String lclBookingAcTransIds = lclBookingAcTransDAO.getConcatenatedBookingAcTransIds(lclBookingAc.getId().toString());
                        lclBookingAc.setSupAcct(null);
                        lclBookingAc.setApBillToParty(null);
                        lclBookingAc.setApAmount(BigDecimal.ZERO);
                        lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                        lclBookingAc.setDeleted(Boolean.TRUE);
                        costChargeDAO.saveOrUpdate(lclBookingAc);
                        lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                        costChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                        lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
                        lclRemarksDAO.insertLclRemarks(blForm.getFileNumberId(), REMARKS_DR_AUTO_NOTES, notes1, user.getUserId());
                    }
                }
                new LclExportManifestDAO().manifest(null, user, false, blForm.getFileNumberId());
            }

            if (bl.getPostedDate() != null) {
                StringBuilder path = new StringBuilder();
                String companyName = new SystemRulesDAO().getSystemRulesByCode("CompanyName");
                path.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
                path.append(companyName).append("/");
                path.append("LCL BL").append("/").append("BLVOID").append("/");
                path.append(DateUtils.formatDate(today, "yyyy/MM/dd")).append("/");
                File dir = new File(path.toString());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String blReportLocation = "BillOfLading_" + bl.getLclFileNumber().getFileNumber()
                        + DateUtils.formatDate(today, "_yyyyMMdd_HHmmss") + ".pdf";
                String reportLocation = path.toString() + blReportLocation;
                new LclBLPdfCreator().createReportJob(realPath, reportLocation, "Bill Of Lading",
                        bl.getLclFileNumber().getId().toString(), true);
                DocumentStoreLog document = new DocumentStoreLog();
                document.setScreenName("LCL EXPORTS DR");
                document.setDocumentName("BL VOID");
                document.setDocumentID(bl.getLclFileNumber().getFileNumber());
                document.setOperation(ConstantsInterface.PAGE_ACTION_SCAN);
                document.setDateOprDone(today);
                document.setFileLocation(path.toString());
                document.setFileName(blReportLocation);
                new DocumentStoreLogDAO().save(document);
            }

            List<LclBlPiece> blPieceList = pieceDAO.findByProperty("lclFileNumber.id", blForm.getFileNumberId());
            if (CommonUtils.isNotEmpty(blPieceList)) {
                for (LclBlPiece blPiece : blPieceList) {
                    new LclBlPieceDetailDAO().deleteByPieceDetails(blPiece.getId());
                }
            }
            pieceDAO.deleteBlPieceByFileNumber(blForm.getFileNumberId());
            acDao.deleteBlAcByFileNumber(blForm.getFileNumberId());
            new LclOptionsDAO().deleteOption(blForm.getFileNumberId());
            if (null != bl) {
                blDao.delete(bl);
            }
            if (CommonUtils.isNotEmpty(blForm.geteReference())) {
                lclRemarksDAO.deleteRemarks(blForm.getFileNumberId(), REMARKS_TYPE_EXPORT_REF);
            }
            if (CommonUtils.isNotEmpty(blForm.getRoutingInstruction())) {
                lclRemarksDAO.deleteRemarks(blForm.getFileNumberId(), REMARKS_TYPE_ROUTING_INSTRU);
            }
            /* Mantis#12857: DR/BL – Voiding a BL is unpicking it from the unit  */
//            if (CommonUtils.isNotEmpty(blForm.getUnitSsId())) {
//                new LclBookingPieceUnitDAO().deletePickedBookedPieceUnit(blForm.getFileNumberId());
//                String remarks = "Un Picked from Unit# " + blForm.getUnitNumber() + " on Voyage# " + blForm.getVoyageNumber();
//                lclRemarksDAO.insertLclRemarks(blForm.getFileNumberId(), REMARKS_TYPE_AUTO, remarks, user.getUserId());
//            }
//
//            fileDao.updateConsolidateBl(blForm.getFileNumberId(), "B");
//            String dispoCode[] = new String[]{"RUNV", "RCVD"};
//            List<String> dispoCodeList = Arrays.asList(dispoCode);
//            Boolean isDispoFlag = new LclBookingDispoDAO().isCheckedDispoCode(blForm.getFileNumberId(), dispoCodeList);
//            LclBookingDispoDAO bookingDispoDAO = new LclBookingDispoDAO();
//            String currentDispo = bookingDispoDAO.getDispositionCode(bl.getFileNumberId());
//            if (isDispoFlag && !"RCVD".equalsIgnoreCase(currentDispo) && !"RUNV".equalsIgnoreCase(currentDispo)) {
//                Disposition disposition = new DispositionDAO().getByProperty("eliteCode", "RCVD");
//                bookingDispoDAO.insertBookingDispoForRCVD(bl.getFileNumberId(), disposition.getId(), null,
//                        null, user.getUserId(), bl.getLclFileNumber().getLclBooking().getPortOfOrigin().getId(), null);
//            }
//            fileDao.updateFileStatus(blForm.getFileNumberId(), "B", isDispoFlag ? "W" : "B");
            lclRemarksDAO.insertLclRemarks(blForm.getFileNumberId(), REMARKS_TYPE_AUTO, "BL is voided", user.getUserId());
            if (blForm.isBlUnitCob()) {
                fileDao.updateFileStatus(blForm.getFileNumberId(), "B", "L");
            } else {
                fileDao.updateFileStatus(blForm.getFileNumberId(), "B", "");
            }
        }
        return redirectAction;
    }

    public ActionForward displaySpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm blForm = (LCLBlForm) form;
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        LclBl lclBl = new LCLBlDAO().findById(Long.parseLong(request.getParameter("fileNumberId")));
        new LCLBlDAO().getCurrentSession().evict(lclBl);
        String destinationCode = null != lclBl && null != lclBl.getFinalDestination() ? lclBl.getFinalDestination().getUnLocationCode()
                : null != lclBl.getPortOfDestination() ? lclBl.getPortOfDestination().getUnLocationCode() : "";
        if (CommonUtils.isNotEmpty(destinationCode)) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", destinationCode);
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                request.setAttribute("engmet", ports.getEngmet());
            }
            request.setAttribute("destination", destinationCode);
        }
        LclBlCostAndChargeForm chargeForm = new LclBlCostAndChargeForm();
        chargeForm.setRate(null != lclBl.getSpotWmRate() ? lclBl.getSpotWmRate().toString() : "");
        chargeForm.setRateN(null != lclBl.getSpotRateMeasure() ? lclBl.getSpotRateMeasure().toString() : "");
        chargeForm.setCheckWM(lclBl.getSpotRateUom());
        chargeForm.setSpotCheckBottom(lclBl.isSpotRateBottom());
        chargeForm.setSpotCheckOF(lclBl.isSpotOfRate());
        chargeForm.setComment(lclBl.getSpotComment());
        request.setAttribute("lclBlCostAndChargeForm", chargeForm);
        request.setAttribute("comment", lclBl.getSpotComment());
        return mapping.findForward("lclSpotRate");
    }

    public ActionForward recaculateSpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLBlForm blForm = (LCLBlForm) form;
            if (CommonUtils.isNotEmpty(blForm.getFileNumberId())) {
                out = response.getWriter();
                response.setContentType("application/json");
                Map<String, String> result = new HashMap<String, String>();
                String spotRate = request.getParameter("spotRate");
                boolean isSpotRate = "Y".equalsIgnoreCase(spotRate) ? true : false;
                User loginUser = getCurrentUser(request);
                LCLBlDAO blDAO = new LCLBlDAO();
                LclBlAcDAO blAcDAO = new LclBlAcDAO();
                LclBl bl = blDAO.getByProperty("lclFileNumber.id", blForm.getFileNumberId());
                blDAO.getCurrentSession().evict(bl);
                bl.setSpotRateUom("M");
                bl.setSpotComment(null);
                bl.setSpotRateBottom(false);
                bl.setSpotRate(isSpotRate);
                bl.setSpotOfRate(false);
                bl.setSpotWmRate(null);
                bl.setSpotRateMeasure(null);
                bl.setModifiedBy(loginUser);
                bl.setModifiedDatetime(new Date());
                blDAO.saveOrUpdate(bl);
                blDAO.getCurrentSession().flush();
                blDAO.getCurrentSession().clear();
                List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", blForm.getFileNumberId());
                if (isSpotRate) {
                    MessageResources messageResources = getResources(request);
                    String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                    LclBlPiece commodity = lclBlPiecesList.isEmpty() ? new LclBlPiece() : lclBlPiecesList.get(0);
                    commodity.setCommodityType(new commodityTypeDAO().getByProperty("code", spotRateCommodity));
                    new LclBLPieceDAO().update(commodity);
                }
                LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
                if (CommonUtils.isNotEmpty(blForm.getOrigin()) && CommonUtils.isNotEmpty(blForm.getDestination())
                        && lclBlPiecesList != null && !lclBlPiecesList.isEmpty() && blForm.getRateType() != null && !blForm.getRateType().trim().equals("")) {
                    String rateType = "R".equalsIgnoreCase(blForm.getRateType()) ? "Y" : blForm.getRateType();
                    lclBlChargesCalculation.calculateRates(blForm.getOrigin(), blForm.getDestination(), blForm.getPol(),
                            blForm.getPod(), blForm.getFileNumberId(), lclBlPiecesList, loginUser, "", blForm.getInsurance(), blForm.getlclBl().getValueOfGoods(), rateType,
                            "C", null, null, null, null, null, blForm.getDeliveryMetro(), blForm.getPcBoth(), null, blForm.getBillForm(), request, false, false);
                    request.setAttribute("ofratebasis", lclBlChargesCalculation.getOfratebasis());
                    request.setAttribute("stdchgratebasis", lclBlChargesCalculation.getStdchgratebasis());
                }
                List<LclBlAc> chargeList = blAcDAO.getLclCostByFileNumberAsc(blForm.getFileNumberId());
                String engmet = new PortsDAO().getPortValue(PORTS_ENGMET, blForm.getDestination());
                blUtils.setWeighMeasureForBl(request, lclBlPiecesList, engmet);
                blUtils.setRolledUpChargesForBl(chargeList, request, blForm.getFileNumberId(), blAcDAO, lclBlPiecesList,
                        blForm.getlclBl().getBillingType(),
                        engmet, bl);
                request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(bl.getFileNumberId()));
                if (!bl.getBillingType().equals("P")) {
                    request.setAttribute("lclBooking", bl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
                }
                request.setAttribute("lclBl", bl);
                request.setAttribute("ffcommchargecode", CommonConstants.FFCOMM_CHARGECODE);
                request.setAttribute("pbachargecode", CommonConstants.PBA_CHARGECODE);
                JspWrapper jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/chargeBlDesc.jsp").include(request, jspWrapper);
                result.put("chargeDesc", jspWrapper.getOutput());

                List<LclBlPiece> resetBlPieceList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", blForm.getFileNumberId());
                request.setAttribute("lclCommodityList", resetBlPieceList);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityBlDesc.jsp").include(request, jspWrapper);
                result.put("commodityDesc", jspWrapper.getOutput());
                Gson gson = new Gson();
                out.print(gson.toJson(result));
            }
            return null;
        } catch (Exception e) {
            log.info("Error in LCL BL recaculateSpotRate method. " + new Date(), e);
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    public ActionForward deleteChargesByFreeBlYes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBlForm blForm = (LCLBlForm) form;
        User loginUser = getCurrentUser(request);
        LCLBlDAO blDAO = new LCLBlDAO();
        LclBlAcDAO blAcDAO = new LclBlAcDAO();
        LclBl bl = blDAO.getByProperty("lclFileNumber.id", blForm.getFileNumberId());
        blDAO.getCurrentSession().evict(bl);
        bl.setFreeBL(true);
        bl.setModifiedDatetime(new Date());
        bl.setModifiedBy(loginUser);
        blDAO.saveOrUpdate(bl);
        blDAO.getCurrentSession().flush();
        blDAO.getCurrentSession().clear();
        List<LclBlAc> chargeList = blAcDAO.getLclCostByFileNumberAsc(blForm.getFileNumberId());
        if (chargeList != null && !chargeList.isEmpty()) {
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            for (LclBlAc charge : chargeList) {
                String notesDesc = "DELETED -> " + charge.getArglMapping().getChargeCode() + " Charge Amount ->  " + charge.getArAmount();
                remarksDAO.insertLclRemarks(blForm.getFileNumberId(), REMARKS_BL_AUTO_NOTES, notesDesc, loginUser.getUserId());
                blAcDAO.delete(charge);
            }
        }
        new BlUtils().setWeightMeasureDetails(bl, blForm, request);
        request.setAttribute("chargeList", null);
        if (!bl.getBillingType().equals("P")) {
            request.setAttribute("lclBooking", bl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
        }
        request.setAttribute("lclBl", bl);
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward calculateChargesByFreeBlNo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLBlForm blForm = (LCLBlForm) form;
            User loginUser = getCurrentUser(request);
            LCLBlDAO blDAO = new LCLBlDAO();
            LclBlAcDAO blAcDAO = new LclBlAcDAO();
            LclBl bl = blDAO.getByProperty("lclFileNumber.id", blForm.getFileNumberId());
            blDAO.getCurrentSession().evict(bl);
            bl.setFreeBL(false);
            bl.setModifiedDatetime(new Date());
            bl.setModifiedBy(loginUser);
            blDAO.saveOrUpdate(bl);
            blDAO.getCurrentSession().flush();
            blDAO.getCurrentSession().clear();
            LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
            List<LclBlPiece> blPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", blForm.getFileNumberId());
            Boolean isConsolidate = lclConsolidateDAO.isConsolidatedByFileAB(blForm.getFileNumberId());
            if (isConsolidate) {
                List conoslidatelist = lclConsolidateDAO.getConsolidatesFiles(blForm.getFileNumberId());
                conoslidatelist.add(blForm.getFileNumberId());
                new ExportBookingUtils().updateConsolidateManualCharge(conoslidatelist, blForm.getFileNumberId(), bl.getBillToParty(), request);
            } else {
                if (CommonUtils.isNotEmpty(blForm.getOrigin()) && CommonUtils.isNotEmpty(blForm.getDestination())
                        && blForm.getRateType() != null && !blForm.getRateType().trim().equals("")) {
                    LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
                    String rateType = "R".equalsIgnoreCase(blForm.getRateType()) ? "Y" : blForm.getRateType();
                    lclBlChargesCalculation.calculateRates(blForm.getOrigin(), blForm.getDestination(), blForm.getPol(),
                            blForm.getPod(), blForm.getFileNumberId(), blPiecesList, loginUser, "", blForm.getInsurance(), new BigDecimal(0.00), rateType,
                            "C", null, null, null, null, null, blForm.getDeliveryMetro(), blForm.getPcBoth(), null, blForm.getBillForm(), request, false, false);
                    request.setAttribute("ofratebasis", lclBlChargesCalculation.getOfratebasis());
                    request.setAttribute("stdchgratebasis", lclBlChargesCalculation.getStdchgratebasis());
                    List<LclBookingAc> manualChargeList = new LclCostChargeDAO().getArAmount(bl.getFileNumberId(), true);
                    if (null != manualChargeList && !manualChargeList.isEmpty()) {
                        for (LclBookingAc charge : manualChargeList) {
                            LclBlAc lclBlAc = new LclBlAc();
                            PropertyUtils.copyProperties(lclBlAc, charge);
                            lclBlAc.setId(null);
                            lclBlAc.setApAmount(BigDecimal.ZERO);
                            lclBlAc.setArBillToParty(bl.getBillToParty());
                            lclBlAc.setApBillToParty(bl.getBillToParty());
                            new LclBlAcDAO().save(lclBlAc);
                        }
                    }

                    if (bl.getSpotRate() && blPiecesList.size() == 1) {
                        String billingType = bl.getBillingType();
                        String CFT = null != bl.getSpotWmRate() ? bl.getSpotWmRate().toString() : "";
                        String CBM = null != bl.getSpotRateMeasure() ? bl.getSpotRateMeasure().toString() : "";
                        Boolean spotCheckBottom = bl.isSpotRateBottom();
                        Boolean isOnlyOcnfrt = bl.isSpotOfRate();
                        String spotComment = bl.getSpotComment();
                        MessageResources messageResources = getResources(request);
                        String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                        blUtils.calculateSpotRate(blForm.getFileNumberId(), bl, billingType, CBM, CFT, rateType, isOnlyOcnfrt,
                                spotCheckBottom, spotComment, spotRateCommodity, request);
                    }
                }
            }
            List<LclBlAc> chargeList = blAcDAO.getLclCostByFileNumberAsc(blForm.getFileNumberId());
            String engmet = new PortsDAO().getPortValue(PORTS_ENGMET, blForm.getDestination());
            blUtils.setWeighMeasureForBl(request, blPiecesList, engmet);
            blUtils.setRolledUpChargesForBl(chargeList, request, blForm.getFileNumberId(), blAcDAO, blPiecesList,
                    blForm.getlclBl().getBillingType(),
                    engmet, bl);
            request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(bl.getFileNumberId()));
            if (!bl.getBillingType().equals("P")) {
                request.setAttribute("lclBooking", bl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
            }
            request.setAttribute("lclBl", bl);
            request.setAttribute("ffcommchargecode", CommonConstants.FFCOMM_CHARGECODE);
            request.setAttribute("pbachargecode", CommonConstants.PBA_CHARGECODE);
        } catch (Exception e) {
            log.info("Error in LCL BL calculateChargesByFreeBlNo method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward updateAgentFromBkg(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLBlForm blForm = (LCLBlForm) form;
            LclBl lclBl = new LCLBlDAO().findById(blForm.getFileNumberId());
            new LCLBlDAO().getCurrentSession().evict(lclBl);
            String agentNo = request.getParameter("agentAcctNo");
            String blAgent = null != lclBl.getAgentAcct() ? lclBl.getAgentAcct().getAccountno() : "";
//            if (!agentNo.equalsIgnoreCase(blAgent)) {
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            result.put("agentacctno", blAgent); //PickupAGENT
            CustAddressDAO custaddress = new CustAddressDAO();
            CustAddress custAddress = custaddress.findPrimeContact(blAgent);
            if (null != custAddress && !agentNo.equalsIgnoreCase(blAgent)) {
                StringBuilder agentAddress = new StringBuilder();
                if (CommonUtils.isNotEmpty(custAddress.getCoName())) {
                    agentAddress.append(custAddress.getCoName()).append(",").append("\n");
                }
                if (CommonUtils.isNotEmpty(custAddress.getAddress1())) {
                    agentAddress.append(custAddress.getAddress1()).append(",").append("\n");
                }
                if (CommonUtils.isNotEmpty(custAddress.getCity1())) {
                    agentAddress.append(custAddress.getCity1()).append(",");
                }
                if (CommonUtils.isNotEmpty(custAddress.getCity1())) {
                    agentAddress.append(custAddress.getCity1()).append(",");
                }
                if (CommonUtils.isNotEmpty(custAddress.getState())) {
                    agentAddress.append(custAddress.getState()).append(",");
                }
                if (CommonUtils.isNotEmpty(custAddress.getZip())) {
                    agentAddress.append(custAddress.getZip());
                }
                result.put("agentName", lclBl.getAgentAcct().getAccountName());
                result.put("agentAddress", agentAddress.toString());
                result.put("agentphone", custAddress.getPhone());
                result.put("agentemail", CommonUtils.isNotEmpty(custAddress.getEmail1())
                        ? custAddress.getEmail1() : custAddress.getEmail2());
                result.put("agentfax", custAddress.getFax());
            }
            LclBooking lclBooking = lclBl.getLclFileNumber().getLclBooking();
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            String freightAgentNo = null;
            Integer PodId = lclBooking.getBookingType().equals("T") ? lclBooking.getLclFileNumber().getLclBookingImport().getForeignPortOfDischarge().getId()
                    : lclBooking.getPortOfDestination().getId();
            if (lclBooking.getBookingType().equals("T")) {
                freightAgentNo = lclBooking.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo() != null
                        ? lclBooking.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo().getAccountno() : "";
                result.put("freightAgentNo", freightAgentNo); // this is for FreightAgentAcct
                result.put("freightAgentName", lclBooking.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo() != null
                        ? lclBooking.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo().getAccountName() : "");
                result.put("TranshipmentFlag", "true");
            } else if (lclBooking.getAgentAcct() != null) {
                freightAgentNo = lclBooking.getAgentAcct().getAccountno();
                result.put("freightAgentNo", freightAgentNo); // this is for FreightAgentAcct
                result.put("freightAgentName", lclBooking.getAgentAcct().getAccountName());
            }
            if (CommonUtils.isNotEmpty(freightAgentNo)) {
                String[] agencyInfo = new AgencyInfoDAO().getAgentPickAcctNo(PodId, freightAgentNo);
                if (agencyInfo != null) {
                    result.put("pod", CommonUtils.isNotEmpty(agencyInfo[1]) ? agencyInfo[1] : unLocationDAO.getUnNameCity(String.valueOf(lclBl.getPortOfDestination().getId())));
                    result.put("fd", CommonUtils.isNotEmpty(agencyInfo[2]) ? agencyInfo[2] : unLocationDAO.getUnNameCity(String.valueOf(lclBl.getFinalDestination().getId())));
                    if (CommonUtils.isNotEmpty(blForm.getDeliveryText())) {
                        result.put("fd", blForm.getDeliveryText());
                    }
                } else if (CommonUtils.isNotEmpty(blForm.getDeliveryText())) {
                    result.put("fd", blForm.getDeliveryText());
                    result.put("pod", unLocationDAO.getUnNameCity(String.valueOf(lclBl.getPortOfDestination().getId())));
                } else {
                    result.put("pod", unLocationDAO.getUnNameCity(String.valueOf(lclBl.getPortOfDestination().getId())));
                    result.put("fd", unLocationDAO.getUnNameCity(String.valueOf(lclBl.getFinalDestination().getId())));
                }
            } else {
                result.put("freightAgentNo", lclBl.getAgentAcct() != null ? lclBl.getAgentAcct().getAccountno() : "");// this is for PickupAgentAcct
                result.put("freightAgentName", lclBl.getAgentAcct() != null ? lclBl.getAgentAcct().getAccountName() : "");
                result.put("pod", unLocationDAO.getUnNameCity(String.valueOf(lclBl.getPortOfDestination().getId())));
                result.put("fd", unLocationDAO.getUnNameCity(String.valueOf(lclBl.getFinalDestination().getId())));
            }
            Gson gson = new Gson();
            out.print(gson.toJson(result));
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    public ActionForward calculateInsuranceCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            PortsDAO portsDAO = new PortsDAO();
            LclBlAcDAO lclBlACDAO = new LclBlAcDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LCLBlDAO blDao = new LCLBlDAO();
            Boolean insurance = request.getParameter("insurance").equals("Y") ? true : false;
            String valueOfGoods = request.getParameter("valueOfGoods");
            LCLBlForm lclBlForm = (LCLBlForm) form;
            LclBl lclBl = blDao.findById(longFileId);
            blDao.getCurrentSession().evict(lclBl);
            lclBl.setInsurance(insurance);
            lclBl.setValueOfGoods(new BigDecimal(valueOfGoods));
            List<LclBlPiece> lclCommodityList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String engmet = new String();
            String rateType = lclBlForm.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }

            String pcBoth = request.getParameter("pcBoth");
            String cif = "";
            LclBlAc lclBlAc = lclCostChargeDAO.manaualBlChargeValidate(longFileId, "INSURE", false);
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            if (insurance && lclCommodityList != null && lclCommodityList.size() > 0 && valueOfGoods != null && !valueOfGoods.trim().equals("")) {
                GlMappingDAO glmappingdao = new GlMappingDAO();
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0006", "LCLE", "AR");
                cif = lclChargesCalculation.calculateInsuranceChargeForBl(pooorigin, polorigin, destinationfd, destinationpod, lclCommodityList,
                        Double.parseDouble(valueOfGoods), getCurrentUser(request),
                        longFileId, lclBlAc, glmapping, request, lclBl.getBillToParty(), "I");
            } else if (!insurance && lclBlAc != null) {
                String noteDesc = "DELETED -> Charge Code -> " + lclBlAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBlAc.getArAmount();
                lclCostChargeDAO.delete(lclBlAc);
                new LclRemarksDAO().insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, noteDesc, loginUser.getUserId());
            }
            LclBlAc cafBlAc = lclCostChargeDAO.manaualBlChargeValidate(longFileId, "CAF", false);
            if (cafBlAc != null) {
                lclChargesCalculation.calculateCAFChargeForBl(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, pcBoth, loginUser, longFileId, request, ports, lclBl.getBillToParty());
            }
            if (CommonUtils.isNotEmpty(cif)) {
                lclBl.setCifValue(new BigDecimal(cif));
            } else {
                lclBl.setCifValue(null);
            }
            lclBl.setModifiedBy(loginUser);
            lclBl.setModifiedDatetime(new Date());
            blDao.saveOrUpdate(lclBl);
            if (!lclBl.getBillingType().equals("P")) {
                request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
            }
            request.setAttribute("lclBl", lclBl);
            request.setAttribute("checkBlInsurance", new LclBlAcDAO().checkBlInsurance(lclBl.getFileNumberId(), INSURANCE_CHARGE_CODE));
            List<LclBlAc> chargeList = new LclBlAcDAO().getLclCostByFileNumberAsc(longFileId);
            blUtils.setWeighMeasureForBl(request, lclCommodityList, engmet);
            blUtils.setRolledUpChargesForBl(chargeList, request, longFileId, lclBlACDAO, lclCommodityList,
                    lclBl.getBillingType(),
                    engmet, lclBl);
        } catch (Exception e) {
            log.info("Error in LCL calculateCharges method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward deleteDocumCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            PortsDAO portsDAO = new PortsDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclBlAcDAO lclBlACDAO = new LclBlAcDAO();
            LCLBlDAO blDao = new LCLBlDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBl lclBl = blDao.findById(longFileId);
            blDao.getCurrentSession().evict(lclBl);
            List<LclBlPiece> lclCommodityList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "", engmet = "";
            Ports ports = null;

            String rateType = lclBl.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }
            String chargeCode = LclCommonConstant.BLUESCREEN_CHARGECODE_DOCUM;
            String documentation = request.getParameter("documentation");
            LclBlAc lclBlAc = lclCostChargeDAO.manaualBlChargeValidate(longFileId, chargeCode, true);
            if (documentation.equals("N") && lclBlAc != null) {
                String remarksDesc = "DELETED -> Charge Code -> " + lclBlAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBlAc.getArAmount();
                lclCostChargeDAO.delete(lclBlAc);
                lclBl.setDocumentation(false);
                lclBl.setModifiedBy(loginUser);
                lclBl.setModifiedDatetime(new Date());
                blDao.saveOrUpdate(lclBl);
                new LclRemarksDAO().insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, remarksDesc, loginUser.getUserId());
            }
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            String pcBoth = request.getParameter("pcBoth");
            List<LclBlAc> chargeList = new LclBlAcDAO().getLclCostByFileNumberAsc(longFileId);
            LclBlAc cafBlAc = lclCostChargeDAO.manaualBlChargeValidate(longFileId, "CAF", false);
            if (cafBlAc != null) {
                lclChargesCalculation.calculateCAFChargeForBl(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, pcBoth, loginUser, longFileId, request, ports, lclBl.getBillToParty());
            }
            if (!lclBl.getBillingType().equals("P")) {
                request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
            }
            request.setAttribute("lclBl", lclBl);
            blUtils.setWeighMeasureForBl(request, lclCommodityList, engmet);
            blUtils.setRolledUpChargesForBl(chargeList, request, longFileId, lclBlACDAO, lclCommodityList, lclBl.getBillingType(), engmet, lclBl);
        } catch (Exception e) {
            log.info("Error in LCL deleteDocumCharge method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public boolean getPostAlert(String fileId) throws Exception {

        List<LclBlPiece> blpiece = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        boolean actualValue = false;
        BigDecimal actualWeightMetric = BigDecimal.ZERO;
        BigDecimal actualWeightImperial = BigDecimal.ZERO;
        double actualPieceCount = 0.000;
        for (LclBlPiece piece : blpiece) {
            actualWeightMetric = actualWeightMetric.add(null != piece.getActualVolumeMetric() ? piece.getActualVolumeMetric() : BigDecimal.ZERO);
            actualWeightImperial = actualWeightImperial.add(null != piece.getActualWeightImperial() ? piece.getActualWeightImperial() : BigDecimal.ZERO);
            actualPieceCount += (null != piece.getActualPieceCount() ? piece.getActualPieceCount() : 0);
        }
        if (!CommonUtils.isEmpty(actualWeightMetric)
                && !CommonUtils.isEmpty(actualWeightImperial)
                && !CommonUtils.isEmpty(actualPieceCount)) {
            actualValue = true;
        }
        return actualValue;

    }

    public ActionForward calculateDeliveryMetroChargeBl(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            LCLBlDAO blDao = new LCLBlDAO();
            LclBlAcDAO lclBlACDAO = new LclBlAcDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBl lclBl = blDao.findById(longFileId);
            String radioValue = request.getParameter("deliveryMetro");
            blDao.getCurrentSession().evict(lclBl);
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            PortsDAO portsDAO = new PortsDAO();
            List<LclBlPiece> lclCommodityList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String rateType = lclBl.getRateType();
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            String engmet = new String();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                destinationfd = ports.getEciportcode();
                engmet = ports.getEngmet();
            }
            String deliveryMetro = new String();
            if (radioValue.equalsIgnoreCase("I")) {
                deliveryMetro = "0060";
                lclBl.setDeliveryMetro("I");
            } else {
                deliveryMetro = "0015";
                lclBl.setDeliveryMetro("O");
            }
            LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
            //lclBl.setDeliveryMetro(radioValue);
            if (radioValue.equalsIgnoreCase("I") || radioValue.equalsIgnoreCase("O")) {
                lclBlChargesCalculation.calculateChargeForDeliveyMetro(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, getCurrentUser(request), longFileId, engmet, deliveryMetro, lclBl.getBillToParty());
            } else if (radioValue.equals("N")) {
                lclBl.setDeliveryMetro("N");
                String chargeCodeArray[] = chargeCodeArray = new String[]{"DELMET", "DELOUT"};
                LclRemarksDAO remarksDAO = new LclRemarksDAO();
                for (int i = 0; i < chargeCodeArray.length; i++) {
                    LclBlAc lclBlAc = lclCostChargeDAO.manaualBlChargeValidate(longFileId, chargeCodeArray[i], false);
                    // LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, chargeCodeArray[i], false);
                    if (lclBlAc != null) {
                        String noteDesc = "DELETED -> Charge Code -> " + lclBlAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBlAc.getArAmount();
                        lclCostChargeDAO.delete(lclBlAc);
                        remarksDAO.insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, noteDesc, loginUser.getUserId());
                    }
                }
            }
            String pcBoth = request.getParameter("pcBoth");
            LclBlAc cafBlAc = lclCostChargeDAO.manaualBlChargeValidate(longFileId, "CAF", false);
            if (cafBlAc != null) {
                lclChargesCalculation.calculateCAFChargeForBl(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, pcBoth, loginUser, longFileId, request, ports, lclBl.getBillToParty());
            }
            lclBl.setModifiedBy(loginUser);
            lclBl.setModifiedDatetime(new Date());
            blDao.saveOrUpdate(lclBl);
            List<LclBlAc> chargeList = new LclBlAcDAO().getLclCostByFileNumberAsc(longFileId);
            if (!lclBl.getBillingType().equals("P")) {
                request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking());//for Freight Agent Acct
            }
            request.setAttribute("lclBl", lclBl);
            blUtils.setWeighMeasureForBl(request, lclCommodityList, engmet);
            blUtils.setRolledUpChargesForBl(chargeList, request, longFileId, lclBlACDAO, lclCommodityList,
                    lclBl.getBillingType(), engmet, lclBl);
        } catch (Exception e) {
            log.info("Error in LCL calculateDeliveryMetroCharge method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public void reChargeAdjustment(String blChargeId, String adjustmentAmount, String comments, String userId) throws Exception {
        new LclBlAcDAO().updateAdjustmentCharge(new BigInteger(blChargeId), new BigDecimal(adjustmentAmount), comments, Integer.parseInt(userId));
    }
}
