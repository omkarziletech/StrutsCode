/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

/** 
 * MyEclipse Struts
 * Creation date: 04-15-2009fclAESDetails
 * 
 * XDoclet definition:
 * @struts.form name="InbondDetailsForm"
 */
public class InbondDetailsForm extends ActionForm {
	/*
	 * Generated Methods
	 */
private String fileNo;
private String buttonValue;
private String bolId;
private String inbondId;
private String inbondDate;
private String inbondPort;
private String inbondType;
private String inbondNumber;
private String podValues;
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

    public String getInbondId() {
        return inbondId;
    }

    public void setInbondId(String inbondId) {
        this.inbondId = inbondId;
    }
	
	

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

    public String getInbondDate() {
        return inbondDate;
    }

    public void setInbondDate(String inbondDate) {
        this.inbondDate = inbondDate;
    }

    public String getInbondNumber() {
        return null != inbondNumber?inbondNumber.toUpperCase():"";
    }

    public void setInbondNumber(String inbondNumber) {
        this.inbondNumber = null != inbondNumber?inbondNumber.toUpperCase():"";
    }

    public String getInbondPort() {
        return inbondPort;
    }

    public void setInbondPort(String inbondPort) {
        this.inbondPort = inbondPort;
    }

    public String getInbondType() {
        return inbondType;
    }

    public void setInbondType(String inbondType) {
        this.inbondType = inbondType;
    }

    public String getPodValues() {
        return podValues;
    }

    public void setPodValues(String podValues) {
        this.podValues = podValues;
    }
   
        
}