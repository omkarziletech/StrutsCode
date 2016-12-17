/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;


/**
 * @author Yogesh
 *
 */

public class AirFreightDocumentRates implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer originTerminal;
	private Integer destinationPort;
	private Integer standardId;
	
	public Integer getStandardId() {
		return standardId;
	}
	public void setStandardId(Integer standardId) {
		this.standardId = standardId;
	}
	public Integer getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(Integer destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOriginTerminal() {
		return originTerminal;
	}
	public void setOriginTerminal(Integer originTerminal) {
		this.originTerminal = originTerminal;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
