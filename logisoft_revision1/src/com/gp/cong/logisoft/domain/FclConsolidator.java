package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.List;

public class FclConsolidator implements Serializable {
private String charge;
private String rollToCharge;
private String display;
private String excludeFromTotal;
private static List fclConsolidatorList=null;
private static String originTerminal;
private static String destinationPort;

public static String getOriginTerminal() {
	return originTerminal;
}
public static void setOriginTerminal(String originTerminal) {
	FclConsolidator.originTerminal = originTerminal;
}
public static String getDestinationPort() {
	return destinationPort;
}
public static void setDestinationPort(String destinationPort) {
	FclConsolidator.destinationPort = destinationPort;
}
public String getCharge() {
	return charge;
}
public FclConsolidator(){
	
}
public FclConsolidator(String charge,String rollToCharge,String display,String excludeFromTotal)
{
	this.charge=charge;
	this.rollToCharge=rollToCharge;
	this.display=display;
	this.excludeFromTotal=excludeFromTotal;
}
public void setCharge(String charge) {
	this.charge = charge;
}
public String getRollToCharge() {
	return rollToCharge;
}
public void setRollToCharge(String rollToCharge) {
	this.rollToCharge = rollToCharge;
}
public String getDisplay() {
	return display;
}
public void setDisplay(String display) {
	this.display = display;
}
public String getExcludeFromTotal() {
	return excludeFromTotal;
}
public void setExcludeFromTotal(String excludeFromTotal) {
	this.excludeFromTotal = excludeFromTotal;
}
public static List getFclConsolidatorList() {
	return fclConsolidatorList;
}
public static void setFclConsolidatorList(List fclConsolidatorList) {
	FclConsolidator.fclConsolidatorList = fclConsolidatorList;
}
}
