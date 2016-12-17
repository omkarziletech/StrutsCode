<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.text.SimpleDateFormat"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Fiscal Calender</title>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
String fisperiod="";
Date startdate1=new Date();
SimpleDateFormat sid=new SimpleDateFormat("MM/dd/yyyy");
Date enddate1=new Date();
String startdate="";
String enddate="";
String valueid="";
int valueid1=0;
if(request.getParameter("val")!=null)
{
valueid=(String)request.getParameter("val");
valueid1=Integer.parseInt(valueid);
}
List searchlist=null;
FiscalPeriod fis=new FiscalPeriod();
if(session.getAttribute("searchlist")!=null)
{
searchlist=(List)session.getAttribute("searchlist");
fis=(FiscalPeriod)searchlist.get(valueid1);
fisperiod=fis.getPeriod();
startdate1=fis.getStartDate();
startdate=sid.format(startdate1);
enddate1=fis.getEndDate();
enddate=sid.format(enddate1);
}

request.setAttribute("fiscalperiod",fis);
String close = (String) request.getAttribute("buttonValue");

if(close!=null && close.equals("completed"))
{
 %>
<script>
 self.close();
 opener.location.href="<%=path%>/jsps/Accounting/SearchFiscalCalender.jsp";
</script>
<% }%>
<%@include file="../includes/baseResources.jsp" %>

<script type="text/javascript">


function update()
{

 var a = "<%=startdate%>";
  document.editSearchfiscalform.index.value="<%=valueid1%>"; 
 document.editSearchfiscalform.fiscalperiod.value="<%=fisperiod%>"; 
 var x = document.editSearchfiscalform.startingdate.value;
 var a1 = a.toString();
 var a2 = a1.substring(0,2);
 var a3 = a1.substring(6,10);
 var b1 = x.toString();
 var b2 = b1.substring(0,2);
 var b3 = b1.substring(6,10);
 var c = "<%=enddate%>";
  document.editSearchfiscalform.index.value="<%=valueid1%>"; 
 document.editSearchfiscalform.fiscalperiod.value="<%=fisperiod%>"; 
 var y = document.editSearchfiscalform.endingdate.value;
 var c1 = c.toString();
 var c2 = c1.substring(0,2);
 var c3 = c1.substring(6,10);
 var d1 = y.toString();
 var d2 = d1.substring(0,2);
 var d3 = d1.substring(6,10); 
   
if((a2 != b2 || a3 != b3)||(c2 != d2 || c3 !=d3))
 {
  alert("..Please change only Date ..");
 }
else
 {
  document.editSearchfiscalform.buttonValue.value="update";
  document.editSearchfiscalform.submit();
  
 }
} 


</script>
</head>
<body class="whitebackgrnd" onload="load()">
<html:form action="/editSearchFiscal" name="editSearchfiscalform" type="com.gp.cvst.logisoft.struts.form.EditSearchFiscalForm" scope="request" >

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" height="90%">Edit FiscalCalender</tr> 
 <td>


<br/>
<%--<table width="100%" height="15" border="0" cellpadding="0" cellspacing="0">--%>
<%-- <tr>--%>
<%--    <td><span >&nbsp; </span></td>--%>
<%-- </tr>--%>
<%-- <tr>--%>
<%--    <td>&nbsp;</td>--%>
<%-- </tr>--%>
<%-- --%>
<%--    <td height="15" width=100% colspan="8"  class="headerbluesmall">&nbsp;&nbsp;</td>--%>
<%-- </tr>--%>
<%--</table>--%>
<table height="15" width="100%" cellpadding="0" cellspacing="0">
<tr class="tableHeadingNew">hai Edit FiscalCalender :</tr>

 <tr>
   <td class="textlabels"><b>Period</b></td>
   <td class="textlabels"><b>StartDate</b></td>
   <td colspan="2" class="textlabels"><b>EndDate</b></td>
   <td class="textlabels">&nbsp;</td>
 </tr>
 <tr>
   <td ><html:text property="fiscalperiod"  value="<%=fisperiod%>" readonly="true" /></td>
   <td><html:text property="startingdate" value="<%=startdate%>"   styleId="txtcal2" /><img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal2" id="cal2" onmousedown="insertDateFromCalendar(this.id,0);" /> </td>
   <td ><html:text property="endingdate"   value="<%=enddate%>" styleId="txtcal3" /><img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal3" id="cal3" onmousedown="insertDateFromCalendar(this.id,0);" /> </td>
   
   
 </tr>
 <tr>
  	<td valign="top" colspan="4" align="center" style="padding-top:10px;">
		      	 <input type="button" name="search" onclick="update()" value="Save" class="buttonStyleNew"/></td>
 </tr>
</table> 
</td>
</table>
<html:hidden property="buttonValue"/>
<html:hidden property="index"/>

</html:form>
</body>

	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
