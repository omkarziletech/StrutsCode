/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import java.math.BigDecimal;

/**
 *
 * @author Mohanapriya
 */
public class LclSegregationForm extends LogiwareActionForm {

    private Long fileId;
    private Long amsHblId;
    private String segDest;
    private Integer segDestId;
    private String amsHbl;
    private Boolean transshipment;
    private Integer pieces;
    private BigDecimal cbm;
    private BigDecimal kgs;
    private BigDecimal cft;
    private BigDecimal lbs;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getAmsHblId() {
        return amsHblId;
    }

    public void setAmsHblId(Long amsHblId) {
        this.amsHblId = amsHblId;
    }

    public String getSegDest() {
        return segDest;
    }

    public void setSegDest(String segDest) {
        this.segDest = segDest;
    }

    public Integer getSegDestId() {
        return segDestId;
    }

    public void setSegDestId(Integer segDestId) {
        this.segDestId = segDestId;
    }

    public String getAmsHbl() {
        return amsHbl;
    }

    public void setAmsHbl(String amsHbl) {
        this.amsHbl = amsHbl;
    }

    public Boolean getTransshipment() {
        return transshipment;
    }

    public void setTransshipment(Boolean transshipment) {
        this.transshipment = transshipment;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public BigDecimal getCbm() {
        return cbm;
    }

    public void setCbm(BigDecimal cbm) {
        this.cbm = cbm;
    }

    public BigDecimal getKgs() {
        return kgs;
    }

    public void setKgs(BigDecimal kgs) {
        this.kgs = kgs;
    }

    public BigDecimal getCft() {
        return cft;
    }

    public void setCft(BigDecimal cft) {
        this.cft = cft;
    }

    public BigDecimal getLbs() {
        return lbs;
    }

    public void setLbs(BigDecimal lbs) {
        this.lbs = lbs;
    }
    
    

}
