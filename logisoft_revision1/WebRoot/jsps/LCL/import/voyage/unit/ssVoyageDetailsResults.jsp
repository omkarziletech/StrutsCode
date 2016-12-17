<%-- 
    Document   : ssVoyageDetailsResults
    Created on : Sep 26, 2015, 12:18:37 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table align="center" id="ssdetailTable"  cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc;">
    <div id="lclDetailHeadingDiv">
        <tr class="tableHeadingNew">
            <td width="99%" colspan="8">Voyage Details</td>
        </tr>
    </div>
</table>
<table align="center" id="ssdetailTable" width="100%" border="0" style="border:1px solid #dcdcdc;">
    <tr>
        <td>
            <table class="dataTable">
                <thead>
                    <tr>
                        <th>Carrier</th>
                        <th>Vessel</th>
                        <th>Origin Agent</th>
                        <th>Departure Pier</th>
                        <th>Arrival Pier/POD</th>
                        <th>ETD Sailing Date</th>
                        <th>ETA at POD</th>
                        <th>Stripped Date</th>
                        <th>Transit Days</th>
                        <th>Transit Mode</th>
                        <th>Docs Received</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody style="text-transform:uppercase;">
                <c:forEach var="ssDetail" items="${lclSsDetail}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'odd'}">
                            <c:set var="rowStyle" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td>${ssDetail.spAcctNo.accountName}</td>
                        <td>${ssDetail.spReferenceName}</td>
                        <td>
                            <span title="${ssDetail.lclSsHeader.lclUnitSsImportsList[0].originAcctNo.accountName}<br>${ssDetail.lclSsHeader.lclUnitSsImportsList[0].originAcctNo.accountno}">
                                ${ssDetail.lclSsHeader.lclUnitSsImportsList[0].originAcctNo.accountno}</span>
                        </td>
                        <td>${ssDetail.departure.unLocationCode}</td>
                        <td>${ssDetail.arrival.unLocationCode}</td>
                        <td>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="std" value="${ssDetail.std}"></fmt:formatDate>
                    ${std}
                    </td>
                    <td class="boldBlue">
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="etaPod" value="${ssDetail.sta}"></fmt:formatDate>
                    ${etaPod}
                    </td>
                    <td>
                        <fmt:parseDate value="${lclUnitWhse.destuffedDatetime}" pattern="yyyy-MM-dd HH:mm" var="strippedDate"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy"  value="${strippedDate}"></fmt:formatDate>
                    </td>
                    <td>${ssDetail.totalTTDays}</td>
                    <td>${ssDetail.transMode}</td>
                    <td>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="docReceived" value="${ssDetail.docReceived}"></fmt:formatDate>
                    ${docReceived}
                    </td>
                    <td>
                        <a href="${path}/lclSSHeaderRemarks.do?methodName=displayNotes&headerId=${ssDetail.lclSsHeader.id}&actions=show All&voyageNumber=${ssDetail.lclSsHeader.scheduleNo}&moduleName=LCL_IMP_VOYAGE" class="noteNo">
                            <img alt="notes" title="Voyage Notes" width="16" height="16" src="${path}/img/icons/e_contents_view.gif" class="notes" /></a>
                        <a style="cursor: pointer;" title="Edit Voyage Details" >
                            <img src="${path}/images/edit.png" width="16" height="16" onclick="editDetails('${path}', '${ssDetail.id}');"/>
                        </a>
                    </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>
