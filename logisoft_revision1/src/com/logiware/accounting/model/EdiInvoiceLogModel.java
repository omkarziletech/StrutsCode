package com.logiware.accounting.model;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceLogModel {

    private Integer id;
    private String fileName;
    private String error;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getError() {
	return error;
    }

    public void setError(String error) {
	this.error = error;
    }
}
