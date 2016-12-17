
<jsp:directive.page import="java.util.Calendar"/>
<jsp:directive.page import="java.util.Date"/>
<%!
	String fromDate=null;
	String toDate=null;
 %>
<%
	Calendar calendar = Calendar.getInstance();
	//Date date = calendar.getTime();
	//calendar.set(date.getYear(),date.getMonth()-1,date.getDate()); not proper way of using caleneder....
	 fromDate=calendar.getTime().toString();
	 calendar.roll(calendar.MONTH, false);// way to substract month to add 1 month calendar.roll(calendar.MONTH, true);
	 toDate=calendar.getTime().toString();
//	calendar.add(Calendar.MONTH, -1);//this is also one way to substract 1 month, to add calendar.add(Calendar.MONTH, 1)
	
%>