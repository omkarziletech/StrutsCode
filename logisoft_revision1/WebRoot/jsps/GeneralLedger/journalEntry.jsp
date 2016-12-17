<%-- 
    Document   : journalEntry
    Created on : Jul 25, 2011, 7:46:06 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="../includes/jspVariables.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html>
    <head>
        <title>Journal Entry</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.fileupload.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
	<script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/journalEntry.js"></script>  
        <link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true" scope="request"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false" scope="request"/>
        </c:if>
        <style type="text/css">
            .drillDownDiv{
                width: 125px;
            }
            .textlabelsBoldForTextBox, .textlabelsBoldForTextBoxDisabledLook{
                width: 150px;
            }
        </style>
    </head>
    <body>
        <%@include file="../preloader.jsp"%>
        <html:form action="/glBatch?accessMode=${accessMode}" name="glBatchForm"
                   styleId="glBatchForm" type="com.logiware.form.GlBatchForm" scope="request" method="post">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="batchId" styleId="batchId"/>
            <html:hidden property="batchIndex" styleId="batchIndex"/>
            <html:hidden property="journalEntryIndex" styleId="journalEntryIndex"/>
            <html:hidden property="newGlBatchId" styleId="newGlBatchId"/>
            <input type="hidden" name="className" id="className"/>
            <input type="hidden" name="methodName" id="methodName"/>
            <c:choose>
                <c:when test="${from == 'Transction History'}">
                    <c:import url="/jsps/GeneralLedger/journalEntryViewMode.jsp"/>
                </c:when>
                <c:when test="${glBatchForm.from == 'Reconcile'}">
                    <c:import url="/jsps/GeneralLedger/journalEntryReconcileMode.jsp"/>
                </c:when>
                <c:when test="${glBatchForm.from == 'FiscalPeriod'}">
                    <c:import url="/jsps/GeneralLedger/journalEntryFiscalPeriodMode.jsp"/>
                </c:when>
                <c:when test="${fn:containsIgnoreCase(glBatchForm.glBatch.status, 'Open')}">
                    <c:import url="/jsps/GeneralLedger/journalEntryFullMode.jsp"/>
                </c:when>
                <c:otherwise>
                    <c:import url="/jsps/GeneralLedger/journalEntryPartialMode.jsp"/>
                </c:otherwise>
            </c:choose>
        </html:form>
    </body>
</html>