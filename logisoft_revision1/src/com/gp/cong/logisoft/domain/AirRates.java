/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;


/**
 * @author Yogesh
 *
 */


public class AirRates implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer airRatesId;
	private RefTerminal originTerminal;
	private Ports destinationPort;
	private GenericCode commodityCode;
	private String match;
	private Set airWeightRangeSet;
	private Set airCommoditySet;

	public Set getAirCommoditySet() {
		return airCommoditySet;
	}
	public void setAirCommoditySet(Set airCommoditySet) {
		this.airCommoditySet = airCommoditySet;
	}
	public Set getAirWeightRangeSet() {
		return airWeightRangeSet;
	}
	public void setAirWeightRangeSet(Set airWeightRangeSet) {
		this.airWeightRangeSet = airWeightRangeSet;
	}
	public Integer getAirRatesId() {
		return airRatesId;
	}
	public void setAirRatesId(Integer airRatesId) {
		this.airRatesId = airRatesId;
	}
	public Ports getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(Ports destinationPort) {
		this.destinationPort = destinationPort;
	}
	public RefTerminal getOriginTerminal() {
		return originTerminal;
	}
	public void setOriginTerminal(RefTerminal originTerminal) {
		this.originTerminal = originTerminal;
	}
	public GenericCode getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(GenericCode commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
