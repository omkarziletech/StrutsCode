/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

/** 
 * MyEclipse Struts
 * Creation date: 01-08-2008
 * 
 * XDoclet definition:
 * @struts.form name="AesHistoryForm"
 */
public class AesHistoryForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String bolId;
	private String fclBlNo;
	private String status;
	private String itnNumber;
	private String aesException;
	private String fileNumber;
	private String buttonValue;

    public String getAesException() {
        return aesException;
    }

    public void setAesException(String aesException) {
        this.aesException = aesException;
    }

    public String getBolId() {
        return bolId;
    }

    public void setBolId(String bolId) {
        this.bolId = bolId;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getFclBlNo() {
        return fclBlNo;
    }

    public void setFclBlNo(String fclBlNo) {
        this.fclBlNo = fclBlNo;
    }

    public String getItnNumber() {
        return itnNumber;
    }

    public void setItnNumber(String itnNumber) {
        this.itnNumber = itnNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
        
}