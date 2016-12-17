<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.domain.*" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
 <%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


CustAddress custAddress = new CustAddress();
String CustomerName="";
String CustomerNo="";

if(session.getAttribute("batch")!=null)
{
 custAddress = (CustAddress)session.getAttribute("batch");
if(custAddress.getAcctName()!=null)
{
CustomerName = custAddress.getAcctName();
}
if(custAddress.getAcctNo()!=null)
{
CustomerNo = custAddress.getAcctNo();
}



}


 %>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Fields to Maintain</title>
<%@include file="../includes/baseResources.jsp" %>
</head>

<body class="whitebackgrnd">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<td>
<table width="805" height="53" border="0" cellpadding="0" cellspacing="0" >
<tr class="textlabels">
  <td colspan="2" align="left" class="headerbluelarge">&nbsp;</td>
</tr>

<tr class="textlabels" >
  <td width="478" align="left" class="headerbluelarge">E-Statement</td>
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
     <tr class="tableHeadingNew">E-Statement</tr>
</table>
  
  <table width="679" border="0" cellpadding="0" cellspacing="0">
  <tr class="textlabels">
    <td width="163">&nbsp;</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td height="19" class="textlabels">Customer:</td>
    <td colspan="2"><%=CustomerName %> </td>
  </tr>
  <tr class="textlabels">
    <td height="17">Customer Number: </td>
    <td colspan="2"><%=CustomerNo %></td>
  </tr>
  <tr class="textlabels">
    <td height="18">Amount</td>
    <td colspan="2">$100.00</td>
  </tr>
  <tr class="textlabels">
    <td height="24">Email Address </td>
    <td colspan="2"><select name="select1"><option>psm@email.com</option></select></td>
  </tr>
  <tr class="textlabels">
    <td height="27">Open All Items
      <label>
      <input name="radiobutton" type="radio" value="radiobutton" checked="checked"/>
    Yes or </label></td>
    <td width="56">Date To </td>
    <td width="460"><select name="select" class="smalltextstyle">
    </select>
    </td>
  </tr>
   
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td colspan="2" >&nbsp;</td>
   </tr>
  
  
</table>
 </td>
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

