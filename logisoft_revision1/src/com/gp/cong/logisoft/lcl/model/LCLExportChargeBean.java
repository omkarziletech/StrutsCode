
package com.gp.cong.logisoft.lcl.model;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cvst.logisoft.domain.GlMapping;
import java.math.BigDecimal;
import java.util.Date;

public class LCLExportChargeBean {
    private Long id;
    private LclBl lclBl;
    private GlMapping arglMapping;
    private String arBillToParty;
    private BigDecimal rolledupCharges;
    private BigDecimal adjustmentAmount;
    private String adjustmentComment;
    private boolean manualEntry;
    private boolean bundleIntoOf;
    private boolean printOnBl;
    private Date modifiedDatetime;
    private User modifiedBy;
    
    private String label2;
    private String consolidateCharges;
    private String label1;
    
    private Long fileNumberId;
    private String fileNumber;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LclBl getLclBl() {
        return lclBl;
    }

    public void setLclBl(LclBl lclBl) {
        this.lclBl = lclBl;
    }

    public GlMapping getArglMapping() {
        return arglMapping;
    }

    public void setArglMapping(GlMapping arglMapping) {
        this.arglMapping = arglMapping;
    }

    public String getArBillToParty() {
        return arBillToParty;
    }

    public void setArBillToParty(String arBillToParty) {
        this.arBillToParty = arBillToParty;
    }

    public BigDecimal getRolledupCharges() {
        return rolledupCharges;
    }

    public void setRolledupCharges(BigDecimal rolledupCharges) {
        this.rolledupCharges = rolledupCharges;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getAdjustmentComment() {
        return adjustmentComment;
    }

    public void setAdjustmentComment(String adjustmentComment) {
        this.adjustmentComment = adjustmentComment;
    }

    public boolean isManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public boolean isBundleIntoOf() {
        return bundleIntoOf;
    }

    public void setBundleIntoOf(boolean bundleIntoOf) {
        this.bundleIntoOf = bundleIntoOf;
    }

    public boolean isPrintOnBl() {
        return printOnBl;
    }

    public void setPrintOnBl(boolean printOnBl) {
        this.printOnBl = printOnBl;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getConsolidateCharges() {
        return consolidateCharges;
    }

    public void setConsolidateCharges(String consolidateCharges) {
        this.consolidateCharges = consolidateCharges;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

}
