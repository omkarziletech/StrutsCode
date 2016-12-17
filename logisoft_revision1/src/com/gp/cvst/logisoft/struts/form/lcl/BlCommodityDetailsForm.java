/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlCommodityDetailsDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class BlCommodityDetailsForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(BlCommodityDetailsForm.class);
    private LclBlPieceDetail lclBlPieceDetail;
    private String totalMeasureImp;
    private String totalMeasureMet;
    private String totalWeightImp;
    private String totalWeightMet;
    private Integer totalPieces;
    private Long pieceDetailId;
    private String dimFlag;
    private String warehouse;
    private String uom;
    private String ActUom;
    private Long blPieceId;
    private String fileNumber;

    public BlCommodityDetailsForm() {
        if (lclBlPieceDetail == null) {
            lclBlPieceDetail = new LclBlPieceDetail();
        }
        if (lclBlPieceDetail.getLclBlPiece() == null) {
            lclBlPieceDetail.setLclBlPiece(new LclBlPiece());
        }
    }

    public String getDimFlag() {
        return dimFlag;
    }

    public void setDimFlag(String dimFlag) {
        this.dimFlag = dimFlag;
    }

    public String getTotalWeightImp() {
        return totalWeightImp;
    }

    public void setTotalWeightImp(String totalWeightImp) {
        this.totalWeightImp = totalWeightImp;
    }

    public String getTotalWeightMet() {
        return totalWeightMet;
    }

    public void setTotalWeightMet(String totalWeightMet) {
        this.totalWeightMet = totalWeightMet;
    }

    public Long getPieceDetailId() {
        return pieceDetailId;
    }

    public void setPieceDetailId(Long pieceDetailId) {
        this.pieceDetailId = pieceDetailId;
    }

    public LclBlPieceDetail getLclBlPieceDetail() {
        return lclBlPieceDetail;
    }

    public void setLclBlPieceDetail(LclBlPieceDetail lclBlPieceDetail) {
        this.lclBlPieceDetail = lclBlPieceDetail;
    }

    public Long getBookingPieceId() {
        return lclBlPieceDetail.getLclBlPiece().getId();
    }

    public void setBookingPieceId(Long bookingPieceId) {
        if (lclBlPieceDetail.getLclBlPiece() != null) {
            lclBlPieceDetail.getLclBlPiece().setId(bookingPieceId);
        }
    }

    public String getPieceCount() {
        if (lclBlPieceDetail.getPieceCount() != null) {
            return "" + lclBlPieceDetail.getPieceCount();
        }
        return "";
    }

    public void setPieceCount(String pieceCount) {
        if (CommonUtils.isNotEmpty(pieceCount)) {
            lclBlPieceDetail.setPieceCount(Integer.parseInt(pieceCount));
        }
    }

    public String getActualUom() {
        if (lclBlPieceDetail.getActualUom() != null && lclBlPieceDetail.getActualUom().equalsIgnoreCase("M")) {
            return "M";
        }
        return "I";
    }

    public void setActualUom(String actualUom) {
        lclBlPieceDetail.setActualUom(actualUom);
    }

    public String getActualLength() {
        if (lclBlPieceDetail.getActualLength() != null) {
            return "" + lclBlPieceDetail.getActualLength();
        }
        return "";
    }

    public void setActualLength(String actualLength) {
        if (CommonUtils.isNotEmpty(actualLength)) {
            lclBlPieceDetail.setActualLength(new BigDecimal(actualLength));
        }
    }

    public String getActualWidth() {
        if (lclBlPieceDetail.getActualWidth() != null) {
            return "" + lclBlPieceDetail.getActualWidth();
        }
        return "";
    }

    public void setActualWidth(String actualWidth) {
        if (CommonUtils.isNotEmpty(actualWidth)) {
            lclBlPieceDetail.setActualWidth(new BigDecimal(actualWidth));
        }
    }

    public String getActualHeight() {
        if (lclBlPieceDetail.getActualHeight() != null) {
            return "" + lclBlPieceDetail.getActualHeight();
        }
        return "";
    }

    public void setActualHeight(String actualHeight) {
        if (CommonUtils.isNotEmpty(actualHeight)) {
            lclBlPieceDetail.setActualHeight(new BigDecimal(actualHeight));
        }
    }

    public String getActualWeight() {
        if (lclBlPieceDetail.getActualWeight() != null) {
            return "" + lclBlPieceDetail.getActualWeight();
        }
        return "";
    }

    public void setActualWeight(String actualWeight) {
        if (CommonUtils.isNotEmpty(actualWeight)) {
            lclBlPieceDetail.setActualWeight(new BigDecimal(actualWeight));
        }
    }

    public String getStowedLocation() {
        return lclBlPieceDetail.getStowedLocation();
    }

    public void setStowedLocation(String stowedLocation) {
        lclBlPieceDetail.setStowedLocation(stowedLocation);
    }

    public String getMarkNoDesc() {
        return lclBlPieceDetail.getMarkNoDesc();
    }

    public void setMarkNoDesc(String markNoDesc) {
        lclBlPieceDetail.setMarkNoDesc(markNoDesc);
    }

    public String getTotalMeasureImp() {
        return totalMeasureImp;
    }

    public void setTotalMeasureImp(String totalMeasureImp) {
        this.totalMeasureImp = totalMeasureImp;
    }

    public String getTotalMeasureMet() {
        return totalMeasureMet;
    }

    public void setTotalMeasureMet(String totalMeasureMet) {
        this.totalMeasureMet = totalMeasureMet;
    }

    public Integer getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Integer totalPieces) {
        this.totalPieces = totalPieces;
    }

    public String getWarehouse() {
        return lclBlPieceDetail.getStowedLocation();
    }

    public void setWarehouse(String warehouse) {
        lclBlPieceDetail.setStowedLocation(warehouse);
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getActUom() {
        return ActUom;
    }

    public void setActUom(String ActUom) {
        this.ActUom = ActUom;
    }

    public Long getBlPieceId() {
        return blPieceId;
    }

    public void setBlPieceId(Long blPieceId) {
        this.blPieceId = blPieceId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        String blPieceDetailsId = request.getParameter("pieceDetailId");
        if (CommonUtils.isNotEmpty(blPieceDetailsId)) {
            try {
                lclBlPieceDetail = new BlCommodityDetailsDAO().findById(Long.parseLong(blPieceDetailsId));
            } catch (Exception ex) {
                log.info("reset() failed on " + new Date(), ex);
            }
        }
        if (lclBlPieceDetail == null) {
            lclBlPieceDetail = new LclBlPieceDetail();
        }
    }
}
