/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclImportUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.LclImportUnitBkgModel;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.BlInfo;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlInfoDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.ImportVoyageSearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.common.dao.PropertyDAO;
import com.logiware.thread.LclVoyageNumberThread;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
 * @author Logiware
 */
public class LclImportAddVoyageAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static String DISPLAY_DETAIL_POPUP = "importDetailPopup";
    private static String DISPLAY_DISP_VIEW_DRS_POPUP = "importdisplayViewDispDrsPopup";
    private static String DISPLAY_DISP_VIEW_DRS_EDIT_CONTACT_POPUP = "importdisplayViewDispDrsEditContactsPopup";
    private LclUtils lclUtils = new LclUtils();

    public ActionForward editVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            if (lclAddVoyageForm.getVoyageOwnerFlag() != null && "true".equalsIgnoreCase(lclAddVoyageForm.getVoyageOwnerFlag())) {
                LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
                User user = getCurrentUser(request);
                LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
                String remarks = "";
                if (lclssheader.getOwnerUserId() != null && lclssheader.getOwnerUserId().getLoginName() != null) {
                    remarks = "Voyage Owner -> " + lclssheader.getOwnerUserId().getLoginName().toUpperCase() + " to " + lclAddVoyageForm.getVoyageOwner().toUpperCase();
                } else {
                    remarks = "Voyage Owner -> " + lclAddVoyageForm.getVoyageOwner().toUpperCase();
                }
                new LclSsRemarksDAO().insertLclSSRemarks(lclssheader.getId(), "auto", null, remarks, null, user.getUserId());
                lclssheader.setOwnerUserId(new UserDAO().findById(Integer.parseInt(lclAddVoyageForm.getVoyenteredById())));
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAuditedRemarks())) {
                    lclssheader.setAuditedRemarks(lclAddVoyageForm.getAuditedRemarks().toUpperCase());
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getClosedRemarks())) {
                    lclssheader.setClosedRemarks(lclAddVoyageForm.getClosedRemarks().toUpperCase());
                }
                lclssheaderdao.update(lclssheader);
            }
            setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        }
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward("importVoyage");
    }

    public ActionForward openSSDetailsPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        return mapping.findForward("importDetailPopup");
    }

    public ActionForward openDetailsPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        PortsDAO portsDAO = new PortsDAO();
        lclAddVoyageForm.setHazmatPermitted("N");
        Integer agentCount = portsDAO.getAgentCount(lclAddVoyageForm.getPolUnlocationCode(), "I");
        if (agentCount <= 1) {
            String agent[] = portsDAO.getDefaultAgentForLcl(lclAddVoyageForm.getPolUnlocationCode(), "I");
            lclAddVoyageForm.setOriginAcctNo(agent[0]);
            lclAddVoyageForm.setOriginAcct(agent[1]);
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("importAddDetailPopup");
    }

    public ActionForward editVoyageHeader(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            lclAddVoyageForm.setScheduleNo(lclssheader.getScheduleNo());
            if (lclssheader.getRefrigerationPermitted()) {
                lclAddVoyageForm.setRefrigerationPermitted("Y");
            } else {
                lclAddVoyageForm.setRefrigerationPermitted("N");
            }
            if (CommonUtils.isNotEmpty(lclssheader.getRemarks())) {
                lclAddVoyageForm.setRemarks(lclssheader.getRemarks());
            }
            if (lclssheader.getBillingTerminal() != null) {
                lclAddVoyageForm.setBillTerminal(lclssheader.getBillingTerminal().getTerminalLocation() + "/" + lclssheader.getBillingTerminal().getTrmnum());
                lclAddVoyageForm.setBillTerminalNo(lclssheader.getBillingTerminal().getTrmnum());
            }
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("editHeaderPopup");
    }

    public ActionForward saveVoyageHeader(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
            Date d = new Date();
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBillTerminalNo())) {
                lclssheader.setBillingTerminal(refTerminalDAO.findById(lclAddVoyageForm.getBillTerminalNo()));
            }
            lclssheader.setRemarks(lclAddVoyageForm.getRemarks());
            lclssheader.setModifiedBy(getCurrentUser(request));
            lclssheader.setModifiedDatetime(d);
            lclssheaderdao.saveOrUpdate(lclssheader);
            clearHeaderValues(lclAddVoyageForm);
            setAllVoyageValues(lclAddVoyageForm, request);//common
            request.setAttribute("openPopup", "notopen");
        }
        return mapping.findForward("importVoyage");
    }

    public ActionForward saveVoyageDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        synchronized (LclImportAddVoyageAction.class) {
            LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
            Date d = new Date();
            User loginUser = getCurrentUser(request);
            LclSsDetail lclssdetail = null;
            LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
            UnLocationDAO unlocationdao = new UnLocationDAO();
            //Save Header
            LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
            if (lclssheader == null) {
                lclssheader = new LclSsHeader();
            }
            if (CommonUtils.isEmpty(lclAddVoyageForm.getHeaderId())) {
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getPooOrigin())) {
                    request.setAttribute("originValue", lclAddVoyageForm.getPooOrigin());
                    String polCode = lclAddVoyageForm.getPooOrigin();
                    request.setAttribute("polUnlocationCode", polCode.substring(polCode.indexOf("(") + 1, polCode.indexOf(")")));
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getPodDestination())) {
                    request.setAttribute("destinationValue", lclAddVoyageForm.getPodDestination());
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalOriginName())) {
                    request.setAttribute("originalOriginName", lclAddVoyageForm.getOriginalOriginName());
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginalDestinationName())) {
                    request.setAttribute("originalDestinationName", lclAddVoyageForm.getOriginalDestinationName());
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginId())) {
                    request.setAttribute("originId", lclAddVoyageForm.getOriginId());
                    lclssheader.setOrigin(unlocationdao.findById(lclAddVoyageForm.getOriginId()));
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFinalDestinationId())) {
                    request.setAttribute("destinationId", lclAddVoyageForm.getFinalDestinationId());
                    lclssheader.setDestination(unlocationdao.findById(lclAddVoyageForm.getFinalDestinationId()));
                }
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBillTerminalNo())) {
                    lclssheader.setBillingTerminal(new RefTerminalDAO().findById(lclAddVoyageForm.getBillTerminalNo()));
                }
                String voyageNumber = "";
                if (CommonUtils.isEmpty(lclssheader.getScheduleNo())) {
                    LclVoyageNumberThread thread = new LclVoyageNumberThread();
                    voyageNumber = thread.getVoyageNumber();
                } else {
                    voyageNumber = lclAddVoyageForm.getLclssheader().getScheduleNo();
                }
                lclssheader.setServiceType("I");
                lclssheader.setTransMode("V");
                lclssheader.setStatus("A");
                lclssheader.setDatasource("L");
                lclssheader.setScheduleNo(voyageNumber);
                lclssheader.setEnteredBy(loginUser);
                lclssheader.setOwnerUserId(loginUser);
                lclssheader.setEnteredDatetime(d);
                lclssheader.setRefrigerationPermitted(false);
                lclssheader.setDisplayrefrigerationPermitted("No");
                lclssheader.setModifiedBy(loginUser);
                lclssheader.setModifiedDatetime(d);
                lclssheaderdao.save(lclssheader);
                request.setAttribute("lclssheader", lclssheader);
            }
            //Voyage Details
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
                lclssdetail = lclssdetaildao.findById(lclAddVoyageForm.getDetailId());
            } else {
                lclssdetail = new LclSsDetail();
                lclssdetail.setEnteredBy(loginUser);
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
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDocReceived())) {
                lclssdetail.setDocReceived(DateUtils.parseDate(lclAddVoyageForm.getDocReceived(), "dd-MMM-yyyy"));
            }
            lclssdetail.setRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
            lclssdetail.setModifiedBy(loginUser);
            lclssdetail.setModifiedDatetime(d);
            lclssdetail.setLclSsHeader(lclssheader);
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDepartureId())) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getDepartureId());
                lclssdetail.setDeparture(unlocation);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getArrivalId())) {
                UnLocation unlocation = unlocationdao.findById(lclAddVoyageForm.getArrivalId());
                lclssdetail.setArrival(unlocation);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAccountNumber())) {
                lclssdetail.setSpAcctNo(new TradingPartnerDAO().findById(lclAddVoyageForm.getAccountNumber()));
            }
            lclssdetaildao.saveOrUpdate(lclssdetail);

            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
            LclUnitSsImportsDAO lclUnitSsImportsDAO = new LclUnitSsImportsDAO();
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            LclUnit lclUnit = null;
            LclUnitSs lclUnitSs = null;
            LclUnitSsImports lclUnitSsImports = null;
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitId())) {
                lclUnit = lclunitdao.findById(lclAddVoyageForm.getUnitId());
            }
            if (lclUnit == null) {
                lclUnit = new LclUnit();
                lclUnit.setEnteredBy(loginUser);
                lclUnit.setEnteredDatetime(d);
            }
            lclUnit.setModifiedBy(loginUser);
            lclUnit.setModifiedDatetime(d);
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitNo())) {
                lclUnit.setUnitNo(lclAddVoyageForm.getUnitNo().toUpperCase());
            }
            UnitTypeDAO unittypedao = new UnitTypeDAO();
            UnitType unittype = unittypedao.findById(lclAddVoyageForm.getUnitType());
            lclUnit.setUnitType(unittype);
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatPermittedUnit())) {
                if (lclAddVoyageForm.getHazmatPermittedUnit().equalsIgnoreCase("Y")) {
                    lclUnit.setHazmatPermitted(true);
                } else {
                    lclUnit.setHazmatPermitted(false);
                }
            }
            lclUnit.setRefrigerated(false);
            lclunitdao.saveOrUpdate(lclUnit);

            lclUnitSs = lclunitssdao.getLclUnitSSByLclUnitHeader(lclAddVoyageForm.getUnitId(), lclssheader.getId());
            if (lclUnitSs == null) {
                lclUnitSs = new LclUnitSs();
                lclUnitSs.setEnteredBy(getCurrentUser(request));
                lclUnitSs.setEnteredDatetime(d);
            }
            lclUnitSs.setCob(false);
            lclUnitSs.setStatus("E");
            lclUnitSs.setOptDocnoLg(false);
            lclUnitSs.setModifiedBy(getCurrentUser(request));
            lclUnitSs.setModifiedDatetime(d);
            lclUnitSs.setLclSsHeader(lclssheader);
            lclUnitSs.setLclUnit(lclUnit);
            lclUnitSs.setPrepaidCollect(lclAddVoyageForm.getPrepaidCollect());
            if (lclAddVoyageForm.getRemarks() != null && !lclAddVoyageForm.getRemarks().trim().equals("")) {
                lclUnitSs.setTruckingRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
            } else {
                lclUnitSs.setTruckingRemarks("");
            }
            lclUnitSs.setSUHeadingNote(null);
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBookingNumber())) {
                lclUnitSs.setSpBookingNo(lclAddVoyageForm.getBookingNumber());
            }
            lclunitssdao.saveOrUpdate(lclUnitSs);

            lclUnitSsImports = lclUnitSsImportsDAO.getLclUnitSSImportsByHeader(lclAddVoyageForm.getUnitId(), lclssheader.getId());
            if (lclUnitSsImports == null) {
                lclUnitSsImports = new LclUnitSsImports();
                lclUnitSsImports.setEnteredBy(loginUser);
                lclUnitSsImports.setModifiedBy(loginUser);
                lclUnitSsImports.setEnteredDatetime(d);
                lclUnitSsImports.setModifiedDatetime(d);
                lclUnitSsImports.setLclUnit(lclUnit);
                lclUnitSsImports.setLclSsHeader(lclssheader);
            }

            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginAcctNo())) {
                lclUnitSsImports.setOriginAcctNo(tradingPartnerDAO.findById(lclAddVoyageForm.getOriginAcctNo()));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getColoaderAcctNo())) {
                lclUnitSsImports.setColoaderAcctNo(tradingPartnerDAO.findById(lclAddVoyageForm.getColoaderAcctNo()));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getColoaderDevngAcctNo())) {
                lclUnitSsImports.setColoaderDevanningAcctNo(tradingPartnerDAO.findById(lclAddVoyageForm.getColoaderDevngAcctNo()));
            } else {
                lclUnitSsImports.setColoaderDevanningAcctNo(null);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getItPortId())) {
                lclUnitSsImports.setItPortId(unLocationDAO.findById(lclAddVoyageForm.getItPortId()));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitsWarehouseId())) {
                lclUnitSsImports.setUnitWareHouseId(warehouseDAO.findById(lclAddVoyageForm.getUnitsWarehouseId()));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getCfsWarehouseId())) {
                lclUnitSsImports.setCfsWarehouseId(warehouseDAO.findById(lclAddVoyageForm.getCfsWarehouseId()));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getLastFreeDate())) {
                lclUnitSsImports.setLastFreeDate(DateUtils.parseDate(lclAddVoyageForm.getLastFreeDate(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getItDatetime())) {
                lclUnitSsImports.setItDatetime(DateUtils.parseDate(lclAddVoyageForm.getItDatetime(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getApproxDueDate())) {
                lclUnitSsImports.setApproxDueDate(DateUtils.parseDate(lclAddVoyageForm.getApproxDueDate(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getGoDate())) {
                lclUnitSsImports.setGoDate(DateUtils.parseDate(lclAddVoyageForm.getGoDate(), "dd-MMM-yyyy"));
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getItNo())) {
                lclUnitSsImports.setItNo(lclAddVoyageForm.getItNo());
            }
            lclUnitSsImportsDAO.save(lclUnitSsImports);



            List<LclUnitSs> lclUnitSsList = new ArrayList<LclUnitSs>();
            lclUnitSsList.add(lclUnitSs);
            lclUnit.setLclUnitSsList(lclUnitSsList);

            LclUnitSsManifestDAO lclUnitSsManifestDAO = new LclUnitSsManifestDAO();
            LclUnitSsManifest lclUnitSsManifest = lclUnitSsManifestDAO.getLclUnitSSManifestByHeader(lclUnit.getId(), lclssheader.getId());
            if (lclUnitSsManifest == null) {
                lclUnitSsManifest = new LclUnitSsManifest();
                lclUnitSsManifest.setEnteredByUser(loginUser);
                lclUnitSsManifest.setModifiedByUser(loginUser);
                lclUnitSsManifest.setEnteredDatetime(d);
                lclUnitSsManifest.setModifiedDatetime(d);
                lclUnitSsManifest.setCalculatedBlCount(0);
                lclUnitSsManifest.setCalculatedDrCount(0);
                lclUnitSsManifest.setCalculatedTotalPieces(0);
                lclUnitSsManifest.setCalculatedVolumeImperial(new BigDecimal(0.00));
                lclUnitSsManifest.setCalculatedVolumeMetric(new BigDecimal(0.00));
                lclUnitSsManifest.setCalculatedWeightImperial(new BigDecimal(0.00));
                lclUnitSsManifest.setCalculatedWeightMetric(new BigDecimal(0.00));
                lclUnitSsManifest.setLclUnit(lclUnit);
                lclUnitSsManifest.setLclSsHeader(lclssheader);
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getMasterBL())) {
                lclUnitSsManifest.setMasterbl(lclAddVoyageForm.getMasterBL().toUpperCase());
            } else {
                lclUnitSsManifest.setMasterbl(null);
            }
            lclUnitSsManifestDAO.save(lclUnitSsManifest);
            //add Warehouse
            LclUnitWhse lclUnitWhse = new LclUnitWhseDAO().getLclUnitWhseDetails(lclUnit.getId(), lclssheader.getId());
            if (lclUnitWhse == null) {
                lclUnitWhse = new LclUnitWhse();
                lclUnitWhse.setEnteredBy(loginUser);
                lclUnitWhse.setEnteredDatetime(d);
                lclUnitWhse.setModifiedBy(loginUser);
                lclUnitWhse.setModifiedDatetime(d);
            }
            lclUnitWhse.setLclSsHeader(lclssheader);
            lclUnitWhse.setLclUnit(lclUnit);
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSealNoIn())) {
                lclUnitWhse.setSealNoIn(lclAddVoyageForm.getSealNoIn().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSealNoOut())) {
                lclUnitWhse.setSealNoOut(lclAddVoyageForm.getSealNoOut().toUpperCase());
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStrippedDate())) {
                lclUnitWhse.setDestuffedDatetime(DateUtils.parseDate(lclAddVoyageForm.getStrippedDate(), "dd-MMM-yyyy"));
            }
            Integer warehse = warehouseDAO.warehouseNo(lclssheader.getDestination().getUnLocationCode());
            if (CommonUtils.isNotEmpty(warehse)) {
                lclUnitWhse.setWarehouse(warehouseDAO.findById(warehse));
            } else {
                Warehouse warehouse = warehouseDAO.getByProperty("warehouseNo", "T99");
                lclUnitWhse.setWarehouse(warehouse);
            }
            new LclUnitWhseDAO().save(lclUnitWhse);
            if (lclUnit != null && lclssdetail != null) {//dispositio
                DispositionDAO dispdao = new DispositionDAO();
                Disposition disp = dispdao.getByProperty("eliteCode", "DATA");
                if (disp != null) {
                    LclUnitSsDispo lclUnitSsDispo = new LclUnitSsDispo();
                    lclUnitSsDispo.setDisposition(disp);
                    lclUnitSsDispo.setModifiedBy(loginUser);
                    lclUnitSsDispo.setEnteredBy(loginUser);
                    lclUnitSsDispo.setModifiedDatetime(d);
                    lclUnitSsDispo.setDispositionDatetime(d);
                    lclUnitSsDispo.setEnteredDatetime(d);
                    lclUnitSsDispo.setLclUnit(lclUnit);
                    lclUnitSsDispo.setLclSsDetail(lclssdetail);
                    new LclUnitSsDispoDAO().save(lclUnitSsDispo);
                }
            }
            //unit rates calculation
            if ("AUTOCOST_YES".equalsIgnoreCase(lclAddVoyageForm.getUnitAutoCostFlag())) {
                new LclSsAcDAO().calculateUnitRates(lclssheader, lclUnitSs, lclssheader.getOrigin().getId(), lclssheader.getDestination().getId(), lclAddVoyageForm.getCfsWarehouseId(), lclAddVoyageForm.getUnitType(), d, request);
            }
            lclAddVoyageForm.setHeaderId(lclssheader.getId());
            lclAddVoyageForm.setUnitssId(lclUnitSs.getId());
            setAllVoyageValues(lclAddVoyageForm, request);//common method
            request.setAttribute("openPopup", "notopen");
        }
        if (!lclssheaderdao.getSession().getTransaction().isActive()) {
            lclssheaderdao.getSession().getTransaction().begin();
        }
        return mapping.findForward("importVoyage");
    }

    public ActionForward saveVoyageDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        Date d = new Date();
        LclSsDetail lclssdetail = null;
        LclSsDetailDAO lclSsDetailDAO = new LclSsDetailDAO();
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            lclssdetail = lclSsDetailDAO.findById(lclAddVoyageForm.getDetailId());
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
            lclssdetail.setGeneralLrdt(DateUtils.parseDate(lclAddVoyageForm.getLoadLrdt(), "dd-MMM-yyyy hh:mm"));
        } else {
            lclssdetail.setGeneralLrdt(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatLrdt())) {
            lclssdetail.setHazmatLrdt(DateUtils.parseDate(lclAddVoyageForm.getHazmatLrdt(), "dd-MMM-yyyy hh:mm"));
        } else {
            lclssdetail.setHazmatLrdt(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDepartureId())) {
            UnLocation unlocation = unLocationDAO.findById(lclAddVoyageForm.getDepartureId());
            lclssdetail.setDeparture(unlocation);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getArrivalId())) {
            UnLocation unlocation = unLocationDAO.findById(lclAddVoyageForm.getArrivalId());
            lclssdetail.setArrival(unlocation);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAccountNumber())) {
            lclssdetail.setSpAcctNo(new TradingPartnerDAO().findById(lclAddVoyageForm.getAccountNumber()));
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDocReceived())) {
            lclssdetail.setDocReceived(DateUtils.parseDate(lclAddVoyageForm.getDocReceived(), "dd-MMM-yyyy"));
        }
        lclssdetail.setRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
        lclssdetail.setModifiedBy(getCurrentUser(request));
        lclssdetail.setModifiedDatetime(d);
        lclssdetail.setLclSsHeader(lclssheader);
        lclSsDetailDAO.saveOrUpdate(lclssdetail);
        lclssheader.setModifiedDatetime(d);
        lclssheaderdao.saveOrUpdate(lclssheader);
        setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        return mapping.findForward("importVoyage");
    }

    public ActionForward deleteVoyageDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId()) && CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
            LclSsDetail lclssdetail = lclssdetaildao.findById(lclAddVoyageForm.getDetailId());
            lclssdetaildao.delete(lclssdetail);
        }
        setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward("importVoyage");
    }

    public ActionForward editVoyageDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
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
            if (lclssdetail.getRemarks() != null) {
                lclAddVoyageForm.setRemarks(lclssdetail.getRemarks());
            }
            if (lclssdetail.getDocReceived() != null) {
                lclAddVoyageForm.setDocReceived(DateUtils.formatDate(lclssdetail.getDocReceived(), "dd-MMM-yyyy"));
            }
            request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        }
        return mapping.findForward(DISPLAY_DETAIL_POPUP);
    }

    public ActionForward saveImpUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitDAO lclunitdao = new LclUnitDAO();
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        LclUnitSsImportsDAO lclUnitSsImportsDAO = new LclUnitSsImportsDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        LclUnitWhseDAO lclUnitWhseDAO = new LclUnitWhseDAO();
        LclUnitSsManifestDAO lclUnitSsManifestDAO = new LclUnitSsManifestDAO();
        LclUnitSsImports lclUnitSsImports = null;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        Date d = new Date();
        User loginUser = getCurrentUser(request);
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        lclssheader.setModifiedDatetime(d);
        lclssheaderdao.saveOrUpdate(lclssheader);
        LclUnitSs lclunitSs = lclunitssdao.getByProperty("lclSsHeader.id", lclssheader.getId());
        LclUnit oldLclUnit = lclunitSs.getLclUnit();


        LclUnit lclUnit = lclunitdao.findById(lclAddVoyageForm.getUnitId());
        if (lclUnit == null) {
            lclUnit = new LclUnit();
            lclUnit.setEnteredBy(loginUser);
            lclUnit.setEnteredDatetime(d);
        }
        UnitType unittype = new UnitTypeDAO().findById(lclAddVoyageForm.getUnitType());
        lclUnit.setModifiedDatetime(d);
        lclUnit.setModifiedBy(loginUser);
        lclUnit.setUnitType(unittype);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatPermittedUnit())) {
            if (lclAddVoyageForm.getHazmatPermittedUnit().equalsIgnoreCase("Y")) {
                lclUnit.setHazmatPermitted(true);
            } else {
                lclUnit.setHazmatPermitted(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getRefrigerationPermittedUnit())) {
            if (lclAddVoyageForm.getRefrigerationPermittedUnit().equalsIgnoreCase("Y")) {
                lclUnit.setRefrigerated(true);
            } else {
                lclUnit.setRefrigerated(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitNo())) {
            lclUnit.setUnitNo(lclAddVoyageForm.getUnitNo().toUpperCase());
        }
        lclunitdao.saveOrUpdate(lclUnit);

        if (lclunitSs == null) {
            lclunitSs = new LclUnitSs();
            lclunitSs.setEnteredBy(loginUser);
            lclunitSs.setEnteredDatetime(d);
            lclunitSs.setStatus("E");
        }
        lclunitSs.setLclUnit(lclUnit);
        lclunitSs.setCob(false);
        lclunitSs.setOptDocnoLg(false);
        lclunitSs.setModifiedBy(loginUser);
        lclunitSs.setModifiedDatetime(d);
        lclunitSs.setLclSsHeader(lclssheader);


        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getIntermodalProvided())) {
            if (lclAddVoyageForm.getIntermodalProvided().equalsIgnoreCase("Y")) {
                lclunitSs.setIntermodalProvided(true);
            } else {
                lclunitSs.setIntermodalProvided(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDrayageProvided())) {
            if (lclAddVoyageForm.getDrayageProvided().equalsIgnoreCase("Y")) {
                lclunitSs.setDrayageProvided(true);
            } else {
                lclunitSs.setDrayageProvided(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStopoff())) {
            if (lclAddVoyageForm.getStopoff().equalsIgnoreCase("Y")) {
                lclunitSs.setStopOff(true);
            } else {
                lclunitSs.setStopOff(false);
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getChasisNo())) {
            lclunitSs.setChassisNo(lclAddVoyageForm.getChasisNo().toUpperCase());
        } else {
            lclunitSs.setChassisNo("");
        }
        if (lclAddVoyageForm.getRemarks() != null && !lclAddVoyageForm.getRemarks().trim().equals("")) {
            lclunitSs.setTruckingRemarks(lclAddVoyageForm.getRemarks().toUpperCase());
        } else {
            lclunitSs.setTruckingRemarks("");
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getBookingNumber())) {
            lclunitSs.setSpBookingNo(lclAddVoyageForm.getBookingNumber());
        } else {
            lclunitSs.setSpBookingNo("");
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getSUHeadingNote())) {
            lclunitSs.setSUHeadingNote(lclAddVoyageForm.getSUHeadingNote().toUpperCase());
        } else {
            lclunitSs.setSUHeadingNote("");
        }
        lclunitSs.setPrepaidCollect(lclAddVoyageForm.getPrepaidCollect());
        lclunitSs.setLclUnit(lclUnit);

        lclunitssdao.saveOrUpdate(lclunitSs);




        if (oldLclUnit != null) {
            StringBuilder remarks = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitNo()) && !oldLclUnit.getUnitNo().equalsIgnoreCase(lclAddVoyageForm.getUnitNo())) {
                remarks.append("Unit # -> ").append(oldLclUnit.getUnitNo()).append(" to ").append(lclAddVoyageForm.getUnitNo().toUpperCase()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitType()) && !oldLclUnit.getUnitType().getId().equals(lclAddVoyageForm.getUnitType())) {
                remarks.append("Size -> ").append(oldLclUnit.getUnitType().getDescription()).append(" to ").append(unittype.getDescription()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHazmatPermitted()) && oldLclUnit.getHazmatPermitted() != Boolean.valueOf(lclAddVoyageForm.getHazmatPermitted())) {
                remarks.append("Hazardous -> ").append(oldLclUnit.getHazmatPermitted()).append(" to ").append(lclAddVoyageForm.getHazmatPermitted()).append(" ");
            }
            if (CommonUtils.isNotEmpty(remarks)) {
                new LclUnitSsRemarksDAO().insertLclunitRemarks(lclssheader.getId(), lclUnit.getId(), "auto", remarks.toString(), loginUser.getUserId());
            }
        }

        List<LclUnitSs> lclUnitSsList = new ArrayList<LclUnitSs>();
        lclUnitSsList.add(lclunitSs);
        lclUnit.setLclUnitSsList(lclUnitSsList);

        lclUnitSsImports = lclUnitSsImportsDAO.getLclUnitSSImportsByHeader(null, lclssheader.getId());
        if (lclUnitSsImports == null) {
            lclUnitSsImports = new LclUnitSsImports();
            lclUnitSsImports.setEnteredBy(loginUser);
            lclUnitSsImports.setEnteredDatetime(d);
            lclUnitSsImports.setLclSsHeader(lclssheader);
        }
        lclUnitSsImports.setLclUnit(lclUnit);
        lclUnitSsImports.setModifiedDatetime(d);
        lclUnitSsImports.setModifiedBy(loginUser);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getOriginAcctNo())) {
            lclUnitSsImports.setOriginAcctNo(tradingPartnerDAO.findById(lclAddVoyageForm.getOriginAcctNo()));
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getColoaderAcctNo())) {
            lclUnitSsImports.setColoaderAcctNo(tradingPartnerDAO.findById(lclAddVoyageForm.getColoaderAcctNo()));
        } else {
            lclUnitSsImports.setColoaderAcctNo(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getColoaderDevngAcctNo())) {
            lclUnitSsImports.setColoaderDevanningAcctNo(tradingPartnerDAO.findById(lclAddVoyageForm.getColoaderDevngAcctNo()));
        } else {
            lclUnitSsImports.setColoaderDevanningAcctNo(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getItPortId())) {
            lclUnitSsImports.setItPortId(unLocationDAO.findById(lclAddVoyageForm.getItPortId()));
        } else {
            lclUnitSsImports.setItPortId(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitsWarehouseId())) {
            lclUnitSsImports.setUnitWareHouseId(warehouseDAO.findById(lclAddVoyageForm.getUnitsWarehouseId()));
        } else {
            lclUnitSsImports.setUnitWareHouseId(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getCfsWarehouseId())) {
            lclUnitSsImports.setCfsWarehouseId(warehouseDAO.findById(lclAddVoyageForm.getCfsWarehouseId()));
        } else {
            lclUnitSsImports.setCfsWarehouseId(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getLastFreeDate())) {
            lclUnitSsImports.setLastFreeDate(DateUtils.parseDate(lclAddVoyageForm.getLastFreeDate(), "dd-MMM-yyyy"));
        } else {
            lclUnitSsImports.setLastFreeDate(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getItDatetime())) {
            lclUnitSsImports.setItDatetime(DateUtils.parseDate(lclAddVoyageForm.getItDatetime(), "dd-MMM-yyyy"));
        } else {
            lclUnitSsImports.setItDatetime(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getGoDate())) {
            lclUnitSsImports.setGoDate(DateUtils.parseDate(lclAddVoyageForm.getGoDate(), "dd-MMM-yyyy"));
        } else {
            lclUnitSsImports.setGoDate(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getItNo())) {
            lclUnitSsImports.setItNo(lclAddVoyageForm.getItNo().toUpperCase());
        } else {
            lclUnitSsImports.setItNo(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getApproxDueDate())) {
            lclUnitSsImports.setApproxDueDate(DateUtils.parseDate(lclAddVoyageForm.getApproxDueDate(), "dd-MMM-yyyy"));
        } else {
            lclUnitSsImports.setApproxDueDate(null);
        }
        lclUnitSsImportsDAO.saveOrUpdate(lclUnitSsImports);
        LclUnitSsManifest lclUnitSsManifest = lclUnitSsManifestDAO.getLclUnitSSManifestByHeader(null, lclssheader.getId());
        if (lclUnitSsManifest == null) {
            lclUnitSsManifest = new LclUnitSsManifest();
            lclUnitSsManifest.setEnteredByUser(loginUser);
            lclUnitSsManifest.setEnteredDatetime(d);
            lclUnitSsManifest.setCalculatedBlCount(0);
            lclUnitSsManifest.setCalculatedDrCount(0);
            lclUnitSsManifest.setCalculatedTotalPieces(0);
            lclUnitSsManifest.setCalculatedVolumeImperial(new BigDecimal(0.00));
            lclUnitSsManifest.setCalculatedVolumeMetric(new BigDecimal(0.00));
            lclUnitSsManifest.setCalculatedWeightImperial(new BigDecimal(0.00));
            lclUnitSsManifest.setCalculatedWeightMetric(new BigDecimal(0.00));
            lclUnitSsManifest.setLclSsHeader(lclssheader);
        }
        lclUnitSsManifest.setLclUnit(lclUnit);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getMasterBL())) {
            lclUnitSsManifest.setMasterbl(lclAddVoyageForm.getMasterBL().toUpperCase());
        } else {
            lclUnitSsManifest.setMasterbl("");
        }
        lclUnitSsManifest.setModifiedByUser(loginUser);
        lclUnitSsManifest.setModifiedDatetime(d);
        lclUnitSsManifestDAO.save(lclUnitSsManifest);

        LclUnitWhse lclUnitWhse = lclUnitWhseDAO.getLclUnitWhseDetails(null, lclssheader.getId());
        if (lclUnitWhse == null) {
            lclUnitWhse = new LclUnitWhse();
            lclUnitWhse.setEnteredBy(getCurrentUser(request));
            lclUnitWhse.setEnteredDatetime(d);
            Integer warehse = warehouseDAO.warehouseNo(lclssheader.getDestination().getUnLocationCode());
            if (CommonUtils.isNotEmpty(warehse)) {
                lclUnitWhse.setWarehouse(warehouseDAO.findById(warehse));
            } else {
                Warehouse warehouse = warehouseDAO.getByProperty("warehouseNo", "T99");
                lclUnitWhse.setWarehouse(warehouse);
            }
        }
        lclUnitWhse.setModifiedBy(loginUser);
        lclUnitWhse.setModifiedDatetime(d);
        lclUnitWhse.setLclSsHeader(lclssheader);
        lclUnitWhse.setLclUnit(lclUnit);
        lclUnitWhse.setSealNoIn(CommonUtils.isNotEmpty(lclAddVoyageForm.getSealNoIn())
                ? lclAddVoyageForm.getSealNoIn().toUpperCase() : null);
        lclUnitWhse.setSealNoOut(CommonUtils.isNotEmpty(lclAddVoyageForm.getSealNoOut())
                ? lclAddVoyageForm.getSealNoOut().toUpperCase() : null);
        lclUnitWhse.setLocation(CommonUtils.isNotEmpty(lclAddVoyageForm.getDoorNumber())
                ? lclAddVoyageForm.getDoorNumber().toUpperCase() : null);
        UserDAO userDAO = new UserDAO();
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStuffedByUserId())) {
            lclUnitWhse.setStuffedByUser(userDAO.findById(Integer.parseInt(lclAddVoyageForm.getStuffedByUserId())));
        } else {
            lclUnitWhse.setStuffedByUser(null);
        }
        lclUnitWhse.setStuffedDatetime(null);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getStrippedDate())) {
            lclUnitWhse.setDestuffedDatetime(DateUtils.parseDate(lclAddVoyageForm.getStrippedDate(), "dd-MMM-yyyy"));
        } else {
            lclUnitWhse.setDestuffedDatetime(null);
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDestuffedByUserId())) {
            lclUnitWhse.setDestuffedByUser(userDAO.findById(Integer.parseInt(lclAddVoyageForm.getDestuffedByUserId())));
        } else {
            lclUnitWhse.setDestuffedByUser(null);
        }
        lclUnitWhseDAO.save(lclUnitWhse);

        LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
        LclSsDetail ssDetail = new LclSsDetailDAO().findByTransMode(lclAddVoyageForm.getHeaderId(), "V");
        List<LclUnitSsDispo> lclDispositionList = lclUnitSsDispoDAO.getDispositionList(null, ssDetail.getId());
        if (CommonUtils.isNotEmpty(lclDispositionList)
                && oldLclUnit != null && !oldLclUnit.getUnitNo().equalsIgnoreCase(lclAddVoyageForm.getUnitNo())) {
            for(LclUnitSsDispo lclUnitSsDispo:lclDispositionList){
                lclUnitSsDispo.setLclUnit(lclUnit);
                lclUnitSsDispo.setModifiedBy(loginUser);
                lclUnitSsDispo.setModifiedDatetime(d);
                lclUnitSsDispoDAO.update(lclUnitSsDispo);
            }
        }
        if (lclDispositionList == null || lclDispositionList.isEmpty()) {
            DispositionDAO dispdao = new DispositionDAO();
            Disposition disp = dispdao.getByProperty("eliteCode", "DATA");
            if (disp != null) {
                LclUnitSsDispo lclUnitSsDispo = new LclUnitSsDispo();
                lclUnitSsDispo.setDisposition(disp);
                lclUnitSsDispo.setModifiedBy(loginUser);
                lclUnitSsDispo.setEnteredBy(loginUser);
                lclUnitSsDispo.setModifiedDatetime(d);
                lclUnitSsDispo.setDispositionDatetime(d);
                lclUnitSsDispo.setEnteredDatetime(d);
                lclUnitSsDispo.setLclUnit(lclUnit);
                lclUnitSsDispo.setLclSsDetail(ssDetail);
                lclUnitSsDispoDAO.save(lclUnitSsDispo);
            }
        }
        setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward("importVoyage");
    }

    public ActionForward deleteUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (lclAddVoyageForm.getUnitId() != null && lclAddVoyageForm.getUnitId() > 0) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddVoyageForm.getUnitId());
            lclunitdao.delete(lclunit);
        }
        setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        request.setAttribute("openPopup", "notopen");
        return mapping.findForward("importVoyage");
    }

    public ActionForward redirectImportsVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        User loginUser = getCurrentUser(request);
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        if (lclAddVoyageForm.getPostFlag() != null && !"".equalsIgnoreCase(lclAddVoyageForm.getPostFlag()) && "Acct_Post".equalsIgnoreCase(lclAddVoyageForm.getPostFlag())) {
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            String realPath = request.getSession().getServletContext().getRealPath("/");
            lclManifestDAO.getAllManifestImportsBookingsByUnitSS(lclAddVoyageForm.getUnitssId(), null, lclAddVoyageForm.getFilterByChanges(),
                    loginUser, true, realPath, true, lclAddVoyageForm.getFileNumberId());
            LclUnit lclunit = new LclUnitDAO().findById(lclAddVoyageForm.getUnitId());
            String HDLG_MIA = new PropertyDAO().getProperty("auto.cost.miami.warehouse.handling");
            if (!lclunit.getUnitType().getDescription().equalsIgnoreCase("coload") && "on".equalsIgnoreCase(HDLG_MIA)) {
                LclBookingAc handlingCharge = lclCostChargeDAO.getLclBookingAcByChargeCode(new Long(lclAddVoyageForm.getFileNumberId()), "HDLG-MIA");
                GlMappingDAO glDao = new GlMappingDAO();
                GlMapping apGlmapping = glDao.findByChargeCode("HDLG-MIA", "LCLI", "AC");
                LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclAddVoyageForm.getFileNumberId()));
                boolean podfdFlag = false;
                if (lclBooking.getBookingType().equals("I") && (lclBooking.getPortOfDestination() != null && lclBooking.getFinalDestination() != null
                        && lclBooking.getPortOfDestination() == lclBooking.getFinalDestination()
                        && lclBooking.getFinalDestination().getUnLocationCode() != null
                        && lclBooking.getFinalDestination().getUnLocationCode().equalsIgnoreCase("USMIA"))) {
                    podfdFlag = true;
                }
                if (podfdFlag && handlingCharge == null) {
                    List<LclBookingPiece> lclBookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclAddVoyageForm.getFileNumberId()));
                    LclBookingPiece lclBookingPiece = lclBookingPieceList.get(0);
                    if (lclBookingPiece != null && lclBookingPiece.getBookedVolumeMetric() != null && !lclBookingPiece.getBookedVolumeMetric().toString().equals("0.000")) {
                        Double cbm = Double.parseDouble(lclBookingPiece.getBookedVolumeMetric().toString());
                        Double apAmount = cbm * 12;
                        LclBookingAc lclBookingAc = lclUtils.saveBookingAc(Long.parseLong(lclAddVoyageForm.getFileNumberId()), apGlmapping,
                                apGlmapping, new BigDecimal(apAmount), BigDecimal.ZERO, lclBooking.getBillToParty(),
                                "V", BigDecimal.ZERO, BigDecimal.ZERO, "", "", getCurrentUser(request));
                        lclBookingAc.setManualEntry(false);
                        lclBookingAc.setRatePerVolumeUnit(new BigDecimal(12));
                        lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                        lclBookingAc.setRatePerWeightUnit(BigDecimal.ZERO);
                        lclBookingAc.setRateFlatMinimum(BigDecimal.ZERO);
                        lclBookingAc.setRateFlatMaximum(BigDecimal.ZERO);
                        TradingPartner vendor = new TradingPartnerDAO().findById("ECOCON0018");
                        lclBookingAc.setSupAcct(vendor);
                        new LclCostChargeDAO().saveOrUpdate(lclBookingAc);
//                        new LclManifestDAO().createLclAccruals(lclBookingAc, "LCLI", "Open");
//                        lclUtils.insertLCLBookingAcTrans(lclBookingAc, "AC", "A", "", lclBookingAc.getApAmount(), getCurrentUser(request));
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFileNumber()) && null != loginUser) {
            new DBUtil().releaseLockByRecordIdAndModuleId(lclAddVoyageForm.getFileNumber(), "LCL FILE", loginUser.getUserId());
        }
        setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        request.setAttribute("searchOriginId", request.getParameter("searchOriginId"));
        request.setAttribute("searchOriginName", request.getParameter("searchOriginName"));
        request.setAttribute("searchDestinationId", request.getParameter("searchDestinationId"));
        request.setAttribute("searchDestinationName", request.getParameter("searchDestinationName"));

        request.setAttribute("billsTerminal", request.getParameter("billsTerminal"));
        request.setAttribute("billsTerminalNo", request.getParameter("billsTerminalNo"));
        request.setAttribute("dispositionCode", request.getParameter("dispositionCode"));
        request.setAttribute("dispositionId", request.getParameter("dispositionId"));
        request.setAttribute("voyageNo", request.getParameter("voyageNo"));
        request.setAttribute("unitNo1", request.getParameter("unitNo1"));
        request.setAttribute("masterBL1", request.getParameter("masterBL1"));
        request.setAttribute("agentNo", request.getParameter("agentNo"));
        return mapping.findForward("importVoyage");
    }

    public void clearHeaderValues(LclAddVoyageForm lclAddVoyageForm) {
        lclAddVoyageForm.setMethodName(null);
        lclAddVoyageForm.setScheduleNo("");
        lclAddVoyageForm.setSailDate("");
        lclAddVoyageForm.setEtaPod("");
        lclAddVoyageForm.setHazmatLrdt("");
        lclAddVoyageForm.setLoadLrdt("");
        lclAddVoyageForm.setRemarks("");
        lclAddVoyageForm.setDocReceived("");
    }

    public void setLclUnitSS(LclUnitSs lclunitss, LclSsHeader lclssheader, HttpServletRequest request, Date d) {
        lclunitss.setCob(false);
        lclunitss.setStatus("E");
        lclunitss.setOptDocnoLg(false);
        lclunitss.setModifiedBy(getCurrentUser(request));
        lclunitss.setModifiedDatetime(d);
        lclunitss.setLclSsHeader(lclssheader);
    }

    public ActionForward copyVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getDetailId())) {
            LclSsHeader lclssheader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
            if (lclssheader != null && lclssheader.getBillingTerminal() != null) {
                lclAddVoyageForm.setBillTerminalNo(lclssheader.getBillingTerminal().getTrmnum());
                lclAddVoyageForm.setBillTerminal(lclssheader.getBillingTerminal().getTerminalLocation() + "/" + lclssheader.getBillingTerminal().getTrmnum());
            }
            String origin = lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getOrigin());
            String destination = lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getDestination());
            LclSsDetail lclssdetail = new LclSsDetailDAO().findById(lclAddVoyageForm.getDetailId());
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
            if (lclssdetail.getSpAcctNo() != null) {
                if (lclssdetail.getSpAcctNo().getAccountno() != null && !lclssdetail.getSpAcctNo().getAccountno().trim().equals("")) {
                    lclAddVoyageForm.setAccountNumber(lclssdetail.getSpAcctNo().getAccountno());
                }
                if (lclssdetail.getSpAcctNo().getAccountName() != null && !lclssdetail.getSpAcctNo().getAccountName().trim().equals("")) {
                    lclAddVoyageForm.setAccountName(lclssdetail.getSpAcctNo().getAccountName());
                }
            }
            if (lclAddVoyageForm.getUnitId() != null) {
                LclUnitSsImports lclUnitSsImports = new LclUnitSsImportsDAO().getLclUnitSSImportsByHeader(lclAddVoyageForm.getUnitId(), lclAddVoyageForm.getHeaderId());
                if (lclUnitSsImports != null) {
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getOriginAcctNo()) && CommonUtils.isNotEmpty(lclUnitSsImports.getOriginAcctNo().getAccountno())) {
                        lclAddVoyageForm.setOriginAcctNo(lclUnitSsImports.getOriginAcctNo().getAccountno());
                        lclAddVoyageForm.setOriginAcct(lclUnitSsImports.getOriginAcctNo().getAccountName());
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderAcctNo()) && CommonUtils.isNotEmpty(lclUnitSsImports.getColoaderAcctNo().getAccountno())) {
                        lclAddVoyageForm.setColoaderAcctNo(lclUnitSsImports.getColoaderAcctNo().getAccountno());
                        lclAddVoyageForm.setColoaderAcct(lclUnitSsImports.getColoaderAcctNo().getAccountName());
                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getUnitWareHouseId())) {
                        lclAddVoyageForm.setUnitsWarehouseId(lclUnitSsImports.getUnitWareHouseId().getId());
                        lclAddVoyageForm.setUnitWarehouse(lclUnitSsImports.getUnitWareHouseId().getWarehouseName() + " - " + lclUnitSsImports.getUnitWareHouseId().getWarehouseNo());

                    }
                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId())) {
                        lclAddVoyageForm.setCfsWarehouseId(lclUnitSsImports.getCfsWarehouseId().getId());
                        lclAddVoyageForm.setCfsWarehouse(lclUnitSsImports.getCfsWarehouseId().getWarehouseName() + " - " + lclUnitSsImports.getCfsWarehouseId().getWarehouseNo());
                    }
                }
            }

            request.setAttribute("originId", lclssheader.getOrigin().getId());
            request.setAttribute("originalOriginId", lclssheader.getOrigin().getId());
            request.setAttribute("originValue", origin);
            request.setAttribute("polUnlocationCode", origin.substring(origin.indexOf("(") + 1, origin.indexOf(")")));
            request.setAttribute("originalOriginName", origin);

            request.setAttribute("destinationId", lclssheader.getDestination().getId());
            request.setAttribute("originalDestinationId", lclssheader.getDestination().getId());
            request.setAttribute("originalDestinationName", destination);
            request.setAttribute("destinationValue", destination);
        }

        lclAddVoyageForm.setHeaderId(null);
        lclAddVoyageForm.setUnitId(null);
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("openPopup", "openPopup");
        return mapping.findForward("importVoyage");
    }

    public ActionForward linkDRDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
            lclAddVoyageForm.setPolUnlocationCode(lclSsHeader.getOrigin().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
            lclAddVoyageForm.setFdUnlocationCode(lclSsHeader.getDestination().getUnLocationCode());
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("linkDrPopup");
    }

    public ActionForward savelinkDR(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getFileNumber())) {
            LclFileNumberDAO lclFilenumberDAO = new LclFileNumberDAO();
            checkBkgStatusSave(lclAddVoyageForm, request, lclFilenumberDAO);
        }
        return mapping.findForward("linkDrPopup");
    }

    private void checkBkgStatusSave(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request, LclFileNumberDAO lclFilenumberDAO) throws Exception {
        lclAddVoyageForm.setFileNumber(lclAddVoyageForm.getFileNumber().toUpperCase());
        Object[] lclFileNumberObject = lclFilenumberDAO.checkImpBkg(lclAddVoyageForm.getFileNumber());
        User user = (User) request.getSession().getAttribute("loginuser");
        if (lclFileNumberObject != null && lclFileNumberObject.length > 0 && lclAddVoyageForm.getHeaderId() != null) {
            String state = lclFileNumberObject[1].toString();
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclFileNumberObject[0].toString()));
            LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(lclAddVoyageForm.getHeaderId());
            if (!state.equalsIgnoreCase("Q")) {
                if (lclFileNumberObject[2] != null && !lclFileNumberObject[2].equals("")) {
                    Integer polId = Integer.parseInt(lclFileNumberObject[2].toString());
                    if (lclFileNumberObject[3] != null && !lclFileNumberObject[3].equals("")) {
                        Integer podId = Integer.parseInt(lclFileNumberObject[3].toString());
                        if (polId.intValue() == lclSsHeader.getOrigin().getId().intValue() && podId.intValue() == lclSsHeader.getDestination().getId().intValue()) {
                            LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclAddVoyageForm.getHeaderId(), "V");
                            String disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(lclAddVoyageForm.getUnitId(), lclSsDetail.getId());
                            RoleDuty roleDuty = new RoleDutyDAO().getRoleDetails(user.getRole().getRoleId());
                            if (disposition.equalsIgnoreCase("PORT") && roleDuty.isLinkDrAfterDispositionPort()) {
                                if (lclBooking.getBillToParty().equals("C") && lclBooking.getConsAcct() == null) {
                                    lclAddVoyageForm.setMessage("Consignee is Required for this D/R# " + lclAddVoyageForm.getFileNumber());
                                } else if (lclBooking.getBillToParty().equals("N") && lclBooking.getNotyAcct() == null) {
                                    lclAddVoyageForm.setMessage("Notify is Required for this D/R# " + lclAddVoyageForm.getFileNumber());
                                } else if (lclBooking.getBillToParty().equals("T") && lclBooking.getThirdPartyAcct() == null) {
                                    lclAddVoyageForm.setMessage("Third Party is Required for this D/R# " + lclAddVoyageForm.getFileNumber());
                                } else {
                                    pickFileNumber(lclAddVoyageForm, request, Long.parseLong(lclFileNumberObject[0].toString()));
                                }
                            } else {
                                pickFileNumber(lclAddVoyageForm, request, Long.parseLong(lclFileNumberObject[0].toString()));
                            }
                        } else {
                            lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " POL and POD does not matched");
                        }
                    } else {
                        lclAddVoyageForm.setMessage("POD is Required for this D/R# " + lclAddVoyageForm.getFileNumber());
                    }
                } else {
                    lclAddVoyageForm.setMessage("POL is Required for this D/R# " + lclAddVoyageForm.getFileNumber());
                }
            } else {
                lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " Not Converted To Booking");
            }
        } else {
            lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " does not exists");
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
    }

    private void pickFileNumber(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request, Long fileNumberId) throws Exception {
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        List<LclBookingPiece> lclBookingPiecesList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            //Insert Into Notes
            lclUtils.insertLCLRemarks(fileNumberId, "Linked to Unit# " + lclAddVoyageForm.getUnitNo() + " of Voyage# " + lclAddVoyageForm.getScheduleNo() + "", REMARKS_DR_AUTO_NOTES, user);
            for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                List<LclBookingPieceUnit> lclBookingPieceUnitlist = new LclBookingPieceUnitDAO().findByProperty("lclBookingPiece.id", lclBookingPiece.getId());
                //checking also wheather Dr exist in Eculine or not**************
                List<BlInfo> BlInfoList = (List<BlInfo>) new BlInfoDao().findByProperty("fileNo.id", fileNumberId);
                if (lclBookingPieceUnitlist.isEmpty() && CommonUtils.isEmpty(BlInfoList)) {
                    //Insert Into LclBookingPieceUnit
                    lclUtils.insertlclBkgpieceUnit(lclBookingPiece.getId(), lclAddVoyageForm.getUnitssId(), user.getUserId());
                    lclAddVoyageForm.setMessage("D/R# Successfully Linked !");
                } else {
                    lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " already Picked");
                }
            }
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileNumberId);
            LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclAddVoyageForm.getHeaderId(), "V");
            String disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(lclAddVoyageForm.getUnitId(), lclSsDetail.getId());
            RoleDuty roleDuty = new RoleDutyDAO().getRoleDetails(user.getRole().getRoleId());
            if (disposition.equalsIgnoreCase("PORT") && roleDuty.isLinkDrAfterDispositionPort()) {
                if (lclBooking.getBookingType().equalsIgnoreCase("I")) {
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    new LclManifestDAO().getAllManifestImportsBookingsByUnitSS(lclAddVoyageForm.getUnitssId(), null, null,
                            getCurrentUser(request), true, realPath, true, null);
                } else {
                    new LclFileNumberDAO().updateLclFileNumbersStatus(fileNumberId.toString(), "M");
                }
            }
        } else {
            lclAddVoyageForm.setMessage("D/R# " + lclAddVoyageForm.getFileNumber() + " Commodity is not exists");
        }
    }

    public ActionForward refreshUnitsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            setAllVoyageValues(lclAddVoyageForm, request);//common method to set all values
        }
        return mapping.findForward("importVoyage");
    }

    public ActionForward viewDispDRSPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        request.setAttribute("drList", lclUnitDAO.getDRSForImportsDisp(lclAddVoyageForm.getUnitssId()));
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("noAlertFlag", request.getParameter("noAlertFlag"));
        return mapping.findForward(DISPLAY_DISP_VIEW_DRS_POPUP);
    }

    public ActionForward viewDispDRSEditContactPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        request.setAttribute("noAlertFlag", request.getParameter("noalertFlag"));
        request.setAttribute("drList", lclUnitSsDAO.getListOFContactFromDrs(lclAddVoyageForm.getUnitssId()));
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward(DISPLAY_DISP_VIEW_DRS_EDIT_CONTACT_POPUP);
    }

    public ActionForward validateChargesBillToParty(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        String errorMessage = "";
        LclUtils utils = new LclUtils();
        errorMessage = utils.getBillingTypeErrorMessage(lclAddVoyageForm.getUnitssId(), "Imports", lclAddVoyageForm.getPostFlag());
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward unmanifest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclManifestDAO manifestDAO = new LclManifestDAO();
        manifestDAO.getAllManifestImportsBookingsByUnitSS(lclAddVoyageForm.getUnitssId(), null, lclAddVoyageForm.getButtonValue(),
                getCurrentUser(request), false, "", true, null);
        lclUtils.updateLCLUnitSSStatus(lclAddVoyageForm.getUnitssId(), "E", getCurrentUser(request));
        setAllVoyageValues(lclAddVoyageForm, request);
        return mapping.findForward("importVoyage");
    }

    public void setAllVoyageValues(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request) throws Exception {
        LclImportUtils lclImportUtils = new LclImportUtils();
        //  LclUnitDAO lclUnitDAO = new LclUnitDAO();
        //    SearchDAO searchDAO = new SearchDAO();
        lclImportUtils.setVoyageAndUnitValues(lclAddVoyageForm, request);
        List<LclImportUnitBkgModel> pickedDrList = new ImportVoyageSearchDAO().getPickedDrList(lclAddVoyageForm);
        request.setAttribute("pickedDrList", pickedDrList);
        LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();
        request.setAttribute("outSourceRemarks", lclUnitSsRemarksDAO.isRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "Outsource"));
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

    public ActionForward displayOSDDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclUnitWhseDAO lclUnitWhseDAO = new LclUnitWhseDAO();
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitWhse lclUnitWhse = lclUnitWhseDAO.getLclUnitWhseDetails(lclAddVoyageForm.getUnitId(), lclAddVoyageForm.getHeaderId());
        request.setAttribute("lclUnitWhse", lclUnitWhse);
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        return mapping.findForward("openCOBPopup");
    }

    public ActionForward saveOSDDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();
        LclUnitWhseDAO lclUnitWhseDAO = new LclUnitWhseDAO();
        Date now = new Date();
        LclUnitWhse lclUnitWhse = lclUnitWhseDAO.getLclUnitWhseDetails(lclAddVoyageForm.getUnitId(), lclAddVoyageForm.getHeaderId());
        lclUnitWhse.setOsdDatetime(now);
        lclUnitWhse.setOsdUser(getCurrentUser(request));
        lclUnitWhse.setModifiedDatetime(now);
        lclUnitWhse.setModifiedBy(getCurrentUser(request));
        lclUnitWhseDAO.saveOrUpdate(lclUnitWhse);
        lclUnitSsRemarksDAO.insertLclunitRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "auto", "OSD is Confirmed", getCurrentUser(request).getUserId());
        setAllVoyageValues(lclAddVoyageForm, request);
        return mapping.findForward("importVoyage");
    }

    public ActionForward clearOSDDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();
        LclUnitWhseDAO lclUnitWhseDAO = new LclUnitWhseDAO();
        Date now = new Date();
        LclUnitWhse lclUnitWhse = lclUnitWhseDAO.getLclUnitWhseDetails(lclAddVoyageForm.getUnitId(), lclAddVoyageForm.getHeaderId());
        lclUnitWhse.setOsdDatetime(null);
        lclUnitWhse.setOsdUser(null);
        lclUnitWhse.setModifiedDatetime(now);
        lclUnitWhse.setModifiedBy(getCurrentUser(request));
        lclUnitWhseDAO.saveOrUpdate(lclUnitWhse);
        lclUnitSsRemarksDAO.insertLclunitRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "auto", "OSD is Reset", getCurrentUser(request).getUserId());
        setAllVoyageValues(lclAddVoyageForm, request);
        return mapping.findForward("importVoyage");
    }

    public ActionForward updateClosedAuditedRemarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
        User user = getCurrentUser(request);
        LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();
        LclSsHeader lclssheader = lclssheaderdao.findById(lclAddVoyageForm.getHeaderId());
        Date d = new Date();
        StringBuilder remarks = new StringBuilder();
        String audited = null;
        if (lclAddVoyageForm.getButtonValue() != null && lclssheader != null) {
            if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Close")) {
                lclssheader.setClosedBy(user);
                lclssheader.setClosedDatetime(d);
                remarks.append("Voyage Closed Remarks ->").append("Voyage No ->").append(lclssheader.getScheduleNo());
                remarks.append(" Billing Terminal ->").append(lclssheader.getBillingTerminal().getTrmnum()).append("-");
                remarks.append(lclssheader.getBillingTerminal().getTerminalLocation()).append(" POL ->");
                remarks.append(lclssheader.getOrigin().getUnLocationName()).append("(").append(lclssheader.getOrigin().getUnLocationCode());
                remarks.append(") ").append("POD ->").append(lclssheader.getDestination().getUnLocationName()).append("(");
                remarks.append(lclssheader.getDestination().getUnLocationCode()).append(")");
                lclUnitSsRemarksDAO.insertLclunitRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "auto", remarks.toString(), user.getUserId());
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Reopen")) {
                lclssheader.setClosedBy(null);
                lclssheader.setClosedDatetime(null);
                lclssheader.setReopenedBy(user);
                lclssheader.setReopenedDatetime(d);
                remarks.append("Voyage Reopened Remarks ->").append("Voyage No ->").append(lclssheader.getScheduleNo());
                remarks.append(" Billing Terminal ->").append(lclssheader.getBillingTerminal().getTerminalLocation()).append("-");
                remarks.append(lclssheader.getBillingTerminal().getTrmnam()).append(" POL ->");
                remarks.append(lclssheader.getOrigin().getUnLocationName()).append("(").append(lclssheader.getOrigin().getUnLocationCode());
                remarks.append(") ").append("POD ->").append(lclssheader.getDestination().getUnLocationName()).append("(");
                remarks.append(lclssheader.getDestination().getUnLocationCode()).append(")");
                lclUnitSsRemarksDAO.insertLclunitRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "auto", remarks.toString(), user.getUserId());
            }
            if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Audit")) {
                lclssheader.setAuditedBy(user);
                lclssheader.setAuditedDatetime(d);
                audited = "Voyage Audited";
                if (CommonUtils.isNotEmpty(lclAddVoyageForm.getAuditedRemarks())) {
                    lclssheader.setAuditedRemarks(lclAddVoyageForm.getAuditedRemarks().toUpperCase());
                    audited += ", Audited Remarks ->" + lclAddVoyageForm.getAuditedRemarks().toUpperCase();
                }
                lclUnitSsRemarksDAO.insertLclunitRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "auto", audited, user.getUserId());
            } else if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Audited")) {
                lclssheader.setAuditedBy(null);
                lclssheader.setAuditedDatetime(null);
                audited = "Voyage Un-Audited";
                lclUnitSsRemarksDAO.insertLclunitRemarks(lclAddVoyageForm.getHeaderId(), lclAddVoyageForm.getUnitId(), "auto", audited, user.getUserId());
            }
        }
        if (lclAddVoyageForm.getButtonValue().equalsIgnoreCase("Close")) {
            lclssheader.setClosedRemarks(lclAddVoyageForm.getClosedRemarks().toUpperCase());
        } else {
            lclssheader.setReopenedRemarks(lclAddVoyageForm.getReopenedRemarks());
        }
        lclssheader.setModifiedDatetime(d);
        lclssheader.setModifiedBy(user);
        lclssheaderdao.update(lclssheader);
        request.setAttribute("lclssheader", lclssheader);
        setAllVoyageValues(lclAddVoyageForm, request);
        return mapping.findForward("importVoyage");
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SearchDAO searchDAO = new SearchDAO();
        String fileNumber = request.getParameter("fileNo");
        String userId = request.getParameter("loginUserId");
        searchDAO.checkLockingStatus(fileNumber, Integer.parseInt(userId), response);
        return null;
    }

    public ActionForward getNonInvoicedCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclAddVoyageForm lclAddVoyageForm = (LclAddVoyageForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String fileNo = lclUnitSsDAO.getNonInvoicedCharges(lclAddVoyageForm.getUnitssId());
        PrintWriter out = response.getWriter();
        out.print(null != fileNo ? fileNo : "");
        return null;
    }
}
