package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.domain.GenericCode;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class JournalEntry
        implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = 5052656435156769707L;
    private String journalEntryId;
    private String journalEntryDesc;
    private Integer batchId;
    private Date jeDate = new Date();
    private String period;
    private GenericCode sourceCode;
    private String sourceCodeDesc;
    private Double debit = Double.valueOf(0.0D);
    private Double credit = Double.valueOf(0.0D);
    private String memo;
    private Set lineItemSet;
    private String flag;
    private String subledgerClose = "N";

    public String getJournalEntryId() {
        return this.journalEntryId;
    }

    public void setJournalEntryId(String journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public String getJournalEntryDesc() {
        return this.journalEntryDesc;
    }

    public void setJournalEntryDesc(String journalEntryDesc) {
        this.journalEntryDesc = journalEntryDesc;
    }

    public Integer getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Date getJeDate() {
        return this.jeDate;
    }

    public void setJeDate(Date jeDate) {
        this.jeDate = jeDate;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSourceCodeDesc() {
        return this.sourceCodeDesc;
    }

    public void setSourceCodeDesc(String sourceCodeDesc) {
        this.sourceCodeDesc = sourceCodeDesc;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public GenericCode getSourceCode() {
        return this.sourceCode;
    }

    public void setSourceCode(GenericCode sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Set getLineItemSet() {
        return this.lineItemSet;
    }

    public void setLineItemSet(Set lineItemSet) {
        this.lineItemSet = lineItemSet;
    }

    public Double getCredit() {
        return this.credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        return this.debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSubledgerClose() {
        return this.subledgerClose;
    }

    public void setSubledgerClose(String subledgerClose) {
        this.subledgerClose = subledgerClose;
    }
}