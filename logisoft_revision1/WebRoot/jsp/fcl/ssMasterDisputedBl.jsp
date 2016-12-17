<%-- 
    Document   : ssMasterDisputedBl
    Created on : Mar 11, 2013, 2:35:23 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SS Master Disputed BL</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript">
	    var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/fcl/ssMasterDisputedBl.js"></script>
        <c:set var="accessMode" value="1"/>
        <c:set var="writeMode" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="writeMode" value="false"/>
        </c:if>
    </head>
    <body>
        <%@include file="../../jsps/preloader.jsp"%>
        <div id="body-container">
            <html:form action="/fcl/ssMasterDisputedBl?accessMode=${accessMode}" name="ssMasterDisputedBlForm"
                       styleId="ssMasterDisputedBlForm" type="com.logiware.fcl.form.SsMasterDisputedBlForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="limit" styleId="limit"/>
                <html:hidden property="selectedPage" styleId="selectedPage"/>
                <html:hidden property="sortBy" styleId="sortBy"/>
                <html:hidden property="orderBy" styleId="orderBy"/>
                <html:hidden property="importFile" styleId="importFile"/>
                <c:if test="${not empty message}">
                    <div class="message">${message}</div>
                </c:if>
                <table class="table" style="margin: 0">
                    <tr>
                        <th colspan="8">Search Criteria</th>
                    </tr>
                    <tr>
                        <td class="label">File Number</td>
                        <td>
                            <html:text property="fileNumber" styleId="fileNumber" styleClass="textbox"/>
                        </td>
                        <td class="label">Origin</td>
                        <td>
                            <html:text property="origin" styleId="origin" styleClass="textbox"/>
                            <input type="hidden" name=originCheck" id="originCheck" value="${ssMasterDisputedBlForm.origin}"/>
                        </td>
                        <td class="label">Destination</td>
                        <td>
                            <html:text property="destination" styleId="destination" styleClass="textbox"/>
                            <input type="hidden" name=destinationCheck" id="destinationCheck" value="${ssMasterDisputedBlForm.destination}"/>
                        </td>
                        <td class="label">POL</td>
                        <td>
                            <html:text property="pol" styleId="pol" styleClass="textbox"/>
                            <input type="hidden" name=polCheck" id="polCheck" value="${ssMasterDisputedBlForm.pol}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">POD</td>
                        <td>
                            <html:text property="pod" styleId="pod" styleClass="textbox"/>
                            <input type="hidden" name=podCheck" id="podCheck" value="${ssMasterDisputedBlForm.pod}"/>
                        </td>
                        <td class="label">SSL</td>
                        <td>
                            <html:text property="sslineName" styleId="sslineName" styleClass="textbox"/>
                            <html:hidden property="sslineNumber" styleId="sslineNumber"/>
                            <input type="hidden" name=sslineNameCheck" id="sslineNameCheck" value="${ssMasterDisputedBlForm.sslineName}"/>
                        </td>
                        <td class="label">ETA</td>
                        <td>
                            <html:text property="eta" styleId="eta" styleClass="textbox" maxlength="10"/>
                        </td>
                        <td class="label">ETD</td>
                        <td>
                            <html:text property="etd" styleId="etd" styleClass="textbox" maxlength="10"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">SSL BL</td>
                        <td class="label">
                            <html:select property="sslBl" styleId="sslBlPrepaid" styleClass="dropdown" style="width: 128px;">
                                <html:option value="">Select</html:option>
                                <html:option value="P-Prepaid">Prepaid</html:option>
                                <html:option value="C-Collect">Collect</html:option>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="8" class="align-center">
                            <input type="button" value="Search" class="button" onclick="search();"/>
                            <input type="button" value="Reset" class="button" onclick="resetAll();"/>
                        </td>
                    </tr>
                    <tr>
                        <th colspan="8">Disputed BL List</th>
                    </tr>
                    <tr>
                        <td colspan="8">
                            <div id="results">
                                <c:import url="/jsp/fcl/ssMasterDisputedBlResults.jsp"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </html:form>
        </div>
    </body>
</html>