<%-- 
    Document   : trialBalance
    Created on : Feb 28, 2014, 12:32:24 AM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trial Balance</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/trialBalance.js"></script>
    </head>
    <body>
        <%@include file="../../../../jsps/preloader.jsp"%>
        <table class="table no-margin-border">
            <tr>
                <td class="label align-right" style="vertical-align: middle">ECU Format</td>
                <td>
                    <input type="checkbox" name="ecuFormat" id="ecuFormat"/>
                </td>
            </tr>
            <tr>
                <td class="label align-right">Starting Period</td>
                <td>
                    <input type="text" name="startPeriod" id="startPeriod" class="textbox"/>
                    <input type="hidden" name="startPeriodCheck" id="startPeriodCheck"/>
                </td>
            </tr>
            <tr>
                <td class="label align-right">Ending Period</td>
                <td>
                    <input type="text" name="endPeriod" id="endPeriod" class="textbox"/>
                    <input type="hidden" name="endPeriodCheck" id="endPeriodCheck"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="align-center">
                    <input type="button" class="button" value="Print" onclick="createTrialBalance('pdf');"/>
                    <input type="button" class="button" value="Export To Excel" onclick="createTrialBalance('xlsx');"/>
                </td>
            </tr>
        </table>
    </body>
</html>
