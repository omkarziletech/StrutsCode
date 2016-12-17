/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class FCLPortConfiguration implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer shedulenumber;
    private RefTerminalTemp trmnum;
    private String lineManager;
    private GenericCode blClauseId;
    private String srvcFcl;
    private String cubeWtMandatoryFcl;
    private String fclSsBlGoCollect;
    private String fclHouseBlGoCollect;
    private String quoteClause;
    private Double radmAm;
    private Double rcomAm;
    private Double nadmAm;
    private Double ncomAm;
    private GenericCode radmRule;
    private GenericCode rcomRule;
    private GenericCode nadmRule;
    private GenericCode ncomRule;
    private Double radmTierAmt;
    private Double rcomTierAmt;
    private Double nadmTierAmt;
    private Double ncomTierAmt;
    private Double currentAdjFactor;
    private String specialRemarks;
    private String specialRemarksForQuot;
    private String defaultPortOfDischarge;
    private String transhipment;
    private String temporaryText;
    private Date temporaryDate;
    private Date expirationDate;
    private String originRemarks;
    private String insuranceAllowed;
    private String defaultMasterSettings;
    private String brandField;

    public String getOriginRemarks() {
        return originRemarks;
    }

    public void setOriginRemarks(String originRemarks) {
        this.originRemarks = originRemarks;
    }

    public String getDefaultPortOfDischarge() {
        return defaultPortOfDischarge;
    }

    public void setDefaultPortOfDischarge(String defaultPortOfDischarge) {
        this.defaultPortOfDischarge = defaultPortOfDischarge;
    }

    public RefTerminalTemp getTrmnum() {
        return trmnum;
    }

    public void setTrmnum(RefTerminalTemp trmnum) {
        this.trmnum = trmnum;
    }

    public String getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(String transhipment) {
        this.transhipment = transhipment;
    }

    public GenericCode getBlClauseId() {
        return blClauseId;
    }

    public void setBlClauseId(GenericCode blClauseId) {
        this.blClauseId = blClauseId;
    }

    public Double getCurrentAdjFactor() {
        return currentAdjFactor;
    }

    public void setCurrentAdjFactor(Double currentAdjFactor) {
        this.currentAdjFactor = currentAdjFactor;
    }

    public String getFclHouseBlGoCollect() {
        return fclHouseBlGoCollect;
    }

    public void setFclHouseBlGoCollect(String fclHouseBlGoCollect) {
        this.fclHouseBlGoCollect = fclHouseBlGoCollect;
    }

    public String getFclSsBlGoCollect() {
        return fclSsBlGoCollect;
    }

    public void setFclSsBlGoCollect(String fclSsBlGoCollect) {
        this.fclSsBlGoCollect = fclSsBlGoCollect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLineManager() {
        return lineManager;
    }

    public void setLineManager(String lineManager) {
        this.lineManager = lineManager;
    }

    public Double getNadmAm() {
        return nadmAm;
    }

    public void setNadmAm(Double nadmAm) {
        this.nadmAm = nadmAm;
    }

    public Double getNadmTierAmt() {
        return nadmTierAmt;
    }

    public void setNadmTierAmt(Double nadmTierAmt) {
        this.nadmTierAmt = nadmTierAmt;
    }

    public Double getNcomAm() {
        return ncomAm;
    }

    public void setNcomAm(Double ncomAm) {
        this.ncomAm = ncomAm;
    }

    public Double getNcomTierAmt() {
        return ncomTierAmt;
    }

    public void setNcomTierAmt(Double ncomTierAmt) {
        this.ncomTierAmt = ncomTierAmt;
    }

    public String getQuoteClause() {
        return quoteClause;
    }

    public void setQuoteClause(String quoteClause) {
        this.quoteClause = quoteClause;
    }

    public Double getRadmAm() {
        return radmAm;
    }

    public void setRadmAm(Double radmAm) {
        this.radmAm = radmAm;
    }

    public Double getRadmTierAmt() {
        return radmTierAmt;
    }

    public void setRadmTierAmt(Double radmTierAmt) {
        this.radmTierAmt = radmTierAmt;
    }

    public Double getRcomAm() {
        return rcomAm;
    }

    public void setRcomAm(Double rcomAm) {
        this.rcomAm = rcomAm;
    }

    public GenericCode getNadmRule() {
        return nadmRule;
    }

    public void setNadmRule(GenericCode nadmRule) {
        this.nadmRule = nadmRule;
    }

    public GenericCode getNcomRule() {
        return ncomRule;
    }

    public void setNcomRule(GenericCode ncomRule) {
        this.ncomRule = ncomRule;
    }

    public GenericCode getRadmRule() {
        return radmRule;
    }

    public void setRadmRule(GenericCode radmRule) {
        this.radmRule = radmRule;
    }

    public GenericCode getRcomRule() {
        return rcomRule;
    }

    public void setRcomRule(GenericCode rcomRule) {
        this.rcomRule = rcomRule;
    }

    public Double getRcomTierAmt() {
        return rcomTierAmt;
    }

    public void setRcomTierAmt(Double rcomTierAmt) {
        this.rcomTierAmt = rcomTierAmt;
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
    }

    public String getSpecialRemarksForQuot() {
        return specialRemarksForQuot;
    }

    public void setSpecialRemarksForQuot(String specialRemarksForQuot) {
        this.specialRemarksForQuot = specialRemarksForQuot;
    }

    public String getSrvcFcl() {
        return srvcFcl;
    }

    public void setSrvcFcl(String srvcFcl) {
        this.srvcFcl = srvcFcl;
    }

    public String getCubeWtMandatoryFcl() {
        return cubeWtMandatoryFcl;
    }

    public void setCubeWtMandatoryFcl(String cubeWtMandatoryFcl) {
        this.cubeWtMandatoryFcl = cubeWtMandatoryFcl;
    }

    public Integer getShedulenumber() {
        return shedulenumber;
    }

    public void setShedulenumber(Integer shedulenumber) {
        this.shedulenumber = shedulenumber;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getTemporaryDate() {
        return temporaryDate;
    }

    public void setTemporaryDate(Date temporaryDate) {
        this.temporaryDate = temporaryDate;
    }

    public String getTemporaryText() {
        return temporaryText;
    }

    public void setTemporaryText(String temporaryText) {
        this.temporaryText = temporaryText;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getInsuranceAllowed() {
        return insuranceAllowed;
    }

    public void setInsuranceAllowed(String insuranceAllowed) {
        this.insuranceAllowed = insuranceAllowed;
    }

    public String getDefaultMasterSettings() {
        return defaultMasterSettings;
    }

    public void setDefaultMasterSettings(String defaultMasterSettings) {
        this.defaultMasterSettings = defaultMasterSettings;
    }

    public String getBrandField() {
        return brandField;
    }

    public void setBrandField(String brandField) {
        this.brandField = brandField;
    }

    }
