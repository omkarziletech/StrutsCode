<%-- 
    Document   : reconcile
    Created on : Mar 28, 2012, 3:14:35 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reconcile</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
            <script type="text/javascript">
            var path = "${path}";
        </script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.fileupload.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/reconcile.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
        <%@include file="../includes/resources.jsp"%>
    </head>
    <body>
        <div class="body-container">
            <%@include file="../preloader.jsp"%>
            <c:set var="accessMode" value="1"/>
            <c:set var="canEdit" value="true" scope="request"/>
            <c:if test="${param.accessMode==0}">
                <c:set var="accessMode" value="0"/>
                <c:set var="canEdit" value="false" scope="request"/>
            </c:if>
            <c:if test="${not empty message}">
                <div class="green bold">${message}</div>
            </c:if>
            <html:form action="/reconcile?accessMode=${accessMode}" name="reconcileForm"
                       styleId="reconcileForm" type="com.logiware.form.ReconcileForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="limit" styleId="limit"/>
                <html:hidden property="selectedPage" styleId="selectedPage"/>
                <html:hidden property="selectedRows" styleId="selectedRows"/>
                <html:hidden property="sortBy" styleId="sortBy"/>
                <html:hidden property="orderBy" styleId="orderBy"/>
                <html:hidden property="importFileName" styleId="importFileName"/>
                <input type="hidden" name="className" id="className"/>
                <input type="hidden" name="methodName" id="methodName"/>
                <table width="100%" cellpadding="0" cellspacing="3" class="tableBorderNew search-filters">
                    <tr class="tableHeadingNew">
                        <td colspan="6">Account Details</td>
                    </tr>
                    <tr class="textLabelsBold">
                        <td>Bank GL Account</td>
                        <td>
                            <html:text property="glAccount" styleId="glAccount" styleClass="textlabelsBoldForTextBox"/>
                            <input type="hidden" name="glAccountCheck" id="glAccountCheck" value="${reconcileForm.glAccount}" class="hidden"/>
                            <html:hidden property="bankName" styleId="bankName"/>
                            <html:hidden property="bankAccount" styleId="bankAccount"/>
                        </td>
                        <td>Bank Reconcile Date</td>
                        <td>
                            <div class="float-left">
                                <html:text property="reconcileDate" styleId="reconcileDate"
                                           styleClass="textlabelsBoldForTextBox" maxlength="10" onchange="validateDate()"/>
                            </div>
                            <div>
                                <img src="${path}/img/CalendarIco.gif" alt="Bank Reconcile Date" align="top" id="reconcileDateCalendar" class="calendar-img"/>
                            </div>
                        </td>
                        <td>Choose Template</td>
                        <td>
                            <input type="file" name="file" id="file" tabindex="-1"/>
                        </td>
                    </tr>
                    <tr class="textLabelsBold">
                        <td>Bank Statement Balance</td>
                        <td>
                            <html:text property="bankBalance" styleId="bankBalance" styleClass="textlabelsBoldForTextBox" maxlength="11"/>
                        </td>
                        <td>GL Balance</td>
                        <td>
                            <html:text property="glBalance" styleId="glBalance" 
                                       styleClass="textlabelsreadOnlyForTextBox" readonly="true" tabindex="-1"/>
                        </td>
                        <td>Last Reconciled Date</td>
                        <td>
                            <html:text property="lastReconciledDate" styleId="lastReconciledDate" 
                                       styleClass="textlabelsreadOnlyForTextBox" readonly="true" tabindex="-1"/>
                        </td>
                    </tr>
                    <tr class="textLabelsBold">
                        <td>Checks Open</td>
                        <td>
                            <html:text property="checksOpen" styleId="checksOpen"
                                       styleClass="textlabelsreadOnlyForTextBox" readonly="true" tabindex="-1"/>
                        </td>
                        <td>Deposits Open</td>
                        <td>
                            <html:text property="depositsOpen" styleId="depositsOpen"
                                       styleClass="textlabelsreadOnlyForTextBox" readonly="true" tabindex="-1"/>
                        </td>
                        <td>Difference</td>
                        <td>
                            <html:text property="difference" styleId="difference"
                                       styleClass="textlabelsreadOnlyForTextBox" readonly="true" tabindex="-1"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" value="Search" class="buttonStyleNew" onclick="search()"/>
                            <input type="button" value="Clear" class="buttonStyleNew" onclick="refresh()"/>
                            <input type="button" value="Reconcile" class="buttonStyleNew" onclick="reconcile()"/>
                            <input type="button" value="Save" class="buttonStyleNew" onclick="save()"/>
                            <input type="button" value="Notes" class="buttonStyleNew" onclick="showNotes()"/>
                            <input type="button" value="Import" class="buttonStyleNew" onclick="importTemplate()"/>
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding="0" cellspacing="3" class="tableBorderNew" style="margin: 20px 0 0 0;">
                    <tr class="tableHeadingNew">
                        <td>List of Transactions</td>
                    </tr>
                    <tr>
                        <td><c:import url="/jsps/GeneralLedger/reconcileResults.jsp"/></td>
                    </tr>
                </table>
            </html:form>
        </div>
    </body>
    <c:if test="${not empty reconcileForm.fileName && (reconcileForm.action=='reconcile' || reconcileForm.action=='reconcileImportTemplate')}">
        <script type="text/javascript">
            downloadFile("${path}/reconcile.do?action=downloadReconcileFile&fileName=${reconcileForm.fileName}");
        </script>
    </c:if>
    <c:if test="${not empty reconcileForm.exceptionFileName && reconcileForm.action=='importTemplate'}">
        <script type="text/javascript">
            downloadFile("${path}/reconcile.do?action=downloadExceptionFile&exceptionFileName=${reconcileForm.exceptionFileName}");
        </script>
    </c:if>
</html>
