<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
// agss,csss,afrh,aarh are abreviations of the tabs found in Air Rates
String airDetailsPath=path+"/jsps/ratemanagement/retailDetails.jsp";
String agssPath=path+"/jsps/ratemanagement/retailAGSC.jsp";
String csssPath=path+"/jsps/ratemanagement/retailCSSC.jsp";
String afrhPath=path+"/jsps/ratemanagement/retailORH.jsp";
String aarhPath=path+"/jsps/ratemanagement/retailOARH.jsp";
String agsedit=path+"/jsps/ratemanagement/retailEditAGSC.jsp";
String documentCharges=path+"/jsps/ratemanagement/retailDocumentCharges.jsp";
String AGSCH=path+"/jsps/ratemanagement/retailAGSCH.jsp";
//String flightShedules=path+"/jsps/ratemanagement/flightShedule.jsp";
 boolean enable=false;;
	if(session.getAttribute("retailsetTabEnable")!=null)
	{
		
		if(session.getAttribute("retailsetTabEnable").equals("retailenable"))
		{
			enable = true;
		}
		else
		{
			enable = false;
		}
		
	}
	String result="";
	
	if (session.getAttribute("retaileditrecord") != null) {
		result = (String) session.getAttribute("retaileditrecord");
		
		
		}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Retail Rates Tabs</title>
<%@include file="includes/baseResources.jsp" %>
<script type="text/javascript">
	var djConfig = { isDebug: true };
</script>
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
 	%>
		<%
if(result!=null && result.equals("save"))
		{%>
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Accesorial & General Standard Charges" >
		  		<iframe src="<%=agssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
			</div>
			<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Document Charges/Commissions/MISC" >
		  		<iframe src="<%=documentCharges %>" width="100%" height="100%" frameborder="0" name="documentFrame" id="documentFrame" > </iframe>
			</div>
		<%
		
	}
if(result!=null && result.equals("edit"))
		{ 
		if(session.getAttribute("addaccess")!=null)
		{
		%>
		   <div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Accesorial & General Standard Charges" >
		  		<iframe src="<%=agsedit%>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
			</div>
			<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Accesorial General Standard Charges History" >
		  		<iframe src="<%=AGSCH%>" width="100%" height="100%" frameborder="0" name="aGSCHFrame" id="aGSCHFrame" > </iframe>
			</div>	
<%		}
	  else if(session.getAttribute("getRetailsAgsss")!=null)
	   {
			%>
			<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Accesorial & General Standard Charges" >
		  		<iframe src="<%=agssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
			</div>
		<%}
		
		else if(session.getAttribute("getRetailsDoc")!=null)
		{
			%>
			<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Document Charges/Commissions/MISC" >
		  		<iframe src="<%=documentCharges %>" width="100%" height="100%" frameborder="0" name="documentFrame" id="documentFrame" > </iframe>
			</div>
		<%
		}
		}
	}else{
	if(result!=null && (result.equals("add") || result.equals("edit")))
	%>
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Ocean Freight Rates" >
	  		<iframe src="<%=airDetailsPath %>" width="100%" height="100%" frameborder="0" name="airDetailsFrame" id="airDetailsFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Commodity Specific Accesorial Charges" >
		  <iframe src="<%=csssPath %>" width="100%" height="100%" frameborder="0" name="csssFrame" id="csssFrame"> </iframe>
	  	</div>
	  	<%if(result!=null && result.equals("edit"))
	  	{ %>
		<div id="tab3"  dojoType="ContentPane" style="font-family:Arial;" label="Ocean Freight Rates History" >
		  <iframe src="<%=afrhPath %>" width="100%" height="100%" frameborder="0" name="airPortsFrame" id="airPortsFrame"> </iframe>
		</div>
		<div id="tab4"  dojoType="ContentPane" label="Commodity Specific Accesorial Charges History" >
		 <iframe src="<%=aarhPath %>" width="100%" height="100%" frameborder="0" name="importPortsFrame" id="importPortsFrame"></iframe>
	  	</div>
	  	<%} %>
	<%
	 }
	%>	
</div>


</body>
</html>
