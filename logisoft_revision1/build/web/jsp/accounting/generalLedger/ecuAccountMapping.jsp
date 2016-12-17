<%-- 
    Document   : ecuAccountMapping
    Created on : Feb 24, 2014, 4:13:56 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:set var="pageUrl" value="${path}/ecuAccountMapping.do"/>
<c:choose>
    <c:when test="${param.accessMode eq 0}">
        <c:set var="accessMode" value="0" scope="request"/>
        <c:set var="writeMode" value="false" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="accessMode" value="1" scope="request"/>
        <c:set var="writeMode" value="true" scope="request"/>
    </c:otherwise>
</c:choose>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ECU Account Mapping</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/ecuAccountMapping.js"></script>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <html:form action="/ecuAccountMapping" type="com.logiware.accounting.form.EcuAccountMappingForm"
                   name="ecuAccountMappingForm" styleId="ecuAccountMappingForm" scope="request" method="post">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="ecuAccountMapping.reportCategory" styleId="reportCategory"/>
            <html:hidden property="ecuAccountMapping.accountType" styleId="accountType"/>
            <html:hidden property="ecuAccountMapping.id" styleId="id"/>
            <c:if test="${not empty message}">
                <span class="message">${message}</span>
            </c:if>
            <c:if test="${not empty error}">
                <span class="error">${error}</span>
            </c:if>
            <div id="addNew" style="display: none">
                <table class="table">
                    <tr>
                        <td class="label width-115px">ECU Report Category</td>
                        <td class="width-155px">
                            <input type="text" class="textbox report-category width-150px"/>
                        </td>
                        <td class="label width-80px">Account Type</td>
                        <td>
                            <select class="dropdown account-type">
                                <option value="">Select</option>
                                <c:forEach var="accountType" items="${ecuAccountMappingForm.accountTypes}">
                                    <option value="${accountType}">${accountType}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <input type="button" class="button" value="Save" onclick="save(this);"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="search-results">
                <table class="table">
                    <tr>
                        <th>
                            <div class="float-left">ECU Report Categories</div>
                            <div class="float-right">
                                <a title="Refresh" href="${pageUrl}" onclick="showPreloading();">
                                    <img alt="Refresh" src="${path}/images/icons/refresh_16.png">
                                </a>
                                <a title="Add New" rel="sexylightbox"
                                   href="${pageUrl}#TB_inline?height=40&width=550&inlineId=addNew&title=Add New">
                                    <img alt="Add New" src="${path}/images/icons/add.png">
                                </a>
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <td>
                            <div class="result-container">
                                <table width="100%" cellpadding="0" cellspacing="1" class="display-table" style="table-layout: fixed;">
                                    <colgroup>
                                        <col span="1" style="width: 225px">
                                        <col span="1" style="width: 150px">
                                        <col span="1" style="width: 100px">
                                        <col span="1" style="width: 125px">
                                        <col span="1" style="width: 100px">
                                        <col span="1" style="width: 125px">
                                        <col span="1" style="width: 50px">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>ECU Report Category</th>
                                            <th>Account Type</th>
                                            <th>Created By</th>
                                            <th>Created Date</th>
                                            <th>Updated By</th>
                                            <th>Updated Date</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="zebra" value="odd"/>
                                        <c:forEach var="mapping" items="${ecuAccountMappingForm.ecuAccountMappings}">
                                            <tr class="${zebra} align-center">
                                                <td class="align-left">
                                                    <input type="text" class="textbox width-250px report-category" value="${mapping.reportCategory}"/>
                                                </td>
                                                <td class="align-left">
                                                    <select class="dropdown account-type" style="min-width: 175px;" value="${mapping.accountType}">
                                                        <option value="">Select</option>
                                                        <c:forEach var="accountType" items="${ecuAccountMappingForm.accountTypes}">
                                                            <c:choose>
                                                                <c:when test="${mapping.accountType eq accountType}">
                                                                    <option value="${accountType}" selected>${accountType}</option>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <option value="${accountType}">${accountType}</option>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td>${mapping.createdBy}</td>
                                                <td><fmt:formatDate value="${mapping.createdOn}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
                                                <td>${mapping.updatedBy}</td>
                                                <td><fmt:formatDate value="${mapping.updatedOn}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
                                                <td>
                                                    <input type="hidden" class="id" value="${mapping.id}"/>
                                                    <img title="Save" src="${path}/images/icons/save.png" onclick="save(this);"/>
                                                    <img title="Delete" src="${path}/images/icons/close.png" onclick="removeMapping(this);"/>
                                                </td>
                                            </tr>
                                            <c:choose>
                                                <c:when test="${zebra eq 'odd'}">
                                                    <c:set var="zebra" value="even"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="zebra" value="odd"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
    </body>
</html>
