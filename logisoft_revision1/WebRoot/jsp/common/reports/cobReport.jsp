<%-- 
    Document   : cobReport
    Created on : 4 Mar, 2015, 4:42:30 PM
    Author     : Lucky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>COB Report</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/reports/cobReport.js"></script>
    </head>
    <body>
        <html:form action="/report" name="reportForm" styleId="reportForm" 
                   type="com.logiware.common.form.ReportForm" scope="request" method="post">
            <table class="table">
                <tr>
                    <th colspan="8">COB Report</th>
                </tr>
                <tr>
                    <td>
                        <table class="table no-margin-border">
                            <tr>
                                <th colspan="2">
                                    Billing Terminal
                                    <input type="button" class="button float-right" value="Select All Terminals" onclick="selectAllTerminals(this)"/>
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <table class="table no-margin-border">
                                        <fmt:parseNumber var="halfSize" value="${fn:length(reportForm.terminals)/2}" integerOnly="true"/>
                                        <c:forEach var="terminal" items="${reportForm.terminals}" varStatus="status">
                                            <tr>
                                                <td class="label">
                                                    <html:checkbox property="billingTerminal" styleId="billingTerminal${status.count}" value="${terminal}"/>
                                                    <label for="billingTerminal${status.count}">${terminal}</label>
                                                </td>
                                            </tr>
                                            <c:if test="${halfSize eq (status.count - 1)}">
                                                </table></td><td><table class="table no-margin-border float-left">
                                            </c:if>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table class="table no-margin-border">
                            <tr>
                                <th colspan="2">
                                    Destination Region
                                    <input type="button" class="button float-right" value="Select All Regions" onclick="selectAllRegions(this)"/>
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <table class="table no-margin-border">
                                        <fmt:parseNumber var="halfSize" value="${fn:length(reportForm.regions)/2}" integerOnly="true"/>
                                        <c:forEach var="region" items="${reportForm.regions}" varStatus="status">
                                            <c:if test="${not empty region.value}">
                                                <tr>
                                                    <td class="label">
                                                        <html:checkbox property="destinationRegions" styleId="destinationRegions${status.count}" value="${region.value}"/>
                                                        <label for="destinationRegions${status.count}">${region.label}</label>
                                                    </td>
                                                </tr>
                                                <c:if test="${halfSize eq (status.count - 1)}">
                                                    </table></td><td><table class="table no-margin-border float-left">
                                                </c:if>
                                            </c:if>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table class="table no-margin-border">
                            <tr>
                                <th colspan="2">Date Range</th>
                            </tr>
                            <tr>
                                <td class="label align-right width-80px">From Date</td>
                                <td class="width-145px">
                                    <html:text property="fromDate" styleId="fromDate" styleClass="textbox" maxlength="10"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label align-right width-80px">To Date</td>
                                <td class="width-145px">
                                    <html:text property="toDate" styleId="toDate" styleClass="textbox" maxlength="10"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label align-right width-80px">Sail Date
                                <html:radio property="dateRange" styleId="dateRange" name="reportForm" value="sailDate"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label align-right width-80px">Doc Cut Off
                                 <html:radio property="dateRange" styleId="dateRange" name="reportForm" value="docCutOff" />
                                 </td>
                            </tr>
                            <tr>
                                <td class="label align-right width-80px">Carrier Doc Cut Off
                                <html:radio property="dateRange" styleId="dateRange" name="reportForm" value="carrierDocCutOff" />
                                </td>
                            </tr>
                            <tr>
                                <td class="label align-right width-80px">Include Bookings
                                    <html:checkbox property="includeBookings" name="reportForm" styleId="includeBookings" value="true"/> 
                              </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="align-center" style="padding-top: 40px">
                                    <input type="button" value="Export to Excel" class="button" onclick="exportToExcel()"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>
