package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * Country object represents any country in the world
 * @author rahul
 * @version 1.0
 */

public class Country implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String countryname;
	private Integer countrycode;
	
	public void setCountryname(String countryname){
		this.countryname=countryname;
	}
	
	public String getCountryname(){
		return countryname;
	}
	
	public void setCountrycode(Integer countrycode){
		this.countrycode=countrycode;
	}
	
	public Integer getCountrycode(){
		return countrycode;
	}
	
}
