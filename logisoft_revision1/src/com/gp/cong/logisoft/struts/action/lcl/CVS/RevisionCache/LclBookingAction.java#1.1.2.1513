/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.google.gson.Gson;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.ExportUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclImportUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LCLImportChargeCalc;
import com.gp.cong.lcl.dwr.LclBlChargesCalculation;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.beans.ImpVoyageSearchBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.beans.LclInlandVoyageInfoBean;
import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingDispo;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingHsCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclBookingSegregation;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.CommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.ImportVoyageSearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclApplyDefaultDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlPieceDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclOptionsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.logisoft.lcl.bc.LclBookingUtils;
import com.gp.cong.logisoft.lcl.comparator.LclConsolidateComparator;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.logisoft.lcl.report.OceanManifestBean;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.kn.BookingEnvelopeDao;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBookingForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclCostAndChargeForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.accounting.model.ResultModel;
import com.logiware.common.filter.JspWrapper;
import com.logiware.common.utils.LclImportWarehouseRateUtil;
import com.logiware.edi.entity.PackageDetails;
import com.logiware.edi.entity.Shipment;
import com.logiware.edi.tracking.EdiTrackingSystem;
import com.logiware.edi.tracking.EdiTrackingSystemDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.lcl.dao.LclNotificationDAO;
import com.logiware.thread.LclFileNumberThread;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;

/**
 *
 * @author Owner
 */
public class LclBookingAction extends LogiwareDispatchAction implements LclCommonConstant, ConstantsInterface {

    private static final Logger log = Logger.getLogger(LclBookingAction.class);
    private static final String LCL_BOOKING_VOYAGE = "lclBookingVoyage";
    private static final String LCL_SPOTRATE = "lclSpotRate";
    private static final String LCL_BOOKING_PLAN = "lclBookingPlan";
    private static String CHARGE_DESC = "chargeDesc";
    private static String COMMODITY_DESC = "commodityDesc";
    private static String CONSOLIDATE_DESC = "consolidateDesc";
    private LclUtils lclUtils = new LclUtils();
    private LclBookingUtils lclBookingUtils = new LclBookingUtils();

    public ActionForward newBooking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String FRWD_PAGE = "";
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclUtils.clearLCLSession(lclSession);
        session.setAttribute("lclSession", lclSession);
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclDwr lcldwr = new LclDwr();
        User loginUser = getCurrentUser(request);
        String terminalLocation = loginUser.getTerminal().getTerminalLocation();
        if (terminalLocation.equals("QUEENS, NY")) {
            lclBookingForm.setRtdTransaction("N");
        }
        Boolean defaultERT = new RoleDutyDAO().getRoleDetails("lcl_booking_default_ert", loginUser.getRole().getRoleId());
        if (defaultERT) {
            lclBookingForm.setRtdTransaction("N");
        }
        RoleDuty roleDuty = checkRoleDuty(request);
        LclContact lclContact = new LclContact();
        LclBooking lclBooking = new LclBooking();
        if (LCL_EXPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
            if (roleDuty.isChangeDefaultFF()) {
                TradingPartner fwdTp = new TradingPartnerDAO().findById("NOFFAA0001");
                CustomerAddress custAdd = fwdTp.getPrimaryCustAddr();
                lclBooking.setFwdAcct(fwdTp);
                lclContact.setFax1(custAdd.getFax());
                lclContact.setCompanyName(fwdTp.getAccountName());
                lclContact.setPhone1(custAdd.getPhone());
                lclBooking.setFwdContact(lclContact);
            }
            LclBookingExport lclBookingExport = new LclBookingExport();
            //   lclBookingExport.setNoBlRequired(false);
            request.setAttribute("lclBookingExport", lclBookingExport);
            request.setAttribute("allowDisposition", new RoleDutyDAO().getRoleDetails("allow_change_disposition", loginUser.getRole().getRoleId()));
            if (null != loginUser.getTerminal()) {
                lclBookingForm.setTerminal(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
                lclBookingForm.setTrmnum(loginUser.getTerminal().getTrmnum());
                if (terminalLocation.equals("QUEENS, NY")) {
                    String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                    if (companyCode.equalsIgnoreCase("03")) {
                        lclBookingForm.setBusinessUnit("ECI");
                    }
                }
            }
            FRWD_PAGE = "exportBooking";
        } else {
            //commented due to 11433 mantis item
//            if (null != loginUser.getImportTerminal()) {
//                lclBookingForm.setTerminal(loginUser.getImportTerminal().getTrmnum() + " - " + loginUser.getImportTerminal().getTerminalLocation());
//                lclBookingForm.setTrmnum(loginUser.getImportTerminal().getTrmnum());
//            }
            lclBookingForm.setPdfDocumentName("Pre Advice");
            FRWD_PAGE = "importBooking";
        }
        lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
        lclBooking.setEnteredBy(loginUser);
        lclBooking.setEnteredDatetime(DateUtils.formatDateAndParseDate(new Date()));
        request.setAttribute("lclBooking", lclBooking);
        lclBookingForm.setShipperToolTip(lcldwr.getContactDetails("Shipper", "", "", ""));
        lclBookingForm.setConsigneeToolTip(lcldwr.getContactDetails("Consignee", "", "", ""));
        lclBookingForm.setNotifyToolTip(lcldwr.getContactDetails("Notify", "", "", ""));
        lclBookingForm.setNotify2ToolTip(lcldwr.getContactDetails("Notify2", "", "", ""));
        request.setAttribute("lclBookingForm", lclBookingForm);
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        request.setAttribute("error", false);
        lclSession.setCommodityList(null);
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward editBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        LclBooking lclBooking = null;
        User loginUser = getCurrentUser(request);
        String engmet = new String();
        String module = null, FRWD_PAGE = null;
        Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
        if (LCL_IMPORT.equalsIgnoreCase(bookingForm.getModuleName())) {
            module = LCL_IMPORT;
            FRWD_PAGE = "importBooking";
        } else {
            module = LCL_EXPORT;
            FRWD_PAGE = "exportBooking";
        }
        bookingForm.setModuleName(module);
        if (CommonUtils.isNotEmpty(fileId)) {
            lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", fileId);
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            lclBooking.setClientContact(lclBooking.getClientContact());
            lclBooking.setShipContact(lclBooking.getShipContact());
            lclBooking.setFwdContact(lclBooking.getFwdContact());
            lclBooking.setConsContact(lclBooking.getConsContact());
            lclBooking.setSupContact(lclBooking.getSupContact());
            lclBooking.setNotyContact(lclBooking.getNotyContact());
            lclBooking.setNotify2Contact(lclBooking.getNotify2Contact());
            this.setDoorCityZip(bookingForm, fileId);
        }
        if (LCL_EXPORT.equalsIgnoreCase(module) && LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
            lclBooking.setShipContact(lclBooking.getSupContact());
            lclBooking.setShipAcct(lclBooking.getSupAcct());
        }
        if (LCL_EXPORT.equalsIgnoreCase(module)) {
            request.setAttribute("allowDisposition", new RoleDutyDAO().getRoleDetails("allow_change_disposition", loginUser.getRole().getRoleId()));
        }
        if (LCL_IMPORT.equalsIgnoreCase(module)) {
            if (bookingForm.getLclBooking().getRtdTransaction() != null && bookingForm.getLclBooking().getRtdTransaction() == true) {
                lclBooking.setRtdTransaction(bookingForm.getLclBooking().getRtdTransaction());
            } else if (bookingForm.getLclBooking().getRtdTransaction() != null && bookingForm.getLclBooking().getRtdTransaction() == false) {
                lclBooking.setRtdTransaction(bookingForm.getLclBooking().getRtdTransaction());
            }
        }
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        LclBookingImport lclBookingImport = new LclBookingImportDAO().getByProperty("lclFileNumber.id", fileId);
        // Adding the Port Of Origin Whse Contact Id for Transhipment File.
        if (LCL_IMPORT.equalsIgnoreCase(module) && lclBookingImport != null && lclBookingImport.getForeignPortOfDischarge() != null
                && lclBookingImport.getForeignPortOfDischarge().getUnLocationCode() != null) {
            Object[] fdRemarksObj = new LCLPortConfigurationDAO().lclDefaultDestinationImportRemarks(lclBookingImport.getForeignPortOfDischarge().getUnLocationCode());
            if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                bookingForm.setSpecialRemarks(fdRemarksObj[0].toString().trim());
            }
            if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                bookingForm.setInternalRemarks(fdRemarksObj[1].toString().trim());
            }
            if (fdRemarksObj[2] != null && !fdRemarksObj[2].toString().trim().equals("")) {
                bookingForm.setPortGriRemarks(fdRemarksObj[2].toString().trim());
            }
        }
        if (LCL_EXPORT.equalsIgnoreCase(module)) {
            String ups = String.valueOf(bookingForm.getUps());
            if (ups.equalsIgnoreCase("true")) {
                if (CommonUtils.isNotEmpty(bookingForm.getSmallParcelRemarks())) {
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES,
                            bookingForm.getSmallParcelRemarks(), loginUser.getUserId());
                    bookingForm.setSmallParcelRemarks("");
                }
            }
        }
        if (LCL_IMPORT.equalsIgnoreCase(module)) {
            LclSsDetail lclSsDetail = null;
            LclSsDetailDAO lclSsDetailDAO = new LclSsDetailDAO();
            LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
            LclUnitSsImportsDAO lclUnitSsImportsDAO = new LclUnitSsImportsDAO();
            if (lclBookingPiecesList != null && !lclBookingPiecesList.isEmpty()) {
                LclBookingPiece lclBkPce = (LclBookingPiece) lclBookingPiecesList.get(0);
                LclBookingPieceUnit lclBkPceUt = new LclBookingPieceUnitDAO().getByProperty("lclBookingPiece.id", lclBkPce.getId());
                if (null != lclBkPceUt && null != lclBkPceUt.getLclUnitSs()) {
                    bookingForm.setUnitSsId(lclBkPceUt.getLclUnitSs().getId().toString());
                }
            }
            if (CommonUtils.isNotEmpty(bookingForm.getUnitSsId())) {
                String disposition = "";
                LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(bookingForm.getUnitSsId()));
                request.setAttribute("lclUnitSs", lclUnitSs);
                request.setAttribute("unitSsCollectType", lclUnitSs.getPrepaidCollect());
                LclSsHeader lclssheader = lclSsHeaderDAO.findById(lclUnitSs.getLclSsHeader().getId());
                lclSsDetail = lclSsDetailDAO.findByTransMode(lclssheader.getId(), "V");
                if (null != lclSsDetail) {
                    disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(lclUnitSs.getLclUnit().getId(), lclSsDetail.getId());
                    if (CommonUtils.isNotEmpty(disposition)) {
                        bookingForm.setDisposition(disposition);
                    }
                }
                bookingForm.setHeaderId(lclUnitSs.getLclSsHeader().getId().toString());
                bookingForm.setUnitId(lclUnitSs.getLclUnit().getId().toString());
                bookingForm.setUnitStatus(lclUnitSs.getStatus());
                LclUnitSsImports lclUnitSsImports = lclUnitSsImportsDAO.getLclUnitSSImportsByHeader(lclUnitSs.getLclUnit().getId(), Long.parseLong(bookingForm.getHeaderId()));
                if (CommonFunctions.isNotNull(lclssheader)) {
                    if (CommonFunctions.isNotNull(lclssheader.getScheduleNo())) {
                        bookingForm.setImpEciVoyage(lclssheader.getScheduleNo().toUpperCase());
                    }
                    if (CommonFunctions.isNotNull(lclssheader.getOwnerUserId())) {
                        bookingForm.setVoyageOwner(lclssheader.getOwnerUserId().getLoginName().toUpperCase());
                    }
                    if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                        bookingForm.setImpUnitNo(lclUnitSs.getLclUnit().getUnitNo().toUpperCase());
                    }
                    request.setAttribute("lclssheader", lclssheader);
                    if (CommonFunctions.isNotNull(lclSsDetail)) {
                        if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                            bookingForm.setImpSsVoyage(lclSsDetail.getSpReferenceNo());
                        }
                        if (CommonFunctions.isNotNull(lclSsDetail.getStd())) {
                            bookingForm.setImpSailDate(DateUtils.formatDate(lclSsDetail.getStd(), "dd-MMM-yyyy"));
                        }
                        if (CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo()) && CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo().getAccountName())) {
                            bookingForm.setImpSsLine(lclSsDetail.getSpAcctNo().getAccountName());
                        }
                        if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                            bookingForm.setImpVesselName(lclSsDetail.getSpReferenceName());
                        }
                        if (CommonFunctions.isNotNull(lclSsDetail.getSta())) {
                            bookingForm.setImpVesselArrival(DateUtils.formatDate(lclSsDetail.getSta(), "dd-MMM-yyyy"));
                        }
                        if (CommonFunctions.isNotNull(lclSsDetail.getDeparture())) {
                            bookingForm.setImpPier(lclUtils.getConcatenatedOriginByUnlocation(lclSsDetail.getArrival()));
                        }
                    }
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports)) {
                    if (lclBooking.getPortOfDestination() != null && lclBooking.getFinalDestination() != null) {
                        if (CommonUtils.isEqualIgnoreCase(lclBooking.getPortOfDestination().getUnLocationCode(),
                                lclBooking.getFinalDestination().getUnLocationCode())
                                && !lclBookingImport.getTransShipment()
                                && CommonFunctions.isNotNull(lclUnitSsImports.getLastFreeDate())) {
                            bookingForm.setLastFd(DateUtils.formatDate(lclUnitSsImports.getLastFreeDate(), "dd-MMM-yyyy"));
                        }
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getItDatetime())) {
                        bookingForm.setUnitItDate(DateUtils.formatDate(lclUnitSsImports.getItDatetime(), "dd-MMM-yyyy"));
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getItNo())) {
                        bookingForm.setUnitItNo(lclUnitSsImports.getItNo());
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getItPortId())) {
                        bookingForm.setUnitItPort(lclUtils.getConcatenatedOriginByUnlocation((lclUnitSsImports.getItPortId())));
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getApproxDueDate())) {
                        bookingForm.setImportApproxDue(DateUtils.formatDate(lclUnitSsImports.getApproxDueDate(), "dd-MMM-yyyy"));
                    }
                    String coName = "";
                    CustAddressDAO custAddressDAO = new CustAddressDAO();
                    if ("COLOAD".equalsIgnoreCase(lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase())) {
                        if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo())) {
                            setColoadDevanning(lclUnitSsImports.getColoaderDevanningAcctNo(), bookingForm);
                            if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName())) {
                                bookingForm.setImpCFSWareName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName());
                            }
                            if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno())) {
                                bookingForm.setCfsWarehouseNo(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                                coName = custAddressDAO.getCoName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                                bookingForm.setCfsWarehouseCoName(coName);
                            }
                        }
                    } else if (lclUnitSsImports.getCfsWarehouseId() != null && lclUnitSsImports.getCfsWarehouseId().getId() != null) {
                        bookingForm.setImpCFSWareName(lclUnitSsImports.getCfsWarehouseId().getWarehouseName());
                        bookingForm.setCfsWarehouseNo(lclUnitSsImports.getCfsWarehouseId().getWarehouseNo());
                        coName = custAddressDAO.getCoName(lclUnitSsImports.getCfsWarehouseId().getVendorNo());
                        bookingForm.setCfsWarehouseCoName(coName);
                        bookingForm.setImpCfsWarehsId(String.valueOf(lclUnitSsImports.getCfsWarehouseId().getId()));
                        setCfsWarehouse(lclUnitSsImports.getCfsWarehouseId(), bookingForm);
                    }
                    if (lclUnitSsImports.getUnitWareHouseId() != null && lclUnitSsImports.getUnitWareHouseId().getId() != null) {
                        bookingForm.setImpUnitWareNo(lclUnitSsImports.getUnitWareHouseId().getWarehouseName());
                        setUnitWarehouse(lclUnitSsImports.getUnitWareHouseId(), bookingForm);
                    }
                }
                LclUnitWhse lclUnitWhse = new LclUnitWhseDAO().getLclUnitWhseDetails(lclUnitSs.getLclUnit().getId(), lclssheader.getId());
                if (lclUnitWhse != null) {
                    if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                        bookingForm.setImpStripDate(DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy"));
                    }
                }
                LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclssheader.getId());
                if (lclUnitSsManifest != null) {
                    if (CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                        bookingForm.setMasterBl(lclUnitSsManifest.getMasterbl().toUpperCase());
                    }
                }
                if (null != lclSsDetail) {
                    bookingForm.setPdfDocumentName(new LclNotificationDAO().getDocumentName(lclSsDetail.getId(), lclUnitSs.getLclUnit().getId()));
                }
            }
            if (CommonUtils.isEmpty(bookingForm.getPdfDocumentName())) {
                bookingForm.setPdfDocumentName("Pre Advice");
            }
            request.setAttribute("amsHBLList", new LclBookingImportAmsDAO().findAll(fileId));
        }

        if (LCL_IMPORT.equalsIgnoreCase(module) || LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
            if (CommonFunctions.isNotNull(lclBookingImport)) {
                bookingForm.setIpiLoadedContainer(lclBookingImport.isIpiLoadedContainer());
                bookingForm.setPickedUpBy(lclBookingImport.getPickedUpBy());
                if (lclBookingImport.getUsaPortOfExit() != null) {
                    bookingForm.setPortExitId(lclBookingImport.getUsaPortOfExit().getId());
                    bookingForm.setPortExit(lclUtils.getConcatenatedOriginByUnlocation(lclBookingImport.getUsaPortOfExit()));
                }
                if (lclBookingImport.getForeignPortOfDischarge() != null) {
                    request.setAttribute("foreignunlocationCode", lclBookingImport.getForeignPortOfDischarge().getUnLocationCode());
                    bookingForm.setForeignDischargeId(lclBookingImport.getForeignPortOfDischarge().getId());
                    bookingForm.setForeignDischarge(lclUtils.getConcatenatedOriginByUnlocation(lclBookingImport.getForeignPortOfDischarge()));
                }
                if (lclBookingImport.getLastFreeDateTime() == null) {
                    if (CommonFunctions.isNotNull(bookingForm.getLastFd())) {
                        lclBookingImport.setLastFreeDateTime(DateUtils.parseDate(bookingForm.getLastFd(), "dd-MMM-yyyy"));
                    }
                }
                if (lclBooking != null && LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                    lclBooking.setRateType("F");
                }
                // inserting booking dispostion for transhipmentfile to solve port status of a transhipmentfile
                BigInteger dispo = null;
                if (lclBooking != null && LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                    LclDwr lcldwr = new LclDwr();
                    LCLBookingDAO lCLBookingDAO = new LCLBookingDAO();
                    Integer dispoId = lcldwr.disposDesc("B");
                    dispo = lCLBookingDAO.checkDisposition(lclBooking.getFileNumberId(), dispoId);
                    if (null == dispo) {
                        LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
                        lclBookingDispoDAO.insertBookingDispo(lclBooking.getFileNumberId(), dispoId, loginUser.getUserId());
                    }
                }

//                if (lclBooking.getPortOfDestination() != null && lclBooking.getFinalDestination() != null) {
//                    String relayFalg = lclBooking.getRelayOverride() ? "Y" : "N";
//                    LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
//                    LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(lclBooking.getPortOfDestination().getId(),
//                            lclBooking.getFinalDestination().getId(), "N");
//                    if (bookingPlanBean != null) {
//                        List<LclBookingVoyageBean> upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclBooking.getPortOfDestination().getId(), bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
//                                lclBooking.getFinalDestination().getId(), "V", bookingPlanBean);
//                        request.setAttribute("voyageList", upcomingSailings);
//                    }
//                }
            }
            request.setAttribute("lclBookingImport", lclBookingImport);
        }

        bookingForm.setLclBooking(lclBooking);
        String rateType = "";
        if (lclBooking != null && lclBooking.getRateType() != null) {
            rateType = lclBooking.getRateType();
        }
        if (rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        if (lclBooking.getPortOfOrigin() != null && lclBooking.getPortOfOrigin().getId() != null) {
            bookingForm.setPortOfOriginId(lclBooking.getPortOfOrigin().getId());
        }
        if (lclBooking.getPortOfLoading() != null) {
            bookingForm.setPortOfLoadingId(lclBooking.getPortOfLoading().getId());
        }
        if (lclBooking.getPortOfDestination() != null) {
            bookingForm.setPortOfDestinationId(lclBooking.getPortOfDestination().getId());
        }
        bookingForm.setFinalDestinationId(lclBooking.getFinalDestination().getId());
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            for (LclBookingPiece lbp : lclBookingPiecesList) {
                lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(lclBooking.getFileNumberId(), lbp.getId()));
                lbp.setLclBookingAcList(lclCostChargeDAO.findByFileAndCommodityList(lclBooking.getFileNumberId(), lbp.getId()));
                lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
                lbp.setLclBookingPieceUnitList(new LclBookingPieceUnitDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
                lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
            }
            request.setAttribute("lclCommodityList", lclBookingPiecesList);
        }
        Lcl3pRefNoDAO _3pRefNoDAO = new Lcl3pRefNoDAO();
        List<Lcl3pRefNo> lcl3pNo = _3pRefNoDAO.get3PRefList(fileId, LCL_3PREF_TYPE_OTI);
        bookingForm = setOtiNumber(lcl3pNo, bookingForm);
        setPolPodValues(lclBooking, module, request);//setPolPodValues
        request.setAttribute("lclBooking", lclBooking);
        // request.setAttribute("lclRemarksPriority", lclRemarksDAO.executeQuery("Select remarks FROM LclRemarks WHERE lclFileNumber.id=" + bookingForm.getFileNumberId() + " AND type='Priority View'  ORDER BY modified_datetime DESC"));
        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(lclBooking.getFileNumberId(), module);
        if (chargeList == null || chargeList.isEmpty()) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        }
        String remarkTypes[];
        if (CommonUtils.isEqualIgnoreCase(module, LCL_IMPORT)) {
            String agentNo = "";
            remarkTypes = new String[]{"OSD", "Loading Remarks", "E", "AutoRates", "Priority View", "SU"};
            if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                agentNo = lclBooking.getAgentAcct().getAccountno();
            }
            if (lclBooking.getPortOfLoading() != null && lclBooking.getFinalDestination() != null && !lclBookingPiecesList.isEmpty()) {
                String pooUnCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
                request.setAttribute("totalStorageAmt", new LCLImportChargeCalc().calculateImpAutoRates(lclBooking.getPortOfLoading().getUnLocationCode(),
                        lclBooking.getFinalDestination().getUnLocationCode(), pooUnCode, "", lclBookingPiecesList, "I".equalsIgnoreCase(lclBooking.getBookingType()) ? "N" : "Y"));
            }
            lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, fileId, lclCostChargeDAO, lclBookingPiecesList, bookingForm.getLclBooking().getBillingType(), engmet, agentNo);
            LclBookingSegregation lclBookingSegregation = new LclBookingSegregationDao().getByProperty("childLclFileNumber.id", lclBooking.getFileNumberId());
            if (lclBookingSegregation != null) {
                new DBUtil().releaseLockByRecordIdAndModuleId(lclBookingSegregation.getParentLclFileNumber().getFileNumber(), "LCL FILE", loginUser.getUserId());
                request.setAttribute("lclBookingSegregation", lclBookingSegregation);
            }
        } else {
            remarkTypes = new String[]{"Special Remarks", "Special Remarks Pod", "G", "Gri Remarks Pod", "Internal Remarks",
                "Internal Remarks Pod", "AutoRates", "OSD", "Loading Remarks", "E", "AutoRates", "Priority View"};
            ExportBookingUtils exportBookingUtil = new ExportBookingUtils();
            bookingForm.setWhsCode((null != lclBooking.getPooWhseContact() && null != lclBooking.getPooWhseContact().getWarehouse())
                    ? lclBooking.getPooWhseContact().getWarehouse().getWarehouseNo() : "");
            exportBookingUtil.setExpVoyageDetails(fileId, "E", bookingForm, request);//Set picked on voyage Details
            PortsDAO portsDAO = new PortsDAO();
            lclUtils.getFormattedConsolidatedList(request, lclBooking.getFileNumberId());
            this.setAesDetailsList(lclBooking.getLclFileNumber().getFileNumber(), request);//Set AES Details List
            exportBookingUtil.setUpcomingSailings(lclBooking, request);
            exportBookingUtil.setDataForConsolidationBL(fileId, request); // Set BL Object if file is consolidation
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
            }
            lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, fileId, lclCostChargeDAO,
                    lclBookingPiecesList, lclBooking.getBillingType(), engmet, "No");
            if (CommonUtils.isNotEmpty(bookingForm.getBusinessUnit())
                    && CommonUtils.isNotEmpty(bookingForm.getEculineCommodity())
                    && !"BL".equalsIgnoreCase(lclBooking.getLclFileNumber().getState())) {
                updateEconoEculineBkg(bookingForm, lclBooking.getLclFileNumber(), request);
            } else {
                if (CommonUtils.isEmpty(lclBooking.getLclFileNumber().getBusinessUnit())) {
                    new LclFileNumberDAO().updateEconoEculine(bookingForm.getCompanyCode(), lclBooking.getLclFileNumber().getId().toString());
                } else {
                    bookingForm.setBusinessUnit(lclBooking.getLclFileNumber().getBusinessUnit());

                }
            }
            this.setSportRateValues(lclBooking, engmet, request);//Set SpotRate Values
            String schedK = "";
            String pierName = "";
            String stateCountryCode = "";
            String mouseOverPier = "";
            if (null != lclBooking.getBookedSsHeaderId() && null != lclBooking.getBookedSsHeaderId().getVesselSsDetail()
                    && null != lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture()) {
                if (CommonUtils.isNotEmpty(lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode())) {
                    schedK = portsDAO.getShedulenumber(lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode());
                    stateCountryCode = unLocationDAO.getStateCountryCode(lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode());
                }

                if (CommonUtils.isNotEmpty(lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName())
                        && CommonUtils.isNotEmpty(stateCountryCode)) {
                    pierName = lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName() + "/" + stateCountryCode;
                    if (CommonUtils.isNotEmpty(lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode())) {
                        mouseOverPier = lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName() + "/" + stateCountryCode + "(" + lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode() + ")";
                    }
                }
            }
            request.setAttribute("scheduleKNum", schedK);
            request.setAttribute("pierName", pierName);
            request.setAttribute("mouseOverPier", mouseOverPier);
            request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(Long.parseLong(request.getParameter("fileNumberId"))));
        }
        //addAvalStatusFromTranshipment(mapping, form, request, response);
        lclBookingUtils.getRemarks(bookingForm, request, fileId, remarkTypes, lclRemarksDAO);
        setPoaandCreditStatusValues(lclBooking, module, bookingForm, request);//set Values POA and Credit Status for all Vendors.Client,Shipper,Forwarder,Notify
        lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
        lclUtils.inbondDetails(fileId, request);//set Inbond Details
        request.setAttribute("lcl3PList", _3pRefNoDAO.get3PRefList(fileId, ""));
        request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", fileId));
        request.setAttribute("lclBookingHsCodeList", new LclBookingHsCodeDAO().getHsCodeList(fileId));
        request.setAttribute("error", false);
        request.setAttribute("aesCount", _3pRefNoDAO.isValidateAes(fileId));
//        request.setAttribute("pickVoyId", bookingForm.getPickVoyId());
//        request.setAttribute("detailId", bookingForm.getDetailId());
//        request.setAttribute("filterValues", bookingForm.getFilterByChanges());

        if (LCL_EXPORT.equalsIgnoreCase(bookingForm.getModuleName())
                || "T".equalsIgnoreCase(lclBooking.getBookingType())) {
            LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
            LclBookingExport lclBookingExport = lclBookingExportDAO.getByProperty("lclFileNumber.id", Long.parseLong(bookingForm.getFileNumberId()));
            if (lclBookingExport == null) {
                lclBookingExport = lclBookingExportDAO.save(lclBookingExport, lclBooking.getLclFileNumber(), lclBooking.getEnteredBy());
            }
            if (null != lclBookingExport.getStorageDatetime()) {
                bookingForm.setStorageDatetime(DateUtils.formatDate(lclBookingExport.getStorageDatetime(), "dd-MMM-yyyy hh:mm:ss a"));
            }
            if ((null != lclBooking.getAgentAcct() && null != lclBooking.getAgentAcct().getAccountno()) && null != lclBooking.getPortOfDestination()) {
                bookingForm.setAgentBrand(new AgencyInfoDAO().getAgentLevelBrand(lclBooking.getAgentAcct().getAccountno(), lclBooking.getPortOfDestination().getUnLocationCode()));
            }
            request.setAttribute("lclBookingExport", lclBookingExport);
            List<LclInlandVoyageInfoBean> voyagInfoList = new LCLBookingDAO().getPickedVoyageInfo(bookingForm.getFileNumberId());
            request.setAttribute("voyagInfoList", voyagInfoList);
        }
        if (null != lclBooking && LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
            if (LCL_EXPORT.equalsIgnoreCase(module)) {
                lclBooking.setRtAgentContact(lclBooking.getSupContact());
                lclBooking.setRtdAgentAcct(lclBooking.getSupAcct());
                lclBooking.setRtAgentContact(lclBooking.getSupContact());
                lclBooking.setRtdAgentAcct(lclBooking.getSupAcct());
            }
            String[] billToParty = lclBookingUtils.getFormatBillToCodeImpAndExp(fileId, lclBooking.getBillToParty(), lclBooking.getBillingType(), module);
            lclBooking.setBillToParty(billToParty[0]);
            lclBooking.setBillingType(billToParty[1]);
            lclUtils.setTemBillToPartyList(request, module);
            request.setAttribute("lclBooking", lclBooking);
        }
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setOverRiddedRemarks(null);
        request.setAttribute("lclBookingForm", bookingForm);
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward replicateBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileId = request.getParameter("fileNumberId");
        String moduleName = lclBookingForm.getModuleName();
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        lclBookingForm.setCopyFnVal("Y");
        PortsDAO portsDAO = new PortsDAO();
        String engmet = new String();
        LclFileNumberDAO fileDao = new LclFileNumberDAO();

        if (null == fileId) {
            String fileNo = request.getParameter("fileNumber");
            fileId = String.valueOf(fileDao.getFileIdByFileNumber(fileNo));
        }
        if (fileId != null && !fileId.trim().equals("")) {
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            LclBooking lclBookingReplicate = (LclBooking) BeanUtils.cloneBean(lclBooking);
            lclBookingForm.setReplicateFileNumber(lclBookingReplicate.getLclFileNumber().getFileNumber());
            if ("I".equalsIgnoreCase(lclBookingReplicate.getBookingType())) {
                lclBookingForm.setModuleName("Imports");
            } else {
                lclBookingForm.setModuleName("Exports");
            }
            lclBookingReplicate.setLclFileNumber(null);
            lclBookingReplicate.setFileNumberId(null);
            lclBookingReplicate.setBookingType(null);
            lclBookingReplicate.setEnteredBy(null);
            lclBookingReplicate.setDefaultAgent(true);
            lclBookingReplicate.setSpotRate(false);
            lclBookingReplicate.setConsAcct(null);
            lclBookingReplicate.setConsContact(null);
            lclBookingReplicate.setFwdAcct(null);
            lclBookingReplicate.setFwdContact(null);
            lclBookingReplicate.setBookedSsHeaderId(null);
            lclBookingReplicate.setNotyAcct(null);
            lclBookingReplicate.setNotyContact(null);
            lclBookingReplicate.setShipAcct(null);
            lclBookingReplicate.setShipContact(null);
            lclBookingReplicate.setSupAcct(null);
            lclBookingReplicate.setSupContact(null);
            lclBookingForm.setLclBooking(lclBookingReplicate);
            lclBookingForm.setPortOfOriginId(lclBookingReplicate.getPortOfOrigin().getId());
            lclBookingReplicate.setPooPickup(false);
            lclBookingForm.setFinalDestinationId(lclBookingReplicate.getFinalDestination().getId());
            LclRemarksDAO lclremarksdao = new LclRemarksDAO();
            String lclRemarks = lclremarksdao.getLclRemarksByTypeSQL(fileId, REMARKS_TYPE_INTERNAL_REMARKS);
            lclBookingForm.setInternalRemarks(lclRemarks);
            lclRemarks = lclremarksdao.getLclRemarksByTypeSQL(fileId, REMARKS_TYPE_GRI);
            lclBookingForm.setPortGriRemarks(lclRemarks);
            lclRemarks = lclremarksdao.getLclRemarksByTypeSQL(fileId, REMARKS_TYPE_SPECIAL_REMARKS);
            lclBookingForm.setSpecialRemarks(lclRemarks);
            new ExportBookingUtils().setUpcomingSailings(lclBooking, request);

            if (lclBookingReplicate.getPortOfLoading() != null) {
                request.setAttribute("pol", lclBookingReplicate.getPortOfLoading().getUnLocationName());
            }
            if (lclBookingReplicate.getPortOfDestination() != null) {
                request.setAttribute("pod", lclBookingReplicate.getPortOfDestination().getUnLocationName());
            }
            request.setAttribute("lclBooking", lclBookingReplicate);
            lclBookingForm.setLclBooking(lclBookingReplicate);
            lclBookingForm.setFileNumberId(null);
            request.setAttribute("lclBookingForm", lclBookingForm);
        }
        String FRWD_PAGE = "";
        if (!("imports").equalsIgnoreCase(moduleName)) {
            FRWD_PAGE = "exportBooking";
        } else {
            FRWD_PAGE = "importBooking";
        }
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward backToMainScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        UserDAO userdao = new UserDAO();
        User userfromdb = userdao.findById(user.getUserId());
        String FRWD_PAGE = "";//LCL_IMPORT.equalsIgnoreCase(lclBookingForm.getModuleName()) ? "importMainScreen" : "searchScreen";
        if (userfromdb.isSearchScreenReset()) {
            request.getSession().removeAttribute("lclSearchForm");
        } else {
            request.setAttribute("lclSearchForm", request.getSession().getAttribute("lclSearchForm"));
        }
        if (!LCL_IMPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
            // for setting apply defaults name.
            if (null != userfromdb.getRole()) {
                request.setAttribute("roleQuickBkg", new RoleDutyDAO().getRoleDetails("warehouse_quickBkg", userfromdb.getRole().getRoleId()));
            }
            request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(userfromdb.getUserId()));
            FRWD_PAGE = "searchScreen";
        } else {
            FRWD_PAGE = "importMainScreen";
        }
        request.setAttribute("user", userfromdb);
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward saveBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        HttpSession session = request.getSession();
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        Boolean isNewFile = lclBookingForm.getLclBooking().getLclFileNumber() == null;
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List commodityList = null != lclSession.getCommodityList() ? lclSession.getCommodityList() : new ArrayList();
        String moduleName = lclBookingForm.getModuleName();
        String FRWD_PAGE = LCL_EXPORT.equalsIgnoreCase(moduleName) ? "exportBooking" : "importBooking";
        if ((commodityList.size() > 0 && isNewFile) || !isNewFile || !LCL_EXPORT.equalsIgnoreCase(moduleName)) {
            synchronized (LclBookingAction.class) {
                String engmet = new String();
                Date now = new Date();
                User thisUser = getCurrentUser(request);
                LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
                LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
                if (lclBookingForm.getLclBooking().getEnteredBy() == null) {
                    lclBookingForm.getLclBooking().setEnteredBy(thisUser);
                    lclBookingForm.getLclBooking().setEnteredDatetime(now);
                }
                LclFileNumber lclFileNumber = isNewFile ? createLclFileNumber(lclBookingForm.getShortShip(), lclFileNumberDAO, lclBookingForm, thisUser, now)
                        : lclBookingForm.getLclBooking().getLclFileNumber();
                if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                    lclFileNumberDAO.getSession().getTransaction().begin();
                }

                lclBookingForm.getLclBooking().setLclFileNumber(lclFileNumber);
                lclBookingForm.getLclBooking().setFileNumberId(lclFileNumber.getId());
                lclBookingForm.getLclBooking().getClientContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getClientContact(), request);
                lclBookingForm.getLclBooking().getSupContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getSupContact(), request);
                lclBookingForm.getLclBooking().getShipContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getShipContact(), request);
                lclBookingForm.getLclBooking().getConsContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getConsContact(), request);
                lclBookingForm.getLclBooking().getFwdContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getFwdContact(), request);
                lclBookingForm.getLclBooking().getNotyContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getNotyContact(), request);
                lclBookingForm.getLclBooking().getNotify2Contact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getNotify2Contact(), request);
                lclBookingForm.getLclBooking().getPooWhseContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getPooWhseContact(), request);
//            lclBookingForm.getLclBooking().getThirdPartyContact().setLclFileNumber(lclFileNumber);
//            this.setUserAndDateTime(lclBookingForm.getLclBooking().getThirdPartyContact(), request);
                lclBookingForm.getLclBooking().setModifiedBy(thisUser);
                lclBookingForm.getLclBooking().setModifiedDatetime(now);
                //will go away after setting into form
                if ("Y".equalsIgnoreCase(lclBookingForm.getTransShipMent())
                        && lclBookingForm.getLclBooking().getPortOfDestination() != null) {
                    String[] deliverCargoTo = new LclDwr().getdeliverCargoDetails(lclBookingForm.getLclBooking().getPortOfDestination().getUnLocationCode());
                    if (CommonUtils.isNotEmpty(deliverCargoTo) && CommonUtils.isNotEmpty(deliverCargoTo[0])) {
                        Warehouse deliverWarehouse = new WarehouseDAO().getWareHouseBywarehsNo(deliverCargoTo[0]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setWarehouse(null != deliverWarehouse ? deliverWarehouse : null);
                        lclBookingForm.getLclBooking().getPooWhseContact().setCompanyName(deliverCargoTo[1]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setAddress(deliverCargoTo[2]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setCity(deliverCargoTo[3]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setState(deliverCargoTo[4]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setZip(deliverCargoTo[5]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setPhone1(deliverCargoTo[6]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setFax1(deliverCargoTo[7]);
                    }
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getWhsCode())) {
                    Warehouse deliverWarehouse = new WarehouseDAO().getWareHouseBywarehsNo(lclBookingForm.getWhsCode());
                    lclBookingForm.getLclBooking().getPooWhseContact().setWarehouse(null != deliverWarehouse ? deliverWarehouse : null);
                }
                RoleDuty roleDuty = checkRoleDuty(request);
                String fileno = lclFileNumber.getId().toString();
                if (LCL_EXPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
                    if (roleDuty.isDefaultNoeeiLowVal() && isNewFile) {
                        LclDwr lcldwr = new LclDwr();
                        lcldwr.noEeiAestoAdd(fileno, "booking", request);
                    }
                }
                if (lclBookingForm.getConsigneeManualContact() != null && !lclBookingForm.getConsigneeManualContact().equals("") && lclBookingForm.getNewConsigneeContact() != null && lclBookingForm.getNewConsigneeContact().equals("on")) {
                    lclBookingForm.getLclBooking().getConsContact().setContactName(lclBookingForm.getConsigneeManualContact());
                }
                if (lclBookingForm.getForwarederContactManual() != null && !lclBookingForm.getForwarederContactManual().equals("") && lclBookingForm.getNewForwarderContact() != null && lclBookingForm.getNewForwarderContact().equals("on")) {
                    lclBookingForm.getLclBooking().getFwdContact().setContactName(lclBookingForm.getForwarederContactManual());
                }
                if (lclBookingForm.getShipperManualContact() != null && !lclBookingForm.getShipperManualContact().equals("") && lclBookingForm.getNewShipperContact() != null && lclBookingForm.getNewShipperContact().equals("on")) {
                    lclBookingForm.getLclBooking().getShipContact().setContactName(lclBookingForm.getShipperManualContact());
                }
                if (lclBookingForm.getClientContactManual() != null && !lclBookingForm.getClientContactManual().equals("") && lclBookingForm.getNewClientContact() != null && lclBookingForm.getNewClientContact().equals("on")) {
                    lclBookingForm.getLclBooking().getClientContact().setContactName(lclBookingForm.getClientContactManual());
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getShipperAddress())) {
                    lclBookingForm.getLclBooking().getShipContact().setAddress(lclBookingForm.getShipperAddress());
                }

                if (CommonUtils.isNotEmpty(lclBookingForm.getForwarderAddress())) {
                    lclBookingForm.getLclBooking().getFwdContact().setAddress(lclBookingForm.getForwarderAddress());
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getConsigneeAddress())) {
                    lclBookingForm.getLclBooking().getConsContact().setAddress(lclBookingForm.getConsigneeAddress());
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getNotifyAddress())) {
                    lclBookingForm.getLclBooking().getNotyContact().setAddress(lclBookingForm.getNotifyAddress());
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getSupplierAddress())) {
                    lclBookingForm.getLclBooking().getSupContact().setAddress(lclBookingForm.getSupplierAddress());
                }
                if (CommonUtils.isEmpty(lclBookingForm.getLclBooking().getRateType())) {
                    lclBookingForm.getLclBooking().setRateType("R");
                }
                if (CommonUtils.isEmpty(lclBookingForm.getLclBooking().getBillToParty()) && LCL_EXPORT.equalsIgnoreCase(moduleName)) {
                    lclBookingForm.getLclBooking().setBillToParty("F");
                }
                if (LCL_EXPORT.equalsIgnoreCase(moduleName)) {
                    request.setAttribute("allowDisposition", new RoleDutyDAO().getRoleDetails("allow_change_disposition", thisUser.getRole().getRoleId()));
                }
                if ("Y".equalsIgnoreCase(lclBookingForm.getTransShipMent()) || LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                    LclBookingImportDAO bookingImportDAO = new LclBookingImportDAO();
                    LclBookingImport lclBookingImport = bookingImportDAO.findById(lclFileNumber.getId());
                    if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                        if (lclBookingImport == null) {
                            lclBookingImport = new LclBookingImport();
                            lclBookingImport.setLclFileNumber(lclFileNumber);
                            lclBookingImport.setEnteredDatetime(now);
                            lclBookingImport.setEnteredBy(thisUser);
                            lclBookingImport.setFileNumberId(lclFileNumber.getId());
                            lclBookingImport.setDestWhse(new WarehouseDAO().findById(17));
                        }
                        lclBookingImport.setTransShipment("Y".equalsIgnoreCase(lclBookingForm.getTransShipMent()) ? true : false);
                        lclBookingImport.setSubHouseBl(CommonUtils.isNotEmpty(lclBookingForm.getSubHouseBl())
                                ? lclBookingForm.getSubHouseBl().toUpperCase() : null);
                        lclBookingImport.setEntryNo(CommonUtils.isNotEmpty(lclBookingForm.getEntryNo())
                                ? lclBookingForm.getEntryNo().toUpperCase() : null);
                        Boolean value = lclBookingImport.getDeclaredValueEstimated();
                        Boolean weight = lclBookingImport.getDeclaredWeightEstimated();
                        lclBookingImport.setDeclaredValueEstimated(null != value ? value : false);
                        lclBookingImport.setDeclaredWeightEstimated(null != weight ? weight : false);
                        lclBookingImport.setItNo(lclBookingForm.getItNo());
                        lclBookingImport.setCustomsReleaseNo(lclBookingForm.getCustomsReleaseNo());
                        if (CommonUtils.isEqual(lclBookingForm.getExpressReleaseClasuse(), "Y")) {
                            lclBookingImport.setExpressReleaseClause(true);
                        } else {
                            lclBookingImport.setExpressReleaseClause(false);
                        }
                        if (CommonUtils.isEqual(lclBookingForm.getDoorDeliveryComment(), "Y")) {
                            lclBookingImport.setDoorDeliveryComment(true);
                        } else {
                            lclBookingImport.setDoorDeliveryComment(false);
                        }
                        lclBookingImport.setLastFreeDateTime(CommonUtils.isNotEmpty(lclBookingForm.getLastFd())
                                ? DateUtils.parseDate(lclBookingForm.getLastFd(), "dd-MMM-yyyy") : null);
                        lclBookingImport.setGoDatetime(CommonUtils.isNotEmpty(lclBookingForm.getGoDate())
                                ? DateUtils.parseDate(lclBookingForm.getGoDate(), "dd-MMM-yyyy") : null);
                        lclBookingImport.setIpiATDDate(CommonUtils.isNotEmpty(lclBookingForm.getIpiATDDate())
                                ? DateUtils.parseDate(lclBookingForm.getIpiATDDate(), "dd-MMM-yyyy") : null);
                        lclBookingImport.setIpiLoadNo(CommonUtils.isNotEmpty(lclBookingForm.getIpiLoadNo())
                                ? lclBookingForm.getIpiLoadNo() : null);
                        lclBookingImport.setPickedUpBy(CommonUtils.isNotEmpty(lclBookingForm.getPickedUpBy())
                                ? lclBookingForm.getPickedUpBy() : null);
                        lclBookingImport.setFdEta(CommonUtils.isNotEmpty(lclBookingForm.getEtaFDDate())
                                ? DateUtils.parseDate(lclBookingForm.getEtaFDDate(), "dd-MMM-yyyy") : null);
                        lclBookingImport.setPickupDateTime(CommonUtils.isNotEmpty(lclBookingForm.getPickupDate())
                                ? DateUtils.parseDate(lclBookingForm.getPickupDate(), "dd-MMM-yyyy") : null);
                        if (CommonUtils.isNotEmpty(lclBookingForm.getStGeorgeAccount())) {
                            if (lclBookingImport != null && lclBookingImport.getIpiCfsAcctNo() != null && lclBookingImport.getIpiCfsAcctNo().getAccountName() != null) {
                                if (!lclBookingImport.getIpiCfsAcctNo().getAccountName().equals(lclBookingForm.getStGeorgeAccount())) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("IPI CFS-> (").append(lclBookingImport.getIpiCfsAcctNo().getAccountName().toUpperCase());
                                    stringBuilder.append(" to ").append(lclBookingForm.getStGeorgeAccount()).append(")");
                                    new LclRemarksDAO().insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, stringBuilder.toString(), thisUser);
                                }
                            }
                            lclBookingImport.setIpiCfsAcctNo(new TradingPartnerDAO().findById(lclBookingForm.getStGeorgeAccountNo()));
                        } else {
                            lclBookingImport.setIpiCfsAcctNo(null);
                        }
                        lclBookingImport.setModifiedBy(thisUser);
                        lclBookingImport.setModifiedDatetime(now);
                        lclBookingImport.setIpiLoadedContainer(lclBookingForm.isIpiLoadedContainer());
                    }
                    if ("Y".equalsIgnoreCase(lclBookingForm.getTransShipMent())) {
                        lclBookingForm.getLclBooking().setRateType("F");
                        lclBookingImport.setUsaPortOfExit(CommonUtils.isNotEmpty(lclBookingForm.getPortExit())
                                ? unLocationDAO.findById(lclBookingForm.getPortExitId()) : null);
                        lclBookingImport.setForeignPortOfDischarge(CommonUtils.isNotEmpty(lclBookingForm.getForeignDischargeId())
                                ? unLocationDAO.findById(lclBookingForm.getForeignDischargeId()) : null);
                        if (CommonUtils.isNotEmpty(lclBookingForm.getExportAgentAcctNo())) {
                            lclBookingImport.setExportAgentAcctNo(new TradingPartnerDAO().findById(lclBookingForm.getExportAgentAcctNo()));
                        } else {
                            lclBookingImport.setExportAgentAcctNo(null);
                        }
                        lclBookingForm.getLclBooking().setRelayOverride(true);
                        request.setAttribute("foreignunlocationCode", null != lclBookingImport.getForeignPortOfDischarge()
                                ? lclBookingImport.getForeignPortOfDischarge().getUnLocationCode() : "");
                    }
                    bookingImportDAO.saveOrUpdate(lclBookingImport);
                    lclBookingImport = bookingImportDAO.findById(lclFileNumber.getId());
                    request.setAttribute("lclBookingImport", lclBookingImport);
                    if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                        LclBookingSegregationDao bookingSegregationDao = new LclBookingSegregationDao();
                        LclBookingSegregation lclBookingSegregation = bookingSegregationDao.getByProperty("childLclFileNumber.id", lclFileNumber.getId());
                        request.setAttribute("lclBookingSegregation", lclBookingSegregation);
                        LclBookingImportAms bkgAms = new LclBookingImportAmsDAO().getFirstBkgAms(lclFileNumber.getId());
                        boolean flag = false;
                        if ((bkgAms == null && CommonUtils.isNotEmpty(lclBookingForm.getDefaultAms()))
                                || (bkgAms != null && ((CommonUtils.isNotEmpty(lclBookingForm.getDefaultAms())
                                && (bkgAms.getScac() != null && !bkgAms.getAmsNo().equalsIgnoreCase(lclBookingForm.getDefaultAms())))
                                || (CommonUtils.isNotEmpty(lclBookingForm.getScac()) && (bkgAms.getScac() != null && !bkgAms.getScac().equalsIgnoreCase(lclBookingForm.getScac())))))) {
                            flag = true;
                        }
                        if ((!isNewFile && flag && lclBookingSegregation == null) || (bkgAms != null && bkgAms.getScac() == null && bkgAms.getAmsNo() != null)) {
                            updateAms(lclFileNumber.getId(), thisUser, lclBookingForm);
                        }
                        request.setAttribute("amsHBLList", new LclBookingImportAmsDAO().findAll(lclFileNumber.getId()));
                    }
                }
                //hard code for enum
                lclBookingForm.setEnums(request, lclBookingForm.getModuleName(), lclBookingForm.getTransShipMent());
                if (!moduleName.equalsIgnoreCase("imports")) {
                    this.addSpecialRemarks(lclBookingForm, lclFileNumber, request);
                    this.addPortGriRemarks(lclBookingForm, lclFileNumber, request);
                    this.addInternalRemarks(lclBookingForm, lclFileNumber, request);
                }
                this.addOsd(lclBookingForm, lclFileNumber, thisUser);
                this.addDoorCityZip(lclBookingForm, lclFileNumber, thisUser, now);
                this.addExternal(lclBookingForm, lclFileNumber, getCurrentUser(request), request);
                if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                    this.addSuHeadingNote(lclBookingForm, lclFileNumber, getCurrentUser(request), request);
                }
                if (CommonUtils.isNotEmpty(lclSession.getOverRiddedRemarks())) {
                    lclBookingForm.setRemarksForLoading(lclSession.getOverRiddedRemarks());
                    lclSession.setOverRiddedRemarks(null);
                }
                this.addLoadingRemarks(lclBookingForm, lclFileNumber, request);
                if (!moduleName.equalsIgnoreCase("imports")) {
                    this.addContactNotes(lclBookingForm, lclFileNumber, request);
                }
                Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
                lcl3pRefNoDAO.deleteLcl3PRef(lclFileNumber.getId(), LCL_3PREF_TYPE_OTI);//delete OTI values from 3pRef
                for (Lcl3pRefNo lcl3pRefNo : this.addOtiNumber(lclBookingForm)) {
                    lcl3pRefNo.setLclFileNumber(lclFileNumber);
                    lcl3pRefNo.setEnteredBy(getCurrentUser(request));
                    lcl3pRefNo.setModifiedBy(getCurrentUser(request));
                    lcl3pRefNo.setModifiedDatetime(now);
                    lcl3pRefNo.setEnteredDatetime(now);
                    lcl3pRefNoDAO.save(lcl3pRefNo);
                }

                for (Object obj : commodityList) {
                    LclBookingPieceUnitDAO bkgPieceUnitDao = new LclBookingPieceUnitDAO();
                    LclBookingPiece lbp = (LclBookingPiece) obj;
                    if (CommonUtils.isNotEmpty(lclBookingForm.getCopyFnVal()) && lclBookingForm.getCopyFnVal().equalsIgnoreCase("Y")) {
                        LclBookingPiece lbpNew = new LclBookingPiece();
                        List<LclBookingPieceDetail> detailList = lbp.getLclBookingPieceDetailList();
                        PropertyUtils.copyProperties(lbpNew, lbp);
                        lbpNew.setLclFileNumber(lclFileNumber);
                        lbpNew.setEnteredBy(getCurrentUser(request));
                        lbpNew.setModifiedBy(getCurrentUser(request));
                        lbpNew.setEnteredDatetime(now);
                        lbpNew.setModifiedDatetime(now);
                        lbpNew.setId(null);
                        lbpNew.setLclBookingPieceUnitList(null);
                        lbpNew.setLclBookingHazmatList(null);
                        lbpNew.setLclBookingPieceWhseList(null);
                        lbpNew.setLclBookingPieceDetailList(null);
                        new LclBookingPieceDAO().save(lbpNew);
                        /* set voyage values */
                        if (CommonUtils.isNotEmpty(lclBookingForm.getUnitSsId())) {
                            LclUnitSs lclUnitss = new LclUnitSsDAO().findById(Long.parseLong(lclBookingForm.getUnitSsId()));
                            LclUnitSsImports lclUnitSsImports = new LclUnitSsImportsDAO().getLclUnitSSImportsByHeader(lclUnitss.getLclUnit().getId(), lclUnitss.getLclSsHeader().getId());
                            if ("COLOAD".equalsIgnoreCase(lclUnitss.getLclUnit().getUnitType().getDescription().toUpperCase())) {
                                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo())) {
                                    setColoadDevanning(lclUnitSsImports.getColoaderDevanningAcctNo(), lclBookingForm);
                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName())) {
                                        lclBookingForm.setImpCFSWareName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName());
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno())) {
                                        lclBookingForm.setCfsWarehouseNo(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                                        String coName = new CustAddressDAO().getCoName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                                        lclBookingForm.setCfsWarehouseCoName(coName);
                                    }
                                }
                            } else if (lclUnitSsImports.getCfsWarehouseId() != null && lclUnitSsImports.getCfsWarehouseId().getId() != null) {
                                lclBookingForm.setImpCFSWareName(lclUnitSsImports.getCfsWarehouseId().getWarehouseName());
                                lclBookingForm.setCfsWarehouseNo(lclUnitSsImports.getCfsWarehouseId().getWarehouseNo());
                                lclBookingForm.setImpCfsWarehsId(String.valueOf(lclUnitSsImports.getCfsWarehouseId().getId()));
                                setCfsWarehouse(lclUnitSsImports.getCfsWarehouseId(), lclBookingForm);
                            }
                            if (lclBookingForm.isAllowVoyageCopy() && lclUnitss != null) {
                                LclBookingPieceUnit copyBkgPieceUnit = new LclBookingPieceUnit();
                                copyBkgPieceUnit.setLclBookingPiece(lbpNew);
                                copyBkgPieceUnit.setLclUnitSs(lclUnitss);
                                copyBkgPieceUnit.setLoadedDatetime(now);
                                copyBkgPieceUnit.setEnteredDatetime(now);
                                copyBkgPieceUnit.setModifiedDatetime(now);
                                copyBkgPieceUnit.setModifiedBy(getCurrentUser(request));
                                copyBkgPieceUnit.setEnteredBy(getCurrentUser(request));
                                bkgPieceUnitDao.save(copyBkgPieceUnit);
                            }
                        }
                        /* ends */
                        if (CommonUtils.isNotEmpty(detailList)) {
                            for (Object detailObj : detailList) {
                                LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailObj;
                                LclBookingPieceDetail lbpdNew = new LclBookingPieceDetail();
                                PropertyUtils.copyProperties(lbpdNew, lbpd);
                                lbpdNew.setLclBookingPiece(lbpNew);
                                lbpdNew.setId(null);
                                new CommodityDetailsDAO().save(lbpdNew);
                            }
                        }
                    } else {
                        lbp.setLclFileNumber(lclFileNumber);
                        List<LclBookingPieceDetail> detailList = lbp.getLclBookingPieceDetailList();
                        lbp.setLclBookingPieceDetailList(null);
                        new LclBookingPieceDAO().save(lbp);
                        if (CommonUtils.isEmpty(detailList) && lclSession.getBookingDetailList() != null) {
                            detailList = lclSession.getBookingDetailList();
                        }
                        if (moduleName.equalsIgnoreCase("Imports") && CommonUtils.isNotEmpty(lclBookingForm.getHeaderId()) && CommonUtils.isNotEmpty(lclBookingForm.getUnitSsId())) {
                            lclUtils.insertlclBkgpieceUnit(lbp.getId(), Long.parseLong(lclBookingForm.getUnitSsId()), getCurrentUser(request).getUserId());//Insert Into LclBookingPieceUnit
                        }
                        if (CommonUtils.isNotEmpty(detailList)) {
                            for (LclBookingPieceDetail detail : detailList) {
                                detail.setLclBookingPiece(lbp);
                                new CommodityDetailsDAO().save(detail);
                            }
                        }
                    }

                }
                this.addReplicateNote(lclBookingForm, lclFileNumber, request);
                this.addClient(lclBookingForm, lclFileNumber, request);
                this.addCientName(lclBookingForm, lclFileNumber, request);
                this.addShipperName(lclBookingForm, lclFileNumber, request);
                this.addConsigneeName(lclBookingForm, lclFileNumber, request);
                this.addNotifyName(lclBookingForm, lclFileNumber, request);
                this.addForwarderName(lclBookingForm, lclFileNumber, request);
                if (moduleName.equalsIgnoreCase("imports")) {
                    this.addNotify2Name(lclBookingForm, lclFileNumber, request);
                } else {
                    lclBookingForm.getLclBooking().setNotify2Contact(null);
                }
                lclBookingForm.getLclBooking().setModifiedBy(getCurrentUser(request));
                lclBookingForm.getLclBooking().setModifiedDatetime(now);
                lclBookingDAO.getCurrentSession().clear();
                lclBookingDAO.saveOrUpdate(lclBookingForm.getLclBooking());

                if (!moduleName.equalsIgnoreCase("imports")) {
                    this.addInsertNotesForContact(lclBookingForm, lclFileNumber, request);
                }
                LclBooking lclBookings = lclBookingForm.getLclBooking();

                BigInteger dispo = null;
                String eliteCode = "";
                if (("T".equalsIgnoreCase(lclBookings.getBookingType()) && "B".equalsIgnoreCase(lclBookings.getLclFileNumber().getStatus()))
                        || LCL_EXPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
                    LclDwr lcldwr = new LclDwr();
                    LCLBookingDAO lCLBookingDAO = new LCLBookingDAO();
                    Integer dispoId = lcldwr.disposDesc("B");

                    dispo = lCLBookingDAO.checkDispositionForTranshipment(lclBookings.getFileNumberId());
                    dispo = null != dispo ? dispo : lCLBookingDAO.checkDisposition(lclBookings.getFileNumberId(), dispoId);
                    if (null == dispo) {
                        LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
                        lclBookingDispoDAO.insertBookingDispo(lclBookings.getFileNumberId(), dispoId, thisUser.getUserId());
                    } else {
                        eliteCode = new LclBookingDispoDAO().getDispositionCode(lclBookings.getFileNumberId());
                    }

                    new ExportBookingUtils().setLclBookingExport(lclBookingForm, lclFileNumber, request, thisUser);
                    if (CommonUtils.isEmpty(lclBookingForm.getFileNumberId())) {
                        lclBookingForm.setFileNumberId(lclFileNumber.getId().toString());
                        this.addingClientHotCodes(mapping, lclBookingForm, request, response);
                    }
                    if (CommonUtils.isNotEmpty(lclBookingForm.getSmallParcelRemarks())) {
                        if (lclBookingForm.getUps()) {
                            new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES,
                                    lclBookingForm.getSmallParcelRemarks(), thisUser.getUserId());
                            lclBookingForm.setSmallParcelRemarks("");
                        }
                    }
                }
                if ("B".equalsIgnoreCase(lclBookings.getLclFileNumber().getStatus()) && LCL_IMPORT.equalsIgnoreCase(lclBookingForm.getModuleName())
                        && CommonUtils.isEmpty(lclBookingForm.getFileNumberId())) {
                    lclBookingForm.setFileNumberId(lclFileNumber.getId().toString());
                    this.addingClientHotCodes(mapping, lclBookingForm, request, response);
                }

                lclSession.setCommodityList(null);
                lclSession.setBookingDetailList(null);
                List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId());
                LclBookingAc cafBookingAc = null;
                LclBookingPieceWhseDAO lclBookingPieceWhseDAO = new LclBookingPieceWhseDAO();
                Double totalAmount = 0.00;
                boolean previousComm = false;
                for (LclBookingPiece lbp : lclCommodityList) {
                    if (lclSession.getBookingAcList() != null && lclSession.getBookingAcList().size() > 0) {
                        for (LclBookingAc lclBookingAc : lclSession.getBookingAcList()) {
                            if (lclBookingAc != null) {
                                lclBookingAc.setLclFileNumber(lclFileNumber);
                                lclBookingAc.setLclBookingPiece(lbp);
                                if (LCL_IMPORT.equalsIgnoreCase(moduleName) && CHARGE_CODE_CAF.equalsIgnoreCase(lclBookingAc.getArglMapping().getChargeCode())) {
                                    cafBookingAc = lclBookingAc;
                                } else {
                                    totalAmount = totalAmount + lclBookingAc.getArAmount().doubleValue();
                                    lclBookingAc.setControlNo(lclBookingAc.getControlNo());
                                    lclCostChargeDAO.save(lclBookingAc);
                                }
                                if (moduleName.equalsIgnoreCase("imports") && lclBookingAc.getApAmount() != null && lclBookingAc.getApAmount().doubleValue() > 0.00) {
                                    lclBookingAc.setLclFileNumber(lclFileNumberDAO.findById(lclFileNumber.getId()));
                                }
                            }
                        }
                    }
                    if (cafBookingAc != null) {
                        Double cafAmt = new LCLImportChargeCalc().calculateCAFAmount(cafBookingAc, totalAmount);
                        cafBookingAc.setArAmount(new BigDecimal(cafAmt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                        cafBookingAc.setControlNo(cafBookingAc.getControlNo());
                        lclCostChargeDAO.saveOrUpdate(cafBookingAc);
                    }
                    if ((null == dispo || eliteCode.equalsIgnoreCase("OBKG"))
                            && (("T".equalsIgnoreCase(lclBookings.getBookingType()) && "B".equalsIgnoreCase(lclBookings.getLclFileNumber().getStatus()))
                            || LCL_EXPORT.equalsIgnoreCase(lclBookingForm.getModuleName()))) {
                        if (null != lclBookings.getPortOfOrigin()) {
                            String unlocationCode = "T".equalsIgnoreCase(lclBookings.getBookingType()) ? lclBookings.getPortOfDestination().getUnLocationCode()
                                    : lclBookings.getPortOfOrigin().getUnLocationCode();
                            Integer warehouseId = new WarehouseDAO().getWarehouseId(unlocationCode, "B");
                            if (CommonUtils.isNotEmpty(warehouseId)) {
                                boolean isWarehouseExists = lclBookingPieceWhseDAO.isContainWarehouseId(lbp.getId(), warehouseId);
                                if (!isWarehouseExists) {
                                    lclBookingPieceWhseDAO.insertLclBookingPieceWhse(lbp.getId(), warehouseId, thisUser.getUserId());
                                }
                            }
                        }
                    }
                    lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(lclFileNumber.getId(), lbp.getId()));
                    lbp.setLclBookingAcList(lclCostChargeDAO.findByFileAndCommodityList(lclFileNumber.getId(), lbp.getId()));
                    lbp.setLclBookingPieceWhseList(lclBookingPieceWhseDAO.findByProperty("lclBookingPiece.id", lbp.getId()));
                    lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
                }
                PortsDAO portsDAO = new PortsDAO();
                if (moduleName.equalsIgnoreCase("imports")) {
                    if (lclCommodityList != null && !lclCommodityList.isEmpty()) {
                        LclBookingPiece lclBkPce = (LclBookingPiece) lclCommodityList.get(0);
                        LclBookingPieceUnit lclBkPceUt = new LclBookingPieceUnitDAO().getByProperty("lclBookingPiece.id", lclBkPce.getId());
                        if (null != lclBkPceUt) {
                            LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(lclBkPceUt.getLclUnitSs().getId());
                            if (null != lclUnitSs) {
                                request.setAttribute("unitSsCollectType", lclUnitSs.getPrepaidCollect());
                                request.setAttribute("lclUnitSs", lclUnitSs);
                                lclBookingForm.setHeaderId(lclUnitSs.getLclSsHeader().getId().toString());
                                request.setAttribute("lclssheader", lclUnitSs.getLclSsHeader());
                                lclBookingForm.setUnitSsId(lclUnitSs.getId().toString());
                                lclBookingForm.setUnitId(lclUnitSs.getLclUnit().getId().toString());
                                LclSsDetail lclSsDeatil = new LclSsDetailDAO().findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
                                String disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(lclUnitSs.getLclUnit().getId(), lclSsDeatil.getId());
                                if (CommonUtils.isNotEmpty(disposition)) {
                                    lclBookingForm.setDisposition(disposition);
                                }
                            }
                        } else {
                            lclBookingForm.setHeaderId("");
                        }
                    }
                    if (lclBookings.getSupAcct() != null && lclBookings.getSupAcct().getAccountno() != null && isNewFile) {
                        String newBrand = new TradingPartnerDAO().getBusinessUnit(lclBookings.getSupAcct().getAccountno());
                        new LclFileNumberDAO().updateEconoEculine(newBrand, String.valueOf(lclBookings.getFileNumberId()));
                        lclBookingForm.setFileNumberId(String.valueOf(lclBookings.getFileNumberId()));
                    }
                } else {
                    String schedK = "";
                    String pierName = "";
                    String stateCountryCode = "";
                    String mouseOverPier = "";
                    if (null != lclBookings.getBookedSsHeaderId() && null != lclBookings.getBookedSsHeaderId().getVesselSsDetail()
                            && null != lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture()) {
                        if (CommonUtils.isNotEmpty(lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode())) {
                            schedK = portsDAO.getShedulenumber(lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode());
                            stateCountryCode = unLocationDAO.getStateCountryCode(lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode());
                        }
                        if (CommonUtils.isNotEmpty(lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName())
                                && CommonUtils.isNotEmpty(stateCountryCode)) {
                            pierName = lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName() + "/" + stateCountryCode;
                            if (CommonUtils.isNotEmpty(lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode())) {
                                mouseOverPier = lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName() + "/" + stateCountryCode + "(" + lclBookings.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationCode() + ")";
                            }
                        }
                    }
                    request.setAttribute("scheduleKNum", schedK);
                    request.setAttribute("pierName", pierName);
                    request.setAttribute("mouseOverPier", mouseOverPier);
                }
                request.setAttribute("lclCommodityList", lclCommodityList);
                request.setAttribute("isArGlMappingFlag", lclSession.isIsArGlmappingFlag());
                lclSession.setBookingAcList(null);
                LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", lclFileNumber.getId());
                lclBookingDAO.getCurrentSession().evict(lclBooking);
                setPolPodValues(lclBooking, moduleName, request);//setPolPodValues
                LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
                List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(lclFileNumber.getId(), moduleName);
                request.setAttribute("chargeList", chargeList);
                request.setAttribute("lclBooking", lclBooking);
                request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(lclFileNumber.getId(), ""));
                request.setAttribute("lclBookingHsCodeList", new LclBookingHsCodeDAO().getHsCodeList(lclFileNumber.getId()));
                request.setAttribute("lclRemarksPriority", lclRemarksDAO.executeQuery("Select remarks FROM LclRemarks WHERE lclFileNumber.id=" + lclFileNumber.getId() + " AND type='Priority View'  ORDER BY modified_datetime DESC"));
                if (chargeList == null || chargeList.isEmpty()) {
                    request.setAttribute("rateErrorMessage", "No Rates Found.");
                }
                if (CommonUtils.isEqual(moduleName, LCL_IMPORT)) {
                    String agentNo = "";
                    if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                        agentNo = lclBooking.getAgentAcct().getAccountno();
                    }
                    if (lclBooking.getPortOfLoading() != null && lclBooking.getPortOfDestination() != null) {
                        String pooUnCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
                        request.setAttribute("totalStorageAmt", new LCLImportChargeCalc().calculateImpAutoRates(lclBooking.getPortOfLoading().getUnLocationCode(),
                                lclBooking.getPortOfDestination().getUnLocationCode(), pooUnCode, "", lclCommodityList, "I".equalsIgnoreCase(lclBooking.getBookingType()) ? "N" : "Y"));
                    }
                    lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
                    lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclFileNumber.getId(), lclCostChargeDAO, lclCommodityList, lclBookingForm.getLclBooking().getBillingType(), engmet, agentNo);
                } else {
                    /* update shortShip values in Original DR*/
                    if (CommonUtils.isNotEmpty(lclBookingForm.getShortShip())) {
                        LclBookingPiece bookingPiece = lclCommodityList.get(0);
                        new LclBookingPieceDAO().updateBkgPiece(lclBookingForm.getShortShip(), bookingPiece.getActualPieceCount(),
                                bookingPiece.getActualWeightImperial(), bookingPiece.getActualWeightMetric(),
                                bookingPiece.getActualVolumeImperial(), bookingPiece.getActualVolumeMetric(), thisUser, request);
                        LclBookingDispoDAO bookingDispoDAO = new LclBookingDispoDAO();
                        List<LclBookingDispo> dispoList = bookingDispoDAO.findByProperty("lclFileNumber.id", lclBookingForm.getShortShipFileId());
                        Collections.reverse(dispoList);
                        for (LclBookingDispo dispos : dispoList) {
                            if ("RUNV".equalsIgnoreCase(dispos.getDisposition().getEliteCode())
                                    || "RCVD".equalsIgnoreCase(dispos.getDisposition().getEliteCode())) {
                                LclBookingDispo bookingDispo = new LclBookingDispo();
                                PropertyUtils.copyProperties(bookingDispo, dispos);
                                bookingDispo.setId(null);
                                bookingDispo.setLclUnit(null);
                                bookingDispo.setLclSsDetail(null);
                                bookingDispo.setEnteredBy(thisUser);
                                bookingDispo.setEnteredDatetime(now);
                                bookingDispo.setModifiedBy(thisUser);
                                bookingDispo.setModifiedDatetime(now);
                                bookingDispo.setLclFileNumber(lclFileNumber);
                                bookingDispoDAO.saveOrUpdate(bookingDispo);
                            }
                        }
                        lclBookingForm.setShortShip(null);
                    }

                    ExportBookingUtils exportBookingUtils = new ExportBookingUtils();
                    exportBookingUtils.setExpVoyageDetails(lclBooking.getFileNumberId(), "E", lclBookingForm, request);
                    lclUtils.getFormattedConsolidatedList(request, lclBooking.getFileNumberId());
                    List<LclInlandVoyageInfoBean> pickedVoyageInlandList = new LCLBookingDAO().getPickedVoyageInfo(lclBookingForm.getFileNumberId());
                    request.setAttribute("voyagInfoList", pickedVoyageInlandList);
                    this.setAesDetailsList(lclFileNumber.getFileNumber(), request);//Set AES Details List
                    exportBookingUtils.setUpcomingSailings(lclBooking, request);
                    String rateType = lclBooking.getRateType();
                    if (rateType.equalsIgnoreCase("R")) {
                        rateType = "Y";
                    }
                    Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                    if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                        engmet = ports.getEngmet();
                    }
                    if (CommonUtils.isNotEmpty(lclBookingForm.getBusinessUnit())) {
                        if (CommonUtils.isNotEmpty(lclBookingForm.getEculineCommodity())) {
                            updateEconoEculineBkg(lclBookingForm, lclFileNumber, request);
                        } else {
                            String previousType = new LclFileNumberDAO().getBusinessUnit(lclBookingForm.getFileNumberId());
                            if (CommonUtils.isEmpty(previousType)) {
                                new LclFileNumberDAO().updateEconoEculine(lclBookingForm.getBusinessUnit(), String.valueOf(lclBooking.getFileNumberId()));
                            }
                        }
                    } else {
                        lclBookingForm.setBusinessUnit(lclFileNumber.getBusinessUnit());
                    }
                    lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
                    lclUtils.setRolledUpChargesForBooking(chargeList, request, lclFileNumber.getId(), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
                    LclRemarks lclRemarks = lclRemarksDAO.getRemarks(lclFileNumber.getId(), REMARKS_TYPE_AUTO_RATES, "");
                    if (lclRemarks != null) {
                        request.setAttribute("highVolumeMessage", lclRemarks.getRemarks());
                    }
                    LclConsolidate consolidate = new LclConsolidateDAO().getByProperty("lclFileNumberA.id", lclFileNumber.getId());
                    if (consolidate == null) {
                        new LclConsolidateDAO().insertLCLConsolidation(lclFileNumber.getId(), lclFileNumber.getId(), thisUser, new Date());
                    }
                }
                request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId()));
                setPoaandCreditStatusValues(lclBooking, moduleName, lclBookingForm, request);//set Values POA and Credit Status for all Vendors.Client,Shipper,Forwarder,Notify
                lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
                lclUtils.inbondDetails(lclBooking.getLclFileNumber().getId(), request);//Set Inbond Details
                request.setAttribute("error", false);
                request.setAttribute("aesCount", lcl3pRefNoDAO.isValidateAes(lclFileNumber.getId()));
                this.setSportRateValues(lclBooking, engmet, request);
                if (null != lclBooking && LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                    String[] billToParty = lclBookingUtils.getFormatBillToCodeImpAndExp(lclFileNumber.getId(), lclBooking.getBillToParty(), lclBooking.getBillingType(), lclBookingForm.getModuleName());
                    lclBookingForm.setBillForm(billToParty[0]);
                    lclBookingForm.setBillingType(billToParty[1]);
                    lclUtils.setTemBillToPartyList(request, lclBookingForm.getModuleName());
                }

//            request.setAttribute("pickVoyId", lclBookingForm.getPickVoyId());
//            request.setAttribute("filterValues", lclBookingForm.getFilterByChanges());
                request.setAttribute("checkDRChange", lclBookingForm.getCheckDRChange());
                request.setAttribute("lclBookingForm", lclBookingForm);
                session.setAttribute("lclSession", lclSession);
                lclFileNumberDAO.getSession().getTransaction().commit();
            }
        }
        if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
            lclFileNumberDAO.getSession().getTransaction().begin();
        }
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward displaySpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        LclBooking lclBooking = bookingDAO.findById(Long.parseLong(request.getParameter("fileNumberId")));
        bookingDAO.getCurrentSession().evict(lclBooking);
        if (CommonUtils.isNotEmpty(bookingForm.getDestination())) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", bookingForm.getDestination());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                request.setAttribute("engmet", ports.getEngmet());
            }
            request.setAttribute("destination", bookingForm.getDestination());
        }
        LclCostAndChargeForm lclCostAndChargeForm = new LclCostAndChargeForm();
        lclCostAndChargeForm.setRate(null != lclBooking.getSpotWmRate() ? lclBooking.getSpotWmRate().toString() : "");
        lclCostAndChargeForm.setRateN(null != lclBooking.getSpotRateMeasure() ? lclBooking.getSpotRateMeasure().toString() : "");
        lclCostAndChargeForm.setCheckWM(lclBooking.getSpotRateUom());
        lclCostAndChargeForm.setSpotCheckBottom(lclBooking.getSpotRateBottom());
        lclCostAndChargeForm.setSpotCheckOF(lclBooking.getSpotOfRate());
        lclCostAndChargeForm.setComment(lclBooking.getSpotComment());
        request.setAttribute("lclCostAndChargeForm", lclCostAndChargeForm);
        request.setAttribute("rate", lclBooking.getSpotWmRate());
        request.setAttribute("rateN", lclBooking.getSpotRateMeasure());
        request.setAttribute("checkWM", lclBooking.getSpotRateUom());
        request.setAttribute("comment", lclBooking.getSpotComment());
        return mapping.findForward(LCL_SPOTRATE);
    }

    public ActionForward displayVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        int poo, pol, pod, fd;
        if ("Imports".equalsIgnoreCase(bookingForm.getModuleName())) {
            poo = bookingForm.getPortOfDestinationId();
            fd = bookingForm.getFinalDestinationId();
            pol = bookingForm.getPortExitId();
            pod = bookingForm.getForeignDischargeId();
        } else {
            poo = bookingForm.getPortOfOriginId() != null ? bookingForm.getPortOfOriginId() : 0;
            pol = bookingForm.getPortOfLoadingId() != null ? bookingForm.getPortOfLoadingId() : 0;
            pod = bookingForm.getPortOfDestinationId() != null ? bookingForm.getPortOfDestinationId() : 0;
            fd = bookingForm.getFinalDestinationId();
        }
//        List<LclBookingVoyageBean> voyageList = new LclBookingPlanDAO().getVoyageList(poo, pol, pod, fd, "V");
//        request.setAttribute("voyageList", voyageList);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }

    public ActionForward displayPolPodVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        String relay = request.getParameter("relay");
//        List<LclBookingVoyageBean> voyageList = new LclBookingPlanDAO().getVoyageList(bookingForm.getPortOfOriginId(),
//                bookingForm.getPortOfLoadingId(), bookingForm.getPortOfDestinationId(),
//                bookingForm.getFinalDestinationId(), "V");
//        if (relay.equals("Y")) {
//            voyageList = new LclBookingPlanDAO().getRelayVoyageList(bookingForm.getPortOfLoadingId(),
//                    bookingForm.getPortOfLoadingId(), bookingForm.getPortOfDestinationId(),
//                    bookingForm.getPortOfDestinationId(), "V");
//        } else {
//            if (CommonUtils.isEmpty(voyageList)) {
//                voyageList = new LclBookingPlanDAO().getVoyageList(bookingForm.getPortOfLoadingId(),
//                        bookingForm.getPortOfLoadingId(), bookingForm.getPortOfDestinationId(),
//                        bookingForm.getPortOfDestinationId(), "V");
//            }
//        }

        //  request.setAttribute("voyageList", voyageList);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }

    public ActionForward displayemptyVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("voyageList", null);
        request.setAttribute("error", false);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
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
            LCLBookingForm bookingForm = (LCLBookingForm) form;
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            Integer pooId = bookingForm.getPortOfOriginId();
            Integer fdId = bookingForm.getFinalDestinationId();
            String cfcl = bookingForm.getCfcl() ? "C" : "E";
            result = new ExportUtils().setRelayandUpcomingSailings(result, pooId, fdId,
                    bookingForm.getRelayOverride(), "Booking", request, response, cfcl);
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
            LCLBookingForm bookingForm = (LCLBookingForm) form;
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            Integer pooId = bookingForm.getPortOfOriginId();
            Integer fdId = bookingForm.getFinalDestinationId();
            String cfcl = bookingForm.getCfcl() ? "C" : "E";
            if ("T".equalsIgnoreCase(bookingForm.getLclBooking().getBookingType())) {
                if (bookingForm.getLclBooking().getPortOfDestination() != null && bookingForm.getLclBooking().getFinalDestination() != null) {
                    pooId = bookingForm.getLclBooking().getPortOfDestination().getId();
                    fdId = bookingForm.getLclBooking().getFinalDestination().getId();
                }

            }
            result = new ExportUtils().setRelayandUpcomingSailingsOlder(result, pooId, fdId,
                    bookingForm.getRelayOverride(), bookingForm.getPreviousSailing(), "Booking", request, response, cfcl);
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

    public ActionForward getRelayOverrideUpcoming(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        List<LclBookingVoyageBean> voyageList = null;
        String polStrId = request.getParameter("polId") != null ? request.getParameter("polId") : bookingForm.getPortOfLoadingId().toString();
        Integer polId = Integer.parseInt(polStrId);
        String podIdStr = request.getParameter("podId") != null ? request.getParameter("podId") : bookingForm.getPortOfDestinationId().toString();
        Integer podId = Integer.parseInt(podIdStr);
        Integer fdTransTime = CommonUtils.isNotEmpty(bookingForm.getPodfdtt()) ? Integer.parseInt(bookingForm.getPodfdtt()) : 0;
        LclBookingPlanBean relayOverride = new LclBookingPlanDAO().getRelayOverride(polId, polId, podId, podId, fdTransTime);
        String cfcl = bookingForm.getCfcl() ? "C" : "E";
        if (relayOverride != null) {
            voyageList = new LclBookingPlanDAO().getUpComingSailingsSchedule(polId, polId, podId, podId, "V", relayOverride, cfcl);
        }
        request.setAttribute("voyageList", voyageList);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }

    public ActionForward getRelayOverrideUpcomingPrevious(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        List<LclBookingVoyageBean> voyageList = null;
        String polStrId = request.getParameter("polId") != null ? request.getParameter("polId") : "";
        Integer polId = Integer.parseInt(polStrId);
        String podIdStr = request.getParameter("podId") != null ? request.getParameter("podId") : "";
        Integer podId = Integer.parseInt(podIdStr);
        Integer fdTransTime = CommonUtils.isNotEmpty(bookingForm.getPodfdtt()) ? Integer.parseInt(bookingForm.getPodfdtt()) : 0;
        String cfcl = bookingForm.getCfcl() ? "C" : "E";
        LclBookingPlanBean relayOverride = new LclBookingPlanDAO().getRelayOverride(polId, polId, podId, podId, fdTransTime);
        if (relayOverride != null) {
            voyageList = new LclBookingPlanDAO().getUpComingSailingsScheduleOlder(polId, polId, podId, podId, "V", relayOverride, bookingForm.getPreviousSailing(), cfcl);
        }
        request.setAttribute("voyageList", voyageList);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }

    public ActionForward lclRelayFind(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
//        Integer pooId = bookingForm.getPortOfOriginId();
//        Integer fdId = bookingForm.getFinalDestinationId();
//        String state = "";
//        String state1 = "";
//        LclBookingPlanBean bookingPlanBean = new LclBookingPlanDAO().lclRelayFind(pooId, fdId);
//        if (bookingPlanBean != null && CommonUtils.isNotEmpty(bookingPlanBean.getPolName()) && CommonUtils.isNotEmpty(bookingPlanBean.getPolName())) {
//            UnLocation unlocation = new UnLocationDAO().findById(new Integer(bookingPlanBean.getPolId()));
//            if (unlocation != null && unlocation.getCountryId() != null) {
//                if (unlocation.getCountryId().getCodedesc().equals("UNITED STATES")) {
//                    if (unlocation.getStateId() != null) {
//                        state = unlocation.getStateId().getCode();
//                    }
//                } else {
//                    state = unlocation.getCountryId().getCodedesc();
//                }
//            }
//            unlocation = new UnLocationDAO().findById(new Integer(bookingPlanBean.getPodId()));
//            if (unlocation != null && unlocation.getCountryId() != null) {
//                if (unlocation.getCountryId().getCodedesc().equals("UNITED STATES")) {
//                    if (unlocation.getStateId() != null) {
//                        state1 = unlocation.getStateId().getCode();
//                    }
//                } else {
//                    state1 = unlocation.getCountryId().getCodedesc();
//                }
//            }
//            request.setAttribute("pol", bookingPlanBean.getPolName() + "/" + state + "(" + bookingPlanBean.getPolCode() + ")");
//            request.setAttribute("pod", bookingPlanBean.getPodName() + "/" + state1 + "(" + bookingPlanBean.getPodCode() + ")");
//            request.setAttribute("polUnlocationcode", bookingPlanBean.getPolCode());
//            request.setAttribute("podUnlocationcode", bookingPlanBean.getPodCode());
//            request.setAttribute("portOfLoadingId", bookingPlanBean.getPolId());
//            request.setAttribute("portOfDestinationId", bookingPlanBean.getPodId());
//            if (bookingForm.getLclBooking().getLclFileNumber() != null && bookingForm.getLclBooking().getLclFileNumber().getFileNumber() != null && bookingForm.getLclBooking().getLclFileNumber().getFileNumber() != "") {
//                request.setAttribute("polCode", bookingPlanBean.getPolCode());
//                request.setAttribute("podCode", bookingPlanBean.getPodCode());
//            }
//            request.setAttribute("relaySearch", "RelayAvailable");
//            request.setAttribute("relayOverRide", false);
//        } else {
//            if (!"".equalsIgnoreCase(bookingForm.getPortOfOrigin())) {
//                request.setAttribute("pol", bookingForm.getPortOfOrigin());
//                request.setAttribute("pod", bookingForm.getFinalDestination());
//                request.setAttribute("polUnlocationcode", bookingForm.getOriginUnlocationCode());
//                request.setAttribute("podUnlocationcode", bookingForm.getUnlocationCode());
//                request.setAttribute("portOfLoadingId", bookingForm.getPortOfOriginId());
//                request.setAttribute("portOfDestinationId", bookingForm.getFinalDestinationId());
//                request.setAttribute("relayOverRide", true);
//            }
//            request.setAttribute("relaySearch", "");
//        }
        return mapping.findForward(LCL_BOOKING_PLAN);
    }

    public ActionForward refreshAgent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
            request.setAttribute("polUnlocationcode", lclBookingForm.getPolUnlocationcode());
        } else {
            request.setAttribute("podUnlocationcode", lclBookingForm.getPodUnlocationcode());
        }
        request.setAttribute("foreignunlocationCode", request.getParameter("foreignunlocationCode"));
        return mapping.findForward("refreshAgent");
    }

    public ActionForward refreshImpOriginAgent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String polUnlocationCode = lclBookingForm.getPolUnlocationcode();
        agentCount(polUnlocationCode, request);
        request.setAttribute("polUnlocationcode", lclBookingForm.getPolUnlocationcode());
        return mapping.findForward("refreshOriginAgent");
    }

    public ActionForward clientSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchByValue", request.getParameter("searchByValue"));
        return mapping.findForward("clientSearch");
    }

    public ActionForward getLcl3pReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String FORWARD_PAGE = "";
        String fileId = request.getParameter("fileNumberId");
        String _3PartyName = request.getParameter("thirdPName");
        String _3PRefType = "";
        if (_3PartyName.equals("hotCodes")) {
            _3PRefType = _3PARTY_TYPE_HTC;
            FORWARD_PAGE = "hoteCodes";
            request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId)));
        } else {
            if (_3PartyName.equals("customerPo")) {
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
            request.setAttribute("lcl3PList", new Lcl3pRefNoDAO().get3PRefList(Long.parseLong(fileId), _3PRefType));
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public ActionForward addLcl3pReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String FORWARD_PAGE = "";
        String _3PartyName = request.getParameter("thirdPName");
        String refValue = request.getParameter("refValue");
        String fileNoId = request.getParameter("fileNumberId");
        String fileState = request.getParameter("fileState");
        String insertInbondFlag = request.getParameter("insertInbondFlag");
        String remarks = "";
        if (fileNoId != null && !"".equals(fileNoId)) {
            Long fileId = Long.parseLong(fileNoId);
            String _3PRefType = "";
            Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            User loginUser = getCurrentUser(request);
            if (_3PartyName.equals("hotCodes")) {
                LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
                List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileId);
                if (lclBookingPieceList != null && lclBookingForm.getGenCodefield1() != null
                        && lclBookingForm.getGenCodefield1().equalsIgnoreCase("Y")) {
                    for (LclBookingPiece lclBookingPiece : lclBookingPieceList) {
                        if (!lclBookingPiece.isHazmat()) {
                            lclBookingPiece.setHazmat(Boolean.TRUE);
                            lclBookingPieceDAO.saveOrUpdate(lclBookingPiece);
                        }
                    }
                }
                _3PRefType = _3PARTY_TYPE_HTC;
                String inbDesc = request.getParameter("inbDesc");
                if (CommonUtils.isNotEmpty(inbDesc)) {
                    LclBookingHotCodeDAO hotCodeDAO = new LclBookingHotCodeDAO();
                    String refNo = lcl3pRefNoDAO.getReferenceSize(fileNoId, inbDesc);
                    List<LclInbond> lclInbondList = new LclInbondsDAO().findByProperty("lclFileNumber.id", fileId);
                    if (!lclInbondList.isEmpty() && refNo == null && insertInbondFlag.equalsIgnoreCase("true")) {
                        refValue = inbDesc;
                        Boolean isHotCodeExist = hotCodeDAO.isHotCodeNotExist(refValue, fileId.toString());
                        if (isHotCodeExist) {
                            lclBookingHotCodeDAO.saveHotCode(fileId, refValue.toUpperCase(), getCurrentUser(request).getUserId());
                            remarks = "Inserted - Hot Code#" + " " + refValue.toUpperCase();
                            new LclRemarksDAO().insertLclRemarks((fileId), REMARKS_DR_AUTO_NOTES, remarks.toUpperCase(), loginUser.getUserId());
                        }
                    }
                } else {
                    refValue = lclBookingForm.getHotCodes();
                }

                String hotCodeXXXComments = request.getParameter("hotCodeXXXComments");
                if (CommonUtils.isNotEmpty(hotCodeXXXComments)) {
                    hotCodeXXXComments = "Added Hot Code XXX Comments-->" + hotCodeXXXComments;
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, hotCodeXXXComments.toUpperCase(), loginUser.getUserId());
                    LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", fileId);
                    if (null != bookingExport && null == bookingExport.getReleasedDatetime()) {
                        String desc = "Hold->Y, " + hotCodeXXXComments;
                        LclBooking booking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
                        booking.setHold("Y");
                        new LCLBookingDAO().saveOrUpdate(booking);
                        new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_ON_HOLD_NOTES, desc.toUpperCase(), loginUser.getUserId());
                    }
                }
                String isEculineHotCode = CommonUtils.isNotEmpty(refValue) ? refValue.substring(0, refValue.indexOf("/")) : "";
                if (lclBookingHotCodeDAO.isHotCodeNotExist(refValue, fileNoId)) {
//                    if ("EBL".equalsIgnoreCase(isEculineHotCode) && !"BL".equalsIgnoreCase(fileState)) {
//                        String brandPreferences = null != request.getParameter("brandPreferences") ? request.getParameter("brandPreferences") : "";
//                        if (brandPreferences.equalsIgnoreCase("None")) {
//                            new LclDwr().updateEconoOREculine("ECU", fileId.toString(), loginUser.getUserId().toString());
//                            refValue = lclBookingForm.getHotCodes();
//                        }
//                    }
                    if (CommonUtils.isNotEmpty(refValue) && CommonUtils.isEmpty(insertInbondFlag)) {
                        lclBookingHotCodeDAO.saveHotCode(fileId, refValue.toUpperCase(), getCurrentUser(request).getUserId());
                        remarks = "Inserted - HOT Code#" + " " + refValue;
                        new LclRemarksDAO().insertLclRemarks((fileId), REMARKS_DR_AUTO_NOTES, remarks.toUpperCase(), loginUser.getUserId());
                        _3PRefType = _3PARTY_TYPE_HTC;
                    }
                }
                if ("Exports".equalsIgnoreCase(lclBookingForm.getModuleName())) {
                    String[] remarkTypes = new String[]{REMARK_TYPE_MANUAL, REMARKS_DR_MANUAL_NOTES};
                    LclRemarksDAO remarksDAO = new LclRemarksDAO();
                    Boolean manualFlag = remarksDAO.isRemarks(fileId, remarkTypes);
                    Boolean hotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(fileId, "XXX");
                    manualFlag = !manualFlag ? hotCodeFlag : manualFlag;
                    if (hotCodeFlag) {
                        request.setAttribute("isHotCodeRemarks", remarksDAO.isRemarks(fileId, "ADDED HOT CODE XXX COMMENTS"));
                    }
                    request.setAttribute("hotCodeFlag", manualFlag);
                }
                request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", fileId));
                FORWARD_PAGE = "hoteCodes";
            } else {
                if (_3PartyName.equals("customerPo")) {
                    refValue = lclBookingForm.getCustomerPo();
                    _3PRefType = _3PARTY_TYPE_CP;
                    remarks = "Inserted - Customer PO#" + " " + lclBookingForm.getCustomerPo();
                    FORWARD_PAGE = "customerPo";
                }
                if (_3PartyName.equals("ncmNo")) {
                    refValue = lclBookingForm.getNcmNo();
                    _3PRefType = _3PARTY_TYPE_NCM;
                    remarks = "Inserted - NCM#" + " " + lclBookingForm.getNcmNo();
                    FORWARD_PAGE = "ncmNo";
                }
                if (_3PartyName.equals("tracking")) {
                    refValue = lclBookingForm.getTracking();
                    _3PRefType = _3PARTY_TYPE_TR;
                    remarks = "Inserted - Tracking#" + " " + lclBookingForm.getTracking();
                    FORWARD_PAGE = "tracking";
                }
                if (_3PartyName.equals("wareHouseDoc")) {
                    refValue = lclBookingForm.getWareHouseDoc();
                    _3PRefType = _3PARTY_TYPE_WH;
                    remarks = "Inserted - Whse Doc# " + " " + lclBookingForm.getWareHouseDoc();
                    FORWARD_PAGE = "wareHouseDoc";
                }
                if (CommonUtils.isNotEmpty(refValue)) {
                    lcl3pRefNoDAO.save3pRefNo(fileId, _3PRefType, refValue.toUpperCase());
                }
                request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(fileId, _3PRefType));
                if (fileNoId != null && _3PRefType != null) {
                    new LclRemarksDAO().insertLclRemarks((fileId), REMARKS_DR_AUTO_NOTES, remarks.toUpperCase(), loginUser.getUserId());
                }
            }
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public ActionForward deleteLcl3pReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String FORWARD_PAGE = "";
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String noteId = request.getParameter("thirdPName");
        String fileId = request.getParameter("fileNumberId");
        User loginUser = getCurrentUser(request);
        String remarksType = "", remarks = "";
        if (noteId.equals("hotCodes")) {
            LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
            LclBookingHotCode lclBookingHotCode = lclBookingHotCodeDAO.findById(lclBookingForm.getLcl3pRefId());
            if (null != lclBookingHotCode) {
                String hotCodeXXXComments = request.getParameter("hotCodecomments");
                if (CommonUtils.isNotEmpty(hotCodeXXXComments)) {
                    hotCodeXXXComments = "Deleted Hot Code XXX Comments-->" + hotCodeXXXComments;
                    new LclRemarksDAO().insertLclRemarks(lclBookingHotCode.getLclFileNumber().getId(),
                            REMARKS_DR_AUTO_NOTES, hotCodeXXXComments.toUpperCase(), loginUser.getUserId());
                }
                remarks = "Deleted - HOT Code#" + " " + lclBookingHotCode.getCode();
                remarksType = _3PARTY_TYPE_HTC;
//                String isEculineHotCode = CommonUtils.isNotEmpty(lclBookingHotCode.getCode())
//                        ? lclBookingHotCode.getCode().substring(0, lclBookingHotCode.getCode().indexOf("/")) : "";
//                if ("EBL".equalsIgnoreCase(isEculineHotCode)
//                        && !"BL".equalsIgnoreCase(lclBookingHotCode.getLclFileNumber().getState())) {
//                    String brandPreferences = null != request.getParameter("brandPreferences") ? request.getParameter("brandPreferences") : "";
//                    if (brandPreferences.equalsIgnoreCase("None")) {
//                        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
//                        companyCode = companyCode.equalsIgnoreCase("03") ? "ECI" : "OTI";
//                        lclBookingForm.setBusinessUnit(companyCode);
//                        new LclDwr().updateEconoOREculine(companyCode, fileId, loginUser.getUserId().toString());
//                }
                lclBookingHotCodeDAO.delete(lclBookingHotCode);
                if ("Exports".equalsIgnoreCase(lclBookingForm.getModuleName())) {
                    LclRemarksDAO remarksDAO = new LclRemarksDAO();
                    String[] remarkTypes = new String[]{REMARK_TYPE_MANUAL, REMARKS_DR_MANUAL_NOTES};
                    Boolean manualFlag = remarksDAO.isRemarks(lclBookingHotCode.getLclFileNumber().getId(), remarkTypes);
                    Boolean hotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(lclBookingHotCode.getLclFileNumber().getId(), "XXX");
                    manualFlag = !manualFlag ? hotCodeFlag : manualFlag;
                    if (hotCodeFlag) {
                        request.setAttribute("isHotCodeRemarks", remarksDAO.isRemarks(lclBookingHotCode.getLclFileNumber().getId(),
                                "ADDED HOT CODE XXX COMMENTS"));
                    }

                    request.setAttribute("hotCodeFlag", manualFlag);
                }
            }
            request.setAttribute("lclHotCodeList", lclBookingHotCodeDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId)));
            if (fileId != null && remarksType != null) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks, loginUser.getUserId());
            }
            FORWARD_PAGE = "hoteCodes";
        } else {
            Lcl3pRefNo lcl3pRefNo = lcl3pRefNoDAO.findById(lclBookingForm.getLcl3pRefId());
            if (lcl3pRefNo != null) {
                if (noteId.equals("customerPo")) {
                    remarks = "Deleted - Customer PO#" + " " + lcl3pRefNo.getReference();
                    remarksType = _3PARTY_TYPE_CP;
                    FORWARD_PAGE = "customerPo";
                }
                if (noteId.equals("ncmNo")) {
                    remarks = "Deleted - NCM#" + " " + lcl3pRefNo.getReference();
                    remarksType = _3PARTY_TYPE_NCM;
                    FORWARD_PAGE = "ncmNo";
                }
                if (noteId.equals("tracking")) {
                    remarks = "Deleted - Tracking#" + " " + lcl3pRefNo.getReference();
                    remarksType = _3PARTY_TYPE_TR;
                    FORWARD_PAGE = "tracking";
                }
                if (noteId.equals("wareHouseDoc")) {
                    remarks = "Deleted - Whse Doc# " + " " + lcl3pRefNo.getReference();
                    remarksType = _3PARTY_TYPE_WH;
                    FORWARD_PAGE = "wareHouseDoc";
                }
                if (noteId.equals("AES_ITNNUMBER") || noteId.equals("AES_EXCEPTION")) {
                    remarks = "Deleted - AES/ITN# " + " " + lcl3pRefNo.getReference();
                    remarksType = noteId;
                    FORWARD_PAGE = "aes";
                }
                lcl3pRefNoDAO.delete(lcl3pRefNo);
                request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(Long.parseLong(fileId), remarksType));
            }
            if (fileId != null && remarksType != null) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks, loginUser.getUserId());
            }
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public ActionForward addImpAmsHBL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclBookingImportAmsDAO lclBookingImportAmsDAO = new LclBookingImportAmsDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        Long fileId = Long.parseLong(fileNumberId);
        LclBookingImportAms lclBookingImportAms = new LclBookingImportAms();
        User thisUser = getCurrentUser(request);
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        if (CommonUtils.isNotEmpty(lclBookingForm.getAmsHblNo())) {
            lclBookingImportAms.setAmsNo(lclBookingForm.getAmsHblNo().toUpperCase());
        }
        if (CommonUtils.isNotEmpty(lclBookingForm.getAmsHblPiece())) {
            lclBookingImportAms.setPieces(Integer.parseInt(lclBookingForm.getAmsHblPiece()));
        }
        if (CommonUtils.isNotEmpty(lclBookingForm.getAmsHblScac())) {
            lclBookingImportAms.setScac(lclBookingForm.getAmsHblScac());
        }
        lclBookingImportAms.setLclFileNumber(new LclFileNumber(fileId));
        lclBookingImportAms.setEnteredByUserId(getCurrentUser(request));
        lclBookingImportAms.setModifiedByUserId(getCurrentUser(request));
        lclBookingImportAms.setModifiedDatetime(new Date());
        lclBookingImportAms.setEnteredDatetime(new Date());
        lclBookingImportAmsDAO.saveOrUpdate(lclBookingImportAms);
        // record a note on this.
        lclRemarksDAO.insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "Created - Scac : " + lclBookingImportAms.getScac().toUpperCase() + " and AMS/HBL # " + lclBookingImportAms.getAmsNo().toUpperCase()
                + " with pieces count : " + lclBookingImportAms.getPieces(), thisUser.getUserId());
        lclBookingDAO.updateModifiedDateTime(fileId, thisUser.getUserId());
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("amsHBLList", lclBookingImportAmsDAO.findAll(Long.parseLong(fileNumberId)));
            LclBookingSegregation lclBookingSegregation = new LclBookingSegregationDao().getByProperty("childLclFileNumber.id", Long.parseLong(fileNumberId));
            request.setAttribute("lclBookingSegregation", lclBookingSegregation);
        }
        if (CommonUtils.isNotEmpty(lclBookingForm.getHeaderId())) {
            LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId()));
            request.setAttribute("lclssheader", lclssheader);
        }
        request.setAttribute("lclBookingForm", lclBookingForm);
        request.setAttribute("lclBooking", lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
        return mapping.findForward("ImpAmsHBL");
    }

    public ActionForward updateAms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LCLBookingForm bkgForm = (LCLBookingForm) form;
        User thisUser = getCurrentUser(request);
        String fileIdParam = request.getParameter("fileId");
        Long fileId = Long.parseLong(fileIdParam);
        LclBookingImportAms bkgAms = updateAms(fileId, thisUser, bkgForm);
        lclBookingDAO.updateModifiedDateTime(fileId, thisUser.getUserId());
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(bkgAms.getId());
        return mapping.findForward(null);
    }

    public ActionForward deleteImpAmsHBL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBookingImportAmsDAO lclBookingImportAmsDAO = new LclBookingImportAmsDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LclUtils utils = new LclUtils();
        User thisUser = getCurrentUser(request);
        String fileNumberId = request.getParameter("fileNumberId");
        String lcl3pRefId = request.getParameter("lcl3pRefId");
        LclBookingImportAms lclBookingImportAms = lclBookingImportAmsDAO.findById(Long.parseLong(lcl3pRefId));
        lclBookingImportAmsDAO.delete(lclBookingImportAms);
        // record a note on this.
        Long fileId = Long.parseLong(fileNumberId);
        if (lclBookingImportAms.getScac() != null) {
            utils.insertLCLRemarks(fileId, "Deleted - Scac : " + lclBookingImportAms.getScac().toUpperCase() + " and AMS/HBL #" + lclBookingImportAms.getAmsNo().toUpperCase()
                    + " with pieces count : " + lclBookingImportAms.getPieces(), REMARKS_DR_AUTO_NOTES, thisUser);
        } else {

            utils.insertLCLRemarks(fileId, "Deleted - Scac : " + " " + " and AMS/HBL #" + lclBookingImportAms.getAmsNo().toUpperCase()
                    + " with pieces count : " + lclBookingImportAms.getPieces(), REMARKS_DR_AUTO_NOTES, thisUser);
        }
        lclBookingDAO.updateModifiedDateTime(fileId, thisUser.getUserId());
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("amsHBLList", lclBookingImportAmsDAO.findAll(fileId));
        }
        return mapping.findForward("ImpAmsHBL");
    }

    public ActionForward addLclBookingHsCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        String remarks = "", remarksType = "";
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclBookingHsCode lclBookingHsCode = new LclBookingHsCode();
            LclBookingHsCodeDAO lclBookingHsCodeDAO = new LclBookingHsCodeDAO();
            if (CommonUtils.isNotEmpty(lclBookingForm.getHsCode())) {
                lclBookingHsCode.setCodes(lclBookingForm.getHsCode());
                remarksType = _3PARTY_TYPE_HSC;
                remarks = "Inserted - HS Code# " + " " + lclBookingForm.getHsCode();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getBookingHsCodeId())) {
                lclBookingHsCode.setId(Long.parseLong(lclBookingForm.getBookingHsCodeId()));
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getBookingHsCode())) {
                lclBookingHsCode.setCodes(lclBookingForm.getBookingHsCode());
            }
            lclBookingHsCode.setLclFileNumber(new LclFileNumberDAO().findById(Long.parseLong(fileNumberId)));
            if (CommonUtils.isNotEmpty(lclBookingForm.getHsCodePiece())) {
                lclBookingHsCode.setNoPieces(Integer.parseInt(lclBookingForm.getHsCodePiece()));
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getHsCodeWeightMetric())) {
                lclBookingHsCode.setWeightMetric(new BigDecimal(lclBookingForm.getHsCodeWeightMetric()));
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getPackageTypeId())) {
                lclBookingHsCode.setPackageType(new PackageType(Long.parseLong(lclBookingForm.getPackageTypeId())));
            }
            lclBookingHsCode.setEnteredBy(getCurrentUser(request));
            lclBookingHsCode.setModifiedBy(getCurrentUser(request));
            lclBookingHsCode.setModifiedDatetime(new Date());
            lclBookingHsCode.setEnteredDatetime(new Date());
            if (CommonUtils.isNotEmpty(lclBookingHsCode.getCodes())) {
                lclBookingHsCodeDAO.saveOrUpdate(lclBookingHsCode);
            }
            if (fileNumberId != null && remarksType != null) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_DR_AUTO_NOTES, remarks, loginUser.getUserId());
            }
            request.setAttribute("lclBookingHsCodeList", lclBookingHsCodeDAO.getHsCodeList(Long.parseLong(fileNumberId)));
        }
        return mapping.findForward("hsCode");
    }

    public ActionForward deleteLclBookingHsCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String FORWARD_PAGE = "";
        LclBookingHsCodeDAO lclBookingHsCodeDAO = new LclBookingHsCodeDAO();
        LclFileNumber lclFileNumber = null;
        LclRemarks lclRemarks = new LclRemarks();
        lclRemarks.setEnteredBy(getCurrentUser(request));
        lclRemarks.setModifiedBy(getCurrentUser(request));
        lclRemarks.setEnteredDatetime(new Date());
        lclRemarks.setModifiedDatetime(new Date());
        String fileNumberId = request.getParameter("fileNumberId");
        String lcl3pRefId = request.getParameter("lcl3pRefId");
        if (CommonUtils.isNotEmpty(lcl3pRefId)) {
            LclBookingHsCode lclBookingHsCode = lclBookingHsCodeDAO.findById(Long.parseLong(lcl3pRefId));
            if (lclBookingHsCode != null) {
                FORWARD_PAGE = "hsCode";
                lclRemarks.setRemarks("Deleted - HS Code#" + " " + lclBookingHsCode.getCodes());
                lclRemarks.setType("HS");
                lclBookingHsCodeDAO.delete(lclBookingHsCode);
            }
        }
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileNumberId));
            lclRemarks.setLclFileNumber(lclFileNumber);
            new LclRemarksDAO().save(lclRemarks);
        }
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            request.setAttribute("lclBookingHsCodeList", new LclBookingHsCodeDAO().executeQuery("from LclBookingHsCode where lclFileNumber.id= " + Long.parseLong(fileNumberId)));
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public void setUserAndDateTime(LclContact lclContact, HttpServletRequest request) {
        lclContact.setEnteredBy(getCurrentUser(request));
        lclContact.setEnteredDatetime(new Date());
        lclContact.setModifiedBy(getCurrentUser(request));
        lclContact.setModifiedDatetime(new Date());
    }

    public List<Lcl3pRefNo> addOtiNumber(LCLBookingForm lclBookingForm) {
        List<Lcl3pRefNo> list = new ArrayList<Lcl3pRefNo>();
        if (CommonUtils.isNotEmpty(lclBookingForm.getOtiNumber())) {
            Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNo();
            lcl3pRefNo.setReference(lclBookingForm.getOtiNumber());
            lcl3pRefNo.setType("OTI");
            list.add(lcl3pRefNo);
        }
        return list;
    }

    public LCLBookingForm setOtiNumber(List<Lcl3pRefNo> list, LCLBookingForm lclBookingForm) {
        for (Lcl3pRefNo lcl3pRefNo : list) {
            if (lcl3pRefNo.getType().equals("OTI")) {
                lclBookingForm.setOtiNumber(lcl3pRefNo.getReference());
            }
        }
        return lclBookingForm;
    }

    public void addClient(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclBookingForm.getTempClientCompany())) {
            lclBookingForm.getLclBooking().getClientContact().setCompanyName(lclBookingForm.getTempClientCompany().toUpperCase());
        }
        if (lclBookingForm.getLclBooking().getClientContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "manual");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getTempClientCompany())) {
                lclContact.setCompanyName(lclBookingForm.getTempClientCompany().toUpperCase());
            } else {
                lclContact.setCompanyName(lclBookingForm.getLclBooking().getClientContact().getCompanyName().toUpperCase());
            }
            lclContact.setContactName(lclBookingForm.getLclBooking().getClientContact().getContactName().toUpperCase());
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getClientContact(),
                    getCurrentUser(request), lclFileNumber, "manual");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setClientContact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setClientContact(null);
        }
    }

    public void setClient(LCLBookingForm lclBookingForm, Long fileId, LCLContactDAO lclContactDAO) throws Exception {
        LclContact lclContact = lclContactDAO.getContact(fileId, "manual");
        if (lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
            lclBookingForm.setTempClientCompany(lclContact.getCompanyName().toUpperCase());
        }
    }

    public void addCientName(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclBookingForm.getSupContactName())) {
            lclBookingForm.getLclBooking().getSupContact().setCompanyName(lclBookingForm.getSupContactName().toUpperCase());
        }
        if (lclBookingForm.getLclBooking().getSupContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "supplier");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getSupContactName())) {
                lclContact.setCompanyName(lclBookingForm.getSupContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclBookingForm.getLclBooking().getSupContact().getCompanyName().toUpperCase());
            }
            lclContact.setContactName("");
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getSupContact(),
                    getCurrentUser(request), lclFileNumber, "supplier");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setSupContact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setSupContact(null);
        }
    }

    public void setSupplierName(LCLBookingForm lclBookingForm, Long fileId, LCLContactDAO lclContactDAO) throws Exception {
        LclContact lclContact = lclContactDAO.getContact(fileId, "supplier");
        if (lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
            lclBookingForm.setSupContactName(lclContact.getCompanyName().toUpperCase());
        }
    }

    public void addShipperName(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclBookingForm.getShipContactName())) {
            lclBookingForm.getLclBooking().getShipContact().setCompanyName(lclBookingForm.getShipContactName().toUpperCase());
        }
        if (lclBookingForm.getLclBooking().getShipContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "shipper");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getShipContactName())) {
                lclContact.setCompanyName(lclBookingForm.getShipContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclBookingForm.getLclBooking().getShipContact().getCompanyName().toUpperCase());
            }
            lclContact.setContactName(lclBookingForm.getLclBooking().getShipContact().getContactName().toUpperCase());
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getShipContact(),
                    getCurrentUser(request), lclFileNumber, "shipper");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setShipContact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setShipContact(null);
        }
    }

    public void setShipperName(LCLBookingForm lclBookingForm, Long fileId, LCLContactDAO lclContactDAO) throws Exception {
        LclContact lclContact = lclContactDAO.getContact(fileId, "shipper");
        if (lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
            lclBookingForm.setShipContactName(lclContact.getCompanyName().toUpperCase());
        }
    }

    public void addConsigneeName(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclBookingForm.getConsContactName())) {
            lclBookingForm.getLclBooking().getConsContact().setCompanyName(lclBookingForm.getConsContactName().toUpperCase());
        }
        if (lclBookingForm.getLclBooking().getConsContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "consignee");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getConsContactName())) {
                lclContact.setCompanyName(lclBookingForm.getConsContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(lclBookingForm.getLclBooking().getConsContact().getCompanyName().toUpperCase());
            }
            lclContact.setContactName(lclBookingForm.getLclBooking().getConsContact().getContactName().toUpperCase());
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getConsContact(),
                    getCurrentUser(request), lclFileNumber, "consignee");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setConsContact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setConsContact(null);
        }
    }

    public void setConsigneeName(LCLBookingForm lclBookingForm, Long fileId, LCLContactDAO lclContactDAO) throws Exception {
        LclContact lclContact = lclContactDAO.getContact(fileId, "consignee");
        if (lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
            lclBookingForm.setConsContactName(lclContact.getCompanyName().toUpperCase());
        }
    }

    public void addNotifyName(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclBookingForm.getNotifyContactName())) {
            lclBookingForm.getLclBooking().getNotyContact().setCompanyName(lclBookingForm.getNotifyContactName().toUpperCase());
        }
        if (lclBookingForm.getLclBooking().getNotyContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "notify");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getNotifyContactName())) {
                lclContact.setCompanyName(lclBookingForm.getNotifyContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(null != lclBookingForm.getLclBooking().getNotyContact().getCompanyName() ? lclBookingForm.getLclBooking().getNotyContact().getCompanyName().toUpperCase() : "");
            }
            lclContact.setContactName("");
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getNotyContact(),
                    getCurrentUser(request), lclFileNumber, "notify");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setNotyContact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setNotyContact(null);
        }
    }

    public void setNotifyName(LCLBookingForm lclBookingForm, Long fileId, LCLContactDAO lclContactDAO) throws Exception {
        LclContact lclContact = lclContactDAO.getContact(fileId, "notify");
        if (lclContact != null && CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
            lclBookingForm.setNotifyContactName(lclContact.getCompanyName().toUpperCase());
        }
    }

    public void addNotify2Name(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclBookingForm.getNotify2ContactName())) {
            lclBookingForm.getLclBooking().getNotify2Contact().setCompanyName(lclBookingForm.getNotify2ContactName().toUpperCase());
        }
        if (lclBookingForm.getLclBooking().getNotify2Contact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "Notify2");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getNotify2ContactName())) {
                lclContact.setCompanyName(lclBookingForm.getNotify2ContactName().toUpperCase());
            } else {
                lclContact.setCompanyName(null != lclBookingForm.getLclBooking().getNotify2Contact().getCompanyName() ? lclBookingForm.getLclBooking().getNotify2Contact().getCompanyName().toUpperCase() : "");
            }
            lclContact.setContactName("");
            if (lclBookingForm.getNotify2AcctNo() != null) {
                lclContact.setTradingPartner(new TradingPartnerDAO().findById(lclBookingForm.getNotify2AcctNo()));
            } else {
                lclContact.setTradingPartner(null);
            }
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getNotify2Contact(),
                    getCurrentUser(request), lclFileNumber, "Notify2");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setNotify2Contact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setNotify2Contact(null);
        }
    }

    public void addForwarderName(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        if (lclBookingForm.getLclBooking().getFwdContact().getValidLclContact().length() > 1) {
            LclContact lclContact = new LCLContactDAO().getContact(lclFileNumber.getId(), "forwarder");
            if (lclContact == null) {
                lclContact = new LclContact();
            }
            lclContact.setCompanyName(null != lclBookingForm.getLclBooking().getFwdContact().getCompanyName()
                    ? lclBookingForm.getLclBooking().getFwdContact().getCompanyName().toUpperCase() : "");
            lclContact.setContactName(null != lclBookingForm.getLclBooking().getFwdContact().getContactName()
                    ? lclBookingForm.getLclBooking().getFwdContact().getContactName().toUpperCase() : "");
            new LclUtils().setContactDataForBooking(lclContact, lclBookingForm.getLclBooking().getFwdContact(),
                    getCurrentUser(request), lclFileNumber, "forwarder");
            new LCLContactDAO().saveOrUpdate(lclContact);
            lclBookingForm.getLclBooking().setFwdContact(lclContact);
        } else {
            lclBookingForm.getLclBooking().setFwdContact(null);
        }
    }

    public void addSpecialRemarks(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_SPECIAL_REMARKS);
        if (CommonUtils.isNotEmpty(lclBookingForm.getSpecialRemarks())) {
            lclRemarks = null == lclRemarks ? new LclRemarks() : lclRemarks;
            lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_SPECIAL_REMARKS, lclBookingForm.getSpecialRemarks(), getCurrentUser(request));
        } else if (lclRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), REMARKS_TYPE_SPECIAL_REMARKS);
        }

        LclRemarks specialRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), SPECIAL_REMARKS_FD);
        if (CommonUtils.isNotEmpty(lclBookingForm.getSpecialRemarksPod())) {
            specialRemarks = null == specialRemarks ? new LclRemarks() : specialRemarks;
            lclRemarksDAO.insertLclRemarks(specialRemarks, lclFileNumber, SPECIAL_REMARKS_FD, lclBookingForm.getSpecialRemarksPod(), getCurrentUser(request));
        } else if (specialRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), SPECIAL_REMARKS_FD);
        }
    }

    public void addPortGriRemarks(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_GRI);
        if (CommonUtils.isNotEmpty(lclBookingForm.getPortGriRemarks())) {
            lclRemarks = null == lclRemarks ? new LclRemarks() : lclRemarks;
            lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_GRI, lclBookingForm.getPortGriRemarks(), getCurrentUser(request));
        } else if (lclRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), REMARKS_TYPE_GRI);
        }

        LclRemarks griRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), GRI_REMARKS_FD);
        if (CommonUtils.isNotEmpty(lclBookingForm.getPortGriRemarksPod())) {
            griRemarks = null == griRemarks ? new LclRemarks() : griRemarks;
            lclRemarksDAO.insertLclRemarks(griRemarks, lclFileNumber, GRI_REMARKS_FD, lclBookingForm.getPortGriRemarksPod(), getCurrentUser(request));
        } else if (griRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), GRI_REMARKS_FD);
        }
    }

    public void addInternalRemarks(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_INTERNAL_REMARKS);
        if (CommonUtils.isNotEmpty(lclBookingForm.getInternalRemarks())) {
            lclRemarks = null == lclRemarks ? new LclRemarks() : lclRemarks;
            lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_INTERNAL_REMARKS, lclBookingForm.getInternalRemarks(), getCurrentUser(request));
        } else if (lclRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), REMARKS_TYPE_INTERNAL_REMARKS);
        }

        LclRemarks internalRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), INTERNAL_REMARKS_FD);
        if (CommonUtils.isNotEmpty(lclBookingForm.getInternalRemarksPod())) {
            internalRemarks = null == internalRemarks ? new LclRemarks() : internalRemarks;
            lclRemarksDAO.insertLclRemarks(internalRemarks, lclFileNumber, INTERNAL_REMARKS_FD, lclBookingForm.getInternalRemarksPod(), getCurrentUser(request));
        } else if (internalRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), INTERNAL_REMARKS_FD);
        }
    }

    public void addPrintDrRemarks(Long fileId, String printDr, Integer userId) throws Exception {
        if (CommonUtils.isNotEmpty(printDr)) {
            String notes = "Y".equalsIgnoreCase(printDr) ? "Yes" : "No";
            new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "PrintDR ->" + notes, userId);
        }
    }

    public void addLabelRemarks(Long fileId, String fileNo, String labels, User loginUser) throws Exception {
        if (CommonUtils.isNotEmpty(labels)) {
            new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, labels + " Labels Printed", loginUser.getUserId());
            lclUtils.setMailTransactionsDetails("LclBooking", "Label Print", loginUser, labels, "Pending", new Date(), fileNo, fileId);
        }
    }

    public void addReplicateNote(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarks lclRemarks = null == new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclFileNumber.getId() + " AND type='Replicate' ") ? new LclRemarks() : new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclFileNumber.getId() + " AND type='Replicate'");
        if (CommonUtils.isNotEmpty(lclBookingForm.getReplicateFileNumber())) {
            lclRemarks.setRemarks("Booking Copied from " + lclBookingForm.getReplicateFileNumber());
            lclRemarks.setType("Replicate");
            lclRemarks.setLclFileNumber(lclFileNumber);
            lclRemarks.setEnteredBy(getCurrentUser(request));
            lclRemarks.setModifiedBy(getCurrentUser(request));
            lclRemarks.setModifiedDatetime(new Date());
            lclRemarks.setEnteredDatetime(new Date());
            new LclRemarksDAO().saveOrUpdate(lclRemarks);
        }
    }

    public void addOsd(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, User loginUser) throws Exception {
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        remarksDAO.updateRemarksByField(lclFileNumber, REMARKS_TYPE_OSD, "OSD",
                lclBookingForm.getOsdRemarks(), loginUser, REMARKS_DR_AUTO_NOTES);
    }

    public void addExternal(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, User loginUser, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_EXTERNAL_COMMENT);
        if (CommonUtils.isNotEmpty(lclBookingForm.getExternalComment())) {
            if (lclRemarks == null) {
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, "Inserted-> External Comments -> " + lclBookingForm.getExternalComment().toUpperCase(), loginUser);
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_TYPE_EXTERNAL_COMMENT, lclBookingForm.getExternalComment().toUpperCase(), loginUser);
            } else if (lclRemarks != null && !lclBookingForm.getExternalComment().equalsIgnoreCase(lclRemarks.getRemarks())) {
                String remarks = "Updated-> External Comments -> " + lclRemarks.getRemarks().toUpperCase() + " to " + lclBookingForm.getExternalComment().toUpperCase();
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, remarks, loginUser);
                lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_EXTERNAL_COMMENT, lclBookingForm.getExternalComment().toUpperCase(), loginUser);
            } else if (lclRemarks != null && lclBookingForm.getExternalComment().equalsIgnoreCase(lclRemarks.getRemarks())) {
                lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_EXTERNAL_COMMENT, lclBookingForm.getExternalComment().toUpperCase(), loginUser);
            }
        }
        if (lclRemarks != null && (lclBookingForm.getExternalComment() == null || "".equals(lclBookingForm.getExternalComment()))) {
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, "Deleted-> External Comments -> " + lclRemarks.getRemarks().toUpperCase(), loginUser);
            lclRemarksDAO.delete(lclRemarks);
        }
    }

    public void addSuHeadingNote(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, User loginUser, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), "SU");
        if (CommonUtils.isNotEmpty(lclBookingForm.getSuHeadingNote())) {
            if (lclRemarks == null) {
                lclRemarks = new LclRemarks();
            }
            lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, "SU", lclBookingForm.getSuHeadingNote().toUpperCase(), loginUser);
        } else if (lclRemarks != null) {
            lclRemarksDAO.deleteRemarks(lclFileNumber.getId(), "SU");
        }
    }

    public void addLoadingRemarks(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        remarksDAO.updateRemarksByField(lclFileNumber, REMARKS_TYPE_LOADING_REMARKS,
                "Remarks Loading", lclBookingForm.getRemarksForLoading(), getCurrentUser(request), REMARKS_DR_AUTO_NOTES);
    }

    public void addContactNotes(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarks lclRemarks = new LclRemarks();
        String contactName = "";
        String email = "";
        if (lclBookingForm.getLclBooking() != null
                && (lclBookingForm.getLclBooking().getClientContact() != null || lclBookingForm.getLclBooking().getShipContact() != null
                || lclBookingForm.getLclBooking().getConsContact() != null || lclBookingForm.getLclBooking().getFwdContact() != null)) {
            if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getClientContact() != null
                    && lclBookingForm.getLclBooking().getClientContact().getContactName() != null
                    && !lclBookingForm.getLclBooking().getClientContact().getContactName().equals("")) {
                contactName = lclBookingForm.getLclBooking().getClientContact().getContactName();
            } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getShipContact() != null && lclBookingForm.getLclBooking().getShipContact().getContactName() != null
                    && !lclBookingForm.getLclBooking().getShipContact().getContactName().equals("")) {
                contactName = lclBookingForm.getLclBooking().getShipContact().getContactName();
            } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getConsContact() != null && lclBookingForm.getLclBooking().getConsContact().getContactName() != null
                    && !lclBookingForm.getLclBooking().getConsContact().getContactName().equals("")) {
                contactName = lclBookingForm.getLclBooking().getConsContact().getContactName();
            } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getFwdContact() != null && lclBookingForm.getLclBooking().getFwdContact().getContactName() != null
                    && !lclBookingForm.getLclBooking().getFwdContact().getContactName().equals("")) {
                contactName = lclBookingForm.getLclBooking().getFwdContact().getContactName();
            }
        }

        if (lclBookingForm.getLclBooking() != null
                && (lclBookingForm.getLclBooking().getClientContact() != null || lclBookingForm.getLclBooking().getShipContact() != null
                || lclBookingForm.getLclBooking().getConsContact() != null || lclBookingForm.getLclBooking().getFwdContact() != null)) {
            if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getClientContact() != null
                    && lclBookingForm.getLclBooking().getClientContact().getEmail1() != null
                    && !lclBookingForm.getLclBooking().getClientContact().getEmail1().equals("")) {
                email = lclBookingForm.getLclBooking().getClientContact().getEmail1();
            } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getShipContact() != null && lclBookingForm.getLclBooking().getShipContact().getEmail1() != null
                    && !lclBookingForm.getLclBooking().getShipContact().getEmail1().equals("")) {
                email = lclBookingForm.getLclBooking().getShipContact().getEmail1();
            } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getConsContact() != null && lclBookingForm.getLclBooking().getConsContact().getEmail1() != null
                    && !lclBookingForm.getLclBooking().getConsContact().getEmail1().equals("")) {
                email = lclBookingForm.getLclBooking().getConsContact().getEmail1();
            } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getFwdContact() != null && lclBookingForm.getLclBooking().getFwdContact().getEmail1() != null
                    && !lclBookingForm.getLclBooking().getFwdContact().getEmail1().equals("")) {
                email = lclBookingForm.getLclBooking().getFwdContact().getEmail1();
            }
        }
        if ((lclBookingForm.getBookingContact() != null && !lclBookingForm.getBookingContact().equals(""))
                || (lclBookingForm.getBookingContactEmail() != null && !lclBookingForm.getBookingContactEmail().equals(""))) {
            if (!lclBookingForm.getBookingContact().equals(contactName) || !lclBookingForm.getBookingContactEmail().equals(email)) {
                lclRemarks.setRemarks("UPDATED -> Booking Contact Name -> " + lclBookingForm.getBookingContact() + " to " + contactName
                        + ", Booking Contact Email -> " + lclBookingForm.getBookingContactEmail() + " to " + email);
                lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
                lclRemarks.setLclFileNumber(lclFileNumber);
                lclRemarks.setEnteredBy(getCurrentUser(request));
                lclRemarks.setModifiedBy(getCurrentUser(request));
                lclRemarks.setModifiedDatetime(new Date());
                lclRemarks.setEnteredDatetime(new Date());
                new LclRemarksDAO().saveOrUpdate(lclRemarks);
            }
        }
        if (CommonUtils.isEmpty(contactName) && CommonUtils.isEmpty(email)) {
            if (CommonUtils.isNotEmpty(lclBookingForm.getBookingContact()) || CommonUtils.isNotEmpty(lclBookingForm.getBookingContactEmail())) {
                lclRemarks.setRemarks("DELETED -> Booking Contact Name -> " + lclBookingForm.getBookingContact()
                        + ", Booking Contact Email -> " + lclBookingForm.getBookingContactEmail());
                lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
                lclRemarks.setLclFileNumber(lclFileNumber);
                lclRemarks.setEnteredBy(getCurrentUser(request));
                lclRemarks.setModifiedBy(getCurrentUser(request));
                lclRemarks.setModifiedDatetime(new Date());
                lclRemarks.setEnteredDatetime(new Date());
                new LclRemarksDAO().saveOrUpdate(lclRemarks);
            }
        }
    }

    public void addInsertNotesForContact(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        LclRemarks lclRemarks = new LclRemarks();
        String contactName = "";
        String email = "";
        if (CommonUtils.isEmpty(lclBookingForm.getBookingContact()) && CommonUtils.isEmpty(lclBookingForm.getBookingContactEmail())) {
            if (lclBookingForm.getLclBooking() != null
                    && (lclBookingForm.getLclBooking().getClientContact() != null || lclBookingForm.getLclBooking().getShipContact() != null
                    || lclBookingForm.getLclBooking().getConsContact() != null || lclBookingForm.getLclBooking().getFwdContact() != null)) {
                if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getClientContact() != null
                        && lclBookingForm.getLclBooking().getClientContact().getContactName() != null
                        && !lclBookingForm.getLclBooking().getClientContact().getContactName().equals("") && !lclBookingForm.getLclBooking().getClientContact().getContactName().equals(lclBookingForm.getBookingContact())) {
                    contactName = lclBookingForm.getLclBooking().getClientContact().getContactName();
                } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getShipContact() != null && lclBookingForm.getLclBooking().getShipContact().getContactName() != null
                        && !lclBookingForm.getLclBooking().getShipContact().getContactName().equals("") && !lclBookingForm.getLclBooking().getShipContact().getContactName().equals(lclBookingForm.getBookingContact())) {
                    contactName = lclBookingForm.getLclBooking().getShipContact().getContactName();
                } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getConsContact() != null && lclBookingForm.getLclBooking().getConsContact().getContactName() != null
                        && !lclBookingForm.getLclBooking().getConsContact().getContactName().equals("") && !lclBookingForm.getLclBooking().getConsContact().getContactName().equals(lclBookingForm.getBookingContact())) {
                    contactName = lclBookingForm.getLclBooking().getConsContact().getContactName();
                } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getFwdContact() != null && lclBookingForm.getLclBooking().getFwdContact().getContactName() != null
                        && !lclBookingForm.getLclBooking().getFwdContact().getContactName().equals("") && !lclBookingForm.getLclBooking().getFwdContact().getContactName().equals(lclBookingForm.getBookingContact())) {
                    contactName = lclBookingForm.getLclBooking().getFwdContact().getContactName();
                }
            }

            if (lclBookingForm.getLclBooking() != null
                    && (lclBookingForm.getLclBooking().getClientContact() != null || lclBookingForm.getLclBooking().getShipContact() != null
                    || lclBookingForm.getLclBooking().getConsContact() != null || lclBookingForm.getLclBooking().getFwdContact() != null)) {
                if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getClientContact() != null
                        && lclBookingForm.getLclBooking().getClientContact().getEmail1() != null
                        && !lclBookingForm.getLclBooking().getClientContact().getEmail1().equals("") && !lclBookingForm.getLclBooking().getClientContact().getEmail1().equals(lclBookingForm.getBookingContactEmail())) {
                    email = lclBookingForm.getLclBooking().getClientContact().getEmail1();
                } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getShipContact() != null && lclBookingForm.getLclBooking().getShipContact().getEmail1() != null
                        && !lclBookingForm.getLclBooking().getShipContact().getEmail1().equals("") && !lclBookingForm.getLclBooking().getShipContact().getEmail1().equals(lclBookingForm.getBookingContactEmail())) {
                    email = lclBookingForm.getLclBooking().getShipContact().getEmail1();
                } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getConsContact() != null && lclBookingForm.getLclBooking().getConsContact().getEmail1() != null
                        && !lclBookingForm.getLclBooking().getConsContact().getEmail1().equals("") && !lclBookingForm.getLclBooking().getConsContact().getEmail1().equals(lclBookingForm.getBookingContactEmail())) {
                    email = lclBookingForm.getLclBooking().getConsContact().getEmail1();
                } else if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getFwdContact() != null && lclBookingForm.getLclBooking().getFwdContact().getEmail1() != null
                        && !lclBookingForm.getLclBooking().getFwdContact().getEmail1().equals("") && !lclBookingForm.getLclBooking().getFwdContact().getEmail1().equals(lclBookingForm.getBookingContactEmail())) {
                    email = lclBookingForm.getLclBooking().getFwdContact().getEmail1();
                }
            }
            if ((contactName != null && !contactName.equals("")) || (email != null && !email.equals(""))) {
                lclRemarks.setRemarks("INSERTED -> Booking Contact Name -> " + contactName
                        + ", Booking Contact Email -> " + email);
                lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
                lclRemarks.setLclFileNumber(lclFileNumber);
                lclRemarks.setEnteredBy(getCurrentUser(request));
                lclRemarks.setModifiedBy(getCurrentUser(request));
                lclRemarks.setModifiedDatetime(new Date());
                lclRemarks.setEnteredDatetime(new Date());
                new LclRemarksDAO().saveOrUpdate(lclRemarks);
            }
        }
    }

    public void addDoorCityZip(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber,
            User loginUser, Date now) throws Exception {
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        if (CommonUtils.isNotEmpty(lclBookingForm.getDoorOriginCityZip())) {
            RefTerminal terminal = LCL_IMPORT.equalsIgnoreCase(lclBookingForm.getModuleName())
                    ? loginUser.getImportTerminal() : loginUser.getTerminal();
            LclBookingPadDAO bkgPadDAO = new LclBookingPadDAO();
            LclBookingPad lclBookingPad = bkgPadDAO.createInstance(lclFileNumber, loginUser, now, terminal);
            lclBookingPad.setPickUpCity(lclBookingForm.getDoorOriginCityZip());
            bkgPadDAO.saveOrUpdate(lclBookingPad);
            if (LCL_IMPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
                LclBookingAc lclBookingAc = new LclCostChargeDAO().getLclBookingAcByChargeCode(lclFileNumber.getId(), "DOORDEL");
                if (lclBookingAc != null) {
                    LclBookingPad doorDel = lclBookingPadDAO.getLclBookingPadWithLclBookingAc(lclFileNumber.getId());
                    if (doorDel != null && doorDel.getLclBookingAc() != null && doorDel.getLclBookingAc().getId() != null) {
                        lclBookingForm.setPickupFlag("true");
                        String transType = new LCLBookingAcTransDAO().getTransType(doorDel.getLclBookingAc().getId());
                        lclBookingForm.setTransType(transType);
                        if (CommonUtils.isNotEmpty(doorDel.getLclBookingAc().getSpReferenceNo())) {
                            lclBookingForm.setSpReferenceNo(doorDel.getLclBookingAc().getSpReferenceNo());
                        }
                    }
                }
            }
        }
    }

    public void setDoorCityZip(LCLBookingForm lclBookingForm, Long fileId) throws Exception {
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        LclBookingPad lclBookingPad = lclBookingPadDAO.getByProperty("lclFileNumber.id", fileId);
        if (lclBookingPad != null) {
            lclBookingForm.setDoorOriginCityZip(lclBookingPad.getPickUpCity());
            if ("Imports".equalsIgnoreCase(lclBookingForm.getModuleName())) {
                LclBookingAc lclBookingAc = new LclCostChargeDAO().getLclBookingAcByChargeCode(fileId, "DOORDEL");
                if (lclBookingAc != null) {
                    LclBookingPad doorDel = lclBookingPadDAO.getLclBookingPadWithLclBookingAc(fileId);
                    if (doorDel != null && doorDel.getLclBookingAc() != null && doorDel.getLclBookingAc().getId() != null) {
                        lclBookingForm.setPickupFlag("true");
                        String transType = new LCLBookingAcTransDAO().getTransType(doorDel.getLclBookingAc().getId());
                        lclBookingForm.setTransType(transType);
                        if (CommonUtils.isNotEmpty(doorDel.getLclBookingAc().getSpReferenceNo())) {
                            lclBookingForm.setSpReferenceNo(doorDel.getLclBookingAc().getSpReferenceNo());
                        }
                    }
                }
            }
        }
    }

    public void bookingStatus(LclFileNumber lclFileNumber, LCLBookingForm bookingForm,
            User loginUser, Date now) throws Exception {
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        Transaction transaction = remarksDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        LclRemarks bookingRemarks = remarksDAO.validateBookingRemarks(lclFileNumber.getId());
        if (bookingRemarks == null) {
            LclRemarks lclRemarks = new LclRemarks();
            lclRemarks.setEnteredDatetime(now);
            lclRemarks.setLclFileNumber(lclFileNumber);
            lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
            if (CommonUtils.isNotEmpty(bookingForm.getCopyRatesDrNo())) {/* Copy DR Inserted Notes*/

                lclRemarks.setRemarks("Booking Created - Copy from DR#" + bookingForm.getCopyRatesDrNo());
                bookingForm.setCopyRatesDrNo(null);
                bookingForm.setCopyRates(null);
            } else {
                lclRemarks.setRemarks("Booking Created");
            }
            lclRemarks.setEnteredBy(loginUser);
            lclRemarks.setEnteredDatetime(now);
            lclRemarks.setModifiedBy(loginUser);
            lclRemarks.setModifiedDatetime(now);
            remarksDAO.save(lclRemarks);
        }
        transaction.commit();
    }

    public ActionForward createShortShip(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        User loginUser = getCurrentUser(request);
        Long fileId = new LclFileNumberDAO().getFileId(bookingForm.getShortShip());
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", fileId);
        bookingDAO.getCurrentSession().evict(lclBooking);
        LclBooking copyBkg = (LclBooking) BeanUtils.cloneBean(lclBooking);
        bookingForm.setBusinessUnit(copyBkg.getLclFileNumber().getBusinessUnit());
        bookingForm.setWhsCode((null != copyBkg.getPooWhseContact() && null != copyBkg.getPooWhseContact().getWarehouse())
                ? copyBkg.getPooWhseContact().getWarehouse().getWarehouseNo() : "");
        copyBkg.setBookedSsHeaderId(null);
        copyBkg.setFileNumberId(null);
        copyBkg.setLclFileNumber(null);
        copyBkg.setDefaultAgent(true);
        copyBkg.setSpotRate(false);
        copyBkg.setInsurance(false);
        copyBkg.setOverShortdamaged(false);
        copyBkg.setEnteredBy(null);
        copyBkg.setModifiedBy(null);
        copyBkg.setRelayOverride(false);
        copyBkg.setRtAgentContact(null);
        copyBkg.setThirdPartyContact(null);
        copyBkg.setThirdPartyAcct(null);
        copyBkg.setBillToParty(null);
        copyBkg.setPooWhseLrdt(null);
        copyBkg.setFdEta(null);
        bookingForm.setLclBooking(lclBooking);
        bookingDAO.getSession().clear();
        if (lclBooking.getBookingType().equalsIgnoreCase("T")) {
            LclBookingImport lclBookingImport = lclBooking.getLclFileNumber().getLclBookingImport();
            if (lclBookingImport != null) {
                if (lclBookingImport.getUsaPortOfExit() != null) {
                    lclBooking.setPortOfOrigin(lclBooking.getPortOfDestination());
                    copyBkg.setPortOfLoading(lclBookingImport.getUsaPortOfExit());
                }
                if (lclBookingImport.getForeignPortOfDischarge() != null) {
                    copyBkg.setPortOfDestination(lclBookingImport.getForeignPortOfDischarge());
                }
                if (null != lclBookingImport.getExportAgentAcctNo()) {
                    copyBkg.setAgentAcct(lclBookingImport.getExportAgentAcctNo());
                }
            }
        }
        setPolPodValues(copyBkg, bookingForm.getModuleName(), request);//set Pol and Pod Values
        bookingForm.setRateType(copyBkg.getRateType());
        bookingForm.setFileNumberId(null);
        if (lclBooking.getPortOfOrigin() != null && lclBooking.getPortOfLoading() != null
                && lclBooking.getPortOfDestination() != null && lclBooking.getFinalDestination() != null) {
            LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
            String relayFlag = lclBooking.getRelayOverride() ? "Y" : "N";
            String cfcl = lclBooking.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" : "E";
            LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(lclBooking.getPortOfOrigin().getId(),
                    lclBooking.getFinalDestination().getId(), relayFlag);
            if (bookingPlanBean != null) {
                List<LclBookingVoyageBean> upcomingSailings = bookingPlanDAO
                        .getUpComingSailingsSchedule(lclBooking.getPortOfOrigin().getId(),
                                bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                                lclBooking.getFinalDestination().getId(), "V", bookingPlanBean, cfcl);
                request.setAttribute("voyageList", upcomingSailings);
            }
        }

        //set Values POA and Credit Status for all Vendors.Client,Shipper,Forwarder,Notify
        setPoaandCreditStatusValues(copyBkg, bookingForm.getModuleName(), bookingForm, request);
        List<LclBookingPiece> lclBookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
        List<LclBookingPiece> lclCommodityList = new ArrayList<LclBookingPiece>();
        if (null != lclBookingPieceList && lclBookingPieceList.size() > 0) {
            LclBookingPiece lbpnew = new LclBookingPiece();
            PropertyUtils.copyProperties(lbpnew, lclBookingPieceList.get(0));
            lbpnew.setId(null);
            lbpnew.setBookedPieceCount(null);
            lbpnew.setBookedVolumeImperial(null);
            lbpnew.setBookedVolumeMetric(null);
            lbpnew.setBookedWeightImperial(null);
            lbpnew.setBookedWeightMetric(null);
            lbpnew.setLclBookingPieceDetailList(null);
            lbpnew.setLclBookingHazmatList(null);
            lbpnew.setLclBookingAcList(null);
            lbpnew.setLclBookingPieceWhseList(null);
            lclCommodityList.add(lbpnew);
        }
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setBookingDetailList(null);
        //Overwrite BookingType value With 'E', since shortshipment cannot be Transhipment
        copyBkg.setBookingType("E");
        lclSession.setCommodityList(lclCommodityList);
        request.setAttribute("lclBooking", copyBkg);
        request.setAttribute("editDimFlag", "true");
        request.setAttribute("lclBookingForm", bookingForm);
        request.setAttribute("shortShip", bookingForm.getShortShip());
        new DBUtil().releaseLockByRecordIdAndModuleId(bookingForm.getShortShip(), "LCL FILE", loginUser.getUserId());
        return mapping.findForward("exportBooking");
    }

    public ActionForward copyDr(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        String moduleName = lclBookingForm.getModuleName();
        User loginUser = getCurrentUser(request);
        lclBookingForm.setCopyFnVal("Y");
        PortsDAO portsDAO = new PortsDAO();
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        String engmet = new String();
        LclFileNumberDAO fileDao = new LclFileNumberDAO();

        if (null == fileNumberId) {
            String fileNo = request.getParameter("fileNumber");
            fileNumberId = String.valueOf(fileDao.getFileIdByFileNumber(fileNo));
        }

        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
            LclBookingPad lclBookingPad = lclBookingPadDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (lclBookingPad != null) {
                lclBookingForm.setDoorOriginCityZip(lclBookingPad.getPickUpCity());
            }
            bookingDAO.getCurrentSession().evict(lclBooking);
            LclBooking copyBkg = (LclBooking) BeanUtils.cloneBean(lclBooking);

            if (LCL_EXPORT.equalsIgnoreCase(moduleName) && LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                LclBookingImport bookingImport = lclBooking.getLclFileNumber().getLclBookingImport();
                copyBkg.setPortOfOrigin(lclBooking.getPortOfDestination());
                copyBkg.setPortOfLoading(bookingImport.getUsaPortOfExit());
                copyBkg.setPortOfDestination(bookingImport.getForeignPortOfDischarge());
                copyBkg.setBookingType(LCL_EXPORT_TYPE);
            }

            lclBookingForm.setBusinessUnit(copyBkg.getLclFileNumber().getBusinessUnit());
            copyBkg.setBookedSsHeaderId(null);
            copyBkg.setFileNumberId(null);
            copyBkg.setLclFileNumber(null);
            copyBkg.setDefaultAgent(true);
            copyBkg.setSpotRate(false);
            copyBkg.setInsurance(false);
            copyBkg.setValueOfGoods(null);
            copyBkg.setCifValue(null);
            copyBkg.setOverShortdamaged(false);
            copyBkg.setEnteredBy(null);
            copyBkg.setModifiedBy(null);
            copyBkg.setRelayOverride(false);
            copyBkg.setRtAgentContact(null);
            copyBkg.setThirdPartyContact(null);
            copyBkg.setThirdPartyAcct(null);
            copyBkg.setBillToParty(null);
            if (!lclBookingForm.isAllowVoyageCopy()) {
                copyBkg.setPooWhseLrdt(null);
                copyBkg.setFdEta(null);
            }
            bookingDAO.getSession().clear();
            setPolPodValues(copyBkg, moduleName, request);//set Pol and Pod Values
            lclBookingForm.setRateType("R");
            lclBookingForm.setFileNumberId(null);
            lclBookingForm.setImpSearchFlag(null != lclBookingForm.getImpSearchFlag() ? lclBookingForm.getImpSearchFlag() : "");
            lclBookingForm.setHeaderId(null != lclBookingForm.getHeaderId() ? lclBookingForm.getHeaderId() : "");
            lclBookingForm.setUnitId(null != lclBookingForm.getUnitId() ? lclBookingForm.getUnitId() : "");
            if (copyBkg.getPortOfOrigin() != null && copyBkg.getPortOfLoading() != null && copyBkg.getPortOfDestination() != null && copyBkg.getFinalDestination() != null) {
                LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
                String relayFlag = lclBooking.getRelayOverride() ? "Y" : "N";
                String cfcl = lclBooking.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" : "E";
                LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(copyBkg.getPortOfOrigin().getId(),
                        copyBkg.getFinalDestination().getId(), relayFlag);
                if (bookingPlanBean != null) {
                    List<LclBookingVoyageBean> upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(copyBkg.getPortOfOrigin().getId(), bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                            copyBkg.getFinalDestination().getId(), "V", bookingPlanBean, cfcl);
                    request.setAttribute("voyageList", upcomingSailings);
                }
//                List<LclBookingVoyageBean> voyageList = new LclBookingPlanDAO().getBookingVoyageList(lclBooking.getPortOfOrigin().getId(),
//                        lclBooking.getPortOfLoading().getId(), lclBooking.getPortOfDestination().getId(), lclBooking.getFinalDestination().getId(), "V");
//                request.setAttribute("voyageList", voyageList);
            }
            setPoaandCreditStatusValues(copyBkg, moduleName, lclBookingForm, request);            //set Values POA and Credit Status for all Vendors.Client,Shipper,Forwarder,Notify
            List<LclBookingPiece> lclBookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            List<LclBookingPiece> lclCommodityList = new ArrayList<LclBookingPiece>();
            for (LclBookingPiece lbp : lclBookingPieceList) {
                LclBookingPiece lbpnew = new LclBookingPiece();
                PropertyUtils.copyProperties(lbpnew, lbp);
                lbpnew.setActualPackageType(null);
                lbpnew.setActualPieceCount(null);
                lbpnew.setActualVolumeMetric(null);
                lbpnew.setActualVolumeImperial(null);
                lbpnew.setActualWeightMetric(null);
                lbpnew.setActualWeightImperial(null);
//                lbpnew.setCommNo(null != lbp.getCommodityType() ? lbp.getCommodityType().getCode() : lbp.getCommNo());
//                lbpnew.setCommName(null != lbp.getCommodityType() ? lbp.getCommodityType().getDescEn() : lbp.getCommName());
//                lbpnew.setPkgName(null != lbp.getPackageType() ? lbp.getPackageType().getDescription() : lbp.getPkgName());
//                lbpnew.setPackageType(null != lbp.getPackageType() ? lbp.getPackageType() : null);
                lbpnew.setId(null);
                lbpnew.setLclBookingPieceDetailList(new CommodityDetailsDAO().findDetailProperty("lclBookingPiece.id", lbp.getId()));
                lbpnew.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(Long.parseLong(fileNumberId), lbp.getId()));
                lbpnew.setLclBookingAcList(new LclCostChargeDAO().findByFileAndCommodityList(Long.parseLong(fileNumberId), lbp.getId()));
                lbpnew.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
                lclCommodityList.add(lbpnew);
                if (lclBookingForm.isAllowVoyageCopy()) {
                    if (LCL_IMPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                        lclUtils.setVoyage(lbp, lclBookingForm, request);
                    } else {
                        copyBkg.setBookedSsHeaderId(lclBooking.getBookedSsHeaderId());
                    }
                }
            }
            HttpSession session = request.getSession();
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setCommodityList(lclCommodityList);
            String shortShip = lclBookingForm.getShortShip();
            if (shortShip == null) {
                request.setAttribute("lclCommodityList", lclCommodityList);
            }
            //******************Calculation Of Cost and Charges*****************
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            LCLImportChargeCalc lCLImportChargeCalc = new LCLImportChargeCalc();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            String radioValue = request.getParameter("radioValue");
            List<LclBookingAc> lclBookingAcList = null;
            if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())
                    || LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                request.setAttribute("lclBookingExport", null != lclBooking.getLclFileNumber().getLclBookingExport()
                        ? lclBooking.getLclFileNumber().getLclBookingExport() : new LclBookingExport());
            }
            if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                String portRemarks[] = new LclDwr().defaultDestinationRemarks(copyBkg.getFinalDestination().getUnLocationCode(),
                        copyBkg.getPortOfDestination().getUnLocationCode());
                if (portRemarks[0] != null && !portRemarks[0].equals("")) {
                    lclBookingForm.setSpecialRemarks(portRemarks[0]);
                }
                if (portRemarks[1] != null && !portRemarks[1].equals("")) {
                    lclBookingForm.setInternalRemarks(portRemarks[1]);
                }
                if (portRemarks[2] != null && !portRemarks[2].equals("")) {
                    lclBookingForm.setPortGriRemarks(portRemarks[2]);
                }
                if (portRemarks[3] != null && !portRemarks[3].equals("")) {
                    lclBookingForm.setSpecialRemarksPod(portRemarks[3]);
                }
                if (portRemarks[4] != null && !portRemarks[4].equals("")) {
                    lclBookingForm.setInternalRemarksPod(portRemarks[4]);
                }
                if (portRemarks[5] != null && !portRemarks[5].equals("")) {
                    lclBookingForm.setPortGriRemarksPod(portRemarks[5]);
                }
                lclBookingForm.setWhsCode((null != copyBkg.getPooWhseContact() && null != copyBkg.getPooWhseContact().getWarehouse())
                        ? copyBkg.getPooWhseContact().getWarehouse().getWarehouseNo() : "");
                String pickupReadyDate = "";
                String rateType = "";
                String fromZip = "";
                if (lclBooking.getRateType() != null && !lclBooking.getRateType().trim().equals("")) {
                    rateType = lclBooking.getRateType();
                    if (rateType.equalsIgnoreCase("R")) {
                        rateType = "Y";
                    }
                }
                if (lclBookingForm.getDoorOriginCityZip() != null && !lclBookingForm.getDoorOriginCityZip().trim().equals("")) {
                    String[] zip = lclBookingForm.getDoorOriginCityZip().split("-");
                    fromZip = zip[0];
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getPickupReadyDate())) {
                    pickupReadyDate = lclBookingForm.getPickupReadyDate();
                }
                Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    engmet = ports.getEngmet();
                }
                if (lclBookingForm.isAllowVoyageCopy()) {
                    lclChargesCalculation.calculateRates(lclBooking.getPortOfOrigin().getUnLocationCode(), lclBooking.getFinalDestination().getUnLocationCode(),
                            lclBooking.getPortOfLoading().getUnLocationCode(), lclBooking.getPortOfDestination().getUnLocationCode(),
                            null, lclCommodityList, getCurrentUser(request), lclBookingForm.getPooDoor(),
                            null, lclBookingForm.getLclBooking().getValueOfGoods(), rateType, "C", null,
                            pickupReadyDate, fromZip, null, lclBookingForm.getCalcHeavy(), lclBookingForm.getDeliveryMetro(),
                            lclBookingForm.getPcBoth(), null, radioValue, request, lclBooking.getBillToParty());
                    request.setAttribute("highVolumeMessage", lclChargesCalculation.getHighVolumeMessage());
                    lclBookingAcList = lclChargesCalculation.getBookingAcList();
                }
                lclUtils.setWeighMeasureForBooking(request, lclCommodityList, lclChargesCalculation.getPorts());
                lclUtils.setRolledUpChargesForBooking(lclBookingAcList, request, null, null, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
            } else {
                String agentNo = "";
                String originUnlocCode = "";
                String polUnlocCode = "";
                String podUnlocCode = "";
                String fdUnlocCode = "";
                if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                    agentNo = lclBooking.getAgentAcct().getAccountno();
                }
                if (lclBooking.getPortOfOrigin() != null && lclBooking.getPortOfOrigin().getUnLocationCode() != null) {
                    originUnlocCode = lclBooking.getPortOfOrigin().getUnLocationCode();
                }
                if (lclBooking.getPortOfLoading() != null && lclBooking.getPortOfLoading().getUnLocationCode() != null) {
                    polUnlocCode = lclBooking.getPortOfLoading().getUnLocationCode();
                }
                if (lclBooking.getPortOfDestination() != null && lclBooking.getPortOfDestination().getUnLocationCode() != null) {
                    podUnlocCode = lclBooking.getPortOfDestination().getUnLocationCode();
                }
                if (lclBooking.getFinalDestination() != null && lclBooking.getFinalDestination().getUnLocationCode() != null) {
                    fdUnlocCode = lclBooking.getFinalDestination().getUnLocationCode();
                }
                lCLImportChargeCalc.ImportRateCalculation(originUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, "N",
                        lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, lclBookingForm.getImpCfsWarehsId(), null,
                        lclCommodityList, request, loginUser, lclBookingForm.getUnitSsId());
                lclBookingAcList = lCLImportChargeCalc.getBookingAcList();
                lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
                lclUtils.setImportRolledUpChargesForBooking(lclBookingAcList, request, null, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), "", agentNo);
            }
            lclSession.setBookingAcList(lclBookingAcList);
            //*********Code for deleting previous booking Session***************
            ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
            ProcessInfo processInfo = processInfoDAO.findByFileNoAndUserId(lclBooking.getLclFileNumber().getFileNumber(), loginUser.getUserId());
            if (processInfo != null) {
                new ProcessInfoBC().delete((processInfo.getId()).toString());
            }
            //******************************************************************
            lclBookingForm.setLclBooking(copyBkg);
            lclBookingForm.setCopyRatesDrNo(lclBooking.getLclFileNumber().getFileNumber());
            session.setAttribute("lclSession", lclSession);
            request.setAttribute("lclBooking", copyBkg);
            request.setAttribute("lclBookingForm", lclBookingForm);
            request.setAttribute("shortShip", lclBookingForm.getShortShip());
            new DBUtil().releaseLockByRecordIdAndModuleId(lclBookingForm.getShortShip(), "LCL FILE", loginUser.getUserId());
        }
        return mapping.findForward(!moduleName.equalsIgnoreCase("imports") ? "exportBooking" : "importBooking");
    }

    public ActionForward calculateCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("started calculateCharges " + new Date());
            LCLBookingForm lclBookingForm = (LCLBookingForm) form;
            List<LclBookingPiece> lclBookingPiecesList = null;
            String radioValue = request.getParameter("radioValue");
            if (CommonUtils.isNotEmpty(lclBookingForm.getFileNumberId())) {
                LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
                LCLBookingDAO bookingDAO = new LCLBookingDAO();
                Long fileNumberId = Long.parseLong(lclBookingForm.getFileNumberId());
                LclBooking lclBooking = bookingDAO.findById(fileNumberId);
                bookingDAO.getCurrentSession().evict(lclBooking);
                lclBooking.setModifiedBy(getCurrentUser(request));
                lclBooking.setModifiedDatetime(new Date());
                lclBooking.setBillingType(lclBookingForm.getLclBooking().getBillingType());
                lclBooking.setBillToParty(lclBookingForm.getLclBooking().getBillToParty());
                lclBooking.setDeliveryMetro(lclBookingForm.getLclBooking().getDeliveryMetro());
                lclBooking = setLclBookingValues(lclBooking, lclBookingForm);
                lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                if (CommonUtils.isNotEmpty(lclBookingForm.getOrigin()) && CommonUtils.isNotEmpty(lclBookingForm.getDestination())
                        && CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                    LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                    if (lclBookingForm.getRateType() != null && !lclBookingForm.getRateType().trim().equals("")) {
                        String rateType = lclBookingForm.getRateType();
                        if (rateType.equalsIgnoreCase("R")) {
                            rateType = "Y";
                        }
                        String fromZip = "";
                        if (lclBookingForm.getDoorOriginCityZip() != null && !lclBookingForm.getDoorOriginCityZip().trim().equals("")) {
                            String[] zip = lclBookingForm.getDoorOriginCityZip().split("-");
                            fromZip = zip[0];
                        }
                        String pickupReadyDate = bookingDAO.getPickupReadyDate(fileNumberId);
                        log.info("calling calculateRates from lclbookingaction. " + new Date());
                        List<LclBookingAc> lclBookingAcList = null;
                        String pooCode = "";
                        String polCode = "";
                        String podCode = "";
                        String fdCode = "";
                        if (CommonFunctions.isNotNull(lclBooking)) {
                            if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin()) && CommonFunctions.isNotNull(lclBooking.getPortOfOrigin().getUnLocationName())) {
                                pooCode = lclBooking.getPortOfOrigin().getUnLocationCode();
                            }
                            if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                                polCode = lclBooking.getPortOfLoading().getUnLocationCode();
                            }
                            if (CommonFunctions.isNotNull(lclBooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclBooking.getPortOfDestination().getUnLocationName())) {
                                podCode = lclBooking.getPortOfDestination().getUnLocationCode();
                            }
                            if (CommonFunctions.isNotNull(lclBooking.getFinalDestination()) && CommonFunctions.isNotNull(lclBooking.getFinalDestination().getUnLocationName())) {
                                fdCode = lclBooking.getFinalDestination().getUnLocationCode();
                            }
                        }
                        if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                            String agentNo = "";
                            if (CommonFunctions.isNotNull(lclBooking.getSupAcct())
                                    && CommonFunctions.isNotNull(lclBooking.getSupAcct().getAccountno())) {
                                agentNo = lclBooking.getSupAcct().getAccountno();
                            }
                            lclBookingAcList = new LCLImportChargeCalc().ImportRateCalculation(pooCode, polCode, podCode, fdCode,
                                    "Y", lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, lclBookingForm.getImpCfsWarehsId(),
                                    fileNumberId, lclBookingPiecesList, request, getCurrentUser(request), lclBookingForm.getUnitSsId());

                            // For Imports transhipment file then rates not found in imports side below logic will apply.
                            if (CommonUtils.isEmpty(lclBookingAcList)) {
                                for (LclBookingPiece piece : lclBookingPiecesList) { // to avoid Lazy intialize error
                                    piece.setLclBookingPieceDetailList(null);
                                }
                                lclChargesCalculation.calculateRates(podCode, fdCode, podCode, fdCode, fileNumberId, lclBookingPiecesList, getCurrentUser(request),
                                        lclBookingForm.getPooDoor(), lclBookingForm.getInsurance(), lclBookingForm.getLclBooking().getValueOfGoods(),
                                        rateType, "C", null, pickupReadyDate, fromZip, null, lclBookingForm.getCalcHeavy(),
                                        lclBookingForm.getDeliveryMetro(), lclBookingForm.getPcBoth(), null, radioValue, request, lclBooking.getBillToParty());
                            }
                        } else {
                            lclChargesCalculation.calculateRates(pooCode, fdCode, polCode, podCode, fileNumberId, lclBookingPiecesList, getCurrentUser(request),
                                    lclBookingForm.getPooDoor(), lclBookingForm.getInsurance(), lclBookingForm.getLclBooking().getValueOfGoods(),
                                    rateType, "C", null, pickupReadyDate, fromZip, null, lclBookingForm.getCalcHeavy(),
                                    lclBookingForm.getDeliveryMetro(), lclBookingForm.getPcBoth(), null, radioValue, request, lclBooking.getBillToParty());
                        }
                        log.info("exited  calculateRates from lclbookingaction.");
                        request.setAttribute("highVolumeMessage", lclChargesCalculation.getHighVolumeMessage());
                        HttpSession session = request.getSession();
                        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                        request.setAttribute("lclSession", lclSession);
                        if (CommonUtils.isNotEmpty(lclChargesCalculation.getHighVolumeMessage())) {
                            LclRemarks lclRemarks = new LclRemarksDAO().getLclRemarksByType(lclBookingForm.getFileNumberId(), "AutoRates");
                            if (lclRemarks == null) {
                                lclRemarks = new LclRemarks();
                            }
                            lclRemarks.setLclFileNumber(lclBooking.getLclFileNumber());
                            lclRemarks.setType("AutoRates");
                            lclRemarks.setRemarks(lclChargesCalculation.getHighVolumeMessage());
                            lclRemarks.setEnteredBy(getCurrentUser(request));
                            lclRemarks.setEnteredDatetime(new Date());
                            lclRemarks.setModifiedBy(getCurrentUser(request));
                            lclRemarks.setModifiedDatetime(new Date());
                            new LclRemarksDAO().saveOrUpdate(lclRemarks);
                        }
                    }
                    request.setAttribute("lclBooking", lclBooking);
                    Ports ports = new PortsDAO().getByProperty("unLocationCode", lclBookingForm.getLclBooking().getFinalDestination().getUnLocationCode());
                    String engmet = "";
                    if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                        engmet = ports.getEngmet();
                    }
                    List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_EXPORT);
                    log.info("chargeList " + chargeList);
                    lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, lclChargesCalculation.getPorts());
                    log.info("calling setRolledUpChargesForBooking ");
                    lclUtils.setRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargesDAO, lclBookingPiecesList, lclBookingForm.getPcBoth(), engmet, "No");
                    log.info("exit setRolledUpChargesForBooking ");
                    if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                        lclUtils.setTemBillToPartyList(request, lclBookingForm.getModuleName());
                    }
                    if (lclBookingForm.isChangeBlCharge() && CommonFunctions.isNotNull(lclBooking.getLclFileNumber().getLclBl())) {
                        LclBl lclBl = new LCLBlDAO().findById(fileNumberId);
//                        Boolean isConsolidate = new LclConsolidateDAO().isConsolidatedByFileAB(lclBooking.getLclFileNumber().getId());
//                        if (isConsolidate) {
//                            List conoslidatelist = new LclConsolidateDAO().getConsolidatesFiles(lclBooking.getLclFileNumber().getId());
//                            conoslidatelist.add(lclBooking.getLclFileNumber().getId());
//                            new ExportBookingUtils().updateConsolidateManualCharge(conoslidatelist, lclBooking.getLclFileNumber().getId(), lclBl.getBillToParty(), request);
//                        } else {
                        List lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                        List<LclBlAc> blAcList = new BlUtils().getTransshipmentRaterForBl(lclBl, getCurrentUser(request), request);
                        if (CommonUtils.isEmpty(blAcList) && CommonUtils.isNotEmpty(lclBlPiecesList)) {
                            if (CommonUtils.isNotEqual(lclBooking.getDeliveryMetro(), lclBl.getDeliveryMetro())) {
                                lclBl.setDeliveryMetro(lclBooking.getDeliveryMetro());
                            }
                            new LCLBlDAO().update(lclBl);
                            new LclBlChargesCalculation().calculateRates(lclBl.getPortOfOrigin().getUnLocationCode(), lclBl.getFinalDestination().getUnLocationCode(), lclBl.getPortOfLoading().getUnLocationCode(),
                                    lclBl.getPortOfDestination().getUnLocationCode(), lclBl.getFileNumberId(), lclBlPiecesList, getCurrentUser(request), "", lclBl.getInsurance() ? "Y" : "N",
                                    lclBl.getValueOfGoods(), "R".equalsIgnoreCase(lclBl.getRateType()) ? "Y" : lclBl.getRateType(), "C", null, null, null, null, null, lclBl.getDeliveryMetro(), lclBl.getBillingType(), null,
                                    lclBl.getBillToParty(), request, false, false);
                        }
//                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("Error in calculateCharges method. " + new Date() + " for ", e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward calculateImportCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getCurrentUser(request);
            LCLBookingForm lclBookingForm = (LCLBookingForm) form;
            if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBookingForm.getBookingType())
                    && "N".equalsIgnoreCase(lclBookingForm.getTransShipMent())) {
                new LclBookingExportDAO().delete(lclBookingForm.getFileNumberId());
            }
            lclBookingForm.setEnums(request, "Imports", lclBookingForm.getTransShipMent());
            LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            List<LclBookingPiece> lclBookingPiecesList = null;
            List<LclBookingAc> chargeList = null;
            Long fileNumberId = Long.parseLong(lclBookingForm.getFileNumberId());
            String polUnCode = "";
            String podUnCode = "";
            String fdUnCode = "";
            String agentNo = "";
            String originCode = "";
            String transhipment = "N";
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
            lclBooking = setLclBookingValues(lclBooking, lclBookingForm);
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            if (CommonFunctions.isNotNull(lclBooking)) {
                if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin()) && CommonFunctions.isNotNull(lclBooking.getPortOfOrigin().getUnLocationName())) {
                    originCode = lclBooking.getPortOfOrigin().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                    polUnCode = lclBooking.getPortOfLoading().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclBooking.getPortOfDestination().getUnLocationName())) {
                    podUnCode = lclBooking.getPortOfDestination().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getFinalDestination()) && CommonFunctions.isNotNull(lclBooking.getFinalDestination().getUnLocationName())) {
                    fdUnCode = lclBooking.getFinalDestination().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getSupAcct())
                        && CommonFunctions.isNotNull(lclBooking.getSupAcct().getAccountno())) {
                    agentNo = lclBooking.getSupAcct().getAccountno();
                }
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getHeaderId())) {
                LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId()));
                request.setAttribute("lclssheader", lclssheader);
            }
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
            if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                transhipment = lclBookingForm.getTransShipMent();
                if ("Y".equalsIgnoreCase(transhipment)) {
                    chargeList = lclImportChargeCalc.ImportRateCalculation(originCode, polUnCode, podUnCode, fdUnCode, transhipment,
                            lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, lclBookingForm.getImpCfsWarehsId(),
                            fileNumberId, lclBookingPiecesList, request, user, lclBookingForm.getUnitSsId());
                    // For Imports transhipment file then rates not found in imports side below logic will apply.
                    if (CommonUtils.isEmpty(chargeList)) {
                        String fromZip = "";
                        if (lclBookingForm.getDoorOriginCityZip() != null && !lclBookingForm.getDoorOriginCityZip().trim().equals("")) {
                            String[] zip = lclBookingForm.getDoorOriginCityZip().split("-");
                            fromZip = zip[0];
                        }
                        String pickupReadyDate = new LCLBookingDAO().getPickupReadyDate(fileNumberId);
                        new LclChargesCalculation().calculateRates(podUnCode, fdUnCode, podUnCode, fdUnCode, fileNumberId, lclBookingPiecesList,
                                getCurrentUser(request), lclBookingForm.getPooDoor(), lclBookingForm.getInsurance(),
                                lclBookingForm.getLclBooking().getValueOfGoods(), "F", "C", null, pickupReadyDate, fromZip,
                                null, lclBookingForm.getCalcHeavy(), lclBookingForm.getDeliveryMetro(),
                                lclBookingForm.getPcBoth(), null, "", request, lclBooking.getBillToParty());
                    }
                } else {
                    lclImportChargeCalc.ImportRateCalculation(originCode, polUnCode, podUnCode, fdUnCode, transhipment,
                            lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, lclBookingForm.getImpCfsWarehsId(),
                            fileNumberId, lclBookingPiecesList, request, user, lclBookingForm.getUnitSsId());
                }
                chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_IMPORT);
                lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargeDAO, lclBookingPiecesList,
                        lclBooking.getBillingType(), "", agentNo);
            }
            if (lclBooking.getPortOfLoading() != null && lclBooking.getFinalDestination() != null) {
                String pooUnCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
                request.setAttribute("totalStorageAmt", lclImportChargeCalc.calculateImpAutoRates(lclBooking.getPortOfLoading().getUnLocationCode(),
                        lclBooking.getPortOfDestination().getUnLocationCode(), pooUnCode, "", lclBookingPiecesList, transhipment));
            }
            request.setAttribute("lclBooking", lclBooking);
        } catch (Exception e) {
            log.info("Error in LCL calculateCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward saveRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        String moduleName = lclBookingForm.getModuleName();
        User loginUser = getCurrentUser(request);
        synchronized (LclBookingAction.class) {
            Date now = new Date();
            request.setAttribute("moduleId", request.getParameter("moduleId"));
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<RoutingOptionsBean> routingOptionsList = lclSession.getRoutingOptionsList();
            LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
            LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();

            if (lclBookingForm.getIndex()
                    != null && !lclBookingForm.getIndex().trim().equals("") && routingOptionsList != null && routingOptionsList.size() > 0) {
                int index = Integer.parseInt(lclBookingForm.getIndex());
                String fileNumberId = lclBookingForm.getFileNumberId();
                LclFileNumber lclFileNumber = null;
                Boolean isNewFile = fileNumberId == null || fileNumberId.trim().equals("");
                if (isNewFile) {
                    LclFileNumberThread thread = new LclFileNumberThread();
                    String fileNumber = thread.getFileNumber();
                    Long newFileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
                    if (newFileNumberId == 0) {
                        lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
                    } else {
                        thread = new LclFileNumberThread();
                        fileNumber = thread.getFileNumber();
                        lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
                    }
                    if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                        lclFileNumberDAO.getSession().getTransaction().begin();
                    }
                } else {
                    lclFileNumber = lclFileNumberDAO.findById(new Long(fileNumberId));
                }
                lclBookingForm.getLclBooking().setEnteredBy(getCurrentUser(request));
                lclBookingForm.getLclBooking().setModifiedBy(getCurrentUser(request));
                lclBookingForm.getLclBooking().setEnteredDatetime(new Date());
                lclBookingForm.getLclBooking().setModifiedDatetime(new Date());
                lclBookingForm.getLclBooking().setLclFileNumber(lclFileNumber);
                lclBookingForm.getLclBooking().setFileNumberId(lclFileNumber.getId());
                RoutingOptionsBean routingOptionsBean = routingOptionsList.get(index);
                if (routingOptionsBean.getPickupCost() == null) {
                    BigDecimal weight = new BigDecimal(0.000);
                    BigDecimal measure = new BigDecimal(0.000);
                    List<LclBookingPiece> lclBookingPiecesList = null;
                    if (fileNumberId != null && !fileNumberId.trim().equals("")) {
                        lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
                    } else if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
                        lclBookingPiecesList = lclSession.getCommodityList();
                    }
                    if (lclBookingPiecesList != null) {
                        for (LclBookingPiece lbp : lclBookingPiecesList) {
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
                        if (CommonUtils.isNotEmpty(lclBookingForm.getSmallParcelRemarks())) {
                            if (lclBookingForm.getUps()) {
                                new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES,
                                        lclBookingForm.getSmallParcelRemarks(), loginUser.getUserId());
                                lclBookingForm.setSmallParcelRemarks("");
                            }
                        }
                        String realPath = session.getServletContext().getRealPath("/xml/");
                        String fileName = "ctsresponse" + session.getId() + ".xml";
                        CallCTSWebServices ctsweb = new CallCTSWebServices();
                        lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, routingOptionsBean.getFromZip(), routingOptionsBean.getToZip(),
                                routingOptionsBean.getSailDate(), "" + weight, "" + measure, null, "CARRIER_COST", lclBookingForm.getModuleName());
                        List<Carrier> carrierCostList = lclSession.getCarrierCostList();
                        BigDecimal pickUpCost = new BigDecimal(0.000);
                        if (CommonUtils.isNotEmpty(carrierCostList) && !carrierCostList.isEmpty()) {
                            for (int j = 0; j < carrierCostList.size(); j++) {
                                Carrier carrier = carrierCostList.get(j);
                                if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(routingOptionsBean.getScac().substring(1, routingOptionsBean.getScac().length() - 1))
                                        && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                                    pickUpCost = new BigDecimal(carrier.getFinalcharge());
                                    break;
                                }
                            }
                            routingOptionsBean.setPickupCost(pickUpCost);
                        }
                    }
                }
                String agentInfo[] = new PortsDAO().getDefaultAgentForLcl(routingOptionsBean.getPortOfDestination().getUnLocationCode(), "L");
                if (null != agentInfo && agentInfo.length > 0 && agentInfo[0] != null && !agentInfo[0].toString().equals("")) {
                    lclBookingForm.getLclBooking().setAgentAcct(new TradingPartner(agentInfo[0].toString()));
                }
                lclBookingForm.getLclBooking().setPortOfOrigin(routingOptionsBean.getPortOfOrigin());
                lclBookingForm.getLclBooking().setPortOfLoading(routingOptionsBean.getPortOfLoading());
                lclBookingForm.getLclBooking().setPortOfDestination(routingOptionsBean.getPortOfDestination());
                lclBookingForm.getLclBooking().setFinalDestination(routingOptionsBean.getFinalDestination());
                lclBookingForm.getLclBooking().getClientContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getClientContact(), request);
                lclBookingForm.getLclBooking().getSupContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getSupContact(), request);
                lclBookingForm.getLclBooking().getShipContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getShipContact(), request);
                lclBookingForm.getLclBooking().getConsContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getConsContact(), request);
                lclBookingForm.getLclBooking().getFwdContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getFwdContact(), request);
                lclBookingForm.getLclBooking().getNotyContact().setLclFileNumber(lclFileNumber);
                this.setUserAndDateTime(lclBookingForm.getLclBooking().getNotyContact(), request);
                //will go away after setting into form
                lclBookingForm.getLclBooking().setPooWhseContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
                lclBookingForm.getLclBooking().setRtAgentContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
                LclDwr lcldwr = new LclDwr();
                if (lclBookingForm.getLclBooking() != null && lclBookingForm.getLclBooking().getPortOfOrigin() != null
                        && CommonUtils.isNotEmpty(lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode())) {
                    String[] deliverCargoTo = lcldwr.getdeliverCargoDetails(lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode());
                    if (CommonUtils.isNotEmpty(deliverCargoTo) && CommonUtils.isNotEmpty(deliverCargoTo[0])) {
                        Warehouse deliverWarehouse = new WarehouseDAO().getWareHouseBywarehsNo(deliverCargoTo[0]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setWarehouse(null != deliverWarehouse ? deliverWarehouse : null);
                        lclBookingForm.getLclBooking().getPooWhseContact().setCompanyName(deliverCargoTo[1]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setAddress(deliverCargoTo[2]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setCity(deliverCargoTo[3]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setState(deliverCargoTo[4]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setZip(deliverCargoTo[5]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setPhone1(deliverCargoTo[6]);
                        lclBookingForm.getLclBooking().getPooWhseContact().setFax1(deliverCargoTo[7]);
                        lclBookingForm.setWhsCode(deliverCargoTo[0]);
                    }
                }
                this.addClient(lclBookingForm, lclFileNumber, request);
                if (lclBookingForm.getModuleName() != null && !lclBookingForm.getModuleName().equalsIgnoreCase("imports")) {
                    this.addSpecialRemarks(lclBookingForm, lclFileNumber, request);
                    this.addPortGriRemarks(lclBookingForm, lclFileNumber, request);
                    this.addInternalRemarks(lclBookingForm, lclFileNumber, request);
                }
                this.addDoorCityZip(lclBookingForm, lclFileNumber, loginUser, now);
                if (lclBookingForm.getLclBooking().getPortOfLoading() != null) {
                    request.setAttribute("pol", lclUtils.getConcatenatedOriginByUnlocation(lclBookingForm.getLclBooking().getPortOfLoading()));
                    request.setAttribute("polCode", lclBookingForm.getLclBooking().getPortOfLoading().getUnLocationCode());
                }
                if (lclBookingForm.getLclBooking().getPortOfDestination() != null) {
                    request.setAttribute("pod", lclUtils.getConcatenatedOriginByUnlocation(lclBookingForm.getLclBooking().getPortOfDestination()));
                    request.setAttribute("podCode", lclBookingForm.getLclBooking().getPortOfDestination().getUnLocationCode());
                }
                if (lclBookingForm.getLclBooking().getPortOfOrigin() != null && lclBookingForm.getLclBooking().getPortOfLoading() != null && lclBookingForm.getLclBooking().getPortOfDestination() != null && lclBookingForm.getLclBooking().getFinalDestination() != null) {
//                    List<LclBookingVoyageBean> voyageList = new LclBookingPlanDAO().getVoyageList(lclBookingForm.getLclBooking().getPortOfOrigin().getId(),
//                            lclBookingForm.getLclBooking().getPortOfLoading().getId(), lclBookingForm.getLclBooking().getPortOfDestination().getId(), lclBookingForm.getLclBooking().getFinalDestination().getId(), "V");
//                    request.setAttribute("voyageList", voyageList);
                }
                if (LCL_EXPORT.equalsIgnoreCase(moduleName)) {
                    request.setAttribute("allowDisposition", new RoleDutyDAO().getRoleDetails("allow_change_disposition", loginUser.getRole().getRoleId()));
                }
                lclBookingForm.setEnums(request, "Exports", "");
                List commodityList = null != lclSession.getCommodityList() ? lclSession.getCommodityList() : new ArrayList();
                for (Object obj : commodityList) {
                    LclBookingPiece lbp = (LclBookingPiece) obj;
                    lbp.setLclFileNumber(lclFileNumber);
                    List<LclBookingPieceDetail> detailList = lbp.getLclBookingPieceDetailList();
                    lbp.setLclBookingPieceDetailList(null);
                    new LclBookingPieceDAO().save(lbp);
                    if (CommonUtils.isNotEmpty(detailList)) {
                        for (int i = 0; i < detailList.size(); i++) {
                            LclBookingPieceDetail detail = (LclBookingPieceDetail) detailList.get(i);
                            detail.setLclBookingPiece(lbp);
                            new CommodityDetailsDAO().save(detail);
                        }
                    }
                }
                new LCLBookingDAO().saveOrUpdate(lclBookingForm.getLclBooking());
                new ExportBookingUtils().setLclBookingExport(lclBookingForm, lclFileNumber, request, loginUser);
                request.setAttribute("lclContact", lclBookingForm);
                lclSession.setCommodityList(null);
                session.setAttribute("lclSession", lclSession);
                List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId());
                request.setAttribute("lclCommodityList", lclBookingPiecesList);
                Ports ports = null;
                if (lclBookingForm.getLclBooking().getPortOfOrigin() != null && lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode() != null
                        && !lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode().trim().equals("")
                        && lclBookingForm.getLclBooking().getPortOfDestination() != null && lclBookingForm.getLclBooking().getPortOfDestination().getUnLocationCode() != null
                        && !lclBookingForm.getLclBooking().getPortOfDestination().getUnLocationCode().trim().equals("")
                        && lclBookingPiecesList != null && !lclBookingPiecesList.isEmpty()) {
                    LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                    if (lclBookingForm.getRateType() != null && !lclBookingForm.getRateType().trim().equals("")) {
                        String rateType = lclBookingForm.getRateType();
                        if (rateType.equalsIgnoreCase("R")) {
                            rateType = "Y";
                        }
                        lclChargesCalculation.calculateRates(lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode(),
                                lclBookingForm.getLclBooking().getFinalDestination().getUnLocationCode(),
                                lclBookingForm.getLclBooking().getPortOfLoading().getUnLocationCode(),
                                lclBookingForm.getLclBooking().getPortOfDestination().getUnLocationCode(),
                                lclFileNumber.getId(), lclBookingPiecesList, getCurrentUser(request),
                                lclBookingForm.getPooDoor(), lclBookingForm.getInsurance(), lclBookingForm.getLclBooking().getValueOfGoods(), rateType, "C", null, null,
                                null, null, lclBookingForm.getCalcHeavy(), lclBookingForm.getDeliveryMetro(),
                                lclBookingForm.getPcBoth(), null, null, request, lclBookingForm.getLclBooking().getBillToParty());
                        ports = lclChargesCalculation.getPorts();
                    }
                }
                //this.bookingStatus(lclBookingForm, lclFileNumber, request, null);
                if (routingOptionsBean.getCtsAmount() != null && !routingOptionsBean.getCtsAmount().trim().equals("")) {
                    GlMappingDAO glMappingDAO = new GlMappingDAO();
                    String chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
                    TradingPartner spRefNumber = new TradingPartnerDAO().findById("CTSFRE0004");
                    LclBookingAc lclBookingAc = lclCostChargesDAO.getLclBookingAcByChargeCode(lclFileNumber.getId(), chargeCode);
                    if (lclBookingAc == null) {
                        lclBookingAc = new LclBookingAc();
                        lclBookingAc.setLclFileNumber(lclFileNumber);
                        lclBookingAc.setEnteredDatetime(now);
                        lclBookingAc.setEnteredBy(loginUser);
                        lclBookingAc.setBundleIntoOf(Boolean.FALSE);
                        lclBookingAc.setPrintOnBl(Boolean.TRUE);
                        lclBookingAc.setAdjustmentAmount(new BigDecimal(0.00));
                    }
                    LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(lclFileNumber.getId());
                    if (lclBookingPad == null) {
                        lclBookingPad = new LclBookingPad();
                        LclContact lclDeliveryContact = new LclContact();
                        lclDeliveryContact.setLclFileNumber(lclFileNumber);
                        this.setUserAndDateTime(lclDeliveryContact, request);
                        lclBookingPad.setDeliveryContact(lclDeliveryContact);
                        LclContact lclPickupContact = new LclContact();
                        lclPickupContact.setLclFileNumber(lclFileNumber);
                        this.setUserAndDateTime(lclPickupContact, request);
                        lclBookingPad.setPickupContact(lclPickupContact);
                        lclBookingPad.setEnteredDatetime(now);
                        lclBookingPad.setEnteredBy(loginUser);
                    }
                    BigDecimal d = new BigDecimal(routingOptionsBean.getCtsAmount().substring(1, routingOptionsBean.getCtsAmount().length()));
                    BigDecimal pickupCost = routingOptionsBean.getPickupCost();
                    lclBookingAc.setArAmount(d);
                    lclBookingAc.setRatePerUnit(d);
                    lclBookingAc.setApAmount(pickupCost);
                    lclBookingAc.setArglMapping(glMappingDAO.findByChargeCode(chargeCode, "LCLE", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
                    lclBookingAc.setSupAcct(spRefNumber);
                    lclBookingAc.setApglMapping(glMappingDAO.findByChargeCode(chargeCode, "LCLE", TRANSACTION_TYPE_ACCRUALS));
                    lclBookingAc.setTransDatetime(now);
                    lclBookingAc.setManualEntry(true);
                    lclBookingAc.setRatePerUnitUom("FL");
                    lclBookingAc.setRateUom("I");
                    lclBookingAc.setModifiedBy(loginUser);
                    lclBookingAc.setModifiedDatetime(now);
                    lclBookingAc.setDeleted(Boolean.TRUE);
                    lclBookingAc.setArBillToParty(lclBookingForm.getLclBooking().getBillToParty());
                    lclCostChargesDAO.saveOrUpdate(lclBookingAc);

                    lclBookingPad.setLclFileNumber(lclFileNumber);
                    lclBookingPad.setModifiedBy(getCurrentUser(request));
                    lclBookingPad.setModifiedDatetime(now);
                    lclBookingPad.getLclBookingAc().setArAmount(d);
                    lclBookingPad.getLclBookingAc().setApAmount(pickupCost);
                    if (routingOptionsBean.getScac() != null && !routingOptionsBean.getScac().trim().equals("")) {
                        lclBookingPad.setScac(routingOptionsBean.getScac().substring(1, routingOptionsBean.getScac().length() - 1));
                    }
                    lclBookingPad.setLclBookingAc(lclBookingAc);
                    lclBookingPadDAO.saveOrUpdate(lclBookingPad);
                    request.setAttribute("lclBookingPad", lclBookingPad);
                }
                List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclFileNumber.getId(), LclCommonConstant.LCL_EXPORT);
                lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
                if (ports != null) {
                    lclUtils.setRolledUpChargesForBooking(chargeList, request, lclFileNumber.getId(), lclCostChargesDAO, lclBookingPiecesList, lclBookingForm.getLclBooking().getBillingType(), ports.getEngmet(), "No");
                }
                LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", lclFileNumber.getId());
                new LCLBookingDAO().getCurrentSession().evict(lclBooking);
                request.setAttribute("lclBooking", lclBooking);
                String relayFlag = lclBooking.getRelayOverride() ? "Y" : "N";
                LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
                LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(lclBooking.getPortOfOrigin().getId(),
                        lclBooking.getFinalDestination().getId(), relayFlag);
                String cfcl = lclBooking.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" : "E";
                if (bookingPlanBean != null) {
                    List<LclBookingVoyageBean> upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclBooking.getPortOfOrigin().getId(), bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                            lclBooking.getFinalDestination().getId(), "V", bookingPlanBean, cfcl);
                    request.setAttribute("voyageList", upcomingSailings);
                }

                List<LclBookingPiece> finallclBookingPiecesList = new LclBookingPieceDAO().getLclBkgPieceList(lclFileNumber.getId());
                for (LclBookingPiece lbp : finallclBookingPiecesList) {
                    if (isNewFile && (null != lclBookingForm.getLclBooking().getPortOfOrigin() && null != lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode())) {
                        Integer warehouseId = new WarehouseDAO().getWarehouseId(lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode(), "B");
                        if (CommonUtils.isNotEmpty(warehouseId)) {
                            new LclBookingPieceWhseDAO().insertLclBookingPieceWhse(lbp.getId(), warehouseId, getCurrentUser(request).getUserId());
                        }
                    }
                    lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(lclFileNumber.getId(), lbp.getId()));
                    lbp.setLclBookingAcList(chargeList);
                    lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
                    lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
                }
                request.setAttribute("lclCommodityList", finallclBookingPiecesList);
                request.setAttribute("error", false);
                if (lclBookingForm.getModuleName().equalsIgnoreCase("Exports") && lclBooking.getLclFileNumber().getStatus().equalsIgnoreCase("B")) {
                    Integer dispoId = lcldwr.disposDesc("B");
                    BigInteger dispo = new LCLBookingDAO().checkDisposition(lclBooking.getFileNumberId(), dispoId);
                    if (null == dispo) {
                        LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
                        lclBookingDispoDAO.insertBookingDispo(lclBooking.getFileNumberId(), dispoId, getCurrentUser(request).getUserId());
                    }
                }
            }
            lclSession.setCarrierList(null);
            lclSession.setCarrierCostList(null);
            setPoaandCreditStatusValues(lclBookingForm.getLclBooking(), lclBookingForm.getModuleName(), lclBookingForm, request);//set Values POA and Credit Status for all Vendors.Client,Shipper,Forwarder,Notify
            lclFileNumberDAO.getSession().getTransaction().commit();
        }
        if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
            lclFileNumberDAO.getSession().getTransaction().begin();
        }
        return mapping.findForward("exportBooking");
    }

    public ActionForward deleteLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        String commodityId = request.getParameter("commodityId");
        new LclBookingPieceDAO().deleteNotesForCommodity(Long.parseLong(fileNumberId), getCurrentUser(request).getUserId());
        if (fileNumberId != null && !fileNumberId.trim().equals("") && commodityId != null && !commodityId.trim().equals("")) {
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            new LclBookingPieceDAO().getUnitssId(Long.parseLong(commodityId));
            request.setAttribute("lclCommodityList", new LclBookingPieceDAO().findByProperty("lclFileNumber.id", new Long(fileNumberId)));
            request.setAttribute("ofspotrate", lclBooking.getSpotRate());
        } else if (request.getParameter("id") != null && !request.getParameter("id").trim().equals("")) {
            int index = Integer.parseInt(request.getParameter("id"));
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<LclBookingPiece> commodityList = (List<LclBookingPiece>) (null != lclSession.getCommodityList() ? lclSession.getCommodityList() : new ArrayList<LclBookingPiece>());
            commodityList.remove(index);
            lclSession.setCommodityList(commodityList);
            session.setAttribute("lclSession", lclSession);
            request.setAttribute("lclCommodityList", commodityList);
        }
        return mapping.findForward(COMMODITY_DESC);
    }

    public ActionForward modifyCommodityAndCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileNumberId");
        String recalculate = request.getParameter("recalculate");
        Long longFileId = new Long(fileId);
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        List<LclBookingAc> lclBookingAcList = null;
        if (null != recalculate && "true".equals(recalculate)) { // if recalculate true then calculate the rates
            List<LclBookingPiece> bkgCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            calculateBkgRates(form, request, bkgCommodityList);
        } else { // otherwise delete the auto rates
            lclBookingAcList = lclCostChargeDAO.executeQuery("from LclBookingAc where lclFileNumber.id = " + longFileId + "and manualEntry = 0");
            for (LclBookingAc lclBookingAc : lclBookingAcList) {
                lclCostChargeDAO.delete(lclBookingAc.getId());
            }
        }
        lclBookingAcList = lclCostChargeDAO.executeQuery("from LclBookingAc where lclFileNumber.id = " + longFileId + "and manualEntry = 1");
        List lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
        Double totalWeight = 0.00;
        Double totalMeasure = 0.00;
        Double calculatedWeight = 0.00;
        Double calculatedMeasure = 0.00;
        String engmet = new String();
        Ports ports = null;
        if (request.getParameter("destination") != null && request.getParameter("destination") != "") {
            PortsDAO portsdao = new PortsDAO();
            ports = portsdao.getByProperty("unLocationCode", request.getParameter("destination"));
            if (ports != null && ports.getEngmet() != null && !ports.getEngmet().trim().equals("")) {
                engmet = ports.getEngmet();
            }
        }
        for (LclBookingAc lclBookingAc : lclBookingAcList) {
            if (CommonUtils.isNotEmpty(longFileId)) {
                lclBookingAc.setLclFileNumber(new LclFileNumber(longFileId));
            }
            if (CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())) {
                GlMapping glmapping = new GlMappingDAO().findByChargeCode(lclBookingAc.getArglMapping().getChargeCode(), "LCLE", "AR");
                lclBookingAc.setArglMapping(glmapping);
                lclBookingAc.setApglMapping(glmapping);
            }
            lclBookingAc.setTransDatetime(new Date());
            lclBookingAc.setEnteredBy(getCurrentUser(request));
            lclBookingAc.setModifiedBy(getCurrentUser(request));
            lclBookingAc.setEnteredDatetime(new Date());
            lclBookingAc.setModifiedDatetime(new Date());
            lclBookingAc.setManualEntry(true);
            lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
            if (lclBookingAc.getRatePerWeightUnit() != null && lclBookingAc.getRatePerWeightUnit().doubleValue() > 0.00
                    && lclBookingAc.getRatePerVolumeUnit() != null && lclBookingAc.getRatePerVolumeUnit().doubleValue() > 0.00
                    && lclBookingAc.getRateFlatMinimum() != null && lclBookingAc.getRateFlatMinimum().doubleValue() > 0.00) {
                for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                    LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                    Double weightDouble = 0.00;
                    Double weightMeasure = 0.00;

                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                            }

                            if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                            } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                            } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                            } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                            }
                        }//end of else if engmet
                    }//end of else null
                    //calculate the Total Weight Of Commodities
                    totalWeight = totalWeight + weightDouble;
                    //calculate the Total Measure Of Commodities
                    totalMeasure = totalMeasure + weightMeasure;
                }//end of for loop

                if (engmet != null) {
                    if (engmet.equals("E")) {
                        calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                    } else if (engmet.equals("M")) {
                        calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                    }
                }//end of else if engmet
                calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                if (lclBookingAc.getRateUom().equals("M")) {
                    lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                } else {
                    lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                }
                lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerWeightUnitDiv());
                lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                    lclBookingAc.setArAmount(new BigDecimal(calculatedWeight));
                    lclBookingAc.setRatePerUnitUom("W");
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                    lclBookingAc.setArAmount(new BigDecimal(calculatedMeasure));
                    lclBookingAc.setRatePerUnitUom("V");
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());

                } else {
                    lclBookingAc.setArAmount(lclBookingAc.getRateFlatMinimum());
                    lclBookingAc.setRatePerUnitUom("M");
                }
                lclBookingAc.setBundleIntoOf(false);
                lclBookingAc.setPrintOnBl(true);
                lclBookingAc.setControlNo(lclBookingAc.getControlNo());
                lclCostChargeDAO.saveOrUpdate(lclBookingAc);
            }
        }
        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, LclCommonConstant.LCL_EXPORT);
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
        LclBooking lclBooking = new LCLBookingDAO().findById(longFileId);
        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
            LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
            if (lclCommodityList != null && lclCommodityList.size() > 0) {
                lclBookingPiece = lclCommodityList.get(0);
                if (lclBookingPiece.getStdchgRateBasis() != null && !lclBookingPiece.getStdchgRateBasis().trim().equals("")) {
                    request.setAttribute("stdchgratebasis", lclBookingPiece.getStdchgRateBasis());
                }
            }
        }
        String rateType = lclBooking.getRateType();
        if (rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = null;
        String origin = "";
        if (lclBooking != null && lclBooking.getPortOfOrigin() != null) {
            origin = lclBooking.getPortOfOrigin().getUnLocationName().toUpperCase();
            refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
        }
        String ofratebasis = new String();
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            ofratebasis = refterminal.getTrmnum() + "-";
        }
        PortsDAO portsDAO = new PortsDAO();
        ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            ofratebasis += ports.getEciportcode() + "-";
        }
        lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclBooking.getFileNumberId());
        request.setAttribute("lclCommodityList", lclCommodityList);
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
            if (lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().getCode() != null && !lclBookingPiece.getCommodityType().getCode().equals("")) {
                ofratebasis += lclBookingPiece.getCommodityType().getCode();
            }
            if (lclBookingPiece.getStdchgRateBasis() != null && !lclBookingPiece.getStdchgRateBasis().trim().equals("")) {
                request.setAttribute("stdchgratebasis", lclBookingPiece.getStdchgRateBasis());
            }
            String pol5Digit = "";
            String polUnCode = "";
            String destination = lclBooking.getFinalDestination().getUnLocationName().toUpperCase();
            String pooUnCode = lclBooking.getPortOfOrigin().getUnLocationCode();
            if (lclBooking.getPortOfLoading() != null && lclBooking.getPortOfLoading().getUnLocationCode() != null) {
                polUnCode = lclBooking.getPortOfLoading().getUnLocationCode();
                if (CommonUtils.isEmpty(polUnCode)) {
                    polUnCode = pooUnCode;
                }
            }
            Ports polPort = new PortsDAO().getByProperty("unLocationCode", polUnCode);
            if (polPort != null) {
                pol5Digit = polPort.getShedulenumber();
            }
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            if (lclBooking.getBookingType().equalsIgnoreCase("E")) {
                request.setAttribute("ofratebasis", ofratebasis);
                request.setAttribute("origin", origin);
                request.setAttribute("destination", destination);
            } else {
                request.setAttribute("origin", pol5Digit);
            }
        }
        lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
        return mapping.findForward("chargeDesc");
    }

    public ActionForward calculateInsuranceCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            PortsDAO portsDAO = new PortsDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            Boolean insurance = request.getParameter("insurance").equals("Y") ? true : false;
            String valueOfGoods = request.getParameter("valueOfGoods");
            LclBooking booking = bookingDAO.findById(longFileId);
            bookingDAO.getCurrentSession().evict(booking);
            booking.setInsurance(insurance);
            booking.setValueOfGoods(new BigDecimal(valueOfGoods));
            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String cif = "";
            String engmet = new String();
            LCLBookingForm lclBookingForm = (LCLBookingForm) form;
            String rateType = lclBookingForm.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(booking.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(booking.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", booking.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", booking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }

            String pcBoth = request.getParameter("pcBoth");
            LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, "INSURE", false);
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            if (insurance && lclCommodityList != null && lclCommodityList.size() > 0 && valueOfGoods != null && !valueOfGoods.trim().equals("")) {
                GlMappingDAO glmappingdao = new GlMappingDAO();
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0006", "LCLE", "AR");
                cif = lclChargesCalculation.calculateInsuranceCharge(pooorigin, polorigin, destinationfd, destinationpod, lclCommodityList,
                        Double.parseDouble(valueOfGoods), getCurrentUser(request),
                        longFileId, lclBookingAc, glmapping, request, booking.getBillToParty(), "I");
            } else if (!insurance && lclBookingAc != null) {
                String noteDesc = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBookingAc.getArAmount();
                lclCostChargeDAO.delete(lclBookingAc);
                new LclRemarksDAO().insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, noteDesc, loginUser.getUserId());
            }
            LclBookingAc cafBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, "CAF", false);
            if (cafBookingAc != null) {
                lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, pcBoth, loginUser, longFileId, request, ports, booking.getBillToParty());
            }
            if (CommonUtils.isNotEmpty(cif)) {
                booking.setCifValue(new BigDecimal(cif));
            } else {
                booking.setCifValue(null);
            }
            booking.setModifiedBy(loginUser);
            booking.setModifiedDatetime(new Date());
            bookingDAO.saveOrUpdate(booking);
            request.setAttribute("lclBooking", booking);
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, LclCommonConstant.LCL_EXPORT);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, booking.getBillingType(), engmet, "No");
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
        } catch (Exception e) {
            log.info("Error in LCL calculateCharges method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward calculateDeliveryMetroCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            String radioValue = request.getParameter("deliveryMetro");
            LclBooking lclBooking = bookingDAO.findById(longFileId);
            bookingDAO.getCurrentSession().evict(lclBooking);
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            PortsDAO portsDAO = new PortsDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String rateType = lclBooking.getRateType();
            String engmet = new String();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                destinationfd = ports.getEciportcode();
                engmet = ports.getEngmet();
            }
            String deliveryMetro = new String();
            if (radioValue.equalsIgnoreCase("I")) {
                deliveryMetro = "0060";
                lclBooking.setDeliveryMetro("I");
            } else {
                deliveryMetro = "0015";
                lclBooking.setDeliveryMetro("O");
            }
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            if (radioValue.equalsIgnoreCase("I") || radioValue.equalsIgnoreCase("O")) {
                lclChargesCalculation.calculateChargeForDeliveyMetro(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, getCurrentUser(request), longFileId, engmet, deliveryMetro, lclBooking.getBillToParty());
            } else if (radioValue.equals("N")) {
                lclBooking.setDeliveryMetro("N");
                String chargeCodeArray[] = chargeCodeArray = new String[]{"DELMET", "DELOUT"};
                LclRemarksDAO remarksDAO = new LclRemarksDAO();
                for (int i = 0; i < chargeCodeArray.length; i++) {
                    LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, chargeCodeArray[i], false);
                    if (lclBookingAc != null) {
                        String noteDesc = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBookingAc.getArAmount();
                        lclCostChargeDAO.delete(lclBookingAc);
                        remarksDAO.insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, noteDesc, loginUser.getUserId());
                    }
                }
            }
            String pcBoth = request.getParameter("pcBoth");
            LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, "CAF", false);
            if (lclBookingAc != null) {
                lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, pcBoth, loginUser, longFileId, request, ports, lclBooking.getBillToParty());
            }
            lclBooking.setModifiedBy(loginUser);
            lclBooking.setModifiedDatetime(new Date());
            new LCLBookingDAO().saveOrUpdate(lclBooking);
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, LclCommonConstant.LCL_EXPORT);
            request.setAttribute("lclBooking", lclBooking);
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
        } catch (Exception e) {
            log.info("Error in LCL calculateDeliveryMetroCharge method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward calculateHeavyCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date now = new Date();
        try {
            String engmet = new String();
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            PortsDAO portsDAO = new PortsDAO();
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBooking lclBooking = bookingDAO.findById(longFileId);
            bookingDAO.getCurrentSession().evict(lclBooking);
            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String radioValue = request.getParameter("calcHeavy");
            String rateType = lclBooking.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                destinationfd = ports.getEciportcode();
            }
            LclBookingExport lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", longFileId);
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            if (radioValue.equals("Y")) {
                lclChargesCalculation.calculateChargeForCalcHeavy(pooorigin, polorigin, destinationfd, destinationpod,
                        true, lclCommodityList, longFileId, getCurrentUser(request), refterminal, ports, lclBooking.getBillToParty());
                lclBookingExport.setCalcHeavy(true);
            } else if (radioValue.equals("N")) {
                String chargeCodeArray[] = chargeCodeArray = new String[]{"DENSE", "HLIFT6", "HLIFT8", "HLIFT", "EXTL", "EXTL30"};
                for (int i = 0; i < chargeCodeArray.length; i++) {
                    LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, chargeCodeArray[i], false);
                    if (lclBookingAc != null) {
                        String remarksDesc = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBookingAc.getArAmount();
                        lclCostChargeDAO.delete(lclBookingAc);
                        lclRemarksDAO.insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, remarksDesc, loginUser.getUserId());

                    }
                }
                lclBookingExport.setCalcHeavy(false);
            }
            String pcBoth = request.getParameter("pcBoth");
            LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, "CAF", false);
            if (lclBookingAc != null) {
                lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod, lclCommodityList, pcBoth,
                        loginUser, longFileId, request, ports, lclBooking.getBillToParty());
            }
            lclBookingExport.setModifiedBy(loginUser);
            lclBookingExport.setModifiedDatetime(now);
            new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, LclCommonConstant.LCL_EXPORT);
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
            request.setAttribute("lclBooking", lclBooking);
        } catch (Exception e) {
            log.info("Error in LCL calculateHeavyCharge method. " + now, e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward createQuickBkg(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        new LclUtils().setUnknowDestValues(request);
        User loginUser = getCurrentUser(request);
        String terminalLocation = loginUser.getTerminal().getTerminalLocation();
        lclBookingForm.setTrmnum(loginUser.getTerminal().getTrmnum());
        lclBookingForm.setTerminal(terminalLocation);
        if (lclBookingForm.getModuleName().equalsIgnoreCase("Exports")) {
            RoleDuty roleDuty = checkRoleDuty(request);
            if (roleDuty.isChangeDefaultFF()) {
                LclContact lclContact = new LclContact();
                TradingPartner noFFAcct = new TradingPartnerDAO().findById("NOFFAA0001");
                lclContact.setCompanyName(noFFAcct.getAccountName());
                request.setAttribute("noFFAcct", noFFAcct);
            }
        }
        if (terminalLocation.equals("QUEENS, NY")) {
            lclBookingForm.setTerminal(loginUser.getTerminal().getTerminalLocation());
            lclBookingForm.setTrmnum(loginUser.getTerminal().getTrmnum());
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            if (companyCode.equalsIgnoreCase("03")) {
                lclBookingForm.setBusinessUnit("ECI");
            }
        }
        HttpSession session = request.getSession();
        session.removeAttribute("lclQuickBookingHotCodeList");
        return mapping.findForward("quickDr");
    }

    public ActionForward copyQuickDr(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileNo = lclBookingForm.getFileNumber();
        LclFileNumberDAO fileNumberDAO = new LclFileNumberDAO();
        Long fileId = fileNumberDAO.getFileIdByFileNumber(fileNo);
        if (CommonUtils.isNotEmpty(fileId)) {
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            LclBooking booking = bookingDAO.getByProperty("lclFileNumber.id", fileId);
            bookingDAO.getCurrentSession().evict(booking);
            lclBookingForm.setPortOfOriginForDr(new LclUtils().getConcatenatedOriginByUnlocation(booking.getPortOfOrigin()));
            lclBookingForm.setQuickdrPortOfOriginId(booking.getPortOfOrigin().getId());
            lclBookingForm.setFinalDestinationForDr(new LclUtils().getConcatenatedOriginByUnlocation(booking.getFinalDestination()));
            lclBookingForm.setFinalDestinationIdForDr(booking.getFinalDestination().getId());
            lclBookingForm.setCompanyCode(booking.getLclFileNumber().getBusinessUnit());
            lclBookingForm.setBusinessUnit(booking.getLclFileNumber().getBusinessUnit());
            lclBookingForm.getLclBooking().setOverShortdamaged(booking.getOverShortdamaged());
            //  lclBookingForm.setOsdRemarks(booking.getBillingType());
            List<LclBookingVoyageBean> upcomingSailings = null;
            LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
            if (booking.getPortOfOrigin() != null && booking.getFinalDestination() != null) {
                String cfcl = booking.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" : "E";
                LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(booking.getPortOfOrigin().getId(),
                        booking.getFinalDestination().getId(), "N");
                if (bookingPlanBean != null && CommonUtils.isNotEmpty(bookingPlanBean.getPol_id())
                        && CommonUtils.isNotEmpty(bookingPlanBean.getPod_id())) {
                    upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(booking.getPortOfOrigin().getId(),
                            bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(), booking.getFinalDestination().getId(),
                            "V", bookingPlanBean, cfcl);
                }
            }
            List<LclBookingPiece> bookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
            if (null != bookingPieceList && !bookingPieceList.isEmpty()) {
                LclBookingPiece bookingPiece = bookingPieceList.get(0);
                lclBookingForm.setCommodityNoForDr(bookingPiece.getCommodityType().getCode());
                lclBookingForm.setCommodityTypeId(bookingPiece.getCommodityType().getId());
                lclBookingForm.setCommodityTypeForDr(bookingPiece.getCommodityType().getDescEn());
                lclBookingForm.setBookedPieceCount(null != bookingPiece.getBookedPieceCount()
                        ? String.valueOf(bookingPiece.getBookedPieceCount()) : "");
                lclBookingForm.setBookedVolumeImperial(
                        null != bookingPiece.getBookedVolumeImperial() ? String.valueOf(bookingPiece.getBookedVolumeImperial()) : "");
                lclBookingForm.setBookedWeightImperial(null != bookingPiece.getBookedWeightImperial()
                        ? String.valueOf(bookingPiece.getBookedWeightImperial()) : "");
                request.setAttribute("hazmat", bookingPiece.isHazmat() ? "Y" : "N");
                request.setAttribute("lclBookingPiece", bookingPiece);
            }
            HttpSession session = request.getSession();
            session.removeAttribute("lclQuickBookingHotCodeList");
            List<LclBookingHotCode> lclBookingHotCodes = new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", fileId);
            if (!lclBookingHotCodes.isEmpty()) {
                List<LclBookingHotCode> quickDrHotCode = new ArrayList<LclBookingHotCode>();
                for (LclBookingHotCode lclBookingHotCode : lclBookingHotCodes) {
                    quickDrHotCode.add(lclBookingHotCode);
                    if (lclBookingHotCode.getCode().contains("XXX")) {
                        String xxxHotCodeComments = new LclRemarksDAO().isRemark(fileId, "XXX");
                        String[] xxHotCodeArray = xxxHotCodeComments.split("-->");
                        lclBookingForm.setHotCodeComments(xxHotCodeArray[1]);
                    }
                }
                session.setAttribute("lclQuickBookingHotCodeList", quickDrHotCode);
            }
            String osdRemarks = new LclRemarksDAO().isRemark(fileId, "OSD");
            if (CommonUtils.isNotEmpty(osdRemarks)) {
                String[] osdremarks1 = osdRemarks.split("->");
                lclBookingForm.setOsdRemarks(osdremarks1[2]);
            }
            request.setAttribute("voyageList", upcomingSailings);
            request.setAttribute("lclBooking", booking);

        }
        request.setAttribute("lclBookingForm", lclBookingForm);
        return mapping.findForward("quickDr");
    }

    public ActionForward quickDr(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        synchronized (LclBookingAction.class) {
            lclBookingForm.getLclBooking().setEnteredBy(loginUser);
            lclBookingForm.getLclBooking().setModifiedBy(loginUser);
            lclBookingForm.getLclBooking().setEnteredDatetime(now);
            lclBookingForm.getLclBooking().setModifiedDatetime(now);
            LclFileNumber lclFileNumber = lclBookingForm.getLclBooking().getLclFileNumber();
            if (lclFileNumber == null) {
                LclFileNumberThread thread = new LclFileNumberThread();
                String fileNumber = thread.getFileNumber();
                Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
                if (fileNumberId == 0) {
                    lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
                } else {
                    thread = new LclFileNumberThread();
                    fileNumber = thread.getFileNumber();
                    lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
                }
                if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                    lclFileNumberDAO.getSession().getTransaction().begin();
                }
            }

            lclBookingForm.getLclBooking().setLclFileNumber(lclFileNumber);
            lclBookingForm.getLclBooking().setFileNumberId(lclFileNumber.getId());
            lclBookingForm.getLclBooking().getClientContact().setLclFileNumber(lclFileNumber);

            this.setUserAndDateTime(lclBookingForm.getLclBooking().getClientContact(), request);
            lclBookingForm.getLclBooking().getSupContact().setLclFileNumber(lclFileNumber);

            this.setUserAndDateTime(lclBookingForm.getLclBooking().getSupContact(), request);
            lclBookingForm.getLclBooking().getShipContact().setLclFileNumber(lclFileNumber);

            this.setUserAndDateTime(lclBookingForm.getLclBooking().getShipContact(), request);
            lclBookingForm.getLclBooking().getConsContact().setLclFileNumber(lclFileNumber);

            this.setUserAndDateTime(lclBookingForm.getLclBooking().getConsContact(), request);
            lclBookingForm.getLclBooking().getFwdContact().setLclFileNumber(lclFileNumber);

            this.setUserAndDateTime(lclBookingForm.getLclBooking().getFwdContact(), request);
            lclBookingForm.getLclBooking().getNotyContact().setLclFileNumber(lclFileNumber);

            this.setUserAndDateTime(lclBookingForm.getLclBooking().getNotyContact(), request);
            if (CommonUtils.isNotEmpty(lclBookingForm.getShipContactForDr())) {
                lclBookingForm.getLclBooking().getShipContact().setCompanyName(lclBookingForm.getShipContactForDr().toUpperCase());
            }

            if (CommonUtils.isNotEmpty(lclBookingForm.getShipAcctForDr())) {
                lclBookingForm.getLclBooking().setShipAcct(new TradingPartner(lclBookingForm.getShipAcctForDr()));
            }

            if (CommonUtils.isNotEmpty(lclBookingForm.getConsContactForDr())) {
                lclBookingForm.getLclBooking().getConsContact().setCompanyName(lclBookingForm.getConsContactForDr().toUpperCase());
            }

            if (CommonUtils.isNotEmpty(lclBookingForm.getConsAcctForDr())) {
                lclBookingForm.getLclBooking().setConsAcct(new TradingPartner(lclBookingForm.getConsAcctForDr()));
            }

            if (CommonUtils.isNotEmpty(lclBookingForm.getFwdContactForDr())) {
                lclBookingForm.getLclBooking().getFwdContact().setCompanyName(lclBookingForm.getFwdContactForDr().toUpperCase());
            }

            if (CommonUtils.isNotEmpty(lclBookingForm.getFwdAcctForDr())) {
                lclBookingForm.getLclBooking().setFwdAcct(new TradingPartner(lclBookingForm.getFwdAcctForDr()));
            }
            lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES, "Quick DR Created", loginUser.getUserId());
            //will go away after setting into form
            lclBookingForm.getLclBooking().setPooWhseContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
            lclBookingForm.getLclBooking().setRtAgentContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));

            LclDwr lcldwr = new LclDwr();
            UnLocation unLocation = unLocationDAO.findById(lclBookingForm.getQuickdrPortOfOriginId());
            String[] deliverCargoTo = new LclDwr().getdeliverCargoDetails(unLocation.getUnLocationCode());
            if (CommonUtils.isNotEmpty(deliverCargoTo) && CommonUtils.isNotEmpty(deliverCargoTo[0])) {
                Warehouse deliverWarehouse = new WarehouseDAO().getWareHouseBywarehsNo(deliverCargoTo[0]);
                lclBookingForm.getLclBooking().getPooWhseContact().setWarehouse(null != deliverWarehouse ? deliverWarehouse : null);
                lclBookingForm.getLclBooking().getPooWhseContact().setCompanyName(deliverCargoTo[1]);
                lclBookingForm.getLclBooking().getPooWhseContact().setAddress(deliverCargoTo[2]);
                lclBookingForm.getLclBooking().getPooWhseContact().setCity(deliverCargoTo[3]);
                lclBookingForm.getLclBooking().getPooWhseContact().setState(deliverCargoTo[4]);
                lclBookingForm.getLclBooking().getPooWhseContact().setZip(deliverCargoTo[5]);
                lclBookingForm.getLclBooking().getPooWhseContact().setPhone1(deliverCargoTo[6]);
                lclBookingForm.getLclBooking().getPooWhseContact().setFax1(deliverCargoTo[7]);
                lclBookingForm.setWhsCode(deliverCargoTo[0]);
            }
            RoleDuty roleDuty = checkRoleDuty(request);
            String fileno = lclFileNumber.getId().toString();
            String val = "";
            String origin = lclBookingForm.getPortOfOriginForDr();
            if (CommonUtils.isNotEmpty(origin)) {
                if (origin.lastIndexOf("(") > -1 && origin.lastIndexOf(")") > -1) {
                    val = origin.substring(origin.lastIndexOf("(") + 1, origin.lastIndexOf(")"));
                }
            }
            if (LCL_EXPORT.equalsIgnoreCase(lclBookingForm.getModuleName())) {
                if (roleDuty.isDefaultNoeeiLowVal()) {
                    lcldwr.noEeiAestoAdd(fileno, "booking", request);
                }
                String role = checkRoleDuty(val, request);
                if (role == "true") {
                    lclBookingForm.setPwk(YES);
                }
            }
            this.addClient(lclBookingForm, lclFileNumber, request);
            this.addNotify2Name(lclBookingForm, lclFileNumber, request);
            Integer pooId = lclBookingForm.getQuickdrPortOfOriginId();
            Integer fdId = lclBookingForm.getFinalDestinationIdForDr();
            if (fdId == 0) {
                fdId = 54727;
            }
            UnLocation unLocFd = unLocationDAO.findById(fdId);
            LclBookingPlanBean bookingPlanBean = new LclBookingPlanDAO().getRelay(pooId, fdId, "N");
            if (bookingPlanBean != null && CommonUtils.isNotEmpty(bookingPlanBean.getPol_id())
                    && CommonUtils.isNotEmpty(bookingPlanBean.getPod_id())) {
                lclBookingForm.getLclBooking().setPortOfLoading(unLocationDAO.findById(bookingPlanBean.getPol_id()));
                lclBookingForm.getLclBooking().setPortOfDestination(unLocationDAO.findById(bookingPlanBean.getPod_id()));
            }
            if (null != bookingPlanBean && CommonUtils.isNotEmpty(unLocFd.getUnLocationCode())
                    && CommonUtils.isNotEmpty(bookingPlanBean.getPod_code())) {
                String[] remarksDetails = lcldwr.defaultDestinationRemarks(unLocFd.getUnLocationCode(), bookingPlanBean.getPod_code());
                if (remarksDetails[0] != null && !remarksDetails[0].equals("")) {
                    lclBookingForm.setSpecialRemarks(remarksDetails[0]);
                }
                if (remarksDetails[1] != null && !remarksDetails[1].equals("")) {
                    lclBookingForm.setInternalRemarks(remarksDetails[1]);
                }
                if (remarksDetails[2] != null && !remarksDetails[2].equals("")) {
                    lclBookingForm.setPortGriRemarks(remarksDetails[2]);
                }
            }
            this.addSpecialRemarks(lclBookingForm, lclFileNumber, request);
            this.addPortGriRemarks(lclBookingForm, lclFileNumber, request);
            this.addInternalRemarks(lclBookingForm, lclFileNumber, request);
            this.addPrintDrRemarks(lclFileNumber.getId(), lclBookingForm.getPrintDr(), loginUser.getUserId());
            this.addLabelRemarks(lclFileNumber.getId(), lclFileNumber.getFileNumber(),
                    lclBookingForm.getLabelFieldName(), loginUser);
            String[] agentInfo = lcldwr.getDefaultAgentForLcl(unLocFd.getUnLocationCode(), "L");
            if (agentInfo != null && agentInfo[0] != null) {
                lclBookingForm.getLclBooking().setAgentAcct(new TradingPartner(agentInfo[0]));
            }

            lclBookingForm.setRateType("R");
            lclBookingForm.setRtdTransaction("N");
            lclBookingForm.setBillingType("P");
            lclBookingForm.getLclBooking().setBillToParty("F");
            lclBookingForm.setEnums(request, lclBookingForm.getModuleName(), "");
            lclBookingForm.setCfcl(lclBookingForm.getCfclForDR());
            lclBookingForm.setAesBy(false);
            if (CommonUtils.isNotEmpty(lclBookingForm.getCfclAcctNameForDr())) {
                lclBookingForm.setCfclAcctName(lclBookingForm.getCfclAcctNameForDr());
            }
            if (CommonUtils.isNotEmpty(lclBookingForm.getCfclAcctNoForDr())) {
                lclBookingForm.setCfclAcctNo(lclBookingForm.getCfclAcctNoForDr());
            }

            lclBookingForm.getLclBooking().setOverShortdamaged(lclBookingForm.isOsdValues());
            if (CommonUtils.isNotEmpty(lclBookingForm.getOsdRemarks())) {
                lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_TYPE_OSD, lclBookingForm.getOsdRemarks(), loginUser.getUserId());
                lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES, "INSERTED-> OSD ->" + lclBookingForm.getOsdRemarks(), loginUser.getUserId());
            }
            HttpSession session = request.getSession();
            List<LclBookingHotCode> batchHsCode = (List<LclBookingHotCode>) session.getAttribute("lclQuickBookingHotCodeList");
            if (CommonUtils.isNotEmpty(batchHsCode)) {
                LclBookingHotCodeDAO hotCodeDAO = new LclBookingHotCodeDAO();
                for (LclBookingHotCode hotCode : batchHsCode) {
                    hotCodeDAO.insertQuery(lclFileNumber.getId().toString(), hotCode.getCode(), loginUser.getUserId());
                }
            }
            String remarks = "";
            List<LclBookingHotCode> quickDrHotCode = (ArrayList<LclBookingHotCode>) session.getAttribute("lclQuickBookingHotCodeList");
            if (CommonUtils.isNotEmpty(quickDrHotCode)) {
                for (int i = 0; i < quickDrHotCode.size(); i++) {
                    remarks = "Inserted - HOT Code#" + " " + quickDrHotCode.get(i).getCode();
                    new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES, remarks.toUpperCase(), loginUser.getUserId());
                }
            }
            String hotCodeXXXComments = lclBookingForm.getHotCodeComments();
            if (CommonUtils.isNotEmpty(hotCodeXXXComments)) {
                hotCodeXXXComments = "Added Hot Code XXX Comments-->" + hotCodeXXXComments;
                new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES, hotCodeXXXComments.toUpperCase(), loginUser.getUserId());
                String desc = "Hold->Y, " + hotCodeXXXComments;
                lclBookingForm.getLclBooking().setHold("Y");
                new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_ON_HOLD_NOTES, desc.toUpperCase(), loginUser.getUserId());
            }
            String holdComments = lclBookingForm.getHoldComments();
            if (CommonUtils.isNotEmpty(holdComments)) {
                String desc = "";
                if (!"Y".equalsIgnoreCase(lclBookingForm.getLclBooking().getHold())) {
                    lclBookingForm.getLclBooking().setHold("Y");
                    desc = "Hold->Y,Comments-->";
                } else {
                    desc = "Hold Comments-->";
                }
                holdComments = desc + holdComments;
                new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES, holdComments.toUpperCase(), loginUser.getUserId());
            }
            if ("Exports".equalsIgnoreCase(lclBookingForm.getModuleName())) {
                String[] remarkTypes = new String[]{REMARK_TYPE_MANUAL, REMARKS_DR_MANUAL_NOTES};
                LclRemarksDAO remarksDAO = new LclRemarksDAO();
                Boolean manualFlag = remarksDAO.isRemarks(lclFileNumber.getId(), remarkTypes);
                Boolean hotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(lclFileNumber.getId(), "XXX");
                manualFlag = !manualFlag ? hotCodeFlag : manualFlag;
                if (hotCodeFlag) {
                    request.setAttribute("isHotCodeRemarks", remarksDAO.isRemarks(lclFileNumber.getId(), "ADDED HOT CODE XXX COMMENTS"));
                }
                request.setAttribute("hotCodeFlag", manualFlag);//notes color change
            }// HOT CODE LABEL in DR
            request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", lclFileNumber.getId()));

            lclBookingForm.getLclBooking().setTerminal(loginUser.getTerminal());
            if (null != loginUser.getTerminal()) {
                lclBookingForm.setTerminal(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
                lclBookingForm.setTrmnum(loginUser.getTerminal().getTrmnum());
            }

            lclBookingForm.setPortOfOriginId(lclBookingForm.getQuickdrPortOfOriginId());
            UnLocation unLocationForPortOrigin = unLocationDAO.findById(lclBookingForm.getQuickdrPortOfOriginId());
            lclBookingForm.getLclBooking().setPortOfOrigin(unLocationForPortOrigin);
            lclBookingForm.setFinalDestinationId(lclBookingForm.getFinalDestinationIdForDr());
            lclBookingForm.getLclBooking().setFinalDestination(unLocFd);
            lclBookingForm.setFinalDestinationId(lclBookingForm.getFinalDestinationIdForDr());
            lclBookingDAO.saveOrUpdate(lclBookingForm.getLclBooking());

            new ExportBookingUtils().setLclBookingExport(lclBookingForm, lclFileNumber, request, loginUser);
            lclFileNumberDAO.updateEconoEculine(lclBookingForm.getBusinessUnit().toUpperCase(), String.valueOf(lclFileNumber.getId()));
            LclBookingPiece lclBookingPiece = new LclBookingPiece();
            if (lclBookingForm.getBookedWeightImperial()
                    != null && !lclBookingForm.getBookedWeightImperial().equals("")) {
                BigDecimal weightImp = new BigDecimal(0.000);
                weightImp = new BigDecimal(lclBookingForm.getBookedWeightImperial());
                lclBookingPiece.setBookedWeightImperial(weightImp);
                lclBookingPiece.setBookedWeightMetric(weightImp.divide(new BigDecimal(2.2046), 3, BigDecimal.ROUND_FLOOR));
            } else {
                lclBookingPiece.setBookedWeightImperial(null);
                lclBookingPiece.setBookedWeightMetric(null);
            }

            if (lclBookingForm.getBookedVolumeImperial()
                    != null && !lclBookingForm.getBookedVolumeImperial().equals("")) {
                BigDecimal measureImp = new BigDecimal(0.000);
                measureImp = new BigDecimal(lclBookingForm.getBookedVolumeImperial());
                lclBookingPiece.setBookedVolumeImperial(measureImp);
                lclBookingPiece.setBookedVolumeMetric(measureImp.divide(new BigDecimal(35.314), 3, BigDecimal.ROUND_FLOOR));
            } else {
                lclBookingPiece.setBookedVolumeImperial(null);
                lclBookingPiece.setBookedVolumeMetric(null);
            }

            if (lclBookingForm.getBookedPieceCount()
                    != null && !lclBookingForm.getBookedPieceCount().equals("")) {
                lclBookingPiece.setBookedPieceCount(Integer.parseInt(lclBookingForm.getBookedPieceCount()));
            } else {
                lclBookingPiece.setBookedPieceCount(0);
            }
            lclBookingPiece.setPackageType(new PackageTypeDAO().findPackage("SKD"));
            CommodityType commodityType = new commodityTypeDAO().getCommodityCode(lclBookingForm.getCommodityNoForDr());
            lclBookingPiece.setCommodityType(commodityType);
            lclBookingPiece.setPieceDesc(commodityType.getDescEn().toUpperCase());
            lclBookingPiece.setEnteredBy(loginUser);
            lclBookingPiece.setModifiedBy(loginUser);
            lclBookingPiece.setEnteredDatetime(now);
            lclBookingPiece.setModifiedDatetime(now);
            lclBookingPiece.setLclFileNumber(lclFileNumber);
            lclBookingPiece.setHazmat((lclBookingForm.isHazmat()));
            lclBookingPiece.setPersonalEffects("N");
            lclBookingPiece.setRefrigerationRequired(false);
            new LclBookingPieceDAO().saveOrUpdate(lclBookingPiece);
            if (null != lclBookingForm.getLclBooking().getPortOfOrigin() && null != lclBookingForm.getLclBooking().getPortOfDestination()) {
                String unlocationCode = "T".equalsIgnoreCase(lclBookingForm.getLclBooking().getBookingType()) ? lclBookingForm.getLclBooking().getPortOfDestination().getUnLocationCode()
                        : lclBookingForm.getLclBooking().getPortOfOrigin().getUnLocationCode();
                Integer warehouseId = new WarehouseDAO().getWarehouseId(unlocationCode, "B");
                if (CommonUtils.isNotEmpty(warehouseId)) {
                    new LclBookingPieceWhseDAO().insertLclBookingPieceWhse(lclBookingPiece.getId(), warehouseId, loginUser.getUserId());
                }
            }
            LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", lclFileNumber.getId());
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            request.setAttribute("lclBooking", lclBooking);

            request.setAttribute(
                    "fileNo", lclBooking.getLclFileNumber().getFileNumber());
            lcldwr.cargoRecivedBookingDispo(
                    "B", lclBooking.getLclFileNumber().getId(), loginUser.getUserId(), null, null, null);
            lcldwr.cargoRecivedBookingDispo(
                    "RUNV", lclBooking.getLclFileNumber().getId(), loginUser.getUserId(), lclBooking.getPortOfOrigin().getId(), null, null);
            new LclRemarksDAO().insertLclRemarks(lclBooking.getLclFileNumber().getId(),
                    REMARKS_DR_AUTO_NOTES, "Inventory Status->WAREHOUSE(Un-verified)", loginUser.getUserId());
            lclFileNumberDAO.updateLclFileNumbersStatus(String.valueOf(lclBooking.getLclFileNumber().getId()), "W");
            // ---Cargo verified -- copy booked to actual---
            //  new LclBookingPieceDAO().bookedValueToActualValue(lclFileNumber.getId());
//            lclFileNumber.setStatus("W");
//            lclFileNumberDAO.saveOrUpdate(lclFileNumber);
            //  lclFileNumberDAO.getSession().getTransaction().commit();
            if ("Exports".equalsIgnoreCase(lclBookingForm.getModuleName())) {
                LclConsolidate consolidate = new LclConsolidateDAO().getByProperty("lclFileNumberA.id", lclFileNumber.getId());
                if (consolidate == null) {
                    new LclConsolidateDAO().insertLCLConsolidation(lclFileNumber.getId(), lclFileNumber.getId(), loginUser, new Date());
                }
            }
        }
        if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
            lclFileNumberDAO.getSession().getTransaction().begin();
        }
        return mapping.findForward("mainscreen");
    }

    public ActionForward deleteCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pooUnCode = "";
        String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        String id = request.getParameter("cid");
        String costStatus = lclBookingAcTransDAO.getTransType(Long.parseLong(id));
        String fileId = request.getParameter("fileNumberId");
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileId));
        String fileNumberStatus = lclFileNumber.getStatus();
        String moduleName = request.getParameter("moduleName");
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        if (CommonUtils.isNotEmpty(id)) {
            LclBookingAc lclBookingAc = lclCostChargeDAO.findById(Long.parseLong(id));
            StringBuilder remarks = new StringBuilder();
            remarks.append("DELETED -> Code -> ");
            String code;
            if (null != lclBookingAc.getArglMapping() && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())) {
                code = lclBookingAc.getArglMapping().getChargeCode();
            } else {
                code = lclBookingAc.getApglMapping().getChargeCode();
            }
            remarks.append(code);
            if (CommonUtils.notIn(costStatus, "AP", "IP", "DS")
                    && NumberUtils.isNotZero(lclBookingAc.getApAmount())
                    && null != lclBookingAc.getApglMapping() && CommonUtils.isNotEmpty(lclBookingAc.getApglMapping().getChargeCode())) {
                String lclBookingAcTransIds = lclBookingAcTransDAO.getConcatenatedBookingAcTransIds(lclBookingAc.getId().toString());
                remarks.append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setApBillToParty(null);
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                lclBookingAc.setDeleted(Boolean.TRUE);
                lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                lclCostChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
            }
            if (LCL_EXPORT.equalsIgnoreCase(moduleName) && lclBookingAc.getArglMapping().isDestinationServices()
                    && CommonUtils.notIn(costStatus, "AP", "IP", "DS") && NumberUtils.isNotZero(lclBookingAc.getApAmount())) {
                remarks.append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setApBillToParty(null);
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
            }
            if ((CommonUtils.isNotEqual(fileNumberStatus, "M") && CommonUtils.isNotEqual(lclBookingAc.getArBillToParty(), "A"))
                    || (CommonUtils.isEqual(lclBookingAc.getArBillToParty(), "A") && CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo()))) {
                remarks.append(" Charge Amount -> ").append(lclBookingAc.getArAmount());
                lclBookingAc.setSpReferenceNo(null);
                lclBookingAc.setArBillToParty(null);
                lclBookingAc.setArAmount(BigDecimal.ZERO);
            }
            if (CommonUtils.isEqual(fileNumberStatus, "M")
                    && CommonUtils.isEqual(lclBookingAc.getArBillToParty(), "W")) {
                lclBookingAc.setArAmount(BigDecimal.ZERO);
            }
            lclBookingAc.setModifiedDatetime(now);
            lclBookingAc.setModifiedBy(loginUser);
            lclCostChargeDAO.saveOrUpdate(lclBookingAc);
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
            if (CommonUtils.in(code, CHARGE_CODE_DOOR, CHARGE_CODE_INLAND)) {
                LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
                LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(Long.parseLong(fileId));
                if (null != lclBookingPad && null != lclBookingPad.getLclBookingAc()
                        && CommonUtils.isEqual(lclBookingPad.getLclBookingAc().getId(), lclBookingAc.getId())) {
                    lclBookingPad.setScac("");
                    lclBookingPadDAO.saveOrUpdate(lclBookingPad);
                }
            }
        }
        LclBooking lclBooking = lclBookingDAO.findById(Long.parseLong(fileId));
        lclBookingDAO.getCurrentSession().evict(lclBooking);
        lclBooking.setModifiedBy(loginUser);
        lclBooking.setModifiedDatetime(now);
        lclBookingDAO.getCurrentSession().clear();
        lclBookingDAO.saveOrUpdate(lclBooking);
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));//BookingPiece List
        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(fileId), moduleName);//Charge List
        if (LCL_EXPORT.equalsIgnoreCase(moduleName)) {//Export
            String rateType = lclBooking.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            PortsDAO portsDAO = new PortsDAO();
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(pooUnCode, rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = null;
            if (null != lclBooking.getPortOfLoading()) {
                refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
            }
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = null;
            if (null != lclBooking.getPortOfDestination()) {
                portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
            }
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                destinationfd = ports.getEciportcode();
            }
            String pcBoth = request.getParameter("pcBoth");
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(Long.parseLong(fileId), "CAF", false);
            if (lclBookingAc != null) {
                lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod, lclCommodityList, pcBoth,
                        getCurrentUser(request), Long.parseLong(fileId), request, ports, lclBooking.getBillToParty());
            }
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, Long.parseLong(fileId), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), ports.getEngmet(), "No");
        } else {
            lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(fileId), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), "", "");
        }
        if ("Exports".equalsIgnoreCase(moduleName) && "T".equalsIgnoreCase(lclBooking.getBookingType())) {
            lclUtils.setTemBillToPartyList(request, "");
        }
        request.setAttribute("lclBooking", lclBooking);
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward renderChargeDescAdjustment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LCLBookingForm lclBookingForm = (LCLBookingForm) form;
            List lclBookingPiecesList = null;
            if (CommonUtils.isNotEmpty(lclBookingForm.getFileNumberId())) {
                Long fileNumberId = new Long(lclBookingForm.getFileNumberId());
                LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
                LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
                LclBooking lclBooking = lclBookingDAO.findById(fileNumberId);
                lclBookingDAO.getCurrentSession().evict(lclBooking);
                request.setAttribute("lclBooking", lclBooking);
                lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                PortsDAO portsdao = new PortsDAO();

                Ports ports = portsdao.getByProperty("unLocationCode", lclBookingForm.getDestination());
                String engmet = "";
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    engmet = ports.getEngmet();
                }
                if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                    List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_EXPORT);
                    lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
                    lclUtils.setRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargesDAO, lclBookingPiecesList, lclBookingForm.getPcBoth(), engmet, "No");
                }
            }
        } catch (Exception e) {
            log.info("Error in renderChargeDescAdjustment method,  on " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward deleteDocumCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            PortsDAO portsDAO = new PortsDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBooking lclBooking = bookingDAO.findById(longFileId);
            bookingDAO.getCurrentSession().evict(lclBooking);
            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "", engmet = "";
            Ports ports = null;

            String rateType = lclBooking.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }
            String chargeCode = LclCommonConstant.BLUESCREEN_CHARGECODE_DOCUM;
            String documentation = request.getParameter("documentation");
            LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, chargeCode, true);
            if (documentation.equals("N") && lclBookingAc != null) {
                String remarksDesc = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBookingAc.getArAmount();
                lclCostChargeDAO.delete(lclBookingAc);
                lclBooking.setDocumentation(false);
                lclBooking.setModifiedBy(loginUser);
                lclBooking.setModifiedDatetime(new Date());
                bookingDAO.saveOrUpdate(lclBooking);
                new LclRemarksDAO().insertLclRemarks(longFileId, REMARKS_DR_AUTO_NOTES, remarksDesc, loginUser.getUserId());
            }
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, LclCommonConstant.LCL_EXPORT);
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            String pcBoth = request.getParameter("pcBoth");
            lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, "CAF", false);
            if (lclBookingAc != null) {
                lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod,
                        lclCommodityList, pcBoth, loginUser, longFileId, request, ports, lclBooking.getBillToParty());
            }
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList,
                    lclBooking.getBillingType(), engmet, "No");
        } catch (Exception e) {
            log.info("Error in LCL deleteDocumCharge method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward deleteConsolidate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
            if (CommonUtils.isNotEmpty(request.getParameter("consolidateId"))) {
                Long fileId = Long.parseLong(request.getParameter("consolidateId"));
                List<Long> relatedConsolidateFiles = lclConsolidateDAO.getConsolidatesFiles(fileId);
                LclConsolidate consolidate = lclConsolidateDAO.getByProperty("lclFileNumberA.id", fileId);
                if (consolidate.getLclFileNumberA().getState().equalsIgnoreCase("BL")) {
                    new LclFileNumberDAO().updateFileNumberState(consolidate.getLclFileNumberA().getId().toString(), "B");
                }
                Long currentFileId = consolidate.getLclFileNumberB().getId();
                consolidate.setLclFileNumberB(consolidate.getLclFileNumberA());
                lclConsolidateDAO.update(consolidate);
                String remarks = "Deconsolidated -> " + new LclFileNumberDAO().getFileNumberByFileId(currentFileId.toString());
                lclUtils.insertLCLRemarks(fileId, remarks, LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                for (Long consolidateFileId : relatedConsolidateFiles) {
                    remarks = "Deconsolidated -> " + new LclFileNumberDAO().getFileNumberByFileId(fileId.toString());
                    lclUtils.insertLCLRemarks(consolidateFileId, remarks, LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                }
                String acceptance_Remarks = request.getParameter("acceptance_remarks");
                if (CommonUtils.isNotEmpty(request.getParameter("current_file_id"))) {
                    new ExportBookingUtils().updateConsolidationBLCharges(Long.parseLong(request.getParameter("current_file_id")),
                            null != acceptance_Remarks ? acceptance_Remarks : "", getCurrentUser(request), request);
                }
                lclUtils.getFormattedConsolidatedList(request, Long.parseLong(request.getParameter("fileNumberId")));
                request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(Long.parseLong(request.getParameter("fileNumberId"))));
            }
        } catch (Exception e) {
            log.info("Error in deleteConsolidate method. ", e);
            return mapping.findForward(CONSOLIDATE_DESC);
        }
        return mapping.findForward(CONSOLIDATE_DESC);
    }

    public ActionForward deletePickUp(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileId = request.getParameter("fileId");
        if (CommonUtils.isNotEmpty(fileId)) {
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            String moduleName = request.getParameter("moduleName");
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            String chargeCode = "";
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                chargeCode = LclCommonConstant.CHARGE_CODE_DOOR;
            } else {
                chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
            }
            LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(Long.parseLong(fileId));
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            Date now = new Date();
            boolean costFlag = false;
            User loginUser = getCurrentUser(request);
            String costStatus = "";
            LclBooking lclBooking = lclBookingDAO.findById(Long.parseLong(fileId));
            lclBooking.setPooPickup(false);
            lclBooking.setModifiedBy(getCurrentUser(request));
            lclBooking.setModifiedDatetime(new Date());
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            lclBookingDAO.update(lclBooking);
            LclBookingAc lclBookingAc = new LclCostChargeDAO().getLclBookingAcByChargeCode(Long.parseLong(fileId), chargeCode);
            if (lclBookingAc != null) {
                costStatus = new LCLBookingAcTransDAO().getTransType(lclBookingAc.getId());
                request.setAttribute("transType", costStatus);
            }
            if (lclBookingAc != null && CommonUtils.isNotEmpty(lclBookingForm.getFileNumberStatus()) && !lclBookingForm.getFileNumberStatus().equals("M")) {
                StringBuilder remarks = new StringBuilder();
                if (costStatus.equals("AP") && CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                    if (lclBookingAc.getArglMapping().getChargeCode() != null) {
                        remarks.append("DELETED -> Code -> ").append(lclBookingAc.getArglMapping().getChargeCode()).append(" Charge Amount -> ").append(lclBookingAc.getArAmount());
                    }
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
                    lclBookingAc.setArAmount(BigDecimal.ZERO);
                    lclBookingAc.setArBillToParty(null);
                    lclBookingAc.setApBillToParty(null);
                    lclBookingAc.setModifiedDatetime(now);
                    lclBookingAc.setControlNo(lclBookingAc.getControlNo());
                    lclCostChargeDAO.saveOrUpdate(lclBookingAc);
                    if (lclBookingPad != null) {
                        lclBookingPadDAO.deleteBookingPadByFileId(Long.parseLong(fileId));
                    }
                } else if (!costStatus.equals("AP") && CommonUtils.isNotEmpty(lclBookingAc.getSpReferenceNo())) {
                    if (lclBookingAc.getArglMapping().getChargeCode() != null) {
                        remarks.append("DELETED -> Code -> ").append(lclBookingAc.getArglMapping().getChargeCode()).append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                    }
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
                    lclBookingAc.setApAmount(BigDecimal.ZERO);
                    lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                    lclBookingAc.setSupAcct(null);
                    lclBookingAc.setDeleted(Boolean.TRUE);
                    lclBookingAc.setModifiedDatetime(now);
                    lclBookingAc.setControlNo(lclBookingAc.getControlNo());
                    lclCostChargeDAO.saveOrUpdate(lclBookingAc);
                    if (lclBookingPad != null) {
                        lclBookingPadDAO.deleteBookingPadByFileId(Long.parseLong(fileId));
                    }
                    lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                    lclCostChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                } else if (!costStatus.equals("AP") && (lclBookingAc.getArAmount() == null || lclBookingAc.getArAmount().doubleValue() == 0.00)) {
                    if (lclBookingAc.getArglMapping().getChargeCode() != null) {
                        remarks.append("DELETED -> Code -> ").append(lclBookingAc.getArglMapping().getChargeCode()).append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                    }
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
                    if (lclBookingAc.getControlNo() == 0) {
                        lclCostChargeDAO.deleteChargesById(lclBookingAc.getId().toString());
                    } else {
                        lclBookingAc.setApAmount(BigDecimal.ZERO);
                        lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                        lclBookingAc.setSupAcct(null);
                        lclBookingAc.setDeleted(Boolean.TRUE);
                        lclBookingAc.setModifiedDatetime(now);
                        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
                        if (lclBookingPad != null) {
                            lclBookingPadDAO.deleteBookingPadByFileId(Long.parseLong(fileId));
                        }
                    }
                    lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                    lclCostChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                } else if (!costStatus.equals("AP") && CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())
                        && lclBookingAc.getArAmount() != null && lclBookingAc.getArAmount().doubleValue() != 0.00) {
                    if (lclBookingAc.getArglMapping().getChargeCode() != null) {
                        remarks.append("DELETED -> Code -> ").append(lclBookingAc.getArglMapping().getChargeCode()).append(" Charge Amount -> ").append(lclBookingAc.getArAmount());
                    }
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
                    if (lclBookingAc.getControlNo() == 0) {
                        lclCostChargeDAO.deleteChargesById(lclBookingAc.getId().toString());
                    } else {
                        lclBookingAc.setApAmount(BigDecimal.ZERO);
                        lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                        lclBookingAc.setArAmount(BigDecimal.ZERO);
                        lclBookingAc.setSupAcct(null);
                        lclBookingAc.setDeleted(Boolean.TRUE);
                        lclBookingAc.setModifiedDatetime(now);
                        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
                        if (lclBookingPad != null) {
                            lclBookingPadDAO.deleteBookingPadByFileId(Long.parseLong(fileId));
                        }
                    }
                    lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                    lclCostChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                }
            } else if (lclBookingPad != null) {
                lclBookingPadDAO.deleteBookingPadByFileId(Long.parseLong(fileId));
            }

            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(fileId), moduleName);
            if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                RefTerminalDAO refterminaldao = new RefTerminalDAO();
                PortsDAO portsDAO = new PortsDAO();
                String engmet = new String();
                String rateType = lclBooking.getRateType();
                if (rateType.equalsIgnoreCase("R")) {
                    rateType = "Y";
                }
                if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin()) && CommonFunctions.isNotNull(lclBooking.getPortOfOrigin().getUnLocationCode())) {
                    RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
                    if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                        pooorigin = refterminal.getTrmnum();
                    }
                }
                RefTerminal refterminalpol = null;
                if (null != lclBooking.getPortOfLoading()) {
                    refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
                }
                if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                    polorigin = refterminalpol.getTrmnum();
                }
                Ports portspod = null;
                if (null != lclBooking.getPortOfDestination()) {
                    portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
                }
                if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                    destinationpod = portspod.getEciportcode();
                }
                Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    destinationfd = ports.getEciportcode();
                    engmet = ports.getEngmet();
                }
                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                lclBookingAc = lclCostChargeDAO.manaualChargeValidate(lclBooking.getLclFileNumber().getId(), "CAF", false);
                if (lclBookingAc != null) {
                    lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod, lclCommodityList, lclBooking.getBillingType(),
                            getCurrentUser(request), lclBooking.getLclFileNumber().getId(), request, ports, lclBooking.getBillToParty());
                }
                lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
                lclUtils.setRolledUpChargesForBooking(chargeList, request, Long.parseLong(fileId), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
            } else {
                lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(fileId), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), "", "");
            }
            LclSsHeader lclssheader = CommonUtils.isNotEmpty(lclBookingForm.getHeaderId()) ? new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId())) : null;
            new LclBookingImportDAO().deleteDoorDeliveryData(Long.parseLong(fileId), loginUser.getUserId());
            request.setAttribute("lclssheader", lclssheader);
            request.setAttribute("chargeList", chargeList);
            request.setAttribute("lclBooking", lclBooking);
        }
        return mapping.findForward("chargeDescription");
    }

    public ActionForward closeDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String bookingPieceId = request.getParameter("bookingPieceId");
        String fileNumberId = request.getParameter("fileNumberId");
        List<LclBookingPiece> lclPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", new Long(fileNumberId));
        request.setAttribute("ofspotrate", lclPieceList.get(0).getLclFileNumber().getLclBooking().getSpotRate());
        for (LclBookingPiece lbp : lclPieceList) {
            lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(new Long(fileNumberId), lbp.getId()));
            lbp.setLclBookingAcList(new LclCostChargeDAO().findByFileAndCommodityList(new Long(fileNumberId), lbp.getId()));
            lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
            lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
        }
        request.setAttribute("lclCommodityList", lclPieceList);
        return mapping.findForward("commodityDesc");
    }

    public ActionForward closeHazmat(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        List<LclBookingPiece> lclPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", new Long(fileNumberId));
        for (LclBookingPiece lbp : lclPieceList) {
            lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(new Long(fileNumberId), lbp.getId()));
            lbp.setLclBookingAcList(new LclCostChargeDAO().findByFileAndCommodityList(new Long(fileNumberId), lbp.getId()));
            lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
            lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
        }
        request.setAttribute("lclCommodityList", lclPieceList);
        return mapping.findForward("commodityDesc");
    }

    public ActionForward closeSched(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumber = request.getParameter("shpdr");
        this.setAesDetailsList(fileNumber, request);//Set AES Details List
        return mapping.findForward("aesList");
    }

    public ActionForward displayAES(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3pRefAesList(Long.parseLong(fileNumberId)));
        }
        request.setAttribute("lclBookingForm", lclBookingForm);
        return mapping.findForward("aes");
    }

    public ActionForward addAES(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        Long fileNumberId = Long.parseLong(lclBookingForm.getFileNumberId());
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        Date now = new Date();
        String FORWARD_PAGE = "aes";
        String remarksType = "", remarks = "";
        String fileId = request.getParameter("fileNumberId");
        String noteId = request.getParameter("thirdPName");
        User loginUser = getCurrentUser(request);
        Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNo();
        if (CommonUtils.isNotEmpty(lclBookingForm.getAesItnNumber())) {
            lcl3pRefNo.setReference(lclBookingForm.getAesItnNumber().toUpperCase());
            lcl3pRefNo.setType("AES_ITNNUMBER");
            remarks = "Inserted - AES/ITN# " + " " + lcl3pRefNo.getReference();
            remarksType = noteId;

        } else {
            lcl3pRefNo.setReference(lclBookingForm.getAesException().toUpperCase());
            lcl3pRefNo.setType("AES_EXCEPTION");
            remarks = "Inserted - AES/ITN# " + " " + lcl3pRefNo.getReference();
            remarksType = noteId;

        }
        lcl3pRefNo.setLclFileNumber(new LclFileNumber(fileNumberId));
        lcl3pRefNo.setEnteredBy(getCurrentUser(request));
        lcl3pRefNo.setModifiedBy(getCurrentUser(request));
        lcl3pRefNo.setModifiedDatetime(now);
        lcl3pRefNo.setEnteredDatetime(now);
        lcl3pRefNoDAO.save(lcl3pRefNo);
        request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3pRefAesList(fileNumberId));
        if (fileId != null && remarksType != null) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks, loginUser.getUserId());
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public ActionForward refreshCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileNumberId");
        List<LclBookingPiece> lclBookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        if (!lclBookingPieceList.isEmpty()) {
            request.setAttribute("lclCommodityList", lclBookingPieceList);
        }
        return mapping.findForward("commodityDesc");
    }

    public ActionForward newQuickDr(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
        return mapping.findForward("quickDr");
    }

    public ActionForward importNewBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclBooking lclBooking = new LclBooking();
        LclBookingImport lclBookingImport = new LclBookingImport();
        LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
        LclUnitSsImportsDAO lclUnitSsImportsDAO = new LclUnitSsImportsDAO();
        User loginUser = getCurrentUser(request);
        LclDwr lclDwr = new LclDwr();
        lclBookingForm.setShipperToolTip(lclDwr.getContactDetails("Shipper", "", "", ""));
        lclBookingForm.setConsigneeToolTip(lclDwr.getContactDetails("Consignee", "", "", ""));
        lclBookingForm.setNotifyToolTip(lclDwr.getContactDetails("Notify", "", "", ""));
        lclBookingForm.setNotify2ToolTip(lclDwr.getContactDetails("Notify2", "", "", ""));
        LclSsHeader lclssheader = lclSsHeaderDAO.findById(Long.parseLong(lclBookingForm.getHeaderId()));
        LclUnitSs lclUnitSs = lclUtils.getLclUnitSSFromHeaderList(Long.parseLong(lclBookingForm.getUnitSsId()), lclssheader);
        lclBookingForm.setUnitStatus(lclUnitSs.getStatus());
        LclSsDetail lclSsDeatil = new LclSsDetailDAO().findByTransMode(lclssheader.getId(), "V");
        lclBookingForm.setPdfDocumentName(new LclUnitSsDispoDAO().getPdfDocumentName(lclUnitSs.getLclUnit().getId().toString(), lclSsDeatil.getId()));
        LclUnitSsImports lclUnitSsImports = lclUnitSsImportsDAO.getLclUnitSSImportsByHeader(lclUnitSs.getLclUnit().getId(), Long.parseLong(lclBookingForm.getHeaderId()));
        if (CommonFunctions.isNotNull(lclssheader)) {
            lclBookingForm.setDispositionToolTip(lclBookingUtils.getImpUnitDispostionDesc(lclUnitSs.getLclUnit().getId(), lclSsDeatil.getId()));
            if (CommonFunctions.isNotNull(lclssheader.getOrigin())) {
                request.setAttribute("pol", lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getOrigin()));
                request.setAttribute("portOfLoadingId", lclssheader.getOrigin().getId());
                request.setAttribute("polUnlocationcode", lclssheader.getOrigin().getUnLocationCode());
                //agent
                agentCount(lclssheader.getOrigin().getUnLocationCode(), request);
                String disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(lclUnitSs.getLclUnit().getId(), lclSsDeatil.getId());
                if (CommonUtils.isNotEmpty(disposition)) {
                    lclBookingForm.setDisposition(disposition);
                }
            }
            if (CommonFunctions.isNotNull(lclssheader.getDestination())) {
                request.setAttribute("pod", lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getDestination()));
                request.setAttribute("podUnlocationcode", lclssheader.getDestination().getUnLocationCode());
                request.setAttribute("portOfDestinationId", lclssheader.getDestination().getId());
                lclBookingForm.setFinalDestination(lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getDestination()));
                lclBookingForm.setFinalDestinationId(lclssheader.getDestination().getId());
            }
            if (CommonFunctions.isNotNull(lclssheader.getScheduleNo())) {
                lclBookingForm.setImpEciVoyage(lclssheader.getScheduleNo().toUpperCase());
            }
            if (CommonFunctions.isNotNull(lclssheader.getOwnerUserId())) {
                lclBookingForm.setVoyageOwner(lclssheader.getOwnerUserId().getLoginName().toUpperCase());
            }
            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                lclBookingForm.setImpUnitNo(lclUnitSs.getLclUnit().getUnitNo().toUpperCase());
            }
            if (CommonFunctions.isNotNull(lclssheader.getBillingTerminal())) {
                lclBookingForm.setTerminal(lclssheader.getBillingTerminal().getTrmnum() + " - " + lclssheader.getBillingTerminal().getTerminalLocation());
                lclBookingForm.setTrmnum(lclssheader.getBillingTerminal().getTrmnum());
            } else {
                lclBookingForm.setTerminal(loginUser.getImportTerminal().getTrmnum() + " - " + loginUser.getImportTerminal().getTerminalLocation());
                lclBookingForm.setTrmnum(loginUser.getImportTerminal().getTrmnum());
            }
            LclSsDetailDAO lclSsDetailDAO = new LclSsDetailDAO();
            LclSsDetail lclSsDetail = lclSsDetailDAO.findByTransMode(lclssheader.getId(), "V");
            if (CommonFunctions.isNotNull(lclSsDetail)) {
                if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                    lclBookingForm.setImpSsVoyage(lclSsDetail.getSpReferenceNo());
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getStd())) {
                    lclBookingForm.setImpSailDate(DateUtils.formatDate(lclSsDetail.getStd(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo()) && CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo().getAccountName())) {
                    lclBookingForm.setImpSsLine(lclSsDetail.getSpAcctNo().getAccountName());
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                    lclBookingForm.setImpVesselName(lclSsDetail.getSpReferenceName());
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getArrival())) {
                    lclBookingForm.setImpVesselArrival(DateUtils.formatDate(lclSsDetail.getSta(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getDeparture())) {
                    lclBookingForm.setImpPier(lclUtils.getConcatenatedOriginByUnlocation(lclSsDetail.getArrival()));
                }
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSsImports)) {
            if (CommonFunctions.isNotNull(lclUnitSsImports.getLastFreeDate())) {
                request.setAttribute("lastFdDate", DateUtils.formatDate(lclUnitSsImports.getLastFreeDate(), "dd-MMM-yyyy"));
            }
            if (CommonFunctions.isNotNull(lclUnitSsImports.getItDatetime())) {
                lclBookingForm.setUnitItDate(DateUtils.formatDate(lclUnitSsImports.getItDatetime(), "dd-MMM-yyyy"));
            }
            if (CommonFunctions.isNotNull(lclUnitSsImports.getItNo())) {
                lclBookingForm.setUnitItNo(lclUnitSsImports.getItNo());
            }
            if (CommonFunctions.isNotNull(lclUnitSsImports.getItPortId())) {
                lclBookingForm.setUnitItPort(lclUtils.getConcatenatedOriginByUnlocation((lclUnitSsImports.getItPortId())));
            }
            if (CommonFunctions.isNotNull(lclUnitSsImports.getApproxDueDate())) {
                lclBookingForm.setImportApproxDue(DateUtils.formatDate(lclUnitSsImports.getApproxDueDate(), "dd-MMM-yyyy"));
            }
            if (CommonFunctions.isNotNull(lclUnitSsImports.getOriginAcctNo())) {
                TradingPartner tradingPartner = new TradingPartnerDAO().findById(lclUnitSsImports.getOriginAcctNo().getAccountno());
                lclBooking.setAgentAcct(tradingPartner);
                lclBooking.getSupContact().setCompanyName(tradingPartner.getAccountName());
                lclBooking.setSupAcct(tradingPartner);
                if (lclUnitSsImports.getOriginAcctNo().getAccountno() != null) {
                    String newBrand = new TradingPartnerDAO().getBusinessUnit(lclUnitSsImports.getOriginAcctNo().getAccountno());
                    lclBookingForm.setBusinessUnit(newBrand);
                }
            }
            String coName = "";
            CustAddressDAO custAddressDAO = new CustAddressDAO();
            if ("COLOAD".equalsIgnoreCase(lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase())) {
                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo())) {
                    setColoadDevanning(lclUnitSsImports.getColoaderDevanningAcctNo(), lclBookingForm);
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName())) {
                        lclBookingForm.setImpCFSWareName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName());
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno())) {
                        lclBookingForm.setCfsWarehouseNo(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                        coName = custAddressDAO.getCoName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                        lclBookingForm.setCfsWarehouseCoName(coName);
                    }
                }
            } else if (lclUnitSsImports.getCfsWarehouseId() != null && lclUnitSsImports.getCfsWarehouseId().getId() != null) {
                lclBookingForm.setImpCFSWareName(lclUnitSsImports.getCfsWarehouseId().getWarehouseName());
                lclBookingForm.setCfsWarehouseNo(lclUnitSsImports.getCfsWarehouseId().getWarehouseNo());
                coName = custAddressDAO.getCoName(lclUnitSsImports.getCfsWarehouseId().getVendorNo());
                lclBookingForm.setCfsWarehouseCoName(coName);
                lclBookingForm.setImpCfsWarehsId(String.valueOf(lclUnitSsImports.getCfsWarehouseId().getId()));
                setCfsWarehouse(lclUnitSsImports.getCfsWarehouseId(), lclBookingForm);
            }
            if (lclUnitSsImports.getUnitWareHouseId() != null && lclUnitSsImports.getUnitWareHouseId().getId() != null) {
                lclBookingForm.setImpUnitWareNo(lclUnitSsImports.getUnitWareHouseId().getWarehouseName());
                setUnitWarehouse(lclUnitSsImports.getUnitWareHouseId(), lclBookingForm);
            }
            LclUnitWhse lclUnitWhse = new LclUnitWhseDAO().getLclUnitWhseDetails(lclUnitSs.getLclUnit().getId(), lclssheader.getId());
            if (lclUnitWhse != null) {
                if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                    lclBookingForm.setImpStripDate(DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy"));
                }
            }
            LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclssheader.getId());
            if (lclUnitSsManifest != null) {
                if (CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                    lclBookingForm.setMasterBl(lclUnitSsManifest.getMasterbl().toUpperCase());
                }
            }
        }
        request.setAttribute("lclUnitSs", lclUnitSs);
        lclUtils.setUnknowDestValues(request);//set default Unknow Destination Values
        lclBooking.setEnteredBy(loginUser);
        lclBooking.setEnteredDatetime(DateUtils.formatDateAndParseDate(new Date()));
        lclBookingForm.setModuleName("Imports");
        request.setAttribute("lclBookingForm", lclBookingForm);
        request.setAttribute("lclBookingImport", lclBookingImport);
        request.setAttribute("lclBooking", lclBooking);
        request.setAttribute("moduleId", request.getParameter("moduleId"));
        request.setAttribute("error", false);
        return mapping.findForward("importBooking");
    }

    public ActionForward refreshChargesTab(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        Ports ports = null;
        String origin = "";
        String destination = "";
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
        destination = lclBooking.getFinalDestination().getUnLocationName().toUpperCase();
        if (lclBooking.getPortOfOrigin() != null) {
            origin = lclBooking.getPortOfOrigin().getUnLocationName().toUpperCase();
        } else if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
            origin = lclBooking.getPortOfLoading().getUnLocationName().toUpperCase();
        }
        String agentNo = "";
        if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
            agentNo = lclBooking.getAgentAcct().getAccountno();
        }
        request.setAttribute("origin", origin);
        request.setAttribute("destination", destination);
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
        List<LclBookingAc> chargeList = new LclCostChargeDAO().getLclCostByFileNumberAsc(Long.parseLong(lclBookingForm.getFileNumberId()), lclBookingForm.getModuleName());
        request.setAttribute("chargeList", chargeList);
        request.setAttribute("lclBooking", lclBooking);
        if ("Imports".equalsIgnoreCase(lclBookingForm.getModuleName())) {
            request.setAttribute("unitSsCollectType", request.getParameter("unitSsCollectType"));
            lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, ports);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(lclBookingForm.getFileNumberId()), new LclCostChargeDAO(), lclCommodityList, lclBooking.getBillingType(), "", agentNo);
        } else {
            ports = new PortsDAO().getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, lclBooking.getFileNumberId(), new LclCostChargeDAO(),
                    lclCommodityList, lclBooking.getBillingType(), ports.getEngmet(), "No");
        }
        if (lclBookingForm.getHeaderId() != null && !lclBookingForm.getHeaderId().equalsIgnoreCase("")) {
            LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId()));
            request.setAttribute("lclssheader", lclssheader);
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward getPredefinedRemarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String multipleSelect = request.getParameter("multipleSelect");
        String moduleName = request.getParameter("moduleName");
        String from = request.getParameter("from");
        Integer Id = null;
        String description = null;
        if ("IMPORTS".equalsIgnoreCase(moduleName)) {
            description = CommonConstants.LCL_PREDEFINED_REMARKS_LCLI_CODE;
        } else {
            Id = CommonConstants.LCL_PREDEFINED_REMARKS_CODE;
        }
        List<String> genericCodeList = new GenericCodeDAO().findByCodeTypeDestination(Id, description);
        request.setAttribute("genericCodeList", genericCodeList);
        request.setAttribute("multipleSelect", multipleSelect);
        request.setAttribute("from", from);
        return mapping.findForward("remarks");
    }

    public void calculateBkgRates(ActionForm form, HttpServletRequest request, List<LclBookingPiece> bkgCommodityList) throws Exception {
        LCLBookingForm lclBkgForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        String fileId = request.getParameter("fileNumberId");
        User user = (User) request.getSession().getAttribute("loginuser");
        LclChargesCalculation calculation = new LclChargesCalculation();
        String fromZip = null;
        String rateType = lclBkgForm.getRateType();
        if (null != rateType && !rateType.trim().equals("")) {
            if ("R".equalsIgnoreCase(rateType)) {
                rateType = "Y";
            }
        }
        if (lclBkgForm.getDoorOriginCityZip() != null && !lclBkgForm.getDoorOriginCityZip().trim().equals("")) {
            String[] zip = lclBkgForm.getDoorOriginCityZip().split("-");
            fromZip = zip[0];
        }
        calculation.calculateRates(lclBkgForm.getOriginUnlocationCode(), lclBkgForm.getDestination(), lclBkgForm.getPolCode(),
                lclBkgForm.getPodCode(), Long.parseLong(fileId), bkgCommodityList, user, lclBkgForm.getPooDoor(),
                lclBkgForm.getInsurance(), lclBkgForm.getLclBooking().getValueOfGoods(), rateType, "C", null, null,
                fromZip, session, lclBkgForm.getCalcHeavy(), lclBkgForm.getDeliveryMetro(),
                lclBkgForm.getPcBoth(), null, null, request, lclBkgForm.getLclBooking().getBillToParty());
    }

    public ActionForward calculateCAFCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileIds = request.getParameter("fileNumberId");
            Long fileId = Long.parseLong(fileIds);
            User loginUser = getCurrentUser(request);
            PortsDAO portsDAO = new PortsDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LCLBookingDAO lCLBookingDAO = new LCLBookingDAO();
            LclBooking lclBooking = new LCLBookingDAO().findById(fileId);
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String engmet = new String();
            String rateType = "R".equalsIgnoreCase(lclBooking.getRateType()) ? "Y" : lclBooking.getRateType();
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
            Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
            if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                destinationpod = portspod.getEciportcode();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }
            String pcBoth = request.getParameter("pcBoth");
            String billTo = request.getParameter("billToParty");
            if (!"B".equalsIgnoreCase(pcBoth)) {
                lclCostChargeDAO.updateBillToPartyForCharges(lclBooking.getFileNumberId(), billTo, loginUser.getUserId());
            }
            if (pcBoth.equals("C") && lclCommodityList != null && lclCommodityList.size() > 0) {
                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                lclBooking.setInsurance(true);
                lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd,
                        destinationpod, lclCommodityList, pcBoth, loginUser, fileId, request, ports, billTo);
            } else if (pcBoth.equals("P")) {
                LclBookingAc lclBookingAc = lclCostChargeDAO.findByChargeCode(fileId, false, "LCLE", "CAF");
                if (lclBookingAc != null) {
                    lclBooking.setInsurance(false);
                    String desc = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclBookingAc.getArAmount();
                    lclCostChargeDAO.delete(lclBookingAc);
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, desc, loginUser.getUserId());
                }
            }
            lclBooking.setBillingType(pcBoth);
            lclBooking.setBillToParty(billTo);
            lclBooking.setModifiedBy(loginUser);
            lclBooking.setModifiedDatetime(new Date());
            lCLBookingDAO.saveOrUpdate(lclBooking);
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(fileId, LclCommonConstant.LCL_EXPORT);
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, fileId, lclCostChargeDAO,
                    lclCommodityList, pcBoth, engmet, "No");
            request.setAttribute("lclBooking", lclBooking);
        } catch (Exception e) {
            log.info("Error in LCL calculateCAFCharge method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    private LclFileNumber createLclFileNumber(String shortShip, LclFileNumberDAO lclFileNumberDAO,
            LCLBookingForm bookingForm, User loginUser, Date now) throws Exception {
        if (CommonUtils.isNotEmpty(shortShip)) {
            LclFileNumber lclFileNumber = lclFileNumberDAO.getLclFileNumberShortShipment(shortShip, 0);
            bookingForm.setShortShipFileId(lclFileNumber.getId());
            Integer shortShipValues = lclFileNumberDAO.getMaxShortShipSequence(lclFileNumber.getFileNumber());
            new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), REMARKS_DR_AUTO_NOTES,
                    "Short Shipment Created : DR# " + lclFileNumber.getFileNumber(), loginUser.getUserId());
            //String prefix = "ZZ" + (lclFileNumber.getShortShipSequence() + 1);
            LclFileNumber shotShipFile = new LclFileNumber();
            shotShipFile.setFileNumber(lclFileNumber.getFileNumber());
            shotShipFile.setState("B");
            shotShipFile.setStatus("W");
            shotShipFile.setShortShip(true);
            shotShipFile.setCreatedDatetime(new Date());
            shotShipFile.setShortShipSequence(shortShipValues + 1);
            lclFileNumberDAO.save(shotShipFile);
            lclFileNumberDAO.update(lclFileNumber);
            new LclRemarksDAO().insertLclRemarks(shotShipFile.getId(), REMARKS_DR_AUTO_NOTES,
                    "Booking Created - Short Shipment of DR#" + lclFileNumber.getFileNumber(), loginUser.getUserId());
            return new LclFileNumber(shotShipFile.getId());
        } else {
            LclFileNumberThread thread = new LclFileNumberThread();
            LclFileNumber lclFileNumber = null;
            String fileNumber = thread.getFileNumber();
            System.out.println(fileNumber);
            Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
            if (fileNumberId == 0) {
                lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
            } else {
                thread = new LclFileNumberThread();
                fileNumber = thread.getFileNumber();
                lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
            }
            this.bookingStatus(lclFileNumber, bookingForm, loginUser, now);
            return lclFileNumber;
        }
    }

    public ActionForward inlandVoyageInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBkgForm = (LCLBookingForm) form;
        if (CommonUtils.isNotEmpty(lclBkgForm.getFileNumberId())) {
            List<LclInlandVoyageInfoBean> inlandInfoList = new LCLBookingDAO().getInlandVoyageInfo(lclBkgForm.getFileNumberId());
            request.setAttribute("inlandVoyInfo", inlandInfoList);
        }
        return mapping.findForward("displayInlandVoyage");
    }

    public ActionForward deleteManualChargeFromBkg(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileNumberId");
        LCLBookingForm lCLBookingForm = (LCLBookingForm) form;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        String lclBookingAcId = lclCostChargeDAO.getBkgAcIdsWithoutApStatus(Long.parseLong(fileId));
        String fileNumber = new LclFileNumberDAO().getFileNumberByFileId(fileId);
        String lclBookingAcTransIds = new LCLBookingAcTransDAO().getConcatenatedBookingAcTransIds(lclBookingAcId);
        new LclManifestDAO().deleteLclAccrualsByIds(lclBookingAcId, fileNumber);
        lclCostChargeDAO.deleteChargesById(lclBookingAcId);
        lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(fileId), lCLBookingForm.getModuleName());
        lclUtils.setWeighMeasureForImportBooking(request, lclBooking.getLclFileNumber().getLclBookingPieceList(), null);
        lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclBooking.getFileNumberId(), new LclCostChargeDAO(),
                lclBooking.getLclFileNumber().getLclBookingPieceList(), lclBooking.getBillingType(), "", lclBooking.getSupAcct().getAccountno());
        request.setAttribute("chargeList", chargeList);
        request.setAttribute("lclBooking", lclBooking);
        return mapping.findForward("chargeDesc");
    }
    // Set pol and pod values .this method is used to savebooking ,editBooking and CopyDR

    public void setPolPodValues(LclBooking lclBooking, String moduleName, HttpServletRequest request) throws Exception {
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        if (lclBooking.getPortOfLoading() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationName()) && null != lclBooking.getPortOfLoading().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfLoading().getUnLocationName() + "/" + lclBooking.getPortOfLoading().getStateId().getCode() + "(" + lclBooking.getPortOfLoading().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationName()) && null != lclBooking.getPortOfLoading().getCountryId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfLoading().getUnLocationName() + "/" + lclBooking.getPortOfLoading().getCountryId().getCodedesc() + "(" + lclBooking.getPortOfLoading().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationName()) && CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfLoading().getUnLocationName() + "(" + lclBooking.getPortOfLoading().getUnLocationCode() + ")");
            }
            request.setAttribute("pol", builder.toString());
            request.setAttribute("polUnlocationcode", lclBooking.getPortOfLoading().getUnLocationCode());
            request.setAttribute("portOfLoadingId", lclBooking.getPortOfLoading().getId());
            //agent
            if ("Imports".equalsIgnoreCase(moduleName)) {
                agentCount(lclBooking.getPortOfLoading().getUnLocationCode(), request);
            }
            request.setAttribute("polCode", lclBooking.getPortOfLoading().getUnLocationCode());
        }
        if (lclBooking.getPortOfDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationName()) && null != lclBooking.getPortOfDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfDestination().getUnLocationName() + "/" + lclBooking.getPortOfDestination().getStateId().getCode() + "(" + lclBooking.getPortOfDestination().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationName()) && null != lclBooking.getPortOfDestination().getCountryId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfDestination().getUnLocationName() + "/" + lclBooking.getPortOfDestination().getCountryId().getCodedesc() + "(" + lclBooking.getPortOfDestination().getUnLocationCode() + ")");
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfDestination().getUnLocationName() + "(" + lclBooking.getPortOfDestination().getUnLocationCode() + ")");
            }
            request.setAttribute("lclBookingPadList", lclBookingPadDAO.getlclBookingPadList(lclBooking));
            request.setAttribute("pod", builder.toString());
            request.setAttribute("podUnlocationcode", lclBooking.getPortOfDestination().getUnLocationCode());
            request.setAttribute("podCode", lclBooking.getPortOfDestination().getUnLocationCode());
            request.setAttribute("portOfDestinationId", lclBooking.getPortOfDestination().getId());
        }
    }
//set POA and CreditStatus for all vendors.This method is used to savebooking and editBooking

    public void setPoaandCreditStatusValues(LclBooking lclBooking, String moduleName, LCLBookingForm lclBookingForm, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        NotesDAO notesDAO = new NotesDAO();
        if (lclBooking.getLclFileNumber() != null) {//check Ar invoice
            //  request.setAttribute("arInvoiceFlag", arRedInvoiceDAO.isArInvoiceByFileNo(lclBooking.getLclFileNumber().getFileNumber()));
            //checking status of Red Invoice for  LCL Exports & LCL Imports
            String[] remarkTypes = new String[]{REMARK_TYPE_MANUAL, REMARKS_DR_MANUAL_NOTES};
            Boolean manualFlag = lclRemarksDAO.isRemarks(lclBooking.getLclFileNumber().getId(), remarkTypes);
            String arInvoiceFlag = "Empty";
            String Ar_Red_Invoice_ScreenName = "LCLI DR";
            if (!LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                Ar_Red_Invoice_ScreenName = "LCLE DR";
                Boolean hotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(lclBooking.getLclFileNumber().getId(), "XXX");
                manualFlag = !manualFlag ? hotCodeFlag : manualFlag;
                if (hotCodeFlag) {
                    request.setAttribute("isHotCodeRemarks", lclRemarksDAO.isRemarks(lclBooking.getLclFileNumber().getId(),
                            "ADDED HOT CODE XXX COMMENTS"));
                }
            }
            String arCount[] = arRedInvoiceDAO.isLclARInvoice(lclBooking.getLclFileNumber().getId().toString(), Ar_Red_Invoice_ScreenName);
            if (!arCount[0].equalsIgnoreCase("0") || !arCount[1].equalsIgnoreCase("0")) {
                if (Integer.parseInt(arCount[0]) > Integer.parseInt(arCount[1])) {
                    arInvoiceFlag = "Posted";
                } else {
                    arInvoiceFlag = "Open";
                }
            }
            request.setAttribute("arInvoiceFlag", arInvoiceFlag);
            request.setAttribute("manualNotesFlag", manualFlag);
            request.setAttribute("outsourceStyle", lclRemarksDAO.isRemarks(lclBooking.getLclFileNumber().getId(), REMARKS_STATUS_OUTSOURCE));
        }
        LclDwr lclDwr = new LclDwr();
        if (!LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            if (lclBooking.getClientAcct() != null && lclBooking.getClientAcct().getAccountno() != null && !lclBooking.getClientAcct().getAccountno().equals("")) {
                lclBookingForm.setClientIcon(notesDAO.isCustomerNotes(lclBooking.getClientAcct().getAccountno()));
                request.setAttribute("CreditForClient", genericCodeDAO.getCreditStatus(lclBooking.getClientAcct().getAccountno()));
            }
            if (lclBooking.getFwdAcct() != null && lclBooking.getFwdAcct().getAccountno() != null && !lclBooking.getFwdAcct().getAccountno().equals("")) {
                lclBookingForm.setForwaderIcon(notesDAO.isCustomerNotes(lclBooking.getFwdAcct().getAccountno()));
                request.setAttribute("CreditForForwarder", genericCodeDAO.getCreditStatus(lclBooking.getFwdAcct().getAccountno()));
            }
        }
        if (LCL_IMPORT.equalsIgnoreCase(moduleName) && lclBooking.getLclFileNumber() != null && CommonUtils.isNotEmpty(lclBookingForm.getUnitId())) {
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            request.setAttribute("correctionStyle", lclCorrectionDAO.isCorrection(lclBooking.getLclFileNumber().getId()));
            if (CommonUtils.isNotEmpty(lclBookingForm.getHeaderId())) {
                LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId()));
                LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclssheader.getId(), "V");
                if (null != lclSsDetail) {
                    lclBookingForm.setDispositionToolTip(lclBookingUtils.getImpUnitDispostionDesc(Long.parseLong(lclBookingForm.getUnitId()), lclSsDetail.getId()));
                }
            }
        }
        if (lclBooking.getShipAcct() != null && lclBooking.getShipAcct().getAccountno() != null && !lclBooking.getShipAcct().getAccountno().equals("")) {
            lclBookingForm.setShipperToolTip(lclDwr.getContactDetails("Shipper", lclBooking.getShipAcct().getAccountno(), lclBooking.getShipAcct().getAccountName(), ""));
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                request.setAttribute("CreditForShipper", genericCodeDAO.getCreditStatusForImports(lclBooking.getShipAcct().getAccountno()));
                request.setAttribute("ScanSopForShipper", genericCodeDAO.getScanSopForImports(lclBooking.getShipAcct().getAccountno()));
                request.setAttribute("ImportCreditForShipper", genericCodeDAO.getImportCredit(lclBooking.getShipAcct().getAccountno()).equalsIgnoreCase("Y") ? "$" : "");
                lclBookingForm.setShipperIcon(notesDAO.isCustomerNotesForImports(lclBooking.getShipAcct().getAccountno()));
            } else {
                lclBookingForm.setShipperIcon(notesDAO.isCustomerNotes(lclBooking.getShipAcct().getAccountno()));
                request.setAttribute("CreditForShipper", genericCodeDAO.getCreditStatus(lclBooking.getShipAcct().getAccountno()));
            }
        } else {
            lclBookingForm.setShipperToolTip(lclDwr.getContactDetails("Shipper", "", "", ""));
        }
        if (lclBooking.getConsAcct() != null && lclBooking.getConsAcct().getAccountno() != null && !lclBooking.getConsAcct().getAccountno().equals("")) {
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                request.setAttribute("CreditForConsignee", genericCodeDAO.getCreditStatusForImports(lclBooking.getConsAcct().getAccountno()));
                request.setAttribute("ScanSopForConsignee", genericCodeDAO.getScanSopForImports(lclBooking.getConsAcct().getAccountno()));
                request.setAttribute("ImportCreditForConsignee", genericCodeDAO.getImportCredit(lclBooking.getConsAcct().getAccountno()).equalsIgnoreCase("Y") ? "$" : "");
                lclBookingForm.setConsigneeIcon(notesDAO.isCustomerNotesForImports(lclBooking.getConsAcct().getAccountno()));
            } else {
                lclBookingForm.setConsigneeIcon(notesDAO.isCustomerNotes(lclBooking.getConsAcct().getAccountno()));
                request.setAttribute("CreditForConsignee", genericCodeDAO.getCreditStatus(lclBooking.getConsAcct().getAccountno()));
            }
            lclBookingForm.setConsigneeToolTip(lclDwr.getContactDetails("Consignee", lclBooking.getConsAcct().getAccountno(), lclBooking.getConsContact().getCompanyName(), ""));
        } else {
            lclBookingForm.setConsigneeToolTip(lclDwr.getContactDetails("Consignee", "", "", ""));
        }
        if (lclBooking.getNotyAcct() != null && lclBooking.getNotyAcct().getAccountno() != null && !lclBooking.getNotyAcct().getAccountno().equals("")) {
            lclBookingForm.setNotifyToolTip(lclDwr.getContactDetails("Notify", lclBooking.getNotyAcct().getAccountno(), lclBooking.getNotyContact().getCompanyName(), ""));
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                lclBookingForm.setNotifyIcon(notesDAO.isCustomerNotesForImports(lclBooking.getNotyAcct().getAccountno()));
                request.setAttribute("CreditForNotify", genericCodeDAO.getCreditStatusForImports(lclBooking.getNotyAcct().getAccountno()));
                request.setAttribute("ScanSopForNotify", genericCodeDAO.getScanSopForImports(lclBooking.getNotyAcct().getAccountno()));
                request.setAttribute("ImportCreditForNotify", genericCodeDAO.getImportCredit(lclBooking.getNotyAcct().getAccountno()).equalsIgnoreCase("Y") ? "$" : "");
            } else {
                lclBookingForm.setNotifyIcon(notesDAO.isCustomerNotes(lclBooking.getNotyAcct().getAccountno()));
                request.setAttribute("CreditForNotify", genericCodeDAO.getCreditStatus(lclBooking.getNotyAcct().getAccountno()));
            }
        } else {
            lclBookingForm.setNotifyToolTip(lclDwr.getContactDetails("Notify", "", "", ""));
        }
        if (lclBooking.getNotify2Contact() != null && lclBooking.getNotify2Contact().getTradingPartner() != null && lclBooking.getNotify2Contact().getTradingPartner().getAccountno() != null) {
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                lclBookingForm.setNotify2ToolTip(lclDwr.getContactDetails("Notify2", lclBooking.getNotify2Contact().getTradingPartner().getAccountno(), lclBooking.getNotify2Contact().getTradingPartner().getAccountName(), ""));
                lclBookingForm.setNotify2Icon(notesDAO.isCustomerNotesForImports(lclBooking.getNotify2Contact().getTradingPartner().getAccountno()));
                request.setAttribute("creditForNotify2", genericCodeDAO.getCreditStatusForImports(lclBooking.getNotify2Contact().getTradingPartner().getAccountno()));
                request.setAttribute("ScanSopForNotify2", genericCodeDAO.getScanSopForImports(lclBooking.getNotify2Contact().getTradingPartner().getAccountno()));
                request.setAttribute("ImportCreditForNotify2", genericCodeDAO.getImportCredit(lclBooking.getNotify2Contact().getTradingPartner().getAccountno()).equalsIgnoreCase("Y") ? "$" : "");
            } else {
                lclBookingForm.setNotify2Icon(notesDAO.isCustomerNotes(lclBooking.getNotify2Contact().getTradingPartner().getAccountno()));
                request.setAttribute("creditForNotify2", genericCodeDAO.getCreditStatus(lclBooking.getNotify2Contact().getTradingPartner().getAccountno()));
            }
        } else {
            lclBookingForm.setNotify2ToolTip(lclDwr.getContactDetails("Notify2", "", "", ""));
        }
        if (LCL_EXPORT.equalsIgnoreCase(moduleName) && lclBooking.getLclFileNumber() != null) {
            LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
            lclBookingForm.setDispositionToolTip(lclBookingUtils.getExpDispoDesc(lclBooking.getLclFileNumber().getId(),
                    lclBooking.getLclFileNumber().getStatus(), lclBooking.getBookingType()));
            lclBookingForm.setDisposition(lclBookingDispoDAO.getDispositionCode(lclBooking.getLclFileNumber().getId()));
            lclBookingForm.setDescription(lclBookingDispoDAO.getDescription(lclBookingForm.getDisposition()));
            request.setAttribute("currentLocationName", lclBookingDispoDAO.currenLocationUnLocName(lclBooking.getLclFileNumber().getId()));
        }
    }

    public void agentCount(String unlocationCode, HttpServletRequest request) throws Exception {
        Integer agentCount = new PortsDAO().getAgentCount(unlocationCode, LclCommonConstant.LCL_IMPORT_TYPE);
        if (agentCount > 1) {
            request.setAttribute("agentFlagCountValue", agentCount);
            request.setAttribute("agentFlag", true);
        }
    }

    // CFS Warehouse Details
    private void setCfsWarehouse(Warehouse warehouse, LCLBookingForm bkgForm) throws Exception {
        if (CommonUtils.isNotEmpty(warehouse.getAddress())) {
            bkgForm.setCfsWarehouseAddress(warehouse.getAddress());
        }
        if (CommonUtils.isNotEmpty(warehouse.getCity())) {
            bkgForm.setCfsWarehouseCity(warehouse.getCity());
        }
        if (CommonUtils.isNotEmpty(warehouse.getState())) {
            bkgForm.setCfsWarehouseState(warehouse.getState());
        }
        if (CommonUtils.isNotEmpty(warehouse.getZipCode())) {
            bkgForm.setCfsWarehouseZip(warehouse.getZipCode());
        }
        if (CommonUtils.isNotEmpty(warehouse.getPhone())) {
            bkgForm.setCfsWarehousePhone(warehouse.getPhone());
        }
        if (CommonUtils.isNotEmpty(warehouse.getFax())) {
            bkgForm.setCfsWarehouseFax(warehouse.getFax());
        }
    }

    public void setUnitWarehouse(Warehouse warehouse, LCLBookingForm bkgForm) throws Exception {
        if (CommonUtils.isNotEmpty(warehouse.getVendorNo())) {
            String coName = new CustAddressDAO().getCoName(warehouse.getVendorNo());
            bkgForm.setUnitWarehouseCoName(coName);
        }
        if (CommonUtils.isNotEmpty(warehouse.getAddress())) {
            bkgForm.setUnitWarehouseAddress(warehouse.getAddress());
        }
        if (CommonUtils.isNotEmpty(warehouse.getCity())) {
            bkgForm.setUnitWarehouseCity(warehouse.getCity());
        }
        if (CommonUtils.isNotEmpty(warehouse.getState())) {
            bkgForm.setUnitWarehouseState(warehouse.getState());
        }
        if (CommonUtils.isNotEmpty(warehouse.getZipCode())) {
            bkgForm.setUnitWarehouseZip(warehouse.getZipCode());
        }
        if (CommonUtils.isNotEmpty(warehouse.getPhone())) {
            bkgForm.setUnitWarehousePhone(warehouse.getPhone());
        }
        if (CommonUtils.isNotEmpty(warehouse.getFax())) {
            bkgForm.setUnitWarehouseFax(warehouse.getFax());
        }
    }

    public void setColoadDevanning(TradingPartner tp, LCLBookingForm bkgForm) throws Exception {
        if (CommonFunctions.isNotNull(tp.getCustAddr().getAddress1())) {
            bkgForm.setCfsWarehouseAddress(tp.getCustAddr().getAddress1());
        }
        if (CommonFunctions.isNotNull(tp.getCustAddr().getCity2())) {
            bkgForm.setCfsWarehouseCity(tp.getCustAddr().getCity2());
        }
        if (CommonFunctions.isNotNull(tp.getCustAddr().getState())) {
            bkgForm.setCfsWarehouseState(tp.getCustAddr().getState());
        }
        if (CommonFunctions.isNotNull(tp.getCustAddr().getZip())) {
            bkgForm.setCfsWarehouseZip(tp.getCustAddr().getZip());
        }
        if (CommonFunctions.isNotNull(tp.getCustAddr().getPhone())) {
            bkgForm.setCfsWarehousePhone(tp.getCustAddr().getPhone());
        }
        if (CommonFunctions.isNotNull(tp.getCustAddr().getFax())) {
            bkgForm.setCfsWarehouseFax(tp.getCustAddr().getFax());
        }
    }

    public ActionForward closeInbond(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        String inbondNumber = "";
        List<LclInbond> lclInbondList = new LclInbondsDAO().findByProperty("lclFileNumber.id", new Long(fileNumberId));
        if (!lclInbondList.isEmpty()) {
            for (LclInbond li : lclInbondList) {
                String inbondDatetime = "";
                String inbondPort = "";
                if (li.getInbondDatetime() != null) {
                    inbondDatetime = DateUtils.formatStringDateToAppFormatMMM(li.getInbondDatetime());
                }
                if (li.getInbondPort() != null && li.getInbondPort().getUnLocationName() != null) {
                    inbondPort = li.getInbondPort().getUnLocationName();
                }
                inbondNumber += li.getInbondType() + " " + li.getInbondNo() + " " + inbondPort + " " + inbondDatetime + "<br/>";
            }
            request.setAttribute("inbondNumber", inbondNumber.toUpperCase());
            request.setAttribute("inbondListSize", lclInbondList.size());
        }
        return mapping.findForward("inbondIcon");
    }

    public ActionForward calculateImpPodFDRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            User user = getCurrentUser(request);
            LCLBookingForm lclBookingForm = (LCLBookingForm) form;
            if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBookingForm.getBookingType()) && "N".equalsIgnoreCase(lclBookingForm.getTransShipMent())) {
                new LclBookingExportDAO().delete(lclBookingForm.getFileNumberId());
            }
            lclBookingForm.setEnums(request, "Imports", lclBookingForm.getTransShipMent());
            LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            List<LclBookingPiece> lclBookingPiecesList = null;
            Long fileNumberId = new Long(lclBookingForm.getFileNumberId());
            String originName = "";
            String destinationName = "";
            String polUnCode = "";
            String podUnCode = "";
            String fdUnCode = "";
            String agentNo = "";
            String originUnCode = "";
            String transhipment = "N";
            fdUnCode = lclBookingForm.getUnlocationCode();
            StringBuilder remarks = new StringBuilder();
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            if (CommonFunctions.isNotNull(lclBooking)) {
                destinationName = lclBooking.getFinalDestination().getUnLocationName();

                if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                    originName = lclBooking.getPortOfLoading().getUnLocationName();
                    polUnCode = lclBooking.getPortOfLoading().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclBooking.getPortOfDestination().getUnLocationName())) {
                    podUnCode = lclBooking.getPortOfDestination().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                    agentNo = lclBooking.getAgentAcct().getAccountno();
                }
                if (null != lclBooking.getPortOfOrigin() && null != lclBooking.getPortOfOrigin().getUnLocationCode()) {
                    originUnCode = lclBooking.getPortOfOrigin().getUnLocationCode();
                }
            }
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
            if (CommonUtils.isNotEmpty(lclBookingForm.getHeaderId())) {
                LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId()));
                request.setAttribute("lclssheader", lclssheader);
            }
            if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                Boolean tranship = lclBooking.getLclFileNumber().getLclBookingImport().getTransShipment();
                if (tranship) {
                    transhipment = "Y";
                }
                LclManifestDAO lclManifestDAO = new LclManifestDAO();
                LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
                String bookingAcIds = lclCostChargeDAO.getIpiAndInbBkgAcIds(fileNumberId);
                boolean flag = false;
                if (CommonUtils.isEmpty(bookingAcIds) && podUnCode != null && fdUnCode != null && CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode)) {
                    lclImportChargeCalc.calculatediffPodFdRates(polUnCode, podUnCode, fdUnCode, transhipment,
                            lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, lclBookingForm.getImpCfsWarehsId(), fileNumberId, lclBookingPiecesList, request, user, originUnCode);
                } else if (CommonUtils.isNotEmpty(bookingAcIds)) {
                    String[] bkgAcId = bookingAcIds.split(",");
                    for (String bkgAcId1 : bkgAcId) {
                        LclBookingAc ipiAndInbCharge = lclCostChargeDAO.findByIpiAndInbChargeId(bkgAcId1);
                        String costStatus = lclBookingAcTransDAO.getTransType(ipiAndInbCharge.getId());
                        if (CommonUtils.notIn(costStatus, "AP", "IP", "DS")) {
                            remarks.append("DELETED -> Code -> ").append(ipiAndInbCharge.getArglMapping().getChargeCode()).append(" Charge Amount -> ").append(ipiAndInbCharge.getArAmount());
                            new LclRemarksDAO().insertLclRemarks(fileNumberId, REMARKS_DR_AUTO_NOTES, remarks.toString(), user.getUserId());
                            String lclBookingAcTransIds = new LCLBookingAcTransDAO().getConcatenatedBookingAcTransIds(bkgAcId1);
                            lclManifestDAO.deleteLclAccruals(Integer.parseInt(bkgAcId1), ipiAndInbCharge.getLclFileNumber().getFileNumber());
                            lclCostChargeDAO.delete(ipiAndInbCharge);
                            lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
                            flag = true;
                        }
                    }
                    if (flag && podUnCode != null && fdUnCode != null && CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode)) {
                        lclImportChargeCalc.calculatediffPodFdRates(polUnCode, podUnCode, fdUnCode, transhipment,
                                lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, lclBookingForm.getImpCfsWarehsId(), fileNumberId, lclBookingPiecesList, request, user, originUnCode);
                    }
                }
                //3rd leg for computing rates, using POD-POD setup
                if (lclBooking.getPortOfDestination() != null && podUnCode.equalsIgnoreCase(fdUnCode)) {
                    PortsDAO portsDAO = new PortsDAO();
                    String pooSchnum = "00000";
                    String polSchnum = portsDAO.getShedulenumber(polUnCode);
                    String podSchnum = portsDAO.getShedulenumber(podUnCode);
                    String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
                    if (null != lclBooking.getPortOfOrigin()) {
                        pooSchnum = portsDAO.getShedulenumber(lclBooking.getPortOfOrigin().getUnLocationCode());
                    }
                    lclImportChargeCalc.calculate3LegSetUp(lclBooking.getBillingType(), lclBooking.getBillToParty(),
                            fileNumberId, lclBookingPiecesList, user, pooSchnum, polSchnum, podSchnum, fdSchnum, request);
                }
                List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_IMPORT);
                lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargeDAO, lclBookingPiecesList,
                        lclBooking.getBillingType(), "", agentNo);
            }
            if (lclBooking.getPortOfLoading() != null && lclBooking.getFinalDestination() != null) {
                String pooUnCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
                request.setAttribute("totalStorageAmt", lclImportChargeCalc.calculateImpAutoRates(lclBooking.getPortOfLoading().getUnLocationCode(),
                        lclBooking.getPortOfDestination().getUnLocationCode(), pooUnCode, "", lclBookingPiecesList, transhipment));
            }
            request.setAttribute("lclBooking", lclBooking);
            request.setAttribute("destination", destinationName);
            request.setAttribute("origin", originName);
            request.setAttribute("oldFdUnlocationCode", fdUnCode);
        } catch (Exception e) {
            log.info("Error in LCL calculateImpPodFDRates method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward updateBillToCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
        String billToParty = "", exitbillToParty = "", billingType = "", existBillingType = "";
        StringBuilder notes = new StringBuilder();
        if (lclBookingForm.getModuleName().equalsIgnoreCase("Imports")) {
            billToParty = request.getParameter("billtoCodeImports");
            exitbillToParty = request.getParameter("whsePhone");
            billingType = request.getParameter("pcBothImports");
            existBillingType = request.getParameter("existBillingType");
            String transhipment = request.getParameter("transhipment");

            if ("Y".equalsIgnoreCase(transhipment) && !billingType.equalsIgnoreCase(existBillingType)) {
                LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
                lclImportChargeCalc.ImportRateCalculation(lclBookingForm.getOriginCode(), lclBookingForm.getPolCode(), lclBookingForm.getPodCode(), lclBookingForm.getUnlocationCode(), transhipment,
                        billingType, billToParty, null, lclBookingForm.getImpCfsWarehsId(), Long.parseLong(lclBookingForm.getFileNumberId()),
                        lclCommodityList, request, loginUser, lclBookingForm.getUnitSsId());
            }
            if (existBillingType == null ? billingType != null : !existBillingType.equals(billingType)) {//checking terms
                notes.append("Terms -> ").append(existBillingType).append(" to ").append(billingType).append(" , ");//notes
            }
            lclBookingDAO.updateBillToParty(Long.parseLong(lclBookingForm.getFileNumberId()), billingType, billToParty, "", "", loginUser.getUserId()); //update bill to party and billing
            notes.append("Bill to Code -> ").append(exitbillToParty).append(" to ").append(billToParty);//notes
            lclUtils.insertLCLRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), notes.toString(), REMARKS_DR_AUTO_NOTES, loginUser);
            LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            String agentNo = "";
            if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                agentNo = lclBooking.getAgentAcct().getAccountno();
            }
            lclCostChargeDAO.updateBillToPartyChargesME(Long.parseLong(lclBookingForm.getFileNumberId()), billToParty, loginUser.getUserId(), "");
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(lclBookingForm.getFileNumberId()), lclBookingForm.getModuleName());
            lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(lclBookingForm.getFileNumberId()), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), "", agentNo);
            request.setAttribute("chargeList", chargeList);
            request.setAttribute("lclBooking", lclBooking);
        } else {
            String engmet = "";
            billToParty = request.getParameter("billToparty");
            exitbillToParty = request.getParameter("exitBillToParty");
            billingType = request.getParameter("billingType");
            lclBookingDAO.updateBillToParty(Long.parseLong(lclBookingForm.getFileNumberId()), billingType, billToParty, "", "", loginUser.getUserId());
//            notes.append("Bill to Code -> ").append(exitbillToParty).append(" to ").append(billToParty);
//            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_TYPE_AUTO, notes.toString(), loginUser.getUserId());
            LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            lclCostChargeDAO.updateBillToPartyForCharges(Long.parseLong(lclBookingForm.getFileNumberId()), billToParty, loginUser.getUserId());
            Ports ports = new PortsDAO().getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
            }
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(lclBookingForm.getFileNumberId()), lclBookingForm.getModuleName());
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, lclBooking.getFileNumberId(), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
            request.setAttribute("lclBooking", lclBooking);
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward linkVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm unitScheduleForm = new LclUnitsScheduleForm();
        String pol = request.getParameter("pol");
        String pod = request.getParameter("pod");
        if (CommonUtils.isNotEmpty(pol)) {
            unitScheduleForm.setPortOfOriginId(Integer.parseInt(pol));
        }
        if (CommonUtils.isNotEmpty(pod)) {
            unitScheduleForm.setFinalDestinationId(Integer.parseInt(pod));
        }
        unitScheduleForm.setMethodName("linkVoyage");
        List<ImpVoyageSearchBean> voyageList = new ImportVoyageSearchDAO().getImportVoyageSearch(unitScheduleForm);
        request.setAttribute("fileId", request.getParameter("fileId"));
        request.setAttribute("load", request.getParameter("load"));
        request.setAttribute("dest", request.getParameter("dest"));
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("voyages", voyageList);
        return mapping.findForward("linkVoyage");
    }

    public ActionForward convertToBL(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        LclBl lclBl = new LclBl();
        LCLBlDAO lCLBlDAO = new LCLBlDAO();
        LclDwr lclDwr = new LclDwr();
        ActionForward redirectAction = null;
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LclBLPieceDAO pieceDAO = new LclBLPieceDAO();
        LclOptionsDAO lclOptionsDAO = new LclOptionsDAO();
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
        LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
        User thisUser = (User) request.getSession().getAttribute("loginuser");
        Date now = new Date();
        String fileNoId = bookingForm.getFileNumberId();
        Long fileId = null != fileNoId ? Long.parseLong(fileNoId) : null;
        LclFileNumber lclFileNumber = null;
        if (CommonUtils.isNotEmpty(fileNoId)) {
            lclFileNumber = fileDao.findById(fileId);
            lclFileNumber.setState("BL");
            fileDao.update(lclFileNumber);
            LclBooking lclBooking = lclBookingDAO.findById(fileId);
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            Map<Long, LclBlPiece> blPieceMap = new HashMap();
            new LclRemarksDAO().insertLclRemarks(lclFileNumber.getId(), "auto", "Converted to BL", thisUser.getUserId());
            List<LclBookingPiece> lclBookingPieces = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
            List conoslidatelist = new LclConsolidateDAO().getConsolidatesFiles(fileId);
            for (LclBookingPiece lbp : lclBookingPieces) {
                LclBlPiece lblp = new LclBlPiece();
                PropertyUtils.copyProperties(lblp, lbp);
                lblp.setPackageType(null != lbp.getActualPackageType() ? lbp.getActualPackageType() : lbp.getPackageType());
                lblp.setId(null);
                if (!conoslidatelist.isEmpty()) {
                    // adding remarks for conoslidated files
                    for (Object Fileid : conoslidatelist) {
                        new LclRemarksDAO().insertLclRemarks(Long.parseLong(Fileid.toString()), "auto", "Converted to BL", thisUser.getUserId());
                    }
                    BigDecimal cft, cbm, lbs, kgs;
                    int piece;
                    if (lbp.equals(lclBookingPieces.get(0))) {
                        // update only for least commodity details of file.
                        piece = !CommonUtils.isEmpty(lbp.getActualPieceCount()) ? lbp.getActualPieceCount() : lbp.getBookedPieceCount();
                        cft = !CommonUtils.isEmpty(lbp.getActualVolumeImperial()) ? lbp.getActualVolumeImperial() : lbp.getBookedVolumeImperial();
                        cbm = !CommonUtils.isEmpty(lbp.getActualVolumeMetric()) ? lbp.getActualVolumeMetric() : lbp.getBookedVolumeMetric();
                        lbs = !CommonUtils.isEmpty(lbp.getActualWeightImperial()) ? lbp.getActualWeightImperial() : lbp.getBookedWeightImperial();
                        kgs = !CommonUtils.isEmpty(lbp.getActualWeightMetric()) ? lbp.getActualWeightMetric() : lbp.getBookedWeightMetric();

                        List<OceanManifestBean> bookingPiece = new LclBookingPieceDAO().getTotalWeightForComm(conoslidatelist);
                        conoslidatelist.add(fileId);
                        OceanManifestBean totalcommodity = bookingPiece.get(0);
                        lblp.setActualPieceCount(totalcommodity.getPiece() + piece);
                        lblp.setActualVolumeImperial(new BigDecimal(null != totalcommodity.getCft() ? totalcommodity.getCft() : 0.00).add(cft));
                        lblp.setActualVolumeMetric(new BigDecimal(null != totalcommodity.getCbm() ? totalcommodity.getCbm() : 0.00).add(cbm));
                        lblp.setActualWeightImperial(new BigDecimal(null != totalcommodity.getLbs() ? totalcommodity.getLbs() : 0.00).add(lbs));
                        lblp.setActualWeightMetric(new BigDecimal(null != totalcommodity.getKgs() ? totalcommodity.getKgs() : 0.00).add(kgs));
                    }
                } else if (lclFileNumber.getStatus().equalsIgnoreCase("B")) {
                    lblp.setActualPieceCount(lbp.getBookedPieceCount());
                    lblp.setActualVolumeImperial(lbp.getBookedVolumeImperial());
                    lblp.setActualVolumeMetric(lbp.getBookedVolumeMetric());
                    lblp.setActualWeightImperial(lbp.getBookedWeightImperial());
                    lblp.setActualWeightMetric(lbp.getBookedWeightMetric());
                }
                pieceDAO.save(lblp);
                lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId())); // for lazy initialize
                List<LclBookingPieceDetail> bookingPieceList = lbp.getLclBookingPieceDetailList();
                for (LclBookingPieceDetail bkgpiecedetail : bookingPieceList) {
                    LclBlPieceDetail blpiecedetail = new LclBlPieceDetail();
                    PropertyUtils.copyProperties(blpiecedetail, bkgpiecedetail);
                    blpiecedetail.setId(null);
                    blpiecedetail.setLclBlPiece(lblp);
                    new LclBlPieceDetailDAO().saveOrUpdate(blpiecedetail);
                }
                if (lbp.getCommodityType() != null && lbp.getCommodityType().getId() != null) {
                    blPieceMap.put(lbp.getCommodityType().getId(), lblp);
                }
            }
            if (!lclConsolidateDAO.isConsoildateFile(fileId.toString())) {
                List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getLclCostByFileNumberAsc(fileId, LclCommonConstant.LCL_EXPORT);
                for (LclBookingAc lclBookingAc : lclBookingAcList) {
                    if (lclBookingAc.getArAmount().doubleValue() != 0.00d) {
                        LclBlAc lclBlAc = new LclBlAc();
                        PropertyUtils.copyProperties(lclBlAc, lclBookingAc);
                        lclBlAc.setId(null);
                        lclBlAc.setEnteredBy(thisUser);
                        lclBlAc.setModifiedBy(thisUser);
                        lclBlAc.setEnteredDatetime(now);
                        lclBlAc.setModifiedDatetime(now);
                        lclBlAc.setApAmount(BigDecimal.ZERO);
                        if (blPieceMap != null && blPieceMap.size() > 0
                                && lclBookingAc.getLclBookingPiece() != null
                                && lclBookingAc.getLclBookingPiece().getCommodityType() != null
                                && lclBookingAc.getLclBookingPiece().getCommodityType().getId() != null) {
                            lclBlAc.setLclBlPiece(blPieceMap.get(lclBookingAc.getLclBookingPiece().getCommodityType().getId()));
                        }
                        if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                            lclBlAc.setArBillToParty(CommonUtils.in(lclBookingAc.getArBillToParty(), "W", "A") ? "S"
                                    : CommonUtils.in(lclBookingAc.getArBillToParty(), "C", "N") ? "A" : lclBookingAc.getArBillToParty());
                            lclBlAc.setApBillToParty(CommonUtils.in(lclBookingAc.getApBillToParty(), "W", "A") ? "S"
                                    : CommonUtils.in(lclBookingAc.getApBillToParty(), "C", "N") ? "A" : lclBookingAc.getApBillToParty());
                        }
                        new LclBlAcDAO().save(lclBlAc);
                    }
                }
            } else {  // adding only manual charge for consolidated DR.
                List lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", fileId);
                String rateType = lclBooking.getRateType().equalsIgnoreCase("R") ? "Y" : lclBooking.getRateType();
                lclBlChargesCalculation.calculateRates(lclBooking.getPortOfOrigin().getUnLocationCode(),
                        lclBooking.getFinalDestination().getUnLocationCode(), lclBooking.getPortOfLoading().getUnLocationCode(),
                        lclBooking.getPortOfDestination().getUnLocationCode(), fileId, lclBlPiecesList, thisUser, "",
                        lclBooking.getInsurance() ? "Y" : "N", lclBooking.getValueOfGoods(), rateType, "C", null, null,
                        null, null, null, lclBooking.getDeliveryMetro(), lclBooking.getBillingType(), null, lclBooking.getBillToParty(), request, true, false);
                new ExportBookingUtils().updateConsolidateManualCharge(conoslidatelist, fileId, lclBooking.getBillToParty(), request);
            }
            PropertyUtils.copyProperties(lclBl, lclBooking);
            String pooUnlocationCode = "";
            String podUnLocCode = "";
            UnLocation podUnlocation = null;
            UnLocation fdUnlocation = lclBooking.getFinalDestination();
            //File is Transhipment File
            if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                pooUnlocationCode = lclBooking.getPortOfDestination().getUnLocationCode();
                lclBl.setPortOfOrigin(lclBooking.getPortOfDestination());
                LclBookingImport lclBookingImport = new LclBookingImportDAO().findById(fileId);
                lclBl.setPortOfLoading(lclBookingImport.getUsaPortOfExit());
                podUnlocation = lclBookingImport.getForeignPortOfDischarge();
                podUnLocCode = lclBookingImport.getForeignPortOfDischarge().getUnLocationCode();
                lclBl.setAgentAcct(null != lclBookingImport.getExportAgentAcctNo() ? lclBookingImport.getExportAgentAcctNo() : null);
            } else {
                pooUnlocationCode = lclBooking.getPortOfOrigin().getUnLocationCode();
                podUnLocCode = lclBooking.getPortOfDestination().getUnLocationCode();
                podUnlocation = lclBooking.getPortOfDestination();
            }
            request.setAttribute("bkgPooUnloc", lclBl.getPortOfOrigin().getUnLocationCode());
            String alternatePodId = "", alternatefdId = "";
            String[] alternatePorts = new PortsDAO().getAlternatePorts(podUnLocCode);
            if (alternatePorts != null && alternatePorts.length > 0) {
                alternatePodId = CommonUtils.isNotEmpty(alternatePorts[0]) ? alternatePorts[0].toString() : "";
                podUnlocation = CommonUtils.isNotEmpty(alternatePodId)
                        ? new UnLocationDAO().findById(Integer.parseInt(alternatePodId)) : podUnlocation;
                podUnLocCode = podUnlocation.getUnLocationCode();
                alternatefdId = CommonUtils.isNotEmpty(alternatePorts[1]) ? alternatePorts[1].toString() : "";
                fdUnlocation = CommonUtils.isNotEmpty(alternatefdId)
                        ? new UnLocationDAO().findById(Integer.parseInt(alternatefdId)) : fdUnlocation;
                if (CommonUtils.isNotEmpty(alternatefdId)) {
                    UnLocation fdUnloc = new UnLocationDAO().findById(Integer.parseInt(alternatefdId));
                    if (fdUnloc.getUnLocationCode().equalsIgnoreCase("PRSJU")) {
                        lclBl.setDeliveryMetro("I");
                        List lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", fileId);
                        String rateType = lclBl.getRateType().equalsIgnoreCase("R") ? "Y" : lclBl.getRateType();
                        if (!lclConsolidateDAO.isConsoildateFile(fileId.toString())) {
                            lclBlChargesCalculation.calculateRates(lclBl.getPortOfOrigin().getUnLocationCode(),
                                    fdUnlocation.getUnLocationCode(), lclBl.getPortOfLoading().getUnLocationCode(),
                                    podUnlocation.getUnLocationCode(), fileId, lclBlPiecesList, thisUser, "",
                                    lclBl.getInsurance() ? "Y" : "N", lclBl.getValueOfGoods(), rateType, "C", null, null,
                                    null, null, null, lclBl.getDeliveryMetro(), lclBl.getBillingType(), null, lclBl.getBillToParty(), request, true, false);
                        }

                    }
                }
            }

            lclBl.setPortOfDestination(podUnlocation);
            lclBl.setFinalDestination(fdUnlocation);
            if (CommonUtils.isNotEmpty(pooUnlocationCode)) {
                String rateType = "R".equalsIgnoreCase(lclBooking.getRateType()) ? "Y" : lclBooking.getRateType();
                String ratesTerminalNo = new RefTerminalDAO().getTrmnum(pooUnlocationCode, rateType);
                lclBl.setRatesFromTerminalNo(CommonUtils.isNotEmpty(ratesTerminalNo) ? ratesTerminalNo : null);
            }
            if (null != lclBl.getAgentAcct()) {
                CustAddressDAO custaddress = new CustAddressDAO();
                Integer PodId = lclBooking.getBookingType().equals("T") ? lclBooking.getLclFileNumber().getLclBookingImport().getForeignPortOfDischarge().getId()
                        : lclBooking.getPortOfDestination().getId();
                String[] pickupAcctNo = new AgencyInfoDAO().getAgentPickAcctNo(PodId, lclBl.getAgentAcct().getAccountno());
                CustAddress custAddress = custaddress.findPrimeContact((pickupAcctNo != null && CommonUtils.isNotEmpty(pickupAcctNo[0])) ? pickupAcctNo[0] : lclBl.getAgentAcct().getAccountno());
                if (null != custAddress) {
                    LclContact agentContact = new LCLContactDAO().getContact(lclBooking.getFileNumberId(), "blAgent");
                    TradingPartner tpPickupAcctNo = new TradingPartnerDAO().findById(custAddress.getAcctNo());
                    lclBl.setAgentAcct(tpPickupAcctNo);
                    if (agentContact == null) {
                        agentContact = new LclContact();
                        agentContact.setLclFileNumber(lclFileNumber);
                    }
                    agentContact.setCompanyName(custAddress.getCoName());
                    agentContact.setAddress(custAddress.getAddress1());
                    agentContact.setCity(custAddress.getCity1());
                    agentContact.setState(custAddress.getState());
                    agentContact.setZip(custAddress.getZip());
                    agentContact.setPhone1(custAddress.getPhone());
                    agentContact.setEmail1(custAddress.getEmail1());
                    agentContact.setFax1(custAddress.getFax());
                    agentContact.setEnteredDatetime(now);
                    agentContact.setModifiedDatetime(now);
                    agentContact.setEnteredBy(thisUser);
                    agentContact.setModifiedBy(thisUser);
                    agentContact.setRemarks("blAgent");
                    lclBl.setAgentContact(agentContact);
                }
            }
            lclBl.setLclFileNumber(lclFileNumber);
            if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                lclBl.setBookingType("T");
            } else {
                lclBl.setBookingType("E");
            }
            lclBl.setRtdTransaction(false);
            LclUtils lclUtil = new LclUtils();
//            if (null != lclBooking.getBookedSsHeaderId() && null != lclBooking.getBookedSsHeaderId().getVesselSsDetail()) {
//                lclBl.getPortOfLoading().setUnLocationName(lclBooking.getBookedSsHeaderId().getVesselSsDetail().getDeparture().getUnLocationName());
//            }
            lclBl.setPooWhseContact(lclBooking.getPooWhseContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForExportBl(lclBooking.getPooWhseContact(), thisUser, lclFileNumber, "") : null);

            lclBl.setClientContact(lclBooking.getClientContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForExportBl(lclBooking.getClientContact(), thisUser, lclFileNumber, "blClient") : null);

            lclBl.setShipContact(lclBooking.getShipContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForExportBl(lclBooking.getShipContact(), thisUser, lclFileNumber, "blShipper") : null);

            lclBl.setConsContact(lclBooking.getConsContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForExportBl(lclBooking.getConsContact(), thisUser, lclFileNumber, "blConsignee") : null);

            lclBl.setSupContact(lclBooking.getSupContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForExportBl(lclBooking.getSupContact(), thisUser, lclFileNumber, "blSupplier") : null);

            lclBl.setNotyContact(lclBooking.getNotyContact().getValidLclContact().length() > 1
                    ? lclUtil.getContactForExportBl(lclBooking.getNotyContact(), thisUser, lclFileNumber, "blNotify") : null);

            if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                if (lclBooking.getSupContact().getValidLclContact().length() > 1) {
                    lclBl.setShipContact(lclUtil.getContactForExportBl(lclBooking.getSupContact(), thisUser, lclFileNumber, "blShipper"));
                    lclBl.setShipAcct(lclBl.getSupAcct());
                }
                String[] billToParty = lclBookingUtils.getFormatBillToCodeImpAndExp(fileId, lclBooking.getBillToParty(), lclBooking.getBillingType(), bookingForm.getModuleName());
                lclBl.setBillToParty(billToParty[0]);
                lclBl.setBillingType(billToParty[1]);
            } else {
                lclBl.setBillingType(bookingForm.getBillingType());
                lclBl.setBillToParty(lclBooking.getBillToParty());
            }

            if (lclBooking.getFwdContact().getValidLclContact().length() > 1) {
                lclBl.setFwdContact(lclUtil.getContactForExportBl(lclBooking.getFwdContact(), thisUser, lclFileNumber, "blForwarder"));
                if (lclBl.getFwdAcct() != null && null != lclBl.getFwdAcct().getGeneralInfo()) {
                    lclBl.setFwdFmcNo(lclBl.getFwdAcct().getGeneralInfo().getFwFmcNo());
                    lclBl.getFwdContact().setAddress(lclBl.getFwdContact().getAddress() + "\n FMC#"
                            + lclBl.getFwdAcct().getGeneralInfo().getFwFmcNo());
                }
            } else {
                lclBl.setFwdContact(null);
            }
            if (null != lclBooking.getThirdPartyContact()) {
                lclBl.setThirdPartyContact(lclBooking.getThirdPartyContact().getValidLclContact().length() > 1
                        ? lclUtil.getContactForExportBl(lclBooking.getThirdPartyContact(), thisUser, lclFileNumber, "blThirdParty") : null);
            }

            lclBl.setRtAgentContact(null);
            lclBl.setEnteredDatetime(now);
            lclBl.setModifiedDatetime(now);
            lclBl.setEnteredBy(thisUser);
            lclBl.setModifiedBy(thisUser);
            lclBl.setBlOwner(thisUser);
            //retrieving all consolidated files
            StringBuilder consolidatedFileBuilder = new StringBuilder();
            List<LclConsolidate> consDescList = lclConsolidateDAO.findByProperty("lclFileNumberA.id", fileId);
            consDescList.remove(lclConsolidateDAO.findByFileAandFileB(fileId, fileId));
            Collections.sort(consDescList, new LclConsolidateComparator());
            for (LclConsolidate lc : consDescList) {
                consolidatedFileBuilder.append(lc.getLclFileNumberB().getFileNumber());
                consolidatedFileBuilder.append(",");
            }
            request.setAttribute("consolidatedDocuments", consolidatedFileBuilder.toString());
            addSequenceNumber(lclBl, fileId);
            lCLBlDAO.save(lclBl);

            String destinationCode = null != lclBooking.getFinalDestination() ? lclBooking.getFinalDestination().getUnLocationCode()
                    : null != lclBooking.getPortOfDestination() ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
            //lclBlAcDAO.updateBillToParty(lclFileNumber.getId(), fieldName, bookingForm.getAgentNumber(), thisUser.getUserId());
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "AES");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "HS");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "NCM");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "FP");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "", "FPFEILD");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "IMP");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "ALERT");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "PROOF");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "PORT");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "", "PORTFIELD");
            Boolean printMetric = new LCLPortConfigurationDAO().isPrintImpOnMetric(destinationCode);
            if (null != printMetric && printMetric) {
                lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "Y", "MET");
            } else {
                lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "MET");
            }
            //lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "MET");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "RECEIVE");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "MINI");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "Y", "PIER");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "Y", "ARRIVALDATE");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "HBLPIER");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "HBLPOL");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "DELIVERY");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "LADENSAILDATE");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "ROUTING", "PRINTTERMSTYPE");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "CORRECTEDBL");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "PRINTHAZBEFORE");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "N", "PRINTPPDBLBOTH");
            lclOptionsDAO.insertLclOptions(fileId, thisUser.getUserId(), "Y", "INSURANCE");
            /* populating the values from Edi data into lclbl page */
            StringBuilder remarks = new StringBuilder();
            ArrayList<String> lclEdiData = lclDwr.getLclEdiAndKnData(lclFileNumber.getId(), lclFileNumber.getFileNumber(), request);
            if (CommonUtils.isNotEmpty(bookingForm.getEdiButton()) && "Yes".equalsIgnoreCase(bookingForm.getEdiButton())) {
                if (CommonUtils.isNotEmpty(lclEdiData.get(1)) || CommonUtils.isNotEmpty(lclEdiData.get(2))) {
                    new LCLBlDAO().updateEdiDataToBl(lclEdiData.get(2), lclEdiData.get(1), fileNoId);
                }
                if (CommonUtils.isNotEmpty(lclEdiData.get(0))) {
                    remarks.append(lclEdiData.get(0)).append("\n");
                }
            }
            if (null != lclBooking.getConsAcct()) {
                if (CommonUtils.isNotEmpty(lclBooking.getConsAcct().getTaxExempt())) {
                    remarks.append("CommReg # ").append(lclBooking.getConsAcct().getTaxExempt()).append("    ");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getConsAcct().getFederalId())) {
                    remarks.append("FedID  ").append(lclBooking.getConsAcct().getFederalId());
                }
            }
            new LclRemarksDAO().updateRemarksByField(lclFileNumber, REMARKS_TYPE_EXPORT_REF, REMARKS_TYPE_EXPORT_REF,
                    remarks.toString(), thisUser, REMARKS_BL_AUTO_NOTES);
//            String[] lclEdiData = null != bookingForm.getEdiData() ? bookingForm.getEdiData().split(",") : null;
//            if (null != lclEdiData && lclEdiData.length > 0) {
//                lclDwr.convertToBlEdi(fileNoId, lclEdiData, request);
//            }
            /* update state of consolidated files */
            fileDao.updateConsolidateBl(fileId, "BL");
        }
        return redirectAction;
    }

    public ActionForward showArTransactions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String blNumber = request.getParameter("blNumber");
        List<ResultModel> postedTransactions = new ArTransactionHistoryDAO().getPostedTransactions(blNumber, lclBookingForm.getFileNumber(), LCL_SHIPMENT_TYPE_IMPORT);
        request.setAttribute("postedTransactions", postedTransactions);
        return mapping.findForward("showArTransactionDetails");
    }

    public void setSportRateValues(LclBooking lclBooking, String engmet, HttpServletRequest request) throws Exception {
        request.setAttribute("engmet", engmet);
        request.setAttribute("ofspotrate", lclBooking.getSpotRate());
        if (engmet != null && engmet.equalsIgnoreCase("E")) {
            request.setAttribute("spotratelabel", (lclBooking.getSpotWmRate() == null ? 0 : lclBooking.getSpotWmRate()) + "/CFT" + "," + (lclBooking.getSpotRateMeasure() == null ? 0 : lclBooking.getSpotRateMeasure()) + "/100 LBS");
        } else {
            request.setAttribute("spotratelabel", (lclBooking.getSpotWmRate() == null ? 0 : lclBooking.getSpotWmRate()) + "/CBM" + "," + (lclBooking.getSpotRateMeasure() == null ? 0 : lclBooking.getSpotRateMeasure()) + "/1000 KGS");
        }
    }

    public void setAesDetailsList(String fileNumber, HttpServletRequest request) throws Exception {
        if (fileNumber != null && !fileNumber.equals("")) {
            List<SedFilings> aesList = new ArrayList<SedFilings>();
            List sedlist = new SedFilingsDAO().getSedFilingsList(fileNumber);
            if (null != sedlist) {
                for (Object object : sedlist) {
                    SedFilings sedFilings = (SedFilings) object;
                    sedFilings.setItnStatus(new LogFileEdiDAO().getITNStatus(sedFilings.getShpdr()));
                    if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref()))) {
                        sedFilings.setSched(true);
                    }
                    aesList.add(sedFilings);
                }
            }
            request.setAttribute("aesDetailsList", aesList);
        }
    }

    private LclBookingImportAms updateAms(Long fileId, User thisUser, LCLBookingForm bkgForm) throws Exception {
        Date today = new Date();
        LclBookingImportAms bkgAms = null;
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        LclBookingImportAmsDAO bkgAmsDao = new LclBookingImportAmsDAO();
        StringBuilder remarks = new StringBuilder();
        LclUtils utils = new LclUtils();
        bkgAms = bkgAmsDao.getBkgAms(fileId);
        boolean isAmsEqual = (null != bkgAms && null != bkgAms.getScac() && null != bkgAms.getAmsNo())
                ? bkgAms.getAmsNo().equalsIgnoreCase(bkgForm.getDefaultAms().toUpperCase())
                && bkgAms.getScac().equalsIgnoreCase(bkgForm.getScac())
                && bkgAms.getPieces() == bkgForm.getDefaultPieces() : false;
        if (null == bkgAms) {
            bkgAms = new LclBookingImportAms();
            bkgAms.setEnteredByUserId(thisUser);
            bkgAms.setEnteredDatetime(today);
            remarks.append("Created - Scac : ").append(bkgForm.getScac().toUpperCase()).append(" and AMS/HBL # ").append(bkgForm.getDefaultAms().toUpperCase()).append(" with pieces count : ").append(bkgForm.getDefaultPieces());
            utils.insertLCLRemarks(fileId, remarks.toString(), REMARKS_DR_AUTO_NOTES, thisUser);
        } else if (!isAmsEqual) {
            bkgAms = bkgAmsDao.getFirstBkgAms(fileId);
            if (bkgAms.getScac() != null) {
                remarks.append("Updated - Scac : ").append(bkgAms.getScac().toUpperCase());
            } else {
                remarks.append("Updated - Scac : ").append(" ");
            }
            if (bkgAms.getAmsNo() != null) {
                remarks.append(" and AMS/HBL # ").append(bkgAms.getAmsNo().toUpperCase());
            } else {
                remarks.append(" and AMS/HBL # ").append(" ");
            }
            remarks.append(" with Pieces : ").append(bkgAms.getPieces()).append(" to Scac : ").append(bkgForm.getScac().toUpperCase()).append(" and AMS/HBL # ").append(bkgForm.getDefaultAms().toUpperCase());
            remarks.append(" with Pieces : ").append(bkgForm.getDefaultPieces());
            utils.insertLCLRemarks(fileId, remarks.toString(), REMARKS_DR_AUTO_NOTES, thisUser);
        }
        if (CommonUtils.isNotEmpty(bkgForm.getDefaultAms())) {
            bkgAms.setAmsNo(bkgForm.getDefaultAms());
        }
        if (CommonUtils.isNotEmpty(bkgForm.getScac())) {
            bkgAms.setScac(bkgForm.getScac());
        }
        if (null != bkgForm.getDefaultPieces()) {
            bkgAms.setPieces(bkgForm.getDefaultPieces());
        }
        bkgAms.setLclFileNumber(fileDao.findById(fileId));
        bkgAms.setModifiedByUserId(thisUser);
        bkgAms.setModifiedDatetime(today);
        bkgAmsDao.saveOrUpdate(bkgAms);
        return bkgAms;
    }

    public ActionForward setColoadCommRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardPage = "";
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String fileId = request.getParameter("fileNumberId");
        String polUnCode = request.getParameter("polUnCode");
        String podUnCode = request.getParameter("podUnCode");
        String coloadComm = request.getParameter("coloadComm");
        String billingType = request.getParameter("billingType");
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        if ("commodity".equalsIgnoreCase(request.getParameter("ratesFlag"))) {
            List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (lclBookingPieceList != null && !lclBookingPieceList.isEmpty()) {
                LclBookingPiece lclBookingPiece = lclBookingPieceList.get(0);
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", coloadComm);
                lclBookingPiece.setCommodityType(commodityType);
                lclBookingPieceDAO.saveOrUpdate(lclBookingPiece);
            }
            forwardPage = "commodityDesc";
            request.setAttribute("lclCommodityList", lclBookingPieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId)));
        } else if ("ert".equalsIgnoreCase(request.getParameter("ratesFlag"))) {
            String ertFlag = "N";
            CommodityType commodityType = new commodityTypeDAO().getCommodityCode(coloadComm);
            if (commodityType != null && commodityType.getDefaultErt()) {
                ertFlag = "Y";
            }
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(ertFlag);
        } else {
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            if (fileId != null && !"".equalsIgnoreCase(fileId)) {
                BigDecimal zeroValues = new BigDecimal(0.00);
                lclCostChargeDAO.updateApAmount(Long.parseLong(fileId), zeroValues, zeroValues, false, true, false, LCL_IMPORT);
                // lclCostChargeDAO.deletetransactionLedger(Long.parseLong(fileId), lclBookingForm.getFileNumber());
                lclCostChargeDAO.deleteArAmount(Long.parseLong(fileId), LCL_IMPORT);
                String bookingAcIds = lclCostChargeDAO.getBkgAcIdsWithoutApStatus(Long.parseLong(fileId));
                String lclBookingAcTransIds = new LCLBookingAcTransDAO().getConcatenatedBookingAcTransIds(bookingAcIds);
                if (CommonUtils.isNotEmpty(bookingAcIds)) {
                    lclCostChargeDAO.deleteChargesById(bookingAcIds);
                    lclManifestDAO.deleteLclAccrualByIds(bookingAcIds, lclFileNumberDAO.getFileNumberByFileId(fileId));
                    lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
                }
            }
            User loginUser = getCurrentUser(request);
            List<LclBookingAc> chargeList = null;
            LclBooking lclBooking = lclBookingDAO.findById(Long.parseLong(fileId));
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
            List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (CommonUtils.isNotEmpty(lclBookingPieceList)) {
                LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
                List<LclImportsRatesBean> importRatesList = Collections.EMPTY_LIST;
                List<LclImportsRatesBean> exceptionRatesList = new ArrayList();
                PortsDAO portsDAO = new PortsDAO();
                String polSchnum = portsDAO.getShedulenumber(polUnCode);
                String podSchnum = portsDAO.getShedulenumber(podUnCode);
                if (!polSchnum.equals("") && !podSchnum.equals("")) {
                    importRatesList = lclImportRatesDAO.getImportRates(polSchnum, podSchnum, "1625", coloadComm, billingType);
                }
                lclImportChargeCalc.calculateImportRate(importRatesList, lclBookingPieceList,
                        lclBooking.getBillToParty(), Long.parseLong(fileId), loginUser, null,
                        lclBookingForm.getTransShipMent(), exceptionRatesList, request);
            }
            chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(fileId), "Imports");
            lclUtils.setWeighMeasureForImportBooking(request, lclBookingPieceList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(fileId), lclCostChargeDAO, lclBookingPieceList,
                    lclBooking.getBillingType(), "", "");
            request.setAttribute("chargeList", chargeList);
            request.setAttribute("lclBooking", lclBooking);
            calculateImpPodFDRates(mapping, form, request, response);
            forwardPage = "chargeDesc";
        }
        return mapping.findForward(forwardPage);
    }

    public ActionForward deleteAutoCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        String fileId = request.getParameter("fileNumberId");
        User loginUser = getCurrentUser(request);
        List<LclBookingAc> chargeList = null;
        chargeList = lclCostChargeDAO.getAllLclChargesByChargeCodeME(Long.parseLong(fileId), "", false);
        if (CommonUtils.isNotEmpty(chargeList)) {
            for (LclBookingAc lclBookingAc : chargeList) {
                String costStatus = lclBookingAcTransDAO.getTransType(lclBookingAc.getId());
                if (CommonUtils.notIn(costStatus, "AP", "IP", "DS") && CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("DELETED -> Code -> ").append(lclBookingAc.getArglMapping().getChargeCode());
                    if (lclBookingAc.getArglMapping().getChargeCode() != null) {
                        sb.append(" Charge Amount -> ").append(lclBookingAc.getArAmount());
                    }

                    if (lclBookingAc.getApAmount() != null) {
                        sb.append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                    }
                    String costId = lclBookingAc.getId().toString();
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, sb.toString(), loginUser.getUserId());
                    String lclBookingAcTransIds = lclBookingAcTransDAO.getConcatenatedBookingAcTransIds(costId);
                    new LclManifestDAO().deleteLclAccruals(Integer.parseInt(costId), lclBookingAc.getLclFileNumber().getFileNumber());
                    lclCostChargeDAO.delete(lclBookingAc);
                    if (CommonUtils.isNotEmpty(lclBookingAcTransIds)) {
                        lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
                    }
                }
            }
        }
        chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(fileId), "Imports");
        LclBooking lclBooking = new LCLBookingDAO().findById(Long.parseLong(fileId));
        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
        List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        lclUtils.setWeighMeasureForImportBooking(request, lclBookingPieceList, null);
        lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(fileId), lclCostChargeDAO, lclBookingPieceList,
                lclBooking.getBillingType(), "", "");
        request.setAttribute("chargeList", chargeList);
        request.setAttribute("lclBooking", lclBooking);
        return mapping.findForward("chargeDesc");
    }

    public ActionForward revertToQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User loginUser = getCurrentUser(request);
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String error = lclBookingUtils.revertToQuote(Long.valueOf(lclBookingForm.getFileNumberId()), lclBookingForm.getFileNumber(), lclBookingForm.getModuleName(), loginUser);
        PrintWriter out = response.getWriter();
        out.print(error);
        out.flush();
        out.close();
        return null;
    }

    public ActionForward validateETACodeFList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportUtils lclImportUtils = new LclImportUtils();
        List<ImportsManifestBean> codeFEmailList = lclImportUtils.getDrCodeFEmailList(request);
        String errorMsg = "";
        if (codeFEmailList.isEmpty()) {
            errorMsg = "Select Customer Type As Code_F Format and Add Email,Fax in Customer Contact";
        }
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMsg);
        return null;
    }

    public ActionForward EftModifyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportUtils lclImportUtils = new LclImportUtils();
        String fileNumberId = request.getParameter("fileNumberId");
        request.setAttribute("contactList", lclImportUtils.getDrCodeFEmailList(request));
        request.setAttribute("headerId", request.getParameter("headerId"));
        new LclUtils().insertLCLRemarks(Long.parseLong(fileNumberId), "Status Update Notification ->Yes", REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
        return mapping.findForward("eftFdModifyDetails");
    }

    public ActionForward sendImpStatusPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String modifyDate = "", subject = "", moduleId = "", headingName = "";
        Integer userId;
        LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
        String fileId = request.getParameter("fileId");
        LclBooking lclBooking = new LCLBookingDAO().findById(Long.parseLong(fileId));
        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
        String maillist = request.getParameter("maillist");
        String headerId = request.getParameter("headerId");
        modifyDate = request.getParameter("eftDate");
        if (CommonUtils.isNotEmpty(modifyDate)) {
            Date EftDate = DateUtils.parseDate(modifyDate, "dd-MMM-yyyy");
            LclBookingImport lclBookingImport = lclBooking.getLclFileNumber().getLclBookingImport();
            lclBookingImport.setFdEta(EftDate);
        }
        if (CommonUtils.isEmpty(headerId)) {
            userId = lclBooking.getEnteredBy().getUserId();
        } else {
            LclSsHeader lclssheader = lclSsHeaderDAO.findById(Long.parseLong(headerId));
            userId = lclssheader.getOwnerUserId().getUserId();
        }
        LclNotificationDAO lclNotificationDAO = new LclNotificationDAO();
        moduleId = lclBooking.getLclFileNumber().getFileNumber() != null ? lclBooking.getLclFileNumber().getFileNumber() : "";
        String companyMnemonicCode = "";
        if ("ECI".equalsIgnoreCase(lclBooking.getLclFileNumber().getBusinessUnit())) {
            companyMnemonicCode = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if ("OTI".equalsIgnoreCase(lclBooking.getLclFileNumber().getBusinessUnit())) {
            companyMnemonicCode = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else {
            companyMnemonicCode = LoadLogisoftProperties.getProperty("application.ECU.companyname");

        }
        headingName = lclNotificationDAO.getDocumentNameByFileid(Long.parseLong(fileId));
        subject = companyMnemonicCode + " " + headingName + " " + moduleId;
        lclNotificationDAO.insertDrStatusUpdate(Long.parseLong(fileId), userId, maillist.trim(), getCurrentUser(request).getUserId(), subject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print("Status Update Sent Sucessfully");
        return null;
    }

    public ActionForward openCurrentLocPopUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        request.setAttribute("oldCurrentLocation", request.getParameter("oldCurreLoca"));
        request.setAttribute("lclBookingForm", lclBookingForm);
        return mapping.findForward("currentLocation");
    }

    public ActionForward saveCurrentLocation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclUtils lclutils = new LclUtils();
        String unLocationId = request.getParameter("unLocationId");
        String oldLocationName = request.getParameter("oldLocationName");
        String errorMessage = "";
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        if (CommonUtils.isNotEmpty(lclBookingForm.getFileNumberId())) {
            Long fileId = Long.parseLong(lclBookingForm.getFileNumberId());
            List<LclBookingDispo> lclBookingDispo = new LclBookingDispoDAO().findByProperty("lclFileNumber.id", fileId);
            UnLocation unLocation = unLocationDAO.findById(Integer.parseInt(unLocationId));
            if (!"RCVD".equalsIgnoreCase(lclBookingDispo.get(0).getDisposition().getEliteCode())) {
                lclutils.insertLclBookingDispo(fileId, lclBookingDispo.get(0).getDisposition(), null, null, getCurrentUser(request), unLocation);
            } else {
                RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unLocation.getUnLocationCode(), "Y");
                Warehouse wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
                new LclBookingDispoDAO().insertBookingDispoForRCVD(fileId, lclBookingDispo.get(0).getDisposition().getId(),
                        null, null, getCurrentUser(request).getUserId(), unLocation.getId(), wareHouse != null ? wareHouse.getId() : null);
            }
            lclutils.insertLCLRemarks(fileId, "Current Location Changed from " + oldLocationName.toUpperCase() + " to " + unLocation.getUnLocationName().toUpperCase(), REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
            errorMessage = "true";
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward isCheckHazmatCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ajaxMessage = "";
        boolean isHazmat = new LclBookingHotCodeDAO().isHazmatHotCodeExist(request.getParameter("fileId"));
        if (isHazmat) {
            ajaxMessage = "true";
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(ajaxMessage);
        return null;
    }

    public ActionForward updateRelease(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ajaxMessage = "";
        Warehouse wareHouse = null;
        Date d = new Date();
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclBookingExport lclBookingExport = new LclBookingExport();
        User user = getCurrentUser(request);
        if (null != lclBookingForm.getFileNumberId()) {
            lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId()));
        }
        if (null == lclBookingExport) {
            lclBookingExport = new LclBookingExport();
            lclBookingExport.setEnteredBy(getCurrentUser(request));
            lclBookingExport.setEnteredDatetime(d);
        }
        if (!"".equalsIgnoreCase(request.getParameter("unLocCode"))) {
            RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(request.getParameter("unLocCode"), "Y");
            wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
        }
        lclBookingExport.setFileNumberId(lclBookingForm.getFileNumberId() != null ? Long.parseLong(lclBookingForm.getFileNumberId()) : 0);
        lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
        lclBookingExport.setRtAgentAcct(null);
        lclBookingExport.setDeliverPickup("P");
        lclBookingExport.setDeliverPickupDatetime(d);

        if (request.getParameter("buttonName").equalsIgnoreCase("ReleaseFromMainScreen")) {
            lclBookingExport.setReleasedDatetime(d);
            //  lclBookingExport.setPrereleaseDatetime(d);
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Cargo Released for Export", getCurrentUser(request).getUserId());
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Inventory Status->WAREHOUSE(Released)", getCurrentUser(request).getUserId());
            ajaxMessage = "R";
        }
        if (request.getParameter("buttonName").equalsIgnoreCase("UnReleaseFromMainScreen")) {
            lclBookingExport.setReleasedDatetime(null);
            //  lclBookingExport.setPrereleaseDatetime(null);
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Cargo Un-Released", getCurrentUser(request).getUserId());
            ajaxMessage = "UR";
            if (lclBookingExport.getPrereleaseDatetime() != null) {
                ajaxMessage = "URPR";
            }
        }
        if (request.getParameter("buttonName").equalsIgnoreCase("R") && null == lclBookingExport.getReleasedDatetime()) {
            lclBookingExport.setReleasedDatetime(d);
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Cargo Released for Export", getCurrentUser(request).getUserId());
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Inventory Status->WAREHOUSE(Released)", getCurrentUser(request).getUserId());
            ajaxMessage = "R";
        } else if (request.getParameter("buttonName").equalsIgnoreCase("UR") && null != lclBookingExport.getReleasedDatetime()) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Cargo UnReleased ", getCurrentUser(request).getUserId());
            lclBookingExport.setReleasedDatetime(null);
            ajaxMessage = "UR";
        } else if (request.getParameter("buttonName").equalsIgnoreCase("PR") && null == lclBookingExport.getPrereleaseDatetime()) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), "auto", "Cargo Pre-Released", getCurrentUser(request).getUserId());
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, "Inventory Status->WAREHOUSE(Pre-Released)", getCurrentUser(request).getUserId());
            lclBookingExport.setPrereleaseDatetime(d);
            ajaxMessage = "PR";
        } else if (request.getParameter("buttonName").equalsIgnoreCase("PR") && null != lclBookingExport.getPrereleaseDatetime()) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), "auto", "Cargo Un-PreReleased", getCurrentUser(request).getUserId());
            lclBookingExport.setPrereleaseDatetime(null);
            ajaxMessage = "UPR";
        }
        lclBookingExport.setPrereleaseUser(getCurrentUser(request));
        lclBookingExport.setReleaseUser(getCurrentUser(request));
        lclBookingExport.setModifiedBy(getCurrentUser(request));
        lclBookingExport.setModifiedDatetime(d);
        lclBookingExport.setAes(null != lclBookingForm.getAesBy() ? lclBookingForm.getAesBy() : false);
        //lclBookingExport.setUps(null != lclBookingForm.getUps() ? lclBookingForm.getUps() : false);
        new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
        new LCLBookingDAO().updateModifiedDateTime(Long.parseLong(lclBookingForm.getFileNumberId()), user.getUserId());

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(ajaxMessage);
        return null;
    }

    public ActionForward quickDrAddHotCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        List<LclBookingHotCode> quickDrHotCode = (List<LclBookingHotCode>) session.getAttribute("lclQuickBookingHotCodeList");
        LclBookingHotCode lclBookingHotCode = new LclBookingHotCode();
        if (quickDrHotCode == null) {
            quickDrHotCode = new ArrayList<LclBookingHotCode>();
        }
        String acctNo = request.getParameter("accountNo");
        if (CommonUtils.isNotEmpty(acctNo)) {
            List<Object[]> hotCodeList = new GeneralInformationDAO().getHotCodesForAccount(acctNo);
            if (CommonUtils.isNotEmpty(hotCodeList)) {
                for (Object[] code : hotCodeList) {
                    lclBookingHotCode = new LclBookingHotCode();
                    lclBookingHotCode.setId(Long.parseLong(code[0].toString()));
                    lclBookingHotCode.setCode(code[1].toString());
                    if (!quickDrHotCode.contains(lclBookingHotCode)) {
                        quickDrHotCode.add(lclBookingHotCode);
                    }
                }
            }
        } else {
            lclBookingHotCode.setCode(lclBookingForm.getHotCodes());
            lclBookingHotCode.setId(Long.valueOf(lclBookingForm.getGenCodefield1()));
            quickDrHotCode.add(lclBookingHotCode);
        }
        session.setAttribute("lclQuickBookingHotCodeList", quickDrHotCode);
        return mapping.findForward("quickDrHotCode");
    }

    public ActionForward quickDrDeleteHotCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        List<LclBookingHotCode> quickDrHotCode = (List<LclBookingHotCode>) session.getAttribute("lclQuickBookingHotCodeList");
        List<LclBookingHotCode> newQuickDRHotCode = new ArrayList<LclBookingHotCode>();
        Long Code = lclBookingForm.getHotCodeId();
        Iterator<LclBookingHotCode> hscode = quickDrHotCode.iterator();
        while (hscode.hasNext()) {
            LclBookingHotCode value = hscode.next();
            if (value.getId().equals(Code)) {
                hscode.remove();
            } else {
                LclBookingHotCode newHotCode = new LclBookingHotCode();
                newHotCode.setCode(value.getCode());
                newHotCode.setId(value.getId());
                newQuickDRHotCode.add(newHotCode);
            }
        }
        session.removeAttribute("lclQuickBookingHotCodeList");
        session.setAttribute("lclQuickBookingHotCodeList", newQuickDRHotCode);
        return mapping.findForward("quickDrHotCode");
    }

    public ActionForward addOrRemoveBatchCode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        String button = lclBookingForm.getButton();
        List<LclBookingHsCode> batchHsCode = (List<LclBookingHsCode>) session.getAttribute("lclBookingHsCodes");
        if (button.equalsIgnoreCase("addBatchHsCode")) {
            if (batchHsCode == null) {
                batchHsCode = new ArrayList<LclBookingHsCode>();
            }
            LclBookingHsCode newBatchHsCode = new LclBookingHsCode();
            newBatchHsCode.setCodes(lclBookingForm.getBatchHsCode());
            batchHsCode.add(newBatchHsCode);
            session.setAttribute("lclBookingHsCodes", batchHsCode);
        } else if (button.equalsIgnoreCase("deleteBatchHsCode")) {
            List<LclBookingHsCode> newHsCode = new ArrayList<LclBookingHsCode>();
            String Code = lclBookingForm.getBatchHsCode();
            Iterator<LclBookingHsCode> hscode = batchHsCode.iterator();
            while (hscode.hasNext()) {
                LclBookingHsCode value = hscode.next();
                if (value.getCodes().equalsIgnoreCase(Code)) {
                    hscode.remove();
                } else {
                    LclBookingHsCode newBatchHsCode = new LclBookingHsCode();
                    newBatchHsCode.setCodes(value.getCodes());
                    newHsCode.add(newBatchHsCode);
                }
            }
            session.removeAttribute("lclBookingHsCodes");
            session.setAttribute("lclBookingHsCodes", newHsCode);
        } else if (button.equalsIgnoreCase("checkBatchHsCode")) {
            String ajaxMessage = "";
            boolean flag = false;
            if (batchHsCode != null) {
                String Code = lclBookingForm.getBatchHsCode();
                Iterator<LclBookingHsCode> hscode = batchHsCode.iterator();
                while (hscode.hasNext()) {
                    LclBookingHsCode value = hscode.next();
                    if (value.getCodes().equalsIgnoreCase(Code)) {
                        flag = true;
                    }
                }
            }
            if (!flag && null != lclBookingForm.getFileNumberId()) {
                flag = new LclBookingHsCodeDAO().isHsCodeExistsForConsolidateFile(lclBookingForm.getFileNumberId(), lclBookingForm.getBatchHsCode());
            }
            ajaxMessage = flag ? "true" : "false";
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(ajaxMessage);
            return null;
        } else if (button.equalsIgnoreCase("saveBatchHsCode")) {
            Long currentFileId = Long.parseLong(lclBookingForm.getFileNumberId());
            List consolidateFileList = new LclConsolidateDAO().getConsolidatesFiles(currentFileId);
            consolidateFileList.add(currentFileId);
            for (Object fileId : consolidateFileList) {
                for (LclBookingHsCode hscode : batchHsCode) {
                    LclBookingHsCode lclBookingHsCode = new LclBookingHsCode();
                    lclBookingHsCode.setLclFileNumber(new LclFileNumberDAO().findById(Long.parseLong(fileId.toString())));
                    lclBookingHsCode.setCodes(hscode.getCodes());
                    lclBookingHsCode.setEnteredBy(getCurrentUser(request));
                    lclBookingHsCode.setModifiedBy(getCurrentUser(request));
                    lclBookingHsCode.setModifiedDatetime(new Date());
                    lclBookingHsCode.setEnteredDatetime(new Date());
                    new LclBookingHsCodeDAO().saveOrUpdate(lclBookingHsCode);
                }
            }
            List<LclBookingHsCode> hsCodeList = new LclBookingHsCodeDAO().getHsCodeList(currentFileId);
            session.removeAttribute("lclBookingHsCodes");
            request.setAttribute("lclBookingHsCodeList", hsCodeList);
            return mapping.findForward("hsCode");
        }
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileId", lclBookingForm.getFileNumberId());
        return mapping.findForward("baseHsCode");
    }

    public ActionForward baseHsCodePopUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        session.removeAttribute("lclBookingHsCodes");
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileId", lclBookingForm.getFileNumberId());
        return mapping.findForward("baseHsCode");
    }

    public ActionForward triggerRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLBookingForm lclBookingForm = (LCLBookingForm) form;
            out = response.getWriter();
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            String commodityNo = request.getParameter("commodityNo");
            Long fileNumberId = Long.parseLong(lclBookingForm.getFileNumberId());
            List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
            if (CommonUtils.isNotEmpty(lclBookingPieceList)) {
                LclBookingPiece lclBookingPiece = lclBookingPieceList.get(0);
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", commodityNo);
                lclBookingPiece.setCommodityType(commodityType);
                lclBookingPieceDAO.saveOrUpdate(lclBookingPiece);
                lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
                request.setAttribute("lclCommodityList", lclBookingPieceList);
                JspWrapper jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityDesc.jsp").include(request, jspWrapper);
                result.put("commodityDesc", jspWrapper.getOutput());

                User user = getCurrentUser(request);
                lclBookingForm.setEnums(request, "Imports", lclBookingForm.getTransShipMent());
                LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
                LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();

                String polUnCode = "";
                String podUnCode = "";
                String fdUnCode = "";
                String agentNo = "";
                String originCode = "";
                LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileNumberId);
                new LCLBookingDAO().getCurrentSession().evict(lclBooking);
                lclBooking.setConsAcct(lclBookingForm.getLclBooking().getConsAcct());
                lclBooking.setNotyAcct(lclBookingForm.getLclBooking().getNotyAcct());
                new LCLBookingDAO().saveOrUpdate(lclBooking);
                fdUnCode = lclBookingForm.getUnlocationCode();
                if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                    polUnCode = lclBooking.getPortOfLoading().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclBooking.getPortOfDestination().getUnLocationName())) {
                    podUnCode = lclBooking.getPortOfDestination().getUnLocationCode();
                }
                if (CommonFunctions.isNotNull(lclBooking.getSupAcct()) && CommonFunctions.isNotNull(lclBooking.getSupAcct().getAccountno())) {
                    agentNo = lclBooking.getSupAcct().getAccountno();
                }
                if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin()) && CommonFunctions.isNotNull(lclBooking.getPortOfOrigin().getUnLocationName())) {
                    originCode = lclBooking.getPortOfOrigin().getUnLocationCode();
                }
                if (CommonUtils.isNotEmpty(lclBookingForm.getHeaderId())) {
                    LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(lclBookingForm.getHeaderId()));
                    request.setAttribute("lclssheader", lclssheader);
                }
                lclBookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                String transhipment = lclBookingForm.getTransShipMent();
                lclImportChargeCalc.ImportRateCalculation(originCode, polUnCode, podUnCode, fdUnCode, transhipment, lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo,
                        lclBookingForm.getImpCfsWarehsId(), fileNumberId, lclBookingPieceList, request, user, lclBookingForm.getUnitSsId());
                List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_IMPORT);
                lclUtils.setWeighMeasureForImportBooking(request, lclBookingPieceList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargeDAO, lclBookingPieceList, lclBooking.getBillingType(), "", agentNo);
                if (lclBooking.getPortOfLoading() != null && lclBooking.getFinalDestination() != null) {
                    String pooUnCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
                    request.setAttribute("totalStorageAmt", lclImportChargeCalc.calculateImpAutoRates(lclBooking.getPortOfLoading().getUnLocationCode(),
                            lclBooking.getPortOfDestination().getUnLocationCode(), pooUnCode, "", lclBookingPieceList, transhipment));
                }
                request.setAttribute("chargeList", chargeList);
                request.setAttribute("lclBooking", lclBooking);

                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/chargeDesc.jsp").include(request, jspWrapper);
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

    public ActionForward addingClientHotCodes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        String refValue = request.getParameter("refValue");
        String remarks = "";
        LclBookingHotCodeDAO hotCodeDAO = new LclBookingHotCodeDAO();
        List<String> acctList = new ArrayList<String>();
        String acctNo = request.getParameter("accountNo");

        if (CommonUtils.isEmpty(acctNo)) {
            if (null != lclBookingForm.getLclBooking()) {
                if (null != lclBookingForm.getLclBooking().getClientAcct()) {
                    acctList.add(lclBookingForm.getLclBooking().getClientAcct().getAccountno());
                }
                if (null != lclBookingForm.getLclBooking().getShipAcct()) {
                    acctList.add(lclBookingForm.getLclBooking().getShipAcct().getAccountno());
                }
                if (null != lclBookingForm.getLclBooking().getFwdAcct()) {
                    acctList.add(lclBookingForm.getLclBooking().getFwdAcct().getAccountno());
                }
                if (null != lclBookingForm.getLclBooking().getConsAcct()) {
                    acctList.add(lclBookingForm.getLclBooking().getConsAcct().getAccountno());
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
                            .isHotCodeNotExist(code.toString(), lclBookingForm.getFileNumberId());
                    remarks = "Inserted - HOT Code#" + " " + str;
                    new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, remarks.toUpperCase(), getCurrentUser(request).getUserId());
                    if (isHotCodeNotExist) {
                        hotCodeDAO.saveHotCode(Long.parseLong(lclBookingForm.getFileNumberId()),
                                code.toString(), getCurrentUser(request).getUserId());
                    }
                }
            }
        }
        request.setAttribute("lclHotCodeList", hotCodeDAO
                .findByProperty("lclFileNumber.id", Long.parseLong(lclBookingForm.getFileNumberId())));
        return mapping.findForward(
                "hoteCodes");
    }

    public ActionForward upComingSailingsForQuickDr(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        List<LclBookingVoyageBean> upcomingSailings = null;
        LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
        if (CommonUtils.isNotEmpty(lclBookingForm.getPortOfOriginId()) && CommonUtils.isNotEmpty(lclBookingForm.getFinalDestinationId())) {
            String cfcl = (null == lclBookingForm.getCfcl() && lclBookingForm.getCfclForDR() != false) ? "C" : "E";
            LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(lclBookingForm.getPortOfOriginId(),
                    lclBookingForm.getFinalDestinationId(), "N");
            if (bookingPlanBean != null && CommonUtils.isNotEmpty(bookingPlanBean.getPol_id())
                    && CommonUtils.isNotEmpty(bookingPlanBean.getPod_id())) {
                if (lclBookingForm.getPreviousSailing().equalsIgnoreCase("true")) {
                    upcomingSailings = bookingPlanDAO.getUpComingSailingsScheduleOlder(lclBookingForm.getPortOfOriginId(),
                            bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(), lclBookingForm.getFinalDestinationId(), "V", bookingPlanBean,
                            lclBookingForm.getPreviousSailing(), cfcl);
                } else {
                    upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclBookingForm.getPortOfOriginId(),
                            bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(), lclBookingForm.getFinalDestinationId(), "V", bookingPlanBean, cfcl);
                }
            }
        }
//        if (lclBookingPlanBean != null) {
//            if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPolId()) && CommonUtils.isNotEmpty(lclBookingPlanBean.getPodId())) {
//                sailingScheduleList = lclBookingPlanDAO.getVoyageList(lclBookingForm.getPortOfOriginId(),
//                        Integer.parseInt(lclBookingPlanBean.getPolId()), Integer.parseInt(lclBookingPlanBean.getPodId()), lclBookingForm.getFinalDestinationId(), "V");
//            }
//        }
        request.setAttribute("voyageList", upcomingSailings);
        return mapping.findForward(LCL_BOOKING_VOYAGE);
    }

    public void updateEconoEculineBkg(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber,
            HttpServletRequest request) throws Exception {
        LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
        String ports = !"".equalsIgnoreCase(lclBookingForm.getPodUnlocationcode())
                ? lclBookingForm.getPodUnlocationcode() : lclBookingForm.getUnlocationCode();
        String defaultAgentSetUp = new LCLPortConfigurationDAO().getDefaultPortSetUpCode(ports);
        String agentNumber = new LCLBookingDAO().getAgentAccountNo(lclBookingForm.getFileNumberId());
        String getAgentLevelBrand = new AgencyInfoDAO().getAgentLevelBrand(agentNumber, ports);
        String terminal = lclFileNumber != null
                && lclBookingForm.getLclBooking().getTerminal() != null
                        ? lclBookingForm.getLclBooking().getTerminal().getTrmnum() : "";

        if ("".equalsIgnoreCase(getAgentLevelBrand) && !terminal.equalsIgnoreCase("59")) {
            if ("B".equalsIgnoreCase(defaultAgentSetUp)) {
                if ("ECULINE_OR_ECONO_FROM_COMMODITY".equalsIgnoreCase(lclBookingForm.getEculineCommodity())) {
                    boolean isEculine = new LCLBookingDAO().checkEculineCommInExport("", ports, lclFileNumber.getId().toString());
                    String EcuOrEci = null != lclFileNumber.getBusinessUnit() ? lclFileNumber.getBusinessUnit() : lclBookingForm.getBusinessUnit();
                    String systemUser = new UserDAO().getUserInfo("system").getId().toString();
                    if (isEculine && ("ECI".equalsIgnoreCase(EcuOrEci) || "OTI".equalsIgnoreCase(EcuOrEci))) {
                        new LclDwr().updateEconoOREculine("ECU", lclFileNumber.getId().toString(), systemUser, REMARKS_DR_AUTO_NOTES);
                        lclBookingForm.setBusinessUnit("ECU");
                        LclBookingHotCode lclBookingHotCode = lclBookingHotCodeDAO.getHotCodeByFileIDCode(lclFileNumber.getId(), "EBL/ECULINE BILL OF LADING");
                        if (null == lclBookingHotCode) {
                            lclBookingHotCodeDAO.saveHotCode(lclFileNumber.getId(), "EBL/ECULINE BILL OF LADING", getCurrentUser(request).getUserId());
                        }
                    } else if (!isEculine && "ECU".equalsIgnoreCase(EcuOrEci)) {
                        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                        companyCode = companyCode.equalsIgnoreCase("03") ? "ECI" : "OTI";
                        new LclDwr().updateEconoOREculine(companyCode, lclFileNumber.getId().toString(), systemUser, REMARKS_DR_AUTO_NOTES);
                        lclBookingForm.setBusinessUnit(companyCode);
                        LclBookingHotCode lclBookingHotCode = lclBookingHotCodeDAO.getHotCodeByFileIDCode(lclFileNumber.getId(), "EBL/ECULINE BILL OF LADING");
                        if (null != lclBookingHotCode) {
                            lclBookingHotCodeDAO.delete(lclBookingHotCode);
                        }
                    } else {
                        String previousType = new LclFileNumberDAO().getBusinessUnit(lclFileNumber.getId().toString());
                        if (CommonUtils.isEmpty(previousType)) {
                            new LclFileNumberDAO().updateEconoEculine(lclBookingForm.getBusinessUnit(), lclFileNumber.getId().toString());
                        }
                    }
                }
            } else {
                String previousType = new LclFileNumberDAO().getBusinessUnit(lclFileNumber.getId().toString());
                if (CommonUtils.isEmpty(previousType)) {
                    new LclFileNumberDAO().updateEconoEculine(lclBookingForm.getBusinessUnit(), lclFileNumber.getId().toString());
                }
            }
        }
        lclBookingForm.setEculineCommodity("");
    }

    public ActionForward calculateRatesWhenSelectCustomer(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LCLBookingForm bookingForm = (LCLBookingForm) form;
            Long fileId = Long.parseLong(bookingForm.getFileNumberId());
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
            List<LclBookingPiece> commodity_list = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
            List<LclBookingAc> chargeList = null;
            Map<String, String> result = new HashMap<String, String>();
            JspWrapper jspWrapper = null;
            out = response.getWriter();
            response.setContentType("application/json");
            if (CommonUtils.isNotEmpty(commodity_list)) {
                String commNo = null != request.getParameter("commNo") ? request.getParameter("commNo") : "";
                if (!commNo.trim().equals("000000")) {
                    commodity_list.get(0).setCommodityType(new commodityTypeDAO().getByProperty("code", commNo.trim()));
                }
                request.setAttribute("lclCommodityList", commodity_list);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityDesc.jsp").include(request, jspWrapper);
                result.put("commodityDesc", jspWrapper.getOutput());
                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                User user = getCurrentUser(request);
                BigDecimal valueOfGoods = CommonUtils.isNotEmpty(bookingForm.getValueOfGoods())
                        ? new BigDecimal(bookingForm.getValueOfGoods()) : BigDecimal.ZERO;
                lclBooking.setModifiedDatetime(new Date());
                request.setAttribute("lclBooking", lclBooking);
                String rateType = bookingForm.getRateType().equalsIgnoreCase("R") ? "Y" : "C";
                if (!lclBooking.getNonRated()) {
                    lclChargesCalculation.calculateRates(lclBooking.getPortOfOrigin().getUnLocationCode(), lclBooking.getFinalDestination().getUnLocationCode(),
                            lclBooking.getPortOfLoading().getUnLocationCode(), lclBooking.getPortOfDestination().getUnLocationCode(),
                            fileId, commodity_list, user, null, bookingForm.getInsurance(), valueOfGoods,
                            rateType, "C", null, null, null, null, bookingForm.getCalcHeavy(), bookingForm.getDeliveryMetro(),
                            bookingForm.getPcBoth(), null, null, request, lclBooking.getBillToParty());

                    chargeList = new LclCostChargeDAO().getLclCostByFileNumberAsc(fileId, "Exports");
                    lclUtils.setWeighMeasureForBooking(request, commodity_list, lclChargesCalculation.getPorts());
                    lclUtils.setRolledUpChargesForBooking(chargeList, request, fileId, new LclCostChargeDAO(), commodity_list,
                            bookingForm.getBillingType(), lclChargesCalculation.getPorts().getEngmet(), "No");
                }
                request.setAttribute("chargeList", chargeList);
                request.setAttribute("lclBooking", lclBooking);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/chargeDesc.jsp").include(request, jspWrapper);
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

    private LclBooking setLclBookingValues(LclBooking lclBooking, LCLBookingForm lclBookingForm) throws Exception {
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        LCLBookingDAO lCLBookingDAO = new LCLBookingDAO();
        if (CommonUtils.isNotEmpty(lclBookingForm.getOriginUnlocationCode())) {
            lclBooking.setPortOfOrigin(unLocationDAO.getUnlocation(lclBookingForm.getOriginUnlocationCode()));
        }
        if (CommonUtils.isNotEmpty(lclBookingForm.getPolUnlocationcode())) {
            lclBooking.setPortOfLoading(unLocationDAO.getUnlocation(lclBookingForm.getPolUnlocationcode()));
        }
        if (CommonUtils.isNotEmpty(lclBookingForm.getPodUnlocationcode())) {
            lclBooking.setPortOfDestination(unLocationDAO.getUnlocation(lclBookingForm.getPodUnlocationcode()));
        }
        if (CommonUtils.isNotEmpty(lclBookingForm.getDestination())) {
            lclBooking.setFinalDestination(unLocationDAO.getUnlocation(lclBookingForm.getDestination()));
        }
        lCLBookingDAO.saveOrUpdate(lclBooking);
        lCLBookingDAO.getCurrentSession().flush();
        lCLBookingDAO.getCurrentSession().clear();
        return lclBooking;
    }

    public ActionForward calculateTTREVCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String fileId = request.getParameter("fileNumberId");
            String PortOfLoading = request.getParameter("polUnloc");
            Long longFileId = Long.parseLong(fileId);
            User loginUser = getCurrentUser(request);
            PortsDAO portsDAO = new PortsDAO();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            LclBooking lclBooking = bookingDAO.findById(longFileId);
            bookingDAO.getCurrentSession().evict(lclBooking);
            List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            String engmet = new String();
            String rateType = lclBooking.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                destinationfd = ports.getEciportcode();
            }
            Ports portspol = portsDAO.getByProperty("unLocationCode", PortOfLoading);
            if (portspol != null && portspol.getEciportcode() != null && !portspol.getEciportcode().trim().equals("")) {
                polorigin = portspol.getEciportcode();
            }
            String relay = request.getParameter("relay");
            String pcBoth = request.getParameter("pcBoth");
            lclBooking.setRelayOverride("Y".equalsIgnoreCase(relay) ? true : false);
            lclBooking.setPortOfLoading(new UnLocationDAO().getUnlocation(PortOfLoading));

            LclBookingAc lclBookingAc = lclCostChargeDAO.getTTCharges(longFileId, false);
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            if (relay.equals("Y") && lclCommodityList != null && lclCommodityList.size() > 0) {
                GlMappingDAO glmappingdao = new GlMappingDAO();
                lclBooking.setInsurance(true);
                String BlueScreenTTRevChgCode = lclCostChargeDAO.BlueScreenTTRevChgCode(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(BlueScreenTTRevChgCode, "LCLE", "AR");
                User loginuser = getCurrentUser(request);
                if (lclBookingAc == null) {
                    lclBookingAc = new LclBookingAc();
                    lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
                    lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                    lclBookingAc.setBundleIntoOf(false);
                    lclBookingAc.setPrintOnBl(true);
                    lclBookingAc.setAdjustmentAmount(new BigDecimal(0.00));
                    lclBookingAc.setArglMapping(glmapping);
                    lclBookingAc.setEnteredDatetime(new Date());
                    lclBookingAc.setEnteredBy(loginuser);
                    lclBookingAc.setArBillToParty(lclBooking.getBillToParty());
                    lclBookingAc.setApBillToParty(lclBooking.getBillToParty());
                }
                lclChargesCalculation.calculaterelayTTCharge(pooorigin, polorigin, lclCommodityList,
                        engmet, loginuser, longFileId, lclBookingAc, glmapping, BlueScreenTTRevChgCode, request);
            }
            lclBooking.setModifiedBy(loginUser);
            lclBooking.setModifiedDatetime(new Date());
            bookingDAO.saveOrUpdate(lclBooking);
            request.setAttribute("lclBooking", lclBooking);
            List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, "Exports");
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
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
            LCLBookingForm bookingForm = (LCLBookingForm) form;
            if (CommonUtils.isNotEmpty(bookingForm.getFileNumberId())) {
                out = response.getWriter();
                response.setContentType("application/json");
                Map<String, String> result = new HashMap<String, String>();
                String spotRate = request.getParameter("spotRate");
                boolean isSpotRate = "Y".equalsIgnoreCase(spotRate) ? true : false;
                User loginUser = getCurrentUser(request);
                LCLBookingDAO bookingDAO = new LCLBookingDAO();
                LclBookingPieceDAO bookingPieceDAO = new LclBookingPieceDAO();
                Long fileId = Long.parseLong(bookingForm.getFileNumberId());
                LclBooking lclBooking = bookingDAO.findById(fileId);
                lclBooking.setSpotRateUom("M");
                lclBooking.setSpotComment(null);
                lclBooking.setSpotRateBottom(false);
                lclBooking.setSpotRate(isSpotRate);
                lclBooking.setSpotOfRate(false);
                lclBooking.setSpotWmRate(null);
                lclBooking.setSpotRateMeasure(null);
                lclBooking.setModifiedBy(loginUser);
                lclBooking.setModifiedDatetime(new Date());
                bookingDAO.saveOrUpdate(lclBooking);
                if (!isSpotRate && CommonUtils.isNotEmpty(bookingForm.getExternalComment())
                        && bookingForm.getExternalComment().contains("SPOT RATE")) {
                    bookingForm.setExternalComment("");
                    this.addExternal(bookingForm, lclBooking.getLclFileNumber(), getCurrentUser(request), request);
                }
                List<LclBookingPiece> bookingPieceList = bookingPieceDAO.findByProperty("lclFileNumber.id", fileId);
                if (isSpotRate) {
                    MessageResources messageResources = getResources(request);
                    String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                    LclBookingPiece commodity = bookingPieceList.isEmpty() ? new LclBookingPiece() : bookingPieceList.get(0);
                    commodity.setCommodityType(new commodityTypeDAO().getByProperty("code", spotRateCommodity));
                    bookingPieceDAO.update(commodity);
                }
                new ExportBookingUtils().refreshRates(bookingForm, request, fileId, lclBooking, loginUser, bookingPieceList);
                JspWrapper jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/chargeDesc.jsp").include(request, jspWrapper);
                result.put("chargeDesc", jspWrapper.getOutput());
                List<LclBookingPiece> resetBookingPieceList = bookingPieceDAO.findByProperty("lclFileNumber.id", fileId);
                request.setAttribute("lclCommodityList", resetBookingPieceList);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/commodityDesc.jsp").include(request, jspWrapper);
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

    public ActionForward reverseToOBKG(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        User loginUser = getCurrentUser(request);
        LclBookingPieceDAO bookingPieceDAO = new LclBookingPieceDAO();
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        Long fileNumberId = Long.parseLong(bookingForm.getFileNumberId());
        LclBooking lclBooking = bookingDAO.findById(fileNumberId);
        bookingDAO.getCurrentSession().evict(lclBooking);
        new LclRemarksDAO().insertLclRemarks(fileNumberId, REMARKS_DR_AUTO_NOTES, "Reverse To OBKG", loginUser.getUserId());
        new LclFileNumberDAO().updateFileStatus(fileNumberId, "B");
        List<LclBookingPiece> bookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
        boolean ratesFlag = false;
        if (null != bookingPieceList && !bookingPieceList.isEmpty()) {
            for (LclBookingPiece bookingPiece : bookingPieceList) {
                if (null != bookingPiece.getBookedWeightImperial() && null != bookingPiece.getBookedVolumeImperial()
                        && (!bookingPiece.getBookedWeightImperial().equals(bookingPiece.getActualWeightImperial())
                        || !bookingPiece.getBookedVolumeImperial().equals(bookingPiece.getActualVolumeImperial()))) {
                    ratesFlag = true;
                } else if (null == bookingPiece.getBookedWeightImperial() || null == bookingPiece.getBookedVolumeImperial()) {
                    ratesFlag = true;
                }
                bookingPiece.setModifiedBy(loginUser);
                bookingPiece.setModifiedDatetime(new Date());
                bookingPiece.setActualPackageType(null);
                bookingPiece.setActualPieceCount(null);
                bookingPiece.setActualVolumeImperial(null);
                bookingPiece.setActualVolumeMetric(null);
                bookingPiece.setActualWeightImperial(null);
                bookingPiece.setActualWeightMetric(null);
                bookingPieceDAO.update(bookingPiece);
            }
        }
        Integer dispoId = new LclDwr().disposDesc("B");
        new LclBookingDispoDAO().insertBookingDispo(fileNumberId, dispoId, loginUser.getUserId());
        List<LclBookingPiece> bookingPieceLists = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
        if (ratesFlag) {
            String rateType = "R".equalsIgnoreCase(bookingForm.getRateType()) ? "Y" : bookingForm.getRateType();
            String polUnCode = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
            String podUnCode = null != lclBooking.getPortOfDestination() ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
            new LclChargesCalculation().calculateRates(lclBooking.getPortOfOrigin().getUnLocationCode(),
                    lclBooking.getFinalDestination().getUnLocationCode(), polUnCode,
                    podUnCode, fileNumberId, bookingPieceLists, loginUser,
                    bookingForm.getPooDoor(), bookingForm.getInsurance(), bookingForm.getLclBooking().getValueOfGoods(),
                    rateType, "C", null, null, null, null, bookingForm.getCalcHeavy(),
                    bookingForm.getDeliveryMetro(), bookingForm.getPcBoth(), null, "", request, lclBooking.getBillToParty());
        }
        editBooking(mapping, form, request, response);
        return mapping.findForward("exportBooking");
    }

    public ActionForward makeDRasParentDR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long parent = Long.parseLong(request.getParameter("fileA"));
        List fileList = new LclConsolidateDAO().getConsolidatesFiles(parent);
        fileList.add(parent);
        for (Object fileId : fileList) {
            new LclConsolidateDAO().updateConsolidateFile(Long.parseLong(fileId.toString()), parent, "");
        }
        lclUtils.getFormattedConsolidatedList(request, Long.parseLong(request.getParameter("currentFileId")));
        return mapping.findForward(CONSOLIDATE_DESC);
    }

    public ActionForward calculateImpCfsCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        Long ssHeaderId = Long.parseLong(bookingForm.getHeaderId());
        Long unitId = Long.parseLong(bookingForm.getUnitId());
        String unitSsId = bookingForm.getUnitSsId();
        User loginUser = getCurrentUser(request);
        LclBookingPieceDAO bookingPieceDAO = new LclBookingPieceDAO();
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        PortsDAO portsDAO = new PortsDAO();
        LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
        Long fileNumberId = Long.parseLong(bookingForm.getFileNumberId());
        LclBooking lclBooking = bookingDAO.findById(fileNumberId);
        bookingDAO.getCurrentSession().evict(lclBooking);
        List<LclBookingPiece> bookingPieceList = bookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
        LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
        String polSchnum = portsDAO.getShedulenumber(lclBooking.getPortOfLoading().getUnLocationCode());//pol Schedule Number
        String podSchnum = portsDAO.getShedulenumber(lclBooking.getPortOfDestination().getUnLocationCode());
        String fdSchnum = portsDAO.getShedulenumber(lclBooking.getFinalDestination().getUnLocationCode());
        String pooSchnum = portsDAO.getShedulenumber(null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "");
        List<String> ipiCommList = new ArrayList<String>();
        LCLImportRatesDAO importRatesDAO = new LCLImportRatesDAO();
        if (CommonUtils.isNotEmpty(podSchnum)) {
            Object[] billingTypes = new LclUnitSsDAO().getBillingTypeByUnit(unitSsId);
            List<Long> bookingAcIdList = new ArrayList<Long>();
            if (null != billingTypes && billingTypes.length > 0 && billingTypes[1] != null
                    && !billingTypes[1].toString().trim().equals("")) {
                ipiCommList.add(billingTypes[1].toString());
                List<String> billingWarehsTermsList = new ArrayList<String>();
                billingWarehsTermsList.add("W");
                List<LclImportsRatesBean> exceptionRatesList = importRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, ipiCommList);
                List<LclImportsRatesBean> warehouseChargesList = new LCLImportRatesDAO().getLCLImportCharges(polSchnum, podSchnum, ipiCommList,
                        null, billingWarehsTermsList);
                List<LclBookingAc> warehouseChargeList = lclImportChargeCalc.calculateImportRate(warehouseChargesList, bookingPieceList,
                        "W", null, loginUser, null, "N", exceptionRatesList, request);
                if (null != warehouseChargeList && !warehouseChargeList.isEmpty()) {
                    boolean stripDateFlag = new LclUnitWhseDAO().isStrippedDateExist(ssHeaderId, unitId);
                    for (LclBookingAc bookingAc : warehouseChargeList) {
                        bookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
                        bookingAc.setPostAr(false);
                        lclCostChargesDAO.saveOrUpdate(bookingAc);
                        bookingAcIdList.add(bookingAc.getId());
                    }
                    if (!stripDateFlag && CommonUtils.isNotEmpty(bookingForm.getImpStripDate()) && CommonUtils.isNotEmpty(bookingAcIdList)) {
                        new LclImportWarehouseRateUtil().manifestWarehouseCharges(Long.parseLong(unitSsId), bookingAcIdList, loginUser);
                    }

                }
            }
        }
        List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclBooking.getFileNumberId(), "Imports");
        if (chargeList == null || chargeList.isEmpty()) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        }
        request.setAttribute("lclBooking", lclBooking);
        lclUtils.setWeighMeasureForImportBooking(request, bookingPieceList, null);
        lclUtils.setImportRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargesDAO,
                bookingPieceList, bookingForm.getLclBooking().getBillingType(), "", "");
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward getDataFromEdiSystem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileNumberId));
        String exportRefNo = "", marksAndNo = "", commodityDesc = "";
        String bookingNo = lclFileNumber.getFileNumber();
        EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
        EdiTrackingSystem ediTrackingSystem = ediTrackingSystemDAO.findByBookingNo(bookingNo);
        List knTrackingSystem = new BookingEnvelopeDao().getKnOblSyatemTracking(new LclDwr().getKNOblNo(fileNumberId, lclFileNumber.getFileNumber()));
        if (null != ediTrackingSystem) {
            Shipment shipment = (Shipment) ediTrackingSystemDAO.findShipmentByEdiTrackingSystem(ediTrackingSystem, request);
            HashSet<PackageDetails> packSet = (HashSet<PackageDetails>) shipment.getPackageDetailsSet();
            PackageDetails packageDetails = null;
            Iterator<PackageDetails> i = packSet.iterator();
            if (i.hasNext()) {
                packageDetails = i.next();
            }
            if (null != shipment) {
                exportRefNo = null != shipment.getExportRefNo() ? shipment.getExportRefNo().toUpperCase() : "";
            }
            if (null != packageDetails) {
                marksAndNo = null != packageDetails.getMarksAndNo() ? packageDetails.getMarksAndNo().toUpperCase() : "";
                marksAndNo = marksAndNo.replace(":;", ":");
                marksAndNo = marksAndNo.replaceAll(";", "<br/>");
                commodityDesc = null != packageDetails.getCommodity()
                        ? packageDetails.getCommodity().replaceAll("\n", "<br/>").toUpperCase() : "";
            }
        } else if (CommonUtils.isNotEmpty(knTrackingSystem)) {
            Object[] knTracking = (Object[]) knTrackingSystem.get(0);
            exportRefNo = (null != knTracking[0] && !knTracking[0].equals(""))
                    ? knTracking[0].toString().toUpperCase() : "";
            marksAndNo = (null != knTracking[1] && !knTracking[1].equals(""))
                    ? knTracking[1].toString().replaceAll("\n", "<br/>").toUpperCase() : "";
            commodityDesc = (null != knTracking[2] && !knTracking[2].equals(""))
                    ? knTracking[2].toString().replaceAll("\n", "<br/>").toUpperCase() : "";
        }
        request.setAttribute("exportRefNo", exportRefNo);
        request.setAttribute("marksAndNo", marksAndNo);
        request.setAttribute("commodityDesc", commodityDesc);
        request.setAttribute("fileNumberId", fileNumberId);
        return mapping.findForward("showLclEdiData");
    }

    public ActionForward updateReleaseConsolidatedDR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ajaxMessage = "";
        //  LclFileNumber lclFileNumber = null;
        Warehouse wareHouse = null;
        Date d = new Date();
        LCLBookingForm lclBookingForm = (LCLBookingForm) form;
        LclBookingExport lclBookingExport = null;
        User user = getCurrentUser(request);
        String unreleasefile = request.getParameter("consolidateFileId");
        List fileList = new ArrayList();
        if (CommonUtils.isNotEmpty(lclBookingForm.getUnPickedFiles())) {
            String[] ConsolidateId = lclBookingForm.getUnPickedFiles().split(",");
            fileList = Arrays.asList(ConsolidateId);
        } else if (request.getParameter("buttonName").equalsIgnoreCase("UnReleaseFromMainScreen")) {
            if (CommonUtils.isNotEmpty(unreleasefile)) {
                String[] unrelease = unreleasefile.split(",");
                fileList = Arrays.asList(unrelease);
            }
        } else {
            fileList = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(lclBookingForm.getFileNumberId()));
            fileList.add(Long.parseLong(lclBookingForm.getFileNumberId()));
        }
        for (Object fileId : fileList) {
            Long fId = Long.parseLong(fileId.toString());
            LclBooking booking = new LCLBookingDAO().getByProperty("lclFileNumber.id", (fId));
            lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", (fId));

            if (null == lclBookingExport) {
                lclBookingExport = new LclBookingExport();
                lclBookingExport.setEnteredBy(getCurrentUser(request));
                lclBookingExport.setEnteredDatetime(d);
            }
            if (null != booking.getPortOfOrigin() || null != booking.getPortOfDestination()) {
                RefTerminal terminal = null;
                if (null != booking.getPortOfOrigin()) {
                    terminal = new RefTerminalDAO().getTerminalByUnLocation(booking.getPortOfOrigin().getUnLocationCode(), "Y");
                } else {
                    terminal = new RefTerminalDAO().getTerminalByUnLocation(booking.getPortOfDestination().getUnLocationCode(), "Y");
                }
                wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
            }
            lclBookingExport.setFileNumberId(fId);
            lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
            lclBookingExport.setRtAgentAcct(null);
            lclBookingExport.setDeliverPickup("P");
            lclBookingExport.setDeliverPickupDatetime(d);
            if (request.getParameter("buttonName").equalsIgnoreCase("ReleaseFromMainScreen")) {
                lclBookingExport.setReleasedDatetime(d);
                new LclRemarksDAO().insertLclRemarks(fId, REMARKS_DR_AUTO_NOTES, "Cargo Released for Export", getCurrentUser(request).getUserId());
                new LclRemarksDAO().insertLclRemarks(fId, REMARKS_DR_AUTO_NOTES, "Inventory Status->WAREHOUSE(Released)", getCurrentUser(request).getUserId());
                ajaxMessage = "R";
            } else if (request.getParameter("buttonName").equalsIgnoreCase("UnReleaseFromMainScreen")) {
                lclBookingExport.setReleasedDatetime(null);
                new LclRemarksDAO().insertLclRemarks(fId, REMARKS_DR_AUTO_NOTES, "Cargo Un-Released", getCurrentUser(request).getUserId());
                ajaxMessage = "UR";
                if (lclBookingExport.getPrereleaseDatetime() != null) {
                    ajaxMessage = "URPR";
                }
            } else if (request.getParameter("buttonName").equalsIgnoreCase("R") && null == lclBookingExport.getReleasedDatetime()) {
                lclBookingExport.setReleasedDatetime(d);
                new LclRemarksDAO().insertLclRemarks(fId, REMARKS_DR_AUTO_NOTES, "Cargo Released for Export", getCurrentUser(request).getUserId());
                new LclRemarksDAO().insertLclRemarks(fId, REMARKS_DR_AUTO_NOTES, "Inventory Status->WAREHOUSE(Released)", getCurrentUser(request).getUserId());
                ajaxMessage = "R";
            } else if (request.getParameter("buttonName").equalsIgnoreCase("UR") && null != lclBookingExport.getReleasedDatetime()) {
                new LclRemarksDAO().insertLclRemarks(fId, REMARKS_DR_AUTO_NOTES, "Cargo UnReleased ", getCurrentUser(request).getUserId());
                lclBookingExport.setReleasedDatetime(null);
                ajaxMessage = "UR";
            } else if (request.getParameter("buttonName").equalsIgnoreCase("PR") && null == lclBookingExport.getPrereleaseDatetime()) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), "auto", "Cargo Pre-Released", getCurrentUser(request).getUserId());
                lclBookingExport.setPrereleaseDatetime(d);
                ajaxMessage = "PR";
            } else if (request.getParameter("buttonName").equalsIgnoreCase("PR") && null != lclBookingExport.getPrereleaseDatetime()) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(lclBookingForm.getFileNumberId()), "auto", "Cargo Un-PreReleased", getCurrentUser(request).getUserId());
                lclBookingExport.setPrereleaseDatetime(null);
                ajaxMessage = "UPR";
            }
            lclBookingExport.setReleaseUser(getCurrentUser(request));
            lclBookingExport.setModifiedBy(getCurrentUser(request));
            lclBookingExport.setModifiedDatetime(d);
            lclBookingExport.setAes(null != lclBookingForm.getAesBy() ? lclBookingForm.getAesBy() : false);
            new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
            new LCLBookingDAO().updateModifiedDateTime(fId, user.getUserId());
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(ajaxMessage);
        return null;
    }
// for Exports #mantis :13725

    public ActionForward deleteInlandForExports(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm bookingForm = (LCLBookingForm) form;
        String engmet = "";
        LclBookingPieceDAO bookingPieceDAO = new LclBookingPieceDAO();
        User loginUser = getCurrentUser(request);
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        Long fileId = Long.parseLong(bookingForm.getFileNumberId());
        LclCostChargeDAO costChargeDAO = new LclCostChargeDAO();
        LclBookingAc lclBookingAc = new LclCostChargeDAO().getLclBookingAcByChargeCode(fileId,
                LoadLogisoftProperties.getProperty("InlandChargeCode"));
        boolean costFlag = false;
        if (null != lclBookingAc && lclBookingAc.getArAmount().doubleValue() != 0.00
                && lclBookingAc.getApAmount().doubleValue() != 0.00) {
            StringBuilder remarks = new StringBuilder();
            String costStatus = new LCLBookingAcTransDAO().getTransType(lclBookingAc.getId());
            remarks.append("DELETED -> Code -> ").append(lclBookingAc.getArglMapping().getChargeCode());
            if (costStatus.equalsIgnoreCase("AC")) {
                if (lclBookingAc.getArglMapping().getChargeCode() != null) {
                    remarks.append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                }
                if (lclBookingAc.getControlNo() != 0) {
                    lclBookingAc.setApAmount(BigDecimal.ZERO);
                    lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                }
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setDeleted(Boolean.TRUE);
                lclBookingAc.setControlNo(lclBookingAc.getControlNo());
                new LclManifestDAO().deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                costChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                costFlag = true;
            }
            remarks.append(" Charge Amount -> ").append(lclBookingAc.getArAmount());
            lclBookingAc.setArAmount(BigDecimal.ZERO);
            lclBookingAc.setArBillToParty(null);
            lclBookingAc.setApBillToParty(null);
            lclBookingAc.setModifiedDatetime(new Date());
            if (lclBookingAc.getControlNo() == 0 && costFlag) {
                costChargeDAO.deleteChargesById(lclBookingAc.getId().toString());
            } else {
                costChargeDAO.saveOrUpdate(lclBookingAc);
            }
            lclRemarksDAO.insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
            lclRemarksDAO.getCurrentSession().clear();
        }
        String deliveryCargoToCode = request.getParameter("deliveryCargoToCode");
        String deliverCargoToName = request.getParameter("deliverCargoToName");
        String deliverCargoToAddress = request.getParameter("deliverCargoToAddress");
        String deliverCargoToCity = request.getParameter("deliverCargoToCity");
        String deliverCargoToState = request.getParameter("deliverCargoToState");
        String deliverCargoToZip = request.getParameter("deliverCargoToZip");
        String deliverCargoToPhone = request.getParameter("deliverCargoToPhone");
        LclBooking booking = bookingDAO.findById(fileId);
        bookingDAO.getCurrentSession().evict(booking);
        new LclBookingPadDAO().updateDeliveryContact(booking, deliverCargoToName, deliverCargoToAddress,
                deliverCargoToCity, deliverCargoToState, deliverCargoToZip, deliverCargoToPhone, deliveryCargoToCode, request);
        Ports ports = new PortsDAO().getByProperty("unLocationCode", booking.getFinalDestination().getUnLocationCode());
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            engmet = ports.getEngmet();
        }
        List<LclBookingPiece> bookingPieceList = bookingPieceDAO.findByProperty("lclFileNumber.id", fileId);

        List<LclBookingAc> chargeList = costChargeDAO.getLclCostByFileNumberAsc(fileId, "Exports");
        lclUtils.setWeighMeasureForBooking(request, bookingPieceList, null);
        lclUtils.setRolledUpChargesForBooking(chargeList, request, fileId, costChargeDAO,
                bookingPieceList, booking.getBillingType(), engmet, "No");
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward baseHotCodePopUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lCLBookingForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        session.removeAttribute("lclHotCodeList");
        session.removeAttribute("isPreHotCodeRemarks");
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileId", lCLBookingForm.getFileNumberId());
        return mapping.findForward("baseHotCode");
    }

    public ActionForward addORemoveBatchHotCode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lCLBookingForm = (LCLBookingForm) form;
        HttpSession session = request.getSession();
        String button = lCLBookingForm.getButton();
        List<LclBookingHotCode> batchHotCode = (List<LclBookingHotCode>) session.getAttribute("lclHotCodeList");
        String hotcodeComments = null != request.getParameter("hotCodeXXXComments") ? request.getParameter("hotCodeXXXComments") : "";
        if (button.equalsIgnoreCase("addBatchHotCodes")) {
            Long fileId = Long.parseLong(lCLBookingForm.getFileNumberId());
            if (batchHotCode == null) {
                batchHotCode = new ArrayList<LclBookingHotCode>();
            }
            LclBookingHotCode newBatchHotcode = new LclBookingHotCode();
            if (CommonUtils.isNotEmpty(lCLBookingForm.getBatchHotCode())) {
                newBatchHotcode.setCode(lCLBookingForm.getBatchHotCode());
                batchHotCode.add(newBatchHotcode);
                session.setAttribute("lclHotCodeList", batchHotCode);
                if (lCLBookingForm.getBatchHotCode().contains("XXX")) {
                    session.setAttribute("isPreHotCodeRemarks", hotcodeComments);
                }
            }

        } else if (button.equalsIgnoreCase("deleteBatchHotCode")) {
            String code = lCLBookingForm.getBatchHotCode();
            Iterator<LclBookingHotCode> hotcode = batchHotCode.iterator();
            List<LclBookingHotCode> newhotCodes = new ArrayList<LclBookingHotCode>();
            while (hotcode.hasNext()) {
                LclBookingHotCode value = hotcode.next();
                if (value.getCode().equalsIgnoreCase(code)) {
                    hotcode.remove();
                } else {
                    LclBookingHotCode newhotCode = new LclBookingHotCode();
                    newhotCode.setCode(value.getCode());
                    newhotCodes.add(newhotCode);
                }
            }
            if (code.contains("XXX")) {
                session.removeAttribute("isPreHotCodeRemarks");
            }
            session.removeAttribute("lclHotCodeList");
            session.setAttribute("lclHotCodeList", newhotCodes);
        } else if (button.equalsIgnoreCase("saveBatchHotCode")) {
            Long fileId = Long.parseLong(lCLBookingForm.getFileNumberId());
            List<Long> consolidateList = new LclConsolidateDAO().getConsolidatesFiles(fileId);
            consolidateList.add(fileId);
            String hotCodeXXXComments = request.getParameter("hotCodeXXXComments");
            String isPreHotCodeRemarks = (String) session.getAttribute("isPreHotCodeRemarks");
            if (CommonUtils.isNotEmpty(hotCodeXXXComments) || CommonUtils.isNotEmpty(isPreHotCodeRemarks)) {
                for (Object file : consolidateList) {
                    Long fileid = Long.parseLong(file.toString());
                    String mainhotcodeComments = CommonUtils.isNotEmpty(hotCodeXXXComments) ? hotCodeXXXComments : isPreHotCodeRemarks;
                    String hotCodeXXXCommentsSingle = "Added Hot Code XXX Comments-->" + mainhotcodeComments;
                    new LclRemarksDAO().insertLclRemarks(fileid, REMARKS_DR_AUTO_NOTES, hotCodeXXXCommentsSingle.toUpperCase(), getCurrentUser(request).getUserId());
                    LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", fileid);
                    if (null != bookingExport && null == bookingExport.getReleasedDatetime()) {
                        String desc = "Hold->Y,Added Hot Code XXX Comments--> " + mainhotcodeComments;
                        LclBooking booking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileid);
                        booking.setHold("Y");
                        new LCLBookingDAO().saveOrUpdate(booking);
                        new LclRemarksDAO().insertLclRemarks(fileid, REMARKS_ON_HOLD_NOTES, desc.toUpperCase(), getCurrentUser(request).getUserId());
                    }
                }
            }
            for (Object file : consolidateList) {
                for (LclBookingHotCode hotCode : batchHotCode) {
                    LclBookingHotCode newlclBookingHotCode = new LclBookingHotCode();
                    newlclBookingHotCode.setCode(hotCode.getCode());
                    newlclBookingHotCode.setLclFileNumber(new LclFileNumber(Long.parseLong(file.toString())));
                    newlclBookingHotCode.setEnteredBy(getCurrentUser(request));
                    newlclBookingHotCode.setModifiedBy(getCurrentUser(request));
                    newlclBookingHotCode.setEnteredDatetime(new Date());
                    newlclBookingHotCode.setModifiedDatetime(new Date());
                    new LclBookingHotCodeDAO().saveOrUpdate(newlclBookingHotCode);
                }

            }
            String[] remarkTypes = new String[]{REMARK_TYPE_MANUAL, REMARKS_DR_MANUAL_NOTES};
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            Boolean manualFlag = remarksDAO.isRemarks(fileId, remarkTypes);
            Boolean hotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(fileId, "XXX");
            manualFlag = !manualFlag ? hotCodeFlag : manualFlag;
            if (hotCodeFlag) {
                request.setAttribute("isHotCodeRemarks", remarksDAO.isRemarks(fileId, "ADDED HOT CODE XXX COMMENTS"));
            }
            request.setAttribute("hotCodeFlagIdNotes", manualFlag);
            List<LclBookingHotCode> newhotcode = new LclBookingHotCodeDAO().getHotCodeList(fileId);
            session.removeAttribute("lclHotCodeList");
            session.setAttribute("lclHotCodeList", newhotcode);
            return mapping.findForward("hoteCodes");
        } else if (button.equalsIgnoreCase("checkBatchHotCode")) {
            String ajaxMessage = "";
            boolean flag = false;
            if (batchHotCode != null) {
                String code = lCLBookingForm.getBatchHotCode();
                Iterator<LclBookingHotCode> hotCode = batchHotCode.iterator();
                while (hotCode.hasNext()) {
                    LclBookingHotCode value = hotCode.next();
                    if (CommonUtils.isNotEmpty(value.getCode()) && value.getCode().equalsIgnoreCase(code)) {
                        flag = true;
                    }
                }
            }
            if (!flag && null != lCLBookingForm.getFileNumberId()) {
                flag = new LclBookingHotCodeDAO().isHotCodeExistsForConsolidateFile(lCLBookingForm.getFileNumberId(), lCLBookingForm.getBatchHotCode());
            }
            if (lCLBookingForm.getBatchHotCode().contains("XXX")) {
                session.removeAttribute("isPreHotCodeRemarks");
            }
            ajaxMessage = flag ? "true" : "false";
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(ajaxMessage);
            return null;
        }
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileId", lCLBookingForm.getFileNumberId());
        return mapping.findForward("baseHotCode");
    }

    private void addSequenceNumber(LclBl bl, Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  lsh.`id`  ");
        sb.append(" FROM ");
        sb.append(" (SELECT lf.`id` AS fileId, lbu.`lcl_unit_ss_id` AS unitSsId FROM lcl_booking_piece bp  ");
        sb.append(" JOIN lcl_booking_piece_unit lbu ON lbu.`booking_piece_id` = bp.`id`  ");
        sb.append(" JOIN lcl_file_number lf ON lf.`id` = bp.`file_number_id` WHERE lf.id =:fileId) fn  ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id = fn.unitSsId ");
        sb.append(" JOIN lcl_ss_header lsh ON lsh.`id` = lus.`ss_header_id` AND lsh.`service_type` IN ('E' ,'C')  ");
        sb.append(" JOIN lcl_ss_detail lsd ON lsd.`ss_header_id` = lsh.`id` AND lsd.`id` =  ");
        sb.append(" (select ls.id from lcl_ss_detail ls where ls.ss_header_id = lsh.id order by ls.id desc limit 1) ");
        sb.append(" JOIN  lcl_unit lu ON lu.`id` =  lus.`unit_id` ");
        sb.append(" LEFT JOIN `lcl_unit_ss_manifest` lusm  ON lusm.`unit_id` = lu.`id` AND  lusm.`ss_header_id` = lsh.`id` ");
        sb.append(" LEFT JOIN user_details u ON lusm.`manifested_by_user_id` = u.`user_id` ");
        sb.append(" ORDER BY lsh.id DESC LIMIT 1 ");
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        BigInteger row = (BigInteger) query.uniqueResult();
        if (row != null) {
            if (CommonUtils.isNotEmpty(row.toString())) {
                Long headerId = Long.parseLong(row.toString());
                LclSsHeader ssHeader = new LclSsHeaderDAO().findById(headerId);
                if (null != ssHeader) {
                    if (CommonUtils.isNotEmpty(ssHeader.getSequenceNumber())) {
                        ssHeader.setSequenceNumber(ssHeader.getSequenceNumber() + 1);
                        bl.setSequenceNumber(ssHeader.getSequenceNumber());
                    } else {
                        ssHeader.setSequenceNumber(1);
                        bl.setSequenceNumber(1);
                    }
                    new LclSsHeaderDAO().update(ssHeader);
                }
            }
        }
    }

    public ActionForward openDispositionPopUp(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("dispositionPopUp");
    }

    public void saveDispositionPopUp(String fileId, String disposition, String cityCode, String whseCode, String user) throws Exception {
        new LclBookingDispoDAO().insertDisposition(Long.parseLong(fileId), !cityCode.isEmpty() ? Integer.parseInt(cityCode) : null,
                !whseCode.isEmpty() ? Integer.parseInt(whseCode) : null, !disposition.isEmpty() ? Integer.parseInt(disposition) : null, Integer.parseInt(user));

    }

    public String checkRoleDuty(String Origin, HttpServletRequest request) throws Exception {
        String val = "false";
        User loginUser = getCurrentUser(request);
        String terminalLocation = loginUser.getTerminal().getUnLocationCode1();
        RoleDuty roleDuty = checkRoleDuty(request);
        if (roleDuty.isDefaultDocsRcvd() && (terminalLocation.equals(Origin))) {
            val = "true";
        }
        return val;
    }

    public ActionForward changeBlFD(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLBookingForm lCLBookingForm = (LCLBookingForm) form;
        Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
        if (CommonUtils.isNotEmpty(fileId)) {
            LclBl lclBl = null;
            LCLBlDAO lclBlDAO = new LCLBlDAO();
            lclBl = lclBlDAO.findById(fileId);
            if (lclBl != null) {
                lclBl.setPortOfLoading(new UnLocationDAO().findById(lCLBookingForm.getPortOfLoadingId()));
                lclBl.setPortOfDestination(new UnLocationDAO().findById(lCLBookingForm.getPortOfDestinationId()));
                lclBl.setFinalDestination(new UnLocationDAO().findById(lCLBookingForm.getFinalDestinationId()));
                lclBlDAO.saveOrUpdate(lclBl);
            }
        }
        return null;
    }
}
