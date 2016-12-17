package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.ExportVoyageUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.lcl.model.LclModel;
import com.gp.cong.logisoft.beans.BookingUnitsBean;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsContact;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.logiware.accounting.dao.LclManifestDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.beans.StopoffsBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.LclSsExports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.FCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclExportsVoyageNotificationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsExportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitSsAutoCostingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.dao.LclExportManifestDAO;
import com.logiware.excel.ExportLclUnitsViewDRToExcel;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.lcl.dao.ExportNotificationDAO;
import com.logiware.thread.LclExportsVoyageNumberThread;
import com.oreilly.servlet.ServletUtils;
import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;

public class LclAddVoyageAction extends LogiwareDispatchAction {

    private static String DISPLAY_DETAIL_POPUP = "displayDetailPopup";
    private static String OPEN_UNIT = "openunits";
    private static String DISPLAY_SS_MASTER_DETAIL_POPUP = "displaySSMasterDetailPopup";
    private static String DISPLAY_ADD_MANIFESTTOBL_POPUP = "displayAddManifestToBLPopup";
    private static String DISPLAY_ADD_MANIFESTTOBL_STATUS = "displayAddManifestToBLStatus";
    private static String DISPLAY_NEW_VOYAGE = "displayNewVoyage";
    private static String DISPLAY_MANIFEST_POPUP = "displayManifestPopup";
    private static String ADD_EXCEPTION_DRS_POPUP = "addExceptionDRsPopup";
    private static String CHANGE_VOYAGE_POPUP = "changeVoyagePopup";
    private static String COB_POPUP = "openCOBPopup";
    private static String DISPLAY_SCHEDULE = "displaySchedule";
    private static String LCLEXPORT_VOYAGE = "LCLEXPORT-VOYAGE";

    public ActionForward editVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(addVoyageForm.getHeaderId())) {
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            User user = getCurrentUser(request);
            LclSsHeader lclssheader = lclssheaderdao.findById(addVoyageForm.getHeaderId());
            //Release the LOCK for Unit Stuffing Screen, this is applicable only for Export and Inland
            if (addVoyageForm.isReleaseLock()) {
                new DBUtil().releaseLockByRecordIdAndModuleId(lclssheader.getScheduleNo(), LCLEXPORT_VOYAGE, user.getUserId());
            }
            String remarks = "";
            if (addVoyageForm.getVoyageOwnerFlag() != null && "true".equalsIgnoreCase(addVoyageForm.getVoyageOwnerFlag())) {
                if (lclssheader.getOwnerUserId() != null && lclssheader.getOwnerUserId().getLoginName() != null) {
                    remarks = "Voyage Owner -> " + lclssheader.getOwnerUserId().getLoginName().toUpperCase() + " to " + addVoyageForm.getVoyageOwner().toUpperCase();
                } else {
                    remarks = "Voyage Owner -> " + addVoyageForm.getVoyageOwner().toUpperCase();
                }
                new LclSsRemarksDAO().insertLclSSRemarks(lclssheader.getId(), "auto", null, remarks, null, user.getUserId());
                lclssheader.setOwnerUserId(new UserDAO().findById(Integer.parseInt(addVoyageForm.getVoyenteredById())));
            }
            String eliteVoyNum = "";
            if (lclssheader.getEliteVoynum() != null) {
                if (lclssheader.getEliteVoynum().length() >= 8 && !lclssheader.getEliteVoynum().contains("-")) {
                    eliteVoyNum = lclssheader.getEliteVoynum().substring(0, 2) + "-" + lclssheader.getEliteVoynum().substring(2, 5) + "-" + lclssheader.getEliteVoynum().substring(5);
                } else {
                    eliteVoyNum = lclssheader.getEliteVoynum();
                }
                request.setAttribute("eliteVoyNum", eliteVoyNum);
            }
            // below function created for Stop off method to avoid repeated calling
            String buttonValue = addVoyageForm.getButtonValue() != null ? addVoyageForm.getButtonValue() : "";
            if (addVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic") && !buttonValue.equalsIgnoreCase("closeStopOff")) {
                setStopOffList(lclssheader, request);
            }
            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, addVoyageForm, getCurrentUser(request));
        }
        request.setAttribute("filterValues", addVoyageForm.getFilterByChanges());
        request.setAttribute("lclAddVoyageForm", addVoyageForm);
        request.setAttribute("goBackVoyage", request.getParameter("voyageNo"));
        request.setAttribute("goBackInland", request.getParameter("domesticInlandNo"));
        request.setAttribute("goBackCfcl", request.getParameter("cfclNo"));
        request.setAttribute("openPopup", "notopen");
        request.setAttribute("searchLoadDisplay", addVoyageForm.getSearchLoadDisplay());
        return mapping.findForward("displayNewVoyage");
    }

    public ActionForward copyVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUtils lclUtils = new LclUtils();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
            LclSsDetail lclssdetail = lclssdetaildao.findById(lclAddVoyageForm.getDetailId());
            if (CommonUtils.isNotEmpty(lclssdetail.getTransMode())) {
                lclAddVoyageForm.setTransMode(lclssdetail.getTransMode());
            }
            if (lclssdetail.getDeparture() != null) {
                lclAddVoyageForm.setDeparturePier(lclUtils.getConcatenatedOriginByUnlocation(lclssdetail.getDeparture()));
                lclAddVoyageForm.setDepartureId(lclssdetail.getDeparture().getId());
            }
            if (lclssdetail.getArrival() != null) {
                lclAddVoyageForm.setArrivalPier(lclUtils.getConcatenatedOriginByUnlocation(lclssdetail.getArrival()));
                lclAddVoyageForm.setArrivalId(lclssdetail.getArrival().getId());
            }
            if (lclssdetail.getStd() != null) {
                lclAddVoyageForm.setStd(DateUtils.formatDate(lclssdetail.getStd(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getSta() != null) {
                lclAddVoyageForm.setEtaPod(DateUtils.formatDate(lclssdetail.getSta(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getSpReferenceNo())) {
                lclAddVoyageForm.setSpReferenceNo(lclssdetail.getSpReferenceNo());
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getSpReferenceName())) {
                lclAddVoyageForm.setSpReferenceName(lclssdetail.getSpReferenceName());
            }
            if (lclssdetail.getSpAcctNo() != null) {
                if (lclssdetail.getSpAcctNo().getAccountno() != null && !lclssdetail.getSpAcctNo().getAccountno().trim().equals("")) {
                    lclAddVoyageForm.setAccountNumber(lclssdetail.getSpAcctNo().getAccountno());
                }
                if (lclssdetail.getSpAcctNo().getAccountName() != null && !lclssdetail.getSpAcctNo().getAccountName().trim().equals("")) {
                    lclAddVoyageForm.setAccountName(lclssdetail.getSpAcctNo().getAccountName());
                }
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getRelayTtOverride())) {
                lclAddVoyageForm.setTtOverrideDays(lclssdetail.getRelayTtOverride());
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getRelayLrdOverride())) {
                lclAddVoyageForm.setLrdOverrideDays(lclssdetail.getRelayLrdOverride());
            }
            if (lclssdetail.getGeneralLrdt() != null) {
                lclAddVoyageForm.setLoadLrdt(DateUtils.formatDate(lclssdetail.getGeneralLrdt(), "dd-MMM-yyyy hh:mm"));
            }
            if (lclssdetail.getHazmatLrdt() != null) {
                lclAddVoyageForm.setHazmatLrdt(DateUtils.formatDate(lclssdetail.getHazmatLrdt(), "dd-MMM-yyyy hh:mm"));
            }
            if (lclssdetail.getAtd() != null) {
                lclAddVoyageForm.setSslDocsCutoffDate(DateUtils.formatDate(lclssdetail.getAtd(), "dd-MMM-yyyy"));
            }
            LclSsHeader lclSsHeader = lclssdetail.getLclSsHeader();
            String origin = lclUtils.getConcatenatedOriginByUnlocation(lclSsHeader.getOrigin());
            String destination = lclUtils.getConcatenatedOriginByUnlocation(lclSsHeader.getDestination());
            request.setAttribute("serviceType", lclSsHeader.getServiceType());
            request.setAttribute("originalOriginName", origin);
            request.setAttribute("originalDestinationName", destination);
            request.setAttribute("pooId", lclSsHeader.getOrigin().getId());
            request.setAttribute("fdId", lclSsHeader.getDestination().getId());
            request.setAttribute("polId", lclSsHeader.getOrigin().getId());
            request.setAttribute("podId", lclSsHeader.getDestination().getId());
            request.setAttribute("originValue", origin);
            request.setAttribute("destinationValue", destination);
            if (CommonUtils.isNotEmpty(lclSsHeader.getOrigin().getId())
                    && CommonUtils.isNotEmpty(lclSsHeader.getDestination().getId())) {
                lclUtils.setRelayTTDBD(request, lclSsHeader.getOrigin().getId(),
                        lclSsHeader.getDestination().getId());
            }
        }
        request.setAttribute("filterValues", lclAddVoyageForm.getFilterByChanges());
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("openPopup", "openPopup");
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward editVoyageHeader(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            lclAddVoyageForm.setScheduleNo(lclssheader.getScheduleNo());
            if (lclssheader.getHazmatPermitted()) {
                lclAddVoyageForm.setHazmatPermitted("Y");
            } else {
                lclAddVoyageForm.setHazmatPermitted("N");
            }
            if (lclssheader.getRefrigerationPermitted()) {
                lclAddVoyageForm.setRefrigerationPermitted("Y");
            } else {
                lclAddVoyageForm.setRefrigerationPermitted("N");
            }
            if (CommonUtils.isNotEmpty(lclssheader.getRemarks())) {
                lclAddVoyageForm.setRemarks(lclssheader.getRemarks());
            }
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("displayHeaderPopup");
    }

    public ActionForward saveVoyageHeader(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        User loginUser = getCurrentUser(request);
        LclSsHeader lclssheader = null;
        Date d = new Date();
        if (CommonUtils.isNotEmpty(addVoyageForm.getHeaderId())) {
            lclssheader = lclssheaderdao.findById(addVoyageForm.getHeaderId());
        }
        if (CommonUtils.isNotEmpty(addVoyageForm.getHazmatPermitted())) {
            if (addVoyageForm.getHazmatPermitted().equalsIgnoreCase("Y")) {
                lclssheader.setHazmatPermitted(true);
                lclssheader.setDisplayhazmatPermitted("Yes");
            } else {
                lclssheader.setHazmatPermitted(false);
                lclssheader.setDisplayhazmatPermitted("No");
            }
        }
        if (CommonUtils.isNotEmpty(addVoyageForm.getHiddenRefrigerationPermitted())) {
            if (addVoyageForm.getHiddenRefrigerationPermitted().equalsIgnoreCase("Y")) {
                lclssheader.setRefrigerationPermitted(true);
                lclssheader.setDisplayrefrigerationPermitted("Yes");
            } else {
                lclssheader.setRefrigerationPermitted(false);
                lclssheader.setDisplayrefrigerationPermitted("No");
            }
        }
        if (CommonUtils.isNotEmpty(addVoyageForm.getRemarks())) {
            lclssheader.setRemarks(addVoyageForm.getRemarks().toUpperCase());
        } else {
            lclssheader.setRemarks("");
        }
        lclssheader.setScheduleNo(addVoyageForm.getSchedule());
        lclssheader.setModifiedBy(loginUser);
        lclssheader.setModifiedDatetime(d);
        lclssheaderdao.saveOrUpdate(lclssheader);
        clearHeaderValues(addVoyageForm);
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, addVoyageForm, loginUser);
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward saveVoyageDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSsHeaderDAO ssHeaderDAO = new LclSsHeaderDAO();
        synchronized (LclAddVoyageAction.class) {
            LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
            User loginUser = getCurrentUser(request);
            Date now = new Date();
            LclSsExports lclSsExports = null;
            UnLocationDAO unlocationdao = new UnLocationDAO();
            TradingPartnerDAO tradingPartner = new TradingPartnerDAO();
            LclSsHeader ssHeader = ssHeaderDAO.findById(addVoyageForm.getHeaderId());
            if (ssHeader == null) {
                ssHeader = new LclSsHeader();
            }
            if (CommonUtils.isEmpty(addVoyageForm.getHeaderId())) {
                if (CommonUtils.isNotEmpty(addVoyageForm.getOriginalOriginId())) {
                    ssHeader.setOrigin(unlocationdao.findById(addVoyageForm.getOriginalOriginId()));
                }
                if (CommonUtils.isNotEmpty(addVoyageForm.getOriginalDestinationId())) {
                    ssHeader.setDestination(unlocationdao.findById(addVoyageForm.getOriginalDestinationId()));
                }
                if (addVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")) {
                    ssHeader.setServiceType("N");
                    ssHeader.setTransMode("T");
                } else if (addVoyageForm.getFilterByChanges().equalsIgnoreCase("lclExport")) {
                    ssHeader.setServiceType("E");
                    ssHeader.setTransMode("V");
                } else if (addVoyageForm.getFilterByChanges().equalsIgnoreCase("lclCfcl")) {
                    ssHeader.setServiceType("C");
                    ssHeader.setTransMode("V");
                }
                ssHeader.setStatus("A");
                ssHeader.setDatasource("L");
                ssHeader.setEnteredBy(loginUser);
                ssHeader.setEnteredDatetime(now);
                ssHeader.setHazmatPermitted(true);
                ssHeader.setDisplayhazmatPermitted("Yes");
                ssHeader.setRefrigerationPermitted(false);
                ssHeader.setDisplayrefrigerationPermitted("No");
                ssHeader.setModifiedBy(loginUser);
                ssHeader.setModifiedDatetime(now);
                ssHeader.setOwnerUserId(loginUser);
                String voyageNumber = "";
                if (CommonUtils.isEmpty(ssHeader.getScheduleNo())) {
                    LclExportsVoyageNumberThread thread = new LclExportsVoyageNumberThread();
                    if ("lclExport".equals(addVoyageForm.getFilterByChanges()) || "lclCfcl".equals(addVoyageForm.getFilterByChanges())) {
                        voyageNumber = thread.getVoyageNumber("LCL Voyage Exports");
                    } else {
                        voyageNumber = thread.getVoyageNumber("LCL Voyage Exports Inland");
                    }
                } else {
                    voyageNumber = addVoyageForm.getLclssheader().getScheduleNo();
                }
                ssHeader.setScheduleNo(voyageNumber);
                ssHeaderDAO.save(ssHeader);
            }
            LclSsExportsDAO ssExportsDAO = new LclSsExportsDAO();
            // add details for lcl Exports
            lclSsExports = ssExportsDAO.findById(addVoyageForm.getHeaderId());
            if (lclSsExports == null) {
                lclSsExports = new LclSsExports();
                lclSsExports.setEnteredUserId(loginUser);
                lclSsExports.setEnteredDatetime(now);
            }
            if (CommonUtils.isNotEmpty(ssHeader.getId())) {
                lclSsExports.setSsHeaderId(ssHeader.getId());
                lclSsExports.setLclSsHeader(ssHeader);
            }
            lclSsExports.setLandCarrierAcctNo(addVoyageForm.getLandCarrierAcountNumber() != null ? tradingPartner.findById(addVoyageForm.getLandCarrierAcountNumber()) : null);
            lclSsExports.setLandExitCity(!addVoyageForm.getLandExitCityUnlocCode().equalsIgnoreCase("") ? unlocationdao.findById(Integer.parseInt(addVoyageForm.getLandExitCityUnlocCode())) : null);
            lclSsExports.setLandExitDate(addVoyageForm.getLandExitDate() != null ? DateUtils.parseDate(addVoyageForm.getLandExitDate(), "dd-MMM-yyyy") : null);
            lclSsExports.setExportAgentAcctoNo(addVoyageForm.getExportAgentAcctNo() != null ? tradingPartner.findById(addVoyageForm.getExportAgentAcctNo()) : null);
            lclSsExports.setPrintViaMasterbl(addVoyageForm.isPrintViaMasterBl());
            lclSsExports.setLclVoyageLevelBrand(addVoyageForm.getAgentBrandName());
            lclSsExports.setModifiedUserId(loginUser);
            lclSsExports.setModifiedDatetime(now);
            ssExportsDAO.saveOrUpdate(lclSsExports);

            if (addVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")
                    && CommonUtils.isEmpty(addVoyageForm.getDetailId())) {
                saveStopOffDetails(addVoyageForm, ssHeader, request);//adding Stop-offs option only for inland Voyage
                if (CommonUtils.isNotEmpty(addVoyageForm.getUnitNo())) {
                    saveUnitsForInlandVoyage(addVoyageForm, request, ssHeader);
                }
            } else {
                LclSsDetail lclssdetail = addLclSsDetails(addVoyageForm, request);
                saveExportsAndInlandVoyage(addVoyageForm, lclssdetail, ssHeader);
            }
            new ExportVoyageUtils().setVoyageRequestVal(request, ssHeader, addVoyageForm, loginUser);
            request.setAttribute("openPopup", "notopen");
        }
        if (!ssHeaderDAO.getSession().getTransaction().isActive()) {
            ssHeaderDAO.getSession().getTransaction().begin();
        }
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public LclSsDetail addLclSsDetails(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request) throws Exception {
        LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
        LclSsDetail lclssdetail = new LclSsDetail();
        Date d = new Date();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            lclssdetail = lclssdetaildao.findById(lclAddVoyageForm.getDetailId());
        } else {
            lclssdetail = new LclSsDetail();
            lclssdetail.setEnteredBy(getCurrentUser(request));
            lclssdetail.setEnteredDatetime(d);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getEtaPod())) {
            lclssdetail.setSta(DateUtils.parseDate(lclAddVoyageForm.getEtaPod(), "dd-MMM-yyyy"));
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getTransMode())) {
            lclssdetail.setTransMode(lclAddVoyageForm.getTransMode());
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStd())) {
            lclssdetail.setStd(DateUtils.parseDate(lclAddVoyageForm.getStd(), "dd-MMM-yyyy"));
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSpReferenceNo())) {
            lclssdetail.setSpReferenceNo(lclAddVoyageForm.getSpReferenceNo().toUpperCase());
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSpReferenceName())) {
            lclssdetail.setSpReferenceName(lclAddVoyageForm.getSpReferenceName());
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getTtOverrideDays())) {
            lclssdetail.setRelayTtOverride(lclAddVoyageForm.getTtOverrideDays());
        } else {
            lclssdetail.setRelayTtOverride(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getLrdOverrideDays())) {
            lclssdetail.setRelayLrdOverride(lclAddVoyageForm.getLrdOverrideDays());
        } else {
            lclssdetail.setRelayLrdOverride(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getLoadLrdt())) {
            lclssdetail.setGeneralLrdt(DateUtils.parseDate(lclAddVoyageForm.getLoadLrdt(), "dd-MMM-yyyy"));
        } else {
            lclssdetail.setGeneralLrdt(null);
        }

        lclssdetail.setGeneralLoadingDeadline(CommonUtils.isNotEmpty(lclAddVoyageForm.getGeneralLoadingDeadline())
                ? DateUtils.parseDate(lclAddVoyageForm.getGeneralLoadingDeadline(), "dd-MMM-yyyy") : null);
        lclssdetail.setHazmatLoadingDeadline(CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatLoadingDeadline())
                ? DateUtils.parseDate(lclAddVoyageForm.getHazmatLoadingDeadline(), "dd-MMM-yyyy") : null);

        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatLrdt())) {
            lclssdetail.setHazmatLrdt(DateUtils.parseDate(lclAddVoyageForm.getHazmatLrdt(), "dd-MMM-yyyy"));
        } else {
            lclssdetail.setHazmatLrdt(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getRemarks())) {
            lclssdetail.setRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSslDocsCutoffDate())) {
            lclssdetail.setAtd(DateUtils.parseDate(lclAddVoyageForm.getSslDocsCutoffDate(), "dd-MMM-yyyy"));
        }
        lclssdetail.setModifiedBy(getCurrentUser(request));
        lclssdetail.setModifiedDatetime(d);

        return lclssdetail;
    }

    public void saveExportsAndInlandVoyage(LclAddVoyageForm lclAddVoyageForm,
            LclSsDetail lclssdetail, LclSsHeader lclssheader) throws Exception {
        UnLocationDAO unlocationdao = new UnLocationDAO();
        LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDepartureId()) && lclAddVoyageForm.getDepartureId() != null
                    && lclssdetail.getDeparture() != null
                    && lclAddVoyageForm.getDepartureId().intValue() != lclssdetail.getDeparture().getId().intValue()) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getDepartureId());
                lclssdetail.setDeparture(unlocation);
            } else if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDepartureId()) && lclssdetail.getDeparture() == null) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getDepartureId());
                lclssdetail.setDeparture(unlocation);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getArrivalId()) && lclAddVoyageForm.getArrivalId() != null
                    && lclssdetail.getArrival() != null && lclAddVoyageForm.getArrivalId().intValue() != lclssdetail.getArrival().getId().intValue()) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getArrivalId());
                lclssdetail.setArrival(unlocation);
            } else if (CommonUtils.isNotEmpty(lclAddVoyageForm.getArrivalId()) && lclssdetail.getArrival() == null) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getArrivalId());
                lclssdetail.setArrival(unlocation);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAccountNumber()) && lclssdetail.getSpAcctNo() != null
                    && !lclAddVoyageForm.getAccountNumber().equals(lclssdetail.getSpAcctNo().getAccountno())) {
                lclssdetail.setSpAcctNo(new TradingPartnerDAO().findById(lclAddVoyageForm.getAccountNumber()));
            } else if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAccountNumber()) && lclssdetail.getSpAcctNo() == null) {
                lclssdetail.setSpAcctNo(new TradingPartnerDAO().findById(lclAddVoyageForm.getAccountNumber()));
            }
        } else {
            lclssdetail.setLclSsHeader(lclssheader);
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDepartureId())) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getDepartureId());
                lclssdetail.setDeparture(unlocation);
            } else {
                lclssdetail.setDeparture(lclAddVoyageForm.getOriginId() != null ? new UnLocationDAO().findById(lclAddVoyageForm.getOriginId()) : null);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getArrivalId())) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getArrivalId());
                lclssdetail.setArrival(unlocation);
            } else {
                lclssdetail.setArrival(lclAddVoyageForm.getFinalDestinationId() != null ? new UnLocationDAO().findById(lclAddVoyageForm.getFinalDestinationId()) : null);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAccountNumber())) {
                lclssdetail.setSpAcctNo(new TradingPartnerDAO().findById(lclAddVoyageForm.getAccountNumber()));
            }
        }
        lclssdetaildao.saveOrUpdate(lclssdetail);
    }

    //adding Stop-offs option only for inland Voyage
    public void saveStopOffDetails(LclAddVoyageForm lclAddVoyageForm,
            LclSsHeader lclssheader, HttpServletRequest request) throws Exception {
        UnLocationDAO unlocationdao = new UnLocationDAO();
        LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<StopoffsBean> stopOffList = lclSession.getStopOffList();
        UnLocation departure = null;
        UnLocation arrival = null;
        LclSsDetail lclssdetail = null;
        if (CommonUtils.isNotEmpty(stopOffList)) {
            for (int i = 0; i <= stopOffList.size(); i++) {
                if (i == 0) {
                    lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
                    lclssdetail.setLclSsHeader(lclssheader);
                    arrival = unlocationdao.findById(stopOffList.get(i).getUnlocationId());
                    lclssdetail.setArrival(arrival);
                    departure = unlocationdao.findById(lclssheader.getOrigin().getId());
                    lclssdetail.setDeparture(departure);
                    String unloctionCode = lclAddVoyageForm.getPooOrigin().substring(lclAddVoyageForm.getPooOrigin().indexOf("(") + 1, lclAddVoyageForm.getPooOrigin().indexOf(")"));
                    RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unloctionCode, "Y");
                    Warehouse wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
                    lclssdetail.setStuffingWarehouse(wareHouse != null ? wareHouse : null);
                    lclssdetail.setSta(stopOffList.get(i).getStdDate());
                } else if (stopOffList.size() == i) {
                    lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
                    lclssdetail.setLclSsHeader(lclssheader);
                    departure = unlocationdao.findById(stopOffList.get(i - 1).getUnlocationId());
                    lclssdetail.setDeparture(departure);
                    arrival = unlocationdao.findById(lclssheader.getDestination().getId());
                    lclssdetail.setArrival(arrival);
                    lclssdetail.setStuffingWarehouse(new WarehouseDAO().findById(stopOffList.get(i - 1).getWarehouseId().intValue()));
                    lclssdetail.setStd(stopOffList.get(i - 1).getStdDate());
                    lclssdetail.setSta(stopOffList.get(i - 1).getStaDate());
                    lclssdetail.setRemarks(stopOffList.get(i - 1).getRemarks());
                } else {
                    lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
                    lclssdetail.setLclSsHeader(lclssheader);
                    departure = unlocationdao.findById(stopOffList.get(i - 1).getUnlocationId());
                    lclssdetail.setDeparture(departure);
                    arrival = unlocationdao.findById(stopOffList.get(i).getUnlocationId());
                    lclssdetail.setArrival(arrival);
                    lclssdetail.setStuffingWarehouse(new WarehouseDAO().findById(stopOffList.get(i - 1).getWarehouseId().intValue()));
                    lclssdetail.setStd(stopOffList.get(i - 1).getStdDate());
                    lclssdetail.setSta(stopOffList.get(i - 1).getStaDate());
                    lclssdetail.setRemarks(stopOffList.get(i - 1).getRemarks());
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAccountNumber())) {
                    lclssdetail.setSpAcctNo(new TradingPartnerDAO().findById(lclAddVoyageForm.getAccountNumber()));
                }
                lclssdetaildao.saveOrUpdate(lclssdetail);
            }
        } else {
            lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
            saveExportsAndInlandVoyage(lclAddVoyageForm, lclssdetail, lclssheader);//when stop-offs entry is empty
        }
    }

    public ActionForward deleteVoyageDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId()) && CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSsDetail lclssdetail = lclssdetaildao.findById(lclAddVoyageForm.getDetailId());
            if (lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")) {
                deleteInlandVoyageDetails(lclAddVoyageForm, lclssdetail, request);
            } else {
                lclssdetaildao.delete(lclssdetail);
            }
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            setStopOffList(lclssheader, request);
            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, getCurrentUser(request));
        }
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public void deleteInlandVoyageDetails(LclAddVoyageForm lclAddVoyageForm,
            LclSsDetail lclssdetail, HttpServletRequest request) throws Exception {
        LclSsDetail lclDetail = null;
        LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
        List<LclSsDetail> lcldetailList = lclssdetaildao.getLclDetailList(lclAddVoyageForm.getHeaderId());
        if (lcldetailList != null && lcldetailList.size() > 1) {
            for (int i = 0; i < lcldetailList.size(); i++) {
                if (lclssdetail.getId() == lcldetailList.get(i).getId()) {
                    if (i == 0) {
                        lclDetail = lclssdetaildao.findById(lcldetailList.get(i + 1).getId());
                        lclDetail.setDeparture(lclssdetail != null ? lclssdetail.getDeparture() : null);
                        lclDetail.setDetailStatus("Open");
                        lclssdetaildao.delete(lclssdetail);
                        lclssdetaildao.saveOrUpdate(lclDetail);
                    } else {
                        lclDetail = lclssdetaildao.findById(lcldetailList.get(i - 1).getId());
                        lclDetail.setArrival(lclssdetail != null ? lclssdetail.getArrival() : null);
                        lclDetail.setDetailStatus("Open");
                        lclssdetaildao.delete(lclssdetail);
                        lclssdetaildao.saveOrUpdate(lclDetail);
                    }
                }
            }
        } else {
            lclssdetaildao.delete(lclssdetail);
        }
    }

    public ActionForward editVoyageDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUtils lclUtils = new LclUtils();
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
            LclSsDetail lclssdetail = lclssdetaildao.findById(lclAddVoyageForm.getDetailId());
            if (CommonUtils.isNotEmpty(lclssdetail.getTransMode())) {
                lclAddVoyageForm.setTransMode(lclssdetail.getTransMode());
            }
            lclAddVoyageForm.setFdUnlocationCode(lclssdetail.getLclSsHeader().getDestination().getUnLocationCode());
            if (lclssdetail.getDeparture() != null) {
                lclAddVoyageForm.setDeparturePier(lclUtils.getConcatenatedOriginByUnlocation(lclssdetail.getDeparture()));
                lclAddVoyageForm.setDepartureId(lclssdetail.getDeparture().getId());
            }
            if (lclssdetail.getArrival() != null) {
                lclAddVoyageForm.setArrivalPier(lclUtils.getConcatenatedOriginByUnlocation(lclssdetail.getArrival()));
                lclAddVoyageForm.setArrivalId(lclssdetail.getArrival().getId());
            }
            if (lclssdetail.getStd() != null) {
                lclAddVoyageForm.setStd(DateUtils.formatDate(lclssdetail.getStd(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getSta() != null) {
                lclAddVoyageForm.setEtaPod(DateUtils.formatDate(lclssdetail.getSta(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getSpReferenceNo())) {
                lclAddVoyageForm.setSpReferenceNo(lclssdetail.getSpReferenceNo());
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getSpReferenceName())) {
                lclAddVoyageForm.setSpReferenceName(lclssdetail.getSpReferenceName());
                request.setAttribute("ImoNumber", genericCodeDAO.findByCodeDesc(lclAddVoyageForm.getSpReferenceName()));
            }
            if (lclssdetail.getSpAcctNo() != null) {
                if (lclssdetail.getSpAcctNo().getAccountno() != null && !lclssdetail.getSpAcctNo().getAccountno().trim().equals("")) {
                    lclAddVoyageForm.setAccountNumber(lclssdetail.getSpAcctNo().getAccountno());
                }
                if (lclssdetail.getSpAcctNo().getAccountName() != null && !lclssdetail.getSpAcctNo().getAccountName().trim().equals("")) {
                    lclAddVoyageForm.setAccountName(lclssdetail.getSpAcctNo().getAccountName());
                }
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getRelayTtOverride())) {
                lclAddVoyageForm.setTtOverrideDays(lclssdetail.getRelayTtOverride());
            }
            if (CommonUtils.isNotEmpty(lclssdetail.getRelayLrdOverride())) {
                lclAddVoyageForm.setLrdOverrideDays(lclssdetail.getRelayLrdOverride());
            }
            if (lclssdetail.getGeneralLrdt() != null) {
                lclAddVoyageForm.setLoadLrdt(DateUtils.formatDate(lclssdetail.getGeneralLrdt(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getHazmatLrdt() != null) {
                lclAddVoyageForm.setHazmatLrdt(DateUtils.formatDate(lclssdetail.getHazmatLrdt(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getGeneralLoadingDeadline() != null) {
                lclAddVoyageForm.setGeneralLoadingDeadline(DateUtils.formatDate(lclssdetail.getGeneralLoadingDeadline(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getHazmatLoadingDeadline() != null) {
                lclAddVoyageForm.setHazmatLoadingDeadline(DateUtils.formatDate(lclssdetail.getHazmatLoadingDeadline(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getRemarks() != null) {
                lclAddVoyageForm.setRemarks(lclssdetail.getRemarks());
            }
            if (lclssdetail.getAtd() != null) {
                lclAddVoyageForm.setSslDocsCutoffDate(DateUtils.formatDate(lclssdetail.getAtd(), "dd-MMM-yyyy"));
            }
            if (lclssdetail.getLclSsHeader() != null) {
                LclSsExports lclSsExport = new LclSsExportsDAO().findById(lclssdetail.getLclSsHeader().getId());
                String companyCodeValue = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                request.setAttribute("companyCodeValue", companyCodeValue);
                if (lclSsExport != null) {
                    lclAddVoyageForm.setLandCarrier(lclSsExport.getLandCarrierAcctNo() != null ? lclSsExport.getLandCarrierAcctNo().getAccountName() : "");
                    lclAddVoyageForm.setLandCarrierAcountNumber(lclSsExport.getLandCarrierAcctNo() != null ? lclSsExport.getLandCarrierAcctNo().getAccountno() : "");
                    lclAddVoyageForm.setLandExitCity(lclSsExport.getLandExitCity() != null ? lclSsExport.getLandExitCity().getUnLocationName() : "");
                    lclAddVoyageForm.setLandExitCityUnlocCode((lclSsExport.getLandExitCity() != null ? lclSsExport.getLandExitCity().getId().toString() : ""));
                    lclAddVoyageForm.setExportAgentAcctName(lclSsExport.getExportAgentAcctoNo() != null ? lclSsExport.getExportAgentAcctoNo().getAccountName() : "");
                    lclAddVoyageForm.setExportAgentAcctNo(lclSsExport.getExportAgentAcctoNo() != null ? lclSsExport.getExportAgentAcctoNo().getAccountno() : "");
                    lclAddVoyageForm.setLandExitDate(lclSsExport.getLandExitDate() != null ? DateUtils.formatDate(lclSsExport.getLandExitDate(), "dd-MMM-yyyy") : "");
                    lclAddVoyageForm.setPrintViaMasterBl(lclSsExport.isPrintViaMasterbl());
                    lclAddVoyageForm.setAgentBrandName(lclSsExport.getLclVoyageLevelBrand() != null ? lclSsExport.getLclVoyageLevelBrand() : new AgencyInfoDAO().getAgentLevelBrand(lclAddVoyageForm.getExportAgentAcctNo(), lclAddVoyageForm.getFdUnlocationCode()));
                }
            }
            request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        }
        request.setAttribute("filterByChanges", lclAddVoyageForm.getFilterByChanges());
        return mapping.findForward(DISPLAY_DETAIL_POPUP);
    }

    public ActionForward saveUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO ssHeaderDAO = new LclSsHeaderDAO();
        Date now = new Date();
        LclSsHeader lclssheader = ssHeaderDAO.findById(lclAddVoyageForm.getHeaderId());
        lclssheader.setModifiedDatetime(now);
        ssHeaderDAO.saveOrUpdate(lclssheader);
        User loginUser = getCurrentUser(request);
        LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
        LclUnitDAO unitDAO = new LclUnitDAO();

        LclUnit lclUnit = unitDAO.findById(lclAddVoyageForm.getUnitId());
        if (lclUnit == null) {
            lclUnit = new LclUnit();
            lclUnit.setEnteredBy(loginUser);
            lclUnit.setEnteredDatetime(now);
        }
        UnitType unittype = new UnitTypeDAO().findById(lclAddVoyageForm.getUnitType());
        lclUnit.setUnitType(unittype);
        lclUnit.setUnitNo(lclAddVoyageForm.getUnitNo().toUpperCase());
        lclUnit.setModifiedDatetime(now);
        lclUnit.setModifiedBy(loginUser);
        lclUnit.setHazmatPermitted("Y".equalsIgnoreCase(lclAddVoyageForm.getHazmatPermittedUnit()) ? true : false);
        lclUnit.setRefrigerated("Y".equalsIgnoreCase(lclAddVoyageForm.getRefrigerationPermittedUnit()) ? true : false);
        lclUnit.setRemarks(CommonUtils.isNotEmpty(lclAddVoyageForm.getRemarks())
                ? lclAddVoyageForm.getRemarks().toUpperCase() : null);
        lclUnit.setComments(CommonUtils.isNotEmpty(lclAddVoyageForm.getComments())
                ? lclAddVoyageForm.getComments().toUpperCase() : "");
        lclUnit.setTareWeightImperial(new BigDecimal(lclAddVoyageForm.getCargoWeightLbs()));
        lclUnit.setTareWeightMetric(new BigDecimal(lclAddVoyageForm.getCargoWeightKgs()));
        unitDAO.saveOrUpdate(lclUnit);
        Long ssDetailId = new LclSsDetailDAO().getIdbyAsc(lclssheader.getId());

        LclUnitWhseDAO unitWhseDAO = new LclUnitWhseDAO();
        unitWhseDAO.deleteWarehouseId(lclUnit.getId());
        LclUnitSsDispoDAO unitSsDispoDAO = new LclUnitSsDispoDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitsReopened())
                && "Y".equalsIgnoreCase(lclAddVoyageForm.getUnitsReopened())
                && CommonUtils.isNotEmpty(lclAddVoyageForm.getOldUnitId()) && lclUnit != null) {
            Long oldUnitId = lclAddVoyageForm.getOldUnitId();
            LclUnit oldUnit = unitDAO.findById(lclAddVoyageForm.getOldUnitId());
            LclUnitSsRemarksDAO unitSsRemarksDAO = new LclUnitSsRemarksDAO();
            StringBuilder remarks = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitNo()) && !oldUnit.getUnitNo().equalsIgnoreCase(lclAddVoyageForm.getUnitNo())) {
                remarks.append("Unit# changed from ").append(oldUnit.getUnitNo()).append(" to ").append(lclAddVoyageForm.getUnitNo().toUpperCase()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitType()) && !oldUnit.getUnitType().getId().equals(lclAddVoyageForm.getUnitType())) {
                remarks.append("Size -> ").append(oldUnit.getUnitType().getDescription()).append(" to ").append(unittype.getDescription()).append(" ");
            }
            if (CommonUtils.isNotEmpty(remarks)) {
                unitSsRemarksDAO.insertLclunitRemarks(lclssheader.getId(), lclUnit.getId(), "auto", remarks.toString(), loginUser.getUserId());
            }
            unitSsDAO.updateUnitId(lclssheader.getId(), oldUnitId, lclUnit.getId(), loginUser.getUserId());
            unitSsRemarksDAO.updateUnitId(lclssheader.getId(), oldUnitId, lclUnit.getId());
            unitWhseDAO.updateUnitId(lclssheader.getId(), oldUnitId, lclUnit.getId(), loginUser.getUserId());
            unitSsDispoDAO.updateUnitId(ssDetailId, oldUnitId, lclUnit.getId(), loginUser.getUserId());
            new LclUnitSsManifestDAO().updateUnitId(lclssheader.getId(), oldUnitId, lclUnit.getId(), loginUser.getUserId());

            List<ManifestBean> pickedDrList = new ExportUnitQueryUtils().getPickedDrList(lclAddVoyageForm.getUnitssId());
            if (CommonUtils.isNotEmpty(pickedDrList)) {
                TransactionDAO transactionDAO = new TransactionDAO();
                TransactionLedgerDAO ledgerDAO = new TransactionLedgerDAO();
                LclRemarksDAO remarksDAO = new LclRemarksDAO();
                for (ManifestBean picked : pickedDrList) {
                    if ("Yes".equalsIgnoreCase(lclAddVoyageForm.getCob())) {
                        transactionDAO.updateLclEContainers(picked.getFileNo(), lclssheader.getScheduleNo(), lclUnit.getUnitNo());
                        ledgerDAO.updateLclEContainers(picked.getFileNo(), lclUnit.getUnitNo());
                    }
                    String notes = "Unit# changed from " + oldUnit.getUnitNo() + " to " + lclUnit.getUnitNo();
                    remarksDAO.insertLclRemarks(picked.getFileId(), "auto", notes, loginUser.getUserId());
                }
            }
        }

        LclUnitSs unitSs = unitSsDAO.getLclUnitSSByLclUnitHeader(lclUnit.getId(), lclssheader.getId());
        if (unitSs == null) {
            unitSs = new LclUnitSs();
            StringBuilder remarks = new StringBuilder();
            unitSs.setEnteredBy(loginUser);
            unitSs.setEnteredDatetime(now);
            unitSs.setStatus("E");
            unitSs.setOptDocnoLg(false);
            unitSs.setCob(false);
            unitSs.setLclSsHeader(lclssheader);
            unitSs.setLclUnit(lclUnit);
            LclSsRemarksDAO ssRemarksDAO = new LclSsRemarksDAO();
            remarks.append("Unit# " + lclUnit.getUnitNo() + " is Added");
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBookingNumber())) {
                remarks.append(" Linked to Unit -> " + lclUnit.getUnitNo() + " ");
            }
            ssRemarksDAO.insertSsHeaderRemarks(lclAddVoyageForm.getHeaderId(), "auto", remarks.toString(), loginUser.getUserId(), null);
        }
        unitSs.setModifiedBy(loginUser);
        unitSs.setModifiedDatetime(now);

        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBookingNumber())) {
            unitSs.setSpBookingNo(lclAddVoyageForm.getBookingNumber());
            List commList = new LclUnitDAO().getCommodityValues(unitSs.getId());
            if (!commList.isEmpty()) {
                Object[] obj = (Object[]) commList.get(0);
                unitSs.setVolumeImperial(obj[0] != null && !obj[0].toString().trim().equals("") ? (BigDecimal) obj[0] : BigDecimal.ZERO);
                unitSs.setVolumeMetric(obj[1] != null && !obj[1].toString().trim().equals("") ? (BigDecimal) obj[1] : BigDecimal.ZERO);
                unitSs.setWeightImperial(obj[2] != null && !obj[2].toString().trim().equals("") ? (BigDecimal) obj[2] : BigDecimal.ZERO);
                unitSs.setWeightMetric(obj[3] != null && !obj[3].toString().trim().equals("") ? (BigDecimal) obj[3] : BigDecimal.ZERO);
                unitSs.setTotalPieces(obj[4] != null && !obj[4].toString().trim().equals("") ? Integer.parseInt(obj[4].toString()) : 0);
            }
        } else {
            unitSs.setSpBookingNo(null);
        }
        unitSs.setSUHeadingNote(CommonUtils.isNotEmpty(lclAddVoyageForm.getSealNo())
                ? lclAddVoyageForm.getSealNo().toUpperCase() : null);
        unitSs.setReceivedMaster(CommonUtils.isNotEmpty(lclAddVoyageForm.getReceivedMaster())
                ? lclAddVoyageForm.getReceivedMaster().toUpperCase() : null);
        unitSs.setChassisNo(CommonUtils.isNotEmpty(lclAddVoyageForm.getChasisNo())
                ? lclAddVoyageForm.getChasisNo().toUpperCase() : null);
        unitSs.setIntermodalProvided("Y".equalsIgnoreCase(lclAddVoyageForm.getIntermodalProvided()));
        unitSs.setDrayageProvided("Y".equalsIgnoreCase(lclAddVoyageForm.getDrayageProvided()));
        unitSs.setStopOff("Y".equalsIgnoreCase(lclAddVoyageForm.getStopoff()));
        unitSs.setSolasDunnageWeightLBS(new BigDecimal(lclAddVoyageForm.getDunnageWeightLbs()));
        unitSs.setSolasDunnageWeightKGS(new BigDecimal(lclAddVoyageForm.getDunnageWeightKgs()));
        unitSs.setSolasTareWeightLBS(new BigDecimal(lclAddVoyageForm.getTareWeightLbs()));
        unitSs.setSolasTareWeightKGS(new BigDecimal(lclAddVoyageForm.getTareWeightKgs()));
        unitSs.setSolasCargoWeightLBS(new BigDecimal(lclAddVoyageForm.getCargoWeightLbs()));
        unitSs.setSolasCargoWeightKGS(new BigDecimal(lclAddVoyageForm.getCargoWeightKgs()));
        unitSs.setSolasVerificationSign(CommonUtils.isNotEmpty(lclAddVoyageForm.getVerificationSignature())
                ? lclAddVoyageForm.getVerificationSignature().toUpperCase() : "");
        unitSs.setSolasVerificationDate(CommonUtils.isNotEmpty(lclAddVoyageForm.getVerificationDate())
                ? DateUtils.parseDate(lclAddVoyageForm.getVerificationDate(), "dd-MMM-yyyy") : null);
        unitSsDAO.saveOrUpdate(unitSs);

        LclUnitWhse unitWhse = unitWhseDAO.createInstance(lclUnit, lclssheader, loginUser, now);
        unitWhse.setStuffedByUser(CommonUtils.isNotEmpty(lclAddVoyageForm.getLoaddeByUserId())
                ? new UserDAO().findById(Integer.parseInt(lclAddVoyageForm.getLoaddeByUserId())) : null);
        unitWhse.setSealNoOut(lclAddVoyageForm.getSealNo());
        unitWhse.setChassisNoIn(lclAddVoyageForm.getChasisNo());
        unitWhseDAO.saveOrUpdate(unitWhse);

        if (null != lclAddVoyageForm.getFilterByChanges() && CommonUtils.isEmpty(lclAddVoyageForm.getUnitssId())) {
            LclDwr lcldwr = new LclDwr();
            Integer dispoId = lcldwr.disposDesc(lclAddVoyageForm.getFilterByChanges());
            if (null != lclUnit && CommonUtils.isNotEmpty(ssDetailId)) {
                unitSsDispoDAO.insertLclUnitSsDisposition(lclUnit.getId(), ssDetailId, dispoId, loginUser.getUserId());
            }
        }

        lclAddVoyageForm.setCob("");

        // lclUtils.insertLclUnitWhse(lclunit, lclssheader, user, lclAddVoyageForm); // adding warehouse default depand upon the voyage origin
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, loginUser);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSearchLoadDisplay())) {
            request.setAttribute("searchLoadDisplay", "empty");
        }
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward deleteUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        if (lclAddVoyageForm.getUnitId() != null && lclAddVoyageForm.getUnitId() > 0) {
            // LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
            // LclUnit lclunit = lclunitdao.findById(lclAddVoyageForm.getUnitId());
            LclUnitSs lclunitss = lclunitssdao.getLclUnitSSByLclUnitHeader(lclAddVoyageForm.getUnitId(), lclAddVoyageForm.getHeaderId());
            LclUnitWhseDAO unitWhseDAO = new LclUnitWhseDAO();
            LclUnitWhse unitWhse = unitWhseDAO.getLclUnitWhseDetails(lclAddVoyageForm.getUnitId(), lclAddVoyageForm.getHeaderId());
            User loginUser = getCurrentUser(request);
            LclSsRemarksDAO ssRemarksDAO = new LclSsRemarksDAO();
            StringBuilder remarks = new StringBuilder();
            String unitNo = lclunitssdao.getUnitNo(lclAddVoyageForm.getUnitId());
            if ("delete".equalsIgnoreCase(lclAddVoyageForm.getDeleteMoveAction())) {
                remarks.append("Unit# ").append(unitNo).append(" is deleted");
            }
            if ("move".equalsIgnoreCase(lclAddVoyageForm.getDeleteMoveAction())) {
                remarks.append("Unit# ").append(unitNo).append("is moved to yard(").append(lclAddVoyageForm.getWareHouseNo()).append(")");
            }
            if (CommonUtils.isNotEmpty(remarks)) {
                ssRemarksDAO.insertSsHeaderRemarks(lclAddVoyageForm.getHeaderId(), "auto", remarks.toString(), loginUser.getUserId(), null);
            }
            if (null != unitWhse) {
                unitWhseDAO.delete(unitWhse);
            }
            lclunitssdao.delete(lclunitss);
        }
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, getCurrentUser(request));
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSearchLoadDisplay())) {
            request.setAttribute("searchLoadDisplay", "empty");
        }
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public void clearHeaderValues(LclAddVoyageForm lclAddVoyageForm) {
        lclAddVoyageForm.setMethodName(null);
        lclAddVoyageForm.setScheduleNo("");
        lclAddVoyageForm.setSailDate("");
        lclAddVoyageForm.setEtaPod("");
        lclAddVoyageForm.setHazmatLrdt("");
        lclAddVoyageForm.setLoadLrdt("");
        lclAddVoyageForm.setRemarks("");
    }

    public void clearDetailValues(LclAddVoyageForm lclAddVoyageForm) {
        lclAddVoyageForm.setAccountName("");
        lclAddVoyageForm.setSpReferenceName("");
        lclAddVoyageForm.setAccountNumber("");
        lclAddVoyageForm.setSpReferenceNo("");
        lclAddVoyageForm.setStd("");
        lclAddVoyageForm.setDeparturePier("");
        lclAddVoyageForm.setDepartureId(null);
        lclAddVoyageForm.setArrivalPier("");
        lclAddVoyageForm.setArrivalId(null);
        lclAddVoyageForm.setLrdOverrideDays(null);
        lclAddVoyageForm.setTtOverrideDays(null);
        lclAddVoyageForm.setBookingNumber("");
        lclAddVoyageForm.setSslDocsCutoffDate("");
    }

    public void setAllLclSsDetail(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            LclSsDetail lclSsdetail = new LclSsDetailDAO().findById(lclAddVoyageForm.getDetailId());
            if (lclSsdetail != null) {
                request.setAttribute("lclSsdetail", lclSsdetail);
            }
        }
    }

    public ActionForward openUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        //Lock the UNIT Stuffing screen for the current User
        ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setModuleId(LCLEXPORT_VOYAGE);
        processInfo.setRecordid(lclAddVoyageForm.getScheduleNo());
        processInfo.setUserid(getCurrentUser(request).getUserId());
        processInfo.setProcessinfodate(new Date());
        processInfo.setAction("UNIT-STUFFING");
        processInfoDAO.save(processInfo);
        if (!"lclDomestic".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
            User user = getCurrentUser(request);
            RoleDuty roleDuty = new RoleDutyDAO().getByProperty("roleName", user.getRole().getRoleDesc());
            if (roleDuty.isDefaultLoadAllReleased()) {
                lclAddVoyageForm.setBookScheduleNo("N");
                lclAddVoyageForm.setCheckAllRealeaseWithCurrLoc(true);
            } else {
                lclAddVoyageForm.setBookScheduleNo("Y");
                lclAddVoyageForm.setCheckAllRealeaseWithCurrLoc(false);
            }
            lclAddVoyageForm.setIsReleasedDr("N");
            setAllUnitPopupValues(lclAddVoyageForm, request);
        } else {
            setAllUnitPopupValuesForDomestic(lclAddVoyageForm, request);
        }
        request.setAttribute("displayLoadComplete", lclAddVoyageForm.getDisplayLoadComplete());
        setAllLclSsDetail(lclAddVoyageForm, request);
        return mapping.findForward(OPEN_UNIT);
    }

    public ActionForward viewCurrentUnit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        setAllUnitPopupValues(lclAddVoyageForm, request);
        if (null != lclAddVoyageForm.getBookScheduleNo() && !"".equals(lclAddVoyageForm.getBookScheduleNo())) {
            String bookScheduleNoPolEtd = lclAddVoyageForm.getBookScheduleNo() + "/" + lclAddVoyageForm.getPolEtd();
            request.setAttribute("bookScheduleNoPolEtd", bookScheduleNoPolEtd);
            request.setAttribute("polEtd", lclAddVoyageForm.getPolEtd());
            request.setAttribute("bookScheduleNo", lclAddVoyageForm.getBookScheduleNo());
        }
        request.setAttribute("displayLoadComplete", lclAddVoyageForm.getDisplayLoadComplete());
        setAllLclSsDetail(lclAddVoyageForm, request);
        return mapping.findForward(OPEN_UNIT);
    }

    public void setAllUnitPopupValues(LclAddVoyageForm lclAddVoyageForm,
            HttpServletRequest request) throws Exception {
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        LclUnitSs lclunitss = null;
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginId())
                && CommonUtils.isNotEmpty(lclAddVoyageForm.getFinalDestinationId())) {
            lclAddVoyageForm.setForceAgentAcctNo("");
            String agent[] = new LclSsExportsDAO().getAgentAcctNo(lclAddVoyageForm.getHeaderId());
            lclAddVoyageForm.setCfclAcctNo(agent[0]);
            lclAddVoyageForm.setCfclAcctName(agent[1]);
            if ("lclExport".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
                Boolean agentReleasedDr = new LCLPortConfigurationDAO().isForceAgentReleased(lclAddVoyageForm.getFinalDestinationId());
                if (agentReleasedDr) {
                    lclAddVoyageForm.setForceAgentAcctNo(agent[0]);
                }
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitId())) {
                request.setAttribute("unit", lclUnitDAO.findById(lclAddVoyageForm.getUnitId()));
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
                    lclunitss = lclunitssdao.findById(lclAddVoyageForm.getUnitssId());
                    request.setAttribute("lclunitss", lclunitss);
                }
                request.setAttribute("intransitDr", lclAddVoyageForm.isIntransitDr());
                request.setAttribute("destuffedList", lclUnitDAO.getAllReleasedBookings(lclAddVoyageForm));
                request.setAttribute("stuffedList", lclUnitDAO.getStuffedListByUnit(lclAddVoyageForm.getUnitssId()));
                request.setAttribute("bookingUnitsBean", lclUnitDAO.getDrBookingUnitsBean());
            } else {
                request.setAttribute("destuffedList", lclUnitDAO.getAllReleasedBookings(lclAddVoyageForm));
            }
            if ("lclExport".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
                request.setAttribute("intransitDr", lclAddVoyageForm.isIntransitDr());
            }
            request.setAttribute("index", lclAddVoyageForm.getIndex());
            List<BookingUnitsBean> drList = lclUnitDAO.getAllUnitsByVoyage(lclAddVoyageForm.getHeaderId());
            if (lclunitss != null && lclunitss.getLclSsHeader() != null) {
                lclAddVoyageForm.setHeaderId(lclunitss.getLclSsHeader().getId());
            }
            // setDRListForVoyage(lclAddVoyageForm.getHeaderId(), lclUnitDAO, drList);  no need this method fix in jsp page
            if (!drList.isEmpty() && lclAddVoyageForm.getMethodName().equalsIgnoreCase("openUnits")
                    || lclAddVoyageForm.getMethodName().equalsIgnoreCase("viewCurrentUnit")
                    || lclAddVoyageForm.getMethodName().equalsIgnoreCase("saveDestuffedUnits")
                    || lclAddVoyageForm.getMethodName().equalsIgnoreCase("deleteStuffedUnits")) {
                Character status = null;
                List<Character> unitStatusList = new ArrayList<Character>();
                for (BookingUnitsBean li : drList) {
                    if (li.getUnitId() != null && CommonUtils.isNotEmpty(li.getUnitId())) {
                        status = new LclUnitDAO().getUnitStatus(li.getUnitId(), lclAddVoyageForm.getHeaderId());
                        unitStatusList.add(status);
                    } else {
                        unitStatusList.add(' ');
                    }
                }
                request.setAttribute("unitStatusList", unitStatusList);
            }
            request.setAttribute("user", getCurrentUser(request));
            request.setAttribute("unitSsIdFlag", request.getParameter("unitSsIdFlag"));
            request.setAttribute("unitssId", lclAddVoyageForm.getUnitssId());
            request.setAttribute("filterValues", lclAddVoyageForm.getFilterByChanges());
            request.setAttribute("headerId", lclAddVoyageForm.getHeaderId());
            request.setAttribute("originId", lclAddVoyageForm.getOriginId());
            request.setAttribute("finalDestinationId", lclAddVoyageForm.getFinalDestinationId());
            request.setAttribute("drList", drList);
        }
    }

    public ActionForward saveDestuffedUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDestuffedFileNumbers()) && CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitId())) {
            LclUnitSs lclUnitSs = lclUnitSsDAO.findById(lclAddVoyageForm.getUnitssId());
            LclBookingPieceUnitDAO lclBookingPieceUnitDAO = new LclBookingPieceUnitDAO();
            String[] destuffedFileNumberArray = lclAddVoyageForm.getDestuffedFileNumbers().split(",");
            Date d = new Date();
            if (destuffedFileNumberArray != null && destuffedFileNumberArray.length > 0) {
                UnLocation unLocCodeOrgn = new UnLocationDAO().findById(lclAddVoyageForm.getOriginId());
                UnLocation unLocCodeDestn = new UnLocationDAO().findById(lclAddVoyageForm.getFinalDestinationId());
                String type = "lclExport".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())
                        ? "Export" : "lclCfcl".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges()) ? "Cfcl" : "Inland";
                for (String fileId : destuffedFileNumberArray) {
                    Long fileNumberId = Long.parseLong(fileId);
                    String notes = "Picked on " + type + " Unit (" + lclAddVoyageForm.getUnitNo() + "," + unLocCodeOrgn.getUnLocationCode() + "-" + unLocCodeDestn.getUnLocationCode() + "-" + lclAddVoyageForm.getScheduleNo() + ")";
                    remarksDAO.insertLclRemarks(fileNumberId, "Unit_Tracking", notes, user.getUserId());
                    List<LclBookingPiece> lclBookingPiecesList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
                    if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                            LclBookingPieceUnit lclBookingPieceUnit = new LclBookingPieceUnit();
                            lclBookingPieceUnit.setLclBookingPiece(lclBookingPiece);
                            lclBookingPieceUnit.setEnteredBy(getCurrentUser(request));
                            lclBookingPieceUnit.setModifiedBy(getCurrentUser(request));
                            lclBookingPieceUnit.setEnteredDatetime(d);
                            lclBookingPieceUnit.setModifiedDatetime(d);
                            lclBookingPieceUnit.setLoadedDatetime(d);
                            lclBookingPieceUnit.setLclUnitSs(lclUnitSs);
                            lclBookingPieceUnitDAO.saveOrUpdate(lclBookingPieceUnit);
                        }
                    }
                    addSequenceNumber(fileNumberId, lclUnitSs);
                    bookingDAO.updateModifiedDateTime(fileNumberId, user.getUserId());
                }
                // Adding Remarks for dimension limits accepted  mantis item - 7934.
                String dimensionsRemarks = request.getParameter("dimensionsRemarks");
                if (!"".equalsIgnoreCase(dimensionsRemarks) && CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
                    addUnitDimensionsRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), dimensionsRemarks, user.getUserId());
                }
            }
            setAllLclSsDetail(lclAddVoyageForm, request);
            if (!"lclDomestic".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
                setAllUnitPopupValues(lclAddVoyageForm, request);
            } else {
                setAllUnitPopupValuesForDomestic(lclAddVoyageForm, request);
            }
        }
        return mapping.findForward(OPEN_UNIT);
    }

    private void addSequenceNumber(Long fileNumberId, LclUnitSs lclUnitSs) throws Exception {
        LclBl lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", fileNumberId);
        if (null != lclbl && null != lclUnitSs) {
            LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getSequenceNumber())) {
                lclUnitSs.getLclSsHeader().setSequenceNumber(lclUnitSs.getLclSsHeader().getSequenceNumber() + 1);
                lclbl.setSequenceNumber(lclUnitSs.getLclSsHeader().getSequenceNumber());
            } else {
                lclUnitSs.getLclSsHeader().setSequenceNumber(1);
                lclbl.setSequenceNumber(1);
            }
            new LCLBlDAO().saveOrUpdate(lclbl);
            new LclSsHeaderDAO().saveOrUpdate(lclSsHeader);
        }
    }

    public void addUnitDimensionsRemarks(Long headerId, Long UnitId, String dimensionsRemarks, Integer user) throws Exception {
        String[] remarks = dimensionsRemarks.split(",");
        for (String remark : remarks) {
            if (CommonUtils.isNotEmpty(remark)) {
                String fileNumber = remark.substring(remark.indexOf("*") + 1, remark.length());
                String dimensionType = remark.substring(remark.indexOf("$") + 1, remark.indexOf("*"));
                String remarksContent = "Accepted dimensions check warning for DR# " + fileNumber + " - " + dimensionType + " limits";
                new LclUnitSsRemarksDAO().insertLclunitRemarks(headerId, UnitId, "auto", remarksContent, user);
            }
        }
    }

    public ActionForward deleteStuffedUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclFileNumberDAO lclFilenumberDAO = new LclFileNumberDAO();
        LclBookingPieceUnitDAO bookingPieceUnitDAO = new LclBookingPieceUnitDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStuffedFileNumbers())) {
            String[] stuffedFileNumberArray = lclAddVoyageForm.getStuffedFileNumbers().split(",");
            if (stuffedFileNumberArray != null && stuffedFileNumberArray.length > 0) {
                UnLocation unLocCodeOrgn = new UnLocationDAO().findById(lclAddVoyageForm.getOriginId());
                UnLocation unLocCodeDestn = new UnLocationDAO().findById(lclAddVoyageForm.getFinalDestinationId());
                String type = "lclExport".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())
                        ? "Export" : "lclCfcl".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges()) ? "Cfcl" : "Inland";
                for (String fileId : stuffedFileNumberArray) {
                    Long fileNumberId = Long.parseLong(fileId);
                    String notes = "Unpicked from " + type + " Unit (" + lclAddVoyageForm.getUnitNo() + ","
                            + unLocCodeOrgn.getUnLocationCode() + "-" + unLocCodeDestn.getUnLocationCode() + "-" + lclAddVoyageForm.getScheduleNo() + ")";
                    remarksDAO.insertLclRemarks(fileNumberId, "Unit_Tracking", notes, user.getUserId());
                    LclFileNumber lclFileNumber = lclFilenumberDAO.findById(fileNumberId);
                    if (lclFileNumber != null && lclFileNumber.getLclBookingPieceList() != null && lclFileNumber.getLclBookingPieceList().size() > 0) {
                        for (int j = 0; j < lclFileNumber.getLclBookingPieceList().size(); j++) {
                            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclFileNumber.getLclBookingPieceList().get(j);
                            bookingPieceUnitDAO.unPickByFile(lclBookingPiece.getId(), lclAddVoyageForm.getUnitssId());
                        }
                    }
                    bookingDAO.updateModifiedDateTime(fileNumberId, user.getUserId());
                }
            }
        }
        setAllLclSsDetail(lclAddVoyageForm, request);
        if (!"lclDomestic".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
            setAllUnitPopupValues(lclAddVoyageForm, request);
        } else {
            setAllUnitPopupValuesForDomestic(lclAddVoyageForm, request);
        }
        return mapping.findForward(OPEN_UNIT);
    }

    public ActionForward loadComplete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(addVoyageForm.getUnitssId()) && CommonUtils.isNotEmpty(addVoyageForm.getUnitId())) {
            LclSsDetail lclSsDetail = null;
            Date now = new Date();
            Long ssDetailId = null;
            User loginUser = getCurrentUser(request);
            LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
            LclUnitSs unitSs = unitSsDAO.findById(addVoyageForm.getUnitssId());
            unitSs.setStatus("C");
            unitSs.setModifiedBy(loginUser);
            unitSs.setModifiedDatetime(now);
            unitSs.setCompletedDatetime(now);
            unitSsDAO.update(unitSs);
            addVoyageForm.setHeaderId(unitSs.getLclSsHeader().getId());
            Boolean voyageFlag = "lclDomestic".equalsIgnoreCase(addVoyageForm.getFilterByChanges()) ? true : false;
            Disposition disposition = new DispositionDAO().getByProperty("eliteCode", voyageFlag ? "INTR" : "LDEX");
            LclSsDetailDAO unSsDetailDAO = new LclSsDetailDAO();
            RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            LclUnitWhseDAO unitWhseDAO = new LclUnitWhseDAO();
            Warehouse wareHouse = null;
            if (voyageFlag) {
                if (CommonUtils.isNotEmpty(addVoyageForm.getArrivallocation())) {
                    lclSsDetail = unSsDetailDAO.getLclDetailByArrivalId(addVoyageForm.getHeaderId(),
                            Integer.parseInt(addVoyageForm.getArrivallocation()));
                } else {
                    lclSsDetail = unSsDetailDAO.getByProperty("lclSsHeader.id", addVoyageForm.getHeaderId());
                }
                RefTerminal terminal = refTerminalDAO.getTerminalByUnLocation(lclSsDetail.getArrival().getUnLocationCode(), "Y");
                wareHouse = warehouseDAO.getWareHouseBywarehsNo(terminal != null ? "T" + terminal.getTrmnum() : "");
                if (null != wareHouse) {
                    lclSsDetail.setDestuffingWarehouse(wareHouse);
                    unSsDetailDAO.saveOrUpdate(lclSsDetail);
                    Boolean warehouseFlag = unitWhseDAO.isWarehouse(unitSs.getLclSsHeader().getId(), unitSs.getLclUnit().getId(),
                            wareHouse.getId());
                    if (!warehouseFlag) {
                        unitWhseDAO.insert(addVoyageForm.getHeaderId(), addVoyageForm.getUnitId(),
                                wareHouse.getId(), loginUser.getUserId());
                    }
                }

            } else {
                ssDetailId = new LclSsDetailDAO().getIdbyAsc(addVoyageForm.getHeaderId());
            }

            List<ManifestBean> pickedDrList = new ExportUnitQueryUtils().getPickedDrList(unitSs.getId());
            for (ManifestBean pickedDr : pickedDrList) {
                if (voyageFlag) {
                    if (null != wareHouse) {
                        new LclBookingPieceWhseDAO().insertLclBookingPieceWhse(pickedDr.getBkgPieceId(),
                                wareHouse.getId(), loginUser.getUserId());
                    }
                } else {
                    new LclFileNumberDAO().updateLclFileNumbersStatus(pickedDr.getFileId().toString(), "L");
                    new LclRemarksDAO().insertLclRemarks(pickedDr.getFileId(), "auto",
                            "Inventory Status->Loaded(R to L)", loginUser.getUserId());
                }
                new LclBookingDispoDAO().insertBookingDispoForRCVD(pickedDr.getFileId(), disposition.getId(),
                        unitSs.getLclUnit().getId().intValue(), null != lclSsDetail ? lclSsDetail.getId().intValue() : null,
                        loginUser.getUserId(), null != lclSsDetail ? lclSsDetail.getArrival().getId()
                                : unitSs.getLclSsHeader().getOrigin().getId(), null != wareHouse ? wareHouse.getId() : null);
            }

            if (null != disposition) {
                new LclUnitSsDispoDAO().insertLclUnitSsDisposition(unitSs.getLclUnit().getId(),
                        null != lclSsDetail ? lclSsDetail.getId() : ssDetailId, disposition.getId(), loginUser.getUserId());
            }
            addVoyageForm.setUnitssId(null);
        }
        setVoyageValues(addVoyageForm, request);
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward unLoad(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(addVoyageForm.getUnitssId()) && CommonUtils.isNotEmpty(addVoyageForm.getUnitId())) {
            Date now = new Date();
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            LclFileNumberDAO fileNumberDAO = new LclFileNumberDAO();
            LclBookingDispoDAO bookingDispoDAO = new LclBookingDispoDAO();
            LclUnitSsDispoDAO unitSsDispoDAO = new LclUnitSsDispoDAO();
            User loginUser = getCurrentUser(request);
            LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
            LclUnitSs unitSs = unitSsDAO.findById(addVoyageForm.getUnitssId());
            unitSs.setStatus("E");
            unitSs.setModifiedBy(loginUser);
            unitSs.setModifiedDatetime(now);
            unitSs.setCompletedDatetime(null);
            unitSsDAO.update(unitSs);
            Integer wareHouseId = null;
            Integer currentLocId = null;
            Boolean voyageFlag = "lclDomestic".equalsIgnoreCase(addVoyageForm.getFilterByChanges()) ? true : false;
            Long ssDetailId = new LclSsDetailDAO().getIdbyAsc(addVoyageForm.getHeaderId());
            currentLocId = unitSs.getLclSsHeader().getOrigin().getId();
            if (voyageFlag) {
                wareHouseId = new WarehouseDAO().getWarehouseId(new UnLocationDAO().findById(currentLocId) != null
                        ? new UnLocationDAO().findById(currentLocId).getUnLocationCode() : "", "T");
            }
            Disposition disposition = new DispositionDAO().getByProperty("eliteCode", "RCVD");
            List<ManifestBean> pickedDrList = new ExportUnitQueryUtils().getPickedDrList(unitSs.getId());
            for (ManifestBean pickedDr : pickedDrList) {
                if (!voyageFlag) {
                    fileNumberDAO.updateLclFileNumbersStatus(pickedDr.getFileId().toString(), "W");
                    remarksDAO.insertLclRemarks(pickedDr.getFileId(), "auto",
                            "Inventory Status->Un Loaded(L to R)", loginUser.getUserId());
                }
                bookingDispoDAO.insertBookingDispoForRCVD(pickedDr.getFileId(), disposition.getId(), null,
                        null, loginUser.getUserId(), currentLocId, null != wareHouseId ? wareHouseId : null);
            }
            if (null != disposition) {
                unitSsDispoDAO.insertLclUnitSsDisposition(unitSs.getLclUnit().getId(), ssDetailId,
                        disposition.getId(), loginUser.getUserId());
            }
        }
        setVoyageValues(addVoyageForm, request);
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public List<BookingUnitsBean> setDRListForRelease(Integer originId, Integer destinationId, LclUnitDAO lclUnitDAO) throws Exception {
        List<BookingUnitsBean> drList = new ArrayList<BookingUnitsBean>();
        BookingUnitsBean bookingUnitsBean = new BookingUnitsBean();
        bookingUnitsBean.setLabel1("Released");
        List<Object[]> bookingUnitsBeanList = lclUnitDAO.getAllReleasedBookingsTotalWeightMeasure(originId, destinationId);
        if (bookingUnitsBeanList != null && bookingUnitsBeanList.size() > 0) {
            bookingUnitsBean.setCount(bookingUnitsBeanList.size());
            Double totalWeight = 0.0;
            Double totalMeasure = 0.0;
            Integer totalPieces = 0;
            for (int i = 0; i < bookingUnitsBeanList.size(); i++) {
                Object[] instance = bookingUnitsBeanList.get(i);
                if (instance[0] != null) {
                    totalPieces += Integer.parseInt(instance[0].toString());
                }
                if (instance[1] != null) {
                    totalWeight += Double.parseDouble(instance[1].toString());
                }
                if (instance[2] != null) {
                    totalMeasure += Double.parseDouble(instance[2].toString());
                }
            }
            bookingUnitsBean.setTotalPieceCount(totalPieces);
            bookingUnitsBean.setTotalWeightImperial(new BigDecimal(NumberUtils.convertToThreeDecimal(totalWeight)));
            bookingUnitsBean.setTotalVolumeImperial(new BigDecimal(NumberUtils.convertToThreeDecimal(totalMeasure)));
        } else {
            bookingUnitsBean.setCount(0);
            bookingUnitsBean.setTotalPieceCount(0);
            bookingUnitsBean.setTotalWeightImperial(new BigDecimal("0.000"));
            bookingUnitsBean.setTotalVolumeImperial(new BigDecimal("0.000"));
        }
        drList.add(bookingUnitsBean);
        return drList;
    }

    public void setDRListForVoyage(Long headerId, LclUnitDAO lclUnitDAO, List<BookingUnitsBean> drList) throws Exception {
        drList.addAll(lclUnitDAO.getAllUnitsByVoyage(headerId));
    }

    public void setLclUnitSS(LclUnitSs lclunitss, LclSsHeader lclssheader, HttpServletRequest request, Date d) {
        lclunitss.setCob(false);
        lclunitss.setStatus("E");
        lclunitss.setOptDocnoLg(false);
        lclunitss.setModifiedBy(getCurrentUser(request));
        lclunitss.setModifiedDatetime(d);
        lclunitss.setLclSsHeader(lclssheader);
    }

    public ActionForward openSSMasterPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSSMasterBlDAO lclMasterBlDAO = new LclSSMasterBlDAO();
        LclSsDetailDAO lclSSDetailsDAO = new LclSsDetailDAO();
        DBUtil dbUtil = new DBUtil();
        FCLPortConfigurationDAO fCLPortConfigurationDAO = new FCLPortConfigurationDAO();
        request.setAttribute("pcList", lclMasterBlDAO.getAllBillingTypes());
        request.setAttribute("destChargeList", lclMasterBlDAO.getDestChargeList());
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            request.setAttribute("contractNumber", lclSSDetailsDAO.getContractNumber(lclAddVoyageForm.getHeaderId()));
        }
        lclAddVoyageForm.setDestPrepaidCollect("C");
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getPolUnlocationCode())) {
            String[] shipperDetails = lclSSDetailsDAO.getAddress(lclAddVoyageForm.getPolUnlocationCode());
            if (CommonUtils.isNotEmpty(shipperDetails)) {
                lclAddVoyageForm.setShipperAccountNo(shipperDetails[0]);
                lclAddVoyageForm.setShipperEdi(shipperDetails[1]);
                lclAddVoyageForm.setShipperAccountNumber(shipperDetails[2]);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFdUnlocationCode())) {
            String[] consigneeDetails = lclSSDetailsDAO.getAgentAddress(lclAddVoyageForm.getFdUnlocationCode());
            if (CommonUtils.isNotEmpty(consigneeDetails)) {
                lclAddVoyageForm.setConsigneeAccountNo(consigneeDetails[0]);
                lclAddVoyageForm.setConsigneeAccountNumber(consigneeDetails[1]);
                StringBuilder concatAgentAddress = new StringBuilder();
                concatAgentAddress.append(consigneeDetails[2]);
                String zipConcated = "";
                if (null != consigneeDetails[3]) {
                    zipConcated = consigneeDetails[3] + ", ";
                }
                if (null != consigneeDetails[4]) {
                    zipConcated += consigneeDetails[4] + ", ";
                }
                if (null != consigneeDetails[5]) {
                    zipConcated = consigneeDetails[5];
                }
                if (!"".equalsIgnoreCase(zipConcated)) {
                    concatAgentAddress.append("\n").append(zipConcated);
                }
                if (null != consigneeDetails[6]) {
                    concatAgentAddress.append("\n").append("Phone:").append(consigneeDetails[6]);
                }
                if (null != consigneeDetails[7]) {
                    concatAgentAddress.append("\n").append("Fax:").append(consigneeDetails[7]);
                }
                lclAddVoyageForm.setConsigneeEdi(concatAgentAddress.toString());
            }
        }
        //********code of Release Clause ***************************************
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFdUnlocationCode())) {
            GenericCode genericCodePortSetUp = fCLPortConfigurationDAO.getReleaseClause(lclAddVoyageForm.getFdUnlocationCode());
            if (null != genericCodePortSetUp) {
                if (CommonUtils.isNotEmpty(genericCodePortSetUp.getId())) {
                    lclAddVoyageForm.setReleaseClause((genericCodePortSetUp.getId()).toString());
                }
                if (CommonUtils.isNotEmpty(genericCodePortSetUp.getCodedesc())) {
                    lclAddVoyageForm.setClauseDescription(genericCodePortSetUp.getCodedesc());
                }
            }
        }
        List clauseList = new ArrayList();
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("FCL Release Codes");
        clauseList = dbUtil.getGenericCodeList(codeTypeId, "No", "Select BL Clauses");
        request.setAttribute("clauseList", clauseList);
        //**********************************************************************
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward(DISPLAY_SS_MASTER_DETAIL_POPUP);
    }

    public ActionForward openSSDetailsPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        String unLocationcode = "";
        if (lclAddVoyageForm.getFilterByChanges() != null && (lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclExport") || lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic"))) {
            PortsDAO portsDAO = new PortsDAO();
            unLocationcode = lclAddVoyageForm.getPodDestination();
            if (CommonUtils.isNotEmpty(unLocationcode)) {
                String agentInfo[] = portsDAO.getDefaultAgentForLcl(unLocationcode.substring(unLocationcode.indexOf("(") + 1, unLocationcode.length() - 1), "L");
                if (CommonUtils.isNotEmpty(agentInfo)) {
                    lclAddVoyageForm.setExportAgentAcctNo(agentInfo[0] != null ? agentInfo[0] : "");
                    lclAddVoyageForm.setExportAgentAcctName(agentInfo[1] != null ? agentInfo[1] : "");
                    lclAddVoyageForm.setAgentBrandName(agentInfo[4] != null ? agentInfo[4] : "");
                }
                lclAddVoyageForm.setFdUnlocationCode(unLocationcode.substring(unLocationcode.indexOf("(") + 1, unLocationcode.length() - 1));
            }
        }
        String companyCodeValue = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        request.setAttribute("companyCodeValue", companyCodeValue);
        request.setAttribute("filterByChanges", lclAddVoyageForm.getFilterByChanges());
        return mapping.findForward(DISPLAY_DETAIL_POPUP);
    }

    public ActionForward saveSSMasterDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsRemarksDAO lclSsRemarksDAO = new LclSsRemarksDAO();
        String remarks = "";
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            Date d = new Date();
            User loginUser = getCurrentUser(request);
            LclSSMasterBl lclssmasterbl = null;
            LclSSMasterBlDAO lclssmasterbldao = new LclSSMasterBlDAO();
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getMasterId())) {
                lclssmasterbl = lclssmasterbldao.findById(lclAddVoyageForm.getMasterId());
                if (!lclssmasterbl.getSpBookingNo().equalsIgnoreCase(lclAddVoyageForm.getBookingNumber())) {
                    new ExportUnitQueryUtils().updateUnitSsAssociatedwithMasterBl(lclssmasterbl.getLclSsHeader().getId(),
                            lclAddVoyageForm.getBookingNumber(), lclssmasterbl.getSpBookingNo());
                }
            } else {
                lclssmasterbl = new LclSSMasterBl();
                lclssmasterbl.setEnteredBy(loginUser);
                lclssmasterbl.setEnteredDatetime(d);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBookingNumber())) {
                lclssmasterbl.setSpBookingNo(lclAddVoyageForm.getBookingNumber().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getContractNumber())) {
                lclssmasterbl.setSpContractNo(lclAddVoyageForm.getContractNumber());
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getPrepaidCollect())) {
                lclssmasterbl.setPrepaidCollect(lclAddVoyageForm.getPrepaidCollect());
            }
            if (lclAddVoyageForm.getExportReferenceEdi() != null && !lclAddVoyageForm.getExportReferenceEdi().trim().equals("")) {
                lclssmasterbl.setExportRefEdi(lclAddVoyageForm.getExportReferenceEdi().toUpperCase());
            } else {
                lclssmasterbl.setExportRefEdi("");
            }
            if (lclAddVoyageForm.getRemarks() != null && !lclAddVoyageForm.getRemarks().trim().equals("")) {
                lclssmasterbl.setRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
            } else {
                lclssmasterbl.setRemarks("");
            }

            if (lclAddVoyageForm.getMoveType() != null && !lclAddVoyageForm.getMoveType().trim().equals("")) {
                lclssmasterbl.setMoveType(lclAddVoyageForm.getMoveType().toUpperCase());
            } else {
                lclssmasterbl.setMoveType("");
            }
            lclssmasterbl.setDestPrepaidCollect(lclAddVoyageForm.getDestPrepaidCollect());
            lclssmasterbl.setMasterBl(CommonUtils.isNotEmpty(lclAddVoyageForm.getSsMasterBl())
                    ? lclAddVoyageForm.getSsMasterBl().toUpperCase() : null);
            lclssmasterbl.setPrintDockRecipt(lclAddVoyageForm.isPrintSsDockReceipt());
            lclssmasterbl.setInvoiceValue(lclAddVoyageForm.isMasterBlInvoiceValue());
            lclssmasterbl.setModifiedBy(getCurrentUser(request));
            lclssmasterbl.setModifiedDatetime(d);
            //SHIPPER
            LclSsContactDAO lclSsContactDAO = new LclSsContactDAO();
            LclSsContact lclSsContact = null;
            if (lclssmasterbl.getShipSsContactId() != null && lclssmasterbl.getShipSsContactId().getId() != null) {
                lclSsContact = lclSsContactDAO.findById(lclssmasterbl.getShipSsContactId().getId());
                if (lclSsContact != null && CommonUtils.isNotEmpty(lclSsContact.getCompanyName())) {
                    if (null != lclSsContact.getTradingPartner() && !lclSsContact.getCompanyName().equals(lclAddVoyageForm.getShipperAccountNo())) {
                        remarks = "UPDATED ->(ShipperAcct# ->" + lclSsContact.getCompanyName() + "(" + lclSsContact.getTradingPartner().getAccountno() + ")"
                                + " to " + lclAddVoyageForm.getShipperAccountNo() + "(" + lclAddVoyageForm.getShipperAccountNumber() + ")" + ")";
                        lclSsRemarksDAO.insertLclSSRemarks(lclAddVoyageForm.getHeaderId(), "auto", null, remarks, null, getCurrentUser(request).getUserId());
                    }
                } else if (CommonUtils.isNotEmpty(lclAddVoyageForm.getShipperAccountNo())) {
                    remarks = "UPDATED ->(ShipperAcct# -> to " + lclAddVoyageForm.getShipperAccountNo() + "(" + lclAddVoyageForm.getShipperAccountNumber() + ")" + ")";
                    lclSsRemarksDAO.insertLclSSRemarks(lclAddVoyageForm.getHeaderId(), "auto", null, remarks, null, getCurrentUser(request).getUserId());
                }
            }
            if (lclSsContact == null) {
                lclSsContact = new LclSsContact();
                lclSsContact.setEnteredDatetime(new Date());
                lclSsContact.setEnteredByUserId(getCurrentUser(request));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getShipperAccountNumber())) {
                lclSsContact.setTradingPartner(tradingPartnerDAO.findById(lclAddVoyageForm.getShipperAccountNumber()));
            } else {
                lclSsContact.setTradingPartner(null);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getShipperAccountNo())) {
                lclSsContact.setCompanyName(lclAddVoyageForm.getShipperAccountNo().toUpperCase());
            } else {
                lclSsContact.setCompanyName("");
            }
            if (lclAddVoyageForm.getShipperEdi() != null && !lclAddVoyageForm.getShipperEdi().trim().equals("")) {
                lclSsContact.setAddress(lclAddVoyageForm.getShipperEdi());
                lclssmasterbl.setShipEdi(lclAddVoyageForm.getShipperEdi().toUpperCase());
            } else {
                lclSsContact.setAddress("");
                lclssmasterbl.setShipEdi("");
            }
            lclSsContact.setContactName("");
            lclSsContact.setLclSsHeader(lclssheader);
            lclSsContact.setModifiedDatetime(new Date());
            lclSsContact.setModifiedByUserId(loginUser);
            lclSsContactDAO.saveOrUpdate(lclSsContact);
            lclssmasterbl.setShipSsContactId(lclSsContact);
            //CONSIGNEE
            LclSsContact lclcongSsContact = null;
            if (lclssmasterbl != null && lclssmasterbl.getConsSsContactId() != null && lclssmasterbl.getConsSsContactId().getId() != null) {
                lclcongSsContact = lclSsContactDAO.findById(lclssmasterbl.getConsSsContactId().getId());
                if (lclcongSsContact != null && CommonUtils.isNotEmpty(lclcongSsContact.getCompanyName())) {
                    if (!lclcongSsContact.getCompanyName().equals(lclAddVoyageForm.getConsigneeAccountNo())) {
                        remarks = "UPDATED ->(ConsigneeAcct# ->" + lclcongSsContact.getCompanyName() + "(" + lclcongSsContact.getTradingPartner().getAccountno() + ")"
                                + " to " + lclAddVoyageForm.getConsigneeAccountNo() + "(" + lclAddVoyageForm.getConsigneeAccountNumber() + ")" + ")";
                        lclSsRemarksDAO.insertLclSSRemarks(lclAddVoyageForm.getHeaderId(), "auto", null, remarks, null, getCurrentUser(request).getUserId());
                    }
                } else if (CommonUtils.isNotEmpty(lclAddVoyageForm.getConsigneeAccountNo())) {
                    remarks = "UPDATED ->(ConsigneeAcct# -> to " + lclAddVoyageForm.getConsigneeAccountNo() + "(" + lclAddVoyageForm.getConsigneeAccountNumber() + ")" + ")";
                    lclSsRemarksDAO.insertLclSSRemarks(lclAddVoyageForm.getHeaderId(), "auto", null, remarks, null, getCurrentUser(request).getUserId());
                }
            }
            if (lclcongSsContact == null) {
                lclcongSsContact = new LclSsContact();
                lclcongSsContact.setEnteredDatetime(new Date());
                lclcongSsContact.setEnteredByUserId(getCurrentUser(request));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getConsigneeAccountNo())) {
                lclcongSsContact.setCompanyName(lclAddVoyageForm.getConsigneeAccountNo());
            } else {
                lclcongSsContact.setCompanyName("");
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getConsigneeAccountNumber())) {
                lclcongSsContact.setTradingPartner(tradingPartnerDAO.findById(lclAddVoyageForm.getConsigneeAccountNumber().toUpperCase()));
            } else {
                lclcongSsContact.setTradingPartner(null);
            }
            if (lclAddVoyageForm.getConsigneeEdi() != null && !lclAddVoyageForm.getConsigneeEdi().trim().equals("")) {
                lclcongSsContact.setAddress(lclAddVoyageForm.getConsigneeEdi().toUpperCase());
                lclssmasterbl.setConsEdi(lclAddVoyageForm.getConsigneeEdi().toUpperCase());
            } else {
                lclcongSsContact.setAddress("");
                lclssmasterbl.setConsEdi("");
            }
            lclcongSsContact.setContactName("");
            lclcongSsContact.setLclSsHeader(lclssheader);
            lclcongSsContact.setModifiedDatetime(new Date());
            lclcongSsContact.setModifiedByUserId(loginUser);
            lclSsContactDAO.saveOrUpdate(lclcongSsContact);
            lclssmasterbl.setConsSsContactId(lclcongSsContact);
            //Notify
            LclSsContact lclNotySsContact = null;
            if (lclssmasterbl != null && lclssmasterbl.getNotySsContactId() != null
                    && lclssmasterbl.getNotySsContactId().getId() != null && CommonUtils.isNotEmpty(lclAddVoyageForm.getNotifyAccountNo())) {
                lclNotySsContact = lclSsContactDAO.findById(lclssmasterbl.getNotySsContactId().getId());
                if (lclNotySsContact != null && CommonUtils.isNotEmpty(lclNotySsContact.getCompanyName()) && lclNotySsContact.getTradingPartner() != null) {
                    if (!lclNotySsContact.getCompanyName().equals(lclAddVoyageForm.getNotifyAccountNo())) {
                        remarks = "UPDATED ->(NotifyAcct# ->" + lclNotySsContact.getCompanyName() + "(" + lclNotySsContact.getTradingPartner().getAccountno() + ")"
                                + " to " + lclAddVoyageForm.getNotifyAccountNo() + "(" + lclAddVoyageForm.getNotifyAccountNumber() + ")" + ")";
                        lclSsRemarksDAO.insertLclSSRemarks(lclAddVoyageForm.getHeaderId(), "auto", null, remarks, null, getCurrentUser(request).getUserId());
                    }
                } else if (CommonUtils.isNotEmpty(lclAddVoyageForm.getNotifyAccountNo())) {
                    remarks = "UPDATED ->(NotifyAcct# -> to " + lclAddVoyageForm.getNotifyAccountNo() + "(" + lclAddVoyageForm.getNotifyAccountNumber() + ")" + ")";
                    lclSsRemarksDAO.insertLclSSRemarks(lclAddVoyageForm.getHeaderId(), "auto", "", remarks, null, getCurrentUser(request).getUserId());
                }
            }
            if (lclNotySsContact == null) {
                lclNotySsContact = new LclSsContact();
                lclNotySsContact.setEnteredDatetime(new Date());
                lclNotySsContact.setEnteredByUserId(getCurrentUser(request));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getNotifyAccountNumber())) {
                lclNotySsContact.setTradingPartner(tradingPartnerDAO.findById(lclAddVoyageForm.getNotifyAccountNumber().toUpperCase()));
                lclNotySsContact.setCompanyName(lclAddVoyageForm.getNotifyAccountNo().toUpperCase());
            } else {
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getManualNotyName())) {
                    lclNotySsContact.setCompanyName(lclAddVoyageForm.getManualNotyName().toUpperCase());
                    lclNotySsContact.setTradingPartner(null);
                } else {
                    lclNotySsContact.setCompanyName("");
                    lclNotySsContact.setTradingPartner(null);
                }
            }
            if (lclAddVoyageForm.getNotifyEdi() != null && !lclAddVoyageForm.getNotifyEdi().trim().equals("")) {
                lclNotySsContact.setAddress(lclAddVoyageForm.getNotifyEdi().toUpperCase());
                lclssmasterbl.setNotyEdi(lclAddVoyageForm.getNotifyEdi().toUpperCase());
            } else {
                lclNotySsContact.setAddress("");
                lclssmasterbl.setNotyEdi("");
            }

            lclNotySsContact.setContactName("");
            lclNotySsContact.setLclSsHeader(lclssheader);
            lclNotySsContact.setModifiedDatetime(new Date());
            lclNotySsContact.setModifiedByUserId(getCurrentUser(request));
            //********code for Release Clause **********************************
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            GenericCode genObj = null;
            genObj = genericCodeDAO.findById(Integer.parseInt(lclAddVoyageForm.getReleaseClause()));
            lclssmasterbl.setReleaseClause(genObj);
            //******************************************************************
            lclSsContactDAO.saveOrUpdate(lclNotySsContact);
            lclssmasterbl.setNotySsContactId(lclNotySsContact);
            lclssmasterbl.setLclSsHeader(lclssheader);
            lclssmasterbldao.saveOrUpdate(lclssmasterbl);

            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitSsIds())) {
                LclSsAcDAO ssAcDAO = new LclSsAcDAO();
                String ssAcId = ssAcDAO.getSsAcIds(lclAddVoyageForm.getUnitSsIds());
                System.out.println("----" + ssAcId);
                String vendorAcctNo = "";
                if ("C".equalsIgnoreCase(lclAddVoyageForm.getPrepaidCollect())) {
                    String[] consigneeDetails = new LclSsDetailDAO().getAgentAddress(lclssheader.getDestination().getUnLocationCode());
                    if (CommonUtils.isNotEmpty(consigneeDetails)) {
                        vendorAcctNo = consigneeDetails[1];
                    }
                } else {
                    vendorAcctNo = lclssheader.getVesselSsDetail().getSpAcctNo().getAccountno();
                }
                if (!"".equalsIgnoreCase(vendorAcctNo)) {
                    ssAcDAO.updateCostByVendor(lclAddVoyageForm.getUnitSsIds(), vendorAcctNo);
                    ssAcDAO.updateTransacationByVendor(ssAcId, vendorAcctNo);
                }
            }

            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, loginUser);
        }
        request.setAttribute("openPopup", "notopen");
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSearchLoadDisplay())) {
            request.setAttribute("searchLoadDisplay", "empty");
        }
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward editSSMasterDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSSMasterBlDAO lclssmasterbldao = new LclSSMasterBlDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        FCLPortConfigurationDAO fCLPortConfigurationDAO = new FCLPortConfigurationDAO();
        DBUtil dbUtil = new DBUtil();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getMasterId())) {
            LclSSMasterBl lclssmasterbl = lclssmasterbldao.findById(lclAddVoyageForm.getMasterId());
            lclAddVoyageForm.setDestPrepaidCollect(lclssmasterbl.getDestPrepaidCollect());
            if (CommonUtils.isNotEmpty(lclssmasterbl.getSpBookingNo())) {
                lclAddVoyageForm.setBookingNumber(lclssmasterbl.getSpBookingNo());
            }
            lclAddVoyageForm.setPrintSsDockReceipt(lclssmasterbl.isPrintDockRecipt());
            lclAddVoyageForm.setMasterBlInvoiceValue(lclssmasterbl.isInvoiceValue());
            lclAddVoyageForm.setHeaderId(lclssmasterbl.getLclSsHeader().getId());
            lclAddVoyageForm.setContractNumber(new LclSsDetailDAO().getContractNumber(lclssmasterbl.getLclSsHeader().getId()));
            if (CommonUtils.isNotEmpty(lclssmasterbl.getPrepaidCollect())) {
                lclAddVoyageForm.setPrepaidCollect(lclssmasterbl.getPrepaidCollect());
            }
            if (lclssmasterbl.getShipSsContactId() != null && lclssmasterbl.getShipSsContactId().getId() != null) {
                if (CommonUtils.isNotEmpty(lclssmasterbl.getShipSsContactId().getCompanyName())) {
                    lclAddVoyageForm.setShipperAccountNo(lclssmasterbl.getShipSsContactId().getCompanyName());
                }
                if (lclssmasterbl.getShipSsContactId().getTradingPartner() != null && lclssmasterbl.getShipSsContactId().getTradingPartner().getAccountno() != null) {
                    lclAddVoyageForm.setShipperAccountNumber(lclssmasterbl.getShipSsContactId().getTradingPartner().getAccountno());
                }
            }
            if (lclssmasterbl.getConsSsContactId() != null && lclssmasterbl.getConsSsContactId().getId() != null) {
                if (CommonUtils.isNotEmpty(lclssmasterbl.getConsSsContactId().getCompanyName())) {
                    lclAddVoyageForm.setConsigneeAccountNo(lclssmasterbl.getConsSsContactId().getCompanyName());
                }
                if (lclssmasterbl.getConsSsContactId().getTradingPartner() != null && lclssmasterbl.getConsSsContactId().getTradingPartner().getAccountno() != null) {
                    lclAddVoyageForm.setConsigneeAccountNumber(lclssmasterbl.getConsSsContactId().getTradingPartner().getAccountno());
                }
            }
            if (lclssmasterbl.getNotySsContactId() != null && lclssmasterbl.getNotySsContactId().getId() != null) {
                if (lclssmasterbl.getNotySsContactId().getTradingPartner() != null && lclssmasterbl.getNotySsContactId().getTradingPartner().getAccountno() != null) {
                    lclAddVoyageForm.setNotifyAccountNumber(lclssmasterbl.getNotySsContactId().getTradingPartner().getAccountno());
                    lclAddVoyageForm.setNotifyAccountNo(lclssmasterbl.getNotySsContactId().getTradingPartner().getAccountName());
                } else {
                    if (lclssmasterbl.getNotySsContactId().getCompanyName() != null) {
                        lclAddVoyageForm.setManualNotyName(lclssmasterbl.getNotySsContactId().getCompanyName());
                    }
                }
            }
            if (CommonUtils.isNotEmpty(lclssmasterbl.getShipEdi())) {
                lclAddVoyageForm.setShipperEdi(lclssmasterbl.getShipEdi());
            }
            if (CommonUtils.isNotEmpty(lclssmasterbl.getConsEdi())) {
                lclAddVoyageForm.setConsigneeEdi(lclssmasterbl.getConsEdi());
            }
            if (CommonUtils.isNotEmpty(lclssmasterbl.getNotyEdi())) {
                lclAddVoyageForm.setNotifyEdi(lclssmasterbl.getNotyEdi());
            }
            if (CommonUtils.isNotEmpty(lclssmasterbl.getExportRefEdi())) {
                lclAddVoyageForm.setExportReferenceEdi(lclssmasterbl.getExportRefEdi());
            }
//            if (CommonUtils.isNotEmpty(lclssmasterbl.getBlBody())) {
//                lclAddVoyageForm.setBlbody(lclssmasterbl.getBlBody());
//            }
            if (lclssmasterbl.getRemarks() != null) {
                lclAddVoyageForm.setRemarks(lclssmasterbl.getRemarks());
            }
            if (lclssmasterbl.getMoveType() != null) {
                lclAddVoyageForm.setMoveType(lclssmasterbl.getMoveType());
            }
            if (lclssmasterbl.getMasterBl() != null) {
                lclAddVoyageForm.setSsMasterBl(lclssmasterbl.getMasterBl());
            }
            //***********Code for Release Clause *******************************
            GenericCode genericCodeSsMasterBl = lclssmasterbl.getReleaseClause();
            if (genericCodeSsMasterBl != null) {
                if (CommonUtils.isNotEmpty(new Integer(genericCodeSsMasterBl.getId()))) {
                    lclAddVoyageForm.setReleaseClause((genericCodeSsMasterBl.getId()).toString());
                }
                if (CommonUtils.isNotEmpty(genericCodeSsMasterBl.getCodedesc())) {
                    lclAddVoyageForm.setClauseDescription(genericCodeSsMasterBl.getCodedesc());
                }
            } else {
                Integer UnLocationId = lclAddVoyageForm.getFinalDestinationId();
                UnLocation unLocation = unLocationDAO.findById(UnLocationId);
                if (unLocation != null) {
                    String unLocatioCode = unLocation.getUnLocationCode();
                    GenericCode genericCodePortSetUp = fCLPortConfigurationDAO.getReleaseClause(unLocatioCode);
                    if (null != genericCodePortSetUp) {
                        lclssmasterbl.setReleaseClause(genericCodePortSetUp);
                        if (CommonUtils.isNotEmpty(new Integer(genericCodePortSetUp.getId()))) {
                            lclAddVoyageForm.setReleaseClause((genericCodePortSetUp.getId()).toString());
                        }
                        if (CommonUtils.isNotEmpty(genericCodePortSetUp.getCodedesc())) {
                            lclAddVoyageForm.setClauseDescription(genericCodePortSetUp.getCodedesc());
                        }
                    }
                }
            }
            List<ExportVoyageSearchModel> unitSsMasterList = new LclUnitSsDAO().showSsMasterDetails(lclssmasterbl.getLclSsHeader().getId(), lclssmasterbl.getSpBookingNo());
            request.setAttribute("unitSsMasterList", unitSsMasterList);
            List clauseList = new ArrayList();
            Integer codeTypeId = new CodetypeDAO().getCodeTypeId("FCL Release Codes");
            clauseList = dbUtil.getGenericCodeList(codeTypeId, "No", "Select BL Clauses");
            request.setAttribute("clauseList", clauseList);
            //******************************************************************
            request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        }
        request.setAttribute("pcList", lclssmasterbldao.getAllBillingTypes());
        return mapping.findForward(DISPLAY_SS_MASTER_DETAIL_POPUP);
    }

    public ActionForward deleteSSMasterDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId()) && CommonUtils.isNotEmpty(lclAddVoyageForm.getMasterId())) {
            LclSSMasterBlDAO lclssmasterbldao = new LclSSMasterBlDAO();
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSSMasterBl lclssmasterbl = lclssmasterbldao.findById(lclAddVoyageForm.getMasterId());
            lclssmasterbldao.delete(lclssmasterbl);
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, getCurrentUser(request));
        }
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward manifest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
            LclManifestDAO manifestDAO = new LclManifestDAO();
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            Date now = new Date();
            User loginUser = getCurrentUser(request);
            LclUnitSs lclUnitSs = lclUnitSsDAO.findById(lclAddVoyageForm.getUnitssId());
            lclUnitSs.setStatus("M");
            lclUnitSs.setModifiedBy(loginUser);
            lclUnitSs.setModifiedDatetime(now);
            lclUnitSs.setManifestedDateTime(now);
            lclUnitSsDAO.saveOrUpdate(lclUnitSs);
            LclSsHeader lclssheader = lclUnitSs.getLclSsHeader();
            new LclUnitSsRemarksDAO().insertLclunitRemarks(lclAddVoyageForm.getHeaderId(),
                    lclAddVoyageForm.getUnitId(), "auto", "Unit Is Manifested", loginUser.getUserId());
            // lclUtils.updateLCLUnitSSStatus(lclAddVoyageForm.getUnitssId(), "M", getCurrentUser(request));
            manifestDAO.updateFileStatusAndNotes(lclUnitSs, loginUser, true);
            LclUnitSsManifestDAO unitSsManifestDAO = new LclUnitSsManifestDAO();
            LclUnitSsManifest lclunitssmanifest = unitSsManifestDAO.insertUnitSsManifest(lclUnitSs, lclUnitSs.getLclUnit(), lclssheader, loginUser, now, true);
            lclunitssmanifest.setManifestedByUser(loginUser);
            lclunitssmanifest.setManifestedDatetime(now);
            unitSsManifestDAO.saveOrUpdate(lclunitssmanifest);
            //lclUtils.updateLCLUnitManifestStatus(lclAddVoyageForm, lclssheader, loginUser);
            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, loginUser);
        }
        return mapping.findForward("displayNewVoyage");
    }

    public ActionForward unManifest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            User loginUser = getCurrentUser(request);
            Date now = new Date();
            LclUnitSs lclUnitSs = lclUnitSsDAO.findById(lclAddVoyageForm.getUnitssId());
            lclUnitSs.setStatus("C");
            lclUnitSs.setModifiedBy(loginUser);
            lclUnitSs.setModifiedDatetime(now);
            lclUnitSs.setManifestedDateTime(null);
            lclUnitSsDAO.saveOrUpdate(lclUnitSs);
            LclSsHeader lclssheader = lclUnitSs.getLclSsHeader();
            LclManifestDAO manifestDAO = new LclManifestDAO();
            new LclUnitSsRemarksDAO().insertLclunitRemarks(lclAddVoyageForm.getHeaderId(),
                    lclAddVoyageForm.getUnitId(), "auto", "Unit Is UnManifested", loginUser.getUserId());
//            new LclUnitSsManifestDAO().insertUnitSsManifest(lclUnitSs.getLclUnit().getId(),
//                    lclUnitSs.getLclUnit(), lclssheader, null, now);
            LclUnitSsManifestDAO unitSsManifestDAO = new LclUnitSsManifestDAO();
            LclUnitSsManifest lclunitssmanifest = unitSsManifestDAO.insertUnitSsManifest(lclUnitSs, lclUnitSs.getLclUnit(), lclssheader, loginUser, now, false);
            lclunitssmanifest.setManifestedByUser(null);
            lclunitssmanifest.setManifestedDatetime(null);
            unitSsManifestDAO.saveOrUpdate(lclunitssmanifest);
            // manifestDAO.getManifestByExport(lclAddVoyageForm.getUnitssId(), getCurrentUser(request), false, null, transMode);
            //lclUtils.updateLCLUnitSSStatus(lclAddVoyageForm.getUnitssId(), "C", getCurrentUser(request));
            //manifestDAO.getManifestByExportForUpdateLcLFile(lclAddVoyageForm.getUnitssId(), getCurrentUser(request), false, null, transMode);
            manifestDAO.updateFileStatusAndNotes(lclUnitSs, loginUser, false);
            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, loginUser);
        }
        return mapping.findForward("displayNewVoyage");
    }

    public void setVoyageValues(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            LclSsHeader lclssheader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
            new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, getCurrentUser(request));
        }
//            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
//            lclUtils.setAllVoyageValues(request, lclssheader, lclAddVoyageForm);
//            if (lclssheader.getOrigin() != null) {
//                request.setAttribute("originId", lclssheader.getOrigin().getId());
//            }
//            if (lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")) {
//                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalOriginName())) {
//                    request.setAttribute("originValue", lclAddVoyageForm.getOriginalOriginName());
//                }
//                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalDestinationName())) {
//                    request.setAttribute("destinationValue", lclAddVoyageForm.getOriginalDestinationName());
//                }
//            } else {
//                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getPol())) {
//                    request.setAttribute("originValue", lclAddVoyageForm.getPol());
//                }
//                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getPod())) {
//                    request.setAttribute("destinationValue", lclAddVoyageForm.getPod());
//                }
//                lclUtils.setRelayTTDBD(request, lclssheader.getOrigin().getId(), lclssheader.getDestination().getId());
//            }
//            if (lclssheader.getDestination() != null) {
//                request.setAttribute("destinationId", lclssheader.getDestination().getId());
//            }
//
//            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalOriginId())) {
//                request.setAttribute("originalOriginId", lclAddVoyageForm.getOriginalOriginId());
//            }
//            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalDestinationId())) {
//                request.setAttribute("originalDestinationId", lclAddVoyageForm.getOriginalDestinationId());
//            }
//            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalOriginName())) {
//                request.setAttribute("originalOriginName", lclAddVoyageForm.getOriginalOriginName());
//            }
//            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalDestinationName())) {
//                request.setAttribute("originalDestinationName", lclAddVoyageForm.getOriginalDestinationName());
//            }
//        }
        request.setAttribute("filterValues", lclAddVoyageForm.getFilterByChanges());
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("openPopup", "notopen");
    }

    public ActionForward addBLToManifest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward(DISPLAY_ADD_MANIFESTTOBL_POPUP);
    }

    public ActionForward showBLStatus(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFileNumber())) {
            LclFileNumberDAO lclFilenumberDAO = new LclFileNumberDAO();
            setBLStatus(lclAddVoyageForm, request, lclFilenumberDAO);
        }
        return mapping.findForward(DISPLAY_ADD_MANIFESTTOBL_STATUS);
    }

    private void setBLStatus(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request,
            LclFileNumberDAO lclFilenumberDAO) throws Exception {
        lclAddVoyageForm.setFileNumber(lclAddVoyageForm.getFileNumber().toUpperCase());
        Object[] lclFileNumberObject = lclFilenumberDAO.getStateStatusByField("file_number", lclAddVoyageForm.getFileNumber());
        if (lclFileNumberObject != null && lclFileNumberObject.length > 0) {
            String state = lclFileNumberObject[0].toString();
            if (!state.equalsIgnoreCase("Q")) {
                Integer polId = Integer.parseInt(lclFileNumberObject[2].toString());
                Integer podId = Integer.parseInt(lclFileNumberObject[3].toString());
                if (polId.intValue() == lclAddVoyageForm.getPolId().intValue() && podId.intValue() == lclAddVoyageForm.getPodId().intValue()) {
                    String status = lclFileNumberObject[1].toString();
                    if (state.equalsIgnoreCase("BL")) {
                        if (status.equalsIgnoreCase("M")) {
                            lclAddVoyageForm.setStatusMessage("MANIFESTED");
                            lclAddVoyageForm.setClassName("greenBold14px");
                        } else if (lclFileNumberObject[4] != null && CommonUtils.isNotEmpty(lclFileNumberObject[4].toString())) {
                            lclAddVoyageForm.setStatusMessage("POSTED");
                            lclAddVoyageForm.setClassName("purpleBold");
                        } else {
                            lclAddVoyageForm.setStatusMessage("POOL");
                            lclAddVoyageForm.setClassName("orangeBold");
                        }
                    } else {
                        lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " Not Converted To BL.");
                    }
                } else {
                    lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " POL and POD does not matches with current voyage POL and POD.");
                }
            } else {
                lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " Not Converted To BL.");
            }
        } else {
            lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " does not exists.");
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
    }

    private void pickFileNumber(LclAddVoyageForm lclAddVoyageForm,
            HttpServletRequest request, Long fileNumberId) throws Exception {
        LclUtils lclUtils = new LclUtils();
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        LclBookingPieceUnitDAO lclBookingPieceUnitDAO = new LclBookingPieceUnitDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        lclUtils.insertLCLRemarks(fileNumberId, "Picked Onto a Unit# " + lclAddVoyageForm.getUnitNo() + " of Voyage# " + lclAddVoyageForm.getScheduleNo() + "", "T", user);
        Disposition disp = new Disposition();
        DispositionDAO dispdao = new DispositionDAO();
        LclDwr lcldwr = new LclDwr();
        Date d = new Date();
        if (null != lclAddVoyageForm.getFilterByChanges() && !"".equals(lclAddVoyageForm.getFilterByChanges())) {
            Integer dispoId = lcldwr.disposDesc(lclAddVoyageForm.getFilterByChanges());
            disp = dispdao.getByProperty("id", dispoId);
            LclUnit lclUnit = new LclUnit();
            lclUnit = lclUnitDAO.getByProperty("unitNo", lclAddVoyageForm.getUnitNo());
            lclUtils.insertLclBookingDispo(fileNumberId, disp, lclUnit, null, user, new UnLocation(lclAddVoyageForm.getPolId()));
        }
        List<LclBookingPiece> lclBookingPiecesList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                LclBookingPieceUnit lclBookingPieceUnit = new LclBookingPieceUnitDAO().getByProperty("lclBookingPiece.id", lclBookingPiece.getId());
                if (lclBookingPieceUnit == null) {
                    lclBookingPieceUnit = new LclBookingPieceUnit();
                    lclBookingPieceUnit.setEnteredDatetime(d);
                    lclBookingPieceUnit.setLoadedDatetime(d);
                    lclBookingPieceUnit.setEnteredBy(getCurrentUser(request));
                }
                lclBookingPieceUnit.setLclBookingPiece(lclBookingPiece);
                lclBookingPieceUnit.setModifiedBy(getCurrentUser(request));
                lclBookingPieceUnit.setModifiedDatetime(d);
                lclBookingPieceUnit.setLclUnitSs(lclUnitSsDAO.findById(lclAddVoyageForm.getUnitssId()));
                lclBookingPieceUnitDAO.saveOrUpdate(lclBookingPieceUnit);
            }
        }
    }

    public ActionForward manifestDr(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFileNumber())) {
            LclFileNumberDAO lclFilenumberDAO = new LclFileNumberDAO();
            Long fileNumberId = lclFilenumberDAO.getFileIdByFileNumber(lclAddVoyageForm.getFileNumber());
            pickFileNumber(lclAddVoyageForm, request, fileNumberId);
            new LclExportManifestDAO().manifest(lclAddVoyageForm.getUnitssId(), getCurrentUser(request), true, fileNumberId);
            // lclUtils.updateLCLUnitSSStatus(lclAddVoyageForm.getUnitssId(), "M", getCurrentUser(request));
            setBLStatus(lclAddVoyageForm, request, lclFilenumberDAO);
        }
        return mapping.findForward(DISPLAY_ADD_MANIFESTTOBL_STATUS);
    }

    public ActionForward openManifestPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitDAO lclunitdao = new LclUnitDAO();
        String filter = request.getParameter("filterByChanges");
        lclAddVoyageForm.setFilterByChanges(filter);
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        Role role = getCurrentUser(request).getRole();
        if (null != role) {
            request.setAttribute("isManifestRole", new RoleDutyDAO().getRoleDetails("lcl_manifest_postedbl", role.getRoleId()));
        }
        request.setAttribute("pickedDrList", lclunitdao.getAllDRSForManifestByUnitSSId(lclAddVoyageForm.getUnitssId()));
        return mapping.findForward(DISPLAY_MANIFEST_POPUP);
    }

    public ActionForward viewManifestPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitDAO lclunitdao = new LclUnitDAO();
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("drList", lclunitdao.getAllDRSForViewManifestByUnitSSId(lclAddVoyageForm.getUnitssId()));
        return mapping.findForward("displayViewManifestPopup");
    }

    public ActionForward viewDRSPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
            LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(lclAddVoyageForm.getUnitssId());
            lclAddVoyageForm.setUnitNo(lclUnitSs.getLclUnit().getUnitNo());
            request.setAttribute("bookingNo", lclUnitSs.getSpBookingNo());
        }
        request.setAttribute("loginuser", loginUser.getRole().getRoleId());
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("pickedViewDrUnitList", new ExportUnitQueryUtils().getUnitViewDrList(lclAddVoyageForm.getUnitssId(), request));
        return mapping.findForward("displayViewDRsPopup");
    }

    public ActionForward changeVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyForm = (LclAddVoyageForm) form;
        User loginUser = getCurrentUser(request);

        LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
        LclUnitSs lclUnitSs = unitSsDAO.findById(addVoyForm.getUnitssId());
        String voyageNo = lclUnitSs.getLclSsHeader().getScheduleNo();
        lclUnitSs.setModifiedBy(loginUser);
        lclUnitSs.setModifiedDatetime(new Date());
        lclUnitSs.setLclSsHeader(new LclSsHeader(addVoyForm.getChangeVoyHeaderId()));
        unitSsDAO.saveOrUpdate(lclUnitSs);
        LclUnitSsRemarksDAO unitSsRemarksDAO = new LclUnitSsRemarksDAO();
        new LclUnitSsManifestDAO().updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                addVoyForm.getUnitId(), loginUser.getUserId());
        new LclUnitWhseDAO().updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                addVoyForm.getUnitId(), loginUser.getUserId());
        unitSsRemarksDAO.updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                addVoyForm.getUnitId());
        LclSsDetailDAO ssDetailDAO = new LclSsDetailDAO();
        if (CommonUtils.isNotEmpty(addVoyForm.getHeaderId())) {
            Long oldSsDetailId = ssDetailDAO.getId(addVoyForm.getHeaderId());
            Long newSsDetailId = ssDetailDAO.getId(addVoyForm.getChangeVoyHeaderId());
            new LclUnitSsDispoDAO().updateSsHeaderId(oldSsDetailId, newSsDetailId,
                    lclUnitSs.getLclUnit().getId(), loginUser.getUserId());
        }

        LclSsRemarksDAO ssRemarksDAO = new LclSsRemarksDAO();
        ssRemarksDAO.insertSsHeaderRemarks(addVoyForm.getHeaderId(), "auto",
                "Unit#" + lclUnitSs.getLclUnit().getUnitNo() + " was changed to Voyage#" + addVoyForm.getChangeVoyageNo(),
                loginUser.getUserId(), null);

        unitSsRemarksDAO.insertLclunitRemarks(addVoyForm.getChangeVoyHeaderId(), lclUnitSs.getLclUnit().getId(), "auto",
                "Voyage# changed from " + voyageNo + " to " + addVoyForm.getChangeVoyageNo(), loginUser.getUserId());

        String concatenatedFileNos = new ExportUnitQueryUtils().getPickedCargoBkgList(lclUnitSs.getId());
        if (null != concatenatedFileNos && !concatenatedFileNos.isEmpty()) {
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            List fileIdList = Arrays.asList(concatenatedFileNos.split(","));
            for (Object fileId : fileIdList) {
                lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId.toString()), "auto",
                        "Voyage# changed from " + voyageNo + " to " + addVoyForm.getChangeVoyageNo(), loginUser.getUserId());
            }
        }

        if (lclUnitSs.getCob()) {
            LclSsDetail lclSsDetail = ssDetailDAO.findByTransMode(addVoyForm.getChangeVoyHeaderId(), "V");
            if (null != lclSsDetail) {
                String vesselCode = ssDetailDAO.getVesselCode(lclSsDetail.getId());
                List concatFileNoList = new LCLBlDAO().getConcatBlByUnitSsId(addVoyForm.getUnitssId());
                if (null != concatFileNoList && !concatFileNoList.isEmpty()) {
                    TransactionDAO transactionDAO = new TransactionDAO();
                    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
                    ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
                    for (Object fileNo : concatFileNoList) {
                        transactionDAO.updateVoyageNoByLcl(fileNo.toString(), lclSsDetail.getStd(), addVoyForm.getChangeVoyageNo(),
                                vesselCode, lclSsDetail.getSta(), lclSsDetail.getSpReferenceName(),
                                lclSsDetail.getSpAcctNo().getAccountName(), loginUser.getUserId(), lclUnitSs.getSpBookingNo());
                        transactionLedgerDAO.updateVoyageNoByLcl(fileNo.toString(), lclSsDetail.getStd(), addVoyForm.getChangeVoyageNo(),
                                vesselCode, loginUser.getUserId(), lclUnitSs.getSpBookingNo());
                        arTransactionHistoryDAO.updateVoyageNoByLcl(fileNo.toString(), addVoyForm.getChangeVoyageNo(), lclSsDetail.getStd());
                    }
                }
            }
        }
        clearHeaderValues(addVoyForm);
        LclSsHeader lclssheader = new LclSsHeaderDAO().findById(addVoyForm.getHeaderId());
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, addVoyForm, loginUser);
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward addExceptionDRsPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitDAO lclunitdao = new LclUnitDAO();
        String unitExceptionFlag = request.getParameter("unitExceptionFlag");
        String addExcepPoppUpFlag = request.getParameter("addExcepPoppUpFlag");
        request.setAttribute("addExcepPoppUpFlag", addExcepPoppUpFlag);
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("drList", lclunitdao.getAllDRSForViewUnitExceptionByUnitSSId(lclAddVoyageForm.getUnitssId(), unitExceptionFlag));
        List<ManifestBean> manifestBeanList = lclunitdao.getAllDRSForViewUnitExceptionByUnitSSId(lclAddVoyageForm.getUnitssId(), "true");
        String showAllButtonFlag = null;
        for (ManifestBean manifestBean : manifestBeanList) {
            if (manifestBean.getUnitException() == null) {
                showAllButtonFlag = "true";
                break;
            }
        }
        request.setAttribute("showAllButtonFlag", showAllButtonFlag);
        return mapping.findForward(ADD_EXCEPTION_DRS_POPUP);
    }

    public ActionForward changeVoyagePopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward(CHANGE_VOYAGE_POPUP);
    }

    public ActionForward openCOBPopup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(lclAddVoyageForm.getUnitssId());
        lclAddVoyageForm.setCob(lclUnitSs.getCob() ? "Y" : "N");
        lclAddVoyageForm.setCobRemarks(CommonUtils.isNotEmpty(lclUnitSs.getCobRemarks()) ? lclUnitSs.getCobRemarks() : "");
        lclAddVoyageForm.setUnitId(lclUnitSs.getLclUnit().getId());
        lclAddVoyageForm.setHeaderId(lclUnitSs.getLclSsHeader().getId());
        lclAddVoyageForm.setUnitssId(lclUnitSs.getId());
        if (null != lclUnitSs.getCobDatetime()) {
            lclAddVoyageForm.setVerifiedEta(DateUtils.formatDate(lclUnitSs.getCobDatetime(), "dd-MMM-yyyy HH:mm:ss"));
        }
        LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
        if (null != lclSsDetail) {
            request.setAttribute("lclSsDetail", lclSsDetail);
        }
        request.setAttribute("lclUnitSs", lclUnitSs);
        if (getCurrentUser(request).getRole() != null) {
            Boolean reverseRole = new RoleDutyDAO().getRoleDetails("reversecob", getCurrentUser(request).getRole().getRoleId());
            request.setAttribute("reverseRoleDuty", reverseRole);
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward(COB_POPUP);
    }

    public ActionForward editUnitException(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;

        LclUnitDAO lclunitdao = new LclUnitDAO();
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        LclRemarks lclRemarks = new LclRemarksDAO().getLclRemarksByType(lclAddVoyageForm.getExceptionFileNumbers(), "Unit Exception");
        if (null != lclRemarks) {
            request.setAttribute("lclRemarks", lclRemarks);
        }
        request.setAttribute("drList", lclunitdao.getAllDRSForViewUnitExceptionByUnitSSId(lclAddVoyageForm.getUnitssId(), "true"));
        return mapping.findForward(ADD_EXCEPTION_DRS_POPUP);
    }

    public void manifestCob(LclUnitSs lclUnitSs, User user) throws Exception {
        new LclExportManifestDAO().manifest(lclUnitSs.getId(), user, true, null);
    }

    public void unManifestCob(LclUnitSs lclUnitSs, User user) throws Exception {
        new LclExportManifestDAO().manifest(lclUnitSs.getId(), user, false, null);
    }

    public ActionForward saveCob(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        LclUnitSs lclUnitSs = lclUnitSsDAO.findById(lclAddVoyageForm.getUnitssId());
        User user = getCurrentUser(request);
        boolean prevCobValue = lclUnitSs.getCob();
        if (null != lclUnitSs) {
            StringBuilder notesForCobModify = new StringBuilder();
            Date now = new Date();
            LclSsDetailDAO lclSsDetailDAO = new LclSsDetailDAO();
            Date verifyDtae = DateUtils.parseDate(lclAddVoyageForm.getVerifiedEta(), "dd-MMM-yyyy HH:mm:ss");
            lclUnitSs.setCobDatetime(CommonUtils.isNotEmpty(lclAddVoyageForm.getVerifiedEta())
                    ?  verifyDtae: null);
            lclUnitSs.setCobByUserId(user);
            lclUnitSs.setCobRemarks(CommonUtils.isNotEmpty(lclAddVoyageForm.getCobRemarks())
                    ? lclAddVoyageForm.getCobRemarks().toUpperCase() : null);
            lclUnitSs.setModifiedBy(user);
            lclUnitSs.setModifiedDatetime(now);

            String concatenatedFileNos = lclUnitSsDAO.getPickedBlByPosted(lclUnitSs.getId().toString());
            notesForCobModify.append(lclUnitSs.getCob() ? "BL is Reverse Confirmed on Board from the Unit#"
                    : "BL is Confirmed on Board  into the Unit# ");
            notesForCobModify.append(lclUnitSs.getLclUnit().getUnitNo()).append(" and Voyage# ");
            notesForCobModify.append(lclUnitSs.getLclSsHeader().getScheduleNo());
            if (CommonUtils.isNotEmpty(concatenatedFileNos)) {
                List fileIdList = Arrays.asList(concatenatedFileNos.split(","));
                for (Object fileId : fileIdList) {
                    new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId.toString()),
                            "auto", notesForCobModify.toString(), user.getUserId());
                }
            }
            lclUnitSs.setCob("Y".equalsIgnoreCase(lclAddVoyageForm.getCob()));
            lclUnitSsDAO.update(lclUnitSs);
            LclSsDetail lclSsDetail = lclSsDetailDAO.findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
            if (null != lclSsDetail) {
                if ("Y".equalsIgnoreCase(lclAddVoyageForm.getCob()) && !prevCobValue) {
                    new UnitSsAutoCostingDAO().calculatedByUnitAutoCost(lclUnitSs, lclUnitSs.getLclSsHeader().getOrigin().getId(),
                            lclUnitSs.getLclSsHeader().getDestination().getId(),
                            lclUnitSs.getLclUnit().getUnitType().getId(), user);
                    manifestCob(lclUnitSs, user);
                    new LclSsAcDAO().calculateUnitCostByLCLE(lclUnitSs.getLclSsHeader(), lclUnitSs,
                            lclSsDetail.getSpAcctNo().getAccountno(),
                            lclSsDetail.getDeparture().getId(), user, now, lclAddVoyageForm.getHazFlag());
                    setDipositionAfterCob(lclAddVoyageForm, lclSsDetail, lclUnitSs, user, true);
                } else if (lclAddVoyageForm.getCob().equalsIgnoreCase("N") && prevCobValue) {
                    setDipositionAfterCob(lclAddVoyageForm, lclSsDetail, lclUnitSs, user, false);
                    unManifestCob(lclUnitSs, user);
                }
            }
        }
        LclSsHeader lclssheader = lclUnitSs.getLclSsHeader();
        request.setAttribute("openPopup", "notopen");
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, user);
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    /**
     * This method is used to set disposition in DRs and Unit after COB *
     */
    public void setDipositionAfterCob(LclAddVoyageForm voyageForm, LclSsDetail lclSsDetail,
            LclUnitSs lclUnitSs, User user, boolean flag) throws Exception {
        LclBookingDispoDAO lclBoookingDispo = new LclBookingDispoDAO();
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        Disposition bkgDispo = new Disposition();
        ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        List<ManifestBean> pickedDrList = new ExportUnitQueryUtils().getPickedDrList(lclUnitSs.getId());
        String vesselCode = new LclSsDetailDAO().getVesselCode(lclSsDetail.getId());
        for (ManifestBean picked : pickedDrList) {
            transactionLedgerDAO.updateLclEAccruals(picked.getFileNo(), picked.getBlNo(), lclUnitSs.getLclSsHeader().getScheduleNo(),
                    lclUnitSs.getSpBookingNo(), voyageForm.getPooOrigin(), voyageForm.getPodDestination(),
                    lclUnitSs.getLclUnit().getUnitNo(), lclSsDetail.getStd(), vesselCode);
            if (flag) {
                notificationDAO.insertNotification(picked.getFileId(), "COB",
                        "Pending", null, user.getUserId());
            }
            //notificationDAO.insertCodeJBkgContactsByDispo(picked.getFileId(), user.getUserId());
            String dispo = lclUnitSsDAO.getPreviousDispoOfFileNumber(picked.getFileId());
            if (null != dispo && !dispo.equalsIgnoreCase("INTR")) {
                bkgDispo = new DispositionDAO().getByProperty("eliteCode", voyageForm.getCob().equals("Y") ? "VSAL" : "RCVD");
                Integer currentLocId = lclUnitSs.getLclSsHeader().getOrigin().getId();
                Integer wareHouseId = new WarehouseDAO().getWarehouseId(new UnLocationDAO().findById(currentLocId) != null
                        ? new UnLocationDAO().findById(currentLocId).getUnLocationCode() : "", "W");
                if (bkgDispo.getEliteCode().equalsIgnoreCase("RCVD")) {
                    lclBoookingDispo.insertBookingDispoForRCVD(picked.getFileId(), bkgDispo.getId(), null,
                            null, user.getUserId(), currentLocId, CommonUtils.isNotEmpty(wareHouseId) ? wareHouseId : null);
                } else {
                    lclBoookingDispo.insertBookingDispo(picked.getFileId(), bkgDispo.getId(), lclUnitSs.getLclUnit().getId().intValue(),
                            lclSsDetail.getId().intValue(), user.getUserId(), null);
                }
            } else { // this may possible when they pick the Drs in INTR Disposition.
                bkgDispo = new DispositionDAO().getByProperty("eliteCode", voyageForm.getCob().equals("Y") ? "VSAL" : "INTR");
                new LclBookingDispoDAO().insertBookingDispo(picked.getFileId(), bkgDispo.getId(), lclUnitSs.getLclUnit().getId().intValue(),
                        lclSsDetail.getId().intValue(), user.getUserId(), null);
            }
        }
        bkgDispo = new DispositionDAO().getByProperty("eliteCode", voyageForm.getCob().equals("Y")
                && !voyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic") ? "VSAL" : "LDEX");
        new LclUnitSsDispoDAO().insertLclUnitSsDisposition(lclUnitSs.getLclUnit().getId(), lclSsDetail.getId(), bkgDispo.getId(), user.getUserId());
    }

    public ActionForward saveUnitException(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitDAO lclunitdao = new LclUnitDAO();
        String fileNumberIds = lclAddVoyageForm.getExceptionFileNumbers();
        String drNumber = "";
        String note = "";
        for (StringTokenizer stringTokenizer = new StringTokenizer(fileNumberIds, ","); stringTokenizer.hasMoreTokens();) {
            String id = stringTokenizer.nextToken();
            LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(new Long(id));
            if (!"".equals(drNumber) && null != lclFileNumber) {
                if (null != lclFileNumber.getLclBooking() && null != lclFileNumber.getLclBooking().getPortOfLoading()) {
                    drNumber = drNumber + "," + lclFileNumber.getLclBooking().getPortOfLoading().getUnLocationCode().substring(2, 5) + "-" + lclFileNumber.getFileNumber();
                } else {
                    drNumber = drNumber + "," + lclFileNumber.getFileNumber();
                }
            } else {
                if (null != lclFileNumber.getLclBooking() && null != lclFileNumber.getLclBooking().getPortOfLoading()) {
                    drNumber = lclFileNumber.getLclBooking().getPortOfLoading().getUnLocationCode().substring(2, 5) + "-" + lclFileNumber.getFileNumber();
                } else {
                    drNumber = lclFileNumber.getFileNumber();
                }
            }
            LclRemarks lclRemarks = new LclRemarksDAO().getLclRemarksByType(id, "Unit Exception");
            if (null != lclRemarks) {
                note = "Exception Updated for DR " + drNumber + " from " + lclRemarks.getRemarks() + " ->" + lclAddVoyageForm.getUnitException();
                lclRemarks.setRemarks(lclAddVoyageForm.getUnitException());
                new LclRemarksDAO().update(lclRemarks);
            } else {
                lclRemarks = new LclRemarks();
                if (null != lclFileNumber) {
                    User user = (User) request.getSession().getAttribute("loginuser");
                    lclRemarks.setLclFileNumber(lclFileNumber);
                    lclRemarks.setEnteredDatetime(new Date());
                    lclRemarks.setEnteredBy(user);
                    lclRemarks.setModifiedDatetime(new Date());
                    lclRemarks.setModifiedBy(user);
                    lclRemarks.setRemarks(lclAddVoyageForm.getUnitException());
                    lclRemarks.setType("Unit Exception");
                    new LclRemarksDAO().save(lclRemarks);
                }
            }
        }
        if ("".equals(note) && !"".equals(drNumber)) {
            note = "Exception Added for DR " + drNumber + " " + lclAddVoyageForm.getUnitException();
        }
        lclAddVoyageForm.setRemarks(note);
        addNotes(lclAddVoyageForm, request);
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("drList", lclunitdao.getAllDRSForViewUnitExceptionByUnitSSId(lclAddVoyageForm.getUnitssId(), "true"));
        return mapping.findForward(ADD_EXCEPTION_DRS_POPUP);
    }

    public void addNotes(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request) throws Exception {
        LclUnitSsRemarks lclunitSsRemarks = new LclUnitSsRemarks();
        lclunitSsRemarks.setLclSsHeader(new LclSsHeader(new Long(lclAddVoyageForm.getHeaderId())));
        lclunitSsRemarks.setLclUnit(new LclUnit(new Long(lclAddVoyageForm.getUnitId())));
        lclunitSsRemarks.setType("Unit Exception");
        lclunitSsRemarks.setRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
        lclunitSsRemarks.setEnteredBy(getCurrentUser(request));
        lclunitSsRemarks.setEnteredDatetime(new Date());
        lclunitSsRemarks.setModifiedby(getCurrentUser(request));
        lclunitSsRemarks.setModifiedDatetime(new Date());
        new LclUnitSsRemarksDAO().save(lclunitSsRemarks);
    }

    public ActionForward voyageEdiTracking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        String _304IntraPath = LoadLogisoftProperties.getProperty("lcl.inttra.xmlLocation");
        request.setAttribute("_304IntraPath", _304IntraPath);
        String _304GtnexusPath = LoadLogisoftProperties.getProperty("lcl.gtnexus.xmlLocation");
        request.setAttribute("_304GtnexusPath", _304GtnexusPath);
        LclSSMasterBl masterBl = new LclSSMasterBlDAO().findById(addVoyageForm.getMasterId());
        List logFileEdiList = logFileEdiDAO.findByBookingNo(masterBl.getSpBookingNo(), masterBl.getLclSsHeader().getScheduleNo());
        request.setAttribute("editList", logFileEdiList);
        return mapping.findForward("ediUnitTracking");
    }

    public ActionForward validateChargesBillToParty(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        String errorMessage = "";
        LclUtils utils = new LclUtils();
        errorMessage = utils.getBillingTypeErrorMessage(lclAddVoyageForm.getUnitssId(), "Exports", "Y");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward isCorrectionFound(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        String errorMessage = "";
        LclUtils utils = new LclUtils();
        if (utils.isCorrectionFound(lclAddVoyageForm.getUnitssId().toString())) {
            errorMessage = "Please void and delete all corrections before Reverse Acct";
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward saveUnassignUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUtils lclUtils = new LclUtils();
        LclUnitDAO lclunitdao = new LclUnitDAO();
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        LclUnit lclunit = null;
        LclUnitSs lclunitss = null;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        Date d = new Date();
        User user = getCurrentUser(request);
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        if (lclAddVoyageForm.getUnitId() != null && lclAddVoyageForm.getUnitId() > 0) {
            lclunit = lclunitdao.findById(lclAddVoyageForm.getUnitId());
            if (lclAddVoyageForm.getUnitsReopened() != null && lclAddVoyageForm.getUnitsReopened().equalsIgnoreCase("Y")) {
                /* lclunitss = new LclUnitSs();
                 lclunitss.setEnteredBy(getCurrentUser(request));
                 lclunitss.setEnteredDatetime(d);
                 lclunitss.setLclUnit(lclunit);
                 setLclUnitSS(lclunitss, lclssheader, request, d);
                 lclunitssdao.save(lclunitss);*/
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOldUnitId()) && lclssheader != null) {
                    lclunitss = lclunitssdao.getLclUnitSSByLclUnitHeader(new Long(lclAddVoyageForm.getOldUnitId()), lclssheader.getId());
                }
            } else {
                lclunitss = lclunitssdao.getLclUnitSSByLclUnitHeader(lclAddVoyageForm.getUnitId(), lclssheader.getId());
            }
            lclunit.setModifiedBy(getCurrentUser(request));
            lclunit.setModifiedDatetime(d);
        } else {
            lclunit = new LclUnit();
            lclunit.setEnteredBy(getCurrentUser(request));
            lclunit.setModifiedBy(getCurrentUser(request));
            lclunit.setEnteredDatetime(d);
            lclunit.setModifiedDatetime(d);
            if (lclunitss == null) {
                lclunitss = new LclUnitSs();
                lclunitss.setEnteredBy(getCurrentUser(request));
                lclunitss.setEnteredDatetime(d);
                lclunitss.setLclUnit(lclunit);
            }
            setLclUnitSS(lclunitss, lclssheader, request, d);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitNo())) {
            //************Code written for making a note when unitNo is being changed
            if (CommonUtils.isNotEmpty(lclunit.getUnitNo())) {
                if (lclunit.getUnitNo().toUpperCase() != lclAddVoyageForm.getUnitNo().toUpperCase()) {
                    String fileNumbers = "";
                    if (lclunitss != null && lclunitss.getId() != null) {
                        fileNumbers = lclunitdao.getUnitNotConvertedFileNumbers(lclunitss.getId().toString());
                    }
                    String drNumber = "";
                    String note = "";
                    for (StringTokenizer stringTokenizer = new StringTokenizer(fileNumbers, ","); stringTokenizer.hasMoreTokens();) {
                        String id = stringTokenizer.nextToken();
                        LclFileNumber lclFileNumber = new LclFileNumberDAO().getLclFileNumber(id);
                        if (!"".equals(drNumber) && null != lclFileNumber) {
                            if (null != lclFileNumber.getLclBooking() && null != lclFileNumber.getLclBooking().getPortOfLoading()) {
                                drNumber = drNumber + "," + lclFileNumber.getLclBooking().getPortOfLoading().getUnLocationCode().substring(2, 5) + "-" + lclFileNumber.getFileNumber();
                            } else {
                                drNumber = drNumber + "," + lclFileNumber.getFileNumber();
                            }
                        } else {
                            if (null != lclFileNumber.getLclBooking() && null != lclFileNumber.getLclBooking().getPortOfLoading()) {
                                drNumber = lclFileNumber.getLclBooking().getPortOfLoading().getUnLocationCode().substring(2, 5) + "-" + lclFileNumber.getFileNumber();
                            } else {
                                drNumber = lclFileNumber.getFileNumber();
                            }
                        }
                        LclRemarks lclRemarks = new LclRemarksDAO().getLclRemarksByType(lclFileNumber.getId().toString(), "Unit No Updated");
                        note = "Unit No Updated for DR " + drNumber + " from " + lclunit.getUnitNo() + " ->" + lclAddVoyageForm.getUnitNo();
                        if (null != lclRemarks) {
                            lclRemarks.setRemarks(note);
                            new LclRemarksDAO().update(lclRemarks);
                        } else {
                            lclRemarks = new LclRemarks();
                            if (null != lclFileNumber) {

                                lclRemarks.setLclFileNumber(lclFileNumber);
                                lclRemarks.setEnteredDatetime(new Date());
                                lclRemarks.setEnteredBy(user);
                                lclRemarks.setModifiedDatetime(new Date());
                                lclRemarks.setModifiedBy(user);
                                lclRemarks.setRemarks(note);
                                lclRemarks.setType("Unit No Updated");
                                new LclRemarksDAO().save(lclRemarks);
                            }
                        }
                    }
                    if ("".equals(note) && !"".equals(drNumber)) {
                        note = "Exception Added for DR " + drNumber + " " + lclAddVoyageForm.getUnitNo();
                    }
                    lclAddVoyageForm.setRemarks(note);
                }
            }
            //******************************************************************
            lclunit.setUnitNo(lclAddVoyageForm.getUnitNo().toUpperCase());
        }
        UnitTypeDAO unittypedao = new UnitTypeDAO();
        UnitType unittype = unittypedao.findById(lclAddVoyageForm.getUnitType());
        lclunit.setUnitType(unittype);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatPermittedUnit())) {
            if (lclAddVoyageForm.getHazmatPermittedUnit().equalsIgnoreCase("Y")) {
                lclunit.setHazmatPermitted(true);
            } else {
                lclunit.setHazmatPermitted(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getRefrigerationPermittedUnit())) {
            if (lclAddVoyageForm.getRefrigerationPermittedUnit().equalsIgnoreCase("Y")) {
                lclunit.setRefrigerated(true);
            } else {
                lclunit.setRefrigerated(false);
            }
        }
        if (lclAddVoyageForm.getRemarks() != null && !lclAddVoyageForm.getRemarks().trim().equals("")) {
            lclunit.setRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
        } else {
            lclunit.setRemarks("");
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBookingNumber())) {
            lclunitss.setSpBookingNo(lclAddVoyageForm.getBookingNumber());
        } else {
            lclunitss.setSpBookingNo("");
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getIntermodalProvided())) {
            if (lclAddVoyageForm.getIntermodalProvided().equalsIgnoreCase("Y")) {
                lclunitss.setIntermodalProvided(true);
            } else {
                lclunitss.setIntermodalProvided(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDrayageProvided())) {
            if (lclAddVoyageForm.getDrayageProvided().equalsIgnoreCase("Y")) {
                lclunitss.setDrayageProvided(true);
            } else {
                lclunitss.setDrayageProvided(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStopoff())) {
            if (lclAddVoyageForm.getStopoff().equalsIgnoreCase("Y")) {
                lclunitss.setStopOff(true);
            } else {
                lclunitss.setStopOff(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getChasisNo())) {
            lclunitss.setChassisNo(lclAddVoyageForm.getChasisNo().toUpperCase());
        } else {
            lclunitss.setChassisNo("");
        }
        lclunitss.setLclUnit(lclunit);
        List<LclUnitSs> lclUnitSsList = new ArrayList<LclUnitSs>();
        lclUnitSsList.add(lclunitss);
        lclunit.setLclUnitSsList(lclUnitSsList);
        if (lclAddVoyageForm.getUnitId() != null && lclAddVoyageForm.getUnitId() > 0) {
            lclunitdao.update(lclunit);
        } else {
            lclunitdao.save(lclunit);
        }
        lclunitssdao.saveOrUpdate(lclunitss);
        if (null != lclAddVoyageForm.getFilterByChanges() && CommonUtils.isEmpty(lclAddVoyageForm.getUnitId())) {
            LclDwr lcldwr = new LclDwr();
            Integer dispoId = lcldwr.disposDesc(lclAddVoyageForm.getFilterByChanges());
            Long ssDetailsId = new LclSsDetailDAO().getIdbyAsc(lclunitss.getLclSsHeader().getId());
            if (null != lclunit && CommonUtils.isNotEmpty(ssDetailsId)) {
                new LclUnitSsDispoDAO().insertLclUnitSsDisposition(lclunit.getId(), ssDetailsId,
                        dispoId, user.getUserId());
            }
        }
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, user);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSearchLoadDisplay())) {
            request.setAttribute("searchLoadDisplay", "empty");
        }
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward(DISPLAY_SCHEDULE);
    }

    public void addingStopOff(String unLocationId, LclSession lclSession,
            LclAddVoyageForm lclAddVoyageForm, String index) throws Exception {
        StopoffsBean bean = new StopoffsBean();
        if (CommonUtils.isNotEmpty(unLocationId)) {
            List<StopoffsBean> sessionList = new ArrayList();
            List<StopoffsBean> stopAddList = new ArrayList();
            UnLocation unlocation = new UnLocationDAO().findById(Integer.parseInt(unLocationId));
            if (CommonUtils.isNotEmpty(lclSession.getStopOffList())) {
                sessionList.addAll(lclSession.getStopOffList());
            }
            stopAddList.addAll(sessionList);
            if (CommonUtils.isNotEmpty(index)) {
                bean.setUnlocationId(unlocation.getId());
                bean.setCountryValue(unlocation.getUnLocationName());
                bean.setWareHouse(lclAddVoyageForm.getWarehouseName());
                bean.setStdDate(DateUtils.parseDate(lclAddVoyageForm.getStopOffETD(), "dd-MMM-yyyy"));
                bean.setStaDate(DateUtils.parseDate(lclAddVoyageForm.getStopOffETA(), "dd-MMM-yyyy"));
                bean.setWarehouseId(lclAddVoyageForm.getWarehouseId());
                bean.setRemarks(lclAddVoyageForm.getStopOffRemarks().toUpperCase());
                stopAddList.add(Integer.parseInt(index), bean);
            } else {
                bean.setUnlocationId(unlocation.getId());
                bean.setCountryValue(unlocation.getUnLocationName());
                bean.setWareHouse(lclAddVoyageForm.getWarehouseName());
                bean.setStdDate(DateUtils.parseDate(lclAddVoyageForm.getStopOffETD(), "dd-MMM-yyyy"));
                bean.setStaDate(DateUtils.parseDate(lclAddVoyageForm.getStopOffETA(), "dd-MMM-yyyy"));
                bean.setWarehouseId(lclAddVoyageForm.getWarehouseId());
                bean.setRemarks(lclAddVoyageForm.getStopOffRemarks().toUpperCase());
                stopAddList.add(bean);
            }
            // clearing current date that shown in Jsp
            lclSession.setStopOffList(stopAddList);
        }
    }

    public void removingStopOff(LclAddVoyageForm lclAddVoyageForm,
            LclSession lclSession, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            List<StopoffsBean> removeStopAddList = lclSession.getStopOffList();
            for (int i = 0; i < removeStopAddList.size(); i++) {
                if (CommonUtils.isNotEmpty(removeStopAddList.get(i).getDetailId())) {
                    if (Long.parseLong(removeStopAddList.get(i).getDetailId().toString()) == lclAddVoyageForm.getDetailId()) {
                        removeStopAddList.get(i).setAddOrRemove(false);
                    }
                    lclSession.setStopOffList(removeStopAddList);
                }
            }
        } else {
            String unlocationId = request.getParameter("delLocId");
            Iterator<StopoffsBean> stopAddList = lclSession.getStopOffList().iterator();
            while (stopAddList.hasNext()) {
                StopoffsBean unlocationBean = stopAddList.next();
                if (unlocationId.equals(unlocationBean.getUnlocationId().toString())) {
                    stopAddList.remove();
                }
            }
        }
    }

    public ActionForward addStopOff(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        HttpSession session = request.getSession();
        String forwardPage = "";
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();

        String unLocationId = request.getParameter("unLocationId");
        String index = request.getParameter("stopOffIndex");
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getButtonValue())) {

            if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("stopOff")) {
                addingStopOff(unLocationId, lclSession, lclAddVoyageForm, index);
                forwardPage = "lclVoyageStopOff";
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("addVoyageStopOff")) {
                addingStopOff(unLocationId, lclSession, lclAddVoyageForm, index);
                forwardPage = "openStopOff";
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("removeStopOff")) {
                removingStopOff(lclAddVoyageForm, lclSession, request);
                forwardPage = "lclVoyageStopOff";
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("removeVoyageStopOff")) {
                removingStopOff(lclAddVoyageForm, lclSession, request);
                forwardPage = "openStopOff";
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("clearStopOff")) {
                forwardPage = "openStopOff";
                LclSsHeader lclssheader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
                setStopOffList(lclssheader, request);
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("updateStopOff")) {
                LclSsHeader lclssheader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
                updateStopOffDetail(lclAddVoyageForm, lclssheader, request);
                forwardPage = "openStopOff";
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("openStopOff")) {
                forwardPage = "openStopOff";
                LclSsHeader lclssheader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
                setStopOffList(lclssheader, request);
            }

        }

        if (CommonUtils.isNotEmpty(lclSession.getStopOffList())) {
            request.setAttribute("stopAddList", lclSession.getStopOffList());
        }

        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward(forwardPage);
    }

    public void setStopOffList(LclSsHeader lclssheader, HttpServletRequest request) throws Exception {
        List<LclSsDetail> lcldetailList = lclssheader != null ? lclssheader.getLclSsDetailList() : null;
        List<StopoffsBean> stopOffList = new ArrayList();
        if (CommonUtils.isNotEmpty(lcldetailList)) {
            for (int i = 1; i < lcldetailList.size(); i++) {
                StopoffsBean bean = new StopoffsBean();
                bean.setCountryValue(lcldetailList.get(i).getDeparture().getUnLocationName());
                bean.setUnlocationId(lcldetailList.get(i).getDeparture().getId());
                bean.setDetailId(lcldetailList.get(i).getId());
                bean.setAddOrRemove(true);
                bean.setWareHouse(lcldetailList.get(i).getStuffingWarehouse() != null ? lcldetailList.get(i).getStuffingWarehouse().getWarehouseName() : "");
                bean.setWarehouseId(lcldetailList.get(i).getStuffingWarehouse() != null ? lcldetailList.get(i).getStuffingWarehouse().getId().longValue() : null);
                bean.setStdDate(lcldetailList.get(i).getStd() != null ? lcldetailList.get(i).getStd() : null);
                bean.setStaDate(lcldetailList.get(i).getSta() != null ? lcldetailList.get(i).getSta() : null);
                bean.setRemarks(lcldetailList.get(i).getRemarks() != null ? lcldetailList.get(i).getRemarks().toUpperCase() : "");
                stopOffList.add(bean);
            }
        }
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setStopOffList(stopOffList);
    }

    public void updateStopOffDetail(LclAddVoyageForm lclAddVoyageForm,
            LclSsHeader lclSsHeader, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<StopoffsBean> stopOffList = lclSession.getStopOffList();
        List<LclSsDetail> lclSsDetailList = new LclSsDetailDAO().getLclDetailList(lclSsHeader.getId());

        for (int i = 0; i < stopOffList.size(); i++) {
            if (lclSsDetailList.size() > i) {
                stopOffList.get(i).setDetailId(lclSsDetailList.get(i).getId());
            } else {
                stopOffList.get(i).setDetailId(null);
            }
        }

        if (CommonUtils.isNotEmpty(stopOffList)) {
            Iterator<StopoffsBean> stopOffs = stopOffList.iterator();
            while (stopOffs.hasNext()) {
                StopoffsBean unlocationBean = stopOffs.next();
                if (!unlocationBean.isAddOrRemove()) {
                    stopOffs.remove();
                }
            }
        }

        if (CommonUtils.isNotEmpty(stopOffList)) {
            List<StopoffsBean> reArrangeStopOffList = stopOffList;
            UnLocationDAO unlocationdao = new UnLocationDAO();
            UnLocation departure = null;
            UnLocation arrival = null;
            LclSsDetail lclssdetail = null;

            for (int i = 0; i <= reArrangeStopOffList.size(); i++) {
                LclSsDetail lclSsDetail = null;
                if (lclSsDetailList.size() > i) {
                    if (CommonUtils.isNotEmpty(lclSsDetailList.get(i).getId())) {
                        lclSsDetail = new LclSsDetailDAO().findById(lclSsDetailList.get(i).getId());
                        lclAddVoyageForm.setDetailId(lclSsDetail.getId());
                    }
                } else {
                    lclSsDetail = new LclSsDetailDAO().findById(lclSsDetailList.get(0).getId());
                    lclAddVoyageForm.setDetailId(null);
                }
                lclAddVoyageForm.setEtaPod(lclSsDetail.getSta() != null ? DateUtils.formatDate(lclSsDetail.getSta(), "dd-MMM-yyyy").toString() : "");
                lclAddVoyageForm.setTransMode(lclSsDetail.getTransMode() != null ? lclSsDetail.getTransMode() : "");
                lclAddVoyageForm.setStd(lclSsDetail.getStd() != null ? DateUtils.formatDate(lclSsDetail.getStd(), "dd-MMM-yyyy").toString() : "");
                lclAddVoyageForm.setSpReferenceNo(CommonUtils.isNotEmpty(lclSsDetail.getSpReferenceNo()) ? lclSsDetail.getSpReferenceNo() : "");
                lclAddVoyageForm.setSpReferenceName(CommonUtils.isNotEmpty(lclSsDetail.getSpReferenceName()) ? lclSsDetail.getSpReferenceName() : "");
                lclAddVoyageForm.setSslDocsCutoffDate(lclSsDetail.getAtd() != null ? DateUtils.formatDate(lclSsDetail.getAtd(), "dd-MMM-yyyy").toString() : "");
                lclAddVoyageForm.setTtOverrideDays(null);
                lclAddVoyageForm.setLrdOverrideDays(null);
                lclAddVoyageForm.setLoadLrdt(null);
                lclAddVoyageForm.setHazmatLrdt(null);

                if (i == 0) {
                    lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
                    lclssdetail.setLclSsHeader(lclSsHeader);
                    arrival = unlocationdao.findById(reArrangeStopOffList.get(i).getUnlocationId());
                    lclssdetail.setArrival(arrival);
                    departure = unlocationdao.findById(lclSsHeader.getOrigin().getId());
                    lclssdetail.setDeparture(departure);
                    String unloctionCode = lclSsHeader.getOrigin() != null ? lclSsHeader.getOrigin().getUnLocationCode() : "";
                    RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unloctionCode, "Y");
                    Warehouse wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
                    lclssdetail.setStuffingWarehouse(wareHouse != null ? wareHouse : null);
                    lclssdetail.setSta(stopOffList.get(i).getStdDate());
                    lclssdetail.setDetailStatus("Open");
                } else if (reArrangeStopOffList.size() == i) {
                    lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
                    lclssdetail.setLclSsHeader(lclSsHeader);
                    departure = unlocationdao.findById(reArrangeStopOffList.get(i - 1).getUnlocationId());
                    lclssdetail.setDeparture(departure);
                    arrival = unlocationdao.findById(lclSsHeader.getDestination().getId());
                    lclssdetail.setArrival(arrival);
                    lclssdetail.setStuffingWarehouse(new WarehouseDAO().findById(stopOffList.get(i - 1).getWarehouseId().intValue()));
                    lclssdetail.setStd(stopOffList.get(i - 1).getStdDate());
                    lclssdetail.setSta(stopOffList.get(i - 1).getStaDate());
                    lclssdetail.setRemarks(stopOffList.get(i - 1).getRemarks());
                    lclssdetail.setDetailStatus("Open");
                } else {
                    lclssdetail = addLclSsDetails(lclAddVoyageForm, request);
                    lclssdetail.setLclSsHeader(lclSsHeader);
                    departure = unlocationdao.findById(reArrangeStopOffList.get(i - 1).getUnlocationId());
                    lclssdetail.setDeparture(departure);
                    arrival = unlocationdao.findById(reArrangeStopOffList.get(i).getUnlocationId());
                    lclssdetail.setArrival(arrival);
                    lclssdetail.setStuffingWarehouse(new WarehouseDAO().findById(stopOffList.get(i - 1).getWarehouseId().intValue()));
                    lclssdetail.setStd(stopOffList.get(i - 1).getStdDate());
                    lclssdetail.setSta(stopOffList.get(i - 1).getStaDate());
                    lclssdetail.setRemarks(stopOffList.get(i - 1).getRemarks());
                    lclssdetail.setDetailStatus("Open");
                }
                new LclSsDetailDAO().saveOrUpdate(lclssdetail);
            }
        } else {
            new LclUnitSsDAO().updateStopOffsForDomestic(lclSsHeader.getId().toString());
        }
        if (stopOffList.size() < lclSsDetailList.size() || stopOffList.isEmpty()) {
            for (int j = stopOffList.size() + 1; j < lclSsDetailList.size(); j++) {
                LclSsDetail lclSsDetail = new LclSsDetailDAO().findById(lclSsDetailList.get(j).getId());
                deleteInlandVoyageDetails(lclAddVoyageForm, lclSsDetail, request);
            }
        }
    }

    public ActionForward sendVoyageNotification(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        User user = getCurrentUser(request);
        request.setAttribute("changedFields", CommonUtils.isNotEmpty(lclAddVoyageForm.getChangedFields()) ? lclAddVoyageForm.getChangedFields() : null);
        boolean Shipper = lclAddVoyageForm.isShipper() ? true : false;
        boolean Consignee = lclAddVoyageForm.isConsignee() ? true : false;
        boolean Forwarder = lclAddVoyageForm.isForwarder() ? true : false;
        boolean Notify = lclAddVoyageForm.isNotify() ? true : false;
        boolean BookingContact = lclAddVoyageForm.isBookingContact() ? true : false;
        boolean econoEmployees = lclAddVoyageForm.isInternalEmployees() ? true : false;
        boolean portAgent = lclAddVoyageForm.isPortAgent() ? true : false;
        String voyageReason = lclAddVoyageForm.getVoyageChangeReason() != null ? lclAddVoyageForm.getVoyageChangeReason() : "";
        Integer reasonCode = lclAddVoyageForm.getVoyageReasonId() != null ? Integer.parseInt(lclAddVoyageForm.getVoyageReasonId()) : null;
        request.setAttribute("reasonCodeList", new LclExportsVoyageNotificationDAO().reasonCodesorVoyageNotification("EXPORT VOYAGE REASON CODES"));
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId()) && CommonUtils.isNotEmpty(lclAddVoyageForm.getChangedFields())) {
            // using same method in LclExportsVoyageNotificationAction.java
            new LclExportsVoyageNotificationDAO().saveLclExportVoyageNotification(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm, Shipper, Consignee, Forwarder, Notify, BookingContact, econoEmployees, portAgent, lclAddVoyageForm.getChangedFields(), reasonCode, voyageReason, "pending", user);
        }
        return mapping.findForward("voyageNotification");
    }

    public ActionForward displayArrivalLocation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        request.setAttribute("arrivalCityList", new LclSsDetailDAO().getLclDetailArrivalCities(null != lclAddVoyageForm.getHeaderId() ? lclAddVoyageForm.getHeaderId() : null));
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("arrivalLocationPopUp");
    }

    public void saveUnitsForInlandVoyage(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request,
            LclSsHeader lclssheader) throws Exception {
        LclUnit lclunit = null;
        Date d = new Date();
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitNo())) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            lclunit = lclunitdao.getUnit(lclAddVoyageForm.getUnitNo());
        }
        if (lclunit == null) {
            lclunit = new LclUnit();
            lclunit.setEnteredBy(getCurrentUser(request));
            lclunit.setEnteredDatetime(d);
        }
        lclunit.setUnitNo(!CommonUtils.isEmpty(lclAddVoyageForm.getUnitNo()) ? lclAddVoyageForm.getUnitNo().toUpperCase() : "");
        lclunit.setModifiedBy(getCurrentUser(request));
        lclunit.setModifiedDatetime(d);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatPermitted())) {
            lclunit.setHazmatPermitted(lclAddVoyageForm.getHazmatPermitted().equalsIgnoreCase("Y") ? true : false);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getRefrigerationPermittedUnit())) {
            lclunit.setRefrigerated(lclAddVoyageForm.getRefrigerationPermittedUnit().equalsIgnoreCase("Y") ? true : false);
        }
        lclunit.setRemarks(!CommonUtils.isEmpty(lclAddVoyageForm.getUnitTruckRemarks()) ? lclAddVoyageForm.getUnitTruckRemarks() : "");
        UnitType unitType = new UnitTypeDAO().findById(lclAddVoyageForm.getUnitType());
        lclunit.setUnitType(unitType);
        new LclUnitDAO().saveOrUpdate(lclunit);

        lclunit = new LclUnitDAO().getUnit(lclAddVoyageForm.getUnitNo());
        LclUnitSs lclunitss = new LclUnitSsDAO().getByProperty("lclUnit.id", lclunit.getId());
        if (lclunitss == null) {
            lclunitss = new LclUnitSs();
            lclunitss.setEnteredBy(loginUser);
            lclunitss.setEnteredDatetime(d);
            lclunitss.setLclUnit(lclunit);
        }
        setLclUnitSS(lclunitss, lclssheader, request, d);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStopoff())) {
            lclunitss.setStopOff(lclAddVoyageForm.getStopoff().equalsIgnoreCase("Y") ? true : false);
        }
        lclunitss.setChassisNo(!CommonUtils.isEmpty(lclAddVoyageForm.getChasisNo()) ? lclAddVoyageForm.getChasisNo() : "");
        new LclUnitSsDAO().saveOrUpdate(lclunitss);

        Integer dispoId = new LclDwr().disposDesc(lclAddVoyageForm.getFilterByChanges());
        Long ssDetailsId = new LclSsDetailDAO().getIdbyAsc(lclunitss.getLclSsHeader().getId());
        if (null != lclunit && CommonUtils.isNotEmpty(ssDetailsId)) {
            new LclUnitSsDispoDAO().insertLclUnitSsDisposition(lclunit.getId(), ssDetailsId,
                    dispoId, loginUser.getUserId());
            //lclUtils.insertLclUnitSsDispo(lclUnitId, ssDetailList, dispoId, getCurrentUser(request));
            LclUnitWhseDAO unitWhseDAO = new LclUnitWhseDAO();
            LclUnitWhse unitWhse = unitWhseDAO.createInstance(lclunit, lclssheader, loginUser, d);
            unitWhse.setStuffedByUser(null);
            unitWhseDAO.saveOrUpdate(unitWhse);
            //lclUtils.insertLclUnitWhse(lclunit, lclssheader, getCurrentUser(request), null); // adding warehouse default depand upon the voyage origin
        }

    }
    // for Add Unit Button in LCL Domestic Voyage 

    public ActionForward addUnitForDomesticVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = new LclAddVoyageForm();
        if (!"".equalsIgnoreCase(request.getParameter("unitId")) && null != request.getParameter("unitId")) {
            LclUnit lclunit = new LclUnitDAO().findById(Long.parseLong(request.getParameter("unitId")));
            lclAddVoyageForm.setUnitNo(lclunit.getUnitNo());
            lclAddVoyageForm.setUnitType(lclunit.getUnitType().getId());
            lclAddVoyageForm.setHazmatPermitted(lclunit.getHazmatPermitted() ? "Y" : "N");
            lclAddVoyageForm.setRefrigerationPermitted(lclunit.getRefrigerated() ? "Y" : "N");
            LclUnitSs lclunitss = new LclUnitSsDAO().getByProperty("lclUnit.id", lclunit.getId());
            lclAddVoyageForm.setChasisNo(lclunitss.getChassisNo());
            lclAddVoyageForm.setStopoff(lclunitss.getStopOff() ? "Y" : "N");
            lclAddVoyageForm.setUnitTruckRemarks(lclunit.getRemarks() != null ? lclunit.getRemarks() : "");
        } else {
            lclAddVoyageForm.setUnitNo(request.getParameter("unitNo"));
            lclAddVoyageForm.setUnitType(!"".equalsIgnoreCase(request.getParameter("unitType")) ? Long.parseLong(request.getParameter("unitType")) : 0);
            lclAddVoyageForm.setStopoff(request.getParameter("stopoff"));
            lclAddVoyageForm.setHazmatPermitted(request.getParameter("hazmat"));
            lclAddVoyageForm.setRefrigerationPermitted(request.getParameter("refrigeration"));
            lclAddVoyageForm.setUnitTruckRemarks(request.getParameter("unitTruckRemarks"));
            lclAddVoyageForm.setChasisNo(request.getParameter("chasisNo"));
        }
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        if (CommonUtils.isNotEmpty(lclSession.getStopOffList())) {
            lclAddVoyageForm.setStopoff("Y");
        }

        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("addUnitForDomesticVoyage");
    }

    public void checkSSMasterOldNew(LclSSMasterBl lclSSMasterBl,
            LclAddVoyageForm addVoyageForm, User user) throws Exception {
        String remarks = "";
        Date date = new Date();
        LclSsRemarksDAO lclSsRemarksDAO = new LclSsRemarksDAO();
        if (lclSSMasterBl != null && lclSSMasterBl.getShipSsContactId() != null && CommonUtils.isNotEmpty(lclSSMasterBl.getShipSsContactId().getTradingPartner().getAccountno())) {
            if (!lclSSMasterBl.getShipSsContactId().getTradingPartner().getAccountno().equalsIgnoreCase(addVoyageForm.getAccountNumber())) {
                remarks = "(ShippAcctNo-->" + lclSSMasterBl.getShipSsContactId().getCompanyName() + "(" + lclSSMasterBl.getShipSsContactId().getTradingPartner().getAccountno() + ")" + " to "
                        + addVoyageForm.getShipperEdi() + "(" + addVoyageForm.getShipperAccountNo() + ")" + ")";
                lclSsRemarksDAO.insertLclSSRemarks(addVoyageForm.getHeaderId(), "auto", "", remarks, "", user.getUserId());

            }
        }
    }

    public ActionForward viewCurrentUnitForDomestic(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        setAllUnitPopupValuesForDomestic(lclAddVoyageForm, request);
        if (null != lclAddVoyageForm.getBookScheduleNo() && !"".equals(lclAddVoyageForm.getBookScheduleNo())) {
            String bookScheduleNoPolEtd = lclAddVoyageForm.getBookScheduleNo() + "/" + lclAddVoyageForm.getPolEtd();
            request.setAttribute("bookScheduleNoPolEtd", bookScheduleNoPolEtd);
            request.setAttribute("polEtd", lclAddVoyageForm.getPolEtd());
            request.setAttribute("bookScheduleNo", lclAddVoyageForm.getBookScheduleNo());
        }
        request.setAttribute("displayLoadComplete", lclAddVoyageForm.getDisplayLoadComplete());
        setAllLclSsDetail(lclAddVoyageForm, request);
        return mapping.findForward(OPEN_UNIT);
    }

    public void setAllUnitPopupValuesForDomestic(LclAddVoyageForm lclAddVoyageForm,
            HttpServletRequest request) throws Exception {
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        LclUnitSs lclunitss = null;
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginId()) && CommonUtils.isNotEmpty(lclAddVoyageForm.getFinalDestinationId())) {
            LclSsDetail lclSsDetail = new LclSsDetailDAO().findById(null != lclAddVoyageForm.getDetailId() ? lclAddVoyageForm.getDetailId() : null);
            if (null != lclSsDetail) {
                lclAddVoyageForm.setOriginId(lclSsDetail.getDeparture().getId());
                lclAddVoyageForm.setFinalDestinationId(lclSsDetail.getArrival().getId());
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitId())) {
                request.setAttribute("unit", lclUnitDAO.findById(lclAddVoyageForm.getUnitId()));
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
                    lclunitss = lclunitssdao.findById(lclAddVoyageForm.getUnitssId());
                    request.setAttribute("lclunitss", lclunitss);
                }
                request.setAttribute("intransitDr", lclAddVoyageForm.isIntransitDr());
                request.setAttribute("destuffedList", lclUnitDAO.getReleasedBookingsForDomestic(lclAddVoyageForm));
                request.setAttribute("stuffedList", lclUnitDAO.getStuffedListByUnit(lclAddVoyageForm.getUnitssId()));
                request.setAttribute("bookingUnitsBean", lclUnitDAO.getDrBookingUnitsBean());
            } else {
                request.setAttribute("destuffedList", lclUnitDAO.getReleasedBookingsForDomestic(lclAddVoyageForm));
            }
            request.setAttribute("index", lclAddVoyageForm.getIndex());
            List<BookingUnitsBean> drList = lclUnitDAO.getAllUnitsByVoyage(lclAddVoyageForm.getHeaderId());
            if (lclunitss != null && lclunitss.getLclSsHeader() != null) {
                lclAddVoyageForm.setHeaderId(lclunitss.getLclSsHeader().getId());
            }
            // setDRListForVoyage(lclAddVoyageForm.getHeaderId(), lclUnitDAO, drList);  no need this method fix in jsp page
            if (!drList.isEmpty() && lclAddVoyageForm.getMethodName().equalsIgnoreCase("openUnits")
                    || lclAddVoyageForm.getMethodName().equalsIgnoreCase("viewCurrentUnitForDomestic")
                    || lclAddVoyageForm.getMethodName().equalsIgnoreCase("saveDestuffedUnits")
                    || lclAddVoyageForm.getMethodName().equalsIgnoreCase("deleteStuffedUnits")) {
                Character status = null;
                List<Character> unitStatusList = new ArrayList<Character>();
                for (BookingUnitsBean li : drList) {
                    if (li.getUnitId() != null && CommonUtils.isNotEmpty(li.getUnitId())) {
                        status = new LclUnitDAO().getUnitStatus(li.getUnitId(), lclAddVoyageForm.getHeaderId());
                        unitStatusList.add(status);
                    } else {
                        unitStatusList.add(' ');
                    }
                }
                request.setAttribute("unitStatusList", unitStatusList);
            }
            request.setAttribute("user", getCurrentUser(request));
            request.setAttribute("unitSsIdFlag", request.getParameter("unitSsIdFlag"));
            request.setAttribute("unitssId", lclAddVoyageForm.getUnitssId());
            request.setAttribute("filterValues", lclAddVoyageForm.getFilterByChanges());
            request.setAttribute("headerId", lclAddVoyageForm.getHeaderId());
            request.setAttribute("originId", lclAddVoyageForm.getOriginId());
            request.setAttribute("finalDestinationId", lclAddVoyageForm.getFinalDestinationId());
            request.setAttribute("drList", drList);
        }
    }

    public ActionForward displayCloseRemarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        if (lclssheader.getClosedRemarks() != null && !lclssheader.getClosedRemarks().trim().equals("")) {
            request.setAttribute("remarks", lclssheader.getClosedRemarks());
        }
        request.setAttribute("status", lclAddVoyageForm.getStatus().trim());
        request.setAttribute("headerId", lclAddVoyageForm.getHeaderId());
        request.setAttribute("unitId", lclAddVoyageForm.getUnitId());
        return mapping.findForward("displayCloseRemarks");
    }

    public ActionForward updateClosedAuditedRemarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        LclSsRemarksDAO lclSSRemarksDAO = new LclSsRemarksDAO();
        User user = getCurrentUser(request);
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        Date d = new Date();
        StringBuilder remarks = new StringBuilder();
        String audited = null;
        if (lclAddVoyageForm.getButtonValue() != null && lclssheader != null) {
            if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Close")) {
                lclssheader.setClosedBy(user);
                lclssheader.setClosedDatetime(d);
                remarks.append("Voyage Close Remarks ->").append("Voyage No ->").append(lclssheader.getScheduleNo());
                remarks.append(" POL ->");
                remarks.append(lclssheader.getOrigin().getUnLocationName()).append("(").append(lclssheader.getOrigin().getUnLocationCode());
                remarks.append(") ").append("POD ->").append(lclssheader.getDestination().getUnLocationName()).append("(");
                remarks.append(lclssheader.getDestination().getUnLocationCode()).append(")");
                lclSSRemarksDAO.insertLclSSRemarks(lclssheader.getId(), "auto", null, remarks.toString(), null, user.getUserId());
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Closed")) {
                lclssheader.setClosedBy(null);
                lclssheader.setClosedDatetime(null);
                lclssheader.setReopenedBy(user);
                lclssheader.setReopenedDatetime(d);
                remarks.append("Voyage Closed Remarks ->").append("Voyage No ->").append(lclssheader.getScheduleNo());
                remarks.append(" POL ->");
                remarks.append(lclssheader.getOrigin().getUnLocationName()).append("(").append(lclssheader.getOrigin().getUnLocationCode());
                remarks.append(") ").append("POD ->").append(lclssheader.getDestination().getUnLocationName()).append("(");
                remarks.append(lclssheader.getDestination().getUnLocationCode()).append(")");
                lclSSRemarksDAO.insertLclSSRemarks(lclssheader.getId(), "auto", null, remarks.toString(), null, user.getUserId());
            }
            if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Audit")) {
                lclssheader.setAuditedBy(user);
                lclssheader.setAuditedDatetime(d);
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Audited")) {
                lclssheader.setAuditedBy(null);
                lclssheader.setAuditedDatetime(null);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAuditedRemarks()) && lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Audit")) {
            lclssheader.setAuditedRemarks(lclAddVoyageForm.getAuditedRemarks().toUpperCase());
            audited = "Audited Remarks ->" + lclAddVoyageForm.getAuditedRemarks().toUpperCase();
            lclSSRemarksDAO.insertLclSSRemarks(lclssheader.getId(), "auto", null, audited, null, user.getUserId());
        }
        if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Close")) {
            lclssheader.setClosedRemarks(lclAddVoyageForm.getClosedRemarks().toUpperCase());
        } else {
            lclssheader.setReopenedRemarks(lclAddVoyageForm.getReopenedRemarks());
        }
        lclssheader.setModifiedDatetime(d);
        lclssheader.setModifiedBy(user);
        lclssheaderdao.update(lclssheader);
        new ExportVoyageUtils().setVoyageRequestVal(request, lclssheader, lclAddVoyageForm, user);
        return mapping.findForward(DISPLAY_NEW_VOYAGE);
    }

    public ActionForward displayAuditedRemarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        if (lclssheader.getAuditedRemarks() != null && !lclssheader.getAuditedRemarks().trim().equals("")) {
            request.setAttribute("remarks", lclssheader.getAuditedRemarks());
        }
        request.setAttribute("status", lclAddVoyageForm.getStatus().trim());
        request.setAttribute("headerId", lclAddVoyageForm.getHeaderId());
        request.setAttribute("unitId", lclAddVoyageForm.getUnitId());
        return mapping.findForward("displayAuditRemarks");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ViewDr/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdir();
        }
        User loginUser = getCurrentUser(request);
        request.setAttribute("loginuser", loginUser.getRole().getRoleId());
        fileName.append("ViewDR.xls");
        List<ManifestBean> drList = new ExportUnitQueryUtils().getUnitViewDrList(lclAddVoyageForm.getUnitssId(), request);
        new ExportLclUnitsViewDRToExcel().exportToExcel(fileName.toString(), drList);
        if (CommonUtils.isNotEmpty(fileName.toString())) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName.toString()));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(fileName.toString(), response.getOutputStream());
            return null;
        }
        request.setAttribute("pickedViewDrUnitList", new ExportUnitQueryUtils().getUnitViewDrList(lclAddVoyageForm.getUnitssId(), request));
        return mapping.findForward("displayViewDRsPopup");
    }

    public ActionForward showSsMasterDetailPopUp(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
            LclUnitSs unitSs = new LclUnitSsDAO().findById(lclAddVoyageForm.getUnitssId());
            if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("editMaster")) {
                lclAddVoyageForm.setBlbody(unitSs.getBlBody());
                lclAddVoyageForm.getPickedDrMasterBL(unitSs.getSpBookingNo());
                lclAddVoyageForm.setVolumeImperial(null != unitSs.getVolumeImperial()
                        ? unitSs.getVolumeImperial().toString() : lclAddVoyageForm.getVolumeImperial());
                lclAddVoyageForm.setVolumeMetric(null != unitSs.getVolumeMetric()
                        ? unitSs.getVolumeMetric().toString() : lclAddVoyageForm.getVolumeMetric());
                lclAddVoyageForm.setWeightMetric(null != unitSs.getWeightMetric()
                        ? unitSs.getWeightMetric().toString() : lclAddVoyageForm.getWeightMetric());
                lclAddVoyageForm.setWeightImperial(null != unitSs.getWeightImperial()
                        ? unitSs.getWeightImperial().toString() : lclAddVoyageForm.getWeightImperial());
                lclAddVoyageForm.setTotalPieces(null != unitSs.getTotalPieces()
                        ? unitSs.getTotalPieces() : lclAddVoyageForm.getTotalPieces());
                request.setAttribute("unitNo", unitSs.getLclUnit().getUnitNo());
                request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("saveMaster")) {
                unitSs.setBlBody(CommonUtils.isNotEmpty(lclAddVoyageForm.getBlbody())
                        ? lclAddVoyageForm.getBlbody() : null);
                unitSs.setTotalPieces(CommonUtils.isNotEmpty(lclAddVoyageForm.getTotalPieces())
                        ? lclAddVoyageForm.getTotalPieces() : null);
                unitSs.setWeightMetric(CommonUtils.isNotEmpty(lclAddVoyageForm.getWeightMetric())
                        ? new BigDecimal(lclAddVoyageForm.getWeightMetric()) : null);
                unitSs.setVolumeMetric(CommonUtils.isNotEmpty(lclAddVoyageForm.getVolumeMetric())
                        ? new BigDecimal(lclAddVoyageForm.getVolumeMetric()) : null);
                unitSs.setWeightImperial(CommonUtils.isNotEmpty(lclAddVoyageForm.getWeightImperial())
                        ? new BigDecimal(lclAddVoyageForm.getWeightImperial()) : null);
                unitSs.setVolumeImperial(CommonUtils.isNotEmpty(lclAddVoyageForm.getVolumeImperial())
                        ? new BigDecimal(lclAddVoyageForm.getVolumeImperial()) : null);
                unitSs.setModifiedBy(getCurrentUser(request));
                unitSs.setModifiedDatetime(new Date());
                unitSs.setModifiedBy(getCurrentUser(request));
                unitSs.setModifiedDatetime(new Date());
                new LclUnitSsDAO().update(unitSs);
            }
        }
        return mapping.findForward("showMasterPieceDetail");
    }

    public ActionForward changeOptionPopUp(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        LclUnitSs unitSs = new LclUnitSsDAO().findById(addVoyageForm.getUnitssId());
        User loginUser = getCurrentUser(request);
        if (unitSs.getLclSsHeader().getClosedDatetime() != null) {
            request.setAttribute("flagOption", true);
        }
        Boolean voyageFlag = true;
        if (unitSs.getCob()) {
            if (loginUser.getRole() != null && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
                RoleDuty roleDuty = new RoleDutyDAO().getByProperty("roleName", loginUser.getRole().getRoleDesc());
                voyageFlag = roleDuty.isChangeVoyage() ? true : false;
            }
        }
        request.setAttribute("voyageFlag", voyageFlag);
        request.setAttribute("unitSs", unitSs);
        return mapping.findForward("changeOptionsBtn");
    }

    public ActionForward changeOptionSubmitBtn(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm addVoyageForm = (LclAddVoyageForm) form;
        User loginUser = getCurrentUser(request);
        if ("CV".equalsIgnoreCase(addVoyageForm.getChangeVoyOpt())) {
            changeVoyageOption(addVoyageForm, loginUser, request);
            request.setAttribute("sucessMsg", "Voyage Sucessfully Changed");
        }
        if ("UC".equalsIgnoreCase(addVoyageForm.getChangeVoyOpt())) {
            changeUnitOption(addVoyageForm, loginUser, request);
            request.setAttribute("sucessMsg", "UnitNo Sucessfully Changed");
        }
        if ("ABL".equalsIgnoreCase(addVoyageForm.getChangeVoyOpt())) {
            manifestBlOption(addVoyageForm, loginUser);
            request.setAttribute("sucessMsg", "BL Sucessfully Manifested");
        }
        if ("RBL".equalsIgnoreCase(addVoyageForm.getChangeVoyOpt())) {
            unManifestBlOption(addVoyageForm, loginUser);
            if (addVoyageForm.isUnPickDrOpt()) {
                request.setAttribute("sucessMsg", "BL Sucessfully UnManifested & Un-picked");
            } else {
                request.setAttribute("sucessMsg", "BL Sucessfully UnManifested");
            }
        }
        return mapping.findForward("changeOptionsBtn");
    }

    public void changeVoyageOption(LclAddVoyageForm addVoyForm,
            User loginUser, HttpServletRequest request) throws Exception {
        LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
        LclUnitSs unitSs = unitSsDAO.findById(addVoyForm.getUnitssId());
        LclSsDetailDAO ssDetailDAO = new LclSsDetailDAO();
        String voyageNo = unitSs.getLclSsHeader().getScheduleNo();
        unitSs.setModifiedBy(loginUser);
        unitSs.setModifiedDatetime(new Date());
        unitSs.setLclSsHeader(new LclSsHeader(addVoyForm.getChangeVoyHeaderId()));
        unitSsDAO.saveOrUpdate(unitSs);
        LclUnitSsRemarksDAO unitSsRemarksDAO = new LclUnitSsRemarksDAO();
        new LclSsAcDAO().updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                addVoyForm.getUnitId(), loginUser.getUserId());
        new LclUnitSsManifestDAO().updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                unitSs.getId(), loginUser.getUserId());
        new LclUnitWhseDAO().updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                addVoyForm.getUnitId(), loginUser.getUserId());
        unitSsRemarksDAO.updateSsHeaderId(addVoyForm.getHeaderId(), addVoyForm.getChangeVoyHeaderId(),
                addVoyForm.getUnitId());
        if (CommonUtils.isNotEmpty(addVoyForm.getHeaderId())) {
            Long oldSsDetailId = ssDetailDAO.getId(addVoyForm.getHeaderId());
            Long newSsDetailId = ssDetailDAO.getId(addVoyForm.getChangeVoyHeaderId());
            new LclUnitSsDispoDAO().updateSsHeaderId(oldSsDetailId, newSsDetailId,
                    unitSs.getLclUnit().getId(), loginUser.getUserId());
        }

        LclSsRemarksDAO ssRemarksDAO = new LclSsRemarksDAO();
        ssRemarksDAO.insertSsHeaderRemarks(addVoyForm.getHeaderId(), "auto",
                "Unit#" + unitSs.getLclUnit().getUnitNo() + " was changed to Voyage#" + addVoyForm.getChangeVoyageNo(),
                loginUser.getUserId(), null);

        unitSsRemarksDAO.insertLclunitRemarks(addVoyForm.getChangeVoyHeaderId(), unitSs.getLclUnit().getId(), "auto",
                "Voyage# changed from " + voyageNo + " to " + addVoyForm.getChangeVoyageNo(), loginUser.getUserId());
        Set<String> fileNo = new HashSet<String>();
        ExportNotificationDAO exportNotificationDAO = new ExportNotificationDAO();
        List<ManifestBean> fileIdList = exportNotificationDAO.getPickedDrList(unitSs.getId());
        if (null != fileIdList && !fileIdList.isEmpty()) {
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");

            String realPath = request.getSession().getServletContext().getRealPath("/");
            Date today = new Date();
            String companyName = LoadLogisoftProperties.getProperty("application.email.companyName").toUpperCase();
            for (ManifestBean fileId : fileIdList) {
                fileNo.add(fileId.getFileNo());
                String emailBody = exportNotificationDAO.getEmailContentBody(request, fileId.getBusinessUnit(),
                        addVoyForm.getVoyageChangeReason());
                new ExportVoyageUtils().sendNotification(addVoyForm, fileId, reportLocation, today,
                        companyName, realPath, exportNotificationDAO, request, loginUser.getLoginName(), emailBody);
                lclRemarksDAO.insertLclRemarks(fileId.getFileId(), "auto",
                        "Voyage# changed from " + voyageNo + " to " + addVoyForm.getChangeVoyageNo(), loginUser.getUserId());
            }
        }

        if (unitSs.getCob()) {
            LclSsDetail lclSsDetail = ssDetailDAO.findByTransMode(addVoyForm.getChangeVoyHeaderId(), "V");
            if (null != lclSsDetail) {
                String vesselCode = ssDetailDAO.getVesselCode(lclSsDetail.getId());
                for (String fileNumber : fileNo) {
                    TransactionDAO transactionDAO = new TransactionDAO();
                    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
                    ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
                    transactionDAO.updateVoyageNoByLcl(fileNumber, lclSsDetail.getStd(), addVoyForm.getChangeVoyageNo(),
                            vesselCode, lclSsDetail.getSta(), lclSsDetail.getSpReferenceName(),
                            lclSsDetail.getSpAcctNo().getAccountName(), loginUser.getUserId(), unitSs.getSpBookingNo());
                    transactionLedgerDAO.updateVoyageNoByLcl(fileNumber, lclSsDetail.getStd(), addVoyForm.getChangeVoyageNo(),
                            vesselCode, loginUser.getUserId(), unitSs.getSpBookingNo());
                    arTransactionHistoryDAO.updateVoyageNoByLcl(fileNumber, addVoyForm.getChangeVoyageNo(), lclSsDetail.getStd());
                }
            }
        }
        clearHeaderValues(addVoyForm);

    }

    public void changeUnitOption(LclAddVoyageForm addVoyageForm, User loginUser, HttpServletRequest request) throws Exception {
        LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
        LclUnitDAO unitDAO = new LclUnitDAO();
        Date now = new Date();
        Long oldUnitId = addVoyageForm.getOldUnitId();
        LclUnit oldUnit = unitDAO.findById(addVoyageForm.getOldUnitId());

        LclUnit lclUnit = unitDAO.getByProperty("unitNo", addVoyageForm.getUnitNo());
        if (lclUnit == null) {
            lclUnit = new LclUnit();
            lclUnit.setEnteredBy(loginUser);
            lclUnit.setEnteredDatetime(now);
            lclUnit.setUnitType(oldUnit.getUnitType());
            lclUnit.setUnitNo(addVoyageForm.getUnitNo().toUpperCase());
            lclUnit.setModifiedDatetime(now);
            lclUnit.setModifiedBy(loginUser);
        }
        lclUnit.setHazmatPermitted(oldUnit.getHazmatPermitted());
        lclUnit.setRefrigerated(oldUnit.getRefrigerated());
        unitDAO.saveOrUpdate(lclUnit);

        Long ssDetailId = new LclSsDetailDAO().getIdbyAsc(addVoyageForm.getHeaderId());

        LclUnitWhseDAO unitWhseDAO = new LclUnitWhseDAO();
        unitWhseDAO.deleteWarehouseId(lclUnit.getId());
        LclUnitSsDispoDAO unitSsDispoDAO = new LclUnitSsDispoDAO();

        LclUnitSsRemarksDAO unitSsRemarksDAO = new LclUnitSsRemarksDAO();
        StringBuilder remarks = new StringBuilder();
        if (CommonUtils.isNotEmpty(addVoyageForm.getUnitNo()) && !oldUnit.getUnitNo().equalsIgnoreCase(addVoyageForm.getUnitNo())) {
            remarks.append("Unit# changed from ").append(oldUnit.getUnitNo()).append(" to ").append(addVoyageForm.getUnitNo().toUpperCase()).append(" ");
        }
        if (CommonUtils.isNotEmpty(remarks)) {
            unitSsRemarksDAO.insertLclunitRemarks(addVoyageForm.getHeaderId(), lclUnit.getId(), "auto", remarks.toString(), loginUser.getUserId());
        }
        unitSsDAO.updateUnitId(addVoyageForm.getHeaderId(), oldUnitId, lclUnit.getId(), loginUser.getUserId());
        unitSsRemarksDAO.updateUnitId(addVoyageForm.getHeaderId(), oldUnitId, lclUnit.getId());
        unitWhseDAO.updateUnitId(addVoyageForm.getHeaderId(), oldUnitId, lclUnit.getId(), loginUser.getUserId());
        unitSsDispoDAO.updateUnitId(ssDetailId, oldUnitId, lclUnit.getId(), loginUser.getUserId());
        new LclUnitSsManifestDAO().updateUnitId(addVoyageForm.getHeaderId(), oldUnitId, lclUnit.getId(), loginUser.getUserId());
        ExportNotificationDAO exportNotificationDAO = new ExportNotificationDAO();

        List<ManifestBean> pickedDrList = exportNotificationDAO.getPickedDrList(addVoyageForm.getUnitssId());
        if (CommonUtils.isNotEmpty(pickedDrList)) {
            LclUnitSs unitSs = unitSsDAO.findById(addVoyageForm.getUnitssId());
            String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
            String realPath = request.getSession().getServletContext().getRealPath("/");
            String companyName = LoadLogisoftProperties.getProperty("application.email.companyName").toUpperCase();
            TransactionDAO transactionDAO = new TransactionDAO();
            TransactionLedgerDAO ledgerDAO = new TransactionLedgerDAO();
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            for (ManifestBean picked : pickedDrList) {
                String emailBody = exportNotificationDAO.getEmailContentBody(request, picked.getBusinessUnit(),
                        addVoyageForm.getVoyageChangeReason());
                new ExportVoyageUtils().sendNotification(addVoyageForm, picked, reportLocation, now,
                        companyName, realPath, exportNotificationDAO, request, loginUser.getLoginName(), emailBody);
                if (unitSs.getCob()) {
                    transactionDAO.updateLclEContainers(picked.getFileNo(), addVoyageForm.getScheduleNo(), lclUnit.getUnitNo());
                    ledgerDAO.updateLclEContainers(picked.getFileNo(), lclUnit.getUnitNo());
                }
                String notes = "Unit# changed from " + oldUnit.getUnitNo() + " to " + lclUnit.getUnitNo();
                remarksDAO.insertLclRemarks(picked.getFileId(), "auto", notes, loginUser.getUserId());
            }
        }
    }

    public void manifestBlOption(LclAddVoyageForm addVoyForm, User loginUser) throws Exception {
        LclUnitSs unitSs = new LclUnitSsDAO().findById(addVoyForm.getUnitssId());
        LclBookingPieceUnitDAO bookingPieceUnitDAO = new LclBookingPieceUnitDAO();
        String[] fileIdArray = addVoyForm.getFileNumberId().split(",");
        Date d = new Date();
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        if (fileIdArray != null && fileIdArray.length > 0) {
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            UnLocation unLocCodeOrgn = unitSs.getLclSsHeader().getOrigin();
            UnLocation unLocCodeDestn = unitSs.getLclSsHeader().getDestination();
            String type = "E".equalsIgnoreCase(unitSs.getLclSsHeader().getServiceType())
                    ? "Export" : "C".equalsIgnoreCase(unitSs.getLclSsHeader().getServiceType()) ? "Cfcl" : "Inland";
            String notes1 = "BL is Manifested into the Unit# "
                    + unitSs.getLclUnit().getUnitNo() + " and Voyage# " + unitSs.getLclSsHeader().getScheduleNo();
            String pickedNotes = "Picked on " + type + " Unit (" + unitSs.getLclUnit().getUnitNo() + "," + unLocCodeOrgn.getUnLocationCode()
                    + "-" + unLocCodeDestn.getUnLocationCode() + "-" + unitSs.getLclSsHeader().getScheduleNo() + ")";
            for (String fileId : fileIdArray) {
                Long fileNumberId = Long.parseLong(fileId);
                LclModel picked = new ExportUnitQueryUtils().getPickedVoyageByFileId(fileNumberId, unitSs.getLclSsHeader().getServiceType());
                if (picked != null && !picked.getScheduleNo().equalsIgnoreCase(unitSs.getLclSsHeader().getScheduleNo())) {
                    LclSsDetail ssDetail = unitSs.getLclSsHeader().getVesselSsDetail();
                    String vesselCode = new LclSsDetailDAO().getVesselCode(ssDetail.getId());
                    TransactionDAO transactionDAO = new TransactionDAO();
                    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
                    ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
                    transactionDAO.updateVoyageNoByLcl(addVoyForm.getFileNumber(), ssDetail.getStd(), addVoyForm.getChangeVoyageNo(),
                            vesselCode, ssDetail.getSta(), ssDetail.getSpReferenceName(),
                            ssDetail.getSpAcctNo().getAccountName(), loginUser.getUserId(), unitSs.getSpBookingNo());
                    transactionLedgerDAO.updateVoyageNoByLcl(addVoyForm.getFileNumber(), ssDetail.getStd(), addVoyForm.getChangeVoyageNo(),
                            vesselCode, loginUser.getUserId(), unitSs.getSpBookingNo());
                    arTransactionHistoryDAO.updateVoyageNoByLcl(addVoyForm.getFileNumber(), addVoyForm.getChangeVoyageNo(), ssDetail.getStd());
                    bookingPieceUnitDAO.updateUnitSsId(fileNumberId, unitSs.getId());
                    String notes = "Unpicked from " + type + " Unit (" + picked.getUnitNo() + "," + unLocCodeOrgn.getUnLocationCode() + "-" + unLocCodeDestn.getUnLocationCode() + "-" + picked.getScheduleNo() + ")";
                    remarksDAO.insertLclRemarks(fileNumberId, "auto", notes, loginUser.getUserId());
                    remarksDAO.insertLclRemarks(fileNumberId, "auto", pickedNotes, loginUser.getUserId());
                } else if (picked == null) {
                    List<LclBookingPiece> lclBookingPiecesList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
                    if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                            LclBookingPieceUnit lclBookingPieceUnit = new LclBookingPieceUnit();
                            lclBookingPieceUnit.setLclBookingPiece(lclBookingPiece);
                            lclBookingPieceUnit.setEnteredBy(loginUser);
                            lclBookingPieceUnit.setModifiedBy(loginUser);
                            lclBookingPieceUnit.setEnteredDatetime(d);
                            lclBookingPieceUnit.setModifiedDatetime(d);
                            lclBookingPieceUnit.setLoadedDatetime(d);
                            lclBookingPieceUnit.setLclUnitSs(unitSs);
                            bookingPieceUnitDAO.saveOrUpdate(lclBookingPieceUnit);
                        }
                    }
                    remarksDAO.insertLclRemarks(fileNumberId, "auto", pickedNotes, loginUser.getUserId());
                }
                remarksDAO.insertLclRemarks(fileNumberId, "auto", notes1, loginUser.getUserId());
                new LclFileNumberDAO().updateFileStatus(fileNumberId, "M");
                if (unitSs.getCob()) {
                    new LclExportManifestDAO().manifest(null, loginUser, true, fileNumberId);
                }
            }
            LclUnitSsManifestDAO unitSsManifestDAO = new LclUnitSsManifestDAO();
            LclUnitSsManifest lclunitssmanifest = unitSsManifestDAO.insertUnitSsManifest(unitSs, unitSs.getLclUnit(), unitSs.getLclSsHeader(), loginUser, d, true);
            lclunitssmanifest.setManifestedByUser(loginUser);
            lclunitssmanifest.setManifestedDatetime(d);
            unitSsManifestDAO.saveOrUpdate(lclunitssmanifest);
        }
    }

    public void unManifestBlOption(LclAddVoyageForm addVoyForm, User loginUser) throws Exception {
        LclFileNumberDAO fileNumberDAO = new LclFileNumberDAO();
        LCLBlDAO blDAO = new LCLBlDAO();
        LclExportManifestDAO manifestDAO = new LclExportManifestDAO();
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        LclUnitSs unitSs = new LclUnitSsDAO().findById(addVoyForm.getUnitssId());

        String notes = "BL is UnManifested from the Unit# " + unitSs.getLclUnit().getUnitNo() + " and Voyage# " + unitSs.getLclSsHeader().getScheduleNo();
        Long fileNumberId = Long.parseLong(addVoyForm.getFileNumberId());
        blDAO.updateModifiedDate(fileNumberId, loginUser.getUserId());
        remarksDAO.insertLclRemarks(fileNumberId, "auto", notes, loginUser.getUserId());
        if (unitSs.getCob()) {
            manifestDAO.manifest(null, loginUser, false, fileNumberId);
        }
        fileNumberDAO.updateFileStatus(fileNumberId, "W");
        if (addVoyForm.isUnPickDrOpt()) {
            LclBookingPieceUnitDAO bookingPieceUnitDAO = new LclBookingPieceUnitDAO();
            UnLocation unLocCodeOrgn = new UnLocationDAO().findById(addVoyForm.getOriginId());
            UnLocation unLocCodeDestn = new UnLocationDAO().findById(addVoyForm.getFinalDestinationId());
            String type = "lclExport".equalsIgnoreCase(addVoyForm.getFilterByChanges())
                    ? "Export" : "lclCfcl".equalsIgnoreCase(addVoyForm.getFilterByChanges()) ? "Cfcl" : "Inland";

            notes = "Unpicked from " + type + " Unit (" + addVoyForm.getOldUnitNo() + ","
                    + unLocCodeOrgn.getUnLocationCode() + "-" + unLocCodeDestn.getUnLocationCode() + "-" + addVoyForm.getScheduleNo() + ")";
            remarksDAO.insertLclRemarks(fileNumberId, "Unit_Tracking", notes, loginUser.getUserId());
            LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(fileNumberId);
            if (lclFileNumber != null && lclFileNumber.getLclBookingPieceList() != null && lclFileNumber.getLclBookingPieceList().size() > 0) {
                for (int j = 0; j < lclFileNumber.getLclBookingPieceList().size(); j++) {
                    LclBookingPiece lclBookingPiece = (LclBookingPiece) lclFileNumber.getLclBookingPieceList().get(j);
                    bookingPieceUnitDAO.unPickByFile(lclBookingPiece.getId(), addVoyForm.getUnitssId());
                }
            }
            new LCLBookingDAO().updateModifiedDateTime(fileNumberId, loginUser.getUserId());
            Disposition disposition = new DispositionDAO().getByProperty("eliteCode", "RCVD");
            new LclBookingDispoDAO().insertBookingDispo(fileNumberId, disposition.getId(), null, null, loginUser.getUserId(), unLocCodeOrgn.getId());
            Date now = new Date();
            LclUnitSsManifestDAO unitSsManifestDAO = new LclUnitSsManifestDAO();
            LclUnitSsManifest lclunitssmanifest = unitSsManifestDAO.insertUnitSsManifest(unitSs, unitSs.getLclUnit(), unitSs.getLclSsHeader(), loginUser, now, true);
            if (CommonUtils.isEmpty(lclunitssmanifest.getCalculatedTotalPieces())) {
                lclunitssmanifest.setManifestedByUser(null);
                lclunitssmanifest.setManifestedDatetime(null);
            } else {
                lclunitssmanifest.setManifestedByUser(loginUser);
                lclunitssmanifest.setManifestedDatetime(now);
            }
            unitSsManifestDAO.saveOrUpdate(lclunitssmanifest);
        }
    }

    public ActionForward openConsolidatePage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        request.setAttribute("consolidateList", new LclUnitDAO()
                .getConsolidateReleasedDetails(lclAddVoyageForm));
        if (!lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")) {
            request.setAttribute("nonReleaseDrList", new LclUnitDAO()
                    .getNonReleasedDrList(lclAddVoyageForm.getConsolidateFiles()));
            request.setAttribute("bookedAnotherVoyageList", new LclUnitDAO()
                    .getBookedOnAnotherVoyageDRList(lclAddVoyageForm.getConsolidateFiles(), lclAddVoyageForm.getHeaderId().toString()));
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("openConsolidatePage");
    }
}
