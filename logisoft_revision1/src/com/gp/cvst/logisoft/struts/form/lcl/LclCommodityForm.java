/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author Thamizh
 */
public class LclCommodityForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclCommodityForm.class);
    private LclBookingPiece lclBookingPiece;
    private String packageType;
    private String commodityType;
    private String commName;
    private String tariff;
    private Integer currentPageSize = 30;
    private String orderBy = "desc";
    private Integer pageNo = 1;
    private Integer noOfPages = 0;
    private Integer totalPageSize = 0;
    private Integer noOfRecords = 0;
    private String cityLocation;
    private String whseLocation;
    private String commodityNo;
    private String fileNumber;
    private String id;
    private boolean autoConvert;
    private String hazmat;
    private Long commId;
    private Long fileNumberId;
    private String editDimFlag;
    private String commCode;
    private String origin;
    private String originNo;
    private String originId;
    private String orgId;
    private String originName;
    private String destination;
    private String destinationNo;
    private String destinationId;
    private String destId;
    private String destinationName;
    private String rateType;
    private String pol;
    private String polId;
    private String pod;
    private String podId;
    private String bookPieceCount;
    private String bookVolumeImp;
    private String bookWeigtImp;
    private String actPieceCount;
    private String actVolumeImp;
    private String actWeigtImp;
    private String barrel;
    private String ok;
    private String okHaz;
    private String count;
    private String status;
    private String yesVerifiedClicked;
    private String pieceId;
    private Integer warehouseId;
    private String calcHeavy;
    private String deliveryMetro;
    private String billingType;
    private String billToParty;
    private String pcBoth;
    private String insurance;
    private String valueOfGoods;
    //Import using this variable to save charge vendor
    private String agentNo;
    //Import Getter and Setter
    private String unitSsId;
    private String transhipment;
    private String impCfsWarehsId;
    private String moduleName;
    //existing Values in DB
    private String oldcommodity;
    private String oldTariffNo;
    private String bookingType;
    private String unitStatus;
    ///LCL Booking piece whse details Id
    private String bookingPieceWhseId;
    private String totalMeasureImp;
    private String totalMeasureMet;
    private String totalWeightImp;
    private String totalWeightMet;
    private Integer totalPieces;
    private String actualUom;
    private String ratesValidationFlag;//rates calculation by setting flag
    private String disposition; // add for exports to check disposition 
    private boolean autoConvertMetric;
    private String weightVerifiedBy;
    private String weightverifiedUserId;
    private Long oldcommodityId;
    private String stowedByUser;
    private String stowedByUserId;
    private Long actualPackageTypeId;
    private String actualPackageName;
    private String overShortdamaged;
    private String osdRemarks;
    private String labelField;
    private String tracking;
    private String hotCodes;
    private String businessUnit;
    private String genCodefield1;
    private Long lcl3pRefId;
    private String companyCode;
    private String fileStatus;
    private String shortShipFileNo;
    private boolean cargoReceived;
    private String verifiedIds;
    private boolean includeDestfees;
    private boolean ups;//Small Parcell Values
    private boolean smallParcelFlag;
    private String methodFlag;
    

    public LclCommodityForm() {
        if (lclBookingPiece == null) {
            lclBookingPiece = new LclBookingPiece();
        }
        if (lclBookingPiece.getLclFileNumber() == null) {
            lclBookingPiece.setLclFileNumber(new LclFileNumber());
        }
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

    public String getDeliveryMetro() {
        return deliveryMetro;
    }

    public void setDeliveryMetro(String deliveryMetro) {
        this.deliveryMetro = deliveryMetro;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getPolId() {
        return polId;
    }

    public void setPolId(String polId) {
        this.polId = polId;
    }

    public String getDestinationNo() {
        return destinationNo;
    }

    public void setDestinationNo(String destinationNo) {
        this.destinationNo = destinationNo;
    }

    public String getOriginNo() {
        return originNo;
    }

    public void setOriginNo(String originNo) {
        this.originNo = originNo;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getYesVerifiedClicked() {
        return yesVerifiedClicked;
    }

    public void setYesVerifiedClicked(String yesVerifiedClicked) {
        this.yesVerifiedClicked = yesVerifiedClicked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPieceId() {
        return pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEditDimFlag() {
        return editDimFlag;
    }

    public void setEditDimFlag(String editDimFlag) {
        this.editDimFlag = editDimFlag;
    }

    public String getCommCode() {
        return commCode;
    }

    public void setCommCode(String commCode) {
        this.commCode = commCode;
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
        if (lclBookingPiece.getLclFileNumber() != null) {
            return lclBookingPiece.getLclFileNumber().getId();
        }
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        if (lclBookingPiece.getLclFileNumber() != null) {
            lclBookingPiece.getLclFileNumber().setId(fileNumberId);
        }
        this.fileNumberId = fileNumberId;
    }

    public LclBookingPiece getLclBookingPiece() {
        return lclBookingPiece;
    }

    public void setLclBookingPiece(LclBookingPiece lclBookingPiece) {
        this.lclBookingPiece = lclBookingPiece;
    }

    public String getPieceDesc() {
        return lclBookingPiece.getPieceDesc();
    }

    public void setPieceDesc(String pieceDesc) {
        lclBookingPiece.setPieceDesc(pieceDesc.replaceAll("\n", ""));
    }

    public String getMarkNoDesc() {
        return lclBookingPiece.getMarkNoDesc();
    }

    public void setMarkNoDesc(String markNoDesc) {
        lclBookingPiece.setMarkNoDesc(markNoDesc.replaceAll("\n", ""));
    }

    public String getHazmat() {
        if (lclBookingPiece.isHazmat() == TRUE) {
            return Y;

        }
        return N;

    }

    public void setHazmat(String hazmat) {
        if (hazmat.equals(Y)) {
            lclBookingPiece.setHazmat(TRUE);

        } else {
            lclBookingPiece.setHazmat(FALSE);

        }
    }

    public boolean getIsBarrel() {
        return lclBookingPiece.isIsBarrel();
    }

    public void setIsBarrel(boolean isBarrel) {
        lclBookingPiece.setIsBarrel(isBarrel);
    }

    public String getPersonalEffects() {
        if (CommonUtils.isEmpty(lclBookingPiece.getPersonalEffects())) {
            return N;
        }
        return lclBookingPiece.getPersonalEffects();
    }

    public void setPersonalEffects(String personalEffects) {
        lclBookingPiece.setPersonalEffects(personalEffects);
    }

    public Boolean getRefrigerationRequired() {
        return lclBookingPiece.getRefrigerationRequired();
    }

    public void setRefrigerationRequired(Boolean refrigerationRequired) {
        lclBookingPiece.setRefrigerationRequired(refrigerationRequired);
    }

    public Boolean getWeightVerified() {
        return lclBookingPiece.getWeightVerified();
    }

    public void setWeightVerified(Boolean weightVerified) {
        lclBookingPiece.setWeightVerified(weightVerified);
    }

    public String getHarmonizedCode() {
        return lclBookingPiece.getHarmonizedCode();
    }

    public void setHarmonizedCode(String harmonizedCode) {
        lclBookingPiece.setHarmonizedCode(harmonizedCode);
    }

    public String getOldcommodity() {
        return oldcommodity;
    }

    public void setOldcommodity(String oldcommodity) {
        this.oldcommodity = oldcommodity;
    }

    public String getOldTariffNo() {
        return oldTariffNo;
    }

    public void setOldTariffNo(String oldTariffNo) {
        this.oldTariffNo = oldTariffNo;
    }

    public String getBookedPieceCount() {
        if (lclBookingPiece.getBookedPieceCount() != null) {
            return "" + lclBookingPiece.getBookedPieceCount();
        }
        return "";
    }

    public void setBookedPieceCount(String bookedPieceCount) {
        if (CommonUtils.isNotEmpty(bookedPieceCount)) {
            lclBookingPiece.setBookedPieceCount(new Integer(bookedPieceCount));
        } else {
            lclBookingPiece.setBookedPieceCount(0);
        }
    }

    public String getBookedWeightImperial() {
        if (lclBookingPiece.getBookedWeightImperial() != null) {
            return "" + lclBookingPiece.getBookedWeightImperial();
        }
        return "";
    }

    public void setBookedWeightImperial(String bookedWeightImperial) {
        if (CommonUtils.isNotEmpty(bookedWeightImperial)) {
            lclBookingPiece.setBookedWeightImperial(new BigDecimal(bookedWeightImperial));
        } else {
            lclBookingPiece.setBookedWeightImperial(null);
        }
    }

    public String getBookedWeightMetric() {
        if (lclBookingPiece.getBookedWeightMetric() != null) {
            return "" + lclBookingPiece.getBookedWeightMetric();
        }
        return "";
    }

    public void setBookedWeightMetric(String bookedWeightMetric) {
        if (CommonUtils.isNotEmpty(bookedWeightMetric)) {
            lclBookingPiece.setBookedWeightMetric(new BigDecimal(bookedWeightMetric));
        } else {
            lclBookingPiece.setBookedWeightMetric(null);
        }
    }

    public String getBookedVolumeImperial() {
        if (lclBookingPiece.getBookedVolumeImperial() != null) {
            return "" + lclBookingPiece.getBookedVolumeImperial();
        }
        return "";
    }

    public void setBookedVolumeImperial(String bookedVolumeImperial) {
        if (CommonUtils.isNotEmpty(bookedVolumeImperial)) {
            lclBookingPiece.setBookedVolumeImperial(new BigDecimal(bookedVolumeImperial));
        } else {
            lclBookingPiece.setBookedVolumeImperial(null);
        }
    }

    public String getBookedVolumeMetric() {
        if (lclBookingPiece.getBookedVolumeMetric() != null) {
            return "" + lclBookingPiece.getBookedVolumeMetric();
        }
        return "";
    }

    public void setBookedVolumeMetric(String bookedVolumeMetric) {
        if (CommonUtils.isNotEmpty(bookedVolumeMetric)) {
            lclBookingPiece.setBookedVolumeMetric(new BigDecimal(bookedVolumeMetric));
        } else {
            lclBookingPiece.setBookedVolumeMetric(null);
        }
    }

    public String getActualPieceCount() {
        if (lclBookingPiece.getActualPieceCount() != null) {
            return "" + lclBookingPiece.getActualPieceCount();
        }
        return "";
    }

    public void setActualPieceCount(String actualPieceCount) {
        if (CommonUtils.isNotEmpty(actualPieceCount)) {
            lclBookingPiece.setActualPieceCount(Integer.parseInt(actualPieceCount));
        }
    }

    public String getActualWeightImperial() {
        if (lclBookingPiece.getActualWeightImperial() != null) {
            return "" + lclBookingPiece.getActualWeightImperial();
        }
        return "";
    }

    public void setActualWeightImperial(String actualWeightImperial) {
        if (CommonUtils.isNotEmpty(actualWeightImperial)) {
            lclBookingPiece.setActualWeightImperial(new BigDecimal(actualWeightImperial));
        } else {
            lclBookingPiece.setActualWeightImperial(null);
        }
    }

    public String getActualWeightMetric() {
        if (lclBookingPiece.getActualWeightMetric() != null) {
            return "" + lclBookingPiece.getActualWeightMetric();
        }
        return "";
    }

    public void setActualWeightMetric(String actualWeightMetric) {
        if (CommonUtils.isNotEmpty(actualWeightMetric)) {
            lclBookingPiece.setActualWeightMetric(new BigDecimal(actualWeightMetric));
        } else {
            lclBookingPiece.setActualWeightMetric(null);
        }
    }

    public String getActualVolumeImperial() {
        if (lclBookingPiece.getActualVolumeImperial() != null) {
            return "" + lclBookingPiece.getActualVolumeImperial();
        }
        return "";
    }

    public void setActualVolumeImperial(String actualVolumeImperial) {
        if (CommonUtils.isNotEmpty(actualVolumeImperial)) {
            lclBookingPiece.setActualVolumeImperial(new BigDecimal(actualVolumeImperial));
        } else {
            lclBookingPiece.setActualVolumeImperial(null);
        }
    }

    public String getActualVolumeMetric() {
        if (lclBookingPiece.getActualVolumeMetric() != null) {
            return "" + lclBookingPiece.getActualVolumeMetric();
        }
        return "";
    }

    public void setActualVolumeMetric(String actualVolumeMetric) {
        if (CommonUtils.isNotEmpty(actualVolumeMetric)) {
            lclBookingPiece.setActualVolumeMetric(new BigDecimal(actualVolumeMetric));
        } else {
            lclBookingPiece.setActualVolumeMetric(null);
        }
    }

    public String getPackageType() {

        if (lclBookingPiece.getPackageType() != null && lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount() <= 1) {
            return lclBookingPiece.getPkgName();
        } else if (lclBookingPiece.getPkgName() != null && lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount() > 1) {
            return lclBookingPiece.getPkgName();
        } else {
            return lclBookingPiece.getPkgName();
        }
        //return lclQuotePiece.getPkgName();
    }

    public void setPackageType(String packageType) {
        lclBookingPiece.setPkgName(packageType);
    }

    public String getCommodityType() {
        if (lclBookingPiece.getCommodityType() != null) {
            return lclBookingPiece.getCommodityType().getDescEn();
        } else {
            return lclBookingPiece.getCommName();
        }
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
        lclBookingPiece.setCommName(commodityType);
    }

    public Long getCommodityTypeId() {
        if (lclBookingPiece.getCommodityType() != null) {
            return lclBookingPiece.getCommodityType().getId();
        }
        return null;

    }

    public void setCommodityTypeId(Long commodityTypeId) {
        if (CommonUtils.isNotEmpty(commodityTypeId)) {
            lclBookingPiece.setCommodityType(new CommodityType(commodityTypeId));
        }
    }

    public Long getPackageTypeId() {
        if (lclBookingPiece.getPackageType() != null) {
            return lclBookingPiece.getPackageType().getId();
        } else {
            return lclBookingPiece.getPkgNo();
        }
    }

    public void setPackageTypeId(Long packageTypeId) {
        if (CommonUtils.isNotEmpty(packageTypeId)) {
            lclBookingPiece.setPackageType(new PackageType(packageTypeId));
            lclBookingPiece.setPkgNo(packageTypeId);
        }
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
        lclBookingPiece.setCommNo(commodityNo);
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }

    public void setAutoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getActPieceCount() {
        return actPieceCount;
    }

    public void setActPieceCount(String actPieceCount) {
        this.actPieceCount = actPieceCount;
    }

    public String getActVolumeImp() {
        return actVolumeImp;
    }

    public void setActVolumeImp(String actVolumeImp) {
        this.actVolumeImp = actVolumeImp;
    }

    public String getActWeigtImp() {
        return actWeigtImp;
    }

    public void setActWeigtImp(String actWeigtImp) {
        this.actWeigtImp = actWeigtImp;
    }

    public String getBarrel() {
        return barrel;
    }

    public void setBarrel(String barrel) {
        this.barrel = barrel;
    }

    public String getBookPieceCount() {
        return bookPieceCount;
    }

    public void setBookPieceCount(String bookPieceCount) {
        this.bookPieceCount = bookPieceCount;
    }

    public String getBookVolumeImp() {
        return bookVolumeImp;
    }

    public void setBookVolumeImp(String bookVolumeImp) {
        this.bookVolumeImp = bookVolumeImp;
    }

    public String getBookWeigtImp() {
        return bookWeigtImp;
    }

    public void setBookWeigtImp(String bookWeigtImp) {
        this.bookWeigtImp = bookWeigtImp;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getOkHaz() {
        return okHaz;
    }

    public void setOkHaz(String okHaz) {
        this.okHaz = okHaz;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

    public String getMethodFlag() {
        return methodFlag;
    }

    public void setMethodFlag(String methodFlag) {
        this.methodFlag = methodFlag;
    }
    

    public String getWhseLocation() {
        return whseLocation;
    }

    public void setWhseLocation(String whseLocation) {
        this.whseLocation = whseLocation;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public Integer getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(Integer currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public Integer getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(Integer noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Integer getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getCalcHeavy() {
        return calcHeavy;
    }

    public void setCalcHeavy(String calcHeavy) {
        this.calcHeavy = calcHeavy;
    }

    public String getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(String transhipment) {
        this.transhipment = transhipment;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getValueOfGoods() {
        return valueOfGoods;
    }

    public void setValueOfGoods(String valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getImpCfsWarehsId() {
        return impCfsWarehsId;
    }

    public void setImpCfsWarehsId(String impCfsWarehsId) {
        this.impCfsWarehsId = impCfsWarehsId;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public String getBookingPieceWhseId() {
        return bookingPieceWhseId;
    }

    public void setBookingPieceWhseId(String bookingPieceWhseId) {
        this.bookingPieceWhseId = bookingPieceWhseId;
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

    public Integer getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Integer totalPieces) {
        this.totalPieces = totalPieces;
    }

    public String getActualUom() {
        return actualUom;
    }

    public void setActualUom(String actualUom) {
        this.actualUom = actualUom;
    }

    public String getRatesValidationFlag() {
        return ratesValidationFlag;
    }

    public void setRatesValidationFlag(String ratesValidationFlag) {
        this.ratesValidationFlag = ratesValidationFlag;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public boolean getAutoConvertMetric() {
        return autoConvertMetric;
    }

    public void setAutoConvertMetric(boolean autoConvertMetric) {
        this.autoConvertMetric = autoConvertMetric;
    }

    public String getWeightVerifiedBy() {
        return weightVerifiedBy;
    }

    public void setWeightVerifiedBy(String weightVerifiedBy) {
        this.weightVerifiedBy = weightVerifiedBy;
    }

    public String getWeightverifiedUserId() {
        return weightverifiedUserId;
    }

    public void setWeightverifiedUserId(String weightverifiedUserId) {
        this.weightverifiedUserId = weightverifiedUserId;
    }

    public Long getOldcommodityId() {
        return oldcommodityId;
    }

    public void setOldcommodityId(Long oldcommodityId) {
        this.oldcommodityId = oldcommodityId;
    }

    public String getStowedByUser() {
        return stowedByUser;
    }

    public void setStowedByUser(String stowedByUser) {
        this.stowedByUser = stowedByUser;
    }

    public String getStowedByUserId() {
        return stowedByUserId;
    }

    public void setStowedByUserId(String stowedByUserId) {
        this.stowedByUserId = stowedByUserId;
    }

    public String getActualPackageName() {
        return actualPackageName;
    }

    public void setActualPackageName(String actualPackageName) {
        this.actualPackageName = actualPackageName;
    }

    public Long getActualPackageTypeId() {
        return actualPackageTypeId;
    }

    public void setActualPackageTypeId(Long actualPackageTypeId) {
        this.actualPackageTypeId = actualPackageTypeId;
    }

    public String getOverShortdamaged() {
        return overShortdamaged;
    }

    public void setOverShortdamaged(String overShortdamaged) {
        this.overShortdamaged = overShortdamaged;
    }

    public String getOsdRemarks() {
        return osdRemarks;
    }

    public void setOsdRemarks(String osdRemarks) {
        this.osdRemarks = osdRemarks;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getGenCodefield1() {
        return genCodefield1;
    }

    public void setGenCodefield1(String genCodefield1) {
        this.genCodefield1 = genCodefield1;
    }

    public Long getLcl3pRefId() {
        return lcl3pRefId;
    }

    public void setLcl3pRefId(Long lcl3pRefId) {
        this.lcl3pRefId = lcl3pRefId;
    }

    public String getCompanyCode() throws Exception {
        companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (null != companyCode) {
            companyCode = companyCode.equalsIgnoreCase("03") ? "ECI" : "OTI";
            this.businessUnit = CommonUtils.isNotEmpty(this.businessUnit)
                    ? this.businessUnit : companyCode;
        }
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getShortShipFileNo() {
        return shortShipFileNo;
    }

    public void setShortShipFileNo(String shortShipFileNo) {
        this.shortShipFileNo = shortShipFileNo;
    }

    public String getVerifiedIds() {
        return CommonUtils.isNotEmpty(verifiedIds) ? verifiedIds : "0";
    }

    public void setVerifiedIds(String verifiedIds) {
        if (CommonUtils.isNotEmpty(this.verifiedIds)) {
            this.verifiedIds = this.verifiedIds + "," + verifiedIds;
        } else {
            this.verifiedIds = verifiedIds;
        }
    }

    public boolean isCargoReceived() {
        return cargoReceived;
    }

    public void setCargoReceived(boolean cargoReceived) {
        this.cargoReceived = cargoReceived;
    }

    public boolean isIncludeDestfees() {
        return includeDestfees;
    }

    public void setIncludeDestfees(boolean includeDestfees) {
        this.includeDestfees = includeDestfees;
    }

    public boolean getUps() {
        return ups;
    }

    public void setUps(boolean ups) {
        this.ups = ups;
    }

    public boolean isSmallParcelFlag() {
        return smallParcelFlag;
    }

    public void setSmallParcelFlag(boolean smallParcelFlag) {
        this.smallParcelFlag = smallParcelFlag;
    }


    

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        autoConvert = true;
        String bookingPieceId = request.getParameter("id");
        String fileNumberId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(bookingPieceId) && fileNumberId != null && !fileNumberId.trim().equals("") && !fileNumberId.trim().equals("0")) {
            try {
                lclBookingPiece = new LclBookingPieceDAO().findById(Long.parseLong(bookingPieceId));
            } catch (Exception ex) {
                log.info("reset()in LclCommodityForm failed on " + new Date(), ex);
            }
        }
        if (lclBookingPiece == null) {
            lclBookingPiece = new LclBookingPiece();
        }
    }
}
