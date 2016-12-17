<%-- 
    Document   : dsoReport
    Created on : Dec 22, 2011, 10:31:52 PM
    Author     : logiware
--%>
<html>
    <head>
        <title>AR Reports</title>
        <%@include file="../includes/jspVariables.jsp" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    </head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp"%>
    <script type="text/javascript" src="${path}/dwr/engine.js"></script>
    <script type="text/javascript" src="${path}/dwr/util.js"></script>
    <script type="text/javascript" src="${path}/dwr/interface/FiscalPeriodBC.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
    <script type="text/javascript" src="${path}/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    <body onload="onchangeDso()">
    <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
    <html:form action="/dsoReport" name="dsoReportForm"  styleId="dsoReportForm" 
               type="com.gp.cvst.logisoft.struts.form.DsoReportForm" scope="request">
        <html:hidden property="action" styleId="action"/>
        <html:hidden property="reportType" styleId="reportType"/>
        <div>
            <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew">
                <tr class="textlabelsBold">
                    <td>DSO to be calculated for </td>
                    <td>
                        <html:select property="searchDsoBy" styleId="searchDsoBy" styleClass="dropdown_accounting" 
                                     style="width: 125px" onchange="onchangeDso()">
                            <html:option value="AllAccountsReceivable">All Accounts Receivable</html:option>
                            <html:option value="ByCollector">By Collector</html:option>
                            <html:option value="ByCustomer">By Customer</html:option>
                        </html:select>
                    </td>
                    <td>Date From</td>
                    <td>
                        <html:text property="fromPeriod" styleId="fromPeriod" styleClass="textlabelsBoldForTextBox"/>
                        <html:hidden property="fromPeriodId" styleId="fromPeriodId"/>
                        <input type="hidden" name="fromPeriodValid" id="fromPeriodValid"/>
                        <div id="fromPeriodChoices" style="display: none" class="newAutoComplete"></div>
                    </td>
                    <td>Date To</td>
                    <td>
                        <html:text property="toPeriod" styleId="toPeriod" styleClass="textlabelsBoldForTextBox"/>
                        <html:hidden property="toPeriodId" styleId="toPeriodId"/>
                        <input type="hidden" name="toPeriodValid" id="toPeriodValid"/>
                        <div id="toPeriodChoices" style="display: none" class="newAutoComplete"></div>
                    </td>
                    <td>Number Of Days</td>
                    <td>
                        <html:text property="numberOfDays" styleId="numberOfDays" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Collector Name</td>
                    <td>
                        <html:text property="userName" styleId="userName" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                   disabled="true" style="text-transform:uppercase;"/>
                        <html:hidden property="userId" styleId="userId"/>
                        <input type="hidden" name="userNameValid" id="userNameValid" value="${dsoReportForm.userName}"></input>
                        <div id="userNameDiv" class="newAutoComplete"></div>
                    </td>
                    <td>Customer Name</td>
                    <td> 
                        <html:text property="vendorName" styleId="vendorName" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                   style="text-transform:upperCase" disabled="true"/>
                        <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${dsoReportForm.vendorName}"/>
                        <div id="vendorNameChoices" style="display: none" class="autocomplete"></div>
                    </td>
                    <td>Customer Numbar</td>
                    <td><html:text property="vendorNumber" styleId="vendorNumber" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                               readonly="true" style="text-transform:uppercase;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <input type="button" class="buttonStyleNew" value="Clear" onclick="refresh()">
                        <input type="button" class="buttonStyleNew" value="Print" onclick="gotoAction('printDSO')">
                        <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="gotoAction('exportDSOToExcel')" style="width:100px">
                    </td>
                </tr>
            </table>
        </div>
    </html:form>
    <script type="text/javascript">
        initAutocomplete("vendorName","vendorNameChoices","vendorNumber","vendorNameCheck","${path}/servlet/AutoCompleterServlet?action=Vendor&textFieldId=vendorName&accountType=V","");
    </script>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/dsoReport.js"></script>
    <c:if test="${not empty reportFileName}">
        <script type="text/javascript">
            window.parent.parent.showGreyBox('DSO Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
        </script>
    </c:if>
</body>
</html>
