package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class RetailRatesBean implements Serializable {
private String match;
private String standard;
private String asFrfgted;
private String metric;



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
}
