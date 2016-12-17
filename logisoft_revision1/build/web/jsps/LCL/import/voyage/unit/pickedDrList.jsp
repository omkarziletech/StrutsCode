<%-- 
    Document   : pickedDrList
    Created on : Sep 26, 2015, 12:17:55 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table id="bkgTable" cellpadding="0" cellspacing="0" width="100%" align="center" border="0" style="border:1px solid #dcdcdc">
    <tr class="tableHeadingNew" style="background-color:#F5D5BC">
        <td width="100%" colspan="8">Dock Receipt</td>
    </tr>
    <tr>
        <td>
            <div id="result-header" class="table-banner green">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${fn:length(pickedDrList)>1}">
                            ${fn:length(pickedDrList)} files found.
                        </c:when>
                        <c:otherwise>1 file found.</c:otherwise>
                    </c:choose>
                </div>
            </div>
            <table class="dataTable">
                <thead>
                    <tr>
                        <th>File No</th>
                        <th>Exp</th>
                        <th>Dispo</th>
                        <th>FinalDest</th>
                        <th>AMS/HBL #</th>
                        <th>SubHouse</th>
                        <th>PCS</th>
                        <th>CBM</th>
                        <th>KGS</th>
                        <th>Collect Chgs</th>
                        <th>Agent</th>
                        <th>Frt Rel</th>
                        <th>Miami Rel</th>
                        <th>DoorDel</th>
                        <th>ETA FD</th>
                        <th>Agt Chgs Rel</th>
                        <th>Agt Chgs Not</th>
                        <th>Shipper</th>
                        <th>Consig</th>
                        <th>Notify Party</th>
                        <th>Booked By</th>
                        <th>SU</th>
                    </tr>
                </thead>
                <tbody  style="text-transform: uppercase;">
                    <c:forEach var="pickedDr" items="${pickedDrList}">
                        <c:choose>
                            <c:when test="${rowStyle eq 'odd'}">
                                <c:set var="rowStyle" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rowStyle" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${rowStyle}">
                            <td>
                                <u>
                                    <span class="link ${lclAddVoyageForm.fileNumber eq pickedDr.fileNo ? 'highlight-saffron' :''}"
                                          onclick="clickImportBooking('${path}', '${pickedDr.fileId}', 'Imports', '${pickedDr.fileNo}');">
                                        IMP-${pickedDr.fileNo}
                                    </span>
                                </u>
                                <c:if test="${pickedDr.hazmat ne null && pickedDr.hazmat eq true}">
                                    <img src="${path}/img/icons/danger..png" style="cursor:pointer" 
                                         width="12" height="12" title="Hazardous Information"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${pickedDr.bookingType eq 'T'}">
                                    <img src="${path}/img/icons/trigger.gif" align="middle" height="16" width="16" alt="Export"
                                         onclick="clickImportBooking('${path}', '${pickedDr.fileId}', 'Exports', '${pickedDr.fileNo}');"/>
                                </c:if>
                            </td>
                            <td>
                                <span title="${lclunitSS.dispoDesc}">
                                    ${lclunitSS.dispoCode}
                                </span>
                            </td>
                            <td>
                                <span title="${pickedDr.fdName}">
                                    ${pickedDr.fdUnloc}
                                </span>
                                <c:if test="${pickedDr.segFileFlag}">
                                    <span style="cursor: pointer;color: brown;font-weight: bold" title="Segregation DR">&nbsp;&nbsp;&nbsp;S
                                    </span>
                                </c:if>
                                <c:if test="${not empty pickedDr.pickedUp}">
                                    <span style="cursor: pointer;color: darkblue;font-weight: bold" title="PickedUp">&nbsp;&nbsp;&nbsp;PU
                                    </span>
                                </c:if>
                            </td>
                            <td>
                                ${not empty pickedDr.segAmsNo ? pickedDr.segAmsNo : pickedDr.defaultAms}
                                <c:if test="${not empty pickedDr.amsDetails}">
                                    <span title="<c:import url="/jsps/LCL/amsTooltip.jsp">
                                              <c:param name="amsDetails" value="${pickedDr.amsDetails}"/>
                                          </c:import>" >
                                        <img style="vertical-align: middle" alt="amsIcon"
                                             src="${path}/img/icons/greenstar.jpg" width="10" height="10"/>
                                    </span>
                                </c:if>
                            </td>
                            <td>
                                ${pickedDr.subhouseBl}
                            </td>
                            <td>${pickedDr.totalPiece}</td>
                            <td>${pickedDr.totalvolumeimperial}</td>
                            <td>${pickedDr.totalweightimperial}</td>
                            <td>${pickedDr.collectAmt}</td>
                            <td>
                                <span title="${pickedDr.originAgentName}<br/>${pickedDr.originAgentNo}">
                                    ${pickedDr.originAgentNo}
                                </span>
                            </td>
                            <td>
                                <span>
                                    <c:choose>
                                        <c:when test="${not empty pickedDr.cargohold}">
                                            <img src="${path}/images/icons/dots/red.gif" alt="green disc" title="Cargo On hold"/>
                                        </c:when>
                                        <c:when test="${not empty pickedDr.cargoorder}">
                                            <img src="${path}/img/icons/purple.gif" alt="green disc" title="Cargo in General Order"/>
                                        </c:when>
                                        <c:when test="${(not empty pickedDr.originalRecv || pickedDr.expressRelease) && not empty pickedDr.freightRel && not empty pickedDr.payRel}">
                                            <img src="${path}/img/icons/darkGreenDot.gif" alt="green disc"/>
                                        </c:when>
                                        <c:when test="${not empty pickedDr.originalRecv || not empty pickedDr.freightRel || not empty pickedDr.payRel}">
                                            <img src="${path}/img/icons/orange_dot.png" alt="green disc"/>
                                        </c:when>
                                    </c:choose>
                                </span>
                            <c:set var="balanceAmt" value="${pickedDr.amtTotal - pickedDr.paidAmt }"></c:set>
                            <c:choose>
                                <c:when test="${pickedDr.isPaymentType}">
                                    <span class=" ${pickedDr.totalArBalanceAmount ne '0.00' ? 'red':'green'}
                                              bold" style="vertical-align: middle;">$</span>
                                </c:when>
                                <c:when test="${not empty pickedDr.paidAmt && not empty pickedDr.amtTotal
                                                && pickedDr.paidAmt eq pickedDr.amtTotal}">
                                    <span class="green bold" style="vertical-align: middle;">$</span>
                                </c:when>
                                <c:when test="${null ne balanceAmt && null ne pickedDr.amtTotal
                                                    && ((pickedDr.amtTotal -balanceAmt) gt 0 ||
                                                    (balanceAmt - pickedDr.amtTotal gt 0))}">
                                    <span class="orange bold" style="vertical-align: middle;">$</span>
                                </c:when>
                            </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty pickedDr.cusrcRecv && not empty pickedDr.delivrecv && not empty pickedDr.relerecv}">
                                        <img src="${path}/img/icons/darkGreenDot.gif" alt="green disc"/>
                                    </c:when>
                                    <c:when test="${not empty pickedDr.cusrcRecv || not empty pickedDr.delivrecv || not empty pickedDr.relerecv}">
                                        <img src="${path}/img/icons/orange_dot.png" alt="green disc"/>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${not empty pickedDr.doorStatus}">
                                    <c:if test="${pickedDr.doorStatus eq 'P' || pickedDr.doorStatus eq 'O'}">
                                        <span style="color: red;font-weight: bold;font-size: 11px;">YES&nbsp;</span>
                                    </c:if>
                                    <c:if test="${pickedDr.doorStatus eq 'D' || pickedDr.doorStatus eq 'F'}">
                                        <span style="color: green;font-weight: bold;font-size: 11px;">YES&nbsp;</span>
                                    </c:if>
                                    <c:if test="${pickedDr.doorStatus eq 'P'}"><span title="Pending(Cargo at CFS )">P</span></c:if>
                                    <c:if test="${pickedDr.doorStatus eq 'O'}"><span title="Out For Delivery">O</span></c:if>
                                    <c:if test="${pickedDr.doorStatus eq 'D'}"><span title="Delivered">D</span></c:if>
                                    <c:if test="${pickedDr.doorStatus eq 'F'}"><span title="Final/Closed">F</span></c:if>
                                </c:if>
                            </td>
                            <td>${pickedDr.etafd}</td>
                            <td>
                                <c:if test="${not empty pickedDr.agentReleAmt}">
                                    <c:choose>
                                        <c:when test="${pickedDr.arInvoiceStatus eq 'AR' || pickedDr.arInvoiceStatus eq 'Open'}">
                                            <span style="font-weight: bold;" class="${pickedDr.arInvoiceStatus eq 'AR' ? "greenBoldArPost":"orangeBoldArOpen"}">
                                                ${pickedDr.agentReleAmt}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="blackBoldArempty">${pickedDr.agentReleAmt}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td>${pickedDr.agentNotReleAmt}</td>
                            <td>
                                <span title="${pickedDr.shipperName}<br/>${pickedDr.shipAcctNo}">
                                    ${fn:substring(pickedDr.shipperName,0,15)}</span>
                            </td>
                            <td>
                                <span title="${pickedDr.consigneeName}<br/>${pickedDr.consAcctNo}">
                                    ${fn:substring(pickedDr.consigneeName,0,15)}
                                </span>
                            </td>
                            <td>
                                <span title="${pickedDr.notifyName}<br/>${pickedDr.notyAcctNo}">
                                    ${fn:substring(pickedDr.notifyName,0,15)}
                                </span>
                            </td>
                            <td>
                                ${pickedDr.bookedBy}
                            </td>
                            <td>
                                <c:if test="${pickedDr.bookingType eq 'I' && dispoCode ne 'DATA'}">
                                    <img id="reportPreview" width="18" alt="report" height="18"
                                         src="${path}/img/icons/search_over.gif" border="0"
                                         title="Preview" onclick="viewDrReport('${pickedDr.fileId}', '${pickedDr.fileNo}', '${path}');"/>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>
