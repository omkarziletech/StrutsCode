<%@ page language="java"
	import="java.util.*,java.text.*,org.json.JSONArray,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.bc.referenceDataManagement.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.bc.ratemanagement.UnLocationBC,com.gp.cong.logisoft.bc.ratemanagement.PortsBC,org.json.JSONObject,com.gp.cong.logisoft.domain.CarriersOrLineTemp,com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC,com.gp.cong.logisoft.bc.ratemanagement.CarriersOrLineBC,com.gp.cong.logisoft.domain.GenericCode"%>
<%@page import="com.gp.cvst.logisoft.domain.Batch"%>

<%
	String requestFor = request.getParameter("requestFor");
	String ETD = request.getParameter("ETD");
	String ETA = request.getParameter("ETA");
	JSONObject jsonObj = new JSONObject();
	if (requestFor == null) {
		return;
	}
	if (requestFor.equals("dateValidation")) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date deliveryDate = null;
		Date arrivalDate = null;
		if (ETD != null && !ETD.equals("")) {
			deliveryDate = dateFormat.parse(ETD);
		}
		if (ETA != null && !ETA.equals("")) {
			arrivalDate = dateFormat.parse(ETA);
		}

		if (deliveryDate != null && !deliveryDate.equals("")
				&& arrivalDate != null && !arrivalDate.equals("")) {

			int results = arrivalDate.compareTo(deliveryDate);

			if (results > 0) {
				jsonObj.put("result", "greater");
			} else if (results < 0) {
				jsonObj.put("result", "lesser");
			} else {
				jsonObj.put("result", "equal");
			}
		}
	}
	if (requestFor.equals("dateValidationReverse")) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date deliveryDate = null;
		Date arrivalDate = null;

		if (ETD != null && !ETD.equals("")) {
			deliveryDate = dateFormat.parse(ETD);
		}
		if (ETA != null && !ETA.equals("")) {
			arrivalDate = dateFormat.parse(ETA);
		}
		if (deliveryDate != null && !deliveryDate.equals("")
				&& arrivalDate != null && !arrivalDate.equals("")) {
			int results = deliveryDate.compareTo(arrivalDate);

			if (results > 0) {
				jsonObj.put("result", "greater");
			} else if (results < 0) {
				jsonObj.put("result", "lesser");
			} else {
				jsonObj.put("result", "equal");
			}
		}
	}
	out.println(jsonObj.toString());
%>