<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.bc.admin.PrinterBC,org.json.JSONObject,com.gp.cong.logisoft.bc.ratemanagement.*,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.bc.voyagemanagement.*"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cvst.logisoft.domain.Batch"%>
<%
	String requestFor = request.getParameter("requestFor");
	String terminalNumber = request.getParameter("terminalNumber");
	String terminalName = request.getParameter("terminalName");
	String scheduleNumber = request.getParameter("scheduleNumber");
	String destAirportName = request.getParameter("destAirportName");
	String sslineNumber = request.getParameter("sslinenumber");
	String sslineName = request.getParameter("sslinename");

	JSONObject jsonObj = new JSONObject();

	TerminalBC terminalBC = new TerminalBC();

	CarriersOrLineBC carriersOrLineBC = new CarriersOrLineBC();
	PortsBC portsBC = new PortsBC();
	if (requestFor == null) {
		return;
	}

	if (requestFor.equals("OrgTerminalName")) {
		RefTerminalTemp refTerminalTemp = terminalBC.getTerminalName(
				terminalNumber, null);
		jsonObj.put("terminalName", refTerminalTemp
				.getTerminalLocation().toString());

	} else if (requestFor.equals("OrgTerminalNumber")) {
		RefTerminalTemp refTerminalTemp = terminalBC.getTerminalName(
				null, terminalName);
		jsonObj.put("terminalNumber", refTerminalTemp.getTrmnum());

	} else if (requestFor.equals("DestAirportname")) {
		PortsTemp portsTemp = portsBC.getPortName(scheduleNumber, null);
		jsonObj.put("destAirportname", portsTemp.getPortname()
				.toString());

	} else if (requestFor.equals("ScheduleNumber")) {
		PortsTemp portsTemp = portsBC
				.getPortName(null, destAirportName);
		jsonObj.put("destSheduleNumber", portsTemp.getShedulenumber());

	} else if (requestFor.equals("SslineName")) {
		CarriersOrLineTemp carriersOrLineTemp = carriersOrLineBC
				.getSslineNumber(sslineNumber);
		jsonObj.put("sslinename", carriersOrLineTemp.getCarriername()
				.toString());

	} else if (requestFor.equals("SslineNumber")) {
		CarriersOrLineTemp carriersOrLineTemp = carriersOrLineBC
				.getSslineName(null, sslineName);
		jsonObj
				.put("sslinenumber", carriersOrLineTemp
						.getCarriercode());

	}
	out.println(jsonObj.toString());
%>

