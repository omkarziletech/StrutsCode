package com.gp.cong.logisoft.edi.util;

import java.sql.ResultSet;

public class MarksNoAttributes {

	private String no_packages;//commodity quantity
	private String measure_cbm;
	private String netweight_kgs;
	private String measure_cft;
	private String netweight_lbs;
	private String uom;//packagingtype
	private String desc_packages;
	private String marks_no;
	private String copy_description_check;
	private String desc_for_master_bl;
	private String trailer_no_id;
	
	public MarksNoAttributes(ResultSet rs) throws Exception{
		for(java.lang.reflect.Field field : this.getClass().getDeclaredFields()){
			field.set(this, rs.getString(field.getName()));
		}
	}
	
	public String writeColumn(){
		
		return null;
	}

	public String getNo_packages() {
		return no_packages;
	}

	public String getMeasure_cbm() {
		return measure_cbm;
	}

	public String getNetweight_kgs() {
		return netweight_kgs;
	}

	public String getMeasure_cft() {
		return measure_cft;
	}

	public String getNetweight_lbs() {
		return netweight_lbs;
	}

	public String getUom() {
		return uom;
	}

	public String getDesc_packages() {
		return desc_packages;
	}

	public String getMarks_no() {
		return marks_no;
	}

	public String getCopy_description_check() {
		return copy_description_check;
	}

	public String getDesc_for_master_bl() {
		return desc_for_master_bl;
	}

	public String getTrailer_no_id() {
		return trailer_no_id;
	}
	
	

}
