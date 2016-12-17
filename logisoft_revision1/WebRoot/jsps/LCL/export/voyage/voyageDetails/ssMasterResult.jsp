<%-- 
    Document   : ssMasterResult
    Created on : Jun 19, 2015, 5:23:19 PM
    Author     : Mei
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table align="center" id="ssmasterdetailTable"  cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc;">
    <tr id="lclDetailHeadingDiv">
        <td class="tableHeadingOrange">SS Master Details
            <div class="button-style1" id="addssmasterdetailsline"
                 style="float: right" onclick="addSSMasterDetails('${path}');">Add</div>
        </td>
    </tr>
    <tr>
        <td>
    <c:if test="${not empty lclSSMasterDetailsList}">
        <div id="divtablesty1" style="width: 100%;">
            <table class="dataTable">
                <thead>
                    <tr>
                        <th>Booking#</th>
                        <th>Contract#</th>
                        <th>P/C</th>
                        <th>Shipper Acct#</th>
                        <th>Consignee Acct#</th>
                        <th>Notify Acct#</th>
                        <th>EDI</th>
                        <th>EDI</th>
                        <th>Remarks</th>
                        <th>Received SSL Master</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="ssMaster" items="${lclSSMasterDetailsList}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'odd'}">
                            <c:set var="rowStyle" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td>${ssMaster.spBookingNo}</td>
                        <td>${ssMaster.spContractNo}</td>
                        <td title="${ssMaster.prepaidCollect eq 'P' ? 'Prepaid' :'Collect'}">${ssMaster.prepaidCollect}</td>
                        <td title="${ssMaster.shipSsContactId.tradingPartner.accountName}">
                            ${ssMaster.shipSsContactId.tradingPartner.accountno}
                        </td>
                        <td title="${ssMaster.consSsContactId.tradingPartner.accountName}">
                            ${ssMaster.consSsContactId.tradingPartner.accountno}
                        </td>
                        <td title="${ssMaster.notySsContactId.tradingPartner.accountName}">
                            ${ssMaster.notySsContactId.tradingPartner.accountno}
                        </td>
                        <td>${ssMaster.edi}</td>
                        <td>
                    <c:choose>
                        <c:when test="${ssMaster.statusSendEdi eq 'green'}">
                            <img src="${path}/images/icons/arrow/green.png" class="image-12x12 showEdi"
                                 onclick="viewEdiPopUp('${path}','${ssMaster.id}')"/>
                        </c:when>
                        <c:when test="${ssMaster.statusSendEdi eq 'yellow'}">
                            <img src="${path}/images/icons/arrow/yellow.png" class="image-12x12 showEdi"
                                 onclick="viewEdiPopUp('${path}','${ssMaster.id}')" />
                        </c:when>
                        <c:when test="${ssMaster.statusSendEdi eq 'red'}">
                            <img src="${path}/images/icons/arrow/red.png" class="image-12x12 showEdi"
                                 onclick="viewEdiPopUp('${path}','${ssMaster.id}')"/>
                        </c:when>
                    </c:choose>
                    </td>
                    <td>
                        <div style="width:300px; white-space: normal">
                            ${fn:replace(ssMaster.remarks,'\\N','<br/>')}
                        </div>
                    </td>
                    <td>
                        <label id="receivedMaster${ssMaster.spBookingNo}-${ssMaster.id}" style="color: green;font-weight: bold;">
                            ${ssMaster.masterBlApproveStatus}
                        </label>
                    </td>
                    <td>
                        <div  class="button-style1" id="notes" onclick="openHeaderNotes('${path}', '${lclssheader.id}', '${lclssheader.scheduleNo}')">
                            Notes
                        </div>
                        <div  class="button-style1" id="PrintFaxEmail" onclick="printreport('${ssMaster.id}', '', 'LCLSSMaster')">
                            Print/Fax/Email
                        </div>
                    <c:set var="ssMasterScanCss" value="${ssMaster.scanAttach ? 'green-background':'button-style1'}"/>
                    <input type="button" id="scan-Attach${ssMaster.spBookingNo}-${ssMaster.id}" value="Scan/Attach"  class="${ssMasterScanCss}"
                           onclick="scanAttach('${ssMaster.spBookingNo}', 'LCL SS MASTER BL', '${ssMaster.id}', '${ssMaster.masterBl}')"/>
                    <c:choose>
                        <c:when test="${ssMaster.statusSendEdi eq 'red'}">
                            <input type="button" class="buttonColorRed sendEdi${index}" value="Send EDI"
                                   onclick="sendEDI('${ssMaster.id}', '${ssMaster.spBookingNo}')"/>
                        </c:when>
                        <c:when test="${ssMaster.statusSendEdi eq 'green'}">
                            <input type="button" class="button-style1green sendEdi${index}" value="Send EDI"
                                   onclick="sendEDI('${ssMaster.id}', '${ssMaster.spBookingNo}')"/>
                        </c:when>

                        <c:when test="${ssMaster.statusSendEdi eq 'yellow'}">
                            <input type="button" class="button-style1yellow sendEdi${index}" value="Send EDI"
                                   onclick="sendEDI('${ssMaster.id}', '${ssMaster.spBookingNo}')"/>
                        </c:when>
                        <c:otherwise>
                            <input type="button" class="button-style1 sendEdi${index}" value="Send EDI"
                                   onclick="sendEDI('${ssMaster.id}', '${ssMaster.spBookingNo}')"/>
                        </c:otherwise>
                    </c:choose>
                    <img src="${path}/images/edit.png" width="16" height="16" title="Edit SS Master Detail" alt="SsMasterEditIcon"
                         onClick ="editSsMasterDetails('${path}', '${ssMaster.id}', '${destinationId}');"/>
                    <img src="${path}/images/error.png" width="16" height="16" title="Delete SS Master Detail" alt="deleteSsMasterIcon"
                         onclick="deleteSSMasterDetail('${ssMaster.id}')"/>
                    </td>
                    </tr>
                    </tbody>
                    <c:set var="index" value="${index+1}"/>
                </c:forEach>
            </table>
        </div>
        <div style="font-weight: bold;font-size: 12px;color: green">
            <c:choose>
                <c:when test="${fn:length(lclSSMasterDetailsList)>=1}">
                    ${fn:length(lclSSMasterDetailsList)} SS Master Displayed.
                </c:when>
                <c:otherwise>No Records Found.</c:otherwise>
            </c:choose>
        </div>
    </c:if>
</td>
</tr>
</table>
<br/>
