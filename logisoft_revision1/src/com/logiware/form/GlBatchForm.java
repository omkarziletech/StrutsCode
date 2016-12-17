package com.logiware.form;

import com.logiware.bean.GlBatchBean;
import com.logiware.bean.JournalEntryBean;
import com.logiware.bean.LineItemBean;
import com.logiware.hibernate.dao.GlBatchDAO;
import com.logiware.utils.LineItemComparator;
import com.logiware.utils.ListUtils;
import java.util.Collections;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;

public class GlBatchForm extends ActionForm {

    private String action;
    private Integer pageNo = 1;
    private Integer currentPageSize = 100;
    private Integer totalPageSize = 0;
    private Integer noOfPages = 0;
    private Integer noOfRecords = 0;
    private String sortBy = "batchId";
    private String orderBy = "desc";
    private String batchId;
    private String status = "Open";
    private String subledgerType;
    private List<GlBatchBean> glBatches;
    private GlBatchBean glBatch = new GlBatchBean();
    private JournalEntryBean journalEntry = new JournalEntryBean();
    private List<JournalEntryBean> journalEntries;
    private List<LineItemBean> lineItems;
    private Integer batchIndex = 0;
    private Integer journalEntryIndex = 0;
    private String newGlBatchId;
    private String batchDebit = "0.00";
    private String batchCredit = "0.00";
    private String batchFileName;
    private String journalEntryFileName;
    private String from;
    private Integer year;

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getCurrentPageSize() {
        return this.currentPageSize;
    }

    public void setCurrentPageSize(Integer currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public List<GlBatchBean> getGlBatches() {
        return this.glBatches;
    }

    public void setGlBatches(List<GlBatchBean> glBatches) {
        this.glBatches = glBatches;
    }

    public Integer getNoOfPages() {
        return this.noOfPages;
    }

    public void setNoOfPages(Integer noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Integer getNoOfRecords() {
        return this.noOfRecords;
    }

    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getTotalPageSize() {
        return this.totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public String getBatchId() {
        return this.batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubledgerType() {
        return this.subledgerType;
    }

    public void setSubledgerType(String subledgerType) {
        this.subledgerType = subledgerType;
    }

    public GlBatchBean getGlBatch() {
        return this.glBatch;
    }

    public void setGlBatch(GlBatchBean glBatch) {
        this.glBatch = glBatch;
    }

    public List<JournalEntryBean> getJournalEntries() {
        return this.journalEntries;
    }

    public void setJournalEntries(List<JournalEntryBean> journalEntries) {
        this.journalEntries = journalEntries;
    }

    public JournalEntryBean getJournalEntry() {
        return this.journalEntry;
    }

    public void setJournalEntry(JournalEntryBean journalEntry) {
        this.journalEntry = journalEntry;
    }

    public List<LineItemBean> getLineItems() throws Exception {
        if (null == this.lineItems) {
            this.lineItems = ListUtils.lazyList(LineItemBean.class);
        } else {
            Collections.sort(lineItems, new LineItemComparator());
        }
        return this.lineItems;
    }

    public void setLineItems(List<LineItemBean> lineItems) {
        this.lineItems = lineItems;
    }

    public List<LabelValueBean> getSubledgerTypes() {
        return new GlBatchDAO().getSubledgerTypes();
    }

    public Integer getNoOfBatches() {
        return new GlBatchDAO().getNoOfBatches();
    }

    public Integer getNoOfJournalEntries() {
        return new GlBatchDAO().getNoOfJournalEntries(Integer.valueOf(this.glBatch.getId()));
    }

    public Integer getBatchIndex() {
        return this.batchIndex;
    }

    public void setBatchIndex(Integer batchIndex) {
        this.batchIndex = batchIndex;
    }

    public Integer getJournalEntryIndex() {
        return this.journalEntryIndex;
    }

    public void setJournalEntryIndex(Integer journalEntryIndex) {
        this.journalEntryIndex = journalEntryIndex;
    }

    public String getNewGlBatchId() {
        return this.newGlBatchId;
    }

    public void setNewGlBatchId(String newGlBatchId) {
        this.newGlBatchId = newGlBatchId;
    }

    public String getBatchCredit() {
        return this.batchCredit;
    }

    public void setBatchCredit(String batchCredit) {
        this.batchCredit = batchCredit;
    }

    public String getBatchDebit() {
        return this.batchDebit;
    }

    public void setBatchDebit(String batchDebit) {
        this.batchDebit = batchDebit;
    }

    public String getBatchFileName() {
        return batchFileName;
    }

    public void setBatchFileName(String batchFileName) {
        this.batchFileName = batchFileName;
    }

    public String getJournalEntryFileName() {
        return journalEntryFileName;
    }

    public void setJournalEntryFileName(String journalEntryFileName) {
        this.journalEntryFileName = journalEntryFileName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
