<%-- 
    Document   : ssMasterDisputedBlResults
    Created on : Mar 11, 2013, 2:44:23 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty ssMasterDisputedBlForm.results}">
        <div id="result-header" class="table-banner green">
            <div class="float-right">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${fn:length(ssMasterDisputedBlForm.results)>1}">
                            ${fn:length(ssMasterDisputedBlForm.results)} Disputed BLs found.
                        </c:when>
                        <c:otherwise>1 Disputed BL found.</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="scrollable-table">
            <div>
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th><div label="File Number" class="underline" onclick="doSort('file_no')"/></th>
                                <th><div label="Origin" class="underline" onclick="doSort('origin')"/></th>
                                <th><div label="Destination" class="underline" onclick="doSort('destination')"/></th>
                                <th><div label="POL" class="underline" onclick="doSort('pol')"/></th>
                                <th><div label="POD" class="underline" onclick="doSort('pod')"/></th>
                                <th><div label="SSL" class="underline" onclick="doSort('ssline_name')"/></th>
                                <th><div label="SSL BL" class="underline" onclick="doSort('ssline_bl')"/></th>
                                <th><div label="Status"/></th>
                                <th><div label="Date Disputed"/></th>
                                <th><div label="ETA" class="underline" onclick="doSort('eta')"/></th>
                                <th><div label="ETD" class="underline" onclick="doSort('etd')"/></th>
                                <th><div label="ACK" class="underline" onclick="doSort('ack_comments')"/></th>
                                <th><div label="Action"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="notesPath" value="${path}/notes.do"/>
                            <c:set var="notesPath" value="${notesPath}?moduleId=FILE"/>
                            <c:if test="${roleDuty.accessDisputedBlNotesAndAck}">
                                <c:set var="notesPath" value="${notesPath}&acknowledge=acknowledge"/>
                            </c:if>
                            <c:set var="documentsPath" value="${path}/document.do"/>
                            <c:set var="documentsPath" value="${documentsPath}?action=showDocuments"/>
                            <c:set var="documentsPath" value="${documentsPath}&screenName=FCLFILE"/>
                            <c:set var="documentsPath" value="${documentsPath}&documentName=SS LINE MASTER BL"/>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="file" items="${ssMasterDisputedBlForm.results}">
                                <tr class="${zebra}">
                                    <td>
                                        <a href="javascript:void(0);" onclick="openFile('${file.fileNumber}');">
                                            ${file.fileNumber}
                                        </a>
                                    </td>
                                    <td>
                                        <span title="${file.origin}">
                                            ${fn:substring(file.origin, fn:indexOf(file.origin, '(')+1, fn:indexOf(file.origin, ')'))}
                                        </span>
                                    </td>
                                    <td>
                                        <span title="${file.destination}">
                                            ${fn:substring(file.destination, fn:indexOf(file.destination, '(')+1, fn:indexOf(file.destination, ')'))}
                                        </span>
                                    </td>
                                    <td>
                                        <span title="${file.pol}">
                                            ${fn:substring(file.pol, fn:indexOf(file.pol, '(')+1, fn:indexOf(file.pol, ')'))}
                                        </span>
                                    </td>
                                    <td>
                                        <span title="${file.pod}">
                                            ${fn:substring(file.pod, fn:indexOf(file.pod, '(')+1, fn:indexOf(file.pod, ')'))}
                                        </span>
                                    </td>
                                    <td>${file.sslineName}</td>
                                    <td class="align-center">${fn:substring(file.sslineBl,0,1)}</td>
                                    <td>Disputed</td>
                                    <td>${file.dateOprDone}</td>
                                    <td>${file.eta}</td>
                                    <td>${file.etd}</td>
                                    <td class="align-center">
                                        <c:choose>
                                            <c:when test="${not empty file.ackComments}">
                                                <img title="${file.ackComments}" src="${path}/images/icons/dots/dark-green.gif"/>
                                            </c:when>
                                            <c:when test="${roleDuty.takeOwnershipOfDisputedBL}">
                                                <img title="Click here to take ownership" id="ackComments${file.fileNumber}"
                                                     src="${path}/images/icons/dots/red.gif" onclick="acknowledge('${file.fileNumber}')"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img title="Click here to take ownership" src="${path}/images/icons/dots/red.gif"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="align-center">
                                        <img title="Notes" class="image-16x16" src="${path}/images/icons/contents-view.gif"
                                             onclick="showNotes('${notesPath}&itemName=100017&moduleRefId=${file.fileNumber}')"/>
                                        <img title="Preview" class="image-16x16" src="${path}/images/icons/preview.png"
                                             onclick="showDocuments('${documentsPath}&documentId=${file.fileNumber}')"/>
                                    </td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
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
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-banner green" style="background-color: #D1E6EE;">No Disputed BLs found</div>
    </c:otherwise>
</c:choose>
