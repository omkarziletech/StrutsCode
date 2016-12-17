/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.QuoteCommodityDetailsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.QuoteCommodityDetailsForm;
import java.math.BigDecimal;
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

/**
 *
 * @author lakshh
 */
public class QuoteCommodityDetailsAction extends LogiwareDispatchAction {

    private static String BOOKED_COMMODITY = "bookedCommodity";
    private static String ACTUAL_COMMODITY = "actualCommodity";
    private BigDecimal measureImp = null;

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
        quoteCommodityDetailsForm.setUom("I");
        List<LclQuotePieceDetail> detailList = null;
        LclQuotePiece lclQuotePiece = null;
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getFileNumberId())
                && CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getBookingPieceId())) {
            detailList = new QuoteCommodityDetailsDAO().findDetailProperty("quotePiece.id", quoteCommodityDetailsForm.getBookingPieceId());
        } else {
            if (lclSession.getQuoteCommodityList() != null) {
                int i;
                for (i = 0; i < lclSession.getQuoteCommodityList().size(); i++) {
                    lclQuotePiece = lclSession.getQuoteCommodityList().get(i);
                    if (CommonUtils.isNotEmpty(lclQuotePiece.getCommNo()) && lclQuotePiece.getCommNo().equals(quoteCommodityDetailsForm.getCommodityNo())) {
                        detailList = lclQuotePiece.getLclQuotePieceDetailList();
                        break;
                    }
                }
                if (i == lclSession.getQuoteCommodityList().size()) {
                    detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                }
            } else {
                detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
            }
            request.setAttribute("countVal", request.getParameter("countVal"));
            session.setAttribute("lclSession", lclSession);
        }
        if (detailList != null && detailList.isEmpty() && lclSession != null && lclSession.getQuoteDetailList() != null) {
            detailList = lclSession.getQuoteDetailList();
        }
        if (CommonUtils.isNotEmpty(detailList)) {
            calculateMeasureAndPiece(detailList, quoteCommodityDetailsForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
//            LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(0);
            quoteCommodityDetailsForm.setUom(detailList.get(0).getActualUom());
        }
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
        request.setAttribute("commodityDetailsForm", quoteCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
        LclQuotePiece lclQuotePiece = null;
        quoteCommodityDetailsForm.getLclQuotePieceDetail().setEnteredBy(getCurrentUser(request));
        quoteCommodityDetailsForm.getLclQuotePieceDetail().setModifiedBy(getCurrentUser(request));
        quoteCommodityDetailsForm.getLclQuotePieceDetail().setEnteredDatetime(new Date());
        quoteCommodityDetailsForm.getLclQuotePieceDetail().setModifiedDatetime(new Date());
        quoteCommodityDetailsForm.getLclQuotePieceDetail().setActualUom(quoteCommodityDetailsForm.getUom());
        List detailList = null;
        if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getFileNumberId())
                && CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getBookingPieceId())) {
            QuoteCommodityDetailsDAO quoteCommodityDetailsDAO = new QuoteCommodityDetailsDAO();
            LclQuotePiece piece = new LclQuotePieceDAO().findById(quoteCommodityDetailsForm.getBookingPieceId());
            quoteCommodityDetailsForm.getLclQuotePieceDetail().setQuotePiece(piece);
            if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getEditDetailId())) {
                LclQuotePieceDetail lclQuotePieceDetail = quoteCommodityDetailsDAO.findById(Long.parseLong(quoteCommodityDetailsForm.getEditDetailId()));
                lclQuotePieceDetail.setActualUom(request.getParameter("uom"));
                lclQuotePieceDetail.setModifiedBy(getCurrentUser(request));
                lclQuotePieceDetail.setQuotePiece(piece);
                lclQuotePieceDetail.setModifiedDatetime(new Date());
                lclQuotePieceDetail.setActualLength(new BigDecimal(quoteCommodityDetailsForm.getActualLength()));
                lclQuotePieceDetail.setActualWidth(new BigDecimal(quoteCommodityDetailsForm.getActualWidth()));
                lclQuotePieceDetail.setActualHeight(new BigDecimal(quoteCommodityDetailsForm.getActualHeight()));
                lclQuotePieceDetail.setPieceCount(Integer.parseInt(quoteCommodityDetailsForm.getPieceCount()));
                lclQuotePieceDetail.setActualWeight(!quoteCommodityDetailsForm.getActualWeight().isEmpty() ? 
                        new BigDecimal(quoteCommodityDetailsForm.getActualWeight()): null);
                lclQuotePieceDetail.setStowedLocation(quoteCommodityDetailsForm.getStowedLocation());
                quoteCommodityDetailsDAO.update(lclQuotePieceDetail);
                quoteCommodityDetailsForm.setEditDetailId(null);
            } else {
                quoteCommodityDetailsDAO.saveOrUpdate(quoteCommodityDetailsForm.getLclQuotePieceDetail());
            }
            detailList = quoteCommodityDetailsDAO.findDetailProperty("quotePiece.id", quoteCommodityDetailsForm.getBookingPieceId());
        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getQuoteCommodityList())) {
                int i;
                for (i = 0; i < lclSession.getQuoteCommodityList().size(); i++) {
                    lclQuotePiece = lclSession.getQuoteCommodityList().get(i);
                    if (CommonUtils.isNotEmpty(lclQuotePiece.getCommNo()) && lclQuotePiece.getCommNo().equals(quoteCommodityDetailsForm.getCommodityNo())) {
                        detailList = lclQuotePiece.getLclQuotePieceDetailList();
                        if (detailList == null) {
                            detailList = new ArrayList<LclQuotePieceDetail>();
                        }
                        String id = request.getParameter("id");
                        if (CommonUtils.isNotEmpty(id)) {
                            LclQuotePieceDetail lbpd = (LclQuotePieceDetail) detailList.get(Integer.parseInt(id));
                            detailList.remove(Integer.parseInt(id));
                        }
                        quoteCommodityDetailsForm.getLclQuotePieceDetail().setQuotePiece(lclQuotePiece);
                        detailList.add(quoteCommodityDetailsForm.getLclQuotePieceDetail());
                        lclQuotePiece.setLclQuotePieceDetailList(detailList);
                        lclSession.getQuoteCommodityList().set(i, lclQuotePiece);
                        break;
                    }
                }
                if (i == lclSession.getQuoteCommodityList().size()) {
                    detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                    String id = request.getParameter("countVal");
                    if (CommonUtils.isNotEmpty(id) && !detailList.isEmpty()) {
                        detailList.remove(Integer.parseInt(id));
                    }
                    detailList.add(quoteCommodityDetailsForm.getLclQuotePieceDetail());
                    lclSession.setQuoteDetailList(detailList);
                }
            } else {
                detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                String id = request.getParameter("id");
                if (CommonUtils.isNotEmpty(id)) {
                    detailList.remove(Integer.parseInt(id));
                }
                detailList.add(quoteCommodityDetailsForm.getLclQuotePieceDetail());
                lclSession.setQuoteDetailList(detailList);
            }
            session.setAttribute("lclSession", lclSession);
        }
        if (CommonUtils.isNotEmpty(detailList)) {
            calculateMeasureAndPiece(detailList, quoteCommodityDetailsForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
        }
        request.setAttribute("quoteCommodityDetailsForm", quoteCommodityDetailsForm);
        request.setAttribute("editFlag", "True");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
        LclQuotePieceDetail quotePieceDetail = null;
        String id = request.getParameter("id");
        String countVal = request.getParameter("countVal");
        List detailList = null;
        if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getFileNumberId())
                && CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getBookingPieceId())) {
            detailList = new QuoteCommodityDetailsDAO().findDetailProperty("quotePiece.id", quoteCommodityDetailsForm.getBookingPieceId());
            quotePieceDetail = quoteCommodityDetailsForm.getLclQuotePieceDetail();
        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getQuoteCommodityList())) {
                int i;
                for (i = 0; i < lclSession.getQuoteCommodityList().size(); i++) {
                    LclQuotePiece lclQuotePiece = lclSession.getQuoteCommodityList().get(i);
                    detailList = lclQuotePiece.getLclQuotePieceDetailList();
                    if (CommonUtils.isNotEmpty(countVal)) {
                        if (CommonUtils.isNotEmpty(detailList)) {
                            if (CommonUtils.isNotEmpty(lclQuotePiece.getCommNo()) && lclQuotePiece.getCommNo().equals(quoteCommodityDetailsForm.getCommodityNo())) {
                                if (CommonUtils.isNotEmpty(id)) {
                                    quotePieceDetail = (LclQuotePieceDetail) detailList.get(Integer.parseInt(id));
                                    break;
                                } else if (CommonUtils.isNotEmpty(countVal)) {
                                    quotePieceDetail = (LclQuotePieceDetail) detailList.get(Integer.parseInt(countVal));
                                    break;
                                }
                            }
                        }
                    }
                }
                if (i == lclSession.getQuoteCommodityList().size()) {
                    detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                    if (CommonUtils.isNotEmpty(countVal)) {
                        quotePieceDetail = (LclQuotePieceDetail) detailList.get(Integer.parseInt(countVal));
                        //detailList.remove(bookingPieceDetail);
                    }
                }
            } else {
                detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                if (CommonUtils.isNotEmpty(id)) {
                    quotePieceDetail = (LclQuotePieceDetail) detailList.get(Integer.parseInt(id));
                    //detailList.remove(bookingPieceDetail);
                }
            }
        }

        calculateMeasureAndPiece(detailList, quoteCommodityDetailsForm);
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getQtPieceDetailId())) {
            request.setAttribute("id", quoteCommodityDetailsForm.getQtPieceDetailId());
        } else {
            request.setAttribute("id", id);
        }
        request.setAttribute("countVal", countVal);
        request.setAttribute("editDetailId", request.getParameter("qtPieceDetailId"));
        request.setAttribute("detailList", detailList);
        request.setAttribute("quotePieceDetail", quotePieceDetail);
        request.setAttribute("quoteCommodityDetailsForm", quoteCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
        String id = request.getParameter("id");
        String commodityNo = request.getParameter("commodityNo");
        List detailList = null;
        QuoteCommodityDetailsDAO detailDAO = new QuoteCommodityDetailsDAO();
        if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getQtPieceDetailId())) {
            detailDAO.delete(quoteCommodityDetailsForm.getLclQuotePieceDetail());
            detailList = detailDAO.findDetailProperty("quotePiece.id", quoteCommodityDetailsForm.getBookingPieceId());
            if (detailList != null && !detailList.isEmpty()) {
                LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(0);
                quoteCommodityDetailsForm.setUom(detail.getActualUom());
            } else {
                quoteCommodityDetailsForm.setUom("I");
            }
            quoteCommodityDetailsForm.setQtPieceDetailId(null);
        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getQuoteCommodityList())) {
                int i;
                for (i = 0; i < lclSession.getQuoteCommodityList().size(); i++) {
                    LclQuotePiece lclQuotePiece = lclSession.getQuoteCommodityList().get(i);
                    detailList = lclQuotePiece.getLclQuotePieceDetailList();
                    if (CommonUtils.isNotEmpty(id)) {
                        if (CommonUtils.isNotEmpty(detailList)) {
                            if (CommonUtils.isNotEmpty(lclQuotePiece.getCommNo()) && lclQuotePiece.getCommNo().equals(commodityNo)) {
                                LclQuotePieceDetail lqpd = (LclQuotePieceDetail) detailList.get(Integer.parseInt(id));
                                detailList.remove(Integer.parseInt(id));
                                break;
                            }
                        }
                    }
                }
                if (i == lclSession.getQuoteCommodityList().size()) {
                    detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                    if (CommonUtils.isNotEmpty(detailList)) {
                        detailList.remove(Integer.parseInt(id));
                    }
                }
            } else {
                detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                if (CommonUtils.isNotEmpty(id)) {
                    if (CommonUtils.isNotEmpty(detailList)) {
                        detailList.remove(Integer.parseInt(id));
                    }
                }
            }
            lclSession.setQuoteDetailList(detailList);
            session.setAttribute("lclSession", lclSession);
        }
        calculateMeasureAndPiece(detailList, quoteCommodityDetailsForm);
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        request.setAttribute("countVal", request.getParameter("countVal"));
        request.setAttribute("commodityDetailsForm", quoteCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward updateBookedComm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
        BigDecimal metricDivisor = new BigDecimal(35.3146);
        BigDecimal weightDivisor = new BigDecimal(2.2046);
        //BigDecimal measureImp = new BigDecimal(0.000);
        BigDecimal weightImp = new BigDecimal(0.000);
        BigDecimal measureMet = new BigDecimal(0.000);
        BigDecimal weightMet = new BigDecimal(0.000);
        BigDecimal measureImp = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalMeasureImp()));
        weightImp = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalWeightImp()));
        measureMet = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalMeasureMet()));
        BigDecimal MeasureMetM = measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        weightMet = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalWeightMet()));
        BigDecimal weightMetM = weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        request.setAttribute("bookedPieceCount", quoteCommodityDetailsForm.getTotalPieces());
        PackageTypeDAO packageTypeDAO = new PackageTypeDAO();
        //PackageType packageType = packageTypeDAO.findById(request.getParameter("packID"));
        //request.setAttribute("pack", packageType.getDescription());
        if (quoteCommodityDetailsForm.getActualUom().equals("I")) {
            BigDecimal measureImpVal = new BigDecimal(NumberUtils.roundDecimalInteger(measureImp.doubleValue()));
            BigDecimal weightImpVal = new BigDecimal(NumberUtils.roundDecimalInteger(weightImp.doubleValue()));
            if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(quoteCommodityDetailsForm.getModuleName())) {
                request.setAttribute("totalMeasureImp", measureImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("totalMeasureMet", measureImp.divide(metricDivisor, 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("totalWeightImp", weightImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("totalWeightMet", weightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
            } else {
                if (quoteCommodityDetailsForm.getUps()) {
                    measureImp = new BigDecimal(NumberUtils.roundDecimalInteger(measureImp.doubleValue() + 2));
                    request.setAttribute("totalMeasureImp", NumberUtils.roundDecimalInteger(measureImp.doubleValue()));
                } else {
                    if (measureImp.doubleValue() < 0.5) {
                        measureImp = BigDecimal.ONE;
                    }
                    measureImp = new BigDecimal(NumberUtils.roundDecimalInteger(measureImp.doubleValue()));
                    request.setAttribute("totalMeasureImp", NumberUtils.roundDecimalInteger(measureImp.doubleValue()));
                }
                request.setAttribute("totalMeasureMet", measureImp.divide(metricDivisor, 3, BigDecimal.ROUND_FLOOR));
                request.setAttribute("totalWeightImp", NumberUtils.roundDecimalInteger(weightImp.doubleValue()));
                request.setAttribute("totalWeightMet", weightImpVal.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
            }
        } else if (quoteCommodityDetailsForm.getActualUom().equals("M")) {
            request.setAttribute("totalMeasureImp", MeasureMetM.multiply(metricDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalMeasureMet", measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalWeightMet", weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
        }

        LclSession lclSession = null != (LclSession) request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getQuoteCommodityList())) {
//             lclSession.setQuoteDetailList(detailList);
            request.getSession().setAttribute("lclSession", lclSession);
        }

        request.setAttribute("moduleNames", quoteCommodityDetailsForm.getModuleName());
        return mapping.findForward(BOOKED_COMMODITY);
    }

    public ActionForward updateActualComm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
        BigDecimal metricDivisor = new BigDecimal(35.3146);
        BigDecimal weightDivisor = new BigDecimal(2.2046);
        //BigDecimal measureImp = new BigDecimal(0.000);
        BigDecimal weightImp = new BigDecimal(0.000);
        BigDecimal measureMet = new BigDecimal(0.000);
        BigDecimal weightMet = new BigDecimal(0.000);
        BigDecimal measureImp = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalMeasureImp()));
        weightImp = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalWeightImp()));
        measureMet = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalMeasureMet()));
        BigDecimal MeasureMetM = measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        weightMet = new BigDecimal(Double.parseDouble(quoteCommodityDetailsForm.getTotalWeightMet()));
        BigDecimal weightMetM = weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        request.setAttribute("actualPieceCount", quoteCommodityDetailsForm.getTotalPieces());
        if (quoteCommodityDetailsForm.getActualUom().equals("I")) {
            request.setAttribute("actualMeasureImp", measureImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualMeasureMet", measureImp.divide(metricDivisor, 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightImp", weightImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightMet", weightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
        } else if (quoteCommodityDetailsForm.getActualUom().equals("M")) {
            request.setAttribute("actualMeasureImp", MeasureMetM.multiply(metricDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualMeasureMet", measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightMet", weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
        }
        return mapping.findForward(ACTUAL_COMMODITY);
    }

    public void calculateMeasureAndPiece(List detailList, QuoteCommodityDetailsForm quoteCommodityDetailsForm) {
        BigDecimal totalMeasure = new BigDecimal(0.000);
        BigDecimal totalMeasureM = new BigDecimal(0.000);
        Integer totalPieces = 0;
        BigDecimal totalWeightImp = new BigDecimal(0.000);
        BigDecimal totalWeightImpM = new BigDecimal(0.000);
        BigDecimal totalWeightMet = new BigDecimal(0.000);
        BigDecimal totalWeightMetM = new BigDecimal(0.000);
        for (Object obj : detailList) {
            LclQuotePieceDetail lqpd = (LclQuotePieceDetail) obj;
            totalMeasure = totalMeasure.add(lqpd.getActualLength().multiply(lqpd.getActualWidth()).multiply(lqpd.getActualHeight()).multiply(new BigDecimal(lqpd.getPieceCount())));
            BigDecimal devisior = new BigDecimal(1728);
            BigDecimal metricDivisor = new BigDecimal(35.3146);
            BigDecimal measureImp = totalMeasure.divide(devisior, 3, BigDecimal.ROUND_CEILING);
            BigDecimal measureMet = measureImp.divide(metricDivisor, 3, BigDecimal.ROUND_CEILING);
            totalMeasureM = totalMeasureM.add((lqpd.getActualLength().multiply(new BigDecimal(0.3937))).multiply(lqpd.getActualWidth().multiply(new BigDecimal(0.3937))).multiply(lqpd.getActualHeight().multiply(new BigDecimal(0.3937))).multiply(new BigDecimal(lqpd.getPieceCount())));
            BigDecimal measureImpM = totalMeasureM.divide(devisior, 3, BigDecimal.ROUND_CEILING);
            BigDecimal measureMetM = measureImpM.divide(metricDivisor, 3, BigDecimal.ROUND_CEILING);
            totalPieces += lqpd.getPieceCount();
            if (lqpd.getActualWeight() != null) {
                totalWeightImp = totalWeightImp.add(lqpd.getActualWeight().multiply(new BigDecimal(lqpd.getPieceCount())));
                totalWeightImpM = totalWeightImpM.add((lqpd.getActualWeight().multiply(new BigDecimal(0.3937))).multiply(new BigDecimal(lqpd.getPieceCount())));
                totalWeightImpM = totalWeightImpM.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_CEILING);
            }
            BigDecimal weightDivisor = new BigDecimal(2.2046);
            totalWeightMet = totalWeightImp.divide(weightDivisor, 3, BigDecimal.ROUND_CEILING);
            totalWeightMetM = totalWeightImpM.divide(weightDivisor, 3, BigDecimal.ROUND_CEILING);
//            int roundInt = (int) Math.round(measureMetM.doubleValue());
            quoteCommodityDetailsForm.setTotalWeightImp("" + totalWeightImp);
            quoteCommodityDetailsForm.setTotalWeightMet("" + totalWeightMetM);
            quoteCommodityDetailsForm.setTotalMeasureImp("" + measureImp);
            quoteCommodityDetailsForm.setTotalMeasureMet("" + measureMetM);
            LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(0);
            if (detail.getActualUom() != null && !detail.getActualUom().equals("")) {
                if (detail.getActualUom().equals("I")) {
                    quoteCommodityDetailsForm.setDuptotalWeightImp("" + totalWeightImp);
                    quoteCommodityDetailsForm.setDuptotalWeightMet("" + totalWeightMet);
                    quoteCommodityDetailsForm.setDuptotalMeasureImp("" + measureImp);
                    quoteCommodityDetailsForm.setDuptotalMeasureMet("" + measureMet);
                } else {
                    quoteCommodityDetailsForm.setDuptotalWeightImp("" + totalWeightImpM);
                    quoteCommodityDetailsForm.setDuptotalWeightMet("" + totalWeightMetM);
                    quoteCommodityDetailsForm.setDuptotalMeasureImp("" + measureImpM);
                    quoteCommodityDetailsForm.setDuptotalMeasureMet("" + measureMetM);
                }
            }
            quoteCommodityDetailsForm.setTotalPieces(totalPieces);
        }
    }

    public void displayListImp(List detailList, HttpServletRequest request) {
        List detailListImp = new LinkedList();
        HttpSession session = request.getSession();
        for (Object obj : detailList) {
            LclQuotePieceDetail pieceDetail = (LclQuotePieceDetail) obj;
            if (pieceDetail.getActualUom().equals("M")) {
                LclQuotePieceDetail pieceNew = new LclQuotePieceDetail();
                pieceNew.setActualLength(pieceDetail.getActualLength());
                pieceNew.setActualWidth(pieceDetail.getActualWidth());
                pieceNew.setActualHeight(pieceDetail.getActualHeight());
                pieceNew.setPieceCount(pieceDetail.getPieceCount());
                pieceNew.setActualUom(pieceDetail.getActualUom());
                pieceNew.setStowedLocation(pieceDetail.getStowedLocation());
                if (pieceDetail.getActualWeight() != null) {
                    pieceNew.setActualWeight(pieceDetail.getActualWeight().multiply(new BigDecimal(2.2046)).setScale(3, BigDecimal.ROUND_CEILING));
                }
                detailListImp.add(pieceNew);
            } else {
                detailListImp.add(pieceDetail);
            }
            request.setAttribute("detailListImp", detailListImp);
        }
    }

    public void displayListMet(List<LclQuotePieceDetail> detailList, HttpServletRequest request) {
        List detailListMet = new LinkedList();
        for (Object obj : detailList) {
            LclQuotePieceDetail pieceDetail = (LclQuotePieceDetail) obj;
            if (pieceDetail.getActualUom().equals("I")) {
                LclQuotePieceDetail pieceNew = new LclQuotePieceDetail();
                pieceNew.setActualLength(pieceDetail.getActualLength());
                pieceNew.setActualWidth(pieceDetail.getActualWidth());
                pieceNew.setActualHeight(pieceDetail.getActualHeight());
                pieceNew.setPieceCount(pieceDetail.getPieceCount());
                pieceNew.setActualUom(pieceDetail.getActualUom());
                pieceNew.setStowedLocation(pieceDetail.getStowedLocation());
                BigDecimal metricDivisor = new BigDecimal(2.2046);
                if (pieceDetail.getActualWeight() != null) {
                    BigDecimal weightMet = pieceDetail.getActualWeight().divide(metricDivisor, 3, BigDecimal.ROUND_FLOOR);
                    pieceNew.setActualWeight(weightMet);
                }
                detailListMet.add(pieceNew);
            } else {
                detailListMet.add(pieceDetail);
            }
            request.setAttribute("detailListMet", detailListMet);
        }

    }

    public ActionForward quoteDeleteAllPiece(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityDetailsForm = (QuoteCommodityDetailsForm) form;
//        String quoteDeleteAll = request.getParameter("quoteDeleteAll");
        if (CommonUtils.isNotEmpty(quoteCommodityDetailsForm.getBookingPieceId()) && quoteCommodityDetailsForm.getBookingPieceId() != 0) {
            List<LclQuotePieceDetail> quoteAllDelete = new QuoteCommodityDetailsDAO().findByProperty("quotePiece.id", quoteCommodityDetailsForm.getBookingPieceId());
            if (CommonUtils.isNotEmpty(quoteAllDelete)) {
                for (LclQuotePieceDetail quoteDelete : quoteAllDelete) {
                    new QuoteCommodityDetailsDAO().delete(quoteDelete);
                }
            }
        }
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        if (lclSession != null) {
            session.removeAttribute("lclSession");
        }
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
        request.setAttribute("countVal", request.getParameter("countVal"));
        request.setAttribute("quoteCommodityDetailsForm", quoteCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward displayMultipleDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityForm = (QuoteCommodityDetailsForm) form;
        request.setAttribute("parentRowLen", request.getParameter("rowLength"));
        request.setAttribute("countVal", request.getParameter("countVal"));
        request.setAttribute("quoteCommodityForm", quoteCommodityForm);
        return mapping.findForward("quoteMultipleDims");
    }

    public ActionForward saveMultiDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCommodityDetailsForm quoteCommodityForm = (QuoteCommodityDetailsForm) form;
        Date now = new Date();
        String actUom = quoteCommodityForm.getUom();
        User user = getCurrentUser(request);
        HttpSession session = request.getSession();
        List<LclQuotePieceDetail> detailList = null;
        LclQuotePiece piece = null;
        QuoteCommodityDetailsDAO QcommodityDetailsDAO = new QuoteCommodityDetailsDAO();

        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String[] lengthDims = quoteCommodityForm.getLengthDims().split(",");
        String[] widthDims = quoteCommodityForm.getAcWidthDims().split(",");
        String[] heightDims = quoteCommodityForm.getHeightDims().split(",");
        String[] piecesDims = quoteCommodityForm.getPiecesDims().split(",");
        String[] weightDims = quoteCommodityForm.getWeightDims().split(",");
        String[] wareHouseDims = quoteCommodityForm.getWarehouseTabDims().split(",");
        boolean stFlag = false;

        if (CommonUtils.isNotEmpty(quoteCommodityForm.getQtPieceDetailId())) {
            piece = new LclQuotePieceDAO().findById(quoteCommodityForm.getQtPieceDetailId());
        }
        for (int i = 0; i < lengthDims.length; i++) {
            LclQuotePieceDetail lclQuotePieceDetail = new LclQuotePieceDetail();
            lclQuotePieceDetail.setActualUom(actUom);
            lclQuotePieceDetail.setEnteredBy(user);
            lclQuotePieceDetail.setModifiedBy(user);
            lclQuotePieceDetail.setEnteredDatetime(now);
            lclQuotePieceDetail.setModifiedDatetime(now);
            lclQuotePieceDetail.setActualLength(new BigDecimal(lengthDims[i]));
            lclQuotePieceDetail.setActualWidth(new BigDecimal(widthDims[i]));
            lclQuotePieceDetail.setActualHeight(new BigDecimal(heightDims[i]));
            lclQuotePieceDetail.setPieceCount(Integer.parseInt(piecesDims[i]));
            if (weightDims.length > i) {
                lclQuotePieceDetail.setActualWeight(!weightDims[i].isEmpty() ? new BigDecimal(weightDims[i]) : null);
            } else {
                lclQuotePieceDetail.setActualWeight(null);
            }
            if (wareHouseDims.length > i) {
                lclQuotePieceDetail.setStowedLocation(!wareHouseDims[i].isEmpty() ? wareHouseDims[i] : "");
            } else {
                lclQuotePieceDetail.setStowedLocation("");
            }
            if (CommonUtils.isNotEmpty(quoteCommodityForm.getQtPieceDetailId())) {
                lclQuotePieceDetail.setQuotePiece(piece);
                QcommodityDetailsDAO.save(lclQuotePieceDetail);
            } else if (!stFlag) {
                stFlag = true;
                if (lclSession.getQuoteCommodityList() != null && !request.getParameter("countVal").isEmpty()) {
                    detailList = null != lclSession.getQuoteCommodityList().get(Integer.parseInt(request.getParameter("countVal"))).getLclQuotePieceDetailList()
                            ? lclSession.getQuoteCommodityList().get(Integer.parseInt(request.getParameter("countVal"))).getLclQuotePieceDetailList() : new LinkedList();
                } else {
                    detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
                }
                detailList.add(lclQuotePieceDetail);
            } else {
                detailList.add(lclQuotePieceDetail);
            }
//            lclUtils.addOverSizeRemarks(commodityForm.getUom(), heightDims[i], widthDims[i], lengthDims[i],
//                    commodityForm.getFileNumberId(), request, lclSession, getCurrentUser(request));
        }

        if (CommonUtils.isNotEmpty(quoteCommodityForm.getQtPieceDetailId())) {
            if (lclSession.getQuoteDetailList() != null && lclSession.getQuoteDetailList().size() > 0) {
                lclSession.getQuoteDetailList().removeAll(lclSession.getQuoteDetailList());
            }
            detailList = QcommodityDetailsDAO.findDetailProperty("quotePiece.id", quoteCommodityForm.getQtPieceDetailId());
            lclSession.setQuoteDetailList(detailList);
        } else {
            lclSession.setQuoteDetailList(detailList);
        }
        session.setAttribute("lclSession", lclSession);
        if (CommonUtils.isNotEmpty(detailList)) {
            calculateMeasureAndPiece(detailList, quoteCommodityForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
            LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(0);
            request.setAttribute("uom", detail.getActualUom());
        }
        request.setAttribute("countVal", request.getParameter("countVal"));
        request.setAttribute("quoteCommodityForm", quoteCommodityForm);
        return mapping.findForward(SUCCESS);
    }

}
