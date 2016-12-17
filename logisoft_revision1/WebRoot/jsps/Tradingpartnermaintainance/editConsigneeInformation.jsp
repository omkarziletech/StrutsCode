<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.ConsigneeInformation,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.beans.ConsigneeBean,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
ConsigneeInformation consigneeInformation=new ConsigneeInformation();
request.setAttribute("consigneeInformation",consigneeInformation);
consigneeInformation.setGoalAcct("Y");
consigneeInformation.setInsurance("Y");
String buttonValue = "load";
GenericCode genericCode = null;
String countryId="219";
String cityId="0";
String state="";
String commodity="";
String port="";
String tax="";
String maxdays="";
String modify="";
List cities=new ArrayList();
modify = (String) session.getAttribute("modifyforcustomer");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
    
	}
request.setAttribute("commoditylist",dbUtil.getGenericCodeList(4,"no","Select Commodity Code"));
request.setAttribute("portList", dbUtil.getPortList());
request.setAttribute("countrylist",dbUtil.getGenericCodeList(11,"yes","Select Country"));

if(session.getAttribute("consigneeInformation")!=null)
{
		consigneeInformation=(ConsigneeInformation)session.getAttribute("consigneeInformation");
		 genericCode= consigneeInformation.getNotifyCountry(); 
		  
		if(consigneeInformation!=null && consigneeInformation.getNotifyCountry()!=null && consigneeInformation.getNotifyCountry().getId()!=null)
  	 {
   		 countryId=consigneeInformation.getNotifyCountry().getId().toString();
   		
  	 }
 		if(consigneeInformation!=null && consigneeInformation.getNotifyCity()!=null && consigneeInformation.getNotifyCity().getId()!=null)
  	 {
    	 cityId=consigneeInformation.getNotifyCity().getId().toString();
   	}
  	 if(consigneeInformation!=null && consigneeInformation.getNotifyState()!=null )
   	{
   		 state=consigneeInformation.getNotifyState();
  	 }	
		if(consigneeInformation!=null && consigneeInformation.getCommodityId()!=null )
  	 {
   		 commodity=consigneeInformation.getCommodityId().getId().toString();
  	 }	
		if(consigneeInformation!=null && consigneeInformation.getPortId()!=null )
  	 {
   		 port=consigneeInformation.getPortId().toString();
  	 }	
  	if(consigneeInformation!=null && consigneeInformation.getTaxExempt()!=null )
  	 {
  	 		tax=consigneeInformation.getTaxExempt().toString();
  	 }
  	  if(consigneeInformation!=null && consigneeInformation.getMaxDaysBetVisits()!=null )
  	 {
  	 		maxdays=consigneeInformation.getMaxDaysBetVisits().toString();
  	 }
 }
 
if(genericCode!=null && genericCode.getCodedesc()!=null)
{
cities=dbUtil.getCityList(genericCode);
}
request.setAttribute("citylist",cities);

request.setAttribute("rediobuttons",consigneeInformation);
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
   <title>JSP for ConsigneeInformationForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	function selectcity()
    {
   		 if(document.editConsigneeInformationForm.notifyCountry.value=="0")
    {
    	alert("Please enter the country");
   	    return;
    }
    if(IsNumeric(document.editConsigneeInformationForm.taxExempt.value.replace(/ /g,''))==false)
   {
    	alert("Tax Exempt should be Numeric.");
  		document.editConsigneeInformationForm.taxExempt.value="";
    	document.editConsigneeInformationForm.taxExempt.focus();
   	  return;
   } 
   if(IsNumeric(document.editConsigneeInformationForm.maxDaysBetVisits.value.replace(/ /g,''))==false)
   {
    	alert("Max Days Between Visits should be Numeric.");
  		document.editConsigneeInformationForm.maxDaysBetVisits.value="";
    	document.editConsigneeInformationForm.maxDaysBetVisits.focus();
   	  return;
   } 
    	document.editConsigneeInformationForm.buttonValue.value="selectcity";
    	document.editConsigneeInformationForm.submit();
    }
    function selectstate()
    {
    if(document.editConsigneeInformationForm.notifyCity.value=="0")
    {
    	alert("Please enter the city");
    	return;
    }
     if(IsNumeric(document.editConsigneeInformationForm.taxExempt.value.replace(/ /g,''))==false)
   {
    	alert("Tax Exempt should be Numeric.");
  		document.editConsigneeInformationForm.taxExempt.value="";
    	document.editConsigneeInformationForm.taxExempt.focus();
   	  return;
   } 
   if(IsNumeric(document.editConsigneeInformationForm.maxDaysBetVisits.value.replace(/ /g,''))==false)
   {
    	alert("Max Days Between Visits should be Numeric.");
  		document.editConsigneeInformationForm.maxDaysBetVisits.value="";
    	document.editConsigneeInformationForm.maxDaysBetVisits.focus();
   	  return;
   } 
    
     document.editConsigneeInformationForm.buttonValue.value="selectstate";
    document.editConsigneeInformationForm.submit();
    }
 function confirmnote()
	{
		document.editConsigneeInformationForm.buttonValue.value="note";
    	document.editConsigneeInformationForm.submit();
   	}
   	
   	
 function disabled(val1,val2)
   {
  
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id != "cancel" &&  imgs[k].id!="note")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue")
	 		{
	  			input[i].disabled = true;
	  		}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled = true;
	  		
  	 	}
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	
    }
	</script>
	</head>
	<body class="whitebackgrnd"  onLoad="disabled('<%=modify%>')">
		<html:form action="/editConsigneeInformation" name="editConsigneeInformationForm" type="com.gp.cong.logisoft.struts.form.EditConsigneeInformationForm" scope="request">
			<table width="807" border="0" cellpadding="0" cellspacing="0">
<tr class="style2">
  <td align="left" class="headerbluelarge"><bean:message key="form.consigneeInformationForm.generalInfo" /> </td>
  <td width="78" align="left"><img src="<%=path%>/img/note.gif" id="note"
										onclick="confirmnote()" />
								</td>
</tr>
<tr class="style2">
    <td width ="807"align="left" class="headerbluelarge">&nbsp;</td>
  </tr>
  <tr>

  <td></td></tr>
     <tr>
    <td height="12"  class="headerbluesmall">&nbsp;&nbsp;<bean:message key="form.consigneeInformationForm.addressinfo" /> </td> 
  </tr>
</table>

  <table width="806" border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td>
  <table>
  <tr class="style2">
        <td >&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
      <tr class="style2">
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.taxExempt" /></td>
      	<td><html:text property="taxExempt" value="<%=tax%>" maxlength="9"/></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.fedId" /></td>
      	<td><html:text property="fedId" value="<%=consigneeInformation.getFedId()%>" maxlength="9"/></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.commodityId" /></td>
      	<td><html:select property="commodityId" styleClass="selectboxstyle" value="<%=commodity%>">
                   <html:optionsCollection name="commoditylist" />
                	</html:select>
        </td>
      </tr>
     <tr class="style2">
        <td >&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr class="style2">
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.portId" /></td>
      	<td><html:select property="portId" styleClass="selectboxstyle" value="<%=port%>">
                   <html:optionsCollection name="portList" />
                	</html:select>
        </td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.maxDaysBetVisits" /></td>
      	<td><html:text property="maxDaysBetVisits" value="<%=maxdays%>" maxlength="5"/></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.goalAcct" /></td>
      	<td>
        	<table>
        		<tr class="style2">
        			<td><html:radio property="goalAcct" value="Y" name="rediobuttons"></html:radio></td>
       			 <td width="20"><bean:message key="form.consigneeInformationForm.y" /></td>
       			 	<td><html:radio property="goalAcct" value="N" name="rediobuttons"></html:radio></td>
        		 <td width="20"><bean:message key="form.consigneeInformationForm.n" /></td>
       		 </tr>
       		</table>
       	</td>
      </tr>
      <tr class="style2">
        <td >&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
            <tr class="style2">
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.salesPCode" /></td>
      	<td><html:text property="salesPCode" value="<%=consigneeInformation.getSalesPCode()%>" maxlength="9"/></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.notifyCountry" /></td>
      	<td><html:select property="notifyCountry" styleClass="selectboxstyle" onchange="selectcity()" value="<%=countryId%>">
            		<html:optionsCollection name="countrylist" />
                	</html:select></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.notifyCity" /></td>
      	<td><html:select property="notifyCity" styleClass="selectboxstyle" onchange="selectstate()" value="<%=cityId%>">
                   <html:optionsCollection name="citylist" />
                	</html:select></td>
      </tr>
     <tr class="style2">
        <td >&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr class="style2">
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.notifyState" /></td>
      	<td><html:text property="notifyState" value="<%=state%>" readonly="true"/></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.zipCode" /></td>
      	<td><html:text property="zipCode" value="<%=consigneeInformation.getZipCode()%>" maxlength="9"/></td>
      	<td  valign="top"><bean:message key="form.consigneeInformationForm.insurance" /></td>
      	<td>
        	<table>
        		<tr class="style2">
        			<td><html:radio property="insurance" value="Y" name="rediobuttons"></html:radio></td>
       			 <td width="20"><bean:message key="form.consigneeInformationForm.y" /></td>
       			 	<td><html:radio property="insurance" value="N" name="rediobuttons"></html:radio></td>
        		 <td width="20"><bean:message key="form.consigneeInformationForm.n" /></td>
       		 </tr>
       		</table>
       	</td>
      </tr>
      <tr class="style2">
        <td >&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr class="style2">
        <td ><bean:message key="form.consigneeInformationForm.notifyParty" /></td>
        <td><html:textarea property="notifyParty" styleClass="textareastyle" value="<%=consigneeInformation.getNotifyParty()%>"/></td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr class="style2">
        <td >&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
					<td>&nbsp;</td>
        <td >&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
    </table>
  </td>
 </tr>
 <tr>
 			<td>
 						<table width="800">
 										<tr>
 												<td width="800" class="headerbluesmall">&nbsp;&nbsp;<bean:message key="form.consigneeInformationForm.webInventory" /></td>
      							</tr>
 						</table>
 			</td>
 </tr>
 <tr>
 		<td>
	 		<table>
	 					
     				<tr class="style2">
     						
     									<td><bean:message key="form.consigneeInformationForm.userName" /></td>
     									<td><bean:message key="form.consigneeInformationForm.password" /></td>
     									<td><bean:message key="form.consigneeInformationForm.specialRemarks" /></td>
     				</tr>
     				<tr class="style2">
     						
     									<td valign="top"><html:text property="userName" value="<%=consigneeInformation.getUserName()%>" maxlength="50"/></td>
     									<td valign="top"><html:password property="password" value="<%=consigneeInformation.getPassword()%>" maxlength="50"/></td>
     									<td><html:textarea property="specialRemarks" styleClass="textareastyle" value="<%=consigneeInformation.getSpecialRemarks()%>"/></td>
     				</tr>
     				
	 		</table>
 		</td>
 </tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
		
		