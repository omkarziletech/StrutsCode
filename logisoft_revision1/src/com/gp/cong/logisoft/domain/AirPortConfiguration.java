/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;


/**
 * @author Yogesh
 *
 */

public class AirPortConfiguration implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer sheduleNumber;
	private RefTerminalTemp trmnum;
	private String lineManager;
	private GenericCode airPortId;
	private String airPortCityId;
	private String printOnAirFitSch;
	private String serviceAir;
	private String airSplRemarksEnglish;
	private String airSplRemarksSpanish;
	private String lclAirBlgoCollect;
	private GenericCode flightScheduleRegion;
	private String airportcityname;
	
	public String getAirportcityname() {
		return airportcityname;
	}
	public void setAirportcityname(String airportcityname) {
		this.airportcityname = airportcityname;
	}

    public String getAirPortCityId() {
        return airPortCityId;
    }

    public void setAirPortCityId(String airPortCityId) {
        this.airPortCityId = airPortCityId;
    }
	
	
	
	public GenericCode getAirPortId() {
		return airPortId;
	}
	public void setAirPortId(GenericCode airPortId) {
		this.airPortId = airPortId;
	}
	
	
	public Integer getSheduleNumber() {
		return sheduleNumber;
	}
	public void setSheduleNumber(Integer sheduleNumber) {
		this.sheduleNumber = sheduleNumber;
	}
	public String getAirSplRemarksEnglish() {
		return airSplRemarksEnglish;
	}
	public void setAirSplRemarksEnglish(String airSplRemarksEnglish) {
		this.airSplRemarksEnglish = airSplRemarksEnglish;
	}
	public String getAirSplRemarksSpanish() {
		return airSplRemarksSpanish;
	}
	public void setAirSplRemarksSpanish(String airSplRemarksSpanish) {
		this.airSplRemarksSpanish = airSplRemarksSpanish;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLineManager() {
		return lineManager;
	}
	public void setLineManager(String lineManager) {
		this.lineManager = lineManager;
	}
	
	
	
	public String getPrintOnAirFitSch() {
		return printOnAirFitSch;
	}
	public void setPrintOnAirFitSch(String printOnAirFitSch) {
		this.printOnAirFitSch = printOnAirFitSch;
	}
	public String getServiceAir() {
		return serviceAir;
	}
	public void setServiceAir(String serviceAir) {
		this.serviceAir = serviceAir;
	}
	
	public RefTerminalTemp getTrmnum() {
		return trmnum;
	}
	public void setTrmnum(RefTerminalTemp trmnum) {
		this.trmnum = trmnum;
	}
	public GenericCode getFlightScheduleRegion() {
		return flightScheduleRegion;
	}
	public void setFlightScheduleRegion(GenericCode flightScheduleRegion) {
		this.flightScheduleRegion = flightScheduleRegion;
	}
	public String getLclAirBlgoCollect() {
		return lclAirBlgoCollect;
	}
	public void setLclAirBlgoCollect(String lclAirBlgoCollect) {
		this.lclAirBlgoCollect = lclAirBlgoCollect;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

}
