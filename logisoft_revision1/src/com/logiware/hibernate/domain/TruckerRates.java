package com.logiware.hibernate.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "trucker_rates")
public class TruckerRates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "trucker")
    private String trucker;
    @Column(name = "from_zip")
    private String fromZip;
    @Column(name = "from_city")
    private String fromCity;
    @Column(name = "from_state")
    private String fromState;
    @Column(name = "to_port")
    private Integer toPort;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "fuel")
    private Double fuel;
    @Column(name = "buy")
    private Double buy;
    @Column(name = "haz")
    private Double haz;
    @Column(name = "markup")
    private String markup;
    @Column(name = "sell")
    private Double sell;
    @Column(name = "trucker_name")
    private String truckerName;
    @Column(name = "trucker_number")
    private String truckerNumber;
    @Column(name = "from_zip_code")
    private String fromZipCode;
    @Column(name = "to_port_code")
    private String toPortCode;

    public TruckerRates() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTrucker() {
	return trucker;
    }

    public void setTrucker(String trucker) {
	this.trucker = trucker;
    }

    public String getFromZip() {
	return fromZip;
    }

    public void setFromZip(String fromZip) {
	this.fromZip = fromZip;
    }

    public String getFromCity() {
	return fromCity;
    }

    public void setFromCity(String fromCity) {
	this.fromCity = fromCity;
    }

    public String getFromState() {
	return fromState;
    }

    public void setFromState(String fromState) {
	this.fromState = fromState;
    }

    public Integer getToPort() {
	return toPort;
    }

    public void setToPort(Integer toPort) {
	this.toPort = toPort;
    }

    public Double getRate() {
	return rate;
    }

    public void setRate(Double rate) {
	this.rate = rate;
    }

    public Double getFuel() {
	return fuel;
    }

    public void setFuel(Double fuel) {
	this.fuel = fuel;
    }

    public Double getBuy() {
	return buy;
    }

    public void setBuy(Double buy) {
	this.buy = buy;
    }

    public Double getHaz() {
        return haz;
    }

    public void setHaz(Double haz) {
        this.haz = haz;
    }

    public String getMarkup() {
	return markup;
    }

    public void setMarkup(String markup) {
	this.markup = markup;
    }

    public Double getSell() {
	return sell;
    }

    public void setSell(Double sell) {
	this.sell = sell;
    }

    public String getTruckerName() {
	return truckerName;
    }

    public void setTruckerName(String truckerName) {
	this.truckerName = truckerName;
    }

    public String getTruckerNumber() {
	return truckerNumber;
    }

    public void setTruckerNumber(String truckerNumber) {
	this.truckerNumber = truckerNumber;
    }

    public String getFromZipCode() {
	return fromZipCode;
    }

    public void setFromZipCode(String fromZipCode) {
	this.fromZipCode = fromZipCode;
    }

    public String getToPortCode() {
	return toPortCode;
    }

    public void setToPortCode(String toPortCode) {
	this.toPortCode = toPortCode;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof TruckerRates)) {
	    return false;
	}
	TruckerRates other = (TruckerRates) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.hibernate.domain.TruckerRates[id=" + id + "]";
    }
    private transient String rateValue;
    private transient String fuelValue;
    private transient String buyValue;
    private transient String hazValue;
    private transient String sellValue;

    public String getRateValue() throws Exception {
	//rateValue = CommonUtils.isNotEmpty(rate) ? NumberUtils.formatAmount(rate) : "0.00";
	return rateValue;
    }

    public void setRateValue(String rateValue) throws Exception {
	//rate = NumberUtils.parseNumber(rateValue);
	this.rateValue = rateValue;
    }

    public String getFuelValue() throws Exception {
	//fuelValue = CommonUtils.isNotEmpty(fuel) ? NumberUtils.formatAmount(fuel) : "0.00";
	return fuelValue;
    }

    public void setFuelValue(String fuelValue) throws Exception {
	//fuel = NumberUtils.parseNumber(fuelValue);
	this.fuelValue = fuelValue;
    }

    public String getBuyValue() throws Exception {
	//buyValue = CommonUtils.isNotEmpty(buy) ? NumberUtils.formatAmount(buy) : "0.00";
	return buyValue;
    }

    public void setBuyValue(String buyValue) throws Exception {
	//buy = NumberUtils.parseNumber(buyValue);
	this.buyValue = buyValue;
    }
    public String getHazValue() throws Exception {
	//hazValue = CommonUtils.isNotEmpty(haz) ? NumberUtils.formatAmount(haz) : "0.00";
	return hazValue;
    }

    public void setHazValue(String hazValue) throws Exception {
	//haz = NumberUtils.parseNumber(hazValue);
	this.hazValue = hazValue;
    }

    public String getSellValue() throws Exception {
	//sellValue = CommonUtils.isNotEmpty(sell) ? NumberUtils.formatAmount(sell) : "0.00";
	return sellValue;
    }

    public void setSellValue(String sellValue) throws Exception {
	//sell = NumberUtils.parseNumber(sellValue);
	this.sellValue = sellValue;
    }
}
