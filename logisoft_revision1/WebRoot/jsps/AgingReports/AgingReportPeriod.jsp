<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.beans.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html>
<head>
  
<title> City Search </title>
<%
UNLocationBean unBean= new UNLocationBean();
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
CustAddress custAddress = new CustAddress();
 
String CustomerName="";
String CustomerNo="";
String CustomerAddress="";
 List searchList=null;
 
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
 if(custAddress.getAddress1()!=null)
{
CustomerAddress = custAddress.getAddress1();
} 
}
if((List)session.getAttribute("searchList")!=null)
{
 searchList=(List)session.getAttribute("searchList");
 
 session.removeAttribute("searchList");
}
com.gp.cong.logisoft.util.DBUtil dbutil=new com.gp.cong.logisoft.util.DBUtil();
List GenericCodeCostList=null;

request.setAttribute("GenericCodeCostList",dbutil.getGenericCodeCostList(24,"yes","Select"));
%>


<%@include file="../includes/baseResources.jsp" %>

<script type="text/javascript">
function popup1(mylink, windowname)
{
alert("this is popup1");
if (!window.focus)
return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=510,height=400,scrollbars=yes');
mywindow.moveTo(200,180);
return false;
}
function agingReportPeriod()
{
alert("this is for printing");
document.agingreportPeriod.buttonValue.value="agingreportPeriod";
document.agingreportPeriod.submit();
}
</script>
</head>

<body class="whitebackgrnd">
<html:form action="/agingreportPeriod" name="agingreportPeriod" type="com.gp.cvst.logisoft.struts.form.AgingReportPeriodsForm" scope="request" >  
<html:hidden property="buttonValue"/>

<table>
<tr class="textlabels">
  <td width="439" align="left" class="headerbluelarge">&nbsp; Aging Report<br /> </td>
  <td width="200">&nbsp;</td>
  <td><img src="<%=path%>/img/print.gif" onClick="agingReportPeriod()"/></td>
  </tr>
  </table>


<table width="100%"  border="0" cellpadding="0" cellspacing="0">
<tr>
    <td class="headerbluesmall">&nbsp;&nbsp; Aging Report<br /> </td> 
  </tr>

  
  </table>
  <table >
      <tr>
    <td>CustomerName</td>
    
    <td  class="textlabels">&nbsp;<html:text property="customerName"  value="<%=CustomerName %>" styleClass="areahighlightgrey" />
        <img src="<%=path%>/img/search1.gif" height="16" onclick="return popup1('<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button='+'AgingReportPeriod&amp;customersearch='+document.agingreportPeriod.customerName.value,'windows3')"/></td>
  <td><html:radio value="yes" property="allcustomers"></html:radio>All Customers</td>
  </tr>
      <tr>
  
       
     <td>CustomerNumber</td>
      <td>&nbsp;<html:text property="customerNumber"  value="<%=CustomerNo %>"  styleClass="areahighlightgrey" /></td>
     </tr>
      
     <tr>
     <td valign="top">CustomerAddress</td> 
      <td>&nbsp;<html:textarea property="customerAddress" rows="4" cols="20" value="<%=CustomerAddress %>"  styleClass="areahighlightgrey" /></td>
     </tr>
     
    <%--<tr><td>Agent</td> 
     <td><html:text property="age"></html:text></td>
     --%>
     <tr>
     <td>Terminal</td><td><html:text property="terminal" size="12" maxlength="12"></html:text></td>
     <td><html:radio property="notinclude" value=""></html:radio>Agents NOT Included</td>
     <td><html:radio property="notinclude" value=""></html:radio>Agents Included&nbsp;</td>
       <td><html:radio property="onlyinclude" value=""></html:radio>Only Agents&nbsp;</td></tr>
       </table>
<tr>
 <tr><td> Collector</td>
 <td><html:select property="collector"><html:optionsCollection name="GenericCodeCostList" styleClass="unfixedtextfiledstyle" /></html:select></td>
 <td></td>
 </tr>
 <tr>
 <td>Company</td>
 
 <td><html:select  property="company"><option>Econo Air</option></html:select></td>
 <td>Number of days OverDue</td>
 
<td><html:text  property="overdue" size="8" maxlength="8"></html:text></td>
<td>Minimun $ Amount</td>
<td><html:text  property="minamt" size="8" maxlength="8"> </html:text></td>
 <td><br /></td>
 
 </tr>

 <table >
 <tr>
 <td >&nbsp;&nbsp;</td>
 <td align="center">&nbsp;&nbsp;Low</td><td>&nbsp;</td><td>&nbsp;</td>
  <td align="center">&nbsp;Medium</td><td>&nbsp;</td><td align="center" >&nbsp;High</td><td>&nbsp;</td><td>&nbsp;</td>
 <td align="center"></td></tr>

 <tr><td>Age Range <br /></td><td colspan="2">
 <html:text property="agingzeero" size="3" maxlength="3" value="0"></html:text> 
   <html:text property="agingthirty" size="3" maxlength="3" value="30"></html:text></td><td>&nbsp;</td>
   <td><html:text property="greaterthanthirty" size="3" maxlength="3" value="31"></html:text>
   <html:text property="agingsixty" size="3" maxlength="3" value="60"></html:text>   <br /></td>

<td><br /></td><td colspan="2">
<html:text property="greaterthansixty" size="3" maxlength="3" value="61"></html:text> 
  <html:text property="agingninty" size="3" maxlength="3" value="90"></html:text></td><td>&nbsp;</td>
  <td><html:text property="greaterthanninty" size="3" maxlength="3" value="90+"></html:text>
   <html:text property="nintyplus" size="3" maxlength="3"></html:text>   <br /></td></tr>
 </table>
 <table width=100% border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="headerbluesmall">&nbsp;&nbsp;Search List Values</td> 
  </tr>
  </table>
</html:form>

	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>