/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import java.util.Date;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author Thamizh
 */
public class LclBlCommodityForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclBlCommodityForm.class);
    private LclBlPiece lclBlPiece;
    private String packageType;
    private String commodityType;
    private String commodityNo;
    private String fileNumber;
    private String blPieceId;//id
    private boolean autoConvert;
    private Long commId;
    private Long fileNumberId;
    private String editDimFlag;
    private String pooUnlocCode;//origin
    private String fdUnlocCode;//destination
    private String rateType;
    private String polUnlocCode;//pol
    private String podUnlocCode;//pod
    private String insurance;
    private String valueOfGoods;
    private String pcBoth;
    private String moduleName;
    private String calcHeavy;
    private boolean ratesRecalFlag;
    private String dimsToolTip;
    private boolean autoConvertMetric;
    private boolean includeDestfees;

    public LclBlCommodityForm() {
        if (lclBlPiece == null) {
            lclBlPiece = new LclBlPiece();
        }
        if (lclBlPiece.getLclFileNumber() == null) {
            lclBlPiece.setLclFileNumber(new LclFileNumber());
        }
    }

    public String getCalcHeavy() {
        return calcHeavy;
    }

    public void setCalcHeavy(String calcHeavy) {
        this.calcHeavy = calcHeavy;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPcBoth() {
        return pcBoth;
    }

    public void setPcBoth(String pcBoth) {
        this.pcBoth = pcBoth;
    }

    public String getValueOfGoods() {
        return valueOfGoods;
    }

    public void setValueOfGoods(String valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getBlPieceId() {
        return blPieceId;
    }

    public void setBlPieceId(String blPieceId) {
        this.blPieceId = blPieceId;
    }

    public String getEditDimFlag() {
        return editDimFlag;
    }

    public void setEditDimFlag(String editDimFlag) {
        this.editDimFlag = editDimFlag;
    }

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getFileNumberId() {
        if (lclBlPiece.getLclFileNumber() != null) {
            return lclBlPiece.getLclFileNumber().getId();
        }
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        if (lclBlPiece.getLclFileNumber() != null) {
            lclBlPiece.getLclFileNumber().setId(fileNumberId);
        }
        this.fileNumberId = fileNumberId;
    }

    public LclBlPiece getLclBlPiece() {
        return lclBlPiece;
    }

    public void setLclBlPiece(LclBlPiece lclBlPiece) {
        this.lclBlPiece = lclBlPiece;
    }

    public String getPieceDesc() {
        return lclBlPiece.getPieceDesc();
    }

    public void setPieceDesc(String pieceDesc) {
        lclBlPiece.setPieceDesc(pieceDesc);
    }

    public String getMarkNoDesc() {
        return lclBlPiece.getMarkNoDesc();
    }

    public void setMarkNoDesc(String markNoDesc) {
        lclBlPiece.setMarkNoDesc(markNoDesc.replaceAll("\n", ""));
    }

    public Boolean getHazmat() {
        return lclBlPiece.getHazmat();
    }

    public void setHazmat(Boolean hazmat) {
        lclBlPiece.setHazmat(hazmat);
    }

    public boolean getIsBarrel() {
        return lclBlPiece.isIsBarrel();
    }

    public void setIsBarrel(boolean isBarrel) {
        lclBlPiece.setIsBarrel(isBarrel);
    }

    public String getPersonalEffects() {
        if (CommonUtils.isEmpty(lclBlPiece.getPersonalEffects())) {
            return N;
        }
        return lclBlPiece.getPersonalEffects();
    }

    public void setPersonalEffects(String personalEffects) {
        lclBlPiece.setPersonalEffects(personalEffects);
    }

    public Boolean getRefrigerationRequired() {
        return lclBlPiece.getRefrigerationRequired();
    }

    public void setRefrigerationRequired(Boolean refrigerationRequired) {
        lclBlPiece.setRefrigerationRequired(refrigerationRequired);
    }

    public Boolean getWeightVerified() {
        return lclBlPiece.getWeightVerified();
    }

    public void setWeightVerified(Boolean weightVerified) {
        lclBlPiece.setWeightVerified(weightVerified);
    }

    public String getHarmonizedCode() {
        return lclBlPiece.getHarmonizedCode();
    }

    public void setHarmonizedCode(String harmonizedCode) {
        lclBlPiece.setHarmonizedCode(harmonizedCode);
    }

    public String getBookedPieceCount() {
        if (lclBlPiece.getActualPieceCount() != null) {
            return "" + lclBlPiece.getActualPieceCount();
        }
        return "";
    }

    public void setBookedPieceCount(String bookedPieceCount) {
        if (CommonUtils.isNotEmpty(bookedPieceCount)) {
            lclBlPiece.setBookedPieceCount(Integer.parseInt(bookedPieceCount));
        } else {
            lclBlPiece.setBookedPieceCount(0);
        }
    }

    public String getBookedWeightImperial() {
        if (lclBlPiece.getBookedWeightImperial() != null) {
            return "" + lclBlPiece.getBookedWeightImperial();
        }
        return "";
    }

    public void setBookedWeightImperial(String bookedWeightImperial) {
        if (CommonUtils.isNotEmpty(bookedWeightImperial)) {
            lclBlPiece.setBookedWeightImperial(new BigDecimal(bookedWeightImperial));
        }
    }

    public String getBookedWeightMetric() {
        if (lclBlPiece.getBookedWeightMetric() != null) {
            return "" + lclBlPiece.getBookedWeightMetric();
        }
        return "";
    }

    public void setBookedWeightMetric(String bookedWeightMetric) {
        if (CommonUtils.isNotEmpty(bookedWeightMetric)) {
            lclBlPiece.setBookedWeightMetric(new BigDecimal(bookedWeightMetric));
        }
    }

    public String getBookedVolumeImperial() {
        if (lclBlPiece.getBookedVolumeImperial() != null) {
            return "" + lclBlPiece.getBookedVolumeImperial();
        }
        return "";
    }

    public void setBookedVolumeImperial(String bookedVolumeImperial) {
        if (CommonUtils.isNotEmpty(bookedVolumeImperial)) {
            lclBlPiece.setBookedVolumeImperial(new BigDecimal(bookedVolumeImperial));
        }
    }

    public String getBookedVolumeMetric() {
        if (lclBlPiece.getBookedVolumeMetric() != null) {
            return "" + lclBlPiece.getBookedVolumeMetric();
        }
        return "";
    }

    public void setBookedVolumeMetric(String bookedVolumeMetric) {
        if (CommonUtils.isNotEmpty(bookedVolumeMetric)) {
            lclBlPiece.setBookedVolumeMetric(new BigDecimal(bookedVolumeMetric));
        }
    }

    public String getActualPieceCount() {
        if (lclBlPiece.getActualPieceCount() != null) {
            return "" + lclBlPiece.getActualPieceCount();
        }
        return "";
    }

    public void setActualPieceCount(String actualPieceCount) {
        if (CommonUtils.isNotEmpty(actualPieceCount)) {
            lclBlPiece.setActualPieceCount(Integer.parseInt(actualPieceCount));
        }
    }

    public String getActualWeightImperial() {
        if (lclBlPiece.getActualWeightImperial() != null) {
            return "" + lclBlPiece.getActualWeightImperial();
        }
        return "";
    }

    public void setActualWeightImperial(String actualWeightImperial) {
        if (CommonUtils.isNotEmpty(actualWeightImperial)) {
            lclBlPiece.setActualWeightImperial(new BigDecimal(actualWeightImperial));
        }
    }

    public String getActualWeightMetric() {
        if (lclBlPiece.getActualWeightMetric() != null) {
            return "" + lclBlPiece.getActualWeightMetric();
        }
        return "";
    }

    public void setActualWeightMetric(String actualWeightMetric) {
        if (CommonUtils.isNotEmpty(actualWeightMetric)) {
            lclBlPiece.setActualWeightMetric(new BigDecimal(actualWeightMetric));
        }
    }

    public String getActualVolumeImperial() {
        if (lclBlPiece.getActualVolumeImperial() != null) {
            return "" + lclBlPiece.getActualVolumeImperial();
        }
        return "";
    }

    public void setActualVolumeImperial(String actualVolumeImperial) {
        if (CommonUtils.isNotEmpty(actualVolumeImperial)) {
            lclBlPiece.setActualVolumeImperial(new BigDecimal(actualVolumeImperial));
        }
    }

    public String getActualVolumeMetric() {
        if (lclBlPiece.getActualVolumeMetric() != null) {
            return "" + lclBlPiece.getActualVolumeMetric();
        }
        return "";
    }

    public void setActualVolumeMetric(String actualVolumeMetric) {
        if (CommonUtils.isNotEmpty(actualVolumeMetric)) {
            lclBlPiece.setActualVolumeMetric(new BigDecimal(actualVolumeMetric));
        }
    }

//    public String getPackageType() {
//        if (lclBookingPiece.getPackageType() != null && lclBookingPiece.getBookedPieceCount() <= 1) {
//            return lclBookingPiece.getPackageType().getDescription();
//        } else if (lclBookingPiece.getPackageType() != null && lclBookingPiece.getBookedPieceCount() > 1) {
//            return lclBookingPiece.getPackageType().getDescription() + "" + lclBookingPiece.getPackageType().getPlural().toLowerCase();
//        } else {
//            return lclBookingPiece.getPkgName();
//        }
//    }
//
//    public void setPackageType(String packageType) {
//        lclBookingPiece.setPkgName(packageType);
//    }
    public String getPackageType() {

        if (lclBlPiece.getPackageType() != null && lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount() <= 1) {
            return lclBlPiece.getPkgName();
        } else if (lclBlPiece.getPkgName() != null && lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount() > 1) {
            return lclBlPiece.getPkgName();
        } else {
            return lclBlPiece.getPkgName();
        }
        //return lclQuotePiece.getPkgName();
    }

    public void setPackageType(String packageType) {
        lclBlPiece.setPkgName(packageType);
    }

    public String getCommodityType() {
        if (lclBlPiece.getCommodityType() != null) {
            return lclBlPiece.getCommodityType().getDescEn();
        } else {
            return lclBlPiece.getCommName();
        }
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
        lclBlPiece.setCommName(commodityType);
    }

    public Long getCommodityTypeId() {
        if (lclBlPiece.getCommodityType() != null) {
            return lclBlPiece.getCommodityType().getId();
        }
        return null;

    }

    public void setCommodityTypeId(Long commodityTypeId) {
        if (CommonUtils.isNotEmpty(commodityTypeId)) {
            CommodityType commodityType = new CommodityType(commodityTypeId);
            //commodityType.setDescEn(lclQuotePiece.getCommName() +"("+lclQuotePiece.getCommNo()+")");
            //commodityType.setDescEn(lclQuotePiece.getCommNo());
            lclBlPiece.setCommodityType(commodityType);
        }
    }

    public Long getPackageTypeId() {
        if (lclBlPiece.getPackageType() != null) {
            return lclBlPiece.getPackageType().getId();
        } else {
            return lclBlPiece.getPkgNo();
        }
    }

    public void setPackageTypeId(Long packageTypeId) {
        if (CommonUtils.isNotEmpty(packageTypeId)) {
            lclBlPiece.setPackageType(new PackageType(packageTypeId));
            lclBlPiece.setPkgNo(packageTypeId);
        }
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
        lclBlPiece.setCommNo(commodityNo);
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }

    public void setAutoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
    }

    public String getFdUnlocCode() {
        return fdUnlocCode;
    }

    public void setFdUnlocCode(String fdUnlocCode) {
        this.fdUnlocCode = fdUnlocCode;
    }

    public String getPooUnlocCode() {
        return pooUnlocCode;
    }

    public void setPooUnlocCode(String pooUnlocCode) {
        this.pooUnlocCode = pooUnlocCode;
    }

    public String getPodUnlocCode() {
        return podUnlocCode;
    }

    public void setPodUnlocCode(String podUnlocCode) {
        this.podUnlocCode = podUnlocCode;
    }

    public String getPolUnlocCode() {
        return polUnlocCode;
    }

    public void setPolUnlocCode(String polUnlocCode) {
        this.polUnlocCode = polUnlocCode;
    }

    public boolean isRatesRecalFlag() {
        return ratesRecalFlag;
    }

    public void setRatesRecalFlag(boolean ratesRecalFlag) {
        this.ratesRecalFlag = ratesRecalFlag;
    }

    public String getDimsToolTip() {
        return dimsToolTip;
    }

    public void setDimsToolTip(String dimsToolTip) {
        this.dimsToolTip = dimsToolTip;
    }

    public boolean isAutoConvertMetric() {
        return autoConvertMetric;
    }

    public void setAutoConvertMetric(boolean autoConvertMetric) {
        this.autoConvertMetric = autoConvertMetric;
    }

    public boolean isIncludeDestfees() {
        return includeDestfees;
    }

    public void setIncludeDestfees(boolean includeDestfees) {
        this.includeDestfees = includeDestfees;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        autoConvert = true;
        String blPieceId = request.getParameter("blPieceId");
        if (CommonUtils.isNotEmpty(blPieceId)) {
            try {
                lclBlPiece = new LclBLPieceDAO().findById(Long.parseLong(blPieceId));
            } catch (Exception ex) {
                log.info("reset()in LclBlCommodityForm failed on " + new Date(), ex);
            }
        }
        if (lclBlPiece == null) {
            lclBlPiece = new LclBlPiece();
        }
    }
}
