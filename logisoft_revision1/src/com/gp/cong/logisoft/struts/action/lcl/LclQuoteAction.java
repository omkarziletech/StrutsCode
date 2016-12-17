package com.gp.cong.logisoft.struts.action.lcl;

import com.google.gson.Gson;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.ExportUtils;
import com.gp.cong.lcl.common.constant.ImportQuoteUtils;
import com.gp.cong.lcl.common.constant.LclQuoteUtils;
import com.gp.cong.lcl.dwr.LCLQuoteImportChargeCalc;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclQuotationChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingDestinationServices;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingHsCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteDestinationServices;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHazmat;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHotCode;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHsCode;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImportAms;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.CommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.QuoteCommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLQuoteForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclQuoteCostAndChargeForm;
import com.logiware.common.filter.JspWrapper;
import com.logiware.thread.LclFileNumberThread;
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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import com.gp.cong.logisoft.domain.User;

/**
 *
 * @author Owner
 */
public class LclQuoteAction extends LogiwareDispatchAction implements LclCommonConstant, ConstantsInterface {
    
    private static final Logger log = Logger.getLogger(LclQuoteAction.class);
    private static final String LCL_QUOTE_EXPORT = "exportQuote";
    private static final String LCL_QUOTE_IMPORT = "importQuote";
    private static final String LCL_QUOTE_VOYAGE = "lclQuoteVoyage";
    private static final String LCL_BOOKING_VOYAGE = "lclVoyage";
    private static final String LCL_QUOTE_PLAN = "lclQuotePlan";
    private static final String LCL_SPOTRATE = "lclSpotRate";
    private static String CHARGE_DESC = "chargeDesc";
    private LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
    private LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
    private LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
    
    public ActionForward newQuote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        
        LclDwr lcldwr = new LclDwr();
        LclUtils lclUtils = new LclUtils();
        HttpSession session = request.getSession();
        String FRWD_PAGE = "";
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclUtils.clearLCLSession(lclSession);
        User loginUser = getCurrentUser(request);
        RoleDuty roleDuty = checkRoleDuty(request);
        LclQuote lclQuote = new LclQuote();
        LclContact lclContact = new LclContact();
        if (LCL_EXPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
            if (roleDuty.isChangeDefaultFF()) {
                TradingPartner fwdTp = new TradingPartnerDAO().findById("NOFFAA0001");
                CustomerAddress custAdd = fwdTp.getPrimaryCustAddr();
                lclQuote.setFwdAcct(fwdTp);
                lclContact.setFax1(custAdd.getFax());
                lclContact.setCompanyName(fwdTp.getAccountName());
                lclContact.setPhone1(custAdd.getPhone());
                lclQuote.setFwdContact(lclContact);
            }
            
            request.setAttribute("lclBookingExport", new LclBookingExport());
            if (null != loginUser.getTerminal()) {
                lclQuoteForm.setTerminalLocation(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
                lclQuoteForm.setTrmnum(loginUser.getTerminal().getTrmnum());
                if (loginUser.getTerminal().getTerminalLocation().equalsIgnoreCase("QUEENS, NY")) {
                    String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                    if (companyCode.equalsIgnoreCase("03")) {
                        lclQuoteForm.setBusinessUnit("ECI");
                    }
                }
            }
            FRWD_PAGE = LCL_QUOTE_EXPORT;
        } else {
            //commented due to 11433 mantis item
//            if (null != loginUser.getImportTerminal()) {
//                lclQuoteForm.setTerminalLocation(loginUser.getImportTerminal().getTrmnum() + " - " + loginUser.getImportTerminal().getTerminalLocation());
//                lclQuoteForm.setTrmnum(loginUser.getImportTerminal().getTrmnum());
//            }
            lclQuoteForm.setShipperToolTip(lcldwr.getContactDetails("Shipper", "", "", ""));
            lclQuoteForm.setConsigneeToolTip(lcldwr.getContactDetails("Consignee", "", "", ""));
            lclQuoteForm.setNotifyToolTip(lcldwr.getContactDetails("Notify", "", "", ""));
            lclQuoteForm.setNotify2ToolTip(lcldwr.getContactDetails("Notify2", "", "", ""));
            FRWD_PAGE = LCL_QUOTE_IMPORT;
        }
        lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
        lclQuote.setEnteredBy(loginUser);
        lclQuote.setEnteredDatetime(DateUtils.formatDateAndParseDate(new Date()));
        request.setAttribute("lclQuote", lclQuote);
        request.setAttribute("lclQuoteForm", lclQuoteForm);
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        lclSession.setQuoteCommodityList(null);
        lclSession.setQuoteDetailList(null);
        session.setAttribute("lclSession", lclSession);
        return mapping.findForward(FRWD_PAGE);
    }
    
    public ActionForward editQuote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
        LclUtils lclUtils = new LclUtils();
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        User loginUser = getCurrentUser(request);
        String FRWD_PAGE = null, moduleName = null;
        Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        LclQuote lclQuote = lclQuoteDAO.getByProperty("lclFileNumber.id", fileId);
        lclQuoteDAO.getCurrentSession().evict(lclQuote);
        lclQuoteForm.getLclQuote().setClientContact(lclQuote.getClientContact());
        lclQuoteForm.getLclQuote().setSupContact(lclQuote.getSupContact());
        lclQuoteForm.getLclQuote().setShipContact(lclQuote.getShipContact());
        lclQuoteForm.getLclQuote().setConsContact(lclQuote.getConsContact());
        lclQuoteForm.getLclQuote().setNotyContact(lclQuote.getNotyContact());
        lclQuoteForm.getLclQuote().setNotify2Contact(lclQuote.getNotify2Contact());
        
        this.setDoorCityZip(lclQuoteForm, lclQuote);// Set Door Origin Values
        String remarkTypes[] = {"Special Remarks", "Special Remarks Pod", "Gri Remarks Pod", "G", "Internal Remarks Pod", "Internal Remarks", "OSD", "Loading Remarks", "E", "AutoRates", "Priority View"};
        lclQuoteUtils.getRemarks(lclQuoteForm, request, fileId, remarkTypes, lclRemarksDAO);
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclQuote.getQuoteType())) {
            FRWD_PAGE = LCL_QUOTE_EXPORT;
            moduleName = LCL_EXPORT;
        } else {
            FRWD_PAGE = LCL_QUOTE_IMPORT;
            moduleName = LCL_IMPORT;
        }
        if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            LclQuoteImport lclQuoteImport = lclQuote.getLclFileNumber().getLclQuoteImport();
            if (lclQuoteImport.getUsaPortOfExit() != null) {
                lclQuoteForm.setPortExitId(lclQuoteImport.getUsaPortOfExit().getId());
                lclQuoteForm.setPortExit(lclUtils.getConcatenatedOriginByUnlocation(lclQuoteImport.getUsaPortOfExit()));
            }
            if (lclQuoteImport.getForeignPortOfDischarge() != null) {
                lclQuoteForm.setForeignDischargeId(lclQuoteImport.getForeignPortOfDischarge().getId());
                lclQuoteForm.setForeignDischarge(lclUtils.getConcatenatedOriginByUnlocation(lclQuoteImport.getForeignPortOfDischarge()));
            }
            importQuoteUtils.setUpcomingSailings(lclQuote, lclQuoteImport, request);
            request.setAttribute("lclQuoteImport", lclQuoteImport);
        }
        if (LCL_EXPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
            if (lclQuoteForm.isUps()) {
                if (CommonUtils.isNotEmpty(lclQuoteForm.getSmallParcelRemarks())) {
                    new LclRemarksDAO().insertLclRemarks(lclQuote.getFileNumberId(), REMARKS_QT_AUTO_NOTES,
                            lclQuoteForm.getSmallParcelRemarks(), loginUser.getUserId());
                    lclQuoteForm.setSmallParcelRemarks("");
                }
            }
        }
        lclQuoteForm.setLclQuote(lclQuote);
        if (lclQuote.getPortOfOrigin() != null) {
            lclQuoteForm.setPortOfOriginId(lclQuote.getPortOfOrigin().getId());
        }
        if (lclQuote.getPortOfLoading() != null) {
            lclQuoteForm.setPortOfLoadingId(lclQuote.getPortOfLoading().getId());
        }
        if (lclQuote.getPortOfDestination() != null) {
            lclQuoteForm.setPortOfDestinationId(lclQuote.getPortOfDestination().getId());
        }
        lclQuoteForm.setModuleName(moduleName);
        lclQuoteForm.setFinalDestinationId(lclQuote.getFinalDestination().getId());
        List<LclQuotePiece> lclQuotePiecesList = lclQuoteUtils.setCommodityList(fileId, request);
        setOtiNumber(lclQuoteForm, fileId);
        lclQuoteUtils.setRequestValues(lclQuoteForm, lclQuote, lclUtils, lclQuotePiecesList, request);
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, moduleName);
        if (CommonUtils.isEqual(moduleName, LCL_IMPORT)) {
            importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePiecesList);
            importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, fileId, lclQuotePiecesList, lclQuote.getBillingType());
        } else {
            lclQuoteForm.setBusinessUnit(lclQuote.getLclFileNumber().getBusinessUnit());
            if ((null != lclQuote.getAgentAcct() && null != lclQuote.getAgentAcct().getAccountno()) && null != lclQuote.getPortOfDestination()) {
                lclQuoteForm.setAgentBrand(new AgencyInfoDAO().getAgentLevelBrand(lclQuote.getAgentAcct().getAccountno(), lclQuote.getPortOfDestination().getUnLocationCode()));
            }
            exportQuoteUtils.setTrmandEciPortCode(lclQuoteForm, lclQuote, lclUtils);
            exportQuoteUtils.setUpcomingSailings(lclQuote, lclQuoteForm.getRelayOverride(), request, "false");
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclQuotePiecesList, chargeList, loginUser, false, request);//set Export Rate Details
        }
        lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
        request.setAttribute("lclQuote", lclQuote);
        request.setAttribute("lclQuoteForm", lclQuoteForm);
        return mapping.findForward(FRWD_PAGE);
    }
    
    public ActionForward replicateQuote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
        String FRWD_PAGE = null;
        String fileNumberId = request.getParameter("fileNumberId");
        LclQuote lclQuoteReplicate = new LclQuote();
        if (fileNumberId != null && !fileNumberId.trim().equals("")) {
            LclQuote lclQuote = new LCLQuoteDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclQuote.getQuoteType())) {
                FRWD_PAGE = LCL_QUOTE_EXPORT;
                lclQuoteForm.setModuleName(LCL_EXPORT);
            } else {
                FRWD_PAGE = LCL_QUOTE_IMPORT;
                lclQuoteForm.setModuleName(LCL_IMPORT);
            }
            if (CommonUtils.isEqual(lclQuote.getLclFileNumber().getState(), "B")) {
                LclBooking lclBooking = lclBookingDAO.findById(Long.parseLong(fileNumberId));
                PropertyUtils.copyProperties(lclQuoteReplicate, lclBooking);
                lclQuoteReplicate.setBillingTerminal(lclBooking.getTerminal());
            } else {
                lclQuoteReplicate = (LclQuote) BeanUtils.cloneBean(lclQuote);
            }
            lclQuoteForm.setReplicateFileNumber(lclQuoteReplicate.getLclFileNumber().getFileNumber());
            lclQuoteReplicate.setQuoteType(null);
            lclQuoteReplicate.setEnteredBy(null);
            lclQuoteReplicate.setDefaultAgent(true);
            lclQuoteReplicate.setFileNumberId(null);
            lclQuoteReplicate.setLclFileNumber(null);
            lclQuoteReplicate.setConsAcct(null);
            lclQuoteReplicate.setConsContact(null);
            lclQuoteReplicate.setFwdAcct(null);
            lclQuoteReplicate.setFwdContact(null);
            lclQuoteReplicate.setNotyAcct(null);
            lclQuoteReplicate.setSpotRate(false);
            lclQuoteReplicate.setNotyContact(null);
            lclQuoteReplicate.setShipAcct(null);
            lclQuoteReplicate.setShipContact(null);
            lclQuoteReplicate.setSupAcct(null);
            lclQuoteReplicate.setSupContact(null);
            lclQuoteReplicate.setPooWhseLrdt(null);
            lclQuoteReplicate.setFdEta(null);
            lclQuoteReplicate.setQuoteComplete(false);
            lclQuoteForm.setLclQuote(lclQuoteReplicate);
            lclQuoteForm.setPortOfOriginId(lclQuoteReplicate.getPortOfOrigin().getId());
            lclQuoteForm.setFinalDestinationId(lclQuoteReplicate.getFinalDestination().getId());
            lclQuoteReplicate.setPooDoor(false);
            LclRemarksDAO lclremarksdao = new LclRemarksDAO();
            String lclRemarks = lclremarksdao.getLclRemarksByTypeSQL(fileNumberId, REMARKS_TYPE_INTERNAL_REMARKS);
            lclQuoteForm.setInternalRemarks(lclRemarks);
            lclRemarks = lclremarksdao.getLclRemarksByTypeSQL(fileNumberId, REMARKS_TYPE_GRI);
            lclQuoteForm.setPortGriRemarks(lclRemarks);
            lclRemarks = lclremarksdao.getLclRemarksByTypeSQL(fileNumberId, REMARKS_TYPE_SPECIAL_REMARKS);
            lclQuoteForm.setSpecialRemarks(lclRemarks);
//            if (lclQuoteReplicate.getPortOfOrigin() != null && lclQuoteReplicate.getPortOfLoading() != null && lclQuoteReplicate.getPortOfDestination() != null && lclQuoteReplicate.getFinalDestination() != null) {
//                List<LclBookingVoyageBean> voyageList = new LclBookingPlanDAO().getBookingVoyageList(lclQuoteReplicate.getPortOfOrigin().getId(),
//                        lclQuoteReplicate.getPortOfLoading().getId(), lclQuoteReplicate.getPortOfDestination().getId(), lclQuoteReplicate.getFinalDestination().getId(), "V");
//                request.setAttribute("voyageList", voyageList);
//            }
            exportQuoteUtils.setUpcomingSailings(lclQuoteReplicate, null, request, "false");
            if (lclQuoteReplicate.getPortOfLoading() != null) {
                request.setAttribute("pol", lclQuoteReplicate.getPortOfLoading().getUnLocationName());
            }
            if (lclQuoteReplicate.getPortOfDestination() != null) {
                request.setAttribute("pod", lclQuoteReplicate.getPortOfDestination().getUnLocationName());
            }
            request.setAttribute("lclQuote", lclQuoteReplicate);
            lclQuoteForm.setLclQuote(lclQuoteReplicate);
            lclQuoteForm.setFileNumberId(null);
            request.setAttribute("lclQuoteForm", lclQuoteForm);
        }
        return mapping.findForward(FRWD_PAGE);
    }
    
    public ActionForward backToMainScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("mainScreen");
    }
    
    public ActionForward saveQuote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
        Date todayDate = new Date();
        User loginUser = getCurrentUser(request);
        HttpSession session = request.getSession();
        LclUtils lclUtils = new LclUtils();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String FRWD_PAGE = LCL_IMPORT.equalsIgnoreCase(lclQuoteForm.getModuleName()) ? LCL_QUOTE_IMPORT : LCL_QUOTE_EXPORT;
        String moduleName = lclQuoteForm.getModuleName();
        Boolean isNewFile = lclQuoteForm.getLclQuote().getLclFileNumber() == null;
        if ((lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0 && isNewFile) || !isNewFile
                || LCL_IMPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
            synchronized (LclQuoteAction.class) {
                if (lclQuoteForm.getLclQuote().getEnteredBy() == null) {
                    lclQuoteForm.getLclQuote().setEnteredDatetime(todayDate);
                    lclQuoteForm.getLclQuote().setEnteredBy(loginUser);
                }
                lclQuoteForm.getLclQuote().setModifiedBy(loginUser);
                lclQuoteForm.getLclQuote().setModifiedDatetime(todayDate);
                LclFileNumber lclFileNumber = lclQuoteForm.getLclQuote().getLclFileNumber();
                boolean isNewQuote = false;
                if (lclFileNumber == null) {
                    LclFileNumberThread thread = new LclFileNumberThread();
                    String fileNumber = thread.getFileNumber();
                    Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
                    isNewFile = Boolean.TRUE;
                    if (fileNumberId == 0) {
                        lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "Q");
                    } else {
                        thread = new LclFileNumberThread();
                        fileNumber = thread.getFileNumber();
                        lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "Q");
                    }
                    if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                        lclFileNumberDAO.getSession().getTransaction().begin();
                    }
                    isNewQuote = true;
                }
                lclQuoteUtils.quoteStatus(lclQuoteForm, lclFileNumber, loginUser, lclRemarksDAO);
                lclQuoteUtils.setRemarks(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
                lclQuoteForm.getLclQuote().setLclFileNumber(lclFileNumber);
                lclQuoteForm.getLclQuote().setFileNumberId(lclFileNumber.getId());
                lclQuoteForm.setFileNumberId(lclFileNumber.getId().toString());
                // lclQuoteUtils.saveLclContact(lclQuoteForm, lclFileNumber, todayDate, loginUser);
                lclQuoteForm.getLclQuote().getPooWhseContact().setLclFileNumber(lclFileNumber);
                lclQuoteUtils.setUserAndDateTime(lclQuoteForm.getLclQuote().getPooWhseContact(), todayDate, loginUser);
                if (CommonUtils.isNotEmpty(lclQuoteForm.getWhsCode())) {
                    Warehouse deliverWarehouse = new WarehouseDAO().getWareHouseBywarehsNo(lclQuoteForm.getWhsCode());
                    lclQuoteForm.getLclQuote().getPooWhseContact().setWarehouse(null != deliverWarehouse ? deliverWarehouse : null);
                }
                
                if (lclQuoteForm.getConsigneeManualContact() != null && !lclQuoteForm.getConsigneeManualContact().equals("") && lclQuoteForm.getNewConsigneeContact() != null && lclQuoteForm.getNewConsigneeContact().equals("on")) {
                    lclQuoteForm.getLclQuote().getConsContact().setContactName(lclQuoteForm.getConsigneeManualContact());
                }
                if (lclQuoteForm.getForwarederContactManual() != null && !lclQuoteForm.getForwarederContactManual().equals("") && lclQuoteForm.getNewForwarderContact() != null && lclQuoteForm.getNewForwarderContact().equals("on")) {
                    lclQuoteForm.getLclQuote().getFwdContact().setContactName(lclQuoteForm.getForwarederContactManual());
                }
                if (lclQuoteForm.getShipperManualContact() != null && !lclQuoteForm.getShipperManualContact().equals("") && lclQuoteForm.getNewShipperContact() != null && lclQuoteForm.getNewShipperContact().equals("on")) {
                    lclQuoteForm.getLclQuote().getShipContact().setContactName(lclQuoteForm.getShipperManualContact());
                }
                if (lclQuoteForm.getClientContactManual() != null && !lclQuoteForm.getClientContactManual().equals("") && lclQuoteForm.getNewClientContact() != null && lclQuoteForm.getNewClientContact().equals("on")) {
                    lclQuoteForm.getLclQuote().getClientContact().setContactName(lclQuoteForm.getClientContactManual());
                }
                //save value in lcl_booking_import table
                if (CommonUtils.isEmpty(lclQuoteForm.getLclQuote().getRateType())) {
                    lclQuoteForm.getLclQuote().setRateType("R");
                }
                if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                    request.setAttribute("lclQuoteImport", importQuoteUtils.saveQteImp(lclQuoteForm, lclFileNumber, loginUser, todayDate));
                    if (isNewQuote) {
                        this.addingQuoteClientHotCodes(mapping, lclQuoteForm, request, response);
                    }
                }
                lclQuoteForm.setEnums(lclQuoteForm.getTransShipMent());//hard code for enum
                this.addClient(lclQuoteForm, lclFileNumber, request);
                this.addSupplierName(lclQuoteForm, lclFileNumber, loginUser, todayDate);
                this.addShipperName(lclQuoteForm, lclFileNumber, request);
                this.addConsigneeName(lclQuoteForm, lclFileNumber, request);
                this.addNotifyName(lclQuoteForm, lclFileNumber, request);
                this.addForwarderName(lclQuoteForm, lclFileNumber, request);
                this.addNotify2Name(lclQuoteForm, lclFileNumber, loginUser, todayDate);
                this.addExternalRemarks(lclQuoteForm, lclFileNumber, loginUser, request);
                if (LCL_EXPORT.equalsIgnoreCase(moduleName) || "Y".equalsIgnoreCase(lclQuoteForm.getTransShipMent())) {
                    exportQuoteUtils.setLclBookingExport(lclQuoteForm, lclFileNumber, request, loginUser);
                    if (isNewQuote) {
                        this.addingQuoteClientHotCodes(mapping, lclQuoteForm, request, response);
                    }
                }
                this.addDoorCityZip(lclQuoteForm, lclFileNumber, todayDate, loginUser);
                Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
                lcl3pRefNoDAO.deleteLcl3PRef(lclFileNumber.getId(), LCL_3PREF_TYPE_OTI);//delete OTI values from 3pRef
                if (CommonUtils.isNotEmpty(lclQuoteForm.getOtiNumber())) {
                    lcl3pRefNoDAO.save3pRefNo(lclFileNumber.getId(), LCL_3PREF_TYPE_OTI, lclQuoteForm.getOtiNumber());
                }
                //  exportQuoteUtils.saveQtePlan(lclQuoteForm, lclFileNumber, loginUser, todayDate);
                lclQuoteUtils.saveCommodity(lclQuoteForm, lclFileNumber, loginUser, lclSession, todayDate);
                if (lclQuoteForm.getLclQuote().getInsurance() == null) {
                    lclQuoteForm.getLclQuote().setInsurance(false);
                }
                lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
                if (null != lclQuoteForm.getQuoteComplete()) {
                    if ((CommonUtils.isNotEmpty(lclQuoteForm.getQuoteComplete()) || CommonUtils.isNotEmpty(lclQuoteForm.getQuoteCompleted())) && lclQuoteForm.getQuoteComplete().equalsIgnoreCase("Y") || lclQuoteForm.getQuoteCompleted().equalsIgnoreCase("Y")) {
                        lclQuoteForm.getLclQuote().setQuoteComplete(true);
                    }
                }
                lclQuoteForm.getLclQuote().setModifiedBy(getCurrentUser(request));
                lclQuoteForm.getLclQuote().setModifiedDatetime(todayDate);
                lclQuoteDAO.getCurrentSession().clear();
                if (CommonUtils.isNotEmpty(lclQuoteForm.getFinalDestination()) && lclQuoteForm.getFinalDestination().startsWith("Unknown")) {
                    lclQuoteForm.getLclQuote().setPortOfLoading(null);
                    lclQuoteForm.getLclQuote().setPortOfDestination(null);
                }
                lclQuoteDAO.saveOrUpdate(lclQuoteForm.getLclQuote());
                //    this.quoteStatus(lclQuoteForm, lclFileNumber, loginUser);
                //  this.addLclQuotePadDetails(lclQuoteForm, lclFileNumber, request, null);//no need for this method
                if (LCL_EXPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
                    exportQuoteUtils.addBkgUpdateorDeleteContactNotes(lclQuoteForm, lclFileNumber, loginUser, lclRemarksDAO);
                    exportQuoteUtils.addInsertBkgContactNotes(lclQuoteForm, lclFileNumber, loginUser, lclRemarksDAO);
                    if (lclQuoteForm.isUps()) {
                        if (CommonUtils.isNotEmpty(lclQuoteForm.getSmallParcelRemarks())) {
                            new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_QT_AUTO_NOTES,
                                    lclQuoteForm.getSmallParcelRemarks(), loginUser.getUserId());
                            lclQuoteForm.setSmallParcelRemarks("");
                        }
                    }
                }
                lclSession.setQuoteCommodityList(null);
                lclSession.setQuoteDetailList(null);
                List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId());
                LclQuoteAc cafQuoteAc = null;
                Double totalAmount = 0.00;
                List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostByFileNumberAsc(lclFileNumber.getId(), moduleName);
                LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
                if (CommonUtils.isNotEmpty(lclCommodityList) && CommonUtils.isEmpty(chargeList)) {
                    for (LclQuotePiece lbp : lclCommodityList) {
                        if (lclSession.getQuoteAcList() != null && lclSession.getQuoteAcList().size() > 0) {
                            for (LclQuoteAc lclQuoteAc : lclSession.getQuoteAcList()) {
                                if (lclQuoteAc != null) {
                                    lclQuoteAc.setLclFileNumber(lclFileNumber);
                                    lclQuoteAc.setLclQuotePiece(lbp);
                                    if (LCL_IMPORT.equalsIgnoreCase(moduleName)
                                            && CHARGE_CODE_CAF.equalsIgnoreCase(lclQuoteAc.getArglMapping().getChargeCode())) {
                                        cafQuoteAc = lclQuoteAc;
                                    } else {
                                        totalAmount = totalAmount + lclQuoteAc.getArAmount().doubleValue();
                                        lclquoteacdao.save(lclQuoteAc);
                                    }
                                }
                            }
                        }
                        if (cafQuoteAc != null) {
                            Double cafAmt = lclQuoteImportChargeCalc.calculateCAFAmount(cafQuoteAc, totalAmount);
                            cafQuoteAc.setArAmount(new BigDecimal(cafAmt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclquoteacdao.saveOrUpdate(cafQuoteAc);
                        }
                        lbp.setLclQuoteHazmatList(new LclQuoteHazmatDAO().findByFileAndCommodityList(lclFileNumber.getId(), lbp.getId()));
                        lbp.setLclQuoteAcList(new LclQuoteAcDAO().findByFileAndCommodityList(lclFileNumber.getId(), lbp.getId()));
                        lbp.setLclQuotePieceWhseList(new LclQuotePieceWhseDAO().findByProperty("lclQuotePiece.id", lbp.getId()));
                    }
                }
                request.setAttribute("lclCommodityList", lclCommodityList);
                lclSession.setQuoteAcList(null);
                session.setAttribute("lclSession", lclSession);
                LclQuote lclQuote = new LclQuote();
                lclQuote = lclQuoteDAO.getByProperty("lclFileNumber.id", lclFileNumber.getId());
                lclQuoteDAO.getCurrentSession().evict(lclQuote);
                
                request.setAttribute("lclQuote", lclQuote);
                String remarkTypes[] = {"AutoRates", "Priority View"};//remarks
                lclQuoteUtils.getRemarks(lclQuoteForm, request, lclQuote.getFileNumberId(), remarkTypes, lclRemarksDAO);
                chargeList = lclquoteacdao.getLclCostByFileNumberAsc(lclFileNumber.getId(), moduleName);
                if (CommonUtils.isEqual(moduleName, "Imports")) {
                    if (lclQuote != null && lclQuote.getLclFileNumber() != null && CommonUtils.notIn(lclQuote.getLclFileNumber().getBusinessUnit(), "ECU", "ECI") || isNewFile) {
                        if (lclQuote.getSupAcct() != null && lclQuote.getSupAcct().getAccountno() != null) {
                            String newBrand = new TradingPartnerDAO().getBusinessUnit(lclQuote.getSupAcct().getAccountno());
                            new LclFileNumberDAO().updateEconoEculine(newBrand, String.valueOf(lclQuote.getFileNumberId()));
                        }
                    }
                    importQuoteUtils.setUpcomingSailings(lclQuote, lclQuote.getLclFileNumber().getLclQuoteImport(), request);//upcomong Sailings List
                    importQuoteUtils.setWeighMeasureForImpQuote(request, lclCommodityList);
                    importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, lclFileNumber.getId(), lclCommodityList, lclQuote.getBillingType());
                } else {
                    if (CommonUtils.isNotEmpty(lclQuoteForm.getEculineCommodity())) {
                        updateEconoEculineFromComm(lclQuoteForm, request);
                    } else {
                        String previousType = new LclFileNumberDAO().getBusinessUnit(lclQuoteForm.getFileNumberId());
                        if (CommonUtils.isEmpty(previousType)) {
                            new LclFileNumberDAO().updateEconoEculine(lclQuoteForm.getBusinessUnit(), lclQuoteForm.getFileNumberId());
                        }
                    }
                    exportQuoteUtils.setTrmandEciPortCode(lclQuoteForm, lclQuote, lclUtils);
                    exportQuoteUtils.setUpcomingSailings(lclQuote, lclQuoteForm.getRelayOverride(), request, "false");//upcomong Sailings List
                    exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
                    // new LCLBookingDAO().updateModifiedDateTime(Long.parseLong(lclQuoteForm.getFileNumberId()),loginUser.getUserId());
                }
                lclQuoteUtils.setRequestValues(lclQuoteForm, lclQuote, lclUtils, lclCommodityList, request);
                try {
                    if (CommonUtils.isNotEmpty(lclCommodityList)) {
                            lclCommodityList.get(0).getLclQuoteAcList().size();
                            lclCommodityList.get(0).getLclQuotePieceDetailList().size();
                    }
                } catch (Exception e) {// lazylinitialization Exception
                    lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId());
                    request.setAttribute("lclCommodityList", lclCommodityList);
                }
                request.setAttribute("lclQuoteForm", lclQuoteForm);
            }
        }
        return mapping.findForward(FRWD_PAGE);
    }
    
    public ActionForward displayVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        Integer pooId, polId, podId, fdId;
        if (LCL_IMPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
            pooId = lclQuoteForm.getPortOfDestinationId();
            fdId = lclQuoteForm.getFinalDestinationId();
            polId = lclQuoteForm.getPortExitId();
            podId = lclQuoteForm.getForeignDischargeId();
        } else {
            pooId = lclQuoteForm.getPortOfOriginId();
            polId = lclQuoteForm.getPortOfLoadingId();
            podId = lclQuoteForm.getPortOfDestinationId();
            fdId = lclQuoteForm.getFinalDestinationId();
        }
        String relay = request.getParameter("relay");
        exportQuoteUtils.setUpcomingSailing(pooId, polId, podId, fdId, relay, request);//Upcomong Sailings List
        return mapping.findForward("lclVoyage");
    }
    
    public ActionForward displayPolPodVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        String polId = request.getParameter("polid");
        String podId = request.getParameter("podid");
        String relay = request.getParameter("relay");
        if (polId != null && !polId.trim().equals("") && podId != null && !podId.trim().equals("")) {
            exportQuoteUtils.setUpcomingSailing(quoteForm.getPortOfOriginId(), Integer.parseInt(polId), Integer.parseInt(podId),
                    quoteForm.getFinalDestinationId(), relay, request);//Upcomong Sailings List
        }
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }
    
    public ActionForward lclRelayFind(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        Integer pooId = quoteForm.getPortOfOriginId();
        Integer fdId = quoteForm.getFinalDestinationId();
        exportQuoteUtils.setRelayDetails(pooId, fdId, request, quoteForm);//Set Relay for Pol and Pod
        return mapping.findForward(LCL_QUOTE_PLAN);
    }
    
    public ActionForward clientSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchByValue", request.getParameter("searchByValue"));
        return mapping.findForward("clientSearch");
    }
    
    public ActionForward addLcl3pReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String FORWARD_PAGE = "", _3pRef_type = "";
        String referenceName = request.getParameter("refTypeFlag");
        String refValue = request.getParameter("refValue");
        String fileNumberId = request.getParameter("fileNumberId");
        String insertInbondFlag = request.getParameter("insertInbondFlag");
        String remarks = "";
        String inbFlag = request.getParameter("inbFlag") != null ? request.getParameter("inbFlag") : "";
        User user = getCurrentUser(request);
        if (referenceName.equals("hotCodes") && CommonUtils.isNotEmpty(fileNumberId)) {
            LclQuoteHotCodeDAO hotCodeDAO = new LclQuoteHotCodeDAO();
            LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
            List<LclQuotePiece> lclQuotePieceList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (!lclQuotePieceList.isEmpty() && null != lclQuoteForm.getGenCodefield1() && lclQuoteForm.getGenCodefield1().equalsIgnoreCase("Y")) {
                for (LclQuotePiece lclQuotePiece : lclQuotePieceList) {
                    if (!lclQuotePiece.isHazmat()) {
                        lclQuotePiece.setHazmat(Boolean.TRUE);
                        lclQuotePieceDAO.save(lclQuotePiece);
                    }
                }
                _3pRef_type = _3PARTY_TYPE_HTC;
                remarks = "Inserted - Hot Code#" + " " + lclQuoteForm.getHotCodes();
                FORWARD_PAGE = "hoteCodes";
            }
            if (CommonUtils.isNotEmpty(refValue) && inbFlag.equalsIgnoreCase("true") && insertInbondFlag.equalsIgnoreCase("true")) {
                String refNo = lcl3pRefNoDAO.getReferenceSize(fileNumberId, refValue);
                Boolean isExistHotCode = hotCodeDAO.isHotCodeExist(refValue, fileNumberId);
                if (isExistHotCode) {
                    hotCodeDAO.insertQuery(fileNumberId, refValue.toUpperCase(), user.getUserId());
                }
                boolean isInbond = new LclInbondsDAO().isInbondExists(fileNumberId);
                _3pRef_type = _3PARTY_TYPE_HTC;
                FORWARD_PAGE = "hoteCodes";
                remarks = "Inserted - Hot Code#" + " " + refValue.toUpperCase();
                if (isInbond && CommonUtils.isEmpty(refNo)) {
                } else {
//                    refValue = "";
                }
            } else {
                FORWARD_PAGE = "hoteCodes";
            }
            String isEculineHotCode = CommonUtils.isNotEmpty(refValue) ? refValue.substring(0, refValue.indexOf("/")) : "";
            if (hotCodeDAO.isHotCodeExist(refValue, fileNumberId)) {
//                if ("EBL".equalsIgnoreCase(isEculineHotCode)) {
//                    if (!"ECU".equalsIgnoreCase(lclQuoteForm.getLclQuote().getLclFileNumber().getBusinessUnit())) {
//                        String brandPreferences = null != request.getParameter("brandPreferences") ? request.getParameter("brandPreferences") : "";
//                        if (brandPreferences.equalsIgnoreCase("None")) {
//                            new LclDwr().updateEconoOREculine("ECU", fileNumberId, user.getId().toString());
//                        }
//                    }
//                    refValue = lclQuoteForm.getHotCodes();
//                }
                if (CommonUtils.isNotEmpty(refValue)) {
                    if (CommonUtils.isNotEmpty(lclQuoteForm.getHotCodes())) {
                        hotCodeDAO.insertQuery(fileNumberId, refValue.toUpperCase(), user.getUserId());
                        _3pRef_type = _3PARTY_TYPE_HTC;
                        String val = lclQuoteForm.getHotCodes() == null ? lclQuoteForm.getHotCodes() : refValue.toUpperCase();
                        remarks = "Inserted - Hot Code#" + " " + val;
                        FORWARD_PAGE = "hoteCodes";
                    }
                }
            }
            request.setAttribute("lclHotCodeList", hotCodeDAO.getHotCodeList(Long.parseLong(fileNumberId)));
        }
        if (referenceName.equals("customerPo")) {
            _3pRef_type = _3PARTY_TYPE_CP;
            // referenceValue = lclQuoteForm.getCustomerPo();
            remarks = "Inserted - Customer PO#" + " " + lclQuoteForm.getCustomerPo();
            FORWARD_PAGE = "customerPo";
        }
        if (referenceName.equals("ncmNo")) {
            _3pRef_type = _3PARTY_TYPE_NCM;
            // referenceValue = lclQuoteForm.getNcmNo();
            remarks = "Inserted - NCM#" + " " + lclQuoteForm.getNcmNo();
            FORWARD_PAGE = "ncmNo";
        }
        if (referenceName.equals("tracking")) {
            //  referenceValue = lclQuoteForm.getTracking();
            _3pRef_type = _3PARTY_TYPE_TR;
            remarks = "Inserted - Tracking#" + " " + lclQuoteForm.getTracking();
            FORWARD_PAGE = "tracking";
        }
        if (referenceName.equals("wareHouseDoc")) {
            _3pRef_type = _3PARTY_TYPE_WH;
            // referenceValue = lclQuoteForm.getWareHouseDoc();
            remarks = "Inserted - Whse Doc# " + " " + lclQuoteForm.getWareHouseDoc();
            FORWARD_PAGE = "wareHouseDoc";
        }
        if (CommonUtils.isNotEmpty(refValue) && !"HTC".equalsIgnoreCase(_3pRef_type)) {
            lcl3pRefNoDAO.save3pRefNo(Long.parseLong(fileNumberId), _3pRef_type, refValue.toUpperCase());
        }
        if (CommonUtils.isNotEmpty(fileNumberId) && !"HTC".equalsIgnoreCase(_3pRef_type)) {
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(Long.parseLong(fileNumberId), _3pRef_type));
        }
        if (CommonUtils.isNotEmpty(remarks) && CommonUtils.isNotEmpty(fileNumberId)) {
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, remarks.toUpperCase(), getCurrentUser(request).getUserId());
        }
        return mapping.findForward(FORWARD_PAGE);
    }
    
    public ActionForward addLclQuoteHsCode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuoteHsCodeDAO lclQuoteHsCodeDAO = new LclQuoteHsCodeDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        String remarks = "", _3pRef_type = "";
        LclQuoteHsCode lclQuoteHsCode = new LclQuoteHsCode();
        if (CommonUtils.isNotEmpty(lclQuoteForm.getHsCode())) {
            lclQuoteHsCode.setCodes(lclQuoteForm.getHsCode());
            _3pRef_type = _3PARTY_TYPE_HSC;
            remarks = "Inserted - HS Code# " + " " + lclQuoteForm.getHsCode();
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getBookingHsCodeId())) {
            lclQuoteHsCode.setId(Long.parseLong(lclQuoteForm.getBookingHsCodeId()));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getBookingHsCode())) {
            lclQuoteHsCode.setCodes(lclQuoteForm.getBookingHsCode());
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getHsCodepiece())) {
            lclQuoteHsCode.setNoPieces(Integer.parseInt(lclQuoteForm.getHsCodepiece()));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getHsCodeWeightMetric())) {
            lclQuoteHsCode.setWeightMetric(new BigDecimal(lclQuoteForm.getHsCodeWeightMetric()));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getPackageTypeId())) {
            lclQuoteHsCode.setPackageType(new PackageType(Long.parseLong(lclQuoteForm.getPackageTypeId())));
        }
        lclQuoteHsCode.setLclFileNumber(new LclFileNumber(Long.parseLong(fileNumberId)));
        lclQuoteHsCode.setEnteredBy(getCurrentUser(request));
        lclQuoteHsCode.setModifiedBy(getCurrentUser(request));
        lclQuoteHsCode.setModifiedDatetime(new Date());
        lclQuoteHsCode.setEnteredDatetime(new Date());
        if (CommonUtils.isNotEmpty(lclQuoteHsCode.getCodes())) {
            lclQuoteHsCodeDAO.saveOrUpdate(lclQuoteHsCode);
        }
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("lclQuoteHsCodeList", lclQuoteHsCodeDAO.getHsCodeList(Long.parseLong(fileNumberId)));
        }
        if (CommonUtils.isNotEmpty(remarks) && CommonUtils.isNotEmpty(fileNumberId)) {
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, remarks, getCurrentUser(request).getUserId());
        }
        return mapping.findForward("hsCode");
    }
    
    public ActionForward deleteLclQuoteHsCode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteHsCodeDAO lclQuoteHsCodeDAO = new LclQuoteHsCodeDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        String lcl3pRefId = request.getParameter("lcl3pRefId");
        if (CommonUtils.isNotEmpty(lcl3pRefId)) {
            LclQuoteHsCode lclQuoteHsCode = lclQuoteHsCodeDAO.findById(Long.parseLong(lcl3pRefId));
            String hsCodeDelete = "";
            if (lclQuoteHsCode != null) {
                hsCodeDelete = "Deleted - HS Code#" + lclQuoteHsCode.getCodes();
                lclQuoteHsCodeDAO.delete(lclQuoteHsCode);
            }
            if (CommonUtils.isNotEmpty(hsCodeDelete)) {
                lclRemarksDAO.insertLclRemarks(lclQuoteHsCode.getLclFileNumber().getId(), REMARKS_QT_AUTO_NOTES, hsCodeDelete, getCurrentUser(request).getUserId());
            }
        }
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("lclQuoteHsCodeList", lclQuoteHsCodeDAO.getHsCodeList(Long.parseLong(fileNumberId)));
        }
        return mapping.findForward("hsCode");
    }
    
    public ActionForward deleteLcl3pReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String FORWARD_PAGE = "";
        String referenceName = lclQuoteForm.getNoteId();
        String fileNumberId = lclQuoteForm.getFileNumberId();
        String lcl3pRefId = lclQuoteForm.getLcl3pRefId();
        String refDeleteValue = "", _3pRefType = "";
        User user = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(lcl3pRefId) && !referenceName.equals("hotCodes")) {
            Lcl3pRefNo lcl3pRefNo = lcl3pRefNoDAO.findById(Long.parseLong(lcl3pRefId));
            if (lcl3pRefNo != null) {
                if (referenceName.equals("customerPo")) {
                    FORWARD_PAGE = "customerPo";
                    refDeleteValue = "Deleted - Customer PO# " + lcl3pRefNo.getReference();
                    _3pRefType = _3PARTY_TYPE_CP;
                }
                if (referenceName.equals("hsCode")) {
                    FORWARD_PAGE = "hsCode";
                    refDeleteValue = "Deleted - HS Code# " + lcl3pRefNo.getReference();
                    _3pRefType = "HS";
                }
                if (referenceName.equals("ncm")) {
                    FORWARD_PAGE = "ncm";
                    refDeleteValue = "Deleted - NCM# " + lcl3pRefNo.getReference();
                    _3pRefType = _3PARTY_TYPE_NCM;
                }
                if (referenceName.equals("tracking")) {
                    FORWARD_PAGE = "tracking";
                    refDeleteValue = "Deleted - Tracking# " + lcl3pRefNo.getReference();
                    _3pRefType = _3PARTY_TYPE_TR;
                }
                if (referenceName.equals("wareHouseDoc")) {
                    FORWARD_PAGE = "wareHouseDoc";
                    refDeleteValue = "Deleted - Whse Doc# " + lcl3pRefNo.getReference();
                    _3pRefType = _3PARTY_TYPE_WH;
                }
                if (referenceName.equals("aes")) {
                    FORWARD_PAGE = "aes";
                    refDeleteValue = "Deleted - AES# " + lcl3pRefNo.getReference();
                    _3pRefType = "AES";
                }
                lcl3pRefNoDAO.delete(lcl3pRefNo);
            }
        } else if (referenceName.equals("hotCodes") && CommonUtils.isNotEmpty(lcl3pRefId)) {
            LclQuoteHotCode hotCode = new LclQuoteHotCodeDAO().findById(Long.parseLong(lcl3pRefId));
            FORWARD_PAGE = "hoteCodes";
            refDeleteValue = "Deleted - HOT Code# " + hotCode.getCode();
            _3pRefType = _3PARTY_TYPE_HTC;
//            String isEculineHotCode = CommonUtils.isNotEmpty(hotCode.getCode())
//                    ? hotCode.getCode().substring(0, hotCode.getCode().indexOf("/")) : "";
//            if ("EBL".equalsIgnoreCase(isEculineHotCode)) {
//                String brandPreferences = null != request.getParameter("brandPreferences") ? request.getParameter("brandPreferences") : "";
//                if (brandPreferences.equalsIgnoreCase("None")) {
//                    String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
//                    companyCode = companyCode.equalsIgnoreCase("03") ? "ECI" : "OTI";
//                    lclQuoteForm.setBusinessUnit(companyCode);
//                    new LclDwr().updateEconoOREculine(companyCode, fileNumberId, user.getId().toString());
//                }
//            }
            new LclQuoteHotCodeDAO().delete(hotCode);
        }
        if (CommonUtils.isNotEmpty(refDeleteValue) && CommonUtils.isNotEmpty(fileNumberId)) {
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, refDeleteValue, getCurrentUser(request).getUserId());
        }
        
        if ("AES".equalsIgnoreCase(_3pRefType)) {
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3pRefAesList(Long.parseLong(fileNumberId)));
        } else if (CommonUtils.isNotEmpty(fileNumberId) && !"HTC".equalsIgnoreCase(_3pRefType)) {
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.getLclHscCodeListByType(fileNumberId, _3pRefType));
        } else {
            request.setAttribute("lclHotCodeList", new LclQuoteHotCodeDAO().getHotCodeList(Long.parseLong(fileNumberId)));
        }
        return mapping.findForward(FORWARD_PAGE);
    }
    
    public void setOtiNumber(LCLQuoteForm lclQuoteForm, Long fileId) throws Exception {
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        Lcl3pRefNo lcl3pRefNo = lcl3pRefNoDAO.getLcl3PRefDetails(fileId, LCL_3PREF_TYPE_OTI, "");
        if (null != lcl3pRefNo && CommonUtils.isNotEmpty(lcl3pRefNo.getReference())) {
            lclQuoteForm.setOtiNumber(lcl3pRefNo.getReference());
        }
    }
    
    public void addClient(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getTempClientCompany())) {
            lclQuoteForm.getLclQuote().getClientContact().setCompanyName(lclQuoteForm.getTempClientCompany().toUpperCase());
        }
        if (lclQuoteForm.getLclQuote().getClientContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_manual");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getTempClientCompany())) {
                lclContact.setCompanyName(lclQuoteForm.getTempClientCompany().toUpperCase());
            } else {
                lclContact.setCompanyName(lclQuoteForm.getLclQuote().getClientContact().getCompanyName().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getLclQuote().getClientContact().getContactName())) {
                lclContact.setContactName(lclQuoteForm.getLclQuote().getClientContact().getContactName().toUpperCase());
            } else {
                lclContact.setContactName("");
            }
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getClientContact(),
                    getCurrentUser(request), lclFileNumber, "qt_manual");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setClientContact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setClientContact(null);
        }
    }
    
    public void setClient(LCLQuoteForm lclQuoteForm, Long fileId,
            HttpServletRequest request) throws Exception {
        LclContact lclContact = new LCLContactDAO().getContact(fileId, "qt_manual");
        lclQuoteForm.setTempClientCompany((lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName()) ? lclContact.getCompanyName() : ""));
        request.setAttribute("lclQuoteForm", lclQuoteForm);
    }
    
    public void addSupplierName(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            User loginUser, Date todayDate) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getSupContactName())) {
            lclQuoteForm.getLclQuote().getSupContact().setCompanyName(lclQuoteForm.getSupContactName().toUpperCase());
        }
        if (lclQuoteForm.getLclQuote().getSupContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_supplier");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getSupContactName())) {
                lclContact.setCompanyName(lclQuoteForm.getSupContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclQuoteForm.getLclQuote().getSupContact().getCompanyName().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getLclQuote().getSupContact().getContactName())) {
                lclContact.setContactName(lclQuoteForm.getLclQuote().getSupContact().getContactName().toUpperCase());
            } else {
                lclContact.setContactName("");
            }
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getSupContact(),
                    loginUser, lclFileNumber, "qt_supplier");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setSupContact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setSupContact(null);
        }
    }
    
    public void setSupplierName(LCLQuoteForm lclQuoteForm, LclQuote lclQuote) throws Exception {
        lclQuoteForm.setSupContactName((lclQuote.getSupContact() != null && CommonUtils.isNotEmpty(lclQuote.getSupContact().getCompanyName()) ? lclQuote.getSupContact().getCompanyName() : ""));
    }
    
    public void addShipperName(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getShipContactName())) {
            lclQuoteForm.getLclQuote().getShipContact().setCompanyName(lclQuoteForm.getShipContactName().toUpperCase());
        }
        if (lclQuoteForm.getLclQuote().getShipContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_shipper");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getShipContactName())) {
                lclContact.setCompanyName(lclQuoteForm.getShipContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclQuoteForm.getLclQuote().getShipContact().getCompanyName().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getLclQuote().getShipContact().getContactName())) {
                lclContact.setContactName(lclQuoteForm.getLclQuote().getShipContact().getContactName().toUpperCase());
            } else {
                lclContact.setContactName("");
            }
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getShipContact(),
                    getCurrentUser(request), lclFileNumber, "qt_shipper");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setShipContact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setShipContact(null);
        }
    }
    
    public void setShipperName(LCLQuoteForm lclQuoteForm, Long fileId,
            HttpServletRequest request) throws Exception {
        LclContact lclContact = new LCLContactDAO().getContact(fileId, "qt_shipper");
        lclQuoteForm.setShipContactName((lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName()) ? lclContact.getCompanyName() : ""));
        request.setAttribute("lclQuoteForm", lclQuoteForm);
    }
    
    public void addConsigneeName(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getConsContactName())) {
            lclQuoteForm.getLclQuote().getConsContact().setCompanyName(lclQuoteForm.getConsContactName().toUpperCase());
        }
        if (lclQuoteForm.getLclQuote().getConsContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_consignee");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getConsContactName())) {
                lclContact.setCompanyName(lclQuoteForm.getConsContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclQuoteForm.getLclQuote().getConsContact().getCompanyName().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getLclQuote().getConsContact().getContactName())) {
                lclContact.setContactName(lclQuoteForm.getLclQuote().getConsContact().getContactName().toUpperCase());
            } else {
                lclContact.setContactName("");
            }
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getConsContact(),
                    getCurrentUser(request), lclFileNumber, "qt_consignee");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setConsContact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setConsContact(null);
        }
    }
    
    public void setConsigneeName(LCLQuoteForm lclQuoteForm, Long fileId,
            HttpServletRequest request) throws Exception {
        LclContact lclContact = new LCLContactDAO().getContact(fileId, "qt_consignee");
        lclQuoteForm.setConsContactName((lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName()) ? lclContact.getCompanyName() : ""));
        request.setAttribute("lclQuoteForm", lclQuoteForm);
    }
    
    public void addNotifyName(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getNotifyContactName())) {
            lclQuoteForm.getLclQuote().getNotyContact().setCompanyName(lclQuoteForm.getNotifyContactName().toUpperCase());
        }
        if (lclQuoteForm.getLclQuote().getNotyContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_notify");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getNotifyContactName())) {
                lclContact.setCompanyName(lclQuoteForm.getNotifyContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclQuoteForm.getLclQuote().getNotyContact().getCompanyName().toUpperCase());
            }
            lclContact.setContactName("");
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getNotyContact(),
                    getCurrentUser(request), lclFileNumber, "qt_notify");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setNotyContact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setNotyContact(null);
        }
    }
    
    public void setNotifyName(LCLQuoteForm lclQuoteForm, Long fileId,
            HttpServletRequest request) throws Exception {
        LclContact lclContact = new LCLContactDAO().getContact(fileId, "qt_notify");
        lclQuoteForm.setNotifyContactName((lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName()) ? lclContact.getCompanyName() : ""));
        request.setAttribute("lclQuoteForm", lclQuoteForm);
    }
    
    public void addNotify2Name(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            User loginUser, Date todayDate) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getNotify2ContactName())) {
            lclQuoteForm.getLclQuote().getNotify2Contact().setCompanyName(lclQuoteForm.getNotify2ContactName().toUpperCase());
        }
        if (lclQuoteForm.getLclQuote().getNotify2Contact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_Notify2");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getNotify2ContactName())) {
                lclContact.setCompanyName(lclQuoteForm.getNotify2ContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclQuoteForm.getLclQuote().getNotify2Contact().getCompanyName().toUpperCase());
            }
            lclContact.setContactName("");
            if (lclQuoteForm.getNotify2AcctNo() != null) {
                lclContact.setTradingPartner(new TradingPartnerDAO().findById(lclQuoteForm.getNotify2AcctNo()));
            } else {
                lclContact.setTradingPartner(null);
            }
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getNotify2Contact(),
                    loginUser, lclFileNumber, "qt_Notify2");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setNotify2Contact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setNotify2Contact(null);
        }
    }
    
    public void setNotify2Name(LCLQuoteForm lclQuoteForm, LclQuote lclQuote) throws Exception {
        if (null != lclQuote.getNotify2Contact() && CommonUtils.isNotEmpty(lclQuote.getNotify2Contact().getCompanyName())) {
            lclQuoteForm.setNotify2ContactName(lclQuote.getNotify2Contact().getCompanyName().toUpperCase());
        }
    }
    
    public void addForwarderName(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request) throws Exception {
        if (lclQuoteForm.getLclQuote().getFwdContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "qt_forwarder");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            lclContact.setCompanyName(lclQuoteForm.getLclQuote().getFwdContact().getCompanyName().toUpperCase());
            lclContact.setContactName(lclQuoteForm.getLclQuote().getFwdContact().getContactName().toUpperCase());
            lclContact = new LclQuoteUtils().setContactDataForQuote(lclContact, lclQuoteForm.getLclQuote().getFwdContact(),
                    getCurrentUser(request), lclFileNumber, "qt_forwarder");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclQuoteForm.getLclQuote().setFwdContact(lclContact);
        } else {
            lclQuoteForm.getLclQuote().setFwdContact(null);
        }
    }
    
    public void addDoorCityZip(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            Date now, User loginUser) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getDoorOriginCityZip())) {
            LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
            RefTerminal terminal = LCL_IMPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())
                    ? loginUser.getImportTerminal() : loginUser.getTerminal();
            LclQuotePad lclQuotePad = lclQuotePadDAO.createInstance(lclFileNumber, loginUser, now, terminal);
            lclQuotePad.setPickUpCity(lclQuoteForm.getDoorOriginCityZip());
            lclQuotePadDAO.saveOrUpdate(lclQuotePad);
        }
    }
    
    public void setDoorCityZip(LCLQuoteForm lclQuoteForm, LclQuote lclQuote) throws Exception {
        List<LclQuotePad> lclQuotePadList = lclQuote.getLclFileNumber().getLclQuotePadList();
        if (null != lclQuotePadList && !lclQuotePadList.isEmpty()) {
            LclQuotePad lclQuotePad = lclQuote.getLclFileNumber().getLclQuotePadList().get(0);
            lclQuoteForm.setDoorOriginCityZip(lclQuotePad.getPickUpCity());
        }
    }
    
    public ActionForward copyQuote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String FRWD_PAGE = null;
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        lclQuoteForm.setCopyFnVal("Y");
        String moduleName = "";
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        String fileId = request.getParameter("fileNumberId");
        if (null == fileId) {
            String fileNo = request.getParameter("fileNumber");
            fileId = String.valueOf(fileDao.getFileIdByFileNumber(fileNo));
        }
        if (CommonUtils.isNotEmpty(fileId)) {
            LclQuote lclQuote = new LCLQuoteDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            lclQuoteForm.setOriginalFileNo(lclQuote.getLclFileNumber().getFileNumber());
            LclQuotePad lclQuotePad = new LclQuotePadDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (lclQuotePad != null) {
                if (lclQuotePad.getPickupContact() != null) {
                    lclQuoteForm.setDoorOriginCityZip(CommonUtils.isEmpty(lclQuotePad.getPickupContact().getCity()) ? lclQuotePad.getPickUpCity() : lclQuotePad.getPickupContact().getCity());
                    lclQuoteForm.setPcontactName(lclQuotePad.getPickupContact().getContactName());
                    lclQuoteForm.setShipSupplier(lclQuotePad.getPickupContact().getCompanyName());
                    lclQuoteForm.setPemail1(lclQuotePad.getPickupContact().getEmail1());
                    lclQuoteForm.setPphone1(lclQuotePad.getPickupContact().getPhone1());
                    lclQuoteForm.setPfax1(lclQuotePad.getPickupContact().getFax1());
                    lclQuoteForm.setPaddress(lclQuotePad.getPickupContact().getAddress());
                }
                if (lclQuotePad.getDeliveryContact() != null) {
                    lclQuoteForm.setWhseState(lclQuotePad.getDeliveryContact().getState());
                    lclQuoteForm.setWhsecompanyName(lclQuotePad.getDeliveryContact().getCompanyName());
                    lclQuoteForm.setWhseZip(lclQuotePad.getDeliveryContact().getZip());
                    lclQuoteForm.setWhseCity(lclQuotePad.getDeliveryContact().getCity());
                    lclQuoteForm.setWhsePhone(lclQuotePad.getDeliveryContact().getPhone1());
                    lclQuoteForm.setWhseAddress(lclQuotePad.getDeliveryContact().getAddress());
                }
                if (lclQuotePad.getLclQuoteAc().getApAmount() != null) {
                    lclQuoteForm.setPickupCost(lclQuotePad.getLclQuoteAc().getApAmount().toString());
                }
                if (lclQuotePad.getLclQuoteAc().getArAmount() != null) {
                    lclQuoteForm.setChargeAmount(lclQuotePad.getLclQuoteAc().getArAmount().toString());
                }
                lclQuoteForm.setScacCode(lclQuotePad.getScac());
                lclQuoteForm.setPickupInstructions(lclQuotePad.getPickupInstructions());
                lclQuoteForm.setPickupReferenceNo(lclQuotePad.getPickupReferenceNo());
                lclQuoteForm.setPcommodityDesc(lclQuotePad.getCommodityDesc());
                lclQuoteForm.setPickupReadyNote(lclQuotePad.getPickupReadyNote());
                lclQuoteForm.setPickupHours(lclQuotePad.getPickupHours());
                lclQuoteForm.setTermsOfService(lclQuotePad.getTermsOfService());
                lclQuoteForm.setPickupCutoffDate(DateUtils.formatDate(lclQuotePad.getPickupCutoffDate(), "dd-MMM-yyyy"));
                lclQuoteForm.setPickupReadyDate(DateUtils.formatDate(lclQuotePad.getPickupReadyDate(), "dd-MMM-yyyy"));
            }
            LclQuote copyQt = (LclQuote) BeanUtils.cloneBean(lclQuote);
            copyQt.setPooWhseLrdt(null);
            copyQt.setFdEta(null);
            copyQt.setFileNumberId(null);
            copyQt.setLclFileNumber(null);
            copyQt.setDefaultAgent(true);
            copyQt.setSpotRate(false);
            copyQt.setInsurance(false);
            copyQt.setValueOfGoods(null);
            copyQt.setCifValue(null);
            copyQt.setOverShortDamaged(false);
//            duplicateQuote.setEnteredBy(null);
//            duplicateQuote.setModifiedBy(null);
            copyQt.setRelayOverride(false);
            copyQt.setQuoteComplete(false);
            copyQt.setPooWhseLrdt(null);
            copyQt.setFdEta(null);
            lclQuoteForm.setRateType("R");
            lclQuoteForm.setFileNumberId(null);
            new LclQuotePieceDAO().getSession().clear();
            exportQuoteUtils.setPoaandCreditStatusValues(lclQuoteForm, copyQt, moduleName, request);//set Values POA and Credit Status for all Vendors.Client,Shipper,Forwarder,Notify
            List<LclQuotePiece> lclQuotePieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            List<LclQuotePiece> lclQuotePieceNewList = new ArrayList<LclQuotePiece>();
            for (LclQuotePiece lqp : lclQuotePieceList) {
                LclQuotePiece lqpnew = new LclQuotePiece();
                PropertyUtils.copyProperties(lqpnew, lqp);
                lqpnew.setCommNo(lqp.getCommodityType().getCode());
                lqpnew.setCommName(lqp.getCommodityType().getDescEn());
                lqpnew.setPkgName(lqp.getPackageType().getDescription());
                lqpnew.setPkgNo(lqp.getPackageType().getId());
                lqpnew.setId(null);
                
                lqpnew.setLclQuoteHazmatList(new LclQuoteHazmatDAO().findByFileAndCommodityList(Long.parseLong(fileId), lqp.getId()));
                lqpnew.setLclQuoteAcList(new LclQuoteAcDAO().findByFileAndCommodityList(Long.parseLong(fileId), lqp.getId()));
                lqpnew.setLclQuotePieceWhseList(new LclQuotePieceWhseDAO().findByProperty("lclQuotePiece.id", lqp.getId()));
                lqpnew.setLclQuotePieceDetailList(new QuoteCommodityDetailsDAO().findByProperty("quotePiece.id", lqp.getId()));
                lclQuotePieceNewList.add(lqpnew);
            }
            lclSession.setQuoteCommodityList(lclQuotePieceNewList);
            request.setAttribute("lclCommodityList", lclQuotePieceNewList);
            List<LclQuoteAc> lclQuoteAcList = null;
            if (lclQuote.getQuoteType().equalsIgnoreCase("E")) {
                String portRemarks[] = new LclDwr().defaultDestinationRemarks(copyQt.getFinalDestination().getUnLocationCode(),
                        copyQt.getPortOfDestination().getUnLocationCode());
                if (portRemarks[0] != null && !portRemarks[0].equals("")) {
                    lclQuoteForm.setSpecialRemarks(portRemarks[0]);
                }
                if (portRemarks[1] != null && !portRemarks[1].equals("")) {
                    lclQuoteForm.setInternalRemarks(portRemarks[1]);
                }
                if (portRemarks[2] != null && !portRemarks[2].equals("")) {
                    lclQuoteForm.setPortGriRemarks(portRemarks[2]);
                }
                if (portRemarks[3] != null && !portRemarks[3].equals("")) {
                    lclQuoteForm.setSpecialRemarksPod(portRemarks[3]);
                }
                if (portRemarks[4] != null && !portRemarks[4].equals("")) {
                    lclQuoteForm.setInternalRemarksPod(portRemarks[4]);
                }
                if (portRemarks[5] != null && !portRemarks[5].equals("")) {
                    lclQuoteForm.setPortGriRemarksPod(portRemarks[5]);
                }
                lclQuoteForm.setWhsCode((null != copyQt.getPooWhseContact() && null != copyQt.getPooWhseContact().getWarehouse())
                        ? copyQt.getPooWhseContact().getWarehouse().getWarehouseNo() : "");
//                if (lclQuote.getPortOfOrigin() != null && lclQuote.getPortOfLoading() != null && lclQuote.getPortOfDestination() != null && lclQuote.getFinalDestination() != null) {
//                    List<LclBookingVoyageBean> voyageList = new LclBookingPlanDAO().getBookingVoyageList(lclQuote.getPortOfOrigin().getId(),
//                            lclQuote.getPortOfLoading().getId(), lclQuote.getPortOfDestination().getId(), lclQuote.getFinalDestination().getId(), "V");
//                    Collections.sort(voyageList, new LclVoyageComparator());
//                    request.setAttribute("voyageList", voyageList);
//                    request.setAttribute("voyageAction", true);
//                }
                LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                String radioValue = request.getParameter("radioValue");
                String pickupReadyDate = "";
                String fromZip = "";
                String rateType = "R".equalsIgnoreCase(lclQuoteForm.getRateType()) ? "Y" : lclQuoteForm.getRateType();
                if (lclQuoteForm.getDoorOriginCityZip() != null && !lclQuoteForm.getDoorOriginCityZip().trim().equals("")) {
                    String[] zip = lclQuoteForm.getDoorOriginCityZip().split("-");
                    fromZip = zip[0];
                }
                if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupReadyDate())) {
                    pickupReadyDate = lclQuoteForm.getPickupReadyDate();
                }
                exportQuoteUtils.setTrmandEciPortCode(lclQuoteForm, lclQuote, new LclUtils());
                lclQuoteForm.setModuleName(LCL_EXPORT);
                lclQuotationChargesCalculation.calculateRates(lclQuote.getPortOfOrigin().getUnLocationCode(), lclQuote.getFinalDestination().getUnLocationCode(),
                        lclQuote.getPortOfLoading().getUnLocationCode(),
                        lclQuote.getPortOfDestination().getUnLocationCode(), null, lclQuotePieceNewList, getCurrentUser(request), lclQuoteForm.getPooDoor(),
                        null, lclQuoteForm.getLclQuote().getValueOfGoods(), rateType, "C", null, pickupReadyDate,
                        fromZip, null, lclQuoteForm.getCalcHeavy(), lclQuoteForm.getDeliveryMetro(), lclQuoteForm.getPcBoth(), null, radioValue, request);
                //   request.setAttribute("ofratebasis", lclQuotationChargesCalculation.getOfratebasis());
                // request.setAttribute("stdchgratebasis", lclQuotationChargesCalculation.getStdchgratebasis());
                request.setAttribute("highVolumeMessage", lclQuotationChargesCalculation.getHighVolumeMessage());
                lclQuoteAcList = lclQuotationChargesCalculation.getQuoteAcList();
                exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePieceNewList, lclQuoteForm.getFdEngmet());
                exportQuoteUtils.setRolledUpChargesForQuote(lclQuoteAcList, request, null, null,
                        lclQuotePieceNewList, lclQuote.getBillingType(), lclQuoteForm.getFdEngmet(), "No");
                LclBookingExport lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclQuote.getFileNumberId());
                request.setAttribute("lclBookingExport", lclBookingExport);
                FRWD_PAGE = LCL_QUOTE_EXPORT;
            } else {
                ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
                lclQuoteForm.setModuleName(LCL_IMPORT);
                importQuoteUtils.agentCount(lclQuote.getPortOfLoading().getUnLocationCode(), request);
//                User user = (User) request.getSession().getAttribute("loginuser");
//                String agentNo = "";
//                if (CommonFunctions.isNotNull(lclQuote.getAgentAcct()) && CommonFunctions.isNotNull(lclQuote.getAgentAcct().getAccountno())) {
//                    agentNo = lclQuote.getAgentAcct().getAccountno();
//                }
//                lclQuoteImportChargeCalc.ImportRateCalculation(lclQuote.getPortOfOrigin().getUnLocationCode(), lclQuote.getPortOfLoading().getUnLocationCode(), lclQuote.getPortOfDestination().getUnLocationCode(), lclQuote.getFinalDestination().getUnLocationCode(),
//                        false, lclQuote.getBillingType(), lclQuote.getBillToParty(), agentNo, null, lclQuotePieceNewList, request, user);
//                lclQuoteAcList = lclQuoteImportChargeCalc.getQuoteAcList();
                importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePieceNewList);
                // lclImportUtils.setImpRolledUpChargesForQuote(lclQuoteAcList, request, Long.parseLong(fileNumberId), lclQuotePieceNewList, lclQuote.getBillingType());
                FRWD_PAGE = LCL_QUOTE_IMPORT;
            }
            lclSession.setQuoteAcList(lclQuoteAcList);
            new LclQuoteUtils().setPolPodValues(copyQt, request);
            //*********Code for deleting previous booking Session***************
            ProcessInfoBC processInfoBc = new ProcessInfoBC();
            ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
            User user = getCurrentUser(request);
            ProcessInfo processInfo = processInfoDAO.findByFileNoAndUserId(lclQuote.getLclFileNumber().getFileNumber(), user.getUserId());
            if (processInfo != null) {
                processInfoBc.delete((processInfo.getId()).toString());
            }
            //******************************************************************
            session.setAttribute("lclSession", lclSession);
            lclQuoteForm.setLclQuote(copyQt);
            request.setAttribute("error", false);
            request.setAttribute("lclQuote", copyQt);
            request.setAttribute("lclQuoteForm", lclQuoteForm);
        }
        return mapping.findForward(FRWD_PAGE);
    }
    
    public void addLclQuotePadDetails(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request, LclBooking lclBooking) throws Exception {
        LclQuotePad lclQuotePad = null == new LclQuotePadDAO().executeUniqueQuery("from LclQuotePad where lclFileNumber.id= " + lclFileNumber.getId() + "") ? new LclQuotePad() : new LclQuotePadDAO().executeUniqueQuery("from LclQuotePad where lclFileNumber.id= " + lclFileNumber.getId() + "");
        if (CommonUtils.isNotEmpty(lclQuotePad.getId())) {
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPcontactName())) {
                lclQuotePad.getPickupContact().setContactName(lclQuoteForm.getPcontactName());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getShipSupplier())) {
                lclQuotePad.getPickupContact().setCompanyName(lclQuoteForm.getShipSupplier());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPemail1())) {
                lclQuotePad.getPickupContact().setEmail1(lclQuoteForm.getPemail1());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPphone1())) {
                lclQuotePad.getPickupContact().setPhone1(lclQuoteForm.getPphone1());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPfax1())) {
                lclQuotePad.getPickupContact().setFax1(lclQuoteForm.getPfax1());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPaddress())) {
                lclQuotePad.getPickupContact().setAddress(lclQuoteForm.getPaddress());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getWhseState())) {
                lclQuotePad.getDeliveryContact().setState(lclQuoteForm.getWhseState());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getWhsecompanyName())) {
                lclQuotePad.getDeliveryContact().setCompanyName(lclQuoteForm.getWhsecompanyName());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getWhseZip())) {
                lclQuotePad.getDeliveryContact().setZip(lclQuoteForm.getWhseZip());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getWhseCity())) {
                lclQuotePad.getDeliveryContact().setCity(lclQuoteForm.getWhseCity());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getWhsePhone())) {
                lclQuotePad.getDeliveryContact().setPhone1(lclQuoteForm.getWhsePhone());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getWhseAddress())) {
                lclQuotePad.getDeliveryContact().setAddress(lclQuoteForm.getWhseAddress());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupCost())) {
                lclQuotePad.getLclQuoteAc().setApAmount(new BigDecimal(lclQuoteForm.getPickupCost()));
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupInstructions())) {
                lclQuotePad.setPickupInstructions(lclQuoteForm.getPickupInstructions());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupReferenceNo())) {
                lclQuotePad.setPickupReferenceNo(lclQuoteForm.getPickupReferenceNo());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPcommodityDesc())) {
                lclQuotePad.setCommodityDesc(lclQuoteForm.getPcommodityDesc());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupReadyNote())) {
                lclQuotePad.setPickupReadyNote(lclQuoteForm.getPickupReadyNote());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupHours())) {
                lclQuotePad.setPickupHours(lclQuoteForm.getPickupHours());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getTermsOfService())) {
                lclQuotePad.setTermsOfService(lclQuoteForm.getTermsOfService());
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupCutoffDate())) {
                lclQuotePad.setPickupCutoffDate(DateUtils.parseDate(lclQuoteForm.getPickupCutoffDate(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupReadyDate())) {
                lclQuotePad.setPickupReadyDate(DateUtils.parseDate(lclQuoteForm.getPickupReadyDate(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getChargeAmount())) {
                lclQuotePad.getLclQuoteAc().setArAmount(new BigDecimal(lclQuoteForm.getChargeAmount()));
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getScacCode())) {
                lclQuotePad.setScac(lclQuoteForm.getScacCode());
            }
            if (CommonUtils.isNotEmpty(lclFileNumber.getId()) && CommonUtils.isNotEmpty(lclQuoteForm.getChargeAmount())) {
                TradingPartner spRefNumber = new TradingPartnerDAO().findById("CTSFRE0004");
                LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
                LclQuoteAc lclQuoteAc = null != lclQuoteAcDAO.getLclCostByChargeCode(lclFileNumber.getId(), "0012") ? lclQuoteAcDAO.getLclCostByChargeCode(lclFileNumber.getId(), "0012") : new LclQuoteAc();
                lclQuoteAc.setLclFileNumber(lclFileNumber);
                lclQuoteAc.setArAmount(new BigDecimal(lclQuoteForm.getChargeAmount()));
                GlMapping gp = new GlMappingDAO().findByBlueScreenChargeCode("0012", "LCLE", "AR");
                lclQuoteAc.setArglMapping(gp);
                lclQuoteAc.setSupAcct(spRefNumber);
                lclQuoteAc.setTransDatetime(new Date());
                lclQuoteAc.setManualEntry(false);
                lclQuoteAc.setRatePerUnitUom("FL");
                lclQuoteAc.setRateUom("I");
                lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                lclQuoteAc.setEnteredBy(getCurrentUser(request));
                lclQuoteAc.setModifiedBy(getCurrentUser(request));
                lclQuoteAc.setEnteredDatetime(new Date());
                lclQuoteAc.setModifiedDatetime(new Date());
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setPrintOnBl(true);
                lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
            }
            lclQuotePad.setLclFileNumber(lclFileNumber);
            lclQuotePad.setModifiedBy(getCurrentUser(request));
            lclQuotePad.setEnteredBy(getCurrentUser(request));
            lclQuotePad.setEnteredDatetime(new Date());
            lclQuotePad.setModifiedDatetime(new Date());
            new LclQuotePadDAO().saveOrUpdate(lclQuotePad);
        }
    }
    
    public ActionForward refreshAgent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        if (LCL_IMPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
            request.setAttribute("podUnlocationcode", lclQuoteForm.getUnlocationCode());
        } else {
            request.setAttribute("podUnlocationcode", lclQuoteForm.getPodUnlocationcode());
        }
        request.setAttribute("lclQuoteForm", lclQuoteForm);
        return mapping.findForward("refreshQuoteAgentName");
    }
    
    public ActionForward refreshImpOriginAgent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        String polUnlocationCode = lclQuoteForm.getPolUnlocationcode();
        new ImportQuoteUtils().agentCount(polUnlocationCode, request);
        request.setAttribute("polUnlocationcode", lclQuoteForm.getPolUnlocationcode());
        return mapping.findForward("refreshOriginAgent");
    }
    
    public ActionForward displayBookingVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        String fileNumber = request.getParameter("fileNumber");
        String prevSailing = request.getParameter("prevSailing");
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(fileNumberId));
            new ExportQuoteUtils().setUpcomingSailings(lclQuote, "", request, prevSailing);
//            if (lclQuote.getPortOfLoading() != null && lclQuote.getPortOfDestination() != null) {
//                exportQuoteUtils.setUpcomingSailing(lclQuote.getPortOfOrigin().getId(),
//                        lclQuote.getPortOfLoading().getId(), lclQuote.getPortOfDestination().getId(),
//                        lclQuote.getFinalDestination().getId(), lclQuoteForm.getRelayOverride(), request);//Upcomong Sailings List
//            }
            //  request.setAttribute("voyageAction", request.getParameter("voyageAction"));
            request.setAttribute("fileNumberId", fileNumberId);
            request.setAttribute("unknownDest", lclQuote.getNonRated());
            request.setAttribute("fileNumber", fileNumber);
            request.setAttribute("prevSailing", prevSailing);
        }
        String forwardMessage = "";
        List<Object> upComingSailing = (List<Object>) request.getAttribute("voyageList");
        if (null != upComingSailing && upComingSailing.isEmpty()) {
            this.convertToBkg(mapping, form, request, response);
        } else {
            forwardMessage = LCL_QUOTE_VOYAGE;
        }
        return mapping.findForward(forwardMessage);
    }
    
    public ActionForward calculateCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            List lclQuotePiecesList = null;
            String radioValue = request.getParameter("radioValue");
            if (CommonUtils.isNotEmpty(lclQuoteForm.getFileNumberId())) {
                LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
                LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(lclQuoteForm.getFileNumberId()));
                lclQuote.setDeliveryMetro(lclQuoteForm.getDeliveryMetro());
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                if (CommonUtils.isNotEmpty(lclQuoteForm.getOrigin())) {
                    lclQuote.setPortOfOrigin(unLocationDAO.getUnlocation(lclQuoteForm.getOrigin()));
                } else {
                    lclQuote.setPortOfOrigin(null);
                }
                if (CommonUtils.isNotEmpty(request.getParameter("pol"))) {
                    lclQuote.setPortOfLoading(unLocationDAO.getUnlocation(request.getParameter("pol")));
                }
                if (CommonUtils.isNotEmpty(request.getParameter("pod"))) {
                    lclQuote.setPortOfDestination(unLocationDAO.getUnlocation(request.getParameter("pod")));
                }
                if (CommonUtils.isNotEmpty(lclQuoteForm.getDestination())) {
                    lclQuote.setFinalDestination(unLocationDAO.getUnlocation(lclQuoteForm.getDestination()));
                }
                lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
                if (CommonUtils.isNotEmpty(lclQuoteForm.getOrigin()) && CommonUtils.isNotEmpty(lclQuoteForm.getDestination())
                        && CommonUtils.isNotEmpty(lclQuotePiecesList)) {
                    LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                    if (lclQuoteForm.getRateType() != null && !lclQuoteForm.getRateType().trim().equals("")) {
                        String rateType = "R".equalsIgnoreCase(lclQuoteForm.getRateType()) ? "Y" : lclQuoteForm.getRateType();
                        String fromZip = "";
                        if (lclQuoteForm.getDoorOriginCityZip() != null && !lclQuoteForm.getDoorOriginCityZip().trim().equals("")) {
                            String[] zip = lclQuoteForm.getDoorOriginCityZip().split("-");
                            fromZip = zip[0];
                        }
                        String pickupReadyDate = lclQuoteDAO.getPickupReadyDate(Long.parseLong(lclQuoteForm.getFileNumberId()));
                        lclQuotationChargesCalculation.calculateRates(lclQuoteForm.getOrigin(), lclQuoteForm.getDestination(), lclQuoteForm.getPolUnlocationcode(),
                                lclQuoteForm.getPodUnlocationcode(), Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuotePiecesList, getCurrentUser(request), lclQuoteForm.getPooDoor(),
                                lclQuoteForm.getInsurance(), lclQuoteForm.getLclQuote().getValueOfGoods(), rateType, "C", null, pickupReadyDate,
                                fromZip, null, lclQuoteForm.getCalcHeavy(), lclQuoteForm.getDeliveryMetro(), lclQuoteForm.getPcBoth(), null, radioValue, request);
                        //request.setAttribute("ofratebasis", lclQuotationChargesCalculation.getOfratebasis());
                        //request.setAttribute("stdchgratebasis", lclQuotationChargesCalculation.getStdchgratebasis());
                        request.setAttribute("highVolumeMessage", lclQuotationChargesCalculation.getHighVolumeMessage());
                        LclRemarks lclRemarks = null == new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclQuote.getLclFileNumber().getId() + " AND type='AutoRates' ") ? new LclRemarks() : new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclQuote.getLclFileNumber().getId() + " AND type='AutoRates'");
                        if (lclQuotationChargesCalculation.getHighVolumeMessage() != null && !"".equalsIgnoreCase(lclQuotationChargesCalculation.getHighVolumeMessage())) {
                            lclRemarks.setLclFileNumber(lclQuote.getLclFileNumber());
                            lclRemarks.setType("AutoRates");
                            lclRemarks.setRemarks(lclQuotationChargesCalculation.getHighVolumeMessage());
                            lclRemarks.setEnteredBy(getCurrentUser(request));
                            lclRemarks.setEnteredDatetime(new Date());
                            lclRemarks.setModifiedBy(getCurrentUser(request));
                            lclRemarks.setModifiedDatetime(new Date());
                            new LclRemarksDAO().saveOrUpdate(lclRemarks);
                        }
                    }
                    request.setAttribute("lclQuote", lclQuote);
                    HttpSession session = request.getSession();
                    LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                    request.setAttribute("lclSession", lclSession);
                    List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), LCL_EXPORT);
                    exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePiecesList, lclQuoteForm.getFdEngmet());
                    exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclquoteacdao, lclQuotePiecesList, lclQuoteForm.getBillingType(), lclQuoteForm.getFdEngmet(), "No");
                }
            }
        } catch (Exception e) {
            log.info("Error in calculateCharges method,  on " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }
    
    public ActionForward calculateImportCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
            LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            User user = getCurrentUser(request);
            lclQuoteForm.setEnums(lclQuoteForm.getTransShipMent());
            String polUnCode = "";
            String podUnCode = "";
            String fdUnCode = "";
            String originCode = "";
            List<LclQuoteAc> chargeList = null;
            LclQuote lclQuote1 = lclQuoteDAO.findById(Long.parseLong(lclQuoteForm.getFileNumberId()));
            if (CommonUtils.isNotEmpty(lclQuoteForm.getDestination())) {
                lclQuote1.setFinalDestination(unLocationDAO.getUnlocation(lclQuoteForm.getDestination()));
            }
            if (CommonUtils.isNotEmpty(lclQuoteForm.getOrigin())) {
                lclQuote1.setPortOfOrigin(unLocationDAO.getUnlocation(lclQuoteForm.getOrigin()));
            } else {
                lclQuote1.setPortOfOrigin(null);
            }
            if (CommonUtils.isNotEmpty(request.getParameter("pol"))) {
                lclQuote1.setPortOfLoading(unLocationDAO.getUnlocation(request.getParameter("pol")));
            }
            if (CommonUtils.isNotEmpty(request.getParameter("pod"))) {
                lclQuote1.setPortOfDestination(unLocationDAO.getUnlocation(request.getParameter("pod")));
            }
            lclQuoteDAO.saveOrUpdate(lclQuote1);
            lclQuoteDAO.getCurrentSession().flush();
            lclQuoteDAO.getCurrentSession().clear();
            //LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(lclQuoteForm.getFileNumberId()));
            if (CommonFunctions.isNotNull(lclQuote1)) {
                fdUnCode = lclQuoteForm.getDestination();
                if (CommonFunctions.isNotNull(lclQuote1.getPortOfLoading()) && CommonFunctions.isNotNull(lclQuote1.getPortOfLoading().getUnLocationName())) {
                    polUnCode = lclQuote1.getPortOfLoading().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclQuote1.getPortOfDestination()) && CommonFunctions.isNotNull(lclQuote1.getPortOfDestination().getUnLocationName())) {
                    podUnCode = lclQuote1.getPortOfDestination().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclQuote1.getPortOfOrigin()) && CommonFunctions.isNotNull(lclQuote1.getPortOfOrigin().getUnLocationName())) {
                    originCode = lclQuote1.getPortOfOrigin().getUnLocationCode();
                }
            }
            List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
            if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
                lclQuoteImportChargeCalc.ImportRateCalculation(originCode, polUnCode, podUnCode, fdUnCode, lclQuoteForm.getTransShipMent(),
                        lclQuote1.getBillingType(), lclQuote1.getBillToParty(), Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuotePiecesList, user, request);
                chargeList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), LCL_IMPORT);
            }
            importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuotePiecesList, lclQuote1.getBillingType());
            importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePiecesList);
            request.setAttribute("lclQuote", lclQuote1);
        } catch (Exception e) {
            log.info("Error in LCL ImportcalculateCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }
    
    public ActionForward displayemptyVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {//remove
        request.setAttribute("voyageList", null);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }
    
    public ActionForward saveRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        Date now = new Date();
        User loginUser = getCurrentUser(request);
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        synchronized (LclQuoteAction.class) {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<RoutingOptionsBean> routingOptionsList = lclSession.getRoutingOptionsList();
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
            
            if (lclQuoteForm.getIndex()
                    != null && !lclQuoteForm.getIndex().trim().equals("") && routingOptionsList != null && routingOptionsList.size() > 0) {
                int index = Integer.parseInt(lclQuoteForm.getIndex());
                String fileNumberId = lclQuoteForm.getFileNumberId();
                LclFileNumber lclFileNumber = null;
                if (fileNumberId == null || fileNumberId.trim().equals("")) {
                    LclFileNumberThread thread = new LclFileNumberThread();
                    String fileNumber = thread.getFileNumber();
                    lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "Q");
                } else {
                    lclFileNumber = lclFileNumberDAO.findById(Long.parseLong(fileNumberId));
                }
                if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                    lclFileNumberDAO.getSession().getTransaction().begin();
                }
                lclQuoteForm.getLclQuote().setEnteredBy(loginUser);
                lclQuoteForm.getLclQuote().setModifiedBy(loginUser);
                lclQuoteForm.getLclQuote().setEnteredDatetime(now);
                lclQuoteForm.getLclQuote().setModifiedDatetime(now);
                lclQuoteForm.getLclQuote().setLclFileNumber(lclFileNumber);
                lclQuoteForm.getLclQuote().setFileNumberId(lclFileNumber.getId());
                RoutingOptionsBean routingOptionsBean = routingOptionsList.get(index);
                //PickUpCost IsEmpty
                if (routingOptionsBean.getPickupCost() == null) {
                    BigDecimal weight = new BigDecimal(0.000);
                    BigDecimal measure = new BigDecimal(0.000);
                    List<LclQuotePiece> lclQuotePiecesList = null;
                    if (fileNumberId != null && !fileNumberId.trim().equals("")) {
                        lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
                    } else {
                        if (lclSession != null && lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0) {
                            lclQuotePiecesList = lclSession.getQuoteCommodityList();
                        }
                    }
                    if (lclQuotePiecesList != null) {
                        for (LclQuotePiece lbp : lclQuotePiecesList) {
                            if (lbp.getActualWeightImperial() != null) {
                                weight = weight.add(lbp.getActualWeightImperial());
                            } else if (lbp.getBookedWeightImperial() != null) {
                                weight = weight.add(lbp.getBookedWeightImperial());
                            }
                            if (lbp.getActualVolumeImperial() != null) {
                                measure = measure.add(lbp.getActualVolumeImperial());
                            } else if (lbp.getBookedVolumeImperial() != null) {
                                measure = measure.add(lbp.getBookedVolumeImperial());
                            }
                        }
                        
                        String realPath = session.getServletContext().getRealPath("/xml/");
                        String fileName = "ctsresponse" + session.getId() + ".xml";
                        CallCTSWebServices ctsweb = new CallCTSWebServices();
                        lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, routingOptionsBean.getFromZip(),
                                routingOptionsBean.getToZip(), routingOptionsBean.getSailDate(), "" + weight, "" + measure,
                                null, "CARRIER_COST", "Exports");
                        List<Carrier> carrierCostList = lclSession.getCarrierCostList();
                        BigDecimal pickUpCost = new BigDecimal(0.000);
                        if (CommonUtils.isNotEmpty(carrierCostList) && !carrierCostList.isEmpty()) {
                            for (int j = 0; j < carrierCostList.size(); j++) {
                                Carrier carrier = carrierCostList.get(j);
                                if (carrier != null && carrier.getScac() != null && routingOptionsBean.getScac() != null
                                        && carrier.getScac().trim().equalsIgnoreCase(routingOptionsBean.getScac().substring(1, routingOptionsBean.getScac().length() - 1))
                                        && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                                    pickUpCost = new BigDecimal(carrier.getFinalcharge());
                                    break;
                                }
                            }
                            routingOptionsBean.setPickupCost(pickUpCost);
                        }
                        if (lclQuoteForm.isUps()) {
                            if (CommonUtils.isNotEmpty(lclQuoteForm.getSmallParcelRemarks())) {
                                new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_QT_AUTO_NOTES,
                                        lclQuoteForm.getSmallParcelRemarks(), loginUser.getUserId());
                                lclQuoteForm.setSmallParcelRemarks("");
                            }
                        }
                    }
                }
                String agentInfo[] = new PortsDAO().getDefaultAgentForLcl(routingOptionsBean.getPortOfDestination().getUnLocationCode(), "L");
                if (null != agentInfo && agentInfo.length > 0 && agentInfo[0] != null && !agentInfo[0].toString().equals("")) {
                    lclQuoteForm.getLclQuote().setAgentAcct(new TradingPartner(agentInfo[0].toString()));
                }
                lclQuoteForm.getLclQuote().setPortOfOrigin(routingOptionsBean.getPortOfOrigin());
                lclQuoteForm.getLclQuote().setPortOfLoading(routingOptionsBean.getPortOfLoading());
                lclQuoteForm.getLclQuote().setPortOfDestination(routingOptionsBean.getPortOfDestination());
                lclQuoteForm.getLclQuote().setFinalDestination(routingOptionsBean.getFinalDestination());
                // lclQuoteForm.getLclQuote().setNotify2Contact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
                lclQuoteForm.getLclQuote().setPooWhseContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
                
                lclQuoteForm.getLclQuote().getSupContact().setLclFileNumber(lclFileNumber);
                lclQuoteUtils.setUserAndDateTime(lclQuoteForm.getLclQuote().getSupContact(), now, loginUser);
                
                lclQuoteUtils.saveLclContact(lclQuoteForm, lclFileNumber, now, loginUser);
                LclDwr lcldwr = new LclDwr();
                if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getPortOfOrigin() != null) {
                    String[] deliverCargoTo = lcldwr.getdeliverCargoDetails(lclQuoteForm.getLclQuote().getPortOfOrigin().getUnLocationCode());
                    if (CommonUtils.isNotEmpty(deliverCargoTo) && CommonUtils.isNotEmpty(deliverCargoTo[0])) {
                        Warehouse deliverWarehouse = new WarehouseDAO().getWareHouseBywarehsNo(deliverCargoTo[0]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setWarehouse(null != deliverWarehouse ? deliverWarehouse : null);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setCompanyName(deliverCargoTo[1]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setAddress(deliverCargoTo[2]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setCity(deliverCargoTo[3]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setState(deliverCargoTo[4]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setZip(deliverCargoTo[5]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setPhone1(deliverCargoTo[6]);
                        lclQuoteForm.getLclQuote().getPooWhseContact().setFax1(deliverCargoTo[7]);
                        lclQuoteForm.setWhsCode(deliverCargoTo[0]);
                    }
                }
                this.addClient(lclQuoteForm, lclFileNumber, request);
                lclQuoteUtils.addPortRemarks(lclQuoteForm, lclFileNumber, loginUser, now, lclRemarksDAO);
                exportQuoteUtils.setLclBookingExport(lclQuoteForm, lclFileNumber, request, loginUser);
                this.addDoorCityZip(lclQuoteForm, lclFileNumber, now, loginUser);
                lclQuoteUtils.setPolPodValues(lclQuoteForm.getLclQuote(), request);
                lclQuoteForm.setEnums("");
                List commodityList = null != lclSession.getQuoteCommodityList() ? lclSession.getQuoteCommodityList() : new ArrayList();
                for (Object obj : commodityList) {
                    LclQuotePiece lqp = (LclQuotePiece) obj;
                    lqp.setLclFileNumber(lclFileNumber);
                    List<LclQuotePieceDetail> detailList = lqp.getLclQuotePieceDetailList();
                    lqp.setLclQuotePieceDetailList(null);
                    lqp.setLclQuoteAcList(null);
                    new LclQuotePieceDAO().save(lqp);
                    if (CommonUtils.isNotEmpty(detailList)) {
                        for (int i = 0; i < detailList.size(); i++) {
                            LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(i);
                            detail.setQuotePiece(lqp);
                            new QuoteCommodityDetailsDAO().save(detail);
                        }
                    }
                }
                lclQuoteDAO.saveOrUpdate(lclQuoteForm.getLclQuote());
                lclSession.setQuoteCommodityList(null);
                lclSession.setQuoteDetailList(null);
                session.setAttribute("lclSession", lclSession);
                List lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId());
                LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                if (lclQuoteForm.getLclQuote().getPortOfOrigin() != null && lclQuoteForm.getLclQuote().getPortOfOrigin().getUnLocationCode() != null
                        && !lclQuoteForm.getLclQuote().getPortOfOrigin().getUnLocationCode().trim().equals("")
                        && lclQuoteForm.getLclQuote().getPortOfDestination() != null && lclQuoteForm.getLclQuote().getPortOfDestination().getUnLocationCode() != null
                        && !lclQuoteForm.getLclQuote().getPortOfDestination().getUnLocationCode().trim().equals("")
                        && lclQuotePiecesList != null && !lclQuotePiecesList.isEmpty()) {
                    String rateType = "R".equalsIgnoreCase(lclQuoteForm.getRateType()) ? "Y" : lclQuoteForm.getRateType();
                    lclQuotationChargesCalculation.calculateRates(lclQuoteForm.getLclQuote().getPortOfOrigin().getUnLocationCode(),
                            lclQuoteForm.getLclQuote().getFinalDestination().getUnLocationCode(),
                            lclQuoteForm.getLclQuote().getPortOfLoading().getUnLocationCode(),
                            lclQuoteForm.getLclQuote().getPortOfDestination().getUnLocationCode(),
                            lclFileNumber.getId(), lclQuotePiecesList, getCurrentUser(request), lclQuoteForm.getPooDoor(), lclQuoteForm.getInsurance(),
                            lclQuoteForm.getLclQuote().getValueOfGoods(), rateType, "C", null, null, null, null, lclQuoteForm.getCalcHeavy(),
                            lclQuoteForm.getDeliveryMetro(), lclQuoteForm.getPcBoth(), null, null, request);
                    //request.setAttribute("ofratebasis", lclQuotationChargesCalculation.getOfratebasis());
                    //request.setAttribute("stdchgratebasis", lclQuotationChargesCalculation.getStdchgratebasis());
                }
                GlMappingDAO glMappingDAO = new GlMappingDAO();
                if (routingOptionsBean.getCtsAmount() != null && !routingOptionsBean.getCtsAmount().trim().equals("")) {
                    TradingPartner spRefNumber = new TradingPartnerDAO().findById("CTSFRE0004");
                    String chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
                    LclQuoteAc lclQuoteAc = lclQuoteAcDAO.getLclQuoteAcByChargeCode(lclFileNumber.getId(), chargeCode);
                    if (lclQuoteAc == null) {
                        lclQuoteAc = new LclQuoteAc();
                        lclQuoteAc.setEnteredBy(loginUser);
                        lclQuoteAc.setEnteredDatetime(now);
                        lclQuoteAc.setLclFileNumber(lclFileNumber);
                        lclQuoteAc.setBundleIntoOf(false);
                        lclQuoteAc.setPrintOnBl(true);
                        lclQuoteAc.setTransDatetime(now);
                    }
                    LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(lclFileNumber.getId());
                    if (lclQuotePad == null) {
                        lclQuotePad = new LclQuotePad();
                        LclContact lclContact = new LclContact();
                        lclContact.setLclFileNumber(lclFileNumber);
                        lclQuoteUtils.setUserAndDateTime(lclContact, now, loginUser);
                        lclQuotePad.setDeliveryContact(lclContact);
                        LclContact lclPickupContact = new LclContact();
                        lclPickupContact.setLclFileNumber(lclFileNumber);
                        lclQuoteUtils.setUserAndDateTime(lclPickupContact, now, loginUser);
                        lclQuotePad.setPickupContact(lclPickupContact);
                        lclQuotePad.setEnteredBy(loginUser);
                        lclQuotePad.setEnteredDatetime(now);
                    }
                    BigDecimal d = new BigDecimal(routingOptionsBean.getCtsAmount().substring(1, routingOptionsBean.getCtsAmount().length()));
                    BigDecimal pickupCost = routingOptionsBean.getPickupCost();
                    lclQuoteAc.setArAmount(d);
                    lclQuoteAc.setRatePerUnit(d);
                    lclQuoteAc.setApAmount(pickupCost);
                    GlMapping gp = glMappingDAO.findByChargeCode(chargeCode, "LCLE", "AR");
                    lclQuoteAc.setArglMapping(gp);
                    GlMapping apGlmapping = glMappingDAO.findByChargeCode(chargeCode, "LCLE", TRANSACTION_TYPE_ACCRUALS);
                    lclQuoteAc.setApglMapping(apGlmapping);
                    lclQuoteAc.setSupAcct(spRefNumber);
                    lclQuoteAc.setManualEntry(true);
                    lclQuoteAc.setRatePerUnitUom("FL");
                    lclQuoteAc.setRateUom("I");
                    lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                    lclQuoteAc.setModifiedBy(loginUser);
                    lclQuoteAc.setModifiedDatetime(now);
                    lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
                    lclQuotePad.setLclFileNumber(lclFileNumber);
                    lclQuotePad.setModifiedBy(loginUser);
                    lclQuotePad.setModifiedDatetime(now);
                    lclQuotePad.getLclQuoteAc().setArAmount(d);
                    lclQuotePad.getLclQuoteAc().setApAmount(pickupCost);
                    if (routingOptionsBean.getScac() != null && !routingOptionsBean.getScac().trim().equals("")) {
                        lclQuotePad.setScac(routingOptionsBean.getScac().substring(1, routingOptionsBean.getScac().length() - 1));
                    }
                    lclQuotePad.setLclQuoteAc(lclQuoteAc);
                    lclQuotePadDAO.saveOrUpdate(lclQuotePad);
                }
                List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(lclFileNumber.getId(), LCL_EXPORT);
                LclQuote lclQuote = lclQuoteDAO.getByProperty("lclFileNumber.id", lclFileNumber.getId());
                lclQuoteDAO.getCurrentSession().evict(lclQuote);
                exportQuoteUtils.setUpcomingSailings(lclQuote, "", request, "false");//Upcomong Sailings List
                exportQuoteUtils.setTrmandEciPortCode(lclQuoteForm, lclQuote, new LclUtils());
                exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePiecesList, lclQuoteForm.getFdEngmet());
                exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, lclFileNumber.getId(), lclQuoteAcDAO, lclQuotePiecesList, lclQuoteForm.getLclQuote().getBillingType(), lclQuoteForm.getFdEngmet(), "No");
                lclQuoteUtils.setCommodityList(lclFileNumber.getId(), request);
                request.setAttribute("ofspotrate", lclQuote.getSpotRate());
                request.setAttribute("lclQuote", lclQuote);
                request.setAttribute("lclQuoteForm", lclQuoteForm);
            }
        }
        return mapping.findForward("exportQuote");
    }
    
    public ActionForward deleteLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        String commodityId = request.getParameter("commodityId");
        LclQuote lclQuote = null;
        new LclQuotePieceDAO().deleteNotesForCommodity(Long.parseLong(fileNumberId), getCurrentUser(request).getUserId());
        if (fileNumberId != null && !fileNumberId.trim().equals("") && commodityId != null && !commodityId.trim().equals("")) {
            lclQuote = lclQuoteDAO.findById(Long.parseLong(fileNumberId));
            LclQuotePiece lclQuotePiece = new LclQuotePieceDAO().findById(Long.parseLong(commodityId));
            new LclQuotePieceDAO().delete(lclQuotePiece.getId());
            request.setAttribute("lclCommodityList", new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
            request.setAttribute("ofspotrate", lclQuote.getSpotRate());
        } else if (request.getParameter("id") != null && !request.getParameter("id").trim().equals("")) {
            int index = Integer.parseInt(request.getParameter("id"));
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<LclQuotePiece> quoteCommodityList = (List<LclQuotePiece>) (null != lclSession.getQuoteCommodityList() ? lclSession.getQuoteCommodityList() : new ArrayList<LclQuotePiece>());
            quoteCommodityList.remove(index);
            lclSession.setQuoteCommodityList(quoteCommodityList);
            session.setAttribute("lclSession", lclSession);
            request.setAttribute("lclCommodityList", quoteCommodityList);
        }
        return mapping.findForward(COMMODITY_DESC);
    }
    
    public ActionForward deleteQuoteCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
        String chargeId = request.getParameter("qcid");
        Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
        User loginUser = getCurrentUser(request);
        LclQuoteAc lclQuoteAc = null;
        if (CommonUtils.isNotEmpty(chargeId)) {
            lclQuoteAc = lclQuoteAcDAO.findById(Long.parseLong(chargeId));
            if (lclQuoteAc != null) {
                if ((CHARGE_CODE_DOOR.equalsIgnoreCase(lclQuoteAc.getArglMapping().getChargeCode())
                        || CHARGE_CODE_INLAND.equalsIgnoreCase(lclQuoteAc.getArglMapping().getChargeCode()))) {
                    LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
                    LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(fileId);
                    if (lclQuotePad != null) {
                        lclQuotePad.setScac(null);
                        lclQuotePad.setLclQuoteAc(null);
                        lclQuotePadDAO.update(lclQuotePad);
                    }
                }
                String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
            }
            lclQuoteAcDAO.delete(lclQuoteAc);
        }
        List<LclQuotePiece> lclCommodityList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", fileId);
        LclQuote lclQuote = lclQuoteDAO.findById(fileId);
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, lclQuoteForm.getModuleName());
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclQuote.getQuoteType())) {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
        } else {
            ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
            importQuoteUtils.setWeighMeasureForImpQuote(request, lclCommodityList);
            importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, fileId, lclCommodityList, lclQuote.getBillingType());
        }
        request.setAttribute("lclQuote", lclQuote);
        return mapping.findForward(CHARGE_DESC);
    }
    
    public ActionForward modifyCommodityAndCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
        User loginUser = getCurrentUser(request);
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        String recalculate = request.getParameter("recalculate");
        List<LclQuoteAc> lclQuoteAcList = null;
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        Date now = new Date();
        if (null != recalculate && "true".equals(recalculate)) { // if recalculate true then calculate the rates
            List<LclQuotePiece> quoteCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            calculateQuoteRates(form, request, quoteCommodityList);
        } else { // otherwise delete the auto rates
            LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
            lclQuoteAcList = lclQuoteAcDAO.executeQuery("from LclQuoteAc where lclFileNumber.id=" + fileId + "and manualEntry=0");
            if (null != lclQuoteAcList && !lclQuoteAcList.isEmpty()) {
                for (LclQuoteAc lclQuoteAc : lclQuoteAcList) {
                    String remarks = lclQuoteUtils.setDeleteChargeTriggerValues(lclQuoteAc);
                    lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                    lclQuoteAcDAO.delete(lclQuoteAc.getId());
                }
            }
        }
        lclQuoteAcList = lclQuoteAcDAO.executeQuery("from LclQuoteAc where lclFileNumber.id = " + fileId + "and manualEntry = 1");
        List lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
        Double totalWeight = 0.00;
        Double totalMeasure = 0.00;
        Double calculatedWeight = 0.00;
        Double calculatedMeasure = 0.00;
        for (LclQuoteAc lclQuoteAc : lclQuoteAcList) {
            if (CommonUtils.isNotEmpty(fileId)) {
                lclQuoteAc.setLclFileNumber(new LclFileNumber(fileId));
            }
            if (CommonUtils.isNotEmpty(lclQuoteAc.getArglMapping().getChargeCode())) {
                GlMapping glmapping = new GlMappingDAO().findByChargeCode(lclQuoteAc.getArglMapping().getChargeCode(), "LCLE", "AR");
                lclQuoteAc.setArglMapping(glmapping);
                lclQuoteAc.setApglMapping(glmapping);
            }
            lclQuoteAc.setTransDatetime(now);
            lclQuoteAc.setEnteredBy(loginUser);
            lclQuoteAc.setModifiedBy(loginUser);
            lclQuoteAc.setEnteredDatetime(now);
            lclQuoteAc.setModifiedDatetime(now);
            lclQuoteAc.setManualEntry(true);
            lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
            if (lclQuoteAc.getRatePerWeightUnit() != null && lclQuoteAc.getRatePerWeightUnit().doubleValue() > 0.00
                    && lclQuoteAc.getRatePerVolumeUnit() != null && lclQuoteAc.getRatePerVolumeUnit().doubleValue() > 0.00
                    && lclQuoteAc.getRateFlatMinimum() != null && lclQuoteAc.getRateFlatMinimum().doubleValue() > 0.00) {
                for (int j = 0; j < lclQuotePiecesList.size(); j++) {
                    LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
                    Double weightDouble = 0.00;
                    Double weightMeasure = 0.00;
                    
                    if (lclQuoteForm.getFdEngmet() != null && !"".equalsIgnoreCase(lclQuoteForm.getFdEngmet())) {
                        if (lclQuoteForm.getFdEngmet().equals("E")) {
                            if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclQuotePiece.getActualWeightImperial().doubleValue();
                            } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclQuotePiece.getBookedWeightImperial().doubleValue();
                            }
                            
                            if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                            } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (lclQuoteForm.getFdEngmet().equals("M")) {
                            if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclQuotePiece.getActualWeightMetric().doubleValue();
                            } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclQuotePiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                            } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                            }
                        }//end of else if engmet
                    }//end of else null

                    //calculate the Total Weight Of Commodities
                    totalWeight = totalWeight + weightDouble;
                    //calculate the Total Measure Of Commodities
                    totalMeasure = totalMeasure + weightMeasure;
                }//end of for loop

                if (lclQuoteForm.getFdEngmet() != null && !"".equalsIgnoreCase(lclQuoteForm.getFdEngmet())) {
                    if (lclQuoteForm.getFdEngmet().equals("E")) {
                        calculatedWeight = (totalWeight / 100) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                    } else if (lclQuoteForm.getFdEngmet().equals("M")) {
                        calculatedWeight = (totalWeight / 1000) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                    }
                }//end of else if engmet
                calculatedMeasure = totalMeasure * lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                if (lclQuoteAc.getRateUom().equals("M")) {
                    lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                } else {
                    lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                }
                lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerWeightUnitDiv());
                lclQuoteAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclQuoteAc.getRateFlatMinimum().doubleValue()) {
                    lclQuoteAc.setArAmount(new BigDecimal(calculatedWeight));
                    lclQuoteAc.setRatePerUnitUom("W");
                    lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerVolumeUnitDiv());
                } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclQuoteAc.getRateFlatMinimum().doubleValue()) {
                    lclQuoteAc.setArAmount(new BigDecimal(calculatedMeasure));
                    lclQuoteAc.setRatePerUnitUom("V");
                    lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerVolumeUnitDiv());
                    
                } else {
                    lclQuoteAc.setArAmount(lclQuoteAc.getRateFlatMinimum());
                    lclQuoteAc.setRatePerUnitUom("M");
                }
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setPrintOnBl(true);
                lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
            }
        }
        LclQuote lclQuote = lclQuoteDAO.findById(fileId);
        List<LclQuotePiece> lclQuotePieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, lclQuoteForm.getModuleName());
        request.setAttribute("lclCommodityList", lclQuotePiecesList);
        exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclQuotePieceList, chargeList, loginUser, false, request);//set Export Rate Details
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward calculateInsuranceCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
            User loginUser = getCurrentUser(request);
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclQuote lclQuote = new LCLQuoteDAO().findById(fileId);
            List<LclQuotePiece> lclQuoteCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            String pcBoth = request.getParameter("pcBoth");
            String valueOfGoods = request.getParameter("valueOfGoods");
            String insurance = request.getParameter("insurance");
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, INSURANCE_CHARGE_CODE, false);
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            String pooTrmNum = "", polTrmNum = "", fdEciPortCode = "", podEciPortCode = "";
            String cif = "";
            pooTrmNum = lclQuoteForm.getPooTrmNum();
            polTrmNum = lclQuoteForm.getPolTrmNum();
            podEciPortCode = lclQuoteForm.getPodEciPortCode();
            fdEciPortCode = lclQuoteForm.getFdEciPortCode();
            if ("Y".equalsIgnoreCase(insurance) && lclQuoteCommodityList != null && lclQuoteCommodityList.size() > 0 && valueOfGoods != null && !valueOfGoods.trim().equals("")) {
                GlMappingDAO glmappingdao = new GlMappingDAO();
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0006", "LCLE", "AR");
                cif = lclQuotationChargesCalculation.calculateInsuaranceChargeForRadio(pooTrmNum, polTrmNum, fdEciPortCode, podEciPortCode, lclQuoteCommodityList,
                        Double.parseDouble(valueOfGoods), loginUser, fileId, lclQuoteAc, glmapping, "I");
                lclQuote.setInsurance(true);
            } else if ("N".equalsIgnoreCase(insurance) && lclQuoteAc != null) {
                String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                lclQuote.setInsurance(false);
                lclQuoteAcDAO.delete(lclQuoteAc);
            }
            if (CommonUtils.isNotEmpty(cif)) {
                lclQuote.setCifValue(new BigDecimal(cif));
            } else {
                lclQuote.setCifValue(null);
            }
            lclQuote.setModifiedBy(loginUser);
            lclQuote.setModifiedDatetime(new Date());
            lclQuoteDAO.saveOrUpdate(lclQuote);
            lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, CAF_CHARGE_CODE, false);
            if (lclQuoteAc != null) {
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmNum, polTrmNum, fdEciPortCode, podEciPortCode, lclQuoteCommodityList, pcBoth,
                        loginUser, fileId, lclQuote.getFinalDestination().getUnLocationCode(), lclQuoteForm.getFdEngmet());
            }
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclQuoteCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
            //request.setAttribute("lclQuoteCommodityList", lclQuoteCommodityList);
        } catch (Exception e) {
            log.info("Error in calculateInsuranceCharge method,  on " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward calculateDeliveryMetroCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
            String deliveryMetroValue = request.getParameter("deliveryMetro");
            User loginUser = getCurrentUser(request);
            LclQuote lclQuote = new LCLQuoteDAO().findById(fileId);
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            String pooTrmNum = "", polTrmNum = "", fdEciPortCode = "", podEciPortCode = "";
            pooTrmNum = lclQuoteForm.getPooTrmNum();
            polTrmNum = lclQuoteForm.getPolTrmNum();
            podEciPortCode = lclQuoteForm.getPodEciPortCode();
            fdEciPortCode = lclQuoteForm.getFdEciPortCode();
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            if (deliveryMetroValue.equalsIgnoreCase("I") || deliveryMetroValue.equalsIgnoreCase("O")) {
                String deliveryMetro = "";
                if (deliveryMetroValue.equalsIgnoreCase("I")) {
                    deliveryMetro = "0060";
                    lclQuote.setDeliveryMetro("I");
                } else {
                    deliveryMetro = "0015";
                    lclQuote.setDeliveryMetro("O");
                }
                lclQuotationChargesCalculation.calculateChargeForDeliveyMetro(pooTrmNum, polTrmNum, fdEciPortCode, podEciPortCode,
                        lclCommodityList, loginUser, fileId, lclQuoteForm.getFdEngmet(), deliveryMetro, lclQuote.getBillToParty());
            } else if (deliveryMetroValue.equals("N")) {
                lclQuote.setDeliveryMetro("N");
                String chargeCodeArray[] = chargeCodeArray = new String[]{"DELMET", "DELOUT"};
                for (int i = 0; i < chargeCodeArray.length; i++) {
                    LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, chargeCodeArray[i], false);
                    if (lclQuoteAc != null) {
                        String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                        lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                        lclQuoteAcDAO.deleteRatesByGlmappingChargeCodeAndManualEntry(fileId, lclQuoteAc.getArglMapping().getChargeCode(), false);
                    }
                }
            }
            String pcBoth = request.getParameter("pcBoth");
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, CAF_CHARGE_CODE, false);
            if (lclQuoteAc != null) {
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmNum, polTrmNum, fdEciPortCode, podEciPortCode, lclCommodityList, pcBoth,
                        loginUser, fileId, lclQuote.getFinalDestination().getUnLocationCode(), lclQuoteForm.getFdEngmet());
            }
            lclQuote.setModifiedBy(loginUser);
            lclQuote.setModifiedDatetime(new Date());
            lclQuoteDAO.saveOrUpdate(lclQuote);
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
            //request.setAttribute("lclCommodityList", lclCommodityList);
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
        } catch (Exception e) {
            log.info("Error in calculateDeliveryMetroCharge method,  on " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward calculateHeavyCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
            User loginUser = getCurrentUser(request);
            LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclQuote lclQuote = new LCLQuoteDAO().findById(fileId);
            List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            String pooTrmNum = "", polTrmNum = "", fdEciPortCode = "", podEciPortCode = "";
            String radioValue = request.getParameter("calcHeavy");
            pooTrmNum = lclQuoteForm.getPooTrmNum();
            polTrmNum = lclQuoteForm.getPolTrmNum();
            podEciPortCode = lclQuoteForm.getPodEciPortCode();
            fdEciPortCode = lclQuoteForm.getFdEciPortCode();
            LclBookingExport lclBookingExport = lclBookingExportDAO.findById(fileId);
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            if ("Y".equalsIgnoreCase(radioValue)) {
                lclQuotationChargesCalculation.calculateChargeForCalcHeavy(pooTrmNum, polTrmNum, fdEciPortCode, podEciPortCode,
                        true, lclCommodityList, fileId, loginUser, lclQuoteForm.getFdEngmet());
                lclBookingExport.setCalcHeavy(true);
            } else if ("N".equalsIgnoreCase(radioValue)) {
                String chargeCodeArray[] = chargeCodeArray = new String[]{"DENSE", "HLIFT6", "HLIFT8", "HLIFT", "EXTL", "EXTL30"};
                for (int i = 0; i < chargeCodeArray.length; i++) {
                    LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, chargeCodeArray[i], false);
                    if (lclQuoteAc != null) {
                        String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                        lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                        lclQuoteAcDAO.deleteRatesByGlmappingChargeCodeAndManualEntry(fileId, lclQuoteAc.getArglMapping().getChargeCode(), false);
                    }
                }
                lclBookingExport.setCalcHeavy(false);
            }
            String pcBoth = request.getParameter("pcBoth");
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, CAF_CHARGE_CODE, false);
            if (lclQuoteAc != null) {
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmNum, polTrmNum, fdEciPortCode,
                        podEciPortCode, lclCommodityList, pcBoth,
                        loginUser, fileId,
                        lclQuote.getFinalDestination().getUnLocationCode(), lclQuoteForm.getFdEngmet());
            }
            lclBookingExport.setModifiedBy(loginUser);
            lclBookingExport.setModifiedDatetime(new Date());
            lclBookingExportDAO.saveOrUpdate(lclBookingExport);
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
        } catch (Exception e) {
            log.info("Error in calculateHeavyCharge method,  on " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward displaySpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LclQuoteCostAndChargeForm chargeForm = new LclQuoteCostAndChargeForm();
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        LclQuote lclQuote = new LCLQuoteDAO().findById(Long.parseLong(request.getParameter("fileNumberId")));
        MessageResources messageResources = getResources(request);
//        LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(Long.parseLong(request.getParameter("fileNumberId")), 
//                messageResources.getMessage("chargeCodeAsSPTCTR"), true);
//        request.setAttribute("lclQuoteAc", lclQuoteAc);
        if (CommonUtils.isNotEmpty(quoteForm.getDestination())) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", quoteForm.getDestination());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                request.setAttribute("engmet", ports.getEngmet());
            }
            request.setAttribute("destination", quoteForm.getDestination());
        }
        chargeForm.setRate(null != lclQuote.getSpotWmRate() ? lclQuote.getSpotWmRate().toString() : "");
        chargeForm.setRateN(null != lclQuote.getSpotRateMeasure() ? lclQuote.getSpotRateMeasure().toString() : "");
        chargeForm.setCheckWM(lclQuote.getSpotRateUom());
        chargeForm.setSpotCheckBottom(lclQuote.isSpotRateBottom());
        chargeForm.setSpotCheckOF(lclQuote.isSpotOfRate());
        chargeForm.setComment(lclQuote.getSpotComment());
        request.setAttribute("lclQuoteCostAndChargeForm", chargeForm);
        return mapping.findForward(LCL_SPOTRATE);
    }
    
    public ActionForward deleteDocumCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            User loginUser = getCurrentUser(request);
            Long fileId = Long.parseLong(lclQuoteForm.getFileNumberId());
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclQuote lclQuote = new LCLQuoteDAO().findById(fileId);
            List<LclQuotePiece> lclQuoteCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            String pooTrmNum = "", polTrmNum = "", fdEciPortCode = "", podEciPortCode = "";
            pooTrmNum = lclQuoteForm.getPooTrmNum();
            polTrmNum = lclQuoteForm.getPolTrmNum();
            podEciPortCode = lclQuoteForm.getPodEciPortCode();
            fdEciPortCode = lclQuoteForm.getFdEciPortCode();
            String documentation = request.getParameter("documentation");
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, "DOCUM", true);
            if (documentation.equals("N") && lclQuoteAc != null) {
                lclQuote.setDocumentation(false);
                lclQuote.setModifiedBy(loginUser);
                lclQuote.setModifiedDatetime(new Date());
                lclQuoteDAO.saveOrUpdate(lclQuote);
                String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                lclQuoteAcDAO.delete(lclQuoteAc);
            }
            String pcBoth = request.getParameter("pcBoth");
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, CAF_CHARGE_CODE, false);
            if (lclQuoteAc != null) {
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmNum, polTrmNum, fdEciPortCode,
                        podEciPortCode, lclQuoteCommodityList, pcBoth, loginUser, fileId,
                        lclQuote.getFinalDestination().getUnLocationCode(), lclQuoteForm.getFdEngmet());
            }
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclQuoteCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
        } catch (Exception e) {
            log.info("Error in deleteDocumCharge method. ", e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward deletingPickUpCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        String deleteFlag = request.getParameter("deleteFlag");
        User user = getCurrentUser(request);
        LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(fileNumberId));
        String moduleName = LCL_EXPORT;
        if ("I".equalsIgnoreCase(lclQuote.getQuoteType())) {
            moduleName = LCL_IMPORT;
        }
        if ("true".equalsIgnoreCase(deleteFlag)) {
            lclQuote.setPooDoor(false);
            lclQuote.setModifiedBy(user);
            lclQuote.setModifiedDatetime(new Date());
            lclQuoteDAO.saveOrUpdate(lclQuote);
        }
        LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(Long.parseLong(fileNumberId));
        if (lclQuotePad != null && null != lclQuotePad.getLclQuoteAc()) {
            Long lclQuoteAcId = lclQuotePad.getLclQuoteAc().getId();
            String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuotePad.getLclQuoteAc());
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, remarks, user.getUserId());
            lclQuoteAcDAO.deleteByQtAcId(lclQuoteAcId);
        }
        
        List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(fileNumberId), moduleName);
        if ("I".equalsIgnoreCase(lclQuote.getQuoteType())) {
            ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
            importQuoteUtils.setWeighMeasureForImpQuote(request, lclCommodityList);
            importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(fileNumberId), lclCommodityList, lclQuote.getBillingType());
            new LclQuoteImportDAO().deleteDoorDeliveryData(Long.parseLong(fileNumberId));
        } else {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, user, true, request);
        }
        return mapping.findForward("chargeDescription");
    }
    
    public ActionForward refreshQtCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        request.setAttribute("lclCommodityList", new LclQuoteUtils().setCommodityList(Long.parseLong(request.getParameter("fileNumberId")), request));
        request.setAttribute("ofspotrate", lclQuoteForm.getLclQuote().getSpotRate());
        return mapping.findForward("commodityDesc");
    }
    
    public ActionForward displayAES(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String fileNumberId = lclQuoteForm.getFileNumberId();
        request.setAttribute("lclQuoteForm", lclQuoteForm);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3pRefAesList(Long.parseLong(fileNumberId)));
        }
        return mapping.findForward("aes");
        
    }
    
    public ActionForward submitQuote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {//remove
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuotationChargesCalculation chargesCalculation = new LclQuotationChargesCalculation();
        List<LclQuoteAc> quoteAcList = chargesCalculation.quickQuoteChargesCalculation(lclQuoteForm.getOriginUnlocationCodeForDr(), lclQuoteForm.getUnlocationCodeForDr(), lclQuoteForm.getPortOfOriginIdForDr(), lclQuoteForm.getFinalDestinationIdForDr(), lclQuoteForm.getCommodityNoForDr(), lclQuoteForm.getRateTypeForDr(), lclQuoteForm.getHazmatDr());
        request.setAttribute("chargeList", quoteAcList);
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setQuoteAcList(quoteAcList);
        request.setAttribute("originUnlocationCodeForDr", lclQuoteForm.getOriginUnlocationCodeForDr());
        request.setAttribute("hazmatDr", lclQuoteForm.getHazmatDr());
        request.setAttribute("unlocationCodeForDr", lclQuoteForm.getUnlocationCodeForDr());
        request.setAttribute("portOfOriginIdForDr", lclQuoteForm.getPortOfOriginIdForDr());
        request.setAttribute("portOfOriginForDr", lclQuoteForm.getPortOfOriginForDr());
        request.setAttribute("finalDestinationIdForDr", lclQuoteForm.getFinalDestinationIdForDr());
        request.setAttribute("finalDestinationForDr", lclQuoteForm.getFinalDestinationForDr());
        request.setAttribute("commodityTypeForDr", lclQuoteForm.getCommodityTypeForDr());
        request.setAttribute("commodityTypeIdForDr", lclQuoteForm.getCommodityTypeId());
        request.setAttribute("commodityNoForDr", lclQuoteForm.getCommodityNoForDr());
        request.setAttribute("clientAcctForDr", lclQuoteForm.getClientAcctForDr());
        request.setAttribute("clientCompanyForDr", lclQuoteForm.getClientCompanyForDr());
        request.setAttribute("rateTypeDr", lclQuoteForm.getRateTypeForDr());
        request.setAttribute("commodityNumber", lclQuoteForm.getCommodityNo());
        request.setAttribute("originDr", lclQuoteForm.getOriginDr());
        request.setAttribute("destinationDr", lclQuoteForm.getDestinationDr());
        request.setAttribute("retailCommodity", lclQuoteForm.getRetailCommodity());
        request.setAttribute("stdRateBasis", chargesCalculation.getStdchgratebasis());
        request.setAttribute("rateAmount", chargesCalculation.getRateAmount());
        return mapping.findForward("submitQuickQuote");
    }
    
    public ActionForward addAES(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        String referenceValue = "", referenceType = "", remarks = "";
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(lclQuoteForm.getAesItnNumber())) {
            referenceValue = lclQuoteForm.getAesItnNumber();
            referenceType = _3PARTY_AES_ITNNUMBER;
            remarks = "Inserted - AES/ITN# " + " " + referenceValue;
            
        } else {
            referenceValue = lclQuoteForm.getAesException();
            referenceType = _3PARTY_AES_EXCEPTION;
            remarks = "Inserted - AES/ITN# " + " " + referenceValue;
            
        }
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            lcl3pRefNoDAO.save3pRefNo(Long.parseLong(fileNumberId), referenceType, referenceValue);
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3pRefAesList(Long.parseLong(fileNumberId)));
            if (CommonUtils.isNotEmpty(remarks) && CommonUtils.isNotEmpty(fileNumberId)) {
//            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), referenceType, remarks, getCurrentUser(request).getUserId());
                lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                
            }
        }
        return mapping.findForward("aes");
    }
    
    public ActionForward modifyColoadRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
        
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
        User user = getCurrentUser(request);
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<LclQuotePiece> quoteCommodityList = (List<LclQuotePiece>) (null != lclSession.getQuoteCommodityList() ? lclSession.getQuoteCommodityList() : new ArrayList<LclQuotePiece>());
        LclQuotePiece lclQuotePiece = new LclQuotePiece();
        PackageTypeDAO packageTypeDAO = new PackageTypeDAO();
        commodityTypeDAO commoditytypedao = new commodityTypeDAO();
        PackageType packageType = packageTypeDAO.getByProperty("description", CommonConstants.DEFAULT_PACKAGE_TYPE);
        CommodityType commodityType = commoditytypedao.getCommodityCode(CommonConstants.DEFAULT_COLOAD_COMMODITY);
        lclQuotePiece.setPackageType(packageType);
        lclQuotePiece.setCommodityType(commodityType);
        lclQuotePiece.setCommNo(commodityType.getCode());
        lclQuotePiece.setPkgNo(packageType.getId());
        lclQuotePiece.setEnteredBy(getCurrentUser(request));
        lclQuotePiece.setEnteredDatetime(new Date());
        lclQuotePiece.setModifiedBy(getCurrentUser(request));
        lclQuotePiece.setModifiedDatetime(new Date());
        lclQuotePiece.setPersonalEffects("N");
        lclQuotePiece.setBookedPieceCount(new Integer(0));
        quoteCommodityList.add(lclQuotePiece);
        lclSession.setQuoteCommodityList(quoteCommodityList);
        LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
        String rateType = lclQuoteForm.getRateType();
        if (lclQuoteForm.getRateType() != null && !lclQuoteForm.getRateType().trim().equals("")) {
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
        }
        String origin = "";
        String destination = "";
        if (lclQuoteForm.getPortOfOrigin() != null) {
            String[] originName = lclQuoteForm.getPortOfOrigin().split("/");
            origin = originName[0].toUpperCase();
        }
        if (lclQuoteForm.getFinalDestination() != null) {
            String[] destinationName = lclQuoteForm.getFinalDestination().split("/");
            destination = destinationName[0].toUpperCase();
        }
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        lclQuotationChargesCalculation.calculateRates(lclQuoteForm.getOriginUnlocationCode(), lclQuoteForm.getUnlocationCode(), lclQuoteForm.getPolUnlocationcode(),
                lclQuoteForm.getPodUnlocationcode(), null, quoteCommodityList, user, null, null, null, rateType,
                "C", null, null, null, null, null, null, null, null, null, request);
//        exportQuoteUtils.setWeighMeasureForQuote(request, quoteCommodityList, lclQuotationChargesCalculation.getPorts());
        //      exportQuoteUtils.setRolledUpChargesForQuote(lclQuotationChargesCalculation.getQuoteAcList(), request, null, null, quoteCommodityList, null, lclQuotationChargesCalculation.getPorts().getEngmet(), "No");
        List<LclQuoteAc> lclQuoteAcList = (List<LclQuoteAc>) request.getAttribute("chargeList");
        request.setAttribute("totalCharges", exportQuoteUtils.calculateTotalByQuoteAcList(lclQuoteAcList));
        request.setAttribute("origin", origin);
        request.setAttribute("pol", lclQuoteForm.getPortOfLoading());
        request.setAttribute("portOfLoadingId", lclQuoteForm.getPortOfLoadingId());
        request.setAttribute("portOfDestinationId", lclQuoteForm.getPortOfDestinationId());
        request.setAttribute("pod", lclQuoteForm.getPortOfDestination());
        request.setAttribute("destination", destination);
        lclSession.setQuoteAcList(lclQuoteAcList);
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("lclCommodityList", quoteCommodityList);
        return mapping.findForward(LCL_QUOTE_EXPORT);
    }
    
    public void calculateQuoteRates(ActionForm form, HttpServletRequest request,
            List<LclQuotePiece> quoteCommodityList) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        HttpSession session = request.getSession();
        String fileId = request.getParameter("fileNumberId");
        User loginUser = getCurrentUser(request);
        LclQuotationChargesCalculation calculation = new LclQuotationChargesCalculation();
        String fromZip = null;
        String rateType = lclQuoteForm.getRateType();
        if (null != rateType && !rateType.trim().equals("") && "R".equalsIgnoreCase(rateType)) {
            rateType = "Y";
        }
        if (lclQuoteForm.getDoorOriginCityZip() != null && !lclQuoteForm.getDoorOriginCityZip().trim().equals("")) {
            String[] zip = lclQuoteForm.getDoorOriginCityZip().split("-");
            fromZip = zip[0];
        }
        calculation.calculateRates(lclQuoteForm.getOriginUnlocationCode(), lclQuoteForm.getDestination(), lclQuoteForm.getPolCode(),
                lclQuoteForm.getPodCode(), Long.parseLong(fileId), quoteCommodityList, loginUser, lclQuoteForm.getPooDoor(),
                lclQuoteForm.getInsurance(), lclQuoteForm.getLclQuote().getValueOfGoods(), rateType, "C", null, null,
                fromZip, session, lclQuoteForm.getCalcHeavy(), lclQuoteForm.getDeliveryMetro(), lclQuoteForm.getPcBoth(), null, null, request);
    }
    
    public ActionForward calculateCAFCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
            User loginUser = getCurrentUser(request);
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclQuote lclQuote = lclQuoteDAO.findById(fileId);
            List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            String pcBoth = request.getParameter("pcBoth");
            if ("C".equalsIgnoreCase(pcBoth) && lclCommodityList != null && lclCommodityList.size() > 0) {
                String pooTrmNum = "", polTrmNum = "", fdEciPortCode = "", podEciPortCode = "";
                pooTrmNum = lclQuoteForm.getPooTrmNum();
                polTrmNum = lclQuoteForm.getPolTrmNum();
                podEciPortCode = lclQuoteForm.getPodEciPortCode();
                fdEciPortCode = lclQuoteForm.getFdEciPortCode();
                LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmNum, polTrmNum, fdEciPortCode, podEciPortCode,
                        lclCommodityList, pcBoth, loginUser, fileId,
                        lclQuote.getFinalDestination().getUnLocationCode(), lclQuoteForm.getFdEngmet());
            } else if ("P".equalsIgnoreCase(pcBoth) || "B".equalsIgnoreCase(pcBoth)) {
                String chargeCodeArray[] = chargeCodeArray = new String[]{"0005", "0095"};
                LclQuoteUtils lclQuoteUtils = new LclQuoteUtils();
                for (int i = 0; i < chargeCodeArray.length; i++) {
                    LclQuoteAc lclQuoteAc = lclQuoteAcDAO.findByBlueScreenChargeCode(fileId, chargeCodeArray[i], false);
                    if (lclQuoteAc != null) {
                        String remarks = lclQuoteUtils.setDeleteChargeTriggerValues(lclQuoteAc);
                        lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                        lclQuoteAcDAO.delete(lclQuoteAc);
                    }
                }
            }
            lclQuote.setBillingType(pcBoth);
            lclQuote.setModifiedBy(loginUser);
            lclQuote.setModifiedDatetime(new Date());
            lclQuoteDAO.saveOrUpdate(lclQuote);
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
            request.setAttribute("lclCommodityList", lclCommodityList);
            exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
        } catch (Exception e) {
            log.info("Error in LCL calculateCAFCharge method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }
    
    public ActionForward renderChargeDescAdjustment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            List lclQuotePiecesList = null;
            if (CommonUtils.isNotEmpty(lclQuoteForm.getFileNumberId())) {
                LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
                LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(lclQuoteForm.getFileNumberId()));
                request.setAttribute("lclQuote", lclQuote);
                lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
                if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
                    List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), LCL_EXPORT);
                    exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePiecesList, lclQuoteForm.getFdEngmet());
                    exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclquoteacdao, lclQuotePiecesList, lclQuoteForm.getBillingType(), lclQuoteForm.getFdEngmet(), "No");
                }
            }
        } catch (Exception e) {
            log.info("Error in renderChargeDescAdjustment method,  on " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }
    //Import

    public ActionForward addImpAmsHBL(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        LclQuoteImportAms lclQuoteImportAms = new LclQuoteImportAms();
        if (CommonUtils.isNotEmpty(lclQuoteForm.getAmsHblNo())) {
            lclQuoteImportAms.setAmsNo(lclQuoteForm.getAmsHblNo());
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getAmsHblPiece())) {
            lclQuoteImportAms.setPieces(Integer.parseInt(lclQuoteForm.getAmsHblPiece()));
        }
        lclQuoteImportAms.setLclFileNumber(new LclFileNumberDAO().findById(Long.parseLong(fileNumberId)));
        lclQuoteImportAms.setEnteredByUserId(getCurrentUser(request));
        lclQuoteImportAms.setModifiedByUserId(getCurrentUser(request));
        lclQuoteImportAms.setModifiedDatetime(new Date());
        lclQuoteImportAms.setEnteredDatetime(new Date());
        new LclQuoteImportAmsDAO().saveOrUpdate(lclQuoteImportAms);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("amsHBLList", new LclQuoteImportAmsDAO().findAll(Long.parseLong(fileNumberId)));
        }
        return mapping.findForward("ImpAmsHBL");
    }
    
    public ActionForward updateAms(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        LclUtils utils = new LclUtils();
        LclQuoteImportAms quoteAms = null;
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        User thisUser = getCurrentUser(request);
        Date today = new Date();
        StringBuilder remarks = new StringBuilder();
        String defaultAms = null;
        LclQuoteImportAmsDAO quoteAmsDao = new LclQuoteImportAmsDAO();
        Long fileId = Long.parseLong(quoteForm.getFileNumberId());
        quoteAms = quoteAmsDao.getQuoteAms(fileId);
        if (CommonUtils.isNotEmpty(quoteForm.getDefaultAms())) {
            defaultAms = quoteForm.getDefaultAms().toUpperCase();
        }
        if (null == quoteAms) {
            quoteAms = new LclQuoteImportAms();
            quoteAms.setEnteredByUserId(thisUser);
            quoteAms.setEnteredDatetime(today);
            remarks.append("Created - AMS/HBL # ").append(defaultAms);
        } else {
            remarks.append("Updated - AMS/HBL # ").append(quoteAms.getAmsNo().toUpperCase()).append(" to ").append(defaultAms);
        }
        if (CommonUtils.isNotEmpty(quoteForm.getDefaultAms())) {
            quoteAms.setAmsNo(quoteForm.getDefaultAms());
        }
        quoteAms.setLclFileNumber(fileDao.findById(fileId));
        quoteAms.setModifiedByUserId(thisUser);
        quoteAms.setModifiedDatetime(today);
        quoteAmsDao.saveOrUpdate(quoteAms);
        utils.insertLCLRemarks(fileId, remarks.toString(), REMARKS_QT_AUTO_NOTES, thisUser);// record a note on this.
        return mapping.findForward(null);
    }
    
    public ActionForward deleteImpAmsHBL(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        User thisUser = getCurrentUser(request);
        String fileNumberId = request.getParameter("fileNumberId");
        String lcl3pRefId = request.getParameter("lcl3pRefId");
        LclQuoteImportAmsDAO lclQuoteImportAmsDAO = new LclQuoteImportAmsDAO();
        LclQuoteImportAms lclQuoteImportAms = lclQuoteImportAmsDAO.findById(Long.parseLong(lcl3pRefId));
        lclQuoteImportAmsDAO.delete(lclQuoteImportAms);
        Long fileId = Long.parseLong(fileNumberId);
        lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, "Deleted - AMS/HBL # -->" + lclQuoteImportAms.getAmsNo().toUpperCase(), thisUser.getUserId());
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("amsHBLList", lclQuoteImportAmsDAO.findAll(Long.parseLong(fileNumberId)));
        }
        return mapping.findForward("ImpAmsHBL");
    }
    
    public ActionForward closeInbond(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileNumberId");
        new LclInbondsDAO().inbondDetails(Long.parseLong(fileId), request);
        return mapping.findForward("inbondIcon");
    }
    
    public ActionForward updateBillToCode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        User loginUser = getCurrentUser(request);
        String billToParty = request.getParameter("billtoCodeImports");
        String exitbillToParty = request.getParameter("exitBillToCode");
        String billingType = request.getParameter("pcBothImports");
        String existBillingType = request.getParameter("existBillingType");
        StringBuilder notes = new StringBuilder();
        if (existBillingType == null ? billingType != null : !existBillingType.equals(billingType)) {//checking terms
            notes.append("Terms -> ").append(existBillingType).append(" to ").append(billingType).append(" , ");//notes
        }
        lclQuoteDAO.updateBillToParty(Long.parseLong(lclQuoteForm.getFileNumberId()), billingType, billToParty, "", "", loginUser.getUserId()); //update bill to party and billing
        LclQuote lclQuote = lclQuoteDAO.getByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
        notes.append("Bill to Code -> ").append(exitbillToParty).append(" to ").append(billToParty);//notes
        lclRemarksDAO.insertLclRemarks(Long.parseLong(lclQuoteForm.getFileNumberId()), REMARKS_QT_AUTO_NOTES, notes.toString(), loginUser.getUserId());
        lclQuoteAcDAO.updateChargesByBillToParty(Long.parseLong(lclQuoteForm.getFileNumberId()), billToParty, loginUser.getUserId(), "");
        List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), LCL_IMPORT);
        importQuoteUtils.setWeighMeasureForImpQuote(request, lclCommodityList);
        importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclCommodityList, lclQuote.getBillingType());
        request.setAttribute("lclQuote", lclQuote);
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward validateQuoteComplete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        List<LclBookingVoyageBean> upcomingSailings = new ArrayList<LclBookingVoyageBean>();
        String fileNoId = quoteForm.getFileNumberId();
        LclQuoteAcDAO quoteAcDao = new LclQuoteAcDAO();
        String errorMessage = null;
        String hasAutoRates = null;
        String module = quoteForm.getModuleName();
        if (fileNoId != null && !fileNoId.trim().equals("")) {
            hasAutoRates = quoteAcDao.hasAutoRates(Long.parseLong(fileNoId), quoteForm.getModuleName());
        }
        if (hasAutoRates.equalsIgnoreCase("false")) {
            errorMessage = "Please calculate rates before completing the Quote";
        } else if (module.equalsIgnoreCase("exports") && quoteForm.getNonRated().equalsIgnoreCase("N")) {
            Integer poo = quoteForm.getPortOfOriginId();
            Integer pol = quoteForm.getPortOfLoadingId();
            Integer pod = quoteForm.getPortOfDestinationId();
            Integer fd = quoteForm.getFinalDestinationId();
            LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
            if (CommonUtils.isNotEmpty(poo) && CommonUtils.isNotEmpty(fd)) {
                String cfcl = quoteForm.getCfcl() ? "C" : "E";
                if ("N".equalsIgnoreCase(quoteForm.getRelayOverride())) {
                    LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(pol, pod, quoteForm.getRelayOverride());
                    if (bookingPlanBean != null) {
                        upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(poo, bookingPlanBean.getPol_id(),
                                bookingPlanBean.getPod_id(), fd, "V", bookingPlanBean, cfcl);
                    }
                } else if (CommonUtils.isNotEmpty(pol) && CommonUtils.isNotEmpty(pod)) {
                    LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelayOverride(poo, pol, pod, fd, 0);
                    if (bookingPlanBean != null) {
                        upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(poo, pol, pod, fd, "V", bookingPlanBean, cfcl);
                    }
                }
            }
            if (upcomingSailings.isEmpty()) {
                errorMessage = "Upcoming Voyage is not available for this Quote, Do you want to Continue?";
            }
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }
    
    public ActionForward convertToBkg(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        String fileNumberId = quoteForm.getFileNumberId();
        User user = getCurrentUser(request);
        LclDwr lclDwr = new LclDwr();
        Date today = new Date();
        ActionForward redirectAction = null;
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclFileNumberDAO fileDao = new LclFileNumberDAO();
            LCLQuoteDAO quoteDao = new LCLQuoteDAO();
            LclHazmatDAO hazmatDao = new LclHazmatDAO();
            LclQuotePieceDAO quotePieceDao = new LclQuotePieceDAO();
            LclBookingPieceDAO bkgPieceDao = new LclBookingPieceDAO();
            LclQuoteHazmatDAO quoteHazmatDao = new LclQuoteHazmatDAO();
            QuoteCommodityDetailsDAO quoteCommodityDetailsDao = new QuoteCommodityDetailsDAO();
            CommodityDetailsDAO commodityDetailsDao = new CommodityDetailsDAO();
            LclQuotePieceWhseDAO quotePieceWhseDao = new LclQuotePieceWhseDAO();
            LclBookingPieceWhseDAO bkgPieceWhseDao = new LclBookingPieceWhseDAO();
            LclQuoteAcDAO quoteAcDao = new LclQuoteAcDAO();
            LclCostChargeDAO costChargeDao = new LclCostChargeDAO();
            LclQuotePadDAO quotePadDao = new LclQuotePadDAO();
            LclBookingPadDAO bkgPadDao = new LclBookingPadDAO();
            LCLBookingDAO bkgDao = new LCLBookingDAO();
            LclRemarksDAO remarksDao = new LclRemarksDAO();
            LclBooking bkg = new LclBooking();
            LclBookingPiece bkgPiece;
            LclBookingHazmat bkgHazmat;
            LclBookingPieceWhse bkgPieceWhse;
            LclBookingPieceDetail bkgPieceDetail;
            LclBookingAc bkgAc;
            LclBookingPad bkgPad;
            LclBookingHsCode bookingHsCode;
            Long fileId = Long.parseLong(fileNumberId);
            LclFileNumber fileNumber = fileDao.findById(fileId);
            fileNumber.setState("B");
            fileDao.update(fileNumber);
            LclQuote quote = quoteDao.findById(fileId);
            String quoteType = quote.getQuoteType();
            List<LclQuotePiece> quotePieces = quotePieceDao.findByProperty("lclFileNumber.id", fileId);
            Map<Long, LclBookingPiece> bookingPieceMap = new HashMap();
            boolean isHazmat = false;
            String blueScreenCode;
            if ("B".equalsIgnoreCase(fileNumber.getState())) {
                remarksDao.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_TYPE_AUTO, "Quote is converted to Booking", user.getUserId());
            }
            for (LclQuotePiece quotePiece : quotePieces) {
                bkgPiece = new LclBookingPiece();
                PropertyUtils.copyProperties(bkgPiece, quotePiece);
                bkgPiece.setId(null);
                bkgPiece.setEnteredBy(user);
                bkgPiece.setModifiedBy(user);
                bkgPiece.setEnteredDatetime(today);
                bkgPiece.setModifiedDatetime(today);
                bkgPieceDao.save(bkgPiece);
                if (quotePiece.getCommodityType() != null && quotePiece.getCommodityType().getId() != null) {
                    bookingPieceMap.put(quotePiece.getCommodityType().getId(), bkgPiece);
                }
                if (quotePiece.isHazmat() && !isHazmat) {
                    isHazmat = true;
                }
                List<LclQuoteHazmat> quoteHazmats = quoteHazmatDao.findByFileAndCommodityList(fileId, quotePiece.getId());
                for (LclQuoteHazmat quoteHazmat : quoteHazmats) {
                    bkgHazmat = new LclBookingHazmat();
                    PropertyUtils.copyProperties(bkgHazmat, quoteHazmat);
                    bkgHazmat.setId(null);
                    bkgHazmat.setEnteredDatetime(today);
                    bkgHazmat.setModifiedDatetime(today);
                    bkgHazmat.setEnteredBy(user);
                    bkgHazmat.setModifiedBy(user);
                    bkgHazmat.setLclBookingPiece(bkgPiece);
                    hazmatDao.save(bkgHazmat);
                }
                List<LclQuotePieceDetail> quotePieceDetails = quoteCommodityDetailsDao.findDetailProperty("quotePiece.id", quotePiece.getId());
                for (LclQuotePieceDetail detail : quotePieceDetails) {
                    bkgPieceDetail = new LclBookingPieceDetail();
                    PropertyUtils.copyProperties(bkgPieceDetail, detail);
                    bkgPieceDetail.setId(null);
                    bkgPieceDetail.setEnteredDatetime(today);
                    bkgPieceDetail.setModifiedDatetime(today);
                    bkgPieceDetail.setEnteredBy(user);
                    bkgPieceDetail.setModifiedBy(user);
                    bkgPieceDetail.setLclBookingPiece(bkgPiece);
                    commodityDetailsDao.save(bkgPieceDetail);
                }
                List<LclQuotePieceWhse> quotePieceWhses = quotePieceWhseDao.findByFileAndCommodityList(quotePiece.getId());
                for (LclQuotePieceWhse quotePieceWhse : quotePieceWhses) {
                    bkgPieceWhse = new LclBookingPieceWhse();
                    PropertyUtils.copyProperties(bkgPieceWhse, quotePieceWhse);
                    bkgPieceWhse.setId(null);
                    bkgPieceWhse.setEnteredDatetime(today);
                    bkgPieceWhse.setModifiedDatetime(today);
                    bkgPieceWhse.setEnteredBy(user);
                    bkgPieceWhse.setModifiedBy(user);
                    bkgPieceWhse.setLclBookingPiece(bkgPiece);
                    bkgPieceWhseDao.save(bkgPieceWhse);
                }
                if (!LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType) && null != quote.getPortOfOrigin()) {
                    Integer warehouseId = new WarehouseDAO().getWarehouseId(quote.getPortOfOrigin().getUnLocationCode(), "B");
                    if (CommonUtils.isNotEmpty(warehouseId)) {
                        new LclBookingPieceWhseDAO().insertLclBookingPieceWhse(bkgPiece.getId(), warehouseId, user.getUserId());
                    }
                }
            }
            if (quote.getDocumentation() == null) {
                quote.setDocumentation(Boolean.FALSE);
            }
            PropertyUtils.copyProperties(bkg, quote);
            LclQuoteUtils lclUtil = new LclQuoteUtils();
            bkg.setClientContact(quote.getClientContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getClientContact(), user, fileNumber, "manual") : null);
            
            bkg.setShipContact(quote.getShipContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getShipContact(), user, fileNumber, "shipper") : null);
            
            bkg.setConsContact(quote.getConsContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getConsContact(), user, fileNumber, "consignee") : null);
            
            bkg.setSupContact(quote.getSupContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getSupContact(), user, fileNumber, "supplier") : null);
            
            bkg.setNotyContact(quote.getNotyContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getNotyContact(), user, fileNumber, "notify") : null);
            
            bkg.setFwdContact(quote.getFwdContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getFwdContact(), user, fileNumber, "forwarder") : null);
            
            bkg.setNotify2Contact(quote.getNotify2Contact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForBooking(quote.getNotify2Contact(), user, fileNumber, "Notify2") : null);
            
            bkg.setThirdPartyContact(null);
            bkg.setEnteredBy(user);
            bkg.setOverShortdamaged(quote.getOverShortDamaged());
            bkg.setModifiedBy(user);
            bkg.setEnteredDatetime(today);
            bkg.setModifiedDatetime(today);
            bkg.setPooPickup(quote.getPooDoor());
            bkg.setTerminal(quote.getBillingTerminal());
            bkg.setLclFileNumber(fileNumber);
            if (!"E".equalsIgnoreCase(quoteType)) {
                bkg.setBookingType(quoteType);
            } else {
                bkg.setBookingType(quoteType);
                bkg.setBookedSsHeaderId(CommonUtils.isNotEmpty(quoteForm.getBookedSsHeaderId()) ? new LclSsHeader(quoteForm.getBookedSsHeaderId()) : null);
                bkg.setPooWhseLrdt(CommonUtils.isNotEmpty(quoteForm.getOriginLrdDate())
                        ? DateUtils.parseDate(quoteForm.getOriginLrdDate(), "dd-MMM-yyyy hh:mm a") : null);
                bkg.setFdEta(CommonUtils.isNotEmpty(quoteForm.getFdEtaDate())
                        ? DateUtils.parseDate(quoteForm.getFdEtaDate(), "dd-MMM-yyyy") : null);
            }
            bkgDao.getCurrentSession().clear();
            bkgDao.getCurrentSession().flush();
            bkgDao.save(bkg);
            if (!LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType)) {
                new ExportQuoteUtils().addDefaultHotCodesOfCustomer(quote, user.getUserId());
                new ExportQuoteUtils().setBookingExportFromQuote(quote, fileNumber, request, user);
            }
            if (LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType) || LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(quoteType)) {
                LclQuoteImportDAO quoteImportsDao = new LclQuoteImportDAO();
                LclBookingImportDAO bkgImportsDao = new LclBookingImportDAO();
                LclBookingImportAmsDAO bkgImportsAmsDao = new LclBookingImportAmsDAO();
                LclQuoteImportAmsDAO quoteImportsAmsDao = new LclQuoteImportAmsDAO();
                LclBookingImportAms bkgImportsAms;
                LclQuoteImport quoteImports = quoteImportsDao.findById(fileId);
                LclBookingImport bkgImports = bkgImportsDao.findById(fileId);
                if (null == bkgImports) {
                    bkgImports = new LclBookingImport();
                }
                PropertyUtils.copyProperties(bkgImports, quoteImports);
                bkgImports.setLclFileNumber(fileNumber);
                bkgImports.setFileNumberId(fileNumber.getId());
                bkgImportsDao.saveOrUpdate(bkgImports);
                List<LclQuoteImportAms> quoteImportsAmses = quoteImportsAmsDao.findByProperty("lclFileNumber.id", fileId);
                for (LclQuoteImportAms quoteImportsAms : quoteImportsAmses) {
                    bkgImportsAms = new LclBookingImportAms();
                    PropertyUtils.copyProperties(bkgImportsAms, quoteImportsAms);
                    bkgImportsAms.setId(null);
                    bkgImportsAms.setEnteredDatetime(today);
                    bkgImportsAms.setModifiedDatetime(today);
                    bkgImportsAms.setEnteredBy(user);
                    bkgImportsAms.setModifiedBy(user);
                    bkgImportsAmsDao.save(bkgImportsAms);
                }
            }
            List<LclQuoteAc> quoteAcList = quoteAcDao.getLclCostByFileNumberAsc(fileId, quoteForm.getModuleName());
            for (LclQuoteAc quoteAc : quoteAcList) {
                bkgAc = new LclBookingAc();
                PropertyUtils.copyProperties(bkgAc, quoteAc);
                bkgAc.setId(null);
                bkgAc.setEnteredBy(user);
                bkgAc.setModifiedBy(user);
                bkgAc.setEnteredDatetime(today);
                bkgAc.setModifiedDatetime(today);
                if (!LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType)) {
                    bkgAc.setArBillToParty(quote.getBillingType().equalsIgnoreCase("C")
                            ? "A" : CommonUtils.isEmpty(quote.getBillToParty()) ? "F" : quote.getBillToParty());
                    bkgAc.setApBillToParty(quote.getBillingType().equalsIgnoreCase("C")
                            ? "A" : CommonUtils.isEmpty(quote.getBillToParty()) ? "F" : quote.getBillToParty());
                    
                }
                if (bookingPieceMap != null && bookingPieceMap.size() > 0 && quoteAc != null && quoteAc.getLclQuotePiece() != null
                        && quoteAc.getLclQuotePiece().getCommodityType() != null && quoteAc.getLclQuotePiece().getCommodityType().getId() != null) {
                    bkgAc.setLclBookingPiece(bookingPieceMap.get(quoteAc.getLclQuotePiece().getCommodityType().getId()));
                }
                if (bkgAc != null) {
                    costChargeDao.getCurrentSession().flush();
                    costChargeDao.getCurrentSession().clear();
                    costChargeDao.save(bkgAc);
                    if (!LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType) && null != quoteAc.getArglMapping()
                            && quoteAc.getArglMapping().isDestinationServices()) {
                        LclQuoteDestinationServices destService = new LclQuoteAcDAO().getDestinationCharge(quoteAc.getId(), quoteAc.getLclFileNumber().getId());
                        LclBookingDestinationServices bkgDestService = new LclBookingDestinationServices();
                        PropertyUtils.copyProperties(bkgDestService, destService);
                        bkgDestService.setId(null);
                        bkgDestService.setLclbookingAc(bkgAc);
                        bkgDestService.setLclFileNumber(fileNumber);
                        new BaseHibernateDAO<LclBookingDestinationServices>().saveOrUpdate(bkgDestService);
                    }
                }
            }
            if (LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType)) {
                blueScreenCode = CHARGE_CODE_DOOR;
            } else {
                blueScreenCode = LoadLogisoftProperties.getProperty("InlandBlueScreenChargeCode");
            }
            LclBookingAc bkgAcPickup = costChargeDao.getLclBookingAcByChargeCode(fileId, blueScreenCode);
            List<LclQuotePad> quotePads = quotePadDao.findByProperty("lclFileNumber.id", fileId);
            for (LclQuotePad quotePad : quotePads) {
                bkgPad = new LclBookingPad();
                PropertyUtils.copyProperties(bkgPad, quotePad);
                bkgPad.setId(null);
                if (bkgAcPickup != null) {
                    bkgPad.setLclBookingAc(bkgAcPickup);
                } else {
                    bkgPad.setLclBookingAc(null);
                }
                bkgPadDao.save(bkgPad);
            }
            
            List<LclQuoteHsCode> hsCodeList = new LclQuoteHsCodeDAO().getHsCodeList(fileId);
            if (CommonUtils.isNotEmpty(hsCodeList)) {
                LclBookingHsCodeDAO lclBookingHsCode = new LclBookingHsCodeDAO();
                bookingHsCode = new LclBookingHsCode();
                for (LclQuoteHsCode quoteHsCode : hsCodeList) {
                    bookingHsCode.setLclFileNumber(quoteHsCode.getLclFileNumber());
                    bookingHsCode.setCodes(quoteHsCode.getCodes());
                    bookingHsCode.setNoPieces(quoteHsCode.getNoPieces());
                    bookingHsCode.setWeightMetric(quoteHsCode.getWeightMetric());
                    bookingHsCode.setWeightMetric(quoteHsCode.getWeightMetric());
                    bookingHsCode.setPackageType(quoteHsCode.getPackageType());
                    bookingHsCode.setEnteredBy(quote.getEnteredBy());
                    bookingHsCode.setModifiedBy(quote.getEnteredBy());
                    bookingHsCode.setModifiedDatetime(today);
                    bookingHsCode.setEnteredDatetime(today);
                    lclBookingHsCode.save(bookingHsCode);
                }
            }
            LclConsolidate consolidate = new LclConsolidateDAO().getByProperty("lclFileNumberA.id", fileId);
            if (consolidate == null) {
                new LclConsolidateDAO().insertLCLConsolidation(fileId, fileId, user, today);
            }
            if (!LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType)) {
                Integer dispoId = lclDwr.disposDesc("B");
                BigInteger dispo = bkgDao.checkDisposition(fileId, dispoId);
                if (null == dispo) {
                    lclDwr.cargoRecivedBookingDispo("B", fileId, user.getUserId(), null, null, null);
                }
            }
        }
        return redirectAction;
    }
    
    public ActionForward setColoadCommRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        String forwardPage = "";
        String fileId = request.getParameter("fileNumberId");
        String polUnCode = request.getParameter("polUnCode");
        String podUnCode = request.getParameter("podUnCode");
        String coloadComm = request.getParameter("coloadComm");
        String billingType = request.getParameter("billingType");
        LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        if ("commodity".equalsIgnoreCase(request.getParameter("ratesFlag"))) {
            List<LclQuotePiece> lclQuotePieceList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (lclQuotePieceList != null && !lclQuotePieceList.isEmpty()) {
                LclQuotePiece lclQuotePiece = lclQuotePieceList.get(0);
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", coloadComm);
                lclQuotePiece.setCommodityType(commodityType);
                lclQuotePieceDAO.saveOrUpdate(lclQuotePiece);
            }
            forwardPage = "commodityDesc";
            request.setAttribute("lclCommodityList", lclQuotePieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId)));
        } else {
            if (fileId != null && !"".equalsIgnoreCase(fileId)) {
                lclQuoteAcDAO.deleteLclCostByFileNumber(Long.parseLong(fileId), "I");
            }
            User loginUser = getCurrentUser(request);
            List<LclQuoteAc> chargeList = null;
            LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(fileId));
            LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
            List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
                LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
                PortsDAO portsDAO = new PortsDAO();
                List<String> commodityList = new ArrayList<String>();
                String pooSchnum = "";
                String polSchnum = portsDAO.getShedulenumber(polUnCode);
                String podSchnum = portsDAO.getShedulenumber(podUnCode);
                String fdSchnum = portsDAO.getShedulenumber(lclQuote.getFinalDestination().getUnLocationCode());
                if (null != lclQuote.getPortOfOrigin() && null != lclQuote.getPortOfOrigin().getUnLocationCode()) {
                    pooSchnum = portsDAO.getShedulenumber(lclQuote.getPortOfOrigin().getUnLocationCode());
                }
                commodityList.add(coloadComm);
                List<LclImportsRatesBean> exceptionRatesList = lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
                List<LclImportsRatesBean> importRatesList = lclImportRatesDAO.getImportRates(polSchnum, podSchnum, "1625", coloadComm, billingType);
                lclQuoteImportChargeCalc.calculateQuoteImportRate(importRatesList, lclQuotePiecesList, lclQuote.getBillToParty(),
                        Long.parseLong(fileId), lclQuoteForm.getTransShipMent(), loginUser, exceptionRatesList, request);
            }
            chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(fileId), LCL_IMPORT);
            importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePiecesList);
            importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(fileId), lclQuotePiecesList, "");
            request.setAttribute("chargeList", chargeList);
            request.setAttribute("lclQuote", lclQuote);
            forwardPage = "chargeDesc";
        }
        return mapping.findForward(forwardPage);
    }
    
    public ActionForward calculateImpPodFdRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        User loginUser = getCurrentUser(request);
        String podUnCode = request.getParameter("pod");
        String polUnCode = request.getParameter("pol");
        String originUnCode = request.getParameter("origin");
        String fdUnCode = request.getParameter("destination");
        String fdId = request.getParameter("finalDestinationId");
        String oldFd = request.getParameter("oldDesnCode");
        String ipiIgnoreStatus = "";
        PortsDAO portsDAO = new PortsDAO();
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        List<String> chargeCodeList = new ArrayList<String>();
        boolean hazmatFound = false;
        LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(lclQuoteForm.getFileNumberId()));
        String billingTerms[] = {"C", "P"};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
        if (lclQuotePiecesList != null && !lclQuotePiecesList.isEmpty()) {
            LclQuotePiece lclQuotePiece = lclQuotePiecesList.get(0);
            String oldFdSchnum = portsDAO.getShedulenumber(oldFd);
//            if (!"".equalsIgnoreCase(oldFdSchnum)) {
//                String oldChargeCode = lclImportRatesDAO.getChargeCode(podSchnum, oldFdSchnum, "", lclQuotePiece.getCommodityType().getCode());
//                if (CommonUtils.isNotEmpty(oldChargeCode)) {
//                    String glMapId = new GlMappingDAO().getglMappingIdUsingBlueScreenCode(oldChargeCode, LCL_SHIPMENT_TYPE_IMPORT, LCL_TRANSACTION_TYPE_AR);
//                    lclQuoteAcDAO.deleteChargesByArGlMappingId(Long.parseLong(lclQuoteForm.getFileNumberId()), glMapId, "false");
//                }
//            }

            List lclCommodityList = new ArrayList();
            lclCommodityList.add(lclQuotePiece.getCommodityType().getCode());
            if (lclQuotePiece.isHazmat()) {
                hazmatFound = true;
            }
            if (!hazmatFound) {
                if (!chargeCodeList.contains("1625")) {
                    chargeCodeList.add("1625");
                }
                if (!chargeCodeList.contains("1682")) {
                    chargeCodeList.add("1682");
                }
            }
            String _3LegBlueChargeCode = lclImportRatesDAO.getRatesChargeCode(podSchnum, oldFdSchnum, lclCommodityList);
            if (CommonUtils.isNotEmpty(_3LegBlueChargeCode)) {
                List<String> _3LegChargeCodeList = new ArrayList<String>();
                for (String _3LegCharges : _3LegBlueChargeCode.split(",")) {
                    _3LegChargeCodeList.addAll(Arrays.asList(_3LegCharges));
                }
                List<LclQuoteAc> _3LegChargeList = lclQuoteAcDAO.getRatesByBlueScreenCode(Long.parseLong(lclQuoteForm.getFileNumberId()), Boolean.FALSE, _3LegChargeCodeList);
                if (null != _3LegChargeList && !_3LegChargeList.isEmpty()) {
                    for (LclQuoteAc _3leg : _3LegChargeList) {
                        String deleteCharges = "Deleted--> Code->" + _3leg.getArglMapping().getChargeCode() + " Charge Amount->" + _3leg.getArAmount();
                        lclRemarksDAO.insertLclRemarks(Long.parseLong(lclQuoteForm.getFileNumberId()), REMARKS_QT_AUTO_NOTES, deleteCharges, loginUser.getUserId());
                        lclQuoteAcDAO.delete(_3leg);
                    }
                }
            }
            
            lclQuote.setFinalDestination(new UnLocationDAO().findById(Integer.parseInt(fdId)));
            lclQuoteDAO.getCurrentSession().clear();
            lclQuoteDAO.update(lclQuote);
            
            LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
            String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
            String pooSchnum = portsDAO.getShedulenumber(originUnCode);
            String polSchnum = portsDAO.getShedulenumber(polUnCode);
            List<LclImportsRatesBean> chargesList = new ArrayList();
            List<LclImportsRatesBean> ratesList = new ArrayList();
            if (lclQuotePiece.getCommodityType() != null && lclQuotePiece.getCommodityType().getCode() != null) {
                ipiIgnoreStatus = lclImportRatesDAO.checkNewIpiCost(lclQuotePiecesList.get(lclQuotePiecesList.size() - 1).getCommodityType().getCode());
            } else {
                ipiIgnoreStatus = lclImportRatesDAO.checkNewIpiCost(lclQuotePiece.getCommNo());
            }
            if (!"".equalsIgnoreCase(podSchnum) && !"".equalsIgnoreCase(fdSchnum)
                    && CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode)) {
                chargesList = lclImportRatesDAO.getLCLImportIPICharges(podSchnum, fdSchnum, lclCommodityList, ipiIgnoreStatus, chargeCodeList);
                ratesList.addAll(chargesList);
                if ("Y".equalsIgnoreCase(ipiIgnoreStatus)) {
                    chargesList = lclImportRatesDAO.ignoreIpiRatesTrigger(podSchnum, fdSchnum, lclCommodityList, null, billingTermsList);
                    ratesList.addAll(chargesList);
                }
            } else if (CommonUtils.isNotEmpty(podUnCode) && podUnCode.equalsIgnoreCase(fdUnCode)) {
                chargesList = lclImportRatesDAO.getLCLImportCharges(podSchnum, fdSchnum, lclCommodityList, null, billingTermsList);
                ratesList.addAll(chargesList);
            }
            if (ratesList.size() > 0) {
                List<LclImportsRatesBean> exceptionRatesList = lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, lclCommodityList);
                lclQuoteImportChargeCalc.calculateQuoteImportRate(chargesList, lclQuotePiecesList,
                        lclQuote.getBillToParty(), Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuoteForm.getTransShipMent(), loginUser, exceptionRatesList, request);
            }
        }
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), LCL_IMPORT);
        importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePiecesList);
        importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuotePiecesList, "");
        request.setAttribute("lclQuote", lclQuote);
        return mapping.findForward("chargeDesc");
        
    }
    
    public ActionForward deleteAutoCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<LclQuoteAc> chargeList = null;
        ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        User loginUser = getCurrentUser(request);
        chargeList = lclQuoteAcDAO.getLclCostByFileNumberME(Long.parseLong(lclQuoteForm.getFileNumberId()), false);
        if (CommonUtils.isNotEmpty(chargeList)) {
            for (LclQuoteAc lclQuoteAc : chargeList) {
                if (lclQuoteAc.getArglMapping().getChargeCode() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("DELETED -> Code -> ").append(lclQuoteAc.getArglMapping().getChargeCode());
                    sb.append(" Charge Amount -> ").append(lclQuoteAc.getArAmount());
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(lclQuoteForm.getFileNumberId()), REMARKS_QT_AUTO_NOTES, sb.toString(), loginUser.getUserId());
                    lclCostChargeDAO.delete(lclQuoteAc);
                }
            }
        }
        chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuoteForm.getModuleName());
        List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclQuoteForm.getFileNumberId()));
        importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePiecesList);
        importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuotePiecesList, "");
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward displayTransitTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuoteAc lclQuoteAc = new LclQuoteAcDAO().getLclQuoteAcByChargeCode(Long.parseLong(lclQuoteForm.getFileNumberId()), "OFIMP");
        int transitTime = 0;
        if (lclQuoteAc != null && lclQuoteAc.getLclQuotePiece() != null && lclQuoteAc.getLclQuotePiece().getCommodityType() != null
                && lclQuoteAc.getLclQuotePiece().getCommodityType().getCode() != null) {
            transitTime = new LclQuoteAcDAO().getTransitTime(lclQuoteForm.getPolUnlocationcode(), lclQuoteForm.getPodUnlocationcode(), lclQuoteAc.getLclQuotePiece().getCommodityType().getCode().toString());
        }
        request.setAttribute("transitTime", transitTime);
        return mapping.findForward("transitTime");
    }
    
    public ActionForward getLcl3pReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String FORWARD_PAGE = "";
        String fileId = request.getParameter("fileNumberId");
        String _3PartyName = request.getParameter("thirdPName");
        String _3PRefType = "";
        if (_3PartyName.equals("hotCodes")) {
            _3PRefType = _3PARTY_TYPE_HTC;
            FORWARD_PAGE = "hoteCodes";
        } else if (_3PartyName.equals("customerPo")) {
            _3PRefType = _3PARTY_TYPE_CP;
            FORWARD_PAGE = "customerPo";
        } else if (_3PartyName.equals("ncmNo")) {
            _3PRefType = _3PARTY_TYPE_NCM;
            FORWARD_PAGE = "ncmNo";
        } else if (_3PartyName.equals("tracking")) {
            _3PRefType = _3PARTY_TYPE_TR;
            FORWARD_PAGE = "tracking";
        } else {
            _3PRefType = _3PARTY_TYPE_WH;
            FORWARD_PAGE = "wareHouseDoc";
        }
        if (!"hotCodes".equalsIgnoreCase(_3PartyName)) {
            request.setAttribute("lcl3PList", new Lcl3pRefNoDAO().get3PRefList(Long.parseLong(fileId), _3PRefType));
        } else {
            request.setAttribute("lclHotCodeList", new LclQuoteHotCodeDAO().getHotCodeList(Long.parseLong(fileId)));
        }
        return mapping.findForward(FORWARD_PAGE);
    }
    
    public ActionForward triggerRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
            ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
            String commodityNo = request.getParameter("commodityNo");
            Long fileNumberId = Long.parseLong(lclQuoteForm.getFileNumberId());
            List<LclQuotePiece> lclQuotePieceList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
            if (CommonUtils.isNotEmpty(lclQuotePieceList)) {
                LclQuotePiece lclQuotePiece = lclQuotePieceList.get(0);
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", commodityNo);
                lclQuotePiece.setCommodityType(commodityType);
                lclQuotePieceDAO.saveOrUpdate(lclQuotePiece);
                lclQuotePieceList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
                request.setAttribute("lclCommodityList", lclQuotePieceList);
                JspWrapper jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityQuoteDesc.jsp").include(request, jspWrapper);
                result.put("commodityDesc", jspWrapper.getOutput());
                
                User user = getCurrentUser(request);
                LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                lclQuoteForm.setEnums(lclQuoteForm.getTransShipMent());
                String polUnCode = "";
                String podUnCode = "";
                String fdUnCode = "";
                String originCode = "";
                List<LclQuoteAc> chargeList = null;
                LclQuote lclQuote = lclQuoteDAO.findById(fileNumberId);
                if (CommonUtils.isNotEmpty(lclQuoteForm.getDestination())) {
                    lclQuote.setFinalDestination(unLocationDAO.getUnlocation(lclQuoteForm.getDestination()));
                }
                if (CommonUtils.isNotEmpty(lclQuoteForm.getOrigin())) {
                    lclQuote.setPortOfOrigin(unLocationDAO.getUnlocation(lclQuoteForm.getOrigin()));
                } else {
                    lclQuote.setPortOfOrigin(null);
                }
                if (CommonUtils.isNotEmpty(request.getParameter("pol"))) {
                    lclQuote.setPortOfLoading(unLocationDAO.getUnlocation(request.getParameter("pol")));
                }
                if (CommonUtils.isNotEmpty(request.getParameter("pod"))) {
                    lclQuote.setPortOfDestination(unLocationDAO.getUnlocation(request.getParameter("pod")));
                }
                lclQuoteDAO.saveOrUpdate(lclQuote);
                lclQuoteDAO.getCurrentSession().flush();
                lclQuoteDAO.getCurrentSession().clear();
                fdUnCode = lclQuoteForm.getDestination();
                if (CommonFunctions.isNotNull(lclQuote.getPortOfLoading()) && CommonFunctions.isNotNull(lclQuote.getPortOfLoading().getUnLocationName())) {
                    polUnCode = lclQuote.getPortOfLoading().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclQuote.getPortOfDestination()) && CommonFunctions.isNotNull(lclQuote.getPortOfDestination().getUnLocationName())) {
                    podUnCode = lclQuote.getPortOfDestination().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclQuote.getPortOfOrigin()) && CommonFunctions.isNotNull(lclQuote.getPortOfOrigin().getUnLocationName())) {
                    originCode = lclQuote.getPortOfOrigin().getUnLocationCode();
                }
                lclQuotePieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                lclQuoteImportChargeCalc.ImportRateCalculation(originCode, polUnCode, podUnCode, fdUnCode, lclQuoteForm.getTransShipMent(), lclQuote.getBillingType(), lclQuote.getBillToParty(), fileNumberId, lclQuotePieceList, user, request);
                chargeList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(Long.parseLong(lclQuoteForm.getFileNumberId()), LCL_IMPORT);
                importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, Long.parseLong(lclQuoteForm.getFileNumberId()), lclQuotePieceList, lclQuote.getBillingType());
                request.setAttribute("chargeList", chargeList);
                importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePieceList);
                request.setAttribute("lclQuote", lclQuote);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/quoteChargeDesc.jsp").include(request, jspWrapper);
                result.put("chargeDesc", jspWrapper.getOutput());
                Gson gson = new Gson();
                out.print(gson.toJson(result));
            }
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
    
    public void addExternalRemarks(LCLQuoteForm lCLQuoteForm, LclFileNumber lclFileNumber,
            User loginUser, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_EXTERNAL_COMMENT);
        if (CommonUtils.isNotEmpty(lCLQuoteForm.getExternalComment())) {
            if (lclRemarks == null) {
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, "Inserted-> External Comments -> " + lCLQuoteForm.getExternalComment().toUpperCase(), loginUser);
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_TYPE_EXTERNAL_COMMENT, lCLQuoteForm.getExternalComment().toUpperCase(), loginUser);
            } else if (lclRemarks != null && !lCLQuoteForm.getExternalComment().equalsIgnoreCase(lclRemarks.getRemarks())) {
                String remarks = "Updated-> External Comments -> " + lclRemarks.getRemarks().toUpperCase() + " to " + lCLQuoteForm.getExternalComment().toUpperCase();
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, remarks, loginUser);
                lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_EXTERNAL_COMMENT, lCLQuoteForm.getExternalComment().toUpperCase(), loginUser);
            } else if (lclRemarks != null && lCLQuoteForm.getExternalComment().equalsIgnoreCase(lclRemarks.getRemarks())) {
                lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_EXTERNAL_COMMENT, lCLQuoteForm.getExternalComment().toUpperCase(), loginUser);
            }
        }
        if (lclRemarks != null && (lCLQuoteForm.getExternalComment() == null || "".equals(lCLQuoteForm.getExternalComment()))) {
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, "Deleted-> External Comments -> " + lclRemarks.getRemarks().toUpperCase(), loginUser);
            lclRemarksDAO.delete(lclRemarks);
        }
    }

    /**
     * Dervie pol and pod based on poo and pod Displaying upcoming sailings
     * schedule
     *
     * @return polpod.jsp and lclVoyage.jsp
     */
    public ActionForward getRelayAndUpComingSailings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLQuoteForm quoteForm = (LCLQuoteForm) form;
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            Integer pooId = quoteForm.getPortOfOriginId();
            Integer fdId = quoteForm.getFinalDestinationId();
            String cfcl = quoteForm.getCfcl() ? "C" : "E";
            result = new ExportUtils().setRelayandUpcomingSailings(result, pooId, fdId,
                    quoteForm.getRelayOverride(), "Quote", request, response, cfcl);
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
    
    public ActionForward getRelayAndUpComingSailingsPrevious(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLQuoteForm quoteForm = (LCLQuoteForm) form;
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            Integer pooId = quoteForm.getPortOfOriginId();
            Integer fdId = quoteForm.getFinalDestinationId();
            String cfcl = quoteForm.getCfcl() ? "C" : "E";
            result = new ExportUtils().setRelayandUpcomingSailingsOlder(result, pooId, fdId,
                    quoteForm.getRelayOverride(), quoteForm.getPreviousSailing(), "Quote", request, response, cfcl);
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
    
    public ActionForward getRelayOverrideUpcomingPrevious(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        List<LclBookingVoyageBean> voyageList = null;
        String polStrId = request.getParameter("polId") != null ? request.getParameter("polId") : "";
        Integer polId = Integer.parseInt(polStrId);
        String podIdStr = request.getParameter("podId") != null ? request.getParameter("podId") : "";
        Integer podId = Integer.parseInt(podIdStr);
        String cfcl = quoteForm.getCfcl() ? "C" : "E";
        Integer fdTransTime = 0;
        LclBookingPlanBean relayOverride = new LclBookingPlanDAO().getRelayOverride(polId, polId, podId, podId, fdTransTime);
        if (relayOverride != null) {
            voyageList = new LclBookingPlanDAO().getUpComingSailingsScheduleOlder(polId, polId, podId, podId, "V", relayOverride, quoteForm.getPreviousSailing(), cfcl);
        }
        request.setAttribute("voyageList", voyageList);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }
    
    public ActionForward getRelayOverrideUpcoming(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        List<LclBookingVoyageBean> voyageList = null;
        String polStrId = request.getParameter("polId") != null ? request.getParameter("polId") : "";
        Integer polId = Integer.parseInt(polStrId);
        String podIdStr = request.getParameter("podId") != null ? request.getParameter("podId") : "";
        Integer podId = Integer.parseInt(podIdStr);
        Integer fdTransTime = 0;//CommonUtils.isNotEmpty(quoteForm.getPodfdtt()) ? Integer.parseInt(quoteForm.getPodfdtt()) : 0;
        String cfcl = quoteForm.getCfcl() ? "C" : "E";
        LclBookingPlanBean relayOverride = new LclBookingPlanDAO().getRelayOverride(polId, polId, podId, podId, fdTransTime);
        if (relayOverride != null) {
            voyageList = new LclBookingPlanDAO().getUpComingSailingsSchedule(polId, polId, podId, podId, "V", relayOverride, cfcl);
        }
        request.setAttribute("voyageList", voyageList);
        request.setAttribute("voyageAction", true);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }
    
    public ActionForward calculateRatesWhenSelectCustomer(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLQuoteForm quoteForm = (LCLQuoteForm) form;
            Long fileId = Long.parseLong(quoteForm.getFileNumberId());
            LclQuote quote = new LCLQuoteDAO().getByProperty("lclFileNumber.id", fileId);
            List<LclQuotePiece> commodity_list = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
            List<LclQuoteAc> chargeList = null;
            Map<String, String> result = new HashMap<String, String>();
            JspWrapper jspWrapper = null;
            out = response.getWriter();
            response.setContentType("application/json");
            if (CommonUtils.isNotEmpty(commodity_list)) {
                ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
                commodity_list.get(0).setCommodityType(new commodityTypeDAO().getByProperty("code", request.getParameter("commNo")));
                request.setAttribute("lclCommodityList", commodity_list);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityQuoteDesc.jsp").include(request, jspWrapper);
                result.put("commodityQuoteDesc", jspWrapper.getOutput());
                LclQuotationChargesCalculation quoteChargesCalculation = new LclQuotationChargesCalculation();
                User user = getCurrentUser(request);
                BigDecimal valueOfGoods = CommonUtils.isNotEmpty(quoteForm.getValueOfGoods())
                        ? new BigDecimal(quoteForm.getValueOfGoods()) : BigDecimal.ZERO;
                quote.setModifiedDatetime(new Date());
                request.setAttribute("quote", quote);
                String rateType = quoteForm.getRateType().equalsIgnoreCase("R") ? "Y" : "C";
                if (!quote.getNonRated()) {
                    quoteChargesCalculation.calculateRates(quote.getPortOfOrigin().getUnLocationCode(), quote.getFinalDestination().getUnLocationCode(),
                            quote.getPortOfLoading().getUnLocationCode(), quote.getPortOfDestination().getUnLocationCode(), fileId,
                            commodity_list, user, null, quoteForm.getInsurance(), valueOfGoods, rateType, "C", null, null, null, null,
                            quoteForm.getCalcHeavy(), quoteForm.getDeliveryMetro(), quoteForm.getPcBoth(), null, null, request);
                    chargeList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(fileId, "Exports");
                    exportQuoteUtils.setWeighMeasureForQuote(request, commodity_list, quoteForm.getFdEngmet());
                    exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, Long.parseLong(quoteForm.getFileNumberId()), new LclQuoteAcDAO(),
                            commodity_list, quoteForm.getBillingType(), quoteForm.getFdEngmet(), "No");
                    
                }
                request.setAttribute("chargeList", chargeList);
                request.setAttribute("lclQuote", quote);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/quoteChargeDesc.jsp").include(request, jspWrapper);
                result.put("quoteChargeDesc", jspWrapper.getOutput());
                Gson gson = new Gson();
                out.print(gson.toJson(result));
            }
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
    
    public ActionForward calculateTTREVCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            String fileId = request.getParameter("fileNumberId");
            String PortOfLoading = request.getParameter("polUnloc");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            PortsDAO portsDAO = new PortsDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclQuote lclQuote = lclQuoteDAO.findById(longFileId);
            lclQuoteDAO.getCurrentSession().evict(lclQuote);
            List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String engmet = new String();
            String rateType = lclQuote.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclQuote.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            
            Ports portspol = portsDAO.getByProperty("unLocationCode", PortOfLoading);
            if (portspol != null && portspol.getEciportcode() != null && !portspol.getEciportcode().trim().equals("")) {
                polorigin = portspol.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclQuote.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }
            String relay = request.getParameter("relay");
            String pcBoth = request.getParameter("pcBoth");
            LclQuoteAc lclQuoteAc = new LclQuoteAcDAO().getTTCharges(longFileId, false);
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            if (relay.equals("Y") && lclCommodityList != null && lclCommodityList.size() > 0) {
                GlMappingDAO glmappingdao = new GlMappingDAO();
                lclQuote.setInsurance(true);
                String BlueScreenTTRevChgCode = lclCostChargeDAO.BlueScreenTTRevChgCode(lclQuote.getPortOfOrigin().getUnLocationCode(), rateType);
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(BlueScreenTTRevChgCode, "LCLE", "AR");
                User loginuser = getCurrentUser(request);
                if (lclQuoteAc == null) {
                    lclQuoteAc = new LclQuoteAc();
                    lclQuoteAc.setLclFileNumber(lclQuote.getLclFileNumber());
                    lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
                    lclQuoteAc.setBundleIntoOf(false);
                    lclQuoteAc.setPrintOnBl(true);
                    lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                    lclQuoteAc.setArglMapping(glmapping);
                    lclQuoteAc.setEnteredDatetime(new Date());
                    lclQuoteAc.setEnteredBy(loginuser);
                    lclQuoteAc.setArBillToParty(lclQuote.getBillToParty());
                    lclQuoteAc.setApBillToParty(lclQuote.getBillToParty());
                }
                
                lclQuotationChargesCalculation.calculaterelayTTCharge(pooorigin, polorigin, lclCommodityList,
                        engmet, loginuser, longFileId, lclQuoteAc, glmapping, BlueScreenTTRevChgCode, request);
            }
            lclQuote.setModifiedBy(loginUser);
            lclQuote.setModifiedDatetime(new Date());
            lclQuoteDAO.saveOrUpdate(lclQuote);
            request.setAttribute("lclQuote", lclQuote);
            List<LclQuoteAc> chargeList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(longFileId, "Exports");
            exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, longFileId, new LclQuoteAcDAO(), lclCommodityList, lclQuote.getBillingType(), engmet, "No");
            exportQuoteUtils.setWeighMeasureForQuote(request, lclCommodityList, engmet);
        } catch (Exception e) {
            log.info("Error in LCL calculateCharges method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward recaculateBySpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLQuoteForm quoteForm = (LCLQuoteForm) form;
            if (CommonUtils.isNotEmpty(quoteForm.getFileNumberId())) {
                User loginUser = getCurrentUser(request);
                Long fileId = Long.parseLong(quoteForm.getFileNumberId());
                String spotRate = request.getParameter("spotRate");
                boolean isSpotRate = "Y".equalsIgnoreCase(spotRate) ? true : false;
                LclQuote lclQuote = lclQuoteDAO.findById(fileId);
                lclQuote.setSpotRateUom("M");
                lclQuote.setSpotComment(null);
                lclQuote.setSpotRateBottom(false);
                lclQuote.setSpotRate(isSpotRate);
                lclQuote.setSpotOfRate(false);
                lclQuote.setSpotWmRate(null);
                lclQuote.setSpotRateMeasure(null);
                lclQuote.setModifiedBy(loginUser);
                lclQuote.setModifiedDatetime(new Date());
                lclQuoteDAO.saveOrUpdate(lclQuote);
                lclQuoteDAO.getCurrentSession().flush();
                lclQuoteDAO.getCurrentSession().clear();
                out = response.getWriter();
                response.setContentType("application/json");
                Map<String, String> result = new HashMap<String, String>();
                
                List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
                if (isSpotRate) {
                    MessageResources messageResources = getResources(request);
                    String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                    LclQuotePiece commodity = lclQuotePiecesList.isEmpty() ? new LclQuotePiece() : lclQuotePiecesList.get(0);
                    commodity.setCommodityType(new commodityTypeDAO().getByProperty("code", spotRateCommodity));
                    new LclQuotePieceDAO().update(commodity);
                }
                new ExportQuoteUtils().refreshRates(quoteForm, request, fileId, lclQuote, loginUser, lclQuotePiecesList);
                JspWrapper jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/quoteChargeDesc.jsp").include(request, jspWrapper);
                result.put("chargeDesc", jspWrapper.getOutput());
                
                new LclQuoteUtils().setCommodityList(fileId, request);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityQuoteDesc.jsp").include(request, jspWrapper);
                result.put("commodityDesc", jspWrapper.getOutput());
                Gson gson = new Gson();
                out.print(gson.toJson(result));
            }
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
    
    public ActionForward refreshQuoteRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(quoteForm.getFileNumberId()));
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(quoteForm.getFileNumberId()), LCL_EXPORT);
        LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(quoteForm.getFileNumberId()));
        lclQuoteDAO.getCurrentSession().evict(lclQuote);
        String destination = lclQuote.getFinalDestination().getUnLocationCode();
        Ports ports = new PortsDAO().getByProperty("unLocationCode", destination);
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        exportQuoteUtils.setWeighMeasureForQuote(request, lclCommodityList, ports.getEngmet());
        exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request,
                Long.parseLong(quoteForm.getFileNumberId()), lclQuoteAcDAO,
                lclCommodityList, lclQuote.getBillingType(), ports.getEngmet(), "No");
        return mapping.findForward("chargeDesc");
    }
    
    public ActionForward updateEconoEculineQT(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> map = new HashMap<String, String>();
            LCLQuoteForm quoteForm = (LCLQuoteForm) form;
            updateEconoEculineFromComm(quoteForm, request);
            map.put("businessUnit", quoteForm.getBusinessUnit());
            
            request.setAttribute("lclHotCodeList", new LclQuoteHotCodeDAO().getHotCodeList(Long.parseLong(quoteForm.getFileNumberId())));
            JspWrapper jspWrapper = new JspWrapper(response);
            request.getRequestDispatcher("/jsps/LCL/ajaxload/generalInfo/hotcodeList.jsp").include(request, jspWrapper);
            map.put("hotCodesList", jspWrapper.getOutput());
            
            Gson gson = new Gson();
            out.print(gson.toJson(map));
            quoteForm.setEculineCommodity("");
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
    
    public ActionForward addingQuoteClientHotCodes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm quoteForm = (LCLQuoteForm) form;
        LclQuoteHotCodeDAO hotCodeDAO = new LclQuoteHotCodeDAO();
        List<String> acctList = new ArrayList<String>();
        String remarks = "";
        String refValue = request.getParameter("refValue");
        String acctNo = request.getParameter("accountNo");
        if (CommonUtils.isEmpty(acctNo)) {
            if (null != quoteForm.getLclQuote()) {
                if (null != quoteForm.getLclQuote().getClientAcct()) {
                    acctList.add(quoteForm.getLclQuote().getClientAcct().getAccountno());
                }
                if (null != quoteForm.getLclQuote().getShipAcct()) {
                    acctList.add(quoteForm.getLclQuote().getShipAcct().getAccountno());
                }
                if (null != quoteForm.getLclQuote().getFwdAcct()) {
                    acctList.add(quoteForm.getLclQuote().getFwdAcct().getAccountno());
                }
                if (null != quoteForm.getLclQuote().getConsAcct()) {
                    acctList.add(quoteForm.getLclQuote().getConsAcct().getAccountno());
                }
            }
        } else {
            acctList.add(acctNo);
        }
        String str = "";
        if (CommonUtils.isNotEmpty(acctList)) {
            List hotCodeList = new GeneralInformationDAO().getHotCodesForMultieAccount(acctList);
            if (CommonUtils.isNotEmpty(hotCodeList)) {
                for (Object code : hotCodeList) {
                    str = (String) code;
                    boolean isHotCodeNotExist = hotCodeDAO
                            .isHotCodeExist(code.toString(), quoteForm.getFileNumberId());
                    remarks = "Inserted - Hot Code#" + " " + str;
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(quoteForm.getFileNumberId()), REMARKS_QT_AUTO_NOTES, remarks, getCurrentUser(request).getUserId());
                    if (isHotCodeNotExist) {
                        hotCodeDAO.insertQuery(quoteForm.getFileNumberId(),
                                code.toString(), getCurrentUser(request).getUserId());
                    }
                }
            }
        }
        request.setAttribute("lclHotCodeList", hotCodeDAO.findByProperty("lclFileNumber.id",
                Long.parseLong(quoteForm.getFileNumberId())));
        return mapping.findForward("hoteCodes");
    }
    
    public void updateEconoEculineFromComm(LCLQuoteForm quoteForm, HttpServletRequest request) throws Exception {
        LclQuoteHotCodeDAO hotCodeDAO = new LclQuoteHotCodeDAO();
        String ports = !"".equalsIgnoreCase(quoteForm.getPodUnlocationcode())
                ? quoteForm.getPodUnlocationcode() : quoteForm.getUnlocationCode();
        String agentNumber = new LclQuoteAcDAO().getAgentAccountNo(quoteForm.getFileNumberId());
        String defaultAgentSetUp = new LCLPortConfigurationDAO().getDefaultPortSetUpCode(ports);
        String getAgentLevelBrand = new AgencyInfoDAO().getAgentLevelBrand(agentNumber, ports);
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(quoteForm.getFileNumberId()));
        String terminal = lclFileNumber != null
                && lclFileNumber.getLclQuote().getBillingTerminal() != null
                        ? lclFileNumber.getLclQuote().getBillingTerminal().getTrmnum() : "";
        if ("".equalsIgnoreCase(getAgentLevelBrand) && !terminal.equalsIgnoreCase("59")) {
            if ("B".equalsIgnoreCase(defaultAgentSetUp)) {
                
                if ("ECULINE_OR_ECONO_FROM_COMMODITY".equalsIgnoreCase(quoteForm.getEculineCommodity())) {
                    boolean isEculine = new LCLQuoteDAO().checkEculineCommInExport("", ports, lclFileNumber.getId().toString());
                    String EcuOrEci = null != lclFileNumber.getBusinessUnit() ? lclFileNumber.getBusinessUnit() : quoteForm.getBusinessUnit();
                    String systemUser = new UserDAO().getUserInfo("system").getId().toString();
                    if (isEculine && ("ECI".equalsIgnoreCase(EcuOrEci) || "OTI".equalsIgnoreCase(EcuOrEci))) {
                        new LclDwr().updateEconoOREculine("ECU", quoteForm.getFileNumberId(), systemUser, REMARKS_QT_AUTO_NOTES);
                        quoteForm.setBusinessUnit("ECU");
                        LclQuoteHotCode hotCode = hotCodeDAO.getQuoteHotCode(lclFileNumber.getId(), "EBL/ECULINE BILL OF LADING");
                        if (null == hotCode) {
                            hotCodeDAO.insertQuery(lclFileNumber.getId().toString(), "EBL/ECULINE BILL OF LADING", getCurrentUser(request).getUserId());
                        }
                    } else if (!isEculine && "ECU".equalsIgnoreCase(EcuOrEci)) {
                        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                        companyCode = companyCode.equalsIgnoreCase("03") ? "ECI" : "OTI";
                        new LclDwr().updateEconoOREculine(companyCode, quoteForm.getFileNumberId(), systemUser, REMARKS_QT_AUTO_NOTES);
                        quoteForm.setBusinessUnit(companyCode);
                        LclQuoteHotCode hotCode = hotCodeDAO.getQuoteHotCode(lclFileNumber.getId(), "EBL/ECULINE BILL OF LADING");
                        if (null != hotCode) {
                            hotCodeDAO.delete(hotCode);
                        }
                    } else {
                        String previousType = new LclFileNumberDAO().getBusinessUnit(quoteForm.getFileNumberId());
                        if (CommonUtils.isEmpty(previousType)) {
                            new LclFileNumberDAO().updateEconoEculine(quoteForm.getBusinessUnit(), quoteForm.getFileNumberId());
                        }
                    }
                }
            } else {
                String previousType = new LclFileNumberDAO().getBusinessUnit(quoteForm.getFileNumberId());
                if (CommonUtils.isEmpty(previousType)) {
                    new LclFileNumberDAO().updateEconoEculine(quoteForm.getBusinessUnit(), quoteForm.getFileNumberId());
                }
            }
        }
        quoteForm.setEculineCommodity("");
    }

    // for Exports #mantis :13725 
    public ActionForward deleteInlandForExports(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLQuoteForm lclQuoteForm = (LCLQuoteForm) form;
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        User user = getCurrentUser(request);
        LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(fileNumberId));
        String moduleName = LCL_EXPORT;
        String chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        LclQuoteAc lclQuoteAc = lclQuoteAcDAO.getLclQuoteAcByChargeCode(Long.parseLong(fileNumberId), chargeCode);
        if (null != lclQuoteAc) {
            String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, remarks, user.getUserId());
            lclQuoteAcDAO.delete(lclQuoteAc);
            lclQuoteAcDAO.getCurrentSession().flush();
            lclQuoteAcDAO.getCurrentSession().clear();
        }
        String deliveryCargoToCode = request.getParameter("deliveryCargoToCode");
        String deliverCargoToName = request.getParameter("deliverCargoToName");
        String deliverCargoToAddress = request.getParameter("deliverCargoToAddress");
        String deliverCargoToCity = request.getParameter("deliverCargoToCity");
        String deliverCargoToState = request.getParameter("deliverCargoToState");
        String deliverCargoToZip = request.getParameter("deliverCargoToZip");
        String deliverCargoToPhone = request.getParameter("deliverCargoToPhone");
        lclQuotePadDAO.updateDeliveryContact(lclQuote, deliverCargoToName, deliverCargoToAddress,
                deliverCargoToCity, deliverCargoToState, deliverCargoToZip, deliverCargoToPhone, deliveryCargoToCode, request);
        List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(Long.parseLong(fileNumberId), moduleName);
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        exportQuoteUtils.setExpChargesDetails(lclQuoteForm, lclQuote, lclCommodityList, chargeList, user, true, request);
        return mapping.findForward(CHARGE_DESC);
    }
    
}
