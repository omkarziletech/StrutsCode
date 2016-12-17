<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
// agss,csss,afrh,aarh are abreviations of the tabs found in Air Rates
String airDetailsPath=path+"/jsps/ratemanagement/airDetails.jsp";
String agssPath=path+"/jsps/ratemanagement/aGSS.jsp";
String csssPath=path+"/jsps/ratemanagement/cSSS.jsp";
String afrhPath=path+"/jsps/ratemanagement/aFRH.jsp";
String aarhPath=path+"/jsps/ratemanagement/aARH.jsp";
String editagssPath=path+"/jsps/ratemanagement/aGSSEdit.jsp";
String airHistory=path+"/jsps/ratemanagement/AirStandardHistory.jsp";
String documentCharges=path+"/jsps/ratemanagement/documentCharges.jsp";
String flightShedules=path+"/jsps/ratemanagement/flightShedule.jsp";
 boolean enable=false;;
 String getTab="";
	if(session.getAttribute("setTabEnable")!=null)
	{
		
		if(session.getAttribute("setTabEnable").equals("enable"))
		{
			enable = true;
		}
		else
		{
			enable = false;
		}
	}
		String result="";
	if (session.getAttribute("editrecord") != null) {
		result = (String) session.getAttribute("editrecord");
		
		
		}	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Port Details tabs</title>
<script type="text/javascript">
	var djConfig = { isDebug: true };
</script>
<%@include file="includes/baseResources.jsp" %>

<script type="text/javascript" src="<%=request.getContextPath()%>/dojo/dojo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/lfx/html.js"></script>
<script type="text/javascript">
	dojo.require("dojo.widget.TabContainer");
	dojo.require("dojo.widget.LinkPane");
	dojo.require("dojo.widget.ContentPane");
	dojo.require("dojo.widget.LayoutContainer");
	dojo.require("dojo.widget.Checkbox");
</script>
<style type="text/css">

.dojoTabPaneWrapper {
  padding : 1px 1px 1px 1px;
}

</style>

</head>

<body marginwidth="0" marginheight="0"  bgcolor="#E6F2FF">



	<div id="mainTabContainer" dojoType="TabContainer" class="mainTabContainer" selectedChild="tab1" >
	<%
    if(enable)
    {
   	if(result!=null  && result.equals("save"))
   	{
 	%>
	
		<div id="tab1"  dojoType="ContentPane" label="Accessorial & General Standard Charges" >
	  		<iframe src="<%=agssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" label="Document Charges/Commissions/MISC" >
	  		<iframe src="<%=documentCharges %>" width="100%" height="100%" frameborder="0" name="documentFrame" id="documentFrame" > </iframe>
		</div>
		<div id="tab3"  dojoType="ContentPane" label="Flight Schedule" >
	  		<iframe src="<%=flightShedules %>" width="100%" height="100%" frameborder="0" name="flightFrame" id="flightFrame" > </iframe>
		</div>
		<%
		
	}
	else
	{
		if(session.getAttribute("getAirEdit")!=null )
		{ %>
			<div id="tab1"  dojoType="ContentPane" label="Accessorial & General Standard Charges" >
	  		<iframe src="<%=editagssPath%>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
			</div>
			<div id="tab2"  dojoType="ContentPane" label="Accessorial General Standard Charges History" >
			 <iframe src="<%=airHistory %>" width="100%" height="100%" frameborder="0" name="importPortsFrame" id="importPortsFrame"></iframe>
		  	</div>
	  	<%} 
	  	if(session.getAttribute("getDocFreight")!=null )
		{ %>
			<div id="tab1"  dojoType="ContentPane" label="Document Charges/Commissions/MISC" >
	  		<iframe src="<%=documentCharges %>" width="100%" height="100%" frameborder="0" name="documentFrame" id="documentFrame" > </iframe>
			</div>
			<div id="tab2"  dojoType="ContentPane" label="Flight Schedule" >
	  		<iframe src="<%=flightShedules %>" width="100%" height="100%" frameborder="0" name="flightFrame" id="flightFrame" > </iframe>
			</div>
	  	<%} if(session.getAttribute("getAirAgsss")!=null )
		{ %>
			<div id="tab1"  dojoType="ContentPane" label="Accessorial & General Standard Charges" >
	  		<iframe src="<%=agssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
			</div>
	  	<%}
	
	}
	
	}
	else
	{
	if(result!=null && (result.equals("save") || result.equals("edit"))){
	%>
		<div id="tab1"  dojoType="ContentPane" label="Air Freight Rates" >
	  		<iframe src="<%=airDetailsPath %>" width="100%" height="100%" frameborder="0" name="airDetailsFrame" id="airDetailsFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" label="Commodity Specific Accessorial Charges" >
		  <iframe src="<%=csssPath %>" width="100%" height="100%" frameborder="0" name="csssFrame" id="csssFrame"> </iframe>
	  	</div>
	  	<%if(result!=null && result.equals("edit"))
	  	{ %>
		<div id="tab3"  dojoType="ContentPane" label="Commodity Specific Accessorial Rate History" >
		  <iframe src="<%=afrhPath %>" width="100%" height="100%" frameborder="0" name="airFrame" id="airFrame"> </iframe>
		</div>
		<div id="tab4"  dojoType="ContentPane" label="Air Freight Rate History" >
		 <iframe src="<%=aarhPath %>" width="100%" height="100%" frameborder="0" name="importFrame" id="importFrame"></iframe>
	  	</div>
	  	<%} %>
	<%
	 }}
	 


%>





</body>
</html>
