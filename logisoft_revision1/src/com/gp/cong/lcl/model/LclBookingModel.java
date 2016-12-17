/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import java.math.BigDecimal;

/**
 *
 * @author Mei
 */
public class LclBookingModel {

    private Long fileId;
    private String fileNo;
    private String fileStatus;
    private String bkgSailDate;
    private String pooName;
    private String clientName;
    private String clientAcctNo;
    private Integer piece;
    private BigDecimal weightMetricKgs;
    private BigDecimal volumeMetricCbm;
    private BigDecimal weightImpLbs;
    private BigDecimal volumeImpcft;
    private String dispoStatus;
    private String consolidateFile;
    private String fileState;

    public String getBkgSailDate() {
        return bkgSailDate;
    }

    public void setBkgSailDate(String bkgSailDate) {
        this.bkgSailDate = bkgSailDate;
    }

    public String getClientAcctNo() {
        return clientAcctNo;
    }

    public void setClientAcctNo(String clientAcctNo) {
        this.clientAcctNo = clientAcctNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public String getPooName() {
        return pooName;
    }

    public void setPooName(String pooName) {
        this.pooName = pooName;
    }

    public BigDecimal getVolumeImpcft() {
        return volumeImpcft;
    }

    public void setVolumeImpcft(BigDecimal volumeImpcft) {
        this.volumeImpcft = volumeImpcft;
    }

    public BigDecimal getVolumeMetricCbm() {
        return volumeMetricCbm;
    }

    public void setVolumeMetricCbm(BigDecimal volumeMetricCbm) {
        this.volumeMetricCbm = volumeMetricCbm;
    }

    public BigDecimal getWeightImpLbs() {
        return weightImpLbs;
    }

    public void setWeightImpLbs(BigDecimal weightImpLbs) {
        this.weightImpLbs = weightImpLbs;
    }

    public BigDecimal getWeightMetricKgs() {
        return weightMetricKgs;
    }

    public void setWeightMetricKgs(BigDecimal weightMetricKgs) {
        this.weightMetricKgs = weightMetricKgs;
    }

    public String getDispoStatus() {
        return dispoStatus;
    }

    public void setDispoStatus(String dispoStatus) {
        this.dispoStatus = dispoStatus;
    }

    public String getConsolidateFile() {
        return consolidateFile;
    }

    public void setConsolidateFile(String consolidateFile) {
        this.consolidateFile = consolidateFile;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

}
