package com.logiware.form;

import org.apache.struts.action.ActionForm;

import com.gp.cvst.logisoft.domain.GlMapping;
import java.util.List;
import org.apache.struts.upload.FormFile;

public class GlMappingForm extends ActionForm {

    private static final long serialVersionUID = -7209695652682849751L;
    private String searchBychargeCode;
    private String startAccount;
    private String endAccount;
    private String action;
    private List<GlMapping> glMappings;
    private GlMapping glMapping;
    private Integer glId;
    private FormFile glMappingSheet;
    private String message;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEndAccount() {
        return endAccount;
    }

    public void setEndAccount(String endAccount) {
        this.endAccount = endAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getGlId() {
        return glId;
    }

    public void setGlId(Integer glId) {
        this.glId = glId;
    }

    public FormFile getGlMappingSheet() {
        return glMappingSheet;
    }

    public void setGlMappingSheet(FormFile glMappingSheet) {
        this.glMappingSheet = glMappingSheet;
    }

    public GlMapping getGlMapping() {
        if (null == glMapping) {
            glMapping = new GlMapping();
        }
        return glMapping;
    }

    public void setGlMapping(GlMapping glMapping) {
        this.glMapping = glMapping;
    }

    public List<GlMapping> getGlMappings() {
        return glMappings;
    }

    public void setGlMappings(List<GlMapping> glMappings) {
        this.glMappings = glMappings;
    }

    public String getSearchBychargeCode() {
        return searchBychargeCode;
    }

    public void setSearchBychargeCode(String searchBychargeCode) {
        this.searchBychargeCode = searchBychargeCode;
    }

    public String getStartAccount() {
        return startAccount;
    }

    public void setStartAccount(String startAccount) {
        this.startAccount = startAccount;
    }

    public Integer getId() {
        return glMapping.getId();
    }

    public void setId(Integer id) {
        glMapping.setId(id);
    }

    public String getShipmentType() {
        return glMapping.getShipmentType();
    }

    public void setShipmentType(String shipmentType) {
        glMapping.setShipmentType(shipmentType);
    }

    public String getChargeCode() {
        return glMapping.getChargeCode();
    }

    public void setChargeCode(String chargeCode) {
        glMapping.setChargeCode(chargeCode);
    }

    public String getRevExp() {
        return glMapping.getRevExp();
    }

    public void setRevExp(String revExp) {
        glMapping.setRevExp(revExp);
    }

    public String getTransactionType() {
        return glMapping.getTransactionType();
    }

    public void setTransactionType(String transactionType) {
        glMapping.setTransactionType(transactionType);
    }

    public String getGlAcct() {
        return glMapping.getGlAcct();
    }

    public void setGlAcct(String glAcct) {
        glMapping.setGlAcct(glAcct);
    }

    public String getSuffixValue() {
        return glMapping.getSuffixValue();
    }

    public void setSuffixValue(String suffixValue) {
        glMapping.setSuffixValue(suffixValue);
    }

    public String getDeriveYn() {
        return glMapping.getDeriveYn();
    }

    public void setDeriveYn(String deriveYn) {
        glMapping.setDeriveYn(deriveYn);
    }

    public String getChargeDescriptions() {
        return glMapping.getChargeDescriptions();
    }

    public void setChargeDescriptions(String chargeDescriptions) {
        glMapping.setChargeDescriptions(chargeDescriptions);
    }

    public String getBlueScreenChargeCode() {
        return glMapping.getBlueScreenChargeCode();
    }

    public void setBlueScreenChargeCode(String blueScreenChargeCode) {
        glMapping.setBlueScreenChargeCode(blueScreenChargeCode);
    }

    public boolean isBluescreenFeedback() {
        return glMapping.isBluescreenFeedback();
    }

    public void setBluescreenFeedback(boolean bluescreenFeedback) {
        glMapping.setBluescreenFeedback(bluescreenFeedback);
    }
    
    public boolean isBlLevelCost() {
        return glMapping.isBlLevelCost();
    }

    public void setBlLevelCost(boolean blLevelCost) {
        glMapping.setBlLevelCost(blLevelCost);
    }
}
