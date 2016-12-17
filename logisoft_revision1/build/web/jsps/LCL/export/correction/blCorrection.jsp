<%-- 
    Document   : blCorrection
    Created on : Dec 30, 2015, 12:47:37 PM
    Author     : Mei
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/export/blCorrection.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">
            <cong:hidden id="fileNo" name="fileNo" />
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="fileId" name="fileId" />
            <cong:hidden id="correctionId" name="correctionId" />
            <cong:hidden id="noticeNo" name="noticeNo" />
            <cong:hidden id="blNo" name="blNo" />
            <cong:hidden id="issuingTerminal" name="issuingTerminal" value="${issuingTerminal}" />
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="75%">
                        List Of Correction for FileNo# <span style="color: red;">&nbsp;${lclCorrectionForm.fileNo}</span>
                    </td>
                    <td width="25%">
                        <input type="button" value="Add New" align="center" class="button-style1" onclick="addCorrection();"/>
                        <input type="button" value="View Void" align="center" class="${isVoidCorrection ? "green-background":"button-style1"}"
                               onclick="viewVoidCorrections('${path}');"/>
                        <input type="button" value="Notes" align="center" class="button-style1"
                               onclick="displayCorrectionNotes('${path}')"/>
                        <input type="button" value="Close" align="center" class="button-style1"
                               onclick="closeCorrection()"/>
                    </td>
                </tr>
            </table>
            <div class="table-banner green">
                <table>
                    <tr><td>
                            <c:choose>
                                <c:when test="${fn:length(correctionList)>=1}">
                                    ${fn:length(correctionList)} Corrections found.
                                </c:when>
                                <c:otherwise>No Corrections found.</c:otherwise>
                            </c:choose>
                        </td></tr>
                </table>
            </div>
            <table class="dataTable">
                <thead>
                    <tr>
                        <th>CN#</th>
                        <th>User</th>
                        <th>P</th>
                        <th>Sail Date</th>
                        <th>Notice Date</th>
                        <th>Current Profit</th>
                        <th>Profit After C/N</th>
                        <th>C/N Code</th>
                        <th>Approval</th>
                        <th>F</th>
                        <th>P</th>
                        <th>C-Type</th>
                        <th>Who Posted</th>
                        <th>Posted Date</th>
                        <th>Posted</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody style="text-transform: uppercase">
                    <c:forEach var="correction" items="${correctionList}">
                        <c:choose>
                            <c:when test="${zebra eq 'odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <td>${correction.noticeNo}</td>
                            <td>${correction.userName}</td>
                            <td>${correction.prepaidCollect}</td>
                            <td>${pickedVoyage.etaSailDate}</td>
                            <td>${correction.noticeDate}</td>

                            <td>${correction.currentProfit}</td>
                            <td>${correction.profitAfterCN}</td>
                            <td>${correction.correctionCode}</td>

                            <td style="color: green;font-weight: bold;">${correction.approval}</td>
                            <td></td>
                            <td></td>
                            <td>${correction.correctionType}</td>

                            <td>${correction.whoPosted}</td>
                            <td>${correction.postedDate}</td>
                            <td style="color: ${correction.posted eq 'Yes' ? 'green':'red'};font-weight: bold;">
                                ${correction.posted eq 'Yes' ? 'Yes':'No'}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${correction.status eq 'Q' || correction.status eq 'A'}">
                                        <img src="${path}/img/icons/container_obj.gif" border="0" title="View Correction"
                                             onclick="viewCorrection('${correction.correctionId}');"/>
                                        <c:if test="${not empty correction.postedDate}">
                                            <c:choose>
                                                <c:when test="${correction.creditDebit eq 'Y'}">
                                                    <img src="${path}/img/icons/dbcr.jpg" border="0" title="Credit/Debit Note"
                                                         onclick="PrintReportsOpenSeperately('${correction.correctionId}', '${correction.noticeNo}', '')"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img title="Corrected Freight Invoices" src="${path}/images/icons/preview.png"
                                                         onclick="PrintReportsOpenSeperately('${correction.correctionId}', '${correction.noticeNo}', '')"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge"
                                             width="13" height="13" alt="edit" title="Edit Correction" onclick="editCorrection('${correction.correctionId}');"/>
                                        <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"  title="Delete Correction"
                                             onclick="deleteCorrection('${correction.correctionId}', '${correction.noticeNo}', '${lclCorrectionForm.blNo}');"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </cong:form>
    </body>
</html>
