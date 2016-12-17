<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.text.SimpleDateFormat"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html>
<head>
<title>Fiscal Calender</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
path="../..";
}



List yearlists=null;
List periodlists=null;
List monthLists=null;
List forcondition3=null;
List forcondition2=null;

String year="";
String fiscalPerid1="";
String month1="";
String u="01/01/2008";
String v="01/31/2008";

DBUtil dbUtil=new DBUtil();
request.setAttribute("yearlists",dbUtil.getyearsList1());
yearlists=(List)request.getAttribute("yearlists");
DBUtil dbUtil1=new DBUtil();
request.setAttribute("periodlists",dbUtil1.getperiodList1());
periodlists=(List)request.getAttribute("periodlists");
DBUtil dbUtil3=new DBUtil();

request.setAttribute("monthLists",dbUtil3.getmonthList1());
monthLists=(List)request.getAttribute("monthLists");
 
if((List)session.getAttribute("abc")!=null)
{
  forcondition3=(List)session.getAttribute("abc");
}
if((List)session.getAttribute("forcondition1")!=null)
{
  forcondition2=(List)session.getAttribute("forcondition1");
}
 
if((String)session.getAttribute("fiscalPerid")!=null)
{
  fiscalPerid1=(String)session.getAttribute("fiscalPerid");
}
if((String)session.getAttribute("month")!=null)
{
  month1=(String)session.getAttribute("month");
}
String year1="";
if(session.getAttribute("year")!=null)
{
year1=(String)session.getAttribute("year");
}
String status2="";
if(session.getAttribute("status1")!=null)
{
status2=(String)session.getAttribute("status1");


}
List saveList2=null;
if(session.getAttribute("saveList")!=null)
{
saveList2=(List)session.getAttribute("saveList");
}
String message1="";
if(request.getAttribute("message")!=null)
 {
  message1=(String)request.getAttribute("message");
 }
%>

<%@include file="../includes/baseResources.jsp" %>

<script type="text/javascript">

function go()
 {
 if(document.fiscalPeriodForm.year.value=="00")
   {
    alert("Please Select Year");
    return false;
  }
   
  if(document.fiscalPeriodForm.year.value!=null && document.fiscalPeriodForm.fisperiod.value=="00")
   {
    alert("Select Only year");
    document.fiscalPeriodForm.buttonValue.value="yearList";
    document.fiscalPeriodForm.submit();
    return false;
   }
 
 if(document.fiscalPeriodForm.year.value!=null && document.fiscalPeriodForm.fisperiod.value!=null)
  {
  if(document.fiscalPeriodForm.fisperiod.value==13)
   {
  
      toggleTable('hiddentablesty4',1);
    document.fiscalPeriodForm.buttonValue.value="go";
   }
  if(document.fiscalPeriodForm.fisperiod.value==12)
  {
 
   document.getElementById('hiddentablesty4').style.visibility='hidden';
 
   document.fiscalPeriodForm.buttonValue.value="go1";
  }
  
 }
 
  
 document.fiscalPeriodForm.submit();
 }
function save()
{
    if(document.fiscalPeriodForm.fisperiod.value==13)
    {
    var i=0;
    var j=1;
   for(i=0;i<13;i++)
    {
    
      if(document.fiscalPeriodForm.startingdate[i].value=="")
	  {
	  alert("please Enter Starting date at .. "+j);
	  document.fiscalPeriodForm.startingdate[i].focus();
	  return false;
	  }
	   if(document.fiscalPeriodForm.endingdate[i].value=="")
	  {
	  alert("please Enter Ending date at .. "+j);
	  document.fiscalPeriodForm.endingdate[i].focus();
	  return false;
	  }
	  var DATEFROM=document.fiscalPeriodForm.startingdate[i].value;
	  var stdateis = DATEFROM.toString();
	  var styear = stdateis.substring(6,10);
	  var DATETO=document.forms[0].endingdate[i].value;
       var enddateis = DATETO.toString();
	  var endyear = enddateis.substring(6,10);
	  
       if(document.fiscalPeriodForm.year.value != styear)
       {
         alert("Please enter right year .. "+j);
         return false;
       }
       if(document.fiscalPeriodForm.year.value != endyear)
       {
         alert("Please enter right year .. "+j);
         return false;
       }
      if (Date.parse(document.fiscalPeriodForm.startingdate[i].value) >= Date.parse(document.fiscalPeriodForm.endingdate[i].value)) 
      {
       alert("Invalid Date Range!\nStartingdate cannot be  equal  or  greater than Endingdate! in --"+j);
       return false;
      }
      j++;
      }
      }
     
document.fiscalPeriodForm.buttonValue.value="save";
document.fiscalPeriodForm.submit();
 }

function editfiscal(obj,val,windowname)
{
alert("Edit Only Date " );
var years="<%=year1%>";

   var x=document.getElementById('divtablesty1').rows[++val].cells;
   var fperiod = x[0].innerHTML;
   var sdate=x[1].innerHTML;
   var edate=x[2].innerHTML;
  if (!window.focus)return true;
    var href;
    var mylink="<%=path%>/jsps/Accounting/EditFiscalCalender.jsp?period="+fperiod+"&startdate="+sdate+"&enddate="+edate+"&val="+val+"&year="+years;
  if (typeof(mylink) == 'string')
  if (typeof(mylink) == 'string')
  href=mylink;
  else
   href=mylink.href;
   window.open(href, windowname, 'width=600,height=200,scrollbars=yes');
   return false;
   document.fiscalPeriodForm.submit();
}
function fiscalmonth()
{

 
  if(document.fiscalPeriodForm.fisperiod.value==12)
   {
  toggleTable('hiddentablesty',1);
  toggleTable('hiddentablesty5',1);
 
  }
  else
  {
  document.getElementById('hiddentablesty').style.visibility='hidden';
  document.getElementById('hiddentablesty5').style.visibility='hidden';
   
  }
 }

function load()
{
if(document.fiscalPeriodForm.fisperiod.value==13)
  {
  toggleTable('hiddentablesty',1);
  toggleTable('hiddentablesty5',1);
 toggleTable('hiddentablesty4',1);
  }
  if(document.fiscalPeriodForm.fisperiod.value==12)
   {
  toggleTable('hiddentablesty',1);
  toggleTable('hiddentablesty5',1);
  toggleTable('divtablesty1',1);
   }
  else
  {
  document.getElementById('hiddentablesty').style.visibility='hidden';
  document.getElementById('hiddentablesty5').style.visibility='hidden';
   }
 }
 function print()
 {

 document.fiscalPeriodForm.buttonValue.value="fiscalperiodReport";
 document.fiscalPeriodForm.submit();
 }
</script>
<%@include file="../includes/resources.jsp" %>
</head>
<body onload="load()">
<html:form action="/fiscalperiod" name="fiscalPeriodForm" type="com.gp.cvst.logisoft.struts.form.FiscalPeriodForm" scope="request" >




<%----%>
<%--<table  border="0" cellpadding="0" cellspacing="0">--%>
<%-- <tr>--%>
<%--    <td height="25"  class="headerbluelarge">Fiscal Calender </td>--%>
<%--   --%>
<%--     <td valign="top" colspan="8" align="center" style="padding-top:10px;"> <html:link page="/jsps/Accounting/SearchFiscalCalender.jsp"><img src="<%=path%>/img/previous.gif" border="0" id="previous"/><%session.removeAttribute("year");%></html:link>--%>
<%--     <img src="<%=path%>/img/save.gif"  border="0" onclick="save()"/>--%>
<%--    <!--  <td ><img src="<%=path%>/img/print.gif" border="0" onclick="print()"/></td></tr>-->--%>
<%--    <font color=red><%=message1%></font></td>--%>
    

<%-- </tr>--%>
<%-- <tr>--%>
<%--     <td width="290">&nbsp;</td>--%>
<%--     <td>&nbsp;</td>--%>
<%-- </tr>--%>

 
 
 
<%-- <table width=100% height="15" border="0" cellpadding="0" cellspacing="0" >--%>
<%--  <tr>--%>
<%--    <td height="15" colspan="8"  class="headerbluesmall">&nbsp;&nbsp;Fiscal Calender </td>--%>
<%--  </tr>--%>
<%--</table>--%>
 

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
<tr class="tableHeadingNew" >Fiscal Calender</tr> 
<td>
  <table width="100%" border="0" cellpadding="0" cellspacing="0"  style="padding-left:2px">
 	<tr>
 	    <td colspan="100">&nbsp;</td>
        <td valign="top" colspan="60" align="center" style="padding-top:10px;">
       
<input type="button" name="search" value="Go Back" class="buttonStyleNew" onclick="window.location.href='<%=path%>/jsps/Accounting/SearchFiscalCalender.jsp';"/>
</td>
       
         <td><font color=red><%=message1%></font></td>
     </tr>
   </table>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew"  style="padding-left:2px;padding-top:10px;padding-bottom:10px;border-left:0px;border-right:0px;">
 	<tr>
 	    <td>
 	        <table width="100%" border="0" cellpadding="3" cellspacing="0"  >
 	         
 	         
 	         <tr>
                  <td class="textlabels"><b>Fiscal Year</b>  </td>
                  <td><html:select property="year" styleClass="textdatestyle" value="<%=year1%>" style="width:120px;">
                      <html:optionsCollection name="yearlists"  styleClass="unfixedtextfiledstyle"  />
                       </html:select></td>
                     <td class="textlabels" ><b>Active Status</b> </td>
                  				<td ><html:radio property="active" value="Open"></html:radio>
                       			 <b>Open</b>
                       			<html:radio property="active"  value="Close"  ></html:radio>
                       			<b>Close</b></td>
                           
               
                  
              </tr>
              <tr>
                        <td ><b>Number Of Fiscal Periods</b></td>
                        <td onchange="fiscalmonth()"><html:select property="fisperiod"  value="<%=fiscalPerid1%>" onchange="fiscalmonth()" style="width:120px;" >
    						<html:optionsCollection name="periodlists" styleClass="unfixedtextfiledstyle" />
    						</html:select></td>
    						<td id="hiddentablesty5"><b>Starting Month</b> </td>
    						<td ><html:select  property="month" value="<%=month1%>" styleId="hiddentablesty" style="width:120px;" >
    						<html:optionsCollection name="monthLists" styleClass="unfixedtextfiledstyle" />
    						</html:select></td>
    				   
    				
              </tr>
              <tr>
                              <td ><b>Adjustment Period Status</b></td>
                                 <td >
    							 <html:radio property="adjperiodrate" value="Open" ></html:radio>
    							 <b>Open</b>
   
   									<html:radio property="adjperiodrate"  value="Close"  ></html:radio>
   									<b>Close</b></td>
   							     <td ><b>Closing Period Status</b> </td>
                             <td>
    							<html:radio property="closperiodstatus" value="Open" ></html:radio>
    							<b>Open</b>
   
   								<html:radio property="closperiodstatus"  value="Close"  ></html:radio>
   								<b>Close</b></td>
              
              </tr>
              
            </table>
           </td>
<%--           <td>--%>
<%--               <table width="100%" border="0" cellpadding="3" cellspacing="0"  >--%>
<%--                   <tr valign="top">--%>
<%--                         --%>
<%--                   --%>
<%--                   </tr>--%>
<%--                   <tr>--%>
<%--                           --%>
<%--                   </tr>--%>
<%--                   <tr valign="down">--%>
<%--                         --%>
<%--                                --%>
<%--                   </tr>--%>
<%--                   --%>
<%--                </table>--%>
<%--           </td>  --%>
                   
      </tr>
      <tr>  
      <td valign="top" colspan="60" align="center" style="padding-top:10px;">
       
<input type="button" name="search" value="Search" class="buttonStyleNew" onclick="go()"/>
</td>
                 
      </tr>
  </table>
<br/><br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
<tr class="tableHeadingNew" >List of values</tr> 
 <td>
<%
if(forcondition3!=null && forcondition3.size()>0) 
 {
   int i=0;
%>
<div id="divtablesty1" class="scrolldisplaytable">
  <display:table name="<%=forcondition3%>" id="divtablesty1" pagesize="<%=pageSize%>" class="displaytagstyle" sort="list"  >
  <display:setProperty name="paging.banner.some_items_found">
    <span class="pagebanner">
     <font color="blue">{0}</font> Fiscal Period details displayed,For more code click on page numbers.
    </span>
  </display:setProperty>
  <display:setProperty name="paging.banner.one_item_found">
    <span class="pagebanner">
       One {0} displayed. Page Number
    </span>
  </display:setProperty>
  <display:setProperty name="paging.banner.all_items_found">
    <span class="pagebanner">
       {0} {1} Displayed, Page Number
    </span>
  </display:setProperty>
  <display:setProperty name="basic.msg.empty_list">
    <span class="pagebanner">
     No Records Found.
    </span>
  </display:setProperty>
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:setProperty name="paging.banner.item_name" value="FiscalPeriod"/>
  <display:setProperty name="paging.banner.items_name" value="FiscalPeriod Details"/>
  <display:column   paramId="paramid"  property="period">  </display:column>
  <display:column    property="staringdate"> </display:column>
  <display:column    property="endingdate"> </display:column>
   <display:column title="Actions">
         <span class="hotspot" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/edit.gif" border="0" alt="" onclick="editfiscal(this,'<%=i%>','windows')"/> </span>
         </display:column>
  
  <%i++;%>
  </display:table>  
  </div>
 
  </td>
  </table>
     
     
        <tr >
        	<td valign="top" colspan="4" align="center" style="padding-top:10px;"><input type="button" name="search" onclick="save()" value="Save" class="buttonStyleNew"/></td>
        
         </tr>
  <%}%>
     
 
  </table>
  
<div  style="height:80%;" >
 
  <table  border="0" cellpadding="0" cellspacing="0"  class="textlabels"  id="hiddentablesty4">
 
  
<tr class="textlabels">
  <td><b>Fiscal Period</b></td>
  <td><b>Start Date</b></td>
  <td><b>End Date</b> </td>
  <td>&nbsp;</td>
</tr>
 
<tr>
  <td>01 </td>
 <td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" />&nbsp;</td>
 &nbsp;<td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal1" value="" /> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal1" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);" />&nbsp;&nbsp;</td>

</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td>02 </td>
 <td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal2" value="" /> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal2" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal3" value="" /> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal3" id="cal3" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td>03 </td>
 <td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal4" value="" /> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal4" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal5" value=""/> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal5" id="cal5" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td >04 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal6" value="" /> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal6" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal7" value=""/> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal7" id="cal7" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>

 <tr><td>&nbsp;</td></tr>
<tr>
  <td>05 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal8" value="" /> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal8" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal9" value=""/> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal9" id="cal9" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>

 <tr><td>&nbsp;</td></tr>
<tr>
  <td width="85">06 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal10" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal10" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal11" value=""/> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal11" id="cal11" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>

 <tr><td>&nbsp;</td></tr>
<tr>
  <td>07 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal12" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal12" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal13" value="" /> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal13" id="cal13" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td >08 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal14" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal14" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal15" value=""/> <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal15" id="cal15" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td >09 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal16" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal16" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal17" value=""/> <img width="17" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal17" id="cal17" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td width="85">10 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal18" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal18" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal19" value=""/> <img width="17" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal19" id="cal19" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td >11 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal20" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal20" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal21" value=""/> <img width="17" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal21" id="cal21" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td >12 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal22" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal22" id="cal22" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal23" value=""/> <img width="17" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal23" id="cal23" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td >13 </td>

<td><html:text property="startingdate" styleClass="textdatestyle" styleId="txtcal24" value=""/> 
   <img width="16" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal24" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
 <td><html:text property="endingdate" styleClass="textdatestyle" styleId="txtcal25" value=""/> <img width="17" height="16" align="top" src="<%=path%>/img/CalendarIco.gif" alt="cal25" id="cal25" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
<td >&nbsp;</td>
</tr>
</table>
</div>
<%
if(saveList2!=null && saveList2.size()>0) 
 {
   int i=1;
   
%>
<div id="divtablesty1" class="scrolldisplaytable">
  <display:table name="<%=saveList2%>"  pagesize="<%=pageSize%>" class="displaytagstyle" sort="list"  >
  <display:setProperty name="paging.banner.some_items_found">
    <span class="pagebanner">
     <font color="blue">{0}</font> Fiscal Period details displayed,For more code click on page numbers.
    </span>
  </display:setProperty>
  <display:setProperty name="paging.banner.one_item_found">
    <span class="pagebanner">
       One {0} displayed. Page Number
    </span>
  </display:setProperty>
  <display:setProperty name="paging.banner.all_items_found">
    <span class="pagebanner">
       {0} {1} Displayed, Page Number
    </span>
  </display:setProperty>
  <display:setProperty name="basic.msg.empty_list">
    <span class="pagebanner">
     No Records Found.
    </span>
  </display:setProperty>
  <display:setProperty name="paging.banner.placement" value="bottom" />
  <display:setProperty name="paging.banner.item_name" value="FiscalPeriod"/>
  <display:setProperty name="paging.banner.items_name" value="FiscalPeriod Details"/>
  <display:column   paramId="paramid"  title="period"> <%=i %> </display:column>
  <display:column    property="staringdate"> </display:column>
  <display:column    property="endingdate"> </display:column>
  <%i++;%>
  </display:table>  
</div>
<%}%>


<html:hidden property="buttonValue"/>
<html:hidden property="id"/>
<html:hidden property="index"/>
</html:form>
</body>
<c:if test="${fileName!=null}">
 <script type="text/javascript">
  GB_show('Income Statement Report','<%=path%>/servlet/PdfServlet?fileName=${fileName}',480,650);
 </script>
</c:if>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>
