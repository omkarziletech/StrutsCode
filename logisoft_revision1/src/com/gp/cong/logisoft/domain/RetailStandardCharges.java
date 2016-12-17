package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class RetailStandardCharges implements Serializable {
private Integer id;
private String orgTerminal;
private String destPort;
private String comCode;
private String orgTerminalName;
private String destPortName;
private String comCodeName;
private Double maxDocCharge;
private Double ffCommission;
private Double blBottomLine;
private Double CostCbm;
private Set retailStandardCharges;
private Set retailDocumentCharges;
private Set retailFlightSchedules; 
private Set retailWeightRangeSet;
private Set retailCommoditySet;

public Double getBlBottomLine() {
	return blBottomLine;
}
public void setBlBottomLine(Double blBottomLine) {
	this.blBottomLine = blBottomLine;
}


public Double getFfCommission() {
	return ffCommission;
}
public void setFfCommission(Double ffCommission) {
	this.ffCommission = ffCommission;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Double getMaxDocCharge() {
	return maxDocCharge;
}
public void setMaxDocCharge(Double maxDocCharge) {
	this.maxDocCharge = maxDocCharge;
}



public String getComCode() {
	return comCode;
}
public void setComCode(String comCode) {
	this.comCode = comCode;
}
public String getDestPort() {
	return destPort;
}
public void setDestPort(String destPort) {
	this.destPort = destPort;
}
public String getOrgTerminal() {
	return orgTerminal;
}
public void setOrgTerminal(String orgTerminal) {
	this.orgTerminal = orgTerminal;
}
public Set getRetailCommoditySet() {
	return retailCommoditySet;
}
public void setRetailCommoditySet(Set retailCommoditySet) {
	this.retailCommoditySet = retailCommoditySet;
}
public Set getRetailDocumentCharges() {
	return retailDocumentCharges;
}
public void setRetailDocumentCharges(Set retailDocumentCharges) {
	this.retailDocumentCharges = retailDocumentCharges;
}
public Set getRetailFlightSchedules() {
	return retailFlightSchedules;
}
public void setRetailFlightSchedules(Set retailFlightSchedules) {
	this.retailFlightSchedules = retailFlightSchedules;
}
public Set getRetailStandardCharges() {
	return retailStandardCharges;
}
public void setRetailStandardCharges(Set retailStandardCharges) {
	this.retailStandardCharges = retailStandardCharges;
}
public Set getRetailWeightRangeSet() {
	return retailWeightRangeSet;
}
public void setRetailWeightRangeSet(Set retailWeightRangeSet) {
	this.retailWeightRangeSet = retailWeightRangeSet;
}
public Double getCostCbm() {
	return CostCbm;
}
public void setCostCbm(Double costCbm) {
	CostCbm = costCbm;
}
public String getComCodeName() {
	return comCodeName;
}
public void setComCodeName(String comCodeName) {
	this.comCodeName = comCodeName;
}
public String getDestPortName() {
	return destPortName;
}
public void setDestPortName(String destPortName) {
	this.destPortName = destPortName;
}
public String getOrgTerminalName() {
	return orgTerminalName;
}
public void setOrgTerminalName(String orgTerminalName) {
	this.orgTerminalName = orgTerminalName;
}


}


