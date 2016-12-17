package com.gp.cvst.logisoft.reports.dto;

import java.util.List;

public class BlMainDTO {

	private String portofdischarge;
	private String blno;
	private String exportreferences;
	private String shipperorexporter;
	private String consignee;
	private String notifyparty;
	private String arivalinfo;
	private String goodsdelivary;
	private String ponl;
	private String ponlno;
	private String cntr;
	private String sealno;
	private String noofpkgs;
	private String goodsweightkgs;
	private String goodsweightlbs;
	private String description;
	private String measurment;
	private String totalgoodsweightkgs;
	private String totalgoodsweightlbs;
	private String empty;
	private String grandtotalgoodsweightkgs;
	private String grandtotalgoodsweightlbs;
	private String grandTotal;
	private String totalWeight;
	private String pier;
	private String quantity;
	private String unitprice;
	private String totalamount;
	private String commodity;
	private String carrier;
	private String bookingno;
	private String loading;
	private String unloading;
	private String deliveryto;
	private String etd;
	private String eta;
	private String vessel;
	private String forwardAgent;
    private String notifypartyAddress;
	private String portofloading;
	private String shipperName;
	private String bolNo;
	private String consigneeName;
	private String forwardingAgentName;
	private String placeOfReciept;
	private String notifyParty;
	private String agentName;
	private String agentAddress;
	private String finalDestination;
	private String marksandnumbers;
	private String precarriageby;
	private String loadingpier;
	private String originals;
	private String frightCharges;
	private String prepaid; 
	private String printOnBl;
	private Double amount;
	private String BLPrinting;
	private String OPrinting;
	private String NPrinting;
	private String houseShipperName;
	private String houseShipperAddress;
	private String placeofTerminal;
	private String shipperSource;
	private String consigneeSource;
	private String notifyPartySource;
	private String masterBlComments;
	private String masterBlNotes;
	
	private List blMainList;
	private List blFieldList;
	private List blByChargesList;
	public String getMasterBlNotes() {
		return masterBlNotes;
	}
	public void setMasterBlNotes(String masterBlNotes) {
		this.masterBlNotes = masterBlNotes;
	}
	public String getMasterBlComments() {
		return masterBlComments;
	}
	public void setMasterBlComments(String masterBlComments) {
		this.masterBlComments = masterBlComments;
	}
	public String getConsigneeSource() {
		return consigneeSource;
	}
	public void setConsigneeSource(String consigneeSource) {
		this.consigneeSource = consigneeSource;
	}
	public String getNotifyPartySource() {
		return notifyPartySource;
	}
	public void setNotifyPartySource(String notifyPartySource) {
		this.notifyPartySource = notifyPartySource;
	}
	public String getShipperSource() {
		return shipperSource;
	}
	public void setShipperSource(String shipperSource) {
		this.shipperSource = shipperSource;
	}
	public String getHouseShipperName() {
		return houseShipperName;
	}
	public void setHouseShipperName(String houseShipperName) {
		this.houseShipperName = houseShipperName;
	}
	public String getHouseShipperAddress() {
		return houseShipperAddress;
	}
	public void setHouseShipperAddress(String houseShipperAddress) {
		this.houseShipperAddress = houseShipperAddress;
	}
	public String getBLPrinting() {
		return BLPrinting;
	}
	public void setBLPrinting(String printing) {
		BLPrinting = printing;
	}
	public String getNPrinting() {
		return NPrinting;
	}
	public void setNPrinting(String printing) {
		NPrinting = printing;
	}
	public String getOPrinting() {
		return OPrinting;
	}
	public void setOPrinting(String printing) {
		OPrinting = printing;
	}
	public String getBookingno() {
		return bookingno;
	}
	public void setBookingno(String bookingno) {
		this.bookingno = bookingno;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getCommodity() {
		return commodity;
	}
	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}
	public String getDeliveryto() {
		return deliveryto;
	}
	public void setDeliveryto(String deliveryto) {
		this.deliveryto = deliveryto;
	}
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}
	public String getEtd() {
		return etd;
	}
	public void setEtd(String etd) {
		this.etd = etd;
	}
	public String getLoading() {
		return loading;
	}
	public void setLoading(String loading) {
		this.loading = loading;
	}
	public String getUnloading() {
		return unloading;
	}
	public void setUnloading(String unloading) {
		this.unloading = unloading;
	}
	public String getVessel() {
		return vessel;
	}
	public void setVessel(String vessel) {
		this.vessel = vessel;
	}
	public String getEmpty() {
		return empty;
	}
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getExportreferences() {
		return exportreferences;
	}
	public void setExportreferences(String exportreferences) {
		this.exportreferences = exportreferences;
	}
	public String getNotifyparty() {
		return notifyparty;
	}
	public void setNotifyparty(String notifyparty) {
		this.notifyparty = notifyparty;
	}
	public String getShipperorexporter() {
		return shipperorexporter;
	}
	public void setShipperorexporter(String shipperorexporter) {
		this.shipperorexporter = shipperorexporter;
	}
	public String getBlno() {
		return blno;
	}
	public void setBlno(String blno) {
		this.blno = blno;
	}
	public String getPortofdischarge() {
		return portofdischarge;
	}
	public void setPortofdischarge(String portofdischarge) {
		this.portofdischarge = portofdischarge;
	}
	public String getArivalinfo() {
		return arivalinfo;
	}
	public void setArivalinfo(String arivalinfo) {
		this.arivalinfo = arivalinfo;
	}
	public String getGoodsdelivary() {
		return goodsdelivary;
	}
	public void setGoodsdelivary(String goodsdelivary) {
		this.goodsdelivary = goodsdelivary;
	}
	public String getPonl() {
		return ponl;
	}
	public void setPonl(String ponl) {
		this.ponl = ponl;
	}
	public String getPonlno() {
		return ponlno;
	}
	public void setPonlno(String ponlno) {
		this.ponlno = ponlno;
	}
	public String getCntr() {
		return cntr;
	}
	public void setCntr(String cntr) {
		this.cntr = cntr;
	}
	public String getSealno() {
		return sealno;
	}
	public void setSealno(String sealno) {
		this.sealno = sealno;
	}
	public String getGoodsweightkgs() {
		return goodsweightkgs;
	}
	public void setGoodsweightkgs(String goodsweightkgs) {
		this.goodsweightkgs = goodsweightkgs;
	}
	public String getGoodsweightlbs() {
		return goodsweightlbs;
	}
	public void setGoodsweightlbs(String goodsweightlbs) {
		this.goodsweightlbs = goodsweightlbs;
	}
	public String getNoofpkgs() {
		return noofpkgs;
	}
	public void setNoofpkgs(String noofpkgs) {
		this.noofpkgs = noofpkgs;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMeasurment() {
		return measurment;
	}
	public void setMeasurment(String measurment) {
		this.measurment = measurment;
	}
	public String getTotalgoodsweightkgs() {
		return totalgoodsweightkgs;
	}
	public void setTotalgoodsweightkgs(String totalgoodsweightkgs) {
		this.totalgoodsweightkgs = totalgoodsweightkgs;
	}
	public String getTotalgoodsweightlbs() {
		return totalgoodsweightlbs;
	}
	public void setTotalgoodsweightlbs(String totalgoodsweightlbs) {
		this.totalgoodsweightlbs = totalgoodsweightlbs;
	}
	public String getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}
	public String getGrandtotalgoodsweightkgs() {
		return grandtotalgoodsweightkgs;
	}
	public void setGrandtotalgoodsweightkgs(String grandtotalgoodsweightkgs) {
		this.grandtotalgoodsweightkgs = grandtotalgoodsweightkgs;
	}
	public String getGrandtotalgoodsweightlbs() {
		return grandtotalgoodsweightlbs;
	}
	public void setGrandtotalgoodsweightlbs(String grandtotalgoodsweightlbs) {
		this.grandtotalgoodsweightlbs = grandtotalgoodsweightlbs;
	}
	public String getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
	public String getPier() {
		return pier;
	}
	public void setPier(String pier) {
		this.pier = pier;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}
	public String getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}
	public String getForwardAgent() {
		return forwardAgent;
	}
	public void setForwardAgent(String forwardAgent) {
		this.forwardAgent = forwardAgent;
	}
	public String getBolNo() {
		return bolNo;
	}
	public void setBolNo(String bolNo) {
		this.bolNo = bolNo;
	}
	public String getNotifypartyAddress() {
		return notifypartyAddress;
	}
	public void setNotifypartyAddress(String notifypartyAddress) {
		this.notifypartyAddress = notifypartyAddress;
	}
	public String getPortofloading() {
		return portofloading;
	}
	public void setPortofloading(String portofloading) {
		this.portofloading = portofloading;
	}
	public String getShipperName() {
		return shipperName;
	}
	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}
	public String getAgentAddress() {
		return agentAddress;
	}
	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getFinalDestination() {
		return finalDestination;
	}
	public void setFinalDestination(String finalDestination) {
		this.finalDestination = finalDestination;
	}
	public String getForwardingAgentName() {
		return forwardingAgentName;
	}
	public void setForwardingAgentName(String forwardingAgentName) {
		this.forwardingAgentName = forwardingAgentName;
	}
	public String getMarksandnumbers() {
		return marksandnumbers;
	}
	public void setMarksandnumbers(String marksandnumbers) {
		this.marksandnumbers = marksandnumbers;
	}
	public String getNotifyParty() {
		return notifyParty;
	}
	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
	}
	public String getPlaceOfReciept() {
		return placeOfReciept;
	}
	public void setPlaceOfReciept(String placeOfReciept) {
		this.placeOfReciept = placeOfReciept;
	}
	public String getLoadingpier() {
		return loadingpier;
	}
	public void setLoadingpier(String loadingpier) {
		this.loadingpier = loadingpier;
	}
	public String getOriginals() {
		return originals;
	}
	public void setOriginals(String originals) {
		this.originals = originals;
	}
	public String getPrecarriageby() {
		return precarriageby;
	}
	public void setPrecarriageby(String precarriageby) {
		this.precarriageby = precarriageby;
	}
	public String getFrightCharges() {
		return frightCharges;
	}
	public void setFrightCharges(String frightCharges) {
		this.frightCharges = frightCharges;
	}
	public String getPrepaid() {
		return prepaid;
	}
	public void setPrepaid(String prepaid) {
		this.prepaid = prepaid;
	}
	 
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPrintOnBl() {
		return printOnBl;
	}
	public void setPrintOnBl(String printOnBl) {
		this.printOnBl = printOnBl;
	}
	public String getPlaceofTerminal() {
		return placeofTerminal;
	}
	public void setPlaceofTerminal(String placeofTerminal) {
		this.placeofTerminal = placeofTerminal;
	}
	public List getBlMainList() {
		return blMainList;
	}
	public void setBlMainList(List blMainList) {
		this.blMainList = blMainList;
	}
	public List getBlFieldList() {
		return blFieldList;
	}
	public void setBlFieldList(List blFieldList) {
		this.blFieldList = blFieldList;
	}
	public List getBlByChargesList() {
		return blByChargesList;
	}
	public void setBlByChargesList(List blByChargesList) {
		this.blByChargesList = blByChargesList;
	}
}