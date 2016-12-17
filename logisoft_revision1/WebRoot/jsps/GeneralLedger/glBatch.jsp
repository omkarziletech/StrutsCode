<%-- 
    Document   : glBatch
    Created on : Jul 24, 2011, 4:27:09 PM
    Author     : lakshh
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
            <title>GL Batch</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.fileupload.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true"/>
        <c:if test="${param.accessMode==0}">

            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false"/>
        </c:if>
    </head>
    <body>
        <%@include file="../preloader.jsp"%>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <input type="hidden" name="notesConstantGlBatch" id="notesConstantGlBatch" value="${notesConstants.GL_BATCH}"/>
        <html:form action="/glBatch?accessMode=${accessMode}" name="glBatchForm"
                   styleId="glBatchForm" type="com.logiware.form.GlBatchForm" scope="request" method="post" onsubmit="showPreloading()">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="pageNo" styleId="pageNo"/>
            <html:hidden property="noOfPages" styleId="noOfPages"/>
            <html:hidden property="currentPageSize" styleId="currentPageSize"/>
            <html:hidden property="totalPageSize" styleId="totalPageSize"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <html:hidden property="glBatch.id" styleId="glBatchId"/>
            <input type="hidden" name="className" id="className"/>
            <input type="hidden" name="methodName" id="methodName"/>
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
                    <td>Subledger Type</td>
                    <td>
                        <html:select property="subledgerType" styleId="subledgerType" styleClass="textlabelsBoldForTextBox" style="width:126px">
                            <html:option value="">Select</html:option>
                            <html:optionsCollection property="subledgerTypes"/>
                        </html:select>
                    </td>
                    <td>Show Status</td>
                    <td>
                        <html:radio property="status" styleId="status" value="${commonConstants.STATUS_OPEN}"/>Open
                        <html:radio property="status" styleId="status" value="${commonConstants.ALL}"/>All
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="3" align="right">
                        <input type="button" class="buttonStyleNew" value="Search" id="search"/>
                        <c:if test="${canEdit}">
                            <input type="button" class="buttonStyleNew" value="Add Batch" id="add"/>
                        </c:if>
                        <input type="button" class="buttonStyleNew" value="Reset" id="reset"/>
                    </td>
                    <td colspan="3">
                        <c:if test="${canEdit}">
                            <div class="float-left">
                                <html:hidden property="batchFileName" styleId="batchFileName"/>
                                <div class="float-left">
                                    <input type="file" name="file" id="file"/>
                                </div>
                                <div class="float-left" style="margin: 2px 0 0 2px">
                                    <input type="button" value="Import" class="buttonStyleNew" onclick="importBatch()"/>
                                </div>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew" style="margin: 10px 0 0 0">
                <tr class="tableHeadingNew">
                    <td>List of Batches</td>
                </tr>
                <tr>
                    <td><%@include file="glBatchResults.jsp"%></td>
                </tr>
            </table>
        </html:form>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/glBatch.js"></script>
        <c:if test="${not empty fileName}">
            <script type="text/javascript">
                            window.parent.showGreyBox("Batch Report", rootPath + "/servlet/FileViewerServlet?fileName=${fileName}");
            </script>
        </c:if>
    </body>
</html>