<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
// agss,csss,afrh,aarh are abreviations of the tabs found in Air Rates
String uniFlate=path+"/jsps/ratemanagement/universalFlateRate.jsp";
String uniCommodity=path+"/jsps/ratemanagement/universalCommodity.jsp";
String uniAirFright=path+"/jsps/ratemanagement/universalAirRate.jsp";
String uniInsurace=path+"/jsps/ratemanagement/universalInsurance.jsp";
String uniFlateHistory=path+"/jsps/ratemanagement/universalFlateRateHistory.jsp";
String uniCommodityHistory=path+"/jsps/ratemanagement/universalCommodityHistory.jsp";
String uniAirFrightHistory=path+"/jsps/ratemanagement/universalAirRateHistory.jsp";
String uniInsuraceHistory=path+"/jsps/ratemanagement/universalInsuranceHistory.jsp";

 boolean enable=false;;
	/*if(session.getAttribute("retailsetTabEnable")!=null)
	{
		
		if(session.getAttribute("retailsetTabEnable").equals("retailenable"))
		{
			enable = true;
		}
		else
		{
			enable = false;
		}
		
	}*/
	String result="";
	if (session.getAttribute("universaltabs") != null) {
		result = (String) session.getAttribute("universaltabs");
		
		
		}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Retail Rates Tabs</title>
<script type="text/javascript">
	var djConfig = { isDebug: true };
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dojo/dojo.js"></script>
<%@include file="../includes/baseResources.jsp" %>

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
//    if(enable)
if(result!=null && result.equals("save"))
    {
 	%>
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Flat Rate Per Unit" >
	  		<iframe src="<%=uniFlate %>" width="100%" height="100%" frameborder="0" name="flateFrame" id="flateFrame" > </iframe>
		</div>
		
		<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Accesorial General Standard Charges" >
	  		<iframe src="<%=uniCommodity %>" width="100%" height="100%" frameborder="0" name="unicommFrame" id="unicommFrame" > </iframe>
		</div>
		
		<div id="tab3"  dojoType="ContentPane" style="font-family:Arial;" label="Import Air" >
	  		<iframe src="<%=uniAirFright%>" width="100%" height="100%" frameborder="0" name="importAir" id="importAir" > </iframe>
		</div>
		<div id="tab4"  dojoType="ContentPane" style="font-family:Arial;" label="Insurance Charges" >
	  		<iframe src="<%=uniInsurace %>" width="100%" height="100%" frameborder="0" name="insurancechrg" id="insurancechrg" > </iframe>
		</div>
	<%
	}else if(result!=null && (result.equals("edit"))){
	%>
		
		<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Flat Rate Per Unit" >
	  		<iframe src="<%=uniFlate %>" width="100%" height="100%" frameborder="0" name="flateFrame" id="flateFrame" > </iframe>
		</div>
		<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="Accesorial General Standard Charges " >
	  		<iframe src="<%=uniCommodity %>" width="100%" height="100%" frameborder="0" name="unicommFrame" id="unicommFrame" > </iframe>
		</div>
		
		<div id="tab3"  dojoType="ContentPane" style="font-family:Arial;" label="Import Air" >
	  		<iframe src="<%=uniAirFright%>" width="100%" height="100%" frameborder="0" name="importAir" id="importAir" > </iframe>
		</div>
		<div id="tab4"  dojoType="ContentPane" style="font-family:Arial;" label="Insurance Charges" >
	  		<iframe src="<%=uniInsurace %>" width="100%" height="100%" frameborder="0" name="insurancechrg" id="insurancechrg" > </iframe>
		</div>
		<div id="tab5"  dojoType="ContentPane" style="font-family:Arial;" label="Flat Rate Per Unit History" >
		  <iframe src="<%=uniFlateHistory %>" width="100%" height="100%" frameborder="0" name="flateFrameHistory" id="flateFrameHistory"> </iframe>
		</div>
		<div id="tab6"  dojoType="ContentPane" label="Acce_ Gen Standard Charges History" >
		 <iframe src="<%=uniCommodityHistory %>" width="100%" height="100%" frameborder="0" name="commFrameHistory" id="commFrameHistory"></iframe>
	  	</div>
	  	<div id="tab7"  dojoType="ContentPane" style="font-family:Arial;" label="Import Air History" >
		  <iframe src="<%=uniAirFrightHistory %>" width="100%" height="100%" frameborder="0" name="importAirHistory" id="importAirHistory"> </iframe>
		</div>
		<div id="tab8"  dojoType="ContentPane" label="Insurance Charges History" >
		 <iframe src="<%=uniInsuraceHistory %>" width="100%" height="100%" frameborder="0" name="insurancechrgHistory" id="insurancechrgHistory"></iframe>
	  	</div>
	  	
	<%
	 }
	%>	
</div>


</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
