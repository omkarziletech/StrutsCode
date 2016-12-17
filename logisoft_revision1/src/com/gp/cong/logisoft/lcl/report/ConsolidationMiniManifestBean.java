/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

/**
 *
 * @author Logiware
 */
public class ConsolidationMiniManifestBean {

    private Long FileId;
    private String fileNumber;
    private Long consolidateFile;
    private String supplierName;
    private Integer piece;
    private String packageName;
    private Double cft;
    private Double kgs;
    private String comDescrption;
    private String customerPo;

    public Long getFileId() {
        return FileId;
    }

    public void setFileId(Long FileId) {
        this.FileId = FileId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getConsolidateFile() {
        return consolidateFile;
    }

    public void setConsolidateFile(Long consolidateFile) {
        this.consolidateFile = consolidateFile;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Double getCft() {
        return cft;
    }

    public void setCft(Double cft) {
        this.cft = cft;
    }

    public Double getKgs() {
        return kgs;
    }

    public void setKgs(Double kgs) {
        this.kgs = kgs;
    }

    public String getComDescrption() {
        return comDescrption;
    }

    public void setComDescrption(String comDescrption) {
        this.comDescrption = comDescrption;
    }

    public String getCustomerPo() {
        return customerPo;
    }

    public void setCustomerPo(String customerPo) {
        this.customerPo = customerPo;
    }

}
