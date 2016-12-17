<%-- 
    Document   : addArBatch
    Created on : May 21, 2011, 8:32:49 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="../includes/jspVariables.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <title>AR Batch</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <style type="text/css">
            .textlabelsBold{
                text-align: right;
            }
            .textlabelsBoldForTextBox,.textlabelsBoldForTextBoxDisabledLook{
                width: 150px;
            }
        </style>
    </head>
    <body>
        <%@include file="../preloader.jsp"%>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <un:useConstants className="com.gp.cvst.logisoft.AccountingConstants" var="accountingConstants"/>
        <html:form action="/arBatch" name="arBatchForm"
                   styleId="arBatchForm" type="com.logiware.form.ArBatchForm" scope="request" method="post" onsubmit="showPreloading()">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="batchId" styleId="batchId"/>
            <html:hidden property="netSettGlAccount" styleId="netSettGlAccount"/>
            <html:checkbox property="searchByUser" styleId="searchByUser" style="display:none"/>
            <html:hidden property="user" styleId="user"/>
            <table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew" style="width:100%">
                    <td colspan="4">
                        <c:choose>
                            <c:when test="${not empty arBatchForm.batchId}">Batch - ${arBatchForm.batchId}</c:when>
                            <c:otherwise>New Batch</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBold">User</td>
                    <td>
                        <html:text property="addBatchUser" styleId="addBatchUser"
                                   styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" style="text-transform:uppercase"/>
                    </td>
                    <td class="textlabelsBold">Deposit Date</td>
                    <td>
                        <html:text property="depositDate" styleId="txtcal1" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                        <img src="${path}/img/CalendarIco.gif" alt="Deposit Date" align="top"
                             id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Is Net Settlement?</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty arBatchForm.batchId}">
                                <html:checkbox property="netsettlement" styleId="netsettlement" value="true" disabled="true"/>
                            </c:when>
                            <c:otherwise>
                                <html:checkbox property="netsettlement" styleId="netsettlement" value="true"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <c:if test="${roleDuty.arBatchDirectGlAccount}">
                        <td class="textlabelsBold">Is Direct GL Account?</td>
                        <td>
                            <html:checkbox property="directGlAccount" styleId="directGlAccount" value="true"/>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td class="textlabelsBold">Batch Amount</td>
                    <td><html:text property="batchAmount" styleId="batchAmount" styleClass="textlabelsBoldForTextBox" maxlength="11"/></td>
                    <td class="textlabelsBold">Batch Balance</td>
                    <td><html:text property="batchBalance" styleId="batchBalance" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Bank Account</td>
                    <td>
                        <c:choose>
                            <c:when test="${arBatchForm.netsettlement}">
                                <html:text property="bankAccount" styleId="bankAccount" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                            </c:when>
                            <c:otherwise>
                                <html:select property="bankAccount" styleId="bankAccount" style="width:155px" styleClass="textlabelsBoldForTextBox">
                                    <html:optionsCollection name="bankAccounts" styleClass="textfieldstyle"/>
                                </html:select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="textlabelsBold">GL Account</td>
                    <td>
                        <html:text property="glAccount" styleId="glAccount" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                        <input type="hidden" name="glAccountValid" id="glAccountValid" value="${arBatchForm.glAccount}"/>
                        <div class="newAutoComplete" id="glAccountChoices"></div>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Bank Account Description</td>
                    <td>
                        <c:choose>
                            <c:when test="${arBatchForm.netsettlement}">
                                <html:textarea property="description" styleId="description" readonly="true"
                                               style="width:155px;text-transform:uppercase;" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                            </c:when>
                            <c:otherwise>
                                <html:textarea property="description" styleId="description" style="width:155px;text-transform:uppercase;" 
                                               styleClass="textlabelsBoldForTextBox"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="textlabelsBold">Notes</td>
                    <td>
                        <html:textarea property="notes" styleId="notes" style="width:155px" styleClass="textlabelsBoldForTextBox"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <c:choose>
                            <c:when test="${not empty arBatchForm.batchId}">
                                <input type="button" class="buttonStyleNew" value="Update Batch" id="createOrUpdateBatch" style="width:90px"/>
                                <input type="button" class="buttonStyleNew"
                                       id="applypayments" value="Update Batch & ApplyPayments" style="width:180px"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="buttonStyleNew" value="Create Batch" id="createOrUpdateBatch" style="width:90px"/>
                                <input type="button" class="buttonStyleNew"
                                       id="applypayments" value="Create Batch & ApplyPayments" style="width:180px"/>
                            </c:otherwise>
                        </c:choose>
                        <input type="button" class="buttonStyleNew" value="Go Back" id="goBack" style="width:90px"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/addArBatch.js"></script>
</html>