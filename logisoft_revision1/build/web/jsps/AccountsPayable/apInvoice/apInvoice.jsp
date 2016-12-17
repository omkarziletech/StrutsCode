<%-- 
    Document   : apInvoice
    Created on : Jun 19, 2012, 8:06:35 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AP Invoice</title>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript">
            var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/apInvoice.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
    </head>
    <body>
        <div id="body-container">
            <%@include file="../../preloader.jsp"%>
            <div id="message" class="message" style="width: 100%; text-align: center;"></div>
            <html:form action="/apInvoice" name="apInvoiceForm"
                       styleId="apInvoiceForm" type="com.logiware.accounting.form.ApInvoiceForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="recurring" styleId="recurring"/>
                <html:hidden property="id" styleId="id"/>
                <html:hidden property="lineItemId" styleId="lineItemId"/>
                <table class="table add">
                    <thead>
                        <tr>
                            <th colspan="6">
                                <c:choose>
                                    <c:when test="${apInvoiceForm.recurring}">Recurring Invoice</c:when>
                                    <c:otherwise>AP Invoice</c:otherwise>
                                </c:choose>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${apInvoiceForm.mode=='view'}">
                                <tr>
                                    <td class="label width-150px">Vendor Name</td>
                                    <td>
                                        <html:text property="vendorName" styleId="vendorName" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                        <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apInvoiceForm.vendorName}" class="hidden"/>
                                    </td>
                                    <td class="label width-150px">Vendor Number</td>
                                    <td>
                                        <html:text property="vendorNumber" styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td class="label width-150px">Invoice Number</td>
                                    <td>
                                        <html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label width-150px">Invoice Date</td>
                                    <td>
                                        <html:text property="invoiceDate" styleId="invoiceDate" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td class="label width-150px">Terms</td>
                                    <td>
                                        <html:text property="creditDesc" styleId="creditDesc" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                        <html:hidden property="creditTerm" styleId="creditTerm"/>
                                        <html:hidden property="creditId" styleId="creditId"/>
                                    </td>
                                    <td class="label width-150px">Due Date</td>
                                    <td>
                                        <html:text property="dueDate" styleId="dueDate" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td rowspan="3" valign="top" class="label">For</td>
                                    <td rowspan="3">
                                        <html:textarea property="forComments" styleId="forComments"
                                                       rows="3" cols="40" styleClass="textbox" style="width:150px;" disabled="true" tabindex="-1"/>
                                    </td>
                                    <td colspan="4" class="label width-200px">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <input type="button" value="Scan/Attach" class="button" onclick="upload()" tabindex="-1"/>
                                        <input type="button" value="Notes" class="button" onclick="notes()" tabindex="-1"/>
                                        <input type="button" value="Search" class="button" onclick="gotoSearch('false')" tabindex="-1"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:when test="${apInvoiceForm.mode=='partial'}">
                                <tr>
                                    <td class="label width-150px">Vendor Name</td>
                                    <td>
                                        <html:text property="vendorName" styleId="vendorName" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                        <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apInvoiceForm.vendorName}" class="hidden"/>
                                    </td>
                                    <td class="label width-150px">Vendor Number</td>
                                    <td>
                                        <html:text property="vendorNumber" styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td class="label width-150px">Invoice Number</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${apInvoiceForm.recurring}">
                                                <html:text property="invoiceNumber" styleId="invoiceNumber"
                                                           styleClass="textbox" maxlength="50" onchange="findDuplicates()"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="invoiceNumber" styleId="invoiceNumber"
                                                           styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label width-150px">Invoice Date</td>
                                    <td>
                                        <html:text property="invoiceDate" styleId="invoiceDate"
                                                   styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
                                        <img src="${path}/img/CalendarIco.gif" alt="Invoice Date" align="top" id="invoiceDateCalendar" class="calendar"/>
                                    </td>
                                    <td class="label width-150px">Terms</td>
                                    <td>
                                        <html:text property="creditDesc" styleId="creditDesc" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                        <html:hidden property="creditTerm" styleId="creditTerm"/>
                                        <html:hidden property="creditId" styleId="creditId"/>
                                    </td>
                                    <td class="label width-150px">Due Date</td>
                                    <td>
                                        <html:text property="dueDate" styleId="dueDate" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td rowspan="3" valign="top" class="label">For</td>
                                    <td rowspan="3">
                                        <html:textarea property="forComments" styleId="forComments"
                                                       rows="3" cols="40" styleClass="textbox" style="width:150px;"/>
                                    </td>
                                    <td colspan="4" class="label width-200px">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <input type="button" value="Save" class="button" onclick="saveInvoice()" tabindex="-1"/>
                                        <input type="button" value="Post" class="button" onclick="post()" tabindex="-1"/>
                                        <input type="button" value="Scan/Attach" class="button" onclick="upload()" tabindex="-1"/>
                                        <input type="button" value="Notes" class="button" onclick="notes()" tabindex="-1"/>
                                        <c:choose>
                                            <c:when test="${apInvoiceForm.recurring}">
                                                <input type="button" value="Ap Invoice Return" class="button" onclick="gotoSearch('false')" tabindex="-1"/>
                                                <input type="button" value="Search" class="button" onclick="gotoSearch('true')" tabindex="-1"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" value="Search" class="button" onclick="gotoSearch('false')" tabindex="-1"/>
                                                <input type="button" value="Recurring" class="button" onclick="gotoSearch('true')" tabindex="-1"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td class="label width-150px">Vendor Name</td>
                                    <td>
                                        <html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
                                        <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apInvoiceForm.vendorName}" class="hidden"/>
                                    </td>
                                    <td class="label width-150px">Vendor Number</td>
                                    <td>
                                        <html:text property="vendorNumber" styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td class="label width-150px">Invoice Number</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${apInvoiceForm.recurring}">
                                                <html:text property="invoiceNumber" styleId="invoiceNumber"
                                                           styleClass="textbox" maxlength="50" onchange="findDuplicates()" tabindex="-1"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="invoiceNumber" styleId="invoiceNumber"
                                                           styleClass="textbox" maxlength="50" onchange="findDuplicates()"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
				    <td class="label width-150px">Invoice Date</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${apInvoiceForm.recurring}">
                                                <html:text property="invoiceDate" styleId="invoiceDate"
                                                           styleClass="textbox" maxlength="10" onchange="validateDate(this)" tabindex="-1"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="invoiceDate" styleId="invoiceDate"
                                                           styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
                                            </c:otherwise>
                                        </c:choose>
					<img src="${path}/img/CalendarIco.gif" alt="Invoice Date" align="top" id="invoiceDateCalendar" class="calendar-img"/>
                                    </td>
                                    <td class="label width-150px">Terms</td>
                                    <td>
                                        <html:text property="creditDesc" styleId="creditDesc" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                        <html:hidden property="creditTerm" styleId="creditTerm"/>
                                        <html:hidden property="creditId" styleId="creditId"/>
                                    </td>
                                    <td class="label width-150px">Due Date</td>
                                    <td>
                                        <html:text property="dueDate" styleId="dueDate" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td rowspan="3" valign="top" class="label">For</td>
                                    <td rowspan="3">
                                        <c:choose>
                                            <c:when test="${apInvoiceForm.recurring}">
                                                <html:textarea property="forComments" styleId="forComments"
                                                               rows="3" cols="40" styleClass="textbox" style="width:150px;" tabindex="-1"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:textarea property="forComments" styleId="forComments"
                                                               rows="3" cols="40" styleClass="textbox" style="width:150px;"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td colspan="4" class="label width-200px">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <input type="button" value="Save" class="button" onclick="saveInvoice()" tabindex="-1"/>
                                        <c:if test="${not apInvoiceForm.recurring}">
                                            <input type="button" value="Post" class="button" onclick="post()" tabindex="-1"/>
                                        </c:if>
                                        <input type="button" value="Scan/Attach" class="button" onclick="upload()" tabindex="-1"/>
                                        <input type="button" value="Notes" class="button" onclick="notes()" tabindex="-1"/>
                                        <input type="button" value="Search" class="button" onclick="gotoSearch('false')" tabindex="-1"/>
                                        <input type="button" value="Recurring" class="button" onclick="gotoSearch('true')" tabindex="-1"/>
                                        <input type="button" value="Clear" class="button" onclick="addInvoice()" tabindex="-1"/>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <div id="line-items-container">
                    <c:import url="/jsps/AccountsPayable/apInvoice/lineItems.jsp"/>
                </div>
            </html:form>
        </div>
    </body>
</html>
