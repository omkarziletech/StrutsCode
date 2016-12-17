<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String buttonValue="";


if(request.getParameter("buttonValue")!=null){

buttonValue=(String)request.getParameter("buttonValue");
}
%>
<%@include file="../includes/baseResources.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script language="javascript" type="text/javascript">
function printSubmit(val){

if(document.fclBillLaddingform.printContainersOnBL[0].checked){
parent.parent.fclBillLaddingform.printContainersOnBL.value='Yes';
}else{
parent.parent.fclBillLaddingform.printContainersOnBL.value='No';
}
parent.parent.fclBillLaddingform.noOfOriginals.value=document.fclBillLaddingform.noOfOriginals.value;
if(document.fclBillLaddingform.shipperLoadsAndCounts[0].checked){
parent.parent.fclBillLaddingform.shipperLoadsAndCounts.value='Yes';
}else{
parent.parent.fclBillLaddingform.shipperLoadsAndCounts.value='No';
}
if(document.fclBillLaddingform.printPhrase[0].checked){
parent.parent.fclBillLaddingform.printPhrase.value='Yes';
}else{
parent.parent.fclBillLaddingform.printPhrase.value='No';
}
if(document.fclBillLaddingform.agentsForCarrier[0].checked){
parent.parent.fclBillLaddingform.agentsForCarrier.value='Yes';
}else{
parent.parent.fclBillLaddingform.agentsForCarrier.value='No';
}
if(document.fclBillLaddingform.alternatePOL[0].checked){
parent.parent.fclBillLaddingform.alternatePOL.value='Yes';
}else{
parent.parent.fclBillLaddingform.alternatePOL.value='No';
}
if(document.fclBillLaddingform.manifestPrintReport[0].checked){
parent.parent.fclBillLaddingform.manifestPrintReport.value='Yes';
}else{
parent.parent.fclBillLaddingform.manifestPrintReport.value='No';
}
if(document.fclBillLaddingform.houseFreightedBL[0].checked){
parent.parent.fclBillLaddingform.houseFreightedBL.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseFreightedBL.value='No';
}
if(document.fclBillLaddingform.houseFreightedNNBL[0].checked){
parent.parent.fclBillLaddingform.houseFreightedNNBL.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseFreightedNNBL.value='No';
}
if(document.fclBillLaddingform.houseFreightedOriginal[0].checked){
parent.parent.fclBillLaddingform.houseFreightedOriginal.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseFreightedOriginal.value='No';
}
if(document.fclBillLaddingform.houseUnFreightedBL[0].checked){
parent.parent.fclBillLaddingform.houseUnFreightedBL.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseUnFreightedBL.value='No';
}
if(document.fclBillLaddingform.houseUnFreightedNNBL[0].checked){
parent.parent.fclBillLaddingform.houseUnFreightedNNBL.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseUnFreightedNNBL.value='No';
}
if(document.fclBillLaddingform.houseUnFreightedOriginal[0].checked){
parent.parent.fclBillLaddingform.houseUnFreightedOriginal.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseUnFreightedOriginal.value='No';
}
if(document.fclBillLaddingform.steamShipMaster[0].checked){
parent.parent.fclBillLaddingform.steamShipMaster.value='Yes';
}else{
parent.parent.fclBillLaddingform.steamShipMaster.value='No';
}
if(document.fclBillLaddingform.manifest[0].checked){
parent.parent.fclBillLaddingform.manifest.value='Yes';
}else{
parent.parent.fclBillLaddingform.manifest.value='No';
}
if(document.fclBillLaddingform.houseFreightInvoice[0].checked){
parent.parent.fclBillLaddingform.houseFreightInvoice.value='Yes';
}else{
parent.parent.fclBillLaddingform.houseFreightInvoice.value='No';
}
if(document.fclBillLaddingform.confirmOnBoardNotice[0].checked){
parent.parent.fclBillLaddingform.confirmOnBoardNotice.value='Yes';
}else{
parent.parent.fclBillLaddingform.confirmOnBoardNotice.value='No';
}
if(document.fclBillLaddingform.fclArrivalNotice[0].checked){
parent.parent.fclBillLaddingform.fclArrivalNotice.value='Yes';
}else{
parent.parent.fclBillLaddingform.fclArrivalNotice.value='No';
}
if(document.fclBillLaddingform.importArrivalNotice[0].checked){
parent.parent.fclBillLaddingform.importArrivalNotice.value='Yes';
}else{
parent.parent.fclBillLaddingform.importArrivalNotice.value='No';
}
if(document.fclBillLaddingform.printCorrected[0].checked){
parent.parent.fclBillLaddingform.printCorrected.value='Yes';
}else{
parent.parent.fclBillLaddingform.printCorrected.value='No';
}
if(document.fclBillLaddingform.freightInvoice[0].checked){
parent.parent.fclBillLaddingform.freightInvoice.value='Yes';
}else{
parent.parent.fclBillLaddingform.freightInvoice.value='No';
}
if(document.fclBillLaddingform.freightNonNego[0].checked){
parent.parent.fclBillLaddingform.freightNonNego.value='Yes';
}else{
parent.parent.fclBillLaddingform.freightNonNego.value='No';
}
if(document.fclBillLaddingform.unFreightNonNego[0].checked){
parent.parent.fclBillLaddingform.unFreightNonNego.value='Yes';
}else{
parent.parent.fclBillLaddingform.unFreightNonNego.value='No';
}
parent.parent.GB_hide();
alert(val);
if(val=='BlnonNegotiation'){
parent.parent.BlnonNegotiation1();
}else{
parent.parent.BlMainPrint1();
}
}
</script>
<%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd">
<html:form action="/fclBillLadding" styleId="fclbl" name="fclBillLaddingform" type="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm" scope="request">
 <table width=100% border="0"cellpadding="0" cellspacing="0" class="tableBorderNew" >
        <tr><td class="tableHeadingNew" colspan="4">Printing Options</td><td>
        <input type="button" value="Submit" onclick="printSubmit('<%=buttonValue%>')" align="right"/></td>
        </tr>
<%--        <tr class="textlabels">--%>
<%--        <td>Original</td>--%>
<%--        <td><html:radio property="OPrinting" value="Yes"></html:radio> Yes--%>
<%--   	   	        <html:radio property="OPrinting" value="No"></html:radio> No</td>--%>
<%--        </tr>--%>
        <tr class="textlabels">
        <td class="textlabels">No.Of Originals </td>
	   	       <td><html:text property="noOfOriginals" size="13" ></html:text></td>
        <td>Non-Negotiable</td>
        <td><html:radio property="NPrinting" value="Yes"></html:radio> Yes
   	   	         <html:radio property="NPrinting" value="No"></html:radio> No</td>
   	   	         <tr><td>&nbsp;</td></tr>
        </tr>
        <tr class="textlabels">
	   	      <td class="textlabels">Express BL</td>
	   	      <td><html:radio property="BLPrinting" value="Yes"></html:radio> Yes
	   	          <html:radio property="BLPrinting" value="No"></html:radio> No</td>
	   	          
	   	          <td class="textlabels" >Shipper Loads and Counts</td>
   <td><html:radio property="shipperLoadsAndCounts" value="Yes"></html:radio> Yes
	   <html:radio property="shipperLoadsAndCounts" value="No"></html:radio> No</td>
	   
	   	    
	   	    </tr>
	   	    
	   	     <tr class="textlabels">
   <td>Print containers on BL</td>
   <td><html:radio property="printContainersOnBL" value="Yes"></html:radio> Yes
	   <html:radio property="printContainersOnBL" value="No"></html:radio> No</td>
   <td>Print Corrected</td>
   <td><html:radio property="printCorrected" value="Yes" ></html:radio> Yes
	   <html:radio property="printCorrected" value="No" ></html:radio> No</td>
	     </tr>
	   	     <tr class="textlabels">
   <td>Print this phrase if</td>
   <td><html:radio property="printPhrase" value="Yes"></html:radio> Yes
       <html:radio property="printPhrase" value="No"></html:radio> No</td>
   <td >agents for carrier</td>
   <td><html:radio property="agentsForCarrier" value="Yes"></html:radio> Yes
       <html:radio property="agentsForCarrier" value="No"></html:radio> No</td>
   </tr>
   <tr class="textlabels">
   <td>Alternate POL</td>
   <td><html:radio property="alternatePOL" value="Yes"></html:radio> Yes
	   <html:radio property="alternatePOL" value="No"></html:radio> No</td>
   <td >Manifest Print Report</td>
   <td><html:radio property="manifestPrintReport" value="Yes"></html:radio> Yes
	   <html:radio property="manifestPrintReport" value="No"></html:radio> No</td>
	    
	     </tr>
	        	   	         <tr><td>&nbsp;</td></tr>
	   	     <tr class="textlabels">
   <td>House Freighted BL</td>
   <td><html:radio property="houseFreightedBL" value="Yes"></html:radio> Yes
	   <html:radio property="houseFreightedBL" value="No"></html:radio> No</td>
	    <td>House Freighted Original</td>
   <td><html:radio property="houseFreightedOriginal" value="Yes"></html:radio> Yes
	   <html:radio property="houseFreightedOriginal" value="No"></html:radio> No</td>
   </tr>
   <tr class="textlabels"> <td >House Freighted Master BL</td>
   <td><html:radio property="houseFreightedNNBL" value="Yes"></html:radio> Yes
	   <html:radio property="houseFreightedNNBL" value="No"></html:radio> No</td>
	   <td >House Un-Freighted Original</td>
   <td><html:radio property="houseUnFreightedOriginal" value="Yes"></html:radio> Yes
	   <html:radio property="houseUnFreightedOriginal" value="No"></html:radio> No</td>
	   </tr>
   <tr class="textlabels">
   <td >House Un-Freighted BL</td>
   <td><html:radio property="houseUnFreightedBL" value="Yes"></html:radio> Yes
	   <html:radio property="houseUnFreightedBL" value="No"></html:radio> No</td>
	   <td>House Freight Invoice</td>
   <td><html:radio property="houseFreightInvoice" value="Yes"></html:radio> Yes
	   <html:radio property="houseFreightInvoice" value="No"></html:radio> No</td>
	     </tr>
	     
	   	     <tr class="textlabels">
   <td>House Un-Freighted Master BL</td>
   <td><html:radio property="houseUnFreightedNNBL" value="Yes"></html:radio> Yes
	   <html:radio property="houseUnFreightedNNBL" value="No"></html:radio> No</td>
   <td>Freight invoice</td>
   <td><html:radio property="freightInvoice" value="Yes"></html:radio> Yes
	   <html:radio property="freightInvoice" value="No"></html:radio> No</td>
   </tr>
      	   	         <tr><td>&nbsp;</td></tr>
   <tr class="textlabels">
   <td>Freighted Non-Negotiable</td>
   <td><html:radio property="freightNonNego" value="Yes"></html:radio> Yes
	   <html:radio property="freightNonNego" value="No"></html:radio> No</td>
   <td>UnFreighted Non-Negotiable</td>
   <td><html:radio property="unFreightNonNego" value="Yes"></html:radio> Yes
	   <html:radio property="unFreightNonNego" value="No"></html:radio> No</td>
  
	     </tr>
   <tr class="textlabels">
    <td>FCL Arrival Notice</td>
   <td><html:radio property="fclArrivalNotice" value="Yes"></html:radio> Yes
	   <html:radio property="fclArrivalNotice" value="No"></html:radio> No</td>
   <td >Import Arrival Notice</td>
   <td><html:radio property="importArrivalNotice" value="Yes"></html:radio> Yes
	   <html:radio property="importArrivalNotice" value="No"></html:radio> No</td>
   </tr>
   <tr class="textlabels">
   </tr>
    <tr class="textlabels">
     <td>Steam Ship Master</td>
   <td><html:radio property="steamShipMaster" value="Yes"></html:radio> Yes
	   <html:radio property="steamShipMaster" value="No"></html:radio> No</td>
	   <td >Confirm ON Board Notice</td>
   <td><html:radio property="confirmOnBoardNotice" value="Yes"></html:radio> Yes
	   <html:radio property="confirmOnBoardNotice" value="No"></html:radio> No</td>
   </tr>
    <tr class="textlabels">
    <td>Manifest</td>
   <td><html:radio property="manifest" value="Yes"></html:radio> Yes
	   <html:radio property="manifest" value="No"></html:radio> No</td>
	   
   </tr>
</table>
<html:hidden property="toprintCharges"/>
<html:hidden property="buttonValue"/>
  </body>
  </html:form>
</html>
