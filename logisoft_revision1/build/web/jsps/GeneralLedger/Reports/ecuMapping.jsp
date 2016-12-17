<%-- 
    Document   : ecuMapping
    Created on : Feb 25, 2014, 12:46:34 AM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:form action="/glReports" name="glReportsForm" styleId="glReportsForm" type="com.logiware.form.GlReportsForm" scope="request" method="post">
    <html:hidden property="action" styleId="action"/>
    <table class="table">
        <tr>
            <th colspan="8">Search Filters</th>
        </tr>
        <tr>
            <td class="label width-150px">ECU Account</td>
            <td>
                <html:text property="reportCategory" styleId="reportCategory" styleClass="textbox" maxlength="10"/>
                <input type="hidden" name="reportCategoryCheck" id="reportCategoryCheck" value="${glReportsForm.reportCategory}" class="hidden"/>
            </td>
            <td class="label width-150px">From Date</td>
            <td>
                <html:text property="fromDate" styleId="fromDateecuMapping" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
                <img src="${path}/img/CalendarIco.gif" alt="From Date" title="From Date" align="top" id="fromDateCalendarecuMapping" class="calendar-img"/>
            </td>
            <td class="label width-150px">To Date</td>
            <td>
                <html:text property="toDate" styleId="toDateecuMapping" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
                <img src="${path}/img/CalendarIco.gif" alt="To Date" title="To Date" align="top" id="toDateCalendarecuMapping" class="calendar-img"/>
            </td>
            <td class="label">
                <html:checkbox property="showPosted" styleId="showPosted" value="true"/>
                Show Posted
            </td>
        </tr>
        <tr>
            <td colspan="8" class="align-center">
                <input type="button" value="Export To Excel" class="button" onclick="createExcel('ecuMapping')"/>
                <input type="button" value="Export Mapping To Excel" class="button" onclick="createMappingToExcel()"/>
                <input type="button" value="Clear" class="button" onclick="refresh('ecuMapping')"/>
            </td>
        </tr>
    </table>
</html:form>
