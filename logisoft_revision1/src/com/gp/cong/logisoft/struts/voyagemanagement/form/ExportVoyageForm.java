/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.voyagemanagement.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 08-12-2008
 * 
 * XDoclet definition:
 * @struts.form name="exportVoyageForm"
 */
public class ExportVoyageForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	String eciVoyage="";
	String loadingTerminal="";
	String loadingTerminalName="";
	String port="";
	String portName="";
	String vessel="";
	String vesselName="";
	String index="";
	private String deliveryCutoffDate;
	String time="";
	private String sailDate;
	String lineNo="";
	String lineName="";
	String pierNo="";
	String flightSsVoyage="";
	String unitType="";
	String sslBookingNo="";
	String showOnSailingSchedule="";
	String arrivalOrDeparture="";
	String currentSequenceNo="";
	String podNo="";
	String podName="";
	String transShipments="";
	String transitDaysOverride="";
	String agentForVoyage="";
	String truckingInfo="";
	String buttonValue="";
	String id="";
	private Integer voyageStdId;
	private String txtItemcreatedon;
	private String txtItemcreated;
	 private String singleSelect;
	 
	public String getSingleSelect() {
		return singleSelect;
	}
	public void setSingleSelect(String singleSelect) {
		this.singleSelect = singleSelect;
	}
	public String getTxtItemcreatedon() {
		return txtItemcreatedon;
	}
	public void setTxtItemcreatedon(String txtItemcreatedon) {
		this.txtItemcreatedon = txtItemcreatedon;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	

	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}
	public String getEciVoyage() {
		return eciVoyage;
	}
	public void setEciVoyage(String eciVoyage) {
		this.eciVoyage = eciVoyage;
	}
	public String getAgentForVoyage() {
		return agentForVoyage;
	}
	public void setAgentForVoyage(String agentForVoyage) {
		this.agentForVoyage = agentForVoyage;
	}
	public String getCurrentSequenceNo() {
		return currentSequenceNo;
	}
	public void setCurrentSequenceNo(String currentSequenceNo) {
		this.currentSequenceNo = currentSequenceNo;
	}
	public String getDeliveryCutoffDate() {
		return deliveryCutoffDate;
	}
	public void setDeliveryCutoffDate(String deliveryCutoffDate) {
		this.deliveryCutoffDate = deliveryCutoffDate;
	}
	public String getFlightSsVoyage() {
		return flightSsVoyage;
	}
	public void setFlightSsVoyage(String flightSsVoyage) {
		this.flightSsVoyage = flightSsVoyage;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getLoadingTerminal() {
		return loadingTerminal;
	}
	public void setLoadingTerminal(String loadingTerminal) {
		this.loadingTerminal = loadingTerminal;
	}
	public String getPierNo() {
		return pierNo;
	}
	public void setPierNo(String pierNo) {
		this.pierNo = pierNo;
	}
	public String getPodName() {
		return podName;
	}
	public void setPodName(String podName) {
		this.podName = podName;
	}
	public String getPodNo() {
		return podNo;
	}
	public void setPodNo(String podNo) {
		this.podNo = podNo;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getSailDate() {
		return sailDate;
	}
	public void setSailDate(String sailDate) {
		this.sailDate = sailDate;
	}
	public String getShowOnSailingSchedule() {
		return showOnSailingSchedule;
	}
	public void setShowOnSailingSchedule(String showOnSailingSchedule) {
		this.showOnSailingSchedule = showOnSailingSchedule;
	}
	
	public String getSslBookingNo() {
		return sslBookingNo;
	}
	public void setSslBookingNo(String sslBookingNo) {
		this.sslBookingNo = sslBookingNo;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTransitDaysOverride() {
		return transitDaysOverride;
	}
	public void setTransitDaysOverride(String transitDaysOverride) {
		this.transitDaysOverride = transitDaysOverride;
	}
	public String getTransShipments() {
		return transShipments;
	}
	public void setTransShipments(String transShipments) {
		this.transShipments = transShipments;
	}
	public String getTruckingInfo() {
		return truckingInfo;
	}
	public void setTruckingInfo(String truckingInfo) {
		this.truckingInfo = truckingInfo;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getVessel() {
		return vessel;
	}
	public void setVessel(String vessel) {
		this.vessel = vessel;
	}
	public String getVesselName() {
		return vesselName;
	}
	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	public String getTxtItemcreated() {
		return txtItemcreated;
	}
	public void setTxtItemcreated(String txtItemcreated) {
		this.txtItemcreated = txtItemcreated;
	}
	public Integer getVoyageStdId() {
		return voyageStdId;
	}
	public void setVoyageStdId(Integer voyageStdId) {
		this.voyageStdId = voyageStdId;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getLoadingTerminalName() {
		return loadingTerminalName;
	}
	public void setLoadingTerminalName(String loadingTerminalName) {
		this.loadingTerminalName = loadingTerminalName;
	}
	public String getArrivalOrDeparture() {
		return arrivalOrDeparture;
	}
	public void setArrivalOrDeparture(String arrivalOrDeparture) {
		this.arrivalOrDeparture = arrivalOrDeparture;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}