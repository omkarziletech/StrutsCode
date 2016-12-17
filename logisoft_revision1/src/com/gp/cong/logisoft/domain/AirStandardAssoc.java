/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;


/**
 * @author Yogesh
 *
 */

public class AirStandardAssoc implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer airRatesId;
	private Integer airStdId;
	public Integer getAirRatesId() {
		return airRatesId;
	}
	public void setAirRatesId(Integer airRatesId) {
		this.airRatesId = airRatesId;
	}
	public Integer getAirStdId() {
		return airStdId;
	}
	public void setAirStdId(Integer airStdId) {
		this.airStdId = airStdId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
