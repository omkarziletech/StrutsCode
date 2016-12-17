package com.gp.cvst.logisoft.domain;

import java.io.Serializable;

public class ApBatch implements Serializable {

    private Integer batchId;
    private String batchDesc;

    public ApBatch() {
    }

    public ApBatch(Integer batchId, String batchDesc) {
        super();
        this.batchId = batchId;
        this.batchDesc = batchDesc;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchDesc() {
        return batchDesc;
    }

    public void setBatchDesc(String batchDesc) {
        this.batchDesc = batchDesc;
    }
}
