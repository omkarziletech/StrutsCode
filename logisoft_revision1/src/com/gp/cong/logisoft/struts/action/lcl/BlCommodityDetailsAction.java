/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlCommodityDetailsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.BlCommodityDetailsForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class BlCommodityDetailsAction extends LogiwareDispatchAction {

    private static String BOOKED_COMMODITY = "bookedCommodity";
    private static String ACTUAL_COMMODITY = "actualCommodity";

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlCommodityDetailsForm blCommodityDetailsForm = (BlCommodityDetailsForm) form;
        if (CommonUtils.isNotEmpty(blCommodityDetailsForm.getBlPieceId())) {
            List detailList = new BlCommodityDetailsDAO().findByProperty("lclBlPiece.id", blCommodityDetailsForm.getBlPieceId());
            if (CommonUtils.isNotEmpty(detailList)) {
                for (int i = 0; i < detailList.size(); i++) {
                    LclBlPieceDetail lclBlPieceDetail = (LclBlPieceDetail) detailList.get(i);
                    blCommodityDetailsForm.setUom(lclBlPieceDetail.getActualUom());
                    break;
                }
            }
            calculateMeasureAndPiece(detailList, blCommodityDetailsForm);
            displayListImp(detailList, request);
            displayListMet(detailList, request);
        }
        request.setAttribute("blCommodityDetailsForm", blCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlCommodityDetailsForm blCommodityDetailsForm = (BlCommodityDetailsForm) form;
        BlCommodityDetailsDAO blCommodityDetailsDAO = new BlCommodityDetailsDAO();
        blCommodityDetailsForm.getLclBlPieceDetail().setEnteredBy(getCurrentUser(request));
        blCommodityDetailsForm.getLclBlPieceDetail().setModifiedBy(getCurrentUser(request));
        blCommodityDetailsForm.getLclBlPieceDetail().setEnteredDatetime(new Date());
        blCommodityDetailsForm.getLclBlPieceDetail().setModifiedDatetime(new Date());
        blCommodityDetailsForm.getLclBlPieceDetail().setLclBlPiece(new LclBlPiece(blCommodityDetailsForm.getBlPieceId()));
        blCommodityDetailsDAO.saveOrUpdate(blCommodityDetailsForm.getLclBlPieceDetail());
        List detailList = blCommodityDetailsDAO.findByProperty("lclBlPiece.id", blCommodityDetailsForm.getBlPieceId());
        calculateMeasureAndPiece(detailList, blCommodityDetailsForm);
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        request.setAttribute("blCommodityDetailsForm", blCommodityDetailsForm);
         request.setAttribute("editFlag", "True");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlCommodityDetailsForm blCommodityDetailsForm = (BlCommodityDetailsForm) form;
        LclBlPieceDetail lclBlPieceDetail = blCommodityDetailsForm.getLclBlPieceDetail();
        List detailList = new BlCommodityDetailsDAO().findByProperty("lclBlPiece.id", blCommodityDetailsForm.getBlPieceId());
        calculateMeasureAndPiece(detailList, blCommodityDetailsForm);
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        request.setAttribute("blPieceDetail", lclBlPieceDetail);
        request.setAttribute("blCommodityDetailsForm", blCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlCommodityDetailsForm blCommodityDetailsForm = (BlCommodityDetailsForm) form;
        BlCommodityDetailsDAO blCommodityDetailsDAO = new BlCommodityDetailsDAO();
        LclBlPieceDetail lclBlPieceDetail = blCommodityDetailsForm.getLclBlPieceDetail();
        blCommodityDetailsDAO.delete(lclBlPieceDetail);
        List detailList = new BlCommodityDetailsDAO().findByProperty("lclBlPiece.id", blCommodityDetailsForm.getBlPieceId());
        calculateMeasureAndPiece(detailList, blCommodityDetailsForm);
        displayListImp(detailList, request);
        displayListMet(detailList, request);
        request.setAttribute("blCommodityDetailsForm", blCommodityDetailsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward updateBookedComm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlCommodityDetailsForm blCommodityDetailsForm = (BlCommodityDetailsForm) form;
        request.setAttribute("bookedPieceCount", blCommodityDetailsForm.getTotalPieces());
        request.setAttribute("totalMeasureImp", blCommodityDetailsForm.getTotalMeasureImp());
        request.setAttribute("totalMeasureMet", blCommodityDetailsForm.getTotalMeasureMet());
        request.setAttribute("totalWeightImp", blCommodityDetailsForm.getTotalWeightImp());
        request.setAttribute("totalWeightMet", blCommodityDetailsForm.getTotalWeightMet());
        return mapping.findForward(BOOKED_COMMODITY);
    }

    public ActionForward updateActualComm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlCommodityDetailsForm blCommodityDetailsForm = (BlCommodityDetailsForm) form;
        request.setAttribute("actualPieceCount", blCommodityDetailsForm.getTotalPieces());
        request.setAttribute("actualWeightImp", blCommodityDetailsForm.getTotalWeightImp()!=null ? new BigDecimal(blCommodityDetailsForm.getTotalWeightImp()).setScale(2, BigDecimal.ROUND_HALF_UP):blCommodityDetailsForm.getTotalWeightImp());
        request.setAttribute("actualWeightMet", blCommodityDetailsForm.getTotalWeightMet());
        request.setAttribute("actualMeasureImp", blCommodityDetailsForm.getTotalMeasureImp()!=null ? new BigDecimal(blCommodityDetailsForm.getTotalMeasureImp()).setScale(2, BigDecimal.ROUND_HALF_UP):blCommodityDetailsForm.getTotalMeasureImp());
        request.setAttribute("actualMeasureMet", blCommodityDetailsForm.getTotalMeasureMet());
        return mapping.findForward(ACTUAL_COMMODITY);
    }

    public void calculateMeasureAndPiece(List detailList, BlCommodityDetailsForm blCommodityDetailsForm) {
        BigDecimal totalMeasure = new BigDecimal(0.000);
        Integer totalPieces = 0;
        BigDecimal totalWeightImp = new BigDecimal(0.000);
        BigDecimal totalWeightMet = new BigDecimal(0.000);
        
        for (Object obj : detailList) {
            LclBlPieceDetail lblpd = (LclBlPieceDetail) obj;
            totalMeasure = totalMeasure.add(lblpd.getActualLength().multiply(lblpd.getActualWidth()).multiply(lblpd.getActualHeight()).multiply(new BigDecimal(lblpd.getPieceCount())));
            BigDecimal devisior = new BigDecimal(1728);
            BigDecimal metricDivisor = new BigDecimal(35.3146);
            BigDecimal measureImp = totalMeasure.divide(devisior, 3, BigDecimal.ROUND_CEILING);
            BigDecimal measureMet = measureImp.divide(metricDivisor, 3, BigDecimal.ROUND_FLOOR);
            totalPieces += lblpd.getPieceCount();
            if (lblpd.getActualWeight() != null) {
                totalWeightImp = totalWeightImp.add(lblpd.getActualWeight().multiply(new BigDecimal(lblpd.getPieceCount())));
            }
            BigDecimal weightDivisor = new BigDecimal(2.2046);
            totalWeightMet = totalWeightImp.divide(weightDivisor, 3, BigDecimal.ROUND_FLOOR);
            blCommodityDetailsForm.setTotalWeightImp("" + totalWeightImp);
            blCommodityDetailsForm.setTotalWeightMet("" + totalWeightMet);
            blCommodityDetailsForm.setTotalMeasureImp("" + measureImp);
            blCommodityDetailsForm.setTotalMeasureMet("" + measureMet);
            blCommodityDetailsForm.setTotalPieces(totalPieces);
        }

    }

    public void displayListImp(List detailList, HttpServletRequest request) {
        List detailListImp = new LinkedList();
        for (Object obj : detailList) {
            LclBlPieceDetail pieceDetail = (LclBlPieceDetail) obj;
            if (pieceDetail.getActualUom().equals("M")) {
                LclBlPieceDetail pieceNew = new LclBlPieceDetail();
                pieceNew.setActualLength(pieceDetail.getActualLength());
                pieceNew.setActualWidth(pieceDetail.getActualWidth());
                pieceNew.setActualHeight(pieceDetail.getActualHeight());
                pieceNew.setPieceCount(pieceDetail.getPieceCount());
                pieceNew.setActualUom(pieceDetail.getActualUom());
                pieceNew.setStowedLocation(pieceDetail.getStowedLocation());
                if (pieceDetail.getActualWeight() != null) {
                    pieceNew.setActualWeight(pieceDetail.getActualWeight().multiply(new BigDecimal(2.2046)).setScale(3, BigDecimal.ROUND_FLOOR));
                }
                detailListImp.add(pieceNew);
            } else {
                detailListImp.add(pieceDetail);
            }
            request.setAttribute("detailListImp", detailListImp);
        }
    }

    public void displayListMet(List<LclBlPieceDetail> detailList, HttpServletRequest request) {
        List detailListMet = new LinkedList();
        for (Object obj : detailList) {
            LclBlPieceDetail pieceDetail = (LclBlPieceDetail) obj;
            if (pieceDetail.getActualUom().equals("I")) {
                LclBlPieceDetail pieceNew = new LclBlPieceDetail();
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
}
