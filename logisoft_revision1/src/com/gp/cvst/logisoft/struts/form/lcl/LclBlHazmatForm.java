/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author Thamizh
 */
public class LclBlHazmatForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclBlHazmatForm.class);
    private Long fileId;
    private Long bkgHazmatId;
    private Long blPieceId;
    private String fileNo;
    private String commodityNo;
    private String commodityName;
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
    private String freeFormValues;
    private String liquidVolumeLitreorGals;

    public LclBlHazmatForm() {
    }

    public Long getBkgHazmatId() {
        return bkgHazmatId;
    }

    public void setBkgHazmatId(Long bkgHazmatId) {
        this.bkgHazmatId = bkgHazmatId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(String flashPoint) {
        this.flashPoint = flashPoint;
    }

    public String getFreeFormValues() {
        return freeFormValues;
    }

    public void setFreeFormValues(String freeFormValues) {
        this.freeFormValues = freeFormValues;
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

    public Boolean getSendEdiMaster() {
        return sendEdiMaster;
    }

    public void setSendEdiMaster(Boolean sendEdiMaster) {
        this.sendEdiMaster = sendEdiMaster;
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

    public Long getBlPieceId() {
        return blPieceId;
    }

    public void setBlPieceId(Long blPieceId) {
        this.blPieceId = blPieceId;
    }

    public String getLiquidVolumeLitreorGals() {
        return liquidVolumeLitreorGals;
    }

    public void setLiquidVolumeLitreorGals(String liquidVolumeLitreorGals) {
        this.liquidVolumeLitreorGals = liquidVolumeLitreorGals;
    }
    

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

    }
}
