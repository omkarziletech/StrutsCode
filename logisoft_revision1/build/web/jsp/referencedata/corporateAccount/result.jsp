<%-- 
    Document   : result
    Created on : Aug 24, 2015, 10:54:23 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table">
    <tr class="tableHeadingNew"><td>Search Result</td></tr>
    <tr><td>
            <div class="table-banner green">
                <table style="margin: 0;overflow: hidden" id="corporateAccountList">
                    <tr><td>
                            <c:choose>
                                <c:when test="${fn:length(corporateAccountForm.corporateAcctList)>=1}">
                                    ${fn:length(corporateAccountForm.corporateAcctList)}   files found.
                                </c:when>
                                <c:otherwise>No file found.</c:otherwise>
                            </c:choose>
                        </td></tr>
                </table>
            </div>
            <c:if test="${not empty corporateAccountForm.corporateAcctList}">
                <table class="dataTable">
                    <thead>
                    <th>Account Name</th>
                    <th>Commodity Code</th>
                    <th>Corporate Acct Type</th>
                    <th>Action</th>
                    </thead>
                    <tbody style="text-transform: uppercase">
                        <c:forEach var="corporateAcct" items="${corporateAccountForm.corporateAcctList}">
                            <c:choose>
                                <c:when test="${zebra eq 'odd'}">
                                    <c:set var="zebra" value="even"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="odd"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}" style="border-color:white;">
                                <td>${corporateAcct.corporateName}</td>
                                <td>${corporateAcct.eliteCommodityCode}</td>
                                <td>${corporateAcct.corporateAccountType.acctTypeDescription}</td>
                                <td>
                                    <img alt="notes" title="Notes" width="16" height="16" onclick="showUpNotes('${path}','${corporateAcct.id}')"
                                         src="${path}/img/icons/e_contents_view.gif" class="notes"/>
                                    <img src="${path}/images/edit.png"  onclick="showAddorEditCoporateAcct('${path}','${corporateAcct.id}')"
                                         style="cursor:pointer" width="13" height="13" alt="edit" title="Edit"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </td>
    </tr>
</table>
