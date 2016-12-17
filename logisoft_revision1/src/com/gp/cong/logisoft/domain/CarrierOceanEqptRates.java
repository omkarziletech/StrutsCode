/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Yogesh
 *
 */

public class CarrierOceanEqptRates implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private GenericCode eqpttype;
	private Double specialrate;
	private String carriercode;
	public String getCarriercode() {
		return carriercode;
	}
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	public GenericCode getEqpttype() {
		return eqpttype;
	}
	public void setEqpttype(GenericCode eqpttype) {
		this.eqpttype = eqpttype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getSpecialrate() {
		return specialrate;
	}
	public void setSpecialrate(Double specialrate) {
		this.specialrate = specialrate;
	}

}
