<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.ClaimDetails,com.gp.cong.logisoft.beans.SearchCarriersBean,java.text.SimpleDateFormat,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	ClaimDetails claim=new ClaimDetails();
	String masterAwb1="";
	String masterAwb2="";
	String masterAwb3="";
	String flightDate="";
	String edit="";
	if(request.getAttribute("editclaim")!=null)
	{
	edit=(String)request.getAttribute("editclaim");
	if(edit.equals("edit"))
	{
	%>
	<script>
	
	self.close();
	mywindow=window.open("<%=path%>/jsps/datareference/ClaimDetails.jsp","","width=650,height=450");
    mywindow.moveTo(200,180);
	</script>
<%
}
	}
	SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
	SearchCarriersBean searchCarriers=new SearchCarriersBean();
	searchCarriers.setHazardous("off");
	searchCarriers.setClaimed("off");
	if(session.getAttribute("cld")!=null)
	{
	claim=(ClaimDetails)session.getAttribute("cld");
	
	if(claim.getMasterAwbNo()!=null)
		{
		if(claim.getMasterAwbNo().length()>3)
		{
			masterAwb1=claim.getMasterAwbNo().substring(0,3);
		}
		if(claim.getMasterAwbNo().length()>8)
		{
			masterAwb2=claim.getMasterAwbNo().substring(4,8);
		}
		if(claim.getMasterAwbNo().length()>=13)
		{
			masterAwb3=claim.getMasterAwbNo().substring(9,13);
		}
		}
		if(claim.getFlightDate()!=null)
		{
			Date date;
			date=claim.getFlightDate();
			flightDate=date.toString();
		}
		if(claim!=null && claim.getHazardous().equalsIgnoreCase("Y"))
		{
			searchCarriers.setHazardous("on");
		}
		else if(claim!=null && claim.getHazardous().equalsIgnoreCase("N"))
		{
			searchCarriers.setHazardous("off");
		}
		if(claim.getClaim()!=null && claim.getClaim().equals("unclaimed"))
		{
		searchCarriers.setClaimed("off");
		}
		else if(claim.getClaim()!=null && claim.getClaim().equals("claimed"))
		{
		searchCarriers.setClaimed("on");
		}
	}
	
	request.setAttribute("searchCarriersBean",searchCarriers);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
   <title>JSP for ClaimDetailsForm form</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<script type="text/javascript" src="<%=path%>/js/caljs/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
<script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
<script type="text/javascript">
function save()
{
document.editClaimDetailsForm.buttonValue.value="save";
   	document.editClaimDetailsForm.submit();
}
function cancel1()
{

document.editClaimDetailsForm.buttonValue.value="cancel";
   	document.editClaimDetailsForm.submit();
}
</script>
<%@include file="../includes/baseResources.jsp" %>

	</head>
	 <body class="whitebackgrnd">
		<html:form action="/editClaimDetails" scope="request">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td>
   
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="80%" class="headerbluelarge">Enter Claim Details</td>
            <td ><img src="<%=path%>/img/add.gif" border="0" align="top" onclick="save()"/></td>
            <td><img src="<%=path%>/img/cancel.gif" id="cancel" border="0" onClick="cancel1()"/></td>
          </tr>
          </table>
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
       	  <tr>
       	  <td height="5" class="headerbluesmall">&nbsp;&nbsp;Claim Details</td>
       	 
       	  </tr>
        </table>
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr>
       			<td>
       					 <table width="100%" height="6" border="0" cellpadding="0" cellspacing="0">
              
             
              <tr class="style2">
               <td><label class="style2">&nbsp;Master AWB Number</label></td>
               <td><table><tr class="style2">
                <td><html:text property="masterAwb1" readonly="true" size="3" value="<%=masterAwb1%>"/></td>
                 <td><html:text property="masterAwb2" size="4" maxlength="4" value="<%=masterAwb2%>"/></td>
                  <td><html:text property="masterAwb3" size="4" maxlength="4" value="<%=masterAwb3%>"/></td>
                  </tr></table></td>
                 <td><table width="100%" border="0"><tr class="style2"><td>Port Number&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'claim','windows')"></td></tr></table></td>
                  <td><html:text property="portNo" readonly="true" value="<%=claim.getPortNumber()%>"></html:text></td>
                  </tr>
                  <tr class="style2">
                <td><label class="style2">&nbsp;Flight Date</label></td>
                <td><html:text property="txtcal" styleId="txtcal" value="<%=flightDate%>"/><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
               
                <td>Hazardous</td>
                <td><html:checkbox property="hazardous" name="searchCarriersBean"></html:checkbox></td>
              </tr>
          <tr class="style2">
          <td>Claimed</td>
          <td><html:checkbox property="claimed" name="searchCarriersBean"></html:checkbox>
          </tr>
          </table>  
         
       			</td>
       </tr>
       </table>
       </td>
       </tr></table>
       
       
			<html:hidden property="buttonValue" styleId="buttonValue"/>
			
		</html:form>
	</body>
	
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



