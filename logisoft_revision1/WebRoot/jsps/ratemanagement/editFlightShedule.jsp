<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.StandardCharges,com.gp.cong.logisoft.domain.AirFreightFlightShedules"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<jsp:useBean id="editFlightBean" class="com.gp.cong.logisoft.struts.ratemangement.form.EditFlightSheduleForm" scope="request"/>  

<%
String path = request.getContextPath();
//String[] args2 = {"376",  "377"};
//editFlightBean.setDay(args2);

DBUtil dbUtil=new DBUtil();
String flight="";
String stops="";
String aircraftType="";
StandardCharges standardCharges= new StandardCharges();
String departureHrs="";
String departureMins="";
AirFreightFlightShedules editFlightSheduleList=new AirFreightFlightShedules();
String terminalNumber = "";
String terminalName = "";
String day="";
String departure="";
String cutOffTime="";
String cutOffTimeHrs="";
String cutOffTimeMins="";
List minutes=dbUtil.getMinutes();
List hours=dbUtil.getHours();
request.setAttribute("minutes",minutes);
request.setAttribute("hours",hours);


request.setAttribute("daysList",dbUtil.getWeekListForFlightShedule(new Integer(25),"yes","Select Days")); 
if(session.getAttribute("standardCharges")!=null )
{
    standardCharges=(StandardCharges)session.getAttribute("standardCharges");
	if(standardCharges.getScheduleTerminal()!=null)
	{
		terminalNumber=standardCharges.getScheduleTerminal().getCode();
		terminalName=standardCharges.getScheduleTerminal().getCodedesc();
		
	}
}
if(session.getAttribute("airFrieght") != null)
{
	editFlightSheduleList = (AirFreightFlightShedules) session.getAttribute("airFrieght");
	day=editFlightSheduleList.getDay();
	editFlightBean.setDay(dbUtil.dayList(day));
	
	flight=editFlightSheduleList.getFlight();
	stops=editFlightSheduleList.getStops();
	departure=editFlightSheduleList.getDeparture();
			 boolean flag=false;
			 for(int i=0;i<departure.length();i++)
			 {
				 if(departure.charAt(i)!=':')
				 {
					 if(!flag)
					 {
						 departureHrs=departureHrs+departure.charAt(i); 
					 }
					 else if(flag)
					 {
						 departureMins=departureMins+departure.charAt(i);
					 }
				 }
				 else if(departure.charAt(i)==':')
				 {
					 flag=true;
				 }
			 }
	cutOffTime=editFlightSheduleList.getCutOffTime();
	
	 boolean flag1=false;
			 for(int i=0;i<cutOffTime.length();i++)
			 {
				 if(cutOffTime.charAt(i)!=':')
				 {
					 if(!flag1)
					 {
						 cutOffTimeHrs=cutOffTimeHrs+cutOffTime.charAt(i); 
					 }
					 else if(flag1)
					 {
						 cutOffTimeMins=cutOffTimeMins+cutOffTime.charAt(i);
					 }
				 }
				 else if(cutOffTime.charAt(i)==':')
				 {
					 flag1=true;
				 }
			 }
			 aircraftType=editFlightSheduleList.getAircraftType();
}


String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}			
String editPath=path+"/flightShedule.do";
%>
 
<html> 
	<head>
		<title>JSP for EditFlightSheduleForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
	   
   		function popup1(mylink, windowname)
		{
			
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
			mywindow.moveTo(200,180);
			return false;
		}
		
		function save()
		{
			document.editFlightSheduleForm.buttonValue.value="add";
  			document.editFlightSheduleForm.submit();
		}
		function confirmdelete()
		{
			document.editFlightSheduleForm.buttonValue.value="delete";
  			document.editFlightSheduleForm.submit();
		}	
		function cancelbtn()
		{
			document.editFlightSheduleForm.buttonValue.value="cancel";
  			document.editFlightSheduleForm.submit();
		}
	function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   			if(imgs[k].id!="cancel")
   			{
   		    	imgs[k].style.visibility = 'hidden';
   		    }
   		 
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			input[i].readOnly=true;
	  			
	  		
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
  	 	for(i=0; i<textarea.length; i++)
	 	{
	 			textarea[i].readOnly=true;
	  			
	 					
	  	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
  	    </script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/editFlightShedule" scope="request">
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<td>
	
	
	
		<table width=100%  border="0" cellpadding="0" cellspacing="0" >
			<tr>
    			<td><table width="100%" border="0"><tr class="style2"><td>Shedule Terminal No&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'sheduleTerminal','windows')"></td></tr></table></td>
    			<td width="108" height="30" ><html:text property="terminalNumber" value="<%=terminalNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td>&nbsp;</td>
    			<td><span class="textlabels">&nbsp;Shedule Terminal Name</span></td>
    			<td width="96" ><html:text property="terminalName" value="<%=terminalName%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td align="right">
					<input type="button" value="Add" class="buttonStyleNew" onclick="save()"/>
				</td>
				<td align="right">
					<input type="button" value="Delete" class="buttonStyleNew" id="delete"	onclick="confirmdelete()" />
				</td>
				<td align="right">
				    <input type="button" value="Go Back" class="buttonStyleNew" id="cancel"	onclick="cancelbtn()"/>
				</td>
    		</tr>
    	</table>	
		<table width="811" border="0" cellpadding="0" cellspacing="0" >
		<tr>
 	    	<td height="12" colspan="10" >&nbsp;&nbsp;</td> 
  		</tr>
		</table>
		<table width="100%" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
		<tr class="tableHeadingNew">Flight Schedule</tr>
		
		<tr class="textlabels">
  			<td height="15">Day</td>
  			<td width="89">Flight</td>
  			<td>Stops </td>
  			
  			<td colspan="4"><div align="center">Departure </div></td>
  			
  			<td colspan="4"><div align="center">CutOff Time</div></td>
  			
  			<td width="130">Aircraft Type </td>
  			
  			<td>&nbsp;</td>
  			<td>&nbsp;</td>
		</tr>
		<tr class="textlabels" height="50" align="top">
			<td  width="52" >
			<html:select   property="day" name="editFlightBean" styleClass="verysmalldropdownStyle" multiple="true" >
           		<html:optionsCollection name="daysList" />	
           	</html:select></td>
			<td><html:text property="flight" value="<%=flight%>" size="5"/></td>
			<td width="52"><html:text property="stops" value="<%=stops%>" size="5"/></td>
			<td>
			 <html:select property="flightHours" value="<%=departureHrs%>">
                 <html:optionsCollection name="hours"/> 
             </html:select>
            </td>
            <td width="22">Hrs</td>
            <td>
              <html:select property="flightMinutes" value="<%=departureMins%>">
                <html:optionsCollection name="minutes"/> 
              </html:select>
            </td> 
            <td >Mins</td>
            <td>
			 <html:select property="cutOffFlightHours" value="<%=cutOffTimeHrs%>">
                 <html:optionsCollection name="hours"/> 
             </html:select>
            </td>
            <td width="22">Hrs</td>
            <td>
              <html:select property="cutOffFlightMinutes" value="<%=cutOffTimeMins%>">
                <html:optionsCollection name="minutes"/> 
              </html:select>
            </td> 
            <td >Mins</td>
			
			<td width="30"><html:text property="airCraftType" value="<%=aircraftType%>"/></td>
			
			
			<td width="13">&nbsp;</td>
		 </tr>
		 <tr class="textlabels">
			  <td height="15">&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td height="15">&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td colspan="2">&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			</tr>
		</table>
		
		</td>
		</table>
		<html:hidden property="buttonValue" styleId="buttonValue"/>	
        <html:hidden property="index" />	
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

