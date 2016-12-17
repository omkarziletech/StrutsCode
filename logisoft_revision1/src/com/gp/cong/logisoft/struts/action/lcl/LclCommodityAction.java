/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LCLImportChargeCalc;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.CommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclCommodityForm;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlPieceDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Owner
 */
public class LclCommodityAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static String COMMODITY_DESC = "commodityDesc";
    private static String SHOW_CARGO = "showCargo";
    private LclUtils lclUtils = new LclUtils();
    private NumberFormat df = new DecimalFormat("#.000");

    /**
     * This method is used to set the commodity List in the session ,we will
     * save the commodity while saving the booking
     *
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward addCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        User weightVerifiedByUser = null;

        boolean editDimFlag = false;
        String dimFlag = request.getParameter("editDimFlag");
        if (request.getParameter("editDimFlag") != null && dimFlag.equals("true")) {
            editDimFlag = true;
        }
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<LclBookingPiece> commodityList = null != lclSession.getCommodityList() ? lclSession.getCommodityList() : new ArrayList<LclBookingPiece>();
        boolean includeDestfees = lclCommodityForm.isIncludeDestfees();
        LclBookingPiece lclBookingPiece = lclCommodityForm.getLclBookingPiece();
        if (lclBookingPiece == null) {
            lclBookingPiece = new LclBookingPiece();
        }
        if (lclBookingPiece.getEnteredBy() == null || lclBookingPiece.getEnteredDatetime() == null) {
            lclBookingPiece.setEnteredBy(getCurrentUser(request));
            lclBookingPiece.setEnteredDatetime(new Date());
        }

        if (CommonUtils.isNotEmpty(lclCommodityForm.getShortShipFileNo())) {
            lclBookingPiece.setActualPieceCount(lclBookingPiece.getBookedPieceCount());
            lclBookingPiece.setActualPackageType(lclBookingPiece.getPackageType());
            lclBookingPiece.setActualWeightImperial(lclBookingPiece.getBookedWeightImperial());
            lclBookingPiece.setActualWeightMetric(lclBookingPiece.getBookedWeightMetric());
            lclBookingPiece.setActualVolumeImperial(lclBookingPiece.getBookedVolumeImperial());
            lclBookingPiece.setActualVolumeMetric(lclBookingPiece.getBookedVolumeMetric());
        }

        if (CommonUtils.isNotEmpty(lclCommodityForm.getWeightverifiedUserId()) && "Exports".equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            weightVerifiedByUser = new UserDAO().findById(Integer.parseInt(lclCommodityForm.getWeightverifiedUserId()));
            lclBookingPiece.setWeightVerifiedBy(weightVerifiedByUser);
        }
        if (editDimFlag && CommonUtils.isNotEmpty(request.getParameter("id"))) {
            try {
                lclBookingPiece.setLclBookingPieceDetailList(commodityList.get(Integer.parseInt(request.getParameter("id"))).getLclBookingPieceDetailList());
                commodityList.set(Integer.parseInt(request.getParameter("id")), lclBookingPiece);
            } catch (Exception e) {
                List<LclBookingPieceDetail> detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new ArrayList<LclBookingPieceDetail>();
                lclBookingPiece.setLclBookingPieceDetailList(detailList);
                commodityList.add(lclBookingPiece);
            }
        } else {
            List<LclBookingPieceDetail> detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new ArrayList<LclBookingPieceDetail>();
            lclBookingPiece.setLclBookingPieceDetailList(detailList);
            commodityList.add(lclBookingPiece);
        }
        lclBookingPiece.setModifiedBy(getCurrentUser(request));
        lclBookingPiece.setModifiedDatetime(new Date());
        lclSession.setCommodityList(commodityList);
        lclSession.setIncludeDestfees(includeDestfees);
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("lclCommodityList", commodityList);
        return mapping.findForward(COMMODITY_DESC);
    }

    public ActionForward modifyMinimumRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
        LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
        Date now = new Date();
        boolean editDimFlag = false;
        User user = getCurrentUser(request);
        String dimFlag = request.getParameter("editDimFlag");
        String postDRflag = request.getParameter("postDRflag");
        String deliveryMetro = request.getParameter("deliveryMetro");
        String insurance = lclCommodityForm.getInsurance();
        String goods = lclCommodityForm.getValueOfGoods();
        BigDecimal valueOfGoods = new BigDecimal(0.00);
        if (CommonUtils.isNotEmpty(lclCommodityForm.getValueOfGoods())) {
            valueOfGoods = new BigDecimal(lclCommodityForm.getValueOfGoods());
        }
        String pcBoth = request.getParameter("pcBoth");
        if (request.getParameter("editDimFlag") != null && dimFlag.equals("true")) {
            editDimFlag = true;
        }
        if (postDRflag == null) {
            postDRflag = "";
        }
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String moduleName = lclCommodityForm.getModuleName();
        List<LclBookingPiece> commodityList = null;
        LclBookingPiece lclBookingPiece = lclCommodityForm.getLclBookingPiece();
        String rateType = lclCommodityForm.getRateType();
        if (lclCommodityForm.getRateType() != null && !lclCommodityForm.getRateType().trim().equals("")) {
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            } else {
                rateType = lclCommodityForm.getRateType();
            }
        }
        lclBookingPiece.setModifiedBy(getCurrentUser(request));
        lclBookingPiece.setModifiedDatetime(now);
        String fileId = request.getParameter("fileNumberId");
        Long fileNumberId = new Long(fileId);
        boolean calcHeavy = false;
        if (CommonUtils.isNotEmpty(lclCommodityForm.getCalcHeavy()) && lclCommodityForm.getCalcHeavy().equalsIgnoreCase("Y")) {
            calcHeavy = true;

        }
        String pooCode = "", polCode = "", podCode = "", fdCode = "";
        List<LclBookingAc> lclBookingAcList = null;
        if (fileNumberId == 0) {
            commodityList = lclSession.getCommodityList();
            pooCode = lclCommodityForm.getOriginId();
            polCode = lclCommodityForm.getPolId();
            podCode = lclCommodityForm.getPodId();
            fdCode = lclCommodityForm.getDestinationId();
            if (CommonUtils.isEqual(moduleName, "Imports")) {
                lclBookingAcList = lclImportChargeCalc.ImportRateCalculation(pooCode, polCode, podCode, fdCode,
                        lclCommodityForm.getTranshipment(), lclCommodityForm.getBillingType(), lclCommodityForm.getBillToParty(),
                        lclCommodityForm.getAgentNo(), lclCommodityForm.getImpCfsWarehsId(), fileNumberId, commodityList,
                        request, user, lclCommodityForm.getUnitSsId());

                lclUtils.setImportRolledUpChargesForBooking(lclImportChargeCalc.getBookingAcList(), request, fileNumberId, null,
                        commodityList, lclCommodityForm.getBillingType(), "", lclCommodityForm.getAgentNo());
                lclUtils.setWeighMeasureForImportBooking(request, commodityList, null);

                // For Imports transhipment file then rates not found in imports side below logic will apply.
                if (CommonUtils.isEmpty(lclBookingAcList) && "Y".equalsIgnoreCase(lclCommodityForm.getTranshipment())
                        && CommonUtils.isNotEmpty(lclCommodityForm.getDestinationName())
                        && !"UNKNOWN".equalsIgnoreCase(lclCommodityForm.getDestinationName())) {
                    lclChargesCalculation.calculateRates(podCode, fdCode, podCode, fdCode, fileNumberId, commodityList,
                            user, null, insurance, null, "F", "C", null, null, null, null, calcHeavy, deliveryMetro,
                            pcBoth, null, null, request, lclCommodityForm.getBillToParty());

                    lclUtils.setRolledUpChargesForBooking(lclChargesCalculation.getBookingAcList(), request, fileNumberId, null,
                            commodityList, pcBoth, lclChargesCalculation.getPorts().getEngmet(), "No");
                    lclBookingAcList = (List<LclBookingAc>) request.getAttribute("chargeList");
                }
            } else {
                if (CommonUtils.isNotEmpty(lclCommodityForm.getDestinationName())
                        && !"UNKNOWN".equalsIgnoreCase(lclCommodityForm.getDestinationName())) {
                    lclChargesCalculation.calculateRates(pooCode, fdCode, polCode, podCode, fileNumberId, commodityList,
                            user, null, insurance, null, rateType, "C", null, null, null, null, calcHeavy, deliveryMetro,
                            pcBoth, null, null, request, lclCommodityForm.getBillToParty());
                    lclUtils.setRolledUpChargesForBooking(lclChargesCalculation.getBookingAcList(), request, fileNumberId, null,
                            commodityList, pcBoth, lclChargesCalculation.getPorts().getEngmet(), "No");
                    lclBookingAcList = (List<LclBookingAc>) request.getAttribute("chargeList");
                }
                lclUtils.setWeighMeasureForBooking(request, commodityList, lclChargesCalculation.getPorts());
            }
            request.setAttribute("totalCharges", lclUtils.calculateTotalByBookingAcList(lclBookingAcList));
            lclSession.setBookingAcList(lclBookingAcList);
            lclSession.setCommodityList(commodityList);
            session.setAttribute("lclSession", lclSession);
        } else {
            LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
            pooCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
            polCode = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
            podCode = null != lclBooking.getPortOfDestination() ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
            fdCode = null != lclBooking.getFinalDestination() ? lclBooking.getFinalDestination().getUnLocationCode() : "";
            lclBooking.setModifiedDatetime(now);
            lclBookingDAO.saveOrUpdate(lclBooking);
            request.setAttribute("lclBooking", lclBooking);
            commodityList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());

            if (CommonUtils.isEqual(moduleName, "Imports")) {
                if ("true".equalsIgnoreCase(lclCommodityForm.getRatesValidationFlag())
                        && CommonUtils.isNotEqualIgnoreCase(lclCommodityForm.getStatus(), "M")) {
                    lclBookingAcList = lclImportChargeCalc.ImportRateCalculation(pooCode, polCode, podCode, fdCode,
                            lclCommodityForm.getTranshipment(), lclCommodityForm.getBillingType(), lclBooking.getBillToParty(),
                            lclCommodityForm.getAgentNo(), lclCommodityForm.getImpCfsWarehsId(), fileNumberId, commodityList,
                            request, user, lclCommodityForm.getUnitSsId());

                    // For Imports transhipment file then rates not found in imports side below logic will apply. 
                    if (CommonUtils.isEmpty(lclBookingAcList) && "Y".equalsIgnoreCase(lclCommodityForm.getTranshipment())
                            && CommonUtils.isNotEmpty(lclCommodityForm.getDestinationName())
                            && !"UNKNOWN".equalsIgnoreCase(lclCommodityForm.getDestinationName())) {
                        lclChargesCalculation.calculateRates(podCode, fdCode, podCode, fdCode, fileNumberId, commodityList,
                                user, null, insurance, null, "F", "C", null, null, null, null, calcHeavy, deliveryMetro,
                                pcBoth, null, null, request, lclBooking.getBillToParty());
                    }
                }
                lclBookingAcList = lclCostChargeDAO.getLclCostByFileNumberAsc(lclCommodityForm.getFileNumberId(), moduleName);
                lclUtils.setWeighMeasureForImportBooking(request, commodityList, null);
                lclUtils.setImportRolledUpChargesForBooking(lclBookingAcList, request, lclCommodityForm.getFileNumberId(),
                        lclCostChargeDAO, commodityList, lclCommodityForm.getBillingType(), "", lclCommodityForm.getAgentNo());
            } else {
                String engmet = "";
                if (CommonUtils.isNotEmpty(lclCommodityForm.getDestinationName())
                        && !"UNKNOWN".equalsIgnoreCase(lclCommodityForm.getDestinationName())) {
                    Ports ports = new PortsDAO().getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                    if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                        engmet = ports.getEngmet();
                    }
                    if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                        lclBookingAcList = lclImportChargeCalc.ImportRateCalculation(pooCode, polCode, podCode, fdCode,
                                "Y", lclCommodityForm.getBillingType(), lclBooking.getBillToParty(),
                                lclCommodityForm.getAgentNo(), lclCommodityForm.getImpCfsWarehsId(), fileNumberId, commodityList,
                                request, user, lclCommodityForm.getUnitSsId());

                        if (CommonUtils.isEmpty(lclBookingAcList)) {
                            for (LclBookingPiece piece : commodityList) { // to avoid Lazy intialize error
                                piece.setLclBookingPieceDetailList(null);
                            }
                            lclChargesCalculation.calculateRates(podCode, fdCode, podCode, fdCode, fileNumberId, commodityList,
                                    user, null, insurance, null, "F", "C", null, null, null, null, calcHeavy, deliveryMetro,
                                    pcBoth, null, null, request, lclBooking.getBillToParty());
                        }
                    } else {
                        lclChargesCalculation.calculateRates(pooCode, fdCode, polCode, podCode, lclCommodityForm.getFileNumberId(),
                                commodityList, user, null, insurance, valueOfGoods, rateType, "C", null, null, null, null,
                                calcHeavy, deliveryMetro, pcBoth, null, null, request, lclBooking.getBillToParty());
                    }
                    if (lclBooking.getSpotRate() && commodityList.size() == 1) {
                        String billingType = lclBooking.getBillingType();
                        String CFT = null != lclBooking.getSpotWmRate() ? lclBooking.getSpotWmRate().toString() : "";
                        String CBM = null != lclBooking.getSpotRateMeasure() ? lclBooking.getSpotRateMeasure().toString() : "";
                        Boolean spotCheckBottom = lclBooking.getSpotRateBottom();
                        Boolean isOnlyOcnfrt = lclBooking.getSpotOfRate();
                        String spotComment = lclBooking.getSpotComment();
                        MessageResources messageResources = getResources(request);
                        String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                        new ExportBookingUtils().calculateSpotRate(lclCommodityForm.getFileNumberId(), lclBooking,
                                billingType, CBM, CFT, rateType, isOnlyOcnfrt, spotCheckBottom, spotComment,
                                spotRateCommodity, request, commodityList, engmet);
                    }
                    lclBookingAcList = new LclCostChargeDAO().getLclCostByFileNumberAsc(lclCommodityForm.getFileNumberId(), moduleName);
                    lclUtils.setWeighMeasureForBooking(request, commodityList, lclChargesCalculation.getPorts());
                    lclUtils.setRolledUpChargesForBooking(lclBookingAcList, request, lclCommodityForm.getFileNumberId(),
                            lclCostChargeDAO, commodityList, pcBoth, engmet, "No");
                }
                lclUtils.setWeighMeasureForBooking(request, commodityList, lclChargesCalculation.getPorts());
//                List consolidateList = new LclConsolidateDAO().getConsolidatesFiles(lclCommodityForm.getFileNumberId());
//                if (null != consolidateList && !consolidateList.isEmpty() && lclBooking.getLclFileNumber().getState().equalsIgnoreCase("BL")) {
//                    Long consolidateBlId = new LCLBlDAO().findConsolidateBl(fileNumberId);
//                    new ExportBookingUtils().updateConsolidationBLCharges(null != consolidateBlId ? consolidateBlId : fileNumberId, "", user, request);
//                }
            }
        }
        request.setAttribute("lclCommodityList", commodityList);
        return mapping.findForward("chargeDesc");
    }

    public ActionForward addLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        Date d = new Date();
        CommodityDetailsDAO commodityDetailsDAO = new CommodityDetailsDAO();
        LclBookingPieceDAO bookingPieceDAO = new LclBookingPieceDAO();
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclBooking booking = bookingDAO.getByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
        bookingDAO.getCurrentSession().evict(booking);
        User user = getCurrentUser(request);
        boolean isHazmat = false;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setCommodityList(null);
        session.setAttribute("lclSession", lclSession);
        if (request.getParameter("hazmat") != null && request.getParameter("hazmat").equals("Y")) {
            lclCommodityForm.getLclBookingPiece().setHazmat(true);
        } else {
            lclCommodityForm.getLclBookingPiece().setHazmat(false);
        }
        if (CommonUtils.isNotEmpty(lclCommodityForm.getActualPackageTypeId()) && CommonUtils.isEmpty(lclCommodityForm.getPackageTypeId())) {
            PackageType packageType = new PackageTypeDAO().findById(lclCommodityForm.getActualPackageTypeId());
            lclCommodityForm.getLclBookingPiece().setPackageType(new PackageType(lclCommodityForm.getActualPackageTypeId()));
            lclCommodityForm.getLclBookingPiece().setActualPackageType(new PackageType(lclCommodityForm.getActualPackageTypeId()));
            lclCommodityForm.setPackageTypeId(packageType.getId());
        }
        LclBookingPiece bookingPiece = lclCommodityForm.getLclBookingPiece();
        if (bookingPiece == null) {
            bookingPiece = new LclBookingPiece();
        }
        if (bookingPiece.getEnteredBy() == null || bookingPiece.getEnteredDatetime() == null) {
            bookingPiece.setEnteredBy(getCurrentUser(request));
            bookingPiece.setEnteredDatetime(d);
        }
        bookingPiece.setModifiedBy(getCurrentUser(request));
        bookingPiece.setModifiedDatetime(d);
        /* update shortShip values in Original DR*/
        if ("Exports".equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            if (booking.getLclFileNumber().isShortShip()) {
                Object[] bkgPieceDiffValues = bookingPieceDAO.getBkgPieceOldValues(bookingPiece.getId());
                Integer actualPieceCount = 0;
                BigDecimal actualWeiImp = BigDecimal.ZERO, actualWeightMet = BigDecimal.ZERO, actualVolImp = BigDecimal.ZERO, actualVolMet = BigDecimal.ZERO;
                String fileStatus = new LclFileNumberDAO().getFileStatus(booking.getLclFileNumber().getId());
                if (null != bkgPieceDiffValues && !"B".equalsIgnoreCase(fileStatus) && !"WU".equalsIgnoreCase(fileStatus)) {
                    actualPieceCount = null != bkgPieceDiffValues[0] ? Integer.parseInt(bkgPieceDiffValues[0].toString()) : 0;
                    actualPieceCount = bookingPiece.getActualPieceCount() > actualPieceCount
                            ? bookingPiece.getActualPieceCount() - actualPieceCount : actualPieceCount - bookingPiece.getActualPieceCount();
                    actualWeiImp = null != bkgPieceDiffValues[1] ? (BigDecimal) bkgPieceDiffValues[1] : BigDecimal.ZERO;
                    actualWeiImp = bookingPiece.getActualWeightImperial().doubleValue() > actualWeiImp.doubleValue()
                            ? bookingPiece.getActualWeightImperial().subtract(actualWeiImp) : actualWeiImp.subtract(bookingPiece.getActualWeightImperial());
                    actualWeightMet = null != bkgPieceDiffValues[2] ? (BigDecimal) bkgPieceDiffValues[2] : BigDecimal.ZERO;
                    actualWeightMet = bookingPiece.getActualWeightMetric().doubleValue() > actualWeightMet.doubleValue()
                            ? bookingPiece.getActualWeightMetric().subtract(actualWeightMet) : actualWeightMet.subtract(bookingPiece.getActualWeightMetric());
                    actualVolImp = null != bkgPieceDiffValues[3] ? (BigDecimal) bkgPieceDiffValues[3] : BigDecimal.ZERO;
                    actualVolImp = bookingPiece.getActualVolumeImperial().doubleValue() > actualVolImp.doubleValue()
                            ? bookingPiece.getActualVolumeImperial().subtract(actualVolImp) : actualVolImp.subtract(bookingPiece.getActualVolumeImperial());
                    actualVolMet = null != bkgPieceDiffValues[4] ? (BigDecimal) bkgPieceDiffValues[4] : BigDecimal.ZERO;
                    actualVolMet = bookingPiece.getActualVolumeMetric().doubleValue() > actualVolMet.doubleValue()
                            ? bookingPiece.getActualVolumeMetric().subtract(actualVolMet) : actualVolMet.subtract(bookingPiece.getActualVolumeMetric());
                }
                bookingPieceDAO.updateBkgPiece(booking.getLclFileNumber().getFileNumber(), actualPieceCount,
                        actualWeiImp, actualWeightMet, actualVolImp,
                        actualVolMet, user, request);
            }

            if (CommonUtils.isNotEmpty(lclCommodityForm.getLabelField())) {
                new LclRemarksDAO().insertLclRemarks(lclCommodityForm.getFileNumberId(), REMARKS_DR_AUTO_NOTES, lclCommodityForm.getLabelField() + " Labels Printed", user.getUserId());
                lclUtils.setMailTransactionsDetails("LclBooking", "Label Print", user, lclCommodityForm.getLabelField(), "Pending", new Date(), lclCommodityForm.getFileNumber(), lclCommodityForm.getFileNumberId());
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getWeightverifiedUserId())) {
                bookingPiece.setWeightVerifiedBy(new UserDAO().findById(Integer.parseInt(lclCommodityForm.getWeightverifiedUserId())));
            } else if (CommonUtils.isEmpty(lclCommodityForm.getWeightVerifiedBy())) {
                bookingPiece.setWeightVerifiedBy(null);
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getActualPieceCount()) && (CommonUtils.isEmpty(lclCommodityForm.getBookedPieceCount()) || "0".equals(lclCommodityForm.getBookedPieceCount()))) {
                bookingPiece.setBookedPieceCount(Integer.parseInt(lclCommodityForm.getActualPieceCount()));
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getActualPackageTypeId()) && CommonUtils.isEmpty(lclCommodityForm.getPackageTypeId())) {
                PackageType packageType = new PackageTypeDAO().findById(lclCommodityForm.getActualPackageTypeId());
                bookingPiece.setPackageType(new PackageType(lclCommodityForm.getActualPackageTypeId()));
                lclCommodityForm.setPackageTypeId(packageType.getId());
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getActualVolumeImperial()) && (CommonUtils.isEmpty(lclCommodityForm.getBookedVolumeImperial()) || new BigDecimal(lclCommodityForm.getBookedVolumeImperial()).compareTo(BigDecimal.ZERO) == 0)) {
                bookingPiece.setBookedVolumeImperial(new BigDecimal(lclCommodityForm.getActualVolumeImperial()));
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getActualVolumeMetric()) && (CommonUtils.isEmpty(lclCommodityForm.getBookedVolumeMetric()) || new BigDecimal(lclCommodityForm.getBookedVolumeMetric()).compareTo(BigDecimal.ZERO) == 0)) {
                bookingPiece.setBookedVolumeMetric(new BigDecimal(lclCommodityForm.getActualVolumeMetric()));
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getActualWeightImperial()) && (CommonUtils.isEmpty(lclCommodityForm.getBookedWeightImperial()) || new BigDecimal(lclCommodityForm.getBookedWeightImperial()).compareTo(BigDecimal.ZERO) == 0)) {
                bookingPiece.setBookedWeightImperial(new BigDecimal(lclCommodityForm.getActualWeightImperial()));
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getActualWeightMetric()) && (CommonUtils.isEmpty(lclCommodityForm.getBookedWeightMetric()) || new BigDecimal(lclCommodityForm.getBookedWeightMetric()).compareTo(BigDecimal.ZERO) == 0)) {
                bookingPiece.setBookedWeightMetric(new BigDecimal(lclCommodityForm.getActualWeightMetric()));
            }

            LclBookingExport lclBookingExport = null;
            Warehouse wareHouse = null;
            lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
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
            lclBookingExport.setFileNumberId(lclCommodityForm.getFileNumberId());
            lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
            lclBookingExport.setRtAgentAcct(null);
            lclBookingExport.setDeliverPickup("P");
            lclBookingExport.setDeliverPickupDatetime(d);
            boolean valComm = bookingDAO.isvalOfcom(lclCommodityForm.getCommodityNo());
            boolean oldValComm = bookingDAO.isvalOfcom(lclCommodityForm.getOldTariffNo());
            if (valComm || oldValComm) {
                lclBookingExport.setIncludeDestfees(lclCommodityForm.isIncludeDestfees());
            }
            lclBookingExport.setReleaseUser(user);
            lclBookingExport.setModifiedBy(user);
            lclBookingExport.setModifiedDatetime(d);
            lclBookingExport.setAes(false);
            lclBookingExport.setUps(lclCommodityForm.getUps());
            new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
            new LCLBookingDAO().updateModifiedDateTime(lclCommodityForm.getFileNumberId(), user.getUserId());
        }

        if (CommonUtils.isNotEmpty(lclCommodityForm.getActualPackageTypeId())) {
            bookingPiece.setActualPackageType(new PackageType(lclCommodityForm.getActualPackageTypeId()));
        }
        Boolean isUpdateLclBlPiece = Boolean.FALSE;
        if (CommonUtils.isEmpty(bookingPiece.getId()) && !"X".equalsIgnoreCase(booking.getLclFileNumber().getStatus())) {
            LclBl lclBl = new LCLBlDAO().getByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
            if (null != lclBl) {
                boolean blPiece = new LclBLPieceDAO().checkCommodityExist(lclCommodityForm.getFileNumberId().toString(), bookingPiece.getCommodityType().getId().toString(), bookingPiece.isIsBarrel());
                if (!blPiece) {
                    isUpdateLclBlPiece = true;
                }
            }
        }
        bookingPiece = (LclBookingPiece) bookingPieceDAO.saveAndReturn(bookingPiece);

        Long updatedId = bookingPiece.getId();
        List deleteList = new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", bookingPiece.getId());
        if (CommonUtils.isEmpty(deleteList)) {
            List dimList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
            for (Object obj : dimList) {
                LclBookingPieceDetail lbpd = (LclBookingPieceDetail) obj;
                lbpd.setLclBookingPiece(bookingPiece);
                commodityDetailsDAO.save(lbpd);
            }
        }
        if (bookingPiece.getLclFileNumber() != null) { // insert lclBookingPieceWhse
            List<LclBookingPiece> pieceList = bookingPieceDAO.findByProperty("lclFileNumber.id", bookingPiece.getLclFileNumber().getId());
            if (CommonUtils.isNotEmpty(pieceList) && pieceList.size() == 1) {
                LclBooking lclBookings = bookingPiece.getLclFileNumber().getLclBooking();
                String unlocationCode = "T".equalsIgnoreCase(lclBookings.getBookingType()) ? lclBookings.getPortOfDestination().getUnLocationCode()
                        : lclBookings.getPortOfOrigin().getUnLocationCode();
                Integer warehouseId = new WarehouseDAO().getWarehouseId(unlocationCode, "B");
                if (CommonUtils.isNotEmpty(warehouseId)) {
                    LclBookingPieceWhseDAO lclBookingPieceWhseDAO = new LclBookingPieceWhseDAO();
                    boolean isWarehouseExists = lclBookingPieceWhseDAO.isContainWarehouseId(bookingPiece.getId(), warehouseId);
                    if (!isWarehouseExists) {
                        lclBookingPieceWhseDAO.insertLclBookingPieceWhse(bookingPiece.getId(), warehouseId, user.getUserId());
                    }
                }
            }
        }

        if (isUpdateLclBlPiece) {
            copyBookingPieceToBl(bookingPiece);
        }
        LclBookingPieceWhse lclBookingPieceWhse = null;
        LclBookingPieceWhse oldLclBookingPieceWhse = null;
        Warehouse previousWareHouse = null;
        LclBookingPiece oldLclBookingPiece = null;
        boolean isBarrenCombo = bookingPieceDAO.isBarrelComboShipment(lclCommodityForm.getFileNumberId());
        if ((isBarrenCombo || lclCommodityForm.getLclBookingPiece().isIsBarrel()) && bookingPiece.getId() != null && "Exports".equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            List<LclBookingPiece> oldBookingPiece = bookingPieceDAO.findByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
            for (LclBookingPiece oldPiece : oldBookingPiece) {
                lclBookingPieceWhse = new LclBookingPieceWhseDAO().findByFileAndCommodityBarrel(oldPiece.getId());
                if (lclBookingPieceWhse != null) {
                    previousWareHouse = lclBookingPieceWhse.getWarehouse();
                }
                if (lclBookingPieceWhse == null) {
                    oldLclBookingPieceWhse = new LclBookingPieceWhse();
                    oldLclBookingPiece = oldPiece;
                }
            }
            if (null != oldLclBookingPiece && (oldLclBookingPiece.getLclBookingPieceWhseList() == null || oldLclBookingPiece.getLclBookingPieceWhseList().isEmpty())) {
                oldLclBookingPieceWhse.setEnteredDatetime(new Date());
                oldLclBookingPieceWhse.setEnteredBy(getCurrentUser(request));
                oldLclBookingPieceWhse.setLclBookingPiece(oldLclBookingPiece);
                oldLclBookingPieceWhse.setLocation(null);
                oldLclBookingPieceWhse.setWarehouse(previousWareHouse);
                oldLclBookingPieceWhse.setStowedDatetime(null);
                oldLclBookingPieceWhse.setStowedByUser(null);
                oldLclBookingPieceWhse.setModifiedDatetime(new Date());
                oldLclBookingPieceWhse.setModifiedBy(getCurrentUser(request));
                new LclBookingPieceWhseDAO().saveOrUpdate(oldLclBookingPieceWhse);
            }
        }
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
        for (LclBookingPiece lbp : lclCommodityList) {
            lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(lclCommodityForm.getFileNumberId(), lbp.getId()));
            lbp.setLclBookingAcList(lclCostChargeDAO.findByFileAndCommodityList(lclCommodityForm.getFileNumberId(), lbp.getId()));
            lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
            lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
            if (lbp.isHazmat() && !isHazmat) { // to check hazmat availability
                isHazmat = true;
            }
        }
        if ("WV".equalsIgnoreCase(lclCommodityForm.getStatus()) && lclCommodityForm.isCargoReceived()) {
            lclCommodityForm.setVerifiedIds(String.valueOf(updatedId));
            lclCommodityList = new LclBookingPieceDAO().getLclBkgPieceList(lclCommodityForm.getVerifiedIds(), lclCommodityForm.getFileNumberId());
            session.setAttribute("cargoReceivedList", lclCommodityList);
        }

        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            LclSsHeader bookedHeader = booking.getBookedSsHeaderId();
            if (null != bookedHeader && null != bookedHeader.getVesselSsDetail()) {
                Date originLrdDate = bookedHeader.getVesselSsDetail().getStd();
                new LCLBookingDAO().updatePooWhseLrdt(originLrdDate, lclCommodityForm.getFileNumberId(), isHazmat);
            }
        }
        request.setAttribute("ofspotrate", booking.getSpotRate());
        request.setAttribute("status", booking.getLclFileNumber().getStatus());
        request.setAttribute("lclCommodityList", lclCommodityList);
        if ("WV".equalsIgnoreCase(lclCommodityForm.getStatus()) && lclCommodityForm.isCargoReceived()) {
            return mapping.findForward(SUCCESS);
        } else {
            session.removeAttribute("cargoReceivedList");
            return mapping.findForward(COMMODITY_DESC);
        }
    }

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        String tabFlag = request.getParameter("tabFlag");
        String fileNumberId = request.getParameter("fileNumberId");
        String verifyCargo = request.getParameter("verifyCargo");
        String cfcl = request.getParameter("cfcl");
        HttpSession session = request.getSession();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setBookingDetailList(null);
        session.setAttribute("lclSession", lclSession);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            lclCommodityForm.setFileStatus(new LclFileNumberDAO().getFileStatus(Long.parseLong(fileNumberId)));
            if (!tabFlag.equals("false")) {
                if ("WV".equalsIgnoreCase(lclCommodityForm.getStatus())) {
                    List<LclBookingPiece> lclCommodityList = null != session.getAttribute("cargoReceivedList") ? (List<LclBookingPiece>) session.getAttribute("cargoReceivedList") : null;
                    if (null != lclCommodityList && !lclCommodityList.isEmpty()) {
                        request.setAttribute("lclCommodityList", lclCommodityList);
                    } else {
                        request.setAttribute("lclCommodityList", new LclBookingPieceDAO().getLclBkgPieceList(lclCommodityForm.getVerifiedIds(), Long.parseLong(fileNumberId)));
                    }
                } else {
                    request.setAttribute("lclCommodityList", new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
                }
            }
        } //**********code written for ShortShip Cocept***************************
        else {
            if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
                List<LclBookingPiece> lclCommodityList = lclSession.getCommodityList();
                LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
                lclCommodityForm.setPersonalEffects(lclBookingPiece.getPersonalEffects());
                request.setAttribute("lclBookingPiece", lclBookingPiece);
                lclSession.setCommodityList(lclCommodityList);
            }
        }

        //**********************************************************************
        PortsDAO portsDAO = new PortsDAO();
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            String clientNo = request.getParameter("clientNo");
            String notifyNo = request.getParameter("notifyNo");
            String consNo = request.getParameter("consNo");
            String agentNo = request.getParameter("agentNo");
            GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
            String[] commodity = null;
            if (CommonUtils.isNotEmpty(clientNo)) {
                commodity = generalInformationDAO.getCommodity(clientNo, "Client", null);
            }
            if (null == commodity || CommonUtils.isEmpty(commodity[0])) {
                if (CommonUtils.isNotEmpty(notifyNo)) {
                    commodity = generalInformationDAO.getCommodity(notifyNo, "Notify", null);
                }
                if ((null == commodity || CommonUtils.isEmpty(commodity[0])) && CommonUtils.isNotEmpty(consNo)) {
                    commodity = generalInformationDAO.getCommodity(consNo, "Consignee", null);
                }
                if ((null == commodity || CommonUtils.isEmpty(commodity[0])) && CommonUtils.isNotEmpty(agentNo)) {
                    commodity = generalInformationDAO.getCommodity(agentNo, "Agent", notifyNo);
                }
            }
            LclBookingPiece lclBookingPiece = new LclBookingPiece();
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", commodity[0]);
                lclBookingPiece.setCommodityType(commodityType);
            }
            request.setAttribute("lclBookingPiece", lclBookingPiece);
        } else {
            String commodity = request.getParameter("clientNo");
            if (CommonUtils.isNotEmpty(commodity)) {
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", commodity);
                if (null != commodityType) {
                    LclBookingPiece lclBookingPiece = new LclBookingPiece();
                    lclBookingPiece.setCommodityType(commodityType);
                    lclBookingPiece.setPieceDesc(commodityType.getDescEn());
                    request.setAttribute("lclBookingPiece", lclBookingPiece);
                }
            }
            String rateType = "R".equalsIgnoreCase(lclCommodityForm.getRateType()) ? "Y" : lclCommodityForm.getRateType();
            String origin = "";
            String destination = "";
            RefTerminal refterminal = null;
            Ports ports = null;
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            if (CommonUtils.isNotEmpty(lclCommodityForm.getOrigin())) {
                UnLocation unLocation = new UnLocationDAO().getUnlocation(lclCommodityForm.getOrigin());
                if (unLocation != null && unLocation.getLclRatedSourceId() != null) {
                    lclCommodityForm.setOrigin(unLocation.getLclRatedSourceId().getUnLocationCode());
                }
            }
            refterminal = refterminaldao.getTerminalByUnLocation(lclCommodityForm.getOrigin(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                origin = refterminal.getTrmnum();
            }
            ports = portsDAO.getByProperty("unLocationCode", lclCommodityForm.getDestination());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                destination = ports.getEciportcode();
            }
            lclCommodityForm.setOriginNo(origin);
            lclCommodityForm.setDestinationNo(destination);
            boolean dojocount;
            if (CommonUtils.isNotEmpty(lclCommodityForm.getOrigin())) {
                dojocount = lclBookingPieceDAO.hasDojoList(origin, destination);
            } else {
                dojocount = lclBookingPieceDAO.hasEmptyOriginDojoList();
            }
            request.setAttribute("dojoCount", dojocount ? "Y" : "N");
            request.setAttribute("cfcl", cfcl);
            if (CommonUtils.isNotEmpty(fileNumberId)) {
                LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
                request.setAttribute("bookingExport", bookingExport);
            }
        }
        session.removeAttribute("cargoReceivedList");
        request.setAttribute("editDimFlag", request.getParameter("editDimFlag"));
        request.setAttribute("state", request.getParameter("state"));
        request.setAttribute("verifyCargo", verifyCargo);
        request.setAttribute("lclCommodityForm", lclCommodityForm);
        lclCommodityForm.setId("");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward updateBookedComm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        LclBookingPiece lclBookingPiece = lclCommodityForm.getLclBookingPiece();
        if (null != lclBookingPiece) {
            if (CommonUtils.isNotEmpty(lclBookingPiece.getCommName())) {
                lclBookingPiece.getCommodityType().setDescEn(lclBookingPiece.getCommName());
            }
            if (CommonUtils.isNotEmpty(lclBookingPiece.getCommNo())) {
                lclBookingPiece.getCommodityType().setCode(lclBookingPiece.getCommNo());
            }
            if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName()) && lclBookingPiece.getLclFileNumber().getId() != 0
                    && lclBookingPiece.getLclFileNumber().getId() != null) {
                lclCommodityForm.setFileStatus(new LclFileNumberDAO().getFileStatus(lclBookingPiece.getLclFileNumber().getId()));
                request.setAttribute("isHotCodeRemarks", new LclRemarksDAO().isRemarks(lclBookingPiece.getLclFileNumber().getId(),
                        "ADDED HOT CODE XXX COMMENTS"));
                request.setAttribute("lcl3PList", new Lcl3pRefNoDAO().get3PRefList(lclBookingPiece.getLclFileNumber().getId(), "TR"));
                request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", lclBookingPiece.getLclFileNumber().getId()));

            }
        }
        BigDecimal metricDivisor = new BigDecimal(35.314);
        BigDecimal weightDivisor = new BigDecimal(2.2046);
        BigDecimal measureImp = new BigDecimal(0.000);
        BigDecimal weightImp = new BigDecimal(0.000);
        BigDecimal measureMet = new BigDecimal(0.000);
        BigDecimal weightMet = new BigDecimal(0.000);
        BigDecimal measureImpVal = null;
        if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            measureImp = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalMeasureImp().replaceAll("-?[^-0-9.]+", "")));
            weightImp = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalWeightImp().replaceAll("-?[^-0-9.]+", "")));
        } else {
            measureImp = new BigDecimal(lclCommodityForm.getTotalMeasureImp().replaceAll("-?[^-0-9.]+", ""));
            weightImp = new BigDecimal(lclCommodityForm.getTotalWeightImp().replaceAll("-?[^-0-9.]+", ""));
        }
        measureMet = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalMeasureMet().replaceAll("-?[^-0-9.]+", "")));
        BigDecimal MeasureMetM = measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        weightMet = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalWeightMet().replaceAll("-?[^-0-9.]+", "")));
        BigDecimal weightMetM = weightMet.divide(new BigDecimal(1.000), 2, BigDecimal.ROUND_FLOOR);
        //BigDecimal measureImperial=measureImp.divide(new BigDecimal(1.000), 2, BigDecimal.ROUND_HALF_UP);
        request.setAttribute("bookedPieceCount", lclCommodityForm.getTotalPieces());
        if (lclCommodityForm.getActualUom().equals("I")) {
            if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                request.setAttribute("totalMeasureImp", measureImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("totalWeightImp", weightImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("totalMeasureMet", df.format(measureImp.doubleValue() / 35.314));
                request.setAttribute("totalWeightMet", weightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
            } else {
                if (lclCommodityForm.getUps()) {
                    measureImpVal = new BigDecimal(NumberUtils.roundDecimalInteger(measureImp.doubleValue() + 2));
                    request.setAttribute("totalMeasureImp", NumberUtils.roundDecimalInteger(measureImpVal.doubleValue()));
                } else {
                    if (measureImp.doubleValue() < 0.5) {
                        measureImp = BigDecimal.ONE;
                    }
                    measureImpVal = new BigDecimal(NumberUtils.roundDecimalInteger(measureImp.doubleValue()));
                    request.setAttribute("totalMeasureImp", NumberUtils.roundDecimalInteger(measureImpVal.doubleValue()));

                }
                BigDecimal weightImpVal = new BigDecimal(NumberUtils.roundDecimalInteger(weightImp.doubleValue()));
                request.setAttribute("totalWeightImp", NumberUtils.roundDecimalInteger(weightImp.doubleValue()));
                request.setAttribute("totalMeasureMet", df.format(measureImpVal.doubleValue() / 35.314));
                request.setAttribute("totalWeightMet", weightImpVal.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
                if (CommonUtils.isNotEmpty(lclBookingPiece.getLclFileNumber().getId())) {
                    LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclBookingPiece.getLclFileNumber().getId());
                    request.setAttribute("bookingExport", bookingExport);
                }
            }
            request.setAttribute("packId", lclCommodityForm.getPackageTypeId());
            request.setAttribute("pack", lclCommodityForm.getPackageType());
        } else if (lclCommodityForm.getActualUom().equals("M")) {
            if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                request.setAttribute("totalMeasureImp", df.format(MeasureMetM.doubleValue() * 35.314));
                request.setAttribute("totalWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            } else {
                request.setAttribute("totalMeasureImp", df.format(MeasureMetM.doubleValue() * 35.314));
                request.setAttribute("totalWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 2, BigDecimal.ROUND_HALF_UP));

            }
            request.setAttribute("totalMeasureMet", measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalWeightMet", weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("packId", lclCommodityForm.getPackageTypeId());
            request.setAttribute("pack", lclCommodityForm.getPackageType());
        }
        request.setAttribute("lclBookingPiece", lclBookingPiece);
        /* Original Short Ship File Status */
        if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            setShortShipFileState(lclCommodityForm.getShortShipFileNo(), request);
        }
//        LclUtils lclUtils = new LclUtils();
//        lclUtils.addOverSizeRemarks(uom1,actualHeight,actualWidth,actualLength,fileNumberId,request,lclSession,getCurrentUser(request));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward updateActualComm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        LclBookingPiece lclBookingPiece = lclCommodityForm.getLclBookingPiece();
        if (null != lclBookingPiece) {
            lclBookingPiece.setModifiedBy(getCurrentUser(request));
            lclBookingPiece.setModifiedDatetime(new Date());
            if (CommonUtils.isNotEmpty(lclBookingPiece.getCommName())) {
                lclBookingPiece.getCommodityType().setDescEn(lclBookingPiece.getCommName());
            }
            if (CommonUtils.isNotEmpty(lclBookingPiece.getCommNo())) {
                lclBookingPiece.getCommodityType().setCode(lclBookingPiece.getCommNo());
            }
            if (null != lclBookingPiece.getPackageType()) {
                PackageType packageType = new PackageTypeDAO().findById(lclBookingPiece.getPackageType().getId());
                lclBookingPiece.setPackageType(packageType);
                lclCommodityForm.setPackageType(packageType.getDescription());
                if (lclBookingPiece.getActualPackageType() == null) {
                    lclCommodityForm.setActualPackageName(packageType.getDescription());
                    lclCommodityForm.setActualPackageTypeId(packageType.getId());
                }
            }
            lclCommodityForm.setLclBookingPiece(lclBookingPiece);
            if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                lclCommodityForm.setFileStatus(new LclFileNumberDAO().getFileStatus(lclBookingPiece.getLclFileNumber().getId()));
                request.setAttribute("isHotCodeRemarks", new LclRemarksDAO().isRemarks(lclBookingPiece.getLclFileNumber().getId(),
                        "ADDED HOT CODE XXX COMMENTS"));
                request.setAttribute("lcl3PList", new Lcl3pRefNoDAO().get3PRefList(lclBookingPiece.getLclFileNumber().getId(), "TR"));
                request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", lclBookingPiece.getLclFileNumber().getId()));

            }
            if (CommonUtils.isNotEmpty(lclBookingPiece.getLclFileNumber().getId())) {
                LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclBookingPiece.getLclFileNumber().getId());
                request.setAttribute("bookingExport", bookingExport);
            }
        }
        BigDecimal metricDivisor = new BigDecimal(35.314);
        BigDecimal weightDivisor = new BigDecimal(2.2046);
        BigDecimal measureImp = new BigDecimal(0.000);
        BigDecimal weightImp = new BigDecimal(0.000);
        BigDecimal measureMet = new BigDecimal(0.000);
        BigDecimal weightMet = new BigDecimal(0.000);
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            measureImp = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalMeasureImp().replaceAll("-?[^-0-9.]+", "")));
            weightImp = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalWeightImp().replaceAll("-?[^-0-9.]+", "")));
        } else {
            measureImp = new BigDecimal(lclCommodityForm.getTotalMeasureImp().replaceAll("-?[^-0-9.]+", ""));
            weightImp = new BigDecimal(lclCommodityForm.getTotalWeightImp().replaceAll("-?[^-0-9.]+", ""));
        }
        measureMet = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalMeasureMet().replaceAll("-?[^-0-9.]+", "")));
        BigDecimal MeasureMetM = measureMet.divide(new BigDecimal(1.000), 2, BigDecimal.ROUND_HALF_UP);
        weightMet = new BigDecimal(Double.parseDouble(lclCommodityForm.getTotalWeightMet().replaceAll("-?[^-0-9.]+", "")));
        BigDecimal weightMetM = weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        request.setAttribute("actualPieceCount", lclCommodityForm.getTotalPieces());
        if (lclCommodityForm.getActualUom().equals("I")) {
            if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                request.setAttribute("actualMeasureImp", measureImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("actualWeightImp", weightImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("actualMeasureMet", df.format(measureImp.doubleValue() / 35.314));
                request.setAttribute("actualWeightMet", weightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
            } else {
                BigDecimal measureImpVal = new BigDecimal(Math.round(measureImp.doubleValue()));
                BigDecimal weightImpVal = new BigDecimal(NumberUtils.roundDecimalInteger(weightImp.doubleValue()));
                if (lclCommodityForm.getUps()) {
                    measureImp = new BigDecimal(NumberUtils.roundDecimalInteger(measureImpVal.doubleValue() + 2));
                    request.setAttribute("actualMeasureImp", NumberUtils.roundDecimalInteger(measureImp.doubleValue()));

                } else {
                    if (measureImp.doubleValue() < 0.5) {
                        measureImp = BigDecimal.ONE;
                    }
                    measureImp = new BigDecimal(NumberUtils.roundDecimalInteger(measureImp.doubleValue()));
                    request.setAttribute("actualMeasureImp", NumberUtils.roundDecimalInteger(measureImp.doubleValue()));

                }
                request.setAttribute("actualWeightImp", NumberUtils.roundDecimalInteger(weightImp.doubleValue()));
                request.setAttribute("actualMeasureMet", df.format(measureImp.doubleValue() / 35.314));
                request.setAttribute("actualWeightMet", weightImpVal.divide(weightDivisor, 3, BigDecimal.ROUND_HALF_UP));
            }

        } else if (lclCommodityForm.getActualUom().equals("M")) {
            request.setAttribute("actualMeasureImp", df.format(MeasureMetM.doubleValue() * 35.314));
            if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                request.setAttribute("actualWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            } else {
                request.setAttribute("actualWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 2, BigDecimal.ROUND_HALF_UP));
            }
            request.setAttribute("actualMeasureMet", measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightMet", weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
        }
        if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            request.setAttribute("totalWeightImp", lclBookingPiece.getBookedWeightImperial());
            request.setAttribute("totalMeasureImp", lclBookingPiece.getBookedVolumeImperial());
            request.setAttribute("bookedVolumeImperial", lclBookingPiece.getBookedVolumeImperial());
            request.setAttribute("bookedWeightImperial", lclBookingPiece.getBookedWeightImperial());
        } else {
            request.setAttribute("totalWeightImp", lclBookingPiece.getBookedWeightImperial() != null ? lclBookingPiece.getBookedWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedWeightImperial());
            request.setAttribute("totalMeasureImp", lclBookingPiece.getBookedVolumeImperial() != null ? lclBookingPiece.getBookedVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedVolumeImperial());
            request.setAttribute("bookedVolumeImperial", lclBookingPiece.getBookedVolumeImperial() != null ? lclBookingPiece.getBookedVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedVolumeImperial());
            request.setAttribute("bookedWeightImperial", lclBookingPiece.getBookedWeightImperial() != null ? lclBookingPiece.getBookedWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedWeightImperial());
        }
        request.setAttribute("barrel", lclBookingPiece.isIsBarrel());
        request.setAttribute("hazmat", lclBookingPiece.isHazmat());
        request.setAttribute("bookedPieceCount", lclBookingPiece.getBookedPieceCount());
        request.setAttribute("totalWeightMet", lclBookingPiece.getBookedWeightMetric());
        request.setAttribute("totalMeasureMet", lclBookingPiece.getBookedVolumeMetric());
        request.setAttribute("lclBookingPiece", lclBookingPiece);

        /* Original Short Ship File Status */
        if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            setShortShipFileState(lclCommodityForm.getShortShipFileNo(), request);
        }
        return mapping.findForward(SUCCESS);
    }
    /*
     * while clicking on Details button in commodity Screen display Warehouse Details
     */

    public ActionForward displayWhse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        LclBookingPieceWhseDAO lclBookingPieceWhseDAO = new LclBookingPieceWhseDAO();
        CommodityDetailsDAO commodityDetailsDAO = new CommodityDetailsDAO();
        String fileNumberId = request.getParameter("fileNumberId");
        List detailList = null;
        List<LclBookingPieceWhse> lclBookingPieceWhseList = null;
        User thisUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(lclCommodityForm.getPieceId())) {
            detailList = commodityDetailsDAO.findDetailProperty("lclBookingPiece.id", Long.parseLong(lclCommodityForm.getPieceId()));
            lclBookingPieceWhseList = lclBookingPieceWhseDAO.findByFileAndCommodityList(Long.parseLong(lclCommodityForm.getPieceId()));
        }
        String unitId = request.getParameter("unitId");
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName()) && CommonUtils.isNotEmpty(unitId)) {
            LclUnitWhseDAO lclUnitWhseDAO = new LclUnitWhseDAO();
            List<LclUnitWhse> lclUnitWhseList = lclUnitWhseDAO.findByProperty("lclUnit.id", Long.parseLong(unitId));
            if (null != lclUnitWhseList && !lclUnitWhseList.isEmpty()) {
                LclUnitWhse lclUnitWhse = (LclUnitWhse) lclUnitWhseList.get(0);
                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse())) {
                    lclCommodityForm.setWhseLocation(lclUnitWhse.getWarehouse().getWarehouseName());
                    lclCommodityForm.setWarehouseId(lclUnitWhse.getWarehouse().getId());
                    request.setAttribute("warehouseNo", lclUnitWhse.getWarehouse().getWarehouseNo());
                }
            }
        }
//       comented for the requirement change
//        if ("Exports".equalsIgnoreCase(lclCommodityForm.getModuleName()) && CommonUtils.isNotEmpty(request.getParameter("warhsNo"))) {
//            Warehouse warehouse = new WarehouseDAO().getWareHouseBywarehsNo(request.getParameter("warhsNo"));
//            if (warehouse != null) {
//                lclCommodityForm.setWhseLocation(warehouse.getWarehouseName());
//                lclCommodityForm.setWarehouseId(warehouse.getId());
//                request.setAttribute("warehouseNo", warehouse.getWarehouseNo());
//            }
//        }
        if (CommonUtils.isNotEmpty(detailList)) {
            LclBookingPieceDetail detail = (LclBookingPieceDetail) detailList.get(0);
            request.setAttribute("actualUom", detail.getActualUom());
        }
        request.setAttribute("fileNumberId", fileNumberId);
        request.setAttribute("detailList", detailList);
        request.setAttribute("lclBookingPieceWhseList", lclBookingPieceWhseList);
        request.setAttribute("lclCommodityForm", lclCommodityForm);
        return mapping.findForward("whseDetail");
    }

    public ActionForward closeDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        List<LclBookingPiece> lclPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        for (LclBookingPiece lbp : lclPieceList) {
            lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(Long.parseLong(fileNumberId), lbp.getId()));
            lbp.setLclBookingAcList(new LclCostChargeDAO().findByFileAndCommodityList(Long.parseLong(fileNumberId), lbp.getId()));
            lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
        }
        request.setAttribute("lclCommodityList", lclPieceList);
        return mapping.findForward("commodityDesc");
    }

    public ActionForward displayTariffDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        commodityTypeDAO commodityDAO = new commodityTypeDAO();
        int totalPageSize = 0;
        if (CommonUtils.isNotEmpty(lclCommodityForm.getTariff())) {
            if (lclCommodityForm.getOrigin() != null && !lclCommodityForm.getOrigin().equals("")) {
                totalPageSize += commodityDAO.getCommodityCountWithoutCarrier(lclCommodityForm.getTariff(), lclCommodityForm.getOrigin(), lclCommodityForm.getDestination());
            } else {
                totalPageSize += commodityDAO.getCommodityCountWithoutCarrier(lclCommodityForm.getTariff(), lclCommodityForm.getOrgId(), lclCommodityForm.getDestId());
            }
        } else {
            if (lclCommodityForm.getOrigin() != null && !lclCommodityForm.getOrigin().equals("")) {
                totalPageSize += commodityDAO.getTotalCommodityWithoutCarrier(lclCommodityForm.getOrigin(), lclCommodityForm.getDestination());
            } else {
                totalPageSize += commodityDAO.getTotalCommodityWithoutCarrier(lclCommodityForm.getOrgId(), lclCommodityForm.getDestId());
            }
        }
        int noOfPages = totalPageSize / lclCommodityForm.getCurrentPageSize();
        int remainSize = totalPageSize % lclCommodityForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages += 1;
        }
        int start = (lclCommodityForm.getCurrentPageSize() * (lclCommodityForm.getPageNo() - 1));
        int end = lclCommodityForm.getCurrentPageSize();

        lclCommodityForm.setNoOfPages(noOfPages);

        lclCommodityForm.setTotalPageSize(totalPageSize);
        List<CommodityType> commodityTypeList = null;
        if (lclCommodityForm.getOrigin() != null && !lclCommodityForm.getOrigin().equals("")) {
            commodityTypeList = new commodityTypeDAO().findAllCommodityTypeListWithoutCarrier(lclCommodityForm.getTariff(), lclCommodityForm.getOrigin(), lclCommodityForm.getDestination(), start, end);
        } else {
            commodityTypeList = new commodityTypeDAO().findAllCommodityTypeListWithoutCarrier(lclCommodityForm.getTariff(), lclCommodityForm.getOrgId(), lclCommodityForm.getDestId(), start, end);
        }
        request.setAttribute("commodityTypeList", commodityTypeList);
        request.setAttribute("tariff", lclCommodityForm.getTariff());
        return mapping.findForward("tariff");
    }

//    public ActionForward showCargo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
//            HttpServletResponse response) throws Exception {
//        return mapping.findForward(SHOW_CARGO);
//    }
    /*
     * Add or Edit Warehouse Details
     */
    public ActionForward addWhseDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        LclBookingPieceWhseDAO lclBookingPieceWhseDAO = new LclBookingPieceWhseDAO();
        CommodityDetailsDAO commodityDetailsDAO = new CommodityDetailsDAO();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        LclBookingPieceWhse lclBookingPieceWhse = null;
        List detailList = null;
        Date now = new Date();
        User thisUser = getCurrentUser(request);
        List<LclBookingPieceWhse> lclBookingPieceWhseList = null;
        String addOrUpdate = "UPDATE";
        Warehouse previousWareHouse = null;
        if (CommonUtils.isNotEmpty(lclCommodityForm.getPieceId())) {
            if (lclCommodityForm.getBookingPieceWhseId() != null && !lclCommodityForm.getBookingPieceWhseId().equals("")) {
                lclBookingPieceWhse = lclBookingPieceWhseDAO.findById(Long.parseLong(lclCommodityForm.getBookingPieceWhseId()));
                previousWareHouse = lclBookingPieceWhse.getWarehouse();
            }
            if (lclBookingPieceWhse == null) {
                addOrUpdate = "ADD";
                lclBookingPieceWhse = new LclBookingPieceWhse();
                lclBookingPieceWhse.setEnteredDatetime(now);
                lclBookingPieceWhse.setEnteredBy(thisUser);
            }
            lclBookingPieceWhse.setLclBookingPiece(lclBookingPieceDAO.findById(Long.parseLong(lclCommodityForm.getPieceId())));
            if (CommonUtils.isNotEmpty(lclCommodityForm.getCityLocation())) {
                lclBookingPieceWhse.setLocation(lclCommodityForm.getCityLocation());
            } else {
                lclBookingPieceWhse.setLocation(null);
            }
            if (CommonUtils.isNotEmpty(lclCommodityForm.getWarehouseId())) {
                lclBookingPieceWhse.setWarehouse(warehouseDAO.findById(lclCommodityForm.getWarehouseId()));
            } else {
                lclBookingPieceWhse.setWarehouse(null);
            }
            if ("Exports".equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                new LCLBookingDAO().updateModifiedDateTime(lclCommodityForm.getFileNumberId(), thisUser.getUserId());
                if (CommonUtils.isNotEmpty(lclCommodityForm.getStowedByUserId())) {
                    lclBookingPieceWhse.setStowedDatetime(now);
                    lclBookingPieceWhse.setStowedByUser(new UserDAO().findById(Integer.parseInt(lclCommodityForm.getStowedByUserId())));
                } else {
                    lclBookingPieceWhse.setStowedDatetime(null);
                    lclBookingPieceWhse.setStowedByUser(null);
                }
            }
            lclBookingPieceWhse.setModifiedDatetime(now);
            lclBookingPieceWhse.setModifiedBy(thisUser);
            lclBookingPieceWhseDAO.saveOrUpdate(lclBookingPieceWhse);
            boolean isBarrenCombo = new LclBookingPieceDAO().isBarrelComboShipment(lclCommodityForm.getFileNumberId());
            if (null != lclBookingPieceWhse.getLclBookingPiece() && isBarrenCombo) {
                updateBarrelComboCommodityWareHouse(lclCommodityForm, lclBookingPieceWhse.getLclBookingPiece().getId(), addOrUpdate, lclBookingPieceWhse, previousWareHouse);
            }
        }
        if (CommonUtils.isNotEmpty(lclCommodityForm.getPieceId())) {
            detailList = commodityDetailsDAO.findDetailProperty("lclBookingPiece.id", Long.parseLong(lclCommodityForm.getPieceId()));
            lclBookingPieceWhseList = lclBookingPieceWhseDAO.findByFileAndCommodityList(Long.parseLong(lclCommodityForm.getPieceId()));
        }
        lclCommodityForm.setWarehouseId(0);
        lclCommodityForm.setWhseLocation("");
        lclCommodityForm.setStowedByUser("");
        lclCommodityForm.setStowedByUserId("");
        lclCommodityForm.setBookingPieceWhseId("");
        request.setAttribute("lclBookingPieceWhseList", lclBookingPieceWhseList);
        request.setAttribute("detailList", detailList);
        request.setAttribute("lclCommodityForm", lclCommodityForm);
        return mapping.findForward("whseDetail");
    }

    public ActionForward editLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession");
        LclBookingPiece lclBookingPiece = null;
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        String status = request.getParameter("status1");
        String copyVal = request.getParameter("copyVal");
        String cfcl = request.getParameter("cfcl");
        if (request.getParameter("fileNumberId") != null && !request.getParameter("fileNumberId").trim().equals("")
                && !request.getParameter("fileNumberId").trim().equals("0")) {
            lclBookingPiece = lclCommodityForm.getLclBookingPiece();
            request.setAttribute("id", lclCommodityForm.getId());
            if (lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().getCode() != null && !lclBookingPiece.getCommodityType().getCode().equals("")) {
                request.setAttribute("commCode", lclBookingPiece.getCommodityType().getCode());
            }
            if ((CommonUtils.isNotEmpty(lclBookingPiece.getBookedPieceCount())) && (lclBookingPiece.getBookedPieceCount() >= 2)) {
                lclCommodityForm.setPackageType(lclBookingPiece.getPackageType().getDescription() + "" + lclBookingPiece.getPackageType().getPlural().toLowerCase());
            } else {
                lclCommodityForm.setPackageType(lclBookingPiece.getPackageType().getDescription());
            }
            if (null != lclBookingPiece.getActualPackageType()) {
                if ((null != lclBookingPiece.getActualPieceCount()) && (lclBookingPiece.getActualPieceCount() >= 2)) {
                    lclCommodityForm.setActualPackageName(lclBookingPiece.getActualPackageType().getDescription() + "" + lclBookingPiece.getPackageType().getPlural().toLowerCase());
                } else {
                    lclCommodityForm.setActualPackageName(lclBookingPiece.getActualPackageType().getDescription());
                }
                lclCommodityForm.setActualPackageTypeId(lclBookingPiece.getActualPackageType().getId());
            }

        } else if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0 && request.getParameter("id") != null) {
            lclBookingPiece = lclSession.getCommodityList().get(Integer.parseInt(request.getParameter("id")));
            if (CommonUtils.isNotEmpty(request.getParameter("copyVal")) && !copyVal.equals("Y")) {
//                    lclBookingPiece.getCommodityType().setDescEn(lclBookingPiece.getCommName());
//                    lclBookingPiece.getCommodityType().setCode(lclBookingPiece.getCommNo());
                request.setAttribute("id", request.getParameter("id"));
                request.setAttribute("commCode", lclBookingPiece.getCommNo());
                //lclCommodityForm.setPackageType(lclBookingPiece.getPkgName());
            } else {
                if (lclBookingPiece.getCommodityType() != null) {
                    lclBookingPiece.setCommodityType(new commodityTypeDAO().findById(lclBookingPiece.getCommodityType().getId()));
                }
                if (CommonUtils.isNotEmpty(lclBookingPiece.getPkgName())) {
                    lclCommodityForm.setPackageType(lclBookingPiece.getPkgName());
                }
                if (lclBookingPiece.isIsBarrel()) {
                    lclCommodityForm.setPackageType("BARREL");
                }
            }
        }
        LclFileNumberDAO fileNumberDAO = new LclFileNumberDAO();
        String rateType = "";
        String origin = "";
        String destination = "";
        RefTerminal refterminal = new RefTerminal();
        Ports ports = new Ports();
        PortsDAO portsdao = new PortsDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        if (lclCommodityForm.getRateType().equals("R")) {
            rateType = "Y";
        } else {
            rateType = lclCommodityForm.getRateType();
        }
        // logic for only export to fetch mirror city.
        if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())
                && CommonUtils.isNotEmpty(lclCommodityForm.getOrigin())) {
            UnLocation unLocation = new UnLocationDAO().getUnlocation(lclCommodityForm.getOrigin());
            if (unLocation != null && unLocation.getLclRatedSourceId() != null) {
                lclCommodityForm.setOrigin(unLocation.getLclRatedSourceId().getUnLocationCode());
            }
        }
        refterminal = refterminaldao.getTerminalByUnLocation(lclCommodityForm.getOrigin(), rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            origin = refterminal.getTrmnum();
        }
        ports = portsdao.getByProperty("unLocationCode", lclCommodityForm.getDestination());
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            destination = ports.getEciportcode();
        }
        lclCommodityForm.setOriginNo(origin);
        lclCommodityForm.setDestinationNo(destination);
        if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            boolean dojocount;
            if (CommonUtils.isNotEmpty(lclCommodityForm.getOrigin())) {
                dojocount = lclBookingPieceDAO.hasDojoList(origin, destination);
            } else {
                dojocount = lclBookingPieceDAO.hasEmptyOriginDojoList();
            }
            request.setAttribute("dojoCount", dojocount ? "Y" : "N");
            request.setAttribute("cfcl", cfcl);
        }
        if ((lclBookingPiece != null && status != null) && "WV".equalsIgnoreCase(status) && "Exports".equalsIgnoreCase(lclCommodityForm.getModuleName())) {
            // lclBookingPiece.setWeightVerifiedBy(thisUser);
            //lclCommodityForm.setWeightverifiedUserId(String.valueOf(thisUser.getId()));
            // lclCommodityForm.setWeightVerified(true);
            lclCommodityForm.setActualPackageTypeId(lclBookingPiece.getPackageType().getId());
            lclCommodityForm.setActualPackageName(lclBookingPiece.getPackageType().getDescription());
        }
        request.setAttribute("lclCommodityForm", lclCommodityForm);
        request.setAttribute("editDimFlag", request.getParameter("editDimFlag"));
        if (lclBookingPiece != null) {
            request.setAttribute("pkgName", lclBookingPiece.getPkgName());
            if (lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().getCode() != null && !lclBookingPiece.getCommodityType().getCode().equals("")) {
                request.setAttribute("commCode", lclBookingPiece.getCommodityType().getCode());
            }
            request.setAttribute("bookingPieceCount", lclBookingPiece.getBookedPieceCount());
            request.setAttribute("barrel", lclBookingPiece.isIsBarrel());
            request.setAttribute("hazmat", lclBookingPiece.isHazmat());
            request.setAttribute("bookedPieceCount", lclBookingPiece.getBookedPieceCount());
            if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                request.setAttribute("totalWeightImp", lclBookingPiece.getBookedWeightImperial());
                request.setAttribute("totalMeasureImp", lclBookingPiece.getBookedVolumeImperial());
                request.setAttribute("bookedWeightImperial", lclBookingPiece.getBookedWeightImperial());
                request.setAttribute("bookedVolumeImperial", lclBookingPiece.getBookedVolumeImperial());
            } else {
                request.setAttribute("bookedWeightImperial", lclBookingPiece.getBookedWeightImperial() != null ? lclBookingPiece.getBookedWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedWeightImperial());
                request.setAttribute("bookedVolumeImperial", lclBookingPiece.getBookedVolumeImperial() != null ? lclBookingPiece.getBookedVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedVolumeImperial());
                request.setAttribute("totalWeightImp", lclBookingPiece.getBookedWeightImperial() != null ? lclBookingPiece.getBookedWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedWeightImperial());
                request.setAttribute("totalMeasureImp", lclBookingPiece.getBookedVolumeImperial() != null ? lclBookingPiece.getBookedVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getBookedVolumeImperial());
            }
            request.setAttribute("totalWeightMet", lclBookingPiece.getBookedWeightMetric());
            request.setAttribute("totalMeasureMet", lclBookingPiece.getBookedVolumeMetric());
            Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
            if (request.getParameter("fileNumberId") != null && !request.getParameter("fileNumberId").trim().equals("")
                    && !request.getParameter("fileNumberId").trim().equals("0")) {
                if (LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                    request.setAttribute("actualWeightImp", lclBookingPiece.getActualWeightImperial());
                    request.setAttribute("actualMeasureImp", lclBookingPiece.getActualVolumeImperial());
                } else {
                    request.setAttribute("actualWeightImp", lclBookingPiece.getActualWeightImperial() != null ? lclBookingPiece.getActualWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getActualWeightImperial());
                    request.setAttribute("actualMeasureImp", lclBookingPiece.getActualVolumeImperial() != null ? lclBookingPiece.getActualVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBookingPiece.getActualVolumeImperial());

                }
                request.setAttribute("actualPieceCount", lclBookingPiece.getActualPieceCount());
                request.setAttribute("actualWeightMet", lclBookingPiece.getActualWeightMetric());
                request.setAttribute("actualMeasureMet", lclBookingPiece.getActualVolumeMetric());
                LclFileNumber lclFileNumber = fileNumberDAO.findById(Long.parseLong(request.getParameter("fileNumberId")));
                request.setAttribute("status", lclFileNumber.getStatus());
                lclCommodityForm.setFileStatus(fileNumberDAO.getFileStatus(Long.parseLong(request.getParameter("fileNumberId"))));

            }
            LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclBookingPiece.getLclFileNumber().getId());
            if (LCL_EXPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                if (lclBookingPiece.getLclFileNumber() != null && lclBookingPiece.getLclFileNumber().getId() != 0) {
                    /* Hot Code XXX Comments and displaying mouse over tooltip in HotCode List  */
                    request.setAttribute("isHotCodeRemarks", new LclRemarksDAO().isRemarks(lclBookingPiece.getLclFileNumber().getId(),
                            "ADDED HOT CODE XXX COMMENTS"));
                    /* get Tracking List  */
                    request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(lclBookingPiece.getLclFileNumber().getId(), "TR"));
                    /* get HotCode List  */
                    request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", lclBookingPiece.getLclFileNumber().getId()));
                    lclCommodityForm.setUps(bookingExport.getUps());
                }
                /* Original Short Ship File Status */
                setShortShipFileState(lclCommodityForm.getShortShipFileNo(), request);
            }
            request.setAttribute("lclBookingPiece", lclBookingPiece);
            request.setAttribute("bookingExport", bookingExport);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward modifyCommodityAndCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        String fileId = request.getParameter("fileNumberId");
        String destination = "";
        String origin = "";
        String originUnLocation = "";
        Long longFileId = new Long(fileId);
        LclBooking lclBooking = new LCLBookingDAO().findById(longFileId);
        User user = (User) request.getSession().getAttribute("loginuser");
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        String rateType = lclBooking.getRateType();
        if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin())) {
            origin = lclBooking.getPortOfOrigin().getUnLocationName().toUpperCase();
        }
        if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin())) {
            destination = lclBooking.getFinalDestination().getUnLocationName().toUpperCase();
        }
        if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin())) {
            originUnLocation = lclBooking.getPortOfOrigin().getUnLocationCode();
        }
        if (rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        String inlandChargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        LclBookingAc lclBookingAcPickup = lclCostChargeDAO.manaualChargeValidate(longFileId, inlandChargeCode, false);
        if (lclBookingAcPickup != null) {
            LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(longFileId);
            lclBookingPad.setLclBookingAc(null);
            lclBookingPadDAO.saveOrUpdate(lclBookingPad);
        }
        List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.executeQuery("from LclBookingAc where lclFileNumber.id=" + longFileId + "and manualEntry=0");
        for (LclBookingAc lbac : lclBookingAcList) {
            if (lbac != null) {
                String chargeCode = lbac.getArglMapping().getChargeCode();
                BigDecimal amount = lbac.getArAmount();
                LclRemarks lclRemarks = new LclRemarks();
                LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
                LclFileNumber lclFileNumber = lclFileNumberDAO.getByProperty("id", longFileId);
                lclRemarks.setLclFileNumber(lclFileNumber);
                lclRemarks.setEnteredBy(user);
                lclRemarks.setEnteredDatetime(new Date());
                lclRemarks.setModifiedBy(user);
                lclRemarks.setModifiedDatetime(new Date());
                lclRemarks.setRemarks("DELETED -> Charge Code -> " + chargeCode + " Charge Amount -> " + amount);
                lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
                new LclRemarksDAO().saveOrUpdate(lclRemarks);
                lclCostChargeDAO.delete(lbac.getId());
            }
        }
        Date pickupReadyDate = null;
        HttpSession session = request.getSession();
        String realPath = session.getServletContext().getRealPath("/xml/");
        String fileName = "ctsresponse" + session.getId() + ".xml";
        CallCTSWebServices ctsweb = new CallCTSWebServices();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(longFileId);
        String fromZip[] = request.getParameter("fromZip").split("-");
        String toZip = "";
        if (lclBookingPad != null && lclBookingPad.getPickupReadyDate() != null) {
            pickupReadyDate = lclBookingPad.getPickupReadyDate();
        }
        if (rateType != null && rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(originUnLocation, rateType);
        if (terminal != null) {
            toZip = terminal.getZipcde();
        }
        BigDecimal weight = new BigDecimal(0.000);
        BigDecimal measure = new BigDecimal(0.000);
        if (CommonUtils.isNotEmpty(longFileId)) {
            List<LclBookingPiece> commList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
            for (LclBookingPiece lbp : commList) {
                if (lbp.getActualWeightImperial() != null) {
                    if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                        weight = weight.add(lbp.getActualWeightImperial());
                    } else {
                        weight = weight.add(lbp.getActualWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                } else if (lbp.getBookedWeightImperial() != null) {
                    weight = weight.add(lbp.getBookedWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (lbp.getActualVolumeImperial() != null) {
                    if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                        measure = measure.add(lbp.getActualVolumeImperial());
                    } else {
                        measure = measure.add(lbp.getActualVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                } else if (lbp.getBookedVolumeImperial() != null) {
                    if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                        measure = measure.add(lbp.getBookedVolumeImperial());
                    } else {
                        measure = measure.add(lbp.getBookedVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        }
        if (fromZip[0] != null) {
            if (pickupReadyDate != null && pickupReadyDate.toString() != null) {
                lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip[0],
                        toZip, pickupReadyDate.toString(), "" + weight, "" + measure, "CARRIER_CHARGE", "CARRIER_COST", "Exports");
            }
        }
        List<Carrier> carrierList = lclSession.getCarrierList();
        if (carrierList != null && carrierList.size() > 0) {
        } else {
            if (lclBookingPad != null) {
                lclBookingPad.getLclBookingAc().setArAmount(new BigDecimal(0.00));
                lclBookingPadDAO.update(lclBookingPad);
            }
        }
        lclBookingAcList = lclCostChargeDAO.findByProperty("lclFileNumber.id", longFileId);
        List lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
        Double totalWeight = 0.00;
        Double totalMeasure = 0.00;
        Double calculatedWeight = 0.00;
        Double calculatedMeasure = 0.00;
        String engmet = "";
        Ports ports = null;
        if (lclBooking.getFinalDestination() != null) {
            PortsDAO portsdao = new PortsDAO();
            ports = portsdao.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
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
                    if (lclCommodityForm.getModuleName().equalsIgnoreCase("Imports")) {
                        engmet = lclBookingAc.getRateUom();
                        if (engmet.equalsIgnoreCase("I")) {
                            engmet = "E";
                        }
                    }
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                                    weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                                } else {
                                    weightDouble = lclBookingPiece.getActualWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                }
                            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                                    weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                                } else {
                                    weightDouble = lclBookingPiece.getBookedWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                }
                            }
                            if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                                    weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                                } else {
                                    weightMeasure = lclBookingPiece.getActualVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                }
                            } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                                } else {
                                    weightMeasure = lclBookingPiece.getBookedVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                }
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
                if (engmet != null && lclSession.getSelectedMenu().equalsIgnoreCase("Exports")) {
                    if (engmet.equals("E")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                            calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                            calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                        } else {
                            calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                            calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                    } else if (engmet.equals("M")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                            calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                            calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                        } else {
                            calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                            calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                    }
                } else {
                    calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                        calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                    } else {
                        calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                    }
                }
                //end of else if engmet
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
                lclCostChargeDAO.saveOrUpdate(lclBookingAc);
            }
        }

        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, lclCommodityForm.getModuleName());
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", longFileId);
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
            if (lclCommodityList != null && lclCommodityList.size() > 0) {
                lclBookingPiece = lclCommodityList.get(0);
                if (lclBookingPiece.getStdchgRateBasis() != null && !lclBookingPiece.getStdchgRateBasis().trim().equals("")) {
                    request.setAttribute("stdchgratebasis", lclBookingPiece.getStdchgRateBasis());
                }
            }
        }
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = null;
        if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin())) {
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
            ofratebasis += lclBookingPiece.getCommodityType().getCode();
            if (lclBookingPiece.getStdchgRateBasis() != null && !lclBookingPiece.getStdchgRateBasis().trim().equals("")) {
                request.setAttribute("stdchgratebasis", lclBookingPiece.getStdchgRateBasis());
            }
            if (lclSession.getSelectedMenu().equals("Exports")) {
                request.setAttribute("ofratebasis", ofratebasis);
                lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
                lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "NO");
            } else {
                lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, ports);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), ports.getEngmet(), "");
            }
            request.setAttribute("origin", origin);
            request.setAttribute("destination", destination);
        }
        return mapping.findForward("chargeDesc");
    }

    public ActionForward calculateHazmatCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
            Boolean isHazmat = "Y".equalsIgnoreCase(request.getParameter("hazmat")) ? true : false;
            LclBookingPieceDAO bookingPieceDAO = new LclBookingPieceDAO();
            User loginUser = getCurrentUser(request);
            String fileId = request.getParameter("fileNumberId");
            String moduleName = request.getParameter("moduleName");
            Long longFileId = Long.parseLong(fileId);
            LclBookingPiece lclBookingPiece = bookingPieceDAO.findById(lclCommodityForm.getLclBookingPiece().getId());
            lclBookingPiece.setHazmat(isHazmat);
            bookingPieceDAO.saveOrUpdate(lclBookingPiece);
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBooking lclBooking = bookingDAO.findById(longFileId);
            List<LclBookingPiece> lclCommodityList = bookingPieceDAO.findByProperty("lclFileNumber.id", longFileId);
            if ("Imports".equalsIgnoreCase(moduleName)) {
                String pooCode = "", polCode = "", podCode = "", fdCode = "";
                pooCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
                polCode = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
                podCode = null != lclBooking.getPortOfDestination() ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
                fdCode = null != lclBooking.getFinalDestination() ? lclBooking.getFinalDestination().getUnLocationCode() : "";
                if (isHazmat) {
                    new LCLImportChargeCalc().calculateHazMatRates(pooCode, polCode, podCode, fdCode, lclCommodityForm.getTranshipment(),
                            lclCommodityForm.getBillingType(), lclBooking.getBillToParty(), lclCommodityForm.getImpCfsWarehsId(), longFileId, lclCommodityList, request, loginUser);
                } else {
                    new LclHazmatDAO().deleteHazmat(longFileId, lclCommodityForm.getLclBookingPiece().getId());
                    List<LclBookingAc> lclBookingAc = new ArrayList<LclBookingAc>();
                    String[] chargeCodeList = {"IPIHAZ", "HAZFEE"};
                    lclBookingAc = lclCostChargeDAO.HazadrousChargeValidate(longFileId, chargeCodeList, false);
                    for (LclBookingAc lba : lclBookingAc) {
                        String chargeCode = lba.getArglMapping().getChargeCode();
                        BigDecimal amount = lba.getArAmount();
                        lclCostChargeDAO.delete(lba);
                        LclRemarks lclRemarks = new LclRemarks();
                        lclRemarks.setLclFileNumber(lclBooking.getLclFileNumber());
                        lclRemarks.setEnteredBy(loginUser);
                        lclRemarks.setModifiedBy(loginUser);
                        lclRemarks.setModifiedDatetime(new Date());
                        lclRemarks.setEnteredDatetime(new Date());
                        lclRemarks.setRemarks("DELETED -> Charge Code -> " + chargeCode + " Charge Amount -> " + amount);
                        lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
                        new LclRemarksDAO().saveOrUpdate(lclRemarks);
                    }

                }
                String agentNo = "";
                if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                    agentNo = lclBooking.getAgentAcct().getAccountno();
                }
                List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, LclCommonConstant.LCL_IMPORT);
                lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList,
                        lclBooking.getBillingType(), "", agentNo);
            } else if (!"Imports".equalsIgnoreCase(moduleName)) {
                PortsDAO portsDAO = new PortsDAO();
                RefTerminalDAO refterminaldao = new RefTerminalDAO();
                String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
                String engmet = new String();
                RefTerminal refterminal = null;
                RefTerminal refterminalpol = null;
                Ports portspod = null;
                Ports ports = null;
                String rateType = lclBooking.getRateType();
                if (rateType.equalsIgnoreCase("R")) {
                    rateType = "Y";
                }
                if (lclBooking.getPortOfOrigin() != null && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationCode())) {
                    refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
                }
                if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                    pooorigin = refterminal.getTrmnum();
                }
                if (lclBooking.getPortOfLoading() != null && CommonUtils.isNotEmpty(lclBooking.getPortOfLoading().getUnLocationCode())) {
                    refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
                }
                if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                    polorigin = refterminalpol.getTrmnum();
                }
                if (lclBooking.getPortOfDestination() != null && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationCode())) {
                    portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
                }
                if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                    destinationpod = portspod.getEciportcode();
                }
                if (lclBooking.getFinalDestination() != null && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                    ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                }
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    engmet = ports.getEngmet();
                    destinationfd = ports.getEciportcode();
                }
                LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(longFileId, "HAZFEE", false);
                if (isHazmat && lclCommodityList != null && lclCommodityList.size() > 0) {
                    if (lclBookingAc == null) {
                        lclBookingAc = new LclBookingAc();
                        lclBookingAc.setEnteredBy(getCurrentUser(request));
                        lclBookingAc.setEnteredDatetime(new Date());
                        lclBookingAc.setTransDatetime(new Date());
                    }
                    lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
                    lclBookingAc.setModifiedBy(getCurrentUser(request));
                    lclBookingAc.setModifiedDatetime(new Date());
                    LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                    GlMappingDAO glmappingdao = new GlMappingDAO();
                    GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0119", "LCLE", "AR");
                    lclChargesCalculation.calculateHazmatChargeForRadio(pooorigin, polorigin, destinationfd, destinationpod, lclCommodityList, lclBookingPiece,
                            engmet, loginUser, longFileId, lclBookingAc, glmapping, request, lclBooking.getBillToParty());
                } else if (!isHazmat) {
                    //lclBookingPiece.setHazmat(false);
                    new LclHazmatDAO().deleteHazmat(longFileId, lclCommodityForm.getLclBookingPiece().getId());
                    if (null != lclBookingAc) {
                        lclCostChargeDAO.delete(lclBookingAc);
                        String chargeCode = lclBookingAc.getArglMapping().getChargeCode();
                        BigDecimal amount = lclBookingAc.getArAmount();
                        LclRemarks lclRemarks = new LclRemarks();
                        lclRemarks.setLclFileNumber(lclBooking.getLclFileNumber());
                        lclRemarks.setEnteredBy(loginUser);
                        lclRemarks.setModifiedBy(loginUser);
                        lclRemarks.setModifiedDatetime(new Date());
                        lclRemarks.setEnteredDatetime(new Date());
                        lclRemarks.setRemarks("DELETED -> Charge Code -> " + chargeCode + " Charge Amount -> " + amount);
                        lclRemarks.setType(REMARKS_DR_AUTO_NOTES);
                        new LclRemarksDAO().saveOrUpdate(lclRemarks);
                    }
                }
                List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(longFileId, lclCommodityForm.getModuleName());
                lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
                lclUtils.setRolledUpChargesForBooking(chargeList, request, longFileId, lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), engmet, "No");
                request.setAttribute("lclCommodityList", lclCommodityList);
            }
            lclBooking.setModifiedBy(loginUser);
            lclBooking.setModifiedDatetime(new Date());
            bookingDAO.saveOrUpdate(lclBooking);
        } catch (Exception e) {
            log.info("Error in LCL calculateHazmatCharge method. " + new Date(), e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public void setTraffiDetails(LclBookingPiece lclBookingPiece,
            HttpServletRequest request) throws Exception {
        String originAgentNo = request.getParameter("agentNo");
        if (originAgentNo != null && !"".equalsIgnoreCase(originAgentNo)) {
            commodityTypeDAO commodityTypeDAO = new commodityTypeDAO();
            String commodityDetails[] = commodityTypeDAO.defaultImportComm(originAgentNo);
            if (commodityDetails[0] != null && !commodityDetails[0].toString().trim().equals("")) {
                CommodityType commodityType = commodityTypeDAO.findById(Long.parseLong(commodityDetails[0].toString()));
                if (commodityType != null) {
                    lclBookingPiece.setCommodityType(commodityType);
                }
            }
        }
    }

    public ActionForward isCommodityValidation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        String fileId = "", tariffCode = "";
        String isCommodityFlag = lclBookingPieceDAO.isCommodityValidation(fileId, tariffCode);
        PrintWriter out = response.getWriter();
        out.print(isCommodityFlag);
        out.flush();
        out.close();
        return null;
    }

    public ActionForward addHotCodeTrackingComm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        String FORWARD_PAGE = "", refAddValue = "";
        String _3PartyName = request.getParameter("thirdPName");
        String refValue = request.getParameter("refValue");
        String fileNoId = request.getParameter("fileNumberId");
        Date now = new Date();
        if (fileNoId != null && !"".equals(fileNoId)) {
            Long fileId = Long.parseLong(fileNoId);
            String _3PRefType = "";
            Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            User loginUser = getCurrentUser(request);
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
            lclBooking.setModifiedDatetime(now);
            lclBookingDAO.saveOrUpdate(lclBooking);
            if (_3PartyName.equals("hotCodes")) {
                LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
                List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileId);
                if (lclBookingPieceList != null && lclCommodityForm.getGenCodefield1() != null
                        && lclCommodityForm.getGenCodefield1().equalsIgnoreCase("Y")) {
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
                    String refNo = lcl3pRefNoDAO.getReferenceSize(fileNoId, inbDesc);
                    List<LclInbond> lclInbondList = new LclInbondsDAO().findByProperty("lclFileNumber.id", fileId);
                    if (!lclInbondList.isEmpty() && refNo == null) {
                        refValue = inbDesc;
                    }
                } else {
                    refValue = lclCommodityForm.getHotCodes();
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "Inserted Hot Code#-->" + refValue.toUpperCase(), loginUser.getUserId());
                }
                String hotCodeXXXComments = request.getParameter("hotCodeXXXComments");
                if (CommonUtils.isNotEmpty(hotCodeXXXComments)) {
                    hotCodeXXXComments = "Added Hot Code XXX Comments-->" + hotCodeXXXComments;
                    LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", fileId);
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, hotCodeXXXComments.toUpperCase(), loginUser.getUserId());
                    if (null != bookingExport && null == bookingExport.getReleasedDatetime()) {
                        String desc = "Hold->Y, " + hotCodeXXXComments;
                        LclBooking booking = lclBookingDAO.getByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
                        lclBookingDAO.getCurrentSession().evict(booking);
                        booking.setHold("Y");
                        lclBookingDAO.saveOrUpdate(booking);
                        new LclRemarksDAO().insertLclRemarks(lclBooking.getLclFileNumber(), "OnHoldNotes", desc.toUpperCase(), loginUser);
                    }
                }
                String getAgentLevelBrand = new AgencyInfoDAO().getAgentLevelBrand(lclCommodityForm.getAgentNo(), lclCommodityForm.getPodId());
                String isEculineHotCode = CommonUtils.isNotEmpty(refValue) ? refValue.substring(0, refValue.indexOf("/")) : "";
                if (lclBookingHotCodeDAO.isHotCodeNotExist(refValue, fileNoId)) {
                    if ("EBL".equalsIgnoreCase(isEculineHotCode)) {
                        if (!"ECU".equalsIgnoreCase(lclCommodityForm.getBusinessUnit()) && "".equalsIgnoreCase(getAgentLevelBrand)) {
                            new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "Booking Changed Econo To Eculine", loginUser.getUserId());
                            new LclFileNumberDAO().updateEconoEculine("ECU", fileId.toString());
                        }
                        refValue = lclCommodityForm.getHotCodes();
                    }
                }
                if (CommonUtils.isNotEmpty(refValue)) {
                    lclBookingHotCodeDAO.saveHotCode(fileId, refValue.toUpperCase(), getCurrentUser(request).getUserId());
                }
                request.setAttribute("isHotCodeRemarks", new LclRemarksDAO().isRemarks(fileId, "ADDED HOT CODE XXX COMMENTS"));
                request.setAttribute("lclHotCodeList", new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", fileId));
                FORWARD_PAGE = "hoteCodes";
            } else {
                if (_3PartyName.equals("tracking")) {
                    refValue = lclCommodityForm.getTracking();
                    _3PRefType = _3PARTY_TYPE_TR;
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "Inserted Tracking#-->" + refValue.toUpperCase(), loginUser.getUserId());
                    FORWARD_PAGE = "tracking";
                }
                if (CommonUtils.isNotEmpty(refValue)) {
                    lcl3pRefNoDAO.save3pRefNo(fileId, _3PRefType, refValue.toUpperCase());
                }
                request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(fileId, _3PRefType));
            }
            request.setAttribute("lclBooking", lclBooking);
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public ActionForward deleteLcl3pReferenceComm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCommodityForm lclCommodityForm = (LclCommodityForm) form;
        String FORWARD_PAGE = "";
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String noteId = request.getParameter("thirdPName");
        String fileId = request.getParameter("fileNumberId");
        User loginUser = getCurrentUser(request);
        String remarksType = "", remarks = "";
        if (noteId.equals("hotCodes")) {
            LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
            LclBookingHotCode lclBookingHotCode = lclBookingHotCodeDAO.findById(lclCommodityForm.getLcl3pRefId());
            if (null != lclBookingHotCode) {
                String hotCodeXXXComments = request.getParameter("hotCodecomments");
                if (CommonUtils.isNotEmpty(hotCodeXXXComments)) {
                    hotCodeXXXComments = "Deleted Hot Code XXX Comments-->" + hotCodeXXXComments;
                    new LclRemarksDAO().insertLclRemarks(lclBookingHotCode.getLclFileNumber().getId(),
                            REMARKS_DR_AUTO_NOTES, hotCodeXXXComments.toUpperCase(), loginUser.getUserId());
                }
                remarks = "Deleted - HOT Code#" + " " + lclBookingHotCode.getCode();
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, remarks, loginUser.getUserId());
                remarksType = _3PARTY_TYPE_HTC;
                String isEculineHotCode = CommonUtils.isNotEmpty(lclBookingHotCode.getCode())
                        ? lclBookingHotCode.getCode().substring(0, lclBookingHotCode.getCode().indexOf("/")) : "";
                String getAgentLevelBrand = new AgencyInfoDAO().getAgentLevelBrand(lclCommodityForm.getAgentNo(), lclCommodityForm.getPodId());
                if ("EBL".equalsIgnoreCase(isEculineHotCode)
                        && !"BL".equalsIgnoreCase(lclBookingHotCode.getLclFileNumber().getState()) && "".equalsIgnoreCase(getAgentLevelBrand)) {
                    lclCommodityForm.setBusinessUnit(lclCommodityForm.getCompanyCode());
                    new LclDwr().updateEconoOREculine(lclCommodityForm.getCompanyCode(),
                            fileId, loginUser.getUserId().toString(), REMARKS_DR_AUTO_NOTES);
                }
                lclBookingHotCodeDAO.delete(lclBookingHotCode);
            }
            request.setAttribute("isHotCodeRemarks", new LclRemarksDAO().isRemarks(Long.parseLong(fileId),
                    "ADDED HOT CODE XXX COMMENTS"));
            request.setAttribute("lclHotCodeList", lclBookingHotCodeDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId)));
            FORWARD_PAGE = "hoteCodes";
        } else {
            Lcl3pRefNo lcl3pRefNo = lcl3pRefNoDAO.findById(lclCommodityForm.getLcl3pRefId());
            if (lcl3pRefNo != null) {
                if (noteId.equals("tracking")) {
                    remarks = "Deleted - Tracking#" + " " + lcl3pRefNo.getReference();
                    remarksType = _3PARTY_TYPE_TR;
                    FORWARD_PAGE = "tracking";
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

    public void setShortShipFileState(String shortShipFileNo, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(shortShipFileNo)) {
            String[] shortShipDetails = new LclFileNumberDAO().getFileNo(shortShipFileNo);
            request.setAttribute("shortShipOriginalState", shortShipDetails[0].toString());
        }
    }

    private void updateBarrelComboCommodityWareHouse(LclCommodityForm lclCommodityForm, Long bookingPieceId, String addOrUpdate, LclBookingPieceWhse lclBookingPieceWhse, Warehouse previousWareHouse) throws Exception {
        List<LclBookingPiece> lclPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCommodityForm.getFileNumberId());
        LclBookingPieceWhseDAO lclBookingPieceWhseDAO = new LclBookingPieceWhseDAO();
        Date now = new Date();
        for (LclBookingPiece lclBookingPiece : lclPieceList) {
            if (!CommonUtils.isEqual(bookingPieceId, lclBookingPiece.getId())) {
                if ("ADD".equalsIgnoreCase(addOrUpdate)) {
                    LclBookingPieceWhse lclBookingPieceWhseCopy = (LclBookingPieceWhse) BeanUtils.cloneBean(lclBookingPieceWhse);
                    lclBookingPieceWhseCopy.setLclBookingPiece(lclBookingPiece);
                    lclBookingPieceWhseCopy.setId(null);
                    lclBookingPieceWhseDAO.save(lclBookingPieceWhseCopy);
                } else {
                    if (null != previousWareHouse) {
                        LclBookingPieceWhse lclBookingPieceWhseCopy = lclBookingPieceWhseDAO.findByWhseAndCommodity(lclBookingPiece.getId(), previousWareHouse.getId());
                        if (null != lclBookingPieceWhseCopy) {
                            if (CommonUtils.isNotEmpty(lclCommodityForm.getCityLocation())) {
                                lclBookingPieceWhseCopy.setLocation(lclCommodityForm.getCityLocation());
                            } else {
                                lclBookingPieceWhseCopy.setLocation(null);
                            }
                            if (CommonUtils.isNotEmpty(lclCommodityForm.getWarehouseId())) {
                                lclBookingPieceWhseCopy.setWarehouse(new WarehouseDAO().findById(lclCommodityForm.getWarehouseId()));
                            } else {
                                lclBookingPieceWhseCopy.setWarehouse(null);
                            }
                            if ("Exports".equalsIgnoreCase(lclCommodityForm.getModuleName())) {
                                if (CommonUtils.isNotEmpty(lclCommodityForm.getStowedByUserId())) {
                                    lclBookingPieceWhseCopy.setStowedDatetime(now);
                                    lclBookingPieceWhseCopy.setStowedByUser(new UserDAO().findById(Integer.parseInt(lclCommodityForm.getStowedByUserId())));
                                } else {
                                    lclBookingPieceWhseCopy.setStowedDatetime(null);
                                    lclBookingPieceWhseCopy.setStowedByUser(null);
                                }
                            }
                            lclBookingPieceWhseCopy.setModifiedDatetime(now);
                            lclBookingPieceWhseCopy.setModifiedBy(lclBookingPieceWhse.getModifiedBy());
                            lclBookingPieceWhseDAO.saveOrUpdate(lclBookingPieceWhse);
                            lclBookingPieceWhseDAO.saveOrUpdate(lclBookingPieceWhseCopy);
                        } else {
                            LclBookingPieceWhse bookingPieceWhseCopy = (LclBookingPieceWhse) BeanUtils.cloneBean(lclBookingPieceWhse);
                            bookingPieceWhseCopy.setLclBookingPiece(lclBookingPiece);
                            bookingPieceWhseCopy.setId(null);
                            lclBookingPieceWhseDAO.save(bookingPieceWhseCopy);
                        }
                    }
                }
            }
        }
    }

    private void validateHold(LclCommodityForm lclCommodityForm, LclBookingPiece piece, LclBooking booking, User user) throws Exception {
        /* String desc = null;
         if (null != piece.getActualPieceCount() && null != piece.getBookedPieceCount() && piece.getBookedPieceCount() != 0 && !piece.getBookedPieceCount().equals(piece.getActualPieceCount())) {
         desc = "->Booked piece count not equal to Actual piece count";
         }
         BigDecimal ONE_HUNDRED = new BigDecimal(100);
         BigDecimal TEN = new BigDecimal(10);
         if (null != piece.getActualVolumeMetric() && null != piece.getBookedVolumeMetric()) {
         BigDecimal cubePercent = piece.getBookedVolumeMetric().multiply(TEN).divide(ONE_HUNDRED);
         cubePercent = piece.getBookedVolumeMetric().add(cubePercent);
         if (piece.getActualVolumeMetric().compareTo(cubePercent) >= 0) {
         desc = "->Booked cube 10% or more different than the Actual cube";
         }
         }
         if (null != piece.getActualWeightMetric() && null != piece.getBookedWeightMetric()) {
         BigDecimal weightPercent = piece.getBookedWeightMetric().multiply(TEN).divide(ONE_HUNDRED);
         weightPercent = piece.getBookedWeightMetric().add(weightPercent);
         if (piece.getActualWeightMetric().compareTo(weightPercent) >= 0) {
         desc = "->Booked weight 10% or more different than the Actual weight";
         }
         }
         if (null != desc) {
         desc = "Hold->Y, Comments" + desc + ", for commodity:" + piece.getCommName();
         booking.setHold("Y");
         new LclRemarksDAO().insertLclRemarks(booking.getLclFileNumber(), "OnHoldNotes", desc, user);
         saveFlag = true;
         }*/
        boolean saveFlag = false;
        if (lclCommodityForm.getOverShortdamaged().equalsIgnoreCase("Y")) {
            booking.setOverShortdamaged(true);
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            remarksDAO.updateRemarksByField(booking.getLclFileNumber(), REMARKS_TYPE_OSD, "OSD",
                    lclCommodityForm.getOsdRemarks(), user, REMARKS_DR_AUTO_NOTES);
            saveFlag = true;
        } else {
            booking.setOverShortdamaged(false);
            saveFlag = true;
        }
        if (saveFlag) {
            new LCLBookingDAO().update(booking);
        }
    }

    private void copyBookingPieceToBl(LclBookingPiece lbp) {
        try {
            LclBlPiece lblp = new LclBlPiece();
            PropertyUtils.copyProperties(lblp, lbp);
            lblp.setPackageType(null != lbp.getActualPackageType() ? lbp.getActualPackageType() : lbp.getPackageType());
            lblp.setId(null);
            new LclBLPieceDAO().save(lblp);
            lbp.setLclBookingPieceDetailList(new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", lbp.getId())); // for lazy initialize
            List<LclBookingPieceDetail> bookingPieceList = lbp.getLclBookingPieceDetailList();
            for (LclBookingPieceDetail bkgpiecedetail : bookingPieceList) {
                LclBlPieceDetail blpiecedetail = new LclBlPieceDetail();
                PropertyUtils.copyProperties(blpiecedetail, bkgpiecedetail);
                blpiecedetail.setId(null);
                blpiecedetail.setLclBlPiece(lblp);
                new LclBlPieceDetailDAO().saveOrUpdate(blpiecedetail);
            }
        } catch (Exception e) {
            //faied to add blpiece
        }
    }
}
