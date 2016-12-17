/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.google.gson.Gson;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.ImportQuoteUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclQuoteUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LCLQuoteImportChargeCalc;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclQuotationChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHotCode;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceWhse;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.QuoteCommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclQuoteCommodityForm;
import com.logiware.common.filter.JspWrapper;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Owner
 */
public class LclQuoteCommodityAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static String COMMODITY_DESC = "commodityDesc";
    private LclUtils lclUtils = new LclUtils();

    public ActionForward addQtCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType("application/json");
            LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
            User user = getCurrentUser(request);
            HttpSession session = request.getSession();
            Map<String, String> result = new HashMap<String, String>();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<LclQuotePiece> quoteCommodityList = (List<LclQuotePiece>) (null != lclSession.getQuoteCommodityList() ? lclSession.getQuoteCommodityList() : new ArrayList<LclQuotePiece>());
            LclQuotePiece lclQuotePiece = lclQuoteCommodityForm.getLclQuotePiece();
            boolean editDimFlag = false;
            boolean includeDestfees = lclQuoteCommodityForm.isIncludeDestfees();
            String dimFlag = request.getParameter("editDimFlag");
            if (request.getParameter("editDimFlag") != null && dimFlag.equals("true")) {
                editDimFlag = true;
            }
            if (lclQuotePiece == null) {
                lclQuotePiece = new LclQuotePiece();
            }
            if (lclQuotePiece.getEnteredBy() == null || lclQuotePiece.getEnteredDatetime() == null) {
                lclQuotePiece.setEnteredBy(user);
                lclQuotePiece.setEnteredDatetime(new Date());
            }
            if (editDimFlag && CommonUtils.isNotEmpty(quoteCommodityList)) {
                lclQuotePiece.setLclQuotePieceDetailList(quoteCommodityList.get(Integer.parseInt(request.getParameter("countVal"))).getLclQuotePieceDetailList());
                quoteCommodityList.set(Integer.parseInt(request.getParameter("countVal")), lclQuotePiece);
            } else {
                List<LclQuotePieceDetail> detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new ArrayList<LclQuotePieceDetail>();
                if (detailList != null) {
                    lclQuotePiece.setLclQuotePieceDetailList(detailList);
                }
                quoteCommodityList.add(lclQuotePiece);
            }
            lclQuotePiece.setModifiedBy(user);
            lclQuotePiece.setModifiedDatetime(new Date());
            lclSession.setQuoteCommodityList(quoteCommodityList);
            lclSession.setIncludeDestfees(includeDestfees);
            session.setAttribute("lclSession", lclSession);
            request.setAttribute("lclCommodityList", quoteCommodityList);
            JspWrapper jspWrapper = new JspWrapper(response);
            request.getRequestDispatcher("/jsps/LCL/commodityQuoteDesc.jsp").include(request, jspWrapper);
            result.put("commodityDesc", jspWrapper.getOutput());

            LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
            String rateType = lclQuoteCommodityForm.getRateType();
            if (lclQuoteCommodityForm.getRateType() != null && !lclQuoteCommodityForm.getRateType().trim().equals("")) {
                if (rateType.equalsIgnoreCase("R")) {
                    rateType = "Y";
                }
            }
            String fileId = request.getParameter("fileNumberId");
            Long fileNumberId = Long.parseLong(fileId);
            List<LclQuoteAc> lclQuoteAcList = null;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            if (CommonUtils.isEqual(lclQuoteCommodityForm.getModuleName(), "Imports")) {
                lclQuoteAcList = lclQuoteImportChargeCalc.ImportRateCalculation(lclQuoteCommodityForm.getOriginUnlocCode(), lclQuoteCommodityForm.getPolUnlocCode(), lclQuoteCommodityForm.getPodUnlocCode(), lclQuoteCommodityForm.getFdUnlocCode(),
                        lclQuoteCommodityForm.getTranshipment(), lclQuoteCommodityForm.getBillingType(), lclQuoteCommodityForm.getBillToParty(), fileNumberId, quoteCommodityList, user, request);
            } else {
                LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getDestinationName())
                        && !"UNKNOWN".equalsIgnoreCase(lclQuoteCommodityForm.getDestinationName())
                        && CommonUtils.isNotEmpty(lclQuoteCommodityForm.getOriginUnlocCode())) {
                    boolean calcHeavy = false;
                    if (!CommonUtils.isEmpty(lclQuoteCommodityForm.getCalcHeavy())
                            && lclQuoteCommodityForm.getCalcHeavy().equalsIgnoreCase("Y")) {
                        calcHeavy = true;
                    }
                    String deliveryMetro = request.getParameter("deliveryMetro");
                    String pcBoth = request.getParameter("pcBoth");
                    quoteCommodityList = lclSession.getQuoteCommodityList();
                    lclQuotationChargesCalculation.calculateRates(lclQuoteCommodityForm.getOriginUnlocCode(), lclQuoteCommodityForm.getFdUnlocCode(), lclQuoteCommodityForm.getPolUnlocCode(),
                            lclQuoteCommodityForm.getPodUnlocCode(), fileNumberId, quoteCommodityList, user, null, null, null, rateType,
                            "C", null, null, null, null, calcHeavy, deliveryMetro, pcBoth, null, null, request);
                    exportQuoteUtils.setRolledUpChargesForQuote(lclQuotationChargesCalculation.getQuoteAcList(), request, fileNumberId, null, quoteCommodityList, pcBoth, lclQuotationChargesCalculation.getPorts().getEngmet(), "No");
                    lclQuoteAcList = (List<LclQuoteAc>) request.getAttribute("chargeList");
                    exportQuoteUtils.setWeighMeasureForQuote(request, quoteCommodityList, lclQuotationChargesCalculation.getPorts().getEngmet());
                }
            }
            request.setAttribute("totalCharges", exportQuoteUtils.calculateTotalByQuoteAcList(lclQuoteAcList));
            lclSession.setQuoteAcList(lclQuoteAcList);
            lclSession.setQuoteCommodityList(quoteCommodityList);
            session.setAttribute("lclSession", lclSession);

            request.setAttribute("chargeList", lclQuoteAcList);
            jspWrapper = new JspWrapper(response);
            request.getRequestDispatcher("/jsps/LCL/ajaxload/quoteChargeDesc.jsp").include(request, jspWrapper);
            result.put("chargeDesc", jspWrapper.getOutput());
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

    public ActionForward saveOrUpdateCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
            LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
            Date d = new Date();
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
            LclQuoteHazmatDAO lclQuoteHazmatDAO = new LclQuoteHazmatDAO();
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclQuotePieceWhseDAO lclQuotePieceWhseDAO = new LclQuotePieceWhseDAO();
            QuoteCommodityDetailsDAO quoteCommodityDetailsDAO = new QuoteCommodityDetailsDAO();
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            LclQuote lclQuote = lclQuoteDAO.getByProperty("lclFileNumber.id", lclQuoteCommodityForm.getFileNumberId());
//            LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclQuoteCommodityForm.getFileNumberId());
            out = response.getWriter();
            User loginUser = getCurrentUser(request);
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<String, String>();

            if (request.getParameter("hazmat").equals("Y")) {
                lclQuoteCommodityForm.getLclQuotePiece().setHazmat(true);
            } else {
                lclQuoteCommodityForm.getLclQuotePiece().setHazmat(false);
            }
            LclQuotePiece lclQuotePiece = lclQuoteCommodityForm.getLclQuotePiece();
            if (lclQuotePiece == null) {
                lclQuotePiece = new LclQuotePiece();
            }
            if (lclQuotePiece.getEnteredBy() == null || lclQuotePiece.getEnteredDatetime() == null) {
                lclQuotePiece.setEnteredBy(loginUser);
                lclQuotePiece.setEnteredDatetime(new Date());
            }
            lclQuotePiece.setModifiedBy(loginUser);
            lclQuotePiece.setModifiedDatetime(new Date());
            lclQuotePieceDAO.saveAndReturn(lclQuotePiece);
            //save the lcl_booking_piece_details
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<LclQuotePieceDetail> dimList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
           if (lclQuoteCommodityForm.getModuleName().equalsIgnoreCase("Exports")) {
            List<LclQuotePieceDetail> pieceDetailList = quoteCommodityDetailsDAO.findByProperty("quotePiece.id",lclQuotePiece.getId());
                if(pieceDetailList.isEmpty() && CommonUtils.isNotEmpty(dimList)){
                    for (LclQuotePieceDetail detail : dimList) {
                        detail.setQuotePiece(lclQuotePiece);
                        quoteCommodityDetailsDAO.save(detail);
                    }
                }
            } else if (CommonUtils.isNotEmpty(dimList)) {//quote piece id is empty
                for (LclQuotePieceDetail detail : dimList) {
                    detail.setQuotePiece(lclQuotePiece);
                    quoteCommodityDetailsDAO.save(detail);
                }
            }
            QuoteCommodityDetailsDAO commodityDetailsDAO = new QuoteCommodityDetailsDAO();
            List<LclQuotePiece> lclQuotePieceList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", lclQuoteCommodityForm.getFileNumberId());
            for (LclQuotePiece lbp : lclQuotePieceList) {
                /*no need for this method ocnfrt charges amt is empty
                 if (CommonUtils.isEqual(lclQuoteCommodityForm.getModuleName(), "Exports") && lclQuoteCommodityForm.isRatesRecalFlag()) {

                 new LclQuotationChargesCalculation().calculateOfRateForCommodity(lclQuoteCommodityForm.getOriginUnlocCode(), lclQuoteCommodityForm.getFdUnlocCode(),
                 lclQuoteCommodityForm.getPolUnlocCode(), lclQuoteCommodityForm.getPodUnlocCode(),
                 lclQuoteCommodityForm.getRateType(), lclQuoteCommodityForm.getFileNumberId(),
                 lbp, getCurrentUser(request));
                 }

                 */
                lbp.setLclQuoteHazmatList(lclQuoteHazmatDAO.findByFileAndCommodityList(lclQuoteCommodityForm.getFileNumberId(), lbp.getId()));
                lbp.setLclQuoteAcList(lclQuoteAcDAO.findByFileAndCommodityList(lclQuoteCommodityForm.getFileNumberId(), lbp.getId()));
                lbp.setLclQuotePieceWhseList(lclQuotePieceWhseDAO.findByProperty("lclQuotePiece.id", lbp.getId()));
                lbp.setLclQuotePieceDetailList(commodityDetailsDAO.findByProperty("quotePiece.id", lbp.getId()));
            }
            if ("Exports".equalsIgnoreCase(lclQuoteCommodityForm.getModuleName())) {
                if (null != lclQuoteCommodityForm.getOverShortdamaged()) {
                    if (lclQuoteCommodityForm.getOverShortdamaged().equals("Y")) {
                        lclQuote.setOverShortDamaged(true);
                        LclRemarksDAO remarksDAO = new LclRemarksDAO();
                        remarksDAO.updateRemarksByField(lclQuote.getLclFileNumber(), "OSD", "OSD",
                                lclQuoteCommodityForm.getOsdRemarks(), loginUser, REMARKS_QT_AUTO_NOTES);
                    } else if (lclQuoteCommodityForm.getOverShortdamaged().equals("N")) {
                        lclQuote.setOverShortDamaged(false);
                    }
                }
                if (lclQuoteCommodityForm.getUps()) {
                    if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getSmallParcelRemarks())) {
                        new LclRemarksDAO().insertLclRemarks(lclQuote.getFileNumberId(), REMARKS_QT_AUTO_NOTES,
                                lclQuoteCommodityForm.getSmallParcelRemarks(), loginUser.getUserId());
                        lclQuoteCommodityForm.setSmallParcelRemarks("");
                    }
                }

                if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getLabelField())) {
                    new LclRemarksDAO().insertLclRemarks(lclQuoteCommodityForm.getFileNumberId(), REMARKS_QT_AUTO_NOTES, lclQuoteCommodityForm.getLabelField() + " Labels Printed", loginUser.getUserId());
                    lclUtils.setMailTransactionsDetails("LclBooking", "Label Print", loginUser, lclQuoteCommodityForm.getLabelField(), "Pending", new Date(), lclQuoteCommodityForm.getFileNumber(), lclQuoteCommodityForm.getFileNumberId());
                }
                LclBookingExport lclBookingExport = null;
                Warehouse wareHouse = null;
                lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclQuoteCommodityForm.getFileNumberId());
                if (null == lclBookingExport) {
                    lclBookingExport = new LclBookingExport();
                    lclBookingExport.setEnteredBy(getCurrentUser(request));
                    lclBookingExport.setEnteredDatetime(d);
                }
                if (null != lclQuote.getPortOfOrigin() || null != lclQuote.getPortOfDestination()) {
                    RefTerminal terminal = null;
                    if (null != lclQuote.getPortOfOrigin()) {
                        terminal = new RefTerminalDAO().getTerminalByUnLocation(lclQuote.getPortOfOrigin().getUnLocationCode(), "Y");
                    } else {
                        terminal = new RefTerminalDAO().getTerminalByUnLocation(lclQuote.getPortOfDestination().getUnLocationCode(), "Y");
                    }
                    wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
                }
                lclBookingExport.setFileNumberId(lclQuoteCommodityForm.getFileNumberId());
                lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
                lclBookingExport.setRtAgentAcct(null);
                lclBookingExport.setDeliverPickup("P");
                lclBookingExport.setDeliverPickupDatetime(d);
                boolean valComm = bookingDAO.isvalOfcom(lclQuoteCommodityForm.getCommodityNo());
                boolean oldValComm = bookingDAO.isvalOfcom(lclQuoteCommodityForm.getOldTariffNo());
                if (valComm || oldValComm) {
                    lclBookingExport.setIncludeDestfees(lclQuoteCommodityForm.isIncludeDestfees());
                    lclSession.setIncludeDestfees(lclQuoteCommodityForm.isIncludeDestfees());
                }
                lclBookingExport.setReleaseUser(getCurrentUser(request));
                lclBookingExport.setModifiedBy(getCurrentUser(request));
                lclBookingExport.setModifiedDatetime(d);
                lclBookingExport.setAes(false);
                lclBookingExport.setUps(lclQuoteCommodityForm.getUps());
                new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
                new LCLBookingDAO().updateModifiedDateTime(lclQuoteCommodityForm.getFileNumberId(), loginUser.getUserId());
            }
            request.setAttribute("lclCommodityList", lclQuotePieceList);
            request.setAttribute("ofspotrate", lclQuote.getSpotRate());
            request.setAttribute("lclQuote", lclQuote);
            JspWrapper jspWrapper = new JspWrapper(response);
            request.getRequestDispatcher("/jsps/LCL/commodityQuoteDesc.jsp").include(request, jspWrapper);
            result.put("commodityDesc", jspWrapper.getOutput());

            if (lclQuoteCommodityForm.isRatesRecalFlag()) {
                if (CommonUtils.isEqual(lclQuoteCommodityForm.getModuleName(), "Imports")) {
                    ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
                    LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
                    lclQuoteImportChargeCalc.ImportRateCalculation(lclQuoteCommodityForm.getOriginUnlocCode(), lclQuoteCommodityForm.getPolUnlocCode(),
                            lclQuoteCommodityForm.getPodUnlocCode(), lclQuoteCommodityForm.getFdUnlocCode(),
                            lclQuoteCommodityForm.getTranshipment(), lclQuoteCommodityForm.getBillingType(), lclQuoteCommodityForm.getBillToParty(),
                            lclQuoteCommodityForm.getFileNumberId(), lclQuotePieceList, loginUser, request);
                    List<LclQuoteAc> lclQuoteAcList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(lclQuoteCommodityForm.getFileNumberId(), lclQuoteCommodityForm.getModuleName());
                    importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePieceList);
                    importQuoteUtils.setImpRolledUpChargesForQuote(lclQuoteAcList, request, lclQuoteCommodityForm.getFileNumberId(),
                            lclQuotePieceList, lclQuoteCommodityForm.getBillingType());
                    request.setAttribute("chargeList", lclQuoteAcList);
                } else {
                    if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getDestinationName()) && !"UNKNOWN".equalsIgnoreCase(lclQuoteCommodityForm.getDestinationName())) {
                        String rateType = "R".equalsIgnoreCase(lclQuoteCommodityForm.getRateType()) ? "Y" : lclQuoteCommodityForm.getRateType();
                        boolean calcHeavy = false;
                        if (!CommonUtils.isEmpty(lclQuoteCommodityForm.getCalcHeavy()) && lclQuoteCommodityForm.getCalcHeavy().equalsIgnoreCase("Y")) {
                            calcHeavy = true;
                        }
                        String deliveryMetro = request.getParameter("deliveryMetro");
                        String pcBoth = request.getParameter("pcBoth");
                        lclQuotationChargesCalculation.calculateRates(lclQuoteCommodityForm.getOriginUnlocCode(), lclQuoteCommodityForm.getFdUnlocCode(),
                                lclQuoteCommodityForm.getPolUnlocCode(), lclQuoteCommodityForm.getPodUnlocCode(),
                                lclQuoteCommodityForm.getFileNumberId(), lclQuotePieceList, loginUser, null, null, null, rateType,
                                "C", null, null, null, null, calcHeavy, deliveryMetro, pcBoth, null, null, request);
                        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
                        if (lclQuote.getSpotRate() && lclQuotePieceList.size() == 1) {
                            String billingType = lclQuote.getBillingType();
                            String CFT = null != lclQuote.getSpotWmRate() ? lclQuote.getSpotWmRate().toString() : "";
                            String CBM = null != lclQuote.getSpotRateMeasure() ? lclQuote.getSpotRateMeasure().toString() : "";
                            Boolean spotCheckBottom = lclQuote.isSpotRateBottom();
                            Boolean isOnlyOcnfrt = lclQuote.isSpotOfRate();
                            String spotComment = lclQuote.getSpotComment();
                            MessageResources messageResources = getResources(request);
                            String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                            exportQuoteUtils.calculateSpotRate(lclQuoteCommodityForm.getFileNumberId(), lclQuote, billingType,
                                    CBM, CFT, isOnlyOcnfrt, spotCheckBottom, spotComment, spotRateCommodity, request, lclQuotePieceList);
                        }
                        List<LclQuoteAc> lclQuoteAcList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(lclQuoteCommodityForm.getFileNumberId(), lclQuoteCommodityForm.getModuleName());
                        exportQuoteUtils.setRolledUpChargesForQuote(lclQuoteAcList, request, lclQuoteCommodityForm.getFileNumberId(),
                                new LclQuoteAcDAO(), lclQuotePieceList, pcBoth, lclQuotationChargesCalculation.getPorts().getEngmet(), "No");
                        exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePieceList, lclQuotationChargesCalculation.getPorts().getEngmet());
                    }
                }

                request.setAttribute("lclQuote", lclQuote);

                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/quoteChargeDesc.jsp").include(request, jspWrapper);
                result.put("chargeDesc", jspWrapper.getOutput());
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

    public ActionForward displayTransitTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        String ofimpFlag = lclQuoteAcDAO.isChargeCodeValidate(String.valueOf(lclQuoteCommodityForm.getFileNumberId()), "OFIMP", "");
        if ("true".equalsIgnoreCase(ofimpFlag)) {
            request.setAttribute("transitTime", lclQuoteAcDAO.getTransitTime(lclQuoteCommodityForm.getPolUnlocCode(),
                    lclQuoteCommodityForm.getPodUnlocCode(), lclQuoteCommodityForm.getCommodityNo()));
        }
        return mapping.findForward("transitTime");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        String fileNumberId = request.getParameter("fileNumberId");
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setQuoteDetailList(null);
        session.setAttribute("lclSession", lclSession);
        String cfcl = request.getParameter("cfcl");
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclQuoteCommodityForm.getModuleName())) {
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
            LclQuotePiece lclQuotePiece = new LclQuotePiece();
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                CommodityType commodityType = new commodityTypeDAO().getByProperty("code", commodity[0]);
                lclQuotePiece.setCommodityType(commodityType);
            }
            request.setAttribute("lclQuotePiece", lclQuotePiece);
        } else {
            CommodityType commodityType = null;
            LclQuotePiece lclQuotePiece = new LclQuotePiece();
            String commodity = request.getParameter("expCommodityNo");
            String rateType = "R".equalsIgnoreCase(lclQuoteCommodityForm.getRateType()) ? "Y" : lclQuoteCommodityForm.getRateType();
            if (CommonUtils.isNotEmpty(commodity)) {
                commodityType = new commodityTypeDAO().getByProperty("code", commodity);
            }
            if (commodityType == null && "C".equalsIgnoreCase(rateType)) {
                commodityType = new commodityTypeDAO().getByProperty("code", "032500");
            }
            if (commodityType != null) {
                lclQuotePiece.setCommodityType(commodityType);
                lclQuotePiece.setPieceDesc(commodityType.getDescEn());
            }
            request.setAttribute("lclQuotePiece", lclQuotePiece);
            String dojocount = setTrmNumAndEciportCode(lclQuoteCommodityForm, rateType);
            request.setAttribute("dojoCount", dojocount);
            request.setAttribute("cfcl", cfcl);
            if (CommonUtils.isNotEmpty(fileNumberId)) {
                LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
                request.setAttribute("bookingExport", bookingExport);
            }
        }
        if (lclQuoteCommodityForm.getCommodityTypeId() == null) {
            PackageType packageType = new PackageTypeDAO().findByDesc("Package");
            if (packageType != null) {
                lclQuoteCommodityForm.setPackageType(packageType.getType());
                lclQuoteCommodityForm.setPackageTypeId(packageType.getId());
            }
        }
        fileNumberId = fileNumberId == null ? "" : fileNumberId;
        String[] data = new LclDwr().displayQuoteDimsDetails(lclQuoteCommodityForm.getQuotePieceId(), request.getParameter("fileNumberId"), "", request);
        lclQuoteCommodityForm.setDimsToolTip(data[0]);
        request.setAttribute("editDimFlag", request.getParameter("editDimFlag"));
        request.setAttribute("lclQuoteCommodityForm", lclQuoteCommodityForm);
        lclQuoteCommodityForm.setQuotePieceId("");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        if (request.getParameter("id") != null && !request.getParameter("id").trim().equals("")) {
            int index = Integer.parseInt(request.getParameter("id"));
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            List<LclQuotePiece> quoteCommodityList = (List<LclQuotePiece>) (null != lclSession.getQuoteCommodityList() ? lclSession.getQuoteCommodityList() : new ArrayList<LclQuotePiece>());
            quoteCommodityList.remove(index);
            lclSession.setQuoteCommodityList(quoteCommodityList);
            session.setAttribute("lclSession", lclSession);
            request.setAttribute("lclCommodityList", quoteCommodityList);
        }
        request.setAttribute("lclQuoteCommodityForm", lclQuoteCommodityForm);
        return mapping.findForward(COMMODITY_DESC);
    }

    public ActionForward displayWhse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        Long quotePieceId = Long.parseLong(lclQuoteCommodityForm.getQuotePieceId());
        List detailList = null;
        List<LclQuotePieceWhse> whseList = null;
        if (CommonUtils.isNotEmpty(quotePieceId)) {
            detailList = new QuoteCommodityDetailsDAO().findDetailProperty("quotePiece.id", quotePieceId);
            whseList = new LclQuotePieceWhseDAO().findByCommodityList(quotePieceId);
        }
        if (CommonUtils.isNotEmpty(detailList)) {
            LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(0);
            request.setAttribute("actualUom", detail.getActualUom());
        }
        request.setAttribute("detailList", detailList);
        request.setAttribute("whseList", whseList);
        request.setAttribute("lclQuoteCommodityForm", lclQuoteCommodityForm);
        return mapping.findForward("whseDetail");
    }

    public ActionForward closeDetail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        List<LclQuotePiece> lclPieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", new Long(fileNumberId));
        for (LclQuotePiece lbp : lclPieceList) {
            lbp.setLclQuoteHazmatList(new LclQuoteHazmatDAO().findByFileAndCommodityList(new Long(fileNumberId), lbp.getId()));
            lbp.setLclQuoteAcList(new LclQuoteAcDAO().findByFileAndCommodityList(new Long(fileNumberId), lbp.getId()));
            lbp.setLclQuotePieceWhseList(new LclQuotePieceWhseDAO().findByFileAndCommodityList(lbp.getId()));
        }
        request.setAttribute("lclCommodityList", lclPieceList);
        return mapping.findForward("commodityDesc");
    }

    public ActionForward addWhseDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        Long qtPieceId = Long.parseLong(lclQuoteCommodityForm.getQuotePieceId());
        Warehouse warehouse = null;
        Date now = new Date();
        User loginUser = getCurrentUser(request);
        List detailList = null;
        List<LclQuotePieceWhse> whseList = null;
        LclQuotePieceWhse lclQuotePieceWhse = null;
        LclQuotePieceWhseDAO lclQuotePieceWhseDAO = new LclQuotePieceWhseDAO();
        if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getWarehouseId())) {
            warehouse = new WarehouseDAO().findById(lclQuoteCommodityForm.getWarehouseId());
        }
        if (lclQuoteCommodityForm.getQuotewhseId() != null && !lclQuoteCommodityForm.getQuotewhseId().equals("")) {
            lclQuotePieceWhse = lclQuotePieceWhseDAO.findById(Long.parseLong(lclQuoteCommodityForm.getQuotewhseId()));
        }
        if (lclQuotePieceWhse == null) {
            lclQuotePieceWhse = new LclQuotePieceWhse();
            lclQuotePieceWhse.setEnteredBy(loginUser);
            lclQuotePieceWhse.setEnteredDatetime(now);

        }
        lclQuotePieceWhse.setLocation(CommonUtils.isNotEmpty(lclQuoteCommodityForm.getCityLocation()) ? lclQuoteCommodityForm.getCityLocation() : null);
        if (CommonUtils.isNotEmpty(qtPieceId)) {
            lclQuotePieceWhse.setLclQuotePiece(lclQuoteCommodityForm.getLclQuotePiece());
            lclQuotePieceWhse.setWarehouse(warehouse);
            lclQuotePieceWhse.setModifiedDatetime(now);
            lclQuotePieceWhse.setModifiedBy(loginUser);
            lclQuotePieceWhseDAO.saveOrUpdate(lclQuotePieceWhse);
            detailList = new QuoteCommodityDetailsDAO().findDetailProperty("quotePiece.id", qtPieceId);
            whseList = lclQuotePieceWhseDAO.findByCommodityList(qtPieceId);
        }
        request.setAttribute("whseList", whseList);
        request.setAttribute("detailList", detailList);
        return mapping.findForward("whseDetail");
    }

    public ActionForward displayTariffDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        commodityTypeDAO commodityDAO = new commodityTypeDAO();
        int totalPageSize = 0;
        if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getTariff())) {
            if (lclQuoteCommodityForm.getOriginNo() != null && !lclQuoteCommodityForm.getOriginNo().equals("")) {
                totalPageSize += commodityDAO.getCommodityCount(lclQuoteCommodityForm.getTariff(), lclQuoteCommodityForm.getOriginNo(), lclQuoteCommodityForm.getDestinationNo());
            } else {
                totalPageSize += commodityDAO.getCommodityCount(lclQuoteCommodityForm.getTariff(), lclQuoteCommodityForm.getOriginNo(), lclQuoteCommodityForm.getDestinationNo());
            }
        } else {
            if (lclQuoteCommodityForm.getOriginNo() != null && !lclQuoteCommodityForm.getOriginNo().equals("")) {
                totalPageSize += commodityDAO.getTotalCommodity(lclQuoteCommodityForm.getOriginNo(), lclQuoteCommodityForm.getDestinationNo());
            } else {
                totalPageSize += commodityDAO.getTotalCommodity(lclQuoteCommodityForm.getOriginNo(), lclQuoteCommodityForm.getDestinationNo());
            }
        }
        int noOfPages = totalPageSize / lclQuoteCommodityForm.getCurrentPageSize();
        int remainSize = totalPageSize % lclQuoteCommodityForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages += 1;
        }
        int start = (lclQuoteCommodityForm.getCurrentPageSize() * (lclQuoteCommodityForm.getPageNo() - 1));
        int end = lclQuoteCommodityForm.getCurrentPageSize();
        lclQuoteCommodityForm.setNoOfPages(noOfPages);
        lclQuoteCommodityForm.setTotalPageSize(totalPageSize);
        List<CommodityType> commodityTypeList = null;
        if (lclQuoteCommodityForm.getOriginNo() != null && !lclQuoteCommodityForm.getOriginNo().equals("")) {
            commodityTypeList = new commodityTypeDAO().findAllCommodityTypeList(lclQuoteCommodityForm.getTariff(), lclQuoteCommodityForm.getOriginNo(), lclQuoteCommodityForm.getDestinationNo(), start, end);
        } else {
            commodityTypeList = new commodityTypeDAO().findAllCommodityTypeList(lclQuoteCommodityForm.getTariff(), lclQuoteCommodityForm.getOriginNo(), lclQuoteCommodityForm.getDestinationNo(), start, end);
        }
        request.setAttribute("commodityTypeList", commodityTypeList);
        request.setAttribute("tariff", lclQuoteCommodityForm.getTariff());
        return mapping.findForward("tariff");
    }

    public ActionForward editLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession");
        LclQuotePiece lclQuotePiece = null;
        LclBookingExport bookingExport = null;
        String cfcl = request.getParameter("cfcl");
        if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getFileNumberId())) {
            bookingExport = new LclBookingExportDAO()
                    .getByProperty("lclFileNumber.id", lclQuoteCommodityForm.getFileNumberId());
            lclQuotePiece = lclQuoteCommodityForm.getLclQuotePiece();
            if (lclQuotePiece.getBookedPieceCount() <= 1) {
                lclQuoteCommodityForm.setPackageType(lclQuotePiece.getPackageType().getDescription());
            } else {
                lclQuoteCommodityForm.setPackageType(lclQuotePiece.getPackageType().getDescription() + "" + lclQuotePiece.getPackageType().getPlural().toLowerCase());
            }
            String[] data = new LclDwr().displayQuoteDimsDetails(lclQuoteCommodityForm.getQuotePieceId(), request.getParameter("fileNumberId"), "", request);
            lclQuoteCommodityForm.setDimsToolTip(data[0]);
        } else {
            if (lclSession != null && lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0) {
                lclQuotePiece = lclSession.getQuoteCommodityList().get(Integer.parseInt(request.getParameter("countVal")));
                if (CommonUtils.isNotEmpty(request.getParameter("copyVal")) && !request.getParameter("copyVal").equals("Y")) {
                    lclQuotePiece.getCommodityType().setDescEn(lclQuotePiece.getCommName());
                    lclQuotePiece.getCommodityType().setCode(lclQuotePiece.getCommNo());
                    request.setAttribute("id", request.getParameter("id"));
                    request.setAttribute("commCode", lclQuotePiece.getCommNo());//remove
                    lclQuoteCommodityForm.setPackageType(lclQuotePiece.getPkgName());
                } else {
                    lclQuotePiece.getCommodityType().setDescEn(lclQuotePiece.getCommName());
                    lclQuotePiece.getCommodityType().setCode(lclQuotePiece.getCommNo());
                    lclQuoteCommodityForm.setPackageType(lclQuotePiece.getPkgName());
                    lclQuoteCommodityForm.setPackageTypeId(lclQuotePiece.getPkgNo());
                }
                String[] data = new LclDwr().displayQuoteDimsDetails("", "", lclQuotePiece.getCommNo(), request);
                lclQuoteCommodityForm.setDimsToolTip(data[0]);
            }
        }
        request.setAttribute("countVal", request.getParameter("countVal"));//this for session editting values
        if (lclQuotePiece != null) {
            request.setAttribute("totalWeightImp", lclQuotePiece.getBookedWeightImperial());
            request.setAttribute("totalWeightMet", lclQuotePiece.getBookedWeightMetric());
            request.setAttribute("totalMeasureImp", lclQuotePiece.getBookedVolumeImperial());
            request.setAttribute("totalMeasureMet", lclQuotePiece.getBookedVolumeMetric());
        }
        if ("Exports".equalsIgnoreCase(lclQuoteCommodityForm.getModuleName())) {
            String rateType = "R".equalsIgnoreCase(lclQuoteCommodityForm.getRateType()) ? "Y" : lclQuoteCommodityForm.getRateType();
            String dojocount = setTrmNumAndEciportCode(lclQuoteCommodityForm, rateType);
            request.setAttribute("dojoCount", dojocount);
            request.setAttribute("cfcl", cfcl);
            if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getFileNumberId())) {
                request.setAttribute("lcl3PList", new Lcl3pRefNoDAO().get3PRefList(lclQuoteCommodityForm.getFileNumberId(), _3PARTY_TYPE_TR));
                request.setAttribute("lclHotCodeList", new LclQuoteHotCodeDAO().getHotCodeList(lclQuoteCommodityForm.getFileNumberId()));
            }
        }
        request.setAttribute("editDimFlag", request.getParameter("editDimFlag"));
        request.setAttribute("bookingExport", bookingExport);
        request.setAttribute("lclQuoteCommodityForm", lclQuoteCommodityForm);
        request.setAttribute("lclQuotePiece", lclQuotePiece);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward calculateHazmatCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date now = new Date();
        try {
            LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
            User loginUser = getCurrentUser(request);
            Long fileId = Long.parseLong(request.getParameter("fileNumberId"));
            String moduleName = request.getParameter("moduleName");
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LCLQuoteDAO lCLQuoteDAO = new LCLQuoteDAO();
            ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
            LclQuote lclQuote = lCLQuoteDAO.findById(fileId);
            LclQuotePiece lclQuotePiece = lclQuotePieceDAO.findById(lclQuoteCommodityForm.getLclQuotePiece().getId());
            List<LclQuotePiece> lclCommodityList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", fileId);
            String hazmat = request.getParameter("hazmat");
            String pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
            String pooUnloCode = lclQuote.getPortOfOrigin() != null ? lclQuote.getPortOfOrigin().getUnLocationCode() : "";
            String polUnloCode = lclQuote.getPortOfLoading() != null ? lclQuote.getPortOfLoading().getUnLocationCode() : "";
            String podUnloCode = lclQuote.getPortOfDestination() != null ? lclQuote.getPortOfDestination().getUnLocationCode() : "";
            String fdUnloCode = lclQuote.getFinalDestination() != null ? lclQuote.getFinalDestination().getUnLocationCode() : "";

            if ("Imports".equalsIgnoreCase(moduleName)) {
                if ("Y".equalsIgnoreCase(hazmat) && lclCommodityList != null && lclCommodityList.size() > 0) {
                    new LCLQuoteImportChargeCalc().calculateHazMatRates(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, lclQuoteCommodityForm.getTranshipment(),
                            lclQuoteCommodityForm.getBillingType(), lclQuote.getBillToParty(), fileId, lclCommodityList, request, loginUser);
                } else if ("N".equalsIgnoreCase(hazmat)) {
                    List<LclQuoteAc> lclQuoteAc = new ArrayList<LclQuoteAc>();
                    lclQuotePiece.setHazmat(false);
                    String[] chargeCodeList = {"IPIHAZ", "HAZFEE"};
                    lclQuoteAc = lclQuoteAcDAO.HazadrousChargeValidate(fileId, chargeCodeList, false);
                    for (LclQuoteAc lqa : lclQuoteAc) {
                        lclQuoteAcDAO.delete(lqa);
                        String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lqa);
                        new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                    }
                }
                List<LclQuoteAc> lclQuoteAcList = new LclQuoteAcDAO().getLclCostByFileNumberAsc(lclQuoteCommodityForm.getFileNumberId(), lclQuoteCommodityForm.getModuleName());
                importQuoteUtils.setWeighMeasureForImpQuote(request, lclCommodityList);
                importQuoteUtils.setImpRolledUpChargesForQuote(lclQuoteAcList, request, lclQuoteCommodityForm.getFileNumberId(),
                        lclCommodityList, lclQuoteCommodityForm.getBillingType());
                request.setAttribute("chargeList", lclQuoteAcList);
            } else if (!"Imports".equalsIgnoreCase(moduleName)) {
                LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileId, HAZMET_CHARGE_CODE, false);
                if ("Y".equalsIgnoreCase(hazmat) && lclCommodityList != null && lclCommodityList.size() > 0) {

                    String rateType = "R".equalsIgnoreCase(lclQuote.getRateType()) ? "Y" : lclQuote.getRateType();
                    List l = new LclUtils().getTrmNumandEciPortCode(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, rateType);
                    for (Object row : l) {
                        Object[] col = (Object[]) row;
                        if (col[2].toString().equalsIgnoreCase("POO")) {
                            pooTrmnum = (String) col[0];
                        }
                        if (col[2].toString().equalsIgnoreCase("POL")) {
                            polTrmnum = (String) col[0];
                        }
                        if (col[2].toString().equalsIgnoreCase("POD")) {
                            podEciPortCode = (String) col[0];
                        }
                        if (col[2].toString().equalsIgnoreCase("FD")) {
                            fdEciPortCode = (String) col[0];
                            fdEngmet = (String) col[1];
                        }
                    }

                    if (lclQuoteAc == null) {
                        lclQuoteAc = new LclQuoteAc();
                        lclQuoteAc.setEnteredBy(loginUser);
                        lclQuoteAc.setEnteredDatetime(now);
                    }
                    lclQuoteAc.setLclFileNumber(lclQuote.getLclFileNumber());
                    lclQuoteAc.setModifiedBy(loginUser);
                    lclQuoteAc.setModifiedDatetime(now);
                    lclQuoteAc.setTransDatetime(now);
                    LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                    GlMappingDAO glmappingdao = new GlMappingDAO();
                    lclQuotePiece.setHazmat(true);
                    new LclQuotePieceDAO().saveOrUpdate(lclQuotePiece);
                    GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0119", "LCLE", "AR");
                    lclQuotationChargesCalculation.calculateHazmatChargeForRadio(pooTrmnum, polTrmnum, fdEciPortCode, podEciPortCode, lclCommodityList, lclQuotePiece,
                            fdEngmet, loginUser, fileId, lclQuoteAc, glmapping);
                } else if ("N".equalsIgnoreCase(hazmat) && lclQuoteAc != null) {
                    lclQuotePiece.setHazmat(false);
                    String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                    new LclRemarksDAO().insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                    lclQuoteAcDAO.delete(lclQuoteAc);
                }
                List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
                request.setAttribute("lclCommodityList", lclCommodityList);
                exportQuoteUtils.setExpChargesDetails(lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
            }

            lclQuote.setModifiedBy(loginUser);
            lclQuote.setModifiedDatetime(now);
            lCLQuoteDAO.saveOrUpdate(lclQuote);

        } catch (Exception e) {
            log.info("Error in LCL calculateCharges method. " + now, e);
            return mapping.findForward("chargeDesc");
        }
        return mapping.findForward("chargeDesc");
    }

    public String setTrmNumAndEciportCode(LclQuoteCommodityForm lclQuoteCommodityForm, String rateType) throws Exception {
        String pooUnlocCode = CommonUtils.isNotEmpty(lclQuoteCommodityForm.getOriginUnlocCode())
                ? lclQuoteCommodityForm.getOriginUnlocCode() : "";
        String pooTrmnum = "", fdEciPortCode = "";
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        if (CommonUtils.isNotEmpty(pooUnlocCode)) {
            UnLocation unLocation = new UnLocationDAO().getUnlocation(pooUnlocCode);
            if (unLocation != null && unLocation.getLclRatedSourceId() != null) {
                pooUnlocCode = unLocation.getLclRatedSourceId().getUnLocationCode();
            }
        }
        List l = new LclUtils().getTrmNumandEciPortCode(pooUnlocCode, "", "", lclQuoteCommodityForm.getFdUnlocCode(), rateType);
        for (Object row : l) {
            Object[] col = (Object[]) row;
            if (col[2].toString().equalsIgnoreCase("POO")) {
                pooTrmnum = (String) col[0];
            }
            if (col[2].toString().equalsIgnoreCase("FD")) {
                fdEciPortCode = (String) col[0];
            }
        }
        lclQuoteCommodityForm.setOriginNo(pooTrmnum);
        lclQuoteCommodityForm.setDestinationNo(fdEciPortCode);
        boolean dojocount;
        if (CommonUtils.isNotEmpty(lclQuoteCommodityForm.getOriginUnlocCode())) {
            dojocount = lclBookingPieceDAO.hasDojoList(pooTrmnum, fdEciPortCode);
        } else {
            dojocount = lclBookingPieceDAO.hasEmptyOriginDojoList();
        }
        return dojocount ? "Y" : "N";

    }

    public ActionForward addHotcodeTrackingComm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String FORWARD_PAGE = "", refAddValue = "", _3pRef_type = "";
        String hotCode = lclQuoteCommodityForm.getHotCodes();
        String tracking = lclQuoteCommodityForm.getTracking();
        String referenceName = request.getParameter("refTypeFlag");
        String refValue = request.getParameter("refValue");
        String fileNumberId = request.getParameter("fileNumberId");
        String inbFlag = request.getParameter("inbFlag") != null ? request.getParameter("inbFlag") : "";
        User user = getCurrentUser(request);
        if (referenceName.equals("hotCodes") && CommonUtils.isNotEmpty(fileNumberId)) {
            LclQuoteHotCodeDAO hotCodeDAO = new LclQuoteHotCodeDAO();
            LclQuotePieceDAO lclQuotePieceDAO = new LclQuotePieceDAO();
            List<LclQuotePiece> lclQuotePieceList = lclQuotePieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (!lclQuotePieceList.isEmpty() && null != lclQuoteCommodityForm.getGenCodefield1() && lclQuoteCommodityForm.getGenCodefield1().equalsIgnoreCase("Y")) {
                for (LclQuotePiece lclQuotePiece : lclQuotePieceList) {
                    if (!lclQuotePiece.isHazmat()) {
                        lclQuotePiece.setHazmat(Boolean.TRUE);
                        lclQuotePieceDAO.save(lclQuotePiece);
                    }
                }
            }
            if (CommonUtils.isNotEmpty(refValue) && inbFlag.equalsIgnoreCase("true")) {
                String refNo = lcl3pRefNoDAO.getReferenceSize(fileNumberId, refValue);
                boolean isInbond = new LclInbondsDAO().isInbondExists(fileNumberId);
                if (isInbond && CommonUtils.isEmpty(refNo)) {
                } else {
                    refValue = "";
                }
            }
            String getAgentLevelBrand = new AgencyInfoDAO().getAgentLevelBrand(lclQuoteCommodityForm.getAgentNo(), lclQuoteCommodityForm.getPodUnlocCode());
            String isEculineHotCode = CommonUtils.isNotEmpty(refValue) ? refValue.substring(0, refValue.indexOf("/")) : "";
            if (hotCodeDAO.isHotCodeExist(refValue, fileNumberId)) {
                if ("EBL".equalsIgnoreCase(isEculineHotCode)) {
                    if (!"ECU".equalsIgnoreCase(lclQuoteCommodityForm.getLclQuotePiece().getLclFileNumber().getBusinessUnit()) && "".equalsIgnoreCase(getAgentLevelBrand)) {
                        new LclDwr().updateEconoOREculine("ECU", fileNumberId,
                                getCurrentUser(request).getUserId().toString(), REMARKS_QT_AUTO_NOTES);
                    }
                    refValue = lclQuoteCommodityForm.getHotCodes();
                }
                if (CommonUtils.isNotEmpty(refValue)) {
                    hotCodeDAO.insertQuery(fileNumberId, refValue.toUpperCase(), user.getUserId());
                }
            }
            request.setAttribute("lclHotCodeList", hotCodeDAO.getHotCodeList(Long.parseLong(fileNumberId)));
            _3pRef_type = _3PARTY_TYPE_HTC;
            refAddValue = "Inserted - Hot Codes# " + hotCode;
            FORWARD_PAGE = "hoteCodes";
        }
        if (referenceName.equals("tracking")) {
            //  referenceValue = lclQuoteForm.getTracking();
            _3pRef_type = _3PARTY_TYPE_TR;
            refAddValue = "Inserted - Tracking# " + tracking;
            FORWARD_PAGE = "tracking";
        }
        if (CommonUtils.isNotEmpty(refValue) && !"HTC".equalsIgnoreCase(_3pRef_type)) {
            lcl3pRefNoDAO.save3pRefNo(Long.parseLong(fileNumberId), _3pRef_type, refValue.toUpperCase());
        }
        if (CommonUtils.isNotEmpty(fileNumberId) && !"HTC".equalsIgnoreCase(_3pRef_type)) {
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(Long.parseLong(fileNumberId), _3pRef_type));
        }
        if (CommonUtils.isNotEmpty(refAddValue) && CommonUtils.isNotEmpty(fileNumberId)) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, refAddValue, getCurrentUser(request).getUserId());
        }
        return mapping.findForward(FORWARD_PAGE);
    }

    public ActionForward deleteLcl3pReferenceComm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCommodityForm lclQuoteCommodityForm = (LclQuoteCommodityForm) form;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        String FORWARD_PAGE = "";
        String referenceName = lclQuoteCommodityForm.getNoteId();
        String fileNumberId = lclQuoteCommodityForm.getFileNumberId().toString();
        String lcl3pRefId = lclQuoteCommodityForm.getLcl3pRefId().toString();
        String refDeleteValue = "", _3pRefType = "";
        if (CommonUtils.isNotEmpty(lcl3pRefId) && !referenceName.equals("hotCodes")) {
            Lcl3pRefNo lcl3pRefNo = lcl3pRefNoDAO.findById(Long.parseLong(lcl3pRefId));
            if (lcl3pRefNo != null) {
                if (referenceName.equals("tracking")) {
                    FORWARD_PAGE = "tracking";
                    refDeleteValue = "Deleted - Tracking# " + lcl3pRefNo.getReference();
                    _3pRefType = _3PARTY_TYPE_TR;
                }
                lcl3pRefNoDAO.delete(lcl3pRefNo);
            }
            request.setAttribute("lcl3PList", lcl3pRefNoDAO.getLclHscCodeListByType(fileNumberId, _3pRefType));
        } else if (referenceName.equals("hotCodes") && CommonUtils.isNotEmpty(lcl3pRefId)) {
            LclQuoteHotCode hotCode = new LclQuoteHotCodeDAO().findById(Long.parseLong(lcl3pRefId));
            FORWARD_PAGE = "hoteCodes";
            refDeleteValue = "Deleted - HOT Code# " + hotCode.getCode();
            _3pRefType = _3PARTY_TYPE_HTC;
            String isEculineHotCode = CommonUtils.isNotEmpty(hotCode.getCode())
                    ? hotCode.getCode().substring(0, hotCode.getCode().indexOf("/")) : "";
            String getAgentLevelBrand = new AgencyInfoDAO().getAgentLevelBrand(lclQuoteCommodityForm.getAgentNo(), lclQuoteCommodityForm.getPodUnlocCode());
            if ("EBL".equalsIgnoreCase(isEculineHotCode) && "".equalsIgnoreCase(getAgentLevelBrand)) {
                new LclDwr().updateEconoOREculine(lclQuoteCommodityForm.getBusinessUnit(),
                        fileNumberId, getCurrentUser(request).getUserId().toString(), REMARKS_QT_AUTO_NOTES);
            }
            new LclQuoteHotCodeDAO().delete(hotCode);
            request.setAttribute("lclHotCodeList", new LclQuoteHotCodeDAO().getHotCodeList(Long.parseLong(fileNumberId)));
        }
        if (CommonUtils.isNotEmpty(refDeleteValue) && CommonUtils.isNotEmpty(fileNumberId)) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, refDeleteValue, getCurrentUser(request).getUserId());
        }
        return mapping.findForward(FORWARD_PAGE);
    }
}
