package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class FclBlBean implements Serializable {

	private String bol;
	private String boldate;
	private String booking;
	private String quote;
	private String consignee;
	private String shipper;
	private String forwardingagent;
	private String voyage;
	private String portofdischarge;
	private String portofloading;
	private String BLPrinting;
	private String OPrinting;
	private String NPrinting;
	private String preCarriage;
	private String placeOfReceipt;
	private String loadingPier;
	private String noOfOriginals;
	
	
	
	public String getPreCarriage() {
		return preCarriage;
	}
	public void setPreCarriage(String preCarriage) {
		this.preCarriage = preCarriage;
	}
	public String getPlaceOfReceipt() {
		return placeOfReceipt;
	}
	public void setPlaceOfReceipt(String placeOfReceipt) {
		this.placeOfReceipt = placeOfReceipt;
	}
	public String getLoadingPier() {
		return loadingPier;
	}
	public void setLoadingPier(String loadingPier) {
		this.loadingPier = loadingPier;
	}
	public String getNoOfOriginals() {
		return noOfOriginals;
	}
	public void setNoOfOriginals(String noOfOriginals) {
		this.noOfOriginals = noOfOriginals;
	}
	public String getBLPrinting() {
		return BLPrinting;
	}
	public void setBLPrinting(String printing) {
		BLPrinting = printing;
	}
	public String getOPrinting() {
		return OPrinting;
	}
	public void setOPrinting(String printing) {
		OPrinting = printing;
	}
	public String getNPrinting() {
		return NPrinting;
	}
	public void setNPrinting(String printing) {
		NPrinting = printing;
	}
	public String getBol() {
		return bol;
	}
	public void setBol(String bol) {
		this.bol = bol;
	}
	public String getBoldate() {
		return boldate;
	}
	public void setBoldate(String boldate) {
		this.boldate = boldate;
	}
	public String getBooking() {
		return booking;
	}
	public void setBooking(String booking) {
		this.booking = booking;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getForwardingagent() {
		return forwardingagent;
	}
	public void setForwardingagent(String forwardingagent) {
		this.forwardingagent = forwardingagent;
	}
	public String getPortofdischarge() {
		return portofdischarge;
	}
	public void setPortofdischarge(String portofdischarge) {
		this.portofdischarge = portofdischarge;
	}
	public String getPortofloading() {
		return portofloading;
	}
	public void setPortofloading(String portofloading) {
		this.portofloading = portofloading;
	}
	public String getQuote() {
		return quote;
	}
	public void setQuote(String quote) {
		this.quote = quote;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getVoyage() {
		return voyage;
	}
	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}
	
	
	
}
