<%-- 
    Document   : onlineUser
    Created on : May 20, 2014, 12:11:07 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/constants" prefix="constant"%>  
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Online Users</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/common/onlineUser.js"></script>
    </head>
    <body>
        <%@include file="../../jsps/preloader.jsp"%>
        <span class="message">${message}</span>
        <html:form action="/onlineUser" type="com.logiware.common.form.OnlineUserForm" 
                   name="onlineUserForm" styleId="onlineUserForm" scope="request" method="post">
            <html:hidden property="id" styleId="id"/>
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="limit" styleId="limit"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <html:hidden property="selectedPage" styleId="selectedPage"/>
            <table class="table">
                <tr>
                    <th>
                        <div class="float-left">List of Online Users</div>
                        <div class="float-right">
                            <a href="javascript: search();">
                                <img title="Refresh" src="${path}/images/icons/refresh_16.png"/>
                            </a>
                            <a href="javascript: exportToExcel();">
                                <img title="Export to Excel" src="${path}/images/icons/excel.png"/>
                            </a>
                        </div>
                    </th>
                </tr>
                <tr>
                    <td>
                        <div id="result-header" class="table-banner green">
                            <div class="float-right">
                                <div class="float-left">
                                    <c:choose>
                                        <c:when test="${onlineUserForm.totalRows gt onlineUserForm.selectedRows}">
                                            ${onlineUserForm.selectedRows} users displayed. ${onlineUserForm.totalRows} users found online.
                                        </c:when>
                                        <c:when test="${onlineUserForm.selectedRows gt 1}">${onlineUserForm.selectedRows} users found online.</c:when>
                                        <c:otherwise>1 user found online.</c:otherwise>
                                    </c:choose>
                                </div>
                                <c:if test="${onlineUserForm.totalPages gt 1 and onlineUserForm.selectedPage gt 1}">
                                    <a href="javascript: paging('1');">
                                        <img title="First page" src="${path}/images/next.png"/>
                                    </a>
                                    <a href="javascript: paging('${onlineUserForm.selectedPage - 1}');">
                                        <img title="Previous page" src="${path}/images/last.png"/>
                                    </a>
                                </c:if>
                                <c:if test="${onlineUserForm.totalPages gt 1}">
                                    <select id="selectedPageNo" class="dropdown float-left">
                                        <c:forEach begin="1" end="${onlineUserForm.totalPages}" var="selectedPage">
                                            <c:choose>
                                                <c:when test="${onlineUserForm.selectedPage eq selectedPage}">
                                                    <option value="${selectedPage}" selected>${selectedPage}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${selectedPage}">${selectedPage}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                    <a href="javascript: paging();">
                                        <img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
                                    </a>
                                </c:if>
                                <c:if test="${onlineUserForm.totalPages gt onlineUserForm.selectedPage}">
                                    <a href="javascript: paging('${onlineUserForm.selectedPage + 1}');">
                                        <img title="Next page" src="${path}/images/next.png"/>
                                    </a>
                                    <a href="javascript: paging('${onlineUserForm.totalPages}');">
                                        <img title="Last page" src="${path}/images/last.png"/>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                        <div class="result-container" style="width: 100%">
                            <table cellpadding="0" cellspacing="1" class="display-table">
                                <thead>
                                    <tr>
                                        <th><a href="javascript: sorting('loginName');">Login Name</a></th>
                                        <th><a href="javascript: sorting('firstName');">First Name</a></th>
                                        <th><a href="javascript: sorting('lastName');">Last Name</a></th>
                                        <th><a href="javascript: sorting('terminal');">Terminal</a></th>
                                        <th><a href="javascript: sorting('phone');">Phone</a></th>
                                        <th><a href="javascript: sorting('email');">Email</a></th>
                                        <th><a href="javascript: sorting('address');">Address</a></th>
                                        <th><a href="javascript: sorting('city');">City</a></th>
                                        <th><a href="javascript: sorting('state');">State</a></th>
                                        <th><a href="javascript: sorting('country');">Country</a></th>
                                        <th><a href="javascript: sorting('ipAddress');">IP Address</a></th>
                                        <th><a href="javascript: sorting('loggedOn');">Logged On</a></th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="zebra" value="odd"/>
                                    <c:forEach var="user" items="${onlineUserForm.onlineUsers}">
                                        <tr class="${zebra}">
                                            <td>${user.loginName}</td>
                                            <td>${user.firstName}</td>
                                            <td>${user.lastName}</td>
                                            <td>${user.terminal}</td>
                                            <td>${user.phone}</td>
                                            <td>${user.email}</td>
                                            <td>${user.address}</td>
                                            <td>${user.city}</td>
                                            <td>${user.state}</td>
                                            <td>${user.country}</td>
                                            <td>${user.ipAddress}</td>
                                            <td>${user.loggedOn}</td>
                                            <td>
                                                <c:if test="${user.userId ne loginuser.userId}">
                                                    <img src="${path}/images/icons/remove-user.png" 
                                                         title="Kill User" onclick="killUser('${user.loginName}', '${user.userId}');"/>
                                                </c:if>
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
        </html:form>
    </body>
</html>
