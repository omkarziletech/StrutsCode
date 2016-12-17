package com.logiware.bean;

import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.hibernate.dao.GlBatchDAO;
import java.util.Date;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

public class JournalEntryBean {

    private String id;
    private String description;
    private String batchId;
    private String date;
    private String period;
    private String subledgerType;
    private String subledgerCode;
    private String subledgerDescription;
    private String debit = "0.00";
    private String credit = "0.00";
    private String memo;
    private String flag;
    private String subledgerClose;
    private boolean uploaded;

    public JournalEntryBean() {
    }

    public JournalEntryBean(JournalEntry journalEntry)throws Exception {
        this.id = journalEntry.getJournalEntryId();
        this.description = journalEntry.getJournalEntryDesc();
        this.batchId = journalEntry.getBatchId().toString();
        this.date = DateUtils.formatDate(null != journalEntry.getJeDate() ? journalEntry.getJeDate() : new Date(), "MM/dd/yyyy");
        this.period = journalEntry.getPeriod();
        this.subledgerType = (null != journalEntry.getSourceCode() ? journalEntry.getSourceCode().getId().toString() : "");
        this.subledgerCode = (null != journalEntry.getSourceCode() ? journalEntry.getSourceCode().getCode() : "");
        this.subledgerDescription = journalEntry.getSourceCodeDesc();
        this.memo = journalEntry.getMemo();
        this.flag = journalEntry.getFlag();
        this.subledgerClose = journalEntry.getSubledgerClose();
        this.debit = NumberUtils.formatNumber(journalEntry.getDebit(), "#0.00");
        this.credit = NumberUtils.formatNumber(journalEntry.getCredit(), "#0.00");
        this.uploaded = new DocumentStoreLogDAO().isUploaded(journalEntry.getJournalEntryId(),"JOURNAL ENTRY","JOURNAL ENTRY");
    }

    public String getBatchId() {
        return this.batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCredit() {
        return this.credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDebit() {
        return this.debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPeriod()throws Exception {
        if (null == this.period) {
            FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
            this.period = fiscalPeriod.getPeriod();
        }
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSubledgerClose() {
        return this.subledgerClose;
    }

    public void setSubledgerClose(String subledgerClose) {
        this.subledgerClose = subledgerClose;
    }

    public String getSubledgerDescription() {
        return this.subledgerDescription;
    }

    public void setSubledgerDescription(String subledgerDescription) {
        this.subledgerDescription = subledgerDescription;
    }

    public String getSubledgerType() {
        return this.subledgerType;
    }

    public void setSubledgerType(String subledgerType) {
        this.subledgerType = subledgerType;
    }

    public List<LabelValueBean> getPeriods()throws Exception {
        return new GlBatchDAO().getPeriods();
    }

    public String getSubledgerCode() {
        return this.subledgerCode;
    }

    public void setSubledgerCode(String subledgerCode) {
        this.subledgerCode = subledgerCode;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}