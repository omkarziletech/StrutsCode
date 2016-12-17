package com.gp.cong.logisoft.domain;

import java.util.Date;

/**
 * SedFilingsId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SedFilingsId implements java.io.Serializable {

	// Fields

	private Date entrdt;
	private String entnam;

	// Constructors

	/** default constructor */
	public SedFilingsId() {
	}

	/** full constructor */
	public SedFilingsId(Date entrdt, String entnam) {
		this.entrdt = entrdt;
		this.entnam = entnam;
	}

	// Property accessors

	public Date getEntrdt() {
		return this.entrdt;
	}

	public void setEntrdt(Date entrdt) {
		this.entrdt = entrdt;
	}

	
	public String getEntnam() {
		return this.entnam;
	}

	public void setEntnam(String entnam) {
		this.entnam = entnam;
	}

}