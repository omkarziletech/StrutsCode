<%-- 
    Document   : subsidiaryAccounts
    Created on : Dec 1, 2014, 5:14:44 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <table class="display-table">
        <thead>
            <tr>
                <th>Account Name</th>
                <th>Account Number</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <tr class="odd">
                <td>
                    <input type="text" class="textbox" id="subsidiaryAccountName"/>
                </td>
                <td>
                    <input type="text" class="textbox readonly" id="subsidiaryAccountNumber" readonly/>
                </td>
                <td>
                    <img src="${path}/images/icons/add.png" title="Add Subsidiary" onclick="addSubsidiary()"/>
            </td>
        </tr>
        <c:set var="zebra" value="even"/>
        <c:forEach var="account" items="${subsidiaryAccounts}">
            <tr class="${zebra}">
                <td>${account.accountName}</td>
                <td>${account.accountno}</td>
                <td>
                    <img src="${path}/images/icons/close.png" title="Remove Subsidiary" onclick="removeSubsidiary('${account.accountno}')"/>
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
