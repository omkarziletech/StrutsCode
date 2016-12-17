package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class SegmentBean implements Serializable {
	
	private String segcode;
	private String segdesc;
	private String segleng;
	public String getSegcode() {
		return segcode;
	}
	public void setSegcode(String segcode) {
		this.segcode = segcode;
	}
	public String getSegdesc() {
		return segdesc;
	}
	public void setSegdesc(String segdesc) {
		this.segdesc = segdesc;
	}
	public String getSegleng() {
		return segleng;
	}
	public void setSegleng(String segleng) {
		this.segleng = segleng;
	}

}
