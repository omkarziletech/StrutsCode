<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.beans.SearchCarriersBean,java.util.*,com.gp.cong.logisoft.domain.ClaimDetails"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	SearchCarriersBean searchCarriersBean=new SearchCarriersBean();
	
	if(request.getAttribute("edit")!=null)
	{
	if(request.getAttribute("edit").equals("edit"))
	{
	%>
	<script>
	self.close();
	mywindow=window.open("<%=path%>/jsps/datareference/EditClaimDetails.jsp","","width=650,height=450");
    mywindow.moveTo(200,180);
	</script>
<%
}}
	searchCarriersBean.setHazardous("off");
	if(session.getAttribute("searchCarriers")!=null)
	{
	searchCarriersBean=(SearchCarriersBean)session.getAttribute("searchCarriers");
	searchCarriersBean.setHazardous(searchCarriersBean.getHazardous());
	}
	request.setAttribute("searchCarriersBean",searchCarriersBean);
	String airlinecode="";
	
	if(session.getAttribute("aircode")!=null)
	{
	airlinecode=(String)session.getAttribute("aircode");
	}
	List claimdetailslist=new ArrayList();
	if(session.getAttribute("claimdetailslist")!=null)
	{
	claimdetailslist=(List)session.getAttribute("claimdetailslist");
	}
	ClaimDetails claim=new ClaimDetails();
	String portNo="";
	if(session.getAttribute("claimDetails")!=null)
	{
	claim=(ClaimDetails)session.getAttribute("claimDetails");
	if(claim.getPortNumber()!=null)
	{
	portNo=claim.getPortNumber();
	}
	}
	String editPath=path+"/claimDetails.do";
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
	
	<script type="text/javascript" type="text/javascript">
	function save()
	{
	document.claimDetailsForm.buttonValue.value="save";
   	document.claimDetailsForm.submit();
	}
	function popup1(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
	document.claimDetailsForm.submit();

return false;
}
var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/datareference/EditClaimDetails.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/datareference/EditClaimDetails.jsp","","width=600,height=300");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 

	</script>
<%@include file="../includes/baseResources.jsp" %>

	</head>  
  
  <body class="whitebackgrnd">
		<html:form action="/claimDetails" scope="request">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td>
   
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="80%" class="headerbluelarge">Enter Claim Details</td>
            <td ><img src="<%=path%>/img/add.gif" border="0" align="top" onclick="save()"/></td>
            <td><img src="<%=path%>/img/cancel.gif" id="cancel" border="0" onClick="javascript:window.close();"/></td>
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
                <td><html:text property="masterAwb1" value="<%=airlinecode%>" readonly="true" size="3"/></td>
                 <td><html:text property="masterAwb2" size="4" maxlength="4" value="<%=searchCarriersBean.getMasterAwb2()%>"/></td>
                  <td><html:text property="masterAwb3" size="4" maxlength="4" value="<%=searchCarriersBean.getMasterAwb3() %>"/></td>
                  </tr></table></td>
                 <td><table width="100%" border="0"><tr class="style2"><td>Port Number&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'claim','windows')"></td></tr></table></td>
                  <td><html:text property="portNo" value="<%=portNo%>" readonly="true"></html:text></td>
                  </tr>
                  <tr class="style2">
                <td><label class="style2">&nbsp;Flight Date</label></td>
                <td><html:text property="txtcal" styleId="txtcal" value="<%=searchCarriersBean.getTxtcal()%>"/><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
               
                <td>Hazardous</td>
                <td><html:checkbox property="hazardous" name="searchCarriersBean"></html:checkbox></td>
              </tr>
             <tr>
            <td><table width="100%" border="0"><tr class="style2"><td>ECI Booking Numbers&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'claim','windows')"></td></tr></table></td>
             </tr>
          </table>  
          <table width="100%">
          <% 
           int i=0; %>
          <tr>
       <td>
             <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
         
          <tr>
        <td height="5" width="100%" class="headerbluesmall">&nbsp;&nbsp;List of Claim Details</td>
          </tr>
    
         <display:table name="<%=claimdetailslist%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="claimtable" style="width:100%">
         <display:setProperty name="basic.msg.empty_list"><span class="pagebanner"></display:setProperty> 
         <display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="ClaimDetails"/>
  			<display:setProperty name="paging.banner.items_name" value="ClaimDetailss"/>
  			<%
  			String awbNo="";
		if(claimdetailslist!=null && claimdetailslist.size()>0)
		{
			ClaimDetails c1=(ClaimDetails)claimdetailslist.get(i);
			awbNo=c1.getMasterAwbNo();
			}
			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?ind="+iStr;
  			%>
	     <display:column title="Master AWB Number" ><a href="<%=tempPath%>"><%=awbNo%></a></display:column>
		 <display:column property="portNumber" title="Port Number" />
		  <display:column property="flightDate" title="Flight Date"></display:column>
		  <display:column property="hazardous" title="Hazardous"></display:column>
	    <display:column property="claim" title="claimed"></display:column>
	    <display:column property="claimDate" title="ClaimDate"></display:column>
	     <% i++;%>
		</display:table>
        </table></div>   
       </td></tr> 
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


