/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form;

import com.logiware.form.EventForm;

/**
 *
 * @author Meiyazhakan.R
 */
public class BlueScreenNotesForm extends EventForm {

    private String methodName;
    private String customerName;
    private String customerNo;
    private String fileNo;
    private String noteType;
    private String noteSymbol;
    private String sortBy;
    private String orderBy;
    private String tpNoteType;
    private String tpNoteDesc;
    private Long tpId;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteSymbol() {
        return noteSymbol;
    }

    public void setNoteSymbol(String noteSymbol) {
        this.noteSymbol = noteSymbol;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getTpNoteType() {
        return tpNoteType;
    }

    public void setTpNoteType(String tpNoteType) {
        this.tpNoteType = tpNoteType;
    }

    public String getTpNoteDesc() {
        return tpNoteDesc;
    }

    public void setTpNoteDesc(String tpNoteDesc) {
        this.tpNoteDesc = tpNoteDesc;
    }

    public Long getTpId() {
        return tpId;
    }

    public void setTpId(Long tpId) {
        this.tpId = tpId;
    }
    
}
