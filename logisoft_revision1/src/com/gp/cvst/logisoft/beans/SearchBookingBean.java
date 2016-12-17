package com.gp.cvst.logisoft.beans;

import java.util.Date;

import java.io.Serializable;

public class SearchBookingBean implements Serializable {
    private String bookingNumber;
    private Date bookingDate;
    private String shipNo;
    private String shipper;
    private String forward;
    private String consignee;
    private String addressforShipper;
    private String addressforForwarder;
    private String addressforConsingee;
    private Date etd;
    private Date eta;
    private String attenName;
    private String destination;
    private String portofOrgin;
    private Date cutofDate;
    private String carrierName;
    private String vessel;
    private String voyageCarrier;
    private String originTerminal;
    private String preparedtoCollect;
    private String exportPositoningPickup;
    private String exportDevliery;
    private Date dateInYard;
    private Date dateoutYard;
    private String billtoCode;
    private String bookingComplete;
    private String goodsDescription;
    private String remarks;
    private Date positioningDate;
    private String voyageInternal;
    private String addressForExpPositioning;
    private String addessForExpDelivery;
    private String salesRepCode;
    private String truckerCode;
    private String name;
    private String address;
    private String prepaidCollect;
    private String portofDischarge;
    private String username;
    private String unitType;
    private String numbers;
    private String rates;
    private String ratesDesc;
    
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	 
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	public String getOriginTerminal() {
		return originTerminal;
	}
	public void setOriginTerminal(String originTerminal) {
		this.originTerminal = originTerminal;
	}
	public String getPortofDischarge() {
		return portofDischarge;
	}
	public void setPortofDischarge(String portofDischarge) {
		this.portofDischarge = portofDischarge;
	}
	public String getRates() {
		return rates;
	}
	public void setRates(String rates) {
		this.rates = rates;
	}
	public String getRatesDesc() {
		return ratesDesc;
	}
	public void setRatesDesc(String ratesDesc) {
		this.ratesDesc = ratesDesc;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getPortofOrgin() {
		return portofOrgin;
	}
	public String getAddessForExpDelivery() {
		return addessForExpDelivery;
	}
	public void setAddessForExpDelivery(String addessForExpDelivery) {
		this.addessForExpDelivery = addessForExpDelivery;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressforConsingee() {
		return addressforConsingee;
	}
	public void setAddressforConsingee(String addressforConsingee) {
		this.addressforConsingee = addressforConsingee;
	}
	public String getAddressForExpPositioning() {
		return addressForExpPositioning;
	}
	public void setAddressForExpPositioning(String addressForExpPositioning) {
		this.addressForExpPositioning = addressForExpPositioning;
	}
	public String getAddressforForwarder() {
		return addressforForwarder;
	}
	public void setAddressforForwarder(String addressforForwarder) {
		this.addressforForwarder = addressforForwarder;
	}
	public String getAddressforShipper() {
		return addressforShipper;
	}
	public void setAddressforShipper(String addressforShipper) {
		this.addressforShipper = addressforShipper;
	}
	public String getAttenName() {
		return attenName;
	}
	public void setAttenName(String attenName) {
		this.attenName = attenName;
	}
	public String getBilltoCode() {
		return billtoCode;
	}
	public void setBilltoCode(String billtoCode) {
		this.billtoCode = billtoCode;
	}
	public String getBookingComplete() {
		return bookingComplete;
	}
	public void setBookingComplete(String bookingComplete) {
		this.bookingComplete = bookingComplete;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getBookingNumber() {
		return bookingNumber;
	}
	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}
	 
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public Date getCutofDate() {
		return cutofDate;
	}
	public void setCutofDate(Date cutofDate) {
		this.cutofDate = cutofDate;
	}
	public Date getDateInYard() {
		return dateInYard;
	}
	public void setDateInYard(Date dateInYard) {
		this.dateInYard = dateInYard;
	}
	public Date getDateoutYard() {
		return dateoutYard;
	}
	public void setDateoutYard(Date dateoutYard) {
		this.dateoutYard = dateoutYard;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Date getEta() {
		return eta;
	}
	public void setEta(Date eta) {
		this.eta = eta;
	}
	public Date getEtd() {
		return etd;
	}
	public void setEtd(Date etd) {
		this.etd = etd;
	}
	public String getExportDevliery() {
		return exportDevliery;
	}
	public void setExportDevliery(String exportDevliery) {
		this.exportDevliery = exportDevliery;
	}
	public String getExportPositoningPickup() {
		return exportPositoningPickup;
	}
	public void setExportPositoningPickup(String exportPositoningPickup) {
		this.exportPositoningPickup = exportPositoningPickup;
	}
	public String getForward() {
		return forward;
	}
	public void setForward(String forward) {
		this.forward = forward;
	}
	public String getGoodsDescription() {
		return goodsDescription;
	}
	public void setGoodsDescription(String goodsDescription) {
		this.goodsDescription = goodsDescription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
	public void setPortofOrgin(String portofOrgin) {
		this.portofOrgin = portofOrgin;
	}
	public Date getPositioningDate() {
		return positioningDate;
	}
	public void setPositioningDate(Date positioningDate) {
		this.positioningDate = positioningDate;
	}
	public String getPrepaidCollect() {
		return prepaidCollect;
	}
	public void setPrepaidCollect(String prepaidCollect) {
		this.prepaidCollect = prepaidCollect;
	}
	public String getPreparedtoCollect() {
		return preparedtoCollect;
	}
	public void setPreparedtoCollect(String preparedtoCollect) {
		this.preparedtoCollect = preparedtoCollect;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSalesRepCode() {
		return salesRepCode;
	}
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}
	public String getShipNo() {
		return shipNo;
	}
	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getTruckerCode() {
		return truckerCode;
	}
	public void setTruckerCode(String truckerCode) {
		this.truckerCode = truckerCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getVessel() {
		return vessel;
	}
	public void setVessel(String vessel) {
		this.vessel = vessel;
	}
	public String getVoyageCarrier() {
		return voyageCarrier;
	}
	public void setVoyageCarrier(String voyageCarrier) {
		this.voyageCarrier = voyageCarrier;
	}
	public String getVoyageInternal() {
		return voyageInternal;
	}
	public void setVoyageInternal(String voyageInternal) {
		this.voyageInternal = voyageInternal;
	}
}
