package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class PrintConfig implements Serializable, Comparable<PrintConfig> {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String screenName;
    private String documentType;
    private String documentName;
    private String fileLocation;
    private String totalScan;
    private String totalAttach;
    private String printerName;
    private String allowPrint;
    private String enableDisablePrint;
    private Integer printCopy;
    private String toFaxNumber;
    private String exportBillToParty;
    private String printerTray;
    private boolean showOnScreen;

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

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getAllowPrint() {
        return allowPrint;
    }

    public void setAllowPrint(String allowPrint) {
        this.allowPrint = allowPrint;
    }

    public Integer getPrintCopy() {
        return printCopy;
    }

    public void setPrintCopy(Integer printCopy) {
        this.printCopy = printCopy;
    }

    public String getToFaxNumber() {
        return toFaxNumber;
    }

    public void setToFaxNumber(String toFaxNumber) {
        this.toFaxNumber = toFaxNumber;
    }

    public String getEnableDisablePrint() {
        return enableDisablePrint;
    }

    public void setEnableDisablePrint(String enableDisablePrint) {
        this.enableDisablePrint = enableDisablePrint;
    }

    public String getExportBillToParty() {
        return exportBillToParty;
    }

    public void setExportBillToParty(String exportBillToParty) {
        this.exportBillToParty = exportBillToParty;
    }

    public String getPrinterTray() {
        return printerTray;
    }

    public void setPrinterTray(String printerTray) {
        this.printerTray = printerTray;
    }

    public int compareTo(PrintConfig o) {
        if (this.id > o.id) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isShowOnScreen() {
        return showOnScreen;
    }

    public void setShowOnScreen(boolean showOnScreen) {
        this.showOnScreen = showOnScreen;
    }
}
