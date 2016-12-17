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

public class CarrierAirline implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer airlineid;
	private String carriercode;
	private String airabbr;
	private String aircod;
	private String alnact;
	private String acomyn;
	private Integer acomcd;
	private Double acompc;
	private Set portcodes;
	private Set claimSet;
	
	public Set getClaimSet() {
		return claimSet;
	}
	public void setClaimSet(Set claimSet) {
		this.claimSet = claimSet;
	}
	public Integer getAcomcd() {
		return acomcd;
	}
	public void setAcomcd(Integer acomcd) {
		this.acomcd = acomcd;
	}
	public Double getAcompc() {
		return acompc;
	}
	public void setAcompc(Double acompc) {
		this.acompc = acompc;
	}
	public String getAcomyn() {
		return acomyn;
	}
	public void setAcomyn(String acomyn) {
		this.acomyn = acomyn;
	}
	public String getAirabbr() {
		return airabbr;
	}
	public void setAirabbr(String airabbr) {
		this.airabbr = airabbr;
	}
	public String getAircod() {
		return aircod;
	}
	public void setAircod(String aircod) {
		this.aircod = aircod;
	}
	public Integer getAirlineid() {
		return airlineid;
	}
	public void setAirlineid(Integer airlineid) {
		this.airlineid = airlineid;
	}
	public String getAlnact() {
		return alnact;
	}
	public void setAlnact(String alnact) {
		this.alnact = alnact;
	}
	public String getCarriercode() {
		return carriercode;
	}
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	public Set getPortcodes() {
		return portcodes;
	}
	public void setPortcodes(Set portcodes) {
		this.portcodes = portcodes;
	}
	

}
