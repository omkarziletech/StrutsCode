<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
	String addAirRatesPath=path+"/jsps/ratemanagement/addAirRates.jsp";
	String addAirRatesTabDetailsPath=path+"/jsps/airRatesTabs.jsp";


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="../includes/baseResources.jsp" %>


<title>Port</title>
</head>

<frameset rows="50,108" cols=""  framespacing="0"  frameborder="NO"  border="0"   >
  <frame src="<%=addAirRatesPath%>" name="topFrame" id="topFrame" title="top" class="tableBorderNew" style="border-bottom:0px;"/>
  <frame src="<%=addAirRatesTabDetailsPath%>" name="mainFrame" id="mainFrame" title="bottom" scrolling="no"  class="tableBorderNew"/>
</frameset>
<noframes><body>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</noframes></html>