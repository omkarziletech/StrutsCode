<%@page import="com.gp.cong.common.CommonConstants"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
    com.gp.cong.logisoft.util.DBUtil dbutil = new com.gp.cong.logisoft.util.DBUtil();
    request.setAttribute("UserList", dbutil.getCollectors());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html>
    <head>
	<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
	<base href="${basePath}"/>
	<title>Customer Statement </title>
	<%@include file="../includes/baseResources.jsp" %>
	<%@include file="../includes/resources.jsp" %>
	<script type="text/javascript" src="/logisoft/dwr/engine.js"></script>
	<script type="text/javascript" src="/logisoft/dwr/util.js"></script>
	<script type="text/javascript" src="/logisoft/dwr/interface/CustAddressBC.js"></script>
	<script type="text/javascript" src="/logisoft/dwr/interface/ArDwr.js"></script>
	<script type="text/javascript" src="${path}/js/common.js"></script>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
	<script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
	<script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
	<script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    </head>
    <body>
	<div id="cover"></div>
	<div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
	    <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
	    <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
		<img src="/logisoft/img/icons/newprogress_bar.gif"/>
	    </div>
	</div>
	<html:form action="/customerstatement?from=${from}" styleId="customerstatementform"
		   name="customerstatementform" type="com.gp.cvst.logisoft.struts.form.CustomerStatementForm" scope="request" >
	    <html:hidden property="buttonValue"/>
	    <input type="hidden" name="eStatement" value="${from}"/>
	    <c:choose>
		<c:when test="${from=='ArInquiry'}">
		    <c:set var="excludeIds" value="${excludeIds}"/>
		</c:when>
		<c:otherwise>
		    <c:set var="excludeIds" value="${customerstatementform.excludeIds}"/>
		</c:otherwise>
	    </c:choose>
	    <html:hidden property="excludeIds" value="${excludeIds}"/>
	    <c:choose>
		<c:when test="${from=='ArInquiry' && not empty tradingPartner}">
		    <c:set var="customerName" value="${tradingPartner.accountName}"/>
		    <c:set var="customerNo" value="${tradingPartner.accountno}"/>
		    <c:if test="${not empty custAddress}">
			<c:set var="customerAddress" value="${custAddress.address1}"/>
		    </c:if>
		</c:when>
		<c:otherwise>
		    <c:set var="customerName" value="${customerstatementform.customerName}"/>
		    <c:set var="customerNo" value="${customerstatementform.customerNumber}"/>
		    <c:set var="customerAddress" value="${customerstatementform.customerAddress}"/>
		</c:otherwise>
	    </c:choose>
	    <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew"><td> Customer Statement</td>
		    <td align="right" class="textlabelsBold">
			<input type="button" class="buttonStyleNew" value="Print"  onclick="generateStatement()" style="width:60px"/>
			<input type="button" class="buttonStyleNew" value="Export To Excel"  onclick="eStatementToExcel()" style="width:100px"/>
			<input type="button" class="buttonStyleNew" value="Email"  onclick="emailStatement()" style="width:60px"/>
			<input type="button" class="buttonStyleNew" value="Configuration Report"  onclick="configurationReport()" style="width:120px"/>
		    </td>
		</tr>
		<tr>
		    <td colspan="2">
			<table width="100%"  border="0"  cellpadding="0" cellspacing="3">
			    <tr class="textlabelsBold">
				<td align="right">Customer Name</td>
				<td>
				    <input type="text" name="customerName" id="customerName"
					   value="${customerName}" class="textlabelsBoldForTextBox" style="width:130px;text-transform: uppercase;"/>
				    <input name="custname_check" id="custname_check"  type="hidden" value="${customerName}"/>
				    <div id="custname_choices" style="display: none" class="autocomplete"></div>
				</td>
				<td align="right">Customer #</td>
				<td>
				    <input type="text" name="customerNumber" id="customerNumber"
					   value="${customerNo}" class="textlabelsBoldForTextBox" style="width:130px;text-transform: uppercase;"/>
				</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="allCustomers" styleId="allCustomers" onclick="changeFilter()"/>All Customers
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td rowspan="3" valign="top" align="right">Customer Address</td>
				<td rowspan="3">
				    <html:textarea property="customerAddress" styleId="customerAddress" rows="3" cols="20"
						   value="${customerAddress}" styleClass="textlabelsBoldForTextBox" style="width:130px;text-transform:uppercase;"/>
				</td>
				<td align="right">Terminal</td>
				<td>
				    <html:text property="terminal" styleId="terminal" styleClass="textlabelsBoldForTextBox" 
                                               style="width:130px;text-transform:uppercase;"/>
				</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="ecuLineReport" styleId="ecuLineReport"/>Ecu Line Format
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td align="right">Collector</td>
				<td>
				    <html:select property="collector" styleId="collector" styleClass="dropdown_accounting" style="width:133px">
					<html:optionsCollection name="UserList" styleClass="unfixedtextfiledstyle"/>
				    </html:select>
				</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="stmtWithCredit" styleId="stmtWithCredit"/>Statements With Credit
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td align="right">Agents</td>
				<td>
				    <html:select property="agentsInclude" styleId="agentsInclude" styleClass="dropdown_accounting" style="width:133px">
					<html:option value="onlyAgents">Only Agents</html:option>
					<html:option value="agentsInclude">Agents Included</html:option>
					<html:option value="agentsNotInclude">Agents NOT Included</html:option>
				    </html:select>
				</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="includeInvoiceCredit" styleId="includeInvoiceCredit"/>Include Invoice With Credit
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td align="right" valign="top">Message</td>
				<td colspan="3" rowspan="3">
				    <html:textarea property="subject" styleId="subject" rows="3" cols="40"
						   styleClass="textlabelsBoldForTextBox" style="width:360px;text-transform:uppercase;"/>
				</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="printCoverLetter" styleId="printCoverLetter"/>Print Cover Letter
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td>&nbsp;</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="includeAP" styleId="includeAP"/>Include AP
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td>&nbsp;</td>
				<td>
				    <html:checkbox value="true" name="customerStatementForm" property="includeAccruals" styleId="includeAccruals"/>Include Accruals
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td>&nbsp;</td>
				<td colspan="3">&nbsp;</td>
				<td>
				    Include Net Settlement
				    <html:radio property="includeNetSettlement" styleId="includeNetSettlement"  value="yes"/>Yes
				    <html:radio property="includeNetSettlement" styleId="includeNetSettlement" value="no"/>No
				    <html:radio property="includeNetSettlement" styleId="includeNetSettlement" value="only"/>Only
				</td>
			    </tr>
			    <tr class="textlabelsBold">
				<td>&nbsp;</td>
				<td colspan="3">&nbsp;</td>
				<td>
				    Include Prepayments
				    <html:radio property="includePrepayment" styleId="includePrepayment" value="yes"/>Yes
				    <html:radio property="includePrepayment" styleId="includePrepayment" value="no"/>No
				    <html:radio property="includePrepayment" styleId="includePrepayment" value="only"/>Only
				</td>
			    </tr>
			</table>
		    </td>
		</tr>
	    </table>
	</html:form>
    </body>
    <script type="text/javascript">
	dwr.engine.setTextHtmlHandler(dwrSessionError);
	function generateStatement(){
	    if(document.customerstatementform.customerNumber.value=="" 
		&& !document.customerstatementform.allCustomers.checked
		&& document.customerstatementform.collector.value==""){
		alert("Please enter either Customer or select All customers");
		document.customerstatementform.customerName.focus();
		document.getElementById('customerName').style.backgroundColor="yellow";
	    }else{
		document.customerstatementform.buttonValue.value="generateStatement";
		document.customerstatementform.submit();
	    }
	}
	
	function eStatementToExcel(){
	    if(document.customerstatementform.customerNumber.value=="" 
		&& !document.customerstatementform.allCustomers.checked
		&& document.customerstatementform.collector.value==""){
		alert("Please enter either Customer or select All customers");
		document.customerstatementform.customerName.focus();
		document.getElementById('customerName').style.backgroundColor="yellow";
	    }else{
		document.customerstatementform.buttonValue.value="eStatementToExcel";
		document.customerstatementform.submit();
	    }
	}
	
	function emailStatement(){
	    if(document.customerstatementform.customerNumber.value==""
		&& !document.customerstatementform.allCustomers.checked
		&& document.customerstatementform.collector.value==""){
		alert("Please enter either Customer or select All customers");
		document.customerstatementform.customerName.focus();
		document.getElementById('customerName').style.backgroundColor="yellow";
	    }else{
		document.customerstatementform.buttonValue.value="emailStatement";
		document.customerstatementform.submit();
	    }
	}
	
	function configurationReport(){
	    document.customerstatementform.buttonValue.value="configurationReport";
	    document.customerstatementform.submit();
	}

	function getClientInfo(){
	    if(event.keyCode==9 || event.keyCode==13 || event.button==0){
		var custName = dwr.util.getValue("customerName");
		var custNo = dwr.util.getValue("customerNumber");
		CustAddressBC.getClientDetails(custName,custNo,populateClientInfo);
	    }
	}
	
	function populateClientInfo(data) {
	    if(data){
		document.getElementById("customerAddress").value=null!=data.address1?data.address1:"";
	    }
	}
        
	function disableFileds(){
	    document.customerstatementform.customerName.style.backgroundColor = "#DFDFDF";
	    document.customerstatementform.customerNumber.style.backgroundColor = "#DFDFDF";
	    document.customerstatementform.customerAddress.style.backgroundColor = "#DFDFDF";
	    document.customerstatementform.terminal.style.backgroundColor = "#DFDFDF";
	    document.getElementById("customerAddress").readOnly=true;
	    document.getElementById("customerNumber").readOnly=true;
	    document.getElementById("customerName").readOnly=true;
	    document.getElementById("terminal").readOnly=true;
	    document.getElementById("collector").disabled=true;
	    document.customerstatementform.agentsInclude.disabled=true;
	}
	<c:if test="${from=='ArInquiry'}">
	    disableFileds();
	</c:if>
	    function changeFilter(){
		if(document.customerstatementform.allCustomers.checked){
		    document.customerstatementform.stmtWithCredit.checked=false;
		    document.customerstatementform.includeInvoiceCredit.checked=false;
		    document.customerstatementform.includeNetSettlement[1].checked=true;
		    document.customerstatementform.agentsInclude.value="agentsNotInclude";
		}
	    }
	    changeFilter();
	    useLogisoftLodingMessageNew  ();
	    initAutocomplete("customerName","custname_choices","customerNumber","custname_check","${path}/servlet/AutoCompleterServlet?action=Vendor&textFieldId=customerName","getClientInfo()");
    </script>
    <c:if test="${!empty fileName}">
	<script type="text/javascript">
	    var path = "${path}/servlet/FileViewerServlet?fileName=${fileName}";
	    if(window.parent.parent.parent.parent.homeForm){
		window.parent.parent.parent.parent.parent.showGreyBox("Customer Statement",path);
	    }else if(window.parent.parent.parent.parent.arCreditHoldForm){
		window.parent.parent.parent.parent.parent.parent.showGreyBox("Customer Statement",path);
	    }else{
		window.parent.parent.parent.showGreyBox("Customer Statement",path);
	    }
	</script>
    </c:if>
    <c:if test="${not empty statementLocation}">
	<script type="text/javascript">
	    var path = rootPath+"/sendEmail.do?emailOption=ArStatement&moduleName=ArStatement&buttonValue=CreateMailForArStatement&fileLocation=${statementLocation}";
	    if(window.parent.parent.document.homeForm){
		window.parent.parent.GB_showCenter("Email",path,455,650);
	    }else if(window.parent.parent.parent.parent.arCreditHoldForm){
		window.parent.parent.parent.parent.parent.parent.GB_showCenter("Email",path,455,650)
	    }else{
		window.parent.parent.parent.GB_showCenter("Email",path,455,650)
	    }
	</script>
    </c:if>
</html:html>