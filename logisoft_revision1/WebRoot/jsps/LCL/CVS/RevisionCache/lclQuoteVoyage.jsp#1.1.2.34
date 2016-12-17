<%@include file="/taglib.jsp" %>
<%@include file="init.jsp" %>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/lclQuoteVoyage.js"></cong:javascript>
<c:set var="fileNumber" value="${fileNumber}"/>
<body style="background:#ffffff">
    <div id="pane" style="overflow: auto;">
        <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
        <cong:hidden name="flag" id="flag" value=""/>
        <cong:hidden name="fileNumber" id="fileNumber" value="${fileNumber}"/>
        <input type="hidden" name="ssHeader" id="ssHeaderId"/>
        <input type="hidden" name="originLrd" id="originLrdId"/>
        <input type="hidden" name="fdEtaDate" id="fdEtaDateId"/>
        <input type="hidden" name="unknownDest" id="unknownDest" value="${unknownDest}"/>
        <input type="hidden" name="prevSailing" id="prevSailing" value="${prevSailing}"/>
        <cong:table styleClass="tableHeadingNew" width="100%" border="0" >
            <cong:tr>
                <cong:td styleClass="floatLeft tableHeadingNew" width="60%">
                    Upcoming Voyage for File No:
                    <span class="fileNo">
                        ${fileNumber}
                    </span>
                    <input type="checkbox" name="showOlder" id="showOlder" title="Show Older"
                            <c:if test="${prevSailing eq 'true'}">
                                checked = "checked"
                            </c:if>
                           style="vertical-align: middle;" onclick="showOlder('${path}', '${fileNumberId}', '${fileNumber}');"/>Show Older
                </cong:td>
                <cong:td width="35%">
                    
                </cong:td>
                <cong:td styleClass="floatRight tableHeadingNew" >
                    <div class="button-style1 floatRight" onclick="cancelConvertToBooking('${path}', '${fileNumberId}')">
                        Cancel
                    </div>
                </cong:td>
                <cong:td styleClass="floatRight tableHeadingNew" >
                    <div class="button-style1 floatRight" id="convertButton" onclick="validateConvertToBkg('${path}', '${fileNumberId}', 'B');">
                        Convert
                    </div>
                </cong:td>
            </cong:tr>
        </cong:table>
        <c:if test="${not empty voyageList}">
            <table width="100%" class="dataTable" style="border: 1px solid #dcdcdc">
                <thead>
                    <tr>
                        <th></th>
                        <th>Vessel</th>
                        <th>Line</th>
                        <th>ECI Voy</th>
                        <th>SSL Voy</th>
                        <th>Origin LRD</th>
                        <th>LOAD LRD</th>
                        <th>ETD at POL</th>
                        <th>Pier</th>
                        <th>ETA at POD</th>
                        <th>ETA FD</th>
                        <th>TT(POL-POD)</th>
                        <th>TT(POO-FD)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${voyageList}" var="voyage">
                        <c:choose>
                            <c:when test="${zebra=='odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${voyage.sailingDate}"/>
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="podEta" value="${voyage.podAtEta}"/>
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="fdEta" value="${voyage.fdEta}"/>
                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="pooLrdt" value="${voyage.originLrd}"/>
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="polLrdt" value="${voyage.loadLrd}"/>
                            <td>
                                <input type="radio" name="voyageRadio" id="voyageRadio"
                                       onclick="setVoyage('${voyage.ssHeaderId}','${pooLrdt}','${fdEta}');">
                            </td>
                            <td>${voyage.vesselName}</td>
                            <td style="text-transform: uppercase">${voyage.carrierName}</td>
                            <td>${voyage.voyageNo}</td>
                            <td>${voyage.ssVoyage}</td>
                            <td>${pooLrdt}</td>
                            <td>${polLrdt}</td>
                            <td>${polEtd}</td>
                            <td>${voyage.departPier}&nbsp;${voyage.scheduleK}</td>
                            <td>${podEta}</td>
                            <td>${fdEta}</td>
                            <td>${voyage.ttPolPod}</td>
                            <td>${voyage.ttPooFd}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</body>
