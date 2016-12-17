package com.gp.cong.logisoft.edi.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class ContainerAttributes {
	private String trailer_no;
	private String seal_no;
	private String trailer_no_id;
	private String size_legend;
	private String marks_no;
	
	public ContainerAttributes(ResultSet rs)throws Exception {
			for (Field field : this.getClass().getDeclaredFields()) {
				field.set(this, rs.getString(field.getName()));
			}
	}
	public String getTrailer_no() {
		return trailer_no;
	}
	public String getSeal_no() {
		return seal_no;
	}
	public String getTrailer_no_id() {
		return trailer_no_id;
	}
	public String getSize_legend() {
		return size_legend;
	}
	public String getMarks_no() {
		return marks_no;
	}
	

}
