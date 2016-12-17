<%-- 
    Document   : dispute
    Created on : Dec 11, 2014, 2:37:57 PM
    Author     : venugopal.s
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html:form action="/arReports" name="arReportsForm" styleId="disputeForm" 
           type="com.logiware.accounting.form.ArReportsForm" scope="request" method="post">
    <table class="table filters">
        <thead>
            <tr>
                <th colspan="6">Filters</th>
            </tr>
        </thead>
        <tbody style="vertical-align: top">
            <tr>
                <td class="label width-150px">Customer Name</td>
                <td>
                    <html:text property="customerName" styleId="disputeCustomerName" styleClass="textbox"/>
                    <input type="hidden" name="customerNameCheck" id="disputeCustomerNameCheck" value="${arReportsForm.customerName}"/>
                </td>
                <td class="label width-150px">Customer Number</td>
                <td>
                    <html:text property="customerNumber" styleId="disputeCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                </td>
                <td class="label width-100px">For Collector</td>
                <td>
                    <html:select property="collector" styleId="disputeCollector" styleClass="dropdown">
                        <html:option value="">Select</html:option>
                        <html:option value="ALL">All</html:option>
                        <html:optionsCollection property="collectors"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td class="label width-150px">Date From</td>
                <td class="label">
                    <html:text property="fromDate" styleId="disputeFromDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
                    <img src="${path}/img/CalendarIco.gif" alt="Date From" align="top" id="disputeFromDateCalendar" class="calendar-img"/>
                </td>
                <td class="label width-150px">Date To</td>
                <td class="label">
                    <html:text property="toDate" styleId="disputeToDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
                    <img src="${path}/img/CalendarIco.gif" alt="Date To" align="top" id="disputeToDateCalendar" class="calendar-img"/>
                </td>
                <td class="label" colspan="2">
                    <html:checkbox property="allCustomers" styleId="disputeAllCustomers" value="true"/>
                    <label for="disputeAllCustomers">All Customers</label>
                </td>
            </tr>
            <tr>
                <td colspan="6" class="align-center">
                    <input type="button" value="Print" class="button" onclick="createPdf('dispute')"/>
                    <input type="button" value="Export To Excel" class="button" onclick="createExcel('dispute');"/>
                    <input type="button" value="Clear" class="button" onclick="refresh('dispute');"/>
                </td>
            </tr>
        </tbody>
    </html:form>
