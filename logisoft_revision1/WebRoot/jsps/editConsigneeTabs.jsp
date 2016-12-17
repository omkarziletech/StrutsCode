<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String accountDetailsPath=path+"/jsps/Tradingpartnermaintainance/editConsignee.jsp";
String consigneeDetailsPath=path+"/jsps/Tradingpartnermaintainance/editConsigneeInformation.jsp";
String consigneeconfig=path+"/jsps/Tradingpartnermaintainance/consigneeConfig.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'ConsigneeTabs.jsp' starting page</title>
    
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
<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Account Details" >
	  <iframe src="<%=accountDetailsPath%>"  width="100%" height="100%" frameborder="0" name="acciframe" id="acciframe"> </iframe>
	</div>
	<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="General Information" >
	  <iframe src="<%=consigneeDetailsPath %>"  width="100%" height="100%" frameborder="0" name="geniframe" id="geniframe"> </iframe>
	</div>
		<div id="tab3"  dojoType="ContentPane" style="font-family:Arial;" label="Contact Configuration" >
		  <iframe src="<%=consigneeconfig%>" width="100%" height="100%" frameborder="0" name="aciframe" id="aciframe"> </iframe>
		</div>

  
</div>

  </body>
</html>
