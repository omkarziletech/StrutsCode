<%-- 
    Document   : DPO
    Created on : Aug 17, 2010, 6:29:25 PM
    Author     : Lakshmi Naryanan
--%>
<%@include file="../../includes/jspVariables.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../../includes/baseResources.jsp"%>
        <%@include file="../../includes/resources.jsp"%>
    </head>
    <body onload="onchangeDpo()">
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/apReports" name="apReportsForm" type="com.gp.cvst.logisoft.struts.form.ApReportsForm" scope="request">
            <html:hidden property="action"/>
            <html:hidden property="reportType"/>
            <div>
                <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <td>DPO to be calculated for </td>
                        <td>
                            <html:select property="searchDpoBy" styleId="searchDpoBy" styleClass="dropdown_accounting" 
                                         style="width: 125px" onchange="onchangeDpo()">
                                <html:option value="AllAccountsPayable">All Accounts Payable</html:option>
                                <html:option value="ByUser">By User</html:option>
                                <html:option value="ByVendor">By Vendor</html:option>
                            </html:select>
                        </td>
                        <td>From Period</td>
                        <td> <input type="text" name="fromPeriod" id="fromPeriod" class="textlabelsBoldForTextBox" 
                                    style="width:100px" value="${apReportsForm.fromPeriod}"/>
                            <html:hidden property="fromPeriodId" styleId="fromPeriodId" value="${apReportsForm.fromPeriodId}"/>
                            <input type="hidden" name="fromPeriodValid" id="fromPeriodValid" />
                            <div id="fromPeriodChoices" style="display: none" class="newAutoComplete"></div>
                        </td>
                        <td>To Period</td>
                        <td>
                            <input type="text" name="toPeriod" id="toPeriod" class="textlabelsBoldForTextBox" 
                                   style="width:100px" value="${apReportsForm.toPeriod}"/>
                            <html:hidden property="toPeriodId" styleId="toPeriodId" value="${apReportsForm.toPeriodId}"/>
                            <input type="hidden" name="toPeriodValid" id="toPeriodValid"/>
                            <div id="toPeriodChoices" style="display: none" class="newAutoComplete"></div>
                        </td>
                        <td>Number Of Days</td>
                        <td>
                            <html:text property="numberOfDays" styleId="numberOfDays" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>User Name</td>
                        <td>
                            <html:text property="userName" styleId="userName" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                       disabled="true" style="text-transform:uppercase;"/>
                            <html:hidden property="userId" styleId="userId"></html:hidden>
                            <input type="hidden" name="userNameValid" id="userNameValid" value="${apReportsForm.userName}"></input>
                            <div id="userNameDiv" class="newAutoComplete"></div>
                        </td>
                        <td>Vendor Name</td>
                        <td>
                            <html:text property="vendorName" styleId="vendorName" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                       style="text-transform:upperCase" disabled="true"/>
                            <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apReportsForm.vendorName}"/>
                            <div id="vendorNameChoices" style="display: none" class="autocomplete"></div>
                        </td>
                        <td>Vendor Number</td>
                        <td><html:text property="vendorNumber" styleId="vendorNumber" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                   readonly="true" style="text-transform:uppercase;"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showDPO')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printDPO')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportDPOToExcel')" style="width:100px">
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript">
            initAutocomplete("vendorName","vendorNameChoices","vendorNumber","vendorNameCheck","${path}/servlet/AutoCompleterServlet?action=Vendor&textFieldId=vendorName&accountType=V","");
        </script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
            window.parent.parent.showGreyBox('DPO Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>

