<%@ page language="java" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.beans.SearchCarriersBean,com.gp.cong.logisoft.domain.CarriersOrLine,com.gp.cong.logisoft.domain.GenericCode" pageEncoding="ISO-8859-1"%>
<%@page import="org.apache.velocity.runtime.directive.Include"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<html>
  <head>
    <title>My JSP 'CustomerFrame.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body>
   <jsp:include page="/jsps/Tradingpartnermaintainance/MasterCustom.jsp" >
       <jsp:param name="callFrom" value="${callFrom}"/>
       <jsp:param name="field" value="${field}"/>
   </jsp:include>
   <br>
   <jsp:include page="/jsps/MasterCustomerTabs.jsp" >
       <jsp:param name="callFrom" value="${callFrom}"/>
       <jsp:param name="field" value="${field}"/>
   </jsp:include>
  </body>
</html>

