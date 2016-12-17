/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author Thamizh
 */
public class LclQuoteCommodityForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclQuoteCommodityForm.class);
    private LclQuotePiece lclQuotePiece;
    private String tariff;
    private Integer currentPageSize = 30;
    private String orderBy = "desc";
    private Integer pageNo = 1;
    private Integer noOfPages = 0;
    private Integer totalPageSize = 0;
    private Integer noOfRecords = 0;
    private String packageType;
    private String commodityType;
    private String commoName;
    private String commodityNo;
    private String fileNumber;
    private String quotePieceId;
    private boolean autoConvert;
    private Long commId;
    private Long fileNumberId;
    private String editDimFlag;
    private String originUnlocCode;//originId
    //private String orgId;
    private String fdUnlocCode;//destinationId
    //private String destId;
    //private String origin;//remove
    private String originNo;
    //private String destination;//remove
    private String destinationNo;
    private String originName;
    private String destinationName;
    private String rateType;
    //private String commCode;
    //private String pol;//remove
    private String polUnlocCode;//polId
    //private String pod;//remove
    private String podUnlocCode;//podId
    //private String ok;
    //private String okHaz;
    //  private String bookPieceCount;
    //private String bookVolumeImp;
    //private String bookWeigtImp;
    // private String actPieceCount;
    //private String actVolumeImp;
    //private String actWeigtImp;
    //   private String barrel;
    private String count;
    private boolean isBarrel;
    private String oldcommodity;
    private String cityLocation;//details screen for Location
    private String whseLocation;//details screen for warehouseName
    private String calcHeavy;
    private String deliveryMetro;
    private String pcBoth;
    private Integer warehouseId;//details screen for warehouseId
    private Long oldcommodityTypeId;
    private String oldTariffNo;
    //Import hidden Fields
    private String agentNo;
    private String transhipment;
    private String billingType;
    private String billToParty;
    private String moduleName;
    private String quotewhseId;
    private String dimsToolTip;
    private boolean ratesRecalFlag;
    private String labelField;
    private String overShortdamaged;
    private String osdRemarks;
    private String tracking;
    private String hotCodes;
    private String genCodefield1;
    private String businessUnit;
    private Long lcl3pRefId;
    private String companyCode;
    private String noteId;
    private boolean includeDestfees;
    private boolean ups;
    private boolean smallParcelFlag;
    private String smalParcelMeasureImp;
    private String smallParcelRemarks;

    public LclQuoteCommodityForm() {
        if (lclQuotePiece == null) {
            lclQuotePiece = new LclQuotePiece();
        }
        if (lclQuotePiece.getLclFileNumber() == null) {
            lclQuotePiece.setLclFileNumber(new LclFileNumber());
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

    public String getFdUnlocCode() {
        return fdUnlocCode;
    }

    public void setFdUnlocCode(String fdUnlocCode) {
        this.fdUnlocCode = fdUnlocCode;
    }

    public String getOriginUnlocCode() {
        return originUnlocCode;
    }

    public void setOriginUnlocCode(String originUnlocCode) {
        this.originUnlocCode = originUnlocCode;
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

    public String getCommoName() {
        return commoName;
    }

    public void setCommoName(String commoName) {
        this.commoName = commoName;
    }

    public String getCalcHeavy() {
        return calcHeavy;
    }

    public void setCalcHeavy(String calcHeavy) {
        this.calcHeavy = calcHeavy;
    }

    public boolean getIsBarrel() {
        return lclQuotePiece.isIsBarrel();
    }

    public void setIsBarrel(boolean isBarrel) {
        lclQuotePiece.setIsBarrel(isBarrel);
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getQuotePieceId() {
        return quotePieceId;
    }

    public void setQuotePieceId(String quotePieceId) {
        this.quotePieceId = quotePieceId;
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
        if (lclQuotePiece.getLclFileNumber() != null) {
            return lclQuotePiece.getLclFileNumber().getId();
        }
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        if (lclQuotePiece.getLclFileNumber() != null) {
            lclQuotePiece.getLclFileNumber().setId(fileNumberId);
        }
        this.fileNumberId = fileNumberId;
    }

    public LclQuotePiece getLclQuotePiece() {
        return lclQuotePiece;
    }

    public void setLclQuotePiece(LclQuotePiece lclQuotePiece) {
        this.lclQuotePiece = lclQuotePiece;
    }

    public String getPieceDesc() {
        return lclQuotePiece.getPieceDesc();
    }

    public void setPieceDesc(String pieceDesc) {
        lclQuotePiece.setPieceDesc(pieceDesc.replaceAll("\n", ""));
    }

    public String getMarkNoDesc() {
        return lclQuotePiece.getMarkNoDesc();
    }

    public void setMarkNoDesc(String markNoDesc) {
        lclQuotePiece.setMarkNoDesc(markNoDesc.replaceAll("\n", ""));
    }

    public String getHazmat() {
        if (lclQuotePiece.isHazmat() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setHazmat(String hazmat) {
        if (hazmat.equals("true") || hazmat.equals(Y)) {
            lclQuotePiece.setHazmat(TRUE);
        } else {
            lclQuotePiece.setHazmat(FALSE);
        }
    }

    public String getPersonalEffects() {
        if (CommonUtils.isEmpty(lclQuotePiece.getPersonalEffects())) {
            return N;
        }
        return lclQuotePiece.getPersonalEffects();
    }

    public void setPersonalEffects(String personalEffects) {
        lclQuotePiece.setPersonalEffects(personalEffects);
    }

    public Boolean getRefrigerationRequired() {
        return lclQuotePiece.getRefrigerationRequired();
    }

    public void setRefrigerationRequired(Boolean refrigerationRequired) {
        lclQuotePiece.setRefrigerationRequired(refrigerationRequired);
    }

    public Boolean getWeightVerified() {
        return lclQuotePiece.getWeightVerified();
    }

    public void setWeightVerified(Boolean weightVerified) {
        lclQuotePiece.setWeightVerified(weightVerified);
    }

    public String getHarmonizedCode() {
        return lclQuotePiece.getHarmonizedCode();
    }

    public void setHarmonizedCode(String harmonizedCode) {
        lclQuotePiece.setHarmonizedCode(harmonizedCode);
    }

    public String getBookedPieceCount() {
        if (CommonUtils.isEmpty(lclQuotePiece.getBookedPieceCount())) {
            return "";
        }
        return "" + lclQuotePiece.getBookedPieceCount();
    }

    public void setBookedPieceCount(String bookedPieceCount) {
        if (CommonUtils.isNotEmpty(bookedPieceCount)) {
            lclQuotePiece.setBookedPieceCount(Integer.parseInt(bookedPieceCount));
        } else {
            lclQuotePiece.setBookedPieceCount(0);
        }
    }

    public String getBookedWeightImperial() {
        if (CommonUtils.isEmpty(lclQuotePiece.getBookedWeightImperial())) {
            return "";
        }
        return "" + lclQuotePiece.getBookedWeightImperial();
    }

    public void setBookedWeightImperial(String bookedWeightImperial) {
        if (CommonUtils.isNotEmpty(bookedWeightImperial)) {
            lclQuotePiece.setBookedWeightImperial(new BigDecimal(bookedWeightImperial));
        } else {
            lclQuotePiece.setBookedWeightImperial(null);
        }
    }

    public String getBookedWeightMetric() {
        if (CommonUtils.isEmpty(lclQuotePiece.getBookedWeightMetric())) {
            return "";
        }
        return "" + lclQuotePiece.getBookedWeightMetric();
    }

    public void setBookedWeightMetric(String bookedWeightMetric) {
        if (CommonUtils.isNotEmpty(bookedWeightMetric)) {
            lclQuotePiece.setBookedWeightMetric(new BigDecimal(bookedWeightMetric));
        } else {
            lclQuotePiece.setBookedWeightMetric(null);
        }
    }

    public String getBookedVolumeImperial() {
        if (CommonUtils.isEmpty(lclQuotePiece.getBookedVolumeImperial())) {
            return "";
        }
        return "" + lclQuotePiece.getBookedVolumeImperial();
    }

    public void setBookedVolumeImperial(String bookedVolumeImperial) {
        if (CommonUtils.isNotEmpty(bookedVolumeImperial)) {
            lclQuotePiece.setBookedVolumeImperial(new BigDecimal(bookedVolumeImperial));
        } else {
            lclQuotePiece.setBookedVolumeImperial(null);
        }
    }

    public String getBookedVolumeMetric() {
        if (CommonUtils.isEmpty(lclQuotePiece.getBookedVolumeMetric())) {
            return "";
        }
        return "" + lclQuotePiece.getBookedVolumeMetric();
    }

    public void setBookedVolumeMetric(String bookedVolumeMetric) {
        if (CommonUtils.isNotEmpty(bookedVolumeMetric)) {
            lclQuotePiece.setBookedVolumeMetric(new BigDecimal(bookedVolumeMetric));
        } else {
            lclQuotePiece.setBookedVolumeMetric(null);
        }
    }

    public String getActualPieceCount() {
        if (CommonUtils.isEmpty(lclQuotePiece.getActualPieceCount())) {
            return "";
        }
        return "" + lclQuotePiece.getActualPieceCount();
    }

    public void setActualPieceCount(String actualPieceCount) {
        if (CommonUtils.isNotEmpty(actualPieceCount)) {
            lclQuotePiece.setActualPieceCount(new Integer(actualPieceCount));
        }
    }

    public String getActualWeightImperial() {
        if (CommonUtils.isEmpty(lclQuotePiece.getActualWeightImperial())) {
            return "";
        }
        return "" + lclQuotePiece.getActualWeightImperial();
    }

    public void setActualWeightImperial(String actualWeightImperial) {
        if (CommonUtils.isNotEmpty(actualWeightImperial)) {
            lclQuotePiece.setActualWeightImperial(new BigDecimal(actualWeightImperial));
        } else {
            lclQuotePiece.setActualWeightImperial(null);
        }
    }

    public String getActualWeightMetric() {
        if (CommonUtils.isEmpty(lclQuotePiece.getActualWeightMetric())) {
            return "";
        }
        return "" + lclQuotePiece.getActualWeightMetric();
    }

    public void setActualWeightMetric(String actualWeightMetric) {
        if (CommonUtils.isNotEmpty(actualWeightMetric)) {
            lclQuotePiece.setActualWeightMetric(new BigDecimal(actualWeightMetric));
        } else {
            lclQuotePiece.setActualWeightMetric(null);
        }
    }

    public String getActualVolumeImperial() {
        if (CommonUtils.isEmpty(lclQuotePiece.getActualVolumeImperial())) {
            return "";
        }
        return "" + lclQuotePiece.getActualVolumeImperial();
    }

    public void setActualVolumeImperial(String actualVolumeImperial) {
        if (CommonUtils.isNotEmpty(actualVolumeImperial)) {
            lclQuotePiece.setActualVolumeImperial(new BigDecimal(actualVolumeImperial));
        } else {
            lclQuotePiece.setActualVolumeImperial(null);
        }
    }

    public String getActualVolumeMetric() {
        if (CommonUtils.isEmpty(lclQuotePiece.getActualVolumeMetric())) {
            return "";
        }
        return "" + lclQuotePiece.getActualVolumeMetric();
    }

    public void setActualVolumeMetric(String actualVolumeMetric) {
        if (CommonUtils.isNotEmpty(actualVolumeMetric)) {
            lclQuotePiece.setActualVolumeMetric(new BigDecimal(actualVolumeMetric));
        } else {
            lclQuotePiece.setActualVolumeMetric(null);
        }
    }

    public String getPackageType() {
        if (lclQuotePiece.getPackageType() != null && lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount() <= 1) {
            return lclQuotePiece.getPkgName();
        } else if (lclQuotePiece.getPkgName() != null && lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount() > 1) {
            return lclQuotePiece.getPkgName();
        } else {
            return lclQuotePiece.getPkgName();
        }
    }

    public void setPackageType(String packageType) {
        lclQuotePiece.setPkgName(packageType);
    }

    public String getCommodityType() {
        if (lclQuotePiece.getCommodityType() != null) {
            return lclQuotePiece.getCommodityType().getDescEn();
        } else {
            return lclQuotePiece.getCommName();
        }
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
        lclQuotePiece.setCommName(commodityType);
    }

    public Long getCommodityTypeId() {
        if (lclQuotePiece.getCommodityType() != null) {
            return lclQuotePiece.getCommodityType().getId();
        } else {
            return null;
        }
    }

    public void setCommodityTypeId(Long commodityTypeId) {
        if (CommonUtils.isNotEmpty(commodityTypeId)) {
            CommodityType commodityType = new CommodityType(commodityTypeId);
            lclQuotePiece.setCommodityType(commodityType);
        }
    }

    public Long getPackageTypeId() {
        if (lclQuotePiece.getPackageType() != null) {
            return lclQuotePiece.getPackageType().getId();
        } else {
            return lclQuotePiece.getPkgNo();
        }
    }

    public void setPackageTypeId(Long packageTypeId) {
        if (CommonUtils.isNotEmpty(packageTypeId)) {
            lclQuotePiece.setPackageType(new PackageType(packageTypeId));
            lclQuotePiece.setPkgNo(packageTypeId);
        }
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
        lclQuotePiece.setCommNo(commodityNo);
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }

    public void setAutoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWhseLocation() {
        return whseLocation;
    }

    public void setWhseLocation(String whseLocation) {
        this.whseLocation = whseLocation;
    }

    public String getSmallParcelRemarks() {
        return smallParcelRemarks;
    }

    public void setSmallParcelRemarks(String smallParcelRemarks) {
        this.smallParcelRemarks = smallParcelRemarks;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(String transhipment) {
        this.transhipment = transhipment;
    }

    public String getQuotewhseId() {
        return quotewhseId;
    }

    public void setQuotewhseId(String quotewhseId) {
        this.quotewhseId = quotewhseId;
    }

    public String getDimsToolTip() {
        return dimsToolTip;
    }

    public void setDimsToolTip(String dimsToolTip) {
        this.dimsToolTip = dimsToolTip;
    }

    public boolean isRatesRecalFlag() {
        return ratesRecalFlag;
    }

    public void setRatesRecalFlag(boolean ratesRecalFlag) {
        this.ratesRecalFlag = ratesRecalFlag;
    }

    public String getOldcommodity() {
        return oldcommodity;
    }

    public void setOldcommodity(String oldcommodity) {
        this.oldcommodity = oldcommodity;
    }

    public Long getOldcommodityTypeId() {
        return oldcommodityTypeId;
    }

    public void setOldcommodityTypeId(Long oldcommodityTypeId) {
        this.oldcommodityTypeId = oldcommodityTypeId;
    }

    public String getOldTariffNo() {
        return oldTariffNo;
    }

    public void setOldTariffNo(String oldTariffNo) {
        this.oldTariffNo = oldTariffNo;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
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

    public String getGenCodefield1() {
        return genCodefield1;
    }

    public void setGenCodefield1(String genCodefield1) {
        this.genCodefield1 = genCodefield1;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public Long getLcl3pRefId() {
        return lcl3pRefId;
    }

    public void setLcl3pRefId(Long lcl3pRefId) {
        this.lcl3pRefId = lcl3pRefId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
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

    public String getSmalParcelMeasureImp() {
        return smalParcelMeasureImp;
    }

    public void setSmalParcelMeasureImp(String smalParcelMeasureImp) {
        this.smalParcelMeasureImp = smalParcelMeasureImp;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        autoConvert = true;
        String quotePieceId = request.getParameter("quotePieceId");
        String countVal = request.getParameter("countVal");
        if (CommonUtils.isNotEmpty(quotePieceId)) {
            try {
                lclQuotePiece = new LclQuotePieceDAO().findById(Long.parseLong(quotePieceId));
            } catch (Exception ex) {
                log.info("reset()in LclQuoteCommodityForm failed on " + new Date(), ex);
            }
        }
//        if (quotePieceId == null || quotePieceId.trim().equals("")) {
//            HttpSession session = request.getSession();
//            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
//            List<LclQuotePiece> quoteCommodityList = lclSession.getQuoteCommodityList();
//            int c = Integer.parseInt(countVal);
//            lclQuotePiece = quoteCommodityList.get(c);
//        }
    }
}
