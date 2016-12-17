package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddUnitsForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.common.dao.PropertyDAO;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Meiyazhakan
 */
public class LclImportAddUnitsAction extends LogiwareDispatchAction {

    private static final Logger log = Logger.getLogger(LclImportAddUnitsAction.class);

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("displaySearch");
    }

    public ActionForward addUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        PortsDAO portsDAO = new PortsDAO();
        lclAddUnitsForm.setHazmatPermitted("N");
        lclAddUnitsForm.setDrayageProvided("N");
        lclAddUnitsForm.setStopoff("N");
        lclAddUnitsForm.setIntermodalProvided("N");
        Integer agentCount = portsDAO.getAgentCount(lclAddUnitsForm.getPolUnlocationCode(), "I");
        if (agentCount <= 1) {
            String agent[] = portsDAO.getDefaultAgentForLcl(lclAddUnitsForm.getPolUnlocationCode(), "I");
            lclAddUnitsForm.setOriginAcctNo(agent[0]);
            lclAddUnitsForm.setOriginAcct(agent[1]);
        }
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward editUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitSsManifestDAO lclUnitSsManifestDAO = new LclUnitSsManifestDAO();
        LclUtils lclUtils = new LclUtils();
        if (lclAddUnitsForm.getUnitId() != null && lclAddUnitsForm.getUnitId() > 0) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
            LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
            LclUnitSs lclunitss = null;
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitssId())) {
                lclunitss = lclunitssdao.findById(lclAddUnitsForm.getUnitssId());
            }
            if (null != lclunitss) {
                if (lclunitss.getDrayageProvided()) {
                    lclAddUnitsForm.setDrayageProvided("Y");
                } else {
                    lclAddUnitsForm.setDrayageProvided("N");
                }
                if (lclunitss.getIntermodalProvided()) {
                    lclAddUnitsForm.setIntermodalProvided("Y");
                } else {
                    lclAddUnitsForm.setIntermodalProvided("N");
                }
                if (lclunitss.getStopOff()) {
                    lclAddUnitsForm.setStopoff("Y");
                } else {
                    lclAddUnitsForm.setStopoff("N");
                }
                if (CommonUtils.isNotEmpty(lclunitss.getChassisNo())) {
                    lclAddUnitsForm.setChasisNo(lclunitss.getChassisNo());
                }
                lclAddUnitsForm.setSUHeadingNote(CommonUtils.isNotEmpty(lclunitss.getSUHeadingNote()) ? lclunitss.getSUHeadingNote() : "");
                lclAddUnitsForm.setPrepaidCollect(lclunitss.getPrepaidCollect());
                request.setAttribute("closedBy", lclunitss.getLclSsHeader().getClosedBy());
            }
            if (lclunit != null) {
                lclAddUnitsForm.setUnitNo(lclunit.getUnitNo());
                if (lclunit.getUnitType() != null && lclunit.getUnitType().getId() != null
                        && lclunit.getUnitType().getId() > 0) {
                    lclAddUnitsForm.setUnitType(lclunit.getUnitType().getId());
                    lclAddUnitsForm.setDupUnitTypeId(lclunit.getUnitType().getId());
                }
                if (lclunit.getHazmatPermitted()) {
                    lclAddUnitsForm.setHazmatPermitted("Y");
                } else {
                    lclAddUnitsForm.setHazmatPermitted("N");
                }
                lclAddUnitsForm.setRefrigerationPermitted("N");
                if (lclunit.getGrossWeightImperial() != null && lclunit.getGrossWeightImperial().doubleValue() > 0.00) {
                    lclAddUnitsForm.setGrossWeightImperial(lclunit.getGrossWeightImperial().toString());
                }
                if (lclunit.getGrossWeightMetric() != null && lclunit.getGrossWeightMetric().doubleValue() > 0.00) {
                    lclAddUnitsForm.setGrossWeightMetric(lclunit.getGrossWeightMetric().toString());
                }
                if (lclunit.getVolumeImperial() != null && lclunit.getVolumeImperial().doubleValue() > 0.00) {
                    lclAddUnitsForm.setVolumeImperial(lclunit.getVolumeImperial().toString());
                }
                if (lclunit.getVolumeMetric() != null && lclunit.getVolumeMetric().doubleValue() > 0.00) {
                    lclAddUnitsForm.setVolumeMetric(lclunit.getVolumeMetric().toString());
                }
                if (CommonUtils.isNotEmpty(lclunitss.getTruckingRemarks())) {
                    lclAddUnitsForm.setRemarks(lclunitss.getTruckingRemarks());
                }
                if (lclunitss != null && CommonUtils.isNotEmpty(lclunitss.getSpBookingNo())) {
                    lclAddUnitsForm.setBookingNumber(lclunitss.getSpBookingNo());
                }
            }
            LclUnitSsImportsDAO lclUnitSsImportsDAO = new LclUnitSsImportsDAO();
            LclUnitSsImports lclUnitSsImports = null;
            lclUnitSsImports = lclUnitSsImportsDAO.getLclUnitSSImportsByHeader(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
            if (lclUnitSsImports != null) {
                if (CommonFunctions.isNotNull(lclUnitSsImports.getOriginAcctNo()) && CommonUtils.isNotEmpty(lclUnitSsImports.getOriginAcctNo().getAccountno())) {
                    lclAddUnitsForm.setOriginAcctNo(lclUnitSsImports.getOriginAcctNo().getAccountno());
                    lclAddUnitsForm.setOriginAcct(lclUnitSsImports.getOriginAcctNo().getAccountName());
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderAcctNo()) && CommonUtils.isNotEmpty(lclUnitSsImports.getColoaderAcctNo().getAccountno())) {
                    lclAddUnitsForm.setColoaderAcctNo(lclUnitSsImports.getColoaderAcctNo().getAccountno());
                    lclAddUnitsForm.setColoaderAcct(lclUnitSsImports.getColoaderAcctNo().getAccountName());
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo()) && CommonUtils.isNotEmpty(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno())) {
                    lclAddUnitsForm.setColoaderDevngAcctNo(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno());
                    lclAddUnitsForm.setColoaderDevngAcctName(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName());
                }

                if (CommonFunctions.isNotNull(lclUnitSsImports.getItPortId())) {
                    lclAddUnitsForm.setItPort(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSsImports.getItPortId()));
                    lclAddUnitsForm.setItPortId(lclUnitSsImports.getItPortId().getId());
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getUnitWareHouseId())) {
                    lclAddUnitsForm.setUnitsWarehouseId(lclUnitSsImports.getUnitWareHouseId().getId());
                    lclAddUnitsForm.setUnitWarehouse(lclUnitSsImports.getUnitWareHouseId().getWarehouseName() + " - " + lclUnitSsImports.getUnitWareHouseId().getWarehouseNo());
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId())) {
                    lclAddUnitsForm.setCfsWarehouseId(lclUnitSsImports.getCfsWarehouseId().getId());
                    lclAddUnitsForm.setDupCfsId(lclUnitSsImports.getCfsWarehouseId().getId());
                    lclAddUnitsForm.setCfsWarehouse(lclUnitSsImports.getCfsWarehouseId().getWarehouseName() + " - " + lclUnitSsImports.getCfsWarehouseId().getWarehouseNo());
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getLastFreeDate())) {
                    lclAddUnitsForm.setLastFreeDate(DateUtils.formatDate(lclUnitSsImports.getLastFreeDate(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getGoDate())) {
                    lclAddUnitsForm.setGoDate(DateUtils.formatDate(lclUnitSsImports.getGoDate(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getItDatetime())) {
                    lclAddUnitsForm.setItDatetime(DateUtils.formatDate(lclUnitSsImports.getItDatetime(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclUnitSsImports.getApproxDueDate())) {
                    lclAddUnitsForm.setApproxDueDate(DateUtils.formatDate(lclUnitSsImports.getApproxDueDate(), "dd-MMM-yyyy"));
                }
                if (CommonUtils.isNotEmpty(lclUnitSsImports.getItNo())) {
                    lclAddUnitsForm.setItNo(lclUnitSsImports.getItNo());
                }
            }
            LclUnitSsManifest lclUnitSsManifest = lclUnitSsManifestDAO.getLclUnitSSManifestByHeader(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
            if (CommonFunctions.isNotNull(lclUnitSsManifest) && CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                lclAddUnitsForm.setMasterBL(lclUnitSsManifest.getMasterbl());
            }
            // lclAddUnitsForm.setUnmanifestLCLUnit(lclUtils.getUnmanifestLCLUnit(getCurrentUser(request)));
            if (lclunitss != null && CommonUtils.isNotEmpty(lclunitss.getStatus())) {
                lclAddUnitsForm.setUnitStatus(lclunitss.getStatus());
            }
            request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
            Date d = new Date();
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            LclUnitWhse lclUnitWhse = lclunitwhsedao.getLclUnitWhseDetails(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
            if (lclUnitWhse != null) {
                if (CommonUtils.isNotEmpty(lclUnitWhse.getSealNoIn())) {
                    lclAddUnitsForm.setSealNoIn(lclUnitWhse.getSealNoIn());
                }
                if (CommonUtils.isNotEmpty(lclUnitWhse.getSealNoOut())) {
                    lclAddUnitsForm.setSealNoOut(lclUnitWhse.getSealNoOut());
                }
                if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                    lclAddUnitsForm.setStrippedDate(DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclUnitWhse.getLocation())) {
                    lclAddUnitsForm.setDoorNumber(lclUnitWhse.getLocation());
                }
                if (CommonFunctions.isNotNull(lclUnitWhse.getStuffedByUser())) {
                    lclAddUnitsForm.setLoadedByUser(lclUnitWhse.getStuffedByUser().getLoginName());
                    lclAddUnitsForm.setLoadedByUserId(String.valueOf(lclUnitWhse.getStuffedByUser().getId()));
                    //lclAddUnitsForm.setStrippedDate(DateUtils.formatDate(lclUnitWhse.getStuffedDatetime(), "dd-MMM-yyyy"));
                }
                if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedByUser())) {
                    lclAddUnitsForm.setStrippedByUser(lclUnitWhse.getDestuffedByUser().getLoginName());
                    lclAddUnitsForm.setStrippedByUserId(String.valueOf(lclUnitWhse.getDestuffedByUser().getId()));
                    //lclAddUnitsForm.setStrippedDate(DateUtils.formatDate(lclUnitWhse.getStuffedDatetime(), "dd-MMM-yyyy"));
                }
            }
            request.setAttribute("lclUnitWhse", lclUnitWhse);
            LclSsDetail ssDetail = new LclSsDetailDAO().findByTransMode(lclAddUnitsForm.getHeaderId(), "V");
            lclAddUnitsForm.setEtaPodDate(DateUtils.formatStringDateToAppFormatMMM(ssDetail.getSta()));
            List<LclUnitSsDispo> lclDispositionList = new LclUnitSsDispoDAO().getDispositionList(lclunit.getId(), ssDetail.getId());
            request.setAttribute("lclDispositionList", lclDispositionList);
            request.setAttribute("unitNoFlag", lclunitssdao.getUnitNoCount(lclunit.getId()));
        }
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        request.setAttribute("unitsReopened", lclAddUnitsForm.getUnitsReopened());
        return mapping.findForward("editUnits");
    }

    public ActionForward addDisposition(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitSsDispoDAO lclunitssDispdao = new LclUnitSsDispoDAO();
        LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
        Date d = new Date();
        LclUtils lclUtils = new LclUtils();
        LclUnitSsDispo lclUnitSsDispo = new LclUnitSsDispo();
        lclUnitSsDispo.setEnteredBy(getCurrentUser(request));
        lclUnitSsDispo.setEnteredDatetime(d);
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getDispositionId())) {
            DispositionDAO dispositionDAO = new DispositionDAO();
            Disposition disp = dispositionDAO.getByProperty("id", lclAddUnitsForm.getDispositionId());
            lclUnitSsDispo.setDisposition(disp);
        }
            lclUnitSsDispo.setDispositionDatetime(DateUtils.formatDateAndParseTo(d, "dd-MMM-yyyy hh:mm:ss a"));
//        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getDispositionDateTime())) {
//        } else {
//            lclUnitSsDispo.setDispositionDatetime(null);
//        }
        LclSsDetail ssDetail = new LclSsDetailDAO().findByTransMode(lclAddUnitsForm.getHeaderId(), "V");
        lclUnitSsDispo.setLclSsDetail(ssDetail);
        lclUnitSsDispo.setModifiedBy(getCurrentUser(request));
        lclUnitSsDispo.setModifiedDatetime(d);
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
            lclUnitSsDispo.setLclUnit(lclunit);
            request.setAttribute("unitNoFlag", new LclUnitSsDAO().getUnitNoCount(lclunit.getId()));
            lclunitssDispdao.save(lclUnitSsDispo);
            LclUnitWhse lclUnitWhse = lclunitwhsedao.getLclUnitWhseDetails(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
            request.setAttribute("lclUnitWhse", lclUnitWhse);
            List<LclUnitSsDispo> lclDispositionList = lclunitssDispdao.getDispositionList(lclunit.getId(), ssDetail.getId());
            request.setAttribute("lclDispositionList", lclDispositionList);
            String HDLG_MIA = new PropertyDAO().getProperty("auto.cost.miami.warehouse.handling");
            if (lclUnitSsDispo.getDisposition().getEliteCode().equalsIgnoreCase("PORT")
                    && !lclunit.getUnitType().getDescription().equalsIgnoreCase("coload") && "on".equalsIgnoreCase(HDLG_MIA)) {
                GlMappingDAO glDao = new GlMappingDAO();
                GlMapping apGlmapping = glDao.findByChargeCode("HDLG-MIA", "LCLI", "AC");
                List<Object[]> drList = new SearchDAO().getAllUnitDr(lclAddUnitsForm.getUnitssId());
                for (int i = 0; i < drList.size(); i++) {
                    Object[] obj = drList.get(i);
                    boolean podfdFlag = false;
                    if (obj[7] != null && CommonUtils.isNotEmpty(obj[7].toString()) && obj[7].toString().equals("I")
                            && (obj[2] != null && CommonUtils.isNotEmpty(obj[2].toString())
                            && obj[3] != null && CommonUtils.isNotEmpty(obj[3].toString())
                            && obj[2].toString().equalsIgnoreCase(obj[3].toString())
                            && obj[3].toString().equalsIgnoreCase("USMIA"))) {
                        podfdFlag = true;
                    }
                    if (podfdFlag && obj[6] != null && CommonUtils.isNotEmpty(obj[6].toString()) && obj[6].toString().equals("false")) {
                        if (obj[5] != null && CommonUtils.isNotEmpty(obj[5].toString()) && !obj[5].toString().equals("0.000")) {
                            Double cbm = Double.parseDouble(obj[5].toString());
                            Double apAmount = cbm * 12;
                            LclBookingAc lclBookingAc = lclUtils.saveBookingAc(Long.parseLong(obj[1].toString()), apGlmapping, apGlmapping, new BigDecimal(apAmount), BigDecimal.ZERO, obj[4].toString(),
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
//                            new LclManifestDAO().createLclAccruals(lclBookingAc, "LCLI", "Open");
//                            lclUtils.insertLCLBookingAcTrans(lclBookingAc, "AC", "A", "", lclBookingAc.getApAmount(), getCurrentUser(request));
                        }
                    }
                }
            }
            // adding condition to change disposition of transhipment DR in exports side
            if (null != lclAddUnitsForm.getDisposition() && "AVAL".equalsIgnoreCase(lclAddUnitsForm.getDisposition())) {
                List unitDrList = new LclUnitSsDAO().getConcatenatedFileIdsForExport(lclAddUnitsForm.getUnitssId().toString(), "T");
                for (Object row : unitDrList) {
                    Object[] col = (Object[]) row;
                    new LclDwr().updateStatus(col[0].toString(), "WV", col[1].toString(), getCurrentUser(request).getUserId().toString(), col[2].toString(),"",null,null);
                    new LclBookingPieceDAO().bookedValueToActualValue(Long.parseLong(col[0].toString()));
                }
            }
        }
        //  lclAddUnitsForm.setUnmanifestLCLUnit(lclUtils.getUnmanifestLCLUnit(getCurrentUser(request)));
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward deleteDisposition(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitSsDispoDAO lclunitSsDispdao = new LclUnitSsDispoDAO();
        LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
        if (lclAddUnitsForm.getUnitDispoId() != null && lclAddUnitsForm.getUnitDispoId() > 0) {
            LclUnitSsDispo lclunitSsDisp = lclunitSsDispdao.findById(lclAddUnitsForm.getUnitDispoId());
            lclunitSsDispdao.delete(lclunitSsDisp);
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
                LclUnitDAO lclunitdao = new LclUnitDAO();
                LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
                LclUnitWhse lclUnitWhse = lclunitwhsedao.getLclUnitWhseDetails(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
                request.setAttribute("lclUnitWhse", lclUnitWhse);
                LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(lclAddUnitsForm.getHeaderId());
                LclSsDetail ssDetail = new LclSsDetailDAO().findByTransMode(lclAddUnitsForm.getHeaderId(), "V");
                List<LclUnitSsDispo> lclDispositionList = new LclUnitSsDispoDAO().getDispositionList(lclunit.getId(), ssDetail.getId());
                request.setAttribute("lclDispositionList", lclDispositionList);
                LclUnitSsRemarks lclUnitSsRemarks = new LclUnitSsRemarks();
                lclUnitSsRemarks.setRemarks("Disposition -> " + lclunitSsDisp.getDisposition().getEliteCode() + " Deleted");
                lclUnitSsRemarks.setType("auto");
                lclUnitSsRemarks.setLclUnit(lclunit);
                lclUnitSsRemarks.setLclSsHeader(lclSsHeader);
                lclUnitSsRemarks.setEnteredBy(getCurrentUser(request));
                lclUnitSsRemarks.setModifiedby(getCurrentUser(request));
                lclUnitSsRemarks.setModifiedDatetime(new Date());
                lclUnitSsRemarks.setEnteredDatetime(new Date());
                new LclUnitSsRemarksDAO().save(lclUnitSsRemarks);
                Integer dispCount = new LclUnitSsDispoDAO().getUnitDispoCountByDispDesc(String.valueOf(lclAddUnitsForm.getUnitId()), "AVAL", ssDetail.getId());
                if (dispCount == 0 && (lclSsHeader.getClosedBy() == null && lclSsHeader.getAuditedBy() == null)) {
                    new LclUnitSsDispoDAO().insertVoyageNotification(lclAddUnitsForm.getUnitssId().toString());
                }
                request.setAttribute("unitNoFlag", new LclUnitSsDAO().getUnitNoCount(lclunit.getId()));
            }
        }
        // lclAddUnitsForm.setUnmanifestLCLUnit(lclUtils.getUnmanifestLCLUnit(getCurrentUser(request)));
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward manifest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclUtils lclUtils = new LclUtils();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        lclManifestDAO.getAllManifestImportsBookingsByUnitSS(lclAddUnitsForm.getUnitssId(), null, null,
                getCurrentUser(request), true, realPath, true, null);
        lclUtils.updateLCLUnitSSStatus(lclAddUnitsForm.getUnitssId(), "M", getCurrentUser(request));
        addDisposition(mapping, form, request, response);
        lclAddUnitsForm.setUnitStatus("M");
        // lclAddUnitsForm.setUnmanifestLCLUnit(lclUtils.getUnmanifestLCLUnit(getCurrentUser(request)));
        log.info("All DR's are Manifested SuccessFully on " + new Date());
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward unmanifest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclUtils lclUtils = new LclUtils();
        lclManifestDAO.getAllManifestImportsBookingsByUnitSS(lclAddUnitsForm.getUnitssId(), null, lclAddUnitsForm.getButtonValue(),
                getCurrentUser(request), false, "", true, null);
        lclUtils.updateLCLUnitSSStatus(lclAddUnitsForm.getUnitssId(), "E", getCurrentUser(request));
        lclAddUnitsForm.setUnitStatus("E");
        //  lclAddUnitsForm.setUnmanifestLCLUnit(lclUtils.getUnmanifestLCLUnit(getCurrentUser(request)));
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
            LclUnitWhse lclUnitWhse = lclunitwhsedao.getLclUnitWhseDetails(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
            request.setAttribute("lclUnitWhse", lclUnitWhse);
            LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclAddUnitsForm.getHeaderId(), "V");
            List<LclUnitSsDispo> lclDispositionList = new LclUnitSsDispoDAO().getDispositionList(lclunit.getId(), lclSsDetail.getId());
            request.setAttribute("lclDispositionList", lclDispositionList);
        }
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward openAccountingReversePopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("displayAccountingReverse");
    }

    public ActionForward isCorrectionFound(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm unitsForm = (LclAddUnitsForm) form;
        String errorMessage = "";
        LclUtils utils = new LclUtils();
        if (utils.isCorrectionFound(unitsForm.getUnitssId().toString())) {
            errorMessage = "Please void and delete all corrections before Reverse Acct";
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward validateChargesBillToParty(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm unitsForm = (LclAddUnitsForm) form;
        String errorMessage = "";
        LclUtils utils = new LclUtils();
        errorMessage = utils.getBillingTypeErrorMessage(unitsForm.getUnitssId(), "Imports", unitsForm.getPostFlag());
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward getDrCount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclAddUnitsForm unitsForm = (LclAddUnitsForm) form;
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        Integer count = fileDao.getFileCount(unitsForm.getUnitssId(), "M", false);
        String errorMessage = String.valueOf(count);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward getNoOFContactFromDrs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclAddUnitsForm unitsForm = (LclAddUnitsForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String fileNo = lclUnitSsDAO.getNoOfContactFromDR(unitsForm.getUnitssId());
        PrintWriter out = response.getWriter();
        out.print(fileNo);
        return null;
    }

    public ActionForward checkTerminalForPodFd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclAddUnitsForm unitsForm = (LclAddUnitsForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String fileNo = lclUnitSsDAO.checkTerminalForPodFd(unitsForm.getUnitssId());
        PrintWriter out = response.getWriter();
        out.print(fileNo);
        return null;
    }

    public ActionForward saveWarehouse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitWhseDAO lclUnitWhseDAO = new LclUnitWhseDAO();
        LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        Date d = new Date();
        LclUnitWhse lclUnitWhse = null;
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getWarehouseId())) {
            lclUnitWhse = lclUnitWhseDAO.findById(lclAddUnitsForm.getUnitWarehouseId());
            lclUnitWhse.setWarehouse(warehouseDAO.findById(lclAddUnitsForm.getWarehouseId()));
            lclUnitWhse.setModifiedBy(getCurrentUser(request));
            lclUnitWhse.setModifiedDatetime(d);
            lclUnitWhseDAO.saveOrUpdate(lclUnitWhse);
        }
        lclUnitWhse = lclUnitWhseDAO.getLclUnitWhseDetails(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
        request.setAttribute("lclUnitWhse", lclUnitWhse);
        LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclAddUnitsForm.getHeaderId(), "V");
        List<LclUnitSsDispo> lclDispositionList = lclUnitSsDispoDAO.getDispositionList(lclUnitWhse.getLclUnit().getId(), lclSsDetail.getId());
        request.setAttribute("lclDispositionList", lclDispositionList);
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        request.setAttribute("unitNoFlag", new LclUnitSsDAO().getUnitNoCount(lclAddUnitsForm.getUnitId()));
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward checkChargeAndCostMappingWithGL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclAddUnitsForm unitsForm = (LclAddUnitsForm) form;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String unMappedChargeCode = null;
        List fileNo = lclUnitSsDAO.getFileNumberForUnit(unitsForm.getUnitssId());
        for (Object fileNumber : fileNo) {
            String chargeCode = lclUnitSsDAO.getChargeCodeNotExistInGlForFileNumber((String) fileNumber, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            List account = lclUnitSsDAO.getAccountForChargeCodeFromGlAndTerminal((String) fileNumber);
            if (CommonUtils.isNotEmpty(account)) {
                for (Object obj1 : account) {
                    Object[] obj = (Object[]) obj1;
                    if (!lclUnitSsDAO.checkAccountExistInAccountDetailsForAccount((String) obj[0])) {
                        if (chargeCode == null) {
                            chargeCode = (String) obj[1];
                        } else if (!chargeCode.contains((String) obj[1])) {
                            chargeCode = chargeCode + "," + (String) obj[1];
                        }
                    }
                }
            }
            if (CommonUtils.isNotEmpty(chargeCode)) {
                unMappedChargeCode = (unMappedChargeCode != null ? unMappedChargeCode : "") + " " + fileNumber + " -> " + chargeCode;
            }
        }

        PrintWriter out = response.getWriter();
        out.print(unMappedChargeCode);
        return null;
    }

    public String checkChargeAndCostMappingWithGLForFileNumber(String fileNumber) throws Exception {
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String chargeCode = lclUnitSsDAO.getChargeCodeNotExistInGlForFileNumber(fileNumber, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        List account = lclUnitSsDAO.getAccountForChargeCodeFromGlAndTerminal(fileNumber);
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                if (!lclUnitSsDAO.checkAccountExistInAccountDetailsForAccount((String) obj[0])) {
                    if (chargeCode == null) {
                        chargeCode = (String) obj[1];
                    } else if (!chargeCode.contains((String) obj[1])) {
                        chargeCode = chargeCode + "," + (String) obj[1];
                    }
                }
            }
        }
        return chargeCode;
    }
}
