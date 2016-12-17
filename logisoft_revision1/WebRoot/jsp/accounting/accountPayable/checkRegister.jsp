<%-- 
    Document   : checkRegister
    Created on : 8 Jul, 2015, 5:12:22 PM
    Author     : Lucky
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
        <title>Check Register</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/checkRegister_new.js"></script>
    </head>
    <body>
        <html:form action="/checkRegister?accessMode=${accessMode}" name="checkRegisterForm" styleId="checkRegisterForm" 
                   type="com.logiware.accounting.form.CheckRegisterForm" scope="request" method="post">
            <table class="table">
                <tr>
                    <th colspan="4">Check Register</th>
                </tr>
                <tr>
                    <td class="label">GL Account</td>
                    <td>
                        <html:text property="glAccount" styleId="glAccount" styleClass="textbox"/>
                        <html:hidden property="bankAccount" styleId="bankAccount"/>
                        <input name="glAccountCheck" id="glAccountCheck"  type="hidden" value="${checkRegisterForm.glAccount}"/>
                    </td>
                    <td class="label">Bank Reconciled Date</td>
                    <td>
                        <html:text property="reconcileDate" styleId="reconcileDate" styleClass="textbox"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">From Date</td>
                    <td>
                        <html:text property="fromDate" styleId="fromDate" styleClass="textbox"/>
                    </td>
                    <td class="label">To Date</td>
                    <td>
                        <html:text property="toDate" styleId="toDate" styleClass="textbox"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Check Number From</td>
                    <td>
                        <html:text property="fromCheckNumber" styleId="fromCheckNumber" styleClass="textbox"/>
                    </td>
                    <td class="label">Check Number To</td>
                    <td>
                        <html:text property="toCheckNumber" styleId="toCheckNumber" styleClass="textbox"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Vendor Name</td>
                    <td>
                        <html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
                        <html:hidden property="vendorNumber" styleId="vendorNumber"/>
                        <input name="vendorNameCheck" id="vendorNameCheck"  type="hidden" value="${checkRegisterForm.vendorName}"/>
                    </td>
                    <td class="label">Payment Amount</td>
                    <td>
                        <html:select property="amountOperator" styleId="amountOperator" styleClass="dropdown width-40px">
                            <html:option value="&lt;&gt;">&lt;&gt;</html:option>
                            <html:option value="&gt;=">&gt;=</html:option>
                            <html:option value="&gt;">&gt;</html:option>
                            <html:option value="=">=</html:option>
                            <html:option value="&lt;=">&lt;=</html:option>
                            <html:option value="&lt;">&lt;</html:option>
                        </html:select>
                        <html:text property="paymentAmount" styleId="paymentAmount" styleClass="textbox width-80px"/>
                    </td>
                <tr>
                <tr>
                    <td class="label">Show Status</td>
                    <td class="label">
                        <html:radio property="status" styleId="statusCleared" value="Cleared"/><label for="statusCleared">Cleared</label>
                        <html:radio property="status" styleId="statusNotCleared" value="Not Cleared"/><label for="statusNotCleared">Not Cleared</label>
                        <html:radio property="status" styleId="statusVoided" value="Voided"/><label for="statusVoided">Voided</label>
                        <html:radio property="status" styleId="statusAll" value="All"/><label for="statusAll">All</label>
                    </td>
                    <td class="label">Batch Number</td>
                    <td>
                        <html:text property="batchId" styleId="batchId" styleClass="textbox"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Payment Method</td>
                    <td>
                        <html:select property="paymentMethod" styleId="paymentMethod" styleClass="dropdown">
                            <html:option value="">ALL</html:option>
                            <html:option value="CHECK">CHECK</html:option>
                            <html:option value="ACH">ACH</html:option>
                            <html:option value="WIRE">WIRE</html:option>
                            <html:option value="ACH DEBIT">ACH DEBIT</html:option>
                            <html:option value="CREDIT CARD">CREDIT CARD</html:option>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="align-center">
                        <input type="button" class="button" value="Search" onclick="search();">
                        <input type="button" class="button" value="Clear" onclick="clearAll();">
                    </td>
                </tr>
                <tr>
                    <th colspan="4">
                        <label>Check Register List</label>
                        <c:if test="${not empty checkRegisterForm.paymentList}">
                            <input type="button" class="button float-right" value="Save" onclick="save();">
                        </c:if>
                    </th>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:import url="checkRegisterList.jsp"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="action" styleId="action"/>
        </html:form>
    </body>
</html>
