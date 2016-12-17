package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonConstants;
import org.apache.struts.action.ActionForm;

import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;

public class RecieptsLedgerForm extends ActionForm {

    private static final long serialVersionUID = 5686976025831442944L;
    private String subLedgerType;
    private String period;
    private String startDate;
    private String endDate;
    private String buttonValue;
    private String index;
    private String posted = "no";
    private String undoBatch;
    private String sortBy = CommonConstants.SORT_BY_CHARGECODE;
    private String chargeCode;
    private String startGLAccount;
    private String endGLAccount;
    private String revOrExp = GLMappingConstant.REVENUE;
    private Integer page;
    private String orderedBy;
    private String sortedBy;

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSubLedgerType() {
        return subLedgerType;
    }

    public void setSubLedgerType(String subLedgerType) {
        this.subLedgerType = subLedgerType;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getUndoBatch() {
        return undoBatch;
    }

    public void setUndoBatch(String undoBatch) {
        this.undoBatch = undoBatch;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getStartGLAccount() {
        return startGLAccount;
    }

    public void setStartGLAccount(String startGLAccount) {
        this.startGLAccount = startGLAccount;
    }

    public String getEndGLAccount() {
        return endGLAccount;
    }

    public void setEndGLAccount(String endGLAccount) {
        this.endGLAccount = endGLAccount;
    }

    public String getRevOrExp() {
        return revOrExp;
    }

    public void setRevOrExp(String revOrExp) {
        this.revOrExp = revOrExp;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }
}
