package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class Printer implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer printerId;
    private Integer document;
    private String printerName;

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Integer getDocument() {
        return document;
    }

    public void setDocument(Integer document) {
        this.document = document;
    }
}
