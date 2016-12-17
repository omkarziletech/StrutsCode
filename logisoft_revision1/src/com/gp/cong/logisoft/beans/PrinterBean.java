package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class PrinterBean implements Serializable {
private String printerType;
private String printerName;
public String getPrinterName() {
	return printerName;
}
public void setPrinterName(String printerName) {
	this.printerName = printerName;
}
public String getPrinterType() {
	return printerType;
}
public void setPrinterType(String printerType) {
	this.printerType = printerType;
}

}
