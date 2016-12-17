package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class SegmentvalueBean implements Serializable {
	
	private Integer id;
	private String segmentvalue;
	private String segmentdesc;
	private String SegcodeId;
	public String getSegcodeId() {
		return SegcodeId;
	}
	public void setSegcodeId(String segcodeId) {
		SegcodeId = segcodeId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSegmentdesc() {
		return segmentdesc;
	}
	public void setSegmentdesc(String segmentdesc) {
		this.segmentdesc = segmentdesc;
	}
	public String getSegmentvalue() {
		return segmentvalue;
	}
	public void setSegmentvalue(String segmentvalue) {
		this.segmentvalue = segmentvalue;
	}
	
}
