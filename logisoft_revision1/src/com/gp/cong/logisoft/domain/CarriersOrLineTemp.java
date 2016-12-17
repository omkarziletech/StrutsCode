/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Yogesh
 *
 */

public class CarriersOrLineTemp implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private String carriercode;
	private String carriername;
	private GenericCode carriertype;
	private String SCAC;
	private String fclContactNumber;
	public String getFclContactNumber() {
		return fclContactNumber;
	}
	public void setFclContactNumber(String fclContactNumber) {
		this.fclContactNumber = fclContactNumber;
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
	
	
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getId() {
		// TODO Auto-generated method stub
		return this.getCarriercode();
	}
	
	
}
