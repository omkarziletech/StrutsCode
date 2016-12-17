<%@ page language="java"
	import="java.util.*,com.gp.cvst.logisoft.domain.*,org.json.JSONObject,com.gp.cong.logisoft.bc.fcl.*"%>
<%
	String requestFor = request.getParameter("requestFor");
	String bookingId = request.getParameter("bookingId");
	JSONObject jsonObj = new JSONObject();
	if (requestFor.equals("QuoteNumber")) {
		BookingFclBC bookingFclBC = new BookingFclBC();
		List BookingList = bookingFclBC.getQuoteNo(bookingId);
		BookingFcl bookingFcl = (BookingFcl) BookingList.get(0);
		jsonObj.put("QuoteNo", bookingFcl.getQuoteNo());
	}
	out.println(jsonObj.toString());
%>