/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author Thamizh
 */
public class LclHazmatForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclHazmatForm.class);
    private String fileNumber;
    private Long fileNumberId;
    private Long bookingPieceId;
    private Long hazmatId;//id
    private String commodityCode;
    private String commodityDesc;
    private String totalNetWeight;
    private String totalGrossWeight;
    private String unHazmatNo;
    private String properShippingName;
    private String technicalName;
    private String imoPriClassCode;
    private String imoPriSubClassCode;
    private String imoSecSubClassCode;
    private String packingGroupCode;
    private String flashPoint;
    private Integer outerPkgNoPieces;
    private Integer innerPkgNoPieces;
    private String outerPkgType;
    private String innerPkgType;
    private String outerPkgComposition;
    private String innerPkgComposition;
    private String innerPkgUom;
    private String innerPkgNwtPiece;
    private String emergencyContact;
    private String liquidVolume;
    private String emergencyPhone;
    private String emsCode;
    private Boolean reportableQuantity;
    private Boolean marinePollutant;
    private Boolean exceptedQuantity;
    private Boolean limitedQuantity;
    private Boolean residue;
    private Boolean inhalationHazard;
    private Boolean printHouseBL;
    private Boolean printMasterBL;
    private Boolean sendEdiMaster;
    private String buttonFlag;
    private String freeFormValues;
    private List<LclBookingHazmat> hazmatList;
    private String hazClassDesc;
    private String moduleName;
    private String liquidVolumeLitreorGals;

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getEmsCode() {
        return emsCode;
    }

    public void setEmsCode(String emsCode) {
        this.emsCode = emsCode;
    }

    public Boolean getExceptedQuantity() {
        return exceptedQuantity;
    }

    public void setExceptedQuantity(Boolean exceptedQuantity) {
        this.exceptedQuantity = exceptedQuantity;
    }

    public String getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(String flashPoint) {
        this.flashPoint = flashPoint;
    }

    public String getImoPriClassCode() {
        return imoPriClassCode;
    }

    public void setImoPriClassCode(String imoPriClassCode) {
        this.imoPriClassCode = imoPriClassCode;
    }

    public String getImoPriSubClassCode() {
        return imoPriSubClassCode;
    }

    public void setImoPriSubClassCode(String imoPriSubClassCode) {
        this.imoPriSubClassCode = imoPriSubClassCode;
    }

    public String getImoSecSubClassCode() {
        return imoSecSubClassCode;
    }

    public void setImoSecSubClassCode(String imoSecSubClassCode) {
        this.imoSecSubClassCode = imoSecSubClassCode;
    }

    public Boolean getInhalationHazard() {
        return inhalationHazard;
    }

    public void setInhalationHazard(Boolean inhalationHazard) {
        this.inhalationHazard = inhalationHazard;
    }

    public String getInnerPkgComposition() {
        return innerPkgComposition;
    }

    public void setInnerPkgComposition(String innerPkgComposition) {
        this.innerPkgComposition = innerPkgComposition;
    }

    public Integer getInnerPkgNoPieces() {
        return innerPkgNoPieces;
    }

    public void setInnerPkgNoPieces(Integer innerPkgNoPieces) {
        this.innerPkgNoPieces = innerPkgNoPieces;
    }

    public String getInnerPkgNwtPiece() {
        return innerPkgNwtPiece;
    }

    public void setInnerPkgNwtPiece(String innerPkgNwtPiece) {
        this.innerPkgNwtPiece = innerPkgNwtPiece;
    }

    public String getInnerPkgType() {
        return innerPkgType;
    }

    public void setInnerPkgType(String innerPkgType) {
        this.innerPkgType = innerPkgType;
    }

    public String getInnerPkgUom() {
        return innerPkgUom;
    }

    public void setInnerPkgUom(String innerPkgUom) {
        this.innerPkgUom = innerPkgUom;
    }

    public Boolean getLimitedQuantity() {
        return limitedQuantity;
    }

    public void setLimitedQuantity(Boolean limitedQuantity) {
        this.limitedQuantity = limitedQuantity;
    }

    public String getLiquidVolume() {
        return liquidVolume;
    }

    public void setLiquidVolume(String liquidVolume) {
        this.liquidVolume = liquidVolume;
    }

    public Boolean getMarinePollutant() {
        return marinePollutant;
    }

    public void setMarinePollutant(Boolean marinePollutant) {
        this.marinePollutant = marinePollutant;
    }

    public String getOuterPkgComposition() {
        return outerPkgComposition;
    }

    public void setOuterPkgComposition(String outerPkgComposition) {
        this.outerPkgComposition = outerPkgComposition;
    }

    public Integer getOuterPkgNoPieces() {
        return outerPkgNoPieces;
    }

    public void setOuterPkgNoPieces(Integer outerPkgNoPieces) {
        this.outerPkgNoPieces = outerPkgNoPieces;
    }

    public String getOuterPkgType() {
        return outerPkgType;
    }

    public void setOuterPkgType(String outerPkgType) {
        this.outerPkgType = outerPkgType;
    }

    public String getPackingGroupCode() {
        return packingGroupCode;
    }

    public void setPackingGroupCode(String packingGroupCode) {
        this.packingGroupCode = packingGroupCode;
    }

    public String getProperShippingName() {
        return properShippingName;
    }

    public void setProperShippingName(String properShippingName) {
        this.properShippingName = properShippingName;
    }

    public Boolean getReportableQuantity() {
        return reportableQuantity;
    }

    public void setReportableQuantity(Boolean reportableQuantity) {
        this.reportableQuantity = reportableQuantity;
    }

    public Boolean getResidue() {
        return residue;
    }

    public void setResidue(Boolean residue) {
        this.residue = residue;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(String totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }

    public String getTotalNetWeight() {
        return totalNetWeight;
    }

    public void setTotalNetWeight(String totalNetWeight) {
        this.totalNetWeight = totalNetWeight;
    }

    public String getUnHazmatNo() {
        return unHazmatNo;
    }

    public void setUnHazmatNo(String unHazmatNo) {
        this.unHazmatNo = unHazmatNo;
    }

    public Long getHazmatId() {
        return hazmatId;
    }

    public void setHazmatId(Long hazmatId) {
        this.hazmatId = hazmatId;
    }

    public Long getBookingPieceId() {
        return bookingPieceId;
    }

    public void setBookingPieceId(Long bookingPieceId) {
        this.bookingPieceId = bookingPieceId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
//        String commId = request.getParameter("commodityId");
//        String fileId = request.getParameter("fileNumberId");
//        if (CommonUtils.isNotEmpty(commId) && CommonUtils.isNotEmpty(fileId)) {
//            try {
//                lclBookingHazmat = new LclHazmatDAO().findByFileAndCommodity(Long.parseLong(fileId), Long.parseLong(commId));
//            } catch (Exception ex) {
//                log.info("reset()in LclHazmatForm failed on " + new Date(),ex);
//            }
//        }
//        if (lclBookingHazmat == null) {
//            lclBookingHazmat = new LclBookingHazmat();
//        }
    }

    public String getButtonFlag() {
        return buttonFlag;
    }

    public void setButtonFlag(String buttonFlag) {
        this.buttonFlag = buttonFlag;
    }

    public String getFreeFormValues() {
        return freeFormValues;
    }

    public void setFreeFormValues(String freeFormValues) {
        this.freeFormValues = freeFormValues;
    }

    public List<LclBookingHazmat> getHazmatList() {
        return hazmatList;
    }

    public void setHazmatList(List<LclBookingHazmat> hazmatList) {
        this.hazmatList = hazmatList;
    }

    public List getHazmatClass() throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Primary ClassCodes");
        return new DBUtil().scanScreenName(codeTypeId);
    }

    public List getPkgGroupCode() throws Exception {
        return new QuotationBC().getPackingGroupCode();
    }

    public Boolean getPrintHouseBL() {
        return printHouseBL;
    }

    public void setPrintHouseBL(Boolean printHouseBL) {
        this.printHouseBL = printHouseBL;
    }

    public Boolean getPrintMasterBL() {
        return printMasterBL;
    }

    public void setPrintMasterBL(Boolean printMasterBL) {
        this.printMasterBL = printMasterBL;
    }

    public Boolean getSendEdiMaster() {
        return sendEdiMaster;
    }

    public void setSendEdiMaster(Boolean sendEdiMaster) {
        this.sendEdiMaster = sendEdiMaster;
    }

    public String getHazClassDesc() {
        return hazClassDesc;
    }

    public void setHazClassDesc(String hazClassDesc) {
        this.hazClassDesc = hazClassDesc;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLiquidVolumeLitreorGals() {
        return liquidVolumeLitreorGals;
    }

    public void setLiquidVolumeLitreorGals(String liquidVolumeLitreorGals) {
        this.liquidVolumeLitreorGals = liquidVolumeLitreorGals;
    }
    
}
