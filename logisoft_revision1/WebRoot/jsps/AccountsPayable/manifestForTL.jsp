<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.struts.form.*,com.gp.cvst.logisoft.hibernate.dao.*"%>
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
if(path==null){
 path="../..";
}
String CustomerName="";
String CustomerNo="";
DBUtil dbUtil = new DBUtil();
		CustAddress custAddress = new CustAddress();
		List AccList = dbUtil.getAccountTypeList();
		List blterms = null;
		List checkType = null;
		DBUtil dbutil = new DBUtil();
		blterms = (List) dbutil.getBlTerms();
		checkType = (List) dbutil.checkType();
		request.setAttribute("bltermslist", dbutil.getBlTerms());
		request.setAttribute("checkType", dbutil.checkType());
		String note = "";
		if (request.getAttribute("note") != null) {
			note = (String) request.getAttribute("note");
			if (note.equals("note")){
%>
	<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/admin/Notes.jsp?notes="+"accountsrec","","width=650,height=450");
      mywindow.moveTo(200,180);
</script>
	<%
		}
		}
		if (request.getAttribute("batch") != null) {
			custAddress = (CustAddress) request.getAttribute("batch");
			StringBuffer addressBuffer = new StringBuffer();
			if (custAddress.getAcctName() != null) {
				CustomerName = custAddress.getAcctName();
				CustomerNo = custAddress.getAcctNo();
			}
		} 
		request.setAttribute("accountTypelist", dbUtil.getAccountTypeList());
	%>
<%@include file="../includes/baseResources.jsp" %>

<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
</script>
<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
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
alert("hai");
if(document.manifestForTLForm.custName.value==""){
alert("Pls Enter custName value");
return false;
}
if(document.manifestForTLForm.custNo.value==""){
alert("Pls Enter CustNo value");
return false;
}
if(document.manifestForTLForm.transactionDate.value==""){
alert("Pls Enter transactionDate value");
return false;
}
if((document.manifestForTLForm.blno.value=="")&& (document.manifestForTLForm.invno.value==0)){
alert("Pls Enter blno or invno value");
return false;
}
if(document.manifestForTLForm.transactionAmt.value==0.0){
alert("Pls Enter transactionAmt value");
return false;
}
if(document.manifestForTLForm.glAccountNumber.value==""){
alert("Pls Enter GlAcctNo value");
return false;
}
if(document.manifestForTLForm.glAccountNumber.value!="" && document.manifestForTLForm.transactionType.value.length==2 &&document.manifestForTLForm.custNo.value!="" && document.manifestForTLForm.custName.value!="" && document.manifestForTLForm.transactionAmt.value!=0.0 && document.manifestForTLForm.transactionDate.value!="" && ((document.manifestForTLForm.blno.value!="") ||(document.manifestForTLForm.invno.value!=0))){
document.manifestForTLForm.buttonValue.value="save";
document.manifestForTLForm.submit();
}
else{
alert("Transaction Type must be in 2 Charecterrs Only");
}
}

function searchResults(){
      if(event.keyCode==13 || (event.keyCode==9)){
       go();
      }
}

function go(){
    document.manifestForTLForm.buttonValue.value="getCustomer";
    document.manifestForTLForm.submit();
 }
</script>

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
	<%@include file="../includes/resources.jsp"%>
</head>
<body class="whitebackgrnd">
<html:form action="/manifestForTL" name="manifestForTLForm" type="com.gp.cvst.logisoft.struts.form.ManifestForTLForm"  scope="request">
<table width="100%" border="0" cellpadding="0" cellspacing="0" id="lineItem">
<tr>
<td>
<table width="100%"  border="0" class="tableBorderNew" cellpadding="0" cellspacing="0">
  <tr class="tableHeadingNew"><td>Manifest For TL</td>
  <td colspan="2" align="right">
  <input type="button" class="buttonStyleNew" value="Cancel" style="width:80px" /></td>
  </tr> <tr><td >&nbsp;</td></tr>
  <tr class="textlabels">
  <td width="33%" valign="top">
	<table border="0">
    <tr><td valign="top" height="17">Customer Name </td>
    <td><input type="text" name="vendor" id="vendor" maxlength="30" size="20" value="<%=CustomerName%>" onkeydown="searchResults()"/>
	<input type="button" name='search' class="buttonStyleNew" value='Go' style='width: 23px' onclick="go()"/>
	<dojo:autoComplete formId="manifestForTLForm" textboxId="vendor" action="<%=path%>/actions/getCustomerName.jsp?tabName=MANIFEST_FOR_TL" /></td></tr>
   <tr><td valign="top" width="106" height="27">Transaction Type</td>
	<td valign="top"><html:text property="transactionType" value="AC"/></td></tr>
   <tr><td valign="top">Invoice Date </td>
   <td><html:text property="transactionDate" value="" styleId="txtcal"/><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td></tr>
   <tr><td valign="top" width="114">ChargeCode</td>
    <td valign="top"><html:text property="chargeCode" value="" /></td></tr>
   <tr><td valign="top" width="106">CurrencyCode</td>
	<td valign="top"><html:text property="currencyCode" value="USD" readonly="true"/></td></tr>
   <tr><td valign="top" width="106">JournalEntrynumber</td>
   <td valign="top"><html:text property="journalEntryNumber"  value=""/></td></tr>
   <tr><td valign="top" width="106">SubHouseBL</td>
	<td valign="top"><html:text property="subHouseBl" value=""/></td></tr>
   </table>
   </td>
   <td width="33%" valign="top">
   <table border="0">
    <tr><td valign="top">CustomerNumber</td>
    <td valign="top"><html:text property="custNo" value="<%=CustomerNo%>" /></td></tr>
    <tr><td valign="top" width="106" height="27">Status</td>
	<td valign="top"><html:text property="status" value="Open"/></td></tr>
    <tr><td valign="top" width="138" height="27">BL_NO</td>
    <td valign="top"><html:text property="blno" value="" /></td></tr>
    <tr><td valign="top" width="106">Amount</td>
    <td valign="top"><html:text property="transactionAmt"  value=""/></td></tr>
    <tr><td valign="top" width="106" height="27">Check Number</td>
	<td valign="top"><html:text property="chequeNumber" value=""/></td></tr>
    <tr><td valign="top" width="106">LineItemnumber</td>
	<td valign="top"><html:text property="lineItemNumber" value=""/></td></tr>
    <tr><td valign="top" width="106">VoyageNo</td>
	<td valign="top"><html:text property="voyageNo" value=""/></td></tr>
	<tr><td align="center"><input type="button" class="buttonStyleNew" value="Save" onclick="save()" style="width:80px" /></td></tr>
   </table>
   </td>
   <td width="33%" valign="top">
   <table border="0">
    <tr><td valign="top" width="138" height="27">INV_NO</td>
    <td colspan="2" valign="top"><html:text property="invno" value="" maxlength="12" /></td></tr>
    <tr><td valign="top" width="106">GLAccountNo</td>
	<td colspan="2" valign="top"><html:text property="glAccountNumber" value="" /></td></tr>
    <tr><td valign="top" width="106">SubledgerSourcecode</td>
	<td colspan="2" valign="top"><html:text property="subledgerSourceCode"  value=""/></td></tr>
    <tr><td valign="top" width="106">BLTerms</td>
	<td colspan="2" valign="top"><html:text property="blTerms" value="" /></td></tr>
    <tr><td valign="top" width="106">VesselNo</td>
	<td colspan="2" valign="top"><html:text property="vesselNo" value=""/></td></tr>
    <tr><td valign="top" width="106">ContainerNo</td>
	<td valign="top"><html:text property="containerNo" value=""/></td></tr>
    <tr><td valign="top" width="106">MasterBL</td>
	<td colspan="2" valign="top"><html:text property="m" value="" /></td></tr>
   </table>
   </td>
  </tr>
</table>
<table>
	<tr>
	<td>
	<b>*Transaction Type <font color="red">AR</font>-Accounts Receivable,<font color="red">AP</font>-Accounts Payable,<font color="red">AC</font>-Accrual,<font color="red">RC</font>-Receipts</b>
	</td>
	</tr>
</table>
<html:hidden property="buttonValue" />
</html:form>
</body>
 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
 
