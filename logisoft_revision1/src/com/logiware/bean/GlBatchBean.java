package com.logiware.bean;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;

public class GlBatchBean
        implements ConstantsInterface {

    private String id;
    private String description;
    private String subledgerType;
    private String debit = "0.00";
    private String credit = "0.00";
    private boolean readyToPost;
    private String type;
    private String status = "Open";
    private String period;
    private boolean uploaded;

    public GlBatchBean() {
    }

    public GlBatchBean(Batch batch)throws Exception {
        this.id = batch.getBatchId().toString();
        this.description = batch.getBatchDesc();
        this.subledgerType = (null != batch.getSourceLedger() ? batch.getSourceLedger().getId().toString() : "");
        this.readyToPost = CommonUtils.isEqual(batch.getReadyToPost(), "Y");
        this.type = batch.getType();
        this.debit = NumberUtils.formatNumber(batch.getTotalDebit(), "#########0.00");
        this.credit = NumberUtils.formatNumber(batch.getTotalCredit(), "#########0.00");
        this.status = batch.getStatus();
        this.uploaded = new DocumentStoreLogDAO().isUploaded(batch.getBatchId().toString(),"JOURNAL ENTRY","JOURNAL ENTRY");
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCredit() {
        return this.credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
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

    public boolean isReadyToPost() {
        return this.readyToPost;
    }

    public void setReadyToPost(boolean readyToPost) {
        this.readyToPost = readyToPost;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}