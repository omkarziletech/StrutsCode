<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String customerDetailsPath=path+"/jsps/Tradingpartnermaintainance/EditGeneralInformation.jsp";
String customerAccount=path+"/jsps/Tradingpartnermaintainance/EditAccounting.jsp";
String customerconfig=path+"/jsps/Tradingpartnermaintainance/ContactConfig.jsp";
String accountDetailsPath=path+"/jsps/Tradingpartnermaintainance/EditCustomer.jsp";
String vendorDetails=path+"/jsps/Tradingpartnermaintainance/Editvendor.jsp";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'EditCustomerTabs.jsp' starting page</title>
    
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
	<div id="tab1"  dojoType="ContentPane" style="font-family:Arial;" label="Addresses" >
	  <iframe src="<%=accountDetailsPath %>"  width="100%" height="100%" frameborder="0" name="geniframe1" id="geniframe"> </iframe>
	</div>
	<div id="tab2"  dojoType="ContentPane" style="font-family:Arial;" label="General Info" >
	  <iframe src="<%=customerDetailsPath %>"  width="100%" height="100%" frameborder="0" name="acciframe" id="acciframe"> </iframe>
	</div>
	<div id="tab3"  dojoType="ContentPane" style="font-family:Arial;" label="AR Config" >
		  <iframe src="<%=customerAccount%>" width="100%" height="100%" frameborder="0" name="aciframe" id="aciframe"> </iframe>
	</div>
    <div id="tab4"  dojoType="ContentPane" style="font-family:Arial;" label="AP Config" >
	  <iframe src="<%=vendorDetails%>" width="100%" height="100%" frameborder="0" name="editvFrame" id="editvframe"> </iframe>
	</div>
	<div id="tab5"  dojoType="ContentPane" style="font-family:Arial;" label="Contact Config" >
	  <iframe src="<%=customerconfig%>" width="100%" height="100%" frameborder="0" name="custiframe"> </iframe>
	</div>
</div>
  </body>
</html>
