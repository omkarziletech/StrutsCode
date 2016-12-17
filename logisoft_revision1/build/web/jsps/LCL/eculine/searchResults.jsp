<%-- 
    Document   : searchResults
    Created on : Jul 24, 2013, 4:21:09 PM
    Author     : Rajesh
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="result-container">
    <c:choose>
        <c:when test="${not empty eculineEdiList}">
            <c:set var="totalRows" value="${fn:length(eculineEdiList)}"/>
            <div class="table-banner green">${totalRows} container${totalRows > 1? 's' : ''} found</div>
            <table class="display-table" style="min-width: 1270px; overflow: hidden;">
                <thead>
                    <tr>
                        <th>Container#</th>
                        <th>Sail Date</th>
                        <th>Arvl Date</th>
                        <th>Voy</th>
                        <th>Agent#</th>
                        <th>POL</th>
                        <th>POD</th>
                        <th>ECI Voy</th>
                        <th>Master BL</th>
                        <th>SS Line#</th>
                        <th>SCAC</th>
                        <th>Billing Terminal</th>
                        <th>Vessel</th>
                        <th>Size</th>
                        <th>Pieces</th>
                        <th>Cube</th>
                        <th>Weight</th>
                        <th>Ref#</th>
                        <th>Inv</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="eculineEdi" items="${eculineEdiList}" varStatus="status">
                        <tr class="${status.count % 2 eq 0 ? 'odd' : 'even'}">
                            <td>
                                <span class="link ${ unitNumber eq eculineEdi.containerNo && voyNumber eq eculineEdi.voyNo ? 'highlight-saffron' : ''}"
                                      onclick="getVoyageDetails('${path}', '${eculineEdi.id}', '${eculineEdi.voyNo}');">
                                    ${eculineEdi.containerNo}
                                </span>
                                <input type="hidden" id="containerNO${status.count}" value="${eculineEdi.containerNo}" />
                                <input type="hidden" id="eculineId${status.count}" value="${eculineEdi.id}" />
                                <input type="hidden" id="agentNo${status.count}" value="${eculineEdi.agentNo}" />
                                <input type="hidden" id="billingTerminal${status.count}" value="${eculineEdi.billingTerminal}" />
                                <input type="hidden" id="vesselCode${status.count}" value="${eculineEdi.vesselCode}" />
                                <input type="hidden" id="vesselErrorCheck${status.count}" value="${eculineEdi.vesselErrorCheck}" />
                                <input type="hidden" id="sslineNo${status.count}" value="${eculineEdi.sslineNo}" />
                                <input type="hidden" id="warehouseNo${status.count}" value="${eculineEdi.warehouseNo}" />
                                <input type="hidden" id="masterBl${status.count}" value="${eculineEdi.masterBl}" />
                                <input type="hidden" id="unitTypeId${status.count}" value="${eculineEdi.unitTypeId}" />
                                <input type="hidden" id="docReceived${status.count}" value="${eculineEdi.docReceived}" />
                                <input type="hidden" name="autoCostFlag" id="autoCostFlag"/>
                            </td>
                            <td><fmt:formatDate value="${eculineEdi.sailDate}" pattern="dd-MMM-yyyy"/></td>
                            <td><fmt:formatDate value="${eculineEdi.arvlDate}" pattern="dd-MMM-yyyy"/></td>
                            <td class="align-right">${eculineEdi.voyNo}</td>
                            <td>${eculineEdi.agentNo}</td>
                            <td><span id="origin${status.count}" title="${eculineEdi.origin}">${eculineEdi.polUncode}</span></td>
                            <td><span id="destn${status.count}" title="${eculineEdi.destination}">${eculineEdi.podUncode}</span></td>
                            <td>
                                <a href="javascript:void(0);" class="${unitId eq eculineEdi.unitId ? 'highlight-saffron' : ''}"
                                   onclick="openVoyage('${path}', '${eculineEdi.id}', '${eculineEdi.eciVoy}', '${eculineEdi.unitId}');">
                                    ${eculineEdi.scheduleNo}
                                </a>
                            </td>
                            <td>${eculineEdi.masterBl}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty eculineEdi.sslineNo}">
                                        <span title="${eculineEdi.sslineName}">${fn:substring(eculineEdi.sslineName, 0, 10)}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span  class="red" title="${eculineEdi.sslineName}">${fn:substring(eculineEdi.sslineName, 0, 10)}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <span class="green" title="${eculineEdi.scacCode}">${eculineEdi.scacCode}</span>
                            </td>
                            <td>${eculineEdi.billingTerminal}</td>
                            <td>
                                <span title="${eculineEdi.vesselName}" 
                                      class="${(not empty eculineEdi.vesselCode or eculineEdi.vesselErrorCheck) ? 'green' : 'red'}">
                                    ${fn:substring(eculineEdi.vesselName, 0, 10)}
                                </span>
                            </td>
                            <td>
                                <span title="${eculineEdi.contSize}" class="${not empty eculineEdi.unitTypeId ? 'green' : 'red'}">
                                    ${not empty eculineEdi.shortContSize ? eculineEdi.shortContSize : fn:substring(eculineEdi.contSize, 0, 5)}
                                </span>
                            </td>
                            <td class="align-right">${eculineEdi.pieces}</td>
                            <td class="align-right">${eculineEdi.cube}</td>
                            <td class="align-right">${eculineEdi.weight}</td>
                            <td>${eculineEdi.refNo}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${eculineEdi.invCount ne 0 and (eculineEdi.invCount - eculineEdi.invProcessed eq 0)}">
                                        <c:set var="invClass" value="green"/>
                                    </c:when>
                                    <c:when test="${eculineEdi.invCount ne 0 and (eculineEdi.invCount - eculineEdi.invProcessed ne eculineEdi.invCount)}">
                                        <c:set var="invClass" value="orange"/>
                                    </c:when>
                                    <c:when test="${eculineEdi.invCount ne 0 and (eculineEdi.invCount - eculineEdi.invProcessed eq eculineEdi.invCount)}">
                                        <c:set var="invClass" value="red"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="invClass" value="black"/>
                                    </c:otherwise>
                                </c:choose>
                                <span class="${invClass}">${eculineEdi.invCount}</span>
                            </td>
                            <td>
                                <img title="View XML file" src="${path}/images/icons/xml.png" onclick="viewXmlFile('${path}', '${eculineEdi.id}');"/>
                                <img title="Notes" src="${path}/images/icons/notes.png" class="notes"
                                     onclick="openNotes('${path}', '${eculineEdi.voyNo}', '${eculineEdi.containerNo}', 'containerNo');"/>
                                <c:choose>
                                    <c:when test="${eculineEdi.approved}">
                                        <img title="Approved Voy# ${eculineEdi.voyNo}" src="${path}/images/icons/check-green.png"/>
                                    </c:when>
                                    <c:when test="${eculineEdi.adjudicated}">
                                        <img title="Ready to Approve Voy# ${eculineEdi.voyNo}" src="${path}/images/icons/check-yellow.png"
                                             onclick="approveVoy('${eculineEdi.voyNo}', '${status.count}', '${eculineEdi.originId}', '${eculineEdi.destinationId}','${eculineEdi.warehouseNo}','${eculineEdi.unitTypeId}','${path}');"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img title="Not Ready to Approve Voy# ${eculineEdi.voyNo}" src="${path}/images/icons/check-gray.png"
                                             onclick="approveCheckVoy('${path}', '${eculineEdi.voyNo}', '${status.count}');"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="table-banner green">No containers found</div>
        </c:otherwise>
    </c:choose>
</div>