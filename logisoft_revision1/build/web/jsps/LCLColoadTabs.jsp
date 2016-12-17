<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String airDetailsPath=path+"/jsps/ratemanagement/AddLclColaodDetails.jsp";
String agssPath=path+"/jsps/ratemanagement/coAgss.jsp";
String editagssPath=path+"/jsps/ratemanagement/editCoAgss.jsp";
String csssPath=path+"/jsps/ratemanagement/AddLclColoadCommodity.jsp";
String afrhPath=path+"/jsps/ratemanagement/ocenDetailHistory.jsp";
String aarhPath=path+"/jsps/ratemanagement/ocenAccessComHistory.jsp";
String documentCharges=path+"/jsps/ratemanagement/lclCoDocumentCharges.jsp";
String AccessHistory=path+"/jsps/ratemanagement/lclAccessComHistory.jsp";
String result="";
boolean enable=false;;
 
	if(session.getAttribute("setLCLColoadTabEnable")!=null)
	{
		if(session.getAttribute("setLCLColoadTabEnable").equals("enable"))
		{
			enable = true;
		}
		else
		{
			enable = false;
		}
	}
	if (session.getAttribute("editrecord") != null) 
	{
		result = (String) session.getAttribute("editrecord");
		
		
		}	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'LCLColoadTabs.jsp' starting page</title>
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
    if(result!=null && result.equals("save"))
   {%>
   <div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Accessorial & General Standard Charges" >
	  		<iframe src="<%=agssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Document Charges/Commissions/MISC" >
	  		<iframe src="<%=documentCharges %>" width="100%" height="100%" frameborder="0" name="documentFrame" id="documentFrame" > </iframe>
		</div>
	
   <%}
   if(result!=null && result.equals("edit"))
   {
    if(session.getAttribute("getEdit")!=null)
    {
 	%>
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Accessorial & General Standard Charges" >
	  		<iframe src="<%=editagssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Accessorial & General Standard Charges History" >
	  		<iframe src="<%=AccessHistory %>" width="100%" height="100%" frameborder="0" name="historyagss" id="historyagss" > </iframe>
		</div>
	<%
	}
	else if(session.getAttribute("getAgsss")!=null)
	{ 
	
	%>
	<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Accessorial & General Standard Charges" >
	  		<iframe src="<%=agssPath %>" width="100%" height="100%" frameborder="0" name="agssFrame" id="agssFrame" > </iframe>
		</div>
	
	<%}
	else if(session.getAttribute("getDoc")!=null)
	{
	
	 %>
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Document Charges/Commissions/MISC" >
	  		<iframe src="<%=documentCharges %>" width="100%" height="100%" frameborder="0" name="documentFrame" id="documentFrame" > </iframe>
		</div>
<%   }
	}
	}
	else
	{
	if(result!=null && (result.equals("add") || result.equals("edit")|| result.equals("save") ))
	{
	%>
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Ocean Freight Rates" >
	  		<iframe src="<%=airDetailsPath %>" width="100%" height="100%" frameborder="0" name="airDetailsFrame" id="airDetailsFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Commodity specific Accessorial charges" >
		  <iframe src="<%=csssPath %>" width="100%" height="100%" frameborder="0" name="csssFrame" id="csssFrame"> </iframe>
	  	</div>
	  	<%if(result!=null && result.equals("edit"))
	  	{%>
		<div id="tab3"  dojoType="ContentPane" style="font-family:Arial;" label="Ocean Freight Rates History" >
		  <iframe src="<%=afrhPath %>" width="100%" height="100%" frameborder="0" name="airPortsFrame" id="airPortsFrame"> </iframe>
		</div>
		<div id="tab4"  dojoType="ContentPane" label="Ocean Accessorial Rates History" >
		 <iframe src="<%=aarhPath %>" width="100%" height="100%" frameborder="0" name="importFrame" id="importFrame"></iframe>
	  	</div>
	<%}
	}
	 }
	%>	
</div>

  </body>
</html>
