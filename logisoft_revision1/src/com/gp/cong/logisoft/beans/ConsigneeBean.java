package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class ConsigneeBean implements Serializable {
private String consNo;
private String consName;


private String buttonValue;

/**
 * @return the buttonValue
 */
public String getButtonValue() {
	return buttonValue;
}
/**
 * @param buttonValue the buttonValue to set
 */
public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
}
public String getConsName() {
	return consName;
}
public void setConsName(String consName) {
	this.consName = consName;
}
public String getConsNo() {
	return consNo;
}
public void setConsNo(String consNo) {
	this.consNo = consNo;
}

}
