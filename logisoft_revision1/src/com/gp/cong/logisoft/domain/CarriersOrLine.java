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

public class CarriersOrLine implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private String carriercode;
	private String carriername;
	private GenericCode carriertype;
	private String SCAC;
	private Set CarrierAirlineSet; 
	private Set PortsSet;
	private Set CarrierOceanEqptSet;
	private String abbreviation;
	private String fclContactNumber;
	private String match;
	private String ediCarrier;
	
	
	public String getEdiCarrier() {
		return ediCarrier;
	}
	public void setEdiCarrier(String ediCarrier) {
		this.ediCarrier = ediCarrier;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getFclContactNumber() {
		return fclContactNumber;
	}
	public void setFclContactNumber(String fclContactNumber) {
		this.fclContactNumber = fclContactNumber;
	}
	public Set getPortsSet() {
		return PortsSet;
	}
	public void setPortsSet(Set portsSet) {
		PortsSet = portsSet;
	}
	public Set getCarrierAirlineSet() {
		return CarrierAirlineSet;
	}
	public void setCarrierAirlineSet(Set carrierAirlineSet) {
		CarrierAirlineSet = carrierAirlineSet;
	}
	public String getCarriercode() {
		return carriercode;
	}
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	public String getCarriername() {
		return carriername;
	}
	public void setCarriername(String carriername) {
		this.carriername = carriername;
	}
	public GenericCode getCarriertype() {
		return carriertype;
	}
	public void setCarriertype(GenericCode carriertype) {
		this.carriertype = carriertype;
	}
	public String getSCAC() {
		return SCAC;
	}
	public void setSCAC(String scac) {
		SCAC = scac;
	}
	public Set getCarrierOceanEqptSet() {
		return CarrierOceanEqptSet;
	}
	public void setCarrierOceanEqptSet(Set carrierOceanEqptSet) {
		CarrierOceanEqptSet = carrierOceanEqptSet;
	}
	
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getId() {
		// TODO Auto-generated method stub
		return this.getCarriercode();
	}
	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}
	/**
	 * @param match the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}
}
