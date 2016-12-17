package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class CodeBean implements Serializable {
private String codeTypeId;
private String codeValue;
private String codeDesc;
private String match;
public String getMatch() {
	return match;
}
public void setMatch(String match) {
	this.match = match;
}
public String getCodeDesc() {
	return codeDesc;
}
public void setCodeDesc(String codeDesc) {
	this.codeDesc = codeDesc;
}
public String getCodeTypeId() {
	return codeTypeId;
}
public void setCodeTypeId(String codeTypeId) {
	this.codeTypeId = codeTypeId;
}
public String getCodeValue() {
	return codeValue;
}
public void setCodeValue(String codeValue) {
	this.codeValue = codeValue;
}
}
