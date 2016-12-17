package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class UserPrinterAssociation implements Auditable, Serializable {

    private Integer id;
    private Integer userId;
    private PrintConfig documentId;
    private String printerName;
    private String printerTray;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public PrintConfig getDocumentId() {
        return documentId;
    }

    public void setDocumentId(PrintConfig documentId) {
        this.documentId = documentId;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterTray() {
        return printerTray;
    }

    public void setPrinterTray(String printerTray) {
        this.printerTray = printerTray;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }
}
