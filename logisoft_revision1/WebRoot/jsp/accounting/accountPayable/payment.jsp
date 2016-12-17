<%-- 
    Document   : payment
    Created on : 23 Jun, 2015, 11:25:56 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${param.accessMode eq 0}">
	<c:set var="accessMode" value="0" scope="request"/>
	<c:set var="writeMode" value="false" scope="request"/>
    </c:when>
    <c:otherwise>
	<c:set var="accessMode" value="1" scope="request"/>
	<c:set var="writeMode" value="true" scope="request"/>
    </c:otherwise>
</c:choose>
<html>
    <head>
        <title>AP Payment</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/payment.js"></script>
    </head>
    <body>
        <html:form action="/apPayment?accessMode=${accessMode}" name="apPaymentForm" styleId="apPaymentForm" 
                   type="com.logiware.accounting.form.ApPaymentForm" scope="request" method="post">
            <input type="hidden" name="role" id="role" value="${loginuser.role.roleDesc}"/>
            <input type="hidden" name="achApprover" id="achApprover" value="${loginuser.achApprover}"/>
            <input type="hidden" name="userId" id="userId" value="${loginuser.userId}"/>
            <table class="table">
                <tr>
                    <td>
                        <label class="label">Vendor Name</label>
                        <html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
                        <html:hidden property="vendorNumber" styleId="vendorNumber"/>
                        <input type="hidden" id="vendorNameCheck" value="${apPaymentForm.vendorName}"/>
                        <input type="button" class="button" value="Go" onclick="search();"/>
                        <input type="button" class="button" value="Clear" onclick="clearAll();"/>
                        <c:if test="${not empty apPaymentForm.paymentList && writeMode}">
                            <input type="button" class="button" value="Make Payment" onclick="validatePayment();"/>
                        </c:if>
                        <span class="message align-left italic margin-2-0-0-10">${message}</span>
                        <span class="error align-left italic margin-2-0-0-10">${error}</span>
                    </td>
                </tr>
                <c:if test="${not empty apPaymentForm.bankNameList}">
                    <tr>
                        <th>Bank Details</th>
                    </tr>
                    <tr>
                        <td>
                            <label class="label">Bank Name&nbsp;&nbsp;&nbsp;</label>
                            <html:select property="bankName" styleId="bankName" styleClass="dropdown" style="min-width: 125px;" onchange="setBankAccount()">
                                <c:if test="${fn:length(apPaymentForm.bankNameList) > 1}">
                                    <html:option value="">SELECT BANK NAME</html:option>
                                </c:if>
                                <c:forEach var="bankName" items="${apPaymentForm.bankNameList}">
                                    <html:option value="${bankName}">${bankName}</html:option>
                                </c:forEach>
                            </html:select>&nbsp;&nbsp;&nbsp;&nbsp;
                            <label class="label">Bank Account</label>
                            <html:select property="bankAccount" styleId="bankAccount" styleClass="dropdown" style="min-width: 175px;" onchange="setStartingNumber()">
                                <c:if test="${fn:length(apPaymentForm.bankNameList) > 1 || fn:length(apPaymentForm.bankAccountList) > 1}">
                                    <html:option value="">SELECT BANK ACCOUNT</html:option>
                                </c:if>
                                <c:forEach var="bankAccount" items="${apPaymentForm.bankAccountList}">
                                    <html:option value="${bankAccount.value}">${bankAccount.label}</html:option>
                                </c:forEach>
                            </html:select>&nbsp;&nbsp;&nbsp;&nbsp;
                            <label class="label">Starting Number</label>&nbsp;
                            <html:text property="startingNumber" styleId="startingNumber" styleClass="textbox readonly bold border-none" readonly="true" style="font-size: 1em"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <th>
                        <div class="float-left">Vendor List</div>
                        <c:if test="${not empty apPaymentForm.paymentList && writeMode}">
                            <div class="float-right" style="margin: 0 125px 0 0;">
                                <label>Payment Date</label>
                                <html:text property="paymentDate" styleId="paymentDate" styleClass="textbox"/>
                                <input type="checkbox" class="payAll" onchange="payAll(this)" value="test" style="vertical-align: middle; margin: 0 0 0 20px;"/>
                                <label for="palAll">Pay All</label>
                                <c:if test="${loginuser.achApprover}">
                                    <input type="checkbox" class="approveAll" onchange="approveAll(this)" style="vertical-align: middle; margin: 0 0 0 20px;"/>
                                    <label for="palAll">Approve All</label>
                                </c:if>
                            </div>
                        </c:if>
                    </th>
                </tr>
                <tr>
                    <td>
                        <c:import url="paymentList.jsp"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="action" styleId="action"/>
        </html:form>
    </body>
</html>
