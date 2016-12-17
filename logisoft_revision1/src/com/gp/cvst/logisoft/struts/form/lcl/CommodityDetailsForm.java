/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.lcl.CommodityDetailsDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class CommodityDetailsForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(CommodityDetailsForm.class);
    private LclBookingPieceDetail lclBookingPieceDetail;
    private String totalMeasureImp;
    private String duptotalMeasureImp;
    private String totalMeasureMet;
    private String duptotalMeasureMet;
    private Long fileNumberId;
    private String totalWeightImp;
    private String duptotalWeightImp;
    private String totalWeightMet;
    private String duptotalWeightMet;
    private Integer totalPieces;
    private String commodityNo;
    private Long bookingPieceId;
    private Integer id;
    private String dimFlag;
    private String warehouse;
    private String uom;
    private String ActUom;
    private String packageTypeId;
    private String packageType;
    private String fileNumber;
    private String ups;
    private String saveRemarks ;
    private String lengthDims;
    private String acWidthDims;
    private String heightDims;
    private String piecesDims;
    private String weightDims;
    private String warehouseTabDims; 
     
    public CommodityDetailsForm() {
        if (lclBookingPieceDetail == null) {
            lclBookingPieceDetail = new LclBookingPieceDetail();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != 0) {
            this.id = id;
        }
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public LclBookingPieceDetail getLclBookingPieceDetail() {
        return lclBookingPieceDetail;
    }

    public void setLclBookingPieceDetail(LclBookingPieceDetail lclBookingPieceDetail) {
        this.lclBookingPieceDetail = lclBookingPieceDetail;
    }

    public Long getBookingPieceId() {
        return bookingPieceId;
    }

    public void setBookingPieceId(Long bookingPieceId) {
        this.bookingPieceId = bookingPieceId;
    }

//    public Long getBookingPieceId() {
//        return lclBookingPieceDetail.getLclBookingPiece().getId();
//    }
//
//    public void setBookingPieceId(Long bookingPieceId) {
//        if (lclBookingPieceDetail.getLclBookingPiece() != null) {
//            lclBookingPieceDetail.getLclBookingPiece().setId(bookingPieceId);
//        }
//    }
    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getPieceCount() {
        if (lclBookingPieceDetail.getPieceCount() != null) {
            return "" + lclBookingPieceDetail.getPieceCount();
        }
        return "";
    }

    public void setPieceCount(String pieceCount) {
        if (CommonUtils.isNotEmpty(pieceCount)) {
            lclBookingPieceDetail.setPieceCount(new Integer(pieceCount));
        }
    }

    public String getActualUom() {
        if (lclBookingPieceDetail.getActualUom() != null && lclBookingPieceDetail.getActualUom().equalsIgnoreCase("M")) {
            return "M";
        }
        return "I";
    }

    public void setActualUom(String actualUom) {
        lclBookingPieceDetail.setActualUom(actualUom);
    }

    public String getActualLength() {
        if (lclBookingPieceDetail.getActualLength() != null) {
            return "" + lclBookingPieceDetail.getActualLength();
        }
        return "";
    }

    public void setActualLength(String actualLength) {
        if (CommonUtils.isNotEmpty(actualLength)) {
            lclBookingPieceDetail.setActualLength(new BigDecimal(actualLength));
        }
    }

    public String getActualWidth() {
        if (lclBookingPieceDetail.getActualWidth() != null) {
            return "" + lclBookingPieceDetail.getActualWidth();
        }
        return "";
    }

    public void setActualWidth(String actualWidth) {
        if (CommonUtils.isNotEmpty(actualWidth)) {
            lclBookingPieceDetail.setActualWidth(new BigDecimal(actualWidth));
        }
    }

    public String getActualHeight() {
        if (lclBookingPieceDetail.getActualHeight() != null) {
            return "" + lclBookingPieceDetail.getActualHeight();
        }
        return "";
    }

    public void setActualHeight(String actualHeight) {
        if (CommonUtils.isNotEmpty(actualHeight)) {
            lclBookingPieceDetail.setActualHeight(new BigDecimal(actualHeight));
        }
    }

    public String getActualWeight() {
        if (lclBookingPieceDetail.getActualWeight() != null) {
            return "" + lclBookingPieceDetail.getActualWeight();
        }
        return "";
    }

    public void setActualWeight(String actualWeight) {
        if (CommonUtils.isNotEmpty(actualWeight)) {
            lclBookingPieceDetail.setActualWeight(new BigDecimal(actualWeight));
        }
    }

    public String getStowedLocation() {
        return lclBookingPieceDetail.getStowedLocation();
    }

    public void setStowedLocation(String stowedLocation) {
        lclBookingPieceDetail.setStowedLocation(stowedLocation);
    }

    public String getMarkNoDesc() {
        return lclBookingPieceDetail.getMarkNoDesc();
    }

    public void setMarkNoDesc(String markNoDesc) {
        lclBookingPieceDetail.setMarkNoDesc(markNoDesc);
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
        return lclBookingPieceDetail.getStowedLocation();
    }

    public void setWarehouse(String warehouse) {
        lclBookingPieceDetail.setStowedLocation(warehouse);
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

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(String packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getSaveRemarks() {
        return saveRemarks;
    }

    public void setSaveRemarks(String saveRemarks) {
        this.saveRemarks = saveRemarks;
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

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        String bookingPieceDetailsId = request.getParameter("id");
        if (CommonUtils.isNotEmpty(bookingPieceDetailsId)) {
            try {
                lclBookingPieceDetail = new CommodityDetailsDAO().findById(Long.parseLong(bookingPieceDetailsId));
            } catch (Exception ex) {
                log.info("reset() in CommodityDetailsForm failed on " + new Date(), ex);
            }
        }
        if (lclBookingPieceDetail == null) {
            lclBookingPieceDetail = new LclBookingPieceDetail();
        }
    }
}
