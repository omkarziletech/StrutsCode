<%-- 
    Document   : result
    Created on : Aug 24, 2015, 11:03:25 AM
    Author     : Wsware
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 0;overflow: hidden">
    <tr class="tableHeadingNew"><td>Search Result</td></tr>
    <tr><td>
            <div class="table-banner green">
                <table>
                    <tr><td>
                            <c:choose>
                                <c:when test="${fn:length(corporateAcctTypeForm.corporateAcctTypeList)>=1}">
                                    ${fn:length(corporateAcctTypeForm.corporateAcctTypeList)}   files found.
                                </c:when>
                                <c:otherwise>No file found.</c:otherwise>
                            </c:choose>
                        </td></tr>
                </table>
            </div>
            <c:if test="${not empty corporateAcctTypeForm.corporateAcctTypeList}">
                <table class="dataTable">
                    <thead>
                    <th>Description</th>
                    <th>Disable</th>
                    <th>Action</th>
                    </thead>
                    <tbody style="text-transform: uppercase">
                        <c:forEach var="corporateAcct" items="${corporateAcctTypeForm.corporateAcctTypeList}">
                            <c:choose>
                                <c:when test="${zebra eq 'odd'}">
                                    <c:set var="zebra" value="even"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="odd"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}" style="border-color:white;">
                                <td>${corporateAcct.acctTypeDescription}</td>
                                <td>${corporateAcct.acctTypeDisabled ? 'Y' :'N'}</td>
                                <td>
                                    <img alt="notes" title="Notes" width="16" height="16" onclick="showUpNotes('${path}','${corporateAcct.id}')"
                                         src="${path}/img/icons/e_contents_view.gif" class="notes"/>
                                    <img src="${path}/images/edit.png"  onclick="editCorporate('${corporateAcct.id}','${corporateAcct.acctTypeDescription}','${corporateAcct.acctTypeDisabled}')"
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
