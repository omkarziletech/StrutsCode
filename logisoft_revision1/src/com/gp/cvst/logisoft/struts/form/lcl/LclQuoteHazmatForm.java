/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Thamizh
 */
public class LclQuoteHazmatForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclQuoteHazmatForm.class);
    private String fileNumber;
    private Long fileNumberId;
    private Long qtPieceId;//commodityId
    private Long hazmatId;
    private String commodityNo;
    private String commodityDesc;//commodityName
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
    private String buttonFlag;
    private String freeFormValues;
    private Boolean printHouseBL;
    private Boolean printMasterBL;
    private Boolean sendEdiMaster;
    private String hazClassDesc;

    public LclQuoteHazmatForm() {
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
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

    public String getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(String flashPoint) {
        this.flashPoint = flashPoint;
    }

    public Long getHazmatId() {
        return hazmatId;
    }

    public void setHazmatId(Long hazmatId) {
        this.hazmatId = hazmatId;
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

    public String getLiquidVolume() {
        return liquidVolume;
    }

    public void setLiquidVolume(String liquidVolume) {
        this.liquidVolume = liquidVolume;
    }

    public Boolean getExceptedQuantity() {
        return exceptedQuantity;
    }

    public void setExceptedQuantity(Boolean exceptedQuantity) {
        this.exceptedQuantity = exceptedQuantity;
    }

    public Boolean getInhalationHazard() {
        return inhalationHazard;
    }

    public void setInhalationHazard(Boolean inhalationHazard) {
        this.inhalationHazard = inhalationHazard;
    }

    public Boolean getLimitedQuantity() {
        return limitedQuantity;
    }

    public void setLimitedQuantity(Boolean limitedQuantity) {
        this.limitedQuantity = limitedQuantity;
    }

    public Boolean getMarinePollutant() {
        return marinePollutant;
    }

    public void setMarinePollutant(Boolean marinePollutant) {
        this.marinePollutant = marinePollutant;
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

    public Long getQtPieceId() {
        return qtPieceId;
    }

    public void setQtPieceId(Long qtPieceId) {
        this.qtPieceId = qtPieceId;
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

    public String getButtonFlag() {
        return buttonFlag;
    }

    public void setButtonFlag(String buttonFlag) {
        this.buttonFlag = buttonFlag;
    }

    public List<GenericCode> getPkgComposition() throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Pack Composition Type");
        return new QuotationBC().getPackageCompositionType(codeTypeId);
    }

    public List<PackageType> getPkgType() throws Exception {
        return new PackageTypeDAO().setHazmetPkgType();
    }

    public List getHazmatClass() throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Primary ClassCodes");
        return new DBUtil().scanScreenName(codeTypeId);
    }

    public List getPkgGroupCode() throws Exception {
        return new QuotationBC().getPackingGroupCode();
    }

    public String getFreeFormValues() {
        return freeFormValues;
    }

    public void setFreeFormValues(String freeFormValues) {
        this.freeFormValues = freeFormValues;
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
}
