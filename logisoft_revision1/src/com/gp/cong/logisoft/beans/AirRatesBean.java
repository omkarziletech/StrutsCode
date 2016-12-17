package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class AirRatesBean implements Serializable {
private String match;
private String standard;
private String asFrfgted;
private String metric;
private String exclude;
private String common;
private String noncommon;
private String ocean;
private String showOnSailingSchedule;

public String getShowOnSailingSchedule() {
return showOnSailingSchedule;
}

public void setShowOnSailingSchedule(String showOnSailingSchedule) {
this.showOnSailingSchedule = showOnSailingSchedule;
}



public String getOcean() {
	return ocean;
}

public void setOcean(String ocean) {
	this.ocean = ocean;
}

public String getCommon() {
	return common;
}

public void setCommon(String common) {
	this.common = common;
}

public String getExclude() {
	return exclude;
}

public void setExclude(String exclude) {
	this.exclude = exclude;
}

public String getMetric() {
	return metric;
}

public void setMetric(String metric) {
	this.metric = metric;
}

public String getAsFrfgted() {
	return asFrfgted;
}

public void setAsFrfgted(String asFrfgted) {
	this.asFrfgted = asFrfgted;
}

public String getStandard() {
	return standard;
}

public void setStandard(String standard) {
	this.standard = standard;
}

public String getMatch() {
	return match;
}

public void setMatch(String match) {
	this.match = match;
}

public String getNoncommon() {
	return noncommon;
}

public void setNoncommon(String noncommon) {
	this.noncommon = noncommon;
}
}
