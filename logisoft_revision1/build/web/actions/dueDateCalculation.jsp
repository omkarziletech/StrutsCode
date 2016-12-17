<%@ page language="java"
	import="java.util.*,java.text.*,org.json.JSONArray,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.bc.referenceDataManagement.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.bc.ratemanagement.UnLocationBC,com.gp.cong.logisoft.bc.ratemanagement.PortsBC,org.json.JSONObject,com.gp.cong.logisoft.domain.CarriersOrLineTemp,com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC,com.gp.cong.logisoft.bc.ratemanagement.CarriersOrLineBC,com.gp.cong.logisoft.domain.GenericCode"%>
<%@page import="com.gp.cvst.logisoft.domain.Batch"%>

<%
	String requestFor = request.getParameter("requestFor");
	String term = request.getParameter("term");
	String date = request.getParameter("date");
	JSONObject jsonObj = new JSONObject();
	if (requestFor == null) {
		return;
	}
	if (requestFor.equals("dueDate") && null != date && !date.trim().equals("")) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date convertedDate = dateFormat.parse(date);
		int daysToAdd = 0;
		if (term != null && term.equalsIgnoreCase("NET 15 DAYS")) {
			daysToAdd = 15;
		} else if (term != null && term.equalsIgnoreCase("NET 30 DAYS")) {

			daysToAdd = 30;
		} else if (term != null && term.equalsIgnoreCase("NET 45 DAYS")) {

			daysToAdd = 45;
		} else if (term != null && term.equalsIgnoreCase("NET 60 DAYS")) {

			daysToAdd = 60;
		} else if (term != null && term.equalsIgnoreCase("NET 7 DAYS")) {
           	daysToAdd = 7;
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(convertedDate);
		calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
		Date hello = calendar.getTime();

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String newDate = df.format(hello);
		jsonObj.put("newDate", newDate);

	}else {		
		throw new Exception("Invalid DueDate");
	}
	out.println(jsonObj.toString());
%>