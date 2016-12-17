<%-- 
    Document   : blCorrectionResultPool
    Created on : Jan 11, 2016, 7:34:53 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/export/blCorrectionSearchPool.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/checkLock.js"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="searchFileNo" name="searchFileNo"/>
            <cong:hidden id="searchCorrectionCode" name="searchCorrectionCode"/>
            <cong:hidden id="searchDate" name="searchDate"/>
            <cong:hidden id="searchShipName" name="searchShipName"/>
            <cong:hidden id="searchShipperNo" name="searchShipperNo"/>
            <cong:hidden id="searchForwarderName" name="searchForwarderName"/>
            <cong:hidden id="searchForwarderNo" name="searchForwarderNo"/>
            <cong:hidden id="searchThirdPartyAcctName" name="searchThirdPartyAcctName"/>
            <cong:hidden id="searchThirdPartyAcctNo" name="searchThirdPartyAcctNo"/>
            <cong:hidden id="searchNoticeNo" name="searchNoticeNo"/>
            <cong:hidden id="searchPooName" name="searchPooName"/>
            <cong:hidden id="searchPooId" name="searchPooId"/>
            <cong:hidden id="searchPolName" name="searchPolName"/>
            <cong:hidden id="searchPolId" name="searchPolId"/>
            <cong:hidden id="searchPodName" name="searchPodName"/>
            <cong:hidden id="searchPodId" name="searchPodId"/>
            <cong:hidden id="searchFdName" name="searchFdName"/>
            <cong:hidden id="searchFdId" name="searchFdId"/>
            <cong:hidden id="searchCreatedBy" name="searchCreatedBy"/>
            <cong:hidden id="searchCreatedByUserId" name="searchCreatedByUserId"/>
            <cong:hidden id="searchApprovedBy" name="searchApprovedBy"/>
            <cong:hidden id="searchApproveByUserId" name="searchApproveByUserId"/>
            <cong:hidden id="filterBy" name="filterBy"/>
            <cong:hidden id="searchBlNo" name="searchBlNo"/>




            <cong:hidden id="correctionId" name="correctionId"/>
            <cong:hidden id="fileId" name="fileId"/>
            <cong:hidden id="blNo" name="blNo"/>
            <cong:hidden id="concatenatedBlNo" name="concatenatedBlNo"/>
            <cong:hidden id="notesBlNo" name="notesBlNo"/>
            <cong:hidden id="selectedMenu" name="selectedMenu"/>
            <cong:hidden id="buttonValue"  name="buttonValue"/>
            <cong:hidden id="noticeNo" name="noticeNo" />
            <cong:hidden id="screenName"  name="screenName"/>
            <cong:hidden id="shipperName"  name="shipperName"/>
            <cong:hidden name="shipperNo" id="shipperNo" />
            <cong:hidden name="forwarder" id="forwarder" />
            <cong:hidden name="forwarderNo" id="forwarderNo" />
            <cong:hidden name="thirdPartyAcctNo" id="thirdPartyAcctNo" />
            <cong:hidden name="thirdPartyName" id="thirdPartyName" />
            <cong:hidden name="dateFilter" id="dateFilter" />
            <cong:hidden name="origin" id="origin" />
            <cong:hidden name="portOfOriginId" id="portOfOriginId"/>
            <cong:hidden name="pol" id="pol"  />
            <cong:hidden name="polId" id="polId" />
            <cong:hidden name="pod" id="pod" />
            <cong:hidden name="podId"  id="podId"/>
            <cong:hidden name="destination" id="destination"/>
            <cong:hidden name="finalDestinationId" id="finalDestinationId"/>
            <cong:hidden name="createdBy" id="createdBy"/>
            <cong:hidden name="createdByUserId" id="createdByUserId"/>
            <cong:hidden name="approvedBy" id="approvedBy"/>
            <cong:hidden name="approvedByUserId" id="approvedByUserId"/>
            <cong:hidden name="userId"  id="userId"/>
            <cong:hidden name="filterBy" id="filterBy"/>
            <cong:hidden name="searchCorrectionCode" id="searchCorrectionCode"/>
            <cong:hidden name="searchShipperNo" id="searchShipperNo"/>
            <cong:hidden name="searchForwarderNo" id="searchForwarderNo"/>
            <cong:hidden name="searchThirdPartyAcctNo" id="searchThirdPartyAcctNo"/>
            <div id="pane" style="overflow: auto;">
                <input type="hidden" name="message" value="${message}"/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                    <tr class="tableHeadingNew">
                        <td width="99%">
                        </td>
                        <td width="1%">
                            <input type="button" value="Go Back" align="center" class="button-style1" onclick="goBackByPoolSearch();"/>
                        </td>
                    </tr>
                </table>
                <table class="dataTable">
                    <thead>
                        <tr>
                            <th>FileNo</th>
                            <th>BlNo</th>
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
                            <th>C-Type</th>
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
                                <td>${correction.fileNo}</td>
                                <td>${correction.blNo}</td>
                                <td>${correction.correctionNo}</td>
                                <td>${correction.createdByUser}</td>
                                <td>${correction.billingType}</td>
                                <td>${correction.sailDate}</td>
                                <td>${correction.createdByDate}</td>

                                <td>${correction.currentProfit}</td>
                                <td>${correction.profitAfterCN}</td>
                                <td title="${correction.correcCodeDesc}">${correction.correctionCode}</td>

                                <td style="color: green;font-weight: bold;">${correction.approvedByUser}</td>
                                <td></td>
                                <td  title="${correction.correcTypeDesc}">${correction.correctionType}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${disabledFilter ne 'D'}">
                                            <c:if test="${correction.corrStatus eq 'A' || correction.corrStatus eq 'Q'}">
                                                <img src="${path}/img/icons/container_obj.gif" border="0" title="View Correction"
                                                     onclick="viewBlSearchCorrection('${correction.correctionId}', '${correction.fileId}', '${correction.blNo}');"/>
                                            </c:if>
                                            <c:if test="${correction.corrStatus eq 'A' && empty correction.postedDate &&
                                                          correction.corrStatus ne 'Q'}">
                                                  <img src="${path}/img/icons/unapp.gif" border="0" id="disapprove${index}"
                                                       onclick="unApproveCorrection('${correction.correctionId}', '${correction.blNo}',
                                                           '${correction.fileId}', '${correction.correctionNo}')" title="Un Approve"/>
                                            </c:if>
                                            <c:if test="${correction.corrStatus ne 'A' && correction.corrStatus ne 'Q' &&
                                                          (correction.corrStatus eq 'O' && !correction.voidStatus)}">
                                                  <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge" width="13" height="13" alt="edit"
                                                       title="Edit" onclick="editPoolCorrection('${correction.correctionId}',
                                                           '${correction.fileId}','${correction.blNo}');"/>
                                            </c:if>
                                            <c:if test="${correction.corrStatus ne 'A' && correction.corrStatus ne 'Q' && (correction.corrStatus eq 'O'
                                                          && !correction.voidStatus)}">
                                                  <img src="${path}/img/icons/pa.gif" border="0"  title="Pending Approval" alt="Pending"
                                                       onclick="approvePendingCorrection('${path}', '${correction.blNo}', '${correction.correctionId}','${correction.fileId}', '${correction.correctionNo}')" />
                                            </c:if>
                                            <c:if test="${showAllFilter eq 'All' && correction.voidStatus}">
                                                <img src="${path}/img/icons/lockon.ico"   border="0" title="Disabled" />
                                            </c:if>
                                        </c:when>
                                        <c:when test="${correction.voidStatus}">
                                            <img src="${path}/img/icons/lockon.ico"   border="0" title="Disabled" />
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </div>
        </cong:form>
    </body>
</html>
