<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerAddress,com.gp.cvst.logisoft.struts.form.*"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Manifest</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
CustomerAddress customerAddress = new CustomerAddress();
String CustomerName="";
String CustomerNo="";
String orgterm="";
String destTerm="";
if(session.getAttribute("manifestobj") != null)
{
manifestForm manifest = (manifestForm)session.getAttribute("manifestobj");
orgterm =manifest.getOrgTerminal();
}
if(session.getAttribute("manifestForm") != null)
{
manifestForm manifest = (manifestForm)session.getAttribute("manifestForm");
destTerm =manifest.getDestination();
}
DBUtil dbUtil = new DBUtil();
		String note = "";
		if (request.getAttribute("note") != null) {
			note = (String) request.getAttribute("note");
			if (note.equals("note")) {
%>
<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/admin/Notes.jsp?notes="+"accountsrec","","width=650,height=450");
      mywindow.moveTo(200,180);
</script>
	<%
		}
		}
		if (request.getAttribute("batch") != null) {
			customerAddress = (CustomerAddress) request.getAttribute("batch");
			StringBuffer addressBuffer = new StringBuffer();
			if (customerAddress.getAcctname() != null) {
				CustomerName = customerAddress.getAcctname();
				CustomerNo = customerAddress.getAccountNo();
			}
		} 
			request.setAttribute("accountTypelist", dbUtil
				.getAccountTypeList());
	%>
<%
List blterms = null;
DBUtil dbutil = new DBUtil();
blterms = (List) dbutil.getBlTerms();
request.setAttribute("blterms",dbutil.getBlTerms());
 %>
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
<style type="text/css">
<!--
.style15 {color: #FF0000}
.style17 {color: #3366FF}
.style20 {color: #00CC33}
.choices {
    background-color: lavender;
    border-width: 1px 1px 1px 1px;
    width:500px;
}
-->
</style>
</head>
<body class="whitebackgrnd">
<html:form action="/manifest" name="manifest" type="com.gp.cvst.logisoft.struts.form.manifestForm" scope="request">
<br/>
 <table width="100%" border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" > 
   <tr class="tableHeadingNew"><td>Manifest</td>
   <td colspan="5" align="right">
   <input type="button" class="buttonStyleNew" value="Go Back" style="width:70px"/></td></tr> <tr><td >&nbsp;</td></tr>
   <tr><td width="6%" align="left">Custmer Name </td>
   <td><input type="text" name="vendor" id="vendor" maxlength="30" size="20" value="<%=CustomerName%>" onkeydown="searchResults()"/>
       <input type="button" name='search' class="buttonStyleNew" value='Go' style='width: 23px' onclick="getName()"/>
	   <dojo:autoComplete formId="manifest" textboxId="vendor" action="<%=path%>/actions/getCustomerName.jsp?tabName=MANIFEST" /></td>
    <td width="10%" align="right">CustomerNumber</td>
    <td width="23%"><html:text property="custNo" value="<%=CustomerNo%>" /></td>
	<td align="right">MasterBL</td>
	<td><html:text property="m" value=""/></td></tr>    
    <tr><td >Transaction Type</td>
    <td><html:text property="tranType" value="AR" /></td>
    <td align="right">Invoice Date</td>
 	<td><html:text property="transactionDate" value="" styleId="txtcal"  style="width:122px"/><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);"/></td>
	<td align="right">Status</td>
    <td><html:text property="status" value="Open" /></td></tr>
    <tr><td>Bl_No</td>
    <td><html:text property="blno" value="" /></td>
    <td align="right">Inv_No</td>
    <td><html:text property="invno" value="" maxlength="12" /></td>
    <td align="right">GLAccountNo</td>
	<td><html:text property="glAccountNumber" value="" /></td></tr>
    <tr><td >ChargeCode</td>
    <td><html:text property="chargeCode" value="" /></td>
    <td align="right">Amount</td>
    <td><html:text property="transactionAmt"  value=""/></td>
    <td align="right">SubledgerSourcecode</td>
	<td><html:text property="subledgerSourceCode"  value=""/></td></tr>
    <tr><td >CurrencyCode</td>
	<td ><html:text property="currencyCode" value="USD" readonly="true"/></td>
	<td align="right">Check Number</td>
	<td ><html:text property="chequeNumber" value=""/></td>
	<td align="right">BLTerms</td>
	<td><html:select property="blTerms" style="width:145px"><html:optionsCollection name="blterms"/></html:select></td></tr>
	<tr><td >JournalEntrynumber</td>
	<td ><html:text property="journalEntryNumber"  value=""/></td>
	<td align="right">LineItemnumber</td>
	<td ><html:text property="lineItemNumber" value=""/></td>
	<td align="right">VesselNo</td>
	<td ><html:text property="vesselNo" value=""/></td></tr>
    <tr><td >SubHouseBL</td>
	<td ><html:text property="subHouseBl" value=""/></td>
	<td align="right">VoyageNo</td>
	<td ><html:text property="voyageNo" value=""/></td>
	<td align="right">Customer Reference No</td>
	<td ><html:text property="custReference" value=""/></td></tr>
    <tr><td>Origin Terminal</td>
	<td><html:text property="orgTerminal" value="<%=orgterm%>"/>&nbsp;&nbsp;<img src="<%=path%>/img/search1.gif" width="16" height="16" onclick="return popup('<%=path %>/jsps/ratemanagement/searchTerminal.jsp?button='+'manifest&amp;trmnum='+document.manifest.orgTerminal.value,'windows3')"/></td>
	<td align="right">Destination</td>
	<td><html:text property="destination" value="<%=destTerm%>"/>&nbsp;&nbsp;<img src="<%=path%>/img/search1.gif" width="16" height="16" onclick="return popup('<%=path %>/jsps/datareference/SearchPierCode.jsp?button='+'manifest&amp;trmnum='+document.manifest.destination.value,'windows3')"/></td>
	<td align="right">ContainerNo</td>
	<td><html:text property="containerNo" value=""/></td></tr> 
	<tr><td>&nbsp;</td></tr>
	<tr><td colspan="6" align="center"><input type="button" class="buttonStyleNew" value="Save" onclick="save()" style="width:80px" /></td></tr>
  </table>
  <table><tr><td>
  <b>*Transaction Type <font color="red">AR</font>-Accounts Receivable,<font color="red">AP</font>-Accounts Payable,<font color="red">AC</font>-Accrual</b>
  </td></tr></table>
<html:hidden property="buttonValue" />
</html:form>
</body>
  <script type="text/javascript">
function popup1(mylink, windowname){
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
mywindow.moveTo(200,180);
 return false;
}
function save(){
if(document.manifest.vendor.value==""){
alert("Pls Enter custName value");
return false;
}
if(document.manifest.custNo.value==""){
alert("Pls Enter CustNo value");
return false;
}
if(document.manifest.transactionDate.value==""){
alert("Pls Enter transactionDate value");
return false;
}
if((document.manifest.blno.value=="")&& (document.manifest.invno.value==0)){
alert("Pls Enter blno or invno value");
return false;
}
if(document.manifest.transactionAmt.value==0.0){
alert("Pls Enter transactionAmt value");
return false;
}
if(document.manifest.glAccountNumber.value==""){
alert("Pls Enter GlAcctNo value");
return false;
}
if(document.manifest.glAccountNumber.value!="" && document.manifest.custNo.value!="" && document.manifest.vendor.value!="" && document.manifest.tranType.value.length==2 && document.manifest.transactionAmt.value!=0.0 && document.manifest.transactionDate.value!="" && ((document.manifest.blno.value!="") ||(document.manifest.invno.value!=0))){
document.manifest.buttonValue.value="save";
document.manifest.submit();
}
else{
alert(" Transaction Type Must Be in Two Characters Only");
}
}
function popup(mylink, windowname){
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=700,height=350,scrollbars=yes');
mywindow.moveTo(200,180);
return false;
}

function searchResults(){
      if(event.keyCode==13 || (event.keyCode==9)){
       getName();
      }
}

function getName(){
    document.manifest.buttonValue.value="getCustomer";
    document.manifest.submit();
}
</script>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
	<%@include file="../includes/resources.jsp"%>
</html>
