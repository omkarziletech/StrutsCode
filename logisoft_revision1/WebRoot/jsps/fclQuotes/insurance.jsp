<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
   <head>
 
<title>Insurance</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
 
<%@include file="../includes/baseResources.jsp" %>

<script language="javascript" type="text/javascript">
</head>

<body class="whitebackgrnd">
<table>
<tr class="textlabels">
  <td width="439" align="left" class="headerbluelarge">&nbsp;&nbsp;Insurance </td>
  </tr>
  </table>

<table width="100%"  border="0" cellpadding="0" cellspacing="0">
<tr>
    <td class="headerbluesmall">&nbsp;&nbsp;Insurance</td> 
  </tr>
 <tr>
    <td height="152"><table width="897" border="0" cellpadding="0" cellspacing="0"> 
	
  
  <tr>
    <td width="84"><span class="textlabels">Insurance Code:</span></td>
    <td width="293" class="textlabels"><input name="radiobutton" type="radio" value="radiobutton" />
Y
  <input name="radiobutton" type="radio" value="radiobutton" />
N</td>
    <td width="520">&nbsp;</td>
  </tr>
  <tr>
    <td height="31"><span class="textlabels">Insurance Rate </span></td>
    <td class="textlabels"><input name="textfield" type="text" value="80" />
=($.80 per $100)</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><span class="textlabels">Cost of Goods </span></td>
    <td class="textlabels"><input name="textfield2" type="text" class="unfixedtextfiledstyle" size="35" /></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>
