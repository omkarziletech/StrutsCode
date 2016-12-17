package com.gp.cong.logisoft.domain;

import java.io.Serializable;

import com.gp.cong.logisoft.struts.form.ScanForm;

public class ScanConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String screenName;
    private String documentType;
    private String documentName;
    private String fileLocation;
    private String totalScan;
    private String totalAttach;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getTotalScan() {
        return totalScan;
    }

    public void setTotalScan(String totalScan) {
        this.totalScan = totalScan;
    }

    public String getTotalAttach() {
        return totalAttach;
    }

    public void setTotalAttach(String totalAttach) {
        this.totalAttach = totalAttach;
    }

    public ScanConfig() {
    }

    public ScanConfig(ScanForm scanForm) {
        this.screenName = scanForm.getScreenName().toUpperCase();
//		this.documentType = scanForm.getDocumentType().toUpperCase();
        this.documentName = scanForm.getDocumentName().toUpperCase();
    }
}
