package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class AccountStructureBean implements Serializable {
	/* to set the bean values for propeties in display tag
	 by chandu
	 * */
private Integer id;
private String seg_code;
private String seg_desc;
private Integer seg_leng;
private Integer Acct_Struct_id;
private Integer acctId;
private String AcctName;
private String AcctDesc;
private String validateList;



public String getValidateList() {
	return validateList;
}
public void setValidateList(String validateList) {
	this.validateList = validateList;
}
public String getAcctDesc() {
	return AcctDesc;
}
public void setAcctDesc(String acctDesc) {
	AcctDesc = acctDesc;
}
public Integer getAcctId() {
	return acctId;
}
public void setAcctId(Integer acctId) {
	this.acctId = acctId;
}
public String getAcctName() {
	return AcctName;
}
public void setAcctName(String acctName) {
	AcctName = acctName;
}
public Integer getAcct_Struct_id() {
	return Acct_Struct_id;
}
public void setAcct_Struct_id(Integer acct_Struct_id) {
	Acct_Struct_id = acct_Struct_id;
}
public Integer getSeg_leng() {
	return seg_leng;
}
public void setSeg_leng(Integer seg_leng) {
	this.seg_leng = seg_leng;
}
public String getSeg_code() {
	return seg_code;
}
public void setSeg_code(String seg_code) {
	this.seg_code = seg_code;
}
public String getSeg_desc() {
	return seg_desc;
}
public void setSeg_desc(String seg_desc) {
	this.seg_desc = seg_desc;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}





}
