/**
 * 
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class SearchWarehouseBean implements Serializable {
	private String warehouseCode;
	private String warehouseName;
	private String city;
	private String airCargo;
	private String match;
	private String buttonValue;
	private String type;
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getAirCargo() {
		return airCargo;
	}
	public void setAirCargo(String airCargo) {
		this.airCargo = airCargo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
        

}
