<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
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

String fisperiod=request.getParameter("period");
String startdate=request.getParameter("startdate");
String enddate=request.getParameter("enddate");
String valueid=request.getParameter("val");
String year=request.getParameter("year");
List editbvalues = null;
String close = (String) request.getAttribute("buttonValue");

if(close!=null && close.equals("completed"))
{
 %>
<script>
 self.close();
 opener.location.href="<%=path%>/jsps/Accounting/FiscalCalender.jsp";
</script>
<% }%>
 
<%@include file="../includes/baseResources.jsp" %>

<script type="text/javascript">

function update()
{

 var a = "<%=startdate%>";
 document.editFiscalForm.fiscalperiod.value="<%=valueid%>"; 
 document.editFiscalForm.year.value="<%=year%>";

 var x = document.editFiscalForm.startingdate1.value;
 var a1 = a.toString();
 var a2 = a1.substring(0,2);
 var a3 = a1.substring(6,10);
 var b1 = x.toString();
 var b2 = b1.substring(0,2);
 var b3 = b1.substring(6,10);
 var c = "<%=enddate%>";
 document.editFiscalForm.fiscalperiod.value="<%=valueid%>"; 
 document.editFiscalForm.year.value="<%=year%>";
 
 var y = document.editFiscalForm.endingdate1.value;
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
 document.editFiscalForm.year.value="<%=year%>";

  document.editFiscalForm.buttonValue.value="update";

  document.editFiscalForm.submit();
 }
} 

</script>

</head>
<body class="whitebackgrnd" onload="load()">
<html:form action="/editfiscal" name="editFiscalForm" type="com.gp.cvst.logisoft.struts.form.EditFiscalForm" scope="request" >
<table width="100%" height="15" border="0" cellpadding="0" cellspacing="0">
 <tr>
    <td><span class="headerbluelarge">&nbsp;Edit FiscalCalender </span></td>
 </tr>
 <tr>
    <td>&nbsp;</td>
 </tr>
 
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
 <tr class="tableHeadingNew" >Batch List</tr>
 
 <tr>
   <td class="textlabels">Period</td>
   <td class="textlabels">StartDate</td>
   <td colspan="2" class="textlabels">EndDate</td>
   <td class="textlabels">&nbsp;</td>
 </tr>
 <tr>
   <td ><html:text property="fisperiod1"  value="<%=fisperiod%>" readonly="true" /></td>
   <td ><html:text property="startingdate1" value="<%=startdate%>"  styleId="txtcal2" /><img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal2" id="cal2" onmousedown="insertDateFromCalendar(this.id,0);" /> </td>
   <td ><html:text property="endingdate1"   value="<%=enddate%>"   styleId="txtcal3" /><img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal3" id="cal3" onmousedown="insertDateFromCalendar(this.id,0);" /> </td>

   
 </tr>
 <tr>
   <td valign="top" colspan="4" align="center" style="padding-top:10px;">
		      	 <input type="button" name="search" onclick="update()" value="Save" class="buttonStyleNew"/>
		      	 </td>
 </tr>
</table> 
<html:hidden property="buttonValue"   />
<html:hidden property="fiscalperiod"/>
<html:hidden property="startingdate"/>
<html:hidden property="endingdate"/> 
<html:hidden property="year"/> 
</html:form>
</body>

	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
