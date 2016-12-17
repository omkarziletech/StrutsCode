<%-- 
    Document   : documents
    Created on : 16 Jan, 2015, 10:10:57 PM
    Author     : Lucky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="results-container">
    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" id="filelist">
        <thead>
            <tr>
                <th><a href="javascript:doSort('documentName')">Document Name</a></th>
                <th><a href="javascript:doSort('fileName')">File Name</a></th>
                <th><a href="javascript:doSort('fileSize')">File Size</a></th>
                <th><a href="javascript:doSort('operation')">Operation</a></th>
                <th><a href="javascript:doSort('operationDate')">Operation Date</a></th>
                <c:if test="${scanForm.documentName eq 'SS LINE MASTER BL'}">
                    <th><a href="javascript:doSort('status')">Status</a></th>
                </c:if>
                <th><a href="javascript:doSort('comment')">Comments</a></th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:set var="zebra" value="odd"/>
            <c:forEach var="result" items="${scanForm.results}" varStatus="status">
                <tr class="${zebra}">
                    <td>${result.documentName}</td>
                    <td>${result.fileName}</td>
                    <td>${result.fileSize}</td>
                    <td>${result.operation}</td>
                    <td>${result.operationDate}</td>
                    <c:if test="${scanForm.documentName eq 'SS LINE MASTER BL'}">
                        <td class="receivedMasterStatus${status.index}">${result.status}</td>
                    </c:if>
                    <td>${str:splitter(result.comments, 75, '<br/>')}</td>
                    <td>
                        <img src="${path}/images/icons/preview.png"
                              title="View" onclick="viewDocument('${result.fileName}', '${result.fileLocation}/${result.fileName}')"/>
                        <c:choose>
                            <c:when test="${roleDuty.deleteAttachedDocuments eq 'true' && not empty results3}">
                               <img src="${path}/images/trash.png"
                                     title="Delete" onclick="deleteDocumentSop('${result.id}', '${result.fileName}', '${scanForm.documentName}')"/>
                            </c:when>
                            <c:otherwise>                                
                                <c:if test="${roleDuty.deleteAttachedDocuments eq 'true'}">
                                    <c:choose>
                                        <c:when test="${not empty scanForm.documentName}">
                                            <img src="${path}/images/trash.png"
                                         title="Delete" onclick="deleteDocumentOlyView('${result.id}', '${result.fileName}', '${result.documentName}')"/>
                                          </c:when>
                                        <c:otherwise>
                                           <img src="${path}/images/trash.png"
                                         title="Delete" onclick="deleteDocument('${result.id}', '${result.fileName}', '')"/>
                                         </c:otherwise>
                                    </c:choose>
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
</div>
              
            <c:if test="${not empty scanForm.importTransResults}">
                <div class="results-container">
                    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" id="filelist">
                    <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="result" items="${scanForm.importTransResults}" varStatus="status">
                                <tr class="${zebra}">
                                    <td>${result.documentName}</td>
                                    <td>${result.fileName}</td>
                                    <td>${result.fileSize}</td>
                                    <td>${result.operation}</td>
                                    <td>${result.operationDate}</td>
                                    <c:if test="${scanForm.documentName eq 'SS LINE MASTER BL'}">
                                        <td class="receivedMasterStatus${status.index}">${result.status}</td>
                                    </c:if>
                                    <td>${str:splitter(result.comments, 75, '<br/>')}</td>
                                    <td>
                                        <input type="hidden" name="bookingType" id="bookingType" value="T">
                                        <img src="${path}/images/icons/preview.png"
                                             title="View" onclick="viewDocument('${result.fileName}', '${result.fileLocation}/${result.fileName}')"/>
                                        <c:choose>
                                            <c:when test="${roleDuty.deleteAttachedDocuments eq 'true' && not empty results3}">
                                                <img src="${path}/images/trash.png"
                                                     title="Delete" onclick="deleteDocumentSop('${result.id}', '${result.fileName}', '${scanForm.documentName}')"/>
                                            </c:when>
                                            <c:otherwise>                                
                                                <c:if test="${roleDuty.deleteAttachedDocuments eq 'true'}">
                                                    <c:choose>
                                                        <c:when test="${not empty scanForm.documentName}">
                                                            <img src="${path}/images/trash.png"
                                                                 title="Delete" onclick="deleteDocumentOlyView('${result.id}', '${result.fileName}', '${result.documentName}')"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${path}/images/trash.png"
                                                                 title="Delete" onclick="deleteDocumentForTrans('${result.id}', '${result.fileName}', '','${scanForm.hiddenScreenName}')"/>
                                                        </c:otherwise>
                                                    </c:choose>
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
                </div>
            </c:if>
