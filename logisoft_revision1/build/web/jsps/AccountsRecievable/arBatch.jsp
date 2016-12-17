<%--
    Document   : arBatch
    Created on : May 21, 2011, 8:32:49 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="../includes/jspVariables.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <title>AR Batch</title>
        <%@include file="../includes/baseResources.jsp"%>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false"/>
        </c:if>
	<style type="text/css">
	    img{
		vertical-align: middle;
	    }
	</style>
    </head>
    <body>
        <%@include file="../preloader.jsp"%>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <un:useConstants className="com.gp.cvst.logisoft.AccountingConstants" var="accountingConstants"/>
        <html:form action="/arBatch?accessMode=${accessMode}" name="arBatchForm"
                   styleId="arBatchForm" type="com.logiware.form.ArBatchForm" scope="request" method="post" onsubmit="showPreloading()">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="pageNo" styleId="pageNo"/>
            <html:hidden property="noOfPages" styleId="noOfPages"/>
            <html:hidden property="currentPageSize" styleId="currentPageSize"/>
            <html:hidden property="totalPageSize" styleId="totalPageSize"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <html:hidden property="selectedBatchId" styleId="selectedBatchId"/>
            <html:hidden property="paymentCheckId" styleId="paymentCheckId"/>
            <html:hidden property="selectedSubType" styleId="selectedSubType"/>
            <input type="hidden" name="notesConstantArBatch" id="notesConstantArBatch" value="${notesConstants.AR_BATCH}"/>
            <c:if test="${not empty message}">
                <div class="message">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="6">Batch Details</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Batch Number</td>
                    <td><html:text property="batchId" styleId="batchId" maxlength="10" styleClass="textlabelsBoldForTextBox"/></td>
                    <td>Date From</td>
                    <td>
                        <img src="${path}/img/CalendarIco.gif" alt="Date From" align="top"
                             id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        <html:text property="fromDate" styleId="txtcal1" styleClass="textlabelsBoldForTextBox" maxlength="10"/>
                    </td>
                    <td>Date To</td>
                    <td>
                        <img src="${path}/img/CalendarIco.gif" alt="Date To" align="top"
                             id="cal2" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        <html:text property="toDate" styleId="txtcal2" styleClass="textlabelsBoldForTextBox" maxlength="10"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Show Status</td>
                    <td>
                        <html:radio property="status" styleId="status" value="${commonConstants.STATUS_OPEN}"/>Open
                        <html:radio property="status" styleId="status" value="${commonConstants.STATUS_CLOSED}"/>Closed
                        <html:radio property="status" styleId="status" value="${commonConstants.BOTH}"/>Both
                    </td>
                    <td>Show Type</td>
                    <td>
                        <html:radio property="batchType" styleId="batchType" value="${accountingConstants.AR_CASH_BATCH}"/>Cash Batch
                        <html:radio property="batchType" styleId="batchType" value="${accountingConstants.AR_NET_SETT_BATCH}"/>Net Settlement Batch
                        <html:radio property="batchType" styleId="batchType" value="${accountingConstants.AR_BATCH_BOTH}"/>Both
                    </td>
                    <td>Batch Amount</td>
                    <td>
                        <html:text property="batchAmount" styleId="batchAmount" styleClass="textlabelsBoldForTextBox" maxlength="11"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        Select User
                        <html:checkbox property="searchByUser" styleId="searchByUser"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${arBatchForm.searchByUser}">
                                <html:select property="user" styleId="user" styleClass="dropdown_accounting" style="width:126px">
                                    <html:option value="">All users</html:option>
                                    <c:forEach var="user" items="${arBatchForm.users}">
                                        <html:option value="${user}"/>
                                    </c:forEach>
                                </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:select property="user" styleId="user" styleClass="dropdown_accounting" style="width:126px;display:none">
                                    <html:option value="">All users</html:option>
                                    <c:forEach var="user" items="${arBatchForm.users}">
                                        <html:option value="${user}"/>
                                    </c:forEach>
                                </html:select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <input type="button" class="buttonStyleNew" value="Search" id="search"/>
                        <c:if test="${canEdit}">
                            <input type="button" class="buttonStyleNew" value="Add Batch" id="addArBatch"/>
                        </c:if>
                        <input type="button" class="buttonStyleNew" value="Reset" id="reset"/>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew" style="margin: 10px 0 0 0">
                <tr class="tableHeadingNew">
                    <td>List of Batches</td>
                </tr>
                <tr>
                    <td><%@include file="arBatchResults.jsp"%></td>
                </tr>
            </table>
        </html:form>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arBatch.js"></script>
        <c:if test="${not empty fileName}">
            <script type="text/javascript">
                window.parent.showGreyBox("Batch Report",rootPath+"/servlet/FileViewerServlet?fileName=${fileName}");
            </script>
        </c:if>
    </body>
</html>
