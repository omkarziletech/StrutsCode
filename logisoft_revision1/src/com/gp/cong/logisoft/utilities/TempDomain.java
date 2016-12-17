package com.gp.cong.logisoft.utilities;

public class TempDomain {
	private String comments;
	private String FileNo;
	private Integer chargesId;
	private String chargesCode;
	private String unitType;
	private String module;
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	
	public Integer getChargesId() {
		return chargesId;
	}
	public void setChargesId(Integer chargesId) {
		this.chargesId = chargesId;
	}
	
	public String getFileNo() {
		return FileNo;
	}
	public void setFileNo(String fileNo) {
		FileNo = fileNo;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getChargesCode() {
		return chargesCode;
	}
	public void setChargesCode(String chargesCode) {
		this.chargesCode = chargesCode;
	}
}
