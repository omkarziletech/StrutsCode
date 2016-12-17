package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.domain.GenericCode;
import java.io.Serializable;
import java.util.Set;
import org.json.JSONObject;

public class Batch
        implements Serializable, ConstantsInterface {

    private Integer batchId;
    private String batchDesc;
    private GenericCode sourceLedger;
    private String type = "manual";
    private Double totalCredit = Double.valueOf(0.0D);
    private Double totalDebit = Double.valueOf(0.0D);
    private String readyToPost = "N";
    private String status = "Open";
    private Set journalEntrySet;

    public Batch() {
    }

    public Batch(String batchDesc, GenericCode sourceLedger, String type, Double totalCredit, Double totalDebit, String readyToPost, String status) {
        this.batchDesc = batchDesc;
        this.sourceLedger = sourceLedger;
        this.type = type;
        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
        this.readyToPost = readyToPost;
        this.status = status;
    }

    public Batch(String batchDesc, GenericCode sourceLedger, String type, String totalCredit, String totalDebit, String readyToPost, String status) {
        this.batchDesc = batchDesc;
        this.sourceLedger = sourceLedger;
        this.type = type;
        if ((totalCredit == null) || (totalCredit.equals(""))) {
            totalCredit = "0";
        }
        if ((totalDebit == null) || (totalDebit.equals(""))) {
            totalDebit = "0";
        }

        this.totalCredit = new Double(totalCredit);
        this.totalDebit = new Double(totalDebit);
        this.readyToPost = readyToPost;
        this.status = status;
    }

    public String getBatchDesc() {
        return this.batchDesc;
    }

    public void setBatchDesc(String batchDesc) {
        this.batchDesc = batchDesc;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTotalCredit() {
        return this.totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Double getTotalDebit() {
        return this.totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getReadyToPost() {
        return this.readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
        this.readyToPost = readyToPost;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set getJournalEntrySet() {
        return this.journalEntrySet;
    }

    public void setJournalEntrySet(Set journalEntrySet) {
        this.journalEntrySet = journalEntrySet;
    }

    public GenericCode getSourceLedger() {
        return this.sourceLedger;
    }

    public void setSourceLedger(GenericCode sourceLedger) {
        this.sourceLedger = sourceLedger;
    }

    public Integer getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String toJSONString() throws Exception {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("batchDesc", "testing,,,,,");
        return jsonObj.toString();
    }
}
