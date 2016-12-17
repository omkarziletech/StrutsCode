<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

if(path==null)
{
path = "../..";
}

%>
 
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Msoutlook</title>
<%@include file="../includes/baseResources.jsp" %>
</head>

<body class="whitebackgrnd">
<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew">MS-OutLook</tr>
<td>

<table width="805" height="53" border="0" cellpadding="0" cellspacing="0" >
<tr class="textlabels">
  <td colspan="2" align="left" class="headerbluelarge">&nbsp;</td>
</tr>
<tr class="textlabels">
<%--  <td width="478" align="left" class="headerbluelarge">MS-OutLook</td>--%>
  <td width="103"><a href="#" onmouseover='xpe(&quot;z0kmyo&quot;);' onmouseout='xpe(&quot;z0kmyn&quot;);'onmousedown='xpe(&quot;z0kmyc&quot;);' onclick="toggleTable('hiddentablesty5',1)"></td>
    <td width="224">&nbsp;</td>
</tr>
<tr class="textlabels">
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
  <td colspan="2"></td></tr>
</table>
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
     <tr class="tableHeadingNew"><td>&nbsp;</td>
     
  </tr>
</table>
  
  <table width="679" border="0" cellpadding="0" cellspacing="0">
  <tr><td>&nbsp;</td></tr>
  <tr class="textlabels">
    <td width="312">TO: (Email filled by in system with address on file for customer) </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>From: ( Email address of user) </td>
    <td>&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>Attachments: (System will consolidate invoices into a<br />
    single PDF and attach).</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td height="27">&nbsp;</td>
    <td width="367">&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td><textarea name="textarea2" class="styleborde6"  cols="40" rows="15">(User can enter free form text for body of email)</textarea></td>
    <td>&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>&nbsp;</td>
    </tr>
</table>
 
</body>

<script language="javascript" type="text/javascript">
	function popup(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=500,height=200,scrollbars=yes');
return false;
}
	function popup1(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=500,height=310,scrollbars=yes');
return false;
}	
	function popup2(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=500,height=350,scrollbars=yes');
return false;
}			
        function confirmdelete()
	{
		var result = confirm('Are you sure you want to delete Warehouse Details?');
		return result
	}
	 function confirmdelete1()
	{
		var result = confirm('This Terminal is associated to user. So cannot delete this Terminal');
		return result
	}
		</script>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

 
