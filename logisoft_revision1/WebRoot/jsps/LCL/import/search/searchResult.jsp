<%-- 
    Document   : importSearchResult
    Created on : Feb 5, 2015, 6:40:37 PM
    Author     : palraj
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="table-banner green">
    <c:choose>
        <c:when test="${fn:length(searchResultList)>=1}">
            ${fn:length(searchResultList)} files found.
        </c:when>
        <c:otherwise>No file found.</c:otherwise>
    </c:choose>
</div>
<c:if test="${not empty searchResultList}">
    <div class="scrollable-table">
        <div>
            <div>
                <table id="result-table">
                    <thead>
                        <tr>
                    <c:if test="${template eq null || template.qu}"><th><div label="QU"/></th></c:if>
                    <c:if test="${template eq null || template.bk}"><th><div label="DR"/></th></c:if>
                    <c:if test="${template eq null || template.hz}"><th><div label="HZ"/></th></c:if>
                    <c:if test="${template eq null || template.edi}"><th><div label="EDI"/></th></c:if>
                    <th><div label="DR No"/></th>
                    <th><div label="Exp"/></th>
                    <c:if test="${template eq null || template.tr}"><th><div label="TR"/></th></c:if>
                    <c:if test="${template eq null || template.status}">
                        <th>
                            <div label="Status" onclick="doSortAscDesc('status')" class="underline"/>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.doc}"><th><div label="Doc"/></th></c:if>
                    <th><div label="ETA"/></th>
                    <th><div label="Dispo"/></th>
                    <c:if test="${template eq null || template.pcs}"><th><div label="PCS"/></th></c:if>
                    <c:if test="${template eq null || template.cube}"><th><div label="${lclSearchForm.commodity eq 'M' ? 'CBM' : 'CFT'}"/></th></c:if>
                    <c:if test="${template eq null || template.cube}"><th><div label="${lclSearchForm.commodity eq 'M' ? 'KGS' : 'LBS'}"/></th></c:if>
                    <c:if test="${template eq null || template.origin}">              
                        <th style="min-width: 60px">
                            <div label="Origin" onclick="doSortAscDesc('originUncode')" class="underline"></div>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.pol}">
                        <th style="min-width: 60px">
                            <div label="Pol" onclick="doSortAscDesc('polUncode')" class="underline"></div>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.pod}">
                        <th style="min-width: 60px">
                            <div label="Pod" onclick="doSortAscDesc('podUncode')" class="underline"></div>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.destination}">
                        <th style="min-width: 60px">
                            <div label="Destn" onclick="doSortAscDesc('destinationUncode')" class="underline"></div>
                        </th>
                    </c:if>
                    
                    <th><div label="Stripped Date"/></th>
                    
                    <th><div label="Pickup Date"/></th>
                    <c:if test="${template eq null || template.shipper}">
                        <th>
                            <div label="Shipper" onclick="doSortAscDesc('shipName')" class="underline"/>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.consignee}">                
                        <th>
                            <div label="Consig" onclick="doSortAscDesc('consName')" class="underline"/>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.billTm}">
                        <th>
                            <div label="Bill TM" onclick="doSortAscDesc('billingTerminal')" class="underline"/>
                        </th>
                    </c:if>
                    <c:if test="${template eq null || template.quoteBy}"><th><div label="Quote By"/></th></c:if>
                    <c:if test="${template eq null || template.bookedBy}"><th><div label="Booked By"/></th></c:if>
                    <c:if test="${template eq null || template.hotCodes}"><th><div label="HotCodes"/></th></c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="result" items="${searchResultList}" >
                        <tr class="${zebra}">
                        <c:if test="${template eq null || template.qu}"> <td>
                            <c:choose>
                                <c:when test="${not empty result.bookedBy && not empty result.quoteBy}">
                                    <span title="Quote"><img src="${path}/img/icons/orange_dot.png" alt="orange disc"/></span>
                                </c:when>
                                <c:when test="${result.state eq 'Q' && empty result.bookedBy && not empty result.quoteBy}">
                                    <c:choose>
                                        <c:when test="${result.quoteComplete}">
                                            <span title="Quote"><img src="${path}/img/icons/darkGreenDot.gif" alt="green disc"/></span>
                                        </c:when><c:otherwise>
                                            <span title="Quote"><img src="${path}/img/icons/lightBlue2.gif" alt="blue disc"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                            </c:choose>
                            </td>
                        </c:if>
                        <c:if test="${template eq null || template.bk}">
                            <td>
                            <c:choose>
                                <c:when test="${result.state eq 'B' || (result.state eq 'BL' && not empty result.bookedBy)}">
                                    <span title="Dock Receipt"><img src="${path}/img/icons/lightBlue2.gif" alt="green disc"/></span>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${result.state eq 'B'}">
                                        <span title="Dock Receipt"><img src="${path}/img/icons/reddot1.gif" alt="green disc"/></span></c:if>
                                </c:otherwise>
                            </c:choose>
                            </td>
                        </c:if>
                        <c:if test="${template eq null || template.hz}">
                            <td>
                            <c:if test="${result.hazmat ne null && result.hazmat eq true}">
                                <img src="${path}/img/icons/danger..png"  style="cursor:pointer" width="12" height="12" alt="Haz" title="Hazardous Information"/>
                            </c:if>
                            </td>
                        </c:if>

                        <c:if test="${template eq null || template.edi eq true}"><td></td></c:if>

                        <td>
                            <u>
                                <span class="link ${result.fileNumber eq fileNumber ? 'highlight-saffron' : ''}" 
                                      onclick="checkLock('${path}', '${result.fileNumber}', '${result.fileNumberId}', '${result.state}', 'Imports', '${result.transshipment}', 'LCLIMPTRANS');">
                                    IMP-${result.fileNumber}
                                </span>
                            </u>
                        </td>
                        <td>
                        <c:if test="${result.transshipment eq true}">
                            <c:choose>
                                <c:when test="${result.state eq 'B' || (result.state eq 'BL' && not empty result.bookedBy)}">
                                    <img src="${path}/img/icons/trigger.gif" align="middle" height="16" width="16" alt="Export"
                                         onclick="checkLock('${path}', '${result.fileNumber}', '${result.fileNumberId}', '${result.state}', 'Exports', '${result.transshipment}', 'LCLEXPTRANS');"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/trigger.gif" align="middle" height="16" width="16" alt="Export"/>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        </td>
                        <c:if test="${template eq null || template.tr}">
                            <td>
                                <span class="track" onclick="openTrackingPopUp('${path}', 'display', '${result.fileNumber}', '${result.fileNumberId}');">
                                    <img src="${path}/img/icons/transaction.gif" align="middle" height="10" width="10" alt="TR"/></span>
                            </td>            </c:if>
                        <c:if test="${template eq null || template.status}">
                            <td class="statusvalues">
                                <label id="Release">
                                    <c:choose>
                                        <c:when test="${result.state eq 'Q'}">
                                            <span style="vertical-align: top;height: 10px" id="statusQ" title="Quote">${result.state}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${result.status eq 'B'}">
                                                    <span style="height: 10px" title="Booking">${result.status}</span>
                                                </c:when>
                                                <c:when test="${result.status eq 'X'}">
                                                    <span style="height: 10px" title="Cargo Terminated">${result.status}</span>
                                                </c:when>
                                                <c:when test="${result.status eq 'RF'}">
                                                    <span style="height: 10px" title="Cargo Refused">${result.status}</span>
                                                </c:when>
                                                <c:when test="${result.status eq 'WU'}">
                                                    <span style="height: 10px" title="Warehouse Un-Verified">${result.status}</span>
                                                </c:when>
                                                <c:when test="${result.status eq 'WV'}">
                                                    <span style="height: 10px" title="Warehouse Verified">${result.status}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="height: 10px" title="Booking">${result.state}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${result.onHold}">
                                        <span style="color:red;font-weight: bold" title="On Hold" >,OH</span>
                                    </c:if>
                                    <c:if test="${result.pickedUpDateTime ne null}">
                                        <span style="height: 10px" title="Picked Up">,PU</span>
                                    </c:if>
                                </label>
                            </td></c:if>
                        <c:if test="${template eq null || template.doc}">
                            <td>
                                <span style="vertical-align:middle">${result.clientPwkReceived eq true ? 'Y' : 'N'}</span>
                            </td></c:if>    

                        <td>
                            ${result.eta}
                        </td>

                        <td>
                            <span style="cursor:pointer" title="${result.dispoDesc}">${result.dispoCode}</span>
                        </td>
                        <c:if test="${template eq null || template.pcs}">
                            <td>
                                ${result.piece}
                            </td></c:if>

                        <c:if test="${template eq null || template.cube}">
                            <td>
                                ${result.volume}
                            </td></c:if>

                        <c:if test="${template eq null || template.weight}">
                            <td>
                                ${result.weight}
                            </td></c:if>

                        <c:if test="${template eq null || template.origin}">
                            <td>
                                <span title="${result.originUncode}/${result.origin}">${result.originUncode}</span>
                            </td>
                        </c:if>

                        <c:if test="${template eq null || template.pol}">
                            <td>
                            <c:if test="${result.destinationUncode ne '007UN'}">
                                <span title="${result.polUncode}/${result.pol}">${result.polUncode}</span>
                            </c:if>
                            </td></c:if>

                        <c:if test="${template eq null || template.pod}"><td>
                            <c:if test="${result.destinationUncode ne '007UN'}">
                                <span title="${result.podUncode}/${result.pod}">${result.podUncode}</span>
                            </c:if>
                            </td></c:if>

                        <c:if test="${template eq null || template.destination}">
                            <td>
                            <c:choose>
                                <c:when test="${result.pickup && not empty result.pickupCity}">
                                    <span  style="color:red;" title="Destination=${result.destinationUncode}/${result.destination} <br>Door Delivery=${fn:substring(result.pickupCity,fn:indexOf(result.pickupCity,'-')+1,fn:indexOf(result.pickupCity,'length-1'))}">${result.destinationUncode}</span>
                                </c:when>
                                <c:otherwise>
                                    <span title="${result.destinationUncode}/${result.destination}">${result.destinationUncode}</span>
                                </c:otherwise>  </c:choose>
                            </td></c:if>
                                    <td>
                                        ${result.strippedDate}
                                    </td>
                                    <td>
                            ${result.pickedUpDateTime}
                        </td>
                        <c:if test="${template eq null || template.shipper}">
                            <td>
                                <span class="hotspot" title="${result.shipName}/${result.shipNo}/${result.shipAddress}
                                      /${result.shipCity},${result.shipState},${result.shipZip}">${fn:substring(result.shipName,0,5)}</span>
                            </td>
                        </c:if>
                        <c:if test="${template eq null || template.consignee}">
                            <td>
                                <span title="${result.consName}/${result.consNo}/${result.consAddress}
                                      /${result.consCity},${result.consState},${result.consZip}">${fn:substring(result.consName,0,5)}</span>
                            </td></c:if>

                        <c:if test="${template eq null || template.billTm}">
                            <td>
                                <span title="${result.billingTerminal}">${result.billingTerminal}</span>
                            </td></c:if>

                        <c:if test="${template eq null || template.quoteBy}">
                            <td>
                                <span title="${fn:toUpperCase(result.quoteBy)}">${fn:toUpperCase(fn:substring(result.quoteBy,0,6))}</span>
                            </td></c:if>

                        <c:if test="${template eq null || template.bookedBy}">
                            <td>
                                <span title="${fn:toUpperCase(result.bookedBy)}">${fn:toUpperCase(fn:substring(result.bookedBy,0,6))}</span>
                            </td></c:if>

                        <c:if test="${template eq null || template.hotCodes}">
                            <td>
                                <span  title="${result.hotCodes}">${fn:substring(result.hotCodes,0,5)}</span>
                            </td></c:if>
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
</c:if>