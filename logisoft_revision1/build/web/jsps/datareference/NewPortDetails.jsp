<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.Ports"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
Ports portobj=new Ports();
String path = request.getContextPath();
if(session.getAttribute("portobj")!=null)
{
portobj=(Ports)session.getAttribute("portobj");
}
%>
<html> 
	<head>
		<title>Port Details</title>
		<%@include file="../includes/baseResources.jsp" %>

		
<script type="text/javascript">
function save()
{
	var netie=navigator.appName;
	if(netie=="Microsoft Internet Explorer")
   	{
	if(parent.mainFrame.document.portsFrame.portDetails.portName.value=="")
   	{
   	alert("Please enter the Port Name");
   	return;
   	}
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.ftfFee.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.ftfFee.value)==false)
   	{
   	alert("FtpFee should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.defaultDomesticRoutingInstructions.value!="")
   	{
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.defaultDomesticRoutingInstructions.value.length>200)
   	{
   	alert("DefaultDomesticRoutingInstructions should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.lclSplRemarksinEnglish.value!="")
   	{
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.lclSplRemarksinEnglish.value.length>400)
   	{
   	alert("LclSplRemarksinEnglish should be only 400 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.lclSplRemarksinSpanish.value!="")
   	{
   	if(parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm.lclSplRemarksinSpanish.value.length>400)
   	{
   	alert("LclSplRemarksinSpanish should be only 400 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.clauseDescription.value!="")
   	{
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.clauseDescription.value.length>200)
   	{
   	alert("clauseDescription should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.specialRemarksforQuotation.value!="")
   	{
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.specialRemarksforQuotation.value.length>320)
   	{
   	alert("specialRemarksforQuotation should be only 320 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteByAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteByAgentAdmin.value)==false)
   	{
   	alert("Amount1 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteByAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteByAgentAdmin.value)==false)
   	{
   	alert("TierAmount1 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteByAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteByAgentCommn.value)==false)
   	{
   	alert("Amount2 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteByAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteByAgentCommn.value)==false)
   	{
   	alert("TierAmount2 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteNotAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteNotAgentAdmin.value)==false)
   	{
   	alert("Amount3 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteNotAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteNotAgentAdmin.value)==false)
   	{
   	alert("TierAmount3 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteNotAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountRouteNotAgentCommn.value)==false)
   	{
   	alert("Amount4 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteNotAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.tierAmountRouteNotAgentCommn.value)==false)
   	{
   	alert("TierAmount4 should be Numeric");
   	return;
   	}
   	}
   
   /*	if(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountCurrentAdjFactor.value!="")
   	{
   	if(isInteger(parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm.amountCurrentAdjFactor.value)==false)
   	{
   	alert("CurrentAdj Factor  should be Numeric");
   	return;
   	}
   	}*/
   	
   	if(parent.mainFrame.document.airPortsFrame.AirPortConfigForm.airPortSplRemarksinEnglish.value!="")
   	{
   	if(parent.mainFrame.document.airPortsFrame.AirPortConfigForm.airPortSplRemarksinEnglish.value.length>200)
   	{
   	alert("airPortSplRemarksinEnglish should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.airPortsFrame.AirPortConfigForm.airPortSplRemarksinSpanish.value!="")
   	{
   	if(parent.mainFrame.document.airPortsFrame.AirPortConfigForm.airPortSplRemarksinSpanish.value.length>200)
   	{
   	alert("airPortSplRemarksinSpanish should be only 200 characters");
   	return;
   	}
   	}
   	}
   	else if(netie=="Netscape")
   	{
   	if(parent.mainFrame.document.getElementById('portsFrame').contentDocument.portDetails.portName.value=="")
   	{
   	alert("Please enter the Port Name");
   	return;
   	
   	}
   
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.ftfFee.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.ftfFee.value)==false)
   	{
   	alert("FtpFee should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.defaultDomesticRoutingInstructions.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.defaultDomesticRoutingInstructions.value.length>200)
   	{
   	alert("DefaultDomesticRoutingInstructions should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.lclSplRemarksinEnglish.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.lclSplRemarksinEnglish.value.length>200)
   	{
   	alert("LclSplRemarksinEnglish should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.lclSplRemarksinSpanish.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm.lclSplRemarksinSpanish.value.length>200)
   	{
   	alert("LclSplRemarksinSpanish should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.clauseDescription.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.clauseDescription.value.length>200)
   	{
   	alert("clauseDescription should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.specialRemarksforQuotation.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.specialRemarksforQuotation.value.length>200)
   	{
   	alert("specialRemarksforQuotation should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteByAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteByAgentAdmin.value)==false)
   	{
   	alert("Amount1 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteByAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteByAgentAdmin.value)==false)
   	{
   	alert("TierAmount1 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteByAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteByAgentCommn.value)==false)
   	{
   	alert("Amount2 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteByAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteByAgentCommn.value)==false)
   	{
   	alert("TierAmount2 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteNotAgentAdmin.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteNotAgentAdmin.value)==false)
   	{
   	alert("Amount3 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteNotAgentAdmin.value!="")
   	{
            if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteNotAgentAdmin.value)==false)
            {
                alert("TierAmount3 should be Numeric");
                return;
            }
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteNotAgentCommn.value!="")
   	{
            if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountRouteNotAgentCommn.value)==false)
            {
                alert("Amount4 should be Numeric");
                return;
            }
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteNotAgentCommn.value!="")
   	{
   	if(isFloat(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.tierAmountRouteNotAgentCommn.value)==false)
   	{
   	alert("TierAmount4 should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountCurrentAdjFactor.value!="")
   	{
   	if(isInteger(parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm.amountCurrentAdjFactor.value)==false)
   	{
   	alert("CurrentAdj Factor  should be Numeric");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('airPortsFrame').contentDocument.AirPortConfigForm.airPortSplRemarksinEnglish.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('airPortsFrame').contentDocument.AirPortConfigForm.airPortSplRemarksinEnglish.value.length>200)
   	{
   	alert("airPortSplRemarksinEnglish should be only 200 characters");
   	return;
   	}
   	}
   	if(parent.mainFrame.document.getElementById('airPortsFrame').contentDocument.AirPortConfigForm.airPortSplRemarksinSpanish.value!="")
   	{
   	if(parent.mainFrame.document.getElementById('airPortsFrame').contentDocument.AirPortConfigForm.airPortSplRemarksinSpanish.value.length>200)
   	{
   	alert("airPortSplRemarksinSpanish should be only 200 characters");
   	return;
   	}
   	}
   	}

	if(netie=="Microsoft Internet Explorer")
   	{
 		var portframeobj=parent.mainFrame.document.portsFrame.portDetails;
 		var lclframeobj=parent.mainFrame.document.lclPortsFrame.LclPortsConfigForm;
 		var fclframeobj=parent.mainFrame.document.fclPortsFrame.FclPortsConfigForm;
 		var airframeobj=parent.mainFrame.document.airPortsFrame.AirPortConfigForm;
 		var impframeobj=parent.mainFrame.document.importPortsFrame.importsForm;
 		portframeobj.submit();
 		lclframeobj.submit();
 		fclframeobj.submit();
 		airframeobj.submit();
 		impframeobj.submit();
 	}
 	else if(netie=="Netscape")
   	{	
   			var portframeobj=parent.mainFrame.document.getElementById('portsFrame').contentDocument.portDetails;
 			var lclframeobj=parent.mainFrame.document.getElementById('lclPortsFrame').contentDocument.LclPortsConfigForm;
 			
 			var fclframeobj=parent.mainFrame.document.getElementById('fclPortsFrame').contentDocument.FclPortsConfigForm;
 			var airframeobj=parent.mainFrame.document.getElementById('airPortsFrame').contentDocument.AirPortConfigForm;
 			var impframeobj=parent.mainFrame.document.getElementById('importPortsFrame').contentDocument.importsForm;
 			portframeobj.submit();
 			lclframeobj.submit();
 			fclframeobj.submit();
 			airframeobj.submit();
 			impframeobj.submit();
 	}
 				document.newPortDetailsForm.buttonValue.value="save";
 				document.newPortDetailsForm.submit();
}
 	function cancel()
	{
		
		var result = confirm("Do you want to save the changes?");
		
		if(result){
		 save();
		}
		else{
			
			document.newPortDetailsForm.buttonValue.value="cancel";
			document.newPortDetailsForm.submit();
		}
   		
 	}
</script>
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/newPortDetails" scope="request">
		<table width="750" cellpadding="0" cellspacing="0">
	<tr class="textlabels">
		
		<td>Schedule Code </td>
    	<td><html:text property="portCode" value="<%=portobj.getShedulenumber() %>" styleClass="textfieldstyle" readonly="true"/></td>
    	<td>Control Number</td>
    	 <td ><html:text property="controlNo" styleClass="textfieldstyle" value="<%=portobj.getControlNo()%>"  readonly="true"/></td> 	
		<td width="82"><input type="button" class="buttonStyleNew"  value="Save" onClick="save()"/></td>
		<td width="137"><input type="button" class="buttonStyleNew" id="search" value="Go Back" onclick="cancel()"/></td>
	</tr>
</table>
		<html:hidden property="buttonValue"/>	
		</html:form>
	</body>
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

