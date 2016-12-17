/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.referencedata.form;

import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Mei
 */
public class LclUnitSsAutoCostingForm extends BaseForm {

    private String originName;
    private String fdName;
    private Integer originId;
    private Integer fdId;
    private Long unitTypeId;
    private String unitTypeDesc;
    private String costCode;
    private Integer costCodeId;
    //Add Fields
    private Long unitSsCostId;
    private Long addUnitTypeId;
    private String addUnitTypeDesc;
    private String addVendorNo;
    private String addVendorName;
    private String addGlMappingId;
    private String addGlMapCode;
    private String ratePerUomAmt;
    private String rateUom;
    private String costType;
    private String rateAction;
    private String rateCondition;
    private String rateConditionQty;
    private String copyOrigin;
    private String copyDestination;
    private Integer copyOriginId;
    private Integer copyDestinationId;
    private String copyCostId;
    private String duplicateCostCode;

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public Integer getCostCodeId() {
        return costCodeId;
    }

    public void setCostCodeId(Integer costCodeId) {
        this.costCodeId = costCodeId;
    }

    public Integer getFdId() {
        return fdId;
    }

    public void setFdId(Integer fdId) {
        this.fdId = fdId;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getUnitTypeDesc() {
        return unitTypeDesc;
    }

    public void setUnitTypeDesc(String unitTypeDesc) {
        this.unitTypeDesc = unitTypeDesc;
    }

    public Long getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(Long unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public List<LabelValueBean> getUnitTypeList() throws Exception {
        return new UnitTypeDAO().getAllUnittypesForDisplay("0", "1");
    }

    public String getAddGlMapCode() {
        return addGlMapCode;
    }

    public void setAddGlMapCode(String addGlMapCode) {
        this.addGlMapCode = addGlMapCode;
    }

    public String getAddGlMappingId() {
        return addGlMappingId;
    }

    public void setAddGlMappingId(String addGlMappingId) {
        this.addGlMappingId = addGlMappingId;
    }

    public String getAddUnitTypeDesc() {
        return addUnitTypeDesc;
    }

    public void setAddUnitTypeDesc(String addUnitTypeDesc) {
        this.addUnitTypeDesc = addUnitTypeDesc;
    }

    public Long getAddUnitTypeId() {
        return addUnitTypeId;
    }

    public void setAddUnitTypeId(Long addUnitTypeId) {
        this.addUnitTypeId = addUnitTypeId;
    }

    public String getAddVendorName() {
        return addVendorName;
    }

    public void setAddVendorName(String addVendorName) {
        this.addVendorName = addVendorName;
    }

    public String getAddVendorNo() {
        return addVendorNo;
    }

    public void setAddVendorNo(String addVendorNo) {
        this.addVendorNo = addVendorNo;
    }

    public Long getUnitSsCostId() {
        return unitSsCostId;
    }

    public void setUnitSsCostId(Long unitSsCostId) {
        this.unitSsCostId = unitSsCostId;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getRateAction() {
        return rateAction;
    }

    public void setRateAction(String rateAction) {
        this.rateAction = rateAction;
    }

    public String getRateCondition() {
        return rateCondition;
    }

    public void setRateCondition(String rateCondition) {
        this.rateCondition = rateCondition;
    }

    public String getRateConditionQty() {
        return rateConditionQty;
    }

    public void setRateConditionQty(String rateConditionQty) {
        this.rateConditionQty = rateConditionQty;
    }

    public String getRatePerUomAmt() {
        return ratePerUomAmt;
    }

    public void setRatePerUomAmt(String ratePerUomAmt) {
        this.ratePerUomAmt = ratePerUomAmt;
    }

    public String getRateUom() {
        return rateUom;
    }

    public void setRateUom(String rateUom) {
        this.rateUom = rateUom;
    }

    public String getCopyOrigin() {
        return copyOrigin;
    }

    public void setCopyOrigin(String copyOrigin) {
        this.copyOrigin = copyOrigin;
    }

    public String getCopyDestination() {
        return copyDestination;
    }

    public void setCopyDestination(String copyDestination) {
        this.copyDestination = copyDestination;
    }

    public Integer getCopyOriginId() {
        return copyOriginId;
    }

    public void setCopyOriginId(Integer copyOriginId) {
        this.copyOriginId = copyOriginId;
    }

    public Integer getCopyDestinationId() {
        return copyDestinationId;
    }

    public void setCopyDestinationId(Integer copyDestinationId) {
        this.copyDestinationId = copyDestinationId;
    }

    public String getCopyCostId() {
        return copyCostId;
    }

    public void setCopyCostId(String copyCostId) {
        this.copyCostId = copyCostId;
    }

    public String getDuplicateCostCode() {
        return duplicateCostCode;
    }

    public void setDuplicateCostCode(String duplicateCostCode) {
        this.duplicateCostCode = duplicateCostCode;
    }
}
