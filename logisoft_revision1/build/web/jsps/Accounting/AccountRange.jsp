<%-- 
    Document   : Account Range
    Created on : Feb 25, 2014, 2:32:34 AM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <html>
        <head>
            <title>Account Range</title>
            <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/accountRange.js"></script>
    </head>
    <body>
        <table class="table">
            <tr>
                <th colspan="5">Trial Balance Report</th>
            </tr>
            <tr>
                <td class="label">
                    <input type="checkbox" name="ecuReport" id="ecuReport"/>ECU Reports
                </td>
                <td class="label">Starting Account</td>
                <td>
                    <input type="text" name="startingAccount" id="startingAccount" class="textbox"/>
                    <input type="hidden" name="startingAccountCheck" id="startingAccountCheck"/>
                </td>
                <td class="label">Ending Account</td>
                <td>
                    <input type="text" name="endingAccount" id="endingAccount" class="textbox"/>
                    <input type="hidden" name="endingAccountCheck" id="endingAccountCheck"/>
                </td>
            </tr> 
            <tr> 
                <td colspan="5" class="align-center">
                    <input type="button" class="button" value="Print" onclick="printTrialBalance('${param.value}');"/>
                    <input type="button" class="button" value="Export To Excel" onclick="exportTrialBalance('${param.value}');"/>
                </td>
            </tr>
        </table>
    </body>
</html>
