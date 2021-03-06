<%-- 
    Document   : results
    Created on : 13 Jan, 2015, 1:04:53 AM
    Author     : Lucky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="results-container">
    <table class="table margin-none border-none" style="border-spacing: 0;">
        <c:if test="${not empty scanForm.results1 || not empty scanForm.results2}">
            <tr>
                <td class="${not empty scanForm.results2 ? 'width-50pc' : 'width-100pc'}">
                    <table width="100%" cellpadding="0" cellspacing="1" class="display-table margin-none">
                        <thead>
                            <tr>
                                <th><a href="javascript:doSort('documentName')">Document Name</a></th>
                                <c:if test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                    <th><a href="javascript:doSort('screenName')">Screen Name</a></th>
                                </c:if>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="result" items="${scanForm.results1}" varStatus="status">
                                <tr class="${zebra}">
                                    <td>${result.documentName}</td>
                                    <c:if test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                        <td>${result.screenName}</td>
                                    </c:if>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                                <img src="${path}/images/edit.png" title="Edit" onclick="editDocument('${result.id}');"/>
                                                <img src="${path}/images/trash.png" title="Delete" onclick="removeDocument('${result.documentName}', '${result.id}');"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${not (roleDuty.viewAccountingScanAttach
                                                                  && (scanForm.screenName eq 'INVOICE' || scanForm.screenName eq 'AR BATCH'))}">
                                                      <img src="${path}/images/icons/scanner.png" title="Scan" onclick="showComment('${result.documentName}', 'Scan');"/>
                                                      <img src="${path}/images/icons/attach.png" title="Attach" onclick="showComment('${result.documentName}', 'Attach');"/>
                                                </c:if>
                                            <img src="${path}/images/icons/preview.png" title="View" onclick="showDocuments('${result.documentName}')"/>
                                                <c:if test="${result.uploadCount > 0}">
                                                    <span class="red-90" style="vertical-align: super">
                                                        (${result.uploadCount})
                                                    </span>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <c:choose>
                                    <c:when test="${status.index % 2 eq 0}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
                <c:if test="${not empty scanForm.results2}">
                    <td class="width-50pc">
                        <table width="100%" cellpadding="0" cellspacing="1" class="display-table margin-none">
                            <thead>
                                <tr>
                                    <th><a href="javascript:doSort('documentName')">Document Name</a></th>
                                    <c:if test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                        <th><a href="javascript:doSort('screenName')">Screen Name</a></th>
                                    </c:if>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="zebra" value="odd"/>
                                <c:forEach var="result" items="${scanForm.results2}" varStatus="status">
                                    <tr class="${zebra}">
                                        <td>${result.documentName}</td>
                                        <c:if test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                            <td>${result.screenName}</td>
                                        </c:if>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                                    <img src="${path}/images/edit.png" title="Edit" onclick="editDocument('${result.id}');"/>
                                                    <img src="${path}/images/trash.png" title="Delete" onclick="removeDocument('${result.documentName}', '${result.id}');"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${not (roleDuty.viewAccountingScanAttach
                                                                  && (scanForm.screenName eq 'INVOICE' || scanForm.screenName eq 'AR BATCH'))}">
                                                          <img src="${path}/images/icons/scanner.png" title="Scan" onclick="showComment('${result.documentName}', 'Scan');"/>
                                                          <img src="${path}/images/icons/attach.png" title="Attach" onclick="showComment('${result.documentName}', 'Attach');"/>
                                                    </c:if>
                                                    <img src="${path}/images/icons/preview.png" title="View" onclick="showDocuments('${result.documentName}')"/>
                                                    <c:if test="${result.uploadCount > 0}">
                                                        <span class="red-90" style="vertical-align: super">
                                                            (${result.uploadCount})
                                                        </span>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <c:choose>
                                        <c:when test="${status.index % 2 eq 0}">
                                            <c:set var="zebra" value="even"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="zebra" value="odd"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>
                </c:if>
            </tr>
        </c:if>
        <c:if test="${not empty scanForm.results3}">
            <tr>
                <td class="width-100pc">
                    <table width="100%" cellpadding="0" cellspacing="1" class="display-table margin-none">
                        <thead>
                            <tr>
                                <th><a href="javascript:doSort('documentName')">Document Name</a></th>
                                <c:if test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                    <th><a href="javascript:doSort('screenName')">Screen Name</a></th>
                                </c:if>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="result" items="${scanForm.results3}" varStatus="status">
                                <tr class="${zebra}">
                                    <td>${result.documentName}</td>
                                    <c:if test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                        <td>${result.screenName}</td>
                                    </c:if>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                                <img src="${path}/images/edit.png" title="Edit" onclick="editDocument('${result.id}');"/>
                                                <img   src="${path}/images/trash.png" title="Delete" onclick="removeDocument('${result.documentName}', '${result.id}');"/>
                                            </c:when>
                                            <c:otherwise>                                               
                                                <img  src="${path}/images/icons/preview.png" title="View" onclick="showSop('${result.documentName}')"/>
                                                <c:if test="${result.uploadCount > 0}">
                                                    <span class="red-90" style="vertical-align: super">
                                                        (${result.uploadCount})
                                                    </span>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <c:choose>
                                    <c:when test="${status.index % 2 eq 0}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
               </td>
            </tr>
        </c:if>

    </table>
</div>