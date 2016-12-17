/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.hibernate.dao.lcl.CommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cvst.logisoft.struts.form.lcl.CommodityDetailsForm;
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

/**
 *
 * @author lakshh
 */
public class CommodityDetailsAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static String BOOKED_COMMODITY = "bookedCommodity";
    private static String ACTUAL_COMMODITY = "actualCommodity";
    private NumberFormat df = new DecimalFormat("#.000");

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        String commodityNo = request.getParameter("commodityNo");
        String fileNumberId = request.getParameter("fileNumberId");
        String packageTypeId = request.getParameter("packageTypeId");
        String packageType = request.getParameter("packageType");
        HttpSession session = request.getSession();
        String editDimFlag = request.getParameter("editDimFlag");
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List detailList = null;
        //!= lclSession.getDetailList() ? lclSession.getDetailList() : new LinkedList();
        LclBookingPiece lclBookingPiece = null;
        if (fileNumberId != null && !fileNumberId.equals("") && !fileNumberId.equals("0") && editDimFlag.equals("true") && request.getParameter("bookingPieceId") != null && !request.getParameter("bookingPieceId").toString().trim().equals("")
                && !request.getParameter("bookingPieceId").equals("0")) {
            Long bookingPieceId = Long.parseLong(request.getParameter("bookingPieceId"));
            commodityDetailsForm.setBookingPieceId(bookingPieceId);
            detailList = new CommodityDetailsDAO().findDetailProperty("lclBookingPiece.id", bookingPieceId);
            if (CommonUtils.isNotEmpty(detailList)) {
                for (int i = 0; i < detailList.size(); i++) {
                    LclBookingPieceDetail lclBookingPieceDetail = (LclBookingPieceDetail) detailList.get(i);
                    request.setAttribute("uom", lclBookingPieceDetail.getActualUom());
                    lclSession.setBookingDetailList(detailList);
                }
            }
        } else {
            if (lclSession.getCommodityList() != null) {
                int i;
                for (i = 0; i < lclSession.getCommodityList().size(); i++) {
                    lclBookingPiece = lclSession.getCommodityList().get(i);
                    if (CommonUtils.isNotEmpty(lclBookingPiece.getCommNo()) && lclBookingPiece.getCommNo().equals(commodityNo)) {
                        detailList = lclBookingPiece.getLclBookingPieceDetailList();
                        if (CommonUtils.isNotEmpty(detailList)) {
                            break;
                        }
                    }
                }
                if (i == lclSession.getCommodityList().size()) {
                    detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                }
            } else {
                detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
            }
        }
        session.setAttribute("lclSession", lclSession);
        if (CommonUtils.isNotEmpty(detailList)) {
            calculateMeasureAndPiece(detailList, commodityDetailsForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
            LclBookingPieceDetail detail = (LclBookingPieceDetail) detailList.get(0);
            request.setAttribute("uom", detail.getActualUom());
        }
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            request.setAttribute("bookingExport", bookingExport);
        }
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
//        request.setAttribute("bookingPieceId", request.getParameter("bookingPieceId"));
        request.setAttribute("commodityNo", commodityNo);
        request.setAttribute("packageTypeId", packageTypeId);
        request.setAttribute("packageType", packageType);
        request.setAttribute("fileNumberId", fileNumberId);
        request.setAttribute("commodityDetailsForm", commodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        LclBookingPiece lclBookingPiece = null;
        boolean editorAddFlag = null == commodityDetailsForm.getLclBookingPieceDetail().getId() ? true : false;
        commodityDetailsForm.getLclBookingPieceDetail().setEnteredBy(getCurrentUser(request));
        commodityDetailsForm.getLclBookingPieceDetail().setModifiedBy(getCurrentUser(request));
        commodityDetailsForm.getLclBookingPieceDetail().setEnteredDatetime(new Date());
        commodityDetailsForm.getLclBookingPieceDetail().setModifiedDatetime(new Date());
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String commodityNo = request.getParameter("commodityNo");
        String bookingPieceId = request.getParameter("bookingPieceId");
        String packageTypeId = request.getParameter("packageTypeId");
        String packageType = request.getParameter("packageType");
        String dimFlag = request.getParameter("dimFlag");
        String actualLength = request.getParameter("actualLength");
        String actualWidth = request.getParameter("actualWidth");
        String actualWeight = request.getParameter("actualWeight");
        String actualHeight = request.getParameter("actualHeight");
        String pieceCount = request.getParameter("pieceCount");
        String warehouse = null != request.getParameter("warehouse") ? request.getParameter("warehouse") : "";
        String uom1 = request.getParameter("uom");
        List detailList = null;
        commodityDetailsForm.getLclBookingPieceDetail().setActualHeight(new BigDecimal(actualHeight));
        commodityDetailsForm.getLclBookingPieceDetail().setActualLength(new BigDecimal(actualLength));
        if (CommonUtils.isNotEmpty(actualWeight)) {
            commodityDetailsForm.getLclBookingPieceDetail().setActualWeight(new BigDecimal(actualWeight));
        }
        commodityDetailsForm.getLclBookingPieceDetail().setActualWidth(new BigDecimal(actualWidth));
        commodityDetailsForm.getLclBookingPieceDetail().setActualUom(uom1);
        commodityDetailsForm.getLclBookingPieceDetail().setStowedLocation(warehouse);
        commodityDetailsForm.getLclBookingPieceDetail().setPieceCount(new Integer(pieceCount));
        if (CommonUtils.isNotEmpty(bookingPieceId) && !bookingPieceId.equals("0")
                && CommonUtils.isNotEmpty(commodityDetailsForm.getFileNumberId())) {
            Long bookingPiecesId = Long.parseLong(request.getParameter("bookingPieceId"));
            LclBookingPiece piece = new LclBookingPieceDAO().findById(bookingPiecesId);
            commodityDetailsForm.getLclBookingPieceDetail().setLclBookingPiece(piece);
            new CommodityDetailsDAO().saveOrUpdate(commodityDetailsForm.getLclBookingPieceDetail());
            detailList = new CommodityDetailsDAO().findDetailProperty("lclBookingPiece.id", bookingPiecesId);
            if (editorAddFlag) {
                List<LclBookingPieceWhse> wareHouseList = new LclBookingPieceWhseDAO().findByFileAndCommodityList(piece.getId());
                String existsWhseLocation = "";
                if (!wareHouseList.isEmpty()) {
                    LclBookingPieceWhse piecewareHouse = wareHouseList.get(0);
                    existsWhseLocation = null != piecewareHouse.getLocation() ? piecewareHouse.getLocation() : "";
                    warehouse = "".equalsIgnoreCase(warehouse) ? warehouse : existsWhseLocation.equalsIgnoreCase("") ? warehouse : "," + warehouse;
                    piecewareHouse.setLocation(existsWhseLocation + "" + warehouse);
                    new LclBookingPieceWhseDAO().update(piecewareHouse);
                }
            }
        } else {
            if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getCommodityList())) {
                int i;
                for (i = 0; i < lclSession.getCommodityList().size(); i++) {
                    lclBookingPiece = lclSession.getCommodityList().get(i);
                    if (null != lclBookingPiece.getCommodityType()
                            && lclBookingPiece.getCommodityType().getCode().equals(commodityNo)) {
                        detailList = lclBookingPiece.getLclBookingPieceDetailList();
                        if (detailList == null) {
                            detailList = new ArrayList<LclBookingPieceDetail>();
                        }
                        String id = request.getParameter("id");
                        if (CommonUtils.isNotEmpty(id) && !detailList.isEmpty()) {
                            LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                            detailList.remove(Integer.parseInt(id));
                        }
                        commodityDetailsForm.getLclBookingPieceDetail().setLclBookingPiece(lclBookingPiece);
                        detailList.add(commodityDetailsForm.getLclBookingPieceDetail());
                        lclBookingPiece.setLclBookingPieceDetailList(detailList);
                        lclSession.getCommodityList().set(i, lclBookingPiece);
                        break;
                    }
                }
                if (i == lclSession.getCommodityList().size()) {
                    detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                    String id = request.getParameter("id");
                    String uom = commodityDetailsForm.getActualUom();
                    if (CommonUtils.isNotEmpty(id)) {
                        LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                        detailList.remove(Integer.parseInt(id));
                    }
                    detailList.add(commodityDetailsForm.getLclBookingPieceDetail());
                    lclSession.setBookingDetailList(detailList);
                }
            } else {
                detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                String id = request.getParameter("id");
                String uom = commodityDetailsForm.getActualUom();
                if (CommonUtils.isNotEmpty(id) && !detailList.isEmpty()) {
                    LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                    detailList.remove(Integer.parseInt(id));
                }
                detailList.add(commodityDetailsForm.getLclBookingPieceDetail());
                lclSession.setBookingDetailList(detailList);
            }
        }
        session.setAttribute("lclSession", lclSession);
        if (CommonUtils.isNotEmpty(commodityDetailsForm.getFileNumberId())) {
            LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", commodityDetailsForm.getFileNumberId());
            request.setAttribute("bookingExport", bookingExport);
        }
        if (detailList != null) {
            calculateMeasureAndPiece(detailList, commodityDetailsForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
            lclSession.setBookingDetailList(detailList);
        }
        LclUtils lclUtils = new LclUtils();
        lclUtils.addOverSizeRemarks(uom1, actualHeight, actualWidth, actualLength, commodityDetailsForm.getFileNumberId(),
                request, lclSession, getCurrentUser(request));
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("dimFlag", dimFlag);
        request.setAttribute("uom", uom1);
        request.setAttribute("packageTypeId", packageTypeId);
        request.setAttribute("packageType", packageType);
//        request.setAttribute("bookingPieceId", bookingPieceId);
        request.setAttribute("commodityNo", commodityNo);
        request.setAttribute("commodityDetailsForm", commodityDetailsForm);
        request.setAttribute("editFlag", "True");
        return mapping.findForward(SUCCESS);

    }

    public ActionForward editDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        LclBookingPieceDetail bookingPieceDetail = null;
        String id = request.getParameter("id");
        String bookingPieceId = request.getParameter("bookingPieceId");
        String detailId = request.getParameter("detailId");
        String fileNumberId = request.getParameter("fileNumberId");
        String uom = request.getParameter("uom");
        HttpSession session = request.getSession();
        String commodityNo = request.getParameter("commodityNo");
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List detailList = null;
        if (CommonUtils.isNotEmpty(fileNumberId) && !fileNumberId.equals("0") && CommonUtils.isNotEmpty(bookingPieceId) && !bookingPieceId.equals("0")) {
            LclBookingPiece piece = new LclBookingPieceDAO().findById(new Long(bookingPieceId));
            detailList = new CommodityDetailsDAO().findDetailProperty("lclBookingPiece.id", piece.getId());
            bookingPieceDetail = new CommodityDetailsDAO().findById(new Long(detailId));
        } else {
            if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getCommodityList())) {
                int i;
                for (i = 0; i < lclSession.getCommodityList().size(); i++) {
                    LclBookingPiece lclBookingPiece = lclSession.getCommodityList().get(i);
                    detailList = lclBookingPiece.getLclBookingPieceDetailList();
                    if (CommonUtils.isNotEmpty(id)) {
                        if (CommonUtils.isNotEmpty(detailList)) {
                            if (CommonUtils.isNotEmpty(lclBookingPiece.getCommNo()) && lclBookingPiece.getCommNo().equals(commodityNo)) {
                                if (CommonUtils.isNotEmpty(id)) {
                                    bookingPieceDetail = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                                    break;
                                }
                            }
                        }
                    }
                }
                if (i == lclSession.getCommodityList().size()) {
                    detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                    if (CommonUtils.isNotEmpty(id)) {
                        bookingPieceDetail = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                        //detailList.remove(bookingPieceDetail);
                    }
                }
            } else {
                detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                if (CommonUtils.isNotEmpty(id)) {
                    bookingPieceDetail = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                    //detailList.remove(bookingPieceDetail);
                }
            }
        }

        calculateMeasureAndPiece(detailList, commodityDetailsForm);
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        if (CommonUtils.isNotEmpty(detailId)) {
            request.setAttribute("id", detailId);
        } else {
            request.setAttribute("id", id);
        }
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
        request.setAttribute("packageTypeId", request.getParameter("packageTypeId"));
        request.setAttribute("packageType", request.getParameter("packageType"));
        request.setAttribute("uom", uom);
        request.setAttribute("fileNumberId", fileNumberId);
        request.setAttribute("commodityNo", commodityNo);
//        request.setAttribute("bookingPieceId", bookingPieceId);
        request.setAttribute("bookingPieceDetail", bookingPieceDetail);
        request.setAttribute("commodityDetailsForm", commodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        String id = request.getParameter("id");
        String detailid = request.getParameter("detailid");
        String pieceId = request.getParameter("bookingPieceId");
        String fileNumberId = request.getParameter("fileNumberId");
        String commodityNo = request.getParameter("commodityNo");
        String uom = commodityDetailsForm.getUom();
        //List<LclBookingPieceDetail> detailList = new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", new Long(bookingPieceId));
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List detailList = null;
        CommodityDetailsDAO detailDAO = new CommodityDetailsDAO();
        if (CommonUtils.isNotEmpty(detailid)) {
            LclBookingPieceDetail detail = new CommodityDetailsDAO().findById(new Long(detailid));
            new CommodityDetailsDAO().delete(detail);
            detailList = detailDAO.findDetailProperty("lclBookingPiece.id", new Long(pieceId));
        } else {
            if (lclSession != null && CommonUtils.isNotEmpty(lclSession.getCommodityList())) {
                int i;
                for (i = 0; i < lclSession.getCommodityList().size(); i++) {
                    LclBookingPiece lclBookingPiece = lclSession.getCommodityList().get(i);
                    detailList = lclBookingPiece.getLclBookingPieceDetailList();
                    if (CommonUtils.isNotEmpty(id)) {
                        if (CommonUtils.isNotEmpty(detailList)) {
                            if (CommonUtils.isNotEmpty(lclBookingPiece.getCommNo()) && lclBookingPiece.getCommNo().equals(commodityNo)) {
                                LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                                detailList.remove(Integer.parseInt(id));
                                break;
                            }
                        }
                    }
                }
                if (i == lclSession.getCommodityList().size()) {
                    detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                    if (CommonUtils.isNotEmpty(detailList)) {
                        LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                        detailList.remove(Integer.parseInt(id));
                    }
                }
            } else {
                detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                if (CommonUtils.isNotEmpty(id)) {
                    if (CommonUtils.isNotEmpty(detailList)) {
                        LclBookingPieceDetail lbpd = (LclBookingPieceDetail) detailList.get(Integer.parseInt(id));
                        detailList.remove(Integer.parseInt(id));
                    }
                }
            }
        }
        lclSession.setBookingDetailList(detailList);
        session.setAttribute("lclSession", lclSession);
        calculateMeasureAndPiece(detailList, commodityDetailsForm);
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
        request.setAttribute("packageTypeId", request.getParameter("packageTypeId"));
        request.setAttribute("packageType", request.getParameter("packageType"));
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        request.setAttribute("uom", uom);
        request.setAttribute("commodityNo", commodityNo);
        request.setAttribute("fileNumberId", fileNumberId);
//        request.setAttribute("bookingPieceId", pieceId);
        request.setAttribute("dimFlag", commodityDetailsForm.getDimFlag());
        request.setAttribute("commodityDetailsForm", commodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward updateBookedComm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        BigDecimal metricDivisor = new BigDecimal(35.314);
        BigDecimal weightDivisor = new BigDecimal(2.2046);
        //BigDecimal measureImp = new BigDecimal(0.000);
        BigDecimal weightImp = new BigDecimal(0.000);
        BigDecimal measureMet = new BigDecimal(0.000);
        BigDecimal weightMet = new BigDecimal(0.000);
        BigDecimal measureImp = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalMeasureImp()));
        weightImp = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalWeightImp()));
        measureMet = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalMeasureMet()));
        BigDecimal MeasureMetM = measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        weightMet = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalWeightMet()));
        BigDecimal weightMetM = weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        request.setAttribute("bookedPieceCount", commodityDetailsForm.getTotalPieces());
        if (commodityDetailsForm.getActualUom().equals("I")) {
            request.setAttribute("totalMeasureImp", measureImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalMeasureMet", df.format(measureImp.doubleValue() / 35.314));
            request.setAttribute("totalWeightImp", weightImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalWeightMet", weightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("packId", commodityDetailsForm.getPackageTypeId());
            request.setAttribute("pack", commodityDetailsForm.getPackageType());
        } else if (commodityDetailsForm.getActualUom().equals("M")) {
            request.setAttribute("totalMeasureImp", df.format(MeasureMetM.doubleValue() * 35.314));
            request.setAttribute("totalMeasureMet", measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("totalWeightMet", weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("packId", commodityDetailsForm.getPackageTypeId());
            request.setAttribute("pack", commodityDetailsForm.getPackageType());
        }
        return mapping.findForward(BOOKED_COMMODITY);
    }

    public ActionForward updateActualComm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        BigDecimal metricDivisor = new BigDecimal(35.314);
        BigDecimal weightDivisor = new BigDecimal(2.2046);
        //BigDecimal measureImp = new BigDecimal(0.000);
        BigDecimal weightImp = new BigDecimal(0.000);
        BigDecimal measureMet = new BigDecimal(0.000);
        BigDecimal weightMet = new BigDecimal(0.000);
        BigDecimal measureImp = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalMeasureImp()));
        weightImp = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalWeightImp()));
        measureMet = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalMeasureMet()));
        BigDecimal MeasureMetM = measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        weightMet = new BigDecimal(Double.parseDouble(commodityDetailsForm.getTotalWeightMet()));
        BigDecimal weightMetM = weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR);
        request.setAttribute("actualPieceCount", commodityDetailsForm.getTotalPieces());
        if (commodityDetailsForm.getActualUom().equals("I")) {
            request.setAttribute("actualMeasureImp", measureImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualMeasureMet", df.format(measureImp.doubleValue() / 35.314));
            request.setAttribute("actualWeightImp", weightImp.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightMet", weightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR));
        } else if (commodityDetailsForm.getActualUom().equals("M")) {
            request.setAttribute("actualMeasureImp", df.format(MeasureMetM.doubleValue() * 35.314));
            request.setAttribute("actualMeasureMet", measureMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightImp", weightMetM.multiply(weightDivisor).divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
            request.setAttribute("actualWeightMet", weightMet.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_FLOOR));
        }
        return mapping.findForward(ACTUAL_COMMODITY);
    }

    public void calculateMeasureAndPiece(List detailList, CommodityDetailsForm commodityDetailsForm) {
        BigDecimal totalMeasure = new BigDecimal(0.000);
        BigDecimal totalMeasureM = new BigDecimal(0.000);
        Integer totalPieces = 0;
        BigDecimal totalWeightImp = new BigDecimal(0.000);
        BigDecimal totalWeightImpM = new BigDecimal(0.000);
        BigDecimal totalWeightMet = new BigDecimal(0.000);
        BigDecimal totalWeightMetM = new BigDecimal(0.000);
        for (Object obj : detailList) {
            LclBookingPieceDetail lbpd = (LclBookingPieceDetail) obj;
            totalMeasure = totalMeasure.add(lbpd.getActualLength().multiply(lbpd.getActualWidth()).multiply(lbpd.getActualHeight()).multiply(new BigDecimal(lbpd.getPieceCount())));
            BigDecimal devisior = new BigDecimal(1728);
            BigDecimal metricDivisor = new BigDecimal(35.314);
            BigDecimal measureImp = totalMeasure.divide(devisior, 3, BigDecimal.ROUND_CEILING);
            BigDecimal measureMet = measureImp.divide(metricDivisor, 3, BigDecimal.ROUND_CEILING);
            totalMeasureM = totalMeasureM.add((lbpd.getActualLength().multiply(new BigDecimal(0.3937))).multiply(lbpd.getActualWidth().multiply(new BigDecimal(0.3937))).multiply(lbpd.getActualHeight().multiply(new BigDecimal(0.3937))).multiply(new BigDecimal(lbpd.getPieceCount())));
            BigDecimal measureImpM = totalMeasureM.divide(devisior, 3, BigDecimal.ROUND_CEILING);
            BigDecimal measureMetM = measureImpM.divide(metricDivisor, 3, BigDecimal.ROUND_CEILING);
            totalPieces += lbpd.getPieceCount();
            if (lbpd.getActualWeight() != null) {
                totalWeightImp = totalWeightImp.add(lbpd.getActualWeight().multiply(new BigDecimal(lbpd.getPieceCount())));
                totalWeightImpM = totalWeightImpM.add((lbpd.getActualWeight().multiply(new BigDecimal(0.3937))).multiply(new BigDecimal(lbpd.getPieceCount())));
                totalWeightImpM = totalWeightImpM.divide(new BigDecimal(1.000), 3, BigDecimal.ROUND_CEILING);
            }
            BigDecimal weightDivisor = new BigDecimal(2.2046);
            totalWeightMet = totalWeightImp.divide(weightDivisor, 3, BigDecimal.ROUND_CEILING);
            totalWeightMetM = totalWeightImpM.divide(weightDivisor, 3, BigDecimal.ROUND_CEILING);
//            int roundInt = (int) Math.round(measureMetM.doubleValue());
            commodityDetailsForm.setTotalWeightImp("" + totalWeightImp);
            commodityDetailsForm.setTotalWeightMet("" + totalWeightMetM);
            commodityDetailsForm.setTotalMeasureImp("" + measureImp);
            commodityDetailsForm.setTotalMeasureMet("" + measureMetM);
            LclBookingPieceDetail detail = (LclBookingPieceDetail) detailList.get(0);
            if (detail.getActualUom() != null && !detail.getActualUom().equals("")) {
                if (detail.getActualUom().equals("I")) {
                    commodityDetailsForm.setDuptotalWeightImp("" + totalWeightImp);
                    commodityDetailsForm.setDuptotalWeightMet("" + totalWeightMet);
                    commodityDetailsForm.setDuptotalMeasureImp("" + measureImp);
                    commodityDetailsForm.setDuptotalMeasureMet("" + measureMet);
                } else {
                    commodityDetailsForm.setDuptotalWeightImp("" + totalWeightImpM);
                    commodityDetailsForm.setDuptotalWeightMet("" + totalWeightMetM);
                    commodityDetailsForm.setDuptotalMeasureImp("" + measureImpM);
                    commodityDetailsForm.setDuptotalMeasureMet("" + measureMetM);
                }
            }
        }
        commodityDetailsForm.setTotalPieces(totalPieces);
    }

    public void displayListImp(List detailList, HttpServletRequest request) {
        List detailListImp = new LinkedList();
        HttpSession session = request.getSession();
        for (Object obj : detailList) {
            LclBookingPieceDetail pieceDetail = (LclBookingPieceDetail) obj;
            if (pieceDetail.getActualUom().equals("M")) {
                LclBookingPieceDetail pieceNew = new LclBookingPieceDetail();
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

    public void displayListMet(List<LclBookingPieceDetail> detailList, HttpServletRequest request) {
        List detailListMet = new LinkedList();
        for (Object obj : detailList) {
            LclBookingPieceDetail pieceDetail = (LclBookingPieceDetail) obj;
            if (pieceDetail.getActualUom().equals("I")) {
                LclBookingPieceDetail pieceNew = new LclBookingPieceDetail();
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
    /*   public void convertWeight(CommodityDetailsForm commodityDetailsForm) {
     BigDecimal metricDivisor = new BigDecimal(2.2046);
     if (CommonUtils.isNotEmpty(commodityDetailsForm.getActualWeight())) {
     if (commodityDetailsForm.getActualUom().equals(new LclDwr().isMetricOrImperial(commodityDetailsForm.getBookingPieceId()))) {
     BigDecimal weightMet = new BigDecimal(commodityDetailsForm.getActualWeight()).divide(metricDivisor, 3, BigDecimal.ROUND_FLOOR);
     commodityDetailsForm.setActualWeight("" + weightMet);
     }
     }
    
     }*/

    public ActionForward deleteAllPiece(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        String bookingPieceId = request.getParameter("bookingPieceId");
        if (CommonUtils.isNotEmpty(bookingPieceId) && !bookingPieceId.equals("0")) {
            List<LclBookingPieceDetail> bookingList = new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", Long.parseLong(bookingPieceId));
            if (CommonUtils.isNotEmpty(bookingList)) {
                for (LclBookingPieceDetail detail : bookingList) {
                    new CommodityDetailsDAO().delete(detail);
                }
            }
        }
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        if (lclSession != null) {
            session.removeAttribute("lclSession");
        }
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
        request.setAttribute("commodityDetailsForm", commodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward displayMultipleDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityDetailsForm = (CommodityDetailsForm) form;
        request.setAttribute("parentRowLen", request.getParameter("rowLength"));
        request.setAttribute("commodityDetailsForm", commodityDetailsForm);
        return mapping.findForward("multipleDims");
    }

    public ActionForward saveMultiDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDetailsForm commodityForm = (CommodityDetailsForm) form;
        Date now = new Date();
        String actUom = commodityForm.getUom();
        User user = getCurrentUser(request);
        HttpSession session = request.getSession();
        LclUtils lclUtils = new LclUtils();
        List<LclBookingPieceDetail> detailList = null;
        LclBookingPiece piece = null;
        CommodityDetailsDAO commodityDetailsDAO = new CommodityDetailsDAO();

        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String[] lengthDims = commodityForm.getLengthDims().split(",");
        String[] widthDims = commodityForm.getAcWidthDims().split(",");
        String[] heightDims = commodityForm.getHeightDims().split(",");
        String[] piecesDims = commodityForm.getPiecesDims().split(",");
        String[] weightDims = commodityForm.getWeightDims().split(",");
        String[] wareHouseDims = commodityForm.getWarehouseTabDims().split(",");
        boolean stFlag = false;

        if (CommonUtils.isNotEmpty(commodityForm.getBookingPieceId()) && CommonUtils.isNotEmpty(commodityForm.getFileNumberId())) {
            piece = new LclBookingPieceDAO().findById(commodityForm.getBookingPieceId());
        }
        for (int i = 0; i < lengthDims.length; i++) {
            LclBookingPieceDetail lclBookingPieceDetail = new LclBookingPieceDetail();
            lclBookingPieceDetail.setActualUom(actUom);
            lclBookingPieceDetail.setEnteredBy(user);
            lclBookingPieceDetail.setModifiedBy(user);
            lclBookingPieceDetail.setEnteredDatetime(now);
            lclBookingPieceDetail.setModifiedDatetime(now);
            lclBookingPieceDetail.setActualLength(new BigDecimal(lengthDims[i]));
            lclBookingPieceDetail.setActualWidth(new BigDecimal(widthDims[i]));
            lclBookingPieceDetail.setActualHeight(new BigDecimal(heightDims[i]));
            lclBookingPieceDetail.setPieceCount(Integer.parseInt(piecesDims[i]));
            if (weightDims.length > i) {
                lclBookingPieceDetail.setActualWeight(!weightDims[i].isEmpty() ? new BigDecimal(weightDims[i]) : null);
            } else {
                lclBookingPieceDetail.setActualWeight(null);
            }
            if (wareHouseDims.length > i) {
                lclBookingPieceDetail.setStowedLocation(!wareHouseDims[i].isEmpty() ? wareHouseDims[i] : "");
            } else {
                lclBookingPieceDetail.setStowedLocation("");
            }
            if (CommonUtils.isNotEmpty(commodityForm.getBookingPieceId()) && CommonUtils.isNotEmpty(commodityForm.getFileNumberId())) {
                lclBookingPieceDetail.setLclBookingPiece(piece);
                commodityDetailsDAO.save(lclBookingPieceDetail);
            } else if (!stFlag) {
                stFlag = true;
                detailList = null != lclSession.getBookingDetailList() ? lclSession.getBookingDetailList() : new LinkedList();
                detailList.add(lclBookingPieceDetail);
            } else {
                detailList.add(lclBookingPieceDetail);
            }
            lclUtils.addOverSizeRemarks(actUom, heightDims[i], widthDims[i], lengthDims[i], commodityForm.getFileNumberId(), request, lclSession, user);
        }

        if (CommonUtils.isNotEmpty(commodityForm.getBookingPieceId()) && CommonUtils.isNotEmpty(commodityForm.getFileNumberId())) {
            if (lclSession.getBookingDetailList() != null && lclSession.getBookingDetailList().size() > 0) {
                lclSession.getBookingDetailList().removeAll(lclSession.getBookingDetailList());
            }
            detailList = commodityDetailsDAO.findDetailProperty("lclBookingPiece.id", commodityForm.getBookingPieceId());
            lclSession.setBookingDetailList(detailList);
        } else {
            lclSession.setBookingDetailList(detailList);
        }
        session.setAttribute("lclSession", lclSession);

        if (CommonUtils.isNotEmpty(detailList)) {
            calculateMeasureAndPiece(detailList, commodityForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
            LclBookingPieceDetail detail = (LclBookingPieceDetail) detailList.get(0);
            request.setAttribute("uom", detail.getActualUom());
        }
        request.setAttribute("dimFlag", request.getParameter("dimFlag"));
        request.setAttribute("bookingPieceId", request.getParameter("bookingPieceId"));
        request.setAttribute("commodityDetailsForm", commodityForm);
        return mapping.findForward(SUCCESS);
    }
}
