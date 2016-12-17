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
 * Creation date: 10-23-2008
 * 
 * XDoclet definition:
 * @struts.form name="exportVoyagesForm"
 */
public class ExportVoyagesForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String eciVoyage;
	private String loadingTerminal;
	private String port;
	private String portName;
	private String vessel;
	private String vesselName;
	private String deliveryCutoffDate;
	private String time;
	private String sailDate;
	private String lineNo;
	private String lineName;
	private String pierNo;
	private String flightSsVoyage;
	private String singleSelect;
	private String sslBookingNo;
	private String showOnSailingSchedule;
	
	private String arrivalOrDeparture;
	
	private String currentSequenceNo;
	private String podNo;
	private String podName;
	private String transShipments;
	private String transitDaysOverride;
	private String agentForVoyage;
	private String truckingInfo;
	private String buttonValue;
	
	
	
	//new values
	private String sailDate1;
	private String vessel1;
	private String vesselName1;
	private String pierNo1;
	private String flightSsVoyage1;
	private String lineNo1;
	private String reasonCode;
	private String reasonDescription;
	private String singleSelect1;
	private String singleSelect2;
	private String lineName1;
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

	public String getAgentForVoyage() {
		return agentForVoyage;
	}

	public void setAgentForVoyage(String agentForVoyage) {
		this.agentForVoyage = agentForVoyage;
	}

	public String getArrivalOrDeparture() {
		return arrivalOrDeparture;
	}

	public void setArrivalOrDeparture(String arrivalOrDeparture) {
		this.arrivalOrDeparture = arrivalOrDeparture;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
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

	public String getEciVoyage() {
		return eciVoyage;
	}

	public void setEciVoyage(String eciVoyage) {
		this.eciVoyage = eciVoyage;
	}

	public String getFlightSsVoyage() {
		return flightSsVoyage;
	}

	public void setFlightSsVoyage(String flightSsVoyage) {
		this.flightSsVoyage = flightSsVoyage;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
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

	public String getSingleSelect() {
		return singleSelect;
	}

	public void setSingleSelect(String singleSelect) {
		this.singleSelect = singleSelect;
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

	public String getSailDate1() {
		return sailDate1;
	}

	public void setSailDate1(String sailDate1) {
		this.sailDate1 = sailDate1;
	}

	public String getFlightSsVoyage1() {
		return flightSsVoyage1;
	}

	public void setFlightSsVoyage1(String flightSsVoyage1) {
		this.flightSsVoyage1 = flightSsVoyage1;
	}

	public String getLineNo1() {
		return lineNo1;
	}

	public void setLineNo1(String lineNo1) {
		this.lineNo1 = lineNo1;
	}

	public String getPierNo1() {
		return pierNo1;
	}

	public void setPierNo1(String pierNo1) {
		this.pierNo1 = pierNo1;
	}

	public String getSingleSelect1() {
		return singleSelect1;
	}

	public void setSingleSelect1(String singleSelect1) {
		this.singleSelect1 = singleSelect1;
	}

	public String getSingleSelect2() {
		return singleSelect2;
	}

	public void setSingleSelect2(String singleSelect2) {
		this.singleSelect2 = singleSelect2;
	}

	public String getVessel1() {
		return vessel1;
	}

	public void setVessel1(String vessel1) {
		this.vessel1 = vessel1;
	}

	public String getVesselName1() {
		return vesselName1;
	}

	public void setVesselName1(String vesselName1) {
		this.vesselName1 = vesselName1;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public String getLineName1() {
		return lineName1;
	}

	public void setLineName1(String lineName1) {
		this.lineName1 = lineName1;
	}
}