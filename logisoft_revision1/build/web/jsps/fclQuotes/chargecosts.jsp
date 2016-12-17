<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String buttonValue=""; 
String msg="";
String modify = null;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'chargecosts.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body class="whitebackgrnd" onLoad="disabled('<%=modify%>')" >
		<html:form action="/chargeCosts" name="chargeCosts" type="com.gp.cong.logisoft.struts.form.ChargeCostsForm" scope="request">
		<font color="blue"><h4><%=msg%></h4></font>
		<table width="531" height="121" border="0" cellpadding="0" cellspacing="0">
		<tr>
    <td width="531">
<div style="width:80%;height:200px;overflow:auto; border:thin;border-style:solid;">
	<table width="385" border="0" cellspacing="0" cellpadding="0">

      <tr class="tablheader">
        <td class="style2" width="84">Charge</td>
        <td class="style2" width="124">Amount</td>
        <td class="style2" width="98">Bill To </td>
        <td class="style2" width="79">Print on BL </td>
      </tr>
      <tr>
             <td ><html:text property="charge0" value="<%=%>" /></td>
             <td ><html:text property="amount0" value="<%=%>" /></td>
		</html:form>
</body>
</html>
