package com.gp.cong.logisoft.edi.util;

import java.sql.ResultSet;

public class SubmitterAttributes {
	private String addres1;
	private String state;
	private String zipcde;
	private String phnnum1;
	private String faxnum1;
	private String city;
	private String cuntry;
	private String city1;
	
	public SubmitterAttributes(ResultSet rs)throws Exception {
			for(java.lang.reflect.Field field : this.getClass().getDeclaredFields()){
				field.set(this, rs.getString(field.getName()));
			}
	}
	
	public String getAddres1() {
		return addres1;
	}

	public String getState() {
		return state;
	}

	public String getZipcde() {
		return zipcde;
	}

	public String getPhnnum1() {
		return phnnum1;
	}

	public String getFaxnum1() {
		return faxnum1;
	}

	public String getCity() {
		return city;
	}

	public String getCuntry() {
		return cuntry;
	}

	public String getCity1() {
		return city1;
	}

}
