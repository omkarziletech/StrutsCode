/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.hibernate.dao.lcl.QuoteCommodityDetailsDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class QuoteCommodityDetailsForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(QuoteCommodityDetailsForm.class);
    private LclQuotePieceDetail lclQuotePieceDetail;
    private String totalMeasureImp;
    private String duptotalMeasureImp;
    private String totalMeasureMet;
    private String duptotalMeasureMet;
    private String totalWeightImp;
    private String fileNumberId;
    private Long bookingPieceId;
    private String duptotalWeightImp;
    private String totalWeightMet;
    private String duptotalWeightMet;
    private Integer totalPieces;
    private Integer id;
    private String dimFlag;
    private String warehouse;
    private String uom;
    private String ActUom;
    private String commodityNo;
    private Long qtPieceDetailId;
    private String moduleName;
    private String fileNumber;
    private boolean ups;
    private String lengthDims;
    private String acWidthDims;
    private String heightDims;
    private String piecesDims;
    private String weightDims;
    private String warehouseTabDims;
    private String editDetailId;

    public QuoteCommodityDetailsForm() {
        if (lclQuotePieceDetail == null) {
            lclQuotePieceDetail = new LclQuotePieceDetail();
        }
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != 0) {
            this.id = id;
        }
    }

    public String getDuptotalMeasureImp() {
        return duptotalMeasureImp;
    }

    public void setDuptotalMeasureImp(String duptotalMeasureImp) {
        this.duptotalMeasureImp = duptotalMeasureImp;
    }

    public String getDuptotalMeasureMet() {
        return duptotalMeasureMet;
    }

    public void setDuptotalMeasureMet(String duptotalMeasureMet) {
        this.duptotalMeasureMet = duptotalMeasureMet;
    }

    public String getDuptotalWeightImp() {
        return duptotalWeightImp;
    }

    public void setDuptotalWeightImp(String duptotalWeightImp) {
        this.duptotalWeightImp = duptotalWeightImp;
    }

    public String getDuptotalWeightMet() {
        return duptotalWeightMet;
    }

    public void setDuptotalWeightMet(String duptotalWeightMet) {
        this.duptotalWeightMet = duptotalWeightMet;
    }

    public LclQuotePieceDetail getLclQuotePieceDetail() {
        return lclQuotePieceDetail;
    }

    public void setLclQuotePieceDetail(LclQuotePieceDetail lclQuotePieceDetail) {
        this.lclQuotePieceDetail = lclQuotePieceDetail;
    }

    public Long getBookingPieceId() {
        return bookingPieceId;
    }

    public void setBookingPieceId(Long bookingPieceId) {
        this.bookingPieceId = bookingPieceId;
    }

//    public Long getBookingPieceId() {
//        return lclQuotePieceDetail.getQuotePiece().getId();
//    }
//
//    public void setBookingPieceId(Long bookingPieceId) {
//        if (lclQuotePieceDetail.getQuotePiece() != null) {
//            lclQuotePieceDetail.getQuotePiece().setId(bookingPieceId);
//        }
//    }
    public String getPieceCount() {
        if (lclQuotePieceDetail.getPieceCount() != null) {
            return "" + lclQuotePieceDetail.getPieceCount();
        }
        return "";
    }

    public void setPieceCount(String pieceCount) {
        if (CommonUtils.isNotEmpty(pieceCount)) {
            lclQuotePieceDetail.setPieceCount(new Integer(pieceCount));
        }
    }

    public String getActualUom() {
        if (lclQuotePieceDetail.getActualUom() != null && lclQuotePieceDetail.getActualUom().equalsIgnoreCase("M")) {
            return "M";
        }
        return "I";
    }

    public void setActualUom(String actualUom) {
//        if (lclQuotePieceDetail == null) {
//            lclQuotePieceDetail = new LclQuotePieceDetail();
//        }
        lclQuotePieceDetail.setActualUom(actualUom);
    }

    public String getActualLength() {
        if (lclQuotePieceDetail.getActualLength() != null) {
            return "" + lclQuotePieceDetail.getActualLength();
        }
        return "";
    }

    public void setActualLength(String actualLength) {
        if (CommonUtils.isNotEmpty(actualLength)) {
            lclQuotePieceDetail.setActualLength(new BigDecimal(actualLength));
        }
    }

    public String getActualWidth() {
        if (lclQuotePieceDetail.getActualWidth() != null) {
            return "" + lclQuotePieceDetail.getActualWidth();
        }
        return "";
    }

    public void setActualWidth(String actualWidth) {
        if (CommonUtils.isNotEmpty(actualWidth)) {
            lclQuotePieceDetail.setActualWidth(new BigDecimal(actualWidth));
        }
    }

    public String getActualHeight() {
        if (lclQuotePieceDetail.getActualHeight() != null) {
            return "" + lclQuotePieceDetail.getActualHeight();
        }
        return "";
    }

    public void setActualHeight(String actualHeight) {
        if (CommonUtils.isNotEmpty(actualHeight)) {
            lclQuotePieceDetail.setActualHeight(new BigDecimal(actualHeight));
        }
    }

    public String getActualWeight() {
        if (lclQuotePieceDetail.getActualWeight() != null) {
            return "" + lclQuotePieceDetail.getActualWeight();
        }
        return "";
    }

    public void setActualWeight(String actualWeight) {
        if (CommonUtils.isNotEmpty(actualWeight)) {
            lclQuotePieceDetail.setActualWeight(new BigDecimal(actualWeight));
        }
    }

    public String getStowedLocation() {
        return lclQuotePieceDetail.getStowedLocation();
    }

    public void setStowedLocation(String stowedLocation) {
        lclQuotePieceDetail.setStowedLocation(stowedLocation);
    }

    public String getMarkNoDesc() {
        return lclQuotePieceDetail.getMarkNoDesc();
    }

    public void setMarkNoDesc(String markNoDesc) {
        lclQuotePieceDetail.setMarkNoDesc(markNoDesc);
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
        return lclQuotePieceDetail.getStowedLocation();
    }

    public void setWarehouse(String warehouse) {
        lclQuotePieceDetail.setStowedLocation(warehouse);
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

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public Long getQtPieceDetailId() {
        return qtPieceDetailId;
    }

    public void setQtPieceDetailId(Long qtPieceDetailId) {
        this.qtPieceDetailId = qtPieceDetailId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public boolean getUps() {
        return ups;
    }

    public void setUps(boolean ups) {
        this.ups = ups;
    }

    public String getLengthDims() {
        return lengthDims;
    }

    public void setLengthDims(String lengthDims) {
        this.lengthDims = lengthDims;
    }

    public String getAcWidthDims() {
        return acWidthDims;
    }

    public void setAcWidthDims(String acWidthDims) {
        this.acWidthDims = acWidthDims;
    }

    public String getHeightDims() {
        return heightDims;
    }

    public void setHeightDims(String heightDims) {
        this.heightDims = heightDims;
    }

    public String getPiecesDims() {
        return piecesDims;
    }

    public void setPiecesDims(String piecesDims) {
        this.piecesDims = piecesDims;
    }

    public String getWeightDims() {
        return weightDims;
    }

    public void setWeightDims(String weightDims) {
        this.weightDims = weightDims;
    }

    public String getWarehouseTabDims() {
        return warehouseTabDims;
    }

    public void setWarehouseTabDims(String warehouseTabDims) {
        this.warehouseTabDims = warehouseTabDims;
    }

    public String getEditDetailId() {
        return editDetailId;
    }

    public void setEditDetailId(String editDetailId) {
        this.editDetailId = editDetailId;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        String bookingPieceDetailsId = request.getParameter("qtPieceDetailId");
        if (CommonUtils.isNotEmpty(bookingPieceDetailsId) && !bookingPieceDetailsId.equalsIgnoreCase("0")) {
            try {
                lclQuotePieceDetail = new QuoteCommodityDetailsDAO().findById(Long.parseLong(bookingPieceDetailsId));
            } catch (Exception ex) {
                log.info("reset()in QuoteCommodityDetailsForm failed on " + new Date(), ex);
            }
        }
        if (lclQuotePieceDetail == null) {
            lclQuotePieceDetail = new LclQuotePieceDetail();
        }
    }
}
