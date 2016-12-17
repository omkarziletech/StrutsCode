/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author lakshh
 */
public class ImportReleaseForm extends LogiwareActionForm {

    private String releaseBlNote;
    private String freightReleaseNote;
    private String fileNumberId;
    private String methodName;
    private String moduleName;
    private String entryNo;
    private String currentLoginName;
    private String currentuserId;
    private String expressRelease;

    public String getCurrentLoginName() {
        return currentLoginName;
    }

    public void setCurrentLoginName(String currentLoginName) {
        this.currentLoginName = currentLoginName;
    }

    public String getCurrentuserId() {
        return currentuserId;
    }

    public void setCurrentuserId(String currentuserId) {
        this.currentuserId = currentuserId;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getFreightReleaseNote() {
        return freightReleaseNote;
    }

    public void setFreightReleaseNote(String freightReleaseNote) {
        this.freightReleaseNote = freightReleaseNote;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getReleaseBlNote() {
        return releaseBlNote;
    }

    public void setReleaseBlNote(String releaseBlNote) {
        this.releaseBlNote = releaseBlNote;
    }

    public String getExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(String expressRelease) {
        this.expressRelease = expressRelease;
    }
}
